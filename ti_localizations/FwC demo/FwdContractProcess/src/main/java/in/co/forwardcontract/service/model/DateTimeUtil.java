package in.co.forwardcontract.service.model;

import in.co.forwardcontract.utility.DBConnectionUtility;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.log4j.Logger;

public class DateTimeUtil
{
  private static final Logger logger = Logger.getLogger(DateTimeUtil.class.getName());
 
  public static void main(String[] args)
    throws Exception
  {
    String dateStr = "2019-03-21";
   


    System.out.println(getSqlDateByStringDate(dateStr, "yyyy-MM-dd"));
  }
 
  public static String dateStrformatChange(String dateStr, String inpformat, String outFormat)
  {
    String result = "";
    try
    {
      DateFormat df = new SimpleDateFormat(inpformat);
      java.util.Date startDate = df.parse(dateStr);
      DateFormat df2 = new SimpleDateFormat(outFormat);
      String startDateString2 = df2.format(startDate);
     
      result = startDateString2;
    }
    catch (ParseException e)
    {
      logger.error("DateString conversion error!! " + e.getMessage());
      e.printStackTrace();
      result = dateStr;
    }
    return result;
  }
 
  public static java.util.Date getLocalDate()
  {
    return new java.util.Date();
  }
 
  public static java.util.Date getLocalTime()
  {
    Calendar cal = Calendar.getInstance();
    cal.setTime(new java.util.Date());
    return cal.getTime();
  }
 
  public static String getCurrentDate()
  {
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    java.util.Date date = new java.util.Date();
   
    return dateFormat.format(date);
  }
 
  public static String getLocalTime(String format)
  {
    SimpleDateFormat sdf = new SimpleDateFormat(format);
    return sdf.format(new java.util.Date());
  }
 
  public static String getTimeNow()
  {
    SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
    java.util.Date now = new java.util.Date();
    String strDate = sdf.format(now);
   
    return strDate;
  }
 
  public static java.util.Date getDateByStringDateInFormat(String dateStr, String dateFormat)
  {
    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
    java.util.Date date = null;
    try
    {
      date = sdf.parse(dateStr);
    }
    catch (ParseException e)
    {
      logger.error(e.getMessage(), e);
    }
    return date;
  }
 
  public static String getStringLocalDate(java.util.Date date)
  {
    String result = "";
   
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    result = formatter.format(date);
   
    return result;
  }
 
