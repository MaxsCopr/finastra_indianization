package in.co.mttenquiry.dao;

import com.opensymphony.xwork2.ActionContext;
import in.co.mttenquiry.dao.exception.DAOException;
import in.co.mttenquiry.utility.ActionConstantsQuery;
import in.co.mttenquiry.utility.CommonMethods;
import in.co.mttenquiry.utility.DBConnectionUtility;
import in.co.mttenquiry.utility.LoggableStatement;
import in.co.mttenquiry.vo.MTTDataVO;
import java.io.PrintStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;

public class MTTDataProcessDAO
  extends AbstractDAO
  implements ActionConstantsQuery
{
  static MTTDataProcessDAO dao;
  private static Logger logger = LogManager.getLogger(MTTDataProcessDAO.class.getName());
 
  public static MTTDataProcessDAO getDAO()
  {
    if (dao == null) {
      dao = new MTTDataProcessDAO();
    }
    return dao;
  }
 
  ArrayList<MTTDataVO> list = null;
  ArrayList<MTTDataVO> Pendinglist = null;
  ArrayList<MTTDataVO> Pendinglist1 = null;
 
  public void setDate()
    throws DAOException
  {
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    CommonMethods commonMethods = null;
    try
    {
      commonMethods = new CommonMethods();
      Map<String, Object> session = ActionContext.getContext().getSession();
      con = DBConnectionUtility.getConnection();
      String query = "SELECT TO_CHAR(TO_DATE(PROCDATE, 'dd-mm-yy'),'dd-mm-yyyy') as PROCDATE FROM dlyprccycl";
      pst = new LoggableStatement(con, query);
      rs = pst.executeQuery();
      while (rs.next())
      {
        String dateValue = commonMethods.getEmptyIfNull(rs.getString("PROCDATE")).trim();
        session.put("processDate", dateValue);
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
  }
 
  public int checkLoginedUserType(MTTDataVO mttVo)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement loggableStatement = null;
    ResultSet rs = null;
    int count = 0;
    String sqlQuery = null;
    CommonMethods commonMethods = null;
    try
    {
      commonMethods = new CommonMethods();
      con = DBConnectionUtility.getConnection();
      if (!commonMethods.isNull(mttVo.getSessionUserName()))
      {
        sqlQuery =
       
          "SELECT COUNT(*) AS LOGIN_COUNT FROM SECAGE88 U,TEAMUSRMAP T WHERE T.USERKEY = U.SKEY80 AND TRIM(UPPER(U.NAME85))  = TRIM(UPPER('" + mttVo.getSessionUserName() + "'))" + " AND TRIM(UPPER(T.TEAMKEY)) = TRIM(UPPER('" + mttVo.getPageType() + "'))";
       
        loggableStatement = new LoggableStatement(con, sqlQuery);
        logger.info("Checker Stage QUery--------------->" + loggableStatement.getQueryString());
        rs = loggableStatement.executeQuery();
        if (rs.next()) {
          count = rs.getInt("LOGIN_COUNT");
        }
        logger.info("Checker Stage QUery-----Count---------->" + count);
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
    return count;
  }
 
  public ArrayList<MTTDataVO> getMTTDetails(MTTDataVO mttVo)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement ppt = null;
    ResultSet rs = null;
    String sqlQuery = null;
    CommonMethods commonMethods = null;
    String masterRefNo = null;
    String eventRefNo = null;
    String mttNumber = null;
    String mttStatus = null;
    String mttcustomer = null;
    String mttfrmfinishedDate = null;
    String mtttofinishedDate = null;
    try
    {
      commonMethods = new CommonMethods();
      masterRefNo = commonMethods.getEmptyIfNull(mttVo.getMasRefNo()).trim();
      eventRefNo = commonMethods.getEmptyIfNull(mttVo.getEventRefNo()).trim();
      mttNumber = commonMethods.getEmptyIfNull(mttVo.getMttNumber()).trim();
      mttStatus = commonMethods.getEmptyIfNull(mttVo.getMttStatus()).trim();
      mttcustomer = commonMethods.getEmptyIfNull(mttVo.getCustomer()).trim();
      mttfrmfinishedDate = commonMethods.getEmptyIfNull(mttVo.getFrmfinished()).trim();
      mtttofinishedDate = commonMethods.getEmptyIfNull(mttVo.getTofinished()).trim();
     
      logger.info("Master_ref---->" + masterRefNo);
      logger.info("mttfrmfinishedDate---->" + mttfrmfinishedDate);
      logger.info("mtttofinishedDate---->" + mtttofinishedDate);
      logger.info("mttNumber---->" + mttNumber);
      this.list = new ArrayList();
      con = DBConnectionUtility.getConnection();
     




















      sqlQuery = " SELECT COUN_NUM,MTTNUMBER,MASTER_REF,EVENT_REF_NO,IMPORT_TRANS_AMOUNT,EXPORT_TRANS_AMOUNT, MTT_STATUS AS MTT_CLOSE_STATUS, CUSTOMER_CODE,CUSTOMER_NAME,FINISHED FROM  (SELECT ROWNUM AS COUN_NUM,TRIM(MARV.MTT_REF) AS MTTNUMBER, CASE WHEN MARV.IMPORT_BILL_ID is null THEN SUBSTR(MARV.EXPORT_BILL_ID,0,16) ELSE SUBSTR(MARV.IMPORT_BILL_ID,0,16) END AS MASTER_REF,  CASE WHEN MARV.IMPORT_BILL_ID is null THEN SUBSTR(MARV.EXPORT_BILL_ID,18,6) ELSE SUBSTR(MARV.IMPORT_BILL_ID,18,6) END AS EVENT_REF_NO,  MARV.IMPORT_BILL_AMOUNT||' '|| MARV.IMPORT_CCY AS IMPORT_TRANS_AMOUNT,MARV.EXPORT_BILL_AMOUNT||' '|| MARV.EXPORT_CCY AS EXPORT_TRANS_AMOUNT,  MTT_STATUS AS MTT_STATUS,MARV.CIF_NO AS CUSTOMER_CODE,MARV.CUSTOMER_NAME,REPORTING_DTE AS FINISHED  FROM MTT_ANNEXURE_RBI_VIEW MARV WHERE TRIM(MARV.MTT_REF)=TRIM(MARV.MTT_REF) ";
      if ((masterRefNo != null) && (!masterRefNo.equalsIgnoreCase("")))
      {
        sqlQuery = sqlQuery + " AND CASE WHEN MARV.IMPORT_BILL_ID is null THEN SUBSTR(MARV.EXPORT_BILL_ID,0,16) ELSE SUBSTR(MARV.IMPORT_BILL_ID,0,16) END = '" + masterRefNo + "' ";
       
        mttVo.setMasRefNo(masterRefNo);
      }
      if ((eventRefNo != null) && (!eventRefNo.equalsIgnoreCase("")))
      {
        sqlQuery = sqlQuery + " AND CASE WHEN MARV.IMPORT_BILL_ID is null THEN SUBSTR(MARV.EXPORT_BILL_ID,18,6) ELSE SUBSTR(MARV.IMPORT_BILL_ID,18,6) END = '" + eventRefNo + "' ";
       
        mttVo.setEventRefNo(eventRefNo);
      }
      if ((mttNumber != null) && (!mttNumber.equalsIgnoreCase("")))
      {
        sqlQuery = sqlQuery + " AND TRIM(MARV.MTT_REF) = '" + mttNumber + "' ";
       
        mttVo.setMttNumber(mttNumber);
      }
      if ((mttStatus != null) && (!mttStatus.equalsIgnoreCase("")) && (!mttStatus.equalsIgnoreCase("B")))
      {
        sqlQuery = sqlQuery + " AND TRIM(MARV.MTT_STATUS) LIKE '%" + mttStatus + "%' ";
       
        mttVo.setMttStatus(mttStatus);
      }
      if ((mttcustomer != null) && (!mttcustomer.equalsIgnoreCase("")))
      {
        sqlQuery = sqlQuery + " AND TRIM(MARV.CIF_NO) LIKE '%" + mttcustomer + "%' ";
       
        mttVo.setCustomer(mttcustomer);
      }
      if ((mttfrmfinishedDate != null) && (!mttfrmfinishedDate.equalsIgnoreCase("")))
      {
        sqlQuery = sqlQuery + " AND MARV.REPORTING_DTE BETWEEN '" + mttfrmfinishedDate + "' AND '" + mtttofinishedDate + "' ";
        mttVo.setFrmfinished(mttfrmfinishedDate);
        mttVo.setTofinished(mtttofinishedDate);
      }
      sqlQuery = sqlQuery + " ) ";
     

      ppt = new LoggableStatement(con, sqlQuery);
      logger.info("Get RBI Values from DB " + ppt.getQueryString());
      rs = ppt.executeQuery();
      while (rs.next())
      {
        mttVo = new MTTDataVO();
        mttVo.setMttListCountNum(commonMethods.getEmptyIfNull(rs.getString("COUN_NUM")).trim());
        mttVo.setMttListMasterRef(commonMethods.getEmptyIfNull(rs.getString("MASTER_REF")).trim());
        mttVo.setListEventeRefNo(commonMethods.getEmptyIfNull(rs.getString("EVENT_REF_NO")).trim());
        mttVo.setMttListNumber(commonMethods.getEmptyIfNull(rs.getString("MTTNUMBER")).trim());
        mttVo.setMttListcustomer(commonMethods.getEmptyIfNull(rs.getString("CUSTOMER_CODE")).trim());
        mttVo.setMttListTransAmt(commonMethods.getEmptyIfNull(rs.getString("IMPORT_TRANS_AMOUNT")).trim());
        mttVo.setMttListOtstAmt(commonMethods.getEmptyIfNull(rs.getString("EXPORT_TRANS_AMOUNT")).trim());
        mttVo.setMttListStatus(commonMethods.getEmptyIfNull(rs.getString("MTT_CLOSE_STATUS")).trim());
        mttVo.setMttListfinished(commonMethods.getEmptyIfNull(rs.getString("FINISHED")).trim());
       

        this.list.add(mttVo);
      }
    }
    catch (SQLException e)
    {
      throwDAOException(e);
    }
    finally
    {
      closeSqlRefferance(rs, ppt, con);
    }
    logger.info("Exiting Method");
    return this.list;
  }
 
  public String fetchStatusForMTT(MTTDataVO mttVo)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement ppt = null;
    ResultSet rs = null;
    String sqlQuery = null;
    LoggableStatement ppt1 = null;
    ResultSet rs1 = null;
    String sqlQuery1 = null;
    CommonMethods commonMethods = null;
   
    String mttNumber = null;
    String cur_status = "";
    String new_status = "";
    String mttNumberCnt = "";
    try
    {
      commonMethods = new CommonMethods();
      mttNumber = commonMethods.getEmptyIfNull(mttVo.getMttStatusNumber()).trim();
     

      con = DBConnectionUtility.getConnection();
     
      sqlQuery = "SELECT EME.MTTNUMBER,EME.MTT_CLOSE_STATUS FROM ETT_MTT_ENQUIRY EME ";
      if ((mttNumber != null) && (!mttNumber.equalsIgnoreCase("")))
      {
        sqlQuery = sqlQuery + " WHERE EME.MTTNUMBER = '" + mttNumber + "' ";
       
        mttVo.setMttStatusNumber(mttNumber);
      }
      ppt = new LoggableStatement(con, sqlQuery);
      logger.info("Get RBI Values from DB " + ppt.getQueryString());
      rs = ppt.executeQuery();
      while (rs.next())
      {
        if (commonMethods.getEmptyIfNull(rs.getString("MTT_CLOSE_STATUS")).trim().equals("Y"))
        {
          cur_status = "Closed";
          new_status = "Active";
          break;
        }
        if (commonMethods.getEmptyIfNull(rs.getString("MTT_CLOSE_STATUS")).trim().equals("N"))
        {
          cur_status = "Active";
          new_status = "Closed";
        }
      }
      mttVo.setMttCurStatus(cur_status);
      mttVo.setMttNewStatus(new_status);
     

      sqlQuery1 = "SELECT COUNT(*) AS COUNT FROM ETT_MTT_ENQUIRY EME ";
      if ((mttNumber != null) && (!mttNumber.equalsIgnoreCase(""))) {
        sqlQuery1 = sqlQuery1 + " WHERE EME.MTTNUMBER = '" + mttNumber + "'";
      }
      ppt1 = new LoggableStatement(con, sqlQuery1);
      logger.info("Get RBI Values from DB " + ppt1.getQueryString());
      rs1 = ppt1.executeQuery();
      if (rs1.next()) {
        mttNumberCnt = commonMethods.getEmptyIfNull(rs1.getString("COUNT")).trim();
      }
    }
    catch (SQLException e)
    {
      throwDAOException(e);
    }
    finally
    {
      closeSqlRefferance(rs1, ppt1, null);
      closeSqlRefferance(rs, ppt, con);
    }
    logger.info("Exiting Method");
    return mttNumberCnt;
  }
 
  public String checkInProgressCntForMTT(MTTDataVO mttVo)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
   
    LoggableStatement ppt1 = null;
    ResultSet rs1 = null;
    String sqlQuery1 = null;
    CommonMethods commonMethods = null;
    String mttNumber = null;
   
    String mttNumberCnt = "";
    try
    {
      commonMethods = new CommonMethods();
      mttNumber = commonMethods.getEmptyIfNull(mttVo.getMttStatusNumber()).trim();
     

      con = DBConnectionUtility.getConnection();
     
      sqlQuery1 = "SELECT COUNT(*) AS COUNT FROM MTT_Status_Amend_MakerChecker MSAM ";
      if ((mttNumber != null) && (!mttNumber.equalsIgnoreCase(""))) {
        sqlQuery1 = sqlQuery1 + " WHERE MSAM.MTTNUMBER = '" + mttNumber + "' AND TRIM(MTTMAKERCHECKERSTATUS)='P'";
      }
      ppt1 = new LoggableStatement(con, sqlQuery1);
      logger.info("Get In Progress Count from DB " + ppt1.getQueryString());
      rs1 = ppt1.executeQuery();
      if (rs1.next()) {
        mttNumberCnt = commonMethods.getEmptyIfNull(rs1.getString("COUNT")).trim();
      }
    }
    catch (SQLException e)
    {
      throwDAOException(e);
    }
    finally
    {
      closeSqlRefferance(rs1, ppt1, con);
    }
    logger.info("Exiting Method");
    return mttNumberCnt;
  }
 
  public int changeStatusForMTT(MTTDataVO mttVo)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement ppt = null;
   
    String sqlQuery = null;
   
    CommonMethods commonMethods = null;
   
    String mttNumber = null;
   
    int updateCnt = 0;
    String mttNewStatus = null;
    try
    {
      HttpSession session = ServletActionContext.getRequest().getSession();
      commonMethods = new CommonMethods();
      mttNumber = commonMethods.getEmptyIfNull(mttVo.getMttStatusNumber()).trim();
     
      mttNewStatus = commonMethods.getEmptyIfNull(mttVo.getMttNewStatus()).trim();
     


      String loginedUserId = (String)session.getAttribute("loginedUserId");
      logger.info("loginedUserId IN changeStatusForMTT :: " + loginedUserId);
      con = DBConnectionUtility.getConnection();
     
      sqlQuery = "INSERT INTO MTT_STATUS_AMEND_MAKERCHECKER (MTT_Key97,MTTNumber,MTTCurrentStatus,MTTMakerCheckerStatus,MTTMakerUserID,MTTMakerTimestamp) \t\t\t\t\t\t\t\t\t   VALUES (MTT_STATUS_AMEND_SEQ.NEXTVAL,?,?,?,?,CURRENT_TIMESTAMP)";
     


      ppt = new LoggableStatement(con, sqlQuery);
      ppt.setString(1, mttNumber);
      ppt.setString(2, mttNewStatus);
      ppt.setString(3, "P");
      ppt.setString(4, loginedUserId);
      logger.info(" Insert Query " + ppt.getQueryString());
      updateCnt = ppt.executeUpdate();
    }
    catch (SQLException e)
    {
      throwDAOException(e);
    }
    finally
    {
      closeSqlRefferance(null, ppt, con);
    }
    logger.info("Exiting Method");
    return updateCnt;
  }
 
  public ArrayList<MTTDataVO> getPendingDataForChecker(MTTDataVO mttVo)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement ppt = null;
    ResultSet rs = null;
    String sqlQuery = null;
    LoggableStatement ppt1 = null;
    ResultSet rs1 = null;
   
    CommonMethods commonMethods = null;
   
    String mttNumber = null;
   

    int setValue = 0;
    try
    {
      HttpSession session = ServletActionContext.getRequest().getSession();
     
      commonMethods = new CommonMethods();
      mttNumber = commonMethods.getEmptyIfNull(mttVo.getMttStatusNumber()).trim();
     
      String loginedUserId = (String)session.getAttribute("loginedUserId");
      logger.info("loginedUserId IN getPendingDataForChecker :: " + loginedUserId);
      this.Pendinglist = new ArrayList();
      con = DBConnectionUtility.getConnection();
     
      sqlQuery = "SELECT MTT_Key97,MTTNumber,MTTCurrentStatus,MTTMakerUserID,MTTMakerTimestamp FROM MTT_Status_Amend_MakerChecker WHERE MTTNUMBER=MTTNUMBER AND MTTMakerCheckerStatus='P'";
      if ((mttNumber != null) && (!mttNumber.equalsIgnoreCase("")))
      {
        sqlQuery = sqlQuery + " AND  MTTNUMBER = '" + mttNumber + "' ";
       
        mttVo.setMttStatusCheckerNumber(mttNumber);
      }
      if (!commonMethods.isNull(loginedUserId)) {
        sqlQuery = sqlQuery + " AND (MTTMakerUserID != ? OR trim(MTTMAKERUSERID) IS NULL) ";
      }
      ppt = new LoggableStatement(con, sqlQuery);
      if ((mttNumber != null) && (!mttNumber.equalsIgnoreCase(""))) {
        ppt.setString(++setValue, mttNumber);
      }
      if (!commonMethods.isNull(loginedUserId)) {
        ppt.setString(++setValue, loginedUserId);
      }
      logger.info("Select Query " + ppt.getQueryString());
      rs = ppt.executeQuery();
      while (rs.next())
      {
        mttVo = new MTTDataVO();
        mttVo.setMttListKeyId(commonMethods.getEmptyIfNull(rs.getString("MTT_Key97")).trim());
        mttVo.setMttListMTTNumber(commonMethods.getEmptyIfNull(rs.getString("MTTNumber")).trim());
        mttVo.setMttListCurrentStatus(commonMethods.getEmptyIfNull(rs.getString("MTTCurrentStatus")).trim());
        mttVo.setMttListMakerUserId(commonMethods.getEmptyIfNull(rs.getString("MTTMakerUserID")).trim());
        mttVo.setMttListMakertmstmp(commonMethods.getEmptyIfNull(rs.getString("MTTMakerTimestamp")).trim());
       
        this.Pendinglist.add(mttVo);
      }
    }
    catch (SQLException e)
    {
      throwDAOException(e);
    }
    finally
    {
      closeSqlRefferance(rs1, ppt1, null);
      closeSqlRefferance(rs, ppt, con);
    }
    logger.info("Exiting Method");
    return this.Pendinglist;
  }
 
  public String updateStatus(String[] chkList, String check, String remarks)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement loggableStatement = null;
    ResultSet rs = null;
    String result = "fail";
    String query = null;
    String sqlQuery1 = null;
    int count = 0;
    try
    {
      con = DBConnectionUtility.getConnection();
     
      HttpSession session = ServletActionContext.getRequest().getSession();
     
      String loginedUserId = (String)session.getAttribute("loginedUserName");
      logger.info("loginedUserId IN updateStatus :: " + loginedUserId);
      if ((chkList != null) && (check != null)) {
        for (int i = 0; i < chkList.length; i++)
        {
          String temp = chkList[i];
          String[] a = temp.split(":");
          logger.info("check updateStatus :: " + check);
          if (check.equalsIgnoreCase("Approve"))
          {
            query = "UPDATE MTT_Status_Amend_MakerChecker SET MTTMakerCheckerStatus ='A',MTTCheckerTimestamp = CURRENT_TIMESTAMP,MTTCheckerUserID=?,MTTREMARKS=?  WHERE MTT_Key97 =?";
           

            loggableStatement = new LoggableStatement(con, query);
            loggableStatement.setString(1, loginedUserId);
            loggableStatement.setString(2, remarks);
            loggableStatement.setInt(3, Integer.parseInt(a[0]));
           
            count = loggableStatement.executeUpdate();
            String newStatus = "";
            if (a[2].equals("Closed")) {
              newStatus = "Y";
            } else {
              newStatus = "N";
            }
            if (count > 0)
            {
              sqlQuery1 = "UPDATE EXTEVENT EE SET EE.MTTCLOSE='" + newStatus + "' WHERE EE.MTTNUM = '" + a[1] + "' ";
             
              LoggableStatement loggableStatement1 = new LoggableStatement(con, sqlQuery1);
             
              loggableStatement1.executeUpdate();
             
              closeSqlRefferance(null, loggableStatement1, null);
            }
          }
          else if (check.equalsIgnoreCase("Reject"))
          {
            query = "UPDATE MTT_Status_Amend_MakerChecker SET MTTMakerCheckerStatus ='R',MTTCheckerTimestamp = CURRENT_TIMESTAMP,MTTCheckerUserID=?,MTTREMARKS=?  WHERE MTT_Key97 =?";
           

            loggableStatement = new LoggableStatement(con, query);
            loggableStatement.setString(1, loginedUserId);
            loggableStatement.setString(2, remarks);
            loggableStatement.setInt(3, Integer.parseInt(a[0]));
            count = loggableStatement.executeUpdate();
          }
        }
      }
      if (count > 0) {
        result = "SUCCESS";
      }
    }
    catch (Exception e)
    {
      logger.info("updateStatus---------------------------Exception-------------" + e);
      e.printStackTrace();
      throwDAOException(e);
    }
    finally
    {
      closeSqlRefferance(rs, loggableStatement, con);
    }
    logger.info("Exiting Method");
    return result;
  }
 
  public ArrayList<MTTDataVO> getMTTDetailsForAmend(MTTDataVO mttVo)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement ppt = null;
    ResultSet rs = null;
    String sqlQuery = null;
    CommonMethods commonMethods = null;
    String masterRefNo = null;
    String eventRefNo = null;
    String mttNumber = null;
    String mttStatus = null;
    String mttcustomer = null;
    String mttfrmfinishedDate = null;
    String mtttofinishedDate = null;
    try
    {
      commonMethods = new CommonMethods();
      masterRefNo = commonMethods.getEmptyIfNull(mttVo.getMasRefNo()).trim();
      eventRefNo = commonMethods.getEmptyIfNull(mttVo.getEventRefNo()).trim();
      mttNumber = commonMethods.getEmptyIfNull(mttVo.getMttNumber()).trim();
      mttStatus = commonMethods.getEmptyIfNull(mttVo.getMttStatus()).trim();
      mttcustomer = commonMethods.getEmptyIfNull(mttVo.getCustomer()).trim();
     
      mttfrmfinishedDate = commonMethods.getEmptyIfNull(mttVo.getFrmfinished()).trim();
      mtttofinishedDate = commonMethods.getEmptyIfNull(mttVo.getTofinished()).trim();
      logger.info("Master_ref---->" + masterRefNo);
      this.list = new ArrayList();
      con = DBConnectionUtility.getConnection();
     





      sqlQuery = " SELECT COUN_NUM,MTTNUMBER,MASTER_REF,EVENT_REF_NO,IMPORT_TRANS_AMOUNT,EXPORT_TRANS_AMOUNT, CASE WHEN MTT_STATUS='Y' THEN 'CLOSED' ELSE 'ACTIVE' END as MTT_CLOSE_STATUS,CUSTOMER_CODE,CUSTOMER_NAME,FINISHED FROM (  SELECT ROWNUM AS COUN_NUM,EME.MTTNUMBER,EME.MASTER_REF,EME.EVENT_REF_NO, MAMV.IMPORT_BILL_AMOUNT||' '|| MAMV.IMPORT_CCY AS IMPORT_TRANS_AMOUNT,  MAMV.EXPORT_BILL_AMOUNT||' '|| MAMV.EXPORT_CCY AS EXPORT_TRANS_AMOUNT, (SELECT EME1.MTT_CLOSE_STATUS FROM ETT_MTT_ENQUIRY EME1 WHERE TRIM(EME1.MTTNUMBER)=TRIM(EME.MTTNUMBER) AND EME1.MTT_CLOSE_STATUS='Y' AND ROWNUM=1) AS MTT_STATUS,  EME.CUSTOMER_CODE,EME.CUSTOMER_NAME,EME.FINISHED  FROM ETT_MTT_ENQUIRY EME,MTT_ANNEXURE_RBI_VIEW MAMV  WHERE EME.MTTNUMBER=MAMV.MTT_REF  and (trim(EME.MASTER_REF)||'-'||trim(EME.EVENT_REF_NO)=MAMV.IMPORT_BILL_ID OR trim(EME.MASTER_REF)||'-'||trim(EME.EVENT_REF_NO)=MAMV.EXPORT_BILL_ID) ";
      if ((masterRefNo != null) && (!masterRefNo.equalsIgnoreCase("")))
      {
        sqlQuery = sqlQuery + " AND EME.MASTER_REF = '" + masterRefNo + "' ";
       
        mttVo.setMasRefNo(masterRefNo);
      }
      if ((eventRefNo != null) && (!eventRefNo.equalsIgnoreCase("")))
      {
        sqlQuery = sqlQuery + " AND EME.EVENT_REF_NO = '" + eventRefNo + "' ";
       
        mttVo.setEventRefNo(eventRefNo);
      }
      if ((mttNumber != null) && (!mttNumber.equalsIgnoreCase("")))
      {
        sqlQuery = sqlQuery + " AND EME.MTTNUMBER = '" + mttNumber + "' ";
       
        mttVo.setMttNumber(mttNumber);
      }
      if ((mttStatus != null) && (!mttStatus.equalsIgnoreCase("")) && (!mttStatus.equalsIgnoreCase("B")))
      {
        sqlQuery = sqlQuery + " AND EME.MTT_CLOSE_STATUS = '" + mttStatus + "' ";
       
        mttVo.setMttStatus(mttStatus);
      }
      if ((mttcustomer != null) && (!mttcustomer.equalsIgnoreCase("")))
      {
        sqlQuery = sqlQuery + " AND EME.CUSTOMER_CODE like '%" + mttcustomer + "%' ";
       
        mttVo.setCustomer(mttcustomer);
      }
      if ((mttfrmfinishedDate != null) && (!mttfrmfinishedDate.equalsIgnoreCase("")))
      {
        sqlQuery = sqlQuery + " AND EME.FINISHED BETWEEN '" + mttfrmfinishedDate + "' AND '" + mtttofinishedDate + "' ";
        mttVo.setFrmfinished(mttfrmfinishedDate);
        mttVo.setTofinished(mtttofinishedDate);
      }
      sqlQuery = sqlQuery + " AND EME.MTTNUMBER NOT IN (SELECT TRIM(MNA.MTTNUMBER) FROM MTT_Number_Amend_MakerChecker MNA where MNA.MTT_Amend_MakerChecker_Status ='P' and Trim(MNA.MASTERREF) = Trim(EME.MASTER_REF) AND  Trim(MNA.EVENTREF) = Trim(EME.EVENT_REF_NO)) ";
      sqlQuery = sqlQuery + " ) ";
     

      ppt = new LoggableStatement(con, sqlQuery);
      logger.info("Get RBI Values from DB " + ppt.getQueryString());
      logger.info("Get RBI Values from DB " + ppt.getQueryString());
      rs = ppt.executeQuery();
      while (rs.next())
      {
        mttVo = new MTTDataVO();
        mttVo.setMttListCountNum(commonMethods.getEmptyIfNull(rs.getString("COUN_NUM")).trim());
        mttVo.setMttListMasterRef(commonMethods.getEmptyIfNull(rs.getString("MASTER_REF")).trim());
        mttVo.setListEventeRefNo(commonMethods.getEmptyIfNull(rs.getString("EVENT_REF_NO")).trim());
        mttVo.setMttListNumber(commonMethods.getEmptyIfNull(rs.getString("MTTNUMBER")).trim());
        mttVo.setMttListcustomer(commonMethods.getEmptyIfNull(rs.getString("CUSTOMER_NAME")).trim());
        mttVo.setMttListTransAmt(commonMethods.getEmptyIfNull(rs.getString("IMPORT_TRANS_AMOUNT")).trim());
        mttVo.setMttListOtstAmt(commonMethods.getEmptyIfNull(rs.getString("EXPORT_TRANS_AMOUNT")).trim());
        mttVo.setMttListStatus(commonMethods.getEmptyIfNull(rs.getString("MTT_CLOSE_STATUS")).trim());
        mttVo.setMttListfinished(commonMethods.getEmptyIfNull(rs.getString("FINISHED")).trim());
       

        this.list.add(mttVo);
      }
    }
    catch (SQLException e)
    {
      throwDAOException(e);
    }
    finally
    {
      closeSqlRefferance(rs, ppt, con);
    }
    logger.info("Exiting Method");
    return this.list;
  }
 
  public int storeDeleteMTTNumber(String[] chkList, MTTDataVO mttVo)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement ppt = null;
    String sqlQuery = null;
    int updateCnt = 0;
    LoggableStatement pptEventQuery = null;
    ResultSet rsEventQuery = null;
    try
    {
      con = DBConnectionUtility.getConnection();
     
      HttpSession session = ServletActionContext.getRequest().getSession();
     
      String loginedUserId = (String)session.getAttribute("loginedUserName");
      logger.info("loginedUserId IN updateStatus :: " + loginedUserId);
      if (chkList != null) {
        for (int i = 0; i < chkList.length; i++)
        {
          String temp = chkList[i];
          String[] a = temp.split(":");
          logger.info("check updateStatus 0 :: " + Integer.parseInt(a[0]));
          logger.info("check updateStatus 1 :: " + a[1]);
          logger.info("check updateStatus 2 :: " + a[2]);
         

          String eventKeyId = "";
         
          String eventSqlQuery = "select EE.KEY29 as eventkeyId from extevent ee, master mas, BASEEVENT be  where MAS.key97=BE.MASTER_KEY and BE.KEY97=EE.EVENT  AND MAS.MASTER_REF='" +
         
            a[1] + "' and TRIM(BE.REFNO_PFIX)||lPAD(TRIM(BE.REFNO_SERL),3,0)='" + a[2] + "'";
         
          pptEventQuery = new LoggableStatement(con, eventSqlQuery);
         

          logger.info("Get RBI Values from DB " + pptEventQuery.getQueryString());
          logger.info("Get RBI Values from DB " + pptEventQuery.getQueryString());
          rsEventQuery = pptEventQuery.executeQuery();
          while (rsEventQuery.next())
          {
            logger.info("Inside resultset ");
            eventKeyId = rsEventQuery.getString("eventkeyId").trim();
            logger.info("Get RBI Values from DB " + eventKeyId);
          }
          sqlQuery = "INSERT INTO MTT_Number_Amend_MakerChecker (MTTNumber_Key97,MTTeventKyId,MTTNumber,MasterRef,EventRef,MTT_Amend_MakerChecker_Status,MTTMakerUserID,MTTMakerTimestamp) \t\t\t\t\t\t\t\t\t   VALUES (MTT_NUMBER_AMEND_SEQ.NEXTVAL,?,?,?,?,?,?,CURRENT_TIMESTAMP)";
         

          ppt = new LoggableStatement(con, sqlQuery);
          ppt.setInt(1, Integer.parseInt(eventKeyId.toString()));
          ppt.setInt(2, Integer.parseInt(a[0]));
          ppt.setString(3, a[1]);
          ppt.setString(4, a[2]);
          ppt.setString(5, "P");
          ppt.setString(6, loginedUserId);
          logger.info(" Insert Query " + ppt.getQueryString());
          updateCnt = ppt.executeUpdate();
        }
      }
    }
    catch (Exception e)
    {
      logger.info("storeDeleteMTTNumber---------------------------Exception-------------" + e);
      e.printStackTrace();
      throwDAOException(e);
    }
    finally
    {
      closeSqlRefferance(rsEventQuery, pptEventQuery, con);
      closeSqlRefferance(null, ppt, con);
    }
    logger.info("Exiting Method");
    return updateCnt;
  }
 
  public ArrayList<MTTDataVO> getPendingMttNumberAmendDataForChecker(MTTDataVO mttVo)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement ppt = null;
    ResultSet rs = null;
    String sqlQuery = null;
    CommonMethods commonMethods = null;
    String mttNumber = null;
    int setValue = 0;
    try
    {
      HttpSession session = ServletActionContext.getRequest().getSession();
     
      commonMethods = new CommonMethods();
      mttNumber = commonMethods.getEmptyIfNull(mttVo.getMttStatusNumber()).trim();
     
      String loginedUserId = (String)session.getAttribute("loginedUserId");
      logger.info("loginedUserId IN getPendingDataForChecker :: " + loginedUserId);
      this.Pendinglist = new ArrayList();
      con = DBConnectionUtility.getConnection();
     
      sqlQuery = "SELECT MTTNumber_Key97,MTTeventKyId,MTTNumber,MasterRef,EventRef,MTTMakerUserID,MTTMakerTimestamp FROM MTT_Number_Amend_MakerChecker WHERE MTTNUMBER=MTTNUMBER AND MTT_Amend_MakerChecker_Status='P'";
      if ((mttNumber != null) && (!mttNumber.equalsIgnoreCase("")))
      {
        sqlQuery = sqlQuery + " AND  MTTNUMBER = '" + mttNumber + "' ";
       
        mttVo.setMttStatusCheckerNumber(mttNumber);
      }
      if (!commonMethods.isNull(loginedUserId)) {
        sqlQuery = sqlQuery + " AND (MTTMakerUserID != ? OR trim(MTTMakerUserID) IS NULL) ";
      }
      ppt = new LoggableStatement(con, sqlQuery);
      if ((mttNumber != null) && (!mttNumber.equalsIgnoreCase(""))) {
        ppt.setString(++setValue, mttNumber);
      }
      if (!commonMethods.isNull(loginedUserId)) {
        ppt.setString(++setValue, loginedUserId);
      }
      logger.info("Select Query " + ppt.getQueryString());
      rs = ppt.executeQuery();
      while (rs.next())
      {
        mttVo = new MTTDataVO();
        mttVo.setMttNumberListKeyId(commonMethods.getEmptyIfNull(rs.getString("MTTNumber_Key97")).trim());
        mttVo.setMttNumberListEventKeyId(commonMethods.getEmptyIfNull(rs.getString("MTTeventKyId")).trim());
        mttVo.setMttNumberListMttNumber(commonMethods.getEmptyIfNull(rs.getString("MTTNumber")).trim());
        mttVo.setMttNumberListMasterReference(commonMethods.getEmptyIfNull(rs.getString("MasterRef")).trim());
        mttVo.setMttNumberListEventRef(commonMethods.getEmptyIfNull(rs.getString("EventRef")).trim());
        mttVo.setMttNumberListMakerUserId(commonMethods.getEmptyIfNull(rs.getString("MTTMakerUserID")).trim());
        mttVo.setMttNumberListMakertmstmp(commonMethods.getEmptyIfNull(rs.getString("MTTMakerTimestamp")).trim());
       
        this.Pendinglist.add(mttVo);
      }
    }
    catch (SQLException e)
    {
      throwDAOException(e);
    }
    finally
    {
      closeSqlRefferance(rs, ppt, con);
    }
    logger.info("Exiting Method");
    return this.Pendinglist;
  }
 
  public ArrayList<MTTDataVO> getPendingMttNumberAddForChecker(MTTDataVO mttVo)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement ppt = null;
    ResultSet rs = null;
    String sqlQuery = null;
    CommonMethods commonMethods = null;
    String mttNumber = null;
    int setValue = 0;
    try
    {
      HttpSession session = ServletActionContext.getRequest().getSession();
     
      commonMethods = new CommonMethods();
      mttNumber = commonMethods.getEmptyIfNull(mttVo.getMttStatusNumber()).trim();
     
      String loginedUserId = (String)session.getAttribute("loginedUserId");
      logger.info("loginedUserId IN getPendingMttNumberAddForChecker :: " + loginedUserId);
      this.Pendinglist1 = new ArrayList();
      con = DBConnectionUtility.getConnection();
     
      sqlQuery = "SELECT MTTAmend_Key97,MTTNumber,MasterRef,EventRef,MTTAmendMakerUserID,MTTAmendMakerTimestamp FROM MTTNumberAmendMasterEventRef WHERE MTTNumber=MTTNumber AND MTTAMENDMAKERCHECKERSTATUS='P'";
      if ((mttNumber != null) && (!mttNumber.equalsIgnoreCase("")))
      {
        sqlQuery = sqlQuery + " AND  MTTNumber = '" + mttNumber + "' ";
       
        mttVo.setMttStatusCheckerNumber(mttNumber);
      }
      if (!commonMethods.isNull(loginedUserId)) {
        sqlQuery = sqlQuery + " AND (MTTAmendMakerUserID != ? OR trim(MTTAmendMakerUserID) IS NULL) ";
      }
      ppt = new LoggableStatement(con, sqlQuery);
      if ((mttNumber != null) && (!mttNumber.equalsIgnoreCase(""))) {
        ppt.setString(++setValue, mttNumber);
      }
      if (!commonMethods.isNull(loginedUserId)) {
        ppt.setString(++setValue, loginedUserId);
      }
      logger.info("Select Query " + ppt.getQueryString());
      rs = ppt.executeQuery();
      while (rs.next())
      {
        mttVo = new MTTDataVO();
        mttVo.setMttNumberListAddKeyId(commonMethods.getEmptyIfNull(rs.getString("MTTAmend_Key97")).trim());
        mttVo.setMttNumberListAddMttNumber(commonMethods.getEmptyIfNull(rs.getString("MTTNumber")).trim());
        mttVo.setMttNumberListAddMasterReference(commonMethods.getEmptyIfNull(rs.getString("MasterRef")).trim());
        mttVo.setMttNumberListAddEventRef(commonMethods.getEmptyIfNull(rs.getString("EventRef")).trim());
        mttVo.setMttNumberListAddMakerUserId(commonMethods.getEmptyIfNull(rs.getString("MTTAmendMakerUserID")).trim());
        mttVo.setMttNumberListAddMakertmstmp(commonMethods.getEmptyIfNull(rs.getString("MTTAmendMakerTimestamp")).trim());
       
        this.Pendinglist1.add(mttVo);
      }
    }
    catch (SQLException e)
    {
      throwDAOException(e);
    }
    finally
    {
      closeSqlRefferance(rs, ppt, con);
    }
    logger.info("Exiting Method");
    return this.Pendinglist1;
  }
 
  public String amendMTTNumber(String[] chkList, String check, String remarks)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement loggableStatement = null;
    ResultSet rs = null;
    String result = "fail";
    String query = null;
    String sqlQuery1 = null;
    String sqlQuery2 = null;
    int count = 0;
    try
    {
      con = DBConnectionUtility.getConnection();
     
      HttpSession session = ServletActionContext.getRequest().getSession();
     
      String loginedUserId = (String)session.getAttribute("loginedUserName");
      logger.info("loginedUserId IN updateStatus :: " + loginedUserId);
      if ((chkList != null) && (check != null)) {
        for (int i = 0; i < chkList.length; i++)
        {
          String temp = chkList[i];
          String[] a = temp.split(":");
         
          logger.info("check updateStatus 0 :: " + Integer.parseInt(a[0]));
          logger.info("check updateStatus 1 :: " + a[1]);
          logger.info("check updateStatus 2 :: " + a[2]);
          logger.info("check updateStatus 2 :: " + a[3]);
          logger.info("check updateStatus :: " + check);
          if (check.equalsIgnoreCase("Approve"))
          {
            query = "UPDATE MTT_Number_Amend_MakerChecker SET MTT_Amend_MakerChecker_Status ='A',MTTCheckerTimestamp = CURRENT_TIMESTAMP,MTTCheckerUserID=?,MTTREMARKS=?  WHERE MTTNumber_Key97 =?";
           

            loggableStatement = new LoggableStatement(con, query);
            loggableStatement.setString(1, loginedUserId);
            loggableStatement.setString(2, remarks);
            loggableStatement.setInt(3, Integer.parseInt(a[0]));
           
            count = loggableStatement.executeUpdate();
            logger.info("Count MTT_Number_Amend_MakerChecker update :: " + count);
            if (count > 0)
            {
              logger.info("Before extevent update :: " + a[1]);
              sqlQuery1 = "UPDATE EXTEVENT EE SET EE.MTTNUM='', MTTCLOSE='',MTTSTUS='' WHERE EE.KEY29 = '" + a[1] + "' ";
             
              LoggableStatement loggableStatement1 = new LoggableStatement(con, sqlQuery1);
             
              loggableStatement1.executeUpdate();
             
              closeSqlRefferance(null, loggableStatement1, null);
             
              sqlQuery2 = "DELETE FROM MTT_TRACER_STAGING where  TRIM(MTT_REF)='" + a[2] + "' AND TRIM(MASTER_REF)='" + a[3] + "' AND TRIM(BEV_REFNO_PFIX)||lPAD(TRIM(BEV_REFNO_SERL),3,0) ='" + a[4] + "' ";
             
              LoggableStatement loggableStatement2 = new LoggableStatement(con, sqlQuery2);
             
              loggableStatement2.executeUpdate();
             
              closeSqlRefferance(null, loggableStatement2, null);
            }
          }
          else if (check.equalsIgnoreCase("Reject"))
          {
            query = "UPDATE MTT_Number_Amend_MakerChecker SET MTT_Amend_MakerChecker_Status ='R',MTTCheckerTimestamp = CURRENT_TIMESTAMP,MTTCheckerUserID=?, MTTREMARKS=?  WHERE MTTNumber_Key97 =?";
           

            loggableStatement = new LoggableStatement(con, query);
            loggableStatement.setString(1, loginedUserId);
            loggableStatement.setString(2, remarks);
            loggableStatement.setInt(3, Integer.parseInt(a[0]));
            count = loggableStatement.executeUpdate();
          }
        }
      }
      if (count > 0) {
        result = "SUCCESS";
      }
    }
    catch (Exception e)
    {
      logger.info("updateStatus---------------------------Exception-------------" + e);
      e.printStackTrace();
      throwDAOException(e);
    }
    finally
    {
      closeSqlRefferance(rs, loggableStatement, con);
    }
    logger.info("Exiting Method");
    return result;
  }
 
  public int addMTTNumberMasterRef(MTTDataVO mttVo)
    throws DAOException
  {
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    int insertCount = 0;
    try
    {
      HttpSession session = ServletActionContext.getRequest().getSession();
     
      String loginedUserId = (String)session.getAttribute("loginedUserName");
      logger.info("loginedUserId IN updateStatus :: " + loginedUserId);
     
      con = DBConnectionUtility.getConnection();
      String query = "INSERT INTO MTTNumberAmendMasterEventRef (MTTAmend_Key97,MTTNumber,MasterRef,EventRef,MTTAmendMakerCheckerStatus,MTTAmendMakerUserID,MTTAmendMakerTimestamp) \t\tVALUES (MTTNUMBERAMEND_SEQ.NEXTVAL,?,?,?,?,?,CURRENT_TIMESTAMP)";
     
      pst = new LoggableStatement(con, query);
      pst.setString(1, mttVo.getAddMttNumber());
      pst.setString(2, mttVo.getAddMasRefNo());
      pst.setString(3, mttVo.getAddEventRefNo());
      pst.setString(4, "P");
      pst.setString(5, loginedUserId);
      logger.info(" Insert Query " + pst.getQueryString());
      insertCount = pst.executeUpdate();
    }
    catch (Exception exception)
    {
      throwDAOException(exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    return insertCount;
  }
 
  public String checkMasterRef(MTTDataVO mttVo)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement ppt = null;
    ResultSet rs = null;
    String sqlQuery = null;
    CommonMethods commonMethods = null;
    String addMasterRef = null;
    String addEventRefNo = null;
    String mttMasterRefCnt = "0";
    try
    {
      commonMethods = new CommonMethods();
      addMasterRef = commonMethods.getEmptyIfNull(mttVo.getAddMasRefNo()).trim();
      addEventRefNo = commonMethods.getEmptyIfNull(mttVo.getAddEventRefNo()).trim();
     
      con = DBConnectionUtility.getConnection();
     
      sqlQuery = "select COUNT(EE.KEY29) as COUNT from extevent ee, master mas, BASEEVENT be  where MAS.key97=BE.MASTER_KEY and BE.KEY97=EE.EVENT  AND MAS.MASTER_REF='" +
     
        addMasterRef + "' and TRIM(BE.REFNO_PFIX)||lPAD(TRIM(BE.REFNO_SERL),3,0)='" + addEventRefNo + "' ";
     

      ppt = new LoggableStatement(con, sqlQuery);
      logger.info("Get RBI Values from DB " + ppt.getQueryString());
      rs = ppt.executeQuery();
      while (rs.next()) {
        mttMasterRefCnt = commonMethods.getEmptyIfNull(rs.getString("COUNT")).trim();
      }
    }
    catch (SQLException e)
    {
      throwDAOException(e);
    }
    finally
    {
      closeSqlRefferance(rs, ppt, con);
    }
    logger.info("Exiting Method");
    return mttMasterRefCnt;
  }
 
  public String checkMasterRefReportPuchaseSale(MTTDataVO mttVo)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement ppt = null;
    ResultSet rs = null;
    String sqlQuery = null;
    CommonMethods commonMethods = null;
    String addMasterRef = null;
    String addEventRefNo = null;
    String mttMasterRefRepPurCnt = "0";
    try
    {
      commonMethods = new CommonMethods();
      addMasterRef = commonMethods.getEmptyIfNull(mttVo.getAddMasRefNo()).trim();
      addEventRefNo = commonMethods.getEmptyIfNull(mttVo.getAddEventRefNo()).trim();
     
      con = DBConnectionUtility.getConnection();
     
      sqlQuery = "select Count(ee.REPPURSL) as COUNT from extevent ee, master mas, BASEEVENT be  where MAS.key97=BE.MASTER_KEY and BE.KEY97=EE.EVENT  AND MAS.MASTER_REF='" +
     
        addMasterRef + "' and TRIM(BE.REFNO_PFIX)||lPAD(TRIM(BE.REFNO_SERL),3,0)='" + addEventRefNo + "' AND ee.REPPURSL !='YES' ";
     
      ppt = new LoggableStatement(con, sqlQuery);
      logger.info("Get RBI Values from DB " + ppt.getQueryString());
      rs = ppt.executeQuery();
      while (rs.next()) {
        mttMasterRefRepPurCnt = commonMethods.getEmptyIfNull(rs.getString("COUNT")).trim();
      }
    }
    catch (SQLException e)
    {
      throwDAOException(e);
    }
    finally
    {
      closeSqlRefferance(rs, ppt, con);
    }
    logger.info("Exiting Method");
    return mttMasterRefRepPurCnt;
  }
 
  public String checkMasterRefMTTLinked(MTTDataVO mttVo)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement ppt = null;
    ResultSet rs = null;
    String sqlQuery = null;
    CommonMethods commonMethods = null;
    String addMasterRef = null;
    String addEventRefNo = null;
    String mttNumberLinkedCount = "0";
    try
    {
      commonMethods = new CommonMethods();
      addMasterRef = commonMethods.getEmptyIfNull(mttVo.getAddMasRefNo()).trim();
      addEventRefNo = commonMethods.getEmptyIfNull(mttVo.getAddEventRefNo()).trim();
     
      con = DBConnectionUtility.getConnection();
     
      sqlQuery = "select NVL(ee.MTTNUM,0) AS MTTNUM from extevent ee, master mas, BASEEVENT be where MAS.key97=BE.MASTER_KEY and BE.KEY97=EE.EVENT  AND MAS.MASTER_REF='" +
     
        addMasterRef + "' and TRIM(BE.REFNO_PFIX)||lPAD(TRIM(BE.REFNO_SERL),3,0)='" + addEventRefNo + "' ";
     

      ppt = new LoggableStatement(con, sqlQuery);
      logger.info("Get MTT Linked Count " + ppt.getQueryString());
      rs = ppt.executeQuery();
      while (rs.next()) {
        mttNumberLinkedCount = commonMethods.getEmptyIfNull(rs.getString("MTTNUM")).trim();
      }
      logger.info("mttNumberLinkedCount " + mttNumberLinkedCount);
    }
    catch (SQLException e)
    {
      throwDAOException(e);
    }
    finally
    {
      closeSqlRefferance(rs, ppt, con);
    }
    logger.info("Exiting Method");
    return mttNumberLinkedCount;
  }
 
  public String checkMasterRefInProgress(MTTDataVO mttVo)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement ppt = null;
    ResultSet rs = null;
    String sqlQuery = null;
    CommonMethods commonMethods = null;
    String addMasterRef = null;
    String addEventRefNo = null;
    String masterRefPendingCount = "0";
    try
    {
      commonMethods = new CommonMethods();
      addMasterRef = commonMethods.getEmptyIfNull(mttVo.getAddMasRefNo()).trim();
      addEventRefNo = commonMethods.getEmptyIfNull(mttVo.getAddEventRefNo()).trim();
     
      con = DBConnectionUtility.getConnection();
     
      sqlQuery = "SELECT COUNT(*) AS PENDINGCNT FROM MTTNUMBERAMENDMASTEREVENTREF  WHERE TRIM(MASTERREF)='" +
        addMasterRef + "' and TRIM(EVENTREF)='" + addEventRefNo + "' AND MTTAMENDMAKERCHECKERSTATUS='P'";
     

      ppt = new LoggableStatement(con, sqlQuery);
      logger.info("Get Master Pending Count " + ppt.getQueryString());
      rs = ppt.executeQuery();
      while (rs.next()) {
        masterRefPendingCount = commonMethods.getEmptyIfNull(rs.getString("PENDINGCNT")).trim();
      }
      logger.info("masterRefPendingCount :: " + masterRefPendingCount);
    }
    catch (SQLException e)
    {
      throwDAOException(e);
    }
    finally
    {
      closeSqlRefferance(rs, ppt, con);
    }
    logger.info("Exiting Method");
    return masterRefPendingCount;
  }
 
  public String checkMasterRefInSplitProcess(MTTDataVO mttVo)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement ppt = null;
    ResultSet rs = null;
    String sqlQuery = null;
    CommonMethods commonMethods = null;
    String addMasterRef = null;
    String addEventRefNo = null;
    String masterRefSplitCount = "0";
    try
    {
      commonMethods = new CommonMethods();
      addMasterRef = commonMethods.getEmptyIfNull(mttVo.getAddMasRefNo()).trim();
      addEventRefNo = commonMethods.getEmptyIfNull(mttVo.getAddEventRefNo()).trim();
     
      con = DBConnectionUtility.getConnection();
     
      sqlQuery = "SELECT COUNT(*) AS SPLITCNT FROM MTT_NUMBER_SPLIT_MAKERCHECKER  WHERE TRIM(MASTERREF)='" +
        addMasterRef + "' and TRIM(EVENTREF)='" + addEventRefNo + "'";
     
      ppt = new LoggableStatement(con, sqlQuery);
      logger.info("Get Master Split Count " + ppt.getQueryString());
      rs = ppt.executeQuery();
      while (rs.next()) {
        masterRefSplitCount = commonMethods.getEmptyIfNull(rs.getString("SPLITCNT")).trim();
      }
      logger.info("masterRefSplitCount :: " + masterRefSplitCount);
    }
    catch (SQLException e)
    {
      throwDAOException(e);
    }
    finally
    {
      closeSqlRefferance(rs, ppt, con);
    }
    logger.info("Exiting Method");
    return masterRefSplitCount;
  }
 
  public String checkMTTNum(MTTDataVO mttVo)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement ppt = null;
    ResultSet rs = null;
    String sqlQuery = null;
    LoggableStatement ppt1 = null;
    ResultSet rs1 = null;
    CommonMethods commonMethods = null;
    String mttNumber = null;
    String mttNumberCnt = "";
    try
    {
      commonMethods = new CommonMethods();
      mttNumber = commonMethods.getEmptyIfNull(mttVo.getAddMttNumber()).trim();
     

      con = DBConnectionUtility.getConnection();
     

      sqlQuery = "SELECT COUNT(*) AS COUNT FROM EXTEVENT EE WHERE TRIM(ee.MTTNUM)='" + mttNumber + "' ";
     


      ppt1 = new LoggableStatement(con, sqlQuery);
      logger.info("Get RBI Values from DB " + ppt1.getQueryString());
      rs1 = ppt1.executeQuery();
      if (rs1.next()) {
        mttNumberCnt = commonMethods.getEmptyIfNull(rs1.getString("COUNT")).trim();
      }
    }
    catch (SQLException e)
    {
      throwDAOException(e);
    }
    finally
    {
      closeSqlRefferance(rs1, ppt1, null);
      closeSqlRefferance(rs, ppt, con);
    }
    logger.info("Exiting Method");
    return mttNumberCnt;
  }
 
  public String checkMTTSts(MTTDataVO mttVo)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement ppt = null;
    ResultSet rs = null;
    String sqlQuery = null;
    LoggableStatement ppt1 = null;
    ResultSet rs1 = null;
    CommonMethods commonMethods = null;
    String mttNumber = null;
    String mttNumberCnt = "";
    try
    {
      commonMethods = new CommonMethods();
      mttNumber = commonMethods.getEmptyIfNull(mttVo.getAddMttNumber()).trim();
     

      con = DBConnectionUtility.getConnection();
     

      sqlQuery = "SELECT COUNT(*) AS COUNT FROM EXTEVENT EE WHERE TRIM(ee.MTTNUM)='" + mttNumber + "' AND EE.MTTCLOSE='N' ";
     


      ppt1 = new LoggableStatement(con, sqlQuery);
      logger.info("Get RBI Values from DB " + ppt1.getQueryString());
      rs1 = ppt1.executeQuery();
      if (rs1.next()) {
        mttNumberCnt = commonMethods.getEmptyIfNull(rs1.getString("COUNT")).trim();
      }
    }
    catch (SQLException e)
    {
      throwDAOException(e);
    }
    finally
    {
      closeSqlRefferance(rs1, ppt1, null);
      closeSqlRefferance(rs, ppt, con);
    }
    logger.info("Exiting Method");
    return mttNumberCnt;
  }
 
  public String checkMTTCustomer(MTTDataVO mttVo)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement ppt = null;
    ResultSet rs = null;
    String sqlQuery = null;
    LoggableStatement ppt1 = null;
    ResultSet rs1 = null;
    String sqlQuery1 = null;
    CommonMethods commonMethods = null;
    String masterRef = null;
    String mttNumber = null;
    String transactionCustomer = "";
    String mttCustomer = "";
    String mttCustomerCnt = "0";
    try
    {
      commonMethods = new CommonMethods();
      mttNumber = commonMethods.getEmptyIfNull(mttVo.getAddMttNumber()).trim();
      masterRef = commonMethods.getEmptyIfNull(mttVo.getAddMasRefNo()).trim();
     

      con = DBConnectionUtility.getConnection();
     
      sqlQuery = "SELECT CASE WHEN (PROD.CODE79 IN ('ILC','IGT','FRN','ISB')) THEN APP_PTY.CUS_MNM\tWHEN (PROD.CODE79 IN ('ELC','EGT','ESB')) THEN BEN_PTY.CUS_MNM\tWHEN (PROD.CODE79 ='IDC') THEN DRAWEE_PTY.CUS_MNM\tWHEN (PROD.CODE79='ODC') THEN DRAWER_PTY.CUS_MNM\tWHEN (PROD.CODE79 IN ('CPCO','CPBO','CPHO')) THEN REMIT_PTY.CUS_MNM\tWHEN (PROD.CODE79 IN ('CPCI','CPBI','CPHI')) THEN PRT7.CUS_MNM\tWHEN (PROD.CODE79='SHG') THEN PCP_PTY.CUS_MNM\tWHEN (PROD.CODE79 IN ('FSA','FOC','FIL','FIC','FEL')) THEN NPCP_PTY.CUS_MNM\tEND TRANS_CUSTOMER FROM MASTER MAS, EXEMPL30 PROD,PARTYDTLS PCP_PTY,PARTYDTLS APP_PTY,PARTYDTLS DRAWEE_PTY,PARTYDTLS NPCP_PTY, PARTYDTLS BEN_PTY,PARTYDTLS DRAWER_PTY,PARTYDTLS REMIT_PTY,LCMASTER LCM,COLLMASTER COLL,CPAYMASTER CPAY, (SELECT MAS.KEY97 MAS_KEY ,PRT.KEY97 PRT_KEY FROM MASTER MAS,TIDATAITEM TID,PARTYDTLS PRT WHERE MAS.KEY97 = TID.MASTER_KEY AND TID.KEY97   = PRT.KEY97 AND PRT.ROLE    ='BEN') BEN, PARTYDTLS PRT7 WHERE TRIM(MAS.MASTER_REF) = '" +
     











        masterRef + "'" +
        " AND MAS.EXEMPLAR    = PROD.KEY97 " +
        " AND MAS.KEY97       = LCM.KEY97 (+)" +
        " AND MAS.KEY97       = COLL.KEY97 (+)" +
        " AND MAS.KEY97       = CPAY.KEY97(+)" +
        " AND COLL.DRAWEE_PTY = DRAWEE_PTY.KEY97 (+)" +
        " AND COLL.DRAWER_PTY = DRAWER_PTY.KEY97 (+)" +
        " AND MAS.PCP_PTY     = PCP_PTY.KEY97 (+)" +
        " AND MAS.NPCP_PTY    = NPCP_PTY.KEY97 (+)" +
        " AND LCM.APP_PTY     = APP_PTY.KEY97 (+)" +
        " AND LCM.BEN_PTY     = BEN_PTY.KEY97 (+)" +
        " AND CPAY.REMIT_PTY  = REMIT_PTY.KEY97(+)" +
        " AND MAS.KEY97       = BEN.MAS_KEY(+)" +
        " AND BEN.PRT_KEY     = PRT7.KEY97(+)";
     
      ppt = new LoggableStatement(con, sqlQuery);
      logger.info("Transaction Customer Query " + ppt.getQueryString());
      rs = ppt.executeQuery();
      if (rs.next()) {
        transactionCustomer = commonMethods.getEmptyIfNull(rs.getString("TRANS_CUSTOMER")).trim();
      }
      sqlQuery1 = "SELECT CUSTOMER_CODE AS MTT_CUSTOMER FROM ETT_MTT_ENQUIRY EME WHERE EME.MTTNUMBER = '" + mttNumber + "' AND MTT_CLOSE_STATUS='N'";
     
      ppt1 = new LoggableStatement(con, sqlQuery1);
      logger.info("MTT Customer Query " + ppt1.getQueryString());
      rs1 = ppt1.executeQuery();
      if (rs1.next()) {
        mttCustomer = commonMethods.getEmptyIfNull(rs1.getString("MTT_CUSTOMER")).trim();
      }
      if (transactionCustomer.equals(mttCustomer)) {
        mttCustomerCnt = "1";
      }
    }
    catch (SQLException e)
    {
      throwDAOException(e);
    }
    finally
    {
      closeSqlRefferance(rs1, ppt1, null);
      closeSqlRefferance(rs, ppt, con);
    }
    logger.info("Exiting Method");
    return mttCustomerCnt;
  }
 
  public String amendAddMTTNumberMasterRef(String[] chkList1, String check1, String remarks1)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement loggableStatement = null;
    LoggableStatement ppt1 = null;
    ResultSet rs = null;
    ResultSet rs1 = null;
    String result = "fail";
    String query = null;
    String sqlQuery = null;
    String sqlQuery1 = null;
    int count = 0;
    CommonMethods commonMethods = null;
    try
    {
      commonMethods = new CommonMethods();
     
      con = DBConnectionUtility.getConnection();
     
      HttpSession session = ServletActionContext.getRequest().getSession();
     
      String loginedUserId = (String)session.getAttribute("loginedUserName");
      logger.info("loginedUserId IN updateStatus :: " + loginedUserId);
      if ((chkList1 != null) && (check1 != null)) {
        for (int i = 0; i < chkList1.length; i++)
        {
          String temp = chkList1[i];
          String[] a = temp.split(":");
         
          logger.info("check updateStatus 0 :: " + Integer.parseInt(a[0]));
          logger.info("check updateStatus 1 :: " + a[1]);
          logger.info("check updateStatus 2 :: " + a[2]);
          logger.info("check updateStatus 2 :: " + a[3]);
          logger.info("check updateStatus :: " + check1);
          if (check1.equalsIgnoreCase("Approve"))
          {
            query = "UPDATE MTTNumberAmendMasterEventRef SET MTTAMENDMAKERCHECKERSTATUS ='A',MTTAMENDCHECKERTIMESTAMP = CURRENT_TIMESTAMP,MTTAMENDCHECKERUSERID=?,MTTREMARKS=?  WHERE MTTAMEND_KEY97 =?";
           

            loggableStatement = new LoggableStatement(con, query);
            loggableStatement.setString(1, loginedUserId);
            loggableStatement.setString(2, remarks1);
            loggableStatement.setInt(3, Integer.parseInt(a[0]));
           
            count = loggableStatement.executeUpdate();
            if (count > 0)
            {
              logger.info("Before extevent update :: " + a[1]);
             
              sqlQuery = "select BE.KEY97 AS BASE_KEY,MAS.KEY97 AS MASTER_KEY,EE.KEY29 AS KEY97 from extevent ee, master mas, BASEEVENT be WHERE MAS.key97=BE.MASTER_KEY and BE.KEY97=EE.EVENT AND TRIM(MAS.MASTER_REF)='" +
                a[2] + "' AND TRIM(BE.REFNO_PFIX)||lPAD(TRIM(BE.REFNO_SERL),3,0) = '" + a[3] + "'";
             
              ppt1 = new LoggableStatement(con, sqlQuery);
              logger.info("Select Query for Key97 :: " + ppt1.getQueryString());
              rs1 = ppt1.executeQuery();
              if (rs1.next())
              {
                String ExtEventKey97 = commonMethods.getEmptyIfNull(rs1.getString("KEY97")).trim();
                String BaseEventKey97 = commonMethods.getEmptyIfNull(rs1.getString("BASE_KEY")).trim();
                String MasterKey97 = commonMethods.getEmptyIfNull(rs1.getString("MASTER_KEY")).trim();
               
                sqlQuery1 = "UPDATE EXTEVENT EE SET EE.MTTNUM='" + Integer.parseInt(a[1]) + "', MTTCLOSE='N',MTTSTUS='E' WHERE EE.KEY29 = '" + ExtEventKey97 + "' ";
               
                LoggableStatement loggableStatement1 = new LoggableStatement(con, sqlQuery1);
               
                loggableStatement1.executeUpdate();
               
                closeSqlRefferance(null, loggableStatement1, null);
               
                String insertDataToStaging = "{call TRACER_PROCEDURE(?,?)}";
               
                CallableStatement callableStatement = con.prepareCall(insertDataToStaging);
                callableStatement.setString(1, BaseEventKey97);
                callableStatement.setString(2, MasterKey97);
                callableStatement.executeUpdate();
                if (callableStatement != null) {
                  callableStatement.close();
                }
              }
              closeSqlRefferance(rs1, ppt1, null);
            }
          }
          else if (check1.equalsIgnoreCase("Reject"))
          {
            query = "UPDATE MTTNumberAmendMasterEventRef SET MTTAMENDMAKERCHECKERSTATUS ='R',MTTAMENDCHECKERTIMESTAMP = CURRENT_TIMESTAMP,MTTAMENDCHECKERUSERID=?,MTTREMARKS=?  WHERE MTTAMEND_KEY97 =?";
           

            loggableStatement = new LoggableStatement(con, query);
            loggableStatement.setString(1, loginedUserId);
            loggableStatement.setString(2, remarks1);
            loggableStatement.setInt(3, Integer.parseInt(a[0]));
            count = loggableStatement.executeUpdate();
          }
        }
      }
      if (count > 0) {
        result = "SUCCESS";
      }
    }
    catch (Exception e)
    {
      logger.info("updateStatus---------------------------Exception-------------" + e);
      e.printStackTrace();
      throwDAOException(e);
    }
    finally
    {
      closeSqlRefferance(rs, loggableStatement, con);
    }
    logger.info("Exiting Method");
    return result;
  }
 
  public String checkMTTNum1(MTTDataVO mttVo, String mttListNumberToCheck)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement ppt1 = null;
    ResultSet rs1 = null;
    String sqlQuery1 = null;
    CommonMethods commonMethods = null;
    String mttNumber = null;
    String mttNumberCnt = "";
    try
    {
      commonMethods = new CommonMethods();
      mttNumber = commonMethods.getEmptyIfNull(mttListNumberToCheck).trim();
     
      con = DBConnectionUtility.getConnection();
     

      sqlQuery1 = "SELECT COUNT(*) AS COUNT FROM EXTEVENT EE WHERE TRIM(ee.MTTNUM)='" + mttNumber + "' ";
      ppt1 = new LoggableStatement(con, sqlQuery1);
      logger.info("Get RBI Values from DB " + ppt1.getQueryString());
      rs1 = ppt1.executeQuery();
      if (rs1.next()) {
        mttNumberCnt = commonMethods.getEmptyIfNull(rs1.getString("COUNT")).trim();
      }
    }
    catch (SQLException e)
    {
      throwDAOException(e);
    }
    finally
    {
      closeSqlRefferance(rs1, ppt1, con);
    }
    logger.info("Exiting Method");
    return mttNumberCnt;
  }
 
  public String checkMTTSts(MTTDataVO mttVo, String mttListNumberToCheck)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    String sqlQuery = null;
    LoggableStatement ppt1 = null;
    ResultSet rs1 = null;
    CommonMethods commonMethods = null;
    String mttNumber = null;
    String mttNumberCnt = "";
    try
    {
      commonMethods = new CommonMethods();
      mttNumber = commonMethods.getEmptyIfNull(mttListNumberToCheck).trim();
     

      con = DBConnectionUtility.getConnection();
     

      sqlQuery = "SELECT COUNT(*) AS COUNT FROM EXTEVENT EE WHERE TRIM(ee.MTTNUM)='" + mttNumber + "' AND EE.MTTCLOSE='N' ";
     


      ppt1 = new LoggableStatement(con, sqlQuery);
      logger.info("Get RBI Values from DB " + ppt1.getQueryString());
      rs1 = ppt1.executeQuery();
      if (rs1.next()) {
        mttNumberCnt = commonMethods.getEmptyIfNull(rs1.getString("COUNT")).trim();
      }
    }
    catch (SQLException e)
    {
      throwDAOException(e);
    }
    finally
    {
      closeSqlRefferance(rs1, ppt1, con);
    }
    logger.info("Exiting Method");
    return mttNumberCnt;
  }
 
  public String checkMTTCustomer1(MTTDataVO mttVo, String mttListNumberToCheck)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement ppt = null;
    ResultSet rs = null;
    String sqlQuery = null;
    LoggableStatement ppt1 = null;
    ResultSet rs1 = null;
    String sqlQuery1 = null;
    CommonMethods commonMethods = null;
    String masterRef = null;
    String mttNumber = null;
    String transactionCustomer = "";
    String mttCustomer = "";
    String mttCustomerCnt = "0";
    try
    {
      commonMethods = new CommonMethods();
      mttNumber = commonMethods.getEmptyIfNull(mttListNumberToCheck).trim();
      masterRef = commonMethods.getEmptyIfNull(mttVo.getMttSplitMasRef()).trim();
     

      con = DBConnectionUtility.getConnection();
     
      sqlQuery = "SELECT CASE WHEN (PROD.CODE79 IN ('ILC','IGT','FRN','ISB')) THEN APP_PTY.CUS_MNM\tWHEN (PROD.CODE79 IN ('ELC','EGT','ESB')) THEN BEN_PTY.CUS_MNM\tWHEN (PROD.CODE79 ='IDC') THEN DRAWEE_PTY.CUS_MNM\tWHEN (PROD.CODE79='ODC') THEN DRAWER_PTY.CUS_MNM\tWHEN (PROD.CODE79 IN ('CPCO','CPBO','CPHO')) THEN REMIT_PTY.CUS_MNM\tWHEN (PROD.CODE79 IN ('CPCI','CPBI','CPHI')) THEN PRT7.CUS_MNM\tWHEN (PROD.CODE79='SHG') THEN PCP_PTY.CUS_MNM\tWHEN (PROD.CODE79 IN ('FSA','FOC','FIL','FIC','FEL')) THEN NPCP_PTY.CUS_MNM\tEND TRANS_CUSTOMER FROM MASTER MAS, EXEMPL30 PROD,PARTYDTLS PCP_PTY,PARTYDTLS APP_PTY,PARTYDTLS DRAWEE_PTY,PARTYDTLS NPCP_PTY, PARTYDTLS BEN_PTY,PARTYDTLS DRAWER_PTY,PARTYDTLS REMIT_PTY,LCMASTER LCM,COLLMASTER COLL,CPAYMASTER CPAY, (SELECT MAS.KEY97 MAS_KEY ,PRT.KEY97 PRT_KEY FROM MASTER MAS,TIDATAITEM TID,PARTYDTLS PRT WHERE MAS.KEY97 = TID.MASTER_KEY AND TID.KEY97   = PRT.KEY97 AND PRT.ROLE    ='BEN') BEN, PARTYDTLS PRT7 WHERE TRIM(MAS.MASTER_REF) = '" +
     











        masterRef + "'" +
        " AND MAS.EXEMPLAR    = PROD.KEY97 " +
        " AND MAS.KEY97       = LCM.KEY97 (+)" +
        " AND MAS.KEY97       = COLL.KEY97 (+)" +
        " AND MAS.KEY97       = CPAY.KEY97(+)" +
        " AND COLL.DRAWEE_PTY = DRAWEE_PTY.KEY97 (+)" +
        " AND COLL.DRAWER_PTY = DRAWER_PTY.KEY97 (+)" +
        " AND MAS.PCP_PTY     = PCP_PTY.KEY97 (+)" +
        " AND MAS.NPCP_PTY    = NPCP_PTY.KEY97 (+)" +
        " AND LCM.APP_PTY     = APP_PTY.KEY97 (+)" +
        " AND LCM.BEN_PTY     = BEN_PTY.KEY97 (+)" +
        " AND CPAY.REMIT_PTY  = REMIT_PTY.KEY97(+)" +
        " AND MAS.KEY97       = BEN.MAS_KEY(+)" +
        " AND BEN.PRT_KEY     = PRT7.KEY97(+)";
     
      ppt = new LoggableStatement(con, sqlQuery);
      logger.info("Transaction Customer Query " + ppt.getQueryString());
      rs = ppt.executeQuery();
      if (rs.next()) {
        transactionCustomer = commonMethods.getEmptyIfNull(rs.getString("TRANS_CUSTOMER")).trim();
      }
      sqlQuery1 = "SELECT CUSTOMER_CODE AS MTT_CUSTOMER FROM ETT_MTT_ENQUIRY EME WHERE EME.MTTNUMBER = '" + mttNumber + "' AND MTT_CLOSE_STATUS='N'";
     
      ppt1 = new LoggableStatement(con, sqlQuery1);
      logger.info("MTT Customer Query " + ppt1.getQueryString());
      rs1 = ppt1.executeQuery();
      if (rs1.next()) {
        mttCustomer = commonMethods.getEmptyIfNull(rs1.getString("MTT_CUSTOMER")).trim();
      }
      if (transactionCustomer.equals(mttCustomer)) {
        mttCustomerCnt = "1";
      }
    }
    catch (SQLException e)
    {
      throwDAOException(e);
    }
    finally
    {
      closeSqlRefferance(rs1, ppt1, con);
    }
    logger.info("Exiting Method");
    return mttCustomerCnt;
  }
 
  public void getMTTNumberOnMasterRef(MTTDataVO mttVo)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement ppt = null;
    ResultSet rs = null;
    LoggableStatement pptRecordCheck = null;
    ResultSet rsRecordCheck = null;
    LoggableStatement pptSplitRecordCheck = null;
    LoggableStatement pptAmendRecordCheck = null;
    LoggableStatement pptRecordCheckREPPURSL = null;
    ResultSet rsSplitRecordCheck = null;
    ResultSet rsRecordCheckREPPURSL = null;
    ResultSet rsAmendRecordCheck = null;
    String sqlQuery = null;
    String sqlQueryRecordCheck = null;
    String sqlQueryRecordCheckREPPURSL = null;
    String sqlQuerySplitRecordCheck = null;
    String sqlQueryAmendRecordCheck = null;
    CommonMethods commonMethods = null;
    LoggableStatement ppt1 = null;
    ResultSet rs1 = null;
    String sqlQuery1 = null;
    String masterRefNo = null;
    String eventRefNo = null;
    try
    {
      commonMethods = new CommonMethods();
      masterRefNo = commonMethods.getEmptyIfNull(mttVo.getMttSplitMasRef()).trim();
      eventRefNo = commonMethods.getEmptyIfNull(mttVo.getMttSplitEveRef()).trim();
     
      logger.info("Master_Ref_No---->" + masterRefNo);
      logger.info("Event_Ref_No---->" + eventRefNo);
      con = DBConnectionUtility.getConnection();
     
      sqlQueryRecordCheck = "select count(ee.MTTNUM) AS CNT from extevent ee, master mas, BASEEVENT be where MAS.key97=BE.MASTER_KEY and BE.KEY97=EE.EVENT ";
      if ((masterRefNo != null) && (!masterRefNo.equalsIgnoreCase("")))
      {
        sqlQueryRecordCheck = sqlQueryRecordCheck + " AND TRIM(MAS.MASTER_REF) = '" + masterRefNo + "' ";
       
        mttVo.setMttSplitMasRef(masterRefNo);
      }
      if ((eventRefNo != null) && (!eventRefNo.equalsIgnoreCase("")))
      {
        sqlQueryRecordCheck = sqlQueryRecordCheck + " AND TRIM(BE.REFNO_PFIX)||lPAD(TRIM(BE.REFNO_SERL),3,0) = '" + eventRefNo + "' ";
       
        mttVo.setMttSplitEveRef(eventRefNo);
      }
      pptRecordCheck = new LoggableStatement(con, sqlQueryRecordCheck);
      logger.info("Check Record is in ExtEvent " + pptRecordCheck.getQueryString());
      rsRecordCheck = pptRecordCheck.executeQuery();
      int count = 0;
      while (rsRecordCheck.next()) {
        count = Integer.parseInt(rsRecordCheck.getString("CNT"));
      }
      sqlQueryRecordCheckREPPURSL = "select count(*) AS CNT from extevent ee, master mas, BASEEVENT be where MAS.key97=BE.MASTER_KEY and BE.KEY97=EE.EVENT ";
      if ((masterRefNo != null) && (!masterRefNo.equalsIgnoreCase("")))
      {
        sqlQueryRecordCheckREPPURSL = sqlQueryRecordCheckREPPURSL + " AND TRIM(MAS.MASTER_REF) = '" + masterRefNo + "' ";
       
        mttVo.setMttSplitMasRef(masterRefNo);
      }
      if ((eventRefNo != null) && (!eventRefNo.equalsIgnoreCase("")))
      {
        sqlQueryRecordCheckREPPURSL = sqlQueryRecordCheckREPPURSL + " AND TRIM(BE.REFNO_PFIX)||lPAD(TRIM(BE.REFNO_SERL),3,0) = '" + eventRefNo + "' ";
       
        mttVo.setMttSplitEveRef(eventRefNo);
      }
      sqlQueryRecordCheckREPPURSL = sqlQueryRecordCheckREPPURSL + " AND ee.REPPURSL = 'YES' ";
     
      pptRecordCheckREPPURSL = new LoggableStatement(con, sqlQueryRecordCheckREPPURSL);
      logger.info("Check Record is in ExtEvent " + pptRecordCheckREPPURSL.getQueryString());
      rsRecordCheckREPPURSL = pptRecordCheckREPPURSL.executeQuery();
      int count0 = 0;
      while (rsRecordCheckREPPURSL.next()) {
        count0 = Integer.parseInt(rsRecordCheckREPPURSL.getString("CNT"));
      }
      sqlQuerySplitRecordCheck = "SELECT COUNT(*) AS CNT FROM MTT_NUMBER_SPLIT_MAKERCHECKER WHERE ISOVERRIDDEN='N' AND  MTT_AMEND_MAKERCHECKER_STATUS ='P' ";
      if ((masterRefNo != null) && (!masterRefNo.equalsIgnoreCase("")))
      {
        sqlQuerySplitRecordCheck = sqlQuerySplitRecordCheck + " AND TRIM(MASTERREF) = '" + masterRefNo + "' ";
       
        mttVo.setMttSplitMasRef(masterRefNo);
      }
      if ((eventRefNo != null) && (!eventRefNo.equalsIgnoreCase("")))
      {
        sqlQuerySplitRecordCheck = sqlQuerySplitRecordCheck + " AND TRIM(EVENTREF) = '" + eventRefNo + "' ";
       
        mttVo.setMttSplitEveRef(eventRefNo);
      }
      pptSplitRecordCheck = new LoggableStatement(con, sqlQuerySplitRecordCheck);
      logger.info("Check Record is in MTT_NUMBER_SPLIT_MAKERCHECKER " + pptSplitRecordCheck.getQueryString());
      rsSplitRecordCheck = pptSplitRecordCheck.executeQuery();
      int count1 = 0;
      while (rsSplitRecordCheck.next()) {
        count1 = Integer.parseInt(rsSplitRecordCheck.getString("CNT"));
      }
      sqlQueryAmendRecordCheck = "SELECT COUNT(*) AS CNT FROM MTTNUMBERAMENDMASTEREVENTREF WHERE MTTAMENDMAKERCHECKERSTATUS ='P' ";
      if ((masterRefNo != null) && (!masterRefNo.equalsIgnoreCase("")))
      {
        sqlQueryAmendRecordCheck = sqlQueryAmendRecordCheck + " AND TRIM(MASTERREF) = '" + masterRefNo + "' ";
       
        mttVo.setMttSplitMasRef(masterRefNo);
      }
      if ((eventRefNo != null) && (!eventRefNo.equalsIgnoreCase("")))
      {
        sqlQueryAmendRecordCheck = sqlQueryAmendRecordCheck + " AND TRIM(EVENTREF) = '" + eventRefNo + "' ";
       
        mttVo.setMttSplitEveRef(eventRefNo);
      }
      pptAmendRecordCheck = new LoggableStatement(con, sqlQueryAmendRecordCheck);
      logger.info("Check Record is in MTTNUMBERAMENDMASTEREVENTREF " + pptAmendRecordCheck.getQueryString());
      rsAmendRecordCheck = pptAmendRecordCheck.executeQuery();
      int count2 = 0;
      while (rsAmendRecordCheck.next()) {
        count2 = Integer.parseInt(rsAmendRecordCheck.getString("CNT"));
      }
      logger.info("count " + count);
      logger.info("count0 " + count0);
      logger.info("count1 " + count1);
      logger.info("count2 " + count2);
      if (count > 0)
      {
        mttVo.setMttSplitDeleteFlag("true");
        return;
      }
      if (count0 == 0)
      {
        mttVo.setMttSplitREPPurchaseFlag("true");
        return;
      }
      if (count1 > 0)
      {
        mttVo.setMttSplitPendingFlag("true");
        return;
      }
      if (count2 > 0)
      {
        mttVo.setMttAmendPendingFlag("true");
        return;
      }
      sqlQuery1 = "SELECT CASE WHEN PRD.CODE79 IN ('ILC','IDC','IGT','CPCO','CPBO') THEN REP_GET_CN_CREDIT_AMT(MAS.KEY97,BE.KEY97) ELSE REP_GET_CN_DEBIT_AMT(MAS.KEY97,BE.KEY97) END AS AMOUNT FROM MASTER MAS, BASEEVENT be,EXEMPL30 PRD WHERE MAS.key97=BE.MASTER_KEY AND MAS.EXEMPLAR = PRD.KEY97 ";
      if ((masterRefNo != null) && (!masterRefNo.equalsIgnoreCase("")))
      {
        sqlQuery1 = sqlQuery1 + " AND TRIM(MAS.MASTER_REF) = '" + masterRefNo + "' ";
       
        mttVo.setMttSplitMasRef(masterRefNo);
      }
      if ((eventRefNo != null) && (!eventRefNo.equalsIgnoreCase("")))
      {
        sqlQuery1 = sqlQuery1 + " AND TRIM(BE.REFNO_PFIX)||lPAD(TRIM(BE.REFNO_SERL),3,0) = '" + eventRefNo + "' ";
       
        mttVo.setMttSplitEveRef(eventRefNo);
      }
      ppt1 = new LoggableStatement(con, sqlQuery1);
     
      logger.info("Get Amount From BaseEvent " + ppt1.getQueryString());
      rs1 = ppt1.executeQuery();
      while (rs1.next()) {
        mttVo.setMttListTransAmt0(commonMethods.getZeroIfNull(rs1.getString("AMOUNT")).trim());
      }
      sqlQuery = "SELECT TRIM(MTTNUMBER) AS MTTNUMBER,TRIM(MTTAMOUNT) AS AMT from MTT_NUMBER_SPLIT_MAKERCHECKER WHERE ISOVERRIDDEN='N' AND  MTT_AMEND_MAKERCHECKER_STATUS !='P' ";
      if ((masterRefNo != null) && (!masterRefNo.equalsIgnoreCase("")))
      {
        sqlQuery = sqlQuery + " AND TRIM(MASTERREF) = '" + masterRefNo + "' ";
       
        mttVo.setMttSplitMasRef(masterRefNo);
      }
      if ((eventRefNo != null) && (!eventRefNo.equalsIgnoreCase("")))
      {
        sqlQuery = sqlQuery + " AND TRIM(EVENTREF) = '" + eventRefNo + "' ";
       
        mttVo.setMttSplitEveRef(eventRefNo);
      }
      ppt = new LoggableStatement(con, sqlQuery);
     
      logger.info("Get Data From MTT_NUMBER_SPLIT_MAKERCHECKER " + ppt.getQueryString());
      rs = ppt.executeQuery();
     
      int cnt = 0;
      while (rs.next())
      {
        cnt++;
        logger.info("cnt " + cnt);
        switch (cnt)
        {
        case 1:
          logger.info("Inside 1st Case ");
          mttVo.setMttListNumber1(commonMethods.getZeroIfNull(rs.getString("MTTNUMBER")).trim());
          mttVo.setMttListTransAmt1(commonMethods.getZeroIfNull(rs.getString("AMT")).trim());
          break;
        case 2:
          logger.info("Inside 2nd Case ");
          mttVo.setMttListNumber2(commonMethods.getZeroIfNull(rs.getString("MTTNUMBER")).trim());
          mttVo.setMttListTransAmt2(commonMethods.getZeroIfNull(rs.getString("AMT")).trim());
          break;
        case 3:
          logger.info("Inside 3rd Case ");
          mttVo.setMttListNumber3(commonMethods.getZeroIfNull(rs.getString("MTTNUMBER")).trim());
          mttVo.setMttListTransAmt3(commonMethods.getZeroIfNull(rs.getString("AMT")).trim());
          break;
        case 4:
          logger.info("Inside 4th Case ");
          mttVo.setMttListNumber4(commonMethods.getZeroIfNull(rs.getString("MTTNUMBER")).trim());
          mttVo.setMttListTransAmt4(commonMethods.getZeroIfNull(rs.getString("AMT")).trim());
          break;
        case 5:
          logger.info("Inside 5th Case ");
          mttVo.setMttListNumber5(commonMethods.getZeroIfNull(rs.getString("MTTNUMBER")).trim());
          mttVo.setMttListTransAmt5(commonMethods.getZeroIfNull(rs.getString("AMT")).trim());
          break;
        case 6:
          logger.info("Inside 6th Case ");
          mttVo.setMttListNumber6(commonMethods.getZeroIfNull(rs.getString("MTTNUMBER")).trim());
          mttVo.setMttListTransAmt6(commonMethods.getZeroIfNull(rs.getString("AMT")).trim());
          break;
        case 7:
          logger.info("Inside 7th Case ");
          mttVo.setMttListNumber7(commonMethods.getZeroIfNull(rs.getString("MTTNUMBER")).trim());
          mttVo.setMttListTransAmt7(commonMethods.getZeroIfNull(rs.getString("AMT")).trim());
          break;
        case 8:
          logger.info("Inside 8th Case ");
          mttVo.setMttListNumber8(commonMethods.getZeroIfNull(rs.getString("MTTNUMBER")).trim());
          mttVo.setMttListTransAmt8(commonMethods.getZeroIfNull(rs.getString("AMT")).trim());
          break;
        case 9:
          logger.info("Inside 9th Case ");
          mttVo.setMttListNumber9(commonMethods.getZeroIfNull(rs.getString("MTTNUMBER")).trim());
          mttVo.setMttListTransAmt9(commonMethods.getZeroIfNull(rs.getString("AMT")).trim());
          break;
        case 10:
          logger.info("Inside 10th Case ");
          mttVo.setMttListNumber10(commonMethods.getZeroIfNull(rs.getString("MTTNUMBER")).trim());
          mttVo.setMttListTransAmt10(commonMethods.getZeroIfNull(rs.getString("AMT")).trim());
        }
      }
    }
    catch (SQLException e)
    {
      throwDAOException(e);
    }
    finally
    {
      closeSqlRefferance(rsSplitRecordCheck, pptSplitRecordCheck, null);
      closeSqlRefferance(rsRecordCheckREPPURSL, pptRecordCheckREPPURSL, null);
      closeSqlRefferance(rsRecordCheck, pptRecordCheck, null);
      closeSqlRefferance(rs1, ppt1, null);
      closeSqlRefferance(rs, ppt, con);
    }
    logger.info("Exiting Method");
  }
 
  public int submitMTTNumbeSplit(MTTDataVO mttVo, String splitRemarks, String mttNumber1, String mttAmount1, String mttNumber2, String mttAmount2, String mttNumber3, String mttAmount3, String mttNumber4, String mttAmount4, String mttNumber5, String mttAmount5, String mttNumber6, String mttAmount6, String mttNumber7, String mttAmount7, String mttNumber8, String mttAmount8, String mttNumber9, String mttAmount9, String mttNumber10, String mttAmount10)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement ppt = null;
    String sqlQuery = null;
    LoggableStatement ppt1 = null;
    LoggableStatement pptKey97 = null;
    LoggableStatement loggableStatement = null;
   
    ResultSet rs1 = null;
    ResultSet rsKey97 = null;
    String sqlQuery1 = null;
    String sqlQueryKey97 = null;
    CommonMethods commonMethods = null;
    String masterRefNo = null;
    String eventRefNo = null;
   

    String mttRemark = null;
    int updateCnt = 0;
    String BaseEventKey97 = "";
    String MasterKey97 = "";
    try
    {
      HttpSession session = ServletActionContext.getRequest().getSession();
      commonMethods = new CommonMethods();
      masterRefNo = commonMethods.getEmptyIfNull(mttVo.getMttSplitMasRef()).trim();
      logger.info("masterRefNo IN submitMTTNumbeSplit :: " + masterRefNo);
      eventRefNo = commonMethods.getEmptyIfNull(mttVo.getMttSplitEveRef()).trim();
      logger.info("eventRefNo IN submitMTTNumbeSplit :: " + eventRefNo);
      mttRemark = commonMethods.getEmptyIfNull(splitRemarks).trim();
      logger.info("mttRemark IN submitMTTNumbeSplit :: " + mttRemark);
      String loginedUserId = (String)session.getAttribute("loginedUserId");
      logger.info("loginedUserId IN submitMTTNumbeSplit :: " + loginedUserId);
     
      con = DBConnectionUtility.getConnection();
     
      int cnt = 0;
      sqlQuery1 = "SELECT COUNT(*) AS CNT from MTT_NUMBER_SPLIT_MAKERCHECKER WHERE ISOVERRIDDEN='N' AND  MTT_AMEND_MAKERCHECKER_STATUS !='P' ";
      if ((masterRefNo != null) && (!masterRefNo.equalsIgnoreCase("")))
      {
        sqlQuery1 = sqlQuery1 + " AND TRIM(MASTERREF) = '" + masterRefNo + "' ";
       
        mttVo.setMttSplitMasRef(masterRefNo);
      }
      if ((eventRefNo != null) && (!eventRefNo.equalsIgnoreCase("")))
      {
        sqlQuery1 = sqlQuery1 + " AND TRIM(EVENTREF) = '" + eventRefNo + "' ";
       
        mttVo.setMttSplitEveRef(eventRefNo);
      }
      ppt1 = new LoggableStatement(con, sqlQuery1);
     
      logger.info("Get Data From MTT_NUMBER_SPLIT_MAKERCHECKER " + ppt1.getQueryString());
      rs1 = ppt1.executeQuery();
      if (rs1.next()) {
        cnt = Integer.parseInt(rs1.getString("CNT"));
      }
      if (cnt > 0)
      {
        String query = "UPDATE MTT_NUMBER_SPLIT_MAKERCHECKER SET ISOVERRIDDEN ='Y' WHERE TRIM(MASTERREF) =? AND TRIM(EVENTREF)=?";
       
        loggableStatement = new LoggableStatement(con, query);
        loggableStatement.setString(1, masterRefNo);
        loggableStatement.setString(2, eventRefNo);
       
        loggableStatement.executeUpdate();
      }
      sqlQueryKey97 =
        "select BE.KEY97 AS BASE_KEY,MAS.KEY97 AS MASTER_KEY,EE.KEY29 AS KEY97 from extevent ee, master mas, BASEEVENT be WHERE MAS.key97=BE.MASTER_KEY and BE.KEY97=EE.EVENT AND TRIM(MAS.MASTER_REF)='" + masterRefNo + "' AND TRIM(BE.REFNO_PFIX)||lPAD(TRIM(BE.REFNO_SERL),3,0) = '" + eventRefNo + "'";
     
      pptKey97 = new LoggableStatement(con, sqlQueryKey97);
      logger.info("Select Query for Key97 :: " + pptKey97.getQueryString());
      rsKey97 = pptKey97.executeQuery();
      if (rsKey97.next())
      {
        BaseEventKey97 = commonMethods.getEmptyIfNull(rsKey97.getString("BASE_KEY")).trim();
        MasterKey97 = commonMethods.getEmptyIfNull(rsKey97.getString("MASTER_KEY")).trim();
      }
      sqlQuery = "INSERT INTO MTT_NUMBER_SPLIT_MAKERCHECKER (MTTNUMBERSPLIT_KEY97,MASTERREF,EVENTREF,MTTNUMBER,MTTAMOUNT,MTTREMARKS, MTT_AMEND_MAKERCHECKER_STATUS,MTTMAKERUSERID,MTTMAKERTIMESTAMP,ISOVERRIDDEN) \tVALUES (?,?,?,?,?,?, ?,?,CURRENT_TIMESTAMP,?)";
     




      ppt = new LoggableStatement(con, sqlQuery);
      if (!commonMethods.isNull(mttNumber1))
      {
        int updateCnt1 = 0;
        String mttSplitSequence = getNextMTTSplitNumSeq();
        ppt.setString(1, mttSplitSequence);
        ppt.setString(2, masterRefNo);
        ppt.setString(3, eventRefNo);
        ppt.setString(4, mttNumber1);
        ppt.setString(5, mttAmount1);
        ppt.setString(6, mttRemark);
        ppt.setString(7, "P");
        ppt.setString(8, loginedUserId);
        ppt.setString(9, "N");
        logger.info(" Insert Query 1: " + ppt.getQueryString());
        updateCnt1 = ppt.executeUpdate();
        logger.info(" updateCnt1 1: " + updateCnt1);
        if (updateCnt1 > 0)
        {
          logger.info("BaseEventKey97 :: " + BaseEventKey97 + " :: MasterKey97 :: " + MasterKey97 + " :: mttSplitSequence :: " + mttSplitSequence);
          String insertDataToStaging = "{call TRACER_PROCEDURE_MTT_SPLIT(?,?,?)}";
          CallableStatement callableStatement = null;
          try
          {
            callableStatement = con.prepareCall(insertDataToStaging);
            callableStatement.setString(1, BaseEventKey97);
            callableStatement.setString(2, MasterKey97);
            callableStatement.setString(3, mttSplitSequence);
            callableStatement.executeUpdate();
          }
          catch (SQLException e)
          {
            e.printStackTrace();
          }
          finally
          {
            closeCallableStatement(callableStatement);
          }
        }
      }
      if (!commonMethods.isNull(mttNumber2))
      {
        int updateCnt2 = 0;
        String mttSplitSequence = getNextMTTSplitNumSeq();
        ppt.setString(1, mttSplitSequence);
        ppt.setString(2, masterRefNo);
        ppt.setString(3, eventRefNo);
        ppt.setString(4, mttNumber2);
        ppt.setString(5, mttAmount2);
        ppt.setString(6, mttRemark);
        ppt.setString(7, "P");
        ppt.setString(8, loginedUserId);
        ppt.setString(9, "N");
        logger.info(" Insert Query 2: " + ppt.getQueryString());
        updateCnt2 = ppt.executeUpdate();
        if (updateCnt2 > 0)
        {
          String insertDataToStaging = "{call TRACER_PROCEDURE_MTT_SPLIT(?,?,?)}";
          CallableStatement callableStatement = null;
          try
          {
            callableStatement = con.prepareCall(insertDataToStaging);
            callableStatement.setString(1, BaseEventKey97);
            callableStatement.setString(2, MasterKey97);
            callableStatement.setString(3, mttSplitSequence);
            callableStatement.executeUpdate();
          }
          catch (SQLException e)
          {
            e.printStackTrace();
          }
          finally
          {
            closeCallableStatement(callableStatement);
          }
        }
      }
      if (!commonMethods.isNull(mttNumber3))
      {
        int updateCnt3 = 0;
        String mttSplitSequence = getNextMTTSplitNumSeq();
        ppt.setString(1, mttSplitSequence);
        ppt.setString(2, masterRefNo);
        ppt.setString(3, eventRefNo);
        ppt.setString(4, mttNumber3);
        ppt.setString(5, mttAmount3);
        ppt.setString(6, mttRemark);
        ppt.setString(7, "P");
        ppt.setString(8, loginedUserId);
        ppt.setString(9, "N");
        logger.info(" Insert Query 3: " + ppt.getQueryString());
        updateCnt3 = ppt.executeUpdate();
        if (updateCnt3 > 0)
        {
          String insertDataToStaging = "{call TRACER_PROCEDURE_MTT_SPLIT(?,?,?)}";
          CallableStatement callableStatement = null;
          try
          {
            callableStatement = con.prepareCall(insertDataToStaging);
            callableStatement.setString(1, BaseEventKey97);
            callableStatement.setString(2, MasterKey97);
            callableStatement.setString(3, mttSplitSequence);
            callableStatement.executeUpdate();
          }
          catch (SQLException e)
          {
            e.printStackTrace();
          }
          finally
          {
            closeCallableStatement(callableStatement);
          }
        }
      }
      if (!commonMethods.isNull(mttNumber4))
      {
        int updateCnt4 = 0;
        String mttSplitSequence = getNextMTTSplitNumSeq();
        ppt.setString(1, mttSplitSequence);
        ppt.setString(2, masterRefNo);
        ppt.setString(3, eventRefNo);
        ppt.setString(4, mttNumber4);
        ppt.setString(5, mttAmount4);
        ppt.setString(6, mttRemark);
        ppt.setString(7, "P");
        ppt.setString(8, loginedUserId);
        ppt.setString(9, "N");
        logger.info(" Insert Query 4: " + ppt.getQueryString());
        updateCnt4 = ppt.executeUpdate();
        if (updateCnt4 > 0)
        {
          String insertDataToStaging = "{call TRACER_PROCEDURE_MTT_SPLIT(?,?,?)}";
          CallableStatement callableStatement = null;
          try
          {
            callableStatement = con.prepareCall(insertDataToStaging);
            callableStatement.setString(1, BaseEventKey97);
            callableStatement.setString(2, MasterKey97);
            callableStatement.setString(3, mttSplitSequence);
            callableStatement.executeUpdate();
          }
          catch (SQLException e)
          {
            e.printStackTrace();
          }
          finally
          {
            closeCallableStatement(callableStatement);
          }
        }
      }
      if (!commonMethods.isNull(mttNumber5))
      {
        int updateCnt5 = 0;
        String mttSplitSequence = getNextMTTSplitNumSeq();
        ppt.setString(1, mttSplitSequence);
        ppt.setString(2, masterRefNo);
        ppt.setString(3, eventRefNo);
        ppt.setString(4, mttNumber5);
        ppt.setString(5, mttAmount5);
        ppt.setString(6, mttRemark);
        ppt.setString(7, "P");
        ppt.setString(8, loginedUserId);
        ppt.setString(9, "N");
        logger.info(" Insert Query 5: " + ppt.getQueryString());
        updateCnt5 = ppt.executeUpdate();
        if (updateCnt5 > 0)
        {
          String insertDataToStaging = "{call TRACER_PROCEDURE_MTT_SPLIT(?,?,?)}";
          CallableStatement callableStatement = null;
          try
          {
            callableStatement = con.prepareCall(insertDataToStaging);
            callableStatement.setString(1, BaseEventKey97);
            callableStatement.setString(2, MasterKey97);
            callableStatement.setString(3, mttSplitSequence);
            callableStatement.executeUpdate();
          }
          catch (SQLException e)
          {
            e.printStackTrace();
          }
          finally
          {
            closeCallableStatement(callableStatement);
          }
        }
      }
      if (!commonMethods.isNull(mttNumber6))
      {
        int updateCnt6 = 0;
        String mttSplitSequence = getNextMTTSplitNumSeq();
        ppt.setString(1, mttSplitSequence);
        ppt.setString(2, masterRefNo);
        ppt.setString(3, eventRefNo);
        ppt.setString(4, mttNumber6);
        ppt.setString(5, mttAmount6);
        ppt.setString(6, mttRemark);
        ppt.setString(7, "P");
        ppt.setString(8, loginedUserId);
        ppt.setString(9, "N");
        logger.info(" Insert Query 6: " + ppt.getQueryString());
        updateCnt6 = ppt.executeUpdate();
        if (updateCnt6 > 0)
        {
          String insertDataToStaging = "{call TRACER_PROCEDURE_MTT_SPLIT(?,?,?)}";
          CallableStatement callableStatement = null;
          try
          {
            callableStatement = con.prepareCall(insertDataToStaging);
            callableStatement.setString(1, BaseEventKey97);
            callableStatement.setString(2, MasterKey97);
            callableStatement.setString(3, mttSplitSequence);
            callableStatement.executeUpdate();
          }
          catch (SQLException e)
          {
            e.printStackTrace();
          }
          finally
          {
            closeCallableStatement(callableStatement);
          }
        }
      }
      if (!commonMethods.isNull(mttNumber7))
      {
        int updateCnt7 = 0;
        String mttSplitSequence = getNextMTTSplitNumSeq();
        ppt.setString(1, mttSplitSequence);
        ppt.setString(2, masterRefNo);
        ppt.setString(3, eventRefNo);
        ppt.setString(4, mttNumber7);
        ppt.setString(5, mttAmount7);
        ppt.setString(6, mttRemark);
        ppt.setString(7, "P");
        ppt.setString(8, loginedUserId);
        ppt.setString(9, "N");
        logger.info(" Insert Query 7: " + ppt.getQueryString());
        updateCnt7 = ppt.executeUpdate();
        if (updateCnt7 > 0)
        {
          String insertDataToStaging = "{call TRACER_PROCEDURE_MTT_SPLIT(?,?,?)}";
          CallableStatement callableStatement = null;
          try
          {
            callableStatement = con.prepareCall(insertDataToStaging);
            callableStatement.setString(1, BaseEventKey97);
            callableStatement.setString(2, MasterKey97);
            callableStatement.setString(3, mttSplitSequence);
            callableStatement.executeUpdate();
          }
          catch (SQLException e)
          {
            e.printStackTrace();
          }
          finally
          {
            closeCallableStatement(callableStatement);
          }
        }
      }
      if (!commonMethods.isNull(mttNumber8))
      {
        int updateCnt8 = 0;
        String mttSplitSequence = getNextMTTSplitNumSeq();
        ppt.setString(1, mttSplitSequence);
        ppt.setString(2, masterRefNo);
        ppt.setString(3, eventRefNo);
        ppt.setString(4, mttNumber8);
        ppt.setString(5, mttAmount8);
        ppt.setString(6, mttRemark);
        ppt.setString(7, "P");
        ppt.setString(8, loginedUserId);
        ppt.setString(9, "N");
        logger.info(" Insert Query 8: " + ppt.getQueryString());
        updateCnt8 = ppt.executeUpdate();
        if (updateCnt8 > 0)
        {
          String insertDataToStaging = "{call TRACER_PROCEDURE_MTT_SPLIT(?,?,?)}";
          CallableStatement callableStatement = null;
          try
          {
            callableStatement = con.prepareCall(insertDataToStaging);
            callableStatement.setString(1, BaseEventKey97);
            callableStatement.setString(2, MasterKey97);
            callableStatement.setString(3, mttSplitSequence);
            callableStatement.executeUpdate();
          }
          catch (SQLException e)
          {
            e.printStackTrace();
          }
          finally
          {
            closeCallableStatement(callableStatement);
          }
        }
      }
      if (!commonMethods.isNull(mttNumber9))
      {
        int updateCnt9 = 0;
        String mttSplitSequence = getNextMTTSplitNumSeq();
        ppt.setString(1, mttSplitSequence);
        ppt.setString(2, masterRefNo);
        ppt.setString(3, eventRefNo);
        ppt.setString(4, mttNumber9);
        ppt.setString(5, mttAmount9);
        ppt.setString(6, mttRemark);
        ppt.setString(7, "P");
        ppt.setString(8, loginedUserId);
        ppt.setString(9, "N");
        logger.info(" Insert Query 9: " + ppt.getQueryString());
        updateCnt9 = ppt.executeUpdate();
        if (updateCnt9 > 0)
        {
          String insertDataToStaging = "{call TRACER_PROCEDURE_MTT_SPLIT(?,?,?)}";
          CallableStatement callableStatement = null;
          try
          {
            callableStatement = con.prepareCall(insertDataToStaging);
            callableStatement.setString(1, BaseEventKey97);
            callableStatement.setString(2, MasterKey97);
            callableStatement.setString(3, mttSplitSequence);
            callableStatement.executeUpdate();
          }
          catch (SQLException e)
          {
            e.printStackTrace();
          }
          finally
          {
            closeCallableStatement(callableStatement);
          }
        }
      }
      if (!commonMethods.isNull(mttNumber10))
      {
        int updateCnt10 = 0;
        String mttSplitSequence = getNextMTTSplitNumSeq();
        ppt.setString(1, mttSplitSequence);
        ppt.setString(2, masterRefNo);
        ppt.setString(3, eventRefNo);
        ppt.setString(4, mttNumber10);
        ppt.setString(5, mttAmount10);
        ppt.setString(6, mttRemark);
        ppt.setString(7, "P");
        ppt.setString(8, loginedUserId);
        ppt.setString(9, "N");
        logger.info(" Insert Query 10: " + ppt.getQueryString());
        updateCnt10 = ppt.executeUpdate();
        if (updateCnt10 > 0)
        {
          String insertDataToStaging = "{call TRACER_PROCEDURE_MTT_SPLIT(?,?,?)}";
          CallableStatement callableStatement = null;
          try
          {
            callableStatement = con.prepareCall(insertDataToStaging);
            callableStatement.setString(1, BaseEventKey97);
            callableStatement.setString(2, MasterKey97);
            callableStatement.setString(3, mttSplitSequence);
            callableStatement.executeUpdate();
          }
          catch (SQLException e)
          {
            e.printStackTrace();
          }
          finally
          {
            closeCallableStatement(callableStatement);
          }
        }
      }
    }
    catch (SQLException e)
    {
      throwDAOException(e);
    }
    finally
    {
      closeSqlRefferance(null, loggableStatement, null);
      closeSqlRefferance(rsKey97, pptKey97, null);
      closeSqlRefferance(rs1, ppt1, null);
      closeSqlRefferance(null, ppt, con);
    }
    logger.info("Exiting Method");
    return updateCnt;
  }
 
  public ArrayList<MTTDataVO> fetchSplitDataForApproval(MTTDataVO mttVo)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement ppt = null;
    ResultSet rs = null;
    String sqlQuery = null;
    CommonMethods commonMethods = null;
    String masterRefNo = null;
    String eventRefNo = null;
    int setValue = 0;
    try
    {
      HttpSession session = ServletActionContext.getRequest().getSession();
     
      commonMethods = new CommonMethods();
      masterRefNo = commonMethods.getEmptyIfNull(mttVo.getMttSplitCheckerMasRef()).trim();
      logger.info("masterRefNo IN fetchSplitDataForApproval :: " + masterRefNo);
      eventRefNo = commonMethods.getEmptyIfNull(mttVo.getMttSplitCheckerEveRef()).trim();
      logger.info("eventRefNo IN fetchSplitDataForApproval :: " + eventRefNo);
      String loginedUserId = (String)session.getAttribute("loginedUserId");
      logger.info("loginedUserId IN fetchSplitDataForApproval :: " + loginedUserId);
      this.Pendinglist = new ArrayList();
      con = DBConnectionUtility.getConnection();
     
      sqlQuery = "SELECT MTTNUMBERSPLIT_KEY97,MASTERREF,EVENTREF,MTTNUMBER,MTTAMOUNT,MTTREMARKS,MTTMAKERUSERID,MTTMAKERTIMESTAMP  FROM MTT_NUMBER_SPLIT_MAKERCHECKER WHERE MTT_AMEND_MAKERCHECKER_STATUS='P'";
      if ((masterRefNo != null) && (!masterRefNo.equalsIgnoreCase("")))
      {
        sqlQuery = sqlQuery + " AND  MASTERREF = '" + masterRefNo + "' ";
       
        mttVo.setMttSplitCheckerMasRef(masterRefNo);
      }
      if ((eventRefNo != null) && (!eventRefNo.equalsIgnoreCase("")))
      {
        sqlQuery = sqlQuery + " AND  EVENTREF = '" + eventRefNo + "' ";
       
        mttVo.setMttSplitCheckerEveRef(eventRefNo);
      }
      if (!commonMethods.isNull(loginedUserId)) {
        sqlQuery = sqlQuery + " AND (MTTMAKERUSERID != ? OR trim(MTTMAKERUSERID) IS NULL) ";
      }
      ppt = new LoggableStatement(con, sqlQuery);
      if (!commonMethods.isNull(loginedUserId)) {
        ppt.setString(++setValue, loginedUserId);
      }
      logger.info("Select Query " + ppt.getQueryString());
      rs = ppt.executeQuery();
      while (rs.next())
      {
        mttVo = new MTTDataVO();
        mttVo.setMttSplitListKeyId(commonMethods.getEmptyIfNull(rs.getString("MTTNUMBERSPLIT_KEY97")).trim());
        mttVo.setMttSplitListMTTNumber(commonMethods.getEmptyIfNull(rs.getString("MTTNUMBER")).trim());
        mttVo.setMttSplitListMTTAmount(commonMethods.getEmptyIfNull(rs.getString("MTTAMOUNT")).trim());
        mttVo.setSplitCheckerRemarks(commonMethods.getEmptyIfNull(rs.getString("MTTREMARKS")).trim());
        mttVo.setMttSplitListMakerUserId(commonMethods.getEmptyIfNull(rs.getString("MTTMAKERUSERID")).trim());
        mttVo.setMttSplitListMakertmstmp(commonMethods.getEmptyIfNull(rs.getString("MTTMAKERTIMESTAMP")).trim());
       
        this.Pendinglist.add(mttVo);
      }
    }
    catch (SQLException e)
    {
      throwDAOException(e);
    }
    finally
    {
      closeSqlRefferance(rs, ppt, con);
    }
    logger.info("Exiting Method");
    return this.Pendinglist;
  }
 
  public String amendSplitCheckerData(MTTDataVO mttVo, String check1, String remarks1)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement loggableStatement = null;
    ResultSet rs = null;
    String result = "fail";
    String query = null;
   
    String masterRefNo = null;
    String eventRefNo = null;
    int count = 0;
   
    CommonMethods commonMethods = null;
    try
    {
      commonMethods = new CommonMethods();
     
      con = DBConnectionUtility.getConnection();
     
      HttpSession session = ServletActionContext.getRequest().getSession();
      masterRefNo = commonMethods.getEmptyIfNull(mttVo.getMttSplitCheckerMasRef()).trim();
      logger.info("masterRefNo IN fetchSplitDataForApproval :: " + masterRefNo);
      eventRefNo = commonMethods.getEmptyIfNull(mttVo.getMttSplitCheckerEveRef()).trim();
      logger.info("eventRefNo IN fetchSplitDataForApproval :: " + eventRefNo);
      String loginedUserId = (String)session.getAttribute("loginedUserName");
      logger.info("loginedUserId IN update Split data :: " + loginedUserId);
     

      logger.info("check update Split data :: " + check1);
      if (check1.equalsIgnoreCase("Approve"))
      {
        query = "UPDATE MTT_NUMBER_SPLIT_MAKERCHECKER SET MTT_AMEND_MAKERCHECKER_STATUS ='A',MTTCHECKERTIMESTAMP = CURRENT_TIMESTAMP,MTTCHECKERUSERID=?,MTTREMARKS=?  WHERE TRIM(MASTERREF) =? AND TRIM(EVENTREF)=? AND ISOVERRIDDEN='N'";
       

        loggableStatement = new LoggableStatement(con, query);
        loggableStatement.setString(1, loginedUserId);
        loggableStatement.setString(2, remarks1);
        loggableStatement.setString(3, masterRefNo);
        loggableStatement.setString(4, eventRefNo);
       
        count = loggableStatement.executeUpdate();
      }
      else if (check1.equalsIgnoreCase("Reject"))
      {
        query = "UPDATE MTT_NUMBER_SPLIT_MAKERCHECKER SET MTT_AMEND_MAKERCHECKER_STATUS ='R',MTTCHECKERTIMESTAMP = CURRENT_TIMESTAMP,MTTCHECKERUSERID=?,MTTREMARKS=?  WHERE TRIM(MASTERREF) =? AND TRIM(EVENTREF)=? AND ISOVERRIDDEN='N'";
       

        loggableStatement = new LoggableStatement(con, query);
        loggableStatement.setString(1, loginedUserId);
        loggableStatement.setString(2, remarks1);
        loggableStatement.setString(3, masterRefNo.trim());
        loggableStatement.setString(4, eventRefNo.trim());
       
        count = loggableStatement.executeUpdate();
      }
      if (count > 0) {
        result = "SUCCESS";
      }
    }
    catch (Exception e)
    {
      logger.info("updateStatus---------------------------Exception-------------" + e);
      e.printStackTrace();
      throwDAOException(e);
    }
    finally
    {
      closeSqlRefferance(rs, loggableStatement, con);
    }
    logger.info("Exiting Method");
    return result;
  }
 
  public String processStagingData(MTTDataVO mttVo)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement ppt = null;
   
    CallableStatement callableStatement = null;
    ResultSet rs = null;
   
    String result = "fail";
   
    String sqlQuery = null;
   
    String BaseEventKey97 = "";
    String MasterKey97 = "";
    CommonMethods commonMethods = null;
    LoggableStatement pptStaging = null;
   
    ResultSet rsStaging = null;
    try
    {
      commonMethods = new CommonMethods();
     
      con = DBConnectionUtility.getConnection();
     
      HttpSession session = ServletActionContext.getRequest().getSession();
     
      String loginedUserId = (String)session.getAttribute("loginedUserName");
      logger.info("loginedUserId IN updateStatus :: " + loginedUserId);
     
      sqlQuery = "select BASE_KEY,MASTER_KEY,KEY97 from ETT_MTT_ENQUIRY where TRIM(MASTER_REF)=? AND EVENT_REF_NO=?  and TRIM(MTTNUMBER) =?";
     
      ppt = new LoggableStatement(con, sqlQuery);
      ppt.setString(1, mttVo.getStgMasRef());
      ppt.setString(2, mttVo.getStgEveRef());
      ppt.setString(3, mttVo.getStgMttNumber());
     
      logger.info("Select Query for Key97 :: " + ppt.getQueryString());
      rs = ppt.executeQuery();
      if (rs.next())
      {
        BaseEventKey97 = commonMethods.getEmptyIfNull(rs.getString("BASE_KEY")).trim();
        MasterKey97 = commonMethods.getEmptyIfNull(rs.getString("MASTER_KEY")).trim();
       
        logger.info("BaseEventKey97 :: " + BaseEventKey97 + " :: MasterKey97 :: " + MasterKey97);
        int stagingCnt = 0;
        String sqlStagingQuery = "SELECT COUNT(*) AS CNT FROM MTT_TRACER_STAGING WHERE TRIM(BASE_KEY)=" + BaseEventKey97 + " AND TRIM(MASTER_KEY)=" + MasterKey97;
       
        pptStaging = new LoggableStatement(con, sqlStagingQuery);
       
        logger.info("Select Query for Staging Count:: " + pptStaging.getQueryString());
       
        rsStaging = pptStaging.executeQuery();
        if (rsStaging.next()) {
          stagingCnt = rsStaging.getInt("CNT");
        }
        logger.info("stagingCnt :: " + stagingCnt);
        if (stagingCnt == 0) {
          try
          {
            logger.info("Before TRACER_PROCEDURE ");
            String insertDataToStaging = "{call TRACER_PROCEDURE(?,?)}";
            callableStatement = con.prepareCall(insertDataToStaging);
            callableStatement.setString(1, BaseEventKey97);
            callableStatement.setString(2, MasterKey97);
            callableStatement.executeUpdate();
            if (callableStatement != null) {
              callableStatement.close();
            }
            logger.info("After TRACER_PROCEDURE");
          }
          catch (SQLException se)
          {
            logger.info("updateStatus---------------------------Exception-------------" + se);
            se.printStackTrace();
          }
          finally
          {
            if (callableStatement != null) {
              callableStatement.close();
            }
          }
        }
      }
    }
    catch (SQLException se)
    {
      logger.info("updateStatus---------------------------Exception-------------" + se);
      se.printStackTrace();
    }
    catch (Exception e)
    {
      logger.info("updateStatus---------------------------Exception-------------" + e);
      e.printStackTrace();
      throwDAOException(e);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(null, pptStaging, rsStaging);
      closeSqlRefferance(rs, ppt, con);
    }
    logger.info("Exiting Method");
    return result;
  }
 
  public static String getNextMTTSplitNumSeq()
  {
    String sourceRefSeq = "";
    ResultSet rs = null;
    Connection conn = null;
    PreparedStatement pst = null;
    try
    {
      conn = DBConnectionUtility.getConnection();
      pst = conn.prepareStatement("Select MTT_NUMBER_SPLIT_SEQ.NEXTVAL from dual");
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
    System.out.println("MTT_NUMBER_SPLIT_SEQ from getNextMTTSplitNumSeq() :----" + sourceRefSeq);
    return sourceRefSeq;
  }
 
  public String getCloseUrl()
    throws DAOException
  {
    Connection con = null;
    PreparedStatement prepareStatement = null;
    ResultSet resultSet = null;
    String url = "";
    try
    {
      con = DBConnectionUtility.getConnection();
      prepareStatement = con.prepareStatement("SELECT TRIM(VALUE1) AS CLOSEURL FROM ETT_PARAMETER_TBL WHERE PARAMETER_ID = ?");
      prepareStatement.setString(1, "closeURL");
      resultSet = prepareStatement.executeQuery();
      if (resultSet.next())
      {
        url = resultSet.getString(1);
        logger.info("CLOSEURL" + url);
      }
    }
    catch (Exception e)
    {
      throwDAOException(e);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, prepareStatement, resultSet);
    }
    return url;
  }
}

