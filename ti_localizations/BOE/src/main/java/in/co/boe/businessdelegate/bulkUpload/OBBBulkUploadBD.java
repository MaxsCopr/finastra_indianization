package in.co.boe.businessdelegate.bulkUpload;

import in.co.boe.businessdelegate.BaseBusinessDelegate;
import in.co.boe.businessdelegate.exception.BusinessException;
import in.co.boe.dao.bulkUpload.OBBBulkUploadDAO;
import in.co.boe.vo.OBBBulkUploadVO;
import in.co.boe.vo.TransactionVO;
import org.apache.log4j.Logger;
 
public class OBBBulkUploadBD
  extends BaseBusinessDelegate
{
  private static Logger logger = Logger.getLogger(OBBBulkUploadBD.class.getName());
  static OBBBulkUploadBD bd;
  public static OBBBulkUploadBD getBD()
  {
    if (bd == null) {
      bd = new OBBBulkUploadBD();
    }
    return bd;
  }
  public TransactionVO getExcelValidate(OBBBulkUploadVO bulkVO)
    throws BusinessException
  {
    logger.info("Entering Method");
    OBBBulkUploadDAO dao = null;
    TransactionVO trans = null;
    try
    {
      dao = OBBBulkUploadDAO.getDAO();
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
    OBBBulkUploadDAO dao = null;
    int status = 0;
    try
    {
      dao = OBBBulkUploadDAO.getDAO();
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
    OBBBulkUploadDAO dao = null;
    int status = 0;
    try
    {
      dao = OBBBulkUploadDAO.getDAO();
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