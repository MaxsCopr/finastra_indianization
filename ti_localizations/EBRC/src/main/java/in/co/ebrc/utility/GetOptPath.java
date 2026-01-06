package in.co.ebrc.utility;

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
      String query = "SELECT VALUE1 FROM ETT_PARAMETER_TBL WHERE PARAMETER_ID ='EBRCIFSC' AND ACTIVE='Y'";
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
  public String Ebrccancel(String string)
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
      String query = "SELECT VALUE1 FROM ETT_PARAMETER_TBL WHERE PARAMETER_ID ='EBRCCANCEL' AND ACTIVE='Y'";
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
  public String Ebrc(String string)
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
      String query = "SELECT VALUE1 FROM ETT_PARAMETER_TBL WHERE PARAMETER_ID ='EBRC' AND ACTIVE='Y'";
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
