package in.co.mttenquiry.businessdelegate;

import in.co.mttenquiry.businessdelegate.exception.BusinessException;
import in.co.mttenquiry.utility.LogHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
 
public class BaseBusinessDelegate
{
  private static Logger logger = LogManager.getLogger(BaseBusinessDelegate.class
    .getName());
  public void throwBDException(Exception exception)
    throws BusinessException
  {
    logger.error(exception.fillInStackTrace());
    LogHelper.logError(logger, exception);
    throw new BusinessException(exception.getMessage());
  }
}
