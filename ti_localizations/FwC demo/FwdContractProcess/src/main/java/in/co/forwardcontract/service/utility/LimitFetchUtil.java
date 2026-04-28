package in.co.forwardcontract.service.utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import in.co.forwardcontract.service.model.LimitFetchBankReq;
import in.co.forwardcontract.service.model.LimitFetchBankRes;
import in.co.forwardcontract.service.model.LimitFetchBankResCustLimitDtls;
import in.co.forwardcontract.service.model.LimitFetchBankResData;
import in.co.forwardcontract.service.model.LimitFetchBankResDataNew;
import in.co.forwardcontract.service.model.LimitFetchBankResInqLtList;
import in.co.forwardcontract.service.model.LimitFetchBankResInqLtListNew;
import in.co.forwardcontract.service.model.LimitFetchBankResNew;
import in.co.forwardcontract.service.model.LimitFetchCustomerReq;
import in.co.forwardcontract.utility.CommonMethods;
import in.co.forwardcontract.utility.ServiceLogging;
import java.io.PrintStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

public class LimitFetchUtil
{
  private static Logger logger = Logger.getLogger(LimitFetchUtil.class.getName());
  static String tempEnc = "";
  static Gson aGson = new GsonBuilder().disableHtmlEscaping().create();
  static int tagcount;
 
  public static List<HashMap<String, String>> getLimitDetailsFromLimitAPI(String customer)
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
    String limitFetchURL = (String)ServiceUtility.TBProperties.get("LIMIT_FETCH_URL");
    String limitFetchKey = (String)ServiceUtility.TBProperties.get("LIMIT_FETCH_KEY");
    List<HashMap<String, String>> responseListTokens = null;
    try
    {
      logger.info("limitFetchURL & limitFetchKey  --> " + limitFetchURL + " & " + limitFetchKey);
     
      Map<String, String> result = generateLimitFetchBankRequest(customer);
      plainBankRequest = (String)result.get("JSON");
      logger.info("LimitFetch Bank Request in Json Format -->" + plainBankRequest);
     

      tempEncRequest = ServiceUtility.generateEncryptBankRequest(plainBankRequest, limitFetchKey);
     

      encBankRequest = generateEncryptedFtrtSelectJson(tempEncRequest, (String)result.get("MSGID"));
      bankRequestTime = CommonMethods.getSqlLocalDateTime();
      encBankResponse = ServiceUtility.getBankFinResponse(encBankRequest, limitFetchURL);
      bankResponseTime = CommonMethods.getSqlLocalDateTime();
     
      plainBankResponse = ServiceUtility.generateDecryptBankResponse(encBankResponse, limitFetchKey);
     
      responseListTokens = getLimitFetchResponseTokens(plainBankResponse);
      if (responseListTokens.size() > 0) {
        status = "SUCCEEDED";
      } else {
        status = "FAILED";
      }
      ServiceLogging.pushServiceLogData("Limit", "Fetch", "ZONE1", "FTI", "Finacle", customer, "", status,
        plainBankRequest, plainBankResponse, bankRequestTime, bankResponseTime);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return responseListTokens;
  }
 
