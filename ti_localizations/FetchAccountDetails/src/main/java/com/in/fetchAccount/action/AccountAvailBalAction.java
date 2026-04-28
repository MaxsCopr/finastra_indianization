package com.in.fetchAccount.action;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.in.fetchAccount.bean.AccountAvailBalBankResAmt;
import com.in.fetchAccount.bean.AccountAvailBalBankResData;
import com.in.fetchAccount.bean.AccountAvailBalBankResponse;
import com.in.fetchAccount.bean.AccountAvailBalEncryptedRequest;
import com.in.fetchAccount.bean.AccountAvailRequestData;
import com.in.fetchAccount.bean.AccountAvailRequestHeader;
import com.in.fetchAccount.bean.FetchAccountResponseBean;
import com.in.fetchAccount.dao.services.AccountFetchLog;
import com.in.fetchAccount.utility.DBConnectionUtility;
import com.in.fetchAccount.utility.ValidationsUtil;
import com.infrasoft.kiya.security.EncryptionDecryptionImpl;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.log4j.Logger;

public class AccountAvailBalAction
{
  private static Logger logger = Logger.getLogger(AccountAvailBalAction.class.getName());
  static LinkedHashMap<String, String> TBProperties = new LinkedHashMap();
 
  public static FetchAccountResponseBean process(String accountNumber, String customerId)
  {
    HashMap<String, String> resultData = null;
    Timestamp bankReqTime = null;
    Timestamp bankResTime = null;
    String plainReqJson = "";
    String encReqJson = "";
    String msgId = "";
    String encRequest = "";
    String bankEncRes = "";
    String bankPlainRes = "";
    FetchAccountResponseBean fetchAccountResponseBean = new FetchAccountResponseBean();
    try
    {
      getProperties();
      String accApikey = (String)TBProperties.get("AVAIL_BAL_KEY");
      String accApiUrl = (String)TBProperties.get("AVAIL_BAL_URL");
      logger.info("AccountAvailBalAction : process : KEY :" + accApikey + " URL :" + accApiUrl);
     
      msgId = ValidationsUtil.getSqlLocalDateTime().toString();
      msgId = msgId.replaceAll("[- :.]", "");
      msgId = "61" + msgId;
     
      plainReqJson = generateAccountBalanceBankRequest(msgId, accountNumber);
     
      encReqJson = encryptAccBalReqJson(plainReqJson, accApikey);
     
      encRequest = generateAccBalEncReq(encReqJson, msgId);
     
      bankReqTime = ValidationsUtil.getSqlLocalDateTime();
     
      bankEncRes = getBankFinResponse(encRequest, accApiUrl);
     
      bankPlainRes = generateDecryptBankResponse(bankEncRes, accApikey);
     
      bankResTime = ValidationsUtil.getSqlLocalDateTime();
     
      fetchAccountResponseBean = getDataFromBankResponseJson(bankPlainRes);
      fetchAccountResponseBean.setRequestAccountNo(accountNumber);
    }
    catch (Exception e)
    {
      logger.info("AccountAvailBalAction : process : Exception :" + e.getMessage());
    }
    finally
    {
      AccountFetchLog.insertAccontDetailsLog("Account_API", customerId, accountNumber, plainReqJson,
        encReqJson, encRequest, bankEncRes, bankPlainRes, bankReqTime, bankResTime);
    }
    return fetchAccountResponseBean;
  }
 
  private static String generateAccountBalanceBankRequest(String msgId, String tireq_accountNumber)
    throws IOException
  {
    logger.info("\n AccountAvailBalAction : generateAccountBalanceBankRequest : Started");
   
    Gson aGson = new GsonBuilder().disableHtmlEscaping().create();
    String bankRequest = "";
    try
    {
      AccountAvailRequestHeader aAccountAvailRequestHeader = new AccountAvailRequestHeader();
      AccountAvailRequestData aAccountAvailRequestData = new AccountAvailRequestData();
     
      aAccountAvailRequestData.setType("account");
      aAccountAvailRequestData.setAccountNumber(tireq_accountNumber);
      aAccountAvailRequestData.setSenderCode("");
     
      aAccountAvailRequestHeader.setRequestType("0");
      aAccountAvailRequestHeader.setMsgid(msgId);
      aAccountAvailRequestHeader.setData(aAccountAvailRequestData);
     
      bankRequest = aGson.toJson(aAccountAvailRequestHeader);
    }
    catch (Exception e)
    {
      logger.info("\n AccountAvailBalAction : generateAccountBalanceBankRequest : Exception :" + e.getMessage());
      logger.error(e);
    }
    logger.info("\n AccountAvailBalAction : generateAccountBalanceBankRequest : bankRequest :" + bankRequest);
   
    return bankRequest;
  }
 
  private static String encryptAccBalReqJson(String bankRequestXml, String key)
  {
    logger.info("\n AccountAvailBalAction : encryptAccBalReqJson : Started");
    String encMes = null;
    EncryptionDecryptionImpl obj = new EncryptionDecryptionImpl();
    try
    {
      encMes = obj.encryptMessage(bankRequestXml, key);
    }
    catch (Exception e)
    {
      logger.info("\n AccountAvailBalAction : encryptAccBalReqJson : Exception :" + e.getMessage());
      logger.error(e);
    }
    logger.info("\n AccountAvailBalAction : encryptAccBalReqJson : encMes :" + encMes);
    return encMes;
  }
 
