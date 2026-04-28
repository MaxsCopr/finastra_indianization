package in.co.localization.businessdelegate.localization;
 
import in.co.localization.businessdelegate.BaseBusinessDelegate;
import in.co.localization.businessdelegate.exception.BusinessException;
import in.co.localization.dao.localization.AcknowledgementDAO;
import in.co.localization.vo.localization.AcknowledgementVO;
import java.util.ArrayList;
import org.apache.log4j.Logger;
 
public class AcknowledgementBD
  extends BaseBusinessDelegate
{
  private static Logger logger = Logger.getLogger(AcknowledgementBD.class
    .getName());
  static AcknowledgementBD bd;
  public static AcknowledgementBD getBD()
  {
    if (bd == null) {
      bd = new AcknowledgementBD();
    }
    return bd;
  }
  public AcknowledgementVO fetchOrmData(AcknowledgementVO ackVO)
    throws BusinessException
  {
    logger.info("--------------fetchOrmData--------------");
    ArrayList<AcknowledgementVO> list = null;
    AcknowledgementDAO dao = null;
    try
    {
      dao = AcknowledgementDAO.getDAO();
      if (dao != null) {
        ackVO = dao.fetchOrmData(ackVO);
      }
    }
    catch (Exception exception)
    {
      logger.info("--------------fetchOrmData------exception--------" + exception);
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return ackVO;
  }
  public AcknowledgementVO fetchBeeData(AcknowledgementVO ackVO)
    throws BusinessException
  {
    logger.info("----fetchBeeData----------");
    ArrayList<AcknowledgementVO> list = null;
    AcknowledgementDAO dao = null;
    try
    {
      dao = AcknowledgementDAO.getDAO();
      if (dao != null) {
        ackVO = dao.fetchBeeData(ackVO);
      }
    }
    catch (Exception exception)
    {
      logger.info("----fetchBeeData------exception----" + exception);
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return ackVO;
  }
  public AcknowledgementVO fetchBeaData(AcknowledgementVO ackVO)
    throws BusinessException
  {
    logger.info("----------fetchBeaData-----------------");
    ArrayList<AcknowledgementVO> list = null;
    AcknowledgementDAO dao = null;
    try
    {
      dao = AcknowledgementDAO.getDAO();
      if (dao != null) {
        ackVO = dao.fetchBeaData(ackVO);
      }
    }
    catch (Exception exception)
    {
      logger.info("----------fetchBeaData------------exception-----" + exception);
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return ackVO;
  }
  public AcknowledgementVO fetchBesData(AcknowledgementVO ackVO)
    throws BusinessException
  {
    logger.info("Entering Method");
    ArrayList<AcknowledgementVO> list = null;
    AcknowledgementDAO dao = null;
    try
    {
      dao = AcknowledgementDAO.getDAO();
      if (dao != null) {
        ackVO = dao.fetchBesData(ackVO);
      }
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return ackVO;
  }
  public AcknowledgementVO fetchBesDataForMaker(AcknowledgementVO ackVO)
    throws BusinessException
  {
    logger.info("Entering Method");
    ArrayList<AcknowledgementVO> list = null;
    AcknowledgementDAO dao = null;
    try
    {
      dao = AcknowledgementDAO.getDAO();
      if (dao != null) {
        ackVO = dao.fetchBesDataForMaker(ackVO);
      }
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return ackVO;
  }
  public AcknowledgementVO deleteBesData(String[] chkList, String remarks, AcknowledgementVO ackVO)
    throws BusinessException
  {
    logger.info("Entering Method deleteBesData");
    ArrayList<AcknowledgementVO> list = null;
    AcknowledgementDAO dao = null;
    try
    {
      dao = AcknowledgementDAO.getDAO();
      if (dao != null) {
        ackVO = dao.deleteBesData(chkList, remarks, ackVO);
      }
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method deleteBesData");
    return ackVO;
  }
  public AcknowledgementVO fetchMbeData(AcknowledgementVO ackVO)
    throws BusinessException
  {
    logger.info("-------------fetchMbeData------------------------");
    ArrayList<AcknowledgementVO> list = null;
    AcknowledgementDAO dao = null;
    try
    {
      dao = AcknowledgementDAO.getDAO();
      if (dao != null) {
        ackVO = dao.fetchMbeData(ackVO);
      }
    }
    catch (Exception exception)
    {
      logger.info("-------------fetchMbeData-------------exception-----------" + exception);
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return ackVO;
  }
  public AcknowledgementVO fetchOraData(AcknowledgementVO ackVO)
    throws BusinessException
  {
    logger.info("--------------fetchOraData---------------------");
    ArrayList<AcknowledgementVO> list = null;
    AcknowledgementDAO dao = null;
    try
    {
      list = new ArrayList();
      dao = AcknowledgementDAO.getDAO();
      if (dao != null) {
        ackVO = dao.fetchOraData(ackVO);
      }
    }
    catch (Exception exception)
    {
      logger.info("--------------fetchOraData---------exception------------" + exception);
      exception.printStackTrace();
      throwBDException(exception);
    }
    return ackVO;
  }
  public AcknowledgementVO fetchObbData(AcknowledgementVO ackVO)
    throws BusinessException
  {
    logger.info("Entering Method");
    ArrayList<AcknowledgementVO> list = null;
    AcknowledgementDAO dao = null;
    try
    {
      dao = AcknowledgementDAO.getDAO();
      if (dao != null) {
        ackVO = dao.fetchObbData(ackVO);
      }
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return ackVO;
  }
  public AcknowledgementVO fetchBttData(AcknowledgementVO ackVO)
    throws BusinessException
  {
    logger.info("Entering Method");
    ArrayList<AcknowledgementVO> list = null;
    AcknowledgementDAO dao = null;
    try
    {
      dao = AcknowledgementDAO.getDAO();
      if (dao != null) {
        ackVO = dao.fetchBttData(ackVO);
      }
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return ackVO;
  }
  public AcknowledgementVO fetchBesCheckerData(AcknowledgementVO ackVO)
    throws BusinessException
  {
    logger.info("Entering Method");
    ArrayList<AcknowledgementVO> list = null;
    AcknowledgementDAO dao = null;
    try
    {
      dao = AcknowledgementDAO.getDAO();
      if (dao != null) {
        ackVO = dao.fetchBesDeleteDataForChecker(ackVO);
      }
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return ackVO;
  }
  public String updateBesCheckerData(String[] chkList1, String check1, String remarks1)
    throws BusinessException
  {
    logger.info("Entering Method");
    AcknowledgementDAO dao = null;
    String result = null;
    try
    {
      dao = AcknowledgementDAO.getDAO();
      result = dao.updateBesCheckerData(chkList1, check1, remarks1);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return result;
  }
  public int checkLoginedUserType(AcknowledgementVO ackVO)
    throws BusinessException
  {
    logger.info("Entering Method");
    AcknowledgementDAO dao = null;
    int count = 0;
    try
    {
      dao = AcknowledgementDAO.getDAO();
      count = dao.checkLoginedUserType(ackVO);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return count;
  }
}