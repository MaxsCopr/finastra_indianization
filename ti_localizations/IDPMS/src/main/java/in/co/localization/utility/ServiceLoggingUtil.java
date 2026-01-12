package in.co.localization.utility;
 
import com.misys.tiplus2.framework.async.processing.execution.step.StepContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import org.apache.log4j.Logger;
 
public class ServiceLoggingUtil
{
  private static Logger logger = Logger.getLogger(ServiceLoggingUtil.class.getName());
  public static HashMap<String, String> getBridgePropertiesMap()
  {
    Statement stmt = null;
    ResultSet res = null;
    Connection con = null;
    HashMap<String, String> map = new HashMap();
    try
    {
      con = DBConnectionUtility.getWiseConnection();
      stmt = con.createStatement();
      res = stmt.executeQuery("SELECT * FROM BRIDGEPROPERTIES");
      while (res.next()) {
        map.put(res.getString("KEY"), res.getString("VALUE"));
      }
    }
    catch (SQLException e)
    {
      logger.info("IDPMS Exceptions! " + e.getMessage());
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderConnection(con, stmt, res);
    }
    return map;
  }
  public static String getCurrentTimeStamp()
  {
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh.mm.ss.FF a");
    java.util.Date now = new java.util.Date();
    String strDate = sdf.format(now);
    return strDate;
  }
  public static java.util.Date getLocalDate()
  {
    return new java.util.Date();
  }
  public static String getDateInFormat(java.util.Date date, String dateFormat)
  {
    SimpleDateFormat sdf = null;
    String result = "";
    if (ValidationsUtil.isValidObject(date)) {
      try
      {
        sdf = new SimpleDateFormat(dateFormat);
        result = sdf.format(date);
      }
      catch (Exception e)
      {
        logger.info("IDPMS Date format error! " + e.getMessage());
        e.printStackTrace();
      }
    }
    return result;
  }
  public static String getBridgePropertyValue(String key)
  {
    logger.info("IDPMS Getting File Path Method ");
    String value = "";
    Connection con = null;
    ResultSet rs = null;
    PreparedStatement ps = null;
    try
    {
      con = DBConnectionUtility.getWiseConnection();
      if (con != null)
      {
        String bridgePropQuery = "SELECT ID, ZONE, BRANCH, KEY, VALUE, CATEGORY FROM BRIDGEPROPERTIES WHERE KEY = ? ";
        logger.info("IDPMS BridgePropQuery : " + bridgePropQuery + " Params[" + key + "]");
        ps = con.prepareStatement(bridgePropQuery);
        ps.setString(1, key);
        rs = ps.executeQuery();
        while (rs.next()) {
          value = rs.getString("VALUE");
        }
        logger.info(" IDPMS-----------File Location------------JOB Name : VALUE ---->>> " + key + 
          " Location File Saved---------: " + value);
      }
    }
    catch (Exception ex)
    {
      logger.info(
        " IDPMS-----------File Location----- getBridgePropertyValue--- Exception is :" + ex.getMessage());
      ex.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderPrepdConnection(con, ps, rs);
    }
    return value;
  }
  public static HashMap<String, String> getBatchJobMeta(String sourceSystem, Long batchstepid, String stepTaskName, StepContext stpContext3)
  {
    ResultSet rs = null;
    Connection con = null;
    PreparedStatement ps = null;
    HashMap<String, String> batchJobMetaMap = new HashMap();
    try
    {
      String phaseIdQuery = "Select trim(BATCHPHASE) as BATCHPHASE,  decode(trim(BATCHPHASE), 'STREONL', 'I', 'STREOD', 'II', 'STRSOD', 'III', 'STRSONL', 'IV', 'UNKNOWN' ) as PHASE_SEQ,  trim(STEPTASK) as STEPTASK, trim(STEPNAME) as STEPNAME, JOB_ID, STEPID, SEQUENCE, AUTOBYPASS, STATUS, PROCDATE, PARENT_PSA,  (TIMESTART + interval '330' minute) AS TIMESTART, (TIMEFINISH + interval '330' minute) AS TIMEFINISH  from BATCHSTEP where STEPID = ? order by SEQUENCE asc";

 
 
 
      logger.info("IDPMS BatchPhaseIDQuery: " + phaseIdQuery + "; Param[" + batchstepid + "]");

 
      con = DBConnectionUtility.getConnection();
      ps = con.prepareStatement(phaseIdQuery);
      ps.setLong(1, batchstepid.longValue());
      rs = ps.executeQuery();
      while (rs.next())
      {
        batchJobMetaMap.put("BATCHPHASE", rs.getString("BATCHPHASE"));
        batchJobMetaMap.put("PHASE_SEQ", rs.getString("PHASE_SEQ"));
        batchJobMetaMap.put("STEPTASK", rs.getString("STEPTASK"));
        batchJobMetaMap.put("STEPNAME", rs.getString("STEPNAME"));
        batchJobMetaMap.put("JOB_ID", rs.getString("JOB_ID"));
        batchJobMetaMap.put("STEPID", rs.getString("STEPID"));
        batchJobMetaMap.put("SEQUENCE", rs.getString("SEQUENCE"));
      }
    }
    catch (SQLException e)
    {
      logger.info("IDPMS JobID SQLException! " + e.getMessage());
      e.printStackTrace();
    }
    catch (Exception e)
    {
      logger.info("IDPMS JobID Exception! " + e.getMessage());
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderPrepdConnection(con, ps, rs);
    }
    logger.info("IDPMS BatchJobMetaMap " + batchJobMetaMap);
    return batchJobMetaMap;
  }
  public static java.sql.Date getTiValueDate(String sourceSystem)
  {
    java.sql.Date tiValueDate = null;
    ResultSet rs = null;
    Connection con = null;
    PreparedStatement ps = null;
    String query = "SELECT to_date(PROCDATE,'dd-mm-yy') as PROCDATE FROM DLYPRCCYCL ";
    logger.info("IDPMS TI_VALUE_DATE Query(dd-mm-yy) : " + query);
    try
    {
      con = DBConnectionUtility.getConnection();
      ps = con.prepareStatement(query);
      rs = ps.executeQuery();
      while (rs.next()) {
        tiValueDate = rs.getDate(1);
      }
    }
    catch (SQLException e)
    {
      logger.info("IDPMS SQL Exceptions! " + e.getMessage());
      e.printStackTrace();
    }
    catch (Exception e)
    {
      logger.info("IDPMS Exception! " + e.getMessage());
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderPrepdConnection(con, ps, rs);
    }
    return tiValueDate;
  }
  public static boolean doServiceLogging(String service, String operation, String hostNode, String branch, String sourcesystem, String targetsystem, String masterreference, String eventreference, String status, Timestamp processtime, String tirequest, String tiresponse, String bankrequest, String bankresponse, Timestamp tireqtime, Timestamp bankreqtime, Timestamp bankrestime, Timestamp tirestime, String narrative1, String narrative2, String transactionKey, String staticKey, String description)
  {
    logger.info("IDPMS.CustomJobs doServiceLogging initiated!");
    boolean result = true;
    Connection con = null;
    PreparedStatement pstmt = null;
    try
    {
      processtime = DateTimeUtil.getSqlLocalTimestamp();
      String serviceLoggingQuery = "Insert into SERVICELOG (ID, SERVICE,\tOPERATION, ZONE, BRANCH, SOURCESYSTEM, TARGETSYSTEM,  MASTERREFERENCE, EVENTREFERENCE, STATUS, PROCESSTIME, TIREQUEST, TIRESPONSE, BANKREQUEST, BANKRESPONSE,  TIREQTIME, BANKREQTIME, BANKRESTIME, TIRESTIME, NARRATIVE1, NARRATIVE2, TRANSACTIONKEY1, STATICKEY1, DESCRIPTION, NODE )  values (servicelog_seq.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

 
 
      logger.info("IDPMS.ServiceLoggingQuery : " + serviceLoggingQuery);
      con = DBConnectionUtility.getWiseConnection();
      pstmt = con.prepareStatement(serviceLoggingQuery);
      pstmt.setString(1, service);
      pstmt.setString(2, operation);
      pstmt.setString(3, sourcesystem);
      pstmt.setString(4, branch);
      pstmt.setString(5, sourcesystem);
      pstmt.setString(6, targetsystem);
      pstmt.setString(7, masterreference);
      pstmt.setString(8, eventreference);
      pstmt.setString(9, status);
      pstmt.setTimestamp(10, processtime);
      pstmt.setString(11, tirequest);
      pstmt.setString(12, tiresponse);
      pstmt.setString(13, bankrequest);
      pstmt.setString(14, bankresponse);
      pstmt.setTimestamp(15, tireqtime);
      pstmt.setTimestamp(16, bankreqtime);
      pstmt.setTimestamp(17, bankrestime);
      pstmt.setTimestamp(18, tirestime);
      pstmt.setString(19, narrative1);
      pstmt.setString(20, narrative2);
      pstmt.setString(21, transactionKey);
      pstmt.setString(22, staticKey);
      pstmt.setString(23, description);
      pstmt.setString(24, hostNode);
      int insertedRows = pstmt.executeUpdate();
      if (insertedRows == 1)
      {
        result = true;
        logger.info("IDPMS.CustomJobs doServiceLogging added successfully..!!!");
      }
    }
    catch (Exception e)
    {
      result = false;
      logger.info("IDPMS.CustomJobs doServiceLogging exception! " + e.getMessage());
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderPrepdConnection(con, pstmt, null);
    }
    logger.info("IDPMS.CustomJobs doServiceLogging completed! " + result);
    return result;
  }
  public static boolean doBatchCustomLogging(String service, String operation, String hostnode, String sourcesystem, String targetsystem, java.sql.Date valuedate, String batchjobid, String phase, String task, String taskdesc, String status, Timestamp processtime, String tirequest, String tiresponse, String bankrequest, String bankresponse, Timestamp tireqtime, Timestamp bankreqtime, Timestamp bankrestime, Timestamp tirestime, String narrative1, String narrative2, String narrative3, String description)
  {
    logger.info("IDPMS.CustomJobs doBatchCustomLogging initiated!");
    boolean result = true;
    Connection conn = null;
    PreparedStatement pstmt = null;
    try
    {
      processtime = DateTimeUtil.getSqlLocalTimestamp();
      String customJobsLogQuery = "INSERT INTO BATCHJOBSLOG (ID, SERVICE, OPERATION, SOURCESYSTEM, TARGETSYSTEM, VALUEDATE,  JOBID, PHASE, TASK, TASKDESC, STATUS, PROCESSTIME, TIREQUEST, TIRESPONSE, BANKREQUEST, BANKRESPONSE,  TIREQTIME, BANKREQTIME, BANKRESTIME, TIRESTIME, NARRATIVE1, NARRATIVE2, NARRATIVE3, DESCRIPTION, NODE )  VALUES (batchjobslog_seq.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

 
 
      logger.info("IDPMS.BatchCustomJobsLogQuery : " + customJobsLogQuery);
      conn = DBConnectionUtility.getWiseConnection();
      pstmt = conn.prepareStatement(customJobsLogQuery);
      pstmt.setString(1, service);
      pstmt.setString(2, operation);
      pstmt.setString(3, sourcesystem);
      pstmt.setString(4, targetsystem);
      pstmt.setDate(5, valuedate);
      pstmt.setString(6, batchjobid);
      pstmt.setString(7, phase);
      pstmt.setString(8, task);
      pstmt.setString(9, taskdesc);
      pstmt.setString(10, status);
      pstmt.setTimestamp(11, processtime);
      pstmt.setString(12, tirequest);
      pstmt.setString(13, tiresponse);
      pstmt.setString(14, bankrequest);
      pstmt.setString(15, bankresponse);
      pstmt.setTimestamp(16, tireqtime);
      pstmt.setTimestamp(17, bankreqtime);
      pstmt.setTimestamp(18, bankrestime);
      pstmt.setTimestamp(19, tirestime);
      pstmt.setString(20, narrative1);
      pstmt.setString(21, narrative2);
      pstmt.setString(22, narrative3);
      pstmt.setString(23, description);
      pstmt.setString(24, hostnode);
      int insertedRows = pstmt.executeUpdate();
      if (insertedRows == 1)
      {
        result = true;
        logger.info("IDPMS.CustomJobs doBatchCustomLogging added successfully! ");
      }
    }
    catch (Exception e)
    {
      result = false;
      logger.info("IDPMS.CustomJobs doBatchCustomLogging exception! " + e.getMessage());
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderPrepdConnection(conn, pstmt, null);
    }
    logger.info("IDPMS.CustomJobs doBatchCustomLogging completed!");
    return result;
  }
}