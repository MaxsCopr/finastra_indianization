package in.co.localization.dao.localization;

import com.opensymphony.xwork2.ActionContext;
import in.co.localization.dao.AbstractDAO;
import in.co.localization.dao.exception.DAOException;
import in.co.localization.utility.ActionConstantsQuery;
import in.co.localization.utility.CommonMethods;
import in.co.localization.utility.DBConnectionUtility;
import in.co.localization.utility.LoggableStatement;
import in.co.localization.utility.UtilityGenerateCard;
import in.co.localization.vo.localization.AdTransferVO;
import in.co.localization.vo.localization.AlertMessagesVO;
import in.co.localization.vo.localization.EDMPSProcessVO;
import in.co.localization.vo.localization.EodDownloadVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

public class AdTransferDAO
  extends AbstractDAO
  implements ActionConstantsQuery
{
  static AdTransferDAO dao;
  private static Logger logger = Logger.getLogger(AdTransferDAO.class
    .getName());
  private ArrayList<AlertMessagesVO> alertMsgArray = new ArrayList();
 
  public static AdTransferDAO getDAO()
  {
    if (dao == null) {
      dao = new AdTransferDAO();
    }
    return dao;
  }
 
  public ArrayList<AlertMessagesVO> getAlertMsgArray()
  {
    return this.alertMsgArray;
  }
 
  public void setAlertMsgArray(ArrayList<AlertMessagesVO> alertMsgArray)
  {
    this.alertMsgArray = alertMsgArray;
  }
 
  public ArrayList<AdTransferVO> fetchAdTransferValue()
    throws DAOException
  {
    logger.info("Entering--------------fetchAdTransferValue------------- Method");
    ArrayList adTransferList = null;
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    AdTransferVO adTransferVO = null;
    CommonMethods commonMethods = null;
    try
    {
      con = DBConnectionUtility.getConnection();
      commonMethods = new CommonMethods();
      adTransferList = new ArrayList();
      pst = new LoggableStatement(con, "SELECT SHIP_ID,IECODE,TO_CHAR(TO_DATE(SHIPPINGBILLDATE, 'ddmmyyyy'),'dd-mm-yyyy') as SHIPPINGBILLDATE,PORTCODE,FORMNO,EXPORTAGENCY,EXPORTTYPE,SHIPPINGBILLNO,EXISTINGADCODE,NEWADCODE FROM ETT_TRR_SHP_STG WHERE TRANSFERSTATUS='Pending' OR TRANSFERSTATUS IS NULL");
      rs = pst.executeQuery();
      logger.info("Entering--------------fetchAdTransferValue---------query" + pst.getQueryString());
      while (rs.next())
      {
        adTransferVO = new AdTransferVO();
        adTransferVO.setTieCode(commonMethods.getEmptyIfNull(rs.getString("IECODE")).trim());
        adTransferVO.setTshippingBillDate(commonMethods.getEmptyIfNull(rs.getString("SHIPPINGBILLDATE")).trim());
        adTransferVO.setTportCode(commonMethods.getEmptyIfNull(rs.getString("PORTCODE")).trim());
        adTransferVO.setTformNo(commonMethods.getEmptyIfNull(rs.getString("FORMNO")).trim());
        String expAgency = commonMethods.getEmptyIfNull(rs.getString("EXPORTAGENCY")).trim();
       
        String temp_expAgency = "";
        if (expAgency.equalsIgnoreCase("1")) {
          temp_expAgency = "Customs";
        } else if (expAgency.equalsIgnoreCase("2")) {
          temp_expAgency = "SEZ";
        } else if (expAgency.equalsIgnoreCase("3")) {
          temp_expAgency = "STPI";
        }
        adTransferVO.setTexportAgency(temp_expAgency);
       
        String expType = commonMethods.getEmptyIfNull(rs.getString("EXPORTTYPE")).trim();
       
        String temp_expType = "";
        if (expType.equalsIgnoreCase("1")) {
          temp_expType = "Goods";
        } else if (expType.equalsIgnoreCase("2")) {
          temp_expType = "Softex";
        } else if (expType.equalsIgnoreCase("3")) {
          temp_expType = "Royalty";
        }
        adTransferVO.setTtypeofExport(temp_expType);
       
        adTransferVO.setTshippingBillNo(commonMethods.getEmptyIfNull(rs.getString("SHIPPINGBILLNO")).trim());
        adTransferVO.setToldAdCode(commonMethods.getEmptyIfNull(rs.getString("EXISTINGADCODE")).trim());
        adTransferVO.setTnewAdCode(commonMethods.getEmptyIfNull(rs.getString("NEWADCODE")).trim());
        adTransferVO.setShipId(rs.getString("SHIP_ID"));
        adTransferList.add(adTransferVO);
      }
    }
    catch (Exception exception)
    {
      logger.info("Entering--------------fetchAdTransferValue------exception" + exception);
      throwDAOException(exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    logger.info("Exiting Method");
    return adTransferList;
  }
 
  public AdTransferVO getShippingDetails(AdTransferVO adTransferVO)
    throws DAOException
  {
    logger.info("Entering Method");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    CommonMethods commonMethods = null;
    try
    {
      commonMethods = new CommonMethods();
      con = DBConnectionUtility.getConnection();
      String GET_ADTRANSFER_VALUE = "SELECT SHIP_ID,IECODE,SHIPPINGBILLDATE,PORTCODE,FORMNO,EXPORTAGENCY,EXPORTTYPE,SHIPPINGBILLNO,EXISTINGADCODE,NEWADCODE FROM ETT_TRR_SHP_STG WHERE SHIP_ID =?";
      pst = new LoggableStatement(con, GET_ADTRANSFER_VALUE);
      pst.setString(1, adTransferVO.getShipId());
      rs = pst.executeQuery();
      if (rs != null) {
        while (rs.next())
        {
          adTransferVO = new AdTransferVO();
          adTransferVO.setTieCode(commonMethods.getEmptyIfNull(rs.getString("IECODE")).trim());
          String tempDate = rs.getString("SHIPPINGBILLDATE");
          if (!commonMethods.isNull(tempDate))
          {
            String dateSH = tempDate.substring(0, 2) + "-" + tempDate.substring(2, 4) + "-" + tempDate.substring(4, 8);
            adTransferVO.setTshippingBillDate(dateSH);
          }
          adTransferVO.setTportCode(commonMethods.getEmptyIfNull(rs.getString("PORTCODE")).trim());
          adTransferVO.setTformNo(commonMethods.getEmptyIfNull(rs.getString("FORMNO")).trim());
         
          String expAgency = commonMethods.getEmptyIfNull(rs.getString("EXPORTAGENCY")).trim();
         
          adTransferVO.setTexportAgency(expAgency);
         
          String expType = commonMethods.getEmptyIfNull(rs.getString("EXPORTTYPE")).trim();
         
          adTransferVO.setTtypeofExport(expType);
         
          adTransferVO.setTshippingBillNo(commonMethods.getEmptyIfNull(rs.getString("SHIPPINGBILLNO")).trim());
          adTransferVO.setToldAdCode(commonMethods.getEmptyIfNull(rs.getString("EXISTINGADCODE")).trim());
          adTransferVO.setTnewAdCode(commonMethods.getEmptyIfNull(rs.getString("NEWADCODE")).trim());
          adTransferVO.setShipId(rs.getString("SHIP_ID"));
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
    return adTransferVO;
  }
 
  public AdTransferVO getValidate(AdTransferVO adTransferVO)
    throws DAOException
  {
    logger.info("Entering Method");
    CommonMethods commonMethods = null;
    Connection con = null;
    try
    {
      con = DBConnectionUtility.getConnection();
     
      commonMethods = new CommonMethods();
      if ((this.alertMsgArray != null) &&
        (this.alertMsgArray.size() > 0)) {
        this.alertMsgArray.clear();
      }
      if (commonMethods.isNull(adTransferVO.getTtypeofExport()))
      {
        String errormsg = UtilityGenerateCard.getErrorDesc("ADT007", "N134");
        Object[] arg = { Integer.valueOf(0), "E", errormsg, "Input" };
        setErrorvalues(arg);
      }
      if (commonMethods.isNull(adTransferVO.getTexportAgency()))
      {
        String errormsg = UtilityGenerateCard.getErrorDesc("ADT009", "N134");
        Object[] arg = { Integer.valueOf(0), "E", errormsg, "Input" };
        setErrorvalues(arg);
      }
      String expType = commonMethods.getEmptyIfNull(adTransferVO.getTtypeofExport()).trim();
      if ((expType.equalsIgnoreCase("1")) ||
        (expType.equalsIgnoreCase("3")))
      {
        if (commonMethods.isNull(adTransferVO.getTshippingBillNo()))
        {
          String errormsg = UtilityGenerateCard.getErrorDesc("ADT001", "N134");
          Object[] arg = { Integer.valueOf(0), "E", errormsg, "Input" };
          setErrorvalues(arg);
        }
      }
      else if ((expType.equalsIgnoreCase("2")) &&
        (commonMethods.isNull(adTransferVO.getTformNo())))
      {
        String errormsg = UtilityGenerateCard.getErrorDesc("ADT008", "N134");
        Object[] arg = { Integer.valueOf(0), "E", errormsg, "Input" };
        setErrorvalues(arg);
      }
      if (commonMethods.isNull(adTransferVO.getTshippingBillDate()))
      {
        String errormsg = UtilityGenerateCard.getErrorDesc("ADT003", "N134");
        Object[] arg = { Integer.valueOf(0), "E", errormsg, "Input" };
        setErrorvalues(arg);
      }
      if (commonMethods.isNull(adTransferVO.getTportCode()))
      {
        String errormsg = UtilityGenerateCard.getErrorDesc("ADT004", "N134");
        Object[] arg = { Integer.valueOf(0), "E", errormsg, "Input" };
        setErrorvalues(arg);
      }
      if (commonMethods.isNull(adTransferVO.getTieCode()))
      {
        String errormsg = UtilityGenerateCard.getErrorDesc("ADT005", "N134");
        Object[] arg = { Integer.valueOf(0), "E", errormsg, "Input" };
        setErrorvalues(arg);
      }
      if (commonMethods.isNull(adTransferVO.getToldAdCode()))
      {
        String errormsg = UtilityGenerateCard.getErrorDesc("ADT006", "N134");
        Object[] arg = { Integer.valueOf(0), "E", errormsg, "Input" };
        setErrorvalues(arg);
      }
      if (commonMethods.isNull(adTransferVO.getTnewAdCode()))
      {
        String errormsg = UtilityGenerateCard.getErrorDesc("ADT002", "N134");
        Object[] arg = { Integer.valueOf(0), "E", errormsg, "Input" };
        setErrorvalues(arg);
      }
      String shipBillNo = commonMethods.getEmptyIfNull(adTransferVO.getTshippingBillNo()).trim();
     
      String shippingDate = commonMethods.getEmptyIfNull(adTransferVO.getTshippingBillDate()).trim();
     
      shippingDate = shippingDate.replace("-", "").trim();
     
      String portCode = commonMethods.getEmptyIfNull(adTransferVO.getTportCode()).trim();
     
      String formNo = commonMethods.getEmptyIfNull(adTransferVO.getTformNo()).trim();
      if ((expType.equalsIgnoreCase("1")) ||
        (expType.equalsIgnoreCase("3")))
      {
        if ((!commonMethods.isNull(shipBillNo)) &&
          (!commonMethods.isNull(shippingDate)) && (!commonMethods.isNull(portCode)))
        {
          int shpCount = 0;
          String query = "SELECT COUNT(*) AS SHP_COUNT FROM ETT_TRR_SHP_STG  WHERE SHIPPINGBILLNO = ? AND SHIPPINGBILLDATE =? AND PORTCODE = ? AND TRANSFERSTATUS = 'Completed' ";
         



          PreparedStatement pst = con.prepareStatement(query);
          pst.setString(1, shipBillNo);
          pst.setString(2, shippingDate);
          pst.setString(3, portCode);
          ResultSet rs = pst.executeQuery();
          if (rs.next()) {
            shpCount = rs.getInt("SHP_COUNT");
          }
          if (shpCount > 0)
          {
            String errormsg = UtilityGenerateCard.getErrorDesc("ADT010", "N134");
            Object[] arg = { Integer.valueOf(0), "E", errormsg, "Input" };
            setErrorvalues(arg);
          }
          closeSqlRefferance(rs, pst);
        }
      }
      else if ((expType.equalsIgnoreCase("2")) &&
        (!commonMethods.isNull(formNo)) &&
        (!commonMethods.isNull(shippingDate)) && (!commonMethods.isNull(portCode)))
      {
        int formCount = 0;
        String query = "SELECT COUNT(*) AS FORM_COUNT FROM ETT_TRR_SHP_STG  WHERE FORMNO = ? AND SHIPPINGBILLDATE = ? AND PORTCODE =? AND TRANSFERSTATUS = 'Completed' ";
       

        PreparedStatement pst = con.prepareStatement(query);
        pst.setString(1, formNo);
        pst.setString(2, shippingDate);
        pst.setString(3, portCode);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
          formCount = rs.getInt("FORM_COUNT");
        }
        if (formCount > 0)
        {
          String errormsg = UtilityGenerateCard.getErrorDesc("ADT010", "N134");
          Object[] arg = { Integer.valueOf(0), "E", errormsg, "Input" };
          setErrorvalues(arg);
        }
        closeSqlRefferance(rs, pst);
      }
      if (this.alertMsgArray.size() > 0) {
        adTransferVO.setErrorList(this.alertMsgArray);
      }
    }
    catch (Exception exception)
    {
      throwDAOException(exception);
    }
    finally
    {
      logger.info("Exiting Method");
    }
    return adTransferVO;
  }
 
  public void setErrorvalues(Object[] arg)
  {
    CommonMethods commonMethods = new CommonMethods();
    AlertMessagesVO altMsg = new AlertMessagesVO();
    altMsg.setErrorId(commonMethods.getEmptyIfNull(arg[1]).equalsIgnoreCase("W") ? "Warning" : "Error");
    altMsg.setErrorDesc("General");
    altMsg.setErrorCode(commonMethods.getEmptyIfNull(arg[3]));
    altMsg.setErrorDetails(commonMethods.getEmptyIfNull(arg[2]));
    altMsg.setErrorMsg(commonMethods.getEmptyIfNull(arg[1]).equalsIgnoreCase("W") ? "N" : "");
    this.alertMsgArray.add(altMsg);
  }
 
  public void updateTransferData(AdTransferVO adTransferVO)
    throws DAOException
  {
    logger.info("Entering --------------updateTransferData-----------Method");
    LoggableStatement pst = null;
    Connection con = null;
    boolean isAlreadyExists = false;
    String userName = null;
    CommonMethods commonMethods = null;
    try
    {
      commonMethods = new CommonMethods();
      con = DBConnectionUtility.getConnection();
     
      HttpSession session = ServletActionContext.getRequest().getSession();
      userName = (String)session.getAttribute("loginedUserName");
     
      logger.info("userName--------loginedUserName-----------" + userName);
     
      String shipId = commonMethods.getEmptyIfNull(adTransferVO.getShipId()).trim();
     
      String shippingDate = commonMethods.getEmptyIfNull(adTransferVO.getTshippingBillDate()).trim();
     
      shippingDate = shippingDate.replace("-", "").trim();
     
      String createdDate = (String)session.getAttribute("CREATEDATE");
     
      String shipBillNo = commonMethods.getEmptyIfNull(adTransferVO.getTshippingBillNo()).trim();
     
      String portCode = commonMethods.getEmptyIfNull(adTransferVO.getTportCode()).trim();
     
      String formNo = commonMethods.getEmptyIfNull(adTransferVO.getTformNo()).trim();
     
      String expType = commonMethods.getEmptyIfNull(adTransferVO.getTtypeofExport()).trim();
      if ((expType.equalsIgnoreCase("1")) ||
        (expType.equalsIgnoreCase("3")))
      {
        if ((!commonMethods.isNull(shipBillNo)) &&
          (!commonMethods.isNull(shippingDate)) && (!commonMethods.isNull(portCode)))
        {
          int shpCount = 0;
          String query = "SELECT COUNT(*) AS SHP_COUNT FROM ETT_TRR_SHP_STG  WHERE SHIPPINGBILLNO = ? AND SHIPPINGBILLDATE = ? AND PORTCODE = ? AND TRANSFERSTATUS = 'Pending' ";
         

          PreparedStatement pst1 = con.prepareStatement(query);
          pst1.setString(1, shipBillNo);
          pst1.setString(2, shippingDate);
          pst1.setString(3, portCode);
          ResultSet rs1 = pst1.executeQuery();
          if (rs1.next()) {
            shpCount = rs1.getInt("SHP_COUNT");
          }
          if (shpCount > 0) {
            isAlreadyExists = true;
          }
          closeSqlRefferance(rs1, pst1);
        }
      }
      else if ((expType.equalsIgnoreCase("2")) &&
        (!commonMethods.isNull(formNo)) &&
        (!commonMethods.isNull(shippingDate)) && (!commonMethods.isNull(portCode)))
      {
        int formCount = 0;
        String query = "SELECT COUNT(*) AS FORM_COUNT FROM ETT_TRR_SHP_STG  WHERE FORMNO =? AND SHIPPINGBILLDATE = ? AND PORTCODE = ? AND TRANSFERSTATUS = 'Pending' ";
       

        LoggableStatement lst1 = new LoggableStatement(con, query);
        lst1.setString(1, formNo);
        lst1.setString(2, shippingDate);
        lst1.setString(3, portCode);
        logger.info("ETT_TRR_SHP_STG Query------------------" + lst1.getQueryString());
        ResultSet rs1 = lst1.executeQuery();
        if (rs1.next())
        {
          formCount = rs1.getInt("FORM_COUNT");
          logger.info("ETT_TRR_SHP_STG Query-----------count value-------" + formCount);
        }
        if (formCount > 0) {
          isAlreadyExists = true;
        }
        closeSqlRefferance(rs1, lst1);
      }
      if (isAlreadyExists)
      {
        pst = new LoggableStatement(con, "UPDATE ETT_TRR_SHP_STG SET SHIPPINGBILLNO =?, SHIPPINGBILLDATE =?, PORTCODE =?, EXPORTAGENCY =?, EXPORTTYPE =?, FORMNO =?, IECODE =?, EXISTINGADCODE =?, NEWADCODE =?, FILE_NO =?, TRANSFERSTATUS =?,LASTUPDATE =to_timestamp(?, 'DD-MM-YYYY HH12:MI:SS'),USERID =?  WHERE SHIP_ID =?");
       

        pst.setString(1, adTransferVO.getTshippingBillNo());
        pst.setString(2, shippingDate);
        pst.setString(3, adTransferVO.getTportCode());
        pst.setString(4, adTransferVO.getTexportAgency());
        pst.setString(5, adTransferVO.getTtypeofExport());
        pst.setString(6, adTransferVO.getTformNo());
        pst.setString(7, adTransferVO.getTieCode());
        pst.setString(8, adTransferVO.getToldAdCode());
        pst.setString(9, adTransferVO.getTnewAdCode());
        pst.setString(10, "1");
        pst.setString(11, "Pending");
        pst.setString(12, createdDate);
        pst.setString(13, userName);
        pst.setString(14, shipId);
        logger.info("UPDATE------------ETT_TRR_SHP_STG----------" + pst.getQueryString());
        int a = pst.executeUpdate();
        logger.info("UPDATE---------count----------" + a);
      }
      else
      {
        pst = new LoggableStatement(con, "Insert into ETT_TRR_SHP_STG (SHIP_ID,SHIPPINGBILLNO,SHIPPINGBILLDATE,PORTCODE,EXPORTAGENCY,EXPORTTYPE,FORMNO,IECODE,EXISTINGADCODE,NEWADCODE,FILE_NO,TRANSFERSTATUS,CREATEDON,USERID) VALUES (TRR_SHP_SEQNO.NEXTVAL,?,?,?,?,?,?,?,?,?,?,?,to_timestamp(?, 'DD-MM-YYYY HH12:MI:SS'),?)");
        pst.setString(1, adTransferVO.getTshippingBillNo());
        pst.setString(2, shippingDate);
        pst.setString(3, adTransferVO.getTportCode());
        pst.setString(4, adTransferVO.getTexportAgency());
        pst.setString(5, adTransferVO.getTtypeofExport());
        pst.setString(6, adTransferVO.getTformNo());
        pst.setString(7, adTransferVO.getTieCode());
        pst.setString(8, adTransferVO.getToldAdCode());
        pst.setString(9, adTransferVO.getTnewAdCode());
        pst.setString(10, "1");
        pst.setString(11, "Pending");
        pst.setString(12, createdDate);
        pst.setString(13, userName);
       
        logger.info("Insert ETT_TRR_SHP_STG------------ETT_TRR_SHP_STG----------" + pst.getQueryString());
        int b = pst.executeUpdate();
       
        logger.info("Insert---------count----------" + b);
      }
    }
    catch (Exception exception)
    {
      logger.info("Entering --------------updateTransferData-------exception" + exception);
      throwDAOException(exception);
    }
    finally
    {
      logger.info("Exiting Method");
    }
  }
 
  public ArrayList<EDMPSProcessVO> fetchEODFileList(EodDownloadVO eodDownloadVO)
  {
    logger.info("Entering Method");
    ArrayList eodFileList = null;
    Connection con = null;
    LoggableStatement pst = null;
    ResultSet rs = null;
    CommonMethods commonMethods = null;
    try
    {
      con = DBConnectionUtility.getConnection();
     
      commonMethods = new CommonMethods();
     
      eodFileList = new ArrayList();
     
      String eodFileType = null;
      if (eodDownloadVO != null)
      {
        eodFileType = commonMethods.getEmptyIfNull(eodDownloadVO.getEodFileType()).trim();
        logger.info("the File Type is : =========================================>>> IN : " + eodFileType);
      }
      logger.info("the File Type is : =========================================>>> OUT : " + eodFileType);
      String sqlQuery = "SELECT TRIM(ID) AS EOD_ID,TRIM(DESCRIPTION) AS DESCRIPTION FROM ETT_EOD_JOBS WHERE FILETYPE = ? ";
     
      pst = new LoggableStatement(con, sqlQuery);
      pst.setString(1, eodFileType);
      rs = pst.executeQuery();
      while (rs.next())
      {
        EDMPSProcessVO eodListVO = new EDMPSProcessVO();
        eodListVO.setKey(rs.getString("EOD_ID"));
        eodListVO.setValue(rs.getString("DESCRIPTION"));
        eodFileList.add(eodListVO);
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
    return eodFileList;
  }
 
  public String getUserId(String userName)
    throws DAOException
  {
    logger.info("getUserId-------------------------" + userName);
    Connection con = null;
    LoggableStatement pst = null;
    String userId = null;
    ResultSet rs = null;
    try
    {
      con = DBConnectionUtility.getConnection();
      String getUserId = "select skey80 from secage88 where name85 ='" +
        userName + "'";
      pst = new LoggableStatement(con, getUserId);
      logger.info("User ID Checking Query------------------" + pst.getQueryString());
      logger.info("User ID Checking Query----------userName--------" + userName);
      rs = pst.executeQuery();
      while (rs.next()) {
        userId = String.valueOf(rs.getInt("SKEY80"));
      }
      logger.info("User id--------------->" + userId);
    }
    catch (Exception exception)
    {
      logger.info("Excepiton in getUserId-----------------" + exception);
     
      throwDAOException(exception);
    }
    finally
    {
      closeSqlRefferance(rs, pst, con);
    }
    logger.info("Exiting Method");
    return userId;
  }
 
  public void setDate()
    throws DAOException
  {
    logger.info("Entering Method");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    CommonMethods commonMethods = null;
    try
    {
      commonMethods = new CommonMethods();
      Map session = ActionContext.getContext().getSession();
      con = DBConnectionUtility.getConnection();
      String query = "SELECT TO_CHAR(TO_DATE(PROCDATE, 'dd-mm-yy'),'dd-mm-yyyy') as PROCDATE1,PROCDATE FROM dlyprccycl";
      pst = new LoggableStatement(con, query);
      rs = pst.executeQuery();
      if (rs.next())
      {
        String dateValue = commonMethods.getEmptyIfNull(rs.getString("PROCDATE1")).trim();
        session.put("processDate", dateValue);
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        Timestamp dbSqlTimestamp = rs.getTimestamp("PROCDATE");
        String createdDate = df.format(dbSqlTimestamp);
        session.put("CREATEDATE", createdDate);
        logger.info("dbSqlTimestamp=" + df.format(dbSqlTimestamp));
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
  }
}