  public static String generateAccBalEncReq(String encReqJson, String msgid)
  {
    logger.info("\n AccountAvailBalAction : generateAccBalEncReq : Started");
    String encRequest = "";
    try
    {
      AccountAvailBalEncryptedRequest accountAvailBalReqEnc = new AccountAvailBalEncryptedRequest(encReqJson, msgid);
      Gson aGson = new Gson();
      encRequest = aGson.toJson(accountAvailBalReqEnc);
    }
    catch (Exception e)
    {
      logger.info("\n AccountAvailBalAction : generateAccBalEncReq : Exception :" + e.getMessage());
      logger.error(e);
      e.printStackTrace();
    }
    logger.info("\n AccountAvailBalAction : generateAccBalEncReq : encRequest :" + encRequest);
    return encRequest;
  }
 
  private static String getBankFinResponse(String bankEncReq, String url)
  {
    logger.info("\n AccountAvailBalAction : getBankFinResponse : Started");
    String encResponse = null;
    PostMethod post = new PostMethod(url);
    try
    {
      StringRequestEntity requestEntity = new StringRequestEntity(bankEncReq, "application/json", "utf-8");
      post.setRequestEntity(requestEntity);
      HttpClient httpclient = new HttpClient();
     
      int result = httpclient.executeMethod(post);
      if (result != 200) {
        throw new Exception("Server returned code " + result);
      }
      encResponse = post.getResponseBodyAsString();
    }
    catch (Exception e)
    {
      logger.info("\n AccountAvailBalAction : getBankFinResponse : Exception :" + e.getMessage());
      logger.error(e);
    }
    finally
    {
      post.releaseConnection();
    }
    logger.info("\n AccountAvailBalAction : getBankFinResponse : encResponse :" + encResponse);
   
    return encResponse.trim();
  }
 
  public static void getProperties()
  {
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    try
    {
      con = DBConnectionUtility.getWiseConnection();
      String query = "SELECT * FROM Bridgeproperties ";
      pst = con.prepareStatement(query);
      rs = pst.executeQuery();
      while (rs.next()) {
        TBProperties.put(rs.getString("key").trim(), rs.getString("value").trim());
      }
    }
    catch (Exception e)
    {
      logger.info("\n AccountAvailBalAction : getProperties : Exception :" + e.getMessage());
      logger.error(e);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    logger.info("AccountAvailBalAction : getProperties : Size Of Bridgeproperties From DB ---->" + TBProperties.size());
  }
 
  private static String generateDecryptBankResponse(String bankEncRes, String key)
  {
    logger.info("\n AccountAvailBalAction : generateDecryptBankResponse : Started");
   
    EncryptionDecryptionImpl obj = new EncryptionDecryptionImpl();
    String decMes = null;
    try
    {
      decMes = obj.decryptMessage(bankEncRes, key);
    }
    catch (Exception e)
    {
      logger.info("\n AccountAvailBalAction : generateDecryptBankResponse : Exception :" + e.getMessage());
      logger.error(e);
    }
    logger.info("\n AccountAvailBalAction : generateDecryptBankResponse : decMes :" + decMes);
    return decMes;
  }
 
  private static FetchAccountResponseBean getDataFromBankResponseJson(String bankResponseJson)
  {
    logger.info("\n AccountAvailBalAction : getDataFromBankResponseJson : Started");
   
    FetchAccountResponseBean fetchAccountResponseBean = new FetchAccountResponseBean();
    try
    {
      Gson aGson = new Gson();
     
      AccountAvailBalBankResponse availBalBankResp = (AccountAvailBalBankResponse)aGson.fromJson(bankResponseJson, AccountAvailBalBankResponse.class);
     
      String msgrrn = ValidationsUtil.isValidString(availBalBankResp.getMsgrrn()) ? availBalBankResp.getMsgrrn().trim() : "";
      fetchAccountResponseBean.setMsgrrn(msgrrn);
     
      String msgtime = ValidationsUtil.isValidString(availBalBankResp.getMsgtime()) ? availBalBankResp.getMsgtime().trim() : "";
      fetchAccountResponseBean.setMsgtime(msgtime);
     
      String msgid = ValidationsUtil.isValidString(availBalBankResp.getMsgid()) ? availBalBankResp.getMsgid().trim() : "";
      fetchAccountResponseBean.setMsgid(msgid);
     
      String channelName = ValidationsUtil.isValidString(availBalBankResp.getChannelName()) ? availBalBankResp.getChannelName().trim() : "";
      fetchAccountResponseBean.setChannelName(channelName);
     
      String status = ValidationsUtil.isValidString(availBalBankResp.getStatus()) ? availBalBankResp.getStatus().trim() : "";
      fetchAccountResponseBean.setStatus(status);
     
      String availBalAmt = ValidationsUtil.isValidString(availBalBankResp.getData().getAmount().getAvailBal()) ? availBalBankResp.getData().getAmount().getAvailBal().trim() : "";
      fetchAccountResponseBean.setAvailBalAmt(availBalAmt);
     
      String ledgerBalance = ValidationsUtil.isValidString(availBalBankResp.getData().getLedgerBalance()) ? availBalBankResp.getData().getLedgerBalance().trim() : "";
      fetchAccountResponseBean.setLedgerBalance(ledgerBalance);
     
      String currencyRes = ValidationsUtil.isValidString(availBalBankResp.getData().getCurrency()) ? availBalBankResp.getData().getCurrency().trim() : "";
      fetchAccountResponseBean.setCurrencyRes(currencyRes);
     
      String responseCode = ValidationsUtil.isValidString(availBalBankResp.getData().getResponseCode()) ? availBalBankResp.getData().getResponseCode().trim() : "";
      fetchAccountResponseBean.setResponseCode(responseCode);
    }
    catch (Exception e)
    {
      logger.info("\n AccountAvailBalAction : getDataFromBankResponseJson : Exception :" + e.getMessage());
      logger.error(e);
    }
    return fetchAccountResponseBean;
  }
}