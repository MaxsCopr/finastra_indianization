package in.co.localization.action;

import in.co.localization.businessdelegate.localization.XMLFileBD;
import in.co.localization.dao.exception.ApplicationException;
import in.co.localization.utility.CommonMethods;
import in.co.localization.vo.localization.ExcelDataVO;
import in.co.localization.vo.localization.XMLFileVO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;

public class EdpmsXMLUploadAction
  extends LocalizationBaseAction
{
  private static Logger logger = Logger.getLogger(EdpmsXMLUploadAction.class.getName());
  private File fileUpload;
  private String fileName;
  private String fileUploadFileName;
  String contentDisposition;
  ByteArrayInputStream excelStream;
  private ArrayList<XMLFileVO> fileList = null;
  XMLFileVO xmlFileVO = null;
  private static final long serialVersionUID = 1L;
 
  public static void main(String[] args)
  {
    logger.info("logger");
  }
 
  public ArrayList<XMLFileVO> getFileList()
  {
    return this.fileList;
  }
 
  public void setFileList(ArrayList<XMLFileVO> fileList)
  {
    this.fileList = fileList;
  }
 
  public XMLFileVO getXmlFileVO()
  {
    return this.xmlFileVO;
  }
 
  public void setXmlFileVO(XMLFileVO xmlFileVO)
  {
    this.xmlFileVO = xmlFileVO;
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
 
  public String execute()
    throws ApplicationException
  {
    try
    {
      isSessionAvailable();
    }
    catch (Exception e)
    {
      logger.info("xmlUploadexecute------Exception" + e);
    }
    return "success";
  }
 
  public String fileUpload()
    throws ApplicationException
  {
    logger.info("fileUpload---------------Entering Method");
   
    String result = null;
    XMLFileVO xmlFileVO = null;
    XMLFileBD bd = null;
    try
    {
      xmlFileVO = new XMLFileVO();
      bd = new XMLFileBD();
     
      logger.info("Filename---------------------->" + getFileUploadFileName());
     
      xmlFileVO.setFileUpload(getFileUpload());
      xmlFileVO.setFileName(getFileUploadFileName());
      if (xmlFileVO.getFileUpload() != null)
      {
        xmlFileVO = bd.readXMLFileData(xmlFileVO);
        String sTagName = "";
        if (xmlFileVO != null)
        {
          result = xmlFileVO.getResult();
         

          logger.info("Already Exist  Table Details ------------>" + result);
         

          File xmlFileUpload = null;
          String xmlFileName = null;
          String xmlFileFormat = null;
          String fileName = null;
          String file = null;
         
          xmlFileUpload = xmlFileVO.getFileUpload();
          xmlFileName = xmlFileVO.getFileName();
          xmlFileFormat = FilenameUtils.getExtension(xmlFileName);
         

          logger.info("Extension format------>" + xmlFileFormat);
          if (xmlFileFormat.equalsIgnoreCase("xml"))
          {
            String fileNamewithEx = xmlFileName.substring(0, xmlFileName.lastIndexOf('.'));
           
            int pos = fileNamewithEx.lastIndexOf(".");
            if (pos > 0) {
              fileName = fileNamewithEx.substring(0, fileNamewithEx.lastIndexOf('.'));
            }
            String tempFile = "";
           
            tempFile = xmlFileName.substring(0, xmlFileName.lastIndexOf('.'));
           
            int removedot = tempFile.indexOf(".") + 1;
           
            file = tempFile.substring(removedot, tempFile.length());
           
            int pos1 = tempFile.lastIndexOf(".");
            if (pos1 > 0) {
              fileName = tempFile.substring(0, tempFile.lastIndexOf('.'));
            } else {
              fileName = file;
            }
            fileName = fileName + file;
          }
          logger.info("File name--------------------->" + fileName);
          if (xmlFileVO.getXmlErrTagName() != null) {
            sTagName = xmlFileVO.getXmlErrTagName();
          }
          int i = xmlFileVO.getShpCount();
         

          logger.info(" ---xmlFileVO.getShpCount()--------->" + i);
          int j = xmlFileVO.getInvCount();
         
          logger.info(" ---xmlFileVO.getInvCount()--------->" + j);
          int k = xmlFileVO.getErrCount();
          if (result.equalsIgnoreCase("N"))
          {
            addActionError("File Name Already Exists");
          }
          else if (result.equalsIgnoreCase("Y"))
          {
            if ((file.equalsIgnoreCase("passrodack")) || (file.equalsIgnoreCase("failrodack"))) {
              addActionMessage("ROD Ack File Uploaded Successfully with " + i + " Shipping Bill(s)");
            }
            if ((file.equalsIgnoreCase("passwsnack")) || (file.equalsIgnoreCase("failwsnack"))) {
              addActionMessage("WNS Ack File Uploaded Successfully with " + i + " Shipping Bill(s) and   " + j + " Invoice(s)");
            } else {
              addActionMessage("File Uploaded Successfully with " + i + " Shipping Bill(s) and " + j + " Invoice(s)");
            }
          }
          else if (result.equalsIgnoreCase("PL"))
          {
            addActionMessage(" File Partially Uploaded Successfully with  Go and get from 'Get Error List' link");
          }
          else if (result.equalsIgnoreCase("F"))
          {
            addActionMessage("File Uploaded Successfully with " + i + " Shipping Bill(s) and " + j + " Invoice(s) and some File Data Already Exists  Go and get from 'Get Error List' link ");
          }
          else if (result.equalsIgnoreCase("EX"))
          {
            addActionError("Invalid File Format");
          }
          else if (result.equalsIgnoreCase("SHP"))
          {
            addActionError("Shipping Bill Header Count Not Match with Total Count");
          }
          else if (result.equalsIgnoreCase("INV"))
          {
            addActionError("Invoice Header Count Not Match with Total Count");
          }
          else if (result.equalsIgnoreCase("BOTH"))
          {
            addActionError("Shipping Bill and Invoice Header Count Not Match with Total Count");
          }
          else if (result.equalsIgnoreCase("XMLE"))
          {
            addActionError("XML Tag Not Matched with or Next Tag of  " + sTagName + " Tag");
          }
          else if (result.equalsIgnoreCase("FTC"))
          {
            addActionError(xmlFileVO.getXmlTagCount());
          }
          else if (result.equalsIgnoreCase("ERR"))
          {
            addActionError("ERROR IN UPLODA FILE Go and get from 'Get Error List' link");
          }
        }
      }
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting  fileUpload Method");
    return "success";
  }
 
  public ByteArrayInputStream prepareExcelStream(XMLFileVO xmlFileVO)
    throws ApplicationException, IOException
  {
    logger.info("Entering -----------prepareExcelStream------------Method");
    setContentDisposition("attachment; filename=\"XML_ErrorList.xls\"");
   
    ArrayList aaData = null;
    HSSFWorkbook hwb = null;
    try
    {
      aaData = new ArrayList();
      hwb = new HSSFWorkbook();
      HSSFSheet sheet = hwb.createSheet("XML_ErrorList_Data");
     
      int i = 0;
     
      Font f = hwb.createFont();
      f.setBoldweight((short)700);
      f.setFontName("Courier New");
      HSSFCellStyle style1 = hwb.createCellStyle();
      style1.setAlignment((short)1);
      style1.setFont(f);
     
      Font f1 = hwb.createFont();
      f1.setFontName("Courier New");
      HSSFCellStyle style2 = hwb.createCellStyle();
      style2.setAlignment((short)1);
      style2.setFont(f);
     
      aaData = xmlFileVO.getXmlErrorList();
     
      HashMap<Integer, String> map = new HashMap();
      map.put(Integer.valueOf(0), "SHIPPING BILL NO");
      map.put(Integer.valueOf(1), "SHIPPING BILL DATE");
      map.put(Integer.valueOf(2), "PORTCODE");
      map.put(Integer.valueOf(3), "FORM NO");
      map.put(Integer.valueOf(4), "INVOICE SERIAL NO");
      map.put(Integer.valueOf(5), "INVOICE NO");
      map.put(Integer.valueOf(6), "ERROR DESCRIPTION");
     
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
        cellData1.setCellValue(vo.getShipBillNo());
       
        Cell cellData2 = row.createCell(1);
        cellData2.setCellStyle(style2);
        cellData2.setCellValue(vo.getShipBillDate());
       
        Cell cellData3 = row.createCell(2);
        cellData3.setCellStyle(style2);
        cellData3.setCellValue(vo.getPortCode());
       
        Cell cellData4 = row.createCell(3);
        cellData4.setCellStyle(style2);
        cellData4.setCellValue(vo.getFormNo());
       
        Cell cellData5 = row.createCell(4);
        cellData5.setCellStyle(style2);
        cellData5.setCellValue(vo.getInvSerialNo());
       
        Cell cellData6 = row.createCell(5);
        cellData6.setCellStyle(style2);
        cellData6.setCellValue(vo.getInvNo());
       
        Cell cellData7 = row.createCell(6);
        cellData7.setCellStyle(style2);
        cellData7.setCellValue(vo.getShpErrorString());
       
        i++;
      }
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      hwb.write(baos);
      this.excelStream = new ByteArrayInputStream(baos.toByteArray());
    }
    catch (Exception exception)
    {
      logger.info("Entering -----------prepareExcelStream------------Method----exception" + exception);
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
     

      logger.info("The Return result is : " + iResult);
      if (iResult == 0)
      {
        addActionError("File Not Found");
      }
      else if (iResult == 1)
      {
        addActionMessage("No Error messages found this File Name");
      }
      else if ((iResult == 2) || (iResult == 1024))
      {
        logger.info("This is error list page : " + iResult);
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