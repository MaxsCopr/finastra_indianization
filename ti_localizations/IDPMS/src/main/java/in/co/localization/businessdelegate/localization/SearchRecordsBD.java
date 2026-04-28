package in.co.localization.businessdelegate.localization;
 
import in.co.localization.businessdelegate.BaseBusinessDelegate;
import in.co.localization.businessdelegate.exception.BusinessException;
import in.co.localization.dao.localization.SearchRecordsDAO;
import in.co.localization.vo.localization.BOEDataSearchVO;
import in.co.localization.vo.localization.BoeVO;
import in.co.localization.vo.localization.IdpmsOrmVO;
import java.util.ArrayList;
import org.apache.log4j.Logger;
 
public class SearchRecordsBD
  extends BaseBusinessDelegate
{
  private static Logger logger = Logger.getLogger(SearchRecordsBD.class.getName());
  static SearchRecordsBD bd;
  public static SearchRecordsBD getBD()
  {
    if (bd == null) {
      bd = new SearchRecordsBD();
    }
    return bd;
  }
  public ArrayList<BoeVO> boeExSearch(BOEDataSearchVO boeSearchVO)
    throws BusinessException
  {
    logger.info("----------boeExSearch-------");
    SearchRecordsDAO dao = null;
    ArrayList<BoeVO> boeList = null;
    try
    {
      dao = SearchRecordsDAO.getDAO();
      boeList = dao.boeExSearch(boeSearchVO);
    }
    catch (Exception e)
    {
      logger.info("----------boeExSearch-----exception--" + e);
      throwBDException(e);
    }
    logger.info("Exiting Method");
    return boeList;
  }
  public ArrayList<BoeVO> boeClSearch(BOEDataSearchVO boeSearchVO)
    throws BusinessException
  {
    logger.info("---------------------boeClSearch---------------------");
    SearchRecordsDAO dao = null;
    ArrayList<BoeVO> boeList = null;
    try
    {
      dao = SearchRecordsDAO.getDAO();
      boeList = dao.boeClSearch(boeSearchVO);
    }
    catch (Exception e)
    {
      logger.info("---------------------boeClSearch---------exception------------" + e);
      throwBDException(e);
    }
    logger.info("Exiting Method");
    return boeList;
  }
  public ArrayList<IdpmsOrmVO> ormClSearch(BOEDataSearchVO boeSearchVO)
    throws BusinessException
  {
    logger.info("------------ormClSearch-------------");
    SearchRecordsDAO dao = null;
    ArrayList<IdpmsOrmVO> idpmsOrmVOList = null;
    try
    {
      dao = SearchRecordsDAO.getDAO();
      idpmsOrmVOList = dao.ormClSearch(boeSearchVO);
    }
    catch (Exception e)
    {
      logger.info("ormClSearch--------------" + e);
      throwBDException(e);
    }
    return idpmsOrmVOList;
  }
}