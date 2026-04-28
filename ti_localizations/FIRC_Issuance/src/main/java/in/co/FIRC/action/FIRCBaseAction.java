package in.co.FIRC.action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import in.co.FIRC.businessdelegate.FIRCHomeBD;
import in.co.FIRC.dao.exception.ApplicationException;
import in.co.FIRC.dao.utility.DBConnectionUtility;
import in.co.FIRC.utility.Checkking_Valid_User;
import in.co.FIRC.utility.LogHelper;
import in.co.FIRC.utility.LoggableStatement;
import in.co.FIRC.vo.UserDetailsVO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;
 
public class FIRCBaseAction
  extends ActionSupport
{
  private static final long serialVersionUID = 1L;
  private static Logger logger = LogManager.getLogger(FIRCBaseAction.class.getName());
  public ArrayList<String> addOnScripts = new ArrayList();
  public boolean isSessionAvailable()
    throws ApplicationException
  {
    logger.info("Entering Method");
    Checkking_Valid_User usr = new Checkking_Valid_User();
    FIRCHomeBD bd = null;
    UserDetailsVO userVO = null;
    boolean isAvail = false;
    String userName = null;
    try
    {
      HttpSession session = ServletActionContext.getRequest().getSession();
      HttpServletRequest request = (HttpServletRequest)ActionContext.getContext().get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");

 
 
 
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
      logger.info("Final Setting Session username--------" + userName);
      if (userName != null)
      {
        session.setAttribute("loginedUserName", userName);

 
        bd = new FIRCHomeBD();
        userVO = new UserDetailsVO();
        userVO.setSessionUserName(userName);
        userVO = bd.fetchLoginedUserId(userVO);

 
 
        bd.setDate();
      }
      return isAvail;
    }
    catch (Exception exception)
    {
      logger.info("isSessionAvailable-----------exception--------" + exception);
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
  public ArrayList<String> getAddOnScripts()
  {
    return this.addOnScripts;
  }
  public void setAddOnScripts(ArrayList<String> addOnScripts)
  {
    this.addOnScripts = addOnScripts;
  }
  public void setServletResponse(HttpServletResponse response) {}
}
