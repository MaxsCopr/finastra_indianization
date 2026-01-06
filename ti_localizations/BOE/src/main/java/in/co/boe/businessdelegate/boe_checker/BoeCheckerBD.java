package in.co.boe.businessdelegate.boe_checker;

import in.co.boe.businessdelegate.BaseBusinessDelegate;
import in.co.boe.businessdelegate.exception.BusinessException;
import in.co.boe.dao.boe_checker.BoeCheckerDAO;
import in.co.boe.vo.BOEDataVO;
import in.co.boe.vo.BOESearchVO;
import in.co.boe.vo.BoeVO;
import in.co.boe.vo.TransactionVO;
import java.util.ArrayList;
import org.apache.log4j.Logger;
 
public class BoeCheckerBD
  extends BaseBusinessDelegate
{
  private static Logger logger = Logger.getLogger(BoeCheckerBD.class
    .getName());
  static BoeCheckerBD bd;
  public static BoeCheckerBD getBD()
  {
    if (bd == null) {
      bd = new BoeCheckerBD();
    }
    return bd;
  }
  public ArrayList<TransactionVO> loadMultiPaymentReferenceData(BOESearchVO boeSearchVO)
    throws BusinessException
  {
    logger.info("Welcome to Inside of loadMultiPaymentReferenceData() ===========>>> BD");
    logger.info("Entering Method");
    BoeCheckerDAO boeCheckerDAO = null;
    ArrayList<TransactionVO> multiPaymentReferenceList = null;
    try
    {
      multiPaymentReferenceList = new ArrayList();
      boeCheckerDAO = BoeCheckerDAO.getDAO();
      if (boeCheckerDAO != null) {
        multiPaymentReferenceList = boeCheckerDAO.loadMultiPaymentReferenceData(boeSearchVO);
      }
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return multiPaymentReferenceList;
  }
  public String updateStatus(String[] chkList, String check, String remarks)
    throws BusinessException
  {
    logger.info("Entering Method");
    BoeCheckerDAO boeCheckerDAO = null;
    String result = null;
    try
    {
      boeCheckerDAO = BoeCheckerDAO.getDAO();
      if (boeCheckerDAO != null) {
        result = boeCheckerDAO.updateStatus(chkList, check, remarks);
      }
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return result;
  }
  public BOEDataVO boeDataView(String boeNo, String boeDate, String portCode, String paymentRef, String paymentSlNo)
    throws BusinessException
  {
    logger.info("Entering Method");
    BoeCheckerDAO boeCheckerDAO = null;
    BOEDataVO boeDataVO = null;
    try
    {
      boeCheckerDAO = BoeCheckerDAO.getDAO();
      if (boeCheckerDAO != null) {
        boeDataVO = boeCheckerDAO.boeDataView(boeNo, boeDate, portCode, paymentRef, paymentSlNo);
      }
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return boeDataVO;
  }
  public ArrayList<TransactionVO> fetchInvoiecListChecker(String boeNo, String boeDate, String portCode, String paymentRef, String paymentSlNo)
  {
    logger.info("Entering Method");
    ArrayList<TransactionVO> list = null;
    BoeCheckerDAO boeCheckerDAO = null;
    try
    {
      boeCheckerDAO = BoeCheckerDAO.getDAO();
      list = boeCheckerDAO.fetchInvoiecListChecker(boeNo, boeDate, portCode, paymentRef, paymentSlNo);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    logger.info("Exiting Method");
    return list;
  }
  public ArrayList<BoeVO> fetchCheckerManualBOE(BOESearchVO boeSearchVO)
    throws BusinessException
  {
    logger.info("Entering Method");
    BoeCheckerDAO boeCheckerDAO = null;
    ArrayList<BoeVO> boeList = null;
    try
    {
      boeCheckerDAO = BoeCheckerDAO.getDAO();
      if (boeCheckerDAO != null) {
        boeList = boeCheckerDAO.fetchCheckerManualBOE(boeSearchVO);
      }
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return boeList;
  }
  public BoeVO getBoeDetails(String boeData)
  {
    logger.info("Entering Method");
    BoeCheckerDAO boeCheckerDAO = null;
    BoeVO boeVO = null;
    try
    {
      boeCheckerDAO = BoeCheckerDAO.getDAO();
      boeVO = boeCheckerDAO.getBoeDetails(boeData);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    logger.info("Exiting Method");
    return boeVO;
  }
  public void updateManualBOE(String[] manualchkList, String boeAuthorizeStatus, String remarks)
    throws BusinessException
  {
    logger.info("Entering Method");
    BoeCheckerDAO boeCheckerDAO = null;
    try
    {
      boeCheckerDAO = BoeCheckerDAO.getDAO();
      if (boeCheckerDAO != null) {
        boeCheckerDAO.updateManualBOE(manualchkList, boeAuthorizeStatus, remarks);
      }
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
  }
  public ArrayList<BoeVO> fetchManualBOESearch(BOESearchVO boeSearchVO)
    throws BusinessException
  {
    logger.info("Entering Method");
    BoeCheckerDAO boeCheckerDAO = null;
    ArrayList<BoeVO> boeList = null;
    try
    {
      boeCheckerDAO = BoeCheckerDAO.getDAO();
      if (boeCheckerDAO != null) {
        boeList = boeCheckerDAO.fetchManualBOESearch(boeSearchVO);
      }
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return boeList;
  }
  public BoeVO getManualBoeDetails(String boeData)
  {
    logger.info("Entering Method");
    BoeCheckerDAO boeCheckerDAO = null;
    BoeVO boeVO = null;
    try
    {
      boeCheckerDAO = BoeCheckerDAO.getDAO();
      boeVO = boeCheckerDAO.getBoeDetails(boeData);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    logger.info("Exiting Method");
    return boeVO;
  }
}