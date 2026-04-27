package in.co.forwardcontract.service.utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import in.co.forwardcontract.service.model.FtrtSelectBankReq;
import in.co.forwardcontract.service.model.FtrtSelectBankReqData;
import in.co.forwardcontract.service.model.TreasuryBankRes;
import in.co.forwardcontract.service.model.TreasuryBankResCustomData;
import in.co.forwardcontract.service.model.TreasuryBankResCustomDataDetails;
import in.co.forwardcontract.service.model.TreasuryBankResData;
import in.co.forwardcontract.utility.CommonMethods;
import in.co.forwardcontract.utility.ServiceLogging;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.log4j.Logger;
 
public class FtrtSelectUtil
{
  private static Logger logger = Logger.getLogger(FtrtSelectUtil.class.getName());
  static String bankRequestJson = "";
  static String bankEncRequest = "";
  static String EncrequestJson = "";
  static Gson aGson = new GsonBuilder().disableHtmlEscaping().create();
  public static Map<String, String> getRateDetailsFromFtrtAPI(String contractRef, String customer)
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
    String ftrtSelectURL = (String)ServiceUtility.TBProperties.get("FTRT_SELECT_URL");
    String ftrtSelectKey = (String)ServiceUtility.TBProperties.get("FTRT_SELECT_KEY");
    Map<String, String> responseTokens = null;
    try
    {
      logger.info("ftrtSelectURL & ftrtSelectKey  --> " + ftrtSelectURL + " & " + ftrtSelectKey);
      Map<String, String> result = generateFtrtSelectBankRequest(contractRef, customer);
      plainBankRequest = (String)result.get("JSON");
      logger.info("FtrtSelect Bank Request in Json Format -->" + plainBankRequest);

 
      tempEncRequest = ServiceUtility.generateEncryptBankRequest(plainBankRequest, ftrtSelectKey);

 
      encBankRequest = generateEncryptedFtrtSelectJson(tempEncRequest, (String)result.get("MSGID"));
      logger.info("FtrtSelect Bank Enc Request -->" + encBankRequest);
      bankRequestTime = CommonMethods.getSqlLocalDateTime();
      encBankResponse = ServiceUtility.getBankFinResponse(encBankRequest, ftrtSelectURL);
      bankResponseTime = CommonMethods.getSqlLocalDateTime();
      plainBankResponse = ServiceUtility.generateDecryptBankResponse(encBankResponse, ftrtSelectKey);
      logger.info("FtrtSelect Bank Json Response -->" + plainBankResponse);
      responseTokens = getRateFtrtTokenDetails(plainBankResponse);
      if (((String)responseTokens.get("FtrtSelectStatus")).contains("S")) {
        status = "SUCCEEDED";
      } else {
        status = "FAILED";
      }
      ServiceLogging.pushServiceLogData("FTRT", "FTRTSelect", "ZONE1", "FTI", "Finacle", contractRef, "", status, 
        plainBankRequest, plainBankResponse, bankRequestTime, bankResponseTime);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return responseTokens;
  }
  public static Map<String, String> generateFtrtSelectBankRequest(String contractRef, String customer)
  {
    FtrtSelectBankReqData aRequestData = new FtrtSelectBankReqData();
    FtrtSelectBankReq aRequestHeader = new FtrtSelectBankReq();
    Map<String, String> result = new HashMap();
    String bankRequest = null;
    String sequence = null;
    try
    {
      String option = "4";
      sequence = ServiceUtility.getSqlLocalDateTime().toString();
      sequence = sequence.replaceAll("[- :.]", "");
      logger.info("contractRef: " + contractRef);
      logger.info("customer: " + customer);
      aRequestData.setOption(option);
      aRequestData.setTrRefNum(contractRef);
      aRequestData.setCifId(customer);
      aRequestHeader.setRequestType("0");
      aRequestHeader.setMsgid(sequence);
      aRequestHeader.setData(aRequestData);
      bankRequest = aGson.toJson(aRequestHeader).trim();
      logger.info("bankRequest of FtrtSelect: " + bankRequest);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    result.put("JSON", bankRequest);
    result.put("MSGID", sequence);
    return result;
  }
  public static String generateEncryptedFtrtSelectJson(String bankEncRequest, String msgId)
  {
    ServiceUtility encryptedReq = new ServiceUtility(bankEncRequest, 
      msgId);
    String reqJson = aGson.toJson(encryptedReq).trim();
    return reqJson;
  }
  public static Map<String, String> getRateFtrtTokenDetails(String plainFtrtSelectBankResponse)
  {
    Map<String, String> rateTokens = new HashMap();
    TreasuryBankRes treasuryBankRes = new TreasuryBankRes();
    rateTokens.put("FtrtSelectStatus", "F");
    try
    {
      treasuryBankRes = (TreasuryBankRes)aGson.fromJson(plainFtrtSelectBankResponse, TreasuryBankRes.class);
      if ((treasuryBankRes != null) && (treasuryBankRes.getData() != null) && 
        (treasuryBankRes.getData().getExecuteFinacleScript_CustomData() != null))
      {
        String status = treasuryBankRes.getData().getExecuteFinacleScript_CustomData().getSuccessorfailure();
        rateTokens.put("FtrtSelectStatus", status);
        if (status.equalsIgnoreCase("F"))
        {
          String message = treasuryBankRes.getData().getExecuteFinacleScript_CustomData().getMessage();
          rateTokens.put("Message", message);
        }
        logger.info("status -->" + status);
        if (status.equalsIgnoreCase("S"))
        {
          TreasuryBankResCustomDataDetails ftrtSelectBankResCustomDataDetails = treasuryBankRes.getData()
            .getExecuteFinacleScript_CustomData().getStatementTransactionDetail();
          rateTokens.put("TrRefNum", ftrtSelectBankResCustomDataDetails.getTR_REF_NUM());
          rateTokens.put("SwapRate", ftrtSelectBankResCustomDataDetails.getSWAP_RATE());
          rateTokens.put("CustRate", ftrtSelectBankResCustomDataDetails.getCUST_RATE());
          rateTokens.put("FcRefNum", ftrtSelectBankResCustomDataDetails.getFC_REF_NUM());
          rateTokens.put("RelatedTrRefNum", ftrtSelectBankResCustomDataDetails.getRELATED_TR_REF_NUM());
          rateTokens.put("Remarks", ftrtSelectBankResCustomDataDetails.getREMARKS());
          rateTokens.put("BankId", ftrtSelectBankResCustomDataDetails.getBANK_ID());
          rateTokens.put("ToCrncyCode", ftrtSelectBankResCustomDataDetails.getTO_CRNCY_CODE());
          rateTokens.put("Status", ftrtSelectBankResCustomDataDetails.getSTATUS());
          rateTokens.put("EntityCreFlg", ftrtSelectBankResCustomDataDetails.getENTITY_CRE_FLG());
          rateTokens.put("RateCode", ftrtSelectBankResCustomDataDetails.getRATECODE());
          rateTokens.put("LchgUserId", ftrtSelectBankResCustomDataDetails.getLCHG_USER_ID());
          rateTokens.put("SwapChargeRate", ftrtSelectBankResCustomDataDetails.getSWAP_CHARGE_RATE());
          rateTokens.put("BuyOrSell", ftrtSelectBankResCustomDataDetails.getBUY_OR_SELL());
          rateTokens.put("TsCnt", ftrtSelectBankResCustomDataDetails.getTS_CNT());
          rateTokens.put("RefAmt", ftrtSelectBankResCustomDataDetails.getREF_AMT());
          rateTokens.put("CifId", ftrtSelectBankResCustomDataDetails.getCIF_ID());
          rateTokens.put("FreeCode1", ftrtSelectBankResCustomDataDetails.getFREE_CODE_1());
          rateTokens.put("UtilizedAmt", ftrtSelectBankResCustomDataDetails.getUTILIZED_AMT());
          rateTokens.put("FreeCode2", ftrtSelectBankResCustomDataDetails.getFREE_CODE_2());
          rateTokens.put("FreeCode3", ftrtSelectBankResCustomDataDetails.getFREE_CODE_3());
          rateTokens.put("RcreUserId", ftrtSelectBankResCustomDataDetails.getRCRE_USER_ID());
          rateTokens.put("DelFlg", ftrtSelectBankResCustomDataDetails.getDEL_FLG());
          rateTokens.put("FromCrncyCode", ftrtSelectBankResCustomDataDetails.getFROM_CRNCY_CODE());
          rateTokens.put("FundsDeliveryDate", ftrtSelectBankResCustomDataDetails.getFUNDS_DELIVERY_DATE());
          rateTokens.put("RcreTime", ftrtSelectBankResCustomDataDetails.getRCRE_TIME());
          rateTokens.put("RequestDate", ftrtSelectBankResCustomDataDetails.getREQUEST_DATE());
          rateTokens.put("EventId", ftrtSelectBankResCustomDataDetails.getEVENT_ID());
          rateTokens.put("LchgTime", ftrtSelectBankResCustomDataDetails.getLCHG_TIME());
          rateTokens.put("TreasuryRate", ftrtSelectBankResCustomDataDetails.getTREASURY_RATE());
          logger.info("Ftrt API for TrRefNum: " + (String)rateTokens.get("TrRefNum"));
          logger.info("Ref & Utilized Amount: " + (String)rateTokens.get("RefAmt") + " & " + 
            (String)rateTokens.get("UtilizedAmt"));
        }
      }
      else
      {
        rateTokens.put("FtrtSelectStatus", "F");
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return rateTokens;
  }
}
