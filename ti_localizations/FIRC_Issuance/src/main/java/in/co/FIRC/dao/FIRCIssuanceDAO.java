package in.co.FIRC.dao;

import in.co.FIRC.dao.exception.DAOException;
import in.co.FIRC.dao.utility.DBConnectionUtility;
import in.co.FIRC.utility.CommonMethods;
import in.co.FIRC.utility.LoggableStatement;
import in.co.FIRC.vo.ourBank.CustomerDataVO;
import in.co.FIRC.vo.ourBank.IssuanceVO;
import in.co.FIRC.vo.ourBank.OurBankVO;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
 
public class FIRCIssuanceDAO
  extends DBConnectionUtility
{
  private static Logger logger = LogManager.getLogger(FIRCIssuanceDAO.class.getName());
  static FIRCIssuanceDAO dao;
  public static FIRCIssuanceDAO getDAO()
  {
    if (dao == null) {
      dao = new FIRCIssuanceDAO();
    }
    return dao;
  }
  public ArrayList<CustomerDataVO> customerSearch(ArrayList<CustomerDataVO> customerList)
  {
    logger.info("Entering Method");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    try
    {
      customerList = new ArrayList();
      con = DBConnectionUtility.getConnection();
      String query = "select GFCPNC,GFCUN from GFPF order by GFCPNC";
      pst = new LoggableStatement(con, query);
      logger.info(pst.getQueryString());
      rs = pst.executeQuery();
      while (rs.next())
      {
        CustomerDataVO cusVo = new CustomerDataVO();
        cusVo.setCifID(CommonMethods.nullAndTrimString(rs.getString(1)));
        cusVo.setBeneficiaryName(CommonMethods.nullAndTrimString(rs
          .getString(2)));
        customerList.add(cusVo);
      }
      logger.info("Executed");
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
    return customerList;
  }
  public ArrayList<CustomerDataVO> filterCusList(ArrayList<CustomerDataVO> customerList, CustomerDataVO cusDataVo)
  {
    logger.info("Entering Method");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    String cusName = "";
    String cusNumber = "";
    String setValue = null;
    String setValue1 = null;
    try
    {
      String query = "";
      customerList = new ArrayList();
      con = DBConnectionUtility.getConnection();
      String CUS_NUM_FILTER_QUERY = "select GFCPNC AS CIFID,GFCUN AS CUSTOMER from GFPF ";
      if (!CommonMethods.isNull(cusDataVo.getBeneficiaryName()))
      {
        query = CUS_NUM_FILTER_QUERY + " WHERE GFCUN like ?||'%' ";
        setValue = cusDataVo.getBeneficiaryName();
      }
      else if (!CommonMethods.isNull(cusDataVo.getCifID()))
      {
        query = CUS_NUM_FILTER_QUERY + " WHERE GFCPNC like ?||'%' ";
        setValue = cusDataVo.getCifID();
      }
      else if ((!CommonMethods.isNull(cusDataVo.getBeneficiaryName())) && 
        (!CommonMethods.isNull(cusDataVo.getCifID())))
      {
        query = 
          CUS_NUM_FILTER_QUERY + " WHERE GFCUN like  ?||'%' AND GFCPNC like '%'||?||'%' ";
        setValue = cusDataVo.getBeneficiaryName();
        setValue1 = cusDataVo.getCifID();
      }
      else
      {
        query = CUS_NUM_FILTER_QUERY;
      }
      pst = new LoggableStatement(con, query);
      if (setValue != null) {
        pst.setString(1, setValue);
      }
      if (setValue1 != null) {
        pst.setString(2, setValue1);
      }
      logger.info(pst.getQueryString());
      rs = pst.executeQuery();
      while (rs.next())
      {
        CustomerDataVO cusVo = new CustomerDataVO();
        cusVo.setCifID(CommonMethods.nullAndTrimString(rs
          .getString("CIFID")));
        cusVo.setBeneficiaryName(CommonMethods.nullAndTrimString(rs
          .getString("CUSTOMER")));
        customerList.add(cusVo);
      }
      logger.info("Executed");
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
    return customerList;
  }
  public String getIssuanceList(IssuanceVO issuanceVO)
  {
    CommonMethods commonMethods = null;
    String query = "SELECT ISSUING_BANK_NAME,ISSUING_BNK_BRANCH,FIRC_SERIAL_NO, TO_CHAR(TO_DATE(FIRC_DATE,'DD-MM-YY'),'DD-MM-YYYY') AS FIRC_DATE,TRANS_REF_NO, BENFICIARY_ADDRESS,PUR_CODE,ORDER_CUSTOMER, AMOUNT,CURRENCY,EXCHANGE_RATE,AVAILABLE_AMOUNT,PAID_AMOUNT, PAID_CURRENCY,INWARD_TYPE,STATUS,REMARKS FROM ETT_FIRC_LODGEMENT WHERE FIRC_SERIAL_NO = FIRC_SERIAL_NO ";

 
 
 
    commonMethods = new CommonMethods();
    String dateFrom = issuanceVO.getDataFrom();
    String dateTo = issuanceVO.getDataTo();
    if ((!CommonMethods.isNull(dateFrom)) && (!CommonMethods.isNull(dateTo)))
    {
      query = 
        query + " AND FIRC_DATE BETWEEN TO_DATE('" + dateFrom.trim() + "','DD-MM-YY') " + " AND TO_DATE('" + dateTo.trim() + "','DD-MM-YY')";
    }
    else
    {
      if (!CommonMethods.isNull(dateFrom)) {
        query = query + " AND FIRC_DATE >= TO_DATE('" + dateFrom.trim() + "','DD-MM-YY')";
      }
      if (!CommonMethods.isNull(dateTo)) {
        query = query + " AND FIRC_DATE <= TO_DATE('" + dateTo.trim() + "','DD-MM-YY')";
      }
    }
    String fircNo = commonMethods.getEmptyIfNull(issuanceVO.getFircno()).trim();
    if (!CommonMethods.isNull(fircNo)) {
      query = query + " AND FIRC_SERIAL_NO ='" + fircNo + "'";
    }
    String transRefNo = commonMethods.getEmptyIfNull(issuanceVO.getTransrefno()).trim();
    if (!CommonMethods.isNull(transRefNo)) {
      query = query + " AND TRANS_REF_NO ='" + transRefNo + "'";
    }
    String transStatus = commonMethods.getEmptyIfNull(issuanceVO.getStatus()).trim();
    if (!CommonMethods.isNull(transStatus)) {
      query = query + " AND STATUS ='" + transStatus + "'";
    }
    return query;
  }
  public HashMap<String, String> getStatus(String trnsacNo)
  {
    String status = "";
    logger.info("Entering Method");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    String setValue = "";
    String inwardType = "";
    HashMap<String, String> createMap = new HashMap();
    try
    {
      con = DBConnectionUtility.getConnection();
      String query = "SELECT TRIM(STATUS),TRIM(INWARD_TYPE) FROM ETT_FIRC_LODGEMENT";
      if (!CommonMethods.isNull(trnsacNo))
      {
        query = query + " WHERE TRANS_REF_NO=?";
        setValue = trnsacNo;
      }
      pst = new LoggableStatement(con, query);
      if (setValue != null) {
        pst.setString(1, setValue);
      }
      logger.info(pst.getQueryString());
      rs = pst.executeQuery();
      while (rs.next())
      {
        status = rs.getString(1);
        inwardType = rs.getString(2);
        logger.info("Value of the status==>" + status);
        logger.info("Value of the inwardType==>" + inwardType);
        createMap.put("status", status);
        createMap.put("inwardType", inwardType);
      }
      logger.info("Executed");
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
    return createMap;
  }
  public ArrayList<OurBankVO> getFircIssuance(IssuanceVO issuanceVO)
    throws DAOException
  {
    logger.info("------------getFircIssuance---------------");
    ArrayList<OurBankVO> issuanceList = null;
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    String query = null;
    OurBankVO ackVO = null;
    CommonMethods commonMethods = null;
    try
    {
      commonMethods = new CommonMethods();
      issuanceList = new ArrayList();
      con = DBConnectionUtility.getConnection();

 
 
 
      query = "SELECT ISSUING_BANK_NAME,ISSUING_BNK_BRANCH,FIRC_SERIAL_NO, TO_CHAR(TO_DATE(FIRC_DATE,'DD-MM-YY'),'DD-MM-YYYY') AS FIRC_DATE,TRANS_REF_NO, BENFICIARY_ADDRESS,PUR_CODE,ORDER_CUSTOMER,CIF_NO, AMOUNT,CURRENCY,EXCHANGE_RATE,AVAILABLE_AMOUNT,PAID_AMOUNT, PAID_CURRENCY,INWARD_TYPE,STATUS,REMARKS FROM ETT_FIRC_LODGEMENT WHERE FIRC_SERIAL_NO = FIRC_SERIAL_NO ";

 
 
 
      String dateFrom = commonMethods.getEmptyIfNull(issuanceVO.getDataFrom()).trim();
      String dateTo = commonMethods.getEmptyIfNull(issuanceVO.getDataTo()).trim();
      boolean dateFromFlag = false;
      boolean dateToFlag = false;
      boolean fircNoFlag = false;
      boolean transRefNoFlag = false;
      boolean transStatusFlag = false;
      boolean transCustomerNoFlag = false;
      int setValue = 0;
      if ((!CommonMethods.isNull(dateFrom)) && (!CommonMethods.isNull(dateTo)))
      {
        query = query + " AND FIRC_DATE BETWEEN TO_DATE( ? ,'DD-MM-YY')  AND TO_DATE( ? ,'DD-MM-YY')";
        dateFromFlag = true;
        dateToFlag = true;
      }
      else
      {
        if (!CommonMethods.isNull(dateFrom))
        {
          query = query + " AND FIRC_DATE >= TO_DATE( ? ,'DD-MM-YY')";
          dateFromFlag = true;
          dateToFlag = false;
        }
        if (!CommonMethods.isNull(dateTo))
        {
          query = query + " AND FIRC_DATE <= TO_DATE( ? ,'DD-MM-YY')";
          dateFromFlag = false;
          dateToFlag = true;
        }
      }
      String fircNo = commonMethods.getEmptyIfNull(issuanceVO.getFircno()).trim();
      if (!CommonMethods.isNull(fircNo))
      {
        query = query + " AND FIRC_SERIAL_NO = ? ";
        fircNoFlag = true;
      }
      String transRefNo = commonMethods.getEmptyIfNull(issuanceVO.getTransrefno()).trim();
      if (!CommonMethods.isNull(transRefNo))
      {
        query = query + " AND TRANS_REF_NO = ? ";
        transRefNoFlag = true;
      }
      String transStatus = commonMethods.getEmptyIfNull(issuanceVO.getStatus()).trim();
      if (!CommonMethods.isNull(transStatus))
      {
        query = query + " AND STATUS = ? ";
        transStatusFlag = true;
      }
      String customerNo = commonMethods.getEmptyIfNull(issuanceVO.getCRN()).trim();
      if (!CommonMethods.isNull(customerNo))
      {
        query = query + " AND CIF_NO = ? ";
        transCustomerNoFlag = true;
      }
      pst = new LoggableStatement(con, query);
      if ((dateFromFlag) && (dateToFlag))
      {
        pst.setString(++setValue, dateFrom.trim());
        pst.setString(++setValue, dateTo.trim());
      }
      else if ((dateFromFlag) && (!dateToFlag))
      {
        pst.setString(++setValue, dateFrom.trim());
      }
      else if ((!dateFromFlag) && (dateToFlag))
      {
        pst.setString(++setValue, dateTo.trim());
      }
      if (fircNoFlag) {
        pst.setString(++setValue, fircNo);
      }
      if (transRefNoFlag) {
        pst.setString(++setValue, transRefNo);
      }
      if (transStatusFlag) {
        pst.setString(++setValue, transStatus);
      }
      if (transCustomerNoFlag) {
        pst.setString(++setValue, customerNo);
      }
      logger.info("getFircIssuance-" + pst.getQueryString());
      rs = pst.executeQuery();
      while (rs.next())
      {
        ackVO = new OurBankVO();
        ackVO.setIssuingBank(commonMethods.getEmptyIfNull(rs.getString("ISSUING_BANK_NAME")).trim());
        ackVO.setIssunibranch(commonMethods.getEmptyIfNull(rs.getString("ISSUING_BNK_BRANCH")).trim());
        ackVO.setTransrefno(commonMethods.getEmptyIfNull(rs.getString("TRANS_REF_NO")).trim());
        ackVO.setFircno(commonMethods.getEmptyIfNull(rs.getString("FIRC_SERIAL_NO")).trim());
        ackVO.setFircdate(commonMethods.getEmptyIfNull(rs.getString("FIRC_DATE")).trim());
        ackVO.setBenificiarydetails(commonMethods.getEmptyIfNull(rs.getString("BENFICIARY_ADDRESS")).trim());
        ackVO.setPurposecode(commonMethods.getEmptyIfNull(rs.getString("PUR_CODE")).trim());
        ackVO.setOrderingcustomer(commonMethods.getEmptyIfNull(rs.getString("ORDER_CUSTOMER")).trim());
        ackVO.setCif_no(commonMethods.getEmptyIfNull(rs.getString("CIF_NO")).trim());
        ackVO.setAmount(commonMethods.getEmptyIfNull(rs.getString("AMOUNT")).trim());
        ackVO.setCurrency(commonMethods.getEmptyIfNull(rs.getString("CURRENCY")).trim());
        ackVO.setExchange_rate(commonMethods.getEmptyIfNull(rs.getString("EXCHANGE_RATE")).trim());
        ackVO.setAvailable_amt(commonMethods.getEmptyIfNull(rs.getString("AVAILABLE_AMOUNT")).trim());
        ackVO.setPaidamount(commonMethods.getEmptyIfNull(rs.getString("PAID_AMOUNT")).trim());
        ackVO.setPaidcurrency(commonMethods.getEmptyIfNull(rs.getString("PAID_CURRENCY")).trim());
        ackVO.setMode(commonMethods.getEmptyIfNull(rs.getString("INWARD_TYPE")).trim());
        ackVO.setRemarks(commonMethods.getEmptyIfNull(rs.getString("REMARKS")).trim());
        ackVO.setStatus(commonMethods.getEmptyIfNull(rs.getString("STATUS")).trim());
        issuanceList.add(ackVO);
      }
    }
    catch (Exception exception)
    {
      logger.info("------------getFircIssuance--------exception-------" + exception);
      throwDAOException(exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    logger.info("Exiting Method");
    return issuanceList;
  }
  public String getIRMEXList(IssuanceVO issuanceVO)
  {
    CommonMethods commonMethods = null;
    String query = "SELECT MASTER_REFNO,ADCODE,IECODE,EXT_IND,TO_CHAR(EXT_DATE,'DD-MM-YY') AS EXT_DATE,LETTER_NO, TO_CHAR(LETTER_DATE,'DD-MM-YY') AS LETTER_DATE,REC_IND,REASON,STATUS,ACK_STATUS  FROM ETT_IRM_EXT_TBL WHERE S_NO = S_NO ";

 
 
    commonMethods = new CommonMethods();
    String dateFrom = issuanceVO.getDataFrom();
    String dateTo = issuanceVO.getDataTo();
    if ((!CommonMethods.isNull(dateFrom)) && (!CommonMethods.isNull(dateTo)))
    {
      query = 
        query + " AND TO_DATE(TO_CHAR(CREATE_DATE,'DD-MM-YY'),'DD-MM-YY') BETWEEN TO_DATE('" + dateFrom.trim() + "','DD-MM-YY') " + " AND TO_DATE('" + dateTo.trim() + "','DD-MM-YY')";
    }
    else
    {
      if (!CommonMethods.isNull(dateFrom)) {
        query = 
          query + " AND TO_DATE(TO_CHAR(CREATE_DATE,'DD-MM-YY'),'DD-MM-YY') >= TO_DATE('" + dateFrom.trim() + "','DD-MM-YY')";
      }
      if (!CommonMethods.isNull(dateTo)) {
        query = 
          query + " AND TO_DATE(TO_CHAR(CREATE_DATE,'DD-MM-YY'),'DD-MM-YY') <= TO_DATE('" + dateTo.trim() + "','DD-MM-YY')";
      }
    }
    String transRefNo = commonMethods.getEmptyIfNull(issuanceVO.getTransrefno()).trim();
    if (!CommonMethods.isNull(transRefNo)) {
      query = query + " AND MASTER_REFNO ='" + transRefNo + "'";
    }
    String transStatus = commonMethods.getEmptyIfNull(issuanceVO.getStatus()).trim();
    if (!CommonMethods.isNull(transStatus)) {
      if (transStatus.equalsIgnoreCase("P")) {
        query = query + " AND STATUS = 'TP' ";
      } else if (transStatus.equalsIgnoreCase("A")) {
        query = query + " AND STATUS = 'A' ";
      } else if (transStatus.equalsIgnoreCase("C")) {
        query = query + " AND STATUS = 'C' ";
      } else {
        query = query + " AND STATUS IN ('RN','RC') ";
      }
    }
    return query;
  }
  public ArrayList<OurBankVO> getIrmExSearch(IssuanceVO issuanceVO)
    throws DAOException
  {
    ArrayList<OurBankVO> issuanceList = null;
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    String query = null;
    OurBankVO ackVO = null;
    CommonMethods commonMethods = null;
    try
    {
      commonMethods = new CommonMethods();
      issuanceList = new ArrayList();
      con = DBConnectionUtility.getConnection();

 
 
      query = "SELECT MASTER_REFNO,ADCODE,IECODE,EXT_IND,TO_CHAR(EXT_DATE,'DD-MM-YY') AS EXT_DATE,LETTER_NO, TO_CHAR(LETTER_DATE,'DD-MM-YY') AS LETTER_DATE,REC_IND,REASON,STATUS,ACK_STATUS  FROM ETT_IRM_EXT_TBL WHERE S_NO = S_NO ";

 
 
      commonMethods = new CommonMethods();
      boolean dateFromFlag = false;
      boolean dateToFlag = false;
      boolean transRefNoFlag = false;
      int setValue = 0;
      String dateFrom = issuanceVO.getDataFrom();
      String dateTo = issuanceVO.getDataTo();
      if ((!CommonMethods.isNull(dateFrom)) && (!CommonMethods.isNull(dateTo)))
      {
        query = query + " AND TO_DATE(TO_CHAR(CREATE_DATE,'DD-MM-YY'),'DD-MM-YY') BETWEEN TO_DATE( ?,'DD-MM-YY')  AND TO_DATE( ?,'DD-MM-YY')";

 
        dateFromFlag = true;
        dateToFlag = false;
      }
      else
      {
        if (!CommonMethods.isNull(dateFrom))
        {
          query = query + " AND TO_DATE(TO_CHAR(CREATE_DATE,'DD-MM-YY'),'DD-MM-YY') >= TO_DATE( ?,'DD-MM-YY')";
          dateFromFlag = true;
        }
        if (!CommonMethods.isNull(dateTo))
        {
          query = query + " AND TO_DATE(TO_CHAR(CREATE_DATE,'DD-MM-YY'),'DD-MM-YY') <= TO_DATE( ?,'DD-MM-YY')";
          dateToFlag = false;
        }
      }
      String transRefNo = commonMethods.getEmptyIfNull(issuanceVO.getTransrefno()).trim();
      if (!CommonMethods.isNull(transRefNo))
      {
        query = query + " AND MASTER_REFNO = ?";
        transRefNoFlag = true;
      }
      String transStatus = commonMethods.getEmptyIfNull(issuanceVO.getStatus()).trim();
      if (!CommonMethods.isNull(transStatus)) {
        if (transStatus.equalsIgnoreCase("P")) {
          query = query + " AND STATUS = 'TP' ";
        } else if (transStatus.equalsIgnoreCase("A")) {
          query = query + " AND STATUS = 'A' ";
        } else if (transStatus.equalsIgnoreCase("C")) {
          query = query + " AND STATUS = 'C' ";
        } else {
          query = query + " AND STATUS IN ('RN','RC') ";
        }
      }
      pst = new LoggableStatement(con, query);
      if ((dateFromFlag) && (dateToFlag))
      {
        pst.setString(++setValue, dateFrom.trim());
        pst.setString(++setValue, dateTo.trim());
      }
      else if ((dateFromFlag) && (!dateToFlag))
      {
        pst.setString(++setValue, dateFrom.trim());
      }
      else if ((!dateFromFlag) && (dateToFlag))
      {
        pst.setString(++setValue, dateTo.trim());
      }
      if (transRefNoFlag) {
        pst.setString(++setValue, transRefNo);
      }
      logger.info("----------------------getIrmExSearch-------------query---------------" + pst.getQueryString());
      rs = pst.executeQuery();
      while (rs.next())
      {
        ackVO = new OurBankVO();
        ackVO.setTransrefno(commonMethods.getEmptyIfNull(rs.getString("MASTER_REFNO")).trim());
        ackVO.setAdCode(commonMethods.getEmptyIfNull(rs.getString("ADCODE")).trim());
        ackVO.setIeCode(commonMethods.getEmptyIfNull(rs.getString("IECODE")).trim());
        String extInd = commonMethods.getEmptyIfNull(rs.getString("EXT_IND")).trim();
        if ((extInd != null) && (extInd.equalsIgnoreCase("1"))) {
          ackVO.setExtInd("RBI");
        } else if ((extInd != null) && (extInd.equalsIgnoreCase("2"))) {
          ackVO.setExtInd("AD Bank");
        } else {
          ackVO.setExtInd("Other");
        }
        ackVO.setExtDate(commonMethods.getEmptyIfNull(rs.getString("EXT_DATE")).trim());
        ackVO.setLetterNo(commonMethods.getEmptyIfNull(rs.getString("LETTER_NO")).trim());
        ackVO.setLetterDate(commonMethods.getEmptyIfNull(rs.getString("LETTER_DATE")).trim());
        String recInd = commonMethods.getEmptyIfNull(rs.getString("REC_IND")).trim();
        if ((recInd != null) && (recInd.equalsIgnoreCase("1"))) {
          ackVO.setRecInd("New");
        } else {
          ackVO.setRecInd("Cancel");
        }
        ackVO.setReason(commonMethods.getEmptyIfNull(rs.getString("REASON")).trim());
        transStatus = commonMethods.getEmptyIfNull(rs.getString("STATUS")).trim();
        if (transStatus.equalsIgnoreCase("TP")) {
          ackVO.setStatus("Pending");
        } else if (transStatus.equalsIgnoreCase("A")) {
          ackVO.setStatus("Approved");
        } else if (transStatus.equalsIgnoreCase("C")) {
          ackVO.setStatus("Cancelled");
        } else if (transStatus.equalsIgnoreCase("RN")) {
          ackVO.setStatus("Reject New");
        } else if (transStatus.equalsIgnoreCase("RC")) {
          ackVO.setStatus("Reject Cancel");
        }
        String errorCode = commonMethods.getEmptyIfNull(rs.getString("ACK_STATUS")).trim();
        if (!errorCode.equalsIgnoreCase("SUCCESS")) {
          errorCode = getErrorDescBasedOnErrorCode(errorCode, con);
        }
        ackVO.setAckStatus(errorCode);
        issuanceList.add(ackVO);
      }
    }
    catch (Exception exception)
    {
      logger.info("----------------------getIrmExSearch------------------count----------" + exception);
      throwDAOException(exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    logger.info("Exiting Method");

 
 
    return issuanceList;
  }
  public String getIRMClList(IssuanceVO issuanceVO)
  {
    CommonMethods commonMethods = null;
    String query = "SELECT S_NO,MASTER_REFNO,ADCODE,IECODE,CURRENCY,AMOUNT,APPROVED_BY,LETTER_NO,TO_CHAR(ADJ_DATE,'DD-MM-YY') AS ADJ_DATE, REASON,DOC_NO,TO_CHAR(DOC_DATE,'DD-MM-YY') AS DOC_DATE,DOC_PORT,REC_IND,REMARKS,STATUS,ACK_STATUS  FROM ETT_IRM_CLOSURE_TBL WHERE S_NO = S_NO ";

 
 
    commonMethods = new CommonMethods();
    String dateFrom = issuanceVO.getDataFrom();
    String dateTo = issuanceVO.getDataTo();
    if ((!CommonMethods.isNull(dateFrom)) && (!CommonMethods.isNull(dateTo)))
    {
      query = 
        query + " AND TO_DATE(TO_CHAR(CREATE_DATE,'DD-MM-YY'),'DD-MM-YY') BETWEEN TO_DATE('" + dateFrom.trim() + "','DD-MM-YY') " + " AND TO_DATE('" + dateTo.trim() + "','DD-MM-YY')";
    }
    else
    {
      if (!CommonMethods.isNull(dateFrom)) {
        query = 
          query + " AND TO_DATE(TO_CHAR(CREATE_DATE,'DD-MM-YY'),'DD-MM-YY') >= TO_DATE('" + dateFrom.trim() + "','DD-MM-YY')";
      }
      if (!CommonMethods.isNull(dateTo)) {
        query = 
          query + " AND TO_DATE(TO_CHAR(CREATE_DATE,'DD-MM-YY'),'DD-MM-YY') <= TO_DATE('" + dateTo.trim() + "','DD-MM-YY')";
      }
    }
    String transRefNo = commonMethods.getEmptyIfNull(issuanceVO.getTransrefno()).trim();
    if (!CommonMethods.isNull(transRefNo)) {
      query = query + " AND MASTER_REFNO ='" + transRefNo + "'";
    }
    String transStatus = commonMethods.getEmptyIfNull(issuanceVO.getStatus()).trim();
    if (!CommonMethods.isNull(transStatus)) {
      if (transStatus.equalsIgnoreCase("P")) {
        query = query + " AND STATUS = 'TP' ";
      } else if (transStatus.equalsIgnoreCase("A")) {
        query = query + " AND STATUS = 'A' ";
      } else if (transStatus.equalsIgnoreCase("C")) {
        query = query + " AND STATUS = 'C' ";
      } else {
        query = query + " AND STATUS IN ('RN','RC') ";
      }
    }
    return query;
  }
  public ArrayList<OurBankVO> getIrmClSearch(IssuanceVO issuanceVO)
    throws DAOException
  {
    logger.info("-------------getIrmClSearch-------------");
    ArrayList<OurBankVO> issuanceList = null;
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    String query = null;
    OurBankVO ackVO = null;
    CommonMethods commonMethods = null;
    try
    {
      commonMethods = new CommonMethods();
      issuanceList = new ArrayList();
      con = DBConnectionUtility.getConnection();

 
 
      boolean dateFromFlag = false;
      boolean dateToFlag = false;
      boolean transRefNoFlag = false;
      int setValue = 0;

 
      query = "SELECT S_NO,MASTER_REFNO,ADCODE,IECODE,CURRENCY,AMOUNT,APPROVED_BY,LETTER_NO,TO_CHAR(ADJ_DATE,'DD-MM-YY') AS ADJ_DATE, REASON,DOC_NO,TO_CHAR(DOC_DATE,'DD-MM-YY') AS DOC_DATE,DOC_PORT,REC_IND,REMARKS,STATUS,ACK_STATUS  FROM ETT_IRM_CLOSURE_TBL WHERE S_NO = S_NO ";

 
 
      commonMethods = new CommonMethods();
      String dateFrom = issuanceVO.getDataFrom();
      String dateTo = issuanceVO.getDataTo();
      if ((!CommonMethods.isNull(dateFrom)) && (!CommonMethods.isNull(dateTo)))
      {
        query = query + " AND TO_DATE(TO_CHAR(CREATE_DATE,'DD-MM-YY'),'DD-MM-YY') BETWEEN TO_DATE(?,'DD-MM-YY')  AND TO_DATE(?,'DD-MM-YY')";
        dateFromFlag = true;
        dateToFlag = true;
      }
      else
      {
        if (!CommonMethods.isNull(dateFrom))
        {
          query = query + " AND TO_DATE(TO_CHAR(CREATE_DATE,'DD-MM-YY'),'DD-MM-YY') >= TO_DATE(?,'DD-MM-YY')";
          dateFromFlag = true;
        }
        if (!CommonMethods.isNull(dateTo))
        {
          query = query + " AND TO_DATE(TO_CHAR(CREATE_DATE,'DD-MM-YY'),'DD-MM-YY') <= TO_DATE(?,'DD-MM-YY')";
          dateToFlag = true;
        }
      }
      String transRefNo = commonMethods.getEmptyIfNull(issuanceVO.getTransrefno()).trim();
      if (!CommonMethods.isNull(transRefNo))
      {
        query = query + " AND MASTER_REFNO = ? ";
        transRefNoFlag = true;
      }
      String transStatus = commonMethods.getEmptyIfNull(issuanceVO.getStatus()).trim();
      if (!CommonMethods.isNull(transStatus)) {
        if (transStatus.equalsIgnoreCase("P")) {
          query = query + " AND STATUS = 'TP' ";
        } else if (transStatus.equalsIgnoreCase("A")) {
          query = query + " AND STATUS = 'A' ";
        } else if (transStatus.equalsIgnoreCase("C")) {
          query = query + " AND STATUS = 'C' ";
        } else {
          query = query + " AND STATUS IN ('RN','RC') ";
        }
      }
      pst = new LoggableStatement(con, query);
      if ((dateFromFlag) && (dateToFlag))
      {
        pst.setString(++setValue, dateFrom.trim());
        pst.setString(++setValue, dateTo.trim());
      }
      else if ((dateFromFlag) && (!dateToFlag))
      {
        pst.setString(++setValue, dateFrom.trim());
      }
      else if ((!dateFromFlag) && (dateToFlag))
      {
        pst.setString(++setValue, dateTo.trim());
      }
      if (transRefNoFlag) {
          pst.setString(++setValue, transRefNo);
        }
        logger.info("-------------getIrmClSearch-------------" + pst.getQueryString());
        rs = pst.executeQuery();
        while (rs.next())
        {
          ackVO = new OurBankVO();
          ackVO.setTransrefno(commonMethods.getEmptyIfNull(rs.getString("MASTER_REFNO")).trim());
          ackVO.setAdCode(commonMethods.getEmptyIfNull(rs.getString("ADCODE")).trim());
          ackVO.setIeCode(commonMethods.getEmptyIfNull(rs.getString("IECODE")).trim());
          ackVO.setCurrency(commonMethods.getEmptyIfNull(rs.getString("CURRENCY")).trim());
          ackVO.setAmount(commonMethods.getEmptyIfNull(rs.getString("AMOUNT")).trim());
          String appBy = commonMethods.getEmptyIfNull(rs.getString("APPROVED_BY")).trim();
          if ((appBy != null) && (appBy.equalsIgnoreCase("1"))) {
            ackVO.setExtInd("RBI");
          } else if ((appBy != null) && (appBy.equalsIgnoreCase("2"))) {
            ackVO.setExtInd("AD Bank");
          } else {
            ackVO.setExtInd("Other");
          }
          ackVO.setLetterNo(commonMethods.getEmptyIfNull(rs.getString("LETTER_NO")).trim());
          ackVO.setAdjDate(commonMethods.getEmptyIfNull(rs.getString("ADJ_DATE")).trim());
          String reason = commonMethods.getEmptyIfNull(rs.getString("REASON")).trim();
          if ((reason != null) && (reason.equalsIgnoreCase("1"))) {
            ackVO.setReason("Refund of full export proceeds");
          } else if ((reason != null) && (reason.equalsIgnoreCase("2"))) {
            ackVO.setReason("Export Document");
          } else if ((reason != null) && (reason.equalsIgnoreCase("3"))) {
            ackVO.setReason("Refund of untilised export proceeds due to quality issue/sort shipment");
          } else if ((reason != null) && (reason.equalsIgnoreCase("4"))) {
            ackVO.setReason("Export proceeds confiscated due to some reasons");
          } else {
            ackVO.setReason("Others");
          }
          ackVO.setDocNo(commonMethods.getEmptyIfNull(rs.getString("DOC_NO")).trim());
          ackVO.setDocDate(commonMethods.getEmptyIfNull(rs.getString("DOC_DATE")).trim());
          ackVO.setDocPort(commonMethods.getEmptyIfNull(rs.getString("DOC_PORT")).trim());
          String recInd = commonMethods.getEmptyIfNull(rs.getString("REC_IND")).trim();
          if ((recInd != null) && (recInd.equalsIgnoreCase("1"))) {
            ackVO.setRecInd("New");
          } else {
            ackVO.setRecInd("Cancel");
          }
          ackVO.setClosureSeqNo(commonMethods.getEmptyIfNull(rs.getString("S_NO")).trim());
          ackVO.setRemarks(commonMethods.getEmptyIfNull(rs.getString("REMARKS")).trim());
          transStatus = commonMethods.getEmptyIfNull(rs.getString("STATUS")).trim();
          if (transStatus.equalsIgnoreCase("TP")) {
            ackVO.setStatus("Pending");
          } else if (transStatus.equalsIgnoreCase("A")) {
            ackVO.setStatus("Approved");
          } else if (transStatus.equalsIgnoreCase("C")) {
            ackVO.setStatus("Cancelled");
          } else if (transStatus.equalsIgnoreCase("RN")) {
            ackVO.setStatus("Reject New");
          } else if (transStatus.equalsIgnoreCase("RC")) {
            ackVO.setStatus("Reject Cancel");
          }
          String errorCode = commonMethods.getEmptyIfNull(rs.getString("ACK_STATUS")).trim();
          if (!errorCode.equalsIgnoreCase("SUCCESS")) {
            errorCode = getErrorDescBasedOnErrorCode(errorCode, con);
          }
          ackVO.setAckStatus(errorCode);
          issuanceList.add(ackVO);
        }
      }
      catch (Exception exception)
      {
        logger.info("-------------getIrmClSearch---------exception----" + exception);
        throwDAOException(exception);
      }
      finally
      {
        DBConnectionUtility.surrenderDB(con, pst, rs);
      }
      logger.info("Exiting Method");
      return issuanceList;
    }
    public String getIRMList(IssuanceVO issuanceVO)
    {
      CommonMethods commonMethods = null;
      String query = "SELECT TRIM(MAS_REF) AS MAS_REF, TRIM(CIF_NO) AS CIF_NO, (TRIM(CREDIT_AMOUNT) /POWER(10,C8.C8CED)) AS AMOUNT, TRIM(CURRENCY) AS CURRENCY FROM ETTV_INWARD_REMITTANCE_AMOUNT INWREM,C8PF C8 WHERE MAS_REF = MAS_REF  AND INWREM.CREDIT_CURRENCY    = C8.C8CCY ";

   
   
   
   
      commonMethods = new CommonMethods();
      String transRefNo = commonMethods.getEmptyIfNull(issuanceVO.getTransrefno()).trim();
      if (!CommonMethods.isNull(transRefNo)) {
        query = query + " AND MAS_REF LIKE '%" + transRefNo + "%'";
      }
      String cifNo = commonMethods.getEmptyIfNull(issuanceVO.getCifNo()).trim();
      if (!CommonMethods.isNull(cifNo))
      {
        query = query + " AND MAS_REF like '%XAR%' AND CIF_NO ='" + cifNo + "'";
        logger.info("query==================>" + query);
      }
      return query;
    }
    public ArrayList<OurBankVO> getIrmSearch(IssuanceVO issuanceVO)
      throws DAOException
    {
      logger.info("----------------getIrmSearch---------------------");
      ArrayList<OurBankVO> issuanceList = null;
      LoggableStatement pst = null;
      ResultSet rs = null;
      Connection con = null;
      String query = null;
      OurBankVO ackVO = null;
      CommonMethods commonMethods = null;
      try
      {
        commonMethods = new CommonMethods();
        issuanceList = new ArrayList();
        con = DBConnectionUtility.getConnection();

   
   
        query = "SELECT TRIM(MAS_REF) AS MAS_REF, TRIM(CIF_NO) AS CIF_NO, (TRIM(CREDIT_AMOUNT) /POWER(10,C8.C8CED)) AS AMOUNT, TRIM(CURRENCY) AS CURRENCY FROM ETTV_INWARD_REMITTANCE_AMOUNT INWREM,C8PF C8 WHERE MAS_REF = MAS_REF  AND INWREM.CREDIT_CURRENCY    = C8.C8CCY ";

   
   
   
   
        commonMethods = new CommonMethods();
        logger.info("111111111111111111111111111111111111");
        String transRefNo = null;
        String cifNo = null;
        try
        {
          transRefNo = commonMethods.getEmptyIfNull(issuanceVO.getTransrefno()).trim();
          logger.info("transRefNo------------------------------" + transRefNo);
          cifNo = commonMethods.getEmptyIfNull(issuanceVO.getCifNo()).trim();
          logger.info("cifNo------------------------------" + cifNo);
        }
        catch (Exception e)
        {
          logger.info("cifNo-----------------------exception-------" + e);
        }
        if ((!CommonMethods.isNull(transRefNo)) && (!CommonMethods.isNull(cifNo)))
        {
          query = query + " AND MAS_REF LIKE ? AND  CIF_NO =?";
          pst = new LoggableStatement(con, query);
          pst.setString(1, "%" + transRefNo + "%");
          pst.setString(2, cifNo);
        }
        else if (!CommonMethods.isNull(transRefNo))
        {
          query = query + " AND MAS_REF LIKE ?";
          pst = new LoggableStatement(con, query);
          pst.setString(1, "%" + transRefNo + "%");
        }
        else if (!CommonMethods.isNull(cifNo))
        {
          query = query + " AND MAS_REF like '%XAR%' AND CIF_NO =?";
          pst = new LoggableStatement(con, query);
          pst.setString(1, cifNo);
        }
        logger.info("1111111111222222222222222222222222222");

   
        logger.info("query==================>" + pst.getQueryString());

   
        rs = pst.executeQuery();
        while (rs.next())
        {
          ackVO = new OurBankVO();
          ackVO.setTransrefno(commonMethods.getEmptyIfNull(rs.getString("MAS_REF")).trim());
          ackVO.setCif_no(commonMethods.getEmptyIfNull(rs.getString("CIF_NO")).trim());
          ackVO.setAmount(commonMethods.getEmptyIfNull(rs.getString("AMOUNT")).trim());
          ackVO.setCurrency(commonMethods.getEmptyIfNull(rs.getString("CURRENCY")).trim());

   
   
          double irmAmount = commonMethods.toDouble(rs.getString("AMOUNT"));
          double utilAmt = 0.0D;
          double closureAmt = 0.0D;
          String queryVal = "SELECT TRIM(MAS.MASTER_REF),TRIM(EXA.INWARD) from master MAS, BASEEVENT BAS, EXTEVENTADV EXA WHERE MAS.KEY97 = BAS.MASTER_KEY AND BAS.EXTFIELD = EXA.FK_EVENT AND trim(EXA.INWARD) = ?";
          LoggableStatement pstt = new LoggableStatement(con, queryVal);
          pstt.setString(1, ackVO.getTransrefno());
          logger.info("Getting master ref number using Inward remittance===>" + queryVal);
          ResultSet rss = pstt.executeQuery();
          String refValue = "";
          while (rss.next()) {
            refValue = rss.getString(1);
          }
          closePreparedStmtResultSet(pstt, rss);
          logger.info("Getting master ref number===>" + refValue);

   
          String utilQuery = "SELECT NVL(SUM(EXA.AMTUTIL/POWER(10,C8.C8CED)),0) AS UTIL_AMT FROM MASTER MAS,BASEEVENT BAS,EXTEVENTADV EXA,C8PF C8 WHERE MAS.KEY97  = BAS.MASTER_KEY AND BAS.EXTFIELD = EXA.FK_EVENT AND BAS.STATUS  IN ('i','c') AND EXA.CCY_1    = C8.C8CCY AND EXA.INWARD   = ? AND MAS.MASTER_REF !=? ";

   
   
   
          LoggableStatement pst1 = new LoggableStatement(con, utilQuery);
          pst1.setString(1, ackVO.getTransrefno());
          pst1.setString(2, refValue);
          logger.info("Inward remittance utilized amount checking===>" + pst1.getQueryString());
          ResultSet rs1 = pst1.executeQuery();
          if (rs1.next()) {
            utilAmt = commonMethods.toDouble(rs1.getString("UTIL_AMT"));
          }
          logger.info("111111111122222222222233333333333332222");
          closePreparedStmtResultSet(pst1, rs1);
          String closureQuery = "SELECT AMOUNT AS CLOSURE_AMT FROM ETT_IRM_CLOSURE_TBL WHERE MASTER_REFNO = ? AND STATUS IN ('TP','A') ";

   
          LoggableStatement pst2 = new LoggableStatement(con, closureQuery);
          pst2.setString(1, ackVO.getTransrefno());
          logger.info("Inward ETT_IRM_CLOSURE_TBL utilized amount checking===>" + pst2.getQueryString());
          ResultSet rs2 = pst2.executeQuery();
          if (rs2.next())
          {
            closureAmt = commonMethods.toDouble(rs2.getString("CLOSURE_AMT"));
            logger.info("Inward ETT_IRM_CLOSURE_TBL closureAmt---------===>" + closureAmt);
          }
          closePreparedStmtResultSet(pst2, rs2);
          double totalUtilAmt = utilAmt + closureAmt;
          totalUtilAmt = Math.round(totalUtilAmt * 100.0D) / 100.0D;
          double outAmt = irmAmount - totalUtilAmt;
          if (outAmt < 0.0D) {
            outAmt = 0.0D;
          }
          BigDecimal irmOutAmt = BigDecimal.valueOf(outAmt).setScale(2, RoundingMode.HALF_UP);
          ackVO.setAvailable_amt(String.valueOf(irmOutAmt));
          double dataList = commonMethods.toDouble(irmOutAmt);
          if (dataList != 0.0D) {
            issuanceList.add(ackVO);
          }
        }
      }
      catch (Exception exception)
      {
        logger.info("-----------getIrmSearch-------------------exception" + exception);
        throwDAOException(exception);
      }
      finally
      {
        DBConnectionUtility.surrenderDB(con, pst, rs);
      }
      logger.info("Exiting Method");
      return issuanceList;
    }
    public ArrayList<OurBankVO> fecthIFSCSearch(IssuanceVO issuanceVO)
      throws DAOException
    {
      logger.info("----------------------fecthIFSCSearch-------------------");
      LoggableStatement pst = null;
      ResultSet rs = null;
      Connection con = null;
      ArrayList<OurBankVO> ifscList = null;
      OurBankVO ifscVO = null;
      CommonMethods commonMethods = null;
      try
      {
        con = DBConnectionUtility.getConnection();
        commonMethods = new CommonMethods();
        ifscList = new ArrayList();
        if (con != null) {
          logger.info("Connection Available");
        }
        String query = "SELECT TRIM(TIIFSC) AS TIIFSC,TRIM(REALIFSC) AS REALIFSC FROM ETTIFSCMAP   WHERE TIIFSC = TIIFSC ";
        if ((!CommonMethods.isNull(issuanceVO.getIfscCode())) && (!CommonMethods.isNull(issuanceVO.getSwiftBIC())))
        {
          int Realifsc = issuanceVO.getIfscCode().length();
          int TIifsc = issuanceVO.getSwiftBIC().length();
          logger.info("realifsc length" + Realifsc);
          logger.info("TIifsc length" + TIifsc);
          if ((Realifsc >= 4) && (TIifsc >= 4))
          {
            query = query + " AND TIIFSC LIKE ? AND REALIFSC LIKE ? and ROWNUM<51";
            pst = new LoggableStatement(con, query);
            pst.setString(1, issuanceVO.getSwiftBIC().toUpperCase() + "%");
            pst.setString(2, issuanceVO.getIfscCode().toUpperCase() + "%");
          }
        }
        else if (!CommonMethods.isNull(issuanceVO.getIfscCode()))
        {
          int Realifsc = issuanceVO.getIfscCode().length();
          logger.info("realifsc length" + Realifsc);
          if (Realifsc >= 4)
          {
            query = query + " AND REALIFSC  LIKE ? and  ROWNUM<51";
            pst = new LoggableStatement(con, query);
            pst.setString(1, issuanceVO.getIfscCode().toUpperCase() + "%");
          }
        }
        else if (!CommonMethods.isNull(issuanceVO.getSwiftBIC()))
        {
          int TIifsc = issuanceVO.getSwiftBIC().length();
          logger.info("TIifsc length" + TIifsc);
          if (TIifsc >= 4)
          {
            query = query + "   AND TIIFSC LIKE ? and ROWNUM<51";
            pst = new LoggableStatement(con, query);
            pst.setString(1, issuanceVO.getSwiftBIC().toUpperCase() + "%");
          }
        }
        else
        {
          logger.info("Final else");
        }
        logger.info("ifsC Search Query-f--------------" + pst.getQueryString());
        rs = pst.executeQuery();
        while (rs.next())
        {
          ifscVO = new OurBankVO();
          ifscVO.setIfscCode(rs.getString("TIIFSC"));
          logger.info("ifsC Search Query-f-----TIIFSC-----value----" + rs.getString("TIIFSC"));
          ifscVO.setSwiftBIC(rs.getString("REALIFSC"));
          logger.info("ifsC Search Query-f-----REALIFSC --value-------" + rs.getString("REALIFSC"));
          ifscList.add(ifscVO);
        }
      }
      catch (Exception exception)
      {
        logger.info("-------------fecthIFSCSearch--------exception" + exception);
        throwDAOException(exception);
      }
      finally
      {
        DBConnectionUtility.surrenderDB(con, pst, rs);
      }
      logger.info("Exiting Method");
      return ifscList;
    }
    public String getErrorDescBasedOnErrorCode(String errorCode, Connection con)
    {
      LoggableStatement pst1 = null;
      ResultSet rs1 = null;
      String errorDesc = "";
      String errorDesc1 = "";
      try
      {
        String sInParam = "";
        if (errorCode.contains("SUCCESS"))
        {
          sInParam = "''";
          errorDesc1 = errorCode;
        }
        else if (errorCode.contains(","))
        {
          String[] splitErrorCodes = errorCode.split(",");
          sInParam = "";
          for (int j = 0; j < splitErrorCodes.length; j++) {
            if (sInParam.equals("")) {
              sInParam = "'" + splitErrorCodes[j].trim() + "'";
            } else {
              sInParam = sInParam + ",'" + splitErrorCodes[j].trim() + "'";
            }
          }
        }
        else
        {
          sInParam = "'" + errorCode + "'";
        }
        String sEerrDescQuery = "SELECT ERRCO||' - '||ERRMSG FROM (Select Trim(ERROR_CODE) ERRCO, Trim(ERROR_MSG) ERRMSG From ETT_EDPMS_ERROR_CODES where trim(ERROR_CODE) in (" + 
          sInParam + "))";
        pst1 = new LoggableStatement(con, sEerrDescQuery);
        rs1 = pst1.executeQuery();
        while (rs1.next())
        {
          errorDesc = rs1.getString(1);
          if (errorDesc1.isEmpty()) {
            errorDesc1 = errorDesc;
          } else {
            errorDesc1 = errorDesc1 + ", " + errorDesc;
          }
        }
        errorCode = errorDesc1;
      }
      catch (Exception e)
      {
        logger.info("Exception in getErrorDescBasedOnErrorCode - " + e.getMessage());
        e.printStackTrace();
      }
      finally
      {
        DBConnectionUtility.surrenderDB(null, pst1, rs1);
      }
      return errorCode;
    }
  }
