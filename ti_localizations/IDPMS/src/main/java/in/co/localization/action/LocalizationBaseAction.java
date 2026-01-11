package in.co.localization.action;
 
import com.opensymphony.xwork2.ActionContext;

import com.opensymphony.xwork2.ActionSupport;

import in.co.localization.businessdelegate.localization.AdTransferBD;

import in.co.localization.dao.exception.ApplicationException;

import in.co.localization.utility.ActionConstants;

import in.co.localization.utility.Checkking_Valid_User;

import in.co.localization.utility.DBConnectionUtility;

import in.co.localization.utility.LogHelper;

import in.co.localization.utility.LoggableStatement;

import java.sql.Connection;

import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import org.apache.struts2.ServletActionContext;
 
public class LocalizationBaseAction

  extends ActionSupport

  implements ActionConstants

{

  private static final long serialVersionUID = 1L;

  private static Logger logger = Logger.getLogger(LocalizationBaseAction.class

    .getName());

  public boolean isSessionAvailable()

    throws ApplicationException

  {

    logger.info("-------isSessionAvailable-------");

    Checkking_Valid_User usr = new Checkking_Valid_User();

    String sessionUserName = null;

    String userId = null;

    AdTransferBD bd = null;

    boolean isAvail = false;

    String userName = null;

    HttpSession session = ServletActionContext.getRequest().getSession();

    HttpServletRequest request = (HttpServletRequest)ActionContext.getContext().get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");

    try

    {

      userName = request.getParameter("u_id");

 
 
      logger.info("------------Para meter userName  values-------" + userName);

      logger.info("------------Para meter u_id values-------" + request.getParameter("u_id"));

      if (userName == null)

      {

        userName = (String)session.getAttribute("loginedUserName");

        if (!usr.isValidTIUser(userName))

        {

          logger.info("----------False Connection-----1111111111--");

          logger.info("------------Valid User Status-------" + usr.isValidTIUser(userName));

          userName = null;

        }

        logger.info("loginedUserName----IDPMS--------------" + userName);

      }

      if (userName == null)

      {

        userName = request.getRemoteUser();

        logger.info("getRemoteUser[-------userName-----------" + request.getRemoteUser());

 
        logger.info("------------Valid User Status-------" + usr.isValidTIUser(userName));

        if (!usr.isValidTIUser(userName))

        {

          logger.info("------------Valid User Status-------" + usr.isValidTIUser(userName));

          userName = null;

        }

        if (userName == null)

        {

          Connection them_con = null;

          them_con = DBConnectionUtility.getWiseConnection();

 
          userName = request.getRequestedSessionId();

          String get_User_ID = "SELECT SCT.USERNAME AS USER_ID FROM CENTRAL_SESSION_DETAILS SCT,LOCAL_SESSION_DETAILS LOC  WHERE SCT.CENTRAL_ID=LOC.CENTRAL_ID AND SCT.ENDED  IS NULL AND LOC.LOCAL_ID= ? ";

 
 
          LoggableStatement lst = new LoggableStatement(them_con, get_User_ID);

          lst.setString(1, userName);

          logger.info("Getting Session Value  Globa Query------------" + lst.getQueryString());

          logger.info("Getting Session Global userName value----------" + userName);

 
          ResultSet rst = lst.executeQuery();

          while (rst.next())

          {

            userName = rst.getString("USER_ID");

            logger.info("Getting Session Value GLOBAL  Query-- user id value----------" + userName);

          }

          DBConnectionUtility.surrenderDB(them_con, lst, rst);

          logger.info("After Global Value Session ---------" + userName);

        }

      }

      if (userName != null)

      {

        isAvail = true;

        bd = new AdTransferBD();

        bd.getUserId(userName);

        session.setAttribute("loginedUserName", userName);

        session.setAttribute("loginedUserId", userName);

        session.setAttribute("loginedUserId", userName);

        bd.setDate();

        return isAvail;

      }

    }

    catch (Exception exception)

    {

      logger.info("Session Exeption--------" + exception);

      throwApplicationException(exception);

      logger.info("Exiting Method");

    }

    return isAvail;

  }

  public void throwApplicationException(Exception exception)

    throws ApplicationException

  {

    logger.error(exception.fillInStackTrace());

    LogHelper.logError(logger, exception);

    throw new ApplicationException(exception.getMessage(), exception);

  }

}

 