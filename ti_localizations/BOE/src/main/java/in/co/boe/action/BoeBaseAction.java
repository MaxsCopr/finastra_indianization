package in.co.boe.action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import in.co.boe.businessdelegate.boehome.BoeHomeBD;
import in.co.boe.dao.exception.ApplicationException;
import in.co.boe.utility.Checkking_Valid_User;
import in.co.boe.utility.CommonMethods;
import in.co.boe.utility.DBConnectionUtility;
import in.co.boe.utility.LogHelper;
import in.co.boe.utility.LoggableStatement;
import in.co.boe.utility.UserDetailsVO;
import java.sql.Connection;
import java.sql.ResultSet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
 
public class BoeBaseAction
  extends ActionSupport
{
  private static final long serialVersionUID = 1L;
  private static Logger logger = Logger.getLogger(BoeBaseAction.class.getName());
  public boolean isSessionAvailable()
    throws ApplicationException
  {
    logger.info("--------------isSessionAvailable---------------");
    BoeHomeBD boeHomeBD = null;
    UserDetailsVO userVO = null;
    boolean isAvail = false;
    boolean UserStatus = false;
    String userName = null;
    Checkking_Valid_User usr = new Checkking_Valid_User();
    try
    {
      HttpSession session = ServletActionContext.getRequest().getSession();
      HttpServletRequest request = (HttpServletRequest)ActionContext.getContext().get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
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
      logger.info("Final Setting Session username--------" + userName);
      if (userName != null)
      {
        boeHomeBD = new BoeHomeBD();
        userVO = new UserDetailsVO();
        userVO.setSessionUserName(userName);
        boeHomeBD.setDate();

 
 
        String tiLoginType = CommonMethods.checkForNullvalue(request.getParameter("loginType"));
        String dirAmount = CommonMethods.checkForNullvalue(request.getParameter("dirAmount")).replace(",", "");

 
 
        String sMstRefNum = CommonMethods.checkForNullvalue(request.getParameter("mstRefNumber"));
        String sEvtRefNum = CommonMethods.checkForNullvalue(request.getParameter("evtRefNumber"));
        String valid_maker_from_ti = CommonMethods.checkForNullvalue(request.getParameter("maker"));
        session.setAttribute("valid_maker", valid_maker_from_ti);
        logger.info("Valid Maker----------->" + valid_maker_from_ti);

 
        session.setAttribute("loginedUserName", userName);

 
        logger.info("loginedUserName-----AFTER SETTING session value-------------" + (String)session.getAttribute("loginedUserName"));

 
        logger.info("tiLoginType is : 1 : " + tiLoginType);
        logger.info("sMstRefNum is : 1 : " + sMstRefNum);
        logger.info("sEvtRefNum is : 1 :" + sEvtRefNum);
        logger.info("BOE DIR Amount Check2----->" + dirAmount);
        if ((sMstRefNum != null) || (sEvtRefNum != null))
        {
          session.setAttribute("loginType", tiLoginType);
          session.setAttribute("loginType", tiLoginType);
          logger.info("This is Inside of Session User Name : 1 : " + session.getAttribute("loginType"));
          session.setAttribute("dirAmount", dirAmount);
          session.setAttribute("xMstRefNum", sMstRefNum);
          session.setAttribute("xEvtRefNum", sEvtRefNum);
        }
      }
    }
    catch (Exception exception)
    {
      logger.info("isSessionAvailable-----------------exception" + exception);
      throwApplicationException(exception);
    }
    logger.info("Exiting Method");
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
