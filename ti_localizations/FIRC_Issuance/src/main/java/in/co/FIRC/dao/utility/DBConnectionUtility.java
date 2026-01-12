package in.co.FIRC.dao.utility;

import in.co.FIRC.dao.exception.DAOException;
import in.co.FIRC.utility.ActionConstants;
import in.co.FIRC.utility.LogHelper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
 
public class DBConnectionUtility
{
  private static Logger logger = LogManager.getLogger(DBConnectionUtility.class.getName());
  public static Boolean isJNDIConn = Boolean.valueOf(true);
  public DBConnectionUtility()
  {
    logger.info("DBConnectionUtility started!");
  }
  public static Connection getConnection()
  {
    Connection connection = null;
    try
    {
      Properties param = new Properties();
      param.put("java.naming.factory.initial", "com.ibm.websphere.naming.WsnInitialContextFactory");
      Context initialContext = new InitialContext(param);
      DataSource dataSource = (DataSource)initialContext.lookup("jdbc/zone");
      connection = dataSource.getConnection();
    }
    catch (NamingException e)
    {
      logger.error("TIZONE JNDI NamingException! " + e.getMessage());
      e.printStackTrace();
    }
    catch (SQLException e)
    {
      logger.error("TIZONE JNDI SQLException! " + e.getMessage());
      e.printStackTrace();
    }
    return connection;
  }
  public static Connection getWiseConnection()
  {
    Connection connection = null;
    try
    {
      Properties param = new Properties();
      param.put("java.naming.factory.initial", "com.ibm.websphere.naming.WsnInitialContextFactory");
      Context initialContext = new InitialContext(param);
      DataSource dataSource = (DataSource)initialContext.lookup("jdbc/global");
      connection = dataSource.getConnection();
    }
    catch (NamingException e)
    {
      logger.error("TIZONE JNDI NamingException! " + e.getMessage());
      e.printStackTrace();
    }
    catch (SQLException e)
    {
      logger.error("TIZONE JNDI SQLException! " + e.getMessage());
      e.printStackTrace();
    }
    return connection;
  }
  public static void surrenderDB(Connection con, Statement stmt, ResultSet res)
  {
    try
    {
      if (res != null) {
        res.close();
      }
      if (stmt != null) {
        stmt.close();
      }
      if (con != null) {
        con.close();
      }
    }
    catch (SQLException e)
    {
      logger.info("Connection Failed! Check output console");
      e.printStackTrace();
    }
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
  public void closePreparedStmtResultSet(PreparedStatement preparedStatement, ResultSet resultSet)
  {
    try
    {
      if (preparedStatement != null) {
        preparedStatement.close();
      }
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
      connection = getConnection();
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
      connection = getConnection();
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
