package in.co.localization.utility;
 
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
  public static Logger logger = Logger.getLogger("DateTimeUtil");
  public static java.util.Date getLocalDate()
  {
    return new java.util.Date();
  }
  public static String formatProcDate(String tiProcDate)
  {
    String resultDate = "";
    try
    {
      SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
      java.util.Date d1 = sdf.parse(tiProcDate);
      sdf.applyPattern("yyyy-MM-dd");
      resultDate = sdf.format(d1);
    }
    catch (ParseException e)
    {
      logger.info("Dete exceptions!!!" + e.getMessage());
      e.printStackTrace();
    }
    logger.info(resultDate);
    return resultDate;
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
  public static String getLocalTime()
  {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    return sdf.format(new java.util.Date());
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
    logger.info(dateOnly + "\t" + timeOnly);
    return dateOnly;
  }
  public static String getStringDateByDateInFormat(java.util.Date date, String dateFormat)
  {
    SimpleDateFormat sdf = null;
    String result = "";
    if (ValidationsUtil.isValidObject(date)) {
      try
      {
        sdf = new SimpleDateFormat(dateFormat);
        result = sdf.format(date);
      }
      catch (Exception e)
      {
        logger.error(e.getMessage(), e);
      }
    }
    return result;
  }
  public static String getCurrentTimeStamp()
  {
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh.mm.ss.FF a");
    java.util.Date now = new java.util.Date();
    String strDate = sdf.format(now);
    return strDate;
  }
  public static String getStringDateByXMLGregorianCalendar(XMLGregorianCalendar xmlGCDate)
  {
    String result = "";
    if (ValidationsUtil.isValidObject(xmlGCDate))
    {
      Calendar calendar = xmlGCDate.toGregorianCalendar();
      java.util.Date date = calendar.getTime();
      result = date.getTime();
    }
    return result;
  }
  public static String getStringLocalTime()
  {
    String result = new java.util.Date();
    java.util.Date today = Calendar.getInstance().getTime();
    SimpleDateFormat formatter = new SimpleDateFormat("HH.mm.ss.S");
    result = formatter.format(today);
    return result;
  }
  public static String getStringLocalTimeFi()
  {
    String result = new java.util.Date();
    java.util.Date today = Calendar.getInstance().getTime();
    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss.S");
    result = formatter.format(today);
    return result;
  }
  public static String getStringLocalTimeInFormat(String timeFormat)
  {
    String result = new java.util.Date();
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
  public static String getStringDateInFormat(String date, String dateFormat, String expectedFormat)
  {
    SimpleDateFormat sdf = null;
    String result = "";
    if (ValidationsUtil.isValidString(date)) {
      try
      {
        sdf = new SimpleDateFormat(dateFormat);
        java.util.Date aDate = sdf.parse(date);
        sdf = new SimpleDateFormat(expectedFormat);
        result = sdf.format(aDate);
      }
      catch (Exception e)
      {
        logger.error(e.getMessage(), e);
      }
    }
    return result;
  }
  public static String getStringDateForExtractionFileFormat(String date, String dateFormat, String expectedFormat)
  {
    SimpleDateFormat sdf = null;
    String result = "";
    if (ValidationsUtil.isValidString(date)) {
      try
      {
        String aResult = "";
        sdf = new SimpleDateFormat(dateFormat);
        java.util.Date aDate = sdf.parse(date);
        sdf = new SimpleDateFormat(expectedFormat);
        aResult = sdf.format(aDate);
        java.util.Date bDate = new java.util.Date();
        String bResult = getStringDateByDateInFormat(bDate, "HHmm");
        result = aResult.concat(bResult);
      }
      catch (Exception e)
      {
        logger.error(e.getMessage(), e);
      }
    }
    return result;
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

		  public static String getStringDateByXMLGregorianCalendar(XMLGregorianCalendar xmlGCDate, String dateFormat)

		  {

		    String result = "";

		    if (ValidationsUtil.isValidObject(xmlGCDate))

		    {

		      Calendar calendar = xmlGCDate.toGregorianCalendar();

		      java.util.Date date = calendar.getTime();

		      SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

		      result = sdf.format(date);

		    }

		    return result;

		  }

		  public static String getXMLGregorianCalendarByStringInFormat(XMLGregorianCalendar xmlGCDate, String dateFormat)

		  {

		    String result = "";

		    if (ValidationsUtil.isValidObject(xmlGCDate))

		    {

		      Calendar calendar = xmlGCDate.toGregorianCalendar();

		      java.util.Date date = calendar.getTime();

		      result = getStringDateByDateInFormat(date, dateFormat);

		    }

		    return result;

		  }

		  public static java.sql.Date getSqlLocalDate()

		  {

		    java.util.Date date = new java.util.Date();

		    long t = date.getTime();

		    java.sql.Date sqlDate = new java.sql.Date(t);

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

		      logger.error(e.getMessage(), e);

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
		    java.util.Date dt = date.toGregorianCalendar().getTime();
		    java.sql.Date sqlDt = new java.sql.Date(dt.getTime());
		    return sqlDt;
		  }
		  public static XMLGregorianCalendar getXmlGregorianDate(String dateString)
		  {
		    if ((dateString == null) || (dateString.equals(""))) {
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
		}