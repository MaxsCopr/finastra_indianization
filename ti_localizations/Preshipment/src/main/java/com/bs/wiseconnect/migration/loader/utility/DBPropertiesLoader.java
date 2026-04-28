package com.bs.wiseconnect.migration.loader.utility;
 
import java.io.FileNotFoundException;

import java.io.IOException;

import java.util.Properties;

import java.util.logging.Level;

import java.util.logging.Logger;
 
public class DBPropertiesLoader

{

  public static String BUYERFIN_DB_TABLE_VIEW = "";

  public static String BUYERFIN_DB_TABLE = "";

  public static String ATTDOC_DB_TABLE = "";

  public static String DRIVER_NAME = "";

  public static String JDBC_URL = "";

  public static String DB_USER = "";

  public static String DB_PASS = "";

  public static String DATA_LOAD = "";

  public static String TI_EJB_URL = "";

  public static String EJBCLIENT_URL = "";

  public static String GUARANTEE_DB_TABLE = "";

  public static String INWCOL_DB_TABLE = "";

  public static String OUTWCOL_DB_TABLE = "";

  public static String IMPLCCLMS_DB_TABLE = "";

  public static String GUARCLMS_DB_TABLE = "";

  public static String ELCDOCPRN_DB_TABLE = "";

  public static String ILC_DB_TABLE = "";

  public static String ELC_DB_TABLE = "";

  public static String TB_DRIVER_NAME = "";

  public static String TB_JDBC_URL = "";

  public static String TB_DB_USER = "";

  public static String TB_DB_PASS = "";

  public static String TIPLUS_DB_USER = "";

  public static String TIPLUS_DB_PASSWRD = "";

  public static String TIPLUS_DB_URL = "";

  public static String TIPLUS_DB_DRIVER = "";

  public static String TOKEN_FILE_LOCATION = "";

  public static String INV_DB_TABLE = "";

  public static void initialize(String propertyFile)

  {

    try

    {

      Properties prop = new Properties();

      prop.load(DBPropertiesLoader.class.getClassLoader().getResourceAsStream(propertyFile));

      DRIVER_NAME = prop.getProperty("DriverName");

      JDBC_URL = prop.getProperty("url");

      DB_USER = prop.getProperty("Username");

      DB_PASS = prop.getProperty("Password");

 
      DATA_LOAD = prop.getProperty("DATA_LOAD");

      TI_EJB_URL = prop.getProperty("TI_EJB_URL");

      EJBCLIENT_URL = prop.getProperty("EjbClientUrl");

      GUARANTEE_DB_TABLE = prop.getProperty("Gurantee_DB_Table");

      ILC_DB_TABLE = prop.getProperty("Ilc_DB_Table");

      INV_DB_TABLE = prop.getProperty("INV_DB_TABLE");

      ATTDOC_DB_TABLE = prop.getProperty("ATTDOC_DB_TABLE");

      ELC_DB_TABLE = prop.getProperty("Elc_DB_Table");

      INWCOL_DB_TABLE = prop.getProperty("InwCol_DB_Table");

      OUTWCOL_DB_TABLE = prop.getProperty("OutwCol_DB_Table");

      TB_DRIVER_NAME = prop.getProperty("TB_DriverName");

      TB_JDBC_URL = prop.getProperty("TB_Resource");

      TB_DB_USER = prop.getProperty("TB_Username");

      TB_DB_PASS = prop.getProperty("TB_Password");

      TIPLUS_DB_USER = prop.getProperty("TIPLUS_Db_User");

      TIPLUS_DB_PASSWRD = prop.getProperty("TIPLUS_Db_Password");

      TIPLUS_DB_URL = prop.getProperty("TIPLUS_Db_Url");

      TIPLUS_DB_DRIVER = prop.getProperty("TIPLUS_Db_DriverName");

      TOKEN_FILE_LOCATION = prop.getProperty("tkn_file_loc");

      IMPLCCLMS_DB_TABLE = prop.getProperty("IlcClaims_DB_Table");

      GUARCLMS_DB_TABLE = prop.getProperty("GuarClaims_DB_Table");

      ELCDOCPRN_DB_TABLE = prop.getProperty("ElcDocPrn_DB_Table");

      BUYERFIN_DB_TABLE = prop.getProperty("BuyerFin_DB_Table");

    }

    catch (FileNotFoundException ex)

    {

      Logger.getLogger(DBPropertiesLoader.class.getName()).log(Level.SEVERE, null, ex);

    }

    catch (IOException ex)

    {

      Logger.getLogger(DBPropertiesLoader.class.getName()).log(Level.SEVERE, null, ex);

    }

  }

}