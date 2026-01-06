package in.co.ebrc.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;
 
public class CommonMethods
{
  private static Logger logger = Logger.getLogger(CommonMethods.class
    .getName());
  public boolean moveFile(String source, String destination)
    throws InterruptedException
  {
    InputStream inStream = null;
    OutputStream outStream = null;
    boolean result = false;
    try
    {
      File afile = new File(source);
      File bfile = new File(destination);
      if ((afile != null) && (afile.exists()))
      {
        inStream = new FileInputStream(afile);
        outStream = new FileOutputStream(bfile);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inStream.read(buffer)) > 0)
        {
          int length;
          outStream.write(buffer, 0, length);
        }
        inStream.close();
        outStream.close();
        afile.setWritable(true);
        System.gc();
        Thread.sleep(2000L);
        afile.delete();

 
        result = true;
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    return result;
  }
  public String setErrorString(String errorString, String currentError)
  {
    if (errorString.length() > 0) {
      errorString = errorString + ",";
    }
    errorString = errorString + currentError;
    return errorString;
  }
  public String checkForNullvalue(String value)
  {
    if (value != null) {
      return value;
    }
    return "";
  }
  public int retrieveNoOfErrors(String errorString)
  {
    StringTokenizer stringTokenizer = new StringTokenizer(errorString, ",");
    int n = stringTokenizer.countTokens();
    return n;
  }
  public static String findRequestUrl(String sentence)
  {
    String retval = null;
    String[] tokens = null;
    String splitPattern = "/ats";
    tokens = sentence.split(splitPattern);
    retval = tokens[0];
    return retval;
  }
  public boolean isNull(String value)
  {
    boolean result = false;
    if ((value == null) || (value.equalsIgnoreCase(""))) {
      result = true;
    }
    return result;
  }
  public String getDateFormat(String tempDate)
  {
    String delimiter = "-";
    try
    {
      if (tempDate != null)
      {
        String str = tempDate;
        String[] temp = str.split(delimiter);
        if (temp != null)
        {
          int len = temp.length;
          if (len == 3)
          {
            String year = temp[2];
            String month = temp[1];
            String date = temp[0];
            tempDate = year + "-" + month + "-" + date;
          }
          else
          {
            tempDate = null;
          }
        }
      }
    }
    catch (Exception e)
    {
      tempDate = null;
    }
    return tempDate;
  }
  public String comma(String amount)
  {
    String output = null;
    if (amount != null)
    {
      String number = amount;
      int len = number.length();
      output = number;
      if (len > 3) {
        for (int i = len; i > 0; i -= 2) {
          if (i == len)
          {
            i -= 3;
            String temp = output.substring(0, i);
            String temp1 = output.substring(i);
            output = temp + "," + temp1;
          }
          else
          {
            String temp = output.substring(0, i);
            String temp1 = output.substring(i);
            output = temp + "," + temp1;
          }
        }
      }
      return output;
    }
    return "0";
  }
  public boolean isNumeric(String quantity)
  {
    try
    {
      Integer.parseInt(quantity);
    }
    catch (NumberFormatException e)
    {
      return false;
    }
    return true;
  }
  public static String convertTimestampToUIDateFormat(Timestamp timestamp)
  {
    if (timestamp != null)
    {
      SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
      return dateFormat.format(timestamp);
    }
    return null;
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
  public double toDouble(Object str)
  {
    double tempVal = 0.0D;
    if (isNullValue(str)) {
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
  public static boolean isNotNull(Object obj)
  {
    return !isNullValue(obj);
  }
  public String getUserviewableUtilDateFormat(Date dateValue)
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
}
