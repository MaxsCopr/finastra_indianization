package in.co.boe.businessdelegate.allRejectedBoe;

import in.co.boe.businessdelegate.BaseBusinessDelegate;
import in.co.boe.businessdelegate.exception.BusinessException;
import in.co.boe.dao.boe_checker.BoeCheckerDAO;
import in.co.boe.dao.boe_reject.BOERejectedDAO;
import in.co.boe.vo.BOESearchVO;
import in.co.boe.vo.BoeVO;
import in.co.boe.vo.TransactionVO;
import java.util.ArrayList;
import org.apache.log4j.Logger;
 
public class RejectedBoeBD
  extends BaseBusinessDelegate
{
  private static Logger logger = Logger.getLogger(BoeCheckerDAO.class.getName());
  static RejectedBoeBD bd;
  public static RejectedBoeBD getBD()
  {
    if (bd == null) {
      bd = new RejectedBoeBD();
    }
    return bd;
  }
  public ArrayList<TransactionVO> fetchRejectedRecords(BOESearchVO boeSearchVO)
    throws BusinessException
  {
    logger.info("Entering Method");
    ArrayList<TransactionVO> rejectedList = null;
    BOERejectedDAO dao = null;
    try
    {
      dao = BOERejectedDAO.getDAO();
      rejectedList = dao.fetchRejectedData(boeSearchVO);
    }
    catch (Exception e)
    {
      throwBDException(e);
    }
    logger.info("Exiting Method");
    return rejectedList;
  }
  public BoeVO deleteRejectedRecords(BoeVO boeVO)
    throws BusinessException
  {
    logger.info("Entering Method");
    BOERejectedDAO dao = null;
    try
    {
      dao = BOERejectedDAO.getDAO();
      boeVO = dao.deleteRecordPermanent(boeVO);
    }
    catch (Exception e)
    {
      throwBDException(e);
    }
    logger.info("Exiting Method");
    return boeVO;
  }
  public void delRejRecords(String[] chkList)
  {
    BOERejectedDAO dao = null;
    ArrayList<TransactionVO> rejectedList = null;
    try
    {
      dao = BOERejectedDAO.getDAO();
      dao.delRecord(chkList);
    }
    catch (Exception localException) {}
  }
  public void delRejRecords_bill_VsM(String[] bill_chkList)
  {
    logger.info("delRejRecords_bill_VsM-------------");
    BOERejectedDAO dao = null;
    ArrayList<TransactionVO> rejectedList = null;
    try
    {
      dao = BOERejectedDAO.getDAO();
      dao.delRecordBills(bill_chkList);
    }
    catch (Exception e)
    {
      logger.info("delRejRecords_bill_VsM()-----------Exception -->" + e.getMessage());
    }
  }
}
