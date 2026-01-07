package in.co.localization.dao.localization;
 
import in.co.localization.dao.AbstractDAO;

import in.co.localization.dao.exception.DAOException;

import in.co.localization.utility.ActionConstantsQuery;

import in.co.localization.utility.CommonMethods;

import in.co.localization.utility.DBConnectionUtility;

import in.co.localization.utility.LoggableStatement;

import in.co.localization.vo.localization.BOEDataSearchVO;

import in.co.localization.vo.localization.IdpmsOrmVO;

import java.sql.Connection;

import java.sql.ResultSet;

import java.text.SimpleDateFormat;

import java.util.ArrayList;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import org.apache.struts2.ServletActionContext;
 
public class IdpmsOrmDAO

  extends AbstractDAO

  implements ActionConstantsQuery

{

  static IdpmsOrmDAO dao;

  private static Logger logger = Logger.getLogger(AcknowledgementDAO.class

    .getName());

  public static IdpmsOrmDAO getDAO()

  {

    if (dao == null) {

      dao = new IdpmsOrmDAO();

    }

    return dao;

  }

  public IdpmsOrmVO fetchormData(IdpmsOrmVO idpmsOrmVO)

    throws DAOException

  {

    logger.info("Entering Method");

    LoggableStatement pst = null;

    ResultSet rs = null;

    Connection con = null;

    String query = null;

    try

    {

      con = DBConnectionUtility.getConnection();

      String ormNo = null;

      if (idpmsOrmVO.getOrefNum() != null) {

        ormNo = idpmsOrmVO.getOrefNum();

      }

      query = "SELECT ADCODE,AMOUNT,CURRENCYCODE,TO_CHAR(TO_DATE(PAYMENTDATE,'DD-MM-YY'),'DD/MM/YYYY') AS PAYMENTDATE, IECODE,SWIFTMESSAGE,STATUS FROM ETT_IDPMS_EOD_DATA WHERE OUTWARDREFERENCENUMBER = ?";

 
      pst = new LoggableStatement(con, query);

      pst.setString(1, ormNo);

      rs = pst.executeQuery();

      if (rs.next())

      {

        idpmsOrmVO.setOadcode(rs.getString("ADCODE"));

        idpmsOrmVO.setOamt(rs.getString("AMOUNT"));

        idpmsOrmVO.setOcurrcode(rs.getString("CURRENCYCODE"));

        idpmsOrmVO.setOpaymdate(rs.getString("PAYMENTDATE"));

        idpmsOrmVO.setOiecode(rs.getString("IECODE"));

        idpmsOrmVO.setOswifmsg(rs.getString("SWIFTMESSAGE"));

        idpmsOrmVO.setOstatus(rs.getString("STATUS"));

        double outStandingAmt = getOutStandingAmountFromPayment(con, ormNo);

        idpmsOrmVO.setUnUtilAmt(outStandingAmt);

      }

      query = "SELECT TDOCNUM,TO_CHAR(TO_DATE(TDOCDAT,'DD-MM-YY'),'DD/MM/YYYY') AS TDOCDAT,TPORTCODE, TLETTERNO,TO_CHAR(TO_DATE(TLETTERDAT,'DD-MM-YY'),'DD/MM/YYYY') AS TLETTERDAT,TRECORDINDICATOR, TO_CHAR(TO_DATE(TCLOSDAT,'DD-MM-YY'),'DD/MM/YYYY') AS TCLOSDAT,TADJCLOINDI,APPROVEDBY, TREMARKS,CREMARKS,XKEY FROM ETT_IDPMS_DATA_EC WHERE TOUTWARDREFERENCENUMBER = ? ORDER BY MAKER_TIMESTAMP DESC ";

 
 
      LoggableStatement pst1 = new LoggableStatement(con, query);

      pst1.setString(1, ormNo);

      ResultSet rs1 = pst1.executeQuery();

      if (rs1.next())

      {

        idpmsOrmVO.setOdocnum(rs1.getString("TDOCNUM"));

        idpmsOrmVO.setOdocdate(rs1.getString("TDOCDAT"));

        idpmsOrmVO.setOcprtcde(rs1.getString("TPORTCODE"));

        idpmsOrmVO.setOcltrno(rs1.getString("TLETTERNO"));

        idpmsOrmVO.setOltrdate(rs1.getString("TLETTERDAT"));

        idpmsOrmVO.setRecInd(rs1.getString("TRECORDINDICATOR"));

        idpmsOrmVO.setOclosdat(rs1.getString("TCLOSDAT"));

        idpmsOrmVO.setOadjcloind(rs1.getString("TADJCLOINDI"));

        idpmsOrmVO.setApprovedBy(rs1.getString("APPROVEDBY"));

        idpmsOrmVO.setRemarks(rs1.getString("TREMARKS"));

        idpmsOrmVO.setOutRemarks(rs1.getString("CREMARKS"));

        idpmsOrmVO.setExKey(rs1.getString("XKEY"));

      }

      closeStatementResultSet(pst1, rs1);

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

    return idpmsOrmVO;

  }

  private double getOutStandingAmountFromPayment(Connection con, String outWardReferenceNo)

  {

    LoggableStatement loggableStatement = null;

    ResultSet resultSet = null;

    double outStandingAmt = 0.0D;

    double closedAmt = 0.0D;

    double outStandingAmount = 0.0D;

    try

    {

      String query = " SELECT CASE WHEN (NVL(BOE.AMOUNT,0) - NVL(ENDO_AMT.ENDORSE_AMT,0)- NVL(EID.TCLOSAMT,0)) < 0 THEN 0 ELSE (NVL(BOE.AMOUNT,0) - NVL(ENDO_AMT.ENDORSE_AMT,0)- NVL(EID.TCLOSAMT,0)) END AS AMOUNT_UNUTILIZED  FROM ETT_IDPMS_EOD_DATA BOE,  (SELECT TOUTWARDREFERENCENUMBER,SUM(NVL(TCLOSAMT,0)) AS TCLOSAMT FROM ETT_IDPMS_DATA_EC GROUP BY TOUTWARDREFERENCENUMBER)  EID,  (SELECT TRIM(BOE_PAYMENT_BP_PAY_REF)||TRIM(BOE_PAYMENT_BP_PAY_PART_REF) AS REF_NO,SUM(NVL(BOE_PAYMENT_BP_PAY_ENDORSE_AMT,0)) AS ENDORSE_AMT  FROM ETT_BOE_PAYMENT WHERE STATUS = 'A' GROUP BY TRIM(BOE_PAYMENT_BP_PAY_REF)||TRIM(BOE_PAYMENT_BP_PAY_PART_REF))  ENDO_AMT  WHERE TRIM(BOE.OUTWARDREFERENCENUMBER)=TRIM(EID.TOUTWARDREFERENCENUMBER(+)) AND TRIM(BOE.OUTWARDREFERENCENUMBER)=TRIM(ENDO_AMT.REF_NO(+))  AND BOE.OUTWARDREFERENCENUMBER = ? ";

      loggableStatement = new LoggableStatement(con, query);

      loggableStatement.setString(1, outWardReferenceNo);

      resultSet = loggableStatement.executeQuery();

      if (resultSet.next()) {

        outStandingAmt = resultSet.getDouble("AMOUNT_UNUTILIZED");

      }

    }

    catch (Exception e)

    {

      e.printStackTrace();

    }

    finally

    {

      DBConnectionUtility.surrenderDB(null, loggableStatement, resultSet);

    }

    return outStandingAmt;

  }

  public double getOutStandingAmountFromClosure(Connection con, String outWardReferenceNo)

  {

    LoggableStatement loggableStatement = null;

    ResultSet resultSet = null;

    double closedAmt = 0.0D;

    try

    {

      loggableStatement = new LoggableStatement(con, "SELECT NVL(SUM(TCLOSAMT),'0') AS TCLOSAMT FROM ETT_IDPMS_DATA_EC WHERE TOUTWARDREFERENCENUMBER = ? AND TSTATUS != 'R' ");

      loggableStatement.setString(1, outWardReferenceNo);

      resultSet = loggableStatement.executeQuery();

      if (resultSet.next()) {

        closedAmt = resultSet.getDouble("TCLOSAMT");

      }

    }

    catch (Exception e)

    {

      e.printStackTrace();

    }

    finally

    {

      DBConnectionUtility.surrenderDB(null, loggableStatement, resultSet);

    }

    return closedAmt;

  }
  public int insertOrm(IdpmsOrmVO idvo)
		    throws DAOException
		  {
		    logger.info("---------------insertOrm-------------");
		    Connection con = null;
		    LoggableStatement pst = null;
		    ResultSet rs = null;
		    CommonMethods commonMethods = null;
		    int result = 0;
		    try
		    {
		      con = DBConnectionUtility.getConnection();
		      commonMethods = new CommonMethods();
		      HttpSession session = ServletActionContext.getRequest().getSession();
		      String makerId = (String)session.getAttribute("loginedUserName");

		 
		      logger.info("Insert ORM-----------------maker id----------" + makerId);
		      int ormCount = 0;
		      String ormCountQuery = "SELECT COUNT(*) AS ORM_COUNT FROM ETT_IDPMS_DATA_EC WHERE TOUTWARDREFERENCENUMBER = ? AND TSTATUS != 'A' ORDER BY MAKER_TIMESTAMP DESC ";
		      LoggableStatement ps = new LoggableStatement(con, ormCountQuery);
		      ps.setString(1, idvo.getOrefNum());
		      ResultSet rst = ps.executeQuery();
		      logger.info("insertOrm--------COUNT---------ETT_IDPMS_DATA_EC-------query-----------" + ps.getQueryString());
		      if (rst.next()) {
		        ormCount = rst.getInt("ORM_COUNT");
		      }
		      closeStatementResultSet(ps, rst);
		      if (ormCount > 0)
		      {
		        String ormClosureSeqNo = null;
		        String ormXkeyQuery = "SELECT XKEY AS ORM_CLO_SEQ FROM ETT_IDPMS_DATA_EC WHERE TOUTWARDREFERENCENUMBER = ? AND TSTATUS != 'A' ORDER BY MAKER_TIMESTAMP DESC ";
		        LoggableStatement ps1 = new LoggableStatement(con, ormXkeyQuery);
		        ps1.setString(1, idvo.getOrefNum());
		        logger.info("insertOrm--------COUNT---------ETT_IDPMS_DATA_EC-------query-----------" + ps.getQueryString());
		        ResultSet rst1 = ps1.executeQuery();
		        if (rst1.next()) {
		          ormClosureSeqNo = rst1.getString("ORM_CLO_SEQ");
		        }
		        closeStatementResultSet(ps1, rst1);
		        pst = new LoggableStatement(con, "UPDATE ETT_IDPMS_DATA_EC SET TADCODE = ?,TCURRENCYCODE = ?,TCLOSAMT = ?,TCLOSDAT = TO_DATE(?,'DD/MM/YY'),TIECODE = ?,TSWIFTMESSAGE = ?,TRECORDINDICATOR = ?,TADJCLOINDI = ?,APPROVEDBY = ?,TLETTERNO = ?,TLETTERDAT = TO_DATE(?,'DD/MM/YY'),TDOCNUM = ?,TDOCDAT = TO_DATE(?,'DD/MM/YY'),TPORTCODE = ?,TREMARKS = ?,MAKER_TIMESTAMP = SYSTIMESTAMP,TSTATUS = 'P',MAKER_USERID = ?,TUNNTILIZEDAMT = ?  WHERE TOUTWARDREFERENCENUMBER = ? AND XKEY = ?  ");
		        pst.setString(1, commonMethods.getEmptyIfNull(idvo.getOadcode()).trim());
		        pst.setString(2, commonMethods.getEmptyIfNull(idvo.getOcurrcode()).trim());
		        pst.setString(3, commonMethods.getEmptyIfNull(idvo.getOclosamt()).trim());
		        pst.setString(4, commonMethods.getEmptyIfNull(idvo.getOclosdat()).trim());
		        pst.setString(5, commonMethods.getEmptyIfNull(idvo.getOiecode()).trim());
		        pst.setString(6, commonMethods.getEmptyIfNull(idvo.getOswifmsg().trim()));
		        pst.setString(7, commonMethods.getEmptyIfNull(idvo.getRecInd()).trim());
		        pst.setString(8, commonMethods.getEmptyIfNull(idvo.getOadjcloind()).trim());
		        pst.setString(9, commonMethods.getEmptyIfNull(idvo.getApprovedBy()).trim());
		        pst.setString(10, commonMethods.getEmptyIfNull(idvo.getOcltrno()).trim());
		        pst.setString(11, commonMethods.getEmptyIfNull(idvo.getOltrdate()).trim());
		        pst.setString(12, commonMethods.getEmptyIfNull(idvo.getOdocnum()).trim());
		        pst.setString(13, commonMethods.getEmptyIfNull(idvo.getOdocdate()).trim());
		        pst.setString(14, commonMethods.getEmptyIfNull(idvo.getOcprtcde()).trim());
		        pst.setString(15, commonMethods.getEmptyIfNull(idvo.getRemarks()).trim());
		        pst.setString(16, commonMethods.getEmptyIfNull(makerId).trim());
		        pst.setString(17, commonMethods.getEmptyIfNull(Double.valueOf(idvo.getUnUtilAmt())).trim());
		        pst.setString(18, commonMethods.getEmptyIfNull(idvo.getOrefNum()).trim());
		        pst.setString(19, commonMethods.getEmptyIfNull(ormClosureSeqNo).trim());
		        result = pst.executeUpdate();
		        logger.info("insertOrm--------COUNT---------UPDATEORMCLOSURE-------query-----------" + ps.getQueryString());
		      }
		      else
		      {
		        pst = new LoggableStatement(con, "INSERT INTO ETT_IDPMS_DATA_EC(TOUTWARDREFERENCENUMBER,TADCODE,TCURRENCYCODE,TCLOSAMT,TCLOSDAT,TIECODE,TSWIFTMESSAGE,TRECORDINDICATOR,TADJCLOINDI,APPROVEDBY,TLETTERNO,TLETTERDAT,TDOCNUM,TDOCDAT,TPORTCODE,TREMARKS,MAKER_TIMESTAMP,TSTATUS,XKEY,MAKER_USERID,TUNNTILIZEDAMT) values(?,?,?,?,TO_DATE(?,'DD/MM/YY'),?,?,?,?,?,?,TO_DATE(?,'DD/MM/YY'),?,TO_DATE(?,'DD/MM/YY'),?,?,SYSTIMESTAMP,'P',ORM_CLO_SEQ_NO,?,?)");
		        pst.setString(1, commonMethods.getEmptyIfNull(idvo.getOrefNum()).trim());
		        pst.setString(2, commonMethods.getEmptyIfNull(idvo.getOadcode()).trim());
		        pst.setString(3, commonMethods.getEmptyIfNull(idvo.getOcurrcode()).trim());
		        pst.setString(4, commonMethods.getEmptyIfNull(idvo.getOclosamt()).trim());
		        pst.setString(5, commonMethods.getEmptyIfNull(idvo.getOclosdat()).trim());
		        pst.setString(6, commonMethods.getEmptyIfNull(idvo.getOiecode()).trim());
		        pst.setString(7, commonMethods.getEmptyIfNull(idvo.getOswifmsg().trim()));
		        pst.setString(8, commonMethods.getEmptyIfNull(idvo.getRecInd()).trim());
		        pst.setString(9, commonMethods.getEmptyIfNull(idvo.getOadjcloind()).trim());
		        pst.setString(10, commonMethods.getEmptyIfNull(idvo.getApprovedBy()).trim());
		        pst.setString(11, commonMethods.getEmptyIfNull(idvo.getOcltrno()).trim());
		        pst.setString(12, commonMethods.getEmptyIfNull(idvo.getOltrdate()).trim());
		        pst.setString(13, commonMethods.getEmptyIfNull(idvo.getOdocnum()).trim());
		        pst.setString(14, commonMethods.getEmptyIfNull(idvo.getOdocdate()).trim());
		        pst.setString(15, commonMethods.getEmptyIfNull(idvo.getOcprtcde()).trim());
		        pst.setString(16, commonMethods.getEmptyIfNull(idvo.getRemarks()).trim());
		        pst.setString(17, commonMethods.getEmptyIfNull(makerId).trim());
		        pst.setString(18, commonMethods.getEmptyIfNull(Double.valueOf(idvo.getUnUtilAmt())).trim());
		        logger.info("insertOrm--------INSERTORMCLOSURE-------query-----------" + ps.getQueryString());
		        result = pst.executeUpdate();
		      }
		    }
		    catch (Exception e)
		    {
		      logger.info("insertOrm--------------------Exception------" + e);
		      throwDAOException(e);
		    }
		    finally
		    {
		      DBConnectionUtility.surrenderDB(con, pst, rs);
		    }
		    return result;
		  }
  public ArrayList<IdpmsOrmVO> idpmsOrmList(BOEDataSearchVO boeSearchVO)
  {
    logger.info("--------------------idpmsOrmList--------------");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    String query = null;
    ArrayList<IdpmsOrmVO> idpmsOrmList = null;
    CommonMethods commonMethods = null;
    try
    {
      con = DBConnectionUtility.getConnection();
      idpmsOrmList = new ArrayList();
      commonMethods = new CommonMethods();
      HttpSession session = ServletActionContext.getRequest().getSession();
      String userId = (String)session.getAttribute("loginedUserId");
      logger.info("idpmsOrmList---------User id" + userId);

 
      query = "SELECT XKEY,TOUTWARDREFERENCENUMBER,TADCODE,TCLOSAMT,TCURRENCYCODE,TIECODE FROM ETT_IDPMS_DATA_EC WHERE TSTATUS = 'P' ";

 
      String ormNo = commonMethods.getEmptyIfNull(boeSearchVO.getOrmNumber()).trim();
      if ((!commonMethods.isNull(ormNo)) && (!commonMethods.isNull(userId))) {
        query = query + "AND TOUTWARDREFERENCENUMBER = ? AND MAKER_USERID != ?";
      } else if (!commonMethods.isNull(ormNo)) {
        query = query + "AND  MAKER_USERID != ?";
      }
      pst = new LoggableStatement(con, query);
      if ((!commonMethods.isNull(ormNo)) && (!commonMethods.isNull(userId)))
      {
        pst.setString(1, ormNo);
        pst.setString(2, userId);
      }
      else if (!commonMethods.isNull(ormNo))
      {
        pst.setString(1, ormNo);
      }
      logger.info("idpmsOrmList----Query-----" + pst.getQueryString());
      rs = pst.executeQuery();
      while (rs.next())
      {
        IdpmsOrmVO idvo = new IdpmsOrmVO();
        idvo.setOrefNum(commonMethods.getEmptyIfNull(rs.getString("TOUTWARDREFERENCENUMBER")).trim());
        idvo.setOadcode(commonMethods.getEmptyIfNull(rs.getString("TADCODE")).trim());
        idvo.setOamt(commonMethods.getEmptyIfNull(rs.getString("TCLOSAMT")).trim());
        idvo.setOcurrcode(commonMethods.getEmptyIfNull(rs.getString("TCURRENCYCODE")).trim());
        idvo.setOiecode(commonMethods.getEmptyIfNull(rs.getString("TIECODE")).trim());
        idvo.setExKey(commonMethods.getEmptyIfNull(rs.getString("XKEY")).trim());
        idpmsOrmList.add(idvo);
      }
    }
    catch (Exception e)
    {
      logger.info("idpmsOrmList----Query----- Exception ____" + e);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    return idpmsOrmList;
  }
  public ArrayList<IdpmsOrmVO> insertOrm(String[] ormChkList, String ormStatus, String remarks, BOEDataSearchVO boeSearchVO)
  {
    logger.info("---------------insertOrm----------------");
    LoggableStatement pst = null;
    Connection con = null;
    String query = null;
    ArrayList<IdpmsOrmVO> alist = null;
    try
    {
      con = DBConnectionUtility.getConnection();
      alist = new ArrayList();
      HttpSession session = ServletActionContext.getRequest().getSession();
      String checkerId = (String)session.getAttribute("loginedUserId");
      for (int i = 0; i < ormChkList.length; i++)
      {
        query = "UPDATE ETT_IDPMS_DATA_EC SET TSTATUS = ?,CREMARKS = ?,CHECKER_USERID = ?,CHECKER_TIMESTAMP = SYSTIMESTAMP WHERE XKEY = ? ";
        pst = new LoggableStatement(con, query);
        pst.setString(1, ormStatus);
        pst.setString(2, remarks);
        pst.setString(3, checkerId);
        pst.setString(4, ormChkList[i]);
        logger.info("insertOrm       Query---------udpate----" + pst.getQueryString());
        int a = pst.executeUpdate();
        logger.info("insertOrm  count---" + a);
      }
      alist = idpmsOrmList(boeSearchVO);
    }
    catch (Exception e)
    {
      logger.info("insertOrm----Exception" + e);
    }
    finally
    {
      closeSqlRefferance(pst, con);
    }
    logger.info("Exiting Method");
    return alist;
  }
  public IdpmsOrmVO ormVIEW(BOEDataSearchVO boeSearchVO)
		    throws DAOException
		  {
		    logger.info("-----ormVIEW------------");
		    LoggableStatement pst = null;
		    ResultSet rs = null;
		    Connection con = null;
		    String query = null;
		    IdpmsOrmVO idpmsOrmVO = null;
		    try
		    {
		      con = DBConnectionUtility.getConnection();
		      query = "SELECT TOUTWARDREFERENCENUMBER,TADCODE,TCLOSAMT,TCURRENCYCODE,TIECODE,TSWIFTMESSAGE, TRECORDINDICATOR,TREMARKS,TLETTERNO,TO_CHAR(TLETTERDAT,'DD/MM/YYYY') AS TLETTERDAT, TO_CHAR(TCLOSDAT,'DD/MM/YYYY') AS TCLOSDAT,TDOCNUM,TO_CHAR(TDOCDAT,'DD/MM/YYYY') AS TDOCDAT, TADJCLOINDI,TPORTCODE,APPROVEDBY FROM ETT_IDPMS_DATA_EC WHERE XKEY = ? ";

		 
		 
		      pst = new LoggableStatement(con, query);
		      pst.setString(1, boeSearchVO.getUniqueNo());
		      logger.info("ormVIEW-----------------ormVIEW--Query--" + pst.getQueryString());
		      rs = pst.executeQuery();
		      if (rs.next())
		      {
		        idpmsOrmVO = new IdpmsOrmVO();
		        idpmsOrmVO.setOrefNum(rs.getString("TOUTWARDREFERENCENUMBER"));
		        idpmsOrmVO.setOadcode(rs.getString("TADCODE"));
		        idpmsOrmVO.setOclosamt(rs.getString("TCLOSAMT"));
		        idpmsOrmVO.setOcurrcode(rs.getString("TCURRENCYCODE"));
		        idpmsOrmVO.setOiecode(rs.getString("TIECODE"));
		        idpmsOrmVO.setOswifmsg(rs.getString("TSWIFTMESSAGE"));
		        String recInd = rs.getString("TRECORDINDICATOR");
		        if ((recInd != null) && (recInd.equalsIgnoreCase("1"))) {
		          idpmsOrmVO.setOrecind("NEW");
		        } else if ((recInd != null) && (recInd.equalsIgnoreCase("3"))) {
		          idpmsOrmVO.setOrecind("CANCEL");
		        }
		        idpmsOrmVO.setOutRemarks(rs.getString("TREMARKS"));
		        idpmsOrmVO.setOletrno(rs.getString("TLETTERNO"));
		        idpmsOrmVO.setOletrdate(rs.getString("TLETTERDAT"));
		        idpmsOrmVO.setOclosdat(rs.getString("TCLOSDAT"));
		        idpmsOrmVO.setOdocnum(rs.getString("TDOCNUM"));
		        idpmsOrmVO.setOdocdate(rs.getString("TDOCDAT"));

		 
		        String adjInd = rs.getString("TADJCLOINDI");
		        logger.info("Welcome to BOE : " + adjInd);
		        if ((adjInd != null) && (adjInd.equalsIgnoreCase("1"))) {
		          idpmsOrmVO.setOadjcloind("Refund of full import proceeds( import not taken place)");
		        } else if ((adjInd != null) && (adjInd.equalsIgnoreCase("2"))) {
		          idpmsOrmVO.setOadjcloind("Import document( BE not in system),");
		        } else if ((adjInd != null) && (adjInd.equalsIgnoreCase("3"))) {
		          idpmsOrmVO.setOadjcloind("Refund of untilised import payments due to quality issue/sort shipment");
		        } else if ((adjInd != null) && (adjInd.equalsIgnoreCase("4"))) {
		          idpmsOrmVO.setOadjcloind("Others");
		        } else if ((adjInd != null) && (adjInd.equalsIgnoreCase("5"))) {
		          idpmsOrmVO.setOadjcloind("BOE Prior To '01/04/2016'");
		        } else if ((adjInd != null) && (adjInd.equalsIgnoreCase("6"))) {
		          idpmsOrmVO.setOadjcloind("BoE Waiver");
		        } else if ((adjInd != null) && (adjInd.equalsIgnoreCase("7"))) {
		          idpmsOrmVO.setOadjcloind("Import through courier");
		        }
		        idpmsOrmVO.setOportcode(rs.getString("TPORTCODE"));
		        String appBy = rs.getString("APPROVEDBY");
		        if ((appBy != null) && (appBy.equalsIgnoreCase("1"))) {
		          idpmsOrmVO.setApprovedBy("RBI");
		        } else if ((appBy != null) && (appBy.equalsIgnoreCase("2"))) {
		          idpmsOrmVO.setApprovedBy("AD Bank");
		        } else if ((appBy != null) && (appBy.equalsIgnoreCase("3"))) {
		          idpmsOrmVO.setApprovedBy("Other");
		        }
		      }
		    }
		    catch (Exception exception)
		    {
		      logger.info("ormVIEW----------------exception--" + exception);
		      throwDAOException(exception);
		    }
		    finally
		    {
		      DBConnectionUtility.surrenderDB(con, pst, rs);
		    }
		    logger.info("Exiting Method");
		    return idpmsOrmVO;
		  }
  public boolean checkOrmOutstandingAmount(String ormNo, String closureAmt)
		    throws DAOException
		  {
		    logger.info("checkOrmOutstandingAmount-------------------");
		    Connection con = null;
		    LoggableStatement loggableStatemnt = null;
		    ResultSet resultSet = null;
		    double outStandingAmt = 0.0D;
		    double alreadyClosed = 0.0D;
		    try
		    {
		      con = DBConnectionUtility.getConnection();
		      String query = " SELECT CASE WHEN (NVL(BOE.AMOUNT,0) - NVL(ENDO_AMT.ENDORSE_AMT,0)- NVL(EID.TCLOSAMT,0)) < 0 THEN 0 ELSE (NVL(BOE.AMOUNT,0) - NVL(ENDO_AMT.ENDORSE_AMT,0)- NVL(EID.TCLOSAMT,0)) END AS AMOUNT_UNUTILIZED  FROM ETT_IDPMS_EOD_DATA BOE,  (SELECT TOUTWARDREFERENCENUMBER,SUM(NVL(TCLOSAMT,0)) AS TCLOSAMT FROM ETT_IDPMS_DATA_EC GROUP BY TOUTWARDREFERENCENUMBER)  EID,  (SELECT TRIM(BOE_PAYMENT_BP_PAY_REF)||TRIM(BOE_PAYMENT_BP_PAY_PART_REF) AS REF_NO,SUM(NVL(BOE_PAYMENT_BP_PAY_ENDORSE_AMT,0)) AS ENDORSE_AMT  FROM ETT_BOE_PAYMENT WHERE STATUS = 'A' GROUP BY TRIM(BOE_PAYMENT_BP_PAY_REF)||TRIM(BOE_PAYMENT_BP_PAY_PART_REF))  ENDO_AMT  WHERE TRIM(BOE.OUTWARDREFERENCENUMBER)=TRIM(EID.TOUTWARDREFERENCENUMBER(+)) AND TRIM(BOE.OUTWARDREFERENCENUMBER)=TRIM(ENDO_AMT.REF_NO(+))  AND BOE.OUTWARDREFERENCENUMBER = ? ";
		      loggableStatemnt = new LoggableStatement(con, query);
		      loggableStatemnt.setString(1, ormNo);
		      logger.info("checkOrmOutstandingAmount---------------Query-" + loggableStatemnt.getQueryString());
		      resultSet = loggableStatemnt.executeQuery();
		      if (resultSet.next())
		      {
		        outStandingAmt = resultSet.getDouble("AMOUNT_UNUTILIZED");
		        logger.info("outStandingAmt----------------" + outStandingAmt);
		      }
		      logger.info("closureAmt :: " + closureAmt + " :: outStandingAmt :: " + outStandingAmt);
		      if (Double.valueOf(closureAmt).doubleValue() > Double.valueOf(outStandingAmt).doubleValue()) {
		        return true;
		      }
		      return false;
		    }
		    catch (Exception e)
		    {
		      logger.info("checkOrmOutstandingAmount-----------------------" + e);
		      throwDAOException(e);
		    }
		    finally
		    {
		      DBConnectionUtility.surrenderDB(con, loggableStatemnt, resultSet);
		    }
		    return false;
		  }
		  public int getOrmCount(IdpmsOrmVO Indvo)
		    throws DAOException
		  {
		    logger.info("---------getOrmCount----------");
		    Connection con = null;
		    LoggableStatement pst = null;
		    ResultSet rs = null;
		    int ormCount = 0;
		    try
		    {
		      con = DBConnectionUtility.getConnection();
		      String query = "SELECT COUNT(*) AS ORM_COUNT FROM ETT_IDPMS_DATA_EC  WHERE TOUTWARDREFERENCENUMBER = ? AND TSTATUS = 'P' ";

		 
		      pst = new LoggableStatement(con, query);
		      pst.setString(1, Indvo.getOrefNum());
		      logger.info("query-----------------------------" + pst.getQueryString());
		      rs = pst.executeQuery();
		      if (rs.next()) {
		        ormCount = rs.getInt("ORM_COUNT");
		      }
		    }
		    catch (Exception e)
		    {
		      logger.info("getOrmCount----------Exception------" + e);
		      throwDAOException(e);
		    }
		    finally
		    {
		      closeSqlRefferance(rs, pst, con);
		    }
		    logger.info("Exiting Method");
		    return ormCount;
		  }
		  public int getORMAckCount(IdpmsOrmVO Indvo)
		    throws DAOException
		  {
		    logger.info("-----------getORMAckCount-------------------");
		    Connection con = null;
		    LoggableStatement pst = null;
		    ResultSet rs = null;
		    int ormCount = 0;
		    try
		    {
		      con = DBConnectionUtility.getConnection();
		      String datevalid = "01-09-2022";
		      String txnDate1 = Indvo.getOpaymdate();
		      logger.info("datevalid :: " + datevalid + " :: txnDate1 :: " + txnDate1);
		      SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		      SimpleDateFormat sdf1 = null;
		      if (txnDate1.length() == 10)
		      {
		        if (txnDate1.contains("/")) {
		          sdf1 = new SimpleDateFormat("dd/MM/yyyy");
		        } else {
		          sdf1 = new SimpleDateFormat("dd-MM-yyyy");
		        }
		      }
		      else {
		        sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		      }
		      Date staticDate = sdf.parse(datevalid);
		      Date txnDate = sdf1.parse(txnDate1);
		      logger.info("staticDate :: " + staticDate + " :: txnDate :: " + txnDate);
		      if (txnDate.before(staticDate))
		      {
		        ormCount = -1;
		      }
		      else
		      {
		        String query = "SELECT COUNT(*) AS ORM_COUNT FROM ETT_ORM_ACK  WHERE OUTWARDREFERNECNO = ? AND UPPER(ERRORCODES) = 'SUCCESS' ";

		 
		        pst = new LoggableStatement(con, query);
		        logger.info("query-------------" + pst.getQueryString());
		        pst.setString(1, Indvo.getOrefNum());
		        rs = pst.executeQuery();
		        if (rs.next())
		        {
		          ormCount = rs.getInt("ORM_COUNT");
		          logger.info("ormCount-------------" + ormCount);
		        }
		      }
		    }
		    catch (Exception e)
		    {
		      logger.info("getORMAckCount-------------------" + e);
		      throwDAOException(e);
		    }
		    finally
		    {
		      closeSqlRefferance(rs, pst, con);
		    }
		    logger.info("Exiting Method");
		    return ormCount;
		  }
		}