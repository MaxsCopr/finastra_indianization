package in.co.FIRC.businessdelegate;

import in.co.FIRC.businessdelegate.exception.BusinessException;
import in.co.FIRC.dao.FIRCHomeDAO;
import in.co.FIRC.vo.UserDetailsVO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
 
public class FIRCHomeBD
  extends BaseBusinessDelegate
{
  private static Logger logger = LogManager.getLogger(FIRCHomeBD.class.getName());
  static FIRCHomeBD bd;
  public static FIRCHomeBD getBD()
  {
    if (bd == null) {
      bd = new FIRCHomeBD();
    }
    return bd;
  }
  public UserDetailsVO fetchLoginedUserId(UserDetailsVO userVO)
    throws BusinessException
  {
    logger.info("Entering Method");
    FIRCHomeDAO boeHomeDAO = null;
    try
    {
      boeHomeDAO = FIRCHomeDAO.getDAO();
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
  public String checkLoginedUserType(String userVO)
    throws BusinessException
  {
    logger.info("Entering Method");
    FIRCHomeDAO boeHomeDAO = null;
    String result = null;
    try
    {
      boeHomeDAO = FIRCHomeDAO.getDAO();
      result = boeHomeDAO.checkLoginedUserType(userVO);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return result;
  }
  public void setDate()
    throws BusinessException
  {
    logger.info("Entering Method");
    FIRCHomeDAO dao = null;
    try
    {
      dao = FIRCHomeDAO.getDAO();
      dao.setDate();
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
  }
  public String getRole(String userVO)
    throws BusinessException
  {
    logger.info("Entering Method");
    FIRCHomeDAO dao = null;
    String result = null;
    try
    {
      dao = FIRCHomeDAO.getDAO();
      result = dao.getRole(userVO);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return result;
  }
}