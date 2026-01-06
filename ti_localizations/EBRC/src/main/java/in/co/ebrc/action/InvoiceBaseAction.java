package in.co.ebrc.action;

import com.opensymphony.xwork2.ActionSupport;
import in.co.ebrc.dao.exception.ApplicationException;
import in.co.ebrc.utility.ActionConstants;
import in.co.ebrc.utility.LogHelper;
import java.util.Map;
import org.apache.log4j.Logger;
 
public class InvoiceBaseAction
  extends ActionSupport
  implements ActionConstants
{
  private static final long serialVersionUID = 1L;
  private static Logger logger = Logger.getLogger(InvoiceBaseAction.class
    .getName());
  Map<String, String> ebrcStatus;
  Map<String, String> status;
  public void throwApplicationException(Exception exception)
    throws ApplicationException
  {
    logger.error(exception.fillInStackTrace());
    LogHelper.logError(logger, exception);
    throw new ApplicationException(exception.getMessage(), exception);
  }
  public Map<String, String> getEbrcStatus()
  {
    return ActionConstants.REC_IND;
  }
  public void setEbrcStatus(Map<String, String> ebrcStatus)
  {
    this.ebrcStatus = ebrcStatus;
  }
  public Map<String, String> getstatus()
  {
    return ActionConstants.REC;
  }
  public void setstatus(Map<String, String> status)
  {
    this.status = status;
  }
}