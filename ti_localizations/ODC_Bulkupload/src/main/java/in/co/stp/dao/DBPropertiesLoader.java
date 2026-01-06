package in.co.stp.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
 
public class DBPropertiesLoader
{
  public static String DRIVER_NAME = "";
  public static String JDBC_URL = "";
  public static String DB_USER = "";
  public static String DB_PASS = "";
  public static String DATA_LOAD = "";
  public static String TI_EJB_URL = "";
  public static String EJBCLIENT_URL = "iiop://localhost:2809";
  public static String GUARANTEE_DB_TABLE = "";
  public static String INWCOL_DB_TABLE = "";
  public static String IMPLCCLMS_DB_TABLE = "";
  public static String GUARCLMS_DB_TABLE = "";
  public static String OUTWCOL_DB_TABLE = "";
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
  public static String INVDISCOUNT_DB_TABLE = "";
  public static String BUYERFIN_DB_TABLE = "";
  public static String FINSTANDALN_DB_TABLE = "";
  public static String EXPGTY_DB_TABLE = "";
  public static String ISBLC_DB_TABLE = "";
  public static String TFESBNEW_DB_TABLE = "";
  public static String TFCPHCRT_DB_TABLE = "";
  public static String SHIPGNT_DB_TABLE = "";
  public static String NEWCLEANPAYMENT_DB_TABLE = "";
  public static String DISPUR_DB_TABLE = "";
  public static String FINNEGOTN_DB_TABLE = "";
  public static String AUTHRRMB_DB_TABLE = "";
  public static String RMBCLAIM_DB_TABLE = "";
  public static String BULKUPLOAD_TABLE = "";
  public static String MIGRATIONUSER = "";
  public static String MIGRATIONTEAM = "";
  public static String FILELOCATION = "";
  public static boolean behalf_branch_lookup = false;
  public static boolean product_type_lookup = false;
  public static boolean source_system_lookup = false;
  public static boolean Api_stub = false;
  public static boolean customer_type_lookup = false;
  public static boolean ReconciledAmount_lookup = true;
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
      FILELOCATION = prop.getProperty("FileLocation");

 
      DATA_LOAD = prop.getProperty("DATA_LOAD");
      TI_EJB_URL = prop.getProperty("TI_EJB_URL");
      EJBCLIENT_URL = prop.getProperty("EjbClientUrl");
      GUARANTEE_DB_TABLE = prop.getProperty("Guarantee_DB_Table");
      ILC_DB_TABLE = prop.getProperty("Ilc_DB_Table");
      ELC_DB_TABLE = prop.getProperty("Elc_DB_Table");
      INWCOL_DB_TABLE = prop.getProperty("InwCol_DB_Table");
      OUTWCOL_DB_TABLE = prop.getProperty("OutwCol_DB_Table");
      BULKUPLOAD_TABLE = prop.getProperty("BULKUPLOAD_TABLE");
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
      INV_DB_TABLE = prop.getProperty("Inv_DB_Table");
      INVDISCOUNT_DB_TABLE = prop.getProperty("InvDiscount_DB_Table");
      BUYERFIN_DB_TABLE = prop.getProperty("BuyerFin_DB_Table");
      FINSTANDALN_DB_TABLE = prop.getProperty("FinStandAln_DB_Table");
      EXPGTY_DB_TABLE = prop.getProperty("ExpGty_DB_Table");
      ISBLC_DB_TABLE = prop.getProperty("Isblc_DB_Table");
      TFESBNEW_DB_TABLE = prop.getProperty("TFESBNEW_DB_TABLE");
      TFCPHCRT_DB_TABLE = prop.getProperty("TFCPHCRT_DB_TABLE");
      SHIPGNT_DB_TABLE = prop.getProperty("SHIPGNT_DB_TABLE");
      NEWCLEANPAYMENT_DB_TABLE = prop.getProperty("NewCleanPayment_DB_Table");
      DISPUR_DB_TABLE = prop.getProperty("DISPUR_DB_TABLE");
      FINNEGOTN_DB_TABLE = prop.getProperty("FINNEGOTN_DB_TABLE");
      AUTHRRMB_DB_TABLE = prop.getProperty("AUTHRRMB_DB_TABLE");
      RMBCLAIM_DB_TABLE = prop.getProperty("RMBCLAIM_DB_TABLE");

 
      MIGRATIONUSER = prop.getProperty("MigrationUser");
      MIGRATIONTEAM = prop.getProperty("DefaultTeam");
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
