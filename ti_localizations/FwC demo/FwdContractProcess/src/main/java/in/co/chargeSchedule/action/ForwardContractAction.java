package in.co.chargeSchedule.action;

import com.opensymphony.xwork2.ActionContext;
import in.co.forwardcontract.bd.ForwardContractBD;
import in.co.forwardcontract.dao.exception.ApplicationException;
import in.co.forwardcontract.utility.CommonMethods;
import in.co.forwardcontract.utility.DBConnectionUtility;
import in.co.forwardcontract.utility.LoggableStatement;
import in.co.forwardcontract.vo.ForwardContractVO;
import in.co.forwardcontract.vo.StaticDataVO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

public class ForwardContractAction
  extends ForwardContractBaseAction
{
  private static Logger logger = Logger.getLogger(ForwardContractAction.class.getName());
  private static final long serialVersionUID = 1L;
  ForwardContractVO fwdContractVO;
  StaticDataVO staticDataVo;
  ArrayList<StaticDataVO> staticDataList;
  ArrayList<ForwardContractVO> forwardContractList;
 
  public ForwardContractVO getFwdContractVO()
  {
    return this.fwdContractVO;
  }
 
  public void setFwdContractVO(ForwardContractVO fwdContractVO)
  {
    this.fwdContractVO = fwdContractVO;
  }
 
  public StaticDataVO getStaticDataVo()
  {
    return this.staticDataVo;
  }
 
  public void setStaticDataVo(StaticDataVO staticDataVo)
  {
    this.staticDataVo = staticDataVo;
  }
 
  public ArrayList<StaticDataVO> getStaticDataList()
  {
    return this.staticDataList;
  }
 
  public void setStaticDataList(ArrayList<StaticDataVO> staticDataList)
  {
    this.staticDataList = staticDataList;
  }
 
  public ArrayList<ForwardContractVO> getForwardContractList()
  {
    return this.forwardContractList;
  }
 
  public void setForwardContractList(ArrayList<ForwardContractVO> forwardContractList)
  {
    this.forwardContractList = forwardContractList;
  }
 
  String idAndFwdContractNo = null;
  ArrayList<ForwardContractVO> enquiryList;
 
  public String getIdAndFwdContractNo()
  {
    return this.idAndFwdContractNo;
  }
 
  public void setIdAndFwdContractNo(String idAndFwdContractNo)
  {
    this.idAndFwdContractNo = idAndFwdContractNo;
  }
 
  public ArrayList<ForwardContractVO> getEnquiryList()
  {
    return this.enquiryList;
  }
 
  public void setEnquiryList(ArrayList<ForwardContractVO> enquiryList)
  {
    this.enquiryList = enquiryList;
  }
 
  Map<String, String> subProductList = null;
  Map<String, String> statusList = null;
 
  public Map<String, String> getSubProductList()
  {
    return SUBPRODUCT;
  }
 
  public void setSubProductList(Map<String, String> subProductList)
  {
    this.subProductList = subProductList;
  }
 
  public Map<String, String> getStatusList()
  {
    return STATUSLIST;
  }
 
  public void setStatusList(Map<String, String> statusList)
  {
    this.statusList = statusList;
  }
 
  public String landingPage()
    throws ApplicationException
  {
    logger.info("Entering Method");
    String sessionUserName = null;
    String result = null;
    String roleCount = null;
    int count = 0;
    ForwardContractBD fwdContractBD = null;
    ForwardContractVO fwdContractVO = null;
    String target = "success";
    try
    {
      fwdContractBD = new ForwardContractBD();
      fwdContractBD.setProcessDate();
      isSessionAvailable();
      fwdContractVO = new ForwardContractVO();
     
      HttpSession session = ServletActionContext.getRequest().getSession();
     
      HttpServletRequest request = (HttpServletRequest)ActionContext.getContext()
        .get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
     
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
          String get_User_ID = "SELECT SCT.USERNAME AS USER_ID FROM CENTRAL_SESSION_DETAILS SCT,LOCAL_SESSION_DETAILS LOC  WHERE SCT.CENTRAL_ID=LOC.CENTRAL_ID AND SCT.ENDED  IS NULL AND LOC.LOCAL_ID= ? ";
         

          LoggableStatement lst = new LoggableStatement(globalConnection, get_User_ID);
          lst.setString(1, sessionUserName);
          logger.info("Getting Session Value Query------------" + lst.getQueryString());
         
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
      }
      logger.info("sessionUserName-------------------------------------->" + sessionUserName);
     
      fwdContractVO.setSessionUserName(sessionUserName);
    }
    catch (Exception exception)
    {
      logger.info("User landingPage-- Exception-------------->" + exception);
     
      throwApplicationException(exception);
    }
    logger.info("target-------------" + target);
    logger.info("Exiting Method");
   
    return target;
  }
 
  public String loadCancelData()
    throws ApplicationException
  {
    logger.info("Entering Method");
    String target = "success";
    int result = 0;
    ForwardContractBD fwdContractBD = null;
    try
    {
      fwdContractBD = new ForwardContractBD();
      String sessionUserName = isSessionAvailable1();
      this.fwdContractVO.setSessionUserName(sessionUserName);
    }
    catch (Exception exception)
    {
      logger.info("Exception is" + exception.getMessage());
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return target;
  }
 
  public String loadMakerProcessData()
    throws ApplicationException
  {
    logger.info("Entering Method");
    int count = 0;
    ForwardContractBD fwdContractBD = null;
    ForwardContractVO fwdContractVO = null;
    String target = "success";
    try
    {
      fwdContractBD = new ForwardContractBD();
      fwdContractBD.setProcessDate();
      fwdContractVO = new ForwardContractVO();
     
      String sessionUserName = isSessionAvailable1();
      if (sessionUserName != null)
      {
        logger.info("Inside outer session name");
        fwdContractBD = new ForwardContractBD();
       
        logger.info("sessionUserName--->" + sessionUserName);
       
        fwdContractVO.setSessionUserName(sessionUserName);
       
        fwdContractVO.setPageType("FWCMAKER");
       
        count = fwdContractBD.checkLoginedUserType(fwdContractVO);
       
        logger.info("Count Session Name for Checker Eligiblity");
        if (count > 0) {
          target = "success";
        } else {
          target = "fail";
        }
      }
      else
      {
        target = "fail";
      }
    }
    catch (Exception exception)
    {
      logger.info("Exception is" + exception.getMessage());
      exception.printStackTrace();
    }
    logger.info("target-------------" + target);
    logger.info("Exiting Method");
    return target;
  }
 
  public String loadCheckerProcessData()
    throws ApplicationException
  {
    logger.info("Entering Method");
    int count = 0;
    ForwardContractBD fwdContractBD = null;
    ForwardContractVO fwdContractVO = null;
    String target = "success";
    try
    {
      fwdContractBD = new ForwardContractBD();
      fwdContractBD.setProcessDate();
      fwdContractVO = new ForwardContractVO();
     
      String sessionUserName = isSessionAvailable1();
      if (sessionUserName != null)
      {
        logger.info("Inside outer session name");
        fwdContractBD = new ForwardContractBD();
       
        logger.info("sessionUserName--->" + sessionUserName);
       
        fwdContractVO.setSessionUserName(sessionUserName);
       
        fwdContractVO.setPageType("FWCCHECKER");
       
        count = fwdContractBD.checkLoginedUserType(fwdContractVO);
       
        logger.info("Count Session Name for Checker Eligiblity");
        if (count > 0) {
          target = "success";
        } else {
          target = "fail";
        }
      }
      else
      {
        target = "fail";
      }
    }
    catch (Exception exception)
    {
      logger.info("Exception is" + exception.getMessage());
      exception.printStackTrace();
    }
    logger.info("target-------------" + target);
    logger.info("Exiting Method");
    return target;
  }
 
  public String loadEnquiryProcessData()
    throws ApplicationException
  {
    logger.info("Entering Method");
    try
    {
      isSessionAvailable();
    }
    catch (Exception exception)
    {
      logger.info("Exception is" + exception.getMessage());
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return "success";
  }
 
  public String loadCancelProcessData()
    throws ApplicationException
  {
    logger.info("Entering Method :: loadCancelProcessData");
    String target = "success";
    int result = 0;
    ForwardContractBD fwdContractBD = null;
    try
    {
      fwdContractBD = new ForwardContractBD();
      String sessionUserName = isSessionAvailable1();
      this.fwdContractVO.setSessionUserName(sessionUserName);
    }
    catch (Exception exception)
    {
      logger.info("Exception is" + exception.getMessage());
      exception.printStackTrace();
    }
    logger.info("Exiting Method :: loadCancelProcessData");
    return target;
  }
 
  public String fetchFWCCancelDetails()
    throws ApplicationException
  {
    ForwardContractBD fwdContractBD = null;
    int result = 0;
    try
    {
      fwdContractBD = new ForwardContractBD();
     
      logger.info("ForwardContract fetchFWCCancelDetails ... before if Condition...");
      if ((this.fwdContractVO != null) &&
        (CommonMethods.isValidString(this.fwdContractVO.getFwdContractNo())))
      {
        logger.info("ForwardContract fetchFWCCancelDetails ... inside if Condition...");
       
        this.fwdContractVO = fwdContractBD.fetchFWCReferenceDetails(this.fwdContractVO.getFwdContractNo());
      }
      String sessionUserName = isSessionAvailable1();
      this.fwdContractVO.setSessionUserName(sessionUserName);
    }
    catch (Exception e)
    {
      e.printStackTrace();
      throwApplicationException(e);
    }
    return "success";
  }
 
  public String fetchFWCReferenceDetails()
    throws ApplicationException
  {
    ForwardContractBD fwdContractBD = null;
    try
    {
      isSessionAvailable();
      String screenType = this.fwdContractVO.getScreenType();
      if (this.fwdContractVO != null)
      {
        logger.info("Show FWCReference Details for " + this.idAndFwdContractNo);
        fwdContractBD = new ForwardContractBD();
        String fwdContractNo = this.idAndFwdContractNo;
        this.fwdContractVO = fwdContractBD.fetchFWCReferenceDetails(fwdContractNo);
        this.fwdContractVO.setScreenType(screenType);
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      throwApplicationException(e);
    }
    return "success";
  }
 
  public String fetchDependentTreasuryDetails()
    throws ApplicationException
  {
    ForwardContractBD fwdContractBD = null;
    try
    {
      isSessionAvailable();
      if (this.fwdContractVO != null) {
        if ((CommonMethods.isValidString(this.fwdContractVO.getTreasuryRefNo())) &&
          (CommonMethods.isValidString(this.fwdContractVO.getCustomerID())))
        {
          fwdContractBD = new ForwardContractBD();
          this.fwdContractVO = fwdContractBD.fetchDependentTreasuryDetails(this.fwdContractVO);
        }
        else
        {
          this.fwdContractVO.setOutstandingAmt("");
          this.fwdContractVO.setTreasuryRate("");
        }
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      throwApplicationException(e);
    }
    return "success";
  }
 
  public String fetchDependentTreasuryCancelDetailsWithPostings()
    throws ApplicationException
  {
    ForwardContractBD fwdContractBD = null;
    int result1 = 0;
    try
    {
      isSessionAvailable();
     
      fwdContractBD = new ForwardContractBD();
      if (this.fwdContractVO != null) {
        if ((CommonMethods.isValidString(this.fwdContractVO.getTreasuryRefNo())) &&
          (CommonMethods.isValidString(this.fwdContractVO.getCustomerID())))
        {
          this.fwdContractVO = fwdContractBD.fetchDependentCancelTreasuryDetails(this.fwdContractVO);
          this.fwdContractVO = fwdContractBD.getFWCPostingsToReverse(this.fwdContractVO);
        }
        else
        {
          this.fwdContractVO = fwdContractBD.getFWCPostingsToReverse(this.fwdContractVO);
          this.fwdContractVO.setOutstandingAmt("");
          this.fwdContractVO.setTreasuryRate("");
        }
      }
      String sessionUserName = isSessionAvailable1();
      this.fwdContractVO.setSessionUserName(sessionUserName);
    }
    catch (Exception e)
    {
      e.printStackTrace();
      throwApplicationException(e);
    }
    return "success";
  }
 
  public String fetchParticularFwdContractDetails()
    throws ApplicationException
  {
    ForwardContractBD fwdContractBD = null;
    try
    {
      isSessionAvailable();
      String screenType = this.fwdContractVO.getScreenType();
      if (this.fwdContractVO != null)
      {
        logger.info("Show details " + this.idAndFwdContractNo);
        fwdContractBD = new ForwardContractBD();
        String[] temp = this.idAndFwdContractNo.split(":");
        String id = temp[0];
        String fwdContractNo = temp[1];
        this.fwdContractVO = fwdContractBD.fetchParticularFwdContractDetails(id, fwdContractNo);
        this.fwdContractVO = fwdContractBD.generateFWCPostings(this.fwdContractVO);
        this.fwdContractVO.setScreenType(screenType);
       
        int result = 0;
       
        String sessionUserName = isSessionAvailable1();
        this.fwdContractVO.setSessionUserName(sessionUserName);
       
        result = fwdContractBD.checkLoginedUserType1(sessionUserName, "CHECKER");
        logger.info("Book screen role Check :: " + result);
        if (result == 2)
        {
          logger.info("Book screen role Check :: inside if... ");
          this.fwdContractVO.setDeleteFlag("true");
        }
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      throwApplicationException(e);
    }
    return "success";
  }
 
  public String fetchParticularFwdContractDetailstoModify()
    throws ApplicationException
  {
    ForwardContractBD fwdContractBD = null;
    try
    {
      isSessionAvailable();
      String screenType = this.fwdContractVO.getScreenType();
      if (this.fwdContractVO != null)
      {
        logger.info("Show details " + this.idAndFwdContractNo);
        fwdContractBD = new ForwardContractBD();
        String[] temp = this.idAndFwdContractNo.split(":");
        String id = temp[0];
        String fwdContractNo = temp[1];
        this.fwdContractVO = fwdContractBD.fetchParticularFwdContractDetailstoModify(id, fwdContractNo);
        this.fwdContractVO = fwdContractBD.generateFWCPostings(this.fwdContractVO);
        this.fwdContractVO.setScreenType(screenType);
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      throwApplicationException(e);
    }
    return "success";
  }
 
  public String fetchParticularCancelFwdContractDetails()
    throws ApplicationException
  {
    ForwardContractBD fwdContractBD = null;
    if (this.fwdContractVO != null) {
      fwdContractBD = new ForwardContractBD();
    }
    int result = 0;
    try
    {
      isSessionAvailable();
      String screenType = this.fwdContractVO.getScreenType();
     
      logger.info("Screen Type:" + screenType);
      if (this.fwdContractVO != null)
      {
        logger.info("Cancel screen");
       
        logger.info("Show details " + this.idAndFwdContractNo);
        fwdContractBD = new ForwardContractBD();
        String[] temp = this.idAndFwdContractNo.split(":");
        String id = temp[0];
        String fwdContractNo = temp[1];
       
        this.fwdContractVO = fwdContractBD.fetchParticularCancelFwdContractDetails(id, fwdContractNo);
        this.fwdContractVO = fwdContractBD.generateFWCPostings(this.fwdContractVO);
        this.fwdContractVO.setScreenType(screenType);
        logger.info("screen" + screenType);
       



        String sessionUserName = isSessionAvailable1();
        this.fwdContractVO.setSessionUserName(sessionUserName);
       
        result = fwdContractBD.checkLoginedUserType1(sessionUserName, "CHECKER");
        logger.info("Cancel screen role Check :: " + result);
        if (result == 2)
        {
          logger.info("Cancel screen role Check :: inside if... ");
          this.fwdContractVO.setDeleteFlag("true");
        }
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      throwApplicationException(e);
    }
    return "success";
  }
 
  public String approveFWC()
    throws ApplicationException
  {
    ForwardContractBD fwdContractBD = null;
    try
    {
      isSessionAvailable();
      if (this.fwdContractVO != null)
      {
        logger.info(" show approveFWC Record Details");
       
        fwdContractBD = new ForwardContractBD();
       
        String category = this.fwdContractVO.getCategory();
        if (category.equalsIgnoreCase("FWCBOOK"))
        {
          this.fwdContractVO = fwdContractBD.approveFWC(this.fwdContractVO, "FWCBOOK");
          if (this.fwdContractVO.getCount() == 1) {
            addActionMessage("Forward Contract Booking Approved successfully for " +
              this.fwdContractVO.getFwdContractNo());
          } else if (this.fwdContractVO.getCount() == 2) {
            addActionError("Approval Failed for " + this.fwdContractVO.getFwdContractNo() + " due to Posting Failure, Contact support team.");
          } else if (this.fwdContractVO.getCount() == 3) {
            addActionError("Approval Failed for " + this.fwdContractVO.getFwdContractNo() + " due to TREAS API Failure, Contact support team.");
          } else if (this.fwdContractVO.getCount() == 4) {
            addActionError("Approval Failed for " + this.fwdContractVO.getFwdContractNo() + " due to FTRT API Failure, Contact support team.");
          } else if (this.fwdContractVO.getCount() == 5) {
            addActionError("Could not approve " + this.fwdContractVO.getFwdContractNo() + " due to insufficient balance in customer account.");
          } else if (this.fwdContractVO.getCount() == 6) {
            addActionError("FWC number " + this.fwdContractVO.getFwdContractNo() + " is rejected as Treasury Reference No. does not exist");
          } else if (this.fwdContractVO.getCount() == 7) {
            addActionError("Approval Failed for  " + this.fwdContractVO.getFwdContractNo() + " as the custom treasury table update failed, Contact support team.");
          } else {
            addActionError("Approval Failed for " + this.fwdContractVO.getFwdContractNo() + ", Contact support team.");
          }
        }
        else if (category.equalsIgnoreCase("FWCCANCEL"))
        {
          this.fwdContractVO = fwdContractBD.cancelBookingDetails(this.fwdContractVO, "FWCCANCEL");
          if (this.fwdContractVO.getCount() == 1) {
            addActionMessage("Forward Contract Cancellation Approved successfully for " +
              this.fwdContractVO.getFwdContractNo());
          } else if (this.fwdContractVO.getCount() == 2) {
            addActionError("Approval Failed for " + this.fwdContractVO.getFwdContractNo() + " due to Posting Failure.");
          } else if (this.fwdContractVO.getCount() == 3) {
            addActionError("Approval Failed for " + this.fwdContractVO.getFwdContractNo() + " due to TREAS API Failure.");
          } else if (this.fwdContractVO.getCount() == 4) {
            addActionError("Approval Failed for " + this.fwdContractVO.getFwdContractNo() + " due to FTRT API Failure.");
          } else if (this.fwdContractVO.getCount() == 5) {
            addActionError("Approval Failed for " + this.fwdContractVO.getFwdContractNo() + " due to insufficient balance.");
          } else if (this.fwdContractVO.getCount() == 6) {
            addActionError("FWC number " + this.fwdContractVO.getFwdContractNo() + " is not valid for cancellation as it is deleted");
          } else if (this.fwdContractVO.getCount() == 7) {
            addActionError("Approval Failed for  " + this.fwdContractVO.getFwdContractNo() + " as the custom treasury table update failed");
          } else {
            addActionError("Approval Failed for " + this.fwdContractVO.getFwdContractNo());
          }
        }
        this.fwdContractVO = new ForwardContractVO();
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      throwApplicationException(e);
    }
    return "success";
  }
 
  public String rejectFWC()
    throws ApplicationException
  {
    ForwardContractBD fwdContractBD = null;
    try
    {
      isSessionAvailable();
      if (this.fwdContractVO != null)
      {
        logger.info(" show rejectFWC Record Details");
        fwdContractBD = new ForwardContractBD();
       
        String category = this.fwdContractVO.getCategory();
        if (category.equalsIgnoreCase("FWCBOOK"))
        {
          this.fwdContractVO = fwdContractBD.rejectFWC(this.fwdContractVO, "FWCBOOK");
          if (this.fwdContractVO.getCount() > 0) {
            addActionMessage("Forward Contract Booking Rejected successfully for " +
              this.fwdContractVO.getFwdContractNo());
          } else {
            addActionError("Action Failed for " + this.fwdContractVO.getFwdContractNo());
          }
        }
        else if (category.equalsIgnoreCase("FWCCANCEL"))
        {
          this.fwdContractVO = fwdContractBD.rejectFWC(this.fwdContractVO, "FWCCANCEL");
          if (this.fwdContractVO.getCount() > 0) {
            addActionMessage("Forward Contract Cancellation Rejected successfully for " +
              this.fwdContractVO.getFwdContractNo());
          } else {
            addActionError("Action Failed for " + this.fwdContractVO.getFwdContractNo());
          }
        }
        this.fwdContractVO = new ForwardContractVO();
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      throwApplicationException(e);
    }
    return "success";
  }
 
  public String deleteFWC()
    throws ApplicationException
  {
    ForwardContractBD fwdContractBD = null;
    try
    {
      isSessionAvailable();
      if (this.fwdContractVO != null)
      {
        logger.info(" show deleteFWC Record Details");
        fwdContractBD = new ForwardContractBD();
       
        String category = this.fwdContractVO.getCategory();
        if (category.equalsIgnoreCase("FWCBOOK"))
        {
          this.fwdContractVO = fwdContractBD.deleteFWC(this.fwdContractVO, "FWCBOOK");
          if (this.fwdContractVO.getCount() > 0) {
            addActionMessage("Forward Contract Booking Deleted successfully for " +
              this.fwdContractVO.getFwdContractNo());
          } else {
            addActionError("Action Failed for " + this.fwdContractVO.getFwdContractNo());
          }
        }
        else if (category.equalsIgnoreCase("FWCCANCEL"))
        {
          this.fwdContractVO = fwdContractBD.deleteFWC(this.fwdContractVO, "FWCCANCEL");
          if (this.fwdContractVO.getCount() > 0) {
            addActionMessage("Forward Contract Cancellation Deleted successfully for " +
              this.fwdContractVO.getFwdContractNo());
          } else {
            addActionError("Action Failed for " + this.fwdContractVO.getFwdContractNo());
          }
        }
        this.fwdContractVO = new ForwardContractVO();
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      throwApplicationException(e);
    }
    return "success";
  }
 
  public String execute()
    throws Exception
  {
    logger.info("Entering Method");
    HttpServletRequest request = ServletActionContext.getRequest();
    ForwardContractBD bd = null;
    String userName = null;
    String userID = null;
    try
    {
      bd = new ForwardContractBD();
      HttpServletRequest uRequest = (HttpServletRequest)ActionContext.getContext()
        .get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
      userName = uRequest.getRemoteUser();
      logger.info(userName);
      userID = bd.getSessionUser(userName);
      HttpSession httpSession = request.getSession();
      logger.info("USERID------>" + userID);
      httpSession.setAttribute("USERID", userID);
    }
    catch (Exception exception)
    {
      throwApplicationException(exception);
    }
    logger.info("Exiting Method");
    return "success";
  }
 
  public String staticDetails()
    throws Exception
  {
    logger.info("Entering Method");
    this.staticDataVo = null;
    try
    {
      this.staticDataVo = new StaticDataVO();
    }
    catch (Exception exception)
    {
      throwApplicationException(exception);
    }
    logger.info("Exiting Method");
    return "success";
  }
 
  public String fetchCustomerStaticData()
    throws Exception
  {
    logger.info("Entering Method");
    ForwardContractBD bd = null;
    try
    {
      bd = new ForwardContractBD();
      this.staticDataList = new ArrayList();
      if (this.staticDataVo != null) {
        if ((CommonMethods.isNull(this.staticDataVo.getCustomerID())) &&
          (CommonMethods.isNull(this.staticDataVo.getCustomerName()))) {
          this.staticDataList = bd.getCustomerList(this.staticDataList);
        } else {
          this.staticDataList = bd.filterCustomer(this.staticDataVo, this.staticDataList);
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
 
  public String fetchAccountStaticData()
    throws Exception
  {
    logger.info("Entering Method");
   
    ForwardContractBD bd = null;
    try
    {
      bd = new ForwardContractBD();
      this.staticDataList = new ArrayList();
      if (this.staticDataVo != null)
      {
        this.staticDataVo.setCustomerID(this.fwdContractVO.getCustomerID());
        if ((CommonMethods.isNull(this.staticDataVo.getCustomerID())) &&
          (CommonMethods.isNull(this.staticDataVo.getAcctNumber()))) {
          this.staticDataList = bd.getAccountList(this.staticDataList);
        } else {
          this.staticDataList = bd.filterAccount(this.staticDataVo, this.staticDataList);
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
 
  public String fetchBranchStaticData()
    throws Exception
  {
    logger.info("Entering Method");
   
    ForwardContractBD bd = null;
    try
    {
      bd = new ForwardContractBD();
      this.staticDataList = new ArrayList();
      if (this.staticDataVo != null) {
        if (CommonMethods.isNull(this.staticDataVo.getBranchCode())) {
          this.staticDataList = bd.getBranchList(this.staticDataList);
        } else {
          this.staticDataList = bd.filterBranch(this.staticDataVo, this.staticDataList);
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
 
  public String fetchCurrencyStaticData()
    throws Exception
  {
    logger.info("Entering Method");
   
    ForwardContractBD bd = null;
    try
    {
      bd = new ForwardContractBD();
      this.staticDataList = new ArrayList();
      if (this.staticDataVo != null) {
        if (CommonMethods.isNull(this.staticDataVo.getCurrency())) {
          this.staticDataList = bd.getCurrencyList(this.staticDataList);
        } else {
          this.staticDataList = bd.filterCurrency(this.staticDataVo, this.staticDataList);
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
 
  public String fetchTreasuryDetails()
    throws Exception
  {
    logger.info("Entering Method");
   
    ForwardContractBD bd = null;
    try
    {
      bd = new ForwardContractBD();
      this.staticDataList = new ArrayList();
      if (this.staticDataVo != null)
      {
        this.staticDataVo.setCustomerID(this.fwdContractVO.getCustomerID());
        if ((CommonMethods.isNull(this.staticDataVo.getTreasuryRefNo())) &&
          (CommonMethods.isNull(this.staticDataVo.getCustomerID()))) {
          this.staticDataList = bd.getTreasuryList(this.staticDataList);
        } else {
          this.staticDataList = bd.filterTreasuryDetails(this.staticDataVo, this.staticDataList);
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
 
  public String fetchLimitDetails()
    throws Exception
  {
    logger.info("Entering Method");
   
    ForwardContractBD bd = null;
    try
    {
      bd = new ForwardContractBD();
      this.staticDataList = new ArrayList();
      if (this.fwdContractVO != null)
      {
        String customerID = this.fwdContractVO.getCustomerID().trim();
        if (CommonMethods.isValidString(customerID)) {
          this.staticDataList = bd.getLimitList(customerID);
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
 
  public String closeWindow()
    throws Exception
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement log = null;
    ResultSet rs = null;
    String closeUrl = "";
    try
    {
      con = DBConnectionUtility.getZoneConnection();
     
      String query = "SELECT TRIM(VALUE1) AS CLOSEURL FROM ETT_PARAMETER_TBL WHERE PARAMETER_ID = 'closeURL'";
      log = new LoggableStatement(con, query);
      rs = log.executeQuery();
      if (rs.next()) {
        closeUrl = rs.getString("CLOSEURL");
      }
      HttpServletResponse response = (HttpServletResponse)ActionContext.getContext()
        .get("com.opensymphony.xwork2.dispatcher.HttpServletResponse");
      HttpServletRequest request = (HttpServletRequest)ActionContext.getContext().get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
     
      logger.info("ForwardContract closeWindow ... cookie clear before Redirect...");
     
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
      response.sendRedirect(closeUrl);
    }
    catch (Exception exception)
    {
      throwApplicationException(exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, log, rs);
    }
    logger.info("Exiting Method");
    return "none";
  }
 
  public String insertBookingDetails()
    throws Exception
  {
    ForwardContractBD bd = null;
    if (this.fwdContractVO != null) {
      bd = new ForwardContractBD();
    }
    String result = "success";
    try
    {
      String screen = this.fwdContractVO.getScreenType();
      if (screen.equalsIgnoreCase("MakerBookingScreen"))
      {
        if (CommonMethods.isValidString(this.fwdContractVO.getFwdContractNo()))
        {
          logger.info("update BookingDetails");
         

          this.fwdContractVO = bd.updateFwdBookingContractDetails(this.fwdContractVO, "FWCBOOK",
            "PENDING FOR APPROVAL", "Modified");
          if (this.fwdContractVO.getCount() > 0) {
            addActionMessage("Forward Contract Booking updated successfully for " +
              this.fwdContractVO.getFwdContractNo());
          } else {
            addActionError("Forward Contract Booking failed for " + this.fwdContractVO.getFwdContractNo());
          }
        }
        else
        {
          logger.info("insert BookingDetails");
          this.fwdContractVO = bd.insertBookingDetails(this.fwdContractVO, "FWCBOOK",
            "PENDING FOR APPROVAL", "Booked");
          if (this.fwdContractVO.getCount() > 0) {
            addActionMessage("Forward Contract Booking is success with Reference Number " +
              this.fwdContractVO.getFwdContractNo());
          } else {
            addActionError("Forward Contract Booking failed");
          }
        }
      }
      else if (screen.equalsIgnoreCase("MakerCancelScreen"))
      {
        logger.info("Screentype:" + screen);
       
        int count = bd.getRecordCountFromDB(this.fwdContractVO, "FWCCANCEL");
        if ((CommonMethods.isValidString(this.fwdContractVO.getFwdContractNo())) && (count == 1))
        {
          this.fwdContractVO = bd.updateFwdCancelContractDetails(this.fwdContractVO, "FWCCANCEL",
            "PENDING FOR APPROVAL", "Modified");
          if (this.fwdContractVO.getCount() > 0) {
            addActionMessage("Forward Contract Cancellation updated successfully for " +
              this.fwdContractVO.getFwdContractNo());
          } else {
            addActionError("Forward Contract Cancellation updation failed for " +
              this.fwdContractVO.getFwdContractNo());
          }
        }
        else if ((CommonMethods.isValidString(this.fwdContractVO.getFwdContractNo())) && (count == 0))
        {
          this.fwdContractVO = bd.insertCancelDetails(this.fwdContractVO, "FWCCANCEL",
            "PENDING FOR APPROVAL", "Cancelled");
          if (this.fwdContractVO.getCount() > 0) {
            addActionMessage(
              "Forward Contract Cancellation is success for " + this.fwdContractVO.getFwdContractNo());
          } else {
            addActionError("Forward Contract Cancellation failed for " + this.fwdContractVO.getFwdContractNo());
          }
        }
        else
        {
          addActionError("Forward Contract Cancellation failed for " + this.fwdContractVO.getFwdContractNo());
        }
      }
      if (this.fwdContractVO.getCount() > 0)
      {
        this.fwdContractVO = new ForwardContractVO();
        result = "success";
      }
      else if (this.fwdContractVO.getErrorList().size() > 0)
      {
        if (screen.equalsIgnoreCase("MakerBookingScreen")) {
          result = "book";
        } else if (screen.equalsIgnoreCase("MakerCancelScreen")) {
          result = "cancel";
        }
      }
      else
      {
        this.fwdContractVO = new ForwardContractVO();
        result = "success";
      }
    }
    catch (Exception e)
    {
      logger.info("Exception in action" + e.getMessage());
    }
    return result;
  }
 
  public String saveBookingDetails()
    throws Exception
  {
    String result = "success";
    ForwardContractBD bd = null;
    if (this.fwdContractVO != null) {
      bd = new ForwardContractBD();
    }
    try
    {
      String screen = this.fwdContractVO.getScreenType();
      this.fwdContractVO = bd.saveBookingDetails(this.fwdContractVO);
      if (this.fwdContractVO.getCount() > 0)
      {
        addActionMessage(
          "Forward Contract is Saved with Reference Number " + this.fwdContractVO.getFwdContractNo());
        this.fwdContractVO = new ForwardContractVO();
        result = "success";
      }
      else if (this.fwdContractVO.getErrorList().size() > 0)
      {
        if (screen.equalsIgnoreCase("MakerBookingScreen")) {
          result = "book";
        } else if (screen.equalsIgnoreCase("MakerCancelScreen")) {
          result = "cancel";
        }
      }
      else
      {
        addActionMessage("Forward Contract failed to save");
        this.fwdContractVO = new ForwardContractVO();
        result = "success";
      }
    }
    catch (Exception e)
    {
      logger.info("Exception in action" + e.getMessage());
    }
    return result;
  }
 
  public String validateBookingDetails()
    throws Exception
  {
    ForwardContractBD bd = null;
    String result = "success";
    int result1 = 0;
    if (this.fwdContractVO != null) {
      bd = new ForwardContractBD();
    }
    try
    {
      String screen = this.fwdContractVO.getScreenType();
      this.fwdContractVO = bd.validateBookingDetails(this.fwdContractVO);
      if (screen.equalsIgnoreCase("MakerBookingScreen"))
      {
        result = "book";
        if ((CommonMethods.isValidString(this.fwdContractVO.getCustomerID())) &&
          (CommonMethods.isValidString(this.fwdContractVO.getSubProduct())) &&
          (CommonMethods.isValidString(this.fwdContractVO.getBranchCode())) &&
          (CommonMethods.isValidString(this.fwdContractVO.getToCurrencyAmt()))) {
          this.fwdContractVO = bd.generateFWCPostings(this.fwdContractVO);
        }
      }
      else if (screen.equalsIgnoreCase("MakerCancelScreen"))
      {
        result = "cancel";
        if ((CommonMethods.isValidString(this.fwdContractVO.getCustomerID())) &&
          (CommonMethods.isValidString(this.fwdContractVO.getSubProduct())) &&
          (CommonMethods.isValidString(this.fwdContractVO.getBranchCode())) &&
          (CommonMethods.isValidString(this.fwdContractVO.getToCurrencyAmt()))) {
          bd.getFWCPostingsToReverse(this.fwdContractVO);
        }
        String sessionUserName = isSessionAvailable1();
        this.fwdContractVO.setSessionUserName(sessionUserName);
      }
    }
    catch (Exception e)
    {
      logger.info("Exception in action" + e.getMessage());
    }
    return result;
  }
 
  public String generateFWCPostings()
    throws Exception
  {
    ForwardContractBD bd = null;
    if (this.fwdContractVO != null) {
      bd = new ForwardContractBD();
    }
    try
    {
      this.fwdContractVO = bd.generateFWCPostings(this.fwdContractVO);
    }
    catch (Exception e)
    {
      logger.info("Exception in action" + e.getMessage());
    }
    return "success";
  }
 
  public String getFWCPostingsToReverse()
    throws Exception
  {
    ForwardContractBD bd = null;
    if (this.fwdContractVO != null) {
      bd = new ForwardContractBD();
    }
    try
    {
      this.fwdContractVO = bd.getFWCPostingsToReverse(this.fwdContractVO);
    }
    catch (Exception e)
    {
      logger.info("Exception in action" + e.getMessage());
    }
    return "success";
  }
 
  public String fetchFwdContractDetails()
    throws Exception
  {
    ForwardContractBD bd = null;
    if (this.fwdContractVO != null) {
      bd = new ForwardContractBD();
    }
    try
    {
      this.forwardContractList = bd.fetchFwdContractDetails(this.fwdContractVO);
      setForwardContractList(this.forwardContractList);
    }
    catch (Exception e)
    {
      logger.info("Exception in action" + e.getMessage());
    }
    return "success";
  }
 
  public String fetchFwdContractEnquiryDetails()
    throws Exception
  {
    ForwardContractBD bd = null;
    if (this.fwdContractVO != null) {
      bd = new ForwardContractBD();
    }
    try
    {
      this.forwardContractList = bd.fetchFwdContractEnquiryDetails(this.fwdContractVO);
      setForwardContractList(this.forwardContractList);
    }
    catch (Exception e)
    {
      logger.info("Exception in action" + e.getMessage());
    }
    return "success";
  }
 
  public String fetchFwdContractDetailsToCancel()
    throws Exception
  {
    logger.info("Entering Method");
   
    ForwardContractBD bd = null;
    try
    {
      bd = new ForwardContractBD();
      this.staticDataList = new ArrayList();
      if (this.staticDataVo != null)
      {
        this.staticDataVo.setCustomerID(this.fwdContractVO.getCustomerID());
        if ((CommonMethods.isNull(this.staticDataVo.getFwdContractNo())) &&
          (CommonMethods.isNull(this.staticDataVo.getCustomerID()))) {
          this.staticDataList = bd.fetchFwdContractList(this.staticDataList);
        } else {
          this.staticDataList = bd.filterfwdContractDetails(this.staticDataVo, this.staticDataList);
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
 
  public String resetval()
  {
    String result = "success";
    try
    {
      if (this.fwdContractVO.getScreenType().equalsIgnoreCase("MakerBookingScreen")) {
        result = "book";
      } else if (this.fwdContractVO.getScreenType().equalsIgnoreCase("MakerCancelScreen")) {
        result = "cancel";
      }
      this.fwdContractVO = new ForwardContractVO();
    }
    catch (Exception e)
    {
      logger.info("Exception in resetval " + e.getMessage());
    }
    return result;
  }
}

