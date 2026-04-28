package in.co.localization.utility;
 
import java.sql.Connection;
import java.sql.ResultSet;
import org.apache.log4j.Logger;
 
public class GetOptPath
{
  public String ORMpath(String string)
  {
    Logger logger = Logger.getLogger(GetOptPath.class
      .getName());
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement log = null;
    ResultSet rs = null;
    String fileloc = "";
    try
    {
      con = DBConnectionUtility.getConnection();
      String query = "SELECT VALUE1 FROM ETT_PARAMETER_TBL WHERE PARAMETER_ID ='ROD_ONLINE'";
      log = new LoggableStatement(con, query);
      rs = log.executeQuery();
      if (rs.next()) {
        fileloc = rs.getString("VALUE1");
      }
    }
    catch (Exception localException) {}finally
    {
      DBConnectionUtility.surrenderDB(con, log, rs);
    }
    logger.info("Exiting Method");
    return fileloc;
  }
  public String BESpath(String string)
  {
    Logger logger = Logger.getLogger(GetOptPath.class
      .getName());
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement log = null;
    ResultSet rs = null;
    String fileloc = "";
    try
    {
      con = DBConnectionUtility.getConnection();
      String query = "SELECT VALUE1 FROM ETT_PARAMETER_TBL WHERE PARAMETER_ID ='TRR_ONLINE'";
      log = new LoggableStatement(con, query);
      rs = log.executeQuery();
      if (rs.next()) {
        fileloc = rs.getString("VALUE1");
      }
    }
    catch (Exception localException) {}finally
    {
      DBConnectionUtility.surrenderDB(con, log, rs);
    }
    logger.info("Exiting Method");
    return fileloc;
  }
  public String BEEpath(String string)
  {
    Logger logger = Logger.getLogger(GetOptPath.class
      .getName());
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement log = null;
    ResultSet rs = null;
    String fileloc = "";
    try
    {
      con = DBConnectionUtility.getConnection();
      String query = "SELECT VALUE1 FROM ETT_PARAMETER_TBL WHERE PARAMETER_ID ='PRN_ONLINE'";
      log = new LoggableStatement(con, query);
      rs = log.executeQuery();
      if (rs.next()) {
        fileloc = rs.getString("VALUE1");
      }
    }
    catch (Exception localException) {}finally
    {
      DBConnectionUtility.surrenderDB(con, log, rs);
    }
    logger.info("Exiting Method");
    return fileloc;
  }
  public String BECpath(String string)
  {
    Logger logger = Logger.getLogger(GetOptPath.class
      .getName());
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement log = null;
    ResultSet rs = null;
    String fileloc = "";
    try
    {
      con = DBConnectionUtility.getConnection();
      String query = "SELECT VALUE1 FROM ETT_PARAMETER_TBL WHERE PARAMETER_ID ='IRM_ONLINE'";
      log = new LoggableStatement(con, query);
      rs = log.executeQuery();
      if (rs.next()) {
        fileloc = rs.getString("VALUE1");
      }
    }
    catch (Exception localException) {}finally
    {
      DBConnectionUtility.surrenderDB(con, log, rs);
    }
    logger.info("Exiting Method");
    return fileloc;
  }
  public String ORCpath(String string)
  {
    Logger logger = Logger.getLogger(GetOptPath.class
      .getName());
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement log = null;
    ResultSet rs = null;
    String fileloc = "";
    try
    {
      con = DBConnectionUtility.getConnection();
      String query = "SELECT VALUE1 FROM ETT_PARAMETER_TBL WHERE PARAMETER_ID ='EBRC_ONLINE'";
      log = new LoggableStatement(con, query);
      rs = log.executeQuery();
      if (rs.next()) {
        fileloc = rs.getString("VALUE1");
      }
    }
    catch (Exception localException) {}finally
    {
      DBConnectionUtility.surrenderDB(con, log, rs);
    }
    logger.info("Exiting Method");
    return fileloc;
  }
  public String MBEpath(String string)
  {
    Logger logger = Logger.getLogger(GetOptPath.class
      .getName());
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement log = null;
    ResultSet rs = null;
    String fileloc = "";
    try
    {
      con = DBConnectionUtility.getConnection();
      String query = "SELECT VALUE1 FROM ETT_PARAMETER_TBL WHERE PARAMETER_ID ='IREXT'";
      log = new LoggableStatement(con, query);
      rs = log.executeQuery();
      if (rs.next()) {
        fileloc = rs.getString("VALUE1");
      }
    }
    catch (Exception localException) {}finally
    {
      DBConnectionUtility.surrenderDB(con, log, rs);
    }
    logger.info("Exiting Method");
    return fileloc;
  }
  public String OBBCpath(String string)
  {
    Logger logger = Logger.getLogger(GetOptPath.class
      .getName());
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement log = null;
    ResultSet rs = null;
    String fileloc = "";
    try
    {
      con = DBConnectionUtility.getConnection();
      String query = "SELECT VALUE1 FROM ETT_PARAMETER_TBL WHERE PARAMETER_ID ='IRCLS'";
      log = new LoggableStatement(con, query);
      rs = log.executeQuery();
      if (rs.next()) {
        fileloc = rs.getString("VALUE1");
      }
    }
    catch (Exception localException) {}finally
    {
      DBConnectionUtility.surrenderDB(con, log, rs);
    }
    logger.info("Exiting Method");
    return fileloc;
  }
  public String EODFILE(String string)
  {
    Logger logger = Logger.getLogger(GetOptPath.class
      .getName());
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement log = null;
    ResultSet rs = null;
    String fileloc = "";
    try
    {
      con = DBConnectionUtility.getConnection();
      String query = "SELECT VALUE1 FROM ETT_PARAMETER_TBL WHERE PARAMETER_ID ='EODFILE_PATH'";
      log = new LoggableStatement(con, query);
      rs = log.executeQuery();
      if (rs.next()) {
        fileloc = rs.getString("VALUE1");
      }
    }
    catch (Exception localException) {}finally
    {
      DBConnectionUtility.surrenderDB(con, log, rs);
    }
    logger.info("Exiting Method");
    return fileloc;
  }
}