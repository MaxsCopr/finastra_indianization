package com.in.fetchAccount.utility;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Currency;
import java.util.LinkedHashMap;
import java.util.StringTokenizer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

public class CommonMethods
{
  public static LinkedHashMap<String, String> TBProperties = new LinkedHashMap();
  private static Logger logger = Logger.getLogger(CommonMethods.class.getName());
 
  public String setErrorString(String errorString, String currentError)
  {
    if (errorString.length() > 0) {
      errorString = errorString + ",";
    }
    errorString = errorString + currentError;
    return errorString;
  }
 
  public int retrieveNoOfErrors(String errorString)
  {
    StringTokenizer stringTokenizer = new StringTokenizer(errorString, ",");
    int n = stringTokenizer.countTokens();
    return n;
  }
 
  public static boolean isNull(String value)
  {
    boolean result = false;
    if ((value == null) || (value.equalsIgnoreCase(""))) {
      result = true;
    }
    return result;
  }
 
  public static boolean isValidString(String string)
  {
    if ((string == null) || ("".equalsIgnoreCase(string)) || (string.isEmpty())) {
      return false;
    }
    return true;
  }
 
  public String getEmptyIfNull(Object sourceStr)
  {
    return convertIfNull(sourceStr, "");
  }
 
  public static String convertIfNull(Object sourceStr, Object toConvert)
  {
    return isNullValue(sourceStr) ? toConvert.toString() : sourceStr.toString();
  }
 
  public static boolean isNullValue(Object obj)
  {
    if (obj == null) {
      return true;
    }
    if ((obj instanceof String)) {
      return ((String)obj).trim().length() == 0;
    }
    if ((obj instanceof Collection)) {
      return ((Collection)obj).size() == 0;
    }
    return false;
  }
 
  public static String nullAndTrimString(String value)
  {
    if (value == null)
    {
      value = "";
      return value;
    }
    return value.trim();
  }
 
