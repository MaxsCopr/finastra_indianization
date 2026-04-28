package in.co.forwardcontract.service.utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import in.co.forwardcontract.service.model.TreasUpdateBankReq;
import in.co.forwardcontract.service.model.TreasUpdateBankReqData;
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
 
public class TreasUpdateUtil
{
  private static Logger logger = Logger.getLogger(TreasUpdateUtil.class.getName());
  static String bankRequestJson = "";
  static String bankEncRequest = "";
  static String EncrequestJson = "";
  static Gson aGson = new GsonBuilder().disableHtmlEscaping().create();
  public static Map<String, String> updateUtilizationAmountInTreasury(String treasuryRefNo, String fwdContractAmt)
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
    String treasUpdateURL = (String)ServiceUtility.TBProperties.get("TREAS_UPDATE_URL");
    String treasUpdateKey = (String)ServiceUtility.TBProperties.get("TREAS_UPDATE_KEY");
    Map<String, String> responseTokens = null;
    logger.info("Inside updateUtilizedAmountInTreasury");
    try
    {
      Map<String, String> result = generateTreasUpdateBankRequest(treasuryRefNo, fwdContractAmt);
      plainBankRequest = (String)result.get("JSON");
      logger.info("treasUpdate Bank Request in Json Format -->" + plainBankRequest);

 
      tempEncRequest = ServiceUtility.generateEncryptBankRequest(plainBankRequest, treasUpdateKey);

 
      encBankRequest = generateEncryptedTreasUpdateJson(tempEncRequest, (String)result.get("MSGID"));
      logger.info("treasUpdate Bank Enc Request -->" + encBankRequest);
      bankRequestTime = CommonMethods.getSqlLocalDateTime();
      encBankResponse = ServiceUtility.getBankFinResponse(encBankRequest, treasUpdateURL);
      bankResponseTime = CommonMethods.getSqlLocalDateTime();
      plainBankResponse = ServiceUtility.generateDecryptBankResponse(encBankResponse, treasUpdateKey);
      logger.info("treasUpdate Bank Json Response -->" + plainBankResponse);
      if (plainBankResponse != null) {
        responseTokens = getTreasUpdateResponseTokens(plainBankResponse);
      }
      if ((plainBankResponse != null) && (((String)responseTokens.get("TreasUpdateStatus")).contains("S"))) {
        status = "SUCCEEDED";
      } else {
        status = "FAILED";
      }
      ServiceLogging.pushServiceLogData("TREAS", "TREASUpdate", "ZONE1", "FTI", "Treasury", treasuryRefNo, "", status, 
        plainBankRequest, plainBankResponse, bankRequestTime, bankResponseTime);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return responseTokens;
  }
  public static Map<String, String> generateTreasUpdateBankRequest(String treasuryRefNo, String fwdContractAmt)
  {
    Map<String, String> result = new HashMap();
    TreasUpdateBankReq aRequestHeader = new TreasUpdateBankReq();
    TreasUpdateBankReqData aRequestData = new TreasUpdateBankReqData();
    String sequence = null;
    String bankRequest = null;
    try
    {
      String option = "7";
      logger.info("treRefNo: " + treasuryRefNo);
      logger.info("fwdContractAmt: " + fwdContractAmt);
      sequence = ServiceUtility.getSqlLocalDateTime().toString();
      sequence = sequence.replaceAll("[- :.]", "");
      aRequestData.setOption(option);
      aRequestData.setUtilizationAmount(fwdContractAmt);
      aRequestData.setUnUtilizedAmount("0");
      aRequestData.setRefAmount(fwdContractAmt);
      aRequestData.setTreRefNo(treasuryRefNo);
      aRequestHeader.setRequestType("0");
      aRequestHeader.setMsgid(sequence);
      aRequestHeader.setData(aRequestData);
      bankRequest = aGson.toJson(aRequestHeader).trim();
      logger.info("bankRequest of TreasUpdate: " + bankRequest);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    result.put("JSON", bankRequest);
    result.put("MSGID", sequence);
    return result;
  }
  public static String generateEncryptedTreasUpdateJson(String bankEncRequest, String msgId)
  {
    ServiceUtility aBackOfficeBatchEncryptedReq = new ServiceUtility(bankEncRequest, 
      msgId);
    String reqJson = aGson.toJson(aBackOfficeBatchEncryptedReq).trim();
    return reqJson;
  }
  public static Map<String, String> getTreasUpdateResponseTokens(String plainBankResponse)
  {
    Map<String, String> treasUpdateTokens = new HashMap();
    TreasuryBankRes treasuryBankRes = new TreasuryBankRes();
    treasUpdateTokens.put("TreasUpdateStatus", "F");
    logger.info("Entering getTreasUpdateResponseTokens ");
    try
    {
      treasuryBankRes = (TreasuryBankRes)aGson.fromJson(plainBankResponse, TreasuryBankRes.class);
      if ((treasuryBankRes != null) && (treasuryBankRes.getData() != null) && 
        (treasuryBankRes.getData().getExecuteFinacleScript_CustomData() != null))
      {
        String status = treasuryBankRes.getData().getExecuteFinacleScript_CustomData().getSuccessorfailure();
        treasUpdateTokens.put("TreasUpdateStatus", status);
      }
      else
      {
        treasUpdateTokens.put("TreasUpdateStatus", "F");
      }
      logger.info("Exiting getTreasUpdateResponseTokens ");
    }
    catch (Exception e)
    {
      logger.info("Exception in getTreasUpdateResponseTokens " + e.getMessage());
      e.printStackTrace();
    }
    return treasUpdateTokens;
  }
}
