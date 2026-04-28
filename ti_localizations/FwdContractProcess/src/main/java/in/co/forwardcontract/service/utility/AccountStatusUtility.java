package in.co.forwardcontract.service.utility;

import com.google.gson.Gson;
import com.infrasoft.kiya.security.EncryptionDecryptionImpl;
import in.co.forwardcontract.service.model.AccountStatusReq;
import in.co.forwardcontract.service.model.AccountStatusReqEnc;
import in.co.forwardcontract.service.model.AccountStatusResp;
import in.co.forwardcontract.service.model.ReqAccStatusData;
import in.co.forwardcontract.service.model.ResData;
import java.io.PrintStream;
import java.util.LinkedHashMap;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
 
public class AccountStatusUtility
{
  EncryptionDecryptionImpl obj = new EncryptionDecryptionImpl();
  public String getStatusAccount(String requestType, String msgid, String type, String accountNumber, String senderCode)
  {
    ServiceUtility.getProperties();
    String key = (String)ServiceUtility.TBProperties.get("ACCOUNT_STATUS_KEY");
    String url = (String)ServiceUtility.TBProperties.get("ACCOUNT_STATUS_URL");
    String accStatus = "";
    try
    {
      String plainReqJson = generateAccountStatusJson(requestType, msgid, type, accountNumber, senderCode);
      System.out.println("Account Status Plain Request : " + plainReqJson);
      String encReqJson = encryptReqJson(plainReqJson, key);
      System.out.println("Account Status Encrypted Request : " + encReqJson);
      String encRequest = generateEncReq(encReqJson, msgid);
      String bankEncRes = callBankEndPoint(encRequest, url);
      System.out.println("Account Status Encrypted Bank Response : " + bankEncRes);
      String bankPlainRes = decryptResJson(bankEncRes, key);
      System.out.println("Account Status Plain Bank Response : " + bankPlainRes);
      accStatus = getAccountStatus(bankPlainRes);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return accStatus;
  }
  private String generateAccountStatusJson(String requestType, String msgid, String type, String accountNumber, String senderCode)
  {
    AccountStatusReq aAccountStatusReq = new AccountStatusReq();
    ReqAccStatusData aReqAccStatusData = new ReqAccStatusData();
    aAccountStatusReq.setRequestType(requestType);
    aAccountStatusReq.setMsgid(msgid);
    aReqAccStatusData.setType(type);
    aReqAccStatusData.setAccountNumber(accountNumber);
    aReqAccStatusData.setSenderCode(senderCode);
    aAccountStatusReq.setData(aReqAccStatusData);
    Gson agson = new Gson();
    String jsonString = agson.toJson(aAccountStatusReq);
    return jsonString;
  }
  public String generateEncReq(String encReqJson, String msgid)
  {
    AccountStatusReqEnc aAccountStatusReqEnc = new AccountStatusReqEnc();
    aAccountStatusReqEnc.setReqdata(encReqJson);
    aAccountStatusReqEnc.setMsgid(msgid);
    Gson aGson = new Gson();
    return aGson.toJson(aAccountStatusReqEnc);
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
    AccountStatusResp aAccountStatusResp = (AccountStatusResp)aGson.fromJson(decRespJson, AccountStatusResp.class);
    return aAccountStatusResp.getData().getStatus();
  }
}
