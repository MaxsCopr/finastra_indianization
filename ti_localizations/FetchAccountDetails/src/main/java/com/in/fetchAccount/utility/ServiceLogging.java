package com.in.fetchAccount.utility;
 
import java.sql.Connection;

import java.sql.Date;

import java.sql.PreparedStatement;

import java.sql.SQLException;

import java.sql.Timestamp;

import org.apache.log4j.Logger;
 
public class ServiceLogging

{

  private static Logger logger = Logger.getLogger(ServiceLogging.class.getName());

  public static void pushServiceLogData(String service, String operation, String zone, String source, String destination, String reference, String category, String status, String bankRequestJson, String bankResponseJson, Timestamp bankRequestTime, Timestamp bankResponseTime)

  {

    logger.info("Process entered into push Service Log Data process...!");

    String query = "INSERT INTO CUSTOM_FWC_SERVICE_LOG(SERVICE,OPERATION,ZONE, SOURCESYSTEM, TARGETSYSTEM,  REFERENCE, CATEGORY, STATUS,BANKREQUEST, BANKRESPONSE,BANKREQTIME,BANKRESTIME)  VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";

 
    Connection zoneConnection = null;

    PreparedStatement aPreparedStatement = null;

    try

    {

      zoneConnection = DBConnectionUtility.getZoneConnection();

      aPreparedStatement = zoneConnection.prepareStatement(query);

      aPreparedStatement.setString(1, service);

      aPreparedStatement.setString(2, operation);

      aPreparedStatement.setString(3, zone);

      aPreparedStatement.setString(4, source);

      aPreparedStatement.setString(5, destination);

      aPreparedStatement.setString(6, reference);

      aPreparedStatement.setString(7, category);

      aPreparedStatement.setString(8, status);

      aPreparedStatement.setString(9, bankRequestJson);

      aPreparedStatement.setString(10, bankResponseJson);

      aPreparedStatement.setTimestamp(11, bankRequestTime);

      aPreparedStatement.setTimestamp(12, bankResponseTime);

      aPreparedStatement.executeUpdate();

      logger.info(

        "pushServiceLogData is added successfully with count: " + aPreparedStatement.getUpdateCount());

    }

    catch (SQLException e)

    {

      e.printStackTrace();

      try

      {

        aPreparedStatement.close();

        zoneConnection.close();

      }

      catch (SQLException e)

      {

        e.printStackTrace();

      }

    }

    catch (Exception e)

    {

      e.printStackTrace();

      try

      {

        aPreparedStatement.close();

        zoneConnection.close();

      }

      catch (SQLException e)

      {

        e.printStackTrace();

      }

    }

    finally

    {

      try

      {

        aPreparedStatement.close();

        zoneConnection.close();

      }

      catch (SQLException e)

      {

        e.printStackTrace();

      }

    }

  }

  public static boolean insertLogData(String service, String operation, String zone, String branch, String sourceSys, String targetSys, String masterRef, String eventRef, String status, Date valueDate, String tiRequest, String tiResponse, String bankRequest, String bankResponse, Timestamp tiReqTime, Timestamp bankReqTime, Timestamp bankResTime, Timestamp tiResTime, String transactionkey1, String statickey1, String narrative1, String narrative2, boolean isReSubmitted, String reSubmittedCount, String description)

  {

    boolean result = true;

    Connection con = null;

    PreparedStatement ps = null;

    String query = "INSERT INTO SERVICELOG (ID,SERVICE,OPERATION,ZONE,BRANCH,SOURCESYSTEM,TARGETSYSTEM,MASTERREFERENCE,EVENTREFERENCE,STATUS,PROCESSTIME,TIREQUEST,TIRESPONSE,BANKREQUEST,BANKRESPONSE,TIREQTIME,BANKREQTIME,BANKRESTIME,TIRESTIME,TRANSACTIONKEY1,STATICKEY1,NARRATIVE1,NARRATIVE2,ISRESUBMITTED,RESUBMITTEDCOUNT,RESUBMITTEDTIME,DESCRIPTION,TYPEFLAG,NODE,VALUEDATE) VALUES (SERVICELOG_SEQ.NEXTVAL,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    try

    {

      con = DBConnectionUtility.getZoneConnection();

      ps = con.prepareStatement(query);

      ps.setString(1, service);

      ps.setString(2, operation);

      ps.setString(3, zone);

      ps.setString(4, branch);

      ps.setString(5, sourceSys);

      ps.setString(6, targetSys);

      ps.setString(7, masterRef);

      ps.setString(8, eventRef);

      ps.setString(9, status);

      ps.setDate(10, null);

      ps.setString(11, tiRequest);

      ps.setString(12, tiResponse);

      ps.setString(13, bankRequest);

      ps.setString(14, bankResponse);

      ps.setTimestamp(15, tiReqTime);

      ps.setTimestamp(16, bankReqTime);

      ps.setTimestamp(17, bankResTime);

      ps.setTimestamp(18, tiResTime);

      ps.setString(19, transactionkey1);

      ps.setString(20, statickey1);

      ps.setString(21, narrative1);

      ps.setString(22, narrative2);

      ps.setBoolean(23, isReSubmitted);

      ps.setInt(24, 0);

      ps.setTimestamp(25, null);

      ps.setString(26, description);

      ps.setString(27, "");

      ps.setString(28, "");

      ps.setDate(29, valueDate);

      ps.executeUpdate();

      result = true;

    }

    catch (Exception e)

    {

      e.printStackTrace();

      result = false;

    }

    finally

    {

      DBConnectionUtility.surrenderDB(con, ps, null);

    }

    return result;

  }

}