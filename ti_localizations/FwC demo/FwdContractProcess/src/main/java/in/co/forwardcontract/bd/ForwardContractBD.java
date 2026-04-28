package in.co.forwardcontract.bd;

import in.co.forwardcontract.bd.exception.BusinessException;
import in.co.forwardcontract.dao.ForwardContractDAO;
import in.co.forwardcontract.utility.ActionConstants;
import in.co.forwardcontract.vo.ForwardContractVO;
import in.co.forwardcontract.vo.StaticDataVO;
import java.util.ArrayList;
import org.apache.log4j.Logger;

public class ForwardContractBD
  extends BaseBusinessDelegate
  implements ActionConstants
{
  private static Logger logger = Logger.getLogger(ForwardContractBD.class.getName());
  static ForwardContractBD bd;
  ForwardContractVO fwdContractVO = null;
 
  public static ForwardContractBD getBD()
  {
    if (bd == null) {
      bd = new ForwardContractBD();
    }
    return bd;
  }
 
  public String getSessionUser(String userName)
  {
    logger.info("Entering Method");
    ForwardContractDAO dao = null;
    String sesID = "";
    try
    {
      dao = ForwardContractDAO.getDAO();
      sesID = dao.getSessionUserID(userName);
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return sesID;
  }
 
  public ForwardContractVO fetchDependentTreasuryDetails(ForwardContractVO fwdContractVO)
    throws BusinessException
  {
    logger.info("Entering Method");
    ForwardContractDAO fwdContractDAO = null;
    try
    {
      fwdContractDAO = ForwardContractDAO.getDAO();
      fwdContractVO = fwdContractDAO.fetchDependentTreasuryDetails(fwdContractVO);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return fwdContractVO;
  }
 
  public ForwardContractVO fetchDependentCancelTreasuryDetails(ForwardContractVO fwdContractVO)
    throws BusinessException
  {
    logger.info("Entering Method");
    ForwardContractDAO fwdContractDAO = null;
    try
    {
      fwdContractDAO = ForwardContractDAO.getDAO();
      fwdContractVO = fwdContractDAO.fetchDependentCancelTreasuryDetails(fwdContractVO);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return fwdContractVO;
  }
 
  public ForwardContractVO fetchParticularFwdContractDetails(String id, String fwdContractNo)
    throws BusinessException
  {
    logger.info("Entering Method");
    ForwardContractDAO fwdContractDAO = null;
    try
    {
      this.fwdContractVO = new ForwardContractVO();
      fwdContractDAO = ForwardContractDAO.getDAO();
      this.fwdContractVO = fwdContractDAO.fetchParticularFwdContractDetails(id, fwdContractNo);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return this.fwdContractVO;
  }
 
  public ForwardContractVO fetchParticularFwdContractDetailstoModify(String id, String fwdContractNo)
    throws BusinessException
  {
    logger.info("Entering Method");
    ForwardContractDAO fwdContractDAO = null;
    try
    {
      this.fwdContractVO = new ForwardContractVO();
      fwdContractDAO = ForwardContractDAO.getDAO();
      this.fwdContractVO = fwdContractDAO.fetchParticularFwdContractDetailstoModify(id, fwdContractNo);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return this.fwdContractVO;
  }
 
  public ForwardContractVO fetchParticularCancelFwdContractDetails(String id, String fwdContractNo)
    throws BusinessException
  {
    logger.info("Entering Method");
    ForwardContractDAO fwdContractDAO = null;
    try
    {
      this.fwdContractVO = new ForwardContractVO();
      fwdContractDAO = ForwardContractDAO.getDAO();
      this.fwdContractVO = fwdContractDAO.fetchParticularCancelFwdContractDetails(id, fwdContractNo);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return this.fwdContractVO;
  }
 
  public ForwardContractVO fetchFWCReferenceDetails(String fwdContractNo)
    throws BusinessException
  {
    logger.info("Entering Method");
    ForwardContractDAO fwdContractDAO = null;
    try
    {
      this.fwdContractVO = new ForwardContractVO();
      fwdContractDAO = ForwardContractDAO.getDAO();
      this.fwdContractVO = fwdContractDAO.fetchFWCReferenceDetails(fwdContractNo);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return this.fwdContractVO;
  }
 
  public ForwardContractVO approveFWC(ForwardContractVO fwdContractVO, String category)
    throws BusinessException
  {
    logger.info("Entering Method");
    ForwardContractDAO fwdContractDAO = null;
    try
    {
      fwdContractDAO = ForwardContractDAO.getDAO();
      fwdContractVO = fwdContractDAO.approveFwdContractDetails(fwdContractVO, category);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return fwdContractVO;
  }
 
  public ForwardContractVO rejectFWC(ForwardContractVO fwdContractVO, String category)
    throws BusinessException
  {
    logger.info("Entering Method");
    ForwardContractDAO fwdContractDAO = null;
    try
    {
      fwdContractDAO = ForwardContractDAO.getDAO();
      fwdContractVO = fwdContractDAO.rejectFwdContractDetails(fwdContractVO, category);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return fwdContractVO;
  }
 
  public ForwardContractVO deleteFWC(ForwardContractVO fwdContractVO, String category)
    throws BusinessException
  {
    logger.info("Entering Method");
    ForwardContractDAO fwdContractDAO = null;
    try
    {
      fwdContractDAO = ForwardContractDAO.getDAO();
      fwdContractVO = fwdContractDAO.deleteFwdContractDetails(fwdContractVO, category);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return fwdContractVO;
  }
 
  public String getRole(ForwardContractVO fwdContractVO)
    throws BusinessException
  {
    logger.info("Entering Method");
    ForwardContractDAO fwdContractDAO = null;
    String result = null;
    try
    {
      fwdContractDAO = ForwardContractDAO.getDAO();
      result = fwdContractDAO.getRole(fwdContractVO);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return result;
  }
 
  public int checkLoginedUserType(ForwardContractVO fwdContractVO)
    throws BusinessException
  {
    logger.info("Entering Method");
    ForwardContractDAO fwdContractDAO = null;
    int result = 0;
    try
    {
      fwdContractDAO = ForwardContractDAO.getDAO();
      result = fwdContractDAO.checkLoginedUserType(fwdContractVO);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return result;
  }
 
  public int checkLoginedUserType1(String user, String teamName)
    throws BusinessException
  {
    logger.info("Entering Method");
    ForwardContractDAO fwdContractDAO = null;
    int result = 0;
    try
    {
      fwdContractDAO = ForwardContractDAO.getDAO();
      result = fwdContractDAO.checkLoginedUserType1(user, teamName);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return result;
  }
 
  public ArrayList<StaticDataVO> getCustomerList(ArrayList<StaticDataVO> customerList)
  {
    logger.info("Entering Method");
    ForwardContractDAO dao = null;
    try
    {
      dao = ForwardContractDAO.getDAO();
      customerList = dao.customerSearch(customerList);
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return customerList;
  }
 
  public ArrayList<StaticDataVO> getAccountList(ArrayList<StaticDataVO> accountList)
  {
    logger.info("Entering Method");
    ForwardContractDAO dao = null;
    try
    {
      dao = ForwardContractDAO.getDAO();
      accountList = dao.accountSearch(accountList);
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return accountList;
  }
 
  public ArrayList<StaticDataVO> getBranchList(ArrayList<StaticDataVO> accountList)
  {
    logger.info("Entering Method");
    ForwardContractDAO dao = null;
    try
    {
      dao = ForwardContractDAO.getDAO();
      accountList = dao.branchSearch(accountList);
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return accountList;
  }
 
  public ArrayList<StaticDataVO> getCurrencyList(ArrayList<StaticDataVO> accountList)
  {
    logger.info("Entering Method");
    ForwardContractDAO dao = null;
    try
    {
      dao = ForwardContractDAO.getDAO();
      accountList = dao.currencySearch(accountList);
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return accountList;
  }
 
  public ArrayList<StaticDataVO> getTreasuryList(ArrayList<StaticDataVO> treasuryList)
  {
    logger.info("Entering Method");
    ForwardContractDAO dao = null;
    try
    {
      dao = ForwardContractDAO.getDAO();
      treasuryList = dao.fetchTreasuryDetails(treasuryList);
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return treasuryList;
  }
 
  public ArrayList<StaticDataVO> fetchFwdContractList(ArrayList<StaticDataVO> fwdContractList)
  {
    logger.info("Entering Method");
    ForwardContractDAO dao = null;
    try
    {
      dao = ForwardContractDAO.getDAO();
      fwdContractList = dao.fetchFwdContractList(fwdContractList);
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return fwdContractList;
  }
 
  public ArrayList<StaticDataVO> getLimitList(String customerID)
  {
    logger.info("Entering Method");
    ForwardContractDAO dao = null;
    ArrayList<StaticDataVO> limitList = new ArrayList();
    try
    {
      dao = ForwardContractDAO.getDAO();
      limitList = dao.fetchLimitDetails(customerID);
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return limitList;
  }
 
  public ArrayList<StaticDataVO> filterCustomer(StaticDataVO cusDataVo, ArrayList<StaticDataVO> customerList)
  {
    logger.info("Entering Method");
    ForwardContractDAO dao = null;
    try
    {
      dao = ForwardContractDAO.getDAO();
      customerList = dao.filterCusList(customerList, cusDataVo);
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return customerList;
  }
 
  public ArrayList<StaticDataVO> filterAccount(StaticDataVO acctDataVO, ArrayList<StaticDataVO> accountList)
  {
    logger.info("Entering Method");
    ForwardContractDAO dao = null;
    try
    {
      dao = ForwardContractDAO.getDAO();
      accountList = dao.filterAcctList(accountList, acctDataVO);
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return accountList;
  }
 
  public ArrayList<StaticDataVO> filterBranch(StaticDataVO acctDataVO, ArrayList<StaticDataVO> accountList)
  {
    logger.info("Entering Method");
    ForwardContractDAO dao = null;
    try
    {
      dao = ForwardContractDAO.getDAO();
      accountList = dao.filterBranchList(accountList, acctDataVO);
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return accountList;
  }
 
  public ArrayList<StaticDataVO> filterCurrency(StaticDataVO acctDataVO, ArrayList<StaticDataVO> accountList)
  {
    logger.info("Entering Method");
    ForwardContractDAO dao = null;
    try
    {
      dao = ForwardContractDAO.getDAO();
      accountList = dao.filterCurrencyList(accountList, acctDataVO);
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return accountList;
  }
 
  public ArrayList<StaticDataVO> filterTreasuryDetails(StaticDataVO treasuryDataVO, ArrayList<StaticDataVO> treasuryList)
  {
    logger.info("Entering Method");
    ForwardContractDAO dao = null;
    try
    {
      dao = ForwardContractDAO.getDAO();
      treasuryList = dao.filterTreasuryList(treasuryDataVO, treasuryList);
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return treasuryList;
  }
 
  public ArrayList<StaticDataVO> filterfwdContractDetails(StaticDataVO fwdContractData, ArrayList<StaticDataVO> treasuryList)
  {
    logger.info("Entering Method");
    ForwardContractDAO dao = null;
    try
    {
      dao = ForwardContractDAO.getDAO();
      treasuryList = dao.filterFwdContractList(fwdContractData, treasuryList);
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return treasuryList;
  }
 
  public ForwardContractVO saveBookingDetails(ForwardContractVO fwdContractVO)
  {
    ForwardContractDAO dao = null;
    try
    {
      dao = ForwardContractDAO.getDAO();
      dao.saveBookingDetails(fwdContractVO);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return fwdContractVO;
  }
 
  public ForwardContractVO insertBookingDetails(ForwardContractVO fwdContractVO, String category, String status, String action)
  {
    ForwardContractDAO dao = null;
    try
    {
      dao = ForwardContractDAO.getDAO();
      dao.insertBookingDetails(fwdContractVO, category, status, action);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return fwdContractVO;
  }
 
  public ForwardContractVO updateFwdBookingContractDetails(ForwardContractVO fwdContractVO, String category, String status, String action)
  {
    ForwardContractDAO dao = null;
    try
    {
      dao = ForwardContractDAO.getDAO();
      dao.updateFwdBookingContractDetails(fwdContractVO, category, status, action);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return fwdContractVO;
  }
 
  public ForwardContractVO updateFwdCancelContractDetails(ForwardContractVO fwdContractVO, String category, String status, String action)
  {
    ForwardContractDAO dao = null;
    try
    {
      dao = ForwardContractDAO.getDAO();
      dao.updateFwdCancelContractDetails(fwdContractVO, category, status, action);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return fwdContractVO;
  }
 
  public ForwardContractVO validateBookingDetails(ForwardContractVO fwdContractVO)
  {
    logger.info("Entering Method");
    ForwardContractDAO dao = null;
    try
    {
      dao = ForwardContractDAO.getDAO();
      fwdContractVO = dao.validateBookingDetails(fwdContractVO);
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return fwdContractVO;
  }
 
  public ForwardContractVO generateFWCPostings(ForwardContractVO fwdContractVO)
  {
    logger.info("Entering Method");
    ForwardContractDAO dao = null;
    try
    {
      dao = ForwardContractDAO.getDAO();
      fwdContractVO = dao.generateFWCPostings(fwdContractVO);
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return fwdContractVO;
  }
 
  public ForwardContractVO getFWCPostingsToReverse(ForwardContractVO fwdContractVO)
  {
    logger.info("Entering Method");
    ForwardContractDAO dao = null;
    try
    {
      dao = ForwardContractDAO.getDAO();
      fwdContractVO = dao.getFWCPostingsToReverse(fwdContractVO);
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return fwdContractVO;
  }
 
  public ForwardContractVO cancelBookingDetails(ForwardContractVO fwdContractVO, String category)
    throws BusinessException
  {
    logger.info("Entering Method");
    ForwardContractDAO fwdContractDAO = null;
    try
    {
      fwdContractDAO = ForwardContractDAO.getDAO();
      logger.info("Cancel Booking Details");
      fwdContractVO = fwdContractDAO.cancelFwdContractDetails(fwdContractVO, category);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return fwdContractVO;
  }
 
  public ForwardContractVO insertCancelDetails(ForwardContractVO fwdContractVO, String category, String status, String action)
    throws BusinessException
  {
    logger.info("Entering Method");
    ForwardContractDAO fwdContractDAO = null;
    try
    {
      fwdContractDAO = ForwardContractDAO.getDAO();
      fwdContractVO = fwdContractDAO.insertCancelDetails(fwdContractVO, category, status, action);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return fwdContractVO;
  }
 
  public ArrayList<ForwardContractVO> fetchFwdContractDetails(ForwardContractVO fwdContractVO)
    throws BusinessException
  {
    logger.info("Entering Method");
    ForwardContractDAO fwdContractDAO = null;
    ArrayList<ForwardContractVO> forwardContractList = null;
    try
    {
      forwardContractList = new ArrayList();
      fwdContractDAO = ForwardContractDAO.getDAO();
      if (fwdContractDAO != null) {
        forwardContractList = fwdContractDAO.fetchFwdContractDetails(fwdContractVO);
      }
    }
    catch (Exception exception)
    {
      logger.info("Exception in BD " + exception.getMessage());
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return forwardContractList;
  }
 
  public ArrayList<ForwardContractVO> fetchFwdContractEnquiryDetails(ForwardContractVO fwdContractVO)
    throws BusinessException
  {
    logger.info("Entering Method");
    ForwardContractDAO fwdContractDAO = null;
    ArrayList<ForwardContractVO> forwardContractList = null;
    try
    {
      forwardContractList = new ArrayList();
      fwdContractDAO = ForwardContractDAO.getDAO();
      if (fwdContractDAO != null) {
        forwardContractList = fwdContractDAO.fetchFwdContractEnquiryDetails(fwdContractVO);
      }
    }
    catch (Exception exception)
    {
      logger.info("Exception in BD " + exception.getMessage());
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return forwardContractList;
  }
 
  public int getRecordCountFromDB(ForwardContractVO fwdContractVO, String category)
    throws BusinessException
  {
    logger.info("Entering Method");
    ForwardContractDAO fwdContractDAO = null;
    int count = 0;
    try
    {
      fwdContractDAO = ForwardContractDAO.getDAO();
      count = fwdContractDAO.getRecordCountFromDB(fwdContractVO, category);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return count;
  }
 
  public void setProcessDate()
  {
    logger.info("Entering Method");
    ForwardContractDAO dao = null;
    try
    {
      dao = ForwardContractDAO.getDAO();
      dao.getTICurrentDate();
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
  }
}
