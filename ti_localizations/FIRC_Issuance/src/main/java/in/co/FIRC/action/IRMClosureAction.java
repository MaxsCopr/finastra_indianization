package in.co.FIRC.action;

import in.co.FIRC.businessdelegate.IRMClosureBD;
import in.co.FIRC.dao.exception.ApplicationException;
import in.co.FIRC.utility.ActionConstants;
import in.co.FIRC.vo.AlertMessageVO;
import in.co.FIRC.vo.IRMClosureVO;
import java.util.ArrayList;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
 
public class IRMClosureAction
  extends FIRCBaseAction
{
  private static Logger logger = LogManager.getLogger(IRMClosureAction.class
    .getName());
  private static final long serialVersionUID = 1L;
  IRMClosureVO irmAdjVO;
  Map<String, String> extensionInd;
  Map<String, String> recordInd;
  Map<String, String> reasonAdj;
  Map<String, String> closeInd;
  ArrayList<AlertMessageVO> errorList = null;
  public Map<String, String> getReasonAdj()
  {
    return ActionConstants.REASON_ADJ;
  }
  public void setReasonAdj(Map<String, String> reasonAdj)
  {
    this.reasonAdj = reasonAdj;
  }
  public IRMClosureVO getIrmAdjVO()
  {
    return this.irmAdjVO;
  }
  public void setIrmAdjVO(IRMClosureVO irmAdjVO)
  {
    this.irmAdjVO = irmAdjVO;
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
  public Map<String, String> getCloseInd()
  {
    return ActionConstants.CLOSE_IND;
  }
  public void setCloseInd(Map<String, String> closeInd)
  {
    this.closeInd = closeInd;
  }
  public ArrayList<AlertMessageVO> getErrorList()
  {
    return this.errorList;
  }
  public void setErrorList(ArrayList<AlertMessageVO> errorList)
  {
    this.errorList = errorList;
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
  public String fetchIRMClosureData()
    throws ApplicationException
  {
    logger.info("Entering Method");
    IRMClosureBD bd = null;
    try
    {
      bd = IRMClosureBD.getBD();
      if (this.irmAdjVO != null) {
        this.irmAdjVO = bd.fetchIRMClosureData(this.irmAdjVO);
      }
    }
    catch (Exception exception)
    {
      throwApplicationException(exception);
    }
    logger.info("Exiting Method");
    return "success";
  }
  public String validateIRMClosureData()
    throws ApplicationException
  {
    logger.info("Entering Method");
    IRMClosureBD bd = null;
    try
    {
      bd = IRMClosureBD.getBD();
      if (this.irmAdjVO != null)
      {
        this.irmAdjVO = bd.validateIRMClosureData(this.irmAdjVO);
        this.errorList = this.irmAdjVO.getErrorList();
      }
    }
    catch (Exception exception)
    {
      throwApplicationException(exception);
    }
    logger.info("Exiting Method");
    return "success";
  }
  public String storeIRMClosureData()
    throws ApplicationException
  {
    logger.info("Entering Method");
    IRMClosureBD bd = null;
    try
    {
      bd = IRMClosureBD.getBD();
      if (this.irmAdjVO != null)
      {
        this.irmAdjVO = bd.validateIRMClosureData(this.irmAdjVO);
        this.errorList = this.irmAdjVO.getErrorList();
        if (this.errorList == null)
        {
          String result = bd.storeIRMClosureData(this.irmAdjVO);
          if ((result != null) && (result.equalsIgnoreCase("success")))
          {
            this.irmAdjVO = new IRMClosureVO();
            addActionMessage("IRM Adjustment/Closure Succesfully Submitted");
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