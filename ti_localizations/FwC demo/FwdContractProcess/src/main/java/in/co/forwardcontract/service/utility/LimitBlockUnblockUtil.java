package in.co.forwardcontract.service.utility;

import com.google.gson.Gson;
import com.infrasoft.kiya.security.EncryptionDecryptionImpl;
import in.co.forwardcontract.service.model.DateTimeUtil;
import in.co.forwardcontract.service.model.LimitBlockReq;
import in.co.forwardcontract.service.model.LimitBlockReqEnc;
import in.co.forwardcontract.service.model.LimitBlockResp;
import in.co.forwardcontract.service.model.LimitBlockRespWithObject;
import in.co.forwardcontract.service.model.ReqKey;
import in.co.forwardcontract.service.model.ReqModifyLimitNodeInputVO;
import in.co.forwardcontract.service.model.ReqUmlLiabValue;
import in.co.forwardcontract.service.model.ReqUserMaintLiabModLL;
import in.co.forwardcontract.service.model.ResLimitBlockResKey;
import in.co.forwardcontract.service.model.ResUserMaintLiabModLL;
import in.co.forwardcontract.utility.CommonMethods;
import in.co.forwardcontract.utility.DBConnectionUtility;
import in.co.forwardcontract.utility.LimitEnquiryAdaptee;
import in.co.forwardcontract.utility.LoggableStatement;
import in.co.forwardcontract.utility.ServiceLogging;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.log4j.Logger;

public class LimitBlockUnblockUtil
{
  private static Logger logger = Logger.getLogger(LimitBlockUnblockUtil.class.getName());
 
  public static Map<String, String> limitexposurethroughAPI(String fwdContractNo, String limitID, String limitAmount, String limitCcy, String BlockorUnblockstatus, String category)
  {
    Map<String, String> responseTokens = new HashMap();
    Timestamp bankRequestTime = null;
    Timestamp bankResponseTime = null;
    String plainReqJson = "";
    String bankPlainRes = "";
    String umlRefId = "";
    String umlRemarks = "";
    String status = "FAILED";
    try
    {
      int prefixEnd = limitID.indexOf("/");
      String limitPrefix = limitID.substring(0, prefixEnd);
      String limitSuffix = limitID.substring(prefixEnd + 1);
     
      String sequence = ServiceUtility.getSqlLocalDateTime().toString();
      sequence = sequence.replaceAll("[- :.]", "");
     
      logger.info("Fwd Contract No : " + fwdContractNo);
      String serialNum = "";
     




      umlRefId = fwdContractNo;
      Date tiSysdate = DateTimeUtil.getTISystemSqlDate();
     
      SimpleDateFormat simpDate = new SimpleDateFormat("yyyy-MM-dd");
     


      String umlDate = simpDate.format(tiSysdate) + "T" + DateTimeUtil.getH24Time();
      String umlEndDate = "2099-12-31T" + DateTimeUtil.getH24Time();
      String umlDept = "001";
      logger.info("umlDept:" + umlDept);
      if (limitAmount != null)
      {
        BigDecimal amtBD = new BigDecimal(limitAmount);
        if ((amtBD.compareTo(new BigDecimal(0)) == 0) || (amtBD.compareTo(new BigDecimal(0)) == -1)) {
          limitAmount = "0.001";
        }
      }
      logger.info("limitAmount" + limitAmount);
      logger.info("Limit Currency:" + limitCcy);
      LimitEnquiryAdaptee aLimitEnquiryAdaptee = new LimitEnquiryAdaptee();
      serialNum = aLimitEnquiryAdaptee.enquireLimitSerialNum(limitPrefix, umlRefId, limitSuffix, "");
      logger.info("serial_num value in Limit Enquiry API: " + serialNum);
      if ((serialNum == null) || (serialNum.isEmpty())) {
        serialNum = "009999";
      }
      logger.info("Serial Number:" + serialNum);
      String umlReasonCode = "ADHBG";
      umlRemarks = fwdContractNo;
      String msgId = DateTimeUtil.getSqlLocalDateTime().toString();
      msgId = msgId.replaceAll("[- :.]", "");
     
      logger.info("msgId:" + msgId);
      plainReqJson = generateLimitBlockReqJson(limitPrefix, limitSuffix, serialNum, umlRefId, umlDate,
        umlEndDate, umlDept, limitAmount, limitCcy, umlReasonCode, umlRemarks, msgId);
     
      logger.info("Plain Request values:");
     
      logger.info("Limit Block Plain Request : " + plainReqJson);
     
      ServiceUtility.getProperties();
      String key = (String)ServiceUtility.TBProperties.get("LIMIT_BLOCK_NEW_KEY");
      String url = (String)ServiceUtility.TBProperties.get("LIMIT_BLOCK_NEW_URL");
      String encReqJson = encryptReqJson(plainReqJson, key);
     
      logger.info("Limit Block URL&Key : " + url + " & " + key);
      logger.info("Limit Block Encrypted Request : " + encReqJson);
      bankRequestTime = CommonMethods.getSqlLocalDateTime();
     

      String encRequest = generateEncReq(encReqJson, msgId);
      logger.info("encRequest:" + encRequest);
      String bankEncRes = callBankEndPoint(encRequest, url);
     
      logger.info("Limit Block Encrypted Bank Response : " + bankEncRes);
      bankResponseTime = CommonMethods.getSqlLocalDateTime();
      bankPlainRes = decryptResJson(bankEncRes, key);
     

      logger.info("Limit Block Plain Bank Response : " + bankPlainRes);
      String serialnum = null;
      if (bankPlainRes != null) {
        serialnum = getRespSerialNum(bankPlainRes, umlRemarks);
      }
      logger.info("Limit Block serialnum after getRespSerialNum : " + serialnum);
      if (serialnum != null) {
        insertLimitDetails(umlRefId, "", serialnum, limitPrefix + "/" + limitSuffix, limitAmount, limitCcy);
      }
      logger.info("Limit Block serialnum after insertLimitDetails : " + serialnum);
      if (CommonMethods.isValidString(serialnum))
      {
        responseTokens.put("SerialNumber", serialnum);
        responseTokens.put("LimitBOUStatus", "S");
      }
      else
      {
        responseTokens.put("LimitBOUStatus", "F");
      }
      if (((String)responseTokens.get("LimitBOUStatus")).contains("S")) {
        status = "SUCCEEDED";
      } else {
        status = "FAILED";
      }
      logger.info("status:" + status);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      ServiceLogging.pushServiceLogData("Limit", BlockorUnblockstatus, "ZONE1", "FTI", "Finacle", umlRefId, "", status,
        plainReqJson, bankPlainRes, bankRequestTime, bankResponseTime);
     
      insertLogData("LIMIT", "EXPOSURE", "", "", "", "", umlRemarks, "", status, null,
        "", "", plainReqJson, bankPlainRes, null, null, DateTimeUtil.getSqlLocalDateTime(), DateTimeUtil.getSqlLocalDateTime(), "", "", "", "", false, "0", "");
    }
    return responseTokens;
  }
 
