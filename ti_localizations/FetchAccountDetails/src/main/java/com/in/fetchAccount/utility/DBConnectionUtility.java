package com.in.fetchAccount.utility;
 
import java.sql.Connection;

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

  private static Logger logger = Logger.getLogger(DBConnectionUtility.class.getName());

  public DBConnectionUtility()

  {

    logger.info("DBConnectionUtility started!");

  }

  public static Connection getZoneConnection()

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

      logger.info("DBConnectionUtility : TIZONE JNDI NamingException! " + e.getMessage());

      logger.error(e);

    }

    catch (SQLException e)

    {

      logger.info("DBConnectionUtility : TIZONE JNDI SQLException! " + e.getMessage());

      logger.error(e);

    }

    catch (Exception e)

    {

      logger.info("DBConnectionUtility : TIZONE JNDI Exception! " + e.getMessage());

      logger.error(e);

    }

    return connection;

  }

  public static Connection getDBLinkConnection()

  {

    Connection connection = null;

    try

    {

      Properties param = new Properties();

      param.put("java.naming.factory.initial", "com.ibm.websphere.naming.WsnInitialContextFactory");

      Context initialContext = new InitialContext(param);

      DataSource dataSource = (DataSource)initialContext.lookup("jdbc/dblink");

      connection = dataSource.getConnection();

    }

    catch (NamingException e)

    {

      logger.info("DBConnectionUtility : DBLink Treasury JNDI NamingException! " + e.getMessage());

      logger.error(e);

    }

    catch (SQLException e)

    {

      logger.info("DBConnectionUtility : DBLink Treasury JNDI SQLException! " + e.getMessage());

      logger.error(e);

    }

    catch (Exception e)

    {

      logger.info("DBConnectionUtility : DBLink TIZONE JNDI Exception! " + e.getMessage());

      logger.error(e);

    }

    return connection;

  }

  public static Connection getGlobalConnection()

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

      logger.info("DBConnectionUtility : Global JNDI NamingException! " + e.getMessage());

      logger.error(e);

    }

    catch (SQLException e)

    {

      logger.info("DBConnectionUtility : Global JNDI SQLException! " + e.getMessage());

      logger.error(e);

    }

    catch (Exception e)

    {

      logger.info("DBConnectionUtility : Global JNDI Exception! " + e.getMessage());

      logger.error(e);

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

      DataSource dataSource = (DataSource)initialContext.lookup("jdbc/wiseconnect");

      connection = dataSource.getConnection();

    }

    catch (NamingException e)

    {

      logger.info("DBConnectionUtility : Wiseconnect JNDI NamingException! " + e.getMessage());

      logger.error(e);

    }

    catch (SQLException e)

    {

      logger.info("DBConnectionUtility : Wiseconnect JNDI SQLException! " + e.getMessage());

      logger.error(e);

    }

    catch (Exception e)

    {

      logger.info("DBConnectionUtility : Wiseconnect JNDI Exception! " + e.getMessage());

      logger.error(e);

    }

    return connection;

  }

  public static void surrenderDB(Connection con, Statement stmt, ResultSet res)

  {

    try

    {

      if (con != null) {

        con.close();

      }

      if (res != null) {

        res.close();

      }

      if (stmt != null) {

        stmt.close();

      }

    }

    catch (SQLException e)

    {

      logger.info("DBConnectionUtility : surrenderDB : Exception! " + e.getMessage());

      logger.error(e);

    }

  }

}