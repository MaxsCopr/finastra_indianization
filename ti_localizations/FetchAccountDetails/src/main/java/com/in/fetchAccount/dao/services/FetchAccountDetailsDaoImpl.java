package com.in.fetchAccount.dao.services;

import com.in.fetchAccount.bean.FetchAccountResponseBean;
import com.in.fetchAccount.utility.DBConnectionUtility;
import com.in.fetchAccount.utility.LoggableStatement;
import com.in.fetchAccount.utility.ValidationsUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

public class FetchAccountDetailsDaoImpl
{
  private static Logger logger = Logger.getLogger(FetchAccountDetailsDaoImpl.class.getName());
 
  public static List<String> getAccountBasedOnCustId(String custId)
  {
    logger.info("FetchAccountDetailsDaoImpl : getAccountBasedOnCustId : Started : custId :" + custId);
   
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    List<String> accountList = new ArrayList();
    try
    {
      String query = "select BO_ACCTNO as AccountVal from account where trim(cus_mnm)='" + custId + "' and TRIM(acc_type) in ('PCA','RPC','DL023') AND TRIM(DATECLOSED) IS NULL AND ACCT_KEY NOT LIKE '1BO%'";
     
      logger.info("FetchAccountDetailsDaoImpl : getAccountBasedOnCustId : query :" + query);
     
      con = DBConnectionUtility.getZoneConnection();
      pst = con.prepareStatement(query);
      rs = pst.executeQuery();
      while (rs.next())
      {
        String accountVal = rs.getString("AccountVal");
        String account = ValidationsUtil.isValidString(accountVal) ? accountVal.trim() : "";
        accountList.add(account);
      }
    }
    catch (Exception e)
    {
      logger.info("FetchAccountDetailsDaoImpl : getAccountBasedOnCustId : Exception :" + e.getMessage());
      logger.error(e);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    logger.info("FetchAccountDetailsDaoImpl : getAccountBasedOnCustId : accountList Size  :" + accountList.size());
   
    return accountList;
  }
 
  public static String getSessionUserId(String sessionUserId)
  {
    logger.info("FetchAccountDetailsDaoImpl : getSessionUserId : Started :");
   
    Connection globalConnection = null;
    LoggableStatement lst = null;
    ResultSet rst = null;
    String sessionUserName = "";
    try
    {
      globalConnection = DBConnectionUtility.getGlobalConnection();
     
      String get_User_ID = "SELECT SCT.USERNAME AS USER_ID FROM CENTRAL_SESSION_DETAILS SCT,LOCAL_SESSION_DETAILS LOC  WHERE SCT.CENTRAL_ID=LOC.CENTRAL_ID AND SCT.ENDED  IS NULL AND LOC.LOCAL_ID= ? ";
     

      lst = new LoggableStatement(globalConnection, get_User_ID);
     
      lst.setString(1, sessionUserId);
     
      logger.info("FetchAccountDetailsDaoImpl : getSessionUserId : Getting Session Value Query------------" + lst.getQueryString());
     
      rst = lst.executeQuery();
      while (rst.next())
      {
        String userName = rst.getString("USER_ID");
        sessionUserName = ValidationsUtil.isValidString(userName) ? userName.trim() : "";
      }
    }
    catch (Exception e)
    {
      logger.info("FetchAccountDetailsDaoImpl : getSessionUserId : Exception :" + e.getMessage());
      logger.error(e);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(globalConnection, lst, rst);
    }
    logger.info("FetchAccountDetailsDaoImpl : getSessionUserId : sessionUserName :" + sessionUserName);
    return sessionUserName;
  }
 
  public static void updateDataInTiScreen(List<FetchAccountResponseBean> fetchAccountResponseList, String keyValue)
  {
    logger.info("FetchAccountDetailsDaoImpl : updateDataInTiScreen : Started : keyValue :" + keyValue);
    try
    {
      String keyValueRef = ValidationsUtil.isValidString(keyValue) ? keyValue.trim() : "";
     
      logger.info("FetchAccountDetailsDaoImpl : updateDataInTiScreen : fetchAccountResponseList Size  :" + fetchAccountResponseList.size());
      if (fetchAccountResponseList.size() > 0)
      {
        int deleteCount = deleteDataInTiScreen(keyValueRef);
        for (FetchAccountResponseBean responseBean : fetchAccountResponseList)
        {
          String account = ValidationsUtil.isValidString(responseBean.getRequestAccountNo()) ? responseBean.getRequestAccountNo().trim() : "";
          String ccyVal = ValidationsUtil.isValidString(responseBean.getCurrencyRes()) ? responseBean.getCurrencyRes().trim() : "";
          String availBal = ValidationsUtil.isValidString(responseBean.getAvailBalAmt()) ? responseBean.getAvailBalAmt().trim() : "";
          String ledgerBal = ValidationsUtil.isValidString(responseBean.getLedgerBalance()) ? responseBean.getLedgerBalance().trim() : "";
          String msgtime = ValidationsUtil.isValidString(responseBean.getMsgtime()) ? responseBean.getMsgtime().trim() : "";
         

          addDataInTiScreen(keyValueRef, account, ccyVal, availBal, ledgerBal, msgtime);
        }
      }
    }
    catch (Exception e)
    {
      logger.info("FetchAccountDetailsDaoImpl : updateDataInTiScreen : Exception :" + e.getMessage());
      logger.error(e);
    }
  }
 
  public static int addDataInTiScreen(String keyValueRef, String account, String ccyVal, String availBal, String ledgerBal, String msgtime)
  {
    logger.info("FetchAccountDetailsDaoImpl : addDataInTiScreen : Started :keyValueRef :" + keyValueRef);
    Connection con = null;
    PreparedStatement ps = null;
    int count = 0;
    int keyVal = 0;
    try
    {
      int sequenceId = getSequenceId(keyValueRef);
      keyVal = Integer.parseInt(keyValueRef);
     
      String query = "INSERT INTO EXTEVENTACCOUNT (XKEY,SEQN,FK_EVENT,RACCOUNT,RACCTTYP,RAMOUNT,RPERCENT,RESPONSE) VALUES (EXTEVENTACCOUNT_SEQ.NEXTVAL,?,?,?,?,?,?,?)";
     

      con = DBConnectionUtility.getZoneConnection();
      ps = con.prepareStatement(query);
     
      ps.setInt(1, sequenceId);
      ps.setInt(2, keyVal);
      ps.setString(3, account);
      ps.setString(4, availBal);
      ps.setString(5, ccyVal);
      ps.setString(6, ledgerBal);
      ps.setString(7, msgtime);
     
      count = ps.executeUpdate();
     
      con.commit();
    }
    catch (Exception e)
    {
      logger.info("FetchAccountDetailsDaoImpl : addDataInTiScreen : Exception :" + e.getMessage());
      logger.error(e);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, ps, null);
    }
    logger.info("FetchAccountDetailsDaoImpl : addDataInTiScreen : keyVal :" + keyVal + " count :" + count);
   
    return count;
  }
 