  public static void limitreversethroughAPI(String fwdContractNo, String limitID, String limitAmount, String limitCcy, String blockorUnblockstatus, String serialnum)
  {
    Timestamp bankRequestTime = null;
    Timestamp bankResponseTime = null;
    String status = "FAILED";
    try
    {
      int prefixEnd = limitID.indexOf("/");
      String limitPrefix = limitID.substring(0, prefixEnd);
      String limitSuffix = limitID.substring(prefixEnd + 1);
     
      String sequence = ServiceUtility.getSqlLocalDateTime().toString();
      sequence = sequence.replaceAll("[- :.]", "");
     
      logger.info("Fwd Contract No : " + fwdContractNo);
     
      String umlRefId = fwdContractNo;
      Date tiSysdate = DateTimeUtil.getTISystemSqlDate();
     
      SimpleDateFormat simpDate = new SimpleDateFormat("yyyy-MM-dd");
      String umlDate = simpDate.format(tiSysdate) + "T" + DateTimeUtil.getH24Time();
      String umlEndDate = "2099-12-31T" + DateTimeUtil.getH24Time();
      String umlDept = "001";
      logger.info("umlDept:" + umlDept);
      if (limitAmount != null)
      {
        BigDecimal amtBD = new BigDecimal(limitAmount);
        if (amtBD.compareTo(new BigDecimal(0)) == 0) {
          limitAmount = "0.001";
        }
      }
      String umlReasonCode = "ADHBG";
      String umlRemarks = fwdContractNo;
      String msgId = DateTimeUtil.getSqlLocalDateTime().toString();
      msgId = msgId.replaceAll("[- :.]", "");
     
      logger.info("msgId:" + msgId);
      String plainReqJson = generateLimitBlockReqJson(limitPrefix, limitSuffix, serialnum, umlRefId, umlDate,
        umlEndDate, umlDept, limitAmount, limitCcy, umlReasonCode, umlRemarks, msgId);
     

      logger.info("Plain Request values:");
     
      logger.info("Limit Block Plain Request : " + plainReqJson);
     
      ServiceUtility.getProperties();
      String key = (String)ServiceUtility.TBProperties.get("LIMIT_BLOCK_NEW_KEY");
      String url = (String)ServiceUtility.TBProperties.get("LIMIT_BLOCK_NEW_URL");
      String encReqJson = encryptReqJson(plainReqJson, key);
     
      logger.info("Limit Block URL&Key : " + url + " & " + key);
      logger.info("Limit Block Encrypted Request : " + encReqJson);
      bankRequestTime = CommonMethods.getSqlLocalDateTime();
     

      String encRequest = generateEncReq(encReqJson, msgId);
      logger.info("encRequest:" + encRequest);
      String bankEncRes = callBankEndPoint(encRequest, url);
     
      logger.info("Limit Block Encrypted Bank Response : " + bankEncRes);
      bankResponseTime = CommonMethods.getSqlLocalDateTime();
      String bankPlainRes = decryptResJson(bankEncRes, key);
     

      logger.info("Limit Block Plain Bank Response : " + bankPlainRes);
      if (bankPlainRes != null)
      {
        if ((!bankPlainRes.contains("ErrorDetailList")) && (!bankPlainRes.contains("\"successorfailure\":\"N\""))) {
          status = "SUCCEEDED";
        } else {
          status = "FAILED";
        }
      }
      else {
        status = "FAILED";
      }
      ServiceLogging.pushServiceLogData("Limit", blockorUnblockstatus, "ZONE1", "FTI", "Finacle", umlRefId, "", status,
        plainReqJson, bankPlainRes, bankRequestTime, bankResponseTime);
      logger.info("Limit has been reversed successfully for fwdContractNo : " + fwdContractNo);
    }
    catch (Exception e)
    {
      e.printStackTrace();
      logger.info("Exception in Limit reversal for fwdContractNo : " + fwdContractNo + " error : " + e.getMessage());
    }
  }
 
