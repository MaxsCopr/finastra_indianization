package in.co.forwardcontract.dao;

import com.opensymphony.xwork2.ActionContext;
import in.co.forwardcontract.dao.exception.DAOException;
import in.co.forwardcontract.service.model.DateTimeUtil;
import in.co.forwardcontract.service.utility.AvailBalAuthCheckUtility;
import in.co.forwardcontract.service.utility.FWCUtil;
import in.co.forwardcontract.service.utility.FtrtSelectUtil;
import in.co.forwardcontract.service.utility.FtrtUpdateUtil;
import in.co.forwardcontract.service.utility.LimitBlockUnblockUtil;
import in.co.forwardcontract.service.utility.LimitFetchUtil;
import in.co.forwardcontract.service.utility.PostingUtil;
import in.co.forwardcontract.service.utility.ServiceUtility;
import in.co.forwardcontract.service.utility.TreasUpdateUtil;
import in.co.forwardcontract.utility.ActionConstantsQuery;
import in.co.forwardcontract.utility.CommonMethods;
import in.co.forwardcontract.utility.DBConnectionUtility;
import in.co.forwardcontract.utility.LoggableStatement;
import in.co.forwardcontract.vo.AlertMessagesVO;
import in.co.forwardcontract.vo.FWCPostingVO;
import in.co.forwardcontract.vo.ForwardContractVO;
import in.co.forwardcontract.vo.StaticDataVO;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

