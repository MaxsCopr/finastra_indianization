package in.co.boe.dao.boe_checker;

import in.co.boe.dao.AbstractDAO;
import in.co.boe.utility.CommonMethods;
import in.co.boe.utility.LoggableStatement;
import in.co.boe.vo.BoeVO;
import in.co.boe.vo.TransactionVO;
import java.sql.Connection;
import java.sql.ResultSet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
 
public class BoeApproveDAO
  extends AbstractDAO
{
  private static Logger logger = Logger.getLogger(BoeCheckerDAO.class
    .getName());
  public String approveBOE(BoeVO boeVO)
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement loggableStatement = null;
    ResultSet rs = null;
    String result = "fail";
    String query = null;
    int count = 0;
    int autoKey = 0;
    int fx_event = 0;
    int seqNo = 0;
    CommonMethods commonMethods = null;
    BoeCheckerDAO dao = null;
    try
    {
      dao = new BoeCheckerDAO();
      commonMethods = new CommonMethods();
      con = getConnection();
      HttpSession session = ServletActionContext.getRequest().getSession();
      String loginedUserId = (String)session.getAttribute("loginedUserName");
      query = "UPDATE ETT_BOE_PAYMENT SET STATUS ='A',LASTUPDATE = SYSTIMESTAMP,UPDATED_BY=?,REMARKS=?  WHERE BOE_PAYMENT_BP_BOE_NO =? AND TO_CHAR(BOE_PAYMENT_BP_BOE_DT,'DD-MM-YYYY') = ? AND PORT_CODE = ? AND BOE_PAYMENT_BP_PAY_REF = ? AND BOE_PAYMENT_BP_PAY_PART_REF =?";

 
 
      loggableStatement = new LoggableStatement(con, query);
      loggableStatement.setString(1, loginedUserId);
      loggableStatement.setString(2, boeVO.getRemarks());
      loggableStatement.setString(3, boeVO.getBoeNo());
      loggableStatement.setString(4, boeVO.getBoeDate());
      loggableStatement.setString(5, boeVO.getPortCode());
      loggableStatement.setString(6, boeVO.getPaymentRefNo());
      loggableStatement.setString(7, boeVO.getPartPaymentSlNo());
      count = loggableStatement.executeUpdate();
      boeVO = dao.getPrintBOEData(boeVO.getBoeNo(), boeVO.getBoeDate(), boeVO.getPortCode(), 
        boeVO.getPaymentRefNo(), boeVO.getPartPaymentSlNo());
      if (count > 0)
      {
        String getAutoKey = "SELECT HIGHVAL FROM AUTOKEYS WHERE BASETAB LIKE '%EXTEVENTBOE%'";
        LoggableStatement pst1 = new LoggableStatement(con, getAutoKey);
        ResultSet rs1 = pst1.executeQuery();
        if (rs1.next()) {
          autoKey = rs1.getInt("HIGHVAL");
        }
        closeSqlRefferance(rs1, pst1);
        String getExField = "SELECT BEV.EXTFIELD as EXTFIELD FROM MASTER MAS,BASEEVENT BEV  WHERE MAS.KEY97=BEV.MASTER_KEY AND MAS.MASTER_REF=? AND (BEV.REFNO_PFIX||lPAD(BEV.REFNO_SERL,3,0))=?";

 
 
        LoggableStatement pst2 = new LoggableStatement(con, getExField);
        pst2.setString(1, boeVO.getPaymentRefNo());
        pst2.setString(2, boeVO.getPartPaymentSlNo());
        ResultSet rs2 = pst2.executeQuery();
        if (rs2.next()) {
          fx_event = rs2.getInt("EXTFIELD");
        }
        closeSqlRefferance(rs2, pst2);
        String getCount = "SELECT count(*) as Count FROM EXTEVENTBOE WHERE FK_EVENT =?";
        LoggableStatement pst3 = new LoggableStatement(con, getCount);
        pst3.setInt(1, fx_event);
        ResultSet rs3 = pst3.executeQuery();
        if (rs3.next())
        {
          int c = rs3.getInt("Count");
          if (c > 0)
          {
            String getSeqNo = "SELECT MAX(SEQN)+1 as SEQNO FROM EXTEVENTBOE WHERE FK_EVENT =?";
            LoggableStatement pst4 = new LoggableStatement(con, getSeqNo);
            pst4.setInt(1, fx_event);
            ResultSet rs4 = pst4.executeQuery();
            if (rs4.next()) {
              seqNo = rs4.getInt("SEQNO");
            }
            closeSqlRefferance(rs4, pst4);
          }
          else
          {
            seqNo = 0;
          }
        }
        closeSqlRefferance(rs3, pst3);
        TransactionVO transactionVO = dao.getBOEData(boeVO.getBoeNo(), boeVO.getBoeDate(), boeVO.getPortCode(), 
          boeVO.getPaymentRefNo(), boeVO.getPartPaymentSlNo());
        if (transactionVO != null)
        {
          String sqlQuery = "INSERT INTO EXTEVENTBOE(XKEY, SEQN,FK_EVENT,BOENUM,BOECUR,BOEDAT,BOTYP,BOENDA)  VALUES (?,?,?,?,?,TO_DATE(?,'dd/mm/yy'),?,?)";

 
 
          LoggableStatement pst5 = new LoggableStatement(con, sqlQuery);
          pst5.setInt(1, autoKey);
          pst5.setInt(2, seqNo);
          pst5.setInt(3, fx_event);
          pst5.setString(4, transactionVO.getBoeNo());
          pst5.setString(5, transactionVO.getBoeCurr());
          pst5.setString(6, transactionVO.getBoeDate());
          pst5.setString(7, transactionVO.getPortCode());
          double endAmt = Double.parseDouble(transactionVO.getBalEndorseAmt());
          pst5.setDouble(8, endAmt);
          int k = pst5.executeUpdate();
          closePreparedStatement(pst5);
          String getBOEChecking = "SELECT BOE_PAYMENT_BP_PAY_FC_AMT,SUM(BOE_PAYMENT_BP_PAY_EDS_FC_AMT) AS BOE_PAYMENT_BP_PAY_EDS_FC_AMT  FROM ETT_BOE_PAYMENT where BOE_PAYMENT_BP_PAY_REF = ? AND BOE_PAYMENT_BP_PAY_PART_REF =? GROUP BY BOE_PAYMENT_BP_PAY_FC_AMT";

 
          LoggableStatement pst6 = new LoggableStatement(con, getBOEChecking);
          pst6.setString(1, boeVO.getPaymentRefNo());
          pst6.setString(2, boeVO.getPartPaymentSlNo());
          ResultSet rs6 = pst6.executeQuery();
          if (rs6.next())
          {
            String temp_fc_amt = commonMethods.toDouble(rs6.getString("BOE_PAYMENT_BP_PAY_FC_AMT")).trim();
            String temp_eds_fc_amt = commonMethods.toDouble(rs6.getString("BOE_PAYMENT_BP_PAY_EDS_FC_AMT"))
              .trim();
            double fc_amt = Double.parseDouble(temp_fc_amt);
            double eds_fc_amt = Double.parseDouble(temp_eds_fc_amt);
            if (fc_amt == eds_fc_amt)
            {
              String upQuery = "UPDATE EXTEVENT SET BOSUM= 'Y' WHERE KEY29 =?";
              LoggableStatement pst7 = new LoggableStatement(con, upQuery);
              pst7.setInt(1, fx_event);
              int checkUpdate = pst7.executeUpdate();
              logger.info("Check Update--->" + checkUpdate);
              closePreparedStatement(pst7);
            }
          }
          closeSqlRefferance(rs6, pst6);
          if (k > 0)
          {
            autoKey++;
            String autoUpQuery = "UPDATE AUTOKEYS SET HIGHVAL =? WHERE BASETAB ='EXTEVENTBOE'";
            LoggableStatement pst8 = new LoggableStatement(con, autoUpQuery);
            pst8.setInt(1, autoKey);
            pst8.executeUpdate();
            closePreparedStatement(pst8);
          }
        }
      }
      if (count > 0) {
        result = "success";
      }
    }
    catch (Exception e)
    {
      logger.info("updateStatus---------------------------Exception-------------" + e);
      e.printStackTrace();
    }
    finally
    {
      closeSqlRefferance(rs, loggableStatement, con);
    }
    logger.info("Exiting Method");
    return result;
  }
}
