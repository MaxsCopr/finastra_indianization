package in.co.FIRC.dao;

import com.opensymphony.xwork2.ActionContext;
import in.co.FIRC.dao.exception.DAOException;
import in.co.FIRC.dao.utility.DBConnectionUtility;
import in.co.FIRC.utility.ActionConstantsQuery;
import in.co.FIRC.utility.CommonMethods;
import in.co.FIRC.utility.DBUtility;
import in.co.FIRC.utility.LoggableStatement;
import in.co.FIRC.vo.UserDetailsVO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
 
public class FIRCHomeDAO
  extends DBConnectionUtility
  implements ActionConstantsQuery
{
  private static Logger logger = LogManager.getLogger(FIRCHomeDAO.class.getName());
  static FIRCHomeDAO dao;
  String userName = null;
  public static FIRCHomeDAO getDAO()
  {
    if (dao == null) {
      dao = new FIRCHomeDAO();
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
      con = getConnection();
      logger.info("Session UserName" + userVO.getSessionUserName());
      sqlQuery = "select skey80 from secage88 where name85 =?";
      loggableStatement = new LoggableStatement(con, sqlQuery);
      loggableStatement.setString(1, userVO.getSessionUserName());
      logger.info("FetchLoginedUserId: " + loggableStatement.getQueryString());
      rs = loggableStatement.executeQuery();
      while (rs.next())
      {
        userVO.setUserid(rs.getInt("SKEY80"));
        logger.info("FIRC HOME DAO Userid" + userVO.getUserid());
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
  public String checkLoginedUserType(String userVO)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement loggableStatement = null;
    ResultSet rs = null;
    String result = null;
    String sqlQuery = null;
    CommonMethods commonMethods = null;
    try
    {
      commonMethods = new CommonMethods();
      con = DBConnectionUtility.getConnection();
      if (!CommonMethods.isNull(userVO))
      {
        sqlQuery = "SELECT T.TEAMKEY as teamkey FROM SECAGE88 U LEFT JOIN TEAMUSRMAP T  ON T.USERKEY = U.SKEY80 WHERE TRIM(UPPER(U.NAME85))  = TRIM(UPPER(?))  AND TRIM(UPPER(T.TEAMKEY)) LIKE '%FIRC%'";

 
 
        loggableStatement = new LoggableStatement(con, sqlQuery);
        loggableStatement.setString(1, userVO);
        logger.info("CheckLoginedUserType: " + loggableStatement.getQueryString());
        rs = loggableStatement.executeQuery();
        if (rs.next())
        {
          result = rs.getString("teamkey");
          logger.info("Home DAO result" + result);
        }
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
    return result;
  }
  public void setDate()
    throws DAOException
  {
    logger.info("-----------------setDate---------------");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    try
    {
      Map<String, Object> session = ActionContext.getContext().getSession();
      con = DBConnectionUtility.getConnection();
      String query = "SELECT TO_CHAR(TO_DATE(PROCDATE, 'dd-mm-yy'),'dd-mm-yyyy') as PROCDATE1,TO_CHAR(TO_DATE(PROCDATE, 'dd-mm-yy'),'dd-mm-yy') as FIRCDATE,PROCDATE FROM dlyprccycl";
      pst = new LoggableStatement(con, query);
      rs = pst.executeQuery();
      logger.info("-----------------setDate-------query--------" + pst.getQueryString());
      if (rs.next())
      {
        String dateValue = DBUtility.getEmptyIfNull(rs.getString("PROCDATE1")).trim();
        session.put("processDate", dateValue);
        logger.info("------------------Datae" + dateValue);
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        Timestamp dbSqlTimestamp = rs.getTimestamp("PROCDATE");
        String createdDate = df.format(dbSqlTimestamp);
        session.put("CREATEDATE", createdDate);
        String fircDate = DBUtility.getEmptyIfNull(rs.getString("FIRCDATE")).trim();
        session.put("fircUtilizedDate", fircDate);
      }
    }
    catch (Exception exception)
    {
      logger.info("-----------------setDate---exception------------" + exception);
      throwDAOException(exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    logger.info("Exiting Method");
  }
  public String getRole(String userVO)
    throws DAOException
  {
    logger.info("------------------getRole-------------------------");
    Connection db_con = null;
    LoggableStatement lst1 = null;
    ResultSet rs1 = null;
    String result_value = null;
    String sqlQuery = null;
    CommonMethods commonMethods = null;
    try
    {
      commonMethods = new CommonMethods();
      db_con = DBConnectionUtility.getConnection();
      if (!CommonMethods.isNull(userVO))
      {
        logger.info("-------------userVO----------------" + userVO);
        sqlQuery = "SELECT COUNT(*) as VAL FROM SECAGE88 U LEFT JOIN TEAMUSRMAP T  ON T.USERKEY = U.SKEY80  WHERE TRIM(UPPER(U.NAME85))  = TRIM(UPPER(?)) AND TRIM(UPPER(T.TEAMKEY)) LIKE '%FIRC%' group by U.NAME85 ";

 
        lst1 = new LoggableStatement(db_con, sqlQuery);
        lst1.setString(1, userVO);
        logger.info("------------------getRole-------------------query------" + lst1.getQueryString());
        rs1 = lst1.executeQuery();
        if (rs1.next())
        {
          result_value = String.valueOf(rs1.getInt("VAL"));
          logger.info("FIRC role count >>>>>> " + result_value);
        }
      }
    }
    catch (Exception exception)
    {
      logger.info("------------------getRole----------------exception---------" + exception);
      throwDAOException(exception);
    }
    finally
    {
      closeSqlRefferance(null, lst1, db_con);
    }
    return result_value;
  }
}
