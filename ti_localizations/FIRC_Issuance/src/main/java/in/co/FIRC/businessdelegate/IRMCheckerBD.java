package in.co.FIRC.businessdelegate;

import in.co.FIRC.businessdelegate.exception.BusinessException;
import in.co.FIRC.dao.IRMCheckerDAO;
import in.co.FIRC.vo.IRMClosureVO;
import in.co.FIRC.vo.IRMVO;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
 
public class IRMCheckerBD
  extends BaseBusinessDelegate
{
  private static Logger logger = LogManager.getLogger(IRMCheckerBD.class
    .getName());
  static IRMCheckerBD bd;
  public static IRMCheckerBD getBD()
  {
    if (bd == null) {
      bd = new IRMCheckerBD();
    }
    return bd;
  }
  public ArrayList<IRMVO> fetchIRMExtensionData(IRMVO irmvo)
    throws BusinessException
  {
    logger.info("Entering Method");
    IRMCheckerDAO irmCheckerDAO = null;
    ArrayList<IRMVO> extensionList = null;
    try
    {
      extensionList = new ArrayList();
      irmCheckerDAO = IRMCheckerDAO.getDAO();
      if (irmCheckerDAO != null) {
        extensionList = irmCheckerDAO.fetchIRMExtensionData(irmvo);
      }
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return extensionList;
  }
  public String updateIRMExtensionStatus(String[] chkList, String check, String remarks)
    throws BusinessException
  {
    logger.info("Entering Method");
    IRMCheckerDAO irmCheckerDAO = null;
    String result = null;
    try
    {
      irmCheckerDAO = IRMCheckerDAO.getDAO();
      if (irmCheckerDAO != null) {
        result = irmCheckerDAO.updateIRMExtensionStatus(chkList, check, remarks);
      }
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return result;
  }
  public ArrayList<IRMClosureVO> fetchIrmClosureData(IRMClosureVO irmAdjVO)
    throws BusinessException
  {
    logger.info("Entering Method");
    IRMCheckerDAO irmCheckerDAO = null;
    ArrayList<IRMClosureVO> closureList = null;
    try
    {
      closureList = new ArrayList();
      irmCheckerDAO = IRMCheckerDAO.getDAO();
      if (irmCheckerDAO != null) {
        closureList = irmCheckerDAO.fetchIrmClosureData(irmAdjVO);
      }
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return closureList;
  }
  public String updateClosureCheckerStatus(String[] chkList, String check, String remarks)
    throws BusinessException
  {
    logger.info("Entering Method");
    IRMCheckerDAO irmCheckerDAO = null;
    String result = null;
    try
    {
      irmCheckerDAO = IRMCheckerDAO.getDAO();
      if (irmCheckerDAO != null) {
        result = irmCheckerDAO.updateClosureCheckerStatus(chkList, check, remarks);
      }
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return result;
  }
}