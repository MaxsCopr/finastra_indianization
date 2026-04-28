package in.co.FIRC.businessdelegate;

import in.co.FIRC.businessdelegate.exception.BusinessException;
import in.co.FIRC.dao.FIRCIssuanceDAO;
import in.co.FIRC.vo.ourBank.CustomerDataVO;
import in.co.FIRC.vo.ourBank.IssuanceVO;
import in.co.FIRC.vo.ourBank.OurBankVO;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
 
public class FIRCIssuanceBD
  extends BaseBusinessDelegate
{
  private static Logger logger = LogManager.getLogger(FIRCIssuanceBD.class.getName());
  static FIRCIssuanceBD bd;
  public static FIRCIssuanceBD getBD()
  {
    if (bd == null) {
      bd = new FIRCIssuanceBD();
    }
    return bd;
  }
  public ArrayList<OurBankVO> getFircIssuance(IssuanceVO issuanceVO)
    throws BusinessException
  {
    logger.info("---------getFircIssuance--------------");
    FIRCIssuanceDAO ourBankDAO = null;
    ArrayList<OurBankVO> issuanceList = null;
    try
    {
      ourBankDAO = new FIRCIssuanceDAO();
      issuanceList = ourBankDAO.getFircIssuance(issuanceVO);
    }
    catch (Exception exception)
    {
      logger.info("---------getFircIssuance--------exception------" + exception);
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return issuanceList;
  }
  public ArrayList<CustomerDataVO> getCustomerList(ArrayList<CustomerDataVO> customerList)
  {
    logger.info("Entering Method");
    FIRCIssuanceDAO dao = null;
    try
    {
      dao = FIRCIssuanceDAO.getDAO();
      customerList = dao.customerSearch(customerList);
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return customerList;
  }
  public ArrayList<CustomerDataVO> filterCustomer(CustomerDataVO cusDataVo, ArrayList<CustomerDataVO> customerList)
  {
    logger.info("Entering Method");
    FIRCIssuanceDAO dao = null;
    try
    {
      dao = FIRCIssuanceDAO.getDAO();
      customerList = dao.filterCusList(customerList, cusDataVo);
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return customerList;
  }
  public ArrayList<OurBankVO> getIrmExSearch(IssuanceVO issuanceVO)
    throws BusinessException
  {
    logger.info("Entering Method");
    FIRCIssuanceDAO ourBankDAO = null;
    ArrayList<OurBankVO> irmExList = null;
    try
    {
      ourBankDAO = new FIRCIssuanceDAO();
      irmExList = ourBankDAO.getIrmExSearch(issuanceVO);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return irmExList;
  }
  public HashMap<String, String> getStatus(String trnsacNo)
  {
    logger.info("---------getStatus--------------");
    HashMap<String, String> createMap = new HashMap();
    FIRCIssuanceDAO ourBankDAO = null;
    try
    {
      ourBankDAO = new FIRCIssuanceDAO();
      createMap = ourBankDAO.getStatus(trnsacNo);
    }
    catch (Exception localException) {}
    return createMap;
  }
  public ArrayList<OurBankVO> getIrmClSearch(IssuanceVO issuanceVO)
    throws BusinessException
  {
    logger.info("Entering Method");
    FIRCIssuanceDAO ourBankDAO = null;
    ArrayList<OurBankVO> irmClList = null;
    try
    {
      ourBankDAO = new FIRCIssuanceDAO();
      irmClList = ourBankDAO.getIrmClSearch(issuanceVO);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return irmClList;
  }
  public ArrayList<OurBankVO> getIrmSearch(IssuanceVO issuanceVO)
    throws BusinessException
  {
    logger.info("Entering Method");
    FIRCIssuanceDAO ourBankDAO = null;
    ArrayList<OurBankVO> irmList = null;
    try
    {
      ourBankDAO = new FIRCIssuanceDAO();
      irmList = ourBankDAO.getIrmSearch(issuanceVO);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return irmList;
  }
  public ArrayList<OurBankVO> fecthIFSCSearch(IssuanceVO issuanceVO)
    throws BusinessException
  {
    logger.info("Entering Method");
    FIRCIssuanceDAO ourBankDAO = null;
    ArrayList<OurBankVO> ifscList = null;
    try
    {
      ourBankDAO = FIRCIssuanceDAO.getDAO();
      ifscList = ourBankDAO.fecthIFSCSearch(issuanceVO);
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return ifscList;
  }
}
