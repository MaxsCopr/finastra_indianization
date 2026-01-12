package com.bs.theme.migration.loader.db.connection;
 
import com.bs.theme.migration.loader.utility.DBPropertiesLoader;

import com.bs.theme.migration.loader.utility.DataSource;

import java.beans.PropertyVetoException;

import java.io.IOException;

import java.sql.Connection;

import java.sql.DriverManager;

import java.sql.SQLException;

import java.util.logging.Level;

import java.util.logging.Logger;
 
public class ThemeDB

{

  public static Connection getDBConnection1()

    throws SQLException

  {

    Connection connection = null;

    try

    {

      Class.forName("com.mysql.jdbc.Driver");

      String url = "jdbc:mysql://localhost:3306/tf_dataload";

      connection = DriverManager.getConnection(url, "root", "");

    }

    catch (Exception e)

    {

      e.printStackTrace();

    }

    return connection;

  }

  public static Connection getTIPLUSConnection()

  {

    Connection connection = null;

    try

    {

      Class.forName(DBPropertiesLoader.TIPLUS_DB_DRIVER);

      connection = DriverManager.getConnection(DBPropertiesLoader.TIPLUS_DB_URL, DBPropertiesLoader.TIPLUS_DB_USER, DBPropertiesLoader.TIPLUS_DB_PASSWRD);

    }

    catch (ClassNotFoundException ex)

    {

      Logger.getLogger(ThemeDB.class.getName()).log(Level.SEVERE, null, ex);

    }

    catch (SQLException e)

    {

      e.printStackTrace();

    }

    return connection;

  }

  public static Connection getDBConnection2()

    throws SQLException

  {

    Connection connection = null;

    try

    {

      Class.forName(DBPropertiesLoader.TIPLUS_DB_DRIVER);

      connection = DriverManager.getConnection(DBPropertiesLoader.TIPLUS_DB_URL, DBPropertiesLoader.TIPLUS_DB_USER, DBPropertiesLoader.TIPLUS_DB_PASSWRD);

    }

    catch (Exception e)

    {

      e.printStackTrace();

    }

    return connection;

  }

  public static Connection getDBConnection()

    throws SQLException

  {

    String driver = DBPropertiesLoader.DRIVER_NAME;

    String url = DBPropertiesLoader.JDBC_URL;

    String username = DBPropertiesLoader.DB_USER;

    String password = DBPropertiesLoader.DB_PASS;

 
    Connection connection = null;

    try

    {

      Class.forName(driver);

      connection = DriverManager.getConnection(

        url, username, 

        password);

    }

    catch (SQLException e)

    {

      e.printStackTrace();

    }

    catch (Exception e)

    {

      e.printStackTrace();

    }

    return connection;

  }

  public static Connection getThemeBridgeDBConnection()

    throws SQLException

  {

    Connection connection = null;

    try

    {

      connection = DataSource.getInstance().getConnection();

    }

    catch (IOException e)

    {

      e.printStackTrace();

    }

    catch (PropertyVetoException e)

    {

      e.printStackTrace();

    }

    return connection;

  }

  public static void main(String[] args)

    throws SQLException

  {

    DBPropertiesLoader.initialize("sourcedb.properties");

  }

}