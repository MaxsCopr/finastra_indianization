package in.co.mttenquiry.businessdelegate;

import in.co.mttenquiry.businessdelegate.exception.BusinessException;
import in.co.mttenquiry.dao.MTTDataProcessDAO;
import in.co.mttenquiry.vo.MTTDataVO;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MTTHomeBD
  extends BaseBusinessDelegate
{
  private static Logger logger = LogManager.getLogger(MTTHomeBD.class
    .getName());
  static MTTHomeBD bd;
 
  public static MTTHomeBD getBD()
  {
    if (bd == null) {
      bd = new MTTHomeBD();
    }
    return bd;
  }
 
  public void setDate()
    throws BusinessException
  {
    logger.info("Entering Method");
    MTTDataProcessDAO dao = null;
    try
    {
      dao = MTTDataProcessDAO.getDAO();
      dao.setDate();
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
  }
 
  public int checkLoginedUserType(MTTDataVO mttVo)
    throws BusinessException
  {
    logger.info("Entering Method");
    MTTDataProcessDAO dao = null;
    int count = 0;
    try
    {
      dao = MTTDataProcessDAO.getDAO();
      count = dao.checkLoginedUserType(mttVo);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return count;
  }
 
  public ArrayList<MTTDataVO> getDataFromTI(MTTDataVO mttVo)
    throws BusinessException
  {
    logger.info("Entering Method");
   
    MTTDataProcessDAO dao = null;
    ArrayList<MTTDataVO> list = null;
    try
    {
      dao = MTTDataProcessDAO.getDAO();
      list = dao.getMTTDetails(mttVo);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return list;
  }
 
  public void getMTTNumberOnMasterRef(MTTDataVO mttVo)
    throws BusinessException
  {
    logger.info("Entering Method");
   
    MTTDataProcessDAO dao = null;
    try
    {
      dao = MTTDataProcessDAO.getDAO();
      dao.getMTTNumberOnMasterRef(mttVo);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
  }
 
  public String fetchStatusForMTT(MTTDataVO mttVo)
    throws BusinessException
  {
    logger.info("Entering Method");
   
    MTTDataProcessDAO dao = null;
    String mttNumberCnt = "";
    try
    {
      dao = MTTDataProcessDAO.getDAO();
      mttNumberCnt = dao.fetchStatusForMTT(mttVo);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return mttNumberCnt;
  }
 
  public String checkInProgressCntForMTT(MTTDataVO mttVo)
    throws BusinessException
  {
    logger.info("Entering Method");
   
    MTTDataProcessDAO dao = null;
    String mttNumberCnt = "";
    try
    {
      dao = MTTDataProcessDAO.getDAO();
      mttNumberCnt = dao.checkInProgressCntForMTT(mttVo);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return mttNumberCnt;
  }
 
  public int changeStatusForMTT(MTTDataVO mttVo)
    throws BusinessException
  {
    logger.info("Entering Method");
   
    MTTDataProcessDAO dao = null;
    int updateCnt = 0;
    try
    {
      dao = MTTDataProcessDAO.getDAO();
      updateCnt = dao.changeStatusForMTT(mttVo);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return updateCnt;
  }
 
  public ArrayList<MTTDataVO> getPendingDataForChecker(MTTDataVO mttVo)
    throws BusinessException
  {
    logger.info("Entering Method");
   
    MTTDataProcessDAO dao = null;
    ArrayList<MTTDataVO> list = null;
    try
    {
      dao = MTTDataProcessDAO.getDAO();
      list = dao.getPendingDataForChecker(mttVo);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return list;
  }
 
  public String updateStatus(String[] chkList, String check, String remarks)
    throws BusinessException
  {
    logger.info("Entering Method");
   
    MTTDataProcessDAO dao = null;
    String result = null;
    try
    {
      dao = MTTDataProcessDAO.getDAO();
      result = dao.updateStatus(chkList, check, remarks);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return result;
  }
 
  public ArrayList<MTTDataVO> getDataFromTIForAmend(MTTDataVO mttVo)
    throws BusinessException
  {
    logger.info("Entering Method");
   
    MTTDataProcessDAO dao = null;
    ArrayList<MTTDataVO> list = null;
    try
    {
      dao = MTTDataProcessDAO.getDAO();
      list = dao.getMTTDetailsForAmend(mttVo);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return list;
  }
 
  public int storeDeleteMTTNumber(String[] chkList, MTTDataVO mttVo)
    throws BusinessException
  {
    logger.info("Entering Method");
   
    MTTDataProcessDAO dao = null;
    int updateCnt = 0;
    try
    {
      dao = MTTDataProcessDAO.getDAO();
      updateCnt = dao.storeDeleteMTTNumber(chkList, mttVo);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return updateCnt;
  }
 
  public ArrayList<MTTDataVO> getPendingMttNumberAmendDataForChecker(MTTDataVO mttVo)
    throws BusinessException
  {
    logger.info("Entering Method");
   
    MTTDataProcessDAO dao = null;
    ArrayList<MTTDataVO> list = null;
    try
    {
      dao = MTTDataProcessDAO.getDAO();
      list = dao.getPendingMttNumberAmendDataForChecker(mttVo);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return list;
  }
 
  public ArrayList<MTTDataVO> getPendingMttNumberAddForChecker(MTTDataVO mttVo)
    throws BusinessException
  {
    logger.info("Entering Method");
   
    MTTDataProcessDAO dao = null;
    ArrayList<MTTDataVO> list = null;
    try
    {
      dao = MTTDataProcessDAO.getDAO();
      list = dao.getPendingMttNumberAddForChecker(mttVo);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return list;
  }
 
  public String amendMTTNumber(String[] chkList, String check, String remarks)
    throws BusinessException
  {
    logger.info("Entering Method");
   
    MTTDataProcessDAO dao = null;
    String result = null;
    try
    {
      dao = MTTDataProcessDAO.getDAO();
      result = dao.amendMTTNumber(chkList, check, remarks);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return result;
  }
 
  public String checkMasterRef(MTTDataVO mttVo)
    throws BusinessException
  {
    logger.info("Entering Method");
   
    MTTDataProcessDAO dao = null;
    String mttNumberCnt = "";
    try
    {
      dao = MTTDataProcessDAO.getDAO();
      mttNumberCnt = dao.checkMasterRef(mttVo);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return mttNumberCnt;
  }
 
  public String checkMasterRefPurSale(MTTDataVO mttVo)
    throws BusinessException
  {
    logger.info("Entering Method");
   
    MTTDataProcessDAO dao = null;
    String mttNumberCnt = "";
    try
    {
      dao = MTTDataProcessDAO.getDAO();
      mttNumberCnt = dao.checkMasterRefReportPuchaseSale(mttVo);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return mttNumberCnt;
  }
 
  public String checkMasterRefMTTLinked(MTTDataVO mttVo)
    throws BusinessException
  {
    logger.info("Entering Method");
   
    MTTDataProcessDAO dao = null;
    String mttNumberCnt = "";
    try
    {
      dao = MTTDataProcessDAO.getDAO();
      mttNumberCnt = dao.checkMasterRefMTTLinked(mttVo);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return mttNumberCnt;
  }
 
  public String checkMasterRefInProgress(MTTDataVO mttVo)
    throws BusinessException
  {
    logger.info("Entering Method");
   
    MTTDataProcessDAO dao = null;
    String mttNumberCnt = "";
    try
    {
      dao = MTTDataProcessDAO.getDAO();
      mttNumberCnt = dao.checkMasterRefInProgress(mttVo);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return mttNumberCnt;
  }
 
  public String checkMasterRefInSplitProcess(MTTDataVO mttVo)
    throws BusinessException
  {
    logger.info("Entering Method");
   
    MTTDataProcessDAO dao = null;
    String mttNumberCnt = "";
    try
    {
      dao = MTTDataProcessDAO.getDAO();
      mttNumberCnt = dao.checkMasterRefInSplitProcess(mttVo);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return mttNumberCnt;
  }
 
  public String checkMTTNum(MTTDataVO mttVo)
    throws BusinessException
  {
    logger.info("Entering Method");
   
    MTTDataProcessDAO dao = null;
    String mttNumberCnt = "";
    try
    {
      dao = MTTDataProcessDAO.getDAO();
      mttNumberCnt = dao.checkMTTNum(mttVo);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return mttNumberCnt;
  }
 
  public String checkMTTSts(MTTDataVO mttVo)
    throws BusinessException
  {
    logger.info("Entering Method");
   
    MTTDataProcessDAO dao = null;
    String mttNumberCnt = "";
    try
    {
      dao = MTTDataProcessDAO.getDAO();
      mttNumberCnt = dao.checkMTTSts(mttVo);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return mttNumberCnt;
  }
 
  public String checkMTTCustomer(MTTDataVO mttVo)
    throws BusinessException
  {
    logger.info("Entering Method");
   
    MTTDataProcessDAO dao = null;
    String mttCustomerCnt = "";
    try
    {
      dao = MTTDataProcessDAO.getDAO();
      mttCustomerCnt = dao.checkMTTCustomer(mttVo);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return mttCustomerCnt;
  }
 
  public int addMTTNumberMasterRef(MTTDataVO mttVo)
    throws BusinessException
  {
    logger.info("Entering Method");
    int insertCnt = 0;
    MTTDataProcessDAO dao = null;
    try
    {
      dao = MTTDataProcessDAO.getDAO();
     
      insertCnt = dao.addMTTNumberMasterRef(mttVo);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return insertCnt;
  }
 
  public String amendAddMTTNumberMasterRef(String[] chkList1, String check1, String remarks1)
    throws BusinessException
  {
    logger.info("Entering Method");
   
    MTTDataProcessDAO dao = null;
    String result = null;
    try
    {
      dao = MTTDataProcessDAO.getDAO();
      result = dao.amendAddMTTNumberMasterRef(chkList1, check1, remarks1);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return result;
  }
 
  public String checkMTTNum1(MTTDataVO mttVo, String mttListNumberToCheck)
    throws BusinessException
  {
    logger.info("Entering Method");
   
    MTTDataProcessDAO dao = null;
    String mttNumberCnt = "";
    try
    {
      dao = MTTDataProcessDAO.getDAO();
      mttNumberCnt = dao.checkMTTNum1(mttVo, mttListNumberToCheck);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return mttNumberCnt;
  }
 
  public String checkMTTSts(MTTDataVO mttVo, String mttListNumberToCheck)
    throws BusinessException
  {
    logger.info("Entering Method");
   
    MTTDataProcessDAO dao = null;
    String mttNumberCnt = "";
    try
    {
      dao = MTTDataProcessDAO.getDAO();
      mttNumberCnt = dao.checkMTTSts(mttVo, mttListNumberToCheck);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return mttNumberCnt;
  }
 
  public String checkMTTCustomer1(MTTDataVO mttVo, String mttListNumberToCheck)
    throws BusinessException
  {
    logger.info("Entering Method");
   
    MTTDataProcessDAO dao = null;
    String mttCustomerCnt = "";
    try
    {
      dao = MTTDataProcessDAO.getDAO();
      mttCustomerCnt = dao.checkMTTCustomer1(mttVo, mttListNumberToCheck);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return mttCustomerCnt;
  }
 
  public int submitMTTNumbeSplit(MTTDataVO mttVo, String splitRemarks, String mttListNumber1, String mttListTransAmt1, String mttListNumber2, String mttListTransAmt2, String mttListNumber3, String mttListTransAmt3, String mttListNumber4, String mttListTransAmt4, String mttListNumber5, String mttListTransAmt5, String mttListNumber6, String mttListTransAmt6, String mttListNumber7, String mttListTransAmt7, String mttListNumber8, String mttListTransAmt8, String mttListNumber9, String mttListTransAmt9, String mttListNumber10, String mttListTransAmt10)
    throws BusinessException
  {
    logger.info("Entering Method");
   
    MTTDataProcessDAO dao = null;
    int updateCnt = 0;
    try
    {
      dao = MTTDataProcessDAO.getDAO();
      updateCnt = dao.submitMTTNumbeSplit(mttVo, splitRemarks, mttListNumber1, mttListTransAmt1, mttListNumber2, mttListTransAmt2, mttListNumber3, mttListTransAmt3, mttListNumber4, mttListTransAmt4, mttListNumber5, mttListTransAmt5, mttListNumber6, mttListTransAmt6, mttListNumber7, mttListTransAmt7, mttListNumber8, mttListTransAmt8, mttListNumber9, mttListTransAmt9, mttListNumber10, mttListTransAmt10);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return updateCnt;
  }
 
  public ArrayList<MTTDataVO> fetchSplitDataForApproval(MTTDataVO mttVo)
    throws BusinessException
  {
    logger.info("Entering Method");
   
    MTTDataProcessDAO dao = null;
    ArrayList<MTTDataVO> list = null;
    try
    {
      dao = MTTDataProcessDAO.getDAO();
      list = dao.fetchSplitDataForApproval(mttVo);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return list;
  }
 
  public String amendSplitCheckerData(MTTDataVO mttVo, String check1, String remarks1)
    throws BusinessException
  {
    logger.info("Entering Method");
   
    MTTDataProcessDAO dao = null;
    String result = null;
    try
    {
      dao = MTTDataProcessDAO.getDAO();
      result = dao.amendSplitCheckerData(mttVo, check1, remarks1);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return result;
  }
 
  public String processStagingData(MTTDataVO mttVo)
    throws BusinessException
  {
    logger.info("Entering Method");
   
    MTTDataProcessDAO dao = null;
    String result = null;
    try
    {
      dao = MTTDataProcessDAO.getDAO();
      result = dao.processStagingData(mttVo);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return result;
  }
 
  public String getCloseUrl()
    throws BusinessException
  {
    logger.info("Entering Method");
    MTTDataProcessDAO dao = null;
    String url = "";
    try
    {
      dao = MTTDataProcessDAO.getDAO();
      url = dao.getCloseUrl();
    }
    catch (Exception e)
    {
      throwBDException(e);
    }
    return url;
  }
}
