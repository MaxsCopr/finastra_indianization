package in.co.ebrc.utility;

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
  public DBConnectionUtility()
  {
    logger.info("DBConnectionUtility started!");
  }
  public static Connection getConnection()
    throws SQLException
  {
    logger.info("Entering Method");
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

 
 
 
 
 
 
 
 
 
 
 
 
 
        String url = "jdbc:oracle:thin:@10.128.230.200:1529/FTRADE";
        String userName = "UBZONE";
        String password = "Cisco123";

 
        Class.forName(driver);
        connection = DriverManager.getConnection(url, userName, password);
      }
      catch (Exception e)
      {
        logger.info("Error is " + e.getMessage());
      }
    }
    logger.info("Exiting Method");
    return connection;
  }
  public static void surrenderDB(Connection con, Statement stmt, ResultSet res)
  {
    try
    {
      if (con != null)
      {
        con.close();
        if (stmt != null) {
          stmt.close();
        }
        if (res != null) {
          res.close();
        }
      }
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
  }
}