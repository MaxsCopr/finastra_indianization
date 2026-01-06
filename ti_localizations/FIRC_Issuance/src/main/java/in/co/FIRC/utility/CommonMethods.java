package in.co.FIRC.utility;

import freemarker.log.Logger;
import in.co.FIRC.dao.utility.DBConnectionUtility;
import in.co.FIRC.vo.AuditVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.StringTokenizer;
 
public class CommonMethods
{
  private static Logger logger = Logger.getLogger(CommonMethods.class.getName());
  public String setErrorString(String errorString, String currentError)
  {
    if (errorString.length() > 0) {
      errorString = errorString + ",";
    }
    errorString = errorString + currentError;
    return errorString;
  }
  public static String checkForNullvalue(String value)
  {
    if (value != null) {
      return value;
    }
    return "";
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
  public static boolean isNull(String value)
  {
    boolean result = false;
    if ((value == null) || (value.equalsIgnoreCase(""))) {
      result = true;
    }
    return result;
  }
  public boolean isNull(Object obj)
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
  public String convertIfNull(Object sourceStr, Object toConvert)
  {
    return isNull(sourceStr) ? toConvert.toString() : sourceStr.toString();
  }
  public String getEmptyIfNull(Object sourceStr)
  {
    return convertIfNull(sourceStr, "");
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
  public boolean insertFIRCAuditInfo(AuditVO auditVO)
    throws SQLException
  {
    logger.info("------------insertFIRCAuditInfo----------");
    String insertNostroAuditQuery = null;
    PreparedStatement preparedStatement = null;
    Connection con = null;
    int nostroUpdate = 0;
    boolean result = false;
    try
    {
      con = DBConnectionUtility.getConnection();
      insertNostroAuditQuery = "INSERT INTO ETT_FIRC_PRINT_ADUIT(ADUIT_NO,BILLREFNO,IRM_TYPE,USERID) VALUES(FIRC_AUDIT_SEQ.NEXTVAL,?,?,?)";
      LoggableStatement pst = new LoggableStatement(con, insertNostroAuditQuery);
      pst.setString(1, auditVO.getReference_nubmer());
      pst.setString(2, auditVO.getIrmType());
      pst.setString(3, auditVO.getAudit_by());
      logger.info("------------insertFIRCAuditInfo-----insert query-----" + pst.getQueryString());
      nostroUpdate = pst.executeUpdate();
      pst.close();
      logger.info("------------insertFIRCAuditInfo-----insert count----" + nostroUpdate);
      if (nostroUpdate > 0) {
        result = true;
      }
    }
    catch (Exception exception)
    {
      logger.info("------------insertFIRCAuditInfo----exception------" + exception);
      exception.printStackTrace();
    }
    finally
    {
      con.close();
    }
    return result;
  }
}
