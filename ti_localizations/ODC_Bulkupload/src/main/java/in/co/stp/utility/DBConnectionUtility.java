package in.co.stp.utility;

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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
 
public class DBConnectionUtility
{
  static Logger logger = LogManager.getLogger(DBConnectionUtility.class);
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
        logger.info("enter");
        Properties param = new Properties();
        param.put("java.naming.factory.initial", "com.ibm.websphere.naming.WsnInitialContextFactory");
        Context initialContext = new InitialContext(param);
        DataSource dataSource = (DataSource)initialContext.lookup("jdbc/zone");
        connection = dataSource.getConnection();
      }
      catch (NamingException e)
      {
        logger.info("JNDI Nammig Exception------------->" + e.getMessage());
        e.printStackTrace();
      }
      catch (Exception e)
      {
        logger.info("JNDI Exception------------------>" + e.getMessage());
      }
    } else {
      try
      {
        logger.info("DB CONNECTION---------------ODC JDBC ELSE PART");
        String driver = "oracle.jdbc.driver.OracleDriver";

 
 
 
        String url = "jdbc:oracle:thin:@10.128.230.200:1529/FTRADE";

 
 
 
        String userName = "UBZONE";
        String password = "Cisco123";

 
 
 
 
        Class.forName(driver);
        connection = DriverManager.getConnection(url, userName, password);
      }
      catch (Exception e)
      {
        logger.info("ODC JDBC ELSE PART" + e.getMessage());
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
  public static void surrenderStatement(Statement stmt, ResultSet res)
  {
    try
    {
      if (stmt != null) {
        stmt.close();
      }
      if (res != null) {
        res.close();
      }
    }
    catch (SQLException e)
    {
      logger.info("Connection Failed! Check output console");
      e.printStackTrace();
    }
  }
}
