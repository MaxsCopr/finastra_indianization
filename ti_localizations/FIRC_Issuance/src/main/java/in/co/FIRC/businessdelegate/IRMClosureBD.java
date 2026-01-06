package in.co.FIRC.businessdelegate;

import in.co.FIRC.businessdelegate.exception.BusinessException;
import in.co.FIRC.dao.IRMClosureDAO;
import in.co.FIRC.vo.IRMClosureVO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
 
public class IRMClosureBD
  extends BaseBusinessDelegate
{
  private static Logger logger = LogManager.getLogger(IRMClosureBD.class.getName());
  static IRMClosureBD bd;
  public static IRMClosureBD getBD()
  {
    if (bd == null) {
      bd = new IRMClosureBD();
    }
    return bd;
  }
  public IRMClosureVO fetchIRMClosureData(IRMClosureVO irmAdjVO)
    throws BusinessException
  {
    logger.info("Entering Method");
    IRMClosureDAO dao = null;
    try
    {
      dao = new IRMClosureDAO();
      irmAdjVO = dao.fetchIRMClosureData(irmAdjVO);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return irmAdjVO;
  }
  public IRMClosureVO validateIRMClosureData(IRMClosureVO irmAdjVO)
    throws BusinessException
  {
    logger.info("Entering Method");
    IRMClosureDAO dao = null;
    try
    {
      dao = new IRMClosureDAO();
      irmAdjVO = dao.validateIRMClosureData(irmAdjVO);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return irmAdjVO;
  }
  public String storeIRMClosureData(IRMClosureVO irmAdjVO)
    throws BusinessException
  {
    logger.info("Entering Method");
    IRMClosureDAO dao = null;
    String result = null;
    try
    {
      dao = new IRMClosureDAO();
      result = dao.storeIRMClosureData(irmAdjVO);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return result;
  }
}
