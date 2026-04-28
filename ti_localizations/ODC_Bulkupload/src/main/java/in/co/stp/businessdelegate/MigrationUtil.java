package in.co.stp.businessdelegate;

import com.misys.tiplus2.services.control.ServiceResponse;
import com.misys.tiplus2.services.control.ServiceResponse.ResponseHeader;
import com.misys.tiplus2.services.control.StatusEnum;
import in.co.stp.dao.DBPropertiesLoader;
import in.co.stp.utility.DBConnectionUtility;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

public class MigrationUtil
{
  private static final Logger logger = LoggerFactory.getLogger(MigrationUtil.class);
 
  public static Boolean istokenValueNotNull(String tokenValue, Map<String, String> tokens, String tokenName)
  {
    if ((tokenValue != null) && (!tokenValue.equals(""))) {
      return Boolean.valueOf(true);
    }
    tokens.put(tokenName, "");
    return Boolean.valueOf(false);
  }
 
  public static void updateMigrationDataStagingStatus(String tableName, StatusEnum statusEnum, String correlationID, String errorDtls)
  {
    Connection con = null;
    PreparedStatement preparedStatement = null;
    try
    {
      con = DBConnectionUtility.getConnection();
      preparedStatement = con.prepareStatement("UPDATE " + tableName + " SET CRESTATUS = ?,CREERRORDTLS=? WHERE TICORRID = ?");
      preparedStatement.setString(1, statusEnum.toString());
      preparedStatement.setString(2, errorDtls);
      preparedStatement.setString(3, correlationID.trim());
      preparedStatement.executeQuery();
     
      logger.debug("UPDATE " + tableName + " SET CRESTATUS=? WHERE TICORRID=?");
     
      logger.debug("*****Status updated for " + correlationID + " as " + statusEnum.toString() + " in Staging Area*****");
    }
    catch (SQLException ex)
    {
      ex.printStackTrace();
    }
    finally
    {
      surrenderConnection(con, null, preparedStatement);
    }
  }
 
  public static void updateMigrationDataStagingPayStatus(String tableName, StatusEnum statusEnum, String correlationID, String errorDtls)
  {
    Connection con = null;
    PreparedStatement preparedStatement = null;
    try
    {
      con = DBConnectionUtility.getConnection();
      preparedStatement = con.prepareStatement("UPDATE " + tableName + " SET PAYSTATUS = ?,PAYERRORDTLS=? WHERE TICORRID = ?");
      preparedStatement.setString(1, statusEnum.toString());
      preparedStatement.setString(2, errorDtls);
      preparedStatement.setString(3, correlationID.trim());
      preparedStatement.executeQuery();
     
      logger.debug("UPDATE " + tableName + " SET PAYSTATUS=? WHERE TICORRID=?");
     
      logger.debug("*****Status updated for " + correlationID + " as " + statusEnum.toString() + " in Staging Area*****");
    }
    catch (SQLException ex)
    {
      ex.printStackTrace();
    }
    finally
    {
      surrenderConnection(con, null, preparedStatement);
    }
  }
 
  public static void updateMigrationDataStartingTimeColAcc(String tableName, String MasterReference)
    throws ParseException
  {
    Connection con = null;
    PreparedStatement preparedStatement = null;
    try
    {
      logger.info("GOING TO UPDATE TIME");
      con = DBConnectionUtility.getConnection();
     
      preparedStatement = con.prepareStatement("UPDATE " + tableName + " SET ACPSTARTTIME = ? WHERE TICORRID = ?");
      preparedStatement.setTimestamp(1, getCurrentTimeStamp());
      preparedStatement.setString(2, MasterReference);
      preparedStatement.executeQuery();
     

      logger.info("TIME UPDATED");
      logger.debug("*****Status updated for " + MasterReference);
    }
    catch (SQLException ex)
    {
      ex.printStackTrace();
    }
    finally
    {
      surrenderConnection(con, null, preparedStatement);
    }
  }
 
  private static Timestamp getCurrentTimeStamp()
  {
    Date today = new Date();
    return new Timestamp(today.getTime());
  }
 
  public static String getCustomerMnmFromMaster(String masterReference)
  {
    Connection con = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    String customer = "";
    logger.info("Master reference ->" + masterReference);
    try
    {
      con = DBConnectionUtility.getConnection();
      preparedStatement = con.prepareStatement("SELECT pricustmnm FROM master WHERE master_ref=? and takeon='Y' and status in ('LIV','EXP')");
      preparedStatement.setString(1, masterReference);
     
      resultSet = preparedStatement.executeQuery();
      while (resultSet.next())
      {
        logger.info("Inside Result set");
       
        customer = resultSet.getString("pricustmnm");
        if ((customer != null) && (!customer.equals(""))) {
          customer = customer.trim();
        }
      }
      logger.info("Customer from Table ->" + customer);
    }
    catch (SQLException ex)
    {
      ex.printStackTrace();
    }
    finally
    {
      surrenderConnection(con, resultSet, preparedStatement);
    }
    return customer;
  }
 
