package in.co.forwardcontract.service.utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import in.co.forwardcontract.service.model.FtrtUpdateBankReq;
import in.co.forwardcontract.service.model.FtrtUpdateBankReqData;
import in.co.forwardcontract.service.model.TreasuryBankRes;
import in.co.forwardcontract.service.model.TreasuryBankResCustomData;
import in.co.forwardcontract.service.model.TreasuryBankResData;
import in.co.forwardcontract.utility.CommonMethods;
import in.co.forwardcontract.utility.ServiceLogging;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.log4j.Logger;
 
public class FtrtUpdateUtil
{
  private static Logger logger = Logger.getLogger(FtrtUpdateUtil.class.getName());
  static String bankRequestJson = "";
  static String bankEncRequest = "";
  static String EncrequestJson = "";
  static Gson aGson = new GsonBuilder().disableHtmlEscaping().create();
  public static Map<String, String> updateUtilizedAmountInFinacle(String treasuryRefNo, String fwdContractAmt)
  {
    String plainBankRequest = "";
    String tempEncRequest = "";
    String encBankRequest = "";
    String encBankResponse = "";
    String plainBankResponse = "";
    Timestamp bankRequestTime = null;
    Timestamp bankResponseTime = null;
    String status = "FAILED";
    ServiceUtility.getProperties();
    String ftrtUpdateURL = (String)ServiceUtility.TBProperties.get("FTRT_UPDATE_URL");
    String ftrtUpdateKey = (String)ServiceUtility.TBProperties.get("FTRT_UPDATE_KEY");
    Map<String, String> responseTokens = null;
    logger.info("Inside updateUtilizedAmountInFinacle");
    try
    {
      Map<String, String> result = generateFtrtUpdateBankRequest(treasuryRefNo, fwdContractAmt);
      plainBankRequest = (String)result.get("JSON");
      logger.info("ftrtUpdate Bank Request in Json Format -->" + plainBankRequest);
      tempEncRequest = ServiceUtility.generateEncryptBankRequest(plainBankRequest, ftrtUpdateKey);

 
      encBankRequest = generateEncryptedFtrtUpdateJson(tempEncRequest, (String)result.get("MSGID"));
      logger.info("ftrtUpdate Bank Enc Request -->" + encBankRequest);
      bankRequestTime = CommonMethods.getSqlLocalDateTime();
      encBankResponse = ServiceUtility.getBankFinResponse(encBankRequest, ftrtUpdateURL);
      bankResponseTime = CommonMethods.getSqlLocalDateTime();
      plainBankResponse = ServiceUtility.generateDecryptBankResponse(encBankResponse, ftrtUpdateKey);
      logger.info("ftrtUpdate Bank Json Response -->" + plainBankResponse);
      if (plainBankResponse != null) {
        responseTokens = getFtrtUpdateResponseTokens(plainBankResponse);
      }
      if ((plainBankResponse != null) && (((String)responseTokens.get("FtrtUpdateStatus")).contains("S"))) {
        status = "SUCCEEDED";
      } else {
        status = "FAILED";
      }
      ServiceLogging.pushServiceLogData("FTRT", "FTRTUpdate", "ZONE1", "FTI", "Finacle", treasuryRefNo, "", status, 
        plainBankRequest, plainBankResponse, bankRequestTime, bankResponseTime);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return responseTokens;
  }
  public static Map<String, String> generateFtrtUpdateBankRequest(String treasuryRefNo, String fwdContractAmt)
  {
    FtrtUpdateBankReq ftrtUpdateBankReq = new FtrtUpdateBankReq();
    FtrtUpdateBankReqData ftrtUpdateBankReqData = new FtrtUpdateBankReqData();
    String bankRequest = null;
    Map<String, String> result = new HashMap();
    String sequence = null;
    try
    {
      String option = "5";
      String status = "U";
      logger.info("trRefNum: " + treasuryRefNo);
      logger.info("fwdContractAmt: " + fwdContractAmt);
      sequence = ServiceUtility.getSqlLocalDateTime().toString();
      sequence = sequence.replaceAll("[- :.]", "");
      ftrtUpdateBankReqData.setOption(option);
      ftrtUpdateBankReqData.setStatus(status);
      ftrtUpdateBankReqData.setUtilizedAmount(fwdContractAmt);
      ftrtUpdateBankReqData.setTrRefNum(treasuryRefNo);
      ftrtUpdateBankReq.setRequestType("0");
      ftrtUpdateBankReq.setMsgid(sequence);
      ftrtUpdateBankReq.setData(ftrtUpdateBankReqData);
      bankRequest = aGson.toJson(ftrtUpdateBankReq).trim();
      logger.info("bankRequest of FtrtUpdate: " + bankRequest);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    result.put("JSON", bankRequest);
    result.put("MSGID", sequence);
    return result;
  }
  public static String generateEncryptedFtrtUpdateJson(String bankEncRequest, String msgId)
  {
    ServiceUtility encryptedReq = new ServiceUtility(bankEncRequest, 
      msgId);
    String reqJson = aGson.toJson(encryptedReq).trim();
    return reqJson;
  }
  public static Map<String, String> getFtrtUpdateResponseTokens(String plainBankResponse)
  {
    TreasuryBankRes treasuryBankRes = new TreasuryBankRes();
    Map<String, String> ftrtUpdateTokens = new HashMap();
    ftrtUpdateTokens.put("FtrtUpdateStatus", "F");
    logger.info("Entering getFtrtUpdateResponseTokens ");
    try
    {
      treasuryBankRes = (TreasuryBankRes)aGson.fromJson(plainBankResponse, TreasuryBankRes.class);
      if ((treasuryBankRes != null) && (treasuryBankRes.getData() != null) && 
        (treasuryBankRes.getData().getExecuteFinacleScript_CustomData() != null))
      {
        String status = treasuryBankRes.getData().getExecuteFinacleScript_CustomData().getSuccessorfailure();
        ftrtUpdateTokens.put("FtrtUpdateStatus", status);
      }
      else
      {
        ftrtUpdateTokens.put("FtrtUpdateStatus", "F");
      }
      logger.info("Exiting getFtrtUpdateResponseTokens ");
    }
    catch (Exception e)
    {
      logger.info("Exception in getFtrtUpdateResponseTokens " + e.getMessage());
      e.printStackTrace();
    }
    return ftrtUpdateTokens;
  }
}
