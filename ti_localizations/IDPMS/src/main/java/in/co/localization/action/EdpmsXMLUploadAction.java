package in.co.localization.action;
 
import in.co.localization.businessdelegate.localization.XMLFileBD;

import in.co.localization.dao.exception.ApplicationException;

import in.co.localization.utility.ActionConstants;

import in.co.localization.utility.CommonMethods;

import in.co.localization.utility.ValidationUtility;

import in.co.localization.vo.localization.AlertMessagesVO;

import in.co.localization.vo.localization.ExcelDataVO;

import in.co.localization.vo.localization.XMLFileVO;

import java.io.ByteArrayInputStream;

import java.io.ByteArrayOutputStream;

import java.io.File;

import java.io.IOException;

import java.util.ArrayList;

import java.util.HashMap;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import org.apache.poi.hssf.usermodel.HSSFCell;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;

import org.apache.poi.hssf.usermodel.HSSFRow;

import org.apache.poi.hssf.usermodel.HSSFSheet;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.apache.poi.ss.usermodel.Cell;

import org.apache.poi.ss.usermodel.Font;

import org.apache.poi.ss.usermodel.HorizontalAlignment;

import org.apache.struts2.ServletActionContext;
 
public class EdpmsXMLUploadAction

  extends LocalizationBaseAction