  public static String generateEncReq(String encReqJson, String msgid)
  {
    logger.info("Generating Encryption Request");
    LimitBlockReqEnc aLimitBlockReqEnc = new LimitBlockReqEnc();
    aLimitBlockReqEnc.setReqdata(encReqJson);
    aLimitBlockReqEnc.setMsgid(msgid);
    Gson aGson = new Gson();
   
    logger.info(aLimitBlockReqEnc.getReqdata() + " " + aLimitBlockReqEnc.getMsgid());
    return aGson.toJson(aLimitBlockReqEnc);
  }
 
  public static String encryptReqJson(String reqJsonStr, String key)
  {
    EncryptionDecryptionImpl obj = new EncryptionDecryptionImpl();
    String encMes = null;
    try
    {
      logger.info("Encrypt Json:");
      encMes = obj.encryptMessage(reqJsonStr, key);
     
      logger.info("Encryption Message:" + encMes);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return encMes;
  }
 
  public static String callBankEndPoint(String bankEncReq, String url)
  {
    String encResponse = null;
    PostMethod post = new PostMethod(url);
    try
    {
      logger.info("Call Bank End Point");
      StringRequestEntity requestEntity = new StringRequestEntity(bankEncReq, "application/json", "utf-8");
      post.setRequestEntity(requestEntity);
      HttpClient httpclient = new HttpClient();
      int result = httpclient.executeMethod(post);
      if (result != 200) {
        throw new Exception("Server returned code " + result);
      }
      encResponse = post.getResponseBodyAsString();
     
      logger.info("Encryption Response:" + encResponse);
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
 
  public static String decryptResJson(String encJsonResp, String key)
  {
    EncryptionDecryptionImpl obj = new EncryptionDecryptionImpl();
    String plainResJson = null;
    try
    {
      logger.info("Decrypt Res Json");
      plainResJson = obj.decryptMessage(encJsonResp, key);
      logger.info("plainResJson" + plainResJson);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return plainResJson;
  }
 
  public static String generateLimitBlockReqJson(String limitPrefix, String limitSuffix, String serialNum, String umlRefId, String umlDate, String umlEndDate, String umlDept, String amountValue, String ccyCode, String umlReasonCode, String umlRemarks, String msgId)
  {
    LimitBlockReq lim = new LimitBlockReq();
    ReqModifyLimitNodeInputVO aModifyLimitNodeInputVO = new ReqModifyLimitNodeInputVO();
    List<ReqUserMaintLiabModLL> usermainlist = new LinkedList();
    ReqUserMaintLiabModLL usermain = new ReqUserMaintLiabModLL();
   
    logger.info("GenerateLimit Request:");
   

    ReqKey aReqKey = new ReqKey();
    ReqUmlLiabValue mlval = new ReqUmlLiabValue();
   
    aModifyLimitNodeInputVO.setlimitPrefix(limitPrefix);
    aModifyLimitNodeInputVO.setlimitSuffix(limitSuffix);
   
    aReqKey.setserial_num(serialNum);
   
    usermain.setUmlReferenceId(umlRefId);
    usermain.setUmlDate(umlDate);
    usermain.setUmlEndDate(umlEndDate);
    usermain.setUmlDept(umlDept);
   
    mlval.setAmountValue(amountValue);
    mlval.setCurrencyCode(ccyCode);
   
    usermain.setUmlReasonCode(umlReasonCode);
    usermain.setUmlRemarks(umlRemarks);
   
    usermain.setKey(aReqKey);
    usermain.setUmlLiabValue(mlval);
    usermainlist.add(usermain);
   
    aModifyLimitNodeInputVO.setUserMaintLiabModLL(usermainlist);
   
    lim.setModifyLimitNodeInputVO(aModifyLimitNodeInputVO);
    lim.setmsgid(msgId);
   


    Gson gson = new Gson();
   
    String jsonString = gson.toJson(lim);
   
    logger.info("Json String:" + jsonString);
   
    return jsonString;
  }
 
  public static boolean insertLimitDetails(String masterRef, String eventRef, String serialNo, String limitId, String limitAmt, String limitCcy)
  {
    boolean result = true;
    int rs = 0;
    Connection con = null;
    PreparedStatement ps = null;
   
    String query = "INSERT INTO LIMITDETAILS(SNO,MASTERREF,EVENTREF,SERIALNO,LIMITID,LIMITAMT,LIMITCCY,PROCESSDATETIME) VALUES (LIMITDETAILS_SEQ.NEXTVAL,?,?,?,?,?,?,SYSDATE)";
    logger.info("insertLimitDetails : " + query);
    try
    {
      con = DBConnectionUtility.getWiseConnection();
      ps = con.prepareStatement(query);
      ps.setString(1, masterRef);
      ps.setString(2, eventRef);
      ps.setString(3, serialNo);
      ps.setString(4, limitId);
      ps.setString(5, limitAmt);
      ps.setString(6, limitCcy);
      rs = ps.executeUpdate();
      logger.info("rs :: " + rs);
      result = true;
    }
    catch (SQLException e)
    {
      logger.error("SQL Exceptions! insertLimitDetails " + e.getMessage(), e);
      e.printStackTrace();
      return false;
    }
    catch (Exception e)
    {
      logger.error("Exception! insertLimitDetails " + e.getMessage(), e);
      e.printStackTrace();
      return false;
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, ps, null);
    }
    DBConnectionUtility.surrenderDB(con, ps, null);
   

    return result;
  }
 
  public static String getRespSerialNum(String decRespJson, String umlRemarks)
  {
    String serialNumber = null;
    if ((umlRemarks == null) || (umlRemarks.isEmpty())) {
      return serialNumber;
    }
    Gson aGsonLimResp = new Gson();
    LimitBlockResp aLimitBlockResp = (LimitBlockResp)aGsonLimResp.fromJson(decRespJson, LimitBlockResp.class);
    LimitBlockRespWithObject aLimitBlockRespWithObject = null;
    try
    {
      aLimitBlockResp = (LimitBlockResp)aGsonLimResp.fromJson(decRespJson, LimitBlockResp.class);
    }
    catch (Exception e)
    {
      logger.info("inside catch block of getRespSerialNum()");
      aLimitBlockRespWithObject = (LimitBlockRespWithObject)aGsonLimResp.fromJson(decRespJson, LimitBlockRespWithObject.class);
      aLimitBlockResp = new LimitBlockResp();
      List<ResUserMaintLiabModLL> userMaintLiabModLLList = new ArrayList();
      userMaintLiabModLLList.add(aLimitBlockRespWithObject.getuserMaintLiabModLL());
      aLimitBlockResp.setuserMaintLiabModLL(userMaintLiabModLLList);
    }
    for (int j = 0; j < aLimitBlockResp.getuserMaintLiabModLL().size(); j++) {
      if ((aLimitBlockResp.getuserMaintLiabModLL() != null) && (((ResUserMaintLiabModLL)aLimitBlockResp.getuserMaintLiabModLL().get(j)).getumlRemarks() != null) &&
        (((ResUserMaintLiabModLL)aLimitBlockResp.getuserMaintLiabModLL().get(j)).getumlRemarks().equals(umlRemarks))) {
        serialNumber = ((ResUserMaintLiabModLL)aLimitBlockResp.getuserMaintLiabModLL().get(j)).getkey().getserial_num();
      }
    }
    return serialNumber;
  }
 
  public static String getLimitSerialNo(String masterRef, String limitId)
  {
    String result = null;
    ResultSet rs = null;
    Connection con = null;
    PreparedStatement ps = null;
   
    String query = "SELECT SERIALNO FROM LIMITDETAILS WHERE MASTERREF = ? AND LIMITID = ? ORDER BY SNO DESC";
    try
    {
      con = DBConnectionUtility.getWiseConnection();
      ps = con.prepareStatement(query);
      ps.setString(1, masterRef);
      ps.setString(2, limitId);
      rs = ps.executeQuery();
      if (rs.next()) {
        result = rs.getString("SERIALNO");
      }
      logger.info("getLimitSerialNo query & result --> " + query + " & " + result);
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, ps, rs);
    }
    return result;
  }
 
  public static boolean insertLogData(String service, String operation, String zone, String branch, String sourceSys, String targetSys, String masterRef, String eventRef, String status, Date valueDate, String tiRequest, String tiResponse, String bankRequest, String bankResponse, Timestamp tiReqTime, Timestamp bankReqTime, Timestamp bankResTime, Timestamp tiResTime, String transactionkey1, String statickey1, String narrative1, String narrative2, boolean isReSubmitted, String reSubmittedCount, String description)
  {
    boolean result = true;
    Connection con = null;
    PreparedStatement ps = null;
   
    String query = "INSERT INTO SERVICELOG (ID,SERVICE,OPERATION,ZONE,BRANCH,SOURCESYSTEM,TARGETSYSTEM,MASTERREFERENCE,EVENTREFERENCE,STATUS,PROCESSTIME,TIREQUEST,TIRESPONSE,BANKREQUEST,BANKRESPONSE,TIREQTIME,BANKREQTIME,BANKRESTIME,TIRESTIME,TRANSACTIONKEY1,STATICKEY1,NARRATIVE1,NARRATIVE2,ISRESUBMITTED,RESUBMITTEDCOUNT,RESUBMITTEDTIME,DESCRIPTION,TYPEFLAG,NODE,VALUEDATE) VALUES (SERVICELOG_SEQ.NEXTVAL,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    try
    {
      con = DBConnectionUtility.getWiseConnection();
      ps = con.prepareStatement(query);
     
      ps.setString(1, service);
      ps.setString(2, operation);
      ps.setString(3, zone);
      ps.setString(4, branch);
      ps.setString(5, sourceSys);
      ps.setString(6, targetSys);
      ps.setString(7, masterRef);
      ps.setString(8, eventRef);
      ps.setString(9, status);
      ps.setDate(10, null);
      ps.setString(11, tiRequest);
      ps.setString(12, tiResponse);
      ps.setString(13, bankRequest);
      ps.setString(14, bankResponse);
      ps.setTimestamp(15, tiReqTime);
      ps.setTimestamp(16, bankReqTime);
      ps.setTimestamp(17, bankResTime);
      ps.setTimestamp(18, tiResTime);
      ps.setString(19, transactionkey1);
      ps.setString(20, statickey1);
      ps.setString(21, narrative1);
      ps.setString(22, narrative2);
      ps.setBoolean(23, isReSubmitted);
      ps.setInt(24, 0);
      ps.setTimestamp(25, null);
      ps.setString(26, description);
      ps.setString(27, "");
      ps.setString(28, "");
      ps.setDate(29, valueDate);
      ps.executeUpdate();
      result = true;
    }
    catch (Exception e)
    {
      e.printStackTrace();
      result = false;
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, ps, null);
    }
    return result;
  }
 
  public static String getLimitNodeForBooking(String forwardContractNo)
  {
    logger.info("inside getLimitNodeForBooking");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    String serial = null;
    try
    {
      logger.info("Enter into getLimitNodeForBooking");
      con = DBConnectionUtility.getZoneConnection();
      String query = "SELECT LIMIT_SERIAL_NUM FROM CUSTOM_FWC_DETAILS WHERE FWC_CONTRACT_NO='" +
        forwardContractNo.trim() + "' AND CATEGORY ='FWCBOOK'";
      pst = new LoggableStatement(con, query);
      rs = pst.executeQuery();
      while (rs.next()) {
        serial = rs.getString("LIMIT_SERIAL_NUM");
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    return serial;
  }
}
