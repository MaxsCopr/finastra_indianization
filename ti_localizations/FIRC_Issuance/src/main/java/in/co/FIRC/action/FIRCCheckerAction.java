package in.co.FIRC.action;

import in.co.FIRC.businessdelegate.FIRCCheckerBD;
import in.co.FIRC.dao.exception.ApplicationException;
import in.co.FIRC.vo.ourBank.IssuanceVO;
import in.co.FIRC.vo.ourBank.OurBankVO;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
 
public class FIRCCheckerAction
  extends FIRCBaseAction
{
  private static Logger logger = LogManager.getLogger(FIRCIssuanceAction.class
    .getName());
  private static final long serialVersionUID = 1L;
  IssuanceVO issuanceVO;
  ArrayList<OurBankVO> issuanceList = null;
  String[] chkList = null;
  String remarks = null;
  String check = null;
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
  public IssuanceVO getIssuanceVO()
  {
    return this.issuanceVO;
  }
  public void setIssuanceVO(IssuanceVO issuanceVO)
  {
    this.issuanceVO = issuanceVO;
  }
  public ArrayList<OurBankVO> getIssuanceList()
  {
    return this.issuanceList;
  }
  public void setIssuanceList(ArrayList<OurBankVO> issuanceList)
  {
    this.issuanceList = issuanceList;
  }
  public String fetchPendingOurBankDetails()
    throws ApplicationException
  {
    logger.info("Entering Method");
    FIRCCheckerBD bd = null;
    try
    {
      bd = new FIRCCheckerBD();
      isSessionAvailable();
      if (this.issuanceVO != null) {
        this.issuanceList = bd.fetchPendingOurBankDetails(this.issuanceVO);
      }
    }
    catch (Exception exception)
    {
      throwApplicationException(exception);
    }
    logger.info("Exiting Method");
    return "success";
  }
  public String resetInputDetails()
    throws ApplicationException
  {
    logger.info("Entering Method");
    try
    {
      if (this.issuanceVO != null) {
        this.issuanceVO = new IssuanceVO();
      }
    }
    catch (Exception exception)
    {
      throwApplicationException(exception);
    }
    logger.info("Exiting Method");
    return "success";
  }
  public String updateStatus()
    throws ApplicationException
  {
    logger.info("------------updateStatus-------------");
    FIRCCheckerBD fircCheckerBD = null;
    String result = null;
    try
    {
      fircCheckerBD = FIRCCheckerBD.getBD();
      result = fircCheckerBD.updateStatus(this.chkList, this.check, this.remarks);
      if ((result != null) && (result.equalsIgnoreCase("fail")))
      {
        this.issuanceList = fircCheckerBD.fetchPendingOurBankDetails(this.issuanceVO);
        this.remarks = "";
        return "success";
      }
      this.issuanceList = fircCheckerBD.fetchPendingOurBankDetails(this.issuanceVO);
      this.remarks = "";
    }
    catch (Exception exception)
    {
      logger.info("------------updateStatus--------exception-----" + exception);
      throwApplicationException(exception);
    }
    logger.info("Exiting Method");
    return result;
  }
}
