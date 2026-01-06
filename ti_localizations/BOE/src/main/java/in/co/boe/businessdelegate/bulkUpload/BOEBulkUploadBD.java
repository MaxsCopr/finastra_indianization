package in.co.boe.businessdelegate.bulkUpload;

import in.co.boe.businessdelegate.BaseBusinessDelegate;
import in.co.boe.businessdelegate.exception.BusinessException;
import in.co.boe.dao.bulkUpload.BOEBulkUploadDAO;
import in.co.boe.vo.BOEBulkUploadVO;
import in.co.boe.vo.TransactionBoeBlkVO;
import org.apache.log4j.Logger;
 
public class BOEBulkUploadBD
  extends BaseBusinessDelegate
{
  private static Logger logger = Logger.getLogger(BOEBulkUploadBD.class.getName());
  static BOEBulkUploadBD bd;
  public static BOEBulkUploadBD getBD()
  {
    if (bd == null) {
      bd = new BOEBulkUploadBD();
    }
    return bd;
  }
  public TransactionBoeBlkVO getExcelValidate(BOEBulkUploadVO bulkVO)
    throws BusinessException
  {
    logger.info("Entering Method");
    BOEBulkUploadDAO dao = null;
    TransactionBoeBlkVO trans = null;
    try
    {
      dao = BOEBulkUploadDAO.getDAO();
      trans = dao.getExcelValidate(bulkVO);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return trans;
  }
  public int rejectData(String batchId)
    throws BusinessException
  {
    logger.info("Entering Method");
    BOEBulkUploadDAO dao = null;
    int status = 0;
    try
    {
      dao = BOEBulkUploadDAO.getDAO();
      status = dao.rejectData(batchId);
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return status;
  }
  public TransactionBoeBlkVO uploadData(String batchId)
    throws BusinessException
  {
    logger.info("Entering Method");
    BOEBulkUploadDAO dao = null;
    TransactionBoeBlkVO trans = null;
    int status = 0;
    try
    {
      trans = new TransactionBoeBlkVO();
      dao = BOEBulkUploadDAO.getDAO();
      trans = dao.uploadDataToTable(batchId);
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return trans;
  }
}
