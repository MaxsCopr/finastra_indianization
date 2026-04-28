package in.co.localization.businessdelegate.localization;
 
import in.co.localization.businessdelegate.BaseBusinessDelegate;

import in.co.localization.businessdelegate.exception.BusinessException;

import in.co.localization.dao.localization.AcknowledgementDAO;

import in.co.localization.vo.localization.AcknowledgementListVO;

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

  public ArrayList<AcknowledgementVO> fetchRodData(AcknowledgementVO ackVO)

    throws BusinessException

  {

    logger.info("Entering Method");

    ArrayList list = null;

    AcknowledgementDAO dao = null;

    try

    {

      list = new ArrayList();

      dao = AcknowledgementDAO.getDAO();

      list = dao.fetchRodData(ackVO);

    }

    catch (Exception exception)

    {

      throwBDException(exception);

    }

    logger.info("Exiting Method");

    return list;

  }

  public ArrayList<AcknowledgementVO> fetchPenData(AcknowledgementVO ackVO)

    throws BusinessException

  {

    logger.info("Entering-------fetchPenData----- Method");

    ArrayList list = null;

    AcknowledgementDAO dao = null;

    try

    {

      list = new ArrayList();

      dao = AcknowledgementDAO.getDAO();

      list = dao.fetchPenData(ackVO);

    }

    catch (Exception exception)

    {

      logger.info("Entering-------fetchPenData-----exception" + exception);

      throwBDException(exception);

    }

    logger.info("Exiting Method");

    return list;

  }

  public ArrayList<AcknowledgementVO> fetchTrrData(AcknowledgementVO ackVO)

    throws BusinessException

  {

    logger.info("Entering ---------fetchTrrData----------Method");

    ArrayList list = null;

    AcknowledgementDAO dao = null;

    try

    {

      list = new ArrayList();

      dao = AcknowledgementDAO.getDAO();

      list = dao.fetchTrrData(ackVO);

    }

    catch (Exception exception)

    {

      logger.info("Entering ---------fetchTrrData-----exception" + exception);

      throwBDException(exception);

    }

    logger.info("Exiting Method");

    return list;

  }

  public ArrayList<AcknowledgementVO> fetchIrmData(AcknowledgementVO ackVO)

    throws BusinessException

  {

    logger.info("Entering-----fetchIrmData--- Method");

    ArrayList list = null;

    AcknowledgementDAO dao = null;

    try

    {

      list = new ArrayList();

      dao = AcknowledgementDAO.getDAO();

      list = dao.fetchIrmData(ackVO);

    }

    catch (Exception exception)

    {

      logger.info("Entering-----fetchIrmData--exception" + exception);

      throwBDException(exception);

    }

    logger.info("Exiting Method");

    return list;

  }

  public ArrayList<AcknowledgementVO> fetchIRFIRcData(AcknowledgementVO ackVO)

    throws BusinessException

  {

    logger.info("Entering---------fetchIRFIRcData------- Method");

    ArrayList list = null;

    AcknowledgementDAO dao = null;

    try

    {

      list = new ArrayList();

      dao = AcknowledgementDAO.getDAO();

      list = dao.fetchIRFIRcData(ackVO);

    }

    catch (Exception exception)

    {

      logger.info("Entering---------fetchIRFIRcData------exception" + exception);

      throwBDException(exception);

    }

    logger.info("Exiting Method");

    return list;

  }

  public AcknowledgementListVO getAckPRNReportList(AcknowledgementListVO ackListVO)

    throws BusinessException

  {

    logger.info("Entering-------getAckPRNReportList-------- Method");

    AcknowledgementDAO dao = null;

    try

    {

      dao = AcknowledgementDAO.getDAO();

      ackListVO = dao.getAckPRNReportList(ackListVO);

    }

    catch (Exception exception)

    {

      logger.info("Entering-------getAckPRNReportList-------- exception" + exception);

      throwBDException(exception);

    }

    logger.info("Exiting Method");

    return ackListVO;

  }

  public AcknowledgementListVO getAckWSNReportList(AcknowledgementListVO ackListVO)

    throws BusinessException

  {

    logger.info("Entering getAckWSNReportList-----Method");

    AcknowledgementDAO dao = null;

    try

    {

      dao = AcknowledgementDAO.getDAO();

      ackListVO = dao.getAckWSNReportList(ackListVO);

    }

    catch (Exception exception)

    {

      logger.info("Entering getAckWSNReportList-----exception" + exception);

      throwBDException(exception);

    }

    logger.info("Exiting Method");

    return ackListVO;

  }

}