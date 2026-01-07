package in.co.prishipment.utility;
 
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
 
public class DBConnectionUtility

{

  public static Boolean isJNDIConn = Boolean.valueOf(true);

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

        String userName = prop.getProperty("UserName");

        String password = prop.getProperty("Password");

        String url = prop.getProperty("Url");

        Class.forName(driver);

        connection = DriverManager.getConnection(url, userName, password);

      }

      catch (Exception e)

      {

        e.printStackTrace();

      }

    }

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