package in.co.FIRC.businessdelegate;

import in.co.FIRC.businessdelegate.exception.BusinessException;
import in.co.FIRC.dao.FIRCOurBankDAO;
import in.co.FIRC.vo.ourBank.OurBankVO;
import in.co.FIRC.vo.ourBank.PrintOurBankVO;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
 
public class FIRCOurBankBD
  extends BaseBusinessDelegate
{
  private static Logger logger = LogManager.getLogger(FIRCOurBankBD.class.getName());
  static FIRCOurBankBD bd;
  public static FIRCOurBankBD getBD()
  {
    if (bd == null) {
      bd = new FIRCOurBankBD();
    }
    return bd;
  }
  ArrayList<OurBankVO> ourAL = null;
  ArrayList<PrintOurBankVO> ourBankVODetails = null;
  public ArrayList<OurBankVO> getOurAL()
  {
    return this.ourAL;
  }
  public void setOurAL(ArrayList<OurBankVO> ourAL)
  {
    this.ourAL = ourAL;
  }
  public ArrayList<PrintOurBankVO> getOurBankVODetails()
  {
    return this.ourBankVODetails;
  }
  public void setOurBankVODetails(ArrayList<PrintOurBankVO> ourBankVODetails)
  {
    this.ourBankVODetails = ourBankVODetails;
  }
  public OurBankVO fetchOurBankList(OurBankVO ourBankVO)
    throws BusinessException
  {
    logger.info("Entering Method");
    FIRCOurBankDAO ourBankDAO = null;
    try
    {
      ourBankDAO = new FIRCOurBankDAO();
      ourBankVO = ourBankDAO.fetchOurBankList(ourBankVO);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return ourBankVO;
  }
  public OurBankVO insertOurBankList(OurBankVO ourBankVO)
    throws BusinessException
  {
    logger.info("-----------insertOurBankList------------");
    FIRCOurBankDAO ourBankDAO = null;
    try
    {
      ourBankDAO = new FIRCOurBankDAO();
      ourBankVO = ourBankDAO.insertOurBankList(ourBankVO);
    }
    catch (Exception exception)
    {
      logger.info("-----------insertOurBankList-----exception-------" + exception);
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return ourBankVO;
  }
  public OurBankVO validate(OurBankVO ourBankVO)
    throws BusinessException
  {
    logger.info("--------validate--------------");
    FIRCOurBankDAO ourBankDAO = null;
    try
    {
      ourBankDAO = new FIRCOurBankDAO();
      ourBankVO = ourBankDAO.validate(ourBankVO);
    }
    catch (Exception exception)
    {
      logger.info("--------validate----------exception----" + exception);
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return ourBankVO;
  }
  public String printStatus(String refNo)
    throws BusinessException
  {
    logger.info("-------------printStatus-----------------");
    FIRCOurBankDAO dao = null;
    String status = null;
    try
    {
      dao = FIRCOurBankDAO.getDAO();
      status = dao.printStatus(refNo);
    }
    catch (Exception exception)
    {
      logger.info("-------------printStatus---------exception--------" + exception);
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return status;
  }
  public PrintOurBankVO printOurBankDetails(PrintOurBankVO printOurBankVO)
    throws BusinessException
  {
    logger.info("-----------printOurBankDetails------------");
    FIRCOurBankDAO ourBankDAO = null;
    try
    {
      ourBankDAO = new FIRCOurBankDAO();
      printOurBankVO = ourBankDAO.printOurBankDetails(printOurBankVO);
    }
    catch (Exception exception)
    {
      logger.info("-----------printOurBankDetails--------exception----" + exception);
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return printOurBankVO;
  }
  public boolean sentAdviceData(String transRefNo)
    throws BusinessException
  {
    logger.info("-----------sentAdviceData------------");
    FIRCOurBankDAO ourBankDAO = null;
    boolean emailStaus = false;
    try
    {
      ourBankDAO = new FIRCOurBankDAO();
      emailStaus = ourBankDAO.sentAdviceData(transRefNo);
    }
    catch (Exception exception)
    {
      logger.info("sentAdviceData---Exception-----" + exception);
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return emailStaus;
  }
  public boolean sentAdviceDataBulk(String CIFNo, ArrayList pdfFileNameArray)
    throws BusinessException
  {
    logger.info("-----------sentAdviceData------------");
    FIRCOurBankDAO ourBankDAO = null;
    boolean emailStaus = false;
    try
    {
      ourBankDAO = new FIRCOurBankDAO();
      emailStaus = ourBankDAO.sentAdviceDataBulk(CIFNo.trim(), pdfFileNameArray);
    }
    catch (Exception exception)
    {
      logger.info("sentAdviceData---Exception-----" + exception);
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return emailStaus;
  }
}
