package in.co.boe.action;

import in.co.boe.businessdelegate.bulkUpload.BOEBulkUploadBD;
import in.co.boe.businessdelegate.exception.BusinessException;
import in.co.boe.dao.exception.ApplicationException;
import in.co.boe.utility.CommonMethods;
import in.co.boe.vo.AlertMessagesVO;
import in.co.boe.vo.BOEBulkUploadVO;
import in.co.boe.vo.TransactionBoeBlkVO;
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
 
public class BOEBulkUploadAction
  extends BoeBaseAction
{
  private static final long serialVersionUID = 1L;
  private static Logger logger = Logger.getLogger(BOEBulkUploadAction.class
    .getName());
  ArrayList<TransactionBoeBlkVO> tiList = null;
  File inputFile;
  String fileNameRef;
  FileInputStream fis = null;
  BOEBulkUploadVO bulkVO = null;
  TransactionBoeBlkVO blkTrans = null;
  String[] chkList = null;
  private String batchId;
  public String getFileNameRef()
  {
    return this.fileNameRef;
  }
  public void setFileNameRef(String fileNameRef)
  {
    this.fileNameRef = fileNameRef;
  }
  private ArrayList<AlertMessagesVO> alertMsgArray = null;
  private ArrayList<TransactionBoeBlkVO> boeBulkList = null;
  private ArrayList<BOEBulkUploadVO> boevoList = null;
  String contentDisposition;
  ByteArrayInputStream excelStream;
  public String[] getChkList()
  {
    return this.chkList;
  }
  public void setChkList(String[] chkList)
  {
    this.chkList = chkList;
  }
  public ArrayList<BOEBulkUploadVO> getBoevoList()
  {
    return this.boevoList;
  }
  public void setBoevoList(ArrayList<BOEBulkUploadVO> boevoList)
  {
    this.boevoList = boevoList;
  }
  public TransactionBoeBlkVO getBlkTrans()
  {
    return this.blkTrans;
  }
  public void setBlkTrans(TransactionBoeBlkVO blkTrans)
  {
    this.blkTrans = blkTrans;
  }
  public BOEBulkUploadVO getBulkVO()
  {
    return this.bulkVO;
  }
  public void setBulkVO(BOEBulkUploadVO bulkVO)
  {
    this.bulkVO = bulkVO;
  }
  public ArrayList<TransactionBoeBlkVO> getBoeBulkList()
  {
    return this.boeBulkList;
  }
  public void setBoeBulkList(ArrayList<TransactionBoeBlkVO> boeBulkList)
  {
    this.boeBulkList = boeBulkList;
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
  public ArrayList<TransactionBoeBlkVO> getTiList()
  {
    return this.tiList;
  }
  public void setTiList(ArrayList<TransactionBoeBlkVO> tiList)
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
  public String BOEBlk()
    throws ApplicationException
  {
    logger.info("Entering Method");
    try
    {
      isSessionAvailable();
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
    BOEBulkUploadBD bd = null;
    String ext = "";
    try
    {
      this.bulkVO = new BOEBulkUploadVO();
      bd = BOEBulkUploadBD.getBD();
      this.bulkVO.setInputFile(this.inputFile);
      if (this.bulkVO.getInputFile() != null)
      {
        ext = this.fileNameRef.split("\\.")[1];
        if (ext.equalsIgnoreCase("xls"))
        {
          TransactionBoeBlkVO trans = bd.getExcelValidate(this.bulkVO);
          if ((trans.getErrorList() != null) && (trans.getErrorList().size() > 0)) {
            validateExcelSheet(trans);
          } else if ((trans.getErrorCodeDesc() != null) && (trans.getErrorCodeDesc().size() > 0)) {
            validateExcelSheet(trans);
          }
          this.boevoList = trans.getBoevoList();
          this.boeBulkList = trans.getBoeList();
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
    BOEBulkUploadBD bd = null;
    try
    {
      bd = BOEBulkUploadBD.getBD();
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
    BOEBulkUploadBD bd = null;
    TransactionBoeBlkVO trans = null;
    String status = "success";
    try
    {
      trans = new TransactionBoeBlkVO();
      if ((this.batchId != null) && (this.batchId.trim().length() != 0))
      {
        bd = BOEBulkUploadBD.getBD();
        trans = bd.uploadData(this.batchId);
        int stat = trans.getSuccessCount();
        String sProcCount = trans.getCount();
        if ((stat > 0) && (!sProcCount.equals("N")))
        {
          addActionMessage(stat + 
            " Record(s) are Successfully uploaded!");
          status = "success";
        }
        else if (sProcCount.equals("N"))
        {
          procErrorMsg(trans);
          status = "fail";
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
  public void validateExcelSheet(TransactionBoeBlkVO trans)
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
  
  public void procErrorMsg(TransactionBoeBlkVO trans)
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
		      if ((trans.getCount().equals("N")) && 
		        (trans.getBoeNo() != null) && (trans.getBoeNo().length() == 7))
		      {
		        String errorcode = "The BOE(" + trans.getBoeNo() + ") " + 
		          "Amount In Payment Currency Should Not Exceed Outstanding Payment Amount";
		        Object[] arg = { "0", 
		          "E", errorcode, "Input" };
		        setErrorvalues(arg);
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
		    ArrayList<TransactionBoeBlkVO> test = new ArrayList();
		    try
		    {
		      if (this.boeBulkList != null)
		      {
		        for (TransactionBoeBlkVO blkvo : this.boeBulkList)
		        {
		          TransactionBoeBlkVO transBlkvo = new TransactionBoeBlkVO();
		          transBlkvo.setPaymentRefNo(blkvo.getPaymentRefNo());
		          transBlkvo.setEventRefNo(blkvo.getEventRefNo());
		          transBlkvo.setPaymentAmnt(blkvo.getPaymentAmnt());
		          transBlkvo.setPayAmntCurr(blkvo.getPayAmntCurr());
		          transBlkvo.setBoeNo(blkvo.getBoeNo());
		          transBlkvo.setBoeDate(blkvo.getBoeDate());
		          transBlkvo.setPortCode(blkvo.getPortCode());
		          transBlkvo.setBoeAmnt(blkvo.getBoeAmnt());
		          transBlkvo.setBoeAmntCurr(blkvo.getBoeAmntCurr());
		          transBlkvo.setBoeAmntEndorse(blkvo.getBoeAmntEndorse());
		          transBlkvo.setBoeAllocAmnt(blkvo.getBoeAllocAmnt());
		          transBlkvo.setChangeIeCode(blkvo.getChangeIeCode());
		          transBlkvo.setBesRecInd(blkvo.getBesRecInd());
		          transBlkvo.setInvoiceSerNo(blkvo.getInvoiceSerNo());
		          transBlkvo.setInvoiceNo(blkvo.getInvoiceNo());
		          transBlkvo.setInvRealAmt(blkvo.getInvRealAmt());
		          transBlkvo.setRemarks(blkvo.getRemarks());
		          transBlkvo.setErrDesc(blkvo.getErrDesc());
		          transBlkvo.setStatus(blkvo.getStatus());
		          test.add(transBlkvo);
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
		  public String errorlistExcelDownload()
		  {
		    logger.info("Entering Method");
		    ArrayList<BOEBulkUploadVO> test = new ArrayList();
		    this.bulkVO = new BOEBulkUploadVO();
		    try
		    {
		      if (this.chkList != null)
		      {
		        for (String e : this.chkList)
		        {
		          this.bulkVO = new BOEBulkUploadVO();
		          logger.info("String " + e);
		          if (e != null)
		          {
		            String[] a = e.split(":");
		            if (a[17].length() != 4)
		            {
		              this.bulkVO.setPaymentRefNo(a[0]);
		              this.bulkVO.setEventRefNo(a[1]);
		              this.bulkVO.setPaymentAmnt(a[2]);
		              this.bulkVO.setPayAmntCurr(a[3]);
		              this.bulkVO.setBoeNo(a[4]);
		              this.bulkVO.setBoeDate(a[5]);
		              this.bulkVO.setPortCode(a[6]);
		              this.bulkVO.setBoeAmnt(a[7]);
		              this.bulkVO.setBoeAmntCurr(a[8]);
		              this.bulkVO.setBoeAmntEndorse(a[9]);
		              this.bulkVO.setBoeAllocAmnt(a[10]);
		              this.bulkVO.setChangeIeCode(a[11]);
		              this.bulkVO.setBesRecInd(a[12]);
		              this.bulkVO.setInvoiceSerNo(a[13]);
		              this.bulkVO.setInvoiceNo(a[14]);
		              this.bulkVO.setInvRelAmt(a[15]);
		              this.bulkVO.setRemarks(a[16]);
		              this.bulkVO.setErrorDesc(a[17]);
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
		    return "download";
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
		  public ByteArrayInputStream prepareExcelStream(ArrayList<TransactionBoeBlkVO> aaData)
		  {
		    setContentDisposition("attachment; filename=\"BOE_Bulkupload_Error_List.xls\"");
		    try
		    {
		      logger.info("In prepareExcelStream");
		      HSSFWorkbook workbook = null;
		      workbook = new HSSFWorkbook();
		      HSSFSheet sheet = workbook.createSheet("BOE_Bulkupload_Error_List");
		      int rowIndex = 2;
		      int cellIndex;
		      if ((aaData != null) && (aaData.size() > 0))
		      {
		        HSSFRow headingRow = sheet.createRow(0);
		        cellIndex = 0;
		        headingRow.createCell(cellIndex++).setCellValue("PAYMENTREFNO");
		        headingRow.createCell(cellIndex++).setCellValue("EVENTREFNO");
		        headingRow.createCell(cellIndex++).setCellValue("PAYMENTAMOUNT");
		        headingRow.createCell(cellIndex++).setCellValue("PAYMENTAMNTCURR");
		        headingRow.createCell(cellIndex++).setCellValue("BOENUM");
		        headingRow.createCell(cellIndex++).setCellValue("BOEDATE");
		        headingRow.createCell(cellIndex++).setCellValue("PORTCODE");
		        headingRow.createCell(cellIndex++).setCellValue("BOEAMNT");
		        headingRow.createCell(cellIndex++).setCellValue("BOEAMNTCURR");
		        headingRow.createCell(cellIndex++).setCellValue("BOEAMNTENDORSE");
		        headingRow.createCell(cellIndex++).setCellValue("BOEALLOCAMNT");
		        headingRow.createCell(cellIndex++).setCellValue("CHANGEIECODE");
		        headingRow.createCell(cellIndex++).setCellValue("RECORDINDICATOR");
		        headingRow.createCell(cellIndex++).setCellValue("INVOICESERNUM");
		        headingRow.createCell(cellIndex++).setCellValue("INVOICENUM");
		        headingRow.createCell(cellIndex++).setCellValue("REALAMT");
		        headingRow.createCell(cellIndex++).setCellValue("REMARKS");
		        headingRow.createCell(cellIndex++).setCellValue("ERROR");
		      }
		      else
		      {
		        HSSFRow headingRow = sheet.createRow(0);
		        headingRow.createCell(0).setCellValue("NO ERRORS");
		      }
		      for (TransactionBoeBlkVO obj : aaData)
		      {
		        HSSFRow row = sheet.createRow(rowIndex);
		        int cellIndex = 0;
		        row.createCell(cellIndex++).setCellValue(obj.getPaymentRefNo());
		        row.createCell(cellIndex++).setCellValue(obj.getEventRefNo());
		        row.createCell(cellIndex++).setCellValue(obj.getPaymentAmnt());
		        row.createCell(cellIndex++).setCellValue(obj.getPayAmntCurr());
		        row.createCell(cellIndex++).setCellValue(obj.getBoeNo());
		        row.createCell(cellIndex++).setCellValue(obj.getBoeDate());
		        row.createCell(cellIndex++).setCellValue(obj.getPortCode());
		        row.createCell(cellIndex++).setCellValue(obj.getBoeAmnt());
		        row.createCell(cellIndex++).setCellValue(obj.getBoeAmntCurr());
		        row.createCell(cellIndex++).setCellValue(obj.getBoeAmntEndorse());
		        row.createCell(cellIndex++).setCellValue(obj.getBoeAllocAmnt());
		        row.createCell(cellIndex++).setCellValue(obj.getChangeIeCode());
		        row.createCell(cellIndex++).setCellValue(obj.getBesRecInd());
		        row.createCell(cellIndex++).setCellValue(obj.getInvoiceSerNo());
		        row.createCell(cellIndex++).setCellValue(obj.getInvoiceNo());
		        row.createCell(cellIndex++).setCellValue(obj.getInvRealAmt());
		        row.createCell(cellIndex++).setCellValue(obj.getRemarks());
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
		  
		  public ByteArrayInputStream prepareExcelStream1(ArrayList<BOEBulkUploadVO> aaData)

		  {

		    setContentDisposition("attachment; filename=\"BOE_Bulkupload_Error_List.xls\"");

		    try

		    {

		      logger.info("In prepareExcelStream");

		      HSSFWorkbook workbook = null;

		      workbook = new HSSFWorkbook();

		      HSSFSheet sheet = workbook.createSheet("BOE_Bulkupload_Error_List");

		      int rowIndex = 2;

		      int cellIndex;

		      if ((aaData != null) && (aaData.size() > 0))

		      {

		        HSSFRow headingRow = sheet.createRow(0);

		        cellIndex = 0;

		        headingRow.createCell(cellIndex++).setCellValue("PAYMENTREFNO");

		        headingRow.createCell(cellIndex++).setCellValue("EVENTREFNO");

		        headingRow.createCell(cellIndex++)

		          .setCellValue("PAYMENTAMOUNT");

		        headingRow.createCell(cellIndex++).setCellValue(

		          "PAYMENTAMNTCURR");

		        headingRow.createCell(cellIndex++).setCellValue("BOENUM");

		        headingRow.createCell(cellIndex++).setCellValue("BOEDATE");

		        headingRow.createCell(cellIndex++).setCellValue("PORTCODE");

		        headingRow.createCell(cellIndex++).setCellValue("BOEAMNT");

		        headingRow.createCell(cellIndex++).setCellValue("BOEAMNTCURR");

		        headingRow.createCell(cellIndex++).setCellValue(

		          "BOEAMNTENDORSE");

		        headingRow.createCell(cellIndex++).setCellValue("BOEALLOCAMNT");

		        headingRow.createCell(cellIndex++).setCellValue("CHANGEIECODE");

		        headingRow.createCell(cellIndex++).setCellValue(

		          "RECORDINDICATOR");

		        headingRow.createCell(cellIndex++)

		          .setCellValue("INVOICESERNUM");

		        headingRow.createCell(cellIndex++).setCellValue("INVOICENUM");

		        headingRow.createCell(cellIndex++).setCellValue("REALAMT");

		        headingRow.createCell(cellIndex++).setCellValue("REMARKS");

		        headingRow.createCell(cellIndex++).setCellValue("ERROR");

		      }

		      else

		      {

		        HSSFRow headingRow = sheet.createRow(0);

		        headingRow.createCell(0).setCellValue("NO ERRORS");

		      }

		      for (BOEBulkUploadVO obj : aaData)

		      {

		        HSSFRow row = sheet.createRow(rowIndex);

		        int cellIndex = 0;

		        row.createCell(cellIndex++).setCellValue(

		          obj.getPaymentRefNo());

		        row.createCell(cellIndex++).setCellValue(

		          obj.getEventRefNo());

		        row.createCell(cellIndex++).setCellValue(

		          obj.getPaymentAmnt());

		        row.createCell(cellIndex++).setCellValue(

		          obj.getPayAmntCurr());

		        row.createCell(cellIndex++).setCellValue(obj.getBoeNo());

		        row.createCell(cellIndex++).setCellValue(obj.getBoeDate());

		        row.createCell(cellIndex++).setCellValue(obj.getPortCode());

		        row.createCell(cellIndex++).setCellValue(obj.getBoeAmnt());

		        row.createCell(cellIndex++).setCellValue(

		          obj.getBoeAmntCurr());

		        row.createCell(cellIndex++).setCellValue(

		          obj.getBoeAmntEndorse());

		        row.createCell(cellIndex++).setCellValue(

		          obj.getBoeAllocAmnt());

		        row.createCell(cellIndex++).setCellValue(

		          obj.getChangeIeCode());

		        row.createCell(cellIndex++)

		          .setCellValue(obj.getBesRecInd());

		        row.createCell(cellIndex++).setCellValue(

		          obj.getInvoiceSerNo());

		        row.createCell(cellIndex++)

		          .setCellValue(obj.getInvoiceNo());

		        row.createCell(cellIndex++)

		          .setCellValue(obj.getInvRelAmt());

		        row.createCell(cellIndex++).setCellValue(obj.getRemarks());

		        row.createCell(cellIndex++)

		          .setCellValue(obj.getErrorDesc());

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

		 