package com.bs.wiseconnect.migration.loader.utility;
 
import java.beans.PropertyVetoException;

import java.io.IOException;

import java.sql.Connection;

import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;
 
public class DataSource

{

  private static DataSource datasource;

  private BasicDataSource ds;

  private DataSource()

    throws IOException, SQLException, PropertyVetoException

  {

    this.ds = new BasicDataSource();

    this.ds.setDriverClassName(DBPropertiesLoader.TB_DRIVER_NAME);

    this.ds.setUsername(DBPropertiesLoader.TB_DB_USER);

    this.ds.setPassword(DBPropertiesLoader.TB_DB_PASS);

    this.ds.setUrl(DBPropertiesLoader.TB_JDBC_URL);

  }

  public static DataSource getInstance()

    throws IOException, SQLException, PropertyVetoException

  {

    if (datasource == null)

    {

      datasource = new DataSource();

      return datasource;

    }

    return datasource;

  }

  public Connection getConnection()

    throws SQLException

  {

    return this.ds.getConnection();

  }

}