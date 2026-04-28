package in.co.FIRC.businessdelegate;

import in.co.FIRC.businessdelegate.exception.BusinessException;
import in.co.FIRC.dao.IrmDAO;
import in.co.FIRC.vo.IRMVO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
 
public class IrmBD
  extends BaseBusinessDelegate
{
  private static Logger logger = LogManager.getLogger(IrmBD.class.getName());
  static IrmBD bd;
  public static IrmBD getBD()
  {
    if (bd == null) {
      bd = new IrmBD();
    }
    return bd;
  }
  public IRMVO fetchIRMData(IRMVO irmExVo)
    throws BusinessException
  {
    logger.info("Entering Method");
    IrmDAO dao = null;
    try
    {
      dao = new IrmDAO();
      irmExVo = dao.fetchIRMData(irmExVo);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return irmExVo;
  }
  public IRMVO validateIRMExData(IRMVO irmExVo)
    throws BusinessException
  {
    logger.info("Entering Method");
    IrmDAO dao = null;
    try
    {
      dao = new IrmDAO();
      irmExVo = dao.validateIRMExData(irmExVo);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return irmExVo;
  }
  public String storeIRMExData(IRMVO irmExVo)
    throws BusinessException
  {
    logger.info("Entering Method");
    IrmDAO dao = null;
    String result = null;
    try
    {
      dao = new IrmDAO();
      result = dao.storeIRMExData(irmExVo);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return result;
  }
}
