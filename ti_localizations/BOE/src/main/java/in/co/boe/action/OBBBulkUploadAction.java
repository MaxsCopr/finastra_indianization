package in.co.boe.action;

import in.co.boe.businessdelegate.bulkUpload.OBBBulkUploadBD;
import in.co.boe.businessdelegate.exception.BusinessException;
import in.co.boe.dao.bulkUpload.OBBDWLD;
import in.co.boe.dao.exception.ApplicationException;
import in.co.boe.utility.CommonMethods;
import in.co.boe.vo.AlertMessagesVO;
import in.co.boe.vo.BoeVO;
import in.co.boe.vo.OBBBulkUploadVO;
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
 
public class OBBBulkUploadAction
  extends BoeBaseAction
{
  private static final long serialVersionUID = 1L;
  private static Logger logger = Logger.getLogger(OBBBulkUploadAction.class.getName());
  BoeVO boeVO = null;
  ArrayList<TransactionVO> invoiceList = null;
  ArrayList<TransactionVO> boeList = null;
  ArrayList<TransactionVO> tiList = null;
  File inputFile;
  FileInputStream fis = null;
  OBBBulkUploadVO bulkVO = null;
  private String batchId;
  private ArrayList<AlertMessagesVO> alertMsgArray = null;
  private ArrayList<TransactionVO> errorMsg = null;
  String fileNameRef;
  String[] chkList = null;
  String csvFileGenFlag;
  private ArrayList<OBBBulkUploadVO> obbvoList = null;
  String contentDisposition;
  ByteArrayInputStream excelStream;
  public String getCsvFileGenFlag()
  {
    return this.csvFileGenFlag;
  }
  public void setCsvFileGenFlag(String csvFileGenFlag)
  {
    this.csvFileGenFlag = csvFileGenFlag;
  }
  public String getFileNameRef()
  {
    return this.fileNameRef;
  }
  public void setFileNameRef(String fileNameRef)
  {
    this.fileNameRef = fileNameRef;
  }
  public String[] getChkList()
  {
    return this.chkList;
  }
  public void setChkList(String[] chkList)
  {
    this.chkList = chkList;
  }
  public ArrayList<OBBBulkUploadVO> getObbvoList()
  {
    return this.obbvoList;
  }
  public void setObbvoList(ArrayList<OBBBulkUploadVO> obbvoList)
  {
    this.obbvoList = obbvoList;
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
  public ArrayList<TransactionVO> getErrorMsg()
  {
    return this.errorMsg;
  }
  public void setErrorMsg(ArrayList<TransactionVO> errorMsg)
  {
    this.errorMsg = errorMsg;
  }
  public ArrayList<TransactionVO> getBoeList()
  {
    return this.boeList;
  }
  public void setBoeList(ArrayList<TransactionVO> boeList)
  {
    this.boeList = boeList;
  }
  public OBBBulkUploadVO getBulkVO()
  {
    return this.bulkVO;
  }
  public void setBulkVO(OBBBulkUploadVO bulkVO)
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
  public String OBBBulkUpload()
    throws ApplicationException
  {
    logger.info("Entering Method");
    try
    {
      logger.info("OBB Bulk Upload Action");
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
    OBBBulkUploadBD bd = null;
    try
    {
      this.bulkVO = new OBBBulkUploadVO();
      bd = OBBBulkUploadBD.getBD();
      this.bulkVO.setInputFile(this.inputFile);
      this.bulkVO.setFileNameRef(this.fileNameRef);
      logger.info("Before excelValidate");
      TransactionVO trans = bd.getExcelValidate(this.bulkVO);
      logger.info("After excelValidate");
      if ((trans.getErrorCodeDesc() != null) && (trans.getErrorCodeDesc().size() > 0)) {
        validateExcelSheet(trans);
      }
      this.obbvoList = trans.getObbvoList();
      this.boeList = trans.getBoeList();
      this.batchId = trans.getBatchId();
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
    OBBBulkUploadBD bd = null;
    try
    {
      bd = OBBBulkUploadBD.getBD();
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
    OBBBulkUploadBD bd = null;
    String status = "success";
    try
    {
      if ((this.batchId != null) && (this.batchId.trim().length() != 0))
      {
        bd = OBBBulkUploadBD.getBD();
        int stat = bd.uploadData(this.batchId);
        if (stat > 0)
        {
          addActionMessage(stat + " Record(s) are Successfully uploaded!");
          status = "success";
          this.csvFileGenFlag = "generated";
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
      altMsg.setErrorId(commonMethods.getEmptyIfNull(arg[1])
        .equalsIgnoreCase("W") ? "Warning" : 
        "Error");
      altMsg.setErrorDesc("General");
      altMsg.setErrorCode(commonMethods.getEmptyIfNull(arg[3]));
      altMsg.setErrorDetails(commonMethods.getEmptyIfNull(arg[2]));
      altMsg.setErrorMsg(commonMethods.getEmptyIfNull(arg[1])
        .equalsIgnoreCase("W") ? "N" : 
        "Error");
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
    try
    {
      this.alertMsgArray = new ArrayList();
      if ((this.alertMsgArray != null) && 
        (this.alertMsgArray.size() > 0)) {
        this.alertMsgArray.clear();
      }
      String sErrorMsg = "";
      if (trans.getErrorCodeDesc().size() > 0)
      {
        sErrorMsg = trans.getErrorCodeDesc().toString();
        if (sErrorMsg != null)
        {
          String sStrVal = sErrorMsg.substring(1, sErrorMsg.length() - 2);
          String[] sStrArray = sStrVal.split("\\|,");
          for (int i = 0; i < sStrArray.length; i++)
          {
            Object[] arg = { "0", "E", sStrArray[i].toString(), 
              "Input" };
            setErrorvalues(arg);
          }
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
  
  public String OBBExcelDownload()
  {
    logger.info("Entering Method");
    ArrayList<TransactionVO> test = new ArrayList();
    try
    {
      if (this.boeList != null)
      {
        for (TransactionVO vo : this.boeList)
        {
          TransactionVO bulkVO = new TransactionVO();
          bulkVO.setBoeNo(vo.getBoeNo());
          bulkVO.setBoeDate(vo.getBoeDate());
          bulkVO.setPortCode(vo.getPortCode());
          bulkVO.setImpagc(vo.getImpagc());
          bulkVO.setAdcode(vo.getAdcode());
          bulkVO.setIeCode(vo.getIeCode());
          bulkVO.setIename(vo.getIename());
          bulkVO.setErrDesc(vo.getErrDesc());
          bulkVO.setStatus(vo.getStatus());
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
    return "download";
  }
  public ByteArrayInputStream prepareExcelStream(ArrayList<TransactionVO> aaData)
  {
    setContentDisposition("attachment; filename=\"OBB_Bulkupload_Error_List.xls\"");
    try
    {
      logger.info("In prepareExcelStream");
      HSSFWorkbook workbook = new HSSFWorkbook();
      HSSFSheet sheet = workbook.createSheet("OBB_Bulkupload_Error_List");
      int rowIndex = 2;
      int cellIndex;
      if ((aaData != null) && (aaData.size() > 0))
      {
        HSSFRow headingRow = sheet.createRow(0);
        cellIndex = 0;
        headingRow.createCell(cellIndex++).setCellValue("BillOfEntryNumber");
        headingRow.createCell(cellIndex++).setCellValue("BillOfEntryDate");
        headingRow.createCell(cellIndex++).setCellValue("PortOfDischarge");
        headingRow.createCell(cellIndex++).setCellValue("ImportAgency");
        headingRow.createCell(cellIndex++).setCellValue("ADCode");
        headingRow.createCell(cellIndex++).setCellValue("IECode");
        headingRow.createCell(cellIndex++).setCellValue("IEName");
        headingRow.createCell(cellIndex++).setCellValue("ERROR");
        headingRow.createCell(cellIndex++).setCellValue("Status");
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
        row.createCell(cellIndex++).setCellValue(obj.getImpagc());
        row.createCell(cellIndex++).setCellValue(obj.getAdcode());
        row.createCell(cellIndex++).setCellValue(obj.getIeCode());
        row.createCell(cellIndex++).setCellValue(obj.getIename());
        row.createCell(cellIndex++).setCellValue(obj.getErrDesc());
        row.createCell(cellIndex++).setCellValue(obj.getStatus());
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
  public String OBBCSVDownload()
  {
    try
    {
      new OBBDWLD().processOBBjob();
      addActionMessage("File Generated Successfully");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return "success";
  }
}