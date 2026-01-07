package in.co.localization.businessdelegate.localization;
 
import in.co.localization.businessdelegate.BaseBusinessDelegate;

import in.co.localization.businessdelegate.exception.BusinessException;

import in.co.localization.dao.localization.EDPMSUtilizeSearchDAO;

import in.co.localization.vo.localization.AcknowledgementListVO;

import in.co.localization.vo.localization.InvoiceDetailsVO;

import in.co.localization.vo.localization.ShippingDetailsVO;

import java.util.ArrayList;

import org.apache.log4j.Logger;
 
public class EDPMSUtilizeSearchBD

  extends BaseBusinessDelegate

{

  private static Logger logger = Logger.getLogger(EDPMSUtilizeSearchBD.class

    .getName());

  static EDPMSUtilizeSearchBD bd;

  public static EDPMSUtilizeSearchBD getBD()

  {

    if (bd == null) {

      bd = new EDPMSUtilizeSearchBD();

    }

    return bd;

  }

  public AcknowledgementListVO fetchGRData(AcknowledgementListVO ackListVO)

    throws BusinessException

  {

    logger.info("-----------fetchGRData-----------Entering Method");

    EDPMSUtilizeSearchDAO dao = null;

    try

    {

      dao = EDPMSUtilizeSearchDAO.getDAO();

      ackListVO = dao.fetchGRData(ackListVO);

    }

    catch (Exception exception)

    {

      logger.info("-----------fetchGRData--------exception" + exception);

      throwBDException(exception);

    }

    logger.info("Exiting Method");

    return ackListVO;

  }

  public ArrayList<AcknowledgementListVO> custData(AcknowledgementListVO ackListVO)

    throws BusinessException

  {

    logger.info("Entering Method");

    EDPMSUtilizeSearchDAO dao = null;

    ArrayList list = null;

    try

    {

      dao = EDPMSUtilizeSearchDAO.getDAO();

      list = dao.custData(ackListVO);

    }

    catch (Exception exception)

    {

      throwBDException(exception);

    }

    logger.info("Exiting Method");

    return list;

  }

  public ArrayList<ShippingDetailsVO> fetchSBUnUtilizeBills(AcknowledgementListVO ackListVO)

    throws BusinessException

  {

    logger.info("Entering   fetchSBUnUtilizeBills Method");

    ArrayList shippingList = null;

    EDPMSUtilizeSearchDAO dao = null;

    try

    {

      dao = EDPMSUtilizeSearchDAO.getDAO();

      shippingList = dao.fetchSBUnUtilizeBills(ackListVO);

    }

    catch (Exception exception)

    {

      logger.info("Entering   fetchSBUnUtilizeBills Method--------exception-----" + exception);

      throwBDException(exception);

    }

    logger.info("Exiting Method");

    return shippingList;

  }

  public ArrayList<InvoiceDetailsVO> fetchMDFUnUtilizeBills(AcknowledgementListVO ackListVO)

    throws BusinessException

  {

    logger.info("Entering -----fetchMDFUnUtilizeBills------------ Method");

    ArrayList shippingList = null;

    EDPMSUtilizeSearchDAO dao = null;

    try

    {

      dao = EDPMSUtilizeSearchDAO.getDAO();

      shippingList = dao.fetchMDFUnUtilizeBills(ackListVO);

    }

    catch (Exception exception)

    {

      logger.info("Entering -----fetchMDFUnUtilizeBills------------ exception" + exception);

      throwBDException(exception);

    }

    logger.info("Exiting Method");

    return shippingList;

  }

}