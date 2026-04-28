package in.co.boe.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
      logger.info("result------------------>" + result);
      logger.info("result------------------>" + result);
    }
    catch (IOException e)
    {
      logger.info("moveFile-------------" + e.getMessage());
      logger.info("moveFile-------------" + e.getMessage());
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
  public String toDouble(String quantity)
  {
    String temp = null;
    try
    {
      temp = removeComma(checkForNullvalue(quantity)).trim();
      if (temp.equalsIgnoreCase("")) {
        return "0";
      }
    }
    catch (NumberFormatException e)
    {
      return "0";
    }
    return temp;
  }
  public double convertToDouble(String str)
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
  public static String convertToNoOfDecimalMethod(String amount, int decimalPlaces)
  {
    String result = "";
    BigDecimal amountVal = null;
    try
    {
      amountVal = new BigDecimal(amount);
      amountVal = amountVal.setScale(decimalPlaces, RoundingMode.HALF_UP);
      result = String.valueOf(amountVal.doubleValue());
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return result;
  }
  public static String checkForNullvalue(String value)
  {
    if (value != null) {
      return value.trim();
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
      SimpleDateFormat dateFormat = new SimpleDateFormat(
        "dd-MMM-yyyy hh:mm:ss a");
      return dateFormat.format(timestamp);
    }
    return null;
  }
  public static String setDefaultValue(String quantity)
  {
    String temp = null;
    try
    {
      temp = removeComma(checkForNullvalue(quantity)).trim();
      if (temp.equalsIgnoreCase("")) {
        return "0";
      }
      if (temp.equalsIgnoreCase(".00")) {
        return "0";
      }
    }
    catch (NumberFormatException e)
    {
      return "0";
    }
    return temp;
  }
  public static String setCheckValue(String item)
  {
    String temp = null;
    try
    {
      temp = checkForNullvalue(item).trim();
      if (temp.equalsIgnoreCase("")) {
        return "";
      }
      if (temp.equalsIgnoreCase(".00")) {
        return "0";
      }
    }
    catch (NumberFormatException e)
    {
      return "0";
    }
    return temp;
  }
  public static String setZeroValue(String item)
  {
    String temp = null;
    try
    {
      temp = checkForNullvalue(item).trim();
      if (temp.equalsIgnoreCase("")) {
        return "0";
      }
      if (temp.equalsIgnoreCase(".00")) {
        return "0";
      }
    }
    catch (NumberFormatException e)
    {
      return "0";
    }
    return temp;
  }
  public static String setDefaultAmount(String quantity)
  {
    String temp = null;
    try
    {
      temp = removeComma(checkForNullvalue(quantity)).trim();
      if (temp.equalsIgnoreCase("")) {
        return "0";
      }
    }
    catch (NumberFormatException e)
    {
      return "0";
    }
    return temp;
  }
  public static String removeComma(String amount)
  {
    String regex = "(?<=[\\d])(,)(?=[\\d])";
    Pattern p = Pattern.compile(regex);
    Matcher m = p.matcher(amount);
    amount = m.replaceAll("");
    return amount;
  }
  public String getErrorDesc(String errorCD, String screenId)
  {
    String errorMsg = "";
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    try
    {
      con = DBConnectionUtility.getConnection();
      String query = "SELECT ERROR_MSG FROM ETT_ERROR_CODES WHERE ERROR_CODE=? AND SCREEN_ID=?";
      pst = con.prepareStatement(query);
      pst.setString(1, errorCD);
      pst.setString(2, screenId);
      rs = pst.executeQuery();
      while (rs.next()) {
        errorMsg = rs.getString(1);
      }
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
  public String getEmptyIfNull(Object sourceStr)
  {
    return convertIfNull(sourceStr, "");
  }
  public static String convertIfNull(Object sourceStr, Object toConvert)
  {
    return isNullValue(sourceStr) ? toConvert.toString() : sourceStr
      .toString();
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
  public static String invalidEventReference(String payRef, String eveRef)
  {
    String referevent = null;
    String eventrefer = null;
    Connection con = null;
    PreparedStatement ppt = null;
    ResultSet rs = null;
    String r = "";
    try
    {
      logger.info("payRef----" + payRef);
      con = DBConnectionUtility.getConnection();
      String query = "Select Trim(Pd_Eve_Code),PD_PART_PAY_REF  From Ettv_Boe_Payment_Details Where Trim(Pd_Txn_Ref)=?";
      logger.info(query);
      ppt = con.prepareStatement(query);
      ppt.setString(1, payRef.trim());
      rs = ppt.executeQuery();
      logger.info("master_ref----" + payRef);
      while (rs.next())
      {
        referevent = rs.getString(1);
        logger.info("referevent IN WHILE :" + referevent);
        eventrefer = rs.getString(2);
        logger.info("eventrefer IN WHILE  :" + eventrefer);
        logger.info("referevent" + referevent);
        logger.info("eventrefer" + eventrefer);
        logger.info("DBEVENT" + eventrefer.substring(0, 3));
        logger.info("event1" + eveRef.substring(0, 3));
      }
      if ((eventrefer != null) && (eventrefer.length() > 0) && 
        (eveRef != null) && (eveRef.length() > 0))
      {
        if (!eventrefer.substring(0, 3).equals(eveRef.substring(0, 3))) {
          r = r + eveRef + " Invalid event reference Number,";
        }
        logger.info("r :" + r);
      }
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
      logger.info(exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, ppt, rs);
    }
    return r;
  }
  public static String invalidPaymentReference(String payRef, String eveRef)
  {
    String status = null;
    int counts = 0;
    Connection con1 = null;
    Connection con2 = null;
    PreparedStatement ppt1 = null;
    PreparedStatement ppt2 = null;
    ResultSet rs1 = null;
    ResultSet rs2 = null;
    String r = "";
    try
    {
      con1 = DBConnectionUtility.getConnection();
      String query = "Select Count(1) From Master Where Trim(Master_Ref) =?";
      logger.info("query  :" + query);
      ppt1 = con1.prepareStatement(query);
      ppt1.setString(1, payRef.trim());
      rs1 = ppt1.executeQuery();
      while (rs1.next()) {
        counts = rs1.getInt(1);
      }
      logger.info("counts" + counts);
      if (counts == 0) {
        r = r + payRef + "Not a Valid Reference Number,";
      }
      if (counts > 0)
      {
        con2 = DBConnectionUtility.getConnection();
        String statusQuery = "Select status From Master Where Trim(Master_Ref) =?";
        logger.info("statusQuery :" + statusQuery);
        ppt2 = con2.prepareStatement(statusQuery);
        ppt2.setString(1, payRef.trim());
        rs2 = ppt2.executeQuery();
        while (rs2.next())
        {
          status = rs2.getString(1);
          logger.info("status :" + status);
        }
        logger.info("in else if ----");
        r = r + invalidEventReference(payRef, eveRef);
        logger.info("r in status liv :" + r);
      }
      logger.info("r" + r);
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
      logger.info(exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con1, ppt1, rs1);
      DBConnectionUtility.surrenderDB(con2, ppt2, rs2);
    }
    return r;
  }
  public double calcCrossCurrency(double fobAmt, double friAmt, double insAmt, double agyAmt, double disAmt, double misAmt, String fobccy, String friccy, String insccy, String agyccy, String disccy, String misccy)
  {
    double value = 0.0D;
    double fobrate = 0.0D;double frirate = 0.0D;double insrate = 0.0D;double agyrate = 0.0D;double disrate = 0.0D;double misrate = 0.0D;
    try
    {
      fobrate = getSpotRate(fobccy);
      frirate = getSpotRate(friccy);
      insrate = getSpotRate(insccy);
      agyrate = getSpotRate(agyccy);
      disrate = getSpotRate(disccy);
      misrate = getSpotRate(misccy);
      if ((fobrate != 0.0D) || (frirate != 0.0D) || (insrate != 0.0D) || (agyrate != 0.0D) || (disrate != 0.0D) || (misrate != 0.0D)) {
        if (((fobrate == 0.0D) || (fobrate == frirate)) && ((frirate == 0.0D) || (frirate == insrate)) && 
          ((insrate == 0.0D) || (insrate == agyrate)) && ((agyrate == 0.0D) || (agyrate == disrate)) && 
          ((disrate == 0.0D) || (disrate == misrate)) && ((misrate == 0.0D) || (misrate == fobrate)))
        {
          value = fobAmt + friAmt + insAmt + agyAmt + disAmt + misAmt;
        }
        else
        {
          value += fobAmt;
          if ((fobrate != 0.0D) && (frirate != 0.0D)) {
            if (fobrate == frirate) {
              value += frirate;
            } else if (fobrate > frirate) {
              value += friAmt * frirate / fobrate;
            } else {
              value += friAmt * (frirate / fobrate);
            }
          }
          if ((fobrate != 0.0D) && (insrate != 0.0D)) {
            if (fobrate == insrate) {
              value += insrate;
            } else if (fobrate > insrate) {
              value += insAmt * insrate / fobrate;
            } else {
              value += insAmt * (insrate / fobrate);
            }
          }
          if ((fobrate != 0.0D) && (agyrate != 0.0D)) {
            if (fobrate == agyrate) {
              value += agyrate;
            } else if (fobrate > agyrate) {
              value += agyAmt * agyrate / fobrate;
            } else {
              value += agyAmt * (agyrate / fobrate);
            }
          }
          if ((fobrate != 0.0D) && (disrate != 0.0D)) {
            if (fobrate == disrate) {
              value += disrate;
            } else if (fobrate > disrate) {
              value += disAmt * disrate / fobrate;
            } else {
              value += disAmt * (disrate / fobrate);
            }
          }
          if ((fobrate != 0.0D) && (misrate != 0.0D)) {
            if (fobrate == misrate) {
              value += misrate;
            } else if (fobrate > misrate) {
              value += misAmt * misrate / fobrate;
            } else {
              value += misAmt * (misrate / fobrate);
            }
          }
        }
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return value;
  }
  public double getSpotRate(String currency)
  {
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    double exrate = 0.0D;
    try
    {
      con = DBConnectionUtility.getConnection();
      if (currency != null)
      {
        String query = "SELECT TRIM(SPOTRATE) AS SPOTRATE FROM SPOTRATE WHERE TRIM(CURRENCY)=?";
        ps = con.prepareStatement(query);
        ps.setString(1, currency);
        rs = ps.executeQuery();
        while (rs.next()) {
          if (rs.getString("SPOTRATE") != null) {
            exrate = Double.parseDouble(rs.getString("SPOTRATE"));
          }
        }
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, ps, rs);
    }
    return exrate;
  }
  public String getProcDate()
  {
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    String procDate = "";
    try
    {
      con = DBConnectionUtility.getConnection();
      String query = "SELECT TO_CHAR(TO_DATE(TRIM(PROCDATE),'DD/MM/YY'),'DD/MM/YYYY') AS PROCDATE FROM DLYPRCCYCL";
      ps = con.prepareStatement(query);
      rs = ps.executeQuery();
      while (rs.next()) {
        procDate = rs.getString("PROCDATE");
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, ps, rs);
    }
    return procDate;
  }
}