package in.co.FIRC.dao;

import in.co.FIRC.dao.exception.DAOException;
import in.co.FIRC.dao.utility.DBConnectionUtility;
import in.co.FIRC.utility.CommonMethods;
import in.co.FIRC.utility.DBUtility;
import in.co.FIRC.utility.LoggableStatement;
import in.co.FIRC.vo.IRMClosureVO;
import in.co.FIRC.vo.IRMVO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;
 
public class IRMCheckerDAO
  extends DBConnectionUtility
{
  private static Logger logger = LogManager.getLogger(IRMCheckerDAO.class.getName());
  static IRMCheckerDAO dao;
  public static IRMCheckerDAO getDAO()
  {
    if (dao == null) {
      dao = new IRMCheckerDAO();
    }
    return dao;
  }
  public String getExtensionList(IRMVO irmvo)
  {
    CommonMethods commonMethods = null;
    String query = "SELECT S_NO,MASTER_REFNO,ADCODE,IECODE,EXT_IND,TO_CHAR(TO_DATE(EXT_DATE,'DD-MM-YY'),'DD-MM-YY') AS EXT_DATE,LETTER_NO,TO_CHAR(TO_DATE(LETTER_DATE,'DD-MM-YY'),'DD-MM-YY') AS LETTER_DATE,REC_IND,REASON,ACK_STATUS,REMARKS,MAKER_USERID  FROM ETT_IRM_EXT_TBL WHERE STATUS IN ('TP') ";

 
 
    commonMethods = new CommonMethods();
    String transRefNo = DBUtility.getEmptyIfNull(irmvo.getTransactionrefno()).trim();
    if (!CommonMethods.isNull(transRefNo)) {
      query = query + " AND MASTER_REFNO ='" + transRefNo + "'";
    }
    return query;
  }
  public ArrayList<IRMVO> fetchIRMExtensionData(IRMVO irmExVO)
    throws DAOException
  {
    logger.info("-----------------fetchIRMExtensionData---------------------");
    Connection con = null;
    LoggableStatement pst = null;
    ResultSet rs = null;
    CommonMethods commonMethods = new CommonMethods();
    ArrayList<IRMVO> extensionList = null;
    try
    {
      HttpSession session = ServletActionContext.getRequest().getSession();
      String loginedUserId = (String)session.getAttribute("loginedUserName");
      con = DBConnectionUtility.getConnection();
      extensionList = new ArrayList();

 
 
 
      String query = "SELECT S_NO,MASTER_REFNO,ADCODE,IECODE,EXT_IND,TO_CHAR(TO_DATE(EXT_DATE,'DD-MM-YY'),'DD-MM-YY') AS EXT_DATE,LETTER_NO,TO_CHAR(TO_DATE(LETTER_DATE,'DD-MM-YY'),'DD-MM-YY') AS LETTER_DATE,REC_IND,REASON,ACK_STATUS,REMARKS,MAKER_USERID  FROM ETT_IRM_EXT_TBL WHERE STATUS IN ('TP') ";

 
 
      String transRefNo = DBUtility.getEmptyIfNull(irmExVO.getTransactionrefno()).trim();
      if (!CommonMethods.isNull(transRefNo))
      {
        query = query + " AND MASTER_REFNO =?";
        pst = new LoggableStatement(con, query);
        pst.setString(1, transRefNo);
      }
      else
      {
        pst = new LoggableStatement(con, query);
      }
      logger.info("------------------fetchIRMExtensionData-------------------" + pst.getQueryString());
      rs = pst.executeQuery();
      while (rs.next())
      {
        irmExVO = new IRMVO();
        irmExVO.setTransactionrefno(DBUtility.getEmptyIfNull(rs.getString("MASTER_REFNO")).trim());
        irmExVO.setAdcode(DBUtility.getEmptyIfNull(rs.getString("ADCODE")).trim());
        irmExVO.setIecode(DBUtility.getEmptyIfNull(rs.getString("IECODE")).trim());
        String extInd = DBUtility.getEmptyIfNull(rs.getString("EXT_IND")).trim();
        String tempExtInd = null;
        if ((extInd != null) && (extInd.equalsIgnoreCase("1"))) {
          tempExtInd = "RBI";
        } else if ((extInd != null) && (extInd.equalsIgnoreCase("2"))) {
          tempExtInd = "AD Bank";
        } else {
          tempExtInd = "Other";
        }
        irmExVO.setExtensionInd(tempExtInd);
        irmExVO.setExtensionDate(DBUtility.getEmptyIfNull(rs.getString("EXT_DATE")).trim());
        irmExVO.setLetterNo(DBUtility.getEmptyIfNull(rs.getString("LETTER_NO")).trim());
        irmExVO.setLetterDate(DBUtility.getEmptyIfNull(rs.getString("LETTER_DATE")).trim());
        String recInd = DBUtility.getEmptyIfNull(rs.getString("REC_IND")).trim();
        String tempRecInd = null;
        if ((recInd != null) && (recInd.equalsIgnoreCase("1"))) {
          tempRecInd = "New";
        } else {
          tempRecInd = "Cancelled";
        }
        irmExVO.setRecordInd(tempRecInd);
        irmExVO.setReason(DBUtility.getEmptyIfNull(rs.getString("REASON")).trim());
        irmExVO.setRemarks(DBUtility.getEmptyIfNull(rs.getString("REMARKS")).trim());
        irmExVO.setAckStatus(DBUtility.getEmptyIfNull(rs.getString("ACK_STATUS")).trim());
        irmExVO.setTempTransRefNo(DBUtility.getEmptyIfNull(rs.getString("S_NO")).trim() + 
          ":" + DBUtility.getEmptyIfNull(rs.getString("MASTER_REFNO")).trim());
        String makerUserId = DBUtility.getEmptyIfNull(rs.getString("MAKER_USERID")).trim();
        if ((loginedUserId != null) && (loginedUserId.equalsIgnoreCase(makerUserId))) {
          irmExVO.setCheckBoxDisabled("true");
        } else {
          irmExVO.setCheckBoxDisabled("false");
        }
        extensionList.add(irmExVO);
      }
    }
    catch (Exception e)
    {
      logger.info("-----------------fetchIRMExtensionData-------------exception--------" + e);
      throwDAOException(e);
    }
    finally
    {
      closeSqlRefferance(rs, pst, con);
    }
    logger.info("Exiting Method");
    return extensionList;
  }
  public String updateIRMExtensionStatus(String[] chkList, String check, String remarks)
    throws DAOException
  {
    logger.info("----------------updateIRMExtensionStatus-------------------");
    Connection con = null;
    LoggableStatement pst = null;
    ResultSet rs = null;
    String result = "fail";
    String query = null;
    int count = 0;
    String loginedUserId = null;
    try
    {
      con = DBConnectionUtility.getConnection();
      HttpSession session = ServletActionContext.getRequest().getSession();
      loginedUserId = (String)session.getAttribute("loginedUserName");
      if ((chkList != null) && (check != null)) {
        for (int i = 0; i < chkList.length; i++)
        {
          String temp = chkList[i];
          String[] a = temp.split(":");
          String serialNo = a[0];
          String transRefNo = a[1];
          if (check.equalsIgnoreCase("approve"))
          {
            query = "UPDATE ETT_IRM_EXT_TBL SET STATUS ='A',EXTSTATUS = 'Pending',UPDATE_DATE = SYSTIMESTAMP,CHECKER_USERID=?,REMARKS=?  WHERE S_NO = ? AND MASTER_REFNO =?";
            pst = new LoggableStatement(con, query);
            pst.setString(1, loginedUserId);
            pst.setString(2, remarks);
            pst.setString(3, serialNo);
            pst.setString(4, transRefNo);
            count = pst.executeUpdate();
          }
          else if (check.equalsIgnoreCase("reject"))
          {
            query = "UPDATE ETT_IRM_EXT_TBL SET STATUS ='RN',UPDATE_DATE = SYSTIMESTAMP,CHECKER_USERID=?,REMARKS=? WHERE S_NO = ? AND MASTER_REFNO =?";
            pst = new LoggableStatement(con, query);
            pst.setString(1, loginedUserId);
            pst.setString(2, remarks);
            pst.setString(3, serialNo);
            pst.setString(4, transRefNo);
            count = pst.executeUpdate();
          }
        }
      }
      logger.info("updateStatus: " + pst.getQueryString());
      if (count > 0) {
        result = "success";
      }
      con.commit();
    }
    catch (Exception e)
    {
      logger.info("----------------updateIRMExtensionStatus-----------exception--------" + e);
      throwDAOException(e);
    }
    finally
    {
      closeSqlRefferance(rs, pst, con);
    }
    logger.info("Exiting Method");
    return result;
  }
  public String getClosureList(IRMClosureVO irmAdjVO)
  {
    CommonMethods commonMethods = null;
    String query = "SELECT S_NO,MASTER_REFNO,ADCODE,IECODE,CURRENCY,AMOUNT,APPROVED_BY,LETTER_NO,TO_CHAR(TO_DATE(ADJ_DATE,'DD-MM-YY'),'DD-MM-YY') AS ADJ_DATE,REASON,DOC_NO,TO_CHAR(TO_DATE(DOC_DATE,'DD-MM-YY'),'DD-MM-YY') AS DOC_DATE,DOC_PORT,REC_IND,CLOSE_IND,MAKER_USERID,REMARKS,STATUS FROM ETT_IRM_CLOSURE_TBL WHERE STATUS IN ('TP','TC') ";

 
 
    commonMethods = new CommonMethods();
    String transRefNo = DBUtility.getEmptyIfNull(irmAdjVO.getTransactionrefno()).trim();
    if (!CommonMethods.isNull(transRefNo)) {
      query = query + " AND MASTER_REFNO ='" + transRefNo + "'";
    }
    return query;
  }
  public ArrayList<IRMClosureVO> fetchIrmClosureData(IRMClosureVO irmAdjVO)
    throws DAOException
  {
    logger.info("-----------------fetchIrmClosureData----------------");
    Connection con = null;
    LoggableStatement pst = null;
    ResultSet rs = null;
    CommonMethods commonMethods = null;
    ArrayList<IRMClosureVO> closureList = null;
    try
    {
      HttpSession session = ServletActionContext.getRequest().getSession();
      String loginedUserId = (String)session.getAttribute("loginedUserName");
      con = DBConnectionUtility.getConnection();
      closureList = new ArrayList();

 
 
      String query = "SELECT S_NO,MASTER_REFNO,ADCODE,IECODE,CURRENCY,AMOUNT,APPROVED_BY,LETTER_NO,TO_CHAR(TO_DATE(ADJ_DATE,'DD-MM-YY'),'DD-MM-YY') AS ADJ_DATE,REASON,DOC_NO,TO_CHAR(TO_DATE(DOC_DATE,'DD-MM-YY'),'DD-MM-YY') AS DOC_DATE,DOC_PORT,REC_IND,CLOSE_IND,MAKER_USERID,REMARKS,STATUS FROM ETT_IRM_CLOSURE_TBL WHERE STATUS IN ('TP','TC') ";

 
 
      commonMethods = new CommonMethods();
      String transRefNo = DBUtility.getEmptyIfNull(irmAdjVO.getTransactionrefno()).trim();
      if (!CommonMethods.isNull(transRefNo))
      {
        query = query + " AND MASTER_REFNO =?";
        pst = new LoggableStatement(con, query);
        pst.setString(1, transRefNo);
      }
      else
      {
        pst = new LoggableStatement(con, query);
      }
      logger.info("-----------------fetchIrmClosureData---------query-------" + pst.getQueryString());
      rs = pst.executeQuery();
      while (rs.next())
      {
        irmAdjVO = new IRMClosureVO();
        irmAdjVO.setTransactionrefno(DBUtility.getEmptyIfNull(rs.getString("MASTER_REFNO")).trim());
        irmAdjVO.setAdcode(DBUtility.getEmptyIfNull(rs.getString("ADCODE")).trim());
        irmAdjVO.setIecode(DBUtility.getEmptyIfNull(rs.getString("IECODE")).trim());
        irmAdjVO.setCurrency(DBUtility.getEmptyIfNull(rs.getString("CURRENCY")).trim());
        irmAdjVO.setAmount(DBUtility.getEmptyIfNull(rs.getString("AMOUNT")).trim());
        String extInd = DBUtility.getEmptyIfNull(rs.getString("APPROVED_BY")).trim();
        String tempExtInd = null;
        if ((extInd != null) && (extInd.equalsIgnoreCase("1"))) {
          tempExtInd = "RBI";
        } else if ((extInd != null) && (extInd.equalsIgnoreCase("2"))) {
          tempExtInd = "AD Bank";
        } else {
          tempExtInd = "Other";
        }
        irmAdjVO.setApprovedBy(tempExtInd);
        irmAdjVO.setLetterNo(DBUtility.getEmptyIfNull(rs.getString("LETTER_NO")).trim());
        irmAdjVO.setAdjDate(DBUtility.getEmptyIfNull(rs.getString("ADJ_DATE")).trim());
        irmAdjVO.setReason(DBUtility.getEmptyIfNull(rs.getString("REASON")).trim());
        irmAdjVO.setDocNo(DBUtility.getEmptyIfNull(rs.getString("DOC_NO")).trim());
        irmAdjVO.setDocDate(DBUtility.getEmptyIfNull(rs.getString("DOC_DATE")).trim());
        irmAdjVO.setDocPort(DBUtility.getEmptyIfNull(rs.getString("DOC_PORT")).trim());
        String recInd = DBUtility.getEmptyIfNull(rs.getString("REC_IND")).trim();
        String tempRecInd = null;
        if ((recInd != null) && (recInd.equalsIgnoreCase("1"))) {
          tempRecInd = "New";
        } else {
          tempRecInd = "Cancelled";
        }
        irmAdjVO.setRecordInd(tempRecInd);
        irmAdjVO.setRemarks(DBUtility.getEmptyIfNull(rs.getString("REMARKS")).trim());
        irmAdjVO.setStatus(DBUtility.getEmptyIfNull(rs.getString("STATUS")).trim());
        irmAdjVO.setTempTransRefNo(DBUtility.getEmptyIfNull(rs.getString("S_NO")).trim() + 
          ":" + DBUtility.getEmptyIfNull(rs.getString("MASTER_REFNO")).trim() + 
          ":" + recInd);
        String makerUserId = DBUtility.getEmptyIfNull(rs.getString("MAKER_USERID")).trim();
        if ((loginedUserId != null) && (loginedUserId.equalsIgnoreCase(makerUserId))) {
          irmAdjVO.setCheckBoxDisabled("true");
        } else {
          irmAdjVO.setCheckBoxDisabled("false");
        }
        closureList.add(irmAdjVO);
      }
    }
    catch (Exception e)
    {
      logger.info("-----------------fetchIrmClosureData---------exception-------" + e);
      throwDAOException(e);
    }
    finally
    {
      closeSqlRefferance(rs, pst, con);
    }
    logger.info("Exiting Method");
    return closureList;
  }
  public String updateClosureCheckerStatus(String[] chkList, String check, String remarks)
    throws DAOException
  {
    logger.info("----------------------------updateClosureCheckerStatus----------------");
    Connection con = null;
    LoggableStatement pst = null;
    ResultSet rs = null;
    String result = "fail";
    String query = null;
    int count = 0;
    String loginedUserId = null;
    try
    {
      con = DBConnectionUtility.getConnection();
      HttpSession session = ServletActionContext.getRequest().getSession();
      loginedUserId = (String)session.getAttribute("loginedUserName");
      if ((chkList != null) && (check != null)) {
        for (int i = 0; i < chkList.length; i++)
        {
          String temp = chkList[i];
          String[] a = temp.split(":");
          String serialNo = a[0];
          String transRefNo = a[1];
          String recInd = a[2];
          if (check.equalsIgnoreCase("approve"))
          {
            if (recInd.equalsIgnoreCase("1"))
            {
              query = "UPDATE ETT_IRM_CLOSURE_TBL SET STATUS ='A',ACK_STATUS = '',CLOSURE_STATUS = 'Pending',UPDATE_DATE = SYSTIMESTAMP,CHECKER_USERID=?,CHECKER_REMARKS=?  WHERE STATUS != 'C' AND S_NO =? AND MASTER_REFNO = ?";
              pst = new LoggableStatement(con, query);
              pst.setString(1, loginedUserId);
              pst.setString(2, remarks);
              pst.setString(3, serialNo);
              pst.setString(4, transRefNo);
              logger.info("----------------------------updateClosureCheckerStatus--ETT_IRM_CLOSURE_TBL ------query--------" + pst.getQueryString());
              count = pst.executeUpdate();
              logger.info("----------------------------updateClosureCheckerStatus--ETT_IRM_CLOSURE_TBL -----count--------" + count);
            }
            else
            {
              query = "UPDATE ETT_IRM_CLOSURE_TBL SET STATUS ='C',ACK_STATUS = '',CLOSURE_STATUS = 'Pending',UPDATE_DATE = SYSTIMESTAMP,CHECKER_USERID=?,CHECKER_REMARKS=? WHERE STATUS != 'C' AND S_NO =? AND MASTER_REFNO = ?";

 
              pst = new LoggableStatement(con, query);
              pst.setString(1, loginedUserId);
              pst.setString(2, remarks);
              pst.setString(3, serialNo);
              pst.setString(4, transRefNo);
              logger.info("----------------------------updateClosureCheckerStatus---ETT_IRM_CLOSURE_TBL-----query---44444444444444-----" + pst.getQueryString());
              count = pst.executeUpdate();
              logger.info("----------------------------updateClosureCheckerStatus--ETT_IRM_CLOSURE_TBL -----count--------" + count);
            }
          }
          else if (check.equalsIgnoreCase("reject")) {
            if (recInd.equalsIgnoreCase("1"))
            {
              query = "UPDATE ETT_IRM_CLOSURE_TBL SET STATUS ='RN',UPDATE_DATE = SYSTIMESTAMP,CHECKER_USERID=?,CHECKER_REMARKS=? WHERE STATUS != 'C' AND S_NO = ? AND MASTER_REFNO =?";
              pst = new LoggableStatement(con, query);
              pst.setString(1, loginedUserId);
              pst.setString(2, remarks);
              pst.setString(3, serialNo);
              pst.setString(4, transRefNo);
              logger.info("----------------------------updateClosureCheckerStatus---ETT_IRM_CLOSURE_TBL-----query------111112222222222--" + pst.getQueryString());
              count = pst.executeUpdate();
              logger.info("----------------------------updateClosureCheckerStatus--ETT_IRM_CLOSURE_TBL -----count--------" + count);
            }
            else
            {
              query = "UPDATE ETT_IRM_CLOSURE_TBL SET STATUS ='RC',UPDATE_DATE = SYSTIMESTAMP,CHECKER_USERID=?,CHECKER_REMARKS=? WHERE STATUS != 'C' AND S_NO = ? AND MASTER_REFNO =?";
              pst = new LoggableStatement(con, query);
              pst.setString(1, loginedUserId);
              pst.setString(2, remarks);
              pst.setString(3, serialNo);
              pst.setString(4, transRefNo);
              logger.info("----------------------------updateClosureCheckerStatus---ETT_IRM_CLOSURE_TBL-----query------1111126666666--" + pst.getQueryString());
              count = pst.executeUpdate();
              logger.info("----------------------------updateClosureCheckerStatus--ETT_IRM_CLOSURE_TBL -----count--------" + count);
            }
          }
        }
      }
      logger.info("updateStatus: " + pst.getQueryString());
      if (count > 0) {
        result = "success";
      }
      con.commit();
    }
    catch (Exception e)
    {
      logger.info("----------------------------updateClosureCheckerStatus---------exception-------" + e);
      throwDAOException(e);
    }
    finally
    {
      closeSqlRefferance(rs, pst, con);
    }
    logger.info("Exiting Method");
    return result;
  }
}
