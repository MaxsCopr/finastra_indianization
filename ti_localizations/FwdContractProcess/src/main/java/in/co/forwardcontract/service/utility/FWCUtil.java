package in.co.forwardcontract.service.utility;

import in.co.forwardcontract.utility.CommonMethods;
import in.co.forwardcontract.utility.DBConnectionUtility;
import in.co.forwardcontract.vo.ForwardContractVO;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class FWCUtil
{
  private static String treasuryHDDTableName = null;
  private static Logger logger = Logger.getLogger(FWCUtil.class.getName());
 
  public static Map<String, String> insertFTIFwdContractDetails(ForwardContractVO fwdContractVO, String userId, String category)
  {
    logger.info("ForwardContractVO ---> " + fwdContractVO.toString());
    logger.info("Treasury Rate ---> " + fwdContractVO.getTreasuryRate());
    logger.info("FWD Ref Num ---> " + fwdContractVO.getFwdContractNo());
    Map<String, String> baseRecordTokens = new HashMap();
    Map<String, String> insertInFTIStatus = new HashMap();
   
    ServiceUtility.getProperties();
    treasuryHDDTableName = (String)ServiceUtility.TBProperties.get("TreasuryHDDTable");
    int count = 0;
    String sequence = "";
   
    insertInFTIStatus.put("SequenceNo", sequence);
    insertInFTIStatus.put("Count", String.valueOf(count));
    try
    {
      logger.info("Screen Type  --> " + fwdContractVO.getScreenType());
     

      String subProduct = fwdContractVO.getSubProduct().trim();
      String treasuryRefNo = fwdContractVO.getTreasuryRefNo().trim();
      String treasuryRate = fwdContractVO.getTreasuryRate().trim();
      BigDecimal treasRate = new BigDecimal(treasuryRate);
      String fwdContractNo = fwdContractVO.getFwdContractNo().trim();
     
      String customerID = fwdContractVO.getCustomerID().trim();
     
      String branch = fwdContractVO.getBranchCode().trim();
     
      String toAmountCcy = fwdContractVO.getToCurrencyAmt().trim();
      String toCcy = toAmountCcy.trim().replaceAll("[^A-Za-z]+", "");
      String toAmount = toAmountCcy.trim().replaceAll("[^0-9.]", "");
      BigDecimal toAmt = new BigDecimal(toAmount);
     
      String fwdContractAmount = fwdContractVO.getFwdContractAmt().trim();
      String dealCcy = fwdContractAmount.trim().replaceAll("[^A-Za-z]+", "");
      String dealAmount = fwdContractAmount.trim().replaceAll("[^0-9.]", "");
      BigDecimal dealAmt = new BigDecimal(dealAmount);
     
      String bookingDate = fwdContractVO.getBookingDate().trim();
      bookingDate = CommonMethods.convertToStringDateFormat(bookingDate, "dd/MM/yyyy", "yyyy-MM-dd");
     
      String dealDirection = null;
      String txnType = null;
      BigDecimal purchaseAmt = new BigDecimal(0);
      BigDecimal saleAmt = new BigDecimal(0);
      String saleCcy = null;
      String purchaseCcy = null;
     
      String selectDealCategory = null;
      String selectQueryFromTreasury = null;
      String insertQueryIntoFTI = null;
      if (subProduct.contains("Sale"))
      {
        dealDirection = "S";
        if (category.equalsIgnoreCase("FWCBOOK")) {
          txnType = "MFS";
        } else if (category.equalsIgnoreCase("FWCCANCEL")) {
          txnType = "MFSCAN";
        }
        saleAmt = dealAmt;
        saleCcy = dealCcy;
        purchaseAmt = toAmt;
        purchaseCcy = toCcy;
      }
      else if (subProduct.contains("Purchase"))
      {
        dealDirection = "B";
        if (category.equalsIgnoreCase("FWCBOOK")) {
          txnType = "MFP";
        } else if (category.equalsIgnoreCase("FWCCANCEL")) {
          txnType = "MFPCAN";
        }
        purchaseAmt = dealAmt;
        purchaseCcy = dealCcy;
        saleAmt = toAmt;
        saleCcy = toCcy;
      }
      if (category.equalsIgnoreCase("FWCBOOK")) {
        selectDealCategory = "FXRATE";
      } else if (category.equalsIgnoreCase("FWCCANCEL")) {
        selectDealCategory = "FWCCANCEL";
      }
      selectQueryFromTreasury =
     


        "SELECT TO_CHAR(HOST_TRAN_DATE,'YYYY-MM-DD') AS HOST_TRAN_DATE,TO_CHAR(START_DATE) AS START_DATE, TO_CHAR(END_DATE) AS END_DATE, TO_CHAR(VALUE_DATE,'YYYY-MM-DD') AS VALUE_DATE, DEAL_AMOUNT,DEAL_AMOUNT_CCY,FWD_CONTRACT_RATE,COUNTERPARTY_STRING,SOL_ID,ADDITIONAL_TEXT_1 AS TRAN_TYPE , BUY_OR_SELL,BUY_AMOUNT,BUY_AMOUNT_CCY,SELL_AMOUNT,SELL_AMOUNT_CCY,FWC_REF_NUM FROM " + treasuryHDDTableName + " WHERE HOST_DEAL_CATEGORY=? AND COUNTERPARTY_STRING IS NOT NULL AND RECORD_STATUS='TRANSFER' AND REFERENCE_NUM = ? ";
     
      baseRecordTokens = getBaseRecordDetailsFromHDDTable(selectDealCategory, treasuryRefNo, bookingDate,
        bookingDate, dealAmt, dealCcy, treasRate, customerID, branch, txnType, fwdContractNo, selectQueryFromTreasury);
     
      String recordStatus = (String)baseRecordTokens.get("RecordStatus");
      String recordStatusReason = (String)baseRecordTokens.get("RecordStatusReason");
      String startDate = (String)baseRecordTokens.get("StartDate");
      String endDate = (String)baseRecordTokens.get("EndDate");
     
      String panNumber = getPanNumberOfCustomer(customerID);
     
      sequence = getNextFBONumSeq();
     
      logger.info("sequence : " + sequence);
     
      java.sql.Date bookingDateSqlFormat = new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(bookingDate).getTime());
     
      logger.info("bookingDate : " + bookingDate);
     
      insertQueryIntoFTI = "INSERT INTO CUSTOM_TREASURY_INSERT_TBL( FBO_ID_TYPE, FBO_ID_NUM, FBO_ID_VER, HOST_DEAL_CATEGORY, HOST_DEAL_ACTION, HOST_TRAN_DATE, CAPTURE_TIMESTAMP, UPDATE_TIMESTAMP, RECORD_STATUS, ENTRY_USER_ID, ACCEPT_USER_ID, BUY_OR_SELL, VALUE_DATE, DEAL_AMOUNT, DEAL_AMOUNT_CCY, BUY_AMOUNT, BUY_AMOUNT_CCY, SELL_AMOUNT, SELL_AMOUNT_CCY, FWD_CONTRACT_RATE, CONTRACT_RATE, LBS_RATE, INTERNAL_RATE,REFERENCE_NUM, COUNTERPARTY_STRING,START_DATE,END_DATE, SOL_ID, BILL_ID, FWC_REF_NUM, RATE_CODE, DEAL_REF, DEAL_VERSION, ADDITIONAL_TEXT_1, ADDITIONAL_TEXT_2, ADDITIONAL_TEXT_3, ADDITIONAL_TEXT_4, CONSOLIDATE_FLAG, ENTITY_FBO_ID_NUM, ADDITIONAL_TEXT_10) values ('HDEALD', ? , 1, ?, 'ACCEPT', ?, SYSDATE, SYSDATE, ?, ?, 'SYSTEM', ?,  TO_DATE(?,'dd-MM-yy'), ?, ?, ?, ?, ?, ?,  ?, 0, 0,?, ?, ?,TO_DATE(?,'dd-MM-yy'),TO_DATE(?,'dd-MM-yy'), ?, ?, ?, 'TTB',  0, 0, ?, 'FINASTRA', ?, ?, 'Y',1,'Y') ";
      if ((category != null) && (category.equalsIgnoreCase("FWCCANCEL")))
      {
        if (baseRecordTokens.get("BUYAMOUNT") != null) {
          purchaseAmt = new BigDecimal((String)baseRecordTokens.get("BUYAMOUNT"));
        }
        if (baseRecordTokens.get("SELLAMOUNT") != null) {
          saleAmt = new BigDecimal((String)baseRecordTokens.get("SELLAMOUNT"));
        }
        purchaseCcy = (String)baseRecordTokens.get("BUYCCY");
        saleCcy = (String)baseRecordTokens.get("SELLCCY");
        dealDirection = (String)baseRecordTokens.get("BUYORSELL");
      }
      if (StringUtils.equalsIgnoreCase(recordStatus, "MATCHED")) {
        count = insertFWCDetailsInFTI(category, sequence, bookingDateSqlFormat, recordStatus, userId, dealDirection,
          endDate, dealAmt, dealCcy, purchaseAmt, purchaseCcy, saleAmt, saleCcy, treasRate, treasuryRefNo,
          customerID, startDate, branch, fwdContractNo, txnType, panNumber, recordStatusReason, "",
          insertQueryIntoFTI);
      }
      insertInFTIStatus.put("SequenceNo", sequence);
      insertInFTIStatus.put("Count", String.valueOf(count));
    }
    catch (Exception e)
    {
      logger.info("Exception in insertFTIFwdContractDetails() : " + e.getMessage());
      e.printStackTrace();
    }
    return insertInFTIStatus;
  }
 
  public static String getNextFBONumSeq()
  {
    String sourceRefSeq = "";
    ResultSet rs = null;
    Connection conn = null;
    PreparedStatement pst = null;
    try
    {
      conn = DBConnectionUtility.getZoneConnection();
      pst = conn.prepareStatement("Select FBO_ID_NUM_SEQ.nextval from dual");
      rs = pst.executeQuery();
      while (rs.next()) {
        sourceRefSeq = rs.getString("NEXTVAL");
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(conn, pst, rs);
    }
    System.out.println("FBO_ID_NUM_SEQ from getNextFBONumSeq() :----" + sourceRefSeq);
    return sourceRefSeq;
  }
 
  public static Map<String, String> getBaseRecordDetailsFromHDDTable(String dealCategory, String treasuryRefNo, String bookingDate, String bookingDate2, BigDecimal dealAmt, String dealCcy, BigDecimal treasRate, String customerID, String branch, String dealType, String forwardRefNum, String selectQueryFromTreasury)
  {
    Connection dbConnection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    String matchedOrUnmatched = "UNMATCHED";
    String matchedOrUnmatchedReason = "UNMATCHED";
    String hostTranDate = null;
    String valueDate = null;
    String buyOrSell = null;
    BigDecimal dealAmount = null;
    BigDecimal fxRate = null;
    String cifId = null;
    String solId = null;
    String tranType = null;
    Map<String, String> baseRecordTokens = new HashMap();
    String startDate = null;
    String endDate = null;
   
    String buyAmount = null;
    String sellAmount = null;
    String buyCcy = null;
    String sellCcy = null;
   
    String fwcRefNum = null;
    try
    {
      logger.info("checkMatchedOrUnmatchedRecordStatus txnFxReference -->" + treasuryRefNo);
     
      logger.info("queryDetails -->" + selectQueryFromTreasury);
      try
      {
        dbConnection = DBConnectionUtility.getDBLinkConnection();
      }
      catch (Exception e)
      {
        logger.info("db connection not established for treasuryRefNo --> " + treasuryRefNo);
        e.printStackTrace();
      }
      preparedStatement = dbConnection.prepareStatement(selectQueryFromTreasury);
      preparedStatement.setString(1, dealCategory);
      preparedStatement.setString(2, treasuryRefNo);
      resultSet = preparedStatement.executeQuery();
      while (resultSet.next())
      {
        fwcRefNum = resultSet.getString("FWC_REF_NUM");
        hostTranDate = resultSet.getString("HOST_TRAN_DATE");
        valueDate = resultSet.getString("VALUE_DATE");
       
        buyOrSell = resultSet.getString("BUY_OR_SELL");
        if (CommonMethods.isValidString(buyOrSell)) {
          if ((buyOrSell.equalsIgnoreCase("B")) || (buyOrSell.equalsIgnoreCase("P")))
          {
            dealAmount = resultSet.getBigDecimal("BUY_AMOUNT");
            dealCcy = resultSet.getString("BUY_AMOUNT_CCY");
          }
          else if (buyOrSell.equalsIgnoreCase("S"))
          {
            dealAmount = resultSet.getBigDecimal("SELL_AMOUNT");
            dealCcy = resultSet.getString("SELL_AMOUNT_CCY");
          }
        }
        fxRate = resultSet.getBigDecimal("FWD_CONTRACT_RATE");
        cifId = resultSet.getString("COUNTERPARTY_STRING");
        solId = resultSet.getString("SOL_ID");
        tranType = resultSet.getString("TRAN_TYPE");
        startDate = resultSet.getString("START_DATE");
        endDate = resultSet.getString("END_DATE");
        if (resultSet.getBigDecimal("BUY_AMOUNT") != null) {
          buyAmount = resultSet.getBigDecimal("BUY_AMOUNT").toString();
        }
        if (resultSet.getBigDecimal("SELL_AMOUNT") != null) {
          sellAmount = resultSet.getBigDecimal("SELL_AMOUNT").toString();
        }
        buyCcy = resultSet.getString("BUY_AMOUNT_CCY");
        sellCcy = resultSet.getString("SELL_AMOUNT_CCY");
       

        logger.info("Values from DBLink for fxReference --> " + treasuryRefNo);
        logger.info("fwd Ref Num -->" + forwardRefNum);
        logger.info("hostTranDate -->" + hostTranDate);
        logger.info("valueDate -->" + valueDate);
        logger.info("dealAmount -->" + dealAmount);
        logger.info("dealCcy -->" + dealCcy);
        logger.info("fxRate -->" + fxRate);
        logger.info("cifId -->" + cifId);
        logger.info("solId -->" + solId);
        logger.info("tranType -->" + tranType);
        logger.info("startDate -->" + startDate);
        logger.info("endDate -->" + endDate);
      }
      dealAmount = dealAmount.setScale(6, RoundingMode.HALF_UP);
      dealAmt = dealAmt.setScale(6, RoundingMode.HALF_UP);
      logger.info("dealAmount -->" + dealAmount);
     
      treasRate = treasRate.setScale(4, RoundingMode.HALF_UP);
      fxRate = fxRate.setScale(4, RoundingMode.HALF_UP);
      logger.info("TreasuryRate: " + treasRate + "\t" + "FxRate: " + fxRate);
      if ((StringUtils.equalsIgnoreCase(fwcRefNum, forwardRefNum)) || (StringUtils.equalsIgnoreCase(dealCategory, "FXRATE")))
      {
        logger.info("Forward Ref Num --> MATCHED");
        matchedOrUnmatched = "MATCHED";
        matchedOrUnmatchedReason = "MATCHED";
        if (hostTranDate.equalsIgnoreCase(bookingDate.toString()))
        {
          logger.info("hostTranDate --> MATCHED");
          matchedOrUnmatched = "MATCHED";
          matchedOrUnmatchedReason = "MATCHED";
          if (dealAmount.compareTo(dealAmt) == 0)
          {
            logger.info("dealAmount --> MATCHED");
            matchedOrUnmatched = "MATCHED";
            matchedOrUnmatchedReason = "MATCHED";
            if (dealCcy.equalsIgnoreCase(dealCcy))
            {
              logger.info("dealCcy --> MATCHED");
              matchedOrUnmatched = "MATCHED";
              matchedOrUnmatchedReason = "MATCHED";
              if (fxRate.compareTo(treasRate) == 0)
              {
                logger.info("fxRate --> MATCHED");
                matchedOrUnmatched = "MATCHED";
                matchedOrUnmatchedReason = "MATCHED";
                if (cifId.equalsIgnoreCase(customerID))
                {
                  logger.info("cifId --> MATCHED");
                  matchedOrUnmatched = "MATCHED";
                  matchedOrUnmatchedReason = "MATCHED";
                  if (solId.equalsIgnoreCase(branch))
                  {
                    logger.info("solId --> MATCHED");
                    matchedOrUnmatched = "MATCHED";
                    matchedOrUnmatchedReason = "MATCHED";
                    if (tranType.equalsIgnoreCase(dealType))
                    {
                      logger.info("tranType --> MATCHED");
                      matchedOrUnmatched = "MATCHED";
                      matchedOrUnmatchedReason = "MATCHED";
                    }
                    else
                    {
                      logger.info("tranType --> UNMATCHED");
                      matchedOrUnmatched = "UNMATCHED";
                      matchedOrUnmatchedReason = "Transaction Type Unmatched";
                    }
                  }
                  else
                  {
                    logger.info("solId --> UNMATCHED");
                    matchedOrUnmatched = "UNMATCHED";
                    matchedOrUnmatchedReason = "Sol ID Unmatched";
                  }
                }
                else
                {
                  logger.info("cifId --> UNMATCHED");
                  matchedOrUnmatched = "UNMATCHED";
                  matchedOrUnmatchedReason = "Customer Unmatched";
                }
              }
              else
              {
                logger.info("fxRate --> UNMATCHED");
                matchedOrUnmatched = "UNMATCHED";
                matchedOrUnmatchedReason = "Rate Unmatched";
              }
            }
            else
            {
              logger.info("dealCcy --> UNMATCHED");
              matchedOrUnmatched = "UNMATCHED";
              matchedOrUnmatchedReason = "Deal Currency Unmatched";
            }
          }
          else
          {
            logger.info("dealAmount --> UNMATCHED");
            matchedOrUnmatched = "UNMATCHED";
            matchedOrUnmatchedReason = "Deal Amount Unmatched";
          }
        }
        else
        {
          logger.info("hostTranDate --> UNMATCHED");
          matchedOrUnmatched = "UNMATCHED";
          matchedOrUnmatchedReason = "Host Tran Date Unmatched";
        }
      }
      else
      {
        logger.info("Forward Ref Num --> UNMATCHED");
        matchedOrUnmatched = "UNMATCHED";
        matchedOrUnmatchedReason = "Forward Contract Ref Num Unmatched";
      }
      logger.info("matchedOrUnmatched --> " + matchedOrUnmatched);
      logger.info("matchedOrUnmatchedReason --> " + matchedOrUnmatchedReason);
      baseRecordTokens.put("RecordStatus", matchedOrUnmatched);
      baseRecordTokens.put("RecordStatusReason", matchedOrUnmatchedReason);
      baseRecordTokens.put("StartDate", startDate);
      baseRecordTokens.put("EndDate", endDate);
     
      baseRecordTokens.put("BUYAMOUNT", buyAmount);
      baseRecordTokens.put("SELLAMOUNT", sellAmount);
      baseRecordTokens.put("BUYCCY", buyCcy);
      baseRecordTokens.put("SELLCCY", sellCcy);
      baseRecordTokens.put("BUYORSELL", buyOrSell);
    }
    catch (SQLException e)
    {
      matchedOrUnmatched = "UNMATCHED";
      e.printStackTrace();
      logger.info(e.getMessage());
    }
    finally
    {
      DBConnectionUtility.surrenderDB(dbConnection, preparedStatement, resultSet);
    }
    return baseRecordTokens;
  }
 
  private static String getPanNumberOfCustomer(String customer)
  {
    ResultSet resultSet = null;
    Connection tiZoneConnection = null;
    PreparedStatement preparedStatement = null;
    String panNumberOfCustomerQuery = "";
    String panNumber = "";
    try
    {
      panNumberOfCustomerQuery = " SELECT PANNO FROM EXTCUST WHERE TRIM(CUST)=? ";
     
      logger.info("panNumOfCustomerQuery : " + panNumberOfCustomerQuery + "; Param[" + customer + "]");
     
      tiZoneConnection = DBConnectionUtility.getZoneConnection();
      preparedStatement = tiZoneConnection.prepareStatement(panNumberOfCustomerQuery);
     
      preparedStatement.setString(1, customer.trim());
     
      resultSet = preparedStatement.executeQuery();
      while (resultSet.next())
      {
        if (CommonMethods.isValidString(resultSet.getString("PANNO"))) {
          panNumber = resultSet.getString("PANNO");
        }
        logger.info("panNumber --> " + panNumber);
      }
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(tiZoneConnection, preparedStatement, resultSet);
    }
    return panNumber;
  }
 
  public static String generateFWCReferenceNumber(String branchCode, String subProduct)
  {
    String fwdContractSeqNo = null;
    String productType = null;
    Connection aConnection = null;
    PreparedStatement aPreparedStatement = null;
    ResultSet aResultSet = null;
    String fwcSeqNo = "";
    try
    {
      aConnection = DBConnectionUtility.getZoneConnection();
      aPreparedStatement = aConnection.prepareStatement("SELECT LPAD(FWC_SEQ.NEXTVAL,5,'0') FROM DUAL");
      aResultSet = aPreparedStatement.executeQuery();
      if (aResultSet.next())
      {
        fwcSeqNo = aResultSet.getString(1);
        logger.info("Seq number form DB " + fwcSeqNo);
      }
      if (subProduct.contains("Purchase")) {
        productType = "MP";
      } else if (subProduct.contains("Sale")) {
        productType = "MS";
      }
      fwdContractSeqNo =
        branchCode + productType + new SimpleDateFormat("yy").format(new java.util.Date()) + fwcSeqNo;
     
      logger.info("Generated FWC number is " + fwdContractSeqNo);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(aConnection, aPreparedStatement, aResultSet);
    }
    return fwdContractSeqNo;
  }
 
  public static int insertFWCDetailsInFTI(String dealCategory, String sequence, java.sql.Date txnBusinessDate, String recordStatus, String userId, String dealDirection, String endDate, BigDecimal dealAmt, String dealCcy, BigDecimal purchaseAmount, String purchaseCcy, BigDecimal saleAmount, String saleCcy, BigDecimal treasuryRate, String treasuryRefNo, String txnCustomer, String startDate, String txnBranch, String fwdContractNo, String txnDealType, String panNumber, String recordStatusReason, String nostroAcct, String queryDetails)
  {
    int count = 0;
    Connection dbConnection = null;
    PreparedStatement preparedStatement = null;
    try
    {
      dbConnection = DBConnectionUtility.getZoneConnection();
      if (dbConnection != null)
      {
        preparedStatement = dbConnection.prepareStatement(queryDetails);
       
        preparedStatement.setInt(1, Integer.parseInt(sequence));
        preparedStatement.setString(2, dealCategory);
        preparedStatement.setDate(3, txnBusinessDate);
        preparedStatement.setString(4, recordStatus);
        preparedStatement.setString(5, userId);
        preparedStatement.setString(6, dealDirection);
        preparedStatement.setString(7, endDate);
        preparedStatement.setBigDecimal(8, dealAmt);
        preparedStatement.setString(9, dealCcy);
        preparedStatement.setBigDecimal(10, purchaseAmount);
        preparedStatement.setString(11, purchaseCcy);
        preparedStatement.setBigDecimal(12, saleAmount);
        preparedStatement.setString(13, saleCcy);
        preparedStatement.setBigDecimal(14, treasuryRate);
        preparedStatement.setBigDecimal(15, treasuryRate);
        preparedStatement.setString(16, treasuryRefNo);
        preparedStatement.setString(17, txnCustomer);
        preparedStatement.setString(18, startDate);
        preparedStatement.setString(19, endDate);
        preparedStatement.setString(20, txnBranch);
        preparedStatement.setString(21, "");
        preparedStatement.setString(22, fwdContractNo);
        preparedStatement.setString(23, txnDealType);
        preparedStatement.setString(24, panNumber);
        preparedStatement.setString(25, recordStatusReason);
       
        preparedStatement.executeUpdate();
       
        count = preparedStatement.getUpdateCount();
       
        logger.info("Inserted Utlization Details into FTI Table successfully with the count: " + count + " for " +
          fwdContractNo);
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(dbConnection, preparedStatement, null);
    }
    return count;
  }
 
  public static void insertUtilizationDetailsInTreasury(String txnReference, String finacleTranId, String category, String seqNo)
  {
    Connection dbConnection = null;
    CallableStatement callableStatement = null;
    try
    {
      dbConnection = DBConnectionUtility.getZoneConnection();
      if (dbConnection != null)
      {
        callableStatement = dbConnection.prepareCall("{CALL CUSTOM_TREASURY_FWC_INSERT_PROC(?,?,?,?)}");
       
        callableStatement.setString(1, txnReference);
        callableStatement.setString(2, txnReference + " " + finacleTranId);
        callableStatement.setString(3, category);
        callableStatement.setString(4, seqNo);
        callableStatement.execute();
       
        logger.info("Inserted Utlization Details into Treasury Table successfully for " + txnReference +
          " under category " + category);
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(dbConnection, callableStatement, null);
    }
  }
}