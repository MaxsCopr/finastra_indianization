package in.co.ebrc.action;

import in.co.ebrc.businessdelegate.exception.BusinessException;
import in.co.ebrc.businessdelegate.pricereftomanybill.EbrcCertificateProcess;
import in.co.ebrc.dao.exception.ApplicationException;
import in.co.ebrc.utility.ActionConstants;
import in.co.ebrc.utility.CommonMethods;
import in.co.ebrc.vo.AlertMessagesVO;
import in.co.ebrc.vo.EbrcDataProcessVO;
import in.co.ebrc.vo.EbrcProcessVO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

public class EbrcCustomerAction
  extends InvoiceBaseAction
{
  private static final long serialVersionUID = 1L;
  private static Logger logger = Logger.getLogger(InvoiceCustomerAction.class.getName());
  ArrayList<EbrcProcessVO> tiList = null;
  ArrayList<EbrcDataProcessVO> storeInvData = null;
  private ArrayList<AlertMessagesVO> alertMsgArray = new ArrayList();
  ArrayList<EbrcProcessVO> eventList = null;
  ArrayList<AlertMessagesVO> errorList = null;
  Map<String, String> ebrcStatus;
  Map<String, String> ebrcStatus1;
  private InputStream inputStream;
  private String fileName;
  private long contentLength;
  Map<String, String> status;
  String mode;
  EbrcProcessVO ebrcVO;
  EbrcDataProcessVO ebrcDataVO;
 
  public String getFileName()
  {
    return this.fileName;
  }
 
  public void setFileName(String fileName)
  {
    this.fileName = fileName;
  }
 
  public InputStream getInputStream()
  {
    return this.inputStream;
  }
 
  public void setInputStream(InputStream inputStream)
  {
    this.inputStream = inputStream;
  }
 
  public long getContentLength()
  {
    return this.contentLength;
  }
 
  public void setContentLength(long contentLength)
  {
    this.contentLength = contentLength;
  }
 
  public Map<String, String> getEbrcStatus1()
  {
    return ActionConstants.REC_IND1;
  }
 
  public void setEbrcStatus1(Map<String, String> ebrcStatus1)
  {
    this.ebrcStatus1 = ebrcStatus1;
  }
 
  public Map<String, String> getEbrcStatus()
  {
    return ActionConstants.REC_IND;
  }
 
  public void setEbrcStatus(Map<String, String> ebrcStatus)
  {
    this.ebrcStatus = ebrcStatus;
  }
 
  public Map<String, String> getstatus()
  {
    return ActionConstants.REC;
  }
 
  public void setstatus(Map<String, String> status)
  {
    this.status = status;
  }
 
  public String getMode()
  {
    return this.mode;
  }
 
  public void setMode(String mode)
  {
    this.mode = mode;
  }
 
  public EbrcDataProcessVO getEbrcDataVO()
  {
    return this.ebrcDataVO;
  }
 
  public void setEbrcDataVO(EbrcDataProcessVO ebrcDataVO)
  {
    this.ebrcDataVO = ebrcDataVO;
  }
 
  public EbrcProcessVO getEbrcVO()
  {
    return this.ebrcVO;
  }
 
  public void setEbrcVO(EbrcProcessVO ebrcVO)
  {
    this.ebrcVO = ebrcVO;
  }
 
  public ArrayList<AlertMessagesVO> getErrorList()
  {
    return this.errorList;
  }
 
  public void setErrorList(ArrayList<AlertMessagesVO> errorList)
  {
    this.errorList = errorList;
  }
 
  public ArrayList<EbrcProcessVO> getEventList()
  {
    return this.eventList;
  }
 
  public void setEventList(ArrayList<EbrcProcessVO> eventList)
  {
    this.eventList = eventList;
  }
 
  public ArrayList<EbrcDataProcessVO> getStoreInvData()
  {
    return this.storeInvData;
  }
 
  public void setStoreInvData(ArrayList<EbrcDataProcessVO> storeInvData)
  {
    this.storeInvData = storeInvData;
  }
 
  public ArrayList<EbrcProcessVO> getTiList()
  {
    return this.tiList;
  }
 
  public void setTiList(ArrayList<EbrcProcessVO> tiList)
  {
    this.tiList = tiList;
  }
 
  public String landingPage()
  {
    logger.info("Entering Method");
   
    logger.info("Exiting Method");
    return "success";
  }
 
  public String getEbrcDataValue()
    throws ApplicationException
  {
    HttpServletResponse response = ServletActionContext.getResponse();
    response.setContentType("text/html");
    HttpServletRequest request = ServletActionContext.getRequest();
   
    EbrcCertificateProcess ebrcProcess = null;
    try
    {
      ebrcProcess =
        EbrcCertificateProcess.getBD();
      if (this.ebrcVO == null) {
        this.ebrcVO = new EbrcProcessVO();
      }
      String ebrcStatus = this.ebrcVO.getEbrcStatus();
      String ebrcIecode = this.ebrcVO.getEbrcIECode();
      String ebrcNumber = this.ebrcVO.getBrcNumber();
      String status = this.ebrcVO.getStatus();
      String billRef = this.ebrcVO.getBillRefNumber();
      String eventRef = this.ebrcVO.getEventRefNumber();
      String fromDate = this.ebrcVO.getFromDate();
      String toDate = this.ebrcVO.getToDate();
      this.ebrcVO = ebrcProcess.getDataFromTI(this.ebrcVO);
      this.tiList = this.ebrcVO.getList();
      if (this.ebrcVO != null)
      {
        this.ebrcVO.setEbrcStatus(ebrcStatus);
        this.ebrcVO.setBrcNumber(ebrcNumber);
        this.ebrcVO.setEbrcIECode(ebrcIecode);
        this.ebrcVO.setStatus(status);
        this.ebrcVO.setBillRefNumber(billRef);
        this.ebrcVO.setEventRefNumber(eventRef);
        this.ebrcVO.setFromDate(fromDate);
        this.ebrcVO.setToDate(toDate);
      }
    }
    catch (BusinessException e)
    {
      throwApplicationException(e);
    }
    return "success";
  }
 
  public String fetchEbrcData()
    throws ApplicationException
  {
    EbrcCertificateProcess ebrcProcess = null;
    try
    {
      ebrcProcess =
        EbrcCertificateProcess.getBD();
      if (this.ebrcVO == null) {
        this.ebrcVO = new EbrcProcessVO();
      }
      String ebrcStatus = this.ebrcVO.getEbrcStatus();
      String ebrcIecode = this.ebrcVO.getEbrcIECode();
      String ebrcNumber = this.ebrcVO.getBrcNumber();
      String billRef = this.ebrcVO.getBillRefNumber();
      String eventRef = this.ebrcVO.getEventRefNumber();
      String fromDate = this.ebrcVO.getFromDate();
      String toDate = this.ebrcVO.getToDate();
      this.tiList = ebrcProcess.fetchEbrcValue(this.ebrcVO);
      setEventList(this.tiList);
      String temp = this.ebrcVO.getBrcNo1();
      this.ebrcVO.setBrcNo(temp);
      this.ebrcVO = ebrcProcess.getGridValue(this.ebrcVO);
      if (this.ebrcVO != null)
      {
        this.ebrcVO.setEbrcStatus(ebrcStatus);
        this.ebrcVO.setBrcNumber(ebrcNumber);
        this.ebrcVO.setEbrcIECode(ebrcIecode);
        this.ebrcVO.setBillRefNumber(billRef);
        this.ebrcVO.setEventRefNumber(eventRef);
        this.ebrcVO.setFromDate(fromDate);
        this.ebrcVO.setToDate(toDate);
      }
    }
    catch (BusinessException e)
    {
      throwApplicationException(e);
    }
    return "success";
  }
 
  public String generateIFSCXmlData()
    throws ApplicationException
  {
    EbrcCertificateProcess ebrcProcess = null;
    try
    {
      ebrcProcess =
        EbrcCertificateProcess.getBD();
     
      ebrcProcess.generateIFSCXmlData();
    }
    catch (BusinessException e)
    {
      throwApplicationException(e);
    }
    return "success";
  }
 
  public String generateEBRCXmlData()
    throws ApplicationException, FileNotFoundException
  {
    EbrcCertificateProcess ebrcProcess = null;
    String r = null;
    int val = 0;
    try
    {
      ebrcProcess =
        EbrcCertificateProcess.getBD();
      r = ebrcProcess.xmlGenerate();
     

      String fileLoc = ActionConstants.FileLocationEBRC;
     
      r = fileLoc + r + ".xml";
      logger.info("zip file path" + r);
     
      File fileToDownload = new File(r);
      logger.info("file---" + fileToDownload.getName());
      File isAlreadyExists = new File(r);
      if (isAlreadyExists.exists())
      {
        this.inputStream = new FileInputStream(fileToDownload);
        this.fileName = fileToDownload.getName();
        logger.info("filename" + this.fileName);
        this.contentLength = fileToDownload.length();
        val++;
        logger.info("value--" + val);
      }
    }
    catch (BusinessException e)
    {
      throwApplicationException(e);
    }
    if (val == 1)
    {
      logger.info("inside if");
      addActionError("PLEASE WAIT XML Files Download");
     
      return "success";
    }
    addActionError("Data already downloaded");
    return "error";
  }
 
  public String storeEbrcMatchingData()
    throws ApplicationException
  {
    EbrcCertificateProcess ebrcProcess = null;
    try
    {
      ebrcProcess =
        EbrcCertificateProcess.getBD();
      this.tiList = ebrcProcess.fetchEbrcValue(this.ebrcVO);
      setEventList(this.tiList);
      if (this.ebrcVO == null) {
        this.ebrcVO = new EbrcProcessVO();
      }
      ebrcProcess.storeEbrcData(this.ebrcVO);
      this.ebrcVO = ebrcProcess.getDataFromTI(this.ebrcVO);
      if (this.ebrcVO.getTempStatus() != null) {
        this.ebrcVO.setStatus(this.ebrcVO.getTempStatus());
      }
      this.tiList = this.ebrcVO.getList();
    }
    catch (BusinessException e)
    {
      throwApplicationException(e);
    }
    return "success";
  }
 
  public String validateEbrcData()
    throws ApplicationException
  {
    return "success";
  }
 
  public void setErrorvalues(Object[] arg)
  {
    CommonMethods commonMethods = new CommonMethods();
    AlertMessagesVO altMsg = new AlertMessagesVO();
    altMsg.setErrorId("Error");
    altMsg.setErrorDesc("General");
    altMsg.setErrorCode(commonMethods.getEmptyIfNull(arg[3]));
    altMsg.setErrorDetails(commonMethods.getEmptyIfNull(arg[2]));
    altMsg.setErrorMsg("");
    this.alertMsgArray.add(altMsg);
  }
}
