package in.co.mttenquiry.action;

import com.opensymphony.xwork2.ActionSupport;
import in.co.mttenquiry.dao.exception.ApplicationException;
import in.co.mttenquiry.utility.ActionConstants;
import in.co.mttenquiry.utility.LogHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
 
public class MTTBaseAction
  extends ActionSupport
  implements ActionConstants
{
  private static final long serialVersionUID = 1L;
  private static Logger logger = LogManager.getLogger(MTTBaseAction.class
    .getName());
  public void throwApplicationException(Exception exception)
    throws ApplicationException
  {
    logger.error(exception.fillInStackTrace());
    LogHelper.logError(logger, exception);
    throw new ApplicationException(exception.getMessage(), exception);
  }
}
