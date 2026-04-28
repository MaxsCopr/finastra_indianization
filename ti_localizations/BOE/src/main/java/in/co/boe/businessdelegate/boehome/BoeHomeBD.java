package in.co.boe.businessdelegate.boehome;

import in.co.boe.businessdelegate.BaseBusinessDelegate;
import in.co.boe.businessdelegate.exception.BusinessException;
import in.co.boe.dao.boehome.BoeHomeDAO;
import in.co.boe.utility.UserDetailsVO;
import org.apache.log4j.Logger;
 
public class BoeHomeBD
  extends BaseBusinessDelegate
{
  private static Logger logger = Logger.getLogger(BoeHomeBD.class
    .getName());
  static BoeHomeBD bd;
  public static BoeHomeBD getBD()
  {
    if (bd == null) {
      bd = new BoeHomeBD();
    }
    return bd;
  }
  public UserDetailsVO fetchLoginedUserId(UserDetailsVO userVO)
    throws BusinessException
  {
    logger.info("Entering Method");
    BoeHomeDAO boeHomeDAO = null;
    try
    {
      boeHomeDAO = BoeHomeDAO.getDAO();
      if (boeHomeDAO != null) {
        userVO = boeHomeDAO.fetchLoginedUserId(userVO);
      }
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return userVO;
  }
  public int checkLoginedUserType(UserDetailsVO userVO)
    throws BusinessException
  {
    logger.info("Entering Method");
    BoeHomeDAO boeHomeDAO = null;
    int count = 0;
    try
    {
      boeHomeDAO = BoeHomeDAO.getDAO();
      count = boeHomeDAO.checkLoginedUserType(userVO);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return count;
  }
  public void setDate()
    throws BusinessException
  {
    logger.info("Entering Method");
    BoeHomeDAO dao = null;
    try
    {
      dao = BoeHomeDAO.getDAO();
      dao.setDate();
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
  }
}
