package in.co.boe.businessdelegate.billnotomanypaymentreference;

import in.co.boe.businessdelegate.BaseBusinessDelegate;
import in.co.boe.businessdelegate.exception.BusinessException;
import in.co.boe.dao.billnotomanypricereference.BoeBillNoToManyPaymentReferenceDAO;
import in.co.boe.dao.pricereferencetomanybill.BoeOfPriceReferenceToManyBillNoDAO;
import in.co.boe.vo.BOESearchVO;
import in.co.boe.vo.BillTypeVO;
import in.co.boe.vo.BoeVO;
import java.util.ArrayList;
import org.apache.log4j.Logger;
 
public class BoeBillNoToManyPaymentReferenceBD
  extends BaseBusinessDelegate
{
  private static Logger logger = Logger.getLogger(BoeBillNoToManyPaymentReferenceBD.class.getName());
  static BoeBillNoToManyPaymentReferenceBD bd;
  public static BoeBillNoToManyPaymentReferenceBD getBD()
  {
    if (bd == null) {
      bd = new BoeBillNoToManyPaymentReferenceBD();
    }
    return bd;
  }
  public ArrayList<BillTypeVO> fetchPayRef(BoeVO boeVO)
    throws BusinessException
  {
    logger.info("---------------fetchPayRef-----------------");
    BoeOfPriceReferenceToManyBillNoDAO boeDAO = null;
    ArrayList<BillTypeVO> eventRefList = null;
    try
    {
      boeDAO = BoeOfPriceReferenceToManyBillNoDAO.getDAO();
      eventRefList = boeDAO.fetchPayRefer(boeVO);
    }
    catch (Exception e)
    {
      logger.info("---------------fetchPayRef----------e-------" + e);
      e.printStackTrace();
    }
    logger.info("Exiting Method");
    return eventRefList;
  }
  public BoeVO retriveDataBasedOnBillNO(BoeVO boeVO)
    throws BusinessException
  {
    logger.info("Entering Method");
    BoeBillNoToManyPaymentReferenceDAO boeManyPayRefDAO = null;
    try
    {
      boeManyPayRefDAO = BoeBillNoToManyPaymentReferenceDAO.getDAO();
      if (boeManyPayRefDAO != null) {
        boeVO = boeManyPayRefDAO.retriveDataBasedOnBillNO(boeVO);
      }
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return boeVO;
  }
  public BoeVO fetchPaymentData(BoeVO boeVO, BOESearchVO searchVO)
    throws BusinessException
  {
    logger.info("-----------fetchPaymentData---------------");
    BoeBillNoToManyPaymentReferenceDAO boeManyPayRefDAO = null;
    try
    {
      boeManyPayRefDAO = BoeBillNoToManyPaymentReferenceDAO.getDAO();
      if (boeManyPayRefDAO != null) {
        boeVO = boeManyPayRefDAO.fetchPaymentData(boeVO, searchVO);
      }
    }
    catch (Exception exception)
    {
      logger.info("-----------fetchPaymentData---------exception------" + exception);
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return boeVO;
  }
  public BoeVO retriveBackManyBillData(BoeVO boeVO)
    throws BusinessException
  {
    logger.info("Entering Method");
    BoeBillNoToManyPaymentReferenceDAO boeManyPayRefDAO = null;
    try
    {
      boeManyPayRefDAO = BoeBillNoToManyPaymentReferenceDAO.getDAO();
      if (boeManyPayRefDAO != null) {
        boeVO = boeManyPayRefDAO.retriveBackManyBillData(boeVO);
      }
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return boeVO;
  }
  public String storeBillData(BoeVO boeVO, String[] chkPayList, String[] radioId)
    throws BusinessException
  {
    logger.info("Entering Method");
    BoeBillNoToManyPaymentReferenceDAO boeManyPayRefDAO = null;
    String result = null;
    try
    {
      logger.info("storeBillData--------------------");
      logger.info("storeBillData--------------------");
      boeManyPayRefDAO = BoeBillNoToManyPaymentReferenceDAO.getDAO();
      if (boeManyPayRefDAO != null)
      {
        logger.info("Inside ---------boeManyPayRefDAO not null-----------");
        logger.info("Inside ----------boeManyPayRefDAO not null----------");
        result = boeManyPayRefDAO.storeBillData(boeVO, chkPayList, radioId);
      }
    }
    catch (Exception exception)
    {
      logger.info("Store Bill Data Exception----------------->" + exception.getMessage());
      logger.info("Store Bill Data Exception----------------->" + exception.getMessage());
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return result;
  }
  public String storeBillData_1(BoeVO boeVO, String[] chkPayList, String[] chkInvoiceVal)
    throws BusinessException
  {
    logger.info("Entering Method");
    BoeBillNoToManyPaymentReferenceDAO boeManyPayRefDAO = null;
    String result = null;
    try
    {
      boeManyPayRefDAO = BoeBillNoToManyPaymentReferenceDAO.getDAO();
      if (boeManyPayRefDAO != null) {
        result = boeManyPayRefDAO.storeBillData_1(boeVO, chkPayList, chkInvoiceVal);
      }
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return result;
  }
  public BoeVO retriveDataFromMBPayTable(BoeVO boeVO)
  {
    logger.info("Entering Method");
    BoeBillNoToManyPaymentReferenceDAO boeManyBillDAO = null;
    try
    {
      boeManyBillDAO = BoeBillNoToManyPaymentReferenceDAO.getDAO();
      if (boeManyBillDAO != null) {
        boeManyBillDAO.retriveDataFromMBPayTable(boeVO);
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    logger.info("Exiting Method");
    return boeVO;
  }
  public int deleteBOEMBData(BoeVO boeVO, String[] chkPayList, String chkInvoiceVal)
  {
    logger.info("Entering Method");
    int iRet = 0;
    BoeBillNoToManyPaymentReferenceDAO boeManyBillDAO = null;
    try
    {
      boeManyBillDAO = BoeBillNoToManyPaymentReferenceDAO.getDAO();
      if (boeManyBillDAO != null) {
        boeManyBillDAO.deleteBOEMBData(boeVO, chkPayList, chkInvoiceVal);
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    logger.info("Exiting Method");
    return iRet;
  }
  public int checkPendingStatus(BoeVO boeVO)
  {
    logger.info("Entering Method");
    int iRet = 0;
    BoeBillNoToManyPaymentReferenceDAO boeManyBillDAO = null;
    try
    {
      boeManyBillDAO = BoeBillNoToManyPaymentReferenceDAO.getDAO();
      if (boeManyBillDAO != null) {
        iRet = boeManyBillDAO.checkPendingStatus(boeVO);
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    logger.info("Exiting Method");
    logger.info("The Return from BD : " + iRet);
    return iRet;
  }
}
