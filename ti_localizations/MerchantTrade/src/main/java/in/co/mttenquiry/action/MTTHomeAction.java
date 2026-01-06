package in.co.mttenquiry.action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import in.co.mttenquiry.businessdelegate.MTTHomeBD;
import in.co.mttenquiry.dao.exception.ApplicationException;
import in.co.mttenquiry.utility.CommonMethods;
import in.co.mttenquiry.utility.DBConnectionUtility;
import in.co.mttenquiry.utility.LoggableStatement;
import in.co.mttenquiry.vo.MTTDataVO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.struts2.ServletActionContext;

public class MTTHomeAction
  extends ActionSupport
{
  private String url;
  private static final long serialVersionUID = 1L;
 
  public String getUrl()
  {
    return this.url;
  }
 
  public void setUrl(String url)
  {
    this.url = url;
  }
 
  public ArrayList<MTTDataVO> getMttStatus()
  {
    return this.mttStatus;
  }
 
  public void setMttStatus(ArrayList<MTTDataVO> mttStatus)
  {
    this.mttStatus = mttStatus;
  }
 
  MTTDataVO mttVO = null;
  ArrayList<MTTDataVO> tiList = null;
  ArrayList<MTTDataVO> mttStatus = null;
  ArrayList<MTTDataVO> statusList = null;
  ArrayList<MTTDataVO> pendingtiList = null;
  String[] chkList = null;
  String check = null;
  String remarks = null;
  ArrayList<MTTDataVO> pendingAddtiList = null;
  String[] chkList1 = null;
  String check1 = null;
  String remarks1 = null;
  String splitCheck = null;
  String splitRemarks = null;
  String splitCheckerRemarks = null;
  String[] splitChkList = null;
  public String mttListNumberToCheck;
  public String mttListNumber11;
  public String mttListTransAmt11;
  public String mttListNumber12;
  public String mttListTransAmt12;
  public String mttListNumber13;
  public String mttListTransAmt13;
  public String mttListNumber14;
  public String mttListTransAmt14;
  public String mttListNumber15;
  public String mttListTransAmt15;
  public String mttListNumber16;
  public String mttListTransAmt16;
  public String mttListNumber17;
  public String mttListTransAmt17;
  public String mttListNumber18;
  public String mttListTransAmt18;
  public String mttListNumber19;
  public String mttListTransAmt19;
  public String mttListNumber20;
  public String mttListTransAmt20;
 
  public String[] getSplitChkList()
  {
    return this.splitChkList;
  }
 
  public void setSplitChkList(String[] splitChkList)
  {
    this.splitChkList = splitChkList;
  }
 
  public String getSplitRemarks()
  {
    return this.splitRemarks;
  }
 
  public void setSplitRemarks(String splitRemarks)
  {
    this.splitRemarks = splitRemarks;
  }
 
  public String getSplitCheckerRemarks()
  {
    return this.splitCheckerRemarks;
  }
 
  public void setSplitCheckerRemarks(String splitCheckerRemarks)
  {
    this.splitCheckerRemarks = splitCheckerRemarks;
  }
 
  public String getSplitCheck()
  {
    return this.splitCheck;
  }
 
  public void setSplitCheck(String splitCheck)
  {
    this.splitCheck = splitCheck;
  }
 
  public String getMttListNumberToCheck()
  {
    return this.mttListNumberToCheck;
  }
 
  public void setMttListNumberToCheck(String mttListNumberToCheck)
  {
    this.mttListNumberToCheck = mttListNumberToCheck;
  }
 
  String fileUtilFileName = null;
  String fileUtilFromDate = null;
  String fileUtilToDate = null;
  String mttNumber = null;
  String custId = null;
  String brnCode = null;
  private InputStream inputStream;
  private String fileName;
  private long contentLength;
 
  public InputStream getInputStream()
  {
    return this.inputStream;
  }
 
  public void setInputStream(InputStream inputStream)
  {
    this.inputStream = inputStream;
  }
 
  public String getFileName()
  {
    return this.fileName;
  }
 
  public void setFileName(String fileName)
  {
    this.fileName = fileName;
  }
 
  public long getContentLength()
  {
    return this.contentLength;
  }
 
  public void setContentLength(long contentLength)
  {
    this.contentLength = contentLength;
  }
 
  public String getMttListNumber11()
  {
    return this.mttListNumber11;
  }
 
  public void setMttListNumber11(String mttListNumber11)
  {
    this.mttListNumber11 = mttListNumber11;
  }
 
  public String getMttListTransAmt11()
  {
    return this.mttListTransAmt11;
  }
 
  public void setMttListTransAmt11(String mttListTransAmt11)
  {
    this.mttListTransAmt11 = mttListTransAmt11;
  }
 
  public String getMttListNumber12()
  {
    return this.mttListNumber12;
  }
 
  public void setMttListNumber12(String mttListNumber12)
  {
    this.mttListNumber12 = mttListNumber12;
  }
 
  public String getMttListTransAmt12()
  {
    return this.mttListTransAmt12;
  }
 
  public void setMttListTransAmt12(String mttListTransAmt12)
  {
    this.mttListTransAmt12 = mttListTransAmt12;
  }
 
  public String getMttListNumber13()
  {
    return this.mttListNumber13;
  }
 
  public void setMttListNumber13(String mttListNumber13)
  {
    this.mttListNumber13 = mttListNumber13;
  }
 
  public String getMttListTransAmt13()
  {
    return this.mttListTransAmt13;
  }
 
  public void setMttListTransAmt13(String mttListTransAmt13)
  {
    this.mttListTransAmt13 = mttListTransAmt13;
  }
 
  public String getMttListNumber14()
  {
    return this.mttListNumber14;
  }
 
  public void setMttListNumber14(String mttListNumber14)
  {
    this.mttListNumber14 = mttListNumber14;
  }
 
  public String getMttListTransAmt14()
  {
    return this.mttListTransAmt14;
  }
 
  public void setMttListTransAmt14(String mttListTransAmt14)
  {
    this.mttListTransAmt14 = mttListTransAmt14;
  }
 
  public String getMttListNumber15()
  {
    return this.mttListNumber15;
  }
 
  public void setMttListNumber15(String mttListNumber15)
  {
    this.mttListNumber15 = mttListNumber15;
  }
 
  public String getMttListTransAmt15()
  {
    return this.mttListTransAmt15;
  }
 
  public void setMttListTransAmt15(String mttListTransAmt15)
  {
    this.mttListTransAmt15 = mttListTransAmt15;
  }
 
  public String getMttListNumber16()
  {
    return this.mttListNumber16;
  }
 
  public void setMttListNumber16(String mttListNumber16)
  {
    this.mttListNumber16 = mttListNumber16;
  }
 
  public String getMttListTransAmt16()
  {
    return this.mttListTransAmt16;
  }
 
  public void setMttListTransAmt16(String mttListTransAmt16)
  {
    this.mttListTransAmt16 = mttListTransAmt16;
  }
 
  public String getMttListNumber17()
  {
    return this.mttListNumber17;
  }
 
  public void setMttListNumber17(String mttListNumber17)
  {
    this.mttListNumber17 = mttListNumber17;
  }
 
  public String getMttListTransAmt17()
  {
    return this.mttListTransAmt17;
  }
 
  public void setMttListTransAmt17(String mttListTransAmt17)
  {
    this.mttListTransAmt17 = mttListTransAmt17;
  }
 
  public String getMttListNumber18()
  {
    return this.mttListNumber18;
  }
 
  public void setMttListNumber18(String mttListNumber18)
  {
    this.mttListNumber18 = mttListNumber18;
  }
 
  public String getMttListTransAmt18()
  {
    return this.mttListTransAmt18;
  }
 
  public void setMttListTransAmt18(String mttListTransAmt18)
  {
    this.mttListTransAmt18 = mttListTransAmt18;
  }
 
  public String getMttListNumber19()
  {
    return this.mttListNumber19;
  }
 
  public void setMttListNumber19(String mttListNumber19)
  {
    this.mttListNumber19 = mttListNumber19;
  }
 
  public String getMttListTransAmt19()
  {
    return this.mttListTransAmt19;
  }
 
  public void setMttListTransAmt19(String mttListTransAmt19)
  {
    this.mttListTransAmt19 = mttListTransAmt19;
  }
 
  public String getMttListNumber20()
  {
    return this.mttListNumber20;
  }
 
  public void setMttListNumber20(String mttListNumber20)
  {
    this.mttListNumber20 = mttListNumber20;
  }
 
  public String getMttListTransAmt20()
  {
    return this.mttListTransAmt20;
  }
 
  public void setMttListTransAmt20(String mttListTransAmt20)
  {
    this.mttListTransAmt20 = mttListTransAmt20;
  }
 
  public String getFileUtilFileName()
  {
    return this.fileUtilFileName;
  }
 
  public void setFileUtilFileName(String fileUtilFileName)
  {
    this.fileUtilFileName = fileUtilFileName;
  }
 
  public String getFileUtilFromDate()
  {
    return this.fileUtilFromDate;
  }
 
  public void setFileUtilFromDate(String fileUtilFromDate)
  {
    this.fileUtilFromDate = fileUtilFromDate;
  }
 
  public String getFileUtilToDate()
  {
    return this.fileUtilToDate;
  }
 
  public void setFileUtilToDate(String fileUtilToDate)
  {
    this.fileUtilToDate = fileUtilToDate;
  }
 
  public String getMttNumber()
  {
    return this.mttNumber;
  }
 
  public void setMttNumber(String mttNumber)
  {
    this.mttNumber = mttNumber;
  }
 
  public String getCustId()
  {
    return this.custId;
  }
 
  public void setCustId(String custId)
  {
    this.custId = custId;
  }
 
  public String getBrnCode()
  {
    return this.brnCode;
  }
 
  public void setBrnCode(String brnCode)
  {
    this.brnCode = brnCode;
  }
 
  public ArrayList<MTTDataVO> getPendingAddtiList()
  {
    return this.pendingAddtiList;
  }
 
  public void setPendingAddtiList(ArrayList<MTTDataVO> pendingAddtiList)
  {
    this.pendingAddtiList = pendingAddtiList;
  }
 
  public String[] getChkList1()
  {
    return this.chkList1;
  }
 
  public void setChkList1(String[] chkList1)
  {
    this.chkList1 = chkList1;
  }
 
  public String getCheck1()
  {
    return this.check1;
  }
 
  public void setCheck1(String check1)
  {
    this.check1 = check1;
  }
 
  public String getRemarks1()
  {
    return this.remarks1;
  }
 
  public void setRemarks1(String remarks1)
  {
    this.remarks1 = remarks1;
  }
 
  public String[] getChkList()
  {
    return this.chkList;
  }
 
  public void setChkList(String[] chkList)
  {
    this.chkList = chkList;
  }
 
  public String getCheck()
  {
    return this.check;
  }
 
  public void setCheck(String check)
  {
    this.check = check;
  }
 
  public String getRemarks()
  {
    return this.remarks;
  }
 
  public void setRemarks(String remarks)
  {
    this.remarks = remarks;
  }
 
  public ArrayList<MTTDataVO> getPendingtiList()
  {
    return this.pendingtiList;
  }
 
  public void setPendingtiList(ArrayList<MTTDataVO> pendingtiList)
  {
    this.pendingtiList = pendingtiList;
  }
 
  public ArrayList<MTTDataVO> getTiList()
  {
    return this.tiList;
  }
 
  public void setTiList(ArrayList<MTTDataVO> tiList)
  {
    this.tiList = tiList;
  }
 
  public ArrayList<MTTDataVO> getStatusList()
  {
    return this.statusList;
  }
 
  public void setStatusList(ArrayList<MTTDataVO> statusList)
  {
    this.statusList = statusList;
  }
 
  public MTTDataVO getMttVO()
  {
    return this.mttVO;
  }
 
  public void setMttVO(MTTDataVO mttVO)
  {
    this.mttVO = mttVO;
  }
 
  private static Logger logger = LogManager.getLogger(MTTHomeAction.class
    .getName());
 
  public String homelandingPage()
    throws ApplicationException
  {
    logger.info("Entering Method");
    String sessionUserName = "";
    String target = null;
    int count = 1;
    try
    {
      HttpSession session = ServletActionContext.getRequest().getSession();
     
      HttpServletRequest request = (HttpServletRequest)ActionContext.getContext().get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
     

      sessionUserName = (String)session.getAttribute("loginedUserName");
      logger.info("loginedUserName------------------>" + sessionUserName);
      if (sessionUserName == null)
      {
        sessionUserName = request.getRemoteUser();
       

        logger.info("getRemoteUser------------------>" + sessionUserName);
        if (sessionUserName == null)
        {
          Connection globalConnection = null;
          globalConnection = DBConnectionUtility.getGlobalConnection();
         
          sessionUserName = request.getRequestedSessionId();
          logger.info("sessionUserName------------------>" + sessionUserName);
         
          String get_User_ID =
            "SELECT SCT.USERNAME AS USER_ID FROM TIGLOBAL.CENTRAL_SESSION_DETAILS SCT,TIGLOBAL.LOCAL_SESSION_DETAILS LOC  WHERE SCT.CENTRAL_ID=LOC.CENTRAL_ID AND SCT.ENDED  IS NULL AND LOC.LOCAL_ID= ? ";
         

          LoggableStatement lst = new LoggableStatement(globalConnection, get_User_ID);
          lst.setString(1, sessionUserName);
          logger.info("Getting Session Value Query------------" +
            lst.getQueryString());
         
          ResultSet rst = lst.executeQuery();
          while (rst.next())
          {
            sessionUserName = rst.getString("USER_ID");
            logger.info("Getting Session Value Query-- user id value----------" + sessionUserName);
          }
          session.setAttribute("loginedUserName", sessionUserName);
          session.setAttribute("loginedUserId", sessionUserName);
          DBConnectionUtility.surrenderDB(globalConnection, lst, rst);
          logger.info("userName-----------" + sessionUserName);
        }
        session.setAttribute("loginedUserName", sessionUserName);
        session.setAttribute("loginedUserId", sessionUserName);
      }
      session.setAttribute("loginedUserName", sessionUserName);
      session.setAttribute("loginedUserId", sessionUserName);
      if (count > 0) {
        session.setAttribute("accessFlg", "true");
      } else {
        session.setAttribute("accessFlg", "false");
      }
      logger.info("target-------------" + target);
    }
    catch (Exception e)
    {
      logger.error("Exception In makercheckerlandingPage", e);
      e.printStackTrace();
    }
    logger.info("Exiting Method");
    return "success";
  }
 
  public String makercheckerlandingPage()
    throws ApplicationException
  {
    logger.info("Entering Method");
    String sessionUserName = "";
    String target = null;
    int count = 1;
    MTTHomeBD bd = null;
    try
    {
      this.mttVO = new MTTDataVO();
      HttpSession session = ServletActionContext.getRequest().getSession();
     
      HttpServletRequest request = (HttpServletRequest)ActionContext.getContext().get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
     

      sessionUserName = (String)session.getAttribute("loginedUserName");
      logger.info("loginedUserName------------------>" + sessionUserName);
      if (sessionUserName == null)
      {
        sessionUserName = request.getRemoteUser();
       

        logger.info("getRemoteUser------------------>" + sessionUserName);
        if (sessionUserName == null)
        {
          Connection globalConnection = null;
          globalConnection = DBConnectionUtility.getGlobalConnection();
         
          sessionUserName = request.getRequestedSessionId();
          logger.info("sessionUserName------------------>" + sessionUserName);
         
          String get_User_ID =
            "SELECT SCT.USERNAME AS USER_ID FROM TIGLOBAL.CENTRAL_SESSION_DETAILS SCT,TIGLOBAL.LOCAL_SESSION_DETAILS LOC  WHERE SCT.CENTRAL_ID=LOC.CENTRAL_ID AND SCT.ENDED  IS NULL AND LOC.LOCAL_ID= ? ";
         

          LoggableStatement lst = new LoggableStatement(globalConnection, get_User_ID);
          lst.setString(1, sessionUserName);
          logger.info("Getting Session Value Query------------" +
            lst.getQueryString());
         
          ResultSet rst = lst.executeQuery();
          while (rst.next())
          {
            sessionUserName = rst.getString("USER_ID");
            logger.info("Getting Session Value Query-- user id value----------" + sessionUserName);
          }
          session.setAttribute("loginedUserName", sessionUserName);
          session.setAttribute("loginedUserId", sessionUserName);
          DBConnectionUtility.surrenderDB(globalConnection, lst, rst);
          logger.info("userName-----------" + sessionUserName);
        }
        session.setAttribute("loginedUserName", sessionUserName);
        session.setAttribute("loginedUserId", sessionUserName);
      }
      session.setAttribute("loginedUserName", sessionUserName);
      session.setAttribute("loginedUserId", sessionUserName);
     
      bd = new MTTHomeBD();
     
      logger.info("sessionUserName--->" + sessionUserName);
     
      this.mttVO.setSessionUserName(sessionUserName);
     
      this.mttVO.setPageType("MTTAMEND");
     
      count = bd.checkLoginedUserType(this.mttVO);
     
      logger.info("Count Session Name for Checker Eligiblity");
      if (count > 0) {
        target = "success";
      } else {
        target = "fail";
      }
      logger.info("target-------------" + target);
    }
    catch (Exception e)
    {
      logger.error("Exception In makercheckerlandingPage", e);
      e.printStackTrace();
    }
    logger.info("Exiting Method");
    return target;
  }
 
  public String statusMakerLandingPage()
    throws ApplicationException
  {
    logger.info("Entering Method");
    if (this.mttVO != null) {
      this.mttVO = new MTTDataVO();
    }
    logger.info("Exiting Method");
    return "success";
  }
 
  public String numberMakerLandingPage()
    throws ApplicationException
  {
    logger.info("Entering Method");
    if (this.mttVO != null) {
      this.mttVO = new MTTDataVO();
    }
    logger.info("Exiting Method");
    return "success";
  }
 
  public String mttNumberSplitMakerLandingPage()
    throws ApplicationException
  {
    logger.info("Entering Method");
    String sessionUserName = "";
    String target = null;
    int count = 1;
    MTTHomeBD bd = null;
    try
    {
      this.mttVO = new MTTDataVO();
      HttpSession session = ServletActionContext.getRequest().getSession();
     
      HttpServletRequest request = (HttpServletRequest)ActionContext.getContext().get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
     

      sessionUserName = (String)session.getAttribute("loginedUserName");
      logger.info("loginedUserName------------------>" + sessionUserName);
      if (sessionUserName == null)
      {
        sessionUserName = request.getRemoteUser();
       

        logger.info("getRemoteUser------------------>" + sessionUserName);
        if (sessionUserName == null)
        {
          Connection globalConnection = null;
          globalConnection = DBConnectionUtility.getGlobalConnection();
         
          sessionUserName = request.getRequestedSessionId();
          logger.info("sessionUserName------------------>" + sessionUserName);
         
          String get_User_ID =
            "SELECT SCT.USERNAME AS USER_ID FROM TIGLOBAL.CENTRAL_SESSION_DETAILS SCT,TIGLOBAL.LOCAL_SESSION_DETAILS LOC  WHERE SCT.CENTRAL_ID=LOC.CENTRAL_ID AND SCT.ENDED  IS NULL AND LOC.LOCAL_ID= ? ";
         

          LoggableStatement lst = new LoggableStatement(globalConnection, get_User_ID);
          lst.setString(1, sessionUserName);
          logger.info("Getting Session Value Query------------" +
            lst.getQueryString());
         
          ResultSet rst = lst.executeQuery();
          while (rst.next())
          {
            sessionUserName = rst.getString("USER_ID");
            logger.info("Getting Session Value Query-- user id value----------" + sessionUserName);
          }
          session.setAttribute("loginedUserName", sessionUserName);
          session.setAttribute("loginedUserId", sessionUserName);
          DBConnectionUtility.surrenderDB(globalConnection, lst, rst);
          logger.info("userName-----------" + sessionUserName);
        }
        session.setAttribute("loginedUserName", sessionUserName);
        session.setAttribute("loginedUserId", sessionUserName);
      }
      session.setAttribute("loginedUserName", sessionUserName);
      session.setAttribute("loginedUserId", sessionUserName);
     
      bd = new MTTHomeBD();
     
      logger.info("sessionUserName--->" + sessionUserName);
     
      this.mttVO.setSessionUserName(sessionUserName);
     
      this.mttVO.setPageType("MTTSPLIT");
     
      count = bd.checkLoginedUserType(this.mttVO);
     
      logger.info("Count Session Name for Checker Eligiblity");
      if (count > 0) {
        target = "success";
      } else {
        target = "fail";
      }
      logger.info("target-------------" + target);
    }
    catch (Exception e)
    {
      logger.error("Exception In makercheckerlandingPage", e);
      e.printStackTrace();
    }
    return target;
  }
 
  public String statusCheckerLandingPage()
    throws ApplicationException
  {
    logger.info("Entering Method");
    if (this.mttVO != null) {
      this.mttVO = new MTTDataVO();
    }
    logger.info("Exiting Method");
    return "success";
  }
 
  public String numberCheckerLandingPage()
    throws ApplicationException
  {
    logger.info("Entering Method");
    if (this.mttVO != null) {
      this.mttVO = new MTTDataVO();
    }
    logger.info("Exiting Method");
    return "success";
  }
 
  public String mttNumberSplitCheckerLandingPage()
    throws ApplicationException
  {
    logger.info("Entering Method");
    String sessionUserName = "";
    String target = null;
    int count = 1;
    MTTHomeBD bd = null;
    try
    {
      this.mttVO = new MTTDataVO();
      HttpSession session = ServletActionContext.getRequest().getSession();
     
      HttpServletRequest request = (HttpServletRequest)ActionContext.getContext().get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
     

      sessionUserName = (String)session.getAttribute("loginedUserName");
      logger.info("loginedUserName------------------>" + sessionUserName);
      if (sessionUserName == null)
      {
        sessionUserName = request.getRemoteUser();
       

        logger.info("getRemoteUser------------------>" + sessionUserName);
        if (sessionUserName == null)
        {
          Connection globalConnection = null;
          globalConnection = DBConnectionUtility.getGlobalConnection();
         
          sessionUserName = request.getRequestedSessionId();
          logger.info("sessionUserName------------------>" + sessionUserName);
         
          String get_User_ID =
            "SELECT SCT.USERNAME AS USER_ID FROM TIGLOBAL.CENTRAL_SESSION_DETAILS SCT,TIGLOBAL.LOCAL_SESSION_DETAILS LOC  WHERE SCT.CENTRAL_ID=LOC.CENTRAL_ID AND SCT.ENDED  IS NULL AND LOC.LOCAL_ID= ? ";
         

          LoggableStatement lst = new LoggableStatement(globalConnection, get_User_ID);
          lst.setString(1, sessionUserName);
          logger.info("Getting Session Value Query------------" +
            lst.getQueryString());
         
          ResultSet rst = lst.executeQuery();
          while (rst.next())
          {
            sessionUserName = rst.getString("USER_ID");
            logger.info("Getting Session Value Query-- user id value----------" + sessionUserName);
          }
          session.setAttribute("loginedUserName", sessionUserName);
          session.setAttribute("loginedUserId", sessionUserName);
          DBConnectionUtility.surrenderDB(globalConnection, lst, rst);
          logger.info("userName-----------" + sessionUserName);
        }
        session.setAttribute("loginedUserName", sessionUserName);
        session.setAttribute("loginedUserId", sessionUserName);
      }
      session.setAttribute("loginedUserName", sessionUserName);
      session.setAttribute("loginedUserId", sessionUserName);
     
      bd = new MTTHomeBD();
     
      logger.info("sessionUserName--->" + sessionUserName);
     
      this.mttVO.setSessionUserName(sessionUserName);
     
      this.mttVO.setPageType("MTTSPLIT");
     
      count = bd.checkLoginedUserType(this.mttVO);
     
      logger.info("Count Session Name for Checker Eligiblity");
      if (count > 0) {
        target = "success";
      } else {
        target = "fail";
      }
      logger.info("target-------------" + target);
    }
    catch (Exception e)
    {
      logger.error("Exception In makercheckerlandingPage", e);
      e.printStackTrace();
    }
    logger.info("Exiting Method");
    return target;
  }
 
  public String enquirylandingPage()
    throws ApplicationException
  {
    logger.info("Entering Method");
    MTTHomeBD bd = null;
    try
    {
      if (this.mttVO != null) {
        this.mttVO = new MTTDataVO();
      }
      bd = new MTTHomeBD();
      bd.setDate();
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return "success";
  }
 
  public String mttProcessStaginglandingPage()
    throws ApplicationException
  {
    logger.info("Entering Method");
    String sessionUserName = "";
    String target = null;
    int count = 1;
    MTTHomeBD bd = null;
    try
    {
      this.mttVO = new MTTDataVO();
      HttpSession session = ServletActionContext.getRequest().getSession();
     
      HttpServletRequest request = (HttpServletRequest)ActionContext.getContext().get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
     

      sessionUserName = (String)session.getAttribute("loginedUserName");
      logger.info("loginedUserName------------------>" + sessionUserName);
      if (sessionUserName == null)
      {
        sessionUserName = request.getRemoteUser();
       

        logger.info("getRemoteUser------------------>" + sessionUserName);
        if (sessionUserName == null)
        {
          Connection globalConnection = null;
          globalConnection = DBConnectionUtility.getGlobalConnection();
         
          sessionUserName = request.getRequestedSessionId();
          logger.info("sessionUserName------------------>" + sessionUserName);
         
          String get_User_ID =
            "SELECT SCT.USERNAME AS USER_ID FROM TIGLOBAL.CENTRAL_SESSION_DETAILS SCT,TIGLOBAL.LOCAL_SESSION_DETAILS LOC  WHERE SCT.CENTRAL_ID=LOC.CENTRAL_ID AND SCT.ENDED  IS NULL AND LOC.LOCAL_ID= ? ";
         

          LoggableStatement lst = new LoggableStatement(globalConnection, get_User_ID);
          lst.setString(1, sessionUserName);
          logger.info("Getting Session Value Query------------" +
            lst.getQueryString());
         
          ResultSet rst = lst.executeQuery();
          while (rst.next())
          {
            sessionUserName = rst.getString("USER_ID");
            logger.info("Getting Session Value Query-- user id value----------" + sessionUserName);
          }
          session.setAttribute("loginedUserName", sessionUserName);
          session.setAttribute("loginedUserId", sessionUserName);
          DBConnectionUtility.surrenderDB(globalConnection, lst, rst);
          logger.info("userName-----------" + sessionUserName);
        }
        session.setAttribute("loginedUserName", sessionUserName);
        session.setAttribute("loginedUserId", sessionUserName);
      }
      session.setAttribute("loginedUserName", sessionUserName);
      session.setAttribute("loginedUserId", sessionUserName);
     
      bd = new MTTHomeBD();
     
      logger.info("sessionUserName--->" + sessionUserName);
     
      this.mttVO.setSessionUserName(sessionUserName);
     
      this.mttVO.setPageType("MTTSPLIT");
     
      count = bd.checkLoginedUserType(this.mttVO);
     
      logger.info("Count Session Name for Checker Eligiblity");
      if (count > 0) {
        target = "success";
      } else {
        target = "fail";
      }
      logger.info("target-------------" + target);
    }
    catch (Exception e)
    {
      logger.error("Exception In makercheckerlandingPage", e);
      e.printStackTrace();
    }
    logger.info("Exiting Method");
    return target;
  }
 
  public String mttProcessReportlandingPage()
    throws ApplicationException
  {
    logger.info("Entering Method");
    String sessionUserName = "";
    String target = null;
    int count = 1;
    MTTHomeBD bd = null;
    try
    {
      this.mttVO = new MTTDataVO();
      HttpSession session = ServletActionContext.getRequest().getSession();
     
      HttpServletRequest request = (HttpServletRequest)ActionContext.getContext().get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
     

      sessionUserName = (String)session.getAttribute("loginedUserName");
      logger.info("loginedUserName------------------>" + sessionUserName);
      if (sessionUserName == null)
      {
        sessionUserName = request.getRemoteUser();
       

        logger.info("getRemoteUser------------------>" + sessionUserName);
        if (sessionUserName == null)
        {
          Connection globalConnection = null;
          globalConnection = DBConnectionUtility.getGlobalConnection();
         
          sessionUserName = request.getRequestedSessionId();
          logger.info("sessionUserName------------------>" + sessionUserName);
         
          String get_User_ID =
            "SELECT SCT.USERNAME AS USER_ID FROM TIGLOBAL.CENTRAL_SESSION_DETAILS SCT,TIGLOBAL.LOCAL_SESSION_DETAILS LOC  WHERE SCT.CENTRAL_ID=LOC.CENTRAL_ID AND SCT.ENDED  IS NULL AND LOC.LOCAL_ID= ? ";
         

          LoggableStatement lst = new LoggableStatement(globalConnection, get_User_ID);
          lst.setString(1, sessionUserName);
          logger.info("Getting Session Value Query------------" +
            lst.getQueryString());
         
          ResultSet rst = lst.executeQuery();
          while (rst.next())
          {
            sessionUserName = rst.getString("USER_ID");
            logger.info("Getting Session Value Query-- user id value----------" + sessionUserName);
          }
          session.setAttribute("loginedUserName", sessionUserName);
          session.setAttribute("loginedUserId", sessionUserName);
          DBConnectionUtility.surrenderDB(globalConnection, lst, rst);
          logger.info("userName-----------" + sessionUserName);
        }
        session.setAttribute("loginedUserName", sessionUserName);
        session.setAttribute("loginedUserId", sessionUserName);
      }
      session.setAttribute("loginedUserName", sessionUserName);
      session.setAttribute("loginedUserId", sessionUserName);
     
      bd = new MTTHomeBD();
     
      logger.info("sessionUserName--->" + sessionUserName);
     
      this.mttVO.setSessionUserName(sessionUserName);
     
      this.mttVO.setPageType("MTTREPORTS");
     
      count = bd.checkLoginedUserType(this.mttVO);
     
      logger.info("Count Session Name for Checker Eligiblity");
      if (count > 0) {
        target = "success";
      } else {
        target = "fail";
      }
      logger.info("target-------------" + target);
    }
    catch (Exception e)
    {
      logger.error("Exception In makercheckerlandingPage", e);
      e.printStackTrace();
    }
    logger.info("Exiting Method");
    return target;
  }
 
  public String fetchTIData()
    throws ApplicationException
  {
    logger.info("Entering Method");
    MTTHomeBD bd = null;
    try
    {
      if (this.mttVO != null)
      {
        bd = new MTTHomeBD();
        this.tiList = bd.getDataFromTI(this.mttVO);
        if ((this.tiList != null) &&
          (this.tiList.size() > 0))
        {
          this.mttVO.setMttListMasterRef(((MTTDataVO)this.tiList.get(0)).getMasRefNo());
          this.mttVO.setListEventeRefNo(((MTTDataVO)this.tiList.get(0)).getListEventeRefNo());
          this.mttVO.setMttListNumber(((MTTDataVO)this.tiList.get(0)).getMttListNumber());
          this.mttVO.setMttListStatus(((MTTDataVO)this.tiList.get(0)).getMttListStatus());
        }
      }
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return "success";
  }
 
  public String fetchStatusForMTT()
    throws ApplicationException
  {
    logger.info("Entering Method");
    MTTHomeBD bd = null;
    String mttNumberCnt = "";
    try
    {
      if (this.mttVO != null)
      {
        bd = new MTTHomeBD();
        mttNumberCnt = bd.fetchStatusForMTT(this.mttVO);
        if (Integer.parseInt(mttNumberCnt.toString()) > 0)
        {
          String mttNumberStatusCnt = "";
          mttNumberStatusCnt = bd.checkInProgressCntForMTT(this.mttVO);
          if (Integer.parseInt(mttNumberStatusCnt.toString()) > 0) {
            addActionError("Status Change Already in Process for this number");
          } else {
            this.mttVO.setIsButtonVisible("true");
          }
        }
        else
        {
          this.mttVO.setIsButtonVisible("false");
          addActionError("Invalid MTT Number");
        }
      }
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return "success";
  }
 
  public String changeStatusForMTT()
    throws ApplicationException
  {
    logger.info("Entering Method");
    MTTHomeBD bd = null;
    try
    {
      if (this.mttVO != null)
      {
        bd = new MTTHomeBD();
       
        int updateCnt = bd.changeStatusForMTT(this.mttVO);
        logger.info("updateCnt After ChangeStatus" + updateCnt);
        this.mttVO.setIsButtonVisible("false");
        this.mttVO.setMttStatusNumber("");
        this.mttVO.setMttCurStatus("");
      }
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return "success";
  }
 
  public String fetchStatusCheckerData()
    throws ApplicationException
  {
    logger.info("Entering Method");
    MTTHomeBD bd = null;
    try
    {
      if (this.mttVO != null)
      {
        bd = new MTTHomeBD();
        this.pendingtiList = bd.getPendingDataForChecker(this.mttVO);
        if ((this.pendingtiList != null) &&
          (this.pendingtiList.size() > 0))
        {
          this.mttVO.setMttListKeyId(((MTTDataVO)this.pendingtiList.get(0)).getMttListKeyId());
          this.mttVO.setMttListMTTNumber(((MTTDataVO)this.pendingtiList.get(0)).getMttListMTTNumber());
          this.mttVO.setMttListCurrentStatus(((MTTDataVO)this.pendingtiList.get(0)).getMttListCurrentStatus());
          this.mttVO.setMttListMakerUserId(((MTTDataVO)this.pendingtiList.get(0)).getMttListMakerUserId());
          this.mttVO.setMttListMakertmstmp(((MTTDataVO)this.pendingtiList.get(0)).getMttListMakertmstmp());
        }
      }
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return "success";
  }
 
  public String updateStatusCheckerData()
    throws ApplicationException
  {
    logger.info("Entering Method");
    MTTHomeBD bd = null;
    try
    {
      bd = new MTTHomeBD();
      if (this.chkList != null) {
        bd.updateStatus(this.chkList, this.check, this.remarks);
      }
      if (this.mttVO != null)
      {
        this.pendingtiList = bd.getPendingDataForChecker(this.mttVO);
        if ((this.pendingtiList != null) &&
          (this.pendingtiList.size() > 0))
        {
          this.mttVO.setMttListKeyId(((MTTDataVO)this.pendingtiList.get(0)).getMttListKeyId());
          this.mttVO.setMttListMTTNumber(((MTTDataVO)this.pendingtiList.get(0)).getMttListMTTNumber());
          this.mttVO.setMttListCurrentStatus(((MTTDataVO)this.pendingtiList.get(0)).getMttListCurrentStatus());
          this.mttVO.setMttListMakerUserId(((MTTDataVO)this.pendingtiList.get(0)).getMttListMakerUserId());
          this.mttVO.setMttListMakertmstmp(((MTTDataVO)this.pendingtiList.get(0)).getMttListMakertmstmp());
        }
      }
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return "success";
  }
 
  public String fetchTIDataForAmend()
    throws ApplicationException
  {
    logger.info("Entering Method");
    MTTHomeBD bd = null;
    try
    {
      if (this.mttVO != null)
      {
        bd = new MTTHomeBD();
        this.tiList = bd.getDataFromTIForAmend(this.mttVO);
        if ((this.tiList != null) &&
          (this.tiList.size() > 0))
        {
          this.mttVO.setMttListMasterRef(((MTTDataVO)this.tiList.get(0)).getMasRefNo());
          this.mttVO.setListEventeRefNo(((MTTDataVO)this.tiList.get(0)).getListEventeRefNo());
          this.mttVO.setMttListNumber(((MTTDataVO)this.tiList.get(0)).getMttListNumber());
          this.mttVO.setMttListStatus(((MTTDataVO)this.tiList.get(0)).getMttListStatus());
        }
      }
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return "success";
  }
 
  public String deleteMttNumber()
    throws ApplicationException
  {
    logger.info("Entering Method");
    MTTHomeBD bd = null;
    try
    {
      if (this.mttVO != null)
      {
        bd = new MTTHomeBD();
        if (this.chkList != null)
        {
          int updateCnt = bd.storeDeleteMTTNumber(this.chkList, this.mttVO);
          logger.info("updateCnt After StoreDelete" + updateCnt);
        }
        this.tiList = bd.getDataFromTIForAmend(this.mttVO);
        if ((this.tiList != null) &&
          (this.tiList.size() > 0))
        {
          this.mttVO.setMttListMasterRef(((MTTDataVO)this.tiList.get(0)).getMasRefNo());
          this.mttVO.setListEventeRefNo(((MTTDataVO)this.tiList.get(0)).getListEventeRefNo());
          this.mttVO.setMttListNumber(((MTTDataVO)this.tiList.get(0)).getMttListNumber());
          this.mttVO.setMttListStatus(((MTTDataVO)this.tiList.get(0)).getMttListStatus());
        }
      }
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return "success";
  }
 
  public String fetchMttNumberCheckerDataForAmend()
    throws ApplicationException
  {
    logger.info("Entering Method");
    MTTHomeBD bd = null;
    try
    {
      if (this.mttVO != null)
      {
        bd = new MTTHomeBD();
        this.pendingtiList = bd.getPendingMttNumberAmendDataForChecker(this.mttVO);
        this.pendingAddtiList = bd.getPendingMttNumberAddForChecker(this.mttVO);
        if ((this.pendingtiList != null) &&
          (this.pendingtiList.size() > 0))
        {
          this.mttVO.setMttListKeyId(((MTTDataVO)this.pendingtiList.get(0)).getMttListKeyId());
          this.mttVO.setMttListMTTNumber(((MTTDataVO)this.pendingtiList.get(0)).getMttListMTTNumber());
          this.mttVO.setMttListCurrentStatus(((MTTDataVO)this.pendingtiList.get(0)).getMttListCurrentStatus());
          this.mttVO.setMttListMakerUserId(((MTTDataVO)this.pendingtiList.get(0)).getMttListMakerUserId());
          this.mttVO.setMttListMakertmstmp(((MTTDataVO)this.pendingtiList.get(0)).getMttListMakertmstmp());
        }
        if ((this.pendingAddtiList != null) &&
          (this.pendingAddtiList.size() > 0))
        {
          this.mttVO.setMttNumberListAddKeyId(((MTTDataVO)this.pendingAddtiList.get(0)).getMttNumberListAddKeyId());
          this.mttVO.setMttNumberListAddMttNumber(((MTTDataVO)this.pendingAddtiList.get(0)).getMttNumberListAddMttNumber());
          this.mttVO.setMttNumberListAddMasterReference(((MTTDataVO)this.pendingAddtiList.get(0)).getMttNumberListAddMasterReference());
          this.mttVO.setMttNumberListAddEventRef(((MTTDataVO)this.pendingAddtiList.get(0)).getMttNumberListAddEventRef());
          this.mttVO.setMttNumberListAddMakerUserId(((MTTDataVO)this.pendingAddtiList.get(0)).getMttNumberListAddMakerUserId());
          this.mttVO.setMttNumberListAddMakertmstmp(((MTTDataVO)this.pendingAddtiList.get(0)).getMttNumberListAddMakertmstmp());
        }
      }
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return "success";
  }
 
  public String amendMTTNumberCheckerData()
    throws ApplicationException
  {
    logger.info("Entering Method");
    MTTHomeBD bd = null;
    try
    {
      bd = new MTTHomeBD();
      logger.info("check   :: " + this.check);
      if (this.chkList != null) {
        bd.amendMTTNumber(this.chkList, this.check, this.remarks);
      }
      if (this.mttVO != null)
      {
        this.pendingtiList = bd.getPendingMttNumberAmendDataForChecker(this.mttVO);
        this.pendingAddtiList = bd.getPendingMttNumberAddForChecker(this.mttVO);
        if ((this.pendingtiList != null) &&
          (this.pendingtiList.size() > 0))
        {
          this.mttVO.setMttListKeyId(((MTTDataVO)this.pendingtiList.get(0)).getMttListKeyId());
          this.mttVO.setMttListMTTNumber(((MTTDataVO)this.pendingtiList.get(0)).getMttListMTTNumber());
          this.mttVO.setMttListCurrentStatus(((MTTDataVO)this.pendingtiList.get(0)).getMttListCurrentStatus());
          this.mttVO.setMttListMakerUserId(((MTTDataVO)this.pendingtiList.get(0)).getMttListMakerUserId());
          this.mttVO.setMttListMakertmstmp(((MTTDataVO)this.pendingtiList.get(0)).getMttListMakertmstmp());
        }
        if ((this.pendingAddtiList != null) &&
          (this.pendingAddtiList.size() > 0))
        {
          this.mttVO.setMttNumberListAddKeyId(((MTTDataVO)this.pendingAddtiList.get(0)).getMttNumberListAddKeyId());
          this.mttVO.setMttNumberListAddMttNumber(((MTTDataVO)this.pendingAddtiList.get(0)).getMttNumberListAddMttNumber());
          this.mttVO.setMttNumberListAddMasterReference(((MTTDataVO)this.pendingAddtiList.get(0)).getMttNumberListAddMasterReference());
          this.mttVO.setMttNumberListAddEventRef(((MTTDataVO)this.pendingAddtiList.get(0)).getMttNumberListAddEventRef());
          this.mttVO.setMttNumberListAddMakerUserId(((MTTDataVO)this.pendingAddtiList.get(0)).getMttNumberListAddMakerUserId());
          this.mttVO.setMttNumberListAddMakertmstmp(((MTTDataVO)this.pendingAddtiList.get(0)).getMttNumberListAddMakertmstmp());
        }
      }
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return "success";
  }
 
  public String mttNumberCheckMasterRef()
    throws ApplicationException
  {
    logger.info("Entering Method");
    MTTHomeBD bd = null;
    String mttMasterCnt = "";
    String mttMasterPurSaleCnt = "";
    String mttNumberCnt = "";
    String masterRefPendingCnt = "";
    String masterRefSplitCnt = "";
    try
    {
      if (this.mttVO != null)
      {
        bd = new MTTHomeBD();
        mttMasterCnt = bd.checkMasterRef(this.mttVO);
        mttMasterPurSaleCnt = bd.checkMasterRefPurSale(this.mttVO);
        mttNumberCnt = bd.checkMasterRefMTTLinked(this.mttVO);
        masterRefPendingCnt = bd.checkMasterRefInProgress(this.mttVO);
        masterRefSplitCnt = bd.checkMasterRefInSplitProcess(this.mttVO);
        logger.info("mttMasterCnt :: " + mttMasterCnt);
        logger.info("mttNumberCnt :: " + mttNumberCnt);
        logger.info("masterRefPendingCnt :: " + masterRefPendingCnt);
        logger.info("masterRefSplitCnt :: " + masterRefSplitCnt);
        if (Integer.parseInt(mttMasterCnt.toString()) == 0)
        {
          addActionError("Invalid Master Reference...");
          this.mttVO.setIsButtonVisible("false");
        }
        else if (Integer.parseInt(mttMasterPurSaleCnt.toString()) > 0)
        {
          addActionError("Report Purchase Sale is not Y");
          this.mttVO.setIsButtonVisible("false");
        }
        else if (Integer.parseInt(mttNumberCnt.toString()) > 0)
        {
          addActionError("ORM Already attached with MTT Number " + mttNumberCnt + "...");
          this.mttVO.setIsButtonVisible("false");
        }
        else if (Integer.parseInt(masterRefPendingCnt.toString()) > 0)
        {
          addActionError("ORM Already In Progress for approval...");
          this.mttVO.setIsButtonVisible("false");
        }
        else if (Integer.parseInt(masterRefSplitCnt.toString()) > 0)
        {
          addActionError("ORM Already In Split Process...");
          this.mttVO.setIsButtonVisible("false");
        }
        else
        {
          this.mttVO.setIsButtonVisible("true");
        }
      }
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return "success";
  }
 
  public String mttNumberCheckMTTNum()
    throws ApplicationException
  {
    logger.info("Entering Method");
    MTTHomeBD bd = null;
    String mttNumberCnt = "";
    String mttCustomerCnt = "";
    String mttNumberStatus = "";
    try
    {
      if (this.mttVO != null)
      {
        bd = new MTTHomeBD();
        mttNumberCnt = bd.checkMTTNum(this.mttVO);
        mttNumberStatus = bd.checkMTTSts(this.mttVO);
        mttCustomerCnt = bd.checkMTTCustomer(this.mttVO);
        if (Integer.parseInt(mttNumberCnt.toString()) == 0)
        {
          addActionError("Invalid MTT Number...");
          this.mttVO.setIsButtonVisible("false");
        }
        else if (Integer.parseInt(mttNumberStatus.toString()) == 0)
        {
          addActionError("MTT Number is Closed...");
          this.mttVO.setIsButtonVisible("false");
        }
        else if (Integer.parseInt(mttCustomerCnt.toString()) == 0)
        {
          addActionError("MTT Number is already used by another Customer...");
          this.mttVO.setIsButtonVisible("false");
        }
        else
        {
          mttNumberCheckMasterRef();
        }
      }
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return "success";
  }
 
  public String mttNumberAddMasterRef()
    throws ApplicationException
  {
    logger.info("Entering Method");
    MTTHomeBD bd = null;
    try
    {
      bd = new MTTHomeBD();
      if (this.mttVO != null)
      {
        int insertCnt = bd.addMTTNumberMasterRef(this.mttVO);
       
        this.mttVO = new MTTDataVO();
        if (insertCnt > 0) {
          addActionMessage("New Master Ref Added Successfully...");
        }
      }
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return "success";
  }
 
  public String amendMTTNumberAddCheckerData()
    throws ApplicationException
  {
    logger.info("Entering Method");
    MTTHomeBD bd = null;
    try
    {
      bd = new MTTHomeBD();
      if (this.chkList1 != null) {
        bd.amendAddMTTNumberMasterRef(this.chkList1, this.check1, this.remarks1);
      }
      if (this.mttVO != null)
      {
        this.pendingtiList = bd.getPendingMttNumberAmendDataForChecker(this.mttVO);
        this.pendingAddtiList = bd.getPendingMttNumberAddForChecker(this.mttVO);
        if ((this.pendingtiList != null) &&
          (this.pendingtiList.size() > 0))
        {
          this.mttVO.setMttListKeyId(((MTTDataVO)this.pendingtiList.get(0)).getMttListKeyId());
          this.mttVO.setMttListMTTNumber(((MTTDataVO)this.pendingtiList.get(0)).getMttListMTTNumber());
          this.mttVO.setMttListCurrentStatus(((MTTDataVO)this.pendingtiList.get(0)).getMttListCurrentStatus());
          this.mttVO.setMttListMakerUserId(((MTTDataVO)this.pendingtiList.get(0)).getMttListMakerUserId());
          this.mttVO.setMttListMakertmstmp(((MTTDataVO)this.pendingtiList.get(0)).getMttListMakertmstmp());
        }
        if ((this.pendingAddtiList != null) &&
          (this.pendingAddtiList.size() > 0))
        {
          this.mttVO.setMttNumberListAddKeyId(((MTTDataVO)this.pendingAddtiList.get(0)).getMttNumberListAddKeyId());
          this.mttVO.setMttNumberListAddMttNumber(((MTTDataVO)this.pendingAddtiList.get(0)).getMttNumberListAddMttNumber());
          this.mttVO.setMttNumberListAddMasterReference(((MTTDataVO)this.pendingAddtiList.get(0)).getMttNumberListAddMasterReference());
          this.mttVO.setMttNumberListAddEventRef(((MTTDataVO)this.pendingAddtiList.get(0)).getMttNumberListAddEventRef());
          this.mttVO.setMttNumberListAddMakerUserId(((MTTDataVO)this.pendingAddtiList.get(0)).getMttNumberListAddMakerUserId());
          this.mttVO.setMttNumberListAddMakertmstmp(((MTTDataVO)this.pendingAddtiList.get(0)).getMttNumberListAddMakertmstmp());
        }
      }
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return "success";
  }
 
  public String mttNumberCheckMTTNum1()
    throws ApplicationException
  {
    logger.info("Entering Method");
    MTTHomeBD bd = null;
    String mttNumberCnt = "";
    String mttCustomerCnt = "";
    String mttNumberStatus = "";
    try
    {
      if (this.mttVO != null)
      {
        bd = new MTTHomeBD();
        mttNumberCnt = bd.checkMTTNum1(this.mttVO, this.mttListNumberToCheck);
        mttNumberStatus = bd.checkMTTSts(this.mttVO, this.mttListNumberToCheck);
        mttCustomerCnt = bd.checkMTTCustomer1(this.mttVO, this.mttListNumberToCheck);
        if (Integer.parseInt(mttNumberCnt.toString()) == 0)
        {
          addActionError("Invalid MTT Number...");
          this.mttVO.setIsButtonVisible("false");
        }
        else if (Integer.parseInt(mttNumberStatus.toString()) == 0)
        {
          addActionError("MTT Number is Closed...");
          this.mttVO.setIsButtonVisible("false");
        }
        else if (Integer.parseInt(mttCustomerCnt.toString()) == 0)
        {
          addActionError("MTT Number already is used by another Customer...");
          this.mttVO.setIsButtonVisible("false");
        }
        else
        {
          this.mttVO.setIsButtonVisible("true");
        }
      }
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return "success";
  }
 
  public String fetchMTTNumberOnMasterRef()
    throws ApplicationException
  {
    logger.info("Entering Method");
    MTTHomeBD bd = null;
    try
    {
      if (this.mttVO != null)
      {
        bd = new MTTHomeBD();
        bd.getMTTNumberOnMasterRef(this.mttVO);
      }
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return "success";
  }
 
  public String submitMTTNumberSplit()
    throws ApplicationException
  {
    logger.info("Entering Method");
    MTTHomeBD bd = null;
    try
    {
      if (this.mttVO != null)
      {
        bd = new MTTHomeBD();
        int updateCnt = bd.submitMTTNumbeSplit(this.mttVO, this.splitRemarks, this.mttListNumber11, this.mttListTransAmt11, this.mttListNumber12, this.mttListTransAmt12, this.mttListNumber13, this.mttListTransAmt13, this.mttListNumber14, this.mttListTransAmt14, this.mttListNumber15,
          this.mttListTransAmt15, this.mttListNumber16, this.mttListTransAmt16, this.mttListNumber17, this.mttListTransAmt17, this.mttListNumber18, this.mttListTransAmt18, this.mttListNumber19, this.mttListTransAmt19, this.mttListNumber20, this.mttListTransAmt20);
        logger.info("updateCnt After submitMTTNumbeSplit" + updateCnt);
      }
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return "success";
  }
 
  public String fetchSplitDataForApproval()
    throws ApplicationException
  {
    logger.info("Entering Method");
    MTTHomeBD bd = null;
    try
    {
      if (this.mttVO != null)
      {
        bd = new MTTHomeBD();
        this.pendingtiList = bd.fetchSplitDataForApproval(this.mttVO);
        if ((this.pendingtiList != null) &&
          (this.pendingtiList.size() > 0))
        {
          this.mttVO.setMttSplitListKeyId(((MTTDataVO)this.pendingtiList.get(0)).getMttSplitListKeyId());
          this.mttVO.setMttSplitListMTTNumber(((MTTDataVO)this.pendingtiList.get(0)).getMttSplitListMTTNumber());
          this.mttVO.setMttSplitListMTTAmount(((MTTDataVO)this.pendingtiList.get(0)).getMttSplitListMTTAmount());
          this.mttVO.setSplitCheckerRemarks(((MTTDataVO)this.pendingtiList.get(0)).getSplitCheckerRemarks());
          this.mttVO.setMttSplitListMakerUserId(((MTTDataVO)this.pendingtiList.get(0)).getMttSplitListMakerUserId());
          this.mttVO.setMttSplitListMakertmstmp(((MTTDataVO)this.pendingtiList.get(0)).getMttSplitListMakertmstmp());
        }
      }
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return "success";
  }
 
  public String amendSplitCheckerData()
    throws ApplicationException
  {
    logger.info("Entering Method");
    MTTHomeBD bd = null;
    try
    {
      bd = new MTTHomeBD();
      if (this.mttVO != null)
      {
        bd.amendSplitCheckerData(this.mttVO, this.splitCheck, this.splitCheckerRemarks);
        this.pendingtiList = bd.fetchSplitDataForApproval(this.mttVO);
        if ((this.pendingtiList != null) &&
          (this.pendingtiList.size() > 0))
        {
          this.mttVO.setMttSplitListKeyId(((MTTDataVO)this.pendingtiList.get(0)).getMttSplitListKeyId());
          this.mttVO.setMttSplitListMTTNumber(((MTTDataVO)this.pendingtiList.get(0)).getMttSplitListMTTNumber());
          this.mttVO.setMttSplitListMTTAmount(((MTTDataVO)this.pendingtiList.get(0)).getMttSplitListMTTAmount());
          this.mttVO.setSplitCheckerRemarks(((MTTDataVO)this.pendingtiList.get(0)).getSplitCheckerRemarks());
          this.mttVO.setMttSplitListMakerUserId(((MTTDataVO)this.pendingtiList.get(0)).getMttSplitListMakerUserId());
          this.mttVO.setMttSplitListMakertmstmp(((MTTDataVO)this.pendingtiList.get(0)).getMttSplitListMakertmstmp());
        }
      }
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return "success";
  }
 
  public String processStagingData()
    throws ApplicationException
  {
    logger.info("Entering Method");
    MTTHomeBD bd = null;
    String mttStaging = "";
    try
    {
      if (this.mttVO != null)
      {
        bd = new MTTHomeBD();
        mttStaging = bd.processStagingData(this.mttVO);
        if (Integer.parseInt(mttStaging.toString()) > 0) {
          addActionError("Inserted data to Staging Successfully...");
        } else {
          addActionError("Data not inserted into Staging...");
        }
      }
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return "success";
  }
 
  public String downloadANNEXIIFile()
  {
    logger.info("----------MTT -------- [downloadANNEXIIFile]- ------------------- :: fileUtilFileName :: " + this.fileUtilFileName + " :: fileUtilFromDate :: " + this.fileUtilFromDate + " :: fileUtilToDate :: " + this.fileUtilToDate);
    Connection con = null;
    LoggableStatement loggableStatement2 = null;
    PreparedStatement pps1 = null;
    ResultSet rs1 = null;
    String result = "success";
    String fileName1 = "";
    String myFilePath = "";
    String frmDate = "";
    String toDate = "";
    try
    {
      con = DBConnectionUtility.getConnection();
     

      String proQuery = " SELECT MTT_FILE_DWNLD_SEQNO.NEXTVAL AS FILEDWNLD_SEQNO, TO_CHAR(TO_DATE('" +
        this.fileUtilFromDate + "','DD/MM/YY'),'DD/MON/YYYY') AS FRMDATE," +
        " TO_CHAR(TO_DATE('" + this.fileUtilToDate + "','DD/MM/YY'),'DD/MON/YYYY') AS TODATE FROM DUAL";
      pps1 = con.prepareStatement(proQuery);
      rs1 = pps1.executeQuery();
     
      int seq = 0;
      if (rs1.next())
      {
        seq = rs1.getInt("FILEDWNLD_SEQNO");
        frmDate = rs1.getString("FRMDATE");
        toDate = rs1.getString("TODATE");
      }
      CallableStatement callableStatement = null;
      try
      {
        String insertDataToStaging = "{call REP_MTT_ANNEXURE_II_INSERT(?,?)}";
        callableStatement = con.prepareCall(insertDataToStaging);
        callableStatement.setString(1, this.fileUtilFromDate);
        callableStatement.setString(2, this.fileUtilToDate);
        logger.info("----------MTT -------- [downloadANNEXIIFile]- ------------------- ::PROCEDURE EXECUTION START");
        callableStatement.executeUpdate();
        logger.info("----------MTT -------- [downloadANNEXIIFile]- ------------------- ::PROCEDURE EXECUTION END");
      }
      catch (SQLException se)
      {
        logger.info("downloadANNEXIIFile---------------------------Exception-------------" + se);
        se.printStackTrace();
      }
      finally
      {
        if (callableStatement != null) {
          callableStatement.close();
        }
      }
      logger.info("----------MTT -------- [downloadANNEXIIFile]- ------------------- :: seq :: " + seq);
     
      String queryForANNEXII = "SELECT MTT_CIF,MTT_REF,ADCODE,ADREFNO,TRADER,BUYER_ADDRESS,SELLER_ADDRESS,TO_CHAR(TO_DATE(COMMENCEMENT_DATE,'dd-mm-yy'),'dd-mm-yy') AS COMMENCEMENT_DATE,TO_CHAR(TO_DATE(COMPLETION_DATE,'dd-mm-yy'),'dd-mm-yy') AS COMPLETION_DATE,EXPORT_AMOUNT_REALISED,EXPORT_OUTSTANDING_AMOUNT,IMPORT_AMOUNT_PAID,IMPORT_OUTSTANDING_AMOUNT,FOREIGN_EXCHANGE_OVERLAY,AGEING FROM REP_MTT_ANNEXURE_II ORDER BY MTT_REF";
     


      logger.info("QueryExcel: " + queryForANNEXII);
      String fileLoc1 = "";
      fileLoc1 = CommonMethods.getBridgePropertyValue("MTTREPORTS");
     
      logger.info("fileLoc1: " + fileLoc1);
     
      File f = new File(fileLoc1);
      if (!f.exists()) {
        f.mkdir();
      }
      fileName1 = "MTTANNEXUREII_" + seq;
      myFilePath = fileLoc1 + fileName1 + ".xlsx";
      generateManualANNEXIIFile(queryForANNEXII, myFilePath, frmDate, toDate);
     
      downloadFiles(myFilePath);
      try
      {
        String DELETEQuery = "DELETE FROM REP_MTT_ANNEXURE_II";
       
        loggableStatement2 = new LoggableStatement(con, DELETEQuery);
        logger.info("Before DELETE " + DELETEQuery);
        loggableStatement2.executeUpdate();
        logger.info("After DELETE ");
      }
      catch (SQLException exe)
      {
        logger.info("----------MTT -------- [downloadANNEXRBIFile]- --------------exception----------------" + exe);
        exe.printStackTrace();
      }
      finally
      {
        DBConnectionUtility.surrenderDB(null, loggableStatement2, null);
      }
      addActionError("File (" + fileName1 + ") Downloaded Successfully..");
    }
    catch (Exception e)
    {
      logger.info("----------MTT -------- [downloadANNEXIIFile]- --------------exception----------------" + e);
      result = "error";
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pps1, rs1);
     
      logger.info("Finally Occurred in saveDetail");
    }
    return result;
  }
 
  public String downloadANNEXIIIFile()
  {
    logger.info("----------MTT -------- [downloadANNEXIIIFile]- ------------------- :: fileUtilFileName :: " + this.fileUtilFileName + " :: fileUtilFromDate :: " + this.fileUtilFromDate + " :: fileUtilToDate :: " + this.fileUtilToDate);
    Connection con = null;
    LoggableStatement loggableStatement2 = null;
    PreparedStatement pps1 = null;
    ResultSet rs1 = null;
    String result = "success";
    String fileName1 = "";
    String myFilePath = "";
    String frmDate = "";
    String toDate = "";
    try
    {
      con = DBConnectionUtility.getConnection();
     
      String proQuery = " SELECT MTT_FILE_DWNLD_SEQNO.NEXTVAL AS FILEDWNLD_SEQNO, TO_CHAR(TO_DATE('" +
        this.fileUtilFromDate + "','DD/MM/YY'),'DD/MON/YYYY') AS FRMDATE," +
        " TO_CHAR(TO_DATE('" + this.fileUtilToDate + "','DD/MM/YY'),'DD/MON/YYYY') AS TODATE FROM DUAL";
      pps1 = con.prepareStatement(proQuery);
      rs1 = pps1.executeQuery();
     
      int seq = 0;
      if (rs1.next())
      {
        seq = rs1.getInt("FILEDWNLD_SEQNO");
        frmDate = rs1.getString("FRMDATE");
        toDate = rs1.getString("TODATE");
      }
      CallableStatement callableStatement = null;
      try
      {
        String insertDataToStaging = "{call REP_MTT_ANNEXURE_III_INSERT(?,?)}";
        callableStatement = con.prepareCall(insertDataToStaging);
        callableStatement.setString(1, this.fileUtilFromDate);
        callableStatement.setString(2, this.fileUtilToDate);
        logger.info("----------MTT -------- [downloadANNEXIIIFile]- ------------------- ::PROCEDURE EXECUTION START");
        callableStatement.executeUpdate();
        logger.info("----------MTT -------- [downloadANNEXIIIFile]- ------------------- ::PROCEDURE EXECUTION END");
      }
      catch (SQLException se)
      {
        logger.info("downloadANNEXIIIFile---------------------------Exception-------------" + se);
        se.printStackTrace();
      }
      finally
      {
        if (callableStatement != null) {
          callableStatement.close();
        }
      }
      logger.info("----------MTT -------- [downloadANNEXIIIFile]- ------------------- :: seq :: " + seq);
     
      String queryForANNEXIII = " SELECT MTT_REF,MTT_STATUS,TXN_REF,RBI_REF,NEW_GEN_REF,AD_CODE,BRANCH_CODE,BRANCH_NAME, TO_CHAR(TO_DATE(REPORTING_DTE,'dd-mm-yy'),'dd-mm-yy') AS REPORTING_DTE, CUSTOMER_UCIC, CUSTOMER_NAME,TO_CHAR(TO_DATE(TRANS_DATE,'dd-mm-yy'),'dd-mm-yy') AS TRANS_DATE, TO_CHAR(TO_DATE(SHIPMENT_DATE,'dd-mm-yy'),'dd-mm-yy') AS SHIPMENT_DATE,  TO_CHAR(TO_DATE(VALUE_DATE,'dd-mm-yy'),'dd-mm-yy') AS VALUE_DATE, TRANS_CCY, TRANS_AMOUNT,OUTSTANDING_AMOUNT,PURPOSE_CODE,PORT_OF_SHIPMENT,PORT_OF_DISCHARGE,  TO_CHAR(TO_DATE(DATE_OF_COMMENCEMENT,'dd-mm-yy'),'dd-mm-yy') AS DATE_OF_COMMENCEMENT, TO_CHAR(TO_DATE(COMPLETION_DATE,'dd-mm-yy'),'dd-mm-yy') AS COMPLETION_DATE,  FOREIGN_EXCHANGE_OVERLAY,AGEING,REMARKS,TRANS_AMOUNT_USD,OUTSTD_AMOUNT_USD,INVOICE_NUMBER, TO_CHAR(TO_DATE(INVOICE_DATE,'dd-mm-yy'),'dd-mm-yy') AS INVOICE_DATE  FROM REP_MTT_ANNEXURE_III ORDER BY MTT_REF";
     










      logger.info("QueryExcel: " + queryForANNEXIII);
      String fileLoc1 = "";
      fileLoc1 = CommonMethods.getBridgePropertyValue("MTTREPORTS");
     
      logger.info("fileLoc1: " + fileLoc1);
     
      File f = new File(fileLoc1);
      if (!f.exists()) {
        f.mkdir();
      }
      fileName1 = "MTTANNEXUREIII_" + seq;
      myFilePath = fileLoc1 + fileName1 + ".xlsx";
      generateManualANNEXIIIFile(queryForANNEXIII, myFilePath, frmDate, toDate);
     
      downloadFiles(myFilePath);
      try
      {
        String DELETEQuery = "DELETE FROM REP_MTT_ANNEXURE_III";
       
        loggableStatement2 = new LoggableStatement(con, DELETEQuery);
        logger.info("Before DELETE " + DELETEQuery);
        loggableStatement2.executeUpdate();
        logger.info("After DELETE ");
      }
      catch (SQLException exe)
      {
        logger.info("----------MTT -------- [downloadANNEXRBIFile]- --------------exception----------------" + exe);
        exe.printStackTrace();
      }
      finally
      {
        DBConnectionUtility.surrenderDB(null, loggableStatement2, null);
      }
      addActionError("File (" + fileName1 + ") Downloaded Successfully..");
    }
    catch (Exception e)
    {
      logger.info("----------MTT -------- [downloadANNEXIIIFile]- --------------exception----------------" + e);
      result = "error";
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pps1, rs1);
     
      logger.info("Finally Occurred in saveDetail");
    }
    return result;
  }
 
  public String downloadRBIFile()
  {
    logger.info("----------MTT -------- [downloadANNEXRBIFile]- ------------------- :: fileUtilFileName :: " + this.fileUtilFileName + " :: fileUtilFromDate :: " + this.fileUtilFromDate + " :: fileUtilToDate :: " + this.fileUtilToDate + " :: mttNumber :: " + this.mttNumber + " :: custId :: " + this.custId + " :: brnCode :: " + this.brnCode);
    Connection con = null;
    LoggableStatement loggableStatement2 = null;
    PreparedStatement pps1 = null;
    ResultSet rs1 = null;
    String result = "success";
    String fileName1 = "";
    String myFilePath = "";
    String frmDate = "";
    String toDate = "";
    try
    {
      con = DBConnectionUtility.getConnection();
     
      String proQuery = " SELECT MTT_FILE_DWNLD_SEQNO.NEXTVAL AS FILEDWNLD_SEQNO, TO_CHAR(TO_DATE('" +
        this.fileUtilFromDate + "','DD/MM/YY'),'DD/MON/YYYY') AS FRMDATE," +
        " TO_CHAR(TO_DATE('" + this.fileUtilToDate + "','DD/MM/YY'),'DD/MON/YYYY') AS TODATE FROM DUAL";
      pps1 = con.prepareStatement(proQuery);
      rs1 = pps1.executeQuery();
     
      int seq = 0;
      if (rs1.next())
      {
        seq = rs1.getInt("FILEDWNLD_SEQNO");
        frmDate = rs1.getString("FRMDATE");
        toDate = rs1.getString("TODATE");
      }
      CallableStatement callableStatement = null;
      try
      {
        String insertDataToStaging = "{call REP_MTT_ANNEXURE_RBI_INSERT(?,?,?,?,?)}";
        callableStatement = con.prepareCall(insertDataToStaging);
        callableStatement.setString(1, this.fileUtilFromDate.trim());
        callableStatement.setString(2, this.fileUtilToDate.trim());
        if (this.brnCode.trim().equalsIgnoreCase("")) {
          callableStatement.setString(3, null);
        } else {
          callableStatement.setString(3, this.brnCode.trim());
        }
        if (this.custId.trim().equalsIgnoreCase("")) {
          callableStatement.setString(4, null);
        } else {
          callableStatement.setString(4, this.custId.trim());
        }
        if (this.mttNumber.trim().equalsIgnoreCase("")) {
          callableStatement.setString(5, null);
        } else {
          callableStatement.setString(5, this.mttNumber.trim());
        }
        logger.info("----------MTT -------- [downloadANNEXRBIFile]- ------------------- ::PROCEDURE EXECUTION START");
        callableStatement.executeUpdate();
        logger.info("----------MTT -------- [downloadANNEXRBIFile]- ------------------- ::PROCEDURE EXECUTION END");
      }
      catch (SQLException se)
      {
        logger.info("downloadANNEXIIIFile---------------------------Exception-------------" + se);
        se.printStackTrace();
      }
      finally
      {
        if (callableStatement != null) {
          callableStatement.close();
        }
      }
      logger.info("----------MTT -------- [downloadANNEXRBIFile]- ------------------- :: seq :: " + seq);
     
      String queryForANNEXRBI = " SELECT MTT_REF,MTT_STATUS,SOL_ID,SOL_NAME,CUSTOMER_NAME,CIF_NO,IMPORT_BILL_ID,IMPORT_COUNTERPARTY, TO_CHAR(TO_DATE(IMPORT_PAYMENT_DATE,'DD/MM/YY'),'DD/MON/YYYY') AS IMPORT_PAYMENT_DATE, TO_CHAR(TO_DATE(IMPORT_SHIPMENT_DATE,'DD/MM/YY'),'DD/MON/YYYY') AS IMPORT_SHIPMENT_DATE,IMPORT_CCY, IMPORT_BILL_AMOUNT,IMPORT_BILL_AMOUNT_USD,EXPORT_BILL_ID, TO_CHAR(TO_DATE(EXPORT_PAYMENT_DATE,'DD/MM/YY'),'DD/MON/YYYY') AS EXPORT_PAYMENT_DATE, TO_CHAR(TO_DATE(EXPORT_SHIPMENT_DATE,'DD/MM/YY'),'DD/MON/YYYY') AS EXPORT_SHIPMENT_DATE, TO_CHAR(TO_DATE(EXPORT_REALISATION_DATE,'DD/MM/YY'),'DD/MON/YYYY') AS EXPORT_REALISATION_DATE, EXPORT_CCY,EXPORT_BILL_AMOUNT, EXPORT_BILL_AMOUNT_USD,EXPORT_COUNTERPARTY,TOTAL_TIME_FOR_OUTLAY_FOREIGN_EXCHANGE, TOTAL_TIME_TAKEN_FOR_COMPLETING_MTT,REMARKS  FROM REP_MTT_ANNEXURE_RBI ORDER BY MTT_REF";
     










      logger.info("QueryExcel: " + queryForANNEXRBI);
      String fileLoc1 = "";
      fileLoc1 = CommonMethods.getBridgePropertyValue("MTTREPORTS");
     
      logger.info("fileLoc1: " + fileLoc1);
     
      File f = new File(fileLoc1);
      if (!f.exists()) {
        f.mkdir();
      }
      fileName1 = "MTTANNEXURERBI_" + seq;
      myFilePath = fileLoc1 + fileName1 + ".xls";
      generateManualANNEXRBIFile(queryForANNEXRBI, myFilePath, frmDate, toDate);
     
      downloadFiles(myFilePath);
      try
      {
        String DELETEQuery = "DELETE FROM REP_MTT_ANNEXURE_RBI";
       
        loggableStatement2 = new LoggableStatement(con, DELETEQuery);
        logger.info("Before DELETE " + DELETEQuery);
        loggableStatement2.executeUpdate();
        logger.info("After DELETE ");
      }
      catch (SQLException exe)
      {
        logger.info("----------MTT -------- [downloadANNEXRBIFile]- --------------exception----------------" + exe);
        exe.printStackTrace();
      }
      finally
      {
        DBConnectionUtility.surrenderDB(null, loggableStatement2, null);
      }
      addActionError("File (" + fileName1 + ") Downloaded Successfully..");
    }
    catch (Exception e)
    {
      logger.info("----------MTT -------- [downloadANNEXRBIFile]- --------------exception----------------" + e);
      result = "error";
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pps1, rs1);
     
      logger.info("Finally Occurred in saveDetail");
    }
    return result;
  }
 
  public void generateManualANNEXIIFile(String sqlQuery, String fileNameWithPath, String frmDate, String toDate)
  {
    Connection connection = null;
    Statement stmt = null;
    ResultSet resultSet = null;
   
    logger.info("generateManualANNEXIIFile Start...");
    try
    {
      connection = DBConnectionUtility.getConnection();
      stmt = connection.createStatement();
      resultSet = stmt.executeQuery(sqlQuery);
     

      HSSFWorkbook workbook = new HSSFWorkbook();
      HSSFSheet sheet = workbook.createSheet("Report");
      try
      {
        writeHeaderLineANNEXII(sheet, workbook, frmDate, toDate);
       
        writeHeaderLineANNEXII1(sheet, workbook);
       
        writeDataLinesANNEXII(resultSet, workbook, sheet);
       
        FileOutputStream outputStream = new FileOutputStream(fileNameWithPath);
        workbook.write(outputStream);
        outputStream.close();
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
      logger.info("generateManualANNEXIIFile End...");
    }
    catch (Exception e)
    {
      e.printStackTrace();
      try
      {
        DBConnectionUtility.surrenderDB(connection, stmt, resultSet);
      }
      catch (Exception e)
      {
        logger.info(e.getMessage());
      }
    }
    finally
    {
      try
      {
        DBConnectionUtility.surrenderDB(connection, stmt, resultSet);
      }
      catch (Exception e)
      {
        logger.info(e.getMessage());
      }
    }
  }
 
  private void writeHeaderLineANNEXII(HSSFSheet sheet, HSSFWorkbook workbook, String frmDate, String toDate)
  {
    Row headerRow = sheet.createRow(0);
    sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 14));
   
    Cell headerCell = headerRow.createCell(0);
    CellStyle cellStyle = workbook.createCellStyle();
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("MTT ANNEXURE II REPORT FROM DATE:" + frmDate + " TO DATE: " + toDate);
  }
 
  private void writeHeaderLineANNEXII1(HSSFSheet sheet, HSSFWorkbook workbook)
  {
    Row headerRow = sheet.createRow(2);
   
    Cell headerCell = headerRow.createCell(0);
    CellStyle cellStyle = workbook.createCellStyle();
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    cellStyle.setBorderBottom(BorderStyle.THIN);
    cellStyle.setBorderTop(BorderStyle.THIN);
    cellStyle.setBorderRight(BorderStyle.THIN);
    cellStyle.setBorderLeft(BorderStyle.THIN);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("MTT CIF");
    headerCell = headerRow.createCell(1);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    cellStyle.setBorderBottom(BorderStyle.THIN);
    cellStyle.setBorderTop(BorderStyle.THIN);
    cellStyle.setBorderRight(BorderStyle.THIN);
    cellStyle.setBorderLeft(BorderStyle.THIN);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("MTT REF");
    headerCell = headerRow.createCell(2);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    cellStyle.setBorderBottom(BorderStyle.THIN);
    cellStyle.setBorderTop(BorderStyle.THIN);
    cellStyle.setBorderRight(BorderStyle.THIN);
    cellStyle.setBorderLeft(BorderStyle.THIN);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("ADCODE");
    headerCell = headerRow.createCell(3);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    cellStyle.setBorderBottom(BorderStyle.THIN);
    cellStyle.setBorderTop(BorderStyle.THIN);
    cellStyle.setBorderRight(BorderStyle.THIN);
    cellStyle.setBorderLeft(BorderStyle.THIN);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("ADREFNO");
    headerCell = headerRow.createCell(4);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    cellStyle.setBorderBottom(BorderStyle.THIN);
    cellStyle.setBorderTop(BorderStyle.THIN);
    cellStyle.setBorderRight(BorderStyle.THIN);
    cellStyle.setBorderLeft(BorderStyle.THIN);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("TRADER");
    headerCell = headerRow.createCell(5);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    cellStyle.setBorderBottom(BorderStyle.THIN);
    cellStyle.setBorderTop(BorderStyle.THIN);
    cellStyle.setBorderRight(BorderStyle.THIN);
    cellStyle.setBorderLeft(BorderStyle.THIN);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("BUYER ADDRESS");
    headerCell = headerRow.createCell(6);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    cellStyle.setBorderBottom(BorderStyle.THIN);
    cellStyle.setBorderTop(BorderStyle.THIN);
    cellStyle.setBorderRight(BorderStyle.THIN);
    cellStyle.setBorderLeft(BorderStyle.THIN);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("SELLER ADDRESS");
    headerCell = headerRow.createCell(7);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    cellStyle.setBorderBottom(BorderStyle.THIN);
    cellStyle.setBorderTop(BorderStyle.THIN);
    cellStyle.setBorderRight(BorderStyle.THIN);
    cellStyle.setBorderLeft(BorderStyle.THIN);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("COMMENCEMENT DATE");
    headerCell = headerRow.createCell(8);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    cellStyle.setBorderBottom(BorderStyle.THIN);
    cellStyle.setBorderTop(BorderStyle.THIN);
    cellStyle.setBorderRight(BorderStyle.THIN);
    cellStyle.setBorderLeft(BorderStyle.THIN);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("COMPLETION DATE");
    headerCell = headerRow.createCell(9);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    cellStyle.setBorderBottom(BorderStyle.THIN);
    cellStyle.setBorderTop(BorderStyle.THIN);
    cellStyle.setBorderRight(BorderStyle.THIN);
    cellStyle.setBorderLeft(BorderStyle.THIN);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("EXPORT AMOUNT REALISED");
    headerCell = headerRow.createCell(10);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    cellStyle.setBorderBottom(BorderStyle.THIN);
    cellStyle.setBorderTop(BorderStyle.THIN);
    cellStyle.setBorderRight(BorderStyle.THIN);
    cellStyle.setBorderLeft(BorderStyle.THIN);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("EXPORT OUTSTANDING AMOUNT");
    headerCell = headerRow.createCell(11);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    cellStyle.setBorderBottom(BorderStyle.THIN);
    cellStyle.setBorderTop(BorderStyle.THIN);
    cellStyle.setBorderRight(BorderStyle.THIN);
    cellStyle.setBorderLeft(BorderStyle.THIN);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("IMPORT AMOUNT PAID");
    headerCell = headerRow.createCell(12);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    cellStyle.setBorderBottom(BorderStyle.THIN);
    cellStyle.setBorderTop(BorderStyle.THIN);
    cellStyle.setBorderRight(BorderStyle.THIN);
    cellStyle.setBorderLeft(BorderStyle.THIN);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("IMPORT OUTSTANDING AMOUNT");
    headerCell = headerRow.createCell(13);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    cellStyle.setBorderBottom(BorderStyle.THIN);
    cellStyle.setBorderTop(BorderStyle.THIN);
    cellStyle.setBorderRight(BorderStyle.THIN);
    cellStyle.setBorderLeft(BorderStyle.THIN);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("FOREIGN EXCHANGE OVERLAY");
    headerCell = headerRow.createCell(14);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    cellStyle.setBorderBottom(BorderStyle.THIN);
    cellStyle.setBorderTop(BorderStyle.THIN);
    cellStyle.setBorderRight(BorderStyle.THIN);
    cellStyle.setBorderLeft(BorderStyle.THIN);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("AGEING");
  }
 
  private void writeDataLinesANNEXII(ResultSet result, HSSFWorkbook workbook, HSSFSheet sheet)
    throws SQLException
  {
    int rowCount = 3;
    while (result.next())
    {
      String MTT_CIF = result.getString("MTT_CIF");
      String MTT_REF = result.getString("MTT_REF");
      String ADCODE = result.getString("ADCODE");
      String ADREFNO = result.getString("ADREFNO");
      String TRADER = result.getString("TRADER");
      String BUYER_ADDRESS = result.getString("BUYER_ADDRESS");
      String SELLER_ADDRESS = result.getString("SELLER_ADDRESS");
      String COMMENCEMENT_DATE = result.getString("COMMENCEMENT_DATE");
      String COMPLETION_DATE = result.getString("COMPLETION_DATE");
      String EXPORT_AMOUNT_REALISED = result.getString("EXPORT_AMOUNT_REALISED");
      String EXPORT_OUTSTANDING_AMOUNT = result.getString("EXPORT_OUTSTANDING_AMOUNT");
      String IMPORT_AMOUNT_PAID = result.getString("IMPORT_AMOUNT_PAID");
      String IMPORT_OUTSTANDING_AMOUNT = result.getString("IMPORT_OUTSTANDING_AMOUNT");
      String FOREIGN_EXCHANGE_OVERLAY = result.getString("FOREIGN_EXCHANGE_OVERLAY");
      String AGEING = result.getString("AGEING");
     
      Row row = sheet.createRow(rowCount++);
     
      int columnCount = 0;
      Cell cell = row.createCell(columnCount++);
      CellStyle cellStyle = workbook.createCellStyle();
      cellStyle.setBorderBottom(BorderStyle.THIN);
      cellStyle.setBorderTop(BorderStyle.THIN);
      cellStyle.setBorderRight(BorderStyle.THIN);
      cellStyle.setBorderLeft(BorderStyle.THIN);
      cell.setCellStyle(cellStyle);
      cell.setCellValue(MTT_CIF);
     
      cell = row.createCell(columnCount++);
      cellStyle.setBorderBottom(BorderStyle.THIN);
      cellStyle.setBorderTop(BorderStyle.THIN);
      cellStyle.setBorderRight(BorderStyle.THIN);
      cellStyle.setBorderLeft(BorderStyle.THIN);
      cell.setCellStyle(cellStyle);
      cell.setCellValue(MTT_REF);
     
      cell = row.createCell(columnCount++);
      cellStyle.setBorderBottom(BorderStyle.THIN);
      cellStyle.setBorderTop(BorderStyle.THIN);
      cellStyle.setBorderRight(BorderStyle.THIN);
      cellStyle.setBorderLeft(BorderStyle.THIN);
      cell.setCellStyle(cellStyle);
      cell.setCellValue(ADCODE);
     
      cell = row.createCell(columnCount++);
      cellStyle.setBorderBottom(BorderStyle.THIN);
      cellStyle.setBorderTop(BorderStyle.THIN);
      cellStyle.setBorderRight(BorderStyle.THIN);
      cellStyle.setBorderLeft(BorderStyle.THIN);
      cell.setCellStyle(cellStyle);
      cell.setCellValue(ADREFNO);
     
      cell = row.createCell(columnCount++);
      cellStyle.setBorderBottom(BorderStyle.THIN);
      cellStyle.setBorderTop(BorderStyle.THIN);
      cellStyle.setBorderRight(BorderStyle.THIN);
      cellStyle.setBorderLeft(BorderStyle.THIN);
      cell.setCellStyle(cellStyle);
      cell.setCellValue(TRADER);
     
      cell = row.createCell(columnCount++);
      cellStyle.setBorderBottom(BorderStyle.THIN);
      cellStyle.setBorderTop(BorderStyle.THIN);
      cellStyle.setBorderRight(BorderStyle.THIN);
      cellStyle.setBorderLeft(BorderStyle.THIN);
      cell.setCellStyle(cellStyle);
      cell.setCellValue(BUYER_ADDRESS);
     
      cell = row.createCell(columnCount++);
      cellStyle.setBorderBottom(BorderStyle.THIN);
      cellStyle.setBorderTop(BorderStyle.THIN);
      cellStyle.setBorderRight(BorderStyle.THIN);
      cellStyle.setBorderLeft(BorderStyle.THIN);
      cell.setCellStyle(cellStyle);
      cell.setCellValue(SELLER_ADDRESS);
     
      cell = row.createCell(columnCount++);
     





      cellStyle.setBorderBottom(BorderStyle.THIN);
      cellStyle.setBorderTop(BorderStyle.THIN);
      cellStyle.setBorderRight(BorderStyle.THIN);
      cellStyle.setBorderLeft(BorderStyle.THIN);
      cell.setCellStyle(cellStyle);
      cell.setCellValue(COMMENCEMENT_DATE);
     
      cell = row.createCell(columnCount++);
     





      cellStyle.setBorderBottom(BorderStyle.THIN);
      cellStyle.setBorderTop(BorderStyle.THIN);
      cellStyle.setBorderRight(BorderStyle.THIN);
      cellStyle.setBorderLeft(BorderStyle.THIN);
      cell.setCellStyle(cellStyle);
      cell.setCellValue(COMPLETION_DATE);
     
      cell = row.createCell(columnCount++);
      cellStyle.setBorderBottom(BorderStyle.THIN);
      cellStyle.setBorderTop(BorderStyle.THIN);
      cellStyle.setBorderRight(BorderStyle.THIN);
      cellStyle.setBorderLeft(BorderStyle.THIN);
      cellStyle.setAlignment(HorizontalAlignment.RIGHT);
      cell.setCellStyle(cellStyle);
      cell.setCellValue(EXPORT_AMOUNT_REALISED);
     
      cell = row.createCell(columnCount++);
      cellStyle.setBorderBottom(BorderStyle.THIN);
      cellStyle.setBorderTop(BorderStyle.THIN);
      cellStyle.setBorderRight(BorderStyle.THIN);
      cellStyle.setBorderLeft(BorderStyle.THIN);
      cellStyle.setAlignment(HorizontalAlignment.RIGHT);
      cell.setCellStyle(cellStyle);
      cell.setCellValue(EXPORT_OUTSTANDING_AMOUNT);
     
      cell = row.createCell(columnCount++);
      cellStyle.setBorderBottom(BorderStyle.THIN);
      cellStyle.setBorderTop(BorderStyle.THIN);
      cellStyle.setBorderRight(BorderStyle.THIN);
      cellStyle.setBorderLeft(BorderStyle.THIN);
      cellStyle.setAlignment(HorizontalAlignment.RIGHT);
      cell.setCellStyle(cellStyle);
      cell.setCellValue(IMPORT_AMOUNT_PAID);
     
      cell = row.createCell(columnCount++);
      cellStyle.setBorderBottom(BorderStyle.THIN);
      cellStyle.setBorderTop(BorderStyle.THIN);
      cellStyle.setBorderRight(BorderStyle.THIN);
      cellStyle.setBorderLeft(BorderStyle.THIN);
      cellStyle.setAlignment(HorizontalAlignment.RIGHT);
      cell.setCellStyle(cellStyle);
      cell.setCellValue(IMPORT_OUTSTANDING_AMOUNT);
     
      cell = row.createCell(columnCount++);
      cellStyle.setBorderBottom(BorderStyle.THIN);
      cellStyle.setBorderTop(BorderStyle.THIN);
      cellStyle.setBorderRight(BorderStyle.THIN);
      cellStyle.setBorderLeft(BorderStyle.THIN);
      cell.setCellStyle(cellStyle);
      cell.setCellValue(FOREIGN_EXCHANGE_OVERLAY);
     
      cell = row.createCell(columnCount);
      cellStyle.setBorderBottom(BorderStyle.THIN);
      cellStyle.setBorderTop(BorderStyle.THIN);
      cellStyle.setBorderRight(BorderStyle.THIN);
      cellStyle.setBorderLeft(BorderStyle.THIN);
      cell.setCellStyle(cellStyle);
      cell.setCellValue(AGEING);
    }
  }
 
  public void generateManualANNEXIIIFile(String sqlQuery, String fileNameWithPath, String frmDate, String toDate)
  {
    Connection connection = null;
    Statement stmt = null;
    ResultSet resultSet = null;
   
    logger.info("generateManualANNEXIIIFile Start...");
    try
    {
      connection = DBConnectionUtility.getConnection();
      stmt = connection.createStatement();
      resultSet = stmt.executeQuery(sqlQuery);
     

      HSSFWorkbook workbook = new HSSFWorkbook();
      HSSFSheet sheet = workbook.createSheet("Report");
      try
      {
        writeHeaderLineANNEXIII(sheet, workbook, frmDate, toDate);
       
        writeHeaderLineANNEXIII1(sheet, workbook);
       
        writeDataLinesANNEXIII(resultSet, workbook, sheet);
       
        FileOutputStream outputStream = new FileOutputStream(fileNameWithPath);
        workbook.write(outputStream);
        outputStream.close();
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
      logger.info("generateManualANNEXIIIFile End...");
    }
    catch (Exception e)
    {
      e.printStackTrace();
      try
      {
        DBConnectionUtility.surrenderDB(connection, stmt, resultSet);
      }
      catch (Exception e)
      {
        logger.info(e.getMessage());
      }
    }
    finally
    {
      try
      {
        DBConnectionUtility.surrenderDB(connection, stmt, resultSet);
      }
      catch (Exception e)
      {
        logger.info(e.getMessage());
      }
    }
  }
 
  private void writeHeaderLineANNEXIII(HSSFSheet sheet, HSSFWorkbook workbook, String frmDate, String toDate)
  {
    Row headerRow = sheet.createRow(0);
    sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 28));
   
    Cell headerCell = headerRow.createCell(0);
    CellStyle cellStyle = workbook.createCellStyle();
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("MTT ANNEXURE III REPORT FROM DATE:" + frmDate + " TO DATE: " + toDate);
  }
 
  private void writeHeaderLineANNEXIII1(HSSFSheet sheet, HSSFWorkbook workbook)
  {
    Row headerRow = sheet.createRow(2);
   
    Cell headerCell = headerRow.createCell(0);
    CellStyle cellStyle = workbook.createCellStyle();
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    cellStyle.setBorderBottom(BorderStyle.THIN);
    cellStyle.setBorderTop(BorderStyle.THIN);
    cellStyle.setBorderRight(BorderStyle.THIN);
    cellStyle.setBorderLeft(BorderStyle.THIN);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("MTT REF");
    headerCell = headerRow.createCell(1);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    cellStyle.setBorderBottom(BorderStyle.THIN);
    cellStyle.setBorderTop(BorderStyle.THIN);
    cellStyle.setBorderRight(BorderStyle.THIN);
    cellStyle.setBorderLeft(BorderStyle.THIN);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("MTT STATUS");
    headerCell = headerRow.createCell(2);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    cellStyle.setBorderBottom(BorderStyle.THIN);
    cellStyle.setBorderTop(BorderStyle.THIN);
    cellStyle.setBorderRight(BorderStyle.THIN);
    cellStyle.setBorderLeft(BorderStyle.THIN);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("TXN REF");
    headerCell = headerRow.createCell(3);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    cellStyle.setBorderBottom(BorderStyle.THIN);
    cellStyle.setBorderTop(BorderStyle.THIN);
    cellStyle.setBorderRight(BorderStyle.THIN);
    cellStyle.setBorderLeft(BorderStyle.THIN);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("RBI REF");
    headerCell = headerRow.createCell(4);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    cellStyle.setBorderBottom(BorderStyle.THIN);
    cellStyle.setBorderTop(BorderStyle.THIN);
    cellStyle.setBorderRight(BorderStyle.THIN);
    cellStyle.setBorderLeft(BorderStyle.THIN);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("NEW GEN REF");
    headerCell = headerRow.createCell(5);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    cellStyle.setBorderBottom(BorderStyle.THIN);
    cellStyle.setBorderTop(BorderStyle.THIN);
    cellStyle.setBorderRight(BorderStyle.THIN);
    cellStyle.setBorderLeft(BorderStyle.THIN);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("AD CODE");
    headerCell = headerRow.createCell(6);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    cellStyle.setBorderBottom(BorderStyle.THIN);
    cellStyle.setBorderTop(BorderStyle.THIN);
    cellStyle.setBorderRight(BorderStyle.THIN);
    cellStyle.setBorderLeft(BorderStyle.THIN);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("BRANCH CODE");
    headerCell = headerRow.createCell(7);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    cellStyle.setBorderBottom(BorderStyle.THIN);
    cellStyle.setBorderTop(BorderStyle.THIN);
    cellStyle.setBorderRight(BorderStyle.THIN);
    cellStyle.setBorderLeft(BorderStyle.THIN);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("BRANCH NAME");
    headerCell = headerRow.createCell(8);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    cellStyle.setBorderBottom(BorderStyle.THIN);
    cellStyle.setBorderTop(BorderStyle.THIN);
    cellStyle.setBorderRight(BorderStyle.THIN);
    cellStyle.setBorderLeft(BorderStyle.THIN);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("REPORTING DATE");
    headerCell = headerRow.createCell(9);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    cellStyle.setBorderBottom(BorderStyle.THIN);
    cellStyle.setBorderTop(BorderStyle.THIN);
    cellStyle.setBorderRight(BorderStyle.THIN);
    cellStyle.setBorderLeft(BorderStyle.THIN);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("CUSTOMER UCIC");
    headerCell = headerRow.createCell(10);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    cellStyle.setBorderBottom(BorderStyle.THIN);
    cellStyle.setBorderTop(BorderStyle.THIN);
    cellStyle.setBorderRight(BorderStyle.THIN);
    cellStyle.setBorderLeft(BorderStyle.THIN);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("CUSTOMER NAME");
    headerCell = headerRow.createCell(11);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    cellStyle.setBorderBottom(BorderStyle.THIN);
    cellStyle.setBorderTop(BorderStyle.THIN);
    cellStyle.setBorderRight(BorderStyle.THIN);
    cellStyle.setBorderLeft(BorderStyle.THIN);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("TRANS DATE");
    headerCell = headerRow.createCell(12);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    cellStyle.setBorderBottom(BorderStyle.THIN);
    cellStyle.setBorderTop(BorderStyle.THIN);
    cellStyle.setBorderRight(BorderStyle.THIN);
    cellStyle.setBorderLeft(BorderStyle.THIN);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("SHIPMENT DATE");
    headerCell = headerRow.createCell(13);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    cellStyle.setBorderBottom(BorderStyle.THIN);
    cellStyle.setBorderTop(BorderStyle.THIN);
    cellStyle.setBorderRight(BorderStyle.THIN);
    cellStyle.setBorderLeft(BorderStyle.THIN);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("VALUE DATE");
    headerCell = headerRow.createCell(14);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    cellStyle.setBorderBottom(BorderStyle.THIN);
    cellStyle.setBorderTop(BorderStyle.THIN);
    cellStyle.setBorderRight(BorderStyle.THIN);
    cellStyle.setBorderLeft(BorderStyle.THIN);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("TRANS CCY");
    headerCell = headerRow.createCell(15);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    cellStyle.setBorderBottom(BorderStyle.THIN);
    cellStyle.setBorderTop(BorderStyle.THIN);
    cellStyle.setBorderRight(BorderStyle.THIN);
    cellStyle.setBorderLeft(BorderStyle.THIN);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("TRANS AMOUNT");
    headerCell = headerRow.createCell(16);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    cellStyle.setBorderBottom(BorderStyle.THIN);
    cellStyle.setBorderTop(BorderStyle.THIN);
    cellStyle.setBorderRight(BorderStyle.THIN);
    cellStyle.setBorderLeft(BorderStyle.THIN);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("OUTSTANDING AMOUNT");
    headerCell = headerRow.createCell(17);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    cellStyle.setBorderBottom(BorderStyle.THIN);
    cellStyle.setBorderTop(BorderStyle.THIN);
    cellStyle.setBorderRight(BorderStyle.THIN);
    cellStyle.setBorderLeft(BorderStyle.THIN);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("PURPOSE CODE");
    headerCell = headerRow.createCell(18);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    cellStyle.setBorderBottom(BorderStyle.THIN);
    cellStyle.setBorderTop(BorderStyle.THIN);
    cellStyle.setBorderRight(BorderStyle.THIN);
    cellStyle.setBorderLeft(BorderStyle.THIN);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("PORT OF SHIPMENT");
    headerCell = headerRow.createCell(19);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    cellStyle.setBorderBottom(BorderStyle.THIN);
    cellStyle.setBorderTop(BorderStyle.THIN);
    cellStyle.setBorderRight(BorderStyle.THIN);
    cellStyle.setBorderLeft(BorderStyle.THIN);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("PORT OF DISCHARGE");
    headerCell = headerRow.createCell(20);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    cellStyle.setBorderBottom(BorderStyle.THIN);
    cellStyle.setBorderTop(BorderStyle.THIN);
    cellStyle.setBorderRight(BorderStyle.THIN);
    cellStyle.setBorderLeft(BorderStyle.THIN);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("DATE OF COMMENCEMENT");
    headerCell = headerRow.createCell(21);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    cellStyle.setBorderBottom(BorderStyle.THIN);
    cellStyle.setBorderTop(BorderStyle.THIN);
    cellStyle.setBorderRight(BorderStyle.THIN);
    cellStyle.setBorderLeft(BorderStyle.THIN);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("COMPLETION DATE");
    headerCell = headerRow.createCell(22);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    cellStyle.setBorderBottom(BorderStyle.THIN);
    cellStyle.setBorderTop(BorderStyle.THIN);
    cellStyle.setBorderRight(BorderStyle.THIN);
    cellStyle.setBorderLeft(BorderStyle.THIN);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("FOREIGN EXCHANGE OVERLAY");
    headerCell = headerRow.createCell(23);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    cellStyle.setBorderBottom(BorderStyle.THIN);
    cellStyle.setBorderTop(BorderStyle.THIN);
    cellStyle.setBorderRight(BorderStyle.THIN);
    cellStyle.setBorderLeft(BorderStyle.THIN);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("AGEING");
    headerCell = headerRow.createCell(24);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    cellStyle.setBorderBottom(BorderStyle.THIN);
    cellStyle.setBorderTop(BorderStyle.THIN);
    cellStyle.setBorderRight(BorderStyle.THIN);
    cellStyle.setBorderLeft(BorderStyle.THIN);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("REMARKS");
    headerCell = headerRow.createCell(25);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    cellStyle.setBorderBottom(BorderStyle.THIN);
    cellStyle.setBorderTop(BorderStyle.THIN);
    cellStyle.setBorderRight(BorderStyle.THIN);
    cellStyle.setBorderLeft(BorderStyle.THIN);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("TRANS AMOUNT USD");
    headerCell = headerRow.createCell(26);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    cellStyle.setBorderBottom(BorderStyle.THIN);
    cellStyle.setBorderTop(BorderStyle.THIN);
    cellStyle.setBorderRight(BorderStyle.THIN);
    cellStyle.setBorderLeft(BorderStyle.THIN);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("OUTSTD AMOUNT USD");
    headerCell = headerRow.createCell(27);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    cellStyle.setBorderBottom(BorderStyle.THIN);
    cellStyle.setBorderTop(BorderStyle.THIN);
    cellStyle.setBorderRight(BorderStyle.THIN);
    cellStyle.setBorderLeft(BorderStyle.THIN);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("INVOICE NUMBER");
    headerCell = headerRow.createCell(28);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    cellStyle.setBorderBottom(BorderStyle.THIN);
    cellStyle.setBorderTop(BorderStyle.THIN);
    cellStyle.setBorderRight(BorderStyle.THIN);
    cellStyle.setBorderLeft(BorderStyle.THIN);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("INVOICE DATE");
  }
 
  private void writeDataLinesANNEXIII(ResultSet result, HSSFWorkbook workbook, HSSFSheet sheet)
    throws SQLException
  {
    int rowCount = 3;
    while (result.next())
    {
      String MTT_REF = result.getString("MTT_REF");
      String MTT_STATUS = result.getString("MTT_STATUS");
      String TXN_REF = result.getString("TXN_REF");
      String RBI_REF = result.getString("RBI_REF");
      String NEW_GEN_REF = result.getString("NEW_GEN_REF");
      String AD_CODE = result.getString("AD_CODE");
      String BRANCH_CODE = result.getString("BRANCH_CODE");
      String BRANCH_NAME = result.getString("BRANCH_NAME");
      String REPORTING_DTE = result.getString("REPORTING_DTE");
      String CUSTOMER_UCIC = result.getString("CUSTOMER_UCIC");
      String CUSTOMER_NAME = result.getString("CUSTOMER_NAME");
      String TRANS_DATE = result.getString("TRANS_DATE");
      String SHIPMENT_DATE = result.getString("SHIPMENT_DATE");
      String VALUE_DATE = result.getString("VALUE_DATE");
      String TRANS_CCY = result.getString("TRANS_CCY");
      String TRANS_AMOUNT = result.getString("TRANS_AMOUNT");
      String OUTSTANDING_AMOUNT = result.getString("OUTSTANDING_AMOUNT");
      String PURPOSE_CODE = result.getString("PURPOSE_CODE");
      String PORT_OF_SHIPMENT = result.getString("PORT_OF_SHIPMENT");
      String PORT_OF_DISCHARGE = result.getString("PORT_OF_DISCHARGE");
      String DATE_OF_COMMENCEMENT = result.getString("DATE_OF_COMMENCEMENT");
      String COMPLETION_DATE = result.getString("COMPLETION_DATE");
      String FOREIGN_EXCHANGE_OVERLAY = result.getString("FOREIGN_EXCHANGE_OVERLAY");
      String AGEING = result.getString("AGEING");
      String REMARKS = result.getString("REMARKS");
      String TRANS_AMOUNT_USD = result.getString("TRANS_AMOUNT_USD");
      String OUTSTD_AMOUNT_USD = result.getString("OUTSTD_AMOUNT_USD");
      String INVOICE_NUMBER = result.getString("INVOICE_NUMBER");
      String INVOICE_DATE = result.getString("INVOICE_DATE");
     

      Row row = sheet.createRow(rowCount++);
     
      int columnCount = 0;
      Cell cell = row.createCell(columnCount++);
      CellStyle cellStyle = workbook.createCellStyle();
      cellStyle.setBorderBottom(BorderStyle.THIN);
      cellStyle.setBorderTop(BorderStyle.THIN);
      cellStyle.setBorderRight(BorderStyle.THIN);
      cellStyle.setBorderLeft(BorderStyle.THIN);
      cell.setCellStyle(cellStyle);
      cell.setCellValue(MTT_REF);
     
      cell = row.createCell(columnCount++);
      cellStyle.setBorderBottom(BorderStyle.THIN);
      cellStyle.setBorderTop(BorderStyle.THIN);
      cellStyle.setBorderRight(BorderStyle.THIN);
      cellStyle.setBorderLeft(BorderStyle.THIN);
      cell.setCellStyle(cellStyle);
      cell.setCellValue(MTT_STATUS);
     
      cell = row.createCell(columnCount++);
      cellStyle.setBorderBottom(BorderStyle.THIN);
      cellStyle.setBorderTop(BorderStyle.THIN);
      cellStyle.setBorderRight(BorderStyle.THIN);
      cellStyle.setBorderLeft(BorderStyle.THIN);
      cell.setCellStyle(cellStyle);
      cell.setCellValue(TXN_REF);
     
      cell = row.createCell(columnCount++);
      cellStyle.setBorderBottom(BorderStyle.THIN);
      cellStyle.setBorderTop(BorderStyle.THIN);
      cellStyle.setBorderRight(BorderStyle.THIN);
      cellStyle.setBorderLeft(BorderStyle.THIN);
      cell.setCellStyle(cellStyle);
      cell.setCellValue(RBI_REF);
     
      cell = row.createCell(columnCount++);
      cellStyle.setBorderBottom(BorderStyle.THIN);
      cellStyle.setBorderTop(BorderStyle.THIN);
      cellStyle.setBorderRight(BorderStyle.THIN);
      cellStyle.setBorderLeft(BorderStyle.THIN);
      cell.setCellStyle(cellStyle);
      cell.setCellValue(NEW_GEN_REF);
     
      cell = row.createCell(columnCount++);
      cellStyle.setBorderBottom(BorderStyle.THIN);
      cellStyle.setBorderTop(BorderStyle.THIN);
      cellStyle.setBorderRight(BorderStyle.THIN);
      cellStyle.setBorderLeft(BorderStyle.THIN);
      cell.setCellStyle(cellStyle);
      cell.setCellValue(AD_CODE);
     
      cell = row.createCell(columnCount++);
      cellStyle.setBorderBottom(BorderStyle.THIN);
      cellStyle.setBorderTop(BorderStyle.THIN);
      cellStyle.setBorderRight(BorderStyle.THIN);
      cellStyle.setBorderLeft(BorderStyle.THIN);
      cell.setCellStyle(cellStyle);
      cell.setCellValue(BRANCH_CODE);
     
      cell = row.createCell(columnCount++);
      cellStyle.setBorderBottom(BorderStyle.THIN);
      cellStyle.setBorderTop(BorderStyle.THIN);
      cellStyle.setBorderRight(BorderStyle.THIN);
      cellStyle.setBorderLeft(BorderStyle.THIN);
      cell.setCellStyle(cellStyle);
      cell.setCellValue(BRANCH_NAME);
     
      cell = row.createCell(columnCount++);
      cellStyle.setBorderBottom(BorderStyle.THIN);
      cellStyle.setBorderTop(BorderStyle.THIN);
      cellStyle.setBorderRight(BorderStyle.THIN);
      cellStyle.setBorderLeft(BorderStyle.THIN);
      cell.setCellStyle(cellStyle);
      cell.setCellValue(REPORTING_DTE);
     
      cell = row.createCell(columnCount++);
      cellStyle.setBorderBottom(BorderStyle.THIN);
      cellStyle.setBorderTop(BorderStyle.THIN);
      cellStyle.setBorderRight(BorderStyle.THIN);
      cellStyle.setBorderLeft(BorderStyle.THIN);
      cell.setCellStyle(cellStyle);
      cell.setCellValue(CUSTOMER_UCIC);
     
      cell = row.createCell(columnCount++);
      cellStyle.setBorderBottom(BorderStyle.THIN);
      cellStyle.setBorderTop(BorderStyle.THIN);
      cellStyle.setBorderRight(BorderStyle.THIN);
      cellStyle.setBorderLeft(BorderStyle.THIN);
      cell.setCellStyle(cellStyle);
      cell.setCellValue(CUSTOMER_NAME);
     
      cell = row.createCell(columnCount++);
      cellStyle.setBorderBottom(BorderStyle.THIN);
      cellStyle.setBorderTop(BorderStyle.THIN);
      cellStyle.setBorderRight(BorderStyle.THIN);
      cellStyle.setBorderLeft(BorderStyle.THIN);
      cell.setCellStyle(cellStyle);
      cell.setCellValue(TRANS_DATE);
     
      cell = row.createCell(columnCount++);
      cellStyle.setBorderBottom(BorderStyle.THIN);
      cellStyle.setBorderTop(BorderStyle.THIN);
      cellStyle.setBorderRight(BorderStyle.THIN);
      cellStyle.setBorderLeft(BorderStyle.THIN);
      cell.setCellStyle(cellStyle);
      cell.setCellValue(SHIPMENT_DATE);
     
      cell = row.createCell(columnCount++);
      cellStyle.setBorderBottom(BorderStyle.THIN);
      cellStyle.setBorderTop(BorderStyle.THIN);
      cellStyle.setBorderRight(BorderStyle.THIN);
      cellStyle.setBorderLeft(BorderStyle.THIN);
      cell.setCellStyle(cellStyle);
      cell.setCellValue(VALUE_DATE);
     
      cell = row.createCell(columnCount++);
      cellStyle.setBorderBottom(BorderStyle.THIN);
      cellStyle.setBorderTop(BorderStyle.THIN);
      cellStyle.setBorderRight(BorderStyle.THIN);
      cellStyle.setBorderLeft(BorderStyle.THIN);
      cell.setCellStyle(cellStyle);
      cell.setCellValue(TRANS_CCY);
     
      cell = row.createCell(columnCount++);
      cellStyle.setBorderBottom(BorderStyle.THIN);
      cellStyle.setBorderTop(BorderStyle.THIN);
      cellStyle.setBorderRight(BorderStyle.THIN);
      cellStyle.setBorderLeft(BorderStyle.THIN);
      cellStyle.setAlignment(HorizontalAlignment.RIGHT);
      cell.setCellStyle(cellStyle);
      cell.setCellValue(TRANS_AMOUNT);
     
      cell = row.createCell(columnCount++);
      cellStyle.setBorderBottom(BorderStyle.THIN);
      cellStyle.setBorderTop(BorderStyle.THIN);
      cellStyle.setBorderRight(BorderStyle.THIN);
      cellStyle.setBorderLeft(BorderStyle.THIN);
      cellStyle.setAlignment(HorizontalAlignment.RIGHT);
      cell.setCellStyle(cellStyle);
      cell.setCellValue(OUTSTANDING_AMOUNT);
     
      cell = row.createCell(columnCount++);
      cellStyle.setBorderBottom(BorderStyle.THIN);
      cellStyle.setBorderTop(BorderStyle.THIN);
      cellStyle.setBorderRight(BorderStyle.THIN);
      cellStyle.setBorderLeft(BorderStyle.THIN);
      cell.setCellStyle(cellStyle);
      cell.setCellValue(PURPOSE_CODE);
     
      cell = row.createCell(columnCount++);
      cellStyle.setBorderBottom(BorderStyle.THIN);
      cellStyle.setBorderTop(BorderStyle.THIN);
      cellStyle.setBorderRight(BorderStyle.THIN);
      cellStyle.setBorderLeft(BorderStyle.THIN);
      cell.setCellStyle(cellStyle);
      cell.setCellValue(PORT_OF_SHIPMENT);
     
      cell = row.createCell(columnCount++);
      cellStyle.setBorderBottom(BorderStyle.THIN);
      cellStyle.setBorderTop(BorderStyle.THIN);
      cellStyle.setBorderRight(BorderStyle.THIN);
      cellStyle.setBorderLeft(BorderStyle.THIN);
      cell.setCellStyle(cellStyle);
      cell.setCellValue(PORT_OF_DISCHARGE);
     
      cell = row.createCell(columnCount++);
      cellStyle.setBorderBottom(BorderStyle.THIN);
      cellStyle.setBorderTop(BorderStyle.THIN);
      cellStyle.setBorderRight(BorderStyle.THIN);
      cellStyle.setBorderLeft(BorderStyle.THIN);
      cell.setCellStyle(cellStyle);
      cell.setCellValue(DATE_OF_COMMENCEMENT);
     
      cell = row.createCell(columnCount++);
      cellStyle.setBorderBottom(BorderStyle.THIN);
      cellStyle.setBorderTop(BorderStyle.THIN);
      cellStyle.setBorderRight(BorderStyle.THIN);
      cellStyle.setBorderLeft(BorderStyle.THIN);
      cell.setCellStyle(cellStyle);
      cell.setCellValue(COMPLETION_DATE);
     
      cell = row.createCell(columnCount++);
      cellStyle.setBorderBottom(BorderStyle.THIN);
      cellStyle.setBorderTop(BorderStyle.THIN);
      cellStyle.setBorderRight(BorderStyle.THIN);
      cellStyle.setBorderLeft(BorderStyle.THIN);
      cell.setCellStyle(cellStyle);
      cell.setCellValue(FOREIGN_EXCHANGE_OVERLAY);
     
      cell = row.createCell(columnCount++);
      cellStyle.setBorderBottom(BorderStyle.THIN);
      cellStyle.setBorderTop(BorderStyle.THIN);
      cellStyle.setBorderRight(BorderStyle.THIN);
      cellStyle.setBorderLeft(BorderStyle.THIN);
      cell.setCellStyle(cellStyle);
      cell.setCellValue(AGEING);
     
      cell = row.createCell(columnCount++);
      cellStyle.setBorderBottom(BorderStyle.THIN);
      cellStyle.setBorderTop(BorderStyle.THIN);
      cellStyle.setBorderRight(BorderStyle.THIN);
      cellStyle.setBorderLeft(BorderStyle.THIN);
      cell.setCellStyle(cellStyle);
      cell.setCellValue(REMARKS);
     
      cell = row.createCell(columnCount++);
      cellStyle.setBorderBottom(BorderStyle.THIN);
      cellStyle.setBorderTop(BorderStyle.THIN);
      cellStyle.setBorderRight(BorderStyle.THIN);
      cellStyle.setBorderLeft(BorderStyle.THIN);
      cellStyle.setAlignment(HorizontalAlignment.RIGHT);
      cell.setCellStyle(cellStyle);
      cell.setCellValue(TRANS_AMOUNT_USD);
     
      cell = row.createCell(columnCount++);
      cellStyle.setBorderBottom(BorderStyle.THIN);
      cellStyle.setBorderTop(BorderStyle.THIN);
      cellStyle.setBorderRight(BorderStyle.THIN);
      cellStyle.setBorderLeft(BorderStyle.THIN);
      cellStyle.setAlignment(HorizontalAlignment.RIGHT);
      cell.setCellStyle(cellStyle);
      cell.setCellValue(OUTSTD_AMOUNT_USD);
     
      cell = row.createCell(columnCount++);
      cellStyle.setBorderBottom(BorderStyle.THIN);
      cellStyle.setBorderTop(BorderStyle.THIN);
      cellStyle.setBorderRight(BorderStyle.THIN);
      cellStyle.setBorderLeft(BorderStyle.THIN);
      cell.setCellStyle(cellStyle);
      cell.setCellValue(INVOICE_NUMBER);
     
      cell = row.createCell(columnCount);
      cellStyle.setBorderBottom(BorderStyle.THIN);
      cellStyle.setBorderTop(BorderStyle.THIN);
      cellStyle.setBorderRight(BorderStyle.THIN);
      cellStyle.setBorderLeft(BorderStyle.THIN);
      cell.setCellStyle(cellStyle);
      cell.setCellValue(INVOICE_DATE);
    }
  }
 
  public void generateManualANNEXRBIFile(String sqlQuery, String fileNameWithPath, String frmDate, String toDate)
  {
    Connection connection = null;
    Statement stmt = null;
    ResultSet resultSet = null;
   
    logger.info("generateManualANNEXRBIFile Start...");
    try
    {
      connection = DBConnectionUtility.getConnection();
      stmt = connection.createStatement();
      resultSet = stmt.executeQuery(sqlQuery);
     

      Workbook workbook = new HSSFWorkbook();
      logger.info("generateManualANNEXRBIFile Start..111.");
      Sheet sheet = workbook.createSheet("Report");
      CellStyle cellStyle = workbook.createCellStyle();
      try
      {
        writeHeaderLineANNEXRBI(sheet, workbook, frmDate, toDate);
       
        writeHeaderLineANNEXRBI1(sheet, workbook, cellStyle);
       
        writeDataLinesANNEXRBI(resultSet, workbook, sheet, cellStyle);
       
        FileOutputStream outputStream = new FileOutputStream(fileNameWithPath);
        workbook.write(outputStream);
        outputStream.close();
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
      logger.info("generateManualANNEXRBIFile End...");
    }
    catch (Exception e)
    {
      e.printStackTrace();
      try
      {
        DBConnectionUtility.surrenderDB(connection, stmt, resultSet);
      }
      catch (Exception e)
      {
        logger.info(e.getMessage());
      }
    }
    finally
    {
      try
      {
        DBConnectionUtility.surrenderDB(connection, stmt, resultSet);
      }
      catch (Exception e)
      {
        logger.info(e.getMessage());
      }
    }
  }
 
  private void writeHeaderLineANNEXRBI(Sheet sheet, Workbook workbook, String frmDate, String toDate)
  {
    logger.info("writeHeaderLineANNEXRBI Start...");
    Row headerRow = sheet.createRow(0);
    CellRangeAddress cellMerge = new CellRangeAddress(0, 0, 0, 23);
    sheet.addMergedRegion(cellMerge);
    setBordersToMergedCells(sheet, cellMerge);
   
    Cell headerCell = headerRow.createCell(0);
    CellStyle cellStyle = workbook.createCellStyle();
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    headerCell.setCellStyle(cellStyle);
   
    headerCell.setCellValue("MTT ANNEXURE RBI REPORT FROM DATE:" + frmDate + " TO DATE: " + toDate);
    logger.info("writeHeaderLineANNEXRBI End...");
  }
 
  protected void setBordersToMergedCells(Sheet sheet, CellRangeAddress rangeAddress)
  {
    RegionUtil.setBorderTop(BorderStyle.THIN, rangeAddress, sheet);
    RegionUtil.setBorderLeft(BorderStyle.THIN, rangeAddress, sheet);
    RegionUtil.setBorderRight(BorderStyle.THIN, rangeAddress, sheet);
    RegionUtil.setBorderBottom(BorderStyle.THIN, rangeAddress, sheet);
  }
 
  private void writeHeaderLineANNEXRBI1(Sheet sheet, Workbook workbook, CellStyle cellStyle)
  {
    logger.info("writeHeaderLineANNEXRBI1 Start...");
    Row headerRow = sheet.createRow(2);
   
    Cell headerCell = headerRow.createCell(0);
   
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    setCellStyleAllBorders(cellStyle);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("MTT REF");
    headerCell = headerRow.createCell(1);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    setCellStyleAllBorders(cellStyle);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("MTT STATUS");
    headerCell = headerRow.createCell(2);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    setCellStyleAllBorders(cellStyle);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("SOL ID");
    headerCell = headerRow.createCell(3);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    setCellStyleAllBorders(cellStyle);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("SOL NAME");
    headerCell = headerRow.createCell(4);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    setCellStyleAllBorders(cellStyle);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("CUSTOMER NAME");
    headerCell = headerRow.createCell(5);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    setCellStyleAllBorders(cellStyle);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("CIF NO");
    headerCell = headerRow.createCell(6);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    setCellStyleAllBorders(cellStyle);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("IMPORT BILL ID");
    headerCell = headerRow.createCell(7);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    setCellStyleAllBorders(cellStyle);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("IMPORT COUNTERPARTY");
    headerCell = headerRow.createCell(8);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    setCellStyleAllBorders(cellStyle);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("IMPORT PAYMENT DATE");
    headerCell = headerRow.createCell(9);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    setCellStyleAllBorders(cellStyle);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("IMPORT SHIPMENT DATE");
    headerCell = headerRow.createCell(10);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    setCellStyleAllBorders(cellStyle);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("IMPORT CCY");
    headerCell = headerRow.createCell(11);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    setCellStyleAllBorders(cellStyle);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("IMPORT BILL AMOUNT");
    headerCell = headerRow.createCell(12);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    setCellStyleAllBorders(cellStyle);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("IMPORT BILL AMOUNT USD");
    headerCell = headerRow.createCell(13);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    setCellStyleAllBorders(cellStyle);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("EXPORT BILL ID");
    headerCell = headerRow.createCell(14);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    setCellStyleAllBorders(cellStyle);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("EXPORT PAYMENT DATE");
    headerCell = headerRow.createCell(15);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    setCellStyleAllBorders(cellStyle);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("EXPORT SHIPMENT DATE");
    headerCell = headerRow.createCell(16);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    setCellStyleAllBorders(cellStyle);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("EXPORT REALISATION DATE");
    headerCell = headerRow.createCell(17);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    setCellStyleAllBorders(cellStyle);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("EXPORT CCY");
    headerCell = headerRow.createCell(18);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    setCellStyleAllBorders(cellStyle);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("EXPORT BILL AMOUNT");
    headerCell = headerRow.createCell(19);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    setCellStyleAllBorders(cellStyle);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("EXPORT BILL AMOUNT USD");
    headerCell = headerRow.createCell(20);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    setCellStyleAllBorders(cellStyle);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("EXPORT COUNTERPARTY");
    headerCell = headerRow.createCell(21);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    setCellStyleAllBorders(cellStyle);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("TOTAL TIME FOR OUTLAY FOREIGN EXCHANGE");
    headerCell = headerRow.createCell(22);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    setCellStyleAllBorders(cellStyle);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("TOTAL TIME TAKEN FOR COMPLETING MTT");
    headerCell = headerRow.createCell(23);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    setCellStyleAllBorders(cellStyle);
    headerCell.setCellStyle(cellStyle);
    headerCell.setCellValue("REMARKS");
    logger.info("writeHeaderLineANNEXRBI1 End...");
  }
 
  private void writeDataLinesANNEXRBI(ResultSet result, Workbook workbook, Sheet sheet, CellStyle cellStyle)
    throws SQLException
  {
    logger.info("writeDataLinesANNEXRBI Start...");
    int rowCount = 3;
    double IMPORT_BILL_AMOUNT_USD_TOTAL = 0.0D;
    double EXPORT_BILL_AMOUNT_USD_TOTAL = 0.0D;
    String PREV_MTT_REF = "";
    double PREV_IMPORT_BILL_AMOUNT_USD = 0.0D;
    double PREV_EXPORT_BILL_AMOUNT_USD = 0.0D;
    CommonMethods commonMethods = null;
    while (result.next()) {
      try
      {
        commonMethods = new CommonMethods();
        String MTT_REF = result.getString("MTT_REF");
        String MTT_STATUS = result.getString("MTT_STATUS");
        String SOL_ID = result.getString("SOL_ID");
        String SOL_NAME = result.getString("SOL_NAME");
        String CUSTOMER_NAME = result.getString("CUSTOMER_NAME");
        String CIF_NO = result.getString("CIF_NO");
        String IMPORT_BILL_ID = result.getString("IMPORT_BILL_ID");
        String IMPORT_COUNTERPARTY = result.getString("IMPORT_COUNTERPARTY");
        String IMPORT_PAYMENT_DATE = result.getString("IMPORT_PAYMENT_DATE");
        String IMPORT_SHIPMENT_DATE = result.getString("IMPORT_SHIPMENT_DATE");
        String IMPORT_CCY = result.getString("IMPORT_CCY");
        String IMPORT_BILL_AMOUNT = result.getString("IMPORT_BILL_AMOUNT");
        String IMPORT_BILL_AMOUNT_USD = result.getString("IMPORT_BILL_AMOUNT_USD");
        String EXPORT_BILL_ID = result.getString("EXPORT_BILL_ID");
        String EXPORT_PAYMENT_DATE = result.getString("EXPORT_PAYMENT_DATE");
        String EXPORT_SHIPMENT_DATE = result.getString("EXPORT_SHIPMENT_DATE");
        String EXPORT_REALISATION_DATE = result.getString("EXPORT_REALISATION_DATE");
        String EXPORT_CCY = result.getString("EXPORT_CCY");
        String EXPORT_BILL_AMOUNT = result.getString("EXPORT_BILL_AMOUNT");
        String EXPORT_BILL_AMOUNT_USD = result.getString("EXPORT_BILL_AMOUNT_USD");
        String EXPORT_COUNTERPARTY = result.getString("EXPORT_COUNTERPARTY");
        String TOTAL_TIME_FOR_OUTLAY_FOREIGN_EXCHANGE = result.getString("TOTAL_TIME_FOR_OUTLAY_FOREIGN_EXCHANGE");
        String TOTAL_TIME_TAKEN_FOR_COMPLETING_MTT = result.getString("TOTAL_TIME_TAKEN_FOR_COMPLETING_MTT");
        String REMARKS = result.getString("REMARKS");
        if ((!PREV_MTT_REF.equalsIgnoreCase(MTT_REF)) && (rowCount > 3))
        {
          IMPORT_BILL_AMOUNT_USD_TOTAL += PREV_IMPORT_BILL_AMOUNT_USD;
          EXPORT_BILL_AMOUNT_USD_TOTAL += PREV_EXPORT_BILL_AMOUNT_USD;
          addTotalAmountRow(workbook, sheet, cellStyle, rowCount, IMPORT_BILL_AMOUNT_USD_TOTAL, EXPORT_BILL_AMOUNT_USD_TOTAL);
          IMPORT_BILL_AMOUNT_USD_TOTAL = 0.0D;
          EXPORT_BILL_AMOUNT_USD_TOTAL = 0.0D;
          PREV_IMPORT_BILL_AMOUNT_USD = 0.0D;
          PREV_EXPORT_BILL_AMOUNT_USD = 0.0D;
          rowCount++;
        }
        if (PREV_MTT_REF.equalsIgnoreCase(MTT_REF))
        {
          try
          {
            IMPORT_BILL_AMOUNT_USD_TOTAL += PREV_IMPORT_BILL_AMOUNT_USD;
            EXPORT_BILL_AMOUNT_USD_TOTAL += PREV_EXPORT_BILL_AMOUNT_USD;
          }
          catch (Exception exce)
          {
            exce.printStackTrace();
          }
        }
        else
        {
          IMPORT_BILL_AMOUNT_USD_TOTAL = PREV_IMPORT_BILL_AMOUNT_USD;
          EXPORT_BILL_AMOUNT_USD_TOTAL = PREV_EXPORT_BILL_AMOUNT_USD;
        }
        Row row = sheet.createRow(rowCount++);
        int columnCount = 0;
        Cell cell = row.createCell(columnCount++);
        setCellStyleAllBorders(cellStyle);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(MTT_REF);
       
        cell = row.createCell(columnCount++);
        setCellStyleAllBorders(cellStyle);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(MTT_STATUS);
       
        cell = row.createCell(columnCount++);
        setCellStyleAllBorders(cellStyle);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(SOL_ID);
       
        cell = row.createCell(columnCount++);
        setCellStyleAllBorders(cellStyle);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(SOL_NAME);
       
        cell = row.createCell(columnCount++);
        setCellStyleAllBorders(cellStyle);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(CUSTOMER_NAME);
       
        cell = row.createCell(columnCount++);
        setCellStyleAllBorders(cellStyle);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(CIF_NO);
       
        cell = row.createCell(columnCount++);
        setCellStyleAllBorders(cellStyle);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(IMPORT_BILL_ID);
       
        cell = row.createCell(columnCount++);
        setCellStyleAllBorders(cellStyle);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(IMPORT_COUNTERPARTY);
       
        cell = row.createCell(columnCount++);
        setCellStyleAllBorders(cellStyle);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(IMPORT_PAYMENT_DATE);
       
        cell = row.createCell(columnCount++);
        setCellStyleAllBorders(cellStyle);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(IMPORT_SHIPMENT_DATE);
       
        cell = row.createCell(columnCount++);
        setCellStyleAllBorders(cellStyle);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(IMPORT_CCY);
       
        cell = row.createCell(columnCount++);
        setCellStyleAllBorders(cellStyle);
        cellStyle.setAlignment(HorizontalAlignment.RIGHT);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(IMPORT_BILL_AMOUNT);
       
        cell = row.createCell(columnCount++);
        setCellStyleAllBorders(cellStyle);
        cellStyle.setAlignment(HorizontalAlignment.RIGHT);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(IMPORT_BILL_AMOUNT_USD);
       
        cell = row.createCell(columnCount++);
        setCellStyleAllBorders(cellStyle);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(EXPORT_BILL_ID);
       
        cell = row.createCell(columnCount++);
        setCellStyleAllBorders(cellStyle);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(EXPORT_PAYMENT_DATE);
       
        cell = row.createCell(columnCount++);
        setCellStyleAllBorders(cellStyle);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(EXPORT_SHIPMENT_DATE);
       
        cell = row.createCell(columnCount++);
        setCellStyleAllBorders(cellStyle);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(EXPORT_REALISATION_DATE);
       
        cell = row.createCell(columnCount++);
        setCellStyleAllBorders(cellStyle);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(EXPORT_CCY);
       
        cell = row.createCell(columnCount++);
        cellStyle.setAlignment(HorizontalAlignment.RIGHT);
        setCellStyleAllBorders(cellStyle);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(EXPORT_BILL_AMOUNT);
       
        cell = row.createCell(columnCount++);
        cellStyle.setAlignment(HorizontalAlignment.RIGHT);
        setCellStyleAllBorders(cellStyle);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(EXPORT_BILL_AMOUNT_USD);
       
        cell = row.createCell(columnCount++);
        setCellStyleAllBorders(cellStyle);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(EXPORT_COUNTERPARTY);
       
        cell = row.createCell(columnCount++);
        setCellStyleAllBorders(cellStyle);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(TOTAL_TIME_FOR_OUTLAY_FOREIGN_EXCHANGE);
       
        cell = row.createCell(columnCount++);
        setCellStyleAllBorders(cellStyle);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(TOTAL_TIME_TAKEN_FOR_COMPLETING_MTT);
       
        cell = row.createCell(columnCount++);
        setCellStyleAllBorders(cellStyle);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(REMARKS);
       
        PREV_MTT_REF = MTT_REF;
        PREV_IMPORT_BILL_AMOUNT_USD = commonMethods.toDouble(IMPORT_BILL_AMOUNT_USD);
        PREV_EXPORT_BILL_AMOUNT_USD = commonMethods.toDouble(EXPORT_BILL_AMOUNT_USD);
      }
      catch (Exception exc)
      {
        logger.info("writeDataLinesANNEXRBI End..." + exc.getMessage());
      }
    }
    IMPORT_BILL_AMOUNT_USD_TOTAL += PREV_IMPORT_BILL_AMOUNT_USD;
    EXPORT_BILL_AMOUNT_USD_TOTAL += PREV_EXPORT_BILL_AMOUNT_USD;
    addTotalAmountRow(workbook, sheet, cellStyle, rowCount++, IMPORT_BILL_AMOUNT_USD_TOTAL, EXPORT_BILL_AMOUNT_USD_TOTAL);
    logger.info("writeDataLinesANNEXRBI End...");
  }
 
  private void addTotalAmountRow(Workbook workbook, Sheet sheet, CellStyle cellStyle, int rowCount, double IMPORT_BILL_AMOUNT_USD_TOTAL, double EXPORT_BILL_AMOUNT_USD_TOTAL)
    throws SQLException
  {
    try
    {
      Row row = sheet.createRow(rowCount);
     
      Cell cell = row.createCell(0);
      setCellStyleAllBorders(cellStyle);
      cell.setCellStyle(cellStyle);
      cell.setCellValue("");
      cell = row.createCell(1);
      setCellStyleAllBorders(cellStyle);
      cell.setCellStyle(cellStyle);
      cell.setCellValue("");
      cell = row.createCell(2);
      setCellStyleAllBorders(cellStyle);
      cell.setCellStyle(cellStyle);
      cell.setCellValue("");
      cell = row.createCell(3);
      setCellStyleAllBorders(cellStyle);
      cell.setCellStyle(cellStyle);
      cell.setCellValue("");
      cell = row.createCell(4);
      setCellStyleAllBorders(cellStyle);
      cell.setCellStyle(cellStyle);
      cell.setCellValue("");
      cell = row.createCell(5);
      setCellStyleAllBorders(cellStyle);
      cell.setCellStyle(cellStyle);
      cell.setCellValue("");
      cell = row.createCell(6);
      setCellStyleAllBorders(cellStyle);
      cell.setCellStyle(cellStyle);
      cell.setCellValue("");
      cell = row.createCell(7);
      setCellStyleAllBorders(cellStyle);
      cell.setCellStyle(cellStyle);
      cell.setCellValue("");
      cell = row.createCell(8);
      setCellStyleAllBorders(cellStyle);
      cell.setCellStyle(cellStyle);
      cell.setCellValue("");
      cell = row.createCell(9);
      setCellStyleAllBorders(cellStyle);
      cell.setCellStyle(cellStyle);
      cell.setCellValue("");
      cell = row.createCell(10);
      setCellStyleAllBorders(cellStyle);
      cell.setCellStyle(cellStyle);
      cell.setCellValue("");
      cell = row.createCell(11);
      setCellStyleAllBorders(cellStyle);
      cell.setCellStyle(cellStyle);
      cell.setCellValue("");
      cell = row.createCell(12);
      cellStyle.setAlignment(HorizontalAlignment.RIGHT);
      setCellStyleAllBorders(cellStyle);
      cell.setCellStyle(cellStyle);
      cell.setCellValue(IMPORT_BILL_AMOUNT_USD_TOTAL);
      cell = row.createCell(13);
      setCellStyleAllBorders(cellStyle);
      cell.setCellStyle(cellStyle);
      cell.setCellValue("");
      cell = row.createCell(14);
      setCellStyleAllBorders(cellStyle);
      cell.setCellStyle(cellStyle);
      cell.setCellValue("");
      cell = row.createCell(15);
      setCellStyleAllBorders(cellStyle);
      cell.setCellStyle(cellStyle);
      cell.setCellValue("");
      cell = row.createCell(16);
      setCellStyleAllBorders(cellStyle);
      cell.setCellStyle(cellStyle);
      cell.setCellValue("");
      cell = row.createCell(17);
      setCellStyleAllBorders(cellStyle);
      cell.setCellStyle(cellStyle);
      cell.setCellValue("");
      cell = row.createCell(18);
      setCellStyleAllBorders(cellStyle);
      cell.setCellStyle(cellStyle);
      cell.setCellValue("");
      cell = row.createCell(19);
      cellStyle.setAlignment(HorizontalAlignment.RIGHT);
      setCellStyleAllBorders(cellStyle);
      cell.setCellStyle(cellStyle);
      cell.setCellValue(EXPORT_BILL_AMOUNT_USD_TOTAL);
      cell = row.createCell(20);
      setCellStyleAllBorders(cellStyle);
      cell.setCellStyle(cellStyle);
      cell.setCellValue("");
      cell = row.createCell(21);
      setCellStyleAllBorders(cellStyle);
      cell.setCellStyle(cellStyle);
      cell.setCellValue("");
      cell = row.createCell(22);
      setCellStyleAllBorders(cellStyle);
      cell.setCellStyle(cellStyle);
      cell.setCellValue("");
      cell = row.createCell(23);
      setCellStyleAllBorders(cellStyle);
     



      cell.setCellStyle(cellStyle);
      cell.setCellValue("");
    }
    catch (Exception exc)
    {
      logger.info("addTotalAmountRow End..." + exc.getMessage());
    }
  }
 
  private void setCellStyleAllBorders(CellStyle cellStyle)
  {
    cellStyle.setBorderBottom(BorderStyle.THIN);
    cellStyle.setBorderLeft(BorderStyle.THIN);
    cellStyle.setBorderRight(BorderStyle.THIN);
    cellStyle.setBorderTop(BorderStyle.THIN);
  }
 
  public void downloadFiles(String filePath)
  {
    try
    {
      logger.info("File Path----->" + filePath);
     
      File fileToDownload = new File(filePath);
      this.inputStream = new FileInputStream(fileToDownload);
      this.fileName = fileToDownload.getName();
      this.contentLength = fileToDownload.length();
    }
    catch (Exception e)
    {
      logger.info("Exception in download Files--->" + e.getMessage());
    }
  }
 
  public static String returnSpaceifNull(String value)
  {
    try
    {
      if (value == null) {
        value = " ";
      } else if (value.trim().equalsIgnoreCase("")) {
        value = " ";
      } else {
        value = value.trim();
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return value;
  }
 
  public String closeUrl()
    throws Exception
  {
    MTTHomeBD rbiBD = null;
    try
    {
      rbiBD = new MTTHomeBD();
      this.url = rbiBD.getCloseUrl();
    }
    catch (Exception e)
    {
      logger.info(e);
      e.printStackTrace();
    }
    return "redirect";
  }
}
