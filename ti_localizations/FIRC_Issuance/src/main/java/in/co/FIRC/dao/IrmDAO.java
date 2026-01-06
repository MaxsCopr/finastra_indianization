package in.co.FIRC.dao;

import in.co.FIRC.dao.exception.DAOException;
import in.co.FIRC.dao.utility.DBConnectionUtility;
import in.co.FIRC.utility.CommonMethods;
import in.co.FIRC.utility.DBUtility;
import in.co.FIRC.utility.LoggableStatement;
import in.co.FIRC.utility.UtilityGenerateCard;
import in.co.FIRC.vo.AlertMessageVO;
import in.co.FIRC.vo.IRMVO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;
 
public class IrmDAO
  extends DBConnectionUtility
{
  private static Logger logger = LogManager.getLogger(IrmDAO.class.getName());
  static IrmDAO dao;
  public static IrmDAO getDAO()
  {
    if (dao == null) {
      dao = new IrmDAO();
    }
    return dao;
  }
  private ArrayList<AlertMessageVO> alertMsgArray = new ArrayList();
  public ArrayList<AlertMessageVO> getAlertMsgArray()
  {
    return this.alertMsgArray;
  }
  public void setAlertMsgArray(ArrayList<AlertMessageVO> alertMsgArray)
  {
    this.alertMsgArray = alertMsgArray;
  }
  public IRMVO fetchIRMData(IRMVO irmExVO)
    throws DAOException
  {
    logger.info("------------fetchIRMData--------------");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    label624:
    try
    {
      con = DBConnectionUtility.getConnection();
      String transRefNo = DBUtility.getEmptyIfNull(irmExVO.getTransactionrefno()).trim();
      int irmCount = 0;
      String countQuery = "SELECT COUNT(*) AS COUNT  FROM ETT_IRM_EXT_TBL WHERE MASTER_REFNO = ? ORDER BY CREATE_DATE DESC ";
      LoggableStatement ps = new LoggableStatement(con, countQuery);
      ps.setString(1, transRefNo);
      logger.info("------------fetchIRMData---------query-----" + ps.getQueryString());
      ResultSet rst = ps.executeQuery();
      if (rst.next()) {
        irmCount = rst.getInt("COUNT");
      }
      closePreparedStmtResultSet(ps, rst);
      if (irmCount > 0)
      {
        String query = "SELECT S_NO,MASTER_REFNO,ADCODE,IECODE,EXT_IND, TO_CHAR(TO_DATE(EXT_DATE,'DD-MM-YY'),'DD-MM-YY') AS EXT_DATE,LETTER_NO, TO_CHAR(TO_DATE(LETTER_DATE,'DD-MM-YY'),'DD-MM-YY') AS LETTER_DATE,REC_IND, REASON,ACK_STATUS,REMARKS,STATUS  FROM ETT_IRM_EXT_TBL WHERE MASTER_REFNO = ? ORDER BY CREATE_DATE DESC ";

 
 
        pst = new LoggableStatement(con, query);
        pst.setString(1, transRefNo);
        logger.info("------------fetchIRMData---------query-11111111----" + pst.getQueryString());
        rs = pst.executeQuery();
        if (rs.next())
        {
          irmExVO = new IRMVO();
          irmExVO.setTransactionrefno(DBUtility.getEmptyIfNull(rs.getString("MASTER_REFNO")).trim());
          irmExVO.setAdcode(DBUtility.getEmptyIfNull(rs.getString("ADCODE")).trim());
          irmExVO.setIecode(DBUtility.getEmptyIfNull(rs.getString("IECODE")).trim());
          irmExVO.setExtensionInd(DBUtility.getEmptyIfNull(rs.getString("EXT_IND")).trim());
          irmExVO.setPreviousExtDate(DBUtility.getEmptyIfNull(rs.getString("EXT_DATE")).trim());
          irmExVO.setLetterNo(DBUtility.getEmptyIfNull(rs.getString("LETTER_NO")).trim());
          irmExVO.setLetterDate(DBUtility.getEmptyIfNull(rs.getString("LETTER_DATE")).trim());
          irmExVO.setRecordInd(DBUtility.getEmptyIfNull(rs.getString("REC_IND")).trim());
          irmExVO.setReason(DBUtility.getEmptyIfNull(rs.getString("REASON")).trim());
          irmExVO.setRemarks(DBUtility.getEmptyIfNull(rs.getString("REMARKS")).trim());
          irmExVO.setStatus(DBUtility.getEmptyIfNull(rs.getString("STATUS")).trim());
          irmExVO.setSerialNo(DBUtility.getEmptyIfNull(rs.getString("S_NO")).trim());
          break label624;
        }
      }
      else
      {
        String query = "SELECT TRXN_REFNO,ADCODE,IECODE,REC_IND  FROM ETT_IRM_XML_FILES_TBL WHERE TRXN_REFNO = ? ";
        pst = new LoggableStatement(con, query);
        pst.setString(1, transRefNo);
        logger.info("------------fetchIRMData---------query-----" + pst.getQueryString());
        rs = pst.executeQuery();
        if (rs.next())
        {
          irmExVO = new IRMVO();
          irmExVO.setTransactionrefno(DBUtility.getEmptyIfNull(rs.getString("TRXN_REFNO")).trim());
          irmExVO.setAdcode(DBUtility.getEmptyIfNull(rs.getString("ADCODE")).trim());
          irmExVO.setIecode(DBUtility.getEmptyIfNull(rs.getString("IECODE")).trim());
          irmExVO.setRecordInd(DBUtility.getEmptyIfNull(rs.getString("REC_IND")).trim());
        }
      }
    }
    catch (Exception exception)
    {
      logger.info("------------fetchIRMData-----exception---------" + exception);
      throwDAOException(exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    logger.info("Exiting Method");
    return irmExVO;
  }
  public IRMVO validateIRMExData(IRMVO irmExVO)
    throws DAOException
  {
    logger.info("----------------validateIRMExData------------");
    CommonMethods commonMethods = null;
    try
    {
      commonMethods = new CommonMethods();
      if ((this.alertMsgArray != null) && 
        (this.alertMsgArray.size() > 0)) {
        this.alertMsgArray.clear();
      }
      String transRefNo = DBUtility.getEmptyIfNull(irmExVO.getTransactionrefno()).trim();
      String recInd = DBUtility.getEmptyIfNull(irmExVO.getRecordInd()).trim();
      int irmAckCount = irmAckStatus(transRefNo);
      if (irmAckCount > 0)
      {
        if (CommonMethods.isNull(recInd))
        {
          String errormsg = UtilityGenerateCard.getErrorDesc("IRM002", "N127");
          Object[] arg = { Integer.valueOf(0), "E", errormsg, "Input" };
          setErrorvalues(arg);
        }
        if (CommonMethods.isNull(irmExVO.getExtensionInd()))
        {
          String errormsg = UtilityGenerateCard.getErrorDesc("IRM003", "N127");
          Object[] arg = { Integer.valueOf(0), "E", errormsg, "Input" };
          setErrorvalues(arg);
        }
        if (CommonMethods.isNull(irmExVO.getExtensionDate()))
        {
          String errormsg = UtilityGenerateCard.getErrorDesc("IRM004", "N127");
          Object[] arg = { Integer.valueOf(0), "E", errormsg, "Input" };
          setErrorvalues(arg);
        }
        if (irmExVO.getExtensionInd().equalsIgnoreCase("1"))
        {
          if (CommonMethods.isNull(irmExVO.getLetterNo()))
          {
            String errormsg = UtilityGenerateCard.getErrorDesc("IRM005", "N127");
            Object[] arg = { Integer.valueOf(0), "E", errormsg, "Input" };
            setErrorvalues(arg);
          }
          if (CommonMethods.isNull(irmExVO.getLetterDate()))
          {
            String errormsg = UtilityGenerateCard.getErrorDesc("IRM006", "N127");
            Object[] arg = { Integer.valueOf(0), "E", errormsg, "Input" };
            setErrorvalues(arg);
          }
        }
        if (CommonMethods.isNull(irmExVO.getAdcode()))
        {
          String errormsg = UtilityGenerateCard.getErrorDesc("IRM008", "N127");
          Object[] arg = { Integer.valueOf(0), "E", errormsg, "Input" };
          setErrorvalues(arg);
        }
        if (CommonMethods.isNull(irmExVO.getIecode()))
        {
          String errormsg = UtilityGenerateCard.getErrorDesc("IRM009", "N127");
          Object[] arg = { Integer.valueOf(0), "E", errormsg, "Input" };
          setErrorvalues(arg);
        }
        if (!CommonMethods.isNull(irmExVO.getLetterDate()))
        {
          int irmRemLetDate = irmRemLetDate(transRefNo, irmExVO.getLetterDate());
          if (irmRemLetDate == 0)
          {
            String errormsg = UtilityGenerateCard.getErrorDesc("IRM017", "N127");
            Object[] arg = { Integer.valueOf(0), "E", errormsg, "Input" };
            setErrorvalues(arg);
          }
        }
        if (!CommonMethods.isNull(irmExVO.getExtensionDate()))
        {
          int irmRemExtDate = irmRemExtDate(transRefNo, irmExVO.getExtensionDate());
          if (irmRemExtDate == 0)
          {
            String errormsg = UtilityGenerateCard.getErrorDesc("IRM018", "N127");
            Object[] arg = { Integer.valueOf(0), "E", errormsg, "Input" };
            setErrorvalues(arg);
          }
        }
        int irmExCount = irmExtPendingCount(transRefNo);
        if (irmExCount > 0)
        {
          String errormsg = UtilityGenerateCard.getErrorDesc("IRM021", "N127");
          Object[] arg = { Integer.valueOf(0), "E", errormsg, "Input" };
          setErrorvalues(arg);
        }
        int irmStatus = irmExtCount(transRefNo);
        if (irmStatus > 0)
        {
          String errormsg = UtilityGenerateCard.getErrorDesc("IRM022", "N127");
          Object[] arg = { Integer.valueOf(0), "E", errormsg, "Input" };
          setErrorvalues(arg);
        }
      }
      else
      {
        String errormsg = UtilityGenerateCard.getErrorDesc("IRM001", "N127");
        Object[] arg = { Integer.valueOf(0), "E", errormsg, "Input" };
        setErrorvalues(arg);
      }
      if (this.alertMsgArray.size() > 0) {
        irmExVO.setErrorList(this.alertMsgArray);
      }
    }
    catch (Exception exception)
    {
      logger.info("----------------validateIRMExData--exception----------" + exception);
      throwDAOException(exception);
    }
    logger.info("Exiting Method");
    return irmExVO;
  }
  public String storeIRMExData(IRMVO irmExVO)
    throws DAOException
  {
    logger.info("------------------storeIRMExData---------------");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    String result = null;
    String loginedUserId = null;
    int i = 0;
    try
    {
      con = DBConnectionUtility.getConnection();
      HttpSession session = ServletActionContext.getRequest().getSession();
      loginedUserId = (String)session.getAttribute("loginedUserName");
      int irmExtCount = 0;
      String query = "SELECT COUNT(*) AS EXT_COUNT FROM ETT_IRM_EXT_TBL WHERE MASTER_REFNO = ?  AND STATUS != 'A' ";

 
      LoggableStatement pst1 = new LoggableStatement(con, query);
      pst1.setString(1, irmExVO.getTransactionrefno());
      logger.info("------------------storeIRMExData------count query--------" + pst1.getQueryString());
      ResultSet rs1 = pst1.executeQuery();
      if (rs1.next()) {
        irmExtCount = rs1.getInt("EXT_COUNT");
      }
      closePreparedStmtResultSet(pst1, rs1);
      if (irmExtCount > 0)
      {
        String irmExtSeqNo = null;
        String boeExXkeyQuery = "SELECT S_NO AS EXTENSION_SNO FROM ETT_IRM_EXT_TBL WHERE MASTER_REFNO = ?  AND STATUS != 'A' ";
        LoggableStatement ps1 = new LoggableStatement(con, boeExXkeyQuery);
        ps1.setString(1, irmExVO.getTransactionrefno());
        logger.info("------------------storeIRMExData---EXTENSION_SNO ---count 11111111111-------" + pst1.getQueryString());
        ResultSet rst1 = ps1.executeQuery();
        if (rst1.next()) {
          irmExtSeqNo = rst1.getString("EXTENSION_SNO");
        }
        closePreparedStmtResultSet(ps1, rst1);

 
        String updateQuery = "UPDATE ETT_IRM_EXT_TBL SET EXT_IND = ?,EXT_DATE = TO_DATE(?,'DD-MM-YY'),LETTER_NO = ?,LETTER_DATE = TO_DATE(?,'DD-MM-YY'),REC_IND = ?,REASON = ?,STATUS = ?,MAKER_USERID = ?,UPDATE_DATE = SYSTIMESTAMP WHERE MASTER_REFNO = ? AND S_NO = ? ";

 
        pst = new LoggableStatement(con, updateQuery);
        pst.setString(1, DBUtility.getEmptyIfNull(irmExVO.getExtensionInd()).trim());
        pst.setString(2, DBUtility.getEmptyIfNull(irmExVO.getExtensionDate()).trim());
        pst.setString(3, DBUtility.getEmptyIfNull(irmExVO.getLetterNo()).trim());
        pst.setString(4, DBUtility.getEmptyIfNull(irmExVO.getLetterDate()).trim());
        pst.setString(5, DBUtility.getEmptyIfNull(irmExVO.getRecordInd()).trim());
        pst.setString(6, DBUtility.getEmptyIfNull(irmExVO.getReason()).trim());
        pst.setString(7, "TP");
        pst.setString(8, loginedUserId);
        pst.setString(9, DBUtility.getEmptyIfNull(irmExVO.getTransactionrefno()).trim());
        pst.setString(10, DBUtility.getEmptyIfNull(irmExtSeqNo).trim());
        logger.info("Update IRM Ex:------------ " + pst.getQueryString());
        i = pst.executeUpdate();
        logger.info("Update IRM Ex:-----------count- " + i);
        closePreparedStatement(pst);
      }
      else
      {
        String insertQuery = "INSERT INTO ETT_IRM_EXT_TBL (S_NO,MASTER_REFNO,ADCODE,IECODE,EXT_IND,EXT_DATE,LETTER_NO,LETTER_DATE,REC_IND,REASON,PREVEXTDATE,STATUS,MAKER_USERID) VALUES (IRM_EXT_SEQ.NEXTVAL,?,?,?,?,TO_DATE(?,'DD-MM-YY'),?,TO_DATE(?,'DD-MM-YY'),?,?,TO_DATE(?,'DD-MM-YY'),?,?)";

 
        pst = new LoggableStatement(con, insertQuery);
        pst.setString(1, DBUtility.getEmptyIfNull(irmExVO.getTransactionrefno()).trim());
        pst.setString(2, DBUtility.getEmptyIfNull(irmExVO.getAdcode()).trim());
        pst.setString(3, DBUtility.getEmptyIfNull(irmExVO.getIecode()).trim());
        pst.setString(4, DBUtility.getEmptyIfNull(irmExVO.getExtensionInd()).trim());
        pst.setString(5, DBUtility.getEmptyIfNull(irmExVO.getExtensionDate()).trim());
        pst.setString(6, DBUtility.getEmptyIfNull(irmExVO.getLetterNo()).trim());
        pst.setString(7, DBUtility.getEmptyIfNull(irmExVO.getLetterDate()).trim());
        pst.setString(8, DBUtility.getEmptyIfNull(irmExVO.getRecordInd()).trim());
        pst.setString(9, DBUtility.getEmptyIfNull(irmExVO.getReason()).trim());
        pst.setString(10, DBUtility.getEmptyIfNull(irmExVO.getPreviousExtDate()).trim());
        pst.setString(11, "TP");
        pst.setString(12, loginedUserId);
        logger.info("Insert ETT_IRM_EXT_TBL------------------ IRM Ex: " + pst.getQueryString());
        i = pst.executeUpdate();
        logger.info("------------------storeIRMExData------count------" + i);
      }
      if (i > 0) {
        result = "success";
      }
    }
    catch (Exception exception)
    {
      logger.info("------------------storeIRMExData-------exception--------" + exception);
      throwDAOException(exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    logger.info("Exiting Method");
    return result;
  }
  public void setErrorvalues(Object[] arg)
  {
    AlertMessageVO altMsg = new AlertMessageVO();
    CommonMethods commonMethods = new CommonMethods();
    altMsg.setErrorId(commonMethods.getEmptyIfNull(arg[1]).equalsIgnoreCase("W") ? "Warning" : "Error");
    altMsg.setErrorDesc("General");
    altMsg.setErrorCode(commonMethods.getEmptyIfNull(arg[3]));
    altMsg.setErrorDetails(commonMethods.getEmptyIfNull(arg[2]));
    altMsg.setErrorMsg(commonMethods.getEmptyIfNull(arg[1]).equalsIgnoreCase("W") ? "N" : "");
    this.alertMsgArray.add(altMsg);
  }
  public int irmAckStatus(String transRefNo)
    throws DAOException
  {
    logger.info("-------------------irmAckStatus--------------");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    int count = 0;
    try
    {
      con = DBConnectionUtility.getConnection();
      String countQuery = "SELECT COUNT(*) AS LIST_COUNT  FROM ETT_IRM_XML_FILES_TBL WHERE UPPER(ACK_STATUS) = 'SUCCESS' AND TRXN_REFNO = ? ";
      pst = new LoggableStatement(con, countQuery);
      pst.setString(1, transRefNo);
      logger.info("logger.info(\"-------------------irmAckStatus----------query----" + pst.getQueryString());
      rs = pst.executeQuery();
      if (rs.next())
      {
        count = rs.getInt("LIST_COUNT");
        logger.info("logger.info(\"-------------------irmAckStatus----count----" + count);
      }
    }
    catch (Exception e)
    {
      logger.info("-------------------irmAckStatus-----------exception---" + e);
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    logger.info("Exiting Method");
    return count;
  }
  public int irmExtCount(String transRefNo)
    throws DAOException
  {
    logger.info("------------irmExtCount------------");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    int count = 0;
    try
    {
      con = DBConnectionUtility.getConnection();
      String countQuery = "SELECT COUNT(*) AS EXT_COUNT FROM ETT_IRM_EXT_TBL WHERE MASTER_REFNO = ?  AND STATUS = 'A'  AND EXTSTATUS = 'Pending' ";

 
      pst = new LoggableStatement(con, countQuery);
      logger.info("------------irmExtCount-------exception--query---" + pst.getQueryString());
      pst.setString(1, transRefNo);
      rs = pst.executeQuery();
      if (rs.next()) {
        count = rs.getInt("EXT_COUNT");
      }
    }
    catch (Exception e)
    {
      logger.info("------------irmExtCount-------exception-----" + e);
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    logger.info("Exiting Method");
    return count;
  }
  public int irmExtPendingCount(String transRefNo)
    throws DAOException
  {
    logger.info("--------------irmExtPendingCount--------------");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    int count = 0;
    try
    {
      con = DBConnectionUtility.getConnection();
      String countQuery = "SELECT COUNT(*) AS LIST_COUNT  FROM ETT_IRM_EXT_TBL WHERE STATUS = 'TP' AND MASTER_REFNO = ? ";
      pst = new LoggableStatement(con, countQuery);
      pst.setString(1, transRefNo);
      logger.info("--------------irmExtPendingCount---------query-----" + pst.getQueryString());
      rs = pst.executeQuery();
      if (rs.next()) {
        count = rs.getInt("LIST_COUNT");
      }
    }
    catch (Exception e)
    {
      logger.info("--------------irmExtPendingCount---------exception-----" + e);
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    logger.info("Exiting Method");
    return count;
  }
  public int irmRemLetDate(String transRefNo, String letterDate)
    throws DAOException
  {
    logger.info("Entering Method");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    int count = 0;
    try
    {
      con = DBConnectionUtility.getConnection();
      String countQuery = "SELECT COUNT(*) AS LIST_COUNT  FROM ETT_IRM_XML_FILES_TBL WHERE UPPER(ACK_STATUS) = 'SUCCESS' AND TRXN_REFNO = ?  AND REM_DATE < TO_DATE(?,'DD-MM-YY') ";

 
      pst = new LoggableStatement(con, countQuery);
      pst.setString(1, transRefNo);
      pst.setString(2, letterDate);
      rs = pst.executeQuery();
      if (rs.next()) {
        count = rs.getInt("LIST_COUNT");
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
    return count;
  }
  public int irmRemExtDate(String transRefNo, String ExtDate)
    throws DAOException
  {
    logger.info("Entering Method");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    int count = 0;
    try
    {
      con = DBConnectionUtility.getConnection();
      String countQuery = "SELECT COUNT(*) AS LIST_COUNT  FROM ETT_IRM_XML_FILES_TBL WHERE UPPER(ACK_STATUS) = 'SUCCESS' AND TRXN_REFNO = ?  AND REM_DATE < TO_DATE(?,'DD-MM-YY') ";

 
      pst = new LoggableStatement(con, countQuery);
      pst.setString(1, transRefNo);
      pst.setString(2, ExtDate);
      rs = pst.executeQuery();
      if (rs.next()) {
        count = rs.getInt("LIST_COUNT");
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
    return count;
  }
}