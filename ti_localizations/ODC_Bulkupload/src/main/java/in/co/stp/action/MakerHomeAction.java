package in.co.stp.action;

import com.opensymphony.xwork2.ActionContext;
import in.co.clf.util.SystemPropertiesUtil;
import in.co.stp.businessdelegate.MakerHomeBD;
import in.co.stp.businessdelegate.exception.BusinessException;
import in.co.stp.dao.exception.ApplicationException;
import in.co.stp.utility.ActionConstants;
import in.co.stp.vo.AlertMessagesVO;
import in.co.stp.vo.ExcelDataVO;
import in.co.stp.vo.MakerHomeVO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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

public class MakerHomeAction
  extends BaseAction
{
  private static final long serialVersionUID = 1L;
  private static Logger logger = LogManager.getLogger(MakerHomeAction.class
    .getName());
  MakerHomeVO makervo;
  ArrayList<MakerHomeVO> customersList = null;
  int excelresult = 0;
  String importresult = null;
  File inputFile;
  FileInputStream fis = null;
  ArrayList<ExcelDataVO> invoiceList = null;
  ArrayList<MakerHomeVO> programList = null;
  ArrayList<MakerHomeVO> branchList = null;
  String batchID;
  String statusFlag = null;
  Map<String, String> STP_Product;
  Map<String, String> scfType;
  Map<String, String> scfSubtype;
  Map<String, String> scfDisbursement;
  Map<String, String> scfExposure;
  Map<String, String> scfProductType;
  ByteArrayInputStream excelStream;
  String contentDisposition;
  public ArrayList<Object> aaData;
  ArrayList<AlertMessagesVO> errorList = null;
 
  public ArrayList<AlertMessagesVO> getErrorList()
  {
    return this.errorList;
  }
 
  public void setErrorList(ArrayList<AlertMessagesVO> errorList)
  {
    this.errorList = errorList;
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
 
  public ArrayList<MakerHomeVO> getBranchList()
  {
    return this.branchList;
  }
 
  public void setBranchList(ArrayList<MakerHomeVO> branchList)
  {
    this.branchList = branchList;
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
 
  public ArrayList<MakerHomeVO> getProgramList()
  {
    return this.programList;
  }
 
  public void setProgramList(ArrayList<MakerHomeVO> programList)
  {
    this.programList = programList;
  }
 
  public void setSTP_Product(Map<String, String> sTP_Product)
  {
    this.STP_Product = sTP_Product;
  }
 
  public String getStatusFlag()
  {
    return this.statusFlag;
  }
 
  public void setStatusFlag(String statusFlag)
  {
    this.statusFlag = statusFlag;
  }
 
  public File getInputFile()
  {
    return this.inputFile;
  }
 
  public void setInputFile(File inputFile)
  {
    this.inputFile = inputFile;
  }
 
  public int getExcelresult()
  {
    return this.excelresult;
  }
 
  public void setExcelresult(int excelresult)
  {
    this.excelresult = excelresult;
  }
 
  public String getBatchID()
  {
    return this.batchID;
  }
 
  public void setBatchID(String batchID)
  {
    this.batchID = batchID;
  }
 
  public ArrayList<ExcelDataVO> getInvoiceList()
  {
    return this.invoiceList;
  }
 
  public void setInvoiceList(ArrayList<ExcelDataVO> invoiceList)
  {
    this.invoiceList = invoiceList;
  }
 
  public MakerHomeVO getMakervo()
  {
    return this.makervo;
  }
 
  public void setMakervo(MakerHomeVO makervo)
  {
    this.makervo = makervo;
  }
 
  public String execute()
    throws ApplicationException
  {
    logger.info("Entering Method");
    try
    {
      logger.info("Test App");
    }
    catch (Exception exception)
    {
      throwApplicationException(exception);
    }
    logger.info("Exiting Method");
    return "success";
  }
 
  public String landingPage()
    throws ApplicationException
  {
    logger.info("Entering Method");
    MakerHomeBD bd = null;
    HttpSession httpSession = null;
    Map<String, Object> session = null;
    try
    {
      httpSession = ServletActionContext.getRequest().getSession();
      httpSession.setAttribute("userRole", "maker");
     

      httpSession.removeAttribute("loginedUserName");
      bd = MakerHomeBD.getBD();
      if (bd != null)
      {
        bd.setDate();
        bd.isSessionAvailable();
      }
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
 
  public String buyerBranchSelection()
    throws ApplicationException
  {
    logger.info("Entering Method");
    MakerHomeBD bd = null;
    try
    {
      bd = MakerHomeBD.getBD();
      this.branchList = bd.getBranchList(this.makervo);
      setBranchList(this.branchList);
    }
    catch (Exception exception)
    {
      throwApplicationException(exception);
    }
    logger.info("Exiting Method");
    return "success";
  }
 
  public String excelValidate()
    throws ApplicationException, BusinessException
  {
    logger.info("Entering Method");
    MakerHomeBD bd = null;
    try
    {
      bd = MakerHomeBD.getBD();
     


      bd.getExcelValidate(this.makervo.getBatchId(), this.makervo);
      if (this.makervo != null) {
        this.invoiceList = this.makervo.getInvoiceList();
      }
    }
    catch (Exception exception)
    {
      throwApplicationException(exception);
    }
    logger.info("Exiting Method");
    return "success";
  }
 
  public String excelLoad()
    throws ApplicationException, BusinessException
  {
    logger.info("-----------excelLoad-------------");
    MakerHomeBD bd = null;
    try
    {
      bd = MakerHomeBD.getBD();
      this.makervo.setInputFile(this.inputFile);
      this.makervo = bd.getExcelLoad(this.makervo);
      String rowCheck = this.makervo.getRowCheck();
      logger.info("Rowcheck -------------*********---------->>>>" + rowCheck);
      if (this.makervo != null)
      {
        if ((rowCheck != null) && (rowCheck.equalsIgnoreCase("fail"))) {
          return "fail";
        }
        this.invoiceList = this.makervo.getInvoiceList();
      }
    }
    catch (Exception exception)
    {
      logger.info("---------------excelLoad-------------" + exception);
      throwApplicationException(exception);
    }
    logger.info("Exiting Method");
    return "success";
  }
 
  public String downloadExcel()
    throws ApplicationException
  {
    logger.info("Entering Method");
    MakerHomeBD bd = null;
    try
    {
      bd = new MakerHomeBD();
      if (this.makervo == null) {
        this.makervo = new MakerHomeVO();
      }
      this.makervo = bd.retrieveTransactionList(this.makervo);
     
      this.excelStream = prepareExcelStream(this.makervo);
    }
    catch (Exception exception)
    {
      logger.info("Exction ---------Download----------" + exception);
    }
    logger.info("Exiting Method");
    return "json";
  }
 
  public String downloadExcel1()
    throws ApplicationException
  {
    logger.info("Entering Method");
    MakerHomeBD bd = null;
    logger.info("Success;;");
    try
    {
      bd = new MakerHomeBD();
      if (this.makervo == null) {
        this.makervo = new MakerHomeVO();
      }
      this.makervo = bd.retrieveTransactionList1(this.makervo);
     
      this.excelStream = prepareExcelStream1(this.makervo);
    }
    catch (Exception exception)
    {
      throwApplicationException(exception);
    }
    logger.info("Exiting Method");
    return "json";
  }
 
  public ByteArrayInputStream prepareExcelStream(MakerHomeVO makervo)
    throws ApplicationException, IOException
  {
    logger.info("Entering Method");
    setContentDisposition("attachment; filename=\"ODC_Bulkupload_Validaton_List.xls\"");
   
    ArrayList<Object> aaData = null;
    HSSFWorkbook hwb = null;
    try
    {
      aaData = new ArrayList();
      hwb = new HSSFWorkbook();
      HSSFSheet sheet = hwb.createSheet("ODC_Bulkupload_Validaton_List");
     
      int i = 0;
     

      Font f = hwb.createFont();
      f.setBoldweight((short)700);
      HSSFCellStyle style = hwb.createCellStyle();
      style.setAlignment((short)2);
      style.setFont(f);
     
      aaData = makervo.getTransactionList();
     
      HashMap<Integer, String> map = new HashMap();
     

      map.put(Integer.valueOf(0), "THEIRREF");
      map.put(Integer.valueOf(1), "COLLECTING_BANK");
      map.put(Integer.valueOf(2), "BEHALFOFBRANCH");
      map.put(Integer.valueOf(3), "DRAWER");
      map.put(Integer.valueOf(4), "DRAWEECUSTOMERID");
     
      map.put(Integer.valueOf(5), "DRAWEE_NAME");
      map.put(Integer.valueOf(6), "DRAWEE_COUNTRY");
     
      map.put(Integer.valueOf(7), "COLLECTIONAMOUNT");
      map.put(Integer.valueOf(8), "COLLECTIONCURRENCY");
     
      map.put(Integer.valueOf(9), "SHIPMENTTOCOUNTRY");
      map.put(Integer.valueOf(10), "SHIPMENTFROMCOUNTRY");
      map.put(Integer.valueOf(11), "HSCODE");
      map.put(Integer.valueOf(12), "INCOTERMS");
      map.put(Integer.valueOf(13), "GOODSCODE");
      map.put(Integer.valueOf(14), "GOODDESCRIPTION");
      map.put(Integer.valueOf(15), "PORTOFDESTINATION");
      map.put(Integer.valueOf(16), "PORTOFLOADING");
      map.put(Integer.valueOf(17), "TRANSPORTDOCNO");
      map.put(Integer.valueOf(18), "TRANSPORTDOCDATE");
      map.put(Integer.valueOf(19), "INVOICENO");
      map.put(Integer.valueOf(20), "INVOICEDATE");
      map.put(Integer.valueOf(21), "FORM_TYPE");
      map.put(Integer.valueOf(22), "SHPBILL_NO");
      map.put(Integer.valueOf(23), "BILL_DATE");
      map.put(Integer.valueOf(24), "PORT_CODE");
      map.put(Integer.valueOf(25), "FORM_NO");
      map.put(Integer.valueOf(26), "SHP_AMT");
      map.put(Integer.valueOf(27), "SHP_CURRENCY");
     
      map.put(Integer.valueOf(28), "REASON_SHORT_SHP_AMT");
      map.put(Integer.valueOf(29), "SHORT_SHP_AMT");
     
      map.put(Integer.valueOf(30), "REPAMT_WRT_AMT");
      map.put(Integer.valueOf(31), "SHORTCOLLECTIONAMOUNT");
      map.put(Integer.valueOf(32), "REMITTANCE_NUM");
      map.put(Integer.valueOf(33), "FIRC_NO");
      map.put(Integer.valueOf(34), "AVAILABLE_AMOUNT");
      map.put(Integer.valueOf(35), "UTILIZATION_AMOUNT");
      map.put(Integer.valueOf(36), "REMITTANCE_ADCODE");
      map.put(Integer.valueOf(37), "REMITTER_NAME");
      map.put(Integer.valueOf(38), "REMITTER_DATE");
      map.put(Integer.valueOf(39), "REMITTER_COUNTRY");
      map.put(Integer.valueOf(40), "CIF_NUMBER");
      map.put(Integer.valueOf(41), "RECORD_INDICATOR");
      map.put(Integer.valueOf(42), "LEO_DATE");
      map.put(Integer.valueOf(43), "EXPORT_AGENCY");
      map.put(Integer.valueOf(44), "EXPORT_TYPE");
      map.put(Integer.valueOf(45), "DESTINATION_COUNTRRY");
      map.put(Integer.valueOf(46), "IECODE");
      map.put(Integer.valueOf(47), "ADCODE");
      map.put(Integer.valueOf(48), "CUSTOMS_NUMBER");
      map.put(Integer.valueOf(49), "SHP_INV_SNO");
      map.put(Integer.valueOf(50), "SHP_INVOICE_NO");
      map.put(Integer.valueOf(51), "SHP_INVOICE_DATE");
      map.put(Integer.valueOf(52), "FOB_AMT");
      map.put(Integer.valueOf(53), "FREIGHT_AMT");
      map.put(Integer.valueOf(54), "INS_AMT");
      map.put(Integer.valueOf(55), "COMM_AMT");
      map.put(Integer.valueOf(56), "DISCOUNT_AMT");
      map.put(Integer.valueOf(57), "DEDUCTION_AMT");
      map.put(Integer.valueOf(58), "PACKAGE_AMT");
      map.put(Integer.valueOf(59), "ERRORDESCRIPTION");
      map.put(Integer.valueOf(60), "STATUS");
     



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
       

        row.createCell(0).setCellValue(vo.getTheirref());
        row.createCell(1).setCellValue(vo.getCollecting_bank());
        row.createCell(2).setCellValue(vo.getBehalfofbranch());
        row.createCell(3).setCellValue(vo.getDrawer());
        row.createCell(4).setCellValue(vo.getDraweecustomerid());
       

        row.createCell(5).setCellValue(vo.getDrawee_address());
        row.createCell(6).setCellValue(vo.getDrawee_name());
       
        row.createCell(7).setCellValue(vo.getCollectionamount());
        row.createCell(8).setCellValue(vo.getCollectioncurrency());
        row.createCell(9).setCellValue(vo.getDrawee_name());
        row.createCell(10).setCellValue(vo.getShipmentfromcountry());
        row.createCell(11).setCellValue(vo.getHscode());
        row.createCell(12).setCellValue(vo.getIncoterms());
        row.createCell(13).setCellValue(vo.getGoodscode());
        row.createCell(14).setCellValue(vo.getGooddescription());
        row.createCell(15).setCellValue(vo.getPortofdestination());
        row.createCell(16).setCellValue(vo.getPortofloading());
        row.createCell(17).setCellValue(vo.getTransportdocno());
        row.createCell(18).setCellValue(vo.getTransportdocdate());
        row.createCell(19).setCellValue(vo.getInvoiceno());
        row.createCell(20).setCellValue(vo.getInvoicedate());
        row.createCell(21).setCellValue(vo.getForm_type());
        row.createCell(22).setCellValue(vo.getShpbill_no());
        row.createCell(23).setCellValue(vo.getBill_date());
        row.createCell(24).setCellValue(vo.getPort_code());
        row.createCell(25).setCellValue(vo.getForm_no());
        row.createCell(26).setCellValue(vo.getShp_amt());
        row.createCell(27).setCellValue(vo.getShp_currency());
       
        row.createCell(28).setCellValue(vo.getReason_short_shp_amt());
        row.createCell(29).setCellValue(vo.getShort_shp_amt());
       
        row.createCell(30).setCellValue(vo.getRepamt_wrt_amt());
        row.createCell(31).setCellValue(vo.getShortcollectionamount());
        row.createCell(32).setCellValue(vo.getRemittance_num());
        row.createCell(33).setCellValue(vo.getFirc_no());
        row.createCell(34).setCellValue(vo.getAvailable_amount());
        row.createCell(35).setCellValue(vo.getUtilization_amount());
        row.createCell(36).setCellValue(vo.getRemittance_adcode());
        row.createCell(37).setCellValue(vo.getRemitter_name());
        row.createCell(38).setCellValue(vo.getRemitter_date());
        row.createCell(39).setCellValue(vo.getRemitter_country());
        row.createCell(40).setCellValue(vo.getCif_number());
        row.createCell(41).setCellValue(vo.getRecord_indicator());
        row.createCell(42).setCellValue(vo.getLeo_date());
        row.createCell(43).setCellValue(vo.getExport_agency());
        row.createCell(44).setCellValue(vo.getExport_type());
        row.createCell(45).setCellValue(vo.getDestination_countrry());
        row.createCell(46).setCellValue(vo.getIecode());
        row.createCell(47).setCellValue(vo.getAdcode());
        row.createCell(48).setCellValue(vo.getCustoms_number());
        row.createCell(49).setCellValue(vo.getShp_inv_sno());
        row.createCell(50).setCellValue(vo.getShp_invoice_no());
        row.createCell(51).setCellValue(vo.getShp_invoice_date());
        row.createCell(52).setCellValue(vo.getFob_amt());
        row.createCell(53).setCellValue(vo.getFreight_amt());
        row.createCell(54).setCellValue(vo.getIns_amt());
        row.createCell(55).setCellValue(vo.getComm_amt());
        row.createCell(56).setCellValue(vo.getDiscount_amt());
        row.createCell(57).setCellValue(vo.getDeduction_amt());
        row.createCell(58).setCellValue(vo.getPackage_amt());
        row.createCell(59).setCellValue(vo.getErrordtls());
        row.createCell(60).setCellValue(vo.getStatus());
       
        i++;
      }
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      hwb.write(baos);
      this.excelStream = new ByteArrayInputStream(baos.toByteArray());
    }
    catch (Exception exception)
    {
      logger.info("Exception Retriveing List-------------->" + exception);
    }
    logger.info("Exiting Method");
    return this.excelStream;
  }
 
  public ByteArrayInputStream prepareExcelStream1(MakerHomeVO makervo)
    throws ApplicationException, IOException
  {
    logger.info("Entering Method");
    setContentDisposition("attachment; filename=\"ODC_Bulkupload_Completed_List.xls\"");
    ArrayList<Object> aaData = null;
    HSSFWorkbook hwb = null;
    try
    {
      aaData = new ArrayList();
      hwb = new HSSFWorkbook();
      HSSFSheet sheet = hwb.createSheet("ODC_Bulkupload_Completed_List");
     
      int i = 0;
     

      Font f = hwb.createFont();
      f.setBoldweight((short)700);
      HSSFCellStyle style = hwb.createCellStyle();
      style.setAlignment((short)2);
      style.setFont(f);
     
      aaData = makervo.getTransactionList();
     
      HashMap<Integer, String> map = new HashMap();
     
      map.put(Integer.valueOf(0), "TI_REFRENCE");
      map.put(Integer.valueOf(1), "COLLECTING_BANK");
      map.put(Integer.valueOf(2), "TI_STATUS");
      map.put(Integer.valueOf(3), "THEIRREF");
      map.put(Integer.valueOf(4), "BEHALFOFBRANCH");
      map.put(Integer.valueOf(5), "DRAWER");
      map.put(Integer.valueOf(6), "DRAWEECUSTOMERID");
     
      map.put(Integer.valueOf(7), "DRAWEE_NAME");
      map.put(Integer.valueOf(8), "DRAWEE_COUNTRY");
     
      map.put(Integer.valueOf(9), "COLLECTIONAMOUNT");
      map.put(Integer.valueOf(10), "COLLECTIONCURRENCY");
     
      map.put(Integer.valueOf(11), "SHIPMENTTOCOUNTRY");
      map.put(Integer.valueOf(12), "SHIPMENTFROMCOUNTRY");
      map.put(Integer.valueOf(13), "HSCODE");
      map.put(Integer.valueOf(14), "INCOTERMS");
      map.put(Integer.valueOf(15), "GOODSCODE");
      map.put(Integer.valueOf(16), "GOODDESCRIPTION");
      map.put(Integer.valueOf(17), "PORTOFDESTINATION");
      map.put(Integer.valueOf(18), "PORTOFLOADING");
      map.put(Integer.valueOf(19), "TRANSPORTDOCNO");
      map.put(Integer.valueOf(20), "TRANSPORTDOCDATE");
      map.put(Integer.valueOf(21), "INVOICENO");
      map.put(Integer.valueOf(22), "INVOICEDATE");
      map.put(Integer.valueOf(23), "FORM_TYPE");
      map.put(Integer.valueOf(24), "SHPBILL_NO");
      map.put(Integer.valueOf(25), "BILL_DATE");
      map.put(Integer.valueOf(26), "PORT_CODE");
      map.put(Integer.valueOf(27), "FORM_NO");
      map.put(Integer.valueOf(28), "SHP_AMT");
      map.put(Integer.valueOf(29), "SHP_CURRENCY");
     
      map.put(Integer.valueOf(30), "REASON_SHORT_SHP_AMT");
      map.put(Integer.valueOf(31), "SHORT_SHP_AMT");
     
      map.put(Integer.valueOf(32), "REPAMT_WRT_AMT");
      map.put(Integer.valueOf(33), "SHORTCOLLECTIONAMOUNT");
      map.put(Integer.valueOf(34), "REMITTANCE_NUM");
      map.put(Integer.valueOf(35), "FIRC_NO");
      map.put(Integer.valueOf(36), "AVAILABLE_AMOUNT");
      map.put(Integer.valueOf(37), "UTILIZATION_AMOUNT");
      map.put(Integer.valueOf(38), "REMITTANCE_ADCODE");
      map.put(Integer.valueOf(39), "REMITTER_NAME");
      map.put(Integer.valueOf(40), "REMITTER_DATE");
      map.put(Integer.valueOf(41), "REMITTER_COUNTRY");
      map.put(Integer.valueOf(42), "CIF_NUMBER");
      map.put(Integer.valueOf(43), "RECORD_INDICATOR");
      map.put(Integer.valueOf(44), "LEO_DATE");
      map.put(Integer.valueOf(45), "EXPORT_AGENCY");
      map.put(Integer.valueOf(46), "EXPORT_TYPE");
      map.put(Integer.valueOf(47), "DESTINATION_COUNTRRY");
      map.put(Integer.valueOf(48), "IECODE");
      map.put(Integer.valueOf(49), "ADCODE");
      map.put(Integer.valueOf(50), "CUSTOMS_NUMBER");
      map.put(Integer.valueOf(51), "SHP_INV_SNO");
      map.put(Integer.valueOf(52), "SHP_INVOICE_NO");
      map.put(Integer.valueOf(53), "SHP_INVOICE_DATE");
      map.put(Integer.valueOf(54), "FOB_AMT");
      map.put(Integer.valueOf(55), "FREIGHT_AMT");
      map.put(Integer.valueOf(56), "INS_AMT");
      map.put(Integer.valueOf(57), "COMM_AMT");
      map.put(Integer.valueOf(58), "DISCOUNT_AMT");
      map.put(Integer.valueOf(59), "DEDUCTION_AMT");
      map.put(Integer.valueOf(60), "PACKAGE_AMT");
      map.put(Integer.valueOf(61), "ERRORDESCRIPTION");
      map.put(Integer.valueOf(62), "STATUS");
     

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
       
        row.createCell(0).setCellValue(vo.getTi_reference());
        row.createCell(1).setCellValue(vo.getCollecting_bank());
        row.createCell(2).setCellValue(vo.getTi_status());
        row.createCell(3).setCellValue(vo.getTheirref());
        row.createCell(4).setCellValue(vo.getBehalfofbranch());
        row.createCell(5).setCellValue(vo.getDrawer());
        row.createCell(6).setCellValue(vo.getDraweecustomerid());
       
        row.createCell(7).setCellValue(vo.getDrawee_address());
        row.createCell(8).setCellValue(vo.getDrawee_name());
       
        row.createCell(9).setCellValue(vo.getCollectionamount());
        row.createCell(10).setCellValue(vo.getCollectioncurrency());
       
        row.createCell(11).setCellValue(vo.getShipmenttocountry());
        row.createCell(12).setCellValue(vo.getShipmentfromcountry());
        row.createCell(13).setCellValue(vo.getHscode());
        row.createCell(14).setCellValue(vo.getIncoterms());
        row.createCell(15).setCellValue(vo.getGoodscode());
        row.createCell(16).setCellValue(vo.getGooddescription());
        row.createCell(17).setCellValue(vo.getPortofdestination());
        row.createCell(18).setCellValue(vo.getPortofloading());
        row.createCell(19).setCellValue(vo.getTransportdocno());
        row.createCell(20).setCellValue(vo.getTransportdocdate());
        row.createCell(21).setCellValue(vo.getInvoiceno());
        row.createCell(22).setCellValue(vo.getInvoicedate());
        row.createCell(23).setCellValue(vo.getForm_type());
        row.createCell(24).setCellValue(vo.getShpbill_no());
        row.createCell(25).setCellValue(vo.getBill_date());
        row.createCell(26).setCellValue(vo.getPort_code());
        row.createCell(27).setCellValue(vo.getForm_no());
        row.createCell(28).setCellValue(vo.getShp_amt());
        row.createCell(29).setCellValue(vo.getShp_currency());
       
        row.createCell(30).setCellValue(vo.getReason_short_shp_amt());
        row.createCell(31).setCellValue(vo.getShort_shp_amt());
       
        row.createCell(32).setCellValue(vo.getRepamt_wrt_amt());
        row.createCell(33).setCellValue(vo.getShortcollectionamount());
        row.createCell(34).setCellValue(vo.getRemittance_num());
        row.createCell(35).setCellValue(vo.getFirc_no());
        row.createCell(36).setCellValue(vo.getAvailable_amount());
        row.createCell(37).setCellValue(vo.getUtilization_amount());
        row.createCell(38).setCellValue(vo.getRemittance_adcode());
        row.createCell(39).setCellValue(vo.getRemitter_name());
        row.createCell(40).setCellValue(vo.getRemitter_date());
        row.createCell(41).setCellValue(vo.getRemitter_country());
        row.createCell(42).setCellValue(vo.getCif_number());
        row.createCell(43).setCellValue(vo.getRecord_indicator());
        row.createCell(44).setCellValue(vo.getLeo_date());
        row.createCell(45).setCellValue(vo.getExport_agency());
        row.createCell(46).setCellValue(vo.getExport_type());
        row.createCell(47).setCellValue(vo.getDestination_countrry());
        row.createCell(48).setCellValue(vo.getIecode());
        row.createCell(49).setCellValue(vo.getAdcode());
        row.createCell(50).setCellValue(vo.getCustoms_number());
        row.createCell(51).setCellValue(vo.getShp_inv_sno());
        row.createCell(52).setCellValue(vo.getShp_invoice_no());
        row.createCell(53).setCellValue(vo.getShp_invoice_date());
        row.createCell(54).setCellValue(vo.getFob_amt());
        row.createCell(55).setCellValue(vo.getFreight_amt());
        row.createCell(56).setCellValue(vo.getIns_amt());
        row.createCell(57).setCellValue(vo.getComm_amt());
        row.createCell(58).setCellValue(vo.getDiscount_amt());
        row.createCell(59).setCellValue(vo.getDeduction_amt());
        row.createCell(60).setCellValue(vo.getPackage_amt());
        row.createCell(61).setCellValue(vo.getErrordtls());
        row.createCell(62).setCellValue(vo.getStatus());
       
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
 
  public String makerUpload()
    throws ApplicationException, BusinessException
  {
    logger.info("Ti XML Total Time Starts------------" + new Date());
    logger.info("Entering Method");
    MakerHomeBD bd = null;
   
    int count = 0;
    try
    {
      bd = MakerHomeBD.getBD();
      this.makervo.setStatusFlag(this.statusFlag);
     






      count = bd.makerUpload(this.makervo);
      if ((this.statusFlag != null) &&
        (this.statusFlag.equalsIgnoreCase("uploadall")))
      {
        if (count > 0) {
          return "success";
        }
      }
      else if ((this.statusFlag != null) &&
        (this.statusFlag.equalsIgnoreCase("rejectall")) &&
        (count > 0)) {
        return "fail";
      }
    }
    catch (Exception exception)
    {
      throwApplicationException(exception);
     
      logger.info("Ti XML Total Time Ends------------" + new Date());
      logger.info("Exiting Method");
    }
    return "success";
  }
 
  public String makerrejectAll()
    throws ApplicationException, BusinessException
  {
    MakerHomeBD bd = null;
    int count = 0;
    try
    {
      bd = MakerHomeBD.getBD();
      count = bd.reject(this.makervo);
    }
    catch (Exception e)
    {
      throwApplicationException(e);
    }
    return "success";
  }
}
