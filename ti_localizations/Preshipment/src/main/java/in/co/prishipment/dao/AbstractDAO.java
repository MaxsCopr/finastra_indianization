package in.co.prishipment.dao;
 
import in.co.prishipment.dao.exception.DAOException;

import in.co.prishipment.utility.LogHelper;

import java.sql.Connection;

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

  public static void closePreparedStatement(PreparedStatement preparedStatement)

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

  public static void closeResultSet(ResultSet resultSet)

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

}