package in.co.boe.dao.boe_viewer;

import in.co.boe.dao.AbstractDAO;
import in.co.boe.dao.exception.DAOException;
import in.co.boe.utility.ActionConstantsQuery;
import in.co.boe.utility.CommonMethods;
import in.co.boe.utility.DBConnectionUtility;
import in.co.boe.utility.LoggableStatement;
import in.co.boe.vo.BOEViewerVO;
import in.co.boe.vo.BoeVO;
import in.co.boe.vo.TransactionVO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import org.apache.log4j.Logger;
 
public class BOEViwerDAO
  extends AbstractDAO
  implements ActionConstantsQuery
{
  private static Logger logger = Logger.getLogger(BOEViwerDAO.class
    .getName());
  static BOEViwerDAO bd;
  public static BOEViwerDAO getBD()
  {
    if (bd == null) {
      bd = new BOEViwerDAO();
    }
    return bd;
  }
  public BOEViewerVO getBOEIssuance(BOEViewerVO viewerVO)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement loggableStatement = null;
    ResultSet rs = null;
    ArrayList<TransactionVO> boeList = null;
    TransactionVO transactionVO = null;
    logger.info("Entering Method");
    CommonMethods commonMethods = null;
    String BOE_LIST_QUERY = null;
    boolean boeRefNoFlag = false;
    boolean boeEventNoFlag = false;
    boolean boeNoFlag = false;
    boolean boeCurrFlag = false;
    boolean cifNoFlag = false;
    int setValue = 0;
    try
    {
      boeList = new ArrayList();

 
 
      BOE_LIST_QUERY = "SELECT NVL(TRIM(BOE_PAYMENT_BP_PAY_REF), ' ') as BOE_PAYMENT_BP_PAY_REF,NVL(TRIM(BOE_PAYMENT_BP_PAY_PART_REF), ' ') as BOE_PAYMENT_BP_PAY_PART_REF, NVL(TRIM(BOE_PAYMENT_BP_BOE_NO), ' ') as BOE_PAYMENT_BP_BOE_NO, NVL(TRIM(BOE_PAYMENT_CIF_ID), ' ') as BOE_PAYMENT_CIF_ID,NVL(TRIM(BOE_PAYMENT_CIF_NAME), ' ') as BOE_PAYMENT_CIF_NAME, TO_CHAR(TO_DATE(BOE_PAYMENT_BP_BOE_DT, 'dd-mm-yy'),'dd-mm-yyyy') as BOE_PAYMENT_BP_BOE_DT,NVL(TRIM(to_char(BOE_ENTRY_AMT,'999,999,999,999,999.99')), '0') as BOE_ENTRY_AMT,NVL(TRIM(to_char(BOE_PAYMENT_BP_PAY_FC_AMT,'999,999,999,999,999.99')), '0') BOE_PAYMENT_BP_PAY_FC_AMT,NVL(TRIM(to_char(AMOUNTOTHERAD,'999,999,999,999,999.99')), '0') as AMOUNTOTHERAD, NVL(TRIM(to_char(ACTUALAMT,'999,999,999,999,999.99')), '0') as ACTUALAMT,NVL(TRIM(to_char(BOE_PAYMENT_BP_PAY_ENDORSE_AMT,'999,999,999,999,999.99')), '0') as BOE_PAYMENT_BP_PAY_ENDORSE_AMT, NVL(TRIM(to_char(BOE_PAYMENT_BP_PAY_OS_FC_AMT,'999,999,999,999,999.99')), '0') as BOE_PAYMENT_BP_PAY_OS_FC_AMT, NVL(TRIM(BOE_PAYMENT_BP_PAY_FULL_YN),' ') as BOE_PAYMENT_BP_PAY_FULL_YN ,NVL(TRIM(to_char(BOE_PAYMENT_BP_PAY_FC_AMT,'999,999,999,999,999.99')), '0') as BOE_PAYMENT_BP_PAY_FC_AMT,NVL(TRIM(BOE_PAYMENT_BP_PAY_CCY),' ') AS BOE_PAYMENT_BP_PAY_CCY, NVL(TRIM(STATUS), ' ') as STATUS  FROM ETT_BOE_PAYMENT WHERE BOE_PAYMENT_BP_PAY_REF = BOE_PAYMENT_BP_PAY_REF ";

 
 
 
 
 
 
 
 
      commonMethods = new CommonMethods();
      String boeRefNo = viewerVO.getBoeRefNo();
      if (!commonMethods.isNull(boeRefNo))
      {
        BOE_LIST_QUERY = BOE_LIST_QUERY + " AND UPPER(BOE_PAYMENT_BP_PAY_REF) LIKE UPPER('%'||?||'%')";
        boeRefNoFlag = true;
      }
      String boeEventNo = viewerVO.getBoeEventNo();
      if (!commonMethods.isNull(boeEventNo))
      {
        BOE_LIST_QUERY = BOE_LIST_QUERY + " AND UPPER(BOE_PAYMENT_BP_PAY_PART_REF) LIKE UPPER('%'||?||'%')";
        boeEventNoFlag = true;
      }
      String boeNo = viewerVO.getBoeNo();
      if (!commonMethods.isNull(boeNo))
      {
        BOE_LIST_QUERY = BOE_LIST_QUERY + " AND UPPER(BOE_PAYMENT_BP_BOE_NO) LIKE UPPER('%'||?||'%')";
        boeNoFlag = true;
      }
      String boeCurr = viewerVO.getBoeCurr();
      if (!commonMethods.isNull(boeCurr))
      {
        BOE_LIST_QUERY = BOE_LIST_QUERY + " AND UPPER(BOE_PAYMENT_BP_PAY_CCY) LIKE UPPER('%'||?||'%')";
        boeCurrFlag = true;
      }
      String cifNo = viewerVO.getCifNo();
      if (!commonMethods.isNull(cifNo))
      {
        BOE_LIST_QUERY = BOE_LIST_QUERY + " AND UPPER(BOE_PAYMENT_CIF_ID) LIKE UPPER('%'||?||'%')";
        cifNoFlag = true;
      }
      con = DBConnectionUtility.getConnection();
      loggableStatement = new LoggableStatement(con, BOE_LIST_QUERY);
      if (boeRefNoFlag) {
        loggableStatement.setString(++setValue, boeRefNo.trim());
      }
      if (boeEventNoFlag) {
        loggableStatement.setString(++setValue, boeEventNo.trim());
      }
      if (boeNoFlag) {
        loggableStatement.setString(++setValue, boeNo.trim());
      }
      if (boeCurrFlag) {
        loggableStatement.setString(++setValue, boeCurr.trim());
      }
      if (cifNoFlag) {
        loggableStatement.setString(++setValue, cifNo.trim());
      }
      logger.info("RetriveDetailsFromBOE: " + loggableStatement.getQueryString());
      rs = loggableStatement.executeQuery();
      while (rs.next())
      {
        transactionVO = new TransactionVO();
        transactionVO.setPaymentRefNo(CommonMethods.setCheckValue(rs.getString("BOE_PAYMENT_BP_PAY_REF")));
        transactionVO.setPartPaymentSlNo(CommonMethods.setCheckValue(rs.getString("BOE_PAYMENT_BP_PAY_PART_REF")));
        transactionVO.setBoeNo(CommonMethods.setCheckValue(rs.getString("BOE_PAYMENT_BP_BOE_NO")));
        transactionVO.setBoeDate(CommonMethods.setCheckValue(rs.getString("BOE_PAYMENT_BP_BOE_DT")));
        transactionVO.setBillAmt(CommonMethods.setCheckValue(rs.getString("BOE_ENTRY_AMT")).toString());
        transactionVO.setAdEndorseAmt(CommonMethods.setCheckValue(rs.getString("AMOUNTOTHERAD")));
        transactionVO.setActualEndorseAmt(CommonMethods.setCheckValue(rs.getString("ACTUALAMT")));
        transactionVO.setEndorseAmt(CommonMethods.setCheckValue(rs.getString("BOE_PAYMENT_BP_PAY_ENDORSE_AMT")));
        transactionVO.setOutAmt(CommonMethods.setCheckValue(rs.getString("BOE_PAYMENT_BP_PAY_OS_FC_AMT")));
        transactionVO.setFullyAlloc(CommonMethods.setCheckValue(rs.getString("BOE_PAYMENT_BP_PAY_FULL_YN")));
        transactionVO.setPaymentAmount(CommonMethods.setCheckValue(rs.getString("BOE_PAYMENT_BP_PAY_FC_AMT")));
        transactionVO.setPaymentCurr(CommonMethods.setCheckValue(rs.getString("BOE_PAYMENT_BP_PAY_CCY")));
        transactionVO.setCifNo(CommonMethods.setCheckValue(rs.getString("BOE_PAYMENT_CIF_ID")));
        transactionVO.setCustName(CommonMethods.setCheckValue(rs.getString("BOE_PAYMENT_CIF_NAME")));
        transactionVO.setStatus(CommonMethods.setCheckValue(rs.getString("STATUS")));
        boeList.add(transactionVO);
      }
      if (boeList != null) {
        viewerVO.setBoeList(boeList);
      }
    }
    catch (Exception exception)
    {
      throwDAOException(exception);
    }
    finally
    {
      closeSqlRefferance(rs, loggableStatement, con);
    }
    logger.info("Exiting Method");
    return viewerVO;
  }
  public ArrayList<BoeVO> custData(BoeVO boeDetailsVO)
    throws DAOException
  {
    logger.info("Entering Method");
    BoeVO boeVO = null;
    ArrayList<BoeVO> custList = null;
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    String query = null;
    CommonMethods commonMethods = null;
    boolean cifFlag = false;
    boolean fullnameFlag = false;
    boolean numberFlag = false;
    int setValue = 0;
    try
    {
      commonMethods = new CommonMethods();
      custList = new ArrayList();
      con = DBConnectionUtility.getConnection();
      query = "select * from GFPF gf,extcust ext where ext.CUST=gf.GFCUS1 AND  ROWNUM<=10  ";
      String cif = boeDetailsVO.getCustomerCIFNo();
      String fullname = boeDetailsVO.getCustomerName();
      String number = boeDetailsVO.getCustomerNo();
      if (fullname != null) {
        fullname = fullname.toLowerCase();
      }
      if ((cif != null) && (cif.length() > 0))
      {
        query = query + " and GFCUS1 like '%'||?||'%'";
        cifFlag = true;
      }
      if ((fullname != null) && (fullname.length() > 0))
      {
        query = query + " and UPPER(GFCUN) like UPPER('%'||?||'%')";
        fullnameFlag = true;
      }
      if ((number != null) && (number.length() > 0))
      {
        query = query + " and GFCPNC like '%'||?||'%'";
        numberFlag = true;
      }
      pst = new LoggableStatement(con, query);
      if (cifFlag) {
        pst.setString(++setValue, cif);
      }
      if (fullnameFlag) {
        pst.setString(++setValue, fullname);
      }
      if (numberFlag) {
        pst.setString(++setValue, number);
      }
      rs = pst.executeQuery();
      while (rs.next())
      {
        boeVO = new BoeVO();
        boeVO.setCustNo(commonMethods.getEmptyIfNull(rs.getString("GFCPNC")).trim());
        boeVO.setCustName(commonMethods.getEmptyIfNull(rs.getString("GFCUN")).trim());
        boeVO.setCustCIFNo(commonMethods.getEmptyIfNull(rs.getString("GFCUS1")).trim());
        boeVO.setCountry(commonMethods.getEmptyIfNull(rs.getString("gfcnal")).trim());
        boeVO.setBlocked(commonMethods.getEmptyIfNull(rs.getString("gfcub")).trim());
        custList.add(boeVO);
      }
    }
    catch (Exception exception)
    {
      throwDAOException(exception);
    }
    finally
    {
      closeSqlRefferance(rs, pst, con);
    }
    logger.info("Exiting Method");
    return custList;
  }
}