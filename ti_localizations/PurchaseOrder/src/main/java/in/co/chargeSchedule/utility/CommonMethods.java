package in.co.chargeSchedule.utility;

import in.co.chargeSchedule.vo.AlertMessagesVO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Properties;
import java.util.StringTokenizer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
 
public class CommonMethods
{
  private static Logger logger = Logger.getLogger(CommonMethods.class
    .getName());
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
  public static void setErrorvalues(Object[] arg, ArrayList<AlertMessagesVO> alertMsgArray)
  {
    CommonMethods commonMethods = new CommonMethods();
    AlertMessagesVO altMsg = new AlertMessagesVO();
    altMsg.setErrorId(commonMethods.getEmptyIfNull(arg[1]).equalsIgnoreCase("W") ? "WARNING" : "ERROR");
    altMsg.setErrorDesc("GENERAL");
    altMsg.setErrorCode(commonMethods.getEmptyIfNull(arg[3]));
    altMsg.setErrorDetails(commonMethods.getEmptyIfNull(arg[2]));
    altMsg.setErrorMsg(commonMethods.getEmptyIfNull(arg[1]).equalsIgnoreCase("W") ? "N" : "");
    alertMsgArray.add(altMsg);
  }
  public static String getErrorDescFromProperties(String id)
  {
    String errorDesc = "";
    try
    {
      Properties prop = ProbUtil.getErrorPropertiesValue();
      errorDesc = prop.getProperty(id.toUpperCase().trim());
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    return errorDesc;
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
      con = DBConnectionUtility.getConnection();
      pst = new LoggableStatement(con, "SELECT ERROR_MSG FROM ETT_SCHEDULE_ERRORCODE WHERE ERROR_CODE=? AND SCREEN_ID=?");
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
      Date date = sdf.parse(dateToValidate);
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
    HttpSession session = ServletActionContext.getRequest()
      .getSession();
    String userID = (String)session.getAttribute("USERID");
    return userID;
  }
  public static String getCurrentDate(String dateFormat)
  {
    SimpleDateFormat df = new SimpleDateFormat(dateFormat);
    String currDate = df.format(new Date());
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
      con = DBConnectionUtility.getConnection();
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
}