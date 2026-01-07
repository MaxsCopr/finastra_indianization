package in.co.localization.dao;

import in.co.localization.dao.exception.DAOException;
import in.co.localization.utility.ActionConstantsQuery;
import in.co.localization.utility.DBConnectionUtility;
import in.co.localization.utility.LogHelper;
import in.co.localization.utility.LoggableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.sql.DataSource;
import org.apache.log4j.Logger;

public class AbstractDAO
  implements ActionConstantsQuery
{
  private static Logger logger = Logger.getLogger(AbstractDAO.class.getName());
  protected static DataSource dataSource = null;
  public boolean logDebug = false;
 
  protected Connection getConnection()
    throws Exception
  {
    logger.debug("Entering Method");
    Connection conn = null;
    String userName = "root";
    String password = "root";
   
    String url = "jdbc:mysql://localhost:3306/demohab_myshare";
    Class.forName("com.mysql.jdbc.Driver").newInstance();
    conn = DriverManager.getConnection(url, userName, password);
   
    logger.debug("Exiting Method");
    return conn;
  }
 
  public void closeConnection(Connection myConnection)
  {
    try
    {
      if (myConnection != null) {
        myConnection.close();
      }
    }
    catch (Exception excepConnection)
    {
      logger.error(excepConnection.fillInStackTrace());
    }
  }
 
  public void closePreparedStatement(PreparedStatement preparedStatement)
  {
    try
    {
      if (preparedStatement != null) {
        preparedStatement.close();
      }
    }
    catch (Exception excepConnection)
    {
      logger.error(excepConnection.fillInStackTrace());
    }
  }
 
  public void closeStatementResultSet(LoggableStatement loggableStatement, ResultSet resultSet)
  {
    try
    {
      if (resultSet != null) {
        resultSet.close();
      }
      if (loggableStatement != null) {
        loggableStatement.close();
      }
    }
    catch (Exception excepConnection)
    {
      logger.error(excepConnection.fillInStackTrace());
    }
  }
 
  public void closeResultSet(ResultSet resultSet)
  {
    try
    {
      if (resultSet != null) {
        resultSet.close();
      }
    }
    catch (Exception excepConnection)
    {
      logger.error(excepConnection.fillInStackTrace());
    }
  }
 
  public void closeStatement(Statement statement)
  {
    try
    {
      if (statement != null) {
        statement.close();
      }
    }
    catch (Exception excepConnection)
    {
      logger.error(excepConnection.fillInStackTrace());
    }
  }
 
  public void throwDAOException(Exception exception)
    throws DAOException
  {
    logger.error(exception.fillInStackTrace());
    LogHelper.logError(logger, exception);
    throw new DAOException(exception.getMessage());
  }
 
  public void closeSqlRefferance(PreparedStatement preparedStatement, Connection connection)
  {
    closePreparedStatement(preparedStatement);
    closeConnection(connection);
  }
 
  public void closeSqlRefferance(ResultSet result, PreparedStatement preparedStatement)
  {
    closeResultSet(result);
    closePreparedStatement(preparedStatement);
  }
 
  public void closeSqlRefferance(ResultSet result, PreparedStatement preparedStatement, Connection connection)
  {
    closeResultSet(result);
    closePreparedStatement(preparedStatement);
    closeConnection(connection);
  }
 
  public boolean isOneDataAvailable(String tableName, String columnName, String value)
    throws DAOException
  {
    logger.error("Entering Method");
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    boolean result = false;
    String query = null;
    try
    {
      connection = DBConnectionUtility.getConnection();
      query = "SELECT COUNT(1) FROM  TABLE  WHERE COLUMN=?";
      query = query.replace("TABLE_NAME", tableName);
      query = query.replace("COLUMN_NAME1", columnName);
      preparedStatement = connection.prepareStatement(query);
      preparedStatement.setString(1, value);
      resultSet = preparedStatement.executeQuery();
      resultSet.next();
      if (resultSet.getInt(1) > 0) {
        result = true;
      } else {
        result = false;
      }
    }
    catch (Exception exception)
    {
      throwDAOException(exception);
    }
    finally
    {
      closeSqlRefferance(resultSet, preparedStatement, connection);
    }
    logger.info("Exiting Method");
    return result;
  }
 
  public int isTwoDataAvailable(String tableName, String columnName1, String columnName2, String value1, String value2)
    throws DAOException
  {
    logger.error("Entering Method");
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    int result = 0;
    String query = null;
    try
    {
      connection = DBConnectionUtility.getConnection();
      query = "SELECT COUNT(1) as COUNT FROM  TABLE_NAME  WHERE COLUMN_NAME1=? AND COLUMN_NAME2=? ";
      query = query.replace("TABLE_NAME", tableName);
      query = query.replace("COLUMN_NAME1", columnName1);
      query = query.replace("COLUMN_NAME2", columnName2);
      preparedStatement = connection.prepareStatement(query);
      preparedStatement.setString(1, value1);
      preparedStatement.setString(2, value2);
      resultSet = preparedStatement.executeQuery();
      resultSet.next();
      if (resultSet.getInt(1) > 0) {
        result = resultSet.getInt("COUNT");
      } else {
        result = resultSet.getInt("COUNT");
      }
    }
    catch (Exception exception)
    {
      throwDAOException(exception);
    }
    finally
    {
      closeSqlRefferance(resultSet, preparedStatement, connection);
    }
    logger.info("Exiting Method");
    return result;
  }
 
  public int isThreeDataAvailable(String tableName, String columnName1, String columnName2, String columnName3, String value1, String value2, String value3)
    throws DAOException
  {
    logger.error("Entering Method");
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    int result = 0;
    try
    {
      connection = DBConnectionUtility.getConnection();
      String query = "SELECT COUNT(1) as COUNT FROM  TABLE_NAME  WHERE COLUMN_NAME1=? AND COLUMN_NAME2=? AND COLUMN_NAME3=?";
     
      query = query.replace("TABLE_NAME", tableName);
      query = query.replace("COLUMN_NAME1", columnName1);
      query = query.replace("COLUMN_NAME2", columnName2);
      query = query.replace("COLUMN_NAME3", columnName3);
      preparedStatement = connection.prepareStatement(query);
      preparedStatement.setString(1, value1);
      preparedStatement.setString(2, value2);
      preparedStatement.setString(3, value3);
      resultSet = preparedStatement.executeQuery();
      resultSet.next();
      if (resultSet.getInt(1) > 0) {
        result = resultSet.getInt("COUNT");
      } else {
        result = resultSet.getInt("COUNT");
      }
    }
    catch (Exception exception)
    {
      throwDAOException(exception);
    }
    finally
    {
      closeSqlRefferance(resultSet, preparedStatement, connection);
    }
    logger.info("Exiting Method");
    return result;
  }
 
  public boolean isDataAvailable(String tableName, String columnName1, String columnName2, String columnName3, String value1, String value2, String value3)
    throws DAOException
  {
    logger.error("Entering Method");
    Connection connection = null;
    LoggableStatement preparedStatement = null;
    ResultSet resultSet = null;
    boolean result = false;
    try
    {
      connection = DBConnectionUtility.getConnection();
      String query = "SELECT COUNT(1) FROM  TABLE_NAME  WHERE COLUMN_NAME1=? AND COLUMN_NAME2=? AND COLUMN_NAME3=?";
     
      query = query.replace("TABLE_NAME", tableName);
      query = query.replace("COLUMN_NAME1", columnName1);
      query = query.replace("COLUMN_NAME2", columnName2);
      query = query.replace("COLUMN_NAME3", columnName3);
      preparedStatement = new LoggableStatement(connection, query);
      preparedStatement.setString(1, value1);
      preparedStatement.setString(2, value2);
      preparedStatement.setString(3, value3);
      resultSet = preparedStatement.executeQuery();
     
      resultSet.next();
      if (resultSet.getInt(1) > 0) {
        result = true;
      } else {
        result = false;
      }
    }
    catch (Exception exception)
    {
      throwDAOException(exception);
    }
    finally
    {
      closeSqlRefferance(resultSet, preparedStatement, connection);
    }
    logger.info("Exiting Method");
    return result;
  }
 
  public boolean isGRShpDataAvailable(String tableName, String columnName1, String columnName2, String columnName3, String columnName4, String columnName5, String value1, String value2, String value3, String value4, String value5)
    throws DAOException
  {
    logger.error("Entering Method");
    Connection connection = null;
    LoggableStatement preparedStatement = null;
    ResultSet resultSet = null;
    boolean result = false;
    try
    {
      connection = DBConnectionUtility.getConnection();
      String query = "SELECT COUNT(1) FROM  TABLE_NAME  WHERE COLUMN_NAME1=? AND COLUMN_NAME2=? AND COLUMN_NAME3=? AND COLUMN_NAME4=? AND COLUMN_NAME5=?";
     
      query = query.replace("TABLE_NAME", tableName);
      query = query.replace("COLUMN_NAME1", columnName1);
      query = query.replace("COLUMN_NAME2", columnName2);
      query = query.replace("COLUMN_NAME3", columnName3);
      query = query.replace("COLUMN_NAME4", columnName4);
      query = query.replace("COLUMN_NAME5", columnName5);
      preparedStatement = new LoggableStatement(connection, query);
      preparedStatement.setString(1, value1);
      preparedStatement.setString(2, value2);
      preparedStatement.setString(3, value3);
      preparedStatement.setString(4, value4);
      preparedStatement.setString(5, value5);
      resultSet = preparedStatement.executeQuery();
     
      resultSet.next();
      if (resultSet.getInt(1) > 0) {
        result = true;
      } else {
        result = false;
      }
    }
    catch (Exception exception)
    {
      throwDAOException(exception);
    }
    finally
    {
      closeSqlRefferance(resultSet, preparedStatement, connection);
    }
    logger.info("Exiting Method");
    return result;
  }
 
  public boolean isGRInvDataAvailable(String tableName, String columnName1, String columnName2, String columnName3, String columnName4, String value1, String value2, String value3, String value4)
    throws DAOException
  {
    logger.error("Entering Method");
    Connection connection = null;
    LoggableStatement preparedStatement = null;
    ResultSet resultSet = null;
    boolean result = false;
    try
    {
      connection = DBConnectionUtility.getConnection();
      String query = "SELECT COUNT(1) FROM  TABLE_NAME  WHERE COLUMN_NAME1=? AND COLUMN_NAME2=? AND COLUMN_NAME3=? AND COLUMN_NAME4=?";
     
      query = query.replace("TABLE_NAME", tableName);
      query = query.replace("COLUMN_NAME1", columnName1);
      query = query.replace("COLUMN_NAME2", columnName2);
      query = query.replace("COLUMN_NAME3", columnName3);
      query = query.replace("COLUMN_NAME4", columnName4);
      preparedStatement = new LoggableStatement(connection, query);
      preparedStatement.setString(1, value1);
      preparedStatement.setString(2, value2);
      preparedStatement.setString(3, value3);
      preparedStatement.setString(4, value4);
      resultSet = preparedStatement.executeQuery();
     
      resultSet.next();
      if (resultSet.getInt(1) > 0) {
        result = true;
      } else {
        result = false;
      }
    }
    catch (Exception exception)
    {
      throwDAOException(exception);
    }
    finally
    {
      closeSqlRefferance(resultSet, preparedStatement, connection);
    }
    logger.info("Exiting Method");
    return result;
  }
 
  public boolean isFourDataAvailable(String tableName, String columnName1, String columnName2, String columnName3, String columnName4, String value1, String value2, String value3, String value4)
    throws DAOException
  {
    logger.error("Entering Method");
    Connection connection = null;
    LoggableStatement preparedStatement = null;
    ResultSet resultSet = null;
    boolean result = false;
    try
    {
      connection = DBConnectionUtility.getConnection();
      String query = "SELECT COUNT(1) FROM  TABLE_NAME  WHERE COLUMN_NAME1=? AND COLUMN_NAME2=? AND COLUMN_NAME3=? AND COLUMN_NAME4=?";
     
      query = query.replace("TABLE_NAME", tableName);
      query = query.replace("COLUMN_NAME1", columnName1);
      query = query.replace("COLUMN_NAME2", columnName2);
      query = query.replace("COLUMN_NAME3", columnName3);
      query = query.replace("COLUMN_NAME4", columnName4);
      preparedStatement = new LoggableStatement(connection, query);
      preparedStatement.setString(1, value1);
      preparedStatement.setString(2, value2);
      preparedStatement.setString(3, value3);
      preparedStatement.setString(4, value4);
      resultSet = preparedStatement.executeQuery();
     
      resultSet.next();
      if (resultSet.getInt(1) > 0) {
        result = true;
      } else {
        result = false;
      }
    }
    catch (Exception exception)
    {
      throwDAOException(exception);
    }
    finally
    {
      closeSqlRefferance(resultSet, preparedStatement, connection);
    }
    logger.info("Exiting Method");
    return result;
  }
 
  public static String getErrorDesc(String errorCD, String screenId)
  {
    String errorMag = "";
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    try
    {
      con = DBConnectionUtility.getConnection();
      String query = "SELECT ERROR_MSG FROM ETT_ERROR_CODES WHERE ERROR_CODE=? AND SCREEN_ID=?";
      pst = con.prepareStatement(query);
      pst.setString(1, errorCD);
      pst.setString(2, screenId);
      rs = pst.executeQuery();
      if (rs.next()) {
        errorMag = rs.getString(1);
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    return errorMag;
  }
 
  public boolean isTIShpDataAvailable(String value1, String value2, String value3)
    throws DAOException
  {
    logger.error("Entering Method");
    Connection connection = null;
    LoggableStatement preparedStatement = null;
    ResultSet resultSet = null;
    boolean result = false;
    try
    {
      connection = DBConnectionUtility.getConnection();
      String query = "SELECT COUNT(1) FROM EXTEVENTSPD  WHERE BILLNO='" +
        value1 +
        "' AND TO_CHAR(TO_DATE(BILLDATE, 'dd-mm-yy'),'ddmmyyyy')='" +
        value2 +
        "' AND PORTCODE='" +
        value3 + "'";
     
      preparedStatement = new LoggableStatement(connection, query);
      resultSet = preparedStatement.executeQuery();
      resultSet.next();
      if (resultSet.getInt(1) > 0) {
        result = true;
      } else {
        result = false;
      }
    }
    catch (Exception exception)
    {
      throwDAOException(exception);
    }
    finally
    {
      closeSqlRefferance(resultSet, preparedStatement, connection);
    }
    logger.info("Exiting Method");
    return result;
  }
 
  public boolean isTIInvDataAvailable(String value1, String value2, String value3)
    throws DAOException
  {
    logger.error("Entering Method");
    Connection connection = null;
    LoggableStatement preparedStatement = null;
    ResultSet resultSet = null;
    boolean result = false;
    try
    {
      connection = DBConnectionUtility.getConnection();
      String query = "SELECT COUNT(1) FROM EXTEVENTINV  WHERE INVBLLNO='" +
        value1 +
        "' AND TO_CHAR(TO_DATE(INBLLDAT, 'dd-mm-yy'),'ddmmyyyy')='" +
        value2 +
        "' AND INVPRTCD='" +
        value3 + "'";
     
      preparedStatement = new LoggableStatement(connection, query);
      resultSet = preparedStatement.executeQuery();
      resultSet.next();
      if (resultSet.getInt(1) > 0) {
        result = true;
      } else {
        result = false;
      }
    }
    catch (Exception exception)
    {
      throwDAOException(exception);
    }
    finally
    {
      closeSqlRefferance(resultSet, preparedStatement, connection);
    }
    logger.info("Exiting Method");
    return result;
  }
 
  public boolean isGRShpRelDataAvailable(String tableName, String columnName1, String columnName2, String columnName3, String columnName4, String columnName5, String columnName6, String value1, String value2, String value3, String value4, String value5, String value6)
    throws DAOException
  {
    logger.error("Entering Method");
    Connection connection = null;
    LoggableStatement preparedStatement = null;
    ResultSet resultSet = null;
    boolean result = false;
    try
    {
      connection = DBConnectionUtility.getConnection();
      String query = "SELECT COUNT(1) FROM  TABLE_NAME  WHERE COLUMN_NAME1=? AND COLUMN_NAME2=? AND COLUMN_NAME3=?  AND COLUMN_NAME4=? AND COLUMN_NAME5=? AND COLUMN_NAME6=?";
     
      query = query.replace("TABLE_NAME", tableName);
      query = query.replace("COLUMN_NAME1", columnName1);
      query = query.replace("COLUMN_NAME2", columnName2);
      query = query.replace("COLUMN_NAME3", columnName3);
      query = query.replace("COLUMN_NAME4", columnName4);
      query = query.replace("COLUMN_NAME5", columnName5);
      query = query.replace("COLUMN_NAME6", columnName6);
     
      preparedStatement = new LoggableStatement(connection, query);
      preparedStatement.setString(1, value1);
      preparedStatement.setString(2, value2);
      preparedStatement.setString(3, value3);
      preparedStatement.setString(4, value4);
      preparedStatement.setString(5, value5);
      preparedStatement.setString(6, value6);
      resultSet = preparedStatement.executeQuery();
      resultSet.next();
      if (resultSet.getInt(1) > 0) {
        result = true;
      } else {
        result = false;
      }
    }
    catch (Exception exception)
    {
      throwDAOException(exception);
    }
    finally
    {
      closeSqlRefferance(resultSet, preparedStatement, connection);
    }
    logger.info("Exiting Method");
    return result;
  }
 
  public boolean isGRInvRelDataAvailable(String tableName, String columnName1, String columnName2, String columnName3, String columnName4, String columnName5, String columnName6, String columnName7, String columnName8, String value1, String value2, String value3, String value4, String value5, String value6, String value7, String value8)
    throws DAOException
  {
    logger.error("Entering Method");
    Connection connection = null;
    LoggableStatement preparedStatement = null;
    ResultSet resultSet = null;
    boolean result = false;
    try
    {
      connection = DBConnectionUtility.getConnection();
      String query = "SELECT COUNT(1) FROM  TABLE_NAME  WHERE COLUMN_NAME1=? AND COLUMN_NAME2=? AND COLUMN_NAME3=? \tAND COLUMN_NAME4=? AND COLUMN_NAME5=? AND COLUMN_NAME6=? AND COLUMN_NAME7=? AND COLUMN_NAME8=? ";
     
      query = query.replace("TABLE_NAME", tableName);
      query = query.replace("COLUMN_NAME1", columnName1);
      query = query.replace("COLUMN_NAME2", columnName2);
      query = query.replace("COLUMN_NAME3", columnName3);
      query = query.replace("COLUMN_NAME4", columnName4);
      query = query.replace("COLUMN_NAME5", columnName5);
      query = query.replace("COLUMN_NAME6", columnName6);
      query = query.replace("COLUMN_NAME7", columnName7);
      query = query.replace("COLUMN_NAME8", columnName8);
      preparedStatement = new LoggableStatement(connection, query);
      preparedStatement.setString(1, value1);
      preparedStatement.setString(2, value2);
      preparedStatement.setString(3, value3);
      preparedStatement.setString(4, value4);
      preparedStatement.setString(5, value5);
      preparedStatement.setString(6, value6);
      preparedStatement.setString(7, value7);
      preparedStatement.setString(8, value8);
      resultSet = preparedStatement.executeQuery();
      resultSet.next();
      if (resultSet.getInt(1) > 0) {
        result = true;
      } else {
        result = false;
      }
    }
    catch (Exception exception)
    {
      throwDAOException(exception);
    }
    finally
    {
      closeSqlRefferance(resultSet, preparedStatement, connection);
    }
    logger.info("Exiting Method");
    return result;
  }
}