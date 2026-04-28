package in.co.forwardcontract.service.utility;

import com.google.gson.Gson;
import com.infrasoft.kiya.security.EncryptionDecryptionImpl;
import in.co.forwardcontract.service.model.AccountAvailBalBankResAmt;
import in.co.forwardcontract.service.model.AccountAvailBalBankResData;
import in.co.forwardcontract.service.model.AccountAvailBalBankResponse;
import in.co.forwardcontract.service.model.AccountAvailBalEncryptedRequest;
import in.co.forwardcontract.service.model.AccountAvailRequestData;
import in.co.forwardcontract.service.model.AccountAvailRequestHeader;
import java.util.LinkedHashMap;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.log4j.Logger;
 
public class AvailBalAuthCheckUtility
{
  EncryptionDecryptionImpl obj = new EncryptionDecryptionImpl();
  private static Logger logger = Logger.getLogger(AvailBalAuthCheckUtility.class.getName());
  public String getAccountBalance(String requestType, String msgid, String type, String accountNumber, String senderCode)
  {
    ServiceUtility.getProperties();

 
    String key = (String)ServiceUtility.TBProperties.get("AVAIL_BAL_KEY");
    String url = (String)ServiceUtility.TBProperties.get("AVAIL_BAL_URL");
    String accBalance = "";
    try
    {
      String plainReqJson = generateAccountBalanceJson(requestType, msgid, type, accountNumber, senderCode);
      logger.info("Account Balance Plain Request : " + plainReqJson);
      String encReqJson = encryptReqJson(plainReqJson, key);
      logger.info("Account Balance Encrypted Request : " + encReqJson);
      String encRequest = generateEncReq(encReqJson, msgid);
      String bankEncRes = callBankEndPoint(encRequest, url);
      logger.info("Account Balance Encrypted Bank Response : " + bankEncRes);
      String bankPlainRes = decryptResJson(bankEncRes, key);
      if (bankPlainRes != null) {
        logger.info("Account Balance Plain Bank Response : " + bankPlainRes);
      } else {
        logger.info("Account Balance Plain Bank Response is NULL");
      }
      accBalance = getAccountStatus(bankPlainRes);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    logger.info("Account Balance Plain Bank Response : " + accBalance);
    return accBalance;
  }
  private String generateAccountBalanceJson(String requestType, String msgid, String type, String accountNumber, String senderCode)
  {
    AccountAvailRequestHeader aAccountBalanceReq = new AccountAvailRequestHeader();
    AccountAvailRequestData aReqAccBalanceData = new AccountAvailRequestData();
    aAccountBalanceReq.setRequestType(requestType);
    aAccountBalanceReq.setMsgid(msgid);
    aReqAccBalanceData.setType(type);
    aReqAccBalanceData.setAccountNumber(accountNumber);
    aReqAccBalanceData.setSenderCode(senderCode);
    aAccountBalanceReq.setData(aReqAccBalanceData);
    Gson agson = new Gson();
    String jsonString = agson.toJson(aAccountBalanceReq);
    return jsonString;
  }
  public String generateEncReq(String encReqJson, String msgid)
  {
    AccountAvailBalEncryptedRequest aAccountBalanceReqEnc = new AccountAvailBalEncryptedRequest();
    aAccountBalanceReqEnc.setReqdata(encReqJson);
    aAccountBalanceReqEnc.setMsgid(msgid);
    Gson aGson = new Gson();
    return aGson.toJson(aAccountBalanceReqEnc);
  }
  public String encryptReqJson(String reqJsonStr, String key)
  {
    String encMes = null;
    try
    {
      encMes = this.obj.encryptMessage(reqJsonStr, key);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return encMes;
  }
  public String callBankEndPoint(String bankEncReq, String url)
  {
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
      e.printStackTrace();
    }
    finally
    {
      post.releaseConnection();
    }
    return encResponse.trim();
  }
  public String decryptResJson(String encJsonResp, String key)
  {
    String plainResJson = null;
    try
    {
      plainResJson = this.obj.decryptMessage(encJsonResp, key);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return plainResJson;
  }
  public String getAccountStatus(String decRespJson)
  {
    Gson aGson = new Gson();
    AccountAvailBalBankResponse aAccountBalanceResp = (AccountAvailBalBankResponse)aGson.fromJson(decRespJson, AccountAvailBalBankResponse.class);
    if ((aAccountBalanceResp.getData() != null) && (aAccountBalanceResp.getData().getAmount() != null) && (aAccountBalanceResp.getData().getAmount().getAvailBal() != null)) {
      return aAccountBalanceResp.getData().getAmount().getAvailBal();
    }
    return "";
  }
}
