package in.co.localization.businessdelegate.localization;
 
import in.co.localization.businessdelegate.BaseBusinessDelegate;

import in.co.localization.businessdelegate.exception.BusinessException;

import in.co.localization.dao.localization.RealizationDAO;

import in.co.localization.vo.localization.AcknowledgementListVO;

import in.co.localization.vo.localization.AcknowledgementVO;

import in.co.localization.vo.localization.ShippingDetailsVO;

import java.util.ArrayList;

import org.apache.log4j.Logger;
 
public class RealizationBD

  extends BaseBusinessDelegate

{

  private static Logger logger = Logger.getLogger(RealizationBD.class

    .getName());

  static RealizationBD bd;

  public static RealizationBD getBD()

  {

    if (bd == null) {

      bd = new RealizationBD();

    }

    return bd;

  }

  public ArrayList<AcknowledgementVO> getPRNAckData(AcknowledgementListVO ackListVO)

    throws BusinessException

  {

    logger.info("Entering -----------getPRNAckData---------Method");

    RealizationDAO dao = null;

    ArrayList prnShippingList = null;

    try

    {

      dao = RealizationDAO.getDAO();

      prnShippingList = dao.fetchPRNShippingData(ackListVO);

    }

    catch (Exception exception)

    {

      logger.info("Entering -----------getPRNAckData-----exception" + exception);

      throwBDException(exception);

    }

    logger.info("Exiting Method");

    return prnShippingList;

  }

  public ShippingDetailsVO prnAckDataView(AcknowledgementListVO ackListVO)

    throws BusinessException

  {

    logger.info("Entering Method");

    RealizationDAO dao = null;

    ShippingDetailsVO shippingVO = null;

    try

    {

      dao = RealizationDAO.getDAO();

      shippingVO = dao.prnAckDataView(ackListVO);

    }

    catch (Exception exception)

    {

      throwBDException(exception);

    }

    logger.info("Exiting Method");

    return shippingVO;

  }

  public int storeCancelPRNData(AcknowledgementListVO ackListVO, ShippingDetailsVO shippingVO)

    throws BusinessException

  {

    logger.info("Entering-----------storeCancelPRNData--------- Method");

    RealizationDAO dao = null;

    int iRet = 0;

    try

    {

      dao = RealizationDAO.getDAO();

      iRet = dao.storeCancelPRNData(ackListVO, shippingVO);

    }

    catch (Exception exception)

    {

      try

      {

        logger.info("Entering-----------Exception--------- exception" + exception);

        throwBDException(exception);

      }

      catch (BusinessException e)

      {

        logger.info("Entering-----------BusinessException--------- exception" + e);

        e.printStackTrace();

      }

    }

    logger.info("Exiting Method");

    return iRet;

  }

}