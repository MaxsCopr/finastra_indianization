package in.co.localization.utility;
 
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

  implements ActionConstants

{

  private static Logger logger = Logger.getLogger(DBConnectionUtility.class.getName());

  public static Boolean isJNDIConn = Boolean.valueOf(true);

  public static Connection getConnection()

    throws SQLException

  {

    logger.info("Entering Method");

    Connection connection = null;

    if (isJNDIConn.booleanValue())

    {

      logger.info("EDMPS JNDI Connection Entered");

      try

      {

        Properties param = new Properties();

        param.put("java.naming.factory.initial", "com.ibm.websphere.naming.WsnInitialContextFactory");

        Context initialContext = new InitialContext(param);

        logger.info("Setting Values-----------jdbc/zone---------");

        DataSource dataSource = (DataSource)initialContext.lookup("jdbc/zone");

        logger.info("dataSource-----------------Name--------------" + dataSource);

        connection = dataSource.getConnection();

      }

      catch (NamingException e)

      {

        e.printStackTrace();

      }

    }

    else

    {

      try

      {

        logger.info("DBConnectionUtility ------------false----------zone----------- state started!");

        String driver = "oracle.jdbc.driver.OracleDriver";

 
 
 
 
 
 
 
        String url = "jdbc:oracle:thin:@10.10.20.137:1530/orcl";

        String userName = "TIZONE";

        String password = "tizone_1234";

 
 
 
 
 
 
 
 
 
 
 
 
 
 
        logger.info("This is Inside of DB Connection");

        Class.forName(driver);

        connection = DriverManager.getConnection(url, userName, password);

      }

      catch (Exception e)

      {

        logger.info("Error is " + e.getMessage());

        logger.info("Error in DB Connection " + e.getMessage());

      }

    }

    logger.info("Exiting Method");

    return connection;

  }

  public static Connection getWiseConnection()

    throws SQLException

  {

    Connection connection = null;

    if (isJNDIConn.booleanValue()) {

      try

      {

        logger.info("DBConnectionUtility ------------true----------global---------- state started!");

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

        logger.info("DBConnectionUtility ------------false----------global---------- state started!");

        String driver = "oracle.jdbc.driver.OracleDriver";

 
 
 
 
 
 
 
 
 
 
 
 
 
 
        String url = "jdbc:oracle:thin:@10.10.20.137:1528/orcl1";

        String userName = "TIGLOBAL";

        String password = "tiglobal_1234";

 
 
 
 
 
 
 
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

      e.printStackTrace();

    }

  }

}