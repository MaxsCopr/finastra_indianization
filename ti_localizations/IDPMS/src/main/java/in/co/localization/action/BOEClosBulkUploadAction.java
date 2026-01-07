package in.co.localization.action;
 
import in.co.localization.businessdelegate.exception.BusinessException;

import in.co.localization.businessdelegate.localization.BOEClosBulkUploadBD;

import in.co.localization.dao.exception.ApplicationException;

import in.co.localization.utility.CommonMethods;

import in.co.localization.vo.localization.AlertMessagesVO;

import in.co.localization.vo.localization.BOEClosBulkUploadVO;

import in.co.localization.vo.localization.TransactionClosVO;

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
 
public class BOEClosBulkUploadAction

  extends LocalizationBaseAction

{

  private static final long serialVersionUID = 1L;

  private static Logger logger = Logger.getLogger(BOEClosBulkUploadAction.class.getName());

  ArrayList<TransactionClosVO> closBoeList = null;

  ArrayList<TransactionClosVO> tiList = null;

  File inputFile;

  String fileNameRef;

  FileInputStream fis = null;

  BOEClosBulkUploadVO bulkVO = null;

  private String batchId;

  private ArrayList<AlertMessagesVO> alertMsgArray = null;

  public String getFileNameRef()

  {

    return this.fileNameRef;

  }

  public void setFileNameRef(String fileNameRef)

  {

    this.fileNameRef = fileNameRef;

  }

  public BOEClosBulkUploadVO getBulkVO()

  {

    return this.bulkVO;

  }

  public void setBulkVO(BOEClosBulkUploadVO bulkVO)

  {

    this.bulkVO = bulkVO;

  }

  private ArrayList<BOEClosBulkUploadVO> boeClsvoList = null;

  public ArrayList<BOEClosBulkUploadVO> getBoeClsvoList()

  {

    return this.boeClsvoList;

  }

  public void setBoeClsvoList(ArrayList<BOEClosBulkUploadVO> boeClsvoList)

  {

    this.boeClsvoList = boeClsvoList;

  }

  String[] chkList = null;

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

  public ArrayList<TransactionClosVO> getClosBoeList()

  {

    return this.closBoeList;

  }

  public void setClosBoeList(ArrayList<TransactionClosVO> closBoeList)

  {

    this.closBoeList = closBoeList;

  }

  public ArrayList<TransactionClosVO> getTiList()

  {

    return this.tiList;

  }

  public void setTiList(ArrayList<TransactionClosVO> tiList)

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

  public String BOECLOSBlk()

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

    logger.info("The value of Passing from struts xml =============>>>");

    logger.info("Entering Method");

    BOEClosBulkUploadBD bd = null;

    String ext = "";

    try

    {

      this.bulkVO = new BOEClosBulkUploadVO();

      bd = BOEClosBulkUploadBD.getBD();

      this.bulkVO.setInputFile(this.inputFile);

      if (this.bulkVO.getInputFile() != null)

      {

        ext = this.fileNameRef.split("\\.")[1];

        if (ext.equalsIgnoreCase("xls"))

        {

          TransactionClosVO trans = bd.getExcelValidate(this.bulkVO);

          if ((trans.getErrorList() != null) && (trans.getErrorList().size() > 0))

          {

            logger.info("In if ErrorList");

            validateExcelSheet(trans);

          }

          else if ((trans.getErrorCodeDesc() != null) && 

            (trans.getErrorCodeDesc().size() > 0))

          {

            validateExcelSheet(trans);

          }

          this.boeClsvoList = trans.getBoeClsvoList();

          this.closBoeList = trans.getBoeList();

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

		    BOEClosBulkUploadBD bd = null;

		    try

		    {

		      bd = BOEClosBulkUploadBD.getBD();

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

		    logger.info("This is Inside of upload Data Action ==================>>>");

		    logger.info("Entering Method");

		    BOEClosBulkUploadBD bd = null;

		    String status = "success";

		    try

		    {

		      if ((this.batchId != null) && (this.batchId.trim().length() != 0))

		      {

		        bd = BOEClosBulkUploadBD.getBD();

		        int stat = bd.uploadData(this.batchId);

		        if (stat > 0)

		        {

		          addActionMessage(stat + 

		            " Record(s) are Successfully uploaded!");

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

		  public void validateExcelSheet(TransactionClosVO trans)

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

		    ArrayList<TransactionClosVO> test = new ArrayList();

		    try

		    {

		      if (this.closBoeList != null)

		      {

		        for (TransactionClosVO vo : this.closBoeList)

		        {

		          TransactionClosVO closurevo = new TransactionClosVO();

		          closurevo.setBoeNo(vo.getBoeNo());

		          closurevo.setBoeDate(vo.getBoeDate());

		          closurevo.setPortCode(vo.getPortCode());

		          closurevo.setClosureType(vo.getClosureType());

		          closurevo.setAdjClsInd(vo.getAdjClsInd());

		          closurevo.setAdjClsDate(vo.getAdjClsDate());

		          closurevo.setApprovedBy(vo.getApprovedBy());

		          closurevo.setDocNo(vo.getDocNo());

		          closurevo.setDocDate(vo.getDocDate());

		          closurevo.setLetterNo(vo.getLetterNo());

		          closurevo.setLetterDate(vo.getLetterDate());

		          closurevo.setDocPortCode(vo.getDocPortCode());

		          closurevo.setRemarks(vo.getRemarks());

		          closurevo.setInvoiceSerNo(vo.getInvoiceSerNo());

		          closurevo.setInvoiceNo(vo.getInvoiceNo());

		          closurevo.setInvamnt(vo.getInvamnt());

		          closurevo.setInvcurr(vo.getInvcurr());

		          closurevo.setOutamnt(vo.getOutamnt());

		          closurevo.setAdjInvoiceAmtIC(vo.getAdjInvoiceAmtIC());

		          closurevo.setErrDesc(vo.getErrDesc());

		          test.add(closurevo);

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

		    ArrayList<BOEClosBulkUploadVO> test = new ArrayList();

		    this.bulkVO = new BOEClosBulkUploadVO();

		    try

		    {

		      logger.info("in excelDownload() ");

		      logger.info("chkList" + this.chkList.length);

		      if (this.chkList != null)

		      {

		        for (String e : this.chkList)

		        {

		          this.bulkVO = new BOEClosBulkUploadVO();

		          logger.info("eee" + e);

		          if (e != null)

		          {
		        	  String[] a = e.split(":");

		              if (a[19].length() != 4)

		              {

		                this.bulkVO.setBoeNo(a[0]);

		                this.bulkVO.setBoeDate(a[1]);

		                this.bulkVO.setPortCode(a[2]);

		                this.bulkVO.setClosType(a[3]);

		                this.bulkVO.setAdjClsInd(a[4]);

		                this.bulkVO.setAdjClsDate(a[5]);

		                this.bulkVO.setApprovedBy(a[6]);

		                this.bulkVO.setDocNo(a[7]);

		                this.bulkVO.setDocDate(a[8]);

		                this.bulkVO.setLetterNo(a[9]);

		                this.bulkVO.setLetterDate(a[10]);

		                this.bulkVO.setDocPort(a[11]);

		                this.bulkVO.setRemarks(a[12]);

		                this.bulkVO.setInvoiceSerNo(a[13]);

		                this.bulkVO.setInvoiceNo(a[14]);

		                this.bulkVO.setInvamnt(a[15]);

		                this.bulkVO.setInvcurr(a[16]);

		                this.bulkVO.setOutamnt(a[17]);

		                this.bulkVO.setAdjInvAmtIC(a[18]);

		                this.bulkVO.setErrorDesc(a[19]);

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

		    public ByteArrayInputStream prepareExcelStream(ArrayList<TransactionClosVO> aaData)

		    {

		      setContentDisposition("attachment; filename=\"BOE_Closure_Bulkupload_Error_List.xls\"");

		      try

		      {

		        logger.info("In prepareExcelStream");

		        HSSFWorkbook workbook = null;

		        workbook = new HSSFWorkbook();

		        HSSFSheet sheet = workbook

		          .createSheet("BOE_Closure_Bulkupload_Error_List");

		        int rowIndex = 2;

		        int cellIndex;

		        if ((aaData != null) && (aaData.size() > 0))

		        {

		          HSSFRow headingRow = sheet.createRow(0);

		          cellIndex = 0;

		          headingRow.createCell(cellIndex++).setCellValue("BOENUMBER");

		          headingRow.createCell(cellIndex++).setCellValue("BOEDATE");

		          headingRow.createCell(cellIndex++).setCellValue("PORTCODE");

		          headingRow.createCell(cellIndex++).setCellValue("CLOS_TYPE");

		          headingRow.createCell(cellIndex++).setCellValue("ADJ_CLS_IND");

		          headingRow.createCell(cellIndex++).setCellValue("ADJ_CLS_DATE");

		          headingRow.createCell(cellIndex++).setCellValue("APPROVED_BY");

		          headingRow.createCell(cellIndex++).setCellValue("DOC_NO");

		          headingRow.createCell(cellIndex++).setCellValue("DOC_DATE");

		          headingRow.createCell(cellIndex++).setCellValue("LETTER_NO");

		          headingRow.createCell(cellIndex++).setCellValue("LETTER_DATE");

		          headingRow.createCell(cellIndex++).setCellValue("DOC_PORT");

		          headingRow.createCell(cellIndex++).setCellValue("REMARKS");

		          headingRow.createCell(cellIndex++).setCellValue("INV_SER_NO");

		          headingRow.createCell(cellIndex++).setCellValue("INV_NO");

		          headingRow.createCell(cellIndex++)

		            .setCellValue("INVOICEAMOUNT");

		          headingRow.createCell(cellIndex++).setCellValue(

		            "INVOICECURRENCY");

		          headingRow.createCell(cellIndex++).setCellValue(

		            "OUTSTANDINGAMOUNTIC");

		          headingRow.createCell(cellIndex++).setCellValue(

		            "ADJ_INV_AMT_IC");

		          headingRow.createCell(cellIndex++).setCellValue("ERROR");

		        }

		        else

		        {

		          HSSFRow headingRow = sheet.createRow(0);

		          headingRow.createCell(0).setCellValue("NO ERRORS");

		        }

		        for (TransactionClosVO obj : aaData)

		        {

		          HSSFRow row = sheet.createRow(rowIndex);

		          int cellIndex = 0;

		          row.createCell(cellIndex++).setCellValue(obj.getBoeNo());

		          row.createCell(cellIndex++).setCellValue(obj.getBoeDate());

		          row.createCell(cellIndex++).setCellValue(obj.getPortCode());

		          row.createCell(cellIndex++).setCellValue(obj.getClosureType());

		          row.createCell(cellIndex++).setCellValue(obj.getAdjClsInd());

		          row.createCell(cellIndex++).setCellValue(obj.getAdjClsDate());

		          row.createCell(cellIndex++).setCellValue(obj.getApprovedBy());

		          row.createCell(cellIndex++).setCellValue(obj.getDocNo());

		          row.createCell(cellIndex++).setCellValue(obj.getDocDate());

		          row.createCell(cellIndex++).setCellValue(obj.getLetterNo());

		          row.createCell(cellIndex++).setCellValue(obj.getLetterDate());

		          row.createCell(cellIndex++).setCellValue(obj.getDocPortCode());

		          row.createCell(cellIndex++).setCellValue(obj.getRemarks());

		          row.createCell(cellIndex++).setCellValue(obj.getInvoiceSerNo());

		          row.createCell(cellIndex++).setCellValue(obj.getInvoiceNo());

		          row.createCell(cellIndex++).setCellValue(obj.getInvamnt());

		          row.createCell(cellIndex++).setCellValue(obj.getInvcurr());

		          row.createCell(cellIndex++).setCellValue(obj.getOutamnt());

		          row.createCell(cellIndex++).setCellValue(obj.getAdjInvoiceAmtIC());

		          row.createCell(cellIndex++).setCellValue(obj.getErrDesc());

		          rowIndex++;

		        }

		        ByteArrayOutputStream baos = new ByteArrayOutputStream();

		        workbook.write(baos);

		        this.excelStream = new ByteArrayInputStream(baos.toByteArray());

		        logger.info("prepareExcelStream COMPLETED");

		        workbook.close();

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

		    public ByteArrayInputStream prepareExcelStream1(ArrayList<BOEClosBulkUploadVO> aaData)

		    {

		      setContentDisposition("attachment; filename=\"BOE_Closure_Bulkupload_Error_List.xls\"");

		      try

		      {

		        logger.info("In prepareExcelStream");

		        HSSFWorkbook workbook = null;

		        workbook = new HSSFWorkbook();

		        HSSFSheet sheet = workbook

		          .createSheet("BOE_Closure_Bulkupload_Error_List");

		        int rowIndex = 2;

		        int cellIndex;
		        if ((aaData != null) && (aaData.size() > 0))

		        {

		          HSSFRow headingRow = sheet.createRow(0);

		          cellIndex = 0;

		          headingRow.createCell(cellIndex++).setCellValue("BOENUMBER");

		          headingRow.createCell(cellIndex++).setCellValue("BOEDATE");

		          headingRow.createCell(cellIndex++).setCellValue("PORTCODE");

		          headingRow.createCell(cellIndex++).setCellValue("CLOS_TYPE");

		          headingRow.createCell(cellIndex++).setCellValue("ADJ_CLS_IND");

		          headingRow.createCell(cellIndex++).setCellValue("ADJ_CLS_DATE");

		          headingRow.createCell(cellIndex++).setCellValue("APPROVED_BY");

		          headingRow.createCell(cellIndex++).setCellValue("DOC_NO");

		          headingRow.createCell(cellIndex++).setCellValue("DOC_DATE");

		          headingRow.createCell(cellIndex++).setCellValue("LETTER_NO");

		          headingRow.createCell(cellIndex++).setCellValue("LETTER_DATE");

		          headingRow.createCell(cellIndex++).setCellValue("DOC_PORT");

		          headingRow.createCell(cellIndex++).setCellValue("REMARKS");

		          headingRow.createCell(cellIndex++).setCellValue("INV_SER_NO");

		          headingRow.createCell(cellIndex++).setCellValue("INV_NO");

		          headingRow.createCell(cellIndex++)

		            .setCellValue("INVOICEAMOUNT");

		          headingRow.createCell(cellIndex++).setCellValue(

		            "INVOICECURRENCY");

		          headingRow.createCell(cellIndex++).setCellValue(

		            "OUTSTANDINGAMOUNTIC");

		          headingRow.createCell(cellIndex++).setCellValue(

		            "ADJ_INV_AMT_IC");

		          headingRow.createCell(cellIndex++).setCellValue("ERROR");

		        }

		        else

		        {

		          HSSFRow headingRow = sheet.createRow(0);

		          headingRow.createCell(0).setCellValue("NO ERRORS");

		        }

		        for (TransactionClosVO obj : aaData)

		        {

		          HSSFRow row = sheet.createRow(rowIndex);

		          int cellIndex = 0;

		          row.createCell(cellIndex++).setCellValue(obj.getBoeNo());

		          row.createCell(cellIndex++).setCellValue(obj.getBoeDate());

		          row.createCell(cellIndex++).setCellValue(obj.getPortCode());

		          row.createCell(cellIndex++).setCellValue(obj.getClosureType());

		          row.createCell(cellIndex++).setCellValue(obj.getAdjClsInd());

		          row.createCell(cellIndex++).setCellValue(obj.getAdjClsDate());

		          row.createCell(cellIndex++).setCellValue(obj.getApprovedBy());

		          row.createCell(cellIndex++).setCellValue(obj.getDocNo());

		          row.createCell(cellIndex++).setCellValue(obj.getDocDate());

		          row.createCell(cellIndex++).setCellValue(obj.getLetterNo());

		          row.createCell(cellIndex++).setCellValue(obj.getLetterDate());

		          row.createCell(cellIndex++).setCellValue(obj.getDocPortCode());

		          row.createCell(cellIndex++).setCellValue(obj.getRemarks());

		          row.createCell(cellIndex++).setCellValue(obj.getInvoiceSerNo());

		          row.createCell(cellIndex++).setCellValue(obj.getInvoiceNo());

		          row.createCell(cellIndex++).setCellValue(obj.getInvamnt());

		          row.createCell(cellIndex++).setCellValue(obj.getInvcurr());

		          row.createCell(cellIndex++).setCellValue(obj.getOutamnt());

		          row.createCell(cellIndex++).setCellValue(obj.getAdjInvoiceAmtIC());

		          row.createCell(cellIndex++).setCellValue(obj.getErrDesc());

		          rowIndex++;

		        }

		        ByteArrayOutputStream baos = new ByteArrayOutputStream();

		        workbook.write(baos);

		        this.excelStream = new ByteArrayInputStream(baos.toByteArray());

		        logger.info("prepareExcelStream COMPLETED");

		        workbook.close();

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

		    public ByteArrayInputStream prepareExcelStream1(ArrayList<BOEClosBulkUploadVO> aaData)

		    {

		      setContentDisposition("attachment; filename=\"BOE_Closure_Bulkupload_Error_List.xls\"");
		      try

		      {

		        logger.info("In prepareExcelStream");

		        HSSFWorkbook workbook = null;

		        workbook = new HSSFWorkbook();

		        HSSFSheet sheet = workbook

		          .createSheet("BOE_Closure_Bulkupload_Error_List");

		        int rowIndex = 2;

		        int cellIndex;

		        if ((aaData != null) && (aaData.size() > 0))

		        {

		          HSSFRow headingRow = sheet.createRow(0);

		          cellIndex = 0;

		          headingRow.createCell(cellIndex++).setCellValue("BOENUMBER");

		          headingRow.createCell(cellIndex++).setCellValue("BOEDATE");

		          headingRow.createCell(cellIndex++).setCellValue("PORTCODE");

		          headingRow.createCell(cellIndex++).setCellValue("CLOS_TYPE");

		          headingRow.createCell(cellIndex++).setCellValue("ADJ_CLS_IND");

		          headingRow.createCell(cellIndex++).setCellValue("ADJ_CLS_DATE");

		          headingRow.createCell(cellIndex++).setCellValue("APPROVED_BY");

		          headingRow.createCell(cellIndex++).setCellValue("DOC_NO");

		          headingRow.createCell(cellIndex++).setCellValue("DOC_DATE");

		          headingRow.createCell(cellIndex++).setCellValue("LETTER_NO");

		          headingRow.createCell(cellIndex++).setCellValue("LETTER_DATE");

		          headingRow.createCell(cellIndex++).setCellValue("DOC_PORT");

		          headingRow.createCell(cellIndex++).setCellValue("REMARKS");

		          headingRow.createCell(cellIndex++).setCellValue("INV_SER_NO");

		          headingRow.createCell(cellIndex++).setCellValue("INV_NO");

		          headingRow.createCell(cellIndex++)

		            .setCellValue("INVOICEAMOUNT");

		          headingRow.createCell(cellIndex++).setCellValue(

		            "INVOICECURRENCY");

		          headingRow.createCell(cellIndex++).setCellValue(

		            "OUTSTANDINGAMOUNTIC");

		          headingRow.createCell(cellIndex++).setCellValue(

		            "ADJ_INV_AMT_IC");

		          headingRow.createCell(cellIndex++).setCellValue("ERROR");

		        }

		        else

		        {

		          HSSFRow headingRow = sheet.createRow(0);

		          headingRow.createCell(0).setCellValue("NO ERRORS");

		        }

		        for (BOEClosBulkUploadVO obj : aaData)

		        {

		          HSSFRow row = sheet.createRow(rowIndex);

		          int cellIndex = 0;

		          row.createCell(cellIndex++).setCellValue(obj.getBoeNo());

		          row.createCell(cellIndex++).setCellValue(obj.getBoeDate());

		          row.createCell(cellIndex++).setCellValue(obj.getPortCode());

		          row.createCell(cellIndex++).setCellValue(obj.getClosType());

		          row.createCell(cellIndex++).setCellValue(obj.getAdjClsInd());

		          row.createCell(cellIndex++).setCellValue(obj.getAdjClsDate());

		          row.createCell(cellIndex++).setCellValue(obj.getApprovedBy());

		          row.createCell(cellIndex++).setCellValue(obj.getDocNo());

		          row.createCell(cellIndex++).setCellValue(obj.getDocPort());

		          row.createCell(cellIndex++).setCellValue(obj.getLetterNo());

		          row.createCell(cellIndex++).setCellValue(obj.getLetterDate());

		          row.createCell(cellIndex++).setCellValue(obj.getDocPort());

		          row.createCell(cellIndex++).setCellValue(obj.getRemarks());

		          row.createCell(cellIndex++).setCellValue(obj.getInvoiceSerNo());

		          row.createCell(cellIndex++).setCellValue(obj.getInvoiceNo());

		          row.createCell(cellIndex++).setCellValue(obj.getInvamnt());

		          row.createCell(cellIndex++).setCellValue(obj.getInvcurr());

		          row.createCell(cellIndex++).setCellValue(obj.getOutamnt());

		          row.createCell(cellIndex++).setCellValue(obj.getAdjInvAmtIC());

		          row.createCell(cellIndex++).setCellValue(obj.getErrorDesc());

		          rowIndex++;

		        }

		        ByteArrayOutputStream baos = new ByteArrayOutputStream();

		        workbook.write(baos);

		        this.excelStream = new ByteArrayInputStream(baos.toByteArray());

		        workbook.close();

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