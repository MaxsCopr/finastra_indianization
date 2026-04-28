package in.co.FIRC.utility;

import in.co.FIRC.dao.AbstractDAO;
import in.co.FIRC.dao.exception.DAOException;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DBUtility
  extends AbstractDAO
{
  private static Logger logger = LogManager.getLogger(DBUtility.class.getName());
  static String string;
 
  public String getErrorDesc(String errorCD, String screenId)
    throws DAOException
  {
    String errorMag = "";
   

    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    try
    {
      con = getConnection();
      String query = "SELECT ERROR_MSG FROM ETT_ERROR_CODES WHERE ERROR_CODE=? AND SCREEN_ID=?";
      pst = con.prepareStatement(query);
      pst.setString(1, errorCD);
      pst.setString(2, screenId);
      rs = pst.executeQuery();
      while (rs.next())
      {
        errorMag = rs.getString(1);
        logger.info("errorMag" + errorMag);
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      logger.info("Finally Occurred in PROCESS");
    }
    return errorMag;
  }
 
  static String[] a = { "Zero", "One", "Two", "Three", "Four", "Five",
    "Six", "Seven", "Eight", "Nine" };
  static String[] b = { "Hundred", "Thousand", "Lakh", "Crore" };
  static String[] c = { "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen",
    "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Ninteen" };
  static String[] d = { "Twenty", "Thirty", "Fourty", "Fifty",
    "Sixty", "Seventy", "Eighty", "Ninty" };
 
  public static boolean isNull(Object obj)
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
 
  public static boolean isNotNull(Object obj)
  {
    return !isNull(obj);
  }
 
  public static String convertIfNull(Object sourceStr, Object toConvert)
  {
    return isNull(sourceStr) ? toConvert.toString() : sourceStr.toString();
  }
 
  public static String getEmptyIfNull(Object sourceStr)
  {
    return convertIfNull(sourceStr, "");
  }
 
  public static String getValueIfNull(Object sourceStr)
  {
    return convIfNull(sourceStr, "0");
  }
 
  public static String convIfNull(Object sourceStr, Object toConvert)
  {
    return isNull(sourceStr) ? toConvert.toString() : sourceStr.toString();
  }
 
  public static boolean isEqual(Object s1, Object s2)
  {
    if (((s1 instanceof String)) && ((s2 instanceof String))) {
      return getEmptyIfNull(s1).equals(s2);
    }
    return s1 == s2 ? true : (s1 == null) || (s2 == null) ? false : s1.equals(s2);
  }
 
  public static boolean isNotEqual(Object s1, Object s2)
  {
    return !isEqual(s1, s2);
  }
 
  public static int toInt(Object str)
  {
    return (int)toDouble(str);
  }
 
  public static long toLong(Object str)
  {
    return toDouble(str);
  }
 
  public static double toDouble(Object str)
  {
    double tempVal = 0.0D;
    if (isNull(str)) {
      return tempVal;
    }
    try
    {
      tempVal = Double.parseDouble(getEmptyIfNull(str).trim());
    }
    catch (NumberFormatException e)
    {
      tempVal = 0.0D;
    }
    return tempVal;
  }
 
  public static float toFloat(Object string)
  {
    float temp = 0.0F;
    if (isNull(string)) {
      return temp;
    }
    try
    {
      temp = Float.parseFloat(getEmptyIfNull(string).trim());
    }
    catch (NumberFormatException e)
    {
      temp = 0.0F;
    }
    return temp;
  }
 
  public static String roundDouble(double d, int places)
  {
    NumberFormat formatter = NumberFormat.getNumberInstance();
    formatter.setMaximumFractionDigits(places);
    formatter.setMinimumFractionDigits(places);
    return formatter.format(d);
  }
 
  public static String getCurrentDate(String userViewableFormat)
  {
    DateFormat dateFormat = new SimpleDateFormat(userViewableFormat);
    java.util.Date date = new java.util.Date();
    return dateFormat.format(date);
  }
 
  public static java.sql.Date getDateValue(String dateValue, String dateOnlyFormat)
    throws ParseException
  {
    if (isNull(dateValue)) {
      dateValue = getCurrentDate(dateOnlyFormat);
    }
    java.sql.Date date = null;
    SimpleDateFormat dateFormat = new SimpleDateFormat(dateOnlyFormat);
    try
    {
      date = new java.sql.Date(dateFormat.parse(dateValue).getTime());
      return date == null ? new java.sql.Date(new java.util.Date().getTime()) : date;
    }
    catch (ParseException e)
    {
      throw e;
    }
  }
 
  public static String convertObjectToDate(Object dateValue, String dateOnlyFormat)
    throws Exception
  {
    if (isNotNull(dateValue)) {
      return getUserviewableDateFormat(getDateValue(dateValue.toString(), dateOnlyFormat));
    }
    return "";
  }
 
  public static String getUserviewableDateFormat(java.sql.Date dateValue)
    throws Exception
  {
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    try
    {
      String formatValue = "";
      if (isNotNull(dateValue)) {}
      return formatter.format(dateValue);
    }
    catch (Exception e)
    {
      throw e;
    }
  }
 
  public static String getUserviewableDateTimeFormat(Timestamp dateTimeValue)
    throws Exception
  {
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    try
    {
      String formatValue = "";
      if (isNotNull(dateTimeValue)) {}
      return formatter.format(dateTimeValue);
    }
    catch (Exception e)
    {
      throw e;
    }
  }
 
  public static int getDayOfWeek(String year, String month, String date)
  {
    Calendar calendar = new GregorianCalendar();
    calendar.set(toInt(year), toInt(month) - 1, toInt(date), 0, 0, 0);
    calendar.setTime(calendar.getTime());
    int day = calendar.get(7);
    return day;
  }
 
  public static java.sql.Date convertTimestampToDate(Timestamp timestamp)
  {
    if (isNull(timestamp)) {
      return null;
    }
    long milliseconds = timestamp.getTime() + timestamp.getNanos() / 1000000;
    return new java.sql.Date(milliseconds);
  }
 
  public static Timestamp getTimeStampValue(String dateValue)
    throws ParseException
  {
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    if (isNotNull(dateValue)) {
      return new Timestamp(formatter.parse(dateValue).getTime());
    }
    return new Timestamp(System.currentTimeMillis());
  }
 
  public static Timestamp getTimeStampValue(String dateValue, String dateFormat)
    throws ParseException
  {
    SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
    if (isNotNull(dateValue)) {
      return new Timestamp(formatter.parse(dateValue).getTime());
    }
    return new Timestamp(System.currentTimeMillis());
  }
 
  public static String replicate(String str, int times)
  {
    String result = "";
    if (isNull(str)) {
      return null;
    }
    for (int idx = 0; idx < times; idx++) {
      result = result + str;
    }
    return result;
  }
 
  public static String getValueFromHashMap(HashMap map, String key, String retvalue)
  {
    if (!map.containsKey(key)) {
      return retvalue;
    }
    return (String)map.get(key);
  }
 
  public static byte[] getByteArray(Blob image)
    throws Exception
  {
    InputStream input = null;
    ByteArrayOutputStream output = null;
    try
    {
      input = image.getBinaryStream();
      output = new ByteArrayOutputStream();
     
      byte[] rb = new byte[1024];
      int ch = 0;
      while ((ch = input.read(rb)) != -1) {
        output.write(rb, 0, ch);
      }
      return output.toByteArray();
    }
    finally
    {
      input.close();
      output.close();
    }
  }
 
  public static String returnIfEqual(String str, String str1, String ret)
  {
    if (isNull(str)) {
      return ret;
    }
    if (isEqual(str, str1)) {
      return ret;
    }
    return str;
  }
 
  public static java.util.Date addDate(java.sql.Date date, int noOfDay)
    throws Exception
  {
    GregorianCalendar calendar = new GregorianCalendar();
    calendar.setTime(date);
    calendar.add(6, noOfDay);
    return calendar.getTime();
  }
 
  public static java.util.Date addDate(java.util.Date date, int noOfDay)
    throws Exception
  {
    GregorianCalendar calendar = new GregorianCalendar();
    calendar.setTime(date);
    calendar.add(6, noOfDay);
    return calendar.getTime();
  }
 
  public static java.util.Date convertSundayToMonday(java.util.Date date)
    throws Exception
  {
    GregorianCalendar calendar = new GregorianCalendar();
    calendar.setTime(date);
    if (calendar.get(7) == 1) {
      calendar.add(6, 1);
    }
    return calendar.getTime();
  }
 
  public static java.util.Date convertSundayToMondayForDateOfJoining(java.util.Date date)
    throws Exception
  {
    GregorianCalendar calendar = new GregorianCalendar();
    calendar.setTime(date);
    if (calendar.get(7) == 1) {
      calendar.add(6, 1);
    } else if (calendar.get(7) == 7) {
      calendar.add(6, 2);
    }
    return calendar.getTime();
  }
 
  public static String getDayName(java.util.Date date)
    throws Exception
  {
    GregorianCalendar calendar = new GregorianCalendar();
    calendar.setTime(date);
   
    String[] dayNames = { "SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY" };
    return dayNames[(calendar.get(7) - 1)];
  }
 
  public static String getUserviewableUtilDateFormat(java.util.Date dateValue)
    throws Exception
  {
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    try
    {
      String formatValue = "";
      if (isNotNull(dateValue)) {}
      return formatter.format(dateValue);
    }
    catch (Exception e)
    {
      throw e;
    }
  }
 
  public static Timestamp convertStringToTimeStamp(String dateTimeValue)
    throws ParseException
  {
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    if (isNotNull(dateTimeValue)) {
      return new Timestamp(formatter.parse(dateTimeValue).getTime());
    }
    return new Timestamp(System.currentTimeMillis());
  }
 
  public static Map<String, Integer> getDaysMonthYearBtDates(java.sql.Date fromdate, java.sql.Date toDate)
  {
    Map<String, Integer> returnMap = new HashMap();
    Calendar calenderFrom = Calendar.getInstance();
    Calendar calenderTo = Calendar.getInstance();
    calenderFrom.setTime(fromdate);
    calenderTo.setTime(toDate);
    double days = (calenderTo.getTimeInMillis() - calenderFrom.getTimeInMillis()) / 86400000L;
    double year = days / 365.0D;
    double months = days / 30.5D;
   


    returnMap.put("days", Integer.valueOf((int)days));
    returnMap.put("years", Integer.valueOf((int)year));
    returnMap.put("months", Integer.valueOf((int)months));
    return returnMap;
  }
 
  public static double minimumValue(double value1, double value2)
  {
    if (value1 < value2) {
      return value1;
    }
    return value2;
  }
 
  public static int getMinimumofNumbers1(int[] numbers)
  {
    int minValue = numbers[0];
    for (int i = 1; i < numbers.length; i++) {
      if (numbers[i] < minValue) {
        minValue = numbers[i];
      }
    }
    return minValue;
  }
 
  public static double minimumValue1(double value1, double value2)
  {
    if (value1 < value2) {
      return value1;
    }
    return value2;
  }
 
  public static int getMinimumofNumbers(int[] numbers)
  {
    int minValue = numbers[0];
    for (int i = 1; i < numbers.length; i++) {
      if (numbers[i] < minValue) {
        minValue = numbers[i];
      }
    }
    return minValue;
  }
 
  public static int getDays(String fromDate, String toDate)
    throws ParseException
  {
    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    java.util.Date d1 = format.parse(fromDate);
    java.util.Date d2 = format.parse(toDate);
    int interval = toInt(Long.valueOf(Math.abs((d1.getTime() - d2.getTime()) / 86400000L)));
    return interval + 1;
  }
 
  public static String convertNumberToRupee(String number)
  {
    if (isNotNull(number))
    {
      if (number.contains(",")) {
        return number;
      }
      char[] k = getEmptyIfNull(number).toCharArray();
      String rupeeValue = "";
      int m = k.length;
      switch (m)
      {
      case 3:
        rupeeValue = k[0] + k[1] + k[2];
        break;
      case 4:
        rupeeValue = k[0] + "," + k[1] + k[2] + k[3];
        break;
      case 5:
        rupeeValue = k[0] + k[1] + "," + k[2] + k[3] + k[4];
        break;
      case 6:
        rupeeValue = k[0] + "," + k[1] + k[2] + "," + k[3] + k[4] + k[5];
        break;
      case 7:
        rupeeValue = k[0] + k[1] + "," + k[2] + k[3] + "," + k[4] + k[5] + k[6];
        break;
      case 8:
        rupeeValue = k[0] + "," + k[1] + k[2] + "," + k[3] + k[4] + "," + k[5] + k[6] + k[7];
        break;
      case 9:
        rupeeValue = k[0] + k[1] + "," + k[2] + k[3] + "," + k[4] + k[5] + "," + k[6] + k[7] + k[8];
        break;
      case 10:
        rupeeValue = k[0] + k[1] + k[2] + "," + k[3] + k[4] + "," + k[5] + k[6] + "," + k[7] + k[8] + k[9];
        break;
      default:
        rupeeValue = number;
      }
      return rupeeValue;
    }
    return "";
  }
 
  public static Properties propertiesLoad(String fileName)
    throws Exception
  {
    Properties properties = new Properties();
    URL url = ClassLoader.getSystemResource(fileName);
   
    properties.load(url.openStream());
   
    return properties;
  }
 
  public static String removeDelimitter(String value, String delimitter)
  {
    if (getEmptyIfNull(value).contains(delimitter)) {
      value = value.replaceAll(delimitter, "");
    }
    return value;
  }
 
  public static String convertNumToWord(long r)
  {
    int c = 1;
   
    string = "";
    if (r == 0L) {
      display(a[1]);
    }
    while (r != 0L)
    {
      switch (c)
      {
      case 1:
        long rm = r % 100L;
        pass(rm);
        if ((r > 100L) && (r % 100L != 0L)) {
          display("and ");
        }
        r /= 100L;
       
        break;
      case 2:
        long rm = r % 10L;
        if (rm != 0L)
        {
          display(" ");
          display(b[0]);
          display(" ");
          pass(rm);
        }
        r /= 10L;
        break;
      case 3:
        long rm = r % 100L;
        if (rm != 0L)
        {
          display(" ");
          display(b[1]);
          display(" ");
          pass(rm);
        }
        r /= 100L;
        break;
      case 4:
        long rm = r % 100L;
        if (rm != 0L)
        {
          display(" ");
          display(b[2]);
          display(" ");
          pass(rm);
        }
        r /= 100L;
        break;
      case 5:
        long rm = r % 100L;
        if (rm != 0L)
        {
          display(" ");
          display(b[3]);
          display(" ");
          pass(rm);
        }
        r /= 100L;
      }
      c++;
    }
    return " " + string + " Rupees Only ";
  }
 
  public static String convertNumToWord(String num)
  {
    long r = Long.parseLong(removeCommas(num));
   
    int c = 1;
   
    string = "";
    if (num.equals("0")) {
      display(a[0]);
    }
    while (r != 0L)
    {
      switch (c)
      {
      case 1:
        long rm = r % 100L;
        pass(rm);
        if ((r > 100L) && (r % 100L != 0L)) {
          display("and ");
        }
        r /= 100L;
       
        break;
      case 2:
        long rm = r % 10L;
        if (rm != 0L)
        {
          display(" ");
          display(b[0]);
          display(" ");
          pass(rm);
        }
        r /= 10L;
        break;
      case 3:
        long rm = r % 100L;
        if (rm != 0L)
        {
          display(" ");
          display(b[1]);
          display(" ");
          pass(rm);
        }
        r /= 100L;
        break;
      case 4:
        long rm = r % 100L;
        if (rm != 0L)
        {
          display(" ");
          display(b[2]);
          display(" ");
          pass(rm);
        }
        r /= 100L;
        break;
      case 5:
        long rm = r % 100L;
        if (rm != 0L)
        {
          display(" ");
          display(b[3]);
          display(" ");
          pass(rm);
        }
        r /= 100L;
      }
      c++;
    }
    return " (" + string + " Rupees Only) ";
  }
 
  public static void pass(long rm2)
  {
    if (rm2 < 10L) {
      display(a[((int)rm2)]);
    }
    if ((rm2 > 9L) && (rm2 < 20L)) {
      display(c[((int)(rm2 - 10L))]);
    }
    if (rm2 > 19L)
    {
      int rm = (int)(rm2 % 10L);
      if (rm == 0)
      {
        int q = (int)(rm2 / 10L);
        display(d[(q - 2)]);
      }
      else
      {
        int q = (int)(rm2 / 10L);
        display(a[rm]);
        display(" ");
        display(d[(q - 2)]);
      }
    }
  }
 
  public static void display(String s)
  {
    String t = string;
    string = s;
    string += t;
  }
 
  public static String MoneyWithCommas(String amount)
  {
    long r = Long.parseLong(removeCommas(amount));
    String sss = String.valueOf(r);
    String result = null;
    if (sss.length() >= 11) {
      return sss;
    }
    char[] k = sss.toCharArray();
   
    int m = k.length;
    switch (m)
    {
    case 1:
      result = k[0] + "/-";
      break;
    case 2:
      result = k[0] + k[1] + "/-";
      break;
    case 3:
      result = k[0] + k[1] + k[2] + "/-";
      break;
    case 4:
      result = k[0] + "," + k[1] + k[2] + k[3] + "/-";
      break;
    case 5:
      result = k[0] + k[1] + "," + k[2] + k[3] + k[4] + "/-";
      break;
    case 6:
      result = k[0] + "," + k[1] + k[2] + "," + k[3] + k[4] + k[5] + "/-";
      break;
    case 7:
      result = k[0] + k[1] + "," + k[2] + k[3] + "," + k[4] + k[5] + k[6] + "/-";
      break;
    case 8:
      result = k[0] + "," + k[1] + k[2] + "," + k[3] + k[4] + "," + k[5] + k[6] + k[7] + "/-";
      break;
    case 9:
      result = k[0] + k[1] + "," + k[2] + k[3] + "," + k[4] + k[5] + "," + k[6] + k[7] + k[8] + "/-";
      break;
    case 10:
      result = k[0] + k[1] + k[2] + "," + k[3] + k[4] + "," + k[5] + k[6] + "," + k[7] + k[8] + k[9] + "/-";
      break;
    default:
      result = "";
    }
    return result;
  }
 
  public static String removeCommas(String Number)
  {
    String No = Number.replaceAll(",", "");
    logger.info("No :" + No);
    return No;
  }
 
  public static String splitString(String str, String delimiter)
  {
    String ss = "";
    StringTokenizer strtkn = new StringTokenizer(str, delimiter);
    while (strtkn.hasMoreTokens())
    {
      String s = "";
      s = s + strtkn.nextToken();
      if (!s.equals("-"))
      {
        ss = ss + s;
        break;
      }
    }
    ss.trim();
   
    return ss;
  }
 
  public static boolean getMaxDate(String d1, String d2)
  {
    boolean flag = false;
    try
    {
      java.sql.Date date1 = convertTimestampToDate(getTimeStampValue(d1));
      java.sql.Date date2 = convertTimestampToDate(getTimeStampValue(d2));
      flag = date1.after(date2);
    }
    catch (ParseException e)
    {
      e.printStackTrace();
    }
    return flag;
  }
 
  public static boolean getMinDate(String d1, String d2)
  {
    boolean flag = false;
    try
    {
      java.sql.Date date1 = convertTimestampToDate(getTimeStampValue(d1));
      java.sql.Date date2 = convertTimestampToDate(getTimeStampValue(d2));
      flag = date1.before(date2);
    }
    catch (ParseException e)
    {
      e.printStackTrace();
    }
    return flag;
  }
 
  public static String addDays(String d1, int days)
    throws Exception
  {
    String addDate = "";
    Calendar calDate = Calendar.getInstance();
    calDate.set(toInt(d1.split("/")[2]), toInt(d1.split("/")[1]) - 1, toInt(d1.split("/")[0]));
    calDate.add(5, days);
    addDate = getUserviewableUtilDateFormat(calDate.getTime());
    return addDate;
  }
 
  public static String subtractDays(String d1, int days)
    throws Exception
  {
    String subDate = "";
    Calendar calDate = Calendar.getInstance();
    calDate.set(toInt(d1.split("/")[2]), toInt(d1.split("/")[1]) - 1, toInt(d1.split("/")[0]));
    calDate.add(5, -days);
    subDate = getUserviewableUtilDateFormat(calDate.getTime());
    return subDate;
  }
 
  public static int numberOfDays(String d1, String d2)
    throws Exception
  {
    Calendar cal1 = new GregorianCalendar();
    Calendar cal2 = new GregorianCalendar();
    cal1.set(toInt(d1.split("/")[2]), toInt(d1.split("/")[1]) - 1, toInt(d1.split("/")[0]));
    cal2.set(toInt(d2.split("/")[2]), toInt(d2.split("/")[1]) - 1, toInt(d2.split("/")[0]));
    int numberOfDays = daysBetween(cal1.getTime(), cal2.getTime());
    if (cal1.after(cal2)) {
      numberOfDays = -numberOfDays;
    }
    return numberOfDays;
  }
 
  public static int daysBetween(java.util.Date date1, java.util.Date date2)
  {
    return (int)((date2.getTime() - date1.getTime()) / 86400000L);
  }
 
  public static int getFinancialYear(java.sql.Date currentDate, int currentYear)
    throws Exception
  {
    java.sql.Date finDt = getDateValue("31/03/" + currentYear, "dd/MM/yyyy");
    if (currentDate.after(finDt)) {
      return currentYear;
    }
    currentYear--;return currentYear;
  }
 
  public static String BudgetYear(Object dateValue)
    throws Exception
  {
    String dateVal = dateValue.toString();
    String month = "";String year = "";
    try
    {
      if (isNotNull(dateValue))
      {
        month = dateVal.substring(3, 5);
        year = dateVal.substring(6);
      }
      if ((toInt(month) >= 4) && (toInt(month) <= 12)) {
        year = year + "-" + getEmptyIfNull(Integer.valueOf(toInt(year) + 1));
      } else {
        year = getEmptyIfNull(Integer.valueOf(toInt(year) - 1)) + "-" + year;
      }
    }
    catch (Exception e)
    {
      throw e;
    }
    return year;
  }
 
  private static Timestamp getCurrentTimeStamp()
  {
    java.util.Date today = new java.util.Date();
    return new Timestamp(today.getTime());
  }
 
  public static String convertDecimalFormat(Object val, String deciLength)
  {
    BigDecimal valBigDecimal = new BigDecimal(String.valueOf(val));
    return convertformatNumber(valBigDecimal, deciLength);
  }
 
  public static String convertformatNumber(BigDecimal valofstr, String deciLength)
  {
    DecimalFormat df = new DecimalFormat(deciLength);
    return df.format(valofstr);
  }
 
  public static boolean isNumeric(String str)
  {
    return str.matches("[+-]?\\d*(\\.\\d+)?");
  }
}
