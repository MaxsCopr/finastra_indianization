package in.co.ebrc.businessdelegate.pricereftomanybill;

import in.co.ebrc.businessdelegate.BaseBusinessDelegate;
import in.co.ebrc.businessdelegate.exception.BusinessException;
import in.co.ebrc.dao.customername.EbrcDataProcessDAO;
import in.co.ebrc.vo.EbrcDataProcessVO;
import in.co.ebrc.vo.EbrcProcessVO;
import java.util.ArrayList;
import java.util.Map;
import org.apache.log4j.Logger;
 
public class EbrcCertificateProcess
  extends BaseBusinessDelegate
{
  private static Logger logger = Logger.getLogger(EbrcCertificateProcess.class
    .getName());
  static EbrcCertificateProcess bd;
  EbrcProcessVO ebrcVO;
  Map<String, String> ebrcStatus;
  Map<String, String> status;
  EbrcDataProcessVO ebrcDataVO;
  public static EbrcCertificateProcess getBD()
  {
    if (bd == null) {
      bd = new EbrcCertificateProcess();
    }
    return bd;
  }
  public EbrcProcessVO getEbrcVO()
  {
    return this.ebrcVO;
  }
  public void setEbrcVO(EbrcProcessVO ebrcVO)
  {
    this.ebrcVO = ebrcVO;
  }
  public Map<String, String> getEbrcStatus()
  {
    return this.ebrcStatus;
  }
  public void setEbrcStatus(Map<String, String> ebrcStatus)
  {
    this.ebrcStatus = ebrcStatus;
  }
  public Map<String, String> getstatus()
  {
    return this.status;
  }
  public void setstatus(Map<String, String> status)
  {
    this.status = status;
  }
  public EbrcDataProcessVO getEbrcDataVO()
  {
    return this.ebrcDataVO;
  }
  public void setEbrcDataVO(EbrcDataProcessVO ebrcDataVO)
  {
    this.ebrcDataVO = ebrcDataVO;
  }
  public EbrcProcessVO getDataFromTI(EbrcProcessVO ebrcVO)
    throws BusinessException
  {
    logger.info("Entering Method");
    EbrcDataProcessDAO dao = null;
    ArrayList<EbrcProcessVO> list = null;
    try
    {
      list = new ArrayList();
      dao = EbrcDataProcessDAO.getDAO();
      ebrcVO = dao.getEbrcDetails(ebrcVO);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return ebrcVO;
  }
  public ArrayList<EbrcProcessVO> fetchEbrcValue(EbrcProcessVO ebrcVO)
    throws BusinessException
  {
    logger.info("Entering Method");
    ArrayList<EbrcProcessVO> list = null;
    EbrcDataProcessDAO dao = null;
    try
    {
      list = new ArrayList();
      dao = EbrcDataProcessDAO.getDAO();
      list = dao.fetchEbrcValue(ebrcVO);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return list;
  }
  public EbrcProcessVO getGridValue(EbrcProcessVO ebrcVO)
    throws BusinessException
  {
    logger.info("Entering Method");
    EbrcDataProcessDAO dao = null;
    EbrcProcessVO ebrcdataVO = null;
    ArrayList<EbrcProcessVO> list = null;
    try
    {
      list = new ArrayList();
      dao = EbrcDataProcessDAO.getDAO();
      ebrcVO = dao.getGridDetails(ebrcVO);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return ebrcVO;
  }
  public String xmlGenerate()
    throws BusinessException
  {
    logger.info("Entering Method");
    EbrcDataProcessDAO dao = null;
    String resultcancel = null;
    String resultfresh = null;
    try
    {
      logger.info("----");
      dao = EbrcDataProcessDAO.getDAO();
      resultcancel = dao.cancelXmlGenerate();
      logger.info("result--cancel" + resultcancel);
      resultfresh = dao.xmlGenerate();
      logger.info("result--" + resultfresh);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    if (resultfresh != null) {
      return resultfresh;
    }
    return resultcancel;
  }
  public void generateIFSCXmlData()
    throws BusinessException
  {
    logger.info("Entering Method");
    EbrcDataProcessDAO dao = null;
    try
    {
      dao = EbrcDataProcessDAO.getDAO();
      dao.ifscXMLGenerate();
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
  }
  public void storeEbrcData(EbrcProcessVO ebrcVO)
    throws BusinessException
  {
    logger.info("Entering Method");
    EbrcDataProcessDAO invProcess = null;
    try
    {
      invProcess = EbrcDataProcessDAO.getDAO();
      invProcess.storeEbrcDetails(ebrcVO);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
  }
  public void setDate()
    throws BusinessException
  {
    logger.info("Entering Method");
    EbrcDataProcessDAO dao = null;
    try
    {
      dao = EbrcDataProcessDAO.getDAO();
      dao.setDate();
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
  }
}
