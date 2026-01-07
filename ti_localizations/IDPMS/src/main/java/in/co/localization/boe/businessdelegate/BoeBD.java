package in.co.localization.boe.businessdelegate;
 
import in.co.boe.vo.TransactionVO;

import in.co.localization.boe.dao.BoeDAO;

import in.co.localization.businessdelegate.BaseBusinessDelegate;

import in.co.localization.businessdelegate.exception.BusinessException;

import in.co.localization.vo.localization.BOEDataSearchVO;

import in.co.localization.vo.localization.BoeVO;

import in.co.localization.vo.localization.InvoiceDetailsVO;

import java.util.ArrayList;

import org.apache.log4j.Logger;
 
public class BoeBD

  extends BaseBusinessDelegate

{

  private static Logger logger = Logger.getLogger(BoeBD.class.getName());

  static BoeBD bd;

  public static BoeBD getBD()

  {

    if (bd == null) {

      bd = new BoeBD();

    }

    return bd;

  }

  public ArrayList<BoeVO> boeExChecker(BOEDataSearchVO boeSearchVO)

    throws BusinessException

  {

    logger.info("Entering Method");

    BoeDAO dao = null;

    ArrayList<BoeVO> boeList = null;

    try

    {

      dao = BoeDAO.getDAO();

      boeList = dao.boeExChecker(boeSearchVO);

    }

    catch (Exception e)

    {

      logger.info("boeExChecker11-----Exception--------" + e);

      throwBDException(e);

    }

    logger.info("Exiting Method");

    return boeList;

  }

  public ArrayList<BoeVO> boeClosureChecker(BOEDataSearchVO boeSearchVO)

    throws BusinessException

  {

    logger.info("-----------boeClosureChecker----------");

    BoeDAO dao = null;

    ArrayList<BoeVO> boeList = null;

    try

    {

      dao = BoeDAO.getDAO();

      boeList = dao.boeClosureChecker(boeSearchVO);

    }

    catch (Exception e)

    {

      logger.info("boeClosureChecker Exception -----------" + e);

      throwBDException(e);

    }

    logger.info("Exiting Method");

    return boeList;

  }

  public ArrayList<BoeVO> boeExAuthorize(BOEDataSearchVO boeSearchVO, String[] exChkList, String boeExStatus, String remarks)

    throws BusinessException

  {

    logger.info("------------boeExAuthorize----------");

    BoeDAO dao = null;

    ArrayList<BoeVO> boeList = null;

    try

    {

      dao = BoeDAO.getDAO();

      boeList = dao.boeExAuthorize(boeSearchVO, exChkList, boeExStatus, remarks);

    }

    catch (Exception e)

    {

      logger.info("boeExAuthorize----------exception" + e);

      throwBDException(e);

    }

    logger.info("Exiting Method");

    return boeList;

  }

  public ArrayList<BoeVO> boeClosureAuthorize(BOEDataSearchVO boeSearchVO, String[] clChkList, String boeClStatus, String remarks)

    throws BusinessException

  {

    logger.info("--------boeClosureAuthorize----------");

    BoeDAO dao = null;

    ArrayList<BoeVO> boeList = null;

    try

    {

      dao = BoeDAO.getDAO();

      boeList = dao.boeClosureAuthorize(boeSearchVO, clChkList, boeClStatus, remarks);

    }

    catch (Exception e)

    {

      logger.info("--------boeClosureAuthorize----exception------" + e);

      throwBDException(e);

    }

    logger.info("Exiting Method");

    return boeList;

  }

  public BoeVO fetchBOEExtension(BoeVO boevo)

    throws BusinessException

  {

    logger.info("--------fetchBOEExtension----------");

    BoeDAO dao = null;

    try

    {

      dao = BoeDAO.getDAO();

      boevo = dao.fetchBOEExtension(boevo);

    }

    catch (Exception e)

    {

      logger.info("fetchBOEExtension-----" + e);

      throwBDException(e);

    }

    logger.info("Exiting Method");

    return boevo;

  }

  public BoeVO fetchBOEClosure(BoeVO boevo)

    throws BusinessException

  {

    logger.info("fetchBOEClosure---------");

    BoeDAO dao = null;

    try

    {

      dao = BoeDAO.getDAO();

      boevo = dao.fetchBOEClosure(boevo);

    }

    catch (Exception e)

    {

      logger.info("fetchBOEClosure---------" + e);

      throwBDException(e);

    }

    logger.info("Exiting Method");

    return boevo;

  }

  public BoeVO revertBOEDetails(BoeVO boevo)

    throws BusinessException

  {

    logger.info("Entering Method");

    BoeDAO dao = null;

    try

    {

      dao = BoeDAO.getDAO();

      boevo = dao.revertBOEDetails(boevo);

    }

    catch (Exception e)

    {

      throwBDException(e);

    }

    logger.info("Exiting Method");

    return boevo;

  }

  public TransactionVO fetchInvoiceData(BoeVO boeVO)

    throws BusinessException

  {

    logger.info("Inside of invoiceData() ====================>>> 222");

    logger.info("Entering Method");

    BoeDAO dao = null;

    TransactionVO transactionVO = null;

    try

    {

      dao = BoeDAO.getDAO();

      transactionVO = dao.fetchInvoiceData(boeVO);

    }

    catch (Exception exception)

    {

      throwBDException(exception);

    }

    logger.info("Exiting Method");

    return transactionVO;

  }

  public TransactionVO updateCrossCurrencyData(BoeVO boeVO, TransactionVO transactionVO)

    throws BusinessException

  {

    logger.info("Entering Method");

    BoeDAO dao = null;

    try

    {

      dao = BoeDAO.getDAO();

      transactionVO = dao.updateCrossCurrencyData(boeVO, transactionVO);

    }

    catch (Exception e)

    {

      throwBDException(e);

    }

    logger.info("Exiting Method");

    return transactionVO;

  }

  public TransactionVO getInvoiceDetails(BoeVO beovo)

    throws BusinessException

  {

    logger.info("Entering Method");

    BoeDAO dao = null;

    TransactionVO transactionVO = null;

    try

    {

      dao = BoeDAO.getDAO();

      transactionVO = dao.getInvoiceDetails(beovo);

    }

    catch (Exception e)

    {

      e.printStackTrace();

      throwBDException(e);

    }

    logger.info("Exiting Method");

    return transactionVO;

  }
  public BoeVO boeExtSubmit(BoeVO boevo)

		    throws BusinessException

		  {

		    logger.info("--------boeExtSubmit----------");

		    BoeDAO dao = null;

		    try

		    {

		      dao = BoeDAO.getDAO();

		      boevo = dao.boeExtSubmit(boevo);

		    }

		    catch (Exception e)

		    {

		      logger.info("--------boeExtSubmit---------e-" + e.getMessage());

		      throwBDException(e);

		    }

		    logger.info("Exiting Method");

		    return boevo;

		  }

		  public BoeVO boeClosureSubmit(BoeVO boevo, String[] chkInvlist)

		    throws BusinessException

		  {

		    logger.info("-------------boeClosureSubmit-------------");

		    BoeDAO dao = null;

		    try

		    {

		      dao = BoeDAO.getDAO();

		      boevo = dao.boeClosureSubmit(boevo, chkInvlist);

		    }

		    catch (Exception e)

		    {

		      logger.info("boeClosureSubmit Exception -----------" + e);

		    }

		    logger.info("Exiting Method");

		    return boevo;

		  }

		  public BoeVO boeView(BOEDataSearchVO boeSearchVO)

		    throws BusinessException

		  {

		    logger.info("Entering Method");

		    BoeDAO dao = null;

		    BoeVO boevo = null;

		    try

		    {

		      dao = BoeDAO.getDAO();

		      boevo = dao.boeView(boeSearchVO);

		    }

		    catch (Exception e)

		    {

		      throwBDException(e);

		    }

		    logger.info("Exiting Method");

		    return boevo;

		  }

		  public BoeVO getBoeDetailss(BoeVO boevo)

		  {

		    logger.info("Entering Method");

		    BoeDAO dao = null;

		    try

		    {

		      dao = BoeDAO.getDAO();

		      boevo = dao.getBoeDetailss(boevo);

		    }

		    catch (Exception e)

		    {

		      e.printStackTrace();

		    }

		    logger.info("Exiting Method");

		    return boevo;

		  }

		  public ArrayList<BoeVO> fetchPaymentDetails(BOEDataSearchVO boeSearchVO)

		  {

		    logger.info("Entering Method");

		    BoeDAO dao = null;

		    ArrayList<BoeVO> boeList = null;

		    try

		    {

		      dao = BoeDAO.getDAO();

		      boeList = dao.fetchPaymentDetails(boeSearchVO);

		    }

		    catch (Exception e)

		    {

		      e.printStackTrace();

		    }

		    logger.info("Exiting Method");

		    return boeList;

		  }

		  public int getBoeExCount(BoeVO boevo)

		  {

		    logger.info("Entering Method");

		    BoeDAO dao = null;

		    int count = 0;

		    try

		    {

		      dao = BoeDAO.getDAO();

		      count = dao.getBoeExCount(boevo);

		    }

		    catch (Exception e)

		    {

		      e.printStackTrace();

		    }

		    logger.info("Exiting Method");

		    return count;

		  }

		  public int getBoeExStatus(BoeVO boevo)

		  {

		    logger.info("Entering Method");

		    BoeDAO dao = null;

		    int count = 0;

		    try

		    {

		      dao = BoeDAO.getDAO();

		      count = dao.getBoeExStatus(boevo);

		    }

		    catch (Exception e)

		    {

		      e.printStackTrace();

		    }

		    logger.info("Exiting Method");

		    return count;

		  }

		  public int getMBEStatus(BoeVO boevo)

		  {

		    logger.info("Entering Method");

		    BoeDAO dao = null;

		    int count = 0;

		    try

		    {

		      dao = BoeDAO.getDAO();

		      count = dao.getMBEStatus(boevo);

		    }

		    catch (Exception e)

		    {

		      e.printStackTrace();

		    }

		    logger.info("Exiting Method");

		    return count;

		  }

		  public int getBoeClCount(BoeVO boevo)

		  {

		    logger.info("Entering Method");

		    BoeDAO dao = null;

		    int count = 0;

		    try

		    {

		      dao = BoeDAO.getDAO();

		      count = dao.getBoeClCount(boevo);

		    }

		    catch (Exception e)

		    {

		      e.printStackTrace();

		    }

		    logger.info("Exiting Method");

		    return count;

		  }

		  public int isCrossCurrencyCase(BoeVO beoVO)

		    throws BusinessException

		  {

		    logger.info("Entering Method");

		    BoeDAO dao = null;

		    int crossCount = 0;

		    try

		    {

		      dao = BoeDAO.getDAO();

		      crossCount = dao.isCrossCurrencyCase(beoVO);

		    }

		    catch (Exception e)

		    {

		      throwBDException(e);

		    }

		    logger.info("Exiting Method");

		    return crossCount;

		  }

		  public ArrayList<BoeVO> fetchDischargePortList(BoeVO boeVO)

		  {

		    logger.info("Entering Method");

		    int iRet = 0;

		    ArrayList<BoeVO> portList1 = null;

		    BoeDAO dao = null;

		    try

		    {

		      dao = BoeDAO.getDAO();

		      if (dao != null) {

		        portList1 = dao.fetchDischargePortList(boeVO);

		      }

		    }

		    catch (Exception ex)

		    {

		      ex.printStackTrace();

		    }

		    logger.info("Exiting Method");

		    return portList1;

		  }

		  public ArrayList<InvoiceDetailsVO> fetchInvoiceList(BoeVO boeVO)

		  {

		    ArrayList<InvoiceDetailsVO> invoiceList = null;

		    BoeDAO dao = null;

		    try

		    {

		      dao = BoeDAO.getDAO();

		      if (dao != null) {

		        invoiceList = dao.fetchInovoiceList(boeVO);

		      }

		    }

		    catch (Exception ex)

		    {

		      ex.printStackTrace();

		    }

		    logger.info("Exiting Method");

		    return invoiceList;

		  }

		}
		 