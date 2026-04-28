package in.co.FIRC.action;

import in.co.FIRC.businessdelegate.IrmBD;
import in.co.FIRC.dao.exception.ApplicationException;
import in.co.FIRC.utility.ActionConstants;
import in.co.FIRC.vo.AlertMessageVO;
import in.co.FIRC.vo.IRMVO;
import java.util.ArrayList;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
 
public class IRMAction
  extends FIRCBaseAction
{
  private static Logger logger = LogManager.getLogger(IRMAction.class
    .getName());
  private static final long serialVersionUID = 1L;
  IRMVO irmExVO;
  Map<String, String> extensionInd;
  Map<String, String> recordInd;
  ArrayList<AlertMessageVO> errorList = null;
  public ArrayList<AlertMessageVO> getErrorList()
  {
    return this.errorList;
  }
  public void setErrorList(ArrayList<AlertMessageVO> errorList)
  {
    this.errorList = errorList;
  }
  public IRMVO getIrmExVO()
  {
    return this.irmExVO;
  }
  public void setIrmExVO(IRMVO irmExVO)
  {
    this.irmExVO = irmExVO;
  }
  public Map<String, String> getExtensionInd()
  {
    return ActionConstants.EX_IND;
  }
  public void setExtensionInd(Map<String, String> extensionInd)
  {
    this.extensionInd = extensionInd;
  }
  public Map<String, String> getRecordInd()
  {
    return ActionConstants.REC_IND;
  }
  public void setRecordInd(Map<String, String> recordInd)
  {
    this.recordInd = recordInd;
  }
  public String execute()
    throws ApplicationException
  {
    logger.info("Entering Method");
    try
    {
      isSessionAvailable();
    }
    catch (Exception exception)
    {
      throwApplicationException(exception);
    }
    logger.info("Exiting Method");
    return "success";
  }
  public String fetchIRMData()
    throws ApplicationException
  {
    logger.info("Entering Method");
    IrmBD bd = null;
    try
    {
      bd = IrmBD.getBD();
      if (this.irmExVO != null) {
        this.irmExVO = bd.fetchIRMData(this.irmExVO);
      }
    }
    catch (Exception exception)
    {
      throwApplicationException(exception);
    }
    logger.info("Exiting Method");
    return "success";
  }
  public String validateIRMExData()
    throws ApplicationException
  {
    logger.info("Entering Method");
    IrmBD bd = null;
    try
    {
      bd = IrmBD.getBD();
      if (this.irmExVO != null)
      {
        this.irmExVO = bd.validateIRMExData(this.irmExVO);
        this.errorList = this.irmExVO.getErrorList();
      }
    }
    catch (Exception exception)
    {
      throwApplicationException(exception);
    }
    logger.info("Exiting Method");
    return "success";
  }
  public String storeIRMExData()
    throws ApplicationException
  {
    logger.info("Entering Method");
    IrmBD bd = null;
    try
    {
      bd = IrmBD.getBD();
      if (this.irmExVO != null)
      {
        this.irmExVO = bd.validateIRMExData(this.irmExVO);
        this.errorList = this.irmExVO.getErrorList();
        if (this.errorList == null)
        {
          String result = bd.storeIRMExData(this.irmExVO);
          if ((result != null) && (result.equalsIgnoreCase("success")))
          {
            this.irmExVO = new IRMVO();
            addActionMessage("IRM Extension Succesfully Submitted");
          }
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
}
