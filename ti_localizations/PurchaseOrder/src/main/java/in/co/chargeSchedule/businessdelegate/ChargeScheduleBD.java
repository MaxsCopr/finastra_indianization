package in.co.chargeSchedule.businessdelegate;

import in.co.chargeSchedule.businessdelegate.exception.BusinessException;
import in.co.chargeSchedule.dao.ChargeScheduleDAO;
import in.co.chargeSchedule.vo.ChargeScheduleVO;
import in.co.chargeSchedule.vo.ChargeSelectionVO;
import in.co.chargeSchedule.vo.CustomerDataVO;
import in.co.chargeSchedule.vo.ProductSelectionVO;
import java.util.ArrayList;
import org.apache.log4j.Logger;

public class ChargeScheduleBD
  extends BaseBusinessDelegate
{
  private static Logger logger = Logger.getLogger(ChargeScheduleBD.class.getName());
  static ChargeScheduleBD bd;
  ChargeScheduleVO chargVO = null;
 
  public static ChargeScheduleBD getBD()
  {
    if (bd == null) {
      bd = new ChargeScheduleBD();
    }
    return bd;
  }
 
  public String getSessionUser(String userName)
  {
    logger.info("Entering Method");
    ChargeScheduleDAO dao = null;
    String sesID = "";
    try
    {
      dao = ChargeScheduleDAO.getDAO();
      sesID = dao.getSessionUserID(userName);
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return sesID;
  }
 
  public ArrayList<ChargeScheduleVO> loadMultiPaymentReferenceData(ChargeScheduleVO chargSchVO)
    throws BusinessException
  {
    logger.info("Entering Method");
    ChargeScheduleDAO chargDAO = null;
    ArrayList<ChargeScheduleVO> multiPaymentReferenceList = null;
    try
    {
      logger.info("entered BD");
      multiPaymentReferenceList = new ArrayList();
      chargDAO = ChargeScheduleDAO.getDAO();
      logger.info("Came Here");
      if (chargDAO != null)
      {
        logger.info("DAO COMING");
        multiPaymentReferenceList = chargDAO.loadMultiPaymentReferenceData(chargSchVO);
        logger.info("DAO EXITING");
      }
      else
      {
        logger.info("Eneterd else");
      }
    }
    catch (Exception exception)
    {
      logger.info("Exception in BD " + exception.getMessage());
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return multiPaymentReferenceList;
  }
 
  public ArrayList<ChargeScheduleVO> loadEnquiryProcess(ChargeScheduleVO chargSchVO)
    throws BusinessException
  {
    logger.info("Entering Method");
    ChargeScheduleDAO chargDAO = null;
    ArrayList<ChargeScheduleVO> multiPaymentReferenceList = null;
    try
    {
      multiPaymentReferenceList = new ArrayList();
      chargDAO = ChargeScheduleDAO.getDAO();
      if (chargDAO != null) {
        multiPaymentReferenceList = chargDAO.loadEnquiryProcessData(chargSchVO);
      } else {
        logger.info("Eneterd else");
      }
    }
    catch (Exception exception)
    {
      logger.info("Exception in BD " + exception.getMessage());
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return multiPaymentReferenceList;
  }
 
  public ArrayList<ChargeScheduleVO> loadFinanceProcess(String poNumber)
    throws BusinessException
  {
    logger.info("Entering Method");
    ChargeScheduleDAO chargDAO = null;
    ArrayList<ChargeScheduleVO> financeList = null;
    try
    {
      logger.info("entered BD");
      financeList = new ArrayList();
      chargDAO = ChargeScheduleDAO.getDAO();
      logger.info("Came Here");
      if (chargDAO != null)
      {
        logger.info("DAO COMING");
        financeList = chargDAO.loadFinanceData(poNumber);
        logger.info("DAO EXITING");
      }
      else
      {
        logger.info("Eneterd else");
      }
    }
    catch (Exception exception)
    {
      logger.info("Exception in BD " + exception.getMessage());
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return financeList;
  }
 
  public ChargeScheduleVO getEnquiryProcess(String poNo, String cifID)
    throws BusinessException
  {
    logger.info("Entering Method");
    ChargeScheduleDAO chargDAO = null;
    String result = null;
    try
    {
      logger.info("Entering into getEnquiryProcess chargeSchBD");
      this.chargVO = new ChargeScheduleVO();
      chargDAO = ChargeScheduleDAO.getDAO();
      this.chargVO = chargDAO.getEnquiryProcessDetails(poNo, cifID);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return this.chargVO;
  }
 
  public ChargeScheduleVO getCheckerProcess(String poNo, String cifID)
    throws BusinessException
  {
    logger.info("Entering Method");
    ChargeScheduleDAO chargDAO = null;
    String result = null;
    try
    {
      logger.info("Entering into get Checker Process chargeSchBD");
      this.chargVO = new ChargeScheduleVO();
      chargDAO = ChargeScheduleDAO.getDAO();
      this.chargVO = chargDAO.getCheckerProcessDetails(poNo, cifID);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return this.chargVO;
  }
 
  public ChargeScheduleVO approveSinglePO(ChargeScheduleVO chargVO)
    throws BusinessException
  {
    logger.info("Entering Method");
    ChargeScheduleDAO chargDAO = null;
    try
    {
      logger.info("Entering into get Checker Process chargeSchBD");
     
      chargDAO = ChargeScheduleDAO.getDAO();
      chargVO = chargDAO.approveSinglePODetails(chargVO);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return chargVO;
  }
 
  public ChargeScheduleVO rejectSinglePO(ChargeScheduleVO chargVO)
    throws BusinessException
  {
    logger.info("Entering Method");
    ChargeScheduleDAO chargDAO = null;
    try
    {
      logger.info("Entering into get Checker Process chargeSchBD");
     
      chargDAO = ChargeScheduleDAO.getDAO();
      chargVO = chargDAO.rejectSinglePODetails(chargVO);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return chargVO;
  }
 
  public String getRole(ChargeScheduleVO chargVO)
    throws BusinessException
  {
    logger.info("Entering Method");
    ChargeScheduleDAO chargDAO = null;
    String result = null;
    try
    {
      chargDAO = ChargeScheduleDAO.getDAO();
      result = chargDAO.getRole(chargVO);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return result;
  }
 
  public void getErrors(String errorCode, ChargeScheduleVO chargeVO)
  {
    logger.info("Entering Method");
    ChargeScheduleDAO dao = null;
    try
    {
      dao = ChargeScheduleDAO.getDAO();
      dao.getErrors(errorCode, chargeVO);
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
  }
 
  public ArrayList<CustomerDataVO> getCustomerList(ArrayList<CustomerDataVO> customerList)
  {
    logger.info("Entering Method");
    ChargeScheduleDAO dao = null;
    try
    {
      dao = ChargeScheduleDAO.getDAO();
      customerList = dao.customerSearch(customerList);
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return customerList;
  }
 
  public ArrayList<CustomerDataVO> fetchIncoTerms(ArrayList<CustomerDataVO> customerList)
  {
    logger.info("Entering Method");
    ChargeScheduleDAO dao = null;
    try
    {
      dao = ChargeScheduleDAO.getDAO();
      customerList = dao.fetchIncoTerms(customerList);
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return customerList;
  }
 
  public ArrayList<ChargeScheduleVO> getChargeScheduleList(ArrayList<ChargeScheduleVO> chargeScheduleList)
  {
    logger.info("Entering Method");
    ChargeScheduleDAO dao = null;
    try
    {
      dao = ChargeScheduleDAO.getDAO();
      chargeScheduleList = dao.chargeScheduleSearch(chargeScheduleList);
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return chargeScheduleList;
  }
 
  public ArrayList<CustomerDataVO> filterCustomer(CustomerDataVO cusDataVo, ArrayList<CustomerDataVO> customerList)
  {
    logger.info("Entering Method");
    ChargeScheduleDAO dao = null;
    try
    {
      dao = ChargeScheduleDAO.getDAO();
      customerList = dao.filterCusList(customerList, cusDataVo);
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return customerList;
  }
 
  public ArrayList<CustomerDataVO> filterIncoTerms(CustomerDataVO cusDataVo, ArrayList<CustomerDataVO> customerList)
  {
    logger.info("Entering Method");
    ChargeScheduleDAO dao = null;
    try
    {
      dao = ChargeScheduleDAO.getDAO();
      customerList = dao.filterIncoTerms(customerList, cusDataVo);
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return customerList;
  }
 
  public ArrayList<ChargeSelectionVO> getChargeList(ArrayList<ChargeSelectionVO> chargeList)
  {
    logger.info("Entering Method");
    ChargeScheduleDAO dao = null;
    try
    {
      dao = ChargeScheduleDAO.getDAO();
      chargeList = dao.chargeSearch(chargeList);
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return chargeList;
  }
 
  public String updateStatus(String[] chkList, String check, String remarks)
    throws BusinessException
  {
    logger.info("Entering Method");
    ChargeScheduleDAO chargDAO = null;
    try
    {
      chargDAO = ChargeScheduleDAO.getDAO();
      if (chargDAO != null) {
        chargDAO.updateStatus(chkList, check, remarks);
      }
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return "success";
  }
 
  public ArrayList<ChargeSelectionVO> filterChargeList(ChargeSelectionVO chargeSelectionVO, ArrayList<ChargeSelectionVO> chargeList)
  {
    logger.info("Entering Method");
    ChargeScheduleDAO dao = null;
    try
    {
      dao = ChargeScheduleDAO.getDAO();
      chargeList = dao.filterOfChargeList(chargeList, chargeSelectionVO);
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return chargeList;
  }
 
  public ArrayList<ProductSelectionVO> getProductList(ArrayList<ProductSelectionVO> productList)
  {
    logger.info("Entering Method");
    ChargeScheduleDAO dao = null;
    try
    {
      dao = ChargeScheduleDAO.getDAO();
      productList = dao.productSearch(productList);
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return productList;
  }
 
  public ArrayList<ProductSelectionVO> filterProductList(ProductSelectionVO productVo, ArrayList<ProductSelectionVO> productList)
  {
    logger.info("Entering Method");
    ChargeScheduleDAO dao = null;
    try
    {
      dao = ChargeScheduleDAO.getDAO();
      productList = dao.filterOfProductList(productList, productVo);
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return productList;
  }
 
  public ArrayList<ChargeScheduleVO> filterChargeScheduleList(ChargeScheduleVO chargeVO, ArrayList<ChargeScheduleVO> chargeScheduleList)
  {
    logger.info("Entering Method");
    ChargeScheduleDAO dao = null;
    try
    {
      dao = ChargeScheduleDAO.getDAO();
      chargeScheduleList = dao.filterOfSceduleList(chargeScheduleList, chargeVO);
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return chargeScheduleList;
  }
 
  public ArrayList<ChargeScheduleVO> createChargeSchedule(ChargeScheduleVO chargeVO, ArrayList<ChargeScheduleVO> chargeScheduleList)
  {
    logger.info("Entering Method");
    ChargeScheduleDAO dao = null;
    try
    {
      dao = ChargeScheduleDAO.getDAO();
      chargeScheduleList = dao.createNewChargeSchedule(chargeScheduleList, chargeVO);
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return chargeScheduleList;
  }
 
  public ArrayList<ChargeScheduleVO> updateChargeSchedule(ChargeScheduleVO chargeVO, ArrayList<ChargeScheduleVO> chargeScheduleList)
  {
    logger.info("Entering Method");
    ChargeScheduleDAO dao = null;
    try
    {
      dao = ChargeScheduleDAO.getDAO();
      chargeScheduleList = dao.updatingChargeSchedule(chargeScheduleList, chargeVO);
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return chargeScheduleList;
  }
 
  public ArrayList<ChargeScheduleVO> delChargeSchedule(ChargeScheduleVO chargeVO, ArrayList<ChargeScheduleVO> chargeScheduleList)
  {
    logger.info("Entering Method");
    ChargeScheduleDAO dao = null;
    try
    {
      dao = ChargeScheduleDAO.getDAO();
      chargeScheduleList = dao.deletingChargeSchedule(chargeScheduleList, chargeVO);
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return chargeScheduleList;
  }
 
  public ChargeScheduleVO insertExport(ChargeScheduleVO chargeVO)
  {
    ChargeScheduleDAO dao = null;
    try
    {
      dao = ChargeScheduleDAO.getDAO();
      dao.insertExport(chargeVO);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return chargeVO;
  }
 
  public ChargeScheduleVO fetchvalues(ChargeScheduleVO chargeVO)
  {
    ChargeScheduleDAO dao = null;
    try
    {
      dao = ChargeScheduleDAO.getDAO();
      chargeVO = dao.fetchVal(chargeVO);
    }
    catch (Exception e)
    {
      logger.info("Exception is " + e.getMessage());
    }
    return chargeVO;
  }
 
  public ChargeScheduleVO fetchBeneficiary(ChargeScheduleVO chargeVO)
  {
    ChargeScheduleDAO dao = null;
    try
    {
      dao = ChargeScheduleDAO.getDAO();
      chargeVO = dao.fetchBeneficiaryName(chargeVO);
    }
    catch (Exception e)
    {
      logger.info("Exception is " + e.getMessage());
    }
    return chargeVO;
  }
 
  public ChargeScheduleVO fetchPurchaseOrder(ChargeScheduleVO chargeVO)
  {
    ChargeScheduleDAO dao = null;
    try
    {
      dao = ChargeScheduleDAO.getDAO();
      chargeVO = dao.fetchPurchaseOrder(chargeVO);
    }
    catch (Exception e)
    {
      logger.info("Exception is " + e.getMessage());
    }
    return chargeVO;
  }
 
  public ChargeScheduleVO searchPurchaseDetails(ChargeScheduleVO chargeVO)
  {
    ChargeScheduleDAO dao = null;
    try
    {
      dao = ChargeScheduleDAO.getDAO();
      chargeVO = dao.searchPurchaseOrderDetails(chargeVO);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return chargeVO;
  }
 
  public ArrayList<CustomerDataVO> searchGoodsCode(CustomerDataVO cusDataVo)
  {
    logger.info("Entering Method");
    ChargeScheduleDAO dao = null;
    ArrayList<CustomerDataVO> goodsList = null;
    try
    {
      goodsList = new ArrayList();
      dao = ChargeScheduleDAO.getDAO();
      goodsList = dao.searchGoodsCode(cusDataVo);
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return goodsList;
  }
 
  public ArrayList<CustomerDataVO> fetchInwardDetails(CustomerDataVO cusDataVo)
  {
    logger.info("Entering Method");
    ChargeScheduleDAO dao = null;
    ArrayList<CustomerDataVO> inwardList = null;
    try
    {
      inwardList = new ArrayList();
      dao = ChargeScheduleDAO.getDAO();
      inwardList = dao.fetchInwardDetails(cusDataVo);
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return inwardList;
  }
 
  public ArrayList<CustomerDataVO> getCountryList()
  {
    logger.info("Entering Method");
    ChargeScheduleDAO dao = null;
    ArrayList<CustomerDataVO> countrylist = null;
    try
    {
      dao = ChargeScheduleDAO.getDAO();
      countrylist = dao.fetchcountryList();
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return countrylist;
  }
 
  public ChargeScheduleVO setMasterDetails(ChargeScheduleVO chargeVO, String masRef)
  {
    logger.info("Entering Method");
    ChargeScheduleDAO dao = null;
    try
    {
      dao = ChargeScheduleDAO.getDAO();
      chargeVO = dao.setMasterDetails(chargeVO, masRef);
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return chargeVO;
  }
 
  public ChargeScheduleVO purchaseOrderValidations(ChargeScheduleVO chargeVO)
  {
    logger.info("Entering Method");
    ChargeScheduleDAO dao = null;
    try
    {
      dao = ChargeScheduleDAO.getDAO();
      chargeVO = dao.purchaseOrderValidations(chargeVO);
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return chargeVO;
  }
 
  public void setProcessDate()
  {
    logger.info("Entering Method");
    ChargeScheduleDAO dao = null;
    try
    {
      dao = ChargeScheduleDAO.getDAO();
      dao.getTICurrentDate();
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
  }
 
  public String checkLoginedUserType(String username)
    throws BusinessException
  {
    logger.info("Entering Method");
    ChargeScheduleDAO boeHomeDAO = null;
    String result = null;
    try
    {
      boeHomeDAO = ChargeScheduleDAO.getDAO();
      result = boeHomeDAO.checkLoginedUserType(username);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return result;
  }
}