package in.co.stp.utility;

import in.co.stp.dao.AbstractDAO;
import in.co.stp.dao.exception.DAOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GenericFundTransferFinance
  extends AbstractDAO
  implements ActionConstantsQuery, ActionConstants
{
  static GenericFundTransferFinance gft;
  private static Logger logger = LogManager.getLogger(GenericFundTransferFinance.class.getName());
 
  public static GenericFundTransferFinance getDAO()
  {
    if (gft == null) {
      gft = new GenericFundTransferFinance();
    }
    return gft;
  }
 
  public static void main(String[] args)
    throws DAOException, SQLException
  {
    GenericFundTransferFinance gft = null;
    gft = getDAO();
   
    gft.updateMasterReference("VFVA201011700421");
  }
 
  public String doGenericFundTransfer(String batchID, String exposureOn, String programeId, String customer, String statusFlag)
    throws DAOException
  {
    logger.info("Entering Method");
    String limitAccountNumber = null;
    String financeAmount = null;
    String fundStatus = null;
    String dealRef = null;
    Connection con = null;
    LoggableStatement pst = null;
    ResultSet rs = null;
    String query = null;
    label236:
    try
    {
      if (exposureOn != null) {
        if (exposureOn.equalsIgnoreCase("A"))
        {
          limitAccountNumber = getAnchorLimitAccount(programeId);
          financeAmount = getAnchorFinanceAmount(batchID, statusFlag);
          if ((financeAmount == null) ||
            (financeAmount.equalsIgnoreCase(""))) {
            financeAmount = "0";
          }
          fundStatus = FundTransferPost.fundPosting(
            limitAccountNumber, financeAmount, customer,
            batchID);
          if ((fundStatus == null) ||
            (!fundStatus.equalsIgnoreCase("OK")))
          {
            con = DBConnectionUtility.getConnection();
            if (batchID != null) {
              batchID = batchID.trim();
            }
            query = "SELECT MAS.MASTER_REF FROM ETT_STP_INVOICES I,ETT_STP_INVOICE_DETAILS ID, INVMASTER INV,FNCEMASTER FNM,MASTER MAS WHERE TRIM(I.BATCHID)=TRIM(INV.BATCHID) AND ID.USER_STATUS='CA' AND (I.STATUS='S'  OR I.STATUS='W') AND TRIM(INV.INVOIC_REF)=TRIM(I.INVOICE_INDENT_NO) AND INV.FINCE_EV = FNM.FINCEEVKEY AND  ID.BATCHID=I.BATCHID  AND FNM.KEY97 = MAS.KEY97 AND MAS.STATUS='LIV' AND TRIM(I.BATCHID)=?";
           


            pst = new LoggableStatement(con, query);
            pst.setString(1, batchID);
            rs = pst.executeQuery();
            while (rs.next())
            {
              dealRef = rs.getString("MASTER_REF");
              updateMasterReference(dealRef);
            }
            break label236;
          }
        }
        else
        {
          fundStatus = processCpartyFund(batchID, programeId,
            customer, statusFlag);
        }
      }
    }
    catch (Exception exception)
    {
      throwDAOException(exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    logger.info("Exiting Method");
    return fundStatus;
  }
 
  private String processCpartyFund(String batchId, String programeId, String customer, String statusFlag)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement ps = null;
    ResultSet rs = null;
    LoggableStatement ps1 = null;
    ResultSet rs1 = null;
    String sql = null;
    String dealRef = null;
    String limitNumber = null;
    String financeAmount = null;
    String Cparty = null;
    String fundStatus = null;
    try
    {
      con = DBConnectionUtility.getConnection();
     
      String query = "";
      if ((statusFlag != null) && (statusFlag.equalsIgnoreCase("uploadwar"))) {
        query = "SELECT SUM(FNM.DEAL_AMT/100) AS FINANCE_AMOUNT,SCP.CPARTY AS DEALER_SELLER_CODE FROM  ETT_STP_INVOICES I,ETT_STP_INVOICE_DETAILS ID,INVMASTER INV ,FNCEMASTER FNM,SCFCPARTY SCP, MASTER MAS  WHERE TRIM(I.BATCHID)=TRIM(INV.BATCHID)  AND (INV.SELLER = SCP.KEY97 OR INV.BUYER = SCP.KEY97) AND TRIM(SCP.CPARTY)=TRIM(I.CPARTY) AND INV.FINCE_EV = FNM.FINCEEVKEY AND  ID.BATCHID=I.BATCHID   AND I.BATCHID=? AND ID.USER_STATUS='CA' AND (I.STATUS='S' OR I.STATUS='W') AND TRIM(INV.INVOIC_REF)=TRIM(I.INVOICE_INDENT_NO) AND FNM.KEY97 = MAS.KEY97       AND MAS.STATUS = 'LIV' GROUP BY SCP.CPARTY";
      } else {
        query = "SELECT SUM(FNM.DEAL_AMT/100) AS FINANCE_AMOUNT,SCP.CPARTY AS DEALER_SELLER_CODE FROM  ETT_STP_INVOICES I,ETT_STP_INVOICE_DETAILS ID,INVMASTER INV ,FNCEMASTER FNM,SCFCPARTY SCP, MASTER MAS  WHERE TRIM(I.BATCHID)=TRIM(INV.BATCHID)  AND (INV.SELLER = SCP.KEY97 OR INV.BUYER = SCP.KEY97) AND TRIM(SCP.CPARTY)=TRIM(I.CPARTY) AND INV.FINCE_EV = FNM.FINCEEVKEY AND  ID.BATCHID=I.BATCHID   AND I.BATCHID=? AND ID.USER_STATUS='CA' AND I.STATUS='S' AND TRIM(INV.INVOIC_REF)=TRIM(I.INVOICE_INDENT_NO) AND FNM.KEY97 = MAS.KEY97   AND MAS.STATUS = 'LIV' GROUP BY SCP.CPARTY";
      }
      ps = new LoggableStatement(con, query);
      ps.setString(1, batchId);
      logger.info("Executing Query->" + ps.getQueryString());
      rs = ps.executeQuery();
      label308:
      for (; rs.next(); rs1.next())
      {
        financeAmount = rs.getString("FINANCE_AMOUNT");
        Cparty = rs.getString("DEALER_SELLER_CODE").trim();
        limitNumber = getCpartyLimitAccount(programeId, Cparty);
        fundStatus = FundTransferPost.fundPosting(limitNumber, financeAmount, customer, batchId);
        if ((fundStatus != null) && (fundStatus.equalsIgnoreCase("OK"))) {
          break label308;
        }
        sql = "SELECT MAS.MASTER_REF FROM  ETT_STP_INVOICES I,ETT_STP_INVOICE_DETAILS ID,INVMASTER INV ,FNCEMASTER FNM,SCFCPARTY SCP, MASTER MAS  WHERE TRIM(I.BATCHID)=TRIM(INV.BATCHID)  AND (INV.SELLER = SCP.KEY97 OR INV.BUYER = SCP.KEY97) AND TRIM(SCP.CPARTY)=TRIM(I.CPARTY) AND INV.FINCE_EV = FNM.FINCEEVKEY AND  ID.BATCHID=I.BATCHID   AND I.BATCHID=? AND SCP.CPARTY=? AND ID.USER_STATUS='CA' AND (I.STATUS='S'  OR I.STATUS='W') AND TRIM(INV.INVOIC_REF)=TRIM(I.INVOICE_INDENT_NO) AND FNM.KEY97 = MAS.KEY97   AND MAS.STATUS = 'LIV'";
       






        ps1 = new LoggableStatement(con, sql);
        ps1.setString(1, batchId);
        ps1.setString(2, Cparty);
        logger.info("Executing Query->" + ps1.getQueryString());
        rs1 = ps1.executeQuery();
        continue;
        dealRef = rs1.getString("MASTER_REF");
        if ((dealRef != null) && (!dealRef.equalsIgnoreCase(""))) {
          updateMasterReference(dealRef);
        }
      }
    }
    catch (Exception exception)
    {
      throwDAOException(exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, ps, rs);
      DBConnectionUtility.surrenderStatement(ps1, rs1);
    }
    logger.info("Exiting Method");
    return fundStatus;
  }
 
  private void updateMasterReference(String dealRef)
    throws DAOException, SQLException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement pst = null;
    LoggableStatement pst1 = null;
    ResultSet rs = null;
    String query = null;
    String invKey97 = null;
    String finKey97 = null;
    CommonMethods comm = null;
    try
    {
      comm = new CommonMethods();
      con = DBConnectionUtility.getConnection();
      if (dealRef != null) {
        dealRef = dealRef.trim();
      }
      query = "SELECT I.KEY97 AS INVKEY,F.KEY97 AS FINANCEKEY FROM BASEEVENT BASE,INVMASTER I,FNCEMASTER F,MASTER M WHERE BASE.KEY97=I.FINCE_EV AND BASE.MASTER_KEY=F.KEY97 AND BASE.MASTER_KEY=M.KEY97(+) AND TRIM(M.MASTER_REF)=? AND M.STATUS='LIV'";
     

      pst = new LoggableStatement(con, query);
      pst.setString(1, dealRef);
      rs = pst.executeQuery();
      if (rs.next())
      {
        invKey97 = comm.getEmptyIfNull(rs.getString("INVKEY")).trim();
        finKey97 = comm.getEmptyIfNull(rs.getString("FINANCEKEY")).trim();
        String sql = "UPDATE MASTER SET STATUS='CAN' WHERE key97 IN (?,?)";
        pst1 = new LoggableStatement(con, sql);
        pst1.setString(1, invKey97);
        pst1.setString(2, finKey97);
        pst1.executeUpdate();
      }
    }
    catch (Exception e)
    {
      throwDAOException(e);
    }
    finally
    {
      if (pst1 != null) {
        pst1.close();
      }
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    logger.info("Exiting Method");
  }
 
  private String processCpartyFundLimit(String batchId, String programeId)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement ps = null;
    ResultSet rs = null;
    String limitNumber = null;
    String financeAmount = null;
    String Cparty = null;
    try
    {
      con = DBConnectionUtility.getConnection();
      String query = "SELECT SUM(I.INVOICE_INDENT_AMOUNT) AS FINANCE_AMOUNT,ETT_CPARTY(ID.PROGRM,DEALER_SELLER_CODE,CLFACCNO) AS DEALER_SELLER_CODE FROM  ETT_STP_INVOICES I,ETT_STP_INVOICE_DETAILS ID WHERE ID.BATCHID=I.BATCHID  AND I.BATCHID=? AND ID.USER_STATUS='CA' AND I.STATUS='S' GROUP BY I.DEALER_SELLER_CODE,ID.PROGRM,CLFACCNO";
     


      ps = new LoggableStatement(con, query);
      ps.setString(1, batchId);
      logger.info("Executing Query->" + ps.getQueryString());
      rs = ps.executeQuery();
      while (rs.next())
      {
        financeAmount = rs.getString("FINANCE_AMOUNT");
        Cparty = rs.getString("DEALER_SELLER_CODE").trim();
        limitNumber = getCpartyLimitAccount(programeId, Cparty);
      }
    }
    catch (Exception exception)
    {
      throwDAOException(exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, ps, rs);
    }
    logger.info("Exiting Method");
    return limitNumber;
  }
 
  private String getAnchorLimitAccount(String programeId)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement ps = null;
    ResultSet rs = null;
    String limitNumber = null;
    try
    {
      con = DBConnectionUtility.getConnection();
      String query = "select distinct PROG_NOR_LMNO AS PROG_NOR_LMNO from ETT_PROG_CP_LM where PROG_ID=?";
      ps = new LoggableStatement(con, query);
      ps.setString(1, programeId);
      logger.info("Executing Query->" + ps.getQueryString());
      rs = ps.executeQuery();
      if (rs.next()) {
        limitNumber = rs.getString("PROG_NOR_LMNO");
      }
    }
    catch (Exception exception)
    {
      throwDAOException(exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, ps, rs);
    }
    logger.info("Exiting Method");
    return limitNumber;
  }
 
  private String getAnchorFinanceAmount(String batchId, String statusFlag)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement ps = null;
    ResultSet rs = null;
    String financeAmount = null;
    try
    {
      con = DBConnectionUtility.getConnection();
     
      String query = "";
      if ((statusFlag != null) && (statusFlag.equalsIgnoreCase("uploadwar"))) {
        query = "SELECT SUM(FNM.DEAL_AMT/100) AS INVOICE_INDENT_AMOUNT FROM  ETT_STP_INVOICES I,  ETT_STP_INVOICE_DETAILS ID,INVMASTER INV ,FNCEMASTER FNM,SCFCPARTY SCP,MASTER MAS WHERE ID.BATCHID=I.BATCHID AND (INV.SELLER = SCP.KEY97 OR INV.BUYER = SCP.KEY97) AND TRIM(SCP.CPARTY)=TRIM(I.CPARTY) AND TRIM(I.BATCHID)=TRIM(INV.BATCHID) AND INV.FINCE_EV = FNM.FINCEEVKEY AND I.BATCHID=? AND ID.USER_STATUS='CA'  AND (I.STATUS='S' OR I.STATUS='W') AND TRIM(INV.INVOIC_REF)=TRIM(I.INVOICE_INDENT_NO) AND FNM.KEY97 = MAS.KEY97       AND MAS.STATUS = 'LIV' ";
      } else {
        query = "SELECT SUM(FNM.DEAL_AMT/100) AS INVOICE_INDENT_AMOUNT FROM  ETT_STP_INVOICES I,  ETT_STP_INVOICE_DETAILS ID,INVMASTER INV ,FNCEMASTER FNM,SCFCPARTY SCP,MASTER MAS  WHERE ID.BATCHID=I.BATCHID AND (INV.SELLER = SCP.KEY97 OR INV.BUYER = SCP.KEY97) AND TRIM(SCP.CPARTY)=TRIM(I.CPARTY) AND TRIM(I.BATCHID)=TRIM(INV.BATCHID) AND INV.FINCE_EV = FNM.FINCEEVKEY AND I.BATCHID=?  AND ID.USER_STATUS='CA' AND I.STATUS='S' AND TRIM(INV.INVOIC_REF)=TRIM(I.INVOICE_INDENT_NO) AND FNM.KEY97 = MAS.KEY97       AND MAS.STATUS = 'LIV' ";
      }
      ps = new LoggableStatement(con, query);
      ps.setString(1, batchId);
      logger.info("Executing Query->" + ps.getQueryString());
      rs = ps.executeQuery();
      if (rs.next()) {
        financeAmount = rs.getString("INVOICE_INDENT_AMOUNT");
      }
    }
    catch (Exception exception)
    {
      throwDAOException(exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, ps, rs);
    }
    logger.info("Exiting Method");
    return financeAmount;
  }
 
  private String getCpartyLimitAccount(String programeId, String cParty)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement ps = null;
    ResultSet rs = null;
    String limitNumber = null;
    try
    {
      con = DBConnectionUtility.getConnection();
      String query = "select PROG_CP_NOR_LMNO AS PROG_CP_NOR_LMNO from ETT_PROG_CP_LM where PROG_ID=? AND PROG_CP=?";
      ps = new LoggableStatement(con, query);
      ps.setString(1, programeId);
      ps.setString(2, cParty);
      logger.info("Executing Query->" + ps.getQueryString());
      rs = ps.executeQuery();
      if (rs.next()) {
        limitNumber = rs.getString("PROG_CP_NOR_LMNO");
      }
    }
    catch (Exception exception)
    {
      throwDAOException(exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, ps, rs);
    }
    logger.info("Exiting Method");
    return limitNumber;
  }
}