package in.co.forwardcontract.service.utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import in.co.forwardcontract.dao.ForwardContractDAO;
import in.co.forwardcontract.service.model.AmtAndCcy;
import in.co.forwardcontract.service.model.PostingReq;
import in.co.forwardcontract.service.model.PostingReqAcc;
import in.co.forwardcontract.service.model.PostingReqCount;
import in.co.forwardcontract.service.model.PostingReqCustomData;
import in.co.forwardcontract.service.model.PostingReqPartTrnRec;
import in.co.forwardcontract.service.model.PostingReqPmtInst;
import in.co.forwardcontract.service.model.PostingReqTranPart;
import in.co.forwardcontract.service.model.PostingReqTrnAddRequest;
import in.co.forwardcontract.service.model.PostingReqTrnAddRq;
import in.co.forwardcontract.service.model.PostingReqTrnDetail;
import in.co.forwardcontract.service.model.PostingReqTrnHdr;
import in.co.forwardcontract.service.model.PostingRes;
import in.co.forwardcontract.utility.CommonMethods;
import in.co.forwardcontract.utility.DBConnectionUtility;
import in.co.forwardcontract.utility.ServiceLogging;
import in.co.forwardcontract.vo.FWCPostingVO;
import in.co.forwardcontract.vo.ForwardContractVO;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

public class PostingUtil
{
  private static Logger logger = Logger.getLogger(PostingUtil.class.getName());
  private static String fwdContractNo = null;
  static String tempEnc = "";
  static PostingRes postingRes = new PostingRes();
  static Gson aGson = new GsonBuilder().disableHtmlEscaping().create();
  static String encBankRes;
  static String dealCategory = "";
  static String sequenceNo = "";
 
  public static Map<String, String> releaseTxnPostings(String category, ForwardContractVO fwdContractVO, String seqNo)
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
    String postingURL = (String)ServiceUtility.TBProperties.get("BO_BATCH_URL");
    String postingKey = (String)ServiceUtility.TBProperties.get("BO_BATCH_KEY");
    Map<String, String> responseTokens = null;
    dealCategory = category;
    sequenceNo = seqNo;
    try
    {
      logger.info("postingURL & postingKey  --> " + postingURL + " & " + postingKey);
     
      Map<String, String> result = generatePostingBankRequest(category, fwdContractVO);
      plainBankRequest = (String)result.get("JSON");
      logger.info("Posting Bank Request in Json Format -->" + plainBankRequest);
     

      tempEncRequest = ServiceUtility.generateEncryptBankRequest(plainBankRequest, postingKey);
      encBankRequest = generateEncryptedPostingJson(tempEncRequest, (String)result.get("MSGID"));
     
      logger.info("Posting Bank Enc Request -->" + encBankRequest);
      bankRequestTime = CommonMethods.getSqlLocalDateTime();
      encBankResponse = ServiceUtility.getBankFinResponse(encBankRequest, postingURL);
      bankResponseTime = CommonMethods.getSqlLocalDateTime();
      plainBankResponse = ServiceUtility.generateDecryptBankResponse(encBankResponse, postingKey);
      logger.info("Posting Bank Json Response -->" + plainBankResponse);
      if ((plainBankResponse != null) && (!plainBankResponse.isEmpty()) && (!plainBankResponse.equals("{}")))
      {
        responseTokens = getPostingResponseTokens(plainBankResponse);
        if ((responseTokens != null) && (((String)responseTokens.get("PostingStatus")).contains("S"))) {
          status = "SUCCEEDED";
        } else {
          status = "FAILED";
        }
      }
      else
      {
        status = "FAILED";
      }
      ServiceLogging.pushServiceLogData("Batch", "Posting", "ZONE1", "FTI", "Finacle", fwdContractVO.getFwdContractNo(), category, status,
        plainBankRequest, plainBankResponse, bankRequestTime, bankResponseTime);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return responseTokens;
  }
 
