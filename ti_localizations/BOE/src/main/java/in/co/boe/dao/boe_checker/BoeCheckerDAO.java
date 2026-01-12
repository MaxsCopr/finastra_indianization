package in.co.boe.dao.boe_checker;

import com.bs.wiseConnect.tiemail.notification.ExtEmailNotificationClient;
import in.co.boe.dao.AbstractDAO;
import in.co.boe.dao.exception.DAOException;
import in.co.boe.utility.ActionConstants;
import in.co.boe.utility.ActionConstantsQuery;
import in.co.boe.utility.CommonMethods;
import in.co.boe.utility.DBConnectionUtility;
import in.co.boe.utility.LoggableStatement;
import in.co.boe.vo.BOEDataVO;
import in.co.boe.vo.BOESearchVO;
import in.co.boe.vo.BoeVO;
import in.co.boe.vo.InvoiceDetailsVO;
import in.co.boe.vo.TransactionVO;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
 
public class BoeCheckerDAO
  extends AbstractDAO
  implements ActionConstantsQuery, ActionConstants
{
  private static Logger logger = Logger.getLogger(BoeCheckerDAO.class
    .getName());
  static BoeCheckerDAO dao;
  String loginedUserId = null;
  public static BoeCheckerDAO getDAO()
  {
    if (dao == null) {
      dao = new BoeCheckerDAO();
    }
    return dao;
  }
  public ArrayList<TransactionVO> loadMultiPaymentReferenceData(BOESearchVO boeSearchVO)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement loggableStatement = null;
    ResultSet rs = null;
    TransactionVO transactionVO = null;
    String sqlQuery = null;
    ArrayList<TransactionVO> multiPaymentReferenceList = null;
    CommonMethods commonMethods = null;
    String refNo = "";
    boolean refNoFlag = false;
    boolean dateFromFlag = false;
    boolean dateToFlag = false;
    boolean amountFromFlag = false;
    boolean amountToFlag = false;
    boolean currencyFlag = false;
    boolean boeNoFlag = false;
    boolean partSerialNoFlag = false;
    int setValue = 0;
    try
    {
      multiPaymentReferenceList = new ArrayList();
      HttpSession session = ServletActionContext.getRequest().getSession();
      logger.info("The Session value is of login Type is : " + session.getAttribute("loginType"));
      logger.info("The Session value is of login Type is : " + session.getAttribute("loginType"));
      String sLoginType = (String)session.getAttribute("loginType");
      logger.info("The Session value is of login Type is : 1 : " + session.getAttribute("loginType"));
      logger.info("The Session value is of login Type is : 1 : " + session.getAttribute("loginType"));
      logger.info("The master_reference_numberis : 2 : " + session.getAttribute("xMstRefNum"));
      logger.info("The master_reference_numberis : 2 : " + session.getAttribute("xMstRefNum"));
      String master_reference_number = (String)session.getAttribute("xMstRefNum");
      logger.info("master_reference_number----------------" + master_reference_number);
      logger.info("sLoginType----------TI LOGIN-----------------------------" + sLoginType);
      logger.info("sLoginType----------TI LOGIN-----------------------------" + sLoginType);
      if (sLoginType == null) {
        sLoginType = "NO";
      }
      this.loginedUserId = ((String)session.getAttribute("loginedUserId"));
      commonMethods = new CommonMethods();

 
      con = DBConnectionUtility.getConnection();

 
      String BOE_QUERY = "SELECT NVL(TRIM(BOE_PAYMENT_BP_PAY_REF), ' ') as BOE_PAYMENT_BP_PAY_REF,NVL(TRIM(BOE_PAYMENT_BP_PAY_PART_REF), ' ') as BOE_PAYMENT_BP_PAY_PART_REF,NVL(TRIM(BOE_PAYMENT_BP_BOE_NO), ' ') as BOE_PAYMENT_BP_BOE_NO, TO_CHAR(TO_DATE(BOE_PAYMENT_BP_BOE_DT, 'dd-mm-yy'),'dd-mm-yyyy') as BOE_PAYMENT_BP_BOE_DT,NVL(TRIM(to_char(BOE_ENTRY_AMT,'999,999,999,999,999.99')), '0') as BOE_ENTRY_AMT,NVL(TRIM(to_char(BOE_PAYMENT_BP_PAY_FC_AMT,'999,999,999,999,999.99')), '0') BOE_PAYMENT_BP_PAY_FC_AMT,NVL(TRIM(to_char(AMOUNTOTHERAD,'999,999,999,999,999.99')), '0') as AMOUNTOTHERAD, NVL(TRIM(to_char(ACTUALAMT,'999,999,999,999,999.99')), '0') as ACTUALAMT,NVL(TRIM(to_char(BOE_PAYMENT_BP_PAY_ENDORSE_AMT,'999,999,999,999,999.99')), '0') as BOE_PAYMENT_BP_PAY_ENDORSE_AMT, NVL(TRIM(to_char(BOE_PAYMENT_BP_PAY_OS_FC_AMT,'999,999,999,999,999.99')), '0') as BOE_PAYMENT_BP_PAY_OS_FC_AMT, NVL(TRIM(BOE_PAYMENT_BP_PAY_FULL_YN),' ') as BOE_PAYMENT_BP_PAY_FULL_YN ,NVL(TRIM(to_char(BOE_PAYMENT_BP_PAY_FC_AMT,'999,999,999,999,999.99')), '0') as BOE_PAYMENT_BP_PAY_FC_AMT,NVL(TRIM(BOE_PAYMENT_BP_PAY_CCY),' ') AS BOE_PAYMENT_BP_PAY_CCY, PORT_CODE FROM ETT_BOE_PAYMENT WHERE STATUS='P' ";
      if (master_reference_number.equalsIgnoreCase(""))
      {
        logger.info("This is Inside of Login Type = YES");

 
        refNo = boeSearchVO.getPaymentRefNo();
        if (!commonMethods.isNull(refNo))
        {
          BOE_QUERY = BOE_QUERY + "AND UPPER(BOE_PAYMENT_BP_PAY_REF) LIKE UPPER('%'||?||'%')";
          refNoFlag = true;
        }
      }
      else
      {
        refNo = (String)session.getAttribute("xMstRefNum");
        BOE_QUERY = BOE_QUERY + "AND BOE_PAYMENT_BP_PAY_REF = ?";
        refNoFlag = true;
      }
      logger.info("The Payment Ref No is : " + refNo);
      String dateFrom = boeSearchVO.getPaymentDateFrom();
      String dateTo = boeSearchVO.getPaymentDateTo();
      if ((!commonMethods.isNull(dateFrom)) && 
        (!commonMethods.isNull(dateTo)))
      {
        BOE_QUERY = BOE_QUERY + "AND BOE_PAYMENT_BP_BOE_DT BETWEEN TO_DATE(?,'DD-MM-YY') AND TO_DATE(?,'DD-MM-YY')";
        dateFromFlag = true;
        dateToFlag = true;
      }
      else
      {
        if (!commonMethods.isNull(dateFrom))
        {
          BOE_QUERY = BOE_QUERY + " AND BOE_PAYMENT_BP_BOE_DT >= TO_DATE(?,'DD-MM-YY')";
          dateFromFlag = true;
        }
        if (!commonMethods.isNull(dateTo))
        {
          BOE_QUERY = BOE_QUERY + " AND BOE_PAYMENT_BP_BOE_DT <= TO_DATE(?,'DD-MM-YY')";
          dateToFlag = true;
        }
      }
      String amountFrom = boeSearchVO.getAmountFrom();
      String amountTo = boeSearchVO.getAmountTo();
      if ((!commonMethods.isNull(amountFrom)) && 
        (!commonMethods.isNull(amountTo)) && 
        (!amountFrom.equals("undefined")) && 
        (!amountTo.equals("undefined")))
      {
        if (amountFrom.trim().equals(amountTo.trim()))
        {
          BOE_QUERY = BOE_QUERY + "AND BOE_ENTRY_AMT  =  ?";
          amountFromFlag = true;
        }
        else
        {
          BOE_QUERY = BOE_QUERY + "AND BOE_ENTRY_AMT >=  ?AND BOE_ENTRY_AMT <=  ?";
          amountFromFlag = true;
          amountToFlag = true;
        }
      }
      else
      {
        if ((!commonMethods.isNull(boeSearchVO.getAmountFrom())) && 
          (!amountFrom.equals("undefined")))
        {
          BOE_QUERY = BOE_QUERY + "AND BOE_ENTRY_AMT >=  ?";
          amountFromFlag = true;
        }
        if ((!commonMethods.isNull(boeSearchVO.getAmountTo())) && 
          (!amountTo.equals("undefined")))
        {
          BOE_QUERY = BOE_QUERY + "AND BOE_ENTRY_AMT <=  ?";
          amountToFlag = true;
        }
      }
      String currency = boeSearchVO.getPaymentCurrency();
      if (!commonMethods.isNull(currency))
      {
        BOE_QUERY = BOE_QUERY + "AND UPPER(BOE_PAYMENT_BP_PAY_CCY) LIKE UPPER('%'||?||'%')";
        currencyFlag = true;
      }
      String boeNo = boeSearchVO.getBoeNo();
      if (!commonMethods.isNull(boeNo))
      {
        BOE_QUERY = BOE_QUERY + "AND UPPER(BOE_PAYMENT_BP_BOE_NO) LIKE UPPER('%'||?||'%')";
        boeNoFlag = true;
      }
      String partSerialNo = boeSearchVO.getPaymentSerialNo();
      if (!commonMethods.isNull(partSerialNo))
      {
        BOE_QUERY = BOE_QUERY + "AND UPPER(BOE_PAYMENT_BP_BOE_NO) LIKE UPPER('%'||?||'%')";
        partSerialNoFlag = true;
      }
      if (!commonMethods.isNull(this.loginedUserId)) {
        BOE_QUERY = BOE_QUERY + "AND USERID != ? ";
      }
      BOE_QUERY = BOE_QUERY + " ORDER BY BOE_PAYMENT_BP_BOE_NO ";
      sqlQuery = BOE_QUERY;
      logger.info("The Querey is : " + sqlQuery);
      loggableStatement = new LoggableStatement(con, sqlQuery);
      if (refNoFlag) {
        loggableStatement.setString(++setValue, refNo.trim());
      }
      if (dateFromFlag) {
        loggableStatement.setString(++setValue, dateFrom.trim());
      }
      if (dateToFlag) {
        loggableStatement.setString(++setValue, dateTo.trim());
      }
      if (amountFromFlag) {
        loggableStatement.setString(++setValue, amountFrom.trim());
      }
      if (amountToFlag) {
        loggableStatement.setString(++setValue, amountTo.trim());
      }
      if (currencyFlag) {
        loggableStatement.setString(++setValue, currency.trim());
      }
      if (boeNoFlag) {
        loggableStatement.setString(++setValue, boeNo.trim());
      }
      if (partSerialNoFlag) {
        loggableStatement.setString(++setValue, partSerialNo.trim());
      }
      if ((this.loginedUserId != null) && (this.loginedUserId.trim().length() > 0)) {
        loggableStatement.setString(++setValue, this.loginedUserId);
      }
      rs = loggableStatement.executeQuery();
      while (rs.next())
      {
        transactionVO = new TransactionVO();
        logger.info("The While value is : " + rs.getString("BOE_PAYMENT_BP_PAY_REF"));
        transactionVO.setPaymentRefNo(CommonMethods.setCheckValue(rs
          .getString("BOE_PAYMENT_BP_PAY_REF")));
        transactionVO.setPartPaymentSlNo(CommonMethods.setCheckValue(rs
          .getString("BOE_PAYMENT_BP_PAY_PART_REF")));
        transactionVO.setPortCode(CommonMethods.setCheckValue(rs
          .getString("PORT_CODE")));
        transactionVO.setBoeNo(CommonMethods.setCheckValue(rs
          .getString("BOE_PAYMENT_BP_BOE_NO")));
        transactionVO.setBoeDate(CommonMethods.setCheckValue(rs
          .getString("BOE_PAYMENT_BP_BOE_DT")));
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
        multiPaymentReferenceList.add(transactionVO);
      }
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    finally
    {
      closeSqlRefferance(rs, loggableStatement, con);
    }
    logger.info("Exiting Method");
    logger.info("The ArrayList size is in DAO : " + multiPaymentReferenceList.size());
    return multiPaymentReferenceList;
  }
  public String updateStatus(String[] chkList, String check, String remarks)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement loggableStatement = null;
    ResultSet rs = null;
    String result = "fail";
    String query = null;
    int count = 0;
    int autoKey = 0;
    int fx_event = 0;
    int seqNo = 0;
    CommonMethods commonMethods = null;
    try
    {
      commonMethods = new CommonMethods();
      con = DBConnectionUtility.getConnection();
      HttpSession session = ServletActionContext.getRequest().getSession();
      String loginedUserId = (String)session.getAttribute("loginedUserName");
      if ((chkList != null) && (check != null)) {
        for (int i = 0; i < chkList.length; i++)
        {
          String temp = chkList[i];
          String[] a = temp.split(":");
          if (check.equalsIgnoreCase("approve"))
          {
            query = "UPDATE ETT_BOE_PAYMENT SET STATUS ='A',LASTUPDATE = SYSTIMESTAMP,UPDATED_BY=?,REMARKS=?  WHERE BOE_PAYMENT_BP_BOE_NO =? AND TO_CHAR(BOE_PAYMENT_BP_BOE_DT,'DD-MM-YYYY') = ? AND PORT_CODE = ? AND BOE_PAYMENT_BP_PAY_REF = ? AND BOE_PAYMENT_BP_PAY_PART_REF =?";

 
 
            loggableStatement = new LoggableStatement(con, query);
            loggableStatement.setString(1, loginedUserId);
            loggableStatement.setString(2, remarks);
            loggableStatement.setString(3, a[0]);
            loggableStatement.setString(4, a[1]);
            loggableStatement.setString(5, a[2]);
            loggableStatement.setString(6, a[3]);
            loggableStatement.setString(7, a[4]);
            count = loggableStatement.executeUpdate();
            BoeVO boeVO = getPrintBOEData(a[0], a[1], a[2], a[3], a[4]);
            if (count > 0) {
              boeVO = getPrintBOEData(a[0], a[1], a[2], a[3], a[4]);
            }
          }
          else if (check.equalsIgnoreCase("reject"))
          {
            String upQuery = "UPDATE ETT_BOE_PAYMENT SET BOE_PAYMENT_BP_PAY_OS_FC_AMT = TO_NUMBER(BOE_PAYMENT_BP_PAY_OS_FC_AMT)+TO_NUMBER(BOE_PAYMENT_BP_PAY_EDS_FC_AMT), BOE_PAYMENT_BP_PAY_EDS_FC_AMT  = 0, BOE_PAYMENT_BP_PAY_ENDORSE_AMT = 0,ENDORSED_BOE_AMT = 0, BOE_PAYMENT_BP_PAY_FULL_YN = CASE WHEN BOE_PAYMENT_BP_PAY_EDS_FC_AMT != 0 THEN 'N' ELSE 'Y' END, STATUS = 'R',LASTUPDATE = SYSTIMESTAMP, UPDATED_BY = ?,REMARKS = ?, BES_IND = '1'  WHERE BOE_PAYMENT_BP_BOE_NO = ? AND TO_CHAR(BOE_PAYMENT_BP_BOE_DT,'DD-MM-YYYY') = ? AND PORT_CODE =? AND BOE_PAYMENT_BP_PAY_REF = ? AND BOE_PAYMENT_BP_PAY_PART_REF=?";

 
 
 
 
 
            LoggableStatement pst9 = new LoggableStatement(con, upQuery);
            pst9.setString(1, loginedUserId);
            pst9.setString(2, remarks);
            pst9.setString(3, a[0]);
            pst9.setString(4, a[1]);
            pst9.setString(5, a[2]);
            pst9.setString(6, a[3]);
            pst9.setString(7, a[4]);
            count = pst9.executeUpdate();
            closePreparedStatement(pst9);
            if (count > 0)
            {
              String invoiceInsertQuery = "INSERT INTO ETT_BOE_INV_PAYMENT_REJ_DET(INV_SNO,INV_NO,INV_AMT,REAL_AMT, REAL_ORM_AMT,BOE_NO,BOE_DATE,PORTCODE,TERMS_OF_INVOICE,INVOICECURR,PAYMENT_REF,EVENT_REF) SELECT INV_SNO,INV_NO,INV_AMT,REAL_AMT,REAL_ORM_AMT,BOE_NO,BOE_DATE,PORTCODE,TERMS_OF_INVOICE, INVOICECURR,PAYMENT_REF,EVENT_REF FROM ETT_BOE_INV_PAYMENT  WHERE BOE_NO  = ? AND TO_CHAR(BOE_DATE,'DD-MM-YYYY') = ? AND PORTCODE = ? AND PAYMENT_REF =? AND EVENT_REF  = ?";

 
 
 
 
 
              LoggableStatement insertInvoiceDate = new LoggableStatement(con, invoiceInsertQuery);
              insertInvoiceDate.setString(1, a[0]);
              insertInvoiceDate.setString(2, a[1]);
              insertInvoiceDate.setString(3, a[2]);
              insertInvoiceDate.setString(4, a[3]);
              insertInvoiceDate.setString(5, a[4]);
              int backupInvoiceData = insertInvoiceDate.executeUpdate();
              closePreparedStatement(insertInvoiceDate);
              if (backupInvoiceData > 0)
              {
                String deleteInvoiceQuery = "DELETE FROM ETT_BOE_INV_PAYMENT WHERE BOE_NO  = ? AND TO_CHAR(BOE_DATE,'DD-MM-YYYY') =? AND PORTCODE = ? AND PAYMENT_REF = ? AND EVENT_REF  = ?";

 
 
 
                LoggableStatement invoiceDeleteStatement = new LoggableStatement(con, deleteInvoiceQuery);
                invoiceDeleteStatement.setString(1, a[0]);
                invoiceDeleteStatement.setString(2, a[1]);
                invoiceDeleteStatement.setString(3, a[2]);
                invoiceDeleteStatement.setString(4, a[3]);
                invoiceDeleteStatement.setString(5, a[4]);
                int deleteedStatus = invoiceDeleteStatement.executeUpdate();
                logger.info("Deletion of Invoice Data " + deleteedStatus);
                closePreparedStatement(invoiceDeleteStatement);
              }
            }
          }
        }
      }
      if (count > 0) {
          result = "success";
        }
      }
      catch (Exception e)
      {
        logger.info("updateStatus---------------------------Exception-------------" + e);
        e.printStackTrace();
        throwDAOException(e);
      }
      finally
      {
        closeSqlRefferance(rs, loggableStatement, con);
      }
      logger.info("Exiting Method");
      return result;
    }
    public TransactionVO getBOEData(String boeNo, String boeDate, String portCode, String paymentRef, String paymentSlNo)
      throws DAOException
    {
      logger.info("Entering Method");
      Connection con = null;
      LoggableStatement loggableStatement = null;
      ResultSet rs = null;
      TransactionVO transactionVO = null;
      String sqlQuery = null;
      try
      {
        con = DBConnectionUtility.getConnection();
        sqlQuery = "SELECT NVL(TRIM(BP.BOE_PAYMENT_BP_PAY_REF), ' ') as BOE_PAYMENT_BP_PAY_REF,  NVL(TRIM(BP.BOE_PAYMENT_BP_BOE_NO), ' ') as BOE_PAYMENT_BP_BOE_NO,  TO_CHAR(TO_DATE(BP.BOE_PAYMENT_BP_BOE_DT, 'dd-mm-yy'), 'dd/mm/yy') as BOE_PAYMENT_BP_BOE_DT, NVL(TRIM(BP.BOE_PAYMENT_BP_BOE_CCY), ' ') as BOE_PAYMENT_BP_PAY_CCY,PORT_CODE, NVL(TRIM(to_char(BP.BOE_PAYMENT_BP_PAY_ENDORSE_AMT)), '0')as BOE_PAYMENT_BP_PAY_ENDORSE_AMT  FROM ETT_BOE_PAYMENT BP WHERE BOE_PAYMENT_BP_BOE_NO =? AND TO_CHAR(BOE_PAYMENT_BP_BOE_DT,'DD-MM-YYYY') = ? AND PORT_CODE =? AND BOE_PAYMENT_BP_PAY_REF =? AND BOE_PAYMENT_BP_PAY_PART_REF=?";

   
   
   
   
   
        loggableStatement = new LoggableStatement(con, sqlQuery);
        loggableStatement.setString(1, boeNo);
        loggableStatement.setString(2, boeDate);
        loggableStatement.setString(3, portCode);
        loggableStatement.setString(4, paymentRef);
        loggableStatement.setString(5, paymentSlNo);
        rs = loggableStatement.executeQuery();
        if (rs.next())
        {
          transactionVO = new TransactionVO();
          transactionVO.setPaymentRefNo(rs
            .getString("BOE_PAYMENT_BP_PAY_REF"));
          transactionVO.setBoeNo(rs
            .getString("BOE_PAYMENT_BP_BOE_NO"));
          transactionVO.setBoeDate(rs
            .getString("BOE_PAYMENT_BP_BOE_DT"));
          transactionVO.setPortCode(rs.getString("PORT_CODE"));
          transactionVO.setBoeCurr(rs
            .getString("BOE_PAYMENT_BP_PAY_CCY"));
          transactionVO.setBalEndorseAmt(CommonMethods.setCheckValue(rs
            .getString("BOE_PAYMENT_BP_PAY_ENDORSE_AMT")));
        }
      }
      catch (SQLException e)
      {
        throwDAOException(e);
      }
      finally
      {
        closeSqlRefferance(rs, loggableStatement, con);
      }
      logger.info("Exiting Method");
      return transactionVO;
    }
    public BOEDataVO boeDataView(String boeNo, String boeDate, String portCode, String paymentRef, String paymentSlNo)
      throws DAOException
    {
      logger.info("Entering Method");
      Connection con = null;
      LoggableStatement loggableStatement = null;
      ResultSet rs = null;
      BOEDataVO boeDataVO = null;
      String sqlQuery = null;
      CommonMethods commonMethods = null;
      try
      {
        con = DBConnectionUtility.getConnection();
        commonMethods = new CommonMethods();
        sqlQuery = "SELECT BOE_TRANS_TYPE,IECODE,CHANGED_IE_CODE,BOE_IGMNUMBER,TO_CHAR(BOE_IGMDATE,'DD/MM/YYYY') AS BOE_IGMDATE, BOE_HAWB_HBLNUMBER,TO_CHAR(BOE_HAWB_HBLDATE,'DD/MM/YYYY') AS BOE_HAWB_HBLDATE,BOE_MAWB_MBLNUMBER, TO_CHAR(BOE_MAWB_MBLDATE,'DD/MM/YYYY') as BOE_MAWB_MBLDATE,IMPORT_AGENCY,ADCODE,PORT_OF_SHIPMENT, RECORD_INDICATOR,THIRD_PARTY_PAYMENT,BOE_PAYMENT_CIF_ID,BOE_PAYMENT_CIF_NAME,BOE_PAYMENT_BENEF_NAME, BOE_PAYMENT_BENEF_COUNTRY,PORT_CODE,IE_ADDRESS,IE_PAN,G_P, BOE_PAYMENT_BP_PAY_REF,BOE_PAYMENT_BP_PAY_PART_REF,TO_CHAR(BOE_PAYMENT_BP_PAY_DT, 'DD/MM/YYYY') as BOE_PAYMENT_BP_PAY_DT, BOE_PAYMENT_BP_PAY_CCY,BOE_PAYMENT_BP_PAY_FC_AMT,BOE_PAYMENT_BP_BOE_NO, TO_CHAR(BOE_PAYMENT_BP_BOE_DT, 'DD/MM/YYYY') as BOE_PAYMENT_BP_BOE_DT,BOE_ENTRY_AMT, BOE_PAYMENT_BP_BOE_CCY,BOE_PAYMENT_BP_PAY_ENDORSE_AMT,BOE_PAYMENT_BP_PAY_FULL_YN,BOE_PAYMENT_BP_PAY_OS_FC_AMT, AMOUNTOTHERAD,REMARKS,ENDORSED_BOE_AMT,EXCHANGE_RATE FROM ETT_BOE_PAYMENT  WHERE BOE_PAYMENT_BP_BOE_NO = ? AND TO_CHAR(BOE_PAYMENT_BP_BOE_DT,'DD-MM-YYYY') = ? AND PORT_CODE = ? AND BOE_PAYMENT_BP_PAY_REF = ? AND BOE_PAYMENT_BP_PAY_PART_REF =?";

   
   
   
   
   
   
   
   
        loggableStatement = new LoggableStatement(con, sqlQuery);
        loggableStatement.setString(1, commonMethods.getEmptyIfNull(boeNo).trim());
        loggableStatement.setString(2, commonMethods.getEmptyIfNull(boeDate).trim());
        loggableStatement.setString(3, commonMethods.getEmptyIfNull(portCode).trim());
        loggableStatement.setString(4, commonMethods.getEmptyIfNull(paymentRef).trim());
        loggableStatement.setString(5, commonMethods.getEmptyIfNull(paymentSlNo).trim());
        rs = loggableStatement.executeQuery();
        if (rs.next())
        {
          boeDataVO = new BOEDataVO();
          boeDataVO.setBoeType(commonMethods.getEmptyIfNull(
            rs.getString("BOE_TRANS_TYPE")).trim());
          boeDataVO.setBoeNo(commonMethods.getEmptyIfNull(
            rs.getString("BOE_PAYMENT_BP_BOE_NO")).trim());
          boeDataVO.setBoeDate(commonMethods.getEmptyIfNull(
            rs.getString("BOE_PAYMENT_BP_BOE_DT")).trim());
          boeDataVO.setCifNo(commonMethods.getEmptyIfNull(
            rs.getString("BOE_PAYMENT_CIF_ID")).trim());
          boeDataVO.setCustName(commonMethods.getEmptyIfNull(
            rs.getString("BOE_PAYMENT_CIF_NAME")).trim());
          boeDataVO.setPortCode(commonMethods.getEmptyIfNull(
            rs.getString("PORT_CODE")).trim());
          boeDataVO.setBenefName(commonMethods.getEmptyIfNull(
            rs.getString("BOE_PAYMENT_BENEF_NAME")).trim());
          boeDataVO.setBenefCountry(commonMethods.getEmptyIfNull(
            rs.getString("BOE_PAYMENT_BENEF_COUNTRY")).trim());
          boeDataVO.setBillAmt(commonMethods.getEmptyIfNull(
            rs.getString("BOE_ENTRY_AMT")).trim());
          boeDataVO.setBillCurrency(commonMethods.getEmptyIfNull(
            rs.getString("BOE_PAYMENT_BP_BOE_CCY")).trim());
          boeDataVO.setAdEndorseAmt(commonMethods.getEmptyIfNull(
            rs.getString("AMOUNTOTHERAD")).trim());
          String tEndAmt = getTotalEndoreAmount(boeDataVO.getBoeNo(), boeDataVO.getBoeDate(), boeDataVO.getPortCode());
          double totalEndAmt = commonMethods.convertToDouble(tEndAmt);
          double boeAmt = commonMethods.convertToDouble(boeDataVO.getBillAmt());
          double otherAmt = commonMethods.convertToDouble(boeDataVO.getAdEndorseAmt());
          double availableAmt = boeAmt - (totalEndAmt + otherAmt);
          BigDecimal availAmt = BigDecimal.valueOf(availableAmt).setScale(2, RoundingMode.HALF_UP);
          boeDataVO.setActualEndorseAmt(String.valueOf(availAmt));
          boeDataVO.setPaymentRefNo(commonMethods.getEmptyIfNull(
            rs.getString("BOE_PAYMENT_BP_PAY_REF")).trim());
          boeDataVO.setPartPaymentSlNo(commonMethods.getEmptyIfNull(
            rs.getString("BOE_PAYMENT_BP_PAY_PART_REF")).trim());
          boeDataVO.setPayDate(commonMethods.getEmptyIfNull(
            rs.getString("BOE_PAYMENT_BP_PAY_DT")).trim());
          boeDataVO.setPaymentCurr(commonMethods.getEmptyIfNull(
            rs.getString("BOE_PAYMENT_BP_PAY_CCY")).trim());
          boeDataVO.setPaymentAmount(commonMethods.getEmptyIfNull(
            rs.getString("BOE_PAYMENT_BP_PAY_FC_AMT")).trim());
          boeDataVO.setEndorseAmt(commonMethods.getEmptyIfNull(
            rs.getString("BOE_PAYMENT_BP_PAY_ENDORSE_AMT")).trim());
          boeDataVO.setRemarks(commonMethods.getEmptyIfNull(
            rs.getString("REMARKS")).trim());
          boeDataVO.setThrdParty(rs.getString("THIRD_PARTY_PAYMENT"));
          boeDataVO.setPos(rs.getString("PORT_OF_SHIPMENT"));
          boeDataVO.setImAgency(rs.getString("IMPORT_AGENCY"));
          boeDataVO.setIeCode(rs.getString("IECODE"));
          boeDataVO.setIeCodeChange(rs.getString("CHANGED_IE_CODE"));
          boeDataVO.setIgmNo(rs.getString("BOE_IGMNUMBER"));
          boeDataVO.setIgmDate(rs.getString("BOE_IGMDATE"));
          boeDataVO.setHblNo(rs.getString("BOE_HAWB_HBLNUMBER"));
          boeDataVO.setHblDate(rs.getString("BOE_HAWB_HBLDATE"));
          boeDataVO.setMblNo(rs.getString("BOE_MAWB_MBLNUMBER"));
          boeDataVO.setMblDate(rs.getString("BOE_MAWB_MBLDATE"));
          boeDataVO.setAdCode(rs.getString("ADCODE"));
          boeDataVO.setRecInd(rs.getString("RECORD_INDICATOR"));
          boeDataVO.setEqPaymentAmount(rs.getString("ENDORSED_BOE_AMT"));
          boeDataVO.setBoeExRate(rs.getString("EXCHANGE_RATE"));
          boeDataVO.setIeadd(rs.getString("IE_ADDRESS"));
          boeDataVO.setIepan(rs.getString("IE_PAN"));
          boeDataVO.setAllocAmt(rs.getString("BOE_PAYMENT_BP_PAY_ENDORSE_AMT"));
          boeDataVO.setGovprv(rs.getString("G_P"));
          String query = "SELECT NVL(TRIM(PD_OUTSTANDING_AMT), '0') AS PD_OUTSTANDING_AMT FROM ETTV_BOE_PAYMENT_DETAILS  WHERE PD_TXN_REF = ? AND PD_PART_PAY_REF=?";
          LoggableStatement ps = new LoggableStatement(con, query);
          ps.setString(1, commonMethods.getEmptyIfNull(paymentRef).trim());
          ps.setString(2, commonMethods.getEmptyIfNull(paymentSlNo).trim());
          ResultSet rst = ps.executeQuery();
          if (rst.next())
          {
            double outAmt = commonMethods.convertToDouble(rst.getString("PD_OUTSTANDING_AMT"));
            if (outAmt == 0.0D) {
              boeDataVO.setFullyAlloc("Y");
            } else {
              boeDataVO.setFullyAlloc("N");
            }
            boeDataVO.setOutAmt(commonMethods.getEmptyIfNull(rst.getString("PD_OUTSTANDING_AMT")).trim());
          }
          closeSqlRefferance(rst, ps);
        }
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
      finally
      {
        closeSqlRefferance(rs, loggableStatement, con);
      }
      logger.info("Exiting Method");
      return boeDataVO;
    }
    public ArrayList<TransactionVO> fetchInvoiecListChecker(String boeNo, String boeDate, String portCode, String paymentRef, String paymentSlNo)
    {
      Connection con = null;
      LoggableStatement pst = null;
      ArrayList<TransactionVO> invList = null;
      ResultSet rs = null;
      CommonMethods commonMethods = null;
      TransactionVO invoiceVO = null;
      try
      {
        con = DBConnectionUtility.getConnection();
        commonMethods = new CommonMethods();
        invList = new ArrayList();
        String query = "SELECT INV.INVOICE_SERIAL_NUMBER AS INV_SERIAL_NO,INV.INVOICE_NUMBER AS INV_NO, INV.INVOICE_TERMS_OF_INVOICE AS INV_TERMS,INV.INVOICE_FOBAMOUNT AS FOB_AMT, INV.INVOICE_FOBCURRENCY AS FOB_CURR,INV.INVOICE_FRIEGHTAMOUNT AS FRI_AMT, INV.INVOICE_FRIEGHTCURRENCYCODE AS FRI_CURR,INV.INVOICE_INSURANCEAMOUNT AS INS_AMT, INV.INVOICE_INSURANCECURRENCY_CODE AS INS_CURR, NVL(IPAY.REAL_AMT,'0') AS REAL_AMT, NVL(IPAY.REAL_ORM_AMT,'0') AS REAL_ORM_AMT,INV.INVOICE_SUPPLIER_NAME AS INVOICE_SUPPLIER_NAME, INV.INVOICE_SUPPLIER_ADDRESS AS INVOICE_SUPPLIER_ADDRESS,INV.INVOICE_SUPPLIER_COUNTRY AS INVOICE_SUPPLIER_COUNTRY, INV.INVOICE_SELLER_NAME AS INVOICE_SELLER_NAME,INV.INVOICE_SELLER_ADDRESS AS INVOICE_SELLER_ADDRESS, INV.INVOICE_SELLER_COUNTRY AS INVOICE_SELLER_COUNTRY,INV.INVOICE_AGENCY_COMMISSION AS INVOICE_AGENCY_COMMISSION, INV.INVOICE_AGENCY_CURRENCY AS INVOICE_AGENCY_CURRENCY,INV.INVOICE_DISCOUNT_CHARGES AS INVOICE_DISCOUNT_CHARGES, INV.INVOICE_DISCOUNT_CURRENCY AS INVOICE_DISCOUNT_CURRENCY, INV.INVOICE_MISCELLANEOUS_CHARGES AS INVOICE_MISCELLANEOUS_CHARGES, INV.INVOICE_MISCELLANEOUS_CURRENCY AS INVOICE_MISCELLANEOUS_CURRENCY, INV.INVOICE_THIRDPARTY_NAME AS INVOICE_THIRDPARTY_NAME, INV.INVOICE_THIRDPARTY_ADDRESS AS INVOICE_THIRDPARTY_ADDRESS,INV.INVOICE_THIRDPARTY_COUNTRY AS INVOICE_THIRDPARTY_COUNTRY FROM ETT_INVOICE_DETAILS INV,ETT_BOE_INV_PAYMENT IPAY WHERE INV.INVOICE_SERIAL_NUMBER = IPAY.INV_SNO  AND INV.INVOICE_NUMBER = IPAY.INV_NO  AND INV.BOE_NUMBER = IPAY.BOE_NO  AND INV.BOE_DATE = IPAY.BOE_DATE AND INV.BOE_PORT_OF_DISCHARGE = IPAY.PORTCODE  AND INV.BOE_NUMBER =? AND TO_CHAR(INV.BOE_DATE,'DD-MM-YYYY') = ? AND INV.BOE_PORT_OF_DISCHARGE = ? AND IPAY.PAYMENT_REF = ? AND IPAY.EVENT_REF =?  ORDER BY TO_NUMBER(INV.INVOICE_SERIAL_NUMBER) ";

   
   
   
   
   
   
   
   
   
   
   
   
   
        pst = new LoggableStatement(con, query);
        pst.setString(1, commonMethods.getEmptyIfNull(boeNo).trim());
        pst.setString(2, commonMethods.getEmptyIfNull(boeDate).trim());
        pst.setString(3, commonMethods.getEmptyIfNull(portCode).trim());
        pst.setString(4, commonMethods.getEmptyIfNull(paymentRef).trim());
        pst.setString(5, commonMethods.getEmptyIfNull(paymentSlNo).trim());
        rs = pst.executeQuery();
        while (rs.next())
        {
          invoiceVO = new TransactionVO();
          invoiceVO.setInvoiceSerialNumber1(rs.getString("INV_SERIAL_NO"));
          invoiceVO.setInvoiceNumber1(rs.getString("INV_NO"));
          invoiceVO.setInvoiceAmount1(rs.getString("FOB_AMT"));
          invoiceVO.setRealizedAmount1(rs.getString("REAL_AMT"));
          invoiceVO.setRealizedAmountIC(rs.getString("REAL_ORM_AMT"));
          invoiceVO.setInvoiceCurr(rs.getString("FOB_CURR"));
          invoiceVO.setTermsofInvoice(rs.getString("INV_TERMS"));
          invoiceVO.setSupplierName(rs.getString("INVOICE_SUPPLIER_NAME"));
          invoiceVO.setSupplierAddress(rs.getString("INVOICE_SUPPLIER_ADDRESS"));
          invoiceVO.setSupplierCountry(rs.getString("INVOICE_SUPPLIER_COUNTRY"));
          invoiceVO.setSellerName(rs.getString("INVOICE_SELLER_NAME"));
          invoiceVO.setSellerAddress(rs.getString("INVOICE_SELLER_ADDRESS"));
          invoiceVO.setSellerCountry(rs.getString("INVOICE_SELLER_COUNTRY"));
          invoiceVO.setFreightAmount(rs.getString("FRI_AMT"));
          invoiceVO.setFreightCurrencyCode(rs.getString("FRI_CURR"));
          invoiceVO.setInsuranceAmount(rs.getString("INS_AMT"));
          invoiceVO.setInsuranceCurrencyCode(rs.getString("INS_CURR"));
          invoiceVO.setAgencyCommission(rs.getString("INVOICE_AGENCY_COMMISSION"));
          invoiceVO.setAgencyCurrency(rs.getString("INVOICE_AGENCY_CURRENCY"));
          invoiceVO.setDiscountCharges(rs.getString("INVOICE_DISCOUNT_CHARGES"));
          invoiceVO.setDiscountCurrency(rs.getString("INVOICE_DISCOUNT_CURRENCY"));
          invoiceVO.setMiscellaneousCharges(rs.getString("INVOICE_MISCELLANEOUS_CHARGES"));
          invoiceVO.setMiscellaneousCurrency(rs.getString("INVOICE_MISCELLANEOUS_CURRENCY"));
          invoiceVO.setThirdPartyName(rs.getString("INVOICE_THIRDPARTY_NAME"));
          invoiceVO.setThirdPartyAddress(rs.getString("INVOICE_THIRDPARTY_ADDRESS"));
          invoiceVO.setThirdPartyCountry(rs.getString("INVOICE_THIRDPARTY_COUNTRY"));
          invList.add(invoiceVO);
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
      return invList;
    }
    public String getTotalEndoreAmount(String boeNo, String boeDate, String portCode)
      throws DAOException
    {
      logger.info("Entering Method");
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
        sqlQuery = "SELECT ROUND(SUM(ENDORSED_BOE_AMT),2) AS TOTAL_EDS_AMT  FROM ETT_BOE_PAYMENT WHERE BOE_PAYMENT_BP_BOE_NO = ? AND TO_CHAR(BOE_PAYMENT_BP_BOE_DT,'DD/MM/YYYY') = ? AND PORT_CODE = ?  AND STATUS != 'R' ";

   
   
        pst = new LoggableStatement(con, sqlQuery);
        pst.setString(1, commonMethods.getEmptyIfNull(boeNo).trim());
        pst.setString(2, commonMethods.getEmptyIfNull(boeDate).trim());
        pst.setString(3, commonMethods.getEmptyIfNull(portCode).trim());
        logger.info("FetchTotalEndoreAmount For Many Bills: " + pst.getQueryString());
        rs = pst.executeQuery();
        if (rs.next()) {
          totalEndAmt = commonMethods.toDouble(rs.getString("TOTAL_EDS_AMT"));
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
      return totalEndAmt;
    }
    public ArrayList<BoeVO> fetchCheckerManualBOE(BOESearchVO boeSearchVO)
    	    throws DAOException
    	  {
    	    logger.info("Entering Method");
    	    Connection con = null;
    	    LoggableStatement loggableStatement = null;
    	    ResultSet rs = null;
    	    BoeVO boeVO = null;
    	    String sqlQuery = null;
    	    ArrayList<BoeVO> boeList = null;
    	    CommonMethods commonMethods = null;
    	    String BOE_QUERY = null;
    	    String loginedUserId = null;
    	    boolean boeNoFlag = false;
    	    boolean dateFromFlag = false;
    	    boolean dateToFlag = false;
    	    boolean portCodeFlag = false;
    	    boolean loginedUserIdFlag = false;
    	    int setValue = 0;
    	    try
    	    {
    	      con = DBConnectionUtility.getConnection();
    	      boeList = new ArrayList();

    	 
    	 
    	      HttpSession session = ServletActionContext.getRequest()
    	        .getSession();
    	      loginedUserId = (String)session.getAttribute("loginedUserId");
    	      commonMethods = new CommonMethods();
    	      BOE_QUERY = "SELECT BOE_NUMBER,TO_CHAR(BOE_DATE,'DD/MM/YYYY') AS BOE_DATE,BOE_PORT_OF_DISCHARGE,BOE_AD_CODE,BOE_IE_CODE,BOE_IE_NAME,BOE_RECORD_INDICATOR FROM ETT_BOE_DETAILS WHERE BOE_TYPE = 'M' AND STATUS = 'P' ";
    	      String boeNo = boeSearchVO.getBoeNo();
    	      if (!commonMethods.isNull(boeNo))
    	      {
    	        BOE_QUERY = BOE_QUERY + "AND BOE_NUMBER LIKE '%'||?||'%'";
    	        boeNoFlag = true;
    	      }
    	      String dateFrom = boeSearchVO.getPaymentDateFrom();
    	      String dateTo = boeSearchVO.getPaymentDateTo();
    	      if ((!commonMethods.isNull(dateFrom)) && 
    	        (!commonMethods.isNull(dateTo)))
    	      {
    	        BOE_QUERY = BOE_QUERY + "AND BOE_DATE BETWEEN TO_DATE(?,'DD-MM-YY') AND TO_DATE(?,'DD-MM-YY')";
    	        dateFromFlag = true;
    	        dateToFlag = true;
    	      }
    	      else
    	      {
    	        if (!commonMethods.isNull(dateFrom))
    	        {
    	          BOE_QUERY = BOE_QUERY + " AND BOE_DATE >= TO_DATE(?,'DD-MM-YY')";
    	          dateFromFlag = true;
    	        }
    	        if (!commonMethods.isNull(dateTo))
    	        {
    	          BOE_QUERY = BOE_QUERY + " AND BOE_DATE <= TO_DATE(?,'DD-MM-YY')";
    	          dateToFlag = true;
    	        }
    	      }
    	      String portCode = boeSearchVO.getPortCode();
    	      if (!commonMethods.isNull(portCode))
    	      {
    	        BOE_QUERY = BOE_QUERY + "AND BOE_PORT_OF_DISCHARGE = ?";
    	        portCodeFlag = true;
    	      }
    	      if (!commonMethods.isNull(loginedUserId))
    	      {
    	        BOE_QUERY = BOE_QUERY + "AND MAKER_USERID !=?";
    	        loginedUserIdFlag = true;
    	      }
    	      BOE_QUERY = BOE_QUERY + " ORDER BY BOE_NUMBER ";

    	 
    	 
    	      loggableStatement = new LoggableStatement(con, BOE_QUERY);
    	      if (boeNoFlag) {
    	        loggableStatement.setString(++setValue, boeNo.trim());
    	      }
    	      if (dateFromFlag) {
    	        loggableStatement.setString(++setValue, dateFrom.trim());
    	      }
    	      if (dateToFlag) {
    	        loggableStatement.setString(++setValue, dateTo.trim());
    	      }
    	      if (portCodeFlag) {
    	        loggableStatement.setString(++setValue, portCode.trim());
    	      }
    	      if (loginedUserIdFlag) {
    	        loggableStatement.setString(++setValue, loginedUserId);
    	      }
    	      rs = loggableStatement.executeQuery();
    	      while (rs.next())
    	      {
    	        boeVO = new BoeVO();
    	        boeVO.setBoeNo(rs.getString("BOE_NUMBER"));
    	        boeVO.setBoeDate(rs.getString("BOE_DATE"));
    	        boeVO.setPortCode(rs.getString("BOE_PORT_OF_DISCHARGE"));
    	        boeVO.setAdCode(rs.getString("BOE_AD_CODE"));
    	        boeVO.setIeCode(rs.getString("BOE_IE_CODE"));
    	        boeVO.setIename(rs.getString("BOE_IE_NAME"));
    	        String recInd = rs.getString("BOE_RECORD_INDICATOR");
    	        if ((recInd != null) && (recInd.equalsIgnoreCase("1"))) {
    	          boeVO.setRecInd("New");
    	        } else if ((recInd != null) && (recInd.equalsIgnoreCase("3"))) {
    	          boeVO.setRecInd("Cancel");
    	        }
    	        boeList.add(boeVO);
    	      }
    	    }
    	    catch (SQLException e)
    	    {
    	      throwDAOException(e);
    	    }
    	    finally
    	    {
    	      closeSqlRefferance(rs, loggableStatement, con);
    	    }
    	    logger.info("Exiting Method");
    	    return boeList;
    	  }
    	  public BoeVO getBoeDetails(String boeData)
    	  {
    	    logger.info("Entering Method");
    	    Connection con = null;
    	    LoggableStatement log = null;
    	    ResultSet res = null;
    	    CommonMethods commonMethods = null;
    	    BoeVO boeVO = null;
    	    try
    	    {
    	      con = DBConnectionUtility.getConnection();
    	      commonMethods = new CommonMethods();
    	      boeVO = new BoeVO();
    	      String boeNo = null;
    	      String boeDate = null;
    	      String portCode = null;
    	      if (boeData != null)
    	      {
    	        String[] temp = boeData.split(":");
    	        boeNo = temp[0];
    	        boeDate = temp[1];
    	        portCode = temp[2];
    	      }
    	      String query = "SELECT BOE_NUMBER,TO_CHAR(BOE_DATE,'DD/MM/YYYY') AS BOE_DATE,BOE_PORT_OF_DISCHARGE,BOE_RECORD_INDICATOR AS RECORD_INDICATOR,BOE_IMPORT_AGENCY AS IMPORT_AGENCY,BOE_AD_CODE AS AD_CODE, BOE_GP AS GP,BOE_IE_CODE AS IE_CODE,BOE_IE_NAME AS IE_NAME,BOE_IE_ADDRESS AS IE_ADDRESS,BOE_IE_PANNUMBER AS PAN_NO,BOE_PORT_OF_SHIPMENT AS PORT_OF_SHIPMENT,BOE_IGMNUMBER AS IGM_NO,TO_CHAR(BOE_IGMDATE,'DD/MM/YYYY') AS IGM_DATE,BOE_HAWB_HBLNUMBER AS HBL_NO,TO_CHAR(BOE_HAWB_HBLDATE,'DD/MM/YYYY') AS HBL_DATE, BOE_MAWB_MBLNUMBER AS MBL_NO,TO_CHAR(BOE_MAWB_MBLDATE,'DD/MM/YYYY') AS MBL_DATE  FROM ETT_BOE_DETAILS WHERE BOE_NUMBER = ?  AND TO_CHAR(BOE_DATE,'DD/MM/YYYY') = ? AND BOE_PORT_OF_DISCHARGE = ? ";
    	      log = new LoggableStatement(con, query);
    	      log.setString(1, commonMethods.getEmptyIfNull(boeNo).trim());
    	      log.setString(2, commonMethods.getEmptyIfNull(boeDate).trim());
    	      log.setString(3, commonMethods.getEmptyIfNull(portCode).trim());
    	      res = log.executeQuery();
    	      if (res.next())
    	      {
    	        boeVO.setBoeNo(res.getString("BOE_NUMBER"));
    	        boeVO.setBoeDate(res.getString("BOE_DATE"));
    	        boeVO.setPortCode(res.getString("BOE_PORT_OF_DISCHARGE"));
    	        boeVO.setAdCode(res.getString("AD_CODE"));
    	        boeVO.setIeCode(res.getString("IE_CODE"));
    	        boeVO.setIename(res.getString("IE_NAME"));
    	        boeVO.setIeadd(res.getString("IE_ADDRESS"));
    	        boeVO.setIepan(res.getString("PAN_NO"));
    	        boeVO.setPordisc(res.getString("PORT_OF_SHIPMENT"));
    	        boeVO.setIgmNo(res.getString("IGM_NO"));
    	        boeVO.setIgmDate(res.getString("IGM_DATE"));
    	        boeVO.setMblNo(res.getString("MBL_NO"));
    	        boeVO.setMblDate(res.getString("MBL_DATE"));
    	        boeVO.setHblNo(res.getString("HBL_NO"));
    	        boeVO.setHblDate(res.getString("HBL_DATE"));
    	        String gpType = commonMethods.getEmptyIfNull(res.getString("GP")).trim();
    	        if (gpType.equalsIgnoreCase("G")) {
    	          boeVO.setGovprv("Government");
    	        } else {
    	          boeVO.setGovprv("Private");
    	        }
    	        String recInd = commonMethods.getEmptyIfNull(res.getString("RECORD_INDICATOR")).trim();
    	        if (recInd.equalsIgnoreCase("1")) {
    	          boeVO.setRecInd("New");
    	        } else {
    	          boeVO.setRecInd("Cancel");
    	        }
    	        String imAgency = commonMethods.getEmptyIfNull(res.getString("IMPORT_AGENCY")).trim();
    	        if (imAgency.equalsIgnoreCase("1")) {
    	          boeVO.setImAgency("Customs");
    	        } else {
    	          boeVO.setImAgency("SEZ");
    	        }
    	      }
    	      boeVO = fetchInvoiceList(con, boeVO);
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
    	    return boeVO;
    	  }
    	  private BoeVO fetchInvoiceList(Connection con, BoeVO boeVO)
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
    	      commonMethods = new CommonMethods();
    	      String query = "SELECT INV.BOE_NUMBER,INV.BOE_DATE,INV.BOE_PORT_OF_DISCHARGE,INV.INVOICE_SERIAL_NUMBER AS INV_SERIAL_NO,INV.INVOICE_NUMBER AS INV_NO,INV.INVOICE_TERMS_OF_INVOICE AS INV_TERMS,INV.INVOICE_FOBAMOUNT AS FOB_AMT,INV.INVOICE_FOBCURRENCY AS FOB_CURR,INV.INVOICE_FRIEGHTAMOUNT AS FRI_AMT,INV.INVOICE_FRIEGHTCURRENCYCODE AS FRI_CURR,INV.INVOICE_INSURANCEAMOUNT AS INS_AMT,INV.INVOICE_INSURANCECURRENCY_CODE AS INS_CURR,INV.INVOICE_DISCOUNT_CHARGES AS DISC_CHARGES,INV.INVOICE_DISCOUNT_CURRENCY AS DISC_CURR,INV.INVOICE_AGENCY_COMMISSION AS AGENCY_COMM,INV.INVOICE_AGENCY_CURRENCY AS AGENCY_CURR,INV.INVOICE_MISCELLANEOUS_CHARGES AS MISC_CHARGES,INV.INVOICE_MISCELLANEOUS_CURRENCY AS MISC_CURR,INV.INVOICE_SUPPLIER_NAME AS SUPP_NAME,INV.INVOICE_SUPPLIER_ADDRESS AS SUPP_ADDR,INV.INVOICE_SUPPLIER_COUNTRY AS SUPP_CTY,INV.INVOICE_SELLER_NAME AS SELL_NAME,INV.INVOICE_SELLER_ADDRESS AS SELL_ADDR,INV.INVOICE_SELLER_COUNTRY AS SELL_CTY,INV.INVOICE_THIRDPARTY_NAME AS THIRD_NAME,INV.INVOICE_THIRDPARTY_ADDRESS AS THIRD_ADDR,INV.INVOICE_THIRDPARTY_COUNTRY AS THIRD_CTY  FROM ETT_INVOICE_DETAILS INV WHERE INV.BOE_NUMBER = ? AND INV.BOE_DATE = TO_DATE(?,'DD/MM/YYYY') AND INV.BOE_PORT_OF_DISCHARGE = ? ORDER BY TO_NUMBER(INV.INVOICE_SERIAL_NUMBER) ";
    	      ls = new LoggableStatement(con, query);
    	      ls.setString(1, commonMethods.getEmptyIfNull(boeVO.getBoeNo()).trim());
    	      ls.setString(2, commonMethods.getEmptyIfNull(boeVO.getBoeDate()).trim());
    	      ls.setString(3, commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
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
    	        invoiceList.add(invoiceVO);
    	      }
    	      boeVO.setBoeInvoiceList(invoiceList);
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
    	    return boeVO;
    	  }
    	  public void updateManualBOE(String[] manualchkList, String boeAuthorizeStatus, String remarks)
    	    throws DAOException
    	  {
    	    logger.info("Entering Method");
    	    Connection con = null;
    	    CommonMethods commonMethods = null;
    	    try
    	    {
    	      commonMethods = new CommonMethods();
    	      con = DBConnectionUtility.getConnection();
    	      HttpSession session = ServletActionContext.getRequest()
    	        .getSession();
    	      String loginedUserId = (String)session.getAttribute("loginedUserId");
    	      if (manualchkList != null) {
    	        for (int i = 0; i < manualchkList.length; i++)
    	        {
    	          String temp = manualchkList[i];
    	          String[] a = temp.split(":");
    	          String boeQuery = "UPDATE ETT_BOE_DETAILS SET STATUS = ?,REMARKS = ?,CHECKER_USERID = ?,CHECKER_TIMESTAMP = SYSTIMESTAMP  WHERE BOE_NUMBER = ? AND TO_CHAR(BOE_DATE,'DD/MM/YYYY') = ? AND BOE_PORT_OF_DISCHARGE = ? ";

    	 
    	          LoggableStatement pst = new LoggableStatement(con, boeQuery);
    	          pst.setString(1, boeAuthorizeStatus);
    	          pst.setString(2, remarks);
    	          pst.setString(3, loginedUserId);
    	          pst.setString(4, commonMethods.getEmptyIfNull(a[0]).trim());
    	          pst.setString(5, commonMethods.getEmptyIfNull(a[1]).trim());
    	          pst.setString(6, commonMethods.getEmptyIfNull(a[2]).trim());
    	          int iCount = pst.executeUpdate();
    	          closePreparedStatement(pst);
    	          if (iCount > 0)
    	          {
    	            String invQuery = "UPDATE ETT_INVOICE_DETAILS SET STATUS = ?  WHERE BOE_NUMBER = ? AND TO_CHAR(BOE_DATE,'DD/MM/YYYY') = ? AND BOE_PORT_OF_DISCHARGE = ? ";

    	 
    	            LoggableStatement pst1 = new LoggableStatement(con, invQuery);
    	            pst1.setString(1, boeAuthorizeStatus);
    	            pst1.setString(2, commonMethods.getEmptyIfNull(a[0]).trim());
    	            pst1.setString(3, commonMethods.getEmptyIfNull(a[1]).trim());
    	            pst1.setString(4, commonMethods.getEmptyIfNull(a[2]).trim());
    	            pst1.executeUpdate();
    	            closePreparedStatement(pst1);
    	          }
    	        }
    	      }
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
    	  }
    	  public ArrayList<BoeVO> fetchManualBOESearch(BOESearchVO boeSearchVO)
    	    throws DAOException
    	  {
    	    logger.info("Entering Method");
    	    Connection con = null;
    	    LoggableStatement loggableStatement = null;
    	    ResultSet rs = null;
    	    BoeVO boeVO = null;
    	    String sqlQuery = null;
    	    ArrayList<BoeVO> boeList = null;
    	    CommonMethods commonMethods = null;
    	    boolean boeNoFlag = false;
    	    boolean dateFromFlag = false;
    	    boolean dateToFlag = false;
    	    boolean portCodeFlag = false;
    	    boolean transStatusFlag = false;
    	    int setValue = 0;
    	    String BOE_QUERY = null;
    	    try
    	    {
    	      con = DBConnectionUtility.getConnection();
    	      commonMethods = new CommonMethods();
    	      boeList = new ArrayList();

    	 
    	 
    	 
    	 
    	      BOE_QUERY = "SELECT BOE_NUMBER,TO_CHAR(BOE_DATE,'DD/MM/YYYY') AS BOE_DATE,BOE_PORT_OF_DISCHARGE,BOE_AD_CODE,BOE_IE_CODE,BOE_IE_NAME,BOE_RECORD_INDICATOR,REMARKS,STATUS FROM ETT_BOE_DETAILS WHERE BOE_TYPE = 'M' ";
    	      String boeNo = boeSearchVO.getBoeNo();
    	      if (!commonMethods.isNull(boeNo))
    	      {
    	        BOE_QUERY = BOE_QUERY + "AND TRIM(BOE_NUMBER) LIKE '%'||?||'%'";
    	        boeNoFlag = true;
    	      }
    	      String dateFrom = boeSearchVO.getPaymentDateFrom();
    	      String dateTo = boeSearchVO.getPaymentDateTo();
    	      if ((!commonMethods.isNull(dateFrom)) && 
    	        (!commonMethods.isNull(dateTo)))
    	      {
    	        BOE_QUERY = BOE_QUERY + "AND BOE_DATE BETWEEN TO_DATE(?,'DD-MM-YY') AND TO_DATE(?,'DD-MM-YY')";
    	        dateFromFlag = true;
    	        dateToFlag = true;
    	      }
    	      else
    	      {
    	        if (!commonMethods.isNull(dateFrom))
    	        {
    	          BOE_QUERY = BOE_QUERY + " AND BOE_DATE >= TO_DATE(?,'DD-MM-YY')";
    	          dateFromFlag = true;
    	        }
    	        if (!commonMethods.isNull(dateTo))
    	        {
    	          BOE_QUERY = BOE_QUERY + " AND BOE_DATE <= TO_DATE(?,'DD-MM-YY')";
    	          dateToFlag = true;
    	        }
    	      }
    	      String portCode = boeSearchVO.getPortCode();
    	      if (!commonMethods.isNull(portCode))
    	      {
    	        BOE_QUERY = BOE_QUERY + "AND BOE_PORT_OF_DISCHARGE =?";
    	        portCodeFlag = true;
    	      }
    	      String transStatus = commonMethods.getEmptyIfNull(boeSearchVO.getStatus()).trim();
    	      if (!commonMethods.isNull(transStatus))
    	      {
    	        BOE_QUERY = BOE_QUERY + " AND STATUS = ?";
    	        transStatusFlag = true;
    	      }
    	      BOE_QUERY = BOE_QUERY + " ORDER BY BOE_NUMBER ";

    	 
    	      loggableStatement = new LoggableStatement(con, BOE_QUERY);
    	      if (boeNoFlag) {
    	        loggableStatement.setString(++setValue, boeNo.trim());
    	      }
    	      if (dateFromFlag) {
    	        loggableStatement.setString(++setValue, dateFrom.trim());
    	      }
    	      if (dateToFlag) {
    	        loggableStatement.setString(++setValue, dateTo.trim());
    	      }
    	      if (portCodeFlag) {
    	        loggableStatement.setString(++setValue, portCode.trim());
    	      }
    	      if (transStatusFlag) {
    	        loggableStatement.setString(++setValue, transStatus);
    	      }
    	      rs = loggableStatement.executeQuery();
    	      logger.info("Manual Boe Search Query------>" + loggableStatement.getQueryString());
    	      while (rs.next())
    	      {
    	        boeVO = new BoeVO();
    	        boeVO.setBoeNo(rs.getString("BOE_NUMBER"));
    	        boeVO.setBoeDate(rs.getString("BOE_DATE"));
    	        boeVO.setPortCode(rs.getString("BOE_PORT_OF_DISCHARGE"));
    	        boeVO.setAdCode(rs.getString("BOE_AD_CODE"));
    	        boeVO.setIeCode(rs.getString("BOE_IE_CODE"));
    	        boeVO.setIename(rs.getString("BOE_IE_NAME"));
    	        String recInd = rs.getString("BOE_RECORD_INDICATOR");
    	        if ((recInd != null) && (recInd.equalsIgnoreCase("1"))) {
    	          boeVO.setRecInd("New");
    	        } else if ((recInd != null) && (recInd.equalsIgnoreCase("3"))) {
    	          boeVO.setRecInd("Cancel");
    	        }
    	        boeVO.setRemarks(rs.getString("REMARKS"));
    	        String transStatuss = commonMethods.getEmptyIfNull(rs.getString("STATUS")).trim();
    	        if (transStatuss.equalsIgnoreCase("P")) {
    	          boeVO.setStatus("Pending");
    	        } else if (transStatuss.equalsIgnoreCase("A")) {
    	          boeVO.setStatus("Approved");
    	        } else if (transStatuss.equalsIgnoreCase("R")) {
    	          boeVO.setStatus("Reject");
    	        }
    	        boeList.add(boeVO);
    	      }
    	    }
    	    catch (SQLException e)
    	    {
    	      throwDAOException(e);
    	    }
    	    finally
    	    {
    	      closeSqlRefferance(rs, loggableStatement, con);
    	    }
    	    logger.info("Exiting Method");
    	    return boeList;
    	  }
    	  public BoeVO getPrintBOEData(String boeNo, String boeDate, String portCode, String paymentRef, String paymentSlNo)
    	    throws DAOException
    	  {
    	    logger.info("Entering Method");
    	    Connection con = null;
    	    LoggableStatement loggableStatement = null;
    	    ResultSet rs = null;
    	    BoeVO boeVO = null;
    	    String sqlQuery = null;
    	    try
    	    {
    	      con = DBConnectionUtility.getConnection();
    	      HttpSession session = ServletActionContext.getRequest()
    	        .getSession();
    	      String procDate = (String)session.getAttribute("processDate");
    	      sqlQuery = "SELECT NVL(TRIM(BP.BOE_PAYMENT_BP_PAY_REF), ' ') AS BOE_PAYMENT_BP_PAY_REF,TO_CHAR(TO_DATE(BOE_PAYMENT_BP_PAY_DT, 'DD-MM-YY'), 'DD-MON-YY') AS BOE_PAYMENT_BP_PAY_DT,NVL(TRIM(BP.BOE_PAYMENT_BP_BOE_NO), ' ') AS BOE_PAYMENT_BP_BOE_NO,TO_CHAR(TO_DATE(BP.BOE_PAYMENT_BP_BOE_DT, 'DD-MM-YY'), 'DD-MM-YYYY') AS BOE_PAYMENT_BP_BOE_DT,NVL(TRIM(BP.BOE_PAYMENT_BP_BOE_CCY), ' ') AS BOE_PAYMENT_BP_BOE_CCY,PORT_CODE,NVL(TRIM(to_char(BP.BOE_ENTRY_AMT)), '0') AS BOE_ENTRY_AMT,NVL(TRIM(to_char(BP.ENDORSED_BOE_AMT)), '0') AS ENDORSED_BOE_AMT,BP.IECODE AS IE_CODE,BP.BOE_PAYMENT_CIF_ID AS CIF_ID FROM ETT_BOE_PAYMENT BP  WHERE BOE_PAYMENT_BP_BOE_NO = ? AND TO_CHAR(BOE_PAYMENT_BP_BOE_DT,'DD-MM-YYYY') = ? AND PORT_CODE = ? AND BOE_PAYMENT_BP_PAY_REF = ? AND BOE_PAYMENT_BP_PAY_PART_REF=?";

    	 
    	 
    	 
    	 
    	 
    	 
    	 
    	      loggableStatement = new LoggableStatement(con, sqlQuery);
    	      loggableStatement.setString(1, boeNo);
    	      loggableStatement.setString(2, boeDate);
    	      loggableStatement.setString(3, portCode);
    	      loggableStatement.setString(4, paymentRef);
    	      loggableStatement.setString(5, paymentSlNo);
    	      rs = loggableStatement.executeQuery();
    	      if (rs.next())
    	      {
    	        boeVO = new BoeVO();
    	        boeVO.setPos(procDate);
    	        boeVO.setIeCode(rs.getString("IE_CODE"));
    	        boeVO.setPaymentRefNo(CommonMethods.checkForNullvalue(rs.getString("BOE_PAYMENT_BP_PAY_REF")).trim());
    	        boeVO.setPartPaymentSlNo(CommonMethods.checkForNullvalue(paymentSlNo).trim());
    	        boeVO.setBoeNo(rs.getString("BOE_PAYMENT_BP_BOE_NO"));
    	        boeVO.setBoeDate(rs.getString("BOE_PAYMENT_BP_BOE_DT"));
    	        boeVO.setPortCode(rs.getString("PORT_CODE"));
    	        boeVO.setBoeCurr(rs.getString("BOE_PAYMENT_BP_BOE_CCY"));
    	        boeVO.setBoeAmt(CommonMethods.setCheckValue(rs.getString("BOE_ENTRY_AMT")));
    	        boeVO.setEndorseAmt(CommonMethods.setCheckValue(rs.getString("ENDORSED_BOE_AMT")));
    	        boeVO.setPaymentDate(rs.getString("BOE_PAYMENT_BP_PAY_DT"));
    	        String cifNo = CommonMethods.checkForNullvalue(rs.getString("CIF_ID")).trim();
    	        boeVO.setCifNo(cifNo);
    	        String ieQuery = "SELECT TRIM(SVNA1) AS IENAME1,TRIM(SVNA2) AS IENAME2,TRIM(SVNA3) AS IENAME3,TRIM(SVNA4) AS IENAME4,TRIM(SVNA5) AS IENAME5,EMAIL FROM SX20LF where TRIM(SXCUS1) = ? ";

    	 
    	        LoggableStatement pst = new LoggableStatement(con, ieQuery);
    	        pst.setString(1, cifNo);
    	        ResultSet rs1 = pst.executeQuery();
    	        if (rs1.next())
    	        {
    	          boeVO.setIename(CommonMethods.checkForNullvalue(rs1.getString("IENAME1")).trim());
    	          boeVO.setIeAddr1(CommonMethods.checkForNullvalue(rs1.getString("IENAME2")).trim());
    	          boeVO.setIeAddr2(CommonMethods.checkForNullvalue(rs1.getString("IENAME3")).trim());
    	          boeVO.setIeAddr3(CommonMethods.checkForNullvalue(rs1.getString("IENAME4")).trim());
    	          boeVO.setIeAddr4(CommonMethods.checkForNullvalue(rs1.getString("IENAME5")).trim());
    	          boeVO.setEmailID(CommonMethods.checkForNullvalue(rs1.getString("EMAIL")).trim());
    	        }
    	        closeSqlRefferance(rs1, pst);

    	 
    	        String gdQuery = "SELECT GOOD_DESC FROM ETTV_BOE_GOODS_DESC WHERE MASTER_REF = ? AND EVENT_REF = ?";
    	        LoggableStatement pst1 = new LoggableStatement(con, gdQuery);
    	        pst1.setString(1, boeVO.getPaymentRefNo());
    	        pst1.setString(2, boeVO.getPartPaymentSlNo());
    	        ResultSet rs2 = pst1.executeQuery();
    	        if (rs2.next()) {
    	          boeVO.setGovprv(CommonMethods.checkForNullvalue(rs2.getString("GOOD_DESC")).trim());
    	        }
    	        closeSqlRefferance(rs2, pst1);
    	      }
    	    }
    	    catch (SQLException e)
    	    {
    	      throwDAOException(e);
    	    }
    	    finally
    	    {
    	      closeSqlRefferance(rs, loggableStatement, con);
    	    }
    	    logger.info("Exiting Method");
    	    return boeVO;
    	  }
    	  public boolean sentAdviceData(BoeVO boeVO)
    	    throws DAOException
    	  {
    	    logger.info("Entering Method");
    	    Connection con = null;
    	    LoggableStatement loggableStatement = null;
    	    ResultSet result = null;
    	    CommonMethods commonMethods = null;
    	    boolean emailStaus = false;
    	    try
    	    {
    	      con = DBConnectionUtility.getConnection();
    	      commonMethods = new CommonMethods();
    	      String[] emailAddr = null;
    	      String fileName = boeVO.getPaymentRefNo() + boeVO.getPartPaymentSlNo();
    	      String emailID = boeVO.getEmailID();
    	      if (emailID != null)
    	      {
    	        emailAddr = commonMethods.getEmptyIfNull(emailID).trim().split(",");
    	        logger.info("BOE Advice Email Address-->" + emailAddr);
    	      }
    	      if (emailAddr != null)
    	      {
    	        ExtEmailNotificationClient emailObj = new ExtEmailNotificationClient();
    	        File file = new File(ActionConstants.FILE_LOCATION + fileName + ".pdf");
    	        byte[] attachData = fileToArrayOfBytes(file);
    	        String emailSubject = "Bill of Entry Acknowledgement for " + fileName;
    	        String emailBodyText = "Dear Sir/Madam,\n\nGreetings for the day.\n\nPlease find attached Bill of Entry acknowledgement letter.\nAssuring you of our best services at all times.\n\n\nThanks &  Best Regards,\nTrade Operations\n\n\nIMPORTANT: Please do not reply to this message or mail address.For any queries, please call our 24 Hrs Customer Contact Centre at our toll free number*- 1800 102 6022 or use Secure Mail by clicking on Inbox link after logging into Net Banking.(*Click here), if the toll free numbers are not supported by your service provider. ";

    	 
    	 
    	 
    	 
    	 
    	        String tempFileName = fileName + "_BOEAck.pdf";
    	        emailStaus = emailObj.sendExternalEmailNotification(boeVO.getPaymentRefNo(), 
    	          boeVO.getPartPaymentSlNo(), boeVO.getCifNo(), emailSubject, emailBodyText, tempFileName, 
    	          emailAddr, null, null, "", attachData);
    	        logger.info("BOE Advice Email Status------>" + emailStaus);
    	      }
    	    }
    	    catch (Exception exception)
    	    {
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