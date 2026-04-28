package in.co.localization.action;
 
import com.opensymphony.xwork2.ActionContext;

import com.opensymphony.xwork2.ActionSupport;

import in.co.localization.businessdelegate.localization.AdTransferBD;

import in.co.localization.dao.exception.ApplicationException;

import in.co.localization.utility.ActionConstants;

import in.co.localization.utility.DBConnectionUtility;

import in.co.localization.utility.LogHelper;

import in.co.localization.utility.LoggableStatement;

import java.sql.Connection;

import java.sql.ResultSet;

import java.util.Map;

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

  Map<String, String> exportAgency;

  Map<String, String> exportType;

  Map<String, String> realStatus;

  Map<String, String> recordInd;

  Map<String, String> shpInd;

  Map<String, String> writeInd;

  public boolean isSessionAvailable()

    throws ApplicationException

  {

    logger.info("--isSessionAvailable-------Entering Method");

    String sessionUserName = null;

    String userId = null;

    AdTransferBD bd = null;

    boolean isAvail = false;

    try

    {

      HttpSession session = ServletActionContext.getRequest().getSession();

      HttpServletRequest request = (HttpServletRequest)ActionContext.getContext().get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");

      String userName = (String)session.getAttribute("loginedUserName");

      logger.info("loginedUserName------------------" + userName);

      if (userName == null)

      {

        userName = request.getRemoteUser();

        logger.info("getRemoteUser[------------------" + userName);

        if (userName == null)

        {

          Connection wise_con = null;

          wise_con = DBConnectionUtility.getWiseConnection();

 
          userName = request.getRequestedSessionId();

          String get_User_ID = "SELECT SCT.USERNAME AS USER_ID FROM CENTRAL_SESSION_DETAILS SCT,LOCAL_SESSION_DETAILS LOC  WHERE SCT.CENTRAL_ID=LOC.CENTRAL_ID AND SCT.ENDED  IS NULL AND LOC.LOCAL_ID= ? ";

 
 
          LoggableStatement lst = new LoggableStatement(wise_con, get_User_ID);

          lst.setString(1, userName);

          logger.info("Getting Session Value Query------------" + lst.getQueryString());

 
          ResultSet rst = lst.executeQuery();

          while (rst.next())

          {

            userName = rst.getString("USER_ID");

            logger.info("Getting Session Value Query-- user id value----------" + userName);

          }

          DBConnectionUtility.surrenderDB(wise_con, lst, rst);

          logger.info("userName-----------" + userName);

        }

      }

      if (userName != null)

      {

        logger.info("Entering if Loop--------sessionUserName-------" + userName);

        bd = new AdTransferBD();

        userId = bd.getUserId(userName);

        session.setAttribute("loginedUserName", userName);

        bd.setDate();

      }

      return isAvail;

    }

    catch (Exception exception)

    {

      throwApplicationException(exception);

 
      logger.info("--isSessionAvailable-------Enexception" + exception);

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

  public Map<String, String> getWriteInd()

  {

    return ActionConstants.WR_IND;

  }

  public void setWriteInd(Map<String, String> writeInd)

  {

    this.writeInd = writeInd;

  }

  public Map<String, String> getShpInd()

  {

    return ActionConstants.SHP_IND;

  }

  public void setShpInd(Map<String, String> shpInd)

  {

    this.shpInd = shpInd;

  }

  public Map<String, String> getExportAgency()

  {

    return ActionConstants.EXP_AGENCY;

  }

  public void setExportAgency(Map<String, String> exportAgency)

  {

    this.exportAgency = exportAgency;

  }

  public Map<String, String> getExportType()

  {

    return ActionConstants.EXP_TYPE;

  }

  public void setExportType(Map<String, String> exportType)

  {

    this.exportType = exportType;

  }

  public Map<String, String> getRealStatus()

  {

    return ActionConstants.REAL_STATUS;

  }

  public void setRealStatus(Map<String, String> realStatus)

  {

    this.realStatus = realStatus;

  }

  public Map<String, String> getRecordInd()

  {

    return ActionConstants.REC_IND;

  }

  public void setRecordInd(Map<String, String> recordInd)

  {

    this.recordInd = recordInd;

  }

}