  public static String getStringLocalDate(java.util.Date date, String format)
  {
    String result = "";
    try
    {
      SimpleDateFormat formatter = new SimpleDateFormat(format);
      result = formatter.format(date);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return result;
  }
 
  public static String getStringYear(String outformat)
  {
    String result = "";
    java.util.Date today = Calendar.getInstance().getTime();
    SimpleDateFormat formatter = new SimpleDateFormat(outformat);
    result = formatter.format(today);
    return result;
  }
 
  public static String getStringLocalDate(String outformat)
  {
    String result = "";
    java.util.Date today = Calendar.getInstance().getTime();
    SimpleDateFormat formatter = new SimpleDateFormat(outformat);
    result = formatter.format(today);
    return result;
  }
 
  public static String getStringLocalDate()
  {
    String result = "";
    java.util.Date today = Calendar.getInstance().getTime();
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    result = formatter.format(today);
    return result;
  }
 
  public static String getStringLocalDateInFormat(String dateformat)
  {
    String result = "";
    DateFormat dateFormat = new SimpleDateFormat(dateformat);
    java.util.Date date = new java.util.Date();
    result = dateFormat.format(date);
    return result;
  }
 
  public static String getStringDateInFormatFromCalendar(String stringdateformat)
  {
    String result = "";
    java.util.Date today = Calendar.getInstance().getTime();
    SimpleDateFormat formatter = new SimpleDateFormat(stringdateformat);
    result = formatter.format(today);
    return result;
  }
 
  public static String getLocalTimeInSwiftFormat()
  {
    SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
    return sdf.format(new java.util.Date());
  }
 
  public static String getStringDate(String timesatmp, String inFormat, String outFormat)
  {
    String dateOnly = "";
   
    DateFormat df = new SimpleDateFormat(inFormat);
   
    java.util.Date dt = null;
    try
    {
      dt = df.parse(timesatmp);
     
      DateFormat dfmt = new SimpleDateFormat(outFormat);
      dateOnly = dfmt.format(dt);
    }
    catch (ParseException e)
    {
      logger.error("Formatter exceptions " + e.getMessage());
      e.printStackTrace();
    }
    return dateOnly;
  }
 
  public static String getStringTimestamp(String timesatmp, String inFormat, String outFormat)
  {
    String timeOnly = "";
   
    DateFormat df = new SimpleDateFormat(inFormat);
   
    java.util.Date dt = null;
    try
    {
      dt = df.parse(timesatmp);
     
      DateFormat Outdf = new SimpleDateFormat(outFormat);
      timeOnly = Outdf.format(dt);
    }
    catch (ParseException e)
    {
      logger.error("Formatter exceptions " + e.getMessage());
      e.printStackTrace();
    }
    return timeOnly;
  }
 
  public static String getStringDateFromTimestamp(String timesatmp)
  {
    String dateOnly = "";
    String fmt = "MM/dd/yyyy HH:mm:ss.SSS";
    DateFormat df = new SimpleDateFormat(fmt);
   
    java.util.Date dt = null;
    try
    {
      dt = df.parse(timesatmp);
    }
    catch (ParseException e)
    {
      e.printStackTrace();
    }
    DateFormat tdf = new SimpleDateFormat("HH:mm:ss a");
    DateFormat dfmt = new SimpleDateFormat("yyyy-MM-dd");
    String timeOnly = tdf.format(dt);
   
    dateOnly = dfmt.format(dt);
   
    logger.debug(dateOnly + "\t" + timeOnly);
    return dateOnly;
  }
 
  public static String getCurrentTimeStamp()
  {
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh.mm.ss.FF a");
    java.util.Date now = new java.util.Date();
    String strDate = sdf.format(now);
   
    return strDate;
  }
 
  public static String getCurrentTimeStamp2()
  {
    SimpleDateFormat sdf = new SimpleDateFormat("HHmmssMs");
    java.util.Date now = new java.util.Date();
    String strDate = sdf.format(now);
   
    return strDate;
  }
 
  public static String getCurrentTimeStamp3()
  {
    int unique_id = (int)(new java.util.Date().getTime() / 1000L % 2147483647L);
   
    String str = String.valueOf(unique_id);
    return str;
  }
 
  public static Timestamp getConvToTimeStamp(String inputStr)
  {
    Timestamp timestampRes = null;
    try
    {
      if ((inputStr != null) && (!inputStr.isEmpty())) {
        timestampRes = Timestamp.valueOf(inputStr);
      }
    }
    catch (Exception e)
    {
      logger.error("Exception " + e.getMessage());
      e.printStackTrace();
    }
    return timestampRes;
  }
 
  public static String getStringLocalTime()
  {
    String result = "";
    java.util.Date today = Calendar.getInstance().getTime();
    SimpleDateFormat formatter = new SimpleDateFormat("HH.mm.ss.S");
    result = formatter.format(today);
    return result;
  }
 
  public static String getStringLocalTimeFi()
  {
    String result = "";
    java.util.Date today = Calendar.getInstance().getTime();
    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss.S");
    result = formatter.format(today);
    return result;
  }
 
  public static String getH24Time()
  {
    String result = "";
    java.util.Date today = Calendar.getInstance().getTime();
    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss.S");
    result = formatter.format(today);
    return result;
  }
 
  public static String getStringLocalTimeInFormat(String timeFormat)
  {
    String result = "";
    java.util.Date today = Calendar.getInstance().getTime();
    SimpleDateFormat formatter = new SimpleDateFormat(timeFormat);
    result = formatter.format(today);
    return result;
  }
 
  public static String getStringTimeZoneGMT()
  {
    Calendar c = Calendar.getInstance();
   
    TimeZone z = c.getTimeZone();
    int offset = z.getRawOffset();
    if (z.inDaylightTime(new java.util.Date())) {
      offset += z.getDSTSavings();
    }
    int offsetHrs = offset / 1000 / 60 / 60;
    int offsetMins = offset / 1000 / 60 % 60;
   

    c.add(11, -offsetHrs);
    c.add(12, -offsetMins);
   
    String formattedoffsetHrs = String.format("%02d", new Object[] { Integer.valueOf(offsetHrs) });
   
    String time = "GMT+" + formattedoffsetHrs + ":" + offsetMins;
   
    return time;
  }
 
  public static java.util.Date getDateLocalDateTime()
  {
    Calendar cal = Calendar.getInstance();
    cal.setTime(new java.util.Date());
    return cal.getTime();
  }
 
  public static String GetStringLocalDateTimeInFormat()
  {
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SS a Z");
   




    java.util.Date now = new java.util.Date();
    String strDate = sdf.format(now);
    return strDate;
  }
 
  public static String getStringEpochLocalDateTime()
  {
    String epochTime = "";
    long unixTime = System.currentTimeMillis() / 1000L;
    epochTime = String.valueOf(unixTime);
    return epochTime;
  }
 
  public static String getStringDateTimeFromEpoch(long epochFormatTime, String dateTimeFormat)
  {
    String dateTime = new SimpleDateFormat(dateTimeFormat)
      .format(new java.util.Date(epochFormatTime * 1000L));
    return dateTime;
  }
 
  public static String getCurrentDateAsTreasury()
  {
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    java.util.Date date = new java.util.Date();
   
    return dateFormat.format(date);
  }
 
  public static String getDateAsEndSystemFormat()
  {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    String date = sdf.format(new java.util.Date());
   
    return date;
  }
 
  public static String getFinSysTimestamp()
  {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    String date = sdf.format(new java.util.Date());
   
    return date;
  }
 
  public static Timestamp GetLocalTimeStamp()
  {
    java.util.Date date = new java.util.Date();
    Timestamp ts = new Timestamp(date.getTime());
    return ts;
  }
 
  public static Timestamp getTimestamp()
  {
    Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
   
    return timeStamp;
  }
 
  public static Timestamp getTimeStampByDateAndFormat(String dateStr, String dateFormat)
  {
    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
    java.util.Date date = null;
    Timestamp sqlTimestamp = null;
    try
    {
      date = sdf.parse(dateStr);
      long t = date.getTime();
      sqlTimestamp = new Timestamp(t);
    }
    catch (ParseException e)
    {
      logger.error("Exception! Check the logs for details", e);
    }
    return sqlTimestamp;
  }
 
  public static String getDateTimeChangeFormat(String date, String currFrmt, String ChngeFormt)
  {
    String frmtChngeDte = "";
    SimpleDateFormat dateCurrFormat = new SimpleDateFormat(currFrmt);
    SimpleDateFormat dateChngeFormat = new SimpleDateFormat(ChngeFormt);
    try
    {
      java.util.Date valueDate = dateCurrFormat.parse(date);
      frmtChngeDte = dateChngeFormat.format(valueDate);
    }
    catch (ParseException e)
    {
      e.printStackTrace();
    }
    return frmtChngeDte;
  }
 
  public static String getXmlGregorianCalendarLocalTimeToString()
    throws Exception
  {
    String result = "";
    Calendar calendar = getLocalDateInXMLGregorian().toGregorianCalendar();
    java.util.Date date = calendar.getTime();
    result = String.valueOf(date.getTime());
    return result;
  }
 
  public static XMLGregorianCalendar getLocalDateInXMLGregorian()
    throws Exception
  {
    XMLGregorianCalendar result = null;
    try
    {
      java.util.Date dateTime = getLocalDate();
      Calendar cal = Calendar.getInstance();
      cal.setTime(dateTime);
      int year = cal.get(1);
      int month = cal.get(2);
      int date = cal.get(5);
      int hour = cal.get(10);
      int minute = cal.get(12);
      int second = cal.get(13);
     
      int millisecond = cal.get(14);
     
      result = DatatypeFactory.newInstance().newXMLGregorianCalendar(year, month + 1, date, hour, minute, second,
        millisecond, -2147483648);
    }
    catch (Exception e)
    {
      logger.error(e.getMessage(), e);
      throw new Exception(e.getMessage());
    }
    return result;
  }
 
  public static XMLGregorianCalendar dateToXMLGregorianCalendarDate(java.util.Date date)
    throws ParseException, DatatypeConfigurationException
  {
    GregorianCalendar cal = new GregorianCalendar();
    cal.setTime(date);
    XMLGregorianCalendar xmlGC = DatatypeFactory.newInstance().newXMLGregorianCalendarDate(cal.get(1),
      cal.get(2) + 1, cal.get(5), -2147483648);
    return xmlGC;
  }
 
  public static XMLGregorianCalendar getDateInXMLGregorianByDate(java.util.Date dateToBeConvert)
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
     
      result = DatatypeFactory.newInstance().newXMLGregorianCalendarDate(year, month + 1, date,
        -2147483648);
    }
    catch (Exception e)
    {
      logger.error(e.getMessage(), e);
      throw new Exception(e.getMessage());
    }
    return result;
  }
 
  public static XMLGregorianCalendar getXmlGregorianDate(String dateString)
  {
    if ((dateString == null) || (dateString.isEmpty())) {
      return null;
    }
    DateFormat formatter = null;
    java.util.Date date = null;
    DatatypeFactory df = null;
    GregorianCalendar gc = new GregorianCalendar();
    if (dateString.charAt(4) == '-')
    {
      if (dateString.length() > 10) {
        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
      }
      formatter = new SimpleDateFormat("yyyy-MM-dd");
    }
    else if (dateString.charAt(4) == '|')
    {
      formatter = new SimpleDateFormat("yyyy|MMM|E");
    }
    else if (dateString.charAt(4) == '/')
    {
      formatter = new SimpleDateFormat("yyyy/MM/dd");
    }
    else if (dateString.charAt(2) == '/')
    {
      formatter = new SimpleDateFormat("dd/MM/yyyy");
    }
    else if (dateString.charAt(2) == '/')
    {
      formatter = new SimpleDateFormat("MM/dd/yyyy");
    }
    else if (dateString.charAt(2) == '-')
    {
      formatter = new SimpleDateFormat("dd-MMM-yy");
    }
    else
    {
      formatter = new SimpleDateFormat("yyyyMMdd");
    }
    try
    {
      date = formatter.parse(dateString);
     
      df = DatatypeFactory.newInstance();
     
      gc.setTimeInMillis(date.getTime());
    }
    catch (DatatypeConfigurationException e)
    {
      e.printStackTrace();
    }
    catch (ParseException e)
    {
      e.printStackTrace();
    }
    XMLGregorianCalendar resultGC = df.newXMLGregorianCalendar(gc);
   
    resultGC.setTime(-2147483648, -2147483648,
      -2147483648);
    return resultGC;
  }
 
  public static XMLGregorianCalendar toXMLGregorianCalendar(java.util.Date date)
  {
    GregorianCalendar gCalendar = new GregorianCalendar();
    gCalendar.setTime(date);
    XMLGregorianCalendar xmlCalendar = null;
    try
    {
      xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gCalendar);
    }
    catch (DatatypeConfigurationException localDatatypeConfigurationException) {}
    return xmlCalendar;
  }
 
  public static XMLGregorianCalendar getDateInXMLGregorianByStringDateInFormat(String dateString, String dateInFormat)
    throws Exception
  {
    XMLGregorianCalendar result = null;
    try
    {
      java.util.Date dateToBeConvert = getDateByStringDateInFormat(dateString, dateInFormat);
      result = getDateInXMLGregorianByDate(dateToBeConvert);
    }
    catch (Exception e)
    {
      logger.error(e.getMessage(), e);
      throw new Exception(e.getMessage());
    }
    return result;
  }
 
  public static XMLGregorianCalendar getDateTimeInXMLGregorianByDate(java.util.Date dateToBeConvert)
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
     
      result = DatatypeFactory.newInstance().newXMLGregorianCalendar(year, month + 1, date, hour, minute, second,
        -2147483648, -2147483648);
    }
    catch (Exception e)
    {
      logger.error(e.getMessage(), e);
      throw new Exception(e.getMessage());
    }
    return result;
  }
 
  public static XMLGregorianCalendar getLocalDateTimemilliInXMLGregorian()
    throws Exception
  {
    XMLGregorianCalendar result = null;
    try
    {
      java.util.Date dateTime = getLocalDate();
      Calendar cal = Calendar.getInstance();
      cal.setTime(dateTime);
      int year = cal.get(1);
      int month = cal.get(2);
      int date = cal.get(5);
      int hour = cal.get(10);
      int minute = cal.get(12);
      int second = cal.get(13);
     
      int millisecond = cal.get(14);
     
      result = DatatypeFactory.newInstance().newXMLGregorianCalendar(year, month + 1, date, hour, minute, second,
        millisecond, -2147483648);
    }
    catch (Exception e)
    {
      logger.error(e.getMessage(), e);
      throw new Exception(e.getMessage());
    }
    return result;
  }
 
  public static XMLGregorianCalendar getLocalDateTimeInXMLGregorian()
    throws Exception
  {
    XMLGregorianCalendar result = null;
    try
    {
      java.util.Date dateTime = getLocalDate();
      Calendar cal = Calendar.getInstance();
      cal.setTime(dateTime);
      int year = cal.get(1);
      int month = cal.get(2);
      int date = cal.get(5);
      int hour = cal.get(10);
      int minute = cal.get(12);
      int second = cal.get(13);
     

      result = DatatypeFactory.newInstance().newXMLGregorianCalendar(year, month + 1, date, hour, minute, second,
        -2147483648, -2147483648);
    }
    catch (Exception e)
    {
      logger.error(e.getMessage(), e);
      throw new Exception(e.getMessage());
    }
    return result;
  }
 
  public static XMLGregorianCalendar getXMLGregorianCalendarAsEndSystemFormat()
  {
    XMLGregorianCalendar gDateFormatted2 = null;
    try
    {
      Calendar cal = Calendar.getInstance();
     
      gDateFormatted2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal.get(1),
        cal.get(2) + 1, cal.get(5), cal.get(11),
        cal.get(12), cal.get(13), cal.get(14),
        -2147483648);
    }
    catch (DatatypeConfigurationException e)
    {
      e.printStackTrace();
    }
    return gDateFormatted2;
  }
 
  public static java.sql.Date getSqlLocalDate()
  {
    java.util.Date date = new java.util.Date();
    long t = date.getTime();
    java.sql.Date sqlDate = new java.sql.Date(t);
   
    return sqlDate;
  }
 
  public static java.sql.Date getSqlDateByStringDate(String dateStr, String dateFormat)
  {
    java.sql.Date sqlDate = null;
    try
    {
      if ((dateStr != null) && (!dateStr.isEmpty()))
      {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        java.util.Date date = sdf.parse(dateStr);
        long t = date.getTime();
        sqlDate = new java.sql.Date(t);
      }
      else
      {
        logger.info("InputDateString is null or empty");
      }
    }
    catch (ParseException e)
    {
      logger.error("Date parse exception " + e.getMessage());
      e.printStackTrace();
    }
    return sqlDate;
  }
 
  public static java.sql.Date getSqlDateByStringDateInFormat(String dateStr, String dateFormat)
  {
    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
    java.util.Date date = null;
    java.sql.Date sqlDate = null;
    try
    {
      date = sdf.parse(dateStr);
      long t = date.getTime();
      sqlDate = new java.sql.Date(t);
    }
    catch (ParseException e)
    {
      logger.error("Date convert exception " + e.getMessage());
      e.printStackTrace();
    }
    return sqlDate;
  }
 
  public static Timestamp getSqlLocalDateTime()
  {
    java.util.Date date = new java.util.Date();
    long t = date.getTime();
    Timestamp sqlTimestamp = new Timestamp(t);
    return sqlTimestamp;
  }
 
  public static Timestamp getSqlLocalTimestamp()
  {
    Calendar calendar = Calendar.getInstance();
    java.util.Date now = calendar.getTime();
    Timestamp currentTimestamp = new Timestamp(now.getTime());
   
    return currentTimestamp;
  }
 
  public static Timestamp getSqlTimeStampByStringDateInFormat(String dateStr, String dateFormat)
  {
    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
    java.util.Date date = null;
    Timestamp sqlTimestamp = null;
    try
    {
      date = sdf.parse(dateStr);
      long t = date.getTime();
      sqlTimestamp = new Timestamp(t);
    }
    catch (ParseException e)
    {
      logger.error(e.getMessage(), e);
    }
    return sqlTimestamp;
  }
 
  public static java.sql.Date getSqlDateByXMLGregorianCalendar(XMLGregorianCalendar date)
  {
    java.sql.Date sqlDt = null;
    try
    {
      if (date != null)
      {
        java.util.Date dt = date.toGregorianCalendar().getTime();
        sqlDt = new java.sql.Date(dt.getTime());
      }
    }
    catch (Exception e)
    {
      logger.error("Grgorian convertion exception " + e.getMessage());
      e.printStackTrace();
    }
    return sqlDt;
  }
 
  public static java.sql.Date getSqlDateByUtilDate(java.util.Date utilDate)
  {
    java.sql.Date sqlDate = null;
    try
    {
      if (utilDate != null)
      {
        sqlDate = new java.sql.Date(utilDate.getTime());
        logger.debug("Converted value of java.sql.Date : " + sqlDate);
      }
      else
      {
        logger.debug("java.util.Date utilDate is null");
      }
    }
    catch (Exception e)
    {
      logger.error("Exception " + e.getMessage());
      e.printStackTrace();
    }
    return sqlDate;
  }
 
  public static java.util.Date getUtilDateBySqlDate(java.sql.Date sqlDate)
  {
    java.util.Date utilDate = null;
    try
    {
      if (sqlDate != null)
      {
        utilDate = new java.util.Date(sqlDate.getTime());
        logger.debug("Converted value of java.util.Date : " + utilDate);
      }
      else
      {
        logger.debug("java.sql.Date utilDate is null");
      }
    }
    catch (Exception e)
    {
      logger.error("Exception " + e.getMessage());
      e.printStackTrace();
    }
    return utilDate;
  }
 
  public static String getTISystemDate()
  {
    String tiCurrDate = null;
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
        tiCurrDate = rs.getString(1);
      }
    }
    catch (SQLException e)
    {
      logger.error("SQL Exceptions! Fince_Pst Failed to insert. " + e.getMessage(), e);
      e.printStackTrace();
    }
    catch (Exception e)
    {
      logger.error("Exception! Fince_Pst Failed to insert " + e.getMessage(), e);
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, ps, rs);
    }
    return tiCurrDate;
  }
 
  public static java.sql.Date getDlyProcCyclDate()
  {
    java.sql.Date tiCurrDate = null;
   
    ResultSet rs = null;
    Connection con = null;
    PreparedStatement ps = null;
   
    String query = "SELECT PROCDATE FROM DLYPRCCYCL ";
    try
    {
      con = DBConnectionUtility.getZoneConnection();
      ps = con.prepareStatement(query);
      rs = ps.executeQuery();
      while (rs.next()) {
        tiCurrDate = rs.getDate("PROCDATE");
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
 
  public static java.sql.Date getTISystemValueDate()
  {
    java.sql.Date tiCurrDate = null;
    ResultSet rs = null;
    Connection con = null;
    PreparedStatement ps = null;
   

    String query = "SELECT to_date(PROCDATE,'dd-MON-yy') as PROCDATE FROM DLYPRCCYCL ";
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
 
  public static int dateDiff(String fromDate, String toDate)
  {
    SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
   
    java.util.Date d1 = null;
    java.util.Date d2 = null;
    try
    {
      d1 = format.parse(fromDate);
      d2 = format.parse(toDate);
    }
    catch (ParseException e)
    {
      e.printStackTrace();
    }
    long diff1 = d1.compareTo(d2);
    System.out.println("From date is small>>>>" + diff1);
   
    long diff2 = d2.compareTo(d1);
    System.out.println("From date is big>>>>" + diff2);
   
    return 1;
  }
 
  public static boolean isValidDate(String dateToValidate, String dateFromat)
  {
    boolean isvalid = true;
    if ((dateToValidate != null) && (!dateToValidate.isEmpty()))
    {
      SimpleDateFormat sdf = new SimpleDateFormat(dateFromat);
      sdf.setLenient(false);
      try
      {
        java.util.Date date = sdf.parse(dateToValidate);
        System.out.println(date);
        isvalid = true;
      }
      catch (ParseException e)
      {
        isvalid = false;
        System.out.println("ParseException " + e.getMessage());
        e.printStackTrace();
      }
    }
    else
    {
      isvalid = false;
    }
    return isvalid;
  }
}
