package in.co.chargeSchedule.businessdelegate;

import in.co.chargeSchedule.businessdelegate.exception.BusinessException;
import in.co.chargeSchedule.utility.LogHelper;
import org.apache.log4j.Logger;
 
public class BaseBusinessDelegate
{
  private static Logger logger = Logger.getLogger(BaseBusinessDelegate.class
    .getName());
  public void throwBDException(Exception exception)
    throws BusinessException
  {
    logger.error(exception.fillInStackTrace());
    LogHelper.logError(logger, exception);
    throw new BusinessException(exception.getMessage());
  }
}
