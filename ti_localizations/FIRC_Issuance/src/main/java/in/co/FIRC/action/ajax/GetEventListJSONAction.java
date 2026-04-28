package in.co.FIRC.action.ajax;

import in.co.FIRC.action.FIRCBaseAction;
import in.co.FIRC.businessdelegate.FIRCOurBankBD;
import in.co.FIRC.dao.exception.ApplicationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
 
public class GetEventListJSONAction
  extends FIRCBaseAction
{
  private static final long serialVersionUID = 1L;
  private static Logger logger = LogManager.getLogger(GetEventListJSONAction.class.getName());
  String refNo;
  String resStatus;
  public String getResStatus()
  {
    return this.resStatus;
  }
  public void setResStatus(String resStatus)
  {
    this.resStatus = resStatus;
  }
  public String getRefNo()
  {
    return this.refNo;
  }
  public void setRefNo(String refNo)
  {
    this.refNo = refNo;
  }
  public String printStatus()
    throws ApplicationException
  {
    logger.info("----------printStatus------------");
    FIRCOurBankBD bd = null;
    String status = null;
    try
    {
      bd = new FIRCOurBankBD();
      if (this.refNo != null)
      {
        status = bd.printStatus(this.refNo);
        setResStatus(status);
      }
    }
    catch (Exception exception)
    {
      logger.info("----------printStatus--------exception----" + exception);
      throwApplicationException(exception);
    }
    logger.info("Exiting Method");
    return "success";
  }
}
