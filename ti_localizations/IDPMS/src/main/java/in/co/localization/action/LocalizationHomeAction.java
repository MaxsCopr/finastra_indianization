package in.co.localization.action;
 
import in.co.localization.dao.exception.ApplicationException;

import org.apache.log4j.Logger;
 
public class LocalizationHomeAction

  extends LocalizationBaseAction

{

  private static Logger logger = Logger.getLogger(LocalizationHomeAction.class

    .getName());

  private static final long serialVersionUID = 1L;

  public String landingPage()

    throws ApplicationException

  {

    logger.info("Entering Method");

    try

    {

      isSessionAvailable();

      logger.info("IDPMS isSessionAvailable--------Status" + isSessionAvailable());

    }

    catch (Exception exception)

    {

      logger.info("Exception in Session " + exception);

    }

    logger.info("Exiting Method");

    return "success";

  }

}