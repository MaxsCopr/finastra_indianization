package in.co.chargeSchedule.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
 
public class DBConnectionUtility
{
  public static Boolean isJNDIConn = Boolean.valueOf(true);
  private static Logger logger = Logger.getLogger(DBConnectionUtility.class.getName());
  public DBConnectionUtility()
  {
    logger.info("DBConnectionUtility started!");
  }
  public static Connection getConnection()
    throws SQLException
  {
    Connection connection = null;
    if (isJNDIConn.booleanValue()) {
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
        e.printStackTrace();
      }
    } else {
      try
      {
        Properties prop = ProbUtil.getPropertiesValue();
        String driver = prop.getProperty("DriverClass");
        String url = prop.getProperty("Url");
        String userName = prop.getProperty("UserName");
        String password = prop.getProperty("Password");

 
        Class.forName(driver);
        connection = DriverManager.getConnection(url, userName, password);
      }
      catch (Exception e)
      {
        logger.info("Error is " + e.getMessage());
      }
    }
    return connection;
  }
  public static Connection getWiseConnection()
    throws SQLException
  {
    Connection connection = null;
    if (isJNDIConn.booleanValue()) {
      try
      {
        Properties param = new Properties();
        param.put("java.naming.factory.initial", 
          "com.ibm.websphere.naming.WsnInitialContextFactory");
        Context initialContext = new InitialContext(param);
        DataSource dataSource = (DataSource)initialContext.lookup("jdbc/global");
        connection = dataSource.getConnection();
      }
      catch (NamingException e)
      {
        e.printStackTrace();
      }
    } else {
      try
      {
        Properties prop = ProbUtil.getPropertiesValue();
        String driver = prop.getProperty("DriverClass");
        String url = prop.getProperty("Url");
        String userName = prop.getProperty("UserName");
        String password = prop.getProperty("Password");
        Class.forName(driver);
        connection = DriverManager.getConnection(url, userName, password);
      }
      catch (Exception e)
      {
        logger.info("Exception-- getWiseConnection--Connection-----------------" + e);
        e.getMessage();
      }
    }
    return connection;
  }
  public static Connection getThemeBridgeConnection()
    throws SQLException
  {
    Connection connection = null;
    if (isJNDIConn.booleanValue()) {
      try
      {
        Properties param = new Properties();
        param.put("java.naming.factory.initial", "com.ibm.websphere.naming.WsnInitialContextFactory");
        Context initialContext = new InitialContext(param);
        DataSource dataSource = (DataSource)initialContext.lookup("jdbc/wiseconnect");
        connection = dataSource.getConnection();
      }
      catch (NamingException e)
      {
        e.printStackTrace();
      }
    } else {
      try
      {
        Properties prop = ProbUtil.getPropertiesValue();
        String driver = prop.getProperty("DriverClass");
        String url = prop.getProperty("Url");
        String userName = prop.getProperty("UserName");
        String password = prop.getProperty("Password");
        Class.forName(driver);
        connection = DriverManager.getConnection(url, userName, password);
      }
      catch (Exception e)
      {
        logger.info("Exception-- getWiseConnection--Connection-----------------" + e);
        e.getMessage();
      }
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
}