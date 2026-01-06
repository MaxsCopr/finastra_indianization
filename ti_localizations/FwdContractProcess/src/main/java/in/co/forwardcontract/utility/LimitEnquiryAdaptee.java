package in.co.forwardcontract.utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.infrasoft.kiya.security.EncryptionDecryptionImpl;
import in.co.forwardcontract.service.model.DateTimeUtil;
import in.co.forwardcontract.service.model.LimitEnquiryReqEnc;
import in.co.forwardcontract.service.model.LimitEnquiryRequestData;
import in.co.forwardcontract.service.model.LimitEnquiryRequestHeader;
import in.co.forwardcontract.service.model.LimitEnquiryRespWithObject;
import in.co.forwardcontract.service.model.LimitEnquiryRespWithObjectA;
import in.co.forwardcontract.service.model.LimitEnquiryResponseData;
import in.co.forwardcontract.service.model.LimitEnquiryResponseDataA;
import in.co.forwardcontract.service.model.LimitEnquiryResponseLimitDetails;
import in.co.forwardcontract.service.model.LimitEnquiryResponseLimitList;
import in.co.forwardcontract.service.model.LimitEnquiryResponseLimitListA;
import java.io.PrintStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
 
public class LimitEnquiryAdaptee
{
  LimitEnquiryRequestData aLimitEnquiryRequestData = new LimitEnquiryRequestData();
  LimitEnquiryRequestHeader aLimitEnquiryRequestHeader = new LimitEnquiryRequestHeader();
  EncryptionDecryptionImpl obj = new EncryptionDecryptionImpl();
  Gson aGson = new GsonBuilder().disableHtmlEscaping().create();
  public String enquireLimitSerialNum(String custId, String refnum, String limitSuffix, String eventRef)
  {
    String serialNum = null;
    String requestType = "0";
    String msgId = DateTimeUtil.getSqlLocalDateTime().toString();
    msgId = msgId.replaceAll("[- :.]", "");
    String plainReqJson = generateLimitEnquiryReq(requestType, custId, msgId, limitSuffix);
    System.out.println("Limit Enquiry Plain Request : " + plainReqJson);
    CommonMethods.getProperties();
    String key = (String)CommonMethods.TBProperties.get("LIMIT_ENQUIRY_KEY");
    String url = (String)CommonMethods.TBProperties.get("LIMIT_ENQUIRY_URL");
    String encReqJson = encryptReqJson(plainReqJson, key);
    System.out.println("Limit Enquiry URL&Key : " + url + " & " + key);
    System.out.println("Limit Enquiry Encrypted Request : " + encReqJson);
    String encRequest = generateEncReq(encReqJson, msgId);
    System.out.println("Enquiry encRequest:" + encRequest);
    String bankEncRes = callBankEndPoint(encRequest, url);
    String bankPlainRes = decryptResJson(bankEncRes, key);
    System.out.println("Limit Enquiry Plain Bank Response : " + bankPlainRes);
    if (bankPlainRes != null)
    {
      if ((!bankPlainRes.contains("ErrorDetailList")) && (!bankPlainRes.contains("\"successorfailure\":\"N\"")))
      {
        serialNum = getRespSerialNum(bankPlainRes, refnum);
        ServiceLogging.pushServiceLogData("Limit", "Enquiry", "ZONE1", "FTI", "Finacle", refnum, serialNum, "SUCCEEDED", 
          plainReqJson, bankPlainRes, DateTimeUtil.getSqlLocalDateTime(), DateTimeUtil.getSqlLocalDateTime());
      }
      else
      {
        ServiceLogging.pushServiceLogData("Limit", "Enquiry", "ZONE1", "FTI", "Finacle", refnum, "", "FAILED", 
          plainReqJson, bankPlainRes, DateTimeUtil.getSqlLocalDateTime(), DateTimeUtil.getSqlLocalDateTime());
      }
    }
    else {
      ServiceLogging.pushServiceLogData("Limit", "Enquiry", "ZONE1", "FTI", "Finacle", refnum, "", "FAILED", 
        plainReqJson, bankPlainRes, DateTimeUtil.getSqlLocalDateTime(), DateTimeUtil.getSqlLocalDateTime());
    }
    return serialNum;
  }
  private String getRespSerialNum(String bankPlainRes, String refnum)
  {
    String serial_num = null;
    if ((refnum == null) || (refnum.isEmpty())) {
      return serial_num;
    }
    Gson aGsonLimResp = new Gson();
    LimitEnquiryRespWithObject aLimitEnquiryRespWithObject = new LimitEnquiryRespWithObject();
    LimitEnquiryRespWithObjectA aLimitEnquiryRespWithObjectA = new LimitEnquiryRespWithObjectA();
    try
    {
      if ((bankPlainRes != null) && (bankPlainRes.contains("\"UserLimitDetails\":[")))
      {
        aLimitEnquiryRespWithObject = (LimitEnquiryRespWithObject)aGsonLimResp.fromJson(bankPlainRes, LimitEnquiryRespWithObject.class);
        if ((aLimitEnquiryRespWithObject != null) && (aLimitEnquiryRespWithObject.getData() != null) && 
          (aLimitEnquiryRespWithObject.getData().getUserMaintainedLimitInquiryList() != null) && 
          (aLimitEnquiryRespWithObject.getData().getUserMaintainedLimitInquiryList()
          .getUserLimitDetails() != null)) {
          for (int j = 0; j < aLimitEnquiryRespWithObject.getData().getUserMaintainedLimitInquiryList()
                .getUserLimitDetails().size(); j++) {
            if (
              ((LimitEnquiryResponseLimitDetails)aLimitEnquiryRespWithObject.getData().getUserMaintainedLimitInquiryList().getUserLimitDetails().get(j)).getReferenceId() != null) {
              if (((LimitEnquiryResponseLimitDetails)aLimitEnquiryRespWithObject.getData().getUserMaintainedLimitInquiryList().getUserLimitDetails().get(j)).getReferenceId().equalsIgnoreCase(refnum)) {
                serial_num = 
                  ((LimitEnquiryResponseLimitDetails)aLimitEnquiryRespWithObject.getData().getUserMaintainedLimitInquiryList().getUserLimitDetails().get(j)).getKeySrNo();
              }
            }
          }
        }
      }
      else
      {
        aLimitEnquiryRespWithObjectA = (LimitEnquiryRespWithObjectA)aGsonLimResp.fromJson(bankPlainRes, LimitEnquiryRespWithObjectA.class);
        if ((aLimitEnquiryRespWithObjectA != null) && (aLimitEnquiryRespWithObjectA.getData() != null) && 
          (aLimitEnquiryRespWithObjectA.getData().getUserMaintainedLimitInquiryList() != null) && 
          (aLimitEnquiryRespWithObjectA.getData().getUserMaintainedLimitInquiryList()
          .getUserLimitDetails() != null)) {
          if (aLimitEnquiryRespWithObjectA.getData().getUserMaintainedLimitInquiryList().getUserLimitDetails()
            .getReferenceId() != null) {
            if (aLimitEnquiryRespWithObjectA.getData().getUserMaintainedLimitInquiryList().getUserLimitDetails().getReferenceId().equalsIgnoreCase(refnum)) {
              serial_num = 
                aLimitEnquiryRespWithObjectA.getData().getUserMaintainedLimitInquiryList().getUserLimitDetails().getKeySrNo();
            }
          }
        }
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      System.out.println("exception in converting response to json in : getRespSerialNum" + e.getMessage());
    }
    System.out.println("serial_num value in : " + serial_num);
    return serial_num;
  }
  public String generateLimitEnquiryReq(String requestType, String custId, String msgId, String limitSuffix)
  {
    this.aLimitEnquiryRequestData.setCustId(custId);
    this.aLimitEnquiryRequestData.setSUFFIX(limitSuffix);
    this.aLimitEnquiryRequestHeader.setRequestType(requestType);
    this.aLimitEnquiryRequestHeader.setMsgid(msgId);
    this.aLimitEnquiryRequestHeader.setData(this.aLimitEnquiryRequestData);
    String bankReqJson = this.aGson.toJson(this.aLimitEnquiryRequestHeader);
    System.out.println("generateLimitEnquiryReq bankReqJson : " + bankReqJson);
    return bankReqJson;
  }
  public String encryptReqJson(String reqJsonStr, String key)
  {
    String encMes = null;
    EncryptionDecryptionImpl obj = new EncryptionDecryptionImpl();
    try
    {
      encMes = obj.encryptMessage(reqJsonStr, key);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return encMes;
  }
  public String decryptResJson(String encJsonResp, String key)
  {
    String plainResJson = null;
    EncryptionDecryptionImpl obj = new EncryptionDecryptionImpl();
    try
    {
      plainResJson = obj.decryptMessage(encJsonResp, key);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return plainResJson;
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
  public String generateEncReq(String encReqJson, String msgid)
  {
    LimitEnquiryReqEnc aLimitEnquiryReqEnc = new LimitEnquiryReqEnc();
    aLimitEnquiryReqEnc.setReqdata(encReqJson);
    aLimitEnquiryReqEnc.setMsgid(msgid);
    Gson aGson = new Gson();
    return aGson.toJson(aLimitEnquiryReqEnc);
  }
}