{

  private static Logger logger = Logger.getLogger(EdpmsXMLUploadAction.class

    .getName());

  private File fileUpload;

  private String fileName;

  private String fileUploadFileName;

  String contentDisposition;

  ByteArrayInputStream excelStream;

  Map<String, String> boeTypeList;

  Map<String, String> appGivenByList;

  Map<String, String> indicatorList;

  private ArrayList<AlertMessagesVO> alertMsgArray = null;

  private ArrayList<XMLFileVO> fileList = null;

  XMLFileVO xmlFileVO = null;

  private static final long serialVersionUID = 1L;

  public XMLFileVO getXmlFileVO()

  {

    return this.xmlFileVO;

  }

  public void setXmlFileVO(XMLFileVO xmlFileVO)

  {

    this.xmlFileVO = xmlFileVO;

  }

  public ArrayList<XMLFileVO> getFileList()

  {

    return this.fileList;

  }

  public void setFileList(ArrayList<XMLFileVO> fileList)

  {

    this.fileList = fileList;

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

  public Map<String, String> getIndicatorList()

  {

    return ActionConstants.ADJ_CLOSURE_IND;

  }

  public void setIndicatorList(Map<String, String> indicatorList)

  {

    this.indicatorList = indicatorList;

  }

  public Map<String, String> getAppGivenByList()

  {

    return ActionConstants.APP_BY;

  }

  public void setAppGivenByList(Map<String, String> appGivenByList)

  {

    this.appGivenByList = appGivenByList;

  }

  public Map<String, String> getBoeTypeList()

  {

    return ActionConstants.BOE_TYPE;

  }

  public void setBoeTypeList(Map<String, String> boeTypeList)

  {

    this.boeTypeList = boeTypeList;

  }

  public String execute()

    throws IOException

  {

    try

    {

      isSessionAvailable();

    }

    catch (Exception e)

    {

      logger.info("IDPMS FILEUPLOAD Exception------------>" + e);

    }

    return "success";

  }

  public String landingPage()

    throws IOException

  {

    String sessionUserName = null;

    XMLFileVO xmlFileVO = null;

    XMLFileBD bd = null;

    String target = null;

    try

    {

      xmlFileVO = new XMLFileVO();

      isSessionAvailable();

      HttpSession session = ServletActionContext.getRequest().getSession();

      sessionUserName = (String)session.getAttribute("loginedUserName");

      logger.info("Maker sessionUserName  : " + sessionUserName);

      if (sessionUserName != null)

      {

        logger.info("Inside outer session name");

        bd = new XMLFileBD();

        logger.info("sessionUserName--->" + sessionUserName);

        xmlFileVO.setSessionUserName(sessionUserName);

        xmlFileVO.setPageType("IDPMS TEAM DFB");

        int count = bd.checkLoginedUserType(xmlFileVO);

        logger.info("Count Session Name for Checker Eligiblity");

        if (count > 0) {

          target = "success";

        } else {

          target = "fail";

        }

        logger.info("target-------------" + target);

      }

      else

      {

        target = "fail";

      }

    }

    catch (Exception e)

    {

      logger.info("IDPMS FILE Upload Exception------------>" + e);

    }

    return target;

  }

  public String fileUploadError()

    throws ApplicationException

  {

    XMLFileVO xmlFileVO = null;

    try

    {

      if (xmlFileVO == null) {

        xmlFileVO = new XMLFileVO();

      }

      addActionError(" File should not exceed 5MB");

    }

    catch (Exception e)

    {

      throwApplicationException(e);

    }

    return "success";

  }
  public String fileUpload()

		    throws ApplicationException

		  {

		    logger.info("-------------fileUpload----------");

		    String result = null;

		    String target = "";

		    String sXMLFileName = "";

		    XMLFileVO xmlFileVO = null;

		    XMLFileBD bd = null;

		    try

		    {

		      xmlFileVO = new XMLFileVO();

		      bd = new XMLFileBD();

		      xmlFileVO.setFileUpload(getFileUpload());

		      xmlFileVO.setFileName(getFileUploadFileName());

		 
		      validateXMLFiles(xmlFileVO);

		      if (this.alertMsgArray.size() == 0)

		      {

		        if (xmlFileVO.getFileUpload() != null)

		        {

		          xmlFileVO = bd.readXMLFileData(xmlFileVO);

		          String sTagName = "";

		          if (xmlFileVO != null)

		          {

		            result = xmlFileVO.getResult();

		            logger.info("-------result--------- " + result);

		            if (xmlFileVO.getXmlErrTagName() != null) {

		              sTagName = xmlFileVO.getXmlErrTagName();

		            }

		            String fName = xmlFileVO.getfileName();

		            logger.info("FileName inside fileUpload method " + fName);

		            int Outref = xmlFileVO.getOutref();

		            logger.info("FileCount inside fileUpload method " + Outref);

		            int outRefFailedCount = xmlFileVO.getOutrefFailedCount();

		            logger.info("FileCount inside fileUpload method " + outRefFailedCount);

		            String file = xmlFileVO.getFilePat();

		            logger.info("File Name " + file);

		            if (((file != null) && (file.equalsIgnoreCase("failormAck"))) || ((file != null) && (file.equalsIgnoreCase("passormAck"))) || ((fName != null) && (fName.equalsIgnoreCase("failormAck"))))

		            {

		              if (result.equalsIgnoreCase("N"))

		              {

		                addActionError("File Name Already Exists");

		                logger.info("--------Inside ORM if Loop------");

		              }

		              else if (result.equalsIgnoreCase("PL"))

		              {

		                addActionMessage("File partially uploaded for " + Outref + " record successfully and  " + outRefFailedCount + " records failed, Please check the Error Details in 'Get Error List Link'");

		              }

		              else if (result.equalsIgnoreCase("Y"))

		              {

		                addActionMessage("File Uploaded Successfully with " + Outref + " Outward Remittances");

		              }

		              else if (result.equalsIgnoreCase("ACKFAIL"))

		              {

		                addActionMessage("ORM UPload Failed,Please check the Error Details in 'Get Error List Link'");

		              }

		            }

		            else

		            {

		              logger.info("No.of BOEs ---------------------123456------------------- :");

		              int bes_count = xmlFileVO.getInsertCount();

		              logger.info("No.of BOEs ----------bes_count------------- :" + bes_count);

		              int i = xmlFileVO.getBoeCount();

		              logger.info("No.of BOEs ---------------------------------------- :" + i);

		              int j = xmlFileVO.getInvCount();

		              logger.info("No.of Invoices ---------------------------------------- :" + j);

		              int k = xmlFileVO.getErrCount();

		              logger.info("No.of Errors ---------------------------------------- :" + k);

		              logger.info("File Name---------------------------------------- :" + file);

		              if (result.equalsIgnoreCase("N"))

		              {

		                addActionError("File Name Already Exists");

		              }

		              else if (result.equalsIgnoreCase("Y"))

		              {

		                if (((file != null) && (file.equalsIgnoreCase("passbesAck"))) || ((file != null) && (file.equalsIgnoreCase("failbesAck")))) {

		                  addActionMessage("BES ACK File Uploaded Successfully with " + i + " Records(s)");

		                } else if (((file != null) && (file.equalsIgnoreCase("passbeeAck"))) || ((file != null) && (file.equalsIgnoreCase("failbeeAck")))) {

		                  addActionMessage("BEE ACK File Uploaded Successfully with " + i + " Records(s)");

		                } else if (((file != null) && (file.equalsIgnoreCase("passoraAck"))) || ((file != null) && (file.equalsIgnoreCase("failoraAck")))) {

		                  addActionMessage("ORA ACK File Uploaded Successfully with " + i + " Records(s)");

		                } else if (((file != null) && (file.equalsIgnoreCase("btt"))) || ((file != null) && (file.equalsIgnoreCase("btt")))) {

		                  addActionMessage("BTT File Uploaded Successfully with " + i + " Records(s)");

		                } else if (((file != null) && (file.equalsIgnoreCase("passmbeAck"))) || ((file != null) && (file.equalsIgnoreCase("failmbeAck")))) {

		                  addActionMessage("MBE ACK File Uploaded Successfully with " + i + " BOE(s) and " + j + " Invoice(s)");

		                } else {

		                  addActionMessage("File Uploaded Successfully with " + i + " BOE(s) and " + j + " Invoice(s)");

		                }

		              }

		              else if (result.equalsIgnoreCase("PL"))

		              {
		            	  if (((file != null) && (file.equalsIgnoreCase("passbesAck"))) || ((file != null) && (file.equalsIgnoreCase("failbesAck")))) {

		                      addActionMessage("BES ACK File Partially Uploaded  with " + i + " Records(s) and " + k + " records failed.");

		                    } else if (((file != null) && (file.equalsIgnoreCase("passbeeAck"))) || ((file != null) && (file.equalsIgnoreCase("failbeeAck")))) {

		                      addActionMessage("BEE ACK File Partially  Uploaded  with " + i + " Records(s) and " + k + " records failed.");

		                    } else if (((file != null) && (file.equalsIgnoreCase("passoraAck"))) || ((file != null) && (file.equalsIgnoreCase("failoraAck")))) {

		                      addActionMessage("ORA ACK File Partially Uploaded  with " + i + " Records(s) and " + k + " records failed.");

		                    } else if (((file != null) && (file.equalsIgnoreCase("btt"))) || ((file != null) && (file.equalsIgnoreCase("btt")))) {

		                      addActionMessage("BTT File Partially Uploaded  with " + i + " Records(s) and " + k + " records failed.");

		                    } else if (((file != null) && (file.equalsIgnoreCase("passmbeAck"))) || ((file != null) && (file.equalsIgnoreCase("failmbeAck")))) {

		                      addActionMessage("MBE ACK File Partially  Uploaded  with " + i + " BOE(s) and " + j + " Invoice(s)and " + k + " records failed.");

		                    } else {

		                      addActionMessage(" File partially uploaded successfully with " + i + " BOE(s) and " + j + " Invoice(s) and " + k + " BOE(s) not get Uploaded, go and get from 'Get Error List' link");

		                    }

		                  }

		                  else if (result.equalsIgnoreCase("F"))

		                  {

		                    addActionMessage("OBE UPload Failed,Please check the Error Details in 'Get Error List Link'");

		                  }

		                  else if (result.equalsIgnoreCase("EX"))

		                  {

		                    addActionError("Invalid File Format");

		                  }

		                  else if (result.equalsIgnoreCase("ACK"))

		                  {

		                    addActionError("ACknowledgement Data Not Present");

		                  }

		                  else if (result.equalsIgnoreCase("FILEFAILED"))

		                  {

		                    addActionError("File Failed Because of Data Error. Please check the File.");

		                  }

		                  else if (result.equalsIgnoreCase("INF"))

		                  {

		                    addActionError("Invalid File Format");

		                  }

		                  else if (result.equalsIgnoreCase("ACKFAIL"))

		                  {

		                    logger.info("File ALready Exist Messages");

		                    addActionError(" File Uploaded Data Already Exists");

		                  }

		                  else if (result.equalsIgnoreCase("IFD"))

		                  {

		                    addActionError("Invalid File Format");

		                  }

		                  else if (result.equalsIgnoreCase("BOE"))

		                  {

		                    addActionError("BOE Header Count Not Match with Total Count");

		                  }

		                  else if (result.equalsIgnoreCase("INV"))

		                  {

		                    addActionError("Invoice Header Count Not Match with Total Count");

		                  }

		                  else if (result.equalsIgnoreCase("BOTH"))

		                  {

		                    addActionError("BOE and Invoice Header Count Not Match with Total Count");

		                  }

		                  else if (result.equalsIgnoreCase("XMLE"))

		                  {

		                    addActionError("XML Tag Not Matched with or Next Tag of  " + sTagName + " Tag");

		                  }

		                }

		              }

		            }

		          }

		          else

		          {

		            logger.info("XML File Upload Error");

		            target = "Error";

		          }

		          logger.info("XML File Upload Error-----target for struts action--------------" + target);

		        }

		        catch (Exception exception)

		        {

		          logger.info("XML File Upload Error----------Exception" + exception);

		          exception.printStackTrace();

		          addActionError(exception.getMessage());

		        }

		        logger.info("Exiting Method");

		        return "success";

		      }

		      public ByteArrayInputStream prepareExcelStream(XMLFileVO xmlFileVO)

		        throws ApplicationException, IOException

		      {

		        logger.info("Entering Method");

		        setContentDisposition("attachment; filename=\"IDPMS_XML_ErrorList.xls\"");

		        ArrayList<Object> aaData = null;

		        HSSFWorkbook hwb = null;

		        try

		        {

		          aaData = new ArrayList();

		          hwb = new HSSFWorkbook();

		          HSSFSheet sheet = hwb.createSheet("XML_ErrorList_IDPMS_Data");

		          int i = 0;

		     
		          Font f = hwb.createFont();

		          f.setBold(true);

		          f.setFontName("Courier New");

		          HSSFCellStyle style1 = hwb.createCellStyle();

		          style1.setAlignment(HorizontalAlignment.LEFT);

		          style1.setFont(f);

		     
		     
		          Font f1 = hwb.createFont();

		          f1.setFontName("Courier New");

		          HSSFCellStyle style2 = hwb.createCellStyle();

		          style2.setAlignment(HorizontalAlignment.LEFT);

		          style2.setFont(f);

		          aaData = xmlFileVO.getXmlErrorList();

		          HashMap<Integer, String> map = new HashMap();

		          map.put(Integer.valueOf(0), "BOE NO");

		          map.put(Integer.valueOf(1), "BOE DATE");

		          map.put(Integer.valueOf(2), "PORTCODE");

		          map.put(Integer.valueOf(3), "INVOICE SERIAL NO");

		          map.put(Integer.valueOf(4), "INVOICE NO");

		          map.put(Integer.valueOf(5), "ERROR DESCRIPTION");

		          HSSFRow row = sheet.createRow(i);

		          for (Integer key : map.keySet())

		          {

		            HSSFCell cell = row.createCell(key.intValue());

		            cell.setCellStyle(style1);

		            cell.setCellValue((String)map.get(key));

		          }

		          i++;

		          for (Object obj : aaData)

		          {

		            ExcelDataVO vo = (ExcelDataVO)obj;

		            row = sheet.createRow(i);

		            Cell cellData1 = row.createCell(0);

		            cellData1.setCellStyle(style2);

		            cellData1.setCellValue(vo.getBoeNo());
		            Cell cellData2 = row.createCell(1);

		            cellData2.setCellStyle(style2);

		            cellData2.setCellValue(vo.getBoeDate());

		            Cell cellData3 = row.createCell(2);

		            cellData3.setCellStyle(style2);

		            cellData3.setCellValue(vo.getPortCode());

		            Cell cellData5 = row.createCell(3);

		            cellData5.setCellStyle(style2);

		            cellData5.setCellValue(vo.getInvSerialNo());

		            Cell cellData6 = row.createCell(4);

		            cellData6.setCellStyle(style2);

		            cellData6.setCellValue(vo.getInvNo());

		            Cell cellData7 = row.createCell(5);

		            cellData7.setCellStyle(style2);

		            cellData7.setCellValue(vo.getBoeErrorString());

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

		      public ByteArrayInputStream prepareExcelStream1(XMLFileVO xmlFileVO)

		        throws ApplicationException, IOException

		      {

		        logger.info("Entering Method");

		        setContentDisposition("attachment; filename=\"XML_ErrorList_Data.xls\"");

		        ArrayList<Object> aaData = null;

		        HSSFWorkbook hwb = null;

		        XMLFileBD bd = null;

		        try

		        {

		          aaData = new ArrayList();

		          hwb = new HSSFWorkbook();

		          HSSFSheet sheet = hwb.createSheet("XML_ErrorList_Data");

		          int i = 0;

		     
		          Font f = hwb.createFont();

		          f.setBold(true);

		          f.setFontName("Courier New");

		          HSSFCellStyle style1 = hwb.createCellStyle();

		          style1.setAlignment(HorizontalAlignment.LEFT);

		          style1.setFont(f);

		     
		     
		          Font f1 = hwb.createFont();

		          f1.setFontName("Courier New");

		          HSSFCellStyle style2 = hwb.createCellStyle();

		          style2.setAlignment(HorizontalAlignment.LEFT);

		          style2.setFont(f);

		          aaData = xmlFileVO.getXmlOrmAckErrorList();

		          HashMap<Integer, String> map = new HashMap();

		          map.put(Integer.valueOf(0), "OUTWARDREFERNECNO/BOE_NO");

		          map.put(Integer.valueOf(1), "ADCODE");

		          map.put(Integer.valueOf(2), "IECODE");

		          map.put(Integer.valueOf(3), "FILENAME");

		          map.put(Integer.valueOf(4), "ERROR_DESC");

		          HSSFRow row = sheet.createRow(i);

		          for (Integer key : map.keySet())

		          {

		            HSSFCell cell = row.createCell(key.intValue());

		            cell.setCellStyle(style1);

		            cell.setCellValue((String)map.get(key));

		          }

		          i++;

		          for (Object obj : aaData)

		          {

		            ExcelDataVO vo = (ExcelDataVO)obj;

		            row = sheet.createRow(i);

		            Cell cellData1 = row.createCell(0);

		            cellData1.setCellStyle(style2);

		            cellData1.setCellValue(vo.getOUTWARDREFERNECNO());

		            Cell cellData2 = row.createCell(1);

		            cellData2.setCellStyle(style2);

		            cellData2.setCellValue(vo.getADCODE());

		            Cell cellData3 = row.createCell(2);

		            cellData3.setCellStyle(style2);

		            cellData3.setCellValue(vo.getIECODE());

		            Cell cellData4 = row.createCell(3);

		            cellData4.setCellStyle(style2);

		            cellData4.setCellValue(vo.getFILENAMEFROMERR());

		            Cell cellData5 = row.createCell(4);

		            cellData5.setCellStyle(style2);

		            cellData5.setCellValue(vo.getERROR_DESC());

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

		      public File getFileUpload()

		      {

		        return this.fileUpload;

		      }

		      public void setFileUpload(File fileUpload)

		      {

		        this.fileUpload = fileUpload;

		      }

		      public String getFileName()

		      {

		        return this.fileName;

		      }

		      public void setFileName(String fileName)

		      {

		        this.fileName = fileName;

		      }

		      public String getFileUploadFileName()

		      {

		        return this.fileUploadFileName;

		      }
		      public void setFileUploadFileName(String fileUploadFileName)

		      {

		        this.fileUploadFileName = fileUploadFileName;

		      }

		      public ArrayList<AlertMessagesVO> getAlertMsgArray()

		      {

		        return this.alertMsgArray;

		      }

		      public void setAlertMsgArray(ArrayList<AlertMessagesVO> alertMsgArray)

		      {

		        this.alertMsgArray = alertMsgArray;

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

		      public void validateXMLFiles(XMLFileVO xmlFileVO)

		        throws ApplicationException

		      {

		        CommonMethods commonMethods = null;

		        try

		        {

		          this.alertMsgArray = new ArrayList();

		          commonMethods = new CommonMethods();

		          if ((this.alertMsgArray != null) && 

		            (this.alertMsgArray.size() > 0)) {

		            this.alertMsgArray.clear();

		          }

		          if (!commonMethods.isNull(xmlFileVO.getFileName()))

		          {

		            boolean bfileSizeStatus = ValidationUtility.isValidFile(xmlFileVO.getFileName());

		            if (!bfileSizeStatus)

		            {

		              String errorcode = "Uploading File Size is More Than Eight MB";

		              Object[] arg = { "0", "E", errorcode, "Input" };

		              setErrorvalues(arg);

		            }

		          }

		          for (int a = 0; a < this.alertMsgArray.size(); a++) {

		            if (((AlertMessagesVO)this.alertMsgArray.get(a)).getErrorMsg().equalsIgnoreCase("N")) {

		              if ((xmlFileVO.getOverridStatus() != null) && 

		                (xmlFileVO.getOverridStatus().equalsIgnoreCase("Y")))

		              {

		                ((AlertMessagesVO)this.alertMsgArray.get(a)).setErrorMsg("Y");

		                xmlFileVO.setOverridStatus("Y");

		              }

		              else if ((xmlFileVO.getOverridStatus() != null) && 

		                (xmlFileVO.getOverridStatus().equalsIgnoreCase("N")))

		              {

		                ((AlertMessagesVO)this.alertMsgArray.get(a)).setErrorMsg("N");

		                xmlFileVO.setOverridStatus("N");

		              }

		            }

		          }

		        }

		        catch (Exception e)

		        {

		          throwApplicationException(e);

		        }

		      }

		      public String getXMLErrorPage()

		      {

		        logger.info("Entering Method");

		        logger.info("Exiting Method");

		        return "success";

		      }

		      public String getFileDownload()

		        throws ApplicationException

		      {

		        logger.info("Entering Method");

		        int iResult = 0;

		        CommonMethods commonMethods = null;

		        XMLFileBD bd = null;

		        try

		        {

		          commonMethods = new CommonMethods();

		          bd = XMLFileBD.getBD();

		          this.xmlFileVO = bd.getErrorListDownload(this.xmlFileVO);

		          iResult = this.xmlFileVO.getFileCount();

		          if (iResult == 0)

		          {

		            this.xmlFileVO = bd.getORMACKErrorListDownload(this.xmlFileVO);

		            this.excelStream = prepareExcelStream1(this.xmlFileVO);

		            return "json";

		          }

		          if (iResult == 0)

		          {

		            addActionError("File Not Found");

		          }

		          else if (iResult == 1)

		          {

		            addActionMessage("All records successfully Uploaded");

		          }

		          else if (iResult == 2)

		          {

		            this.excelStream = prepareExcelStream(this.xmlFileVO);

		            return "json";

		          }

		        }

		        catch (Exception exception)

		        {

		          throwApplicationException(exception);

		     
		          logger.info("Exiting Method");

		        }

		        return "success";

		      }

		      public String getXMLFileList()

		        throws ApplicationException

		      {

		        logger.info("Entering Method");

		        CommonMethods commonMethods = null;

		        int iResult = 0;

		        XMLFileBD bd = null;

		        try

		        {

		          commonMethods = new CommonMethods();

		          bd = XMLFileBD.getBD();

		          this.xmlFileVO = bd.getXMLFileList(this.xmlFileVO);

		          iResult = this.xmlFileVO.getFileCount();

		          if (iResult == 0) {

		            addActionError("File Not Found");

		          } else {

		            this.fileList = this.xmlFileVO.getFileList();

		          }

		        }

		        catch (Exception exception)

		        {

		          throwApplicationException(exception);

		        }

		        logger.info("Exiting Method");

		        return "success";

		      }

		    }
		     