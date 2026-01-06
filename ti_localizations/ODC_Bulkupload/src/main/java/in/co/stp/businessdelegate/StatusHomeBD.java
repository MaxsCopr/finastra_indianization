package in.co.stp.businessdelegate;

import in.co.stp.action.MakerHomeAction;
import in.co.stp.businessdelegate.exception.BusinessException;
import in.co.stp.dao.StatusHomeDAO;
import in.co.stp.vo.ExcelDataVO;
import in.co.stp.vo.StatusHomeVO;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
 
public class StatusHomeBD
  extends BaseBusinessDelegate
{
  static StatusHomeBD bd;
  private static Logger logger = LogManager.getLogger(MakerHomeAction.class
    .getName());
  public static StatusHomeBD getBD()
  {
    if (bd == null) {
      bd = new StatusHomeBD();
    }
    return bd;
  }
  public StatusHomeVO fetchInvoiceDetails(StatusHomeVO statusvo)
    throws BusinessException
  {
    logger.info("Entering Method");
    try
    {
      StatusHomeDAO dao = StatusHomeDAO.getDAO();
      statusvo = dao.fetchInvoiceDetails(statusvo);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return statusvo;
  }
  public void isSessionAvailable()
    throws BusinessException
  {
    logger.info("Entering Method");
    StatusHomeDAO dao = null;
    try
    {
      dao = StatusHomeDAO.getDAO();
      dao.isSessionAvailable();
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
  }
  public StatusHomeVO retrieveTransactionList(StatusHomeVO statusvo)
    throws BusinessException
  {
    logger.info("Entering Method");
    StatusHomeDAO dao = null;
    try
    {
      dao = StatusHomeDAO.getDAO();
      statusvo = dao.fetchInvoice(statusvo);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return statusvo;
  }
  public ArrayList<ExcelDataVO> fetchInvoiceList(String invoiceAjaxListval, StatusHomeVO statusvo)
    throws BusinessException
  {
    logger.info("Entering Method");

 
    ArrayList<ExcelDataVO> list = null;
    try
    {
      StatusHomeDAO dao = StatusHomeDAO.getDAO();
      list = dao.fetchInvoiceList(invoiceAjaxListval, statusvo);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return list;
  }
}
