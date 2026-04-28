package in.co.prishipment.dao;

import com.bs.wiseconnect.migration.loader.tiplus.pojos.TiattdocExtra;
import com.opensymphony.xwork2.ActionContext;
import in.co.prishipment.dao.exception.DAOException;
import in.co.prishipment.util.MapTokenResolver;
import in.co.prishipment.util.PreshipUtil;
import in.co.prishipment.util.TokenReplacingReader;
import in.co.prishipment.utility.ActionConstants;
import in.co.prishipment.utility.CommonMethods;
import in.co.prishipment.utility.DBConnectionUtility;
import in.co.prishipment.utility.LoggableStatement;
import in.co.prishipment.vo.AlertMessagesVO;
import in.co.prishipment.vo.PreShipmentVO;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

public class PreShipmentDAO
  extends AbstractDAO
  implements ActionConstants
{
  private static Logger logger = Logger.getLogger(PreShipmentDAO.class.getName());
  static PreShipmentDAO dao = null;
 
  public static PreShipmentDAO getDAO()
  {
    if (dao == null) {
      dao = new PreShipmentDAO();
    }
    return dao;
  }
 
  public static String ejbUrl = null;
  private ArrayList<AlertMessagesVO> alertMsgArray = new ArrayList();
 
  public ArrayList<AlertMessagesVO> getAlertMsgArray()
  {
    return this.alertMsgArray;
  }
 
  public void setErrorvalues(Object[] arg)
  {
    CommonMethods commonMethods = new CommonMethods();
    AlertMessagesVO altMsg = new AlertMessagesVO();
    altMsg.setErrorId(commonMethods.getEmptyIfNull(arg[1]).equalsIgnoreCase("W") ? "WARNING" : "ERROR");
    altMsg.setErrorDesc("GENERAL");
    altMsg.setErrorCode(commonMethods.getEmptyIfNull(arg[3]));
    altMsg.setErrorDetails(commonMethods.getEmptyIfNull(arg[2]));
    altMsg.setErrorMsg(commonMethods.getEmptyIfNull(arg[1]).equalsIgnoreCase("W") ? "N" : "");
    this.alertMsgArray.add(altMsg);
  }
 
  public void setAlertMsgArray(ArrayList<AlertMessagesVO> alertMsgArray)
  {
    this.alertMsgArray = alertMsgArray;
  }
 
  public String getSessionUserID(String sessionUserName)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    String sessionUserId = null;
    String query = "select skey80 from secage88 where name85 =?";
    try
    {
      con = DBConnectionUtility.getConnection();
      logger.info("Query111:" + query);
      pst = con.prepareStatement(query);
      pst.setString(1, sessionUserName);
      rs = pst.executeQuery();
      while (rs.next()) {
        sessionUserId = rs.getString("skey80");
      }
    }
    catch (Exception e)
    {
      throwDAOException(e);
    }
    return sessionUserId;
  }
 
  public String getValueDate()
    throws DAOException
  {
    String valDate = "";
    Connection connection = null;
    LoggableStatement logg = null;
    ResultSet res = null;
    try
    {
      SimpleDateFormat date = new SimpleDateFormat("dd-mm-yyyy");
      connection = DBConnectionUtility.getConnection();
      String query = "SELECT TO_CHAR(TO_DATE(PROCDATE, 'dd/mm/yy'),'dd/mm/yyyy') as PROCDATE1,PROCDATE FROM dlyprccycl";
      logg = new LoggableStatement(connection, query);
      res = logg.executeQuery();
      if (res.next()) {
        valDate = res.getString(1);
      }
    }
    catch (Exception e)
    {
      throwDAOException(e);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(connection, logg, res);
    }
    return valDate;
  }
 
  public void getTIDate()
    throws DAOException
  {
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    try
    {
      Map<String, Object> session = ActionContext.getContext().getSession();
      con = DBConnectionUtility.getConnection();
      logger.info("Before get Date ================>>> ");
      String query = "SELECT TO_CHAR(TO_DATE(PROCDATE, 'dd-mm-yy'),'dd-mm-yyyy') as PROCDATE1,PROCDATE FROM dlyprccycl";
      pst = new LoggableStatement(con, query);
      logger.info("Before get Date ================>>> 111 : ");
      rs = pst.executeQuery();
      if (rs.next())
      {
        String dateValue = CommonMethods.nullAndTrimString(rs.getString("PROCDATE1"));
        session.put("processDate", dateValue);
        logger.info("Before get Date ================>>> 222 : " + dateValue);
       
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        Timestamp dbSqlTimestamp = rs.getTimestamp("PROCDATE");
        String createdDate = df.format(dbSqlTimestamp);
        session.put("CREATEDATE", createdDate);
        logger.info("Before get Date ================>>> 333 : " + createdDate);
      }
    }
    catch (Exception exception)
    {
      logger.info("getTIDate------------Exception" + exception);
      throwDAOException(exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
  }
 
  public void getErrorMsg(String errorCode, String screenID, PreShipmentVO preship)
  {
    if ((this.alertMsgArray != null) &&
      (this.alertMsgArray.size() > 0)) {
      this.alertMsgArray.clear();
    }
    String errormsg = CommonMethods.getErrorDesc(errorCode, screenID);
    Object[] arg = { Integer.valueOf(0), "E", errormsg, "INPUT" };
    setErrorvalues(arg);
    if (this.alertMsgArray.size() > 0) {
      preship.setErrorList(this.alertMsgArray);
    }
  }
 
  public PreShipmentVO fetchPreShipment(PreShipmentVO preShipVo)
    throws DAOException
  {
    Connection con = null;
    LoggableStatement pst = null;
    LoggableStatement pst1 = null;
    ResultSet rs = null;
    ResultSet rs1 = null;
    LoggableStatement ps = null;
    ResultSet rs2 = null;
    String query = "";
    String osAmount = "";
    String amount = "";
    CommonMethods commonMethods = null;
    String sortBy = "";
    String sortBy2 = "";
   
    logger.info("1111111111111111111111---------------->" + query);
   
    preShipVo.setValueDate(preShipVo.getValueDate());
    if (preShipVo.getProdType() != null) {
      preShipVo.setProdType(preShipVo.getProdType());
    }
    try
    {
      commonMethods = new CommonMethods();
      if (checkValidation(preShipVo) == 1)
      {
        double payment1 = Double.parseDouble(preShipVo.getAmount());
        boolean paymentstatus = true;
        ArrayList<PreShipmentVO> shipList = new ArrayList();
        con = DBConnectionUtility.getConnection();
       
        logger.info("Is check box checked " + preShipVo.getAllCurr());
        boolean cifflag = false;
        boolean currflag = false;
        boolean prodflag = false;
        boolean sortflag = false;
        boolean facilityflag = false;
        int setindex = 0;
        if (preShipVo.getAllCurr().equalsIgnoreCase("false"))
        {
          if (preShipVo.getSortBy().equalsIgnoreCase("due"))
          {
            sortBy = "PRESHIPMENT_FIN_BASE_VIEW.FNC_MATURITY";
            sortBy2 = "PRESHIPMENT_FIN_BASE_VIEW.FNC_STARTDATE";
          }
          else
          {
            sortBy = "PRESHIPMENT_FIN_BASE_VIEW.FNC_STARTDATE";
            sortBy2 = "PRESHIPMENT_FIN_BASE_VIEW.FNC_MATURITY";
          }
          if (preShipVo.getFaciType().equalsIgnoreCase("-1"))
          {
            query = "select NVL(TRIM(PRESHIPMENT_FIN_BASE_VIEW.MAS_MASTER_REF), ' ') AS MASTER, TO_CHAR(TO_DATE(PRESHIPMENT_FIN_BASE_VIEW.FNC_STARTDATE, 'dd-mm-yy'),'dd-mm-yy') as START_DATE, TO_CHAR(TO_DATE(PRESHIPMENT_FIN_BASE_VIEW.FNC_MATURITY, 'dd-mm-yy'),'dd-mm-yy') as DUE_DATE, to_char(PRESHIPMENT_FIN_BASE_VIEW.FNC_FINCE_AMT,'999,999,999,999,999.99') AS LOAN_AMT, to_char(PRESHIPMENT_FIN_BASE_VIEW.MAS_AMT_O_S,'999,999,999,999,999.99') AS OUTSTANDING_AMT,MAS_CCY AS CURR,TO_CHAR(PRESHIPMENT_FIN_BASE_VIEW.MAS_STATUS) AS STATUS, MAS_KEY97, TRIM(MAS_BHALF_BRN) AS BHALF_BRN , TRIM(MAS_INPUT_BRN) AS INPUT_BRN,FACILITY_TYPE AS FACILITY from PRESHIPMENT_FIN_BASE_VIEW where PRESHIPMENT_FIN_BASE_VIEW.MAS_STATUS IN ('LIV')  and PRODUCT_CODE='FSA' AND PRESHIPMENT_FIN_BASE_VIEW.MAS_AMT_O_S > 0  AND PRESHIPMENT_FIN_BASE_VIEW.MAS_CCY in ('EUR','INR','GBP','JPY','USD')";
            String cifcode = preShipVo.getCifCode();
            if (!CommonMethods.isNull(cifcode))
            {
              query = query + " and trim(APPCUSTEMERID)=?";
              cifflag = true;
            }
            String curr = preShipVo.getCurrency();
            if (!CommonMethods.isNull(curr))
            {
              query = query + " and MAS_CCY=?";
              currflag = true;
            }
            String prod = preShipVo.getProdType();
            if (!CommonMethods.isNull(prod))
            {
              query = query + " and CODE=?";
              prodflag = true;
            }
            query = query + " ORDER BY TO_DATE(" + sortBy + ", 'dd-mm-yy')," + " TO_DATE(" + sortBy2 + ", 'dd-mm-yy')";
            System.out.println("Get Preshipment Query-----------1----------------->" + query);
            logger.info("Get Preshipment Query-----------1----------------->" + query);
          }
          else
          {
            query = "select NVL(TRIM(PRESHIPMENT_FIN_BASE_VIEW.MAS_MASTER_REF), ' ') AS MASTER, TO_CHAR(TO_DATE(PRESHIPMENT_FIN_BASE_VIEW.FNC_STARTDATE, 'dd-mm-yy'),'dd-mm-yy') as START_DATE, TO_CHAR(TO_DATE(PRESHIPMENT_FIN_BASE_VIEW.FNC_MATURITY, 'dd-mm-yy'),'dd-mm-yy') as DUE_DATE, to_char(PRESHIPMENT_FIN_BASE_VIEW.FNC_FINCE_AMT,'999,999,999,999,999.99') AS LOAN_AMT, to_char(PRESHIPMENT_FIN_BASE_VIEW.MAS_AMT_O_S,'999,999,999,999,999.99') AS OUTSTANDING_AMT,MAS_CCY AS CURR,TO_CHAR(PRESHIPMENT_FIN_BASE_VIEW.MAS_STATUS) AS STATUS, MAS_KEY97, TRIM(MAS_BHALF_BRN) AS BHALF_BRN , TRIM(MAS_INPUT_BRN) AS INPUT_BRN,FACILITY_TYPE AS FACILITY from PRESHIPMENT_FIN_BASE_VIEW where PRESHIPMENT_FIN_BASE_VIEW.MAS_STATUS IN ('LIV')  and PRODUCT_CODE='FSA' AND PRESHIPMENT_FIN_BASE_VIEW.MAS_AMT_O_S > 0  AND PRESHIPMENT_FIN_BASE_VIEW.MAS_CCY in ('EUR','INR','GBP','JPY','USD')";
           




            String cifcode = preShipVo.getCifCode();
            if (!CommonMethods.isNull(cifcode))
            {
              query = query + " and trim(APPCUSTEMERID)=?";
              cifflag = true;
            }
            String curr = preShipVo.getCurrency();
            if (!CommonMethods.isNull(curr))
            {
              query = query + " and MAS_CCY=?";
              currflag = true;
            }
            String prod = preShipVo.getProdType();
            if (!CommonMethods.isNull(prod))
            {
              query = query + " and CODE=?";
              prodflag = true;
            }
            String fac = preShipVo.getFaciType();
            if (!CommonMethods.isNull(fac))
            {
              query = query + " and FACILITY_CODE=?";
              facilityflag = true;
            }
            query = query + " ORDER BY TO_DATE(" + sortBy + ", 'dd-mm-yy')," + " TO_DATE(" + sortBy2 + ", 'dd-mm-yy')";
           
            System.out.println("Get Preshipment Query-----------2----------------->" + query);
            logger.info("Get Preshipment Query-----------2----------------->" + query);
          }
        }
        else
        {
          if (preShipVo.getSortBy().equalsIgnoreCase("due"))
          {
            sortBy = "PRESHIPMENT_FIN_BASE_VIEW.FNC_MATURITY";
            sortBy2 = "PRESHIPMENT_FIN_BASE_VIEW.FNC_STARTDATE";
          }
          else
          {
            sortBy = "PRESHIPMENT_FIN_BASE_VIEW.FNC_STARTDATE";
            sortBy2 = "PRESHIPMENT_FIN_BASE_VIEW.FNC_MATURITY";
          }
          if (preShipVo.getFaciType().equalsIgnoreCase("-1"))
          {
            query = "select NVL(TRIM(PRESHIPMENT_FIN_BASE_VIEW.MAS_MASTER_REF), ' ') AS MASTER, TO_CHAR(TO_DATE(PRESHIPMENT_FIN_BASE_VIEW.FNC_STARTDATE, 'dd-mm-yy'),'dd-mm-yy') as START_DATE, TO_CHAR(TO_DATE(PRESHIPMENT_FIN_BASE_VIEW.FNC_MATURITY, 'dd-mm-yy'),'dd-mm-yy') as DUE_DATE, to_char(PRESHIPMENT_FIN_BASE_VIEW.FNC_FINCE_AMT,'999,999,999,999,999.99') AS LOAN_AMT, to_char(PRESHIPMENT_FIN_BASE_VIEW.MAS_AMT_O_S,'999,999,999,999,999.99') AS OUTSTANDING_AMT,MAS_CCY AS CURR,TO_CHAR(PRESHIPMENT_FIN_BASE_VIEW.MAS_STATUS) AS STATUS, MAS_KEY97, TRIM(MAS_BHALF_BRN) AS BHALF_BRN , TRIM(MAS_INPUT_BRN) AS INPUT_BRN,FACILITY_TYPE AS FACILITY from PRESHIPMENT_FIN_BASE_VIEW where PRESHIPMENT_FIN_BASE_VIEW.MAS_STATUS IN ('LIV')  and PRODUCT_CODE='FSA' AND PRESHIPMENT_FIN_BASE_VIEW.MAS_AMT_O_S > 0  AND PRESHIPMENT_FIN_BASE_VIEW.MAS_CCY in ('EUR','INR','GBP','JPY','USD')";
            String cifcode = preShipVo.getCifCode();
            if (!CommonMethods.isNull(cifcode))
            {
              query = query + " and trim(APPCUSTEMERID)=?";
              cifflag = true;
            }
            query = query + " ORDER BY TO_DATE(" + sortBy + ", 'dd-mm-yy')," + " TO_DATE(" + sortBy2 + ", 'dd-mm-yy')";
            sortflag = true;
           
            System.out.println("Get Preshipment Query-----------3----------------->" + query);
            logger.info("Get Preshipment Query-------------3--------------->" + query);
          }
          else
          {
            query = "select NVL(TRIM(PRESHIPMENT_FIN_BASE_VIEW.MAS_MASTER_REF), ' ') AS MASTER, TO_CHAR(TO_DATE(PRESHIPMENT_FIN_BASE_VIEW.FNC_STARTDATE, 'dd-mm-yy'),'dd-mm-yy') as START_DATE, TO_CHAR(TO_DATE(PRESHIPMENT_FIN_BASE_VIEW.FNC_MATURITY, 'dd-mm-yy'),'dd-mm-yy') as DUE_DATE, to_char(PRESHIPMENT_FIN_BASE_VIEW.FNC_FINCE_AMT,'999,999,999,999,999.99') AS LOAN_AMT, to_char(PRESHIPMENT_FIN_BASE_VIEW.MAS_AMT_O_S,'999,999,999,999,999.99') AS OUTSTANDING_AMT,MAS_CCY AS CURR,TO_CHAR(PRESHIPMENT_FIN_BASE_VIEW.MAS_STATUS) AS STATUS, MAS_KEY97, TRIM(MAS_BHALF_BRN) AS BHALF_BRN , TRIM(MAS_INPUT_BRN) AS INPUT_BRN,FACILITY_TYPE AS FACILITY from PRESHIPMENT_FIN_BASE_VIEW where PRESHIPMENT_FIN_BASE_VIEW.MAS_STATUS IN ('LIV')  and PRODUCT_CODE='FSA' AND PRESHIPMENT_FIN_BASE_VIEW.MAS_AMT_O_S > 0  AND PRESHIPMENT_FIN_BASE_VIEW.MAS_CCY in ('EUR','INR','GBP','JPY','USD')";
            String cifcode = preShipVo.getCifCode();
            if (!CommonMethods.isNull(cifcode))
            {
              query = query + " and trim(APPCUSTEMERID)=?";
              cifflag = true;
            }
            query = query + " ORDER BY TO_DATE(" + sortBy + ", 'dd-mm-yy')," + " TO_DATE(" + sortBy2 + ", 'dd-mm-yy')";
           

            System.out.println("Get Preshipment Query-----------4----------------->" + query);
            logger.info("Get Preshipment Query--------------4-------------->" + query);
          }
        }
        pst = new LoggableStatement(con, query);
        if (cifflag) {
          pst.setString(++setindex, preShipVo.getCifCode());
        }
        if (currflag) {
          pst.setString(++setindex, preShipVo.getCurrency());
        }
        if (prodflag) {
          pst.setString(++setindex, preShipVo.getProdType());
        }
        if (facilityflag) {
          pst.setString(++setindex, preShipVo.getFaciType());
        }
        logger.info("FIN_BASE query " + pst.getQueryString());
       
        double out_amt_flag = 0.0D;
        int i = 0;
        rs = pst.executeQuery();
        double payment = payment1;
        while (rs.next())
        {
          String mas_ref = rs.getString("MASTER");
         







          String Outstanding_amt = "SELECT ETT.LOAN_REF, SUM(ETT.REPAYAMT/power(10,C8CED)) AS OUT_AMT FROM ETT_PRESHIPMENT_APISERVER ETT , BASEEVENT BEV,C8PF c8, (SELECT BEV.KEY97 AS BEV_KEY  FROM MASTER MAS,  BASEEVENT BEV,  EVENTSTEP EVS,  ORCH_MAP ORM WHERE MAS.KEY97   = BEV.MASTER_KEY AND BEV.KEY97     = EVS.EVENT_KEY  AND EVS.ORCH_MAP  = ORM.KEY97 AND ORM.ORCH_STEP = 16 AND BEV.STATUS   <>'c' )FP WHERE ETT.SUB_KEY = BEV.KEY97 AND ETT.CURR= c8.C8CCY AND BEV.KEY97     = FP.BEV_KEY(+) AND fp.BEV_KEY   IS NULL AND BEV.STATUS   IN ('i','c') AND ETT.LOAN_REF  = ? GROUP BY ETT.LOAN_REF";
         

















          logger.info("ETT_PRESHIPMENT_OUTSTANDING QUERY---------------------->" + Outstanding_amt);
          pst1 = new LoggableStatement(con, Outstanding_amt);
          pst1.setString(1, mas_ref);
          double total_ettpreshipment_table_repayamt = 0.0D;
          rs1 = pst1.executeQuery();
          while (rs1.next()) {
            total_ettpreshipment_table_repayamt = Double.valueOf(rs1.getString("OUT_AMT")).doubleValue();
          }
          String Outstanding_amt_manual = "Select mas.MASTER_REF as MAS_REF,SUM(PAY.PRI_PAID/power(10,C8CED)) AS OUT_AMT from master mas, baseevent bev,FINREPAY pay,C8PF c8  where mas.key97 = bev.MASTER_KEY  AND BEV.KEY97 = pay.KEY97  and bev.status<>'a'  AND C8.C8CCY= BEV.CCY   aND BEV.CREATNMTHD='M'  AND trim(MAS.MASTER_REF)= ?  group by MAS.MASTER_REF ";
         









          logger.info("ETT_PRESHIPMENT_MANUAL_OUTSTANDING QUERY---------------------->" + Outstanding_amt_manual);
          ps = new LoggableStatement(con, Outstanding_amt_manual);
          ps.setString(1, mas_ref);
          double total_manual_repayamt = 0.0D;
          rs2 = ps.executeQuery();
          logger.info("MANUAL_OUTSTANDING QUERY------------------>" + ps.getQueryString());
          while (rs2.next())
          {
            String outamt = rs2.getString("OUT_AMT");
            if (outamt != null) {
              total_manual_repayamt = Double.valueOf(outamt).doubleValue();
            }
          }
          String curr = "";
          PreShipmentVO preVo = new PreShipmentVO();
          preVo.setMasterRefNo(rs.getString("MASTER"));
          preVo.setFacilityID(rs.getString("FACILITY"));
          preVo.setStarDate(rs.getString("START_DATE"));
          preVo.setDueDate(rs.getString("DUE_DATE"));
          curr = rs.getString("CURR");
          preVo.setLoanAmount(CommonMethods.removeComma(rs.getString("LOAN_AMT")) + " " + curr);
          preVo.setKey97(rs.getString("MAS_KEY97"));
          preVo.setInputBranch(rs.getString("INPUT_BRN"));
          preVo.setBehalfOfBranch(rs.getString("BHALF_BRN"));
          osAmount = CommonMethods.removeComma(rs.getString("OUTSTANDING_AMT"));
          double Outamt = Double.valueOf(osAmount).doubleValue();
          double temp = Double.valueOf(osAmount).doubleValue();
          double loan_amt = Double.valueOf(CommonMethods.removeComma(rs.getString("LOAN_AMT"))).doubleValue();
         
          logger.info("Loan Amount-------------------->" + loan_amt);
         
          logger.info(
            "ETT PRESHIPMENT OUT AMT------------------------" + total_ettpreshipment_table_repayamt);
          logger.info(
            "ETT PRESHIPMENT manual OUT AMT------------------------" + total_manual_repayamt);
          logger.info("TORAL OUTSTANDING AMT---------------------------" + temp);
          logger.info("TOtAL OUTSTANDING AMT---------------------------" + Outamt);
          double total_repayamt = 0.0D;
          total_repayamt = total_ettpreshipment_table_repayamt + total_manual_repayamt;
         
          logger.info(
            "ETT PRESHIPMENT total OUT AMT------------------------" + total_repayamt);
          temp = loan_amt - total_repayamt;
          if (temp < 0.0D) {
            temp = 0.0D;
          }
          logger.info("BALANCE OUTSTADING ---------------------------" + temp);
          String abc = String.format("%.2f", new Object[] { Double.valueOf(temp) });
         
          logger.info("SETTING IN VARIABLE ---------------------------" + osAmount);
          preVo.setOutStandingAmt(abc + " " + curr);
         
          out_amt_flag = Double.valueOf(preVo.getOutStandingAmt().replaceAll("[^\\d.]", "")).doubleValue();
         
          preVo.setStatus(rs.getString("STATUS"));
          if ((!CommonMethods.findDouble(osAmount)) && (preShipVo.getAllCurr().equalsIgnoreCase("false")) &&
            (out_amt_flag != 0.0D))
          {
            double amt = Double.parseDouble(osAmount);
           
            logger.info("Old Outstanding Amount-------------------" + amt);
           


            logger.info("Payment Amount---------------------------" + payment);
            if (payment >= temp)
            {
              logger.info("If Part---------->" + payment);
             
              payment -= temp;
             
              String ab = String.format("%.2f", new Object[] { Double.valueOf(temp) });
              preVo.setRepayAmt(ab + "  " + curr);
            }
            else if ((payment <= temp) && (payment > 0.0D))
            {
              logger.info("Else Part---------->" + payment);
             
              String cd = String.format("%.2f", new Object[] { Double.valueOf(payment) });
             
              preVo.setRepayAmt(cd + " " + curr);
              payment -= temp;
            }
            else
            {
              logger.info("Second Else Part---------->");
             
              preVo.setRepayAmt("0.0 " + curr);
            }
          }
          else
          {
            logger.info("final Else Part---------->");
            preVo.setRepayAmt("0.0 " + curr);
          }
          if (preShipVo.getAllCurr().equalsIgnoreCase("true")) {
            preShipVo.setReadOnly("true");
          }
          if (out_amt_flag != 0.0D)
          {
            shipList.add(preVo);
            logger.info("Ship list " + shipList.toString());
          }
          logger.info("before error");
          preShipVo.setPreShipList(shipList);
          logger.info("org list value " + preShipVo.getPreShipList().toString());
        }
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      throwDAOException(e);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    preShipVo.setProdType(preShipVo.getProdType());
   
    logger.info("***********************************");
   

    return preShipVo;
  }
 
  private int checkValidation(PreShipmentVO preShipVo)
    throws DAOException
  {
    int flag = 1;
    ArrayList<PreShipmentVO> resetList = new ArrayList();
    String whole_Amount = "";
    String Amount_without_Currency = "";
    String Amount_with_Currency_only = "";
    try
    {
      if ((preShipVo.getAmount() != null) || (!preShipVo.getAmount().equalsIgnoreCase("")))
      {
        whole_Amount = preShipVo.getAmtWithCurr().toUpperCase();
        Amount_without_Currency = whole_Amount.replaceAll("[^\\d.]", "");
        Amount_with_Currency_only = whole_Amount.replaceAll("[^A-Z]", "");
        whole_Amount = Amount_without_Currency + Amount_with_Currency_only;
        preShipVo.setAmtWithCurr(whole_Amount);
      }
      if ((this.alertMsgArray != null) &&
        (this.alertMsgArray.size() > 0)) {
        this.alertMsgArray.clear();
      }
      String currency = "";
      String amount = "";
      if (CommonMethods.isNull(preShipVo.getCifCode()))
      {
        getErrors("0", preShipVo);
        flag = 0;
      }
      if ((preShipVo.getProdType().equalsIgnoreCase("-1")) && (preShipVo.getAllCurr().equalsIgnoreCase("false")))
      {
        getErrors("10", preShipVo);
        preShipVo.setPreShipList(resetList);
        flag = 0;
      }
      if (CommonMethods.isNull(preShipVo.getAmtWithCurr()))
      {
        getErrors("1", preShipVo);
        flag = 0;
      }
      else
      {
        String splitValues = preShipVo.getAmtWithCurr().trim();
        for (int i = 0; i < splitValues.length(); i++) {
          if (Character.isLetter(splitValues.charAt(i))) {
            currency = currency + splitValues.charAt(i);
          } else {
            amount = amount + splitValues.charAt(i);
          }
        }
        if (CommonMethods.findDouble(CommonMethods.removeComma(amount)))
        {
          getErrors("2", preShipVo);
          flag = 0;
        }
        else if (CommonMethods.stringToDouble(CommonMethods.removeComma(amount)) <= 0.0D)
        {
          getErrors("2", preShipVo);
          flag = 0;
        }
        else
        {
          preShipVo.setAmount(CommonMethods.removeComma(amount));
        }
        if (currency.equalsIgnoreCase(""))
        {
          getErrors("7", preShipVo);
          flag = 0;
        }
        else if (checkCurrency(currency) == 1)
        {
          getErrors("8", preShipVo);
          flag = 0;
        }
        else
        {
          preShipVo.setCurrency(currency);
        }
      }
      SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");
      String val_date = preShipVo.getValueDate().trim();
      String proc_date = getValueDate().trim();
      logger.info("Value date " + val_date + " Proc Date " + proc_date);
     
      Date date1 = format.parse(val_date);
     

      Date date2 = format.parse(proc_date);
      logger.info(date1 + "-------------------------------------------" + date2);
      if (date1.compareTo(date2) == 1)
      {
        getErrors("12", preShipVo);
        flag = 0;
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    logger.info("flag Status------------------------------------>" + flag);
    return flag;
  }
 
  private int checkCurrency(String currency)
    throws DAOException
  {
    logger.info("Entering Method");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    int flag = 1;
    try
    {
      con = DBConnectionUtility.getConnection();
      pst = new LoggableStatement(con, "select C8SCY,C8CUR from C8PF where C8SCY=? order by C8SCY");
      pst.setString(1, currency);
      logger.info(pst.getQueryString());
      rs = pst.executeQuery();
      if (rs.next())
      {
        flag = 0;
        logger.info("Executed");
      }
    }
    catch (Exception e)
    {
      throwDAOException(e);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    logger.info("Exiting Method");
    return flag;
  }
 
  public void getErrors(String errorCode, PreShipmentVO preShipVo)
  {
    String errormsg = CommonMethods.getErrorDesc(errorCode, "PS01");
    Object[] arg = { Integer.valueOf(0), "E", errormsg, "INPUT" };
    CommonMethods.setErrorvalues(arg, this.alertMsgArray);
    if (this.alertMsgArray.size() > 0) {
      preShipVo.setErrorList(this.alertMsgArray);
    }
  }
 
  public String show_Records(ArrayList<PreShipmentVO> list, PreShipmentVO preShipVo)
    throws DAOException
  {
    String result = "SUCCESS";
    ArrayList<PreShipmentVO> updated_list = new ArrayList();
    preShipVo.setPreShipList(updated_list);
    try
    {
      for (int i = 0; i < list.size(); i++)
      {
        double rePaymentAmt =
          Double.parseDouble(CommonMethods.removeComma(((PreShipmentVO)list.get(i)).getRepayAmt().replaceAll("[^\\d.]", "")));
        if ((rePaymentAmt > 0.0D) && (!((PreShipmentVO)list.get(i)).getStatus().equalsIgnoreCase("SUCCEEDED")) &&
          (!((PreShipmentVO)list.get(i)).getStatus().equalsIgnoreCase("FAILED")))
        {
          PreShipmentVO pVo = new PreShipmentVO();
          pVo.setMasterRefNo(((PreShipmentVO)list.get(i)).getMasterRefNo());
         
          pVo.setStarDate(((PreShipmentVO)list.get(i)).getStarDate());
          pVo.setDueDate(((PreShipmentVO)list.get(i)).getDueDate());
          pVo.setLoanAmount(((PreShipmentVO)list.get(i)).getLoanAmount());
          pVo.setOutStandingAmt(((PreShipmentVO)list.get(i)).getOutStandingAmt());
         

          pVo.setRepayAmt(((PreShipmentVO)list.get(i)).getRepayAmt());
          pVo.setStatus("SUCCEEDED");
          updated_list.add(pVo);
        }
      }
      preShipVo.setPreShipList(updated_list);
    }
    catch (Exception e)
    {
      logger.info("Exception is " + e.getMessage());
    }
    return result;
  }
 
  public String checkValueDate(String valDate, String startDate)
  {
    String result = "TRUE";
    try
    {
      logger.info("value Date " + valDate + "Start date " + startDate);
      SimpleDateFormat date = new SimpleDateFormat("dd/MM/yy");
      Date vDate = date.parse(valDate);
      vDate = date.parse(date.format(vDate));
      SimpleDateFormat date1 = new SimpleDateFormat("dd-MM-yy");
     
      Date sDate = date1.parse(startDate);
      logger.info("vdate " + vDate);
      logger.info("sdate " + sDate);
      logger.info("result befoer-------------------- " + vDate.compareTo(sDate));
      if (vDate.compareTo(sDate) == -1) {
        result = "FALSE";
      }
    }
    catch (Exception e)
    {
      logger.info("Exception is " + e.getMessage());
    }
    return result;
  }
 
  public PreShipmentVO repayProcess(PreShipmentVO preShipVo)
    throws DAOException
  {
    logger.info("---------repayProcess-----------");
    Connection con = null;
    String totalcurr = "";
    String totalamount = "";
   
    LoggableStatement pst = null;
    ResultSet rs = null;
    ArrayList<TiattdocExtra> invoiceList = null;
   
    ArrayList<PreShipmentVO> remainingList = new ArrayList();
    ArrayList<PreShipmentVO> tempList = new ArrayList();
    ArrayList<PreShipmentVO> addedList = new ArrayList();
    double payment = 0.0D;
   
    int batchID = 0;
   
    boolean findTrue = false;
    String loanno = "";
    Map<String, String> limitReversalXmlValues = null;
    try
    {
      limitReversalXmlValues = new HashMap();
     



      int a = preShipListValidation(preShipVo);
     
      logger.info("Validation COunt--------------------------------->" + a);
      if (a == 1)
      {
        int y = deletelocalLoan(preShipVo.getMasterRef().trim(), preShipVo.getEventRef().trim(),
          preShipVo.getCurrency().trim());
       
        int z = deleteLoan(preShipVo.getMasterRef().trim(), preShipVo.getEventRef().trim(),
          preShipVo.getCurrency().trim());
       
        preShipVo.setReadOnly("true");
       

        logger.info("Before COnverions-----------Date ===>" + preShipVo.getValueDate());
       
        String formated = getDateTimeChangeFormat(preShipVo.getValueDate(), "dd/mm/yy", "dd/mm/yyyy");
       
        logger.info("VALUE DATE After Conversion----------------===>" + formated);
       
        preShipVo.setValueDate(formated);
        for (int i = 0; i < preShipVo.getPreShipList().size(); i++)
        {
          double rePaymentAmt = 0.0D;
          String abcd = ((PreShipmentVO)preShipVo.getPreShipList().get(i)).getRepayAmt();
         
          String originalrepay_amt = ((PreShipmentVO)preShipVo.getPreShipList().get(i)).getAmtWithCurr();
          logger.info("originalrepay_amt--------------->" + originalrepay_amt);
          if ((!abcd.equalsIgnoreCase("")) && (abcd != null)) {
            rePaymentAmt = Double.parseDouble(CommonMethods.removeComma(
              ((PreShipmentVO)preShipVo.getPreShipList().get(i)).getRepayAmt().replaceAll("[^\\d.]", "")));
          } else {
            rePaymentAmt = 0.0D;
          }
          if ((rePaymentAmt > 0.0D) && (!((PreShipmentVO)preShipVo.getPreShipList().get(i)).getStatus().equalsIgnoreCase("SUCCEEDED")) &&
            (!((PreShipmentVO)preShipVo.getPreShipList().get(i)).getStatus().equalsIgnoreCase("SUCEEDED")))
          {
            findTrue = true;
            payment = Double.parseDouble(
              CommonMethods.removeComma(preShipVo.getAmount().replaceAll("[^\\d.]", "")));
            con = DBConnectionUtility.getConnection();
            loanno = ((PreShipmentVO)preShipVo.getPreShipList().get(i)).getMasterRefNo();
           
            pst = new LoggableStatement(con, "insert into ETT_PRESHIPMENT(CIF_CODE,VALUE_DATE,PAYMENT_AMOUNT,MAS_REF,DISBURSE_DATE,DUE_DATE,LOAN_AMOUNT,OUT_AMOUNT,REPAY_AMOUNT,STATUS,BASEEVENT_KEY97,INPUT_BRANCH,BEHALF_BRANCH,PRODUCT,EVENT,BATCHID,CURRENCY,CUS_MAS_REF,CUS_EVENT_REF) values(?,to_date(?,'DD/MM/YYYY'),TO_NUMBER(?,'999999999999999.99'),?,to_date(?,'DD/MM/YYYY'),to_date(?,'DD/MM/YYYY'),TO_NUMBER(?,'999999999999999.99'),TO_NUMBER(?,'999999999999999.99'),TO_NUMBER(?,'999999999999999.99'),?,?,?,?,?,?,?,?,?,?)");
            pst.setString(1, CommonMethods.nullAndTrimString(preShipVo.getCifCode()));
           


            pst.setString(2, CommonMethods.nullAndTrimString(preShipVo.getValueDate()));
            pst.setString(3, CommonMethods.removeComma(preShipVo.getAmount()).replaceAll("[^\\d.]", ""));
            pst.setString(4,
              CommonMethods.nullAndTrimString(((PreShipmentVO)preShipVo.getPreShipList().get(i)).getMasterRefNo()));
            pst.setString(5,
              CommonMethods.nullAndTrimString(((PreShipmentVO)preShipVo.getPreShipList().get(i)).getStarDate()));
            pst.setString(6,
              CommonMethods.nullAndTrimString(((PreShipmentVO)preShipVo.getPreShipList().get(i)).getDueDate()));
            pst.setString(7, CommonMethods.nullAndTrimString(CommonMethods.removeComma(
              ((PreShipmentVO)preShipVo.getPreShipList().get(i)).getLoanAmount().replaceAll("[^\\d.]", ""))));
            pst.setString(8, CommonMethods.nullAndTrimString(CommonMethods.removeComma(
              ((PreShipmentVO)preShipVo.getPreShipList().get(i)).getOutStandingAmt().replaceAll("[^\\d.]", ""))));
           



            pst.setString(9, CommonMethods.nullAndTrimString(CommonMethods.removeComma(
              ((PreShipmentVO)preShipVo.getPreShipList().get(i)).getRepayAmt().replaceAll("[^\\d.]", ""))));
            pst.setString(10,
              CommonMethods.nullAndTrimString(((PreShipmentVO)preShipVo.getPreShipList().get(i)).getStatus()));
            pst.setString(11,
              CommonMethods.nullAndTrimString(((PreShipmentVO)preShipVo.getPreShipList().get(i)).getKey97()));
            pst.setString(12,
              CommonMethods.nullAndTrimString(((PreShipmentVO)preShipVo.getPreShipList().get(i)).getInputBranch()));
            pst.setString(13,
              CommonMethods.nullAndTrimString(((PreShipmentVO)preShipVo.getPreShipList().get(i)).getBehalfOfBranch()));
            pst.setString(14, "FSA");
            pst.setString(15, "RSA");
            batchID = getBatchSeq(con);
            pst.setString(16, batchID);
            pst.setString(17, preShipVo.getCurrency());
            pst.setString(18, preShipVo.getMasterRef());
            pst.setString(19, preShipVo.getEventRef());
            logger.info("TABLE --------------repayProcess--inSERT qUERY-----------------------------> " +
              pst.getQueryString());
            int count = pst.executeUpdate();
           
            logger.info("TABLE -------------repayProcess---inSERT qUERY--------count---------------------> " +
              count);
            BigDecimal totalAmt = BigDecimal.ZERO;
            BigDecimal totalAmt2 = new BigDecimal(rePaymentAmt);
            totalAmt2 = totalAmt2.setScale(2, 6);
           
            BigDecimal amt = BigDecimal.valueOf(100L);
            totalAmt = totalAmt2.multiply(amt);
            totalAmt = totalAmt.setScale(2, 6);
            invoiceList = transferBuyerVO((PreShipmentVO)preShipVo.getPreShipList().get(i), preShipVo.getCifCode());
            String wholeAmount = preShipVo.getAmtWithCurr();
            totalamount = ((PreShipmentVO)preShipVo.getPreShipList().get(i)).getRepayAmt().replaceAll("[^\\d.]", "");
           
            Currency ccyNameCode = Currency.getInstance(preShipVo.getCurrency());
            logger.info("Currency:" + ccyNameCode);
            int precision = ccyNameCode.getDefaultFractionDigits();
            RoundingMode DEFAULT_ROUNDING = RoundingMode.HALF_EVEN;
            logger.info("CCY_precision " + precision);
           
            double minorValueD = Math.pow(10.0D, precision);
           
            BigDecimal denominator = BigDecimal.valueOf(minorValueD);
            logger.info("CCY_denominatorValue " + denominator);
           
            BigDecimal repayValue1 = null;
            BigDecimal repayValue2 = null;
            BigDecimal totalamountBg = new BigDecimal(totalamount);
           
            repayValue1 = totalamountBg.setScale(precision, DEFAULT_ROUNDING);
            logger.info("RepayValue before multiply " + repayValue1);
           
            repayValue2 = repayValue1.multiply(denominator);
           

            BigDecimal repayValue = repayValue2.setScale(0, 1);
           






















            logger.info("RepayValue BigDecimal(WholeValue) : " + repayValue);
            totalcurr = wholeAmount.substring(wholeAmount.length() - 3).trim();
           

            limitReversalXmlValues = getDataForReversalXml(preShipVo.getMasterRef(), preShipVo.getEventRef(), loanno, repayValue.toString(),
              preShipVo.getCurrency(), preShipVo.getProdType(),
              CommonMethods.nullAndTrimString(preShipVo.getCifCode()), preShipVo.getValueDate());
           
            logger.info("PreshipmentDAO 653 limit Reversal Value XML ----------------->" +
              limitReversalXmlValues);
           

            String finalReversalXml = generateReservationXml(limitReversalXmlValues);
           
            logger.info("PreshipmentDAO 658 limit Final Remersal  Value XML ----------------->" +
              finalReversalXml);
           
            logger.info("---------------------------------Value--------------------Date----------" +
              preShipVo.getValueDate());
           
            String statusOfXml = processSTPBuyerFinance(con, invoiceList, "", "REPAIR",
              batchID, totalAmt2, preShipVo.getCurrency(), preShipVo.getMasterRef(),
              preShipVo.getEventRef(), repayValue,
              CommonMethods.nullAndTrimString(preShipVo.getCifCode()), totalamount, totalcurr, loanno,
              preShipVo.getValueDate(), preShipVo.getProdType(), finalReversalXml);
           
            logger.info("PreshipmentDAO 664-------------STATUS OF XML--------------->" + statusOfXml);
           









            callprocedure(con, preShipVo.getMasterRef());
           
            ((PreShipmentVO)preShipVo.getPreShipList().get(i)).setStatus("SUCCEEDED");
            addedList.add((PreShipmentVO)preShipVo.getPreShipList().get(i));
          }
          else
          {
            remainingList.add((PreShipmentVO)preShipVo.getPreShipList().get(i));
          }
          logger.info("I value---->" + i);
        }
        if (findTrue)
        {
          logger.info("true");
          preShipVo.setPreShipList(tempList);
          preShipVo.setIsSubmitted("Y");
        }
      }
      preShipVo.setPreShipList(addedList);
    }
    catch (Exception e)
    {
      logger.info("---------repayProcess---- Exception-------" + e);
      e.printStackTrace();
      throwDAOException(e);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    return preShipVo;
  }
 
  public String generateReservationXml(Map<String, String> xmlValueList)
  {
    String requestReservationXml = "";
    InputStream anInputStream = null;
    try
    {
      PreShipmentDAO dao = new PreShipmentDAO();
      anInputStream = dao.getClass().getResourceAsStream("Reversal.xml");
     





      String requestTemplate = PreshipUtil.readFile(anInputStream);
     
      Map<String, String> tokens = new HashMap();
      tokens.put("CreationDate", (String)xmlValueList.get("CreationDate"));
      tokens.put("TransactionId", (String)xmlValueList.get("TransactionId"));
      tokens.put("MasterReference", (String)xmlValueList.get("MasterReference"));
      tokens.put("FacilityIdentifier", (String)xmlValueList.get("FacilityIdentifier"));
      tokens.put("ProductSubType", (String)xmlValueList.get("ProductSubType"));
      tokens.put("MasReference", (String)xmlValueList.get("MasReference"));
      tokens.put("EventReference", (String)xmlValueList.get("EventReference"));
      tokens.put("Customer", (String)xmlValueList.get("Customer"));
      tokens.put("InputBranch", (String)xmlValueList.get("InputBranch"));
      tokens.put("BehalfOfBranch", (String)xmlValueList.get("BehalfOfBranch"));
     
      logger.info("AMOUNTMAP: " + (String)xmlValueList.get("Amount"));
     
      tokens.put("Amount", (String)xmlValueList.get("Amount"));
      tokens.put("Currency", (String)xmlValueList.get("Currency"));
     
      MapTokenResolver resolver = new MapTokenResolver(tokens);
      Reader fileValue = new StringReader(requestTemplate);
      Reader reader = new TokenReplacingReader(fileValue, resolver);
      requestReservationXml = reader.toString();
     
      logger.info("Rversal XML------------------------------->" + requestReservationXml);
      reader.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
      logger.info("XML Generation Exception " + e.getMessage());
    }
    return requestReservationXml;
  }
 
  public Map<String, String> getDataForReversalXml(String Masterref, String Eventref, String masRef, String amount, String curr, String productSubType, String customer, String valueDate)
  {
    Connection con = null;
    LoggableStatement preparedStatement = null;
    ResultSet resultSet = null;
    Map<String, String> dataValuesForXml = null;
    CommonMethods commonMethods = null;
    try
    {
      commonMethods = new CommonMethods();
      con = DBConnectionUtility.getConnection();
      dataValuesForXml = new HashMap();
      String query = "SELECT AMASTER.INPUT_BRN,AMASTER.BHALF_BRN,AECMSTATUS.FACILTYID ,(SELECT TO_CHAR(DLYPRCCYCL.PROCDATE,'YYYY-MM-DD') FROM DLYPRCCYCL) FROM MASTER AMASTER,   ECMSTATUS AECMSTATUS  WHERE AMASTER.KEY97   =AECMSTATUS.MASTER_KEY  AND TRIM(AMASTER.MASTER_REF)=?";
     

      preparedStatement = new LoggableStatement(con, query);
      preparedStatement.setString(1, masRef.trim());
      logger.info(
        "PreshipementDAO Line 652 Reservation Query---------------> " + preparedStatement.getQueryString());
      resultSet = preparedStatement.executeQuery();
      if (resultSet.next())
      {
        dataValuesForXml.put("InputBranch", commonMethods.getEmptyIfNull(resultSet.getString(1)).trim());
        dataValuesForXml.put("BehalfOfBranch", commonMethods.getEmptyIfNull(resultSet.getString(2)).trim());
        dataValuesForXml.put("FacilityIdentifier", commonMethods.getEmptyIfNull(resultSet.getString(3)).trim());
      }
      logger.info("valueDate: " + valueDate);
      SimpleDateFormat date = new SimpleDateFormat("dd/mm/yyyy");
      SimpleDateFormat sm = new SimpleDateFormat("yyyy-mm-dd");
      String strDate = sm.format(date.parse(valueDate));
      logger.info("strDate: " + strDate);
      dataValuesForXml.put("CreationDate", strDate);
      dataValuesForXml.put("MasterReference", masRef);
      logger.info("MASREFERENCE: " + Masterref);
      logger.info("EVENTREFERENCE: " + Eventref);
      dataValuesForXml.put("MasReference", Masterref);
      dataValuesForXml.put("EventReference", Eventref);
      dataValuesForXml.put("Customer", customer);
      logger.info("AmountXML: " + amount);
      dataValuesForXml.put("Amount", amount);
      dataValuesForXml.put("Currency", curr);
      dataValuesForXml.put("ProductSubType", productSubType);
      dataValuesForXml.put("TransactionId", PreshipUtil.randomCorrelationId());
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, preparedStatement, resultSet);
    }
    return dataValuesForXml;
  }
 
  public void callprocedure(Connection con, String masterref)
  {
    String getmasterquery = "select EX.REPEVNT,EX.REPMAS from extevent ex,master ma,BASEEVENT bs where BS.MASTER_KEY = MA.KEY97 and BS.KEY97 = EX.EVENT and MA.MASTER_REF = ? and MA.REFNO_PFIX = 'FSA'";
   

    LoggableStatement ps = null;
    ResultSet rs = null;
    try
    {
      ps = new LoggableStatement(con, getmasterquery);
      ps.setString(1, masterref);
      rs = ps.executeQuery();
      while (rs.next())
      {
        String event = rs.getString("REPEVNT");
        String repaymaster = rs.getString("REPMAS");
        String procedure = "{call ETT_PRESHIPMENT_CLOSURE(?,?,?)} ";
        LoggableStatement loggableStatement = new LoggableStatement(con, procedure);
        loggableStatement.setString(1, repaymaster);
        loggableStatement.setString(2, event);
        loggableStatement.setString(3, masterref);
        loggableStatement.executeUpdate();
        loggableStatement.close();
      }
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
  }
 
  public static String getDateTimeChangeFormat(String date, String currFrmt, String ChngeFormt)
  {
    String frmtChngeDte = "";
    SimpleDateFormat dateCurrFormat = new SimpleDateFormat(currFrmt);
    SimpleDateFormat dateChngeFormat = new SimpleDateFormat(ChngeFormt);
    try
    {
      Date valueDate = dateCurrFormat.parse(date);
      frmtChngeDte = dateChngeFormat.format(valueDate);
      logger.info("getDateTimeChangeFormat-----After Conversion---------" + frmtChngeDte);
    }
    catch (ParseException e)
    {
      logger.info("Excepton getDateTimeChangeFormat------------" + e);
     
      e.printStackTrace();
    }
    return frmtChngeDte;
  }
 
  public double getDecimalforCurrency(String curr)
  {
    double deci = 0.0D;
    Connection conn = null;
    LoggableStatement pre = null;
    ResultSet res = null;
    try
    {
      conn = DBConnectionUtility.getConnection();
      String query = "select power(10,C8CED) from C8PF where trim(c8ccy)=?";
      logger.info("Qurey is " + query);
      pre = new LoggableStatement(conn, query);
      pre.setString(1, curr);
      res = pre.executeQuery();
      if (res.next()) {
        deci = res.getDouble(1);
      }
    }
    catch (Exception e)
    {
      logger.info("exception is " + e.getMessage());
    }
    finally
    {
      DBConnectionUtility.surrenderDB(conn, pre, res);
    }
    return deci;
  }
 
  private int preShipListValidation(PreShipmentVO preShipVo)
    throws DAOException
  {
    int flag = 1;
    int count = 0;
    int checkPayment = 0;
    int checkLoop = 0;
    BigDecimal amountt = new BigDecimal(0);
   
    BigDecimal paymentAmt = new BigDecimal(0);
    double out_amt = 0.0D;
    BigDecimal paymentamount = new BigDecimal(0);
    BigDecimal repayamt = new BigDecimal(0);
    BigDecimal loanAmount = new BigDecimal(0);
    BigDecimal payamt = new BigDecimal(0);
    try
    {
      String amount = "";
      String currency = "";
      Double Amt = Double.valueOf(0.0D);
      Double ostAmt = Double.valueOf(0.0D);
      double repay_amt = 0.0D;
      if ((this.alertMsgArray != null) &&
        (this.alertMsgArray.size() > 0)) {
        this.alertMsgArray.clear();
      }
      if (preShipVo.getPreShipList().size() > 0)
      {
        for (int i = 0; i < preShipVo.getPreShipList().size(); i++)
        {
          String repay_amt1 = ((PreShipmentVO)preShipVo.getPreShipList().get(i)).getRepayAmt();
          if (repay_amt1.equalsIgnoreCase("")) {
            repay_amt = 0.0D;
          } else {
            repay_amt =
              Double.valueOf(((PreShipmentVO)preShipVo.getPreShipList().get(i)).getRepayAmt().replaceAll("[^\\d.]", "")).doubleValue();
          }
          String abc = String.valueOf(repay_amt);
          if (repay_amt != 0.0D) {
            if (checkValueDate(preShipVo.getValueDate(), ((PreShipmentVO)preShipVo.getPreShipList().get(i)).getStarDate()).equalsIgnoreCase("FALSE")) {
              count++;
            }
          }
          if (!((PreShipmentVO)preShipVo.getPreShipList().get(i)).getRepayAmt().equalsIgnoreCase("")) {
            ((PreShipmentVO)preShipVo.getPreShipList().get(i)).getRepayAmt();
          }
          String re_amt = ((PreShipmentVO)preShipVo.getPreShipList().get(i)).getRepayAmt();
          if (re_amt.equalsIgnoreCase("")) {
            ostAmt = Double.valueOf(0.0D);
          } else {
            ostAmt =
              Double.valueOf(((PreShipmentVO)preShipVo.getPreShipList().get(i)).getRepayAmt().replaceAll("[^\\d.]", ""));
          }
          out_amt =
            Double.valueOf(((PreShipmentVO)preShipVo.getPreShipList().get(i)).getOutStandingAmt().replaceAll("[^\\d.]", "")).doubleValue();
         
          Amt = Double.valueOf(preShipVo.getAmtWithCurr().replaceAll("[^\\d.]", ""));
          if (out_amt < ostAmt.doubleValue())
          {
            logger.info("Error 1----------------------");
            getErrors("11", preShipVo);
            flag = 0;
          }
        }
        logger.info("OustStanding Amount----- From form Submission  Values--------- " + out_amt);
       
        logger.info("Url passing Amount-------------- " + Amt);
       
        logger.info("Entered  passing Amount-------------- " + ostAmt);
        if (count > 0)
        {
          logger.info("Error 2----------------------");
          getErrors("13", preShipVo);
          flag = 0;
        }
      }
      logger.info("ossssssss----------------------->  " + ostAmt);
      if (CommonMethods.isNull(preShipVo.getAmtWithCurr()))
      {
        logger.info("Error 3----------------------");
        getErrors("1", preShipVo);
        flag = 0;
      }
      else
      {
        String splitValues = preShipVo.getAmtWithCurr().trim();
        for (int i = 0; i < splitValues.length(); i++) {
          if (Character.isLetter(splitValues.charAt(i))) {
            currency = currency + splitValues.charAt(i);
          } else {
            amount = amount + splitValues.charAt(i);
          }
        }
        if (CommonMethods.findDouble(CommonMethods.removeComma(amount)))
        {
          logger.info("Error 4----------------------");
          getErrors("2", preShipVo);
          flag = 0;
        }
        else if (CommonMethods.stringToDouble(CommonMethods.removeComma(amount)) <= 0.0D)
        {
          logger.info("Error 5----------------------");
          getErrors("2", preShipVo);
          flag = 0;
        }
        else
        {
          preShipVo.setAmount(CommonMethods.removeComma(amount));
          paymentAmt = new BigDecimal(Double.parseDouble(CommonMethods.removeComma(preShipVo.getAmount())));
          Currency ccyNameCode = Currency.getInstance(currency);
          int precision = ccyNameCode.getDefaultFractionDigits();
          RoundingMode DEFAULT_ROUNDING = RoundingMode.HALF_EVEN;
         


          paymentamount = paymentAmt.setScale(precision, DEFAULT_ROUNDING);
        }
        if (currency.equalsIgnoreCase(""))
        {
          logger.info("Error 6----------------------");
          getErrors("7", preShipVo);
          flag = 0;
        }
        else if (checkCurrency(currency) == 1)
        {
          logger.info("Error 7----------------------");
          getErrors("8", preShipVo);
          flag = 0;
        }
        else
        {
          preShipVo.setCurrency(currency);
        }
      }
      if ((preShipVo != null) && (preShipVo.getPreShipList().size() > 0))
      {
        for (int i = 0; i < preShipVo.getPreShipList().size(); i++) {
          if ((CommonMethods.findDouble(CommonMethods.removeComma(((PreShipmentVO)preShipVo.getPreShipList().get(i)).getRepayAmt()))) ||
         
            (CommonMethods.findDouble(CommonMethods.removeComma(((PreShipmentVO)preShipVo.getPreShipList().get(i)).getOutStandingAmt()))))
          {
            checkLoop = 1;
           

            BigDecimal osAmt = new BigDecimal(Double.parseDouble(
              CommonMethods.removeComma(((PreShipmentVO)preShipVo.getPreShipList().get(i)).getOutStandingAmt()).replaceAll("[^\\d.]", "")));
           

            Currency ccyNameCode = Currency.getInstance(currency);
            int precision = ccyNameCode.getDefaultFractionDigits();
            RoundingMode DEFAULT_ROUNDING = RoundingMode.HALF_EVEN;
           


            payamt = osAmt.setScale(precision, DEFAULT_ROUNDING);
           




            BigDecimal payAmt = new BigDecimal(Double.parseDouble(CommonMethods.removeComma(
              ((PreShipmentVO)preShipVo.getPreShipList().get(i)).getRepayAmt().replaceAll("[^\\d.]", ""))));
           




            repayamt = payAmt.setScale(precision, DEFAULT_ROUNDING);
           






            logger.info("repayamt Amt------test----------------------------------->" + repayamt);
           
            logger.info("Outstanding Amt------test----------------------------------->" + osAmt);
            logger.info("Outstanding Amt-------->" + payamt);
            amountt = amountt.add(repayamt);
           
            logger.info("Total Repay Amt---------test------------------------------->" + amountt);
            int amt = 0;
            amt = payamt.compareTo(repayamt);
            logger.info("amt---->" + amt);
            if (amt == -1) {
              checkPayment = 1;
            }
          }
        }
        int amtt = 0;
        amtt = amountt.compareTo(paymentamount);
        logger.info("amountt----------------------------->" + amountt);
        logger.info("paymentamount----------------------------->" + paymentamount);
        logger.info("amtt----------------------------->" + amtt);
        if ((checkLoop == 1) && (amtt != 0))
        {
          logger.info("Error 8----------------------");
          getErrors("6", preShipVo);
          flag = 0;
        }
        if (checkPayment == 1)
        {
          logger.info("Error 9----------------------");
          getErrors("9", preShipVo);
          flag = 0;
        }
      }
      else
      {
        flag = 0;
      }
      if ((preShipVo != null) && (preShipVo.getPreShipList().size() > 0))
      {
        Boolean errorStatus = Boolean.valueOf(true);
       
        BigDecimal paymentamt = new BigDecimal(0);
        String payAMT = "";
        for (int i = 0; i < preShipVo.getPreShipList().size(); i++)
        {
          String master_ref = ((PreShipmentVO)preShipVo.getPreShipList().get(i)).getMasterRefNo();
          logger.info("Mile stone");
          payAMT = CommonMethods.removeComma(((PreShipmentVO)preShipVo.getPreShipList().get(i)).getRepayAmt().replaceAll("[^\\d.]", ""));
          logger.info("DAO Repay Amount---->" + payAMT);
          paymentamt = BigDecimal.valueOf(Double.valueOf(payAMT).doubleValue());
         
          logger.info("DAO Master Reference--->" + master_ref);
          logger.info("DAO Repay Amount---->" + paymentamt);
          errorStatus = Boolean.valueOf(CommonMethods.dopreshipmentKnockoffAmountValidation(master_ref, paymentamt));
          if (errorStatus.booleanValue())
          {
            flag = 0;
            getErrors("14", preShipVo);
          }
        }
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      throwDAOException(e);
    }
    return flag;
  }
 
  private ArrayList<TiattdocExtra> transferBuyerVO(PreShipmentVO preShipmentVO, String customer)
    throws DAOException
  {
    TiattdocExtra docExtra = null;
    ArrayList<TiattdocExtra> list = null;
    try
    {
      list = new ArrayList();
      if (preShipmentVO != null)
      {
        PreShipmentVO temp = preShipmentVO;
        docExtra = new TiattdocExtra();
       
        docExtra.setBranch(temp.getInputBranch());
        docExtra.setBehalfofbranch(temp.getBehalfOfBranch());
        docExtra.setTeam("REPAIR");
       
        docExtra.setProduct("FSA");
        docExtra.setCustomer(customer);
        docExtra.setEvent("RSA");
        docExtra.setTheirreference(temp.getMasterRefNo());
       
        list.add(docExtra);
      }
    }
    catch (Exception e)
    {
      throwDAOException(e);
    }
    return list;
  }
 
  /* Error */
  public String processSTPBuyerFinance(Connection con, ArrayList<TiattdocExtra> invoiceList, String userName, String userTeam, String Batchid, BigDecimal repayAmt, String currency, String MasterRef, String EventRef, BigDecimal repayAmt2, String cifcode, String totalla, String totalC, String Loanreff, String valueDate, String prodType, String reservationRequest)
    throws Exception
  {

  }
 
  public int deletelocalLoan(String MasterRef, String EventRef, String curr)
    throws DAOException
  {
    int r = 0;
   
    Connection con = null;
    LoggableStatement log = null;
    try
    {
      con = DBConnectionUtility.getConnection();
      String deleting_query = "delete from ett_preshipment_apiserver where trim(masref)=? AND trim(eventref)=? AND CURR=?";
     
      logger.info("Delete query" + deleting_query);
      log = new LoggableStatement(con, deleting_query);
      log.setString(1, MasterRef.trim());
      log.setString(2, EventRef);
      log.setString(3, curr);
      r = log.executeUpdate();
    }
    catch (Exception e)
    {
      throwDAOException(e);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, log, null);
    }
    return r;
  }
 
  public int deleteLoan(String MasterRef, String EventRef, String curr)
    throws DAOException
  {
    int r = 0;
   
    Connection con = null;
    LoggableStatement log = null;
    try
    {
      con = DBConnectionUtility.getConnection();
      String deleting_query = "delete from ett_preshipment where trim(CUS_MAS_REF)=? AND trim(CUS_EVENT_REF)=? AND CURRENCY=?";
     
      logger.info("Delete query" + deleting_query);
      log = new LoggableStatement(con, deleting_query);
      log.setString(1, MasterRef.trim());
      log.setString(2, EventRef);
      log.setString(3, curr);
      logger.info("Delete query" + log.getQueryString());
      r = log.executeUpdate();
    }
    catch (Exception e)
    {
      throwDAOException(e);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, log, null);
    }
    return r;
  }
 
  private int getBatchSeq(Connection con)
    throws DAOException
  {
    PreparedStatement ppst = null;
   
    ResultSet rsst = null;
    int seq = 0;
    try
    {
      String sq = "SELECT PRESHIPMENT_SEQNO.NEXTVAL FROM DUAL";
      ppst = con.prepareStatement(sq);
      rsst = ppst.executeQuery();
      while (rsst.next()) {
        seq = rsst.getInt(1);
      }
    }
    catch (Exception e)
    {
      logger.info("Exception Inv Seq " + e.getMessage());
     
      throwDAOException(e);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(null, ppst, rsst);
    }
    return seq;
  }
 
  public String getTIDate(Connection con)
    throws DAOException
  {
    PreparedStatement ppt = null;
    ResultSet rs = null;
    String sqlQuery = null;
    String tiDate = "";
    try
    {
      sqlQuery = "select to_char (to_date(procdate,'dd-mm-yy'),'dd-mm-yy') as procdate from dlyprccycl";
      con = DBConnectionUtility.getConnection();
     
      ppt = con.prepareStatement(sqlQuery);
      logger.info("Get TI Date " + sqlQuery);
      rs = ppt.executeQuery();
      if (rs.next()) {
        tiDate = rs.getString("procdate");
      }
    }
    catch (SQLException e)
    {
      logger.info("Exception 7  " + e.getMessage());
     
      throwDAOException(e);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(null, ppt, rs);
    }
    return tiDate;
  }
 
  public PreShipmentVO fetchdeletePreship(PreShipmentVO preShipVo)
    throws DAOException
  {
    PreparedStatement ppt = null;
    Connection con = null;
    ResultSet rs = null;
    String Query = null;
   
    String masterref = preShipVo.getMasterRef();
    String eventref = preShipVo.getEventRef();
    String currency = preShipVo.getCurrency();
    String cifcode = preShipVo.getCifCode();
    String curr = null;
   




    ArrayList<PreShipmentVO> shipLis = new ArrayList();
    try
    {
      con = DBConnectionUtility.getConnection();
      Query = "select mas_ref as MASTER,CIF_CODE as FACILITY, DISBURSE_DATE as START_DATE,DUE_DATE , BATCHID, currency as CURR,LOAN_AMOUNT as LOAN_AMT,OUT_AMOUNT   as OUTSTANDING_AMT,REPAY_AMOUNT AS REPAY_AMNT,STATUS from ett_preshipment where CUS_MAS_REF=? and CUS_EVENT_REF=? and cif_code =? and currency=?";
     


      ppt = con.prepareStatement(Query);
      ppt.setString(1, masterref);
      ppt.setString(2, eventref);
      ppt.setString(3, cifcode);
      ppt.setString(4, currency);
      logger.info("Get Query " + Query);
     
      rs = ppt.executeQuery();
      while (rs.next())
      {
        PreShipmentVO preVo = new PreShipmentVO();
       
        preVo.setMasterRefNo(rs.getString("MASTER"));
        preVo.setFacilityID(rs.getString("FACILITY"));
        preVo.setStarDate(rs.getString("START_DATE"));
        preVo.setDueDate(rs.getString("DUE_DATE"));
        curr = rs.getString("CURR");
        preVo.setLoanAmount(CommonMethods.removeComma(rs.getString("LOAN_AMT")) + " " + curr);
        preVo.setOutStandingAmt(rs.getString("OUTSTANDING_AMT"));
        preVo.setRepayAmt(rs.getString("REPAY_AMNT"));
        preVo.setStatus(rs.getString("STATUS"));
        preVo.setBatchid(rs.getString("BATCHID"));
        shipLis.add(preVo);
      }
      preShipVo.setPreShipListdel(shipLis);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, ppt, null);
    }
    return preShipVo;
  }
 
  public PreShipmentVO deletefetchPreship(PreShipmentVO preShipVo)
    throws DAOException
  {
    PreparedStatement ppt = null;
    Connection con = null;
    ResultSet rs = null;
    String Query = null;
    PreparedStatement ppt1 = null;
    Connection con1 = null;
    ResultSet rs1 = null;
    String Query1 = null;
   
    String masref = preShipVo.getMasterRef();
    String eventref = preShipVo.getEventRef();
    String currency = preShipVo.getCurrency();
    String cifcode = preShipVo.getCifCode();
    String curr = null;
    try
    {
      con = DBConnectionUtility.getConnection();
     










      Query = "delete  from ett_preshipment where  cus_mas_ref=? and CUS_EVENT_REF=? and cif_code =? and currency=?";
     
      ppt = con.prepareStatement(Query);
      ppt.setString(1, masref);
     


      ppt.setString(2, eventref);
      ppt.setString(3, cifcode);
      ppt.setString(4, currency);
     
      logger.info("delete Query ett_preshipment " + Query);
      ppt.executeUpdate();
     
      con1 = DBConnectionUtility.getConnection();
      Query1 = "delete  from ETT_PRESHIPMENT_APISERVER where  MASREF=? and EVENTREF=? and CIFCODE =? and curr=?";
     
      ppt1 = con.prepareStatement(Query1);
      ppt1.setString(1, masref);
     


      ppt1.setString(2, eventref);
      ppt1.setString(3, cifcode);
      ppt1.setString(4, currency);
     
      ppt1.executeUpdate();
     
      logger.info("delete Query ETT_PRESHIPMENT_APISERVER" + Query1);
    }
    catch (Exception e)
    {
      logger.info("Delete action exception " + e.getMessage());
      throwDAOException(e);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, ppt, rs);
    }
    return preShipVo;
  }
}