package in.co.localization.businessdelegate.localization;
 
import in.co.localization.businessdelegate.BaseBusinessDelegate;
import in.co.localization.businessdelegate.exception.BusinessException;
import in.co.localization.dao.localization.BOEClosBulkUploadDAO;
import in.co.localization.vo.localization.BOEClosBulkUploadVO;
import in.co.localization.vo.localization.TransactionClosVO;
import org.apache.log4j.Logger;
 
public class BOEClosBulkUploadBD
  extends BaseBusinessDelegate
{
  private static Logger logger = Logger.getLogger(BOEClosBulkUploadBD.class.getName());
  static BOEClosBulkUploadBD bd;
  public static BOEClosBulkUploadBD getBD()
  {
    if (bd == null) {
      bd = new BOEClosBulkUploadBD();
    }
    return bd;
  }
  public TransactionClosVO getExcelValidate(BOEClosBulkUploadVO bulkVO)
    throws BusinessException
  {
    logger.info("Entering Method");
    BOEClosBulkUploadDAO dao = null;
    TransactionClosVO trans = null;
    try
    {
      dao = BOEClosBulkUploadDAO.getDAO();
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
    BOEClosBulkUploadDAO dao = null;
    int status = 0;
    try
    {
      dao = BOEClosBulkUploadDAO.getDAO();
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
  public int uploadData(String batchId)
    throws BusinessException
  {
    logger.info("This is inside of uploadData ===============>>>");
    logger.info("Entering Method");
    BOEClosBulkUploadDAO dao = null;
    int status = 0;
    try
    {
      dao = BOEClosBulkUploadDAO.getDAO();
      status = dao.uploadDataToTable(batchId);
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return status;
  }
}