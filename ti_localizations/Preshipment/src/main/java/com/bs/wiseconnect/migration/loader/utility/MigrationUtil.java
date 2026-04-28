package com.bs.wiseconnect.migration.loader.utility;

import com.bs.wiseconnect.migration.loader.db.connection.WiseconnectDB;
import com.misys.tiplus2.services.control.ServiceResponse;
import com.misys.tiplus2.services.control.ServiceResponse.ResponseHeader;
import com.misys.tiplus2.services.control.StatusEnum;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
 
  public static void updateStagingStatus(String tableName, StatusEnum statusEnum, String originalMasterReference)
  {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    try
    {
      connection = WiseconnectDB.getDBConnection();
      preparedStatement = connection.prepareStatement(createupdateQuery(tableName));
      preparedStatement.setString(1, statusEnum.toString());
      preparedStatement.setString(2, originalMasterReference);
      preparedStatement.executeUpdate();
     
      logger.debug("*****Status updated for " + originalMasterReference + " as " + statusEnum.toString() + " in Staging Area*****");
    }
    catch (SQLException ex)
    {
      ex.printStackTrace();
    }
    finally
    {
      surrenderConnection(connection, null, preparedStatement);
    }
  }
 
  public static Boolean istokenValueNotNull(String tokenValue, Map<String, String> tokens, String tokenName)
  {
    if ((tokenValue != null) && (!tokenValue.equals(""))) {
      return Boolean.valueOf(true);
    }
    tokens.put(tokenName, "");
    return Boolean.valueOf(false);
  }
 
  public static void updateStaticDataStagingStatus(String tableName, StatusEnum statusEnum, String originalMasterReference)
  {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    try
    {
      connection = WiseconnectDB.getDBConnection();
      preparedStatement = connection.prepareStatement(createupdateQuery(tableName));
      preparedStatement.setString(1, statusEnum.toString());
      preparedStatement.setString(2, originalMasterReference);
      preparedStatement.executeQuery();
     
      logger.debug("*****Status updated for " + originalMasterReference + " as " + statusEnum.toString() + " in Staging Area*****");
    }
    catch (SQLException ex)
    {
      ex.printStackTrace();
    }
    finally
    {
      surrenderConnection(connection, null, preparedStatement);
    }
  }
 
  public static void updateMigrationDataStagingStatus(String tableName, StatusEnum statusEnum, String correlationID, String errorDtls)
  {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    try
    {
      connection = WiseconnectDB.getDBConnection();
      preparedStatement = connection.prepareStatement("UPDATE " + tableName + " SET STATUS = ?,ERRORDTLS=? WHERE TICORRID = ?");
      preparedStatement.setString(1, statusEnum.toString());
      preparedStatement.setString(2, errorDtls);
      preparedStatement.setString(3, correlationID.trim());
      preparedStatement.executeQuery();
     


      logger.debug("*****Status updated for " + correlationID + " as " + statusEnum.toString() + " in Staging Area*****");
    }
    catch (SQLException ex)
    {
      ex.printStackTrace();
    }
    finally
    {
      surrenderConnection(connection, null, preparedStatement);
    }
  }
 
  public static void updateClaimsStagingStatus(String tableName, StatusEnum statusEnum, String correlationID, String errorDtls, String relatedReference, String logID)
  {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    try
    {
      connection = WiseconnectDB.getDBConnection();
      preparedStatement = connection.prepareStatement("UPDATE " + tableName + " SET STATUS = ?,ERRORDTLS=? WHERE LOGID=?");
      preparedStatement.setString(1, statusEnum.toString());
      preparedStatement.setString(2, errorDtls);
      preparedStatement.setString(3, logID);
     
      preparedStatement.executeQuery();
     


      logger.debug("*****Status updated for " + correlationID + " as " + statusEnum.toString() + " in Staging Area*****");
    }
    catch (SQLException ex)
    {
      ex.printStackTrace();
    }
    finally
    {
      surrenderConnection(connection, null, preparedStatement);
    }
  }
 
  public static String createupdateQuery(String tableName)
  {
    String finalQuery = "";
    if (tableName.equals("FGBDM_ILC_STAGING")) {
      return finalQuery = "UPDATE " + tableName + " SET STATUS = ? WHERE ORIGINALMASTERREFERENCE = ?";
    }
    if (tableName.equals("FGBDM_ELC_STAGING")) {
      return finalQuery = "UPDATE " + tableName + " SET STATUS = ? WHERE ORIGINALMASTERREFERENCE = ?";
    }
    if (tableName.equals("FGBDM_IMPGTY_STAGING")) {
      return finalQuery = "UPDATE " + tableName + " SET STATUS = ? WHERE ORIGINALMASTERREFERENCE = ?";
    }
    if (tableName.equals("FGBDM_INW_COL_STAGING")) {
      return finalQuery = "UPDATE " + tableName + " SET STATUS = ? WHERE MASTERREFERENCE = ?";
    }
    if (tableName.equals("FGBDM_OUTW_COL_STAGING")) {
      return finalQuery = "UPDATE " + tableName + " SET STATUS = ? WHERE MASTERREFERENCE = ?";
    }
    if (tableName.equals("FGBDM_ILC_OUTCLM_STAGING")) {
      return finalQuery = "UPDATE " + tableName + " SET STATUS = ? WHERE RELATEDREFERENCE = ?";
    }
    if (tableName.equals("FGBDM_ELCDOCPRN_STAGING")) {
      return finalQuery = "UPDATE " + tableName + " SET STATUS = ? WHERE RELATEDREFERENCE = ?";
    }
    if (tableName.equals("FGBDM_GUACLM_STAGING")) {
      return finalQuery = "UPDATE " + tableName + " SET STATUS = ? WHERE RELATEDREFERENCE = ?";
    }
    return finalQuery;
  }
 
  public static String getSourceBankingBusiness(String sourceSystem)
  {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    String sbb = "";
    try
    {
      connection = WiseconnectDB.getWiseconnectDBConnection();
      preparedStatement = connection.prepareStatement("SELECT MBE FROM SOURCESYSTEMLOOKUP WHERE SOURCESYSTEM = ?");
      preparedStatement.setString(1, sourceSystem);
     

      resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        sbb = resultSet.getString("MBE");
      }
    }
    catch (SQLException ex)
    {
      ex.printStackTrace();
    }
    finally
    {
      surrenderConnection(connection, resultSet, preparedStatement);
    }
    return sbb;
  }
 
  public static String getAnalysisCodeFromWiseconnect(String sourceSystem)
  {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    String analysisCode = "";
    try
    {
      connection = WiseconnectDB.getWiseconnectDBConnection();
      preparedStatement = connection.prepareStatement("SELECT ANALYSISCODE FROM SOURCESYSTEMLOOKUP WHERE SOURCESYSTEM = ?");
      preparedStatement.setString(1, sourceSystem);
     

      resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        analysisCode = resultSet.getString("ANALYSISCODE");
      }
    }
    catch (SQLException ex)
    {
      ex.printStackTrace();
    }
    finally
    {
      surrenderConnection(connection, resultSet, preparedStatement);
    }
    return analysisCode;
  }
 
  public static String getCustomerMnmFromMaster(String masterReference)
  {
    Connection connection = null;
    Statement preparedStatement = null;
    ResultSet resultSet = null;
    String customer = "";
    logger.info("Master reference ->" + masterReference);
    try
    {
      connection = WiseconnectDB.getTIPLUSConnection();
      preparedStatement = connection.createStatement();
      resultSet = preparedStatement.executeQuery("SELECT pricustmnm FROM master WHERE master_ref='" + masterReference + "' and takeon='Y' and status in ('LIV','EXP')");
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
      surrenderConnection(connection, resultSet, preparedStatement);
    }
    return customer;
  }
 
  public static String getBankingEntity(String sourceSystem)
  {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    String sbb = "";
    try
    {
      connection = WiseconnectDB.getWiseconnectDBConnection();
      preparedStatement = connection.prepareStatement("SELECT BRANCH FROM SOURCESYSTEMLOOKUP WHERE SOURCESYSTEM = ?");
      preparedStatement.setString(1, sourceSystem);
     

      resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        sbb = resultSet.getString("BRANCH");
      }
    }
    catch (SQLException ex)
    {
      ex.printStackTrace();
    }
    finally
    {
      surrenderConnection(connection, resultSet, preparedStatement);
    }
    return sbb;
  }
 
  public static String getCustomerType(String counterPartyType)
  {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    String custType = "";
    try
    {
      connection = WiseconnectDB.getWiseconnectDBConnection();
      preparedStatement = connection.prepareStatement("SELECT CUSTOMERTYPE FROM CUSTOMERTYPELOOKUP WHERE COUNTERPARTYTYPE = ? AND ZONE='UAE' AND BRANCH='BNK'");
      preparedStatement.setString(1, counterPartyType);
     

      resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        custType = resultSet.getString("CUSTOMERTYPE");
      }
    }
    catch (SQLException ex)
    {
      ex.printStackTrace();
    }
    finally
    {
      surrenderConnection(connection, resultSet, preparedStatement);
    }
    return custType;
  }
 
  public static String getAccountType(String catagoryCode)
  {
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    String accType = "";
    try
    {
      connection = WiseconnectDB.getWiseconnectDBConnection();
      preparedStatement = connection.prepareStatement("SELECT ACCOUNTTYPE FROM ACCOUNTTYPELOOKUP WHERE CATEGORYCODE = ?");
      preparedStatement.setString(1, catagoryCode);
     

      resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        accType = resultSet.getString("ACCOUNTTYPE");
      }
    }
    catch (SQLException ex)
    {
      ex.printStackTrace();
    }
    finally
    {
      surrenderConnection(connection, resultSet, preparedStatement);
    }
    return accType;
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
    Connection connection = null;
    Statement statement = null;
    ResultSet resultSet = null;
   
    String eventRef = "";
    try
    {
      connection = WiseconnectDB.getTIPLUSConnection();
     
      statement = connection.createStatement();
     
      resultSet = statement.executeQuery("SELECT A.REFNO_PFIX MASTER_PRD_KEY,B.REFNO_PFIX EVENT_PFIX,B.REFNO_SERL EVENT_SERL,A.ORIG_REF,B.THEIR_REF,b.amount FROM MASTER A,BASEEVENT B WHERE A.KEY97 = B.MASTER_KEY AND B.REFNO_PFIX ='" + eventPrefix + "' AND  A.MASTER_REF ='" + masterRef + "' AND B.THEIR_REF ='" + theirRef + "' and b.amount='" + claimAmount + "' order by b.refno_serl desc");
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
      surrenderConnection(connection, resultSet, statement);
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