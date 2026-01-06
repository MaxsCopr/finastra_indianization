package in.co.stp.action;

import com.opensymphony.xwork2.ActionContext;
import in.co.clf.util.SystemPropertiesUtil;
import in.co.stp.businessdelegate.CheckerHomeBD;
import in.co.stp.businessdelegate.MakerHomeBD;
import in.co.stp.businessdelegate.StatusHomeBD;
import in.co.stp.dao.exception.ApplicationException;
import in.co.stp.utility.ActionConstants;
import in.co.stp.vo.CheckerHomeVO;
import in.co.stp.vo.ExcelDataVO;
import in.co.stp.vo.StatusHomeVO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Font;
import org.apache.struts2.ServletActionContext;

public class StatusHomeAction
  extends BaseAction
  implements ActionConstants
{
  private static final long serialVersionUID = 1L;
  private static Logger logger = LogManager.getLogger(CheckerHomeAction.class
    .getName());
  StatusHomeVO statusvo;
  ArrayList<ExcelDataVO> invoiceDetailsList;
  ArrayList<ExcelDataVO> invoiceAjaxDetails;
  ArrayList<CheckerHomeVO> invoiceList;
  ArrayList<CheckerHomeVO> invoiceApprovedList;
  String getInvoiceAjaxListval;
  Map<String, String> scfProductType;
  ByteArrayInputStream excelStream;
  String contentDisposition;
  public ArrayList<Object> aaData;
  ArrayList<Object> transactionList;
  Map<String, String> status;
 
  public StatusHomeVO getStatusvo()
  {
    return this.statusvo;
  }
 
  public void setStatusvo(StatusHomeVO statusvo)
  {
    this.statusvo = statusvo;
  }
 
  public String getGetInvoiceAjaxListval()
  {
    return this.getInvoiceAjaxListval;
  }
 
  public void setGetInvoiceAjaxListval(String getInvoiceAjaxListval)
  {
    this.getInvoiceAjaxListval = getInvoiceAjaxListval;
  }
 
  public ArrayList<ExcelDataVO> getInvoiceAjaxDetails()
  {
    return this.invoiceAjaxDetails;
  }
 
  public void setInvoiceAjaxDetails(ArrayList<ExcelDataVO> invoiceAjaxDetails)
  {
    this.invoiceAjaxDetails = invoiceAjaxDetails;
  }
 
  public Map<String, String> getStatus()
  {
    return ActionConstants.BLK_STATUS;
  }
 
  public void setStatus(Map<String, String> status)
  {
    this.status = status;
  }
 
  public ArrayList<Object> getTransactionList()
  {
    return this.transactionList;
  }
 
  public void setTransactionList(ArrayList<Object> transactionList)
  {
    this.transactionList = transactionList;
  }
 
  public ArrayList<ExcelDataVO> getInvoiceDetailsList()
  {
    return this.invoiceDetailsList;
  }
 
  public void setInvoiceDetailsList(ArrayList<ExcelDataVO> invoiceDetailsList)
  {
    this.invoiceDetailsList = invoiceDetailsList;
  }
 
  public ArrayList<CheckerHomeVO> getInvoiceApprovedList()
  {
    return this.invoiceApprovedList;
  }
 
  public void setInvoiceApprovedList(ArrayList<CheckerHomeVO> invoiceApprovedList)
  {
    this.invoiceApprovedList = invoiceApprovedList;
  }
 
  public ByteArrayInputStream getExcelStream()
  {
    return this.excelStream;
  }
 
  public void setExcelStream(ByteArrayInputStream excelStream)
  {
    this.excelStream = excelStream;
  }
 
  public String getContentDisposition()
  {
    return this.contentDisposition;
  }
 
  public void setContentDisposition(String contentDisposition)
  {
    this.contentDisposition = contentDisposition;
  }
 
  public ArrayList<Object> getAaData()
  {
    return this.aaData;
  }
 
  public void setAaData(ArrayList<Object> aaData)
  {
    this.aaData = aaData;
  }
 
  public String statusFlag = null;
  public String tesbatchID = null;
  public String batchID = null;
  private String invoiceAjaxListval;
  Map<String, String> STP_Product;
 
  public ArrayList<CheckerHomeVO> getInvoiceList()
  {
    return this.invoiceList;
  }
 
  public void setInvoiceList(ArrayList<CheckerHomeVO> invoiceList)
  {
    this.invoiceList = invoiceList;
  }
 
  public String getBatchID()
  {
    return this.batchID;
  }
 
  public void setBatchID(String batchID)
  {
    this.batchID = batchID;
  }
 
  public String getStatusFlag()
  {
    return this.statusFlag;
  }
 
  public void setStatusFlag(String statusFlag)
  {
    this.statusFlag = statusFlag;
  }
 
  public String execute()
    throws ApplicationException
  {
    logger.info("Entering Method");
    try
    {
      logger.info("Status Side App");
      isSessionAvailable();
    }
    catch (Exception exception)
    {
      throwApplicationException(exception);
    }
    logger.info("Exiting Method");
    return "success";
  }
 
  public String fetchInvoiceDetails()
    throws ApplicationException
  {
    logger.info("Entering Method");
    StatusHomeBD bd = null;
    try
    {
      bd = StatusHomeBD.getBD();
      this.statusvo = bd.fetchInvoiceDetails(this.statusvo);
      if (this.statusvo != null) {
        this.invoiceDetailsList = this.statusvo.getInvoiceDetails();
      }
    }
    catch (Exception exception)
    {
      throwApplicationException(exception);
    }
    logger.info("Entering Method");
    return "success";
  }
 
  public String isSessionAvailable()
    throws ApplicationException
  {
    logger.info("Entering Method");
    CheckerHomeBD bd = null;
    MakerHomeBD makerBD = null;
    HttpSession httpSession = null;
    Map<String, Object> session = null;
    try
    {
      httpSession = ServletActionContext.getRequest().getSession();
      httpSession.setAttribute("userRole", "maker");
      httpSession.removeAttribute("loginedUserName");
      makerBD = MakerHomeBD.getBD();
      bd = CheckerHomeBD.getBD();
     
      makerBD.setDate();
      bd.isSessionAvailable();
     
      session = ActionContext.getContext().getSession();
      session.remove("key");
      session.remove("id");
      session.put("closeURL", SystemPropertiesUtil.fetchCloseURL());
    }
    catch (Exception exception)
    {
      throwApplicationException(exception);
    }
    logger.info("Exiting Method");
    return "success";
  }
 
  public String checkerFilter()
  {
    return "success";
  }
 
  public String statusDownloadExcel()
    throws ApplicationException
  {
    logger.info("Entering Method");
    StatusHomeBD bd = null;
    try
    {
      bd = new StatusHomeBD();
     
      this.statusvo = bd.retrieveTransactionList(this.statusvo);
      if (this.statusvo != null) {
        this.transactionList = this.statusvo.getTransactionList();
      }
      this.excelStream = prepareExcelStream(this.statusvo);
    }
    catch (Exception exception)
    {
      throwApplicationException(exception);
    }
    logger.info("Exiting Method");
    return "json";
  }
 
  public ByteArrayInputStream prepareExcelStream(StatusHomeVO statusvo)
    throws ApplicationException, IOException
  {
    logger.info("Entering Method");
    setContentDisposition("attachment; filename=\"Invoice-list12.xls\"");
   
    ArrayList<Object> aaData = null;
    HSSFWorkbook hwb = null;
    try
    {
      aaData = new ArrayList();
      hwb = new HSSFWorkbook();
      HSSFSheet sheet = hwb.createSheet("Invoice-list12");
     
      int i = 0;
     

      Font f = hwb.createFont();
      f.setBoldweight((short)700);
      HSSFCellStyle style = hwb.createCellStyle();
      style.setAlignment((short)2);
      style.setFont(f);
     
      aaData = statusvo.getTransactionList();
     
      HashMap<Integer, String> map = new HashMap();
      map.put(Integer.valueOf(0), "DOCUMENT_RELEASE");
      map.put(Integer.valueOf(1), "PRODUCT_TYPE");
      map.put(Integer.valueOf(2), "RECEIVED_FROM_REF");
      map.put(Integer.valueOf(3), "RECEIVED_ON");
      map.put(Integer.valueOf(4), "COLLECTION_AMOUNT");
      map.put(Integer.valueOf(5), "FINANCE_REQUESTED");
      map.put(Integer.valueOf(6), "RECEIVED_FROM");
      map.put(Integer.valueOf(7), "SEND_TO");
      map.put(Integer.valueOf(8), "DRAWEE");
      map.put(Integer.valueOf(9), "TENOR_PERIOD");
      map.put(Integer.valueOf(10), "BASE_DATE");
      map.put(Integer.valueOf(11), "HAS_ATTACHED_DOC");
      map.put(Integer.valueOf(12), "DOCUMENT_CODE");
      map.put(Integer.valueOf(13), "FIRST_MAIL");
      map.put(Integer.valueOf(14), "SECOND_MAIL");
      map.put(Integer.valueOf(15), "TOTAL");
      map.put(Integer.valueOf(16), "NO_INVOICE");
      map.put(Integer.valueOf(17), "INVOICE_SERIAL_NO");
      map.put(Integer.valueOf(18), "INVOICE_DATE");
      map.put(Integer.valueOf(19), "INVOICE_AMT_CCY");
      map.put(Integer.valueOf(20), "DISCOUNTED_AMT_CCY");
      map.put(Integer.valueOf(21), "DEDUCTION_AMT_CCY");
      map.put(Integer.valueOf(22), "FINANCE_REF_NO");
      map.put(Integer.valueOf(23), "PRODUCT_TYPE2");
      map.put(Integer.valueOf(24), "PERIOD");
      map.put(Integer.valueOf(25), "INTEREST_ADVANCE");
      map.put(Integer.valueOf(26), "INTEREST_ARREARS");
      map.put(Integer.valueOf(27), "BASE_RATE");
      map.put(Integer.valueOf(28), "SPREAD_RATE");
      map.put(Integer.valueOf(29), "BATCH_ID");
     

      HSSFRow row = sheet.createRow(i);
      for (Integer key : map.keySet())
      {
        HSSFCell cell = row.createCell(key.intValue());
        cell.setCellStyle(style);
        cell.setCellValue((String)map.get(key));
      }
      i++;
      for (Object obj : aaData)
      {
        ExcelDataVO vo = (ExcelDataVO)obj;
        row = sheet.createRow(i);
       





























        i++;
      }
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      hwb.write(baos);
      this.excelStream = new ByteArrayInputStream(baos.toByteArray());
    }
    catch (Exception exception)
    {
      throwApplicationException(exception);
    }
    logger.info("Exiting Method");
    return this.excelStream;
  }
 
  public String fetchInvoiceList()
    throws ApplicationException
  {
    logger.info("Entering Method");
    StatusHomeBD bd = null;
    try
    {
      bd = StatusHomeBD.getBD();
     
      this.invoiceAjaxDetails = bd.fetchInvoiceList(this.invoiceAjaxListval, this.statusvo);
     
      this.statusvo = bd.fetchInvoiceDetails(this.statusvo);
      if (this.statusvo != null) {
        this.invoiceDetailsList = this.statusvo.getInvoiceDetails();
      }
    }
    catch (Exception exception)
    {
      throwApplicationException(exception);
    }
    logger.info("Entering Method");
    return "success";
  }
}
