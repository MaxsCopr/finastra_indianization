package in.co.stp.businessdelegate;

import in.co.stp.action.MakerHomeAction;
import in.co.stp.businessdelegate.exception.BusinessException;
import in.co.stp.dao.CheckerHomeDAO;
import in.co.stp.utility.DataGridVO;
import in.co.stp.vo.CheckerHomeVO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
 
public class CheckerHomeBD
  extends BaseBusinessDelegate
{
  static CheckerHomeBD bd;
  private static Logger logger = LogManager.getLogger(MakerHomeAction.class
    .getName());
  public static CheckerHomeBD getBD()
  {
    if (bd == null) {
      bd = new CheckerHomeBD();
    }
    return bd;
  }
  public CheckerHomeVO fetchInvoiceDetails(CheckerHomeVO checkervo)
    throws BusinessException
  {
    logger.info("Entering Method");
    try
    {
      CheckerHomeDAO dao = CheckerHomeDAO.getDAO();
      checkervo = dao.fetchInvoiceDetails(checkervo);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return checkervo;
  }
  public void isSessionAvailable()
    throws BusinessException
  {
    logger.info("Entering Method");
    CheckerHomeDAO dao = null;
    try
    {
      dao = CheckerHomeDAO.getDAO();
      dao.isSessionAvailable();
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
  }
  public CheckerHomeVO retrieveTransactionList(CheckerHomeVO checkervo)
    throws BusinessException
  {
    logger.info("Entering Method");
    CheckerHomeDAO dao = null;
    try
    {
      dao = CheckerHomeDAO.getDAO();
      checkervo = dao.fetchInvoice(checkervo);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return checkervo;
  }
  public DataGridVO fetchInvoiceList(String invoiceAjaxListval, CheckerHomeVO checkervo, DataGridVO daVO)
    throws BusinessException
  {
    logger.info("Entering Method");
    CheckerHomeDAO dao = null;
    DataGridVO list = null;
    try
    {
      dao = CheckerHomeDAO.getDAO();
      list = dao.fetchInvoiceList(invoiceAjaxListval, checkervo, daVO);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return list;
  }
  public int checkerUpload(CheckerHomeVO checkervo)
    throws BusinessException
  {
    logger.info("Entering Method");
    CheckerHomeDAO dao = null;
    int count = 0;
    try
    {
      dao = CheckerHomeDAO.getDAO();
      count = dao.updateAllODC(checkervo);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return count;
  }
}
