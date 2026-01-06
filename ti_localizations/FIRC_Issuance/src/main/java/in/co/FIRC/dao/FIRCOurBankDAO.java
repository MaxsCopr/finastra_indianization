package in.co.FIRC.dao;

import com.bs.ett.email.firc.EmailNotification;
import com.bs.ett.email.firc.NotificationUtil;
import in.co.FIRC.dao.exception.DAOException;
import in.co.FIRC.dao.utility.DBConnectionUtility;
import in.co.FIRC.utility.ActionConstants;
import in.co.FIRC.utility.CommonMethods;
import in.co.FIRC.utility.LoggableStatement;
import in.co.FIRC.utility.UtilityGenerateCard;
import in.co.FIRC.vo.AlertMessageVO;
import in.co.FIRC.vo.AuditVO;
import in.co.FIRC.vo.ourBank.OurBankVO;
import in.co.FIRC.vo.ourBank.PrintOurBankVO;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;
 
public class FIRCOurBankDAO
  extends DBConnectionUtility
{
  private static Logger logger = LogManager.getLogger(FIRCOurBankDAO.class.getName());
  static FIRCOurBankDAO dao;
  public static FIRCOurBankDAO getDAO()
  {
    if (dao == null) {
      dao = new FIRCOurBankDAO();
    }
    return dao;
  }
  private ArrayList<AlertMessageVO> alertMsgArray = new ArrayList();
  public ArrayList<AlertMessageVO> getAlertMsgArray()
  {
    return this.alertMsgArray;
  }
  public void setAlertMsgArray(ArrayList<AlertMessageVO> alertMsgArray)
  {
    this.alertMsgArray = alertMsgArray;
  }
  public OurBankVO fetchOurBankList(OurBankVO ourBankVO)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    PreparedStatement pst1 = null;
    ResultSet result = null;
    CommonMethods commonMethods = null;
    label2030:
    try
    {
      commonMethods = new CommonMethods();
      con = DBConnectionUtility.getConnection();
      String transactionRefNo = commonMethods.getEmptyIfNull(ourBankVO.getTransactionrefno()).trim();
      int count = 0;
      String tranref = " SELECT COUNT(*) AS COUNT FROM ETT_FIRC_LODGEMENT WHERE TRANS_REF_NO = ?";
      PreparedStatement ps = con.prepareStatement(tranref);
      ps.setString(1, transactionRefNo);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        count = rs.getInt("COUNT");
      }
      closePreparedStmtResultSet(ps, rs);
      if (count > 0)
      {
        String fecth_firc_view = "SELECT TO_CHAR(AMOUNT,'999,999,999,999,999.99') AS AMOUNT,TRIM(BENFICIARY_ADDRESS) AS BENFICIARY_ADDRESS,TRIM(CIF_NO) AS CIF_NO,TRIM(CURRENCY) AS CURRENCY,TRIM(EXCHANGE_RATE) AS EXCHANGE_RATE,TO_CHAR(TO_DATE(FIRC_DATE, 'DD-MM-YY'),'DD-MM-YY') AS FIRC_DATE,TRIM(FIRC_SERIAL_NO) AS FIRC_SERIAL_NO,TRIM(ISSUING_BANK_NAME) AS ISSUING_BANK_NAME,TRIM(ISSUING_BNK_BRANCH) as ISSUING_BANK_BRANCH,TRIM(ORDER_CUSTOMER) AS ORDER_CUSTOMER,TO_CHAR(PAID_AMOUNT,'999,999,999,999,999.99') AS PAID_AMOUNT,TRIM(INWARD_TYPE) AS INWARD_TYPE,TRIM(PUR_CODE) AS PUR_CODE,TRIM(PURPOSE) AS PURPOSE,TRIM(REM_COUNTRY) AS REM_COUNTRY,TRIM(REM_ADDRESS) AS REM_ADDRESS,TRIM(REMARKS) AS REMARKS,TO_CHAR(TO_DATE(VALUE_DATE, 'DD-MM-YY'),'DD-MM-YY') AS VALUE_DATE,TRIM(PAID_CURRENCY) AS PAID_CURRENCY,TRIM(USERID) AS USERID FROM ETT_FIRC_LODGEMENT WHERE TRIM(TRANS_REF_NO)=?";

 
 
 
 
 
 
        pst1 = con.prepareStatement(fecth_firc_view);
        pst1.setString(1, ourBankVO.getTransactionrefno().trim());
        logger.info(fecth_firc_view);
        result = pst1.executeQuery();
        if (result.next())
        {
          ourBankVO.setAmount(
            commonMethods.getEmptyIfNull(result.getString("AMOUNT")).trim());
          ourBankVO.setBenificiarydetails(commonMethods
            .getEmptyIfNull(result.getString("BENFICIARY_ADDRESS")).trim());
          ourBankVO.setCif_no(
            commonMethods.getEmptyIfNull(result.getString("CIF_NO")).trim());
          ourBankVO.setCurrency(
            commonMethods.getEmptyIfNull(result.getString("CURRENCY")).trim());
          ourBankVO.setExchange_rate(
            commonMethods.getEmptyIfNull(result.getString("EXCHANGE_RATE")).trim());
          ourBankVO.setFircdate(
            commonMethods.getEmptyIfNull(result.getString("FIRC_DATE")).trim());
          ourBankVO.setFircno(
            commonMethods.getEmptyIfNull(result.getString("FIRC_SERIAL_NO")).trim());
          ourBankVO.setIssuingBank(commonMethods
            .getEmptyIfNull(result.getString("ISSUING_BANK_NAME")).trim());
          ourBankVO.setIssunibranch(commonMethods
            .getEmptyIfNull(result.getString("ISSUING_BANK_BRANCH")).trim());
          ourBankVO.setOrderingcustomer(
            commonMethods.getEmptyIfNull(result.getString("ORDER_CUSTOMER")).trim());
          ourBankVO.setPaidamount(
            commonMethods.getEmptyIfNull(result.getString("PAID_AMOUNT")).trim());
          ourBankVO.setInwardType(
            commonMethods.getEmptyIfNull(result.getString("INWARD_TYPE")).trim());
          ourBankVO.setPurposecode(
            commonMethods.getEmptyIfNull(result.getString("PUR_CODE")).trim());
          ourBankVO.setPurposedesc(
            commonMethods.getEmptyIfNull(result.getString("PURPOSE")).trim());
          ourBankVO.setRembank(
            commonMethods.getEmptyIfNull(result.getString("REM_ADDRESS")).trim());
          ourBankVO.setRem_country(
            commonMethods.getEmptyIfNull(result.getString("REM_COUNTRY")).trim());
          ourBankVO.setRemarks(
            commonMethods.getEmptyIfNull(result.getString("REMARKS")).trim());
          ourBankVO.setValue_date(
            commonMethods.getEmptyIfNull(result.getString("VALUE_DATE")).trim());
          ourBankVO.setUserId(
            commonMethods.getEmptyIfNull(result.getString("USERID")).trim());
          ourBankVO.setPaidcurrency(
            commonMethods.getEmptyIfNull(result.getString("PAID_CURRENCY")).trim());
          ourBankVO.setTransactionrefno(commonMethods.getEmptyIfNull(ourBankVO.getTransactionrefno()).trim());
          break label2030;
        }
      }
      else
      {
        String fecth_firc_view = "SELECT TO_CHAR(AMOUNT,'999,999,999,999,999.99') AS AMOUNT ,TRIM(BENEFICIARY_ADDRESS) AS BENEFICIARY_ADDRESS,TRIM(BENEFICIARY_ADDRESS2) AS BENEFICIARY_ADDRESS2,TRIM(BENEFICIARY_ADDRESS3) AS BENEFICIARY_ADDRESS3,TRIM(BENEFICIARY_ADDRESS4) AS BENEFICIARY_ADDRESS4,TRIM(BENEFICIARY_ADDRESS5) AS BENEFICIARY_ADDRESS5,TRIM(CIF_NO) AS CIF_NO,TRIM(CURRENCY) AS CURRENCY,TRIM(EXCHANGE_RATE) AS EXCHANGE_RATE,TO_CHAR(TO_DATE(RECDATE, 'dd-mm-yy'),'dd-mm-yy') AS RECDATE,TRIM(REMITTER_NAME) AS REMITTER_NAME,TRIM(BANK_NAME) AS BANK_NAME,TRIM(ISSUING_BANK_BRANCH) AS ISSUING_BANK_BRANCH,TRIM(ORDCUS_CST) AS ORDCUS_CST,TRIM(ORDCUS_ADDRESS2) AS ORDCUS_ADDRESS2,TRIM(ORDCUS_ADDRESS3) AS ORDCUS_ADDRESS3,TRIM(ORDCUS_ADDRESS4) AS ORDCUS_ADDRESS4,TO_CHAR(REC_AMT,'999,999,999,999,999.99') AS REC_AMT,TRIM(PURCODE) AS PURCODE,TRIM(PURDESCRIPTION) AS PURDESCRIPTION,TRIM(ORDADDRESS) AS ORDADDRESS,TRIM(REMITTER_ADDRESS) AS REMITTER_ADDRESS,TRIM(REMITTER_ADDRESS2) AS REMITTER_ADDRESS2,TRIM(REMITTER_ADDRESS3) AS REMITTER_ADDRESS3,TRIM(REMITTER_ADDRESS4) AS REMITTER_ADDRESS4,TRIM(MAS_REF) AS MAS_REF,trim(REC_CCY) AS REC_CCY,TO_CHAR(TO_DATE(VALUE_DATE, 'dd-mm-yy'),'dd-mm-yy') AS VALUE_DATE FROM ETTV_FIRC_LODGEMENT_VIEW WHERE TRIM(MAS_REF)=?";

 
 
 
 
 
 
 
        pst1 = new LoggableStatement(con, fecth_firc_view);
        pst1.setString(1, ourBankVO.getTransactionrefno().trim());
        result = pst1.executeQuery();
        if (result.next())
        {
          ourBankVO.setAmount(
            commonMethods.getEmptyIfNull(result.getString("AMOUNT")).trim());
          String beneficiaryCustomer = null;
          String temp_beneficiaryAddress1 = commonMethods
            .getEmptyIfNull(result.getString("BENEFICIARY_ADDRESS")).trim();
          String temp_beneficiaryAddress2 = commonMethods
            .getEmptyIfNull(result.getString("BENEFICIARY_ADDRESS2")).trim();
          String temp_beneficiaryAddress3 = commonMethods
            .getEmptyIfNull(result.getString("BENEFICIARY_ADDRESS3")).trim();
          String temp_beneficiaryAddress4 = commonMethods
            .getEmptyIfNull(result.getString("BENEFICIARY_ADDRESS4")).trim();
          String temp_beneficiaryAddress5 = commonMethods
            .getEmptyIfNull(result.getString("BENEFICIARY_ADDRESS5")).trim();
          if ((!CommonMethods.isNull(temp_beneficiaryAddress1)) && 
            (!CommonMethods.isNull(temp_beneficiaryAddress2)) && 
            (!CommonMethods.isNull(temp_beneficiaryAddress3)) && 
            (!CommonMethods.isNull(temp_beneficiaryAddress4)) && 
            (!CommonMethods.isNull(temp_beneficiaryAddress5))) {
            beneficiaryCustomer = 
              temp_beneficiaryAddress1 + "," + temp_beneficiaryAddress2 + "," + temp_beneficiaryAddress3 + "," + temp_beneficiaryAddress4 + "," + temp_beneficiaryAddress5;
          } else if ((!CommonMethods.isNull(temp_beneficiaryAddress1)) && 
            (!CommonMethods.isNull(temp_beneficiaryAddress2)) && 
            (!CommonMethods.isNull(temp_beneficiaryAddress3)) && 
            (!CommonMethods.isNull(temp_beneficiaryAddress4))) {
            beneficiaryCustomer = 
              temp_beneficiaryAddress1 + "," + temp_beneficiaryAddress2 + "," + temp_beneficiaryAddress3 + "," + temp_beneficiaryAddress4;
          } else if ((!CommonMethods.isNull(temp_beneficiaryAddress1)) && 
            (!CommonMethods.isNull(temp_beneficiaryAddress2)) && 
            (!CommonMethods.isNull(temp_beneficiaryAddress3))) {
            beneficiaryCustomer = 
              temp_beneficiaryAddress1 + "," + temp_beneficiaryAddress2 + "," + temp_beneficiaryAddress3;
          } else if ((!CommonMethods.isNull(temp_beneficiaryAddress1)) && 
            (!CommonMethods.isNull(temp_beneficiaryAddress2))) {
            beneficiaryCustomer = temp_beneficiaryAddress1 + "," + temp_beneficiaryAddress2;
          } else {
            beneficiaryCustomer = temp_beneficiaryAddress1;
          }
          ourBankVO.setBenificiarydetails(commonMethods.getEmptyIfNull(beneficiaryCustomer).trim());
          ourBankVO.setCif_no(
            commonMethods.getEmptyIfNull(result.getString("CIF_NO")).trim());
          ourBankVO.setCurrency(
            commonMethods.getEmptyIfNull(result.getString("CURRENCY")).trim());
          ourBankVO.setExchange_rate(
            commonMethods.getEmptyIfNull(result.getString("EXCHANGE_RATE")).trim());
          ourBankVO.setFircdate(
            commonMethods.getEmptyIfNull(result.getString("RECDATE")).trim());
          ourBankVO.setFirc_our_bank(
            commonMethods.getEmptyIfNull(result.getString("REMITTER_NAME")).trim());
          ourBankVO.setIssuingBank(
            commonMethods.getEmptyIfNull(result.getString("BANK_NAME")).trim());
          ourBankVO.setIssunibranch(commonMethods
            .getEmptyIfNull(result.getString("ISSUING_BANK_BRANCH")).trim());
          String orderingCustomer = null;
          String temp_orderingCustAddress1 = commonMethods
            .getEmptyIfNull(result.getString("ORDCUS_CST")).trim();
          String temp_orderingCustAddress2 = commonMethods
            .getEmptyIfNull(result.getString("ORDCUS_ADDRESS2")).trim();
          String temp_orderingCustAddress3 = commonMethods
            .getEmptyIfNull(result.getString("ORDCUS_ADDRESS3")).trim();
          String temp_orderingCustAddress4 = commonMethods
            .getEmptyIfNull(result.getString("ORDCUS_ADDRESS4")).trim();
          if ((!CommonMethods.isNull(temp_orderingCustAddress1)) && 
            (!CommonMethods.isNull(temp_orderingCustAddress2)) && 
            (!CommonMethods.isNull(temp_orderingCustAddress3)) && 
            (!CommonMethods.isNull(temp_orderingCustAddress4))) {
            orderingCustomer = 
              temp_orderingCustAddress1 + "," + temp_orderingCustAddress2 + "," + temp_orderingCustAddress3 + "," + temp_orderingCustAddress4;
          } else if ((!CommonMethods.isNull(temp_orderingCustAddress1)) && 
            (!CommonMethods.isNull(temp_orderingCustAddress2)) && 
            (!CommonMethods.isNull(temp_orderingCustAddress3))) {
            orderingCustomer = 
              temp_orderingCustAddress1 + "," + temp_orderingCustAddress2 + "," + temp_orderingCustAddress3;
          } else if ((!CommonMethods.isNull(temp_orderingCustAddress1)) && 
            (!CommonMethods.isNull(temp_orderingCustAddress2))) {
            orderingCustomer = temp_orderingCustAddress1 + "," + temp_orderingCustAddress2;
          } else {
            orderingCustomer = temp_orderingCustAddress1;
          }
          ourBankVO.setOrderingcustomer(commonMethods.getEmptyIfNull(orderingCustomer).trim());
          ourBankVO.setPaidamount(
            commonMethods.getEmptyIfNull(result.getString("REC_AMT")).trim());
          ourBankVO.setPurposecode(
            commonMethods.getEmptyIfNull(result.getString("PURCODE")).trim());
          ourBankVO.setPurposedesc(
            commonMethods.getEmptyIfNull(result.getString("PURDESCRIPTION")).trim());
          ourBankVO.setRem_country(
            commonMethods.getEmptyIfNull(result.getString("ORDADDRESS")).trim());
          String remmitngbankdetails = null;
          String remAddr1 = commonMethods
            .getEmptyIfNull(result.getString("REMITTER_ADDRESS")).trim();
          String remAddr2 = commonMethods
            .getEmptyIfNull(result.getString("REMITTER_ADDRESS2")).trim();
          String remAddr3 = commonMethods
            .getEmptyIfNull(result.getString("REMITTER_ADDRESS3")).trim();
          String remAddr4 = commonMethods
            .getEmptyIfNull(result.getString("REMITTER_ADDRESS4")).trim();
          if ((!CommonMethods.isNull(remAddr1)) && (!CommonMethods.isNull(remAddr2)) && 
            (!CommonMethods.isNull(remAddr3)) && (!CommonMethods.isNull(remAddr4))) {
            remmitngbankdetails = remAddr1 + "," + remAddr2 + "," + remAddr3 + "," + remAddr4;
          } else if ((!CommonMethods.isNull(remAddr1)) && (!CommonMethods.isNull(remAddr2)) && 
            (!CommonMethods.isNull(remAddr3))) {
            remmitngbankdetails = remAddr1 + "," + remAddr2 + "," + remAddr3;
          } else if ((!CommonMethods.isNull(remAddr1)) && (!CommonMethods.isNull(remAddr2))) {
            remmitngbankdetails = remAddr1 + "," + remAddr2;
          } else {
            remmitngbankdetails = remAddr1;
          }
          ourBankVO.setRembank(commonMethods.getEmptyIfNull(remmitngbankdetails).trim());
          ourBankVO.setValue_date(
            commonMethods.getEmptyIfNull(result.getString("VALUE_DATE")).trim());
          ourBankVO.setPaidcurrency(
            commonMethods.getEmptyIfNull(result.getString("REC_CCY")).trim());
          ourBankVO.setTransactionrefno(commonMethods.getEmptyIfNull(ourBankVO.getTransactionrefno()).trim());
        }
      }
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
      throwDAOException(exception);
    }
    finally
    {
      closePreparedStmtResultSet(pst1, result);
    }
    logger.info("Exiting Method");
    return ourBankVO;
  }
  public OurBankVO validate(OurBankVO ourBankVO)
    throws DAOException
  {
    logger.info("--------validate-------------");
    CommonMethods commonMethods = null;
    Connection con = null;
    try
    {
      commonMethods = new CommonMethods();
      con = DBConnectionUtility.getConnection();
      if ((this.alertMsgArray != null) && 
        (this.alertMsgArray.size() > 0)) {
        this.alertMsgArray.clear();
      }
      String transRefNo = commonMethods.getEmptyIfNull(ourBankVO.getTransactionrefno()).trim();
      if (ourBankVO.getMode().equalsIgnoreCase("fetch"))
      {
        if (CommonMethods.isNull(transRefNo))
        {
          String errormsg = UtilityGenerateCard.getErrorDesc("FIRC01", "N125");
          Object[] arg = { Integer.valueOf(0), "E", errormsg, "Input" };
          setErrorvalues(arg);
        }
      }
      else if (ourBankVO.getMode().equalsIgnoreCase("validate"))
      {
        if (CommonMethods.isNull(transRefNo))
        {
          String errormsg = UtilityGenerateCard.getErrorDesc("FIRC01", "N125");
          Object[] arg = { Integer.valueOf(0), "E", errormsg, "Input" };
          setErrorvalues(arg);
        }
        if (!CommonMethods.isNull(transRefNo))
        {
          String tranref = " SELECT COUNT(*) AS FIRC_COUNT FROM ETT_FIRC_LODGEMENT WHERE STATUS IN ('A','P') AND TRANS_REF_NO = ?";
          int count = 0;
          PreparedStatement ps = con.prepareStatement(tranref);
          ps.setString(1, transRefNo);
          ResultSet rs = ps.executeQuery();
          if (rs.next()) {
            count = rs.getInt("FIRC_COUNT");
          }
          if (count > 0)
          {
            String errormsg = UtilityGenerateCard.getErrorDesc("FIRC02", "N125");
            Object[] arg = { Integer.valueOf(0), "E", errormsg, "Input" };
            setErrorvalues(arg);
          }
          closePreparedStmtResultSet(ps, rs);
        }
        String inwardType = commonMethods.getEmptyIfNull(ourBankVO.getInwardType()).trim();
        if (CommonMethods.isNull(inwardType))
        {
          String errormsg = UtilityGenerateCard.getErrorDesc("FIRC03", "N125");
          Object[] arg = { Integer.valueOf(0), "E", errormsg, "Input" };
          setErrorvalues(arg);
        }
      }
      if (this.alertMsgArray.size() > 0) {
        ourBankVO.setErrorList(this.alertMsgArray);
      }
    }
    catch (Exception exception)
    {
      logger.info("--------validate-----exception--------" + exception);
      throwDAOException(exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, null, null);
    }
    logger.info("Exiting Method");
    return ourBankVO;
  }
  public void setErrorvalues(Object[] arg)
  {
    CommonMethods commonMethods = new CommonMethods();
    AlertMessageVO altMsg = new AlertMessageVO();
    altMsg.setErrorId(commonMethods.getEmptyIfNull(arg[1]).equalsIgnoreCase("W") ? "Warning" : "Error");
    altMsg.setErrorDesc("General");
    altMsg.setErrorCode(commonMethods.getEmptyIfNull(arg[3]));
    altMsg.setErrorDetails(commonMethods.getEmptyIfNull(arg[2]));
    altMsg.setErrorMsg(commonMethods.getEmptyIfNull(arg[1]).equalsIgnoreCase("W") ? "N" : "");
    this.alertMsgArray.add(altMsg);
  }
  public OurBankVO insertOurBankList(OurBankVO ourBankVO)
    throws DAOException
  {
    logger.info("--------------insertOurBankList-------------------");
    Connection con = null;
    LoggableStatement pst = null;
    ResultSet rs = null;
    String loginedUserId = null;
    CommonMethods commonMethods = null;
    int i = 0;
    try
    {
      commonMethods = new CommonMethods();
      con = DBConnectionUtility.getConnection();
      HttpSession session = ServletActionContext.getRequest().getSession();
      loginedUserId = (String)session.getAttribute("loginedUserName");
      String transactionRefNo = commonMethods.getEmptyIfNull(ourBankVO.getTransactionrefno()).trim();
      int count = 0;
      String tranref = " SELECT COUNT(*) AS COUNT FROM ETT_FIRC_LODGEMENT WHERE TRANS_REF_NO =?";
      LoggableStatement ps = new LoggableStatement(con, tranref);
      ps.setString(1, transactionRefNo);
      logger.info("--------------insertOurBankList----ETT_FIRC_LODGEMENT count query---------------" + 
        ps.getQueryString());
      ResultSet rst = ps.executeQuery();
      if (rst.next()) {
        count = rst.getInt("COUNT");
      }
      closePreparedStmtResultSet(ps, rst);
      String rmvamountcomma = ourBankVO.getAmount().replaceAll("[$,]", "");
      String paidamt = ourBankVO.getPaidamount().replaceAll("[$,]", "");
      if (count > 0)
      {
        String updateQuery = "UPDATE ETT_FIRC_LODGEMENT SET ISSUING_BANK_NAME =?,ISSUING_BNK_BRANCH=?,FIRC_SERIAL_NO=?,FIRC_DATE=TO_DATE(?,'DD-MM-YY'),TRANS_REF_NO=?,BENFICIARY_ADDRESS=? ,REM_ADDRESS=?,CURRENCY=?,AMOUNT=?,PUR_CODE=?,ORDER_CUSTOMER=?,PAID_AMOUNT=?,AVAILABLE_AMOUNT=?,CIF_NO=?,EXCHANGE_RATE=?,INWARD_TYPE=?,REM_COUNTRY=?,REMARKS=? ,USERID=?,STATUS=?,PAID_CURRENCY=?,VALUE_DATE=TO_DATE(?,'DD-MM-YY'),PURPOSE=?,LASTUPDATE = SYSTIMESTAMP  WHERE TRANS_REF_NO=? AND FIRC_SERIAL_NO=?";

 
 
 
        pst = new LoggableStatement(con, updateQuery);
        pst.setString(1, ourBankVO.getIssuingBank());
        pst.setString(2, ourBankVO.getIssunibranch());
        pst.setString(3, ourBankVO.getFircno());
        pst.setString(4, ourBankVO.getFircdate());
        pst.setString(5, ourBankVO.getTransactionrefno());
        pst.setString(6, ourBankVO.getBenificiarydetails());
        pst.setString(7, ourBankVO.getRembank());
        pst.setString(8, ourBankVO.getCurrency());
        pst.setString(9, rmvamountcomma);
        pst.setString(10, ourBankVO.getPurposecode());
        pst.setString(11, ourBankVO.getOrderingcustomer());
        pst.setString(12, paidamt);
        pst.setString(13, rmvamountcomma);
        pst.setString(14, ourBankVO.getCif_no());
        pst.setString(15, ourBankVO.getExchange_rate());
        pst.setString(16, ourBankVO.getInwardType());
        pst.setString(17, ourBankVO.getRem_country());
        pst.setString(18, ourBankVO.getRemarks());
        pst.setString(19, loginedUserId);
        pst.setString(20, "P");
        pst.setString(21, ourBankVO.getPaidcurrency());
        pst.setString(22, ourBankVO.getValue_date());
        pst.setString(23, ourBankVO.getPurposedesc());
        pst.setString(24, ourBankVO.getTransactionrefno().trim());
        pst.setString(25, ourBankVO.getFircno());
        i = pst.executeUpdate();
      }
      else
      {
        String insertQuery = "INSERT INTO ETT_FIRC_LODGEMENT(AMOUNT, BENFICIARY_ADDRESS, CIF_NO, CURRENCY, EXCHANGE_RATE, FIRC_DATE,FIRC_SERIAL_NO,AVAILABLE_AMOUNT, ISSUING_BANK_NAME,  ISSUING_BNK_BRANCH, ORDER_CUSTOMER,PAID_AMOUNT, INWARD_TYPE, PUR_CODE, PURPOSE, REM_ADDRESS, REM_COUNTRY, REMARKS, TRANS_REF_NO, USERID, VALUE_DATE,STATUS,PAID_CURRENCY,CREATEDON) VALUES(?,?,?,?,?,TO_DATE(?,'DD-MM-YY'),ETT_FIRC_SEQ_NO,?,?,?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?,'DD-MM-YY'),?,?,SYSTIMESTAMP)";

 
 
        pst = new LoggableStatement(con, insertQuery);
        pst.setString(1, rmvamountcomma);
        pst.setString(2, ourBankVO.getBenificiarydetails());
        pst.setString(3, ourBankVO.getCif_no());
        pst.setString(4, ourBankVO.getCurrency());
        pst.setString(5, ourBankVO.getExchange_rate());
        pst.setString(6, ourBankVO.getFircdate());
        pst.setString(7, rmvamountcomma);
        pst.setString(8, ourBankVO.getIssuingBank());
        pst.setString(9, ourBankVO.getIssunibranch());
        pst.setString(10, ourBankVO.getOrderingcustomer());
        pst.setString(11, paidamt);
        pst.setString(12, ourBankVO.getInwardType());
        pst.setString(13, ourBankVO.getPurposecode());
        pst.setString(14, ourBankVO.getPurposedesc());
        pst.setString(15, ourBankVO.getRembank());
        pst.setString(16, ourBankVO.getRem_country());
        pst.setString(17, ourBankVO.getRemarks());
        pst.setString(18, ourBankVO.getTransactionrefno());
        pst.setString(19, loginedUserId);
        pst.setString(20, ourBankVO.getValue_date());
        pst.setString(21, "P");
        pst.setString(22, ourBankVO.getPaidcurrency());
        logger.info("Query FIRC----------------->" + pst.getQueryString());
        i = pst.executeUpdate();
        String fircNo = null;
        String getFircQuery = " SELECT FIRC_SERIAL_NO FROM ETT_FIRC_LODGEMENT WHERE TRANS_REF_NO = '" + 
          transactionRefNo + "'";

 
 
        LoggableStatement ps1 = new LoggableStatement(con, getFircQuery);
        logger.info("Query FIRC--------ETT_FIRC_LODGEMENT--------->" + pst.getQueryString());
        ResultSet rst1 = ps1.executeQuery();
        if (rst1.next())
        {
          fircNo = rst1.getString("FIRC_SERIAL_NO");
          ourBankVO.setFircno(fircNo);
          logger.info("Query FIRC--------ETT_FIRC_LODGEMENT--------->" + fircNo);
        }
        closePreparedStmtResultSet(ps1, rst1);
      }
      if (i > 0) {
        ourBankVO.setStatus("success");
      }
      logger.info("Query FIRC--------ETT_FIRC_LODGEMENT insertion count--------->" + i);
    }
    catch (Exception exception)
    {
      logger.info("insertOurBankList--------------exception" + exception);
      throwDAOException(exception);
    }
    finally
    {
      closeSqlRefferance(rs, pst, con);
    }
    logger.info("Exiting Method");
    return ourBankVO;
  }
  public PrintOurBankVO printOurBankDetails(PrintOurBankVO ourBankVO)
    throws DAOException
  {
    logger.info("---------------------printOurBankDetails------------------");
    Connection con = null;
    LoggableStatement loggableStatement = null;
    ResultSet result = null;
    String status = null;
    CommonMethods commonMethods = null;
    ArrayList<PrintOurBankVO> acclist = null;
    ArrayList<PrintOurBankVO> list = null;
    AuditVO auditVO = null;
    try
    {
      con = DBConnectionUtility.getConnection();
      commonMethods = new CommonMethods();
      list = new ArrayList();
      auditVO = new AuditVO();
      String mas_ref = commonMethods.getEmptyIfNull(ourBankVO.getTransactionrefno()).trim();
      String query1 = "SELECT PRINT_COUNT from ETT_FIRC_LODGEMENT WHERE trim(TRANS_REF_NO) = ?";
      LoggableStatement pst1 = new LoggableStatement(con, query1);
      pst1.setString(1, ourBankVO.getTransactionrefno().trim());
      logger.info("---------------------printOurBankDetails------------query------" + pst1.getQueryString());
      ResultSet rs1 = pst1.executeQuery();
      if (rs1.next())
      {
        status = rs1.getString("PRINT_COUNT");
        logger.info("---------------------printOurBankDetails---------count------" + status);
      }
      closePreparedStmtResultSet(pst1, rs1);
      HttpSession session = ServletActionContext.getRequest().getSession();
      String loginedUserId = (String)session.getAttribute("loginedUserName");
      if (auditVO != null)
      {
        auditVO.setReference_nubmer(mas_ref);
        auditVO.setIrmType(ourBankVO.getInwardType());
        auditVO.setAudit_by(loginedUserId);
      }
      if ((status != null) && (status.equalsIgnoreCase("N")))
      {
        String sqlUpdate = "UPDATE ETT_FIRC_LODGEMENT SET PRINT_COUNT='Y' WHERE TRIM(TRANS_REF_NO)=?";
        LoggableStatement pst2 = new LoggableStatement(con, sqlUpdate);
        pst2.setString(1, ourBankVO.getTransactionrefno().trim());
        logger.info("----------UPDATE----ETT_FIRC_LODGEMENT-------------------" + pst2.getQueryString());
        int i = pst2.executeUpdate();
        logger.info("----------UPDATE----ETT_FIRC_LODGEMENT---count----------------" + i);
        if (i > 0)
        {
          boolean insertStatus = commonMethods.insertFIRCAuditInfo(auditVO);
          logger.info(Boolean.valueOf(insertStatus));
        }
        closePreparedStatement(pst2);
      }
      else
      {
        boolean insertStatus = commonMethods.insertFIRCAuditInfo(auditVO);
        logger.info(Boolean.valueOf(insertStatus));
      }
      String fecth_firc_view = "select to_char(AMOUNT,'999,999,999,999,999.99')as AMOUNT,trim(BENFICIARY_ADDRESS)as BENFICIARY_ADDRESS,trim(CIF_NO) as CIF_NO,trim(CURRENCY) as CURRENCY,trim(EXCHANGE_RATE) as EXCHANGE_RATE,trim(TO_CHAR(TO_DATE(FIRC_DATE,'dd-mm-yy'),'DD-Mon-YYYY')) as FIRC_DATE,trim(FIRC_SERIAL_NO) as FIRC_SERIAL_NO,trim(ISSUING_BANK_NAME) as ISSUING_BANK_NAME,trim(ISSUING_BNK_BRANCH) as ISSUING_BANK_BRANCH,trim(ORDER_CUSTOMER) as ORDER_CUSTOMER,to_char(PAID_AMOUNT,'999,999,999,999,999.99') as PAID_AMOUNT,trim(INWARD_TYPE) as INWARD_TYPE, trim(PUR_CODE) as PUR_CODE,trim(PURPOSE) as PURPOSE,trim(REM_COUNTRY) as REM_COUNTRY,trim(REM_ADDRESS) as REM_ADDRESS,trim(REMARKS) as REMARKS,trim(VALUE_DATE) as VALUE_DATE,trim(PAID_CURRENCY) as PAID_CURRENCY,trim(USERID) as USERID from ETT_FIRC_LODGEMENT where trim(TRANS_REF_NO)=?";

 
 
 
 
 
 
      loggableStatement = new LoggableStatement(con, fecth_firc_view);
      loggableStatement.setString(1, ourBankVO.getTransactionrefno().trim());
      logger.info(
        "fecth_firc_view:----------------- " + loggableStatement.getQueryString());
      result = loggableStatement.executeQuery();
      if (result.next())
      {
        String amount = commonMethods.getEmptyIfNull(result.getString("AMOUNT")).trim();
        String beneficiaryCustomer = commonMethods
          .getEmptyIfNull(result.getString("BENFICIARY_ADDRESS")).trim();
        String cif_no = commonMethods.getEmptyIfNull(result.getString("CIF_NO")).trim();
        String currency = commonMethods.getEmptyIfNull(result.getString("CURRENCY")).trim();
        String exchange_rate = commonMethods
          .getEmptyIfNull(result.getString("EXCHANGE_RATE")).trim();
        String fircdate = commonMethods.getEmptyIfNull(result.getString("FIRC_DATE")).trim();
        String fircno = commonMethods.getEmptyIfNull(result.getString("FIRC_SERIAL_NO"))
          .trim();
        String issuingBank = commonMethods
          .getEmptyIfNull(result.getString("ISSUING_BANK_NAME")).trim();
        String issunibranch = commonMethods
          .getEmptyIfNull(result.getString("ISSUING_BANK_BRANCH")).trim();
        String orderingcustomer = commonMethods
          .getEmptyIfNull(result.getString("ORDER_CUSTOMER")).trim();
        String paid_amount = commonMethods.getEmptyIfNull(result.getString("PAID_AMOUNT"))
          .trim();
        String inwardType = commonMethods.getEmptyIfNull(result.getString("INWARD_TYPE"))
          .trim();
        String purposecode = commonMethods.getEmptyIfNull(result.getString("PUR_CODE"))
          .trim();
        String purposedesc = commonMethods.getEmptyIfNull(result.getString("PURPOSE"))
          .trim();
        String remmitngbankdetails = commonMethods
          .getEmptyIfNull(result.getString("REM_ADDRESS")).trim();
        String rem_country = commonMethods.getEmptyIfNull(result.getString("REM_COUNTRY"))
          .trim();
        String remarks = commonMethods.getEmptyIfNull(result.getString("REMARKS")).trim();
        String value_date = commonMethods.getEmptyIfNull(result.getString("VALUE_DATE"))
          .trim();
        String userid = commonMethods.getEmptyIfNull(result.getString("USERID")).trim();
        String paidcurrency = commonMethods.getEmptyIfNull(result.getString("PAID_CURRENCY"))
          .trim();
        ourBankVO.setAmount(amount);
        ourBankVO.setCif_no(cif_no);
        ourBankVO.setCurrency(currency);
        ourBankVO.setExchange_rate(exchange_rate);
        ourBankVO.setFircdate(fircdate);
        ourBankVO.setFircno(fircno);
        ourBankVO.setIssuingBank(issuingBank);
        ourBankVO.setIssunibranch(issunibranch);
        ourBankVO.setOrderingcustomer(orderingcustomer);
        ourBankVO.setBenificiarydetails(beneficiaryCustomer);
        ourBankVO.setPaidamount(paid_amount);
        ourBankVO.setInwardType(inwardType);
        ourBankVO.setPurposecode(purposecode);
        if (purposedesc != null)
        {
          purposedesc = purposedesc.replace("\n", "").replace("\r", "");
          ourBankVO.setPurposedesc(purposedesc);
        }
        ourBankVO.setRemmitngbankdetails(remmitngbankdetails);
        ourBankVO.setRem_country(rem_country);
        ourBankVO.setRemarks(remarks);
        ourBankVO.setValue_date(value_date);
        ourBankVO.setUserId(userid);
        ourBankVO.setTransactionrefno(mas_ref);
        ourBankVO.setTransrefno(mas_ref);
        ourBankVO.setPaidcurrency(paidcurrency);
        String query = "SELECT TO_CHAR(TO_DATE(PROCDATE, 'dd-mm-yy'),'dd-mm-yyyy') as SYSTEMDATE FROM DLYPRCCYCL";
        LoggableStatement pst3 = new LoggableStatement(con, query);
        logger.info("Date Query------------" + loggableStatement.getQueryString());
        ResultSet rs2 = pst3.executeQuery();
        if (rs2.next())
        {
          ourBankVO.setSystemDate(rs2.getString("SYSTEMDATE").trim());
          logger.info("Date Query--date value----------" + rs2.getString("SYSTEMDATE").trim());
        }
        closePreparedStmtResultSet(pst3, rs2);
        String mtQuery = "SELECT E.NOSTRM AS MT_NO,TO_CHAR(TO_DATE(E.NOSTDAT, 'DD-MM-YY'),'DD-Mon-YYYY') AS NOS_DATE,TRIM(H.HVBAD1) AS ADDR1,TRIM(H.HVBAD2) AS ADDR2,TRIM(H.HVBAD3) AS ADDR3,TRIM(H.HVBAD4) AS ADDR4,TRIM(H.HVBAD5) AS ADDR5 FROM MASTER M,BASEEVENT B,EXTEVENT E,HVPF H,CAPF C8 WHERE M.key97 = B.MASTER_KEY AND  B.KEY97 =  E.EVENT  AND  M.BHALF_BRN = C8.CABRNM  AND  H.HVBRNM = C8.CABRNM  AND TRIM(MASTER_REF)=?";

 
 
        LoggableStatement pst4 = new LoggableStatement(con, mtQuery);
        pst4.setString(1, ourBankVO.getTransactionrefno().trim());
        logger.info("-----------Query---------------------" + pst4.getQueryString());
        ResultSet rs3 = pst4.executeQuery();
        if (rs3.next())
        {
          ourBankVO.setNostroNo(commonMethods.getEmptyIfNull(rs3.getString("MT_NO")).trim());
          ourBankVO.setNostroDate(commonMethods.getEmptyIfNull(rs3.getString("NOS_DATE")).trim());
          ourBankVO.setAddress1(commonMethods.getEmptyIfNull(rs3.getString("ADDR1")).trim().replace(",", ""));
          ourBankVO.setAddress2(commonMethods.getEmptyIfNull(rs3.getString("ADDR2")).trim().replace(",", ""));
          ourBankVO.setAddress3(commonMethods.getEmptyIfNull(rs3.getString("ADDR3")).trim().replace(",", ""));
          ourBankVO.setAddress4(commonMethods.getEmptyIfNull(rs3.getString("ADDR4")).trim().replace(",", ""));
          ourBankVO.setAddress5(commonMethods.getEmptyIfNull(rs3.getString("ADDR5")).trim().replace(",", ""));
        }
        closePreparedStmtResultSet(pst4, rs3);
        list.add(ourBankVO);
      }
      if ((list != null) && (!list.isEmpty())) {
        ourBankVO.setPrintList(list);
      }
      String fxQuery = "SELECT ACC_NO,PURCHASE_CCY,PURCHASE_AMT,EX_RATE,SALE_CCY,SALE_AMT  FROM ETTV_FIRC_CURR_CONV WHERE MASTER_REF =?";

 
      LoggableStatement pst5 = new LoggableStatement(con, fxQuery);
      pst5.setString(1, mas_ref);
      logger.info("-----------Query---------------------" + pst5.getQueryString());
      ResultSet rs4 = pst5.executeQuery();
      String accNo = null;
      int fxCount = 0;
      PrintOurBankVO accValue = null;
      acclist = new ArrayList();
      while (rs4.next())
      {
        accValue = new PrintOurBankVO();
        accNo = commonMethods.getEmptyIfNull(rs4.getString("ACC_NO")).trim();
        accValue.setFircCurrRec(commonMethods.getEmptyIfNull(rs4.getString("PURCHASE_CCY")).trim());
        accValue.setFircAmount(commonMethods.getEmptyIfNull(rs4.getString("PURCHASE_AMT")).trim());
        accValue.setFircEX(commonMethods.getEmptyIfNull(rs4.getString("EX_RATE")).trim());
        accValue.setFircCurr(commonMethods.getEmptyIfNull(rs4.getString("SALE_CCY")).trim());
        accValue.setFircConAmt(commonMethods.getEmptyIfNull(rs4.getString("SALE_AMT")).trim());
        acclist.add(accValue);
        fxCount++;
      }
      closePreparedStmtResultSet(pst5, rs4);
      if (fxCount == 0)
      {
        String fircCurrQuery = "SELECT ACC_NO,PURCHASE_CCY,PURCHASE_AMT,EX_RATE,SALE_CCY,SALE_AMT  FROM ETTV_FIRC_BAS_CURR_CONV WHERE MASTER_REF = ?";

 
        LoggableStatement pst6 = new LoggableStatement(con, fircCurrQuery);
        pst6.setString(1, mas_ref);
        logger.info("-----------Query---------------------" + pst6.getQueryString());
        ResultSet rs5 = pst6.executeQuery();
        if (rs5.next())
        {
          accValue = new PrintOurBankVO();
          accNo = commonMethods.getEmptyIfNull(rs5.getString("ACC_NO")).trim();
          accValue.setFircCurrRec(commonMethods.getEmptyIfNull(rs5.getString("PURCHASE_CCY")).trim());
          accValue.setFircAmount(commonMethods.getEmptyIfNull(rs5.getString("PURCHASE_AMT")).trim());
          accValue.setFircEX(commonMethods.getEmptyIfNull(rs5.getString("EX_RATE")).trim());
          accValue.setFircCurr(commonMethods.getEmptyIfNull(rs5.getString("SALE_CCY")).trim());
          accValue.setFircConAmt(commonMethods.getEmptyIfNull(rs5.getString("SALE_AMT")).trim());
          acclist.add(accValue);
          fxCount++;
        }
        closePreparedStmtResultSet(pst6, rs5);
      }
      ourBankVO.setAccNo(accNo);
      ourBankVO.setAccList(acclist);

 
      String amtQuery = "SELECT Amount_Word_Converter(" + ourBankVO.getFircConAmt() + ") AS AMT_WORD FROM DUAL";
      LoggableStatement pst7 = new LoggableStatement(con, amtQuery);
      logger.info("pst7 - " + pst7.getQueryString());
      ResultSet rs6 = pst7.executeQuery();
      if (rs6.next()) {
        ourBankVO.setAmountWord(commonMethods.getEmptyIfNull(rs6.getString("AMT_WORD")).trim());
      }
      closePreparedStmtResultSet(pst7, rs6);
    }
    catch (Exception exception)
    {
      logger.info("---------------------printOurBankDetails------------exception------" + exception);
      throwDAOException(exception);
    }
    finally
    {
      closeSqlRefferance(result, loggableStatement, con);
    }
    logger.info("Exiting Method");
    return ourBankVO;
  }
  public String evaluateAmount(String amount)
    throws DAOException
  {
    logger.info("Entering Method");
    String output = null;
    Connection con = null;
    CallableStatement stmt = null;
    ResultSet rs = null;
    try
    {
      con = DBConnectionUtility.getConnection();
      String query = "SELECT AMOUNT_WORD_CONVERTER(" + amount + ") AS AMOUNT_WORDS FROM DUAL";
      logger.info("Purpose Code -->" + query);
      stmt = con.prepareCall(query);
      rs = stmt.executeQuery();
      if (rs.next()) {
        output = rs.getString("AMOUNT_WORDS");
      }
    }
    catch (Exception exception)
    {
      throwDAOException(exception);
    }
    finally
    {
      closeSqlRefferance(rs, stmt, con);
    }
    logger.info("Exiting Method");
    return output;
  }
  public String printStatus(String refNo)
    throws DAOException
  {
    logger.info("---------printStatus-----------");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    String billRefNoVal = null;
    String status = null;
    try
    {
      con = DBConnectionUtility.getConnection();
      if (refNo != null) {
        billRefNoVal = refNo.trim();
      }
      String query = "SELECT PRINT_COUNT from ETT_FIRC_LODGEMENT WHERE trim(TRANS_REF_NO)=?";
      pst = new LoggableStatement(con, query);
      pst.setString(1, billRefNoVal);
      logger.info("---------printStatus-----query------" + pst.getQueryString());
      rs = pst.executeQuery();
      if (rs.next()) {
        status = rs.getString("PRINT_COUNT");
      }
    }
    catch (Exception e)
    {
      logger.info("---------printStatus--------exception---" + e);
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    logger.info("Exiting Method");
    return status;
  }
  public boolean sentAdviceDataBulk(String cifNo, ArrayList pdfFileNameArray)
    throws DAOException
  {
    logger.info("-------------sentAdviceData------------");
    Connection con = null;
    LoggableStatement loggableStatement = null;
    ResultSet result = null;
    CommonMethods commonMethods = null;
    boolean emailStaus = false;
    boolean mailStatus = true;
    String errorDesc = "";
    String hostNodeName = "";
    String ccEmailIdsLogList = "";
    String toEmailIdsLogList = "";
    String bccEmailIdsLogList = "";
    Timestamp processTime = null;
    Timestamp tiReqTime = null;
    Timestamp bankReqTime = null;
    Timestamp bankResTime = null;
    Timestamp tiResTime = null;
    String[] ccEmailIds = null;
    String[] bccEmailIds = null;
    String attachFileName = "";
    byte[] attachData = null;
    String emailSubject = "";
    try
    {
      con = DBConnectionUtility.getConnection();
      commonMethods = new CommonMethods();
      if (!CommonMethods.isNull(cifNo.trim()))
      {
        logger.info("Email Entering");
        String[] emailAddr = null;

 
        String query = "SELECT EMAIL FROM SX20LF WHERE TRIM(SXCUS1) = ?";
        LoggableStatement lst1 = new LoggableStatement(con, query);
        lst1.setString(1, cifNo.trim());
        logger.info("-------------sentAdviceData-------query--111---" + lst1.getQueryString());
        ResultSet rst2 = lst1.executeQuery();
        if (rst2.next())
        {
          String email = rst2.getString("EMAIL");
          if (email != null)
          {
            emailAddr = commonMethods.getEmptyIfNull(email).trim().split(",");
            logger.info("FIRC Advice Email Address-->" + emailAddr);
          }
        }
        closePreparedStmtResultSet(lst1, rst2);
        for (int counts = 0; counts < pdfFileNameArray.size(); counts++) {
          if (emailAddr != null) {
            emailSubject = "FOREIGN INWARD REMITTANCE ADVICE  ";
          }
        }
        String emailBodyText = "Dear Sir/Madam,\n  THIS IS A SYSTEM GENERATED EMAIL. PLEASE DO NOT REPLY TO THIS EMAIL.\n\n  Regards\n Kotak Mahindra Bank Ltd";

 
 
 
        logger.info("FIRC email process initiated new");

 
 
 
        hostNodeName = NotificationUtil.getCurrentHost();
        logger.info("HostNodeName: " + hostNodeName);
        tiReqTime = NotificationUtil.getSqlLocalTimestamp();
        HashMap<String, String> bridgePropertiesMap = new HashMap();
        bridgePropertiesMap = NotificationUtil.getBridgePropertiesConfigMap();
        String SMTPHost = (String)bridgePropertiesMap.get(NotificationUtil.SMTP_HOST);
        String SMTPPort = (String)bridgePropertiesMap.get(NotificationUtil.SMTP_PORT);
        String EmailUser = (String)bridgePropertiesMap.get(NotificationUtil.EMAIL_USER);
        String EmailPassword = "";

 
        Properties props = System.getProperties();
        props.put("mail.smtp.host", SMTPHost);
        props.put("mail.smtp.port", SMTPPort);
        props.put("mail.smtp.auth", Boolean.valueOf(false));
        Session session = Session.getInstance(props, new FIRCOurBankDAO.1(this, EmailUser));

 
 
 
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(EmailUser));
        if (emailAddr != null)
        {
          InternetAddress[] toAddress = new InternetAddress[emailAddr.length];
          for (int i = 0; i < emailAddr.length; i++)
          {
            toAddress[i] = new InternetAddress(emailAddr[i].trim());
            logger.info("SetTO >-->>" + toAddress[i] + "<<--<");
            message.addRecipient(Message.RecipientType.TO, toAddress[i]);
            if (i == 0) {
              toEmailIdsLogList = emailAddr[i].toString();
            } else {
              toEmailIdsLogList = toEmailIdsLogList + "," + toAddress[i];
            }
          }
        }
        else
        {
          toEmailIdsLogList = "No recipient email addresses. TO address is empty";
          logger.info(toEmailIdsLogList);
        }
        if (ccEmailIds != null)
        {
          InternetAddress[] ccAddress = new InternetAddress[ccEmailIds.length];
          for (int i = 0; i < ccEmailIds.length; i++)
          {
            ccAddress[i] = new InternetAddress(ccEmailIds[i].trim());
            logger.info("SetCC >-->>" + ccAddress[i] + "<<--<");
            message.addRecipient(Message.RecipientType.CC, ccAddress[i]);
            if (i == 0) {
              ccEmailIdsLogList = ccAddress[i].toString();
            } else {
              ccEmailIdsLogList = ccEmailIdsLogList + "," + ccAddress[i];
            }
          }
        }
        if (bccEmailIds != null)
        {
          InternetAddress[] bccAddress = new InternetAddress[bccEmailIds.length];
          for (int i = 0; i < bccEmailIds.length; i++)
          {
            bccAddress[i] = new InternetAddress(bccEmailIds[i].trim());
            logger.info("SetBCC >-->>" + bccAddress[i] + "<<--<");
            message.addRecipient(Message.RecipientType.BCC, bccAddress[i]);
            bccEmailIdsLogList = bccEmailIdsLogList + "," + bccAddress[i];
          }
        }
        message.setSubject(emailSubject);
        message.setSentDate(new Date());
        Multipart multiPart = new MimeMultipart();
        BodyPart textPart = new MimeBodyPart();

 
 
 
        String emailBody = emailBodyText.replaceAll("(\r\n|\n)", "<br />");
        String htmlBodyContent = "<html><head><style>html,body{height:297mm;width:210mm;}</style></head><body><p style=\"font-size:15px; color:#1f497d; font-family: Calibri;text-align:left\">" + 
          emailBody + 
          "</p></body></html>";
        textPart.setContent(htmlBodyContent, "text/html; charset=utf-8");
        multiPart.addBodyPart(textPart);
        for (int counts = 0; counts < pdfFileNameArray.size(); counts++)
        {
          attachFileName = pdfFileNameArray.get(counts) + ".pdf";
          File file = new File(
            ActionConstants.FILE_LOCATION + pdfFileNameArray.get(counts) + ".pdf");
          attachData = fileToArrayOfBytes(file);
          if (attachData != null)
          {
            MimeBodyPart attachFiles = new MimeBodyPart();
            attachFiles.setFileName(attachFileName);
            attachFiles.setContent(attachData, "application/pdf");
            multiPart.addBodyPart(attachFiles);
          }
          else
          {
            logger.info("FIRC Attchment data is null");
          }
        }
        message.setContent(multiPart);
        bankReqTime = NotificationUtil.getSqlLocalTimestamp();
        Transport.send(message);
        mailStatus = true;
        bankResTime = NotificationUtil.getSqlLocalTimestamp();

 
 
        logger.info("Milestone 10L FIRC Final : " + mailStatus);
        return mailStatus;
      }
    }
    catch (Exception exception)
    {
      logger.error("sentAdviceData---Exception-----" + exception);
      logger.info("sentAdviceData---Exception-----" + exception);
      throwDAOException(exception);
    }
    finally
    {
      closeSqlRefferance(result, loggableStatement, con);
    }
    closeSqlRefferance(result, loggableStatement, con);

 
 
    logger.info("Exiting Method");
    return emailStaus;
  }
  public boolean sentAdviceData(String transRefNo)
    throws DAOException
  {
    logger.info("-------------sentAdviceData------------");
    Connection con = null;
    LoggableStatement loggableStatement = null;
    ResultSet result = null;
    CommonMethods commonMethods = null;
    boolean emailStaus = false;
    try
    {
      con = DBConnectionUtility.getConnection();
      commonMethods = new CommonMethods();
      String mas_ref = commonMethods.getEmptyIfNull(transRefNo).trim();
      String cifNo = null;
      String query1 = "SELECT CIF_NO FROM ETT_FIRC_LODGEMENT WHERE TRIM(TRANS_REF_NO)=?";
      LoggableStatement pst1 = new LoggableStatement(con, query1);
      pst1.setString(1, mas_ref);
      logger.info("-------------sentAdviceData-------query-----" + pst1.getQueryString());
      ResultSet rs1 = pst1.executeQuery();
      if (rs1.next())
      {
        cifNo = commonMethods.getEmptyIfNull(rs1.getString("CIF_NO")).trim();
        logger.info("-------------sentAdviceData-------query-count----" + cifNo);
      }
      closePreparedStmtResultSet(pst1, rs1);
      if (!CommonMethods.isNull(cifNo))
      {
        logger.info("Email Entering");
        String[] emailAddr = null;

 
        String query = "SELECT EMAIL FROM SX20LF WHERE TRIM(SXCUS1) = ?";
        LoggableStatement lst1 = new LoggableStatement(con, query);
        lst1.setString(1, cifNo);
        logger.info("-------------sentAdviceData-------query--111---" + lst1.getQueryString());
        ResultSet rst2 = lst1.executeQuery();
        if (rst2.next())
        {
          String email = rst2.getString("EMAIL");
          if (email != null)
          {
            emailAddr = commonMethods.getEmptyIfNull(email).trim().split(",");
            logger.info("FIRC Advice Email Address-->" + emailAddr);
          }
        }
        closePreparedStmtResultSet(lst1, rst2);
        if (emailAddr != null)
        {
          String attachFileName = "FIRA" + transRefNo + ".pdf";
          File file = new File(
            ActionConstants.FILE_LOCATION + "FIRA" + transRefNo + ".pdf");
          byte[] attachData = fileToArrayOfBytes(file);
          String emailSubject = "FOREIGN INWARD REMITTANCE ADVICE - FIRA" + 
            transRefNo;
          String emailBodyText = "Dear Sir/Madam,\n  THIS IS A SYSTEM GENERATED EMAIL. PLEASE DO NOT REPLY TO THIS EMAIL.\n\n  Regards\n Kotak Mahindra Bank Ltd";

 
 
 
 
 
 
 
          EmailNotification emailObj = new EmailNotification();
          emailStaus = emailObj.sendEmailNotification("EMAIL", "FIRA", 
            "FIRA" + transRefNo, "CRE001", cifNo, emailSubject, emailBodyText, 
            attachFileName, emailAddr, null, null, attachData, cifNo);
          logger.info("FIRC Advice Email Status------>" + emailStaus);
        }
      }
    }
    catch (Exception exception)
    {
      logger.error("sentAdviceData---Exception-----" + exception);
      logger.info("sentAdviceData---Exception-----" + exception);
      throwDAOException(exception);
    }
    finally
    {
      closeSqlRefferance(result, loggableStatement, con);
    }
    logger.info("Exiting Method");
    return emailStaus;
  }
  public byte[] fileToArrayOfBytes(File file)
  {
    FileInputStream fileInputStream = null;
    byte[] bytesArray = null;
    try
    {
      bytesArray = new byte[(int)file.length()];

 
      fileInputStream = new FileInputStream(file);
      fileInputStream.read(bytesArray);
    }
    catch (IOException e)
    {
      e.printStackTrace();
      if (fileInputStream != null) {
        try
        {
          fileInputStream.close();
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
      }
    }
    finally
    {
      if (fileInputStream != null) {
        try
        {
          fileInputStream.close();
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
      }
    }
    return bytesArray;
  }
}