  public static Boolean istokenValueNull(String input)
  {
    if ((input != null) && (!input.equals(""))) {
      return Boolean.valueOf(true);
    }
    return Boolean.valueOf(false);
  }
 
  public static ServiceResponse.ResponseHeader processEJBClientResponse(String result)
  {
    StatusEnum statusEnum = StatusEnum.FAILED;
    ServiceResponse.ResponseHeader responseHeader = null;
    try
    {
      InputStream inStream = new ByteArrayInputStream(result.getBytes());
      JAXBContext context = JAXBInstanceInitialiser.getServiceResponseContext();
      Unmarshaller unmarshaller = context.createUnmarshaller();
      ServiceResponse response = (ServiceResponse)unmarshaller.unmarshal(inStream);
      responseHeader = response.getResponseHeader();
      statusEnum = responseHeader.getStatus();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return responseHeader;
  }
 
  public static List<ServiceResponse> processSingleResponse(String result)
  {
    List<ServiceResponse> responses = new ArrayList();
    try
    {
      InputStream inStream = new ByteArrayInputStream(result.getBytes());
      JAXBContext context = JAXBInstanceInitialiser.getServiceResponseContext();
      Unmarshaller unmarshaller = context.createUnmarshaller();
      ServiceResponse response = (ServiceResponse)unmarshaller.unmarshal(inStream);
      responses.add(response);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return responses;
  }
 
  public static XMLGregorianCalendar stringToGregorianConversion(String stringDate)
  {
    XMLGregorianCalendar s = null;
    try
    {
      String dateInString = stringDate;
      SimpleDateFormat formatter = new SimpleDateFormat(
        "dd/MM/yyyy HH:mm:ss");
     
      Date date = formatter.parse(dateInString);
     

      s = getDateInXMLGregorian(date);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return s;
  }
 
  public static long generateRandom(int length)
  {
    Random random = new Random();
    char[] digits = new char[length];
    digits[0] = ((char)(random.nextInt(9) + 49));
    for (int i = 1; i < length; i++) {
      digits[i] = ((char)(random.nextInt(10) + 48));
    }
    return Long.parseLong(new String(digits));
  }
 
  public static XMLGregorianCalendar getDateInXMLGregorian(Date dateToBeConvert)
    throws Exception
  {
    XMLGregorianCalendar result = null;
    try
    {
      Calendar cal = Calendar.getInstance();
      cal.setTime(dateToBeConvert);
      int year = cal.get(1);
      int month = cal.get(2);
      int date = cal.get(5);
      int hour = cal.get(10);
      int minute = cal.get(12);
      int second = cal.get(13);
     
      result = DatatypeFactory.newInstance().newXMLGregorianCalendar(
        year, month + 1, date, hour, minute, second,
        -2147483648,
        -2147483648);
    }
    catch (Exception e)
    {
      throw new Exception(e.getMessage());
    }
    return result;
  }
 
  public static String getEventRefKey(String masterRef, String theirRef, String eventPrefix, String claimAmount)
  {
    Connection con = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
   
    String eventRef = "";
    try
    {
      con = DBConnectionUtility.getConnection();
     
      statement = con.prepareStatement("SELECT A.REFNO_PFIX MASTER_PRD_KEY,B.REFNO_PFIX EVENT_PFIX,B.REFNO_SERL EVENT_SERL,A.ORIG_REF,B.THEIR_REF,b.amount FROM MASTER A,BASEEVENT B WHERE A.KEY97 = B.MASTER_KEY AND B.REFNO_PFIX =? AND  A.MASTER_REF =? AND B.THEIR_REF =? and b.amount=? order by b.refno_serl desc");
      statement.setString(1, eventPrefix);
      statement.setString(2, masterRef);
      statement.setString(3, theirRef);
      statement.setString(4, claimAmount);
     
      resultSet = statement.executeQuery();
      while (resultSet.next())
      {
        logger.info("Entering into Resultset");
       
        eventRef = eventPrefix + String.format("%03d", new Object[] { Integer.valueOf(Integer.parseInt(resultSet.getString("EVENT_SERL"))) });
      }
    }
    catch (SQLException ex)
    {
      ex.printStackTrace();
    }
    finally
    {
      surrenderConnection(con, resultSet, statement);
    }
    return eventRef;
  }
 
  public static void surrenderConnection(Connection connection, ResultSet resultSet, Statement preparedStatement)
  {
    if (connection != null) {
      try
      {
        connection.close();
      }
      catch (SQLException localSQLException) {}
    }
    if (resultSet != null) {
      try
      {
        resultSet.close();
      }
      catch (SQLException localSQLException1) {}
    }
    if (preparedStatement != null) {
      try
      {
        preparedStatement.close();
      }
      catch (SQLException localSQLException2) {}
    }
  }
 
  public static void main(String[] args)
    throws IOException, XPathExpressionException, ParserConfigurationException, SAXException
  {
    DBPropertiesLoader.initialize("sourcedb.properties");
   
    logger.info("Claim ID Generated ->" + getEventRefKey("1P13ILC001327", "1P13ILC001327", "CLM", "4127920"));
  }
}

