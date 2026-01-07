package in.co.prishipment.utility;

import in.co.prishipment.vo.AlertMessagesVO;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;

public class CommonMethods
{
  public static void main(String[] args)
  {
    Connection con = null;
    LoggableStatement pst = null;
    ResultSet rs = null;
    try
    {
      con = DBConnectionUtility.getConnection();
      String query2 = "{call ETT_CUSTOMIZATION_PKG(?,?,?)}";
     

      pst.setString(1, "0958OCF160100108");
      pst.setString(2, "CRE001");
      pst.setString(3, "ROD");
     
      pst = new LoggableStatement(con, query2);
      pst.executeUpdate();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
 
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
 
  public static String removeComma(String amount)
  {
    String output = null;
    if (amount != null)
    {
      String number = amount.trim();
      output = number.replace(",", "");
      return output;
    }
    return "0";
  }
 
  public static double stringToDouble(String value)
  {
    double output = 0.0D;
    if (value != null)
    {
      output = Double.parseDouble(value);
      return output;
    }
    return output;
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
 
  public static String getErrorDesc(String errorCD, String screenId)
  {
    String errorMsg = "";
    Connection con = null;
    LoggableStatement pst = null;
    ResultSet rs = null;
    try
    {
      con = DBConnectionUtility.getConnection();
     
      pst = new LoggableStatement(con, "SELECT ERROR_MSG FROM ETT_PRESHIP_ERROR_CODES WHERE ERROR_CODE=? AND SCREEN_ID=?");
      pst.setString(1, errorCD);
      pst.setString(2, screenId);
      logger.info(pst.getQueryString());
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
 
  public static boolean bitwiseEqualsWithCanonicalNaN(double x, double y)
  {
    return Double.doubleToLongBits(x) == Double.doubleToLongBits(y);
  }
 
  public static boolean dopreshipmentKnockoffAmountValidation(String masterRef, BigDecimal repayAmt)
  {
    Boolean errorFlag = Boolean.valueOf(false);
    BigDecimal getOutAmount = new BigDecimal(0);
    BigDecimal getmanualOutAmount = new BigDecimal(0);
    BigDecimal totalAmt = new BigDecimal(0);
    BigDecimal subrahendAMt = new BigDecimal(0);
    BigDecimal loanAmount = new BigDecimal(0);
    BigDecimal loanAmt = new BigDecimal(0);
    try
    {
      getOutAmount = getOutstandingAmount(masterRef);
      getmanualOutAmount = getmanualOutstandingAmount(masterRef);
      logger.info("Master_ref--------->" + masterRef);
      loanAmount = getLoanAmount(masterRef);
      if (getmanualOutAmount == null) {
        totalAmt = getOutAmount;
      } else {
        totalAmt = getOutAmount.add(getmanualOutAmount);
      }
      if (loanAmount != null) {
        loanAmt = loanAmount;
      }
      logger.info("Total Outstanding amount--->" + totalAmt);
      logger.info("Total Loan Amount---->" + loanAmt);
     










      subrahendAMt = loanAmt.subtract(totalAmt);
      logger.info("Subtrahend Amount---->" + subrahendAMt);
      logger.info("Repay Aount------>" + repayAmt);
      int compVal = repayAmt.compareTo(subrahendAMt);
      logger.info("Comaparison value---->" + compVal);
      if (compVal > 0)
      {
        errorFlag = Boolean.valueOf(true);
        logger.info("Setting error------->");
        logger.info("Preshipment ERROR Total amount greater than outstanding amount");
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      e.getMessage();
    }
    finally
    {
      logger.info("Preshipment validation flag---->" + errorFlag);
    }
    return errorFlag.booleanValue();
  }
 
  public static BigDecimal getOutstandingAmount(String masterRef)
  {
    BigDecimal total_ettpreshipment_table_repayamt = new BigDecimal(0);
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rst = null;
    try
    {
      con = DBConnectionUtility.getConnection();
      pst = con.prepareStatement("select ETT.MAS_REF, SUM(ETT.REPAY_AMOUNT) AS OUT_AMT  from ETT_PRESHIPMENT ETT ,master MAS,baseevent BEV where trim(ETT.CUS_MAS_REF)=trim(mas.master_ref) and trim(ett.cus_event_ref)=trim(bev.refno_pfix)||lpad(bev.refno_serl,3,0) and mas.key97=bev.master_key  and bev.status in ('c','i')  and ETT.MAS_REF= ? GROUP BY ETT.MAS_REF");
      pst.setString(1, masterRef);
      rst = pst.executeQuery();
      while (rst.next()) {
        total_ettpreshipment_table_repayamt = rst.getBigDecimal("OUT_AMT");
      }
      logger.info("Outstanding Amount--------->" + total_ettpreshipment_table_repayamt);
    }
    catch (Exception e)
    {
      e.printStackTrace();
      e.getMessage();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rst);
    }
    return total_ettpreshipment_table_repayamt;
  }
 
  public static BigDecimal getmanualOutstandingAmount(String masterRef)
  {
    BigDecimal total_ettpreshipment_table_manual_repayamt = new BigDecimal(0);
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rst = null;
    try
    {
      con = DBConnectionUtility.getConnection();
      pst = con.prepareStatement("Select mas.MASTER_REF as MAS_REF,SUM(PAY.PRI_PAID/power(10,C8CED)) AS OUT_AMT from master mas, baseevent bev,FINREPAY pay,C8PF c8  where mas.key97 = bev.MASTER_KEY  AND BEV.KEY97 = pay.KEY97  and bev.status<>'a'  AND C8.C8CCY= BEV.CCY   aND BEV.CREATNMTHD='M'  AND trim(MAS.MASTER_REF)= ?  group by MAS.MASTER_REF ");
      pst.setString(1, masterRef);
      rst = pst.executeQuery();
      while (rst.next()) {
        total_ettpreshipment_table_manual_repayamt = rst.getBigDecimal("OUT_AMT");
      }
      logger.info("Manual Outstanding Amount--------->" + total_ettpreshipment_table_manual_repayamt);
    }
    catch (Exception e)
    {
      e.printStackTrace();
      e.getMessage();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rst);
    }
    return total_ettpreshipment_table_manual_repayamt;
  }
 
  public static BigDecimal getLoanAmount(String master)
  {
    BigDecimal LoanAmount = new BigDecimal(0);
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rst = null;
    try
    {
      con = DBConnectionUtility.getConnection();
      pst = con.prepareStatement("select fnc.DEAL_AMT/(SELECT power(10,c8.c8ced) as LOAN_AMT  FROM c8pf c8 WHERE c8.c8ccy= fnc.DEAL_CCY) as LOAN_AMOUNT from master mas ,fncemaster fnc where mas.key97=fnc.KEY97 and trim(mas.MASTER_REF) = ?");
      pst.setString(1, master);
      rst = pst.executeQuery();
      logger.info("Get Loan Amount Query---->select fnc.DEAL_AMT/(SELECT power(10,c8.c8ced) as LOAN_AMT  FROM c8pf c8 WHERE c8.c8ccy= fnc.DEAL_CCY) as LOAN_AMOUNT from master mas ,fncemaster fnc where mas.key97=fnc.KEY97 and trim(mas.MASTER_REF) = ? : " + master);
      while (rst.next())
      {
        logger.info("LOAN Amount----01----->select fnc.DEAL_AMT/(SELECT power(10,c8.c8ced) as LOAN_AMT  FROM c8pf c8 WHERE c8.c8ccy= fnc.DEAL_CCY) as LOAN_AMOUNT from master mas ,fncemaster fnc where mas.key97=fnc.KEY97 and trim(mas.MASTER_REF) = ?");
        LoanAmount = rst.getBigDecimal(1);
      }
      logger.info("Result set count---->" + rst.getRow());
      logger.info("LOAN Amount--------->" + LoanAmount);
    }
    catch (Exception e)
    {
      e.printStackTrace();
      e.getMessage();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rst);
    }
    return LoanAmount;
  }
}