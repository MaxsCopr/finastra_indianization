package in.co.boe.dao.boe_reject;

import in.co.boe.dao.AbstractDAO;
import in.co.boe.dao.boe_checker.BoeCheckerDAO;
import in.co.boe.dao.exception.DAOException;
import in.co.boe.utility.ActionConstants;
import in.co.boe.utility.ActionConstantsQuery;
import in.co.boe.utility.CommonMethods;
import in.co.boe.utility.DBConnectionUtility;
import in.co.boe.utility.LoggableStatement;
import in.co.boe.vo.BOESearchVO;
import in.co.boe.vo.BoeVO;
import in.co.boe.vo.TransactionVO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
 
public class BOERejectedDAO
  extends AbstractDAO
  implements ActionConstantsQuery, ActionConstants
{
  private static Logger logger = Logger.getLogger(BoeCheckerDAO.class.getName());
  static BOERejectedDAO dao;
  public static BOERejectedDAO getDAO()
  {
    if (dao == null) {
      dao = new BOERejectedDAO();
    }
    return dao;
  }
  private String getRejectedRecordsQuery(BOESearchVO boeSearchVO)
  {
    CommonMethods commonMethods = null;
    String query = null;
    try
    {
      commonMethods = new CommonMethods();
      query = "SELECT NVL(TRIM(BOE_PAYMENT_BP_PAY_REF), ' ') as BOE_PAYMENT_BP_PAY_REF,NVL(TRIM(BOE_PAYMENT_BP_PAY_PART_REF), ' ') as BOE_PAYMENT_BP_PAY_PART_REF,NVL(TRIM(BOE_PAYMENT_BP_BOE_NO), ' ') as BOE_PAYMENT_BP_BOE_NO, TO_CHAR(TO_DATE(BOE_PAYMENT_BP_BOE_DT, 'DD-MM-YY'),'DD/MM/YYYY') as BOE_PAYMENT_BP_BOE_DT,NVL(TRIM(BOE_ENTRY_AMT), '0') as BOE_ENTRY_AMT,NVL(TRIM(BOE_PAYMENT_BP_PAY_FC_AMT), '0') BOE_PAYMENT_BP_PAY_FC_AMT,NVL(TRIM(AMOUNTOTHERAD), '0') as AMOUNTOTHERAD, NVL(TRIM(BOE_PAYMENT_BP_PAY_ENDORSE_AMT), '0') as BOE_PAYMENT_BP_PAY_ENDORSE_AMT, NVL(TRIM(BOE_PAYMENT_BP_PAY_OS_FC_AMT), '0') as BOE_PAYMENT_BP_PAY_OS_FC_AMT, NVL(TRIM(BOE_PAYMENT_BP_PAY_FULL_YN),' ') as BOE_PAYMENT_BP_PAY_FULL_YN ,NVL(TRIM(to_char(BOE_PAYMENT_BP_PAY_FC_AMT,'999,999,999,999,999.99')), '0') as BOE_PAYMENT_BP_PAY_FC_AMT,NVL(TRIM(BOE_PAYMENT_BP_PAY_CCY),' ') AS BOE_PAYMENT_BP_PAY_CCY, PORT_CODE,PAGETYPE FROM ETT_BOE_PAYMENT WHERE STATUS='R' ";
      if (!commonMethods.isNull(boeSearchVO.getPaymentRefNo())) {
        query = query + "AND TRIM(BOE_PAYMENT_BP_PAY_REF) = '" + boeSearchVO.getPaymentRefNo().trim() + "'";
      }
      if (!commonMethods.isNull(boeSearchVO.getPaymentSerialNo())) {
        query = query + "AND TRIM(BOE_PAYMENT_BP_PAY_PART_REF) = '" + boeSearchVO.getPaymentSerialNo().trim() + "'";
      }
      if (!commonMethods.isNull(boeSearchVO.getBoeNo())) {
        query = query + "AND TRIM(BOE_PAYMENT_BP_BOE_NO) = '" + boeSearchVO.getBoeNo().trim() + "'";
      }
      if (!commonMethods.isNull(boeSearchVO.getPaymentCurrency())) {
        query = query + "AND TRIM(BOE_PAYMENT_BP_PAY_CCY) = '" + boeSearchVO.getBoeNo().trim() + "'";
      }
    }
    catch (NullPointerException npe)
    {
      npe.printStackTrace();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return query;
  }
  public ArrayList<TransactionVO> fetchRejectedData(BOESearchVO boeSearchVO)
    throws DAOException
  {
    logger.info("Entering Method");
    ArrayList<TransactionVO> list = null;
    TransactionVO transactionVO = null;
    Connection con = null;
    LoggableStatement pst = null;
    ResultSet rs = null;
    CommonMethods commonMethods = null;
    String query = null;
    boolean payRefFlag = false;
    boolean paySerialFlag = false;
    boolean boeNoFlag = false;
    boolean payCurrFlag = false;
    int setValue = 0;
    try
    {
      list = new ArrayList();
      con = DBConnectionUtility.getConnection();

 
 
      commonMethods = new CommonMethods();
      query = "SELECT NVL(TRIM(BOE_PAYMENT_BP_PAY_REF), ' ') as BOE_PAYMENT_BP_PAY_REF,NVL(TRIM(BOE_PAYMENT_BP_PAY_PART_REF), ' ') as BOE_PAYMENT_BP_PAY_PART_REF,NVL(TRIM(BOE_PAYMENT_BP_BOE_NO), ' ') as BOE_PAYMENT_BP_BOE_NO, TO_CHAR(TO_DATE(BOE_PAYMENT_BP_BOE_DT, 'DD-MM-YY'),'DD/MM/YYYY') as BOE_PAYMENT_BP_BOE_DT,NVL(TRIM(BOE_ENTRY_AMT), '0') as BOE_ENTRY_AMT,NVL(TRIM(BOE_PAYMENT_BP_PAY_FC_AMT), '0') BOE_PAYMENT_BP_PAY_FC_AMT,NVL(TRIM(AMOUNTOTHERAD), '0') as AMOUNTOTHERAD, NVL(TRIM(BOE_PAYMENT_BP_PAY_ENDORSE_AMT), '0') as BOE_PAYMENT_BP_PAY_ENDORSE_AMT, NVL(TRIM(BOE_PAYMENT_BP_PAY_OS_FC_AMT), '0') as BOE_PAYMENT_BP_PAY_OS_FC_AMT, NVL(TRIM(BOE_PAYMENT_BP_PAY_FULL_YN),' ') as BOE_PAYMENT_BP_PAY_FULL_YN ,NVL(TRIM(to_char(BOE_PAYMENT_BP_PAY_FC_AMT,'999,999,999,999,999.99')), '0') as BOE_PAYMENT_BP_PAY_FC_AMT,NVL(TRIM(BOE_PAYMENT_BP_PAY_CCY),' ') AS BOE_PAYMENT_BP_PAY_CCY, PORT_CODE,PAGETYPE FROM ETT_BOE_PAYMENT WHERE STATUS='R' ";
      if (!commonMethods.isNull(boeSearchVO.getPaymentRefNo()))
      {
        query = query + "AND TRIM(BOE_PAYMENT_BP_PAY_REF) = ?";
        payRefFlag = true;
      }
      if (!commonMethods.isNull(boeSearchVO.getPaymentSerialNo()))
      {
        query = query + "AND TRIM(BOE_PAYMENT_BP_PAY_PART_REF) =?";
        paySerialFlag = true;
      }
      if (!commonMethods.isNull(boeSearchVO.getBoeNo()))
      {
        query = query + "AND TRIM(BOE_PAYMENT_BP_BOE_NO) =?";
        boeNoFlag = true;
      }
      if (!commonMethods.isNull(boeSearchVO.getPaymentCurrency()))
      {
        query = query + "AND TRIM(BOE_PAYMENT_BP_PAY_CCY) = ?";
        payCurrFlag = true;
      }
      pst = new LoggableStatement(con, query);
      if (payRefFlag) {
        pst.setString(++setValue, boeSearchVO.getPaymentRefNo().trim());
      }
      if (paySerialFlag) {
        pst.setString(++setValue, boeSearchVO.getPaymentSerialNo().trim());
      }
      if (boeNoFlag) {
        pst.setString(++setValue, boeSearchVO.getBoeNo().trim());
      }
      if (payCurrFlag) {
        pst.setString(++setValue, boeSearchVO.getPaymentCurrency().trim());
      }
      rs = pst.executeQuery();
      while (rs.next())
      {
        transactionVO = new TransactionVO();
        transactionVO.setPaymentRefNo(CommonMethods.setCheckValue(rs
          .getString("BOE_PAYMENT_BP_PAY_REF")));
        transactionVO.setPartPaymentSlNo(CommonMethods.setCheckValue(rs
          .getString("BOE_PAYMENT_BP_PAY_PART_REF")));
        transactionVO.setBoeNo(CommonMethods.setCheckValue(rs
          .getString("BOE_PAYMENT_BP_BOE_NO")));
        transactionVO.setBoeDate(CommonMethods.setCheckValue(rs
          .getString("BOE_PAYMENT_BP_BOE_DT")));
        transactionVO.setPortCode(CommonMethods.setCheckValue(rs
          .getString("PORT_CODE")));
        transactionVO.setPageType(CommonMethods.setCheckValue(rs
          .getString("PAGETYPE")));
        transactionVO.setBillAmt(CommonMethods.setCheckValue(
          rs.getString("BOE_ENTRY_AMT")).toString());
        transactionVO.setAdEndorseAmt(CommonMethods.setCheckValue(rs
          .getString("AMOUNTOTHERAD")));
        transactionVO.setAdEndorseAmt_temp(
          CommonMethods.setCheckValue(rs.getString("AMOUNTOTHERAD")));
        transactionVO.setEndorseAmt(CommonMethods.setCheckValue(rs
          .getString("BOE_PAYMENT_BP_PAY_ENDORSE_AMT")));
        transactionVO.setOutAmt(CommonMethods.setCheckValue(rs
          .getString("BOE_PAYMENT_BP_PAY_OS_FC_AMT")));
        transactionVO.setFullyAlloc(CommonMethods.setCheckValue(rs
          .getString("BOE_PAYMENT_BP_PAY_FULL_YN")));
        transactionVO.setPaymentAmount(CommonMethods.setCheckValue(rs
          .getString("BOE_PAYMENT_BP_PAY_FC_AMT")));
        transactionVO.setPaymentCurr(CommonMethods.setCheckValue(rs
          .getString("BOE_PAYMENT_BP_PAY_CCY")));
        list.add(transactionVO);
      }
    }
    catch (NullPointerException npe)
    {
      npe.printStackTrace();
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
    return list;
  }
  public BoeVO deleteRecordPermanent(BoeVO boeVO)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement pst = null;
    String loginedUserId = "";
    try
    {
      HttpSession session = ServletActionContext.getRequest().getSession();
      loginedUserId = (String)session.getAttribute("loginedUserId");
      con = DBConnectionUtility.getConnection();
      String query = "INSERT INTO ETT_BOE_PAYMENT_REJ_DET(BOE_PAYMENT_CIF_ID,BOE_PAYMENT_CIF_NAME,BOE_PAYMENT_BP_BOE_NO,BOE_PAYMENT_BP_BOE_DT,BOE_PAYMENT_BP_PAY_DT,BOE_PAYMENT_BP_PAY_CCY,BOE_PAYMENT_BP_PAY_FC_AMT,BOE_PAYMENT_BP_PAY_EDS_FC_AMT,BOE_PAYMENT_BP_PAY_OS_FC_AMT,BOE_PAYMENT_BP_PAY_FULL_YN,CREATEDDATE,LASTUPDATE,BOE_ENTRY_AMT,BOE_PAYMENT_BP_BOE_CCY,USERID,AMOUNTOTHERAD,ACTUALAMT,BOE_PAYMENT_BP_PAY_ENDORSE_AMT,REMARKS,STATUS,UPDATED_BY,PORT_CODE,BOE_PAYMENT_BENEF_NAME,BOE_PAYMENT_BENEF_COUNTRY,BOE_MAWB_MBLNUMBER,BOE_MAWB_MBLDATE,BOE_HAWB_HBLNUMBER,BOE_HAWB_HBLDATE,BOE_IGMNUMBER,BOE_IGMDATE,IECODE,ADCODE,RECORD_INDICATOR,IMPORT_AGENCY,PORT_OF_SHIPMENT,THIRD_PARTY_PAYMENT,CHANGED_IE_CODE,EODSTATUS,BOE_TRANS_TYPE,IE_NAME,IE_ADDRESS,IE_PAN,G_P,ENDORSED_BOE_AMT,EXCHANGE_RATE,BOE_PAYMENT_BP_PAY_REF,BOE_PAYMENT_BP_PAY_PART_REF,PAGETYPE,REJ_USERID)(SELECT BOE_PAYMENT_CIF_ID,BOE_PAYMENT_CIF_NAME,BOE_PAYMENT_BP_BOE_NO,BOE_PAYMENT_BP_BOE_DT,BOE_PAYMENT_BP_PAY_DT,BOE_PAYMENT_BP_PAY_CCY,BOE_PAYMENT_BP_PAY_FC_AMT,BOE_PAYMENT_BP_PAY_EDS_FC_AMT,BOE_PAYMENT_BP_PAY_OS_FC_AMT,BOE_PAYMENT_BP_PAY_FULL_YN,CREATEDDATE,LASTUPDATE,BOE_ENTRY_AMT,BOE_PAYMENT_BP_BOE_CCY,USERID,AMOUNTOTHERAD,ACTUALAMT,BOE_PAYMENT_BP_PAY_ENDORSE_AMT,REMARKS,STATUS,UPDATED_BY,PORT_CODE,BOE_PAYMENT_BENEF_NAME,BOE_PAYMENT_BENEF_COUNTRY,BOE_MAWB_MBLNUMBER,BOE_MAWB_MBLDATE,BOE_HAWB_HBLNUMBER,BOE_HAWB_HBLDATE,BOE_IGMNUMBER,BOE_IGMDATE,IECODE,ADCODE,RECORD_INDICATOR,IMPORT_AGENCY,PORT_OF_SHIPMENT,THIRD_PARTY_PAYMENT,CHANGED_IE_CODE,EODSTATUS,BOE_TRANS_TYPE,IE_NAME,IE_ADDRESS,IE_PAN,G_P,ENDORSED_BOE_AMT,EXCHANGE_RATE,BOE_PAYMENT_BP_PAY_REF,BOE_PAYMENT_BP_PAY_PART_REF,PAGETYPE,? FROM ETT_BOE_PAYMENT WHERE BOE_PAYMENT_BP_BOE_NO = ? AND TO_CHAR(BOE_PAYMENT_BP_BOE_DT,'DD/MM/YYYY') = ? AND PORT_CODE = ? AND BOE_PAYMENT_BP_PAY_REF = ? AND BOE_PAYMENT_BP_PAY_PART_REF = ?)";

 
 
 
 
 
 
 
 
 
 
      pst = new LoggableStatement(con, query);
      pst.setString(1, loginedUserId);
      pst.setString(2, boeVO.getBoeNo());
      pst.setString(3, boeVO.getBoeDate());
      pst.setString(4, boeVO.getPortCode());
      pst.setString(5, boeVO.getPaymentRefNo());
      pst.setString(6, boeVO.getPartPaymentSlNo());
      int iCount = pst.executeUpdate();
      if (iCount > 0)
      {
        String delQuery = "DELETE FROM ETT_BOE_PAYMENT WHERE BOE_PAYMENT_BP_BOE_NO = ? AND TO_CHAR(BOE_PAYMENT_BP_BOE_DT,'DD/MM/YYYY') = ? AND PORT_CODE = ? AND BOE_PAYMENT_BP_PAY_REF = ? AND BOE_PAYMENT_BP_PAY_PART_REF = ? ";

 
        LoggableStatement pst1 = new LoggableStatement(con, delQuery);
        pst1.setString(1, loginedUserId);
        pst1.setString(2, boeVO.getBoeNo());
        pst1.setString(3, boeVO.getBoeDate());
        pst1.setString(4, boeVO.getPortCode());
        pst1.setString(5, boeVO.getPaymentRefNo());
        pst1.setString(6, boeVO.getPartPaymentSlNo());
        int delCount = pst1.executeUpdate();
        logger.info("Delete Count --->" + delCount);
        closePreparedStatement(pst1);
      }
    }
    catch (Exception e)
    {
      throwDAOException(e);
    }
    finally
    {
      closeSqlRefferance(pst, con);
    }
    logger.info("Exiting Method");
    return boeVO;
  }
  public void delRecord(String[] chkList)
  {
    logger.info("Entering Method");
    Connection con = null;

 
    int delete = 0;
    LoggableStatement pst1 = null;
    String result = "fail";
    try
    {
      con = DBConnectionUtility.getConnection();
      for (int i = 0; i < chkList.length; i++)
      {
        String temp = chkList[i];
        String[] a = temp.split(":");
        String delQuery = "DELETE FROM ETT_BOE_PAYMENT WHERE BOE_PAYMENT_BP_BOE_NO = ?  AND TO_CHAR(BOE_PAYMENT_BP_BOE_DT,'DD/MM/YYYY') = ?  AND PORT_CODE = ? ";

 
 
        pst1 = new LoggableStatement(con, delQuery);
        pst1.setString(1, a[0]);
        pst1.setString(2, a[1]);
        pst1.setString(3, a[2]);
        delete = pst1.executeUpdate();
      }
      closePreparedStatement(pst1);
      if (delete > 0) {
        result = "success";
      }
    }
    catch (Exception localException) {}finally
    {
      closeSqlRefferance(pst1, con);
    }
    logger.info("Exiting Method");
  }
  public void delRecordBills(String[] bill_chkList)
  {
    logger.info("--------------delRecordBills--------------");
    Connection con = null;

 
    int delete = 0;
    LoggableStatement pst1 = null;
    String result = "fail";
    try
    {
      con = DBConnectionUtility.getConnection();
      for (int i = 0; i < bill_chkList.length; i++)
      {
        String temp = bill_chkList[i];
        String[] a = temp.split(":");
        String delBill_vsMutliple_BOE = "DELETE FROM ETT_BOE_PAYMENT WHERE BOE_PAYMENT_BP_PAY_REF=?";

 
        pst1 = new LoggableStatement(con, delBill_vsMutliple_BOE);
        pst1.setString(1, a[0]);
        logger.info("DELETE FROM ETT_BOE_PAYMEN---------------------query" + pst1.getQueryString());
      }
      closePreparedStatement(pst1);
      if (delete > 0)
      {
        logger.info("DELETE FROM ETT_BOE_PAYMEN-------delete count" + delete);
        result = "success";
      }
    }
    catch (Exception e)
    {
      logger.info("delRecordBills()------------>Exception->" + e.getMessage());
    }
    finally
    {
      closeSqlRefferance(pst1, con);
    }
    logger.info("Exiting Method");
  }
}
