package in.co.localization.action;
 
import com.opensymphony.xwork2.ActionContext;

import in.co.localization.businessdelegate.localization.AdTransferBD;

import in.co.localization.dao.exception.ApplicationException;

import in.co.localization.utility.ActionConstants;

import in.co.localization.utility.DBConnectionUtility;

import in.co.localization.utility.LoggableStatement;

import in.co.localization.vo.localization.AdTransferVO;

import in.co.localization.vo.localization.AlertMessagesVO;

import java.sql.Connection;

import java.sql.ResultSet;

import java.util.ArrayList;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
 
public class AdTransferAction
  extends LocalizationBaseAction

{

  private static Logger logger = Logger.getLogger(AdTransferAction.class.getName());

  private static final long serialVersionUID = 1L;

  AdTransferVO adTransferVO = null;

  Map<String, String> exportAgency;

  Map<String, String> exportType;

  ArrayList<AdTransferVO> adTransferList = null;

  String mode;

  String shippingNo;

  ArrayList<AlertMessagesVO> errorList = null;

  public ArrayList<AlertMessagesVO> getErrorList()

  {

    return this.errorList;

  }

  public void setErrorList(ArrayList<AlertMessagesVO> errorList)

  {

    this.errorList = errorList;

  }

  public String getShippingNo()

  {

    return this.shippingNo;

  }

  public void setShippingNo(String shippingNo)

  {

    this.shippingNo = shippingNo;

  }

  public String getMode()

  {

    return this.mode;

  }

  public void setMode(String mode)

  {

    this.mode = mode;

  }

  public ArrayList<AdTransferVO> getAdTransferList()

  {

    return this.adTransferList;

  }

  public void setAdTransferList(ArrayList<AdTransferVO> adTransferList)

  {

    this.adTransferList = adTransferList;

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

  public AdTransferVO getAdTransferVO()

  {

    return this.adTransferVO;

  }

  public void setAdTransferVO(AdTransferVO adTransferVO)

  {

    this.adTransferVO = adTransferVO;

  }

  public String execute()

    throws ApplicationException

  {

    logger.info("Entering Method");

    AdTransferBD bd = null;

    try

    {

      isSessionAvailable();

      bd = new AdTransferBD();

      this.adTransferList = bd.fetchAdTransferValue();

      setAdTransferList(this.adTransferList);

    }

    catch (Exception exception)

    {

      throwApplicationException(exception);

    }

    logger.info("Exiting Method");

    return "success";

  }

  public String fetchAdTranferValue()

    throws ApplicationException

  {

    logger.info("Entering--------fetchAdTranferValue---------- Method");

    AdTransferBD bd = null;

    String target = "success";

    try

    {

      bd = new AdTransferBD();

      this.adTransferList = bd.fetchAdTransferValue();

      setAdTransferList(this.adTransferList);

      if (this.adTransferVO == null) {

        this.adTransferVO = new AdTransferVO();

      }

      if ((this.mode != null) && (this.mode.equalsIgnoreCase("edit")))

      {

        String temp = this.adTransferVO.getTempValue();

        this.adTransferVO.setShipId(temp);

        this.adTransferVO = bd.getShippingDetails(this.adTransferVO);

      }

      else if ((this.mode != null) && (this.mode.equalsIgnoreCase("validate")))

      {

        this.adTransferVO = bd.getValidate(this.adTransferVO);

        this.errorList = this.adTransferVO.getErrorList();

      }

      else if ((this.mode != null) && (this.mode.equalsIgnoreCase("store")))

      {

        this.adTransferVO = bd.getValidate(this.adTransferVO);

        this.errorList = this.adTransferVO.getErrorList();

        if (this.errorList == null)

        {

          bd.updateTransferData(this.adTransferVO);

          target = "confirm";

        }

      }

    }

    catch (Exception exception)

    {

      logger.info("Entering--------fetchAdTranferValue------exception0000000" + exception);

      throwApplicationException(exception);

    }

    logger.info("Exiting Method");

    return target;

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

      con = DBConnectionUtility.getConnection();

      String query = "SELECT VALUE1 FROM ETT_PARAMETER_TBL WHERE PARAMETER_ID = 'closeURL' ";

      log = new LoggableStatement(con, query);

      rs = log.executeQuery();

      if (rs.next()) {

        closeUrl = rs.getString("VALUE1");

      }

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