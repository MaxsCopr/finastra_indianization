package in.co.FIRC.action;

import in.co.FIRC.businessdelegate.FIRCIssuanceBD;
import in.co.FIRC.businessdelegate.FIRCOurBankBD;
import in.co.FIRC.dao.exception.ApplicationException;
import in.co.FIRC.utility.ActionConstants;
import in.co.FIRC.utility.BarcodeGenerator;
import in.co.FIRC.utility.CommonMethods;
import in.co.FIRC.utility.DBUtility;
import in.co.FIRC.utility.WordDocGenerator;
import in.co.FIRC.vo.ourBank.CustomerDataVO;
import in.co.FIRC.vo.ourBank.IssuanceVO;
import in.co.FIRC.vo.ourBank.OurBankVO;
import in.co.FIRC.vo.ourBank.PrintOurBankVO;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletResponseAware;
 
public class FIRCIssuanceAction
  extends FIRCBaseAction
  implements ServletResponseAware
{
  private static Logger logger = LogManager.getLogger(FIRCIssuanceAction.class
    .getName());
  private static final long serialVersionUID = 1L;
  IssuanceVO issuanceVO;
  ArrayList<OurBankVO> issuanceList = null;
  ArrayList<OurBankVO> irmList = null;
  ArrayList<OurBankVO> irmExList = null;
  ArrayList<OurBankVO> irmClList = null;
  ArrayList<OurBankVO> ifscList;
  Map<String, String> transStatus;
  Map<String, String> irmStatus;
  ArrayList<CustomerDataVO> customerList;
  public ArrayList<CustomerDataVO> getCustomerList()
  {
    return this.customerList;
  }
  public void setCustomerList(ArrayList<CustomerDataVO> customerList)
  {
    this.customerList = customerList;
  }
  String[] chkList = null;
  CustomerDataVO cusDataVo = new CustomerDataVO();
  private HttpServletResponse response;
  private HttpServletResponse response1;
  String fircValue;
  public CustomerDataVO getCusDataVo()
  {
    return this.cusDataVo;
  }
  public void setCusDataVo(CustomerDataVO cusDataVo)
  {
    this.cusDataVo = cusDataVo;
  }
  public String[] getChkList()
  {
    return this.chkList;
  }
  public void setChkList(String[] chkList)
  {
    this.chkList = chkList;
  }
  public HttpServletResponse getResponse1()
  {
    return this.response1;
  }
  public void setResponse1(HttpServletResponse response1)
  {
    this.response1 = response1;
  }
  public ArrayList<OurBankVO> getIfscList()
  {
    return this.ifscList;
  }
  public void setIfscList(ArrayList<OurBankVO> ifscList)
  {
    this.ifscList = ifscList;
  }
  public ArrayList<OurBankVO> getIrmList()
  {
    return this.irmList;
  }
  public void setIrmList(ArrayList<OurBankVO> irmList)
  {
    this.irmList = irmList;
  }
  public Map<String, String> getIrmStatus()
  {
    return ActionConstants.IRM_STATUS;
  }
  public void setIrmStatus(Map<String, String> irmStatus)
  {
    this.irmStatus = irmStatus;
  }
  public ArrayList<OurBankVO> getIrmExList()
  {
    return this.irmExList;
  }
  public void setIrmExList(ArrayList<OurBankVO> irmExList)
  {
    this.irmExList = irmExList;
  }
  public ArrayList<OurBankVO> getIrmClList()
  {
    return this.irmClList;
  }
  public void setIrmClList(ArrayList<OurBankVO> irmClList)
  {
    this.irmClList = irmClList;
  }
  public String getFircValue()
  {
    return this.fircValue;
  }
  public void setFircValue(String fircValue)
  {
    this.fircValue = fircValue;
  }
  public HttpServletResponse getResponse()
  {
    return this.response;
  }
  public void setResponse(HttpServletResponse response)
  {
    this.response = response;
  }
  public Map<String, String> getTransStatus()
  {
    return ActionConstants.TRANS_STATUS;
  }
  public void setTransStatus(Map<String, String> transStatus)
  {
    this.transStatus = transStatus;
  }
  public IssuanceVO getIssuanceVO()
  {
    return this.issuanceVO;
  }
  public void setIssuanceVO(IssuanceVO issuanceVO)
  {
    this.issuanceVO = issuanceVO;
  }
  public ArrayList<OurBankVO> getIssuanceList()
  {
    return this.issuanceList;
  }
  public void setIssuanceList(ArrayList<OurBankVO> issuanceList)
  {
    this.issuanceList = issuanceList;
  }
  public String execute()
    throws ApplicationException
  {
    logger.info("Entering Method");
    try
    {
      isSessionAvailable();
      if (this.issuanceVO != null) {
        this.issuanceVO = new IssuanceVO();
      }
    }
    catch (Exception exception)
    {
      throwApplicationException(exception);
    }
    logger.info("Exiting Method");
    return "success";
  }
  public String fetchCustomer()
    throws Exception
  {
    logger.info("Entering Method");

 
    FIRCIssuanceBD bd = null;
    try
    {
      bd = new FIRCIssuanceBD();
      this.customerList = new ArrayList();
      if (this.cusDataVo != null) {
        if ((CommonMethods.isNull(this.cusDataVo.getCifID())) && 
          (CommonMethods.isNull(this.cusDataVo.getBeneficiaryName())))
        {
          logger.info("Inside fetch cusmer 1");
          this.customerList = bd.getCustomerList(this.customerList);
        }
        else
        {
          logger.info("CIF ID IS" + this.cusDataVo.getCifID());
          logger.info("Inside fetch cusmer 2");
          this.customerList = bd.filterCustomer(this.cusDataVo, this.customerList);
        }
      }
    }
    catch (Exception exception)
    {
      throwApplicationException(exception);
    }
    logger.info("Exiting Method");
    return "success";
  }
  public String gotoHome()
    throws Exception
  {
    logger.info("Entering Method");
    try
    {
      if (this.issuanceVO != null)
      {
        logger.info("CIF ID==>" + this.issuanceVO.getCifID());
        this.issuanceVO.setCRN(this.issuanceVO.getCifID());
        this.issuanceVO.setBeneficiaryName(this.issuanceVO.getBeneficiaryName());
        logger.info("chargeVO.setBeneficiaryName==>" + 
          this.issuanceVO.getBeneficiaryName());
      }
    }
    catch (Exception exception)
    {
      throwApplicationException(exception);
    }
    return "success";
  }
  public String customerCifCode()
    throws Exception
  {
    logger.info("Entering Method");
    this.cusDataVo = null;
    try
    {
      this.cusDataVo = new CustomerDataVO();
    }
    catch (Exception exception)
    {
      throwApplicationException(exception);
    }
    logger.info("Exiting Method");
    return "success";
  }
  public String fircDownloadBulk()
  {
    int count = 0;
    String countOfCheckedBox = this.issuanceVO.getChecked();
    logger.info("Count of check box=====>" + countOfCheckedBox);
    String[] temp = countOfCheckedBox.split(",");
    String tempFircVal = temp[0];
    System.out
      .println("Count of check box substringed=====>" + tempFircVal);
    FIRCOurBankBD bd = null;
    ByteArrayOutputStream baos = null;
    BarcodeGenerator printGenerator = null;
    PrintOurBankVO printOurBankVO = null;
    String pdfFileName = "";
    ArrayList masterbyteArrayList = new ArrayList();
    ArrayList trnsactionList = new ArrayList();
    try
    {
      ZipOutputStream zos = new ZipOutputStream(
        this.response.getOutputStream());
      count = Integer.parseInt(tempFircVal);
      logger.info("Count--->" + count);
      bd = new FIRCOurBankBD();
      for (int i = 0; i < count; i++)
      {
        baos = new ByteArrayOutputStream();
        printGenerator = new BarcodeGenerator();
        printOurBankVO = new PrintOurBankVO();
        logger.info("I am inside---" + i);
        String trnsacNo = this.chkList[i];
        logger.info("trnsacNo" + trnsacNo);
        trnsactionList.add(trnsacNo);
        printOurBankVO.setTransactionrefno(trnsacNo);
        printOurBankVO.setInwardType("FIRC");
        printOurBankVO = bd.printOurBankDetails(printOurBankVO);
        printGenerator.writePdf(printOurBankVO, baos);
        pdfFileName = "FIRC" + trnsacNo;
        zos.setMethod(8);
        masterbyteArrayList.add(baos.toByteArray());
        logger.info("masterbyteArrayList size---->" + 
          masterbyteArrayList.size());
      }
      for (int j = 0; j < masterbyteArrayList.size(); j++)
      {
        byte[] buffer = new byte[200000];
        zos.putNextEntry(new ZipEntry("_FIRC_" + trnsactionList.get(j) + 
          ".pdf"));
        buffer = (byte[])masterbyteArrayList.get(j);
        for (int bufcount = 0; bufcount < buffer.length; bufcount++) {
          zos.write(buffer[bufcount]);
        }
        zos.closeEntry();
      }
      zos.finish();
      zos.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return "success";
  }
  
  public String firaPrintActionBulk()
  {
    int count = 0;
    int j = 0;
    String countOfCheckedBox = this.issuanceVO.getChecked();
    logger.info("Count of check box=====>" + countOfCheckedBox);
    String[] temp = countOfCheckedBox.split(",");
    String tempFircVal = temp[0];
    FIRCOurBankBD bd = null;
    FIRCIssuanceBD issueBD = null;
    BarcodeGenerator printGenerator = null;
    PrintOurBankVO printOurBankVO = null;
    HashMap<String, ByteArrayOutputStream> pdfMap = new HashMap();
    ArrayList pdfFileNameArray = new ArrayList();
    ArrayList pdfFileNameArray1 = new ArrayList();
    HashMap<String, String> createMap = new HashMap();
    try
    {
      count = Integer.parseInt(tempFircVal);
      logger.info("Count--->" + count);
      for (String strTemp : this.chkList)
      {
        bd = new FIRCOurBankBD();
        issueBD = new FIRCIssuanceBD();
        printGenerator = new BarcodeGenerator();
        printOurBankVO = new PrintOurBankVO();
        String trnsacNo = strTemp;
        logger.info("trnsacNo---" + trnsacNo);
        createMap = issueBD.getStatus(trnsacNo);
        if ((((String)createMap.get("status")).equalsIgnoreCase("A")) && (((String)createMap.get("inwardType")).equalsIgnoreCase("FIRA")))
        {
          printOurBankVO.setTransactionrefno(trnsacNo);
          printOurBankVO.setInwardType("FIRA");
          printOurBankVO = bd.printOurBankDetails(printOurBankVO);
          String pdfFileName = "FIRA_" + trnsacNo;
          pdfFileNameArray.add(pdfFileName);
          printGenerator.writeFiraPdfBulk(printOurBankVO, 
            pdfFileNameArray);
          j++;
          logger.info("Count Of j===>" + j);
        }
      }
      this.response.setContentType("Content-type: text/zip");
      this.response.setHeader("Content-Disposition", 
        "attachment; filename=FIRCIssuance.zip");

 
 
      ServletOutputStream out = this.response.getOutputStream();
      ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(
        out));
      for (int i = 0; i < j; i++)
      {
        File file = new File(ActionConstants.FILE_LOCATION + 
          pdfFileNameArray.get(i) + ".pdf");
        pdfFileNameArray1.add(pdfFileNameArray.get(i) + ".pdf");
        logger.info("Adding file " + pdfFileNameArray1.get(i));
        zos.putNextEntry(new ZipEntry(pdfFileNameArray1.get(i)
          .toString()));

 
        FileInputStream fis = null;
        try
        {
          fis = new FileInputStream(file);
        }
        catch (FileNotFoundException fnfe)
        {
          zos.write(("ERROR: Could not find file " + file.getName())
            .getBytes());
          zos.closeEntry();
          logger.info("Could not find file " + 
            file.getAbsolutePath());
          continue;
        }
        BufferedInputStream fif = new BufferedInputStream(fis);

 
        int data = 0;
        while ((data = fif.read()) != -1) {
          zos.write(data);
        }
        fif.close();
        zos.closeEntry();
        logger.info("Finished adding file " + file.getName());
      }
      zos.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return "success";
  }
  public String fircDupPrintActionBulk()
  {
    int count = 0;
    String countOfCheckedBox = this.issuanceVO.getChecked();
    logger.info("Count of check box=====>" + countOfCheckedBox);
    String[] temp = countOfCheckedBox.split(",");
    String tempFircVal = temp[0];
    System.out
      .println("Count of check box substringed=====>" + tempFircVal);
    FIRCOurBankBD bd = null;
    ByteArrayOutputStream baos = null;
    BarcodeGenerator printGenerator = null;
    PrintOurBankVO printOurBankVO = null;
    String pdfFileName = "";
    ArrayList masterbyteArrayList = new ArrayList();
    ArrayList trnsactionList = new ArrayList();
    HashMap<String, ByteArrayOutputStream> pdfMap = new HashMap();
    try
    {
      ZipOutputStream zos = new ZipOutputStream(
        this.response.getOutputStream());
      count = Integer.parseInt(tempFircVal);
      logger.info("Count--->" + count);
      bd = new FIRCOurBankBD();
      for (int i = 0; i < count; i++)
      {
        baos = new ByteArrayOutputStream();
        printGenerator = new BarcodeGenerator();
        printOurBankVO = new PrintOurBankVO();
        logger.info("I am inside---" + i);
        String trnsacNo = this.chkList[i];
        logger.info("trnsacNo" + trnsacNo);
        trnsactionList.add(trnsacNo);
        printOurBankVO.setTransactionrefno(trnsacNo);
        printOurBankVO.setInwardType("FIRA");
        printOurBankVO = bd.printOurBankDetails(printOurBankVO);
        printGenerator.writeDupPdf(printOurBankVO, baos);
        pdfFileName = "FIRA" + trnsacNo;
        zos.setMethod(8);
        masterbyteArrayList.add(baos.toByteArray());
        logger.info("masterbyteArrayList size---->" + 
          masterbyteArrayList.size());
      }
      for (int j = 0; j < masterbyteArrayList.size(); j++)
      {
        byte[] buffer = new byte[200000];
        zos.putNextEntry(new ZipEntry("_FIRC_" + trnsactionList.get(j) + 
          ".pdf"));
        buffer = (byte[])masterbyteArrayList.get(j);
        for (int bufcount = 0; bufcount < buffer.length; bufcount++) {
          zos.write(buffer[bufcount]);
        }
        zos.closeEntry();
      }
      zos.finish();
      zos.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return "success";
  }
  public String fircIssuanceSearch()
    throws ApplicationException
  {
    logger.info("--------fircIssuanceSearch123-----------");
    FIRCIssuanceBD bd = null;
    try
    {
      bd = new FIRCIssuanceBD();
      logger.info("Inside try");
      if (this.issuanceVO != null)
      {
        logger.info("Inside try1");
        this.issuanceList = bd.getFircIssuance(this.issuanceVO);
      }
    }
    catch (Exception exception)
    {
      logger.info("--------fircIssuanceSearch------exception-----" + 
        exception);
      throwApplicationException(exception);
    }
    logger.info("Exiting Method");
    return "success";
  }
  public String fircPrintAction()
    throws ApplicationException
  {
    logger.info("---------------fircPrintAction-----------------");
    FIRCOurBankBD bd = null;
    ByteArrayOutputStream baos = null;
    BarcodeGenerator printGenerator = null;
    PrintOurBankVO printOurBankVO = null;
    WordDocGenerator wordGenerator = null;
    try
    {
      bd = new FIRCOurBankBD();
      baos = new ByteArrayOutputStream();
      wordGenerator = new WordDocGenerator();
      printGenerator = new BarcodeGenerator();
      printOurBankVO = new PrintOurBankVO();
      if (this.fircValue != null)
      {
        String[] temp = this.fircValue.split(":");
        String tempFircVal = temp[0];
        printOurBankVO.setTransactionrefno(tempFircVal);
        printOurBankVO.setInwardType("FIRC");
        printOurBankVO = bd.printOurBankDetails(printOurBankVO);

 
 
 
        String wordFileName = "FIRC" + tempFircVal;
        wordGenerator.writeDoc(printOurBankVO, baos, "FIRC", false, false, wordFileName);
        generateWord(this.response, baos, wordFileName);
      }
    }
    catch (Exception exception)
    {
      logger.info("---------------fircPrintAction----------exception-------" + 
        exception);
      throwApplicationException(exception);
    }
    return "success";
  }
  private void generatePdf(HttpServletResponse response, ByteArrayOutputStream baos, String fileName)
    throws IOException
  {
    response.setHeader("Content-Disposition", "attachment; filename=\"" + 
      fileName + ".pdf\"");
    response.setContentType("application/pdf");
    response.setContentLength(baos.size());
    OutputStream outputStream = response.getOutputStream();
    baos.writeTo(outputStream);
    response.getOutputStream().flush();
    response.getOutputStream().close();
  }
  public String fircDupPrintAction()
    throws ApplicationException
  {
    logger.info("-----------fircDupPrintAction----------------------");
    FIRCOurBankBD bd = null;
    ByteArrayOutputStream baos = null;
    WordDocGenerator wordGenerator = null;
    PrintOurBankVO printOurBankVO = null;
    try
    {
      bd = new FIRCOurBankBD();
      baos = new ByteArrayOutputStream();
      wordGenerator = new WordDocGenerator();
      printOurBankVO = new PrintOurBankVO();
      if (this.fircValue != null)
      {
        String[] temp = this.fircValue.split(":");
        String tempFircVal = temp[0];
        printOurBankVO.setTransactionrefno(tempFircVal);
        printOurBankVO.setInwardType("FIRC");
        printOurBankVO = bd.printOurBankDetails(printOurBankVO);

 
 
 
        String wordFileName = "FIRC" + tempFircVal;
        wordGenerator.writeDoc(printOurBankVO, baos, "FIRC", true, false, wordFileName);
        generateWord(this.response, baos, wordFileName);
      }
    }
    catch (Exception exception)
    {
      logger.info("-----------fircDupPrintAction-----------exception-----------" + exception);
      throwApplicationException(exception);
    }
    return "success";
  }
  
  public String fircPrintAdviceAction()
		    throws ApplicationException
		  {
		    System.out.println("----------------fircPrintAdviceAction---------------");
		    FIRCOurBankBD bd = null;
		    BarcodeGenerator printGenerator = null;
		    WordDocGenerator wordGenerator = null;
		    PrintOurBankVO printOurBankVO = null;
		    boolean emailStaus = false;
		    String traget = null;
		    try
		    {
		      bd = new FIRCOurBankBD();
		      printGenerator = new BarcodeGenerator();
		      wordGenerator = new WordDocGenerator();
		      printOurBankVO = new PrintOurBankVO();
		      if (this.fircValue != null)
		      {
		        String[] temp = this.fircValue.split(":");
		        String tempFircVal = temp[0];
		        printOurBankVO.setTransactionrefno(tempFircVal);
		        printOurBankVO.setInwardType("FIRA");
		        printOurBankVO = bd.printOurBankDetails(printOurBankVO);
		        String pdfFileName = "FIRA" + tempFircVal;
		        printGenerator.writeAdvicePdf(printOurBankVO, pdfFileName);

		 
		 
		 
		        emailStaus = bd.sentAdviceData(tempFircVal);
		      }
		      if (emailStaus) {
		        traget = "success";
		      } else {
		        traget = "fail";
		      }
		    }
		    catch (Exception exception)
		    {
		      System.out.println("fircPrintAdviceAction----Exception" + exception);
		      throwApplicationException(exception);
		    }
		    return traget;
		  }
		  public String fircPrintAdviceActionBulk()
		    throws ApplicationException
		  {
		    System.out.println("----------------fircPrintAdviceActionBulk---------------");
		    FIRCOurBankBD bd = null;
		    int count = 0;
		    BarcodeGenerator printGenerator = null;
		    WordDocGenerator wordGenerator = null;
		    PrintOurBankVO printOurBankVO = null;
		    boolean emailStaus = false;
		    String traget = null;
		    ArrayList pdfFileNameArray = new ArrayList();
		    HashMap<String, String> createMap = new HashMap();
		    FIRCIssuanceBD issueBD = null;
		    try
		    {
		      String countOfCheckedBox = this.issuanceVO.getChecked();
		      logger.info("Count of check box=====>" + countOfCheckedBox);
		      String[] temp = countOfCheckedBox.split(",");
		      String tempFircVal = temp[0];
		      count = Integer.parseInt(tempFircVal);
		      logger.info("Count--->" + count);
		      for (String strTemp : this.chkList)
		      {
		        bd = new FIRCOurBankBD();
		        issueBD = new FIRCIssuanceBD();
		        printGenerator = new BarcodeGenerator();
		        wordGenerator = new WordDocGenerator();
		        printOurBankVO = new PrintOurBankVO();
		        String trnsacNo = strTemp;
		        logger.info("trnsacNo---" + trnsacNo);
		        createMap = issueBD.getStatus(trnsacNo);
		        if ((((String)createMap.get("status")).equalsIgnoreCase("A")) && (((String)createMap.get("inwardType")).equalsIgnoreCase("FIRC")))
		        {
		          printOurBankVO.setTransactionrefno(trnsacNo);
		          printOurBankVO.setInwardType("FIRA");
		          printOurBankVO = bd.printOurBankDetails(printOurBankVO);
		          String pdfFileName = "FIRA" + trnsacNo;
		          pdfFileNameArray.add(pdfFileName);
		          printGenerator.writeAdvicePdfBulk(printOurBankVO, 
		            pdfFileNameArray);
		        }
		      }
		      emailStaus = bd.sentAdviceDataBulk(this.issuanceVO.getCRN().trim(), 
		        pdfFileNameArray);
		      if (emailStaus) {
		        traget = "success";
		      } else {
		        traget = "fail";
		      }
		    }
		    catch (Exception exception)
		    {
		      System.out.println("fircPrintAdviceAction----Exception" + exception);
		      throwApplicationException(exception);
		    }
		    return traget;
		  }
		  public String firaPrintAction()
		    throws ApplicationException
		  {
		    logger.info("---------------fircPrintAction-----------------");
		    FIRCOurBankBD bd = null;
		    ByteArrayOutputStream baos = null;
		    BarcodeGenerator printGenerator = null;
		    PrintOurBankVO printOurBankVO = null;
		    try
		    {
		      bd = new FIRCOurBankBD();
		      baos = new ByteArrayOutputStream();
		      printGenerator = new BarcodeGenerator();
		      printOurBankVO = new PrintOurBankVO();
		      if (this.fircValue != null)
		      {
		        String[] temp = this.fircValue.split(":");
		        String tempFircVal = temp[0];
		        printOurBankVO.setTransactionrefno(tempFircVal);
		        printOurBankVO.setInwardType("FIRA");
		        printOurBankVO = bd.printOurBankDetails(printOurBankVO);
		        printGenerator.writeFiraPdf(printOurBankVO, baos);
		        String pdfFileName = "FIRA" + tempFircVal;
		        generatePdf(this.response, baos, pdfFileName);
		      }
		    }
		    catch (Exception exception)
		    {
		      logger.info("---------------fircPrintAction----------exception-------" + 
		        exception);
		      throwApplicationException(exception);
		    }
		    return "success";
		  }
		  public String irmExSearch()
		    throws ApplicationException
		  {
		    logger.info("Entering Method");
		    FIRCIssuanceBD bd = null;
		    try
		    {
		      bd = new FIRCIssuanceBD();
		      if (this.issuanceVO != null) {
		        this.irmExList = bd.getIrmExSearch(this.issuanceVO);
		      }
		    }
		    catch (Exception exception)
		    {
		      throwApplicationException(exception);
		    }
		    logger.info("Exiting Method");
		    return "success";
		  }
		  public String irmAdjSearch()
		    throws ApplicationException
		  {
		    FIRCIssuanceBD bd = null;
		    try
		    {
		      bd = new FIRCIssuanceBD();
		      if (this.issuanceVO != null) {
		        this.irmClList = bd.getIrmClSearch(this.issuanceVO);
		      }
		    }
		    catch (Exception exception)
		    {
		      logger.info("irmAdjSearch--------------------------" + 
		        exception);
		      throwApplicationException(exception);
		    }
		    logger.info("Exiting Method");
		    return "success";
		  }
		  private void generateWord(HttpServletResponse response, ByteArrayOutputStream baos, String fileName)
		  {
		    try
		    {
		      response.setHeader("Content-Disposition", "attachment; filename=\"" + 
		        fileName + ".docx\"");
		      response.setContentType("application/docx");
		      response.setContentLength(baos.size());
		      OutputStream outputStream = response.getOutputStream();
		      baos.writeTo(outputStream);
		      outputStream.flush();
		      outputStream.close();
		    }
		    catch (Exception e)
		    {
		      e.printStackTrace();
		    }
		  }
		  public void setServletResponse(HttpServletResponse response)
		  {
		    this.response = response;
		  }
		  public String irmSearch()
		    throws Exception
		  {
		    logger.info("Entering Method");
		    HttpServletRequest request = ServletActionContext.getRequest();
		    try
		    {
		      isSessionAvailable();
		      String cifNo = DBUtility.getEmptyIfNull(request
		        .getParameter("cifNo"));
		      this.issuanceVO = new IssuanceVO();
		      this.issuanceVO.setCifNo(cifNo);
		    }
		    catch (Exception exception)
		    {
		      throwApplicationException(exception);
		    }
		    logger.info("Exiting Method");
		    return "success";
		  }
		  public String fecthIRMSearch()
		    throws ApplicationException
		  {
		    logger.info("Entering Method");
		    FIRCIssuanceBD bd = null;
		    try
		    {
		      bd = new FIRCIssuanceBD();
		      this.irmList = bd.getIrmSearch(this.issuanceVO);
		    }
		    catch (Exception exception)
		    {
		      exception.printStackTrace();
		    }
		    logger.info("Exiting Method");
		    return "success";
		  }
		  public String ifscSearch()
		    throws Exception
		  {
		    logger.info("Entering Method");
		    HttpServletRequest request = ServletActionContext.getRequest();
		    try
		    {
		      isSessionAvailable();
		      String ifscCode = DBUtility.getEmptyIfNull(request
		        .getParameter("ifscCode"));
		      this.issuanceVO = new IssuanceVO();
		      this.issuanceVO.setIfscCode(ifscCode);
		    }
		    catch (Exception exception)
		    {
		      throwApplicationException(exception);
		    }
		    logger.info("Exiting Method");
		    return "success";
		  }
		  public String fecthIFSCSearch()
		    throws ApplicationException
		  {
		    logger.info("Entering Method");
		    FIRCIssuanceBD bd = null;
		    try
		    {
		      bd = new FIRCIssuanceBD();
		      this.ifscList = bd.fecthIFSCSearch(this.issuanceVO);
		    }
		    catch (Exception exception)
		    {
		      exception.printStackTrace();
		    }
		    logger.info("Exiting Method");
		    return "success";
		  }
		}
