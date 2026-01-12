package com.in.fetchAccount.dao.services;
 
import com.in.fetchAccount.utility.DBConnectionUtility;

import java.sql.Connection;

import java.sql.PreparedStatement;

import java.sql.Timestamp;

import org.apache.log4j.Logger;
 
public class AccountFetchLog

{

  private static Logger logger = Logger.getLogger(AccountFetchLog.class.getName());

  public static int insertAccontDetailsLog(String service, String customerId, String accountNumber, String plainReqJson, String encReqJson, String encRequest, String bankEncRes, String bankPlainRes, Timestamp bankReqTime, Timestamp bankResTime)

  {

    logger.info("AccountFetchLog : insertAccontDetailsLog : Started");

    Connection con = null;

    PreparedStatement ps = null;

    int count = 0;

    try

    {

      String query = "INSERT INTO PCA_ACCOUNT_FETCH_LOG (ID,SERVICE,CUSTOMERID,ACCOUNT_NO,BANKREQUEST,ENC_REQUEST_JSON,ENC_REQUEST,ENC_RESPONSE,BANKRESPONSE,BANKREQTIME,BANKRESTIME ) VALUES (PCA_ACCOUNT_FETCH_LOG_SEQ.NEXTVAL,?,?,?,?,?,?,?,?,?,?)";

 
 
      con = DBConnectionUtility.getWiseConnection();

      ps = con.prepareStatement(query);

      ps.setString(1, service);

      ps.setString(2, customerId);

      ps.setString(3, accountNumber);

      ps.setString(4, plainReqJson);

      ps.setString(5, encReqJson);

      ps.setString(6, encRequest);

      ps.setString(7, bankEncRes);

      ps.setString(8, bankPlainRes);

      ps.setTimestamp(9, bankReqTime);

      ps.setTimestamp(10, bankResTime);

      count = ps.executeUpdate();

    }

    catch (Exception e)

    {

      logger.info("AccountFetchLog : insertAccontDetailsLog : Exception :" + e.getMessage());

      logger.error(e);

    }

    finally

    {

      DBConnectionUtility.surrenderDB(con, ps, null);

    }

    logger.info("AccountFetchLog : insertAccontDetailsLog : count :" + count);

    return count;

  }

}