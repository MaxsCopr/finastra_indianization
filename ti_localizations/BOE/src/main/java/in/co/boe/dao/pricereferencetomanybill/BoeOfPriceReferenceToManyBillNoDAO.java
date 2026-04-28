package in.co.boe.dao.pricereferencetomanybill;

import in.co.boe.dao.AbstractDAO;
import in.co.boe.dao.exception.DAOException;
import in.co.boe.utility.ActionConstants;
import in.co.boe.utility.ActionConstantsQuery;
import in.co.boe.utility.CommonMethods;
import in.co.boe.utility.DBConnectionUtility;
import in.co.boe.utility.LoggableStatement;
import in.co.boe.utility.ValidationUtility;
import in.co.boe.vo.BOEPortSelectionVO;
import in.co.boe.vo.BillTypeVO;
import in.co.boe.vo.BoeVO;
import in.co.boe.vo.TransactionVO;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
 
public class BoeOfPriceReferenceToManyBillNoDAO
  extends AbstractDAO
  implements ActionConstantsQuery, ActionConstants
{
  static BoeOfPriceReferenceToManyBillNoDAO dao;
  private static Logger logger = Logger.getLogger(BoeOfPriceReferenceToManyBillNoDAO.class.getName());
  public static BoeOfPriceReferenceToManyBillNoDAO getDAO()
  {
    if (dao == null) {
      dao = new BoeOfPriceReferenceToManyBillNoDAO();
    }
    return dao;
  }
  public ArrayList<BillTypeVO> fetchPayRefer(BoeVO boeVO)
  {
    logger.info("--------------fetchPayRefer---------------");
    ArrayList<BillTypeVO> eventList = null;
    Connection con = null;
    LoggableStatement pst = null;
    ResultSet rs = null;
    String paymentRefNo = null;
    try
    {
      con = DBConnectionUtility.getConnection();
      eventList = new ArrayList();
      if (boeVO != null) {
        paymentRefNo = boeVO.getPaymentRefNo();
      }
      if (paymentRefNo != null) {
        paymentRefNo = paymentRefNo.trim();
      }
      String sqlQuery = "SELECT SUBSTR(OUTWARDREFERENCENUMBER,17,22) AS REF ,PAYMENTDATE AS TXNDATE FROM ETT_IDPMS_EOD_DATA  WHERE SUBSTR(OUTWARDREFERENCENUMBER,0,16) = ? ";
      pst = new LoggableStatement(con, sqlQuery);
      pst.setString(1, paymentRefNo);
      rs = pst.executeQuery();
      while (rs.next())
      {
        String datevalid = "01-09-2022";
        String txnDate1 = rs.getString("TXNDATE").trim();
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
          BillTypeVO eventData = new BillTypeVO();
          eventData.setKey(rs.getString("REF"));
          eventData.setValue(rs.getString("REF"));
          eventList.add(eventData);
        }
        else
        {
          LoggableStatement lst = null;
          ResultSet rs1 = null;
          String ormref = null;
          try
          {
            String sqlQuery1 = "SELECT DISTINCT OUTWARDREFERNECNO AS OUTWARDREFERNECNO FROM ETT_ORM_ACK WHERE OUTWARDREFERNECNO= ? ";
            ormref = paymentRefNo + rs.getString("REF");
            lst = new LoggableStatement(con, sqlQuery1);
            lst.setString(1, ormref);
            rs1 = lst.executeQuery();
            while (rs1.next())
            {
              BillTypeVO eventData = new BillTypeVO();
              eventData.setKey(rs.getString("REF"));
              eventData.setValue(rs.getString("REF"));
              eventList.add(eventData);
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
    catch (Exception e)
    {
      logger.info("--------------fetchPayRefer---------------" + e);
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    logger.info("Exiting Method");
    return eventList;
  }
  public BoeVO retriveDataFromTI(BoeVO boeVO)
    throws DAOException
  {
    logger.info("----------------retriveDataFromTI--------------------");
    Connection con = null;
    LoggableStatement pst = null;
    ResultSet rs = null;
    String sqlQuery = null;
    CommonMethods commonMethods = null;
    try
    {
      commonMethods = new CommonMethods();
      con = DBConnectionUtility.getConnection();
      sqlQuery = "SELECT NVL(TRIM(PD_TXN_REF), '') AS PD_TXN_REF, NVL(TRIM(PD_PART_PAY_REF), '') AS PD_PART_PAY_REF, TO_CHAR(PD_TXN_DT, 'DD-MM-YYYY') AS PD_TXN_DT,NVL(TRIM(PD_TXN_CCY), '') AS PD_TXN_CCY,  NVL(TRIM(PD_FC_AMT), '0') AS PD_FC_AMT, NVL(TRIM(PD_ENDORSED_AMT), '0') AS PD_ENDORSED_AMT, NVL(TRIM(PD_OUTSTANDING_AMT), '0') AS PD_OUTSTANDING_AMT, NVL(TRIM(PD_CIF_ID), '') AS PD_CIF_ID,NVL(TRIM(PD_CIF_NAME), '') AS PD_CIF_NAME, NVL(TRIM(BENEF_NAME), '') AS BENEF_NAME,NVL(TRIM(BENEF_COUNTRY), '') AS BENEF_COUNTRY, NVL(TRIM(PD_IE_CODE), '') AS PD_IE_CODE,NVL(TRIM(PD_CIF_ADDRESS),'') AS PD_CIF_ADDRESS, NVL(TRIM(PD_IE_PAN_NO) , '') AS PD_IE_PAN_NO,NVL(TRIM(BR.ADCODE), '') AS AD_CODE  FROM ETTV_BOE_PAYMENT_DETAILS ,EXTBRAMAS BR WHERE SUBSTR(PD_TXN_REF,0,4) = BR.BCODE(+) AND PD_TXN_REF =? AND PD_PART_PAY_REF=?";

 
 
 
 
 
 
      pst = new LoggableStatement(con, sqlQuery);
      pst.setString(1, boeVO.getPaymentRefNo().trim());
      pst.setString(2, boeVO.getPartPaymentSlNo().trim());
      logger.info("RetriveDataFromTI For many Bill: " + pst.getQueryString());
      rs = pst.executeQuery();
      if (!commonMethods.isNull(boeVO.getOkIdFlg()))
      {
        boeVO.setOkIdFlg(boeVO.getOkIdFlg());
        logger.info("The OK ID FLG is---- : " + boeVO.getOkIdFlg());
      }
      if (rs.next())
      {
        boeVO.setPaymentRefNo(rs.getString("PD_TXN_REF"));
        boeVO.setPartPaymentSlNo(rs.getString("PD_PART_PAY_REF"));
        boeVO.setPayDate(rs.getString("PD_TXN_DT"));
        boeVO.setPaymentCurr(rs.getString("PD_TXN_CCY"));
        boeVO.setBillCurrency(rs.getString("PD_TXN_CCY"));
        boeVO.setPaymentAmount(rs.getString("PD_FC_AMT"));
        boeVO = fetchPreviousEndoreAmount(con, boeVO);
        boeVO.setEndorseAmt(commonMethods.toDouble(boeVO.getTotalEndorseAmt()));
        double ormClosureAmt = getORMClosureAmount(con, boeVO);
        double billOutAmt = commonMethods.convertToDouble(rs.getString("PD_OUTSTANDING_AMT"));
        double remainAmt = billOutAmt - ormClosureAmt;
        BigDecimal outAmt = BigDecimal.valueOf(remainAmt).setScale(2, RoundingMode.HALF_UP);
        boeVO.setOutAmt(String.valueOf(outAmt));
        boeVO.setOutAmt1(String.valueOf(outAmt));
        if (remainAmt == 0.0D) {
          boeVO.setFullyAlloc("Y");
        } else {
          boeVO.setFullyAlloc("N");
        }
        boeVO.setBenefName(rs.getString("BENEF_NAME"));
        boeVO.setBenefCountry(rs.getString("BENEF_COUNTRY"));
        boeVO.setCifNo(rs.getString("PD_CIF_ID"));
        boeVO.setCustName(rs.getString("PD_CIF_NAME"));
        boeVO.setIeCode(rs.getString("PD_IE_CODE"));
      }
    }
    catch (SQLException e)
    {
      logger.info("----------------retriveDataFromTI-------sql exception-------------" + e);
      e.printStackTrace();
      throwDAOException(e);
    }
    catch (Exception e)
    {
      logger.info("----------------retriveDataFromTI----Exception-----------" + e);
    }
    finally
    {
      closeSqlRefferance(rs, pst, con);
    }
    logger.info("Exiting Method");
    return boeVO;
  }
  public double getORMClosureAmount(Connection con, BoeVO boeVO)
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
		      String outwardReferenceNo = commonMethods.getEmptyIfNull(boeVO.getPaymentRefNo()).trim() + 
		        commonMethods.getEmptyIfNull(boeVO.getPartPaymentSlNo()).trim();
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
		  public ArrayList<TransactionVO> fetchPaymentReferenceDataList(BoeVO boeVO)
		    throws DAOException
		  {
		    logger.info("Entering Method");
		    String sQuery = null;
		    LoggableStatement pst1 = null;
		    ResultSet rs1 = null;
		    Connection con = null;
		    LoggableStatement pst = null;
		    ResultSet rs = null;
		    String sqlQuery = null;
		    ArrayList<TransactionVO> tiList = null;
		    TransactionVO transactionVO = null;
		    CommonMethods commonMethods = null;
		    try
		    {
		      commonMethods = new CommonMethods();
		      con = DBConnectionUtility.getConnection();
		      tiList = new ArrayList();
		      logger.info(" boeVO.getPaymentRefNo().trim()---------" + boeVO.getPaymentRefNo().trim());
		      logger.info(" boeVO.getPartPaymentSlNo()------------" + boeVO.getPartPaymentSlNo());
		      sqlQuery = "SELECT NVL(TRIM(BP.BOE_PAYMENT_BP_PAY_REF), ' ') AS BOE_PAYMENT_BP_PAY_REF,  NVL(TRIM(BP.BOE_PAYMENT_BP_PAY_PART_REF), ' ') AS BOE_PAYMENT_BP_PAY_PART_REF, NVL(TRIM(BP.BOE_PAYMENT_BP_BOE_NO), ' ') AS BOE_PAYMENT_BP_BOE_NO,  TO_CHAR(TO_DATE(BP.BOE_PAYMENT_BP_BOE_DT, 'DD-MM-YY'),'DD/MM/YYYY') AS BOE_PAYMENT_BP_BOE_DT, TRIM(BP.PORT_CODE) AS PORTCODE,NVL(TRIM(BP.BOE_ENTRY_AMT), '0') AS BOE_ENTRY_AMT,  NVL(BOE_PAYMENT_BP_BOE_CCY,'') AS BOE_PAYMENT_BP_BOE_CCY, NVL(TRIM(BP.BOE_PAYMENT_BP_PAY_ENDORSE_AMT), '0') AS BOE_PAYMENT_BP_PAY_ENDORSE_AMT,  NVL(TRIM(BP.BOE_PAYMENT_BP_PAY_OS_FC_AMT), '0') AS BOE_PAYMENT_BP_PAY_OS_FC_AMT,  NVL(TRIM(BP.BOE_PAYMENT_BP_PAY_FULL_YN),' ') AS BOE_PAYMENT_BP_PAY_FULL_YN,STATUS FROM ETT_BOE_PAYMENT BP WHERE BP.BOE_PAYMENT_BP_PAY_REF=?  AND BP.BOE_PAYMENT_BP_PAY_PART_REF= ? AND BOE_PAYMENT_BP_BOE_NO IS NOT NULL AND PORT_CODE IS NOT NULL AND BOE_PAYMENT_BP_BOE_DT IS NOT NULL";

		 
		 
		 
		 
		 
		 
		 
		      pst = new LoggableStatement(con, sqlQuery);
		      pst.setString(1, boeVO.getPaymentRefNo().trim());
		      pst.setString(2, boeVO.getPartPaymentSlNo());
		      rs = pst.executeQuery();
		      logger.info("Search Button Query--------------------------->" + pst.getQueryString());
		      while (rs.next())
		      {
		        transactionVO = new TransactionVO();
		        transactionVO.setPaymentRefNo(rs.getString("BOE_PAYMENT_BP_PAY_REF"));
		        transactionVO.setPartPaymentSlNo(rs.getString("BOE_PAYMENT_BP_PAY_PART_REF"));
		        transactionVO.setBoeNo(rs.getString("BOE_PAYMENT_BP_BOE_NO"));
		        transactionVO.setBoeDate(rs.getString("BOE_PAYMENT_BP_BOE_DT"));
		        transactionVO.setPortCode(rs.getString("PORTCODE"));
		        transactionVO.setBillAmt(CommonMethods.setCheckValue(rs.getString("BOE_ENTRY_AMT")));
		        transactionVO.setBoeCurr(rs.getString("BOE_PAYMENT_BP_BOE_CCY"));

		 
		        String tempVal = commonMethods.getEmptyIfNull(rs.getString("BOE_PAYMENT_BP_PAY_REF")).trim() + ":" + 
		          commonMethods.getEmptyIfNull(rs.getString("BOE_PAYMENT_BP_PAY_PART_REF")).trim() + ":" + 
		          commonMethods.getEmptyIfNull(rs.getString("BOE_PAYMENT_BP_BOE_NO")).trim() + ":" + 
		          commonMethods.getEmptyIfNull(rs.getString("BOE_PAYMENT_BP_BOE_DT")).trim() + ":" + 
		          commonMethods.getEmptyIfNull(rs.getString("PORTCODE")).trim();
		        transactionVO.setDelBoeDetails(tempVal);

		 
		 
		        String tEndAmt = getTotalEndoreAmount(transactionVO.getBoeNo(), transactionVO.getBoeDate(), 
		          transactionVO.getPortCode());
		        double totalEndAmt = commonMethods.convertToDouble(tEndAmt);
		        double boeAmt = commonMethods.convertToDouble(transactionVO.getBillAmt());
		        double availableAmt = boeAmt - totalEndAmt;
		        BigDecimal availAmt = BigDecimal.valueOf(availableAmt).setScale(2, RoundingMode.HALF_UP);
		        transactionVO.setActualEndorseAmt(String.valueOf(availAmt));
		        transactionVO.setBalEndorseAmt(
		          CommonMethods.setCheckValue(rs.getString("BOE_PAYMENT_BP_PAY_ENDORSE_AMT")));
		        transactionVO.setStatus(rs.getString("STATUS"));

		 
		 
		        sQuery = "SELECT COUNT(1) FROM ETT_BOE_PAYMENT WHERE BOE_PAYMENT_BP_BOE_NO = ? AND BOE_PAYMENT_BP_BOE_DT   = TO_DATE(?,'DD/MM/YYYY') AND PORT_CODE = ? AND STATUS = 'P'";
		        pst1 = new LoggableStatement(con, sQuery);
		        pst1.setString(1, transactionVO.getBoeNo());
		        pst1.setString(2, transactionVO.getBoeDate());
		        pst1.setString(3, transactionVO.getPortCode());
		        rs1 = pst1.executeQuery();
		        if (rs1.next()) {
		          transactionVO.setInvPendingCount(rs1.getString(1));
		        }
		        tiList.add(transactionVO);
		      }
		    }
		    catch (SQLException e)
		    {
		      logger.info("fetchPaymentReferenceDataList Exception------------->" + e.getMessage());
		      throwDAOException(e);
		    }
		    finally
		    {
		      closeSqlRefferance(rs, pst, con);
		    }
		    logger.info("Exiting Method");
		    return tiList;
		  }
		  public String getTotalEndoreAmount(String boeNo, String boeDate, String portCode)
		    throws DAOException
		  {
		    logger.info("getTotalEndoreAmount-------------------");
		    Connection con = null;
		    LoggableStatement pst = null;
		    ResultSet rs = null;
		    String sqlQuery = null;
		    CommonMethods commonMethods = null;
		    String totalEndAmt = null;
		    try
		    {
		      commonMethods = new CommonMethods();
		      con = DBConnectionUtility.getConnection();
		      double realAmtIc = 0.0D;
		      sqlQuery = "SELECT ROUND(SUM(ENDORSED_BOE_AMT),2) AS TOTAL_EDS_AMT  FROM ETT_BOE_PAYMENT WHERE BOE_PAYMENT_BP_BOE_NO = ? AND TO_CHAR(BOE_PAYMENT_BP_BOE_DT,'DD/MM/YYYY') = ? AND PORT_CODE = ? AND STATUS != 'R' ";

		 
		 
		      pst = new LoggableStatement(con, sqlQuery);
		      pst.setString(1, commonMethods.getEmptyIfNull(boeNo).trim());
		      pst.setString(2, commonMethods.getEmptyIfNull(boeDate).trim());
		      pst.setString(3, commonMethods.getEmptyIfNull(portCode).trim());
		      logger.info("FetchTotalEndoreAmount For Many Bills: " + pst.getQueryString());
		      rs = pst.executeQuery();
		      if (rs.next()) {
		        realAmtIc = commonMethods.convertToDouble(rs.getString("TOTAL_EDS_AMT"));
		      }
		      double adjAmtIc = 0.0D;
		      sqlQuery = "SELECT SUM(ADJ_INVAMT) AS CLOSURE_AMTIC FROM ETT_BOE_INV_CLOSURE  WHERE BOE_NO =? AND TO_CHAR(BOE_DATE,'DD/MM/YYYY') = ? AND PORTCODE =?  AND STATUS != 'R' ";

		 
		      LoggableStatement pst2 = new LoggableStatement(con, sqlQuery);
		      pst2.setString(1, commonMethods.getEmptyIfNull(boeNo).trim());
		      pst2.setString(2, commonMethods.getEmptyIfNull(boeDate).trim());
		      pst2.setString(3, commonMethods.getEmptyIfNull(portCode).trim());
		      logger.info("ETT_BOE_INV_CLOSURE For Many Bills: " + pst2.getQueryString());
		      ResultSet rs2 = pst2.executeQuery();
		      if (rs2.next()) {
		        adjAmtIc = commonMethods.convertToDouble(rs2.getString("CLOSURE_AMTIC"));
		      }
		      closeSqlRefferance(rs2, pst2);
		      double totalRealAmt = realAmtIc + adjAmtIc;
		      totalRealAmt = Math.round(totalRealAmt * 100.0D) / 100.0D;
		      totalEndAmt = String.valueOf(totalRealAmt);
		    }
		    catch (SQLException e)
		    {
		      logger.info("getTotalEndoreAmount-----SQLException--------------" + e);
		      throwDAOException(e);
		    }
		    catch (Exception e)
		    {
		      logger.info("getTotalEndoreAmount--------Exception-----------" + e);
		      throwDAOException(e);
		    }
		    finally
		    {
		      closeSqlRefferance(rs, pst, con);
		    }
		    logger.info("Exiting Method");
		    return totalEndAmt;
		  }
		  public BoeVO retriveDataBasedOnBillNO(BoeVO boeVO)
		    throws DAOException
		  {
		    logger.info("---------------retriveDataBasedOnBillNO------------");
		    Connection con = null;
		    LoggableStatement pst = null;
		    ResultSet rs = null;
		    ArrayList<TransactionVO> invoiceList = null;
		    CommonMethods commonMethods = null;
		    try
		    {
		      con = DBConnectionUtility.getConnection();
		      commonMethods = new CommonMethods();
		      String query = "SELECT BOE_TYPE AS TRANS_TYPE,BOE_AD_CODE AS ADCODE,CIF AS CIF_ID,BOE_IE_CODE AS IE_CODE,BOE_IE_NAME AS IE_NAME,\tBOE_IE_ADDRESS AS BOE_IE_ADDRESS,BOE_IE_PANNUMBER AS BOE_IE_PANNUMBER,\tBOE_PORT_OF_SHIPMENT AS PORTOFSHIPMENT,BOE_RECORD_INDICATOR AS RECORDINDICATOR, BOE_GP AS BOE_GP,BOE_IMPORT_AGENCY AS IMPORTAGENCY,BOE_MAWB_MBLNUMBER AS MBLNO, TO_CHAR(BOE_MAWB_MBLDATE,'DD/MM/YYYY') AS MBLDATE,BOE_HAWB_HBLNUMBER AS HBLNO,TO_CHAR(BOE_HAWB_HBLDATE,'DD/MM/YYYY') AS HBLDATE,\tBOE_IGMNUMBER AS IGMNO,TO_CHAR(BOE_IGMDATE,'DD/MM/YYYY') AS IGMDATE  FROM ETT_BOE_DETAILS WHERE BOE_NUMBER =? AND TO_CHAR(BOE_DATE,'DD/MM/YYYY') =? AND BOE_PORT_OF_DISCHARGE =? AND STATUS = 'A' ";

		 
		 
		 
		 
		      LoggableStatement pst1 = new LoggableStatement(con, query);
		      pst1.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
		      pst1.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
		      pst1.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
		      logger.info("---------------retriveDataBasedOnBillNO-----query-------" + pst1.getQueryString());
		      ResultSet rs1 = pst1.executeQuery();
		      if (rs1.next())
		      {
		        boeVO.setCifNo(commonMethods.getEmptyIfNull(rs1.getString("CIF_ID")).trim());
		        boeVO.setIeCode(commonMethods.getEmptyIfNull(rs1.getString("IE_CODE")).trim());
		        boeVO.setCustName(commonMethods.getEmptyIfNull(rs1.getString("IE_NAME")).trim());
		        boeVO.setIeadd(commonMethods.getEmptyIfNull(rs1.getString("BOE_IE_ADDRESS")).trim());
		        boeVO.setIepan(commonMethods.getEmptyIfNull(rs1.getString("BOE_IE_PANNUMBER")).trim());
		        boeVO.setIgmNo(commonMethods.getEmptyIfNull(rs1.getString("IGMNO")).trim());
		        boeVO.setIgmDate(commonMethods.getEmptyIfNull(rs1.getString("IGMDATE")).trim());
		        boeVO.setHblNo(commonMethods.getEmptyIfNull(rs1.getString("HBLNO")).trim());
		        boeVO.setHblDate(commonMethods.getEmptyIfNull(rs1.getString("HBLDATE")).trim());
		        boeVO.setMblNo(commonMethods.getEmptyIfNull(rs1.getString("MBLNO")).trim());
		        boeVO.setMblDate(commonMethods.getEmptyIfNull(rs1.getString("MBLDATE")).trim());
		        boeVO.setAdCode(commonMethods.getEmptyIfNull(rs1.getString("ADCODE")).trim());
		        boeVO.setPos(commonMethods.getEmptyIfNull(rs1.getString("PORTOFSHIPMENT")).trim());
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
		      boolean isAlreadyExsists = false;
		      String sqlQuery = "SELECT BP.CHANGED_IE_CODE AS CHANGED_IE_CODE, NVL(TRIM(BP.BOE_ENTRY_AMT), '0') AS BOE_ENTRY_AMT, NVL(TRIM(BP.BOE_PAYMENT_BP_PAY_FC_AMT), '0') AS BOE_PAYMENT_BP_PAY_FC_AMT, NVL(TRIM(BP.BOE_PAYMENT_BP_PAY_ENDORSE_AMT), '0') AS BOE_PAYMENT_BP_PAY_ENDORSE_AMT,  NVL(TRIM(EB.PD_OUTSTANDING_AMT), '0') AS PD_OUTSTANDING_AMT,BP.STATUS,BP.REMARKS, NVL(TRIM(BP.BOE_PAYMENT_BP_PAY_FULL_YN),' ') AS BOE_PAYMENT_BP_PAY_FULL_YN,BP.EXCHANGE_RATE AS EXCHANGE_RATE, DECODE(BES_IND, 1, 'NEW', 'CANCEL') BES_IND  FROM ETT_BOE_PAYMENT BP,ETTV_BOE_PAYMENT_DETAILS EB WHERE EB.PD_TXN_REF = BP.BOE_PAYMENT_BP_PAY_REF AND EB.PD_PART_PAY_REF = BP.BOE_PAYMENT_BP_PAY_PART_REF   AND BP.BOE_PAYMENT_BP_BOE_NO = ? AND TO_CHAR(BP.BOE_PAYMENT_BP_BOE_DT,'DD/MM/YYYY') = ? AND BP.PORT_CODE = ? AND BP.BOE_PAYMENT_BP_PAY_REF= ?  AND BP.BOE_PAYMENT_BP_PAY_PART_REF=? ";

		 
		 
		 
		 
		 
		 
		      pst = new LoggableStatement(con, sqlQuery);
		      pst.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
		      pst.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
		      pst.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
		      pst.setString(4, commonMethods.getEmptyIfNull(boeVO.getPaymentRefNo()).trim());
		      pst.setString(5, commonMethods.getEmptyIfNull(boeVO.getPartPaymentSlNo()));
		      logger.info("RetriveDataBasedOnBillNO For many Bill: " + pst.getQueryString());

		 
		      rs = pst.executeQuery();
		      if (rs.next())
		      {
		        isAlreadyExsists = true;
		        boeVO.setBillAmt(commonMethods.toDouble(rs.getString("BOE_ENTRY_AMT")));
		        boeVO.setAllocAmt("");
		        boeVO.setOutAmt(commonMethods.toDouble(rs.getString("PD_OUTSTANDING_AMT")));
		        boeVO.setOutAmt_temp(commonMethods.toDouble(rs.getString("PD_OUTSTANDING_AMT")));
		        boeVO.setFullyAlloc(
		          commonMethods.getEmptyIfNull(rs.getString("BOE_PAYMENT_BP_PAY_FULL_YN")).trim());
		        boeVO.setStatus(commonMethods.getEmptyIfNull(rs.getString("STATUS")).trim());
		        boeVO.setRemarks(commonMethods.getEmptyIfNull(rs.getString("REMARKS")).trim());
		        boeVO.setIeCodeChange(commonMethods.getEmptyIfNull(rs.getString("CHANGED_IE_CODE")).trim());
		        boeVO.setBoeExRate(commonMethods.getEmptyIfNull(rs.getString("EXCHANGE_RATE")).trim());
		        boeVO.setExRate(commonMethods.getEmptyIfNull(rs.getString("EXCHANGE_RATE")).trim());
		        boeVO.setBoeBesSBInd(commonMethods.getEmptyIfNull(rs.getString("BES_IND")).trim());
		      }
		      if (!commonMethods.isNull(boeVO.getOkIdFlg()))
		      {
		        boeVO.setOkIdFlg(boeVO.getOkIdFlg());
		        logger.info("The OK ID FLG is : " + boeVO.getOkIdFlg());
		      }
		      int boeCount = 0;
		      String boeQuery = "SELECT COUNT(*) AS BOE_COUNT  FROM ETT_BOE_PAYMENT WHERE BOE_PAYMENT_BP_BOE_NO =? AND TO_CHAR(BOE_PAYMENT_BP_BOE_DT,'DD/MM/YYYY') = ?  AND PORT_CODE = ? AND STATUS != 'R' ";

		 
		      LoggableStatement pst2 = new LoggableStatement(con, boeQuery);
		      pst2.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
		      pst2.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
		      pst2.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
		      logger.info("BOE COUNT QUERY--->: " + pst2.getQueryString());

		 
		      ResultSet rs2 = pst2.executeQuery();
		      if (rs2.next()) {
		        boeCount = rs2.getInt("BOE_COUNT");
		      }
		      if (boeCount == 0)
		      {
		        boeVO = getTotalBOEAmt_Fetching(con, boeVO);
		      }
		      else
		      {
		        if (!isAlreadyExsists) {
		          boeVO = getBOEAmt(con, boeVO);
		        }
		        String tEndAmt = getTotalEndoreAmount(boeVO.getBoeNo(), boeVO.getBoeDate(), boeVO.getPortCode());
		        double totalEndAmt = commonMethods.convertToDouble(tEndAmt);
		        double boeAmt = commonMethods.convertToDouble(boeVO.getBillAmt());
		        double availableAmt = boeAmt - totalEndAmt;
		        BigDecimal availAmt = BigDecimal.valueOf(availableAmt).setScale(2, RoundingMode.HALF_UP);
		        boeVO.setActualEndorseAmt(String.valueOf(availAmt));
		        boeVO.setActualEndorseAmt_temp(String.valueOf(availAmt));
		        boeVO = fetchPreviousEndoreAmount(con, boeVO);
		        boeVO.setEndorseAmt(boeVO.getTotalEndorseAmt());
		      }
		      closeSqlRefferance(rs2, pst2);
		      int totalBoeCount = 0;
		      String boeCountQuery = "SELECT SUM(BOE_COUNT) AS B_COUNT FROM (SELECT COUNT(*) AS BOE_COUNT FROM ETT_BOE_CLOSURE_DATA  WHERE BOENUMBER = ? AND TO_CHAR(BOEDATE,'DD/MM/YYYY') = ? AND PORTOFDISCHARGE = ? AND STATUS != 'R' UNION SELECT COUNT(*) AS BOE_COUNT FROM ETT_BOE_PAYMENT WHERE BOE_PAYMENT_BP_BOE_NO = ?  AND TO_CHAR(BOE_PAYMENT_BP_BOE_DT,'DD/MM/YYYY') = ? AND PORT_CODE = ? AND STATUS != 'R') ";

		 
		 
		      LoggableStatement pst3 = new LoggableStatement(con, boeCountQuery);
		      pst3.setString(1, boeVO.getBoeNo());
		      pst3.setString(2, boeVO.getBoeDate());
		      pst3.setString(3, boeVO.getPortCode());
		      pst3.setString(4, boeVO.getBoeNo());
		      pst3.setString(5, boeVO.getBoeDate());
		      pst3.setString(6, boeVO.getPortCode());
		      ResultSet rs3 = pst3.executeQuery();
		      if (rs3.next()) {
		        totalBoeCount = rs3.getInt("B_COUNT");
		      }
		      closeSqlRefferance(rs3, pst3);
		      if (totalBoeCount > 0) {
		        boeVO.setManualPartialData("Y");
		      } else {
		        boeVO.setManualPartialData("N");
		      }
		      invoiceList = fetchInvoiceList(con, boeVO);
		      if ((invoiceList.size() > 0) && (!invoiceList.isEmpty()))
		      {
		        boeVO.setInvoiceList(invoiceList);
		        boeVO.setBillCurrency(((TransactionVO)invoiceList.get(0)).getInvoiceCurr());
		      }
		    }
		    catch (Exception e)
		    {
		      logger.info("---------------retriveDataBasedOnBillNO--------exception----" + e);
		      e.printStackTrace();
		    }
		    finally
		    {
		      closeSqlRefferance(rs, pst, con);
		    }
		    logger.info("Exiting Method");
		    return boeVO;
		  }
		  public BoeVO retriveDataBasedOnBillNO1(BoeVO boeVO)
				    throws DAOException
				  {
				    logger.info("retriveDataBasedOnBillNO1---------------");
				    Connection con = null;
				    LoggableStatement pst = null;
				    ResultSet rs = null;
				    ArrayList<TransactionVO> invoiceList = null;
				    PreparedStatement prep = null;
				    CommonMethods commonMethods = null;
				    String amt = "";
				    try
				    {
				      con = DBConnectionUtility.getConnection();
				      commonMethods = new CommonMethods();
				      int reset_amt = 0;
				      try
				      {
				        String original_amt = "SELECT NVL(BOE_PAYMENT_BP_PAY_FC_AMT,'0') AS AMT   FROM ETT_BOE_PAYMENT WHERE BOE_PAYMENT_BP_PAY_REF   ='" + 
				          boeVO.getPaymentRefNo() + "' " + " AND BOE_PAYMENT_BP_PAY_PART_REF='" + boeVO.getEventRefno() + 
				          "'";
				        LoggableStatement lst = new LoggableStatement(con, original_amt);
				        ResultSet rst = lst.executeQuery();
				        while (rst.next()) {
				          amt = rst.getString("AMT");
				        }
				      }
				      catch (Exception e)
				      {
				        logger.info("Select Query Value");
				      }
				      try
				      {
				        String boe_payment_invoice_reset_query = "UPDATE ETT_BOE_INV_PAYMENT SET REAL_AMT='0', REAL_ORM_AMT='0', REAL_MOD_AMT='0', REAL_ORM_MOD_AMT='0', INV_MODIFIED_TIME=SYSTIMESTAMP  WHERE PAYMENT_REF=?  AND EVENT_REF=? AND BOE_NO =?  AND BOE_DATE  =TO_DATE(?,'DD-MM-YY') AND PORTCODE     =?";

				 
				        LoggableStatement pst3 = new LoggableStatement(con, boe_payment_invoice_reset_query);
				        pst3.setString(1, boeVO.getPaymentRefNo());
				        pst3.setString(2, boeVO.getEventRefno());
				        pst3.setString(3, boeVO.getBoeNo());
				        pst3.setString(4, boeVO.getBoeDate());
				        pst3.setString(5, boeVO.getPortCode());
				        int invoice_amt_reversal = pst3.executeUpdate();
				        if (invoice_amt_reversal > 0) {
				          logger.info("Payment Invoice REversal Excecuted-------------->" + invoice_amt_reversal);
				        }
				      }
				      catch (Exception e)
				      {
				        logger.info("Error in BOE AMount Reversal Invoice Exception" + e.getMessage());
				      }
				      try
				      {
				        String boe_payment = " UPDATE ETT_BOE_PAYMENT SET BOE_PAYMENT_BP_PAY_OS_FC_AMT =?, BOE_PAYMENT_BP_PAY_ENDORSE_AMT ='0',  BOE_PAYMENT_BP_PAY_EDS_FC_AMT    ='0',PRE_ENDORSED_AMT ='0', BOE_MODIFIED_TIME                =SYSTIMESTAMP  WHERE BOE_PAYMENT_BP_PAY_REF       =? AND BOE_PAYMENT_BP_PAY_PART_REF    =? BOE_PAYMENT_BP_BOE_NO=?, BOE_PAYMENT_BP_BOE_DT=TO_CHAR(TO_DATE(?,'DD-MON-YYYY'),'DD/MM/YYYY'), PORTCODE=?";

				 
				 
				        LoggableStatement pst3 = new LoggableStatement(con, boe_payment);
				        pst3.setString(1, amt);
				        pst3.setString(2, boeVO.getPaymentRefNo());
				        pst3.setString(3, boeVO.getEventRefno());
				        pst3.setString(4, boeVO.getBoeNo());
				        pst3.setString(5, boeVO.getBoeDate());
				        pst3.setString(6, boeVO.getPortCode());
				        int invoice_amt_reversal = pst3.executeUpdate();
				        logger.info("---------------ETT_BOE_PAYMENT-----------Update Query" + pst3.getQueryString());
				        if (invoice_amt_reversal > 0) {
				          logger.info("Payment Invoice REversal Excecuted-------------->" + invoice_amt_reversal);
				        }
				      }
				      catch (Exception e)
				      {
				        logger.info("Error in BOE AMount Reversal BOE PAYMENT Exception" + e.getMessage());
				      }
				      String query = "SELECT BOE_TYPE AS TRANS_TYPE,BOE_AD_CODE AS ADCODE,CIF AS CIF_ID,BOE_IE_CODE AS IE_CODE,BOE_IE_NAME AS IE_NAME,\tBOE_IE_ADDRESS AS BOE_IE_ADDRESS,BOE_IE_PANNUMBER AS BOE_IE_PANNUMBER,\tBOE_PORT_OF_SHIPMENT AS PORTOFSHIPMENT,BOE_RECORD_INDICATOR AS RECORDINDICATOR, BOE_GP AS BOE_GP,BOE_IMPORT_AGENCY AS IMPORTAGENCY,BOE_MAWB_MBLNUMBER AS MBLNO, TO_CHAR(BOE_MAWB_MBLDATE,'DD/MM/YYYY') AS MBLDATE,BOE_HAWB_HBLNUMBER AS HBLNO,TO_CHAR(BOE_HAWB_HBLDATE,'DD/MM/YYYY') AS HBLDATE,\tBOE_IGMNUMBER AS IGMNO,TO_CHAR(BOE_IGMDATE,'DD/MM/YYYY') AS IGMDATE  FROM ETT_BOE_DETAILS WHERE BOE_NUMBER = ? AND TO_CHAR(BOE_DATE,'DD/MM/YYYY') =? AND BOE_PORT_OF_DISCHARGE = ? ";

				 
				 
				 
				 
				      LoggableStatement pst1 = new LoggableStatement(con, query);
				      pst1.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
				      pst1.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
				      pst1.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
				      logger.info("-----------SELECT ----ETT_BOE_DETAILS-----------" + pst1.getQueryString());
				      ResultSet rs1 = pst1.executeQuery();
				      if (rs1.next())
				      {
				        boeVO.setCifNo(commonMethods.getEmptyIfNull(rs1.getString("CIF_ID")).trim());
				        boeVO.setIeCode(commonMethods.getEmptyIfNull(rs1.getString("IE_CODE")).trim());
				        boeVO.setCustName(commonMethods.getEmptyIfNull(rs1.getString("IE_NAME")).trim());
				        boeVO.setIeadd(commonMethods.getEmptyIfNull(rs1.getString("BOE_IE_ADDRESS")).trim());
				        boeVO.setIepan(commonMethods.getEmptyIfNull(rs1.getString("BOE_IE_PANNUMBER")).trim());
				        boeVO.setIgmNo(commonMethods.getEmptyIfNull(rs1.getString("IGMNO")).trim());
				        boeVO.setIgmDate(commonMethods.getEmptyIfNull(rs1.getString("IGMDATE")).trim());
				        boeVO.setHblNo(commonMethods.getEmptyIfNull(rs1.getString("HBLNO")).trim());
				        boeVO.setHblDate(commonMethods.getEmptyIfNull(rs1.getString("HBLDATE")).trim());
				        boeVO.setMblNo(commonMethods.getEmptyIfNull(rs1.getString("MBLNO")).trim());
				        boeVO.setMblDate(commonMethods.getEmptyIfNull(rs1.getString("MBLDATE")).trim());
				        boeVO.setAdCode(commonMethods.getEmptyIfNull(rs1.getString("ADCODE")).trim());
				        boeVO.setPos(commonMethods.getEmptyIfNull(rs1.getString("PORTOFSHIPMENT")).trim());
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
				      boolean isAlreadyExsists = false;
				      String sqlQuery = "SELECT BP.CHANGED_IE_CODE AS CHANGED_IE_CODE, NVL(TRIM(BP.BOE_ENTRY_AMT), '0') AS BOE_ENTRY_AMT, NVL(TRIM(BP.BOE_PAYMENT_BP_PAY_FC_AMT), '0') AS BOE_PAYMENT_BP_PAY_FC_AMT, NVL(TRIM(BP.BOE_PAYMENT_BP_PAY_ENDORSE_AMT), '0') AS BOE_PAYMENT_BP_PAY_ENDORSE_AMT,  NVL(TRIM(EB.PD_OUTSTANDING_AMT), '0') AS PD_OUTSTANDING_AMT,BP.STATUS,BP.REMARKS, NVL(TRIM(BP.BOE_PAYMENT_BP_PAY_FULL_YN),' ') AS BOE_PAYMENT_BP_PAY_FULL_YN,BP.EXCHANGE_RATE AS EXCHANGE_RATE, DECODE(BES_IND, 1, 'NEW', 'CANCEL') BES_IND  FROM ETT_BOE_PAYMENT BP,ETTV_BOE_PAYMENT_DETAILS EB WHERE EB.PD_TXN_REF = BP.BOE_PAYMENT_BP_PAY_REF AND EB.PD_PART_PAY_REF = BP.BOE_PAYMENT_BP_PAY_PART_REF   AND BP.BOE_PAYMENT_BP_BOE_NO = ? AND TO_CHAR(BP.BOE_PAYMENT_BP_BOE_DT,'DD/MM/YYYY') =? AND BP.PORT_CODE = ? AND BP.BOE_PAYMENT_BP_PAY_REF= ? AND BP.BOE_PAYMENT_BP_PAY_PART_REF=?";

				 
				 
				 
				 
				 
				 
				      pst = new LoggableStatement(con, sqlQuery);
				      pst.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
				      pst.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
				      pst.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
				      pst.setString(4, commonMethods.getEmptyIfNull(boeVO.getPaymentRefNo()).trim());
				      pst.setString(5, commonMethods.getEmptyIfNull(boeVO.getPartPaymentSlNo()).trim());
				      logger.info("RetriveDataBasedOnBillNO For many Bill: " + pst.getQueryString());
				      rs = pst.executeQuery();
				      if (rs.next())
				      {
				        isAlreadyExsists = true;
				        boeVO.setBillAmt(commonMethods.toDouble(rs.getString("BOE_ENTRY_AMT")));
				        boeVO.setAllocAmt("");
				        boeVO.setOutAmt(commonMethods.toDouble(rs.getString("PD_OUTSTANDING_AMT")));
				        boeVO.setOutAmt_temp(commonMethods.toDouble(rs.getString("PD_OUTSTANDING_AMT")));
				        boeVO.setFullyAlloc(
				          commonMethods.getEmptyIfNull(rs.getString("BOE_PAYMENT_BP_PAY_FULL_YN")).trim());
				        boeVO.setStatus(commonMethods.getEmptyIfNull(rs.getString("STATUS")).trim());
				        boeVO.setRemarks(commonMethods.getEmptyIfNull(rs.getString("REMARKS")).trim());
				        boeVO.setIeCodeChange(commonMethods.getEmptyIfNull(rs.getString("CHANGED_IE_CODE")).trim());
				        boeVO.setBoeExRate(commonMethods.getEmptyIfNull(rs.getString("EXCHANGE_RATE")).trim());
				        boeVO.setExRate(commonMethods.getEmptyIfNull(rs.getString("EXCHANGE_RATE")).trim());
				        boeVO.setBoeBesSBInd(commonMethods.getEmptyIfNull(rs.getString("BES_IND")).trim());
				        logger.info("Outstanding Amt-----11111111111------->" + boeVO.getOutAmt());
				      }
				      if (!commonMethods.isNull(boeVO.getOkIdFlg()))
				      {
				        boeVO.setOkIdFlg(boeVO.getOkIdFlg());
				        logger.info("The OK ID FLG is : " + boeVO.getOkIdFlg());
				      }
				      int boeCount = 0;
				      String boeQuery = "SELECT COUNT(*) AS BOE_COUNT  FROM ETT_BOE_PAYMENT WHERE BOE_PAYMENT_BP_BOE_NO =? AND TO_CHAR(BOE_PAYMENT_BP_BOE_DT,'DD/MM/YYYY') = ? AND PORT_CODE =? ";

				 
				 
				      LoggableStatement pst2 = new LoggableStatement(con, boeQuery);
				      pst2.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
				      pst2.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
				      pst2.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
				      logger.info("BOE COUNT QUERY--->: " + pst2.getQueryString());
				      ResultSet rs2 = pst2.executeQuery();
				      if (rs2.next()) {
				        boeCount = rs2.getInt("BOE_COUNT");
				      }
				      if (boeCount == 0)
				      {
				        boeVO = getTotalBOEAmt(con, boeVO);
				      }
				      else
				      {
				        if (!isAlreadyExsists) {
				          boeVO = getBOEAmt(con, boeVO);
				        }
				        String tEndAmt = getTotalEndoreAmount(boeVO.getBoeNo(), boeVO.getBoeDate(), boeVO.getPortCode());
				        double totalEndAmt = commonMethods.convertToDouble(tEndAmt);
				        double boeAmt = commonMethods.convertToDouble(boeVO.getBillAmt());
				        double availableAmt = boeAmt - totalEndAmt;
				        BigDecimal availAmt = BigDecimal.valueOf(availableAmt).setScale(2, RoundingMode.HALF_UP);
				        boeVO.setActualEndorseAmt(String.valueOf(availAmt));
				        boeVO.setActualEndorseAmt_temp(String.valueOf(availAmt));
				        boeVO = fetchPreviousEndoreAmount_1(con, boeVO);
				        boeVO.setEndorseAmt(boeVO.getTotalEndorseAmt());
				      }
				      closeSqlRefferance(rs2, pst2);
				      int totalBoeCount = 0;
				      String boeCountQuery = "SELECT SUM(BOE_COUNT) AS B_COUNT FROM (SELECT COUNT(*) AS BOE_COUNT FROM ETT_BOE_CLOSURE_DATA  WHERE BOENUMBER = ? AND TO_CHAR(BOEDATE,'DD/MM/YYYY') = ? AND PORTOFDISCHARGE = ?  UNION SELECT COUNT(*) AS BOE_COUNT FROM ETT_BOE_PAYMENT WHERE BOE_PAYMENT_BP_BOE_NO = ?  AND TO_CHAR(BOE_PAYMENT_BP_BOE_DT,'DD/MM/YYYY') = ? AND PORT_CODE = ? ) ";

				 
				 
				 
				      LoggableStatement pst3 = new LoggableStatement(con, boeCountQuery);
				      pst3.setString(1, boeVO.getBoeNo());
				      pst3.setString(2, boeVO.getBoeDate());
				      pst3.setString(3, boeVO.getPortCode());
				      pst3.setString(4, boeVO.getBoeNo());
				      pst3.setString(5, boeVO.getBoeDate());
				      pst3.setString(6, boeVO.getPortCode());
				      ResultSet rs3 = pst3.executeQuery();
				      logger.info("retriveDataBasedOnBillNO1-------query--------" + pst3.getQueryString());
				      if (rs3.next()) {
				        totalBoeCount = rs3.getInt("B_COUNT");
				      }
				      closeSqlRefferance(rs3, pst3);
				      if (totalBoeCount > 0) {
				        boeVO.setManualPartialData("Y");
				      } else {
				        boeVO.setManualPartialData("N");
				      }
				      invoiceList = fetchInvoiceList(con, boeVO);
				      if ((invoiceList.size() > 0) && (!invoiceList.isEmpty()))
				      {
				        boeVO.setInvoiceList(invoiceList);
				        boeVO.setBillCurrency(((TransactionVO)invoiceList.get(0)).getInvoiceCurr());
				      }
				    }
				    catch (Exception e)
				    {
				      logger.info("retriveDataBasedOnBillNO1----------exception-----" + e);
				      e.printStackTrace();
				    }
				    finally
				    {
				      closeSqlRefferance(rs, pst, con);
				    }
				    logger.info("Exiting Method");
				    return boeVO;
				  }
				  public BoeVO fetchPreviousEndoreAmount(Connection con, BoeVO boeVO)
				    throws DAOException
				  {
				    logger.info("Entering Method");
				    LoggableStatement loggableStatement = null;
				    ResultSet rs = null;
				    String sqlQuery = null;
				    CommonMethods commonMethods = null;
				    try
				    {
				      if (con == null) {
				        con = DBConnectionUtility.getConnection();
				      }
				      commonMethods = new CommonMethods();
				      sqlQuery = "SELECT ROUND(SUM(ENDORSED_BOE_AMT),2) AS TOTAL_AMT FROM ETT_BOE_PAYMENT  WHERE BOE_PAYMENT_BP_BOE_NO = ? AND TO_CHAR(BOE_PAYMENT_BP_BOE_DT,'DD/MM/YYYY') = ?  AND PORT_CODE =?  AND STATUS != 'R'  GROUP BY BOE_PAYMENT_BP_BOE_NO ";

				 
				 
				      loggableStatement = new LoggableStatement(con, sqlQuery);
				      loggableStatement.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
				      loggableStatement.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
				      loggableStatement.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
				      logger.info("FetchTotalEndoreAmount For Many Bill: " + 
				        loggableStatement.getQueryString());

				 
				 
				      rs = loggableStatement.executeQuery();
				      int count = 0;
				      if (rs.next())
				      {
				        count++;
				        boeVO.setTotalEndorseAmt(rs.getString("TOTAL_AMT"));
				      }
				      if (count == 0)
				      {
				        boeVO.setTotalEndorseAmt("0");
				        boeVO.setOutAmt("0");
				        boeVO.setFullyAlloc("N");
				      }
				    }
				    catch (SQLException e)
				    {
				      throwDAOException(e);
				    }
				    finally
				    {
				      closeSqlRefferance(rs, loggableStatement);
				    }
				    logger.info("Exiting Method");
				    return boeVO;
				  }
				  public BoeVO fetchPreviousEndoreAmount_1(Connection con, BoeVO boeVO)
				    throws DAOException
				  {
				    logger.info("fetchPreviousEndoreAmount_1-----------");
				    LoggableStatement loggableStatement = null;
				    ResultSet rs = null;
				    String sqlQuery = null;
				    CommonMethods commonMethods = null;
				    try
				    {
				      if (con == null) {
				        con = DBConnectionUtility.getConnection();
				      }
				      commonMethods = new CommonMethods();
				      sqlQuery = "SELECT ROUND(SUM(ENDORSED_BOE_AMT),2) AS TOTAL_AMT FROM ETT_BOE_PAYMENT  WHERE BOE_PAYMENT_BP_BOE_NO = ? AND TO_CHAR(BOE_PAYMENT_BP_BOE_DT,'DD/MM/YYYY') = ? AND PORT_CODE = ? GROUP BY BOE_PAYMENT_BP_BOE_NO ";

				 
				 
				      loggableStatement = new LoggableStatement(con, sqlQuery);
				      loggableStatement.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
				      loggableStatement.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
				      loggableStatement.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
				      logger.info("FetchTotalEndoreAmount For Many Bill: " + 
				        loggableStatement.getQueryString());
				      rs = loggableStatement.executeQuery();
				      int count = 0;
				      if (rs.next())
				      {
				        count++;
				        boeVO.setTotalEndorseAmt(rs.getString("TOTAL_AMT"));
				      }
				      if (count == 0)
				      {
				        boeVO.setTotalEndorseAmt("0");
				        boeVO.setOutAmt("0");
				        boeVO.setFullyAlloc("N");
				      }
				    }
				    catch (SQLException e)
				    {
				      logger.info("fetchPreviousEndoreAmount_1--SQLException---------" + e);
				      throwDAOException(e);
				    }
				    catch (Exception e)
				    {
				      logger.info("fetchPreviousEndoreAmount_1-- Exception---------" + e);
				      throwDAOException(e);
				    }
				    finally
				    {
				      closeSqlRefferance(rs, loggableStatement);
				    }
				    logger.info("Exiting Method");
				    return boeVO;
				  }
				  public ArrayList<TransactionVO> fetchInvoiceList(Connection con, BoeVO boeVO)
				  {
				    logger.info("fetchInvoiceList------------------");
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
				      String query = "SELECT INV_SERIAL_NO,INV_NO,INV_TERMS,FOB_AMT,FOB_CURR,FRI_AMT,FRI_CURR, INS_AMT,INS_CURR,REAL_AMT,REAL_AMTIC AS REAL_ORM_AMT,CLOSURE_AMTIC FROM ETTV_BOE_INV_DETAILS WHERE BOE_NUMBER = ? AND TO_CHAR(BOE_DATE,'DD/MM/YYYY') = ? AND BOE_PORT_OF_DISCHARGE = ? ORDER BY TO_NUMBER(INV_SERIAL_NO) ";

				 
				 
				      pst = new LoggableStatement(con, query);
				      pst.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
				      pst.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
				      pst.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
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
				        String tempVal = commonMethods.getEmptyIfNull(rs.getString("INV_SERIAL_NO")).trim() + ":" + 
				          commonMethods.getEmptyIfNull(rs.getString("INV_NO")).trim();
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
				        invoiceList.add(invoiceVO);
				      }
				    }
				    catch (Exception e)
				    {
				      logger.info("fetchInvoiceList-----------exceptin-------" + e);
				    }
				    finally
				    {
				      closeSqlRefferance(rs, pst);
				    }
				    logger.info("Exiting Method");
				    return invoiceList;
				  }
				  public BoeVO getTotalBOEAmt_Fetching(Connection con, BoeVO boeVO)
				  {
				    logger.info("getTotalBOEAmt---------------");
				    logger.info("Allocate Amount----------boeVO.getPortCode()----" + boeVO.getAllocAmt());
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

				 
				 
				 
				      String query = "SELECT SUM(NVL(INVOICE_FOBAMOUNT,0)) AS TOT_BOE_AMT,INVOICE_FOBCURRENCY AS BOE_CURR FROM ETT_INVOICE_DETAILS WHERE BOE_NUMBER = ? AND TO_CHAR(BOE_DATE,'DD/MM/YYYY') = ? AND BOE_PORT_OF_DISCHARGE = ? AND STATUS = 'A' GROUP BY INVOICE_FOBCURRENCY ";

				 
				 
				      pst = new LoggableStatement(con, query);
				      pst.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
				      pst.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
				      pst.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
				      rs = pst.executeQuery();
				      logger.info("getTotalBOEAmt-------------query" + pst.getQueryString());
				      double totalBoeAmt = 0.0D;
				      String boeCurr = null;
				      if (rs.next())
				      {
				        totalBoeAmt = rs.getDouble("TOT_BOE_AMT");
				        boeCurr = rs.getString("BOE_CURR");
				        logger.info("getTotalBOEAmt--------totalBoeAmt----" + totalBoeAmt);
				        logger.info("getTotalBOEAmt--------boeCurr-----" + boeCurr);
				      }
				      double adjAmtIc = 0.0D;
				      String sqlQuery = "SELECT SUM(ADJ_INVAMT) AS CLOSURE_AMTIC FROM ETT_BOE_INV_CLOSURE  WHERE BOE_NO = ? AND TO_CHAR(BOE_DATE,'DD/MM/YYYY') = ? AND PORTCODE = ? AND STATUS != 'R' ";

				 
				      LoggableStatement pst2 = new LoggableStatement(con, sqlQuery);
				      pst2.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
				      pst2.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
				      pst2.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
				      logger.info("---CLOSURE_AMTIC----------query" + pst2.getQueryString());
				      ResultSet rs2 = pst2.executeQuery();
				      if (rs2.next()) {
				        adjAmtIc = commonMethods.convertToDouble(rs2.getString("CLOSURE_AMTIC"));
				      }
				      closeSqlRefferance(rs2, pst2);
				      double availableAmt = totalBoeAmt - adjAmtIc;
				      logger.info("---availAmt-----totalBoeAmt-----------" + totalBoeAmt);
				      logger.info("---availAmt--------adjAmtIc--------" + adjAmtIc);
				      logger.info("---availAmt--------availableAmt--------" + availableAmt);
				      BigDecimal availAmt = BigDecimal.valueOf(availableAmt).setScale(2, RoundingMode.HALF_UP);
				      logger.info("---availAmt---------availAmt-------" + availAmt);
				      totalAmt = new BigDecimal(totalBoeAmt);
				      totalAmt = totalAmt.setScale(2, RoundingMode.HALF_UP);
				      boeVO.setBillAmt(String.valueOf(totalAmt));
				      boeVO.setActualEndorseAmt(String.valueOf(availAmt));
				      boeVO.setActualEndorseAmt_temp(String.valueOf(availAmt));
				      boeVO.setBillCurrency(boeCurr);
				      boeVO.setEqPaymentAmount("0");
				      boeVO.setAllocAmt("0");

				 
				      logger.info("---availAmt---------setEqPaymentAmount-------" + boeVO.getEqPaymentAmount());
				      if ((!commonMethods.isNull(boeVO.getPaymentCurr())) && (!commonMethods.isNull(boeVO.getBillCurrency()))) {
				        if (boeVO.getPaymentCurr().equalsIgnoreCase(boeVO.getBillCurrency()))
				        {
				          boeVO.setBoeExRate("1");
				          boeVO.setExRate("1");
				        }
				        else
				        {
				          double exRate = getBOE_ExRate(con, boeVO);
				          boeVO.setBoeExRate(String.valueOf(exRate));
				          boeVO.setExRate(String.valueOf(exRate));
				        }
				      }
				    }
				    catch (Exception e)
				    {
				      logger.info("getTotalBOEAmt----------exception-----" + e);
				      e.printStackTrace();
				    }
				    finally
				    {
				      closeSqlRefferance(rs, pst);
				    }
				    logger.info("Exiting Method");
				    return boeVO;
				  }
				  public BoeVO getTotalBOEAmt(Connection con, BoeVO boeVO)
				  {
				    logger.info("getTotalBOEAmt---------------");
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

				 
				 
				 
				      String query = "SELECT SUM(NVL(INVOICE_FOBAMOUNT,0)) AS TOT_BOE_AMT,INVOICE_FOBCURRENCY AS BOE_CURR FROM ETT_INVOICE_DETAILS WHERE BOE_NUMBER = ? AND TO_CHAR(BOE_DATE,'DD/MM/YYYY') = ? AND BOE_PORT_OF_DISCHARGE = ? AND STATUS = 'A' GROUP BY INVOICE_FOBCURRENCY ";

				 
				 
				      pst = new LoggableStatement(con, query);
				      pst.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
				      pst.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
				      pst.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
				      rs = pst.executeQuery();
				      logger.info("getTotalBOEAmt-------------query" + pst.getQueryString());
				      double totalBoeAmt = 0.0D;
				      String boeCurr = null;
				      if (rs.next())
				      {
				        totalBoeAmt = rs.getDouble("TOT_BOE_AMT");
				        boeCurr = rs.getString("BOE_CURR");
				        logger.info("getTotalBOEAmt--------totalBoeAmt----" + totalBoeAmt);
				        logger.info("getTotalBOEAmt--------boeCurr-----" + boeCurr);
				      }
				      double adjAmtIc = 0.0D;
				      String sqlQuery = "SELECT SUM(ADJ_INVAMT) AS CLOSURE_AMTIC FROM ETT_BOE_INV_CLOSURE  WHERE BOE_NO = ? AND TO_CHAR(BOE_DATE,'DD/MM/YYYY') = ? AND PORTCODE = ? AND STATUS != 'R' ";

				 
				      LoggableStatement pst2 = new LoggableStatement(con, sqlQuery);
				      pst2.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
				      pst2.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
				      pst2.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
				      logger.info("---CLOSURE_AMTIC----------query" + pst2.getQueryString());
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

				 
				      boeVO.setEqPaymentAmount(boeVO.getAllocAmt());
				      if ((!commonMethods.isNull(boeVO.getPaymentCurr())) && (!commonMethods.isNull(boeVO.getBillCurrency()))) {
				        if (boeVO.getPaymentCurr().equalsIgnoreCase(boeVO.getBillCurrency()))
				        {
				          boeVO.setBoeExRate("1");
				          boeVO.setExRate("1");
				        }
				        else
				        {
				          double exRate = getBOE_ExRate(con, boeVO);
				          boeVO.setBoeExRate(String.valueOf(exRate));
				          boeVO.setExRate(String.valueOf(exRate));
				        }
				      }
				    }
				    catch (Exception e)
				    {
				      logger.info("getTotalBOEAmt----------exception-----" + e);
				      e.printStackTrace();
				    }
				    finally
				    {
				      closeSqlRefferance(rs, pst);
				    }
				    logger.info("Exiting Method");
				    return boeVO;
				  }
				  public BoeVO getBOEAmt(Connection con, BoeVO boeVO)
				  {
				    logger.info("------------getBOEAmt----------------");
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
				      String boeQuery = "SELECT BOE_ENTRY_AMT,BOE_PAYMENT_BP_BOE_CCY  FROM ETT_BOE_PAYMENT WHERE BOE_PAYMENT_BP_BOE_NO =  ?  AND TO_CHAR(BOE_PAYMENT_BP_BOE_DT,'DD/MM/YYYY') = ?  AND PORT_CODE = ? ";
				      if ((boeVO.getBtnModify() != null) && (boeVO.getBtnModify().equalsIgnoreCase("M"))) {
				        boeQuery = boeQuery + " AND STATUS = 'P' ";
				      }
				      boeQuery = boeQuery + "GROUP BY BOE_PAYMENT_BP_BOE_NO,BOE_PAYMENT_BP_BOE_DT, PORT_CODE,BOE_ENTRY_AMT,BOE_PAYMENT_BP_BOE_CCY ";

				 
				      pst = new LoggableStatement(con, boeQuery);
				      pst.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
				      pst.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
				      pst.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
				      logger.info("------------getBOEAmt---------query-------" + pst.getQueryString());
				      rs = pst.executeQuery();
				      double totalBoeAmt = 0.0D;
				      String boeCurr = null;
				      if (rs.next())
				      {
				        totalBoeAmt = rs.getDouble("BOE_ENTRY_AMT");
				        boeCurr = rs.getString("BOE_PAYMENT_BP_BOE_CCY");
				      }
				      logger.info("Get BOE Value " + totalBoeAmt);
				      totalAmt = new BigDecimal(totalBoeAmt);
				      totalAmt = totalAmt.setScale(2, RoundingMode.HALF_UP);
				      boeVO.setBillAmt(String.valueOf(totalAmt));
				      boeVO.setBillCurrency(boeCurr);
				      boeVO.setEqPaymentAmount("0");
				      if ((!commonMethods.isNull(boeVO.getPaymentCurr())) && (!commonMethods.isNull(boeVO.getBillCurrency()))) {
				        if (boeVO.getPaymentCurr().equalsIgnoreCase(boeVO.getBillCurrency()))
				        {
				          boeVO.setBoeExRate("1");
				          boeVO.setExRate("1");
				        }
				        else
				        {
				          double exRate = getBOE_ExRate(con, boeVO);
				          boeVO.setBoeExRate(String.valueOf(exRate));
				          boeVO.setExRate(String.valueOf(exRate));
				        }
				      }
				    }
				    catch (Exception e)
				    {
				      logger.info("------------getBOEAmt------------exception----" + e);
				      e.printStackTrace();
				    }
				    finally
				    {
				      closeSqlRefferance(rs, pst);
				    }
				    logger.info("Exiting Method");
				    return boeVO;
				  }
				  public double getBOE_ExRate(Connection con, BoeVO boeVO)
				  {
				    logger.info("Entering Method");
				    LoggableStatement pst = null;
				    ResultSet rs = null;
				    double exRate = 0.0D;
				    CommonMethods commonMethods = null;
				    try
				    {
				      if (con == null) {
				        con = DBConnectionUtility.getConnection();
				      }
				      commonMethods = new CommonMethods();
				      String query = "SELECT ETT_BOE_EX_RATE_CAL(?,?) AS EX_RATE FROM DUAL ";
				      pst = new LoggableStatement(con, query);
				      pst.setString(1, commonMethods.getEmptyIfNull(boeVO.getPaymentCurr()).trim());
				      pst.setString(2, commonMethods.getEmptyIfNull(boeVO.getBillCurrency()).trim());
				      rs = pst.executeQuery();
				      if (rs.next()) {
				        exRate = rs.getDouble("EX_RATE");
				      }
				      logger.info("BOE Ex-Rate Value ---> " + exRate);
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
				    return exRate;
				  }
				  public BoeVO fetchAllocateInvoice(BoeVO boeVO)
						    throws DAOException
						  {
						    logger.info("----------------fetchAllocateInvoice--------------");
						    Connection con = null;
						    LoggableStatement pst = null;
						    ResultSet rs = null;
						    CommonMethods commonMethods = null;
						    ArrayList<TransactionVO> invoiceList = null;
						    try
						    {
						      con = DBConnectionUtility.getConnection();
						      commonMethods = new CommonMethods();
						      invoiceList = new ArrayList();
						      logger.info(
						        "The Non Upaded Warning Value is : ============================>>> " + boeVO.getHideFromWarning());
						      double payEndorseAmt = commonMethods.convertToDouble(commonMethods.toDouble(boeVO.getAllocAmt()));

						 
						      double exRate = commonMethods.convertToDouble(commonMethods.toDouble(boeVO.getExRate()));
						      logger.info("exRate - " + exRate);
						      String query = "SELECT INV_SERIAL_NO,INV_NO,INV_TERMS,FOB_AMT,FOB_CURR,FRI_AMT,FRI_CURR, INS_AMT,INS_CURR,REAL_AMT,REAL_AMTIC AS REAL_ORM_AMT,CLOSURE_AMTIC FROM ETTV_BOE_INV_DETAILS WHERE BOE_NUMBER = ? AND TO_CHAR(BOE_DATE,'DD/MM/YYYY') = ? AND BOE_PORT_OF_DISCHARGE = ? ORDER BY TO_NUMBER(INV_SERIAL_NO) ";

						 
						 
						      pst = new LoggableStatement(con, query);
						      pst.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
						      pst.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
						      pst.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
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
						        String tempVal = commonMethods.getEmptyIfNull(rs.getString("INV_SERIAL_NO")).trim() + ":" + 
						          commonMethods.getEmptyIfNull(rs.getString("INV_NO")).trim();
						        invoiceVO.setUtilityRefNo(tempVal);
						        double fobAmt = commonMethods.convertToDouble(rs.getString("FOB_AMT"));
						        double friAmt = commonMethods.convertToDouble(rs.getString("FRI_AMT"));
						        double insAmt = commonMethods.convertToDouble(rs.getString("INS_AMT"));

						 
						 
						        double totalAmt = fobAmt;
						        double totalInvAmt = Math.round(totalAmt * 100.0D) / 100.0D;
						        logger.info("The total Invoice Amount is : ======================>>> " + totalInvAmt);
						        BigDecimal toInvAmt = BigDecimal.valueOf(totalInvAmt).setScale(2, RoundingMode.HALF_UP);
						        invoiceVO.setTotalInvAmt(String.valueOf(toInvAmt));
						        double totalBOEAmt = commonMethods.convertToDouble(commonMethods.toDouble(boeVO.getBillAmt()));

						 
						 
						 
						        double boeEndorseAmt = commonMethods
						          .convertToDouble(commonMethods.toDouble(boeVO.getEqPaymentAmount()));

						 
						        double rAmtIC = commonMethods.convertToDouble(rs.getString("REAL_ORM_AMT"));
						        double closureAmtIc = commonMethods.convertToDouble(rs.getString("CLOSURE_AMTIC"));
						        invoiceVO.setClosureAmt(String.valueOf(closureAmtIc));
						        double totalPaidAmt = rAmtIC + closureAmtIc;
						        totalPaidAmt = Math.round(totalPaidAmt * 100.0D) / 100.0D;
						        double outAmtIC = totalInvAmt - totalPaidAmt;
						        outAmtIC = Math.round(outAmtIC * 100.0D) / 100.0D;
						        BigDecimal toOutAmt = BigDecimal.valueOf(outAmtIC).setScale(2, RoundingMode.HALF_UP);
						        double realAmt = 0.0D;
						        double realAmtIC = 0.0D;
						        double dOutAmt = Math.round(toOutAmt.doubleValue() / exRate * 100.0D) / 100.0D;
						        if (toOutAmt.compareTo(BigDecimal.ZERO) > 0)
						        {
						          if (totalBOEAmt > 0.0D)
						          {
						            if (payEndorseAmt >= dOutAmt)
						            {
						              realAmt = dOutAmt;
						              realAmtIC = Math.round(dOutAmt * exRate * 100.0D) / 100.0D;
						              payEndorseAmt -= dOutAmt;
						            }
						            else if (payEndorseAmt <= dOutAmt)
						            {
						              realAmt = Math.round(payEndorseAmt * 100.0D) / 100.0D;
						              realAmtIC = Math.round(payEndorseAmt * exRate * 100.0D) / 100.0D;

						 
						              payEndorseAmt = 0.0D;
						            }
						          }
						          else
						          {
						            realAmt = 0.0D;
						            realAmtIC = 0.0D;
						          }
						          logger.info("The Total Endorse Amount is : " + payEndorseAmt);
						          logger.info("The Total Endorse Amount is : " + dOutAmt);
						          logger.info("Realized Amount is In Invoice Settlement : " + realAmt);
						        }
						        invoiceVO.setRealizedAmount(String.valueOf(realAmt));
						        invoiceVO.setRealizedAmountIC(String.valueOf(realAmtIC));

						 
						        invoiceVO.setOutAmtIC(String.valueOf(toOutAmt));
						        invoiceList.add(invoiceVO);
						      }
						      if (invoiceList.size() > 0) {
						        boeVO.setInvoiceList(invoiceList);
						      }
						    }
						    catch (Exception e)
						    {
						      logger.info("----------------fetchAllocateInvoice-------------exception-");
						      e.printStackTrace();
						    }
						    finally
						    {
						      closeSqlRefferance(rs, pst, con);
						    }
						    logger.info("Exiting Method");
						    return boeVO;
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
						      pst = new LoggableStatement(con, "SELECT COUNT(*) AS CROSS_COUNT FROM ETT_INVOICE_DETAILS  WHERE BOE_NUMBER = ? AND TO_CHAR(BOE_DATE,'DD/MM/YYYY') = ? AND BOE_PORT_OF_DISCHARGE  = ? AND (TRIM(INVOICE_FOBCURRENCY) != NVL(INVOICE_FRIEGHTCURRENCYCODE,TRIM(INVOICE_FOBCURRENCY)) OR TRIM(INVOICE_FOBCURRENCY)   != NVL(INVOICE_INSURANCECURRENCY_CODE,TRIM(INVOICE_FOBCURRENCY))) AND INVOICE_SERIAL_NUMBER = ? AND INVOICE_NUMBER = ?");
						      pst.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
						      pst.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
						      pst.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
						      pst.setString(4, commonMethods.getEmptyIfNull(boeVO.getInvoiceSerialNumber()).trim());
						      pst.setString(5, commonMethods.getEmptyIfNull(boeVO.getInvoiceNumber()).trim());
						      rs = pst.executeQuery();
						      if (rs.next()) {
						        count = rs.getInt("CROSS_COUNT");
						      }
						    }
						    catch (Exception e)
						    {
						      e.printStackTrace();
						    }
						    logger.info("Exiting Method");
						    return count;
						  }
						  public int isIECodeCount(BoeVO boeVO)
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
						      pst = new LoggableStatement(con, "SELECT COUNT(*) AS IE_COUNT FROM ETTV_BOE_PAYMENT_DETAILS WHERE PD_TXN_REF = ?  AND PD_PART_PAY_REF = ?  AND PD_IE_CODE = ? ");
						      pst.setString(1, commonMethods.getEmptyIfNull(boeVO.getPaymentRefNo()).trim());
						      pst.setString(2, commonMethods.getEmptyIfNull(boeVO.getPartPaymentSlNo()).trim());
						      pst.setString(3, commonMethods.getEmptyIfNull(boeVO.getIeCode()).trim());
						      rs = pst.executeQuery();
						      if (rs.next()) {
						        count = rs.getInt("IE_COUNT");
						      }
						    }
						    catch (Exception e)
						    {
						      e.printStackTrace();
						    }
						    logger.info("Exiting Method");
						    return count;
						  }
						  public String storeBillData(String[] chkInvlist, BoeVO boeVO)
						    throws DAOException, SQLException
						  {
						    logger.info("Entering Method");
						    String sExtby = "2";
						    Connection con = null;
						    LoggableStatement loggableStatement = null;
						    CallableStatement cs = null;
						    ResultSet rs = null;
						    CommonMethods commonMethods = null;
						    int insCount = 0;
						    String result = null;
						    try
						    {
						      HttpSession session = ServletActionContext.getRequest().getSession();
						      String loginedUserId = (String)session.getAttribute("loginedUserId");
						      con = DBConnectionUtility.getConnection();
						      commonMethods = new CommonMethods();
						      int count = 0;

						 
						 
						      String sPorcOutput = "";
						      String sProcName = "{call ETT_BOE_EXP_VALIDATION(?, ?, ?, ?)}";
						      cs = con.prepareCall(sProcName);
						      cs.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
						      cs.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
						      cs.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
						      cs.setString(4, sExtby);
						      if (cs.executeUpdate() <= 0) {
						        logger.info("Procedure having some problem ======================>>> ");
						      }
						      if (cs != null) {
						        cs.close();
						      }
						      String checkQuery = "SELECT COUNT(*) AS BOE_COUNT FROM   ETT_BOE_PAYMENT WHERE BOE_PAYMENT_BP_PAY_REF=?   AND  BOE_PAYMENT_BP_PAY_PART_REF=?  AND BOE_PAYMENT_BP_BOE_NO= ?  AND TO_CHAR(BOE_PAYMENT_BP_BOE_DT,'DD/MM/YYYY') = ?   AND PORT_CODE = ?";

						 
						 
						 
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
						      logger.info("CHecking BOE Status COUNT-------------------" + count);
						      logger.info("CHecking BOE Status COUNT-------------------" + count);
						      logger.info("Button type Mode-------------->" + boeVO.getBtnModify());
						      logger.info("Button type Mode-------------->" + boeVO.getBtnModify());
						      if ((count > 0) && (!boeVO.getBtnModify().equalsIgnoreCase("M")))
						      {
						        logger.info("---------------------Update Part Executed");
						        insCount = updateBillData(con, boeVO);
						        logger.info("--------------------");
						      }
						      else if ((boeVO.getBtnModify() != null) && (!boeVO.getBtnModify().equalsIgnoreCase("")))
						      {
						        logger.info(
						          "111111111111111111-------------Modification--------Executed---------1111111111111111111");
						        insCount = updateBOEModification(con, boeVO);
						        logger.info("2222222222222222--------------------");
						      }
						      else
						      {
						        logger.info("BOE-------------INSERTION---------------------------");
						        String sqlQuery = "INSERT INTO ETT_BOE_PAYMENT(BOE_PAYMENT_CIF_ID,BOE_PAYMENT_CIF_NAME, BOE_PAYMENT_BP_BOE_NO,BOE_PAYMENT_BP_BOE_DT,BOE_PAYMENT_BP_PAY_DT,BOE_PAYMENT_BP_PAY_REF, BOE_PAYMENT_BP_PAY_PART_REF,BOE_ENTRY_AMT,BOE_PAYMENT_BP_PAY_ENDORSE_AMT, BOE_PAYMENT_BP_PAY_OS_FC_AMT,BOE_PAYMENT_BP_PAY_EDS_FC_AMT,BOE_PAYMENT_BP_PAY_FC_AMT, BOE_PAYMENT_BP_PAY_FULL_YN,REMARKS,BOE_PAYMENT_BP_PAY_CCY,BOE_PAYMENT_BP_BOE_CCY, PORT_CODE,USERID,CREATEDDATE,STATUS,BOE_PAYMENT_BENEF_NAME, BOE_PAYMENT_BENEF_COUNTRY,BOE_MAWB_MBLNUMBER,BOE_MAWB_MBLDATE,BOE_HAWB_HBLNUMBER,BOE_HAWB_HBLDATE, BOE_IGMNUMBER,BOE_IGMDATE,THIRD_PARTY_PAYMENT,CHANGED_IE_CODE,RECORD_INDICATOR,ADCODE,IECODE, PORT_OF_SHIPMENT,IMPORT_AGENCY,IE_ADDRESS,IE_PAN,G_P,ENDORSED_BOE_AMT,EXCHANGE_RATE,BOE_TRANS_TYPE,PAGETYPE, BES_IND, PRE_ENDORSED_AMT) VALUES (?,?,?,TO_DATE(?,'DD-MM-YYYY'),TO_DATE(?,'DD-MM-YYYY'),?,?,?,?, ?,?,?,?,?,?,?,?,?,SYSTIMESTAMP,?,?,?,?, TO_DATE(?,'DD-MM-YYYY'),?,TO_DATE(?,'DD-MM-YYYY'),?,TO_DATE(?,'DD-MM-YYYY'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, ?)";

						 
						 
						 
						 
						 
						 
						 
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
						        logger.info("Payment Currency------------------------->" + boeVO.getPaymentCurr());
						        pst.setString(16, commonMethods.getEmptyIfNull(boeVO.getBillCurrency()).trim());
						        logger.info("Bill Currency-------------------->" + boeVO.getBillCurrency());
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
						        pst.setString(41, "S");
						        pst.setString(42, CommonMethods.checkForNullvalue(boeVO.getBoeBesSBInd()));
						        pst.setString(43, CommonMethods.checkForNullvalue(boeVO.getAllocAmt()));
						        insCount = pst.executeUpdate();
						        logger.info("Insert Payment Count---------------------->" + insCount);
						        logger.info("Insert Payment Count---------------------->" + insCount);
						        closePreparedStatement(pst);
						      }
						      int invCount = 0;
						      if ((insCount > 0) && (!boeVO.getBtnModify().equalsIgnoreCase("M")))
						      {
						        logger.info("This is inside of insert invoice details");
						        invCount = storeInvoiceData(con, chkInvlist, boeVO);
						      }
						      else if ((boeVO.getBtnModify() != null) && (boeVO.getBtnModify().equalsIgnoreCase("M")))
						      {
						        invCount = updateInvoiceData(con, chkInvlist, boeVO);
						      }
						      if (invCount > 0) {
						        result = "success";
						      } else {
						        result = "fail";
						      }
						    }
						    catch (Exception e)
						    {
						      logger.info("storeBillData-----Main Exception-----" + e.getMessage());
						    }
						    finally
						    {
						      cs.close();
						      closeSqlRefferance(rs, loggableStatement, con);
						    }
						    logger.info("Exiting Method");
						    return result;
						  }
						  private int updateBillData(Connection con, BoeVO boeVO)
						    throws DAOException
						  {
						    logger.info("Entering Method");
						    LoggableStatement pst = null;
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
						      String sqlQuery = " UPDATE ETT_BOE_PAYMENT SET BOE_PAYMENT_BP_BOE_DT = TO_DATE(?,'DD-MM-YYYY'), BOE_PAYMENT_BP_PAY_FC_AMT = ?, BOE_PAYMENT_BP_PAY_ENDORSE_AMT = TO_NUMBER(BOE_PAYMENT_BP_PAY_ENDORSE_AMT) + TO_NUMBER(?), BOE_PAYMENT_BP_PAY_EDS_FC_AMT  = TO_NUMBER(BOE_PAYMENT_BP_PAY_EDS_FC_AMT) + TO_NUMBER (?), BOE_PAYMENT_BP_PAY_OS_FC_AMT  = ?, BOE_PAYMENT_BP_PAY_FULL_YN = ?, BOE_PAYMENT_BP_BOE_CCY = ?, BOE_ENTRY_AMT=?, ENDORSED_BOE_AMT = TO_NUMBER(ENDORSED_BOE_AMT) + TO_NUMBER(?), PORT_CODE = ?, USERID = ?,STATUS = ?, REMARKS = ?, THIRD_PARTY_PAYMENT =?, CHANGED_IE_CODE = ?, BOE_TRANS_TYPE = ?, PAGETYPE =?, LASTUPDATE = SYSTIMESTAMP, BES_IND =?, PRE_ENDORSED_AMT = TO_NUMBER(?) \tWHERE BOE_PAYMENT_BP_PAY_REF = ? AND BOE_PAYMENT_BP_PAY_PART_REF = ? AND BOE_PAYMENT_BP_BOE_NO = ? AND TO_CHAR(BOE_PAYMENT_BP_BOE_DT,'DD/MM/YYYY') = ? AND PORT_CODE = ?";

						 
						 
						 
						 
						 
						 
						 
						      pst = new LoggableStatement(con, sqlQuery);
						      pst.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
						      pst.setString(2, CommonMethods.setDefaultAmount(boeVO.getPaymentAmount()));
						      pst.setString(3, CommonMethods.setDefaultAmount(boeVO.getAllocAmt()));
						      pst.setString(4, CommonMethods.setDefaultAmount(boeVO.getAllocAmt()));
						      pst.setString(5, CommonMethods.setDefaultAmount(boeVO.getOutAmt()));
						      pst.setString(6, commonMethods.getEmptyIfNull(boeVO.getFullyAlloc()).trim());
						      pst.setString(7, commonMethods.getEmptyIfNull(boeVO.getPaymentCurr()).trim());
						      pst.setString(8, commonMethods.getEmptyIfNull(boeVO.getBillAmt()).trim());
						      pst.setString(9, CommonMethods.setDefaultAmount(boeVO.getEqPaymentAmount()));
						      pst.setString(10, loginedUserId);
						      pst.setString(11, "P");
						      pst.setString(12, commonMethods.getEmptyIfNull(boeVO.getRemarks()).trim());
						      pst.setString(13, commonMethods.getEmptyIfNull(boeVO.getThrdParty()).trim());
						      pst.setString(14, commonMethods.getEmptyIfNull(boeVO.getIeCodeChange()).trim());
						      pst.setString(15, commonMethods.getEmptyIfNull(boeVO.getTransType()).trim());
						      pst.setString(16, "S");
						      pst.setString(17, commonMethods.getEmptyIfNull(boeVO.getBoeBesSBInd()).trim());
						      pst.setString(18, CommonMethods.setDefaultAmount(boeVO.getEqPaymentAmount()));
						      pst.setString(19, commonMethods.getEmptyIfNull(boeVO.getPaymentRefNo()).trim());
						      pst.setString(20, commonMethods.getEmptyIfNull(boeVO.getPartPaymentSlNo()).trim());
						      pst.setString(21, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
						      pst.setString(22, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
						      pst.setString(23, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
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
						  public int storeInvoiceData(Connection con, String[] chkList, BoeVO boeVO)
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
								      logger.info("chkList----------------------->" + chkList);
								      logger.info("chkList----------------------->" + chkList);
								      if ((chkList != null) && (chkList.length > 0)) {
								        for (int i = 0; i < chkList.length; i++)
								        {
								          String tempRefNo = chkList[i];
								          if (!tempRefNo.equalsIgnoreCase("false"))
								          {
								            String[] s = chkList[i].split(":");
								            String invSerialNo = commonMethods.getEmptyIfNull(s[0]).trim();
								            String invNo = commonMethods.getEmptyIfNull(s[1]).trim();
								            String realAmt = commonMethods.getEmptyIfNull(s[2]).trim();
								            logger.info("invSerialNo------------------" + invSerialNo);
								            logger.info("invSerialNo------------------" + invSerialNo);
								            logger.info("invNo------------------" + invNo);
								            logger.info("invNo------------------" + invNo);
								            logger.info("realAmt------------------" + realAmt);
								            logger.info("realAmt------------------" + realAmt);

								 
								            String sQuery = "SELECT INVPAY.INV_AMT, INVPAY.REAL_ORM_AMT, INVPAY.PAYMENT_SRL FROM ETT_BOE_INV_PAYMENT INVPAY, ETT_INVOICE_DETAILS INVDET WHERE INVPAY.BOE_NO = ? AND INVPAY.BOE_DATE = TO_DATE(?,'DD/MM/YYYY') AND INVPAY.PORTCODE = ? AND INVPAY.INV_NO   = ? AND INVPAY.BOE_NO   = INVDET.BOE_NUMBER AND INVPAY.BOE_DATE = INVDET.BOE_DATE AND INVPAY.PORTCODE = INVDET.BOE_PORT_OF_DISCHARGE AND INVPAY.INV_NO   = INVDET.INVOICE_NUMBER AND INVDET.STATUS  = 'A' AND INVPAY.PAYMENT_SRL = (SELECT MAX(PAYMENT_SRL) FROM ETT_BOE_INV_PAYMENT WHERE BOE_NO = ? AND BOE_DATE = TO_DATE(?,'DD/MM/YYYY') AND PORTCODE = ?)";
								            LoggableStatement pst5 = new LoggableStatement(con, sQuery);
								            pst5.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
								            pst5.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
								            pst5.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
								            pst5.setString(4, invNo);
								            pst5.setString(5, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
								            pst5.setString(6, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
								            pst5.setString(7, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
								            ResultSet rs5 = pst5.executeQuery();
								            if (rs5.next())
								            {
								              dInvAmt = rs5.getDouble(1);
								              dRelOrmAmt = rs5.getDouble(2);
								              sPaySrl = rs5.getString(3);
								            }
								            if (dInvAmt - dRelOrmAmt > 0.0D)
								            {
								              Date date = new Date();
								              String sDate = sdf.format(date);
								              sSequence = ValidationUtility.dateConvertToDDMMYYYString(sDate, sPaySrl);
								              logger.info("The payment sequence is : =====================>>> sSequence : " + sSequence);
								            }
								            String query = "SELECT INVOICE_SERIAL_NUMBER AS INV_SNO,INVOICE_NUMBER AS INVNO,INVOICE_TERMS_OF_INVOICE AS TERM_INV, NVL(INVOICE_FOBAMOUNT,0) AS TOT_INV_AMT, INVOICE_FOBCURRENCY AS INV_CURR FROM ETT_INVOICE_DETAILS WHERE  BOE_NUMBER = ? AND TO_CHAR(BOE_DATE,'DD/MM/YYYY') = ? AND BOE_PORT_OF_DISCHARGE = ? AND INVOICE_SERIAL_NUMBER = ? AND INVOICE_NUMBER = ?  ";

								 
								 
								            LoggableStatement pst1 = new LoggableStatement(con, query);
								            pst1.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
								            pst1.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
								            pst1.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
								            pst1.setString(4, invSerialNo);
								            pst1.setString(5, invNo);
								            ResultSet rs1 = pst1.executeQuery();
								            if (rs1.next())
								            {
								              String termsOfInvoice = commonMethods.getEmptyIfNull(rs1.getString("TERM_INV")).trim();
								              String totalInvAmt = commonMethods.getEmptyIfNull(rs1.getString("TOT_INV_AMT")).trim();
								              String invCurr = commonMethods.getEmptyIfNull(rs1.getString("INV_CURR")).trim();
								              double realInvAmt = commonMethods.convertToDouble(commonMethods.toDouble(realAmt));
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
								              String insertQuery = "INSERT INTO ETT_BOE_INV_PAYMENT(INV_SNO,INV_NO,INV_AMT,REAL_AMT,REAL_ORM_AMT, INVOICECURR,TERMS_OF_INVOICE,BOE_NO,BOE_DATE,PORTCODE,PAYMENT_REF,EVENT_REF,RINV_SEQNO, PAYMENT_SRL, REAL_MOD_AMT, REAL_ORM_MOD_AMT) VALUES(?,?,?,?,?,?,?,?,TO_DATE(?,'DD-MM-YYYY'),?,?,?,?, ?, ?, ?) ";

								 
								 
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
								              pst2.setString(14, sSequence);
								              pst2.setString(15, String.valueOf(realInvAmt));
								              pst2.setString(16, String.valueOf(realInvAmtIC));
								              count += pst2.executeUpdate();
								              closePreparedStatement(pst2);
								              logger.info("Ivoice Insertion Count-------------------" + count);
								            }
								            closeSqlRefferance(rs1, pst1);
								          }
								        }
								      }
								    }
								    catch (Exception e)
								    {
								      logger.info("storeInvoiceData  Exception--------------" + e.getMessage());
								      logger.info("storeInvoiceData  Exception--------------" + e.getMessage());
								    }
								    logger.info("Exiting Method");
								    return count;
								  }
						  public TransactionVO fetchInvoiceData(BoeVO boeVO)
								    throws DAOException
								  {
								    logger.info("-------------fetchInvoiceData-----------");
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
								      pst.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
								      pst.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
								      pst.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
								      pst.setString(4, commonMethods.getEmptyIfNull(invSerialNo).trim());
								      pst.setString(5, commonMethods.getEmptyIfNull(invNo).trim());
								      logger.info("Invoice Query --> " + pst.getQueryString());
								      rs = pst.executeQuery();
								      if (rs.next())
								      {
								        invoiceVO.setInvSno(commonMethods.getEmptyIfNull(rs.getString("INV_SERIAL_NO")).trim());
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
								      closeSqlRefferance(rs, pst);
								    }
								    logger.info("Exiting Method");
								    return invoiceVO;
								  }
								  public TransactionVO insertInvoiceDatatoTable(BoeVO boeVO, TransactionVO transactionVO)
								    throws DAOException
								  {
								    logger.info("Entering Method");
								    Connection con = null;
								    ResultSet rs = null;
								    LoggableStatement pst = null;
								    CommonMethods commonMethods = null;
								    try
								    {
								      logger.info("Invoice------------------Method=--------------------");
								      con = DBConnectionUtility.getConnection();
								      commonMethods = new CommonMethods();
								      String query = "";
								      if ((!commonMethods.isNull(boeVO.getButtonType())) && (boeVO.getButtonType().equalsIgnoreCase("Update")))
								      {
								        logger.info(">>>>>>>>>>>>>>>>...Inside the Update Black>>>>>>>>>>>>>");
								        query = "UPDATE ETT_INVOICE_DETAILS SET INVOICE_SERIAL_NUMBER=?,INVOICE_NUMBER=?,INVOICE_FOBAMOUNT=?,INVOICE_FOBCURRENCY=?, INVOICE_TERMS_OF_INVOICE=?,INVOICE_SUPPLIER_NAME=?,INVOICE_SUPPLIER_ADDRESS=?,INVOICE_SUPPLIER_COUNTRY=?, INVOICE_FRIEGHTAMOUNT=?,INVOICE_FRIEGHTCURRENCYCODE=?,INVOICE_INSURANCEAMOUNT=?,INVOICE_INSURANCECURRENCY_CODE=?, INVOICE_AGENCY_COMMISSION=?,INVOICE_AGENCY_CURRENCY=?,INVOICE_DISCOUNT_CHARGES=?,INVOICE_DISCOUNT_CURRENCY=?, INVOICE_MISCELLANEOUS_CHARGES=?,INVOICE_MISCELLANEOUS_CURRENCY=?,INVOICE_SELLER_NAME=?,INVOICE_SELLER_ADDRESS=?, INVOICE_SELLER_COUNTRY=?,INVOICE_THIRDPARTY_NAME=?,INVOICE_THIRDPARTY_ADDRESS=?, INVOICE_THIRDPARTY_COUNTRY=?,STATUS='P' WHERE BOE_NUMBER = ? AND BOE_DATE= TO_DATE(?,'DD/MM/YYYY') AND BOE_PORT_OF_DISCHARGE = ? AND INVOICE_SERIAL_NUMBER = ? AND INVOICE_NUMBER = ? ";

								 
								 
								 
								 
								        pst = new LoggableStatement(con, query);
								        pst.setString(1, commonMethods.getEmptyIfNull(transactionVO.getInvSno()).trim());
								        pst.setString(2, commonMethods.getEmptyIfNull(transactionVO.getInvoiceNumber()).trim());
								        pst.setString(3, commonMethods.getEmptyIfNull(transactionVO.getInvAmt()).trim());
								        pst.setString(4, commonMethods.getEmptyIfNull(transactionVO.getInvoiceCurr()).trim());
								        pst.setString(5, commonMethods.getEmptyIfNull(transactionVO.getTermsofInvoice()).trim());
								        pst.setString(6, commonMethods.getEmptyIfNull(transactionVO.getSupplierName()).trim());
								        pst.setString(7, commonMethods.getEmptyIfNull(transactionVO.getSupplierAddress()).trim());
								        pst.setString(8, commonMethods.getEmptyIfNull(transactionVO.getSupplierCountry()).trim());
								        pst.setString(9, commonMethods.getEmptyIfNull(transactionVO.getFreightAmount()).trim());
								        pst.setString(10, commonMethods.getEmptyIfNull(transactionVO.getFreightCurrencyCode()).trim());
								        pst.setString(11, commonMethods.getEmptyIfNull(transactionVO.getInsuranceAmount()).trim());
								        pst.setString(12, commonMethods.getEmptyIfNull(transactionVO.getInsuranceCurrencyCode()).trim());
								        pst.setString(13, commonMethods.getEmptyIfNull(transactionVO.getAgencyCommission()).trim());
								        pst.setString(14, commonMethods.getEmptyIfNull(transactionVO.getAgencyCurrency()).trim());
								        pst.setString(15, commonMethods.getEmptyIfNull(transactionVO.getDiscountCharges()).trim());
								        pst.setString(16, commonMethods.getEmptyIfNull(transactionVO.getDiscountCurrency()).trim());
								        pst.setString(17, commonMethods.getEmptyIfNull(transactionVO.getMiscellaneousCharges()).trim());
								        pst.setString(18, commonMethods.getEmptyIfNull(transactionVO.getMiscellaneousCurrency()).trim());
								        pst.setString(19, commonMethods.getEmptyIfNull(transactionVO.getSellerName()).trim());
								        pst.setString(20, commonMethods.getEmptyIfNull(transactionVO.getSellerAddress()).trim());
								        pst.setString(21, commonMethods.getEmptyIfNull(transactionVO.getSellerCountry()).trim());
								        pst.setString(22, commonMethods.getEmptyIfNull(transactionVO.getThirdPartyName()).trim());
								        pst.setString(23, commonMethods.getEmptyIfNull(transactionVO.getThirdPartyAddress()).trim());
								        pst.setString(24, commonMethods.getEmptyIfNull(transactionVO.getThirdPartyCountry()).trim());
								        pst.setString(25, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
								        pst.setString(26, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
								        pst.setString(27, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
								        pst.setString(28, commonMethods.getEmptyIfNull(transactionVO.getInvSno()).trim());
								        pst.setString(29, commonMethods.getEmptyIfNull(transactionVO.getInvoiceNumber()).trim());
								      }
								      else if ((!commonMethods.isNull(boeVO.getButtonType())) && 
								    	        (boeVO.getButtonType().equalsIgnoreCase("Insert")))
								    	      {
								    	        logger.info(">>>>>>>>>>>>>>>>...Inside the Insert Black>>>>>>>>>>>>>");
								    	        query = "INSERT INTO ETT_INVOICE_DETAILS(INVOICE_SERIAL_NUMBER,INVOICE_NUMBER,INVOICE_FOBAMOUNT,INVOICE_FOBCURRENCY,INVOICE_TERMS_OF_INVOICE,BOE_NUMBER,BOE_DATE,BOE_PORT_OF_DISCHARGE,INVOICE_SUPPLIER_NAME,INVOICE_SUPPLIER_ADDRESS,INVOICE_SUPPLIER_COUNTRY,INVOICE_FRIEGHTAMOUNT,INVOICE_FRIEGHTCURRENCYCODE,INVOICE_INSURANCEAMOUNT,INVOICE_INSURANCECURRENCY_CODE,INVOICE_AGENCY_COMMISSION,INVOICE_AGENCY_CURRENCY,INVOICE_DISCOUNT_CHARGES,INVOICE_DISCOUNT_CURRENCY,INVOICE_MISCELLANEOUS_CHARGES,INVOICE_MISCELLANEOUS_CURRENCY,INVOICE_SELLER_NAME,INVOICE_SELLER_ADDRESS,INVOICE_SELLER_COUNTRY,INVOICE_THIRDPARTY_NAME,INVOICE_THIRDPARTY_ADDRESS,INVOICE_THIRDPARTY_COUNTRY,STATUS) VALUES(?,?,?,?,?,?,TO_DATE(?,'DD/MM/YYYY'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

								    	 
								    	 
								    	 
								    	 
								    	        pst = new LoggableStatement(con, query);
								    	        pst.setString(1, commonMethods.getEmptyIfNull(transactionVO.getInvSno()).trim());
								    	        pst.setString(2, commonMethods.getEmptyIfNull(transactionVO.getInvoiceNumber()).trim());
								    	        pst.setString(3, commonMethods.getEmptyIfNull(transactionVO.getInvAmt()).trim());
								    	        pst.setString(4, commonMethods.getEmptyIfNull(transactionVO.getInvoiceCurr()).trim());
								    	        pst.setString(5, commonMethods.getEmptyIfNull(transactionVO.getTermsofInvoice()).trim());
								    	        pst.setString(6, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
								    	        pst.setString(7, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
								    	        pst.setString(8, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
								    	        pst.setString(9, commonMethods.getEmptyIfNull(transactionVO.getSupplierName()).trim());
								    	        pst.setString(10, commonMethods.getEmptyIfNull(transactionVO.getSupplierAddress()).trim());
								    	        pst.setString(11, commonMethods.getEmptyIfNull(transactionVO.getSupplierCountry()).trim());
								    	        pst.setString(12, commonMethods.getEmptyIfNull(transactionVO.getFreightAmount()).trim());
								    	        pst.setString(13, commonMethods.getEmptyIfNull(transactionVO.getFreightCurrencyCode()).trim());
								    	        pst.setString(14, commonMethods.getEmptyIfNull(transactionVO.getInsuranceAmount()).trim());
								    	        pst.setString(15, commonMethods.getEmptyIfNull(transactionVO.getInsuranceCurrencyCode()).trim());
								    	        pst.setString(16, commonMethods.getEmptyIfNull(transactionVO.getAgencyCommission()).trim());
								    	        pst.setString(17, commonMethods.getEmptyIfNull(transactionVO.getAgencyCurrency()).trim());
								    	        pst.setString(18, commonMethods.getEmptyIfNull(transactionVO.getDiscountCharges()).trim());
								    	        pst.setString(19, commonMethods.getEmptyIfNull(transactionVO.getDiscountCurrency()).trim());
								    	        pst.setString(20, commonMethods.getEmptyIfNull(transactionVO.getMiscellaneousCharges()).trim());
								    	        pst.setString(21, commonMethods.getEmptyIfNull(transactionVO.getMiscellaneousCurrency()).trim());
								    	        pst.setString(22, commonMethods.getEmptyIfNull(transactionVO.getSellerName()).trim());
								    	        pst.setString(23, commonMethods.getEmptyIfNull(transactionVO.getSellerAddress()).trim());
								    	        pst.setString(24, commonMethods.getEmptyIfNull(transactionVO.getSellerCountry()).trim());
								    	        pst.setString(25, commonMethods.getEmptyIfNull(transactionVO.getThirdPartyName()).trim());
								    	        pst.setString(26, commonMethods.getEmptyIfNull(transactionVO.getThirdPartyAddress()).trim());
								    	        pst.setString(27, commonMethods.getEmptyIfNull(transactionVO.getThirdPartyCountry()).trim());
								    	        pst.setString(28, "P");
								    	      }
								    	      else if ((!commonMethods.isNull(boeVO.getButtonType())) && 
								    	        (boeVO.getButtonType().equalsIgnoreCase("Delete")))
								    	      {
								    	        logger.info(">>>>>>>>>>>>>>>>...Inside the Delete Black>>>>>>>>>>>>>");
								    	        query = "DELETE FROM ETT_INVOICE_DETAILS WHERE INVOICE_SERIAL_NUMBER=? AND INVOICE_NUMBER=? AND  BOE_NUMBER=? AND BOE_DATE= TO_DATE(?,'DD/MM/YYYY') AND BOE_PORT_OF_DISCHARGE = ? ";
								    	        pst = new LoggableStatement(con, query);
								    	        pst.setString(1, commonMethods.getEmptyIfNull(transactionVO.getInvSno()).trim());
								    	        pst.setString(2, commonMethods.getEmptyIfNull(transactionVO.getInvoiceNumber()).trim());
								    	        pst.setString(3, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
								    	        pst.setString(4, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
								    	        pst.setString(5, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
								    	      }
								    	      logger.info("Invoice Query ---> " + pst.getQueryString());
								    	      int result = pst.executeUpdate();
								    	      logger.info("Query Status --->" + result);
								    	      if (result > 0) {
								    	        if ((!commonMethods.isNull(boeVO.getButtonType())) && (boeVO.getButtonType().equalsIgnoreCase("Insert"))) {
								    	          CheckBoeCount(boeVO);
								    	        }
								    	      }
								    	    }
								    	    catch (Exception e)
								    	    {
								    	      logger.info("Exception in insertInvoiceDatatoTable DAO" + e.getMessage());
								    	    }
								    	    logger.info("Exiting Method");
								    	    return transactionVO;
								    	  }
								    	  public TransactionVO updateInvoiceDatatoTable(BoeVO boeVO, TransactionVO transactionVO)
								    	    throws DAOException
								    	  {
								    	    logger.info("Entering Method");
								    	    Connection con = null;
								    	    LoggableStatement pst = null;
								    	    CommonMethods commonMethods = null;
								    	    try
								    	    {
								    	      logger.info("Invoice------------------Method=--------------------");
								    	      con = DBConnectionUtility.getConnection();
								    	      commonMethods = new CommonMethods();
								    	      String query = "";
								    	      query = "UPDATE ETT_INVOICE_DETAILS SET INVOICE_SERIAL_NUMBER=?,INVOICE_NUMBER=?,INVOICE_FOBAMOUNT=?,INVOICE_FOBCURRENCY=?, INVOICE_TERMS_OF_INVOICE=?,INVOICE_SUPPLIER_NAME=?,INVOICE_SUPPLIER_ADDRESS=?,INVOICE_SUPPLIER_COUNTRY=?, INVOICE_FRIEGHTAMOUNT=?,INVOICE_FRIEGHTCURRENCYCODE=?,INVOICE_INSURANCEAMOUNT=?,INVOICE_INSURANCECURRENCY_CODE=?, INVOICE_AGENCY_COMMISSION=?,INVOICE_AGENCY_CURRENCY=?,INVOICE_DISCOUNT_CHARGES=?,INVOICE_DISCOUNT_CURRENCY=?, INVOICE_MISCELLANEOUS_CHARGES=?,INVOICE_MISCELLANEOUS_CURRENCY=?,INVOICE_SELLER_NAME=?,INVOICE_SELLER_ADDRESS=?, INVOICE_SELLER_COUNTRY=?,INVOICE_THIRDPARTY_NAME=?,INVOICE_THIRDPARTY_ADDRESS=?, INVOICE_THIRDPARTY_COUNTRY=?,STATUS='P' WHERE BOE_NUMBER = ? AND BOE_DATE= TO_DATE(?,'DD/MM/YYYY') AND BOE_PORT_OF_DISCHARGE = ? AND INVOICE_SERIAL_NUMBER = ? AND INVOICE_NUMBER = ? ";

								    	 
								    	 
								    	 
								    	 
								    	      pst = new LoggableStatement(con, query);
								    	      pst.setString(1, commonMethods.getEmptyIfNull(transactionVO.getInvSno()).trim());
								    	      pst.setString(2, commonMethods.getEmptyIfNull(transactionVO.getInvoiceNumber()).trim());
								    	      pst.setString(3, commonMethods.getEmptyIfNull(transactionVO.getInvAmt()).trim());
								    	      pst.setString(4, commonMethods.getEmptyIfNull(transactionVO.getInvoiceCurr()).trim());
								    	      pst.setString(5, commonMethods.getEmptyIfNull(transactionVO.getTermsofInvoice()).trim());
								    	      pst.setString(6, commonMethods.getEmptyIfNull(transactionVO.getSupplierName()).trim());
								    	      pst.setString(7, commonMethods.getEmptyIfNull(transactionVO.getSupplierAddress()).trim());
								    	      pst.setString(8, commonMethods.getEmptyIfNull(transactionVO.getSupplierCountry()).trim());
								    	      pst.setString(9, commonMethods.getEmptyIfNull(transactionVO.getFreightAmount()).trim());
								    	      pst.setString(10, commonMethods.getEmptyIfNull(transactionVO.getFreightCurrencyCode()).trim());
								    	      pst.setString(11, commonMethods.getEmptyIfNull(transactionVO.getInsuranceAmount()).trim());
								    	      pst.setString(12, commonMethods.getEmptyIfNull(transactionVO.getInsuranceCurrencyCode()).trim());
								    	      pst.setString(13, commonMethods.getEmptyIfNull(transactionVO.getAgencyCommission()).trim());
								    	      pst.setString(14, commonMethods.getEmptyIfNull(transactionVO.getAgencyCurrency()).trim());
								    	      pst.setString(15, commonMethods.getEmptyIfNull(transactionVO.getDiscountCharges()).trim());
								    	      pst.setString(16, commonMethods.getEmptyIfNull(transactionVO.getDiscountCurrency()).trim());
								    	      pst.setString(17, commonMethods.getEmptyIfNull(transactionVO.getMiscellaneousCharges()).trim());
								    	      pst.setString(18, commonMethods.getEmptyIfNull(transactionVO.getMiscellaneousCurrency()).trim());
								    	      pst.setString(19, commonMethods.getEmptyIfNull(transactionVO.getSellerName()).trim());
								    	      pst.setString(20, commonMethods.getEmptyIfNull(transactionVO.getSellerAddress()).trim());
								    	      pst.setString(21, commonMethods.getEmptyIfNull(transactionVO.getSellerCountry()).trim());
								    	      pst.setString(22, commonMethods.getEmptyIfNull(transactionVO.getThirdPartyName()).trim());
								    	      pst.setString(23, commonMethods.getEmptyIfNull(transactionVO.getThirdPartyAddress()).trim());
								    	      pst.setString(24, commonMethods.getEmptyIfNull(transactionVO.getThirdPartyCountry()).trim());
								    	      pst.setString(25, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
								    	      pst.setString(26, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
								    	      pst.setString(27, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
								    	      pst.setString(28, commonMethods.getEmptyIfNull(transactionVO.getInvSno()).trim());
								    	      pst.setString(29, commonMethods.getEmptyIfNull(transactionVO.getInvoiceNumber()).trim());
								    	      logger.info("Invoice Query ---> " + pst.getQueryString());
								    	      int result = pst.executeUpdate();
								    	      logger.info("Query Status --->" + result);
								    	      CheckBoeCount(boeVO);
								    	    }
								    	    catch (Exception e)
								    	    {
								    	      logger.info("Exception in insertInvoiceDatatoTable DAO" + e.getMessage());
								    	    }
								    	    finally
								    	    {
								    	      closeSqlRefferance(pst, con);
								    	    }
								    	    logger.info("Exiting Method");
								    	    return transactionVO;
								    	  }
								    	  public TransactionVO getInvoiceDetails(BoeVO boeVO)
								    			    throws DAOException
								    			  {
								    			    logger.info("------------getInvoiceDetails-------------------");
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
								    			      if (boeVO.getInvValue() != null)
								    			      {
								    			        String[] tempVal = boeVO.getInvValue().split(":");
								    			        invSerialNo = tempVal[0];
								    			        invNo = tempVal[1];
								    			      }
								    			      pst = new LoggableStatement(con, " SELECT INVOICE_SERIAL_NUMBER AS INV_SNO,INVOICE_NUMBER AS INV_NO, INVOICE_FOBAMOUNT AS FOB_AMT,INVOICE_FOBCURRENCY AS FOB_CURR, DECODE(TRIM(FR_EX_AMT),NULL,NVL(INVOICE_FRIEGHTAMOUNT,'0'),FR_EX_AMT) AS FR_AMT, DECODE(TRIM(FR_EX_CURR),NULL,NVL(TRIM(INVOICE_FRIEGHTCURRENCYCODE),INVOICE_FOBCURRENCY),FR_EX_CURR) AS FR_CURR, DECODE(TRIM(INS_EX_AMT),NULL,NVL(INVOICE_INSURANCEAMOUNT,'0'),INS_EX_AMT) AS INS_AMT, DECODE(TRIM(INS_EX_CURR),NULL,NVL(TRIM(INVOICE_INSURANCECURRENCY_CODE),INVOICE_FOBCURRENCY),INS_EX_CURR) AS INS_CURR, DECODE(TRIM(FR_EX_RATE),NULL,(SELECT ETT_BOE_EX_RATE_CAL(NVL(INVOICE_FRIEGHTCURRENCYCODE,INVOICE_FOBCURRENCY),INVOICE_FOBCURRENCY) FROM DUAL),FR_EX_RATE) AS FR_EX_RATE, DECODE(TRIM(INS_EX_RATE),NULL,(SELECT ETT_BOE_EX_RATE_CAL(NVL(INVOICE_INSURANCECURRENCY_CODE,INVOICE_FOBCURRENCY),INVOICE_FOBCURRENCY) FROM DUAL),INS_EX_RATE) AS INS_EX_RATE  FROM ETT_INVOICE_DETAILS WHERE BOE_NUMBER = ? AND TO_CHAR(BOE_DATE,'DD/MM/YYYY') = ? AND BOE_PORT_OF_DISCHARGE = ? AND INVOICE_SERIAL_NUMBER = ? AND TRIM(INVOICE_NUMBER) = ? ");
								    			      pst.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
								    			      pst.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
								    			      pst.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
								    			      pst.setString(4, commonMethods.getEmptyIfNull(invSerialNo).trim());
								    			      pst.setString(5, commonMethods.getEmptyIfNull(invNo).trim());
								    			      logger.info("------------getInvoiceDetails------------exception-------" + pst.getQueryString());
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
								    			      logger.info("------------getInvoiceDetails--------exception-----------" + e);
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
								    			  public TransactionVO updateCrossCurrencyData(BoeVO boeVO, TransactionVO transactionVO)
								    			    throws DAOException
								    			  {
								    			    logger.info("-----------updateCrossCurrencyData--------------");
								    			    Connection con = null;
								    			    CommonMethods commonMethods = null;
								    			    String sInvoiceSerNumber = "";
								    			    String sInvoiceNumber = "";
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
								    			      pst2.setString(8, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
								    			      pst2.setString(9, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
								    			      pst2.setString(10, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
								    			      pst2.setString(11, commonMethods.getEmptyIfNull(transactionVO.getInvSno()).trim());
								    			      pst2.setString(12, commonMethods.getEmptyIfNull(transactionVO.getInvNo()).trim());
								    			      sInvoiceSerNumber = transactionVO.getInvSno();
								    			      sInvoiceNumber = transactionVO.getInvNo();
								    			      boeVO.setInvoiceSerialNumber(sInvoiceSerNumber);
								    			      boeVO.setInvoiceNumber(sInvoiceNumber);
								    			      logger.info("Update UPDATE_CROSS_CURRENCY_VALUES-------------- " + pst2.getQueryString());
								    			      int icount = pst2.executeUpdate();
								    			      logger.info("Invoice Count ------------->" + icount);
								    			      closePreparedStatement(pst2);
								    			    }
								    			    catch (Exception e)
								    			    {
								    			      logger.info("-----------updateCrossCurrencyData--------exception------" + e);
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
								    			  public BoeVO revertBOEDetails(BoeVO boeVO)
								    			    throws DAOException
								    			  {
								    			    logger.info("--------------revertBOEDetails------------");
								    			    Connection con = null;
								    			    LoggableStatement pst = null;
								    			    ResultSet rs = null;
								    			    ArrayList<TransactionVO> invoiceList = null;
								    			    CommonMethods commonMethods = null;
								    			    try
								    			    {
								    			      con = DBConnectionUtility.getConnection();
								    			      commonMethods = new CommonMethods();
								    			      String sqlQuery = "SELECT COUNT(*) AS BOE_COUNT  FROM ETT_BOE_PAYMENT BP WHERE BP.BOE_PAYMENT_BP_BOE_NO = ? AND TO_CHAR(BP.BOE_PAYMENT_BP_BOE_DT,'DD/MM/YYYY') = ? AND BP.PORT_CODE = ? AND BP.STATUS != 'R' ";

								    			 
								    			 
								    			      pst = new LoggableStatement(con, sqlQuery);
								    			      pst.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
								    			      pst.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
								    			      pst.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
								    			      logger.info("--------------revertBOEDetails-------get query-----" + pst.getQueryString());
								    			      rs = pst.executeQuery();
								    			      int boeCount = 0;
								    			      if (rs.next())
								    			      {
								    			        boeCount = rs.getInt("BOE_COUNT");
								    			        logger.info("--------------revertBOEDetails-----be count----" + boeCount);
								    			      }
								    			      if (boeCount == 0) {
								    			        boeVO = getTotalBOEAmt(con, boeVO);
								    			      }
								    			      invoiceList = fetchInvoiceList(con, boeVO);
								    			      if (invoiceList.size() > 0)
								    			      {
								    			        boeVO.setInvoiceList(invoiceList);
								    			        boeVO.setBillCurrency(((TransactionVO)invoiceList.get(0)).getInvoiceCurr());
								    			      }
								    			    }
								    			    catch (Exception e)
								    			    {
								    			      logger.info("--------------revertBOEDetails-------exception-----" + e);
								    			      e.printStackTrace();
								    			    }
								    			    finally
								    			    {
								    			      closeSqlRefferance(rs, pst, con);
								    			    }
								    			    logger.info("Exiting Method");
								    			    return boeVO;
								    			  }
								    			  public int checkAdcode(String adCode)
								    			  {
								    			    logger.info("Entering Method");
								    			    Connection con = null;
								    			    LoggableStatement pst = null;
								    			    ResultSet rs = null;
								    			    int count = 0;
								    			    try
								    			    {
								    			      con = DBConnectionUtility.getConnection();
								    			      String query = "SELECT COUNT(*) AS AD_COUNT FROM EXTBRAMAS WHERE ADCODE =?";
								    			      pst = new LoggableStatement(con, query);
								    			      pst.setString(1, adCode);
								    			      rs = pst.executeQuery();
								    			      if (rs.next()) {
								    			        count = rs.getInt("AD_COUNT");
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
								    			    return count;
								    			  }
								    			  public int validatePortCode(String portCode)
								    			  {
								    			    logger.info("Entering Method");
								    			    Connection con = null;
								    			    LoggableStatement pst = null;
								    			    ResultSet rs = null;
								    			    CommonMethods commonMethods = null;
								    			    int count = 0;
								    			    try
								    			    {
								    			      con = DBConnectionUtility.getConnection();
								    			      commonMethods = new CommonMethods();
								    			      String query = "SELECT COUNT(*) AS PORT_COUNT FROM EXTPORTCO WHERE TRIM(PCODE) = ?   AND COUNTRY = 'IN'";
								    			      pst = new LoggableStatement(con, query);
								    			      pst.setString(1, commonMethods.getEmptyIfNull(portCode).trim());
								    			      rs = pst.executeQuery();
								    			      if (rs.next())
								    			      {
								    			        count = rs.getInt("PORT_COUNT");
								    			        logger.info("port code validation count----" + count);
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
								    			    return count;
								    			  }
								    			  public int validatePos(String portCode)
								    			  {
								    			    logger.info("Entering Method");
								    			    Connection con = null;
								    			    LoggableStatement pst = null;
								    			    ResultSet rs = null;
								    			    CommonMethods commonMethods = null;
								    			    int count = 0;
								    			    try
								    			    {
								    			      con = DBConnectionUtility.getConnection();
								    			      commonMethods = new CommonMethods();
								    			      String query = "SELECT COUNT(*) AS POS_COUNT FROM EXTPORTCO WHERE TRIM(PCODE) = ?  AND COUNTRY <> 'IN'";
								    			      pst = new LoggableStatement(con, query);
								    			      pst.setString(1, commonMethods.getEmptyIfNull(portCode).trim());
								    			      rs = pst.executeQuery();
								    			      if (rs.next()) {
								    			        count = rs.getInt("POS_COUNT");
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
								    			    return count;
								    			  }
								    			  public int checkIECodeData(BoeVO boeVO)
								    			    throws DAOException
								    			  {
								    			    logger.info("Entering Method");
								    			    Connection con = null;
								    			    LoggableStatement pst = null;
								    			    ResultSet rs = null;
								    			    CommonMethods commonMethods = null;
								    			    int ieCodeCount = 0;
								    			    try
								    			    {
								    			      con = DBConnectionUtility.getConnection();
								    			      commonMethods = new CommonMethods();
								    			      String sqlQuery = "SELECT COUNT(*) AS IE_COUNT FROM EXTCUST WHERE TRIM(IECODE) = ? ";
								    			      pst = new LoggableStatement(con, sqlQuery);
								    			      pst.setString(1, commonMethods.getEmptyIfNull(boeVO.getIeCode()).trim());

								    			 
								    			      rs = pst.executeQuery();
								    			      if (rs.next()) {
								    			        ieCodeCount = rs.getInt("IE_COUNT");
								    			      }
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
								    			    return ieCodeCount;
								    			  }
								    			  public double getOrmClosedAmt(String ormNo)
								    			    throws DAOException
								    			  {
								    			    logger.info("Entering Method");
								    			    Connection con = null;
								    			    LoggableStatement pst = null;
								    			    ResultSet rs = null;
								    			    double closedAmt = 0.0D;
								    			    try
								    			    {
								    			      con = DBConnectionUtility.getConnection();
								    			      pst = new LoggableStatement(con, "SELECT NVL(SUM(TCLOSAMT),'0') FROM ETT_IDPMS_DATA_EC WHERE TRIM(TOUTWARDREFERENCENUMBER) = ? ");
								    			      pst.setString(1, ormNo);
								    			      rs = pst.executeQuery();
								    			      if (rs.next()) {
								    			        closedAmt = rs.getDouble(1);
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
								    			    return closedAmt;
								    			  }
								    			  public BoeVO fetchManualBOEDetails(BoeVO boeVO)
								    			    throws DAOException
								    			  {
								    			    logger.info("Welcome to Inside of fetchManualBOEDetails()");
								    			    logger.info("Entering Method");
								    			    Connection con = null;
								    			    LoggableStatement pst = null;
								    			    ResultSet rs = null;
								    			    ArrayList<TransactionVO> invoiceList = null;
								    			    CommonMethods commonMethods = null;
								    			    try
								    			    {
								    			      con = DBConnectionUtility.getConnection();
								    			      commonMethods = new CommonMethods();

								    			 
								    			 
								    			 
								    			 
								    			 
								    			 
								    			 
								    			 
								    			 
								    			 
								    			 
								    			      String query = "SELECT BOE_IE_CODE AS IE_CODE,BOE_IE_NAME AS IE_NAME,BOE_IE_ADDRESS AS BOE_IE_ADDRESS,  BOE_IE_PANNUMBER AS BOE_IE_PANNUMBER,BOE_AD_CODE AS ADCODE,BOE_PORT_OF_SHIPMENT AS PORTOFSHIPMENT, BOE_RECORD_INDICATOR AS RECORDINDICATOR,BOE_GP AS BOE_GP,BOE_IMPORT_AGENCY AS IMPORTAGENCY, BOE_MAWB_MBLNUMBER AS MBLNO,TO_CHAR(BOE_MAWB_MBLDATE,'DD/MM/YYYY') AS MBLDATE, BOE_HAWB_HBLNUMBER AS HBLNO,TO_CHAR(BOE_HAWB_HBLDATE,'DD/MM/YYYY') AS HBLDATE,\tBOE_IGMNUMBER AS IGMNO,TO_CHAR(BOE_IGMDATE,'DD/MM/YYYY') AS IGMDATE,BOE_TYPE,REMARKS,STATUS  FROM ETT_BOE_DETAILS WHERE BOE_NUMBER =?  AND BOE_DATE =TO_DATE(?,'DD/MM/YYYY') AND BOE_PORT_OF_DISCHARGE = ?";

								    			 
								    			 
								    			 
								    			 
								    			      pst = new LoggableStatement(con, query);
								    			      pst.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
								    			      pst.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
								    			      pst.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
								    			      rs = pst.executeQuery();
								    			      if (rs.next())
								    			      {
								    			        boeVO.setIeCode(commonMethods.getEmptyIfNull(rs.getString("IE_CODE")).trim());
								    			        boeVO.setCustName(commonMethods.getEmptyIfNull(rs.getString("IE_NAME")).trim());
								    			        boeVO.setIeadd(commonMethods.getEmptyIfNull(rs.getString("BOE_IE_ADDRESS")).trim());
								    			        boeVO.setIepan(commonMethods.getEmptyIfNull(rs.getString("BOE_IE_PANNUMBER")).trim());
								    			        boeVO.setGovprv(commonMethods.getEmptyIfNull(rs.getString("BOE_GP")).trim());
								    			        boeVO.setIgmNo(commonMethods.getEmptyIfNull(rs.getString("IGMNO")).trim());
								    			        boeVO.setIgmDate(commonMethods.getEmptyIfNull(rs.getString("IGMDATE")).trim());
								    			        boeVO.setHblNo(commonMethods.getEmptyIfNull(rs.getString("HBLNO")).trim());
								    			        boeVO.setHblDate(commonMethods.getEmptyIfNull(rs.getString("HBLDATE")).trim());
								    			        boeVO.setMblNo(commonMethods.getEmptyIfNull(rs.getString("MBLNO")).trim());
								    			        boeVO.setMblDate(commonMethods.getEmptyIfNull(rs.getString("MBLDATE")).trim());
								    			        boeVO.setImAgency(commonMethods.getEmptyIfNull(rs.getString("IMPORTAGENCY")).trim());
								    			        boeVO.setAdCode(commonMethods.getEmptyIfNull(rs.getString("ADCODE")).trim());
								    			        boeVO.setPos(commonMethods.getEmptyIfNull(rs.getString("PORTOFSHIPMENT")).trim());
								    			        boeVO.setRecInd(commonMethods.getEmptyIfNull(rs.getString("RECORDINDICATOR")).trim());
								    			        boeVO.setBoeType(commonMethods.getEmptyIfNull(rs.getString("BOE_TYPE")).trim());
								    			        boeVO.setRemarks(commonMethods.getEmptyIfNull(rs.getString("REMARKS")).trim());
								    			        boeVO.setBoestatus(commonMethods.getEmptyIfNull(rs.getString("STATUS")).trim());
								    			        logger.info("status" + boeVO.getBoestatus());
								    			      }
								    			      invoiceList = fetchManualInvoiceList(con, boeVO);
								    			      if (invoiceList.size() > 0)
								    			      {
								    			        boeVO.setInvoiceList(invoiceList);
								    			        CheckBoeCount(boeVO);
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
								    			  public BoeVO revertManualBOEDetails(BoeVO boeVO)
								    			    throws DAOException
								    			  {
								    			    logger.info("Entering Method");
								    			    Connection con = null;
								    			    LoggableStatement pst = null;
								    			    ResultSet rs = null;
								    			    ArrayList<TransactionVO> invoiceList = null;
								    			    try
								    			    {
								    			      con = DBConnectionUtility.getConnection();
								    			      invoiceList = fetchManualInvoiceList(con, boeVO);
								    			      if (invoiceList.size() > 0)
								    			      {
								    			        boeVO.setInvoiceList(invoiceList);
								    			        CheckBoeCount(boeVO);
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
								    			  public ArrayList<TransactionVO> fetchManualInvoiceList(Connection con, BoeVO boeVO)
								    			  {
								    			    logger.info("Welcome to Inside of fetchManualInvoiceList()");
								    			    logger.info("Entering Method");
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
								    			      String query = "SELECT INV.INVOICE_SERIAL_NUMBER AS INV_SERIAL_NO,INV.INVOICE_NUMBER AS INV_NO, INV.INVOICE_TERMS_OF_INVOICE AS INV_TERMS,INV.INVOICE_FOBAMOUNT AS FOB_AMT,INV.STATUS, INV.INVOICE_FOBCURRENCY AS FOB_CURR,INV.INVOICE_FRIEGHTAMOUNT AS FRI_AMT, INV.INVOICE_FRIEGHTCURRENCYCODE AS FRI_CURR,INV.INVOICE_INSURANCEAMOUNT AS INS_AMT, INV.INVOICE_INSURANCECURRENCY_CODE AS INS_CURR, NVL(INV.INVOICE_AGENCY_COMMISSION,0) AS AGCY_AMT,INV.INVOICE_AGENCY_CURRENCY AS AGCY_CURR,\tNVL(INV.INVOICE_DISCOUNT_CHARGES,0) AS DIS_AMT,INVOICE_DISCOUNT_CURRENCY AS DIS_CURR,\tNVL(INV.INVOICE_MISCELLANEOUS_CHARGES,0) AS MISC_AMT,INV.INVOICE_MISCELLANEOUS_CURRENCY AS MISC_CURR FROM ETT_INVOICE_DETAILS INV  WHERE INV.BOE_NUMBER = ? AND INV.BOE_DATE = TO_DATE(?,'DD/MM/YYYY')  AND INV.BOE_PORT_OF_DISCHARGE = ? ORDER BY TO_NUMBER(INV.INVOICE_SERIAL_NUMBER) ";

								    			 
								    			 
								    			 
								    			 
								    			 
								    			 
								    			      pst = new LoggableStatement(con, query);
								    			      pst.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
								    			      pst.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
								    			      pst.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
								    			      logger.info("Invoice Query --> " + pst.getQueryString());
								    			      logger.info("The Query Result is : =============>>> " + query);
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
								    			        invoiceVO.setStatus(commonMethods.getEmptyIfNull(rs.getString("STATUS")));

								    			 
								    			        invoiceVO.setAgencyCommission(commonMethods.getEmptyIfNull(rs.getString("AGCY_AMT")).trim());
								    			        invoiceVO.setAgencyCurrency(commonMethods.getEmptyIfNull(rs.getString("AGCY_CURR")).trim());
								    			        invoiceVO.setDiscountCharges(commonMethods.getEmptyIfNull(rs.getString("DIS_AMT")).trim());
								    			        invoiceVO.setDiscountCurrency(commonMethods.getEmptyIfNull(rs.getString("DIS_CURR")).trim());
								    			        invoiceVO.setMiscellaneousCharges(commonMethods.getEmptyIfNull(rs.getString("MISC_AMT")).trim());
								    			        invoiceVO.setMiscellaneousCurrency(commonMethods.getEmptyIfNull(rs.getString("MISC_CURR")).trim());

								    			 
								    			        logger.info("Invoice Status-----------" + invoiceVO.getStatus());
								    			        double fobAmt = commonMethods.convertToDouble(rs.getString("FOB_AMT"));
								    			        double friAmt = commonMethods.convertToDouble(rs.getString("FRI_AMT"));
								    			        double insAmt = commonMethods.convertToDouble(rs.getString("INS_AMT"));

								    			 
								    			 
								    			        double agyAmt = commonMethods.convertToDouble(rs.getString("AGCY_AMT"));
								    			        double disAmt = commonMethods.convertToDouble(rs.getString("DIS_AMT"));
								    			        double misAmt = commonMethods.convertToDouble(rs.getString("MISC_AMT"));

								    			 
								    			 
								    			 
								    			        logger.info(">>>>>>>>>>>>> FOB AMT " + fobAmt);

								    			 
								    			 
								    			 
								    			 
								    			        double totalAmt = fobAmt;
								    			        double totalInvAmt = Math.round(totalAmt * 100.0D) / 100.0D;
								    			        BigDecimal toInvAmt = BigDecimal.valueOf(totalInvAmt).setScale(2, RoundingMode.HALF_UP);
								    			        invoiceVO.setTotalInvAmt(String.valueOf(toInvAmt));
								    			        invoiceList.add(invoiceVO);
								    			        logger.info(
								    			          "\n\n----------------------------------------------------------------------------------------");
								    			        logger.info(">>>>>>>>>>>>>>>>>>>>>>Invoice Amount >>>>>>>>> " + invoiceVO.getInvoiceAmount());
								    			        logger.info(">>>>>>>>>>>>>>>>>>>>>>Invoice Currency >>>>>>>>> " + invoiceVO.getInvoiceCurr());
								    			        logger.info(">>>>>>>>>>>>>>>>>>>>>>Freight Amount >>>>>>>>> " + invoiceVO.getFreightAmount());
								    			        logger.info(
								    			          ">>>>>>>>>>>>>>>>>>>>>>Freight Currency >>>>>>>>> " + invoiceVO.getFreightCurrencyCode());
								    			        logger.info(">>>>>>>>>>>>>>>>>>>>>>Insurence Amount >>>>>>>>> " + invoiceVO.getInsuranceAmount());
								    			        logger.info(
								    			          ">>>>>>>>>>>>>>>>>>>>>>Insurence Currency >>>>>>>>> " + invoiceVO.getInsuranceCurrencyCode());
								    			        logger.info(">>>>>>>>>>>>>>>>>>>>>>Agency Amount >>>>>>>>> " + invoiceVO.getAgencyCommission());
								    			        logger.info(">>>>>>>>>>>>>>>>>>>>>>Agency Currency >>>>>>>>> " + invoiceVO.getAgencyCurrency());
								    			        logger.info(">>>>>>>>>>>>>>>>>>>>>>Discount Amount >>>>>>>>> " + invoiceVO.getDiscountCharges());
								    			        logger.info(
								    			          ">>>>>>>>>>>>>>>>>>>>>>Discount Currency >>>>>>>>> " + invoiceVO.getDiscountCurrency());
								    			        logger.info(
								    			          ">>>>>>>>>>>>>>>>>>>>>>miscellaneous  Amount >>>>>>>>> " + invoiceVO.getMiscellaneousCharges());
								    			        logger.info(">>>>>>>>>>>>>>>>>>>>>>miscellaneous  Currency >>>>>>>>> " + 
								    			          invoiceVO.getMiscellaneousCurrency());
								    			        logger.info(
								    			          "\n----------------------------------------------------------------------------------------");
								    			      }
								    			    }
								    			    catch (Exception e)
								    			    {
								    			      logger.info("Exception in is " + e.getMessage());
								    			    }
								    			    finally
								    			    {
								    			      closeSqlRefferance(rs, pst);
								    			    }
								    			    logger.info("Exiting Method");
								    			    return invoiceList;
								    			  }
								    			  public int deleteManualBOEInvoice(BoeVO boeVO)
								    			    throws DAOException
								    			  {
								    			    logger.info("Entering Method");
								    			    Connection con = null;
								    			    LoggableStatement pst = null;
								    			    CommonMethods commonMethods = null;
								    			    int boeCount = 0;
								    			    try
								    			    {
								    			      logger.info("deleteManualBOEDataInvoice-------------2222222222");
								    			      con = DBConnectionUtility.getConnection();
								    			      commonMethods = new CommonMethods();
								    			      HttpSession session = ServletActionContext.getRequest().getSession();
								    			      String loginedUserId = (String)session.getAttribute("loginedUserId");
								    			      String cifNo = getCifCode(con, boeVO.getIeCode());
								    			      int count = 0;
								    			      String countQuery = "SELECT COUNT(*) AS BOE_COUNT FROM ETT_BOE_DETAILS WHERE BOE_NUMBER = ? AND TO_CHAR(BOE_DATE,'DD/MM/YYYY') = ? AND BOE_PORT_OF_DISCHARGE = ? ";

								    			 
								    			      LoggableStatement ps = new LoggableStatement(con, countQuery);
								    			      ps.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
								    			      ps.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
								    			      ps.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
								    			      ResultSet rs = ps.executeQuery();
								    			      logger.info("BOE COUNT QUERY--------------" + ps.getQueryString());
								    			      if (rs.next()) {
								    			        count = rs.getInt("BOE_COUNT");
								    			      }
								    			      closeSqlRefferance(rs, ps);
								    			      logger.info("Count------------------" + count);
								    			      if (count > 0)
								    			      {
								    			        String mannual_boe_delete_invoice = "DELETE FROM ETT_BOE_DETAILS WHERE BOE_NUMBER=? AND BOE_DATE=? AND BOE_PORT_OF_DISCHARGE=?";
								    			        pst = new LoggableStatement(con, mannual_boe_delete_invoice);
								    			        pst.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
								    			        pst.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
								    			        pst.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
								    			        logger.info("MANNUAL BOE INVOICE  DELETE  Query ---> " + pst.getQueryString());
								    			        logger.info("MANNUAL BOE INVOICE  DELETE  Query ---> " + pst.getQueryString());
								    			        logger.info("MANNUAL BOE INVOICE Query Status --->" + boeCount);
								    			        boeCount = count;
								    			        logger.info("MANNUAL BOE INVOICE Query Status --->" + boeCount);
								    			      }
								    			    }
								    			    catch (Exception e)
								    			    {
								    			      logger.info("MANNUAL BOE INVOICE  DELETE EXCEPTION -" + e.getMessage());
								    			    }
								    			    finally
								    			    {
								    			      closeSqlRefferance(pst, con);
								    			    }
								    			    logger.info("Exiting Method");
								    			    return boeCount;
								    			  }
								    			  public int insertManualBOEData(BoeVO boeVO)
								    			    throws DAOException
								    			  {
								    			    logger.info("Entering Method");
								    			    Connection con = null;
								    			    LoggableStatement pst = null;
								    			    CommonMethods commonMethods = null;
								    			    int boeCount = 0;
								    			    try
								    			    {
								    			      con = DBConnectionUtility.getConnection();
								    			      commonMethods = new CommonMethods();
								    			      HttpSession session = ServletActionContext.getRequest().getSession();
								    			      String loginedUserId = (String)session.getAttribute("loginedUserId");
								    			      String cifNo = getCifCode(con, boeVO.getIeCode());
								    			      int count = 0;
								    			      String countQuery = "SELECT COUNT(*) AS BOE_COUNT FROM ETT_BOE_DETAILS WHERE BOE_NUMBER = ? AND TO_CHAR(BOE_DATE,'DD/MM/YYYY') = ? AND BOE_PORT_OF_DISCHARGE = ? ";

								    			 
								    			      LoggableStatement ps = new LoggableStatement(con, countQuery);
								    			      ps.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
								    			      ps.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
								    			      ps.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
								    			      ResultSet rs = ps.executeQuery();
								    			      if (rs.next()) {
								    			        count = rs.getInt("BOE_COUNT");
								    			      }
								    			      closeSqlRefferance(rs, ps);
								    			      if (count > 0)
								    			      {
								    			        String query = "UPDATE ETT_BOE_DETAILS SET BOE_IE_CODE = ?,BOE_IE_NAME = ?,BOE_IE_ADDRESS = ?,BOE_IE_PANNUMBER = ?,CIF = ?,BOE_MAWB_MBLNUMBER = ?,BOE_MAWB_MBLDATE = TO_DATE(?,'DD-MM-YY'),BOE_HAWB_HBLNUMBER = ?,BOE_HAWB_HBLDATE = TO_DATE(?,'DD-MM-YY'),BOE_IGMNUMBER = ?,BOE_IGMDATE = TO_DATE(?,'DD-MM-YY'),BOE_AD_CODE = ?,BOE_IMPORT_AGENCY = ?,BOE_GP = ?,BOE_PORT_OF_SHIPMENT = ?,BOE_RECORD_INDICATOR = ?,STATUS = ?,MAKER_USERID = ?,REMARKS = ?,MAKER_TIMESTAMP = SYSTIMESTAMP  WHERE BOE_NUMBER = ? AND TO_CHAR(BOE_DATE,'DD/MM/YYYY') = ? AND BOE_PORT_OF_DISCHARGE = ? ";

								    			 
								    			 
								    			 
								    			        pst = new LoggableStatement(con, query);
								    			        pst.setString(1, commonMethods.getEmptyIfNull(boeVO.getIeCode()).trim());
								    			        pst.setString(2, commonMethods.getEmptyIfNull(boeVO.getCustName()).trim());
								    			        pst.setString(3, commonMethods.getEmptyIfNull(boeVO.getIeadd()).trim());
								    			        pst.setString(4, commonMethods.getEmptyIfNull(boeVO.getIepan()).trim());
								    			        pst.setString(5, commonMethods.getEmptyIfNull(cifNo).trim());
								    			        pst.setString(6, commonMethods.getEmptyIfNull(boeVO.getMblNo()).trim());
								    			        pst.setString(7, commonMethods.getEmptyIfNull(boeVO.getMblDate()).trim());
								    			        pst.setString(8, commonMethods.getEmptyIfNull(boeVO.getHblNo()).trim());
								    			        pst.setString(9, commonMethods.getEmptyIfNull(boeVO.getHblDate()).trim());
								    			        pst.setString(10, commonMethods.getEmptyIfNull(boeVO.getIgmNo()).trim());
								    			        pst.setString(11, commonMethods.getEmptyIfNull(boeVO.getIgmDate()).trim());
								    			        pst.setString(12, commonMethods.getEmptyIfNull(boeVO.getAdCode()).trim());
								    			        pst.setString(13, commonMethods.getEmptyIfNull(boeVO.getImAgency()).trim());
								    			        pst.setString(14, commonMethods.getEmptyIfNull(boeVO.getGovprv()).trim());
								    			        pst.setString(15, commonMethods.getEmptyIfNull(boeVO.getPos()).trim());
								    			        pst.setString(16, commonMethods.getEmptyIfNull(boeVO.getRecInd()).trim());
								    			        pst.setString(17, "P");
								    			        pst.setString(18, loginedUserId);
								    			        pst.setString(19, commonMethods.getEmptyIfNull(boeVO.getRemarks()).trim());
								    			        pst.setString(20, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
								    			        pst.setString(21, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
								    			        pst.setString(22, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
								    			        logger.info("BOE Query ---> " + pst.getQueryString());
								    			        boeCount = pst.executeUpdate();
								    			        logger.info("BOE Query Status --->" + boeCount);
								    			      }
								    			      else
								    			      {
								    			        String query = "INSERT INTO ETT_BOE_DETAILS(BOE_NUMBER,BOE_DATE,BOE_PORT_OF_DISCHARGE,BOE_IE_CODE,BOE_IE_NAME,BOE_IE_ADDRESS,BOE_IE_PANNUMBER,CIF,BOE_MAWB_MBLNUMBER,BOE_MAWB_MBLDATE,BOE_HAWB_HBLNUMBER,BOE_HAWB_HBLDATE,BOE_IGMNUMBER,BOE_IGMDATE,BOE_AD_CODE,BOE_IMPORT_AGENCY,BOE_GP,BOE_PORT_OF_SHIPMENT,BOE_RECORD_INDICATOR,BOE_TYPE,STATUS,MAKER_USERID,REMARKS,MAKER_TIMESTAMP) VALUES (?,TO_DATE(?,'DD-MM-YY'),?,?,?,?,?,?,?,TO_DATE(?,'DD-MM-YY'),?,TO_DATE(?,'DD-MM-YY'),?,TO_DATE(?,'DD-MM-YY'),?,?,?,?,?,?,?,?,?,SYSTIMESTAMP)";

								    			 
								    			 
								    			 
								    			        pst = new LoggableStatement(con, query);
								    			        pst.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
								    			        pst.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
								    			        pst.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
								    			        pst.setString(4, commonMethods.getEmptyIfNull(boeVO.getIeCode()).trim());
								    			        pst.setString(5, commonMethods.getEmptyIfNull(boeVO.getCustName()).trim());
								    			        pst.setString(6, commonMethods.getEmptyIfNull(boeVO.getIeadd()).trim());
								    			        pst.setString(7, commonMethods.getEmptyIfNull(boeVO.getIepan()).trim());
								    			        pst.setString(8, commonMethods.getEmptyIfNull(cifNo).trim());
								    			        pst.setString(9, commonMethods.getEmptyIfNull(boeVO.getMblNo()).trim());
								    			        pst.setString(10, commonMethods.getEmptyIfNull(boeVO.getMblDate()).trim());
								    			        pst.setString(11, commonMethods.getEmptyIfNull(boeVO.getHblNo()).trim());
								    			        pst.setString(12, commonMethods.getEmptyIfNull(boeVO.getHblDate()).trim());
								    			        pst.setString(13, commonMethods.getEmptyIfNull(boeVO.getIgmNo()).trim());
								    			        pst.setString(14, commonMethods.getEmptyIfNull(boeVO.getIgmDate()).trim());
								    			        pst.setString(15, commonMethods.getEmptyIfNull(boeVO.getAdCode()).trim());
								    			        pst.setString(16, commonMethods.getEmptyIfNull(boeVO.getImAgency()).trim());
								    			        pst.setString(17, commonMethods.getEmptyIfNull(boeVO.getGovprv()).trim());
								    			        pst.setString(18, commonMethods.getEmptyIfNull(boeVO.getPos()).trim());
								    			        pst.setString(19, commonMethods.getEmptyIfNull(boeVO.getRecInd()).trim());
								    			        pst.setString(20, "M");
								    			        pst.setString(21, "P");
								    			        pst.setString(22, loginedUserId);
								    			        pst.setString(23, commonMethods.getEmptyIfNull(boeVO.getRemarks()).trim());
								    			        logger.info("BOE Query ---> " + pst.getQueryString());
								    			        boeCount = pst.executeUpdate();
								    			        logger.info("BOE Query Status --->" + boeCount);
								    			        checkBoeCount(boeVO);
								    			      }
								    			    }
								    			    catch (Exception e)
								    			    {
								    			      logger.info("Exception in insertInvoiceDatatoTable DAO" + e.getMessage());
								    			    }
								    			    finally
								    			    {
								    			      closeSqlRefferance(pst, con);
								    			    }
								    			    logger.info("Exiting Method");
								    			    return boeCount;
								    			  }
								    			  private String getCifCode(Connection con, String iECode)
								    			  {
								    			    logger.info("Entering Method");
								    			    LoggableStatement pst = null;
								    			    ResultSet rs = null;
								    			    String cifNo = "";
								    			    try
								    			    {
								    			      if (con == null) {
								    			        con = DBConnectionUtility.getConnection();
								    			      }
								    			      String getCifQuery = "SELECT TRIM(CUST) FROM EXTCUST WHERE IECODE=?";
								    			      pst = new LoggableStatement(con, getCifQuery);
								    			      pst.setString(1, iECode);
								    			      rs = pst.executeQuery();
								    			      if (rs.next()) {
								    			        cifNo = rs.getString(1);
								    			      }
								    			      closeSqlRefferance(rs, pst);
								    			    }
								    			    catch (Exception e)
								    			    {
								    			      e.printStackTrace();
								    			    }
								    			    logger.info("Exiting Method");
								    			    return cifNo;
								    			  }
								    			  public int checkBoeCount(BoeVO boeVO)
								    			    throws DAOException
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
								    			      String chkQuery = "SELECT COUNT(*) AS BOE_COUNT  FROM ETT_BOE_DETAILS WHERE BOE_NUMBER = ? AND TO_CHAR(BOE_DATE,'DD/MM/YYYY') =? AND BOE_PORT_OF_DISCHARGE = ? AND STATUS != 'R' ";

								    			 
								    			      pst = new LoggableStatement(con, chkQuery);
								    			      pst.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
								    			      pst.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
								    			      pst.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
								    			      rs = pst.executeQuery();
								    			      if (rs.next()) {
								    			        count = rs.getInt("BOE_COUNT");
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
								    			    return count;
								    			  }
								    			  public int checkInvCount(BoeVO boeVO, TransactionVO transactionVO)
								    			    throws DAOException
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
								    			      String chkQuery = "SELECT COUNT(*) AS INV_COUNT  FROM ETT_INVOICE_DETAILS WHERE BOE_NUMBER = ? AND TO_CHAR(BOE_DATE,'DD/MM/YYYY') =?  AND BOE_PORT_OF_DISCHARGE = ? AND INVOICE_SERIAL_NUMBER =? AND INVOICE_NUMBER =?  AND STATUS != 'R' ";

								    			 
								    			 
								    			      pst = new LoggableStatement(con, chkQuery);
								    			      pst.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
								    			      pst.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
								    			      pst.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
								    			      pst.setString(4, commonMethods.getEmptyIfNull(transactionVO.getInvSno()).trim());
								    			      pst.setString(5, commonMethods.getEmptyIfNull(transactionVO.getInvoiceNumber()).trim());
								    			      rs = pst.executeQuery();
								    			      if (rs.next()) {
								    			        count = rs.getInt("INV_COUNT");
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
								    			    return count;
								    			  }
								    			  public int boeInvCount(BoeVO boeVO, TransactionVO transactionVO)
								    			    throws DAOException
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
								    			      int totalCount = 0;
								    			      String chkQuery = "SELECT COUNT(*) AS INV_COUNT  FROM ETT_INVOICE_DETAILS WHERE BOE_NUMBER = ? AND TO_CHAR(BOE_DATE,'DD/MM/YYYY') =?  AND BOE_PORT_OF_DISCHARGE = ?";

								    			 
								    			      pst = new LoggableStatement(con, chkQuery);
								    			      pst.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
								    			      pst.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
								    			      pst.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
								    			      rs = pst.executeQuery();
								    			      if (rs.next()) {
								    			        totalCount = rs.getInt("INV_COUNT");
								    			      }
								    			      closeSqlRefferance(rs, pst);
								    			      if (totalCount >= 1)
								    			      {
								    			        String query = "SELECT INVOICE_FOBCURRENCY AS INV_CURR  FROM ETT_INVOICE_DETAILS WHERE BOE_NUMBER = ? AND TO_CHAR(BOE_DATE,'DD/MM/YYYY') =? AND BOE_PORT_OF_DISCHARGE = ?  GROUP BY INVOICE_FOBCURRENCY ";

								    			 
								    			 
								    			        LoggableStatement pst1 = new LoggableStatement(con, query);
								    			        pst1.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
								    			        pst1.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
								    			        pst1.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
								    			        ResultSet rs1 = pst1.executeQuery();
								    			        while (rs1.next())
								    			        {
								    			          String invCurr = commonMethods.getEmptyIfNull(rs1.getString("INV_CURR"));
								    			          if ((boeVO.getButtonType().equalsIgnoreCase("Update")) && (totalCount == 1)) {
								    			            count = 0;
								    			          } else if (!invCurr.equalsIgnoreCase(transactionVO.getInvoiceCurr())) {
								    			            count++;
								    			          }
								    			        }
								    			        closeSqlRefferance(rs1, pst1);
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
								    			    return count;
								    			  }
								    			  public int validateInvCurrency(String invCurrency)
								    					    throws DAOException
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
								    					      String chkQuery = "SELECT COUNT(*) AS INV_CURR_COUNT FROM C8PF WHERE UPPER(C8CCY) = ?";
								    					      pst = new LoggableStatement(con, chkQuery);
								    					      pst.setString(1, commonMethods.getEmptyIfNull(invCurrency).trim());
								    					      rs = pst.executeQuery();
								    					      if (rs.next()) {
								    					        count = rs.getInt("INV_CURR_COUNT");
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
								    					    return count;
								    					  }
								    					  public int checkBOEStatus(String boeData)
								    					    throws DAOException
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
								    					      if (boeData != null)
								    					      {
								    					        String[] tempData = commonMethods.getEmptyIfNull(boeData).trim().split(":");
								    					        String boeNo = tempData[0];
								    					        String boeDate = tempData[1];
								    					        String portCode = tempData[2];
								    					        String chkQuery = "SELECT COUNT(*) AS BOE_COUNT  FROM ETT_BOE_PAYMENT WHERE BOE_PAYMENT_BP_BOE_NO =? AND TO_CHAR(BOE_PAYMENT_BP_BOE_DT,'DD/MM/YYYY') =?  AND PORT_CODE = ?";

								    					 
								    					 
								    					        pst = new LoggableStatement(con, chkQuery);
								    					        pst.setString(1, commonMethods.getEmptyIfNull(boeNo).trim());
								    					        pst.setString(2, commonMethods.getEmptyIfNull(boeDate).trim());
								    					        pst.setString(3, commonMethods.getEmptyIfNull(portCode).trim());
								    					        rs = pst.executeQuery();
								    					        if (rs.next()) {
								    					          count = rs.getInt("BOE_COUNT");
								    					        }
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
								    					  public BoeVO manualPopCIFDetails(BoeVO boeVO)
								    					    throws DAOException
								    					  {
								    					    logger.info("Entering Method");
								    					    Connection con = null;
								    					    LoggableStatement pst = null;
								    					    ResultSet rs = null;
								    					    String sqlQuery = null;
								    					    CommonMethods commonMethods = null;
								    					    try
								    					    {
								    					      commonMethods = new CommonMethods();
								    					      con = DBConnectionUtility.getConnection();

								    					 
								    					      sqlQuery = "SELECT TRIM(GF.GFCUN) AS CUST_NAME,TRIM(SX.SVNA1) AS IE_ADDR1,TRIM(SX.SVNA2) AS IE_ADDR2,TRIM(SX.SVNA3) AS IE_ADDR3,TRIM(SX.SVNA4) AS IE_ADDR4,TRIM(SX.SVNA5) AS IE_ADDR5,TRIM(EXT.PANNO) AS PANNO FROM EXTCUST EXT,GFPF GF,SX20LF SX WHERE EXT.CUST = SX.SXCUS1 AND EXT.CUST = GF.GFCUS1 AND EXT.IECODE = ?";

								    					 
								    					 
								    					      pst = new LoggableStatement(con, sqlQuery);
								    					      pst.setString(1, commonMethods.getEmptyIfNull(boeVO.getIeCode()).trim());
								    					      rs = pst.executeQuery();
								    					      if (rs.next())
								    					      {
								    					        boeVO.setCustName(rs.getString("CUST_NAME"));
								    					        boeVO.setIepan(rs.getString("PANNO"));
								    					        String ieAddr1 = commonMethods.getEmptyIfNull(rs.getString("IE_ADDR1")).trim();
								    					        String ieAddr2 = commonMethods.getEmptyIfNull(rs.getString("IE_ADDR2")).trim();
								    					        String ieAddr3 = commonMethods.getEmptyIfNull(rs.getString("IE_ADDR3")).trim();
								    					        String ieAddr4 = commonMethods.getEmptyIfNull(rs.getString("IE_ADDR4")).trim();
								    					        String ieAddr5 = commonMethods.getEmptyIfNull(rs.getString("IE_ADDR5")).trim();
								    					        String ieAddr = ieAddr1 + " " + ieAddr2 + " " + ieAddr3 + " " + ieAddr4 + " " + ieAddr5;
								    					        boeVO.setIeadd(ieAddr);
								    					      }
								    					    }
								    					    catch (SQLException e)
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
								    					      pst.setString(1, boevo.getBoeNo());
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
								    					        pst1.setString(1, boevo.getBoeNo());
								    					        pst1.setString(2, boevo.getBoeDate());
								    					        pst1.setString(3, boevo.getPortCode());
								    					        ResultSet rs1 = pst1.executeQuery();
								    					        logger.info("MBE Count Query--------------->" + pst1.getQueryString());
								    					        if (rs1.next()) {
								    					          mbeCount = rs1.getInt("MBE_COUNT");
								    					        }
								    					        logger.info("mbeCount------------------>" + mbeCount);
								    					        closeSqlRefferance(rs1, pst1);
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
								    					  public ArrayList<BOEPortSelectionVO> fetchPortOfShipmentList(BoeVO boeVO)
								    					    throws DAOException
								    					  {
								    					    logger.info("Entering Method");
								    					    String sQuery = "";
								    					    Connection con = null;
								    					    LoggableStatement pst = null;
								    					    ResultSet rs = null;
								    					    BOEPortSelectionVO portSelectionVO = null;
								    					    ArrayList<BOEPortSelectionVO> portList = null;
								    					    CommonMethods commonMethods = null;
								    					    boolean portFlag = false;
								    					    boolean countryFlag = false;
								    					    int setValue = 0;
								    					    try
								    					    {
								    					      commonMethods = new CommonMethods();
								    					      portList = new ArrayList();
								    					      con = DBConnectionUtility.getConnection();
								    					      sQuery = "SELECT PCODE, PNAME, COUNTRY FROM EXTPORTCO WHERE TRIM(COUNTRY) <> 'IN'";
								    					      if (!commonMethods.isNull(boeVO.getPortId()))
								    					      {
								    					        sQuery = sQuery + "AND  PCODE LIKE '%'||?||'%' ";
								    					        portFlag = true;
								    					      }
								    					      if (!commonMethods.isNull(boeVO.getCountryName()))
								    					      {
								    					        sQuery = sQuery + "AND COUNTRY LIKE '%'||?||'%' ";
								    					        countryFlag = true;
								    					      }
								    					      pst = new LoggableStatement(con, sQuery);
								    					      if (portFlag) {
								    					        pst.setString(++setValue, boeVO.getPortId());
								    					      }
								    					      if (countryFlag) {
								    					        pst.setString(++setValue, boeVO.getCountryName());
								    					      }
								    					      rs = pst.executeQuery();
								    					      while (rs.next())
								    					      {
								    					        portSelectionVO = new BOEPortSelectionVO();
								    					        portSelectionVO.setPortId(rs.getString("PCODE"));
								    					        portSelectionVO.setPortLocation(rs.getString("PNAME"));
								    					        portSelectionVO.setCountryName(rs.getString("COUNTRY"));
								    					        portList.add(portSelectionVO);
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
								    					    return portList;
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
								    					    boolean portFlag = false;
								    					    int setValue = 0;
								    					    try
								    					    {
								    					      commonMethods = new CommonMethods();
								    					      portList1 = new ArrayList();
								    					      con = DBConnectionUtility.getConnection();
								    					      sQuery = "SELECT BOE.PD_TXN_REF AS PAYMENT_REF_NO, BOE.PD_PART_PAY_REF AS EVENT_REF_NO,BOE.PD_OUTSTANDING_AMT FROM ETTV_BOE_PAYMENT_DETAILS BOE WHERE BOE.PD_OUTSTANDING_AMT > 0  ";
								    					      if (!commonMethods.isNull(boeVO1.getPortId()))
								    					      {
								    					        sQuery = sQuery + "AND (BOE.PD_TXN_REF||BOE.PD_PART_PAY_REF)=?";
								    					        portFlag = true;
								    					      }
								    					      pst = new LoggableStatement(con, sQuery);
								    					      if (portFlag) {
								    					        pst.setString(++setValue, boeVO1.getPortId());
								    					      }
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
								    					  public ArrayList<BoeVO> fetchoutstandingORMList(BoeVO boeVO1)
								    							    throws DAOException
								    							  {
								    							    logger.info("Entering Method");
								    							    String sQuery = "";
								    							    Connection con = null;
								    							    LoggableStatement pst = null;
								    							    ResultSet rs = null;
								    							    ArrayList<BoeVO> ormList1 = null;
								    							    CommonMethods commonMethods = null;
								    							    boolean cifFlag = false;
								    							    boolean iecFlag = false;
								    							    boolean frmDateFlag = false;
								    							    boolean ormFlag = false;
								    							    int setValue = 0;
								    							    try
								    							    {
								    							      commonMethods = new CommonMethods();
								    							      ormList1 = new ArrayList();
								    							      con = DBConnectionUtility.getConnection();
								    							      sQuery = " SELECT SUBSTR(ORM_REFERENCE_NUMBER,0,16) AS PAYMENT_REF_NO,SUBSTR(ORM_REFERENCE_NUMBER,17,22) AS EVENT_REF_NO,ORM_DATE AS TXNDATE,AMOUNT_UNUTILIZED AS OUTSTANDING_AMT,ACK_NACK AS STATUS FROM REP_ORM_VIEW WHERE ORM_REFERENCE_NUMBER=ORM_REFERENCE_NUMBER ";
								    							      if (!commonMethods.isNull(boeVO1.getCustomerCIF()))
								    							      {
								    							        sQuery = sQuery + "AND CIF LIKE '%'||?||'%'";
								    							        cifFlag = true;
								    							      }
								    							      if (!commonMethods.isNull(boeVO1.getBoeIeCode()))
								    							      {
								    							        sQuery = sQuery + "AND IE_CODE LIKE '%'||?||'%'";
								    							        iecFlag = true;
								    							      }
								    							      if (!commonMethods.isNull(boeVO1.getOrmNum()))
								    							      {
								    							        sQuery = sQuery + "AND SUBSTR(ORM_REFERENCE_NUMBER,0,16) = ?";
								    							        ormFlag = true;
								    							      }
								    							      if (!commonMethods.isNull(boeVO1.getOrmFrmDate()))
								    							      {
								    							        sQuery = sQuery + "AND ORM_DATE between ? AND ?";
								    							        frmDateFlag = true;
								    							      }
								    							      pst = new LoggableStatement(con, sQuery);
								    							      if (cifFlag) {
								    							        pst.setString(++setValue, boeVO1.getCustomerCIF());
								    							      }
								    							      if (iecFlag) {
								    							        pst.setString(++setValue, boeVO1.getBoeIeCode());
								    							      }
								    							      if (ormFlag) {
								    							        pst.setString(++setValue, boeVO1.getOrmNum());
								    							      }
								    							      if (frmDateFlag)
								    							      {
								    							        pst.setString(++setValue, boeVO1.getOrmFrmDate());
								    							        if (boeVO1.getOrmToDate().equals("")) {
								    							          pst.setString(++setValue, commonMethods.getProcDate());
								    							        } else {
								    							          pst.setString(++setValue, boeVO1.getOrmToDate());
								    							        }
								    							      }
								    							      logger.info("The Query is : " + pst.getQueryString());
								    							      rs = pst.executeQuery();
								    							      while (rs.next())
								    							      {
								    							        boeVO1 = new BoeVO();
								    							        boeVO1.setPayRef(rs.getString("PAYMENT_REF_NO"));
								    							        boeVO1.setEveRef(rs.getString("EVENT_REF_NO"));
								    							        boeVO1.setTxnDate(rs.getString("TXNDATE"));
								    							        boeVO1.setOsAmt(rs.getString("OUTSTANDING_AMT"));
								    							        boeVO1.setAckNackStatus(rs.getString("STATUS"));
								    							        ormList1.add(boeVO1);
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
								    							    return ormList1;
								    							  }
								    							  public ArrayList<BoeVO> fetchOutstandingBOEList(BoeVO boeVO1)
								    							    throws DAOException
								    							  {
								    							    logger.info("Entering Method");
								    							    String sQuery = "";
								    							    Connection con = null;
								    							    LoggableStatement pst = null;
								    							    ResultSet rs = null;
								    							    ArrayList<BoeVO> boeList1 = null;
								    							    CommonMethods commonMethods = null;
								    							    boolean cifFlag = false;
								    							    boolean iecFlag = false;
								    							    boolean boeFlag = false;
								    							    boolean frmDateFlag = false;
								    							    int setValue = 0;
								    							    try
								    							    {
								    							      commonMethods = new CommonMethods();
								    							      boeList1 = new ArrayList();
								    							      con = DBConnectionUtility.getConnection();
								    							      sQuery = "SELECT BOE_NUMBER AS BOE_NO,TO_CHAR(BOE_DATE,'dd/MM/YYYY') AS BOE_DT,PORT_OF_DISCHARGE AS BOE_PC,AMOUNT_UNUTILIZED AS OS_AMT FROM REP_BOE_VIEW WHERE BOE_NUMBER=BOE_NUMBER ";
								    							      if (!commonMethods.isNull(boeVO1.getCustomerCIF()))
								    							      {
								    							        sQuery = sQuery + "AND CIF_ID LIKE '%'||?||'%'";
								    							        cifFlag = true;
								    							      }
								    							      if (!commonMethods.isNull(boeVO1.getBoeIeCode()))
								    							      {
								    							        sQuery = sQuery + "AND IE_CODE LIKE '%'||?||'%'";
								    							        iecFlag = true;
								    							      }
								    							      if (!commonMethods.isNull(boeVO1.getSearchBoeNo()))
								    							      {
								    							        sQuery = sQuery + "AND BOE_NUMBER = ?";
								    							        boeFlag = true;
								    							      }
								    							      if (!commonMethods.isNull(boeVO1.getBoeFrmDate()))
								    							      {
								    							        sQuery = sQuery + "AND BOE_DATE between ? AND ?";
								    							        frmDateFlag = true;
								    							      }
								    							      pst = new LoggableStatement(con, sQuery);
								    							      if (cifFlag) {
								    							        pst.setString(++setValue, boeVO1.getCustomerCIF());
								    							      }
								    							      if (iecFlag) {
								    							        pst.setString(++setValue, boeVO1.getBoeIeCode());
								    							      }
								    							      if (boeFlag) {
								    							        pst.setString(++setValue, boeVO1.getSearchBoeNo());
								    							      }
								    							      if (frmDateFlag)
								    							      {
								    							        pst.setString(++setValue, boeVO1.getBoeFrmDate());
								    							        if (boeVO1.getOrmToDate().equals("")) {
								    							          pst.setString(++setValue, commonMethods.getProcDate());
								    							        } else {
								    							          pst.setString(++setValue, boeVO1.getOrmToDate());
								    							        }
								    							      }
								    							      logger.info("The Query is : " + pst.getQueryString());
								    							      rs = pst.executeQuery();
								    							      while (rs.next())
								    							      {
								    							        boeVO1 = new BoeVO();
								    							        boeVO1.setBoeNumberSearch(rs.getString("BOE_NO"));
								    							        boeVO1.setBoePortCodeSearch(rs.getString("BOE_PC"));
								    							        boeVO1.setBoeDateSearch(rs.getString("BOE_DT"));
								    							        boeVO1.setBoeOsAmt(rs.getString("OS_AMT"));

								    							 
								    							        boeList1.add(boeVO1);
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
								    							    return boeList1;
								    							  }
								    							  public int deleteBOEData(String[] bill_chkList)
								    							    throws DAOException
								    							  {
								    							    logger.info("----------------------deleteBOEData---------------------");
								    							    int iRet = 0;
								    							    int i = 0;
								    							    String sProcedureName = "";

								    							 
								    							    Connection con = null;
								    							    CallableStatement cs = null;
								    							    CommonMethods commonMethods = null;
								    							    try
								    							    {
								    							      commonMethods = new CommonMethods();
								    							      con = DBConnectionUtility.getConnection();
								    							      if ((bill_chkList != null) && (bill_chkList.length > 0)) {
								    							        for (i = 0; i < bill_chkList.length; i++)
								    							        {
								    							          String tempRefNo = bill_chkList[i];
								    							          if (!tempRefNo.equalsIgnoreCase("false"))
								    							          {
								    							            String[] deleteParam = bill_chkList[i].split(":");

								    							 
								    							 
								    							            logger.info("DELETE The Ref No is : " + deleteParam[0]);
								    							            logger.info("The Eve No is : " + deleteParam[1]);
								    							            logger.info("The Boe No is : " + deleteParam[2]);
								    							            logger.info("The Dat No is : " + deleteParam[3]);
								    							            logger.info("The Por No is : " + deleteParam[4]);
								    							            sProcedureName = "{call ETT_BOE_DELETED_RECORDS(?, ?, ?, ?, ?)}";
								    							            cs = con.prepareCall(sProcedureName);
								    							            cs.setString(1, commonMethods.getEmptyIfNull(deleteParam[0]).trim());
								    							            cs.setString(2, commonMethods.getEmptyIfNull(deleteParam[1]).trim());
								    							            cs.setString(3, commonMethods.getEmptyIfNull(deleteParam[2]).trim());
								    							            cs.setString(4, commonMethods.getEmptyIfNull(deleteParam[3]).trim());
								    							            cs.setString(5, commonMethods.getEmptyIfNull(deleteParam[4]).trim());
								    							            if (cs.executeUpdate() <= 0) {
								    							              logger.info("Procedure having some problem ======================>>> ");
								    							            } else {
								    							              logger.info("Delelete Part Excecuted");
								    							            }
								    							          }
								    							        }
								    							      }
								    							      if (cs != null) {
								    							        cs.close();
								    							      }
								    							    }
								    							    catch (Exception e)
								    							    {
								    							      logger.info("deleteBOEData-----------------exception" + e);
								    							      throwDAOException(e);
								    							    }
								    							    logger.info("Exiting Method");
								    							    return iRet;
								    							  }
								    							  public BoeVO retriveDataFromPayTable(BoeVO boeVO)
								    									    throws DAOException
								    									  {
								    									    logger.info("This BOE ----------retriveDataFromPayTable-----------");
								    									    double availableAmt = 0.0D;
								    									    String sPayRefNum = null;
								    									    String sEveRefNum = null;
								    									    String sBoeNum = null;
								    									    String sBoeDate = null;
								    									    String sPortCode = null;
								    									    String sQuery = null;
								    									    Connection con = null;
								    									    LoggableStatement pst = null;
								    									    LoggableStatement pst1 = null;
								    									    ResultSet rs = null;
								    									    ResultSet rs1 = null;
								    									    ArrayList<TransactionVO> invoiceList = null;
								    									    CommonMethods commonMethods = null;
								    									    try
								    									    {
								    									      con = DBConnectionUtility.getConnection();
								    									      commonMethods = new CommonMethods();
								    									      if (boeVO.getBoeValue() != null)
								    									      {
								    									        String[] tempVal = boeVO.getBoeValue().split(":");
								    									        sPayRefNum = tempVal[0];
								    									        sEveRefNum = tempVal[1];
								    									        sBoeNum = tempVal[2];
								    									        sBoeDate = tempVal[3];
								    									        sPortCode = tempVal[4];
								    									      }
								    									      boeVO.setPaymentRefNo(sPayRefNum);
								    									      boeVO.setPartPaymentSlNo(sEveRefNum);
								    									      boeVO.setBoeNo(sBoeNum);
								    									      boeVO.setBoeDate(sBoeDate);
								    									      boeVO.setPortCode(sPortCode);
								    									      sQuery = "SELECT BOE_ENTRY_AMT-ENDORSED_BOE_AMT FROM ETT_BOE_PAYMENT WHERE BOE_PAYMENT_BP_BOE_NO = ? AND STATUS = 'P' AND TO_CHAR(BOE_PAYMENT_BP_BOE_DT, 'DD/MM/YYYY') = ? AND PORT_CODE = ?";
								    									      pst1 = new LoggableStatement(con, sQuery);
								    									      pst1.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
								    									      pst1.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
								    									      pst1.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
								    									      logger.info("This BOE ----------retriveDataFromPayTable----query-------" + pst1.getQueryString());
								    									      rs1 = pst1.executeQuery();
								    									      if (rs1.next()) {
								    									        availableAmt = rs1.getInt(1);
								    									      }
								    									      sQuery = "SELECT BP.BOE_PAYMENT_BP_BOE_NO AS BOE_PAYMENT_BP_BOE_NO, TO_CHAR(BP.BOE_PAYMENT_BP_BOE_DT,'DD/MM/YYYY') AS BOE_PAYMENT_BP_BOE_DT, BP.PORT_CODE AS PORT_CODE, BP.BOE_TRANS_TYPE AS BOE_TRANS_TYPE, EB.PD_CIF_NAME AS PD_CIF_NAME, EB.PD_CIF_ID AS PD_CIF_ID, BP.IECODE AS IE_CODE, BP.ADCODE AS AD_CODE, BP.CHANGED_IE_CODE AS CHANGED_IE_CODE, BP.IE_ADDRESS AS IE_ADDRESS, BP.IE_PAN AS IE_PAN, EB.BENEF_NAME AS BENEF_NAME, EB.BENEF_COUNTRY AS BENEF_COUNTRY, BP.BOE_PAYMENT_BP_BOE_CCY AS BOE_PAYMENT_BP_BOE_CCY, NVL(TRIM(BP.BOE_ENTRY_AMT), '0') AS BOE_ENTRY_AMT, BP.BOE_PAYMENT_BP_PAY_REF AS BOE_PAYMENT_BP_PAY_REF, BP.BOE_PAYMENT_BP_PAY_PART_REF AS BOE_PAYMENT_BP_PAY_PART_REF, NVL(TRIM(BP.BOE_PAYMENT_BP_PAY_FC_AMT), '0') AS BOE_PAYMENT_BP_PAY_FC_AMT, NVL(TRIM(EB.PD_OUTSTANDING_AMT), '0') AS PD_OUTSTANDING_AMT, TO_CHAR(EB.PD_TXN_DT,'DD/MM/YYYY') AS PD_TXN_DT, EB.PD_TXN_CCY AS PD_TXN_CCY, BP.EXCHANGE_RATE AS EXCHANGE_RATE, NVL(TRIM(BP.BOE_PAYMENT_BP_PAY_FULL_YN),' ') AS BOE_PAYMENT_BP_PAY_FULL_YN, NVL(TRIM(BP.BOE_PAYMENT_BP_PAY_ENDORSE_AMT), '0') AS BOE_PAYMENT_BP_PAY_ENDORSE_AMT, NVL(TRIM(BP.BOE_IGMNUMBER),'') AS BOE_IGMNUMBER, TO_CHAR(BP.BOE_IGMDATE,'DD/MM/YYYY') AS BOE_IGMDATE, NVL(TRIM(BP.BOE_HAWB_HBLNUMBER),'') AS BOE_HAWB_HBLNUMBER, TO_CHAR(BP.BOE_HAWB_HBLDATE,'DD/MM/YYYY') AS BOE_HAWB_HBLDATE, NVL(TRIM(BP.BOE_MAWB_MBLNUMBER),'') AS BOE_MAWB_MBLNUMBER, TO_CHAR(BP.BOE_MAWB_MBLDATE,'DD/MM/YYYY') AS BOE_MAWB_MBLDATE, DECODE(BP.IMPORT_AGENCY, 1, 'Customs', 'SEZ') AS IMPORT_AGENCY, BP.PORT_OF_SHIPMENT AS PORT_OF_SHIPMENT, BP.G_P AS G_P, DECODE(BP.BES_IND, 1, 'NEW', 3, 'CANCEL') AS BES_IND, DECODE(BP.RECORD_INDICATOR, 1, 'New', 2, 'Amendment', 3, 'Cancel') AS RECORD_INDICATOR, BP.REMARKS AS REMARKS, BP.PRE_ENDORSED_AMT AS PRE_ENDORSED_AMT FROM ETT_BOE_PAYMENT BP, ETTV_BOE_PAYMENT_DETAILS EB WHERE EB.PD_TXN_REF = BP.BOE_PAYMENT_BP_PAY_REF AND EB.PD_PART_PAY_REF = BP.BOE_PAYMENT_BP_PAY_PART_REF AND BP.BOE_PAYMENT_BP_BOE_NO = ? AND TO_CHAR(BP.BOE_PAYMENT_BP_BOE_DT,'DD/MM/YYYY') = ? AND BP.PORT_CODE = ? AND BP.BOE_PAYMENT_BP_PAY_REF = ? AND BP.BOE_PAYMENT_BP_PAY_PART_REF = ? AND BP.STATUS = 'P'";
								    									      pst = new LoggableStatement(con, sQuery);
								    									      pst.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
								    									      pst.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
								    									      pst.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
								    									      pst.setString(4, commonMethods.getEmptyIfNull(boeVO.getPaymentRefNo()).trim());
								    									      pst.setString(5, commonMethods.getEmptyIfNull(boeVO.getPartPaymentSlNo()).trim());
								    									      logger.info("Check P SELECT_BOE_DATA For Many Bill: " + pst.getQueryString());
								    									      rs = pst.executeQuery();
								    									      if (rs.next())
								    									      {
								    									        boeVO.setBoeNo(commonMethods.getEmptyIfNull(rs.getString("BOE_PAYMENT_BP_BOE_NO").trim()));
								    									        boeVO.setBoeDate(commonMethods.getEmptyIfNull(rs.getString("BOE_PAYMENT_BP_BOE_DT").trim()));
								    									        boeVO.setPortCode(commonMethods.getEmptyIfNull(rs.getString("PORT_CODE").trim()));
								    									        boeVO.setTransType(commonMethods.getEmptyIfNull(rs.getString("BOE_TRANS_TYPE").trim()));
								    									        boeVO.setIename(commonMethods.getEmptyIfNull(rs.getString("PD_CIF_NAME").trim()));
								    									        boeVO.setCifNo(commonMethods.getEmptyIfNull(rs.getString("PD_CIF_ID").trim()));
								    									        boeVO.setIeCode(commonMethods.getEmptyIfNull(rs.getString("IE_CODE").trim()));
								    									        boeVO.setAdCode(commonMethods.getEmptyIfNull(rs.getString("AD_CODE").trim()));
								    									        boeVO.setIeCodeChange(commonMethods.getEmptyIfNull(rs.getString("CHANGED_IE_CODE")));
								    									        boeVO.setIeadd(commonMethods.getEmptyIfNull(rs.getString("IE_ADDRESS").trim()));
								    									        boeVO.setIepan(commonMethods.getEmptyIfNull(rs.getString("IE_PAN").trim()));
								    									        boeVO.setBenefName(commonMethods.getEmptyIfNull(rs.getString("BENEF_NAME").trim()));
								    									        boeVO.setBenefCountry(commonMethods.getEmptyIfNull(rs.getString("BENEF_COUNTRY").trim()));
								    									        boeVO.setBillCurrency(commonMethods.getEmptyIfNull(rs.getString("BOE_PAYMENT_BP_BOE_CCY").trim()));
								    									        boeVO.setBillAmt(CommonMethods.setCheckValue(rs.getString("BOE_ENTRY_AMT")));
								    									        boeVO = fetchPreviousEndoreAmount(con, boeVO);
								    									        boeVO.setEndorseAmt(commonMethods.toDouble(boeVO.getTotalEndorseAmt()));
								    									        boeVO.setPaymentRefNo(commonMethods.getEmptyIfNull(rs.getString("BOE_PAYMENT_BP_PAY_REF")));
								    									        boeVO.setPartPaymentSlNo(
								    									          commonMethods.getEmptyIfNull(rs.getString("BOE_PAYMENT_BP_PAY_PART_REF")));
								    									        boeVO.setPaymentAmount(commonMethods.toDouble(rs.getString("BOE_PAYMENT_BP_PAY_FC_AMT")));
								    									        boeVO.setOutAmt(commonMethods.toDouble(rs.getString("PD_OUTSTANDING_AMT")));
								    									        boeVO.setPayDate(commonMethods.getEmptyIfNull(rs.getString("PD_TXN_DT")));
								    									        boeVO.setPaymentCurr(commonMethods.getEmptyIfNull(rs.getString("PD_TXN_CCY")));
								    									        boeVO.setBoeExRate(commonMethods.getEmptyIfNull(rs.getString("EXCHANGE_RATE")));
								    									        boeVO.setExRate(commonMethods.getEmptyIfNull(rs.getString("EXCHANGE_RATE")));
								    									        boeVO.setFullyAlloc(commonMethods.getEmptyIfNull(rs.getString("BOE_PAYMENT_BP_PAY_FULL_YN")));
								    									        BigDecimal availAmt = BigDecimal.valueOf(availableAmt).setScale(2, RoundingMode.HALF_UP);
								    									        boeVO.setActualEndorseAmt(String.valueOf(availAmt));

								    									 
								    									        double dPreEndorseAmt = Double.parseDouble(rs.getString("PRE_ENDORSED_AMT"));
								    									        double dExRate = Double.parseDouble(rs.getString("EXCHANGE_RATE"));
								    									        double dEqPaymentAmount = dPreEndorseAmt * dExRate;
								    									        BigDecimal bdEqPaymentAmount = BigDecimal.valueOf(dEqPaymentAmount).setScale(2, RoundingMode.HALF_UP);
								    									        boeVO.setAllocAmt(commonMethods.toDouble(rs.getString("PRE_ENDORSED_AMT")));
								    									        boeVO.setEqPaymentAmount(commonMethods.toDouble(bdEqPaymentAmount.toString()));
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
								    									      invoiceList = fetchInvoiceListToModify(con, boeVO1);
								    									      if ((invoiceList.size() > 0) && (!invoiceList.isEmpty()))
								    									      {
								    									        boeVO.setInvoiceList(invoiceList);
								    									        boeVO.setBillCurrency(((TransactionVO)invoiceList.get(0)).getInvoiceCurr());
								    									      }
								    									    }
								    									    catch (Exception e)
								    									    {
								    									      logger.info("This BOE ----------retriveDataFromPayTable-----------" + e);
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
								    							    logger.info("fetchInvoiceListToModify---------------");
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
								    							      query = "SELECT INV_SERIAL_NO, INV_NO, INV_TERMS, FOB_AMT AS FOB_AMT, FOB_CURR, FRI_AMT AS FRI_AMT, FRI_CURR, INS_AMT AS INS_AMT, INS_CURR, SUM(REAL_AMT) AS REAL_AMT,  SUM(REAL_ORM_AMT)     AS REAL_ORM_AMT, SUM(REAL_MOD_AMT)  AS REAL_MOD_AMT, SUM(REAL_ORM_MOD_AMT) AS REAL_ORM_MOD_AMT, SUM(CLO_AMTIC) AS CLO_AMTIC FROM (SELECT INV_SERIAL_NO INV_SERIAL_NO, INV_NO INV_NO,INV_TERMS INV_TERMS,FOB_AMT FOB_AMT, FOB_CURR FOB_CURR, FRI_AMT FRI_AMT, FRI_CURR FRI_CURR, INS_AMT INS_AMT, INS_CURR INS_CURR, 0 REAL_AMT, 0 AS REAL_ORM_AMT, REAL_MOD_AMT REAL_MOD_AMT, REAL_ORM_MOD_AMT REAL_ORM_MOD_AMT, CLOSURE_AMTIC CLO_AMTIC FROM ETTV_BOE_MOD_INV_DETAILS WHERE BOE_NUMBER = ? AND TO_CHAR(BOE_DATE,'DD/MM/YYYY') = ? AND BOE_PORT_OF_DISCHARGE = ? UNION ALL SELECT INV_SERIAL_NO INV_SERIAL_NO, INV_NO INV_NO, INV_TERMS INV_TERMS, FOB_AMT FOB_AMT, FOB_CURR FOB_CURR, FRI_AMT FRI_AMT, FRI_CURR FRI_CURR, INS_AMT INS_AMT, INS_CURR INS_CURR,  REAL_AMT REAL_AMT,  REAL_AMTIC AS REAL_ORM_AMT, 0 REAL_MOD_AMT, 0 AS REAL_ORM_MOD_AMT, CLOSURE_AMTIC CLO_AMTIC FROM ETTV_BOE_INV_DETAIL WHERE BOE_NUMBER = ? AND TO_CHAR(BOE_DATE,'DD/MM/YYYY') = ? AND BOE_PORT_OF_DISCHARGE = ?) GROUP BY INV_SERIAL_NO, INV_NO, INV_TERMS,FOB_AMT,FOB_CURR, FRI_AMT, FRI_CURR, INS_AMT, INS_CURR ORDER BY TO_NUMBER(INV_SERIAL_NO) ASC";
								    							      pst = new LoggableStatement(con, query);
								    							      pst.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
								    							      pst.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
								    							      pst.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
								    							      pst.setString(4, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
								    							      pst.setString(5, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
								    							      pst.setString(6, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
								    							      logger.info(" SELECT_MOD_INV_DETAILS Invoice Query --> " + pst.getQueryString());
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
								    							        String tempVal = commonMethods.getEmptyIfNull(rs.getString("INV_SERIAL_NO")).trim() + ":" + 
								    							          commonMethods.getEmptyIfNull(rs.getString("INV_NO")).trim();
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
								    							        invoiceList.add(invoiceVO);
								    							      }
								    							    }
								    							    catch (Exception e)
								    							    {
								    							      logger.info("Exception  SELECT_MOD_INV_DETAILS in is " + e.getMessage());
								    							    }
								    							    finally
								    							    {
								    							      closeSqlRefferance(rs, pst);
								    							    }
								    							    logger.info("Exiting Method");
								    							    return invoiceList;
								    							  }
								    							  public int updateBOEModification(Connection con, BoeVO boeVO)
								    							  {
								    							    logger.info("Entering Method");
								    							    int iRet = 0;
								    							    String sBOEModificationQuery = null;
								    							    String sBOEPayQuery = null;
								    							    double dAllocatAmount = 0.0D;
								    							    double dPayFcAmt = 0.0D;
								    							    double dPayEndorseAmt = 0.0D;
								    							    double dPayEDSFcAmt = 0.0D;
								    							    double dPayOSFcAmt = 0.0D;
								    							    double dBOEEntryAmt = 0.0D;
								    							    double dEndorsedAmt = 0.0D;
								    							    double dPreEndorsedAmt = 0.0D;
								    							    LoggableStatement pst = null;
								    							    LoggableStatement pst1 = null;
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
								    							      sBOEPayQuery = "SELECT BOE_PAYMENT_BP_PAY_FC_AMT, BOE_PAYMENT_BP_PAY_ENDORSE_AMT, BOE_PAYMENT_BP_PAY_EDS_FC_AMT, BOE_PAYMENT_BP_PAY_OS_FC_AMT, BOE_ENTRY_AMT, ENDORSED_BOE_AMT, PRE_ENDORSED_AMT FROM ETT_BOE_PAYMENT WHERE BOE_PAYMENT_BP_PAY_REF = ? AND BOE_PAYMENT_BP_PAY_PART_REF = ? AND BOE_PAYMENT_BP_BOE_NO = ? AND TO_CHAR(BOE_PAYMENT_BP_BOE_DT,'DD/MM/YYYY') = ? AND PORT_CODE = ? AND STATUS = 'P'";
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
								    							        dBOEEntryAmt = commonMethods.convertToDouble(rs.getString("BOE_ENTRY_AMT"));
								    							        dEndorsedAmt = commonMethods.convertToDouble(rs.getString("ENDORSED_BOE_AMT"));
								    							        dPreEndorsedAmt = commonMethods.convertToDouble(rs.getString("PRE_ENDORSED_AMT"));
								    							      }
								    							      closeSqlRefferance(rs, pst);
								    							      dAllocatAmount = commonMethods.convertToDouble(boeVO.getAllocAmt());
								    							      if (dPreEndorsedAmt > dAllocatAmount)
								    							      {
								    							        double dTempValue = dPreEndorsedAmt - dAllocatAmount;
								    							        dPayEndorseAmt -= dTempValue;
								    							        dPayEDSFcAmt -= dTempValue;
								    							        dPayOSFcAmt -= dTempValue;
								    							        dEndorsedAmt -= dTempValue;
								    							        dPreEndorsedAmt -= dTempValue;
								    							      }
								    							      if (dPreEndorsedAmt < dAllocatAmount)
								    							      {
								    							        double dTempValue = dAllocatAmount - dPreEndorsedAmt;
								    							        dPayEndorseAmt += dTempValue;
								    							        dPayEDSFcAmt += dTempValue;
								    							        dPayOSFcAmt += dTempValue;
								    							        dEndorsedAmt += dTempValue;
								    							        dPreEndorsedAmt += dTempValue;
								    							      }
								    							      sBOEModificationQuery = "UPDATE ETT_BOE_PAYMENT SET BOE_PAYMENT_BP_BOE_DT = TO_DATE(?,'DD-MM-YYYY'), BOE_PAYMENT_BP_PAY_FC_AMT =?, BOE_PAYMENT_BP_PAY_ENDORSE_AMT = TO_NUMBER (?), BOE_PAYMENT_BP_PAY_EDS_FC_AMT  = TO_NUMBER (?), BOE_PAYMENT_BP_PAY_OS_FC_AMT  = ?, BOE_PAYMENT_BP_PAY_FULL_YN = ?, BOE_PAYMENT_BP_BOE_CCY = ?, BOE_ENTRY_AMT=?, ENDORSED_BOE_AMT = TO_NUMBER(?), PORT_CODE = ?, USERID = ?,STATUS =?,REMARKS =?, THIRD_PARTY_PAYMENT = ?, CHANGED_IE_CODE =?, BOE_TRANS_TYPE =?, PAGETYPE =?, LASTUPDATE = SYSTIMESTAMP, BES_IND =?, PRE_ENDORSED_AMT = TO_NUMBER(?) WHERE BOE_PAYMENT_BP_PAY_REF = ? AND BOE_PAYMENT_BP_PAY_PART_REF = ? AND BOE_PAYMENT_BP_BOE_NO = ? AND TO_CHAR(BOE_PAYMENT_BP_BOE_DT,'DD/MM/YYYY') = ? AND PORT_CODE = ?";

								    							      
								    							      
								    							      
								    							      
								    							      
								    							      
								    							      pst1 = new LoggableStatement(con, sBOEModificationQuery);
								    							      pst1.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
								    							      pst1.setString(2, CommonMethods.setDefaultAmount(boeVO.getPaymentAmount()));
								    							      pst1.setString(3, CommonMethods.setDefaultAmount(dPayEndorseAmt));
								    							      pst1.setString(4, CommonMethods.setDefaultAmount(dPayEDSFcAmt));
								    							      pst1.setString(5, CommonMethods.setDefaultAmount(dPayOSFcAmt));
								    							      pst1.setString(6, commonMethods.getEmptyIfNull(boeVO.getFullyAlloc()).trim());
								    							      pst1.setString(7, commonMethods.getEmptyIfNull(boeVO.getPaymentCurr()).trim());
								    							      pst1.setString(8, commonMethods.getEmptyIfNull(boeVO.getBillAmt()).trim());
								    							      pst1.setString(9, CommonMethods.setDefaultAmount(dEndorsedAmt));
								    							      pst1.setString(10, loginedUserId);
								    							      pst1.setString(11, "P");
								    							      pst1.setString(12, commonMethods.getEmptyIfNull(boeVO.getRemarks()).trim());
								    							      pst1.setString(13, commonMethods.getEmptyIfNull(boeVO.getThrdParty()).trim());
								    							      pst1.setString(14, commonMethods.getEmptyIfNull(boeVO.getIeCodeChange()).trim());
								    							      pst1.setString(15, commonMethods.getEmptyIfNull(boeVO.getTransType()).trim());
								    							      pst1.setString(16, "S");
								    							      pst1.setString(17, commonMethods.getEmptyIfNull(boeVO.getBoeBesSBInd()).trim());
								    							      pst1.setString(18, CommonMethods.setDefaultAmount(dPreEndorsedAmt));
								    							      pst1.setString(19, commonMethods.getEmptyIfNull(boeVO.getPaymentRefNo()).trim());
								    							      pst1.setString(20, commonMethods.getEmptyIfNull(boeVO.getPartPaymentSlNo()).trim());
								    							      pst1.setString(21, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
								    							      pst1.setString(22, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
								    							      pst1.setString(23, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
								    							      logger.info("updateBillData For many Bill: " + pst.getQueryString());
								    							      iRet = pst1.executeUpdate();
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
								    							  public int updateInvoiceData(Connection con, String[] chkInvlist, BoeVO boeVO)
								    							  {
								    							    logger.info("Entering Method");
								    							    int iRet = 0;
								    							    double dOldRealAmt = 0.0D;
								    							    double dOldRealOrmAmt = 0.0D;
								    							    double dOldRealModAmt = 0.0D;
								    							    double dOldRealOrmModAmt = 0.0D;
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
								    							      if ((chkInvlist != null) && (chkInvlist.length > 0)) {
								    							        for (int i = 0; i < chkInvlist.length; i++)
								    							        {
								    							          String tempRefNo = chkInvlist[i];
								    							          if (!tempRefNo.equalsIgnoreCase("false"))
								    							          {
								    							            String[] s = chkInvlist[i].split(":");
								    							            String invSerialNo = commonMethods.getEmptyIfNull(s[0]).trim();
								    							            String invNo = commonMethods.getEmptyIfNull(s[1]).trim();
								    							            String realAmt = commonMethods.getEmptyIfNull(s[2]).trim();
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
								    							              dOldRealAmt = commonMethods.convertToDouble(rs.getString("REAL_AMT"));
								    							              dOldRealOrmAmt = commonMethods.convertToDouble(rs.getString("REAL_ORM_AMT"));
								    							              dOldRealModAmt = commonMethods.convertToDouble(rs.getString("REAL_MOD_AMT"));
								    							              dOldRealOrmModAmt = commonMethods.convertToDouble(rs.getString("REAL_ORM_MOD_AMT"));
								    							            }
								    							            if (dOldRealModAmt < dNewRealAmt)
								    							            {
								    							              double dTempValue = dNewRealAmt - dOldRealModAmt;
								    							              dOldRealAmt += dTempValue;
								    							              dOldRealOrmAmt += dTempValue;
								    							              dOldRealModAmt += dTempValue;
								    							              dOldRealOrmModAmt += dTempValue;
								    							            }
								    							            if (dOldRealModAmt > dNewRealAmt)
								    							            {
								    							              double dTempValue = dOldRealModAmt - dNewRealAmt;
								    							              dOldRealAmt -= dTempValue;
								    							              dOldRealOrmAmt -= dTempValue;
								    							              dOldRealModAmt -= dTempValue;
								    							              dOldRealOrmModAmt -= dTempValue;
								    							            }
								    							            sUpdateQuery = "UPDATE ETT_BOE_INV_PAYMENT SET REAL_AMT = ?, REAL_ORM_AMT = ?, REAL_MOD_AMT = ?, REAL_ORM_MOD_AMT = ? WHERE BOE_NO = ? AND BOE_DATE = TO_DATE(?,'DD/MM/YYYY') AND PORTCODE = ? AND INV_SNO = ? AND INV_NO = ? AND EOD_STATUS IS NULL";
								    							            pst1 = new LoggableStatement(con, sUpdateQuery);
								    							            pst1.setString(1, String.valueOf(dOldRealAmt));
								    							            pst1.setString(2, String.valueOf(dOldRealOrmAmt));
								    							            pst1.setString(3, String.valueOf(dOldRealModAmt));
								    							            pst1.setString(4, String.valueOf(dOldRealOrmModAmt));
								    							            pst1.setString(5, boeVO.getBoeNo());
								    							            pst1.setString(6, boeVO.getBoeDate());
								    							            pst1.setString(7, boeVO.getPortCode());
								    							            pst1.setString(8, invSerialNo);
								    							            pst1.setString(9, invNo);
								    							            iRet = pst1.executeUpdate();
								    							          }
								    							        }
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
								    							  public int lddeletedata(BoeVO boeVO)
								    									    throws DAOException
								    									  {
								    									    logger.info("Entering Method");
								    									    Connection con = null;
								    									    LoggableStatement pst = null;
								    									    CommonMethods commonMethods = null;
								    									    int boeCount = 0;
								    									    try
								    									    {
								    									      con = DBConnectionUtility.getConnection();
								    									      commonMethods = new CommonMethods();
								    									      HttpSession session = ServletActionContext.getRequest().getSession();
								    									      String loginedUserId = (String)session.getAttribute("loginedUserId");
								    									      String cifNo = getCifCode(con, boeVO.getIeCode());
								    									      String buttonType = commonMethods.getEmptyIfNull(boeVO.getButtonType()).trim();
								    									      int count = 0;
								    									      String countQuery = "SELECT COUNT(*) AS BOE_COUNT FROM ETT_BOE_DETAILS WHERE BOE_NUMBER = ? AND TO_CHAR(BOE_DATE,'DD/MM/YYYY') = ? AND BOE_PORT_OF_DISCHARGE = ? ";

								    									 
								    									      LoggableStatement ps = new LoggableStatement(con, countQuery);
								    									      ps.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
								    									      ps.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
								    									      ps.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
								    									      ResultSet rs = ps.executeQuery();
								    									      if (rs.next())
								    									      {
								    									        count = rs.getInt("BOE_COUNT");
								    									        logger.info("count" + count);
								    									      }
								    									      closeSqlRefferance(rs, ps);
								    									      CheckBoeCount(boeVO);
								    									    }
								    									    catch (Exception e)
								    									    {
								    									      logger.info("Exception in insertInvoiceDatatoTable DAO" + e.getMessage());
								    									    }
								    									    finally
								    									    {
								    									      closeSqlRefferance(pst, con);
								    									    }
								    									    logger.info("Exiting Method");
								    									    return boeCount;
								    									  }
								    									  public TransactionVO deletedata(BoeVO boeVO, TransactionVO transactionVO)
								    									    throws DAOException
								    									  {
								    									    logger.info("Entering Method");
								    									    Connection con = null;
								    									    LoggableStatement pst = null;
								    									    CommonMethods commonMethods = null;
								    									    try
								    									    {
								    									      con = DBConnectionUtility.getConnection();
								    									      commonMethods = new CommonMethods();
								    									      String query = "";
								    									      logger.info("entering into delete");
								    									      if ((!commonMethods.isNull(boeVO.getButtonType())) && (boeVO.getButtonType().equalsIgnoreCase("Delete")))
								    									      {
								    									        logger.info("enter into invoice delete");
								    									        query = "DELETE FROM ETT_INVOICE_DETAILS WHERE INVOICE_SERIAL_NUMBER=? AND INVOICE_NUMBER=? AND  BOE_NUMBER=? AND BOE_DATE= TO_DATE(?,'DD/MM/YYYY') AND BOE_PORT_OF_DISCHARGE = ? AND STATUS=? ";
								    									        pst = new LoggableStatement(con, query);
								    									        pst.setString(1, commonMethods.getEmptyIfNull(transactionVO.getInvSno()).trim());
								    									        pst.setString(2, commonMethods.getEmptyIfNull(transactionVO.getInvoiceNumber()).trim());
								    									        pst.setString(3, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
								    									        pst.setString(4, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
								    									        pst.setString(5, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
								    									        pst.setString(6, "R");
								    									        logger.info("exit");
								    									      }
								    									      int result = pst.executeUpdate();
								    									      logger.info("Query Status --->" + result);
								    									      CheckBoeCount(boeVO);
								    									    }
								    									    catch (Exception e)
								    									    {
								    									      logger.info("Exception in insertInvoiceDatatoTable DAO" + e.getMessage());
								    									    }
								    									    logger.info("Exiting Method");
								    									    return transactionVO;
								    									  }
								    									  public TransactionVO deleteBOEdata(BoeVO boeVO, TransactionVO transactionVO)
								    									    throws DAOException
								    									  {
								    									    logger.info("Entering Method");
								    									    Connection con = null;
								    									    LoggableStatement pst = null;
								    									    CommonMethods commonMethods = null;
								    									    try
								    									    {
								    									      con = DBConnectionUtility.getConnection();
								    									      commonMethods = new CommonMethods();
								    									      String query = "";
								    									      logger.info("entering into delete");
								    									      if ((!commonMethods.isNull(boeVO.getButtonType())) && (boeVO.getButtonType().equalsIgnoreCase("deleteBOEdata")))
								    									      {
								    									        logger.info(">>>>>>>>>>>>>>>>>>>>" + commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
								    									        logger.info(">>>>>>>>>>>>>>>>>>>>" + commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
								    									        logger.info(">>>>>>>>>>>>>>>>>>>>" + commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
								    									        query = "DELETE FROM ETT_BOE_DETAILS WHERE   BOE_NUMBER=? AND BOE_DATE= TO_DATE(?,'DD/MM/YYYY') AND BOE_PORT_OF_DISCHARGE = ? ";
								    									        pst = new LoggableStatement(con, query);
								    									        pst.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
								    									        pst.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
								    									        pst.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
								    									      }
								    									      int result1 = pst.executeUpdate();
								    									      logger.info("Query Status --->" + result1);
								    									      CheckBoeCount(boeVO);
								    									    }
								    									    catch (Exception e)
								    									    {
								    									      logger.info("Exception in Delete BOE DATA DAO" + e.getMessage());
								    									    }
								    									    logger.info("Exiting Method");
								    									    return transactionVO;
								    									  }
								    									  public TransactionVO updateboeDatatoTable(BoeVO boeVO, TransactionVO transactionVO)
								    									    throws DAOException
								    									  {
								    									    logger.info("Entering Method");
								    									    Connection con = null;
								    									    LoggableStatement pst = null;
								    									    CommonMethods commonMethods = null;
								    									    try
								    									    {
								    									      con = DBConnectionUtility.getConnection();
								    									      commonMethods = new CommonMethods();
								    									      HttpSession session = ServletActionContext.getRequest().getSession();
								    									      String loginedUserId = (String)session.getAttribute("loginedUserId");
								    									      String cifNo = getCifCode(con, boeVO.getIeCode());
								    									      String query = "";
								    									      if ((!commonMethods.isNull(boeVO.getButtonType())) && 
								    									        (boeVO.getButtonType().equalsIgnoreCase("UpdateEditedData")))
								    									      {
								    									        query = "UPDATE ETT_BOE_DETAILS SET BOE_IE_CODE = ?,BOE_IE_NAME = ?,BOE_IE_ADDRESS = ?,BOE_IE_PANNUMBER = ?,CIF = ?,BOE_MAWB_MBLNUMBER = ?,BOE_MAWB_MBLDATE = TO_DATE(?,'DD-MM-YY'),BOE_HAWB_HBLNUMBER = ?,BOE_HAWB_HBLDATE = TO_DATE(?,'DD-MM-YY'),BOE_IGMNUMBER = ?,BOE_IGMDATE = TO_DATE(?,'DD-MM-YY'),BOE_AD_CODE = ?,BOE_IMPORT_AGENCY = ?,BOE_GP = ?,BOE_PORT_OF_SHIPMENT = ?,BOE_RECORD_INDICATOR = ?,STATUS = ?,MAKER_USERID = ?,REMARKS = ?,MAKER_TIMESTAMP = SYSTIMESTAMP  WHERE BOE_NUMBER = ? AND TO_CHAR(BOE_DATE,'DD/MM/YYYY') = ? AND BOE_PORT_OF_DISCHARGE = ? ";

								    									 
								    									 
								    									 
								    									        pst = new LoggableStatement(con, query);
								    									        pst.setString(1, commonMethods.getEmptyIfNull(boeVO.getIeCode()).trim());
								    									        pst.setString(2, commonMethods.getEmptyIfNull(boeVO.getCustName()).trim());
								    									        pst.setString(3, commonMethods.getEmptyIfNull(boeVO.getIeadd()).trim());
								    									        pst.setString(4, commonMethods.getEmptyIfNull(boeVO.getIepan()).trim());
								    									        pst.setString(5, commonMethods.getEmptyIfNull(cifNo).trim());
								    									        pst.setString(6, commonMethods.getEmptyIfNull(boeVO.getMblNo()).trim());
								    									        pst.setString(7, commonMethods.getEmptyIfNull(boeVO.getMblDate()).trim());
								    									        pst.setString(8, commonMethods.getEmptyIfNull(boeVO.getHblNo()).trim());
								    									        pst.setString(9, commonMethods.getEmptyIfNull(boeVO.getHblDate()).trim());
								    									        pst.setString(10, commonMethods.getEmptyIfNull(boeVO.getIgmNo()).trim());
								    									        pst.setString(11, commonMethods.getEmptyIfNull(boeVO.getIgmDate()).trim());
								    									        pst.setString(12, commonMethods.getEmptyIfNull(boeVO.getAdCode()).trim());
								    									        pst.setString(13, commonMethods.getEmptyIfNull(boeVO.getImAgency()).trim());
								    									        pst.setString(14, commonMethods.getEmptyIfNull(boeVO.getGovprv()).trim());
								    									        pst.setString(15, commonMethods.getEmptyIfNull(boeVO.getPos()).trim());
								    									        pst.setString(16, commonMethods.getEmptyIfNull(boeVO.getRecInd()).trim());
								    									        pst.setString(17, "P");
								    									        pst.setString(18, loginedUserId);
								    									        pst.setString(19, commonMethods.getEmptyIfNull(boeVO.getRemarks()).trim());
								    									        pst.setString(20, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
								    									        pst.setString(21, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
								    									        pst.setString(22, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
								    									      }
								    									      logger.info("Invoice Query ---> " + pst.getQueryString());
								    									      int result = pst.executeUpdate();
								    									      boeVO.setUpdateCount(result);
								    									      logger.info("Query Status --->" + result);
								    									      CheckBoeCount(boeVO);
								    									    }
								    									    catch (Exception e)
								    									    {
								    									      logger.info("Exception in insertInvoiceDatatoTable DAO" + e.getMessage());
								    									    }
								    									    logger.info("Exiting Method");
								    									    return transactionVO;
								    									  }
								    									  public boolean checkStatus(BoeVO boeVO)
								    									  {
								    									    logger.info("Entering Method");
								    									    int count = 0;
								    									    boolean flag = false;
								    									    Connection con = null;
								    									    LoggableStatement pst = null;
								    									    ResultSet rst = null;
								    									    CommonMethods commonMethods = null;
								    									    try
								    									    {
								    									      con = DBConnectionUtility.getConnection();
								    									      commonMethods = new CommonMethods();
								    									      String query = "SELECT count(BOE.BOE_NUMBER) AS BOE_COUNT FROM ETT_BOE_DETAILS BOE WHERE BOE.BOE_NUMBER = ? AND BOE.BOE_DATE =TO_DATE(?,'DD-MM-YY') AND BOE.BOE_PORT_OF_DISCHARGE =? ";

								    									      pst = new LoggableStatement(con, query);

								    									      pst.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());

								    									      pst.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());

								    									      pst.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());

								    									      logger.info("Status Query --> " + pst.getQueryString());

								    									      rst = pst.executeQuery();

								    									      if (rst.next()) {

								    									        count = rst.getInt(1);

								    									      }

								    									      if (count > 0) {

								    									        flag = true;

								    									      } else {

								    									        flag = false;

								    									      }

								    									      logger.info("Recored count from DB >>>>>>>>>>>>>>>>>>>>>>" + count);

								    									    }

								    									    catch (Exception e)

								    									    {

								    									      logger.info("Exception in Checking BOE status" + e.getMessage());

								    									    }

								    									    finally

								    									    {

								    									      closeSqlRefferance(rst, pst, con);

								    									    }

								    									    logger.info("Exiting Method");

								    									    return flag;

								    									  }

								    									  public void CheckBoeCount(BoeVO boeVO)

								    									  {

								    									    Connection con = null;

								    									    ResultSet rs = null;

								    									    LoggableStatement pst = null;

								    									    CommonMethods commonMethods = null;

								    									    LoggableStatement pst1 = null;

								    									    ResultSet rs1 = null;

								    									    try

								    									    {

								    									      logger.info("Invoice------------------CheckBoeCount--------------------");

								    									      con = DBConnectionUtility.getConnection();

								    									      commonMethods = new CommonMethods();

								    									      String QUERY = "SELECT COUNT (*)FROM ETT_BOE_DETAILS BOE WHERE BOE.BOE_NUMBER = ? AND BOE.BOE_DATE= TO_DATE(?,'DD/MM/YYYY') AND BOE.BOE_PORT_OF_DISCHARGE = ? AND EXISTS (SELECT COUNT(*) FROM ETT_INVOICE_DETAILS INV WHERE INV.BOE_NUMBER= ? AND INV.BOE_DATE= TO_DATE(?,'DD/MM/YYYY') AND INV.BOE_PORT_OF_DISCHARGE = ?)";

								    									      pst = new LoggableStatement(con, QUERY);

								    									      pst.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());

								    									      pst.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());

								    									      pst.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());

								    									      pst.setString(4, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());

								    									      pst.setString(5, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());

								    									      pst.setString(6, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());

								    									      logger.info("Invoice and BOE Count  Query---> " + pst.getQueryString());

								    									      rs = pst.executeQuery();

								    									      int count = 0;

								    									      if (rs.next()) {

								    									        count = rs.getInt(1);

								    									      }

								    									      boeVO.setRecordCount(String.valueOf(count));

								    									      logger.info("Count from DB >>>>>" + count);

								    									      if (count > 0) {

								    									        boeVO.setInsertCount(String.valueOf(count));

								    									      }

								    									      String QUERY1 = "SELECT BOE.STATUS as boe_state FROM ETT_BOE_DETAILS BOE WHERE BOE.BOE_NUMBER=?";

								    									      pst1 = new LoggableStatement(con, QUERY1);

								    									      pst1.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());

								    									      logger.info("BOE Count  Query---> " + pst1.getQueryString());

								    									      rs1 = pst1.executeQuery();

								    									      if (rs1.next()) {

								    									        boeVO.setBoestatus(rs1.getString("boe_state"));

								    									      }

								    									      logger.info("BOE status from DB >>>>>" + boeVO.getBoestatus());

								    									    }

								    									    catch (SQLException e)

								    									    {

								    									      e.printStackTrace();

								    									    }

								    									    finally

								    									    {

								    									      closeSqlRefferance(rs, pst, con);

								    									    }

								    									  }

								    									}

								    									 