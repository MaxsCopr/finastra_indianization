package in.co.ebrc.dao;

import in.co.ebrc.dao.exception.DAOException;
import in.co.ebrc.utility.LogHelper;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
 
public class AbstractDAO
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
  public void closeSqlRefferance(ResultSet result, PreparedStatement preparedStatement)
  {
    closeResultSet(result);
    closePreparedStatement(preparedStatement);
  }
}
