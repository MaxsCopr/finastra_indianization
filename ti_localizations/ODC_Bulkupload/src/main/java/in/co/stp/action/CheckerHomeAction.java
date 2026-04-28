package in.co.stp.action;

import com.opensymphony.xwork2.ActionContext;
import in.co.clf.util.SystemPropertiesUtil;
import in.co.stp.businessdelegate.CheckerHomeBD;
import in.co.stp.businessdelegate.MakerHomeBD;
import in.co.stp.businessdelegate.exception.BusinessException;
import in.co.stp.dao.exception.ApplicationException;
import in.co.stp.utility.ActionConstants;
import in.co.stp.vo.CheckerHomeVO;
import in.co.stp.vo.ExcelDataVO;
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

public class CheckerHomeAction
  extends BaseAction
  implements ActionConstants
{
  private static final long serialVersionUID = 1L;
  private static Logger logger = LogManager.getLogger(CheckerHomeAction.class
    .getName());
  CheckerHomeVO checkervo;
  ArrayList<ExcelDataVO> invoiceDetailsList;
  ArrayList<ExcelDataVO> invoiceAjaxDetails;
  ArrayList<CheckerHomeVO> invoiceList;
  ArrayList<CheckerHomeVO> invoiceApprovedList;
  String getInvoiceAjaxListval;
 
  public String getGetInvoiceAjaxListval()
  {
    return this.getInvoiceAjaxListval;
  }
 
  public void setGetInvoiceAjaxListval(String getInvoiceAjaxListval)
  {
    this.getInvoiceAjaxListval = getInvoiceAjaxListval;
  }
 
  ArrayList<CheckerHomeVO> programList = null;
  Map<String, String> scfType;
  Map<String, String> scfSubtype;
  Map<String, String> scfDisbursement;
  Map<String, String> scfExposure;
  Map<String, String> scfProductType;
  ByteArrayInputStream excelStream;
  String contentDisposition;
  public ArrayList<Object> aaData;
  ArrayList<Object> transactionList;
 
  public ArrayList<ExcelDataVO> getInvoiceAjaxDetails()
  {
    return this.invoiceAjaxDetails;
  }
 
  public void setInvoiceAjaxDetails(ArrayList<ExcelDataVO> invoiceAjaxDetails)
  {
    this.invoiceAjaxDetails = invoiceAjaxDetails;
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
 
  public CheckerHomeVO getCheckervo()
  {
    return this.checkervo;
  }
 
  public void setCheckervo(CheckerHomeVO checkervo)
  {
    this.checkervo = checkervo;
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
 
  public Map<String, String> getScfDisbursement()
  {
    return ActionConstants.SCF_DISBURSEMENT;
  }
 
  public void setScfDisbursement(Map<String, String> scfDisbursement)
  {
    this.scfDisbursement = scfDisbursement;
  }
 
  public Map<String, String> getScfSubtype()
  {
    return ActionConstants.SCF_SUBTYPE;
  }
 
  public void setScfSubtype(Map<String, String> scfSubtype)
  {
    this.scfSubtype = scfSubtype;
  }
 
  public Map<String, String> getScfType()
  {
    return ActionConstants.SCF_TYPE;
  }
 
  public void setScfType(Map<String, String> scfType)
  {
    this.scfType = scfType;
  }
 
  public Map<String, String> getScfExposure()
  {
    return ActionConstants.SCF_EXPOSURE;
  }
 
  public void setScfExposure(Map<String, String> scfExposure)
  {
    this.scfExposure = scfExposure;
  }
 
  public Map<String, String> getScfProductType()
  {
    return ActionConstants.SCF_PRODUCT_TYPE;
  }
 
  public void setScfProductType(Map<String, String> scfProductType)
  {
    this.scfProductType = scfProductType;
  }
 
  public ArrayList<CheckerHomeVO> getProgramList()
  {
    return this.programList;
  }
 
  public void setProgramList(ArrayList<CheckerHomeVO> programList)
  {
    this.programList = programList;
  }
 
  public String statusFlag = null;
  public String tesbatchID = null;
  public String batchID = null;
  Map<String, String> STP_Product;
 
  public ArrayList<CheckerHomeVO> getInvoiceList()
  {
    return this.invoiceList;
  }
 
  public void setInvoiceList(ArrayList<CheckerHomeVO> invoiceList)
  {
    this.invoiceList = invoiceList;
  }
 
  public void setSTP_Product(Map<String, String> sTP_Product)
  {
    this.STP_Product = sTP_Product;
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
      logger.info("Checker Side App");
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
    CheckerHomeBD bd = null;
    try
    {
      bd = CheckerHomeBD.getBD();
      this.checkervo = bd.fetchInvoiceDetails(this.checkervo);
      if (this.checkervo != null) {
        this.invoiceDetailsList = this.checkervo.getInvoiceDetails();
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
      String username = (String)httpSession.getAttribute("loginedUserName");
     
      logger.info("Session User Name---------------------" + username);
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
 
  public String checkerDownloadExcel()
    throws ApplicationException
  {
    logger.info("Entering Method");
    CheckerHomeBD bd = null;
    try
    {
      bd = new CheckerHomeBD();
     
      this.checkervo = bd.retrieveTransactionList(this.checkervo);
      if (this.checkervo != null) {
        this.transactionList = this.checkervo.getTransactionList();
      }
      this.excelStream = prepareExcelStream(this.checkervo);
    }
    catch (Exception exception)
    {
      throwApplicationException(exception);
    }
    logger.info("Exiting Method");
    return "json";
  }
 
  public ByteArrayInputStream prepareExcelStream(CheckerHomeVO checkervo)
    throws ApplicationException, IOException
  {
    logger.info("Entering Method");
    setContentDisposition("attachment; filename=\"Checker_Invoice_List.xls\"");
   
    ArrayList<Object> aaData = null;
    HSSFWorkbook hwb = null;
    try
    {
      aaData = new ArrayList();
      hwb = new HSSFWorkbook();
      HSSFSheet sheet = hwb.createSheet("Checker_Invoice_List");
     
      int i = 0;
     

      Font f = hwb.createFont();
      f.setBoldweight((short)700);
      HSSFCellStyle style = hwb.createCellStyle();
      style.setAlignment((short)2);
      style.setFont(f);
     
      aaData = checkervo.getTransactionList();
     
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
 
  public String checkerUpload()
    throws ApplicationException, BusinessException
  {
    logger.info("Entering Method");
    CheckerHomeBD bd = null;
    int count = 0;
    try
    {
      bd = CheckerHomeBD.getBD();
      this.checkervo.setStatusFlag(this.statusFlag);
     
      count = bd.checkerUpload(this.checkervo);
      logger.info("method entering");
      if ((this.statusFlag != null) && (this.statusFlag.equalsIgnoreCase("uploadall")))
      {
        if (count > 0) {
          return "success";
        }
      }
      else if ((this.statusFlag != null) && (this.statusFlag.equalsIgnoreCase("rejectall")) &&
        (count > 0)) {
        return "fail";
      }
    }
    catch (Exception exception)
    {
      throwApplicationException(exception);
     
      logger.info("Exiting Method");
    }
    return "success";
  }
}
