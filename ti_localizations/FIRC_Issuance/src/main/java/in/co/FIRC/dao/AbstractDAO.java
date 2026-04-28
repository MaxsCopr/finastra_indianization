package in.co.FIRC.dao;

import in.co.FIRC.dao.exception.DAOException;
import in.co.FIRC.dao.utility.DBConnectionUtility;
import in.co.FIRC.utility.ActionConstants;
import in.co.FIRC.utility.LogHelper;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.sql.DataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
 
public class AbstractDAO
{
  private static Logger logger = LogManager.getLogger(AbstractDAO.class.getName());
  protected static DataSource dataSource = null;
  public boolean logDebug = false;
  protected Connection getConnection()
    throws Exception
  {
    logger.debug("Entering Method");
    Connection conn = null;
    String userName = "tizone2";
    String password = "wiseConnect123";

 
 
    String url = "jdbc:oracle:thin:@103.230.85.236:1521/XE";
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
  public void closeSqlRefferance(ResultSet result, PreparedStatement preparedStatement, Connection connection)
  {
    closeResultSet(result);
    closePreparedStatement(preparedStatement);
    closeConnection(connection);
  }
  public boolean isDataAvailable(String columnName, String tableName, String value)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    boolean result = false;
    String query = null;
    try
    {
      connection = DBConnectionUtility.getConnection();
      query = "SELECT COUNT(1) FROM  TABLE_NAME  WHERE COLUMN_NAME1=?";
      query = query.replace(ActionConstants.TABLE_NAME, tableName);
      query = query.replace(ActionConstants.COLUMN_NAME1, columnName);
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
  public boolean isDataAvailable(String tableName, String columnName1, String columnName2, String value1, String value2)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    boolean result = false;
    try
    {
      connection = DBConnectionUtility.getConnection();
      String query = "SELECT COUNT(1) FROM  TABLE_NAME  WHERE COLUMN_NAME1=? AND COLUMN_NAME2=?";
      query = query.replace(ActionConstants.TABLE_NAME, tableName);
      query = query.replace(ActionConstants.COLUMN_NAME1, columnName1);
      query = query.replace(ActionConstants.COLUMN_NAME2, columnName2);
      preparedStatement = connection.prepareStatement(query);
      preparedStatement.setString(1, value1);
      preparedStatement.setString(2, value2);
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
