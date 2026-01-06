package in.co.FIRC.action;

import in.co.FIRC.businessdelegate.IRMCheckerBD;
import in.co.FIRC.dao.exception.ApplicationException;
import in.co.FIRC.vo.IRMClosureVO;
import in.co.FIRC.vo.IRMVO;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
 
public class IRMCheckerAction
  extends FIRCBaseAction
{
  private static final long serialVersionUID = 1L;
  private static Logger logger = LogManager.getLogger(FIRCCheckerAction.class.getName());
  ArrayList<IRMVO> extensionList = null;
  ArrayList<IRMClosureVO> closureList = null;
  String[] chkList = null;
  String remarks = null;
  String check = null;
  IRMVO irmExVO = null;
  IRMClosureVO irmAdjVO = null;
  String[] closureChkList = null;
  String closureRemarks = null;
  String closureCheck = null;
  public String[] getClosureChkList()
  {
    return this.closureChkList;
  }
  public void setClosureChkList(String[] closureChkList)
  {
    this.closureChkList = closureChkList;
  }
  public String getClosureRemarks()
  {
    return this.closureRemarks;
  }
  public void setClosureRemarks(String closureRemarks)
  {
    this.closureRemarks = closureRemarks;
  }
  public String getClosureCheck()
  {
    return this.closureCheck;
  }
  public void setClosureCheck(String closureCheck)
  {
    this.closureCheck = closureCheck;
  }
  public ArrayList<IRMClosureVO> getClosureList()
  {
    return this.closureList;
  }
  public void setClosureList(ArrayList<IRMClosureVO> closureList)
  {
    this.closureList = closureList;
  }
  public IRMClosureVO getIrmAdjVO()
  {
    return this.irmAdjVO;
  }
  public void setIrmAdjVO(IRMClosureVO irmAdjVO)
  {
    this.irmAdjVO = irmAdjVO;
  }
  public IRMVO getIrmExVO()
  {
    return this.irmExVO;
  }
  public void setIrmExVO(IRMVO irmExVO)
  {
    this.irmExVO = irmExVO;
  }
  public ArrayList<IRMVO> getExtensionList()
  {
    return this.extensionList;
  }
  public void setExtensionList(ArrayList<IRMVO> extensionList)
  {
    this.extensionList = extensionList;
  }
  public String[] getChkList()
  {
    return this.chkList;
  }
  public void setChkList(String[] chkList)
  {
    this.chkList = chkList;
  }
  public String getRemarks()
  {
    return this.remarks;
  }
  public void setRemarks(String remarks)
  {
    this.remarks = remarks;
  }
  public String getCheck()
  {
    return this.check;
  }
  public void setCheck(String check)
  {
    this.check = check;
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
  public String fetchIrmExtension()
    throws ApplicationException
  {
    logger.info("Entering Method");
    IRMCheckerBD bd = null;
    try
    {
      bd = IRMCheckerBD.getBD();
      if (this.irmExVO != null) {
        this.extensionList = bd.fetchIRMExtensionData(this.irmExVO);
      }
    }
    catch (Exception exception)
    {
      throwApplicationException(exception);
    }
    logger.info("Exiting Method");
    return "success";
  }
  public String updateIRMExtensionStatus()
    throws ApplicationException
  {
    logger.info("Entering Method");
    IRMCheckerBD bd = null;
    String result = null;
    try
    {
      bd = IRMCheckerBD.getBD();
      result = bd.updateIRMExtensionStatus(this.chkList, this.check, this.remarks);
      if ((result != null) && (result.equalsIgnoreCase("fail")))
      {
        this.extensionList = bd.fetchIRMExtensionData(this.irmExVO);
        return "success";
      }
      this.extensionList = bd.fetchIRMExtensionData(this.irmExVO);
      this.remarks = "";
    }
    catch (Exception exception)
    {
      throwApplicationException(exception);
    }
    logger.info("Exiting Method");
    return result;
  }
  public String irmClosureChecker()
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
  public String fetchIrmClosure()
    throws ApplicationException
  {
    logger.info("Entering Method");
    IRMCheckerBD bd = null;
    try
    {
      bd = IRMCheckerBD.getBD();
      if (this.irmAdjVO != null) {
        this.closureList = bd.fetchIrmClosureData(this.irmAdjVO);
      }
    }
    catch (Exception exception)
    {
      throwApplicationException(exception);
    }
    logger.info("Exiting Method");
    return "success";
  }
  public String updateClosureCheckerStatus()
    throws ApplicationException
  {
    logger.info("Entering Method");
    IRMCheckerBD bd = null;
    String result = null;
    try
    {
      bd = IRMCheckerBD.getBD();
      result = bd.updateClosureCheckerStatus(this.closureChkList, this.closureCheck, this.closureRemarks);
      if ((result != null) && (result.equalsIgnoreCase("fail")))
      {
        this.closureList = bd.fetchIrmClosureData(this.irmAdjVO);
        return "success";
      }
      this.closureList = bd.fetchIrmClosureData(this.irmAdjVO);
      this.closureRemarks = "";
    }
    catch (Exception exception)
    {
      throwApplicationException(exception);
    }
    logger.info("Exiting Method");
    return result;
  }
}