  public static Map<String, String> generatePostingBankRequest(String category, ForwardContractVO fwdContractVO)
  {
    String bankRequest = null;
    int creditCount = 0;
    int debitCount = 0;
    int serialNo = 1;
    Map<String, String> result = new HashMap();
    List<PostingReqPartTrnRec> trnRecList = new LinkedList();
    List<PostingReqTranPart> trnPartList = new LinkedList();
    PostingReq postingReq = new PostingReq();
    ForwardContractDAO fwdcontractdao = new ForwardContractDAO();
    PostingReqTrnHdr aPostingReqTrnHdr = new PostingReqTrnHdr();
    PostingReqCount aPostingReqCount = new PostingReqCount();
    PostingReqTrnDetail aPostingReqTrnDetail = new PostingReqTrnDetail();
    PostingReqTrnAddRequest aPostingReqTrnAddRequest = new PostingReqTrnAddRequest();
    PostingReqCustomData aPostingReqCustomData = new PostingReqCustomData();
    PostingReqTrnAddRq aPostingReqTrnAddRq = new PostingReqTrnAddRq();
    String sequence = null;
    try
    {
      logger.info("Fwd Contract No : " + fwdContractVO.getFwdContractNo());
     
      fwdContractNo = fwdContractVO.getFwdContractNo().trim();
      String customerID = fwdContractVO.getCustomerID().trim();
      String postingBranch = fwdContractVO.getBranchCode();
     
      int postingCount = fwdContractVO.getPostingList().size();
      if (postingCount > 0)
      {
        for (int i = 0; i < postingCount; i++)
        {
          String forwardContractNo = fwdContractVO.getFwdContractNo();
         
          String postingAmountCcy = ((FWCPostingVO)fwdContractVO.getPostingList().get(i)).getPostingAmountCcy();
          String postingType = ((FWCPostingVO)fwdContractVO.getPostingList().get(i)).getPostingType();
          String postingCcy = postingAmountCcy.trim().replaceAll("[^A-Za-z]+", "");
          String postingAmount = postingAmountCcy.trim().replaceAll("[^0-9.]", "");
          if ((category != null) && (category.equalsIgnoreCase("FWCCANCEL")))
          {
            logger.info("postingAmount in generatePostingBankRequest for FWCCANCEL --> " + postingAmount);
            String bookingrate = "";
            if ((postingType != null) && (!"Charges".equalsIgnoreCase(postingType)))
            {
              bookingrate = fwdcontractdao.getBookingTreasuryrate(forwardContractNo);
              postingAmount = new BigDecimal(postingAmount).multiply(new BigDecimal(bookingrate)).setScale(4,
                RoundingMode.HALF_UP).toString();
            }
            logger.info("postingAmount*bookingrate in generatePostingBankRequest for FWCCANCEL --> " + postingAmount);
          }
          String postingAcctNo = ((FWCPostingVO)fwdContractVO.getPostingList().get(i)).getPostingAcctNumber();
          String postingDrCrFlag = ((FWCPostingVO)fwdContractVO.getPostingList().get(i)).getPostingDrCrFlag();
          String valueDate = ((FWCPostingVO)fwdContractVO.getPostingList().get(i)).getPostingValueDate();
         
          logger.info("TI posting value date --> " + valueDate);
          valueDate = CommonMethods.convertToStringDateFormat(valueDate, "dd-MM-yyyy", "yyyy-MM-dd");
         
          String postingDesc = ((FWCPostingVO)fwdContractVO.getPostingList().get(i)).getPostingDesc();
          if ((CommonMethods.isValidString(postingDrCrFlag)) && (postingDrCrFlag.equalsIgnoreCase("C"))) {
            creditCount++;
          } else if ((CommonMethods.isValidString(postingDrCrFlag)) && (postingDrCrFlag.equalsIgnoreCase("D"))) {
            debitCount++;
          }
          PostingReqPartTrnRec aPostingReqPartTrnRec = new PostingReqPartTrnRec();
         
          PostingReqAcc aPostingReqAcc = new PostingReqAcc();
          aPostingReqAcc.setAcctId(postingAcctNo);
         
          aPostingReqPartTrnRec.setAcctId(aPostingReqAcc);
          aPostingReqPartTrnRec.setCreditDebitFlg(postingDrCrFlag);
         
          AmtAndCcy aAmtAndCcy = new AmtAndCcy();
         
          aAmtAndCcy.setAmountValue(postingAmount);
          aAmtAndCcy.setCurrencyCode(postingCcy);
         
          aPostingReqPartTrnRec.setTrnAmt(aAmtAndCcy);
          aPostingReqPartTrnRec.setTrnParticulars(fwdContractNo);
          aPostingReqPartTrnRec.setValueDt(valueDate + "T" + CommonMethods.getH24Time());
          aPostingReqPartTrnRec.setSerialNum(String.valueOf(serialNo));
         
          PostingReqPmtInst aPostingReqPmtInst = new PostingReqPmtInst();
          aPostingReqPmtInst.setPmtInstType("DV");
         
          aPostingReqPartTrnRec.setPmtInst(aPostingReqPmtInst);
         
          trnRecList.add(aPostingReqPartTrnRec);
          String reptcode = fetchReptCODE(fwdContractNo);
          if (reptcode == null) {
            reptcode = "";
          }
          PostingReqTranPart aPostingReqTranPart = new PostingReqTranPart();
          aPostingReqTranPart.setSERIAL_NUM(String.valueOf(serialNo));
          aPostingReqTranPart.setPARTTNDTLENTERED("1");
          aPostingReqTranPart.setENTITYDISPID(fwdContractNo);
          aPostingReqTranPart.setENTITY_TYPE("FWC");
          aPostingReqTranPart.setTRANRMKS(fwdContractNo);
          aPostingReqTranPart.setTRANPARTICULARS2(fwdContractNo + " " + category);
          aPostingReqTranPart.setREFNO(fwdContractNo);
          aPostingReqTranPart.setREPTCODE(reptcode);
         
          trnPartList.add(aPostingReqTranPart);
         
          pushCustomFWCPosting(category, fwdContractNo, customerID, postingDrCrFlag, postingAcctNo,
            postingBranch, postingAmount, postingCcy, valueDate, postingDesc, serialNo);
         
          serialNo++;
        }
        aPostingReqTrnHdr.setTrnSubType("BI");
        aPostingReqTrnHdr.setTrnType("T");
       
        aPostingReqCount.setCreditCount(String.valueOf(creditCount));
        aPostingReqCount.setDebitCount(String.valueOf(debitCount));
        aPostingReqCount.setTotalXferCount(String.valueOf(creditCount + debitCount));
       
        aPostingReqTrnDetail.setPartTrnRec(trnRecList);
       
        aPostingReqTrnAddRq.setXferTrnHdr(aPostingReqTrnHdr);
        aPostingReqTrnAddRq.setXferCount(aPostingReqCount);
        aPostingReqTrnAddRq.setXferTrnDetail(aPostingReqTrnDetail);
       
        aPostingReqCustomData.setSolid(postingBranch);
        aPostingReqCustomData.setTRANPART(trnPartList);
       
        aPostingReqTrnAddRequest.setXferTrnAdd_CustomData(aPostingReqCustomData);
        aPostingReqTrnAddRequest.setXferTrnAddRq(aPostingReqTrnAddRq);
       
        sequence = ServiceUtility.getSqlLocalDateTime().toString();
        sequence = sequence.replaceAll("[- :.]", "");
       
        postingReq.setXferTrnAddRequest(aPostingReqTrnAddRequest);
        postingReq.setMsgid(sequence);
       
        bankRequest = aGson.toJson(postingReq).trim();
        logger.info("bankRequest of FWC posting: " + bankRequest);
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    result.put("JSON", bankRequest);
    result.put("MSGID", sequence);
    return result;
  }
 
  public static String generateEncryptedPostingJson(String bankEncRequest, String msgId)
  {
    ServiceUtility encryptedReq = new ServiceUtility(bankEncRequest, msgId);
    String reqJson = aGson.toJson(encryptedReq).trim();
    return reqJson;
  }
 
  public static String fetchReptCODE(String masterRef)
  {
    logger.info("fetchReptCODE() : Started");
    ResultSet resultSet = null;
    Connection tiZoneConnection = null;
    PreparedStatement preparedStatement = null;
    String result = "";
    try
    {
      String query = "SELECT exte.RPTCODE AS RPTCODE  FROM master mas, baseevent bev, extevent exte where mas.KEY97 = bev.MASTER_KEY and bev.KEY97 = exte.EVENT and mas.MASTER_REF IN ('" +
        masterRef.trim() + "')";
      logger.info("query :" + query);
      tiZoneConnection = DBConnectionUtility.getZoneConnection();
      preparedStatement = tiZoneConnection.prepareStatement(query);
      resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        result = resultSet.getString("RPTCODE");
      }
    }
    catch (Exception e)
    {
      logger.info(" fetchReptCODE() Exception" + e.getMessage());
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(tiZoneConnection, preparedStatement, resultSet);
    }
    logger.info("fetchReptCODE() : result :" + result);
    return result;
  }
 
  public static Map<String, String> getPostingResponseTokens(String plainBankResponse)
  {
    Map<String, String> postingTokens = new HashMap();
    logger.info("Entering getPostingResponseTokens ");
    postingTokens.put("PostingStatus", "FAILURE");
    String tranID = "";
    String trandate = "";
    try
    {
      postingRes = (PostingRes)aGson.fromJson(plainBankResponse, PostingRes.class);
      if (!CommonMethods.isNull(postingRes.getStatus()))
      {
        String status = postingRes.getStatus();
       
        postingTokens.put("PostingStatus", status);
        if (status.equalsIgnoreCase("SUCCESS"))
        {
          tranID = postingRes.getTransactionId();
         
          trandate = postingRes.getTransactionDt();
          if (CommonMethods.isValidString(tranID))
          {
            logger.info("FWC Posting SUCCEEDED with " + tranID);
            FWCUtil.insertUtilizationDetailsInTreasury(fwdContractNo.trim(), tranID.trim(), dealCategory, sequenceNo);
          }
          if (CommonMethods.isValidString(trandate))
          {
            logger.info("FWC Posting SUCCEEDED with " + trandate);
            FWCUtil.insertUtilizationDetailsInTreasury(fwdContractNo.trim(), trandate.trim(), dealCategory, sequenceNo);
          }
        }
        else
        {
          postingTokens.put("PostingStatus", "FAILURE");
        }
        if ((CommonMethods.isValidString(tranID)) && (CommonMethods.isValidString(trandate)))
        {
          postingTokens.put("TranID", tranID);
          postingTokens.put("Trandate", trandate);
        }
        else
        {
          postingTokens.put("PostingStatus", "FAILURE");
        }
      }
      else
      {
        postingTokens.put("PostingStatus", "FAILURE");
      }
      logger.info("Exiting getPostingResponseTokens ");
    }
    catch (Exception e)
    {
      logger.info("Exception in getPostingResponseTokens " + e.getMessage());
      e.printStackTrace();
    }
    return postingTokens;
  }
 
  public static void pushCustomFWCPosting(String category, String fwdContractNo, String customerID, String postingDrCrFlag, String postingAcctNo, String postingBranch, String postingAmount, String postingCcy, String valueDate, String postingDesc, int serialNo)
  {
    logger.info("Process entered into Push Custom FWC Posting...!");
   
    String query = "INSERT INTO CUSTOM_FWC_POSTING(CATEGORY,FWC_REFERENCE,CUSTOMER_ID,DR_CR_FLAG,POSTING_ACCT_NO,POSTING_BRANCH,POSTING_AMOUNT,POSTING_CCY,VALUE_DATE, POSTING_DESC,POSTING_SEQNO, POSTING_TIMESTAMP) VALUES (?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?,SYSTIMESTAMP) ";
   
    Connection wiseConnec = null;
   
    PreparedStatement pstmt = null;
    try
    {
      wiseConnec = DBConnectionUtility.getZoneConnection();
      pstmt = wiseConnec.prepareStatement(query);
     
      pstmt.setString(1, category);
      pstmt.setString(2, fwdContractNo);
      pstmt.setString(3, customerID);
      pstmt.setString(4, postingDrCrFlag);
      pstmt.setString(5, postingAcctNo);
      pstmt.setString(6, postingBranch);
      pstmt.setString(7, postingAmount);
      pstmt.setString(8, postingCcy);
      pstmt.setString(9, valueDate);
      pstmt.setString(10, postingDesc);
      pstmt.setString(11, Integer.toString(serialNo));
     
      pstmt.executeUpdate();
     
      logger.info("FWC Postings are added successfully with count: " + pstmt.getUpdateCount());
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      try
      {
        pstmt.close();
        wiseConnec.close();
      }
      catch (SQLException sqlEx)
      {
        sqlEx.printStackTrace();
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      try
      {
        pstmt.close();
        wiseConnec.close();
      }
      catch (SQLException sqlEx)
      {
        sqlEx.printStackTrace();
      }
    }
    finally
    {
      try
      {
        pstmt.close();
        wiseConnec.close();
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
  }
}
