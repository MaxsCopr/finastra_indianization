package in.co.boe.action;

import in.co.boe.businessdelegate.bulkUpload.ManualBOEBulkUploadBD;
import in.co.boe.businessdelegate.exception.BusinessException;
import in.co.boe.dao.exception.ApplicationException;
import in.co.boe.utility.CommonMethods;
import in.co.boe.vo.AlertMessagesVO;
import in.co.boe.vo.BoeVO;
import in.co.boe.vo.ManualBOEBulkUploadVO;
import in.co.boe.vo.TransactionVO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
 
public class ManualBOEBulkUploadAction
  extends BoeBaseAction
{
  private static final long serialVersionUID = 1L;
  private static Logger logger = Logger.getLogger(ManualBOEBulkUploadAction.class.getName());
  BoeVO boeVO = null;
  ArrayList<TransactionVO> invoiceList = null;
  ArrayList<TransactionVO> tiList = null;
  File inputFile;
  String fileNameRef;
  FileInputStream fis = null;
  ManualBOEBulkUploadVO bulkVO = null;
  private ArrayList<ManualBOEBulkUploadVO> manualBoevoList;
  private String batchId;
  public String getFileNameRef()
  {
    return this.fileNameRef;
  }
  public void setFileNameRef(String fileNameRef)
  {
    this.fileNameRef = fileNameRef;
  }
  public ArrayList<ManualBOEBulkUploadVO> getManualBoevoList()
  {
    return this.manualBoevoList;
  }
  public void setManualBoevoList(ArrayList<ManualBOEBulkUploadVO> manualBoevoList)
  {
    this.manualBoevoList = manualBoevoList;
  }
  private ArrayList<AlertMessagesVO> alertMsgArray = null;
  public ManualBOEBulkUploadVO getBulkVO()
  {
    return this.bulkVO;
  }
  public void setBulkVO(ManualBOEBulkUploadVO bulkVO)
  {
    this.bulkVO = bulkVO;
  }
  public File getInputFile()
  {
    return this.inputFile;
  }
  public void setInputFile(File inputFile)
  {
    this.inputFile = inputFile;
  }
  public FileInputStream getFis()
  {
    return this.fis;
  }
  public void setFis(FileInputStream fis)
  {
    this.fis = fis;
  }
  public BoeVO getBoeVO()
  {
    return this.boeVO;
  }
  public void setBoeVO(BoeVO boeVO)
  {
    this.boeVO = boeVO;
  }
  String[] mchkList = null;
  String contentDisposition;
  ByteArrayInputStream excelStream;
  public String[] getMchkList()
  {
    return this.mchkList;
  }
  public void setMchkList(String[] mchkList)
  {
    this.mchkList = mchkList;
  }
  public ArrayList<TransactionVO> getInvoiceList()
  {
    return this.invoiceList;
  }
  public void setInvoiceList(ArrayList<TransactionVO> invoiceList)
  {
    this.invoiceList = invoiceList;
  }
  public ArrayList<TransactionVO> getTiList()
  {
    return this.tiList;
  }
  public void setTiList(ArrayList<TransactionVO> tiList)
  {
    this.tiList = tiList;
  }
  public String getBatchId()
  {
    return this.batchId;
  }
  public void setBatchId(String batchId)
  {
    this.batchId = batchId;
  }
  public ArrayList<AlertMessagesVO> getAlertMsgArray()
  {
    return this.alertMsgArray;
  }
  public void setAlertMsgArray(ArrayList<AlertMessagesVO> alertMsgArray)
  {
    this.alertMsgArray = alertMsgArray;
  }
  public String manualBOEBlk()
    throws ApplicationException
  {
    logger.info("Entering Method");
    try
    {
      logger.info("Manual BOE Bulk Upload Action");
    }
    catch (Exception e)
    {
      logger.info("Error here " + e.getMessage());
    }
    logger.info("Exiting Method");
    return "success";
  }
  public String excelValidate()
    throws ApplicationException, BusinessException
  {
    logger.info("Entering Method");
    ManualBOEBulkUploadBD bd = null;
    String ext = "";
    try
    {
      this.bulkVO = new ManualBOEBulkUploadVO();
      bd = ManualBOEBulkUploadBD.getBD();
      this.bulkVO.setInputFile(this.inputFile);
      if (this.bulkVO.getInputFile() != null)
      {
        ext = this.fileNameRef.split("\\.")[1];
        if (ext.equalsIgnoreCase("xls"))
        {
          TransactionVO trans = bd.getExcelValidate(this.bulkVO);
          if ((trans.getErrorList() != null) && (trans.getErrorList().size() > 0)) {
            validateExcelSheet(trans);
          } else if ((trans.getErrorCodeDesc() != null) && (trans.getErrorCodeDesc().size() > 0)) {
            validateExcelSheet(trans);
          }
          this.manualBoevoList = trans.getManualBoevoList();
          this.invoiceList = trans.getBoeList();
          this.batchId = trans.getBatchId();
        }
        else
        {
          addActionError("Please input xls format file to upload");
        }
      }
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
      throwApplicationException(exception);
    }
    logger.info("Exiting Method");
    return "success";
  }
  public String rejectData()
    throws ApplicationException, BusinessException
  {
    logger.info("Entering Method");
    ManualBOEBulkUploadBD bd = null;
    try
    {
      bd = ManualBOEBulkUploadBD.getBD();
      bd.rejectData(this.batchId);
      this.batchId = "";
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
      throwApplicationException(exception);
    }
    logger.info("Exiting Method");
    return "success";
  }
  public String uploadData()
    throws ApplicationException, BusinessException
  {
    logger.info("Entering Method");
    ManualBOEBulkUploadBD bd = null;
    String status = "success";
    try
    {
      if ((this.batchId != null) && (this.batchId.trim().length() != 0))
      {
        bd = ManualBOEBulkUploadBD.getBD();
        int stat = bd.uploadData(this.batchId);
        if (stat > 0)
        {
          addActionMessage("Successfully uploaded!");
          status = "success";
        }
        else
        {
          status = "cancel";
        }
      }
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
      throwApplicationException(exception);
    }
    logger.info("Exiting Method");
    return status;
  }
  public void setErrorvalues(Object[] arg)
    throws ApplicationException
  {
    logger.info("Entering Method");
    try
    {
      CommonMethods commonMethods = new CommonMethods();
      AlertMessagesVO altMsg = new AlertMessagesVO();
      altMsg.setErrorId(commonMethods.getEmptyIfNull(arg[1]).equalsIgnoreCase("W") ? 
        "Warning" : "Error");
      altMsg.setErrorDesc("General");
      altMsg.setErrorCode(commonMethods.getEmptyIfNull(arg[3]));
      altMsg.setErrorDetails(commonMethods.getEmptyIfNull(arg[2]));
      altMsg.setErrorMsg(commonMethods.getEmptyIfNull(arg[1]).equalsIgnoreCase("W") ? 
        "N" : "Error");
      this.alertMsgArray.add(altMsg);
    }
    catch (Exception exception)
    {
      throwApplicationException(exception);
    }
    logger.info("Exiting Method");
  }
  public void validateExcelSheet(TransactionVO trans)
    throws ApplicationException
  {
    logger.info("Entering Method");
    try
    {
      this.alertMsgArray = new ArrayList();
      if ((this.alertMsgArray != null) && 
        (this.alertMsgArray.size() > 0)) {
        this.alertMsgArray.clear();
      }
      String sErrorMsg = "";
      if ((trans.getErrorList() != null) && (trans.getErrorList().size() > 0))
      {
        sErrorMsg = trans.getErrorList().toString();
        if (sErrorMsg != null)
        {
          Object[] arg = { "0", 
            "E", sErrorMsg, "Input" };
          setErrorvalues(arg);
        }
      }
      else if ((trans.getErrorCodeDesc() != null) && 
        (trans.getErrorCodeDesc().size() > 0))
      {
        sErrorMsg = trans.getErrorCodeDesc().toString();
        if (sErrorMsg != null)
        {
          Object[] arg = { "0", 
            "E", 
            "Error occured.Kindly view the excel file", 
            "Input" };
          setErrorvalues(arg);
        }
      }
      for (int a = 0; a < this.alertMsgArray.size(); a++) {
        if (((AlertMessagesVO)this.alertMsgArray.get(a)).getErrorMsg().equalsIgnoreCase("N")) {
          if ((this.bulkVO.getOverridStatus() != null) && 
            (this.bulkVO.getOverridStatus().equalsIgnoreCase("Y")))
          {
            ((AlertMessagesVO)this.alertMsgArray.get(a)).setErrorMsg("Y");
            this.bulkVO.setOverridStatus("Y");
          }
          else if ((this.bulkVO.getOverridStatus() != null) && 
            (this.bulkVO.getOverridStatus().equalsIgnoreCase("N")))
          {
            ((AlertMessagesVO)this.alertMsgArray.get(a)).setErrorMsg("N");
            this.bulkVO.setOverridStatus("N");
          }
        }
      }
    }
    catch (Exception e)
    {
      throwApplicationException(e);
    }
  }
  
  public String excelDownload()
  {
    logger.info("Entering Method");
    ArrayList<TransactionVO> test = new ArrayList();
    try
    {
      if (this.invoiceList != null)
      {
        for (TransactionVO vo : this.invoiceList)
        {
          TransactionVO bulkVO = new TransactionVO();
          bulkVO.setBoeNo(vo.getBoeNo());
          bulkVO.setBoeDate(vo.getBoeDate());
          bulkVO.setPortCode(vo.getPortCode());
          bulkVO.setIeCode(vo.getIeCode());
          bulkVO.setAdcode(vo.getAdcode());
          bulkVO.setIgmNumber(vo.getIgmNumber());
          bulkVO.setIgmDate(vo.getIgmDate());
          bulkVO.setHblNumber(vo.getHblNumber());
          bulkVO.setHblDate(vo.getHblDate());
          bulkVO.setMblNumber(vo.getMblNumber());
          bulkVO.setMblDate(vo.getMblDate());
          bulkVO.setImpagc(vo.getImpagc());
          bulkVO.setRecordInd(vo.getRecordInd());
          bulkVO.setGovprv(vo.getGovprv());
          bulkVO.setPorshp(vo.getPorshp());
          bulkVO.setInvSno(vo.getInvSno());
          bulkVO.setInvNo(vo.getInvNo());
          bulkVO.setTermsofInvoice(vo.getTermsofInvoice());
          bulkVO.setInvAmt(vo.getInvAmt());
          bulkVO.setInvCurr(vo.getInvCurr());
          bulkVO.setFreightAmount(vo.getFreightAmount());
          bulkVO.setFreightCurrencyCode(vo.getFreightCurrencyCode());
          bulkVO.setInsuranceAmount(vo.getInsuranceAmount());
          bulkVO.setInsuranceCurrencyCode(vo.getInsuranceCurrencyCode());
          bulkVO.setAgencyCommission(vo.getAgencyCommission());
          bulkVO.setAgencyCurrency(vo.getAgencyCurrency());
          bulkVO.setDiscountCharges(vo.getDiscountCharges());
          bulkVO.setDiscountCurrency(vo.getDiscountCurrency());
          bulkVO.setMiscellaneousCharges(vo.getMiscellaneousCharges());
          bulkVO.setMiscellaneousCurrency(vo.getMiscellaneousCurrency());
          bulkVO.setSupplierName(vo.getSupplierName());
          bulkVO.setSupplierAddress(vo.getSupplierAddress());
          bulkVO.setSupplierCountry(vo.getSupplierCountry());
          bulkVO.setSellerName(vo.getSellerName());
          bulkVO.setSellerAddress(vo.getSellerAddress());
          bulkVO.setSellerCountry(vo.getSellerCountry());
          bulkVO.setThirdPartyName(vo.getThirdPartyName());
          bulkVO.setThirdPartyAddress(vo.getThirdPartyAddress());
          bulkVO.setThirdPartyCountry(vo.getThirdPartyCountry());
          bulkVO.setErrDesc(vo.getErrDesc());
          test.add(bulkVO);
        }
        this.excelStream = prepareExcelStream(test);
      }
      else
      {
        this.excelStream = prepareExcelStream(test);
      }
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return "download1";
  }
  public String manualErrorlistExcelDownload()
  {
    logger.info("Entering Method");
    ArrayList<ManualBOEBulkUploadVO> test = new ArrayList();
    this.bulkVO = new ManualBOEBulkUploadVO();
    try
    {
      if (this.mchkList != null)
      {
        for (String e : this.mchkList)
        {
          this.bulkVO = new ManualBOEBulkUploadVO();
          logger.info("String e" + e);
          if (e != null)
          {
            String[] a = e.split(":");
            if (a[39].length() != 4)
            {
              this.bulkVO.setBoeNo(a[0]);
              this.bulkVO.setBoeDate(a[1]);
              this.bulkVO.setPortCode(a[2]);
              this.bulkVO.setIeCode(a[3]);
              this.bulkVO.setAdCode(a[4]);
              this.bulkVO.setIgmNo(a[5]);
              this.bulkVO.setIgmDate(a[6]);
              this.bulkVO.setHblNo(a[7]);
              this.bulkVO.setHblDate(a[8]);
              this.bulkVO.setMblNo(a[9]);
              this.bulkVO.setMblDate(a[10]);
              this.bulkVO.setImAgency(a[11]);
              this.bulkVO.setRecInd(a[12]);
              this.bulkVO.setGovprv(a[13]);
              this.bulkVO.setPos(a[14]);
              this.bulkVO.setInvoiceSerialNo(a[15]);
              this.bulkVO.setInvoiceNo(a[16]);
              this.bulkVO.setTermsofInvoice(a[17]);
              this.bulkVO.setInvoiceAmt(a[18]);
              this.bulkVO.setInvoiceCurrency(a[19]);
              this.bulkVO.setFreightAmount(a[20]);
              this.bulkVO.setFreightCurrencyCode(a[21]);
              this.bulkVO.setInsuranceAmount(a[22]);
              this.bulkVO.setInsuranceCurrencyCode(a[23]);
              this.bulkVO.setAgencyCommission(a[24]);
              this.bulkVO.setAgencyCurrency(a[25]);
              this.bulkVO.setDiscountCharges(a[26]);
              this.bulkVO.setDiscountCurrency(a[27]);
              this.bulkVO.setMiscellaneousCharges(a[28]);
              this.bulkVO.setMiscellaneousCurrency(a[29]);
              this.bulkVO.setSupplierName(a[30]);
              this.bulkVO.setSupplierAddress(a[31]);
              this.bulkVO.setSupplierCountry(a[32]);
              this.bulkVO.setSellerName(a[33]);
              this.bulkVO.setSellerAddress(a[34]);
              this.bulkVO.setSellerCountry(a[35]);
              this.bulkVO.setThirdPartyName(a[36]);
              this.bulkVO.setThirdPartyAddress(a[37]);
              this.bulkVO.setThirdPartyCountry(a[38]);
              this.bulkVO.setErrorDesc(a[39]);
              test.add(this.bulkVO);
            }
          }
        }
        this.excelStream = prepareExcelStream1(test);
      }
      else
      {
        this.excelStream = prepareExcelStream1(test);
      }
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return "download1";
  }
  public String getContentDisposition()
  {
    return this.contentDisposition;
  }
  public void setContentDisposition(String contentDisposition)
  {
    this.contentDisposition = contentDisposition;
  }
  public ByteArrayInputStream getExcelStream()
  {
    return this.excelStream;
  }
  public void setExcelStream(ByteArrayInputStream excelStream)
  {
    this.excelStream = excelStream;
  }
  
  public ByteArrayInputStream prepareExcelStream(ArrayList<TransactionVO> aaData)
  {
    setContentDisposition("attachment; filename=\"Manual_BOE_Bulkupload_Error_List.xls\"");
    try
    {
      logger.info("In prepareExcelStream");
      HSSFWorkbook workbook = null;
      workbook = new HSSFWorkbook();
      HSSFSheet sheet = workbook.createSheet("Manual_BOE_Bulkupload_Error_List");
      int rowIndex = 2;
      int cellIndex;
      if ((aaData != null) && (aaData.size() > 0))
      {
        HSSFRow headingRow = sheet.createRow(0);
        cellIndex = 0;
        headingRow.createCell(cellIndex++).setCellValue("BillOfEntryNumber");
        headingRow.createCell(cellIndex++).setCellValue("BillOfEntryDate");
        headingRow.createCell(cellIndex++).setCellValue("PORTCODE");
        headingRow.createCell(cellIndex++).setCellValue("IECODE");
        headingRow.createCell(cellIndex++).setCellValue("ADCODE");
        headingRow.createCell(cellIndex++).setCellValue("IGMNo");
        headingRow.createCell(cellIndex++).setCellValue("IGMDate");
        headingRow.createCell(cellIndex++).setCellValue("HAWB-HBLNo");
        headingRow.createCell(cellIndex++).setCellValue("HAWB-HBLDate");
        headingRow.createCell(cellIndex++).setCellValue("MAWB-MBLNo");
        headingRow.createCell(cellIndex++).setCellValue("MAWB-MBLDate");
        headingRow.createCell(cellIndex++).setCellValue("ImportAgency");
        headingRow.createCell(cellIndex++).setCellValue("RecordIndicator");
        headingRow.createCell(cellIndex++).setCellValue("G-P");
        headingRow.createCell(cellIndex++).setCellValue("PortOfShipment");
        headingRow.createCell(cellIndex++).setCellValue("InvoiceSerialNo");
        headingRow.createCell(cellIndex++).setCellValue("InvoiceNo");
        headingRow.createCell(cellIndex++).setCellValue("TermsOfServices");
        headingRow.createCell(cellIndex++).setCellValue("InvoiceAmount");
        headingRow.createCell(cellIndex++).setCellValue("InvoiceCurrency");
        headingRow.createCell(cellIndex++).setCellValue("FreightAmount");
        headingRow.createCell(cellIndex++).setCellValue("FreightCurrencyCode");
        headingRow.createCell(cellIndex++).setCellValue("InsuranceAmount");
        headingRow.createCell(cellIndex++).setCellValue("InsuranceCurrencyCode");
        headingRow.createCell(cellIndex++).setCellValue("AgencyCommission");
        headingRow.createCell(cellIndex++).setCellValue("AgencyCurrency");
        headingRow.createCell(cellIndex++).setCellValue("DiscountCharges");
        headingRow.createCell(cellIndex++).setCellValue("DiscountCurrency");
        headingRow.createCell(cellIndex++).setCellValue("MiscellaneousCharges");
        headingRow.createCell(cellIndex++).setCellValue("MiscellaneousCurrency");
        headingRow.createCell(cellIndex++).setCellValue("SupplierName");
        headingRow.createCell(cellIndex++).setCellValue("SupplierAddress");
        headingRow.createCell(cellIndex++).setCellValue("SupplierCountry");
        headingRow.createCell(cellIndex++).setCellValue("SellerName");
        headingRow.createCell(cellIndex++).setCellValue("SellerAddress");
        headingRow.createCell(cellIndex++).setCellValue("SellerCountry");
        headingRow.createCell(cellIndex++).setCellValue("ThirdPartyName");
        headingRow.createCell(cellIndex++).setCellValue("ThirdPartyAddress");
        headingRow.createCell(cellIndex++).setCellValue("ThirdPartyCountry");
        headingRow.createCell(cellIndex++).setCellValue("ERROR");
      }
      else
      {
        HSSFRow headingRow = sheet.createRow(0);
        headingRow.createCell(0).setCellValue("NO ERRORS");
      }
      for (TransactionVO obj : aaData)
      {
        HSSFRow row = sheet.createRow(rowIndex);
        int cellIndex = 0;
        row.createCell(cellIndex++).setCellValue(obj.getBoeNo());
        row.createCell(cellIndex++).setCellValue(obj.getBoeDate());
        row.createCell(cellIndex++).setCellValue(obj.getPortCode());
        row.createCell(cellIndex++).setCellValue(obj.getIeCode());
        row.createCell(cellIndex++).setCellValue(obj.getAdcode());
        row.createCell(cellIndex++).setCellValue(obj.getIgmNumber());
        row.createCell(cellIndex++).setCellValue(obj.getIgmDate());
        row.createCell(cellIndex++).setCellValue(obj.getHblNumber());
        row.createCell(cellIndex++).setCellValue(obj.getHblDate());
        row.createCell(cellIndex++).setCellValue(obj.getMblNumber());
        row.createCell(cellIndex++).setCellValue(obj.getMblDate());
        row.createCell(cellIndex++).setCellValue(obj.getImpagc());
        row.createCell(cellIndex++).setCellValue(obj.getRecordInd());
        row.createCell(cellIndex++).setCellValue(obj.getGovprv());
        row.createCell(cellIndex++).setCellValue(obj.getPorshp());
        row.createCell(cellIndex++).setCellValue(obj.getInvSno());
        row.createCell(cellIndex++).setCellValue(obj.getInvNo());
        row.createCell(cellIndex++).setCellValue(obj.getTermsofInvoice());
        row.createCell(cellIndex++).setCellValue(obj.getInvoiceAmount());
        row.createCell(cellIndex++).setCellValue(obj.getInvoiceCurr());
        row.createCell(cellIndex++).setCellValue(obj.getFreightAmount());
        row.createCell(cellIndex++).setCellValue(obj.getFreightCurrencyCode());
        row.createCell(cellIndex++).setCellValue(obj.getInsuranceAmount());
        row.createCell(cellIndex++).setCellValue(obj.getInsuranceCurrencyCode());
        row.createCell(cellIndex++).setCellValue(obj.getAgencyCommission());
        row.createCell(cellIndex++).setCellValue(obj.getAgencyCurrency());
        row.createCell(cellIndex++).setCellValue(obj.getDiscountCharges());
        row.createCell(cellIndex++).setCellValue(obj.getDiscountCurrency());
        row.createCell(cellIndex++).setCellValue(obj.getMiscellaneousCharges());
        row.createCell(cellIndex++).setCellValue(obj.getMiscellaneousCurrency());
        row.createCell(cellIndex++).setCellValue(obj.getSupplierName());
        row.createCell(cellIndex++).setCellValue(obj.getSupplierAddress());
        row.createCell(cellIndex++).setCellValue(obj.getSupplierCountry());
        row.createCell(cellIndex++).setCellValue(obj.getSellerName());
        row.createCell(cellIndex++).setCellValue(obj.getSellerAddress());
        row.createCell(cellIndex++).setCellValue(obj.getSellerCountry());
        row.createCell(cellIndex++).setCellValue(obj.getThirdPartyName());
        row.createCell(cellIndex++).setCellValue(obj.getThirdPartyAddress());
        row.createCell(cellIndex++).setCellValue(obj.getThirdPartyCountry());
        row.createCell(cellIndex++).setCellValue(obj.getErrDesc());
        rowIndex++;
      }
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      workbook.write(baos);
      this.excelStream = new ByteArrayInputStream(baos.toByteArray());
      logger.info("prepareExcelStream COMPLETED");
    }
    
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    return this.excelStream;
  }
  public ByteArrayInputStream prepareExcelStream1(ArrayList<ManualBOEBulkUploadVO> aaData)
  {
    setContentDisposition("attachment; filename=\"Manual_BOE_Bulkupload_Error_List.xls\"");
    try
    {
      logger.info("In prepareExcelStream");
      HSSFWorkbook workbook = null;
      workbook = new HSSFWorkbook();
      HSSFSheet sheet = workbook.createSheet("Manual_BOE_Bulkupload_Error_List");
      int rowIndex = 2;
      int cellIndex;
      if ((aaData != null) && (aaData.size() > 0))
      {
        HSSFRow headingRow = sheet.createRow(0);
        cellIndex = 0;
        headingRow.createCell(cellIndex++).setCellValue("BillOfEntryNumber");
        headingRow.createCell(cellIndex++).setCellValue("BillOfEntryDate");
        headingRow.createCell(cellIndex++).setCellValue("PORTCODE");
        headingRow.createCell(cellIndex++).setCellValue("IECODE");
        headingRow.createCell(cellIndex++).setCellValue("ADCODE");
        headingRow.createCell(cellIndex++).setCellValue("IGMNo");
        headingRow.createCell(cellIndex++).setCellValue("IGMDate");
        headingRow.createCell(cellIndex++).setCellValue("HAWB-HBLNo");
        headingRow.createCell(cellIndex++).setCellValue("HAWB-HBLDate");
        headingRow.createCell(cellIndex++).setCellValue("MAWB-MBLNo");
        headingRow.createCell(cellIndex++).setCellValue("MAWB-MBLDate");
        headingRow.createCell(cellIndex++).setCellValue("ImportAgency");
        headingRow.createCell(cellIndex++).setCellValue("RecordIndicator");
        headingRow.createCell(cellIndex++).setCellValue("G-P");
        headingRow.createCell(cellIndex++).setCellValue("PortOfShipment");
        headingRow.createCell(cellIndex++).setCellValue("InvoiceSerialNo");
        headingRow.createCell(cellIndex++).setCellValue("InvoiceNo");
        headingRow.createCell(cellIndex++).setCellValue("TermsOfServices");
        headingRow.createCell(cellIndex++).setCellValue("InvoiceAmount");
        headingRow.createCell(cellIndex++).setCellValue("InvoiceCurrency");
        headingRow.createCell(cellIndex++).setCellValue("FreightAmount");
        headingRow.createCell(cellIndex++).setCellValue("FreightCurrencyCode");
        headingRow.createCell(cellIndex++).setCellValue("InsuranceAmount");
        headingRow.createCell(cellIndex++).setCellValue("InsuranceCurrencyCode");
        headingRow.createCell(cellIndex++).setCellValue("AgencyCommission");
        headingRow.createCell(cellIndex++).setCellValue("AgencyCurrency");
        headingRow.createCell(cellIndex++).setCellValue("DiscountCharges");
        headingRow.createCell(cellIndex++).setCellValue("DiscountCurrency");
        headingRow.createCell(cellIndex++).setCellValue("MiscellaneousCharges");
        headingRow.createCell(cellIndex++).setCellValue("MiscellaneousCurrency");
        headingRow.createCell(cellIndex++).setCellValue("SupplierName");
        headingRow.createCell(cellIndex++).setCellValue("SupplierAddress");
        headingRow.createCell(cellIndex++).setCellValue("SupplierCountry");
        headingRow.createCell(cellIndex++).setCellValue("SellerName");
        headingRow.createCell(cellIndex++).setCellValue("SellerAddress");
        headingRow.createCell(cellIndex++).setCellValue("SellerCountry");
        headingRow.createCell(cellIndex++).setCellValue("ThirdPartyName");
        headingRow.createCell(cellIndex++).setCellValue("ThirdPartyAddress");
        headingRow.createCell(cellIndex++).setCellValue("ThirdPartyCountry");
        headingRow.createCell(cellIndex++).setCellValue("ERROR");
      }
      else
      {
        HSSFRow headingRow = sheet.createRow(0);
        headingRow.createCell(0).setCellValue("NO ERRORS");
      }
      for (ManualBOEBulkUploadVO obj : aaData)
      {
        HSSFRow row = sheet.createRow(rowIndex);
        int cellIndex = 0;
        row.createCell(cellIndex++).setCellValue(obj.getBoeNo());
        row.createCell(cellIndex++).setCellValue(obj.getBoeDate());
        row.createCell(cellIndex++).setCellValue(obj.getPortCode());
        row.createCell(cellIndex++).setCellValue(obj.getIeCode());
        row.createCell(cellIndex++).setCellValue(obj.getAdCode());
        row.createCell(cellIndex++).setCellValue(obj.getIgmNo());
        row.createCell(cellIndex++).setCellValue(obj.getIgmDate());
        row.createCell(cellIndex++).setCellValue(obj.getHblNo());
        row.createCell(cellIndex++).setCellValue(obj.getHblDate());
        row.createCell(cellIndex++).setCellValue(obj.getMblNo());
        row.createCell(cellIndex++).setCellValue(obj.getMblDate());
        row.createCell(cellIndex++).setCellValue(obj.getImAgency());
        row.createCell(cellIndex++).setCellValue(obj.getRecInd());
        row.createCell(cellIndex++).setCellValue(obj.getGovprv());
        row.createCell(cellIndex++).setCellValue(obj.getPos());
        row.createCell(cellIndex++).setCellValue(obj.getInvoiceSerialNo());
        row.createCell(cellIndex++).setCellValue(obj.getInvoiceNo());
        row.createCell(cellIndex++).setCellValue(obj.getTermsofInvoice());
        row.createCell(cellIndex++).setCellValue(obj.getInvoiceAmt());
        row.createCell(cellIndex++).setCellValue(obj.getInvoiceCurrency());
        row.createCell(cellIndex++).setCellValue(obj.getFreightAmount());
        row.createCell(cellIndex++).setCellValue(obj.getFreightCurrencyCode());
        row.createCell(cellIndex++).setCellValue(obj.getInsuranceAmount());
        row.createCell(cellIndex++).setCellValue(obj.getInsuranceCurrencyCode());
        row.createCell(cellIndex++).setCellValue(obj.getAgencyCommission());
        row.createCell(cellIndex++).setCellValue(obj.getAgencyCurrency());
        row.createCell(cellIndex++).setCellValue(obj.getDiscountCharges());
        row.createCell(cellIndex++).setCellValue(obj.getDiscountCurrency());
        row.createCell(cellIndex++).setCellValue(obj.getMiscellaneousCharges());
        row.createCell(cellIndex++).setCellValue(obj.getMiscellaneousCurrency());
        row.createCell(cellIndex++).setCellValue(obj.getSupplierName());
        row.createCell(cellIndex++).setCellValue(obj.getSupplierAddress());
        row.createCell(cellIndex++).setCellValue(obj.getSupplierCountry());
        row.createCell(cellIndex++).setCellValue(obj.getSellerName());
        row.createCell(cellIndex++).setCellValue(obj.getSellerAddress());
        row.createCell(cellIndex++).setCellValue(obj.getSellerCountry());
        row.createCell(cellIndex++).setCellValue(obj.getThirdPartyName());
        row.createCell(cellIndex++).setCellValue(obj.getThirdPartyAddress());
        row.createCell(cellIndex++).setCellValue(obj.getThirdPartyCountry());
        row.createCell(cellIndex++).setCellValue(obj.getErrorDesc());
        rowIndex++;
      }
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      workbook.write(baos);
      this.excelStream = new ByteArrayInputStream(baos.toByteArray());
      logger.info("prepareExcelStream COMPLETED");
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    return this.excelStream;
  }
}