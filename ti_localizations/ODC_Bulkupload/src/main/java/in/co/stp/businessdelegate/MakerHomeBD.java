package in.co.stp.businessdelegate;

import in.co.stp.action.MakerHomeAction;
import in.co.stp.businessdelegate.exception.BusinessException;
import in.co.stp.dao.MakerHomeDAO;
import in.co.stp.vo.MakerHomeVO;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
 
public class MakerHomeBD
  extends BaseBusinessDelegate
{
  private static Logger logger = LogManager.getLogger(MakerHomeAction.class
    .getName());
  static MakerHomeBD bd;
  public static MakerHomeBD getBD()
  {
    if (bd == null) {
      bd = new MakerHomeBD();
    }
    return bd;
  }
  public void setDate()
    throws BusinessException
  {
    logger.info("Entering Method");
    MakerHomeDAO dao = null;
    try
    {
      dao = MakerHomeDAO.getDAO();
      logger.info("Entering BD");
      dao.setDate();
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
  }
  public void isSessionAvailable()
    throws BusinessException
  {
    logger.info("Entering Method");
    MakerHomeDAO dao = null;
    try
    {
      dao = MakerHomeDAO.getDAO();
      dao.isSessionAvailable();
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
  }
  public ArrayList<MakerHomeVO> getBranchList(MakerHomeVO makervo)
    throws BusinessException
  {
    logger.info("Entering Method");
    MakerHomeDAO dao = null;
    ArrayList<MakerHomeVO> branchList = null;
    try
    {
      dao = MakerHomeDAO.getDAO();
      branchList = dao.getBranchList(makervo);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return branchList;
  }
  public MakerHomeVO getExcelLoad(MakerHomeVO makervo)
    throws BusinessException
  {
    logger.info("Entering Method");
    MakerHomeDAO dao = null;
    try
    {
      dao = MakerHomeDAO.getDAO();
      makervo = dao.getExcelLoad(makervo);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return makervo;
  }
  public String getExcelValidate(String batchId, MakerHomeVO makervo)
    throws BusinessException
  {
    logger.info("Entering Method");
    MakerHomeDAO dao = null;
    try
    {
      dao = MakerHomeDAO.getDAO();
      dao.getExcelValidate(batchId, makervo);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return "success";
  }
  public MakerHomeVO retrieveTransactionList(MakerHomeVO makervo)
    throws BusinessException
  {
    logger.info("Entering Method");
    MakerHomeDAO dao = null;
    try
    {
      dao = MakerHomeDAO.getDAO();
      makervo = dao.fetchInvoiceDetails(makervo);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return makervo;
  }
  public MakerHomeVO retrieveTransactionList1(MakerHomeVO makervo)
    throws BusinessException
  {
    logger.info("Entering Method");
    MakerHomeDAO dao = null;
    try
    {
      dao = MakerHomeDAO.getDAO();
      makervo = dao.fetchInvoiceDetails1(makervo);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return makervo;
  }
  public boolean getUploadStatus(String batchId)
    throws BusinessException
  {
    logger.info("Entering Method");
    MakerHomeDAO dao = null;
    boolean uploadStatus = false;
    try
    {
      dao = MakerHomeDAO.getDAO();
      uploadStatus = dao.getUploadStatus(batchId);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return uploadStatus;
  }
  public void setUploadStatus(String batchId)
    throws BusinessException
  {
    logger.info("Entering Method");
    MakerHomeDAO dao = null;
    try
    {
      dao = MakerHomeDAO.getDAO();
      dao.setUploadStatus(batchId);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
  }
  public int makerUpload(MakerHomeVO makervo)
    throws BusinessException
  {
    logger.info("Entering Method");
    MakerHomeDAO dao = null;
    int count = 0;
    try
    {
      dao = MakerHomeDAO.getDAO();
      count = dao.makerUpload(makervo);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return count;
  }
  public int reject(MakerHomeVO makervo)
    throws BusinessException
  {
    MakerHomeDAO dao = null;
    int count = 0;
    try
    {
      dao = MakerHomeDAO.getDAO();
      count = dao.rejectAll(makervo);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    return count;
  }
}
