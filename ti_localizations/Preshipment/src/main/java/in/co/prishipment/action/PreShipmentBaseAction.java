package in.co.prishipment.action;
 
import com.opensymphony.xwork2.ActionSupport;

import in.co.prishipment.dao.exception.ApplicationException;

import in.co.prishipment.utility.LogHelper;

import org.apache.log4j.Logger;
 
public class PreShipmentBaseAction

  extends ActionSupport

{

  private static final long serialVersionUID = 1L;

  private static Logger logger = Logger.getLogger(PreShipmentBaseAction.class

    .getName());

  public void throwApplicationException(Exception exception)

    throws ApplicationException

  {

    logger.error(exception.fillInStackTrace());

    LogHelper.logError(logger, exception);

    throw new ApplicationException(exception.getMessage(), exception);

  }

}