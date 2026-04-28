package in.co.chargeSchedule.action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import in.co.chargeSchedule.businessdelegate.ChargeScheduleBD;
import in.co.chargeSchedule.dao.exception.ApplicationException;
import in.co.chargeSchedule.utility.ActionConstants;
import in.co.chargeSchedule.utility.DBConnectionUtility;
import in.co.chargeSchedule.utility.LogHelper;
import in.co.chargeSchedule.utility.LoggableStatement;
import in.co.chargeSchedule.vo.ChargeScheduleVO;
import java.sql.Connection;
import java.sql.ResultSet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
 
public class ChargeScheduleBaseAction
  extends ActionSupport
  implements ActionConstants
{
  private static final long serialVersionUID = 1L;
  private static Logger logger = Logger.getLogger(ChargeScheduleBaseAction.class
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
    ChargeScheduleBD chargBD = null;
    ChargeScheduleVO chargVO = null;
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
          them_con = DBConnectionUtility.getWiseConnection();

 
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
        chargBD = new ChargeScheduleBD();
        chargVO = new ChargeScheduleVO();
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
}
