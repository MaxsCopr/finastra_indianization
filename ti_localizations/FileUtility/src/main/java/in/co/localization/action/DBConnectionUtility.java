package in.co.localization.action;
 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
        param.put("java.naming.factory.initial", 
          "com.ibm.websphere.naming.WsnInitialContextFactory");
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
        String driver = "oracle.jdbc.driver.OracleDriver";
        String url = "jdbc:oracle:thin:@172.28.194.26:1529:FTRADE";
        String userName = "UBZONE";
        String password = "Ubzo#2024";
        Class.forName(driver);
        connection = DriverManager.getConnection(url, userName, password);
      }
      catch (Exception e)
      {
        logger.info("Exception----Connection-----------------" + e);
        e.getMessage();
      }
    }
    return connection;
  }
  public static Connection getGlobalWiseConnection()
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
        String driver = "oracle.jdbc.driver.OracleDriver";
        String url = "jdbc:oracle:thin:@10.128.230.200:1529/FTRADE";
        String userName = "UBZONE";
        String password = "Cisco123";
        Class.forName(driver);
        connection = DriverManager.getConnection(url, userName, password);
      }
      catch (Exception e)
      {
        logger.info("Exception-- getGlobalWiseConnection--Connection-----------------" + e);
        e.getMessage();
      }
    }
    return connection;
  }
  public static Connection getGlobalConnection()
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
        String driver = "oracle.jdbc.driver.OracleDriver";

 
 
        String url = "jdbc:oracle:thin:@10.128.230.200:1529/FTRADE";
        String userName = "TIGLOBAL";
        String password = "Cisco123";

 
        Class.forName(driver);
        connection = DriverManager.getConnection(url, userName, password);
      }
      catch (Exception e)
      {
        logger.info("Exception-- getGlobalConnection--Connection-----------------" + e);
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
  public static void surrenderConnection(Connection aConnection, Statement aStatement, ResultSet aResultset)
  {
    try
    {
      if (ValidationsUtil.isValidObject(aResultset)) {
        aResultset.close();
      }
    }
    catch (SQLException e)
    {
      logger.info("Close Resultset Failed!" + e.getMessage());
    }
    try
    {
      if (ValidationsUtil.isValidObject(aStatement)) {
        aStatement.close();
      }
    }
    catch (SQLException e)
    {
      logger.info("Close Statement Failed!" + e.getMessage());
    }
    try
    {
      if (ValidationsUtil.isValidObject(aConnection)) {
        aConnection.close();
      }
    }
    catch (SQLException e)
    {
      logger.info("Close Connection Failed!" + e.getMessage());
    }
  }
  public static Connection getWiseConnection()
  {
    Connection connection = null;
    if (isJNDIConn.booleanValue())
    {
      logger.info("Wiseconnect JNDI Connection Encountered");
      try
      {
        Properties param = new Properties();
        param.put("java.naming.factory.initial", "com.ibm.websphere.naming.WsnInitialContextFactory");
        Context initialContext = new InitialContext(param);
        DataSource dataSource = (DataSource)initialContext.lookup("jdbc/wiseconnect");
        connection = dataSource.getConnection();
        if (connection != null) {
          break label245;
        }
        logger.info("Wiseconnect JDBC connection failed! ");
      }
      catch (NamingException e)
      {
        logger.info("Wiseconnect JNDI NamingException! " + e.getMessage());
        e.printStackTrace();
      }
      catch (SQLException e)
      {
        logger.info("Wiseconnect JNDI SQLException! " + e.getMessage());
        e.printStackTrace();
      }
    }
    else
    {
      logger.info("Wiseconnect JDBC Connection Encountered");
      try
      {
        String driver = "oracle.jdbc.driver.OracleDriver";
        String url = "jdbc:oracle:thin:@10.10.20.137:1530:orcl";
        String userName = "WISECONNECT";
        String password = "Wiseconnect_123";

 
 
 
 
        Class.forName(driver);
        connection = DriverManager.getConnection(url, userName, password);
      }
      catch (SQLException e)
      {
        logger.info("Wiseconnect JDBC SQLException! " + e.getMessage());
        e.printStackTrace();
      }
      catch (Exception e)
      {
        logger.info("Wiseconnect JDBC Exception! " + e.getMessage());
        e.printStackTrace();
      }
    }
    label245:
    logger.info("Wiseconnect JNDI Connection returns! " + connection);
    return connection;
  }
  public static void surrenderPrepdConnection(Connection aConnection, PreparedStatement aPreparedStatement, ResultSet aResultset)
  {
    try
    {
      if (ValidationsUtil.isValidObject(aResultset)) {
        aResultset.close();
      }
    }
    catch (SQLException e)
    {
      logger.info("Close Resultset Failed!" + e.getMessage());
    }
    try
    {
      if (ValidationsUtil.isValidObject(aPreparedStatement)) {
        aPreparedStatement.close();
      }
    }
    catch (SQLException e)
    {
      logger.info("Close PreparedStatement Failed!" + e.getMessage());
    }
    try
    {
      if (ValidationsUtil.isValidObject(aConnection)) {
        aConnection.close();
      }
    }
    catch (SQLException e)
    {
      logger.info("Close Connection Failed!" + e.getMessage());
    }
  }
}