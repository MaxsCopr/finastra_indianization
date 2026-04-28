package in.co.FIRC.businessdelegate;

import in.co.FIRC.businessdelegate.exception.BusinessException;
import in.co.FIRC.dao.FIRCCheckerDAO;
import in.co.FIRC.utility.ActionConstants;
import in.co.FIRC.utility.ActionConstantsQuery;
import in.co.FIRC.vo.ourBank.IssuanceVO;
import in.co.FIRC.vo.ourBank.OurBankVO;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
 
public class FIRCCheckerBD
  extends BaseBusinessDelegate
  implements ActionConstants, ActionConstantsQuery
{
  private static Logger logger = LogManager.getLogger(FIRCCheckerBD.class
    .getName());
  static FIRCCheckerBD bd;
  public static FIRCCheckerBD getBD()
  {
    if (bd == null) {
      bd = new FIRCCheckerBD();
    }
    return bd;
  }
  public ArrayList<OurBankVO> fetchPendingOurBankDetails(IssuanceVO issuanceVO)
    throws BusinessException
  {
    logger.info("Entering Method");
    FIRCCheckerDAO fircCheckerDAO = null;
    ArrayList<OurBankVO> issuanceList = null;
    try
    {
      fircCheckerDAO = new FIRCCheckerDAO();
      issuanceList = fircCheckerDAO.fetchPendingOurBankDetails(issuanceVO);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return issuanceList;
  }
  public String updateStatus(String[] chkList, String check, String remarks)
    throws BusinessException
  {
    logger.info("-----------updateStatus------------");
    FIRCCheckerDAO fircCheckerDAO = null;
    String result = null;
    try
    {
      fircCheckerDAO = FIRCCheckerDAO.getDAO();
      if (fircCheckerDAO != null) {
        result = fircCheckerDAO.updateStatus(chkList, check, remarks);
      }
    }
    catch (Exception exception)
    {
      logger.info("-----------updateStatus--------exception----" + exception);
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return result;
  }
}