  public static String returnEmptyIfNull(String value)
  {
    try
    {
      if (value == null) {
        value = "";
      } else if ((value instanceof String)) {
        value = value.trim();
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return value;
  }
 
  public static String getErrorDesc(String errorCD, String screenId)
  {
    String errorMsg = "";
    Connection con = null;
    LoggableStatement pst = null;
    ResultSet rs = null;
    try
    {
      con = DBConnectionUtility.getZoneConnection();
     

      pst = new LoggableStatement(con,
        "SELECT ERROR_MSG FROM ETT_SCHEDULE_ERRORCODE WHERE ERROR_CODE=? AND SCREEN_ID=?");
      pst.setString(1, errorCD);
      pst.setString(2, screenId);
      logger.info(pst.getQueryString());
      rs = pst.executeQuery();
      while (rs.next()) {
        errorMsg = rs.getString("ERROR_MSG");
      }
      logger.info("Error msg is " + errorMsg);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    return errorMsg;
  }
 
  public static boolean isThisDateValid(String dateToValidate)
  {
    if (dateToValidate == null) {
      return false;
    }
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    sdf.setLenient(false);
    try
    {
      java.util.Date date = sdf.parse(dateToValidate);
      logger.info(date);
    }
    catch (ParseException e)
    {
      e.printStackTrace();
      logger.info("false");
      return false;
    }
    logger.info("true");
    return true;
  }
 
  public static String getUserID()
  {
    HttpSession session = ServletActionContext.getRequest().getSession();
    String userID = (String)session.getAttribute("USERID");
    return userID;
  }
 
  public static String getCurrentDate(String dateFormat)
  {
    SimpleDateFormat df = new SimpleDateFormat(dateFormat);
    String currDate = df.format(new java.util.Date());
    return currDate;
  }
 
  public static boolean findDouble(String value)
  {
    boolean result = false;
    try
    {
      double d = Double.parseDouble(value);
    }
    catch (NumberFormatException e)
    {
      return true;
    }
    return result;
  }
 
  public static boolean bitwiseEqualsWithCanonicalNaN(double x, double y)
  {
    return Double.doubleToLongBits(x) == Double.doubleToLongBits(y);
  }
 
  public static String getTIDateAddOneYear()
  {
    logger.info("Entering Method");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    String tiDate = null;
    try
    {
      CommonMethods commonMethods = null;
      commonMethods = new CommonMethods();
      con = DBConnectionUtility.getZoneConnection();
      String query = "SELECT TO_CHAR(TO_DATE(PROCDATE, 'dd-mm-yy')+365,'yyyy-mm-dd') as PROCDATE FROM dlyprccycl";
      pst = new LoggableStatement(con, query);
      logger.info(pst.getQueryString());
      rs = pst.executeQuery();
      while (rs.next()) {
        tiDate = rs.getString("PROCDATE").trim();
      }
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    logger.info("Exiting Method");
    return tiDate;
  }
 
  public static String removeComma(String value)
  {
    if (value == null)
    {
      value = "";
      return value;
    }
    return value.replace(",", "");
  }
 
  public static String getTiDateFormat(String date)
    throws ParseException
  {
    SimpleDateFormat tiFormat = new SimpleDateFormat("yyyy-MM-dd");
    DateFormat jsonFormat = new SimpleDateFormat("dd-MM-yyyy");
    if ((date != null) && (!date.equalsIgnoreCase("")))
    {
      java.util.Date d1 = jsonFormat.parse(date);
      date = tiFormat.format(d1);
    }
    else
    {
      date = "";
    }
    return date;
  }
 
  public static String returnZeroIfEmpty(String Value)
  {
    if (Value == "") {
      Value = "0.00";
    }
    return Value;
  }
 
  public static String getTISystemDate()
  {
    String tiCurrDate = null;
    ResultSet rs = null;
    Connection con = null;
    PreparedStatement ps = null;
   
    String query = "SELECT to_char(PROCDATE,'dd-mm-yyyy') as PROCDATE FROM DLYPRCCYCL";
    try
    {
      con = DBConnectionUtility.getZoneConnection();
      ps = con.prepareStatement(query);
      rs = ps.executeQuery();
      while (rs.next()) {
        tiCurrDate = rs.getString(1);
      }
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, ps, rs);
    }
    return tiCurrDate;
  }
 
  public static java.sql.Date getTISystemSqlDate()
  {
    java.sql.Date tiCurrDate = null;
    ResultSet rs = null;
    Connection con = null;
    PreparedStatement ps = null;
    String query = "SELECT to_char(PROCDATE,'yyyy-mm-dd') as PROCDATE FROM DLYPRCCYCL ";
    try
    {
      con = DBConnectionUtility.getZoneConnection();
      ps = con.prepareStatement(query);
      rs = ps.executeQuery();
      while (rs.next()) {
        tiCurrDate = rs.getDate(1);
      }
    }
    catch (SQLException e)
    {
      logger.error("SQL Exceptions! " + e.getMessage(), e);
      e.printStackTrace();
    }
    catch (Exception e)
    {
      logger.error("Exception! " + e.getMessage(), e);
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, ps, rs);
    }
    return tiCurrDate;
  }
 
  public static String getEquivalentINRAmount(String outputCcyCode, String regfrmtAmount, String regfrmtSpotRate)
  {
    String respAmount = "";
    try
    {
      BigDecimal value1 = new BigDecimal(regfrmtAmount);
      BigDecimal value2 = new BigDecimal(regfrmtSpotRate);
     
      BigDecimal multipliedValue = value1.multiply(value2);
     
      Currency ccyNameCode = Currency.getInstance(outputCcyCode);
      int precision = ccyNameCode.getDefaultFractionDigits();
      RoundingMode DEFAULT_ROUNDING = RoundingMode.HALF_EVEN;
     
      BigDecimal roundOffValue = null;
     
      roundOffValue = multipliedValue.setScale(precision, DEFAULT_ROUNDING);
     
      respAmount = String.valueOf(roundOffValue);
    }
    catch (Exception e)
    {
      System.out.println("Roundoff amount exception " + e.getMessage());
      e.printStackTrace();
    }
    return respAmount;
  }
 
  public static String getH24Time()
  {
    String result = "";
    java.util.Date today = Calendar.getInstance().getTime();
    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss.S");
    result = formatter.format(today);
    return result;
  }
 
  public static String convertToStringDateFormat(String value, String fromDateFormat, String toDateFormat)
  {
    try
    {
      SimpleDateFormat fromFormat = new SimpleDateFormat(fromDateFormat);
      SimpleDateFormat toFormat = new SimpleDateFormat(toDateFormat);
      java.util.Date date = fromFormat.parse(value);
      value = toFormat.format(date);
      System.out.println("Expected String Date Format Value : " + value);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return value;
  }
 
  public static Timestamp getSqlLocalDateTime()
  {
    java.util.Date date = new java.util.Date();
    long t = date.getTime();
    Timestamp sqlTimestamp = new Timestamp(t);
    return sqlTimestamp;
  }
 
  public static void getProperties()
  {
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    System.out.println(" Entering getProperties ");
    try
    {
      con = DBConnectionUtility.getWiseConnection();
      String query = "SELECT * FROM Bridgeproperties ";
      pst = con.prepareStatement(query);
      rs = pst.executeQuery();
      while (rs.next()) {
        TBProperties.put(rs.getString("key").trim(), rs.getString("value").trim());
      }
      System.out.println(" Size of Bridgeproperties From DB ---->" + TBProperties.size());
      System.out.println(" Entering getProperties ");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
  }
}