package in.co.chargeSchedule.action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import in.co.forwardcontract.dao.exception.ApplicationException;
import in.co.forwardcontract.utility.ActionConstants;
import in.co.forwardcontract.utility.DBConnectionUtility;
import in.co.forwardcontract.utility.LogHelper;
import in.co.forwardcontract.utility.LoggableStatement;
import in.co.forwardcontract.vo.ForwardContractVO;
import java.sql.Connection;
import java.sql.ResultSet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
 
public class ForwardContractBaseAction
  extends ActionSupport
  implements ActionConstants
{
  private static final long serialVersionUID = 1L;
  private static Logger logger = Logger.getLogger(ForwardContractBaseAction.class
    .getName());
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
  public boolean isSessionAvailable()
    throws ApplicationException
  {
    logger.info("Entering Method");
    String sessionUserName = null;
    ForwardContractVO chargVO = null;
    boolean isAvail = false;
    String userName = null;
    String loginedUserId = null;
    try
    {
      HttpSession session = ServletActionContext.getRequest().getSession();
      HttpServletRequest request = (HttpServletRequest)ActionContext.getContext().get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
      sessionUserName = (String)session.getAttribute("loginedUserName");
      logger.info("loginedUserName------------------" + sessionUserName);
      if (sessionUserName == null)
      {
        sessionUserName = request.getRemoteUser();
        logger.info("getRemoteUser[------------------" + sessionUserName);
        if (sessionUserName == null)
        {
          Connection them_con = null;
          them_con = DBConnectionUtility.getGlobalConnection();

 
          sessionUserName = request.getRequestedSessionId();
          String get_User_ID = "SELECT SCT.USERNAME AS USER_ID FROM CENTRAL_SESSION_DETAILS SCT,LOCAL_SESSION_DETAILS LOC  WHERE SCT.CENTRAL_ID=LOC.CENTRAL_ID AND SCT.ENDED  IS NULL AND LOC.LOCAL_ID= ? ";

 
 
          LoggableStatement lst = new LoggableStatement(them_con, get_User_ID);
          lst.setString(1, sessionUserName);
          logger.info("Getting Session Value Query------------" + lst.getQueryString());

 
          ResultSet rst = lst.executeQuery();
          while (rst.next())
          {
            sessionUserName = rst.getString("USER_ID");
            logger.info("Getting Session Value Query-- user id value----------" + sessionUserName);
          }
          session.setAttribute("loginedUserName", userName);
          session.setAttribute("loginedUserId", userName);
          DBConnectionUtility.surrenderDB(them_con, lst, rst);
          logger.info("userName-----------" + userName);
        }
      }
      if (sessionUserName != null)
      {
        chargVO = new ForwardContractVO();
        chargVO.setSessionUserName(sessionUserName);
        loginedUserId = String.valueOf(chargVO.getUserid());
        session.setAttribute("loginedUserName", sessionUserName);
        session.setAttribute("loginedUserId", loginedUserId);
      }
      return isAvail;
    }
    catch (Exception exception)
    {
      throwApplicationException(exception);
      logger.info("Exiting Method");
    }
    return isAvail;
  }
  public String isSessionAvailable1()
    throws ApplicationException
  {
    logger.info("Entering Method");
    String sessionUserName = null;
    ForwardContractVO chargVO = null;
    String userName = null;
    String loginedUserId = null;
    try
    {
      HttpSession session = ServletActionContext.getRequest().getSession();
      HttpServletRequest request = (HttpServletRequest)ActionContext.getContext().get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
      sessionUserName = (String)session.getAttribute("loginedUserName");
      logger.info("loginedUserName------------------" + sessionUserName);
      if (sessionUserName == null)
      {
        sessionUserName = request.getRemoteUser();
        logger.info("getRemoteUser[------------------" + sessionUserName);
        if (sessionUserName == null)
        {
          Connection them_con = null;
          them_con = DBConnectionUtility.getGlobalConnection();

 
          sessionUserName = request.getRequestedSessionId();
          String get_User_ID = "SELECT SCT.USERNAME AS USER_ID FROM CENTRAL_SESSION_DETAILS SCT,LOCAL_SESSION_DETAILS LOC  WHERE SCT.CENTRAL_ID=LOC.CENTRAL_ID AND SCT.ENDED  IS NULL AND LOC.LOCAL_ID= ? ";

 
 
          LoggableStatement lst = new LoggableStatement(them_con, get_User_ID);
          lst.setString(1, sessionUserName);
          logger.info("Getting Session Value Query------------" + lst.getQueryString());

 
          ResultSet rst = lst.executeQuery();
          while (rst.next())
          {
            sessionUserName = rst.getString("USER_ID");
            logger.info("Getting Session Value Query-- user id value----------" + sessionUserName);
          }
          session.setAttribute("loginedUserName", userName);
          session.setAttribute("loginedUserId", userName);
          DBConnectionUtility.surrenderDB(them_con, lst, rst);
          logger.info("userName-----------" + userName);
        }
      }
      if (sessionUserName != null)
      {
        chargVO = new ForwardContractVO();
        chargVO.setSessionUserName(sessionUserName);
        loginedUserId = String.valueOf(chargVO.getUserid());
        session.setAttribute("loginedUserName", sessionUserName);
        session.setAttribute("loginedUserId", loginedUserId);
      }
      return sessionUserName;
    }
    catch (Exception exception)
    {
      throwApplicationException(exception);
      logger.info("Exiting Method");
    }
    return sessionUserName;
  }
}
