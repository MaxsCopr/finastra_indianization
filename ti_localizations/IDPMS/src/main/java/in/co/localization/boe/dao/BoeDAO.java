package in.co.localization.boe.dao;
 
import in.co.boe.vo.TransactionVO;
import in.co.localization.dao.AbstractDAO;
import in.co.localization.dao.exception.DAOException;
import in.co.localization.utility.ActionConstantsQuery;
import in.co.localization.utility.CommonMethods;
import in.co.localization.utility.DBConnectionUtility;
import in.co.localization.utility.LoggableStatement;
import in.co.localization.vo.localization.BOEDataSearchVO;
import in.co.localization.vo.localization.BoeVO;
import in.co.localization.vo.localization.InvoiceDetailsVO;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
 
public class BoeDAO
  extends AbstractDAO
  implements ActionConstantsQuery
{
  private static Logger logger = Logger.getLogger(BoeDAO.class.getName());
  static BoeDAO dao;
  public static BoeDAO getDAO()
  {
    if (dao == null) {
      dao = new BoeDAO();
    }
    return dao;
  }
  public BoeVO fetchBOEExtension(BoeVO boevo)
    throws DAOException
  {
    logger.info("-----------fetchBOEExtension-----------");
    Connection con = null;
    LoggableStatement pst = null;
    ResultSet rs = null;
    label510:
    try
    {
      con = DBConnectionUtility.getConnection();
      int boeExtCount = 0;
      String query = "SELECT COUNT(*) AS EXT_COUNT FROM ETT_BOE_EXT_DATA  WHERE BOENUMBER = ? AND TO_CHAR(BOEDATE,'DD/MM/YYYY') = ?  AND PORTOFDISCHARGE = ? ORDER BY MAKER_CREATEDON DESC";

 
      LoggableStatement pst1 = new LoggableStatement(con, query);
      pst1.setString(1, boevo.getBoeNumber());
      pst1.setString(2, boevo.getBoeDate());
      pst1.setString(3, boevo.getPortCode());
      ResultSet rs1 = pst1.executeQuery();
      logger.info("-----------fetchBOEExtension-----Query------" + pst1.getQueryString());
      if (rs1.next()) {
        boeExtCount = rs1.getInt("EXT_COUNT");
      }
      closeStatementResultSet(pst1, rs1);
      if (boeExtCount > 0)
      {
        pst = new LoggableStatement(con, "SELECT ADCODE,IECODE,RECORDINDICATOR,EXTENSIONBY,LETTERNO,TO_CHAR(LETTERDATE,'DD/MM/YYYY') AS LETTERDATE, TO_CHAR(EXTENSIONDATE,'DD/MM/YYYY') AS PREVEXTDATE,REMARKS,CHECKER_REMARKS, EXT_INDICATOR FROM ETT_BOE_EXT_DATA WHERE BOENUMBER = ?  AND TO_CHAR(BOEDATE,'DD/MM/YYYY') = ? AND PORTOFDISCHARGE = ? ORDER BY MAKER_CREATEDON DESC");
        pst.setString(1, boevo.getBoeNumber());
        pst.setString(2, boevo.getBoeDate());
        pst.setString(3, boevo.getPortCode());
        rs = pst.executeQuery();
        logger.info("-----------fetchBOEExtension--BOE_EXT_DATA---Query------" + pst.getQueryString());
        if (rs.next())
        {
          boevo.setAdCode(rs.getString("ADCODE"));
          boevo.setIeCode(rs.getString("IECODE"));
          boevo.setRecordIndicator(rs.getString("RECORDINDICATOR"));
          boevo.setApprovalBy(rs.getString("EXTENSIONBY"));
          boevo.setExtLetterNo(rs.getString("LETTERNO"));
          boevo.setExtLetterDate(rs.getString("LETTERDATE"));
          boevo.setExtDate(rs.getString("PREVEXTDATE"));
          boevo.setExtRemarks(rs.getString("REMARKS"));
          boevo.setCheckerRemarks(rs.getString("CHECKER_REMARKS"));
          break label510;
        }
      }
      else
      {
        pst = new LoggableStatement(con, "SELECT BOE_AD_CODE,BOE_IE_CODE,BOE_PORT_OF_SHIPMENT,TRIM(BOE_RECORD_INDICATOR) AS RECORD_INDICATOR FROM ETT_BOE_DETAILS WHERE BOE_NUMBER=? AND TO_CHAR(BOE_DATE,'DD/MM/YYYY') = ? AND BOE_PORT_OF_DISCHARGE = ? ");
        pst.setString(1, boevo.getBoeNumber());
        pst.setString(2, boevo.getBoeDate());
        pst.setString(3, boevo.getPortCode());
        rs = pst.executeQuery();
        logger.info("-----------fetchBOEExtension--BOE_SEARCH---Query------" + pst.getQueryString());
        if (rs.next())
        {
          boevo.setAdCode(rs.getString("BOE_AD_CODE"));
          boevo.setIeCode(rs.getString("BOE_IE_CODE"));
          boevo.setRecordIndicator(rs.getString("RECORD_INDICATOR"));
        }
      }
    }
    catch (Exception e)
    {
      logger.info("-----------fetchBOEExtension-----exception------" + e);
      throwDAOException(e);
    }
    finally
    {
      closeSqlRefferance(rs, pst, con);
    }
    logger.info("Exiting Method");
    return boevo;
  }
  public BoeVO fetchBOEClosure(BoeVO boevo)
    throws DAOException
  {
    logger.info("fetchBOEClosure------------");
    Connection con = null;
    LoggableStatement pst = null;
    ResultSet rs = null;
    ArrayList<InvoiceDetailsVO> invoiecList = null;
    try
    {
      con = DBConnectionUtility.getConnection();
      int boeClosureCount = 0;
      String query = "SELECT COUNT(*) AS CLOSURE_COUNT FROM ETT_BOE_CLOSURE_DATA  WHERE BOENUMBER = ? AND TO_CHAR(BOEDATE,'DD/MM/YYYY') = ?  AND PORTOFDISCHARGE = ? ORDER BY MAKER_CREATEDON DESC";

 
      LoggableStatement pst1 = new LoggableStatement(con, query);
      pst1.setString(1, boevo.getBoeNumber());
      pst1.setString(2, boevo.getBoeDate());
      pst1.setString(3, boevo.getPortCode());
      ResultSet rs1 = pst1.executeQuery();
      logger.info("fetchBOEClosure----------pst query------------" + pst1.getQueryString());
      if (rs1.next())
      {
        boeClosureCount = rs1.getInt("CLOSURE_COUNT");
        logger.info("boeClosureCount----------value------------" + boeClosureCount);
      }
      closeStatementResultSet(pst1, rs1);
      if (boeClosureCount > 0)
      {
        pst = new LoggableStatement(con, "SELECT ADCODE,IECODE,RECORDINDICATOR,ADJ_IND,TO_CHAR(ADJ_DATE,'DD/MM/YYYY') AS ADJ_DATE,DOCUMENT_NO, TO_CHAR(DOCUMENT_DATE,'DD/MM/YYYY') AS DOCUMENT_DATE,DOCUMENT_PORT,APPROVEDBY, LETTERNO,TO_CHAR(LETTERDATE,'DD/MM/YYYY') AS LETTERDATE,REMARKS,CHECKER_REMARKS, BOE_CLOS_IND  FROM ETT_BOE_CLOSURE_DATA WHERE BOENUMBER = ?  AND TO_CHAR(BOEDATE,'DD/MM/YYYY') = ? AND PORTOFDISCHARGE = ? ORDER BY MAKER_CREATEDON DESC");
        pst.setString(1, boevo.getBoeNumber());
        pst.setString(2, boevo.getBoeDate());
        pst.setString(3, boevo.getPortCode());
        logger.info("BOE_CLO_DATA  Query--------------" + pst.getQueryString());
        rs = pst.executeQuery();
        if (rs.next())
        {

            boevo.setAdCode(rs.getString("ADCODE"));
            boevo.setIeCode(rs.getString("IECODE"));
            boevo.setAdjClosureIndicator(rs.getString("ADJ_IND"));
            boevo.setAdjClosureDate(rs.getString("ADJ_DATE"));
            boevo.setDocNo(rs.getString("DOCUMENT_NO"));
            boevo.setDocDate(rs.getString("DOCUMENT_DATE"));
            boevo.setDocPort(rs.getString("DOCUMENT_PORT"));
            boevo.setApprovedBy(rs.getString("APPROVEDBY"));
            boevo.setRecordIndicator(rs.getString("RECORDINDICATOR"));
            boevo.setClosureLetterNo(rs.getString("LETTERNO"));
            boevo.setClosureLetterDate(rs.getString("LETTERDATE"));
            boevo.setClosureRemarks(rs.getString("REMARKS"));
            boevo.setCheckerRemarks(rs.getString("CHECKER_REMARKS"));
            boevo.setBoeCloInd(rs.getString("BOE_CLOS_IND"));

          }

        }

        else

        {

          pst = new LoggableStatement(con, "SELECT BOE_AD_CODE,BOE_IE_CODE,BOE_PORT_OF_SHIPMENT,TRIM(BOE_RECORD_INDICATOR) AS RECORD_INDICATOR FROM ETT_BOE_DETAILS WHERE BOE_NUMBER=? AND TO_CHAR(BOE_DATE,'DD/MM/YYYY') = ? AND BOE_PORT_OF_DISCHARGE = ? ");

          pst.setString(1, boevo.getBoeNumber());
          pst.setString(2, boevo.getBoeDate());
          pst.setString(3, boevo.getPortCode());

          logger.info("BOE_SEARCH  Query--------------" + pst.getQueryString());

          rs = pst.executeQuery();

          if (rs.next())

          {

            boevo.setAdCode(rs.getString("BOE_AD_CODE"));
            boevo.setIeCode(rs.getString("BOE_IE_CODE"));
            boevo.setRecordIndicator(rs.getString("RECORD_INDICATOR"));

          }

        }

        int totalBoeCount = 0;

        String boeCountQuery = "SELECT SUM(BOE_COUNT) AS B_COUNT FROM (SELECT COUNT(*) AS BOE_COUNT FROM ETT_BOE_CLOSURE_DATA  WHERE BOENUMBER = ? AND TO_CHAR(BOEDATE,'DD/MM/YYYY') = ? AND PORTOFDISCHARGE = ? AND STATUS != 'R' UNION SELECT COUNT(*) AS BOE_COUNT FROM ETT_BOE_PAYMENT WHERE BOE_PAYMENT_BP_BOE_NO = ?  AND TO_CHAR(BOE_PAYMENT_BP_BOE_DT,'DD/MM/YYYY') = ? AND PORT_CODE = ? AND STATUS != 'R') ";

   
   
        LoggableStatement pst2 = new LoggableStatement(con, boeCountQuery);

        pst2.setString(1, boevo.getBoeNumber());
        pst2.setString(2, boevo.getBoeDate());
        pst2.setString(3, boevo.getPortCode());
        pst2.setString(4, boevo.getBoeNumber());
        pst2.setString(5, boevo.getBoeDate());
        pst2.setString(6, boevo.getPortCode());
        ResultSet rs2 = pst2.executeQuery();

        logger.info("BOE_COUNT  Query--------------" + pst2.getQueryString());

        if (rs2.next()) {

          totalBoeCount = rs2.getInt("B_COUNT");

        }

        closeStatementResultSet(pst2, rs2);

        if (totalBoeCount > 0) {

          boevo.setManualPartialData("Y");

        } else {

          boevo.setManualPartialData("N");

        }

        invoiecList = getInvoiceDetails(con, boevo);

        if (invoiecList.size() > 0) {

          boevo.setInvoiceList(invoiecList);

        }

      }

      catch (Exception e)

      {

        logger.info("fetchBOEClosure----------Exception" + e);

        throwDAOException(e);

      }

      finally

      {

        closeSqlRefferance(rs, pst, con);

      }

      logger.info("Exiting Method");

      return boevo;

    }

    public ArrayList<InvoiceDetailsVO> getInvoiceDetails(Connection con, BoeVO boevo)

      throws DAOException

    {

      logger.info("----------getInvoiceDetails---------");

      LoggableStatement pst = null;

      ResultSet rs = null;

      ArrayList<InvoiceDetailsVO> invoiecList = null;

      CommonMethods commonMethods = null;

      try

      {

        if (con == null) {

          con = DBConnectionUtility.getConnection();

        }

        commonMethods = new CommonMethods();

        invoiecList = new ArrayList();

        String query = "SELECT INV_SERIAL_NO,INV_NO,FOB_AMT,FOB_CURR,FRI_AMT,FRI_CURR,INS_AMT,INS_CURR,REAL_AMTIC,CLOSURE_AMTIC FROM ETTV_BOE_INV_DETAILS WHERE BOE_NUMBER = ? AND TO_CHAR(BOE_DATE,'DD/MM/YYYY') = ? AND BOE_PORT_OF_DISCHARGE = ? ORDER BY TO_NUMBER(INV_SERIAL_NO) ";

   
        pst = new LoggableStatement(con, query);

   
        pst.setString(1, commonMethods.getEmptyIfNull(boevo.getBoeNumber()).trim());
        pst.setString(2, commonMethods.getEmptyIfNull(boevo.getBoeDate()).trim());
        pst.setString(3, commonMethods.getEmptyIfNull(boevo.getPortCode()).trim());

        logger.info("----------getInvoiceDetails-----ETTV_BOE_INV_DETAILS Query----" + pst.getQueryString());

        rs = pst.executeQuery();

        while (rs.next())

        {

          InvoiceDetailsVO invoiceVO = new InvoiceDetailsVO();

          invoiceVO.setInvoiceSerialNo(rs.getString("INV_SERIAL_NO"));

          invoiceVO.setInvoiceNo(rs.getString("INV_NO"));

          String tempVal = commonMethods.getEmptyIfNull(rs.getString("INV_SERIAL_NO")).trim() + 

            ":" + commonMethods.getEmptyIfNull(rs.getString("INV_NO")).trim();

          invoiceVO.setUtilityRefNo(tempVal);

          double fobAmt = commonMethods.convertToDouble(rs.getString("FOB_AMT"));
          double friAmt = commonMethods.convertToDouble(rs.getString("FRI_AMT"));
          double insAmt = commonMethods.convertToDouble(rs.getString("INS_AMT"));

   
   
          double totalAmt = fobAmt;

          double totalInvAmt = Math.round(totalAmt * 100.0D) / 100.0D;

          double realAmtIC = commonMethods.convertToDouble(rs.getString("REAL_AMTIC"));

          double closureAmtIc = commonMethods.convertToDouble(rs.getString("CLOSURE_AMTIC"));

          double totalPaidAmt = realAmtIC + closureAmtIc;

          totalPaidAmt = Math.round(totalPaidAmt * 100.0D) / 100.0D;

          double outAmtIC = totalInvAmt - totalPaidAmt;

          outAmtIC = Math.round(outAmtIC * 100.0D) / 100.0D;

          BigDecimal toInvAmt = BigDecimal.valueOf(totalInvAmt).setScale(2, RoundingMode.HALF_UP);

          BigDecimal toOutAmt = BigDecimal.valueOf(outAmtIC).setScale(2, RoundingMode.HALF_UP);

          invoiceVO.setInvoiceAmt(String.valueOf(toInvAmt));

          invoiceVO.setInvoiceCurrency(rs.getString("FOB_CURR"));

          invoiceVO.setOutAmtIC(String.valueOf(toOutAmt));

          invoiecList.add(invoiceVO);

        }

      }

      catch (Exception e)

      {

        logger.info("----------getInvoiceDetails---Exception------" + e);

        e.printStackTrace();

      }

      finally

      {

        closeStatementResultSet(pst, rs);

      }

      logger.info("Exiting Method");

      return invoiecList;

    }
    public BoeVO revertBOEDetails(BoeVO boevo)

    	    throws DAOException

    	  {

    	    logger.info("Entering Method");

    	    Connection con = null;

    	    LoggableStatement pst = null;

    	    ResultSet rs = null;

    	    ArrayList<InvoiceDetailsVO> invoiecList = null;

    	    try

    	    {

    	      con = DBConnectionUtility.getConnection();

    	      invoiecList = getInvoiceDetails(con, boevo);

    	      if (invoiecList.size() > 0) {

    	        boevo.setInvoiceList(invoiecList);

    	      }

    	    }

    	    catch (Exception e)

    	    {

    	      throwDAOException(e);

    	    }

    	    finally

    	    {

    	      closeSqlRefferance(rs, pst, con);

    	    }

    	    logger.info("Exiting Method");

    	    return boevo;

    	  }

    	  public TransactionVO fetchInvoiceData(BoeVO boeVO)

    	    throws DAOException

    	  {

    	    logger.info("Inside of invoiceData() ====================>>> 333");

    	    logger.info("Entering Method");

    	    Connection con = null;

    	    LoggableStatement pst = null;

    	    ResultSet rs = null;

    	    CommonMethods commonMethods = null;

    	    TransactionVO invoiceVO = null;

    	    try

    	    {

    	      con = DBConnectionUtility.getConnection();

    	      commonMethods = new CommonMethods();

    	      invoiceVO = new TransactionVO();

    	      String invSerialNo = null;

    	      String invNo = null;

    	      if (boeVO.getInvValue() != null)

    	      {

    	        String[] tempVal = boeVO.getInvValue().split(":");

    	        invSerialNo = tempVal[0];

    	        invNo = tempVal[1];

    	      }

    	      String query = "SELECT INV.INVOICE_SERIAL_NUMBER AS INV_SERIAL_NO,INV.INVOICE_NUMBER AS INV_NO, INV.INVOICE_TERMS_OF_INVOICE AS INV_TERMS,INV.INVOICE_FOBAMOUNT AS FOB_AMT, INV.INVOICE_FOBCURRENCY AS FOB_CURR,INV.INVOICE_FRIEGHTAMOUNT AS FRI_AMT, INV.INVOICE_FRIEGHTCURRENCYCODE AS FRI_CURR,INV.INVOICE_INSURANCEAMOUNT AS INS_AMT, INV.INVOICE_INSURANCECURRENCY_CODE AS INS_CURR,INVOICE_SUPPLIER_NAME AS SUPP_NAME, INVOICE_SUPPLIER_ADDRESS AS SUPP_ADDR,INVOICE_SUPPLIER_COUNTRY AS SUPP_CTY, INVOICE_SELLER_NAME AS SELL_NAME,INVOICE_SELLER_ADDRESS AS SELL_ADDR, INVOICE_SELLER_COUNTRY AS SELL_CTY,INVOICE_AGENCY_COMMISSION AS AGEN_AMT, INVOICE_AGENCY_CURRENCY AS AGEN_CURR,INVOICE_DISCOUNT_CURRENCY AS DIS_CURR, INVOICE_DISCOUNT_CHARGES AS DIS_AMT,INVOICE_MISCELLANEOUS_CHARGES AS  MISC_AMT, INVOICE_MISCELLANEOUS_CURRENCY AS MISC_CURR,INVOICE_THIRDPARTY_NAME AS THIRD_NAME, INVOICE_THIRDPARTY_ADDRESS AS THIRD_ADDR,INVOICE_THIRDPARTY_COUNTRY AS THIRD_CTY FROM ETT_INVOICE_DETAILS INV WHERE INV.BOE_NUMBER = ?  AND TO_CHAR(INV.BOE_DATE,'DD/MM/YYYY') = ? AND INV.BOE_PORT_OF_DISCHARGE = ? AND INVOICE_SERIAL_NUMBER = ? AND INVOICE_NUMBER = ? ";

    	 
    	 
    	 
    	 
    	 
    	 
    	 
    	 
    	      pst = new LoggableStatement(con, query);

    	      pst.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNumber()).trim());

    	      pst.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());

    	      pst.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());

    	      pst.setString(4, commonMethods.getEmptyIfNull(invSerialNo).trim());

    	      pst.setString(5, commonMethods.getEmptyIfNull(invNo).trim());

    	      logger.info("Invoice Query --> " + 

    	        pst.getQueryString());

    	      rs = pst.executeQuery();

    	      if (rs.next())

    	      {

    	        invoiceVO.setInvoiceSerialNumber(commonMethods.getEmptyIfNull(rs.getString("INV_SERIAL_NO")).trim());

    	        invoiceVO.setInvoiceNumber(commonMethods.getEmptyIfNull(rs.getString("INV_NO")).trim());

    	        invoiceVO.setTermsofInvoice(commonMethods.getEmptyIfNull(rs.getString("INV_TERMS")).trim());

    	        invoiceVO.setInvAmt(commonMethods.getEmptyIfNull(rs.getString("FOB_AMT")).trim());

    	        invoiceVO.setInvoiceCurr(commonMethods.getEmptyIfNull(rs.getString("FOB_CURR")).trim());

    	        invoiceVO.setFreightAmount(commonMethods.getEmptyIfNull(rs.getString("FRI_AMT")).trim());

    	        invoiceVO.setFreightCurrencyCode(commonMethods.getEmptyIfNull(rs.getString("FRI_CURR")).trim());

    	        invoiceVO.setInsuranceAmount(commonMethods.getEmptyIfNull(rs.getString("INS_AMT")).trim());

    	        invoiceVO.setInsuranceCurrencyCode(commonMethods.getEmptyIfNull(rs.getString("INS_CURR")).trim());

    	        invoiceVO.setAgencyCommission(commonMethods.getEmptyIfNull(rs.getString("AGEN_AMT")).trim());

    	        invoiceVO.setAgencyCurrency(commonMethods.getEmptyIfNull(rs.getString("AGEN_CURR")).trim());

    	        invoiceVO.setDiscountCharges(commonMethods.getEmptyIfNull(rs.getString("DIS_AMT")).trim());

    	        invoiceVO.setDiscountCurrency(commonMethods.getEmptyIfNull(rs.getString("DIS_CURR")).trim());

    	        invoiceVO.setMiscellaneousCharges(commonMethods.getEmptyIfNull(rs.getString("MISC_AMT")).trim());

    	        invoiceVO.setMiscellaneousCurrency(commonMethods.getEmptyIfNull(rs.getString("MISC_CURR")).trim());

    	        invoiceVO.setSupplierName(commonMethods.getEmptyIfNull(rs.getString("SUPP_NAME")).trim());

    	        invoiceVO.setSupplierAddress(commonMethods.getEmptyIfNull(rs.getString("SUPP_ADDR")).trim());

    	        invoiceVO.setSupplierCountry(commonMethods.getEmptyIfNull(rs.getString("SUPP_CTY")).trim());

    	        invoiceVO.setSellerName(commonMethods.getEmptyIfNull(rs.getString("SELL_NAME")).trim());

    	        invoiceVO.setSellerAddress(commonMethods.getEmptyIfNull(rs.getString("SELL_ADDR")).trim());

    	        invoiceVO.setSellerCountry(commonMethods.getEmptyIfNull(rs.getString("SELL_CTY")).trim());

    	        invoiceVO.setThirdPartyName(commonMethods.getEmptyIfNull(rs.getString("THIRD_NAME")).trim());

    	        invoiceVO.setThirdPartyAddress(commonMethods.getEmptyIfNull(rs.getString("THIRD_ADDR")).trim());

    	        invoiceVO.setThirdPartyCountry(commonMethods.getEmptyIfNull(rs.getString("THIRD_CTY")).trim());

    	      }

    	    }

    	    catch (Exception e)

    	    {

    	      logger.info("Exception in is " + e.getMessage());

    	    }

    	    finally

    	    {

    	      closeSqlRefferance(rs, pst, con);

    	    }

    	    logger.info("Exiting Method");

    	    return invoiceVO;

    	  }

    	  public BoeVO boeExtSubmit(BoeVO boevo)

    	    throws DAOException

    	  {

    	    logger.info("-------------boeExtSubmit-----------");

    	    Connection con = null;

    	    LoggableStatement pst = null;

    	    ResultSet rs = null;

    	    String makerId = null;

    	    int insertResult = 0;

    	    CommonMethods commonMethods = null;

    	    try

    	    {

    	      HttpSession session = ServletActionContext.getRequest().getSession();

    	      makerId = (String)session.getAttribute("loginedUserName");

    	      logger.info("boeExtSubmit makerId---------------------->" + makerId);
    	      con = DBConnectionUtility.getConnection();

    	      commonMethods = new CommonMethods();

    	      int boeExtCount = 0;

    	      String query = "SELECT COUNT(*) AS EXT_COUNT FROM ETT_BOE_EXT_DATA WHERE BOENUMBER = ?  AND TO_CHAR(BOEDATE,'DD/MM/YYYY') = ? AND PORTOFDISCHARGE = ? AND STATUS != 'A' ";

    	 
    	      LoggableStatement pst1 = new LoggableStatement(con, query);

    	      pst1.setString(1, boevo.getBoeNumber());

    	      pst1.setString(2, boevo.getBoeDate());

    	      pst1.setString(3, boevo.getPortCode());

    	      ResultSet rs1 = pst1.executeQuery();

    	      logger.info("boeExtSubmit----------------Query------pst" + pst1.getQueryString());

    	      if (rs1.next())

    	      {

    	        boeExtCount = rs1.getInt("EXT_COUNT");

    	        logger.info("boeExtSubmit----boeExtCount count" + boeExtCount);

    	      }

    	      closeStatementResultSet(pst1, rs1);

    	      if (boeExtCount > 0)

    	      {

    	        String boeExtSeqNo = null;

    	        String boeExXkeyQuery = "SELECT EXTENSION_SNO AS EXTENSION_SNO FROM ETT_BOE_EXT_DATA WHERE BOENUMBER = ?  AND TO_CHAR(BOEDATE,'DD/MM/YYYY') = ? AND PORTOFDISCHARGE = ? AND STATUS != 'A' ";

    	        LoggableStatement ps1 = new LoggableStatement(con, boeExXkeyQuery);

    	 
    	        ps1.setString(1, boevo.getBoeNumber());

    	        ps1.setString(2, boevo.getBoeDate());

    	        ps1.setString(3, boevo.getPortCode());

    	        ResultSet rst1 = ps1.executeQuery();

    	        logger.info("boeExtSubmit----------------Query------pst" + ps1.getQueryString());

    	        if (rst1.next())

    	        {

    	          boeExtSeqNo = rst1.getString("EXTENSION_SNO");

    	          logger.info("boeExtSubmit-----------boeExtSeqNo-----Query------pst" + boeExtSeqNo);

    	        }

    	        logger.info("boeExtSeqNo------------------->" + boeExtSeqNo);

    	        closeStatementResultSet(ps1, rst1);

    	        pst = new LoggableStatement(con, "UPDATE ETT_BOE_EXT_DATA SET ADCODE = ?,IECODE = ?,RECORDINDICATOR = ?,EXTENSIONBY = ?,LETTERNO = ?,LETTERDATE = TO_DATE(?,'DD-MM-YYYY'),EXTENSIONDATE = TO_DATE(?,'DD-MM-YYYY'),PREVEXTDATE = TO_DATE(?,'DD-MM-YYYY'),REMARKS = ?,STATUS = 'P',MAKER_USERID = ?,MAKER_CREATEDON = SYSTIMESTAMP,EXT_INDICATOR = ? WHERE BOENUMBER = ? AND TO_CHAR(BOEDATE,'DD/MM/YYYY') = ? AND PORTOFDISCHARGE = ? AND EXTENSION_SNO = ? ");

    	        pst.setString(1, commonMethods.getEmptyIfNull(boevo.getAdCode()).trim());

    	        pst.setString(2, commonMethods.getEmptyIfNull(boevo.getIeCode()).trim());

    	        pst.setString(3, commonMethods.getEmptyIfNull(boevo.getRecordIndicator()).trim());

    	        pst.setString(4, commonMethods.getEmptyIfNull(boevo.getApprovalBy()).trim());

    	        pst.setString(5, commonMethods.getEmptyIfNull(boevo.getExtLetterNo()).trim());

    	        pst.setString(6, commonMethods.getEmptyIfNull(boevo.getExtLetterDate()).trim());

    	        pst.setString(7, commonMethods.getEmptyIfNull(boevo.getBoeExtDate()).trim());

    	        pst.setString(8, commonMethods.getEmptyIfNull(boevo.getExtDate()).trim());

    	        pst.setString(9, commonMethods.getEmptyIfNull(boevo.getExtRemarks()).trim());

    	        pst.setString(10, commonMethods.getEmptyIfNull(makerId).trim());

    	 
    	 
    	        pst.setString(11, commonMethods.getEmptyIfNull(boevo.getBoeExtInd()).trim());

    	        pst.setString(12, commonMethods.getEmptyIfNull(boevo.getBoeNumber()).trim());

    	        pst.setString(13, commonMethods.getEmptyIfNull(boevo.getBoeDate()).trim());

    	        pst.setString(14, commonMethods.getEmptyIfNull(boevo.getPortCode()).trim());

    	        pst.setString(15, commonMethods.getEmptyIfNull(boeExtSeqNo).trim());

    	 
    	        logger.info("boeExtSubmit-----BOE_EXT_UPDATE----------" + pst.getQueryString());

    	        insertResult = pst.executeUpdate();

    	        logger.info("executeUpdate-------->" + insertResult);

    	      }

    	      else

    	      {

    	        pst = new LoggableStatement(con, "INSERT INTO ETT_BOE_EXT_DATA(EXTENSION_SNO,BOENUMBER,BOEDATE,PORTOFDISCHARGE,ADCODE,IECODE,RECORDINDICATOR,EXTENSIONBY,LETTERNO,LETTERDATE,EXTENSIONDATE,PREVEXTDATE,REMARKS,STATUS,MAKER_USERID, EXT_INDICATOR) VALUES(BOE_EXT_SEQ_NO,?,TO_DATE(?,'DD-MM-YYYY'),?,?,?,?,?,?,TO_DATE(?,'DD-MM-YYYY'),TO_DATE(?,'DD-MM-YYYY'),TO_DATE(?,'DD-MM-YYYY'),?,'P',?, ?)");

    	 
    	        pst.setString(1, commonMethods.getEmptyIfNull(boevo.getBoeNumber()).trim());

    	        pst.setString(2, commonMethods.getEmptyIfNull(boevo.getBoeDate()).trim());

    	        pst.setString(3, commonMethods.getEmptyIfNull(boevo.getPortCode()).trim());

    	        pst.setString(4, commonMethods.getEmptyIfNull(boevo.getAdCode()).trim());

    	        pst.setString(5, commonMethods.getEmptyIfNull(boevo.getIeCode()).trim());

    	        pst.setString(6, commonMethods.getEmptyIfNull(boevo.getRecordIndicator()).trim());

    	        pst.setString(7, commonMethods.getEmptyIfNull(boevo.getApprovalBy()).trim());

    	        pst.setString(8, commonMethods.getEmptyIfNull(boevo.getExtLetterNo()).trim());

    	        pst.setString(9, commonMethods.getEmptyIfNull(boevo.getExtLetterDate()).trim());

    	        pst.setString(10, commonMethods.getEmptyIfNull(boevo.getBoeExtDate()).trim());

    	        pst.setString(11, commonMethods.getEmptyIfNull(boevo.getExtDate()).trim());

    	        pst.setString(12, commonMethods.getEmptyIfNull(boevo.getExtRemarks()).trim());

    	        pst.setString(13, commonMethods.getEmptyIfNull(makerId).trim());

    	        pst.setString(14, commonMethods.getEmptyIfNull(boevo.getBoeExtInd()).trim());

    	        insertResult = pst.executeUpdate();

    	        logger.info("boeExtSubmit-----BOE_EXT_INSERT-- query--------" + pst.getQueryString());

    	        logger.info("insertResult-------->" + insertResult);

    	      }

    	      if (insertResult > 0) {

    	        boevo.setErrorCode("SUCCESS");

    	      }

    	      pst.close();

    	    }

    	    catch (Exception e)

    	    {

    	      logger.info("BOE EXTENSTION MAKER EXCEPTION---------------boeExtSubmit" + e);

    	    }

    	    finally

    	    {

    	      DBConnectionUtility.surrenderDB(con, pst, rs);

    	    }

    	    logger.info("Exiting Method");

    	    return boevo;

    	  }
    	  public BoeVO boeClosureSubmit(BoeVO boevo, String[] chkInvlist)

    			    throws DAOException

    			  {

    			    logger.info("---------boeClosureSubmit--------------");

    			    Connection con = null;

    			    LoggableStatement pst = null;

    			    ResultSet rs = null;

    			    String makerId = null;

    			    int insertResult = 0;

    			    CommonMethods commonMethods = null;

    			    label1485:

    			    try

    			    {

    			      HttpSession session = ServletActionContext.getRequest().getSession();

    			      makerId = (String)session.getAttribute("loginedUserName");

    			      logger.info("boeClosureSubmit makerId---------------------->" + makerId);

    			 
    			 
    			      con = DBConnectionUtility.getConnection();

    			      commonMethods = new CommonMethods();

    			      int boeClCount = 0;

    			 
    			 
    			      String sExtby = "2";

    			      String sPorcOutput = "";

    			      CallableStatement cs = null;

    			      String sProcName = "{call ETT_BOE_EXP_VALIDATION(?, ?, ?, ?)}";

    			      cs = con.prepareCall(sProcName);

    			      cs.setString(1, commonMethods.getEmptyIfNull(boevo.getBoeNumber()).trim());

    			      cs.setString(2, commonMethods.getEmptyIfNull(boevo.getBoeDate()).trim());

    			      cs.setString(3, commonMethods.getEmptyIfNull(boevo.getPortCode()).trim());

    			      cs.setString(4, sExtby);

    			      if (cs.executeUpdate() <= 0) {

    			        logger.info("Procedure having some problem");

    			      }

    			      if (cs != null) {

    			        cs.close();

    			      }

    			      String query = "SELECT COUNT(*) AS CL_COUNT FROM ETT_BOE_CLOSURE_DATA WHERE BOENUMBER = ?  AND TO_CHAR(BOEDATE,'DD/MM/YYYY') = ? AND PORTOFDISCHARGE = ? AND STATUS != 'A' ";

    			 
    			      LoggableStatement pst1 = new LoggableStatement(con, query);

    			      pst1.setString(1, boevo.getBoeNumber());

    			      pst1.setString(2, boevo.getBoeDate());

    			      pst1.setString(3, boevo.getPortCode());

    			      logger.info("Existing Query--------count--->" + pst1.getQueryString());

    			      ResultSet rs1 = pst1.executeQuery();

    			      if (rs1.next()) {

    			        boeClCount = rs1.getInt("CL_COUNT");

    			      }

    			      logger.info("boeClCount------Existing Data ----------" + boeClCount);

    			      closeStatementResultSet(pst1, rs1);

    			      if (boeClCount > 0)

    			      {

    			        String boeClSeqNo = null;

    			        String boeClXkeyQuery = "SELECT CLOSURE_SNO AS CLOSURE_SNO FROM ETT_BOE_CLOSURE_DATA WHERE BOENUMBER = ?  AND TO_CHAR(BOEDATE,'DD/MM/YYYY') = ? AND PORTOFDISCHARGE = ? AND STATUS != 'A' ";

    			        LoggableStatement ps1 = new LoggableStatement(con, boeClXkeyQuery);

    			        ps1.setString(1, boevo.getBoeNumber());

    			        ps1.setString(2, boevo.getBoeDate());

    			        ps1.setString(3, boevo.getPortCode());

    			        logger.info("boeClXkeyQuery-------------->" + ps1.getQueryString());

    			        ResultSet rst1 = ps1.executeQuery();

    			        if (rst1.next()) {

    			          boeClSeqNo = rst1.getString("CLOSURE_SNO");

    			        }

    			        closeStatementResultSet(ps1, rst1);

    			        logger.info("Sequence NUmber CLOSURE_SNO--- " + boeClSeqNo);

    			        pst = new LoggableStatement(con, "UPDATE ETT_BOE_CLOSURE_DATA SET IECODE = ?,ADCODE = ?,RECORDINDICATOR = ?,ADJ_DATE = TO_DATE(?,'DD-MM-YYYY'),ADJ_IND = ?,DOCUMENT_NO = ?,DOCUMENT_DATE = TO_DATE(?,'DD-MM-YYYY'),DOCUMENT_PORT = ?,APPROVEDBY = ?,LETTERNO = ?,LETTERDATE = TO_DATE(?,'DD-MM-YYYY'),REMARKS = ?,STATUS = 'P',BOE_CL_TYPE = ?,MAKER_USERID = ?,MAKER_CREATEDON = SYSTIMESTAMP, BOE_CLOS_IND = ?  WHERE BOENUMBER = ? AND TO_CHAR(BOEDATE,'DD/MM/YYYY') = ? AND PORTOFDISCHARGE = ? AND CLOSURE_SNO = ? ");

    			        pst.setString(1, commonMethods.getEmptyIfNull(boevo.getIeCode()).trim());

    			        pst.setString(2, commonMethods.getEmptyIfNull(boevo.getAdCode()).trim());

    			        pst.setString(3, commonMethods.getEmptyIfNull(boevo.getRecordIndicator()).trim());

    			        pst.setString(4, commonMethods.getEmptyIfNull(boevo.getAdjClosureDate()).trim());

    			        pst.setString(5, commonMethods.getEmptyIfNull(boevo.getAdjClosureIndicator()).trim());

    			        pst.setString(6, commonMethods.getEmptyIfNull(boevo.getDocNo()).trim());

    			        pst.setString(7, commonMethods.getEmptyIfNull(boevo.getDocDate()).trim());

    			        pst.setString(8, commonMethods.getEmptyIfNull(boevo.getDocPort()).trim());

    			        logger.info("getDocPort-------------------" + boevo.getDocPort());

    			        pst.setString(9, commonMethods.getEmptyIfNull(boevo.getApprovedBy()).trim());

    			        pst.setString(10, commonMethods.getEmptyIfNull(boevo.getClosureLetterNo()).trim());

    			        pst.setString(11, commonMethods.getEmptyIfNull(boevo.getClosureLetterDate()).trim());

    			        pst.setString(12, commonMethods.getEmptyIfNull(boevo.getClosureRemarks()).trim());

    			        pst.setString(13, commonMethods.getEmptyIfNull(boevo.getClTransType()).trim());

    			        pst.setString(14, makerId);

    			        pst.setString(15, commonMethods.getEmptyIfNull(boevo.getBoeCloInd()).trim());

    			        pst.setString(16, commonMethods.getEmptyIfNull(boevo.getBoeNumber()).trim());

    			        pst.setString(17, commonMethods.getEmptyIfNull(boevo.getBoeDate()).trim());

    			        pst.setString(18, commonMethods.getEmptyIfNull(boevo.getPortCode()).trim());

    			        pst.setString(19, commonMethods.getEmptyIfNull(boeClSeqNo).trim());

    			 
    			        logger.info("BOE_CLOSURE_UPDATE---- Query---------->" + pst.getQueryString());

    			        insertResult = pst.executeUpdate();

    			        logger.info("insertResult-------count----" + insertResult);

    			        int invCount = 0;

    			        if (insertResult > 0) {

    			          invCount = storeInvoiceData(con, chkInvlist, boeClSeqNo, boevo);

    			        }

    			        if (invCount > 0)

    			        {

    			          boevo.setErrorCode("SUCCESS");

    			          break label1485;

    			        }

    			      }

    			      else

    			      {

    			        String uniqueKey = getUniqueReferenceforORM(con);

    			 
    			        pst = new LoggableStatement(con, "INSERT INTO ETT_BOE_CLOSURE_DATA(CLOSURE_SNO,BOENUMBER,BOEDATE,PORTOFDISCHARGE,IECODE,ADCODE,RECORDINDICATOR,ADJ_DATE,ADJ_IND,DOCUMENT_NO,DOCUMENT_DATE,DOCUMENT_PORT,APPROVEDBY,LETTERNO,LETTERDATE,REMARKS,STATUS,BOE_CL_TYPE,MAKER_USERID, BOE_CLOS_IND)VALUES(?,?,TO_DATE(?,'DD-MM-YYYY'),?,?,?,?,TO_DATE(?,'DD-MM-YYYY'),?,?,TO_DATE(?,'DD-MM-YYYY'),?,?,?,TO_DATE(?,'DD-MM-YYYY'),?,'P',?,?, ?)");

    			        pst.setString(1, commonMethods.getEmptyIfNull(uniqueKey).trim());

    			        pst.setString(2, commonMethods.getEmptyIfNull(boevo.getBoeNumber()).trim());

    			        pst.setString(3, commonMethods.getEmptyIfNull(boevo.getBoeDate()).trim());

    			        pst.setString(4, commonMethods.getEmptyIfNull(boevo.getPortCode()).trim());

    			        pst.setString(5, commonMethods.getEmptyIfNull(boevo.getIeCode()).trim());

    			        pst.setString(6, commonMethods.getEmptyIfNull(boevo.getAdCode()).trim());

    			        pst.setString(7, commonMethods.getEmptyIfNull(boevo.getRecordIndicator()).trim());

    			        pst.setString(8, commonMethods.getEmptyIfNull(boevo.getAdjClosureDate()).trim());

    			        pst.setString(9, commonMethods.getEmptyIfNull(boevo.getAdjClosureIndicator()).trim());

    			        pst.setString(10, commonMethods.getEmptyIfNull(boevo.getDocNo()).trim());

    			        pst.setString(11, commonMethods.getEmptyIfNull(boevo.getDocDate()).trim());

    			        pst.setString(12, commonMethods.getEmptyIfNull(boevo.getDocPort()).trim());

    			        pst.setString(13, commonMethods.getEmptyIfNull(boevo.getApprovedBy()).trim());

    			        pst.setString(14, commonMethods.getEmptyIfNull(boevo.getClosureLetterNo()).trim());

    			        pst.setString(15, commonMethods.getEmptyIfNull(boevo.getClosureLetterDate()).trim());

    			        pst.setString(16, commonMethods.getEmptyIfNull(boevo.getClosureRemarks()).trim());

    			        pst.setString(17, commonMethods.getEmptyIfNull(boevo.getClTransType()).trim());

    			        pst.setString(18, makerId);

    			        pst.setString(19, commonMethods.getEmptyIfNull(boevo.getBoeCloInd()).trim());
    			        insertResult = pst.executeUpdate();

    			        logger.info("BOE_CLOSURE_INSERT-------------Query----------" + pst.getQueryString());

    			 
    			        logger.info("BOE_CLOSURE_INSERT-------------Query---insertResult-- count-----" + insertResult);

    			        int invCount = 0;

    			        if (insertResult > 0) {

    			          invCount = storeInvoiceData(con, chkInvlist, uniqueKey, boevo);

    			        }

    			        if (invCount > 0) {

    			          boevo.setErrorCode("SUCCESS");

    			        }

    			      }

    			    }

    			    catch (Exception e)

    			    {

    			      logger.info("boeClosureSubmit Exception-------------" + e);

    			      throwDAOException(e);

    			    }

    			    finally

    			    {

    			      DBConnectionUtility.surrenderDB(con, pst, rs);

    			    }

    			    logger.info("Exiting Method");

    			    return boevo;

    			  }

    			  public String getUniqueReferenceforORM(Connection con)

    			  {

    			    String xKey = null;

    			    ResultSet result = null;

    			    LoggableStatement statement = null;

    			    try

    			    {

    			      String query = "SELECT BOE_CLO_SEQ_NO FROM DUAL";

    			      statement = new LoggableStatement(con, query);

    			      result = statement.executeQuery();

    			      if (result.next()) {

    			        xKey = result.getString(1);

    			      }

    			    }

    			    catch (Exception e)

    			    {

    			      e.printStackTrace();

    			    }

    			    return xKey;

    			  }

    			  public int storeInvoiceData(Connection con, String[] chkList, String uniqueNo, BoeVO boeVO)

    			    throws DAOException

    			  {

    			    logger.info("storeInvoiceData-----------");

    			    int count = 0;

    			    CommonMethods commonMethods = null;

    			    try

    			    {

    			      if (con == null) {

    			        con = DBConnectionUtility.getConnection();

    			      }

    			      commonMethods = new CommonMethods();

    			      logger.info("chkList-------------value" + chkList);

    			      if ((chkList != null) && (chkList.length > 0)) {

    			        for (int i = 0; i < chkList.length; i++)

    			        {

    			          String tempRefNo = chkList[i];

    			          if (!tempRefNo.equalsIgnoreCase("false"))

    			          {

    			            String[] s = chkList[i].split(":");

    			            String invSerialNo = commonMethods.getEmptyIfNull(s[0]).trim();

    			            String invNo = commonMethods.getEmptyIfNull(s[1]).trim();

    			            String adjAmt = commonMethods.getEmptyIfNull(s[2]).trim();

    			            logger.info("invSerialNo-------" + invSerialNo);

    			            logger.info("invNo-------" + invNo);

    			            logger.info("adjAmt-------" + adjAmt);

    			 
    			 
    			 
    			 
    			            String query = "SELECT INVOICE_SERIAL_NUMBER AS INV_SNO,INVOICE_NUMBER AS INVNO,INVOICE_TERMS_OF_INVOICE AS TERM_INV, NVL(INVOICE_FOBAMOUNT,0) AS TOT_INV_AMT, INVOICE_FOBCURRENCY AS INV_CURR FROM ETT_INVOICE_DETAILS WHERE  BOE_NUMBER = ? AND TO_CHAR(BOE_DATE,'DD/MM/YYYY') = ? AND BOE_PORT_OF_DISCHARGE = ? AND INVOICE_SERIAL_NUMBER = ? AND INVOICE_NUMBER = ? ";

    			 
    			 
    			            LoggableStatement pst1 = new LoggableStatement(con, query);

    			            logger.info("pst1---------------Query" + pst1.getQueryString());

    			            pst1.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNumber()).trim());

    			            pst1.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());

    			            pst1.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());

    			            pst1.setString(4, invSerialNo);

    			            pst1.setString(5, invNo);

    			            ResultSet rs1 = pst1.executeQuery();

    			            if (rs1.next())

    			            {

    			              String totalInvAmt = commonMethods.getEmptyIfNull(rs1.getString("TOT_INV_AMT")).trim();

    			              String invCurr = commonMethods.getEmptyIfNull(rs1.getString("INV_CURR")).trim();

    			 
    			              String insertQuery = "INSERT INTO ETT_BOE_INV_CLOSURE(RINV_SEQNO,CLOSURE_SNO,INV_SNO,INV_NO, INV_AMT,INVOICECURR,ADJ_INVAMT,STATUS,BOE_CL_TYPE,BOE_NO,BOE_DATE,PORTCODE) VALUES(ETT_BEC_SEQ_NO,?,?,?,?,?,?,?,?,?,TO_DATE(?,'DD-MM-YYYY'),?) ";

    			 
    			 
    			              LoggableStatement pst2 = new LoggableStatement(con, insertQuery);

    			              logger.info("Insert Query--------------->" + pst2.getQueryString());

    			              pst2.setString(1, uniqueNo);

    			              pst2.setString(2, invSerialNo);

    			              pst2.setString(3, invNo);

    			              pst2.setString(4, totalInvAmt);

    			              pst2.setString(5, invCurr);

    			              pst2.setString(6, adjAmt);

    			              pst2.setString(7, "P");

    			              pst2.setString(8, commonMethods.getEmptyIfNull(boeVO.getClTransType()).trim());

    			              pst2.setString(9, commonMethods.getEmptyIfNull(boeVO.getBoeNumber()).trim());

    			              pst2.setString(10, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());

    			              pst2.setString(11, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());

    			              count += pst2.executeUpdate();

    			 
    			              logger.info("Insert Query------count--------->" + count);

    			              closePreparedStatement(pst2);

    			            }

    			            closeResultSet(rs1);

    			            closePreparedStatement(pst1);

    			          }

    			        }

    			      }

    			    }

    			    catch (Exception e)

    			    {

    			      logger.info("Maker storeInvoiceData----------Excepiton " + e);

    			    }

    			    logger.info("Exiting Method");

    			    return count;

    			  }
    			 
    			  public int isCrossCurrencyCase(BoeVO boeVO)

    			  {

    			    logger.info("Entering Method");

    			    Connection con = null;

    			    LoggableStatement pst = null;

    			    ResultSet rs = null;

    			    int count = 0;

    			    CommonMethods commonMethods = null;

    			    try

    			    {

    			      con = DBConnectionUtility.getConnection();

    			      commonMethods = new CommonMethods();

    			      pst = new LoggableStatement(con, "SELECT COUNT(*) AS CROSS_COUNT FROM ETT_INVOICE_DETAILS  WHERE BOE_NUMBER = ? AND TO_CHAR(BOE_DATE,'DD/MM/YYYY') = ? AND BOE_PORT_OF_DISCHARGE  = ? AND (TRIM(INVOICE_FOBCURRENCY) != NVL(INVOICE_FRIEGHTCURRENCYCODE,TRIM(INVOICE_FOBCURRENCY)) OR TRIM(INVOICE_FOBCURRENCY)   != NVL(INVOICE_INSURANCECURRENCY_CODE,TRIM(INVOICE_FOBCURRENCY)))");

    			      pst.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNumber()).trim());

    			      pst.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());

    			      pst.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());

    			      rs = pst.executeQuery();

    			      if (rs.next()) {

    			        count = rs.getInt("CROSS_COUNT");

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

    			    logger.info("Exiting Method");

    			    return count;

    			  }

    			  public TransactionVO getInvoiceDetails(BoeVO boevo)

    			    throws DAOException

    			  {

    			    logger.info("Exiting Method");

    			    Connection con = null;

    			    LoggableStatement pst = null;

    			    ResultSet rs = null;

    			    CommonMethods commonMethods = null;

    			    TransactionVO transactionVO = null;

    			    try

    			    {

    			      con = DBConnectionUtility.getConnection();

    			      commonMethods = new CommonMethods();

    			      String invSerialNo = null;

    			      String invNo = null;

    			      if (boevo.getInvValue() != null)

    			      {

    			        String[] tempVal = boevo.getInvValue().split(":");

    			        invSerialNo = tempVal[0];

    			        invNo = tempVal[1];

    			      }

    			      pst = new LoggableStatement(con, " SELECT INVOICE_SERIAL_NUMBER AS INV_SNO,INVOICE_NUMBER AS INV_NO, INVOICE_FOBAMOUNT AS FOB_AMT,INVOICE_FOBCURRENCY AS FOB_CURR, DECODE(TRIM(FR_EX_AMT),NULL,NVL(INVOICE_FRIEGHTAMOUNT,'0'),FR_EX_AMT) AS FR_AMT, DECODE(TRIM(FR_EX_CURR),NULL,NVL(TRIM(INVOICE_FRIEGHTCURRENCYCODE),INVOICE_FOBCURRENCY),FR_EX_CURR) AS FR_CURR, DECODE(TRIM(INS_EX_AMT),NULL,NVL(INVOICE_INSURANCEAMOUNT,'0'),INS_EX_AMT) AS INS_AMT, DECODE(TRIM(INS_EX_CURR),NULL,NVL(TRIM(INVOICE_INSURANCECURRENCY_CODE),INVOICE_FOBCURRENCY),INS_EX_CURR) AS INS_CURR, DECODE(TRIM(FR_EX_RATE),NULL,(SELECT ETT_BOE_EX_RATE_CAL(NVL(INVOICE_FRIEGHTCURRENCYCODE,INVOICE_FOBCURRENCY),INVOICE_FOBCURRENCY) FROM DUAL),FR_EX_RATE) AS FR_EX_RATE, DECODE(TRIM(INS_EX_RATE),NULL,(SELECT ETT_BOE_EX_RATE_CAL(NVL(INVOICE_INSURANCECURRENCY_CODE,INVOICE_FOBCURRENCY),INVOICE_FOBCURRENCY) FROM DUAL),INS_EX_RATE) AS INS_EX_RATE  FROM ETT_INVOICE_DETAILS WHERE BOE_NUMBER = ? AND TO_CHAR(BOE_DATE,'DD/MM/YYYY') = ? AND BOE_PORT_OF_DISCHARGE = ? AND INVOICE_SERIAL_NUMBER = ? AND TRIM(INVOICE_NUMBER) = ? ");

    			      pst.setString(1, commonMethods.getEmptyIfNull(boevo.getBoeNumber()).trim());

    			      pst.setString(2, commonMethods.getEmptyIfNull(boevo.getBoeDate()).trim());

    			      pst.setString(3, commonMethods.getEmptyIfNull(boevo.getPortCode()).trim());

    			      pst.setString(4, commonMethods.getEmptyIfNull(invSerialNo).trim());

    			      pst.setString(5, commonMethods.getEmptyIfNull(invNo).trim());

    			      rs = pst.executeQuery();

    			      if (rs.next())

    			      {

    			        transactionVO = new TransactionVO();

    			        transactionVO.setInvSno(commonMethods.getEmptyIfNull(invSerialNo).trim());

    			        transactionVO.setInvNo(commonMethods.getEmptyIfNull(invNo).trim());

    			        transactionVO.setCrossFrCurr(commonMethods.getEmptyIfNull(rs.getString("FR_CURR")).trim());

    			        transactionVO.setCrossFobAmt(commonMethods.getEmptyIfNull(rs.getString("FOB_AMT")).trim());

    			        transactionVO.setCrossFobCurr(commonMethods.getEmptyIfNull(rs.getString("FOB_CURR")).trim());

    			        transactionVO.setCrossFrAmt(commonMethods.getEmptyIfNull(rs.getString("FR_AMT")).trim());

    			        transactionVO.setCrossFrCurr(commonMethods.getEmptyIfNull(rs.getString("FR_CURR")).trim());

    			        transactionVO.setCrossInsAmt(commonMethods.getEmptyIfNull(rs.getString("INS_AMT")).trim());

    			        transactionVO.setCrossInsCurr(commonMethods.getEmptyIfNull(rs.getString("INS_CURR")).trim());

    			        transactionVO.setCrossExRate1(commonMethods.getEmptyIfNull(rs.getString("INS_EX_RATE")).trim());

    			        transactionVO.setCrossExRate2(commonMethods.getEmptyIfNull(rs.getString("FR_EX_RATE")).trim());

    			 
    			        double exRate1 = commonMethods.convertToDouble(rs.getString("FR_EX_RATE"));

    			        double exRate2 = commonMethods.convertToDouble(rs.getString("INS_EX_RATE"));

    			        double fobAmt = commonMethods.convertToDouble(rs.getString("FOB_AMT"));

    			        double friAmt = commonMethods.convertToDouble(rs.getString("FR_AMT"));

    			        double insAmt = commonMethods.convertToDouble(rs.getString("INS_AMT"));

    			        double eqFriAmt = friAmt * exRate1;

    			        eqFriAmt = Math.round(eqFriAmt * 100.0D) / 100.0D;

    			        BigDecimal tEqFriAmt = BigDecimal.valueOf(eqFriAmt).setScale(2, RoundingMode.HALF_UP);

    			        transactionVO.setEquivalentFreAmt(String.valueOf(tEqFriAmt));

    			        double eqInsAmt = insAmt * exRate2;

    			        eqInsAmt = Math.round(eqInsAmt * 100.0D) / 100.0D;

    			        BigDecimal tEqInsAmt = BigDecimal.valueOf(eqInsAmt).setScale(2, RoundingMode.HALF_UP);

    			        transactionVO.setEquivalentInsAmt(String.valueOf(tEqInsAmt));

    			 
    			 
    			        double totalAmt = fobAmt;

    			        double totalInvAmt = Math.round(totalAmt * 100.0D) / 100.0D;

    			        BigDecimal toInvAmt = BigDecimal.valueOf(totalInvAmt).setScale(2, RoundingMode.HALF_UP);

    			        transactionVO.setTotalInvAmt(String.valueOf(toInvAmt));

    			      }

    			    }

    			    catch (Exception e)

    			    {

    			      e.printStackTrace();

    			      throwDAOException(e);

    			    }

    			    finally

    			    {

    			      DBConnectionUtility.surrenderDB(con, pst, rs);

    			    }

    			    logger.info("Exiting Method");

    			    return transactionVO;

    			  }

    			  public TransactionVO updateCrossCurrencyData(BoeVO boevo, TransactionVO transactionVO)

    			    throws DAOException

    			  {

    			    logger.info("Entering Method");

    			    Connection con = null;

    			    CommonMethods commonMethods = null;

    			    try

    			    {

    			      con = DBConnectionUtility.getConnection();

    			      HttpSession session = ServletActionContext.getRequest().getSession();

    			      String loginedUserId = (String)session.getAttribute("loginedUserId");

    			      commonMethods = new CommonMethods();

    			      LoggableStatement pst2 = new LoggableStatement(con, "UPDATE ETT_INVOICE_DETAILS  SET INVOICE_FRIEGHTAMOUNT=?,INVOICE_FRIEGHTCURRENCYCODE=?, INVOICE_INSURANCEAMOUNT=?,INVOICE_INSURANCECURRENCY_CODE=?,FR_EX_RATE=?,INS_EX_RATE=?, FR_EX_AMT = DECODE(TRIM(FR_EX_AMT),NULL,INVOICE_FRIEGHTAMOUNT,FR_EX_AMT), FR_EX_CURR = DECODE(TRIM(FR_EX_CURR),NULL,INVOICE_FRIEGHTCURRENCYCODE,FR_EX_CURR), INS_EX_AMT = DECODE(TRIM(INS_EX_AMT),NULL,INVOICE_INSURANCEAMOUNT,INS_EX_AMT), INS_EX_CURR = DECODE(TRIM(INS_EX_CURR),NULL,INVOICE_INSURANCECURRENCY_CODE,INS_EX_CURR),UPDATEDBY=?,UPDATEDTIME=SYSTIMESTAMP WHERE BOE_NUMBER = ? AND TO_CHAR(BOE_DATE,'DD/MM/YYYY') = ? AND BOE_PORT_OF_DISCHARGE = ?  AND INVOICE_SERIAL_NUMBER = ? AND INVOICE_NUMBER = ? ");

    			      pst2.setString(1, commonMethods.getEmptyIfNull(transactionVO.getEquivalentFreAmt()).trim());

    			      pst2.setString(2, commonMethods.getEmptyIfNull(transactionVO.getCrossFobCurr()).trim());

    			      pst2.setString(3, commonMethods.getEmptyIfNull(transactionVO.getEquivalentInsAmt()).trim());

    			      pst2.setString(4, commonMethods.getEmptyIfNull(transactionVO.getCrossFobCurr()).trim());

    			      pst2.setString(5, commonMethods.getEmptyIfNull(transactionVO.getCrossExRate2()).trim());

    			      pst2.setString(6, commonMethods.getEmptyIfNull(transactionVO.getCrossExRate1()).trim());

    			      pst2.setString(7, commonMethods.getEmptyIfNull(loginedUserId).trim());

    			      pst2.setString(8, commonMethods.getEmptyIfNull(boevo.getBoeNumber()).trim());

    			      pst2.setString(9, commonMethods.getEmptyIfNull(boevo.getBoeDate()).trim());

    			      pst2.setString(10, commonMethods.getEmptyIfNull(boevo.getPortCode()).trim());

    			      pst2.setString(11, commonMethods.getEmptyIfNull(transactionVO.getInvSno()).trim());

    			      pst2.setString(12, commonMethods.getEmptyIfNull(transactionVO.getInvNo()).trim());
    			      logger.info("Update Statement " + pst2.getQueryString());

    			      int icount = pst2.executeUpdate();

    			      logger.info("Invoice Count -->" + icount);

    			      closePreparedStatement(pst2);

    			    }

    			    catch (Exception e)

    			    {

    			      e.printStackTrace();

    			      throwDAOException(e);

    			    }

    			    finally

    			    {

    			      closeConnection(con);

    			    }

    			    logger.info("Exiting Method");

    			    return transactionVO;

    			  }

    			  public ArrayList<BoeVO> boeExChecker(BOEDataSearchVO boeSearchVO)

    			    throws DAOException

    			  {

    			    logger.info("-----------boeExChecker-----------");

    			    Connection con = null;

    			    LoggableStatement pst = null;

    			    ResultSet rs = null;

    			    ArrayList<BoeVO> boeList = null;

    			    BoeVO vo = null;

    			    CommonMethods commonMethods = null;

    			    String BOE_QUERY = null;

    			    String loginedUserId = null;

    			    boolean boeno = false;

    			    boolean boedate = false;

    			    boolean portcode = false;

    			    boolean loginid = false;

    			    int setValue = 0;

    			    try

    			    {

    			      con = DBConnectionUtility.getConnection();

    			      boeList = new ArrayList();

    			 
    			      HttpSession session = ServletActionContext.getRequest()

    			        .getSession();

    			      loginedUserId = (String)session.getAttribute("loginedUserId");

    			      logger.info("getExtensionQuery loginedUserId--------------------->" + loginedUserId);

    			      commonMethods = new CommonMethods();

    			      BOE_QUERY = "SELECT BOENUMBER,TO_CHAR(BOEDATE,'DD/MM/YYYY') AS BOEDATE,PORTOFDISCHARGE, ADCODE,IECODE,RECORDINDICATOR,EXTENSIONBY,LETTERNO,TO_CHAR(LETTERDATE,'DD-MM-YYYY') AS LETTERDATE, TO_CHAR(EXTENSIONDATE,'DD-MM-YYYY') AS EXTENSIONDATE,TO_CHAR(PREVEXTDATE,'DD-MM-YYYY') AS PREVEXTDATE, REMARKS,EXTENSION_SNO, EXT_INDICATOR FROM ETT_BOE_EXT_DATA WHERE STATUS = 'P' ";

    			      String boeNo = boeSearchVO.getBoeNo();

    			      if (!commonMethods.isNull(boeNo))

    			      {

    			        BOE_QUERY = BOE_QUERY + " AND UPPER(BOENUMBER) LIKE UPPER('%'||?||'%')";

    			        boeno = true;

    			      }

    			      String boeDate = boeSearchVO.getBoeDate();

    			      if (!commonMethods.isNull(boeDate))

    			      {

    			        BOE_QUERY = BOE_QUERY + " AND TO_CHAR(BOEDATE,'DD/MM/YYYY')  =?";

    			        boedate = true;

    			      }

    			      String portCode = boeSearchVO.getPortCode();

    			      if (!commonMethods.isNull(portCode))

    			      {

    			        BOE_QUERY = BOE_QUERY + " AND UPPER(PORTOFDISCHARGE) = UPPER(?)";

    			        portcode = true;

    			      }

    			      if (!commonMethods.isNull(loginedUserId))

    			      {

    			        BOE_QUERY = BOE_QUERY + " AND MAKER_USERID !=?";

    			        loginid = true;

    			      }

    			      BOE_QUERY = BOE_QUERY + " ORDER BY BOENUMBER ";

    			      pst = new LoggableStatement(con, BOE_QUERY);

    			      if (boeno) {

    			        pst.setString(++setValue, boeNo.trim());

    			      }

    			      if (boedate) {

    			        pst.setString(++setValue, boeDate.trim());

    			      }

    			      if (portcode) {

    			        pst.setString(++setValue, portCode.trim());

    			      }

    			      if (loginid) {

    			        pst.setString(++setValue, loginedUserId);

    			      }

    			      logger.info("BOE Extension Checker The Query is : query : " + BOE_QUERY);

    			      rs = pst.executeQuery();

    			      while (rs.next())

    			      {

    			        vo = new BoeVO();

    			        vo.setBoeNumber(rs.getString("BOENUMBER"));

    			        vo.setBoeDate(rs.getString("BOEDATE"));

    			        vo.setPortCode(rs.getString("PORTOFDISCHARGE"));

    			        vo.setAdCode(rs.getString("ADCODE"));

    			        vo.setIeCode(rs.getString("IECODE"));

    			        String recInd = rs.getString("RECORDINDICATOR");

    			        if ((recInd != null) && (recInd.equalsIgnoreCase("1"))) {

    			          vo.setRecordIndicator("New");

    			        } else if ((recInd != null) && (recInd.equalsIgnoreCase("3"))) {

    			          vo.setRecordIndicator("Cancel");

    			        }

    			        String extBy = rs.getString("EXTENSIONBY");

    			        if ((extBy != null) && (extBy.equalsIgnoreCase("1"))) {

    			          vo.setApprovedBy("RBI");

    			        } else if ((extBy != null) && (extBy.equalsIgnoreCase("2"))) {

    			          vo.setApprovedBy("AD Bank");

    			        } else if ((extBy != null) && (extBy.equalsIgnoreCase("3"))) {

    			          vo.setApprovedBy("Others");

    			        }

    			        vo.setExtLetterNo(rs.getString("LETTERNO"));

    			        vo.setExtLetterDate(rs.getString("LETTERDATE"));

    			        vo.setExtDate(rs.getString("EXTENSIONDATE"));

    			        vo.setExtRemarks(rs.getString("REMARKS"));

    			        vo.setPrevExtDate(rs.getString("PREVEXTDATE"));

    			        vo.setExKey(rs.getString("EXTENSION_SNO"));

    			        String recExtInd = rs.getString("EXT_INDICATOR");

    			        if ((recExtInd != null) && (recExtInd.equalsIgnoreCase("1"))) {

    			          vo.setBoeExtTxtInd("New");

    			        } else if ((recExtInd != null) && (recExtInd.equalsIgnoreCase("3"))) {

    			          vo.setBoeExtTxtInd("Cancel");

    			        }

    			        int i = 1;

    			        logger.info(i + " BOENUMBER : " + vo.getBoeNumber());

    			        logger.info(i + " BOEDATE : " + vo.getBoeNumber());

    			        logger.info(i + " PORTOFDISCHARGE : " + vo.getBoeNumber());

    			        logger.info(i + " ADCODE : " + vo.getBoeNumber());

    			        i++;

    			        boeList.add(vo);

    			      }

    			    }

    			    catch (Exception e)

    			    {

    			      logger.info("boeExChecker-------Exception" + e);

    			      e.printStackTrace();

    			    }

    			    finally

    			    {

    			      DBConnectionUtility.surrenderDB(con, pst, rs);

    			    }

    			    logger.info("Exiting Method");

    			    return boeList;

    			  }
				  public String getClosureQuery(BOEDataSearchVO boeSearchVO)
				  
    				  throws DAOException
				  
  				  {
				  
    				  CommonMethods commonMethods = null;
				  
    				  String BOE_QUERY = null;
				  
    				  String loginedUserId = null;
				  
    				  try
				  
    				  {
				  
      				  HttpSession session = ServletActionContext.getRequest()
				  
        				  .getSession();
				  
      				  loginedUserId = (String)session.getAttribute("loginedUserId");
				  
      				  logger.info("loginedUserId Query-------------->" + loginedUserId);
				  
      				  commonMethods = new CommonMethods();
				  
      				  BOE_QUERY = "SELECT BOENUMBER,TO_CHAR(BOEDATE,'DD/MM/YYYY') AS BOEDATE,PORTOFDISCHARGE, ADCODE,IECODE, RECORDINDICATOR, REMARKS,CLOSURE_SNO, BOE_CLOS_IND FROM ETT_BOE_CLOSURE_DATA WHERE STATUS = 'P' ";
 				  
      				  String boeNo = boeSearchVO.getBoeNo();
				  
      				  logger.info("boeNo -------------->" + boeNo);
				  
      				  if (!commonMethods.isNull(boeNo)) {
				  
        				  BOE_QUERY = BOE_QUERY + " AND UPPER(BOENUMBER) LIKE UPPER('%" + boeNo.trim() + "%')";
				  
      				  }
				  
      				  String boeDate = boeSearchVO.getBoeDate();
				  
      				  logger.info("boeDate -------------->" + boeDate);
				  
      				  if (!commonMethods.isNull(boeDate)) {
				  
        				  BOE_QUERY = BOE_QUERY + " AND TO_CHAR(BOEDATE,'DD/MM/YYYY')  = '" + boeDate.trim() + "'";
				  
      				  }
				  
      				  String portCode = boeSearchVO.getPortCode();
				  
      				  logger.info("portCode -------------->" + portCode);
				  
      				  if (!commonMethods.isNull(portCode)) {
				  
        				  BOE_QUERY = BOE_QUERY + " AND UPPER(PORTOFDISCHARGE) = UPPER('" + portCode.trim() + "')";
				  
      				  }
				  
      				  logger.info("loginedUserId -------------->" + loginedUserId);
				  
      				  if (!commonMethods.isNull(loginedUserId)) {
				  
        				  BOE_QUERY = BOE_QUERY + " AND MAKER_USERID != '" + loginedUserId + "'";
				  
      				  }
				  
      				  BOE_QUERY = BOE_QUERY + " ORDER BY BOENUMBER ";
				  
    				  }
				  
    				  catch (Exception exception)
				  
    				  {
				  
      				  logger.info("getClosureQuery --------exception------>" + exception);
				  
      				  throwDAOException(exception);
				  
    				  }
				  
    				  return BOE_QUERY;
				  
  				  }
				  
  				  public ArrayList<BoeVO> boeClosureChecker(BOEDataSearchVO boeSearchVO)
				  
    				  throws DAOException
				  
  				  {
				  
    				  logger.info("------------------boeClosureChecker-----1111-------------");
				  
    				  Connection con = null;
				  
    				  LoggableStatement pst = null;
				  
    				  ResultSet rs = null;
				  
    				  ArrayList<BoeVO> boeList = null;
				  
    				  CommonMethods commonMethods = null;
				  
    				  String BOE_QUERY = null;
				  
    				  String loginedUserId = null;
				  
    				  boolean boeno = false;
				  
    				  boolean boedate = false;
				  
    				  boolean portcode = false;
				  
    				  boolean loginid = false;
				  
    				  int setValue = 0;
				  
    				  try
				  
    				  {
				  
      				  con = DBConnectionUtility.getConnection();
				  
      				  boeList = new ArrayList();
 				  
      				  HttpSession session = ServletActionContext.getRequest()
				  
        				  .getSession();
				  
      				  loginedUserId = (String)session.getAttribute("loginedUserId");
				  
      				  logger.info("loginedUserId Query-------------->" + loginedUserId);
				  
      				  commonMethods = new CommonMethods();
				  
      				  BOE_QUERY = "SELECT BOENUMBER,TO_CHAR(BOEDATE,'DD/MM/YYYY') AS BOEDATE,PORTOFDISCHARGE, ADCODE,IECODE, RECORDINDICATOR, REMARKS,CLOSURE_SNO, BOE_CLOS_IND FROM ETT_BOE_CLOSURE_DATA WHERE STATUS = 'P' ";
 				  
      				  String boeNo = boeSearchVO.getBoeNo();
				  
      				  logger.info("boeNo -------------->" + boeNo);
				  
      				  if (!commonMethods.isNull(boeNo))
				  
      				  {
				  
        				  BOE_QUERY = BOE_QUERY + " AND UPPER(BOENUMBER) LIKE UPPER('%'||?||'%')";
				  
        				  boeno = true;
				  
      				  }
				  
      				  String boeDate = boeSearchVO.getBoeDate();
				  
      				  logger.info("boeDate -------------->" + boeDate);
				  
      				  if (!commonMethods.isNull(boeDate))
				  
      				  {
				  
        				  BOE_QUERY = BOE_QUERY + " AND TO_CHAR(BOEDATE,'DD/MM/YYYY')  = ?";
				  
        				  boedate = true;
				  
      				  }
				  
      				  String portCode = boeSearchVO.getPortCode();
				  
      				  logger.info("portCode -------------->" + portCode);
				  
      				  if (!commonMethods.isNull(portCode))
				  
      				  {
				  
        				  BOE_QUERY = BOE_QUERY + " AND UPPER(PORTOFDISCHARGE) = UPPER(?)";
				  
        				  portcode = true;
				  
      				  }
				  
      				  logger.info("loginedUserId -------------->" + loginedUserId);
				  
      				  if (!commonMethods.isNull(loginedUserId))
				  
      				  {
				  
        				  BOE_QUERY = BOE_QUERY + " AND MAKER_USERID != ?";
				  
        				  loginid = true;
				  
      				  }
				  
      				  BOE_QUERY = BOE_QUERY + " ORDER BY BOENUMBER ";
				  
      				  pst = new LoggableStatement(con, BOE_QUERY);
				  
      				  if (boeno) {
				  
        				  pst.setString(++setValue, boeNo.trim());
				  
      				  }
				  
      				  if (boedate) {
				  
        				  pst.setString(++setValue, boeDate.trim());
				  
      				  }
				  
      				  if (portcode) {
				  
        				  pst.setString(++setValue, portCode.trim());
				  
      				  }
				  
      				  if (loginid) {
				  
        				  pst.setString(++setValue, loginedUserId);
				  
      				  }
				  
      				  rs = pst.executeQuery();
      				logger.info("boeClosureChecker Query-------------->" + pst.getQueryString());

      		      while (rs.next())

      		      {

      		        BoeVO vo = new BoeVO();

      		        vo.setBoeNumber(rs.getString("BOENUMBER"));

      		        vo.setBoeDate(rs.getString("BOEDATE"));

      		        vo.setPortCode(rs.getString("PORTOFDISCHARGE"));

      		        vo.setAdCode(rs.getString("ADCODE"));

      		        vo.setIeCode(rs.getString("IECODE"));

      		        String recInd = rs.getString("RECORDINDICATOR");

      		        if ((recInd != null) && (recInd.equalsIgnoreCase("1"))) {

      		          vo.setRecordIndicator("New");

      		        } else if ((recInd != null) && (recInd.equalsIgnoreCase("3"))) {

      		          vo.setRecordIndicator("Cancel");

      		        }

      		        String recClosInd = rs.getString("BOE_CLOS_IND");

      		        if ((recClosInd != null) && (recClosInd.equalsIgnoreCase("1"))) {

      		          vo.setRecordClosIndicator("New");

      		        } else if ((recClosInd != null) && (recClosInd.equalsIgnoreCase("3"))) {

      		          vo.setRecordClosIndicator("Cancel");

      		        }

      		        vo.setClosureRemarks(rs.getString("REMARKS"));

      		        vo.setExKey(rs.getString("CLOSURE_SNO"));

      		        boeList.add(vo);

      		      }

      		    }

      		    catch (Exception e)

      		    {

      		      logger.info("boeClosureChecker-----------Exception ---------" + e);

      		    }

      		    finally

      		    {

      		      DBConnectionUtility.surrenderDB(con, pst, rs);

      		    }

      		    logger.info("Exiting Method");

      		    return boeList;

      		  }

      		  public ArrayList<BoeVO> boeExAuthorize(BOEDataSearchVO boeSearchVO, String[] exChkList, String boeExStatus, String remarks)

      		    throws DAOException

      		  {

      		    logger.info("-----------boeExAuthorize-----------");

      		    Connection con = null;

      		    LoggableStatement pst = null;

      		    ArrayList<BoeVO> boeList = null;

      		    try

      		    {

      		      HttpSession session = ServletActionContext.getRequest().getSession();

      		      String checkerId = (String)session.getAttribute("loginedUserId");

      		      logger.info("sessionUserName--------getAttribute--------------" + checkerId);

      		 
      		      con = DBConnectionUtility.getConnection();

      		      if (exChkList != null) {

      		        for (int i = 0; i < exChkList.length; i++)

      		        {

      		          String extensionNo = exChkList[i];

      		          pst = new LoggableStatement(con, "UPDATE ETT_BOE_EXT_DATA SET STATUS = ?,CHECKER_USERID = ?,CHECKER_REMARKS = ?,CHECKER_UPDATEDON = SYSTIMESTAMP WHERE EXTENSION_SNO = ?");

      		          pst.setString(1, boeExStatus);

      		          pst.setString(2, checkerId);

      		          pst.setString(3, remarks);

      		          pst.setString(4, extensionNo);

      		          logger.info("-----------boeExAuthorize---------Update--Query--" + pst.getQueryString());

      		          int a = pst.executeUpdate();

      		          logger.info("-----------boeExAuthorize-----count-" + a);

      		        }

      		      }

      		      boeList = boeExChecker(boeSearchVO);

      		    }

      		    catch (Exception e)

      		    {

      		      logger.info("-----------boeExAuthorize----exception-------" + e);

      		      e.printStackTrace();

      		    }

      		    finally

      		    {

      		      closeSqlRefferance(pst, con);

      		    }

      		    logger.info("Exiting Method");

      		    return boeList;

      		  }

      		  public ArrayList<BoeVO> boeClosureAuthorize(BOEDataSearchVO boeSearchVO, String[] clChkList, String boeClStatus, String remarks)

      		    throws DAOException

      		  {

      		    logger.info("-------boeClosureAuthorize-------------------");

      		    Connection con = null;

      		    LoggableStatement pst = null;

      		    ArrayList<BoeVO> boeList = null;

      		    CommonMethods commonMethods = null;

      		    try

      		    {

      		      HttpSession session = ServletActionContext.getRequest().getSession();

      		      String checkerId = (String)session.getAttribute("loginedUserId");

      		      logger.info("boeClosureAuthorize----------getAttribute------------------" + checkerId);

      		 
      		 
      		      con = DBConnectionUtility.getConnection();

      		      commonMethods = new CommonMethods();

      		      if (clChkList != null) {

      		        for (int i = 0; i < clChkList.length; i++)

      		        {

      		          String closureNo = clChkList[i];

      		          logger.info("boeClosureAuthorize-- ----closureNo----------" + closureNo);

      		          String boeType = "";

      		          String query = "SELECT BOE_CL_TYPE FROM ETT_BOE_CLOSURE_DATA WHERE CLOSURE_SNO = ? ";

      		          LoggableStatement ps = new LoggableStatement(con, query);

      		          ps.setString(1, closureNo);

      		          ResultSet rs = ps.executeQuery();

      		          logger.info("boeClosureAuthorize----ETT_BOE_CLOSURE_DATA-----query----------------" + ps.getQueryString());

      		          if (rs.next())

      		          {

      		            boeType = commonMethods.getEmptyIfNull(rs.getString("BOE_CL_TYPE")).trim();

      		            logger.info("boeType----boeType-----query----------------" + boeType);

      		          }

      		          closeStatementResultSet(ps, rs);

      		          String eodStatus = "";

      		          logger.info("boeType----------------" + boeType);

      		          logger.info("boeClStatus----------------" + boeClStatus);

      		          if ((boeType.equalsIgnoreCase("O")) && (boeClStatus.equalsIgnoreCase("A"))) {

      		            eodStatus = "C";

      		          }

      		          logger.info("checkerId----------------" + checkerId);

      		          logger.info("remarks----------------" + remarks);

      		          logger.info("eodStatus----------------" + eodStatus);

      		          logger.info("closureNo----------------" + closureNo);
      		        pst = new LoggableStatement(con, "UPDATE ETT_BOE_CLOSURE_DATA SET STATUS = ?,CHECKER_USERID = ?,CHECKER_REMARKS = ?,EOD_STATUS = ?,CHECKER_UPDATEDON = SYSTIMESTAMP WHERE CLOSURE_SNO = ?");

      	          pst.setString(1, boeClStatus);

      	          pst.setString(2, checkerId);

      	          pst.setString(3, remarks);

      	          pst.setString(4, eodStatus);

      	          pst.setString(5, closureNo);

      	          logger.info("BOE_CL_AUTHORIZE--------------Query" + pst.getQueryString());

      	          int checkCl = pst.executeUpdate();

      	          logger.info("BOE_CL_AUTHORIZE--count value------------Query" + checkCl);

      	          if (checkCl > 0)

      	          {

      	            LoggableStatement pst1 = new LoggableStatement(con, "UPDATE ETT_BOE_INV_CLOSURE SET STATUS = ?,EOD_STATUS = ? WHERE CLOSURE_SNO = ?");

      	            pst1.setString(1, boeClStatus);

      	            pst1.setString(2, eodStatus);

      	            pst1.setString(3, closureNo);

      	            logger.info("BOE_CL_INV_AUTHORIZE--------------Query" + pst1.getQueryString());

      	            int a = pst1.executeUpdate();

      	            logger.info("BOE_CL_INV_AUTHORIZE--------------a" + a);

      	            pst1.close();

      	          }

      	        }

      	      }

      	      logger.info("-------boeClosureChecker----111111111111---------------");

      	      boeList = boeClosureChecker(boeSearchVO);

      	      logger.info("-------boeClosureChecker----2222222222222222---------------");

      	    }

      	    catch (Exception e)

      	    {

      	      logger.info("-------boeClosureAuthorize------------exception-------" + e);

      	      e.printStackTrace();

      	    }

      	    finally

      	    {

      	      closeSqlRefferance(pst, con);

      	    }

      	    logger.info("Exiting Method");

      	    return boeList;

      	  }

      	  public ArrayList<BoeVO> getBOEClosure(BOEDataSearchVO boeSearchVO)

      	    throws DAOException

      	  {

      	    logger.info("Entering Method");

      	    Connection con = null;

      	    LoggableStatement pst = null;

      	    ResultSet rs = null;

      	    ArrayList<BoeVO> boeList = null;

      	    try

      	    {

      	      con = DBConnectionUtility.getConnection();

      	      boeList = new ArrayList();

      	      String query = getClosureQuery(boeSearchVO);

      	      pst = new LoggableStatement(con, query);

      	      rs = pst.executeQuery();

      	      while (rs.next())

      	      {

      	        BoeVO vo = new BoeVO();

      	        vo.setBoeNumber(rs.getString("BOENUMBER"));

      	        vo.setBoeDate(rs.getString("BOEDATE"));

      	        vo.setPortCode(rs.getString("PORTOFDISCHARGE"));

      	        vo.setAdCode(rs.getString("ADCODE"));

      	        vo.setIeCode(rs.getString("IECODE"));

      	        String recInd = rs.getString("RECORDINDICATOR");

      	        if ((recInd != null) && (recInd.equalsIgnoreCase("1"))) {

      	          vo.setRecordIndicator("New");

      	        } else if ((recInd != null) && (recInd.equalsIgnoreCase("3"))) {

      	          vo.setRecordIndicator("Cancel");

      	        }

      	        vo.setClosureRemarks(rs.getString("REMARKS"));

      	        vo.setExKey(rs.getString("CLOSURE_SNO"));

      	        boeList.add(vo);

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

      	    logger.info("Exiting Method");

      	    return boeList;

      	  }

      	  public BoeVO boeView(BOEDataSearchVO boeDataSearchVO)

      	    throws DAOException

      	  {

      	    logger.info("Entering Method");

      	    Connection con = null;

      	    LoggableStatement pst = null;

      	    ResultSet rs = null;

      	    BoeVO boevo = null;

      	    try

      	    {

      	      con = DBConnectionUtility.getConnection();

      	      String query = "SELECT BOENUMBER,TO_CHAR(BOEDATE,'DD/MM/YYYY') AS BOEDATE,PORTOFDISCHARGE,ADCODE,IECODE, RECORDINDICATOR,ADJ_IND,TO_CHAR(ADJ_DATE,'DD/MM/YYYY') AS ADJ_DATE,DOCUMENT_NO, TO_CHAR(DOCUMENT_DATE,'DD/MM/YYYY') AS DOCUMENT_DATE,DOCUMENT_PORT,APPROVEDBY, LETTERNO,TO_CHAR(LETTERDATE,'DD/MM/YYYY') AS LETTERDATE,REMARKS,BOE_CL_TYPE  FROM ETT_BOE_CLOSURE_DATA WHERE CLOSURE_SNO = ? ";

      	      pst = new LoggableStatement(con, query);

      	      pst.setString(1, boeDataSearchVO.getUniqueNo());

      	      rs = pst.executeQuery();

      	      if (rs.next())

      	      {

      	        boevo = new BoeVO();

      	        boevo.setBoeNumber(rs.getString("BOENUMBER"));

      	        boevo.setBoeDate(rs.getString("BOEDATE"));

      	        boevo.setPortCode(rs.getString("PORTOFDISCHARGE"));

      	        boevo.setAdCode(rs.getString("ADCODE"));

      	        boevo.setIeCode(rs.getString("IECODE"));

      	        boevo.setAdjClosureIndicator(rs.getString("ADJ_IND"));

      	        boevo.setAdjClosureDate(rs.getString("ADJ_DATE"));

      	        boevo.setDocNo(rs.getString("DOCUMENT_NO"));

      	        boevo.setDocDate(rs.getString("DOCUMENT_DATE"));

      	        boevo.setDocPort(rs.getString("DOCUMENT_PORT"));

      	        boevo.setApprovedBy(rs.getString("APPROVEDBY"));

      	        boevo.setRecordIndicator(rs.getString("RECORDINDICATOR"));

      	        boevo.setClosureLetterNo(rs.getString("LETTERNO"));

      	        boevo.setClosureLetterDate(rs.getString("LETTERDATE"));

      	        boevo.setClosureRemarks(rs.getString("REMARKS"));

      	        boevo.setClTransType(rs.getString("BOE_CL_TYPE"));

      	      }

      	      ArrayList<InvoiceDetailsVO> invoiceList = getInvoiceClosureDetails(boeDataSearchVO);

      	      if ((invoiceList.size() > 0) && (!invoiceList.isEmpty())) {

      	        boevo.setInvoiceList(invoiceList);

      	      }

      	    }

      	    catch (Exception e)

      	    {

      	      e.printStackTrace();

      	      throwDAOException(e);

      	    }

      	    finally

      	    {

      	      DBConnectionUtility.surrenderDB(con, pst, rs);

      	    }

      	    logger.info("Exiting Method");

      	    return boevo;

      	  }

      	  public ArrayList<InvoiceDetailsVO> getInvoiceClosureDetails(BOEDataSearchVO boeDataSearchVO)

      	  {

      	    logger.info("Entering Method");

      	    Connection con = null;

      	    LoggableStatement pst = null;

      	    ResultSet rs = null;

      	    ArrayList<InvoiceDetailsVO> invoiecList = null;

      	    try

      	    {

      	      invoiecList = new ArrayList();

      	      con = DBConnectionUtility.getConnection();

      	      String query = "SELECT INV_SNO,INV_NO,INV_AMT,INVOICECURR,ADJ_INVAMT FROM ETT_BOE_INV_CLOSURE WHERE CLOSURE_SNO = '" + 

      	        boeDataSearchVO.getUniqueNo() + "' ORDER BY TO_NUMBER(INV_SNO) ";

      	      pst = new LoggableStatement(con, query);

      	      rs = pst.executeQuery();

      	      while (rs.next())
      	    {
      	        InvoiceDetailsVO invoiceVO = new InvoiceDetailsVO();
      	        invoiceVO.setInvoiceSerialNo(rs.getString("INV_SNO"));
      	        invoiceVO.setInvoiceNo(rs.getString("INV_NO"));
      	        invoiceVO.setInvoiceAmt(rs.getString("INV_AMT"));
      	        invoiceVO.setInvoiceCurrency(rs.getString("INVOICECURR"));
      	        invoiceVO.setAdjsustAmtIC(rs.getString("ADJ_INVAMT"));
      	        invoiecList.add(invoiceVO);
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
      	    return invoiecList;
      	  }
      	  public BoeVO getBoeDetailss(BoeVO boevo)
      	  {
      	    logger.info("Entering Method");
      	    Connection con = null;
      	    LoggableStatement log = null;
      	    ResultSet res = null;
      	    CommonMethods commonMethods = null;
      	    try
      	    {
      	      con = DBConnectionUtility.getConnection();
      	      commonMethods = new CommonMethods();
      	      String query = "SELECT BOE_RECORD_INDICATOR AS RECORD_INDICATOR,BOE_IMPORT_AGENCY AS IMPORT_AGENCY,BOE_AD_CODE AS AD_CODE, BOE_GP AS GP,BOE_IE_CODE AS IE_CODE,BOE_IE_NAME AS IE_NAME,BOE_IE_ADDRESS AS IE_ADDRESS,BOE_IE_PANNUMBER AS PAN_NO,BOE_PORT_OF_SHIPMENT AS PORT_OF_SHIPMENT,BOE_IGMNUMBER AS IGM_NO,TO_CHAR(BOE_IGMDATE,'DD/MM/YYYY') AS IGM_DATE,BOE_HAWB_HBLNUMBER AS HBL_NO,TO_CHAR(BOE_HAWB_HBLDATE,'DD/MM/YYYY') AS HBL_DATE, BOE_MAWB_MBLNUMBER AS MBL_NO,TO_CHAR(BOE_MAWB_MBLDATE,'DD/MM/YYYY') AS MBL_DATE, STATUS AS STATUS  FROM ETT_BOE_DETAILS WHERE BOE_NUMBER = ?  AND TO_CHAR(BOE_DATE,'DD/MM/YYYY') = ? AND BOE_PORT_OF_DISCHARGE = ? ";
      	      log = new LoggableStatement(con, query);
      	      log.setString(1, commonMethods.getEmptyIfNull(boevo.getBillNo()).trim());
      	      log.setString(2, commonMethods.getEmptyIfNull(boevo.getBillDate()).trim());
      	      log.setString(3, commonMethods.getEmptyIfNull(boevo.getBillPort()).trim());
      	      res = log.executeQuery();
      	      if (res.next())
      	      {
      	        boevo.setBillAdCode(res.getString("AD_CODE"));
      	        boevo.setBillIeCode(res.getString("IE_CODE"));
      	        boevo.setBillIeName(res.getString("IE_NAME"));
      	        boevo.setBillIeAddress(res.getString("IE_ADDRESS"));
      	        boevo.setBillIePanNumber(res.getString("PAN_NO"));
      	        boevo.setBillPortOfShipment(res.getString("PORT_OF_SHIPMENT"));
      	        boevo.setBillIgmNo(res.getString("IGM_NO"));
      	        boevo.setBillIgmDate(res.getString("IGM_DATE"));
      	        boevo.setBillMblNo(res.getString("MBL_NO"));
      	        boevo.setBillMblDate(res.getString("MBL_DATE"));
      	        boevo.setBillHblNo(res.getString("HBL_NO"));
      	        boevo.setBillHblDate(res.getString("HBL_DATE"));
      	        String gpType = commonMethods.getEmptyIfNull(res.getString("GP")).trim();
      	        if (gpType.equalsIgnoreCase("G")) {
      	          boevo.setBillGp("Government");
      	        } else {
      	          boevo.setBillGp("Private");
      	        }
      	        String recInd = commonMethods.getEmptyIfNull(res.getString("RECORD_INDICATOR")).trim();
      	        if (recInd.equalsIgnoreCase("1")) {
      	          boevo.setBillRecordIndicator("New");
      	        } else if (recInd.equalsIgnoreCase("2")) {
      	          boevo.setBillRecordIndicator("Amendment");
      	        } else {
      	          boevo.setBillRecordIndicator("Cancel");
      	        }
      	        String imAgency = commonMethods.getEmptyIfNull(res.getString("IMPORT_AGENCY")).trim();
      	        if (imAgency.equalsIgnoreCase("1")) {
      	          boevo.setBillImportAgency("Customs");
      	        } else {
      	          boevo.setBillImportAgency("SEZ");
      	        }
      	      }
      	      boevo = fetchInvoiceList(con, boevo);
      	    }
      	    catch (Exception e)
      	    {
      	      e.printStackTrace();
      	    }
      	    finally
      	    {
      	      closeSqlRefferance(res, log, con);
      	    }
      	    logger.info("Exiting Method");
      	    return boevo;
      	  }
      	  private BoeVO fetchInvoiceList(Connection con, BoeVO boevo)
      	  {
      	    logger.info("Entering Method");
      	    LoggableStatement ls = null;
      	    ResultSet rs = null;
      	    ArrayList<InvoiceDetailsVO> invoiceList = null;
      	    CommonMethods commonMethods = null;
      	    try
      	    {
      	      if (con == null) {
      	        con = DBConnectionUtility.getConnection();
      	      }
      	      invoiceList = new ArrayList();
      	      String query = null;
      	      commonMethods = new CommonMethods();
      	      logger.info("boevo.getBillNo()" + boevo.getBillNo());
      	      query = "SELECT INV.BOE_NUMBER AS BOE_NUMBER,INV.BOE_DATE AS BOE_DATE,INV.BOE_PORT_OF_DISCHARGE AS BOE_PORT_OF_DISCHARGE,INV.INVOICE_SERIAL_NUMBER AS INV_SERIAL_NO,INV.INVOICE_NUMBER AS INV_NO,INV.INVOICE_TERMS_OF_INVOICE AS INV_TERMS,INV.INVOICE_FOBAMOUNT AS FOB_AMT,INV.INVOICE_FOBCURRENCY AS FOB_CURR,INV.INVOICE_FRIEGHTAMOUNT AS FRI_AMT,INV.INVOICE_FRIEGHTCURRENCYCODE AS FRI_CURR,INV.INVOICE_INSURANCEAMOUNT AS INS_AMT,INV.INVOICE_INSURANCECURRENCY_CODE AS INS_CURR,INV.INVOICE_DISCOUNT_CHARGES AS DISC_CHARGES,INV.INVOICE_DISCOUNT_CURRENCY AS DISC_CURR,INV.INVOICE_AGENCY_COMMISSION AS AGENCY_COMM,INV.INVOICE_AGENCY_CURRENCY AS AGENCY_CURR,INV.INVOICE_MISCELLANEOUS_CHARGES AS MISC_CHARGES,INV.INVOICE_MISCELLANEOUS_CURRENCY AS MISC_CURR,INV.INVOICE_SUPPLIER_NAME AS SUPP_NAME,INV.INVOICE_SUPPLIER_ADDRESS AS SUPP_ADDR,INV.INVOICE_SUPPLIER_COUNTRY AS SUPP_CTY,INV.INVOICE_SELLER_NAME AS SELL_NAME,INV.INVOICE_SELLER_ADDRESS AS SELL_ADDR,INV.INVOICE_SELLER_COUNTRY AS SELL_CTY,INV.INVOICE_THIRDPARTY_NAME AS THIRD_NAME,INV.INVOICE_THIRDPARTY_ADDRESS AS THIRD_ADDR,INV.INVOICE_THIRDPARTY_COUNTRY AS THIRD_CTY,NVL(IPAY.REAL_AMTIC,0) AS REAL_AMTIC,NVL(BCL.CLOSURE_AMTIC,0) AS  CLOSURE_AMTIC FROM ETT_INVOICE_DETAILS INV,(SELECT INV_SNO,INV_NO,BOE_NO,BOE_DATE,PORTCODE,SUM(REAL_ORM_AMT) AS REAL_AMTIC FROM ETT_BOE_INV_PAYMENT GROUP BY INV_SNO,INV_NO,BOE_NO,BOE_DATE,PORTCODE) IPAY,(SELECT INV_SNO,INV_NO,BOE_NO,BOE_DATE,PORTCODE,SUM(ADJ_INVAMT) AS CLOSURE_AMTIC FROM ETT_BOE_INV_CLOSURE WHERE STATUS != 'R' GROUP BY INV_SNO,INV_NO,BOE_NO,BOE_DATE,PORTCODE) BCL WHERE INV.INVOICE_SERIAL_NUMBER        = IPAY.INV_SNO  (+) AND INV.INVOICE_NUMBER                 = IPAY.INV_NO   (+) AND INV.BOE_NUMBER                     = IPAY.BOE_NO   (+) AND INV.BOE_DATE                       = IPAY.BOE_DATE (+) AND INV.BOE_PORT_OF_DISCHARGE          = IPAY.PORTCODE (+) AND INV.INVOICE_SERIAL_NUMBER          = BCL.INV_SNO   (+) AND INV.INVOICE_NUMBER                 = BCL.INV_NO    (+) AND INV.BOE_NUMBER                     = BCL.BOE_NO    (+) AND INV.BOE_DATE                       = BCL.BOE_DATE  (+) AND INV.BOE_PORT_OF_DISCHARGE          = BCL.PORTCODE  (+) AND INV.BOE_NUMBER = ? AND TO_CHAR(INV.BOE_DATE,'DD/MM/YYYY') = ? AND INV.BOE_PORT_OF_DISCHARGE = ? ORDER BY INV.INVOICE_SERIAL_NUMBER  ";
      	      ls = new LoggableStatement(con, query);
      	      logger.info("boevo.getBillNo() inside setstring()" + boevo.getBillNo());
      	      ls.setString(1, commonMethods.getEmptyIfNull(boevo.getBillNo()).trim());
      	      ls.setString(2, commonMethods.getEmptyIfNull(boevo.getBillDate()).trim());
      	      ls.setString(3, commonMethods.getEmptyIfNull(boevo.getBillPort()).trim());
      	      logger.info("ls.getQueryString()" + ls.getQueryString());
      	      rs = ls.executeQuery();
      	      while (rs.next())
      	      {
      	        InvoiceDetailsVO invoiceVO = new InvoiceDetailsVO();
      	        invoiceVO.setInvoiceSerialNo(rs.getString("INV_SERIAL_NO"));
      	        invoiceVO.setInvoiceNo(rs.getString("INV_NO"));
      	        invoiceVO.setTermsofInvoice(rs.getString("INV_TERMS"));
      	        invoiceVO.setInvoiceAmt(rs.getString("FOB_AMT"));
      	        invoiceVO.setInvoiceCurrency(rs.getString("FOB_CURR"));
      	        invoiceVO.setFreightAmount(rs.getString("FRI_AMT"));
      	        invoiceVO.setFreightCurrencyCode(rs.getString("FRI_CURR"));
      	        invoiceVO.setInsuranceAmount(rs.getString("INS_AMT"));
      	        invoiceVO.setInsuranceCurrencyCode(rs.getString("INS_CURR"));
      	        invoiceVO.setAgencyCommission(rs.getString("AGENCY_COMM"));
      	        invoiceVO.setAgencyCurrency(rs.getString("AGENCY_CURR"));
      	        invoiceVO.setDiscountCharges(rs.getString("DISC_CHARGES"));
      	        invoiceVO.setDiscountCurrency(rs.getString("DISC_CURR"));
      	        invoiceVO.setMiscellaneousCharges(rs.getString("MISC_CHARGES"));
      	        invoiceVO.setMiscellaneousCurrency(rs.getString("MISC_CURR"));
      	        invoiceVO.setSupplierName(rs.getString("SUPP_NAME"));
      	        invoiceVO.setSupplierAddress(rs.getString("SUPP_ADDR"));
      	        invoiceVO.setSupplierCountry(rs.getString("SUPP_CTY"));
      	        invoiceVO.setSellerName(rs.getString("SELL_NAME"));
      	        invoiceVO.setSellerAddress(rs.getString("SELL_ADDR"));
      	        invoiceVO.setSellerCountry(rs.getString("SELL_CTY"));
      	        invoiceVO.setThirdPartyName(rs.getString("THIRD_NAME"));
      	        invoiceVO.setThirdPartyAddress(rs.getString("THIRD_ADDR"));
      	        invoiceVO.setThirdPartyCountry(rs.getString("THIRD_CTY"));
      	        double fobAmt = commonMethods.convertToDouble(rs.getString("FOB_AMT"));
      	        double friAmt = commonMethods.convertToDouble(rs.getString("FRI_AMT"));
      	        double insAmt = commonMethods.convertToDouble(rs.getString("INS_AMT"));
      	      double totalAmt = fobAmt;

              double totalInvAmt = Math.round(totalAmt * 100.0D) / 100.0D;

              BigDecimal toInvAmt = BigDecimal.valueOf(totalInvAmt).setScale(2, RoundingMode.HALF_UP);

              invoiceVO.setTotalInvAmt(String.valueOf(toInvAmt));

              invoiceVO.setAdjsustAmtIC(rs.getString("REAL_AMTIC"));

              invoiceVO.setOutAmtIC(rs.getString("CLOSURE_AMTIC"));

              invoiceList.add(invoiceVO);

            }

            boevo.setInvoiceList(invoiceList);

          }

          catch (Exception e)

          {

            e.printStackTrace();

          }

          finally

          {

            DBConnectionUtility.surrenderDB(null, ls, rs);

          }

          logger.info("Exiting Method");

          return boevo;

        }

        public ArrayList<BoeVO> fetchPaymentDetails(BOEDataSearchVO boeSearchVO)

        {

          logger.info("Entering Method");

          Connection con = null;

          LoggableStatement log = null;

          ResultSet rs = null;

          CommonMethods commonMethods = null;

          BoeVO boevo = null;

          ArrayList<BoeVO> boeList = null;

          boolean boeno = false;

          boolean boedate = false;

          boolean portcodee = false;

          boolean billref = false;

          boolean eventref = false;

          boolean cifref = false;

          int setValue = 0;

          try

          {

            con = DBConnectionUtility.getConnection();

            commonMethods = new CommonMethods();

            boeList = new ArrayList();

       
            String BOE_QUERY = " SELECT TRIM(BOE_PAYMENT_BP_BOE_NO) AS BILLOFENTRYNUMBER,TO_CHAR(BOE_PAYMENT_BP_BOE_DT,'DD/MM/YYYY') AS BILLOFENTRYDATE,TRIM(PORT_CODE) AS PORTOFDISCHARGE,TRIM(BOE_ENTRY_AMT) AS BOE_ENTRY_AMT,TRIM(BOE_PAYMENT_BP_BOE_CCY) AS BOE_ENTRY_CURR,TRIM(BOE_PAYMENT_CIF_NAME) AS CUSTOMER_NAME,TRIM(BOE_PAYMENT_BENEF_NAME) AS BENEF_NAME,TRIM(BOE_PAYMENT_BENEF_COUNTRY) AS BENEF_CTY,TRIM(BOE_PAYMENT_BP_PAY_REF) AS MASTER_REFNO,TRIM(BOE_PAYMENT_BP_PAY_PART_REF) AS EVENTREFNO,TRIM(BOE_PAYMENT_BP_PAY_CCY) AS REMITTANCECURRENCY,TRIM(BOE_PAYMENT_BP_PAY_FC_AMT) AS BILL_AMT,NVL(TRIM(BOE_PAYMENT_BP_PAY_ENDORSE_AMT),0) AS REAL_AMT,NVL(TRIM(ENDORSED_BOE_AMT),0) AS REAL_ORM_AMT FROM ETT_BOE_PAYMENT WHERE BOE_PAYMENT_BP_PAY_REF=BOE_PAYMENT_BP_PAY_REF";

            logger.info("----" + BOE_QUERY);

            String boeNo = boeSearchVO.getBoeNo();

            if (!commonMethods.isNull(boeNo))

            {

              BOE_QUERY = BOE_QUERY + " AND UPPER(BOE_PAYMENT_BP_BOE_NO) LIKE UPPER('%'||?||'%')";

              boeno = true;

            }

            String boeDate = boeSearchVO.getBoeDate();

            if (!commonMethods.isNull(boeDate))

            {

              BOE_QUERY = BOE_QUERY + " AND TO_CHAR(BOE_PAYMENT_BP_BOE_DT,'DD/MM/YYYY')  = ?";

              boedate = true;

            }

            String portCode = boeSearchVO.getPortCode();

            if (!commonMethods.isNull(portCode))

            {

              BOE_QUERY = BOE_QUERY + " AND UPPER(PORT_CODE) LIKE UPPER('%'||?||'%')";

              portcodee = true;

            }

            String billRefNo = boeSearchVO.getPaymentRefNo();

            if (!commonMethods.isNull(billRefNo))

            {

              BOE_QUERY = BOE_QUERY + " AND UPPER(BOE_PAYMENT_BP_PAY_REF) LIKE UPPER('%'||?||'%')";

              billref = true;

            }

            String eventNo = boeSearchVO.getEventRefNo();

            if (!commonMethods.isNull(eventNo))

            {

              BOE_QUERY = BOE_QUERY + " AND BOE_PAYMENT_BP_PAY_PART_REF  = ?";

              eventref = true;

            }

            String cifNo = boeSearchVO.getCifNo();

            if (!commonMethods.isNull(cifNo))

            {

              BOE_QUERY = BOE_QUERY + " AND BOE_PAYMENT_CIF_ID LIKE ('%'||?||'%')";

              cifref = true;

            }

            logger.info("query--" + BOE_QUERY);

            log = new LoggableStatement(con, BOE_QUERY);

            if (boeno) {

              log.setString(++setValue, boeNo.trim());

            }

            if (boedate) {

              log.setString(++setValue, boeDate.trim());

            }

            if (portcodee) {

              log.setString(++setValue, portCode.trim());

            }

            if (billref) {

              log.setString(++setValue, billRefNo.trim());

            }

            if (eventref) {

              log.setString(++setValue, eventNo.trim());

            }

            if (cifref) {

              log.setString(++setValue, cifNo.trim());

            }

            logger.info("Search Query------------------------" + log.getQueryString());

            logger.info("-------");

            rs = log.executeQuery();

            logger.info("Search Query1------------------------" + log.getQueryString());

       
            logger.info("---");

            while (rs.next())

            {

              logger.info("---rs---");

              boevo = new BoeVO();

              String boeNo1 = rs.getString("BILLOFENTRYNUMBER");

              String boDate = rs.getString("BILLOFENTRYDATE");

              String portcode = rs.getString("PORTOFDISCHARGE");

              String boeAmt = rs.getString("BOE_ENTRY_AMT");

              boevo.setBoeNumber(boeNo1);

              boevo.setBoeDate(boDate);

              boevo.setPortCode(portcode);

              boevo.setBoeamount(boeAmt);

              boevo.setBoeccy(rs.getString("BOE_ENTRY_CURR"));

              boevo.setLnkdAMT(rs.getString("REAL_AMT"));

              boevo.setLnkdAMTBOECCY(rs.getString("REAL_ORM_AMT"));

              double totalRealAmt = fetchPreviousEndoreAmount(con, boeNo1, boDate, portcode);

              double bAmount = commonMethods.toDouble(boeAmt);

              double outAmt = bAmount - totalRealAmt;

              outAmt = Math.round(outAmt * 100.0D) / 100.0D;

              double clAmount = getClosureAmountData(con, boeNo1, boDate, portcode);

              boevo.setClosureAmount(String.valueOf(clAmount));

              boevo.setBoeOSAMT(String.valueOf(outAmt));

              boevo.setBillid(rs.getString("MASTER_REFNO"));

              boevo.setBillevent(rs.getString("EVENTREFNO"));

              boevo.setBillAmt(rs.getString("BILL_AMT"));

              boevo.setBillCurrency(rs.getString("REMITTANCECURRENCY"));

              boevo.setCustomername(rs.getString("CUSTOMER_NAME"));

              boevo.setBeneficiaryname(rs.getString("BENEF_NAME"));

              boevo.setBeneficiarycountry(rs.getString("BENEF_CTY"));

              boeList.add(boevo);

            }

          }

          catch (Exception e)

          {

            logger.info("1111111111111--------------------------" + e.getMessage());

            e.printStackTrace();

          }

          finally

          {

            closeSqlRefferance(rs, log, con);

          }

          logger.info("Exiting Method");

          return boeList;

        }

        public double fetchPreviousEndoreAmount(Connection con, String boeNo, String boeDate, String portCode)

          throws DAOException

        {

          logger.info("Entering Method");

          String sqlQuery = null;

          CommonMethods commonMethods = null;

          double totalRealAmt = 0.0D;

          try

          {

            if (con == null) {

              con = DBConnectionUtility.getConnection();

            }

            commonMethods = new CommonMethods();

            double realAmtIc = 0.0D;

            sqlQuery = "SELECT ROUND(SUM(ENDORSED_BOE_AMT),2) AS TOTAL_AMT FROM ETT_BOE_PAYMENT  WHERE BOE_PAYMENT_BP_BOE_NO = '" + 

              commonMethods.getEmptyIfNull(boeNo).trim() + "'" + 

              " AND TO_CHAR(BOE_PAYMENT_BP_BOE_DT,'DD/MM/YYYY') = '" + commonMethods.getEmptyIfNull(boeDate).trim() + "'" + 

              " AND PORT_CODE = '" + commonMethods.getEmptyIfNull(portCode).trim() + "'" + 

              " GROUP BY BOE_PAYMENT_BP_BOE_NO ";

            LoggableStatement pst1 = new LoggableStatement(con, sqlQuery);

            logger.info("fetchPreviousEndoreAmount----------------------query---" + pst1.getQueryString());

            ResultSet rs1 = pst1.executeQuery();

            if (rs1.next()) {

              realAmtIc = commonMethods.toDouble(rs1.getString("TOTAL_AMT"));

            }

            closeStatementResultSet(pst1, rs1);
            double adjAmtIc = 0.0D;

            sqlQuery = "SELECT SUM(ADJ_INVAMT) AS CLOSURE_AMTIC FROM ETT_BOE_INV_CLOSURE  WHERE BOE_NO = '" + 

              commonMethods.getEmptyIfNull(boeNo).trim() + "'" + 

              " AND TO_CHAR(BOE_DATE,'DD/MM/YYYY') = '" + commonMethods.getEmptyIfNull(boeDate).trim() + "'" + 

              " AND PORTCODE = '" + commonMethods.getEmptyIfNull(portCode).trim() + "' AND STATUS != 'R' ";

            LoggableStatement pst2 = new LoggableStatement(con, sqlQuery);

            logger.info("ETT_BOE_INV_CLOSURE-------------pst2---------query---" + pst2.getQueryString());

            ResultSet rs2 = pst2.executeQuery();

            if (rs2.next()) {

              adjAmtIc = commonMethods.toDouble(rs2.getString("CLOSURE_AMTIC"));

            }

            closeStatementResultSet(pst2, rs2);

            totalRealAmt = realAmtIc + adjAmtIc;

            totalRealAmt = Math.round(totalRealAmt * 100.0D) / 100.0D;

          }

          catch (SQLException e)

          {

            logger.info("fetchPreviousEndoreAmount----------------------" + e.getMessage());

            throwDAOException(e);

          }

          logger.info("Exiting Method");

          return totalRealAmt;

        }

        public double getClosureAmountData(Connection con, String boeNo, String boeDate, String portCode)

          throws DAOException

        {

          logger.info("Entering Method");

          String sqlQuery = null;

          CommonMethods commonMethods = null;

          double tempAdjAmtIc = 0.0D;

          try

          {

            if (con == null) {

              con = DBConnectionUtility.getConnection();

            }

            commonMethods = new CommonMethods();

            double adjAmtIc = 0.0D;

            sqlQuery = "SELECT SUM(ADJ_INVAMT) AS CLOSURE_AMTIC FROM ETT_BOE_INV_CLOSURE  WHERE BOE_NO = '" + 

              commonMethods.getEmptyIfNull(boeNo).trim() + "'" + 

              " AND TO_CHAR(BOE_DATE,'DD/MM/YYYY') = '" + commonMethods.getEmptyIfNull(boeDate).trim() + "'" + 

              " AND PORTCODE = '" + commonMethods.getEmptyIfNull(portCode).trim() + "' AND STATUS != 'R' ";

            LoggableStatement pst2 = new LoggableStatement(con, sqlQuery);

            ResultSet rs2 = pst2.executeQuery();

            if (rs2.next()) {

              adjAmtIc = commonMethods.toDouble(rs2.getString("CLOSURE_AMTIC"));

            }

            closeStatementResultSet(pst2, rs2);

            tempAdjAmtIc = Math.round(adjAmtIc * 100.0D) / 100.0D;

          }

          catch (SQLException e)

          {

            throwDAOException(e);

          }

          logger.info("Exiting Method");

          return tempAdjAmtIc;

        }

        public int getBoeExCount(BoeVO boevo)

          throws DAOException

        {

          logger.info("Entering Method");

          Connection con = null;

          LoggableStatement pst = null;

          ResultSet rs = null;

          int boeExtCount = 0;

          try

          {

            con = DBConnectionUtility.getConnection();

            String query = "SELECT COUNT(*) AS EXT_COUNT FROM ETT_BOE_EXT_DATA WHERE BOENUMBER = ?  AND TO_CHAR(BOEDATE,'DD/MM/YYYY') = ? AND PORTOFDISCHARGE = ? AND STATUS = 'P' ";

       
            pst = new LoggableStatement(con, query);

            pst.setString(1, boevo.getBoeNumber());

            pst.setString(2, boevo.getBoeDate());

            pst.setString(3, boevo.getPortCode());

            rs = pst.executeQuery();

            if (rs.next()) {

              boeExtCount = rs.getInt("EXT_COUNT");

            }

          }

          catch (Exception e)

          {

            throwDAOException(e);

          }

          finally

          {

            closeSqlRefferance(rs, pst, con);

          }

          logger.info("Exiting Method");

          return boeExtCount;

        }

        public int getBoeExStatus(BoeVO boevo)

          throws DAOException

        {

          logger.info("Entering Method");

          Connection con = null;

          LoggableStatement pst = null;

          ResultSet rs = null;

          int boeExtCount = 0;

          try

          {

            con = DBConnectionUtility.getConnection();

            String query = "SELECT COUNT(*) AS EXT_COUNT FROM ETT_BOE_EXT_DATA WHERE BOENUMBER = ?  AND TO_CHAR(BOEDATE,'DD/MM/YYYY') = ? AND PORTOFDISCHARGE = ? AND STATUS = 'A'  AND EOD_STATUS IS NULL ";

       
       
            pst = new LoggableStatement(con, query);

            pst.setString(1, boevo.getBoeNumber());

            pst.setString(2, boevo.getBoeDate());

            pst.setString(3, boevo.getPortCode());

            rs = pst.executeQuery();

            if (rs.next()) {

              boeExtCount = rs.getInt("EXT_COUNT");

            }
          }

          catch (Exception e)

          {

            throwDAOException(e);

          }

          finally

          {

            closeSqlRefferance(rs, pst, con);

          }

          logger.info("Exiting Method");

          return boeExtCount;

        }

        public int getMBEStatus(BoeVO boevo)

          throws DAOException

        {

          logger.info("Entering Method");

          Connection con = null;

          LoggableStatement pst = null;

          ResultSet rs = null;

          int mbeCount = 0;

          try

          {

            con = DBConnectionUtility.getConnection();

       
            int boeCount = 0;

            String boeQuery = "SELECT COUNT(*) AS BOE_COUNT FROM ETT_BOE_DETAILS WHERE BOE_NUMBER = ?  AND TO_CHAR(BOE_DATE,'DD/MM/YYYY') = ? AND BOE_PORT_OF_DISCHARGE = ?  AND STATUS = 'A' AND BOE_TYPE = 'M' ";

       
       
            pst = new LoggableStatement(con, boeQuery);

            pst.setString(1, boevo.getBoeNumber());

            pst.setString(2, boevo.getBoeDate());

            pst.setString(3, boevo.getPortCode());

            rs = pst.executeQuery();

            if (rs.next()) {

              boeCount = rs.getInt("BOE_COUNT");

            }

            if (boeCount > 0)

            {

              String query = "SELECT COUNT(*) AS MBE_COUNT FROM ETT_MANUAL_BOE_ACK WHERE BILLOFENTRYNUMBER = ?  AND BILLOFENTRYDATE = ? AND PORTOFDISCHARGE = ?  AND UPPER(ERRORCODE) = 'SUCCESS' ";

       
       
              LoggableStatement pst1 = new LoggableStatement(con, query);

              pst1.setString(1, boevo.getBoeNumber());

              pst1.setString(2, boevo.getBoeDate());

              pst1.setString(3, boevo.getPortCode());

              ResultSet rs1 = pst1.executeQuery();

              if (rs1.next()) {

                mbeCount = rs1.getInt("MBE_COUNT");

              }

              closeStatementResultSet(pst1, rs1);

            }

            else

            {

              mbeCount = 1;

            }

          }

          catch (Exception e)

          {

            throwDAOException(e);

          }

          finally

          {

            closeSqlRefferance(rs, pst, con);

          }

          logger.info("Exiting Method");

          return mbeCount;

        }

        public int getBoeClCount(BoeVO boevo)

          throws DAOException

        {

          logger.info("Entering Method");

          Connection con = null;

          LoggableStatement pst = null;

          ResultSet rs = null;

          int boeClCount = 0;

          try

          {

            con = DBConnectionUtility.getConnection();

            String query = "SELECT COUNT(*) AS CL_COUNT FROM ETT_BOE_CLOSURE_DATA WHERE BOENUMBER = ?  AND TO_CHAR(BOEDATE,'DD/MM/YYYY') = ? AND PORTOFDISCHARGE = ? AND STATUS = 'P' ";

       
            pst = new LoggableStatement(con, query);
            pst.setString(1, boevo.getBoeNumber());
            pst.setString(2, boevo.getBoeDate());
            pst.setString(3, boevo.getPortCode());
            rs = pst.executeQuery();

            if (rs.next()) {

              boeClCount = rs.getInt("CL_COUNT");

            }

          }

          catch (Exception e)

          {

            throwDAOException(e);

          }

          finally

          {

            closeSqlRefferance(rs, pst, con);

          }

          logger.info("Exiting Method");

          return boeClCount;

        }

        public ArrayList<BoeVO> fetchDischargePortList(BoeVO boeVO1)

          throws DAOException

        {

          logger.info("Entering Method");

          String sQuery = "";

          Connection con = null;

          LoggableStatement pst = null;

          ResultSet rs = null;

          ArrayList<BoeVO> portList1 = null;

          CommonMethods commonMethods = null;

          try

          {

            commonMethods = new CommonMethods();

            portList1 = new ArrayList();

            con = DBConnectionUtility.getConnection();

            sQuery = "SELECT PCODE, PNAME, COUNTRY FROM EXTPORTCO WHERE COUNTRY = 'IN' ";

            if (!commonMethods.isNull(boeVO1.getPortId())) {

              sQuery = sQuery + " AND  PCODE LIKE '%" + boeVO1.getPortId() + "%' ";

            }

            pst = new LoggableStatement(con, sQuery);

            rs = pst.executeQuery();

            logger.info("The Query is : " + sQuery + " Country name is : " + boeVO1.getCountryName());

            while (rs.next())

            {

              boeVO1 = new BoeVO();

              boeVO1.setPortId(rs.getString("PCODE"));

              boeVO1.setPortLocation(rs.getString("PNAME"));

              boeVO1.setCountryName(rs.getString("COUNTRY"));

              portList1.add(boeVO1);

            }

          }

          catch (Exception e)

          {

            throwDAOException(e);

          }

          finally

          {

            closeSqlRefferance(rs, pst, con);

          }

          logger.info("Exiting Method");

          return portList1;

        }

        public ArrayList<InvoiceDetailsVO> fetchInovoiceList(BoeVO boevo)

        {

          logger.info("Entering Method");

          Connection con = null;

          ArrayList<InvoiceDetailsVO> invoiceList = null;

          try

          {

            invoiceList = new ArrayList();

            con = DBConnectionUtility.getConnection();

            boevo = fetchInvoiceList(con, boevo);

            if (boevo != null) {

              invoiceList = boevo.getInvoiceList();

            }

          }

          catch (Exception e)
          {
              e.printStackTrace();
            }
            finally
            {
              closeSqlRefferance(null, null, con);
            }
            logger.info("Exiting Method");
            return invoiceList;
          }
        }
      	 
    			 