  public static Map<String, String> generateLimitFetchBankRequest(String customer)
  {
    String bankRequest = null;
   

    LimitFetchBankReq limitFetchBankReq = new LimitFetchBankReq();
    LimitFetchCustomerReq limitFetchBankReqData = new LimitFetchCustomerReq();
    Map<String, String> result = new HashMap();
    String sequence = null;
    try
    {
      sequence = ServiceUtility.getSqlLocalDateTime().toString();
      sequence = sequence.replaceAll("[- :.]", "");
     
      logger.info("customer: " + customer);
     
      limitFetchBankReqData.setCustCifId(customer);
      limitFetchBankReq.setMsgid(sequence);
      limitFetchBankReq.setData(limitFetchBankReqData);
     
      bankRequest = aGson.toJson(limitFetchBankReq).trim();
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
    ServiceUtility encryptedReq = new ServiceUtility(bankEncRequest, msgId);
    String reqJson = aGson.toJson(encryptedReq).trim();
    return reqJson;
  }
 
  public static List<HashMap<String, String>> getLimitFetchResponseTokens(String plainFtrtSelectBankResponse)
  {
    LimitFetchBankRes limitFetchBankRes = new LimitFetchBankRes();
    List<HashMap<String, String>> hashMapList = new ArrayList();
    logger.info("Entering getLimitTokenDetails ");
    try
    {
      limitFetchBankRes = (LimitFetchBankRes)aGson.fromJson(plainFtrtSelectBankResponse, LimitFetchBankRes.class);
      if ((limitFetchBankRes != null) && (limitFetchBankRes.getData() != null) &&
        (limitFetchBankRes.getData().getInquireLimitList() != null))
      {
        tagcount = limitFetchBankRes.getData().getInquireLimitList().getCustomerLimitDetails().size();
        logger.info("BankResponse Customer Limit Details Count ---> : " + tagcount);
        for (int i = 0; i < tagcount; i++)
        {
          HashMap<String, String> hashmap = new HashMap();
         
          String expiryDate = CommonMethods.returnEmptyIfNull(
            ((LimitFetchBankResCustLimitDtls)limitFetchBankRes.getData().getInquireLimitList().getCustomerLimitDetails().get(i)).getExpiryDate());
          String expiryDateFormatted = CommonMethods.getTiDateFormat(expiryDate);
          hashmap.put("ti_expiryDate", expiryDateFormatted);
          hashmap.put("expiryDate", expiryDate);
         
          String crncyCode = CommonMethods.returnEmptyIfNull(
            ((LimitFetchBankResCustLimitDtls)limitFetchBankRes.getData().getInquireLimitList().getCustomerLimitDetails().get(i)).getCrncyCode());
          hashmap.put("crncyCode", crncyCode);
         
          String limitPrefix = CommonMethods.returnEmptyIfNull(
            ((LimitFetchBankResCustLimitDtls)limitFetchBankRes.getData().getInquireLimitList().getCustomerLimitDetails().get(i)).getLimitPrefix());
          hashmap.put("limitPrefix", limitPrefix);
         
          String limitSufix = CommonMethods.returnEmptyIfNull(
            ((LimitFetchBankResCustLimitDtls)limitFetchBankRes.getData().getInquireLimitList().getCustomerLimitDetails().get(i)).getLimitSuffix());
          hashmap.put("limitSuffix", limitSufix);
         
          String limitAmt = CommonMethods.returnZeroIfEmpty(CommonMethods.returnEmptyIfNull(
            ((LimitFetchBankResCustLimitDtls)limitFetchBankRes.getData().getInquireLimitList().getCustomerLimitDetails().get(i)).getLimitAmt()));
          hashmap.put("limitAmt", limitAmt);
         
          String sanDate = CommonMethods.getTiDateFormat(
            ((LimitFetchBankResCustLimitDtls)limitFetchBankRes.getData().getInquireLimitList().getCustomerLimitDetails().get(i)).getSanctionDate());
          hashmap.put("ti_sanDate", sanDate);
          hashmap.put("sanDate",
            ((LimitFetchBankResCustLimitDtls)limitFetchBankRes.getData().getInquireLimitList().getCustomerLimitDetails().get(i)).getSanctionDate());
         
          String limitDesc = CommonMethods.returnEmptyIfNull(
            ((LimitFetchBankResCustLimitDtls)limitFetchBankRes.getData().getInquireLimitList().getCustomerLimitDetails().get(i)).getLimitDesc());
          hashmap.put("limitDesc", limitDesc);
         
          String totalLiability =
            CommonMethods.returnZeroIfEmpty(CommonMethods.returnEmptyIfNull(
            ((LimitFetchBankResCustLimitDtls)limitFetchBankRes.getData().getInquireLimitList().getCustomerLimitDetails().get(i)).getTotalLiability()));
          hashmap.put("totalLiability", totalLiability);
         
          hashMapList.add(hashmap);
        }
      }
      logger.info("After fetching all the limit details - list size ----> " + hashMapList.size());
      logger.info("Exiting getLimitDetailsFromRes ");
    }
    catch (Exception e)
    {
      logger.info("Limit facilities exception in getLimitDetailsFromRes " + e.getMessage());
      e.printStackTrace();
      return getLimitDetailsFromResNew(plainFtrtSelectBankResponse);
    }
    return hashMapList;
  }
 
  public static List<HashMap<String, String>> getLimitDetailsFromResNew(String plainBankRes)
  {
    List<HashMap<String, String>> hashMapList = new ArrayList();
    System.out.println("Entering getLimitDetailsFromRes ");
    try
    {
      LimitFetchBankResNew aLimitFetchBankResNew = (LimitFetchBankResNew)aGson.fromJson(plainBankRes, LimitFetchBankResNew.class);
     
      HashMap<String, String> hashmap = new HashMap();
     
      String expiryDate = CommonMethods.returnEmptyIfNull(
        aLimitFetchBankResNew.getData().getInquireLimitList().getCustomerLimitDetails().getExpiryDate());
      String expiryDateFormatted = CommonMethods.getTiDateFormat(expiryDate);
      hashmap.put("ti_expiryDate", expiryDateFormatted);
      hashmap.put("expiryDate", expiryDate);
     
      String crncyCode = CommonMethods.returnEmptyIfNull(
        aLimitFetchBankResNew.getData().getInquireLimitList().getCustomerLimitDetails().getCrncyCode());
      hashmap.put("crncyCode", crncyCode);
     
      String limitPrefix = CommonMethods.returnEmptyIfNull(
        aLimitFetchBankResNew.getData().getInquireLimitList().getCustomerLimitDetails().getLimitPrefix());
      hashmap.put("limitPrefix", limitPrefix);
     
      String limitSufix = CommonMethods.returnEmptyIfNull(
        aLimitFetchBankResNew.getData().getInquireLimitList().getCustomerLimitDetails().getLimitSuffix());
      hashmap.put("limitSuffix", limitSufix);
     
      String limitAmt = CommonMethods.returnZeroIfEmpty(CommonMethods.returnEmptyIfNull(
        aLimitFetchBankResNew.getData().getInquireLimitList().getCustomerLimitDetails().getLimitAmt()));
      hashmap.put("limitAmt", limitAmt);
     
      String sanDate = CommonMethods.getTiDateFormat(
        aLimitFetchBankResNew.getData().getInquireLimitList().getCustomerLimitDetails().getSanctionDate());
      hashmap.put("ti_sanDate", sanDate);
      hashmap.put("sanDate",
        aLimitFetchBankResNew.getData().getInquireLimitList().getCustomerLimitDetails().getSanctionDate());
     
      String limitDesc = CommonMethods.returnEmptyIfNull(
        aLimitFetchBankResNew.getData().getInquireLimitList().getCustomerLimitDetails().getLimitDesc());
     


      hashmap.put("limitDesc", limitDesc);
     
      String totalLiability =
        CommonMethods.returnZeroIfEmpty(CommonMethods.returnEmptyIfNull(aLimitFetchBankResNew.getData()
        .getInquireLimitList().getCustomerLimitDetails().getTotalLiability()));
      hashmap.put("totalLiability", totalLiability);
     
      hashMapList.add(hashmap);
     
      System.out.println("After fecthing all the limit details list size ----> " + hashMapList.size());
      System.out.println("Exiting getLimitDetailsFromRes ");
    }
    catch (Exception e)
    {
      System.out.println("Limit facilities exception in getLimitDetailsFromRes " + e.getMessage());
      e.printStackTrace();
    }
    return hashMapList;
  }
}
