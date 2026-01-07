package in.co.localization.dao.localization;
 
import com.opensymphony.xwork2.ActionContext;
import in.co.localization.dao.AbstractDAO;
import in.co.localization.dao.exception.DAOException;
import in.co.localization.utility.ActionConstantsQuery;
import in.co.localization.utility.CommonMethods;
import in.co.localization.utility.DBConnectionUtility;
import in.co.localization.utility.LoggableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Map;
import org.apache.log4j.Logger;
 
public class AdTransferDAO
  extends AbstractDAO
  implements ActionConstantsQuery
{
  static AdTransferDAO dao;
  private static Logger logger = Logger.getLogger(AdTransferDAO.class
    .getName());
  public static AdTransferDAO getDAO()
  {
    if (dao == null) {
      dao = new AdTransferDAO();
    }
    return dao;
  }
  public String getUserId(String userName)
    throws DAOException
  {
    Connection con = null;
    LoggableStatement pst = null;
    String userId = null;
    ResultSet rs = null;
    try
    {
      logger.info("userName--------------------" + userName);
      logger.info("11111111111111111");
      con = DBConnectionUtility.getConnection();
      if (con != null) {
        logger.info("Conection Avaialbale");
      }
      logger.info("222222222222222");
      String getUserId = "select skey80 from secage88 where name85 =?";
      logger.info("3333333333333333333");
      pst = new LoggableStatement(con, getUserId);
      pst.setString(1, userName);
      logger.info("444444444444444444444444");
      logger.info("getUserId-------query----------" + pst.getQueryString());
      rs = pst.executeQuery();
      logger.info("555555555555555");
      while (rs.next())
      {
        userId = String.valueOf(rs.getInt("SKEY80"));
        logger.info("userId-----------" + userId);
      }
      logger.info("66666666666666666");
    }
    catch (Exception exception)
    {
      logger.info("getUserId  Exception-------------" + exception);
      throwDAOException(exception);
    }
    finally
    {
      closeSqlRefferance(rs, pst, con);
    }
    logger.info("Exiting Method");
    return userId;
  }
  public void setDate()
    throws DAOException
  {
    logger.info("Entering Method");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    CommonMethods commonMethods = null;
    try
    {
      commonMethods = new CommonMethods();
      Map<String, Object> session = ActionContext.getContext().getSession();
      con = DBConnectionUtility.getConnection();
      String query = "SELECT TO_CHAR(TO_DATE(PROCDATE, 'dd-mm-yy'),'dd-mm-yyyy') as PROCDATE1,PROCDATE FROM dlyprccycl WHERE ID='UBIFTI'";
      pst = new LoggableStatement(con, query);
      rs = pst.executeQuery();
      if (rs.next())
      {
        String dateValue = commonMethods.getEmptyIfNull(rs.getString("PROCDATE1")).trim();
        session.put("processDate", dateValue);
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        Timestamp dbSqlTimestamp = rs.getTimestamp("PROCDATE");
        String createdDate = df.format(dbSqlTimestamp);
        session.put("CREATEDATE", createdDate);
      }
    }
    catch (Exception exception)
    {
      throwDAOException(exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    logger.info("Exiting Method");
  }
  public int checkLoginedUserType(String userName, String pageType)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement loggableStatement = null;
    ResultSet rs = null;
    int count = 0;
    String sqlQuery = null;
    CommonMethods commonMethods = null;
    try
    {
      commonMethods = new CommonMethods();
      con = DBConnectionUtility.getConnection();
      logger.info("IDPMS V- checkLoginedUserType--userName---------->" + userName);
      logger.info("IDPMS V-checkLoginedUserType--userName---------->" + userName);
      if (!commonMethods.isNull(userName))
      {
        sqlQuery = "SELECT COUNT(1) AS LOGIN_COUNT FROM SECAGE88 U,TEAMUSRMAP T  WHERE T.USERKEY = U.SKEY80 AND TRIM(UPPER(U.NAME85))  = TRIM(UPPER(?)) AND TRIM(UPPER(T.TEAMKEY)) = TRIM(UPPER(?))";

 
        loggableStatement = new LoggableStatement(con, sqlQuery);
        loggableStatement.setString(1, userName);
        loggableStatement.setString(2, pageType);
        logger.info("checkLoginedUserType-----------Query---->" + loggableStatement.getQueryString());
        rs = loggableStatement.executeQuery();
        if (rs.next()) {
          count = rs.getInt("LOGIN_COUNT");
        }
        logger.info("Checker COunt-------------+" + count);
      }
    }
    catch (Exception exception)
    {
      logger.info("checkLoginedUserType----------->" + exception);
      throwDAOException(exception);
    }
    finally
    {
      closeSqlRefferance(rs, loggableStatement, con);
    }
    return count;
  }
}