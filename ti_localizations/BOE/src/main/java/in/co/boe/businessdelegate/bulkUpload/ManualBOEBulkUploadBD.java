package in.co.boe.businessdelegate.bulkUpload;

import in.co.boe.businessdelegate.BaseBusinessDelegate;
import in.co.boe.businessdelegate.exception.BusinessException;
import in.co.boe.dao.bulkUpload.ManualBOEBulkUploadDAO;
import in.co.boe.vo.ManualBOEBulkUploadVO;
import in.co.boe.vo.TransactionVO;
import org.apache.log4j.Logger;
 
public class ManualBOEBulkUploadBD
  extends BaseBusinessDelegate
{
  private static Logger logger = Logger.getLogger(ManualBOEBulkUploadBD.class.getName());
  static ManualBOEBulkUploadBD bd;
  public static ManualBOEBulkUploadBD getBD()
  {
    if (bd == null) {
      bd = new ManualBOEBulkUploadBD();
    }
    return bd;
  }
  public TransactionVO getExcelValidate(ManualBOEBulkUploadVO bulkVO)
    throws BusinessException
  {
    logger.info("Entering Method");
    ManualBOEBulkUploadDAO dao = null;
    TransactionVO trans = null;
    try
    {
      dao = ManualBOEBulkUploadDAO.getDAO();
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
    ManualBOEBulkUploadDAO dao = null;
    int status = 0;
    try
    {
      dao = ManualBOEBulkUploadDAO.getDAO();
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
    logger.info("Entering Method");
    ManualBOEBulkUploadDAO dao = null;
    int status = 0;
    try
    {
      dao = ManualBOEBulkUploadDAO.getDAO();
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
