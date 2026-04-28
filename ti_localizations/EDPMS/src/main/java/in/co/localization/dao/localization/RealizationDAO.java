package in.co.localization.dao.localization;

import in.co.localization.dao.AbstractDAO;
import in.co.localization.dao.exception.DAOException;
import in.co.localization.utility.ActionConstantsQuery;
import in.co.localization.utility.CommonMethods;
import in.co.localization.utility.DBConnectionUtility;
import in.co.localization.utility.LoggableStatement;
import in.co.localization.vo.localization.AcknowledgementListVO;
import in.co.localization.vo.localization.AcknowledgementVO;
import in.co.localization.vo.localization.InvoiceDetailsVO;
import in.co.localization.vo.localization.ShippingDetailsVO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

public class RealizationDAO
  extends AbstractDAO
  implements ActionConstantsQuery
{
  static RealizationDAO dao;
  private static Logger logger = Logger.getLogger(RealizationDAO.class.getName());
 
  public static RealizationDAO getDAO()
  {
    if (dao == null) {
      dao = new RealizationDAO();
    }
    return dao;
  }
 
  public ArrayList<AcknowledgementVO> fetchPRNShippingData(AcknowledgementListVO ackListVO)
    throws DAOException
  {
    logger.info("Entering--------fetchPRNShippingData--------- Method");
    ArrayList prnAckShippingList = null;
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    String query = null;
    AcknowledgementVO ackVO = null;
    CommonMethods commonMethods = null;
    boolean shippingNoFlag = false;
    boolean shippingDateFlag = false;
    boolean portCodeFlag = false;
    boolean formNoFlag = false;
    boolean paySeqNoFlag = false;
    int setValue = 0;
    try
    {
      con = DBConnectionUtility.getConnection();
      prnAckShippingList = new ArrayList();
     

      String PRN_SHP_QUERY = "SELECT GR_NUMBER,SHIPPINGBILLNO,TO_CHAR(TO_DATE(SHIPPINGBILLDATE, 'ddmmyyyy'),'dd-mm-yyyy') AS SHIPPINGBILLDATE, PORTCODE,FORMNO,RECORDINDICATOR,EXPORTTYPE,TO_CHAR(LEODATE, 'dd-mm-yyyy') AS LEODATE,ADCODE, PAY_SEQNO,INWARDNO,FIRCNO,REM_ADCODE,PAY_PARTY,REALIZATONCURR, TO_CHAR(REALIZATONDATE, 'dd-mm-yyyy') AS REALIZATONDATE,ERRORCODES FROM ETT_PRN_SHP_ACK_STG WHERE ERRORCODES = 'SUCCESS' AND STATUS IS NULL ";
     
      commonMethods = new CommonMethods();
     
      String shippingNo = ackListVO.getShippingBillNo();
      if (!commonMethods.isNull(shippingNo))
      {
        PRN_SHP_QUERY = PRN_SHP_QUERY + "AND SHIPPINGBILLNO =?";
        shippingNoFlag = true;
      }
      String shippingDate = commonMethods.getEmptyIfNull(ackListVO.getShippingBillDate()).replace("/", "").trim();
      if (!commonMethods.isNull(shippingDate))
      {
        PRN_SHP_QUERY = PRN_SHP_QUERY + "AND SHIPPINGBILLDATE =?";
        shippingDateFlag = true;
      }
      String portCode = ackListVO.getPortCode();
      if (!commonMethods.isNull(portCode))
      {
        PRN_SHP_QUERY = PRN_SHP_QUERY + "AND PORTCODE = ?";
        portCodeFlag = true;
      }
      String formNo = ackListVO.getFormNo();
      if (!commonMethods.isNull(formNo))
      {
        PRN_SHP_QUERY = PRN_SHP_QUERY + "AND FORMNO =?";
        formNoFlag = true;
      }
      String paySeqNo = ackListVO.getPaySeqNo();
      if (!commonMethods.isNull(paySeqNo))
      {
        PRN_SHP_QUERY = PRN_SHP_QUERY + "AND PAY_SEQNO =?";
        paySeqNoFlag = true;
      }
      String chPayStatus = ackListVO.getChPayStatus();
      logger.info("The RecordIndicator is : Inside of DAO : " + chPayStatus);
      if (!commonMethods.isNull(chPayStatus)) {
        if (chPayStatus.equals("N")) {
          PRN_SHP_QUERY = PRN_SHP_QUERY + "AND RECORDINDICATOR IN ('1')";
        } else if (chPayStatus.equals("A")) {
          PRN_SHP_QUERY = PRN_SHP_QUERY + "AND RECORDINDICATOR IN ('2')";
        } else if (chPayStatus.equals("C")) {
          PRN_SHP_QUERY = PRN_SHP_QUERY + "AND RECORDINDICATOR IN ('3')";
        }
      }
      pst = new LoggableStatement(con, PRN_SHP_QUERY);
      if (shippingNoFlag) {
        pst.setString(++setValue, shippingNo.trim());
      }
      if (shippingDateFlag) {
        pst.setString(++setValue, shippingDate.trim());
      }
      if (portCodeFlag) {
        pst.setString(++setValue, portCode.trim());
      }
      if (formNoFlag) {
        pst.setString(++setValue, formNo.trim());
      }
      if (paySeqNoFlag) {
        pst.setString(++setValue, paySeqNo.trim());
      }
      logger.info("Entering--------fetchPRNShippingData---------query" + pst.getQueryString());
      rs = pst.executeQuery();
      while (rs.next())
      {
        ackVO = new AcknowledgementVO();
        ackVO.setGrNumber(rs.getString("GR_NUMBER"));
        ackVO.setShippingBillNo(rs.getString("SHIPPINGBILLNO"));
        ackVO.setShippingBillDate(rs.getString("SHIPPINGBILLDATE"));
        ackVO.setPortCode(rs.getString("PORTCODE"));
        ackVO.setFormNo(rs.getString("FORMNO"));
        ackVO.setExportType(rs.getString("EXPORTTYPE"));
        ackVO.setRecordInd(rs.getString("RECORDINDICATOR"));
        ackVO.setLeoDate(rs.getString("LEODATE"));
        ackVO.setExAdcode(rs.getString("ADCODE"));
        ackVO.setPaymentSequence(rs.getString("PAY_SEQNO"));
        ackVO.setIrmNumber(rs.getString("INWARDNO"));
        ackVO.setFircNo(rs.getString("FIRCNO"));
        ackVO.setRemittanceAdCode(rs.getString("REM_ADCODE"));
        ackVO.setRealizationCurr(rs.getString("REALIZATONCURR"));
        ackVO.setRealizationDate(rs.getString("REALIZATONDATE"));
        ackVO.setThirdParty(rs.getString("PAY_PARTY"));
        String errorCode = rs.getString("ERRORCODES");
        ackVO.setErrorDes(errorCode);
        prnAckShippingList.add(ackVO);
      }
    }
    catch (Exception exception)
    {
      logger.info("Entering--------fetchPRNShippingData---------exception----------" + exception);
      throwDAOException(exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    logger.info("Exiting Method");
    return prnAckShippingList;
  }
 
  public ShippingDetailsVO prnAckDataView(AcknowledgementListVO ackListVO)
    throws DAOException
  {
    logger.info("Entering Method");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    ShippingDetailsVO shippingVO = null;
    try
    {
      con = DBConnectionUtility.getConnection();
     
      String query = "SELECT SHIPPINGBILLNO,TO_CHAR(TO_DATE(SHIPPINGBILLDATE, 'ddmmyyyy'),'dd-mm-yyyy') AS SHIPPINGBILLDATE, PORTCODE,FORMNO,RECORDINDICATOR,EXPORTTYPE,TO_CHAR(LEODATE, 'dd-mm-yyyy') AS LEODATE,ADCODE, PAY_SEQNO,INWARDNO,FIRCNO,REM_ADCODE,PAY_PARTY,REALIZATONCURR, TO_CHAR(REALIZATONDATE, 'dd-mm-yyyy') AS REALIZATONDATE FROM ETT_PRN_SHP_ACK_STG WHERE GR_NUMBER = ? ";
     
      pst = new LoggableStatement(con, query);
      pst.setString(1, ackListVO.getUniqueSeqNo());
      rs = pst.executeQuery();
      if (rs.next())
      {
        shippingVO = new ShippingDetailsVO();
        shippingVO.setShippingBillNo(rs.getString("SHIPPINGBILLNO"));
        shippingVO.setShippingBillDate(rs.getString("SHIPPINGBILLDATE"));
        shippingVO.setPortCode(rs.getString("PORTCODE"));
        shippingVO.setFormNo(rs.getString("FORMNO"));
        shippingVO.setExportType(rs.getString("EXPORTTYPE"));
        shippingVO.setRecInd(rs.getString("RECORDINDICATOR"));
        shippingVO.setLeoDate(rs.getString("LEODATE"));
        shippingVO.setAdCode(rs.getString("ADCODE"));
        shippingVO.setPaySeqNo(rs.getString("PAY_SEQNO"));
        shippingVO.setInwardNo(rs.getString("INWARDNO"));
        shippingVO.setFircNo(rs.getString("FIRCNO"));
        shippingVO.setRemAdcode(rs.getString("REM_ADCODE"));
        shippingVO.setRealizationCurr(rs.getString("REALIZATONCURR"));
        shippingVO.setRelDate(rs.getString("REALIZATONDATE"));
        shippingVO.setPayParty(rs.getString("PAY_PARTY"));
      }
      ArrayList invoiceList = getPrnInvoiceData(ackListVO);
      if (invoiceList != null) {
        shippingVO.setInvoiceList(invoiceList);
      }
    }
    catch (Exception exception)
    {
      throwDAOException(exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    logger.info("Exiting Method");
    return shippingVO;
  }
 
  public ArrayList<InvoiceDetailsVO> getPrnInvoiceData(AcknowledgementListVO ackListVO)
    throws DAOException
  {
    logger.info("Entering Method");
    ArrayList invoiceList = null;
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    try
    {
      con = DBConnectionUtility.getConnection();
     
      invoiceList = new ArrayList();
     
      String query = "SELECT INVOICESERIALNO,INVOICENO,TO_CHAR(INVOICEDATE, 'dd-mm-yyyy') AS INVOICEDATE, ACCOUNTNUMBER,to_char(BANK_CHG,'999,999,999,999,999.99') as BANKINGCHARGESAMT, to_char(FOBAMT,'999,999,999,999,999.99') as FOBAMT,to_char(FOBAMTIC,'999,999,999,999,999.99') as FOBAMTIC, to_char(FREIGHTAMT,'999,999,999,999,999.99') as FREIGHTAMT,to_char(FREIGHTAMTIC,'999,999,999,999,999.99') as FREIGHTAMTIC, to_char(INSURANCEAMT,'999,999,999,999,999.99') as INSURANCEAMT,to_char(INSURANCEAMTIC,'999,999,999,999,999.99') as INSURANCEAMTIC, CLOSEOFBILLINDICATOR,REMITTERNAME,REMITTERCOUNTRY FROM ETT_PRN_SHP_INV_ACK_STG WHERE GR_SHP_NO = ? ";
     
      pst = new LoggableStatement(con, query);
      pst.setString(1, ackListVO.getUniqueSeqNo());
      rs = pst.executeQuery();
      while (rs.next())
      {
        InvoiceDetailsVO invVO = new InvoiceDetailsVO();
        invVO.setInvoiceSerialNo(rs.getString("INVOICESERIALNO"));
        invVO.setInvoiceNo(rs.getString("INVOICENO"));
        invVO.setInvoiceDate(rs.getString("INVOICEDATE"));
        invVO.setAccountNo(rs.getString("ACCOUNTNUMBER"));
        invVO.setBankCharges(rs.getString("BANKINGCHARGESAMT"));
        invVO.setFobAmt(rs.getString("FOBAMT"));
        invVO.setFobAmtIC(rs.getString("FOBAMTIC"));
        invVO.setFreightAmt(rs.getString("FREIGHTAMT"));
        invVO.setFreightAmtIC(rs.getString("FREIGHTAMTIC"));
        invVO.setInsuranceAmt(rs.getString("INSURANCEAMT"));
        invVO.setInsuranceAmtIC(rs.getString("INSURANCEAMTIC"));
        invVO.setCloseInd(rs.getString("CLOSEOFBILLINDICATOR"));
        invVO.setRemitterName(rs.getString("REMITTERNAME"));
        invVO.setRemitterCountry(rs.getString("REMITTERCOUNTRY"));
        invoiceList.add(invVO);
      }
    }
    catch (Exception exception)
    {
      throwDAOException(exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    logger.info("Exiting Method");
    return invoiceList;
  }
 
  public int storeCancelPRNData(AcknowledgementListVO ackListVO, ShippingDetailsVO shippingVO)
    throws DAOException
  {
    logger.info("Exiting ---------storeCancelPRNData---------Method");
    int iRet = 0;
    String sBCQuery = "";
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    try
    {
      con = DBConnectionUtility.getConnection();
     
      HttpSession session = ServletActionContext.getRequest().getSession();
      String userName = (String)session.getAttribute("loginedUserName");
     

      String sRcdIndStatus = shippingVO.getPayStatus();
      String sBankChrgesAmount = shippingVO.getBankChargesAmt();
      if (sRcdIndStatus.equalsIgnoreCase("C"))
      {
        logger.info("This is Inside of Cancel : ");
        String parQuery = " ETT_PRN_SHP_ACK_STG SET STATUS = ?, USERID = ?, UPDTIME = SYSTIMESTAMP WHERE TRIM(GR_NUMBER) = ?";
        pst = new LoggableStatement(con, parQuery);
        pst.setString(1, sRcdIndStatus);
        pst.setString(2, userName);
        pst.setString(3, ackListVO.getUniqueSeqNo());
       
        logger.info("Exiting ---------ETT_PRN_SHP_ACK_ SUPDATE--------Method" + pst.getQueryString());
        int ie = pst.executeUpdate();
        logger.info("sRcdIndStatus : " + sRcdIndStatus);
        logger.info("userName : " + userName);
        logger.info("ackListVO.getUniqueSeqNo() : " + ackListVO.getUniqueSeqNo());
       
        closePreparedStatement(pst);
        if (ie > 0)
        {
          String chdQuery = "UPDATE ETT_PRN_SHP_INV_ACK_STG SET STATUS = ? WHERE GR_SHP_NO = ?";
          pst = new LoggableStatement(con, chdQuery);
          pst.setString(1, sRcdIndStatus);
          pst.setString(2, ackListVO.getUniqueSeqNo());
          logger.info("Exiting ---UPDATE------ETT_PRN_SHP_INV_ACK_STG SUPDATE--------Method" + pst.getQueryString());
          int a = pst.executeUpdate();
         
          logger.info("Exiting ---UPDATE-------count-----Method" + a);
          closePreparedStatement(pst);
        }
      }
      if (sRcdIndStatus.equalsIgnoreCase("A"))
      {
        logger.info("The Value Inside of Approved is : 1 : " + sBankChrgesAmount);
        logger.info("The Value Inside of Approved is : 2 : " + shippingVO.getPaySeqNo());
        sBCQuery = "UPDATE ETT_GR_REL_SHP_TBL SET PAYMENT_SEQNO = ETT_GR_REL_SHP_SEQ_NO,BANKCHRGS = ?,RSTATUS = 'N' WHERE PAYMENT_SEQNO = ?";
        pst = new LoggableStatement(con, sBCQuery);
        pst.setString(1, sBankChrgesAmount);
        pst.setString(2, shippingVO.getPaySeqNo());
       
        logger.info("Exiting ---UPDATE------ETT_GR_REL_SHP_TBL SUPDATE--------Method" + pst.getQueryString());
        int ie = pst.executeUpdate();
       
        logger.info("Exiting ---UPDATE-------count-----Method" + ie);
       
        closePreparedStatement(pst);
        if (ie > 0)
        {
          sBCQuery = "UPDATE ETT_GR_REL_INV_TBL SET RSTATUS = 'N', BANKCHRGS = (SELECT ?/CNT FROM (SELECT COUNT(1) CNT FROM ETT_GR_REL_INV_TBL WHERE GRSHPNO IN (SELECT ETT_GR_REL_SHP_TBL.GRNUMBER FROM ETT_GR_REL_SHP_TBL WHERE PAYMENT_SEQNO = ?))) WHERE GRSHPNO = (SELECT GRNUMBER FROM ETT_GR_REL_SHP_TBL WHERE PAYMENT_SEQNO = ?)";
          pst = new LoggableStatement(con, sBCQuery);
          pst.setString(1, sBankChrgesAmount);
          pst.setString(2, shippingVO.getPaySeqNo());
          pst.setString(3, shippingVO.getPaySeqNo());
          logger.info("Exiting ---UPDATE------ETT_GR_REL_INV_TBLS SUPDATE--------Method" + pst.getQueryString());
          int as = pst.executeUpdate();
          logger.info("Exiting ---UPDATE-- ETT_GR_REL_INV_TBLS-----count-----Method" + as);
          closePreparedStatement(pst);
        }
        logger.info("sRcdIndStatus : " + sRcdIndStatus);
        logger.info("userName : " + userName);
        logger.info("ackListVO.getUniqueSeqNo() : " + ackListVO.getUniqueSeqNo());
       
        String parQuery = "UPDATE ETT_PRN_SHP_ACK_STG SET STATUS = ?, USERID = ?, UPDTIME = SYSTIMESTAMP WHERE TRIM(GR_NUMBER) = ?";
        pst = new LoggableStatement(con, parQuery);
        pst.setString(1, sRcdIndStatus);
        pst.setString(2, userName);
        pst.setString(3, ackListVO.getUniqueSeqNo());
        logger.info("Exiting ---UPDATE------ETT_PRN_SHP_ACK_STG SUPDATE--------Method" + pst.getQueryString());
        int i = pst.executeUpdate();
        logger.info("Exiting ---UPDATE-- ETT_PRN_SHP_ACK_STG-----count-----Method" + i);
        closePreparedStatement(pst);
        if (i > 0)
        {
          String chdQuery = "UPDATE ETT_PRN_SHP_INV_ACK_STG SET STATUS = ? WHERE GR_SHP_NO = ?";
          pst = new LoggableStatement(con, chdQuery);
          pst.setString(1, sRcdIndStatus);
          pst.setString(2, ackListVO.getUniqueSeqNo());
         
          logger.info("Exiting ---UPDATE------ETT_PRN_SHP_INV_ACK_STG SUPDATE--------Method" + pst.getQueryString());
          int abc = pst.executeUpdate();
          logger.info("Exiting ---UPDATE-- ETT_PRN_SHP_INV_ACK_STG-----count-----Method" + abc);
          closePreparedStatement(pst);
        }
      }
    }
    catch (Exception ex)
    {
      logger.info("Exiting ---------storeCancelPRNData---------Excpeiton" + ex);
      throwDAOException(ex);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    logger.info("Exiting Method");
    return iRet;
  }
}