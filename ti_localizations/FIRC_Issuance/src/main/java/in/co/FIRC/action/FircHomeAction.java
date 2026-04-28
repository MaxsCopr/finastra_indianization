package in.co.FIRC.action;

import com.opensymphony.xwork2.ActionContext;
import in.co.FIRC.businessdelegate.FIRCHomeBD;
import in.co.FIRC.dao.exception.ApplicationException;
import in.co.FIRC.dao.utility.DBConnectionUtility;
import in.co.FIRC.utility.LoggableStatement;
import in.co.FIRC.vo.UserDetailsVO;
import java.sql.Connection;
import java.sql.ResultSet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;
 
public class FircHomeAction
  extends FIRCBaseAction
{
  private static Logger logger = LogManager.getLogger(FircHomeAction.class
    .getName());
  private static final long serialVersionUID = 1L;
  public String execute()
    throws ApplicationException
  {
    logger.info("Entering Method");
    String target = null;
    String result = null;
    FIRCHomeBD fircHomeBD = null;
    UserDetailsVO userVO = null;
    String count = null;
    try
    {
      isSessionAvailable();
      HttpSession session = ServletActionContext.getRequest().getSession();
      String userName = (String)session.getAttribute("loginedUserName");
      logger.info("loginedUserName------------------" + userName);
      fircHomeBD = new FIRCHomeBD();
      userVO = new UserDetailsVO();

 
 
      logger.info("Home Action session UserName" + userName);
      userVO.setSessionUserName(userName);
      count = fircHomeBD.getRole(userName);
      logger.info("111111111111111111-----------------" + userName);
      logger.info("count-------value-------------" + count);

 
      session.setAttribute("count", count);
      if ((count != null) && (count.equalsIgnoreCase("1")))
      {
        result = fircHomeBD.checkLoginedUserType(userName);
        if ((result != null) && (result.trim().equalsIgnoreCase("FIRCMAKER"))) {
          target = "maker";
        } else if ((result != null) && (result.trim().equalsIgnoreCase("FIRCCHECKER"))) {
          target = "checker";
        }
      }
      else if ((count != null) && (count.equalsIgnoreCase("2")))
      {
        target = "both";
      }
      else
      {
        target = "viewer";
      }
    }
    catch (Exception exception)
    {
      logger.info("---------execute-------------" + exception);
      throwApplicationException(exception);
    }
    logger.info("-----------------target" + target);
    logger.info("Exiting Method");
    return target;
  }
  public String makerProcess()
    throws ApplicationException
  {
    logger.info("--------------makerProcess-----------------");
    UserDetailsVO userVO = null;
    String sessionUserName = null;
    FIRCHomeBD fircHomeBD = null;
    String count = null;
    try
    {
      logger.info("Maker Screen----->");
      isSessionAvailable();
      userVO = new UserDetailsVO();
      HttpSession session = ServletActionContext.getRequest()
        .getSession();
      sessionUserName = (String)session.getAttribute("loginedUserName");
      if (sessionUserName != null)
      {
        fircHomeBD = new FIRCHomeBD();
        logger.info("sessionUserName--->" + sessionUserName);
        userVO.setSessionUserName(sessionUserName);
        count = (String)session.getAttribute("count");
        if (count == null)
        {
          count = fircHomeBD.getRole(sessionUserName);
          session.setAttribute("count", count);
        }
      }
    }
    catch (Exception exception)
    {
      logger.info("--------------makerProcess--------exception---------" + exception);
      throwApplicationException(exception);
    }
    logger.info("Exiting Method");
    return "success";
  }
  public String checkerProcess()
    throws ApplicationException
  {
    logger.info("Entering Method");
    UserDetailsVO userVO = null;
    String sessionUserName = null;
    FIRCHomeBD fircHomeBD = null;
    String count = null;
    try
    {
      logger.info("Checker Screen----->");
      isSessionAvailable();
      userVO = new UserDetailsVO();
      HttpSession session = ServletActionContext.getRequest()
        .getSession();
      sessionUserName = (String)session.getAttribute("loginedUserName");
      if (sessionUserName != null)
      {
        fircHomeBD = new FIRCHomeBD();
        logger.info("sessionUserName--->" + sessionUserName);
        userVO.setSessionUserName(sessionUserName);
        count = (String)session.getAttribute("count");
        if (count == null)
        {
          count = fircHomeBD.getRole(sessionUserName);
          session.setAttribute("count", count);
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
      logger.info("inside close window");
      con = DBConnectionUtility.getConnection();
      String query = "SELECT VALUE1 FROM ETT_PARAMETER_TBL WHERE PARAMETER_ID = 'closeURL' ";
      log = new LoggableStatement(con, query);
      rs = log.executeQuery();
      if (rs.next()) {
        closeUrl = rs.getString("VALUE1");
      }
      logger.info("close url value---------" + closeUrl);
      HttpServletResponse response = (HttpServletResponse)
        ActionContext.getContext().get("com.opensymphony.xwork2.dispatcher.HttpServletResponse");
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
}