  public static int getSequenceId(String keyValueRef)
  {
    logger.info("FetchAccountDetailsDaoImpl : getSequenceId : Started");
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rst = null;
    int sequenceId = 0;
    try
    {
      String query = "select MAX(SEQN) AS SeqVal from EXTEVENTACCOUNT where FK_EVENT=?";
     
      con = DBConnectionUtility.getZoneConnection();
      ps = con.prepareStatement(query);
     
      ps.setString(1, keyValueRef);
      rst = ps.executeQuery();
      while (rst.next())
      {
        sequenceId = rst.getInt("SeqVal");
        sequenceId++;
      }
    }
    catch (Exception e)
    {
      logger.info("FetchAccountDetailsDaoImpl : addDataInTiScreen : Exception :" + e.getMessage());
      logger.error(e);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, ps, rst);
    }
    logger.info("FetchAccountDetailsDaoImpl : getSequenceId : sequenceId :" + sequenceId);
   
    return sequenceId;
  }
 
  public static int deleteDataInTiScreen(String keyValueRef)
  {
    logger.info("FetchAccountDetailsDaoImpl : deleteDataInTiScreen : Started : keyValueRef :" + keyValueRef);
    int deletedCount = 0;
    ResultSet res = null;
    Connection conn = null;
    PreparedStatement ps = null;
   
    String deleteQuery = "DELETE FROM EXTEVENTACCOUNT WHERE FK_EVENT = ?";
    logger.info("deleteQuery: " + deleteQuery + "; Params[" + keyValueRef + "]");
    try
    {
      conn = DBConnectionUtility.getZoneConnection();
      ps = conn.prepareStatement(deleteQuery);
      ps.setString(1, keyValueRef);
     
      deletedCount = ps.executeUpdate();
     
      conn.commit();
    }
    catch (Exception e)
    {
      logger.info("FetchAccountDetailsDaoImpl : deleteDataInTiScreen : Exception :" + e.getMessage());
      logger.error(e);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(conn, ps, res);
    }
    logger.info("FetchAccountDetailsDaoImpl : deleteDataInTiScreen : deletedCount :" + deletedCount);
   
    return deletedCount;
  }
 
  public static String getDbKeyValue(String masterRef, String eventRef)
  {
    logger.info("FetchAccountDetailsDaoImpl : getDbKeyValue : Started");
    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rst = null;
    String keyValue = "";
    try
    {
      String query = " Select bev.extfield as keyValue from master mas,baseevent bev where mas.key97=bev.master_key and mas.MASTER_REF = '" + masterRef.trim() + "' and bev.REFNO_PFIX||LPAD(bev.refno_serl,3,0) = '" + eventRef.trim() + "' and bev.status not in ('a','c') ";
     
      logger.info("FetchAccountDetailsDaoImpl : getDbKeyValue : query :" + query);
      con = DBConnectionUtility.getZoneConnection();
      ps = con.prepareStatement(query);
     


      rst = ps.executeQuery();
      while (rst.next()) {
        keyValue = rst.getString("keyValue");
      }
    }
    catch (Exception e)
    {
      logger.info("FetchAccountDetailsDaoImpl : getDbKeyValue : Exception :" + e.getMessage());
      logger.error(e);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, ps, rst);
    }
    logger.info("FetchAccountDetailsDaoImpl : getDbKeyValue : keyValue :" + keyValue);
   
    return keyValue;
  }
}