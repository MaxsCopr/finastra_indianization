package in.co.boe.utility;

import java.sql.Connection;
import java.sql.ResultSet;
import org.apache.log4j.Logger;
 
public class GetOptPath
{
  public String fileLoc(String string)
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
      logger.info("inside  file location function");
      con = DBConnectionUtility.getConnection();
      String query = "SELECT VALUE1 FROM ETT_PARAMETER_TBL WHERE PARAMETER_ID ='BOEOPTPATH'";
      log = new LoggableStatement(con, query);
      rs = log.executeQuery();
      if (rs.next()) {
        fileloc = rs.getString("VALUE1");
      }
      logger.info("file path---------" + fileloc);
    }
    catch (Exception localException) {}finally
    {
      DBConnectionUtility.surrenderDB(con, log, rs);
    }
    logger.info("Exiting Method");
    return fileloc;
  }
  public String obbcsvpath(String string)
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
      logger.info("inside  file location function");
      con = DBConnectionUtility.getConnection();
      String query = "SELECT VALUE1 FROM ETT_PARAMETER_TBL WHERE PARAMETER_ID ='OBBCSVPATH'";
      log = new LoggableStatement(con, query);
      rs = log.executeQuery();
      if (rs.next()) {
        fileloc = rs.getString("VALUE1");
      }
      logger.info("file path---------" + fileloc);
    }
    catch (Exception localException) {}finally
    {
      DBConnectionUtility.surrenderDB(con, log, rs);
    }
    logger.info("Exiting Method");
    return fileloc;
  }
  public String Imagefile(String string)
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
      logger.info("inside  file location function");
      con = DBConnectionUtility.getConnection();
      String query = "SELECT VALUE1 FROM ETT_PARAMETER_TBL WHERE PARAMETER_ID ='boeadviseimg'";
      log = new LoggableStatement(con, query);
      rs = log.executeQuery();
      if (rs.next()) {
        fileloc = rs.getString("VALUE1");
      }
      logger.info("file path---------" + fileloc);
    }
    catch (Exception localException) {}finally
    {
      DBConnectionUtility.surrenderDB(con, log, rs);
    }
    logger.info("Exiting Method");
    return fileloc;
  }
}