package in.co.localization.businessdelegate.localization;
 
import in.co.localization.businessdelegate.BaseBusinessDelegate;
import in.co.localization.businessdelegate.exception.BusinessException;
import in.co.localization.dao.localization.AdTransferDAO;
import org.apache.log4j.Logger;
 
public class AdTransferBD
  extends BaseBusinessDelegate
{
  private static Logger logger = Logger.getLogger(AdTransferBD.class
    .getName());
  static AdTransferBD bd;
  public static AdTransferBD getBD()
  {
    if (bd == null) {
      bd = new AdTransferBD();
    }
    return bd;
  }
  public String getUserId(String userName)
    throws BusinessException
  {
    logger.info("Entering Method");
    AdTransferDAO dao = null;
    String userId = null;
    dao = AdTransferDAO.getDAO();
    if (dao != null) {
      try
      {
        userId = dao.getUserId(userName);
      }
      catch (Exception e)
      {
        throwBDException(e);
      }
    }
    logger.info("Exiting Method");
    return userId;
  }
  public void setDate()
    throws BusinessException
  {
    logger.info("Entering Method");
    AdTransferDAO dao = null;
    try
    {
      dao = AdTransferDAO.getDAO();
      dao.setDate();
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
  }
  public int checkLoginedUserType(String userName, String pageType)
    throws BusinessException
  {
    logger.info("Entering Method");
    AdTransferDAO dao = null;
    int count = 0;
    try
    {
      dao = AdTransferDAO.getDAO();
      count = dao.checkLoginedUserType(userName, pageType);
    }
    catch (Exception exception)
    {
      logger.info("checkLoginedUserType-------------->" + exception);
    }
    logger.info("Exiting Method");
    return count;
  }
}