package in.co.localization.action;

import com.opensymphony.xwork2.ActionContext;
import in.co.localization.dao.exception.ApplicationException;
import in.co.localization.vo.localization.EodDownloadVO;
import in.co.localization.vo.localization.FileUtitlityVO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class FileUtilityAction
  extends LocalizationBaseAction
  implements ActionConstantsQuery, ActionConstants
{
  private static Logger logger = Logger.getLogger(FileUtilityAction.class.getName());
  private static final long serialVersionUID = 1L;
  private InputStream inputStream;
  private String fileName;
  private long contentLength;
  String cifId = null;
  String fileUtilFileName = null;
  String idpmsFileType = null;
  String fileUtilFromDate = null;
  String fileUtilToDate = null;
  String fileNameRef;
  FileUtitlityVO fileVO = null;
  File inputFile;
  String fileUtilName;
  String mstReference;
  String fileUtilMasterRef = null;
  String fileUtilBrcNO = null;
  String fileUtilityUid = null;
  String fileUtilityCifId = null;
  List<String> filesListInDir = new ArrayList();
  ArrayList<FileUtitlityVO> fileUtltList = null;
  private EodDownloadVO vo;
 
  public String getCifId()
  {
    return this.cifId;
  }
 
  public void setCifId(String cifId)
  {
    this.cifId = cifId;
  }
 
  public String getFileUtilName()
  {
    return this.fileUtilName;
  }
 
  public void setFileUtilName(String fileUtilName)
  {
    this.fileUtilName = fileUtilName;
  }
 
  public String getMstReference()
  {
    return this.mstReference;
  }
 
  public void setMstReference(String mstReference)
  {
    this.mstReference = mstReference;
  }
 
  public EodDownloadVO getVo()
  {
    return this.vo;
  }
 
  public void setVo(EodDownloadVO vo)
  {
    this.vo = vo;
  }
 
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
 
  public String getFileUtilFileName()
  {
    return this.fileUtilFileName;
  }
 
  public void setFileUtilFileName(String fileUtilFileName)
  {
    this.fileUtilFileName = fileUtilFileName;
  }
 
  public String getIdpmsFileType()
  {
    return this.idpmsFileType;
  }
 
  public void setIdpmsFileType(String idpmsFileType)
  {
    this.idpmsFileType = idpmsFileType;
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
 
  public String getFileNameRef()
  {
    return this.fileNameRef;
  }
 
  public void setFileNameRef(String fileNameRef)
  {
    this.fileNameRef = fileNameRef;
  }
 
  public FileUtitlityVO getFileVO()
  {
    return this.fileVO;
  }
 
  public void setFileVO(FileUtitlityVO fileVO)
  {
    this.fileVO = fileVO;
  }
 
  public File getInputFile()
  {
    return this.inputFile;
  }
 
  public void setInputFile(File inputFile)
  {
    this.inputFile = inputFile;
  }
 
  public String getFileUtilMasterRef()
  {
    return this.fileUtilMasterRef;
  }
 
  public void setFileUtilMasterRef(String fileUtilMasterRef)
  {
    this.fileUtilMasterRef = fileUtilMasterRef;
  }
 
  public String getFileUtilBrcNO()
  {
    return this.fileUtilBrcNO;
  }
 
  public void setFileUtilBrcNO(String fileUtilBrcNO)
  {
    this.fileUtilBrcNO = fileUtilBrcNO;
  }
 
  public String getFileUtilityUid()
  {
    return this.fileUtilityUid;
  }
 
  public void setFileUtilityUid(String fileUtilityUid)
  {
    this.fileUtilityUid = fileUtilityUid;
  }
 
  public String getFileUtilityCifId()
  {
    return this.fileUtilityCifId;
  }
 
  public void setFileUtilityCifId(String fileUtilityCifId)
  {
    this.fileUtilityCifId = fileUtilityCifId;
  }
 
  public ArrayList<FileUtitlityVO> getFileUtltList()
  {
    return this.fileUtltList;
  }
 
  public void setFileUtltList(ArrayList<FileUtitlityVO> fileUtltList)
  {
    this.fileUtltList = fileUtltList;
  }
 
  public String landingPage()
    throws ApplicationException
  {
    try
    {
      isSessionAvailable();
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    return "success";
  }
 
  public String downloadLandingPage()
    throws IOException
  {
    try
    {
      isSessionAvailable();
    }
    catch (Exception e)
    {
      logger.info("FILE UTIL Download Exception------------>" + e);
    }
    return "success";
  }
 
  public String downloadedpmsLandingPage()
    throws IOException
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
     

      logger.info("sessionUserName--->" + sessionUserName);
     


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
      logger.error("Exception In uploadLandingPage", e);
      e.printStackTrace();
    }
    logger.info("Exiting Method");
    return target;
  }
 
  public String downloadAdvocesLandingPage()
    throws IOException
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
     

      logger.info("sessionUserName--->" + sessionUserName);
     


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
      logger.error("Exception In uploadLandingPage", e);
      e.printStackTrace();
    }
    logger.info("Exiting Method");
    return target;
  }
 
  public String uploadLandingPage()
    throws IOException
  {
    logger.info("Entering Method");
    String sessionUserName = "";
    String target = null;
    int count = 0;
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
     

      logger.info("sessionUserName--->" + sessionUserName);
     
      count = checkLoginedUserType(sessionUserName, "DTUPLOAD");
     
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
      logger.error("Exception In uploadLandingPage", e);
      e.printStackTrace();
    }
    logger.info("Exiting Method");
    return target;
  }
 
  public String uidManagementHomeLandingPage()
    throws IOException
  {
    logger.info("Entering Method");
    String sessionUserName = "";
    String target = null;
    int count = 0;
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
     

      logger.info("sessionUserName--->" + sessionUserName);
     

      count = 1;
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
      logger.error("Exception In uploadLandingPage", e);
      e.printStackTrace();
    }
    logger.info("Exiting Method");
    return target;
  }
 
  public String modifyCIFHomeLandingPage()
    throws IOException
  {
    logger.info("Entering Method");
    String sessionUserName = "";
    String target = null;
    int count = 0;
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
     

      logger.info("sessionUserName--->" + sessionUserName);
     

      count = 1;
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
      logger.error("Exception In uploadLandingPage", e);
      e.printStackTrace();
    }
    logger.info("Exiting Method");
    return target;
  }
 
  public String updateUidMailDataHomeLandingPage()
    throws IOException
  {
    logger.info("Entering Method");
    String sessionUserName = "";
    String target = null;
    int count = 0;
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
     

      logger.info("sessionUserName--->" + sessionUserName);
     

      count = 1;
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
      logger.error("Exception In uploadLandingPage", e);
      e.printStackTrace();
    }
    logger.info("Exiting Method");
    return target;
  }
 
  public String uidWithouCIFHomeLandingPage()
    throws IOException
  {
    logger.info("Entering Method");
    String sessionUserName = "";
    String target = null;
    int count = 0;
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
     

      logger.info("sessionUserName--->" + sessionUserName);
     

      count = 1;
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
      logger.error("Exception In uploadLandingPage", e);
      e.printStackTrace();
    }
    logger.info("Exiting Method");
    return target;
  }
 
  public String cifWithoutMailIdHomeLandingPage()
    throws IOException
  {
    logger.info("Entering Method");
    String sessionUserName = "";
    String target = null;
    int count = 0;
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
     

      logger.info("sessionUserName--->" + sessionUserName);
     

      count = 1;
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
      logger.error("Exception In uploadLandingPage", e);
      e.printStackTrace();
    }
    logger.info("Exiting Method");
    return target;
  }
 
  public String sendMailHomeLandingPage()
    throws IOException
  {
    logger.info("Entering Method");
    String sessionUserName = "";
    String target = null;
    int count = 0;
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
     

      logger.info("sessionUserName--->" + sessionUserName);
     

      count = 1;
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
      logger.error("Exception In uploadLandingPage", e);
      e.printStackTrace();
    }
    logger.info("Exiting Method");
    return target;
  }
 
  public String sendBranchMailHomeLandingPage()
    throws IOException
  {
    logger.info("Entering Method");
    String sessionUserName = "";
    String target = null;
    int count = 0;
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
     

      logger.info("sessionUserName--->" + sessionUserName);
     

      count = 1;
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
      logger.error("Exception In uploadLandingPage", e);
      e.printStackTrace();
    }
    logger.info("Exiting Method");
    return target;
  }
 
  public String mailStsHomeLandingPage()
    throws IOException
  {
    logger.info("Entering Method");
    String sessionUserName = "";
    String target = null;
    int count = 0;
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
     

      logger.info("sessionUserName--->" + sessionUserName);
     

      count = 1;
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
      logger.error("Exception In uploadLandingPage", e);
      e.printStackTrace();
    }
    logger.info("Exiting Method");
    return target;
  }
 
  public int seqGeneration(String sequence)
  {
    int seq = 0;
    Connection con = null;
    LoggableStatement pst = null;
    ResultSet rs = null;
    String seq_query = null;
    try
    {
      con = DBConnectionUtility.getConnection();
      seq_query = "SELECT " + sequence + ".NEXTVAL AS SEQNO FROM DUAL";
      pst = new LoggableStatement(con, seq_query);
      logger.info(pst.getQueryString());
      rs = pst.executeQuery();
      while (rs.next())
      {
        seq = rs.getInt("SEQNO");
        logger.info("Sequence Number----->" + seq);
      }
    }
    catch (Exception e)
    {
      logger.info("Excpetion in SeqGeneration Method" + e.getMessage());
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    return seq;
  }
 
  public String landingPage1()
    throws IOException
  {
    try
    {
      isSessionAvailable();
    }
    catch (Exception e)
    {
      logger.info("File Download Landing Exception------------>" + e);
    }
    return "success";
  }
 
  public String downloadTBMLIFile()
  {
    logger.info("----------TBML ------TBMLDWLD--- [downloadTBMLIFile]- ------------------- :: fileUtilFileName :: " + this.fileUtilFileName + " :: fileUtilFromDate :: " + this.fileUtilFromDate + " :: fileUtilToDate :: " + this.fileUtilToDate);
    Connection con = null;
    LoggableStatement ps = null;
    PreparedStatement pps1 = null;
    ResultSet rs1 = null;
    ResultSet res = null;
    int noOfRec = 0;
   
    String result = "success";
    String fileName1 = "";
    String myFilePath = "";
    try
    {
      con = DBConnectionUtility.getConnection();
     
      String proQuery = "SELECT FILE_DWNLD_SEQNO.NEXTVAL AS FILEDWNLD_SEQNO,TO_CHAR(TO_DATE('" + this.fileUtilFromDate + "','DD/MM/YYYY'),'DD-MM-YY') AS FILEDATAFROMDATE,TO_CHAR(TO_DATE('" + this.fileUtilToDate + "','DD/MM/YYYY'),'DD-MM-YY') AS FILEDATATODATE FROM DUAL";
      pps1 = con.prepareStatement(proQuery);
      rs1 = pps1.executeQuery();
      String prodate = null;
      String fileDataFromDate = null;
      String fileDataToDate = null;
      int seq = 0;
      if (rs1.next())
      {
        seq = rs1.getInt("FILEDWNLD_SEQNO");
        fileDataFromDate = rs1.getString("FILEDATAFROMDATE");
        fileDataToDate = rs1.getString("FILEDATATODATE");
      }
      logger.info("----------TBML ------TBMLIDWLD--- [downloadTBMLOFile]- ------------------- prodate :: " + prodate + " :: seq :: " + seq + " :: fileDataFromDate :: " + fileDataFromDate + " :: fileDataToDate :: " + fileDataToDate);
     
      String queryCsvbill = "SELECT TO_CHAR(TO_DATE(TRAN_DATE,'dd-mm-yy'),'dd-Mon-yyyy') as TRAN_DATE, TRAN_ID, ACCOUNT, CUSTID, CUST_NAME,TXN_TYPE, INWARDOUTWARD, TXN_AMT_INR, PURPOSE_CODE, TXN_AMT_FCNR, BILL_CRNCY_CODE, CONV_RATE, TRAN_PARTICULAR,GOODS_N_EXP, GOODS_N_IMP, TO_CHAR(TO_DATE(IMPORT_DATE,'dd-mm-yy'),'dd-Mon-yyyy') as IMPORT_DATE, TO_CHAR(TO_DATE(REMIT_DATE,'dd-mm-yy'),'dd-Mon-yyyy') as REMIT_DATE,REMITTER_NAME, REMITTER_ADDRESS, REMITTER_COUNTRY, TXN_TYPE_1,MAKER, CHECKER, NOOFAMENDMENTS, FLG, BENEFICIARY_NAME, IRM_BILL_ID, LC_NUMBER, GAP_DAYS, IRM_AMT, TOTAL_IRMLINK_AMT,OUTSTANDING_IRM_AMT, ORM_BILL, BOE_AMT FROM REP_TBML_INWARD_REMITTANCE WHERE TRAN_DATE  BETWEEN '" +
     


        fileDataFromDate + "' AND '" + fileDataToDate + "' ";
     
      String fileLoc1 = "";
      fileLoc1 = ServiceLoggingUtil.getBridgePropertyValue("FILEUTIL");
     
      logger.info("QueryCSV: " + queryCsvbill);
     
      File f = new File(fileLoc1);
      if (!f.exists()) {
        f.mkdir();
      }
      fileName1 = "TBMLINWARD" + seq;
      myFilePath = fileLoc1 + fileName1 + ".txt";
      noOfRec = generateManualTBMLItxtFile(queryCsvbill, myFilePath);
      if (noOfRec > 0)
      {
        downloadFiles(myFilePath);
       
        addActionMessage("File (" + fileName1 + ") Downloaded Successfully..");
      }
      else
      {
        result = "nodatafound";
        addActionError("No Data Found");
      }
    }
    catch (Exception e)
    {
      logger.info("----------TBML ------TBMLIDWLD--- [downloadTBMLIFile]- --------------exception----------------" + e);
      result = "error";
      e.printStackTrace();
    }
    finally
    {
      closeSqlRefferance(rs1, pps1);
      DBConnectionUtility.surrenderDB(con, ps, res);
      logger.info("Finally Occurred in saveDetail");
    }
    return result;
  }
 
  public String downloadTBMLOFile()
  {
    logger.info("----------TBML ------TBMLODWLD--- [downloadTBMLOFile]- ------------------- :: fileUtilFileName :: " + this.fileUtilFileName + " :: fileUtilFromDate :: " + this.fileUtilFromDate + " :: fileUtilToDate :: " + this.fileUtilToDate);
    Connection con = null;
    LoggableStatement ps = null;
    ResultSet rs1 = null;
    PreparedStatement pps1 = null;
    ResultSet res = null;
    int noOfRec = 0;
    String result = "success";
    String fileName1 = "";
    String myFilePath = "";
    try
    {
      con = DBConnectionUtility.getConnection();
     
      String proQuery = "SELECT FILE_DWNLD_SEQNO.NEXTVAL AS FILEDWNLD_SEQNO,TO_CHAR(TO_DATE('" + this.fileUtilFromDate + "','DD/MM/YYYY'),'DD-MM-YY') AS FILEDATAFROMDATE,TO_CHAR(TO_DATE('" + this.fileUtilToDate + "','DD/MM/YYYY'),'DD-MM-YY') AS FILEDATATODATE FROM DUAL";
      pps1 = con.prepareStatement(proQuery);
      rs1 = pps1.executeQuery();
      String prodate = null;
      String fileDataFromDate = null;
      String fileDataToDate = null;
      int seq = 0;
      if (rs1.next())
      {
        seq = rs1.getInt("FILEDWNLD_SEQNO");
        fileDataFromDate = rs1.getString("FILEDATAFROMDATE");
        fileDataToDate = rs1.getString("FILEDATATODATE");
      }
      logger.info("----------TBML ------TBMLODWLD--- [downloadTBMLOFile]- ------------------- prodate :: " + prodate + " :: seq :: " + seq + " :: fileDataFromDate :: " + fileDataFromDate + " :: fileDataToDate :: " + fileDataToDate);
     
      String queryCsvbill = "SELECT TO_CHAR(TO_DATE(TRAN_DATE,'dd-mm-yy'),'dd-MON-yyyy') as TRAN_DATE, TRAN_ID, ACCOUNT, CUSTID, CUST_NAME, TXN_TYPE, INWARDOUTWARD,TXN_AMT_INR, PURPOSE_CODE, TXN_AMT_FCNR, BILL_CRNCY_CODE, CONV_RATE, TRAN_PARTICULAR, GOODS_N_EXP, GOODS_N_IMP,TO_CHAR(TO_DATE(IMPORT_DATE,'dd-mm-yy'),'dd-MON-yyyy') as IMPORT_DATE, TO_CHAR(TO_DATE(REMIT_DATE,'dd-mm-yy'),'dd-MON-yyyy') as REMIT_DATE,REMITTER_NAME, REMITTER_ADDRESS, REMITTER_COUNTRY, TXN_TYPE_1, MAKER, CHECKER, NOOFAMENDMENTS, FLG, BENEFICIARY_NAME, ORM_BILL_ID, LC_NUMBER, BLANK,INVOICE_NUMBER,TO_CHAR(TO_DATE(INVOICE_DATE,'dd-mm-yy'),'dd-MON-yyyy') AS INVOICE_DATE,GAP_DAYS, ORM_AMT, TOTAL_ORMLINK_AMT, OUTSTANDING_ORM_AMT,ORM_BILL, BOE_AMT, BENEFICIARY_COUNTRY FROM REP_TBML_OUTWARD_REMITTANCE WHERE TRAN_DATE BETWEEN '" +
     



        fileDataFromDate + "' AND '" + fileDataToDate + "' ";
     
      String fileLoc1 = "";
      fileLoc1 = ServiceLoggingUtil.getBridgePropertyValue("FILEUTIL");
     
      logger.info("QueryCSV: " + queryCsvbill);
     
      File f = new File(fileLoc1);
      if (!f.exists()) {
        f.mkdir();
      }
      fileName1 = "TBMLOUTWARD" + seq;
      myFilePath = fileLoc1 + fileName1 + ".txt";
      noOfRec = generateManualTBMLOtxtFile(queryCsvbill, myFilePath);
      if (noOfRec > 0)
      {
        downloadFiles(myFilePath);
       
        addActionMessage("File (" + fileName1 + ") Downloaded Successfully..");
      }
      else
      {
        result = "nodatafound";
        addActionError("No Data Found");
      }
    }
    catch (Exception e)
    {
      logger.info("----------TBML ------TBMLODWLD--- [downloadTBMLOFile]- --------------exception----------------" + e);
      result = "error";
      e.printStackTrace();
    }
    finally
    {
      closeSqlRefferance(rs1, pps1);
      DBConnectionUtility.surrenderDB(con, ps, res);
      logger.info("Finally Occurred in saveDetail");
    }
    return result;
  }
 
  public String downloadDRI_INWARDFile()
  {
    logger.info("----------TBML ------TBMLODWLD--- [downloadTBMLOFile]- ------------------- :: fileUtilFileName :: " + this.fileUtilFileName + " :: fileUtilFromDate :: " + this.fileUtilFromDate + " :: fileUtilToDate :: " + this.fileUtilToDate);
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet res = null;
    String result = "success";
    int noOfRec = 0;
    try
    {
      con = DBConnectionUtility.getConnection();
     

      String proQuery = " SELECT INWARDREM_SEQNO.NEXTVAL AS INWARD_SEQNO, TO_CHAR(TO_DATE('" +
        this.fileUtilFromDate + "','DD/MM/YYYY'),'DDMMYYYY') AS PROCDATE," +
        " TO_CHAR(TO_DATE('" + this.fileUtilToDate + "','DD/MM/YYYY'),'DD-MM-YY') AS PROCDATE1 FROM DUAL";
      PreparedStatement pps1 = con.prepareStatement(proQuery);
      ResultSet rs1 = pps1.executeQuery();
      String prodate = null;
      String prodate1 = null;
      int seq = 0;
      if (rs1.next())
      {
        prodate = rs1.getString("PROCDATE");
        prodate1 = rs1.getString("PROCDATE1");
        seq = rs1.getInt("INWARD_SEQNO");
        logger.info(prodate + " :: " + prodate1 + " :: " + seq);
      }
      closeSqlRefferance(rs1, pps1);
     
      String fileLoc = "";
      fileLoc = ServiceLoggingUtil.getBridgePropertyValue("DRIINWARD");
     
      String queryCsv = "SELECT IRMNUMBER, PURPOSECODE, REMITTEENAME, REMITTEEADDR, REMITTEEPAN, REMITTEEGSTIN, REMITTEEAADHAAR, IFSCCODE, REMITTEEBANKAC, REMITTERNAME, REMITTERADDR, SWIFTBIC, REMITTERBANKAC, CURCODE, AMOUNT_FC, AMOUNT_INR, REMITTANCEDT, PAYMENTMODE, SB_DT, SB_NO, REMARKS  FROM REP_DRI_INWARDREMITTANCE_QUERY WHERE REMITTANCEDT1 = '" +
     
        prodate1 + "'";
     
      logger.info(queryCsv);
     
      fileLoc = fileLoc + prodate + "/";
     
      File f = new File(fileLoc);
      if (!f.exists())
      {
        f.mkdir();
        logger.info(Boolean.valueOf(f.mkdir()));
      }
      String fileName = "dribanks_inw_UBIN_" + prodate + seq;
     
      String myFilePath1 = fileLoc + fileName + ".csv";
     
      noOfRec = generateManualDRI_INWARDCSVFile(queryCsv, myFilePath1);
      if (noOfRec > 0)
      {
        downloadFiles(myFilePath1);
       
        addActionMessage("File (" + fileName + ") Downloaded Successfully..");
      }
      else
      {
        result = "nodatafound";
        addActionError("No Data Found");
      }
    }
    catch (Exception e)
    {
      logger.error("Exception in DRI-INWARD Fie---" + e);
      result = "Error";
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, ps, res);
      logger.info("Finally Occurred in saveDetail");
    }
    return result;
  }
 
  public String downloadPRNFile()
  {
    logger.info("----------PRNDWLD--- [downloadPRNFile]- ------------------- :: fileUtilFileName :: " + this.fileUtilFileName + " :: fileUtilFromDate :: " + this.fileUtilFromDate + " :: fileUtilToDate :: " + this.fileUtilToDate);
    Connection con = null;
    PreparedStatement ps = null;
    PreparedStatement psData = null;
    LoggableStatement psInsert = null;
    ResultSet res = null;
    String result = "success";
    int noOfRec = 0;
    CommonMethods commonMethods = null;
    try
    {
      con = DBConnectionUtility.getConnection();
      commonMethods = new CommonMethods();
     

      String prodate = null;
      String prodate1 = null;
      int seq = 0;
      int dataCount = 0;
      String dataCntQuery = "SELECT COUNT(*) AS DATACOUNT FROM ETT_PRN_DATA WHERE PAYMENT_DATE='" + this.fileUtilFromDate + "'";
      PreparedStatement ppsdataCnt = con.prepareStatement(dataCntQuery);
      ResultSet rstdataCnt = ppsdataCnt.executeQuery();
      if (rstdataCnt.next()) {
        dataCount = rstdataCnt.getInt("DATACOUNT");
      }
      closeSqlRefferance(rstdataCnt, ppsdataCnt);
     
      String dateQuery = " SELECT PRNXML_SEQNO.NEXTVAL AS PRN_SEQNO,  TO_CHAR(TO_DATE('" +
        this.fileUtilFromDate + "','DD/MM/YYYY'),'DDMMYYYY') AS PROCDATE," +
        " TO_CHAR(TO_DATE('" + this.fileUtilFromDate + "','DD/MM/YYYY'),'DD-MM-YY') AS PROCDATE1 FROM DUAL";
      PreparedStatement ppsDateQuery = con.prepareStatement(dateQuery);
      ResultSet rsDateQuery = ppsDateQuery.executeQuery();
      if (rsDateQuery.next())
      {
        prodate = rsDateQuery.getString("PROCDATE");
        prodate1 = rsDateQuery.getString("PROCDATE1");
        seq = rsDateQuery.getInt("PRN_SEQNO");
        logger.info(prodate + " :: " + prodate1 + " :: " + seq);
      }
      closeSqlRefferance(rsDateQuery, ppsDateQuery);
      if (dataCount == 0)
      {
        String sqlQuery = "select TO_CHAR(TO_DATE(PAYMENT_DATE, 'dd/mm/yy'),'dd/mm/yyyy') AS PAYMENT_DATE,EXPORT_TYPE, SHIPPING_BILLNO,TO_CHAR(TO_DATE(SHIPPING_BILLDATE, 'dd/mm/yy'),'dd/mm/yyyy') AS SHIPPING_BILLDATE, PORT_CODE,REFERENCE_NO,REALISED_AMOUNT,BILL_STATUS,BANK_CHARGES,SOFTEX_NO,BILL_REF_NO,AD_CODE,IRM_NO, OTHER_BANK_FIRC_REF_NO,CURRENCY,TO_CHAR(TO_DATE(REALIZATON_DATE, 'dd/mm/yy'),'dd/mm/yyyy') AS REALIZATON_DATE, REMITTER_NAME,REMITTER_COUNTRY,EVENT_REF from REP_PRN_NEW_REPORT where PAYMENT_DATE ='" +
       


          prodate1 + "' ORDER BY BILL_STATUS";
       

        psData = con.prepareStatement(sqlQuery);
        logger.info("sqlQuery" + sqlQuery);
        res = psData.executeQuery();
        while (res.next())
        {
          String export_type = commonMethods.getEmptyIfNull(res.getString("EXPORT_TYPE"));
          String shipping_billno = commonMethods.getEmptyIfNull(res.getString("SHIPPING_BILLNO"));
          String port_code = commonMethods.getEmptyIfNull(res.getString("PORT_CODE"));
         
          String realised_amount = commonMethods.getEmptyIfNull(res.getString("REALISED_AMOUNT"));
          String bill_status = commonMethods.getEmptyIfNull(res.getString("BILL_STATUS"));
          String bank_charges = commonMethods.getEmptyIfNull(res.getString("BANK_CHARGES"));
          String softex_no = commonMethods.getEmptyIfNull(res.getString("SOFTEX_NO"));
          String bill_ref_no = commonMethods.getEmptyIfNull(res.getString("BILL_REF_NO"));
          String ad_code = commonMethods.getEmptyIfNull(res.getString("AD_CODE"));
          String irm_no = commonMethods.getEmptyIfNull(res.getString("IRM_NO"));
          String other_bank_firc_ref_no = commonMethods.getEmptyIfNull(res.getString("OTHER_BANK_FIRC_REF_NO"));
          String currency = commonMethods.getEmptyIfNull(res.getString("CURRENCY"));
          String remitter_name = commonMethods.getEmptyIfNull(res.getString("REMITTER_NAME"));
          String remitter_country = commonMethods.getEmptyIfNull(res.getString("REMITTER_COUNTRY"));
          String event_ref = commonMethods.getEmptyIfNull(res.getString("EVENT_REF"));
          String shipping_billdate = commonMethods.getEmptyIfNull(res.getString("SHIPPING_BILLDATE"));
          String realizaton_date = commonMethods.getEmptyIfNull(res.getString("REALIZATON_DATE"));
          String payment_date = commonMethods.getEmptyIfNull(res.getString("PAYMENT_DATE"));
         


          String reference_no = "";
          String squ = "SELECT PRN_SEQ_NO_NEW('" + prodate + "') AS PAY_SEQ FROM DUAL";
          PreparedStatement ppsPaySeq = con.prepareStatement(squ);
          ResultSet rstPaySeq = ppsPaySeq.executeQuery();
          if (rstPaySeq.next()) {
            reference_no = rstPaySeq.getString("PAY_SEQ");
          }
          closeSqlRefferance(rstPaySeq, ppsPaySeq);
         



          String sqlInsertQuery = " INSERT INTO ETT_PRN_DATA(EXPORT_TYPE,SHIPPING_BILLNO,SHIPPING_BILLDATE,PORT_CODE,REFERENCE_NO,REALISED_AMOUNT,  BILL_STATUS,BANK_CHARGES,SOFTEX_NO,BILL_REF_NO,AD_CODE,IRM_NO,  OTHER_BANK_FIRC_REF_NO,CURRENCY,REALIZATON_DATE,REMITTER_NAME,REMITTER_COUNTRY,EVENT_REF,PAYMENT_DATE)  VALUES (?,?,TO_DATE(?, 'DD-MM-YYYY'),?,?,?, ?,?,?,?,?,?, ?,?,TO_DATE(?, 'DD-MM-YYYY'),?,?,?,TO_DATE(?, 'DD-MM-YYYY'))";
         




          psInsert = new LoggableStatement(con, sqlInsertQuery);
          psInsert.setString(1, commonMethods.getEmptyIfNull(export_type).trim());
          psInsert.setString(2, commonMethods.getEmptyIfNull(shipping_billno).trim());
          psInsert.setString(3, commonMethods.getEmptyIfNull(shipping_billdate).trim());
          psInsert.setString(4, commonMethods.getEmptyIfNull(port_code).trim());
          psInsert.setString(5, commonMethods.getEmptyIfNull(reference_no).trim());
          psInsert.setString(6, commonMethods.getEmptyIfNull(realised_amount).trim());
          psInsert.setString(7, commonMethods.getEmptyIfNull(bill_status).trim());
          psInsert.setString(8, commonMethods.getEmptyIfNull(bank_charges).trim());
          psInsert.setString(9, commonMethods.getEmptyIfNull(softex_no).trim());
          psInsert.setString(10, commonMethods.getEmptyIfNull(bill_ref_no).trim());
          psInsert.setString(11, commonMethods.getEmptyIfNull(ad_code).trim());
          psInsert.setString(12, commonMethods.getEmptyIfNull(irm_no).trim());
          psInsert.setString(13, commonMethods.getEmptyIfNull(other_bank_firc_ref_no).trim());
          psInsert.setString(14, commonMethods.getEmptyIfNull(currency).trim());
          psInsert.setString(15, commonMethods.getEmptyIfNull(realizaton_date));
          psInsert.setString(16, commonMethods.getEmptyIfNull(remitter_name).trim());
          psInsert.setString(17, commonMethods.getEmptyIfNull(remitter_country));
          psInsert.setString(18, commonMethods.getEmptyIfNull(event_ref).trim());
          psInsert.setString(19, commonMethods.getEmptyIfNull(payment_date));
         
          psInsert.executeUpdate();
          psInsert.close();
        }
      }
      String fileLoc = "";
      fileLoc = ServiceLoggingUtil.getBridgePropertyValue("PRN1");
     
      String queryCsvbill = "SELECT TRIM(EXPORT_TYPE) AS EXPORTTYPE, TRIM(SHIPPING_BILLNO) SHIPPINGBILLNO, \r\n  TO_CHAR(TO_DATE(SHIPPING_BILLDATE, 'dd/mm/yy'),'dd/mm/yyyy') AS SHIPPINGBILLDATE,\r\n  TRIM(PORT_CODE) PORTCODE, REFERENCE_NO AS PAYMENT_SEQNO, TRIM(REALISED_AMOUNT) REALIZATIONAMOUNT, BILL_STATUS AS BILLSTATUS,\r\n  TRIM(BANK_CHARGES) BANKCHARGEAMT, TRIM(SOFTEX_NO) FORMNO, TRIM(BILL_REF_NO) BILLREFNO, TRIM(AD_CODE) ADCODE, \r\n  TRIM(IRM_NO) AS INWARDNO, TRIM(OTHER_BANK_FIRC_REF_NO) AS FIRCNO,\r\n  TRIM(CURRENCY) REALIZATIONCURR, TO_CHAR(TO_DATE(REALIZATON_DATE, 'dd/mm/yy'),'dd/mm/yyyy') AS REALIZATONDATE, \r\n  TRIM(REMITTER_NAME) REMITTER_NAME,TRIM(REMITTER_COUNTRY) REMITTER_COUNTRY FROM\r\n  ETT_PRN_DATA WHERE PAYMENT_DATE='" +
     





        prodate1 + "' ORDER BY BILL_STATUS \r\n";
     
      logger.info(queryCsvbill);
     
      fileLoc = fileLoc + prodate + "/";
     
      File f = new File(fileLoc);
      if (!f.exists())
      {
        f.mkdir();
        logger.info(Boolean.valueOf(f.mkdir()));
      }
      String fileName = "RBI" + prodate + seq + ".prn";
     
      String myFilePath1 = fileLoc + fileName + ".csv";
     
      noOfRec = generateManualPRNCSVFile(queryCsvbill, myFilePath1);
      if (noOfRec > 0)
      {
        downloadFiles(myFilePath1);
       
        addActionMessage("File (" + fileName + ") Downloaded Successfully..");
      }
      else
      {
        result = "nodatafound";
        addActionError("No Data Found");
      }
    }
    catch (Exception e)
    {
      logger.error("Exception in PRN Fie---" + e);
      result = "Error";
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, ps, res);
      logger.info("Finally Occurred in saveDetail");
    }
    return result;
  }
 
  public String downloadRODFile()
  {
    logger.info("----------RODWLD--- [downloadRODFile]- ------------------- :: fileUtilFileName :: " + this.fileUtilFileName + " :: fileUtilFromDate :: " + this.fileUtilFromDate);
    Connection con = null;
    PreparedStatement ps = null;
    PreparedStatement psData = null;
    LoggableStatement psInsert = null;
    ResultSet res = null;
    String result = "success";
    int noOfRec = 0;
    CommonMethods commonMethods = null;
    try
    {
      con = DBConnectionUtility.getConnection();
      commonMethods = new CommonMethods();
     

      String prodate = null;
      String prodate1 = null;
      int seq = 0;
      int dataCount = 0;
      String dataCntQuery = "SELECT COUNT(*) AS DATACOUNT FROM ETT_ROD_DATA WHERE BILL_LODGED_DATE='" + this.fileUtilFromDate + "'";
      PreparedStatement ppsdataCnt = con.prepareStatement(dataCntQuery);
      ResultSet rstdataCnt = ppsdataCnt.executeQuery();
      if (rstdataCnt.next()) {
        dataCount = rstdataCnt.getInt("DATACOUNT");
      }
      closeSqlRefferance(rstdataCnt, ppsdataCnt);
     
      String dateQuery = " SELECT RODXML_SEQNO.NEXTVAL AS ROD_SEQNO,TO_CHAR(TO_DATE('" + this.fileUtilFromDate + "','DD/MM/YYYY'),'DDMMYYYY') AS PROCDATE,TO_CHAR(TO_DATE('" + this.fileUtilFromDate + "','DD/MM/YYYY'),'DD-MM-YY') AS PROCDATE1 FROM DUAL";
      PreparedStatement ppsDateQuery = con.prepareStatement(dateQuery);
      ResultSet rsDateQuery = ppsDateQuery.executeQuery();
      if (rsDateQuery.next())
      {
        prodate = rsDateQuery.getString("PROCDATE");
        prodate1 = rsDateQuery.getString("PROCDATE1");
        seq = rsDateQuery.getInt("ROD_SEQNO");
        logger.info(prodate + " :: " + prodate1 + " :: " + seq);
      }
      closeSqlRefferance(rsDateQuery, ppsDateQuery);
      if (dataCount == 0)
      {
        String sqlQuery = "select BANKREFID,ADCODE,GOODSTYPE,PORTCODE,SHIPPINGBILLNO,TO_CHAR(TO_DATE(SHIPPINGBILLDATE, 'dd/mm/yy'),'dd/mm/yyyy') AS SHIPPINGBILLDATE, INVOICE_NO,COUNTRYCODE,DIRECTDISPATCHINDICATOR,BILL_ID,TO_CHAR(TO_DATE(BILL_LODGED_DATE, 'dd/mm/yy'),'dd/mm/yyyy') AS  BILL_LODGED_DATE, FOREIGNPARTYNAME,FOREIGNPARTYADDRESS,BUYERCOUNTRYCODE,FORMNO from REP_EDPMS_ROD_RPT_VIEW where BILL_LODGED_DATE ='" +
       
          prodate1 + "'";
       
        psData = con.prepareStatement(sqlQuery);
        logger.info("sqlQuery" + sqlQuery);
        res = psData.executeQuery();
        while (res.next())
        {
          String bankrefid = commonMethods.getEmptyIfNull(res.getString("BANKREFID"));
          String adcode = commonMethods.getEmptyIfNull(res.getString("ADCODE"));
          String goodstype = commonMethods.getEmptyIfNull(res.getString("GOODSTYPE"));
          String portcode = commonMethods.getEmptyIfNull(res.getString("PORTCODE"));
          String shippingbillno = commonMethods.getEmptyIfNull(res.getString("SHIPPINGBILLNO"));
          String shippingbilldate = commonMethods.getEmptyIfNull(res.getString("SHIPPINGBILLDATE"));
          String invoice_no = commonMethods.getEmptyIfNull(res.getString("INVOICE_NO"));
          String countrycode = commonMethods.getEmptyIfNull(res.getString("COUNTRYCODE"));
          String directdispatchindicator = commonMethods.getEmptyIfNull(res.getString("DIRECTDISPATCHINDICATOR"));
          String bill_id = commonMethods.getEmptyIfNull(res.getString("BILL_ID"));
          String bill_lodged_date = commonMethods.getEmptyIfNull(res.getString("BILL_LODGED_DATE"));
          String foreignpartyname = commonMethods.getEmptyIfNull(res.getString("FOREIGNPARTYNAME"));
          String foreignpartyaddress = commonMethods.getEmptyIfNull(res.getString("FOREIGNPARTYADDRESS"));
          String buyercountrycode = commonMethods.getEmptyIfNull(res.getString("BUYERCOUNTRYCODE"));
          String formno = commonMethods.getEmptyIfNull(res.getString("FORMNO"));
         
          String sqlInsertQuery = " INSERT INTO ETT_ROD_DATA(BANKREFID,ADCODE,GOODSTYPE,PORTCODE,SHIPPINGBILLNO,SHIPPINGBILLDATE,  INVOICE_NO,COUNTRYCODE,DIRECTDISPATCHINDICATOR,BILL_ID,BILL_LODGED_DATE,  FOREIGNPARTYNAME,FOREIGNPARTYADDRESS,BUYERCOUNTRYCODE,FORMNO)  VALUES (?,?,?,?,?, TO_DATE(?, 'DD-MM-YYYY'),?,?,?,?, TO_DATE(?, 'DD-MM-YYYY'),?,?,?,?)";
         




          psInsert = new LoggableStatement(con, sqlInsertQuery);
          psInsert.setString(1, commonMethods.getEmptyIfNull(bankrefid).trim());
          psInsert.setString(2, commonMethods.getEmptyIfNull(adcode).trim());
          psInsert.setString(3, commonMethods.getEmptyIfNull(goodstype).trim());
          psInsert.setString(4, commonMethods.getEmptyIfNull(portcode).trim());
          psInsert.setString(5, commonMethods.getEmptyIfNull(shippingbillno).trim());
          psInsert.setString(6, commonMethods.getEmptyIfNull(shippingbilldate).trim());
          psInsert.setString(7, commonMethods.getEmptyIfNull(invoice_no).trim());
          psInsert.setString(8, commonMethods.getEmptyIfNull(countrycode).trim());
          psInsert.setString(9, commonMethods.getEmptyIfNull(directdispatchindicator).trim());
          psInsert.setString(10, commonMethods.getEmptyIfNull(bill_id).trim());
          psInsert.setString(11, commonMethods.getEmptyIfNull(bill_lodged_date).trim());
          psInsert.setString(12, commonMethods.getEmptyIfNull(foreignpartyname).trim());
          psInsert.setString(13, commonMethods.getEmptyIfNull(foreignpartyaddress).trim());
          psInsert.setString(14, commonMethods.getEmptyIfNull(buyercountrycode).trim());
          psInsert.setString(15, commonMethods.getEmptyIfNull(formno));
         

          psInsert.executeUpdate();
          psInsert.close();
        }
      }
      String fileLoc = "";
      fileLoc = ServiceLoggingUtil.getBridgePropertyValue("ROD1");
     
      String queryCsv = "SELECT BANKREFID,ADCODE,GOODSTYPE, PORTCODE, SHIPPINGBILLNO,TO_CHAR(TO_DATE(SHIPPINGBILLDATE, 'dd/mm/yy'),'dd/mm/yyyy') AS SHIPPINGBILLDATE,INVOICE_NO,COUNTRYCODE,DIRECTDISPATCHINDICATOR, BILL_ID,TO_CHAR(TO_DATE(BILL_LODGED_DATE, 'dd/mm/yy'),'dd/mm/yyyy') AS BILL_LODGED_DATE,FOREIGNPARTYNAME,FOREIGNPARTYADDRESS,BUYERCOUNTRYCODE,FORMNO,' ' FROM ETT_ROD_DATA WHERE BILL_LODGED_DATE='" +
     


        prodate1 + "'";
     
      logger.info(queryCsv);
     
      fileLoc = fileLoc + prodate + "/";
     
      File f = new File(fileLoc);
      if (!f.exists())
      {
        f.mkdir();
        logger.info(Boolean.valueOf(f.mkdir()));
      }
      String fileName = "RBI" + prodate + seq + ".rod";
     
      String myFilePath1 = fileLoc + fileName + ".csv";
     
      noOfRec = generateManualRODCSVFile(queryCsv, myFilePath1);
      if (noOfRec > 0)
      {
        downloadFiles(myFilePath1);
       
        addActionMessage("File (" + fileName + ") Downloaded Successfully..");
      }
      else
      {
        result = "nodatafound";
        addActionError("No Data Found");
      }
    }
    catch (Exception e)
    {
      logger.error("Exception in ROD Fie---" + e);
      result = "Error";
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, ps, res);
      logger.info("Finally Occurred in saveDetail");
    }
    return result;
  }
 
  public String downloadIRMFile()
  {
    logger.info("----------IRMWLD--- [downloadIRMFile]- ------------------- :: fileUtilFileName :: " + this.fileUtilFileName + " :: fileUtilFromDate :: " + this.fileUtilFromDate);
    Connection con = null;
    PreparedStatement ps = null;
    PreparedStatement psData = null;
    LoggableStatement psInsert = null;
    ResultSet res = null;
    String result = "success";
    int noOfRec = 0;
    CommonMethods commonMethods = null;
    try
    {
      con = DBConnectionUtility.getConnection();
      commonMethods = new CommonMethods();
     

      String prodate = null;
      String prodate1 = null;
      int seq = 0;
      int dataCount = 0;
      String dataCntQuery = "SELECT COUNT(*) AS DATACOUNT FROM ETT_IRM_DOWNLOAD_DATA WHERE RUN_DATE='" + this.fileUtilFromDate + "'";
      PreparedStatement ppsdataCnt = con.prepareStatement(dataCntQuery);
      ResultSet rstdataCnt = ppsdataCnt.executeQuery();
      if (rstdataCnt.next()) {
        dataCount = rstdataCnt.getInt("DATACOUNT");
      }
      closeSqlRefferance(rstdataCnt, ppsdataCnt);
     
      String dateQuery = " SELECT IRMXML_SEQNO.NEXTVAL AS IRM_SEQNO,TO_CHAR(TO_DATE('" + this.fileUtilFromDate + "','DD/MM/YYYY'),'DDMMYYYY') AS PROCDATE,TO_CHAR(TO_DATE('" + this.fileUtilFromDate + "','DD/MM/YYYY'),'DD-MM-YY') AS PROCDATE1 FROM DUAL";
      PreparedStatement ppsDateQuery = con.prepareStatement(dateQuery);
      ResultSet rsDateQuery = ppsDateQuery.executeQuery();
      if (rsDateQuery.next())
      {
        prodate = rsDateQuery.getString("PROCDATE");
        prodate1 = rsDateQuery.getString("PROCDATE1");
        seq = rsDateQuery.getInt("IRM_SEQNO");
        logger.info(prodate + " :: " + prodate1 + " :: " + seq);
      }
      closeSqlRefferance(rsDateQuery, ppsDateQuery);
      if (dataCount == 0)
      {
        String sqlQuery = "select INWARD_REF_NO,REMITTANCE_AMOUNT,TO_CHAR(TO_DATE(REMITTANCE_DATE, 'dd/mm/yy'),'dd/mm/yyyy') AS REMITTANCE_DATE,\tAD_CODE,CURRENCY,IE_CODE,IE_NAME,REMITTER_NAME,REMITTER_ADD,REMITTER_COUNTRY,REMITTER_BANK_NAME,\tREMITTER_BANK_COUNTRY,SWIFT_REF_NO,PURPOSE_CODE,REMARKS from REP_EDPMS_IRM_VIEW where REMITTANCE_DATE ='" +
       
          prodate1 + "'";
       
        psData = con.prepareStatement(sqlQuery);
        logger.info("sqlQuery" + sqlQuery);
        res = psData.executeQuery();
        while (res.next())
        {
          String inward_ref_no = commonMethods.getEmptyIfNull(res.getString("INWARD_REF_NO"));
          String remittance_amount = commonMethods.getEmptyIfNull(res.getString("REMITTANCE_AMOUNT"));
          String remittance_date = commonMethods.getEmptyIfNull(res.getString("REMITTANCE_DATE"));
          String ad_code = commonMethods.getEmptyIfNull(res.getString("AD_CODE"));
          String currency = commonMethods.getEmptyIfNull(res.getString("CURRENCY"));
          String ie_code = commonMethods.getEmptyIfNull(res.getString("IE_CODE"));
          String ie_name = commonMethods.getEmptyIfNull(res.getString("IE_NAME"));
          String remitter_name = commonMethods.getEmptyIfNull(res.getString("REMITTER_NAME"));
          String remitter_add = commonMethods.getEmptyIfNull(res.getString("REMITTER_ADD"));
          String remitter_country = commonMethods.getEmptyIfNull(res.getString("REMITTER_COUNTRY"));
          String remitter_bank_name = commonMethods.getEmptyIfNull(res.getString("REMITTER_BANK_NAME"));
          String remitter_bank_country = commonMethods.getEmptyIfNull(res.getString("REMITTER_BANK_COUNTRY"));
          String swift_ref_no = commonMethods.getEmptyIfNull(res.getString("SWIFT_REF_NO"));
          String purpose_code = commonMethods.getEmptyIfNull(res.getString("PURPOSE_CODE"));
          String remarks = commonMethods.getEmptyIfNull(res.getString("REMARKS"));
         
          String sqlInsertQuery = " INSERT INTO ETT_IRM_DOWNLOAD_DATA(INWARD_REF_NO,REMITTANCE_AMOUNT,REMITTANCE_DATE,AD_CODE,CURRENCY,  IE_CODE,IE_NAME,REMITTER_NAME,REMITTER_ADD,REMITTER_COUNTRY,  REMITTER_BANK_NAME,REMITTER_BANK_COUNTRY,SWIFT_REF_NO,PURPOSE_CODE,REMARKS)  VALUES (?,?,TO_DATE(?, 'DD-MM-YYYY'),?,?, ?,?,?,?,?, ?,?,?,?,?)";
         




          psInsert = new LoggableStatement(con, sqlInsertQuery);
          psInsert.setString(1, commonMethods.getEmptyIfNull(inward_ref_no).trim());
          psInsert.setString(2, commonMethods.getEmptyIfNull(remittance_amount).trim());
          psInsert.setString(3, commonMethods.getEmptyIfNull(remittance_date).trim());
          psInsert.setString(4, commonMethods.getEmptyIfNull(ad_code).trim());
          psInsert.setString(5, commonMethods.getEmptyIfNull(currency).trim());
          psInsert.setString(6, commonMethods.getEmptyIfNull(ie_code).trim());
          psInsert.setString(7, commonMethods.getEmptyIfNull(ie_name).trim());
          psInsert.setString(8, commonMethods.getEmptyIfNull(remitter_name).trim());
          psInsert.setString(9, commonMethods.getEmptyIfNull(remitter_add).trim());
          psInsert.setString(10, commonMethods.getEmptyIfNull(remitter_country).trim());
          psInsert.setString(11, commonMethods.getEmptyIfNull(remitter_bank_name).trim());
          psInsert.setString(12, commonMethods.getEmptyIfNull(remitter_bank_country).trim());
          psInsert.setString(13, commonMethods.getEmptyIfNull(swift_ref_no).trim());
          psInsert.setString(14, commonMethods.getEmptyIfNull(purpose_code).trim());
          psInsert.setString(15, commonMethods.getEmptyIfNull(remarks));
         

          psInsert.executeUpdate();
          psInsert.close();
        }
      }
      String fileLoc = "";
      fileLoc = ServiceLoggingUtil.getBridgePropertyValue("IRM1");
     
      String queryCsv = "SELECT INWARD_REF_NO AS IRMNO,REMITTANCE_AMOUNT AS REM_AMOUNT,TO_CHAR(TO_DATE(REMITTANCE_DATE,'DD-MM-YY'),'DD/MM/YYYY') AS REM_DATE,AD_CODE AS ADCODE,CURRENCY AS IRM_RECCURR,IE_CODE AS IECODE,IE_NAME AS BENEFICIARYNAME,REMITTER_NAME AS REMITTERNAME,REMITTER_ADD AS REMITTERADDRESS,REMITTER_COUNTRY AS COUNTRY,REMITTER_BANK_NAME AS REMITTERBANKNAME,REMITTER_BANK_COUNTRY AS REM_BANKCOUNTRY,SWIFT_REF_NO AS SWIFT_REFNO,PURPOSE_CODE AS PURPOSECODE,REMARKS AS REMARKS FROM ETT_IRM_DOWNLOAD_DATA WHERE RUN_DATE='" +
     



        prodate1 + "'";
     
      logger.info(queryCsv);
     
      fileLoc = fileLoc + prodate + "/";
     
      File f = new File(fileLoc);
      if (!f.exists())
      {
        f.mkdir();
        logger.info(Boolean.valueOf(f.mkdir()));
      }
      String fileName = "RBI" + prodate + seq + ".irm";
     
      String myFilePath1 = fileLoc + fileName + ".csv";
     
      noOfRec = generateManualIRMCSVFile(queryCsv, myFilePath1);
      if (noOfRec > 0)
      {
        downloadFiles(myFilePath1);
       
        addActionMessage("File (" + fileName + ") Downloaded Successfully..");
      }
      else
      {
        result = "nodatafound";
        addActionError("No Data Found");
      }
    }
    catch (Exception e)
    {
      logger.error("Exception in IRM Fie---" + e);
      result = "Error";
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, ps, res);
      logger.info("Finally Occurred in saveDetail");
    }
    return result;
  }
 
  public String downloadIRMDGFTFile()
  {
    logger.info("----------IRMWLD--- [downloadIRMDGFTFile]- ------------------- :: fileUtilFileName :: " + this.fileUtilFileName + " :: fileUtilFromDate :: " + this.fileUtilFromDate);
    Connection con = null;
    PreparedStatement ps = null;
    PreparedStatement psData = null;
    LoggableStatement psInsert = null;
    ResultSet res = null;
    String result = "success";
    int noOfRec = 0;
    CommonMethods commonMethods = null;
    try
    {
      con = DBConnectionUtility.getConnection();
      commonMethods = new CommonMethods();
     
      String prodate = null;
      String prodate1 = null;
      int seq = 0;
      int dataCount = 0;
     
      String dateQuery = " SELECT IRMXML_SEQNO.NEXTVAL AS IRM_SEQNO,\tTO_CHAR(TO_DATE('" +
        this.fileUtilFromDate + "','DD/MM/YYYY'),'DDMMYYYY') AS PROCDATE," +
        "\tTO_CHAR(TO_DATE('" + this.fileUtilFromDate + "','DD/MM/YYYY'),'DD-MM-YY') AS PROCDATE1 FROM DUAL";
      PreparedStatement ppsDateQuery = con.prepareStatement(dateQuery);
      ResultSet rsDateQuery = ppsDateQuery.executeQuery();
      if (rsDateQuery.next())
      {
        seq = rsDateQuery.getInt("IRM_SEQNO");
        prodate = rsDateQuery.getString("PROCDATE");
        prodate1 = rsDateQuery.getString("PROCDATE1");
      }
      closeSqlRefferance(rsDateQuery, ppsDateQuery);
     


      String fileLoc = "";
      fileLoc = ServiceLoggingUtil.getBridgePropertyValue("IRM1");
     
      String queryCsv = "select * from REP_EDPMS_IRM_VIEW_DGFT where IRMISSUEDATE='" + this.fileUtilFromDate + "'";
     
      logger.info(queryCsv);
     
      fileLoc = fileLoc + prodate + "/";
     
      File f = new File(fileLoc);
      if (!f.exists())
      {
        f.mkdir();
        logger.info(Boolean.valueOf(f.mkdir()));
      }
      String fileName = "RBI" + prodate + seq + ".dgft.irm";
     
      String myFilePath1 = fileLoc + fileName + ".csv";
     
      noOfRec = generateManualIRMDGFTCSVFile(queryCsv, myFilePath1);
      if (noOfRec > 0)
      {
        downloadFiles(myFilePath1);
       
        addActionMessage("File (" + fileName + ") Downloaded Successfully..");
      }
      else
      {
        result = "nodatafound";
        addActionError("No Data Found");
      }
    }
    catch (Exception e)
    {
      logger.error("Exception in IRM Fie---" + e);
      result = "Error";
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, ps, res);
      logger.info("Finally Occurred in saveDetail");
    }
    return result;
  }
 
  public String downloadORMDGFTFile()
  {
    logger.info("----------ORMWLD--- [downloadORMDGFTFile]- ------------------- :: fileUtilFileName :: " + this.fileUtilFileName + " :: fileUtilFromDate :: " + this.fileUtilFromDate);
    Connection con = null;
    PreparedStatement ps = null;
    PreparedStatement psData = null;
    LoggableStatement psInsert = null;
    ResultSet res = null;
    String result = "success";
    int noOfRec = 0;
    CommonMethods commonMethods = null;
    try
    {
      con = DBConnectionUtility.getConnection();
      commonMethods = new CommonMethods();
     
      String prodate = null;
      String prodate1 = null;
      int seq = 0;
      int dataCount = 0;
     
      String dateQuery = " SELECT ORMXML_SEQNO.NEXTVAL AS ORM_SEQNO,\tTO_CHAR(TO_DATE('" +
        this.fileUtilFromDate + "','DD/MM/YYYY'),'DDMMYYYY') AS PROCDATE," +
        "\tTO_CHAR(TO_DATE('" + this.fileUtilFromDate + "','DD/MM/YYYY'),'DD-MM-YY') AS PROCDATE1 FROM DUAL";
      PreparedStatement ppsDateQuery = con.prepareStatement(dateQuery);
      ResultSet rsDateQuery = ppsDateQuery.executeQuery();
      if (rsDateQuery.next())
      {
        seq = rsDateQuery.getInt("ORM_SEQNO");
        prodate = rsDateQuery.getString("PROCDATE");
        prodate1 = rsDateQuery.getString("PROCDATE1");
      }
      closeSqlRefferance(rsDateQuery, ppsDateQuery);
     


      String fileLoc = "";
      fileLoc = ServiceLoggingUtil.getBridgePropertyValue("ORM1");
     
      String queryCsv = "select * from REP_EDPMS_ORM_VIEW_DGFT where REMITTANCE_DATE='" + this.fileUtilFromDate + "'";
     
      logger.info(queryCsv);
     
      fileLoc = fileLoc + prodate + "/";
     
      File f = new File(fileLoc);
      if (!f.exists())
      {
        f.mkdir();
        logger.info(Boolean.valueOf(f.mkdir()));
      }
      String fileName = "RBI" + prodate + seq + ".dgft.orm";
     
      String myFilePath1 = fileLoc + fileName + ".csv";
     
      noOfRec = generateManualIRMDGFTCSVFile(queryCsv, myFilePath1);
      if (noOfRec > 0)
      {
        downloadFiles(myFilePath1);
       
        addActionMessage("File (" + fileName + ") Downloaded Successfully..");
      }
      else
      {
        result = "nodatafound";
        addActionError("No Data Found");
      }
    }
    catch (Exception e)
    {
      logger.error("Exception in IRM Fie---" + e);
      result = "Error";
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, ps, res);
      logger.info("Finally Occurred in saveDetail");
    }
    return result;
  }
 
  public String downloadEBRCFile()
  {
    logger.info("----------EBRCDWLD--- [downloadEBRCFile]- ------------------- :: fileUtilFileName :: " + this.fileUtilFileName + " :: fileUtilFromDate :: " + this.fileUtilFromDate + " :: fileUtilToDate :: " + this.fileUtilToDate);
    Connection con = null;
    PreparedStatement ps = null;
    PreparedStatement psData = null;
    LoggableStatement psInsert = null;
    ResultSet res = null;
    String result = "success";
    int noOfRec = 0;
    CommonMethods commonMethods = null;
    try
    {
      con = DBConnectionUtility.getConnection();
      commonMethods = new CommonMethods();
     

      String prodate = null;
      String prodate1 = null;
      int seq = 0;
      int dataCount = 0;
      String dataCntQuery = "SELECT COUNT(*) AS DATACOUNT FROM ETT_EBRC_FILES WHERE BRCDATE='" + this.fileUtilFromDate + "'";
      PreparedStatement ppsdataCnt = con.prepareStatement(dataCntQuery);
      ResultSet rstdataCnt = ppsdataCnt.executeQuery();
      if (rstdataCnt.next()) {
        dataCount = rstdataCnt.getInt("DATACOUNT");
      }
      closeSqlRefferance(rstdataCnt, ppsdataCnt);
      logger.info("dataCount :: " + dataCount);
      if (dataCount > 0)
      {
        String proQuery = " SELECT LPAD(EBRC_SEQNO.NEXTVAL,3,'0') AS ID, TO_CHAR(TO_DATE('" +
          this.fileUtilFromDate + "','DD/MM/YYYY'),'DDMMYYYY') AS PROCDATE," +
          " TO_CHAR(TO_DATE('" + this.fileUtilFromDate + "','DD/MM/YYYY'),'DD-MM-YY') AS PROCDATE1 FROM DUAL";
        PreparedStatement pps1 = con.prepareStatement(proQuery);
        ResultSet rs1 = pps1.executeQuery();
        if (rs1.next())
        {
          prodate = rs1.getString("PROCDATE");
          prodate1 = rs1.getString("PROCDATE1");
          seq = rs1.getInt("ID");
         
          logger.info(prodate + " :: " + prodate1 + " :: " + seq);
        }
        closeSqlRefferance(rs1, pps1);
       
        String fileName1 = "UBIN0000000" + prodate + String.format("%03d", new Object[] { Integer.valueOf(seq) });
       
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();
        document.setXmlStandalone(true);
       
        Element root = document.createElement("BRCDATA");
        document.appendChild(root);
       
        Element fname = document.createElement("FILENAME");
        fname.appendChild(document.createTextNode(fileName1));
        root.appendChild(fname);
       
        Element rocdoc2 = document.createElement("ENVELOPE");
        root.appendChild(rocdoc2);
       
        String sqlQuery11 = "select NVL(TRIM(BRCNO), ' ') AS BRCNO, TO_CHAR(TO_DATE(BRCDATE, 'dd-mm-yy'),'yyyy-mm-dd') AS BRCDATE, NVL(TRIM(STATUS), ' ') AS STATUS, NVL(TRIM(IEC), ' ') AS IEC, NVL(TRIM(EXPNAME), ' ') AS EXPNAME, NVL(TRIM(IFSC), ' ') AS IFSC, NVL(TRIM(BILLID), ' ') AS BILLID, NVL(TRIM(SNO), ' ') AS SNO, NVL(TRIM(SPORT), ' ') AS SPORT, TO_CHAR(TO_DATE(SDATE, 'dd-mm-yy'),'yyyy-mm-dd') AS SDATE, NVL(TRIM(SCC), ' ') AS SCC, NVL(TRIM(SVALUE), ' ') AS SVALUE, NVL(TRIM(RCC), ' ') AS RCC, NVL(TRIM(RVALUE), ' ') AS RVALUE, TO_CHAR(TO_DATE(RDATE, 'dd-mm-yy'),'yyyy-mm-dd') AS RDATE, NVL(TRIM(RVALUEINR), ' ') AS RVALUEINR, NVL(TRIM(RMTBANK), ' ') AS RMTBANK, NVL(TRIM(RMTCITY), ' ') AS RMTCITY, NVL(TRIM(RMTCTRY), ' ') AS RMTCTRY, NVL(TRIM(FREIGHT_VALUE), ' ') AS FREIGHT_VALUE, NVL(TRIM(INSURANCE_VALUE), ' ') AS INSURANCE_VALUE, NVL(TRIM(COMMISSION_VALUE), ' ') AS COMMISSION_VALUE, NVL(TRIM(IS_VOSTRO), ' ') AS IS_VOSTRO, NVL(TRIM(CATEGORY_OF_EXPORTS), ' ') AS CATEGORY_OF_EXPORTS, NVL(TRIM(SERVICING_ACCOUNTING_CODE), '') AS SERVICING_ACCOUNTING_CODE, NVL(TRIM(IS_FORFEITING), ' ') AS IS_FORFEITING, NVL(TRIM(IS_FACTORING), ' ') AS IS_FACTORING, NVL(TRIM(FILENAME), ' ') AS FILENAME, NVL(TRIM(FILE_ERRCODE), ' ') AS FILE_ERRCODE, NVL(TRIM(BRC_ERRORCODE), ' ') AS BRC_ERRORCODE, NVL(TRIM(CRN), ' ') AS CRN, TO_CHAR(TO_DATE(LODGEMENTDATE, 'dd-mm-yy'),'yyyy-mm-dd') AS LODGEMENTDATE, NVL(TRIM(EXPORTTYPE), ' ') AS EXPORTTYPE, NVL(TRIM(INVOICENO), ' ') AS INVOICENO,  TO_CHAR(TO_DATE(INVOICEDATE, 'dd-mm-yy'),'yyyy-mm-dd') AS INVOICEDATE FROM ETT_EBRC_FILES WHERE BRCDATE ='" +
       














          prodate1 + "'";
       
        PreparedStatement ps2 = con.prepareStatement(sqlQuery11);
        ResultSet res1 = ps2.executeQuery();
        while (res1.next())
        {
          String brcNo = res1.getString("BRCNO");
          String brcDate = res1.getString("BRCDATE");
          String status = res1.getString("STATUS");
          String ieCode = res1.getString("IEC");
          String exportName = res1.getString("EXPNAME");
          String codeIFSC = res1.getString("IFSC");
          String billID = res1.getString("BILLID");
          String shipNo = res1.getString("SNO");
          String shipPort = res1.getString("SPORT");
          String shipDate = res1.getString("SDATE");
          String shipCurr = res1.getString("SCC");
          String shipValue = res1.getString("SVALUE");
         
          String realCurr = res1.getString("RCC");
          String realValue = res1.getString("RVALUE");
          String realDate = res1.getString("RDATE");
          String rmtBank = res1.getString("RMTBANK");
          String rmtCity = res1.getString("RMTCITY");
          String rmtCtry = res1.getString("RMTCTRY");
          String freight = res1.getString("FREIGHT_VALUE");
          String insurance = res1.getString("INSURANCE_VALUE");
          String commission = res1.getString("COMMISSION_VALUE");
          String isVostro = res1.getString("IS_VOSTRO");
          String categoryOfExport = res1.getString("CATEGORY_OF_EXPORTS");
          String s_a_c = res1.getString("SERVICING_ACCOUNTING_CODE");
          String isForFeit = res1.getString("IS_FORFEITING");
          String isFactoring = res1.getString("IS_FACTORING");
          String crn = res1.getString("CRN");
          String lodgementDate = res1.getString("LODGEMENTDATE");
          String exportType = res1.getString("EXPORTTYPE");
          String invoiceNo = res1.getString("INVOICENO");
          String invoiceDate = res1.getString("INVOICEDATE");
         
          Element rocdoc1 = document.createElement("EBRC");
          rocdoc2.appendChild(rocdoc1);
          if (brcNo == null)
          {
            Element pc = document.createElement("BRCNO");
            pc.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(pc);
          }
          else
          {
            Element pc = document.createElement("BRCNO");
            pc.appendChild(document.createTextNode(brcNo));
            rocdoc1.appendChild(pc);
          }
          if (brcDate == null)
          {
            Element ri = document.createElement("BRCDATE");
            ri.appendChild(document.createTextNode(" "));
            rocdoc1.appendChild(ri);
          }
          else
          {
            Element ri = document.createElement("BRCDATE");
            ri.appendChild(document.createTextNode(brcDate));
            rocdoc1.appendChild(ri);
          }
          if (status == null)
          {
            Element et = document.createElement("STATUS");
            et.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(et);
          }
          else
          {
            Element et = document.createElement("STATUS");
            et.appendChild(document.createTextNode("F"));
            rocdoc1.appendChild(et);
          }
          if (ieCode == null)
          {
            Element sbn = document.createElement("IEC");
            sbn.appendChild(document.createTextNode(" "));
            rocdoc1.appendChild(sbn);
          }
          else
          {
            Element sbn = document.createElement("IEC");
            sbn.appendChild(document.createTextNode(ieCode));
            rocdoc1.appendChild(sbn);
          }
          if (exportName == null)
          {
            Element sbd = document.createElement("EXPNAME");
            sbd.appendChild(document.createTextNode(" "));
            rocdoc1.appendChild(sbd);
          }
          else
          {
            Element sbd = document.createElement("EXPNAME");
            sbd.appendChild(document.createTextNode(exportName));
            rocdoc1.appendChild(sbd);
          }
          if (codeIFSC == null)
          {
            Element fn = document.createElement("IFSC");
            fn.appendChild(document.createTextNode(" "));
            rocdoc1.appendChild(fn);
          }
          else
          {
            Element fn = document.createElement("IFSC");
            fn.appendChild(document.createTextNode(codeIFSC));
            rocdoc1.appendChild(fn);
          }
          if (billID == null)
          {
            Element ld = document.createElement("BILLID");
            ld.appendChild(document.createTextNode(" "));
            rocdoc1.appendChild(ld);
          }
          else
          {
            Element ld = document.createElement("BILLID");
            ld.appendChild(document.createTextNode(billID));
            rocdoc1.appendChild(ld);
          }
          if (shipNo == null)
          {
            Element ic = document.createElement("SNO");
            ic.appendChild(document.createTextNode(" "));
            rocdoc1.appendChild(ic);
          }
          else
          {
            Element ic = document.createElement("SNO");
            ic.appendChild(document.createTextNode(shipNo));
            rocdoc1.appendChild(ic);
          }
          if (shipPort == null)
          {
            Element cic = document.createElement("SPORT");
            cic.appendChild(document.createTextNode(" "));
            rocdoc1.appendChild(cic);
          }
          else
          {
            Element cic = document.createElement("SPORT");
            cic.appendChild(document.createTextNode(shipPort));
            rocdoc1.appendChild(cic);
          }
          if (shipDate == null)
          {
            Element ac = document.createElement("SDATE");
            ac.appendChild(document.createTextNode(" "));
            rocdoc1.appendChild(ac);
          }
          else
          {
            Element ac = document.createElement("SDATE");
            ac.appendChild(document.createTextNode(shipDate));
            rocdoc1.appendChild(ac);
          }
          if (shipCurr == null)
          {
            Element aea = document.createElement("SCC");
            aea.appendChild(document.createTextNode(" "));
            rocdoc1.appendChild(aea);
          }
          else
          {
            Element aea = document.createElement("SCC");
            aea.appendChild(document.createTextNode(shipCurr));
            rocdoc1.appendChild(aea);
          }
          if (shipValue == null)
          {
            Element dd = document.createElement("SVALUE");
            dd.appendChild(document.createTextNode(" "));
            rocdoc1.appendChild(dd);
          }
          else
          {
            Element dd = document.createElement("SVALUE");
            dd.appendChild(document.createTextNode(shipValue));
            rocdoc1.appendChild(dd);
          }
          if (realCurr == null)
          {
            Element abn = document.createElement("RCC");
            abn.appendChild(document.createTextNode(" "));
            rocdoc1.appendChild(abn);
          }
          else
          {
            Element abn = document.createElement("RCC");
            abn.appendChild(document.createTextNode(realCurr));
            rocdoc1.appendChild(abn);
          }
          if (realValue == null)
          {
            Element dn = document.createElement("RVALUE");
            dn.appendChild(document.createTextNode(" "));
            rocdoc1.appendChild(dn);
          }
          else
          {
            Element dn = document.createElement("RVALUE");
            dn.appendChild(document.createTextNode(realValue));
            rocdoc1.appendChild(dn);
          }
          if ((realDate == null) || (realDate == ""))
          {
            Element bn = document.createElement("RDATE");
            bn.appendChild(document.createTextNode(" "));
            rocdoc1.appendChild(bn);
          }
          else
          {
            Element bn = document.createElement("RDATE");
            bn.appendChild(document.createTextNode(realDate));
            rocdoc1.appendChild(bn);
          }
          if ((rmtBank == null) || (rmtBank == ""))
          {
            Element bn = document.createElement("RMTBANK");
            bn.appendChild(document.createTextNode(" "));
            rocdoc1.appendChild(bn);
          }
          else
          {
            Element bn = document.createElement("RMTBANK");
            bn.appendChild(document.createTextNode(rmtBank));
            rocdoc1.appendChild(bn);
          }
          if ((rmtCity == null) || (rmtCity == ""))
          {
            Element bn = document.createElement("RMTCITY");
            bn.appendChild(document.createTextNode(" "));
            rocdoc1.appendChild(bn);
          }
          else
          {
            Element bn = document.createElement("RMTCITY");
            bn.appendChild(document.createTextNode(rmtCity));
            rocdoc1.appendChild(bn);
          }
          if ((rmtCtry == null) || (rmtCtry == ""))
          {
            Element bn = document.createElement("RMTCTRY");
            bn.appendChild(document.createTextNode(" "));
            rocdoc1.appendChild(bn);
          }
          else
          {
            Element bn = document.createElement("RMTCTRY");
            bn.appendChild(document.createTextNode(rmtCtry));
            rocdoc1.appendChild(bn);
          }
          if ((freight == null) || (freight == ""))
          {
            Element bn = document.createElement("FREIGHT");
            bn.appendChild(document.createTextNode(" "));
            rocdoc1.appendChild(bn);
          }
          else
          {
            Element bn = document.createElement("FREIGHT");
            bn.appendChild(document.createTextNode(freight));
            rocdoc1.appendChild(bn);
          }
          if ((insurance == null) || (insurance == ""))
          {
            Element bn = document.createElement("INSURANCE");
            bn.appendChild(document.createTextNode(" "));
            rocdoc1.appendChild(bn);
          }
          else
          {
            Element bn = document.createElement("INSURANCE");
            bn.appendChild(document.createTextNode(insurance));
            rocdoc1.appendChild(bn);
          }
          if ((commission == null) || (commission == ""))
          {
            Element bn = document.createElement("COMMISSION");
            bn.appendChild(document.createTextNode(" "));
            rocdoc1.appendChild(bn);
          }
          else
          {
            Element bn = document.createElement("COMMISSION");
            bn.appendChild(document.createTextNode(commission));
            rocdoc1.appendChild(bn);
          }
          if ((isVostro == null) || (isVostro == ""))
          {
            Element bn = document.createElement("ISVOSTRO");
            bn.appendChild(document.createTextNode(" "));
            rocdoc1.appendChild(bn);
          }
          else
          {
            Element bn = document.createElement("ISVOSTRO");
            bn.appendChild(document.createTextNode(isVostro));
            rocdoc1.appendChild(bn);
          }
          if ((categoryOfExport == null) || (categoryOfExport == ""))
          {
            Element bn = document.createElement("EXPORTTYPE");
            bn.appendChild(document.createTextNode(" "));
            rocdoc1.appendChild(bn);
          }
          else
          {
            Element bn = document.createElement("EXPORTTYPE");
            bn.appendChild(document.createTextNode(categoryOfExport));
            rocdoc1.appendChild(bn);
          }
          if ((s_a_c == null) || (s_a_c == ""))
          {
            Element bn = document.createElement("SACCODE");
            bn.appendChild(document.createTextNode(""));
            rocdoc1.appendChild(bn);
          }
          else
          {
            Element bn = document.createElement("SACCODE");
            bn.appendChild(document.createTextNode(s_a_c));
            rocdoc1.appendChild(bn);
          }
          if ((isForFeit == null) || (isForFeit == ""))
          {
            Element bn = document.createElement("ISFORFEIT");
            bn.appendChild(document.createTextNode(" "));
            rocdoc1.appendChild(bn);
          }
          else
          {
            Element bn = document.createElement("ISFORFEIT");
            bn.appendChild(document.createTextNode(isForFeit));
            rocdoc1.appendChild(bn);
          }
          if ((isFactoring == null) || (isFactoring == ""))
          {
            Element bn = document.createElement("ISFACTOR");
            bn.appendChild(document.createTextNode(" "));
            rocdoc1.appendChild(bn);
          }
          else
          {
            Element bn = document.createElement("ISFACTOR");
            bn.appendChild(document.createTextNode(isFactoring));
            rocdoc1.appendChild(bn);
          }
          noOfRec++;
        }
        logger.info("noOfRec :: " + noOfRec);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty("method", "html");
        DOMSource domSource = new DOMSource(document);
       
        String proQuery1 = "SELECT TO_CHAR(TO_DATE(PROCDATE,'DD-MM-YY'),'YYYYMMDD') AS PROCDATE FROM DLYPRCCYCL";
       
        PreparedStatement pps2 = con.prepareStatement(proQuery1);
        ResultSet rs2 = pps2.executeQuery();
        String eodDate = null;
        if (rs2.next()) {
          eodDate = rs2.getString("PROCDATE");
        }
        closeSqlRefferance(rs2, pps2);
       
        String fileLoc = "";
        fileLoc = ServiceLoggingUtil.getBridgePropertyValue("EBRC1");
       

        fileLoc = fileLoc + eodDate + "/";
        File f = new File(fileLoc);
        if (!f.exists()) {
          f.mkdir();
        }
        String myFilePath = fileLoc + fileName1 + ".xml";
       
        StringWriter writer = new StringWriter();
        transformer.transform(domSource, new StreamResult(writer));
        String output = writer.toString();
       

        File file = new File(myFilePath);
       
        file.createNewFile();
       
        FileWriter writer1 = new FileWriter(file);
       
        writer1.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>" + output);
        writer1.flush();
        writer1.close();
       
        logger.info("File has been successfully saved");
        if (noOfRec > 0)
        {
          downloadFiles(myFilePath);
         
          addActionMessage("File (" + fileName1 + ") Downloaded Successfully..");
        }
      }
      else
      {
        addActionError("No date found.");
        return "error";
      }
    }
    catch (Exception e)
    {
      logger.error("Exception in EBRC Fie---" + e);
      result = "Error";
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, ps, res);
      logger.info("Finally Occurred in saveDetail");
    }
    DBConnectionUtility.surrenderDB(con, ps, res);
    logger.info("Finally Occurred in saveDetail");
   
    return result;
  }
 
  public String downloadCANEBRCFile()
  {
    logger.info("----------EBRCDWLD--- [downloadCEBRCFile]- ------------------- :: fileUtilFileName :: " + this.fileUtilFileName + " :: fileUtilFromDate :: " + this.fileUtilFromDate + " :: fileUtilToDate :: " + this.fileUtilToDate + " :: fileUtilMasterRef ::" + this.fileUtilMasterRef + " :: fileUtilBrcNO :: " + this.fileUtilBrcNO);
    Connection con = null;
    PreparedStatement ps = null;
    PreparedStatement psData = null;
    LoggableStatement psInsert = null;
    ResultSet res = null;
    String result = "success";
    int noOfRec = 0;
    CommonMethods commonMethods = null;
    try
    {
      con = DBConnectionUtility.getConnection();
      commonMethods = new CommonMethods();
     

      String prodate = null;
     
      int seq = 0;
      int dataCount = 0;
     









      String proQuery = " SELECT LPAD(EBRC_SEQNO.NEXTVAL,3,'0') AS ID, TO_CHAR(TO_DATE(PROCDATE,'DD/MM/YYYY'),'DDMMYYYY') AS PROCDATE FROM DLYPRCCYCL";
     

      PreparedStatement pps1 = con.prepareStatement(proQuery);
      ResultSet rs1 = pps1.executeQuery();
      if (rs1.next())
      {
        prodate = rs1.getString("PROCDATE");
       
        seq = rs1.getInt("ID");
        logger.info(prodate + " ::  :: " + seq);
      }
      closeSqlRefferance(rs1, pps1);
     
      String fileName1 = "UBIN0000000" + prodate + String.format("%03d", new Object[] { Integer.valueOf(seq) });
     
      DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
      Document document = documentBuilder.newDocument();
      document.setXmlStandalone(true);
     
      Element root = document.createElement("BRCDATA");
      document.appendChild(root);
     
      Element fname = document.createElement("FILENAME");
      fname.appendChild(document.createTextNode(fileName1));
      root.appendChild(fname);
     
      Element rocdoc2 = document.createElement("ENVELOPE");
      root.appendChild(rocdoc2);
     
      String sqlQuery11 = "select NVL(TRIM(BRCNO), ' ') AS BRCNO, TO_CHAR(TO_DATE(BRCDATE, 'dd-mm-yy'),'yyyy-mm-dd') AS BRCDATE, NVL(TRIM(STATUS), ' ') AS STATUS, NVL(TRIM(IEC), ' ') AS IEC, NVL(TRIM(EXPNAME), ' ') AS EXPNAME, NVL(TRIM(IFSC), ' ') AS IFSC, NVL(TRIM(BILLID), ' ') AS BILLID, NVL(TRIM(SNO), ' ') AS SNO, NVL(TRIM(SPORT), ' ') AS SPORT, TO_CHAR(TO_DATE(SDATE, 'dd-mm-yy'),'yyyy-mm-dd') AS SDATE, NVL(TRIM(SCC), ' ') AS SCC, NVL(TRIM(SVALUE), ' ') AS SVALUE, NVL(TRIM(RCC), ' ') AS RCC, NVL(TRIM(RVALUE), ' ') AS RVALUE, TO_CHAR(TO_DATE(RDATE, 'dd-mm-yy'),'yyyy-mm-dd') AS RDATE, NVL(TRIM(RVALUEINR), ' ') AS RVALUEINR, NVL(TRIM(RMTBANK), ' ') AS RMTBANK, NVL(TRIM(RMTCITY), ' ') AS RMTCITY, NVL(TRIM(RMTCTRY), ' ') AS RMTCTRY, NVL(TRIM(FREIGHT_VALUE), ' ') AS FREIGHT_VALUE, NVL(TRIM(INSURANCE_VALUE), ' ') AS INSURANCE_VALUE, NVL(TRIM(COMMISSION_VALUE), ' ') AS COMMISSION_VALUE, NVL(TRIM(IS_VOSTRO), ' ') AS IS_VOSTRO, NVL(TRIM(CATEGORY_OF_EXPORTS), ' ') AS CATEGORY_OF_EXPORTS, NVL(TRIM(SERVICING_ACCOUNTING_CODE), ' ') AS SERVICING_ACCOUNTING_CODE, NVL(TRIM(IS_FORFEITING), ' ') AS IS_FORFEITING, NVL(TRIM(IS_FACTORING), ' ') AS IS_FACTORING, NVL(TRIM(FILENAME), ' ') AS FILENAME, NVL(TRIM(FILE_ERRCODE), ' ') AS FILE_ERRCODE, NVL(TRIM(BRC_ERRORCODE), ' ') AS BRC_ERRORCODE, NVL(TRIM(CRN), ' ') AS CRN, TO_CHAR(TO_DATE(LODGEMENTDATE, 'dd-mm-yy'),'yyyy-mm-dd') AS LODGEMENTDATE, NVL(TRIM(EXPORTTYPE), ' ') AS EXPORTTYPE, NVL(TRIM(INVOICENO), ' ') AS INVOICENO,  TO_CHAR(TO_DATE(INVOICEDATE, 'dd-mm-yy'),'yyyy-mm-dd') AS INVOICEDATE FROM ETT_EBRC_FILES WHERE 1=1   ";
      if ((this.fileUtilBrcNO != null) && (!this.fileUtilBrcNO.equalsIgnoreCase(""))) {
        sqlQuery11 = sqlQuery11 + " AND BRCNO='" + this.fileUtilBrcNO + "'";
      }
      if ((this.fileUtilMasterRef != null) && (!this.fileUtilMasterRef.equalsIgnoreCase(""))) {
        sqlQuery11 = sqlQuery11 + "  AND BILL_REFNO ='" + this.fileUtilMasterRef + "'";
      }
      PreparedStatement ps2 = con.prepareStatement(sqlQuery11);
      logger.info("sqlQuery11 :: " + sqlQuery11);
      ResultSet res1 = ps2.executeQuery();
      while (res1.next())
      {
        String brcNo = res1.getString("BRCNO");
        String brcDate = res1.getString("BRCDATE");
        String status = res1.getString("STATUS");
        String ieCode = res1.getString("IEC");
        String exportName = res1.getString("EXPNAME");
        String codeIFSC = res1.getString("IFSC");
        String billID = res1.getString("BILLID");
        String shipNo = res1.getString("SNO");
        String shipPort = res1.getString("SPORT");
        String shipDate = res1.getString("SDATE");
        String shipCurr = res1.getString("SCC");
        String shipValue = res1.getString("SVALUE");
       
        String realCurr = res1.getString("RCC");
        String realValue = res1.getString("RVALUE");
        String realDate = res1.getString("RDATE");
        String rmtBank = res1.getString("RMTBANK");
        String rmtCity = res1.getString("RMTCITY");
        String rmtCtry = res1.getString("RMTCTRY");
        String freight = res1.getString("FREIGHT_VALUE");
        String insurance = res1.getString("INSURANCE_VALUE");
        String commission = res1.getString("COMMISSION_VALUE");
        String isVostro = res1.getString("IS_VOSTRO");
        String categoryOfExport = res1.getString("CATEGORY_OF_EXPORTS");
        String s_a_c = res1.getString("SERVICING_ACCOUNTING_CODE");
        String isForFeit = res1.getString("IS_FORFEITING");
        String isFactoring = res1.getString("IS_FACTORING");
        String crn = res1.getString("CRN");
        String lodgementDate = res1.getString("LODGEMENTDATE");
        String exportType = res1.getString("EXPORTTYPE");
        String invoiceNo = res1.getString("INVOICENO");
        String invoiceDate = res1.getString("INVOICEDATE");
       
        Element rocdoc1 = document.createElement("EBRC");
        rocdoc2.appendChild(rocdoc1);
        if (brcNo == null)
        {
          Element pc = document.createElement("BRCNO");
          pc.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(pc);
        }
        else
        {
          Element pc = document.createElement("BRCNO");
          pc.appendChild(document.createTextNode(brcNo));
          rocdoc1.appendChild(pc);
        }
        if (brcDate == null)
        {
          Element ri = document.createElement("BRCDATE");
          ri.appendChild(document.createTextNode(" "));
          rocdoc1.appendChild(ri);
        }
        else
        {
          Element ri = document.createElement("BRCDATE");
          ri.appendChild(document.createTextNode(brcDate));
          rocdoc1.appendChild(ri);
        }
        if (status == null)
        {
          Element et = document.createElement("STATUS");
          et.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(et);
        }
        else
        {
          Element et = document.createElement("STATUS");
          et.appendChild(document.createTextNode("C"));
          rocdoc1.appendChild(et);
        }
        if (ieCode == null)
        {
          Element sbn = document.createElement("IEC");
          sbn.appendChild(document.createTextNode(" "));
          rocdoc1.appendChild(sbn);
        }
        else
        {
          Element sbn = document.createElement("IEC");
          sbn.appendChild(document.createTextNode(ieCode));
          rocdoc1.appendChild(sbn);
        }
        if (exportName == null)
        {
          Element sbd = document.createElement("EXPNAME");
          sbd.appendChild(document.createTextNode(" "));
          rocdoc1.appendChild(sbd);
        }
        else
        {
          Element sbd = document.createElement("EXPNAME");
          sbd.appendChild(document.createTextNode(exportName));
          rocdoc1.appendChild(sbd);
        }
        if (codeIFSC == null)
        {
          Element fn = document.createElement("IFSC");
          fn.appendChild(document.createTextNode(" "));
          rocdoc1.appendChild(fn);
        }
        else
        {
          Element fn = document.createElement("IFSC");
          fn.appendChild(document.createTextNode(codeIFSC));
          rocdoc1.appendChild(fn);
        }
        if (billID == null)
        {
          Element ld = document.createElement("BILLID");
          ld.appendChild(document.createTextNode(" "));
          rocdoc1.appendChild(ld);
        }
        else
        {
          Element ld = document.createElement("BILLID");
          ld.appendChild(document.createTextNode(billID));
          rocdoc1.appendChild(ld);
        }
        if (shipNo == null)
        {
          Element ic = document.createElement("SNO");
          ic.appendChild(document.createTextNode(" "));
          rocdoc1.appendChild(ic);
        }
        else
        {
          Element ic = document.createElement("SNO");
          ic.appendChild(document.createTextNode(shipNo));
          rocdoc1.appendChild(ic);
        }
        if (shipPort == null)
        {
          Element cic = document.createElement("SPORT");
          cic.appendChild(document.createTextNode(" "));
          rocdoc1.appendChild(cic);
        }
        else
        {
          Element cic = document.createElement("SPORT");
          cic.appendChild(document.createTextNode(shipPort));
          rocdoc1.appendChild(cic);
        }
        if (shipDate == null)
        {
          Element ac = document.createElement("SDATE");
          ac.appendChild(document.createTextNode(" "));
          rocdoc1.appendChild(ac);
        }
        else
        {
          Element ac = document.createElement("SDATE");
          ac.appendChild(document.createTextNode(shipDate));
          rocdoc1.appendChild(ac);
        }
        if (shipCurr == null)
        {
          Element aea = document.createElement("SCC");
          aea.appendChild(document.createTextNode(" "));
          rocdoc1.appendChild(aea);
        }
        else
        {
          Element aea = document.createElement("SCC");
          aea.appendChild(document.createTextNode(shipCurr));
          rocdoc1.appendChild(aea);
        }
        if (shipValue == null)
        {
          Element dd = document.createElement("SVALUE");
          dd.appendChild(document.createTextNode(" "));
          rocdoc1.appendChild(dd);
        }
        else
        {
          Element dd = document.createElement("SVALUE");
          dd.appendChild(document.createTextNode(shipValue));
          rocdoc1.appendChild(dd);
        }
        if (realCurr == null)
        {
          Element abn = document.createElement("RCC");
          abn.appendChild(document.createTextNode(" "));
          rocdoc1.appendChild(abn);
        }
        else
        {
          Element abn = document.createElement("RCC");
          abn.appendChild(document.createTextNode(realCurr));
          rocdoc1.appendChild(abn);
        }
        if (realValue == null)
        {
          Element dn = document.createElement("RVALUE");
          dn.appendChild(document.createTextNode(" "));
          rocdoc1.appendChild(dn);
        }
        else
        {
          Element dn = document.createElement("RVALUE");
          dn.appendChild(document.createTextNode(realValue));
          rocdoc1.appendChild(dn);
        }
        if ((realDate == null) || (realDate == ""))
        {
          Element bn = document.createElement("RDATE");
          bn.appendChild(document.createTextNode(" "));
          rocdoc1.appendChild(bn);
        }
        else
        {
          Element bn = document.createElement("RDATE");
          bn.appendChild(document.createTextNode(realDate));
          rocdoc1.appendChild(bn);
        }
        if ((rmtBank == null) || (rmtBank == ""))
        {
          Element bn = document.createElement("RMTBANK");
          bn.appendChild(document.createTextNode(" "));
          rocdoc1.appendChild(bn);
        }
        else
        {
          Element bn = document.createElement("RMTBANK");
          bn.appendChild(document.createTextNode(rmtBank));
          rocdoc1.appendChild(bn);
        }
        if ((rmtCity == null) || (rmtCity == ""))
        {
          Element bn = document.createElement("RMTCITY");
          bn.appendChild(document.createTextNode(" "));
          rocdoc1.appendChild(bn);
        }
        else
        {
          Element bn = document.createElement("RMTCITY");
          bn.appendChild(document.createTextNode(rmtCity));
          rocdoc1.appendChild(bn);
        }
        if ((rmtCtry == null) || (rmtCtry == ""))
        {
          Element bn = document.createElement("RMTCTRY");
          bn.appendChild(document.createTextNode(" "));
          rocdoc1.appendChild(bn);
        }
        else
        {
          Element bn = document.createElement("RMTCTRY");
          bn.appendChild(document.createTextNode(rmtCtry));
          rocdoc1.appendChild(bn);
        }
        if ((freight == null) || (freight == ""))
        {
          Element bn = document.createElement("FREIGHT");
          bn.appendChild(document.createTextNode(" "));
          rocdoc1.appendChild(bn);
        }
        else
        {
          Element bn = document.createElement("FREIGHT");
          bn.appendChild(document.createTextNode(freight));
          rocdoc1.appendChild(bn);
        }
        if ((insurance == null) || (insurance == ""))
        {
          Element bn = document.createElement("INSURANCE");
          bn.appendChild(document.createTextNode(" "));
          rocdoc1.appendChild(bn);
        }
        else
        {
          Element bn = document.createElement("INSURANCE");
          bn.appendChild(document.createTextNode(insurance));
          rocdoc1.appendChild(bn);
        }
        if ((commission == null) || (commission == ""))
        {
          Element bn = document.createElement("COMMISSION");
          bn.appendChild(document.createTextNode(" "));
          rocdoc1.appendChild(bn);
        }
        else
        {
          Element bn = document.createElement("COMMISSION");
          bn.appendChild(document.createTextNode(commission));
          rocdoc1.appendChild(bn);
        }
        if ((isVostro == null) || (isVostro == ""))
        {
          Element bn = document.createElement("ISVOSTRO");
          bn.appendChild(document.createTextNode(" "));
          rocdoc1.appendChild(bn);
        }
        else
        {
          Element bn = document.createElement("ISVOSTRO");
          bn.appendChild(document.createTextNode(isVostro));
          rocdoc1.appendChild(bn);
        }
        if ((categoryOfExport == null) || (categoryOfExport == ""))
        {
          Element bn = document.createElement("EXPORTTYPE");
          bn.appendChild(document.createTextNode(" "));
          rocdoc1.appendChild(bn);
        }
        else
        {
          Element bn = document.createElement("EXPORTTYPE");
          bn.appendChild(document.createTextNode(categoryOfExport));
          rocdoc1.appendChild(bn);
        }
        if ((s_a_c == null) || (s_a_c == ""))
        {
          Element bn = document.createElement("SACCODE");
          bn.appendChild(document.createTextNode(""));
          rocdoc1.appendChild(bn);
        }
        else
        {
          Element bn = document.createElement("SACCODE");
          bn.appendChild(document.createTextNode(s_a_c));
          rocdoc1.appendChild(bn);
        }
        if ((isForFeit == null) || (isForFeit == ""))
        {
          Element bn = document.createElement("ISFORFEIT");
          bn.appendChild(document.createTextNode(" "));
          rocdoc1.appendChild(bn);
        }
        else
        {
          Element bn = document.createElement("ISFORFEIT");
          bn.appendChild(document.createTextNode(isForFeit));
          rocdoc1.appendChild(bn);
        }
        if ((isFactoring == null) || (isFactoring == ""))
        {
          Element bn = document.createElement("ISFACTOR");
          bn.appendChild(document.createTextNode(" "));
          rocdoc1.appendChild(bn);
        }
        else
        {
          Element bn = document.createElement("ISFACTOR");
          bn.appendChild(document.createTextNode(isFactoring));
          rocdoc1.appendChild(bn);
        }
        noOfRec++;
      }
      logger.info("noOfRec :: " + noOfRec);
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty("method", "html");
      DOMSource domSource = new DOMSource(document);
     
      String proQuery1 = "SELECT TO_CHAR(TO_DATE(PROCDATE,'DD-MM-YY'),'YYYYMMDD') AS PROCDATE FROM DLYPRCCYCL";
     
      PreparedStatement pps2 = con.prepareStatement(proQuery1);
      ResultSet rs2 = pps2.executeQuery();
      String eodDate = null;
      if (rs2.next()) {
        eodDate = rs2.getString("PROCDATE");
      }
      closeSqlRefferance(rs2, pps2);
     
      String fileLoc = "";
      fileLoc = ServiceLoggingUtil.getBridgePropertyValue("EBRC1");
     

      fileLoc = fileLoc + eodDate + "/";
      File f = new File(fileLoc);
      if (!f.exists()) {
        f.mkdir();
      }
      String myFilePath = fileLoc + fileName1 + ".xml";
     
      StringWriter writer = new StringWriter();
      transformer.transform(domSource, new StreamResult(writer));
      String output = writer.toString();
     

      File file = new File(myFilePath);
     
      file.createNewFile();
     
      FileWriter writer1 = new FileWriter(file);
     
      writer1.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>" + output);
      writer1.flush();
      writer1.close();
     
      logger.info("File has been successfully saved");
      if (noOfRec > 0)
      {
        downloadFiles(myFilePath);
       
        addActionMessage("File (" + fileName1 + ") Downloaded Successfully..");
      }
      else
      {
        addActionError("No date found.");
        return "error";
      }
    }
    catch (Exception e)
    {
      logger.error("Exception in EBRC Fie---" + e);
      result = "Error";
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, ps, res);
      logger.info("Finally Occurred in saveDetail");
    }
    DBConnectionUtility.surrenderDB(con, ps, res);
    logger.info("Finally Occurred in saveDetail");
   
    return result;
  }
 
  /* Error */
  public String downloadAdvicesFile()
  {
    // Byte code:
    //   0: getstatic 61      in/co/localization/action/FileUtilityAction:logger    Lorg/apache/log4j/Logger;
    //   3: new 188     java/lang/StringBuilder
    //   6: dup
    //   7: ldc_w 1169
    //   10: invokespecial 192      java/lang/StringBuilder:<init>      (Ljava/lang/String;)V
    //   13: aload_0
    //   14: getfield 68      in/co/localization/action/FileUtilityAction:cifId     Ljava/lang/String;
    //   17: invokevirtual 246      java/lang/StringBuilder:append      (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   20: ldc_w 1171
    //   23: invokevirtual 246      java/lang/StringBuilder:append      (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   26: aload_0
    //   27: getfield 105     in/co/localization/action/FileUtilityAction:mstReference    Ljava/lang/String;
    //   30: invokevirtual 246      java/lang/StringBuilder:append      (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   33: ldc_w 1173
    //   36: invokevirtual 246      java/lang/StringBuilder:append      (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   39: aload_0
    //   40: getfield 70      in/co/localization/action/FileUtilityAction:fileUtilFileName      Ljava/lang/String;
    //   43: invokevirtual 246      java/lang/StringBuilder:append      (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   46: ldc_w 397
    //   49: invokevirtual 246      java/lang/StringBuilder:append      (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   52: aload_0
    //   53: getfield 74      in/co/localization/action/FileUtilityAction:fileUtilFromDate      Ljava/lang/String;
    //   56: invokevirtual 246      java/lang/StringBuilder:append      (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   59: ldc_w 399
    //   62: invokevirtual 246      java/lang/StringBuilder:append      (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   65: aload_0
    //   66: getfield 76      in/co/localization/action/FileUtilityAction:fileUtilToDate  Ljava/lang/String;
    //   69: invokevirtual 246      java/lang/StringBuilder:append      (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   72: invokevirtual 198      java/lang/StringBuilder:toString    ()Ljava/lang/String;
    //   75: invokevirtual 201      org/apache/log4j/Logger:info  (Ljava/lang/Object;)V
    //   78: aconst_null
    //   79: astore_1
    //   80: aconst_null
    //   81: astore_2
    //   82: aconst_null
    //   83: astore_3
    //   84: aconst_null
    //   85: astore 4
    //   87: aconst_null
    //   88: astore 5
    //   90: aconst_null
    //   91: astore 6
    //   93: aconst_null
    //   94: astore 7
    //   96: ldc 180
    //   98: astore 8
    //   100: invokestatic 361      in/co/localization/action/DBConnectionUtility:getConnection ()Ljava/sql/Connection;
    //   103: astore_1
    //   104: ldc 209
    //   106: astore 9
    //   108: ldc_w 1175
    //   111: astore 10
    //   113: aload_1
    //   114: invokeinterface 1177 1 0
    //   119: astore_3
    //   120: aload_3
    //   121: aload 10
    //   123: invokeinterface 1181 2 0
    //   128: astore 5
    //   130: goto +13 -> 143
    //   133: aload 5
    //   135: iconst_1
    //   136: invokeinterface 1186 2 0
    //   141: astore 10
    //   143: aload 5
    //   145: invokeinterface 295 1 0
    //   150: ifne -17 -> 133
    //   153: aload 5
    //   155: invokeinterface 1189 1 0
    //   160: aload_3
    //   161: invokeinterface 1190 1 0
    //   166: getstatic 61    in/co/localization/action/FileUtilityAction:logger    Lorg/apache/log4j/Logger;
    //   169: new 188   java/lang/StringBuilder
    //   172: dup
    //   173: ldc_w 1191
    //   176: invokespecial 192     java/lang/StringBuilder:<init>      (Ljava/lang/String;)V
    //   179: aload 10
    //   181: invokevirtual 246     java/lang/StringBuilder:append      (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   184: invokevirtual 198     java/lang/StringBuilder:toString    ()Ljava/lang/String;
    //   187: invokevirtual 201     org/apache/log4j/Logger:info  (Ljava/lang/Object;)V
    //   190: ldc_w 1193
    //   193: invokestatic 436      in/co/localization/action/ServiceLoggingUtil:getBridgePropertyValue     (Ljava/lang/String;)Ljava/lang/String;
    //   196: astore 9
    //   198: new 188   java/lang/StringBuilder
    //   201: dup
    //   202: aload 9
    //   204: invokestatic 454      java/lang/String:valueOf      (Ljava/lang/Object;)Ljava/lang/String;
    //   207: invokespecial 192     java/lang/StringBuilder:<init>      (Ljava/lang/String;)V
    //   210: aload 10
    //   212: invokevirtual 246     java/lang/StringBuilder:append      (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   215: ldc_w 542
    //   218: invokevirtual 246     java/lang/StringBuilder:append      (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   221: invokevirtual 198     java/lang/StringBuilder:toString    ()Ljava/lang/String;
    //   224: astore 9
    //   226: new 443   java/io/File
    //   229: dup
    //   230: aload 9
    //   232: invokespecial 445     java/io/File:<init>     (Ljava/lang/String;)V
    //   235: astore 11
    //   237: aload 11
    //   239: invokevirtual 446     java/io/File:exists     ()Z
    //   242: ifne +23 -> 265
    //   245: aload 11
    //   247: invokevirtual 449     java/io/File:mkdir      ()Z
    //   250: pop
    //   251: getstatic 61    in/co/localization/action/FileUtilityAction:logger    Lorg/apache/log4j/Logger;
    //   254: aload 11
    //   256: invokevirtual 449     java/io/File:mkdir      ()Z
    //   259: invokestatic 544      java/lang/Boolean:valueOf     (Z)Ljava/lang/Boolean;
    //   262: invokevirtual 201     org/apache/log4j/Logger:info  (Ljava/lang/Object;)V
    //   265: ldc 209
    //   267: astore 12
    //   269: aload_0
    //   270: getfield 105    in/co/localization/action/FileUtilityAction:mstReference    Ljava/lang/String;
    //   273: ldc 209
    //   275: invokevirtual 1156    java/lang/String:equalsIgnoreCase   (Ljava/lang/String;)Z
    //   278: ifne +58 -> 336
    //   281: new 188   java/lang/StringBuilder
    //   284: dup
    //   285: ldc_w 1195
    //   288: invokespecial 192     java/lang/StringBuilder:<init>      (Ljava/lang/String;)V
    //   291: aload_0
    //   292: getfield 105    in/co/localization/action/FileUtilityAction:mstReference    Ljava/lang/String;
    //   295: invokevirtual 246     java/lang/StringBuilder:append      (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   298: ldc_w 540
    //   301: invokevirtual 246     java/lang/StringBuilder:append      (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   304: ldc_w 1197
    //   307: invokevirtual 246     java/lang/StringBuilder:append      (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   310: ldc_w 1199
    //   313: invokevirtual 246     java/lang/StringBuilder:append      (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   316: ldc_w 1201
    //   319: invokevirtual 246     java/lang/StringBuilder:append      (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   322: ldc_w 1203
    //   325: invokevirtual 246     java/lang/StringBuilder:append      (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   328: invokevirtual 198     java/lang/StringBuilder:toString    ()Ljava/lang/String;
    //   331: astore 12
    //   333: goto +87 -> 420
    //   336: new 188   java/lang/StringBuilder
    //   339: dup
    //   340: ldc_w 1205
    //   343: invokespecial 192     java/lang/StringBuilder:<init>      (Ljava/lang/String;)V
    //   346: aload_0
    //   347: getfield 68     in/co/localization/action/FileUtilityAction:cifId     Ljava/lang/String;
    //   350: invokevirtual 246     java/lang/StringBuilder:append      (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   353: ldc_w 540
    //   356: invokevirtual 246     java/lang/StringBuilder:append      (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   359: ldc_w 1197
    //   362: invokevirtual 246     java/lang/StringBuilder:append      (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   365: ldc_w 1199
    //   368: invokevirtual 246     java/lang/StringBuilder:append      (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   371: ldc_w 1207
    //   374: invokevirtual 246     java/lang/StringBuilder:append      (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   377: aload_0
    //   378: getfield 74     in/co/localization/action/FileUtilityAction:fileUtilFromDate      Ljava/lang/String;
    //   381: invokevirtual 246     java/lang/StringBuilder:append      (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   384: ldc_w 430
    //   387: invokevirtual 246     java/lang/StringBuilder:append      (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   390: aload_0
    //   391: getfield 76     in/co/localization/action/FileUtilityAction:fileUtilToDate  Ljava/lang/String;
    //   394: invokevirtual 246     java/lang/StringBuilder:append      (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   397: ldc_w 540
    //   400: invokevirtual 246     java/lang/StringBuilder:append      (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   403: ldc_w 1201
    //   406: invokevirtual 246     java/lang/StringBuilder:append      (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   409: ldc_w 1203
    //   412: invokevirtual 246     java/lang/StringBuilder:append      (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   415: invokevirtual 198     java/lang/StringBuilder:toString    ()Ljava/lang/String;
    //   418: astore 12
    //   420: aload_1
    //   421: invokeinterface 1177 1 0
    //   426: astore_2
    //   427: aload_2
    //   428: aload 12
    //   430: invokeinterface 1181 2 0
    //   435: astore 4
    //   437: getstatic 61    in/co/localization/action/FileUtilityAction:logger    Lorg/apache/log4j/Logger;
    //   440: new 188   java/lang/StringBuilder
    //   443: dup
    //   444: ldc_w 1209
    //   447: invokespecial 192     java/lang/StringBuilder:<init>      (Ljava/lang/String;)V
    //   450: aload 12
    //   452: invokevirtual 246     java/lang/StringBuilder:append      (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   455: invokevirtual 198     java/lang/StringBuilder:toString    ()Ljava/lang/String;
    //   458: invokevirtual 201     org/apache/log4j/Logger:info  (Ljava/lang/Object;)V
    //   461: iconst_0
    //   462: istore 13
    //   464: goto +146 -> 610
    //   467: iinc 13 1
    //   470: aload 4
    //   472: iconst_1
    //   473: invokeinterface 1186 2 0
    //   478: astore 14
    //   480: aload 4
    //   482: iconst_2
    //   483: invokeinterface 1186 2 0
    //   488: astore 15
    //   490: aload 4
    //   492: iconst_3
    //   493: invokeinterface 1186 2 0
    //   498: astore 16
    //   500: aload 4
    //   502: iconst_4
    //   503: invokeinterface 1211 2 0
    //   508: astore 17
    //   510: aload 17
    //   512: invokeinterface 1215 1 0
    //   517: astore 7
    //   519: new 1220  java/io/FileOutputStream
    //   522: dup
    //   523: new 188   java/lang/StringBuilder
    //   526: dup
    //   527: aload 9
    //   529: invokestatic 454      java/lang/String:valueOf      (Ljava/lang/Object;)Ljava/lang/String;
    //   532: invokespecial 192     java/lang/StringBuilder:<init>      (Ljava/lang/String;)V
    //   535: aload 14
    //   537: invokevirtual 633     java/lang/String:trim   ()Ljava/lang/String;
    //   540: invokevirtual 246     java/lang/StringBuilder:append      (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   543: ldc_w 1222
    //   546: invokevirtual 246     java/lang/StringBuilder:append      (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   549: aload 15
    //   551: invokevirtual 633     java/lang/String:trim   ()Ljava/lang/String;
    //   554: invokevirtual 246     java/lang/StringBuilder:append      (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   557: ldc_w 1222
    //   560: invokevirtual 246     java/lang/StringBuilder:append      (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   563: aload 16
    //   565: invokevirtual 633     java/lang/String:trim   ()Ljava/lang/String;
    //   568: invokevirtual 246     java/lang/StringBuilder:append      (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   571: ldc_w 1224
    //   574: invokevirtual 246     java/lang/StringBuilder:append      (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   577: invokevirtual 198     java/lang/StringBuilder:toString    ()Ljava/lang/String;
    //   580: invokespecial 1226    java/io/FileOutputStream:<init>     (Ljava/lang/String;)V
    //   583: astore 6
    //   585: iconst_0
    //   586: istore 18
    //   588: goto +10 -> 598
    //   591: aload 6
    //   593: iload 18
    //   595: invokevirtual 1227    java/io/FileOutputStream:write      (I)V
    //   598: aload 7
    //   600: invokevirtual 1230    java/io/InputStream:read      ()I
    //   603: dup
    //   604: istore 18
    //   606: iconst_m1
    //   607: if_icmpne -16 -> 591
    //   610: aload 4
    //   612: invokeinterface 295 1 0
    //   617: ifne -150 -> 467
    //   620: new 188   java/lang/StringBuilder
    //   623: dup
    //   624: aload 9
    //   626: invokestatic 454      java/lang/String:valueOf      (Ljava/lang/Object;)Ljava/lang/String;
    //   629: invokespecial 192     java/lang/StringBuilder:<init>      (Ljava/lang/String;)V
    //   632: aload 10
    //   634: invokevirtual 246     java/lang/StringBuilder:append      (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   637: ldc_w 1235
    //   640: invokevirtual 246     java/lang/StringBuilder:append      (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   643: invokevirtual 198     java/lang/StringBuilder:toString    ()Ljava/lang/String;
    //   646: astore 14
    //   648: new 443   java/io/File
    //   651: dup
    //   652: aload 14
    //   654: invokespecial 445     java/io/File:<init>     (Ljava/lang/String;)V
    //   657: astore 15
    //   659: aload 15
    //   661: invokevirtual 446     java/io/File:exists     ()Z
    //   664: ifne +343 -> 1007
    //   667: new 443   java/io/File
    //   670: dup
    //   671: aload 9
    //   673: invokespecial 445     java/io/File:<init>     (Ljava/lang/String;)V
    //   676: astore 16
    //   678: aload 16
    //   680: invokevirtual 446     java/io/File:exists     ()Z
    //   683: ifeq +324 -> 1007
    //   686: aload_0
    //   687: aload 16
    //   689: aload 14
    //   691: invokespecial 1237    in/co/localization/action/FileUtilityAction:zipDirectory    (Ljava/io/File;Ljava/lang/String;)V
    //   694: new 443   java/io/File
    //   697: dup
    //   698: aload 14
    //   700: invokespecial 445     java/io/File:<init>     (Ljava/lang/String;)V
    //   703: astore 17
    //   705: aload 17
    //   707: invokevirtual 446     java/io/File:exists     ()Z
    //   710: ifeq +297 -> 1007
    //   713: aload_0
    //   714: new 1241  java/io/FileInputStream
    //   717: dup
    //   718: aload 17
    //   720: invokespecial 1243    java/io/FileInputStream:<init>      (Ljava/io/File;)V
    //   723: putfield 116    in/co/localization/action/FileUtilityAction:inputStream     Ljava/io/InputStream;
    //   726: aload_0
    //   727: aload 17
    //   729: invokevirtual 1244    java/io/File:getName    ()Ljava/lang/String;
    //   732: putfield 121    in/co/localization/action/FileUtilityAction:fileName  Ljava/lang/String;
    //   735: aload_0
    //   736: aload 17
    //   738: invokevirtual 1245    java/io/File:length     ()J
    //   741: putfield 126    in/co/localization/action/FileUtilityAction:contentLength   J
    //   744: goto +263 -> 1007
    //   747: astore 9
    //   749: aload 9
    //   751: invokevirtual 1248    java/io/IOException:getMessage      ()Ljava/lang/String;
    //   754: pop
    //   755: aload 9
    //   757: invokevirtual 1249    java/io/IOException:printStackTrace ()V
    //   760: getstatic 61    in/co/localization/action/FileUtilityAction:logger    Lorg/apache/log4j/Logger;
    //   763: aload 9
    //   765: invokevirtual 201     org/apache/log4j/Logger:info  (Ljava/lang/Object;)V
    //   768: aload 7
    //   770: invokevirtual 1250    java/io/InputStream:close     ()V
    //   773: aload 6
    //   775: invokevirtual 1251    java/io/FileOutputStream:close      ()V
    //   778: aload 4
    //   780: invokeinterface 1189 1 0
    //   785: goto +10 -> 795
    //   788: astore 20
    //   790: aload 20
    //   792: invokevirtual 1252    java/sql/SQLException:printStackTrace     ()V
    //   795: aload_2
    //   796: invokeinterface 1190 1 0
    //   801: goto +10 -> 811
    //   804: astore 20
    //   806: aload 20
    //   808: invokevirtual 1252    java/sql/SQLException:printStackTrace     ()V
    //   811: aload_1
    //   812: invokeinterface 1255 1 0
    //   817: goto +259 -> 1076
    //   820: astore 20
    //   822: aload 20
    //   824: invokevirtual 1252    java/sql/SQLException:printStackTrace     ()V
    //   827: goto +249 -> 1076
    //   830: astore 20
    //   832: aload 20
    //   834: invokevirtual 1249    java/io/IOException:printStackTrace ()V
    //   837: goto +239 -> 1076
    //   840: astore 9
    //   842: aload 9
    //   844: invokevirtual 1256    java/sql/SQLException:getMessage    ()Ljava/lang/String;
    //   847: pop
    //   848: aload 9
    //   850: invokevirtual 1252    java/sql/SQLException:printStackTrace     ()V
    //   853: getstatic 61    in/co/localization/action/FileUtilityAction:logger    Lorg/apache/log4j/Logger;
    //   856: aload 9
    //   858: invokevirtual 201     org/apache/log4j/Logger:info  (Ljava/lang/Object;)V
    //   861: aload 7
    //   863: invokevirtual 1250    java/io/InputStream:close     ()V
    //   866: aload 6
    //   868: invokevirtual 1251    java/io/FileOutputStream:close      ()V
    //   871: aload 4
    //   873: invokeinterface 1189 1 0
    //   878: goto +10 -> 888
    //   881: astore 20
    //   883: aload 20
    //   885: invokevirtual 1252    java/sql/SQLException:printStackTrace     ()V
    //   888: aload_2
    //   889: invokeinterface 1190 1 0
    //   894: goto +10 -> 904
    //   897: astore 20
    //   899: aload 20
    //   901: invokevirtual 1252    java/sql/SQLException:printStackTrace     ()V
    //   904: aload_1
    //   905: invokeinterface 1255 1 0
    //   910: goto +166 -> 1076
    //   913: astore 20
    //   915: aload 20
    //   917: invokevirtual 1252    java/sql/SQLException:printStackTrace     ()V
    //   920: goto +156 -> 1076
    //   923: astore 20
    //   925: aload 20
    //   927: invokevirtual 1249    java/io/IOException:printStackTrace ()V
    //   930: goto +146 -> 1076
    //   933: astore 19
    //   935: aload 7
    //   937: invokevirtual 1250    java/io/InputStream:close     ()V
    //   940: aload 6
    //   942: invokevirtual 1251    java/io/FileOutputStream:close      ()V
    //   945: aload 4
    //   947: invokeinterface 1189 1 0
    //   952: goto +10 -> 962
    //   955: astore 20
    //   957: aload 20
    //   959: invokevirtual 1252    java/sql/SQLException:printStackTrace     ()V
    //   962: aload_2
    //   963: invokeinterface 1190 1 0
    //   968: goto +10 -> 978
    //   971: astore 20
    //   973: aload 20
    //   975: invokevirtual 1252    java/sql/SQLException:printStackTrace     ()V
    //   978: aload_1
    //   979: invokeinterface 1255 1 0
    //   984: goto +20 -> 1004
    //   987: astore 20
    //   989: aload 20
    //   991: invokevirtual 1252    java/sql/SQLException:printStackTrace     ()V
    //   994: goto +10 -> 1004
    //   997: astore 20
    //   999: aload 20
    //   1001: invokevirtual 1249   java/io/IOException:printStackTrace ()V
    //   1004: aload 19
    //   1006: athrow
    //   1007: aload 7
    //   1009: invokevirtual 1250   java/io/InputStream:close     ()V
    //   1012: aload 6
    //   1014: invokevirtual 1251   java/io/FileOutputStream:close      ()V
    //   1017: aload 4
    //   1019: invokeinterface 1189 1 0
    //   1024: goto +10 -> 1034
    //   1027: astore 20
    //   1029: aload 20
    //   1031: invokevirtual 1252   java/sql/SQLException:printStackTrace     ()V
    //   1034: aload_2
    //   1035: invokeinterface 1190 1 0
    //   1040: goto +10 -> 1050
    //   1043: astore 20
    //   1045: aload 20
    //   1047: invokevirtual 1252   java/sql/SQLException:printStackTrace     ()V
    //   1050: aload_1
    //   1051: invokeinterface 1255 1 0
    //   1056: goto +20 -> 1076
    //   1059: astore 20
    //   1061: aload 20
    //   1063: invokevirtual 1252   java/sql/SQLException:printStackTrace     ()V
    //   1066: goto +10 -> 1076
    //   1069: astore 20
    //   1071: aload 20
    //   1073: invokevirtual 1249   java/io/IOException:printStackTrace ()V
    //   1076: aload 8
    //   1078: areturn
    // Line number table:
    //   Java source line #3033     -> byte code offset #0
    //   Java source line #3034     -> byte code offset #78
    //   Java source line #3035     -> byte code offset #80
    //   Java source line #3036     -> byte code offset #82
    //   Java source line #3037     -> byte code offset #84
    //   Java source line #3038     -> byte code offset #87
    //   Java source line #3039     -> byte code offset #90
    //   Java source line #3040     -> byte code offset #93
    //   Java source line #3041     -> byte code offset #96
    //   Java source line #3043     -> byte code offset #100
    //   Java source line #3044     -> byte code offset #104
    //   Java source line #3047     -> byte code offset #108
    //   Java source line #3048     -> byte code offset #113
    //   Java source line #3049     -> byte code offset #120
    //   Java source line #3050     -> byte code offset #130
    //   Java source line #3051     -> byte code offset #133
    //   Java source line #3050     -> byte code offset #143
    //   Java source line #3053     -> byte code offset #153
    //   Java source line #3054     -> byte code offset #160
    //   Java source line #3055     -> byte code offset #166
    //   Java source line #3056     -> byte code offset #190
    //   Java source line #3057     -> byte code offset #198
    //   Java source line #3058     -> byte code offset #226
    //   Java source line #3059     -> byte code offset #237
    //   Java source line #3060     -> byte code offset #245
    //   Java source line #3061     -> byte code offset #251
    //   Java source line #3063     -> byte code offset #265
    //   Java source line #3065     -> byte code offset #269
    //   Java source line #3066     -> byte code offset #281
    //   Java source line #3082     -> byte code offset #291
    //   Java source line #3083     -> byte code offset #304
    //   Java source line #3084     -> byte code offset #310
    //   Java source line #3085     -> byte code offset #316
    //   Java source line #3086     -> byte code offset #322
    //   Java source line #3066     -> byte code offset #328
    //   Java source line #3087     -> byte code offset #333
    //   Java source line #3088     -> byte code offset #336
    //   Java source line #3105     -> byte code offset #346
    //   Java source line #3107     -> byte code offset #359
    //   Java source line #3108     -> byte code offset #365
    //   Java source line #3109     -> byte code offset #371
    //   Java source line #3110     -> byte code offset #403
    //   Java source line #3111     -> byte code offset #409
    //   Java source line #3088     -> byte code offset #415
    //   Java source line #3115     -> byte code offset #420
    //   Java source line #3116     -> byte code offset #427
    //   Java source line #3117     -> byte code offset #437
    //   Java source line #3118     -> byte code offset #461
    //   Java source line #3119     -> byte code offset #464
    //   Java source line #3121     -> byte code offset #467
    //   Java source line #3122     -> byte code offset #470
    //   Java source line #3123     -> byte code offset #480
    //   Java source line #3124     -> byte code offset #490
    //   Java source line #3126     -> byte code offset #500
    //   Java source line #3128     -> byte code offset #510
    //   Java source line #3129     -> byte code offset #519
    //   Java source line #3131     -> byte code offset #585
    //   Java source line #3132     -> byte code offset #588
    //   Java source line #3134     -> byte code offset #591
    //   Java source line #3132     -> byte code offset #598
    //   Java source line #3119     -> byte code offset #610
    //   Java source line #3139     -> byte code offset #620
    //   Java source line #3140     -> byte code offset #648
    //   Java source line #3142     -> byte code offset #659
    //   Java source line #3144     -> byte code offset #667
    //   Java source line #3145     -> byte code offset #678
    //   Java source line #3147     -> byte code offset #686
    //   Java source line #3149     -> byte code offset #694
    //   Java source line #3151     -> byte code offset #705
    //   Java source line #3152     -> byte code offset #713
    //   Java source line #3153     -> byte code offset #726
    //   Java source line #3154     -> byte code offset #735
    //   Java source line #3165     -> byte code offset #744
    //   Java source line #3166     -> byte code offset #749
    //   Java source line #3167     -> byte code offset #760
    //   Java source line #3175     -> byte code offset #768
    //   Java source line #3176     -> byte code offset #773
    //   Java source line #3178     -> byte code offset #778
    //   Java source line #3179     -> byte code offset #785
    //   Java source line #3180     -> byte code offset #790
    //   Java source line #3183     -> byte code offset #795
    //   Java source line #3184     -> byte code offset #801
    //   Java source line #3185     -> byte code offset #806
    //   Java source line #3188     -> byte code offset #811
    //   Java source line #3189     -> byte code offset #817
    //   Java source line #3190     -> byte code offset #822
    //   Java source line #3192     -> byte code offset #827
    //   Java source line #3193     -> byte code offset #832
    //   Java source line #3169     -> byte code offset #840
    //   Java source line #3170     -> byte code offset #842
    //   Java source line #3171     -> byte code offset #853
    //   Java source line #3175     -> byte code offset #861
    //   Java source line #3176     -> byte code offset #866
    //   Java source line #3178     -> byte code offset #871
    //   Java source line #3179     -> byte code offset #878
    //   Java source line #3180     -> byte code offset #883
    //   Java source line #3183     -> byte code offset #888
    //   Java source line #3184     -> byte code offset #894
    //   Java source line #3185     -> byte code offset #899
    //   Java source line #3188     -> byte code offset #904
    //   Java source line #3189     -> byte code offset #910
    //   Java source line #3190     -> byte code offset #915
    //   Java source line #3192     -> byte code offset #920
    //   Java source line #3193     -> byte code offset #925
    //   Java source line #3173     -> byte code offset #933
    //   Java source line #3175     -> byte code offset #935
    //   Java source line #3176     -> byte code offset #940
    //   Java source line #3178     -> byte code offset #945
    //   Java source line #3179     -> byte code offset #952
    //   Java source line #3180     -> byte code offset #957
    //   Java source line #3183     -> byte code offset #962
    //   Java source line #3184     -> byte code offset #968
    //   Java source line #3185     -> byte code offset #973
    //   Java source line #3188     -> byte code offset #978
    //   Java source line #3189     -> byte code offset #984
    //   Java source line #3190     -> byte code offset #989
    //   Java source line #3192     -> byte code offset #994
    //   Java source line #3193     -> byte code offset #999
    //   Java source line #3195     -> byte code offset #1004
    //   Java source line #3175     -> byte code offset #1007
    //   Java source line #3176     -> byte code offset #1012
    //   Java source line #3178     -> byte code offset #1017
    //   Java source line #3179     -> byte code offset #1024
    //   Java source line #3180     -> byte code offset #1029
    //   Java source line #3183     -> byte code offset #1034
    //   Java source line #3184     -> byte code offset #1040
    //   Java source line #3185     -> byte code offset #1045
    //   Java source line #3188     -> byte code offset #1050
    //   Java source line #3189     -> byte code offset #1056
    //   Java source line #3190     -> byte code offset #1061
    //   Java source line #3192     -> byte code offset #1066
    //   Java source line #3193     -> byte code offset #1071
    //   Java source line #3196     -> byte code offset #1076
    // Local variable table:
    //   start    length      slot  name  signature
    //   0  1079  0     this  FileUtilityAction
    //   79 972   1     connection  Connection
    //   81 954   2     statement   Statement
    //   83 78    3     statement1  Statement
    //   85 933   4     rs    ResultSet
    //   88 66    5     rs1   ResultSet
    //   91 922   6     fos   FileOutputStream
    //   94 914   7     is    InputStream
    //   98 979   8     result      String
    //   106      566   9     fileLoc     String
    //   747      17    9     e     IOException
    //   840      17    9     e     SQLException
    //   111      522   10    fileNameFormat    String
    //   235      20    11    f     File
    //   267      184   12    queryCsvbill      String
    //   462      6     13    cnt   int
    //   478      58    14    masterRef   String
    //   646      53    14    zipFileName String
    //   488      62    15    eventRef    String
    //   657      3     15    isAlreadyExists   File
    //   498      66    16    descr String
    //   676      12    16    f1    File
    //   508      3     17    blob  java.sql.Blob
    //   703      34    17    fileToDownload    File
    //   586      19    18    b     int
    //   933      72    19    localObject Object
    //   788      3     20    e     SQLException
    //   804      3     20    e     SQLException
    //   820      3     20    e     SQLException
    //   830      3     20    e     IOException
    //   881      3     20    e     SQLException
    //   897      3     20    e     SQLException
    //   913      3     20    e     SQLException
    //   923      3     20    e     IOException
    //   955      3     20    e     SQLException
    //   971      3     20    e     SQLException
    //   987      3     20    e     SQLException
    //   997      3     20    e     IOException
    //   1027     3     20    e     SQLException
    //   1043     3     20    e     SQLException
    //   1059     3     20    e     SQLException
    //   1069     3     20    e     IOException
    // Exception table:
    //   from     to    target      type
    //   100      744   747   java/io/IOException
    //   778      785   788   java/sql/SQLException
    //   795      801   804   java/sql/SQLException
    //   811      817   820   java/sql/SQLException
    //   768      827   830   java/io/IOException
    //   100      744   840   java/sql/SQLException
    //   871      878   881   java/sql/SQLException
    //   888      894   897   java/sql/SQLException
    //   904      910   913   java/sql/SQLException
    //   861      920   923   java/io/IOException
    //   100      768   933   finally
    //   840      861   933   finally
    //   945      952   955   java/sql/SQLException
    //   962      968   971   java/sql/SQLException
    //   978      984   987   java/sql/SQLException
    //   935      994   997   java/io/IOException
    //   1017     1024  1027  java/sql/SQLException
    //   1034     1040  1043  java/sql/SQLException
    //   1050     1056  1059  java/sql/SQLException
    //   1007     1066  1069  java/io/IOException
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
 
  public String setDate(Connection con)
  {
    PreparedStatement pst = null;
    ResultSet rs = null;
    CommonMethods commonMethods = null;
    String dateValue = null;
    try
    {
      commonMethods = new CommonMethods();
      if (con == null) {
        con = DBConnectionUtility.getConnection();
      }
      String query = "SELECT TO_CHAR(TO_DATE(PROCDATE, 'dd-mm-yy'),'dd-mm-yyyy') as PROCDATE FROM dlyprccycl";
      pst = con.prepareStatement(query);
      rs = pst.executeQuery();
      if (rs.next()) {
        dateValue = commonMethods.getEmptyIfNull(rs.getString("PROCDATE")).trim();
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(null, pst, rs);
    }
    return dateValue;
  }
 
  public static int generateManualTBMLItxtFile(String sqlQuery, String fileNameWithPath)
  {
    Connection connection = null;
    Statement stmt = null;
    ResultSet resultSet = null;
    boolean isheaderExists = false;
    logger.info("generateManualtxtFile");
    int noOfRecords = 0;
    try
    {
      connection = DBConnectionUtility.getConnection();
     
      PrintWriter csvWriter = null;
     
      stmt = connection.createStatement();
      resultSet = stmt.executeQuery(sqlQuery);
     

      ResultSetMetaData meta = resultSet.getMetaData();
      int numberOfColumns = meta.getColumnCount();
      String dataHeaders = "TRAN_DATE|TRAN_ID|ACCOUNT|CUSTID|CUST_NAME|TXN_TYPE|INWARDOUTWARD|TXN_AMT_INR|PURPOSE_CODE|TXN_AMT_FCNR|BILL_CRNCY_CODE|CONV_RATE|TRAN_PARTICULAR|GOODS_N_EXP|GOODS_N_IMP|IMPORT_DATE|REMIT_DATE|REMITTER_NAME|REMITTER_ADDRESS|REMITTER_COUNTRY|TXN_TYPE_1|MAKER|CHECKER|NOOFAMENDMENTS|FLG|BENEFICIARY_NAME|IRM_BILL_ID|LC_NUMBER|GAP_DAYS|IRM_AMT|TOTAL_IRMLINK_AMT|OUTSTANDING_IRM_AMT|ORM_BILL|BOE_AMT";
      while (resultSet.next())
      {
        String row = resultSet.getString(1);
        for (int i = 2; i < numberOfColumns + 1; i++)
        {
          String data = returnSpaceifNull(resultSet.getString(i));
         
          data = data.replaceAll(",", " ");
          if (i <= numberOfColumns) {
            row = row + "|" + data;
          } else {
            row = row + "|" + data + "|";
          }
        }
        if (!isheaderExists)
        {
          csvWriter = new PrintWriter(new File(fileNameWithPath));
         
          csvWriter.println(dataHeaders);
         
          isheaderExists = true;
        }
        csvWriter.println(row);
       
        noOfRecords++;
      }
      if (csvWriter != null) {
        csvWriter.close();
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      noOfRecords = 0;
      try
      {
        connection.close();
        stmt.close();
        resultSet.close();
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
        connection.close();
        stmt.close();
        resultSet.close();
      }
      catch (Exception e)
      {
        logger.info(e.getMessage());
      }
    }
    return noOfRecords;
  }
 
  public static int generateManualTBMLOtxtFile(String sqlQuery, String fileNameWithPath)
  {
    Connection connection = null;
    Statement stmt = null;
    ResultSet resultSet = null;
    boolean isheaderExists = false;
    logger.info("generateManualtxtFile");
    int noOfRecords = 0;
    try
    {
      connection = DBConnectionUtility.getConnection();
     
      PrintWriter csvWriter = null;
     
      stmt = connection.createStatement();
      resultSet = stmt.executeQuery(sqlQuery);
     

      ResultSetMetaData meta = resultSet.getMetaData();
      int numberOfColumns = meta.getColumnCount();
      String dataHeaders = "TRAN_DATE|TRAN_ID|ACCOUNT|CUSTID|CUST_NAME|TXN_TYPE|INWARDOUTWARD|TXN_AMT_INR|PURPOSE_CODE|TXN_AMT_FCNR|BILL_CRNCY_CODE|CONV_RATE|TRAN_PARTICULAR|GOODS_N_EXP|GOODS_N_IMP|IMPORT_DATE|REMIT_DATE|REMITTER_NAME|REMITTER_ADDRESS|REMITTER_COUNTRY|TXN_TYPE_1|MAKER|CHECKER|NOOFAMENDMENTS|FLG|BENEFICIARY_NAME|ORM_BILL_ID|LC_NUMBER|BLANK|INVOICE_NUMBER|INVOICE_DATE|GAP_DAYS|ORM_AMT|TOTAL_ORMLINK_AMT|OUTSTANDING_ORM_AMT|ORM_BILL|BOE_AMT|BENEFICIARY_COUNTRY";
      while (resultSet.next())
      {
        String row = resultSet.getString(1);
        for (int i = 2; i < numberOfColumns + 1; i++)
        {
          String data = returnSpaceifNull(resultSet.getString(i));
         
          data = data.replaceAll(",", " ");
          if (i <= numberOfColumns) {
            row = row + "|" + data;
          } else {
            row = row + "|" + data + "|";
          }
        }
        if (!isheaderExists)
        {
          csvWriter = new PrintWriter(new File(fileNameWithPath));
         
          csvWriter.println(dataHeaders);
         
          isheaderExists = true;
        }
        csvWriter.println(row);
       
        noOfRecords++;
      }
      if (csvWriter != null) {
        csvWriter.close();
      }
    }
    catch (Exception e)
    {
      logger.info(e.getMessage());
      e.printStackTrace();
      noOfRecords = 0;
      try
      {
        connection.close();
        stmt.close();
        resultSet.close();
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
        connection.close();
        stmt.close();
        resultSet.close();
      }
      catch (Exception e)
      {
        logger.info(e.getMessage());
      }
    }
    return noOfRecords;
  }
 
  public static int generateManualDRI_INWARDCSVFile(String sqlQuery, String fileNameWithPath)
  {
    boolean result = true;
    Connection connection = null;
    Statement stmt = null;
    ResultSet resultSet = null;
    boolean isheaderExists = false;
    int noOfRecords = 0;
    logger.info("generateManualCSVFile");
    try
    {
      connection = DBConnectionUtility.getConnection();
     
      PrintWriter csvWriter = null;
     
      stmt = connection.createStatement();
      resultSet = stmt.executeQuery(sqlQuery);
     

      ResultSetMetaData meta = resultSet.getMetaData();
      int numberOfColumns = meta.getColumnCount();
      String dataHeaders = "IRMNUMBER, PURPOSECODE, REMITTEENAME, REMITTEEADDR, REMITTEEPAN, REMITTEEGSTIN, REMITTEEAADHAAR, IFSCCODE, REMITTEEBANKAC, REMITTERNAME, REMITTERADDR, SWIFTBIC, REMITTERBANKAC, CURCODE, AMOUNT_FC, AMOUNT_INR, REMITTANCEDT, PAYMENTMODE, SB_DT, SB_NO, REMARKS ";
      while (resultSet.next())
      {
        String row = resultSet.getString(1);
        String portCode = row;
        String no = "";
        String Date = "";
        for (int i = 2; i < numberOfColumns + 1; i++)
        {
          String data = returnSpaceifNull(resultSet.getString(i));
          if (i == 3) {
            no = data;
          } else if (i == 4) {
            Date = data;
          }
          data = data.replaceAll(",", " ");
         
          row = row + "," + data;
        }
        if (!isheaderExists)
        {
          csvWriter = new PrintWriter(new File(fileNameWithPath));
         
          csvWriter.println(dataHeaders);
         
          isheaderExists = true;
        }
        csvWriter.println(row);
        noOfRecords++;
      }
      if (csvWriter != null) {
        csvWriter.close();
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      noOfRecords = 0;
      try
      {
        connection.close();
        stmt.close();
        resultSet.close();
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
        connection.close();
        stmt.close();
        resultSet.close();
      }
      catch (Exception e)
      {
        logger.info(e.getMessage());
      }
    }
    return noOfRecords;
  }
 
  public static int generateManualPRNCSVFile(String sqlQuery, String fileNameWithPath)
  {
    boolean result = true;
    Connection connection = null;
    Statement stmt = null;
    ResultSet resultSet = null;
    boolean isheaderExists = false;
    int noOfRecords = 0;
    logger.info("generateManualCSVFile");
    try
    {
      connection = DBConnectionUtility.getConnection();
     
      PrintWriter csvWriter = null;
     
      stmt = connection.createStatement();
      resultSet = stmt.executeQuery(sqlQuery);
     

      ResultSetMetaData meta = resultSet.getMetaData();
      int numberOfColumns = meta.getColumnCount();
      String dataHeaders = "EXPORT TYPE|SHIPPING BILLNO|SHIPPING BILLDATE|PORT CODE|REFERENCE NO.|REALISED AMOUNT|BILL STATUS|BANK CHARGES|SOFTEX NO.|BILL REF NO|AD CODE|IRM NO|OTHER BANK FIRC REF NO|CURRENCY|REALIZATON DATE|REMITTER NAME|REMITTER COUNTRY";
      while (resultSet.next())
      {
        String row = resultSet.getString(1);
        String portCode = row;
        String no = "";
        String Date = "";
        for (int i = 2; i < numberOfColumns + 1; i++)
        {
          String data = returnSpaceifNull(resultSet.getString(i));
          if (i == 3) {
            no = data;
          } else if (i == 4) {
            Date = data;
          }
          data = data.replaceAll(",", " ");
          if (i <= numberOfColumns) {
            row = row + "|" + data;
          } else {
            row = row + "|";
          }
        }
        if (!isheaderExists)
        {
          csvWriter = new PrintWriter(new File(fileNameWithPath));
         
          csvWriter.println(dataHeaders);
         
          isheaderExists = true;
        }
        csvWriter.println(row);
        noOfRecords++;
      }
      if (csvWriter != null) {
        csvWriter.close();
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      noOfRecords = 0;
      try
      {
        connection.close();
        stmt.close();
        resultSet.close();
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
        connection.close();
        stmt.close();
        resultSet.close();
      }
      catch (Exception e)
      {
        logger.info(e.getMessage());
      }
    }
    return noOfRecords;
  }
 
  public static int generateManualRODCSVFile(String sqlQuery, String fileNameWithPath)
  {
    boolean result = true;
    Connection connection = null;
    Statement stmt = null;
    ResultSet resultSet = null;
    boolean isheaderExists = false;
    int noOfRecords = 0;
    logger.info("generateManualCSVFile");
    try
    {
      connection = DBConnectionUtility.getConnection();
     
      PrintWriter csvWriter = null;
     
      stmt = connection.createStatement();
      resultSet = stmt.executeQuery(sqlQuery);
     

      ResultSetMetaData meta = resultSet.getMetaData();
      int numberOfColumns = meta.getColumnCount();
     
      String dataHeaders = "BANKREFID| ADCODE | GOODSTYPE | PORTCODE | SHIPPINGBILLNO| SHIPPINGBILLDATE| INVOICE_NO| COUNTRYCODE | DIRECTDISPATCHINDICATOR| BILL_ID | BILL_LODGED_DATE| FOREIGNPARTYNAME| FOREIGNPARTYADDRESS | BUYERCOUNTRYCODE | FORMNO |";
      while (resultSet.next())
      {
        String row = resultSet.getString(1);
        String portCode = row;
        String no = "";
        String Date = "";
        for (int i = 2; i < numberOfColumns + 1; i++)
        {
          String data = returnSpaceifNull(resultSet.getString(i));
          if (i == 3) {
            no = data;
          } else if (i == 4) {
            Date = data;
          }
          data = data.replaceAll(",", " ");
          if (i < numberOfColumns) {
            row = row + "|" + data;
          } else {
            row = row + "|";
          }
        }
        if (!isheaderExists)
        {
          csvWriter = new PrintWriter(new File(fileNameWithPath));
         
          csvWriter.println(dataHeaders);
         
          isheaderExists = true;
        }
        csvWriter.println(row);
       
        noOfRecords++;
      }
      if (csvWriter != null) {
        csvWriter.close();
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      result = false;
      try
      {
        connection.close();
        stmt.close();
        resultSet.close();
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
        connection.close();
        stmt.close();
        resultSet.close();
      }
      catch (Exception e)
      {
        logger.info(e.getMessage());
      }
    }
    return noOfRecords;
  }
 
  public static int generateManualIRMCSVFile(String sqlQuery, String fileNameWithPath)
  {
    boolean result = true;
    Connection connection = null;
    Statement stmt = null;
    ResultSet resultSet = null;
    boolean isheaderExists = false;
    int noOfRecords = 0;
    logger.info("generateManualCSVFile");
    try
    {
      connection = DBConnectionUtility.getConnection();
     
      PrintWriter csvWriter = null;
     
      stmt = connection.createStatement();
      resultSet = stmt.executeQuery(sqlQuery);
     

      ResultSetMetaData meta = resultSet.getMetaData();
      int numberOfColumns = meta.getColumnCount();
     





      String dataHeaders = "INWARD REF NO.|REMITTANCE AMOUNT|REMITTANCE DATE|AD CODE|Currency|IE CODE|IE NAME|REMITTER NAME|REMITTER ADD|REMITTER COUNTRY|REMITTER BANK NAME|REMITTER BANK COUNTRY|SWIFT REF NO.|PURPOSE CODE|REMARKS";
      while (resultSet.next())
      {
        String row = resultSet.getString(1);
        String portCode = row;
        String no = "";
        String Date = "";
        for (int i = 2; i < numberOfColumns + 1; i++)
        {
          String data = returnSpaceifNull(resultSet.getString(i));
          if (i == 3) {
            no = data;
          } else if (i == 4) {
            Date = data;
          }
          data = data.replaceAll(",", " ");
          if (i <= numberOfColumns) {
            row = row + "|" + data;
          } else {
            row = row + "|";
          }
        }
        if (!isheaderExists)
        {
          csvWriter = new PrintWriter(new File(fileNameWithPath));
         
          csvWriter.println(dataHeaders);
         
          isheaderExists = true;
        }
        csvWriter.println(row);
        noOfRecords++;
      }
      if (csvWriter != null) {
        csvWriter.close();
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      result = false;
      try
      {
        connection.close();
        stmt.close();
        resultSet.close();
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
        connection.close();
        stmt.close();
        resultSet.close();
      }
      catch (Exception e)
      {
        logger.info(e.getMessage());
      }
    }
    return noOfRecords;
  }
 
  public static int generateManualIRMDGFTCSVFile(String sqlQuery, String fileNameWithPath)
  {
    boolean result = true;
    Connection connection = null;
    Statement stmt = null;
    ResultSet resultSet = null;
    boolean isheaderExists = false;
    int noOfRecords = 0;
    logger.info("generateManualCSVFile");
    try
    {
      connection = DBConnectionUtility.getConnection();
     
      PrintWriter csvWriter = null;
     
      stmt = connection.createStatement();
      resultSet = stmt.executeQuery(sqlQuery);
     

      ResultSetMetaData meta = resultSet.getMetaData();
      int numberOfColumns = meta.getColumnCount();
     





      String dataHeaders = "IRMNUMBER|REMITTANCEFCAMOUNT|REMITTANCEDATE|REMITTANCEADCODE|REMITTANCEFCC|IECCODE|REMITTERNAME|REMITTER_COUNTRY|PURPOSEOFREMITTANCE|IFSCCODE|INRCREDITAMOUNT|PANNUMBER|BANK_REFERENCE_NUMBER|BANK_ACCOUNT_NUMBER|IRMISSUEDATE";
      while (resultSet.next())
      {
        String row = resultSet.getString(1);
        String portCode = row;
        String no = "";
        String Date = "";
        for (int i = 2; i < numberOfColumns + 1; i++)
        {
          String data = returnSpaceifNull(resultSet.getString(i));
          if (i == 3) {
            no = data;
          } else if (i == 4) {
            Date = data;
          }
          data = data.replaceAll(",", " ");
          if (i <= numberOfColumns) {
            row = row + "|" + data;
          } else {
            row = row + "|";
          }
        }
        if (!isheaderExists)
        {
          csvWriter = new PrintWriter(new File(fileNameWithPath));
         


          isheaderExists = true;
        }
        csvWriter.println(row);
        noOfRecords++;
      }
      if (csvWriter != null) {
        csvWriter.close();
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      result = false;
      try
      {
        connection.close();
        stmt.close();
        resultSet.close();
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
        connection.close();
        stmt.close();
        resultSet.close();
      }
      catch (Exception e)
      {
        logger.info(e.getMessage());
      }
    }
    return noOfRecords;
  }
 
  public static int generateManualORMDGFTCSVFile(String sqlQuery, String fileNameWithPath)
  {
    boolean result = true;
    Connection connection = null;
    Statement stmt = null;
    ResultSet resultSet = null;
    boolean isheaderExists = false;
    int noOfRecords = 0;
    logger.info("generateManualCSVFile");
    try
    {
      connection = DBConnectionUtility.getConnection();
     
      PrintWriter csvWriter = null;
     
      stmt = connection.createStatement();
      resultSet = stmt.executeQuery(sqlQuery);
     

      ResultSetMetaData meta = resultSet.getMetaData();
      int numberOfColumns = meta.getColumnCount();
     





      String dataHeaders = "IRMNUMBER|REMITTANCEFCAMOUNT|REMITTANCEDATE|REMITTANCEADCODE|REMITTANCEFCC|IECCODE|REMITTERNAME|REMITTER_COUNTRY|PURPOSEOFREMITTANCE|IFSCCODE|INRCREDITAMOUNT|PANNUMBER|BANK_REFERENCE_NUMBER|BANK_ACCOUNT_NUMBER|IRMISSUEDATE";
      while (resultSet.next())
      {
        String row = resultSet.getString(1);
        String portCode = row;
        String no = "";
        String Date = "";
        for (int i = 2; i < numberOfColumns + 1; i++)
        {
          String data = returnSpaceifNull(resultSet.getString(i));
          if (i == 3) {
            no = data;
          } else if (i == 4) {
            Date = data;
          }
          data = data.replaceAll(",", " ");
          if (i <= numberOfColumns) {
            row = row + "|" + data;
          } else {
            row = row + "|";
          }
        }
        if (!isheaderExists)
        {
          csvWriter = new PrintWriter(new File(fileNameWithPath));
         


          isheaderExists = true;
        }
        csvWriter.println(row);
        noOfRecords++;
      }
      if (csvWriter != null) {
        csvWriter.close();
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      result = false;
      try
      {
        connection.close();
        stmt.close();
        resultSet.close();
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
        connection.close();
        stmt.close();
        resultSet.close();
      }
      catch (Exception e)
      {
        logger.info(e.getMessage());
      }
    }
    return noOfRecords;
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
 
  public static String setValue(String val)
  {
    if (val == null) {
      val = "";
    } else {
      val = val.trim();
    }
    return val;
  }
 
  public void closePreparedStatement(PreparedStatement preparedStatement)
  {
    try
    {
      if (preparedStatement != null) {
        preparedStatement.close();
      }
    }
    catch (Exception excepConnection)
    {
      logger.info(excepConnection.fillInStackTrace());
    }
  }
 
  public void closeResultSet(ResultSet resultSet)
  {
    try
    {
      if (resultSet != null) {
        resultSet.close();
      }
    }
    catch (Exception excepConnection)
    {
      logger.info(excepConnection.fillInStackTrace());
    }
  }
 
  public void closeSqlRefferance(ResultSet result, PreparedStatement preparedStatement)
  {
    closeResultSet(result);
    closePreparedStatement(preparedStatement);
  }
 
  public String insertExcelData()
    throws ApplicationException
  {
    logger.info("Entering Method");
   
    String ext = "";
    try
    {
      this.fileVO = new FileUtitlityVO();
     
      this.fileVO.setInputFile(this.inputFile);
      logger.info("fileNameRef" + this.fileNameRef);
      ext = this.fileNameRef.split("\\.")[1];
      if (ext.equalsIgnoreCase("xls"))
      {
        String noOfRow = "";
        if (this.fileUtilName.equalsIgnoreCase("UCREDIT"))
        {
          noOfRow = processUCREDITXLSData(this.fileVO);
        }
        else if (this.fileUtilName.equalsIgnoreCase("UDEBIT"))
        {
          noOfRow = processUDEBITXLSData(this.fileVO);
        }
        else if (this.fileUtilName.equalsIgnoreCase("CBALANCE"))
        {
          noOfRow = processCBALANCEXLSData(this.fileVO);
        }
        else if (this.fileUtilName.equalsIgnoreCase("FXRATE86"))
        {
          noOfRow = processFXRATE86XLSData(this.fileVO);
        }
        else if (this.fileUtilName.equalsIgnoreCase("SPOTRATE"))
        {
          noOfRow = processSPOTRATEXLSData(this.fileVO);
        }
        else if (this.fileUtilName.equalsIgnoreCase("EBRC"))
        {
          noOfRow = processEBRCXLSData(this.fileVO);
        }
        else
        {
          addActionError("Please Select FileName..");
          return "success";
        }
        logger.info("Count Success :: " + this.fileVO.getFailedCount() + " :: Failed :: " + this.fileVO.getInsertCount());
        if (!noOfRow.equalsIgnoreCase(""))
        {
          if ((this.fileUtilName.equalsIgnoreCase("FXRATE86")) || (this.fileUtilName.equalsIgnoreCase("SPOTRATE"))) {
            addActionError(noOfRow.split("\\-")[0] + "  Records Updated Successfully And " + noOfRow.split("\\-")[1] + " Records Failed to Update");
          } else {
            addActionError(noOfRow.split("\\-")[0] + "  Records Inserted Successfully And " + noOfRow.split("\\-")[1] + " Records Failed to insert");
          }
        }
        else {
          addActionError("File Not Uploaded...");
        }
      }
      else if (ext.equalsIgnoreCase("xlsx"))
      {
        String noOfRow = "";
        if (this.fileUtilName.equalsIgnoreCase("UCREDIT"))
        {
          noOfRow = processUCREDITData(this.fileVO);
        }
        else if (this.fileUtilName.equalsIgnoreCase("UDEBIT"))
        {
          noOfRow = processUDEBITData(this.fileVO);
        }
        else if (this.fileUtilName.equalsIgnoreCase("CBALANCE"))
        {
          noOfRow = processCBALANCEData(this.fileVO);
        }
        else if (this.fileUtilName.equalsIgnoreCase("FXRATE86"))
        {
          noOfRow = processFXRATE86Data(this.fileVO);
        }
        else if (this.fileUtilName.equalsIgnoreCase("SPOTRATE"))
        {
          noOfRow = processSPOTRATEData(this.fileVO);
        }
        else if (this.fileUtilName.equalsIgnoreCase("EBRC"))
        {
          noOfRow = processEBRCData(this.fileVO);
        }
        else
        {
          addActionError("Please Select FileName..");
          return "success";
        }
        logger.info("Count Success :: " + this.fileVO.getFailedCount() + " :: Failed :: " + this.fileVO.getInsertCount());
        if (!noOfRow.equalsIgnoreCase(""))
        {
          if ((this.fileUtilName.equalsIgnoreCase("FXRATE86")) || (this.fileUtilName.equalsIgnoreCase("SPOTRATE"))) {
            addActionError(noOfRow.split("\\-")[0] + "  Records Updated Successfully And " + noOfRow.split("\\-")[1] + " Records Failed to Update");
          } else {
            addActionError(noOfRow.split("\\-")[0] + "  Records Inserted Successfully And " + noOfRow.split("\\-")[1] + " Records Failed to insert");
          }
        }
        else {
          addActionError("File Not Uploaded...");
        }
      }
      else
      {
        addActionError("Please input xls format file to upload");
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
 
  public String processUCREDITData(FileUtitlityVO fileUpldVO)
    throws IOException
  {
    logger.info("Entering Method");
    int i = 1;
    String insertedRowCount = "";
   
    Workbook workbook = null;
    ArrayList<FileUtitlityVO> fileUploadList = null;
    ArrayList<String> excelErrorList = new ArrayList();
    logger.info("1");
    try
    {
      fileUploadList = new ArrayList();
     
      File filename = fileUpldVO.getInputFile();
      if (filename != null)
      {
        FileInputStream uidCreditFile = new FileInputStream(filename);
        logger.info("2");
        workbook = new XSSFWorkbook(uidCreditFile);
        logger.info("3");
        Sheet sheet = workbook.getSheetAt(0);
        logger.info("4");
        String sequenceOfColumns = getSequenceOfColumns();
        String[] columnsArray = sequenceOfColumns.split("\\|");
       
        boolean isSquenceMissed = false;
        Iterator<Row> rows = sheet.rowIterator();
       


        logger.info("5");
        while (rows.hasNext())
        {
          logger.info("row---1" + rows);
          String nostro = "";String ccy = "";String entry_date = "";String sol_id_b = "";String sol_id_c = "";String unique_id = "";
          String amount = "";String value_date = "";String dr_cr = "";String owner_ref_num = "";String inst_ref_num = "";
          String field_86 = "";String run_date = "";
          if (i == 1)
          {
            Row rowObject = (Row)rows.next();
            if (columnsArray.length != rowObject.getLastCellNum())
            {
              String excelError = "Excel Column count mismatched";
              excelErrorList.add(excelError);
              break;
            }
            for (int columnCount = 0; columnCount < columnsArray.length; columnCount++) {
              if (!columnsArray[columnCount].toString().equalsIgnoreCase(rowObject.getCell(columnCount).toString())) {
                isSquenceMissed = true;
              }
            }
            if (isSquenceMissed)
            {
              String excelError = "Excel Column Sequence mismatch";
              excelErrorList.add(excelError);
              break;
            }
          }
          else
          {
            logger.info("inside---" + i);
            fileUpldVO = new FileUtitlityVO();
           
            Row rowObject = (Row)rows.next();
           

            DataFormatter formatter = new DataFormatter();
           
            Cell nostro_FromFile = rowObject.getCell(0);
            Cell ccy_FromFile = rowObject.getCell(1);
            Cell entry_date_FromFile = rowObject.getCell(2);
            Cell sol_id_b_FromFile = rowObject.getCell(3);
            Cell sol_id_c_FromFile = rowObject.getCell(4);
            Cell unique_id_FromFile = rowObject.getCell(5);
            Cell amount_FromFile = rowObject.getCell(6);
            Cell value_date_FromFile = rowObject.getCell(7);
            Cell dr_cr_FromFile = rowObject.getCell(8);
            Cell owner_ref_num_FromFile = rowObject.getCell(9);
            Cell inst_ref_num_FromFile = rowObject.getCell(10);
            Cell field_86_FromFile = rowObject.getCell(11);
            Cell run_date_FromFile = rowObject.getCell(12);
            if ((nostro_FromFile != null) &&
              (nostro_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              nostro = formatter.formatCellValue(nostro_FromFile);
              nostro = nostro.trim();
            }
            if ((ccy_FromFile != null) &&
              (ccy_FromFile.getCellTypeEnum() != CellType.BLANK)) {
              ccy = ccy_FromFile.toString().trim();
            }
            if ((entry_date_FromFile != null) &&
              (entry_date_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              entry_date = entry_date_FromFile.toString().trim();
              entry_date = entry_date.trim();
            }
            if ((sol_id_b_FromFile != null) &&
              (sol_id_b_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              sol_id_b = formatter.formatCellValue(sol_id_b_FromFile);
              sol_id_b = sol_id_b.trim();
            }
            if ((sol_id_c_FromFile != null) &&
              (sol_id_c_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              sol_id_c = formatter.formatCellValue(sol_id_c_FromFile);
              sol_id_c = sol_id_c.trim();
            }
            if ((unique_id_FromFile != null) &&
              (unique_id_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              unique_id = formatter.formatCellValue(unique_id_FromFile);
              unique_id = unique_id.trim();
            }
            if ((amount_FromFile != null) &&
              (amount_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              amount = formatter.formatCellValue(amount_FromFile);
              amount = amount.trim();
            }
            if ((value_date_FromFile != null) &&
              (value_date_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              value_date = value_date_FromFile.toString().trim();
              value_date = value_date.trim();
            }
            if ((dr_cr_FromFile != null) &&
              (dr_cr_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              dr_cr = formatter.formatCellValue(dr_cr_FromFile);
              dr_cr = dr_cr.trim();
            }
            if ((owner_ref_num_FromFile != null) &&
              (owner_ref_num_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              owner_ref_num = owner_ref_num_FromFile.toString().trim();
              owner_ref_num = owner_ref_num.trim();
            }
            if ((inst_ref_num_FromFile != null) &&
              (inst_ref_num_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              inst_ref_num = formatter.formatCellValue(inst_ref_num_FromFile);
              inst_ref_num = inst_ref_num.trim();
            }
            if ((field_86_FromFile != null) &&
              (field_86_FromFile.getCellTypeEnum() != CellType.BLANK)) {
              field_86 = field_86_FromFile.toString().trim();
            }
            if ((run_date_FromFile != null) &&
              (run_date_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              run_date = run_date_FromFile.toString().trim();
              run_date = run_date.trim();
            }
            fileUpldVO.setNostro(nostro);
            fileUpldVO.setCcy(ccy);
            fileUpldVO.setEntry_date(entry_date);
            fileUpldVO.setSol_id_b(sol_id_b);
            fileUpldVO.setSol_id_c(sol_id_c);
            fileUpldVO.setUnique_id(unique_id);
            fileUpldVO.setAmount(amount);
            fileUpldVO.setValue_date(value_date);
            fileUpldVO.setDr_cr(dr_cr);
            fileUpldVO.setOwner_ref_num(owner_ref_num);
            fileUpldVO.setInst_ref_num(inst_ref_num);
            fileUpldVO.setField_86(field_86);
            fileUpldVO.setRun_date(run_date);
           
            fileUploadList.add(fileUpldVO);
          }
          logger.info("outside----" + i);
          i++;
        }
        logger.info("UID_CREDIT_DAILY size---" + fileUploadList.size());
        logger.info("i----" + i);
        if ((fileUploadList != null) &&
          (fileUploadList.size() > 0))
        {
          insertedRowCount = insertUCREDITData(fileUploadList);
          logger.info("---" + insertedRowCount);
        }
      }
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
      logger.info("Exception in insertExcelData1 :: " + exception.getMessage());
    }
    finally
    {
      workbook.close();
    }
    logger.info("Exiting Method");
   
    return insertedRowCount;
  }
 
  public String processUCREDITXLSData(FileUtitlityVO fileUpldVO)
    throws IOException
  {
    logger.info("Entering Method");
    int i = 1;
    String insertedRowCount = "";
    HSSFWorkbook workbook = null;
    ArrayList<FileUtitlityVO> fileUploadList = null;
    ArrayList<String> excelErrorList = new ArrayList();
    try
    {
      fileUploadList = new ArrayList();
     
      File filename = fileUpldVO.getInputFile();
      if (filename != null)
      {
        FileInputStream uidCreditFile = new FileInputStream(filename);
       
        workbook = new HSSFWorkbook(uidCreditFile);
        HSSFSheet sheet = workbook.getSheetAt(0);
        String sequenceOfColumns = getSequenceOfColumns();
        String[] columnsArray = sequenceOfColumns.split("\\|");
       
        boolean isSquenceMissed = false;
        Iterator<Row> rows = sheet.rowIterator();
        while (rows.hasNext())
        {
          logger.info("row---1" + rows);
          String nostro = "";String ccy = "";String entry_date = "";String sol_id_b = "";String sol_id_c = "";String unique_id = "";
          String amount = "";String value_date = "";String dr_cr = "";String owner_ref_num = "";String inst_ref_num = "";
          String field_86 = "";String run_date = "";
          if (i == 1)
          {
            HSSFRow rowObject = (HSSFRow)rows.next();
            if (columnsArray.length != rowObject.getLastCellNum())
            {
              String excelError = "Excel Column count mismatched";
              excelErrorList.add(excelError);
              break;
            }
            for (int columnCount = 0; columnCount < columnsArray.length; columnCount++) {
              if (!columnsArray[columnCount].toString().equalsIgnoreCase(rowObject.getCell(columnCount).toString())) {
                isSquenceMissed = true;
              }
            }
            if (isSquenceMissed)
            {
              String excelError = "Excel Column Sequence mismatch";
              excelErrorList.add(excelError);
              break;
            }
          }
          else
          {
            logger.info("inside---" + i);
            fileUpldVO = new FileUtitlityVO();
           
            HSSFRow rowObject = (HSSFRow)rows.next();
           

            DataFormatter formatter = new DataFormatter();
           
            HSSFCell nostro_FromFile = rowObject.getCell(0);
            HSSFCell ccy_FromFile = rowObject.getCell(1);
            HSSFCell entry_date_FromFile = rowObject.getCell(2);
            HSSFCell sol_id_b_FromFile = rowObject.getCell(3);
            HSSFCell sol_id_c_FromFile = rowObject.getCell(4);
            HSSFCell unique_id_FromFile = rowObject.getCell(5);
            HSSFCell amount_FromFile = rowObject.getCell(6);
            HSSFCell value_date_FromFile = rowObject.getCell(7);
            HSSFCell dr_cr_FromFile = rowObject.getCell(8);
            HSSFCell owner_ref_num_FromFile = rowObject.getCell(9);
            HSSFCell inst_ref_num_FromFile = rowObject.getCell(10);
            HSSFCell field_86_FromFile = rowObject.getCell(11);
            HSSFCell run_date_FromFile = rowObject.getCell(12);
            if ((nostro_FromFile != null) &&
              (ccy_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              nostro = formatter.formatCellValue(nostro_FromFile);
              nostro = nostro.trim();
            }
            if ((ccy_FromFile != null) &&
              (ccy_FromFile.getCellTypeEnum() != CellType.BLANK)) {
              ccy = ccy_FromFile.toString().trim();
            }
            if ((entry_date_FromFile != null) &&
              (entry_date_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              entry_date = entry_date_FromFile.toString().trim();
              entry_date = entry_date.trim();
            }
            if ((sol_id_b_FromFile != null) &&
              (sol_id_b_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              sol_id_b = formatter.formatCellValue(sol_id_b_FromFile);
              sol_id_b = sol_id_b.trim();
            }
            if ((sol_id_c_FromFile != null) &&
              (sol_id_c_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              sol_id_c = formatter.formatCellValue(sol_id_c_FromFile);
              sol_id_c = sol_id_c.trim();
            }
            if ((unique_id_FromFile != null) &&
              (unique_id_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              unique_id = formatter.formatCellValue(unique_id_FromFile);
              unique_id = unique_id.trim();
            }
            if ((amount_FromFile != null) &&
              (amount_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              amount = formatter.formatCellValue(amount_FromFile);
              amount = amount.trim();
            }
            if ((value_date_FromFile != null) &&
              (value_date_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              value_date = value_date_FromFile.toString().trim();
              value_date = value_date.trim();
            }
            if ((dr_cr_FromFile != null) &&
              (dr_cr_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              dr_cr = formatter.formatCellValue(dr_cr_FromFile);
              dr_cr = dr_cr.trim();
            }
            if ((owner_ref_num_FromFile != null) &&
              (owner_ref_num_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              owner_ref_num = owner_ref_num_FromFile.toString().trim();
              owner_ref_num = owner_ref_num.trim();
            }
            if ((inst_ref_num_FromFile != null) &&
              (inst_ref_num_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              inst_ref_num = formatter.formatCellValue(inst_ref_num_FromFile);
              inst_ref_num = inst_ref_num.trim();
            }
            if ((field_86_FromFile != null) &&
              (field_86_FromFile.getCellTypeEnum() != CellType.BLANK)) {
              field_86 = field_86_FromFile.toString().trim();
            }
            if ((run_date_FromFile != null) &&
              (run_date_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              run_date = run_date_FromFile.toString().trim();
              run_date = run_date.trim();
            }
            fileUpldVO.setNostro(nostro);
            fileUpldVO.setCcy(ccy);
            fileUpldVO.setEntry_date(entry_date);
            fileUpldVO.setSol_id_b(sol_id_b);
            fileUpldVO.setSol_id_c(sol_id_c);
            fileUpldVO.setUnique_id(unique_id);
            fileUpldVO.setAmount(amount);
            fileUpldVO.setValue_date(value_date);
            fileUpldVO.setDr_cr(dr_cr);
            fileUpldVO.setOwner_ref_num(owner_ref_num);
            fileUpldVO.setInst_ref_num(inst_ref_num);
            fileUpldVO.setField_86(field_86);
            fileUpldVO.setRun_date(run_date);
           
            fileUploadList.add(fileUpldVO);
          }
          logger.info("outside----" + i);
          i++;
        }
        logger.info("UID_CREDIT_DAILY size---" + fileUploadList.size());
        logger.info("i----" + i);
        if ((fileUploadList != null) &&
          (fileUploadList.size() > 0))
        {
          insertedRowCount = insertUCREDITData(fileUploadList);
         
          logger.info("---" + insertedRowCount);
        }
      }
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
      logger.info("Exception in insertExcelData1 :: " + exception.getMessage());
    }
    logger.info("Exiting Method");
   
    return insertedRowCount;
  }
 
  public String insertUCREDITData(ArrayList<FileUtitlityVO> fileUploadList)
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement ppt = null;
    LoggableStatement ppt1 = null;
    LoggableStatement ppt2 = null;
    LoggableStatement ppt3 = null;
    CallableStatement callableStatement = null;
    FileUtitlityVO excelDataVO = null;
    int insertCount = 0;
    int failedCount = 0;
    try
    {
      con = DBConnectionUtility.getConnection();
      HttpSession session = ServletActionContext.getRequest().getSession();
      String userId = (String)session.getAttribute("loginedUserId");
      for (int i = 0; i < fileUploadList.size(); i++)
      {
        String uidDataInsertProcCall = "{call ETT_UID_CREDIT_TBL_INSERT(?,?,?,?,?,?,?,?,?,?,?,?,?)}";
       



        excelDataVO = new FileUtitlityVO();
        excelDataVO = (FileUtitlityVO)fileUploadList.get(i);
        logger.info("excelDataVO " + excelDataVO.toString());
        try
        {
          callableStatement = con.prepareCall(uidDataInsertProcCall);
          callableStatement.setString(1, excelDataVO.getNostro());
          callableStatement.setString(2, excelDataVO.getCcy());
          callableStatement.setString(3, excelDataVO.getEntry_date());
          callableStatement.setString(4, excelDataVO.getSol_id_c());
          callableStatement.setString(5, excelDataVO.getSol_id_b());
          callableStatement.setString(6, excelDataVO.getUnique_id());
          callableStatement.setString(7, excelDataVO.getAmount());
          callableStatement.setString(8, excelDataVO.getValue_date());
          callableStatement.setString(9, excelDataVO.getDr_cr());
          callableStatement.setString(10, excelDataVO.getOwner_ref_num());
          callableStatement.setString(11, excelDataVO.getInst_ref_num());
          callableStatement.setString(12, excelDataVO.getField_86());
          callableStatement.setString(13, excelDataVO.getRun_date());
         
          callableStatement.executeUpdate();
         
          insertCount++;
         






































          String insertException = "SUCCESS";
         
          ppt2 = new LoggableStatement(con, "INSERT INTO MNL_FLE_UPLD_ERR_SUCCESS_DTA(UNIQUE_ID,EXCEPTION_MESSAGE,TABLE_NAME,UPLOAD_DATE,USER_ID) VALUES(?,?,?,SYSTIMESTAMP,?)");
         
          ppt2.setString(1, excelDataVO.getUnique_id());
          ppt2.setString(2, insertException);
          ppt2.setString(3, "UID_CREDIT_DAILY_TBL");
          ppt2.setString(4, userId);
          ppt2.executeUpdate();
        }
        catch (SQLException se)
        {
          failedCount++;
          String insertException = se.getMessage().trim();
         
          ppt1 = new LoggableStatement(con, "INSERT INTO MNL_FLE_UPLD_ERR_SUCCESS_DTA(UNIQUE_ID,EXCEPTION_MESSAGE,TABLE_NAME,UPLOAD_DATE,USER_ID) VALUES(?,?,?,SYSTIMESTAMP,?)");
         
          ppt1.setString(1, excelDataVO.getUnique_id());
          ppt1.setString(2, insertException);
          ppt1.setString(3, "UID_CREDIT_DAILY_TBL");
          ppt1.setString(4, userId);
          ppt1.executeUpdate();
        }
        finally
        {
          if (callableStatement != null) {
            callableStatement.close();
          }
          closeSqlRefferance(null, ppt1);
          closeSqlRefferance(null, ppt2);
        }
      }
      excelDataVO.setFailedCount(failedCount);
      excelDataVO.setInsertCount(insertCount);
      logger.info("Inserted Record Successfully :: " + excelDataVO.getFailedCount() + " :: " + excelDataVO.getInsertCount());
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
      logger.info("Exception in insertExcelData2 :: " + exception.getMessage());
      try
      {
        con.close();
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
    finally
    {
      try
      {
        con.close();
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
    logger.info("Exiting Method");
   
    return insertCount + "-" + failedCount;
  }
 
  public String processUDEBITData(FileUtitlityVO fileUpldVO)
    throws IOException
  {
    logger.info("Entering Method");
    int i = 1;
    String insertedRowCount = "";
    XSSFWorkbook workbook = null;
    ArrayList<FileUtitlityVO> fileUploadList = null;
    ArrayList<String> excelErrorList = new ArrayList();
    try
    {
      fileUploadList = new ArrayList();
     
      File filename = fileUpldVO.getInputFile();
      if (filename != null)
      {
        FileInputStream uidCreditFile = new FileInputStream(filename);
       
        workbook = new XSSFWorkbook(uidCreditFile);
        Sheet sheet = workbook.getSheetAt(0);
        String sequenceOfColumns = getSequenceOfColumns1();
        String[] columnsArray = sequenceOfColumns.split("\\|");
       
        boolean isSquenceMissed = false;
        Iterator<Row> rows = sheet.rowIterator();
        while (rows.hasNext())
        {
          logger.info("row---1" + rows);
          String account_name = "";String sol_id = "";String ccy = "";String entry_date = "";String owner_ref = "";String uid_no = "";
          String select_t_f = "";String absolute_amt = "";String outstanding_amt = "";String dr_cr = "";String value_date = "";
          String inst_ref_num = "";String supplementary_dtls = "";String nostro_id = "";String date_of_recovery = "";
          String tran_id = "";String status = "";String addl_detail1 = "";String addl_detail2 = "";String addl_detail3 = "";String run_date = "";
          if (i == 1)
          {
            Row rowObject = (Row)rows.next();
            if (columnsArray.length != rowObject.getLastCellNum())
            {
              String excelError = "Excel Column count mismatched";
              excelErrorList.add(excelError);
              break;
            }
            for (int columnCount = 0; columnCount < columnsArray.length; columnCount++) {
              if (!columnsArray[columnCount].toString().equalsIgnoreCase(rowObject.getCell(columnCount).toString())) {
                isSquenceMissed = true;
              }
            }
            if (isSquenceMissed)
            {
              String excelError = "Excel Column Sequence mismatch";
              excelErrorList.add(excelError);
              break;
            }
          }
          else
          {
            logger.info("inside---" + i);
            fileUpldVO = new FileUtitlityVO();
            Row rowObject = (Row)rows.next();
            DataFormatter formatter = new DataFormatter();
            Cell account_name_FromFile = rowObject.getCell(0);
            Cell sol_id_FromFile = rowObject.getCell(1);
            Cell ccy_FromFile = rowObject.getCell(2);
            Cell entry_date_FromFile = rowObject.getCell(3);
            Cell owner_ref_FromFile = rowObject.getCell(4);
            Cell uid_no_FromFile = rowObject.getCell(5);
            Cell select_t_f_FromFile = rowObject.getCell(6);
            Cell absolute_amt_FromFile = rowObject.getCell(7);
            Cell outstanding_amt_FromFile = rowObject.getCell(8);
            Cell dr_cr_FromFile = rowObject.getCell(9);
            Cell value_date_FromFile = rowObject.getCell(10);
            Cell inst_ref_num_FromFile = rowObject.getCell(11);
            Cell supplementary_dtls_FromFile = rowObject.getCell(12);
            Cell nostro_id_FromFile = rowObject.getCell(13);
            Cell date_of_recovery_FromFile = rowObject.getCell(14);
            Cell tran_id_FromFile = rowObject.getCell(15);
            Cell status_FromFile = rowObject.getCell(16);
            Cell addl_detail1_FromFile = rowObject.getCell(17);
            Cell addl_detail2_FromFile = rowObject.getCell(18);
            Cell addl_detail3_FromFile = rowObject.getCell(19);
            Cell run_date_FromFile = rowObject.getCell(20);
            if ((account_name_FromFile != null) &&
              (account_name_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              account_name = formatter.formatCellValue(account_name_FromFile);
              account_name = account_name.trim();
            }
            if ((sol_id_FromFile != null) &&
              (sol_id_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              sol_id = formatter.formatCellValue(sol_id_FromFile);
              sol_id = sol_id_FromFile.toString().trim();
            }
            if ((ccy_FromFile != null) &&
              (ccy_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              ccy = ccy_FromFile.toString().trim();
              ccy = ccy.trim();
            }
            if ((entry_date_FromFile != null) &&
              (entry_date_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              entry_date = formatter.formatCellValue(entry_date_FromFile);
              entry_date = formatDate(entry_date);
              entry_date = entry_date.trim();
            }
            if ((owner_ref_FromFile != null) &&
              (owner_ref_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              owner_ref = formatter.formatCellValue(owner_ref_FromFile);
              owner_ref = owner_ref.trim();
            }
            if ((uid_no_FromFile != null) &&
              (uid_no_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              uid_no = formatter.formatCellValue(uid_no_FromFile);
              uid_no = uid_no.trim();
            }
            if ((select_t_f_FromFile != null) &&
              (select_t_f_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              select_t_f = formatter.formatCellValue(select_t_f_FromFile);
              select_t_f = select_t_f.trim();
            }
            if ((absolute_amt_FromFile != null) &&
              (absolute_amt_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              absolute_amt = absolute_amt_FromFile.toString().trim();
              absolute_amt = absolute_amt.trim();
            }
            if ((outstanding_amt_FromFile != null) &&
              (outstanding_amt_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              outstanding_amt = formatter.formatCellValue(outstanding_amt_FromFile);
              outstanding_amt = outstanding_amt.trim();
            }
            if ((dr_cr_FromFile != null) &&
              (dr_cr_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              dr_cr = dr_cr_FromFile.toString().trim();
              dr_cr = dr_cr.trim();
            }
            if ((value_date_FromFile != null) &&
              (value_date_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              value_date = formatter.formatCellValue(value_date_FromFile);
              value_date = formatDate(value_date);
              value_date = value_date.trim();
            }
            if ((inst_ref_num_FromFile != null) &&
              (inst_ref_num_FromFile.getCellTypeEnum() != CellType.BLANK)) {
              inst_ref_num = inst_ref_num_FromFile.toString().trim();
            }
            if ((supplementary_dtls_FromFile != null) &&
              (supplementary_dtls_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              supplementary_dtls = supplementary_dtls_FromFile.toString().trim();
              supplementary_dtls = supplementary_dtls.trim();
            }
            if ((nostro_id_FromFile != null) &&
              (nostro_id_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              nostro_id = formatter.formatCellValue(nostro_id_FromFile);
              nostro_id = nostro_id.trim();
            }
            if ((date_of_recovery_FromFile != null) &&
              (date_of_recovery_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              date_of_recovery = formatter.formatCellValue(date_of_recovery_FromFile);
              date_of_recovery = formatDate(date_of_recovery);
              date_of_recovery = date_of_recovery.trim();
            }
            if ((tran_id_FromFile != null) &&
              (tran_id_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              tran_id = formatter.formatCellValue(tran_id_FromFile);
              tran_id = tran_id.trim();
            }
            if ((status_FromFile != null) &&
              (status_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              status = status_FromFile.toString().trim();
              status = status.trim();
            }
            if ((addl_detail1_FromFile != null) &&
              (addl_detail1_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              addl_detail1 = formatter.formatCellValue(addl_detail1_FromFile);
              addl_detail1 = addl_detail1.trim();
            }
            if ((addl_detail2_FromFile != null) &&
              (addl_detail2_FromFile.getCellTypeEnum() != CellType.BLANK)) {
              addl_detail2 = addl_detail2_FromFile.toString().trim();
            }
            if ((addl_detail3_FromFile != null) &&
              (addl_detail3_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              addl_detail3 = formatter.formatCellValue(addl_detail3_FromFile);
              addl_detail3 = formatDate(addl_detail3);
              addl_detail3 = addl_detail3.trim();
            }
            if ((run_date_FromFile != null) &&
              (run_date_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              run_date = formatter.formatCellValue(run_date_FromFile);
              run_date = formatDate(run_date);
              run_date = run_date.trim();
            }
            fileUpldVO.setAccount_name(account_name);
            fileUpldVO.setSol_id(sol_id);
            fileUpldVO.setCcy(ccy);
            fileUpldVO.setEntry_date(entry_date);
            fileUpldVO.setOwner_ref(owner_ref);
            fileUpldVO.setUid_no(uid_no);
            fileUpldVO.setSelect_t_f(select_t_f);
            fileUpldVO.setAbsolute_amt(absolute_amt);
            fileUpldVO.setOutstanding_amt(outstanding_amt);
            fileUpldVO.setDr_cr(dr_cr);
            fileUpldVO.setValue_date(value_date);
            fileUpldVO.setInst_ref_num(inst_ref_num);
            fileUpldVO.setSupplementary_dtls(supplementary_dtls);
            fileUpldVO.setNostro_id(nostro_id);
            fileUpldVO.setDate_of_recovery(date_of_recovery);
            fileUpldVO.setTran_id(tran_id);
            fileUpldVO.setStatus(status);
            fileUpldVO.setAddl_detail1(addl_detail1);
            fileUpldVO.setAddl_detail2(addl_detail2);
            fileUpldVO.setAddl_detail3(addl_detail3);
            fileUpldVO.setRun_date(run_date);
           
            fileUploadList.add(fileUpldVO);
          }
          logger.info("outside----" + i);
          i++;
        }
        logger.info("List size---" + fileUploadList.size());
        logger.info("i----" + i);
        if ((fileUploadList != null) &&
          (fileUploadList.size() > 0))
        {
          insertedRowCount = insertUDEBITData(fileUploadList);
          logger.info("---" + insertedRowCount);
        }
      }
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
      logger.info("Exception in insertExcelData1 :: " + exception.getMessage());
    }
    logger.info("Exiting Method");
   
    return insertedRowCount;
  }
 
  public String processUDEBITXLSData(FileUtitlityVO fileUpldVO)
    throws IOException
  {
    logger.info("Entering Method");
    int i = 1;
    String insertedRowCount = "";
    HSSFWorkbook workbook = null;
    ArrayList<FileUtitlityVO> fileUploadList = null;
    ArrayList<String> excelErrorList = new ArrayList();
    try
    {
      fileUploadList = new ArrayList();
     
      File filename = fileUpldVO.getInputFile();
      if (filename != null)
      {
        FileInputStream uidCreditFile = new FileInputStream(filename);
       
        workbook = new HSSFWorkbook(uidCreditFile);
        HSSFSheet sheet = workbook.getSheetAt(0);
        String sequenceOfColumns = getSequenceOfColumns1();
        String[] columnsArray = sequenceOfColumns.split("\\|");
       
        boolean isSquenceMissed = false;
        Iterator<Row> rows = sheet.rowIterator();
        while (rows.hasNext())
        {
          logger.info("row---1" + rows);
          String account_name = "";String sol_id = "";String ccy = "";String entry_date = "";String owner_ref = "";String uid_no = "";
          String select_t_f = "";String absolute_amt = "";String outstanding_amt = "";String dr_cr = "";String value_date = "";
          String inst_ref_num = "";String supplementary_dtls = "";String nostro_id = "";String date_of_recovery = "";
          String tran_id = "";String status = "";String addl_detail1 = "";String addl_detail2 = "";String addl_detail3 = "";String run_date = "";
          if (i == 1)
          {
            HSSFRow rowObject = (HSSFRow)rows.next();
            if (columnsArray.length != rowObject.getLastCellNum())
            {
              String excelError = "Excel Column count mismatched";
              excelErrorList.add(excelError);
              break;
            }
            for (int columnCount = 0; columnCount < columnsArray.length; columnCount++) {
              if (!columnsArray[columnCount].toString().equalsIgnoreCase(rowObject.getCell(columnCount).toString())) {
                isSquenceMissed = true;
              }
            }
            if (isSquenceMissed)
            {
              String excelError = "Excel Column Sequence mismatch";
              excelErrorList.add(excelError);
              break;
            }
          }
          else
          {
            logger.info("inside---" + i);
            fileUpldVO = new FileUtitlityVO();
            HSSFRow rowObject = (HSSFRow)rows.next();
            DataFormatter formatter = new DataFormatter();
            HSSFCell account_name_FromFile = rowObject.getCell(0);
            HSSFCell sol_id_FromFile = rowObject.getCell(1);
            HSSFCell ccy_FromFile = rowObject.getCell(2);
            HSSFCell entry_date_FromFile = rowObject.getCell(3);
            HSSFCell owner_ref_FromFile = rowObject.getCell(4);
            HSSFCell uid_no_FromFile = rowObject.getCell(5);
            HSSFCell select_t_f_FromFile = rowObject.getCell(6);
            HSSFCell absolute_amt_FromFile = rowObject.getCell(7);
            HSSFCell outstanding_amt_FromFile = rowObject.getCell(8);
            HSSFCell dr_cr_FromFile = rowObject.getCell(9);
            HSSFCell value_date_FromFile = rowObject.getCell(10);
            HSSFCell inst_ref_num_FromFile = rowObject.getCell(11);
            HSSFCell supplementary_dtls_FromFile = rowObject.getCell(12);
            HSSFCell nostro_id_FromFile = rowObject.getCell(13);
            HSSFCell date_of_recovery_FromFile = rowObject.getCell(14);
            HSSFCell tran_id_FromFile = rowObject.getCell(15);
            HSSFCell status_FromFile = rowObject.getCell(16);
            HSSFCell addl_detail1_FromFile = rowObject.getCell(17);
            HSSFCell addl_detail2_FromFile = rowObject.getCell(18);
            HSSFCell addl_detail3_FromFile = rowObject.getCell(19);
            HSSFCell run_date_FromFile = rowObject.getCell(20);
            if ((account_name_FromFile != null) &&
              (account_name_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              account_name = formatter.formatCellValue(account_name_FromFile);
              account_name = account_name.trim();
            }
            if ((sol_id_FromFile != null) &&
              (sol_id_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              sol_id = formatter.formatCellValue(sol_id_FromFile);
              sol_id = sol_id.toString().trim();
            }
            if ((ccy_FromFile != null) &&
              (ccy_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              ccy = ccy_FromFile.toString().trim();
              ccy = ccy.trim();
            }
            if ((entry_date_FromFile != null) &&
              (entry_date_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              entry_date = formatter.formatCellValue(entry_date_FromFile);
              entry_date = formatDate(entry_date);
              entry_date = entry_date.trim();
            }
            if ((owner_ref_FromFile != null) &&
              (owner_ref_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              owner_ref = formatter.formatCellValue(owner_ref_FromFile);
              owner_ref = owner_ref.trim();
            }
            if ((uid_no_FromFile != null) &&
              (uid_no_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              uid_no = formatter.formatCellValue(uid_no_FromFile);
              uid_no = uid_no.trim();
            }
            if ((select_t_f_FromFile != null) &&
              (select_t_f_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              select_t_f = formatter.formatCellValue(select_t_f_FromFile);
              select_t_f = select_t_f.trim();
            }
            if ((absolute_amt_FromFile != null) &&
              (absolute_amt_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              absolute_amt = absolute_amt_FromFile.toString().trim();
              absolute_amt = absolute_amt.trim();
            }
            if ((outstanding_amt_FromFile != null) &&
              (outstanding_amt_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              outstanding_amt = formatter.formatCellValue(outstanding_amt_FromFile);
              outstanding_amt = outstanding_amt.trim();
            }
            if ((dr_cr_FromFile != null) &&
              (dr_cr_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              dr_cr = dr_cr_FromFile.toString().trim();
              dr_cr = dr_cr.trim();
            }
            if ((value_date_FromFile != null) &&
              (value_date_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              value_date = formatter.formatCellValue(value_date_FromFile);
              value_date = formatDate(value_date);
              value_date = value_date.trim();
            }
            if ((inst_ref_num_FromFile != null) &&
              (inst_ref_num_FromFile.getCellTypeEnum() != CellType.BLANK)) {
              inst_ref_num = inst_ref_num_FromFile.toString().trim();
            }
            if ((supplementary_dtls_FromFile != null) &&
              (supplementary_dtls_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              supplementary_dtls = supplementary_dtls_FromFile.toString().trim();
              supplementary_dtls = supplementary_dtls.trim();
            }
            if ((nostro_id_FromFile != null) &&
              (nostro_id_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              nostro_id = formatter.formatCellValue(nostro_id_FromFile);
              nostro_id = nostro_id.trim();
            }
            if ((date_of_recovery_FromFile != null) &&
              (date_of_recovery_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              date_of_recovery = formatter.formatCellValue(date_of_recovery_FromFile);
              date_of_recovery = formatDate(date_of_recovery);
              date_of_recovery = date_of_recovery.trim();
            }
            if ((tran_id_FromFile != null) &&
              (tran_id_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              tran_id = formatter.formatCellValue(tran_id_FromFile);
              tran_id = tran_id.trim();
            }
            if ((status_FromFile != null) &&
              (status_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              status = status_FromFile.toString().trim();
              status = status.trim();
            }
            if ((addl_detail1_FromFile != null) &&
              (addl_detail1_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              addl_detail1 = formatter.formatCellValue(addl_detail1_FromFile);
              addl_detail1 = addl_detail1.trim();
            }
            if ((addl_detail2_FromFile != null) &&
              (addl_detail2_FromFile.getCellTypeEnum() != CellType.BLANK)) {
              addl_detail2 = addl_detail2_FromFile.toString().trim();
            }
            if ((addl_detail3_FromFile != null) &&
              (addl_detail3_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              addl_detail3 = formatter.formatCellValue(addl_detail3_FromFile);
              addl_detail3 = formatDate(addl_detail3);
              addl_detail3 = addl_detail3.trim();
            }
            if ((run_date_FromFile != null) &&
              (run_date_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              run_date = formatter.formatCellValue(run_date_FromFile);
             
              run_date = formatDate(run_date);
              run_date = run_date.trim();
            }
            fileUpldVO.setAccount_name(account_name);
            fileUpldVO.setSol_id(sol_id);
            fileUpldVO.setCcy(ccy);
            fileUpldVO.setEntry_date(entry_date);
            fileUpldVO.setOwner_ref(owner_ref);
            fileUpldVO.setUid_no(uid_no);
            fileUpldVO.setSelect_t_f(select_t_f);
            fileUpldVO.setAbsolute_amt(absolute_amt);
            fileUpldVO.setOutstanding_amt(outstanding_amt);
            fileUpldVO.setDr_cr(dr_cr);
            fileUpldVO.setValue_date(value_date);
            fileUpldVO.setInst_ref_num(inst_ref_num);
            fileUpldVO.setSupplementary_dtls(supplementary_dtls);
            fileUpldVO.setNostro_id(nostro_id);
            fileUpldVO.setDate_of_recovery(date_of_recovery);
            fileUpldVO.setTran_id(tran_id);
            fileUpldVO.setStatus(status);
            fileUpldVO.setAddl_detail1(addl_detail1);
            fileUpldVO.setAddl_detail2(addl_detail2);
            fileUpldVO.setAddl_detail3(addl_detail3);
            fileUpldVO.setRun_date(run_date);
           
            fileUploadList.add(fileUpldVO);
          }
          logger.info("outside----" + i);
          i++;
        }
        logger.info("List size---" + fileUploadList.size());
        logger.info("i----" + i);
        if ((fileUploadList != null) &&
          (fileUploadList.size() > 0))
        {
          insertedRowCount = insertUDEBITData(fileUploadList);
         
          logger.info("---" + insertedRowCount);
        }
      }
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
      logger.info("Exception in insertExcelData1 :: " + exception.getMessage());
    }
    logger.info("Exiting Method");
   
    return insertedRowCount;
  }
 
  public String insertUDEBITData(ArrayList<FileUtitlityVO> fileUploadList)
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement ppt = null;
    LoggableStatement ppt1 = null;
    LoggableStatement ppt2 = null;
   
    FileUtitlityVO excelDataVO = null;
    int insertCount = 0;
    int failedCount = 0;
    try
    {
      con = DBConnectionUtility.getConnection();
      HttpSession session = ServletActionContext.getRequest().getSession();
      String userId = (String)session.getAttribute("loginedUserId");
      for (int i = 0; i < fileUploadList.size(); i++)
      {
        ppt = new LoggableStatement(con, "INSERT INTO UID_DEBIT_DAILY_TBL(ACCOUNT_NAME,SOL_ID,CCY,ENTRY_DATE,OWNER_REF,UID_NO,SELECT_T_F,ABSOLUTE_AMT,OUTSTANDING_AMT,DR_CR,VALUE_DATE,INST_REF_NUM,SUPPLEMENTARY_DTLS,NOSTRO_ID,DATE_OF_RECOVERY,TRAN_ID,STATUS,ADDL_DETAIL1,ADDL_DETAIL2,ADDL_DETAIL3,RUN_DATE) VALUES(?,?,?,TO_DATE(?,'DD-MM-YYYY'),?,?,?,?,?,?,TO_DATE(?,'DD-MM-YYYY'),?,?,?,TO_DATE(?,'DD-MM-YYYY'),?,?,?,?,?,?)");
        excelDataVO = new FileUtitlityVO();
        excelDataVO = (FileUtitlityVO)fileUploadList.get(i);
        try
        {
          ppt.setString(1, excelDataVO.getAccount_name());
          ppt.setString(2, excelDataVO.getSol_id());
          ppt.setString(3, excelDataVO.getCcy());
          ppt.setString(4, excelDataVO.getEntry_date());
          ppt.setString(5, excelDataVO.getOwner_ref());
          ppt.setString(6, excelDataVO.getUid_no());
          ppt.setString(7, excelDataVO.getSelect_t_f());
          ppt.setString(8, excelDataVO.getAbsolute_amt());
          ppt.setString(9, excelDataVO.getOutstanding_amt());
          ppt.setString(10, excelDataVO.getDr_cr());
          ppt.setString(11, excelDataVO.getValue_date());
          ppt.setString(12, excelDataVO.getInst_ref_num());
          ppt.setString(13, excelDataVO.getSupplementary_dtls());
          ppt.setString(14, excelDataVO.getNostro_id());
          ppt.setString(15, excelDataVO.getDate_of_recovery());
          ppt.setString(16, excelDataVO.getTran_id());
          ppt.setString(17, excelDataVO.getStatus());
          ppt.setString(18, excelDataVO.getAddl_detail1());
          ppt.setString(19, excelDataVO.getAddl_detail2());
          ppt.setString(20, excelDataVO.getAddl_detail3());
          ppt.setString(21, excelDataVO.getRun_date());
          logger.info("Before Insert" + ppt.getQueryString());
          ppt.executeUpdate();
          logger.info("After Insert");
          insertCount++;
         
          String insertException = "SUCCESS";
         
          ppt2 = new LoggableStatement(con, "INSERT INTO MNL_FLE_UPLD_ERR_SUCCESS_DTA(UNIQUE_ID,EXCEPTION_MESSAGE,TABLE_NAME,UPLOAD_DATE,USER_ID) VALUES(?,?,?,SYSTIMESTAMP,?)");
         
          ppt2.setString(1, excelDataVO.getUid_no());
          ppt2.setString(2, insertException);
          ppt2.setString(3, "UID_DEBIT_DAILY_TBL");
          ppt2.setString(4, userId);
          ppt2.executeUpdate();
        }
        catch (SQLException se)
        {
          failedCount++;
          String insertException = se.getMessage().trim();
         
          ppt1 = new LoggableStatement(con, "INSERT INTO MNL_FLE_UPLD_ERR_SUCCESS_DTA(UNIQUE_ID,EXCEPTION_MESSAGE,TABLE_NAME,UPLOAD_DATE,USER_ID) VALUES(?,?,?,SYSTIMESTAMP,?)");
         
          ppt1.setString(1, excelDataVO.getUid_no());
          ppt1.setString(2, insertException);
          ppt1.setString(3, "UID_DEBIT_DAILY_TBL");
          ppt1.setString(4, userId);
          ppt1.executeUpdate();
        }
        finally
        {
          closeSqlRefferance(null, ppt);
          closeSqlRefferance(null, ppt1);
          closeSqlRefferance(null, ppt2);
        }
      }
      excelDataVO.setFailedCount(failedCount);
      excelDataVO.setInsertCount(insertCount);
      logger.info("Inserted Record Successfully :: " + excelDataVO.getFailedCount() + " :: " + excelDataVO.getInsertCount());
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
      logger.info("Exception in insertExcelData2 :: " + exception.getMessage());
     
      closeSqlRefferance(null, ppt);
      closeSqlRefferance(null, ppt1);
      try
      {
        con.close();
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
    finally
    {
      closeSqlRefferance(null, ppt);
      closeSqlRefferance(null, ppt1);
      try
      {
        con.close();
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
    logger.info("Exiting Method");
   
    return insertCount + "-" + failedCount;
  }
 
  public String processCBALANCEData(FileUtitlityVO fileUpldVO)
    throws IOException
  {
    logger.info("Entering Method");
    int i = 1;
    String insertedRowCount = "";
    XSSFWorkbook workbook = null;
    ArrayList<FileUtitlityVO> fileUploadList = null;
    ArrayList<String> excelErrorList = new ArrayList();
    try
    {
      fileUploadList = new ArrayList();
     
      File filename = fileUpldVO.getInputFile();
      if (filename != null)
      {
        FileInputStream uidCreditFile = new FileInputStream(filename);
       
        workbook = new XSSFWorkbook(uidCreditFile);
        Sheet sheet = workbook.getSheetAt(0);
        String sequenceOfColumns = getSequenceOfColumns2();
        String[] columnsArray = sequenceOfColumns.split("\\|");
       
        boolean isSquenceMissed = false;
        Iterator<Row> rows = sheet.rowIterator();
       

        boolean flg = true;
        while ((rows.hasNext()) && (flg))
        {
          String run_date = "";String account_number = "";String closing_bal = "";
          if (i == 1)
          {
            Row rowObject = (Row)rows.next();
            if (columnsArray.length != rowObject.getLastCellNum())
            {
              String excelError = "Excel Column count mismatched";
              excelErrorList.add(excelError);
              break;
            }
            for (int columnCount = 0; columnCount < columnsArray.length; columnCount++) {
              if (!columnsArray[columnCount].toString().equalsIgnoreCase(rowObject.getCell(columnCount).toString())) {
                isSquenceMissed = true;
              }
            }
            if (isSquenceMissed)
            {
              String excelError = "Excel Column Sequence mismatch";
              excelErrorList.add(excelError);
              break;
            }
          }
          else
          {
            fileUpldVO = new FileUtitlityVO();
            Row rowObject = (Row)rows.next();
            Cell run_date_FromFile_to_check = rowObject.getCell(0);
            if ((rowObject == null) || (isCellEmpty(run_date_FromFile_to_check)))
            {
              flg = false;
            }
            else
            {
              DataFormatter formatter = new DataFormatter();
             
              Cell run_date_FromFile = rowObject.getCell(0);
              Cell account_number_FromFile = rowObject.getCell(1);
              Cell closing_bal_FromFile = rowObject.getCell(2);
              if ((run_date_FromFile != null) &&
                (run_date_FromFile.getCellTypeEnum() != CellType.BLANK))
              {
                run_date = formatter.formatCellValue(run_date_FromFile);
               
                run_date = formatDate(run_date);
                run_date = run_date.trim();
              }
              if ((account_number_FromFile != null) &&
                (account_number_FromFile.getCellTypeEnum() != CellType.BLANK)) {
                account_number = account_number_FromFile.toString().trim();
              }
              if ((closing_bal_FromFile != null) &&
                (closing_bal_FromFile.getCellTypeEnum() != CellType.BLANK))
              {
                closing_bal = formatter.formatCellValue(closing_bal_FromFile);
                closing_bal = closing_bal.trim();
              }
              fileUpldVO.setRun_date(run_date);
              fileUpldVO.setAccount_number(account_number);
              fileUpldVO.setClosing_bal(closing_bal);
             

              fileUploadList.add(fileUpldVO);
            }
          }
          i++;
        }
        logger.info("i----" + i);
        if ((fileUploadList != null) &&
          (fileUploadList.size() > 0)) {
          insertedRowCount = insertCBALANCEData(fileUploadList);
        }
      }
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
      logger.info("Exception in insertExcelData1 :: " + exception.getMessage());
    }
    logger.info("Exiting Method");
   
    return insertedRowCount;
  }
 
  public String processCBALANCEXLSData(FileUtitlityVO fileUpldVO)
    throws IOException
  {
    logger.info("Entering Method");
    int i = 1;
    String insertedRowCount = "";
    HSSFWorkbook workbook = null;
    ArrayList<FileUtitlityVO> fileUploadList = null;
    ArrayList<String> excelErrorList = new ArrayList();
    try
    {
      fileUploadList = new ArrayList();
     
      File filename = fileUpldVO.getInputFile();
      if (filename != null)
      {
        FileInputStream uidCreditFile = new FileInputStream(filename);
       
        workbook = new HSSFWorkbook(uidCreditFile);
        HSSFSheet sheet = workbook.getSheetAt(0);
        String sequenceOfColumns = getSequenceOfColumns2();
        String[] columnsArray = sequenceOfColumns.split("\\|");
       
        boolean isSquenceMissed = false;
        Iterator<Row> rows = sheet.rowIterator();
       

        boolean flg = true;
        while ((rows.hasNext()) && (flg))
        {
          String run_date = "";String account_number = "";String closing_bal = "";
          if (i == 1)
          {
            HSSFRow rowObject = (HSSFRow)rows.next();
            if (columnsArray.length != rowObject.getLastCellNum())
            {
              String excelError = "Excel Column count mismatched";
              excelErrorList.add(excelError);
              break;
            }
            for (int columnCount = 0; columnCount < columnsArray.length; columnCount++) {
              if (!columnsArray[columnCount].toString().equalsIgnoreCase(rowObject.getCell(columnCount).toString())) {
                isSquenceMissed = true;
              }
            }
            if (isSquenceMissed)
            {
              String excelError = "Excel Column Sequence mismatch";
              excelErrorList.add(excelError);
              break;
            }
          }
          else
          {
            fileUpldVO = new FileUtitlityVO();
            HSSFRow rowObject = (HSSFRow)rows.next();
            HSSFCell run_date_FromFile_to_check = rowObject.getCell(0);
            if ((rowObject == null) || (isCellEmpty(run_date_FromFile_to_check)))
            {
              flg = false;
            }
            else
            {
              DataFormatter formatter = new DataFormatter();
              HSSFCell run_date_FromFile = rowObject.getCell(0);
              HSSFCell account_number_FromFile = rowObject.getCell(1);
              HSSFCell closing_bal_FromFile = rowObject.getCell(2);
              if ((run_date_FromFile != null) &&
                (run_date_FromFile.getCellTypeEnum() != CellType.BLANK))
              {
                run_date = formatter.formatCellValue(run_date_FromFile);
               
                run_date = formatDate(run_date);
                run_date = run_date.trim();
              }
              if ((account_number_FromFile != null) &&
                (account_number_FromFile.getCellTypeEnum() != CellType.BLANK)) {
                account_number = account_number_FromFile.toString().trim();
              }
              if ((closing_bal_FromFile != null) &&
                (closing_bal_FromFile.getCellTypeEnum() != CellType.BLANK))
              {
                closing_bal = formatter.formatCellValue(closing_bal_FromFile);
                closing_bal = closing_bal.trim();
              }
              fileUpldVO.setRun_date(run_date);
              fileUpldVO.setAccount_number(account_number);
              fileUpldVO.setClosing_bal(closing_bal);
             

              fileUploadList.add(fileUpldVO);
            }
          }
          i++;
        }
        logger.info("i----" + i);
        if ((fileUploadList != null) &&
          (fileUploadList.size() > 0))
        {
          insertedRowCount = insertCBALANCEData(fileUploadList);
         
          logger.info("---" + insertedRowCount);
        }
      }
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
      logger.info("Exception in insertExcelData1 :: " + exception.getMessage());
    }
    logger.info("Exiting Method");
   
    return insertedRowCount;
  }
 
  public String insertCBALANCEData(ArrayList<FileUtitlityVO> fileUploadList)
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement ppt = null;
    LoggableStatement ppt1 = null;
    LoggableStatement ppt2 = null;
    FileUtitlityVO excelDataVO = null;
    int insertCount = 0;
    int failedCount = 0;
    try
    {
      con = DBConnectionUtility.getConnection();
      HttpSession session = ServletActionContext.getRequest().getSession();
      String userId = (String)session.getAttribute("loginedUserId");
      for (int i = 0; i < fileUploadList.size(); i++)
      {
        ppt = new LoggableStatement(con, "INSERT INTO REP_CLOSING_BAL_FINACLE(RUN_DATE,ACCOUNT_NUMBER,CLOSING_BAL,ROW_ID) VALUES(TO_DATE(?,'DD-MM-YY'),?,?,CLOSING_BAL_SEQ.NEXTVAL)");
        excelDataVO = new FileUtitlityVO();
        excelDataVO = (FileUtitlityVO)fileUploadList.get(i);
        try
        {
          ppt.setString(1, excelDataVO.getRun_date());
          ppt.setString(2, excelDataVO.getAccount_number());
          ppt.setString(3, excelDataVO.getClosing_bal());
         
          logger.info("Before Insert" + ppt.getQueryString());
          ppt.executeUpdate();
         
          insertCount++;
         
          logger.info("After Insert : " + insertCount);
         
          String insertException = "SUCCESS";
         
          ppt2 = new LoggableStatement(con, "INSERT INTO MNL_FLE_UPLD_ERR_SUCCESS_DTA(UNIQUE_ID,EXCEPTION_MESSAGE,TABLE_NAME,UPLOAD_DATE,USER_ID) VALUES(?,?,?,SYSTIMESTAMP,?)");
         
          ppt2.setString(1, excelDataVO.getAccount_number());
          ppt2.setString(2, insertException);
          ppt2.setString(3, "REP_CLOSING_BAL_FINACLE");
          ppt2.setString(4, userId);
          ppt2.executeUpdate();
        }
        catch (SQLException se)
        {
          failedCount++;
          String insertException = se.getMessage().trim();
          ppt1 = new LoggableStatement(con, "INSERT INTO MNL_FLE_UPLD_ERR_SUCCESS_DTA(UNIQUE_ID,EXCEPTION_MESSAGE,TABLE_NAME,UPLOAD_DATE,USER_ID) VALUES(?,?,?,SYSTIMESTAMP,?)");
          ppt1.setString(1, excelDataVO.getAccount_number());
          ppt1.setString(2, insertException);
          ppt1.setString(3, "REP_CLOSING_BAL_FINACLE");
          ppt1.setString(4, userId);
          ppt1.executeUpdate();
          logger.info("SQLException :" + se);
        }
        finally
        {
          closeSqlRefferance(null, ppt);
          closeSqlRefferance(null, ppt1);
          closeSqlRefferance(null, ppt2);
        }
      }
      excelDataVO.setFailedCount(failedCount);
      excelDataVO.setInsertCount(insertCount);
      logger.info("Inserted Record Successfully :: " + excelDataVO.getFailedCount() + " :: " + excelDataVO.getInsertCount());
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
      logger.info("Exception in insertExcelData2 :: " + exception.getMessage());
     
      closeSqlRefferance(null, ppt);
      closeSqlRefferance(null, ppt1);
      try
      {
        con.close();
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
    finally
    {
      closeSqlRefferance(null, ppt);
      closeSqlRefferance(null, ppt1);
      try
      {
        con.close();
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
    logger.info("Exiting Method");
   
    return insertCount + "-" + failedCount;
  }
 
  public String processFXRATE86Data(FileUtitlityVO fileUpldVO)
    throws IOException
  {
    logger.info("Entering Method");
    int i = 1;
    String insertedRowCount = "";
    XSSFWorkbook workbook = null;
    ArrayList<FileUtitlityVO> fileUploadList = null;
    ArrayList<String> excelErrorList = new ArrayList();
    try
    {
      fileUploadList = new ArrayList();
     
      File filename = fileUpldVO.getInputFile();
      if (filename != null)
      {
        FileInputStream uidCreditFile = new FileInputStream(filename);
       
        workbook = new XSSFWorkbook(uidCreditFile);
        Sheet sheet = workbook.getSheetAt(0);
        String sequenceOfColumns = getSequenceOfColumns3();
        String[] columnsArray = sequenceOfColumns.split("\\|");
        boolean isSquenceMissed = false;
        Iterator<Row> rows = sheet.rowIterator();
        while (rows.hasNext())
        {
          logger.info("row---1" + rows);
          String curren49 = "";String buyexc03 = "";
          if (i == 1)
          {
            Row rowObject = (Row)rows.next();
            if (columnsArray.length != rowObject.getLastCellNum())
            {
              String excelError = "Excel Column count mismatched";
              excelErrorList.add(excelError);
              break;
            }
            for (int columnCount = 0; columnCount < columnsArray.length; columnCount++) {
              if (!columnsArray[columnCount].toString().equalsIgnoreCase(rowObject.getCell(columnCount).toString())) {
                isSquenceMissed = true;
              }
            }
            if (isSquenceMissed)
            {
              String excelError = "Excel Column Sequence mismatch";
              excelErrorList.add(excelError);
              break;
            }
          }
          else
          {
            logger.info("inside---" + i);
            fileUpldVO = new FileUtitlityVO();
            Row rowObject = (Row)rows.next();
            DataFormatter formatter = new DataFormatter();
            Cell curren49_FromFile = rowObject.getCell(0);
            Cell buyexc03_FromFile = rowObject.getCell(1);
            if ((curren49_FromFile != null) &&
              (curren49_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              curren49 = formatter.formatCellValue(curren49_FromFile);
              curren49 = curren49.trim();
            }
            if ((buyexc03_FromFile != null) &&
              (buyexc03_FromFile.getCellTypeEnum() != CellType.BLANK)) {
              buyexc03 = buyexc03_FromFile.toString().trim();
            }
            fileUpldVO.setCurren49(curren49);
            fileUpldVO.setBuyexc03(buyexc03);
           
            fileUploadList.add(fileUpldVO);
          }
          logger.info("outside----" + i);
          i++;
        }
        logger.info("UID_CREDIT_DAILY size---" + fileUploadList.size());
        logger.info("i----" + i);
        if ((fileUploadList != null) &&
          (fileUploadList.size() > 0))
        {
          insertedRowCount = updateFXRATE86Data(fileUploadList);
          logger.info("---" + insertedRowCount);
        }
      }
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
      logger.info("Exception in insertExcelData1 :: " + exception.getMessage());
    }
    logger.info("Exiting Method");
   
    return insertedRowCount;
  }
 
  public String processFXRATE86XLSData(FileUtitlityVO fileUpldVO)
    throws IOException
  {
    logger.info("Entering Method");
    int i = 1;
    String insertedRowCount = "";
    HSSFWorkbook workbook = null;
    ArrayList<FileUtitlityVO> fileUploadList = null;
    ArrayList<String> excelErrorList = new ArrayList();
    try
    {
      fileUploadList = new ArrayList();
     
      File filename = fileUpldVO.getInputFile();
      if (filename != null)
      {
        FileInputStream uidCreditFile = new FileInputStream(filename);
       
        workbook = new HSSFWorkbook(uidCreditFile);
        HSSFSheet sheet = workbook.getSheetAt(0);
        String sequenceOfColumns = getSequenceOfColumns3();
        String[] columnsArray = sequenceOfColumns.split("\\|");
        boolean isSquenceMissed = false;
        Iterator<Row> rows = sheet.rowIterator();
        while (rows.hasNext())
        {
          logger.info("row---1" + rows);
          String curren49 = "";String buyexc03 = "";
          if (i == 1)
          {
            HSSFRow rowObject = (HSSFRow)rows.next();
            if (columnsArray.length != rowObject.getLastCellNum())
            {
              String excelError = "Excel Column count mismatched";
              excelErrorList.add(excelError);
              break;
            }
            for (int columnCount = 0; columnCount < columnsArray.length; columnCount++) {
              if (!columnsArray[columnCount].toString().equalsIgnoreCase(rowObject.getCell(columnCount).toString())) {
                isSquenceMissed = true;
              }
            }
            if (isSquenceMissed)
            {
              String excelError = "Excel Column Sequence mismatch";
              excelErrorList.add(excelError);
              break;
            }
          }
          else
          {
            logger.info("inside---" + i);
            fileUpldVO = new FileUtitlityVO();
            HSSFRow rowObject = (HSSFRow)rows.next();
            DataFormatter formatter = new DataFormatter();
            HSSFCell curren49_FromFile = rowObject.getCell(0);
            HSSFCell buyexc03_FromFile = rowObject.getCell(1);
            if ((curren49_FromFile != null) &&
              (curren49_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              curren49 = formatter.formatCellValue(curren49_FromFile);
              curren49 = curren49.trim();
            }
            if ((buyexc03_FromFile != null) &&
              (buyexc03_FromFile.getCellTypeEnum() != CellType.BLANK)) {
              buyexc03 = buyexc03_FromFile.toString().trim();
            }
            fileUpldVO.setCurren49(curren49);
            fileUpldVO.setBuyexc03(buyexc03);
           
            fileUploadList.add(fileUpldVO);
          }
          logger.info("outside----" + i);
          i++;
        }
        logger.info("UID_CREDIT_DAILY size---" + fileUploadList.size());
        logger.info("i----" + i);
        if ((fileUploadList != null) &&
          (fileUploadList.size() > 0))
        {
          insertedRowCount = updateFXRATE86Data(fileUploadList);
          logger.info("---" + insertedRowCount);
        }
      }
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
      logger.info("Exception in insertExcelData1 :: " + exception.getMessage());
    }
    logger.info("Exiting Method");
   
    return insertedRowCount;
  }
 
  public String updateFXRATE86Data(ArrayList<FileUtitlityVO> fileUploadList)
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement stmt = null;
    ResultSet resultSet = null;
    LoggableStatement ppt = null;
    LoggableStatement ppt1 = null;
    LoggableStatement ppt2 = null;
    FileUtitlityVO excelDataVO = null;
    int updateCount = 0;
    int failedCount = 0;
    try
    {
      con = DBConnectionUtility.getConnection();
      HttpSession session = ServletActionContext.getRequest().getSession();
      String userId = (String)session.getAttribute("loginedUserId");
      for (int i = 0; i < fileUploadList.size(); i++)
      {
        excelDataVO = new FileUtitlityVO();
        excelDataVO = (FileUtitlityVO)fileUploadList.get(i);
        logger.info("excelDataVO " + excelDataVO.toString());
        int tstamp = 0;
        try
        {
          stmt = new LoggableStatement(con, "SELECT TSTAMP FROM FXRATE86 WHERE CODE53='FEDAI' AND CURREN49=? AND BRANCH='MBWW'");
          stmt.setString(1, excelDataVO.getCurren49());
          resultSet = stmt.executeQuery();
          if (resultSet.next())
          {
            tstamp = Integer.parseInt(resultSet.getString("TSTAMP"));
            try
            {
              ppt = new LoggableStatement(con, "UPDATE FXRATE86 SET BUYEXC03=?, SELLEX99=?,TSTAMP=? WHERE CODE53='FEDAI' AND CURREN49=? AND BRANCH='MBWW'");
              ppt.setString(1, excelDataVO.getBuyexc03());
              ppt.setString(2, excelDataVO.getBuyexc03());
              ppt.setInt(3, tstamp + 1);
              ppt.setString(4, excelDataVO.getCurren49());
              logger.info("Before Update" + ppt.getQueryString());
              ppt.executeUpdate();
              logger.info("After Update");
              updateCount++;
             
              String insertException = "SUCCESS";
             
              ppt2 = new LoggableStatement(con, "INSERT INTO MNL_FLE_UPLD_ERR_SUCCESS_DTA(UNIQUE_ID,EXCEPTION_MESSAGE,TABLE_NAME,UPLOAD_DATE,USER_ID) VALUES(?,?,?,SYSTIMESTAMP,?)");
             
              ppt2.setString(1, excelDataVO.getCurren49());
              ppt2.setString(2, insertException);
              ppt2.setString(3, "FXRATE");
              ppt2.setString(4, userId);
              ppt2.executeUpdate();
            }
            catch (SQLException se)
            {
              failedCount++;
              String insertException = se.getMessage().trim();
              ppt1 = new LoggableStatement(con, "INSERT INTO MNL_FLE_UPLD_ERR_SUCCESS_DTA(UNIQUE_ID,EXCEPTION_MESSAGE,TABLE_NAME,UPLOAD_DATE,USER_ID) VALUES(?,?,?,SYSTIMESTAMP,?)");
              ppt1.setString(1, excelDataVO.getCurren49());
              ppt1.setString(2, insertException);
              ppt1.setString(3, "FXRATE");
              ppt1.setString(4, userId);
              ppt1.executeUpdate();
            }
          }
          else
          {
            failedCount++;
            String insertException = "Currency does not exist";
            ppt1 = new LoggableStatement(con, "INSERT INTO MNL_FLE_UPLD_ERR_SUCCESS_DTA(UNIQUE_ID,EXCEPTION_MESSAGE,TABLE_NAME,UPLOAD_DATE,USER_ID) VALUES(?,?,?,SYSTIMESTAMP,?)");
            ppt1.setString(1, excelDataVO.getCurren49());
            ppt1.setString(2, insertException);
            ppt1.setString(3, "FXRATE");
            ppt1.setString(4, userId);
            ppt1.executeUpdate();
          }
        }
        catch (Exception exc)
        {
          logger.info("Exception in updateFXRATE86Data :: " + exc.getMessage());
        }
        finally
        {
          closeSqlRefferance(resultSet, stmt);
          closeSqlRefferance(null, ppt);
          closeSqlRefferance(null, ppt1);
          closeSqlRefferance(null, ppt2);
        }
      }
      logger.info("Update Record Successfully");
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
      logger.info("Exception in updateFXRATE86Data :: " + exception.getMessage());
      try
      {
        con.close();
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
    finally
    {
      try
      {
        con.close();
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
    logger.info("Exiting Method");
    return updateCount + "-" + failedCount;
  }
 
  public String processSPOTRATEData(FileUtitlityVO fileUpldVO)
    throws IOException
  {
    logger.info("Entering Method");
    int i = 1;
    String insertedRowCount = "";
    XSSFWorkbook workbook = null;
    ArrayList<FileUtitlityVO> fileUploadList = null;
    ArrayList<String> excelErrorList = new ArrayList();
    try
    {
      fileUploadList = new ArrayList();
     
      File filename = fileUpldVO.getInputFile();
      if (filename != null)
      {
        FileInputStream uidCreditFile = new FileInputStream(filename);
       
        workbook = new XSSFWorkbook(uidCreditFile);
        Sheet sheet = workbook.getSheetAt(0);
        String sequenceOfColumns = getSequenceOfColumns4();
        String[] columnsArray = sequenceOfColumns.split("\\|");
       
        boolean isSquenceMissed = false;
        Iterator<Row> rows = sheet.rowIterator();
        while (rows.hasNext())
        {
          logger.info("row---1" + rows);
          String currency = "";String spotrate = "";String reciprocal = "";
          if (i == 1)
          {
            Row rowObject = (Row)rows.next();
            if (columnsArray.length != rowObject.getLastCellNum())
            {
              String excelError = "Excel Column count mismatched";
              excelErrorList.add(excelError);
              break;
            }
            for (int columnCount = 0; columnCount < columnsArray.length; columnCount++) {
              if (!columnsArray[columnCount].toString().equalsIgnoreCase(rowObject.getCell(columnCount).toString())) {
                isSquenceMissed = true;
              }
            }
            if (isSquenceMissed)
            {
              String excelError = "Excel Column Sequence mismatch";
              excelErrorList.add(excelError);
              break;
            }
          }
          else
          {
            logger.info("inside---" + i);
            fileUpldVO = new FileUtitlityVO();
           
            Row rowObject = (Row)rows.next();
           

            DataFormatter formatter = new DataFormatter();
           
            Cell ccy_FromFile = rowObject.getCell(0);
            Cell spotrate_FromFile = rowObject.getCell(1);
            Cell reciprocal_FromFile = rowObject.getCell(2);
            if ((ccy_FromFile != null) &&
              (ccy_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              currency = formatter.formatCellValue(ccy_FromFile);
              currency = currency.trim();
            }
            if ((spotrate_FromFile != null) &&
              (spotrate_FromFile.getCellTypeEnum() != CellType.BLANK)) {
              spotrate = spotrate_FromFile.toString().trim();
            }
            if ((reciprocal_FromFile != null) &&
              (reciprocal_FromFile.getCellTypeEnum() != CellType.BLANK)) {
              reciprocal = reciprocal_FromFile.toString().trim();
            }
            fileUpldVO.setCurrency(currency);
            fileUpldVO.setSpotrate(spotrate);
            fileUpldVO.setReciprocal(reciprocal);
           

            fileUploadList.add(fileUpldVO);
          }
          logger.info("outside----" + i);
          i++;
        }
        logger.info("UID_CREDIT_DAILY size---" + fileUploadList.size());
        logger.info("i----" + i);
        if ((fileUploadList != null) &&
          (fileUploadList.size() > 0))
        {
          insertedRowCount = updateSPOTRATEData(fileUploadList);
          logger.info("---" + insertedRowCount);
        }
      }
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
      logger.info("Exception in processSPOTRATEData :: " + exception.getMessage());
    }
    logger.info("Exiting Method");
   
    return insertedRowCount;
  }
 
  public String processSPOTRATEXLSData(FileUtitlityVO fileUpldVO)
    throws IOException
  {
    logger.info("Entering Method");
    int i = 1;
    String insertedRowCount = "";
    HSSFWorkbook workbook = null;
    ArrayList<FileUtitlityVO> fileUploadList = null;
    ArrayList<String> excelErrorList = new ArrayList();
    try
    {
      fileUploadList = new ArrayList();
     
      File filename = fileUpldVO.getInputFile();
      if (filename != null)
      {
        FileInputStream uidCreditFile = new FileInputStream(filename);
       
        workbook = new HSSFWorkbook(uidCreditFile);
        HSSFSheet sheet = workbook.getSheetAt(0);
        String sequenceOfColumns = getSequenceOfColumns4();
        String[] columnsArray = sequenceOfColumns.split("\\|");
       
        boolean isSquenceMissed = false;
        Iterator<Row> rows = sheet.rowIterator();
        while (rows.hasNext())
        {
          String currency = "";String spotrate = "";String reciprocal = "";
          if (i == 1)
          {
            HSSFRow rowObject = (HSSFRow)rows.next();
            if (columnsArray.length != rowObject.getLastCellNum())
            {
              String excelError = "Excel Column count mismatched";
              excelErrorList.add(excelError);
              break;
            }
            for (int columnCount = 0; columnCount < columnsArray.length; columnCount++) {
              if (!columnsArray[columnCount].toString().equalsIgnoreCase(rowObject.getCell(columnCount).toString())) {
                isSquenceMissed = true;
              }
            }
            if (isSquenceMissed)
            {
              String excelError = "Excel Column Sequence mismatch";
              excelErrorList.add(excelError);
              break;
            }
          }
          else
          {
            fileUpldVO = new FileUtitlityVO();
           
            HSSFRow rowObject = (HSSFRow)rows.next();
           

            DataFormatter formatter = new DataFormatter();
           
            HSSFCell ccy_FromFile = rowObject.getCell(0);
            HSSFCell spotrate_FromFile = rowObject.getCell(1);
            HSSFCell reciprocal_FromFile = rowObject.getCell(2);
            if ((ccy_FromFile != null) &&
              (ccy_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              currency = formatter.formatCellValue(ccy_FromFile);
              currency = currency.trim();
            }
            if ((spotrate_FromFile != null) &&
              (spotrate_FromFile.getCellTypeEnum() != CellType.BLANK)) {
              spotrate = spotrate_FromFile.toString().trim();
            }
            if ((reciprocal_FromFile != null) &&
              (reciprocal_FromFile.getCellTypeEnum() != CellType.BLANK)) {
              reciprocal = reciprocal_FromFile.toString().trim();
            }
            fileUpldVO.setCurrency(currency);
            fileUpldVO.setSpotrate(spotrate);
            fileUpldVO.setReciprocal(reciprocal);
           

            fileUploadList.add(fileUpldVO);
          }
          logger.info("outside----" + i);
          i++;
        }
        logger.info("UID_CREDIT_DAILY size---" + fileUploadList.size());
        logger.info("i----" + i);
        if ((fileUploadList != null) &&
          (fileUploadList.size() > 0))
        {
          insertedRowCount = updateSPOTRATEData(fileUploadList);
         
          logger.info("---" + insertedRowCount);
        }
      }
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
      logger.info("Exception in processSPOTRATEData :: " + exception.getMessage());
    }
    logger.info("Exiting Method");
   
    return insertedRowCount;
  }
 
  public String updateSPOTRATEData(ArrayList<FileUtitlityVO> fileUploadList)
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement stmt = null;
    ResultSet resultSet = null;
    LoggableStatement ppt = null;
    LoggableStatement ppt1 = null;
    LoggableStatement ppt2 = null;
    FileUtitlityVO excelDataVO = null;
    int updateCount = 0;
    int failedCount = 0;
    try
    {
      con = DBConnectionUtility.getConnection();
      HttpSession session = ServletActionContext.getRequest().getSession();
      String userId = (String)session.getAttribute("loginedUserId");
      for (int i = 0; i < fileUploadList.size(); i++)
      {
        excelDataVO = new FileUtitlityVO();
        excelDataVO = (FileUtitlityVO)fileUploadList.get(i);
        try
        {
          stmt = new LoggableStatement(con, "SELECT SPOTRATE,RECIPROCAL,TSTAMP FROM SPOTRATE WHERE BRANCH='MBWW' AND CURRENCY=?");
          stmt.setString(1, excelDataVO.getCurrency());
          logger.info("Before Loop " + stmt.getQueryString());
          resultSet = stmt.executeQuery();
          if (resultSet.next())
          {
            int tstamp = Integer.parseInt(resultSet.getString("TSTAMP"));
            String prevSpotRate = resultSet.getString("SPOTRATE");
           
            int tstamp1 = tstamp + 1;
            logger.info("tstamp1 :: " + tstamp1);
            try
            {
              ppt = new LoggableStatement(con, "UPDATE SPOTRATE SET SPOTRATE=?,PREV_RATE=?,TSTAMP=? WHERE BRANCH='MBWW' AND CURRENCY=?");
              ppt.setString(1, excelDataVO.getSpotrate());
             
              ppt.setString(2, prevSpotRate);
             
              ppt.setInt(3, tstamp1);
              ppt.setString(4, excelDataVO.getCurrency());
             
              logger.info("Before Update" + ppt.getQueryString());
              ppt.executeUpdate();
              logger.info("After Update");
              updateCount++;
             
              String insertException = "SUCCESS";
             
              ppt2 = new LoggableStatement(con, "INSERT INTO MNL_FLE_UPLD_ERR_SUCCESS_DTA(UNIQUE_ID,EXCEPTION_MESSAGE,TABLE_NAME,UPLOAD_DATE,USER_ID) VALUES(?,?,?,SYSTIMESTAMP,?)");
             
              ppt2.setString(1, excelDataVO.getCurrency());
              ppt2.setString(2, insertException);
              ppt2.setString(3, "SPOTRATE");
              ppt2.setString(4, userId);
              ppt2.executeUpdate();
            }
            catch (SQLException se)
            {
              failedCount++;
              String insertException = se.getMessage().trim();
              ppt1 = new LoggableStatement(con, "INSERT INTO MNL_FLE_UPLD_ERR_SUCCESS_DTA(UNIQUE_ID,EXCEPTION_MESSAGE,TABLE_NAME,UPLOAD_DATE,USER_ID) VALUES(?,?,?,SYSTIMESTAMP,?)");
              ppt1.setString(1, excelDataVO.getCurrency());
              ppt1.setString(2, insertException);
              ppt1.setString(3, "SPOTRATE");
              ppt1.setString(4, userId);
              ppt1.executeUpdate();
            }
          }
          else
          {
            failedCount++;
            String insertException = "Currency does not exist";
            ppt1 = new LoggableStatement(con, "INSERT INTO MNL_FLE_UPLD_ERR_SUCCESS_DTA(UNIQUE_ID,EXCEPTION_MESSAGE,TABLE_NAME,UPLOAD_DATE,USER_ID) VALUES(?,?,?,SYSTIMESTAMP,?)");
            ppt1.setString(1, excelDataVO.getCurrency());
            ppt1.setString(2, insertException);
            ppt1.setString(3, "SPOTRATE");
            ppt1.setString(4, userId);
            ppt1.executeUpdate();
          }
        }
        catch (Exception exc)
        {
          logger.info("Exception in updateSPOTRATEData :: " + exc.getMessage());
        }
        finally
        {
          closeSqlRefferance(resultSet, stmt);
          closeSqlRefferance(null, ppt);
          closeSqlRefferance(null, ppt1);
          closeSqlRefferance(null, ppt2);
        }
      }
      excelDataVO.setFailedCount(failedCount);
      excelDataVO.setInsertCount(updateCount);
      logger.info("Update Record Successfully");
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
      logger.info("Exception in updateSPOTRATEData :: " + exception.getMessage());
      try
      {
        con.close();
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
    finally
    {
      try
      {
        con.close();
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
    logger.info("Exiting Method");
   
    return updateCount + "-" + failedCount;
  }
 
  public String processEBRCData(FileUtitlityVO fileUpldVO)
    throws IOException
  {
    logger.info("Entering Method");
    int i = 1;
    String insertedRowCount = "";
    XSSFWorkbook workbook = null;
    ArrayList<FileUtitlityVO> fileUploadList = null;
    ArrayList<String> excelErrorList = new ArrayList();
    try
    {
      fileUploadList = new ArrayList();
     
      File filename = fileUpldVO.getInputFile();
      if (filename != null)
      {
        FileInputStream uidCreditFile = new FileInputStream(filename);
       
        workbook = new XSSFWorkbook(uidCreditFile);
        Sheet sheet = workbook.getSheetAt(0);
        String sequenceOfColumns = getSequenceOfColumns5();
        String[] columnsArray = sequenceOfColumns.split("\\|");
       
        boolean isSquenceMissed = false;
        Iterator<Row> rows = sheet.rowIterator();
        while (rows.hasNext())
        {
          String iec = "";String expname = "";String ifsc = "";String billid = "";String sno = "";String sport = "";
          String sdate = "";String scc = "";String svalue = "";String rcc = "";String rvalue = "";String rdate = "";
          String rvalueinr = "";String rmtbank = "";String rmtcity = "";String rmtctry = "";String bill_refno = "";String event_refno = "";
          String crn = "";String lodgementdate = "";String exporttype = "";String invoiceno = "";String invoicedate = "";
          String repay_amt = "";String freight_value = "";String category_of_exports = "";String insurance_value = "";
          String commission_value = "";String servicing_accounting_code = "";
          String is_forfeiting = "";String is_factoring = "";String is_vostro = "";
          if (i == 1)
          {
            Row rowObject = (Row)rows.next();
            if (columnsArray.length != rowObject.getLastCellNum())
            {
              String excelError = "Excel Column count mismatched";
              excelErrorList.add(excelError);
              break;
            }
            for (int columnCount = 0; columnCount < columnsArray.length; columnCount++) {
              if (!columnsArray[columnCount].toString().equalsIgnoreCase(rowObject.getCell(columnCount).toString())) {
                isSquenceMissed = true;
              }
            }
            if (isSquenceMissed)
            {
              String excelError = "Excel Column Sequence mismatch";
              excelErrorList.add(excelError);
              break;
            }
          }
          else
          {
            logger.info("inside---" + i);
            fileUpldVO = new FileUtitlityVO();
           
            Row rowObject = (Row)rows.next();
           

            DataFormatter formatter = new DataFormatter();
           

            Cell iec_FromFile = rowObject.getCell(0);
            Cell expname_FromFile = rowObject.getCell(1);
            Cell ifsc_FromFile = rowObject.getCell(2);
            Cell billid_FromFile = rowObject.getCell(3);
            Cell sno_FromFile = rowObject.getCell(4);
            Cell sport_FromFile = rowObject.getCell(5);
            Cell sdate_FromFile = rowObject.getCell(6);
            Cell scc_FromFile = rowObject.getCell(7);
            Cell svalue_FromFile = rowObject.getCell(8);
            Cell rcc_FromFile = rowObject.getCell(9);
            Cell rvalue_FromFile = rowObject.getCell(10);
            Cell rdate_FromFile = rowObject.getCell(11);
            Cell rvalueinr_FromFile = rowObject.getCell(12);
            Cell rmtbank_FromFile = rowObject.getCell(13);
            Cell rmtcity_FromFile = rowObject.getCell(14);
            Cell rmtctry_FromFile = rowObject.getCell(15);
            Cell bill_refno_FromFile = rowObject.getCell(16);
            Cell event_refno_FromFile = rowObject.getCell(17);
            Cell crn_FromFile = rowObject.getCell(18);
            Cell lodgementdate_FromFile = rowObject.getCell(19);
            Cell exporttype_FromFile = rowObject.getCell(20);
            Cell invoiceno_FromFile = rowObject.getCell(21);
            Cell invoicedate_FromFile = rowObject.getCell(22);
            Cell repay_amt_FromFile = rowObject.getCell(23);
            Cell freight_value_FromFile = rowObject.getCell(24);
            Cell category_of_exports_FromFile = rowObject.getCell(25);
            Cell insurance_value_FromFile = rowObject.getCell(26);
            Cell commission_value_FromFile = rowObject.getCell(27);
            Cell servicing_accounting_code_FromFile = rowObject.getCell(28);
            Cell is_forfeiting_FromFile = rowObject.getCell(29);
            Cell is_factoring_FromFile = rowObject.getCell(30);
            Cell is_vostro_FromFile = rowObject.getCell(31);
            if ((iec_FromFile != null) &&
              (iec_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              iec = formatter.formatCellValue(iec_FromFile);
              iec = iec.trim();
            }
            if ((expname_FromFile != null) &&
              (expname_FromFile.getCellTypeEnum() != CellType.BLANK)) {
              expname = expname_FromFile.toString().trim();
            }
            if ((ifsc_FromFile != null) &&
              (ifsc_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              ifsc = ifsc_FromFile.toString().trim();
              ifsc = ifsc.trim();
            }
            if ((billid_FromFile != null) &&
              (billid_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              billid = formatter.formatCellValue(billid_FromFile);
              billid = billid.trim();
            }
            if ((sno_FromFile != null) &&
              (sno_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              sno = formatter.formatCellValue(sno_FromFile);
              sno = sno.trim();
            }
            if ((sport_FromFile != null) &&
              (sport_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              sport = formatter.formatCellValue(sport_FromFile);
              sport = sport.trim();
            }
            if ((sdate_FromFile != null) &&
              (sdate_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              sdate = formatter.formatCellValue(sdate_FromFile);
             
              sdate = sdate.trim();
            }
            if ((scc_FromFile != null) &&
              (scc_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              scc = scc_FromFile.toString().trim();
              scc = scc.trim();
            }
            if ((svalue_FromFile != null) &&
              (svalue_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              svalue = formatter.formatCellValue(svalue_FromFile);
              svalue = svalue.trim();
            }
            if ((rcc_FromFile != null) &&
              (rcc_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              rcc = rcc_FromFile.toString().trim();
              rcc = rcc.trim();
            }
            if ((rvalue_FromFile != null) &&
              (rvalue_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              rvalue = formatter.formatCellValue(rvalue_FromFile);
              rvalue = rvalue.trim();
            }
            if ((rdate_FromFile != null) &&
              (rdate_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              rdate = formatter.formatCellValue(rdate_FromFile);
             
              rdate = rdate.toString().trim();
            }
            if ((rvalueinr_FromFile != null) &&
              (rvalueinr_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              rvalueinr = rvalueinr_FromFile.toString().trim();
              rvalueinr = rvalueinr.trim();
            }
            if ((rmtbank_FromFile != null) &&
              (rmtbank_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              rmtbank = formatter.formatCellValue(rmtbank_FromFile);
              rmtbank = rmtbank.trim();
            }
            if ((rmtcity_FromFile != null) &&
              (rmtcity_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              rmtcity = rmtcity_FromFile.toString().trim();
              rmtcity = rmtcity.trim();
            }
            if ((rmtctry_FromFile != null) &&
              (rmtctry_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              rmtctry = formatter.formatCellValue(rmtctry_FromFile);
              rmtctry = rmtctry.trim();
            }
            if ((bill_refno_FromFile != null) &&
              (bill_refno_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              bill_refno = bill_refno_FromFile.toString().trim();
              bill_refno = bill_refno.trim();
            }
            if ((event_refno_FromFile != null) &&
              (event_refno_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              event_refno = formatter.formatCellValue(event_refno_FromFile);
              event_refno = event_refno.trim();
            }
            if ((crn_FromFile != null) &&
              (crn_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              crn = formatter.formatCellValue(crn_FromFile);
             
              crn = crn.trim();
            }
            if ((lodgementdate_FromFile != null) &&
              (lodgementdate_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              lodgementdate = formatter.formatCellValue(lodgementdate_FromFile);
             
              lodgementdate = lodgementdate.trim();
            }
            if ((exporttype_FromFile != null) &&
              (exporttype_FromFile.getCellTypeEnum() != CellType.BLANK)) {
              exporttype = exporttype_FromFile.toString().trim();
            }
            if ((invoiceno_FromFile != null) &&
              (invoiceno_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              invoiceno = formatter.formatCellValue(invoiceno_FromFile);
             
              invoiceno = invoiceno.trim();
            }
            if ((invoicedate_FromFile != null) &&
              (invoicedate_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              invoicedate = formatter.formatCellValue(invoicedate_FromFile);
             
              invoicedate = invoicedate.trim();
            }
            if ((repay_amt_FromFile != null) &&
              (repay_amt_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              repay_amt = repay_amt_FromFile.toString().trim();
              repay_amt = repay_amt.trim();
            }
            if ((freight_value_FromFile != null) &&
              (freight_value_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              freight_value = freight_value_FromFile.toString().trim();
              freight_value = freight_value.trim();
            }
            if ((category_of_exports_FromFile != null) &&
              (category_of_exports_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              category_of_exports = formatter.formatCellValue(category_of_exports_FromFile);
              category_of_exports = category_of_exports.trim();
            }
            if ((insurance_value_FromFile != null) &&
              (insurance_value_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              insurance_value = insurance_value_FromFile.toString().trim();
              insurance_value = insurance_value.trim();
            }
            if ((commission_value_FromFile != null) &&
              (commission_value_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              commission_value = formatter.formatCellValue(commission_value_FromFile);
              commission_value = commission_value.trim();
            }
            if ((servicing_accounting_code_FromFile != null) &&
              (servicing_accounting_code_FromFile.getCellTypeEnum() != CellType.BLANK)) {
              servicing_accounting_code = servicing_accounting_code_FromFile.toString().trim();
            }
            if ((is_forfeiting_FromFile != null) &&
              (is_forfeiting_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              is_forfeiting = is_forfeiting_FromFile.toString().trim();
              is_forfeiting = is_forfeiting.trim();
            }
            if ((is_factoring_FromFile != null) &&
              (is_factoring_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              is_factoring = formatter.formatCellValue(is_factoring_FromFile);
              is_factoring = is_factoring.trim();
            }
            if ((is_vostro_FromFile != null) &&
              (is_vostro_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              is_vostro = is_vostro_FromFile.toString().trim();
              is_vostro = is_vostro.trim();
            }
            fileUpldVO.setIec(iec);
            fileUpldVO.setExpname(expname);
            fileUpldVO.setIfsc(ifsc);
            fileUpldVO.setBillid(billid);
            fileUpldVO.setSno(sno);
            fileUpldVO.setSport(sport);
            fileUpldVO.setSdate(sdate);
            fileUpldVO.setScc(scc);
            fileUpldVO.setSvalue(svalue);
            fileUpldVO.setRcc(rcc);
            fileUpldVO.setRvalue(rvalue);
            fileUpldVO.setRdate(rdate);
            fileUpldVO.setRvalueinr(rvalueinr);
            fileUpldVO.setRmtbank(rmtbank);
            fileUpldVO.setRmtcity(rmtcity);
            fileUpldVO.setRmtctry(rmtctry);
            fileUpldVO.setBill_refno(bill_refno);
            fileUpldVO.setEvent_refno(event_refno);
            fileUpldVO.setCrn(crn);
            fileUpldVO.setLodgementdate(lodgementdate);
            fileUpldVO.setExporttype(exporttype);
            fileUpldVO.setInvoiceno(invoiceno);
            fileUpldVO.setInvoicedate(invoicedate);
            fileUpldVO.setRepay_amt(repay_amt);
            fileUpldVO.setFreight_value(freight_value);
            fileUpldVO.setCategory_of_exports(category_of_exports);
            fileUpldVO.setInsurance_value(insurance_value);
            fileUpldVO.setCommission_value(commission_value);
            fileUpldVO.setServicing_accounting_code(servicing_accounting_code);
            fileUpldVO.setIs_forfeiting(is_forfeiting);
            fileUpldVO.setIs_factoring(is_factoring);
            fileUpldVO.setIs_vostro(is_vostro);
           


            fileUploadList.add(fileUpldVO);
          }
          logger.info("outside----" + i);
          i++;
        }
        logger.info("EBRC size---" + fileUploadList.size());
        logger.info("i----" + i);
        if ((fileUploadList != null) &&
          (fileUploadList.size() > 0))
        {
          insertedRowCount = insertEBRCData(fileUploadList);
          logger.info("---" + insertedRowCount);
        }
      }
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
      logger.info("Exception in insertExcelData1 :: " + exception.getMessage());
    }
    logger.info("Exiting Method");
   
    return insertedRowCount;
  }
 
  public String processEBRCXLSData(FileUtitlityVO fileUpldVO)
    throws IOException
  {
    logger.info("Entering Method");
    int i = 1;
    String insertedRowCount = "";
    HSSFWorkbook workbook = null;
    ArrayList<FileUtitlityVO> fileUploadList = null;
    ArrayList<String> excelErrorList = new ArrayList();
    try
    {
      fileUploadList = new ArrayList();
     
      File filename = fileUpldVO.getInputFile();
      if (filename != null)
      {
        FileInputStream uidCreditFile = new FileInputStream(filename);
       
        workbook = new HSSFWorkbook(uidCreditFile);
        HSSFSheet sheet = workbook.getSheetAt(0);
        String sequenceOfColumns = getSequenceOfColumns5();
        String[] columnsArray = sequenceOfColumns.split("\\|");
       
        boolean isSquenceMissed = false;
        Iterator<Row> rows = sheet.rowIterator();
        while (rows.hasNext())
        {
          String iec = "";String expname = "";String ifsc = "";String billid = "";String sno = "";String sport = "";
          String sdate = "";String scc = "";String svalue = "";String rcc = "";String rvalue = "";String rdate = "";
          String rvalueinr = "";String rmtbank = "";String rmtcity = "";String rmtctry = "";String bill_refno = "";String event_refno = "";
          String crn = "";String lodgementdate = "";String exporttype = "";String invoiceno = "";String invoicedate = "";
          String repay_amt = "";String freight_value = "";String category_of_exports = "";String insurance_value = "";
          String commission_value = "";String servicing_accounting_code = "";
          String is_forfeiting = "";String is_factoring = "";String is_vostro = "";
          if (i == 1)
          {
            HSSFRow rowObject = (HSSFRow)rows.next();
            if (columnsArray.length != rowObject.getLastCellNum())
            {
              String excelError = "Excel Column count mismatched";
              excelErrorList.add(excelError);
              break;
            }
            for (int columnCount = 0; columnCount < columnsArray.length; columnCount++) {
              if (!columnsArray[columnCount].toString().equalsIgnoreCase(rowObject.getCell(columnCount).toString())) {
                isSquenceMissed = true;
              }
            }
            if (isSquenceMissed)
            {
              String excelError = "Excel Column Sequence mismatch";
              excelErrorList.add(excelError);
              break;
            }
          }
          else
          {
            logger.info("inside---" + i);
            fileUpldVO = new FileUtitlityVO();
           
            HSSFRow rowObject = (HSSFRow)rows.next();
           

            DataFormatter formatter = new DataFormatter();
           
            HSSFCell iec_FromFile = rowObject.getCell(0);
            HSSFCell expname_FromFile = rowObject.getCell(1);
            HSSFCell ifsc_FromFile = rowObject.getCell(2);
            HSSFCell billid_FromFile = rowObject.getCell(3);
            HSSFCell sno_FromFile = rowObject.getCell(4);
            HSSFCell sport_FromFile = rowObject.getCell(5);
            HSSFCell sdate_FromFile = rowObject.getCell(6);
            HSSFCell scc_FromFile = rowObject.getCell(7);
            HSSFCell svalue_FromFile = rowObject.getCell(8);
            HSSFCell rcc_FromFile = rowObject.getCell(9);
            HSSFCell rvalue_FromFile = rowObject.getCell(10);
            HSSFCell rdate_FromFile = rowObject.getCell(11);
            HSSFCell rvalueinr_FromFile = rowObject.getCell(12);
            HSSFCell rmtbank_FromFile = rowObject.getCell(13);
            HSSFCell rmtcity_FromFile = rowObject.getCell(14);
            HSSFCell rmtctry_FromFile = rowObject.getCell(15);
            HSSFCell bill_refno_FromFile = rowObject.getCell(16);
            HSSFCell event_refno_FromFile = rowObject.getCell(17);
            HSSFCell crn_FromFile = rowObject.getCell(18);
            HSSFCell lodgementdate_FromFile = rowObject.getCell(19);
            HSSFCell exporttype_FromFile = rowObject.getCell(20);
            HSSFCell invoiceno_FromFile = rowObject.getCell(21);
            HSSFCell invoicedate_FromFile = rowObject.getCell(22);
            HSSFCell repay_amt_FromFile = rowObject.getCell(23);
            HSSFCell freight_value_FromFile = rowObject.getCell(24);
            HSSFCell category_of_exports_FromFile = rowObject.getCell(25);
            HSSFCell insurance_value_FromFile = rowObject.getCell(26);
            HSSFCell commission_value_FromFile = rowObject.getCell(27);
            HSSFCell servicing_accounting_code_FromFile = rowObject.getCell(28);
            HSSFCell is_forfeiting_FromFile = rowObject.getCell(29);
            HSSFCell is_factoring_FromFile = rowObject.getCell(30);
            HSSFCell is_vostro_FromFile = rowObject.getCell(31);
            if ((iec_FromFile != null) &&
              (iec_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              iec = formatter.formatCellValue(iec_FromFile);
              iec = iec.trim();
            }
            if ((expname_FromFile != null) &&
              (expname_FromFile.getCellTypeEnum() != CellType.BLANK)) {
              expname = expname_FromFile.toString().trim();
            }
            if ((ifsc_FromFile != null) &&
              (ifsc_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              ifsc = ifsc_FromFile.toString().trim();
              ifsc = ifsc.trim();
            }
            if ((billid_FromFile != null) &&
              (billid_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              billid = formatter.formatCellValue(billid_FromFile);
              billid = billid.trim();
            }
            if ((sno_FromFile != null) &&
              (sno_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              sno = formatter.formatCellValue(sno_FromFile);
              sno = sno.trim();
            }
            if ((sport_FromFile != null) &&
              (sport_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              sport = formatter.formatCellValue(sport_FromFile);
              sport = sport.trim();
            }
            if ((sdate_FromFile != null) &&
              (sdate_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              sdate = formatter.formatCellValue(sdate_FromFile);
             
              sdate = sdate.trim();
            }
            if ((scc_FromFile != null) &&
              (scc_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              scc = scc_FromFile.toString().trim();
              scc = scc.trim();
            }
            if ((svalue_FromFile != null) &&
              (svalue_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              svalue = formatter.formatCellValue(svalue_FromFile);
              svalue = svalue.trim();
            }
            if ((rcc_FromFile != null) &&
              (rcc_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              rcc = rcc_FromFile.toString().trim();
              rcc = rcc.trim();
            }
            if ((rvalue_FromFile != null) &&
              (rvalue_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              rvalue = formatter.formatCellValue(rvalue_FromFile);
              rvalue = rvalue.trim();
            }
            if ((rdate_FromFile != null) &&
              (rdate_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              rdate = formatter.formatCellValue(rdate_FromFile);
             
              rdate = rdate.trim();
            }
            if ((rvalueinr_FromFile != null) &&
              (rvalueinr_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              rvalueinr = rvalueinr_FromFile.toString().trim();
              rvalueinr = rvalueinr.trim();
            }
            if ((rmtbank_FromFile != null) &&
              (rmtbank_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              rmtbank = formatter.formatCellValue(rmtbank_FromFile);
              rmtbank = rmtbank.trim();
            }
            if ((rmtcity_FromFile != null) &&
              (rmtcity_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              rmtcity = rmtcity_FromFile.toString().trim();
              rmtcity = rmtcity.trim();
            }
            if ((rmtctry_FromFile != null) &&
              (rmtctry_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              rmtctry = formatter.formatCellValue(rmtctry_FromFile);
              rmtctry = rmtctry.trim();
            }
            if ((bill_refno_FromFile != null) &&
              (bill_refno_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              bill_refno = bill_refno_FromFile.toString().trim();
              bill_refno = bill_refno.trim();
            }
            if ((event_refno_FromFile != null) &&
              (event_refno_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              event_refno = formatter.formatCellValue(event_refno_FromFile);
              event_refno = event_refno.trim();
            }
            if ((crn_FromFile != null) &&
              (crn_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              crn = formatter.formatCellValue(crn_FromFile);
             
              crn = crn.trim();
            }
            if ((lodgementdate_FromFile != null) &&
              (lodgementdate_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              lodgementdate = formatter.formatCellValue(lodgementdate_FromFile);
             
              lodgementdate = lodgementdate.trim();
            }
            if ((exporttype_FromFile != null) &&
              (exporttype_FromFile.getCellTypeEnum() != CellType.BLANK)) {
              exporttype = exporttype_FromFile.toString().trim();
            }
            if ((invoiceno_FromFile != null) &&
              (invoiceno_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              invoiceno = formatter.formatCellValue(invoiceno_FromFile);
             
              invoiceno = invoiceno.trim();
            }
            if ((invoicedate_FromFile != null) &&
              (invoicedate_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              invoicedate = formatter.formatCellValue(invoicedate_FromFile);
             
              invoicedate = invoicedate.trim();
            }
            if ((repay_amt_FromFile != null) &&
              (repay_amt_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              repay_amt = repay_amt_FromFile.toString().trim();
              repay_amt = repay_amt.trim();
            }
            if ((freight_value_FromFile != null) &&
              (freight_value_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              freight_value = freight_value_FromFile.toString().trim();
              freight_value = freight_value.trim();
            }
            if ((category_of_exports_FromFile != null) &&
              (category_of_exports_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              category_of_exports = formatter.formatCellValue(category_of_exports_FromFile);
              category_of_exports = category_of_exports.trim();
            }
            if ((insurance_value_FromFile != null) &&
              (insurance_value_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              insurance_value = insurance_value_FromFile.toString().trim();
              insurance_value = insurance_value.trim();
            }
            if ((commission_value_FromFile != null) &&
              (commission_value_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              commission_value = formatter.formatCellValue(commission_value_FromFile);
              commission_value = commission_value.trim();
            }
            if ((servicing_accounting_code_FromFile != null) &&
              (servicing_accounting_code_FromFile.getCellTypeEnum() != CellType.BLANK)) {
              servicing_accounting_code = servicing_accounting_code_FromFile.toString().trim();
            }
            if ((is_forfeiting_FromFile != null) &&
              (is_forfeiting_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              is_forfeiting = is_forfeiting_FromFile.toString().trim();
              is_forfeiting = is_forfeiting.trim();
            }
            if ((is_factoring_FromFile != null) &&
              (is_factoring_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              is_factoring = formatter.formatCellValue(is_factoring_FromFile);
              is_factoring = is_factoring.trim();
            }
            if ((is_vostro_FromFile != null) &&
              (is_vostro_FromFile.getCellTypeEnum() != CellType.BLANK))
            {
              is_vostro = is_vostro_FromFile.toString().trim();
              is_vostro = is_vostro.trim();
            }
            fileUpldVO.setIec(iec);
            fileUpldVO.setExpname(expname);
            fileUpldVO.setIfsc(ifsc);
            fileUpldVO.setBillid(billid);
            fileUpldVO.setSno(sno);
            fileUpldVO.setSport(sport);
            fileUpldVO.setSdate(sdate);
            fileUpldVO.setScc(scc);
            fileUpldVO.setSvalue(svalue);
            fileUpldVO.setRcc(rcc);
            fileUpldVO.setRvalue(rvalue);
            fileUpldVO.setRdate(rdate);
            fileUpldVO.setRvalueinr(rvalueinr);
            fileUpldVO.setRmtbank(rmtbank);
            fileUpldVO.setRmtcity(rmtcity);
            fileUpldVO.setRmtctry(rmtctry);
            fileUpldVO.setBill_refno(bill_refno);
            fileUpldVO.setEvent_refno(event_refno);
            fileUpldVO.setCrn(crn);
            fileUpldVO.setLodgementdate(lodgementdate);
            fileUpldVO.setExporttype(exporttype);
            fileUpldVO.setInvoiceno(invoiceno);
            fileUpldVO.setInvoicedate(invoicedate);
            fileUpldVO.setRepay_amt(repay_amt);
            fileUpldVO.setFreight_value(freight_value);
            fileUpldVO.setCategory_of_exports(category_of_exports);
            fileUpldVO.setInsurance_value(insurance_value);
            fileUpldVO.setCommission_value(commission_value);
            fileUpldVO.setServicing_accounting_code(servicing_accounting_code);
            fileUpldVO.setIs_forfeiting(is_forfeiting);
            fileUpldVO.setIs_factoring(is_factoring);
            fileUpldVO.setIs_vostro(is_vostro);
           

            fileUploadList.add(fileUpldVO);
          }
          logger.info("outside----" + i);
          i++;
        }
        logger.info("EBRC size---" + fileUploadList.size());
        logger.info("i----" + i);
        if ((fileUploadList != null) &&
          (fileUploadList.size() > 0))
        {
          insertedRowCount = insertEBRCData(fileUploadList);
         
          logger.info("---" + insertedRowCount);
        }
      }
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
      logger.info("Exception in insertExcelData1 :: " + exception.getMessage());
    }
    logger.info("Exiting Method");
   
    return insertedRowCount;
  }
 
  public String insertEBRCData(ArrayList<FileUtitlityVO> fileUploadList)
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement ppt = null;
    LoggableStatement ppt1 = null;
    LoggableStatement ppt2 = null;
    FileUtitlityVO excelDataVO = null;
    int insertCount = 0;
    int failedCount = 0;
    try
    {
      con = DBConnectionUtility.getConnection();
      HttpSession session = ServletActionContext.getRequest().getSession();
      String userId = (String)session.getAttribute("loginedUserId");
      for (int i = 0; i < fileUploadList.size(); i++)
      {
        ppt = new LoggableStatement(con, "INSERT INTO ETT_EBRC_MANUAL_UPLOAD_STG(IEC,EXPNAME,IFSC,BILLID,SNO,SPORT,SDATE,SCC,SVALUE,RCC,RVALUE,RDATE,RVALUEINR,RMTBANK,RMTCITY,RMTCTRY,BILL_REFNO,EVENT_REFNO,CRN,LODGEMENTDATE,EXPORTTYPE,INVOICENO,INVOICEDATE,REPAY_AMT,FILE_UPLOAD_DATE,FILE_GENERATED_STATUS,FREIGHT_VALUE,CATEGORY_OF_EXPORTS,INSURANCE_VALUE,COMMISSION_VALUE,SERVICING_ACCOUNTING_CODE,IS_FORFEITING,IS_FACTORING,IS_VOSTRO) VALUES(?,?,?,?,?,?,TO_DATE(?,'MM-DD-YYYY'),?,?,?,?,TO_DATE(?,'MM-DD-YYYY'),?,?,?,?,?,?,?,TO_DATE(?,'MM-DD-YYYY'),?,?,TO_DATE(?,'MM-DD-YYYY'),?,SYSTIMESTAMP,'N',?,?,?,?,?,?,?,?)");
        excelDataVO = new FileUtitlityVO();
        excelDataVO = (FileUtitlityVO)fileUploadList.get(i);
        try
        {
          ppt.setString(1, excelDataVO.getIec());
          ppt.setString(2, excelDataVO.getExpname());
          ppt.setString(3, excelDataVO.getIfsc());
          ppt.setString(4, excelDataVO.getBillid());
          ppt.setString(5, excelDataVO.getSno());
          ppt.setString(6, excelDataVO.getSport());
          ppt.setString(7, excelDataVO.getSdate());
          ppt.setString(8, excelDataVO.getScc());
          ppt.setString(9, excelDataVO.getSvalue());
          ppt.setString(10, excelDataVO.getRcc());
          ppt.setString(11, excelDataVO.getRvalue());
          ppt.setString(12, excelDataVO.getRdate());
          ppt.setString(13, excelDataVO.getRvalueinr());
          ppt.setString(14, excelDataVO.getRmtbank());
          ppt.setString(15, excelDataVO.getRmtcity());
          ppt.setString(16, excelDataVO.getRmtctry());
          ppt.setString(17, excelDataVO.getBill_refno());
          ppt.setString(18, excelDataVO.getEvent_refno());
          ppt.setString(19, excelDataVO.getCrn());
          ppt.setString(20, excelDataVO.getLodgementdate());
          ppt.setString(21, excelDataVO.getExporttype());
          ppt.setString(22, excelDataVO.getInvoiceno());
          ppt.setString(23, excelDataVO.getInvoicedate());
          ppt.setString(24, excelDataVO.getRepay_amt());
          ppt.setString(25, excelDataVO.getFreight_value());
          ppt.setString(26, excelDataVO.getCategory_of_exports());
          ppt.setString(27, excelDataVO.getInsurance_value());
          ppt.setString(28, excelDataVO.getCommission_value());
          ppt.setString(29, excelDataVO.getServicing_accounting_code());
          ppt.setString(30, excelDataVO.getIs_forfeiting());
          ppt.setString(31, excelDataVO.getIs_factoring());
          ppt.setString(32, excelDataVO.getIs_vostro());
         

          logger.info("Before Insert" + ppt.getQueryString());
          ppt.executeUpdate();
          logger.info("After Insert");
          insertCount++;
         
          String insertException = "SUCCESS";
         
          ppt2 = new LoggableStatement(con, "INSERT INTO MNL_FLE_UPLD_ERR_SUCCESS_DTA(UNIQUE_ID,EXCEPTION_MESSAGE,TABLE_NAME,UPLOAD_DATE,USER_ID) VALUES(?,?,?,SYSTIMESTAMP,?)");
         
          ppt2.setString(1, excelDataVO.getBill_refno() + excelDataVO.getEvent_refno());
          ppt2.setString(2, insertException);
          ppt2.setString(3, "ETT_EBRC_MANUAL_UPLOAD_STG");
          ppt2.setString(4, userId);
          ppt2.executeUpdate();
        }
        catch (SQLException se)
        {
          failedCount++;
          String insertException = se.getMessage().trim();
          ppt1 = new LoggableStatement(con, "INSERT INTO MNL_FLE_UPLD_ERR_SUCCESS_DTA(UNIQUE_ID,EXCEPTION_MESSAGE,TABLE_NAME,UPLOAD_DATE,USER_ID) VALUES(?,?,?,SYSTIMESTAMP,?)");
          ppt1.setString(1, excelDataVO.getBill_refno() + excelDataVO.getEvent_refno());
          ppt1.setString(2, insertException);
          ppt1.setString(3, "ETT_EBRC_MANUAL_UPLOAD_STG");
          ppt1.setString(4, userId);
          ppt1.executeUpdate();
        }
        finally
        {
          closeSqlRefferance(null, ppt);
          closeSqlRefferance(null, ppt1);
          closeSqlRefferance(null, ppt2);
        }
      }
      logger.info("Inserted Record Successfully");
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
      logger.info("Exception in insertExcelData2 :: " + exception.getMessage());
     
      closeSqlRefferance(null, ppt);
      closeSqlRefferance(null, ppt1);
      try
      {
        con.close();
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
    finally
    {
      closeSqlRefferance(null, ppt);
      closeSqlRefferance(null, ppt1);
      try
      {
        con.close();
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
    logger.info("Exiting Method");
   
    return insertCount + "-" + failedCount;
  }
 
  public String getSequenceOfColumns()
  {
    return "NOSTRO|CCY|ENTRY_DATE|SOL_ID_B|SOL_ID_C|UNIQUE_ID|AMOUNT|VALUE_DATE|DR_CR|OWNER_REF_NUM|INST_REF_NUM|FIELD_86|RUN_DATE";
  }
 
  public String getSequenceOfColumns1()
  {
    return "ACCOUNT_NAME|SOL_ID|CCY|ENTRY_DATE|OWNER_REF|UID_NO|SELECT_T_F|ABSOLUTE_AMT|OUTSTANDING_AMT|DR_CR|VALUE_DATE|INST_REF_NUM|SUPPLEMENTARY_DTLS|NOSTRO_ID|DATE_OF_RECOVERY|TRAN_ID|STATUS|ADDL_DETAIL1|ADDL_DETAIL2|ADDL_DETAIL3|RUN_DATE";
  }
 
  public String getSequenceOfColumns2()
  {
    return "RUN_DATE|ACCOUNT_NUMBER|CLOSING_BAL";
  }
 
  public String getSequenceOfColumns3()
  {
    return "CURREN49|BUYEXC03";
  }
 
  public String getSequenceOfColumns4()
  {
    return "CURRENCY|SPOTRATE";
  }
 
  public String getSequenceOfColumns5()
  {
    return "IEC|EXPNAME|IFSC|BILLID|SNO|SPORT|SDATE|SCC|SVALUE|RCC|RVALUE|RDATE|RVALUEINR|RMTBANK|RMTCITY|RMTCTRY|BILL_REFNO|EVENT_REFNO|CRN|LODGEMENTDATE|EXPORTTYPE|INVOICENO|INVOICEDATE|REPAY_AMT|FREIGHT_VALUE|CATEGORY_OF_EXPORTS|INSURANCE_VALUE|COMMISSION_VALUE|SERVICING_ACCOUNTING_CODE|IS_FORFEITING|IS_FACTORING|IS_VOSTRO";
  }
 
  public String getSequenceOfColumns6()
  {
    return "NOSTRO|CCY|ENTRY_DATE|SOL_ID_B|SOL_ID_C|UNIQUE_ID|AMOUNT|VALUE_DATE|DR_CR|OWNER_REF_NUM|INST_REF_NUM|FIELD_86|RUN_DATE";
  }
 
  public static boolean isCellEmpty(Cell cell)
  {
    if ((cell == null) || (cell.getCellType() == 3)) {
      return true;
    }
    return false;
  }
 
  public String closeWindow()
    throws Exception
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement log = null;
    ResultSet rs = null;
    String closeUrl = "";
    HttpServletResponse response = null;
    HttpServletRequest request = null;
    try
    {
      con = DBConnectionUtility.getConnection();
     

      String query = "SELECT VALUE1 FROM ETT_PARAMETER_TBL WHERE PARAMETER_ID = 'closeURL'";
      log = new LoggableStatement(con, query);
      rs = log.executeQuery();
      if (rs.next()) {
        closeUrl = rs.getString("VALUE1");
      }
      response = (HttpServletResponse)
        ActionContext.getContext().get("com.opensymphony.xwork2.dispatcher.HttpServletResponse");
      request = (HttpServletRequest)ActionContext.getContext().get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
     
      logger.info("FileUtility closeWindow ... cookie clear before Redirect...");
     
      HttpSession ses = request.getSession(false);
      if (ses != null)
      {
        String sesId = ses.getId();
        ses.invalidate();
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
          if (sesId.equalsIgnoreCase(cookie.getValue()))
          {
            cookie.setMaxAge(0);
            cookie.setValue(null);
            cookie.setDomain(request.getServerName());
            cookie.setPath(request.getServletContext().getContextPath() + "/");
            cookie.setSecure(request.isSecure());
            response.addCookie(cookie);
            break;
          }
        }
      }
    }
    catch (Exception exception)
    {
      throwApplicationException(exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, log, rs);
    }
    response.sendRedirect(closeUrl);
    logger.info("Exiting Method");
    return "none";
  }
 
  public int checkLoginedUserType(String username, String teamName)
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement loggableStatement = null;
    ResultSet rs = null;
    int count = 0;
    String sqlQuery = null;
    CommonMethods commonMethods = null;
    try
    {
      commonMethods = new CommonMethods();
      con = DBConnectionUtility.getConnection();
      if (!commonMethods.isNull(username))
      {
        sqlQuery =
       
          "SELECT COUNT(*) AS LOGIN_COUNT FROM SECAGE88 U,TEAMUSRMAP T WHERE T.USERKEY = U.SKEY80 AND U.NAME85 ='" + username + "'" + " AND T.TEAMKEY = '" + teamName + "'";
       
        loggableStatement = new LoggableStatement(con, sqlQuery);
        logger.info("Checker Stage QUery--------------->" + loggableStatement.getQueryString());
        rs = loggableStatement.executeQuery();
        if (rs.next()) {
          count = rs.getInt("LOGIN_COUNT");
        }
        logger.info("Checker Stage QUery-----Count---------->" + count);
      }
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
     

      closeSqlRefferance(rs, loggableStatement);
      try
      {
        con.close();
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
    finally
    {
      closeSqlRefferance(rs, loggableStatement);
      try
      {
        con.close();
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
    return count;
  }
 
  private void zipDirectory(File dir, String zipDirName)
  {
    try
    {
      populateFilesList(dir);
     
      FileOutputStream fos = new FileOutputStream(zipDirName);
      ZipOutputStream zos = new ZipOutputStream(fos);
      for (String filePath : this.filesListInDir)
      {
        ZipEntry ze = new ZipEntry(filePath.substring(dir.getAbsolutePath().length() + 1, filePath.length()));
        zos.putNextEntry(ze);
       
        FileInputStream fis = new FileInputStream(filePath);
        byte[] buffer = new byte[1024];
        int len;
        while ((len = fis.read(buffer)) > 0)
        {
          int len;
          zos.write(buffer, 0, len);
        }
        zos.closeEntry();
        fis.close();
      }
      zos.close();
      fos.close();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
 
  private void populateFilesList(File dir)
    throws IOException
  {
    File[] files = dir.listFiles();
    for (File file : files) {
      if (file.isFile()) {
        this.filesListInDir.add(file.getAbsolutePath());
      } else {
        populateFilesList(file);
      }
    }
  }
 
  public String formatDate(String dt)
  {
    String dateFormat = "";
    try
    {
      SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
      Date txnDate = sdf.parse(dt);
      DateFormat df = new SimpleDateFormat("dd-MM-yy");
      dateFormat = df.format(txnDate);
    }
    catch (Exception exc)
    {
      exc.printStackTrace();
    }
    return dateFormat;
  }
 
  public String updateUIDDataProcedure()
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement ppt = null;
    LoggableStatement ppt2 = null;
    CallableStatement callableStatement = null;
    int updateCount = 0;
    String insertStatus = "SUCCESS";
    try
    {
      logger.info("------------- [updateUIDDataProcedure]- ------------------- :: fileUtilFromDate :: " + this.fileUtilFromDate);
      HttpSession session = ServletActionContext.getRequest().getSession();
      String userId = (String)session.getAttribute("loginedUserId");
      try
      {
        con = DBConnectionUtility.getConnection();
       
        String uidDataUpdateProcCall = "{call ETT_UID_DATA_UPDATE_PROC(?)}";
        callableStatement = con.prepareCall(uidDataUpdateProcCall);
        callableStatement.setString(1, this.fileUtilFromDate);
        callableStatement.executeUpdate();
      }
      catch (SQLException se)
      {
        addActionError("UID Data Not Updated...");
        insertStatus = se.getMessage().trim();
      }
      finally
      {
        closeSqlRefferance(null, ppt);
      }
      ppt2 = new LoggableStatement(con, "INSERT INTO ETT_UID_MNG_LOGS_TABLE(UNIQUE_ID,ACTION_TYPE,ACTION_STATUS,ACTION_DATE,USER_ID) VALUES(?,?,?,SYSTIMESTAMP,?)");
     
      ppt2.setString(1, this.fileUtilityUid);
      ppt2.setString(2, "UPDATE_UID_DATA");
      ppt2.setString(3, insertStatus);
      ppt2.setString(4, userId);
      ppt2.executeUpdate();
     
      logger.info("Record Updated Successfully");
    }
    catch (Exception exception)
    {
      addActionError("UID Update Logs Not Inserted...");
      exception.printStackTrace();
      logger.info("Exception in insertExcelData2 :: " + exception.getMessage());
      if (callableStatement != null) {
        try
        {
          callableStatement.close();
        }
        catch (SQLException e)
        {
          e.printStackTrace();
        }
      }
      closeSqlRefferance(null, ppt2);
      try
      {
        con.close();
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
    finally
    {
      if (callableStatement != null) {
        try
        {
          callableStatement.close();
        }
        catch (SQLException e)
        {
          e.printStackTrace();
        }
      }
      closeSqlRefferance(null, ppt2);
      try
      {
        con.close();
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
    logger.info("Exiting Method");
    if (updateCount > 0) {
      addActionError("UID Updated Successfully...");
    } else {
      addActionError("UID Not Updated...");
    }
    return "success";
  }
 
  public String modifyInsertCIF()
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement ppt = null;
    LoggableStatement ppt2 = null;
    int updateCount = 0;
    String insertStatus = "SUCCESS";
    try
    {
      logger.info("------------- [modifyInsertCIF]- ------------------- :: fileUtilityUid :: " + this.fileUtilityUid + " :: fileUtilityCifId :: " + this.fileUtilityCifId);
      con = DBConnectionUtility.getConnection();
      HttpSession session = ServletActionContext.getRequest().getSession();
      String userId = (String)session.getAttribute("loginedUserId");
     
      ppt = new LoggableStatement(con, "UPDATE UID_CREDIT_DAILY_TBL_MAIL SET CIF_ID=? WHERE UNIQUE_ID=? ");
      try
      {
        ppt.setString(1, this.fileUtilityCifId);
        ppt.setString(2, this.fileUtilityUid);
       
        logger.info("Before Update " + ppt.getQueryString());
        updateCount = ppt.executeUpdate();
        logger.info("After Update");
       
        insertStatus = "SUCCESS";
      }
      catch (SQLException se)
      {
        addActionError("UID Not Updated...");
        insertStatus = se.getMessage().trim();
      }
      finally
      {
        closeSqlRefferance(null, ppt);
      }
      ppt2 = new LoggableStatement(con, "INSERT INTO ETT_UID_MNG_LOGS_TABLE(UNIQUE_ID,ACTION_TYPE,ACTION_STATUS,ACTION_DATE,USER_ID) VALUES(?,?,?,SYSTIMESTAMP,?)");
     
      ppt2.setString(1, this.fileUtilityUid);
      ppt2.setString(2, "MODIFY_CIF");
      ppt2.setString(3, insertStatus);
      ppt2.setString(4, userId);
      ppt2.executeUpdate();
     
      logger.info("Record Updated Successfully");
    }
    catch (Exception exception)
    {
      addActionError("UID Update Logs Not Inserted...");
      exception.printStackTrace();
      logger.info("Exception in insertExcelData2 :: " + exception.getMessage());
     
      closeSqlRefferance(null, ppt2);
      try
      {
        con.close();
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
    finally
    {
      closeSqlRefferance(null, ppt2);
      try
      {
        con.close();
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
    logger.info("Exiting Method");
    if (updateCount > 0) {
      addActionError("UID Updated Successfully...");
    } else {
      addActionError("UID Not Updated...");
    }
    return "success";
  }
 
  public String searchUidWithoutCIF()
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement ppt = null;
    ResultSet rs = null;
    CommonMethods commonMethods = null;
    try
    {
      logger.info("------------- [searchUidWithoutCIF]- ------------------- :: fileUtilFromDate :: " + this.fileUtilFromDate);
      con = DBConnectionUtility.getConnection();
      HttpSession session = ServletActionContext.getRequest().getSession();
      String userId = (String)session.getAttribute("loginedUserId");
      FileUtitlityVO fileutltVO = null;
      ppt = new LoggableStatement(con, "SELECT * FROM UID_CREDIT_DAILY_TBL_MAIL WHERE RUN_DATE=? AND CIF_ID IS NULL");
      try
      {
        commonMethods = new CommonMethods();
        this.fileUtltList = new ArrayList();
        ppt.setString(1, this.fileUtilFromDate);
       
        logger.info("Before Select " + ppt.getQueryString());
        rs = ppt.executeQuery();
        logger.info("After Select Query");
        while (rs.next())
        {
          fileutltVO = new FileUtitlityVO();
          fileutltVO.setFileUtltListUID(commonMethods.getEmptyIfNull(rs.getString("UNIQUE_ID")).trim());
          fileutltVO.setFileUtltListRefNum(commonMethods.getEmptyIfNull(rs.getString("OWNER_REF_NUM")).trim());
          fileutltVO.setFileUtltListAmount(commonMethods.getEmptyIfNull(rs.getString("AMOUNT")).trim());
          fileutltVO.setFileUtltListCifId(commonMethods.getEmptyIfNull(rs.getString("CIF_ID")).trim());
          fileutltVO.setFileUtltListCustMail(commonMethods.getEmptyIfNull(rs.getString("CUSTOMER_MAIL_ID")).trim());
         




          this.fileUtltList.add(fileutltVO);
        }
        logger.info("List:: " + this.fileUtltList.toString());
      }
      catch (Exception se)
      {
        se.printStackTrace();
      }
      finally
      {
        closeSqlRefferance(rs, ppt);
      }
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
      logger.info("Exception in : " + exception.getMessage());
      try
      {
        con.close();
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
    finally
    {
      try
      {
        con.close();
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
    logger.info("Exiting Method");
   
    return "success";
  }
 
  public String searchCifWithoutMailId()
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement ppt = null;
    ResultSet rs = null;
    try
    {
      logger.info("------------- [searchCifWithoutMailId]- ------------------- :: fileUtilFromDate :: " + this.fileUtilFromDate);
      con = DBConnectionUtility.getConnection();
      HttpSession session = ServletActionContext.getRequest().getSession();
      String userId = (String)session.getAttribute("loginedUserId");
      FileUtitlityVO fileutltVO = null;
      ppt = new LoggableStatement(con, "SELECT * FROM UID_CREDIT_DAILY_TBL_MAIL WHERE RUN_DATE=? AND (CIF_ID IS NOT NULL AND TRIM(CIF_ID)!=' ') AND CUSTOMER_MAIL_ID IS NULL");
      try
      {
        this.fileUtltList = new ArrayList();
        ppt.setString(1, this.fileUtilFromDate);
       
        logger.info("Before Select " + ppt.getQueryString());
        rs = ppt.executeQuery();
        logger.info("After Select");
        while (rs.next())
        {
          fileutltVO = new FileUtitlityVO();
          fileutltVO.setFileUtltListUID(rs.getString("UNIQUE_ID"));
          fileutltVO.setFileUtltListRefNum(rs.getString("OWNER_REF_NUM"));
          fileutltVO.setFileUtltListAmount(rs.getString("AMOUNT"));
          fileutltVO.setFileUtltListCifId(rs.getString("CIF_ID"));
          fileutltVO.setFileUtltListCustMail(rs.getString("CUSTOMER_MAIL_ID"));
         




          this.fileUtltList.add(fileutltVO);
        }
      }
      catch (SQLException se)
      {
        se.printStackTrace();
      }
      finally
      {
        closeSqlRefferance(rs, ppt);
      }
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
      logger.info("Exception in : " + exception.getMessage());
      try
      {
        con.close();
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
    finally
    {
      try
      {
        con.close();
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
    logger.info("Exiting Method");
   
    return "success";
  }
 
  public String triggerMailCustNBrnch()
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement ppt = null;
    LoggableStatement ppt2 = null;
    ResultSet rs = null;
    int updateCount = 0;
    int setValue = 1;
    boolean fileUtilityUidFlag = false;
    boolean fileUtilityCifIdFlag = false;
    String insertStatus = "SUCCESS";
    try
    {
      HashMap<String, String> configMap = NotificationUtil.getConfigMap();
      logger.info("------------- [triggerMailCustNBrnch]- ------------------- :: fileUtilityUid :: " + this.fileUtilityUid + " :: fileUtilityCifId :: " + this.fileUtilityCifId + " :: fileUtilFromDate :: " + this.fileUtilFromDate);
      con = DBConnectionUtility.getConnection();
      HttpSession session = ServletActionContext.getRequest().getSession();
      String userId = (String)session.getAttribute("loginedUserId");
      String sqlQuery = "SELECT UNIQUE_ID,AMOUNT,CIF_ID,CUSTOMER_MAIL_ID,BRANCH_MAIL_ID FROM UID_CREDIT_DAILY_TBL_MAIL WHERE RUN_DATE=TO_DATE(?,'DD-MM-YY') AND PROC_EXEC_STATUS='Y' AND (CUST_MAIL_STATUS IS NULL OR CUSTOMER_MAIL_ID !='Y') AND (BRN_MAIL_STATUS IS NULL OR BRN_MAIL_STATUS !='Y')";
      if ((this.fileUtilityUid != null) && (this.fileUtilityUid.trim() != "") && (this.fileUtilityUid.trim().length() > 0))
      {
        sqlQuery = sqlQuery + " AND UNIQUE_ID =?";
        fileUtilityUidFlag = true;
      }
      if ((this.fileUtilityCifId != null) && (this.fileUtilityCifId.trim() != "") && (this.fileUtilityCifId.trim().length() > 0))
      {
        sqlQuery = sqlQuery + " AND CIF_ID =?";
        fileUtilityCifIdFlag = true;
      }
      try
      {
        ppt = new LoggableStatement(con, sqlQuery);
        ppt.setString(1, this.fileUtilFromDate);
        if (fileUtilityUidFlag) {
          ppt.setString(++setValue, this.fileUtilityUid.trim());
        }
        if (fileUtilityCifIdFlag) {
          ppt.setString(++setValue, this.fileUtilityCifId.trim());
        }
        logger.info("Before Select " + ppt.getQueryString());
        rs = ppt.executeQuery();
        logger.info("After Select");
        if (rs.next())
        {
          String uid = rs.getString("UNIQUE_ID");
          String amount = rs.getString("AMOUNT");
          String customerId = rs.getString("CIF_ID");
         


          String to = "jadhavyo4141@gmail.com";
          String cc = "";
         
          sendEmail((String)configMap.get("SMTP_IP"),
            (String)configMap.get("SMTP_FROM"),
            (String)configMap.get("SMTP_PORT"),
            (String)configMap.get("SMTP_PWD"),
            to, cc, amount, uid, customerId, customerId);
        }
      }
      catch (SQLException se)
      {
        addActionError("Mail Not Sent....");
        insertStatus = se.getMessage().trim();
      }
      finally
      {
        closeSqlRefferance(rs, ppt);
      }
      logger.info("Record Updated Successfully");
    }
    catch (Exception exception)
    {
      addActionError("UID Update Logs Not Inserted...");
      exception.printStackTrace();
      logger.info("Exception in insertExcelData2 :: " + exception.getMessage());
     
      closeSqlRefferance(null, ppt2);
      try
      {
        con.close();
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
    finally
    {
      closeSqlRefferance(null, ppt2);
      try
      {
        con.close();
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
    logger.info("Exiting Method");
   

    return "success";
  }
 
  public String triggerMailToBrnch()
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement ppt = null;
    LoggableStatement ppt2 = null;
    ResultSet rs = null;
    int updateCount = 0;
    int setValue = 1;
    boolean fileUtilityUidFlag = false;
    boolean fileUtilityCifIdFlag = false;
    String insertStatus = "SUCCESS";
    try
    {
      HashMap<String, String> configMap = NotificationUtil.getConfigMap();
      logger.info("------------- [triggerMailToBrnch]- ------------------- :: fileUtilityUid :: " + this.fileUtilityUid + " :: fileUtilityCifId :: " + this.fileUtilityCifId + " :: fileUtilFromDate :: " + this.fileUtilFromDate);
      con = DBConnectionUtility.getConnection();
      HttpSession session = ServletActionContext.getRequest().getSession();
      String userId = (String)session.getAttribute("loginedUserId");
     
      String sqlQuery = "SELECT UNIQUE_ID,AMOUNT,CIF_ID,CUSTOMER_MAIL_ID,BRANCH_MAIL_ID FROM UID_CREDIT_DAILY_TBL_MAIL WHERE RUN_DATE=TO_DATE(?,'DD-MM-YY') AND PROC_EXEC_STATUS='Y' AND (CUST_MAIL_STATUS IS NULL OR CUSTOMER_MAIL_ID !='Y') AND (BRN_MAIL_STATUS IS NULL OR BRN_MAIL_STATUS !='Y') AND (CUSTOMER_MAIL_ID IS NULL OR CUSTOMER_MAIL_ID='') AND AND (CUSTOMER_MAIL_ID IS NOT NULL OR CUSTOMER_MAIL_ID !='')";
      if ((this.fileUtilityUid != null) && (this.fileUtilityUid != ""))
      {
        sqlQuery = sqlQuery + " AND UNIQUE_ID =?";
        fileUtilityUidFlag = true;
      }
      if ((this.fileUtilityCifId != null) && (this.fileUtilityCifId != ""))
      {
        sqlQuery = sqlQuery + " AND CIF_ID =?";
        fileUtilityCifIdFlag = true;
      }
      try
      {
        ppt = new LoggableStatement(con, sqlQuery);
        ppt.setString(1, this.fileUtilFromDate);
        if (fileUtilityUidFlag) {
          ppt.setString(++setValue, this.fileUtilityUid.trim());
        }
        if (fileUtilityCifIdFlag) {
          ppt.setString(++setValue, this.fileUtilityCifId.trim());
        }
        logger.info("Before Update " + ppt.getQueryString());
        rs = ppt.executeQuery();
        logger.info("After Update");
        while (rs.next())
        {
          String uid = rs.getString("UNIQUE_ID");
          String amount = rs.getString("AMOUNT");
          String customerId = rs.getString("CIF_ID");
          String to = rs.getString("CUSTOMER_MAIL_ID");
          String cc = rs.getString("BRANCH_MAIL_ID");
         
          sendEmail((String)configMap.get("SMTP_IP"),
            (String)configMap.get("SMTP_FROM"),
            (String)configMap.get("SMTP_PORT"),
            (String)configMap.get("SMTP_PWD"),
            to, cc, amount, uid, customerId, customerId);
        }
      }
      catch (SQLException se)
      {
        addActionError("Mail Not Sent....");
        insertStatus = se.getMessage().trim();
      }
      finally
      {
        closeSqlRefferance(rs, ppt);
      }
    }
    catch (Exception exception)
    {
      addActionError("UID Update Logs Not Inserted...");
      exception.printStackTrace();
      logger.info("Exception in insertExcelData2 :: " + exception.getMessage());
     
      closeSqlRefferance(null, ppt2);
      try
      {
        con.close();
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
    finally
    {
      closeSqlRefferance(null, ppt2);
      try
      {
        con.close();
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
    logger.info("Exiting Method");
   
    return "success";
  }
 
  public String searchMailStatusNotSucceeded()
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement ppt = null;
    ResultSet rs = null;
    try
    {
      logger.info("------------- [searchMailStatusNotSucceeded]- ------------------- :: fileUtilFromDate :: " + this.fileUtilFromDate);
      con = DBConnectionUtility.getConnection();
      HttpSession session = ServletActionContext.getRequest().getSession();
      String userId = (String)session.getAttribute("loginedUserId");
      FileUtitlityVO fileutltVO = null;
      ppt = new LoggableStatement(con, "SELECT * FROM UID_CREDIT_DAILY_TBL_MAIL WHERE RUN_DATE=? AND CUST_MAIL_STATUS IS NULL OR BRN_MAIL_STATUS IS NULL");
      try
      {
        this.fileUtltList = new ArrayList();
        ppt.setString(1, this.fileUtilFromDate);
       
        logger.info("Before Select " + ppt.getQueryString());
        rs = ppt.executeQuery();
        logger.info("After Select");
        while (rs.next())
        {
          fileutltVO = new FileUtitlityVO();
          fileutltVO.setFileUtltListUID(rs.getString("UNIQUE_ID"));
          fileutltVO.setFileUtltListRefNum(rs.getString("OWNER_REF_NUM"));
          fileutltVO.setFileUtltListCifId(rs.getString("CIF_ID"));
          fileutltVO.setFileUtltListCustMailStatus(rs.getString("CUST_MAIL_STATUS"));
          fileutltVO.setFileUtltListBrnMailStatus(rs.getString("BRN_MAIL_STATUS"));
         




          this.fileUtltList.add(fileutltVO);
        }
      }
      catch (SQLException se)
      {
        se.printStackTrace();
      }
      finally
      {
        closeSqlRefferance(rs, ppt);
      }
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
      logger.info("Exception in : " + exception.getMessage());
      try
      {
        con.close();
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
    finally
    {
      try
      {
        con.close();
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
    logger.info("Exiting Method");
   
    return "success";
  }
 
  public String sendEmail(String host, String fromEmailID, String port, String password, String to, String cc, String amount, String uid, String customerId, String ownerPassword)
  {
    DBConnectionUtility con = new DBConnectionUtility();
    String mailReleaseStatus = "";
    boolean result = true;
    String SMTPHost = host;
    String SMTPPort = port;
    String EmailUser = fromEmailID;
    String EmailPassword = password;
    try
    {
      Properties props = System.getProperties();
      props.put("mail.smtp.host", SMTPHost);
      props.put("mail.smtp.port", SMTPPort);
      props.put("mail.smtp.auth", "true");
      Session session = Session.getInstance(props,
        new FileUtilityAction.1(this, EmailUser, EmailPassword));
     





      Message message = new MimeMessage(session);
      logger.info("EmailNotificationClient : sendEmail() : TO Email : " + to);
      logger.info("EmailNotificationClient : sendEmail() : CC Email : " + cc);
      message.setFrom(new InternetAddress("trade.finance@unionbankofindia.bank"));
      if ((to != null) && (!to.equalsIgnoreCase("")))
      {
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        if ((cc != null) && (!cc.equalsIgnoreCase(""))) {
          message.addRecipient(Message.RecipientType.CC, new InternetAddress(cc));
        }
      }
      else
      {
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(cc));
      }
      message.setSubject("Inward Remittance " + uid + " & disclosure form request");
      message.setSentDate(new Date());
      String body = "<p>Dear Sir/ Madam,</p><br>";
      body = body + "<p>We wish to inform that, we received Inward Remittance of " + amount + " from " + uid + "</p><br>";
      body = body + "<p>Kindly send the disclosure form</p><br>";
      Multipart multiPart = new MimeMultipart();
      BodyPart textPart = new MimeBodyPart();
     
      textPart.setContent(body, "text/plain");
      multiPart.addBodyPart(textPart);
     




























      logger.info("EmailNotificationClient  : sendEmail() : Adding the content to mail..........");
     
      logger.info("Before mail sending");
      message.setContent(multiPart);
      Transport.send(message);
      mailReleaseStatus = "SUCCEEDED";
     
      String updateQuery = "UPDATE UID_CREDIT_DAILY_TBL_MAIL SET ";
      if ((to != null) && (!to.equalsIgnoreCase(""))) {
        updateQuery = updateQuery + "CUSTOMER_MAIL_ID ='Y', BRN_MAIL_STATUS='Y'";
      } else {
        updateQuery = updateQuery + "BRN_MAIL_STATUS='Y'";
      }
      updateQuery = updateQuery + " WHERE UNIQUE_ID=" + uid;
     
      logger.info("EmailNotificationClient  : sendEmail() :  Alert Email Sent successfully!");
    }
    catch (AddressException e)
    {
      e.printStackTrace();
      result = false;
     
      mailReleaseStatus = "FAILED|Mail sending failed due to : " +
        e.getMessage();
      logger.info("Mail sending failed due to : " + e.getMessage() +
        "\n" + e);
    }
    catch (MessagingException e)
    {
      e.printStackTrace();
      result = false;
      mailReleaseStatus = "FAILED|Mail sending failed due to : " +
        e.getMessage();
      logger.info("Mail sending failed due to : " + e.getMessage() +
        "\n" + e);
    }
    catch (NullPointerException e)
    {
      logger.info("Mail sending failed due to : " + e.getMessage());
      e.printStackTrace();
      result = false;
      mailReleaseStatus = "FAILED|Mail sending failed due to : " +
        e.getMessage();
    }
    catch (Exception e)
    {
      logger.info("Mail sending failed due to : " + e.getMessage() +
        "\n" + e);
      mailReleaseStatus = "FAILED|Mail sending failed due to : " +
        e.getMessage();
      e.printStackTrace();
      result = false;
    }
    return mailReleaseStatus;
  }
}