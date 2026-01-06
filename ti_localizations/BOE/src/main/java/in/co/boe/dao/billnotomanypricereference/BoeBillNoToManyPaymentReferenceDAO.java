package in.co.boe.dao.billnotomanypricereference;

import in.co.boe.dao.AbstractDAO;
import in.co.boe.dao.exception.DAOException;
import in.co.boe.utility.ActionConstants;
import in.co.boe.utility.ActionConstantsQuery;
import in.co.boe.utility.CommonMethods;
import in.co.boe.utility.DBConnectionUtility;
import in.co.boe.utility.LoggableStatement;
import in.co.boe.utility.ValidationUtility;
import in.co.boe.vo.BOESearchVO;
import in.co.boe.vo.BoeVO;
import in.co.boe.vo.MeditorVO;
import in.co.boe.vo.TransactionVO;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
 
public class BoeBillNoToManyPaymentReferenceDAO
  extends AbstractDAO
  implements ActionConstantsQuery, ActionConstants
{
  private static Logger logger = Logger.getLogger(BoeBillNoToManyPaymentReferenceDAO.class.getName());
  static BoeBillNoToManyPaymentReferenceDAO dao;
  public static BoeBillNoToManyPaymentReferenceDAO getDAO()
  {
    if (dao == null) {
      dao = new BoeBillNoToManyPaymentReferenceDAO();
    }
    return dao;
  }
  public BoeVO retriveDataBasedOnBillNO(BoeVO boeVO)
    throws DAOException
  {
    logger.info("---------retriveDataBasedOnBillNO------------");
    Connection con = null;
    LoggableStatement pst = null;
    ResultSet rs = null;
    ArrayList<TransactionVO> invoiceList = null;
    ArrayList<MeditorVO> paymentList = null;
    ArrayList<TransactionVO> boePaymentList = null;
    CommonMethods commonMethods = null;
    try
    {
      con = DBConnectionUtility.getConnection();
      commonMethods = new CommonMethods();
      String query = "SELECT BOE_TYPE AS TRANS_TYPE,CIF AS CIF_ID,BOE_IE_NAME AS IE_NAME,BOE_IE_CODE AS IE_CODE,\tBOE_IE_ADDRESS AS BOE_IE_ADDRESS,BOE_IE_PANNUMBER AS BOE_IE_PANNUMBER,\tBOE_GP AS BOE_GP,BOE_IGMNUMBER AS IGMNO,TO_CHAR(BOE_IGMDATE,'DD/MM/YYYY') AS IGMDATE, BOE_HAWB_HBLNUMBER AS HBLNO,TO_CHAR(BOE_HAWB_HBLDATE,'DD/MM/YYYY') AS HBLDATE, BOE_MAWB_MBLNUMBER AS MBLNO,TO_CHAR(BOE_MAWB_MBLDATE,'DD/MM/YYYY') AS MBLDATE,\tBOE_RECORD_INDICATOR AS RECORDINDICATOR,BOE_PORT_OF_SHIPMENT AS PORTOFSHIPMENT, BOE_AD_CODE AS ADCODE,BOE_IMPORT_AGENCY AS IMPORTAGENCY  FROM ETT_BOE_DETAILS WHERE BOE_NUMBER = ? AND TO_CHAR(BOE_DATE,'DD/MM/YYYY') =? AND BOE_PORT_OF_DISCHARGE =? AND STATUS = 'A' ";

 
 
 
 
 
 
      LoggableStatement pst1 = new LoggableStatement(con, query);
      pst1.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
      pst1.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
      pst1.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
      ResultSet rs1 = pst1.executeQuery();
      if (!commonMethods.isNull(boeVO.getOkIdFlg())) {
        boeVO.setOkIdFlg(boeVO.getOkIdFlg());
      }
      if (rs1.next())
      {
        boeVO.setCifNo(commonMethods.getEmptyIfNull(rs1.getString("CIF_ID")).trim());
        boeVO.setCustName(commonMethods.getEmptyIfNull(rs1.getString("IE_NAME")).trim());
        boeVO.setIeCode(commonMethods.getEmptyIfNull(rs1.getString("IE_CODE")).trim());
        boeVO.setIeadd(commonMethods.getEmptyIfNull(rs1.getString("BOE_IE_ADDRESS")).trim());
        boeVO.setIepan(commonMethods.getEmptyIfNull(rs1.getString("BOE_IE_PANNUMBER")).trim());
        boeVO.setIgmNo(commonMethods.getEmptyIfNull(rs1.getString("IGMNO")).trim());
        boeVO.setIgmDate(commonMethods.getEmptyIfNull(rs1.getString("IGMDATE")).trim());
        boeVO.setHblNo(commonMethods.getEmptyIfNull(rs1.getString("HBLNO")).trim());
        boeVO.setHblDate(commonMethods.getEmptyIfNull(rs1.getString("HBLDATE")).trim());
        boeVO.setMblNo(commonMethods.getEmptyIfNull(rs1.getString("MBLNO")).trim());
        boeVO.setMblDate(commonMethods.getEmptyIfNull(rs1.getString("MBLDATE")).trim());
        boeVO.setPos(commonMethods.getEmptyIfNull(rs1.getString("PORTOFSHIPMENT")).trim());
        boeVO.setAdCode(commonMethods.getEmptyIfNull(rs1.getString("ADCODE")).trim());
        String gpType = commonMethods.getEmptyIfNull(rs1.getString("BOE_GP")).trim();
        if (gpType.equalsIgnoreCase("G")) {
          boeVO.setGovprv("Government");
        } else {
          boeVO.setGovprv("Private");
        }
        String recInd = commonMethods.getEmptyIfNull(rs1.getString("RECORDINDICATOR")).trim();
        if (recInd.equalsIgnoreCase("1")) {
          boeVO.setRecInd("New");
        } else if (recInd.equalsIgnoreCase("2")) {
          boeVO.setRecInd("Amendment");
        } else {
          boeVO.setRecInd("Cancel");
        }
        String imAgency = commonMethods.getEmptyIfNull(rs1.getString("IMPORTAGENCY")).trim();
        if (imAgency.equalsIgnoreCase("1")) {
          boeVO.setImAgency("Customs");
        } else {
          boeVO.setImAgency("SEZ");
        }
        String transType = commonMethods.getEmptyIfNull(rs1.getString("TRANS_TYPE")).trim();
        if (transType.equalsIgnoreCase("RBI")) {
          boeVO.setTransType("RBI");
        } else {
          boeVO.setTransType("MANUAL");
        }
      }
      closeSqlRefferance(rs1, pst1);

 
      String sqlQuery = "SELECT BP.CHANGED_IE_CODE AS CHANGED_IE_CODE, NVL(TRIM(BP.BOE_PAYMENT_BP_BOE_NO), ' ') AS BOE_PAYMENT_BP_BOE_NO,\tTO_CHAR(TO_DATE(BP.BOE_PAYMENT_BP_BOE_DT, 'dd-mm-yy'),'dd/mm/yyyy') AS BOE_PAYMENT_BP_BOE_DT, NVL(TRIM(BP.BOE_ENTRY_AMT), '0') AS BOE_ENTRY_AMT, NVL(TRIM(BP.BOE_PAYMENT_BP_BOE_CCY), ' ') AS BOE_ENTRY_CURR, TRIM(BP.PORT_CODE) AS PORT_CODE, BP.STATUS,BP.REMARKS  FROM ETT_BOE_PAYMENT BP WHERE BP.BOE_PAYMENT_BP_BOE_NO = ? AND TO_CHAR(BP.BOE_PAYMENT_BP_BOE_DT,'DD/MM/YYYY') = ? AND BP.PORT_CODE = ? AND BP.STATUS != 'R' ORDER BY BP.CREATEDDATE DESC ";

 
 
 
 
 
      pst = new LoggableStatement(con, sqlQuery);
      pst.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
      pst.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
      pst.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
      logger.info("RetriveDataBasedOnBillNO For Many Bill: " + pst.getQueryString());
      rs = pst.executeQuery();
      boolean isAvailable = false;
      if (rs.next())
      {
        isAvailable = true;
        boeVO.setBillAmt(commonMethods.toDouble(rs.getString("BOE_ENTRY_AMT")));

 
        boeVO.setBillCurrency(commonMethods.getEmptyIfNull(rs.getString("BOE_ENTRY_CURR")).trim());

 
 
 
        boeVO = fetchTotalEndoreAmount(boeVO);
        double totalEndAmt = commonMethods.convertToDouble(boeVO.getTotalEndorseAmt());
        double boeAmt = commonMethods.convertToDouble(boeVO.getBillAmt());

 
        double availableAmt = boeAmt - totalEndAmt;
        BigDecimal availAmt = BigDecimal.valueOf(availableAmt).setScale(2, RoundingMode.HALF_UP);
        logger.info("availAmt--ACT END AMT------------>" + availAmt);
        boeVO.setActualEndorseAmt(String.valueOf(availAmt));
        boeVO.setActualEndorseAmt_temp(String.valueOf(availAmt));

 
 
        boeVO.setRemarks(commonMethods.getEmptyIfNull(rs.getString("REMARKS")).trim());
        boeVO.setIeCodeChange(commonMethods.getEmptyIfNull(rs.getString("CHANGED_IE_CODE")).trim());
      }
      if (!isAvailable)
      {
        logger.info("Inside..");
        boeVO = getTotalBOEAmt(con, boeVO);
      }
      int totalBoeCount = 0;
      String boeCountQuery = "SELECT SUM(BOE_COUNT) AS B_COUNT FROM (SELECT COUNT(*) AS BOE_COUNT FROM ETT_BOE_CLOSURE_DATA  WHERE BOENUMBER = ? AND TO_CHAR(BOEDATE,'DD/MM/YYYY') = ? AND PORTOFDISCHARGE = ? AND STATUS != 'R' UNION SELECT COUNT(*) AS BOE_COUNT FROM ETT_BOE_PAYMENT WHERE BOE_PAYMENT_BP_BOE_NO = ?  AND TO_CHAR(BOE_PAYMENT_BP_BOE_DT,'DD/MM/YYYY') = ? AND PORT_CODE = ? AND STATUS != 'R') ";

 
 
      LoggableStatement pst3 = new LoggableStatement(con, boeCountQuery);
      pst3.setString(1, boeVO.getBoeNo());
      pst3.setString(2, boeVO.getBoeDate());
      pst3.setString(3, boeVO.getPortCode());
      pst3.setString(4, boeVO.getBoeNo());
      pst3.setString(5, boeVO.getBoeDate());
      pst3.setString(6, boeVO.getPortCode());
      logger.info("-----------ETT_BOE_CLOSURE_DATA----------Query------------" + pst3.getQueryString());
      ResultSet rs3 = pst3.executeQuery();
      if (rs3.next())
      {
        totalBoeCount = rs3.getInt("B_COUNT");
        logger.info("-----------ETT_BOE_CLOSURE_DATA----------count-------" + totalBoeCount);
      }
      closeSqlRefferance(rs3, pst3);
      if (totalBoeCount > 0) {
        boeVO.setManualPartialData("Y");
      } else {
        boeVO.setManualPartialData("N");
      }
      invoiceList = fetchInvoiceList(con, boeVO);
      if ((invoiceList.size() > 0) && (!invoiceList.isEmpty())) {
        boeVO.setInvoiceList(invoiceList);
      }
      paymentList = fetchDataMultiPayments(con, boeVO);
      if ((paymentList.size() > 0) && (!paymentList.isEmpty())) {
        boeVO.setPaymentList(paymentList);
      }
      boePaymentList = fetchBOEPaymentList(con, boeVO);
      if ((boePaymentList.size() > 0) && (!boePaymentList.isEmpty())) {
        boeVO.setBoePaymentList(boePaymentList);
      }
    }
    catch (Exception e)
    {
      logger.info("---------retriveDataBasedOnBillNO---------excepion---" + e);
      e.printStackTrace();
    }
    finally
    {
      closeSqlRefferance(rs, pst, con);
    }
    logger.info("Exiting Method");
    return boeVO;
  }
  
  public BoeVO fetchTotalEndoreAmount(BoeVO boeVO)
		    throws DAOException
		  {
		    logger.info("Entering Method");
		    Connection con = null;
		    LoggableStatement pst = null;
		    ResultSet rs = null;
		    CommonMethods commonMethods = null;
		    try
		    {
		      commonMethods = new CommonMethods();
		      con = DBConnectionUtility.getConnection();
		      double realAmtIc = 0.0D;
		      String sqlQuery = "SELECT ROUND(SUM(ENDORSED_BOE_AMT),2) AS TOTAL_EDS_AMT  FROM ETT_BOE_PAYMENT WHERE BOE_PAYMENT_BP_BOE_NO = ? AND TO_CHAR(BOE_PAYMENT_BP_BOE_DT,'DD/MM/YYYY') = ? AND PORT_CODE = ?  AND STATUS != 'R' ORDER BY CREATEDDATE DESC ";

		 
		 
		      pst = new LoggableStatement(con, sqlQuery);
		      pst.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
		      pst.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
		      pst.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
		      logger.info("FetchTotalEndoreAmount For Many Bills: " + pst.getQueryString());
		      rs = pst.executeQuery();
		      if (rs.next()) {
		        realAmtIc = commonMethods.convertToDouble(rs.getString("TOTAL_EDS_AMT"));
		      }
		      double adjAmtIc = 0.0D;
		      String closureQuery = "SELECT SUM(ADJ_INVAMT) AS CLOSURE_AMTIC FROM ETT_BOE_INV_CLOSURE  WHERE BOE_NO = ? AND TO_CHAR(BOE_DATE,'DD/MM/YYYY') = ? AND PORTCODE = ? AND STATUS != 'R' ";

		 
		 
		      LoggableStatement pst2 = new LoggableStatement(con, closureQuery);
		      pst2.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
		      pst2.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
		      pst2.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
		      ResultSet rs2 = pst2.executeQuery();
		      if (rs2.next()) {
		        adjAmtIc = commonMethods.convertToDouble(rs2.getString("CLOSURE_AMTIC"));
		      }
		      closeSqlRefferance(rs2, pst2);
		      double bttRealizedAmtIc = 0.0D;
		      String bttCheckQuery = "select SUM(REALIZEDINVOICEAMTIC) AS BTTREALIZEDAMT from ETT_BTT_ACK  WHERE BOENO = ? AND TO_CHAR(BOEDATE,'DD/MM/YYYY') = ? AND PORTCODE = ? ";

		 
		 
		      LoggableStatement pst3 = new LoggableStatement(con, bttCheckQuery);
		      pst3.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
		      pst3.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
		      pst3.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
		      ResultSet rs3 = pst3.executeQuery();
		      if (rs3.next()) {
		        bttRealizedAmtIc = commonMethods.convertToDouble(rs3.getString("BTTREALIZEDAMT"));
		      }
		      closeSqlRefferance(rs3, pst3);
		      double totalRealAmt = realAmtIc + adjAmtIc + bttRealizedAmtIc;
		      totalRealAmt = Math.round(totalRealAmt * 100.0D) / 100.0D;
		      String totalEndAmt = String.valueOf(totalRealAmt);
		      boeVO.setTotalEndorseAmt(totalEndAmt);
		      logger.info("Total Endorsement Amount------------>" + totalEndAmt);
		    }
		    catch (SQLException e)
		    {
		      throwDAOException(e);
		    }
		    finally
		    {
		      closeSqlRefferance(rs, pst, con);
		    }
		    logger.info("Exiting Method");
		    return boeVO;
		  }
		  public BoeVO getTotalBOEAmt(Connection con, BoeVO boeVO)
		  {
		    logger.info("--------------getTotalBOEAmt-----------");
		    LoggableStatement pst = null;
		    ResultSet rs = null;
		    BigDecimal totalAmt = null;
		    CommonMethods commonMethods = null;
		    try
		    {
		      if (con == null) {
		        con = DBConnectionUtility.getConnection();
		      }
		      commonMethods = new CommonMethods();

		 
		 
		 
		 
		 
		 
		      String query = "SELECT SUM(NVL(INVOICE_FOBAMOUNT,0)) AS TOT_BOE_AMT, INVOICE_FOBCURRENCY AS BOE_CURR FROM ETT_INVOICE_DETAILS WHERE BOE_NUMBER = ? AND TO_CHAR(BOE_DATE,'DD/MM/YYYY') = ? AND BOE_PORT_OF_DISCHARGE = ? GROUP BY INVOICE_FOBCURRENCY";

		 
		 
		      pst = new LoggableStatement(con, query);

		 
		      pst.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
		      pst.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
		      pst.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
		      rs = pst.executeQuery();
		      logger.info("--------------getTotalBOEAmt------query------" + pst.getQueryString());
		      double totalBoeAmt = 0.0D;
		      String boeCurr = null;
		      if (rs.next())
		      {
		        totalBoeAmt = rs.getDouble("TOT_BOE_AMT");
		        boeCurr = rs.getString("BOE_CURR");
		      }
		      double adjAmtIc = 0.0D;
		      String sqlQuery = "SELECT SUM(ADJ_INVAMT) AS CLOSURE_AMTIC FROM ETT_BOE_INV_CLOSURE  WHERE BOE_NO = ? AND TO_CHAR(BOE_DATE,'DD/MM/YYYY') = ? AND PORTCODE = ? AND STATUS != 'R' ";

		 
		 
		      LoggableStatement pst2 = new LoggableStatement(con, sqlQuery);
		      pst2.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
		      pst2.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
		      pst2.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
		      logger.info("--------------getTotalBOEAmt------query-11111111111-----" + pst2.getQueryString());
		      ResultSet rs2 = pst2.executeQuery();
		      if (rs2.next()) {
		        adjAmtIc = commonMethods.convertToDouble(rs2.getString("CLOSURE_AMTIC"));
		      }
		      closeSqlRefferance(rs2, pst2);
		      double availableAmt = totalBoeAmt - adjAmtIc;
		      BigDecimal availAmt = BigDecimal.valueOf(availableAmt).setScale(2, RoundingMode.HALF_UP);
		      totalAmt = new BigDecimal(totalBoeAmt);
		      totalAmt = totalAmt.setScale(2, RoundingMode.HALF_UP);
		      boeVO.setBillAmt(String.valueOf(totalAmt));
		      boeVO.setActualEndorseAmt(String.valueOf(availAmt));

		 
		      boeVO.setActualEndorseAmt_temp(String.valueOf(availAmt));
		      boeVO.setBillCurrency(boeCurr);
		    }
		    catch (Exception e)
		    {
		      logger.info("--------------getTotalBOEAmt------exception------" + e);
		      e.printStackTrace();
		    }
		    finally
		    {
		      closeSqlRefferance(rs, pst);
		    }
		    logger.info("Exiting Method");
		    return boeVO;
		  }
		  
		  public ArrayList<TransactionVO> fetchInvoiceList(Connection con, BoeVO boeVO)
		  {
		    logger.info("----------fetchInvoiceList---------");
		    LoggableStatement pst = null;
		    ResultSet rs = null;
		    CommonMethods commonMethods = null;
		    ArrayList<TransactionVO> invoiceList = null;
		    try
		    {
		      if (con == null) {
		        con = DBConnectionUtility.getConnection();
		      }
		      commonMethods = new CommonMethods();
		      invoiceList = new ArrayList();
		      double totalEndorseAmt = 0.0D;
		      String query = "SELECT INV_SERIAL_NO,INV_NO,INV_TERMS,FOB_AMT,FOB_CURR,FRI_AMT,FRI_CURR, INS_AMT,INS_CURR,REAL_AMT,REAL_AMTIC AS REAL_ORM_AMT,CLOSURE_AMTIC FROM ETTV_BOE_INV_DETAILS WHERE BOE_NUMBER = ? AND TO_CHAR(BOE_DATE,'DD/MM/YYYY') = ? AND BOE_PORT_OF_DISCHARGE = ? ORDER BY TO_NUMBER(INV_SERIAL_NO) ";

		 
		 
		      pst = new LoggableStatement(con, query);
		      pst.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
		      pst.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
		      pst.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
		      logger.info("Invoice Query --> " + 
		        pst.getQueryString());
		      rs = pst.executeQuery();
		      while (rs.next())
		      {
		        TransactionVO invoiceVO = new TransactionVO();
		        invoiceVO.setInvoiceSerialNumber(commonMethods.getEmptyIfNull(rs.getString("INV_SERIAL_NO")).trim());
		        invoiceVO.setInvoiceNumber(commonMethods.getEmptyIfNull(rs.getString("INV_NO")).trim());
		        invoiceVO.setTermsofInvoice(commonMethods.getEmptyIfNull(rs.getString("INV_TERMS")).trim());
		        invoiceVO.setInvoiceAmount(commonMethods.getEmptyIfNull(rs.getString("FOB_AMT")).trim());
		        invoiceVO.setInvoiceCurr(commonMethods.getEmptyIfNull(rs.getString("FOB_CURR")).trim());
		        invoiceVO.setFreightAmount(commonMethods.getEmptyIfNull(rs.getString("FRI_AMT")).trim());
		        invoiceVO.setFreightCurrencyCode(commonMethods.getEmptyIfNull(rs.getString("FRI_CURR")).trim());
		        invoiceVO.setInsuranceAmount(commonMethods.getEmptyIfNull(rs.getString("INS_AMT")).trim());
		        invoiceVO.setInsuranceCurrencyCode(commonMethods.getEmptyIfNull(rs.getString("INS_CURR")).trim());
		        invoiceVO.setAlreadyRealizedAmt(commonMethods.getEmptyIfNull(rs.getString("REAL_AMT")).trim());
		        invoiceVO.setAlreadyRealizedAmtIC(commonMethods.getEmptyIfNull(rs.getString("REAL_ORM_AMT")).trim());
		        String tempVal = commonMethods.getEmptyIfNull(rs.getString("INV_SERIAL_NO")).trim() + 
		          ":" + commonMethods.getEmptyIfNull(rs.getString("INV_NO")).trim();
		        invoiceVO.setUtilityRefNo(tempVal);
		        double fobAmt = commonMethods.convertToDouble(rs.getString("FOB_AMT"));
		        double friAmt = commonMethods.convertToDouble(rs.getString("FRI_AMT"));
		        double insAmt = commonMethods.convertToDouble(rs.getString("INS_AMT"));

		 
		 
		        double totalAmt = fobAmt;
		        double totalInvAmt = Math.round(totalAmt * 100.0D) / 100.0D;
		        BigDecimal toInvAmt = BigDecimal.valueOf(totalInvAmt).setScale(2, RoundingMode.HALF_UP);
		        invoiceVO.setTotalInvAmt(String.valueOf(toInvAmt));

		 
		        double realAmtIC = commonMethods.convertToDouble(rs.getString("REAL_ORM_AMT"));
		        double closureAmtIc = commonMethods.convertToDouble(rs.getString("CLOSURE_AMTIC"));
		        invoiceVO.setClosureAmt(String.valueOf(closureAmtIc));
		        double totalPaidAmt = realAmtIC + closureAmtIc;
		        totalPaidAmt = Math.round(totalPaidAmt * 100.0D) / 100.0D;
		        double outAmtIC = totalInvAmt - totalPaidAmt;
		        outAmtIC = Math.round(outAmtIC * 100.0D) / 100.0D;
		        BigDecimal toOutAmt = BigDecimal.valueOf(outAmtIC).setScale(2, RoundingMode.HALF_UP);
		        invoiceVO.setOutAmtIC(String.valueOf(toOutAmt));
		        double endorseAmt = commonMethods.convertToDouble(rs.getString("REAL_ORM_AMT"));
		        totalEndorseAmt += endorseAmt;
		        totalEndorseAmt = Math.round(totalEndorseAmt * 100.0D) / 100.0D;
		        invoiceList.add(invoiceVO);
		      }
		      BigDecimal toEndAmt = BigDecimal.valueOf(totalEndorseAmt).setScale(2, RoundingMode.HALF_UP);
		      boeVO.setEndorseAmt(String.valueOf(toEndAmt));
		    }
		    catch (Exception e)
		    {
		      logger.info("----------fetchInvoiceList------exception---" + e);
		      e.printStackTrace();
		    }
		    finally
		    {
		      closeSqlRefferance(rs, pst);
		    }
		    logger.info("Exiting Method");
		    return invoiceList;
		  }
		  
		  public ArrayList<MeditorVO> fetchDataMultiPayments(Connection con, BoeVO boeVO)
				    throws DAOException
				  {
				    logger.info("-----fetchDataMultiPayments------------");
				    logger.info("-----fetchDataMultiPayments------------");
				    logger.info("Entering Method");
				    String sqlQuery = null;
				    String sPreEndorsedAmt = null;
				    LoggableStatement pst = null;
				    ResultSet rs = null;
				    ArrayList<MeditorVO> paymentList = null;
				    CommonMethods commonMethods = null;
				    try
				    {
				      if (con == null) {
				        con = DBConnectionUtility.getConnection();
				      }
				      paymentList = new ArrayList();
				      commonMethods = new CommonMethods();
				      logger.info("fetchDataMultiPayments-------boeVO.getCifNo()" + boeVO.getCifNo());
				      logger.info("fetchDataMultiPayments-------boeVO.getBillCurrency()" + boeVO.getBillCurrency());
				      if ((boeVO.getBtnModify() != null) && (!boeVO.getBtnModify().equalsIgnoreCase("M")))
				      {
				        sqlQuery = "SELECT DISTINCT NVL(TRIM(PD.PD_CIF_NAME), ' ')  AS PD_CIF_NAME, NVL(TRIM(PD.PD_IE_CODE), ' ') AS PD_IE_CODE,   NVL(TRIM(PD.PD_TXN_REF), ' ') AS PD_TXN_REF,  NVL(TRIM(PD.PD_PART_PAY_REF), ' ')  AS PD_PART_PAY_REF, TO_CHAR(TO_DATE(PD.PD_TXN_DT, 'dd-mm-yy'),'dd-mm-yyyy') AS PD_TXN_DT,NVL(TRIM(PD.PD_TXN_CCY), ' ') AS PD_TXN_CCY,NVL(TRIM(PD.PD_FC_AMT), '0')  AS PD_FC_AMT,NVL(TRIM(PD.PD_ENDORSED_AMT), '0')  AS PD_ENDORSED_AMT, NVL(TRIM(BENEF_NAME), '')  AS BENEF_NAME,NVL(TRIM(BENEF_COUNTRY), '') AS BENEF_COUNTRY,PD.PD_FC_AMT*ETT_BOE_EX_RATE_CAL(NVL(TRIM(PD.PD_TXN_CCY),'USD'),?) AS PD_OUTSTANDING_AMT, (SELECT ETT_BOE_EX_RATE_CAL(NVL(TRIM(PD.PD_TXN_CCY),'USD'),?) FROM DUAL ) AS EXCHANGE_RATE FROM ETTV_BOE_PAYMENT_DETAILS PD WHERE PD.PD_CIF_ID = ? and PD.EVENT_STATUS='c' ";
				        pst = new LoggableStatement(con, sqlQuery);
				        pst.setString(1, commonMethods.getEmptyIfNull(boeVO.getBillCurrency()).trim());
				        pst.setString(2, commonMethods.getEmptyIfNull(boeVO.getBillCurrency()).trim());
				        pst.setString(3, commonMethods.getEmptyIfNull(boeVO.getCifNo()).trim());
				        logger.info("BOE_FETCH_PAYMENT_DETAILS Query------------------>" + pst.getQueryString());
				      }
				      if ((boeVO.getBtnModify() != null) && (boeVO.getBtnModify().equalsIgnoreCase("M")))
				      {
				        sqlQuery = "SELECT NVL(TRIM(PD.PD_CIF_NAME), ' ') AS PD_CIF_NAME, NVL(TRIM(PD.PD_IE_CODE), ' ') AS PD_IE_CODE, NVL(TRIM(PD.PD_TXN_REF), ' ') AS PD_TXN_REF, NVL(TRIM(PD.PD_PART_PAY_REF), ' ')  AS PD_PART_PAY_REF,   TO_CHAR(TO_DATE(PD.PD_TXN_DT, 'dd-mm-yy'),'dd-mm-yyyy') AS PD_TXN_DT, NVL(TRIM(PD.PD_TXN_CCY), ' ') AS PD_TXN_CCY, NVL(TRIM(PD.PD_FC_AMT), '0') AS PD_FC_AMT, NVL(TRIM(PD.PD_ENDORSED_AMT), '0') AS PD_ENDORSED_AMT, NVL(TRIM(BENEF_NAME), '') AS BENEF_NAME, NVL(TRIM(BENEF_COUNTRY), '') AS BENEF_COUNTRY, NVL(TRIM(PD.PD_OUTSTANDING_AMT), '0') AS PD_OUTSTANDING_AMT, (SELECT ETT_BOE_EX_RATE_CAL(NVL(TRIM(PD.PD_TXN_CCY),'USD'),?) FROM DUAL  ) AS EXCHANGE_RATE, PD.PRE_ENDORSED_AMT  AS PRE_ENDORSED_AMT FROM ETTV_BOE_PAY_DETAILS PD WHERE PD.PD_CIF_ID = ? UNION SELECT NVL(TRIM(PD.PD_CIF_NAME), ' ')AS PD_CIF_NAME, NVL(TRIM(PD.PD_IE_CODE), ' ') AS PD_IE_CODE, NVL(TRIM(PD.PD_TXN_REF), ' ') AS PD_TXN_REF,  NVL(TRIM(PD.PD_PART_PAY_REF), ' ') AS PD_PART_PAY_REF, TO_CHAR(TO_DATE(PD.PD_TXN_DT, 'dd-mm-yy'),'dd-mm-yyyy') AS PD_TXN_DT, NVL(TRIM(PD.PD_TXN_CCY), ' ') AS PD_TXN_CCY, NVL(TRIM(PD.PD_FC_AMT), '0') AS PD_FC_AMT, NVL(TRIM(PD.PD_ENDORSED_AMT), '0') AS PD_ENDORSED_AMT,  NVL(TRIM(BENEF_NAME), '') AS BENEF_NAME,  NVL(TRIM(BENEF_COUNTRY), '') AS BENEF_COUNTRY, NVL(TRIM(PD.PD_OUTSTANDING_AMT), '0') AS PD_OUTSTANDING_AMT,(SELECT ETT_BOE_EX_RATE_CAL(NVL(TRIM(PD.PD_TXN_CCY),'USD'),?) FROM DUAL) AS EXCHANGE_RATE, PD.PRE_ENDORSED_AMT AS PRE_ENDORSED_AMT FROM ETTV_TI_BOE_PAY_DETAILS PD WHERE PD.PD_CIF_ID = ? AND PD.PD_TXN_REF NOT IN (SELECT NVL(TRIM(PD.PD_TXN_REF), ' ') FROM ETTV_BOE_PAY_DETAILS PD WHERE PD.PD_CIF_ID = ?)";
				        pst = new LoggableStatement(con, sqlQuery);
				        pst.setString(1, commonMethods.getEmptyIfNull(boeVO.getBillCurrency()).trim());
				        pst.setString(2, commonMethods.getEmptyIfNull(boeVO.getCifNo()).trim());
				        pst.setString(3, commonMethods.getEmptyIfNull(boeVO.getBillCurrency()).trim());
				        pst.setString(4, commonMethods.getEmptyIfNull(boeVO.getCifNo()).trim());
				        pst.setString(5, commonMethods.getEmptyIfNull(boeVO.getCifNo()).trim());
				        logger.info("Multi Payments Query --> " + pst.getQueryString());
				      }
				      if (pst != null)
				      {
				        rs = pst.executeQuery();
				        while (rs.next())
				        {
				          MeditorVO paymentVO = new MeditorVO();
				          String datevalid = "01-09-2022";
				          String txnDate1 = rs.getString("PD_TXN_DT").trim();
				          SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
				          SimpleDateFormat sdf1 = null;
				          if (txnDate1.length() == 10) {
				            sdf1 = new SimpleDateFormat("dd-MM-yyyy");
				          } else {
				            sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				          }
				          Date staticDate = sdf.parse(datevalid);
				          Date txnDate = sdf1.parse(txnDate1);
				          if (txnDate.before(staticDate))
				          {
				            paymentVO.setPaymentRefNo(commonMethods.getEmptyIfNull(rs.getString("PD_TXN_REF")).trim());
				            paymentVO.setPartPaymentSlNo(commonMethods.getEmptyIfNull(rs.getString("PD_PART_PAY_REF").trim()));
				            paymentVO.setPaymentDate(commonMethods.getEmptyIfNull(rs.getString("PD_TXN_DT")).trim());
				            paymentVO.setPaymentCurr(commonMethods.getEmptyIfNull(rs.getString("PD_TXN_CCY")).trim());
				            paymentVO.setPaymentAmount(commonMethods.getEmptyIfNull(rs.getString("PD_FC_AMT")).trim());
				            paymentVO.setEndorseAmt(commonMethods.getEmptyIfNull(rs.getString("PD_ENDORSED_AMT")).trim());
				            paymentVO.setEndorseAmt_temp(commonMethods.getEmptyIfNull(rs.getString("PD_ENDORSED_AMT")).trim());
				            paymentVO.setBenefName(commonMethods.getEmptyIfNull(rs.getString("BENEF_NAME")).trim());
				            paymentVO.setBenefCountry(commonMethods.getEmptyIfNull(rs.getString("BENEF_COUNTRY")).trim());
				            paymentVO.setExRate(commonMethods.getEmptyIfNull(rs.getString("EXCHANGE_RATE")).trim());
				            paymentVO.setExRateTemp(commonMethods.getEmptyIfNull(rs.getString("EXCHANGE_RATE")).trim());
				            if ((boeVO.getBtnModify() != null) && (boeVO.getBtnModify().equalsIgnoreCase("M")))
				            {
				              sPreEndorsedAmt = commonMethods.getEmptyIfNull(rs.getString("PRE_ENDORSED_AMT")).trim();
				              paymentVO.setAllocAmt(sPreEndorsedAmt);
				              double exRate = commonMethods.convertToDouble(rs.getString("EXCHANGE_RATE"));
				              double dBoeAllocAmt = commonMethods.convertToDouble(sPreEndorsedAmt) * exRate;

				 
				 
				              paymentVO.setBoeAllocAmt(dBoeAllocAmt);
				            }
				            String tempVal = commonMethods.getEmptyIfNull(rs.getString("PD_TXN_REF")).trim() + 
				              ":" + commonMethods.getEmptyIfNull(rs.getString("PD_PART_PAY_REF")).trim();
				            paymentVO.setUtilityTransNo(tempVal);
				            double ormClosureAmt = getORMClosureAmount(con, paymentVO);
				            double billOutAmt = commonMethods.convertToDouble(rs.getString("PD_OUTSTANDING_AMT"));
				            double remainAmt = billOutAmt - ormClosureAmt;
				            BigDecimal outAmt = BigDecimal.valueOf(remainAmt).setScale(2, RoundingMode.HALF_UP);
				            if (remainAmt == 0.0D) {
				              paymentVO.setFullyAlloc("Y");
				            } else {
				              paymentVO.setFullyAlloc("N");
				            }
				            paymentVO.setOutstandingAmt(String.valueOf(outAmt));
				            paymentVO.setOutstandingAmt_temp(String.valueOf(outAmt));
				            if (!paymentVO.getFullyAlloc().equalsIgnoreCase("Y")) {
				              paymentList.add(paymentVO);
				            }
				          }
				          
				          else
				          {
				            LoggableStatement lst = null;
				            ResultSet rs1 = null;
				            String ormref = null;
				            try
				            {
				              String sqlQuery1 = "SELECT DISTINCT OUTWARDREFERNECNO AS OUTWARDREFERNECNO FROM ETT_ORM_ACK WHERE OUTWARDREFERNECNO= ? ";
				              ormref = commonMethods.getEmptyIfNull(rs.getString("PD_TXN_REF")).trim() + commonMethods.getEmptyIfNull(rs.getString("PD_PART_PAY_REF").trim());
				              lst = new LoggableStatement(con, sqlQuery1);
				              lst.setString(1, ormref);
				              rs1 = lst.executeQuery();
				              while (rs1.next())
				              {
				                paymentVO.setPaymentRefNo(commonMethods.getEmptyIfNull(rs.getString("PD_TXN_REF")).trim());
				                paymentVO.setPartPaymentSlNo(commonMethods.getEmptyIfNull(rs.getString("PD_PART_PAY_REF").trim()));
				                paymentVO.setPaymentDate(commonMethods.getEmptyIfNull(rs.getString("PD_TXN_DT")).trim());
				                paymentVO.setPaymentCurr(commonMethods.getEmptyIfNull(rs.getString("PD_TXN_CCY")).trim());
				                paymentVO.setPaymentAmount(commonMethods.getEmptyIfNull(rs.getString("PD_FC_AMT")).trim());
				                paymentVO.setEndorseAmt(commonMethods.getEmptyIfNull(rs.getString("PD_ENDORSED_AMT")).trim());
				                paymentVO.setEndorseAmt_temp(commonMethods.getEmptyIfNull(rs.getString("PD_ENDORSED_AMT")).trim());
				                paymentVO.setBenefName(commonMethods.getEmptyIfNull(rs.getString("BENEF_NAME")).trim());
				                paymentVO.setBenefCountry(commonMethods.getEmptyIfNull(rs.getString("BENEF_COUNTRY")).trim());
				                paymentVO.setExRate(commonMethods.getEmptyIfNull(rs.getString("EXCHANGE_RATE")).trim());
				                paymentVO.setExRateTemp(commonMethods.getEmptyIfNull(rs.getString("EXCHANGE_RATE")).trim());
				                if ((boeVO.getBtnModify() != null) && (boeVO.getBtnModify().equalsIgnoreCase("M")))
				                {
				                  sPreEndorsedAmt = commonMethods.getEmptyIfNull(rs.getString("PRE_ENDORSED_AMT")).trim();
				                  paymentVO.setAllocAmt(sPreEndorsedAmt);
				                  double exRate = commonMethods.convertToDouble(rs.getString("EXCHANGE_RATE"));
				                  double dBoeAllocAmt = commonMethods.convertToDouble(sPreEndorsedAmt) * exRate;

				 
				 
				                  paymentVO.setBoeAllocAmt(dBoeAllocAmt);
				                }
				                String tempVal = commonMethods.getEmptyIfNull(rs.getString("PD_TXN_REF")).trim() + 
				                  ":" + commonMethods.getEmptyIfNull(rs.getString("PD_PART_PAY_REF")).trim();
				                paymentVO.setUtilityTransNo(tempVal);
				                double ormClosureAmt = getORMClosureAmount(con, paymentVO);
				                double billOutAmt = commonMethods.convertToDouble(rs.getString("PD_OUTSTANDING_AMT"));
				                double remainAmt = billOutAmt - ormClosureAmt;
				                BigDecimal outAmt = BigDecimal.valueOf(remainAmt).setScale(2, RoundingMode.HALF_UP);
				                if (remainAmt == 0.0D) {
				                  paymentVO.setFullyAlloc("Y");
				                } else {
				                  paymentVO.setFullyAlloc("N");
				                }
				                paymentVO.setOutstandingAmt(String.valueOf(outAmt));
				                paymentVO.setOutstandingAmt_temp(String.valueOf(outAmt));
				                if (!paymentVO.getFullyAlloc().equalsIgnoreCase("Y")) {
				                  paymentList.add(paymentVO);
				                }
				              }
				            }
				            catch (SQLException se)
				            {
				              se.printStackTrace();
				            }
				            finally
				            {
				              DBConnectionUtility.surrenderDB(null, lst, rs1);
				            }
				          }
				        }
				      }
				    }
				    catch (Exception e)
				    {
				      e.printStackTrace();
				      logger.info("Exception is " + e.getMessage());
				    }
				    finally
				    {
				      closeSqlRefferance(rs, pst);
				    }
				    logger.info("Exiting Method");
				    return paymentList;
				  }
				  public double getORMClosureAmount(Connection con, MeditorVO paymentVO)
				    throws DAOException
				  {
				    logger.info("Entering Method");
				    LoggableStatement pst = null;
				    ResultSet rs = null;
				    String sqlQuery = null;
				    CommonMethods commonMethods = null;
				    double ormClosureAmt = 0.0D;
				    try
				    {
				      commonMethods = new CommonMethods();
				      if (con == null) {
				        con = DBConnectionUtility.getConnection();
				      }
				      String outwardReferenceNo = commonMethods.getEmptyIfNull(paymentVO.getPaymentRefNo()).trim() + 
				        commonMethods.getEmptyIfNull(paymentVO.getPartPaymentSlNo()).trim();
				      sqlQuery = "SELECT NVL(SUM(TCLOSAMT),'0') AS TCLOSAMT FROM ETT_IDPMS_DATA_EC WHERE TOUTWARDREFERENCENUMBER = ? AND TSTATUS != 'R' ";
				      pst = new LoggableStatement(con, sqlQuery);
				      pst.setString(1, outwardReferenceNo);
				      logger.info("Fetch ORM Closure Amount : " + pst.getQueryString());
				      rs = pst.executeQuery();
				      if (rs.next()) {
				        ormClosureAmt = commonMethods.convertToDouble(rs.getString("TCLOSAMT"));
				      }
				    }
				    catch (SQLException e)
				    {
				      throwDAOException(e);
				    }
				    finally
				    {
				      closeSqlRefferance(rs, pst);
				    }
				    logger.info("Exiting Method");
				    return ormClosureAmt;
				  }
				  
				  public ArrayList<TransactionVO> fetchBOEPaymentList(Connection con, BoeVO boeVO)
						    throws DAOException
						  {
						    logger.info("This is Inside of fetchBOEPaymentList() ===================>>> ");
						    logger.info("fetchBOEPaymentList");
						    logger.info("Entering Method");
						    LoggableStatement pst = null;
						    ResultSet rs = null;
						    ArrayList<TransactionVO> boePaymentList = null;
						    CommonMethods commonMethods = null;
						    try
						    {
						      if (con == null) {
						        con = DBConnectionUtility.getConnection();
						      }
						      boePaymentList = new ArrayList();
						      commonMethods = new CommonMethods();
						      String sqlQuery = "SELECT NVL(TRIM(BP.BOE_PAYMENT_BP_PAY_REF), ' ') AS BOE_PAYMENT_BP_PAY_REF,  NVL(TRIM(BP.BOE_PAYMENT_BP_PAY_PART_REF), ' ') AS BOE_PAYMENT_BP_PAY_PART_REF, NVL(TRIM(BP.BOE_PAYMENT_BP_BOE_NO), ' ') AS BOE_PAYMENT_BP_BOE_NO,  TO_CHAR(TO_DATE(BP.BOE_PAYMENT_BP_BOE_DT, 'DD-MM-YY'),'DD/MM/YYYY') AS BOE_PAYMENT_BP_BOE_DT, TRIM(BP.PORT_CODE) AS PORTCODE,NVL(TRIM(BP.BOE_PAYMENT_BP_PAY_FC_AMT),'0') AS BOE_PAYMENT_BP_PAY_FC_AMT, TRIM(BP.BOE_PAYMENT_BP_PAY_CCY) AS BOE_PAYMENT_BP_PAY_CCY, NVL(TRIM(BP.BOE_PAYMENT_BP_PAY_ENDORSE_AMT), '0') AS BOE_PAYMENT_BP_PAY_ENDORSE_AMT  FROM ETT_BOE_PAYMENT BP WHERE BP.BOE_PAYMENT_BP_BOE_NO = ?  AND TO_CHAR(BP.BOE_PAYMENT_BP_BOE_DT,'DD/MM/YYYY') = ? AND BP.PORT_CODE = ? ";
						      pst = new LoggableStatement(con, sqlQuery);
						      pst.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
						      pst.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
						      pst.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
						      logger.info("BOE Payments Query --> " + pst.getQueryString());
						      rs = pst.executeQuery();
						      while (rs.next())
						      {
						        TransactionVO transactionVO = new TransactionVO();
						        transactionVO.setPaymentRefNo(rs.getString("BOE_PAYMENT_BP_PAY_REF"));
						        transactionVO.setPartPaymentSlNo(rs.getString("BOE_PAYMENT_BP_PAY_PART_REF"));
						        transactionVO.setBoeNo(rs.getString("BOE_PAYMENT_BP_BOE_NO"));
						        transactionVO.setBoeDate(rs.getString("BOE_PAYMENT_BP_BOE_DT"));
						        transactionVO.setPortCode(rs.getString("PORTCODE"));
						        transactionVO.setBillAmt(rs.getString("BOE_PAYMENT_BP_PAY_FC_AMT"));
						        transactionVO.setBillCurrency(rs.getString("BOE_PAYMENT_BP_PAY_CCY"));
						        transactionVO.setActualEndorseAmt(CommonMethods.setCheckValue(rs.getString("BOE_PAYMENT_BP_PAY_ENDORSE_AMT")));
						        boePaymentList.add(transactionVO);
						      }
						    }
						    catch (Exception e)
						    {
						      e.printStackTrace();
						      logger.info("Exception is " + e.getMessage());
						    }
						    finally
						    {
						      closeSqlRefferance(rs, pst);
						    }
						    logger.info("Exiting Method");
						    return boePaymentList;
						  }
						  public BoeVO retriveBackManyBillData(BoeVO boeVO)
						    throws DAOException
						  {
						    logger.info("Entering Method");
						    Connection con = null;
						    LoggableStatement pst = null;
						    ResultSet rs = null;
						    ArrayList<TransactionVO> invoiceList = null;
						    ArrayList<TransactionVO> boePaymentList = null;
						    ArrayList<MeditorVO> paymentList = null;
						    CommonMethods commonMethods = null;
						    try
						    {
						      con = DBConnectionUtility.getConnection();
						      commonMethods = new CommonMethods();
						      int boeCount = 0;
						      String sqlQuery = "SELECT COUNT(*) AS BOE_COUNT  FROM ETT_BOE_PAYMENT BP WHERE BP.BOE_PAYMENT_BP_BOE_NO = ? AND TO_CHAR(BP.BOE_PAYMENT_BP_BOE_DT,'DD/MM/YYYY') = ? AND BP.PORT_CODE =?  AND BP.STATUS != 'R' ";

						 
						 
						      LoggableStatement ps = new LoggableStatement(con, sqlQuery);
						      ps.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
						      ps.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
						      ps.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
						      ResultSet rst = ps.executeQuery();
						      if (rst.next()) {
						        boeCount = rst.getInt("BOE_COUNT");
						      }
						      closeSqlRefferance(rst, ps);
						      if (boeCount == 0) {
						        boeVO = getTotalBOEAmt(con, boeVO);
						      }
						      invoiceList = fetchInvoiceList(con, boeVO);
						      if ((invoiceList.size() > 0) && (!invoiceList.isEmpty())) {
						        boeVO.setInvoiceList(invoiceList);
						      }
						      paymentList = fetchDataMultiPayments(con, boeVO);
						      if ((paymentList.size() > 0) && (!paymentList.isEmpty())) {
						        boeVO.setPaymentList(paymentList);
						      }
						      boePaymentList = fetchBOEPaymentList(con, boeVO);
						      if ((boePaymentList.size() > 0) && (!boePaymentList.isEmpty())) {
						        boeVO.setBoePaymentList(boePaymentList);
						      }
						    }
						    catch (Exception e)
						    {
						      e.printStackTrace();
						    }
						    finally
						    {
						      closeSqlRefferance(rs, pst, con);
						    }
						    logger.info("Exiting Method");
						    return boeVO;
						  }
						  public BoeVO fetchPaymentData(BoeVO boeVO, BOESearchVO searchVO)
						    throws DAOException
						  {
						    logger.info("----------fetchPaymentData--------------------");
						    Connection con = null;
						    LoggableStatement pst = null;
						    ResultSet rs = null;
						    ArrayList<TransactionVO> invoiceList = null;
						    ArrayList<TransactionVO> boePaymentList = null;
						    ArrayList<MeditorVO> paymentList = null;
						    CommonMethods commonMethods = null;
						    try
						    {
						      con = DBConnectionUtility.getConnection();
						      commonMethods = new CommonMethods();
						      int boeCount = 0;
						      String sqlQuery = "SELECT COUNT(*) AS BOE_COUNT  FROM ETT_BOE_PAYMENT BP WHERE BP.BOE_PAYMENT_BP_BOE_NO = ? AND TO_CHAR(BP.BOE_PAYMENT_BP_BOE_DT,'DD/MM/YYYY') = ? AND BP.PORT_CODE = ? AND BP.STATUS != 'R' ";

						 
						 
						      LoggableStatement ps = new LoggableStatement(con, sqlQuery);
						      ps.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
						      ps.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
						      ps.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
						      ResultSet rst = ps.executeQuery();
						      logger.info("----------fetchPaymentData-------------query-------" + ps.getQueryString());
						      if (rst.next())
						      {
						        boeCount = rst.getInt("BOE_COUNT");
						        logger.info("----------fetchPaymentData----------count---" + boeCount);
						      }
						      closeSqlRefferance(rst, ps);
						      if (boeCount == 0) {
						        boeVO = getTotalBOEAmt(con, boeVO);
						      }
						      invoiceList = fetchInvoiceList(con, boeVO);
						      if ((invoiceList.size() > 0) && (!invoiceList.isEmpty())) {
						        boeVO.setInvoiceList(invoiceList);
						      }
						      paymentList = fetchDataMultiPaymentSearch(con, boeVO, searchVO);
						      if ((paymentList.size() > 0) && (!paymentList.isEmpty())) {
						        boeVO.setPaymentList(paymentList);
						      }
						      boePaymentList = fetchBOEPaymentList(con, boeVO);
						      if ((boePaymentList.size() > 0) && (!boePaymentList.isEmpty())) {
						        boeVO.setBoePaymentList(boePaymentList);
						      }
						    }
						    catch (Exception e)
						    {
						      e.printStackTrace();
						    }
						    finally
						    {
						      closeSqlRefferance(rs, pst, con);
						    }
						    logger.info("Exiting Method");
						    return boeVO;
						  }
						  
						  public String getPaymentList(BOESearchVO boeSearchVO)
								    throws DAOException
								  {
								    CommonMethods commonMethods = null;
								    String BOE_QUERY = null;
								    try
								    {
								      commonMethods = new CommonMethods();
								      BOE_QUERY = "SELECT DISTINCT NVL(TRIM(PD.PD_CIF_NAME), ' ')  AS PD_CIF_NAME, NVL(TRIM(PD.PD_IE_CODE), ' ') AS PD_IE_CODE,   NVL(TRIM(PD.PD_TXN_REF), ' ') AS PD_TXN_REF,  NVL(TRIM(PD.PD_PART_PAY_REF), ' ')  AS PD_PART_PAY_REF, TO_CHAR(TO_DATE(PD.PD_TXN_DT, 'dd-mm-yy'),'dd-mm-yyyy') AS PD_TXN_DT,NVL(TRIM(PD.PD_TXN_CCY), ' ') AS PD_TXN_CCY,NVL(TRIM(PD.PD_FC_AMT), '0')  AS PD_FC_AMT,NVL(TRIM(PD.PD_ENDORSED_AMT), '0')  AS PD_ENDORSED_AMT, NVL(TRIM(BENEF_NAME), '')  AS BENEF_NAME,NVL(TRIM(BENEF_COUNTRY), '') AS BENEF_COUNTRY,PD.PD_FC_AMT*ETT_BOE_EX_RATE_CAL(NVL(TRIM(PD.PD_TXN_CCY),'USD'),?) AS PD_OUTSTANDING_AMT, (SELECT ETT_BOE_EX_RATE_CAL(NVL(TRIM(PD.PD_TXN_CCY),'USD'),?) FROM DUAL ) AS EXCHANGE_RATE FROM ETTV_BOE_PAYMENT_DETAILS PD WHERE PD.PD_CIF_ID = ? and PD.EVENT_STATUS='c' ";
								      String refNo = boeSearchVO.getPaymentRefNo();
								      if (!commonMethods.isNull(refNo)) {
								        BOE_QUERY = 
								          BOE_QUERY + "AND UPPER(PD.PD_TXN_REF) LIKE UPPER('%" + refNo.trim() + "%')";
								      }
								      String dateFrom = boeSearchVO.getPaymentDateFrom();
								      String dateTo = boeSearchVO.getPaymentDateTo();
								      if ((!commonMethods.isNull(dateFrom)) && 
								        (!commonMethods.isNull(dateTo)))
								      {
								        BOE_QUERY = 

								 
								          BOE_QUERY + "AND PD.PD_TXN_DT BETWEEN TO_DATE('" + dateFrom.trim() + "','DD-MM-YY') AND TO_DATE('" + dateTo.trim() + "','DD-MM-YY')";
								      }
								      else
								      {
								        if (!commonMethods.isNull(dateFrom)) {
								          BOE_QUERY = 
								            BOE_QUERY + " AND PD.PD_TXN_DT >= TO_DATE('" + dateFrom.trim() + "','DD-MM-YY')";
								        }
								        if (!commonMethods.isNull(dateTo)) {
								          BOE_QUERY = 
								            BOE_QUERY + " AND PD.PD_TXN_DT <= TO_DATE('" + dateTo.trim() + "','DD-MM-YY')";
								        }
								      }
								      String amountFrom = boeSearchVO.getAmountFrom();
								      String amountTo = boeSearchVO.getAmountTo();
								      if ((!commonMethods.isNull(amountFrom)) && 
								        (!commonMethods.isNull(amountTo)) && 
								        (!amountFrom.equals("undefined")) && 
								        (!amountTo.equals("undefined")))
								      {
								        if (amountFrom.trim().equals(amountTo.trim())) {
								          BOE_QUERY = 
								            BOE_QUERY + "AND PD.PD_FC_AMT  =  '" + boeSearchVO.getAmountFrom().trim() + "'";
								        } else {
								          BOE_QUERY = 

								 
								            BOE_QUERY + "AND PD.PD_FC_AMT >=  '" + boeSearchVO.getAmountFrom().trim() + "'" + "AND" + " PD.PD_FC_AMT <=  " + "'" + boeSearchVO.getAmountTo().trim() + "'";
								        }
								      }
								      else
								      {
								        if ((!commonMethods.isNull(boeSearchVO.getAmountFrom())) && 
								          (!amountFrom.equals("undefined"))) {
								          BOE_QUERY = 
								            BOE_QUERY + "AND PD.PD_FC_AMT >=  '" + boeSearchVO.getAmountFrom().trim() + "'";
								        }
								        if ((!commonMethods.isNull(boeSearchVO.getAmountTo())) && 
								          (!amountTo.equals("undefined"))) {
								          BOE_QUERY = 
								            BOE_QUERY + "AND PD.PD_FC_AMT <=  '" + boeSearchVO.getAmountTo().trim() + "'";
								        }
								      }
								      String currency = boeSearchVO.getPaymentCurrency();
								      if (!commonMethods.isNull(currency)) {
								        BOE_QUERY = 
								          BOE_QUERY + "AND UPPER(PD.PD_TXN_CCY) LIKE UPPER('%" + currency.trim() + "%')";
								      }
								      BOE_QUERY = BOE_QUERY + " ORDER BY PD_TXN_REF ";
								    }
								    catch (Exception exception)
								    {
								      throwDAOException(exception);
								    }
								    return BOE_QUERY;
								  }
								  public ArrayList<MeditorVO> fetchDataMultiPaymentSearch(Connection con, BoeVO boeVO, BOESearchVO searchVO)
								    throws DAOException
								  {
								    logger.info("Entering Method");
								    LoggableStatement pst = null;
								    ResultSet rs = null;
								    ArrayList<MeditorVO> paymentList = null;
								    CommonMethods commonMethods = null;
								    try
								    {
								      if (con == null) {
								        con = DBConnectionUtility.getConnection();
								      }
								      paymentList = new ArrayList();
								      commonMethods = new CommonMethods();
								      String sqlQuery = getPaymentList(searchVO);
								      pst = new LoggableStatement(con, sqlQuery);
								      pst.setString(1, commonMethods.getEmptyIfNull(boeVO.getBillCurrency()).trim());
								      pst.setString(2, commonMethods.getEmptyIfNull(boeVO.getBillCurrency()).trim());
								      pst.setString(3, commonMethods.getEmptyIfNull(boeVO.getCifNo()).trim());

								 
								 
								      rs = pst.executeQuery();
								      while (rs.next())
								      {
								        MeditorVO paymentVO = new MeditorVO();
								        String datevalid = "01-09-2022";
								        String txnDate1 = rs.getString("PD_TXN_DT").trim();
								        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
								        SimpleDateFormat sdf1 = null;
								        if (txnDate1.length() == 10) {
								          sdf1 = new SimpleDateFormat("dd-MM-yyyy");
								        } else {
								          sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
								        }
								        Date staticDate = sdf.parse(datevalid);
								        Date txnDate = sdf1.parse(txnDate1);
								        if (txnDate.before(staticDate))
								        {
								          try
								          {
								            paymentVO.setPaymentRefNo(commonMethods.getEmptyIfNull(rs.getString("PD_TXN_REF")).trim());
								            paymentVO.setPartPaymentSlNo(commonMethods.getEmptyIfNull(rs.getString("PD_PART_PAY_REF").trim()));
								            paymentVO.setPaymentDate(commonMethods.getEmptyIfNull(rs.getString("PD_TXN_DT")).trim());
								            paymentVO.setPaymentCurr(commonMethods.getEmptyIfNull(rs.getString("PD_TXN_CCY")).trim());
								            paymentVO.setPaymentAmount(commonMethods.getEmptyIfNull(rs.getString("PD_FC_AMT")).trim());
								            paymentVO.setEndorseAmt(commonMethods.getEmptyIfNull(rs.getString("PD_ENDORSED_AMT")).trim());
								            paymentVO.setEndorseAmt_temp(commonMethods.getEmptyIfNull(rs.getString("PD_ENDORSED_AMT")).trim());
								            paymentVO.setBenefName(commonMethods.getEmptyIfNull(rs.getString("BENEF_NAME")).trim());
								            paymentVO.setBenefCountry(commonMethods.getEmptyIfNull(rs.getString("BENEF_COUNTRY")).trim());
								            paymentVO.setExRate(commonMethods.getEmptyIfNull(rs.getString("EXCHANGE_RATE")).trim());
								            paymentVO.setExRateTemp(commonMethods.getEmptyIfNull(rs.getString("EXCHANGE_RATE")).trim());
								            String tempVal = commonMethods.getEmptyIfNull(rs.getString("PD_TXN_REF")).trim() + 
								              ":" + commonMethods.getEmptyIfNull(rs.getString("PD_PART_PAY_REF")).trim();
								            paymentVO.setUtilityTransNo(tempVal);
								            
								            double ormClosureAmt = getORMClosureAmount(con, paymentVO);
								            double billOutAmt = commonMethods.convertToDouble(rs.getString("PD_OUTSTANDING_AMT"));
								            double remainAmt = billOutAmt - ormClosureAmt;
								            BigDecimal outAmt = BigDecimal.valueOf(remainAmt).setScale(2, RoundingMode.HALF_UP);
								            if (remainAmt == 0.0D) {
								              paymentVO.setFullyAlloc("Y");
								            } else {
								              paymentVO.setFullyAlloc("N");
								            }
								            paymentVO.setOutstandingAmt(String.valueOf(outAmt));
								            paymentVO.setOutstandingAmt_temp(String.valueOf(outAmt));
								            if (paymentVO.getFullyAlloc().equalsIgnoreCase("Y")) {
								              continue;
								            }
								            paymentList.add(paymentVO);
								          }
								          catch (Exception exce)
								          {
								            exce.printStackTrace();
								            logger.info("Exception is " + exce.getMessage());
								          }
								        }
								        else
								        {
								          LoggableStatement lstOrmCheck = null;
								          ResultSet rsOrmCheck = null;
								          String ormref = null;
								          try
								          {
								            String sqlQuery1 = "SELECT DISTINCT OUTWARDREFERNECNO AS OUTWARDREFERNECNO FROM ETT_ORM_ACK WHERE OUTWARDREFERNECNO= ? ";
								            ormref = commonMethods.getEmptyIfNull(rs.getString("PD_TXN_REF")).trim() + commonMethods.getEmptyIfNull(rs.getString("PD_PART_PAY_REF").trim());
								            lstOrmCheck = new LoggableStatement(con, sqlQuery1);
								            lstOrmCheck.setString(1, ormref);
								            rsOrmCheck = lstOrmCheck.executeQuery();
								            while (rsOrmCheck.next()) {
								              try
								              {
								                paymentVO.setPaymentRefNo(commonMethods.getEmptyIfNull(rs.getString("PD_TXN_REF")).trim());
								                paymentVO.setPartPaymentSlNo(commonMethods.getEmptyIfNull(rs.getString("PD_PART_PAY_REF").trim()));
								                paymentVO.setPaymentDate(commonMethods.getEmptyIfNull(rs.getString("PD_TXN_DT")).trim());
								                paymentVO.setPaymentCurr(commonMethods.getEmptyIfNull(rs.getString("PD_TXN_CCY")).trim());
								                paymentVO.setPaymentAmount(commonMethods.getEmptyIfNull(rs.getString("PD_FC_AMT")).trim());
								                paymentVO.setEndorseAmt(commonMethods.getEmptyIfNull(rs.getString("PD_ENDORSED_AMT")).trim());
								                paymentVO.setEndorseAmt_temp(commonMethods.getEmptyIfNull(rs.getString("PD_ENDORSED_AMT")).trim());
								                paymentVO.setBenefName(commonMethods.getEmptyIfNull(rs.getString("BENEF_NAME")).trim());
								                paymentVO.setBenefCountry(commonMethods.getEmptyIfNull(rs.getString("BENEF_COUNTRY")).trim());
								                paymentVO.setExRate(commonMethods.getEmptyIfNull(rs.getString("EXCHANGE_RATE")).trim());
								                paymentVO.setExRateTemp(commonMethods.getEmptyIfNull(rs.getString("EXCHANGE_RATE")).trim());
								                String tempVal = commonMethods.getEmptyIfNull(rs.getString("PD_TXN_REF")).trim() + 
								                  ":" + commonMethods.getEmptyIfNull(rs.getString("PD_PART_PAY_REF")).trim();
								                paymentVO.setUtilityTransNo(tempVal);
								                double ormClosureAmt = getORMClosureAmount(con, paymentVO);
								                double billOutAmt = commonMethods.convertToDouble(rs.getString("PD_OUTSTANDING_AMT"));
								                double remainAmt = billOutAmt - ormClosureAmt;
								                BigDecimal outAmt = BigDecimal.valueOf(remainAmt).setScale(2, RoundingMode.HALF_UP);
								                if (remainAmt == 0.0D) {
								                  paymentVO.setFullyAlloc("Y");
								                } else {
								                  paymentVO.setFullyAlloc("N");
								                }
								                paymentVO.setOutstandingAmt(String.valueOf(outAmt));
								                paymentVO.setOutstandingAmt_temp(String.valueOf(outAmt));
								                if (!paymentVO.getFullyAlloc().equalsIgnoreCase("Y")) {
								                  paymentList.add(paymentVO);
								                }
								              }
								              catch (Exception exce)
								              {
								                exce.printStackTrace();
								                logger.info("Exception is " + exce.getMessage());
								              }
								            }
								          }
								          catch (SQLException se)
								          {
								            se.printStackTrace();
								          }
								          finally
								          {
								            DBConnectionUtility.surrenderDB(null, lstOrmCheck, rsOrmCheck);
								          }
								        }
								      }
								    }
								    catch (Exception e)
								    {
								      e.printStackTrace();
								      logger.info("Exception is " + e.getMessage());
								    }
								    finally
								    {
								      closeSqlRefferance(rs, pst);
								    }
								    logger.info("Exiting Method");
								    return paymentList;
								  }
								  public String storeBillData(BoeVO boeVO, String[] chkPayList, String[] radioId)
								    throws DAOException, SQLException
								  {
								    logger.info("This is Inside of Sotore Bill Data");
								    logger.info("Entering Method");
								    String sExtby = "2";
								    Connection con = null;
								    LoggableStatement loggableStatement = null;
								    ResultSet rs = null;
								    CommonMethods commonMethods = null;
								    int invCount = 0;
								    String result = null;
								    try
								    {
								      HttpSession session = ServletActionContext.getRequest().getSession();

								 
								      String loginedUserId = (String)session.getAttribute("loginedUserName");
								      logger.info("loginedUserName------------------root");

								 
								      con = DBConnectionUtility.getConnection();
								      logger.info("beore common methods---------");
								      commonMethods = new CommonMethods();
								      logger.info("after common methods---------");
								      for (int i = 0; i < chkPayList.length; i++)
								      {
								        int insCount = 0;
								        String[] s = chkPayList[i].split(":");
								        String chkListVal = chkPayList[i];
								        boeVO.setPaymentRefNo(commonMethods.getEmptyIfNull(s[0]).trim());
								        logger.info("commonMethods.getEmptyIfNull(s[0]).trim()" + commonMethods.getEmptyIfNull(s[0]).trim());
								        boeVO.setPartPaymentSlNo(commonMethods.getEmptyIfNull(s[1]).trim());
								        logger.info("commonMethods.getEmptyIfNull(s[1]).trim()" + commonMethods.getEmptyIfNull(s[1]).trim());
								        double allocateAmt = commonMethods.convertToDouble(s[2]);
								        logger.info("commonMethods.convertToDouble(s[2])" + commonMethods.convertToDouble(s[2]));
								        double exRate = commonMethods.convertToDouble(s[3]);
								        logger.info("commonMethods.convertToDouble(s[3])" + commonMethods.convertToDouble(s[3]));
								      }
								      logger.info("chkPayList---------chkPayList---------" + chkPayList);
								      
								      if ((chkPayList != null) && (chkPayList.length > 0))
								      {
								        logger.info("222");
								        for (int i = 0; i < chkPayList.length; i++)
								        {
								          int insCount = 0;
								          String[] s = chkPayList[i].split(":");
								          String chkListVal = chkPayList[i];
								          boeVO.setPaymentRefNo(commonMethods.getEmptyIfNull(s[0]).trim());
								          boeVO.setPartPaymentSlNo(commonMethods.getEmptyIfNull(s[1]).trim());
								          double allocateAmt = commonMethods.convertToDouble(s[2]);
								          double exRate = commonMethods.convertToDouble(s[3]);
								          logger.info("allocateAmt--------------" + allocateAmt);
								          logger.info("exRate--------------" + exRate);
								          int count = 0;

								 
								          logger.info("333");
								          String sPorcOutput = "";
								          CallableStatement cs = null;
								          String sProcName = "{call ETT_BOE_EXP_VALIDATION(?, ?, ?, ?)}";
								          cs = con.prepareCall(sProcName);
								          cs.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
								          cs.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
								          cs.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
								          cs.setString(4, sExtby);
								          if (cs.executeUpdate() <= 0)
								          {
								            logger.info("Procedure having some problem");
								            logger.info("Procedure having some problem");
								          }
								          if (cs != null) {
								            cs.close();
								          }
								          logger.info("444");
								          String checkQuery = "SELECT COUNT(*) AS BOE_COUNT FROM   ETT_BOE_PAYMENT WHERE BOE_PAYMENT_BP_PAY_REF=?  AND  BOE_PAYMENT_BP_PAY_PART_REF=?   AND BOE_PAYMENT_BP_BOE_NO=?  AND TO_CHAR(BOE_PAYMENT_BP_BOE_DT,'DD/MM/YYYY') =?  AND PORT_CODE = ?";

								 
								 
								 
								          loggableStatement = new LoggableStatement(con, checkQuery);
								          loggableStatement.setString(1, commonMethods.getEmptyIfNull(boeVO.getPaymentRefNo()).trim());
								          loggableStatement.setString(2, commonMethods.getEmptyIfNull(boeVO.getPartPaymentSlNo()).trim());
								          loggableStatement.setString(3, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
								          loggableStatement.setString(4, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
								          loggableStatement.setString(5, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
								          rs = loggableStatement.executeQuery();
								          if (rs.next()) {
								            count = rs.getInt("BOE_COUNT");
								          }
								          String boeQuery = "SELECT TO_CHAR(TO_DATE(PD.PD_TXN_DT, 'dd-mm-yy'),'dd/mm/yyyy') AS PD_TXN_DT, NVL(TRIM(PD.PD_TXN_CCY), ' ') AS PD_TXN_CCY, NVL(TRIM(PD.PD_FC_AMT), '0') AS PD_FC_AMT, NVL(TRIM(PD.PD_ENDORSED_AMT), '0') AS PD_ENDORSED_AMT, NVL(TRIM(BENEF_NAME), '') AS BENEF_NAME,NVL(TRIM(BENEF_COUNTRY), '') AS BENEF_COUNTRY, NVL(TRIM(PD.PD_OUTSTANDING_AMT), '0') AS PD_OUTSTANDING_AMT  FROM ETTV_BOE_PAYMENT_DETAILS PD WHERE PD.PD_TXN_REF = ? AND PD.PD_PART_PAY_REF = ? AND PD.PD_CIF_ID = ? ";
								          LoggableStatement ps = new LoggableStatement(con, boeQuery);
								          ps.setString(1, commonMethods.getEmptyIfNull(boeVO.getPaymentRefNo()).trim());
								          ps.setString(2, commonMethods.getEmptyIfNull(boeVO.getPartPaymentSlNo()).trim());
								          ps.setString(3, commonMethods.getEmptyIfNull(boeVO.getCifNo()).trim());
								          logger.info("555");
								          ResultSet rs1 = ps.executeQuery();
								          if (rs1.next())
								          {
								            boeVO.setPayDate(commonMethods.getEmptyIfNull(rs1.getString("PD_TXN_DT")).trim());
								            boeVO.setPaymentCurr(commonMethods.getEmptyIfNull(rs1.getString("PD_TXN_CCY")).trim());
								            boeVO.setBenefName(commonMethods.getEmptyIfNull(rs1.getString("BENEF_NAME")).trim());
								            boeVO.setBenefCountry(commonMethods.getEmptyIfNull(rs1.getString("BENEF_COUNTRY")).trim());
								            double payAmt = commonMethods.convertToDouble(rs1.getString("PD_FC_AMT"));
								            double outAmt = commonMethods.convertToDouble(rs1.getString("PD_OUTSTANDING_AMT"));

								 
								            double tempOutAmt = outAmt - allocateAmt;
								            double realInvAmtIC = allocateAmt * exRate;
								            realInvAmtIC = Math.round(realInvAmtIC * 100.0D) / 100.0D;
								            boeVO.setPaymentAmount(String.valueOf(payAmt));
								            boeVO.setAllocAmt(String.valueOf(allocateAmt));
								            boeVO.setOutAmt(String.valueOf(tempOutAmt));
								            boeVO.setEqPaymentAmount(String.valueOf(realInvAmtIC));
								            boeVO.setBoeExRate(String.valueOf(exRate));
								            if (tempOutAmt == 0.0D) {
								              boeVO.setFullyAlloc("Y");
								            } else {
								              boeVO.setFullyAlloc("N");
								            }
								          }
								          closeSqlRefferance(rs1, ps);
								          logger.info("666 : " + count);
								          logger.info("The count is : ========================================>>> : " + count);
								          if ((count > 0) && (!boeVO.getBtnModify().equalsIgnoreCase("M")))
								          {
								            logger.info("999 : ");
								            insCount = updateBillData(con, boeVO);
								          }
								          else if ((boeVO.getBtnModify() != null) && (boeVO.getBtnModify().equalsIgnoreCase("M")) && (count > 0))
								          {
								            logger.info("101 : ");
								            insCount = updateModifiedBillData(con, boeVO);
								          }
								          else
								          {
								            logger.info("777 : ");
								            String sqlQuery = "INSERT INTO ETT_BOE_PAYMENT(BOE_PAYMENT_CIF_ID,BOE_PAYMENT_CIF_NAME, BOE_PAYMENT_BP_BOE_NO,BOE_PAYMENT_BP_BOE_DT,BOE_PAYMENT_BP_PAY_DT,BOE_PAYMENT_BP_PAY_REF, BOE_PAYMENT_BP_PAY_PART_REF,BOE_ENTRY_AMT,BOE_PAYMENT_BP_PAY_ENDORSE_AMT, BOE_PAYMENT_BP_PAY_OS_FC_AMT,BOE_PAYMENT_BP_PAY_EDS_FC_AMT,BOE_PAYMENT_BP_PAY_FC_AMT, BOE_PAYMENT_BP_PAY_FULL_YN,REMARKS,BOE_PAYMENT_BP_PAY_CCY,BOE_PAYMENT_BP_BOE_CCY, PORT_CODE,USERID,CREATEDDATE,STATUS,BOE_PAYMENT_BENEF_NAME, BOE_PAYMENT_BENEF_COUNTRY,BOE_MAWB_MBLNUMBER,BOE_MAWB_MBLDATE,BOE_HAWB_HBLNUMBER,BOE_HAWB_HBLDATE, BOE_IGMNUMBER,BOE_IGMDATE,THIRD_PARTY_PAYMENT,CHANGED_IE_CODE,RECORD_INDICATOR,ADCODE,IECODE, PORT_OF_SHIPMENT,IMPORT_AGENCY,IE_ADDRESS,IE_PAN,G_P,ENDORSED_BOE_AMT, EXCHANGE_RATE,BOE_TRANS_TYPE,PAGETYPE, BES_IND, PRE_ENDORSED_AMT)  VALUES (?,?,?,TO_DATE(?,'DD-MM-YYYY'),TO_DATE(?,'DD-MM-YYYY'),?,?,?,?, ?,?,?,?,?,?,?,?,?,SYSTIMESTAMP,?,?,?,?, TO_DATE(?,'DD-MM-YYYY'),?,TO_DATE(?,'DD-MM-YYYY'),?,TO_DATE(?,'DD-MM-YYYY'),\t?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, ?)";

								 
								 
								 
								 
								 
								 
								 
								            LoggableStatement pst = new LoggableStatement(con, sqlQuery);
								            String gpType = null;
								            if (boeVO.getGovprv().equalsIgnoreCase("Government")) {
								              gpType = "G";
								            } else {
								              gpType = "P";
								            }
								            String recInd = null;
								            if (boeVO.getRecInd().equalsIgnoreCase("New")) {
								              recInd = "1";
								            } else if (boeVO.getRecInd().equalsIgnoreCase("Amendment")) {
								              recInd = "2";
								            } else {
								              recInd = "3";
								            }
								            String imAgency = null;
								            if (boeVO.getImAgency().equalsIgnoreCase("Customs")) {
								              imAgency = "1";
								            } else {
								              imAgency = "2";
								            }
								            
								            logger.info("888 : ");
								            pst.setString(1, commonMethods.getEmptyIfNull(boeVO.getCifNo()).trim());
								            pst.setString(2, commonMethods.getEmptyIfNull(boeVO.getCustName()).trim());
								            pst.setString(3, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
								            pst.setString(4, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
								            pst.setString(5, commonMethods.getEmptyIfNull(boeVO.getPayDate()).trim());
								            pst.setString(6, commonMethods.getEmptyIfNull(boeVO.getPaymentRefNo()).trim());
								            pst.setString(7, commonMethods.getEmptyIfNull(boeVO.getPartPaymentSlNo()).trim());
								            pst.setString(8, CommonMethods.setDefaultAmount(boeVO.getBillAmt()));
								            pst.setString(9, CommonMethods.setDefaultAmount(boeVO.getAllocAmt()));
								            pst.setString(10, CommonMethods.setDefaultAmount(boeVO.getOutAmt()));
								            pst.setString(11, CommonMethods.setDefaultAmount(boeVO.getAllocAmt()));
								            pst.setString(12, CommonMethods.setDefaultAmount(boeVO.getPaymentAmount()));
								            pst.setString(13, commonMethods.getEmptyIfNull(boeVO.getFullyAlloc()).trim());
								            pst.setString(14, commonMethods.getEmptyIfNull(boeVO.getRemarks()).trim());
								            pst.setString(15, commonMethods.getEmptyIfNull(boeVO.getPaymentCurr()).trim());
								            pst.setString(16, commonMethods.getEmptyIfNull(boeVO.getBillCurrency()).trim());
								            pst.setString(17, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
								            pst.setString(18, loginedUserId);
								            pst.setString(19, "P");
								            pst.setString(20, CommonMethods.checkForNullvalue(boeVO.getBenefName()));
								            pst.setString(21, CommonMethods.checkForNullvalue(boeVO.getBenefCountry()));
								            pst.setString(22, CommonMethods.checkForNullvalue(boeVO.getMblNo()));
								            pst.setString(23, CommonMethods.checkForNullvalue(boeVO.getMblDate()));
								            pst.setString(24, CommonMethods.checkForNullvalue(boeVO.getHblNo()));
								            pst.setString(25, CommonMethods.checkForNullvalue(boeVO.getHblDate()));
								            pst.setString(26, CommonMethods.checkForNullvalue(boeVO.getIgmNo()));
								            pst.setString(27, CommonMethods.checkForNullvalue(boeVO.getIgmDate()));
								            pst.setString(28, CommonMethods.checkForNullvalue(boeVO.getThrdParty()));
								            pst.setString(29, CommonMethods.checkForNullvalue(boeVO.getIeCodeChange()));
								            pst.setString(30, CommonMethods.checkForNullvalue(recInd));
								            pst.setString(31, CommonMethods.checkForNullvalue(boeVO.getAdCode()));
								            pst.setString(32, CommonMethods.checkForNullvalue(boeVO.getIeCode()));
								            pst.setString(33, CommonMethods.checkForNullvalue(boeVO.getPos()));
								            pst.setString(34, CommonMethods.checkForNullvalue(imAgency));
								            pst.setString(35, CommonMethods.checkForNullvalue(boeVO.getIeadd()));
								            pst.setString(36, CommonMethods.checkForNullvalue(boeVO.getIepan()));
								            pst.setString(37, CommonMethods.checkForNullvalue(gpType));
								            pst.setString(38, CommonMethods.checkForNullvalue(boeVO.getEqPaymentAmount()));
								            pst.setString(39, CommonMethods.checkForNullvalue(boeVO.getBoeExRate()));
								            pst.setString(40, CommonMethods.checkForNullvalue(boeVO.getTransType()));
								            pst.setString(41, "M");
								            pst.setString(42, CommonMethods.checkForNullvalue(boeVO.getBoeBesMBInd()));
								            pst.setString(43, CommonMethods.setDefaultAmount(boeVO.getEqPaymentAmount()));
								            logger.info("102 : ");
								            insCount = pst.executeUpdate();
								            logger.info("insCount------insert------------>" + insCount);
								            logger.info("insCount------insert------------>" + insCount);
								            logger.info("103 : ");
								            closePreparedStatement(pst);
								          }
								          logger.info("boeVO.getBtnModify()--------------->" + boeVO.getBtnModify());
								          logger.info("boeVO.getBtnModify()---------------------->" + boeVO.getBtnModify());
								          if (insCount > 0)
								          {
								            logger.info("104 : ");
								            invCount += storeInvoiceData(con, radioId, boeVO);
								            logger.info("105 : " + invCount);
								          }
								          else if ((boeVO.getBtnModify() != null) && (boeVO.getBtnModify().equalsIgnoreCase("M")))
								          {
								            String radInvoiceVal = boeVO.getInvoicVal();
								            invCount += updateInvoiceData(con, chkListVal, radioId, boeVO);
								          }
								        }
								      }
								      if (invCount > 0) {
								        result = "success";
								      }
								    }
								    catch (Exception e)
								    {
								      logger.info("storeBillData-------Exception--------" + e.getMessage());
								      logger.info("storeBillData-------Exception--------" + e.getMessage());
								      throwDAOException(e);
								    }
								    finally
								    {
								      closeSqlRefferance(rs, loggableStatement, con);
								    }
								    logger.info("Exiting Method");
								    return result;
								  }
								  public String storeBillData_1(BoeVO boeVO, String[] chkPayList, String[] radioId)
								    throws DAOException, SQLException
								  {
								    logger.info("This is Inside of Sotore Bill Data");
								    logger.info("Entering Method");
								    String sExtby = "2";
								    Connection con = null;
								    LoggableStatement loggableStatement = null;
								    ResultSet rs = null;
								    CommonMethods commonMethods = null;
								    int invCount = 0;
								    String result = null;
								    try
								    {
								      HttpSession session = ServletActionContext.getRequest().getSession();
								      String loginedUserId = (String)session.getAttribute("loginedUserId");
								      logger.info("111");
								      con = DBConnectionUtility.getConnection();
								      commonMethods = new CommonMethods();
								      if ((chkPayList != null) && (chkPayList.length > 0))
								      {
								        logger.info("222");
								        for (int i = 0; i < chkPayList.length; i++)
								        {
								          int insCount = 0;
								          String[] s = chkPayList[i].split(":");
								          String chkListVal = chkPayList[i];
								          boeVO.setPaymentRefNo(commonMethods.getEmptyIfNull(s[0]).trim());
								          boeVO.setPartPaymentSlNo(commonMethods.getEmptyIfNull(s[1]).trim());
								          double allocateAmt = commonMethods.convertToDouble(s[2]);
								          double exRate = commonMethods.convertToDouble(s[3]);
								          int count = 0;

								 
								          logger.info("333");
								          String sPorcOutput = "";
								          CallableStatement cs = null;
								          String sProcName = "{call ETT_BOE_EXP_VALIDATION(?, ?, ?, ?)}";
								          cs = con.prepareCall(sProcName);
								          
								          cs.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
								          cs.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
								          cs.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
								          cs.setString(4, sExtby);
								          if (cs.executeUpdate() <= 0) {
								            logger.info("Procedure having some problem");
								          }
								          if (cs != null) {
								            cs.close();
								          }
								          logger.info("444");
								          String checkQuery = "SELECT COUNT(*) AS BOE_COUNT FROM   ETT_BOE_PAYMENT WHERE BOE_PAYMENT_BP_PAY_REF=?  AND  BOE_PAYMENT_BP_PAY_PART_REF=?   AND BOE_PAYMENT_BP_BOE_NO= ?  AND TO_CHAR(BOE_PAYMENT_BP_BOE_DT,'DD/MM/YYYY') = ?  AND PORT_CODE = ?";

								 
								 
								 
								          loggableStatement = new LoggableStatement(con, checkQuery);
								          loggableStatement.setString(1, commonMethods.getEmptyIfNull(boeVO.getPaymentRefNo()).trim());
								          loggableStatement.setString(2, commonMethods.getEmptyIfNull(boeVO.getPartPaymentSlNo()).trim());
								          loggableStatement.setString(3, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
								          loggableStatement.setString(4, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
								          loggableStatement.setString(5, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
								          rs = loggableStatement.executeQuery();
								          if (rs.next()) {
								            count = rs.getInt("BOE_COUNT");
								          }
								          String boeQuery = "SELECT TO_CHAR(TO_DATE(PD.PD_TXN_DT, 'dd-mm-yy'),'dd/mm/yyyy') AS PD_TXN_DT, NVL(TRIM(PD.PD_TXN_CCY), ' ') AS PD_TXN_CCY, NVL(TRIM(PD.PD_FC_AMT), '0') AS PD_FC_AMT, NVL(TRIM(PD.PD_ENDORSED_AMT), '0') AS PD_ENDORSED_AMT, NVL(TRIM(BENEF_NAME), '') AS BENEF_NAME,NVL(TRIM(BENEF_COUNTRY), '') AS BENEF_COUNTRY, NVL(TRIM(PD.PD_OUTSTANDING_AMT), '0') AS PD_OUTSTANDING_AMT  FROM ETTV_BOE_PAYMENT_DETAILS PD WHERE PD.PD_TXN_REF = ? AND PD.PD_PART_PAY_REF = ? AND PD.PD_CIF_ID = ? ";
								          LoggableStatement ps = new LoggableStatement(con, boeQuery);
								          ps.setString(1, commonMethods.getEmptyIfNull(boeVO.getPaymentRefNo()).trim());
								          ps.setString(2, commonMethods.getEmptyIfNull(boeVO.getPartPaymentSlNo()).trim());
								          ps.setString(3, commonMethods.getEmptyIfNull(boeVO.getCifNo()).trim());
								          logger.info("555");
								          ResultSet rs1 = ps.executeQuery();
								          if (rs1.next())
								          {
								            boeVO.setPayDate(commonMethods.getEmptyIfNull(rs1.getString("PD_TXN_DT")).trim());
								            boeVO.setPaymentCurr(commonMethods.getEmptyIfNull(rs1.getString("PD_TXN_CCY")).trim());
								            boeVO.setBenefName(commonMethods.getEmptyIfNull(rs1.getString("BENEF_NAME")).trim());
								            boeVO.setBenefCountry(commonMethods.getEmptyIfNull(rs1.getString("BENEF_COUNTRY")).trim());
								            double payAmt = commonMethods.convertToDouble(rs1.getString("PD_FC_AMT"));
								            double outAmt = commonMethods.convertToDouble(rs1.getString("PD_OUTSTANDING_AMT"));

								 
								            double tempOutAmt = outAmt - allocateAmt;
								            double realInvAmtIC = allocateAmt * exRate;
								            realInvAmtIC = Math.round(realInvAmtIC * 100.0D) / 100.0D;
								            boeVO.setPaymentAmount(String.valueOf(payAmt));
								            boeVO.setAllocAmt(String.valueOf(allocateAmt));
								            boeVO.setOutAmt(String.valueOf(tempOutAmt));
								            boeVO.setEqPaymentAmount(String.valueOf(realInvAmtIC));
								            boeVO.setBoeExRate(String.valueOf(exRate));
								            if (tempOutAmt == 0.0D) {
								              boeVO.setFullyAlloc("Y");
								            } else {
								              boeVO.setFullyAlloc("N");
								            }
								          }
								          closeSqlRefferance(rs1, ps);
								          logger.info("666 : " + count);
								          logger.info("The count is : ========================================>>> : " + count);
								          if ((count > 0) && (!boeVO.getBtnModify().equalsIgnoreCase("M")))
								          {
								            logger.info("999 : ");
								            insCount = updateBillData(con, boeVO);
								          }
								          else if ((boeVO.getBtnModify() != null) && (boeVO.getBtnModify().equalsIgnoreCase("M")) && (count > 0))
								          {
								            logger.info("101 : ");
								            insCount = updateModifiedBillData(con, boeVO);
								          }
								          else
								          {
								            logger.info("777 : ");
								            String sqlQuery = "INSERT INTO ETT_BOE_PAYMENT(BOE_PAYMENT_CIF_ID,BOE_PAYMENT_CIF_NAME, BOE_PAYMENT_BP_BOE_NO,BOE_PAYMENT_BP_BOE_DT,BOE_PAYMENT_BP_PAY_DT,BOE_PAYMENT_BP_PAY_REF, BOE_PAYMENT_BP_PAY_PART_REF,BOE_ENTRY_AMT,BOE_PAYMENT_BP_PAY_ENDORSE_AMT, BOE_PAYMENT_BP_PAY_OS_FC_AMT,BOE_PAYMENT_BP_PAY_EDS_FC_AMT,BOE_PAYMENT_BP_PAY_FC_AMT, BOE_PAYMENT_BP_PAY_FULL_YN,REMARKS,BOE_PAYMENT_BP_PAY_CCY,BOE_PAYMENT_BP_BOE_CCY, PORT_CODE,USERID,CREATEDDATE,STATUS,BOE_PAYMENT_BENEF_NAME, BOE_PAYMENT_BENEF_COUNTRY,BOE_MAWB_MBLNUMBER,BOE_MAWB_MBLDATE,BOE_HAWB_HBLNUMBER,BOE_HAWB_HBLDATE, BOE_IGMNUMBER,BOE_IGMDATE,THIRD_PARTY_PAYMENT,CHANGED_IE_CODE,RECORD_INDICATOR,ADCODE,IECODE, PORT_OF_SHIPMENT,IMPORT_AGENCY,IE_ADDRESS,IE_PAN,G_P,ENDORSED_BOE_AMT, EXCHANGE_RATE,BOE_TRANS_TYPE,PAGETYPE, BES_IND, PRE_ENDORSED_AMT)  VALUES (?,?,?,TO_DATE(?,'DD-MM-YYYY'),TO_DATE(?,'DD-MM-YYYY'),?,?,?,?, ?,?,?,?,?,?,?,?,?,SYSTIMESTAMP,?,?,?,?, TO_DATE(?,'DD-MM-YYYY'),?,TO_DATE(?,'DD-MM-YYYY'),?,TO_DATE(?,'DD-MM-YYYY'),\t?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, ?)";

								 
								 
								 
								 
								 
								 
								 
								            LoggableStatement pst = new LoggableStatement(con, sqlQuery);
								            String gpType = null;
								            if (boeVO.getGovprv().equalsIgnoreCase("Government")) {
								              gpType = "G";
								            } else {
								              gpType = "P";
								            }
								            String recInd = null;
								            if (boeVO.getRecInd().equalsIgnoreCase("New")) {
								              recInd = "1";
								            } else if (boeVO.getRecInd().equalsIgnoreCase("Amendment")) {
								              recInd = "2";
								            } else {
								              recInd = "3";
								            }
								            String imAgency = null;
								            if (boeVO.getImAgency().equalsIgnoreCase("Customs")) {
								              imAgency = "1";
								            } else {
								              imAgency = "2";
								            }
								            
								            logger.info("888 : ");
								            pst.setString(1, commonMethods.getEmptyIfNull(boeVO.getCifNo()).trim());
								            pst.setString(2, commonMethods.getEmptyIfNull(boeVO.getCustName()).trim());
								            pst.setString(3, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
								            pst.setString(4, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
								            pst.setString(5, commonMethods.getEmptyIfNull(boeVO.getPayDate()).trim());
								            pst.setString(6, commonMethods.getEmptyIfNull(boeVO.getPaymentRefNo()).trim());
								            pst.setString(7, commonMethods.getEmptyIfNull(boeVO.getPartPaymentSlNo()).trim());
								            pst.setString(8, CommonMethods.setDefaultAmount(boeVO.getBillAmt()));
								            pst.setString(9, CommonMethods.setDefaultAmount(boeVO.getAllocAmt()));
								            pst.setString(10, CommonMethods.setDefaultAmount(boeVO.getOutAmt()));
								            pst.setString(11, CommonMethods.setDefaultAmount(boeVO.getAllocAmt()));
								            pst.setString(12, CommonMethods.setDefaultAmount(boeVO.getPaymentAmount()));
								            pst.setString(13, commonMethods.getEmptyIfNull(boeVO.getFullyAlloc()).trim());
								            pst.setString(14, commonMethods.getEmptyIfNull(boeVO.getRemarks()).trim());
								            pst.setString(15, commonMethods.getEmptyIfNull(boeVO.getPaymentCurr()).trim());
								            pst.setString(16, commonMethods.getEmptyIfNull(boeVO.getBillCurrency()).trim());
								            pst.setString(17, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
								            pst.setString(18, loginedUserId);
								            pst.setString(19, "P");
								            pst.setString(20, CommonMethods.checkForNullvalue(boeVO.getBenefName()));
								            pst.setString(21, CommonMethods.checkForNullvalue(boeVO.getBenefCountry()));
								            pst.setString(22, CommonMethods.checkForNullvalue(boeVO.getMblNo()));
								            pst.setString(23, CommonMethods.checkForNullvalue(boeVO.getMblDate()));
								            pst.setString(24, CommonMethods.checkForNullvalue(boeVO.getHblNo()));
								            pst.setString(25, CommonMethods.checkForNullvalue(boeVO.getHblDate()));
								            pst.setString(26, CommonMethods.checkForNullvalue(boeVO.getIgmNo()));
								            pst.setString(27, CommonMethods.checkForNullvalue(boeVO.getIgmDate()));
								            pst.setString(28, CommonMethods.checkForNullvalue(boeVO.getThrdParty()));
								            pst.setString(29, CommonMethods.checkForNullvalue(boeVO.getIeCodeChange()));
								            pst.setString(30, CommonMethods.checkForNullvalue(recInd));
								            pst.setString(31, CommonMethods.checkForNullvalue(boeVO.getAdCode()));
								            pst.setString(32, CommonMethods.checkForNullvalue(boeVO.getIeCode()));
								            pst.setString(33, CommonMethods.checkForNullvalue(boeVO.getPos()));
								            pst.setString(34, CommonMethods.checkForNullvalue(imAgency));
								            pst.setString(35, CommonMethods.checkForNullvalue(boeVO.getIeadd()));
								            pst.setString(36, CommonMethods.checkForNullvalue(boeVO.getIepan()));
								            pst.setString(37, CommonMethods.checkForNullvalue(gpType));
								            pst.setString(38, CommonMethods.checkForNullvalue(boeVO.getEqPaymentAmount()));
								            pst.setString(39, CommonMethods.checkForNullvalue(boeVO.getBoeExRate()));
								            pst.setString(40, CommonMethods.checkForNullvalue(boeVO.getTransType()));
								            pst.setString(41, "M");
								            pst.setString(42, CommonMethods.checkForNullvalue(boeVO.getBoeBesMBInd()));
								            pst.setString(43, CommonMethods.setDefaultAmount(boeVO.getEqPaymentAmount()));
								            logger.info("102 : ");
								            insCount = pst.executeUpdate();
								            logger.info("103 : ");
								            closePreparedStatement(pst);
								          }
								          if ((insCount > 0) && (!boeVO.getBtnModify().equalsIgnoreCase("M")))
								          {
								            logger.info("104 : ");
								            invCount += storeInvoiceData(con, radioId, boeVO);
								            logger.info("105 : " + invCount);
								          }
								          else if ((boeVO.getBtnModify() != null) && (boeVO.getBtnModify().equalsIgnoreCase("M")))
								          {
								            String radInvoiceVal = boeVO.getInvoicVal();
								            invCount += updateInvoiceData(con, chkListVal, radioId, boeVO);
								          }
								        }
								      }
								      if (invCount > 0) {
								        result = "success";
								      }
								    }
								    catch (Exception e)
								    {
								      throwDAOException(e);
								    }
								    finally
								    {
								      closeSqlRefferance(rs, loggableStatement, con);
								    }
								    logger.info("Exiting Method");
								    return result;
								  }
								  private int updateBillData(Connection con, BoeVO boeVO)
								    throws DAOException
								  {
								    logger.info("Entering Method");
								    LoggableStatement pst1 = null;
								    int count = 0;
								    CommonMethods commonMethods = null;
								    try
								    {
								      HttpSession session = ServletActionContext.getRequest().getSession();
								      String loginedUserId = (String)session.getAttribute("loginedUserId");
								      if (con == null) {
								        con = DBConnectionUtility.getConnection();
								      }
								      commonMethods = new CommonMethods();
								      String sqlQuery = " UPDATE ETT_BOE_PAYMENT SET BOE_PAYMENT_BP_BOE_DT = TO_DATE(?,'DD-MM-YYYY'), BOE_PAYMENT_BP_PAY_FC_AMT =?, BOE_PAYMENT_BP_PAY_ENDORSE_AMT = TO_NUMBER(BOE_PAYMENT_BP_PAY_ENDORSE_AMT) + TO_NUMBER(?), BOE_PAYMENT_BP_PAY_EDS_FC_AMT  = TO_NUMBER(BOE_PAYMENT_BP_PAY_EDS_FC_AMT) + TO_NUMBER (?), BOE_PAYMENT_BP_PAY_OS_FC_AMT  = ?, BOE_PAYMENT_BP_PAY_FULL_YN = ?, BOE_PAYMENT_BP_BOE_CCY = ?, BOE_ENTRY_AMT=?, ENDORSED_BOE_AMT = TO_NUMBER(ENDORSED_BOE_AMT) + TO_NUMBER(?), PORT_CODE =?, USERID =?,STATUS =?, REMARKS =?, THIRD_PARTY_PAYMENT = ?, CHANGED_IE_CODE = ?, BOE_TRANS_TYPE = ?, PAGETYPE = ?, CREATEDDATE = SYSTIMESTAMP, BES_IND = ?, PRE_ENDORSED_AMT = TO_NUMBER(?) WHERE BOE_PAYMENT_BP_PAY_REF = ? AND BOE_PAYMENT_BP_PAY_PART_REF = ? AND BOE_PAYMENT_BP_BOE_NO =? AND TO_CHAR(BOE_PAYMENT_BP_BOE_DT,'DD/MM/YYYY') = ? AND PORT_CODE = ?";

								 
								 
								 
								 
								 
								 
								 
								 
								 
								 
								 
								 
								      pst1 = new LoggableStatement(con, sqlQuery);
								      pst1.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
								      pst1.setString(2, CommonMethods.setDefaultAmount(boeVO.getPaymentAmount()));
								      pst1.setString(3, CommonMethods.setDefaultAmount(boeVO.getAllocAmt()));
								      pst1.setString(4, CommonMethods.setDefaultAmount(boeVO.getAllocAmt()));
								      pst1.setString(5, CommonMethods.setDefaultAmount(boeVO.getOutAmt()));
								      pst1.setString(6, commonMethods.getEmptyIfNull(boeVO.getFullyAlloc()).trim());
								      pst1.setString(7, commonMethods.getEmptyIfNull(boeVO.getPaymentCurr()).trim());
								      pst1.setString(8, commonMethods.getEmptyIfNull(boeVO.getBillAmt()).trim());
								      pst1.setString(9, commonMethods.getEmptyIfNull(boeVO.getPortCode().trim()));
								      pst1.setString(10, loginedUserId);
								      pst1.setString(11, "P");
								      pst1.setString(12, commonMethods.getEmptyIfNull(boeVO.getRemarks()).trim());
								      pst1.setString(13, commonMethods.getEmptyIfNull(boeVO.getThrdParty()).trim());
								      pst1.setString(14, commonMethods.getEmptyIfNull(boeVO.getIeCodeChange()).trim());
								      pst1.setString(15, commonMethods.getEmptyIfNull(boeVO.getTransType()).trim());
								      pst1.setString(16, "M");
								      pst1.setString(17, commonMethods.getEmptyIfNull(boeVO.getBoeBesMBInd()).trim());
								      pst1.setString(18, CommonMethods.setDefaultAmount(boeVO.getAllocAmt()));
								      pst1.setString(19, commonMethods.getEmptyIfNull(boeVO.getPaymentRefNo()).trim());
								      pst1.setString(20, commonMethods.getEmptyIfNull(boeVO.getPartPaymentSlNo()).trim());
								      pst1.setString(21, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
								      pst1.setString(22, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
								      pst1.setString(23, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
								      logger.info("updateBillData For many Bill: " + pst1.getQueryString());
								      count = pst1.executeUpdate();
								    }
								    catch (SQLException e)
								    {
								      throwDAOException(e);
								    }
								    finally
								    {
								      closePreparedStatement(pst1);
								    }
								    logger.info("Exiting Method");
								    return count;
								  }
								  public int storeInvoiceData(Connection con, String[] radioId, BoeVO boeVO)
								    throws DAOException
								  {
								    logger.info("Entering Method");
								    int count = 0;
								    int iCount = 0;
								    String sPaySrl = "";
								    double dInvAmt = 0.0D;
								    double dRelOrmAmt = 0.0D;
								    String sSequence = "";
								    CommonMethods commonMethods = null;
								    SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
								    try
								    {
								      if (con == null) {
								        con = DBConnectionUtility.getConnection();
								      }
								      commonMethods = new CommonMethods();
								      for (int i = 0; i < radioId.length; i++)
								      {
								        String[] a = radioId[i].split(":");
								        logger.info("radio first value-----------------a[0]-----" + a[0]);
								        logger.info("radio first value-----------------a[0]-----" + a[0]);
								        logger.info("radio first value-----------------a[0]-----" + a[1]);
								        logger.info("radio first value-----------------a[0]-----" + a[1]);
								      }
								      if (radioId != null) {
								        for (int i = 0; i < radioId.length; i++)
								        {
								          String[] a = radioId[i].split(":");
								          String invSerialNo = commonMethods.getEmptyIfNull(a[0]).trim();
								          String invNo = commonMethods.getEmptyIfNull(a[1]).trim();

								 
								 
								 
								 
								 
								 
								          String sQuery = "SELECT INVPAY.INV_AMT, INVPAY.REAL_ORM_AMT, INVPAY.PAYMENT_SRL FROM ETT_BOE_INV_PAYMENT INVPAY, ETT_INVOICE_DETAILS INVDET WHERE INVPAY.BOE_NO = ? AND INVPAY.BOE_DATE = TO_DATE(?,'DD/MM/YYYY') AND INVPAY.PORTCODE = ? AND INVPAY.INV_NO   = ? AND INVPAY.BOE_NO   = INVDET.BOE_NUMBER AND INVPAY.BOE_DATE = INVDET.BOE_DATE AND INVPAY.PORTCODE = INVDET.BOE_PORT_OF_DISCHARGE AND INVPAY.INV_NO   = INVDET.INVOICE_NUMBER AND INVDET.STATUS  = 'A' AND INVPAY.PAYMENT_SRL = (SELECT MAX(PAYMENT_SRL) FROM ETT_BOE_INV_PAYMENT WHERE BOE_NO = ? AND BOE_DATE = TO_DATE(?,'DD/MM/YYYY') AND PORTCODE = ?)";
								          LoggableStatement pst5 = new LoggableStatement(con, sQuery);
								          pst5.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
								          pst5.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
								          pst5.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
								          pst5.setString(4, invNo);
								          pst5.setString(5, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
								          pst5.setString(6, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
								          pst5.setString(7, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
								          logger.info("Multi Payments Query --> " + pst5.getQueryString());
								          ResultSet rs5 = pst5.executeQuery();
								          if (rs5.next())
								          {
								            dInvAmt = rs5.getDouble(2);
								            dRelOrmAmt = rs5.getDouble(3);
								            sPaySrl = rs5.getString(4);
								          }
								          if (dInvAmt - dRelOrmAmt > 0.0D)
								          {
								            Date date = new Date();
								            String sDate = sdf.format(date);
								            sSequence = ValidationUtility.dateConvertToDDMMYYYString(sDate, sPaySrl);
								            logger.info("The payment sequence is : =====================>>> sSequence : " + sSequence);
								          }
								          String query = "SELECT INVOICE_SERIAL_NUMBER AS INV_SNO,INVOICE_NUMBER AS INVNO,INVOICE_TERMS_OF_INVOICE AS TERM_INV, NVL(INVOICE_FOBAMOUNT,0) AS TOT_INV_AMT, INVOICE_FOBCURRENCY AS INV_CURR FROM ETT_INVOICE_DETAILS WHERE  BOE_NUMBER = ? AND TO_CHAR(BOE_DATE,'DD/MM/YYYY') = ? AND BOE_PORT_OF_DISCHARGE = ? AND INVOICE_SERIAL_NUMBER = ? AND INVOICE_NUMBER = ? ";

								 
								 
								          LoggableStatement pst1 = new LoggableStatement(con, query);
								          pst1.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
								          pst1.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
								          pst1.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
								          pst1.setString(4, invSerialNo);
								          pst1.setString(5, invNo);
								          logger.info("Multi Payments Query --> 111 " + pst1.getQueryString());
								          ResultSet rs1 = pst1.executeQuery();
								          if (rs1.next())
								          {
								            logger.info("This is Inside of If While condition");
								            String termsOfInvoice = commonMethods.getEmptyIfNull(rs1.getString("TERM_INV")).trim();
								            String totalInvAmt = commonMethods.getEmptyIfNull(rs1.getString("TOT_INV_AMT")).trim();
								            String invCurr = commonMethods.getEmptyIfNull(rs1.getString("INV_CURR")).trim();
								            double realInvAmt = commonMethods.convertToDouble(boeVO.getAllocAmt());
								            double exRate = commonMethods.convertToDouble(boeVO.getBoeExRate());
								            double realInvAmtIC = realInvAmt * exRate;
								            realInvAmtIC = Math.round(realInvAmtIC * 100.0D) / 100.0D;
								            String invSeqNo = null;
								            String getInvSeqQuery = "SELECT RINV_SEQNO FROM ETT_BOE_INV_PAYMENT WHERE BOE_NO = ?  AND TO_CHAR(BOE_DATE,'DD/MM/YYYY') = ? AND PORTCODE = ? AND PAYMENT_REF = ? AND EVENT_REF = ? AND INV_SNO = ? AND INV_NO = ? AND TRIM(EOD_STATUS) IS NULL ";

								 
								 
								            LoggableStatement pst3 = new LoggableStatement(con, getInvSeqQuery);
								            pst3.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
								            pst3.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
								            pst3.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
								            pst3.setString(4, commonMethods.getEmptyIfNull(boeVO.getPaymentRefNo()).trim());
								            pst3.setString(5, commonMethods.getEmptyIfNull(boeVO.getPartPaymentSlNo()).trim());
								            pst3.setString(6, invSerialNo);
								            pst3.setString(7, invNo);
								            logger.info("Multi Payments Query --> 222 " + pst3.getQueryString());
								            ResultSet rs3 = pst3.executeQuery();
								            if (rs3.next()) {
								              invSeqNo = rs3.getString("RINV_SEQNO");
								            }
								            closeSqlRefferance(rs3, pst3);
								            if (commonMethods.isNull(invSeqNo))
								            {
								              String seqQuery = "SELECT ETT_BES_SEQ_NO AS BINV_SEQ FROM DUAL";
								              LoggableStatement pst4 = new LoggableStatement(con, seqQuery);
								              ResultSet rs4 = pst4.executeQuery();
								              if (rs4.next()) {
								                invSeqNo = rs4.getString("BINV_SEQ");
								              }
								              closeSqlRefferance(rs4, pst4);
								            }
								            String insertQuery = "INSERT INTO ETT_BOE_INV_PAYMENT(INV_SNO,INV_NO,INV_AMT,REAL_AMT,REAL_ORM_AMT, INVOICECURR,TERMS_OF_INVOICE,BOE_NO,BOE_DATE,PORTCODE,PAYMENT_REF,EVENT_REF,RINV_SEQNO, REAL_MOD_AMT, REAL_ORM_MOD_AMT) VALUES(?,?,?,?,?,?,?,?,TO_DATE(?,'DD-MM-YYYY'),?,?,?,?, ?, ?) ";

								 
								 
								            LoggableStatement pst2 = new LoggableStatement(con, insertQuery);
								            pst2.setString(1, invSerialNo);
								            pst2.setString(2, invNo);
								            pst2.setString(3, totalInvAmt);
								            pst2.setString(4, String.valueOf(realInvAmt));
								            pst2.setString(5, String.valueOf(realInvAmtIC));
								            pst2.setString(6, invCurr);
								            pst2.setString(7, termsOfInvoice);
								            pst2.setString(8, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
								            pst2.setString(9, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
								            pst2.setString(10, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
								            pst2.setString(11, commonMethods.getEmptyIfNull(boeVO.getPaymentRefNo()).trim());
								            pst2.setString(12, commonMethods.getEmptyIfNull(boeVO.getPartPaymentSlNo()).trim());
								            pst2.setString(13, commonMethods.getEmptyIfNull(invSeqNo).trim());
								            pst2.setString(14, String.valueOf(realInvAmt));
								            pst2.setString(15, String.valueOf(realInvAmtIC));
								            count = pst2.executeUpdate();
								            logger.info("Value Inerted----------------->" + count);
								            logger.info("Value Inerted----------------->" + count);
								            logger.info("Multi Payments Query --> 333 " + pst2.getQueryString());
								            closePreparedStatement(pst2);
								          }
								          closeSqlRefferance(rs1, pst1);
								        }
								      }
								    }
								    catch (Exception e)
								    {
								      logger.info("storeInvoiceData----Exception---------" + e.getMessage());
								      logger.info("storeInvoiceData----Exception---------" + e.getMessage());
								    }
								    logger.info("Exiting Method");
								    return count;
								  }
								  public BoeVO retriveDataFromMBPayTable(BoeVO boeVO)
								    throws DAOException
								  {
								    logger.info("Entering Method");
								    double availableAmt = 0.0D;

								 
								    String sBoeNum = null;
								    String sBoeDate = null;
								    String sPortCode = null;
								    String sQuery = null;
								    Connection con = null;
								    LoggableStatement pst = null;
								    LoggableStatement pst1 = null;
								    ResultSet rs = null;
								    ResultSet rs1 = null;
								    ArrayList<TransactionVO> invoiceMBList = null;
								    ArrayList<MeditorVO> paymentMBList = null;
								    ArrayList<TransactionVO> boePaymentMBList = null;
								    CommonMethods commonMethods = null;
								    try
								    {
								      con = DBConnectionUtility.getConnection();
								      commonMethods = new CommonMethods();
								      sQuery = "";
								      sQuery = "SELECT DISTINCT BP.BOE_PAYMENT_BP_BOE_NO AS BOE_PAYMENT_BP_BOE_NO, TO_CHAR(BP.BOE_PAYMENT_BP_BOE_DT,'DD/MM/YYYY') AS BOE_PAYMENT_BP_BOE_DT, BP.PORT_CODE AS PORT_CODE, BP.BOE_TRANS_TYPE AS BOE_TRANS_TYPE, BP.BOE_PAYMENT_CIF_NAME AS BOE_PAYMENT_CIF_NAME, BP.BOE_PAYMENT_CIF_ID AS BOE_PAYMENT_CIF_ID, BP.IECODE AS IE_CODE, BP.ADCODE AS AD_CODE, BP.CHANGED_IE_CODE AS CHANGED_IE_CODE, BP.IE_ADDRESS AS IE_ADDRESS, BP.IE_PAN AS IE_PAN, BP.BOE_PAYMENT_BP_BOE_CCY AS BOE_PAYMENT_BP_BOE_CCY, NVL(TRIM(BP.BOE_ENTRY_AMT), '0') AS BOE_ENTRY_AMT, NVL(TRIM(BP.BOE_PAYMENT_BP_PAY_FULL_YN),' ') AS BOE_PAYMENT_BP_PAY_FULL_YN, NVL(TRIM(BP.BOE_IGMNUMBER),'') AS BOE_IGMNUMBER, TO_CHAR(BP.BOE_IGMDATE,'DD/MM/YYYY') AS BOE_IGMDATE, NVL(TRIM(BP.BOE_HAWB_HBLNUMBER),'') AS BOE_HAWB_HBLNUMBER, TO_CHAR(BP.BOE_HAWB_HBLDATE,'DD/MM/YYYY') AS BOE_HAWB_HBLDATE, NVL(TRIM(BP.BOE_MAWB_MBLNUMBER),'') AS BOE_MAWB_MBLNUMBER, TO_CHAR(BP.BOE_MAWB_MBLDATE,'DD/MM/YYYY') AS BOE_MAWB_MBLDATE, DECODE(IMPORT_AGENCY, 1, 'Customs', 'SEZ') AS IMPORT_AGENCY, BP.PORT_OF_SHIPMENT AS PORT_OF_SHIPMENT, DECODE(BP.G_P, 'P','Private','G','Goverment') AS G_P, DECODE(BES_IND, 1, 'NEW', 3, 'CANCEL') AS BES_IND, DECODE(BP.RECORD_INDICATOR, 1, 'New', 2, 'Amendment', 3, 'Cancel') AS RECORD_INDICATOR, BP.REMARKS AS REMARKS FROM ETT_BOE_PAYMENT BP WHERE BP.BOE_PAYMENT_BP_BOE_NO = ? AND TO_CHAR(BP.BOE_PAYMENT_BP_BOE_DT,'DD/MM/YYYY') = ? AND BP.PORT_CODE = ? AND BP.STATUS = 'P'";
								      pst = new LoggableStatement(con, sQuery);
								      pst.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
								      pst.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
								      pst.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
								      logger.info("Multi Payments Query --> " + pst.getQueryString());
								      rs = pst.executeQuery();
								      if (rs.next())
								      {
								        boeVO.setBoeNo(commonMethods.getEmptyIfNull(rs.getString("BOE_PAYMENT_BP_BOE_NO").trim()));
								        boeVO.setBoeDate(commonMethods.getEmptyIfNull(rs.getString("BOE_PAYMENT_BP_BOE_DT").trim()));
								        boeVO.setPortCode(commonMethods.getEmptyIfNull(rs.getString("PORT_CODE").trim()));
								        boeVO.setTransType(commonMethods.getEmptyIfNull(rs.getString("BOE_TRANS_TYPE").trim()));
								        boeVO.setCustName(commonMethods.getEmptyIfNull(rs.getString("BOE_PAYMENT_CIF_NAME").trim()));
								        boeVO.setCifNo(commonMethods.getEmptyIfNull(rs.getString("BOE_PAYMENT_CIF_ID").trim()));
								        boeVO.setIeCode(commonMethods.getEmptyIfNull(rs.getString("IE_CODE").trim()));
								        boeVO.setAdCode(commonMethods.getEmptyIfNull(rs.getString("AD_CODE").trim()));
								        boeVO.setIeCodeChange(commonMethods.getEmptyIfNull(rs.getString("CHANGED_IE_CODE")));
								        boeVO.setIeadd(commonMethods.getEmptyIfNull(rs.getString("IE_ADDRESS").trim()));
								        boeVO.setIepan(commonMethods.getEmptyIfNull(rs.getString("IE_PAN").trim()));
								        boeVO.setBillCurrency(commonMethods.getEmptyIfNull(rs.getString("BOE_PAYMENT_BP_BOE_CCY").trim()));

								 
								 
								        boeVO.setBillAmt(CommonMethods.setCheckValue(rs.getString("BOE_ENTRY_AMT")));
								        BigDecimal availAmt = BigDecimal.valueOf(availableAmt).setScale(2, RoundingMode.HALF_UP);
								        boeVO.setActualEndorseAmt(String.valueOf(availAmt));
								        logger.info("Actual Endorsement Amount------------------>" + availAmt);
								        boeVO.setIgmNo(commonMethods.getEmptyIfNull(rs.getString("BOE_IGMNUMBER")));
								        boeVO.setIgmDate(commonMethods.getEmptyIfNull(rs.getString("BOE_IGMDATE")));
								        boeVO.setHblNo(commonMethods.getEmptyIfNull(rs.getString("BOE_HAWB_HBLNUMBER")));
								        boeVO.setHblDate(commonMethods.getEmptyIfNull(rs.getString("BOE_HAWB_HBLDATE")));
								        boeVO.setMblNo(commonMethods.getEmptyIfNull(rs.getString("BOE_MAWB_MBLNUMBER")));
								        boeVO.setMblDate(commonMethods.getEmptyIfNull(rs.getString("BOE_MAWB_MBLDATE")));
								        boeVO.setImAgency(commonMethods.getEmptyIfNull(rs.getString("IMPORT_AGENCY")));
								        boeVO.setPos(commonMethods.getEmptyIfNull(rs.getString("PORT_OF_SHIPMENT")));
								        boeVO.setGovprv(commonMethods.getEmptyIfNull(rs.getString("G_P")));
								        boeVO.setBoeBesSBInd(commonMethods.getEmptyIfNull(rs.getString("BES_IND")));
								        boeVO.setRecInd(commonMethods.getEmptyIfNull(rs.getString("RECORD_INDICATOR")));
								        boeVO.setRemarks(commonMethods.getEmptyIfNull(rs.getString("REMARKS")));
								      }
								      BoeVO boeVO1 = new BoeVO();
								      boeVO1.setBoeNo(sBoeNum);
								      boeVO1.setBoeDate(sBoeDate);
								      boeVO1.setPortCode(sPortCode);
								      int totalBoeCount = 0;

								 
								 
								      String sqlQuery = "SELECT BP.CHANGED_IE_CODE AS CHANGED_IE_CODE, NVL(TRIM(BP.BOE_PAYMENT_BP_BOE_NO), ' ') AS BOE_PAYMENT_BP_BOE_NO,\tTO_CHAR(TO_DATE(BP.BOE_PAYMENT_BP_BOE_DT, 'dd-mm-yy'),'dd/mm/yyyy') AS BOE_PAYMENT_BP_BOE_DT, NVL(TRIM(BP.BOE_ENTRY_AMT), '0') AS BOE_ENTRY_AMT, NVL(TRIM(BP.BOE_PAYMENT_BP_BOE_CCY), ' ') AS BOE_ENTRY_CURR, TRIM(BP.PORT_CODE) AS PORT_CODE, BP.STATUS,BP.REMARKS, BP.EXCHANGE_RATE  FROM ETT_BOE_PAYMENT BP WHERE BP.BOE_PAYMENT_BP_BOE_NO = ? AND TO_CHAR(BP.BOE_PAYMENT_BP_BOE_DT,'DD/MM/YYYY') =? AND BP.PORT_CODE = ? AND BP.STATUS = 'P' ORDER BY BP.CREATEDDATE DESC ";

								 
								 
								 
								 
								 
								      pst1 = new LoggableStatement(con, sqlQuery);
								      pst1.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
								      pst1.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
								      pst1.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
								      logger.info("RetriveDataBasedOnBillNO For Many Bill: " + pst.getQueryString());
								      rs1 = pst1.executeQuery();
								      boolean isAvailable = false;
								      if (rs1.next())
								      {
								        isAvailable = true;
								        boeVO.setBillAmt(commonMethods.toDouble(rs1.getString("BOE_ENTRY_AMT")));
								        boeVO.setBillCurrency(commonMethods.getEmptyIfNull(rs1.getString("BOE_ENTRY_CURR")).trim());

								 
								 
								        boeVO.setExRate(commonMethods.getEmptyIfNull(rs1.getString("EXCHANGE_RATE")).trim());

								 
								 
								        boeVO = fetchTotalEndoreAmount(boeVO);
								        double totalEndAmt = commonMethods.convertToDouble(boeVO.getTotalEndorseAmt());
								        double boeAmt = commonMethods.convertToDouble(boeVO.getBillAmt());
								        double availableAmt1 = boeAmt - totalEndAmt;
								        BigDecimal availAmt = BigDecimal.valueOf(availableAmt1).setScale(2, RoundingMode.HALF_UP);
								        boeVO.setActualEndorseAmt(String.valueOf(availAmt));
								        boeVO.setActualEndorseAmt_temp(String.valueOf(availAmt));
								        logger.info("Actual Endorsement--------------->" + availAmt);
								        boeVO.setRemarks(commonMethods.getEmptyIfNull(rs1.getString("REMARKS")).trim());
								        boeVO.setIeCodeChange(commonMethods.getEmptyIfNull(rs1.getString("CHANGED_IE_CODE")).trim());
								      }
								      invoiceMBList = fetchInvoiceListToModify(con, boeVO);
								      if ((invoiceMBList.size() > 0) && (!invoiceMBList.isEmpty())) {
								        boeVO.setInvoiceList(invoiceMBList);
								      }
								      paymentMBList = fetchDataMultiPayments(con, boeVO);
								      if ((paymentMBList.size() > 0) && (!paymentMBList.isEmpty())) {
								        boeVO.setPaymentList(paymentMBList);
								      }
								      boePaymentMBList = fetchBOEPaymentList(con, boeVO);
								      if ((boePaymentMBList.size() > 0) && (!boePaymentMBList.isEmpty())) {
								        boeVO.setBoePaymentList(boePaymentMBList);
								      }
								    }
								    catch (Exception e)
								    {
								      e.printStackTrace();
								    }
								    finally
								    {
								      closeSqlRefferance(rs, pst, con);
								    }
								    logger.info("Exiting Method");
								    return boeVO;
								  }
								  public ArrayList<TransactionVO> fetchInvoiceListToModify(Connection con, BoeVO boeVO)
								  {
								    logger.info("Entering Method");
								    String query = null;
								    LoggableStatement pst = null;
								    ResultSet rs = null;
								    CommonMethods commonMethods = null;
								    ArrayList<TransactionVO> invoiceList = null;
								    try
								    {
								      if (con == null) {
								        con = DBConnectionUtility.getConnection();
								      }
								      commonMethods = new CommonMethods();
								      invoiceList = new ArrayList();
								      double totalEndorseAmt = 0.0D;
								      query = "SELECT INV_SERIAL_NO, INV_NO, INV_TERMS, FOB_AMT AS FOB_AMT, FOB_CURR, FRI_AMT AS FRI_AMT, FRI_CURR, INS_AMT AS INS_AMT, INS_CURR, SUM(REAL_AMT) AS REAL_AMT,  SUM(REAL_ORM_AMT)     AS REAL_ORM_AMT, SUM(REAL_MOD_AMT)  AS REAL_MOD_AMT, SUM(REAL_ORM_MOD_AMT) AS REAL_ORM_MOD_AMT, SUM(CLO_AMTIC) AS CLO_AMTIC FROM (SELECT INV_SERIAL_NO INV_SERIAL_NO, INV_NO INV_NO,INV_TERMS INV_TERMS,FOB_AMT FOB_AMT, FOB_CURR FOB_CURR, FRI_AMT FRI_AMT, FRI_CURR FRI_CURR, INS_AMT INS_AMT, INS_CURR INS_CURR, 0 REAL_AMT, 0 AS REAL_ORM_AMT, REAL_MOD_AMT REAL_MOD_AMT, REAL_ORM_MOD_AMT REAL_ORM_MOD_AMT, CLOSURE_AMTIC CLO_AMTIC FROM ETTV_BOE_MOD_INV_DETAILS WHERE BOE_NUMBER = ? AND TO_CHAR(BOE_DATE,'DD/MM/YYYY') = ? AND BOE_PORT_OF_DISCHARGE = ? UNION ALL SELECT INV_SERIAL_NO INV_SERIAL_NO, INV_NO INV_NO, INV_TERMS INV_TERMS, FOB_AMT FOB_AMT, FOB_CURR FOB_CURR, FRI_AMT FRI_AMT, FRI_CURR FRI_CURR, INS_AMT INS_AMT, INS_CURR INS_CURR,  REAL_AMT REAL_AMT,  REAL_AMTIC AS REAL_ORM_AMT, 0 REAL_MOD_AMT, 0 AS REAL_ORM_MOD_AMT, CLOSURE_AMTIC CLO_AMTIC FROM ETTV_BOE_INV_DETAIL WHERE BOE_NUMBER = ? AND TO_CHAR(BOE_DATE,'DD/MM/YYYY') = ? AND BOE_PORT_OF_DISCHARGE = ?) GROUP BY INV_SERIAL_NO, INV_NO, INV_TERMS,FOB_AMT,FOB_CURR, FRI_AMT, FRI_CURR, INS_AMT, INS_CURR ORDER BY TO_NUMBER(INV_SERIAL_NO) ASC";
								      pst = new LoggableStatement(con, query);
								      pst.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
								      pst.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
								      pst.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
								      pst.setString(4, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
								      pst.setString(5, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
								      pst.setString(6, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
								      logger.info("Invoice Query --> " + pst.getQueryString());
								      rs = pst.executeQuery();
								      while (rs.next())
								      {
								        TransactionVO invoiceVO = new TransactionVO();
								        invoiceVO.setInvoiceSerialNumber(commonMethods.getEmptyIfNull(rs.getString("INV_SERIAL_NO")).trim());
								        invoiceVO.setInvoiceNumber(commonMethods.getEmptyIfNull(rs.getString("INV_NO")).trim());
								        invoiceVO.setTermsofInvoice(commonMethods.getEmptyIfNull(rs.getString("INV_TERMS")).trim());
								        invoiceVO.setInvoiceAmount(commonMethods.getEmptyIfNull(rs.getString("FOB_AMT")).trim());
								        invoiceVO.setInvoiceCurr(commonMethods.getEmptyIfNull(rs.getString("FOB_CURR")).trim());
								        invoiceVO.setFreightAmount(commonMethods.getEmptyIfNull(rs.getString("FRI_AMT")).trim());
								        invoiceVO.setFreightCurrencyCode(commonMethods.getEmptyIfNull(rs.getString("FRI_CURR")).trim());
								        invoiceVO.setInsuranceAmount(commonMethods.getEmptyIfNull(rs.getString("INS_AMT")).trim());
								        invoiceVO.setInsuranceCurrencyCode(commonMethods.getEmptyIfNull(rs.getString("INS_CURR")).trim());
								        invoiceVO.setAlreadyRealizedAmt(commonMethods.getEmptyIfNull(rs.getString("REAL_AMT")).trim());
								        invoiceVO.setAlreadyRealizedAmtIC(commonMethods.getEmptyIfNull(rs.getString("REAL_ORM_AMT")).trim());
								        invoiceVO.setRealizedAmount(commonMethods.getEmptyIfNull(rs.getString("REAL_MOD_AMT")).trim());
								        invoiceVO.setRealizedAmountIC(commonMethods.getEmptyIfNull(rs.getString("REAL_ORM_MOD_AMT")).trim());
								        String tempVal = commonMethods.getEmptyIfNull(rs.getString("INV_SERIAL_NO")).trim() + 
								          ":" + commonMethods.getEmptyIfNull(rs.getString("INV_NO")).trim();
								        invoiceVO.setUtilityRefNo(tempVal);
								        double fobAmt = commonMethods.convertToDouble(rs.getString("FOB_AMT"));
								        double friAmt = commonMethods.convertToDouble(rs.getString("FRI_AMT"));
								        double insAmt = commonMethods.convertToDouble(rs.getString("INS_AMT"));

								 
								 
								        double totalAmt = fobAmt;
								        double totalInvAmt = Math.round(totalAmt * 100.0D) / 100.0D;
								        BigDecimal toInvAmt = BigDecimal.valueOf(totalInvAmt).setScale(2, RoundingMode.HALF_UP);
								        invoiceVO.setTotalInvAmt(String.valueOf(toInvAmt));

								 
								        double realAmtIC = commonMethods.convertToDouble(rs.getString("REAL_ORM_AMT"));
								        double closureAmtIc = commonMethods.convertToDouble(rs.getString("CLO_AMTIC"));
								        invoiceVO.setClosureAmt(String.valueOf(closureAmtIc));
								        double totalPaidAmt = realAmtIC + closureAmtIc;
								        totalPaidAmt = Math.round(totalPaidAmt * 100.0D) / 100.0D;
								        double outAmtIC = totalInvAmt - totalPaidAmt;
								        outAmtIC = Math.round(outAmtIC * 100.0D) / 100.0D;
								        BigDecimal toOutAmt = BigDecimal.valueOf(outAmtIC).setScale(2, RoundingMode.HALF_UP);
								        invoiceVO.setOutAmtIC(String.valueOf(toOutAmt));
								        double endorseAmt = commonMethods.convertToDouble(rs.getString("REAL_ORM_AMT"));
								        totalEndorseAmt += endorseAmt;
								        totalEndorseAmt = Math.round(totalEndorseAmt * 100.0D) / 100.0D;
								        invoiceList.add(invoiceVO);
								      }
								      BigDecimal toEndAmt = BigDecimal.valueOf(totalEndorseAmt).setScale(2, RoundingMode.HALF_UP);
								      boeVO.setEndorseAmt(String.valueOf(toEndAmt));
								    }
								    catch (Exception e)
								    {
								      e.printStackTrace();
								    }
								    finally
								    {
								      closeSqlRefferance(rs, pst);
								    }
								    logger.info("Exiting Method");
								    return invoiceList;
								  }
							
								  private int updateModifiedBillData(Connection con, BoeVO boeVO)
										    throws DAOException
										  {
										    logger.info("Entering Method");
										    int count = 0;
										    String sBOEPayQuery = null;
										    double dPayFcAmt = 0.0D;
										    double dPayEndorseAmt = 0.0D;
										    double dPayEDSFcAmt = 0.0D;
										    double dPayOSFcAmt = 0.0D;
										    double dEndorsedAmt = 0.0D;
										    double dPreEndorsedAmt = 0.0D;
										    double dAllocatAmount = 0.0D;
										    LoggableStatement pst = null;
										    ResultSet rs = null;
										    CommonMethods commonMethods = null;
										    try
										    {
										      HttpSession session = ServletActionContext.getRequest().getSession();
										      String loginedUserId = (String)session.getAttribute("loginedUserId");
										      if (con == null) {
										        con = DBConnectionUtility.getConnection();
										      }
										      commonMethods = new CommonMethods();

										 
										      sBOEPayQuery = "SELECT BOE_PAYMENT_BP_PAY_FC_AMT, BOE_PAYMENT_BP_PAY_ENDORSE_AMT, BOE_PAYMENT_BP_PAY_EDS_FC_AMT, BOE_PAYMENT_BP_PAY_OS_FC_AMT, BOE_ENTRY_AMT, ENDORSED_BOE_AMT, PRE_ENDORSED_AMT FROM ETT_BOE_PAYMENT WHERE BOE_PAYMENT_BP_PAY_REF = ? AND BOE_PAYMENT_BP_PAY_PART_REF = ? AND BOE_PAYMENT_BP_BOE_NO = ? AND TO_CHAR(BOE_PAYMENT_BP_BOE_DT,'DD/MM/YYYY') = ? AND PORT_CODE = ? AND STATUS = 'P'";
										      logger.info("This 201");
										      pst = new LoggableStatement(con, sBOEPayQuery);
										      pst.setString(1, boeVO.getPaymentRefNo());
										      pst.setString(2, boeVO.getPartPaymentSlNo());
										      pst.setString(3, boeVO.getBoeNo());
										      pst.setString(4, boeVO.getBoeDate());
										      pst.setString(5, boeVO.getPortCode());
										      rs = pst.executeQuery();
										      if (rs.next())
										      {
										        dPayFcAmt = commonMethods.convertToDouble(rs.getString("BOE_PAYMENT_BP_PAY_FC_AMT"));
										        dPayEndorseAmt = commonMethods.convertToDouble(rs.getString("BOE_PAYMENT_BP_PAY_ENDORSE_AMT"));
										        dPayEDSFcAmt = commonMethods.convertToDouble(rs.getString("BOE_PAYMENT_BP_PAY_EDS_FC_AMT"));
										        dPayOSFcAmt = commonMethods.convertToDouble(rs.getString("BOE_PAYMENT_BP_PAY_OS_FC_AMT"));
										        dEndorsedAmt = commonMethods.convertToDouble(rs.getString("ENDORSED_BOE_AMT"));
										        dPreEndorsedAmt = commonMethods.convertToDouble(rs.getString("PRE_ENDORSED_AMT"));
										      }
										      logger.info("This 202");
										      closeSqlRefferance(rs, pst);
										      dAllocatAmount = commonMethods.convertToDouble(boeVO.getAllocAmt());
										      if (dPreEndorsedAmt > dAllocatAmount)
										      {
										        double dTempValue = dPreEndorsedAmt - dAllocatAmount;
										        dPayFcAmt -= dTempValue;
										        dPayEndorseAmt -= dTempValue;
										        dPayEDSFcAmt -= dTempValue;
										        dPayOSFcAmt -= dTempValue;
										        dEndorsedAmt -= dTempValue;
										        dPreEndorsedAmt -= dTempValue;
										      }
										      if (dPreEndorsedAmt < dAllocatAmount)
										      {
										        double dTempValue = dAllocatAmount - dPreEndorsedAmt;
										        dPayFcAmt += dTempValue;
										        dPayEndorseAmt += dTempValue;
										        dPayEDSFcAmt += dTempValue;
										        dPayOSFcAmt += dTempValue;
										        dEndorsedAmt += dTempValue;
										        dPreEndorsedAmt += dTempValue;
										      }
										      logger.info("This 203");
										      String sqlQuery = " UPDATE ETT_BOE_PAYMENT SET BOE_PAYMENT_BP_BOE_DT = TO_DATE(?,'DD-MM-YYYY'), BOE_PAYMENT_BP_PAY_FC_AMT = TO_NUMBER(?), BOE_PAYMENT_BP_PAY_ENDORSE_AMT = TO_NUMBER(?), BOE_PAYMENT_BP_PAY_EDS_FC_AMT  = TO_NUMBER (?), BOE_PAYMENT_BP_PAY_OS_FC_AMT  = TO_NUMBER(?), BOE_PAYMENT_BP_PAY_FULL_YN = ?, BOE_PAYMENT_BP_BOE_CCY = ?, BOE_ENTRY_AMT=?, ENDORSED_BOE_AMT = TO_NUMBER(?), PORT_CODE = ?, USERID = ?,STATUS = ?, REMARKS = ?, THIRD_PARTY_PAYMENT =?, CHANGED_IE_CODE = ?, BOE_TRANS_TYPE =?, PAGETYPE = ?, CREATEDDATE = SYSTIMESTAMP, BES_IND =?, PRE_ENDORSED_AMT = TO_NUMBER(?) WHERE BOE_PAYMENT_BP_PAY_REF = ? AND BOE_PAYMENT_BP_PAY_PART_REF = ? AND BOE_PAYMENT_BP_BOE_NO =? AND TO_CHAR(BOE_PAYMENT_BP_BOE_DT,'DD/MM/YYYY') =? AND PORT_CODE = ?";

										 
										 
										 
										 
										 
										 
										 
										 
										 
										 
										 
										      logger.info("This 204");
										      pst = new LoggableStatement(con, sqlQuery);
										      pst.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
										      pst.setString(2, CommonMethods.setDefaultAmount(dPayFcAmt));
										      pst.setString(3, CommonMethods.setDefaultAmount(dPayEndorseAmt));
										      pst.setString(4, CommonMethods.setDefaultAmount(dPayEndorseAmt));
										      pst.setString(5, CommonMethods.setDefaultAmount(dPayEDSFcAmt));
										      pst.setString(6, CommonMethods.setDefaultAmount(dPayOSFcAmt));
										      pst.setString(7, commonMethods.getEmptyIfNull(boeVO.getFullyAlloc()).trim());
										      pst.setString(8, commonMethods.getEmptyIfNull(boeVO.getPaymentCurr()).trim());
										      pst.setString(9, commonMethods.getEmptyIfNull(boeVO.getBillAmt()).trim());
										      pst.setString(10, CommonMethods.setDefaultAmount(dEndorsedAmt));
										      pst.setString(11, commonMethods.getEmptyIfNull(boeVO.getPortCode().trim()));
										      pst.setString(12, loginedUserId);
										      pst.setString(13, "P");
										      pst.setString(14, commonMethods.getEmptyIfNull(boeVO.getRemarks()).trim());
										      pst.setString(15, commonMethods.getEmptyIfNull(boeVO.getThrdParty()).trim());
										      pst.setString(16, commonMethods.getEmptyIfNull(boeVO.getIeCodeChange()).trim());
										      pst.setString(17, commonMethods.getEmptyIfNull(boeVO.getTransType()).trim());
										      pst.setString(18, "M");
										      pst.setString(19, commonMethods.getEmptyIfNull(boeVO.getBoeBesSBInd()).trim());
										      pst.setString(20, CommonMethods.setDefaultAmount(dPreEndorsedAmt));
										      pst.setString(21, commonMethods.getEmptyIfNull(boeVO.getPaymentRefNo()).trim());
										      pst.setString(22, commonMethods.getEmptyIfNull(boeVO.getPartPaymentSlNo()).trim());
										      pst.setString(23, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
										      pst.setString(24, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
										      pst.setString(25, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());

										 
										      logger.info("updateBillData For many Bill: " + pst.getQueryString());
										      count = pst.executeUpdate();
										    }
										    catch (SQLException e)
										    {
										      throwDAOException(e);
										    }
										    finally
										    {
										      closePreparedStatement(pst);
										    }
										    logger.info("Exiting Method");
										    return count;
										  }
										  public int updateInvoiceData(Connection con, String chkListVal, String[] radioId, BoeVO boeVO)
										  {
										    logger.info("Entering Method");
										    int iRet = 0;
										    double dRealAmt = 0.0D;
										    double dRealOrmAmt = 0.0D;
										    double dRealModAmt = 0.0D;
										    double dOldRealModAmt = 0.0D;
										    double dRealOrmModAmt = 0.0D;
										    String sQuery = null;
										    String sUpdateQuery = null;
										    LoggableStatement pst = null;
										    LoggableStatement pst1 = null;
										    ResultSet rs = null;
										    ResultSet rs1 = null;
										    CommonMethods commonMethods = null;
										    try
										    {
										      if (con == null) {
										        con = DBConnectionUtility.getConnection();
										      }
										      commonMethods = new CommonMethods();
										      String radInvoiceVal = null;
										      if (radInvoiceVal != null)
										      {
										        String[] s = radInvoiceVal.split(":");
										        String[] s1 = chkListVal.split(":");

										 
										 
										 
										 
										 
										 
										        String invSerialNo = commonMethods.getEmptyIfNull(s[0]).trim();
										        String invNo = commonMethods.getEmptyIfNull(s[1]).trim();
										        
										        String realAmt = commonMethods.getEmptyIfNull(s1[2]).trim();
										        double dNewRealAmt = commonMethods.convertToDouble(realAmt);
										        sQuery = "SELECT REAL_AMT, REAL_ORM_AMT, REAL_MOD_AMT, REAL_ORM_MOD_AMT FROM ETT_BOE_INV_PAYMENT WHERE BOE_NO = ? AND BOE_DATE = TO_DATE(?,'DD/MM/YYYY') AND PORTCODE = ? AND INV_SNO = ? AND INV_NO = ? AND EOD_STATUS IS NULL";
										        pst = new LoggableStatement(con, sQuery);
										        pst.setString(1, boeVO.getBoeNo());
										        pst.setString(2, boeVO.getBoeDate());
										        pst.setString(3, boeVO.getPortCode());
										        pst.setString(4, invSerialNo);
										        pst.setString(5, invNo);
										        rs = pst.executeQuery();
										        if (rs.next())
										        {
										          dRealAmt = commonMethods.convertToDouble(rs.getString("REAL_AMT"));
										          dRealOrmAmt = commonMethods.convertToDouble(rs.getString("REAL_ORM_AMT"));
										          dRealModAmt = commonMethods.convertToDouble(rs.getString("REAL_MOD_AMT"));
										          dOldRealModAmt = commonMethods.convertToDouble(rs.getString("REAL_MOD_AMT"));
										          dRealOrmModAmt = commonMethods.convertToDouble(rs.getString("REAL_ORM_MOD_AMT"));
										        }
										        if (dOldRealModAmt < dNewRealAmt)
										        {
										          double dTempValue = dNewRealAmt - dOldRealModAmt;
										          dRealAmt += dTempValue;
										          dRealOrmAmt += dTempValue;
										          dRealModAmt += dTempValue;
										          dRealOrmModAmt += dTempValue;
										        }
										        if (dOldRealModAmt > dNewRealAmt)
										        {
										          double dTempValue = dOldRealModAmt - dNewRealAmt;
										          dRealAmt -= dTempValue;
										          dRealOrmAmt -= dTempValue;
										          dRealModAmt -= dTempValue;
										          dRealOrmModAmt -= dTempValue;
										        }
										        sUpdateQuery = "UPDATE ETT_BOE_INV_PAYMENT SET REAL_AMT = ?, REAL_ORM_AMT = ?, REAL_MOD_AMT = ?, REAL_ORM_MOD_AMT = ? WHERE BOE_NO = ? AND BOE_DATE = TO_DATE(?,'DD/MM/YYYY') AND PORTCODE = ? AND INV_SNO = ? AND INV_NO = ? AND PAYMENT_REF = ? AND EVENT_REF = ? AND EOD_STATUS IS NULL";
										        pst1 = new LoggableStatement(con, sUpdateQuery);
										        pst1.setString(1, String.valueOf(dRealAmt));
										        pst1.setString(2, String.valueOf(dRealOrmAmt));
										        pst1.setString(3, String.valueOf(dRealModAmt));
										        pst1.setString(4, String.valueOf(dRealOrmModAmt));
										        pst1.setString(5, boeVO.getBoeNo());
										        pst1.setString(6, boeVO.getBoeDate());
										        pst1.setString(7, boeVO.getPortCode());
										        pst1.setString(8, invSerialNo);
										        pst1.setString(9, invNo);
										        pst1.setString(10, boeVO.getPaymentRefNo());
										        pst1.setString(11, boeVO.getPartPaymentSlNo());
										        iRet = pst1.executeUpdate();
										      }
										    }
										    catch (Exception e)
										    {
										      logger.info("Exception in is " + e.getMessage());
										      try
										      {
										        pst1.close();
										      }
										      catch (SQLException e)
										      {
										        e.printStackTrace();
										      }
										    }
										    finally
										    {
										      try
										      {
										        pst1.close();
										      }
										      catch (SQLException e)
										      {
										        e.printStackTrace();
										      }
										    }
										    logger.info("Exiting Method");
										    return iRet;
										  }
										  public int deleteBOEMBData(BoeVO boeVO, String[] chkPayList, String chkInvoiceVal)
										  {
										    logger.info("Entering Method");
										    int iRet = 0;
										    double dPayFcAmt = 0.0D;
										    double dPayEndorseAmt = 0.0D;
										    double dPayEDSFcAmt = 0.0D;
										    double dPayOSFcAmt = 0.0D;
										    double dEndorsedAmt = 0.0D;
										    double dPreEndorsedAmt = 0.0D;
										    String sQuery = null;
										    String sqlQuery = null;
										    Connection con = null;
										    LoggableStatement loggableStatement = null;
										    LoggableStatement ls1 = null;
										    ResultSet rs = null;
										    CommonMethods commonMethods = null;
										    try
										    {
										      if (con == null) {
										        con = DBConnectionUtility.getConnection();
										      }
										      commonMethods = new CommonMethods();

										 
										      HttpSession session = ServletActionContext.getRequest().getSession();
										      String loginedUserId = (String)session.getAttribute("loginedUserId");
										      if ((chkPayList != null) && (chkPayList.length > 0)) {
										        for (int i = 0; i < chkPayList.length; i++)
										        {
										          int insCount = 0;
										          String[] s = chkPayList[i].split(":");
										          String sPaymentRefNo = commonMethods.getEmptyIfNull(s[0]).trim();
										          String sPaymentSrlNo = commonMethods.getEmptyIfNull(s[1]).trim();
										          double allocateAmt = commonMethods.convertToDouble(s[2]);
										          double exRate = commonMethods.convertToDouble(s[3]);
										          sQuery = "SELECT BOE_PAYMENT_BP_PAY_FC_AMT, BOE_PAYMENT_BP_PAY_ENDORSE_AMT, BOE_PAYMENT_BP_PAY_EDS_FC_AMT, BOE_PAYMENT_BP_PAY_OS_FC_AMT, ENDORSED_BOE_AMT, PRE_ENDORSED_AMT, USERID FROM ETT_BOE_PAYMENT WHERE BOE_PAYMENT_BP_BOE_NO = ? AND TO_CHAR(BOE_PAYMENT_BP_BOE_DT,'DD/MM/YYYY') = ? AND PORT_CODE = ? AND BOE_PAYMENT_BP_PAY_REF = ? AND BOE_PAYMENT_BP_PAY_PART_REF = ? AND STATUS = 'P'";
										          loggableStatement = new LoggableStatement(con, sQuery);
										          loggableStatement.setString(1, boeVO.getBoeNo());
										          loggableStatement.setString(2, boeVO.getBoeDate());
										          loggableStatement.setString(3, boeVO.getPortCode());
										          loggableStatement.setString(4, sPaymentRefNo);
										          loggableStatement.setString(5, sPaymentSrlNo);
										          rs = loggableStatement.executeQuery();
										          if (rs.next())
										          {
										            dPayFcAmt = commonMethods.convertToDouble(rs.getString("BOE_PAYMENT_BP_PAY_FC_AMT"));
										            dPayEndorseAmt = commonMethods.convertToDouble(rs.getString("BOE_PAYMENT_BP_PAY_ENDORSE_AMT"));
										            dPayEDSFcAmt = commonMethods.convertToDouble(rs.getString("BOE_PAYMENT_BP_PAY_EDS_FC_AMT"));
										            dPayOSFcAmt = commonMethods.convertToDouble(rs.getString("BOE_PAYMENT_BP_PAY_OS_FC_AMT"));
										            dEndorsedAmt = commonMethods.convertToDouble(rs.getString("ENDORSED_BOE_AMT"));
										            dPreEndorsedAmt = commonMethods.convertToDouble(rs.getString("PRE_ENDORSED_AMT"));
										            String sUserId = commonMethods.getEmptyIfNull(rs.getString("USERID"));
										            if (dEndorsedAmt > dPreEndorsedAmt)
										            {
										              sqlQuery = "";
										              sqlQuery = "UPDATE ETT_BOE_PAYMENT SET BOE_PAYMENT_BP_BOE_DT = TO_DATE(?,'DD-MM-YYYY'), BOE_PAYMENT_BP_PAY_FC_AMT = TO_NUMBER?), BOE_PAYMENT_BP_PAY_ENDORSE_AMT = TO_NUMBER(BOE_PAYMENT_BP_PAY_ENDORSE_AMT) - TO_NUMBER(?), BOE_PAYMENT_BP_PAY_EDS_FC_AMT  = TO_NUMBER(BOE_PAYMENT_BP_PAY_EDS_FC_AMT) - TO_NUMBER (?), BOE_PAYMENT_BP_PAY_OS_FC_AMT  = TO_NUMBER(BOE_PAYMENT_BP_PAY_OS_FC_AMT) + TO_NUMBER(?), BOE_PAYMENT_BP_PAY_FULL_YN =?, BOE_ENTRY_AMT=?, ENDORSED_BOE_AMT = TO_NUMBER(ENDORSED_BOE_AMT) - TO_NUMBER(?), PORT_CODE = ?, USERID =?,STATUS = ?, REMARKS = ?, THIRD_PARTY_PAYMENT = ?, CHANGED_IE_CODE =?, BOE_TRANS_TYPE =?, PAGETYPE =?, CREATEDDATE = SYSTIMESTAMP, BES_IND = ?, PRE_ENDORSED_AMT = TO_NUMBER(PRE_ENDORSED_AMT) - TO_NUMBER(?) WHERE BOE_PAYMENT_BP_PAY_REF = ? AND BOE_PAYMENT_BP_PAY_PART_REF =? AND BOE_PAYMENT_BP_BOE_NO = ? AND TO_CHAR(BOE_PAYMENT_BP_BOE_DT,'DD/MM/YYYY') = ? AND PORT_CODE = ?";

										 
										 
										 
										 
										 
										 
										 
										 
										 
										 
										 
										              ls1 = new LoggableStatement(con, sqlQuery);
										              ls1.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
										              ls1.setString(2, CommonMethods.setDefaultAmount(dPayFcAmt));
										              ls1.setDouble(3, allocateAmt);
										              ls1.setDouble(4, allocateAmt);
										              ls1.setDouble(5, allocateAmt);
										              ls1.setString(6, CommonMethods.setDefaultAmount(dPayOSFcAmt));
										              ls1.setString(7, commonMethods.getEmptyIfNull(boeVO.getFullyAlloc()).trim());
										              ls1.setString(8, commonMethods.getEmptyIfNull(boeVO.getBillAmt()).trim());
										              ls1.setDouble(9, allocateAmt);
										              ls1.setString(10, commonMethods.getEmptyIfNull(boeVO.getPortCode().trim()));
										              ls1.setString(11, loginedUserId);
										              ls1.setString(12, "A");
										              ls1.setString(13, commonMethods.getEmptyIfNull(boeVO.getRemarks()).trim());
										              ls1.setString(14, commonMethods.getEmptyIfNull(boeVO.getThrdParty()).trim());
										              ls1.setString(15, commonMethods.getEmptyIfNull(boeVO.getIeCodeChange()).trim());
										              ls1.setString(16, commonMethods.getEmptyIfNull(boeVO.getTransType()).trim());
										              ls1.setString(17, "M");
										              ls1.setString(18, commonMethods.getEmptyIfNull(boeVO.getBoeBesMBInd()));
										              ls1.setDouble(19, allocateAmt);
										              ls1.setString(20, commonMethods.getEmptyIfNull(sPaymentRefNo).trim());
										              ls1.setString(21, commonMethods.getEmptyIfNull(sPaymentSrlNo).trim());
										              ls1.setString(22, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
										              ls1.setString(23, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
										              ls1.setString(24, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
										              iReturn = ls1.executeUpdate();
										              ls1.close();
										            }
										            if (dEndorsedAmt == dPreEndorsedAmt)
										            {
										              sqlQuery = "";
										              sqlQuery = "INSERT INTO ETT_BOE_PAYMENT_DEL SELECT * FROM ETT_BOE_PAYMENT WHERE BOE_PAYMENT_BP_BOE_NO = ? AND TO_CHAR(BOE_PAYMENT_BP_BOE_DT,'DD/MM/YYYY') = ? AND PORT_CODE = ? AND BOE_PAYMENT_BP_PAY_REF = ? AND BOE_PAYMENT_BP_PAY_PART_REF = ? AND STATUS = 'P'";
										              ls1 = new LoggableStatement(con, sqlQuery);
										              ls1.setString(1, boeVO.getBoeNo());
										              ls1.setString(2, boeVO.getBoeDate());
										              ls1.setString(3, boeVO.getPortCode());
										              ls1.setString(4, sPaymentRefNo);
										              ls1.setString(5, sPaymentSrlNo);
										              ls1.executeUpdate();
										              ls1.close();
										              sqlQuery = "";
										              sqlQuery = "DELETE FROM ETT_BOE_PAYMENT WHERE BOE_PAYMENT_BP_BOE_NO = ? AND TO_CHAR(BOE_PAYMENT_BP_BOE_DT,'DD/MM/YYYY') = ? AND PORT_CODE = ? AND BOE_PAYMENT_BP_PAY_REF = ? AND BOE_PAYMENT_BP_PAY_PART_REF = ? AND STATUS = 'P'";
										              ls1 = new LoggableStatement(con, sqlQuery);
										              ls1.setString(1, boeVO.getBoeNo());
										              ls1.setString(2, boeVO.getBoeDate());
										              ls1.setString(3, boeVO.getPortCode());
										              ls1.setString(4, sPaymentRefNo);
										              ls1.setString(5, sPaymentSrlNo);
										              ls1.executeUpdate();
										              ls1.close();
										            }
										            int iReturn = deleteInvoiceDetails(con, boeVO, chkInvoiceVal);
										          }
										        }
										      }
										    }
										    catch (Exception e)
										    {
										      logger.info("Exception in is " + e.getMessage());
										      try
										      {
										        rs.close();
										        loggableStatement.close();
										        con.close();
										      }
										      catch (SQLException e)
										      {
										        e.printStackTrace();
										      }
										    }
										    finally
										    {
										      try
										      {
										        rs.close();
										        loggableStatement.close();
										        con.close();
										      }
										      catch (SQLException e)
										      {
										        e.printStackTrace();
										      }
										    }
										    logger.info("Exiting Method");
										    return iRet;
										  }
										  public int deleteInvoiceDetails(Connection con, BoeVO boeVO, String chkInvoiceVal)
										  {
										    logger.info("Entering Method");
										    int iRet = 0;
										    double dRealAmt = 0.0D;
										    double dRealOrmAmt = 0.0D;
										    double dRealModAmt = 0.0D;
										    double dRealOrmModAmt = 0.0D;
										    String sQuery = null;
										    LoggableStatement loggableStatement = null;
										    LoggableStatement ls1 = null;
										    ResultSet rs = null;
										    CommonMethods commonMethods = null;
										    label698:
										    try
										    {
										      if (con == null) {
										        con = DBConnectionUtility.getConnection();
										      }
										      commonMethods = new CommonMethods();
										      if (chkInvoiceVal != null)
										      {
										        String[] s = chkInvoiceVal.split(":");
										        String invSerialNo = commonMethods.getEmptyIfNull(s[0]).trim();
										        String invNo = commonMethods.getEmptyIfNull(s[1]).trim();
										        sQuery = "SELECT REAL_AMT, REAL_ORM_AMT, REAL_MOD_AMT, REAL_ORM_MOD_AMT FROM ETT_BOE_INV_PAYMENT WHERE BOE_NO = ? AND TO_CHAR(BOE_DATE,'DD/MM/YYYY') = ? AND PORTCODE = ? AND INV_SNO = ? AND INV_NO = ? AND EOD_STATUS IS NULL";
										        loggableStatement = new LoggableStatement(con, sQuery);
										        loggableStatement.setString(1, boeVO.getBoeNo());
										        loggableStatement.setString(2, boeVO.getBoeDate());
										        loggableStatement.setString(3, boeVO.getPortCode());
										        loggableStatement.setString(4, invSerialNo);
										        loggableStatement.setString(5, invNo);
										        rs = loggableStatement.executeQuery();
										        sQuery = "";
										        if (rs.next())
										        {
										          dRealAmt = commonMethods.convertToDouble(rs.getString("REAL_AMT"));
										          dRealOrmAmt = commonMethods.convertToDouble(rs.getString("REAL_ORM_AMT"));
										          dRealModAmt = commonMethods.convertToDouble(rs.getString("REAL_MOD_AMT"));
										          dRealOrmModAmt = commonMethods.convertToDouble(rs.getString("REAL_ORM_MOD_AMT"));
										          if (dRealAmt > dRealModAmt)
										          {
										            dRealAmt -= dRealModAmt;
										            dRealOrmAmt -= dRealModAmt;
										            dRealOrmModAmt -= dRealModAmt;
										            dRealModAmt -= dRealModAmt;
										            sQuery = "UPDATE ETT_BOE_INV_PAYMENT SET REAL_AMT = ?, REAL_ORM_AMT = ?, REAL_MOD_AMT = ?, REAL_ORM_MOD_AMT = ? WHERE BOE_NO = ? AND BOE_DATE = TO_DATE(?,'DD/MM/YYYY') AND PORTCODE = ? AND INV_SNO = ? AND INV_NO = ? AND EOD_STATUS IS NULL";
										            ls1 = new LoggableStatement(con, sQuery);
										            ls1.setString(1, dRealAmt);
										            ls1.setString(2, dRealOrmAmt);
										            ls1.setString(3, dRealModAmt);
										            ls1.setString(4, dRealOrmModAmt);
										            ls1.setString(5, boeVO.getBoeNo());
										            ls1.setString(6, boeVO.getBoeDate());
										            ls1.setString(7, boeVO.getPortCode());
										            ls1.setString(8, invSerialNo);
										            ls1.setString(9, invNo);
										            ls1.executeUpdate();
										            ls1.close();
										          }
										          if (dRealAmt != dRealModAmt) {
										            break label698;
										          }
										          sQuery = "";
										          sQuery = "INSERT INTO ETT_BOE_INV_PAYMENT_DEL SELECT * FROM ETT_BOE_INV_PAYMENT WHERE BOE_NO = ? AND TO_CHAR(BOE_DATE,'DD/MM/YYYY') = ? AND PORTCODE  = ? AND INV_SNO = ? AND INV_NO = ? AND EOD_STATUS IS NULL";
										          ls1 = new LoggableStatement(con, sQuery);
										          ls1.setString(1, boeVO.getBoeNo());
										          ls1.setString(2, boeVO.getBoeDate());
										          ls1.setString(3, boeVO.getPortCode());
										          ls1.setString(4, invSerialNo);
										          ls1.setString(5, invNo);
										          ls1.executeUpdate();
										          ls1.close();
										          sQuery = "";
										          sQuery = "DELETE FROM ETT_BOE_INV_PAYMENT WHERE BOE_NO = ? AND TO_CHAR(BOE_DATE,'DD/MM/YYYY') = ? AND PORTCODE = ? AND INV_SNO = ? AND INV_NO = ? AND EOD_STATUS IS NULL";
										          ls1 = new LoggableStatement(con, sQuery);
										          ls1.setString(1, boeVO.getBoeNo());
										          ls1.setString(2, boeVO.getBoeDate());
										          ls1.setString(3, boeVO.getPortCode());
										          ls1.setString(4, invSerialNo);
										          ls1.setString(5, invNo);
										          ls1.executeUpdate();
										          ls1.close();
										        }
										      }
										    }
										    catch (Exception e)
										    {
										      logger.info("Exception in is " + e.getMessage());
										      try
										      {
										        rs.close();
										        loggableStatement.close();
										      }
										      catch (SQLException e)
										      {
										        e.printStackTrace();
										      }
										    }
										    finally
										    {
										      try
										      {
										        rs.close();
										        loggableStatement.close();
										      }
										      catch (SQLException e)
										      {
										        e.printStackTrace();
										      }
										    }
										    logger.info("Exiting Method");
										    return iRet;
										  }
										  public int checkPendingStatus(BoeVO boeVO)
												    throws SQLException
												  {
												    logger.info("Entering Method");
												    int iRet = 0;
												    String sQuery = null;
												    Connection con = null;
												    LoggableStatement ls1 = null;
												    ResultSet rs = null;
												    CommonMethods commonMethods = null;
												    try
												    {
												      con = DBConnectionUtility.getConnection();
												      commonMethods = new CommonMethods();
												      sQuery = "SELECT COUNT(1) FROM ETT_BOE_PAYMENT WHERE BOE_PAYMENT_BP_BOE_NO = ? AND BOE_PAYMENT_BP_BOE_DT   = TO_DATE(?,'DD/MM/YYYY') AND PORT_CODE = ? AND STATUS = 'P'";
												      ls1 = new LoggableStatement(con, sQuery);
												      ls1.setString(1, boeVO.getBoeNo());
												      ls1.setString(2, boeVO.getBoeDate());
												      ls1.setString(3, boeVO.getPortCode());
												      logger.info("Check Pending For Many Bill: " + ls1.getQueryString());
												      rs = ls1.executeQuery();
												      if (rs.next()) {
												        iRet = rs.getInt(1);
												      }
												      logger.info("The Return from DAO : " + iRet);
												    }
												    catch (Exception ex)
												    {
												      ex.printStackTrace();
												      try
												      {
												        rs.close();
												        ls1.close();
												      }
												      catch (SQLException e)
												      {
												        e.printStackTrace();
												      }
												    }
												    finally
												    {
												      try
												      {
												        rs.close();
												        ls1.close();
												      }
												      catch (SQLException e)
												      {
												        e.printStackTrace();
												      }
												    }
												    logger.info("Exiting Method");
												    logger.info("The Return from DAO : " + iRet);
												    return iRet;
												  }
												}
								  
