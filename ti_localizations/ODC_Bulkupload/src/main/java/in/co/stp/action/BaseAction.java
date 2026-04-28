package in.co.stp.action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import in.co.stp.dao.exception.ApplicationException;
import in.co.stp.utility.ActionConstants;
import in.co.stp.utility.DBConnectionUtility;
import in.co.stp.utility.LogHelper;
import in.co.stp.utility.LoggableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
 
public class BaseAction
  extends ActionSupport
  implements ActionConstants
{
  private static final long serialVersionUID = 1L;
  private static Logger logger = LogManager.getLogger(BaseAction.class.getName());
  public String closeWindow()
    throws Exception
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement log = null;
    ResultSet rs1 = null;
    String closeUrl = "";
    try
    {
      con = DBConnectionUtility.getConnection();
      String query = "SELECT VALUE1 FROM ETT_PARAMETER_TBL WHERE PARAMETER_ID ='closeURL'";
      log = new LoggableStatement(con, query);
      rs1 = log.executeQuery();
      if (rs1.next())
      {
        logger.info("value" + rs1.getString("VALUE1"));
        closeUrl = rs1.getString("VALUE1");
        logger.info("-----" + closeUrl);
      }
      logger.info("enter into close window2");
      HttpServletResponse response = (HttpServletResponse)ActionContext.getContext()
        .get("com.opensymphony.xwork2.dispatcher.HttpServletResponse");
      response.sendRedirect(closeUrl);
    }
    catch (Exception exception)
    {
      logger.info("Exception in Close Window Method--------" + exception.getMessage());
      logger.info("Exception in Close Window Method--------" + exception.getMessage());
      throwApplicationException(exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, log, rs1);
    }
    logger.info("Exiting Method");
    logger.info("enter into close window4");
    return "none";
  }
  public void throwApplicationException(Exception exception)
    throws ApplicationException
  {
    logger.error(exception.fillInStackTrace());
    LogHelper.logError(logger, exception);
    throw new ApplicationException(exception.getMessage(), exception);
  }
}
