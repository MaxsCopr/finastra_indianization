package in.co.chargeSchedule.dao;

import com.opensymphony.xwork2.ActionContext;
import in.co.chargeSchedule.action.Checkking_Valid_User;
import in.co.chargeSchedule.dao.exception.DAOException;
import in.co.chargeSchedule.utility.ActionConstantsQuery;
import in.co.chargeSchedule.utility.CommonMethods;
import in.co.chargeSchedule.utility.DBConnectionUtility;
import in.co.chargeSchedule.utility.LoggableStatement;
import in.co.chargeSchedule.vo.AlertMessagesVO;
import in.co.chargeSchedule.vo.ChargeScheduleVO;
import in.co.chargeSchedule.vo.ChargeSelectionVO;
import in.co.chargeSchedule.vo.ChargeXmlVO;
import in.co.chargeSchedule.vo.CustomerDataVO;
import in.co.chargeSchedule.vo.ProductSelectionVO;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ChargeScheduleDAO
  implements ActionConstantsQuery
{
  static ChargeScheduleDAO dao;
  private static Logger logger = LogManager.getLogger("appLog");
  private ArrayList<AlertMessagesVO> alertMsgArray = new ArrayList();
 
  public static ChargeScheduleDAO getDAO()
  {
    if (dao == null) {
      dao = new ChargeScheduleDAO();
    }
    return dao;
  }
 
  public ArrayList<ChargeScheduleVO> loadMultiPaymentReferenceData(ChargeScheduleVO chargSchVO)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement loggableStatement = null;
    ResultSet rs = null;
   
    ChargeScheduleVO chargVO = null;
    String sqlQuery = null;
    ArrayList<ChargeScheduleVO> multiPaymentReferenceList = null;
    CommonMethods commonMethods = null;
    String query = null;
    CommonMethods CM = null;
    String poNo = null;
    int setValue = 0;
    boolean poNoFlag = false;
    boolean custFlag = false;
    boolean benNameFlag = false;
    boolean amountFlag = false;
    boolean fromDateFlag = false;
    boolean todateFlag = false;
    String cust = null;
    String benName = null;
    String amount = null;
    String fromDate = null;
    String todate = null;
    try
    {
      commonMethods = new CommonMethods();
      multiPaymentReferenceList = new ArrayList();
     
      HttpSession session = ServletActionContext.getRequest().getSession();
     


      String loginedUserId = (String)session.getAttribute("loginedUserName");
     













      logger.info("loginedUserId- ---->" + loginedUserId);
      logger.info("Search Query UserId- ---->" + loginedUserId);
     

      String userID = getUserID();
     

      logger.info("Checker User ID-----------" + userID);
     
      query = "select Export_Order_Number,TO_CHAR(TO_DATE(Export_Order_date,'DD/MM/YY'),'dd/mm/YYYY') AS Export_Order_date, CIF_ID,Beneficiary_Name,Inco_Terms,Goods_Code,PO_Value,Currency,TO_CHAR(TO_DATE(Expiry_Date,'DD/MM/YY'),'dd/mm/YYYY') AS Expiry_Date, TO_CHAR(TO_DATE(Last_Shipment_Date,'DD/MM/YY'),'dd/mm/YYYY') AS Last_Shipment_Date,Percentage_Freight_Deduction, Percentage_Insurance_Deduction,Margin_percentage,Eligible_amount,importer_name from ett_export_order where STATUS='P' AND USER_ID <> ";
      logger.info("Checker Query---------------->" + query);
     
      query = query + "'" + userID + "'";
      if (chargSchVO != null)
      {
        cust = chargSchVO.getPoCif();
        benName = chargSchVO.getBeneficiaryName();
        amount = chargSchVO.getPoAmtValue();
        fromDate = chargSchVO.getFromDate();
        todate = chargSchVO.getToDate();
       
        poNo = chargSchVO.getPoNo();
        if (!CommonMethods.isNull(poNo))
        {
          query = query + " AND UPPER(EXPORT_ORDER_NUMBER) LIKE '%'||?||'%'";
          poNoFlag = true;
        }
        if (!CommonMethods.isNull(cust))
        {
          query = query + " AND UPPER(CIF_ID) LIKE '%'||?||'%'";
          custFlag = true;
        }
        if (!CommonMethods.isNull(benName))
        {
          query = query + " AND UPPER(BENEFICIARY_NAME) LIKE '%'||?||'%'";
          benNameFlag = true;
        }
        if (!CommonMethods.isNull(amount))
        {
          query = query + " AND UPPER(PO_VALUE) LIKE '%'||?||'%'";
          amountFlag = true;
        }
        if (!CommonMethods.isNull(fromDate))
        {
          query = query + " AND to_date(TO_CHAR(maker_timestamp,'DD/MM/YYYY'),'DD/MM/YYYY') >= TO_DATE(?,'DD/MM/YYYY')";
          fromDateFlag = true;
        }
        if (!CommonMethods.isNull(todate))
        {
          query = query + " AND to_date(TO_CHAR(maker_timestamp,'DD/MM/YYYY'),'DD/MM/YYYY') <= TO_DATE(?,'DD/MM/YYYY')";
          todateFlag = true;
        }
      }
      logger.info("query " + query);
     
      con = DBConnectionUtility.getConnection();
      loggableStatement = new LoggableStatement(con, query);
      if (poNoFlag) {
        loggableStatement.setString(++setValue, poNo.trim().toUpperCase());
      }
      if (custFlag) {
        loggableStatement.setString(++setValue, cust.trim().toUpperCase());
      }
      if (benNameFlag) {
        loggableStatement.setString(++setValue, benName.trim().toUpperCase());
      }
      if (amountFlag) {
        loggableStatement.setString(++setValue, amount.trim().toUpperCase());
      }
      if (fromDateFlag) {
        loggableStatement.setString(++setValue, fromDate.trim().toUpperCase());
      }
      if (todateFlag) {
        loggableStatement.setString(++setValue, todate.trim().toUpperCase());
      }
      logger.info(
        "RetriveDetailsFromBOE::::::::::" + loggableStatement.getQueryString());
      logger.info("RetriveDetailsFromBOE: " + loggableStatement.getQueryString());
      rs = loggableStatement.executeQuery();
      logger.info("qUERY eXECUTED");
      while (rs.next())
      {
        chargVO = new ChargeScheduleVO();
       
        chargVO.setExportOrderNumber(rs.getString("EXPORT_ORDER_NUMBER"));
       
        chargVO.setExporterOrderDate(rs.getString("EXPORT_ORDER_DATE"));
       
        chargVO.setCifID(rs.getString("CIF_ID"));
       
        chargVO.setBeneficiaryName(rs.getString("BENEFICIARY_NAME"));
        chargVO.setGoodsCode(rs.getString("GOODS_CODE"));
       

        String poCCy = rs.getString("CURRENCY");
        if ((poCCy != null) && (!poCCy.isEmpty()) && (rs.getString("PO_VALUE") != null)) {
          chargVO.setPoValue(rs.getString("PO_VALUE") + " " + poCCy);
        } else {
          chargVO.setPoValue(rs.getString("PO_VALUE"));
        }
        chargVO.setExportexpiryDate(rs.getString("EXPIRY_DATE"));
       
        chargVO.setLastShipmentDate(rs.getString("LAST_SHIPMENT_DATE"));
       
        chargVO.setFreightDeduction(rs.getString("PERCENTAGE_FREIGHT_DEDUCTION"));
       
        chargVO.setInsuranceDeduction(rs.getString("PERCENTAGE_INSURANCE_DEDUCTION"));
       
        chargVO.setMarginPercentage(rs.getString("MARGIN_PERCENTAGE"));
        if ((poCCy != null) && (!poCCy.isEmpty()) && (rs.getString("ELIGIBLE_AMOUNT") != null)) {
          chargVO.setEligibleAmount(rs.getString("ELIGIBLE_AMOUNT") + " " + poCCy);
        } else {
          chargVO.setEligibleAmount(rs.getString("ELIGIBLE_AMOUNT"));
        }
        chargVO.setImporterName(rs.getString("IMPORTER_NAME"));
       
        multiPaymentReferenceList.add(chargVO);
      }
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, loggableStatement, rs);
    }
    logger.info("Exiting Method");
    return multiPaymentReferenceList;
  }
 
  public double getDecimalforCurrency(String curr)
  {
    double deci = 0.0D;
    Connection conn = null;
    PreparedStatement pre = null;
    ResultSet res = null;
    try
    {
      conn = DBConnectionUtility.getConnection();
      String query = "select power(10,C8CED) from C8PF where c8ccy=? ";
     
      pre = conn.prepareStatement(query);
      pre.setString(1, curr);
      res = pre.executeQuery();
      if (res.next()) {
        deci = res.getDouble(1);
      }
    }
    catch (Exception localException)
    {
      try
      {
        if (res != null) {
          res.close();
        }
        if (pre != null) {
          pre.close();
        }
        if (conn != null) {
          conn.close();
        }
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
        if (res != null) {
          res.close();
        }
        if (pre != null) {
          pre.close();
        }
        if (conn != null) {
          conn.close();
        }
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
    return deci;
  }
 
  public ArrayList<ChargeScheduleVO> loadFinanceData(String poNumber)
  {
    Connection aConnection = null;
    LoggableStatement aLoggableStatement = null;
    ResultSet aResultSet = null;
    ArrayList<ChargeScheduleVO> poFinList = new ArrayList();
    try
    {
      aConnection = DBConnectionUtility.getConnection();
      aLoggableStatement = new LoggableStatement(aConnection, "SELECT AMASTER.MASTER_REF,AEXTEVENTPPO.UFINAMT AS AMOUNT, AEXTEVENTPPO.CCY_2 AS CCY FROM MASTER AMASTER,BASEEVENT ABASEEVENT,EXTEVENTPPO AEXTEVENTPPO WHERE AMASTER.KEY97 = ABASEEVENT.MASTER_KEY AND ABASEEVENT.STATUS = 'c' AND ABASEEVENT.EXTFIELD = AEXTEVENTPPO.FK_EVENT AND TRIM(AEXTEVENTPPO.PON) = ?");
      aLoggableStatement.setString(1, poNumber);
      aResultSet = aLoggableStatement.executeQuery();
      logger.info("QueryString " + aLoggableStatement.getQueryString());
      while (aResultSet.next())
      {
        logger.info("inside while condition");
        ChargeScheduleVO aChargeScheduleVO = new ChargeScheduleVO();
        if (aResultSet.getString("MASTER_REF") != null) {
          aChargeScheduleVO.setDescription(aResultSet.getString("MASTER_REF"));
        }
        if ((aResultSet.getString("AMOUNT") != null) && (aResultSet.getString("CCY") != null))
        {
          logger.info("Eligible amount" + aResultSet.getString("AMOUNT") + aResultSet.getString("CCY"));
          String currency = aResultSet.getString("CCY");
          String eligible_amt = aResultSet.getString("AMOUNT");
          double divideByDecimal = getDecimalforCurrency(currency);
          BigDecimal eligible_Val = new BigDecimal(eligible_amt);
          BigDecimal divideByDecimal_Val = new BigDecimal(divideByDecimal);
          BigDecimal total_Val = eligible_Val.divide(divideByDecimal_Val);
          String final_Value = String.valueOf(total_Val);
          logger.info("Final Eligible amount" + final_Value + " " + currency);
          aChargeScheduleVO.setEligibleAmount(final_Value + " " + currency);
        }
        poFinList.add(aChargeScheduleVO);
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(aConnection, aLoggableStatement, aResultSet);
    }
    return poFinList;
  }
 
  public ArrayList<ChargeScheduleVO> loadEnquiryProcessData(ChargeScheduleVO chargSchVO)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement loggableStatement = null;
    ResultSet rs = null;
    String query = null;
   
    String poNo = null;
    String cust = null;
    String benName = null;
    String amount = null;
    String fromDate = null;
    String todate = null;
    String status = null;
    ChargeScheduleVO chargVO = null;
    String sqlQuery = null;
    ArrayList<ChargeScheduleVO> multiPaymentReferenceList = null;
    CommonMethods commonMethods = null;
    int setValue = 0;
    boolean poNoFlag = false;
    boolean custFlag = false;
    boolean benNameFlag = false;
    boolean amountFlag = false;
    boolean fromDateFlag = false;
    boolean todateFlag = false;
    boolean statusFlag = false;
    String loginedUserId = null;
    try
    {
      commonMethods = new CommonMethods();
      multiPaymentReferenceList = new ArrayList();
      Date aDate = new Date();
      SimpleDateFormat aFormat = new SimpleDateFormat("dd/MM/yyyy");
      String todayDate = aFormat.format(aDate);
     







      HttpSession session = ServletActionContext.getRequest().getSession();
      String userId = (String)session.getAttribute("loginedUserId");
     
      query = "select Export_Order_Number,TO_CHAR(TO_DATE(Export_Order_date,'DD/MM/YY'),'dd/mm/YYYY') AS Export_Order_date,STATUS, CIF_ID,Beneficiary_Name,Inco_Terms,Goods_Code,PO_Value,Currency,TO_CHAR(TO_DATE(Expiry_Date,'DD/MM/YY'),'dd/mm/YYYY') AS Expiry_Date, TO_CHAR(TO_DATE(Last_Shipment_Date,'DD/MM/YY'),'dd/mm/YYYY') AS Last_Shipment_Date,Percentage_Freight_Deduction, Percentage_Insurance_Deduction,Margin_percentage,Eligible_amount,importer_name from ett_export_order where  Export_Order_Number=Export_Order_Number ";
      if (chargSchVO != null)
      {
        poNo = chargSchVO.getPoNo();
        cust = chargSchVO.getPoCif();
        benName = chargSchVO.getBeneficiaryName();
        amount = chargSchVO.getPoAmtValue();
        fromDate = chargSchVO.getFromDate();
        todate = chargSchVO.getToDate();
        status = chargSchVO.getStatusList();
        if (!CommonMethods.isNull(poNo))
        {
          query = query + " AND UPPER(EXPORT_ORDER_NUMBER) LIKE '%'||?||'%'";
          poNoFlag = true;
        }
        if (!CommonMethods.isNull(cust))
        {
          query = query + " AND UPPER(CIF_ID) LIKE '%'||?||'%'";
          custFlag = true;
        }
        if (!CommonMethods.isNull(benName))
        {
          query = query + " AND UPPER(BENEFICIARY_NAME) LIKE '%'||?||'%'";
          benNameFlag = true;
        }
        if (!CommonMethods.isNull(amount))
        {
          query = query + " AND UPPER(PO_VALUE) LIKE '%'||?||'%'";
          amountFlag = true;
        }
        if (!CommonMethods.isNull(fromDate))
        {
          query = query + " AND to_date(TO_CHAR(maker_timestamp,'DD/MM/YYYY'),'DD/MM/YYYY') >= TO_DATE(?,'DD/MM/YYYY')";
          fromDateFlag = true;
        }
        if (!CommonMethods.isNull(todate))
        {
          query = query + " AND to_date(TO_CHAR(maker_timestamp,'DD/MM/YYYY'),'DD/MM/YYYY') <= TO_DATE(?,'DD/MM/YYYY')";
          todateFlag = true;
        }
        if (!CommonMethods.isNull(status))
        {
          query = query + " AND UPPER(STATUS) = ?";
          statusFlag = true;
        }
      }
      logger.info("query " + query);
     
      con = DBConnectionUtility.getConnection();
      loggableStatement = new LoggableStatement(con, query);
      if (poNoFlag) {
        loggableStatement.setString(++setValue, poNo.trim().toUpperCase());
      }
      if (custFlag) {
        loggableStatement.setString(++setValue, cust.trim().toUpperCase());
      }
      if (benNameFlag) {
        loggableStatement.setString(++setValue, benName.trim().toUpperCase());
      }
      if (amountFlag) {
        loggableStatement.setString(++setValue, amount.trim().toUpperCase());
      }
      if (fromDateFlag) {
        loggableStatement.setString(++setValue, fromDate.trim().toUpperCase());
      }
      if (todateFlag) {
        loggableStatement.setString(++setValue, todate.trim().toUpperCase());
      }
      if (statusFlag) {
        loggableStatement.setString(++setValue, status.trim().toUpperCase());
      }
      logger.info(
        "RetriveDetails for Enquiry Process=>" + loggableStatement.getQueryString());
      logger.info(
        "RetriveDetails for Enquiry Process: " + loggableStatement.getQueryString());
      rs = loggableStatement.executeQuery();
      logger.info("qUERY eXECUTED");
      while (rs.next())
      {
        chargVO = new ChargeScheduleVO();
       
        chargVO.setExportOrderNumber(rs.getString("EXPORT_ORDER_NUMBER"));
       
        chargVO.setExporterOrderDate(rs.getString("EXPORT_ORDER_DATE"));
       
        chargVO.setStatus(rs.getString("STATUS"));
       
        chargVO.setCifID(rs.getString("CIF_ID"));
       
        chargVO.setBeneficiaryName(rs.getString("BENEFICIARY_NAME"));
        chargVO.setGoodsCode(rs.getString("GOODS_CODE"));
       

        String poCCy = rs.getString("CURRENCY");
        if ((poCCy != null) && (!poCCy.isEmpty()) && (rs.getString("PO_VALUE") != null)) {
          chargVO.setPoValue(rs.getString("PO_VALUE") + " " + poCCy);
        } else {
          chargVO.setPoValue(rs.getString("PO_VALUE"));
        }
        chargVO.setExportexpiryDate(rs.getString("EXPIRY_DATE"));
       
        chargVO.setLastShipmentDate(rs.getString("LAST_SHIPMENT_DATE"));
       
        chargVO.setFreightDeduction(rs.getString("PERCENTAGE_FREIGHT_DEDUCTION"));
       
        chargVO.setInsuranceDeduction(rs.getString("PERCENTAGE_INSURANCE_DEDUCTION"));
       
        chargVO.setMarginPercentage(rs.getString("MARGIN_PERCENTAGE"));
        if ((poCCy != null) && (!poCCy.isEmpty()) && (rs.getString("ELIGIBLE_AMOUNT") != null)) {
          chargVO.setEligibleAmount(rs.getString("ELIGIBLE_AMOUNT") + " " + poCCy);
        } else {
          chargVO.setEligibleAmount(rs.getString("ELIGIBLE_AMOUNT"));
        }
        chargVO.setImporterName(rs.getString("IMPORTER_NAME"));
       
        multiPaymentReferenceList.add(chargVO);
      }
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, loggableStatement, rs);
    }
    logger.info("Exiting Method");
    return multiPaymentReferenceList;
  }
 
  public ChargeScheduleVO getEnquiryProcessDetails(String poNo, String cifId)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement loggableStatement = null;
    ResultSet rs = null;
   
    ChargeScheduleVO chargVO = null;
    try
    {
      chargVO = new ChargeScheduleVO();
     
      con = DBConnectionUtility.getConnection();
      loggableStatement = new LoggableStatement(con, "select STATUS,EXPORT_ORDER_NUMBER,TO_CHAR(TO_DATE(EXPORT_ORDER_DATE,'DD/MM/YY'),'dd/mm/YYYY') AS EXPORT_ORDER_DATE,CIF_ID,BENEFICIARY_NAME,INCO_TERMS,GOODS_CODE, PO_VALUE,CURRENCY,IMPORTER_NAME,TO_CHAR(TO_DATE(Expiry_Date,'DD/MM/YY'),'dd/mm/YYYY') AS EXPIRY_DATE,TO_CHAR(TO_DATE(Last_Shipment_Date,'DD/MM/YY'),'dd/mm/YYYY') AS LAST_SHIPMENT_DATE,PERCENTAGE_FREIGHT_DEDUCTION, PERCENTAGE_INSURANCE_DEDUCTION,MARGIN_PERCENTAGE,ELIGIBLE_AMOUNT, TO_CHAR(GOODS_DESC) as GOODS_DESC, INWARD_NO,NTP,TO_CHAR(TO_DATE(DUE_DATE,'DD/MM/YY'),'dd/mm/YYYY') AS DUE_DATE,SHPMODE from ett_export_order where EXPORT_ORDER_NUMBER=? and CIF_ID=?");
      loggableStatement.setString(1, poNo);
      loggableStatement.setString(2, cifId);
     
      logger.info("fetch Single EnquiryDetails: " + loggableStatement.getQueryString());
      rs = loggableStatement.executeQuery();
      logger.info("qUERY eXECUTED");
      while (rs.next())
      {
        chargVO.setExportOrderNumber(rs.getString("EXPORT_ORDER_NUMBER"));
       
        chargVO.setExporterOrderDate(rs.getString("EXPORT_ORDER_DATE"));
       
        chargVO.setCifID(rs.getString("CIF_ID"));
       
        chargVO.setBeneficiaryName(rs.getString("BENEFICIARY_NAME"));
       
        chargVO.setIncoTerms(rs.getString("INCO_TERMS"));
       
        chargVO.setGoodsCode(rs.getString("GOODS_CODE"));
       
        String poCCy = rs.getString("CURRENCY");
        if ((poCCy != null) && (!poCCy.isEmpty()) && (rs.getString("PO_VALUE") != null)) {
          chargVO.setPoValue(rs.getString("PO_VALUE") + " " + poCCy);
        } else {
          chargVO.setPoValue(rs.getString("PO_VALUE"));
        }
        chargVO.setImporterName(rs.getString("IMPORTER_NAME"));
       
        chargVO.setExportexpiryDate(rs.getString("EXPIRY_DATE"));
       
        chargVO.setLastShipmentDate(rs.getString("LAST_SHIPMENT_DATE"));
       
        chargVO.setFreightDeduction(rs.getString("PERCENTAGE_FREIGHT_DEDUCTION"));
       
        chargVO.setInsuranceDeduction(rs.getString("PERCENTAGE_INSURANCE_DEDUCTION"));
       
        chargVO.setMarginPercentage(rs.getString("MARGIN_PERCENTAGE"));
        if ((poCCy != null) && (!poCCy.isEmpty()) && (rs.getString("ELIGIBLE_AMOUNT") != null)) {
          chargVO.setEligibleAmount(rs.getString("ELIGIBLE_AMOUNT") + " " + poCCy);
        } else {
          chargVO.setEligibleAmount(rs.getString("ELIGIBLE_AMOUNT"));
        }
        chargVO.setInwardNo(rs.getString("INWARD_NO"));
       
        chargVO.setGoodsDescription(rs.getString("GOODS_DESC"));
       
        chargVO.setNtPeriod(rs.getString("NTP"));
        chargVO.setDueDate(rs.getString("DUE_DATE"));
        chargVO.setShpMode(rs.getString("SHPMODE"));
        if (((rs.getString("STATUS") instanceof String)) && (rs.getString("STATUS").trim().equalsIgnoreCase("P"))) {
          chargVO.setStatus("PENDING");
        } else if (((rs.getString("STATUS") instanceof String)) &&
          (rs.getString("STATUS").trim().equalsIgnoreCase("R"))) {
          chargVO.setStatus("REJECTED");
        } else if (((rs.getString("STATUS") instanceof String)) &&
          (rs.getString("STATUS").trim().equalsIgnoreCase("A"))) {
          chargVO.setStatus("AUTHORIZED");
        }
      }
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, loggableStatement, rs);
    }
    logger.info("Exiting Method");
    return chargVO;
  }
 
  public ChargeScheduleVO getCheckerProcessDetails(String poNo, String cifId)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement loggableStatement = null;
    ResultSet rs = null;
   
    ChargeScheduleVO chargVO = null;
    try
    {
      chargVO = new ChargeScheduleVO();
     
      con = DBConnectionUtility.getConnection();
      loggableStatement = new LoggableStatement(con, "select STATUS,EXPORT_ORDER_NUMBER,TO_CHAR(TO_DATE(EXPORT_ORDER_DATE,'DD/MM/YY'),'dd/mm/YYYY') AS EXPORT_ORDER_DATE,CIF_ID,BENEFICIARY_NAME,INCO_TERMS,GOODS_CODE, PO_VALUE,CURRENCY,IMPORTER_NAME,TO_CHAR(TO_DATE(Expiry_Date,'DD/MM/YY'),'dd/mm/YYYY') AS EXPIRY_DATE,TO_CHAR(TO_DATE(Last_Shipment_Date,'DD/MM/YY'),'dd/mm/YYYY') AS LAST_SHIPMENT_DATE,PERCENTAGE_FREIGHT_DEDUCTION, PERCENTAGE_INSURANCE_DEDUCTION,MARGIN_PERCENTAGE,ELIGIBLE_AMOUNT, TO_CHAR(GOODS_DESC) as GOODS_DESC, INWARD_NO,NTP,TO_CHAR(TO_DATE(DUE_DATE,'DD/MM/YY'),'dd/mm/YYYY') AS DUE_DATE,SHPMODE from ett_export_order where EXPORT_ORDER_NUMBER=? and CIF_ID=?");
      loggableStatement.setString(1, poNo);
      loggableStatement.setString(2, cifId);
     
      logger.info("fetch Single EnquiryDetails: " + loggableStatement.getQueryString());
      rs = loggableStatement.executeQuery();
      logger.info("qUERY eXECUTED");
      while (rs.next())
      {
        chargVO.setExportOrderNumber(rs.getString("EXPORT_ORDER_NUMBER"));
       
        chargVO.setExporterOrderDate(rs.getString("EXPORT_ORDER_DATE"));
       
        chargVO.setCifID(rs.getString("CIF_ID"));
       
        chargVO.setBeneficiaryName(rs.getString("BENEFICIARY_NAME"));
       
        chargVO.setIncoTerms(rs.getString("INCO_TERMS"));
       
        chargVO.setGoodsCode(rs.getString("GOODS_CODE"));
       
        String poCCy = rs.getString("CURRENCY");
        if ((poCCy != null) && (!poCCy.isEmpty()) && (rs.getString("PO_VALUE") != null)) {
          chargVO.setPoValue(rs.getString("PO_VALUE") + " " + poCCy);
        } else {
          chargVO.setPoValue(rs.getString("PO_VALUE"));
        }
        chargVO.setImporterName(rs.getString("IMPORTER_NAME"));
       
        chargVO.setExportexpiryDate(rs.getString("EXPIRY_DATE"));
       
        chargVO.setLastShipmentDate(rs.getString("LAST_SHIPMENT_DATE"));
       
        chargVO.setFreightDeduction(rs.getString("PERCENTAGE_FREIGHT_DEDUCTION"));
       
        chargVO.setInsuranceDeduction(rs.getString("PERCENTAGE_INSURANCE_DEDUCTION"));
       
        chargVO.setMarginPercentage(rs.getString("MARGIN_PERCENTAGE"));
        if ((poCCy != null) && (!poCCy.isEmpty()) && (rs.getString("ELIGIBLE_AMOUNT") != null)) {
          chargVO.setEligibleAmount(rs.getString("ELIGIBLE_AMOUNT") + " " + poCCy);
        } else {
          chargVO.setEligibleAmount(rs.getString("ELIGIBLE_AMOUNT"));
        }
        chargVO.setGoodsDescription(rs.getString("GOODS_DESC"));
        chargVO.setInwardNo(rs.getString("INWARD_NO"));
        chargVO.setNtPeriod(rs.getString("NTP"));
        chargVO.setDueDate(rs.getString("DUE_DATE"));
        chargVO.setShpMode(rs.getString("SHPMODE"));
      }
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, loggableStatement, rs);
    }
    logger.info("Exiting Method");
    return chargVO;
  }
 
  public ChargeScheduleVO approveSinglePODetails(ChargeScheduleVO chargeScheduleVO)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement loggableStatement = null;
    ResultSet rs = null;
    try
    {
      HttpSession session = ServletActionContext.getRequest().getSession();
      String userId = (String)session.getAttribute("loginedUserId");
     
      logger.info("chargeScheduleVO.getRemark()=>" + chargeScheduleVO.getRemark());
      logger.info(" chargeScheduleVO.getExportOrderNumber()==>" + chargeScheduleVO.getExportOrderNumber());
      logger.info("chargeScheduleVO.getCifID()==>" + chargeScheduleVO.getCifID());
     
      con = DBConnectionUtility.getConnection();
      loggableStatement = new LoggableStatement(con, "UPDATE ett_export_order SET STATUS ='A',UPDATED_BY=?,REMARKS=? WHERE EXPORT_ORDER_NUMBER =? AND CIF_ID=?");
      logger.info("ActionConstants.APPROVE_SINGLE_POUPDATE ett_export_order SET STATUS ='A',UPDATED_BY=?,REMARKS=? WHERE EXPORT_ORDER_NUMBER =? AND CIF_ID=?");
      loggableStatement.setString(1, userId);
      loggableStatement.setString(2, chargeScheduleVO.getRemark());
      loggableStatement.setString(3, chargeScheduleVO.getExportOrderNumber());
      loggableStatement.setString(4, chargeScheduleVO.getCifID());
     
      logger.info(
        "UPDATE Single CHECKER Details" + loggableStatement.getQueryString());
      logger.info("UPDATE Single CHECKER Details: " + loggableStatement.getQueryString());
      loggableStatement.executeUpdate();
      logger.info("qUERY eXECUTED");
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, loggableStatement, rs);
    }
    logger.info("Exiting Method");
    return chargeScheduleVO;
  }
 
  public ChargeScheduleVO rejectSinglePODetails(ChargeScheduleVO chargeScheduleVO)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement loggableStatement = null;
    ResultSet rs = null;
    try
    {
      HttpSession session = ServletActionContext.getRequest().getSession();
      String userId = (String)session.getAttribute("loginedUserId");
     
      logger.info("chargeScheduleVO.getRemark()=>" + chargeScheduleVO.getRemark());
      logger.info(" chargeScheduleVO.getExportOrderNumber()==>" + chargeScheduleVO.getExportOrderNumber());
      logger.info("chargeScheduleVO.getCifID()==>" + chargeScheduleVO.getCifID());
     
      con = DBConnectionUtility.getConnection();
      loggableStatement = new LoggableStatement(con, "UPDATE ett_export_order SET STATUS ='R',UPDATED_BY=?,REMARKS=?  WHERE EXPORT_ORDER_NUMBER =? AND CIF_ID=?");
      logger.info("ActionConstants.APPROVE_SINGLE_POUPDATE ett_export_order SET STATUS ='A',UPDATED_BY=?,REMARKS=? WHERE EXPORT_ORDER_NUMBER =? AND CIF_ID=?");
      loggableStatement.setString(1, userId);
      loggableStatement.setString(2, chargeScheduleVO.getRemark());
      loggableStatement.setString(3, chargeScheduleVO.getExportOrderNumber());
      loggableStatement.setString(4, chargeScheduleVO.getCifID());
     
      logger.info(
        "UPDATE Single CHECKER Details" + loggableStatement.getQueryString());
      logger.info("UPDATE Single CHECKER Details: " + loggableStatement.getQueryString());
      loggableStatement.executeUpdate();
      logger.info("qUERY eXECUTED");
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, loggableStatement, rs);
    }
    logger.info("Exiting Method");
    return chargeScheduleVO;
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
    String loginedUserId = null;
    try
    {
      commonMethods = new CommonMethods();
      con = DBConnectionUtility.getConnection();
      HttpSession session = ServletActionContext.getRequest().getSession();
      loginedUserId = (String)session.getAttribute("loginedUserName");
      if ((chkList != null) && (check != null))
      {
        for (int i = 0; i < chkList.length; i++)
        {
          String temp = chkList[i];
          String[] a = temp.split(":");
          if (checkExportStatus(a[0])) {
            if (check.equalsIgnoreCase("approve"))
            {
              logger.info("Length----->" + chkList.length);
             
              query = "UPDATE ett_export_order SET STATUS ='A',UPDATED_BY=?,REMARKS=?  WHERE EXPORT_ORDER_NUMBER =?";
             
              loggableStatement = new LoggableStatement(con, query);
              loggableStatement.setString(1, loginedUserId);
              loggableStatement.setString(2, remarks);
              loggableStatement.setString(3, a[0]);
              count = loggableStatement.executeUpdate();
            }
            else if (check.equalsIgnoreCase("reject"))
            {
              if (checkExportStatus(a[0]))
              {
                query = "UPDATE ett_export_order SET STATUS ='R',UPDATED_BY=?,REMARKS=? WHERE EXPORT_ORDER_NUMBER =?";
                loggableStatement = new LoggableStatement(con, query);
                loggableStatement.setString(1, loginedUserId);
                loggableStatement.setString(2, remarks);
                loggableStatement.setString(3, a[0]);
                count = loggableStatement.executeUpdate();
              }
            }
          }
        }
        logger.info("updateStatus:::::::::" + loggableStatement.getQueryString());
        logger.info("updateStatus: " + loggableStatement.getQueryString());
        if (count > 0) {
          result = "success";
        }
        con.commit();
      }
    }
    catch (Exception e)
    {
      e.getMessage();
      throwDAOException(e);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, loggableStatement, rs);
    }
    logger.info("Exiting Method");
    return result;
  }
 
  public boolean checkExportStatus(String exportNumber)
  {
    Boolean check = Boolean.valueOf(true);
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rst = null;
    int checkStat = 0;
    String checkPO = "SELECT count(*) as TOT FROM ETT_EXPORT_ORDER where STATUS in ('A','R') and EXPORT_ORDER_NUMBER=?";
    try
    {
      con = DBConnectionUtility.getConnection();
      pst = con.prepareStatement(checkPO);
      pst.setString(1, exportNumber);
      rst = pst.executeQuery();
      while (rst.next()) {
        checkStat = rst.getInt(1);
      }
      if (checkStat > 0) {
        check = Boolean.valueOf(false);
      }
      logger.info("Check status-->" + check);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return check.booleanValue();
  }
 
  public String getRole(ChargeScheduleVO chargVO)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement loggableStatement = null;
    ResultSet rs = null;
    String result = null;
    String sqlQuery = null;
    CommonMethods commonMethods = null;
    try
    {
      commonMethods = new CommonMethods();
      con = DBConnectionUtility.getConnection();
      if (!CommonMethods.isNull(chargVO.getSessionUserName()))
      {
        sqlQuery = "SELECT COUNT(*) as Count FROM SECAGE88 U, TEAMUSRMAP T  WHERE  T.USERKEY = U.SKEY80 AND U.NAME85  = ? AND UPPER(T.TEAMKEY) LIKE 'PO%' group by U.NAME85 ";
       


        loggableStatement = new LoggableStatement(con, sqlQuery);
        loggableStatement.setString(1, chargVO.getSessionUserName());
       
        rs = loggableStatement.executeQuery();
        while (rs.next()) {
          result = rs.getString("Count");
        }
      }
    }
    catch (Exception exception)
    {
      throwDAOException(exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, loggableStatement, rs);
    }
    return result;
  }
 
  private void throwDAOException(Exception exception) {}
 
  public String getSessionUserID(String userName)
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement pst = null;
    ResultSet rs = null;
    String sessionUserId = null;
    String QUERY = "select skey80 from secage88 where name85=?";
    try
    {
      con = DBConnectionUtility.getConnection();
      pst = new LoggableStatement(con, QUERY);
      pst.setString(1, userName);
      logger.info(pst.getQueryString());
      rs = pst.executeQuery();
      while (rs.next())
      {
        sessionUserId = rs.getString("skey80");
        logger.info("Executed");
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
    return sessionUserId;
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
      pst = new LoggableStatement(con, "select GFCPNC,GFCUN from GFPF order by GFCPNC");
      logger.info(pst.getQueryString());
      rs = pst.executeQuery();
      while (rs.next())
      {
        CustomerDataVO cusVo = new CustomerDataVO();
        cusVo.setCifID(CommonMethods.nullAndTrimString(rs.getString(1)));
        cusVo.setBeneficiaryName(CommonMethods.nullAndTrimString(rs.getString(2)));
       
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
 
  public ArrayList<CustomerDataVO> fetchIncoTerms(ArrayList<CustomerDataVO> customerList)
  {
    logger.info("Entering Method");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    try
    {
      customerList = new ArrayList();
      con = DBConnectionUtility.getConnection();
      pst = new LoggableStatement(con, "select code79,incote33 from incote48");
      logger.info(pst.getQueryString());
      rs = pst.executeQuery();
      while (rs.next())
      {
        CustomerDataVO cusVo = new CustomerDataVO();
        cusVo.setIncoTerms(CommonMethods.nullAndTrimString(rs.getString(1)));
        cusVo.setDescription(CommonMethods.nullAndTrimString(rs.getString(2)));
       
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
 
  public ArrayList<ChargeScheduleVO> chargeScheduleSearch(ArrayList<ChargeScheduleVO> chargeScheduleList)
  {
    logger.info("Entering Method");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    try
    {
      chargeScheduleList = new ArrayList();
      con = DBConnectionUtility.getConnection();
      pst = new LoggableStatement(con, "SELECT  R.ID, R.DESCR, R.KEY97, C.CODE79, C.CUS_MNM FROM ETT_CHARGE99_DET C,RELTEMPLTE R WHERE R.ID=C.CHG_TYPE AND C.OBSOLETE='N' AND C.STATUS ='MA'  AND R.TYPEFLAG=10136");
      logger.info(pst.getQueryString());
      rs = pst.executeQuery();
      while (rs.next())
      {
        ChargeScheduleVO scheduleVO = new ChargeScheduleVO();
        scheduleVO.setChargeId(CommonMethods.nullAndTrimString(rs.getString("ID")));
        scheduleVO.setChargeDesc(CommonMethods.nullAndTrimString(rs.getString("descr")));
        scheduleVO.setChargeKey97(CommonMethods.nullAndTrimString(rs.getString("KEY97")));
        scheduleVO.setCustomerCif(CommonMethods.nullAndTrimString(rs.getString("CUS_MNM")));
        scheduleVO.setProductId(CommonMethods.nullAndTrimString(rs.getString("CODE79")));
        chargeScheduleList.add(scheduleVO);
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
    return chargeScheduleList;
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
      if (!CommonMethods.isNull(cusDataVo.getBeneficiaryName()))
      {
        query = "select GFCPNC AS CIFID,GFCUN AS CUSTOMER from GFPF  WHERE GFCUN like ?||'%' ";
        setValue = cusDataVo.getBeneficiaryName();
      }
      else if (!CommonMethods.isNull(cusDataVo.getCifID()))
      {
        query = "select GFCPNC AS CIFID,GFCUN AS CUSTOMER from GFPF  WHERE GFCPNC like ?||'%' ";
        setValue = cusDataVo.getCifID();
      }
      else if ((!CommonMethods.isNull(cusDataVo.getBeneficiaryName())) &&
        (!CommonMethods.isNull(cusDataVo.getCifID())))
      {
        query = "select GFCPNC AS CIFID,GFCUN AS CUSTOMER from GFPF  WHERE GFCUN like  ?||'%' AND GFCPNC like '%'||?||'%' ";
        setValue = cusDataVo.getBeneficiaryName();
        setValue1 = cusDataVo.getCifID();
      }
      else
      {
        query = "select GFCPNC AS CIFID,GFCUN AS CUSTOMER from GFPF ";
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
        cusVo.setCifID(CommonMethods.nullAndTrimString(rs.getString("CIFID")));
        cusVo.setBeneficiaryName(CommonMethods.nullAndTrimString(rs.getString("CUSTOMER")));
       
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
 
  public ArrayList<CustomerDataVO> filterIncoTerms(ArrayList<CustomerDataVO> customerList, CustomerDataVO cusDataVo)
  {
    logger.info("Entering Method");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    String cusName = "";
    String cusNumber = "";
    String setValue = null;
   
    String query = "";
    try
    {
      customerList = new ArrayList();
      con = DBConnectionUtility.getConnection();
      if (!CommonMethods.isNull(cusDataVo.getIncoTerms()))
      {
        query = "select code79,incote33 from incote48 WHERE UPPER(TRIM(code79)) like ?||'%' ";
        setValue = cusDataVo.getIncoTerms().trim().toUpperCase();
      }
      else
      {
        query = "select code79,incote33 from incote48";
      }
      pst = new LoggableStatement(con, query);
      if (setValue != null) {
        pst.setString(1, setValue);
      }
      logger.info(pst.getQueryString());
      rs = pst.executeQuery();
      while (rs.next())
      {
        CustomerDataVO cusVo = new CustomerDataVO();
        cusVo.setIncoTerms(CommonMethods.nullAndTrimString(rs.getString("CODE79")));
        cusVo.setDescription(CommonMethods.nullAndTrimString(rs.getString("INCOTE33")));
       
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
 
  public ArrayList<ChargeSelectionVO> chargeSearch(ArrayList<ChargeSelectionVO> chargeList)
  {
    logger.info("Entering Method");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    try
    {
      chargeList = new ArrayList();
      con = DBConnectionUtility.getConnection();
      pst = new LoggableStatement(con, "select id, descr, KEY97 from RELTEMPLTE where obsolete ='N' AND TYPEFLAG=10136");
      logger.info(pst.getQueryString());
      rs = pst.executeQuery();
      while (rs.next())
      {
        ChargeSelectionVO chargeVo = new ChargeSelectionVO();
        chargeVo.setChargeId(CommonMethods.nullAndTrimString(rs.getString("ID")));
        chargeVo.setChargeDesc(CommonMethods.nullAndTrimString(rs.getString("descr")));
        chargeVo.setChargeKey97(CommonMethods.nullAndTrimString(rs.getString("KEY97")));
        chargeList.add(chargeVo);
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
    return chargeList;
  }
 
  public ArrayList<ChargeSelectionVO> filterOfChargeList(ArrayList<ChargeSelectionVO> chargeList, ChargeSelectionVO chargeSelectionVO)
  {
    logger.info("Entering Method");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    String cusName = "";
    String cusNumber = "";
    boolean chrgcodeFlag = false;
    boolean chrgdescFlag = false;
    int setValue = 0;
    try
    {
      chargeList = new ArrayList();
      con = DBConnectionUtility.getConnection();
      if (!CommonMethods.isNull(chargeSelectionVO.getFilterChargeCode()))
      {
        cusName = " AND ID like '%'||?||'%' ";
        chrgcodeFlag = true;
      }
      if (!CommonMethods.isNull(chargeSelectionVO.getFilterChargeDesc()))
      {
        cusNumber = " AND DESCR like '%'||?||'%' ";
        chrgdescFlag = true;
      }
      String query = "select id, descr, KEY97 from RELTEMPLTE where obsolete ='N' AND TYPEFLAG=10136" + cusName + cusNumber;
      pst = new LoggableStatement(con, query);
      if (chrgcodeFlag) {
        pst.setString(++setValue, chargeSelectionVO.getFilterChargeCode());
      }
      if (chrgdescFlag) {
        pst.setString(++setValue, chargeSelectionVO.getFilterChargeDesc());
      }
      logger.info(pst.getQueryString());
      rs = pst.executeQuery();
      while (rs.next())
      {
        ChargeSelectionVO chargeVo = new ChargeSelectionVO();
        chargeVo.setChargeId(CommonMethods.nullAndTrimString(rs.getString("ID")));
        chargeVo.setChargeDesc(CommonMethods.nullAndTrimString(rs.getString("descr")));
        chargeVo.setChargeKey97(CommonMethods.nullAndTrimString(rs.getString("KEY97")));
        chargeList.add(chargeVo);
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
    return chargeList;
  }
 
  public ArrayList<ProductSelectionVO> productSearch(ArrayList<ProductSelectionVO> productList)
  {
    logger.info("Entering Method");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    try
    {
      productList = new ArrayList();
      con = DBConnectionUtility.getConnection();
      String query = "SELECT code79 as product,longna85 as description,key97 FROM exempl30 where typeflag=13270";
      pst = new LoggableStatement(con, query);
      logger.info(pst.getQueryString());
      rs = pst.executeQuery();
      while (rs.next())
      {
        ProductSelectionVO productVO = new ProductSelectionVO();
        productVO.setProductId(CommonMethods.nullAndTrimString(rs.getString("product")));
        productVO.setProductDesc(CommonMethods.nullAndTrimString(rs.getString("description")));
        productVO.setProductKey97(CommonMethods.nullAndTrimString(rs.getString("KEY97")));
        productList.add(productVO);
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
    return productList;
  }
 
  public ArrayList<ProductSelectionVO> filterOfProductList(ArrayList<ProductSelectionVO> productList, ProductSelectionVO productVo)
  {
    logger.info("Entering Method");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    String cusName = "";
    String cusNumber = "";
    boolean prdcodeFlag = false;
    boolean prddescFlag = false;
    int setValue = 0;
    try
    {
      String queryy = "SELECT code79 as product,longna85 as description,key97 FROM exempl30 where typeflag=13270";
      productList = new ArrayList();
      con = DBConnectionUtility.getConnection();
      if (!CommonMethods.isNull(productVo.getFilterProductCode()))
      {
        cusName = " AND code79 like '%'||?||'%' ";
        prdcodeFlag = true;
      }
      if (!CommonMethods.isNull(productVo.getFilterProductDesc()))
      {
        cusNumber = " AND longna85 like '%'||?||'%' ";
        prddescFlag = true;
      }
      String query = queryy + cusName + cusNumber;
      pst = new LoggableStatement(con, query);
      if (prdcodeFlag) {
        pst.setString(++setValue, productVo.getFilterProductCode());
      }
      if (prddescFlag) {
        pst.setString(++setValue, productVo.getFilterProductDesc());
      }
      logger.info(pst.getQueryString());
      rs = pst.executeQuery();
      while (rs.next())
      {
        ProductSelectionVO productVO = new ProductSelectionVO();
        productVO.setProductId(CommonMethods.nullAndTrimString(rs.getString("product")));
        productVO.setProductDesc(CommonMethods.nullAndTrimString(rs.getString("description")));
        productVO.setProductKey97(CommonMethods.nullAndTrimString(rs.getString("KEY97")));
        productList.add(productVO);
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
    return productList;
  }
 
  public ArrayList<ChargeScheduleVO> filterOfSceduleList(ArrayList<ChargeScheduleVO> chargeScheduleList, ChargeScheduleVO chargeVO)
  {
    logger.info("Entering Method");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    String query = null;
    String setValue = null;
    try
    {
      chargeScheduleList = new ArrayList();
      con = DBConnectionUtility.getConnection();
      if ((!CommonMethods.isNull(chargeVO.getCustomerCif())) && (!CommonMethods.isNull(chargeVO.getChargeType())) &&
        (!CommonMethods.isNull(chargeVO.getProductId())))
      {
        query = "SELECT R.ID, R.DESCR, R.KEY97, C.CODE79, C.CUS_MNM FROM ETT_CHARGE99_DET C,RELTEMPLTE R WHERE R.ID=C.CHG_TYPE AND C.OBSOLETE='N' AND C.STATUS ='MA' AND R.TYPEFLAG=10136 AND TRIM(C.CUS_MNM)=? AND R.KEY97=? AND C.CODE79=?";
       


        setValue = "query1";
      }
      else if ((!CommonMethods.isNull(chargeVO.getCustomerCif())) &&
        (!CommonMethods.isNull(chargeVO.getProductId())))
      {
        query = "SELECT R.ID, R.DESCR, R.KEY97, C.CODE79, C.CUS_MNM FROM ETT_CHARGE99_DET C,RELTEMPLTE R WHERE R.ID=C.CHG_TYPE AND C.OBSOLETE='N' AND C.STATUS ='MA' AND R.TYPEFLAG=10136 AND TRIM(C.CUS_MNM)=? AND C.CODE79=?";
       


        setValue = "query2";
      }
      else if (!CommonMethods.isNull(chargeVO.getCustomerCif()))
      {
        query = "SELECT R.ID, R.DESCR, R.KEY97, C.CODE79, C.CUS_MNM FROM ETT_CHARGE99_DET C,RELTEMPLTE R WHERE R.ID=C.CHG_TYPE AND C.OBSOLETE='N' AND C.STATUS ='MA' AND R.TYPEFLAG=10136 AND TRIM(C.CUS_MNM)=?";
       

        setValue = "query3";
      }
      else
      {
        query = "SELECT R.ID, R.DESCR, R.KEY97,C.CUS_MNM FROM CHARGE99 C,RELTEMPLTE R WHERE R.KEY97=C.CHG_TYPE AND C.OBSOLETE='N' AND R.TYPEFLAG=10136 AND TRIM(C.CUS_MNM) IS NOT NULL AND R.KEY97=?";
       


        setValue = "query4";
      }
      pst = new LoggableStatement(con, query);
      if ((setValue != null) && (setValue.equals("query1")))
      {
        pst.setString(1, chargeVO.getCustomerCif().trim());
        pst.setString(2, chargeVO.getChargeKey97().trim());
        pst.setString(3, chargeVO.getProductId().trim());
      }
      else if ((setValue != null) && (setValue.equals("query2")))
      {
        pst.setString(1, chargeVO.getCustomerCif().trim());
        pst.setString(2, chargeVO.getProductId().trim());
      }
      else if ((setValue != null) && (setValue.equals("query3")))
      {
        pst.setString(1, chargeVO.getCustomerCif().trim());
      }
      else if ((setValue != null) && (setValue.equals("query4")))
      {
        pst.setString(1, chargeVO.getChargeKey97().trim());
      }
      logger.info(pst.getQueryString());
      rs = pst.executeQuery();
      while (rs.next())
      {
        ChargeScheduleVO scheduleVO = new ChargeScheduleVO();
        scheduleVO.setChargeId(CommonMethods.nullAndTrimString(rs.getString("ID")));
        scheduleVO.setChargeDesc(CommonMethods.nullAndTrimString(rs.getString("descr")));
        scheduleVO.setChargeKey97(CommonMethods.nullAndTrimString(rs.getString("KEY97")));
        scheduleVO.setCustomerCif(CommonMethods.nullAndTrimString(rs.getString("CUS_MNM")));
        scheduleVO.setProductId(CommonMethods.nullAndTrimString(rs.getString("CODE79")));
       
        chargeScheduleList.add(scheduleVO);
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
    return chargeScheduleList;
  }
 
  public void getErrors(String errorCode, ChargeScheduleVO chargeVO)
  {
    String errormsg = CommonMethods.getErrorDesc(errorCode, "CS01");
    Object[] arg = { Integer.valueOf(0), "E", errormsg, "INPUT" };
    CommonMethods.setErrorvalues(arg, this.alertMsgArray);
    if (this.alertMsgArray.size() > 0) {
      chargeVO.setErrorList(this.alertMsgArray);
    }
  }
 
  public void setErrorForPurchaseDetails(String errorCode, ChargeScheduleVO chargeVO)
  {
    String errormsg = CommonMethods.getErrorDescFromProperties(errorCode);
    Object[] arg = { Integer.valueOf(0), "E", errormsg, "INPUT" };
    CommonMethods.setErrorvalues(arg, this.alertMsgArray);
    if (this.alertMsgArray.size() > 0) {
      chargeVO.setErrorList(this.alertMsgArray);
    }
  }
 
  public ArrayList<ChargeScheduleVO> createNewChargeSchedule(ArrayList<ChargeScheduleVO> chargeScheduleList, ChargeScheduleVO chargeVO)
  {
    logger.info("Entering Method");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    String query = null;
    try
    {
      chargeScheduleList = new ArrayList();
      con = DBConnectionUtility.getConnection();
     
      HttpSession session = ServletActionContext.getRequest().getSession();
     
      String userId = (String)session.getAttribute("USERID");
      if (createScheduleValidation(chargeVO) == 1)
      {
        pushToETTCharge(chargeVO.getChargeKey97().trim(), chargeVO.getCustomerCif().trim(),
          chargeVO.getProductId().trim(), userId, chargeVO.getChargeId().trim());
       





















        query = "SELECT R.ID, R.DESCR, R.KEY97, C.CUS_MNM FROM ETT_CHARGE99_DET C, RELTEMPLTE R WHERE R.ID=C.CHG_TYPE AND C.OBSOLETE='N'  AND R.TYPEFLAG=10136 AND TRIM(C.CUS_MNM)=? AND R.KEY97=? ";
       
        pst = new LoggableStatement(con, query);
        pst.setString(1, chargeVO.getCustomerCif().trim());
        pst.setString(2, chargeVO.getChargeKey97().trim());
        logger.info(pst.getQueryString());
        ResultSet rset = pst.executeQuery();
        while (rset.next())
        {
          ChargeScheduleVO scheduleVO = new ChargeScheduleVO();
          scheduleVO.setChargeId(CommonMethods.nullAndTrimString(rset.getString("ID")));
          scheduleVO.setChargeDesc(CommonMethods.nullAndTrimString(rset.getString("descr")));
          scheduleVO.setChargeKey97(CommonMethods.nullAndTrimString(rset.getString("KEY97")));
          scheduleVO.setCustomerCif(CommonMethods.nullAndTrimString(rset.getString("CUS_MNM")));
         
          chargeScheduleList.add(scheduleVO);
        }
        getErrors("8", chargeVO);
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
    return chargeScheduleList;
  }
 
  private String checkStatus(String sequence)
  {
    logger.info("Entering Method");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    String status = null;
    try
    {
      con = DBConnectionUtility.getConnection();
      String query = "select STATUS from apiserver where sequence=" + sequence;
      pst = new LoggableStatement(con, query);
      logger.info(pst.getQueryString());
      rs = pst.executeQuery();
      while (rs.next())
      {
        status = rs.getString("STATUS");
        if ((!status.equalsIgnoreCase("SUCCEEDED")) && (!status.equalsIgnoreCase("FAILED")))
        {
          Thread.sleep(5000L);
          break;
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
    return status;
  }
 
  public String pushToAPISERVER(String xmlToPost, String operation)
  {
    logger.info("Entering Method");
    Connection con = null;
    ResultSet rs = null;
    LoggableStatement ps = null;
    String lowRange = getSequenceAPISERVER();
    try
    {
      con = DBConnectionUtility.getConnection();
      String insert_apiserver = "insert into apiserver(SEQUENCE,VERSION,SERVICE,OPERATION,REQUEST,STATUS) VALUES('" +
        lowRange + "',(CAST(SYSDATE AS TIMESTAMP)),'TI','" + operation + "','" + xmlToPost +
        "','WAITING')";
      ps = new LoggableStatement(con, insert_apiserver);
      logger.info("Query " + ps.getQueryString());
      ps.executeUpdate();
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, ps, rs);
    }
    logger.info("Exiting Method");
    return lowRange;
  }
 
  public int getSequenceAPISERVER()
  {
    logger.info("Entering Method");
    Connection con = null;
    ResultSet rs = null;
    LoggableStatement ps = null;
    int sequenceNumber = 0;
    try
    {
      con = DBConnectionUtility.getConnection();
      ps = new LoggableStatement(con, "select max(to_number(SEQUENCE)) from APISERVER");
      rs = ps.executeQuery();
      if (rs.next()) {
        sequenceNumber = rs.getInt(1);
      }
      sequenceNumber++;
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, ps, rs);
    }
    logger.info("Exiting Method");
    return sequenceNumber;
  }
 
  private String generateXml(ChargeXmlVO xmlVO, String update)
    throws ParserConfigurationException
  {
    DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
    Document document = documentBuilder.newDocument();
   
    Element rocdoc01 = document.createElement("m:ChargeSchedule");
    document.appendChild(rocdoc01);
   
    Element mainType = document.createElement("m:MaintType");
    mainType.appendChild(document.createTextNode(update));
    rocdoc01.appendChild(mainType);
   
    Element chargeType = document.createElement("m:ChargeType");
    chargeType.appendChild(document.createTextNode(xmlVO.getChargeId()));
    rocdoc01.appendChild(chargeType);
   






    Element rocdoc02 = document.createElement("m:Customer");
    rocdoc01.appendChild(rocdoc02);
   
    Element srcBank = document.createElement("c:SourceBankingBusiness");
    srcBank.appendChild(document.createTextNode(xmlVO.getSrcBankBus()));
    rocdoc02.appendChild(srcBank);
   
    Element nemonic = document.createElement("c:Mnemonic");
    nemonic.appendChild(document.createTextNode(xmlVO.getCustomerCif()));
    rocdoc02.appendChild(nemonic);
    if (!xmlVO.getChargeGrp().equalsIgnoreCase(""))
    {
      Element chargeGrp = document.createElement("m:ChargeGroup");
      chargeGrp.appendChild(document.createTextNode(xmlVO.getChargeGrp()));
      rocdoc01.appendChild(chargeGrp);
    }
    if (!xmlVO.getChargeScheduleType().equalsIgnoreCase(""))
    {
      Element scheduleType = document.createElement("m:ChargeScheduleType");
      scheduleType.appendChild(document.createTextNode(xmlVO.getChargeScheduleType()));
      rocdoc01.appendChild(scheduleType);
    }
    if (!xmlVO.getCurrency().equalsIgnoreCase(""))
    {
      Element curr = document.createElement("m:Currency");
      curr.appendChild(document.createTextNode(xmlVO.getCurrency()));
      rocdoc01.appendChild(curr);
    }
    if (!xmlVO.getPeriodAmt().equalsIgnoreCase(""))
    {
      Element perAmt = document.createElement("m:PeriodOrAmount");
      perAmt.appendChild(document.createTextNode(xmlVO.getPeriodAmt()));
      rocdoc01.appendChild(perAmt);
    }
    if (!xmlVO.getChargeWaived().equalsIgnoreCase(""))
    {
      Element waived = document.createElement("m:ChargesWaived");
      waived.appendChild(document.createTextNode(xmlVO.getChargeWaived()));
      rocdoc01.appendChild(waived);
    }
    if (!xmlVO.getPeriodic().equalsIgnoreCase(""))
    {
      Element perCharge = document.createElement("m:PeriodicCharge");
      perCharge.appendChild(document.createTextNode(xmlVO.getPeriodic()));
      rocdoc01.appendChild(perCharge);
    }
    Element rocdoc03 = document.createElement("m:PeriodicChargeDetails");
    rocdoc01.appendChild(rocdoc03);
    if (!xmlVO.getAdvanceOfArrear().equalsIgnoreCase(""))
    {
      Element advArrear = document.createElement("c:AdvanceOrArrears");
      advArrear.appendChild(document.createTextNode(xmlVO.getAdvanceOfArrear()));
      rocdoc03.appendChild(advArrear);
    }
    if (!xmlVO.getOverAllMin().equalsIgnoreCase(""))
    {
      Element overMinAmt = document.createElement("m:OverallMinimumAmount");
      overMinAmt.appendChild(document.createTextNode(xmlVO.getOverAllMin()));
      rocdoc01.appendChild(overMinAmt);
    }
    if (!xmlVO.getOverAllMax().equalsIgnoreCase(""))
    {
      Element overMaxAmt = document.createElement("m:OverallMaximumAmount");
      overMaxAmt.appendChild(document.createTextNode(xmlVO.getOverAllMax()));
      rocdoc01.appendChild(overMaxAmt);
    }
    if (!xmlVO.getSplit().equalsIgnoreCase(""))
    {
      Element split = document.createElement("m:Split");
      split.appendChild(document.createTextNode(xmlVO.getSplit()));
      rocdoc01.appendChild(split);
    }
    Element tier1 = document.createElement("m:Tier1");
    rocdoc01.appendChild(tier1);
   
    Element t1Charge = document.createElement("c:ChargeTierBasic");
    tier1.appendChild(t1Charge);
    if (!xmlVO.getTier1Amt().equalsIgnoreCase(""))
    {
      Element t1Amt = document.createElement("c:Amount");
      t1Amt.appendChild(document.createTextNode(xmlVO.getTier1Amt()));
      t1Charge.appendChild(t1Amt);
    }
    if (!xmlVO.getTier1MinAmt().equalsIgnoreCase(""))
    {
      Element t1MinAmt = document.createElement("c:MinAmount");
      t1MinAmt.appendChild(document.createTextNode(xmlVO.getTier1MinAmt()));
      t1Charge.appendChild(t1MinAmt);
    }
    if (!xmlVO.getTier1MaxAmt().equalsIgnoreCase(""))
    {
      Element t1MaxAmt = document.createElement("c:MaxAmount");
      t1MaxAmt.appendChild(document.createTextNode(xmlVO.getTier1MaxAmt()));
      t1Charge.appendChild(t1MaxAmt);
    }
    if ((!xmlVO.getTier1Days().equalsIgnoreCase("")) && (!xmlVO.getTier1Period().equalsIgnoreCase("")))
    {
      Element t1Period = document.createElement("c:Period");
      t1Charge.appendChild(t1Period);
     
      Element t1TendorDay = document.createElement("c:TenorDays");
      t1TendorDay.appendChild(document.createTextNode(xmlVO.getTier1Days()));
      t1Period.appendChild(t1TendorDay);
     
      Element t1TendorPeriod = document.createElement("c:TenorPeriod");
      t1TendorPeriod.appendChild(document.createTextNode(xmlVO.getTier1Period()));
      t1Period.appendChild(t1TendorPeriod);
    }
    if (!xmlVO.getTier1Freq().equalsIgnoreCase(""))
    {
      Element t1Freq = document.createElement("c:ChargeFrequency");
      t1Freq.appendChild(document.createTextNode(xmlVO.getTier1Freq()));
      t1Charge.appendChild(t1Freq);
    }
    if (!xmlVO.getTier1Amount().equalsIgnoreCase(""))
    {
      Element t1Amount = document.createElement("c:Amount");
      t1Amount.appendChild(document.createTextNode(xmlVO.getTier1Amount()));
      tier1.appendChild(t1Amount);
    }
    if (!xmlVO.getTier1BaseRate().equalsIgnoreCase(""))
    {
      Element t1BaseRate = document.createElement("c:BaseRate");
      t1BaseRate.appendChild(document.createTextNode(xmlVO.getTier1BaseRate()));
      tier1.appendChild(t1BaseRate);
    }
    if (!xmlVO.getTier1DiffRate().equalsIgnoreCase(""))
    {
      Element t1DiffRate = document.createElement("c:DifferentialRate");
      t1DiffRate.appendChild(document.createTextNode(xmlVO.getTier1DiffRate()));
      tier1.appendChild(t1DiffRate);
    }
    if (!xmlVO.getTier1Interest().equalsIgnoreCase(""))
    {
      Element t1Intr = document.createElement("c:Interest");
      t1Intr.appendChild(document.createTextNode(xmlVO.getTier1Interest()));
      tier1.appendChild(t1Intr);
    }
    if (!xmlVO.getTier1Percent().equalsIgnoreCase(""))
    {
      Element t1Percent = document.createElement("c:Percent");
      t1Percent.appendChild(document.createTextNode(xmlVO.getTier1Percent()));
      tier1.appendChild(t1Percent);
    }
    Element tier2 = document.createElement("m:Tier2");
    rocdoc01.appendChild(tier2);
   
    Element t2Charge = document.createElement("c:ChargeTierBasic");
    tier2.appendChild(t2Charge);
    if (!xmlVO.getTier2Amt().equalsIgnoreCase(""))
    {
      Element t2Amt = document.createElement("c:Amount");
      t2Amt.appendChild(document.createTextNode(xmlVO.getTier2Amt()));
      t2Charge.appendChild(t2Amt);
    }
    if (!xmlVO.getTier2MinAmt().equalsIgnoreCase(""))
    {
      Element t2MinAmt = document.createElement("c:MinAmount");
      t2MinAmt.appendChild(document.createTextNode(xmlVO.getTier2MinAmt()));
      t2Charge.appendChild(t2MinAmt);
    }
    if (!xmlVO.getTier2MaxAmt().equalsIgnoreCase(""))
    {
      Element t2MaxAmt = document.createElement("c:MaxAmount");
      t2MaxAmt.appendChild(document.createTextNode(xmlVO.getTier2MaxAmt()));
      t2Charge.appendChild(t2MaxAmt);
    }
    if ((!xmlVO.getTier2Days().equalsIgnoreCase("")) && (!xmlVO.getTier2Period().equalsIgnoreCase("")))
    {
      Element t2Period = document.createElement("c:Period");
      t2Charge.appendChild(t2Period);
     
      Element t2TendorDay = document.createElement("c:TenorDays");
      t2TendorDay.appendChild(document.createTextNode(xmlVO.getTier2Days()));
      t2Period.appendChild(t2TendorDay);
     
      Element t2TendorPeriod = document.createElement("c:TenorPeriod");
      t2TendorPeriod.appendChild(document.createTextNode(xmlVO.getTier2Period()));
      t2Period.appendChild(t2TendorPeriod);
    }
    if (!xmlVO.getTier2Freq().equalsIgnoreCase(""))
    {
      Element t2Freq = document.createElement("c:ChargeFrequency");
      t2Freq.appendChild(document.createTextNode(xmlVO.getTier2Freq()));
      t2Charge.appendChild(t2Freq);
    }
    if (!xmlVO.getTier2Amount().equalsIgnoreCase(""))
    {
      Element t2Amount = document.createElement("c:Amount");
      t2Amount.appendChild(document.createTextNode(xmlVO.getTier2Amount()));
      tier2.appendChild(t2Amount);
    }
    if (!xmlVO.getTier2BaseRate().equalsIgnoreCase(""))
    {
      Element t2BaseRate = document.createElement("c:BaseRate");
      t2BaseRate.appendChild(document.createTextNode(xmlVO.getTier2BaseRate()));
      tier2.appendChild(t2BaseRate);
    }
    if (!xmlVO.getTier2DiffRate().equalsIgnoreCase(""))
    {
      Element t2DiffRate = document.createElement("c:DifferentialRate");
      t2DiffRate.appendChild(document.createTextNode(xmlVO.getTier2DiffRate()));
      tier2.appendChild(t2DiffRate);
    }
    if (!xmlVO.getTier2Interest().equalsIgnoreCase(""))
    {
      Element t2Intr = document.createElement("c:Interest");
      t2Intr.appendChild(document.createTextNode(xmlVO.getTier2Interest()));
      tier2.appendChild(t2Intr);
    }
    if (!xmlVO.getTier2Percent().equalsIgnoreCase(""))
    {
      Element t2Percent = document.createElement("c:Percent");
      t2Percent.appendChild(document.createTextNode(xmlVO.getTier2Percent()));
      tier2.appendChild(t2Percent);
    }
    Element tier3 = document.createElement("m:Tier3");
    rocdoc01.appendChild(tier3);
   
    Element t3Charge = document.createElement("c:ChargeTierBasic");
    tier3.appendChild(t3Charge);
    if (!xmlVO.getTier3Amt().equalsIgnoreCase(""))
    {
      Element t3Amt = document.createElement("c:Amount");
      t3Amt.appendChild(document.createTextNode(xmlVO.getTier3Amt()));
      t3Charge.appendChild(t3Amt);
    }
    if (!xmlVO.getTier3MinAmt().equalsIgnoreCase(""))
    {
      Element t3MinAmt = document.createElement("c:MinAmount");
      t3MinAmt.appendChild(document.createTextNode(xmlVO.getTier3MinAmt()));
      t3Charge.appendChild(t3MinAmt);
    }
    if (!xmlVO.getTier3MaxAmt().equalsIgnoreCase(""))
    {
      Element t3MaxAmt = document.createElement("c:MaxAmount");
      t3MaxAmt.appendChild(document.createTextNode(xmlVO.getTier3MaxAmt()));
      t3Charge.appendChild(t3MaxAmt);
    }
    if ((!xmlVO.getTier3Days().equalsIgnoreCase("")) && (!xmlVO.getTier3Period().equalsIgnoreCase("")))
    {
      Element t3Period = document.createElement("c:Period");
      t3Charge.appendChild(t3Period);
     
      Element t3TendorDay = document.createElement("c:TenorDays");
      t3TendorDay.appendChild(document.createTextNode(xmlVO.getTier3Days()));
      t3Period.appendChild(t3TendorDay);
     
      Element t3TendorPeriod = document.createElement("c:TenorPeriod");
      t3TendorPeriod.appendChild(document.createTextNode(xmlVO.getTier3Period()));
      t3Period.appendChild(t3TendorPeriod);
    }
    if (!xmlVO.getTier3Freq().equalsIgnoreCase(""))
    {
      Element t3Freq = document.createElement("c:ChargeFrequency");
      t3Freq.appendChild(document.createTextNode(xmlVO.getTier3Freq()));
      t3Charge.appendChild(t3Freq);
    }
    if (!xmlVO.getTier3Amount().equalsIgnoreCase(""))
    {
      Element t3Amount = document.createElement("c:Amount");
      t3Amount.appendChild(document.createTextNode(xmlVO.getTier3Amount()));
      tier3.appendChild(t3Amount);
    }
    if (!xmlVO.getTier3BaseRate().equalsIgnoreCase(""))
    {
      Element t3BaseRate = document.createElement("c:BaseRate");
      t3BaseRate.appendChild(document.createTextNode(xmlVO.getTier3BaseRate()));
      tier3.appendChild(t3BaseRate);
    }
    if (!xmlVO.getTier3DiffRate().equalsIgnoreCase(""))
    {
      Element t3DiffRate = document.createElement("c:DifferentialRate");
      t3DiffRate.appendChild(document.createTextNode(xmlVO.getTier3DiffRate()));
      tier3.appendChild(t3DiffRate);
    }
    if (!xmlVO.getTier3Interest().equalsIgnoreCase(""))
    {
      Element t3Intr = document.createElement("c:Interest");
      t3Intr.appendChild(document.createTextNode(xmlVO.getTier3Interest()));
      tier3.appendChild(t3Intr);
    }
    if (!xmlVO.getTier3Percent().equalsIgnoreCase(""))
    {
      Element t3Percent = document.createElement("c:Percent");
      t3Percent.appendChild(document.createTextNode(xmlVO.getTier3Percent()));
      tier3.appendChild(t3Percent);
    }
    Element tier4 = document.createElement("m:Tier4");
    rocdoc01.appendChild(tier4);
   
    Element t4Charge = document.createElement("c:ChargeTierBasic");
    tier4.appendChild(t4Charge);
    if (!xmlVO.getTier4Amt().equalsIgnoreCase(""))
    {
      Element t4Amt = document.createElement("c:Amount");
      t4Amt.appendChild(document.createTextNode(xmlVO.getTier4Amt()));
      t4Charge.appendChild(t4Amt);
    }
    if (!xmlVO.getTier4MinAmt().equalsIgnoreCase(""))
    {
      Element t4MinAmt = document.createElement("c:MinAmount");
      t4MinAmt.appendChild(document.createTextNode(xmlVO.getTier4MinAmt()));
      t4Charge.appendChild(t4MinAmt);
    }
    if (!xmlVO.getTier4MaxAmt().equalsIgnoreCase(""))
    {
      Element t4MaxAmt = document.createElement("c:MaxAmount");
      t4MaxAmt.appendChild(document.createTextNode(xmlVO.getTier4MaxAmt()));
      t4Charge.appendChild(t4MaxAmt);
    }
    if ((!xmlVO.getTier4Days().equalsIgnoreCase("")) && (!xmlVO.getTier4Period().equalsIgnoreCase("")))
    {
      Element t4Period = document.createElement("c:Period");
      t4Charge.appendChild(t4Period);
     
      Element t4TendorDay = document.createElement("c:TenorDays");
      t4TendorDay.appendChild(document.createTextNode(xmlVO.getTier4Days()));
      t4Period.appendChild(t4TendorDay);
     
      Element t4TendorPeriod = document.createElement("c:TenorPeriod");
      t4TendorPeriod.appendChild(document.createTextNode(xmlVO.getTier4Period()));
      t4Period.appendChild(t4TendorPeriod);
    }
    if (!xmlVO.getTier4Freq().equalsIgnoreCase(""))
    {
      Element t4Freq = document.createElement("c:ChargeFrequency");
      t4Freq.appendChild(document.createTextNode(xmlVO.getTier4Freq()));
      t4Charge.appendChild(t4Freq);
    }
    if (!xmlVO.getTier4Amount().equalsIgnoreCase(""))
    {
      Element t4Amount = document.createElement("c:Amount");
      t4Amount.appendChild(document.createTextNode(xmlVO.getTier4Amount()));
      tier4.appendChild(t4Amount);
    }
    if (!xmlVO.getTier4BaseRate().equalsIgnoreCase(""))
    {
      Element t4BaseRate = document.createElement("c:BaseRate");
      t4BaseRate.appendChild(document.createTextNode(xmlVO.getTier4BaseRate()));
      tier4.appendChild(t4BaseRate);
    }
    if (!xmlVO.getTier4DiffRate().equalsIgnoreCase(""))
    {
      Element t4DiffRate = document.createElement("c:DifferentialRate");
      t4DiffRate.appendChild(document.createTextNode(xmlVO.getTier4DiffRate()));
      tier4.appendChild(t4DiffRate);
    }
    if (!xmlVO.getTier4Interest().equalsIgnoreCase(""))
    {
      Element t4Intr = document.createElement("c:Interest");
      t4Intr.appendChild(document.createTextNode(xmlVO.getTier4Interest()));
      tier4.appendChild(t4Intr);
    }
    if (!xmlVO.getTier4Percent().equalsIgnoreCase(""))
    {
      Element t4Percent = document.createElement("c:Percent");
      t4Percent.appendChild(document.createTextNode(xmlVO.getTier4Percent()));
      tier4.appendChild(t4Percent);
    }
    if (!xmlVO.getStartDate().equalsIgnoreCase(""))
    {
      Element startDate = document.createElement("m:StartDate");
      startDate.appendChild(document.createTextNode(xmlVO.getStartDate()));
      rocdoc01.appendChild(startDate);
    }
    if (!xmlVO.getExpiryDate().equalsIgnoreCase(""))
    {
      Element expDate = document.createElement("m:ExpiryDate");
      expDate.appendChild(document.createTextNode(xmlVO.getExpiryDate()));
      rocdoc01.appendChild(expDate);
    }
    String xmlStr = convertDocumentToString(document);
   
    logger.info("XML File-------->" + xmlStr);
    xmlStr = xmlStr.replaceAll("\\<\\?xml(.+?)\\?\\>", "").trim();
    logger.info("XML Filee-------->" + xmlStr);
    return xmlStr;
  }
 
  private static String convertDocumentToString(Document doc)
  {
    TransformerFactory tf = TransformerFactory.newInstance();
    try
    {
      Transformer transformer = tf.newTransformer();
     
      transformer.setOutputProperty("omit-xml-declaration", "yes");
      StringWriter writer = new StringWriter();
      transformer.transform(new DOMSource(doc), new StreamResult(writer));
      return writer.getBuffer().toString();
    }
    catch (TransformerException e)
    {
      e.printStackTrace();
    }
    return null;
  }
 
  private int createScheduleValidation(ChargeScheduleVO chargeVO)
  {
    int flag = 1;
    if ((this.alertMsgArray != null) &&
      (this.alertMsgArray.size() > 0)) {
      this.alertMsgArray.clear();
    }
    if (CommonMethods.isNull(chargeVO.getCustomerCif()))
    {
      getErrors("2", chargeVO);
      flag = 0;
    }
    if (CommonMethods.isNull(chargeVO.getChargeType()))
    {
      getErrors("3", chargeVO);
      flag = 0;
    }
    if ((flag == 1) &&
      (findChargeType(chargeVO) == 0))
    {
      getErrors("4", chargeVO);
      flag = 0;
    }
    return flag;
  }
 
  private int findChargeType(ChargeScheduleVO chargeVO)
  {
    logger.info("Entering Method");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    String query = null;
    int flag = 1;
    try
    {
      con = DBConnectionUtility.getConnection();
      query = "SELECT R.ID, R.DESCR, R.KEY97,C.CUS_MNM FROM ETT_CHARGE99_DET C,RELTEMPLTE R WHERE R.ID=C.CHG_TYPE AND C.OBSOLETE='N'  AND R.TYPEFLAG=10136 AND  TRIM(C.CUS_MNM)=? AND R.ID=?";
      pst = new LoggableStatement(con, query);
      pst.setString(1, chargeVO.getCustomerCif().trim());
      pst.setString(2, chargeVO.getChargeId().trim());
      logger.info(pst.getQueryString());
      rs = pst.executeQuery();
      while (rs.next()) {
        flag = 0;
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
    return flag;
  }
 
  public ArrayList<ChargeScheduleVO> updatingChargeSchedule(ArrayList<ChargeScheduleVO> chargeScheduleList, ChargeScheduleVO chargeVO)
  {
    logger.info("Entering Method");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    String query = null;
    ChargeXmlVO xmlVO = null;
    try
    {
      chargeScheduleList = new ArrayList();
      con = DBConnectionUtility.getConnection();
     
      HttpSession session = ServletActionContext.getRequest().getSession();
     
      String userId = (String)session.getAttribute("USERID");
     










































































































































      updateToETTCharge(chargeVO.getUpdateChargeKey97().trim(), chargeVO.getUpdateCusCif().trim(), userId);
     












      query = "SELECT R.ID, R.DESCR, R.KEY97, E.CODE79, C.CUS_MNM FROM CHARGE99 C, EXEMPL30 E, RELTEMPLTE R WHERE R.key97=C.CHG_TYPE AND C.OBSOLETE='N' AND R.TYPEFLAG=10136 AND TRIM(C.CUS_MNM)=? AND R.ID=?";
     







      pst = new LoggableStatement(con, query);
      pst.setString(1, chargeVO.getUpdateCusCif().trim());
      pst.setString(2, chargeVO.getUpdateChargeId().trim());
      logger.info(pst.getQueryString());
      ResultSet rset = pst.executeQuery();
      while (rset.next())
      {
        ChargeScheduleVO scheduleVO = new ChargeScheduleVO();
        scheduleVO.setChargeId(CommonMethods.nullAndTrimString(rset.getString("ID")));
        scheduleVO.setChargeDesc(CommonMethods.nullAndTrimString(rset.getString("descr")));
        scheduleVO.setChargeKey97(CommonMethods.nullAndTrimString(rset.getString("KEY97")));
        scheduleVO.setCustomerCif(CommonMethods.nullAndTrimString(rset.getString("CUS_MNM")));
        scheduleVO.setProductId("");
        chargeScheduleList.add(scheduleVO);
      }
      chargeVO.setUpdateChargeId("");
      chargeVO.setUpdateChargeKey97("");
      chargeVO.setUpdateCusCif("");
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
    return chargeScheduleList;
  }
 
  private int updateScheduleValidation(ChargeScheduleVO chargeVO)
  {
    int flag = 1;
    if ((this.alertMsgArray != null) &&
      (this.alertMsgArray.size() > 0)) {
      this.alertMsgArray.clear();
    }
    if (CommonMethods.isNull(chargeVO.getCustomerCif()))
    {
      getErrors("2", chargeVO);
      flag = 0;
    }
    if (CommonMethods.isNull(chargeVO.getChargeType()))
    {
      getErrors("3", chargeVO);
      flag = 0;
    }
    return flag;
  }
 
  public ArrayList<ChargeScheduleVO> deletingChargeSchedule(ArrayList<ChargeScheduleVO> chargeScheduleList, ChargeScheduleVO chargeVO)
  {
    logger.info("Entering Method");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    String query = null;
    ChargeXmlVO xmlVO = null;
    try
    {
      chargeScheduleList = new ArrayList();
      con = DBConnectionUtility.getConnection();
      HttpSession session = ServletActionContext.getRequest().getSession();
     
      String userId = (String)session.getAttribute("USERID");
     










































































































































      deleteToETTCharge(chargeVO.getUpdateChargeKey97().trim(), chargeVO.getUpdateCusCif().trim(), userId);
     

































      chargeVO.setUpdateChargeId("");
      chargeVO.setUpdateChargeKey97("");
      chargeVO.setUpdateCusCif("");
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
    return chargeScheduleList;
  }
 
  public String pushToETTCharge(String key97, String customerCIF, String productid, String userId, String id)
  {
    logger.info("Entering Method");
    Connection con = null;
    ResultSet rs = null;
    LoggableStatement ps = null;
    String lowRange = getSequenceAPISERVER();
    try
    {
      con = DBConnectionUtility.getConnection();
      String insert_apiserver = "INSERT INTO ETT_CHARGE99_DET(KEY97, CUS_SBB, CUS_MNM, BRANCH, CHRG_GRP, SCHED_TYPE, CHG_TYPE1, NARRATIVE, T1_AMT, T1_UNIT, T1_NO, T1_FRQ, T1_MINAMT, T1_MAXAMT,   T2_AMT, T2_UNIT, T2_NO, T2_FRQ, T2_MINAMT, T2_MAXAMT, T3_AMT, T3_UNIT, T3_NO, T3_FRQ, T3_MINAMT, T3_MAXAMT, T4_AMT, T4_UNIT, T4_NO,   T4_FRQ, T4_MINAMT, T4_MAXAMT, CCY, OVERALLMAX, OVERALLMIN, PERD_AMT, SPLIT, CHGS_WAIVE, MAX_CHGS, PERDMINMAX, PERIODIC, ADV_ARR, P_CHG_UNIT,  P_CHG_NO, COPYFRMSTR, SPECIAL_PS, OBSOLETE, TIERAMT1, TIERAMT2, TIERAMT3, TIERAMT4, WHEN_COLL, INTFREQ, IDB, INT_TYPE, MINPERUNIT, MINPERNO,  T1_BASE, T2_BASE, T3_BASE, T4_BASE, T1_DIFF, T1_DIFFDTE, T2_DIFF, T2_DIFFDTE, T3_DIFF, T3_DIFFDTE, T4_DIFF, T4_DIFFDTE, T1_INTRATE,  T2_INTRATE, T3_INTRATE, T4_INTRATE, T1_PERCENT, T2_PERCENT, T3_PERCENT, T4_PERCENT, FULLCYCLE, ANNUAL_CHG, ANNUAL_PST, MINMAX_CCY,  CYCLENOAPP, MULTIPLYBY, EXPDTE, STRDTE, TYPEFLAG, TSTAMP,STATUS,OPTIONS,MAKER_USERID,CODE79,CHG_TYPE)  SELECT KEY97, CUS_SBB,?, BRANCH, CHRG_GRP, SCHED_TYPE, CHG_TYPE, NARRATIVE, T1_AMT, T1_UNIT, T1_NO, T1_FRQ, T1_MINAMT, T1_MAXAMT,   T2_AMT, T2_UNIT, T2_NO, T2_FRQ, T2_MINAMT, T2_MAXAMT, T3_AMT, T3_UNIT, T3_NO, T3_FRQ, T3_MINAMT, T3_MAXAMT, T4_AMT, T4_UNIT, T4_NO,   T4_FRQ, T4_MINAMT, T4_MAXAMT, CCY, OVERALLMAX, OVERALLMIN, PERD_AMT, SPLIT, CHGS_WAIVE, MAX_CHGS, PERDMINMAX, PERIODIC, ADV_ARR, P_CHG_UNIT,  P_CHG_NO, COPYFRMSTR, SPECIAL_PS, OBSOLETE, TIERAMT1, TIERAMT2, TIERAMT3, TIERAMT4, WHEN_COLL, INTFREQ, IDB, INT_TYPE, MINPERUNIT, MINPERNO,  T1_BASE, T2_BASE, T3_BASE, T4_BASE, T1_DIFF, T1_DIFFDTE, T2_DIFF, T2_DIFFDTE, T3_DIFF, T3_DIFFDTE, T4_DIFF, T4_DIFFDTE, T1_INTRATE,  T2_INTRATE, T3_INTRATE, T4_INTRATE, T1_PERCENT, T2_PERCENT, T3_PERCENT, T4_PERCENT, FULLCYCLE, ANNUAL_CHG, ANNUAL_PST, MINMAX_CCY,  CYCLENOAPP, MULTIPLYBY, EXPDTE, STRDTE, TYPEFLAG, TSTAMP,'MA','A',?,?,? from CHARGE99  WHERE OBSOLETE='N' AND TRIM(CUS_MNM) IS NOT NULL AND ROWNUM=1 AND CHG_TYPE = ?";
     















      ps = new LoggableStatement(con, insert_apiserver);
      ps.setString(1, customerCIF);
      ps.setString(2, userId);
      ps.setString(3, productid);
      ps.setString(4, id);
      ps.setString(5, key97);
      logger.info("Query " + ps.getQueryString());
      ps.executeUpdate();
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, ps, rs);
    }
    logger.info("Exiting Method");
    return lowRange;
  }
 
  public String updateToETTCharge(String key97, String customerCIF, String userId)
  {
    logger.info("Entering Method");
    Connection con = null;
    ResultSet rs = null;
    LoggableStatement ps = null;
    String lowRange = getSequenceAPISERVER();
    try
    {
      con = DBConnectionUtility.getConnection();
      String insert_apiserver = "UPDATE ETT_CHARGE99_DET SET(KEY97, CUS_SBB, CUS_MNM, BRANCH, CHRG_GRP, SCHED_TYPE, CHG_TYPE1, NARRATIVE, T1_AMT, T1_UNIT, T1_NO, T1_FRQ, T1_MINAMT, T1_MAXAMT,   T2_AMT, T2_UNIT, T2_NO, T2_FRQ, T2_MINAMT, T2_MAXAMT, T3_AMT, T3_UNIT, T3_NO, T3_FRQ, T3_MINAMT, T3_MAXAMT, T4_AMT, T4_UNIT, T4_NO,   T4_FRQ, T4_MINAMT, T4_MAXAMT, CCY, OVERALLMAX, OVERALLMIN, PERD_AMT, SPLIT, CHGS_WAIVE, MAX_CHGS, PERDMINMAX, PERIODIC, ADV_ARR, P_CHG_UNIT,  P_CHG_NO, COPYFRMSTR, SPECIAL_PS, OBSOLETE, TIERAMT1, TIERAMT2, TIERAMT3, TIERAMT4, WHEN_COLL, INTFREQ, IDB, INT_TYPE, MINPERUNIT, MINPERNO,  T1_BASE, T2_BASE, T3_BASE, T4_BASE, T1_DIFF, T1_DIFFDTE, T2_DIFF, T2_DIFFDTE, T3_DIFF, T3_DIFFDTE, T4_DIFF, T4_DIFFDTE, T1_INTRATE,  T2_INTRATE, T3_INTRATE, T4_INTRATE, T1_PERCENT, T2_PERCENT, T3_PERCENT, T4_PERCENT, FULLCYCLE, ANNUAL_CHG, ANNUAL_PST, MINMAX_CCY,  CYCLENOAPP, MULTIPLYBY, EXPDTE, STRDTE, TYPEFLAG, TSTAMP,STATUS,OPTIONS,MAKER_USERID,MAKER_TIMESTAMP)= (SELECT KEY97, CUS_SBB,?, BRANCH, CHRG_GRP, SCHED_TYPE, CHG_TYPE, NARRATIVE, T1_AMT, T1_UNIT, T1_NO, T1_FRQ, T1_MINAMT, T1_MAXAMT,   T2_AMT, T2_UNIT, T2_NO, T2_FRQ, T2_MINAMT, T2_MAXAMT, T3_AMT, T3_UNIT, T3_NO, T3_FRQ, T3_MINAMT, T3_MAXAMT, T4_AMT, T4_UNIT, T4_NO,   T4_FRQ, T4_MINAMT, T4_MAXAMT, CCY, OVERALLMAX, OVERALLMIN, PERD_AMT, SPLIT, CHGS_WAIVE, MAX_CHGS, PERDMINMAX, PERIODIC, ADV_ARR, P_CHG_UNIT,  P_CHG_NO, COPYFRMSTR, SPECIAL_PS, OBSOLETE, TIERAMT1, TIERAMT2, TIERAMT3, TIERAMT4, WHEN_COLL, INTFREQ, IDB, INT_TYPE, MINPERUNIT, MINPERNO,  T1_BASE, T2_BASE, T3_BASE, T4_BASE, T1_DIFF, T1_DIFFDTE, T2_DIFF, T2_DIFFDTE, T3_DIFF, T3_DIFFDTE, T4_DIFF, T4_DIFFDTE, T1_INTRATE,  T2_INTRATE, T3_INTRATE, T4_INTRATE, T1_PERCENT, T2_PERCENT, T3_PERCENT, T4_PERCENT, FULLCYCLE, ANNUAL_CHG, ANNUAL_PST, MINMAX_CCY,  CYCLENOAPP, MULTIPLYBY, EXPDTE, STRDTE, TYPEFLAG, TSTAMP,'MA','F',?,SYSTIMESTAMP from CHARGE99 C WHERE OBSOLETE='N' AND TRIM(CUS_MNM) IS NOT NULL AND ROWNUM=1 AND CHG_TYPE1 =?) WHERE CUS_MNM =? and CHG_TYPE1 = ?";
     
















      ps = new LoggableStatement(con, insert_apiserver);
      ps.setString(1, customerCIF);
      ps.setString(2, userId);
      ps.setString(3, key97);
      ps.setString(4, customerCIF);
      ps.setString(5, key97);
      logger.info("Query " + ps.getQueryString());
      ps.executeUpdate();
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, ps, rs);
    }
    logger.info("Exiting Method");
    return lowRange;
  }
 
  public String deleteToETTCharge(String key97, String customerCIF, String userId)
  {
    logger.info("Entering Method");
    Connection con = null;
    ResultSet rs = null;
    LoggableStatement ps = null;
    String lowRange = getSequenceAPISERVER();
    try
    {
      con = DBConnectionUtility.getConnection();
      String insert_apiserver = "UPDATE ETT_CHARGE99_DET SET(KEY97, CUS_SBB, CUS_MNM, BRANCH, CHRG_GRP, SCHED_TYPE, CHG_TYPE1, NARRATIVE, T1_AMT, T1_UNIT, T1_NO, T1_FRQ, T1_MINAMT, T1_MAXAMT,   T2_AMT, T2_UNIT, T2_NO, T2_FRQ, T2_MINAMT, T2_MAXAMT, T3_AMT, T3_UNIT, T3_NO, T3_FRQ, T3_MINAMT, T3_MAXAMT, T4_AMT, T4_UNIT, T4_NO,   T4_FRQ, T4_MINAMT, T4_MAXAMT, CCY, OVERALLMAX, OVERALLMIN, PERD_AMT, SPLIT, CHGS_WAIVE, MAX_CHGS, PERDMINMAX, PERIODIC, ADV_ARR, P_CHG_UNIT,  P_CHG_NO, COPYFRMSTR, SPECIAL_PS, OBSOLETE, TIERAMT1, TIERAMT2, TIERAMT3, TIERAMT4, WHEN_COLL, INTFREQ, IDB, INT_TYPE, MINPERUNIT, MINPERNO,  T1_BASE, T2_BASE, T3_BASE, T4_BASE, T1_DIFF, T1_DIFFDTE, T2_DIFF, T2_DIFFDTE, T3_DIFF, T3_DIFFDTE, T4_DIFF, T4_DIFFDTE, T1_INTRATE,  T2_INTRATE, T3_INTRATE, T4_INTRATE, T1_PERCENT, T2_PERCENT, T3_PERCENT, T4_PERCENT, FULLCYCLE, ANNUAL_CHG, ANNUAL_PST, MINMAX_CCY,  CYCLENOAPP, MULTIPLYBY, EXPDTE, STRDTE, TYPEFLAG, TSTAMP,STATUS,OPTIONS,MAKER_USERID,MAKER_TIMESTAMP)= (SELECT KEY97, CUS_SBB,?, BRANCH, CHRG_GRP, SCHED_TYPE, CHG_TYPE, NARRATIVE, T1_AMT, T1_UNIT, T1_NO, T1_FRQ, T1_MINAMT, T1_MAXAMT,   T2_AMT, T2_UNIT, T2_NO, T2_FRQ, T2_MINAMT, T2_MAXAMT, T3_AMT, T3_UNIT, T3_NO, T3_FRQ, T3_MINAMT, T3_MAXAMT, T4_AMT, T4_UNIT, T4_NO,   T4_FRQ, T4_MINAMT, T4_MAXAMT, CCY, OVERALLMAX, OVERALLMIN, PERD_AMT, SPLIT, CHGS_WAIVE, MAX_CHGS, PERDMINMAX, PERIODIC, ADV_ARR, P_CHG_UNIT,  P_CHG_NO, COPYFRMSTR, SPECIAL_PS, OBSOLETE, TIERAMT1, TIERAMT2, TIERAMT3, TIERAMT4, WHEN_COLL, INTFREQ, IDB, INT_TYPE, MINPERUNIT, MINPERNO,  T1_BASE, T2_BASE, T3_BASE, T4_BASE, T1_DIFF, T1_DIFFDTE, T2_DIFF, T2_DIFFDTE, T3_DIFF, T3_DIFFDTE, T4_DIFF, T4_DIFFDTE, T1_INTRATE,  T2_INTRATE, T3_INTRATE, T4_INTRATE, T1_PERCENT, T2_PERCENT, T3_PERCENT, T4_PERCENT, FULLCYCLE, ANNUAL_CHG, ANNUAL_PST, MINMAX_CCY,  CYCLENOAPP, MULTIPLYBY, EXPDTE, STRDTE, TYPEFLAG, TSTAMP,'D','D',?,SYSTIMESTAMP from CHARGE99 C WHERE OBSOLETE='N' AND TRIM(CUS_MNM) IS NOT NULL AND ROWNUM=1 AND CHG_TYPE1 =?) WHERE CUS_MNM = ? and CHG_TYPE1 = ?";
     
















      ps = new LoggableStatement(con, insert_apiserver);
      ps.setString(1, customerCIF);
      ps.setString(2, userId);
      ps.setString(3, key97);
      ps.setString(4, customerCIF);
      ps.setString(5, key97);
      logger.info("Query " + ps.getQueryString());
      ps.executeUpdate();
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, ps, rs);
    }
    logger.info("Exiting Method");
    return lowRange;
  }
 
  public boolean executeGenericQuery(String query, String parameters)
  {
    Connection connection = null;
    LoggableStatement loggableStatement = null;
    ResultSet resultSet = null;
    try
    {
      int parameterCount = 1;
      connection = DBConnectionUtility.getConnection();
      loggableStatement = new LoggableStatement(connection, query);
      if (((parameters instanceof String)) && (!parameters.trim().equalsIgnoreCase(""))) {
        for (String invidualParameters : parameters.split("\\|"))
        {
          loggableStatement.setString(parameterCount, invidualParameters);
          parameterCount++;
        }
      }
      logger.info("Query -----------Currency " + loggableStatement.getQueryString());
      resultSet = loggableStatement.executeQuery();
      while (resultSet.next()) {
        if (resultSet.getInt(1) > 0) {
          return true;
        }
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(connection, loggableStatement, resultSet);
    }
    return false;
  }
 
  public ChargeScheduleVO purchaseOrderValidations(ChargeScheduleVO chargeVO)
  {
    try
    {
      if ((this.alertMsgArray != null) &&
        (this.alertMsgArray.size() > 0)) {
        this.alertMsgArray.clear();
      }
      if (((chargeVO.getPoValue() instanceof String)) && (!chargeVO.getPoValue().trim().equalsIgnoreCase("")))
      {
        logger.info("Currency before------------>" + chargeVO.getPoValue().trim());
        String currency = chargeVO.getPoValue().trim().replaceAll("[^A-Za-z]+", "");
        logger.info("Currency after------------>" + currency);
        if ((!(currency instanceof String)) || (currency.trim().equalsIgnoreCase(""))) {
          setErrorForPurchaseDetails("CURRENCY_NULL", chargeVO);
        } else if (!executeGenericQuery("select COUNT(1) from c8pf where TRIM(C8CCY)=?", currency.trim())) {
          setErrorForPurchaseDetails("INVALID_CURRENCY", chargeVO);
        }
        String amount = chargeVO.getPoValue().trim().replaceAll("[^0-9.]", "");
        if (((amount instanceof String)) && (!amount.trim().equalsIgnoreCase("")))
        {
          if (Double.valueOf(amount).doubleValue() <= 0.0D) {
            setErrorForPurchaseDetails("AMOUNT_VALUE", chargeVO);
          }
        }
        else {
          setErrorForPurchaseDetails("AMOUNT_NULL", chargeVO);
        }
      }
      else
      {
        setErrorForPurchaseDetails("AMOUNT_NULL", chargeVO);
      }
      if ((chargeVO.getExportOrderNumber() == null) || (chargeVO.getExportOrderNumber().equalsIgnoreCase(""))) {
        getErrors("16", chargeVO);
      }
      if ((chargeVO.getExporterOrderDate() == null) || (chargeVO.getExporterOrderDate().equalsIgnoreCase(""))) {
        getErrors("19", chargeVO);
      }
      if ((chargeVO.getLastShipmentDate() == null) || (chargeVO.getLastShipmentDate().equalsIgnoreCase(""))) {
        getErrors("21", chargeVO);
      }
      if ((chargeVO.getCifID() == null) || (chargeVO.getCifID().equalsIgnoreCase(""))) {
        getErrors("0", chargeVO);
      }
      if ((chargeVO.getImporterName() == null) || (chargeVO.getImporterName().equalsIgnoreCase(""))) {
        getErrors("18", chargeVO);
      }
      if ((chargeVO.getExportexpiryDate() == null) || (chargeVO.getExportexpiryDate().equalsIgnoreCase(""))) {
        setErrorForPurchaseDetails("EXPIRY_DATE_NULL", chargeVO);
      }
      if ((chargeVO.getBeneficiaryName() != null) && (!chargeVO.getBeneficiaryName().isEmpty()) &&
        (chargeVO.getImporterName() != null) && (!chargeVO.getImporterName().isEmpty()) &&
        (chargeVO.getBeneficiaryName().trim().equalsIgnoreCase(chargeVO.getImporterName().trim()))) {
        getErrors("31", chargeVO);
      }
      if ((chargeVO.getExportexpiryDate() != null) && (!chargeVO.getExportexpiryDate().isEmpty()))
      {
        boolean exporyDateValidationResult = isValidExpiryDate(chargeVO.getExportexpiryDate(),
          getTICurrentDate());
        if (!exporyDateValidationResult)
        {
          logger.info("Expiry Date cannot be past date");
          getErrors("32", chargeVO);
        }
      }
      SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
      if (((chargeVO.getExporterOrderDate() instanceof String)) &&
        (!chargeVO.getExporterOrderDate().trim().equalsIgnoreCase("")) &&
        ((chargeVO.getLastShipmentDate() instanceof String)) &&
        (!chargeVO.getLastShipmentDate().trim().equalsIgnoreCase("")))
      {
        logger.info("exportOrderDate = " + chargeVO.getExporterOrderDate());
        logger.info("lastShipmentDate = " + chargeVO.getLastShipmentDate());
        Date exporterOrderDate = format.parse(chargeVO.getExporterOrderDate());
        Date tiDate = format.parse(getTICurrentDate());
        Date date = new Date();
        String curDate = format.format(date);
        Date currentDate = format.parse(curDate);
        Date lastShipmentDate = format.parse(chargeVO.getLastShipmentDate());
        Date expiryDate = format.parse(chargeVO.getExportexpiryDate());
       
        logger.info("Date = " + currentDate);
        logger.info("TICurrentDate = " + getTICurrentDate());
        logger.info("exportexpiryDate = " + chargeVO.getExportexpiryDate());
        logger.info("exportOrderDate = " + chargeVO.getExporterOrderDate());
        logger.info("lastShipmentDate = " + chargeVO.getLastShipmentDate());
        if (exporterOrderDate.after(lastShipmentDate)) {
          getErrors("26", chargeVO);
        } else if (exporterOrderDate.after(tiDate)) {
          getErrors("28", chargeVO);
        }
        if (lastShipmentDate.before(exporterOrderDate)) {
          getErrors("27", chargeVO);
        }
        if (lastShipmentDate.after(expiryDate)) {
          getErrors("35", chargeVO);
        }
      }
      if (executeGenericQuery("SELECT COUNT(1) FROM ETT_EXPORT_ORDER WHERE TRIM(EXPORT_ORDER_NUMBER)= ?  AND TRIM(CIF_ID)= ? AND UPPER(TRIM(STATUS)) !='R' ", chargeVO.getExportOrderNumber().trim() + "|" + chargeVO.getCifID().trim())) {
        getErrors("12", chargeVO);
      }
      if (((chargeVO.getCifID() instanceof String)) && (!chargeVO.getCifID().trim().equalsIgnoreCase("")) &&
        (!executeGenericQuery("select COUNT(1) from GFPF  WHERE TRIM(GFCPNC) = ?", chargeVO.getCifID().trim()))) {
        setErrorForPurchaseDetails("INVALID_CUSTOMER", chargeVO);
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return chargeVO;
  }
 
  public ChargeScheduleVO insertExport(ChargeScheduleVO chargeVO)
    throws DAOException
  {
    Connection connection = null;
    LoggableStatement loggableStatement = null;
    ResultSet resultSet = null;
    CommonMethods commonMethods = null;
    String userID = "";
    HttpSession session = ServletActionContext.getRequest().getSession();
   
    HttpServletRequest request = (HttpServletRequest)ActionContext.getContext().get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
    Checkking_Valid_User usr = new Checkking_Valid_User();
    try
    {
      logger.info("Eligible Amount-------------------------------" + chargeVO.getEligibleAmount().trim());
      purchaseOrderValidations(chargeVO);
      if (this.alertMsgArray.size() == 0)
      {
        userID = getUserID();
        logger.info("Maker User ID--------------------------->" + userID);
       
        String currency = chargeVO.getPoValue().trim().replaceAll("[^A-Za-z]+", "");
        String amount = chargeVO.getPoValue().trim().replaceAll("[^0-9.]", "");
        commonMethods = new CommonMethods();
        if (executeGenericQuery("SELECT COUNT(1) FROM ETT_EXPORT_ORDER WHERE TRIM(EXPORT_ORDER_NUMBER)= ?  AND TRIM(CIF_ID)= ? AND UPPER(TRIM(STATUS)) !='A' ", chargeVO.getExportOrderNumber().trim() + "|" + chargeVO.getCifID().trim()))
        {
          logger.info("Update");
          connection = DBConnectionUtility.getConnection();
          loggableStatement = new LoggableStatement(connection, "UPDATE ETT_EXPORT_ORDER SET EXPORT_ORDER_DATE=to_date(?,'dd/mm/yyyy'),BENEFICIARY_NAME=?,INCO_TERMS=?,GOODS_CODE=?,PO_VALUE=?,CURRENCY=?,EXPIRY_DATE=to_date(?,'dd/mm/yyyy'),LAST_SHIPMENT_DATE=to_date(?,'dd/mm/yyyy'),PERCENTAGE_FREIGHT_DEDUCTION=?,PERCENTAGE_INSURANCE_DEDUCTION=?,MARGIN_PERCENTAGE=?,ELIGIBLE_AMOUNT=?,IMPORTER_NAME=?,USER_ID=?,MAKER_TIMESTAMP=SYSTIMESTAMP,STATUS=?,GOODS_DESC=?,INWARD_NO=?,NTP=?,DUE_DATE=to_date(?,'dd/mm/yyyy'),SHPMODE=? WHERE EXPORT_ORDER_NUMBER=? AND CIF_ID=? ");
          loggableStatement.setString(1, chargeVO.getExporterOrderDate());
          loggableStatement.setString(2, chargeVO.getBeneficiaryName());
          loggableStatement.setString(3, chargeVO.getIncoTerms());
          loggableStatement.setString(4, chargeVO.getGoodsCode());
          loggableStatement.setString(5, amount);
          loggableStatement.setString(6, currency);
         
          loggableStatement.setString(7, chargeVO.getExportexpiryDate());
          loggableStatement.setString(8, chargeVO.getLastShipmentDate());
         
          loggableStatement.setString(9, chargeVO.getFreightDeduction());
          loggableStatement.setString(10, chargeVO.getInsuranceDeduction());
          loggableStatement.setString(11, chargeVO.getMarginPercentage());
         
          String elgAmount = chargeVO.getEligibleAmount().trim();
         
          elgAmount = elgAmount.substring(0, elgAmount.length() - 3);
          elgAmount = elgAmount.trim();
         
          loggableStatement.setString(12, elgAmount);
         
          loggableStatement.setString(13, chargeVO.getImporterName());
          loggableStatement.setString(14, userID);
          loggableStatement.setString(15, "P");
          loggableStatement.setString(16, chargeVO.getGoodsDescription());
          loggableStatement.setString(17, chargeVO.getInwardNo());
          loggableStatement.setString(18, commonMethods.getEmptyIfNull(chargeVO.getNtPeriod()).trim());
          loggableStatement.setString(19, commonMethods.getEmptyIfNull(chargeVO.getDueDate()).trim());
          loggableStatement.setString(20, commonMethods.getEmptyIfNull(chargeVO.getShpMode()).trim());
          loggableStatement.setString(21, commonMethods.getEmptyIfNull(chargeVO.getExportOrderNumber()).trim());
          loggableStatement.setString(22, commonMethods.getEmptyIfNull(chargeVO.getCifID()).trim());
         
          int count = loggableStatement.executeUpdate();
          if (count > 0)
          {
            logger.info("Updated Successfully");
            chargeVO.setCount(count);
          }
        }
        else
        {
          logger.info("Insert");
          connection = DBConnectionUtility.getConnection();
          loggableStatement = new LoggableStatement(connection, "insert into ett_export_order(Export_Order_date,Beneficiary_Name,Inco_Terms,Goods_Code,PO_Value,Currency,Expiry_Date,Last_Shipment_Date,Percentage_Freight_Deduction,Percentage_Insurance_Deduction,Margin_percentage,Eligible_amount,Importer_Name,user_id,maker_timestamp,status,GOODS_DESC,INWARD_NO,Export_Order_Number,CIF_ID,NTP,DUE_DATE,SHPMODE) values(to_date(?,'dd/mm/yyyy'),?,?,?,?,?,to_date(?,'dd/mm/yyyy'),to_date(?,'dd/mm/yyyy'),?,?,?,?,?,?,SYSTIMESTAMP,?,?,?,?,?,?,to_date(?,'dd/mm/yyyy'))");
          loggableStatement.setString(1, chargeVO.getExporterOrderDate());
          loggableStatement.setString(2, chargeVO.getBeneficiaryName());
          loggableStatement.setString(3, chargeVO.getIncoTerms());
          loggableStatement.setString(4, chargeVO.getGoodsCode());
          loggableStatement.setString(5, amount);
          loggableStatement.setString(6, currency);
         
          loggableStatement.setString(7, chargeVO.getExportexpiryDate());
          loggableStatement.setString(8, chargeVO.getLastShipmentDate());
         
          loggableStatement.setString(9, chargeVO.getFreightDeduction());
          loggableStatement.setString(10, chargeVO.getInsuranceDeduction());
          loggableStatement.setString(11, chargeVO.getMarginPercentage());
          try
          {
            String elgAmount = chargeVO.getEligibleAmount();
            logger.info("elgAmount = " + elgAmount + currency);
            loggableStatement.setString(12, elgAmount);
          }
          catch (Exception exp)
          {
            logger.info("getEligibleAmount Exception" + exp);
          }
          loggableStatement.setString(13, chargeVO.getImporterName());
          loggableStatement.setString(14, userID);
          loggableStatement.setString(15, "P");
          loggableStatement.setString(16, chargeVO.getGoodsDescription());
          loggableStatement.setString(17, chargeVO.getInwardNo());
          loggableStatement.setString(18, commonMethods.getEmptyIfNull(chargeVO.getExportOrderNumber()).trim());
          loggableStatement.setString(19, commonMethods.getEmptyIfNull(chargeVO.getCifID()).trim());
          loggableStatement.setString(20, commonMethods.getEmptyIfNull(chargeVO.getNtPeriod()).trim());
          loggableStatement.setString(21, commonMethods.getEmptyIfNull(chargeVO.getDueDate()).trim());
          loggableStatement.setString(22, commonMethods.getEmptyIfNull(chargeVO.getShpMode()).trim());
          logger.info("Maker Query -------------------->" + loggableStatement);
         
          logger.info("Insert Query----------------->" + loggableStatement.getQueryString());
         
          int count = loggableStatement.executeUpdate();
          if (count > 0)
          {
            logger.info("Inserted Successfully");
            chargeVO.setCount(count);
          }
        }
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      throwDAOException(e);
    }
    return chargeVO;
  }
 
  public ChargeScheduleVO insertExport1(ChargeScheduleVO chargeVO)
    throws DAOException
  {
    Connection con = null;
    ResultSet rs = null;
    LoggableStatement ps = null;
    String query = null;
    String query1 = null;
    String full = null;
    String lastString = null;
    String firstString = null;
    int flag = 1;
    int c = 0;
    int ccount = 0;
    int ccount1 = 0;
    int ccount2 = 0;
    int ccount3 = 0;
    CommonMethods commonMethods = new CommonMethods();
    int d = 0;
    HttpSession session = ServletActionContext.getRequest().getSession();
    String userID = (String)session.getAttribute("USERID");
    logger.info("User Id Is " + userID);
   
    full = chargeVO.getPoValue();
    if ((full != null) && (full.length() >= 3))
    {
      lastString = full.substring(full.length() - 3).trim();
      firstString = full.substring(0, full.length() - 3).trim();
      logger.info(lastString);
      logger.info(firstString);
    }
    try
    {
      con = DBConnectionUtility.getConnection();
      query1 = "select count(*) from c8pf where c8ccy='" + lastString + "'";
      LoggableStatement ps1 = new LoggableStatement(con, query1);
      ResultSet rs1 = ps1.executeQuery();
      while (rs1.next()) {
        c = rs1.getInt(1);
      }
      String replica = "select count(1) from ett_export_order where Export_Order_Number=? AND CIF_ID=? AND UPPER(STATUS) !='R'";
     
      LoggableStatement ls = new LoggableStatement(con, replica);
      ls.setString(1, chargeVO.getExportOrderNumber());
      ls.setString(2, chargeVO.getCifID());
      ResultSet result = ls.executeQuery();
      if (result.next()) {
        ccount = result.getInt(1);
      }
      if ((this.alertMsgArray != null) &&
        (this.alertMsgArray.size() > 0)) {
        this.alertMsgArray.clear();
      }
      if ((chargeVO.getExportOrderNumber() == null) || (chargeVO.getExportOrderNumber().equalsIgnoreCase("")))
      {
        logger.info("PO Number is Mandatory");
        getErrors("16", chargeVO);
        flag = 0;
      }
      if ((chargeVO.getExporterOrderDate() == null) || (chargeVO.getExporterOrderDate().equalsIgnoreCase("")))
      {
        logger.info("INVALID_PO_DATE");
        getErrors("19", chargeVO);
        flag = 0;
      }
      if ((chargeVO.getLastShipmentDate() == null) || (chargeVO.getLastShipmentDate().equalsIgnoreCase("")))
      {
        logger.info("INVALID_SHIPMENT_DATE");
        getErrors("21", chargeVO);
        flag = 0;
      }
      if ((chargeVO.getCifID() == null) || (chargeVO.getCifID().equalsIgnoreCase("")))
      {
        logger.info("INVALID_SHIPMENT_DATE");
        getErrors("0", chargeVO);
        flag = 0;
      }
      if ((chargeVO.getImporterName() == null) || (chargeVO.getImporterName().equalsIgnoreCase("")))
      {
        logger.info("Importer Name is Madatory");
        getErrors("18", chargeVO);
        flag = 0;
      }
      if ((chargeVO.getPoValue() == null) || (chargeVO.getPoValue().equalsIgnoreCase("")))
      {
        logger.info("PO Value is Madatory");
        getErrors("22", chargeVO);
        flag = 0;
      }
      if ((chargeVO.getBeneficiaryName() != null) && (!chargeVO.getBeneficiaryName().isEmpty()) &&
        (chargeVO.getImporterName() != null) && (!chargeVO.getImporterName().isEmpty()) &&
        (chargeVO.getBeneficiaryName().trim().equalsIgnoreCase(chargeVO.getImporterName().trim())))
      {
        logger.info("Ben and Importer Name are Equal");
        getErrors("31", chargeVO);
        flag = 0;
      }
      if ((chargeVO.getExportexpiryDate() != null) && (!chargeVO.getExportexpiryDate().isEmpty()))
      {
        boolean exporyDateValidationResult = isValidExpiryDate(chargeVO.getExportexpiryDate(),
          getTICurrentDate());
        if (!exporyDateValidationResult)
        {
          logger.info("Expiry Date cannot be past date");
          getErrors("32", chargeVO);
          flag = 0;
        }
      }
      SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
     
      logger.info("count is" + ccount);
      if ((c == 1) && (ccount == 0) && (ccount1 == 0))
      {
        query = "insert into ett_export_order(Export_Order_date,Beneficiary_Name,Inco_Terms,Goods_Code,PO_Value,Currency,Expiry_Date,Last_Shipment_Date,Percentage_Freight_Deduction,Percentage_Insurance_Deduction,Margin_percentage,Eligible_amount,Importer_Name,user_id,maker_timestamp,status,GOODS_DESC,INWARD_NO,Export_Order_Number,CIF_ID,NTP,DUE_DATE,SHPMODE) values(to_date(?,'dd/mm/yyyy'),?,?,?,?,?,to_date(?,'dd/mm/yyyy'),to_date(?,'dd/mm/yyyy'),?,?,?,?,?,?,SYSTIMESTAMP,?,?,?,?,?,?,to_date(?,'dd/mm/yyyy'))";
        if (isPoRejected(con, chargeVO.getExportOrderNumber(), chargeVO.getCifID())) {
          query = "UPDATE ETT_EXPORT_ORDER SET EXPORT_ORDER_DATE=to_date(?,'dd/mm/yyyy'),BENEFICIARY_NAME=?,INCO_TERMS=?,GOODS_CODE=?,PO_VALUE=?,CURRENCY=?,EXPIRY_DATE=to_date(?,'dd/mm/yyyy'),LAST_SHIPMENT_DATE=to_date(?,'dd/mm/yyyy'),PERCENTAGE_FREIGHT_DEDUCTION=?,PERCENTAGE_INSURANCE_DEDUCTION=?,MARGIN_PERCENTAGE=?,ELIGIBLE_AMOUNT=?,IMPORTER_NAME=?,USER_ID=?,MAKER_TIMESTAMP=SYSTIMESTAMP,STATUS=?,GOODS_DESC=?,INWARD_NO=?,NTP=?,DUE_DATE=to_date(?,'dd/mm/yyyy'),SHPMODE=? WHERE EXPORT_ORDER_NUMBER=? AND CIF_ID=? ";
        }
        ps = new LoggableStatement(con, query);
       
        Date exporterOrderDate = format.parse(chargeVO.getExporterOrderDate());
        Date date = new Date();
        String curDate = format.format(date);
        Date currentDate = format.parse(curDate);
       



        Date lastShipmentDate = format.parse(chargeVO.getLastShipmentDate());
       
        logger.info("lastShipmentDate=======>" + lastShipmentDate);
        logger.info("exporterOrderDate======>" + exporterOrderDate);
        if ((exporterOrderDate.before(currentDate)) || (exporterOrderDate.equals(currentDate)))
        {
          ps.setString(1, chargeVO.getExporterOrderDate());
        }
        else if ((exporterOrderDate.after(lastShipmentDate)) && (exporterOrderDate.after(lastShipmentDate)))
        {
          getErrors("26", chargeVO);
          flag = 0;
        }
        else if (exporterOrderDate.after(lastShipmentDate))
        {
          logger.info("INVALID_PO_DATE");
          getErrors("25", chargeVO);
          flag = 0;
        }
        else if (exporterOrderDate.after(lastShipmentDate))
        {
          logger.info("INVALID_PO_DATE");
          getErrors("24", chargeVO);
          flag = 0;
        }
        else if (exporterOrderDate.after(currentDate))
        {
          getErrors("28", chargeVO);
          flag = 0;
        }
        else
        {
          logger.info("INVALID_PO_DATE");
          getErrors("15", chargeVO);
          flag = 0;
        }
        ps.setString(2, chargeVO.getBeneficiaryName());
        ps.setString(3, chargeVO.getIncoTerms());
        ps.setString(4, chargeVO.getGoodsCode());
        ps.setString(5, firstString);
        ps.setString(6, lastString);
       
        ps.setString(7, chargeVO.getExportexpiryDate());
        if (lastShipmentDate.after(exporterOrderDate))
        {
          ps.setString(8, chargeVO.getLastShipmentDate());
        }
        else if (lastShipmentDate.before(exporterOrderDate))
        {
          logger.info("INVALID_SHIPMENT_DATE");
          getErrors("27", chargeVO);
          flag = 0;
        }
        ps.setString(9, chargeVO.getFreightDeduction());
        ps.setString(10, chargeVO.getInsuranceDeduction());
        ps.setString(11, chargeVO.getMarginPercentage());
       
        String elgAmount = chargeVO.getEligibleAmount().trim();
       

        elgAmount = elgAmount.substring(0, elgAmount.length() - 3);
        elgAmount = elgAmount.trim();
       
        ps.setString(12, elgAmount);
       
        ps.setString(13, chargeVO.getImporterName());
        ps.setString(14, userID);
        ps.setString(15, "P");
        ps.setString(16, chargeVO.getGoodsDescription());
        ps.setString(17, chargeVO.getInwardNo());
        ps.setString(18, commonMethods.getEmptyIfNull(chargeVO.getExportOrderNumber()).trim());
        ps.setString(19, commonMethods.getEmptyIfNull(chargeVO.getCifID()).trim());
       
        logger.info("Query " + ps.getQueryString());
       
        int count = ps.executeUpdate();
        if (count > 0)
        {
          logger.info("Inserted Successfully");
          chargeVO.setCount(count);
        }
      }
      if (c == 0)
      {
        getErrors("11", chargeVO);
        flag = 0;
      }
      if (ccount > 0)
      {
        getErrors("12", chargeVO);
        flag = 0;
      }
      if (ccount1 > 0)
      {
        logger.info("INVALID_PO_DATE");
        getErrors("15", chargeVO);
        flag = 0;
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      throwDAOException(e);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, ps, rs);
    }
    return chargeVO;
  }
 
  public boolean isPoRejected(Connection con, String poNo, String cifNo)
  {
    LoggableStatement loggableStatement = null;
    ResultSet resultSet = null;
    try
    {
      loggableStatement = new LoggableStatement(con, "SELECT COUNT(1) FROM ETT_EXPORT_ORDER WHERE EXPORT_ORDER_NUMBER = ? AND CIF_ID = ? AND STATUS='R' ");
      loggableStatement.setString(1, poNo);
      loggableStatement.setString(2, cifNo);
      resultSet = loggableStatement.executeQuery();
      if ((resultSet.next()) &&
        (resultSet.getInt(1) > 0)) {
        return true;
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(null, loggableStatement, resultSet);
    }
    DBConnectionUtility.surrenderDB(null, loggableStatement, resultSet);
   
    return false;
  }
 
  public boolean isValidExpiryDate(String expiryDate, String tiDate)
  {
    logger.info("inside validation");
    Boolean isValid = Boolean.valueOf(false);
    try
    {
      logger.info("inside validation-----");
     
      logger.info("----/////---TIDATE-----------" + tiDate);
     




      Date expiryDateObj = new SimpleDateFormat("dd/MM/yyyy").parse(expiryDate);
      Date tiDateObj = new SimpleDateFormat("dd/MM/yyyy").parse(tiDate);
     




      logger.info("Expiry Date---------" + expiryDate);
      logger.info("Ti Date------------" + tiDateObj);
     
      logger.info("expiryDateObj==>" + expiryDateObj);
      logger.info("tiDateObj==>" + tiDateObj);
     





      int i = expiryDateObj.compareTo(tiDateObj);
      logger.info("Difference date--------" + i);
      if (expiryDateObj.compareTo(tiDateObj) > 0)
      {
        logger.info("---------Correct-1-----");
        isValid = Boolean.valueOf(true);
      }
      else if (expiryDateObj.compareTo(tiDateObj) < 0)
      {
        logger.info("-----Expiry date-------" + i);
        logger.info("-----Expiry date-----");
        isValid = Boolean.valueOf(false);
      }
      else if (expiryDateObj.compareTo(tiDateObj) == 0)
      {
        isValid = Boolean.valueOf(true);
        logger.info("---------Correct---2-------");
      }
    }
    catch (Exception e)
    {
      isValid = Boolean.valueOf(false);
    }
    return isValid.booleanValue();
  }
 
  public String getTICurrentDate()
  {
    String tiCurrDate = "";
    ResultSet rs = null;
    Connection con = null;
    PreparedStatement ps = null;
    Map<String, Object> session = ActionContext.getContext().getSession();
   



    String query = "SELECT to_char(PROCDATE,'dd/mm/yyyy') as PROCDATE FROM DLYPRCCYCL ";
    try
    {
      con = DBConnectionUtility.getConnection();
      ps = con.prepareStatement(query);
      rs = ps.executeQuery();
      while (rs.next())
      {
        tiCurrDate = rs.getString(1);
        logger.info("tiCurrDate====>" + tiCurrDate);
       
        session.put("processDate", tiCurrDate);
      }
      logger.info("TIDATE-----" + tiCurrDate);
    }
    catch (Exception e)
    {
      logger.info("tiCurrDate=== Exception=>" + e);
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, ps, rs);
    }
    return tiCurrDate;
  }
 
  public String convertValidAmtAndCCy(String amount)
  {
    String validAmt = null;
    if ((amount == null) || (amount.isEmpty())) {
      return validAmt;
    }
    amount = amount.trim();
    if (amount.length() <= 3) {
      return validAmt;
    }
    String ccy = amount.substring(amount.length() - 3);
   

    String floatingPointCount = isValidCcy(ccy);
    if (floatingPointCount == null) {
      return validAmt;
    }
    String amountOnly = amount.substring(0, amount.length() - 3).trim();
    if (!isValidDecimal(amountOnly)) {
      return validAmt;
    }
    if ((floatingPointCount.equals("0")) && (amountOnly.contains("."))) {
      amountOnly = amountOnly.substring(0, amountOnly.indexOf("."));
    }
    if ((!amountOnly.contains(".")) && (!floatingPointCount.equals("0"))) {
      amountOnly = amountOnly + "." + getDefaultFloatingPoint(floatingPointCount);
    }
    if ((amountOnly.indexOf(".") + 1 == amountOnly.length()) && (!floatingPointCount.equals("0"))) {
      amountOnly = amountOnly + getDefaultFloatingPoint(floatingPointCount);
    }
    if ((amountOnly.indexOf(".") + 1 < amountOnly.length()) && (!floatingPointCount.equals("0")))
    {
      String floatingPointValue = amountOnly.substring(amountOnly.indexOf(".") + 1, amountOnly.length());
      if (floatingPointValue.length() > new Integer(floatingPointCount).intValue()) {
        floatingPointValue = floatingPointValue.substring(0, new Integer(floatingPointCount).intValue());
      }
      if (floatingPointValue.length() < new Integer(floatingPointCount).intValue())
      {
        int zeroCountToAdd = new Integer(floatingPointCount).intValue() - floatingPointValue.length();
        while (zeroCountToAdd > 0)
        {
          floatingPointValue = floatingPointValue + "0";
          zeroCountToAdd--;
        }
      }
      amountOnly = amountOnly.substring(0, amountOnly.indexOf(".") + 1);
      amountOnly = amountOnly + floatingPointValue;
    }
    validAmt = amountOnly + " " + ccy.toUpperCase();
   
    return validAmt;
  }
 
  public boolean isValidDecimal(String amtInStr)
  {
    try
    {
      BigDecimal localBigDecimal = new BigDecimal(amtInStr);
    }
    catch (Exception e)
    {
      return false;
    }
    return true;
  }
 
  public String isValidCcy(String ccy)
  {
    String result = null;
    Connection aConnection = null;
    LoggableStatement aLoggableStatement = null;
    ResultSet aResultSet = null;
    try
    {
      aConnection = DBConnectionUtility.getConnection();
      String query = "SELECT C8CED FROM C8PF WHERE C8CCY = ?";
      aLoggableStatement = new LoggableStatement(aConnection, query);
      aLoggableStatement.setString(1, ccy.toUpperCase());
      aResultSet = aLoggableStatement.executeQuery();
      if (aResultSet.next()) {
        result = aResultSet.getString("C8CED");
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      result = null;
    }
    finally
    {
      DBConnectionUtility.surrenderDB(aConnection, aLoggableStatement, aResultSet);
    }
    return result;
  }
 
  public String getDefaultFloatingPoint(String floatingPointCount)
  {
    String result = "";
    int floatingPointCountToInt = new Integer(floatingPointCount).intValue();
    for (int counter = 1; counter <= floatingPointCountToInt; counter++) {
      result = result + "0";
    }
    return result;
  }
 
  public ChargeScheduleVO fetchVal(ChargeScheduleVO chargeVO)
  {
    Connection con = null;
    LoggableStatement ls = null;
    ResultSet res = null;
    BigDecimal margin = new BigDecimal(0);
    BigDecimal frded = new BigDecimal(0);
    BigDecimal insded = new BigDecimal(0);
    BigDecimal poval = new BigDecimal(0);
    BigDecimal eligval = new BigDecimal(0);
    String marginper = "";
    String elig = "";
    String PO = "";
    try
    {
      con = DBConnectionUtility.getConnection();
      String query = "select MARGIN from CUSTOMERMARGIN where TRIM(CIF)=? AND FACILITY ='PO'";
      ls = new LoggableStatement(con, query);
      ls.setString(1, chargeVO.getCifID());
      logger.info("margin fetch==>" + query);
      res = ls.executeQuery();
      if (res.next()) {
        margin = new BigDecimal(res.getDouble(1));
      }
      logger.info("margin is " + margin);
    }
    catch (Exception e)
    {
      logger.info("Exception is " + e.getMessage());
    }
    PO = chargeVO.getPoValue();
   
    String tempAmt = convertValidAmtAndCCy(PO);
    if (tempAmt != null)
    {
      PO = tempAmt;
      chargeVO.setPoValue(PO);
    }
    else
    {
      chargeVO.setExportOrderNumber(CommonMethods.removeComma(chargeVO.getExportOrderNumber()));
      chargeVO.setEligibleAmount(elig);
      chargeVO.setMarginPercentage(marginper);
      return chargeVO;
    }
    PO = PO.trim();
    String POAmtOnly = PO.substring(0, PO.length() - 3).trim();
    logger.info("PO Amount ONly " + POAmtOnly);
    poval = new BigDecimal(POAmtOnly.trim());
    String POAmtCurr = PO.replaceAll("[^A-Z]", "");
   
    frded = new BigDecimal(chargeVO.getFreightDeduction());
    insded = new BigDecimal(chargeVO.getInsuranceDeduction());
   



    BigDecimal newMargin = margin.divide(new BigDecimal(100));
    System.out.println("newMargin==>" + newMargin);
   
    BigDecimal ValuesForFreight = new BigDecimal(100).subtract(frded).subtract(insded);
    System.out.println("ValuesForFreight----" + ValuesForFreight);
   
    BigDecimal ValuesForMargin = new BigDecimal(100).subtract(margin);
    System.out.println("ValuesForMargin----" + ValuesForMargin);
   

    BigDecimal newEligibleValue = poval.multiply(ValuesForFreight).multiply(ValuesForMargin).divide(new BigDecimal(10000));
    System.out.println("newEligibleValue---" + newEligibleValue);
   
    newEligibleValue = newEligibleValue.setScale(2, 4);
    System.out.println("newEligibleValue1---" + newEligibleValue);
    if (newEligibleValue.compareTo(new BigDecimal(0)) == -1) {
      elig = POAmtCurr;
    } else {
      elig = newEligibleValue.toString() + " " + POAmtCurr;
    }
    logger.info("elig--> " + elig + " poamtonly--> " + POAmtOnly + " poamtcurr--> " + POAmtCurr);
    chargeVO.setEligibleAmount(elig);
    logger.info("percentage----------" + chargeVO.getMarginPercentage());
    logger.info("elegibile amount----------" + chargeVO.getEligibleAmount());
    chargeVO.setExportOrderNumber(CommonMethods.removeComma(chargeVO.getExportOrderNumber()));
    return chargeVO;
  }
 
  public void fetchValTest()
  {
    BigDecimal margin = new BigDecimal(0);
    BigDecimal frded = new BigDecimal(0);
    BigDecimal insded = new BigDecimal(0);
    BigDecimal poval = new BigDecimal(0);
    BigDecimal eligval = new BigDecimal(0);
    String marginper = "";
    String elig = "";
    String PO = "";
   
    PO = "15645.00inr";
   
    String tempAmt = convertValidAmtAndCCy(PO);
    if (tempAmt != null) {
      PO = tempAmt;
    }
    PO = PO.trim();
    String POAmtOnly = PO.substring(0, PO.length() - 3).trim();
   
    logger.info("PO Amount ONly " + POAmtOnly);
    poval = new BigDecimal(POAmtOnly.trim());
    String POAmtCurr = PO.replaceAll("[^A-Z]", "");
    logger.info("POAmtCurr : " + POAmtCurr);
   
    frded = new BigDecimal("2");
    insded = new BigDecimal("1");
   
    BigDecimal additionalVal = frded.add(insded).add(margin);
   
    eligval = poval.subtract(additionalVal.multiply(poval).divide(new BigDecimal(100)));
   

    eligval = eligval.setScale(2, 4);
   
    marginper = margin.toString();
    if (eligval.compareTo(new BigDecimal(0)) == -1) {
      elig = POAmtCurr;
    } else {
      elig = eligval.toString() + " " + POAmtCurr;
    }
    logger.info(elig);
  }
 
  public ChargeScheduleVO fetchBeneficiaryName(ChargeScheduleVO chargeVO)
  {
    Connection con = null;
    LoggableStatement ls = null;
    ResultSet res = null;
    String benName = null;
    try
    {
      con = DBConnectionUtility.getConnection();
     









      String query = "select GFCUN from GFPF where trim(GFCPNC) = TRIM(?)";
     
      ls = new LoggableStatement(con, query);
      ls.setString(1, chargeVO.getCifID());
     
      logger.info("query=====>" + query);
      res = ls.executeQuery();
      if (res.next()) {
        chargeVO.setBeneficiaryName(res.getString("GFCUN"));
      }
      if (CommonMethods.isNull(chargeVO.getCifID()))
      {
        logger.info("***--Ben is null so setting as null---***");
        chargeVO.setBeneficiaryName(null);
      }
      logger.info("BeneficiaryName is " + chargeVO.getBeneficiaryName());
    }
    catch (Exception e)
    {
      e.printStackTrace();
      logger.info("Exception is " + e.getMessage());
    }
    return chargeVO;
  }
 
  public ChargeScheduleVO fetchPurchaseOrder(ChargeScheduleVO chargeVO)
  {
    Connection con = null;
    ResultSet rs = null;
    LoggableStatement ps = null;
    String query = null;
    try
    {
      con = DBConnectionUtility.getConnection();
     
      String ExportorderNO = chargeVO.getExportOrderNumber();
      String CIFID = chargeVO.getCifID();
      logger.info("ExportorderNO" + ExportorderNO);
      logger.info("CIFID" + CIFID);
      if (ExportorderNO.length() != 0)
      {
        logger.info("hai");
        query = "select Export_Order_Number,TO_CHAR(TO_DATE(Export_Order_date,'DD/MM/YY'),'dd/mm/YYYY') AS Export_Order_date,CIF_ID,Beneficiary_Name,Inco_Terms,Goods_Code,PO_Value,Currency,TO_CHAR(TO_DATE(Expiry_Date,'DD/MM/YY'),'dd/mm/YYYY') AS Expiry_Date,TO_CHAR(TO_DATE(Last_Shipment_Date,'DD/MM/YY'),'dd/mm/YYYY') AS Last_Shipment_Date,Percentage_Freight_Deduction,Percentage_Insurance_Deduction,Margin_percentage,Eligible_amount,importer_name,INWARD_NO,TO_CHAR(GOODS_DESC) GOODS_DESC from ett_export_order where Export_Order_Number= ? ";
        logger.info("444444444");
      }
      else
      {
        logger.info("hi");
        query = "select Export_Order_Number,TO_CHAR(TO_DATE(Export_Order_date,'DD/MM/YY'),'dd/mm/YYYY') AS Export_Order_date,CIF_ID,Beneficiary_Name,Inco_Terms,Goods_Code,PO_Value,Currency,TO_CHAR(TO_DATE(Expiry_Date,'DD/MM/YY'),'dd/mm/YYYY') AS Expiry_Date,TO_CHAR(TO_DATE(Last_Shipment_Date,'DD/MM/YY'),'dd/mm/YYYY') AS Last_Shipment_Date,Percentage_Freight_Deduction,Percentage_Insurance_Deduction,Margin_percentage,Eligible_amount,importer_name from ett_export_order where CIF_ID= ? ";
      }
      logger.info("query is " + query);
      ps = new LoggableStatement(con, query);
      if (ExportorderNO.length() != 0) {
        ps.setString(1, ExportorderNO);
      } else {
        ps.setString(1, CIFID);
      }
      logger.info("111111111111");
      rs = ps.executeQuery();
      logger.info("222222222222");
      if (rs.next())
      {
        chargeVO.setExportOrderNumber(rs.getString(1));
       
        chargeVO.setExporterOrderDate(rs.getString(2));
        chargeVO.setCifID(rs.getString(3));
        chargeVO.setBeneficiaryName(rs.getString(4));
        chargeVO.setIncoTerms(rs.getString(5));
        chargeVO.setGoodsCode(rs.getString(6));
        chargeVO.setPoValue(rs.getString(7));
        chargeVO.setExportcurrency(rs.getString(8));
        chargeVO.setExportexpiryDate(rs.getString(9));
        chargeVO.setLastShipmentDate(rs.getString(10));
        chargeVO.setFreightDeduction(rs.getString(11));
        chargeVO.setInsuranceDeduction(rs.getString(12));
        chargeVO.setMarginPercentage(rs.getString(13));
        chargeVO.setEligibleAmount(rs.getString(14));
        chargeVO.setImporterName(rs.getString(15));
        chargeVO.setInwardNo(rs.getString("INWARD_NO"));
        chargeVO.setGoodsDescription(rs.getString("GOODS_DESC"));
        if ((chargeVO != null) && (chargeVO.getPoValue() != null) && (chargeVO.getExportcurrency() != null)) {
          chargeVO.setPoValue(chargeVO.getPoValue() + " " + chargeVO.getExportcurrency());
        }
        if ((chargeVO != null) && (chargeVO.getEligibleAmount() != null) && (chargeVO.getExportcurrency() != null)) {
          chargeVO.setEligibleAmount(chargeVO.getEligibleAmount() + " " + chargeVO.getExportcurrency());
        }
      }
    }
    catch (Exception e)
    {
      logger.info("Exception is " + e.getMessage());
    }
    return chargeVO;
  }
 
  public ChargeScheduleVO searchPurchaseOrderDetails(ChargeScheduleVO chargeVO)
  {
    ArrayList<ChargeScheduleVO> purchase = null;
    Connection con = null;
    LoggableStatement log = null;
    ResultSet res = null;
    ChargeScheduleVO chVo = null;
    String query = null;
    boolean cifIDFlag = false;
    boolean exportOrderNumberFlag = false;
    int setValue = 0;
    try
    {
      purchase = new ArrayList();
      con = DBConnectionUtility.getConnection();
      logger.info("CIFID" + chargeVO.getCifID());
      String ExportorderNO = chargeVO.getExportOrderNumber();
      String CIFID = chargeVO.getCifID();
      if (((ExportorderNO instanceof String)) && (!ExportorderNO.trim().equalsIgnoreCase("")) &&
        ((CIFID instanceof String)) && (!CIFID.trim().equalsIgnoreCase("")))
      {
        String Where = "WHERE CIF_ID=? and EXPORT_ORDER_NUMBER=?";
        query = "SELECT EXPORT_ORDER_NUMBER AS PO_NO,TO_CHAR(EXPORT_ORDER_DATE,'DD/MM/YY') AS PO_DATE,CIF_ID AS CIF,BENEFICIARY_NAME AS BEN_NAME,INCO_TERMS AS INCO,GOODS_CODE AS GOODS,PO_VALUE AS PO_VAL,CURRENCY AS CURR,TO_CHAR(EXPIRY_DATE,'DD/MM/YY') AS EXPIRY_DATE,TO_CHAR(LAST_SHIPMENT_DATE,'DD/MM/YY') AS LAST_SHIPMENT_DATE,PERCENTAGE_FREIGHT_DEDUCTION AS FREIGHT,PERCENTAGE_INSURANCE_DEDUCTION AS INSURANCE,ELIGIBLE_AMOUNT AS AMOUNT,IMPORTER_NAME AS IMPORTER,MARGIN_PERCENTAGE AS MARGIN FROM ETT_EXPORT_ORDER " + Where;
       
        cifIDFlag = true;
        exportOrderNumberFlag = true;
      }
      else if (((ExportorderNO instanceof String)) && (!ExportorderNO.trim().equalsIgnoreCase("")))
      {
        query = "SELECT EXPORT_ORDER_NUMBER AS PO_NO,TO_CHAR(EXPORT_ORDER_DATE,'DD/MM/YY') AS PO_DATE,CIF_ID AS CIF,BENEFICIARY_NAME AS BEN_NAME,INCO_TERMS AS INCO,GOODS_CODE AS GOODS,PO_VALUE AS PO_VAL,CURRENCY AS CURR,TO_CHAR(EXPIRY_DATE,'DD/MM/YY') AS EXPIRY_DATE,TO_CHAR(LAST_SHIPMENT_DATE,'DD/MM/YY') AS LAST_SHIPMENT_DATE,PERCENTAGE_FREIGHT_DEDUCTION AS FREIGHT,PERCENTAGE_INSURANCE_DEDUCTION AS INSURANCE,ELIGIBLE_AMOUNT AS AMOUNT,IMPORTER_NAME AS IMPORTER,MARGIN_PERCENTAGE AS MARGIN FROM ETT_EXPORT_ORDER WHERE EXPORT_ORDER_NUMBER=?";
       
        exportOrderNumberFlag = true;
      }
      else if (((CIFID instanceof String)) && (!CIFID.trim().equalsIgnoreCase("")))
      {
        query = "SELECT EXPORT_ORDER_NUMBER AS PO_NO,TO_CHAR(EXPORT_ORDER_DATE,'DD/MM/YY') AS PO_DATE,CIF_ID AS CIF,BENEFICIARY_NAME AS BEN_NAME,INCO_TERMS AS INCO,GOODS_CODE AS GOODS,PO_VALUE AS PO_VAL,CURRENCY AS CURR,TO_CHAR(EXPIRY_DATE,'DD/MM/YY') AS EXPIRY_DATE,TO_CHAR(LAST_SHIPMENT_DATE,'DD/MM/YY') AS LAST_SHIPMENT_DATE,PERCENTAGE_FREIGHT_DEDUCTION AS FREIGHT,PERCENTAGE_INSURANCE_DEDUCTION AS INSURANCE,ELIGIBLE_AMOUNT AS AMOUNT,IMPORTER_NAME AS IMPORTER,MARGIN_PERCENTAGE AS MARGIN FROM ETT_EXPORT_ORDER  where CIF_ID=?";
       
        cifIDFlag = true;
      }
      else
      {
        query = "SELECT EXPORT_ORDER_NUMBER AS PO_NO,TO_CHAR(EXPORT_ORDER_DATE,'DD/MM/YY') AS PO_DATE,CIF_ID AS CIF,BENEFICIARY_NAME AS BEN_NAME,INCO_TERMS AS INCO,GOODS_CODE AS GOODS,PO_VALUE AS PO_VAL,CURRENCY AS CURR,TO_CHAR(EXPIRY_DATE,'DD/MM/YY') AS EXPIRY_DATE,TO_CHAR(LAST_SHIPMENT_DATE,'DD/MM/YY') AS LAST_SHIPMENT_DATE,PERCENTAGE_FREIGHT_DEDUCTION AS FREIGHT,PERCENTAGE_INSURANCE_DEDUCTION AS INSURANCE,ELIGIBLE_AMOUNT AS AMOUNT,IMPORTER_NAME AS IMPORTER,MARGIN_PERCENTAGE AS MARGIN FROM ETT_EXPORT_ORDER ";
      }
      log = new LoggableStatement(con, query);
      if (cifIDFlag) {
        log.setString(++setValue, CIFID);
      }
      if (exportOrderNumberFlag) {
        log.setString(++setValue, ExportorderNO);
      }
      logger.info(log.getQueryString());
      res = log.executeQuery();
      while (res.next())
      {
        chVo = new ChargeScheduleVO();
        chVo.setPoNo(res.getString("PO_NO"));
        chVo.setPoDate(res.getString("PO_DATE"));
        chVo.setPoCif(res.getString("CIF"));
        chVo.setPoBen(res.getString("BEN_NAME"));
        chVo.setPoInco(res.getString("INCO"));
        chVo.setPoGoodDesc(res.getString("GOODS"));
        chVo.setPoImpName(res.getString("IMPORTER"));
        chVo.setPoAmtValue(res.getString("PO_VAL") + " " + res.getString("CURR"));
        chVo.setPoExpdate(res.getString("EXPIRY_DATE"));
        logger.info("Expiry " + res.getString("EXPIRY_DATE"));
        chVo.setPoLastShipDate(res.getString("LAST_SHIPMENT_DATE"));
        chVo.setPoFrDeduct(res.getString("FREIGHT"));
        chVo.setPoInsDeduct(res.getString("INSURANCE"));
        chVo.setPoMargin(res.getString("MARGIN"));
        chVo.setPoEligAmt(res.getString("AMOUNT") + " " + res.getString("CURR"));
        logger.info("chVo.getPoEligAmt=====>" + chVo.getPoEligAmt());
        purchase.add(chVo);
      }
      chargeVO.setPurchaseOrderList(purchase);
      for (int i = 0; i < purchase.size(); i++) {
        logger.info("Value of Expiry Date" + ((ChargeScheduleVO)purchase.get(i)).getPoExpdate());
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return chargeVO;
  }
 
  public ArrayList<CustomerDataVO> searchGoodsCode(CustomerDataVO cusDataVo)
  {
    ArrayList<CustomerDataVO> goodsList = null;
    Connection connection = null;
    LoggableStatement loggableStatement = null;
    ResultSet resultSet = null;
    CustomerDataVO cusVo = null;
    boolean goodsCodeFlag = false;
    boolean goodsCodeContainFlag = false;
    boolean goodsDescFlag = false;
    boolean goodsDescContainFlag = false;
    int setValue = 0;
    try
    {
      String query = "SELECT CODE79,DESCRI73 FROM GOODSC87 WHERE CODE79=CODE79  ";
      if (((cusDataVo.getGoodsCode() instanceof String)) && (!cusDataVo.getGoodsCode().trim().equalsIgnoreCase(""))) {
        if (cusDataVo.getGoodsCode().trim().contains("%"))
        {
          query = query + " AND TRIM(CODE79) LIKE '%'||?||'%'";
          goodsCodeContainFlag = true;
        }
        else
        {
          query = query + " AND TRIM(CODE79)=?";
          goodsCodeFlag = true;
        }
      }
      if (((cusDataVo.getGoodsDesc() instanceof String)) && (!cusDataVo.getGoodsDesc().trim().equalsIgnoreCase(""))) {
        if (cusDataVo.getGoodsDesc().trim().contains("%"))
        {
          query = query + " AND UPPER(TRIM(DESCRI73)) LIKE '%'||?||'%'";
          goodsDescContainFlag = true;
        }
        else
        {
          query = query + " AND UPPER(TRIM(DESCRI73))=?";
          goodsDescFlag = true;
        }
      }
      goodsList = new ArrayList();
      connection = DBConnectionUtility.getConnection();
      loggableStatement = new LoggableStatement(connection, query);
      if (goodsCodeContainFlag) {
        loggableStatement.setString(++setValue, cusDataVo.getGoodsCode().trim().replaceAll("%", ""));
      } else if (goodsCodeFlag) {
        loggableStatement.setString(++setValue, cusDataVo.getGoodsCode().trim());
      }
      if (goodsDescContainFlag) {
        loggableStatement.setString(++setValue, cusDataVo.getGoodsDesc().trim().replaceAll("%", ""));
      } else if (goodsDescFlag) {
        loggableStatement.setString(++setValue, cusDataVo.getGoodsDesc().trim().toUpperCase());
      }
      logger.info("Query " + loggableStatement.getQueryString());
      resultSet = loggableStatement.executeQuery();
      while (resultSet.next())
      {
        cusVo = new CustomerDataVO();
        cusVo.setGoods(resultSet.getString(1));
        cusVo.setDescription(resultSet.getString(2));
        goodsList.add(cusVo);
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(connection, loggableStatement, resultSet);
    }
    return goodsList;
  }
 
  public ArrayList<CustomerDataVO> fetchcountryList()
  {
    ArrayList<CustomerDataVO> inwardList = null;
    Connection connection = null;
    LoggableStatement loggableStatement = null;
    ResultSet resultSet = null;
    CustomerDataVO cusVo = null;
    try
    {
      inwardList = new ArrayList();
      connection = DBConnectionUtility.getConnection();
      String getCountrylistQuery = "SELECT C7CNA, C7CNM FROM C7PF";
      loggableStatement = new LoggableStatement(connection, getCountrylistQuery);
      logger.info("Query " + loggableStatement.getQueryString());
      resultSet = loggableStatement.executeQuery();
      while (resultSet.next())
      {
        cusVo = new CustomerDataVO();
        cusVo.setCountry(resultSet.getString("C7CNM"));
        cusVo.setCountrycode(resultSet.getString("C7CNA"));
        inwardList.add(cusVo);
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(connection, loggableStatement, resultSet);
    }
    return inwardList;
  }
 
  public ArrayList<CustomerDataVO> fetchInwardDetails(CustomerDataVO cusDataVo)
  {
    ArrayList<CustomerDataVO> inwardList = null;
    Connection connection = null;
    LoggableStatement loggableStatement = null;
    ResultSet resultSet = null;
    CustomerDataVO cusVo = null;
    String inwardDetailsQuery = "";
   
    String setValue = null;
    String setValue1 = null;
    String setValue2 = null;
    int setValuecount = 0;
    try
    {
      inwardList = new ArrayList();
      connection = DBConnectionUtility.getConnection();
     


      inwardDetailsQuery = "SELECT TRIM(AMASTER.CCY), TRIM(AMASTER.MASTER_REF),TRIM(B.CUS_MNM),TRIM(AMASTER.AMOUNT)/POWER(10,TRIM(C82.C8CED)),TRIM(APPPARTYDTLS.ADDRESS1)   FROM MASTER AMASTER,LCMASTER ALCMASTER,PARTYDTLS B,C8PF C82,PARTYDTLS APPPARTYDTLS WHERE  AMASTER.KEY97 = ALCMASTER.KEY97 AND ALCMASTER.BEN_PTY = B.KEY97 AND  ALCMASTER.APP_PTY= \tAPPPARTYDTLS.KEY97 AND TRIM(C82.C8CCY)=trim(AMASTER.CCY) AND AMASTER.STATUS='LIV' ";
      if (((cusDataVo.getMaster() instanceof String)) && (!cusDataVo.getMaster().trim().equalsIgnoreCase("")))
      {
        inwardDetailsQuery = inwardDetailsQuery + " AND TRIM(AMASTER.MASTER_REF) = ? ";
        setValue = cusDataVo.getMaster().trim();
        logger.info("setvalue---" + setValue);
      }
      if (((cusDataVo.getCustNo() instanceof String)) && (!cusDataVo.getCustNo().trim().equalsIgnoreCase("")))
      {
        inwardDetailsQuery = inwardDetailsQuery + " AND TRIM(B.CUS_MNM)  =? ";
        setValue1 = cusDataVo.getCustNo().trim();
        logger.info("setvalue1---" + setValue1);
      }
      loggableStatement = new LoggableStatement(connection, inwardDetailsQuery);
      if (setValue != null) {
        loggableStatement.setString(++setValuecount, setValue);
      }
      if (setValue1 != null) {
        loggableStatement.setString(++setValuecount, setValue1);
      }
      logger.info("query---" + loggableStatement.getQueryString());
      resultSet = loggableStatement.executeQuery();
      while (resultSet.next())
      {
        cusVo = new CustomerDataVO();
        cusVo.setMasCcy(resultSet.getString(1));
        cusVo.setMasRef(resultSet.getString(2));
        cusVo.setCif(resultSet.getString(3));
        cusVo.setMasAmount(resultSet.getString(4));
        cusVo.setImpName(resultSet.getString(5));
        inwardList.add(cusVo);
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(connection, loggableStatement, resultSet);
    }
    return inwardList;
  }
 
  public ChargeScheduleVO setMasterDetails(ChargeScheduleVO chargeVO, String masRef)
  {
    Connection connection = null;
    LoggableStatement loggableStatement = null;
    ResultSet resultSet = null;
    try
    {
      connection = DBConnectionUtility.getConnection();
      loggableStatement = new LoggableStatement(connection, "SELECT  BENPARTYDTLS.CUS_MNM AS CUSTOMER , BENPARTYDTLS.ADDRESS1 AS BEN_NAME,  APPPARTYDTLS.ADDRESS1 AS APP_NAME, TO_CHAR(ALCMASTER.SHIP_DATE,'DD/MM/YYYY') AS SHIP_DATE,  ALCMASTER.GOODSCODE AS GOODS_CODE,  ALCMASTER.GOODS_DESC AS GOODS_DESC,  ALCMASTER.INCOTERMS AS INCO, TO_CHAR(AMASTER.EXPIRY_DAT,'DD/MM/YYYY') AS EXPIRY_DATE,(((AMASTER.AMT_O_S)/100)||' '||AMASTER.CCY) AS AMOUNT,EE.SMODE  FROM MASTER AMASTER,LCMASTER ALCMASTER,PARTYDTLS BENPARTYDTLS,PARTYDTLS APPPARTYDTLS,BASEEVENT BEV,EXTEVENT EE WHERE  AMASTER.KEY97 = \tALCMASTER.KEY97 AND ALCMASTER.BEN_PTY=  BENPARTYDTLS.KEY97 AND ALCMASTER.APP_PTY= \tAPPPARTYDTLS.KEY97 AND BEV.MASTER_KEY=AMASTER.KEY97 AND BEV.KEY97=EE.EVENT AND TRIM(AMASTER.MASTER_REF)=?");
      loggableStatement.setString(1, masRef);
      logger.info("Query " + loggableStatement.getQueryString());
      resultSet = loggableStatement.executeQuery();
      while (resultSet.next())
      {
        chargeVO.setCifID(CommonMethods.returnEmptyIfNull(resultSet.getString("CUSTOMER")));
        chargeVO.setBeneficiaryName(CommonMethods.returnEmptyIfNull(resultSet.getString("BEN_NAME")));
        chargeVO.setPoValue(CommonMethods.returnEmptyIfNull(resultSet.getString("AMOUNT")));
        chargeVO.setExportexpiryDate(CommonMethods.returnEmptyIfNull(resultSet.getString("EXPIRY_DATE")));
        chargeVO.setLastShipmentDate(CommonMethods.returnEmptyIfNull(resultSet.getString("SHIP_DATE")));
        chargeVO.setGoodsCode(CommonMethods.returnEmptyIfNull(resultSet.getString("GOODS_CODE")));
        chargeVO.setInwardNo(CommonMethods.returnEmptyIfNull(masRef.trim()));
        chargeVO.setGoodsDescription(CommonMethods.returnEmptyIfNull(resultSet.getString("GOODS_DESC")));
        chargeVO.setImporterName(CommonMethods.returnEmptyIfNull(resultSet.getString("APP_NAME")));
        chargeVO.setIncoTerms(CommonMethods.returnEmptyIfNull(resultSet.getString("INCO")));
       
        String lastShipdate = CommonMethods.returnEmptyIfNull(resultSet.getString("SHIP_DATE"));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(lastShipdate));
        cal.add(5, 21);
        String dueDate = sdf.format(cal.getTime());
        chargeVO.setDueDate(CommonMethods.returnEmptyIfNull(dueDate));
        chargeVO.setShpMode(CommonMethods.returnEmptyIfNull(resultSet.getString("SMODE")));
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(connection, loggableStatement, resultSet);
    }
    return chargeVO;
  }
 
  public String getWatchList()
  {
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rst = null;
    String serviceURL = null;
    String getWatchListService = "SELECT trim(VALUE) as URL FROM BRIDGEPROPERTIES where trim(KEY)='WatchListUrl'";
    logger.info("Inside getting URL");
    try
    {
      con = DBConnectionUtility.getWiseConnection();
      logger.info("Getting themebridge connection");
      pst = con.prepareStatement(getWatchListService);
      rst = pst.executeQuery();
      logger.info("Executing URL query");
      while (rst.next())
      {
        serviceURL = rst.getString("URL");
        logger.info("inside while loop");
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rst);
    }
    logger.info("ServiceURL" + serviceURL);
    return serviceURL;
  }
 
  public static String readFileInpStream(InputStream inpStream)
    throws IOException
  {
    String result = "";
    BufferedReader buffReader = null;
    try
    {
      String line = null;
      StringBuilder strBuilder = new StringBuilder();
      String newLineSeparator = System.getProperty("line.separator");
      buffReader = new BufferedReader(new InputStreamReader(inpStream));
     
      int lineCount = 1;
      while ((line = buffReader.readLine()) != null)
      {
        if (lineCount > 1) {
          strBuilder.append(newLineSeparator);
        }
        strBuilder.append(line);
        lineCount++;
      }
      result = strBuilder.toString();
    }
    catch (IOException ex)
    {
      logger.error("IOException " + ex.getMessage());
      ex.printStackTrace();
      try
      {
        if (buffReader != null) {
          buffReader.close();
        }
      }
      catch (IOException ex)
      {
        logger.error("BufferedReader close exception! " + ex.getMessage());
        ex.printStackTrace();
      }
    }
    finally
    {
      try
      {
        if (buffReader != null) {
          buffReader.close();
        }
      }
      catch (IOException ex)
      {
        logger.error("BufferedReader close exception! " + ex.getMessage());
        ex.printStackTrace();
      }
    }
    return result;
  }
 
  public String checkLoginedUserType(String usrname)
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement loggableStatement = null;
    ResultSet rs = null;
    String result = null;
    String sqlQuery = null;
    CommonMethods commonMethods = null;
    try
    {
      commonMethods = new CommonMethods();
      con = DBConnectionUtility.getConnection();
      logger.info("username is" + usrname);
      if (!CommonMethods.isNull(usrname))
      {
        sqlQuery = "SELECT count(T.TEAMKEY) as teamkey FROM SECAGE88 U LEFT JOIN TEAMUSRMAP T  ON T.USERKEY = U.SKEY80 WHERE TRIM(UPPER(U.NAME85))  = TRIM(UPPER(?))  AND TRIM(UPPER(T.TEAMKEY)) LIKE 'PO%'";
       



        loggableStatement = new LoggableStatement(con, sqlQuery);
        loggableStatement.setString(1, usrname);
        logger.info("CheckLoginedUserType: " + loggableStatement.getQueryString());
       
        rs = loggableStatement.executeQuery();
        while (rs.next())
        {
          result = rs.getString(1);
          logger.info("Home DAO result" + result);
        }
      }
    }
    catch (Exception exception)
    {
      throwDAOException(exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, loggableStatement, rs);
    }
    return result;
  }
 
  public String getUserID()
  {
    logger.info("Entering Method");
    String sessionUserName = null;
   
    boolean isAvail = false;
    String userName = null;
    String loginedUserId = null;
    try
    {
      HttpSession session = ServletActionContext.getRequest().getSession();
     
      HttpServletRequest request = (HttpServletRequest)ActionContext.getContext().get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
     
      sessionUserName = (String)session.getAttribute("loginedUserName");
      logger.info("loginedUserName------------------" + sessionUserName);
      if (sessionUserName == null)
      {
        sessionUserName = request.getRemoteUser();
        logger.info("getRemoteUser[------------------" + sessionUserName);
        if (sessionUserName == null)
        {
          Connection them_con = null;
          them_con = DBConnectionUtility.getWiseConnection();
         

          sessionUserName = request.getRequestedSessionId();
          String get_User_ID = "SELECT SCT.USERNAME AS USER_ID FROM CENTRAL_SESSION_DETAILS SCT,LOCAL_SESSION_DETAILS LOC  WHERE SCT.CENTRAL_ID=LOC.CENTRAL_ID AND SCT.ENDED  IS NULL AND LOC.LOCAL_ID= ? ";
         


          LoggableStatement lst = new LoggableStatement(them_con, get_User_ID);
          lst.setString(1, sessionUserName);
          logger.info("Getting Session Value Query------------" + lst.getQueryString());
         

          ResultSet rst = lst.executeQuery();
          while (rst.next())
          {
            sessionUserName = rst.getString("USER_ID");
            logger.info("Getting Session Value Query-- user id value----------" + sessionUserName);
          }
          DBConnectionUtility.surrenderDB(them_con, lst, rst);
          logger.info("userName-----------" + sessionUserName);
        }
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return sessionUserName;
  }
}
