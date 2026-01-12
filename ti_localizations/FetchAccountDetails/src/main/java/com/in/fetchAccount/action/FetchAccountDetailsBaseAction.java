package com.in.fetchAccount.action;
 
import com.in.fetchAccount.dao.exception.ApplicationException;

import com.in.fetchAccount.utility.ActionConstants;

import com.in.fetchAccount.utility.DBConnectionUtility;

import com.in.fetchAccount.utility.LogHelper;

import com.in.fetchAccount.utility.LoggableStatement;

import com.in.fetchAccount.utility.ValidationsUtil;

import com.opensymphony.xwork2.ActionContext;

import com.opensymphony.xwork2.ActionSupport;

import java.sql.Connection;

import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import org.apache.struts2.ServletActionContext;
 
public class FetchAccountDetailsBaseAction

  extends ActionSupport

  implements ActionConstants

{

  private static final long serialVersionUID = 1L;

  private static Logger logger = Logger.getLogger(FetchAccountDetailsBaseAction.class.getName());

  public void throwApplicationException(Exception exception)

    throws ApplicationException

  {

    logger.error(exception.fillInStackTrace());

    LogHelper.logError(logger, exception);

    throw new ApplicationException(exception.getMessage(), exception);

  }

  public String execute()

    throws Exception

  {

    return super.execute();

  }

  public void isSessionAvailable()

    throws ApplicationException

  {

    logger.info("FetchAccountDetailsBaseAction : isSessionAvailable : Started");

    String sessionUserName = null;

    String userName = null;

    String loginedUserId = null;

    try

    {

      HttpSession session = ServletActionContext.getRequest().getSession();

      HttpServletRequest request = (HttpServletRequest)ActionContext.getContext().get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");

      sessionUserName = (String)session.getAttribute("loginedUserName");

      logger.info("FetchAccountDetailsBaseAction : isSessionAvailable : loginedUserName :------------------" + sessionUserName);

      if (sessionUserName == null)

      {

        sessionUserName = request.getRemoteUser();

        logger.info("FetchAccountDetailsBaseAction : isSessionAvailable : getRemoteUser :------------------" + sessionUserName);

        if (sessionUserName == null)

        {

          sessionUserName = request.getRequestedSessionId();

          String userId = getUserIdBasedOnSession(sessionUserName);

          session.setAttribute("loginedUserName", userId);

          session.setAttribute("loginedUserId", userId);

        }

      }

      if (sessionUserName != null)

      {

        session.setAttribute("loginedUserName", sessionUserName);

        session.setAttribute("loginedUserId", sessionUserName);

      }

    }

    catch (Exception e)

    {

      logger.info("FetchAccountDetailsBaseAction : isSessionAvailable : Exception :" + e.getMessage());

      throwApplicationException(e);

    }

    logger.info("FetchAccountDetailsBaseAction : isSessionAvailable : Ended");

  }

  public String getUserIdBasedOnSession(String sessionUserName)

  {

    logger.info("FetchAccountDetailsBaseAction : getUserIdBasedOnSession : Started");

    Connection wise_con = null;

    LoggableStatement lst = null;

    ResultSet rst = null;

    String userId = "";

    try

    {

    	wise_con = DBConnectionUtility.getGlobalConnection();

      String get_User_ID = "SELECT SCT.USERNAME AS USER_ID FROM CENTRAL_SESSION_DETAILS SCT,LOCAL_SESSION_DETAILS LOC  WHERE SCT.CENTRAL_ID=LOC.CENTRAL_ID AND SCT.ENDED  IS NULL AND LOC.LOCAL_ID= ? ";

 
 
      lst = new LoggableStatement(wise_con, get_User_ID);

      lst.setString(1, sessionUserName);

      logger.info("FetchAccountDetailsBaseAction : getUserIdBasedOnSession :  Getting Session Value Query------------" + lst.getQueryString());

      rst = lst.executeQuery();

      while (rst.next()) {

        userId = ValidationsUtil.isValidString(rst.getString("USER_ID")) ? rst.getString("USER_ID").trim() : "";

      }

    }

    catch (Exception e)

    {

      logger.info("FetchAccountDetailsBaseAction : getUserIdBasedOnSession : Exception :" + e.getMessage());

      logger.error(e);

    }

    finally

    {

      DBConnectionUtility.surrenderDB(wise_con, lst, rst);

    }

    logger.info("FetchAccountDetailsBaseAction : getUserIdBasedOnSession : userId :" + userId);

    return userId;

  }

}