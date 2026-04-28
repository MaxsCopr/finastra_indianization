package in.co.stp.dao;

import com.opensymphony.xwork2.ActionContext;
import in.co.stp.action.MakerHomeAction;
import in.co.stp.dao.exception.DAOException;
import in.co.stp.utility.ActionConstants;
import in.co.stp.utility.ActionConstantsQuery;
import in.co.stp.utility.CommonMethods;
import in.co.stp.utility.DBConnectionUtility;
import in.co.stp.utility.LoggableStatement;
import in.co.stp.vo.ExcelDataVO;
import in.co.stp.vo.StatusHomeVO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;
 
public class StatusHomeDAO
  extends AbstractDAO
  implements ActionConstants, ActionConstantsQuery
{
  static StatusHomeDAO dao;
  private static Logger logger = LogManager.getLogger(MakerHomeAction.class
    .getName());
  public static StatusHomeDAO getDAO()
  {
    if (dao == null) {
      dao = new StatusHomeDAO();
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
      HttpSession session = ServletActionContext.getRequest()
        .getSession();
      logger.info("RemoteUserNameBefore Login" + sessionUserName);
      logger.info("RemoteUserNameBefore Login" + sessionUserName);
      HttpServletRequest request = (HttpServletRequest)
        ActionContext.getContext().get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
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
  public StatusHomeVO fetchInvoiceDetails(StatusHomeVO statusvo)
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
    boolean statusFlag = false;
    int setValue = 0;
    try
    {
      con = DBConnectionUtility.getConnection();
      invoiceDetailsList = new ArrayList();

 
 
      commonMethods = new CommonMethods();
      String BLK_QUERY = "SELECT DISTINCT TRIM(BATCH_ID) AS BATCH_ID, TRIM(ODC_UP_DATE) AS ODC_UP_DATE,REMARKS FROM ETT_ODC_BLK_UPLOAD WHERE TRIM(BATCH_ID) = TRIM(BATCH_ID) ";
      if (!commonMethods.isNull(statusvo.getBatchId()))
      {
        BLK_QUERY = BLK_QUERY + " AND TRIM(BATCH_ID) = ?";
        batchIdFlag = true;
      }
      if (!commonMethods.isNull(statusvo.getOdcUpDate()))
      {
        BLK_QUERY = BLK_QUERY + " AND TRIM(ODC_UP_DATE) = TO_DATE(?,'DD-MM-YY') ";
        odcUpdateFlag = true;
      }
      if (!commonMethods.isNull(statusvo.getStatus()))
      {
        BLK_QUERY = BLK_QUERY + " AND STATUS = ? ";
        statusFlag = true;
      }
      ps = new LoggableStatement(con, BLK_QUERY);
      if (batchIdFlag) {
        ps.setString(++setValue, statusvo.getBatchId());
      }
      if (odcUpdateFlag) {
        ps.setString(++setValue, statusvo.getOdcUpDate());
      }
      if (statusFlag) {
        ps.setString(++setValue, statusvo.getStatus());
      }
      rs = ps.executeQuery();
      while (rs.next())
      {
        excelDataVO = new ExcelDataVO();

 
 
        invoiceDetailsList.add(excelDataVO);
      }
      statusvo.setInvoiceDetails(invoiceDetailsList);
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
    return statusvo;
  }
  public StatusHomeVO fetchInvoice(StatusHomeVO statusvo)
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
      String query = "SELECT DOCUMENT_RELEASE,PRODUCT_TYPE,RECEIVED_FROM_REF,RECEIVED_ON,COLLECTION_AMOUNT,FINANCE_REQUESTED,RECEIVED_FROM,SEND_TO,DRAWEE,TENOR_PERIOD,TO_CHAR(TO_DATE(BASE_DATE,'DD-MM-YY'),'DD-MM-YY') AS BASE_DATE,HAS_ATTACHED_DOC,DOCUMENT_CODE,FIRST_MAIL,SECOND_MAIL,TOTAL,NO_INVOICE,INVOICE_SERIAL_NO,TO_CHAR(TO_DATE(INVOICE_DATE,'DD-MM-YY'),'DD-MM-YY') AS INVOICE_DATE,INVOICE_AMT_CCY,DISCOUNTED_AMT_CCY,DEDUCTION_AMT_CCY,FINANCE_REF_NO,PRODUCT_TYPE2,PERIOD,INTEREST_ADVANCE,INTEREST_ARREARS,BASE_RATE,SPREAD_RATE,BATCH_ID FROM ETT_ODC_BLK_UPLOAD WHERE  TRIM(BATCH_ID) = ?  ";
      ps = new LoggableStatement(con, query);
      ps.setString(1, CommonMethods.nullAndTrimString(statusvo.getBatchId()).trim());
      rs = ps.executeQuery();
      while (rs.next())
      {
        excelDataVO = new ExcelDataVO();

 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
        transactionList.add(excelDataVO);
      }
      statusvo.setTransactionList(transactionList);
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
    return statusvo;
  }
  public ArrayList<ExcelDataVO> fetchInvoiceList(String invoiceAjaxListval, StatusHomeVO statusvo)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    ResultSet rs = null;
    LoggableStatement ps = null;
    ArrayList<ExcelDataVO> invoiceAjaxDetails = null;
    try
    {
      invoiceAjaxDetails = new ArrayList();
      con = DBConnectionUtility.getConnection();
      String query = "SELECT DOCUMENT_RELEASE,PRODUCT_TYPE,RECEIVED_FROM_REF,RECEIVED_ON,COLLECTION_AMOUNT,FINANCE_REQUESTED,RECEIVED_FROM,SEND_TO,DRAWEE,TENOR_PERIOD,TO_CHAR(TO_DATE(BASE_DATE,'DD-MM-YY'),'DD-MM-YY') AS BASE_DATE,HAS_ATTACHED_DOC,DOCUMENT_CODE,FIRST_MAIL,SECOND_MAIL,TOTAL,NO_INVOICE,INVOICE_SERIAL_NO,TO_CHAR(TO_DATE(INVOICE_DATE,'DD-MM-YY'),'DD-MM-YY') AS INVOICE_DATE,INVOICE_AMT_CCY,DISCOUNTED_AMT_CCY,DEDUCTION_AMT_CCY,FINANCE_REF_NO,PRODUCT_TYPE2,PERIOD,INTEREST_ADVANCE,INTEREST_ARREARS,BASE_RATE,SPREAD_RATE FROM ETT_ODC_BLK_UPLOAD WHERE  TRIM(BATCH_ID) = ? ";
      ps = new LoggableStatement(con, query);
      ps.setString(1, CommonMethods.nullAndTrimString(statusvo.getBatchId()).trim());
      rs = ps.executeQuery();
      while (rs.next())
      {
        ExcelDataVO vo = new ExcelDataVO();

 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
        invoiceAjaxDetails.add(vo);
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
    return invoiceAjaxDetails;
  }
}
