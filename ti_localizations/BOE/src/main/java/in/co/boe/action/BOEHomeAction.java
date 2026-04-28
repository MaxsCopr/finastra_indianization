package in.co.boe.action;

import com.opensymphony.xwork2.ActionContext;

import in.co.boe.businessdelegate.boehome.BoeHomeBD;

import in.co.boe.dao.exception.ApplicationException;

import in.co.boe.dao.tiReference_from_ti;

import in.co.boe.utility.CommonMethods;

import in.co.boe.utility.DBConnectionUtility;

import in.co.boe.utility.LoggableStatement;

import in.co.boe.utility.UserDetailsVO;

import java.sql.Connection;

import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import org.apache.struts2.ServletActionContext;
 
public class BOEHomeAction extends BoeBaseAction

{

  private static final long serialVersionUID = 1L;

  private static Logger logger = Logger.getLogger(BOEHomeAction.class

    .getName());

  String sMstRefNums = null;

  String sEvtRefNums = null;

  public String getsMstRefNums()

  {

    return this.sMstRefNums;

  }

  public void setsMstRefNums(String sMstRefNums)

  {

    this.sMstRefNums = sMstRefNums;

  }

  public String getsEvtRefNums()

  {

    return this.sEvtRefNums;

  }

  public void setsEvtRefNums(String sEvtRefNums)

  {

    this.sEvtRefNums = sEvtRefNums;

  }

  public String landingPage()

    throws ApplicationException

  {

    logger.info("Entering Method");

    try

    {

      tiReference_from_ti tiVo = null;

      isSessionAvailable();

      HttpServletRequest request = (HttpServletRequest)ActionContext.getContext()

        .get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");

      tiVo = new tiReference_from_ti();

      this.sMstRefNums = CommonMethods.checkForNullvalue(request.getParameter("mstRefNumber"));

      this.sEvtRefNums = CommonMethods.checkForNullvalue(request.getParameter("evtRefNumber"));

 
 
      tiVo.setEvent_ref(this.sEvtRefNums);

      tiVo.setMaster_ref(this.sMstRefNums);

    }

    catch (Exception exception)

    {

      throwApplicationException(exception);

    }

    logger.info("Exiting Method");

    return "success";

  }

  public String makerProcess()

    throws ApplicationException

  {

    logger.info("Entering Method");

    UserDetailsVO userVO = null;

    String sessionUserName = null;

    BoeHomeBD boeHomeBD = null;

    String target = null;

    try

    {

      logger.info("Maker Screen----->");

      logger.info("Maker Screen----->");

      userVO = new UserDetailsVO();

      isSessionAvailable();

      logger.info("Maker Screen--1111111--->");

      HttpSession session = ServletActionContext.getRequest().getSession();

      sessionUserName = (String)session.getAttribute("loginedUserName");

      logger.info("Maker sessionUserName  : " + sessionUserName);

      HttpServletRequest request = (HttpServletRequest)ActionContext.getContext()

        .get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");

 
 
      String sMstRefNum = CommonMethods.checkForNullvalue(request.getParameter("mstRefNumber"));

      String sEvtRefNum = CommonMethods.checkForNullvalue(request.getParameter("evtRefNumber"));

      session.setAttribute("xMstRefNum", sMstRefNum);

      session.setAttribute("xEvtRefNum", sEvtRefNum);

      this.sMstRefNums = CommonMethods.checkForNullvalue(request.getParameter("mstRefNumber"));

      this.sEvtRefNums = CommonMethods.checkForNullvalue(request.getParameter("evtRefNumber"));

      logger.info("Transaction Reference number setted----xMstRefNum----------------->" + this.sMstRefNums);

      logger.info("Event  Reference number setted-------xEvtRefNum-------------->" + this.sMstRefNums);

      if (sessionUserName != null)

      {

        logger.info("Inside outer session name");

        boeHomeBD = new BoeHomeBD();

        logger.info("sessionUserName--->" + sessionUserName);

        userVO.setSessionUserName(sessionUserName);

        userVO.setPageType("BOEMaker");

        int count = boeHomeBD.checkLoginedUserType(userVO);

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

    catch (Exception exception)

    {

      throwApplicationException(exception);

      logger.info("Exception in makerProcess  makerProcess" + exception.getMessage());

    }

    logger.info("Exiting Method");

    return target;

  }

  public String checkerProcess()

    throws ApplicationException

  {

    logger.info("Entering Method");

    UserDetailsVO userVO = null;

    String sessionUserName = null;

    BoeHomeBD boeHomeBD = null;

    String target = null;

    try

    {

      logger.info("Checker Screen----->");

      userVO = new UserDetailsVO();

      isSessionAvailable();

      HttpSession session = ServletActionContext.getRequest().getSession();

      sessionUserName = (String)session.getAttribute("loginedUserName");

      logger.info("Checker sessionUserName  : " + sessionUserName);

      if (sessionUserName != null)

      {

        boeHomeBD = new BoeHomeBD();

        logger.info("sessionUserName--->" + sessionUserName);

        userVO.setSessionUserName(sessionUserName);

        userVO.setPageType("BOEChecker");

        int count = boeHomeBD.checkLoginedUserType(userVO);

        if (count > 0) {

          target = "success";

        } else {

          target = "fail";

        }

      }

    }

    catch (Exception exception)

    {

      logger.info("Checker Screen-----exception>---------------" + exception);

      throwApplicationException(exception);

    }

    logger.info("Exiting Method");

    return target;

  }

  public String closeBOEWindow()

    throws ApplicationException

  {

    logger.info("Entering Method");

    try

    {

      logger.info("BOE Close Window");

    }

    catch (Exception exception)

    {

      logger.info("closeBOEWindow--------------Exception-------------" + exception);

      throwApplicationException(exception);

    }

    logger.info("Exiting Method");

    return "success";

  }

  public String closeWindow()

    throws Exception

  {

    logger.info("-----------closeWindow------------------");

    Connection con = null;

    LoggableStatement log = null;

    ResultSet rs = null;

    String closeUrl = "";

    try

    {

      con = DBConnectionUtility.getConnection();

 
      HttpSession session = ServletActionContext.getRequest().getSession();

      String userName = (String)session.getAttribute("loginedUserName");

 
      logger.info("-----------userName-------------" + userName);

 
      String query = "SELECT VALUE1 FROM ETT_PARAMETER_TBL WHERE PARAMETER_ID = 'boeCloseURL' ";

      log = new LoggableStatement(con, query);

      rs = log.executeQuery();

      logger.info("-----------closeWindow---------query---------" + log.getQueryString());

      if (rs.next())

      {

        closeUrl = rs.getString("VALUE1");

        logger.info("-----------closeWindow---------closeUrl---------" + closeUrl);

      }

      closeUrl = closeUrl + "?u_id=" + userName;

 
      logger.info("------closeUrl---appended Value-userName-------------" + closeUrl);

 
      HttpServletResponse response = (HttpServletResponse)

        ActionContext.getContext().get("com.opensymphony.xwork2.dispatcher.HttpServletResponse");

      response.sendRedirect(closeUrl);

    }

    catch (Exception exception)

    {

      logger.info("-----------closeWindow--------------exception----" + exception);

      throwApplicationException(exception);

    }

    finally

    {

      DBConnectionUtility.surrenderDB(con, log, rs);

    }

    logger.info("Exiting Method");

    return "none";

  }

}

 