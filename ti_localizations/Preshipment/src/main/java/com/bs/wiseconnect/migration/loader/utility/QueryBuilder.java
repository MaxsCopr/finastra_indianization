package com.bs.wiseconnect.migration.loader.utility;

import com.bs.wiseconnect.migration.loader.data.handler.TFBuyerFinHandler;
import com.bs.wiseconnect.migration.loader.data.handler.TFinvdtoHandler;
import com.bs.wiseconnect.migration.loader.db.connection.WiseconnectDB;
import com.bs.wiseconnect.migration.loader.tiplus.pojos.InvoiceCustomer;
import com.bs.wiseconnect.migration.loader.tiplus.pojos.TFBuyFin;
import com.misys.tiplus2.services.control.ServiceRequest;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import javax.xml.bind.JAXBException;
import org.apache.commons.dbutils.BeanProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryBuilder
  extends BeanProcessor
{
  private static final Logger logger = LoggerFactory.getLogger(QueryBuilder.class);
 
  public static String getQuery(String table, String inputFile, String low, String high)
    throws IOException
  {
    String queryField = "INVOICEREF as invoiceref , BUYER as buyer , ISSUESDATE as issueddate , FACEAMOUNT as faceamount , STATUS as status , CUSTOMER as customer , INVOICENUMBER as invoiceNumber , BATCHID as batchid , SELLER as seller , SETTELEMENTDATE as settelementdate , MNEMONIC as mnemonic , BRANCH as branch , BEHALFOFBRANCH as behalfofbranch , THEIRREF as theirref , SOURCEBANKINGBUISNESS as sourcebankingbuisness , FACECCY as faceccy , PROGRAMME as programme , FLAGVAL as flag";
    String query;
    String query;
    if ((low == null) && (high == null))
    {
      query =
        " SELECT " + queryField + ", batchid as count" + " FROM " + table + " where STATUS = 'WAITING'";
    }
    else
    {
      String query;
      if (high == null) {
        query =
          " SELECT " + queryField + ", batchid as count" + " FROM " + table + " where batchid <=" + low + " AND STATUS = 'WAITING'";
      } else {
        query =
          " SELECT " + queryField + ", batchid as count" + " FROM " + table + " WHERE batchid >= " + low + " AND batchid <=" + high + " AND STATUS = 'WAITING'";
      }
    }
    logger.debug(query + "\n");
    return query;
  }
 
  public static String getQuery1(String table, String inputFile, String low, String high, String financeFlag, String batchid)
    throws IOException
  {
    Connection con = null;
    InputStream resource = QueryBuilder.class.getResourceAsStream("/BUYFIN.properties");
   

    Properties prop = new Properties();
   


    prop.load(resource);
    String queryField = "";
    String separater = "";
    Set<Object> keys = prop.keySet();
    for (Object key : keys)
    {
      String tifield = (String)key;
      String dbField = prop.getProperty(tifield);
      if ((dbField != null) && (dbField.trim().length() != 0))
      {
        queryField = queryField + separater + dbField + " as " + tifield;
        separater = " , ";
      }
    }
    Statement s2 = null;
    try
    {
      con = WiseconnectDB.getDBConnection();
      s2 = con.createStatement();
     
      String staginginsert = "insert into ETTDM_BUYFIN_STAGING(INVOICENUMBER,PROGRAMME,SELLER,BUYER,MNEMONIC,BRANCH,CUSTOMER,BEHALFOFBRANCH,FINANCECCY,RECEIVEDON,FINANCEDATE,OUTSTANDINGAMT,OUTSTANDINGAMTCCY,STATUS,THEIRREF,MASTERREF,MATURITYDATE,BATCHID) select INVOICENUMBER,PROGRAMME, SELLER,BUYER, MNEMONIC, BRANCH,CUSTOMER, BEHALFOFBRANCH,FINANCECCY,TO_CHAR(ISSUEDATE,'YYYY-MM-DD'),TO_CHAR(FINANCEDATE,'YYYY-MM-DD'),OUTSTANDINGAMT,OUTSTANDINGAMTCCY, 'WAITING', THEIRREF,MASTERREF,TO_CHAR(ISSUEDATE,'YYYY-MM-DD'),ETTDM_BUYFIN_BACTH_ID.nextval  FROM buyerfin_db_table_view where  INVOICENUMBER='" +
     
        financeFlag + "'";
      s2.executeQuery(staginginsert);
      s2.close();
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    String query;
    String query;
    if ((low == null) && (high == null))
    {
      query =
        " SELECT " + queryField + "  FROM " + "ETTDM_BUYFIN_STAGING" + " where STATUS = 'WAITING'";
    }
    else
    {
      String query;
      if (high == null) {
        query =
          " SELECT " + queryField + "  FROM " + "ETTDM_BUYFIN_STAGING" + " where batchid <=" + low + " AND STATUS = 'WAITING'";
      } else {
        query =
          " SELECT " + queryField + "  FROM " + "ETTDM_BUYFIN_STAGING" + " WHERE batchid >= " + low + " AND batchid <=" + high + " AND STATUS = 'WAITING'";
      }
    }
    logger.debug(query + "\n");
    return query;
  }
 
  public static String doTFBuyerFinQuery(String table, String inputFile, String lowRange, String highRange, String userName, String userTeam, String product, String invoiceno, String batchid)
    throws SQLException, IOException, JAXBException
  {
    Connection connection = null;
    Statement queryStatement = null;
    ResultSet resultSet = null;
    String xmlToPost = null;
    try
    {
      connection = WiseconnectDB.getDBConnection();
     
      queryStatement = connection.createStatement();
      String query = getQuery1(table, inputFile, lowRange, highRange, invoiceno, batchid);
      String finalResult = "Unable to Process";
     
      resultSet = queryStatement.executeQuery(query);
      BeanProcessor bp = new BeanProcessor();
      List<ServiceRequest> sRequestItems = new ArrayList();
     

      connection = WiseconnectDB.getDBConnection();
      queryStatement = connection.createStatement();
      ResultSet resultSet1 = null;
      String max = null;
      String last_max = "select nvl(max(sequence),0) from APISERVER";
      resultSet1 = queryStatement.executeQuery(last_max);
      while (resultSet1.next()) {
        max = resultSet1.getString(1);
      }
      int max1 = Integer.parseInt(max);
      int max2 = max1 + 1;
     
      int num = 0;
      int count = 0;
      while (resultSet.next()) {
        try
        {
          num++;
         

          ArrayList<TFBuyFin> list = new ArrayList();
          list.add((TFBuyFin)bp.toBean(resultSet, TFBuyFin.class));
         
          TFBuyerFinHandler tfBuyFinHandler = new TFBuyerFinHandler();
          if (num % 1 == 0)
          {
            xmlToPost = MessageUtil.xmlServiceRequest(
              sRequestItems, "TFBUYFIN", userName);
            logger.debug(xmlToPost);
           

            String api_stub = "insert into APISERVER (VERSION, SERVICE, OPERATION, REQUEST, STATUS, NODE, SEQUENCE) values (CAST(SYSDATE AS TIMESTAMP), 'TIBulk','Item',?, 'WAITING',1,?)";
           
            PreparedStatement pst = connection.prepareStatement(api_stub);
            pst.setString(1, xmlToPost);
            pst.setInt(2, max2);
            pst.executeUpdate();
            pst.close();
            max2++;
          }
          logger.debug("\n");
          sRequestItems = new ArrayList();
          finalResult = "Unable to Process";
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
      }
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      MigrationUtil.surrenderConnection(connection, resultSet,
        queryStatement);
    }
    return xmlToPost;
  }
 
  public static String doTFinvdtoQuery(String table, String inputFile, String lowRange, String highRange, String userName, String userTeam, String product, String financeFlag)
  {
    Connection connection = null;
    String xmlToPost = null;
    Statement queryStatement = null;
    ResultSet resultSet = null;
    try
    {
      connection = WiseconnectDB.getDBConnection();
      queryStatement = connection.createStatement();
      logger.info("****Generating Query****\n");
      logger.info(lowRange + "-" + "-" + highRange);
      String query = getQuery(table, inputFile, lowRange, highRange);
     


      resultSet = queryStatement.executeQuery(query);
      BeanProcessor bp = new BeanProcessor();
      List<ServiceRequest> sRequestItems = new ArrayList();
      String finalResult = "Unable to Process";
     

      int num = 0;
      int count = 0;
      while (resultSet.next()) {
        try
        {
          num++;
          count = resultSet.getInt("count");
         
          ArrayList<InvoiceCustomer> list = new ArrayList();
          list.add((InvoiceCustomer)bp.toBean(resultSet, InvoiceCustomer.class));
         
          TFinvdtoHandler tfinv = new TFinvdtoHandler();
          sRequestItems.add(tfinv
            .createTFinvdtoRequest(list, userName, userTeam, product, financeFlag));
          if (num % 1 == 0)
          {
            xmlToPost = MessageUtil.xmlServiceRequest(
              sRequestItems, "TFINVNEW", userName);
            logger.debug(xmlToPost);
           

            String insert_apiserver = "insert into apiserver(SEQUENCE,VERSION,SERVICE,OPERATION,REQUEST,STATUS) VALUES('" +
              lowRange + "',SYSDATE,'TI','TFINVNEW','" + xmlToPost + "','WAITING')";
           
            int status = queryStatement.executeUpdate(insert_apiserver);
            if (status != 0) {
              logger.info("No of invoice inserted " + status);
            } else {
              logger.info("Not inserted...");
            }
          }
        }
        catch (Exception e)
        {
          logger.debug("Exception Occured ->" + e.getMessage());
          e.printStackTrace();
        }
      }
    }
    catch (SQLException e1)
    {
      e1.printStackTrace();
     
      logger.debug("Exception Occured ->" + e1.getMessage());
    }
    catch (IOException e1)
    {
      e1.printStackTrace();
      logger.debug("Exception Occured ->" + e1.getMessage());
    }
    catch (Exception e)
    {
      logger.debug("Exception Occured ->" + e.getMessage());
    }
    finally
    {
      MigrationUtil.surrenderConnection(connection, resultSet,
        queryStatement);
    }
    return xmlToPost;
  }
 
  protected Object processColumn(ResultSet rs, int index, Class<?> propType)
    throws SQLException
  {
    logger.debug("Column Override ");
   
    String result = rs.getString(index);
   
    return result;
  }
}