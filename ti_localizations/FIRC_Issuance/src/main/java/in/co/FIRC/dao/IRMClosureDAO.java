package in.co.FIRC.dao;

import in.co.FIRC.dao.exception.DAOException;
import in.co.FIRC.dao.utility.DBConnectionUtility;
import in.co.FIRC.utility.CommonMethods;
import in.co.FIRC.utility.DBUtility;
import in.co.FIRC.utility.LoggableStatement;
import in.co.FIRC.utility.UtilityGenerateCard;
import in.co.FIRC.vo.AlertMessageVO;
import in.co.FIRC.vo.IRMClosureVO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;
 
public class IRMClosureDAO
  extends DBConnectionUtility
{
  private static Logger logger = LogManager.getLogger(IRMClosureDAO.class.getName());
  static IRMClosureDAO dao;
  public static IRMClosureDAO getDAO()
  {
    if (dao == null) {
      dao = new IRMClosureDAO();
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
  public IRMClosureVO fetchIRMClosureData(IRMClosureVO irmAdjVO)
    throws DAOException
  {
    logger.info("--------------fetchIRMClosureData-------------");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    label800:
    try
    {
      con = DBConnectionUtility.getConnection();
      String transRefNo = DBUtility.getEmptyIfNull(irmAdjVO.getTransactionrefno()).trim();
      int irmCount = 0;
      String countQuery = "SELECT COUNT(*) AS COUNT  FROM ETT_IRM_CLOSURE_TBL WHERE MASTER_REFNO = ? ";
      LoggableStatement ps = new LoggableStatement(con, countQuery);
      ps.setString(1, transRefNo);
      logger.info("--------------fetchIRMClosureData-----query--------" + ps.getQueryString());
      ResultSet rst = ps.executeQuery();
      if (rst.next())
      {
        irmCount = rst.getInt("COUNT");
        logger.info("--------------fetchIRMClosureData-----count------" + irmCount);
      }
      closePreparedStmtResultSet(ps, rst);
      if (irmCount == 0)
      {
        String query = "SELECT IRC.MASTER_REFNO AS MASTER_REFNO,IRC.ADCODE AS ADCODE,IRC.IECODE AS IECODE, IRC.CURRENCY AS CURRENCY,NVL(TRIM(IRC.REM_AMOUNT),0) - NVL(TRIM(IRM.INWARD_AMT),0) AS AMOUNT FROM ETT_IRM_XML_FILES_TBL IRC,(SELECT ADV.INWARD AS INWARD_NO,SUM(ADV.AMTUTIL/POWER(10,C82.C8CED)) AS INWARD_AMT  FROM MASTER MAS,BASEEVENT BAS,EXTEVENT EXT,EXTEVENTADV ADV,C8PF C82 WHERE MAS.KEY97 = BAS.MASTER_KEY AND BAS.KEY97 = EXT.EVENT AND EXT.KEY29 = ADV.FK_EVENT AND ADV.CCY_1 = C82.C8CCY GROUP BY ADV.INWARD) IRM WHERE TRIM(IRC.MASTER_REFNO) = TRIM(IRM.INWARD_NO (+)) AND MASTER_REFNO = ? ";

 
 
 
        pst = new LoggableStatement(con, query);
        pst.setString(1, transRefNo);
        logger.info("--------------fetchIRMClosureData-----transRefNo--query----" + pst.getQueryString());
        rs = pst.executeQuery();
        if (rs.next())
        {
          irmAdjVO = new IRMClosureVO();
          irmAdjVO.setTransactionrefno(DBUtility.getEmptyIfNull(rs.getString("MASTER_REFNO")).trim());
          irmAdjVO.setAdcode(DBUtility.getEmptyIfNull(rs.getString("ADCODE")).trim());
          irmAdjVO.setIecode(DBUtility.getEmptyIfNull(rs.getString("IECODE")).trim());
          irmAdjVO.setCurrency(DBUtility.getEmptyIfNull(rs.getString("CURRENCY")).trim());
          irmAdjVO.setAmount(DBUtility.getEmptyIfNull(rs.getString("AMOUNT")).trim());
          break label800;
        }
      }
      else
      {
        String query = "SELECT S_NO,MASTER_REFNO,ADCODE,IECODE,CURRENCY,AMOUNT,APPROVED_BY,LETTER_NO,TO_CHAR(TO_DATE(ADJ_DATE,'DD-MM-YY'),'DD-MM-YY') AS ADJ_DATE,REASON,DOC_NO,TO_CHAR(TO_DATE(DOC_DATE,'DD-MM-YY'),'DD-MM-YY') AS DOC_DATE,DOC_PORT,REC_IND,CLOSE_IND,REMARKS,CHECKER_REMARKS,STATUS,ACK_STATUS  FROM ETT_IRM_CLOSURE_TBL WHERE MASTER_REFNO = ? AND S_NO = (SELECT MAX(S_NO) FROM  ETT_IRM_CLOSURE_TBL WHERE MASTER_REFNO = ? )";

 
 
        pst = new LoggableStatement(con, query);
        pst.setString(1, transRefNo);
        pst.setString(2, transRefNo);
        logger.info("--------------fetchIRMClosureData---ETT_IRM_CLOSURE_TBL --transRefNo--query----" + pst.getQueryString());
        rs = pst.executeQuery();
        if (rs.next())
        {
          irmAdjVO = new IRMClosureVO();
          irmAdjVO.setTransactionrefno(DBUtility.getEmptyIfNull(rs.getString("MASTER_REFNO")).trim());
          irmAdjVO.setAdcode(DBUtility.getEmptyIfNull(rs.getString("ADCODE")).trim());
          irmAdjVO.setIecode(DBUtility.getEmptyIfNull(rs.getString("IECODE")).trim());
          irmAdjVO.setCurrency(DBUtility.getEmptyIfNull(rs.getString("CURRENCY")).trim());
          irmAdjVO.setAmount(DBUtility.getEmptyIfNull(rs.getString("AMOUNT")).trim());
          irmAdjVO.setApprovedBy(DBUtility.getEmptyIfNull(rs.getString("APPROVED_BY")).trim());
          irmAdjVO.setLetterNo(DBUtility.getEmptyIfNull(rs.getString("LETTER_NO")).trim());
          irmAdjVO.setAdjDate(DBUtility.getEmptyIfNull(rs.getString("ADJ_DATE")).trim());
          irmAdjVO.setReason(DBUtility.getEmptyIfNull(rs.getString("REASON")).trim());
          irmAdjVO.setDocNo(DBUtility.getEmptyIfNull(rs.getString("DOC_NO")).trim());
          irmAdjVO.setDocDate(DBUtility.getEmptyIfNull(rs.getString("DOC_DATE")).trim());
          irmAdjVO.setDocPort(DBUtility.getEmptyIfNull(rs.getString("DOC_PORT")).trim());
          irmAdjVO.setRecordInd(DBUtility.getEmptyIfNull(rs.getString("REC_IND")).trim());
          irmAdjVO.setCloseInd(DBUtility.getEmptyIfNull(rs.getString("CLOSE_IND")).trim());
          irmAdjVO.setRemarks(DBUtility.getEmptyIfNull(rs.getString("REMARKS")).trim());
          irmAdjVO.setCheckerRemarks(DBUtility.getEmptyIfNull(rs.getString("CHECKER_REMARKS")).trim());
          irmAdjVO.setAckStatus(DBUtility.getEmptyIfNull(rs.getString("ACK_STATUS")).trim());
          irmAdjVO.setStatus(DBUtility.getEmptyIfNull(rs.getString("STATUS")).trim());
          irmAdjVO.setSerialNo(DBUtility.getEmptyIfNull(rs.getString("S_NO")).trim());
        }
      }
    }
    catch (Exception exception)
    {
      logger.info("--------------fetchIRMClosureData-------exception------" + exception);
      throwDAOException(exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    logger.info("Exiting Method");
    return irmAdjVO;
  }
  public IRMClosureVO validateIRMClosureData(IRMClosureVO irmAdjVO)
    throws DAOException
  {
    logger.info("------------validateIRMClosureData--------------");
    CommonMethods commonMethods = null;
    try
    {
      commonMethods = new CommonMethods();
      if ((this.alertMsgArray != null) && 
        (this.alertMsgArray.size() > 0)) {
        this.alertMsgArray.clear();
      }
      String serialNo = DBUtility.getEmptyIfNull(irmAdjVO.getSerialNo()).trim();
      String transRefNo = DBUtility.getEmptyIfNull(irmAdjVO.getTransactionrefno()).trim();
      String recInd = DBUtility.getEmptyIfNull(irmAdjVO.getRecordInd()).trim();
      int irmAckCount = irmAckStatus(transRefNo);
      if (irmAckCount > 0)
      {
        int irmClosureCount = irmClosureCount(serialNo, transRefNo, recInd);
        if (irmClosureCount > 0)
        {
          String errormsg = UtilityGenerateCard.getErrorDesc("IRM007", "N127");
          Object[] arg = { Integer.valueOf(0), "E", errormsg, "Input" };
          setErrorvalues(arg);
        }
        else
        {
          int irmClosurePendingCount = irmClosurePendingCount(serialNo, transRefNo, recInd);
          if (irmClosurePendingCount > 0)
          {
            String errormsg = UtilityGenerateCard.getErrorDesc("IRM007", "N127");
            Object[] arg = { Integer.valueOf(0), "E", errormsg, "Input" };
            setErrorvalues(arg);
          }
          else
          {
            if (CommonMethods.isNull(irmAdjVO.getAmount()))
            {
              String errormsg = UtilityGenerateCard.getErrorDesc("IRM010", "N127");
              Object[] arg = { Integer.valueOf(0), "E", errormsg, "Input" };
              setErrorvalues(arg);
            }
            else
            {
              double irmAmt = Double.parseDouble(irmAdjVO.getAmount());
              if (irmAmt <= 0.0D)
              {
                String errormsg = "Adjustment Amount should be greater than Zero";
                Object[] arg = { Integer.valueOf(0), "E", errormsg, "Input" };
                setErrorvalues(arg);
              }
            }
            String approvedBy = irmAdjVO.getApprovedBy();
            if (CommonMethods.isNull(approvedBy))
            {
              String errormsg = UtilityGenerateCard.getErrorDesc("IRM011", "N127");
              Object[] arg = { Integer.valueOf(0), "E", errormsg, "Input" };
              setErrorvalues(arg);
            }
            if (CommonMethods.isNull(recInd))
            {
              String errormsg = UtilityGenerateCard.getErrorDesc("IRM002", "N127");
              Object[] arg = { Integer.valueOf(0), "E", errormsg, "Input" };
              setErrorvalues(arg);
            }
            else
            {
              String status = DBUtility.getEmptyIfNull(irmAdjVO.getStatus()).trim();
              String ackStatus = DBUtility.getEmptyIfNull(irmAdjVO.getAckStatus()).trim();
              if (recInd.equalsIgnoreCase("1")) {
                if ((ackStatus.equalsIgnoreCase("success")) && 
                  (status.equalsIgnoreCase("TC")))
                {
                  String errormsg = UtilityGenerateCard.getErrorDesc("IRM019", "N127");
                  Object[] arg = { Integer.valueOf(0), "E", errormsg, "Input" };
                  setErrorvalues(arg);
                }
                else if ((!ackStatus.equalsIgnoreCase("success")) && 
                  (status.equalsIgnoreCase("C")))
                {
                  String errormsg = "IRM Extension Acknowledgement is not Success";
                  Object[] arg = { Integer.valueOf(0), "E", errormsg, "Input" };
                  setErrorvalues(arg);
                }
                else if ((ackStatus.equalsIgnoreCase("success")) && 
                  (status.equalsIgnoreCase("A")))
                {
                  String errormsg = UtilityGenerateCard.getErrorDesc("IRM019", "N127");
                  Object[] arg = { Integer.valueOf(0), "E", errormsg, "Input" };
                  setErrorvalues(arg);
                }
              }
            }
            if ((approvedBy.equalsIgnoreCase("1")) && 
              (CommonMethods.isNull(irmAdjVO.getLetterNo())))
            {
              String errormsg = UtilityGenerateCard.getErrorDesc("IRM005", "N127");
              Object[] arg = { Integer.valueOf(0), "E", errormsg, "Input" };
              setErrorvalues(arg);
            }
            if (CommonMethods.isNull(irmAdjVO.getAdjDate()))
            {
              String errormsg = UtilityGenerateCard.getErrorDesc("IRM012", "N127");
              Object[] arg = { Integer.valueOf(0), "E", errormsg, "Input" };
              setErrorvalues(arg);
            }
            else
            {
              int adjCount = irmRemittanceValidation(transRefNo, irmAdjVO.getAdjDate());
              if (adjCount == 0)
              {
                String errormsg = UtilityGenerateCard.getErrorDesc("IRM020", "N127");
                Object[] arg = { Integer.valueOf(0), "E", errormsg, "Input" };
                setErrorvalues(arg);
              }
            }
            if (CommonMethods.isNull(irmAdjVO.getReason()))
            {
              String errormsg = UtilityGenerateCard.getErrorDesc("IRM013", "N127");
              Object[] arg = { Integer.valueOf(0), "E", errormsg, "Input" };
              setErrorvalues(arg);
            }
            else if (irmAdjVO.getReason().equalsIgnoreCase("2"))
            {
              if (CommonMethods.isNull(irmAdjVO.getDocNo()))
              {
                String errormsg = UtilityGenerateCard.getErrorDesc("IRM014", "N127");
                Object[] arg = { Integer.valueOf(0), "E", errormsg, "Input" };
                setErrorvalues(arg);
              }
              if (CommonMethods.isNull(irmAdjVO.getDocDate()))
              {
                String errormsg = UtilityGenerateCard.getErrorDesc("IRM015", "N127");
                Object[] arg = { Integer.valueOf(0), "E", errormsg, "Input" };
                setErrorvalues(arg);
              }
            }
            if (CommonMethods.isNull(irmAdjVO.getAdcode()))
            {
              String errormsg = UtilityGenerateCard.getErrorDesc("IRM008", "N127");
              Object[] arg = { Integer.valueOf(0), "E", errormsg, "Input" };
              setErrorvalues(arg);
            }
            if (CommonMethods.isNull(irmAdjVO.getIecode()))
            {
              String errormsg = UtilityGenerateCard.getErrorDesc("IRM009", "N127");
              Object[] arg = { Integer.valueOf(0), "E", errormsg, "Input" };
              setErrorvalues(arg);
            }
            if (recInd.equalsIgnoreCase("3"))
            {
              String ackStatus = DBUtility.getEmptyIfNull(irmAdjVO.getAckStatus()).trim();
              String status = DBUtility.getEmptyIfNull(irmAdjVO.getStatus()).trim();
              int irmCount = irmCount(transRefNo, recInd);
              if ((ackStatus.equalsIgnoreCase("success")) && 
                (!status.equalsIgnoreCase("RC")))
              {
                if (irmCount == 0)
                {
                  String errormsg = UtilityGenerateCard.getErrorDesc("IRM019", "N127");
                  Object[] arg = { Integer.valueOf(0), "E", errormsg, "Input" };
                  setErrorvalues(arg);
                }
              }
              else if (!ackStatus.equalsIgnoreCase("success"))
              {
                String errormsg = "IRM Extension Acknowledgement is not Success";
                Object[] arg = { Integer.valueOf(0), "E", errormsg, "Input" };
                setErrorvalues(arg);
              }
            }
          }
        }
      }
      else
      {
        String errormsg = UtilityGenerateCard.getErrorDesc("IRM001", "N127");
        Object[] arg = { Integer.valueOf(0), "E", errormsg, "Input" };
        setErrorvalues(arg);
      }
      if (this.alertMsgArray.size() > 0) {
        irmAdjVO.setErrorList(this.alertMsgArray);
      }
    }
    catch (Exception exception)
    {
      logger.info("------------validateIRMClosureData-----exception---------" + exception);
      throwDAOException(exception);
    }
    logger.info("Exiting Method");
    return irmAdjVO;
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
    logger.info("Entering Method");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    int count = 0;
    try
    {
      con = DBConnectionUtility.getConnection();
      String countQuery = "SELECT COUNT(*) AS LIST_COUNT  FROM ETT_IRM_XML_FILES_TBL WHERE UPPER(ACK_STATUS) = 'SUCCESS' AND MASTER_REFNO = ? ";
      pst = new LoggableStatement(con, countQuery);
      pst.setString(1, transRefNo);
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
  public int irmClosureCount(String serialNo, String transRefNo, String recInd)
    throws DAOException
  {
    logger.info("--------------------irmClosureCount--------------------");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    int count = 0;
    try
    {
      con = DBConnectionUtility.getConnection();
      String countQuery = "SELECT COUNT(*) AS LIST_COUNT  FROM ETT_IRM_CLOSURE_TBL WHERE STATUS NOT IN ('RC','RN') AND S_NO = ? AND MASTER_REFNO = ?  AND REC_IND = ? AND (TRIM(ACK_STATUS) IS NULL OR TRIM(ACK_STATUS) = 'SUCCESS') ";

 
      pst = new LoggableStatement(con, countQuery);
      logger.info("--------------------irmClosureCount-------------exception----pst---" + pst.getQueryString());
      pst.setString(1, serialNo);
      pst.setString(2, transRefNo);
      pst.setString(3, recInd);
      rs = pst.executeQuery();
      if (rs.next())
      {
        count = rs.getInt("LIST_COUNT");
        logger.info("--------------------irmClosureCount----------count---" + count);
      }
    }
    catch (Exception e)
    {
      logger.info("--------------------irmClosureCount-------------exception-------" + e);
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    logger.info("Exiting Method");
    return count;
  }
  public int irmCount(String transRefNo, String recInd)
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
		      String countQuery = "SELECT COUNT(*) AS LIST_COUNT  FROM ETT_IRM_CLOSURE_TBL WHERE STATUS != 'C' AND MASTER_REFNO = ? AND REC_IND != ? ";
		      pst = new LoggableStatement(con, countQuery);
		      pst.setString(1, transRefNo);
		      pst.setString(2, recInd);
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
		  public int irmClosurePendingCount(String serialNo, String transRefNo, String recInd)
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
		      String countQuery = "SELECT COUNT(*) AS LIST_COUNT  FROM ETT_IRM_CLOSURE_TBL WHERE STATUS IN ('TP','TC') AND S_NO = ? AND MASTER_REFNO = ?  AND REC_IND = ? AND TRIM(ACK_STATUS) = 'SUCCESS' ";

		 
		      pst = new LoggableStatement(con, countQuery);
		      pst.setString(1, serialNo);
		      pst.setString(2, transRefNo);
		      pst.setString(3, recInd);
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
		  public int irmRemittanceValidation(String transRefNo, String adjDate)
		    throws DAOException
		  {
		    logger.info("----------------irmRemittanceValidation-----------------");
		    LoggableStatement pst = null;
		    ResultSet rs = null;
		    Connection con = null;
		    int count = 0;
		    try
		    {
		      con = DBConnectionUtility.getConnection();
		      String countQuery = "SELECT COUNT(*) AS LIST_COUNT FROM ETT_IRM_XML_FILES_TBL WHERE UPPER(ACK_STATUS) = 'SUCCESS'  AND MASTER_REFNO = ?  AND REM_DATE <= TO_DATE(?,'DD-MM-YY') ";

		 
		      pst = new LoggableStatement(con, countQuery);
		      pst.setString(1, transRefNo);
		      pst.setString(2, adjDate);
		      logger.info("----------------irmRemittanceValidation---------query------" + pst.getQueryString());
		      rs = pst.executeQuery();
		      if (rs.next())
		      {
		        count = rs.getInt("LIST_COUNT");
		        logger.info("----------------irmRemittanceValidation--------count----" + count);
		      }
		    }
		    catch (Exception e)
		    {
		      logger.info("----------------irmRemittanceValidation----------exception-------" + e);
		      e.printStackTrace();
		    }
		    finally
		    {
		      DBConnectionUtility.surrenderDB(con, pst, rs);
		    }
		    logger.info("Exiting Method");
		    return count;
		  }
		  public String storeIRMClosureData(IRMClosureVO irmAdjVO)
		    throws DAOException
		  {
		    logger.info("---------------------storeIRMClosureData------------");
		    LoggableStatement pst = null;
		    ResultSet rs = null;
		    Connection con = null;
		    String result = null;
		    String loginedUserId = null;
		    CommonMethods commonMethods = null;
		    int i = 0;
		    try
		    {
		      con = DBConnectionUtility.getConnection();
		      commonMethods = new CommonMethods();
		      HttpSession session = ServletActionContext.getRequest()
		        .getSession();
		      loginedUserId = (String)session.getAttribute("loginedUserName");
		      String status = DBUtility.getEmptyIfNull(irmAdjVO.getStatus()).trim();
		      String ackStatus = DBUtility.getEmptyIfNull(irmAdjVO.getAckStatus()).trim();
		      if (((ackStatus.equalsIgnoreCase("success")) && 
		        (status.equalsIgnoreCase("A"))) || ((ackStatus.equalsIgnoreCase("success")) && 
		        (status.equalsIgnoreCase("RC"))))
		      {
		        if ((!CommonMethods.isNull(irmAdjVO.getRecordInd())) && (irmAdjVO.getRecordInd().equalsIgnoreCase("3")))
		        {
		          String updateQuery = "UPDATE ETT_IRM_CLOSURE_TBL SET REC_IND = ?,STATUS = ?,MAKER_USERID = ?,UPDATE_DATE = SYSTIMESTAMP  WHERE MASTER_REFNO = ? AND S_NO = ? ";
		          pst = new LoggableStatement(con, updateQuery);
		          pst.setString(1, DBUtility.getEmptyIfNull(irmAdjVO.getRecordInd()).trim());
		          pst.setString(2, "TC");
		          pst.setString(3, loginedUserId);
		          pst.setString(4, DBUtility.getEmptyIfNull(irmAdjVO.getTransactionrefno()).trim());
		          pst.setString(5, DBUtility.getEmptyIfNull(irmAdjVO.getSerialNo()).trim());
		          logger.info("Update Cancel IRM Ex: " + pst.getQueryString());
		          i = pst.executeUpdate();
		        }
		        else if ((!CommonMethods.isNull(irmAdjVO.getRecordInd())) && (irmAdjVO.getRecordInd().equalsIgnoreCase("1")))
		        {
		          String updateQuery = "UPDATE ETT_IRM_CLOSURE_TBL SET AMOUNT = ?,APPROVED_BY = ?,LETTER_NO = ?,ADJ_DATE = TO_DATE(?,'DD-MM-YY'),REASON = ?,DOC_NO = ?,DOC_DATE = TO_DATE(?,'DD-MM-YY'),DOC_PORT = ?,REC_IND = ?,CLOSE_IND = ?,REMARKS = ?,STATUS = ?,MAKER_USERID = ?,UPDATE_DATE = SYSTIMESTAMP WHERE MASTER_REFNO = ? AND S_NO = ? ";

		 
		 
		          pst = new LoggableStatement(con, updateQuery);
		          pst.setString(1, DBUtility.getEmptyIfNull(irmAdjVO.getAmount()).trim());
		          pst.setString(2, DBUtility.getEmptyIfNull(irmAdjVO.getApprovedBy()).trim());
		          pst.setString(3, DBUtility.getEmptyIfNull(irmAdjVO.getLetterNo()).trim());
		          pst.setString(4, DBUtility.getEmptyIfNull(irmAdjVO.getAdjDate()).trim());
		          pst.setString(5, DBUtility.getEmptyIfNull(irmAdjVO.getReason()).trim());
		          pst.setString(6, DBUtility.getEmptyIfNull(irmAdjVO.getDocNo()).trim());
		          pst.setString(7, DBUtility.getEmptyIfNull(irmAdjVO.getDocDate()).trim());
		          pst.setString(8, DBUtility.getEmptyIfNull(irmAdjVO.getDocPort()).trim());
		          pst.setString(9, DBUtility.getEmptyIfNull(irmAdjVO.getRecordInd()).trim());
		          pst.setString(10, DBUtility.getEmptyIfNull(irmAdjVO.getCloseInd()).trim());
		          pst.setString(11, DBUtility.getEmptyIfNull(irmAdjVO.getRemarks()).trim());
		          pst.setString(12, "TP");
		          pst.setString(13, loginedUserId);
		          pst.setString(14, DBUtility.getEmptyIfNull(irmAdjVO.getTransactionrefno()).trim());
		          pst.setString(15, DBUtility.getEmptyIfNull(irmAdjVO.getSerialNo()).trim());
		          logger.info("Update IRM Ex: " + pst.getQueryString());
		          i = pst.executeUpdate();
		        }
		      }
		      else if (status.equalsIgnoreCase("RN"))
		      {
		        String updateQuery = "UPDATE ETT_IRM_CLOSURE_TBL SET AMOUNT = ?,APPROVED_BY = ?,LETTER_NO = ?,ADJ_DATE = TO_DATE(?,'DD-MM-YY'),REASON = ?,DOC_NO = ?,DOC_DATE = TO_DATE(?,'DD-MM-YY'),DOC_PORT = ?,REC_IND = ?,CLOSE_IND = ?,REMARKS = ?,STATUS = ?,MAKER_USERID = ?,UPDATE_DATE = SYSTIMESTAMP WHERE MASTER_REFNO = ? AND S_NO = ? ";

		 
		 
		        pst = new LoggableStatement(con, updateQuery);
		        pst.setString(1, DBUtility.getEmptyIfNull(irmAdjVO.getAmount()).trim());
		        pst.setString(2, DBUtility.getEmptyIfNull(irmAdjVO.getApprovedBy()).trim());
		        pst.setString(3, DBUtility.getEmptyIfNull(irmAdjVO.getLetterNo()).trim());
		        pst.setString(4, DBUtility.getEmptyIfNull(irmAdjVO.getAdjDate()).trim());
		        pst.setString(5, DBUtility.getEmptyIfNull(irmAdjVO.getReason()).trim());
		        pst.setString(6, DBUtility.getEmptyIfNull(irmAdjVO.getDocNo()).trim());
		        pst.setString(7, DBUtility.getEmptyIfNull(irmAdjVO.getDocDate()).trim());
		        pst.setString(8, DBUtility.getEmptyIfNull(irmAdjVO.getDocPort()).trim());
		        pst.setString(9, DBUtility.getEmptyIfNull(irmAdjVO.getRecordInd()).trim());
		        pst.setString(10, DBUtility.getEmptyIfNull(irmAdjVO.getCloseInd()).trim());
		        pst.setString(11, DBUtility.getEmptyIfNull(irmAdjVO.getRemarks()).trim());
		        pst.setString(12, "TP");
		        pst.setString(13, loginedUserId);
		        pst.setString(14, DBUtility.getEmptyIfNull(irmAdjVO.getTransactionrefno()).trim());
		        pst.setString(15, DBUtility.getEmptyIfNull(irmAdjVO.getSerialNo()).trim());
		        logger.info("Update IRM Ex: " + pst.getQueryString());
		        i = pst.executeUpdate();
		      }
		      else if ((CommonMethods.isNull(status)) || (
		        (ackStatus.equalsIgnoreCase("success")) && 
		        (status.equalsIgnoreCase("C"))))
		      {
		        String insertQuery = "INSERT INTO ETT_IRM_CLOSURE_TBL (S_NO,MASTER_REFNO,ADCODE,IECODE,CURRENCY,AMOUNT,APPROVED_BY,LETTER_NO,ADJ_DATE,REASON,DOC_NO,DOC_DATE,DOC_PORT,REC_IND,CLOSE_IND,REMARKS,STATUS,MAKER_USERID) VALUES (IDPMS_IRM_CLOSURE_SEQ,?,?,?,?,?,?,?,TO_DATE(?,'DD-MM-YY'),?,?,TO_DATE(?,'DD-MM-YY'),?,?,?,?,?,?)";

		 
		        pst = new LoggableStatement(con, insertQuery);
		        pst.setString(1, DBUtility.getEmptyIfNull(irmAdjVO.getTransactionrefno()).trim());
		        pst.setString(2, DBUtility.getEmptyIfNull(irmAdjVO.getAdcode()).trim());
		        pst.setString(3, DBUtility.getEmptyIfNull(irmAdjVO.getIecode()).trim());
		        pst.setString(4, DBUtility.getEmptyIfNull(irmAdjVO.getCurrency()).trim());
		        pst.setString(5, DBUtility.getEmptyIfNull(irmAdjVO.getAmount()).trim());
		        pst.setString(6, DBUtility.getEmptyIfNull(irmAdjVO.getApprovedBy()).trim());
		        pst.setString(7, DBUtility.getEmptyIfNull(irmAdjVO.getLetterNo()).trim());
		        pst.setString(8, DBUtility.getEmptyIfNull(irmAdjVO.getAdjDate()).trim());
		        pst.setString(9, DBUtility.getEmptyIfNull(irmAdjVO.getReason()).trim());
		        pst.setString(10, DBUtility.getEmptyIfNull(irmAdjVO.getDocNo()).trim());
		        pst.setString(11, DBUtility.getEmptyIfNull(irmAdjVO.getDocDate()).trim());
		        pst.setString(12, DBUtility.getEmptyIfNull(irmAdjVO.getDocPort()).trim());
		        pst.setString(13, DBUtility.getEmptyIfNull(irmAdjVO.getRecordInd()).trim());
		        pst.setString(14, DBUtility.getEmptyIfNull(irmAdjVO.getCloseInd()).trim());
		        pst.setString(15, DBUtility.getEmptyIfNull(irmAdjVO.getRemarks()).trim());
		        pst.setString(16, "TP");
		        pst.setString(17, loginedUserId);
		        logger.info("Insert IRM Ex: " + pst.getQueryString());
		        i = pst.executeUpdate();
		      }
		      if (i > 0) {
		        result = "success";
		      }
		    }
		    catch (Exception exception)
		    {
		      logger.info("---------------------storeIRMClosureData--------exception----" + exception);
		      throwDAOException(exception);
		    }
		    finally
		    {
		      DBConnectionUtility.surrenderDB(con, pst, rs);
		    }
		    logger.info("Exiting Method");
		    return result;
		  }
		}
