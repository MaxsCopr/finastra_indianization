package in.co.stp.dao;

import com.opensymphony.xwork2.ActionContext;
import in.co.stp.action.MakerHomeAction;
import in.co.stp.dao.exception.DAOException;
import in.co.stp.utility.ActionConstants;
import in.co.stp.utility.ActionConstantsQuery;
import in.co.stp.utility.CommonMethods;
import in.co.stp.utility.DBConnectionUtility;
import in.co.stp.utility.DataGridVO;
import in.co.stp.utility.LoggableStatement;
import in.co.stp.vo.CheckerHomeVO;
import in.co.stp.vo.ExcelDataVO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;

public class CheckerHomeDAO
  extends AbstractDAO
  implements ActionConstants, ActionConstantsQuery
{
  static CheckerHomeDAO dao;
  private static Logger logger = LogManager.getLogger(MakerHomeAction.class
    .getName());
 
  public static CheckerHomeDAO getDAO()
  {
    if (dao == null) {
      dao = new CheckerHomeDAO();
    }
    return dao;
  }
 
  public void isSessionAvailable()
    throws DAOException
  {
    String sessionUserName = null;
    String userId = null;
    try
    {
      HttpSession session = ServletActionContext.getRequest().getSession();
      logger.info("RemoteUserNameBefore Login" + sessionUserName);
      logger.info("RemoteUserNameBefore Login" + sessionUserName);
      HttpServletRequest request = (HttpServletRequest)ActionContext.getContext().get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
      sessionUserName = request.getRemoteUser();
      logger.info("RemoteUserNameAfter Login" + sessionUserName);
      logger.info("RemoteUserNameAfter Login" + sessionUserName);
      if (sessionUserName == null) {
        sessionUserName = "SUPERVISOR";
      }
      userId = getUserId(sessionUserName);
      session.setAttribute("loginedUserName", userId);
    }
    catch (Exception exception)
    {
      throwDAOException(exception);
    }
    logger.info("Exiting Method");
  }
 
  public String getUserId(String userName)
    throws DAOException
  {
    Connection con = null;
    LoggableStatement pst = null;
    String userId = null;
    ResultSet rs = null;
    try
    {
      con = DBConnectionUtility.getConnection();
      String getUserId = "select skey80 from secage88 WHERE NAME85 =?";
      pst = new LoggableStatement(con, getUserId);
      pst.setString(1, userName);
      rs = pst.executeQuery();
      while (rs.next()) {
        userId = String.valueOf(rs.getInt("SKEY80"));
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
    return userId;
  }
 
  public String getBulkUploadQuery(CheckerHomeVO checkervo)
    throws DAOException
  {
    logger.info("Entering Method");
    CommonMethods commonMethods = null;
    String BLK_QUERY = null;
    String userName = null;
    try
    {
      commonMethods = new CommonMethods();
    }
    catch (Exception exception)
    {
      throwDAOException(exception);
    }
    logger.info("Exiting Method");
    return BLK_QUERY;
  }
 
  public CheckerHomeVO fetchInvoiceDetails(CheckerHomeVO checkervo)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    ResultSet rs = null;
    LoggableStatement ps = null;
    ExcelDataVO excelDataVO = null;
    ArrayList<ExcelDataVO> invoiceDetailsList = null;
    CommonMethods commonMethods = null;
    boolean batchIdFlag = false;
    boolean odcUpdateFlag = false;
    int setValue = 0;
    try
    {
      con = DBConnectionUtility.getConnection();
      commonMethods = new CommonMethods();
     
      invoiceDetailsList = new ArrayList();
     


      HttpSession session = ServletActionContext.getRequest()
        .getSession();
      String userName = (String)session.getAttribute("loginedUserName");
     
      String BLK_QUERY = "SELECT DISTINCT TRIM(BATCH_ID) AS BATCH_ID, TRIM(ODC_UP_DATE) AS ODC_UP_DATE,REMARKS FROM ETT_ODC_BLK_UPLOAD WHERE STATUS = 'MU' ";
      if (!commonMethods.isNull(checkervo.getBatchId()))
      {
        BLK_QUERY = BLK_QUERY + " AND TRIM(BATCH_ID) = ?";
        batchIdFlag = true;
      }
      if (!commonMethods.isNull(checkervo.getOdcUpDate()))
      {
        BLK_QUERY = BLK_QUERY + " AND TRIM(ODC_UP_DATE) = TO_DATE(?,'DD-MM-YY') ";
        odcUpdateFlag = true;
      }
      if (!commonMethods.isNull(userName)) {
        BLK_QUERY = BLK_QUERY + " AND TRIM(MAKER_USERID) != ?";
      }
      ps = new LoggableStatement(con, BLK_QUERY);
      if (batchIdFlag) {
        ps.setString(++setValue, checkervo.getBatchId());
      }
      if (odcUpdateFlag) {
        ps.setString(++setValue, checkervo.getOdcUpDate());
      }
      if (!commonMethods.isNull(userName)) {
        ps.setString(++setValue, userName);
      }
      rs = ps.executeQuery();
      while (rs.next()) {
        excelDataVO = new ExcelDataVO();
      }
      checkervo.setInvoiceDetails(invoiceDetailsList);
    }
    catch (Exception exception)
    {
      throwDAOException(exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, ps, rs);
    }
    logger.info("Exiting Method");
    return checkervo;
  }
 
  public CheckerHomeVO fetchInvoice(CheckerHomeVO checkervo)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    ResultSet rs = null;
    LoggableStatement ps = null;
    ArrayList<Object> transactionList = null;
    ExcelDataVO excelDataVO = null;
    try
    {
      con = DBConnectionUtility.getConnection();
      transactionList = new ArrayList();
     
      String query = "SELECT DOCUMENT_RELEASE,PRODUCT_TYPE,RECEIVED_FROM_REF,RECEIVED_ON,COLLECTION_AMOUNT,FINANCE_REQUESTED,RECEIVED_FROM,SEND_TO,DRAWEE,TENOR_PERIOD,TO_CHAR(TO_DATE(BASE_DATE,'DD-MM-YY'),'DD-MM-YY') AS BASE_DATE,HAS_ATTACHED_DOC,DOCUMENT_CODE,FIRST_MAIL,SECOND_MAIL,TOTAL,NO_INVOICE,INVOICE_SERIAL_NO,TO_CHAR(TO_DATE(INVOICE_DATE,'DD-MM-YY'),'DD-MM-YY') AS INVOICE_DATE,INVOICE_AMT_CCY,DISCOUNTED_AMT_CCY,DEDUCTION_AMT_CCY,FINANCE_REF_NO,PRODUCT_TYPE2,PERIOD,INTEREST_ADVANCE,INTEREST_ARREARS,BASE_RATE,SPREAD_RATE,BATCH_ID,ERRORDESCRIPTION,STATUS FROM ETT_ODC_BLK_UPLOAD WHERE STATUS='MU' AND TRIM(BATCH_ID) = ? ";
     
      ps = new LoggableStatement(con, query);
      ps.setString(1, CommonMethods.nullAndTrimString(checkervo.getBatchId()).trim());
      rs = ps.executeQuery();
      while (rs.next()) {
        excelDataVO = new ExcelDataVO();
      }
      checkervo.setTransactionList(transactionList);
    }
    catch (Exception exception)
    {
      throwDAOException(exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, ps, rs);
    }
    logger.info("Exiting Method");
    return checkervo;
  }
 
  public DataGridVO fetchInvoiceList(String invoiceAjaxListval, CheckerHomeVO checkervo, DataGridVO daVO)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    ResultSet rs = null;
    LoggableStatement ps = null;
    int totalRows = 0;
    ArrayList<Object> invoiceAjaxDetails = null;
    ExcelDataVO vo = null;
    try
    {
      invoiceAjaxDetails = new ArrayList();
      con = DBConnectionUtility.getConnection();
     
      String query = "SELECT DOCUMENT_RELEASE,PRODUCT_TYPE,RECEIVED_FROM_REF,RECEIVED_ON,COLLECTION_AMOUNT,FINANCE_REQUESTED,RECEIVED_FROM,SEND_TO,DRAWEE,TENOR_PERIOD,TO_CHAR(TO_DATE(BASE_DATE,'DD-MM-YY'),'DD-MM-YY') AS BASE_DATE,HAS_ATTACHED_DOC,DOCUMENT_CODE,FIRST_MAIL,SECOND_MAIL,TOTAL,NO_INVOICE,INVOICE_SERIAL_NO,TO_CHAR(TO_DATE(INVOICE_DATE,'DD-MM-YY'),'DD-MM-YY') AS INVOICE_DATE,INVOICE_AMT_CCY,DISCOUNTED_AMT_CCY,DEDUCTION_AMT_CCY,FINANCE_REF_NO,PRODUCT_TYPE2,PERIOD,INTEREST_ADVANCE,INTEREST_ARREARS,BASE_RATE,SPREAD_RATE,BRANCH FROM ETT_ODC_BLK_UPLOAD WHERE STATUS='MU' AND TRIM(BATCH_ID) = ? ";
     
      ps = new LoggableStatement(con, query);
      ps.setString(1, CommonMethods.nullAndTrimString("211220160000153").trim());
      logger.info(ps.getQueryString());
      rs = ps.executeQuery();
      while (rs.next())
      {
        vo = new ExcelDataVO();
       






























        totalRows++;
      }
      daVO.setAaData(invoiceAjaxDetails);
      daVO.setiTotalRecords(totalRows);
    }
    catch (Exception exception)
    {
      throwDAOException(exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, ps, rs);
    }
    logger.info("Exiting Method");
    return daVO;
  }
 
  public int updateAllODC(CheckerHomeVO checkervo)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    ResultSet rs = null;
    LoggableStatement ps = null;
    ArrayList<ExcelDataVO> transactionList = null;
    EventCreationDAO xmlGenerationDAO = null;
    CommonMethods commonMethods = null;
    String xmlString = null;
    int count = 0;
    String userName = null;
    try
    {
      xmlGenerationDAO = new EventCreationDAO();
     
      HttpSession session = ServletActionContext.getRequest()
        .getSession();
      userName = (String)session.getAttribute("loginedUserName");
     
      con = DBConnectionUtility.getConnection();
      commonMethods = new CommonMethods();
      if ((!commonMethods.isNull(checkervo.getStatusFlag())) &&
        (checkervo.getStatusFlag().equalsIgnoreCase("uploadall")))
      {
        String updateQuery = "UPDATE ETT_ODC_BLK_UPLOAD SET STATUS = 'CA',CHECKER_USERID = ?,UPDATEDON = SYSTIMESTAMP WHERE TRIM(BATCH_ID) = ?";
       

        ps = new LoggableStatement(con, updateQuery);
        ps.setString(1, userName);
        ps.setString(2, checkervo.getBatchId());
       
        count = ps.executeUpdate();
      }
      else if ((!commonMethods.isNull(checkervo.getStatusFlag())) &&
        (checkervo.getStatusFlag().equalsIgnoreCase("rejectall")))
      {
        logger.info("ENTERING ELSE PART FOR REJECT");
        String updateQuery = "UPDATE ETT_ODC_BLK_UPLOAD SET STATUS = 'CR',CHECKER_USERID = ?,UPDATEDON = SYSTIMESTAMP WHERE TRIM(BATCH_ID) = ?";
       

        ps = new LoggableStatement(con, updateQuery);
        ps.setString(1, userName);
        ps.setString(2, checkervo.getBatchId());
       
        count = ps.executeUpdate();
      }
      con.commit();
     
      transactionList = fetchTransactionList(checkervo);
      if (transactionList != null)
      {
        xmlString = xmlGenerationDAO.generateXmlTFOutwColNew(transactionList);
        if ((xmlString != null) &&
          (xmlString.equalsIgnoreCase("SUCCESS"))) {
          count = 1;
        }
      }
    }
    catch (Exception exception)
    {
      throwDAOException(exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, ps, rs);
    }
    logger.info("Exiting Method");
    return count;
  }
 
  public ArrayList<ExcelDataVO> fetchTransactionList(CheckerHomeVO checkervo)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    ResultSet rs = null;
    LoggableStatement ps = null;
    ArrayList<ExcelDataVO> transactionList = null;
    ExcelDataVO excelDataVO = null;
    try
    {
      transactionList = new ArrayList();
      con = DBConnectionUtility.getConnection();
     
      String query = "SELECT DOCUMENT_RELEASE,PRODUCT_TYPE,RECEIVED_FROM_REF,RECEIVED_ON,COLLECTION_AMOUNT,FINANCE_REQUESTED,RECEIVED_FROM,SEND_TO,DRAWEE,TENOR_PERIOD,TO_CHAR(TO_DATE(BASE_DATE,'DD-MM-YY'),'DD-MM-YY') AS BASE_DATE,HAS_ATTACHED_DOC,DOCUMENT_CODE,FIRST_MAIL,SECOND_MAIL,TOTAL,NO_INVOICE,INVOICE_SERIAL_NO,TO_CHAR(TO_DATE(INVOICE_DATE,'DD-MM-YY'),'DD-MM-YY') AS INVOICE_DATE,INVOICE_AMT_CCY,DISCOUNTED_AMT_CCY,DEDUCTION_AMT_CCY,FINANCE_REF_NO,PRODUCT_TYPE2,PERIOD,INTEREST_ADVANCE,INTEREST_ARREARS,BASE_RATE,SPREAD_RATE,BATCH_ID,ERRORDESCRIPTION,STATUS BRANCH FROM ETT_ODC_BLK_UPLOAD WHERE STATUS = 'CA' AND TRIM(BATCH_ID) = ? ";
     
      logger.info("the query is" + query);
     
      ps = new LoggableStatement(con, query);
      ps.setString(1, checkervo.getBatchId());
     
      rs = ps.executeQuery();
      while (rs.next())
      {
        excelDataVO = new ExcelDataVO();
       






























        transactionList.add(excelDataVO);
      }
    }
    catch (Exception exception)
    {
      throwDAOException(exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, ps, rs);
    }
    logger.info("Exiting Method");
    return transactionList;
  }
}
