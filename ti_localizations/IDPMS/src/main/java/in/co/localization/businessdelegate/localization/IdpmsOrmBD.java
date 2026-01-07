package in.co.localization.businessdelegate.localization;
 
import in.co.localization.businessdelegate.BaseBusinessDelegate;
import in.co.localization.businessdelegate.exception.BusinessException;
import in.co.localization.dao.localization.IdpmsOrmDAO;
import in.co.localization.vo.localization.BOEDataSearchVO;
import in.co.localization.vo.localization.IdpmsOrmVO;
import java.util.ArrayList;
import org.apache.log4j.Logger;
 
public class IdpmsOrmBD
  extends BaseBusinessDelegate
{
  private static Logger logger = Logger.getLogger(IdpmsOrmBD.class.getName());
  static IdpmsOrmBD bd;
  public static IdpmsOrmBD getBD()
  {
    if (bd == null) {
      bd = new IdpmsOrmBD();
    }
    return bd;
  }
  public IdpmsOrmVO fetchormData(IdpmsOrmVO idpmsVO)
    throws BusinessException
  {
    logger.info("Entering Method");
    IdpmsOrmDAO dao = null;
    try
    {
      dao = IdpmsOrmDAO.getDAO();
      idpmsVO = dao.fetchormData(idpmsVO);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return idpmsVO;
  }
  public int insertOrm(IdpmsOrmVO Indvo)
    throws BusinessException
  {
    logger.info("Entering Method");
    IdpmsOrmDAO dao = null;
    int result = 0;
    try
    {
      dao = IdpmsOrmDAO.getDAO();
      result = dao.insertOrm(Indvo);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return result;
  }
  public ArrayList<IdpmsOrmVO> idpmsOrmList(BOEDataSearchVO boeSearchVO)
  {
    logger.info("--------idpmsOrmList------------");
    IdpmsOrmDAO dao = null;
    ArrayList<IdpmsOrmVO> idpmsOrmList = null;
    try
    {
      dao = IdpmsOrmDAO.getDAO();
      idpmsOrmList = dao.idpmsOrmList(boeSearchVO);
    }
    catch (Exception e)
    {
      logger.info("idpmsOrmList------------>" + e);
    }
    logger.info("Exiting Method");
    return idpmsOrmList;
  }
  public ArrayList<IdpmsOrmVO> insertOrm(String[] ormChkList, String ormStatus, String remarks, BOEDataSearchVO boeSearchVO)
    throws BusinessException
  {
    logger.info("------------insertOrm------------");
    IdpmsOrmDAO dao = null;
    ArrayList<IdpmsOrmVO> alist = null;
    try
    {
      dao = IdpmsOrmDAO.getDAO();
      alist = dao.insertOrm(ormChkList, ormStatus, remarks, boeSearchVO);
    }
    catch (Exception e)
    {
      logger.info("error insertOrm of action " + e.getMessage());
    }
    logger.info("Exiting Method");
    return alist;
  }
  public IdpmsOrmVO ormVIEW(BOEDataSearchVO boeSearchVO)
    throws BusinessException
  {
    logger.info("Entering Method");
    IdpmsOrmDAO dao = null;
    IdpmsOrmVO imvo = null;
    try
    {
      dao = IdpmsOrmDAO.getDAO();
      imvo = dao.ormVIEW(boeSearchVO);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return imvo;
  }
  public boolean checkOrmOutstandingAmount(String ormNo, String closureAmt)
    throws BusinessException
  {
    IdpmsOrmDAO dao = null;
    boolean result = true;
    try
    {
      dao = IdpmsOrmDAO.getDAO();
      result = dao.checkOrmOutstandingAmount(ormNo, closureAmt);
    }
    catch (Exception e)
    {
      throwBDException(e);
    }
    return result;
  }
  public int getORMCount(IdpmsOrmVO Indvo)
    throws BusinessException
  {
    logger.info("Entering Method");
    IdpmsOrmDAO dao = null;
    int count = 0;
    try
    {
      dao = IdpmsOrmDAO.getDAO();
      count = dao.getOrmCount(Indvo);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return count;
  }
  public int getORMAckCount(IdpmsOrmVO Indvo)
    throws BusinessException
  {
    logger.info("Entering Method");
    IdpmsOrmDAO dao = null;
    int count = 0;
    try
    {
      dao = IdpmsOrmDAO.getDAO();
      count = dao.getORMAckCount(Indvo);
    }
    catch (Exception exception)
    {
      throwBDException(exception);
    }
    logger.info("Exiting Method");
    return count;
  }
}