public class ForwardContractDAO
  implements ActionConstantsQuery
{
  private static final String DELETE = null;
  private static String treasuryHDDTableName = null;
  static ForwardContractDAO dao;
  private static Logger logger = Logger.getLogger(ForwardContractDAO.class.getName());
  private ArrayList<AlertMessagesVO> alertMsgArray = new ArrayList();
 
  public static ForwardContractDAO getDAO()
  {
    if (dao == null) {
      dao = new ForwardContractDAO();
    }
    return dao;
  }
 
  public ForwardContractVO fetchDependentTreasuryDetails(ForwardContractVO fwdContractVO)
  {
    try
    {
      String treRefNo = fwdContractVO.getTreasuryRefNo().trim();
      String cifID = fwdContractVO.getCustomerID().trim();
      String accNo = fwdContractVO.getAcctNumber();
      String msgId = DateTimeUtil.getSqlLocalDateTime().toString();
      msgId = msgId.replaceAll("[- :.]", "");
     






      logger.info("treRefNo & cifID  & Acc Num -->" + treRefNo + " & " + cifID + " & " + accNo);
      HashMap map = checkDealUtilization(treRefNo, cifID, fwdContractVO.getFwdContractNo());
      String flag = map.get("errormsg").toString();
     



      Map<String, String> fxOptionTokens = FtrtSelectUtil.getRateDetailsFromFtrtAPI(treRefNo, cifID);
      String rateStatus = ((String)fxOptionTokens.get("FtrtSelectStatus")).trim();
      fwdContractVO.setRateStatus(rateStatus);
      if (rateStatus.equalsIgnoreCase("S"))
      {
        String treasuryRefNo = (String)fxOptionTokens.get("TrRefNum");
        String customerID = (String)fxOptionTokens.get("CifId");
        String treasuryRate = (String)fxOptionTokens.get("TreasuryRate");
        String fromCrncyCode = (String)fxOptionTokens.get("FromCrncyCode");
        String refAmount = (String)fxOptionTokens.get("RefAmt");
        String utilizedAmount = null;
        String toCrncyCode = (String)fxOptionTokens.get("ToCrncyCode");
        String buyOrSell = (String)fxOptionTokens.get("BuyOrSell");
        String branchCode = (String)fxOptionTokens.get("FreeCode1");
       


        logger.info(" refAmount --> " + refAmount);
        if (CommonMethods.isValidString((String)fxOptionTokens.get("UtilizedAmt"))) {
          utilizedAmount = (String)fxOptionTokens.get("UtilizedAmt");
        } else {
          utilizedAmount = new BigDecimal(0).toString();
        }
        logger.info("utilizedAmount" + utilizedAmount);
       
        BigDecimal availableAmount = new BigDecimal((String)fxOptionTokens.get("RefAmt"))
          .subtract(new BigDecimal(utilizedAmount));
        logger.info("availableAmount" + availableAmount);
        logger.info("refAmount" + refAmount);
       
        int compareResult = availableAmount.compareTo(new BigDecimal(refAmount));
       
        logger.info("compare result:" + compareResult);
        if (compareResult != -1)
        {
          fwdContractVO.setCustomerID(customerID);
          fwdContractVO.setTreasuryRefNo(treasuryRefNo);
          fwdContractVO.setTreasuryRate(treasuryRate);
          fwdContractVO.setOutstandingAmt(availableAmount + " " + fromCrncyCode);
          fwdContractVO.setFwdContractAmt(refAmount + " " + fromCrncyCode);
          fwdContractVO.setDealCurrency(fromCrncyCode);
          fwdContractVO.setBranchCode(branchCode);
         




          BigDecimal toAmount = new BigDecimal(0);
          if ((CommonMethods.isValidString(toCrncyCode)) && (CommonMethods.isValidString(treasuryRate)))
          {
            toAmount = new BigDecimal(refAmount).multiply(new BigDecimal(treasuryRate));
            logger.info(" treasuryRate --> " + treasuryRate);
          }
          toAmount = toAmount.setScale(2, RoundingMode.HALF_UP);
          String toCcyAmount = toAmount + " " + toCrncyCode;
          logger.info(" toAmount &&  toCrncyCode --> " + toCcyAmount);
          fwdContractVO.setToCurrencyAmt(toCcyAmount);
          fwdContractVO.setRateBuyOrSell(buyOrSell);
        }
        else
        {
          fwdContractVO.setRateStatus("NoBal");
        }
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    logger.info("Exiting Method");
    return fwdContractVO;
  }
 
  public HashMap checkDealUtilization(String treRefNo, String cifID, String forwardnum)
  {
    String errormsg = "Y";
    String fwcnum = "";
    String query = null;
    ResultSet resultSet = null;
    HashMap<String, String> map = new HashMap();
    Connection tiZoneConnection = null;
    PreparedStatement preparedStatement = null;
    try
    {
      query = "SELECT * FROM CUSTOM_FWC_DETAILS WHERE TREASURY_REF_NO =? AND CIF_ID=?";
     
      tiZoneConnection = DBConnectionUtility.getZoneConnection();
      preparedStatement = tiZoneConnection.prepareStatement(query);
      preparedStatement.setString(1, treRefNo);
      preparedStatement.setString(2, cifID);
      resultSet = preparedStatement.executeQuery();
      while (resultSet.next())
      {
        if ((resultSet.getString("STATUS") != null) && (
          (!"REJECTED".equalsIgnoreCase(resultSet.getString("STATUS"))) || (("REJECTED".equalsIgnoreCase(resultSet.getString("STATUS"))) &&
          (!forwardnum.equalsIgnoreCase(resultSet.getString("FWC_CONTRACT_NO"))))))
        {
          errormsg = "AU";
          fwcnum = resultSet.getString("FWC_CONTRACT_NO") != null ? resultSet.getString("FWC_CONTRACT_NO") :
            "";
          break;
        }
        if ((resultSet.getString("STATUS") != null) &&
          ("REJECTED".equalsIgnoreCase(resultSet.getString("STATUS"))))
        {
          String date1 = resultSet.getString("BOOKING_DATE");
          logger.info("BOOKING_DATE date1 : " + date1);
          if (date1.length() > 10) {
            date1 = date1.substring(0, 10).replace('-', '/');
          } else {
            date1 = "20" + date1.substring(6) + "/" + date1.substring(3, 5) + "/" + date1.substring(0, 2);
          }
          logger.info("checkDealUtilization date1 : " + date1);
          String date2 = getTICurrentDateFormat();
          logger.info("checkDealUtilization date2 : " + date2);
          if ((date1 != null) && (date2 != null) && (date1.compareTo(date2) != 0))
          {
            errormsg = "AR";
            fwcnum = resultSet.getString("FWC_CONTRACT_NO") != null ? resultSet.getString("FWC_CONTRACT_NO") :
              "";
          }
        }
      }
      logger.info("checkDealUtilization result : " + errormsg);
      map.put("errormsg", errormsg);
      map.put("fwcnum", fwcnum);
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(tiZoneConnection, preparedStatement, resultSet);
    }
    return map;
  }
 
  public ForwardContractVO fetchDependentCancelTreasuryDetails(ForwardContractVO fwdContractVO)
  {
    try
    {
      logger.info("Enter into fetch DependantCancel ");
     
      String treRefNo = fwdContractVO.getTreasuryRefNo().trim();
      String cifID = fwdContractVO.getCustomerID().trim();
     
      logger.info("treRefNo & cifID -->" + treRefNo + " & " + cifID);
     
      Map<String, String> fxOptionTokens = FtrtSelectUtil.getRateDetailsFromFtrtAPI(treRefNo, cifID);
     
      String rateStatus = ((String)fxOptionTokens.get("FtrtSelectStatus")).trim();
      fwdContractVO.setRateStatus(rateStatus);
      String fwdcontractnum = fwdContractVO.getFwdContractNo();
     

      String buyorsell = getBuyorSell(fwdcontractnum);
      logger.info("Forward Contract Number:" + fwdcontractnum);
      String fwdcontrbal = null;
      logger.info("buyorsell:" + buyorsell);
      if (buyorsell.equalsIgnoreCase("S")) {
        fwdcontrbal = getFwContractBalsell(fwdContractVO.getFwdContractNo(), cifID);
      } else if (buyorsell.equalsIgnoreCase("B")) {
        fwdcontrbal = getFwContractBalbuy(fwdContractVO.getFwdContractNo(), cifID);
      }
      logger.info("Forward Contract Balance:" + fwdcontrbal);
     
      String bookingrate = getBookingTreasuryrate(fwdContractVO.getFwdContractNo());
      if (rateStatus.equalsIgnoreCase("S"))
      {
        String treasuryRefNo = (String)fxOptionTokens.get("TrRefNum");
        String customerID = (String)fxOptionTokens.get("CifId");
        String treasuryRate = (String)fxOptionTokens.get("TreasuryRate");
        String fromCrncyCode = (String)fxOptionTokens.get("FromCrncyCode");
        String refAmount = (String)fxOptionTokens.get("RefAmt");
        String utilizedAmount = null;
        String toCrncyCode = (String)fxOptionTokens.get("ToCrncyCode");
        String buyOrSell = (String)fxOptionTokens.get("BuyOrSell");
        String branchCode = (String)fxOptionTokens.get("FreeCode1");
       


        logger.info(" refAmount --> " + refAmount);
        if (CommonMethods.isValidString((String)fxOptionTokens.get("UtilizedAmt"))) {
          utilizedAmount = (String)fxOptionTokens.get("UtilizedAmt");
        } else {
          utilizedAmount = new BigDecimal(0).toString();
        }
        logger.info("utilizedAmount" + utilizedAmount);
       
        BigDecimal availableAmount = new BigDecimal((String)fxOptionTokens.get("RefAmt"))
          .subtract(new BigDecimal(utilizedAmount));
        logger.info("availableAmount" + availableAmount);
        logger.info("refAmount" + refAmount);
       
        int compareResult = availableAmount.compareTo(new BigDecimal(refAmount));
       
        logger.info("compare result:" + compareResult);
        if (compareResult != -1)
        {
          fwdContractVO.setCustomerID(customerID);
          fwdContractVO.setTreasuryRefNo(treasuryRefNo);
          fwdContractVO.setTreasuryRate(treasuryRate);
         



          fwdContractVO.setOutstandingAmt(availableAmount + " " + fromCrncyCode);
          fwdContractVO.setFwdContractAmt(refAmount + " " + fromCrncyCode);
          fwdContractVO.setDealCurrency(fromCrncyCode);
          fwdContractVO.setBranchCode(branchCode);
          fwdContractVO.setCancellationamount(fwdcontrbal + " " + fromCrncyCode);
          fwdContractVO.setBookingrate(bookingrate);
         
          BigDecimal PLAmt = new BigDecimal(0);
          BigDecimal PLAmtBeforeConvertion = new BigDecimal(0);
          BigDecimal cancelamt = availableAmount;
          BigDecimal bookingamount = new BigDecimal(refAmount);
          BigDecimal bookingtreasrate = new BigDecimal(bookingrate);
          logger.info("bookingamount" + bookingamount);
          BigDecimal cancelrate = new BigDecimal(treasuryRate);
          String fxRateOnCurrency = "";
          String cancelAmtTres = "";
          if (buyorsell.equalsIgnoreCase("S"))
          {
            BigDecimal rateDiff = new BigDecimal(0);
            rateDiff = bookingtreasrate.subtract(cancelrate);
            logger.info("bookingtreasrate " + bookingtreasrate + " cancelrate " + cancelrate + " rateDiff " + rateDiff);
            cancelAmtTres = getBuyOrSellAmount(fwdcontractnum, treRefNo, buyOrSell);
            BigDecimal cancellationamtFrmTreasury = new BigDecimal(cancelAmtTres);
            PLAmtBeforeConvertion = cancellationamtFrmTreasury.multiply(rateDiff);
            logger.info("PLAmtBeforeConvertion" + PLAmtBeforeConvertion);
            if (PLAmtBeforeConvertion.compareTo(BigDecimal.ZERO) > 0) {
              fxRateOnCurrency = getRateForConversion(toCrncyCode, "B");
            } else {
              fxRateOnCurrency = getRateForConversion(toCrncyCode, "S");
            }
            BigDecimal fxRateOnCurrencyFetched = new BigDecimal(fxRateOnCurrency);
            PLAmt = PLAmtBeforeConvertion.multiply(fxRateOnCurrencyFetched);
          }
          else if (buyorsell.equalsIgnoreCase("B"))
          {
            BigDecimal rateDiff = new BigDecimal(0);
            rateDiff = bookingtreasrate.subtract(cancelrate);
            logger.info("bookingtreasrate " + bookingtreasrate + " cancelrate " + cancelrate + " rateDiff " + rateDiff);
            cancelAmtTres = getBuyOrSellAmount(fwdcontractnum, treRefNo, buyOrSell);
            BigDecimal cancellationamtFrmTreasury = new BigDecimal(cancelAmtTres);
            PLAmtBeforeConvertion = cancellationamtFrmTreasury.multiply(rateDiff);
            logger.info("PLAmtBeforeConvertion" + PLAmtBeforeConvertion);
            if (PLAmtBeforeConvertion.compareTo(BigDecimal.ZERO) > 0) {
              fxRateOnCurrency = getRateForConversion(toCrncyCode, "B");
            } else {
              fxRateOnCurrency = getRateForConversion(toCrncyCode, "S");
            }
            BigDecimal fxRateOnCurrencyFetched = new BigDecimal(fxRateOnCurrency);
            PLAmt = PLAmtBeforeConvertion.multiply(fxRateOnCurrencyFetched);
          }
          fwdContractVO.setPlAmount(PLAmt + " " + "INR");
         






















          BigDecimal toAmount = new BigDecimal(0);
          if ((CommonMethods.isValidString(toCrncyCode)) && (CommonMethods.isValidString(treasuryRate)))
          {
            toAmount = new BigDecimal(refAmount).multiply(new BigDecimal(treasuryRate));
            logger.info(" treasuryRate --> " + treasuryRate);
          }
          toAmount = toAmount.setScale(2, RoundingMode.HALF_UP);
          String toCcyAmount = toAmount + " " + toCrncyCode;
          logger.info(" toAmount &&  toCrncyCode --> " + toCcyAmount);
          fwdContractVO.setToCurrencyAmt(toCcyAmount);
          fwdContractVO.setRateBuyOrSell(buyOrSell);
        }
        else
        {
          fwdContractVO.setRateStatus("NoBal");
        }
      }
    }
    catch (Exception e)
    {
      fwdContractVO.setRateStatus("D");
      e.printStackTrace();
    }
    logger.info("Exiting Method");
    return fwdContractVO;
  }
 
  public String getBuyorSell(String fwdcontractnum)
  {
    logger.info("Entering Method");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    String buyorsell = null;
    treasuryHDDTableName = (String)ServiceUtility.TBProperties.get("TreasuryHDDTable");
    try
    {
      logger.info("Enter into Buy or sell Rate");
      con = DBConnectionUtility.getZoneConnection();
      String query = "SELECT BUY_OR_SELL FROM " + treasuryHDDTableName + " WHERE FWC_REF_NUM='" +
        fwdcontractnum.trim() + "' AND HOST_DEAL_CATEGORY ='FWCBOOK'";
      logger.info("Query:" + query);
      pst = new LoggableStatement(con, query);
      rs = pst.executeQuery();
      while (rs.next())
      {
        buyorsell = rs.getString("BUY_OR_SELL");
        logger.info("buyorsell::" + buyorsell);
      }
      logger.info("Buy or Sell:" + buyorsell);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    return buyorsell;
  }
 
  public static String getFwContractBalbuy(String fwContractNo, String custId)
  {
    ResultSet resultSet = null;
   
    Connection tiZoneConnection = null;
   
    PreparedStatement preparedStatement = null;
    treasuryHDDTableName = (String)ServiceUtility.TBProperties.get("TreasuryHDDTable");
   
    String purchaseAmount = null;
   
    String saleAmount = null;
    try
    {
      String availablePurchaseAndSaleAmtsQuery = "SELECT TO_CHAR(SUM(CASE WHEN HOST_DEAL_CATEGORY='FXRATE' THEN BUY_AMOUNT  WHEN HOST_DEAL_CATEGORY='FWCBOOK' THEN BUY_AMOUNT           WHEN HOST_DEAL_CATEGORY='FWCUTIL' THEN -BUY_AMOUNT           WHEN HOST_DEAL_CATEGORY='FWCCANCEL' THEN -SELL_AMOUNT END)) AS BUY_AMOUNT,  TO_CHAR(SUM(CASE WHEN HOST_DEAL_CATEGORY='FXRATE' THEN SELL_AMOUNT  WHEN HOST_DEAL_CATEGORY='FWCBOOK' THEN SELL_AMOUNT           WHEN HOST_DEAL_CATEGORY='FWCUTIL' THEN -SELL_AMOUNT           WHEN HOST_DEAL_CATEGORY='FWCCANCEL' THEN -BUY_AMOUNT END)) AS SELL_AMOUNT FROM " +
     














        treasuryHDDTableName +
       
        " WHERE RECORD_STATUS <>'DELETED' AND RECORD_STATUS <>'TRANSFER' AND COUNTERPARTY_STRING IS NOT NULL AND FWC_REF_NUM IS NOT NULL " +
       
        " AND FWC_REF_NUM=? AND COUNTERPARTY_STRING =? AND HOST_DEAL_CATEGORY <> 'FXRATE'";
     
      tiZoneConnection = DBConnectionUtility.getZoneConnection();
     
      preparedStatement = tiZoneConnection.prepareStatement(availablePurchaseAndSaleAmtsQuery);
     
      preparedStatement.setString(1, fwContractNo.trim());
     
      preparedStatement.setString(2, custId.trim());
     
      resultSet = preparedStatement.executeQuery();
      while (resultSet.next())
      {
        if (CommonMethods.isValidString(resultSet.getString("BUY_AMOUNT"))) {
          purchaseAmount = resultSet.getString("BUY_AMOUNT");
        }
        if (CommonMethods.isValidString(resultSet.getString("SELL_AMOUNT"))) {
          saleAmount = resultSet.getString("SELL_AMOUNT");
        }
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
    return purchaseAmount;
  }
 
  public static String getFwContractBalsell(String fwContractNo, String custId)
  {
    ResultSet resultSet = null;
   
    Connection tiZoneConnection = null;
   
    PreparedStatement preparedStatement = null;
    treasuryHDDTableName = (String)ServiceUtility.TBProperties.get("TreasuryHDDTable");
   
    String purchaseAmount = null;
   
    String saleAmount = null;
    try
    {
      String availablePurchaseAndSaleAmtsQuery = "SELECT TO_CHAR(SUM(CASE WHEN HOST_DEAL_CATEGORY='FXRATE' THEN BUY_AMOUNT  WHEN HOST_DEAL_CATEGORY='FWCBOOK' THEN BUY_AMOUNT           WHEN HOST_DEAL_CATEGORY='FWCUTIL' THEN -BUY_AMOUNT           WHEN HOST_DEAL_CATEGORY='FWCCANCEL' THEN -SELL_AMOUNT END)) AS BUY_AMOUNT,  TO_CHAR(SUM(CASE WHEN HOST_DEAL_CATEGORY='FXRATE' THEN SELL_AMOUNT  WHEN HOST_DEAL_CATEGORY='FWCBOOK' THEN SELL_AMOUNT           WHEN HOST_DEAL_CATEGORY='FWCUTIL' THEN -SELL_AMOUNT           WHEN HOST_DEAL_CATEGORY='FWCCANCEL' THEN -BUY_AMOUNT END)) AS SELL_AMOUNT FROM " +
     














        treasuryHDDTableName +
       
        " WHERE RECORD_STATUS <>'DELETED' AND RECORD_STATUS <> 'TRANSFER' AND COUNTERPARTY_STRING IS NOT NULL AND FWC_REF_NUM IS NOT NULL " +
       
        " AND FWC_REF_NUM=? AND COUNTERPARTY_STRING =? AND HOST_DEAL_CATEGORY <> 'FXRATE'";
     
      tiZoneConnection = DBConnectionUtility.getZoneConnection();
     
      preparedStatement = tiZoneConnection.prepareStatement(availablePurchaseAndSaleAmtsQuery);
     
      preparedStatement.setString(1, fwContractNo.trim());
     
      preparedStatement.setString(2, custId.trim());
     
      resultSet = preparedStatement.executeQuery();
      while (resultSet.next())
      {
        if (CommonMethods.isValidString(resultSet.getString("BUY_AMOUNT"))) {
          purchaseAmount = resultSet.getString("BUY_AMOUNT");
        }
        if (CommonMethods.isValidString(resultSet.getString("SELL_AMOUNT"))) {
          saleAmount = resultSet.getString("SELL_AMOUNT");
        }
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
    return saleAmount;
  }
 
  public ForwardContractVO fetchParticularFwdContractDetails(String id, String fwdContractNo)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement loggableStatement = null;
    ResultSet rs = null;
   
    ForwardContractVO fwdContractVO = null;
    try
    {
      fwdContractVO = new ForwardContractVO();
      con = DBConnectionUtility.getZoneConnection();
      loggableStatement = new LoggableStatement(con, "SELECT ID,CATEGORY,FWC_CONTRACT_NO,SUB_PRODUCT,CIF_ID,BRANCH,ACCT_NUMBER,DEAL_CCY, STATUS,  FWC_AMOUNT,TO_CHAR(TO_DATE(BOOKING_DATE,'DD/MM/YY'),'dd/mm/YYYY') AS BOOKING_DATE,TO_CCY_AMT,TO_CHAR(TO_DATE(DEAL_VALID_FROM,'DD/MM/YY'),'dd/mm/YYYY') AS DEAL_VALID_FROM,  TO_CHAR(TO_DATE(DEAL_VALID_TO,'DD/MM/YY'),'dd/mm/YYYY') AS DEAL_VALID_TO,TREASURY_REF_NO,TREASURY_RATE,OUTSTANDING_AMT,LIMIT_ID,WITHOUT_LIMIT,AVAILABLE_LIMIT,WASH_RATE,LEI_NUMBER,  PL_AMOUNT,CHARGE_AMOUNT,GST_AMOUNT,INSTRUCTIONS,MARGIN,REMARKS FROM CUSTOM_FWC_DETAILS WHERE ID=? AND FWC_CONTRACT_NO=? ");
      loggableStatement.setInt(1, Integer.valueOf(id.trim()).intValue());
      loggableStatement.setString(2, fwdContractNo.trim());
     
      rs = loggableStatement.executeQuery();
      while (rs.next())
      {
        fwdContractVO.setId(String.valueOf(rs.getInt("ID")));
        fwdContractVO.setCategory(rs.getString("CATEGORY"));
        fwdContractVO.setFwdContractNo(rs.getString("FWC_CONTRACT_NO"));
        fwdContractVO.setSubProduct(rs.getString("SUB_PRODUCT"));
        fwdContractVO.setCustomerID(rs.getString("CIF_ID"));
        fwdContractVO.setBranchCode(rs.getString("BRANCH"));
        fwdContractVO.setAcctNumber(rs.getString("ACCT_NUMBER"));
        fwdContractVO.setDealCurrency(rs.getString("DEAL_CCY"));
        fwdContractVO.setFwdContractAmt(rs.getString("FWC_AMOUNT"));
        fwdContractVO.setBookingDate(rs.getString("BOOKING_DATE"));
        fwdContractVO.setToCurrencyAmt(rs.getString("TO_CCY_AMT"));
        fwdContractVO.setDealValidFromDate(rs.getString("DEAL_VALID_FROM"));
        fwdContractVO.setDealValidToDate(rs.getString("DEAL_VALID_TO"));
        fwdContractVO.setTreasuryRefNo(rs.getString("TREASURY_REF_NO"));
        fwdContractVO.setTreasuryRate(rs.getString("TREASURY_RATE"));
        fwdContractVO.setOutstandingAmt(rs.getString("OUTSTANDING_AMT"));
        fwdContractVO.setLimitID(rs.getString("LIMIT_ID"));
        fwdContractVO.setWithoutLimit(rs.getString("WITHOUT_LIMIT"));
        fwdContractVO.setAvailableLimit(rs.getString("AVAILABLE_LIMIT"));
        fwdContractVO.setWashRate(rs.getString("WASH_RATE"));
        fwdContractVO.setLeiNumber(rs.getString("LEI_NUMBER"));
        fwdContractVO.setPlAmount(rs.getString("PL_AMOUNT"));
        fwdContractVO.setChargeAmount(rs.getString("CHARGE_AMOUNT"));
        fwdContractVO.setGstAmount(rs.getString("GST_AMOUNT"));
        fwdContractVO.setInstructions(rs.getString("INSTRUCTIONS"));
        fwdContractVO.setMargin(rs.getString("MARGIN"));
        fwdContractVO.setRemarks(rs.getString("REMARKS"));
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, loggableStatement, rs);
    }
    logger.info("Exiting Method");
    return fwdContractVO;
  }
 
  public ForwardContractVO fetchParticularFwdContractDetailstoModify(String id, String fwdContractNo)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement loggableStatement = null;
    ResultSet rs = null;
   
    ForwardContractVO fwdContractVO = null;
    try
    {
      fwdContractVO = new ForwardContractVO();
      con = DBConnectionUtility.getZoneConnection();
      loggableStatement = new LoggableStatement(con, "SELECT ID,CATEGORY,FWC_CONTRACT_NO,SUB_PRODUCT,CIF_ID,BRANCH,ACCT_NUMBER,DEAL_CCY, STATUS,  FWC_AMOUNT,TO_CHAR(TO_DATE(BOOKING_DATE,'DD/MM/YY'),'dd/mm/YYYY') AS BOOKING_DATE,TO_CCY_AMT,TO_CHAR(TO_DATE(DEAL_VALID_FROM,'DD/MM/YY'),'dd/mm/YYYY') AS DEAL_VALID_FROM,  TO_CHAR(TO_DATE(DEAL_VALID_TO,'DD/MM/YY'),'dd/mm/YYYY') AS DEAL_VALID_TO,TREASURY_REF_NO,TREASURY_RATE,OUTSTANDING_AMT,LIMIT_ID,WITHOUT_LIMIT,AVAILABLE_LIMIT,WASH_RATE,LEI_NUMBER,  PL_AMOUNT,CHARGE_AMOUNT,GST_AMOUNT,INSTRUCTIONS,MARGIN,REMARKS FROM CUSTOM_FWC_DETAILS WHERE ID=? AND FWC_CONTRACT_NO=? ");
      loggableStatement.setInt(1, Integer.valueOf(id.trim()).intValue());
      loggableStatement.setString(2, fwdContractNo.trim());
     
      rs = loggableStatement.executeQuery();
      while (rs.next())
      {
        fwdContractVO.setId(String.valueOf(rs.getInt("ID")));
        fwdContractVO.setCategory(rs.getString("CATEGORY"));
        fwdContractVO.setFwdContractNo(rs.getString("FWC_CONTRACT_NO"));
        fwdContractVO.setSubProduct(rs.getString("SUB_PRODUCT"));
        fwdContractVO.setCustomerID(rs.getString("CIF_ID"));
        fwdContractVO.setBranchCode(rs.getString("BRANCH"));
        fwdContractVO.setAcctNumber(rs.getString("ACCT_NUMBER"));
        fwdContractVO.setDealCurrency(rs.getString("DEAL_CCY"));
        if ((rs.getString("STATUS") != null) && ("PENDING TO SUBMIT".equalsIgnoreCase(rs.getString("STATUS"))))
        {
          fwdContractVO.setFwdContractAmt(rs.getString("FWC_AMOUNT"));
          fwdContractVO.setBookingDate(rs.getString("BOOKING_DATE"));
          fwdContractVO.setToCurrencyAmt(rs.getString("TO_CCY_AMT"));
          fwdContractVO.setDealValidFromDate(rs.getString("DEAL_VALID_FROM"));
          fwdContractVO.setDealValidToDate(rs.getString("DEAL_VALID_TO"));
          fwdContractVO.setTreasuryRefNo(rs.getString("TREASURY_REF_NO"));
          fwdContractVO.setTreasuryRate(rs.getString("TREASURY_RATE"));
          fwdContractVO.setOutstandingAmt(rs.getString("OUTSTANDING_AMT"));
        }
        fwdContractVO.setLimitID(rs.getString("LIMIT_ID"));
        fwdContractVO.setWithoutLimit(rs.getString("WITHOUT_LIMIT"));
        fwdContractVO.setAvailableLimit(rs.getString("AVAILABLE_LIMIT"));
        fwdContractVO.setWashRate(rs.getString("WASH_RATE"));
        fwdContractVO.setLeiNumber(rs.getString("LEI_NUMBER"));
        fwdContractVO.setPlAmount(rs.getString("PL_AMOUNT"));
        fwdContractVO.setChargeAmount(rs.getString("CHARGE_AMOUNT"));
        fwdContractVO.setGstAmount(rs.getString("GST_AMOUNT"));
        fwdContractVO.setInstructions(rs.getString("INSTRUCTIONS"));
        fwdContractVO.setMargin(rs.getString("MARGIN"));
        fwdContractVO.setRemarks(rs.getString("REMARKS"));
      }
    }
    catch (Exception e)
    {
      logger.info("Exception in fetchParticularFwdContractDetailstoModify" + e.getMessage());
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, loggableStatement, rs);
    }
    logger.info("Exiting Method");
    return fwdContractVO;
  }
 
  public ForwardContractVO fetchParticularCancelFwdContractDetails(String id, String fwdContractNo)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement loggableStatement = null;
    ResultSet rs = null;
   
    ForwardContractVO fwdContractVO = null;
    try
    {
      fwdContractVO = new ForwardContractVO();
      con = DBConnectionUtility.getZoneConnection();
      loggableStatement = new LoggableStatement(con, "SELECT ID,CATEGORY,FWC_CONTRACT_NO,SUB_PRODUCT,CIF_ID,BRANCH,ACCT_NUMBER,DEAL_CCY,  FWC_AMOUNT,TO_CHAR(TO_DATE(BOOKING_DATE,'DD/MM/YY'),'dd/mm/YYYY') AS BOOKING_DATE,TO_CCY_AMT,TO_CHAR(TO_DATE(DEAL_VALID_FROM,'DD/MM/YY'),'dd/mm/YYYY') AS DEAL_VALID_FROM,  TO_CHAR(TO_DATE(DEAL_VALID_TO,'DD/MM/YY'),'dd/mm/YYYY') AS DEAL_VALID_TO,TREASURY_REF_NO,TREASURY_RATE,OUTSTANDING_AMT,LIMIT_ID,WITHOUT_LIMIT,AVAILABLE_LIMIT,WASH_RATE,LEI_NUMBER,  PL_AMOUNT,CHARGE_AMOUNT,GST_AMOUNT,INSTRUCTIONS,MARGIN,REMARKS,CANCELLATION_AMOUNT,TRANS_ID,TRANS_DATE,BOOKING_RATE FROM CUSTOM_FWC_DETAILS WHERE ID=? AND FWC_CONTRACT_NO=? ");
      loggableStatement.setInt(1, Integer.valueOf(id.trim()).intValue());
      loggableStatement.setString(2, fwdContractNo.trim());
     
      rs = loggableStatement.executeQuery();
      while (rs.next())
      {
        fwdContractVO.setId(String.valueOf(rs.getInt("ID")));
        fwdContractVO.setCategory(rs.getString("CATEGORY"));
        fwdContractVO.setFwdContractNo(rs.getString("FWC_CONTRACT_NO"));
        fwdContractVO.setSubProduct(rs.getString("SUB_PRODUCT"));
        fwdContractVO.setCustomerID(rs.getString("CIF_ID"));
        fwdContractVO.setBranchCode(rs.getString("BRANCH"));
        fwdContractVO.setAcctNumber(rs.getString("ACCT_NUMBER"));
        fwdContractVO.setDealCurrency(rs.getString("DEAL_CCY"));
        fwdContractVO.setFwdContractAmt(rs.getString("FWC_AMOUNT"));
        fwdContractVO.setBookingDate(rs.getString("BOOKING_DATE"));
        fwdContractVO.setToCurrencyAmt(rs.getString("TO_CCY_AMT"));
        fwdContractVO.setDealValidFromDate(rs.getString("DEAL_VALID_FROM"));
        fwdContractVO.setDealValidToDate(rs.getString("DEAL_VALID_TO"));
        fwdContractVO.setTreasuryRefNo(rs.getString("TREASURY_REF_NO"));
        fwdContractVO.setTreasuryRate(rs.getString("TREASURY_RATE"));
        fwdContractVO.setOutstandingAmt(rs.getString("OUTSTANDING_AMT"));
        fwdContractVO.setLimitID(rs.getString("LIMIT_ID"));
        fwdContractVO.setWithoutLimit(rs.getString("WITHOUT_LIMIT"));
        fwdContractVO.setAvailableLimit(rs.getString("AVAILABLE_LIMIT"));
        fwdContractVO.setWashRate(rs.getString("WASH_RATE"));
        fwdContractVO.setLeiNumber(rs.getString("LEI_NUMBER"));
        fwdContractVO.setPlAmount(rs.getString("PL_AMOUNT"));
        fwdContractVO.setChargeAmount(rs.getString("CHARGE_AMOUNT"));
        fwdContractVO.setGstAmount(rs.getString("GST_AMOUNT"));
        fwdContractVO.setInstructions(rs.getString("INSTRUCTIONS"));
        fwdContractVO.setMargin(rs.getString("MARGIN"));
        fwdContractVO.setRemarks(rs.getString("REMARKS"));
        fwdContractVO.setCancellationamount(rs.getString("CANCELLATION_AMOUNT"));
        fwdContractVO.setTransid(rs.getString("TRANS_ID"));
        fwdContractVO.setTransdate(rs.getString("TRANS_DATE"));
        fwdContractVO.setBookingrate(rs.getString("BOOKING_RATE"));
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, loggableStatement, rs);
    }
    logger.info("Exiting Method");
    return fwdContractVO;
  }
 
  public ForwardContractVO fetchFWCReferenceDetails(String fwdContractNo)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement loggableStatement = null;
    ResultSet rs = null;
   
    ForwardContractVO fwdContractVO = null;
    try
    {
      fwdContractVO = new ForwardContractVO();
      con = DBConnectionUtility.getZoneConnection();
      loggableStatement = new LoggableStatement(con, "SELECT ID,CATEGORY,FWC_CONTRACT_NO,SUB_PRODUCT,CIF_ID,BRANCH,ACCT_NUMBER,DEAL_CCY,  FWC_AMOUNT,TO_CHAR(TO_DATE(BOOKING_DATE,'DD/MM/YY'),'dd/mm/YYYY') AS BOOKING_DATE,TO_CCY_AMT,TO_CHAR(TO_DATE(DEAL_VALID_FROM,'DD/MM/YY'),'dd/mm/YYYY') AS DEAL_VALID_FROM,  TO_CHAR(TO_DATE(DEAL_VALID_TO,'DD/MM/YY'),'dd/mm/YYYY') AS DEAL_VALID_TO,TREASURY_REF_NO,TREASURY_RATE,OUTSTANDING_AMT,LIMIT_ID,WITHOUT_LIMIT,AVAILABLE_LIMIT,WASH_RATE,LEI_NUMBER,  PL_AMOUNT,CHARGE_AMOUNT,GST_AMOUNT,INSTRUCTIONS,MARGIN,REMARKS FROM CUSTOM_FWC_DETAILS WHERE FWC_CONTRACT_NO=? AND CATEGORY='FWCBOOK' ");
      loggableStatement.setString(1, fwdContractNo.trim());
     
      rs = loggableStatement.executeQuery();
      while (rs.next())
      {
        fwdContractVO.setCategory(rs.getString("CATEGORY"));
        fwdContractVO.setFwdContractNo(rs.getString("FWC_CONTRACT_NO"));
        fwdContractVO.setSubProduct(rs.getString("SUB_PRODUCT"));
        fwdContractVO.setCustomerID(rs.getString("CIF_ID"));
        fwdContractVO.setBranchCode(rs.getString("BRANCH"));
        fwdContractVO.setAcctNumber(rs.getString("ACCT_NUMBER"));
        fwdContractVO.setBookingDate(rs.getString("BOOKING_DATE"));
        fwdContractVO.setDealValidFromDate(rs.getString("DEAL_VALID_FROM"));
        fwdContractVO.setDealValidToDate(rs.getString("DEAL_VALID_TO"));
        fwdContractVO.setLimitID(rs.getString("LIMIT_ID"));
        fwdContractVO.setWithoutLimit(rs.getString("WITHOUT_LIMIT"));
        fwdContractVO.setAvailableLimit(rs.getString("AVAILABLE_LIMIT"));
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, loggableStatement, rs);
    }
    logger.info("Exiting Method");
    return fwdContractVO;
  }
 
  public String getRole(ForwardContractVO fwdContractVO)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement loggableStatement = null;
    ResultSet rs = null;
    String result = null;
    String sqlQuery = null;
    try
    {
      logger.info("get sessionUserName --> " + fwdContractVO.getSessionUserName());
      if (!CommonMethods.isNull(fwdContractVO.getSessionUserName()))
      {
        sqlQuery =
       
          "SELECT COUNT(*) as Count FROM SECAGE88 U, TEAMUSRMAP T  WHERE  T.USERKEY = U.SKEY80 AND U.NAME85  = '" + fwdContractVO.getSessionUserName().trim() + "' AND UPPER(T.TEAMKEY) LIKE '%FWC%' group by U.NAME85 ";
       
        con = DBConnectionUtility.getZoneConnection();
       
        loggableStatement = new LoggableStatement(con, sqlQuery);
       



        logger.info(loggableStatement.getQueryString());
        rs = loggableStatement.executeQuery();
        while (rs.next())
        {
          result = Integer.toString(rs.getInt(1));
          logger.info("get Role result" + result);
        }
      }
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, loggableStatement, rs);
    }
    return result;
  }
 
  public int checkLoginedUserType(ForwardContractVO fwdContractVO)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement loggableStatement = null;
    ResultSet rs = null;
    int result = 0;
    String sqlQuery = null;
    try
    {
      if (CommonMethods.isValidString(fwdContractVO.getSessionUserName()))
      {
        con = DBConnectionUtility.getZoneConnection();
       
        sqlQuery = "SELECT Count(*) AS TEAMCNT FROM SECAGE88 U LEFT JOIN TEAMUSRMAP T  ON T.USERKEY = U.SKEY80 WHERE TRIM(UPPER(U.NAME85))  = TRIM(UPPER('" +
          fwdContractVO.getSessionUserName().trim() + "')) " +
          " AND TRIM(UPPER(T.TEAMKEY)) = TRIM(UPPER('" + fwdContractVO.getPageType() + "'))";
       
        loggableStatement = new LoggableStatement(con, sqlQuery);
       


        logger.info("CheckLoginedUserType: " + loggableStatement.getQueryString());
       
        rs = loggableStatement.executeQuery();
        if (rs.next())
        {
          result = rs.getInt("TEAMCNT");
          logger.info("check Logined UserType result" + result);
        }
      }
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, loggableStatement, rs);
    }
    logger.info("Exiting Method");
    return result;
  }
 
  public int checkLoginedUserType1(String user, String team)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement loggableStatement = null;
    ResultSet rs = null;
    int result = 0;
    String sqlQuery = null;
    try
    {
      if (CommonMethods.isValidString(user))
      {
        con = DBConnectionUtility.getZoneConnection();
       
        sqlQuery = "SELECT count(T.TEAMKEY) as TEAMCNT FROM SECAGE88 U LEFT JOIN TEAMUSRMAP T  ON T.USERKEY = U.SKEY80 WHERE TRIM(UPPER(U.NAME85))  = TRIM(UPPER('" +
          user.trim() + "')) " +
          " AND TRIM(UPPER(T.TEAMKEY)) LIKE 'FWC%" + team + "%'";
       
        loggableStatement = new LoggableStatement(con, sqlQuery);
       


        logger.info("CheckLoginedUserType: " + loggableStatement.getQueryString());
       
        rs = loggableStatement.executeQuery();
        if (rs.next())
        {
          result = rs.getInt("TEAMCNT");
          logger.info("check Logined UserType result" + result);
        }
      }
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, loggableStatement, rs);
    }
    logger.info("Exiting Method");
    return result;
  }
 
  public String getSessionUserID(String userName)
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement pst = null;
    ResultSet rs = null;
    String sessionUserId = null;
    String QUERY = "select skey80 from secage88 where name85='" + userName.trim() + "'";
    try
    {
      con = DBConnectionUtility.getZoneConnection();
      pst = new LoggableStatement(con, QUERY);
     
      logger.info(pst.getQueryString());
      rs = pst.executeQuery();
      while (rs.next()) {
        sessionUserId = rs.getString("skey80");
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
    logger.info("Exiting Method");
    return sessionUserId;
  }
 
  public ArrayList<StaticDataVO> customerSearch(ArrayList<StaticDataVO> customerList)
  {
    logger.info("Entering Method");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    try
    {
      customerList = new ArrayList();
      con = DBConnectionUtility.getZoneConnection();
      pst = new LoggableStatement(con, "select GFCUS1,GFCUN,GFCPNC from GFPF order by GFCUS1");
      logger.info(pst.getQueryString());
      rs = pst.executeQuery();
      while (rs.next())
      {
        StaticDataVO cusVo = new StaticDataVO();
        cusVo.setCustomerID(CommonMethods.nullAndTrimString(rs.getString(1)));
        cusVo.setCustomerName(CommonMethods.nullAndTrimString(rs.getString(2)));
        customerList.add(cusVo);
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
    logger.info("Exiting Method");
    return customerList;
  }
 
  public ArrayList<StaticDataVO> accountSearch(ArrayList<StaticDataVO> accountList)
  {
    logger.info("Entering Method");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    try
    {
      accountList = new ArrayList();
      con = DBConnectionUtility.getZoneConnection();
      pst = new LoggableStatement(con, "SELECT BO_ACCTNO,ACC_TYPE,BRCH_MNM,CUS_MNM,SHORTNAME,CURRENCY FROM ACCOUNT WHERE TRIM(CUS_MNM) IS NOT NULL AND TRIM(BO_ACCTNO) IS NOT NULL AND CURRENCY = 'INR' ORDER BY CUS_MNM,BO_ACCTNO,ACC_TYPE ");
      logger.info(pst.getQueryString());
      rs = pst.executeQuery();
      while (rs.next())
      {
        StaticDataVO acctVO = new StaticDataVO();
        acctVO.setAcctNumber(CommonMethods.nullAndTrimString(rs.getString(1)));
        acctVO.setAcctType(CommonMethods.nullAndTrimString(rs.getString(2)));
        acctVO.setBranchCode(CommonMethods.nullAndTrimString(rs.getString(3)));
        acctVO.setCustomerID(CommonMethods.nullAndTrimString(rs.getString(4)));
        acctVO.setShortName(CommonMethods.nullAndTrimString(rs.getString(5)));
        acctVO.setCurrency(CommonMethods.nullAndTrimString(rs.getString(6)));
        accountList.add(acctVO);
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
    logger.info("Exiting Method");
    return accountList;
  }
 
  public ArrayList<StaticDataVO> branchSearch(ArrayList<StaticDataVO> branchList)
  {
    logger.info("Entering Method");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    try
    {
      branchList = new ArrayList();
      con = DBConnectionUtility.getZoneConnection();
      pst = new LoggableStatement(con, "SELECT CABRNM,FULLNAME FROM CAPF ORDER BY CABRNM");
      logger.info(pst.getQueryString());
      rs = pst.executeQuery();
      while (rs.next())
      {
        StaticDataVO branchVO = new StaticDataVO();
        branchVO.setBranchCode(CommonMethods.nullAndTrimString(rs.getString(1)));
        branchVO.setBranchFullName(CommonMethods.nullAndTrimString(rs.getString(2)));
        branchList.add(branchVO);
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
    logger.info("Exiting Method");
    return branchList;
  }
 
  public ArrayList<StaticDataVO> currencySearch(ArrayList<StaticDataVO> currencyList)
  {
    logger.info("Entering Method");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    try
    {
      currencyList = new ArrayList();
      con = DBConnectionUtility.getZoneConnection();
      pst = new LoggableStatement(con, "SELECT C8CCY,C8CUR FROM C8PF ORDER BY C8CCY");
      logger.info(pst.getQueryString());
      rs = pst.executeQuery();
      while (rs.next())
      {
        StaticDataVO ccyVO = new StaticDataVO();
        ccyVO.setCurrency(CommonMethods.nullAndTrimString(rs.getString(1)));
        ccyVO.setCurrencyFullName(CommonMethods.nullAndTrimString(rs.getString(2)));
        currencyList.add(ccyVO);
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
    logger.info("Exiting Method");
    return currencyList;
  }
 
  public ArrayList<StaticDataVO> fetchTreasuryDetails(ArrayList<StaticDataVO> treasuryList)
  {
    logger.info("Entering Method");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    ServiceUtility.getProperties();
    treasuryHDDTableName = (String)ServiceUtility.TBProperties.get("TreasuryHDDTable");
   
    String treasuryListQuery = "SELECT REFERENCE_NUM,COUNTERPARTY_STRING,TO_CHAR(TO_DATE(HOST_TRAN_DATE,'DD/MM/YY'),'dd/mm/YYYY') AS HOST_TRAN_DATE, TO_CHAR(TO_DATE(START_DATE,'DD/MM/YY'),'dd/mm/YYYY') AS START_DATE,TO_CHAR(TO_DATE(END_DATE,'DD/MM/YY'),'dd/mm/YYYY') AS END_DATE FROM " +
   
      treasuryHDDTableName +
      " WHERE ((HOST_DEAL_CATEGORY = 'FXRATE' AND (FWC_REF_NUM IS NULL OR TRIM(FWC_REF_NUM)='') AND HOST_DEAL_SUB_CATEGORY='FWCBOOK') " +
      " OR (HOST_DEAL_CATEGORY ='FWCCANCEL' AND FWC_REF_NUM IS NOT NULL AND ADDITIONAL_TEXT_1 IN ('MFPCAN','MFSCAN'))) AND RECORD_STATUS='TRANSFER' " +
      " ORDER BY COUNTERPARTY_STRING";
    try
    {
      treasuryList = new ArrayList();
     
      con = DBConnectionUtility.getDBLinkConnection();
      pst = new LoggableStatement(con, treasuryListQuery);
      logger.info(pst.getQueryString());
      rs = pst.executeQuery();
      while (rs.next())
      {
        StaticDataVO treasuryDataVO = new StaticDataVO();
        treasuryDataVO.setTreasuryRefNo(CommonMethods.nullAndTrimString(rs.getString("REFERENCE_NUM")));
        treasuryDataVO.setCustomerID(CommonMethods.nullAndTrimString(rs.getString("COUNTERPARTY_STRING")));
        treasuryDataVO.setBookingDate(CommonMethods.nullAndTrimString(rs.getString("HOST_TRAN_DATE")));
        treasuryDataVO.setDealValidFromDate(CommonMethods.nullAndTrimString(rs.getString("START_DATE")));
        treasuryDataVO.setDealValidToDate(CommonMethods.nullAndTrimString(rs.getString("END_DATE")));
        treasuryList.add(treasuryDataVO);
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
    logger.info("Exiting Method");
    return treasuryList;
  }
 
  public ArrayList<StaticDataVO> fetchFwdContractList(ArrayList<StaticDataVO> fwdContractList)
  {
    logger.info("Entering Method");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    try
    {
      fwdContractList = new ArrayList();
     
      con = DBConnectionUtility.getDBLinkConnection();
      pst = new LoggableStatement(con, "SELECT ID,CATEGORY,FWC_CONTRACT_NO,SUB_PRODUCT,CIF_ID,BRANCH,CATEGORY FROM CUSTOM_FWC_DETAILS WHERE FWC_CONTRACT_NO IS NOT NULL AND STATUS='APPROVED' AND CATEGORY ='FWCBOOK' ORDER BY ID DESC ");
      logger.info(pst.getQueryString());
      rs = pst.executeQuery();
      while (rs.next())
      {
        StaticDataVO fwdContractDataVO = new StaticDataVO();
       
        fwdContractDataVO.setFwdContractNo(CommonMethods.nullAndTrimString(rs.getString("FWC_CONTRACT_NO")));
        fwdContractDataVO.setCustomerID(CommonMethods.nullAndTrimString(rs.getString("CIF_ID")));
        fwdContractDataVO.setBranchCode(CommonMethods.nullAndTrimString(rs.getString("BRANCH")));
        fwdContractDataVO.setSubProduct(CommonMethods.nullAndTrimString(rs.getString("SUB_PRODUCT")));
        fwdContractDataVO.setCategory(CommonMethods.nullAndTrimString(rs.getString("CATEGORY")));
        fwdContractList.add(fwdContractDataVO);
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
    logger.info("Exiting Method");
    return fwdContractList;
  }
 
  public ArrayList<StaticDataVO> fetchLimitDetails(String customerID)
  {
    logger.info("Entering Method");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    ArrayList<StaticDataVO> limitList = null;
    try
    {
      ServiceUtility.getProperties();
      String[] allowedFWCLimitSuffixes = ((String)ServiceUtility.TBProperties.get("AllowedFWCLimits")).split(",");
     
      limitList = new ArrayList();
     
      List<HashMap<String, String>> limitDtlsResList = LimitFetchUtil.getLimitDetailsFromLimitAPI(customerID);
      for (int i = 0; i < limitDtlsResList.size(); i++)
      {
        StaticDataVO limitDataVO = new StaticDataVO();
       
        String limitPrefix = (String)((HashMap)limitDtlsResList.get(i)).get("limitPrefix");
        String limitSuffix = ((String)((HashMap)limitDtlsResList.get(i)).get("limitSuffix")).trim();
        limitDataVO.setLimitID(CommonMethods.returnEmptyIfNull(limitPrefix) + "/" +
          CommonMethods.returnEmptyIfNull(limitSuffix));
        limitDataVO.setLimitDesc((String)((HashMap)limitDtlsResList.get(i)).get("limitDesc"));
       
        limitDataVO.setLimitAmount((String)((HashMap)limitDtlsResList.get(i)).get("limitAmt"));
        limitDataVO.setTotalLiabilityAmt((String)((HashMap)limitDtlsResList.get(i)).get("totalLiability"));
        limitDataVO.setCurrency((String)((HashMap)limitDtlsResList.get(i)).get("crncyCode"));
        limitDataVO.setSanctionDate((String)((HashMap)limitDtlsResList.get(i)).get("sanDate"));
        limitDataVO.setExpiryDate((String)((HashMap)limitDtlsResList.get(i)).get("expiryDate"));
        if (ArrayUtils.contains(allowedFWCLimitSuffixes, limitSuffix)) {
          limitList.add(limitDataVO);
        }
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
    logger.info("Exiting Method");
    return limitList;
  }
 
  public ArrayList<StaticDataVO> filterCusList(ArrayList<StaticDataVO> customerList, StaticDataVO cusDataVo)
  {
    logger.info("Entering Method");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    String cusName = "";
    String cusNumber = "";
    String setValue = null;
    String setValue1 = null;
    try
    {
      String query = "";
      customerList = new ArrayList();
      con = DBConnectionUtility.getZoneConnection();
      if (!CommonMethods.isNull(cusDataVo.getCustomerName()))
      {
        query = "select GFCUS1 AS CIFID,GFCUN AS CUSTOMER from GFPF  WHERE GFCUN like '%'||?||'%' ";
        setValue = cusDataVo.getCustomerName();
      }
      else if (!CommonMethods.isNull(cusDataVo.getCustomerID()))
      {
        query = "select GFCUS1 AS CIFID,GFCUN AS CUSTOMER from GFPF  WHERE GFCUS1 like '%'||?||'%' ";
        setValue = cusDataVo.getCustomerID();
      }
      else if ((!CommonMethods.isNull(cusDataVo.getCustomerName())) &&
        (!CommonMethods.isNull(cusDataVo.getCustomerID())))
      {
        query = "select GFCUS1 AS CIFID,GFCUN AS CUSTOMER from GFPF  WHERE GFCUN like '%'||?||'%' AND GFCUS1 like '%'||?||'%' ";
        setValue = cusDataVo.getCustomerName();
        setValue1 = cusDataVo.getCustomerID();
      }
      else
      {
        query = "select GFCUS1 AS CIFID,GFCUN AS CUSTOMER from GFPF ";
      }
      pst = new LoggableStatement(con, query);
      if (setValue != null) {
        pst.setString(1, setValue);
      }
      if (setValue1 != null) {
        pst.setString(2, setValue1);
      }
      logger.info(pst.getQueryString());
      rs = pst.executeQuery();
      while (rs.next())
      {
        StaticDataVO cusVo = new StaticDataVO();
        cusVo.setCustomerID(CommonMethods.nullAndTrimString(rs.getString("CIFID")));
        cusVo.setCustomerName(CommonMethods.nullAndTrimString(rs.getString("CUSTOMER")));
       
        customerList.add(cusVo);
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
    logger.info("Exiting Method");
    return customerList;
  }
 
  public ArrayList<StaticDataVO> filterAcctList(ArrayList<StaticDataVO> accountList, StaticDataVO acctDataVO)
  {
    logger.info("Entering Method");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    String setValue = null;
    String setValue1 = null;
    try
    {
      String query = "";
      accountList = new ArrayList();
      con = DBConnectionUtility.getZoneConnection();
      if (!CommonMethods.isNull(acctDataVO.getAcctNumber()))
      {
        query = "SELECT BO_ACCTNO,ACC_TYPE,BRCH_MNM,CUS_MNM,SHORTNAME,CURRENCY FROM ACCOUNT WHERE  CURRENCY = 'INR' AND BO_ACCTNO like '%'||?||'%' ";
        setValue = acctDataVO.getAcctNumber();
      }
      else if (!CommonMethods.isNull(acctDataVO.getCustomerID()))
      {
        query = "SELECT BO_ACCTNO,ACC_TYPE,BRCH_MNM,CUS_MNM,SHORTNAME,CURRENCY FROM ACCOUNT WHERE  CURRENCY = 'INR' AND CUS_MNM like '%'||?||'%' ";
        setValue = acctDataVO.getCustomerID();
      }
      else if ((!CommonMethods.isNull(acctDataVO.getAcctNumber())) &&
        (!CommonMethods.isNull(acctDataVO.getCustomerID())))
      {
        query = "SELECT BO_ACCTNO,ACC_TYPE,BRCH_MNM,CUS_MNM,SHORTNAME,CURRENCY FROM ACCOUNT WHERE  CURRENCY = 'INR' AND BO_ACCTNO like '%'||?||'%' AND CUS_MNM like '%'||?||'%' ";
        setValue = acctDataVO.getAcctNumber();
        setValue1 = acctDataVO.getCustomerID();
      }
      else
      {
        query = "SELECT BO_ACCTNO,ACC_TYPE,BRCH_MNM,CUS_MNM,SHORTNAME,CURRENCY FROM ACCOUNT WHERE  CURRENCY = 'INR'";
      }
      pst = new LoggableStatement(con, query);
      if (setValue != null) {
        pst.setString(1, setValue);
      }
      if (setValue1 != null) {
        pst.setString(2, setValue1);
      }
      logger.info(pst.getQueryString());
      rs = pst.executeQuery();
      while (rs.next())
      {
        StaticDataVO acctVO = new StaticDataVO();
        acctVO.setAcctNumber(CommonMethods.nullAndTrimString(rs.getString(1)));
        acctVO.setAcctType(CommonMethods.nullAndTrimString(rs.getString(2)));
        acctVO.setBranchCode(CommonMethods.nullAndTrimString(rs.getString(3)));
        acctVO.setCustomerID(CommonMethods.nullAndTrimString(rs.getString(4)));
        acctVO.setShortName(CommonMethods.nullAndTrimString(rs.getString(5)));
        acctVO.setCurrency(CommonMethods.nullAndTrimString(rs.getString(6)));
        accountList.add(acctVO);
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
    logger.info("Exiting Method");
    return accountList;
  }
 
  public ArrayList<StaticDataVO> filterBranchList(ArrayList<StaticDataVO> branchList, StaticDataVO branchDataVO)
  {
    logger.info("Entering Method");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    String setValue = null;
    try
    {
      String query = "";
      branchList = new ArrayList();
      con = DBConnectionUtility.getZoneConnection();
      if (!CommonMethods.isNull(branchDataVO.getBranchCode()))
      {
        query = "SELECT CABRNM,FULLNAME FROM CAPF  WHERE CABRNM like ?||'%' ";
        setValue = branchDataVO.getBranchCode();
      }
      else
      {
        query = "SELECT CABRNM,FULLNAME FROM CAPF ";
      }
      pst = new LoggableStatement(con, query);
      if (setValue != null) {
        pst.setString(1, setValue);
      }
      logger.info(pst.getQueryString());
      rs = pst.executeQuery();
      while (rs.next())
      {
        StaticDataVO branchVO = new StaticDataVO();
        branchVO.setBranchCode(CommonMethods.nullAndTrimString(rs.getString(1)));
        branchVO.setBranchFullName(CommonMethods.nullAndTrimString(rs.getString(2)));
        branchList.add(branchVO);
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
    logger.info("Exiting Method");
    return branchList;
  }
 
  public ArrayList<StaticDataVO> filterCurrencyList(ArrayList<StaticDataVO> currencyList, StaticDataVO ccyDataVO)
  {
    logger.info("Entering Method");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    String setValue = null;
    try
    {
      String query = "";
      currencyList = new ArrayList();
      con = DBConnectionUtility.getZoneConnection();
      if (!CommonMethods.isNull(ccyDataVO.getCurrency()))
      {
        query = "SELECT C8CCY,C8CUR FROM C8PF  WHERE C8CCY like '%'||?||'%' ";
        setValue = ccyDataVO.getCurrency();
      }
      else
      {
        query = "SELECT C8CCY,C8CUR FROM C8PF ";
      }
      pst = new LoggableStatement(con, query);
      if (setValue != null) {
        pst.setString(1, setValue);
      }
      logger.info(pst.getQueryString());
      rs = pst.executeQuery();
      while (rs.next())
      {
        StaticDataVO ccyVO = new StaticDataVO();
        ccyVO.setCurrency(CommonMethods.nullAndTrimString(rs.getString(1)));
        ccyVO.setCurrencyFullName(CommonMethods.nullAndTrimString(rs.getString(2)));
        currencyList.add(ccyVO);
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
    logger.info("Exiting Method");
    return currencyList;
  }
 
  public ArrayList<StaticDataVO> filterTreasuryList(StaticDataVO treasuryDataVO, ArrayList<StaticDataVO> treasuryList)
  {
    logger.info("Entering Method");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    String setValue = null;
    String setValue1 = null;
   
    ServiceUtility.getProperties();
    treasuryHDDTableName = (String)ServiceUtility.TBProperties.get("TreasuryHDDTable");
   
    String treasuryFilterQuery = "SELECT REFERENCE_NUM,COUNTERPARTY_STRING,TO_CHAR(TO_DATE(HOST_TRAN_DATE,'DD/MM/YY'),'dd/mm/YYYY') AS HOST_TRAN_DATE, TO_CHAR(TO_DATE(START_DATE,'DD/MM/YY'),'dd/mm/YYYY') AS START_DATE,TO_CHAR(TO_DATE(END_DATE,'DD/MM/YY'),'dd/mm/YYYY') AS END_DATE FROM " +
   
      treasuryHDDTableName +
      " WHERE ((HOST_DEAL_CATEGORY = 'FXRATE' AND (FWC_REF_NUM IS NULL OR TRIM(FWC_REF_NUM)='') AND HOST_DEAL_SUB_CATEGORY='FWCBOOK') " +
      " OR (HOST_DEAL_CATEGORY ='FWCCANCEL' AND FWC_REF_NUM IS NOT NULL AND ADDITIONAL_TEXT_1 IN ('MFPCAN','MFSCAN'))) AND RECORD_STATUS='TRANSFER' ";
    try
    {
      String query = "";
     
      treasuryList = new ArrayList();
      con = DBConnectionUtility.getDBLinkConnection();
      if (!CommonMethods.isNull(treasuryDataVO.getTreasuryRefNo()))
      {
        query = treasuryFilterQuery + " AND REFERENCE_NUM like '%'||?||'%' ";
        setValue = treasuryDataVO.getTreasuryRefNo();
      }
      else if (!CommonMethods.isNull(treasuryDataVO.getCustomerID()))
      {
        query = treasuryFilterQuery + " AND COUNTERPARTY_STRING like '%'||?||'%' ";
        setValue = treasuryDataVO.getCustomerID();
      }
      else if ((!CommonMethods.isNull(treasuryDataVO.getTreasuryRefNo())) &&
        (!CommonMethods.isNull(treasuryDataVO.getCustomerID())))
      {
        query =
          treasuryFilterQuery + " AND REFERENCE_NUM like '%'||?||'%' AND COUNTERPARTY_STRING like '%'||?||'%' ";
        setValue = treasuryDataVO.getTreasuryRefNo();
        setValue1 = treasuryDataVO.getCustomerID();
      }
      else
      {
        query = treasuryFilterQuery;
      }
      pst = new LoggableStatement(con, query);
      if (setValue != null) {
        pst.setString(1, setValue);
      }
      if (setValue1 != null) {
        pst.setString(2, setValue1);
      }
      logger.info(pst.getQueryString());
      rs = pst.executeQuery();
      while (rs.next())
      {
        StaticDataVO treasuryVO = new StaticDataVO();
        treasuryVO.setTreasuryRefNo(CommonMethods.nullAndTrimString(rs.getString("REFERENCE_NUM")));
        treasuryVO.setCustomerID(CommonMethods.nullAndTrimString(rs.getString("COUNTERPARTY_STRING")));
        treasuryVO.setBookingDate(CommonMethods.nullAndTrimString(rs.getString("HOST_TRAN_DATE")));
        treasuryVO.setDealValidFromDate(CommonMethods.nullAndTrimString(rs.getString("START_DATE")));
        treasuryVO.setDealValidToDate(CommonMethods.nullAndTrimString(rs.getString("END_DATE")));
        treasuryList.add(treasuryVO);
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
    logger.info("Exiting Method");
    return treasuryList;
  }
 
  public ArrayList<StaticDataVO> filterFwdContractList(StaticDataVO fwdContactDataVO, ArrayList<StaticDataVO> fwdContractList)
  {
    logger.info("Entering Method");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    String setValue = null;
    String setValue1 = null;
    try
    {
      String query = "";
      fwdContractList = new ArrayList();
      con = DBConnectionUtility.getDBLinkConnection();
      if (!CommonMethods.isNull(fwdContactDataVO.getFwdContractNo()))
      {
        query = "SELECT ID,CATEGORY,FWC_CONTRACT_NO,SUB_PRODUCT,CIF_ID,BRANCH,CATEGORY FROM CUSTOM_FWC_DETAILS WHERE FWC_CONTRACT_NO IS NOT NULL AND STATUS='APPROVED' AND CATEGORY ='FWCBOOK'  AND FWC_CONTRACT_NO like '%'||?||'%' ";
        setValue = fwdContactDataVO.getFwdContractNo();
      }
      else if (!CommonMethods.isNull(fwdContactDataVO.getCustomerID()))
      {
        query = "SELECT ID,CATEGORY,FWC_CONTRACT_NO,SUB_PRODUCT,CIF_ID,BRANCH,CATEGORY FROM CUSTOM_FWC_DETAILS WHERE FWC_CONTRACT_NO IS NOT NULL AND STATUS='APPROVED' AND CATEGORY ='FWCBOOK'  AND CIF_ID like '%'||?||'%' ";
        setValue = fwdContactDataVO.getCustomerID();
      }
      else if ((!CommonMethods.isNull(fwdContactDataVO.getFwdContractNo())) &&
        (!CommonMethods.isNull(fwdContactDataVO.getCustomerID())))
      {
        query = "SELECT ID,CATEGORY,FWC_CONTRACT_NO,SUB_PRODUCT,CIF_ID,BRANCH,CATEGORY FROM CUSTOM_FWC_DETAILS WHERE FWC_CONTRACT_NO IS NOT NULL AND STATUS='APPROVED' AND CATEGORY ='FWCBOOK'  AND FWC_CONTRACT_NO like '%'||?||'%' AND CIF_ID like '%'||?||'%' ";
       
        setValue = fwdContactDataVO.getFwdContractNo();
        setValue1 = fwdContactDataVO.getCustomerID();
      }
      else
      {
        query = "SELECT ID,CATEGORY,FWC_CONTRACT_NO,SUB_PRODUCT,CIF_ID,BRANCH,CATEGORY FROM CUSTOM_FWC_DETAILS WHERE FWC_CONTRACT_NO IS NOT NULL AND STATUS='APPROVED' AND CATEGORY ='FWCBOOK' ";
      }
      pst = new LoggableStatement(con, query);
      if (setValue != null) {
        pst.setString(1, setValue);
      }
      if (setValue1 != null) {
        pst.setString(2, setValue1);
      }
      logger.info(pst.getQueryString());
      rs = pst.executeQuery();
      while (rs.next())
      {
        StaticDataVO fwdContractDataVO = new StaticDataVO();
       
        fwdContractDataVO.setFwdContractNo(CommonMethods.nullAndTrimString(rs.getString("FWC_CONTRACT_NO")));
        fwdContractDataVO.setCustomerID(CommonMethods.nullAndTrimString(rs.getString("CIF_ID")));
        fwdContractDataVO.setBranchCode(CommonMethods.nullAndTrimString(rs.getString("BRANCH")));
        fwdContractDataVO.setSubProduct(CommonMethods.nullAndTrimString(rs.getString("SUB_PRODUCT")));
        fwdContractDataVO.setCategory(CommonMethods.nullAndTrimString(rs.getString("CATEGORY")));
        fwdContractList.add(fwdContractDataVO);
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
    logger.info("Exiting Method");
    return fwdContractList;
  }
 
  public void setErrorForFWCDetails(String errorCode, ForwardContractVO chargeVO)
  {
    String errormsg = CommonMethods.getErrorDescFromProperties(errorCode);
    Object[] arg = { Integer.valueOf(0), "E", errormsg, "INPUT" };
    CommonMethods.setErrorvalues(arg, this.alertMsgArray);
    if (this.alertMsgArray.size() > 0) {
      chargeVO.setErrorList(this.alertMsgArray);
    }
  }
 
  public ForwardContractVO saveBookingDetails(ForwardContractVO fwdContractVO)
    throws DAOException
  {
    HttpSession session = ServletActionContext.getRequest().getSession();
    HttpServletRequest request = (HttpServletRequest)ActionContext.getContext()
      .get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
    try
    {
      logger.info("CIF ID-------------------------------" + fwdContractVO.getCustomerID().trim());
      validateBookingDetails(fwdContractVO);
      logger.info("alertMsgArray.size() " + this.alertMsgArray.size());
      if (this.alertMsgArray.size() == 0)
      {
        String userID = request.getRemoteUser();
        if (userID == null) {
          userID = "SUPERVISOR";
        }
        logger.info("User ID--------------------------->" + userID);
        String screen = fwdContractVO.getScreenType();
        if (screen.equalsIgnoreCase("MakerBookingScreen"))
        {
          if (CommonMethods.isValidString(fwdContractVO.getFwdContractNo())) {
            updateFwdBookingContractDetails(fwdContractVO, "FWCBOOK", "PENDING TO SUBMIT", "Saved");
          } else {
            insertBookingDetails(fwdContractVO, "FWCBOOK", "PENDING TO SUBMIT", "Saved");
          }
        }
        else if (screen.equalsIgnoreCase("MakerCancelScreen"))
        {
          int count = getRecordCountFromDB(fwdContractVO, "FWCCANCEL");
          if ((CommonMethods.isValidString(fwdContractVO.getFwdContractNo())) && (count == 1)) {
            fwdContractVO = updateFwdCancelContractDetails(fwdContractVO, "FWCCANCEL", "PENDING TO SUBMIT", "Saved");
          } else if ((CommonMethods.isValidString(fwdContractVO.getFwdContractNo())) && (count == 0)) {
            fwdContractVO = insertCancelDetails(fwdContractVO, "FWCCANCEL", "PENDING TO SUBMIT", "Saved");
          }
        }
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return fwdContractVO;
  }
 
  public ForwardContractVO insertBookingDetails(ForwardContractVO fwdContractVO, String category, String status, String action)
    throws DAOException
  {
    Connection connection = null;
    LoggableStatement loggableStatement = null;
    HttpSession session = ServletActionContext.getRequest().getSession();
    HttpServletRequest request = (HttpServletRequest)ActionContext.getContext()
      .get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
    try
    {
      logger.info("Sub product-------------------------------" + fwdContractVO.getSubProduct().trim());
      logger.info("CIF ID-------------------------------" + fwdContractVO.getCustomerID().trim());
      validateBookingDetails(fwdContractVO);
      logger.info("alertMsgArray size " + this.alertMsgArray.size());
      if (this.alertMsgArray.size() == 0)
      {
        String userID = request.getRemoteUser();
        if (userID == null) {
          userID = "SUPERVISOR";
        }
        connection = DBConnectionUtility.getZoneConnection();
        loggableStatement = new LoggableStatement(connection, "INSERT INTO CUSTOM_FWC_DETAILS(CATEGORY,SUB_PRODUCT,CIF_ID,BRANCH,ACCT_NUMBER,DEAL_CCY, \tBOOKING_DATE,FWC_AMOUNT,TO_CCY_AMT,DEAL_VALID_FROM,DEAL_VALID_TO,TREASURY_REF_NO,TREASURY_RATE,OUTSTANDING_AMT,LIMIT_ID,WITHOUT_LIMIT, \tAVAILABLE_LIMIT,WASH_RATE,LEI_NUMBER,PL_AMOUNT,CHARGE_AMOUNT,GST_AMOUNT,INSTRUCTIONS,MARGIN,STATUS,INPUT_BY,INPUT_TIMESTAMP,LAST_ACTION,FWC_CONTRACT_NO) \tVALUES(?,?,?,?,?,?,TO_DATE(?,'dd/mm/yyyy'),?,?,TO_DATE(?,'dd/mm/yyyy'),TO_DATE(?,'dd/mm/yyyy'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,SYSTIMESTAMP,?,?) ");
        loggableStatement.setString(1, category);
        loggableStatement.setString(2, fwdContractVO.getSubProduct());
        loggableStatement.setString(3, fwdContractVO.getCustomerID());
        loggableStatement.setString(4, fwdContractVO.getBranchCode());
        loggableStatement.setString(5, fwdContractVO.getAcctNumber());
        loggableStatement.setString(6, fwdContractVO.getDealCurrency());
        loggableStatement.setString(7, fwdContractVO.getBookingDate());
        loggableStatement.setString(8, fwdContractVO.getFwdContractAmt());
        loggableStatement.setString(9, fwdContractVO.getToCurrencyAmt());
        loggableStatement.setString(10, fwdContractVO.getDealValidFromDate());
        loggableStatement.setString(11, fwdContractVO.getDealValidToDate());
        loggableStatement.setString(12, fwdContractVO.getTreasuryRefNo());
        loggableStatement.setString(13, fwdContractVO.getTreasuryRate());
        loggableStatement.setString(14, fwdContractVO.getOutstandingAmt());
       
        loggableStatement.setString(15, fwdContractVO.getLimitID());
        loggableStatement.setString(16, fwdContractVO.getWithoutLimit());
        loggableStatement.setString(17, fwdContractVO.getAvailableLimit());
       
        loggableStatement.setString(18, fwdContractVO.getWashRate());
        loggableStatement.setString(19, fwdContractVO.getLeiNumber());
        loggableStatement.setString(20, fwdContractVO.getPlAmount());
        loggableStatement.setString(21, fwdContractVO.getChargeAmount());
        loggableStatement.setString(22, fwdContractVO.getGstAmount());
        loggableStatement.setString(23, fwdContractVO.getInstructions());
        loggableStatement.setString(24, fwdContractVO.getMargin());
        loggableStatement.setString(25, status);
        loggableStatement.setString(26, userID.trim());
        loggableStatement.setString(27, action);
       
        String fwdContractSeqNo = FWCUtil.generateFWCReferenceNumber(fwdContractVO.getBranchCode(),
          fwdContractVO.getSubProduct());
        loggableStatement.setString(28, fwdContractSeqNo);
       
        logger.info("Insert Query----------------->" + loggableStatement.getQueryString());
       
        int count = loggableStatement.executeUpdate();
        if (count > 0)
        {
          logger.info("Inserted Successfully");
          fwdContractVO.setCount(count);
          fwdContractVO.setFwdContractNo(fwdContractSeqNo);
        }
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(connection, loggableStatement, null);
    }
    return fwdContractVO;
  }
 
  public ForwardContractVO updateFwdBookingContractDetails(ForwardContractVO fwdContractVO, String category, String status, String action)
    throws DAOException
  {
    Connection connection = null;
    LoggableStatement loggableStatement = null;
    HttpSession session = ServletActionContext.getRequest().getSession();
    HttpServletRequest request = (HttpServletRequest)ActionContext.getContext()
      .get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
    try
    {
      logger.info("Forward Contract-------------------------------" + fwdContractVO.getFwdContractNo().trim());
      logger.info("CIF ID-------------------------------" + fwdContractVO.getCustomerID().trim());
      validateBookingDetails(fwdContractVO);
      logger.info("alertMsgArray size " + this.alertMsgArray.size());
      if (this.alertMsgArray.size() == 0)
      {
        String userID = request.getRemoteUser();
        if (userID == null) {
          userID = "SUPERVISOR";
        }
        connection = DBConnectionUtility.getZoneConnection();
        loggableStatement = new LoggableStatement(connection, "UPDATE CUSTOM_FWC_DETAILS SET SUB_PRODUCT=?,BRANCH=?,ACCT_NUMBER=?,DEAL_CCY=?,BOOKING_DATE=TO_DATE(?,'dd/mm/yyyy'),  FWC_AMOUNT=?,TO_CCY_AMT=?,DEAL_VALID_FROM=TO_DATE(?,'dd/mm/yyyy'),DEAL_VALID_TO=TO_DATE(?,'dd/mm/yyyy') ,TREASURY_REF_NO=?,TREASURY_RATE=?,OUTSTANDING_AMT=?,LIMIT_ID=?,WITHOUT_LIMIT=?,AVAILABLE_LIMIT=?,WASH_RATE=?,LEI_NUMBER=?,  PL_AMOUNT=?,CHARGE_AMOUNT=?,GST_AMOUNT=?,INSTRUCTIONS=?,MARGIN=?,STATUS=?,MODIFIED_BY=?,MODIFIED_TIMESTAMP=SYSTIMESTAMP,LAST_ACTION=? WHERE CATEGORY=? AND FWC_CONTRACT_NO=? ");
        loggableStatement.setString(1, fwdContractVO.getSubProduct());
        loggableStatement.setString(2, fwdContractVO.getBranchCode());
        loggableStatement.setString(3, fwdContractVO.getAcctNumber());
        loggableStatement.setString(4, fwdContractVO.getDealCurrency());
        loggableStatement.setString(5, fwdContractVO.getBookingDate());
        loggableStatement.setString(6, fwdContractVO.getFwdContractAmt());
        loggableStatement.setString(7, fwdContractVO.getToCurrencyAmt());
        loggableStatement.setString(8, fwdContractVO.getDealValidFromDate());
        loggableStatement.setString(9, fwdContractVO.getDealValidToDate());
        loggableStatement.setString(10, fwdContractVO.getTreasuryRefNo());
        loggableStatement.setString(11, fwdContractVO.getTreasuryRate());
        loggableStatement.setString(12, fwdContractVO.getOutstandingAmt());
       
        loggableStatement.setString(13, fwdContractVO.getLimitID());
        loggableStatement.setString(14, fwdContractVO.getWithoutLimit());
        loggableStatement.setString(15, fwdContractVO.getAvailableLimit());
       
        loggableStatement.setString(16, fwdContractVO.getWashRate());
        loggableStatement.setString(17, fwdContractVO.getLeiNumber());
        loggableStatement.setString(18, fwdContractVO.getPlAmount());
        loggableStatement.setString(19, fwdContractVO.getChargeAmount());
        loggableStatement.setString(20, fwdContractVO.getGstAmount());
        loggableStatement.setString(21, fwdContractVO.getInstructions());
        loggableStatement.setString(22, fwdContractVO.getMargin());
        loggableStatement.setString(23, status);
        loggableStatement.setString(24, userID.trim());
        loggableStatement.setString(25, action);
        loggableStatement.setString(26, category);
        loggableStatement.setString(27, fwdContractVO.getFwdContractNo());
       

        logger.info("Update Query----------------->" + loggableStatement.getQueryString());
       
        int count = loggableStatement.executeUpdate();
        if (count > 0)
        {
          logger.info("Updated Successfully");
          fwdContractVO.setCount(count);
        }
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(connection, loggableStatement, null);
    }
    return fwdContractVO;
  }
 
  public ForwardContractVO updateFwdCancelContractDetails(ForwardContractVO fwdContractVO, String category, String status, String action)
    throws DAOException
  {
    Connection connection = null;
    LoggableStatement loggableStatement = null;
    HttpSession session = ServletActionContext.getRequest().getSession();
    HttpServletRequest request = (HttpServletRequest)ActionContext.getContext()
      .get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
    try
    {
      logger.info("Forward Contract-------------------------------" + fwdContractVO.getFwdContractNo().trim());
      logger.info("CIF ID-------------------------------" + fwdContractVO.getCustomerID().trim());
      validateBookingDetails(fwdContractVO);
      logger.info("alertMsgArray size " + this.alertMsgArray.size());
      if (this.alertMsgArray.size() == 0)
      {
        String userID = request.getRemoteUser();
        if (userID == null) {
          userID = "SUPERVISOR";
        }
        logger.info("Update Forward Contract Cancel Details");
       
        connection = DBConnectionUtility.getZoneConnection();
        logger.info("after getting db connection");
       







        loggableStatement = new LoggableStatement(connection, "UPDATE CUSTOM_FWC_DETAILS SET SUB_PRODUCT=?,BRANCH=?,ACCT_NUMBER=?,DEAL_CCY=?,BOOKING_DATE=TO_DATE(?,'dd/mm/yyyy'),  FWC_AMOUNT=?,TO_CCY_AMT=?,DEAL_VALID_FROM=TO_DATE(?,'dd/mm/yyyy'),DEAL_VALID_TO=TO_DATE(?,'dd/mm/yyyy') ,TREASURY_REF_NO=?,TREASURY_RATE=?,OUTSTANDING_AMT=?,LIMIT_ID=?,WITHOUT_LIMIT=?,AVAILABLE_LIMIT=?,WASH_RATE=?,LEI_NUMBER=?,  PL_AMOUNT=?,CHARGE_AMOUNT=?,GST_AMOUNT=?,INSTRUCTIONS=?,MARGIN=?,STATUS=?,MODIFIED_BY=?,MODIFIED_TIMESTAMP=SYSTIMESTAMP,LAST_ACTION=?,CANCELLATION_AMOUNT=?,TRANS_ID=?,TRANS_DATE=?,BOOKING_RATE=? WHERE CATEGORY=? AND FWC_CONTRACT_NO=? ");
        logger.info("Output of query:");
        loggableStatement.setString(1, fwdContractVO.getSubProduct());
        loggableStatement.setString(2, fwdContractVO.getBranchCode());
        loggableStatement.setString(3, fwdContractVO.getAcctNumber());
        loggableStatement.setString(4, fwdContractVO.getDealCurrency());
        loggableStatement.setString(5, fwdContractVO.getBookingDate());
        loggableStatement.setString(6, fwdContractVO.getFwdContractAmt());
        loggableStatement.setString(7, fwdContractVO.getToCurrencyAmt());
        loggableStatement.setString(8, fwdContractVO.getDealValidFromDate());
        loggableStatement.setString(9, fwdContractVO.getDealValidToDate());
        loggableStatement.setString(10, fwdContractVO.getTreasuryRefNo());
        loggableStatement.setString(11, fwdContractVO.getTreasuryRate());
        loggableStatement.setString(12, fwdContractVO.getOutstandingAmt());
       
        loggableStatement.setString(13, fwdContractVO.getLimitID());
        loggableStatement.setString(14, fwdContractVO.getWithoutLimit());
        loggableStatement.setString(15, fwdContractVO.getAvailableLimit());
       
        loggableStatement.setString(16, fwdContractVO.getWashRate());
        loggableStatement.setString(17, fwdContractVO.getLeiNumber());
        loggableStatement.setString(18, fwdContractVO.getPlAmount());
        loggableStatement.setString(19, fwdContractVO.getChargeAmount());
        loggableStatement.setString(20, fwdContractVO.getGstAmount());
        loggableStatement.setString(21, fwdContractVO.getInstructions());
        loggableStatement.setString(22, fwdContractVO.getMargin());
        loggableStatement.setString(23, status);
        loggableStatement.setString(24, userID.trim());
        loggableStatement.setString(25, action);
       





        loggableStatement.setString(26, fwdContractVO.getCancellationamount());
        loggableStatement.setString(27, fwdContractVO.getTransid());
        loggableStatement.setString(28, fwdContractVO.getTransdate());
        loggableStatement.setString(29, fwdContractVO.getBookingrate());
        loggableStatement.setString(30, category);
        loggableStatement.setString(31, fwdContractVO.getFwdContractNo());
       

        logger.info("Update Query----------------->" + loggableStatement.getQueryString());
       
        int count = loggableStatement.executeUpdate();
        if (count > 0)
        {
          logger.info("Updated Successfully");
          fwdContractVO.setCount(count);
        }
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      logger.info("Inside exception cancel fwc : " + e.getMessage());
    }
    finally
    {
      DBConnectionUtility.surrenderDB(connection, loggableStatement, null);
    }
    return fwdContractVO;
  }
 
  public ForwardContractVO insertCancelDetails(ForwardContractVO fwdContractVO, String category, String status, String action)
    throws DAOException
  {
    Connection connection = null;
    LoggableStatement loggableStatement = null;
    HttpSession session = ServletActionContext.getRequest().getSession();
    HttpServletRequest request = (HttpServletRequest)ActionContext.getContext()
      .get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
    try
    {
      logger.info("Sub product-------------------------------" + fwdContractVO.getSubProduct().trim());
      logger.info("CIF ID-------------------------------" + fwdContractVO.getCustomerID().trim());
      validateBookingDetails(fwdContractVO);
      logger.info("alertMsgArray size " + this.alertMsgArray.size());
      if (this.alertMsgArray.size() == 0)
      {
        String userID = request.getRemoteUser();
        if (userID == null) {
          userID = "SUPERVISOR";
        }
        connection = DBConnectionUtility.getZoneConnection();
        loggableStatement = new LoggableStatement(connection, "INSERT INTO CUSTOM_FWC_DETAILS(CATEGORY,SUB_PRODUCT,CIF_ID,BRANCH,ACCT_NUMBER,DEAL_CCY, \tBOOKING_DATE,FWC_AMOUNT,TO_CCY_AMT,DEAL_VALID_FROM,DEAL_VALID_TO,TREASURY_REF_NO,TREASURY_RATE,OUTSTANDING_AMT,LIMIT_ID,WITHOUT_LIMIT, \tAVAILABLE_LIMIT,WASH_RATE,LEI_NUMBER,PL_AMOUNT,CHARGE_AMOUNT,GST_AMOUNT,INSTRUCTIONS,MARGIN,STATUS,INPUT_BY,INPUT_TIMESTAMP,LAST_ACTION,FWC_CONTRACT_NO,CANCELLATION_AMOUNT,TRANS_ID, TRANS_DATE, BOOKING_RATE)\tVALUES(?,?,?,?,?,?,TO_DATE(?,'dd/mm/yyyy'),?,?,TO_DATE(?,'dd/mm/yyyy'),TO_DATE(?,'dd/mm/yyyy'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,SYSTIMESTAMP,?,?,?,?,?,?) ");
        loggableStatement.setString(1, category);
        loggableStatement.setString(2, fwdContractVO.getSubProduct());
        loggableStatement.setString(3, fwdContractVO.getCustomerID());
        loggableStatement.setString(4, fwdContractVO.getBranchCode());
        loggableStatement.setString(5, fwdContractVO.getAcctNumber());
        loggableStatement.setString(6, fwdContractVO.getDealCurrency());
        loggableStatement.setString(7, fwdContractVO.getBookingDate());
        loggableStatement.setString(8, fwdContractVO.getFwdContractAmt());
        loggableStatement.setString(9, fwdContractVO.getToCurrencyAmt());
        loggableStatement.setString(10, fwdContractVO.getDealValidFromDate());
        loggableStatement.setString(11, fwdContractVO.getDealValidToDate());
        loggableStatement.setString(12, fwdContractVO.getTreasuryRefNo());
       




        loggableStatement.setString(13, fwdContractVO.getTreasuryRate());
        loggableStatement.setString(14, fwdContractVO.getOutstandingAmt());
       
        loggableStatement.setString(15, fwdContractVO.getLimitID());
        loggableStatement.setString(16, fwdContractVO.getWithoutLimit());
        loggableStatement.setString(17, fwdContractVO.getAvailableLimit());
       
        loggableStatement.setString(18, fwdContractVO.getWashRate());
        loggableStatement.setString(19, fwdContractVO.getLeiNumber());
        loggableStatement.setString(20, fwdContractVO.getPlAmount());
        loggableStatement.setString(21, fwdContractVO.getChargeAmount());
        loggableStatement.setString(22, fwdContractVO.getGstAmount());
        loggableStatement.setString(23, fwdContractVO.getInstructions());
        loggableStatement.setString(24, fwdContractVO.getMargin());
        loggableStatement.setString(25, status);
        loggableStatement.setString(26, userID.trim());
        loggableStatement.setString(27, action);
        loggableStatement.setString(28, fwdContractVO.getFwdContractNo());
       
        loggableStatement.setString(29, fwdContractVO.getCancellationamount());
        loggableStatement.setString(30, fwdContractVO.getTransid());
        loggableStatement.setString(31, fwdContractVO.getTransdate());
        loggableStatement.setString(32, fwdContractVO.getBookingrate());
       
        logger.info("Insert Query for cancel booking----------------->" + loggableStatement.getQueryString());
       
        int count = loggableStatement.executeUpdate();
        if (count > 0)
        {
          logger.info("Inserted Successfully");
          fwdContractVO.setCount(count);
        }
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(connection, loggableStatement, null);
    }
    return fwdContractVO;
  }
 
  public String getBookingTreasuryrate(String forwardContractNo)
  {
    logger.info("Entering Method");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    String treasuryrate = null;
    try
    {
      logger.info("Enter into getBookingTreasuryrate");
      con = DBConnectionUtility.getZoneConnection();
      String query = "SELECT TREASURY_RATE FROM CUSTOM_FWC_DETAILS WHERE FWC_CONTRACT_NO='" +
        forwardContractNo.trim() + "' AND CATEGORY ='FWCBOOK'";
      pst = new LoggableStatement(con, query);
      rs = pst.executeQuery();
      while (rs.next()) {
        treasuryrate = rs.getString("TREASURY_RATE");
      }
      logger.info("Exit getBookingTreasuryrate treasuryrate= " + treasuryrate);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    logger.info("Exiting Method");
    return treasuryrate;
  }
 
  public String getBookingAmount(String forwardContractNo)
  {
    logger.info("Entering Method");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    String amt = null;
    try
    {
      logger.info("Enter into getBookingAmount");
      con = DBConnectionUtility.getZoneConnection();
      String query = "SELECT FWC_AMOUNT FROM CUSTOM_FWC_DETAILS WHERE FWC_CONTRACT_NO='" +
        forwardContractNo.trim() + "' AND CATEGORY ='FWCBOOK'";
      pst = new LoggableStatement(con, query);
      rs = pst.executeQuery();
      while (rs.next()) {
        amt = rs.getString("FWC_AMOUNT");
      }
      logger.info("Enter getBookingAmount amt= " + amt);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    logger.info("Exiting Method");
    return amt;
  }
 
  public String getBuyOrSellAmount(String forwardContractNo, String treasuryRefNo, String buysell)
  {
    logger.info("Entering Method");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    String amt = null;
    try
    {
      logger.info("Enter into getBuyOrSellAmount");
      ServiceUtility.getProperties();
      treasuryHDDTableName = (String)ServiceUtility.TBProperties.get("TreasuryHDDTable");
      con = DBConnectionUtility.getZoneConnection();
      String query = "";
      if (buysell.equalsIgnoreCase("S")) {
        query =
          "SELECT BUY_AMOUNT AS FWC_AMOUNT FROM " + treasuryHDDTableName + " WHERE FWC_REF_NUM='" + forwardContractNo.trim() + "' AND REFERENCE_NUM= '" + treasuryRefNo.trim() + "' AND HOST_DEAL_CATEGORY ='FWCCANCEL'";
      } else if ((buysell.equalsIgnoreCase("B")) || (buysell.equalsIgnoreCase("P"))) {
        query =
          "SELECT SELL_AMOUNT AS FWC_AMOUNT FROM " + treasuryHDDTableName + " WHERE FWC_REF_NUM='" + forwardContractNo.trim() + "' AND REFERENCE_NUM= '" + treasuryRefNo.trim() + "' AND HOST_DEAL_CATEGORY ='FWCCANCEL'";
      }
      pst = new LoggableStatement(con, query);
      logger.info("Query= " + pst.getQueryString());
      rs = pst.executeQuery();
      while (rs.next()) {
        amt = rs.getString("FWC_AMOUNT");
      }
      logger.info("Exit getBuyOrSellAmount amt= " + amt);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    logger.info("Exiting Method");
    return amt;
  }
 
  public String getRateForConversion(String currency, String buysell)
  {
    logger.info("Entering Method");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    String rate = null;
    try
    {
      logger.info("Enter into getRateForConversion");
      con = DBConnectionUtility.getZoneConnection();
      String query = "";
      if (buysell.equalsIgnoreCase("S")) {
        query = "SELECT SELLEX99 AS CUR_RATE FROM FXRATE86 WHERE CURREN49='" + currency.trim() + "'";
      } else if (buysell.equalsIgnoreCase("B")) {
        query = "SELECT BUYEXC03 AS CUR_RATE FROM FXRATE86 WHERE CURREN49='" + currency.trim() + "'";
      }
      pst = new LoggableStatement(con, query);
      rs = pst.executeQuery();
      while (rs.next()) {
        rate = rs.getString("CUR_RATE");
      }
      logger.info("Exiting into getRateForConversion rate= " + rate);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    logger.info("Exiting Method");
    return rate;
  }
 
  public String getLimitNodeForBooking(String forwardContractNo)
  {
    logger.info("Entering Method");
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
 
  public ForwardContractVO validateBookingDetails(ForwardContractVO fwdContractVO)
  {
    String customerID = "";
    String subProduct = "";
    String fwdContractAmtCcy = "";
    String fwdContractAmt = "";
    String fwdContractCcy = "";
    String treasuryRate = "";
    String treRefNo = "";
    String branch = "";
    String bookingDate = "";
    String limitID = "";
    String toAmtCcy = "";
    String toCcy = "";
    String toAmount = "";
    String chargeAmountCcy = "";
    String gstAmountCcy = "";
    String chargeAmt = "";
    String chargeCcy = "";
    String gstAmt = "";
    String gstCcy = "";
    String customerAcctNo = "";
    String washRate = "";
    String outstandingamt = "";
    String outstandingamtCcy = "";
    AvailBalAuthCheckUtility accountBalance = new AvailBalAuthCheckUtility();
    String balance = "";
    String msgId = DateTimeUtil.getSqlLocalDateTime().toString();
    msgId = msgId.replaceAll("[- :.]", "");
    String totalAmt = "";
    String outstandingccy = "";
    String cancellationamountccy = "";
    try
    {
      StaticDataVO staticdatavo = new StaticDataVO();
      String screenType = fwdContractVO.getScreenType();
     
      logger.info("Inside validate of screen --> " + screenType);
      if ((this.alertMsgArray != null) &&
        (this.alertMsgArray.size() > 0)) {
        this.alertMsgArray.clear();
      }
      if (CommonMethods.isValidString(fwdContractVO.getCustomerID())) {
        customerID = fwdContractVO.getCustomerID().trim();
      }
      if (CommonMethods.isValidString(fwdContractVO.getFwdContractAmt()))
      {
        fwdContractAmtCcy = fwdContractVO.getFwdContractAmt().trim();
        fwdContractCcy = fwdContractAmtCcy.trim().replaceAll("[^A-Za-z]+", "");
        fwdContractAmt = fwdContractAmtCcy.trim().replaceAll("[^0-9.]", "");
        logger.info("fwdContractAmtCcy --> " + fwdContractAmtCcy + " :: fwdContractCcy --> " + fwdContractCcy + " :: fwdContractAmt --> " + fwdContractAmt);
      }
      if (CommonMethods.isValidString(fwdContractVO.getTreasuryRate())) {
        treasuryRate = fwdContractVO.getTreasuryRate().trim();
      }
      if (CommonMethods.isValidString(fwdContractVO.getSubProduct())) {
        subProduct = fwdContractVO.getSubProduct().trim();
      }
      if (CommonMethods.isValidString(fwdContractVO.getTreasuryRefNo())) {
        treRefNo = fwdContractVO.getTreasuryRefNo().trim();
      }
      if (CommonMethods.isValidString(fwdContractVO.getBranchCode())) {
        branch = fwdContractVO.getBranchCode().trim();
      }
      if (CommonMethods.isValidString(fwdContractVO.getBookingDate())) {
        bookingDate = fwdContractVO.getBookingDate().trim();
      }
      if (CommonMethods.isValidString(fwdContractVO.getLimitID())) {
        limitID = fwdContractVO.getLimitID().trim();
      }
      if (CommonMethods.isValidString(fwdContractVO.getAcctNumber())) {
        customerAcctNo = fwdContractVO.getAcctNumber().trim();
      }
      if (CommonMethods.isValidString(fwdContractVO.getToCurrencyAmt()))
      {
        toAmtCcy = fwdContractVO.getToCurrencyAmt().trim();
        toCcy = toAmtCcy.trim().replaceAll("[^A-Za-z]+", "");
        toAmount = toAmtCcy.trim().replaceAll("[^0-9.]", "");
      }
      if (CommonMethods.isValidString(fwdContractVO.getChargeAmount()))
      {
        chargeAmountCcy = fwdContractVO.getChargeAmount().trim();
        chargeCcy = chargeAmountCcy.trim().replaceAll("[^A-Za-z]+", "");
        chargeAmt = chargeAmountCcy.trim().replaceAll("[^0-9.]", "");
      }
      if (CommonMethods.isValidString(fwdContractVO.getGstAmount()))
      {
        gstAmountCcy = fwdContractVO.getGstAmount().trim();
        gstCcy = gstAmountCcy.trim().replaceAll("[^A-Za-z]+", "");
        gstAmt = gstAmountCcy.trim().replaceAll("[^0-9.]", "");
      }
      if (CommonMethods.isValidString(fwdContractVO.getWashRate())) {
        washRate = fwdContractVO.getWashRate().trim();
      }
      if (screenType.equals("MakerBookingScreen"))
      {
        String treasuryHostDealCategory = getBookHostDealCategoryFromTreasury(treRefNo, customerID);
        if ((treasuryHostDealCategory == null) || (treasuryHostDealCategory.equalsIgnoreCase("null")) || (!treasuryHostDealCategory.equals("FWCBOOK"))) {
          setErrorForFWCDetails("DEAL_CATEGORY_CHECK", fwdContractVO);
        }
      }
      else if (screenType.equals("MakerCancelScreen"))
      {
        String treasuryHostDealCategory = getCancelHostDealCategoryFromTreasury(treRefNo, customerID);
        if ((treasuryHostDealCategory == null) || (treasuryHostDealCategory.equalsIgnoreCase("null")) || (!treasuryHostDealCategory.equals("FWCCANCEL"))) {
          setErrorForFWCDetails("DEAL_CATEGORY_CHECK", fwdContractVO);
        }
      }
      if ((screenType.equals("MakerCancelScreen")) &&
        (!CommonMethods.isValidString(fwdContractVO.getFwdContractNo()))) {
        setErrorForFWCDetails("FWCNO_MANDATORY", fwdContractVO);
      }
      if (!CommonMethods.isValidString(treRefNo)) {
        setErrorForFWCDetails("TRYREFNO_MANDATORY", fwdContractVO);
      }
      if ((screenType.equals("MakerBookingScreen")) && (screenType.equals("MakerCancelScreen")) &&
        (!CommonMethods.isValidString(treRefNo)) &&
        (!CommonMethods.isValidString(staticdatavo.getTreasuryRefNo()))) {
        setErrorForFWCDetails("TRY_TRANSFER", fwdContractVO);
      }
      if (!screenType.equals("MakerCancelScreen"))
      {
        if (!CommonMethods.isValidString(subProduct)) {
          setErrorForFWCDetails("SUBPRODUCT_MANDATORY", fwdContractVO);
        }
        if (!CommonMethods.isValidString(customerID)) {
          setErrorForFWCDetails("CIF_MANDATORY", fwdContractVO);
        }
        if (!CommonMethods.isValidString(treasuryRate)) {
          setErrorForFWCDetails("TRYRATE_MANDATORY", fwdContractVO);
        }
        if (!CommonMethods.isValidString(branch)) {
          setErrorForFWCDetails("BRANCH_MANDATORY", fwdContractVO);
        }
        if (!CommonMethods.isValidString(bookingDate)) {
          setErrorForFWCDetails("BOOKINGDATE_MANDATORY", fwdContractVO);
        }
        if (!CommonMethods.isValidString(customerAcctNo)) {
          setErrorForFWCDetails("ACCTNO_MANDATORY", fwdContractVO);
        }
      }
      if (CommonMethods.isValidString(fwdContractVO.getTransid()))
      {
        String transid = fwdContractVO.getTransid().trim();
        logger.info("transid" + transid);
      }
      if (CommonMethods.isValidString(fwdContractVO.getTransdate()))
      {
        String transdate = fwdContractVO.getTransdate().trim();
        logger.info("transdate" + transdate);
      }
      if (CommonMethods.isValidString(fwdContractVO.getBookingrate()))
      {
        String bookingrate = fwdContractVO.getTransdate().trim();
        logger.info("bookingrate:" + bookingrate);
      }
      if (CommonMethods.isValidString(fwdContractAmtCcy))
      {
        logger.info("FWC Currency ------------>" + fwdContractAmtCcy);
        if ((!(fwdContractCcy instanceof String)) || (fwdContractCcy.trim().equalsIgnoreCase(""))) {
          setErrorForFWCDetails("DEALCCY_MANDATORY", fwdContractVO);
        } else if (!executeGenericQuery("select COUNT(1) from c8pf where TRIM(C8CCY)=?", fwdContractCcy)) {
          setErrorForFWCDetails("INVALID_CURRENCY", fwdContractVO);
        }
        if (((fwdContractAmt instanceof String)) && (!fwdContractAmt.trim().equalsIgnoreCase("")))
        {
          if (Double.valueOf(fwdContractAmt).doubleValue() <= 0.0D) {
            setErrorForFWCDetails("AMOUNT_VALUE", fwdContractVO);
          }
        }
        else {
          setErrorForFWCDetails("AMOUNT_NULL", fwdContractVO);
        }
      }
      else
      {
        setErrorForFWCDetails("AMOUNT_NULL", fwdContractVO);
      }
      if ((CommonMethods.isValidString(customerID)) &&
        (!executeGenericQuery("select COUNT(1) from GFPF  WHERE TRIM(GFCPNC) = ?", customerID))) {
        setErrorForFWCDetails("INVALID_CUSTOMER", fwdContractVO);
      }
      logger.info("Normal Validations--------------");
      if ((!screenType.equals("MakerCancelScreen")) && (CommonMethods.isValidString(treRefNo)) &&
        (CommonMethods.isValidString(customerID)) && (CommonMethods.isValidString(subProduct)))
      {
        fwdContractVO = fetchDependentTreasuryDetails(fwdContractVO);
        String rateStatus = fwdContractVO.getRateStatus();
        if ((CommonMethods.isValidString(rateStatus)) && (rateStatus.trim().equalsIgnoreCase("D")))
        {
          setErrorForFWCDetails("FINACLE_SERVICE_DOWN", fwdContractVO);
        }
        else if ((CommonMethods.isValidString(rateStatus)) && (rateStatus.trim().equalsIgnoreCase("F")))
        {
          setErrorForFWCDetails("INVALID_TREASURY_REF_NO", fwdContractVO);
        }
        else if ((CommonMethods.isValidString(rateStatus)) &&
          (rateStatus.trim().equalsIgnoreCase("NoBal")))
        {
          setErrorForFWCDetails("INSUFFICIENT_BALANCE", fwdContractVO);
        }
        else if ((CommonMethods.isValidString(rateStatus)) && (rateStatus.trim().equalsIgnoreCase("S")))
        {
          String rateBuyOrSell = fwdContractVO.getRateBuyOrSell();
          if (CommonMethods.isValidString(rateBuyOrSell))
          {
            if (((rateBuyOrSell.trim().equalsIgnoreCase("B")) || (rateBuyOrSell.trim().equalsIgnoreCase("P"))) &&
              (subProduct.contains("Sale"))) {
              setErrorForFWCDetails("INVALID_SUBPRODUCT_S", fwdContractVO);
            }
            if ((rateBuyOrSell.trim().equalsIgnoreCase("S")) && (subProduct.contains("Purchase"))) {
              setErrorForFWCDetails("INVALID_SUBPRODUCT_P", fwdContractVO);
            }
          }
        }
      }
      logger.info("Validation to check available balance--------------");
      if (((screenType.equals("MakerBookingScreen")) || (screenType.equals("MakerCancelScreen"))) &&
        (CommonMethods.isValidString(treRefNo)) && (CommonMethods.isValidString(customerID)) &&
        (CommonMethods.isValidString(subProduct)))
      {
        balance = accountBalance.getAccountBalance("0", msgId, "account", fwdContractVO.getAcctNumber(), "");
        logger.info("Account Balance availiable for account number " + fwdContractVO.getAcctNumber() + " is " +
          balance);
        if (CommonMethods.isValidString(fwdContractVO.getChargeAmount()))
        {
          chargeAmountCcy = fwdContractVO.getChargeAmount().trim();
          chargeCcy = chargeAmountCcy.trim().replaceAll("[^A-Za-z]+", "");
          chargeAmt = chargeAmountCcy.trim().replaceAll("[^0-9.]", "");
        }
        if (CommonMethods.isValidString(fwdContractVO.getGstAmount()))
        {
          gstAmountCcy = fwdContractVO.getGstAmount().trim();
          gstCcy = gstAmountCcy.trim().replaceAll("[^A-Za-z]+", "");
          gstAmt = gstAmountCcy.trim().replaceAll("[^0-9.]", "");
        }
        if ((CommonMethods.isValidString(chargeAmountCcy)) && (CommonMethods.isValidString(gstAmountCcy)) &&
          (CommonMethods.isValidString(chargeCcy)) && (CommonMethods.isValidString(gstCcy)) &&
          (chargeCcy.equalsIgnoreCase(gstCcy))) {
          totalAmt = new BigDecimal(chargeAmt).add(new BigDecimal(gstAmt)).toString();
        }
        logger.info("totalAmt :: " + totalAmt);
       
        int balanceCompare = new BigDecimal(totalAmt).compareTo(new BigDecimal(balance));
       
        logger.info("balanceCompare :: " + balanceCompare);
        if (balanceCompare == 1) {
          fwdContractVO.setRateStatus("AB" + balance.toString());
        }
        String rateStatus = fwdContractVO.getRateStatus();
        if ((CommonMethods.isValidString(rateStatus)) && (rateStatus.trim().contains("AB")))
        {
          logger.info("rate Status--" + rateStatus);
          Object[] arg = { Integer.valueOf(0), "E", "Insufficient Balance for customer account " +
            fwdContractVO.getAcctNumber() + ". Available balance is:" + rateStatus.substring(2),
            "INPUT" };
          CommonMethods.setErrorvalues(arg, this.alertMsgArray);
          if (this.alertMsgArray.size() > 0) {
            fwdContractVO.setErrorList(this.alertMsgArray);
          }
        }
      }
      logger.info("Validation for deal type--------------");
      if (((screenType.equals("MakerBookingScreen")) || (screenType.equals("MakerCancelScreen"))) &&
        (CommonMethods.isValidString(treRefNo)) && (CommonMethods.isValidString(customerID)) &&
        (CommonMethods.isValidString(subProduct)))
      {
        HashMap map = checkDealUtilization(treRefNo, fwdContractVO.getCustomerID(), fwdContractVO.getFwdContractNo());
        String flag = map.get("errormsg").toString();
        if ((flag != null) && (!"Y".equalsIgnoreCase(flag))) {
          fwdContractVO.setRateStatus(flag + map.get("fwcnum"));
        } else {
          fwdContractVO.setRateStatus(flag);
        }
        String rateStatus = fwdContractVO.getRateStatus();
        if ((CommonMethods.isValidString(rateStatus)) && (rateStatus.trim().contains("AU")))
        {
          String fwc_Status = "";
          String fwc_UtilizedContractNo = "";
          String fwc_StatusNContract = checkFWC_Status(treRefNo, fwdContractVO.getCustomerID(), fwdContractVO.getFwdContractNo());
          logger.info("fwc_StatusNContract--" + fwc_StatusNContract);
          String[] fwc_StatusNContractSplit = fwc_StatusNContract.split("-");
          fwc_Status = fwc_StatusNContractSplit[0].toString();
          logger.info("fwc_Status--" + fwc_Status);
          fwc_UtilizedContractNo = fwc_StatusNContractSplit[1].toString();
          logger.info("fwc_UtilizedContractNo--" + fwc_UtilizedContractNo);
          if (!fwc_Status.equalsIgnoreCase("PENDING TO SUBMIT"))
          {
            logger.info("rate Status--" + rateStatus);
            Object[] arg = { Integer.valueOf(0), "E",
              "Deal already used against Forward Reference Number:" + fwc_UtilizedContractNo, "INPUT" };
            CommonMethods.setErrorvalues(arg, this.alertMsgArray);
            if (this.alertMsgArray.size() > 0) {
              fwdContractVO.setErrorList(this.alertMsgArray);
            }
          }
        }
        else if ((CommonMethods.isValidString(rateStatus)) && (rateStatus.trim().contains("AR")))
        {
          logger.info("rate Status--" + rateStatus);
          setErrorForFWCDetails("REJECTED_DEAL", fwdContractVO);
        }
      }
      if ((screenType.equals("MakerCancelScreen")) && (CommonMethods.isValidString(treRefNo)))
      {
        String cancelamtccy = fwdContractVO.getOutstandingAmt();
        String outstandingamountccy = fwdContractVO.getCancellationamount();
        if ((CommonMethods.isValidString(cancelamtccy)) && (CommonMethods.isValidString(outstandingamountccy)))
        {
          String cancelamt = cancelamtccy.trim().replaceAll("[^0-9.]", "");
         

          String outstandamt = outstandingamountccy.trim().replaceAll("[^0-9.]", "");
         


          BigDecimal outstandamount = new BigDecimal(outstandamt).setScale(4, RoundingMode.HALF_UP);
          BigDecimal cancelamount = new BigDecimal(cancelamt).setScale(4, RoundingMode.HALF_UP);
         
          logger.info(
            "outstandingamount.compareTo(cancelamount) : " + outstandamount.compareTo(cancelamount));
          if (outstandamount.compareTo(cancelamount) == -1) {
            setErrorForFWCDetails("NOT_VALID",
              fwdContractVO);
          } else {
            logger.info("Valid amount");
          }
        }
      }
      logger.info("fwdContractAmtCcy --> " + fwdContractAmtCcy + " :: toAmtCcy --> " + toAmtCcy + " ::Treasury Rate --> " + treasuryRate);
      if ((CommonMethods.isValidString(fwdContractAmtCcy)) && (CommonMethods.isValidString(treasuryRate)) &&
        (CommonMethods.isValidString(toAmtCcy)))
      {
        BigDecimal toAmtValue = new BigDecimal(fwdContractAmt).multiply(new BigDecimal(treasuryRate));
       
        String toCcyAmtValue = toAmtValue + " " + toCcy;
        logger.info("ToCcyAmtValue --> " + toCcyAmtValue);
        fwdContractVO.setToCurrencyAmt(toCcyAmtValue);
      }
      if ((CommonMethods.isValidString(toCcy)) && (!toCcy.equalsIgnoreCase("INR")) &&
        (!CommonMethods.isValidString(washRate)))
      {
        logger.info("validate wash rate mandatory for " + toCcy);
        setErrorForFWCDetails("WASHRATE_MANDATORY", fwdContractVO);
      }
      if ((CommonMethods.isValidString(chargeAmountCcy)) && (!CommonMethods.isValidString(gstAmountCcy))) {
        setErrorForFWCDetails("INPUT_GST_AMT", fwdContractVO);
      }
      if ((CommonMethods.isValidString(gstAmountCcy)) && (!CommonMethods.isValidString(chargeAmountCcy))) {
        setErrorForFWCDetails("INPUT_CHARGE_AMT", fwdContractVO);
      }
      if ((CommonMethods.isValidString(chargeAmountCcy)) && (CommonMethods.isValidString(gstAmountCcy)) &&
        (!chargeCcy.equalsIgnoreCase(gstCcy))) {
        setErrorForFWCDetails("INVALID_CHARGE_GST_CCY", fwdContractVO);
      }
      if ((CommonMethods.isValidString(chargeAmountCcy)) && (CommonMethods.isValidString(gstAmountCcy)) &&
        (!CommonMethods.isValidString(customerAcctNo))) {
        setErrorForFWCDetails("INPUT_ACCOUNT_NO", fwdContractVO);
      }
      int accountCheck = checkAccNo(fwdContractVO.getAcctNumber(), fwdContractVO.getCustomerID());
      if (accountCheck == 0) {
        setErrorForFWCDetails("INVALID_ACCOUNT_NO", fwdContractVO);
      }
      if ((CommonMethods.isValidString(chargeAmountCcy)) && (!CommonMethods.isValidString(chargeCcy))) {
        setErrorForFWCDetails("CHARGE_CCY_NULL", fwdContractVO);
      }
      if ((CommonMethods.isValidString(gstAmountCcy)) && (!CommonMethods.isValidString(gstCcy))) {
        setErrorForFWCDetails("GST_CCY_NULL", fwdContractVO);
      }
      if ((CommonMethods.isValidString(chargeCcy)) && (!executeGenericQuery("select COUNT(1) from c8pf where TRIM(C8CCY)=?", chargeCcy.trim()))) {
        setErrorForFWCDetails("INVALID_CURRENCY", fwdContractVO);
      }
      if ((CommonMethods.isValidString(gstCcy)) && (!executeGenericQuery("select COUNT(1) from c8pf where TRIM(C8CCY)=?", gstCcy.trim()))) {
        setErrorForFWCDetails("INVALID_CURRENCY", fwdContractVO);
      }
      logger.info("Validation ends");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return fwdContractVO;
  }
 
  public ForwardContractVO generateFWCPostings(ForwardContractVO fwdContractVO)
  {
    ResultSet rs1 = null;
    Statement st1 = null;
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    ArrayList<FWCPostingVO> postingVO = new ArrayList();
    String query = "SELECT TYPE,ACCOUNT_NUMBER,DR_CR_FLAG,DESCRIPTION FROM CUSTOM_FWC_GL_ACCOUNTS WHERE SUBPRODUCT LIKE '%'||?||'%' AND ACCOUNT_NUMBER IN ('4220013000', 'CustomerAccount')";
    String subProduct = "";
    String branch = "";
    String toAmtCcy = "";
    String toCcy = "";
    String toCcyToCheck = "";
    String toAmount = "";
    String chargeAmountCcy = "";
    String gstAmountCcy = "";
    String chargeAmt = "";
    String chargeCcy = "";
    String gstAmt = "";
    String gstCcy = "";
    String customerAcctNo = "";
    String totalAmt = "";
    String washRate = "";
    try
    {
      logger.info("Inside generateFWCPostings ");
      if (CommonMethods.isValidString(fwdContractVO.getSubProduct())) {
        subProduct = fwdContractVO.getSubProduct().trim();
      }
      if (CommonMethods.isValidString(fwdContractVO.getBranchCode())) {
        branch = fwdContractVO.getBranchCode().trim();
      }
      String forwardContractNo = fwdContractVO.getFwdContractNo();
      BigDecimal postingamount = new BigDecimal(0);
      String treasuryrate = getBookingTreasuryrate(forwardContractNo);
      logger.info("treasuryrate:" + treasuryrate);
      if ((CommonMethods.isValidString(fwdContractVO.getOutstandingAmt())) &&
        (CommonMethods.isValidString(treasuryrate)))
      {
        toAmtCcy = fwdContractVO.getOutstandingAmt().trim();
        logger.info("toAmtCcy:" + toAmtCcy);
        toCcy = toAmtCcy.trim().replaceAll("[^A-Za-z]+", "");
        toCcyToCheck = toAmtCcy.trim().replaceAll("[^A-Za-z]+", "");
        logger.info("toCcy:" + toCcy);
        toAmount = toAmtCcy.trim().replaceAll("[^0-9.]", "");
        logger.info("toAmount:" + toAmount);
        try
        {
          if (!toCcy.equalsIgnoreCase("INR")) {
            toCcy = "INR";
          }
        }
        catch (Exception e)
        {
          logger.info(e);
        }
        postingamount = new BigDecimal(toAmount).multiply(new BigDecimal(treasuryrate)).setScale(4,
          RoundingMode.HALF_UP);
        logger.info("postingMOUNT:" + postingamount + toCcy);
      }
      logger.info(" postingMOUNT & toCcy -->" + postingamount + " " + toCcy);
      if (CommonMethods.isValidString(fwdContractVO.getChargeAmount()))
      {
        chargeAmountCcy = fwdContractVO.getChargeAmount().trim();
        chargeCcy = chargeAmountCcy.trim().replaceAll("[^A-Za-z]+", "");
        chargeAmt = chargeAmountCcy.trim().replaceAll("[^0-9.]", "");
      }
      if (CommonMethods.isValidString(fwdContractVO.getGstAmount()))
      {
        gstAmountCcy = fwdContractVO.getGstAmount().trim();
        gstCcy = gstAmountCcy.trim().replaceAll("[^A-Za-z]+", "");
        gstAmt = gstAmountCcy.trim().replaceAll("[^0-9.]", "");
      }
      if (CommonMethods.isValidString(fwdContractVO.getAcctNumber())) {
        customerAcctNo = fwdContractVO.getAcctNumber().trim();
      }
      if ((CommonMethods.isValidString(chargeAmountCcy)) && (CommonMethods.isValidString(gstAmountCcy)) &&
        (CommonMethods.isValidString(chargeCcy)) && (CommonMethods.isValidString(gstCcy)) &&
        (chargeCcy.equalsIgnoreCase(gstCcy))) {
        totalAmt = new BigDecimal(chargeAmt).add(new BigDecimal(gstAmt)).toString();
      }
      if (CommonMethods.isValidString(fwdContractVO.getWashRate())) {
        washRate = fwdContractVO.getWashRate().trim();
      }
      logger.info(" Wash rate is : " + washRate);
      if ((CommonMethods.isValidString(toCcyToCheck)) && (!toCcyToCheck.equalsIgnoreCase("INR")) &&
        (CommonMethods.isValidString(washRate)))
      {
        toAmount = CommonMethods.getEquivalentINRAmount("INR", toAmount, washRate);
        logger.info(" toAmount * washrate -->" + postingamount);
        postingamount = new BigDecimal(
          CommonMethods.getEquivalentINRAmount("INR", postingamount.toString(), washRate)).setScale(4,
          RoundingMode.HALF_UP);
        logger.info(" postingamount * washrate -->" + postingamount);
        toCcy = "INR";
      }
      if ((CommonMethods.isValidString(chargeCcy)) && (!chargeCcy.equalsIgnoreCase("INR")) &&
        (CommonMethods.isValidString(washRate)))
      {
        totalAmt = CommonMethods.getEquivalentINRAmount("INR", totalAmt, washRate);
        chargeCcy = "INR";
      }
      if ((CommonMethods.isValidString(totalAmt)) && (CommonMethods.isValidString(customerAcctNo))) {
        query = query + " OR TYPE LIKE '%Charges%' ";
      }
      logger.info(" subProduct -->" + subProduct);
      String tiSystemDate = CommonMethods.getTISystemDate();
     
      con = DBConnectionUtility.getZoneConnection();
      pst = new LoggableStatement(con, query);
      pst.setString(1, subProduct);
      logger.info(pst.getQueryString());
      rs = pst.executeQuery();
      while (rs.next())
      {
        FWCPostingVO fwcPostingVO = new FWCPostingVO();
        if (rs.getString("ACCOUNT_NUMBER").equalsIgnoreCase("CustomerAccount")) {
          fwcPostingVO.setPostingAcctNumber(customerAcctNo);
        } else {
          fwcPostingVO.setPostingAcctNumber(branch + rs.getString("ACCOUNT_NUMBER"));
        }
        fwcPostingVO.setPostingDrCrFlag(rs.getString("DR_CR_FLAG"));
        if (rs.getString("TYPE").equalsIgnoreCase("Charges"))
        {
          fwcPostingVO.setPostingAmountCcy(totalAmt + " " + chargeCcy);
        }
        else if ((rs.getString("TYPE").equalsIgnoreCase("Charges")) && (postingamount != null))
        {
          fwcPostingVO.setPostingAmountCcy(postingamount + " " + toCcy);
        }
        else
        {
          logger.info("toAmount & toCcy:" + postingamount + toCcy);
          fwcPostingVO.setPostingAmountCcy(postingamount + " " + toCcy);
        }
        fwcPostingVO.setPostingValueDate(tiSystemDate);
        if (rs.getString("DESCRIPTION").equalsIgnoreCase("Customer Account"))
        {
          String acctName = null;
          st1 = con.createStatement();
          String query1 = "SELECT cast(SHORTNAME as varchar2(15)) AS SHORTNAME FROM ACCOUNT WHERE  CURRENCY = 'INR' AND BO_ACCTNO='" + customerAcctNo + "'";
          rs1 = st1.executeQuery(query1);
          if (rs1.next())
          {
            acctName = rs1.getString("SHORTNAME");
            logger.info(" Account Name " + acctName);
          }
          if ((acctName.equalsIgnoreCase("")) || (acctName.equalsIgnoreCase("null")) || (acctName == null)) {
            fwcPostingVO.setPostingDesc(rs.getString("DESCRIPTION"));
          } else {
            fwcPostingVO.setPostingDesc(acctName);
          }
        }
        else
        {
          fwcPostingVO.setPostingDesc(rs.getString("DESCRIPTION"));
        }
        postingVO.add(fwcPostingVO);
      }
      logger.info(" postingVO size in generateFWCPostings -->" + postingVO.size());
      if (postingVO.size() > 0) {
        fwdContractVO.setPostingList(postingVO);
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(null, st1, rs1);
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    logger.info("Exiting Method");
    return fwdContractVO;
  }
 
  public int checkAccNo(String customerAcctNo, String customerId)
  {
    logger.info("Entering Method");
    int cnt = 0;
    Connection con = null;
    ResultSet rs1 = null;
    Statement st1 = null;
    try
    {
      con = DBConnectionUtility.getZoneConnection();
      st1 = con.createStatement();
      String query1 = "SELECT count(*) AS CNT FROM ACCOUNT WHERE TRIM(BO_ACCTNO)='" + customerAcctNo + "' AND TRIM(CUS_MNM)='" + customerId + "'";
      rs1 = st1.executeQuery(query1);
      if (rs1.next()) {
        cnt = rs1.getInt("CNT");
      }
      logger.info(" Account Cnt " + cnt);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, st1, rs1);
    }
    logger.info("Exiting Method");
    return cnt;
  }
 
  public ForwardContractVO getFWCPostingsToReverse(ForwardContractVO fwdContractVO)
  {
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    ArrayList<FWCPostingVO> postingVO = new ArrayList();
    String query = "SELECT TYPE,ACCOUNT_NUMBER,DR_CR_FLAG,DESCRIPTION FROM CUSTOM_FWC_GL_ACCOUNTS WHERE SUBPRODUCT LIKE '%'||?||'%' AND ACCOUNT_NUMBER IN ('4220013000', 'CustomerAccount')";
    String subProduct = "";
    String branch = "";
    String toAmtCcy = "";
    String toCcy = "";
    String toAmount = "";
    String chargeAmountCcy = "";
    String gstAmountCcy = "";
    String chargeAmt = "";
    String chargeCcy = "";
    String gstAmt = "";
    String gstCcy = "";
    String customerAcctNo = "";
    String totalAmt = "";
    String washRate = "";
    try
    {
      logger.info(" get FWCPostings To Reverse ");
      if (CommonMethods.isValidString(fwdContractVO.getSubProduct())) {
        subProduct = fwdContractVO.getSubProduct().trim();
      }
      if (CommonMethods.isValidString(fwdContractVO.getBranchCode())) {
        branch = fwdContractVO.getBranchCode().trim();
      }
      String forwardContractNo = fwdContractVO.getFwdContractNo();
      BigDecimal postingamount = new BigDecimal(0);
      String treasuryrate = getBookingTreasuryrate(forwardContractNo);
      logger.info("treasuryrate:" + treasuryrate);
      if ((CommonMethods.isValidString(fwdContractVO.getOutstandingAmt())) &&
        (CommonMethods.isValidString(treasuryrate)))
      {
        toAmtCcy = fwdContractVO.getOutstandingAmt().trim();
        logger.info("toAmtCcy:" + toAmtCcy);
        toCcy = toAmtCcy.trim().replaceAll("[^A-Za-z]+", "");
        logger.info("toCcy:" + toCcy);
        toAmount = toAmtCcy.trim().replaceAll("[^0-9.]", "");
        logger.info("toAmount:" + toAmount);
        try
        {
          if (!toCcy.equalsIgnoreCase("INR")) {
            toCcy = "INR";
          }
        }
        catch (Exception e)
        {
          logger.info(e);
        }
        postingamount = new BigDecimal(toAmount).multiply(new BigDecimal(treasuryrate)).setScale(4,
          RoundingMode.HALF_UP);
       
        logger.info("postingMOUNT:" + postingamount + toCcy);
      }
      logger.info(" toAmount & toCcy -->" + postingamount + " " + toCcy);
      if (CommonMethods.isValidString(fwdContractVO.getChargeAmount()))
      {
        chargeAmountCcy = fwdContractVO.getChargeAmount().trim();
        chargeCcy = chargeAmountCcy.trim().replaceAll("[^A-Za-z]+", "");
        chargeAmt = chargeAmountCcy.trim().replaceAll("[^0-9.]", "");
      }
      if (CommonMethods.isValidString(fwdContractVO.getGstAmount()))
      {
        gstAmountCcy = fwdContractVO.getGstAmount().trim();
        gstCcy = gstAmountCcy.trim().replaceAll("[^A-Za-z]+", "");
        gstAmt = gstAmountCcy.trim().replaceAll("[^0-9.]", "");
      }
      if (CommonMethods.isValidString(fwdContractVO.getAcctNumber())) {
        customerAcctNo = fwdContractVO.getAcctNumber().trim();
      }
      if ((CommonMethods.isValidString(chargeAmountCcy)) && (CommonMethods.isValidString(gstAmountCcy)) &&
        (CommonMethods.isValidString(chargeCcy)) && (CommonMethods.isValidString(gstCcy)) &&
        (chargeCcy.equalsIgnoreCase(gstCcy))) {
        totalAmt = new BigDecimal(chargeAmt).add(new BigDecimal(gstAmt)).toString();
      }
      if (CommonMethods.isValidString(fwdContractVO.getWashRate())) {
        washRate = fwdContractVO.getWashRate().trim();
      }
      if ((CommonMethods.isValidString(toCcy)) && (!toCcy.equalsIgnoreCase("INR")) &&
        (CommonMethods.isValidString(washRate)))
      {
        toAmount = CommonMethods.getEquivalentINRAmount("INR", toAmount, washRate);
        toCcy = "INR";
      }
      if ((CommonMethods.isValidString(toCcy)) && (!toCcy.equalsIgnoreCase("INR")) &&
        (CommonMethods.isValidString(washRate))) {
        toCcy = "INR";
      }
      if ((CommonMethods.isValidString(toCcy)) && (!toCcy.equalsIgnoreCase("INR")) &&
        (CommonMethods.isValidString(treasuryrate)))
      {
        logger.info("To Amount and Currency:" + toCcy + " " + toAmount);
        toAmount = CommonMethods.getEquivalentINRAmount("INR", String.valueOf(toAmount), treasuryrate);
       
        toCcy = "INR";
      }
      if ((CommonMethods.isValidString(totalAmt)) && (CommonMethods.isValidString(customerAcctNo))) {
        query = query + " OR TYPE LIKE '%Charges%' ";
      }
      logger.info(" subProduct -->" + subProduct);
      String tiSystemDate = CommonMethods.getTISystemDate();
     
      con = DBConnectionUtility.getZoneConnection();
      pst = new LoggableStatement(con, query);
      pst.setString(1, subProduct);
      logger.info(pst.getQueryString());
      rs = pst.executeQuery();
      while (rs.next())
      {
        FWCPostingVO fwcPostingVO = new FWCPostingVO();
        if (rs.getString("ACCOUNT_NUMBER").equalsIgnoreCase("CustomerAccount")) {
          fwcPostingVO.setPostingAcctNumber(customerAcctNo);
        } else {
          fwcPostingVO.setPostingAcctNumber(branch + rs.getString("ACCOUNT_NUMBER"));
        }
        String drCrFlag = rs.getString("DR_CR_FLAG");
        if ((drCrFlag.equalsIgnoreCase("D")) && (!rs.getString("TYPE").equalsIgnoreCase("Charges"))) {
          drCrFlag = "C";
        } else if ((drCrFlag.equalsIgnoreCase("C")) && (!rs.getString("TYPE").equalsIgnoreCase("Charges"))) {
          drCrFlag = "D";
        }
        fwcPostingVO.setPostingDrCrFlag(drCrFlag);
        if (rs.getString("TYPE").equalsIgnoreCase("Charges"))
        {
          logger.info("total amount & charge currency:" + totalAmt + chargeCcy);
          fwcPostingVO.setPostingAmountCcy(totalAmt + " " + chargeCcy);
        }
        else if ((rs.getString("TYPE").equalsIgnoreCase("Charges")) && (postingamount != null))
        {
          fwcPostingVO.setPostingAmountCcy(postingamount + " " + toCcy);
        }
        else
        {
          logger.info("toAmount & toCcy:" + postingamount + toCcy);
          fwcPostingVO.setPostingAmountCcy(postingamount + " " + toCcy);
        }
        fwcPostingVO.setPostingValueDate(tiSystemDate);
        fwcPostingVO.setPostingDesc(rs.getString("DESCRIPTION"));
       
        postingVO.add(fwcPostingVO);
      }
      logger.info(" postingVO size in getFWCPostingsToReverse -->" + postingVO.size());
      if (postingVO.size() > 0) {
        fwdContractVO.setPostingList(postingVO);
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
    logger.info("Exiting Method");
    return fwdContractVO;
  }
 
  public ArrayList<ForwardContractVO> fetchFwdContractDetails(ForwardContractVO fwdContractVO)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement loggableStatement = null;
    ResultSet rs = null;
    String sqlQuery = null;
    ArrayList<ForwardContractVO> fwdContractList = null;
    String query = null;
    String fwdContractNo = null;
    int setValue = 0;
    boolean fwdContractNoFlag = false;
    boolean custIDFlag = false;
    boolean subProductFlag = false;
    boolean branchFlag = false;
    boolean fwdContractAmtFlag = false;
    boolean bookingDateFlag = false;
    boolean acctNoFlag = false;
    boolean statusFlag = false;
    boolean dealCcyFlag = false;
    try
    {
      fwdContractList = new ArrayList();
     
      HttpServletRequest request = (HttpServletRequest)ActionContext.getContext()
        .get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
      String userID = request.getRemoteUser();
      if (userID == null) {
        userID = "SUPERVISOR";
      }
      logger.info("User ID-----------" + userID);
     
      query = "SELECT ID,CATEGORY,FWC_CONTRACT_NO,SUB_PRODUCT,CIF_ID,BRANCH,ACCT_NUMBER,DEAL_CCY,FWC_AMOUNT,TO_CHAR(TO_DATE(BOOKING_DATE,'DD/MM/YY'),'dd/mm/YYYY') AS BOOKING_DATE,TO_CCY_AMT,TO_CHAR(TO_DATE(DEAL_VALID_FROM,'DD/MM/YY'),'dd/mm/YYYY') AS DEAL_VALID_FROM,TO_CHAR(TO_DATE(DEAL_VALID_TO,'DD/MM/YY'),'dd/mm/YYYY') AS DEAL_VALID_TO,TREASURY_REF_NO,TREASURY_RATE,OUTSTANDING_AMT,STATUS FROM CUSTOM_FWC_DETAILS WHERE FWC_CONTRACT_NO IS NOT NULL ";
     
      fwdContractNo = fwdContractVO.getFwdContractNo();
      String subProduct = fwdContractVO.getSubProduct();
      String custID = fwdContractVO.getCustomerID();
      String branch = fwdContractVO.getBranchCode();
      String acctNo = fwdContractVO.getAcctNumber();
      String fwdContractAmt = fwdContractVO.getFwdContractAmt();
      String dealCcy = fwdContractVO.getDealCurrency();
      String bookingDate = fwdContractVO.getBookingDate();
      String status = fwdContractVO.getStatus();
      if (fwdContractVO != null)
      {
        if (!CommonMethods.isNull(fwdContractNo))
        {
          query = query + " AND FWC_CONTRACT_NO LIKE '%'||?||'%'";
          fwdContractNoFlag = true;
        }
        if (!CommonMethods.isNull(subProduct))
        {
          query = query + " AND SUB_PRODUCT LIKE ?";
          subProductFlag = true;
        }
        if (!CommonMethods.isNull(custID))
        {
          query = query + " AND CIF_ID LIKE '%'||?||'%'";
          custIDFlag = true;
        }
        if (!CommonMethods.isNull(branch))
        {
          query = query + " AND BRANCH LIKE '%'||?||'%'";
          branchFlag = true;
        }
        if (!CommonMethods.isNull(acctNo))
        {
          query = query + " AND ACCT_NUMBER LIKE '%'||?||'%'";
          acctNoFlag = true;
        }
        if (!CommonMethods.isNull(fwdContractAmt))
        {
          query = query + " AND FWC_AMOUNT LIKE '%'||?||'%'";
          fwdContractAmtFlag = true;
        }
        if (!CommonMethods.isNull(dealCcy))
        {
          query = query + " AND DEAL_CCY LIKE '%'||?||'%'";
          dealCcyFlag = true;
        }
        if (!CommonMethods.isNull(bookingDate))
        {
          query = query + " AND TO_CHAR(TO_DATE(BOOKING_DATE,'DD/MM/YY'),'dd/mm/YYYY') LIKE '%'||?||'%'";
          bookingDateFlag = true;
        }
        if (!CommonMethods.isNull(status))
        {
          query = query + " AND STATUS LIKE '%'||?||'%'";
          statusFlag = true;
        }
      }
      query = query + " ORDER BY ID DESC";
     
      con = DBConnectionUtility.getZoneConnection();
      loggableStatement = new LoggableStatement(con, query);
      if (fwdContractNoFlag) {
        loggableStatement.setString(++setValue, fwdContractNo.trim());
      }
      if (custIDFlag) {
        loggableStatement.setString(++setValue, custID.trim());
      }
      if (subProductFlag) {
        loggableStatement.setString(++setValue, subProduct.trim());
      }
      if (fwdContractAmtFlag) {
        loggableStatement.setString(++setValue, fwdContractAmt.trim());
      }
      if (bookingDateFlag) {
        loggableStatement.setString(++setValue, bookingDate.trim());
      }
      if (acctNoFlag) {
        loggableStatement.setString(++setValue, acctNo.trim());
      }
      if (branchFlag) {
        loggableStatement.setString(++setValue, branch.trim());
      }
      if (dealCcyFlag) {
        loggableStatement.setString(++setValue, dealCcy.trim());
      }
      if (statusFlag) {
        loggableStatement.setString(++setValue, status.trim());
      }
      logger.info("RetriveDetailsFrom FWC: " + loggableStatement.getQueryString());
     
      rs = loggableStatement.executeQuery();
      while (rs.next())
      {
        fwdContractVO = new ForwardContractVO();
        fwdContractVO.setId(rs.getString("ID"));
        fwdContractVO.setCategory(rs.getString("CATEGORY"));
        fwdContractVO.setFwdContractNo(rs.getString("FWC_CONTRACT_NO"));
        fwdContractVO.setSubProduct(rs.getString("SUB_PRODUCT"));
        fwdContractVO.setCustomerID(rs.getString("CIF_ID"));
        fwdContractVO.setBranchCode(rs.getString("BRANCH"));
        fwdContractVO.setAcctNumber(rs.getString("ACCT_NUMBER"));
        fwdContractVO.setDealCurrency(rs.getString("DEAL_CCY"));
        fwdContractVO.setFwdContractAmt(rs.getString("FWC_AMOUNT"));
        fwdContractVO.setBookingDate(rs.getString("BOOKING_DATE"));
        fwdContractVO.setToCurrencyAmt(rs.getString("TO_CCY_AMT"));
        fwdContractVO.setDealValidFromDate(rs.getString("DEAL_VALID_FROM"));
        fwdContractVO.setDealValidToDate(rs.getString("DEAL_VALID_TO"));
        fwdContractVO.setTreasuryRefNo(rs.getString("TREASURY_REF_NO"));
        fwdContractVO.setTreasuryRate(rs.getString("TREASURY_RATE"));
        fwdContractVO.setOutstandingAmt(rs.getString("OUTSTANDING_AMT"));
        fwdContractVO.setStatus(rs.getString("STATUS"));
        fwdContractList.add(fwdContractVO);
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, loggableStatement, rs);
    }
    logger.info("Exiting Method");
    return fwdContractList;
  }
 
  public ArrayList<ForwardContractVO> fetchFwdContractEnquiryDetails(ForwardContractVO fwdContractVO)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement loggableStatement = null;
    ResultSet rs = null;
    String sqlQuery = null;
    ArrayList<ForwardContractVO> fwdContractList = null;
    String query = null;
    String fwdContractNo = null;
    int setValue = 0;
    boolean fwdContractNoFlag = false;
    boolean custIDFlag = false;
    boolean branchFlag = false;
    boolean startDateFlag = false;
    boolean endDateFlag = false;
    try
    {
      fwdContractList = new ArrayList();
     
      HttpServletRequest request = (HttpServletRequest)ActionContext.getContext()
        .get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
      String userID = request.getRemoteUser();
      if (userID == null) {
        userID = "SUPERVISOR";
      }
      logger.info("User ID-----------" + userID);
     
      query = "SELECT * FROM CUSTOM_FWC_ENQ_DETAILS_VIEW WHERE FWC_REF_NUM IS NOT NULL AND COUNTERPARTY_STRING IS NOT NULL ";
     
      fwdContractNo = fwdContractVO.getFwdContractNo();
      String custID = fwdContractVO.getCustomerID();
      String branch = fwdContractVO.getBranchCode();
      String validFromDate = fwdContractVO.getValidFrom();
      String validToDate = fwdContractVO.getValidTo();
      if (fwdContractVO != null)
      {
        if (!CommonMethods.isNull(fwdContractNo))
        {
          query = query + " AND FWC_REF_NUM LIKE '%'||?||'%'";
          fwdContractNoFlag = true;
        }
        if (!CommonMethods.isNull(custID))
        {
          query = query + " AND COUNTERPARTY_STRING LIKE '%'||?||'%'";
          custIDFlag = true;
        }
        if (!CommonMethods.isNull(branch))
        {
          query = query + " AND SOL_ID LIKE '%'||?||'%'";
          branchFlag = true;
        }
        if (!CommonMethods.isNull(validFromDate))
        {
          query = query + " AND TO_CHAR(TO_DATE(START_DATE,'DD/MM/YY'),'dd/mm/YYYY') LIKE '%'||?||'%'";
          startDateFlag = true;
        }
        if (!CommonMethods.isNull(validToDate))
        {
          query = query + " AND TO_CHAR(TO_DATE(END_DATE,'DD/MM/YY'),'dd/mm/YYYY') LIKE '%'||?||'%'";
          endDateFlag = true;
        }
      }
      query = query + " ORDER BY FWC_REF_NUM DESC";
     
      con = DBConnectionUtility.getZoneConnection();
      loggableStatement = new LoggableStatement(con, query);
      if (fwdContractNoFlag) {
        loggableStatement.setString(++setValue, fwdContractNo.trim());
      }
      if (custIDFlag) {
        loggableStatement.setString(++setValue, custID.trim());
      }
      if (startDateFlag) {
        loggableStatement.setString(++setValue, validFromDate.trim());
      }
      if (endDateFlag) {
        loggableStatement.setString(++setValue, validToDate.trim());
      }
      if (branchFlag) {
        loggableStatement.setString(++setValue, branch.trim());
      }
      logger.info("Retrive enquiry Details From FWC: " + loggableStatement.getQueryString());
     
      rs = loggableStatement.executeQuery();
      while (rs.next())
      {
        fwdContractVO = new ForwardContractVO();
        fwdContractVO.setId(rs.getString("ID"));
        fwdContractVO.setFwdContractNo(rs.getString("FWC_REF_NUM"));
        fwdContractVO.setCategory(rs.getString("HOST_DEAL_CATEGORY"));
        fwdContractVO.setBillId(rs.getString("BILL_ID"));
        fwdContractVO.setCustomerID(rs.getString("COUNTERPARTY_STRING"));
        fwdContractVO.setBranchCode(rs.getString("SOL_ID"));
        fwdContractVO.setBuyOrSell(rs.getString("BUY_OR_SELL"));
        fwdContractVO.setBuyAmount(rs.getString("BUY_AMOUNT") + " " + rs.getString("BUY_AMOUNT_CCY"));
        fwdContractVO.setSellAmount(rs.getString("SELL_AMOUNT") + " " + rs.getString("SELL_AMOUNT_CCY"));
        fwdContractVO.setTranType(rs.getString("TRAN_TYPE"));
       
        HashMap<String, String> purchaseAndSaleAmtMap = getAvailablePurchaseAndSaleAmts(
          fwdContractVO.getFwdContractNo(), fwdContractVO.getCustomerID());
        if ((fwdContractVO.getBuyOrSell().equalsIgnoreCase("B")) ||
          (fwdContractVO.getBuyOrSell().equalsIgnoreCase("P"))) {
          fwdContractVO.setOutstandingAmt(
            (String)purchaseAndSaleAmtMap.get("PurchaseAmount") + " " + rs.getString("BUY_AMOUNT_CCY"));
        }
        if (fwdContractVO.getBuyOrSell().equalsIgnoreCase("S")) {
          fwdContractVO.setOutstandingAmt(
            (String)purchaseAndSaleAmtMap.get("SaleAmount") + " " + rs.getString("SELL_AMOUNT_CCY"));
        }
        fwdContractVO.setDealValidFromDate(rs.getString("START_DATE"));
        fwdContractVO.setDealValidToDate(rs.getString("END_DATE"));
        fwdContractVO.setTreasuryRefNo(rs.getString("REFERENCE_NUM"));
        fwdContractVO.setTreasuryRate(rs.getString("FWD_CONTRACT_RATE"));
        fwdContractVO.setStatus(rs.getString("RECORD_STATUS"));
        fwdContractList.add(fwdContractVO);
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, loggableStatement, rs);
    }
    logger.info("Exiting Method");
    return fwdContractList;
  }
 
  private HashMap<String, String> getAvailablePurchaseAndSaleAmts(String contractRef, String customer)
  {
    ResultSet resultSet = null;
    Connection tiZoneConnection = null;
    PreparedStatement preparedStatement = null;
    String purchaseAmount = "0.0";
    String saleAmount = "0.0";
    HashMap<String, String> aHashMap = new HashMap();
    try
    {
      String availablePurchaseAndSaleAmtsQuery = "SELECT TO_CHAR(SUM(CASE WHEN HOST_DEAL_CATEGORY='FXRATE' THEN BUY_AMOUNT  WHEN HOST_DEAL_CATEGORY='FWCBOOK' THEN BUY_AMOUNT \tWHEN HOST_DEAL_CATEGORY='FWCUTIL' THEN -BUY_AMOUNT \tWHEN HOST_DEAL_CATEGORY='FWCCANCEL' THEN -SELL_AMOUNT END)) AS BUY_AMOUNT,  TO_CHAR(SUM(CASE WHEN HOST_DEAL_CATEGORY='FXRATE' THEN SELL_AMOUNT  WHEN HOST_DEAL_CATEGORY='FWCBOOK' THEN SELL_AMOUNT \tWHEN HOST_DEAL_CATEGORY='FWCUTIL' THEN -SELL_AMOUNT \tWHEN HOST_DEAL_CATEGORY='FWCCANCEL' THEN -BUY_AMOUNT END)) AS SELL_AMOUNT FROM CUSTOM_TREASURY_INSERT_TBL  WHERE RECORD_STATUS <>'DELETED' AND COUNTERPARTY_STRING IS NOT NULL AND FWC_REF_NUM IS NOT NULL  AND FWC_REF_NUM=? AND COUNTERPARTY_STRING =?";
     
      tiZoneConnection = DBConnectionUtility.getZoneConnection();
      preparedStatement = tiZoneConnection.prepareStatement(availablePurchaseAndSaleAmtsQuery);
     
      preparedStatement.setString(1, contractRef.trim());
      preparedStatement.setString(2, customer.trim());
     
      resultSet = preparedStatement.executeQuery();
      while (resultSet.next())
      {
        if (CommonMethods.isValidString(resultSet.getString("BUY_AMOUNT"))) {
          purchaseAmount = resultSet.getString("BUY_AMOUNT");
        }
        if (CommonMethods.isValidString(resultSet.getString("SELL_AMOUNT"))) {
          saleAmount = resultSet.getString("SELL_AMOUNT");
        }
        aHashMap.put("PurchaseAmount", purchaseAmount);
        aHashMap.put("SaleAmount", saleAmount);
       
        logger.info("purchaseAmt and saleAmt from Treasury : " + purchaseAmount + " & " + saleAmount);
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
    return aHashMap;
  }
 
  public ForwardContractVO approveFwdContractDetails(ForwardContractVO fwdContractVO, String category)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement loggableStatement = null;
    ResultSet rs = null;
    ResultSet rs1 = null;
    int records = 0;
   


    Map<String, String> limitexposureTokens = new HashMap();
    Map<String, String> postingTokens = new HashMap();
    Map<String, String> ftrtUpdateTokens = new HashMap();
    Map<String, String> treasUpdateTokens = new HashMap();
    String limitBlockedID = "";
    String postingTranID = "";
    String limitStatus = "S";
    String postingStatus = "";
    String ftrtUpdateStatus = "";
    String treasUpdateStatus = "";
    int insertedCount = 0;
    String seqNo = "";
    String postingTransdate = "";
    AvailBalAuthCheckUtility accountBalance = new AvailBalAuthCheckUtility();
    String balance = "";
    String msgId = DateTimeUtil.getSqlLocalDateTime().toString();
    msgId = msgId.replaceAll("[- :.]", "");
    String chargeAmountCcy = "";
    String chargeCcy = "";
    String chargeAmt = "";
    String gstAmountCcy = "";
    String gstCcy = "";
    String gstAmt = "";
    String totalAmt = "";
    Boolean flag = Boolean.valueOf(true);
    String BlockorUnblockstatus = "LIMITBLOCKED";
   
    String treasuryHDDTableName = ServiceUtility.getBridgePropertyValue("TreasuryHDDTable");
    try
    {
      HttpServletRequest request = (HttpServletRequest)ActionContext.getContext()
        .get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
      String userId = request.getRemoteUser();
      if (userId == null) {
        userId = "SUPERVISOR";
      }
      logger.info("fwdContractVO.getRemarks()=>" + fwdContractVO.getRemarks());
      logger.info("fwdContractVO.getFwdContractNo()==>" + fwdContractVO.getFwdContractNo());
      logger.info("fwdContractVO.getCustomerID()==>" + fwdContractVO.getCustomerID());
      logger.info("fwdContractVO.getScreenType()==>" + fwdContractVO.getScreenType());
      logger.info("ftreasuryHDDTableName==>" + treasuryHDDTableName);
      String treasuryRefNo = fwdContractVO.getTreasuryRefNo().trim();
      String fwdContractNo = fwdContractVO.getFwdContractNo().trim();
      String remarks = fwdContractVO.getRemarks().trim();
      String customer = fwdContractVO.getCustomerID().trim();
      String limitID = fwdContractVO.getLimitID().trim();
      String Id = fwdContractVO.getId().trim();
      String accNo = fwdContractVO.getAcctNumber().trim();
      String whereClause = "";
      if ((customer != null) && (!customer.isEmpty())) {
        whereClause = whereClause + " AND TRIM(COUNTERPARTY_STRING) LIKE '" + customer + "' ";
      }
      if ((treasuryRefNo != null) && (!treasuryRefNo.isEmpty())) {
        whereClause = whereClause + " AND REFERENCE_NUM LIKE '" + treasuryRefNo + "' ";
      }
      if ((fwdContractVO.getBranchCode() != null) && (!fwdContractVO.getBranchCode().isEmpty())) {
        whereClause = whereClause + " AND SOL_ID LIKE '" + fwdContractVO.getBranchCode() + "' ";
      }
      String fxOptionSearchQuery = "SELECT COUNT(*) AS COUNT FROM " + treasuryHDDTableName +
        " WHERE HOST_DEAL_CATEGORY='FXRATE' AND RECORD_STATUS = 'TRANSFER' AND HOST_DEAL_SUB_CATEGORY IN ('FWCBOOK','FXBS')" +
        " AND COUNTERPARTY_STRING IS NOT NULL AND REFERENCE_NUM IS NOT NULL AND FWC_REF_NUM IS NULL " + whereClause;
     


      con = DBConnectionUtility.getZoneConnection();
      loggableStatement = new LoggableStatement(con, fxOptionSearchQuery);
      logger.info(loggableStatement.getQueryString());
      rs1 = loggableStatement.executeQuery();
      while (rs1.next()) {
        records = rs1.getInt(1);
      }
      logger.info("get Role result" + records);
      String bookingdate = fwdContractVO.getBookingDate().trim();
      bookingdate = "20" + bookingdate.substring(6) + "/" + bookingdate.substring(3, 5) + "/" +
        bookingdate.substring(0, 2);
      logger.info("approveFwdContractDetails bookingdate :: " + bookingdate);
     
      String date2 = getTICurrentDateFormat();
      logger.info("approveFwdContractDetails date2 : " + date2);
      if ((bookingdate != null) && (date2 != null) && (bookingdate.compareTo(date2) != 0) && (records > 0) && (flag.booleanValue()))
      {
        String fwdContractAmt = fwdContractVO.getFwdContractAmt();
        fwdContractAmt = fwdContractAmt.trim().replaceAll("[^0-9.]", "");
        String toAmount = fwdContractVO.getToCurrencyAmt().trim();
       
        String limitCcy = toAmount.trim().replaceAll("[^A-Za-z]+", "");
        String limitAmount = toAmount.trim().replaceAll("[^0-9.]", "");
       
        String washRate = fwdContractVO.getWashRate().trim();
        if ((CommonMethods.isValidString(limitCcy)) && (!limitCcy.equalsIgnoreCase("INR")) &&
          (CommonMethods.isValidString(washRate)))
        {
          limitAmount = CommonMethods.getEquivalentINRAmount("INR", limitAmount, washRate);
         

          limitCcy = "INR";
        }
        balance = accountBalance.getAccountBalance("0", msgId, "account", accNo, "");
        logger.info("Account Balance available for account number " + accNo + " is " + balance);
        if (CommonMethods.isValidString(fwdContractVO.getChargeAmount()))
        {
          chargeAmountCcy = fwdContractVO.getChargeAmount().trim();
          chargeCcy = chargeAmountCcy.trim().replaceAll("[^A-Za-z]+", "");
          chargeAmt = chargeAmountCcy.trim().replaceAll("[^0-9.]", "");
        }
        if (CommonMethods.isValidString(fwdContractVO.getGstAmount()))
        {
          gstAmountCcy = fwdContractVO.getGstAmount().trim();
          gstCcy = gstAmountCcy.trim().replaceAll("[^A-Za-z]+", "");
          gstAmt = gstAmountCcy.trim().replaceAll("[^0-9.]", "");
        }
        if ((CommonMethods.isValidString(chargeAmountCcy)) && (CommonMethods.isValidString(gstAmountCcy)) &&
          (CommonMethods.isValidString(chargeCcy)) && (CommonMethods.isValidString(gstCcy)) &&
          (chargeCcy.equalsIgnoreCase(gstCcy))) {
          totalAmt = new BigDecimal(chargeAmt).add(new BigDecimal(gstAmt)).toString();
        }
        int balanceCompare = 0;
        if ((balance != null) && (!balance.isEmpty())) {
          balanceCompare = new BigDecimal(totalAmt).compareTo(new BigDecimal(balance));
        } else {
          balanceCompare = 1;
        }
        logger.info("balanceCompare :: " + balanceCompare);
        if (balanceCompare != 1)
        {
          if (CommonMethods.isValidString(limitID)) {
            try
            {
              limitexposureTokens = LimitBlockUnblockUtil.limitexposurethroughAPI(fwdContractNo, limitID,
                limitAmount, limitCcy, BlockorUnblockstatus, "FWCBOOK");
              limitStatus = ((String)limitexposureTokens.get("LimitBOUStatus")).trim();
              if (limitStatus.equalsIgnoreCase("S")) {
                limitBlockedID = ((String)limitexposureTokens.get("SerialNumber")).trim();
              }
            }
            catch (Exception e)
            {
              e.printStackTrace();
              logger.info("Exception in Limit Block for FWD ref :" + fwdContractVO.getFwdContractNo() +
                " error: " + e.getMessage());
            }
          }
          Map<String, String> insertInFTIStatus = FWCUtil.insertFTIFwdContractDetails(fwdContractVO, userId,
            "FWCBOOK");
          insertedCount = Integer.valueOf((String)insertInFTIStatus.get("Count")).intValue();
          seqNo = (String)insertInFTIStatus.get("SequenceNo");
          if (insertedCount > 0)
          {
            ftrtUpdateTokens = FtrtUpdateUtil.updateUtilizedAmountInFinacle(treasuryRefNo, fwdContractAmt);
            ftrtUpdateStatus = ((String)ftrtUpdateTokens.get("FtrtUpdateStatus")).trim();
           
            treasUpdateTokens = TreasUpdateUtil.updateUtilizationAmountInTreasury(treasuryRefNo,
              fwdContractAmt);
            treasUpdateStatus = ((String)treasUpdateTokens.get("TreasUpdateStatus")).trim();
           
            fwdContractVO = generateFWCPostings(fwdContractVO);
            if (ftrtUpdateStatus.equalsIgnoreCase("S"))
            {
              if ((treasUpdateStatus.equalsIgnoreCase("S")) && (fwdContractVO.getPostingList().size() > 0))
              {
                postingTokens = PostingUtil.releaseTxnPostings("FWCBOOK", fwdContractVO, seqNo);
                if (postingTokens != null) {
                  postingStatus = ((String)postingTokens.get("PostingStatus")).trim();
                } else {
                  postingStatus = "FAILED";
                }
                logger.info("Posting Status -->" + postingStatus);
                if (postingStatus.equalsIgnoreCase("SUCCESS"))
                {
                  postingTranID = ((String)postingTokens.get("TranID")).trim();
                  logger.info("postingTranID:" + postingTranID);
                  postingTransdate = ((String)postingTokens.get("Trandate")).trim();
                  logger.info("postingTransdate:" + postingTransdate);
                  fwdContractVO.setTransid(postingTranID);
                  fwdContractVO.setTransdate(postingTransdate);
                  String updatebooktransdetails = UpdatebookTransdetails(fwdContractVO, postingTranID,
                    postingTransdate);
                  logger.info("updatebooktransdetails:" + updatebooktransdetails);
                 
                  logger.info(" inside fwd contract updation in FTI table");
                 

                  loggableStatement = new LoggableStatement(con, "UPDATE CUSTOM_FWC_DETAILS SET STATUS =?,CHECKER_ACTION_BY=?,CHECKER_ACTION_TIMESTAMP=SYSTIMESTAMP,REMARKS=?,LAST_ACTION=?,LIMIT_SERIAL_NUM=?,POSTING_TRAN_ID=? WHERE CATEGORY=? AND FWC_CONTRACT_NO=? AND ID=? ");
                 
                  loggableStatement.setString(1, "APPROVED");
                  loggableStatement.setString(2, userId.trim());
                  loggableStatement.setString(3, remarks);
                  loggableStatement.setString(4, "Approve");
                  loggableStatement.setString(5, limitBlockedID);
                  loggableStatement.setString(6, postingTranID);
                  loggableStatement.setString(7, category);
                  loggableStatement.setString(8, fwdContractNo);
                  loggableStatement.setInt(9, Integer.valueOf(Id).intValue());
                 
                  logger.info(
                    "UPDATE approve CHECKER Details: " + loggableStatement.getQueryString());
                 
                  int count = loggableStatement.executeUpdate();
                  if (count > 0)
                  {
                    logger.info("Approved Successfully");
                    fwdContractVO.setCount(count);
                  }
                }
                else
                {
                  int count = updateFailedStatus(fwdContractVO, category);
                  if (count > 0)
                  {
                    fwdContractVO.setCount(0);
                    if ((CommonMethods.isValidString(limitID)) &&
                      (limitStatus != null) && (limitStatus.equalsIgnoreCase("S")))
                    {
                      BlockorUnblockstatus = "LIMITUNBLOCKED";
                      LimitBlockUnblockUtil.limitreversethroughAPI(fwdContractNo, limitID,
                        "0", limitCcy, BlockorUnblockstatus, limitBlockedID);
                    }
                  }
                  fwdContractVO.setCount(2);
                }
              }
              else
              {
                int count = updateFailedStatus(fwdContractVO, category);
                if (count > 0) {
                  fwdContractVO.setCount(3);
                }
              }
            }
            else
            {
              int count = updateFailedStatus(fwdContractVO, category);
              if (count > 0) {
                fwdContractVO.setCount(4);
              }
            }
          }
          else
          {
            int count = updateFailedStatus(fwdContractVO, category);
            if (count > 0) {
              fwdContractVO.setCount(7);
            }
          }
        }
        else
        {
          int count = updatePendingStatus(fwdContractVO, category);
          if (count > 0) {
            fwdContractVO.setCount(5);
          }
        }
      }
      else
      {
        int count = updateRejectStatus(fwdContractVO, category);
        if (count > 0) {
          fwdContractVO.setCount(6);
        }
      }
      logger.info("fwdContractVO COUNT-->" + fwdContractVO.getCount());
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, loggableStatement, rs);
      DBConnectionUtility.surrenderDB(con, loggableStatement, rs1);
    }
    logger.info("Exiting Method");
    return fwdContractVO;
  }
 
  public int updateRejectStatus(ForwardContractVO fwdContractVO, String category)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement loggableStatement = null;
    ResultSet rs = null;
    int count = 0;
    try
    {
      HttpServletRequest request = (HttpServletRequest)ActionContext.getContext()
        .get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
      String userId = request.getRemoteUser();
      if (userId == null) {
        userId = "SUPERVISOR";
      }
      String Id = fwdContractVO.getId().trim();
     
      con = DBConnectionUtility.getZoneConnection();
      loggableStatement = new LoggableStatement(con, "UPDATE CUSTOM_FWC_DETAILS SET STATUS =?,CHECKER_ACTION_BY=?,CHECKER_ACTION_TIMESTAMP=SYSTIMESTAMP,REMARKS=?,LAST_ACTION=?  WHERE CATEGORY=? AND FWC_CONTRACT_NO=? AND ID=? ");
      loggableStatement.setString(1, "REJECTED");
      loggableStatement.setString(2, userId.trim());
      loggableStatement.setString(3, fwdContractVO.getRemarks());
      loggableStatement.setString(4, "Rejected");
      loggableStatement.setString(5, category);
      loggableStatement.setString(6, fwdContractVO.getFwdContractNo());
      loggableStatement.setInt(7, Integer.valueOf(Id).intValue());
      logger.info("Update as Pending for Approval : " + loggableStatement.getQueryString());
     
      count = loggableStatement.executeUpdate();
      if (count > 0) {
        logger.info("Updated Successfully");
      }
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, loggableStatement, rs);
    }
    logger.info("Exiting Method");
    return count;
  }
 
  public ForwardContractVO rejectFwdContractDetails(ForwardContractVO fwdContractVO, String category)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement loggableStatement = null;
    ResultSet rs = null;
    try
    {
      HttpServletRequest request = (HttpServletRequest)ActionContext.getContext()
        .get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
      String userId = request.getRemoteUser();
      if (userId == null) {
        userId = "SUPERVISOR";
      }
      logger.info("fwdContractVO.getRemarks()=>" + fwdContractVO.getRemarks());
      logger.info("fwdContractVO.getFwdContractNo()==>" + fwdContractVO.getFwdContractNo());
      logger.info("fwdContractVO.getCustomerID()==>" + fwdContractVO.getCustomerID());
      String Id = fwdContractVO.getId().trim();
      con = DBConnectionUtility.getZoneConnection();
      loggableStatement = new LoggableStatement(con, "UPDATE CUSTOM_FWC_DETAILS SET STATUS =?,CHECKER_ACTION_BY=?,CHECKER_ACTION_TIMESTAMP=SYSTIMESTAMP,REMARKS=?,LAST_ACTION=?  WHERE CATEGORY=? AND FWC_CONTRACT_NO=? AND ID=? ");
     
      loggableStatement.setString(1, "REJECTED");
      loggableStatement.setString(2, userId.trim());
      loggableStatement.setString(3, fwdContractVO.getRemarks());
      loggableStatement.setString(4, "Rejected");
      loggableStatement.setString(5, category);
      loggableStatement.setString(6, fwdContractVO.getFwdContractNo());
      loggableStatement.setInt(7, Integer.valueOf(Id).intValue());
     
      logger.info("UPDATE reject CHECKER Details: " + loggableStatement.getQueryString());
     
      int count = loggableStatement.executeUpdate();
      if (count > 0)
      {
        logger.info("Rejected Successfully");
        fwdContractVO.setCount(count);
      }
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, loggableStatement, rs);
    }
    logger.info("Exiting Method");
    return fwdContractVO;
  }
 
  public ForwardContractVO deleteFwdContractDetails(ForwardContractVO fwdContractVO, String category)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement loggableStatement = null;
    ResultSet rs = null;
    try
    {
      HttpServletRequest request = (HttpServletRequest)ActionContext.getContext()
        .get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
      String userId = request.getRemoteUser();
      if (userId == null) {
        userId = "SUPERVISOR";
      }
      logger.info("fwdContractVO.getRemarks()=>" + fwdContractVO.getRemarks());
      logger.info("fwdContractVO.getFwdContractNo()==>" + fwdContractVO.getFwdContractNo());
      logger.info("fwdContractVO.getCustomerID()==>" + fwdContractVO.getCustomerID());
      String Id = fwdContractVO.getId().trim();
      con = DBConnectionUtility.getZoneConnection();
      loggableStatement = new LoggableStatement(con, "UPDATE CUSTOM_FWC_DETAILS SET STATUS =?,CHECKER_ACTION_BY=?,CHECKER_ACTION_TIMESTAMP=SYSTIMESTAMP,REMARKS=?,LAST_ACTION=?  WHERE CATEGORY=? AND FWC_CONTRACT_NO=? AND ID=? ");
     
      loggableStatement.setString(1, "DELETED");
      loggableStatement.setString(2, userId.trim());
      loggableStatement.setString(3, fwdContractVO.getRemarks());
      loggableStatement.setString(4, DELETE);
      loggableStatement.setString(5, category);
      loggableStatement.setString(6, fwdContractVO.getFwdContractNo());
      loggableStatement.setInt(7, Integer.valueOf(Id).intValue());
     
      logger.info("UPDATE DELETE CHECKER Details: " + loggableStatement.getQueryString());
     
      int count = loggableStatement.executeUpdate();
      if (count > 0)
      {
        logger.info("Deleted Successfully");
        fwdContractVO.setCount(count);
      }
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, loggableStatement, rs);
    }
    logger.info("Exiting Method");
    return fwdContractVO;
  }
 
  public ForwardContractVO cancelFwdContractDetails(ForwardContractVO fwdContractVO, String category)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement loggableStatement = null;
    ResultSet rs = null;
    ResultSet rs1 = null;
    int records = 0;
   



    Map<String, String> limitexposureTokens = new HashMap();
    Map<String, String> ftrtUpdateTokens = new HashMap();
    Map<String, String> treasUpdateTokens = new HashMap();
    String BlockorUnblockstatus = "LIMIT_UNBLOCKED";
    Map<String, String> postingTokens = new HashMap();
    String postingTranID = "";
    String limitUnblockedID = "";
    String postingStatus = "";
    String limitStatus = "S";
    String washRate = "";
    int insertedCount = 0;
    String seqNo = "";
    String postingTransdate = "";
    String ftrtUpdateStatus = "";
    String treasUpdateStatus = "";
    String chargeAmountCcy = "";
    String chargeCcy = "";
    String chargeAmt = "";
    String gstAmountCcy = "";
    String gstCcy = "";
    String gstAmt = "";
    String totalAmt = "";
    String treasuryHDDTableName1 = ServiceUtility.getBridgePropertyValue("TreasuryHDDTable");
    label2049:
    try
    {
      HttpServletRequest request = (HttpServletRequest)ActionContext.getContext()
        .get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
      String userId = request.getRemoteUser();
      if (userId == null) {
        userId = "SUPERVISOR";
      }
      logger.info("fwdContractVO.getRemarks()=>" + fwdContractVO.getRemarks());
      logger.info("fwdContractVO.getFwdContractNo()==>" + fwdContractVO.getFwdContractNo());
      logger.info("fwdContractVO.getCustomerID()==>" + fwdContractVO.getCustomerID());
     
      String treasuryRefNo = fwdContractVO.getTreasuryRefNo().trim();
      String fwdContractNo = fwdContractVO.getFwdContractNo().trim();
      String remarks = fwdContractVO.getRemarks().trim();
      String customer = fwdContractVO.getCustomerID().trim();
      String limitID = fwdContractVO.getLimitID().trim();
      String accNo = fwdContractVO.getAcctNumber();
      logger.info("fwdContractVO.getLimitID()=>" + fwdContractVO.getLimitID());
      String Id = fwdContractVO.getId().trim();
      String outstandingamtccy = fwdContractVO.getCancellationamount();
      String cancellationamountccy = fwdContractVO.getOutstandingAmt();
     
































      String bookingamt = getBookingAmount(fwdContractVO.getFwdContractNo());
      String bookingdate = fwdContractVO.getBookingDate().trim();
      bookingdate = "20" + bookingdate.substring(6) + "/" + bookingdate.substring(3, 5) + "/" +
        bookingdate.substring(0, 2);
      logger.info("approveFwdContractDetails bookingdate :: " + bookingdate);
     
      String date2 = getTICurrentDateFormat();
      logger.info("approveFwdContractDetails date2 : " + date2);
      if ((bookingdate != null) && (date2 != null) && (bookingdate.compareTo(date2) != 0))
      {
        String outstandingamt = outstandingamtccy.trim().replaceAll("[^0-9.]", "");
        String outstandingccy = outstandingamtccy.trim().replaceAll("[^A-Za-z]+", "");
       
        String cancellationamount = cancellationamountccy.trim().replaceAll("[^0-9.]", "");
        String cancellationccy = cancellationamountccy.trim().replaceAll("[^A-Za-z]+", "");
        AvailBalAuthCheckUtility accountBalance = new AvailBalAuthCheckUtility();
       













        BigDecimal limitamt = new BigDecimal(0);
        logger.info("outstandingamt Amount:" + outstandingamt);
        BigDecimal outstandingamtdec = new BigDecimal(outstandingamt);
        logger.info("outstandingamtdec Amount:" + outstandingamtdec);
        logger.info("cancellationamount Amount:" + cancellationamount);
        BigDecimal cancellationamountdec = new BigDecimal(cancellationamount);
        logger.info("cancellationamountdec Amount:" + cancellationamountdec);
        limitamt = outstandingamtdec.subtract(cancellationamountdec);
        logger.info("Limit Amount:" + limitamt);
        String bookingrate = getBookingTreasuryrate(fwdContractVO.getFwdContractNo());
        BigDecimal bookingtreasrate = new BigDecimal(bookingrate);
        limitamt = limitamt.multiply(bookingtreasrate);
        String limitsr = getLimitNodeForBooking(fwdContractVO.getFwdContractNo());
        logger.info("Limit Amount:" + limitamt);
        if (limitamt.compareTo(new BigDecimal("0.00")) <= 0) {
          limitamt = new BigDecimal("0.001");
        }
        String msgId = DateTimeUtil.getSqlLocalDateTime().toString();
        msgId = msgId.replaceAll("[- :.]", "");
       
        String balance = accountBalance.getAccountBalance("0", msgId, "account", accNo, "");
        logger.info("Account Balance available for account number " + accNo + " is " + balance);
        if (CommonMethods.isValidString(fwdContractVO.getChargeAmount()))
        {
          chargeAmountCcy = fwdContractVO.getChargeAmount().trim();
          chargeCcy = chargeAmountCcy.trim().replaceAll("[^A-Za-z]+", "");
          chargeAmt = chargeAmountCcy.trim().replaceAll("[^0-9.]", "");
        }
        if (CommonMethods.isValidString(fwdContractVO.getGstAmount()))
        {
          gstAmountCcy = fwdContractVO.getGstAmount().trim();
          gstCcy = gstAmountCcy.trim().replaceAll("[^A-Za-z]+", "");
          gstAmt = gstAmountCcy.trim().replaceAll("[^0-9.]", "");
        }
        if ((CommonMethods.isValidString(chargeAmountCcy)) && (CommonMethods.isValidString(gstAmountCcy)) &&
          (CommonMethods.isValidString(chargeCcy)) && (CommonMethods.isValidString(gstCcy)) &&
          (chargeCcy.equalsIgnoreCase(gstCcy))) {
          totalAmt = new BigDecimal(chargeAmt).add(new BigDecimal(gstAmt)).toString();
        }
        int balanceCompare = 0;
        if ((balance != null) && (!balance.isEmpty())) {
          balanceCompare = new BigDecimal(totalAmt).compareTo(new BigDecimal(balance));
        } else {
          balanceCompare = 1;
        }
        logger.info("balanceCompare :: " + balanceCompare);
        if (balanceCompare != 1)
        {
          fwdContractVO = getFWCPostingsToReverse(fwdContractVO);
         





          String toAmount = fwdContractVO.getToCurrencyAmt().trim();
         
          String limitCcy = toAmount.trim().replaceAll("[^A-Za-z]+", "");
         




          String limitAmount = limitamt.toString();
          if (CommonMethods.isValidString(fwdContractVO.getWashRate())) {
            washRate = fwdContractVO.getWashRate().trim();
          }
          if ((CommonMethods.isValidString(limitCcy)) && (!limitCcy.equalsIgnoreCase("INR")) &&
            (CommonMethods.isValidString(washRate)))
          {
            limitAmount = CommonMethods.getEquivalentINRAmount("INR", limitAmount, washRate);
            limitCcy = "INR";
          }
          if (CommonMethods.isValidString(limitID))
          {
            logger.info("Cancel forward Contract in Limit");
           
            logger.info(fwdContractNo + limitID + limitAmount + limitCcy +
              BlockorUnblockstatus);
            limitexposureTokens = LimitBlockUnblockUtil.limitexposurethroughAPI(fwdContractNo, limitID,
              limitAmount, limitCcy, BlockorUnblockstatus, "FWCCANCEL");
            limitStatus = ((String)limitexposureTokens.get("LimitBOUStatus")).trim();
            if (limitStatus.equalsIgnoreCase("S")) {
              limitUnblockedID = ((String)limitexposureTokens.get("SerialNumber")).trim();
            }
          }
          logger.info("Limit status --> " + limitStatus);
         
          Map<String, String> insertInFTIStatus = FWCUtil.insertFTIFwdContractDetails(fwdContractVO, userId,
            "FWCCANCEL");
          insertedCount = Integer.valueOf((String)insertInFTIStatus.get("Count")).intValue();
          seqNo = (String)insertInFTIStatus.get("SequenceNo");
          if (insertedCount > 0)
          {
            ftrtUpdateTokens = FtrtUpdateUtil.updateUtilizedAmountInFinacle(treasuryRefNo,
              cancellationamount);
            ftrtUpdateStatus = ((String)ftrtUpdateTokens.get("FtrtUpdateStatus")).trim();
           
            treasUpdateTokens = TreasUpdateUtil.updateUtilizationAmountInTreasury(treasuryRefNo,
              cancellationamount);
            treasUpdateStatus = ((String)treasUpdateTokens.get("TreasUpdateStatus")).trim();
            if (ftrtUpdateStatus.equalsIgnoreCase("S"))
            {
              if ((treasUpdateStatus.equalsIgnoreCase("S")) && (fwdContractVO.getPostingList().size() > 0))
              {
                postingTokens = PostingUtil.releaseTxnPostings("FWCCANCEL", fwdContractVO, seqNo);
                postingStatus = ((String)postingTokens.get("PostingStatus")).trim();
                if (postingStatus.equalsIgnoreCase("SUCCESS"))
                {
                  postingTranID = ((String)postingTokens.get("TranID")).trim();
                  postingTranID = ((String)postingTokens.get("TranID")).trim();
                  logger.info("postingTranID:" + postingTranID);
                  postingTransdate = ((String)postingTokens.get("Trandate")).trim();
                  logger.info("postingTransdate:" + postingTransdate);
                  fwdContractVO.setTransid(postingTranID);
                  fwdContractVO.setTransdate(postingTransdate);
                 
                  String updatecanceltransdetails = UpdatecancelTransdetails(fwdContractVO,
                    postingTranID, postingTransdate);
                 
                  logger.info("updatecanceltransdetails:" + updatecanceltransdetails);
                  logger.info("postingStatus status --> " + postingStatus);
                 
                  con = DBConnectionUtility.getZoneConnection();
                  loggableStatement = new LoggableStatement(con, "UPDATE CUSTOM_FWC_DETAILS SET STATUS =?,CHECKER_ACTION_BY=?,CHECKER_ACTION_TIMESTAMP=SYSTIMESTAMP,REMARKS=?,LAST_ACTION=?,LIMIT_SERIAL_NUM=?,POSTING_TRAN_ID=? WHERE CATEGORY=? AND FWC_CONTRACT_NO=? AND ID=? ");
                 
                  loggableStatement.setString(1, "APPROVED");
                  loggableStatement.setString(2, userId.trim());
                  loggableStatement.setString(3, remarks);
                  loggableStatement.setString(4, "Approved");
                  loggableStatement.setString(5, limitUnblockedID);
                  loggableStatement.setString(6, postingTranID);
                  loggableStatement.setString(7, category);
                  loggableStatement.setString(8, fwdContractNo);
                  loggableStatement.setInt(9, Integer.valueOf(Id).intValue());
                 
                  logger.info("UPDATE cancel Details: " + loggableStatement.getQueryString());
                 
                  int count = loggableStatement.executeUpdate();
                  if (count > 0)
                  {
                    logger.info("Approved Successfully");
                    fwdContractVO.setCount(count);
                    break label2049;
                  }
                }
                else
                {
                  int count = updateFailedStatus(fwdContractVO, category);
                  if (count > 0)
                  {
                    fwdContractVO.setCount(2);
                    break label2049;
                  }
                }
              }
              else
              {
                int count = updateFailedStatus(fwdContractVO, category);
                if (count > 0)
                {
                  fwdContractVO.setCount(3);
                  break label2049;
                }
              }
            }
            else
            {
              int count = updateFailedStatus(fwdContractVO, category);
              if (count > 0)
              {
                fwdContractVO.setCount(4);
                break label2049;
              }
            }
          }
          else
          {
            int count = updateFailedStatus(fwdContractVO, category);
            if (count > 0)
            {
              fwdContractVO.setCount(7);
              break label2049;
            }
          }
        }
        else
        {
          int count = updatePendingStatus(fwdContractVO, category);
          if (count > 0)
          {
            fwdContractVO.setCount(5);
            break label2049;
          }
        }
      }
      else
      {
        int count = updateRejectStatus(fwdContractVO, category);
        if (count > 0) {
          fwdContractVO.setCount(6);
        }
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, loggableStatement, rs);
    }
    logger.info("Exiting Method");
    return fwdContractVO;
  }
 
  public int updateFailedStatus(ForwardContractVO fwdContractVO, String category)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement loggableStatement = null;
    ResultSet rs = null;
    int count = 0;
    try
    {
      HttpServletRequest request = (HttpServletRequest)ActionContext.getContext()
        .get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
      String userId = request.getRemoteUser();
      if (userId == null) {
        userId = "SUPERVISOR";
      }
      String Id = fwdContractVO.getId().trim();
     
      con = DBConnectionUtility.getZoneConnection();
      loggableStatement = new LoggableStatement(con, "UPDATE CUSTOM_FWC_DETAILS SET STATUS =?,CHECKER_ACTION_BY=?,CHECKER_ACTION_TIMESTAMP=SYSTIMESTAMP,REMARKS=?,LAST_ACTION=? WHERE CATEGORY=? AND FWC_CONTRACT_NO=? AND ID=? ");
      loggableStatement.setString(1, "FAILED");
      loggableStatement.setString(2, userId.trim());
      loggableStatement.setString(3, fwdContractVO.getRemarks());
      loggableStatement.setString(4, "Failed");
      loggableStatement.setString(5, category);
      loggableStatement.setString(6, fwdContractVO.getFwdContractNo());
      loggableStatement.setInt(7, Integer.valueOf(Id).intValue());
      logger.info("Update as Failed : " + loggableStatement.getQueryString());
     
      count = loggableStatement.executeUpdate();
      if (count > 0) {
        logger.info("Updated Successfully");
      }
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, loggableStatement, rs);
    }
    logger.info("Exiting Method");
    return count;
  }
 
  public int updatePendingStatus(ForwardContractVO fwdContractVO, String category)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement loggableStatement = null;
    ResultSet rs = null;
    int count = 0;
    try
    {
      HttpServletRequest request = (HttpServletRequest)ActionContext.getContext()
        .get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
      String userId = request.getRemoteUser();
      if (userId == null) {
        userId = "SUPERVISOR";
      }
      String Id = fwdContractVO.getId().trim();
     
      con = DBConnectionUtility.getZoneConnection();
      loggableStatement = new LoggableStatement(con, "UPDATE CUSTOM_FWC_DETAILS SET STATUS =?,CHECKER_ACTION_BY=?,CHECKER_ACTION_TIMESTAMP=SYSTIMESTAMP,REMARKS=?,LAST_ACTION=? WHERE CATEGORY=? AND FWC_CONTRACT_NO=? AND ID=? ");
      loggableStatement.setString(1, "PENDING FOR APPROVAL");
      loggableStatement.setString(2, userId.trim());
      loggableStatement.setString(3, fwdContractVO.getRemarks());
      loggableStatement.setString(4, "Failed");
      loggableStatement.setString(5, category);
      loggableStatement.setString(6, fwdContractVO.getFwdContractNo());
      loggableStatement.setInt(7, Integer.valueOf(Id).intValue());
      logger.info("Update as Pending for Approval : " + loggableStatement.getQueryString());
     
      count = loggableStatement.executeUpdate();
      if (count > 0) {
        logger.info("Updated Successfully");
      }
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, loggableStatement, rs);
    }
    logger.info("Exiting Method");
    return count;
  }
 
  public static String UpdatecancelTransdetails(ForwardContractVO fwdContractVO, String postingTranID, String postingTransdate)
  {
    String status = null;
    logger.info("Enter into Update trans details");
    Connection con = null;
    LoggableStatement loggableStatement = null;
    ResultSet rs = null;
    int count = 0;
    try
    {
      HttpServletRequest request = (HttpServletRequest)ActionContext.getContext()
        .get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
      String userId = request.getRemoteUser();
      if (userId == null) {
        userId = "SUPERVISOR";
      }
      String Id = fwdContractVO.getId().trim();
     
      con = DBConnectionUtility.getZoneConnection();
      loggableStatement = new LoggableStatement(con, "UPDATE CUSTOM_FWC_DETAILS SET TRANS_ID =?,TRANS_DATE=? WHERE CATEGORY=? AND FWC_CONTRACT_NO=? AND ID=?");
      logger.info("Update Query :UPDATE CUSTOM_FWC_DETAILS SET TRANS_ID =?,TRANS_DATE=? WHERE CATEGORY=? AND FWC_CONTRACT_NO=? AND ID=?");
      loggableStatement.setString(1, postingTranID);
      loggableStatement.setString(2, postingTransdate);
      loggableStatement.setString(3, "FWCCANCEL");
      loggableStatement.setString(4, fwdContractVO.getFwdContractNo());
      loggableStatement.setString(5, Id);
      count = loggableStatement.executeUpdate();
      if (count > 0)
      {
        logger.info("Updated Successfully");
        status = "success";
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      logger.info(e.getMessage());
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, loggableStatement, rs);
    }
    logger.info("Exiting Method");
    return status;
  }
 
  public static String UpdatebookTransdetails(ForwardContractVO fwdContractVO, String postingTranID, String postingTransdate)
  {
    String status = null;
    logger.info("Enter into Update trans details");
    Connection con = null;
    LoggableStatement loggableStatement = null;
    ResultSet rs = null;
    int count = 0;
    try
    {
      HttpServletRequest request = (HttpServletRequest)ActionContext.getContext()
        .get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
      String userId = request.getRemoteUser();
      if (userId == null) {
        userId = "SUPERVISOR";
      }
      String Id = fwdContractVO.getId().trim();
     
      con = DBConnectionUtility.getZoneConnection();
      loggableStatement = new LoggableStatement(con, "UPDATE CUSTOM_FWC_DETAILS SET TRANS_ID =?,TRANS_DATE=? WHERE CATEGORY=? AND FWC_CONTRACT_NO=? AND ID=?");
      logger.info("Update Query :UPDATE CUSTOM_FWC_DETAILS SET TRANS_ID =?,TRANS_DATE=? WHERE CATEGORY=? AND FWC_CONTRACT_NO=? AND ID=?");
      loggableStatement.setString(1, postingTranID);
      loggableStatement.setString(2, postingTransdate);
      loggableStatement.setString(3, "FWCBOOK");
      loggableStatement.setString(4, fwdContractVO.getFwdContractNo());
      loggableStatement.setString(5, Id);
      count = loggableStatement.executeUpdate();
      if (count > 0)
      {
        logger.info("Updated Successfully");
        status = "success";
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      logger.info(e.getMessage());
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, loggableStatement, rs);
    }
    logger.info("Exiting Method");
    return status;
  }
 
  public static String readFileInpStream(InputStream inpStream)
    throws IOException
  {
    String result = "";
    BufferedReader buffReader = null;
    try
    {
      String line = null;
      StringBuilder strBuilder = new StringBuilder();
      String newLineSeparator = System.getProperty("line.separator");
      buffReader = new BufferedReader(new InputStreamReader(inpStream));
     
      int lineCount = 1;
      while ((line = buffReader.readLine()) != null)
      {
        if (lineCount > 1) {
          strBuilder.append(newLineSeparator);
        }
        strBuilder.append(line);
        lineCount++;
      }
      result = strBuilder.toString();
    }
    catch (IOException ex)
    {
      logger.error("IOException " + ex.getMessage());
      ex.printStackTrace();
      try
      {
        if (buffReader != null) {
          buffReader.close();
        }
      }
      catch (IOException ex)
      {
        logger.error("BufferedReader close exception! " + ex.getMessage());
        ex.printStackTrace();
      }
    }
    finally
    {
      try
      {
        if (buffReader != null) {
          buffReader.close();
        }
      }
      catch (IOException ex)
      {
        logger.error("BufferedReader close exception! " + ex.getMessage());
        ex.printStackTrace();
      }
    }
    return result;
  }
 
  public boolean executeGenericQuery(String query, String parameters)
  {
    Connection connection = null;
    LoggableStatement loggableStatement = null;
    ResultSet resultSet = null;
    try
    {
      int parameterCount = 1;
      connection = DBConnectionUtility.getZoneConnection();
      loggableStatement = new LoggableStatement(connection, query);
      if (((parameters instanceof String)) && (!parameters.trim().equalsIgnoreCase(""))) {
        for (String invidualParameters : parameters.split("\\|"))
        {
          loggableStatement.setString(parameterCount, invidualParameters);
          parameterCount++;
        }
      }
      logger.info("Query ----------->" + loggableStatement.getQueryString());
      resultSet = loggableStatement.executeQuery();
      while (resultSet.next()) {
        if (resultSet.getInt(1) > 0) {
          return true;
        }
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(connection, loggableStatement, resultSet);
    }
    return false;
  }
 
  public String getTICurrentDate()
  {
    String tiCurrDate = "";
    ResultSet rs = null;
    Connection con = null;
    PreparedStatement ps = null;
    Map<String, Object> session = ActionContext.getContext().getSession();
    String query = "SELECT to_char(PROCDATE,'dd/mm/yyyy') as PROCDATE FROM DLYPRCCYCL ";
    try
    {
      con = DBConnectionUtility.getZoneConnection();
      ps = con.prepareStatement(query);
      rs = ps.executeQuery();
      while (rs.next())
      {
        tiCurrDate = rs.getString(1);
        logger.info("tiCurrDate====>" + tiCurrDate);
       
        session.put("processDate", tiCurrDate);
      }
      logger.info("TIDATE-----" + tiCurrDate);
    }
    catch (Exception e)
    {
      logger.info("tiCurrDate=== Exception=>" + e);
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, ps, rs);
    }
    return tiCurrDate;
  }
 
  public String getTICurrentDateFormat()
  {
    String tiCurrDate = "";
    ResultSet rs = null;
    Connection con = null;
    PreparedStatement ps = null;
    Map<String, Object> session = ActionContext.getContext().getSession();
    String query = "SELECT to_char(PROCDATE,'yyyy/mm/dd') as PROCDATE FROM DLYPRCCYCL ";
    try
    {
      con = DBConnectionUtility.getZoneConnection();
      ps = con.prepareStatement(query);
      rs = ps.executeQuery();
      while (rs.next())
      {
        tiCurrDate = rs.getString(1);
        logger.info("tiCurrDate====>" + tiCurrDate);
       
        session.put("processDate", tiCurrDate);
      }
      logger.info("TIDATE-----" + tiCurrDate);
    }
    catch (Exception e)
    {
      logger.info("tiCurrDate=== Exception=>" + e);
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, ps, rs);
    }
    return tiCurrDate;
  }
 
  public String checkFWC_Status(String treRefNo, String cifID, String forwardnum)
  {
    String fwcStatusNContractNo = "";
    String fwcStatus = "";
    String fwcnum = "";
    String query = null;
    ResultSet resultSet = null;
    Connection tiZoneConnection = null;
    PreparedStatement preparedStatement = null;
    try
    {
      query = "SELECT * FROM CUSTOM_FWC_DETAILS WHERE TREASURY_REF_NO =? AND CIF_ID=?";
     
      tiZoneConnection = DBConnectionUtility.getZoneConnection();
      preparedStatement = tiZoneConnection.prepareStatement(query);
      preparedStatement.setString(1, treRefNo);
      preparedStatement.setString(2, cifID);
      resultSet = preparedStatement.executeQuery();
      while (resultSet.next())
      {
        fwcStatus = resultSet.getString("STATUS");
        fwcnum = resultSet.getString("FWC_CONTRACT_NO");
      }
      fwcStatusNContractNo = fwcStatus + "-" + fwcnum;
      logger.info("checkFWC_Status result fwcStatus & ContractNo : " + fwcStatusNContractNo);
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(tiZoneConnection, preparedStatement, resultSet);
    }
    return fwcStatusNContractNo;
  }
 
  public String getBookHostDealCategoryFromTreasury(String treRefNo, String cifID)
  {
    logger.info("Entering Method");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    String treas_Host_Deal_Category = null;
    treasuryHDDTableName = (String)ServiceUtility.TBProperties.get("TreasuryHDDTable");
    try
    {
      con = DBConnectionUtility.getDBLinkConnection();
      String query = "SELECT HOST_DEAL_SUB_CATEGORY FROM " + treasuryHDDTableName + " WHERE HOST_DEAL_CATEGORY='FXRATE' AND RECORD_STATUS='TRANSFER' AND REFERENCE_NUM=? AND COUNTERPARTY_STRING=? ";
      logger.info("Query:" + query);
      pst = new LoggableStatement(con, query);
      pst.setString(1, treRefNo);
      pst.setString(2, cifID);
      rs = pst.executeQuery();
      while (rs.next()) {
        treas_Host_Deal_Category = rs.getString("HOST_DEAL_SUB_CATEGORY");
      }
      logger.info("Host Deal category:" + treas_Host_Deal_Category);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    return treas_Host_Deal_Category;
  }
 
  public String getCancelHostDealCategoryFromTreasury(String treRefNo, String cifID)
  {
    logger.info("Entering Method");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    String treas_Host_Deal_Category = null;
    treasuryHDDTableName = (String)ServiceUtility.TBProperties.get("TreasuryHDDTable");
    try
    {
      con = DBConnectionUtility.getDBLinkConnection();
      String query = "SELECT HOST_DEAL_CATEGORY FROM " + treasuryHDDTableName + " WHERE HOST_DEAL_CATEGORY='FWCCANCEL' AND RECORD_STATUS='TRANSFER' AND REFERENCE_NUM=? AND COUNTERPARTY_STRING=? ";
      logger.info("Query:" + query);
      pst = new LoggableStatement(con, query);
      pst.setString(1, treRefNo);
      pst.setString(2, cifID);
      rs = pst.executeQuery();
      while (rs.next()) {
        treas_Host_Deal_Category = rs.getString("HOST_DEAL_CATEGORY");
      }
      logger.info("Host Deal category:" + treas_Host_Deal_Category);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    return treas_Host_Deal_Category;
  }
 
  public int getRecordCountFromDB(ForwardContractVO fwdContractVO, String category)
  {
    Connection con = null;
    LoggableStatement loggableStatement = null;
    ResultSet rs = null;
    int count = 0;
    try
    {
      if (CommonMethods.isValidString(fwdContractVO.getFwdContractNo()))
      {
        con = DBConnectionUtility.getZoneConnection();
        loggableStatement = new LoggableStatement(con, "SELECT COUNT(*) AS COUNT FROM CUSTOM_FWC_DETAILS WHERE FWC_CONTRACT_NO=? AND CATEGORY=? AND ID=?");
        loggableStatement.setString(1, fwdContractVO.getFwdContractNo());
        loggableStatement.setString(2, category);
        if (CommonMethods.isValidString(fwdContractVO.getId())) {
          loggableStatement.setInt(3, Integer.valueOf(fwdContractVO.getId()).intValue());
        } else {
          loggableStatement.setInt(3, 0);
        }
        logger.info("getRecordCountFromDB Query----------------->" + loggableStatement.getQueryString());
       
        rs = loggableStatement.executeQuery();
        if (rs.next())
        {
          count = rs.getInt("COUNT");
          logger.info("Record Count from dB --> " + count);
        }
        logger.info("getRecordCountFromDB --> " + count);
      }
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, loggableStatement, rs);
    }
    return count;
  }
}

