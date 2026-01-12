package com.bs.ett.email.firc;

import java.sql.Connection;
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
 
public class DatabaseUtility
{
  private static Logger logger = LogManager.getLogger(DatabaseUtility.class);
  public static void main(String[] args)
  {
    logger.info(getWiseConnection());
    logger.info(getTizoneConnection());
  }
  public static Connection getWiseConnection()
  {
    Connection connection = null;
    try
    {
      Properties param = new Properties();
      param.put("java.naming.factory.initial", "com.ibm.websphere.naming.WsnInitialContextFactory");
      Context initialContext = new InitialContext(param);
      DataSource dataSource = (DataSource)initialContext.lookup("jdbc/wiseconnect");
      connection = dataSource.getConnection();
      if (connection == null) {
        logger.error("WiseConnect connection failed!");
      }
    }
    catch (NamingException e)
    {
      logger.error("WiseConnect NamingException!" + e.getMessage(), e);
      e.printStackTrace();
    }
    catch (SQLException e)
    {
      logger.error("WiseConnect SQLException!" + e.getMessage(), e);
      e.printStackTrace();
    }
    return connection;
  }
  public static Connection getTizoneConnection()
  {
    Connection connection = null;
    try
    {
      Properties param = new Properties();
      param.put("java.naming.factory.initial", "com.ibm.websphere.naming.WsnInitialContextFactory");
      Context initialContext = new InitialContext(param);
      DataSource dataSource = (DataSource)initialContext.lookup("jdbc/zone");
      connection = dataSource.getConnection();
      if (connection == null) {
        logger.error("TINotifiation zone connection failed!");
      }
    }
    catch (NamingException e)
    {
      logger.error("TINotifiation zone NamingException!" + e.getMessage());
      e.printStackTrace();
    }
    catch (SQLException e)
    {
      logger.error("TINotifiation zone SQLException!" + e.getMessage());
      e.printStackTrace();
    }
    return connection;
  }
  public static void surrenderConnection(Connection conn, Statement stmt, ResultSet res)
  {
    try
    {
      if (isValidObject(res)) {
        res.close();
      }
    }
    catch (SQLException e)
    {
      logger.error("Close Resultset Failed!" + e.getMessage());
      e.printStackTrace();
    }
    try
    {
      if (isValidObject(stmt)) {
        stmt.close();
      }
    }
    catch (SQLException e)
    {
      logger.error("Close Statement Failed!" + e.getMessage());
      e.printStackTrace();
    }
    try
    {
      if (isValidObject(conn)) {
        conn.close();
      }
    }
    catch (SQLException e)
    {
      logger.error("Close Connection Failed!" + e.getMessage());
      e.printStackTrace();
    }
  }
  public static boolean isValidObject(Object object)
  {
    if (object == null) {
      return false;
    }
    return true;
  }
}