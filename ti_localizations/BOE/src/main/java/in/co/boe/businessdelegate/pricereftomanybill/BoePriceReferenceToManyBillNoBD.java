package in.co.boe.businessdelegate.pricereftomanybill;

import in.co.boe.businessdelegate.BaseBusinessDelegate;
import in.co.boe.businessdelegate.exception.BusinessException;
import in.co.boe.dao.pricereferencetomanybill.BoeOfPriceReferenceToManyBillNoDAO;
import in.co.boe.vo.BOEPortSelectionVO;
import in.co.boe.vo.BoeVO;
import in.co.boe.vo.TransactionVO;
import java.util.ArrayList;
import org.apache.log4j.Logger;
 
public class BoePriceReferenceToManyBillNoBD
  extends BaseBusinessDelegate
{
  private static Logger logger = Logger.getLogger(BoePriceReferenceToManyBillNoBD.class.getName());
  static BoePriceReferenceToManyBillNoBD bd;
  public static BoePriceReferenceToManyBillNoBD getBD()
  {
    if (bd == null) {
      bd = new BoePriceReferenceToManyBillNoBD();
    }
    return bd;
  }
  public TransactionVO updateboeDatatoTable(BoeVO boeVO, TransactionVO transactionVO)
  {
    logger.info("Entering Method");
    BoeOfPriceReferenceToManyBillNoDAO boeManyBillDAO = null;
    try
    {
      boeManyBillDAO = BoeOfPriceReferenceToManyBillNoDAO.getDAO();
      transactionVO = boeManyBillDAO.updateboeDatatoTable(boeVO, transactionVO);
    }
    catch (Exception e)
    {
      logger.info("Exception -->" + e.getMessage());
    }
    logger.info("Exiting Method");
    return transactionVO;
  }
  public int loaddeletedata(BoeVO beoVO)
    throws BusinessException
  {
    logger.info("Entering Method");
    BoeOfPriceReferenceToManyBillNoDAO boeManyBillDAO = null;
    int boeCount = 0;
    try
    {
      boeManyBillDAO = BoeOfPriceReferenceToManyBillNoDAO.getDAO();
      boeCount = boeManyBillDAO.lddeletedata(beoVO);
    }
    catch (Exception e)
    {
      throwBDException(e);
    }
    logger.info("Exiting Method");
    return boeCount;
  }
  public TransactionVO deletedata(BoeVO boeVO, TransactionVO transactionVO)
  {
    logger.info("Entering Method");
    BoeOfPriceReferenceToManyBillNoDAO boeManyBillDAO = null;
    try
    {
      boeManyBillDAO = BoeOfPriceReferenceToManyBillNoDAO.getDAO();
      transactionVO = boeManyBillDAO.deletedata(boeVO, transactionVO);
    }
    catch (Exception e)
    {
      logger.info("Exception -->" + e.getMessage());
    }
    logger.info("Exiting Method");
    return transactionVO;
  }
  public TransactionVO deleteBOEdata(BoeVO boeVO, TransactionVO transactionVO)
  {
    logger.info("Entering Method");
    BoeOfPriceReferenceToManyBillNoDAO boeManyBillDAO = null;
    try
    {
      boeManyBillDAO = BoeOfPriceReferenceToManyBillNoDAO.getDAO();
      transactionVO = boeManyBillDAO.deleteBOEdata(boeVO, transactionVO);
    }
    catch (Exception e)
    {
      logger.info("Exception -->" + e.getMessage());
    }
    logger.info("Exiting Method");
    return transactionVO;
  }
  public BoeVO retriveDataFromTI(BoeVO boeVO)
    throws BusinessException
  {
    logger.info("------------------retriveDataFromTI---------------");
    BoeOfPriceReferenceToManyBillNoDAO boeManyBillDAO = null;
    try
    {
      boeManyBillDAO = BoeOfPriceReferenceToManyBillNoDAO.getDAO();
      if (boeManyBillDAO != null) {
        boeVO = boeManyBillDAO.retriveDataFromTI(boeVO);
      }
    }
    catch (Exception exception)
    {
      logger.info("------------------retriveDataFromTI-----------exception----" + exception);
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return boeVO;
  }
  public ArrayList<TransactionVO> fetchPaymentReferenceDataList(BoeVO boeVO)
    throws BusinessException
  {
    logger.info("Entering Method");
    BoeOfPriceReferenceToManyBillNoDAO boeManyBillDAO = null;
    ArrayList<TransactionVO> tiList = null;
    try
    {
      boeManyBillDAO = BoeOfPriceReferenceToManyBillNoDAO.getDAO();
      if (boeManyBillDAO != null) {
        tiList = boeManyBillDAO.fetchPaymentReferenceDataList(boeVO);
      }
      logger.info("Status----------" + boeVO.getStatus());
      logger.info("Status----------" + boeVO.getStatus());
    }
    catch (Exception exception)
    {
      logger.info("fetchPaymentReferenceDataList-----" + exception.getMessage());
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return tiList;
  }
  public BoeVO retriveDataBasedOnBillNO(BoeVO boeVO)
    throws BusinessException
  {
    logger.info("------------retriveDataBasedOnBillNO--------------");
    BoeOfPriceReferenceToManyBillNoDAO boeManyBillDAO = null;
    try
    {
      boeManyBillDAO = BoeOfPriceReferenceToManyBillNoDAO.getDAO();
      if (boeManyBillDAO != null) {
        boeVO = boeManyBillDAO.retriveDataBasedOnBillNO(boeVO);
      }
    }
    catch (Exception exception)
    {
      logger.info("------------retriveDataBasedOnBillNO-------exception-------" + exception);
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return boeVO;
  }
  public BoeVO retriveDataBasedOnBillNO1(BoeVO boeVO)
    throws BusinessException
  {
    logger.info("-------------retriveDataBasedOnBillNO1------------");
    BoeOfPriceReferenceToManyBillNoDAO boeManyBillDAO = null;
    try
    {
      boeManyBillDAO = BoeOfPriceReferenceToManyBillNoDAO.getDAO();
      if (boeManyBillDAO != null) {
        boeVO = boeManyBillDAO.retriveDataBasedOnBillNO1(boeVO);
      }
    }
    catch (Exception exception)
    {
      logger.info("-------------retriveDataBasedOnBillNO1-------exception-----" + exception);
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return boeVO;
  }
  public BoeVO fetchAllocateInvoice(BoeVO boeVO)
    throws BusinessException
  {
    logger.info("------------fetchAllocateInvoice-----------");
    BoeOfPriceReferenceToManyBillNoDAO boeManyBillDAO = null;
    try
    {
      boeManyBillDAO = BoeOfPriceReferenceToManyBillNoDAO.getDAO();
      if (boeManyBillDAO != null) {
        boeVO = boeManyBillDAO.fetchAllocateInvoice(boeVO);
      }
    }
    catch (Exception exception)
    {
      logger.info("------------fetchAllocateInvoice------exception-----" + exception);
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return boeVO;
  }
  public TransactionVO fetchInvoiceData(BoeVO boeVO)
    throws BusinessException
  {
    logger.info("fetchInvoiceData---------------fetchInvoiceData");
    BoeOfPriceReferenceToManyBillNoDAO boeManyBillDAO = null;
    TransactionVO transactionVO = null;
    try
    {
      boeManyBillDAO = BoeOfPriceReferenceToManyBillNoDAO.getDAO();
      if (boeManyBillDAO != null) {
        transactionVO = boeManyBillDAO.fetchInvoiceData(boeVO);
      }
    }
    catch (Exception exception)
    {
      logger.info("fetchInvoiceData---------------fetchInvoiceData---------exception" + exception);
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return transactionVO;
  }
  public boolean checkBOEStatus(BoeVO boeVO)
  {
    logger.info("Entering Method");
    boolean flag = false;
    BoeOfPriceReferenceToManyBillNoDAO boeManyBillDAO = null;
    try
    {
      boeManyBillDAO = BoeOfPriceReferenceToManyBillNoDAO.getDAO();
      if ((boeManyBillDAO != null) && 
        (boeManyBillDAO.checkStatus(boeVO))) {
        flag = true;
      }
    }
    catch (Exception localException) {}
    logger.info("Exiting Method");
    return flag;
  }
  public TransactionVO insertInvoiceDatatoTable(BoeVO boeVO, TransactionVO transactionVO)
  {
    logger.info("Entering Method");
    BoeOfPriceReferenceToManyBillNoDAO boeManyBillDAO = null;
    try
    {
      boeManyBillDAO = BoeOfPriceReferenceToManyBillNoDAO.getDAO();
      transactionVO = boeManyBillDAO.insertInvoiceDatatoTable(boeVO, transactionVO);
    }
    catch (Exception e)
    {
      logger.info("Exception -->" + e.getMessage());
    }
    logger.info("Exiting Method");
    return transactionVO;
  }
  public TransactionVO UpdateInvoiceDatatoTable(BoeVO boeVO, TransactionVO transactionVO)
  {
    logger.info("Entering Method");
    BoeOfPriceReferenceToManyBillNoDAO boeManyBillDAO = null;
    try
    {
      boeManyBillDAO = BoeOfPriceReferenceToManyBillNoDAO.getDAO();
      transactionVO = boeManyBillDAO.updateInvoiceDatatoTable(boeVO, transactionVO);
    }
    catch (Exception e)
    {
      logger.info("Exception -->" + e.getMessage());
    }
    logger.info("Exiting Method");
    return transactionVO;
  }
  public String storeBillData(String[] chkInvlist, BoeVO boeVO)
    throws BusinessException
  {
    logger.info("Entering Method");
    BoeOfPriceReferenceToManyBillNoDAO boeManyBillDAO = null;
    String result = null;
    try
    {
      boeManyBillDAO = BoeOfPriceReferenceToManyBillNoDAO.getDAO();
      if (boeManyBillDAO != null) {
        result = boeManyBillDAO.storeBillData(chkInvlist, boeVO);
      }
    }
    catch (Exception exception)
    {
      logger.info("storeBillData Exception------------ " + exception);
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return result;
  }
  public BoeVO revertBOEDetails(BoeVO boeVO)
    throws BusinessException
  {
    logger.info("Entering Method");
    BoeOfPriceReferenceToManyBillNoDAO boeManyBillDAO = null;
    try
    {
      boeManyBillDAO = BoeOfPriceReferenceToManyBillNoDAO.getDAO();
      if (boeManyBillDAO != null) {
        boeVO = boeManyBillDAO.revertBOEDetails(boeVO);
      }
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return boeVO;
  }
  
  public TransactionVO getInvoiceDetails(BoeVO beoVO)
		    throws BusinessException
		  {
		    logger.info("------------------getInvoiceDetails---------------");
		    BoeOfPriceReferenceToManyBillNoDAO boeManyBillDAO = null;
		    TransactionVO transactionVO = null;
		    try
		    {
		      boeManyBillDAO = BoeOfPriceReferenceToManyBillNoDAO.getDAO();
		      transactionVO = boeManyBillDAO.getInvoiceDetails(beoVO);
		    }
		    catch (Exception e)
		    {
		      logger.info("------------------getInvoiceDetails----------exception-----" + e);
		      e.printStackTrace();
		      throwBDException(e);
		    }
		    logger.info("Exiting Method");
		    return transactionVO;
		  }
		  public TransactionVO updateCrossCurrencyData(BoeVO boeVO, TransactionVO transactionVO)
		    throws BusinessException
		  {
		    logger.info("-----------updateCrossCurrencyData-----------");
		    BoeOfPriceReferenceToManyBillNoDAO boeManyBillDAO = null;
		    try
		    {
		      boeManyBillDAO = BoeOfPriceReferenceToManyBillNoDAO.getDAO();
		      transactionVO = boeManyBillDAO.updateCrossCurrencyData(boeVO, transactionVO);
		    }
		    catch (Exception e)
		    {
		      logger.info("-----------updateCrossCurrencyData------exception-----" + e);
		      throwBDException(e);
		    }
		    logger.info("Exiting Method");
		    return transactionVO;
		  }
		  public int checkAdCode(String adCode)
		    throws BusinessException
		  {
		    logger.info("Entering Method");
		    BoeOfPriceReferenceToManyBillNoDAO boeManyBillDAO = null;
		    int adCount = 0;
		    try
		    {
		      boeManyBillDAO = BoeOfPriceReferenceToManyBillNoDAO.getDAO();
		      adCount = boeManyBillDAO.checkAdcode(adCode);
		    }
		    catch (Exception e)
		    {
		      throwBDException(e);
		    }
		    logger.info("Exiting Method");
		    return adCount;
		  }
		  public int validatePortCode(String portCode)
		    throws BusinessException
		  {
		    logger.info("Entering Method");
		    BoeOfPriceReferenceToManyBillNoDAO boeManyBillDAO = null;
		    int adCount = 0;
		    try
		    {
		      boeManyBillDAO = BoeOfPriceReferenceToManyBillNoDAO.getDAO();
		      adCount = boeManyBillDAO.validatePortCode(portCode);
		    }
		    catch (Exception e)
		    {
		      throwBDException(e);
		    }
		    logger.info("Exiting Method");
		    return adCount;
		  }
		  public int validatePos(String pos)
		    throws BusinessException
		  {
		    logger.info("Entering Method");
		    BoeOfPriceReferenceToManyBillNoDAO boeManyBillDAO = null;
		    int adCount = 0;
		    try
		    {
		      boeManyBillDAO = BoeOfPriceReferenceToManyBillNoDAO.getDAO();
		      adCount = boeManyBillDAO.validatePos(pos);
		    }
		    catch (Exception e)
		    {
		      throwBDException(e);
		    }
		    logger.info("Exiting Method");
		    return adCount;
		  }
		  public int checkIECodeData(BoeVO beoVO)
		    throws BusinessException
		  {
		    logger.info("Entering Method");
		    BoeOfPriceReferenceToManyBillNoDAO boeManyBillDAO = null;
		    int transIecode = 0;
		    try
		    {
		      boeManyBillDAO = BoeOfPriceReferenceToManyBillNoDAO.getDAO();
		      transIecode = boeManyBillDAO.checkIECodeData(beoVO);
		    }
		    catch (Exception e)
		    {
		      throwBDException(e);
		    }
		    logger.info("Exiting Method");
		    return transIecode;
		  }
		  public double getOrmClosedAmt(String paymentRefNo)
		    throws BusinessException
		  {
		    logger.info("Entering Method");
		    BoeOfPriceReferenceToManyBillNoDAO boeManyBillDAO = null;
		    double ormClosedAmt = 0.0D;
		    try
		    {
		      boeManyBillDAO = BoeOfPriceReferenceToManyBillNoDAO.getDAO();
		      ormClosedAmt = boeManyBillDAO.getOrmClosedAmt(paymentRefNo);
		    }
		    catch (Exception e)
		    {
		      throwBDException(e);
		    }
		    logger.info("Exiting Method");
		    return ormClosedAmt;
		  }
		  public int isCrossCurrencyCase(BoeVO beoVO)
		    throws BusinessException
		  {
		    logger.info("Entering Method");
		    BoeOfPriceReferenceToManyBillNoDAO boeManyBillDAO = null;
		    int crossCount = 0;
		    try
		    {
		      boeManyBillDAO = BoeOfPriceReferenceToManyBillNoDAO.getDAO();
		      crossCount = boeManyBillDAO.isCrossCurrencyCase(beoVO);
		    }
		    catch (Exception e)
		    {
		      throwBDException(e);
		    }
		    logger.info("Exiting Method");
		    return crossCount;
		  }
		  public int isIECodeCount(BoeVO beoVO)
		    throws BusinessException
		  {
		    logger.info("Entering Method");
		    BoeOfPriceReferenceToManyBillNoDAO boeManyBillDAO = null;
		    int ieCount = 0;
		    try
		    {
		      boeManyBillDAO = BoeOfPriceReferenceToManyBillNoDAO.getDAO();
		      ieCount = boeManyBillDAO.isIECodeCount(beoVO);
		    }
		    catch (Exception e)
		    {
		      throwBDException(e);
		    }
		    logger.info("Exiting Method");
		    return ieCount;
		  }
		  public int insertManualBOEData(BoeVO beoVO)
		    throws BusinessException
		  {
		    logger.info("Entering Method");
		    BoeOfPriceReferenceToManyBillNoDAO boeManyBillDAO = null;
		    int boeCount = 0;
		    try
		    {
		      boeManyBillDAO = BoeOfPriceReferenceToManyBillNoDAO.getDAO();
		      boeCount = boeManyBillDAO.insertManualBOEData(beoVO);
		    }
		    catch (Exception e)
		    {
		      throwBDException(e);
		    }
		    logger.info("Exiting Method");
		    return boeCount;
		  }
		  public int deleteManualBOEDataInvoice(BoeVO beoVO)
		    throws BusinessException
		  {
		    logger.info("Entering Method");
		    BoeOfPriceReferenceToManyBillNoDAO boeManyBillDAO = null;
		    int boeCount = 0;
		    try
		    {
		      logger.info("deleteManualBOEDataInvoice-------------111111111111");
		      boeManyBillDAO = BoeOfPriceReferenceToManyBillNoDAO.getDAO();
		      boeCount = boeManyBillDAO.deleteManualBOEInvoice(beoVO);
		    }
		    catch (Exception e)
		    {
		      throwBDException(e);
		    }
		    logger.info("Exiting Method");
		    return boeCount;
		  }
		  public BoeVO fetchManualBOEDetails(BoeVO boeVO)
		    throws BusinessException
		  {
		    logger.info("Welcome retriveBOEData()");
		    logger.info("Entering Method");
		    BoeOfPriceReferenceToManyBillNoDAO boeManyBillDAO = null;
		    try
		    {
		      boeManyBillDAO = BoeOfPriceReferenceToManyBillNoDAO.getDAO();
		      if (boeManyBillDAO != null) {
		        boeVO = boeManyBillDAO.fetchManualBOEDetails(boeVO);
		      }
		    }
		    catch (Exception exception)
		    {
		      throwBDException(exception);
		    }
		    logger.info("Exiting Method");
		    return boeVO;
		  }
		  
		  public BoeVO revertManualBOEDetails(BoeVO boeVO)
				    throws BusinessException
				  {
				    logger.info("Entering Method");
				    BoeOfPriceReferenceToManyBillNoDAO boeManyBillDAO = null;
				    try
				    {
				      boeManyBillDAO = BoeOfPriceReferenceToManyBillNoDAO.getDAO();
				      if (boeManyBillDAO != null) {
				        boeVO = boeManyBillDAO.revertManualBOEDetails(boeVO);
				      }
				    }
				    catch (Exception exception)
				    {
				      throwBDException(exception);
				    }
				    logger.info("Exiting Method");
				    return boeVO;
				  }
				  public int checkBoeCount(BoeVO boeVO)
				    throws BusinessException
				  {
				    logger.info("Entering Method");
				    BoeOfPriceReferenceToManyBillNoDAO boeManyBillDAO = null;
				    int boeCount = 0;
				    try
				    {
				      boeManyBillDAO = BoeOfPriceReferenceToManyBillNoDAO.getDAO();
				      boeCount = boeManyBillDAO.checkBoeCount(boeVO);
				    }
				    catch (Exception e)
				    {
				      throwBDException(e);
				    }
				    logger.info("Exiting Method");
				    return boeCount;
				  }
				  public int checkInvCount(BoeVO boeVO, TransactionVO transactionVO)
				    throws BusinessException
				  {
				    logger.info("Entering Method");
				    BoeOfPriceReferenceToManyBillNoDAO boeManyBillDAO = null;
				    int boeCount = 0;
				    try
				    {
				      boeManyBillDAO = BoeOfPriceReferenceToManyBillNoDAO.getDAO();
				      boeCount = boeManyBillDAO.checkInvCount(boeVO, transactionVO);
				    }
				    catch (Exception e)
				    {
				      throwBDException(e);
				    }
				    logger.info("Exiting Method");
				    return boeCount;
				  }
				  public int boeInvCount(BoeVO boeVO, TransactionVO transactionVO)
				    throws BusinessException
				  {
				    logger.info("Entering Method");
				    BoeOfPriceReferenceToManyBillNoDAO boeManyBillDAO = null;
				    int boeCount = 0;
				    try
				    {
				      boeManyBillDAO = BoeOfPriceReferenceToManyBillNoDAO.getDAO();
				      boeCount = boeManyBillDAO.boeInvCount(boeVO, transactionVO);
				    }
				    catch (Exception e)
				    {
				      throwBDException(e);
				    }
				    logger.info("Exiting Method");
				    return boeCount;
				  }
				  public int validateInvCurrency(String invCurrency)
				    throws BusinessException
				  {
				    logger.info("Entering Method");
				    BoeOfPriceReferenceToManyBillNoDAO boeManyBillDAO = null;
				    int invCurrCount = 0;
				    try
				    {
				      boeManyBillDAO = BoeOfPriceReferenceToManyBillNoDAO.getDAO();
				      invCurrCount = boeManyBillDAO.validateInvCurrency(invCurrency);
				    }
				    catch (Exception e)
				    {
				      throwBDException(e);
				    }
				    logger.info("Exiting Method");
				    return invCurrCount;
				  }
				  public int checkBOEStatus(String boeData)
				    throws BusinessException
				  {
				    logger.info("Entering Method");
				    BoeOfPriceReferenceToManyBillNoDAO boeManyBillDAO = null;
				    int boeCount = 0;
				    try
				    {
				      boeManyBillDAO = BoeOfPriceReferenceToManyBillNoDAO.getDAO();
				      boeCount = boeManyBillDAO.checkBOEStatus(boeData);
				    }
				    catch (Exception exception)
				    {
				      throwBDException(exception);
				    }
				    logger.info("Exiting Method");
				    return boeCount;
				  }
				  public BoeVO manualPopCIFDetails(BoeVO boeVO)
				    throws BusinessException
				  {
				    logger.info("Entering Method");
				    BoeOfPriceReferenceToManyBillNoDAO boeManyBillDAO = null;
				    try
				    {
				      boeManyBillDAO = BoeOfPriceReferenceToManyBillNoDAO.getDAO();
				      if (boeManyBillDAO != null) {
				        boeVO = boeManyBillDAO.manualPopCIFDetails(boeVO);
				      }
				    }
				    catch (Exception exception)
				    {
				      throwBDException(exception);
				    }
				    logger.info("Exiting Method");
				    return boeVO;
				  }
				  public int getMBEStatus(BoeVO boevo)
				  {
				    logger.info("Entering Method");
				    BoeOfPriceReferenceToManyBillNoDAO boeManyBillDAO = null;
				    int count = 0;
				    try
				    {
				      boeManyBillDAO = BoeOfPriceReferenceToManyBillNoDAO.getDAO();
				      count = boeManyBillDAO.getMBEStatus(boevo);
				    }
				    catch (Exception e)
				    {
				      e.printStackTrace();
				    }
				    logger.info("Exiting Method");
				    return count;
				  }
				  public ArrayList<BOEPortSelectionVO> fetchPortOfShipmentList(BoeVO boeVO)
				  {
				    logger.info("Entering Method");
				    int iRet = 0;
				    ArrayList<BOEPortSelectionVO> portList = null;
				    BoeOfPriceReferenceToManyBillNoDAO boeManyBillDAO = null;
				    try
				    {
				      boeManyBillDAO = BoeOfPriceReferenceToManyBillNoDAO.getDAO();
				      if (boeManyBillDAO != null) {
				        portList = boeManyBillDAO.fetchPortOfShipmentList(boeVO);
				      }
				    }
				    catch (Exception ex)
				    {
				      ex.printStackTrace();
				    }
				    logger.info("Exiting Method");
				    return portList;
				  }
				  public ArrayList<BoeVO> fetchDischargePortList(BoeVO boeVO)
				  {
				    logger.info("Entering Method");
				    int iRet = 0;
				    ArrayList<BoeVO> portList1 = null;
				    BoeOfPriceReferenceToManyBillNoDAO boeManyBillDAO = null;
				    try
				    {
				      boeManyBillDAO = BoeOfPriceReferenceToManyBillNoDAO.getDAO();
				      if (boeManyBillDAO != null) {
				        portList1 = boeManyBillDAO.fetchDischargePortList(boeVO);
				      }
				    }
				    catch (Exception ex)
				    {
				      ex.printStackTrace();
				    }
				    logger.info("Exiting Method");
				    return portList1;
				  }
				  public int deleteBOEData(String[] bill_chkList)
				  {
				    logger.info("deleteBOEData-----------------");
				    int iRet = 0;
				    BoeOfPriceReferenceToManyBillNoDAO boeManyBillDAO = null;
				    logger.info("DELETE METHOD 222222222222222222");
				    try
				    {
				      boeManyBillDAO = BoeOfPriceReferenceToManyBillNoDAO.getDAO();
				      if (boeManyBillDAO != null) {
				        iRet = boeManyBillDAO.deleteBOEData(bill_chkList);
				      }
				    }
				    catch (Exception ex)
				    {
				      logger.info("deleteBOEData----------exception-------" + ex);
				      ex.printStackTrace();
				    }
				    logger.info("Exiting Method");
				    return iRet;
				  }
				  public BoeVO retriveDataFromPayTable(BoeVO boeVO)
				  {
				    logger.info("retriveDataFromPayTable----------------------");
				    BoeOfPriceReferenceToManyBillNoDAO boeManyBillDAO = null;
				    try
				    {
				      boeManyBillDAO = BoeOfPriceReferenceToManyBillNoDAO.getDAO();
				      if (boeManyBillDAO != null) {
				        boeManyBillDAO.retriveDataFromPayTable(boeVO);
				      }
				    }
				    catch (Exception ex)
				    {
				      logger.info("retriveDataFromPayTable-----------------exceptin-----" + ex);
				      ex.printStackTrace();
				    }
				    logger.info("Exiting Method");
				    return boeVO;
				  }
				  public ArrayList<BoeVO> fetchoutstandingORMList(BoeVO boeVO)
				  {
				    logger.info("Entering Method");
				    int iRet = 0;
				    ArrayList<BoeVO> ormList1 = null;
				    BoeOfPriceReferenceToManyBillNoDAO boeManyBillDAO = null;
				    try
				    {
				      boeManyBillDAO = BoeOfPriceReferenceToManyBillNoDAO.getDAO();
				      if (boeManyBillDAO != null) {
				        ormList1 = boeManyBillDAO.fetchoutstandingORMList(boeVO);
				      }
				    }
				    catch (Exception ex)
				    {
				      ex.printStackTrace();
				    }
				    logger.info("Exiting Method");
				    return ormList1;
				  }
				  public ArrayList<BoeVO> fetchOutstandingBOEList(BoeVO boeVO)
				  {
				    logger.info("Entering Method");
				    int iRet = 0;
				    ArrayList<BoeVO> boeOSList = null;
				    BoeOfPriceReferenceToManyBillNoDAO boeManyBillDAO = null;
				    try
				    {
				      boeManyBillDAO = BoeOfPriceReferenceToManyBillNoDAO.getDAO();
				      if (boeManyBillDAO != null) {
				        boeOSList = boeManyBillDAO.fetchOutstandingBOEList(boeVO);
				      }
				    }
				    catch (Exception ex)
				    {
				      ex.printStackTrace();
				    }
				    logger.info("Exiting Method");
				    return boeOSList;
				  }
				}
