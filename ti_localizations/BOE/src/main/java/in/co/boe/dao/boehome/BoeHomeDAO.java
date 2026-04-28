package in.co.boe.dao.boehome;

import com.opensymphony.xwork2.ActionContext;
import in.co.boe.dao.AbstractDAO;
import in.co.boe.dao.exception.DAOException;
import in.co.boe.utility.ActionConstantsQuery;
import in.co.boe.utility.CommonMethods;
import in.co.boe.utility.DBConnectionUtility;
import in.co.boe.utility.LoggableStatement;
import in.co.boe.utility.UserDetailsVO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Map;
import org.apache.log4j.Logger;
 
public class BoeHomeDAO
  extends AbstractDAO
  implements ActionConstantsQuery
{
  private static Logger logger = Logger.getLogger(BoeHomeDAO.class.getName());
  static BoeHomeDAO dao;
  String userName = null;
  public static BoeHomeDAO getDAO()
  {
    if (dao == null) {
      dao = new BoeHomeDAO();
    }
    return dao;
  }
  public UserDetailsVO fetchLoginedUserId(UserDetailsVO userVO)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement loggableStatement = null;
    ResultSet rs = null;
    String sqlQuery = null;
    try
    {
      con = DBConnectionUtility.getConnection();
      sqlQuery = "select skey80 from secage88 where name85 ='" + userVO.getSessionUserName() + "'";
      loggableStatement = new LoggableStatement(con, sqlQuery);
      logger.info("FetchLoginedUserId: " + loggableStatement.getQueryString());

 
      rs = loggableStatement.executeQuery();
      while (rs.next()) {
        userVO.setUserid(rs.getInt("SKEY80"));
      }
    }
    catch (Exception exception)
    {
      throwDAOException(exception);
    }
    finally
    {
      closeSqlRefferance(rs, loggableStatement, con);
    }
    logger.info("Exiting Method");
    return userVO;
  }
  public int checkLoginedUserType(UserDetailsVO userVO)
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
      if (!commonMethods.isNull(userVO.getSessionUserName()))
      {
        sqlQuery = 
          "SELECT COUNT(*) AS LOGIN_COUNT FROM SECAGE88 U,TEAMUSRMAP T WHERE T.USERKEY = U.SKEY80 AND TRIM(UPPER(U.NAME85))  = TRIM(UPPER('" + userVO.getSessionUserName() + "'))" + " AND TRIM(UPPER(T.TEAMKEY)) = TRIM(UPPER('" + userVO.getPageType() + "'))";
        loggableStatement = new LoggableStatement(con, sqlQuery);
        logger.info("Checker Stage QUery--------------->" + loggableStatement.getQueryString());
        rs = loggableStatement.executeQuery();
        if (rs.next()) {
          count = rs.getInt("LOGIN_COUNT");
        }
        logger.info("Checker Stage QUery-----Count---------->" + count);
      }
    }
    catch (Exception exception)
    {
      throwDAOException(exception);
    }
    finally
    {
      closeSqlRefferance(rs, loggableStatement, con);
    }
    return count;
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
      String query = "SELECT TO_CHAR(TO_DATE(PROCDATE, 'dd-mm-yy'),'dd-mm-yyyy') as PROCDATE1,PROCDATE FROM dlyprccycl";
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
}
