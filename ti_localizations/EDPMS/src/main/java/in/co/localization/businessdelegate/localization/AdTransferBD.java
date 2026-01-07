package in.co.localization.businessdelegate.localization;
 
import in.co.localization.businessdelegate.BaseBusinessDelegate;

import in.co.localization.businessdelegate.exception.BusinessException;

import in.co.localization.dao.localization.AdTransferDAO;

import in.co.localization.vo.localization.AdTransferVO;

import in.co.localization.vo.localization.EDMPSProcessVO;

import in.co.localization.vo.localization.EodDownloadVO;

import java.util.ArrayList;

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

  public ArrayList<AdTransferVO> fetchAdTransferValue()

    throws BusinessException

  {

    logger.info("EnteringfetchAdTransferValue---------- Method");

    ArrayList list = null;

    AdTransferDAO dao = null;

    try

    {

      list = new ArrayList();

      dao = AdTransferDAO.getDAO();

      list = dao.fetchAdTransferValue();

    }

    catch (Exception exception)

    {

      logger.info("Exiting Method-----fetchAdTransferValue---exception-----" + exception);

      throwBDException(exception);

    }

    return list;

  }

  public AdTransferVO getShippingDetails(AdTransferVO adTransferVO)

    throws BusinessException

  {

    logger.info("Entering Method");

    AdTransferDAO dao = null;

    try

    {

      dao = AdTransferDAO.getDAO();

      adTransferVO = dao.getShippingDetails(adTransferVO);

    }

    catch (Exception exception)

    {

      throwBDException(exception);

    }

    logger.info("Exiting Method");

    return adTransferVO;

  }

  public AdTransferVO getValidate(AdTransferVO adTransferVO)

    throws BusinessException

  {

    logger.info("Entering Method");

    AdTransferDAO dao = null;

    try

    {

      dao = AdTransferDAO.getDAO();

      adTransferVO = dao.getValidate(adTransferVO);

    }

    catch (Exception exception)

    {

      throwBDException(exception);

    }

    logger.info("Exiting Method");

    return adTransferVO;

  }

  public void updateTransferData(AdTransferVO adTransferVO)

    throws BusinessException

  {

    logger.info("Entering---------updateTransferData---------- Method");

    AdTransferDAO dao = null;

    try

    {

      dao = AdTransferDAO.getDAO();

      dao.updateTransferData(adTransferVO);

    }

    catch (Exception exception)

    {

      logger.info("Entering---------updateTransferData----------exception" + exception);

      throwBDException(exception);

    }

    logger.info("Exiting Method");

  }

  public ArrayList<EDMPSProcessVO> fetchEODFileList(EodDownloadVO eodDownloadVO)

    throws BusinessException

  {

    logger.info("Entering Method");

    ArrayList list = null;

    AdTransferDAO dao = null;

    try

    {

      dao = AdTransferDAO.getDAO();

      list = dao.fetchEODFileList(eodDownloadVO);

    }

    catch (Exception exception)

    {

      throwBDException(exception);

    }

    logger.info("Exiting Method");

    return list;

  }

  public String getUserId(String userName)

    throws BusinessException

  {

    logger.info("Entering Method---------getUserId-----userName---" + userName);

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

}