package in.co.localization.action;
 
import in.co.localization.dao.exception.ApplicationException;

import in.co.localization.vo.localization.AdTransferVO;

import org.apache.log4j.Logger;
 
public class AdTransferAction

  extends LocalizationBaseAction

{

  private static Logger logger = Logger.getLogger(AdTransferAction.class

    .getName());

  private static final long serialVersionUID = 1L;

  AdTransferVO adTransferVO = null;

  public AdTransferVO getAdTransferVO()

  {

    return this.adTransferVO;

  }

  public void setAdTransferVO(AdTransferVO adTransferVO)

  {

    this.adTransferVO = adTransferVO;

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

}

 