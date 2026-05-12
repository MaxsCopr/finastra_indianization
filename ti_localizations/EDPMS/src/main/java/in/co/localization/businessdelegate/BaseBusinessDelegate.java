package in.co.localization.businessdelegate;

import org.apache.log4j.Logger;

import in.co.localization.businessdelegate.exception.BusinessException;
import in.co.localization.utility.LogHelper;

public class BaseBusinessDelegate {
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