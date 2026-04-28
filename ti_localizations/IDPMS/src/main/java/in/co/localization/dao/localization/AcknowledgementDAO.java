package in.co.localization.dao.localization;
 
import in.co.localization.dao.AbstractDAO;
import in.co.localization.dao.exception.DAOException;
import in.co.localization.utility.ActionConstantsQuery;
import in.co.localization.utility.CommonMethods;
import in.co.localization.utility.DBConnectionUtility;
import in.co.localization.utility.LoggableStatement;
import in.co.localization.vo.localization.AcknowledgementVO;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
 
public class AcknowledgementDAO
  extends AbstractDAO
  implements ActionConstantsQuery
{
  static AcknowledgementDAO dao;
  String bType = null;
  private static Logger logger = Logger.getLogger(AcknowledgementDAO.class.getName());
  public static AcknowledgementDAO getDAO()
  {
    if (dao == null) {
      dao = new AcknowledgementDAO();
    }
    return dao;
  }
  public AcknowledgementVO fetchOrmData(AcknowledgementVO ackVO)
    throws DAOException
  {
    logger.info("--------------fetchOrmData--------------");
    int successCount = 0;
    int failCount = 0;
    int iCount = 0;
    String sInParam = "";
    String errorDesc = "";
    String errorDesc1 = "";
    String sEerrDescQuery = "";
    String errorCode = "";
    CommonMethods commonMethods = null;
    String ormNo = "";
    String paymDate = "";
    String adCodde = "";
    String ieCodde = "";
    String sAckStatus = null;
    String ORM_QUERY = null;
    String ORM_QUERY1 = null;
    ArrayList<AcknowledgementVO> ormAckList = null;
    LoggableStatement pst = null;
    LoggableStatement pst1 = null;
    ResultSet rs = null;
    ResultSet rs1 = null;
    Connection con = null;
    boolean ormNoFlag = false;
    boolean paymDateFlag = false;
    boolean adCoddeFlag = false;
    boolean ieCoddeFlag = false;
    int setValue = 0;
    try
    {
      con = DBConnectionUtility.getConnection();
      ormAckList = new ArrayList();

 
 
      commonMethods = new CommonMethods();
      sAckStatus = ackVO.getAckStatus();
      ORM_QUERY = "SELECT OUTWARDREFERNECNO,ADCODE,AMOUNT,CURR,TO_CHAR(TO_DATE(PAYDATE,'DD-MM-YY'),'DD/MM/YYYY') AS PAYDATE, IECODE,IENAME,IEADDRESS,IEPANNO,ISCAPITAL,BENNAME,BENACCNO,BENCOUNTRY,SWIFT,PURPCODE,RECORDINDICATOR,REMARKS,\tPAYTERMS,ERRORCODES FROM ETT_ORM_ACK WHERE OUTWARDREFERNECNO = OUTWARDREFERNECNO ";

 
 
      ormNo = ackVO.getOutRemNo();
      if (!commonMethods.isNull(ormNo))
      {
        ORM_QUERY = ORM_QUERY + " AND OUTWARDREFERNECNO = '" + ormNo.trim() + "'";
        ormNoFlag = true;
      }
      paymDate = commonMethods.getEmptyIfNull(ackVO.getPaymDate());
      if (!commonMethods.isNull(paymDate))
      {
        ORM_QUERY = ORM_QUERY + " AND TO_CHAR(TO_DATE(PAYDATE,'DD-MM-YY'),'DD/MM/YYYY') = '" + paymDate.trim() + "'";
        paymDateFlag = true;
      }
      adCodde = ackVO.getAdCodde();
      if (!commonMethods.isNull(adCodde))
      {
        ORM_QUERY = ORM_QUERY + " AND ADCODE =  '" + adCodde.trim() + "'";
        adCoddeFlag = true;
      }
      ieCodde = ackVO.getIeCodde();
      if (!commonMethods.isNull(ieCodde))
      {
        ORM_QUERY = ORM_QUERY + " AND IECODE =  '" + ieCodde.trim() + "'";
        ieCoddeFlag = true;
      }
      if (sAckStatus.equals("S"))
      {
        ORM_QUERY = ORM_QUERY + " AND ERRORCODES = 'SUCCESS'";
      }
      else if (sAckStatus.equals("F"))
      {
        ORM_QUERY = ORM_QUERY + " AND OUTWARDREFERNECNO NOT IN (SELECT OUTWARDREFERNECNO FROM ETT_ORM_ACK WHERE ERRORCODES = 'SUCCESS')";
      }
      else
      {
        ORM_QUERY1 = ORM_QUERY.toString();
        ORM_QUERY = ORM_QUERY + " AND ERRORCODES = 'SUCCESS' UNION ALL ";
        ORM_QUERY1 = ORM_QUERY1 + " AND OUTWARDREFERNECNO NOT IN (SELECT OUTWARDREFERNECNO FROM ETT_ORM_ACK WHERE ERRORCODES = 'SUCCESS')";
        ORM_QUERY = ORM_QUERY + ORM_QUERY1.toString();
      }
      pst = new LoggableStatement(con, ORM_QUERY);

 
      logger.info("--------------fetchOrmData-------query-------" + pst.getQueryString());
      rs = pst.executeQuery();
      while (rs.next())
      {
        ackVO = new AcknowledgementVO();
        ackVO.setOrmNo(rs.getString("OUTWARDREFERNECNO"));
        ackVO.setAdcode(rs.getString("ADCODE"));
        ackVO.setAmount(rs.getString("AMOUNT"));
        ackVO.setCurr(rs.getString("CURR"));
        ackVO.setPayDate(rs.getString("PAYDATE"));
        ackVO.setIeCode(rs.getString("IECODE"));
        ackVO.setIeName(rs.getString("IENAME"));
        ackVO.setIeAddress(rs.getString("IEADDRESS"));
        ackVO.setIePanNo(rs.getString("IEPANNO"));
        ackVO.setIsCapital(rs.getString("ISCAPITAL"));
        ackVO.setBenName(rs.getString("BENNAME"));
        ackVO.setBenAccNo(rs.getString("BENACCNO"));
        ackVO.setBenCountry(rs.getString("BENCOUNTRY"));
        ackVO.setSwift(rs.getString("SWIFT"));
        ackVO.setPurpCode(rs.getString("PURPCODE"));
        ackVO.setRecordInd(rs.getString("RECORDINDICATOR"));
        ackVO.setRemarks(rs.getString("REMARKS"));
        ackVO.setPayTerms(rs.getString("PAYTERMS"));
        if (rs.getString("ERRORCODES") != null) {
          errorCode = rs.getString("ERRORCODES");
        }
        if (ormNoFlag) {
          ackVO.setOutRemNo(rs.getString("OUTWARDREFERNECNO"));
        }
        if (paymDateFlag) {
          ackVO.setPaymDate(rs.getString("PAYDATE"));
        }
        if (adCoddeFlag) {
          ackVO.setAdCodde(rs.getString("ADCODE"));
        }
        if (ieCoddeFlag) {
          ackVO.setIeCodde(rs.getString("IECODE"));
        }
        ackVO.setErrorDes(errorCode);

 
        ArrayList<String> alStr = new ArrayList();
        ArrayList<String> alStrDB = new ArrayList();
        String[] splitErrorCodes = { "" };
        if (errorCode.contains(","))
        {
          splitErrorCodes = errorCode.split(",");
          sInParam = "";
          for (int j = 0; j < splitErrorCodes.length; j++)
          {
            alStr.add(splitErrorCodes[j]);
            if (sInParam.equals("")) {
              sInParam = "'" + splitErrorCodes[j] + "'";
            } else {
              sInParam = sInParam + ",'" + splitErrorCodes[j] + "'";
            }
          }
        }
        else
        {
          alStr.add(errorCode);
          sInParam = "'" + errorCode + "'";
        }
        sEerrDescQuery = "SELECT Trim(ERRCO) as ERRCO, ERRCO||'-'||ERRMSG ERRCO FROM (SELECT ERROR_CODE ERRCO, ERROR_MSG ERRMSG FROM ETT_IDPMS_ERROR_CODES WHERE trim(ERROR_CODE) IN (?) AND PCATEGORY = 'ORM')";
        pst1 = new LoggableStatement(con, sEerrDescQuery);
        pst1.setString(1, sInParam);
        logger.info("--------------fetchOrmData---111111----query-------" + pst1.getQueryString());
        rs1 = pst1.executeQuery();
        while (rs1.next())
        {
          alStrDB.add(rs1.getString(1));
          errorDesc = rs1.getString(2);
          if (errorDesc1.isEmpty()) {
            errorDesc1 = errorDesc.trim();
          } else {
            errorDesc1 = errorDesc1 + ",\n" + errorDesc.trim();
          }
        }
        for (String temp : alStr) {
          if (!alStrDB.contains(temp)) {
            if (errorDesc1.isEmpty()) {
              errorDesc1 = temp.trim();
            } else {
              errorDesc1 = errorDesc1 + ",\n" + temp.trim();
            }
          }
        }
        ackVO.setErrorDesc(errorDesc1);
        errorDesc = "";
        errorDesc1 = "";
        rs1.close();
        pst1.close();
        if (errorCode.equals("SUCCESS"))
        {
          ackVO.setStatusField(errorCode);
          successCount++;
        }
        if (!errorCode.equals("SUCCESS"))
        {
          ackVO.setStatusField("FAIL");
          failCount++;
        }
        ormAckList.add(ackVO);
        iCount = ormAckList.size();
      }
      ackVO.setAckStatus(sAckStatus);
      ackVO.setSuccessCount(successCount);
      ackVO.setFailCount(failCount);
      ackVO.setOrmAckValues(ormAckList);
      ackVO.setTotalCount(iCount);
    }
    catch (Exception exception)
    {
      logger.info("--------------fetchOrmData------exception--------" + exception);
      exception.printStackTrace();
      throwDAOException(exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    logger.info("Exiting Method");
    return ackVO;
  }
  public AcknowledgementVO fetchBesData(AcknowledgementVO ackVO)

		    throws DAOException

		  {

		    logger.info("Entering Method");

		    int successCount = 0;

		    int failCount = 0;

		    int iCount = 0;

		    String sInParam = "";

		    String errorDesc = "";

		    String errorDesc1 = "";

		    String sEerrDescQuery = "";

		    String errorCode = "";

		    ArrayList<AcknowledgementVO> besAckList = null;

		    LoggableStatement pst = null;

		    ResultSet rs = null;

		    Connection con = null;

		    String query = null;

		    LoggableStatement pst1 = null;

		    ResultSet rs1 = null;

		    CommonMethods commonMethods = null;

		    String BES_QUERY = null;

		    String BES_QUERY1 = null;

		    String sAckStatus = "";

		    boolean outRefNoFlag = false;

		    boolean boeNoFlag = false;

		    boolean boeDateFlag = false;

		    boolean portcodeFlag = false;

		    boolean payrefnoFlag = false;

		    int setValue = 0;

		    try

		    {

		      con = DBConnectionUtility.getConnection();

		 
		      besAckList = new ArrayList();

		 
		 
		      sAckStatus = ackVO.getAckStatus();

		      commonMethods = new CommonMethods();

		      BES_QUERY = "SELECT PORTOFDISCHARGE,BOENUMBER,TO_CHAR(TO_DATE(BOEDATE,'DD-MM-YY'),'DD/MM/YYYY') AS BOEDATE,IECODE,CHANGEIECODE,ADCODE,RECORDINDICATOR,PAYMENTPARTY,PAYMENTREFNUMBER,ORNUMBER,ORADCODE,REMITTCUR,BILLCLOSUREINDI,INVOICESERIALNO,INVOICENO,INVOICEAMTIC,INVOICEAMT,ERRORCODES FROM ETT_BES_ACK WHERE BOENUMBER = BOENUMBER  ";

		 
		 
		      String outRefNo = ackVO.getOrmNo();

		      if (!commonMethods.isNull(outRefNo))

		      {

		        BES_QUERY = BES_QUERY + " AND TRIM(ORNUMBER) ='" + outRefNo.trim() + "'";

		        outRefNoFlag = true;

		      }

		      String boeNo = ackVO.getBoeNoEntered();

		      if (!commonMethods.isNull(boeNo))

		      {

		        BES_QUERY = BES_QUERY + " AND TRIM(BOENUMBER) ='" + boeNo.trim() + "'";

		        boeNoFlag = true;

		      }

		      String boeDate = commonMethods.getEmptyIfNull(ackVO.getBoeDateEntered());

		      if (!commonMethods.isNull(boeDate))

		      {

		        BES_QUERY = BES_QUERY + " AND TO_CHAR(TO_DATE(BOEDATE,'DD-MM-YY'),'DD/MM/YYYY') ='" + boeDate.trim() + "'";

		        boeDateFlag = true;

		      }

		      String portcode = commonMethods.getEmptyIfNull(ackVO.getBoePortEntered());

		      if (!commonMethods.isNull(portcode))

		      {

		        BES_QUERY = BES_QUERY + " AND TRIM(PORTOFDISCHARGE) = '" + portcode.trim() + "'";

		        portcodeFlag = true;

		      }

		      String payrefno = ackVO.getBoePayRefEntered();

		      if (!commonMethods.isNull(payrefno))

		      {

		        BES_QUERY = BES_QUERY + " AND TRIM(PAYMENTREFNUMBER) = '" + payrefno.trim() + "'";

		        payrefnoFlag = true;

		      }

		      if (sAckStatus.equals("S"))

		      {

		        BES_QUERY = BES_QUERY + " AND ERRORCODES = 'SUCCESS'";

		      }

		      else if (sAckStatus.equals("F"))

		      {

		        BES_QUERY = BES_QUERY + " AND PAYMENTREFNUMBER NOT IN (SELECT PAYMENTREFNUMBER FROM ETT_BES_ACK WHERE ERRORCODES = 'SUCCESS')";

		      }

		      else

		      {

		        BES_QUERY1 = BES_QUERY.toString();

		        BES_QUERY = BES_QUERY + " AND ERRORCODES = 'SUCCESS' UNION ALL ";

		        BES_QUERY1 = BES_QUERY1 + " AND PAYMENTREFNUMBER NOT IN (SELECT PAYMENTREFNUMBER FROM ETT_BES_ACK WHERE ERRORCODES = 'SUCCESS')";

		        BES_QUERY = BES_QUERY + BES_QUERY1.toString();

		      }

		      pst = new LoggableStatement(con, BES_QUERY);

		      rs = pst.executeQuery();

		      while (rs.next())

		      {

		        ackVO = new AcknowledgementVO();

		        ackVO.setPortDischarge(rs.getString("PORTOFDISCHARGE"));

		        ackVO.setBoeNo(rs.getString("BOENUMBER"));

		        ackVO.setBoeDate(rs.getString("BOEDATE"));

		        ackVO.setBesIECode(rs.getString("IECODE"));

		        ackVO.setChanIECode(rs.getString("CHANGEIECODE"));

		        ackVO.setBesADCode(rs.getString("ADCODE"));

		        ackVO.setBesrecordInd(rs.getString("RECORDINDICATOR"));

		        ackVO.setPaymentParty(rs.getString("PAYMENTPARTY"));

		        ackVO.setPaymentRefNo(rs.getString("PAYMENTREFNUMBER"));

		        ackVO.setOrNo(rs.getString("ORNUMBER"));

		        ackVO.setOrADCode(rs.getString("ORADCODE"));

		        ackVO.setRemitCurr(rs.getString("REMITTCUR"));

		        ackVO.setBesbillCloInd(rs.getString("BILLCLOSUREINDI"));

		        ackVO.setInvoiceSNo(rs.getString("INVOICESERIALNO"));

		        ackVO.setInvoiceNo(rs.getString("INVOICENO"));

		        ackVO.setInvoiceAmt(rs.getString("INVOICEAMT"));

		        ackVO.setInvoiceAmtIC(rs.getString("INVOICEAMTIC"));

		        if (outRefNoFlag) {

		          ackVO.setOrmNo(outRefNo);

		        }

		        if (boeNoFlag) {

		          ackVO.setBoeNoEntered(boeNo);

		        }

		        if (boeDateFlag) {

		          ackVO.setBoeDateEntered(boeDate);

		        }

		        if (portcodeFlag) {

		          ackVO.setBoePortEntered(portcode);

		        }

		        if (payrefnoFlag) {

		          ackVO.setBoePayRefEntered(payrefno);

		        }

		        if (rs.getString("ERRORCODES") != null) {

		          errorCode = rs.getString("ERRORCODES");

		        }

		        ackVO.setBesErrorCodes(errorCode);

		 
		 
		        ArrayList<String> alStr = new ArrayList();

		        ArrayList<String> alStrDB = new ArrayList();

		        String[] splitErrorCodes = { "" };

		        if (errorCode.contains(","))

		        {

		          splitErrorCodes = errorCode.split(",");

		          sInParam = "";

		          for (int j = 0; j < splitErrorCodes.length; j++)

		          {

		            alStr.add(splitErrorCodes[j]);

		            if (sInParam.equals("")) {

		              sInParam = "'" + splitErrorCodes[j] + "'";

		            } else {

		              sInParam = sInParam + ",'" + splitErrorCodes[j] + "'";

		            }

		          }

		        }

		        else

		        {

		          alStr.add(errorCode);

		          sInParam = "'" + errorCode + "'";

		        }

		        sEerrDescQuery = "SELECT Trim(ERRCO) as ERRCO, ERRCO||'-'||ERRMSG ERRCO FROM (SELECT ERROR_CODE ERRCO, ERROR_MSG ERRMSG FROM ETT_IDPMS_ERROR_CODES WHERE trim(ERROR_CODE) IN (?) AND PCATEGORY = 'BES')";

		        pst1 = new LoggableStatement(con, sEerrDescQuery);

		        pst1.setString(1, sInParam);

		        rs1 = pst1.executeQuery();

		        while (rs1.next())

		        {

		          alStrDB.add(rs1.getString(1));

		          errorDesc = rs1.getString(2);

		          if (errorDesc1.isEmpty()) {

		            errorDesc1 = errorDesc.trim();

		          } else {

		            errorDesc1 = errorDesc1 + ",\n" + errorDesc.trim();

		          }

		        }

		        for (String temp : alStr) {

		          if (!alStrDB.contains(temp)) {

		            if (errorDesc1.isEmpty()) {

		              errorDesc1 = temp.trim();

		            } else {

		              errorDesc1 = errorDesc1 + ",\n" + temp.trim();

		            }

		          }

		        }

		        ackVO.setErrorDesc(errorDesc1);

		        errorDesc = "";

		        errorDesc1 = "";

		        rs1.close();

		        pst1.close();

		        if (errorCode.equals("SUCCESS"))

		        {

		          ackVO.setStatusField(errorCode);

		          successCount++;

		        }

		        if (!errorCode.equals("SUCCESS"))

		        {

		          ackVO.setStatusField("FAIL");

		          failCount++;

		        }

		        besAckList.add(ackVO);

		        iCount = besAckList.size();

		      }

		      logger.info(ackVO.getOrmNo() + " :: " + ackVO.getBoeNoEntered() + " :: " + ackVO.getBoeDateEntered() + " :: " + ackVO.getBoePortEntered() + " :: " + ackVO.getBoePayRefEntered());

		      ackVO.setSuccessCount(successCount);

		      ackVO.setFailCount(failCount);

		      ackVO.setBesAckValues(besAckList);

		      ackVO.setTotalCount(iCount);

		    }

		    catch (Exception exception)

		    {

		      exception.printStackTrace();

		      throwDAOException(exception);

		    }

		    finally

		    {

		      DBConnectionUtility.surrenderDB(con, pst, rs);

		    }

		    logger.info("Exiting Method");

		    return ackVO;

		  }

  public AcknowledgementVO fetchBeeData(AcknowledgementVO ackVO)
		    throws DAOException
		  {
		    logger.info("--------------fetchBeeData-----------");
		    int iCount = 0;
		    int successCount = 0;
		    int failCount = 0;
		    String sInParam = "";
		    String errorDesc = "";
		    String errorDesc1 = "";
		    String sEerrDescQuery = "";
		    String errorCode = "";
		    ArrayList<AcknowledgementVO> beeAckList = null;
		    LoggableStatement pst = null;
		    ResultSet rs = null;
		    Connection con = null;
		    String query = null;
		    LoggableStatement pst1 = null;
		    ResultSet rs1 = null;
		    String sAckStatus = "";
		    CommonMethods commonMethods = null;
		    String BEE_QUERY = null;
		    String BEE_QUERY1 = null;
		    boolean boeNoFlag = false;
		    boolean boeDateFlag = false;
		    boolean portCodeFlag = false;
		    boolean ieCodeFlag = false;
		    boolean adCodeFlag = false;
		    int setValue = 0;
		    try
		    {
		      con = DBConnectionUtility.getConnection();
		      beeAckList = new ArrayList();

		 
		 
		      sAckStatus = ackVO.getAckStatus();
		      commonMethods = new CommonMethods();
		      BEE_QUERY = "SELECT PORTOFDISCHARGE,BOENUMBER,TO_CHAR(BOEDATE,'DD/MM/YYYY') AS BOEDATE,IECODE,ADCODE,RECORDINDICATOR,EXTENSIONBY AS EXTENSIONBY,LETTERNO AS LETTERNO,TO_CHAR(LETTERDATE,'DD/MM/YYYY') AS LETTERDATE,TO_CHAR(EXTENSIONDATE,'DD/MM/YYYY') AS EXTENSIONDATE,REMARKS,ERRORCODES  FROM ETT_BOE_EXT_ACK WHERE BOENUMBER = BOENUMBER ";

		 
		 
		 
		      String boeNo = ackVO.getBoeNo();
		      if (!commonMethods.isNull(boeNo))
		      {
		        BEE_QUERY = BEE_QUERY + " AND TRIM(BOENUMBER) ='" + boeNo.trim() + "'";
		        boeNoFlag = true;
		      }
		      String boeDate = commonMethods.getEmptyIfNull(ackVO.getBoeDate());
		      if (!commonMethods.isNull(boeDate))
		      {
		        BEE_QUERY = BEE_QUERY + " AND TO_CHAR(TO_DATE(BOEDATE,'DD-MM-YY'),'DD/MM/YYYY') = '" + boeDate.trim() + "'";
		        boeDateFlag = true;
		      }
		      String portCode = ackVO.getPortCode();
		      if (!commonMethods.isNull(portCode))
		      {
		        BEE_QUERY = BEE_QUERY + " AND TRIM(PORTOFDISCHARGE) ='" + portCode.trim() + "'";
		        portCodeFlag = true;
		      }
		      String ieCode = ackVO.getBesIECode();
		      if (!commonMethods.isNull(ieCode))
		      {
		        BEE_QUERY = BEE_QUERY + " AND TRIM(IECODE) =  '" + ieCode.trim() + "'";
		        ieCodeFlag = true;
		      }
		      String adCode = ackVO.getBesADCode();
		      if (!commonMethods.isNull(adCode))
		      {
		        BEE_QUERY = BEE_QUERY + " AND TRIM(ADCODE) = '" + adCode.trim() + "'";
		        adCodeFlag = true;
		      }
		      if (sAckStatus.equals("S"))
		      {
		        BEE_QUERY = BEE_QUERY + " AND ERRORCODES = 'SUCCESS'";
		      }
		      else if (sAckStatus.equals("F"))
		      {
		        BEE_QUERY = BEE_QUERY + " AND BOENUMBER NOT IN (SELECT BOENUMBER FROM ETT_BOE_EXT_ACK WHERE ERRORCODES = 'SUCCESS')";
		      }
		      else
		      {
		        BEE_QUERY1 = BEE_QUERY.toString();
		        BEE_QUERY = BEE_QUERY + " AND ERRORCODES = 'SUCCESS' UNION ALL ";
		        BEE_QUERY1 = BEE_QUERY1 + " AND BOENUMBER NOT IN (SELECT BOENUMBER FROM ETT_BOE_EXT_ACK WHERE ERRORCODES = 'SUCCESS')";
		        BEE_QUERY = BEE_QUERY + BEE_QUERY1.toString();
		      }
		      pst = new LoggableStatement(con, BEE_QUERY);

		 
		 
		      logger.info("--------------fetchBeeData------query-----" + pst.getQueryString());
		      rs = pst.executeQuery();
		      while (rs.next())
		      {
		        ackVO = new AcknowledgementVO();
		        ackVO.setPortDischarge(rs.getString("PORTOFDISCHARGE"));
		        ackVO.setBoeNoVal(rs.getString("BOENUMBER"));
		        ackVO.setBoeDateVal(rs.getString("BOEDATE"));
		        ackVO.setIeCodeVal(rs.getString("IECODE"));
		        ackVO.setAdCodeVal(rs.getString("ADCODE"));
		        ackVO.setBesrecordInd(rs.getString("RECORDINDICATOR"));
		        ackVO.setExtBy(rs.getString("EXTENSIONBY"));
		        ackVO.setLetterNo(rs.getString("LETTERNO"));
		        ackVO.setLetterDate(rs.getString("LETTERDATE"));
		        ackVO.setExtRealDate(rs.getString("EXTENSIONDATE"));
		        String remarks = rs.getString("REMARKS");
		        if (remarks != null) {
		          remarks = remarks.replaceAll("[^A-Za-z0-9 ]", "");
		        }
		        if (boeNoFlag) {
		          ackVO.setBoeNo(boeNo.trim());
		        }
		        if (boeDateFlag) {
		          ackVO.setBoeDate(boeDate.trim());
		        }
		        if (portCodeFlag) {
		          ackVO.setPortCode(portCode.trim());
		        }
		        if (ieCodeFlag) {
		          ackVO.setBesIECode(ieCode.trim());
		        }
		        if (adCodeFlag) {
		          ackVO.setBesADCode(adCode.trim());
		        }
		        ackVO.setRemarks(remarks);
		        if (rs.getString("ERRORCODES") != null) {
		          errorCode = rs.getString("ERRORCODES");
		        }
		        ackVO.setBesErrorCodes(errorCode);

		 
		 
		        ArrayList<String> alStr = new ArrayList();
		        ArrayList<String> alStrDB = new ArrayList();
		        String[] splitErrorCodes = { "" };
		        if (errorCode.contains(","))
		        {
		          splitErrorCodes = errorCode.split(",");
		          sInParam = "";
		          for (int j = 0; j < splitErrorCodes.length; j++)
		          {
		            alStr.add(splitErrorCodes[j]);
		            if (sInParam.equals("")) {
		              sInParam = "'" + splitErrorCodes[j] + "'";
		            } else {
		              sInParam = sInParam + ",'" + splitErrorCodes[j] + "'";
		            }
		          }
		        }
		        else
		        {
		          alStr.add(errorCode);
		          sInParam = "'" + errorCode + "'";
		        }
		        sEerrDescQuery = "SELECT Trim(ERRCO) as ERRCO, ERRCO||'-'||ERRMSG ERRCO FROM (SELECT ERROR_CODE ERRCO, ERROR_MSG ERRMSG FROM ETT_IDPMS_ERROR_CODES WHERE trim(ERROR_CODE) IN (?) AND PCATEGORY = 'BEE')";
		        pst1 = new LoggableStatement(con, sEerrDescQuery);
		        pst1.setString(1, sInParam);
		        logger.info("--------------fetchBeeData--- ERRMSG---query-----" + pst1.getQueryString());
		        rs1 = pst1.executeQuery();
		        while (rs1.next())
		        {
		          alStrDB.add(rs1.getString(1));
		          errorDesc = rs1.getString(2);
		          if (errorDesc1.isEmpty()) {
		            errorDesc1 = errorDesc.trim();
		          } else {
		            errorDesc1 = errorDesc1 + ",\n" + errorDesc.trim();
		          }
		        }
		        for (String temp : alStr) {
		          if (!alStrDB.contains(temp)) {
		            if (errorDesc1.isEmpty()) {
		              errorDesc1 = temp.trim();
		            } else {
		              errorDesc1 = errorDesc1 + ",\n" + temp.trim();
		            }
		          }
		        }
		        ackVO.setErrorDesc(errorDesc1);
		        rs1.close();
		        pst1.close();
		        errorDesc = "";
		        errorDesc1 = "";
		        if (errorCode.equals("SUCCESS"))
		        {
		          ackVO.setStatusField(errorCode);
		          successCount++;
		        }
		        if (!errorCode.equals("SUCCESS"))
		        {
		          ackVO.setStatusField("FAIL");
		          failCount++;
		        }
		        beeAckList.add(ackVO);
		        iCount = beeAckList.size();
		      }
		      ackVO.setSuccessCount(successCount);
		      ackVO.setFailCount(failCount);
		      ackVO.setBeeAckValues(beeAckList);
		      ackVO.setTotalCount(iCount);
		    }
		    catch (Exception exception)
		    {
		      logger.info("--------------fetchBeeData----exception-------" + exception);
		      exception.printStackTrace();
		      throwDAOException(exception);
		    }
		    finally
		    {
		      DBConnectionUtility.surrenderDB(con, pst, rs);
		    }
		    logger.info("Exiting Method");
		    return ackVO;
		  }
  public AcknowledgementVO fetchBeaData(AcknowledgementVO ackVO)
		    throws DAOException
		  {
		    logger.info("-------------fetchBeaData----------------");
		    int iCount = 0;
		    int successCount = 0;
		    int failCount = 0;
		    String sInParam = "";
		    String errorDesc = "";
		    String errorDesc1 = "";
		    String sEerrDescQuery = "";
		    String errorCode = "";
		    ArrayList<AcknowledgementVO> beaAckList = null;
		    LoggableStatement pst = null;
		    ResultSet rs = null;
		    Connection con = null;
		    LoggableStatement pst1 = null;
		    ResultSet rs1 = null;
		    CommonMethods commonMethods = null;
		    String BEA_QUERY = null;
		    String BEA_QUERY1 = null;
		    String sAckStatus = null;
		    boolean boeNoFlag = false;
		    boolean boeDateFlag = false;
		    boolean portCodeFlag = false;
		    boolean ieCodeFlag = false;
		    boolean adCodeFlag = false;
		    int setValue = 0;
		    try
		    {
		      con = DBConnectionUtility.getConnection();
		      beaAckList = new ArrayList();

		 
		 
		      sAckStatus = ackVO.getAckStatus();
		      commonMethods = new CommonMethods();
		      BEA_QUERY = "SELECT PORTOFDISCHARGE,BOENUMBER,TO_CHAR(BOEDATE,'DD/MM/YYYY') AS BOEDATE,IECODE,ADCODE,RECORDINDICATOR,TO_CHAR(ADJ_DATE,'DD/MM/YYYY') AS ADJ_DATE,ADJ_IND,ADJ_NO,CLOSE_IND,DOCUMENT_NO,TO_CHAR(DOCUMENT_DATE,'DD/MM/YYYY') AS DOCUMENT_DATE,DOCUMENT_PORT,APPROVEDBY,LETTERNO,TO_CHAR(LETTERDATE,'DD/MM/YYYY') AS LETTERDATE,REMARKS,INV_SNO,INV_NO,ADJ_INVAMT,ERRORCODES FROM ETT_BEA_ACK WHERE BOENUMBER = BOENUMBER ";

		 
		 
		 
		      String boeNo = ackVO.getBoeNo();
		      if (!commonMethods.isNull(boeNo))
		      {
		        BEA_QUERY = BEA_QUERY + " AND TRIM(BOENUMBER) = '" + boeNo.trim() + "'";
		        boeNoFlag = true;
		      }
		      String boeDate = commonMethods.getEmptyIfNull(ackVO.getBoeDate());
		      if (!commonMethods.isNull(boeDate))
		      {
		        BEA_QUERY = BEA_QUERY + " AND TO_CHAR(TO_DATE(BOEDATE,'DD-MM-YY'),'DD/MM/YYYY') = '" + boeDate.trim() + "'";
		        boeDateFlag = true;
		      }
		      String portCode = ackVO.getPortCode();
		      if (!commonMethods.isNull(portCode))
		      {
		        BEA_QUERY = BEA_QUERY + " AND TRIM(PORTOFDISCHARGE) ='" + portCode.trim() + "'";
		        portCodeFlag = true;
		      }
		      String ieCode = ackVO.getBesIECode();
		      if (!commonMethods.isNull(ieCode))
		      {
		        BEA_QUERY = BEA_QUERY + " AND TRIM(IECODE) =  '" + ieCode.trim() + "'";
		        ieCodeFlag = true;
		      }
		      String adCode = ackVO.getBesADCode();
		      if (!commonMethods.isNull(adCode))
		      {
		        BEA_QUERY = BEA_QUERY + " AND TRIM(ADCODE) =  '" + adCode.trim() + "'";
		        adCodeFlag = true;
		      }
		      if (sAckStatus.equals("S"))
		      {
		        BEA_QUERY = BEA_QUERY + " AND ERRORCODES = 'SUCCESS'";
		      }
		      else if (sAckStatus.equals("F"))
		      {
		        BEA_QUERY = BEA_QUERY + " AND BOENUMBER NOT IN (SELECT BOENUMBER FROM ETT_BEA_ACK WHERE ERRORCODES = 'SUCCESS')";
		      }
		      else
		      {
		        BEA_QUERY1 = BEA_QUERY.toString();
		        BEA_QUERY = BEA_QUERY + " AND ERRORCODES = 'SUCCESS' UNION ALL ";
		        BEA_QUERY1 = BEA_QUERY1 + " AND BOENUMBER NOT IN (SELECT BOENUMBER FROM ETT_BEA_ACK WHERE ERRORCODES = 'SUCCESS')";
		        BEA_QUERY = BEA_QUERY + BEA_QUERY1.toString();
		      }
		      pst = new LoggableStatement(con, BEA_QUERY);
		      logger.info("-------------fetchBeaData----------getBEAAckList query------" + pst.getQueryString());
		      rs = pst.executeQuery();
		      while (rs.next())
		      {
		        ackVO = new AcknowledgementVO();
		        ackVO.setPortDischarge(rs.getString("PORTOFDISCHARGE"));
		        ackVO.setBoeNoVal(rs.getString("BOENUMBER"));
		        ackVO.setBoeDateVal(rs.getString("BOEDATE"));
		        ackVO.setIeCodeVal(rs.getString("IECODE"));
		        ackVO.setAdCodeVal(rs.getString("ADCODE"));
		        ackVO.setBesrecordInd(rs.getString("RECORDINDICATOR"));
		        ackVO.setAdjDate(rs.getString("ADJ_DATE"));
		        ackVO.setAdjInd(rs.getString("ADJ_IND"));
		        ackVO.setAdjNo(rs.getString("ADJ_NO"));
		        ackVO.setBesbillCloInd(rs.getString("CLOSE_IND"));
		        ackVO.setDocNo(rs.getString("DOCUMENT_NO"));
		        ackVO.setDocDate(rs.getString("DOCUMENT_DATE"));
		        ackVO.setDocPort(rs.getString("DOCUMENT_PORT"));
		        ackVO.setExtBy(rs.getString("APPROVEDBY"));
		        ackVO.setLetterNo(rs.getString("LETTERNO"));
		        ackVO.setLetterDate(rs.getString("LETTERDATE"));
		        String remarks = rs.getString("REMARKS");
		        if (remarks != null) {
		          remarks = remarks.replaceAll("[^A-Za-z0-9 ]", "");
		        }
		        ackVO.setRemarks(remarks);
		        ackVO.setInvSerialNo(rs.getString("INV_SNO"));
		        ackVO.setInvNo(rs.getString("INV_NO"));
		        ackVO.setAmount(rs.getString("ADJ_INVAMT"));
		        if (rs.getString("ERRORCODES") != null) {
		          errorCode = rs.getString("ERRORCODES");
		        }
		        ackVO.setBesErrorCodes(errorCode);
		        if (boeNoFlag) {
		          ackVO.setBoeNo(boeNo.trim());
		        }
		        if (boeDateFlag) {
		          ackVO.setBoeDate(boeDate.trim());
		        }
		        if (portCodeFlag) {
		          ackVO.setPortCode(portCode.trim());
		        }
		        if (ieCodeFlag) {
		          ackVO.setBesIECode(ieCode.trim());
		        }
		        if (adCodeFlag) {
		          ackVO.setBesADCode(adCode.trim());
		        }
		        ArrayList<String> alStr = new ArrayList();
		        ArrayList<String> alStrDB = new ArrayList();
		        String[] splitErrorCodes = { "" };
		        if (errorCode.contains(","))
		        {
		          splitErrorCodes = errorCode.split(",");
		          sInParam = "";
		          for (int j = 0; j < splitErrorCodes.length; j++)
		          {
		            alStr.add(splitErrorCodes[j]);
		            if (sInParam.equals("")) {
		              sInParam = "'" + splitErrorCodes[j] + "'";
		            } else {
		              sInParam = sInParam + ",'" + splitErrorCodes[j] + "'";
		            }
		          }
		        }
		        else
		        {
		          alStr.add(errorCode);
		          sInParam = "'" + errorCode + "'";
		        }
		        sEerrDescQuery = "SELECT Trim(ERRCO) as ERRCO, ERRCO||'-'||ERRMSG ERRCO FROM (SELECT ERROR_CODE ERRCO, ERROR_MSG ERRMSG FROM ETT_IDPMS_ERROR_CODES WHERE trim(ERROR_CODE) IN (?) AND PCATEGORY = 'BEA')";
		        pst1 = new LoggableStatement(con, sEerrDescQuery);
		        pst1.setString(1, sInParam);
		        logger.info("-------------fetchBeaData-----ERRCO-----sEerrDescQuery------" + pst1.getQueryString());
		        rs1 = pst1.executeQuery();
		        while (rs1.next())
		        {
		          alStrDB.add(rs1.getString(1));
		          errorDesc = rs1.getString(2);
		          if (errorDesc1.isEmpty()) {
		            errorDesc1 = errorDesc.trim();
		          } else {
		            errorDesc1 = errorDesc1 + ",\n" + errorDesc.trim();
		          }
		        }
		        for (String temp : alStr) {
		          if (!alStrDB.contains(temp)) {
		            if (errorDesc1.isEmpty()) {
		              errorDesc1 = temp.trim();
		            } else {
		              errorDesc1 = errorDesc1 + ",\n" + temp.trim();
		            }
		          }
		        }
		        ackVO.setErrorDesc(errorDesc1);
		        errorDesc = "";
		        errorDesc1 = "";
		        rs1.close();
		        pst1.close();
		        if (errorCode.equals("SUCCESS"))
		        {
		          ackVO.setStatusField(errorCode);
		          successCount++;
		        }
		        if (!errorCode.equals("SUCCESS"))
		        {
		          ackVO.setStatusField("FAIL");
		          failCount++;
		        }
		        beaAckList.add(ackVO);
		        iCount = beaAckList.size();
		      }
		      ackVO.setBeaAckValues(beaAckList);
		      ackVO.setFailCount(failCount);
		      ackVO.setSuccessCount(successCount);
		      ackVO.setTotalCount(iCount);
		    }
		    catch (Exception exception)
		    {
		      logger.info("-------------fetchBeaData-----exception-----------" + exception);
		      exception.printStackTrace();
		      throwDAOException(exception);
		    }
		    finally
		    {
		      DBConnectionUtility.surrenderDB(con, pst, rs);
		    }
		    logger.info("Exiting Method");
		    return ackVO;
		  }
  public AcknowledgementVO fetchMbeData(AcknowledgementVO ackVO)
		    throws DAOException
		  {
		    logger.info("-----------fetchMbeData--------------------");
		    int iCount = 0;
		    int successCount = 0;
		    int failCount = 0;
		    String sInParam = "";
		    String errorDesc = "";
		    String errorDesc1 = "";
		    String sEerrDescQuery = "";
		    String errorCode = "";
		    ArrayList<AcknowledgementVO> mbeAckList = null;
		    LoggableStatement pst = null;
		    ResultSet rs = null;
		    Connection con = null;
		    LoggableStatement pst1 = null;
		    ResultSet rs1 = null;
		    CommonMethods commonMethods = null;
		    String MBE_QUERY = null;
		    String MBE_QUERY1 = null;
		    String sAckStatus = null;
		    String query = null;
		    boolean boeNoFlag = false;
		    boolean boeDateFlag = false;
		    boolean ieCodeFlag = false;
		    boolean adCodeFlag = false;
		    int setValue = 0;
		    try
		    {
		      con = DBConnectionUtility.getConnection();
		      mbeAckList = new ArrayList();

		 
		 
		 
		      sAckStatus = ackVO.getAckStatus();
		      commonMethods = new CommonMethods();
		      MBE_QUERY = "SELECT PORTOFDISCHARGE,IMPORTAGENCY,BILLOFENTRYNUMBER,BILLOFENTRYDATE,ADCODE,GP,IECODE,IENAME,IEADDRESS,IEPANNUMBER,PORTOFSHIPMENT,RECORDINDICATOR,IGMNUMBER,IGMDATE,MAWBMBLNUMBER,MAWBMBLDATE,HAWBHBLNUMBER,HAWBHBLDATE,INVOICESERIALNO,INVOICENO,TERMSOFINVOICE,SUPPLIERNAME,SUPPLIERADDRESS,SUPPLIERCOUNTRY,SELLERNAME,SELLERADDRESS,SELLERCOUNTRY,INVOICEAMOUNT,INVOICECURRENCY,FREIGHTAMOUNT,FREIGHTCURRENCYCODE,INSURANCEAMOUNT,INSURANCECURRENCYCODE,AGENCYCOMMISSION,AGENCYCURRENCY,DISCOUNTCHARGES,DISCOUNTCURRENCY,MISCELLANEOUSCHARGES,MISCELLANEOUSCURRENCY,THIRDPARTYNAME,THIRDPARTYADDRESS,THIRDPARTYCOUNTRY,ERRORCODE FROM ETT_MANUAL_BOE_ACK WHERE BILLOFENTRYNUMBER = BILLOFENTRYNUMBER ";

		 
		 
		 
		 
		      String boeNo = ackVO.getBoeNo();
		      if (!commonMethods.isNull(boeNo))
		      {
		        MBE_QUERY = MBE_QUERY + " AND TRIM(BILLOFENTRYNUMBER) ='" + boeNo.trim() + "' ";
		        boeNoFlag = true;
		      }
		      String boeDate = commonMethods.getEmptyIfNull(ackVO.getBoeDate());
		      if (!commonMethods.isNull(boeDate))
		      {
		        MBE_QUERY = MBE_QUERY + " AND TO_CHAR(TO_DATE(BILLOFENTRYDATE,'DD-MM-YY'),'DD/MM/YYYY') = '" + boeDate.trim() + "' ";
		        boeDateFlag = true;
		      }
		      String ieCode = ackVO.getBesIECode();
		      if (!commonMethods.isNull(ieCode))
		      {
		        MBE_QUERY = MBE_QUERY + " AND TRIM(IECODE) =  '" + ieCode.trim() + "' ";
		        ieCodeFlag = true;
		      }
		      String adCode = ackVO.getBesADCode();
		      if (!commonMethods.isNull(adCode))
		      {
		        MBE_QUERY = MBE_QUERY + " AND TRIM(ADCODE) =  '" + adCode.trim() + "' ";
		        adCodeFlag = true;
		      }
		      if (sAckStatus.equalsIgnoreCase("S"))
		      {
		        MBE_QUERY = MBE_QUERY + " AND ERRORCODE = 'SUCCESS'";
		      }
		      else if (sAckStatus.equalsIgnoreCase("F"))
		      {
		        MBE_QUERY = MBE_QUERY + " AND BILLOFENTRYNUMBER NOT IN (SELECT BILLOFENTRYNUMBER FROM ETT_MANUAL_BOE_ACK WHERE ERRORCODE = 'SUCCESS')";
		      }
		      else
		      {
		        MBE_QUERY1 = MBE_QUERY.toString();
		        MBE_QUERY = MBE_QUERY + " AND ERRORCODE = 'SUCCESS' UNION ALL ";
		        MBE_QUERY1 = MBE_QUERY1 + " AND BILLOFENTRYNUMBER NOT IN (SELECT BILLOFENTRYNUMBER FROM ETT_MANUAL_BOE_ACK WHERE ERRORCODE = 'SUCCESS')";
		        MBE_QUERY = MBE_QUERY + MBE_QUERY1.toString();
		      }
		      pst = new LoggableStatement(con, MBE_QUERY);
		      logger.info("-------fetchMbeData---exception------query-------" + pst.getQueryString());
		      rs = pst.executeQuery();
		      while (rs.next())
		      {
		        ackVO = new AcknowledgementVO();
		        ackVO.setPortVal(rs.getString("PORTOFDISCHARGE"));
		        ackVO.setImAgencyVal(rs.getString("IMPORTAGENCY"));
		        ackVO.setBoeNoVal(rs.getString("BILLOFENTRYNUMBER"));
		        ackVO.setBoeDateVal(rs.getString("BILLOFENTRYDATE"));
		        ackVO.setAdCodeVal(rs.getString("ADCODE"));
		        ackVO.setGpCodeVal(rs.getString("GP"));
		        ackVO.setIeCodeVal(rs.getString("IECODE"));
		        ackVO.setIeNameVal(rs.getString("IENAME"));
		        ackVO.setIeAddressVal(rs.getString("IEADDRESS"));
		        ackVO.setIePanVal(rs.getString("IEPANNUMBER"));
		        ackVO.setPosVal(rs.getString("PORTOFSHIPMENT"));
		        ackVO.setRecordIndVal(rs.getString("RECORDINDICATOR"));
		        ackVO.setIgmNoVal(rs.getString("IGMNUMBER"));
		        ackVO.setIgmDateVal(rs.getString("IGMDATE"));
		        ackVO.setMblNoVal(rs.getString("MAWBMBLNUMBER"));
		        ackVO.setMblDateVal(rs.getString("MAWBMBLDATE"));
		        ackVO.setHblNoVal(rs.getString("HAWBHBLNUMBER"));
		        ackVO.setHblDateVal(rs.getString("HAWBHBLDATE"));
		        ackVO.setInvSnoVal(rs.getString("INVOICESERIALNO"));
		        ackVO.setInvNoVal(rs.getString("INVOICENO"));
		        ackVO.setTermsofInvoiceVal(rs.getString("TERMSOFINVOICE"));
		        ackVO.setInvAmtVal(rs.getString("INVOICEAMOUNT"));
		        ackVO.setInvAmtCurrVal(rs.getString("INVOICECURRENCY"));
		        ackVO.setInvFrAmtVal(rs.getString("FREIGHTAMOUNT"));
		        ackVO.setInvFrAmtCurrVal(rs.getString("FREIGHTCURRENCYCODE"));
		        ackVO.setInvInsAmtVal(rs.getString("INSURANCEAMOUNT"));
		        ackVO.setInvInsAmtCurrVal(rs.getString("INSURANCECURRENCYCODE"));
		        ackVO.setInvAgAmtVal(rs.getString("AGENCYCOMMISSION"));
		        ackVO.setInvAgAmtCurrVal(rs.getString("AGENCYCURRENCY"));
		        ackVO.setInvDisAmtVal(rs.getString("DISCOUNTCHARGES"));
		        ackVO.setInvDisAmtCurrVal(rs.getString("DISCOUNTCURRENCY"));
		        ackVO.setInvMisAmtVal(rs.getString("MISCELLANEOUSCHARGES"));
		        ackVO.setInvMisAmtCurrVal(rs.getString("MISCELLANEOUSCURRENCY"));
		        ackVO.setSupNameVal(rs.getString("SUPPLIERNAME"));
		        ackVO.setSupAddrVal(rs.getString("SUPPLIERADDRESS"));
		        ackVO.setSupCounVal(rs.getString("SUPPLIERCOUNTRY"));
		        ackVO.setSellNameVal(rs.getString("SELLERNAME"));
		        ackVO.setSellAddrVal(rs.getString("SELLERADDRESS"));
		        ackVO.setSellCounVal(rs.getString("SELLERCOUNTRY"));
		        ackVO.setThirdPartyNameVal(rs.getString("THIRDPARTYNAME"));
		        ackVO.setThirdPartyAddrVal(rs.getString("THIRDPARTYADDRESS"));
		        ackVO.setThirdPartyCounVal(rs.getString("THIRDPARTYCOUNTRY"));
		        if (rs.getString("ERRORCODE") != null) {
		          errorCode = rs.getString("ERRORCODE");
		        }
		        if (boeNoFlag) {
		          ackVO.setBoeNo(boeNo.trim());
		        }
		        if (boeDateFlag) {
		          ackVO.setBoeDate(boeDate.trim());
		        }
		        if (ieCodeFlag) {
		          ackVO.setBesIECode(ieCode.trim());
		        }
		        if (adCodeFlag) {
		          ackVO.setBesADCode(adCode.trim());
		        }
		        ackVO.setErrorCodesVal(rs.getString("ERRORCODE"));

		 
		 
		        ArrayList<String> alStr = new ArrayList();
		        ArrayList<String> alStrDB = new ArrayList();
		        String[] splitErrorCodes = { "" };
		        if (errorCode.contains(","))
		        {
		          splitErrorCodes = errorCode.split(",");
		          sInParam = "";
		          for (int j = 0; j < splitErrorCodes.length; j++)
		          {
		            alStr.add(splitErrorCodes[j]);
		            if (sInParam.equals("")) {
		              sInParam = "'" + splitErrorCodes[j] + "'";
		            } else {
		              sInParam = sInParam + ",'" + splitErrorCodes[j] + "'";
		            }
		          }
		        }
		        else
		        {
		          alStr.add(errorCode);
		          sInParam = "'" + errorCode + "'";
		        }
		        sEerrDescQuery = "SELECT Trim(ERRCO) as ERRCO, ERRCO||'-'||ERRMSG ERRCO FROM (SELECT ERROR_CODE ERRCO, ERROR_MSG ERRMSG FROM ETT_IDPMS_ERROR_CODES WHERE trim(ERROR_CODE) IN (?) AND PCATEGORY = 'MBE')";
		        pst1 = new LoggableStatement(con, sEerrDescQuery);
		        pst1.setString(1, sInParam);
		        logger.info("-------fetchMbeData---exception------ERRCO-------" + pst1.getQueryString());
		        rs1 = pst1.executeQuery();
		        while (rs1.next())
		        {
		          alStrDB.add(rs1.getString(1));
		          errorDesc = rs1.getString(2);
		          if (errorDesc1.isEmpty()) {
		            errorDesc1 = errorDesc.trim();
		          } else {
		            errorDesc1 = errorDesc1 + ",\n" + errorDesc.trim();
		          }
		        }
		        for (String temp : alStr) {
		          if (!alStrDB.contains(temp)) {
		            if (errorDesc1.isEmpty()) {
		              errorDesc1 = temp.trim();
		            } else {
		              errorDesc1 = errorDesc1 + ",\n" + temp.trim();
		            }
		          }
		        }
		        ackVO.setErrorDesc(errorDesc1);
		        errorDesc = "";
		        errorDesc1 = "";
		        rs1.close();
		        pst1.close();
		        if (errorCode.equalsIgnoreCase("SUCCESS"))
		        {
		          ackVO.setStatusField(errorCode);
		          successCount++;
		        }
		        if (!errorCode.equalsIgnoreCase("SUCCESS"))
		        {
		          ackVO.setStatusField("FAIL");
		          failCount++;
		        }
		        mbeAckList.add(ackVO);
		        iCount = mbeAckList.size();
		      }
		      ackVO.setSuccessCount(successCount);
		      ackVO.setFailCount(failCount);
		      ackVO.setMbeAckValues(mbeAckList);
		      ackVO.setTotalCount(iCount);
		    }
		    catch (Exception exception)
		    {
		      logger.info("-------fetchMbeData---exception-------------" + exception);
		      exception.printStackTrace();
		      throwDAOException(exception);
		    }
		    finally
		    {
		      DBConnectionUtility.surrenderDB(con, pst, rs);
		    }
		    logger.info("Exiting Method");
		    return ackVO;
		  }
  public AcknowledgementVO fetchOraData(AcknowledgementVO ackVO)
		    throws DAOException
		  {
		    logger.info("-----------------fetchOraData-----------------");
		    int iCount = 0;
		    int successCount = 0;
		    int failCount = 0;
		    String sInParam = "";
		    String errorDesc = "";
		    String errorDesc1 = "";
		    String sEerrDescQuery = "";
		    String errorCode = "";
		    ArrayList<AcknowledgementVO> oraAckList = null;
		    LoggableStatement pst = null;
		    ResultSet rs = null;
		    Connection con = null;
		    LoggableStatement pst1 = null;
		    ResultSet rs1 = null;
		    String query = null;
		    CommonMethods commonMethods = null;
		    String ORA_QUERY = null;
		    String ORA_QUERY1 = null;
		    String sAckStatus = null;
		    boolean ormNoFlag = false;
		    boolean ieCodeFlag = false;
		    boolean adCodeFlag = false;
		    int setValue = 0;
		    try
		    {
		      con = DBConnectionUtility.getConnection();

		 
		      oraAckList = new ArrayList();

		 
		 
		 
		      sAckStatus = ackVO.getAckStatus();
		      commonMethods = new CommonMethods();
		      ORA_QUERY = "SELECT OUTWARDREFERENCENUMBER , ADCODE, CURRENCYCODE, CLOSAMT, TO_CHAR(TO_DATE(CLOSDAT,'DD/MM/YYYY'),'DD/MM/YYYY') AS CLDAT, IECODE, SWIFTMESSAGE, ADJCLOINDI, APPROVEDBY, LETTERNO, TO_CHAR(LETTERDAT,'DD/MM/YYYY') LETDAT, DOCNUM, TO_CHAR(DOCDAT,'DD/MM/YYYY') DOCDAT, PORTCODE, RECORDINDICATOR, REMARKS, ERRORCODES FROM ETT_ORA_ACK WHERE OUTWARDREFERENCENUMBER = OUTWARDREFERENCENUMBER ";

		 
		 
		      String ormNo = ackVO.getOrmNo();
		      logger.info("-----------------ormNo-----------------" + ormNo.trim());
		      if (!commonMethods.isNull(ormNo))
		      {
		        ORA_QUERY = ORA_QUERY + " AND TRIM(OUTWARDREFERENCENUMBER) ='" + ormNo.trim() + "'";
		        ormNoFlag = true;
		      }
		      String ieCode = ackVO.getIecode();
		      logger.info("-----------------ieCode-----------------" + ieCode.trim());
		      if (!commonMethods.isNull(ieCode))
		      {
		        ORA_QUERY = ORA_QUERY + " AND TRIM(IECODE) ='" + ieCode.trim() + "'";
		        ieCodeFlag = true;
		      }
		      String adCode = ackVO.getAdcode();
		      logger.info("-----------------adCode-----------------" + adCode.trim());
		      if (!commonMethods.isNull(adCode))
		      {
		        ORA_QUERY = ORA_QUERY + " AND TRIM(ADCODE) ='" + adCode.trim() + "'";
		        adCodeFlag = true;
		      }
		      if (sAckStatus.equals("S"))
		      {
		        ORA_QUERY = ORA_QUERY + " AND ERRORCODES = 'SUCCESS'";
		      }
		      else if (sAckStatus.equals("F"))
		      {
		        ORA_QUERY = ORA_QUERY + " AND OUTWARDREFERENCENUMBER NOT IN (SELECT OUTWARDREFERENCENUMBER FROM ETT_ORA_ACK WHERE ERRORCODES = 'SUCCESS')";
		      }
		      else
		      {
		        ORA_QUERY1 = ORA_QUERY.toString();
		        ORA_QUERY = ORA_QUERY + " AND ERRORCODES = 'SUCCESS' UNION ALL ";
		        ORA_QUERY1 = ORA_QUERY1 + " AND OUTWARDREFERENCENUMBER NOT IN (SELECT OUTWARDREFERENCENUMBER FROM ETT_ORA_ACK WHERE ERRORCODES = 'SUCCESS')";
		        ORA_QUERY = ORA_QUERY + ORA_QUERY1.toString();
		      }
		      pst = new LoggableStatement(con, ORA_QUERY);
		      logger.info("-----------------fetchOraData-----query------------" + pst.getQueryString());
		      rs = pst.executeQuery();
		      while (rs.next())
		      {
		        ackVO = new AcknowledgementVO();
		        ackVO.setAckOrmNo(rs.getString("OUTWARDREFERENCENUMBER"));
		        ackVO.setOrmAdCode(rs.getString("ADCODE"));
		        ackVO.setOrmCurr(rs.getString("CURRENCYCODE"));
		        ackVO.setOrmAmt(rs.getString("CLOSAMT"));
		        ackVO.setOrmDate(rs.getString("CLDAT"));
		        ackVO.setOrmIeCode(rs.getString("IECODE"));
		        ackVO.setOrmSwift(rs.getString("SWIFTMESSAGE"));
		        ackVO.setOrmIndicator(rs.getString("ADJCLOINDI"));
		        ackVO.setOrmApprovedBy(rs.getString("APPROVEDBY"));
		        ackVO.setOrmLno(rs.getString("LETTERNO"));
		        ackVO.setOrmLdate(rs.getString("LETDAT"));
		        ackVO.setOrmDocNo(rs.getString("DOCNUM"));
		        ackVO.setOrmDocDate(rs.getString("DOCDAT"));
		        ackVO.setOrmPod(rs.getString("PORTCODE"));
		        ackVO.setOrmRec(rs.getString("RECORDINDICATOR"));
		        String remarks = rs.getString("REMARKS");
		        if (remarks != null) {
		          remarks = remarks.replaceAll("[^A-Za-z0-9 ]", "");
		        }
		        if (ormNoFlag) {
		          ackVO.setOrmNo(ormNo.trim());
		        }
		        if (ieCodeFlag) {
		          ackVO.setIecode(ieCode.trim());
		        }
		        if (adCodeFlag) {
		          ackVO.setAdcode(adCode.trim());
		        }
		        ackVO.setOrmRemarks(remarks);
		        if (rs.getString("ERRORCODES") != null) {
		          errorCode = rs.getString("ERRORCODES");
		        }
		        ArrayList<String> alStr = new ArrayList();
		        ArrayList<String> alStrDB = new ArrayList();
		        String[] splitErrorCodes = { "" };
		        if (errorCode.contains(","))
		        {
		          splitErrorCodes = errorCode.split(",");
		          sInParam = "";
		          for (int j = 0; j < splitErrorCodes.length; j++)
		          {
		            alStr.add(splitErrorCodes[j]);
		            if (sInParam.equals("")) {
		              sInParam = "'" + splitErrorCodes[j] + "'";
		            } else {
		              sInParam = sInParam + ",'" + splitErrorCodes[j] + "'";
		            }
		          }
		        }
		        else
		        {
		          alStr.add(errorCode);
		          sInParam = "'" + errorCode + "'";
		        }
		        sEerrDescQuery = "SELECT Trim(ERRCO) as ERRCO, ERRCO||'-'||ERRMSG ERRCO FROM (SELECT ERROR_CODE ERRCO, ERROR_MSG ERRMSG FROM ETT_IDPMS_ERROR_CODES WHERE trim(ERROR_CODE) IN (" + sInParam + ") AND PCATEGORY = 'ORA')";
		        pst1 = new LoggableStatement(con, sEerrDescQuery);
		        logger.info("-----------------fetchOraData------sEerrDescQuery-----------" + pst1.getQueryString());
		        rs1 = pst1.executeQuery();
		        while (rs1.next())
		        {
		          alStrDB.add(rs1.getString(1));
		          errorDesc = rs1.getString(2);
		          if (errorDesc1.isEmpty()) {
		            errorDesc1 = errorDesc.trim();
		          } else {
		            errorDesc1 = errorDesc1 + ",\n" + errorDesc.trim();
		          }
		        }
		        for (String temp : alStr) {
		          if (!alStrDB.contains(temp)) {
		            if (errorDesc1.isEmpty()) {
		              errorDesc1 = temp.trim();
		            } else {
		              errorDesc1 = errorDesc1 + ",\n" + temp.trim();
		            }
		          }
		        }
		        ackVO.setErrorDesc(errorDesc1);
		        errorDesc = "";
		        errorDesc1 = "";
		        pst1.close();
		        rs1.close();
		        if (errorCode.equals("SUCCESS"))
		        {
		          ackVO.setStatusField(errorCode);
		          successCount++;
		        }
		        if (!errorCode.equals("SUCCESS"))
		        {
		          ackVO.setStatusField("FAIL");
		          failCount++;
		        }
		        oraAckList.add(ackVO);
		        iCount = oraAckList.size();
		      }
		      ackVO.setSuccessCount(successCount);
		      ackVO.setFailCount(failCount);
		      ackVO.setOraAckValues(oraAckList);
		      ackVO.setTotalCount(iCount);
		    }
		    catch (Exception exception)
		    {
		      logger.info("-----------------fetchOraData------exception-----------" + exception);
		      exception.printStackTrace();
		      throwDAOException(exception);
		    }
		    finally
		    {
		      DBConnectionUtility.surrenderDB(con, pst, rs);
		    }
		    logger.info("Exiting Method");
		    return ackVO;
		  }
  public AcknowledgementVO fetchObbData(AcknowledgementVO ackVO)

		    throws DAOException

		  {

		    logger.info("Entering Method");

		    int recCount = 0;

		    int successCount = 0;

		    int uploadCount = 0;

		    int iCount = 0;

		    String sInParam = "";

		    String errorDesc = "";

		    String errorDesc1 = "";

		    String sEerrDescQuery = "";

		    String boeStatus = "";

		    ArrayList<AcknowledgementVO> obbAckList = null;

		    LoggableStatement pst = null;

		    LoggableStatement pst1 = null;

		    ResultSet rs = null;

		    ResultSet rs1 = null;

		    Connection con = null;

		    String query = null;

		    CommonMethods commonMethods = null;

		    String obbBoeNo = "";

		    String boeDate = "";

		    String adCodde = "";

		    String ieCodde = "";

		    String recDate = "";

		    String portCode = "";

		    String sAckStatus = null;

		    String OBB_QUERY = null;

		    String OBB_QUERY1 = null;

		    boolean obbBoeNoFlag = false;

		    boolean boeDateFlag = false;

		    boolean portCodeFlag = false;

		    boolean recDateFlag = false;

		    boolean adCoddeFlag = false;

		    boolean ieCoddeFlag = false;

		    int setValue = 0;

		    try

		    {

		      con = DBConnectionUtility.getConnection();

		      obbAckList = new ArrayList();

		 
		 
		 
		      commonMethods = new CommonMethods();

		      sAckStatus = ackVO.getAckStatus();

		 
		      OBB_QUERY = "SELECT BOENUM, TO_CHAR(BOEDATE,'DD/MM/YYYY') BOEDATE, PORTCODE, ADCODE, IE_CODE, IENAME, BOE_IMP_AGENCY, DECODE(STATUS, 'R', 'RECEIVED', 'U', 'UPLOADED', 'S', 'SUCCESS','') STATUS, TO_CHAR(TO_DATE(REC_DATE,'DD/MM/rrrr'), 'DD/MM/YYYY') REC_DATE FROM ETT_BOE_OBB_DETAILS WHERE BOENUM = BOENUM ";

		 
		 
		      obbBoeNo = ackVO.getBoeNo();

		      if (!commonMethods.isNull(obbBoeNo))

		      {

		        OBB_QUERY = OBB_QUERY + " AND BOENUM =?";

		        obbBoeNoFlag = true;

		      }

		      boeDate = commonMethods.getEmptyIfNull(ackVO.getBoeDate());

		      if (!commonMethods.isNull(boeDate))

		      {

		        OBB_QUERY = OBB_QUERY + " AND TO_CHAR(TO_DATE(BOEDATE,'DD-MM-YY'),'DD/MM/YYYY') = ?";

		        boeDateFlag = true;

		      }

		      portCode = ackVO.getPortCode();

		      if (!commonMethods.isNull(portCode))

		      {

		        OBB_QUERY = OBB_QUERY + " AND TRIM(PORTCODE) =?";

		        portCodeFlag = true;

		      }

		      recDate = ackVO.getRecDate();

		      if (!commonMethods.isNull(recDate))

		      {

		        OBB_QUERY = OBB_QUERY + " AND REC_DATE =  TO_CHAR(TO_DATE(?,'DD/MM/YYYY'),'DD/MM/YYYY') ";

		        recDateFlag = true;

		      }

		      adCodde = ackVO.getAdCodde();

		      if (!commonMethods.isNull(adCodde))

		      {

		        OBB_QUERY = OBB_QUERY + " AND ADCODE =  ?";

		        adCoddeFlag = true;

		      }

		      ieCodde = ackVO.getIeCodde();

		      if (!commonMethods.isNull(ieCodde))

		      {

		        OBB_QUERY = OBB_QUERY + " AND IECODE = ?";

		        ieCoddeFlag = true;

		      }

		      if (sAckStatus.equals("R")) {

		        OBB_QUERY = OBB_QUERY + " AND STATUS = 'R'";

		      }

		      if (sAckStatus.equals("U")) {

		        OBB_QUERY = OBB_QUERY + " AND STATUS = 'U'";

		      }

		      if (sAckStatus.equals("S")) {

		        OBB_QUERY = OBB_QUERY + " AND STATUS = 'S'";

		      }

		      pst = new LoggableStatement(con, query);

		      if (obbBoeNoFlag) {

		        pst.setString(++setValue, obbBoeNo.trim());

		      }

		      if (boeDateFlag) {

		        pst.setString(++setValue, boeDate.trim());

		      }

		      if (portCodeFlag) {

		        pst.setString(++setValue, portCode.trim());

		      }

		      if (recDateFlag) {

		        pst.setString(++setValue, recDate.trim());

		      }

		      if (adCoddeFlag) {

		        pst.setString(++setValue, adCodde.trim());

		      }

		      if (ieCoddeFlag) {

		        pst.setString(++setValue, ieCodde.trim());

		      }

		      rs = pst.executeQuery();

		      while (rs.next())

		      {

		        ackVO = new AcknowledgementVO();

		        ackVO.setPortVal(rs.getString("PORTCODE"));

		        ackVO.setImAgencyVal(rs.getString("BOE_IMP_AGENCY"));

		        ackVO.setBoeNoVal(rs.getString("BOENUM"));

		        ackVO.setBoeDateVal(rs.getString("BOEDATE"));

		        ackVO.setAdCodeVal(rs.getString("ADCODE"));

		        ackVO.setIeCodeVal(rs.getString("IE_CODE"));

		        ackVO.setIeNameVal(rs.getString("IENAME"));

		        boeStatus = rs.getString("STATUS");

		        ackVO.setRecDate(rs.getString("REC_DATE"));

		        if (boeStatus.equalsIgnoreCase("RECEIVED"))

		        {

		          ackVO.setStatusField(boeStatus);

		          recCount++;

		        }

		        if (boeStatus.equalsIgnoreCase("UPLOADED"))

		        {

		          ackVO.setStatusField(boeStatus);

		          uploadCount++;

		        }

		        if (boeStatus.equalsIgnoreCase("SUCCESS"))

		        {

		          ackVO.setStatusField(boeStatus);

		          successCount++;

		        }

		        obbAckList.add(ackVO);

		        iCount = obbAckList.size();

		      }

		      ackVO.setSuccessCount(successCount);

		      ackVO.setRecCount(recCount);

		      ackVO.setUploadCount(uploadCount);

		      ackVO.setObbAckValues(obbAckList);

		      ackVO.setTotalCount(iCount);

		    }

		    catch (Exception exception)

		    {

		      exception.printStackTrace();

		      throwDAOException(exception);

		    }

		    finally

		    {

		      DBConnectionUtility.surrenderDB(con, pst, rs);

		    }

		    logger.info("Exiting Method");

		    return ackVO;

		  }
  public AcknowledgementVO fetchBttData(AcknowledgementVO ackVO)
		    throws DAOException
		  {
		    logger.info("-------------fetchBeaData----------------");
		    int iCount = 0;
		    int successCount = 0;
		    int failCount = 0;
		    String sInParam = "";
		    String errorDesc = "";
		    String errorDesc1 = "";
		    String sEerrDescQuery = "";
		    String errorCode = "";
		    ArrayList<AcknowledgementVO> bttAckList = null;
		    LoggableStatement pst = null;
		    ResultSet rs = null;
		    Connection con = null;
		    LoggableStatement pst1 = null;
		    ResultSet rs1 = null;
		    CommonMethods commonMethods = null;
		    String BTT_QUERY = null;
		    String BTT_QUERY1 = null;
		    String sAckStatus = null;
		    boolean boeNoFlag = false;
		    boolean boeDateFlag = false;
		    boolean portCodeFlag = false;
		    boolean ieCodeFlag = false;
		    boolean adCodeFlag = false;
		    int setValue = 0;
		    try
		    {
		      con = DBConnectionUtility.getConnection();
		      bttAckList = new ArrayList();

		 
		 
		      sAckStatus = ackVO.getAckStatus();
		      commonMethods = new CommonMethods();
		      BTT_QUERY = "SELECT PORTCODE,BOENO,TO_CHAR(BOEDATE,'DD/MM/YYYY') AS BOEDATE,IECODE,ADCODE,RECORDINDICATOR,\tIGMNUMBER,TO_CHAR(IGMDATE,'DD/MM/YYYY') AS IGMDATE,MBLNUMBER,TO_CHAR(MBLDATE,'DD/MM/YYYY') AS MBLDATE,  INSNO,INNO,TOTALINVOICEAMTIC,REALIZEDINVOICEAMTIC,UNREALIZEDINVOICEAMTIC\tFROM ETT_BTT_ACK WHERE BOENO = BOENO ";

		 
		 
		      String boeNo = ackVO.getBoeNo();
		      if (!commonMethods.isNull(boeNo))
		      {
		        BTT_QUERY = BTT_QUERY + " AND TRIM(BOENO) =?";
		        boeNoFlag = true;
		      }
		      String boeDate = commonMethods.getEmptyIfNull(ackVO.getBoeDate());
		      if (!commonMethods.isNull(boeDate))
		      {
		        BTT_QUERY = BTT_QUERY + " AND TO_CHAR(TO_DATE(BOEDATE,'DD-MM-YY'),'DD/MM/YYYY') = ?";
		        boeDateFlag = true;
		      }
		      String portCode = ackVO.getPortCode();
		      if (!commonMethods.isNull(portCode))
		      {
		        BTT_QUERY = BTT_QUERY + " AND TRIM(PORTCODE) =?";
		        portCodeFlag = true;
		      }
		      String ieCode = ackVO.getBesIECode();
		      if (!commonMethods.isNull(ieCode))
		      {
		        BTT_QUERY = BTT_QUERY + " AND TRIM(IECODE) =  ?";
		        ieCodeFlag = true;
		      }
		      String adCode = ackVO.getBesADCode();
		      if (!commonMethods.isNull(adCode))
		      {
		        BTT_QUERY = BTT_QUERY + " AND TRIM(ADCODE) =  ?";
		        adCodeFlag = true;
		      }
		      pst = new LoggableStatement(con, BTT_QUERY);
		      if (boeNoFlag) {
		        pst.setString(++setValue, boeNo.trim());
		      }
		      if (boeDateFlag) {
		        pst.setString(++setValue, boeDate.trim());
		      }
		      if (portCodeFlag) {
		        pst.setString(++setValue, portCode.trim());
		      }
		      if (ieCodeFlag) {
		        pst.setString(++setValue, ieCode.trim());
		      }
		      if (adCodeFlag) {
		        pst.setString(++setValue, adCode.trim());
		      }
		      logger.info("-------------fetchBttData----------getBEAAckList query------" + pst.getQueryString());
		      rs = pst.executeQuery();
		      while (rs.next())
		      {
		        ackVO = new AcknowledgementVO();
		        ackVO.setPortDischarge(rs.getString("PORTCODE"));
		        ackVO.setBoeNo(rs.getString("BOENO"));
		        ackVO.setBoeDate(rs.getString("BOEDATE"));
		        ackVO.setBesIECode(rs.getString("IECODE"));
		        ackVO.setBesADCode(rs.getString("ADCODE"));
		        ackVO.setBesrecordInd(rs.getString("RECORDINDICATOR"));
		        ackVO.setIgmNumber(rs.getString("IGMNUMBER"));
		        ackVO.setIgmDate(rs.getString("IGMDATE"));
		        ackVO.setMblNumber(rs.getString("MBLNUMBER"));
		        ackVO.setMblDate(rs.getString("MBLDATE"));
		        ackVO.setInvSerialNo(rs.getString("INSNO"));
		        ackVO.setInvNo(rs.getString("INNO"));
		        ackVO.setTotalInvoiceAmtIc(rs.getString("TOTALINVOICEAMTIC"));
		        ackVO.setRealizedInvoiceAmtIc(rs.getString("REALIZEDINVOICEAMTIC"));
		        ackVO.setUnrealizedInvoiceAmtIc(rs.getString("UNREALIZEDINVOICEAMTIC"));
		        bttAckList.add(ackVO);
		        iCount = bttAckList.size();
		      }
		      ackVO.setBttAckValues(bttAckList);

		 
		      ackVO.setTotalCount(iCount);
		    }
		    catch (Exception exception)
		    {
		      logger.info("-------------fetchBttData-----exception-----------" + exception);
		      exception.printStackTrace();
		      throwDAOException(exception);
		    }
		    finally
		    {
		      DBConnectionUtility.surrenderDB(con, pst, rs);
		    }
		    logger.info("Exiting Method");
		    return ackVO;
		  }
  public AcknowledgementVO fetchBesDataForMaker(AcknowledgementVO ackVO)
		    throws DAOException
		  {
		    logger.info("Entering Method");
		    int successCount = 0;
		    int failCount = 0;
		    int iCount = 0;
		    String sInParam = "";
		    String errorDesc = "";
		    String errorDesc1 = "";
		    String sEerrDescQuery = "";
		    String errorCode = "";
		    ArrayList<AcknowledgementVO> besAckList = null;
		    LoggableStatement pst = null;
		    ResultSet rs = null;
		    Connection con = null;
		    String query = null;
		    LoggableStatement pst1 = null;
		    ResultSet rs1 = null;
		    CommonMethods commonMethods = null;
		    String BES_QUERY = null;
		    String BES_QUERY1 = null;
		    String sAckStatus = "";
		    boolean outRefNoFlag = false;
		    boolean boeNoFlag = false;
		    boolean boeDateFlag = false;
		    boolean portcodeFlag = false;
		    boolean payrefnoFlag = false;
		    int setValue = 0;
		    try
		    {
		      con = DBConnectionUtility.getConnection();

		 
		      besAckList = new ArrayList();

		 
		 
		      sAckStatus = ackVO.getAckStatus();
		      commonMethods = new CommonMethods();
		      BES_QUERY = "SELECT eba.PORTOFDISCHARGE,eba.BOENUMBER,TO_CHAR(TO_DATE(eba.BOEDATE,'DD-MM-YY'),'DD/MM/YYYY') AS BOEDATE,eba.IECODE,eba.CHANGEIECODE,eba.ADCODE,eba.RECORDINDICATOR,eba.PAYMENTPARTY,eba.PAYMENTREFNUMBER,eba.ORNUMBER,eba.ORADCODE,eba.REMITTCUR,eba.BILLCLOSUREINDI,eba.INVOICESERIALNO,eba.INVOICENO,eba.INVOICEAMTIC,eba.INVOICEAMT,eba.ERRORCODES FROM ETT_BES_ACK eba WHERE eba.BOENUMBER not in (select ebac.BOENUMBER from ETT_BES_ACK_CANCEL ebac where  ebac.BOENUMBER = eba.BOENUMBER and ebac.BOEDATE=eba.BOEDATE and ebac.PORTOFDISCHARGE=eba.PORTOFDISCHARGE and ebac.PAYMENTREFNUMBER=eba.PAYMENTREFNUMBER) ";

		 
		 
		 
		      String outRefNo = ackVO.getOrmNo();
		      if (!commonMethods.isNull(outRefNo))
		      {
		        BES_QUERY = BES_QUERY + " AND TRIM(ORNUMBER) ='" + outRefNo.trim() + "'";
		        outRefNoFlag = true;
		      }
		      String boeNo = ackVO.getBoeNoEntered();
		      if (!commonMethods.isNull(boeNo))
		      {
		        BES_QUERY = BES_QUERY + " AND TRIM(BOENUMBER) ='" + boeNo.trim() + "'";
		        boeNoFlag = true;
		      }
		      String boeDate = commonMethods.getEmptyIfNull(ackVO.getBoeDateEntered());
		      if (!commonMethods.isNull(boeDate))
		      {
		        BES_QUERY = BES_QUERY + " AND TO_CHAR(TO_DATE(BOEDATE,'DD-MM-YY'),'DD/MM/YYYY') ='" + boeDate.trim() + "'";
		        boeDateFlag = true;
		      }
		      String portcode = commonMethods.getEmptyIfNull(ackVO.getBoePortEntered());
		      if (!commonMethods.isNull(portcode))
		      {
		        BES_QUERY = BES_QUERY + " AND TRIM(PORTOFDISCHARGE) = '" + portcode.trim() + "'";
		        portcodeFlag = true;
		      }
		      String payrefno = ackVO.getBoePayRefEntered();
		      if (!commonMethods.isNull(payrefno))
		      {
		        BES_QUERY = BES_QUERY + " AND TRIM(PAYMENTREFNUMBER) = '" + payrefno.trim() + "'";
		        payrefnoFlag = true;
		      }
		      if (sAckStatus.equals("S"))
		      {
		        BES_QUERY = BES_QUERY + " AND ERRORCODES = 'SUCCESS'";
		      }
		      else if (sAckStatus.equals("F"))
		      {
		        BES_QUERY = BES_QUERY + " AND PAYMENTREFNUMBER NOT IN (SELECT PAYMENTREFNUMBER FROM ETT_BES_ACK WHERE ERRORCODES = 'SUCCESS')";
		      }
		      else
		      {
		        BES_QUERY1 = BES_QUERY.toString();
		        BES_QUERY = BES_QUERY + " AND ERRORCODES = 'SUCCESS' UNION ALL ";
		        BES_QUERY1 = BES_QUERY1 + " AND PAYMENTREFNUMBER NOT IN (SELECT PAYMENTREFNUMBER FROM ETT_BES_ACK WHERE ERRORCODES = 'SUCCESS')";
		        BES_QUERY = BES_QUERY + BES_QUERY1.toString();
		      }
		      pst = new LoggableStatement(con, BES_QUERY);
		      rs = pst.executeQuery();
		      while (rs.next())
		      {
		        ackVO = new AcknowledgementVO();
		        ackVO.setPortDischarge(rs.getString("PORTOFDISCHARGE"));
		        ackVO.setBoeNo(rs.getString("BOENUMBER"));
		        ackVO.setBoeDate(rs.getString("BOEDATE"));
		        ackVO.setBesIECode(rs.getString("IECODE"));
		        ackVO.setChanIECode(rs.getString("CHANGEIECODE"));
		        ackVO.setBesADCode(rs.getString("ADCODE"));
		        ackVO.setBesrecordInd(rs.getString("RECORDINDICATOR"));
		        ackVO.setPaymentParty(rs.getString("PAYMENTPARTY"));
		        ackVO.setPaymentRefNo(rs.getString("PAYMENTREFNUMBER"));
		        ackVO.setOrNo(rs.getString("ORNUMBER"));
		        ackVO.setOrADCode(rs.getString("ORADCODE"));
		        ackVO.setRemitCurr(rs.getString("REMITTCUR"));
		        ackVO.setBesbillCloInd(rs.getString("BILLCLOSUREINDI"));
		        ackVO.setInvoiceSNo(rs.getString("INVOICESERIALNO"));
		        ackVO.setInvoiceNo(rs.getString("INVOICENO"));
		        ackVO.setInvoiceAmt(rs.getString("INVOICEAMT"));
		        ackVO.setInvoiceAmtIC(rs.getString("INVOICEAMTIC"));
		        if (outRefNoFlag) {
		          ackVO.setOrmNo(outRefNo);
		        }
		        if (boeNoFlag) {
		          ackVO.setBoeNoEntered(boeNo);
		        }
		        if (boeDateFlag) {
		          ackVO.setBoeDateEntered(boeDate);
		        }
		        if (portcodeFlag) {
		          ackVO.setBoePortEntered(portcode);
		        }
		        if (payrefnoFlag) {
		          ackVO.setBoePayRefEntered(payrefno);
		        }
		        if (rs.getString("ERRORCODES") != null) {
		          errorCode = rs.getString("ERRORCODES");
		        }
		        ackVO.setBesErrorCodes(errorCode);

		 
		 
		        ArrayList<String> alStr = new ArrayList();
		        ArrayList<String> alStrDB = new ArrayList();
		        String[] splitErrorCodes = { "" };
		        if (errorCode.contains(","))
		        {
		          splitErrorCodes = errorCode.split(",");
		          sInParam = "";
		          for (int j = 0; j < splitErrorCodes.length; j++)
		          {
		            alStr.add(splitErrorCodes[j]);
		            if (sInParam.equals("")) {
		              sInParam = "'" + splitErrorCodes[j] + "'";
		            } else {
		              sInParam = sInParam + ",'" + splitErrorCodes[j] + "'";
		            }
		          }
		        }
		        else
		        {
		          alStr.add(errorCode);
		          sInParam = "'" + errorCode + "'";
		        }
		        sEerrDescQuery = "SELECT Trim(ERRCO) as ERRCO, ERRCO||'-'||ERRMSG ERRCO FROM (SELECT ERROR_CODE ERRCO, ERROR_MSG ERRMSG FROM ETT_IDPMS_ERROR_CODES WHERE trim(ERROR_CODE) IN (?) AND PCATEGORY = 'BES')";
		        pst1 = new LoggableStatement(con, sEerrDescQuery);
		        pst1.setString(1, sInParam);
		        rs1 = pst1.executeQuery();
		        while (rs1.next())
		        {
		          alStrDB.add(rs1.getString(1));
		          errorDesc = rs1.getString(2);
		          if (errorDesc1.isEmpty()) {
		            errorDesc1 = errorDesc.trim();
		          } else {
		            errorDesc1 = errorDesc1 + ",\n" + errorDesc.trim();
		          }
		        }
		        for (String temp : alStr) {
		          if (!alStrDB.contains(temp)) {
		            if (errorDesc1.isEmpty()) {
		              errorDesc1 = temp.trim();
		            } else {
		              errorDesc1 = errorDesc1 + ",\n" + temp.trim();
		            }
		          }
		        }
		        ackVO.setErrorDesc(errorDesc1);
		        errorDesc = "";
		        errorDesc1 = "";
		        rs1.close();
		        pst1.close();
		        if (errorCode.equals("SUCCESS"))
		        {
		          ackVO.setStatusField(errorCode);
		          successCount++;
		        }
		        if (!errorCode.equals("SUCCESS"))
		        {
		          ackVO.setStatusField("FAIL");
		          failCount++;
		        }
		        besAckList.add(ackVO);
		        iCount = besAckList.size();
		      }
		      logger.info(ackVO.getOrmNo() + " :: " + ackVO.getBoeNoEntered() + " :: " + ackVO.getBoeDateEntered() + " :: " + ackVO.getBoePortEntered() + " :: " + ackVO.getBoePayRefEntered());
		      ackVO.setSuccessCount(successCount);
		      ackVO.setFailCount(failCount);
		      ackVO.setBesAckValues(besAckList);
		      ackVO.setTotalCount(iCount);
		    }
		    catch (Exception exception)
		    {
		      exception.printStackTrace();
		      throwDAOException(exception);
		    }
		    finally
		    {
		      DBConnectionUtility.surrenderDB(con, pst, rs);
		    }
		    logger.info("Exiting Method");
		    return ackVO;
		  }
  d
  
  public boolean isDataAvailable(String tableName, String columnName1, String columnName2, String columnName3, String value1, String value2, String value3)

      throws DAOException

    {

      logger.error("Entering Method");

      Connection connection = null;

      LoggableStatement preparedStatement = null;

      ResultSet resultSet = null;

      boolean result = false;

      try

      {

        connection = DBConnectionUtility.getConnection();

        String query = "SELECT COUNT(1) FROM  TABLE_NAME  WHERE COLUMN_NAME1=? AND COLUMN_NAME2=? AND COLUMN_NAME3=?";

        query = query.replace("TABLE_NAME", tableName);

        query = query.replace("COLUMN_NAME1", columnName1);

        query = query.replace("COLUMN_NAME2", columnName2);

        query = query.replace("COLUMN_NAME3", columnName3);

        preparedStatement = new LoggableStatement(connection, query);

        preparedStatement.setString(1, value1);

        preparedStatement.setString(2, value2);

        preparedStatement.setString(3, value3);

        resultSet = preparedStatement.executeQuery();

        resultSet.next();

        if (resultSet.getInt(1) > 0) {

          result = true;

        } else {

          result = false;

        }

      }

      catch (Exception exception)

      {

        throwDAOException(exception);

      }

      finally

      {

        closeSqlRefferance(resultSet, preparedStatement, connection);

      }

      logger.info("Exiting Method");

      return result;

    }

    public boolean isGRShpDataAvailable(String tableName, String columnName1, String columnName2, String columnName3, String columnName4, String columnName5, String value1, String value2, String value3, String value4, String value5)

      throws DAOException

    {

      logger.error("Entering Method");

      Connection connection = null;

      LoggableStatement preparedStatement = null;

      ResultSet resultSet = null;

      boolean result = false;

      try

      {

        connection = DBConnectionUtility.getConnection();

        String query = "SELECT COUNT(1) FROM  TABLE_NAME  WHERE COLUMN_NAME1=? AND COLUMN_NAME2=? AND COLUMN_NAME3=? AND COLUMN_NAME4=? AND COLUMN_NAME5=?";

        query = query.replace("TABLE_NAME", tableName);

        query = query.replace("COLUMN_NAME1", columnName1);

        query = query.replace("COLUMN_NAME2", columnName2);

        query = query.replace("COLUMN_NAME3", columnName3);

        query = query.replace("COLUMN_NAME4", columnName4);

        query = query.replace("COLUMN_NAME5", columnName5);

        preparedStatement = new LoggableStatement(connection, query);

        preparedStatement.setString(1, value1);

        preparedStatement.setString(2, value2);

        preparedStatement.setString(3, value3);

        preparedStatement.setString(4, value4);

        preparedStatement.setString(5, value5);

        resultSet = preparedStatement.executeQuery();

        resultSet.next();

        if (resultSet.getInt(1) > 0) {

          result = true;

        } else {

          result = false;

        }

      }

      catch (Exception exception)

      {

        throwDAOException(exception);

      }

      finally

      {

        closeSqlRefferance(resultSet, preparedStatement, connection);

      }

      logger.info("Exiting Method");

      return result;

    }

    public boolean isGRInvDataAvailable(String tableName, String columnName1, String columnName2, String columnName3, String columnName4, String value1, String value2, String value3, String value4)

      throws DAOException

    {

      logger.error("Entering Method");

      Connection connection = null;

      LoggableStatement preparedStatement = null;

      ResultSet resultSet = null;

      boolean result = false;

      try

      {

        connection = DBConnectionUtility.getConnection();

        String query = "SELECT COUNT(1) FROM  TABLE_NAME  WHERE COLUMN_NAME1=? AND COLUMN_NAME2=? AND COLUMN_NAME3=? AND COLUMN_NAME4=?";

        query = query.replace("TABLE_NAME", tableName);

        query = query.replace("COLUMN_NAME1", columnName1);

        query = query.replace("COLUMN_NAME2", columnName2);

        query = query.replace("COLUMN_NAME3", columnName3);

        query = query.replace("COLUMN_NAME4", columnName4);

        preparedStatement = new LoggableStatement(connection, query);

        preparedStatement.setString(1, value1);

        preparedStatement.setString(2, value2);

        preparedStatement.setString(3, value3);

        preparedStatement.setString(4, value4);

        resultSet = preparedStatement.executeQuery();

        resultSet.next();

        if (resultSet.getInt(1) > 0) {

          result = true;

        } else {

          result = false;

        }

      }

      catch (Exception exception)

      {

        throwDAOException(exception);

      }

      finally

      {

        closeSqlRefferance(resultSet, preparedStatement, connection);

      }

      logger.info("Exiting Method");

      return result;

    }

    public boolean isFourDataAvailable(String tableName, String columnName1, String columnName2, String columnName3, String columnName4, String value1, String value2, String value3, String value4)

      throws DAOException

    {

      logger.error("Entering Method");

      Connection connection = null;

      LoggableStatement preparedStatement = null;

      ResultSet resultSet = null;

      boolean result = false;

      try

      {

        connection = DBConnectionUtility.getConnection();

        String query = "SELECT COUNT(1) FROM  TABLE_NAME  WHERE COLUMN_NAME1=? AND COLUMN_NAME2=? AND COLUMN_NAME3=? AND COLUMN_NAME4=?";

        query = query.replace("TABLE_NAME", tableName);

        query = query.replace("COLUMN_NAME1", columnName1);

        query = query.replace("COLUMN_NAME2", columnName2);

        query = query.replace("COLUMN_NAME3", columnName3);

        query = query.replace("COLUMN_NAME4", columnName4);

        preparedStatement = new LoggableStatement(connection, query);

        preparedStatement.setString(1, value1);

        preparedStatement.setString(2, value2);

        preparedStatement.setString(3, value3);

        preparedStatement.setString(4, value4);

        resultSet = preparedStatement.executeQuery();

        resultSet.next();

        if (resultSet.getInt(1) > 0) {

          result = true;

        } else {

          result = false;

        }

      }

      catch (Exception exception)

      {

        throwDAOException(exception);

      }

      finally

      {

        closeSqlRefferance(resultSet, preparedStatement, connection);

      }

      logger.info("Exiting Method");

      return result;

    }

    public static String getErrorDesc(String errorCD, String screenId)

    {

      String errorMag = "";

      Connection con = null;

      PreparedStatement pst = null;

      ResultSet rs = null;

      try

      {

        con = DBConnectionUtility.getConnection();

        String query = "SELECT ERROR_MSG FROM ETT_ERROR_CODES WHERE ERROR_CODE=? AND SCREEN_ID=?";

        pst = con.prepareStatement(query);

        pst.setString(1, errorCD);

        pst.setString(2, screenId);

        rs = pst.executeQuery();

        while (rs.next()) {

          errorMag = rs.getString(1);

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

      return errorMag;

    }
   
  d
   
  public boolean isTIShpDataAvailable(String value1, String value2, String value3)

      throws DAOException

    {

      logger.error("Entering Method");

      Connection connection = null;

      LoggableStatement preparedStatement = null;

      ResultSet resultSet = null;

      boolean result = false;

      try

      {

        connection = DBConnectionUtility.getConnection();

        String query = "SELECT COUNT(1) FROM EXTEVENTSPD  WHERE BILLNO=? AND TO_CHAR(TO_DATE(BILLDATE, 'dd-mm-yy'),'ddmmyyyy')=? AND PORTCODE=?";

   
        preparedStatement = new LoggableStatement(connection, query);

        preparedStatement.setString(1, value1);

        preparedStatement.setString(2, value2);

        preparedStatement.setString(3, value3);

        resultSet = preparedStatement.executeQuery();

        resultSet.next();

        if (resultSet.getInt(1) > 0) {

          result = true;

        } else {

          result = false;

        }

      }

      catch (Exception exception)

      {

        throwDAOException(exception);

      }

      finally

      {

        closeSqlRefferance(resultSet, preparedStatement, connection);

      }

      logger.info("Exiting Method");

      return result;

    }

    public boolean isTIInvDataAvailable(String value1, String value2, String value3)

      throws DAOException

    {

      logger.error("Entering Method");

      Connection connection = null;

      LoggableStatement preparedStatement = null;

      ResultSet resultSet = null;

      boolean result = false;

      try

      {

        connection = DBConnectionUtility.getConnection();

        String query = "SELECT COUNT(1) FROM EXTEVENTINV  WHERE INVBLLNO=? AND TO_CHAR(TO_DATE(INBLLDAT, 'dd-mm-yy'),'ddmmyyyy')=? AND INVPRTCD=?";

   
        preparedStatement = new LoggableStatement(connection, query);

        preparedStatement.setString(1, value1);

        preparedStatement.setString(2, value2);

        preparedStatement.setString(3, value3);

        resultSet = preparedStatement.executeQuery();

        resultSet.next();

        if (resultSet.getInt(1) > 0) {

          result = true;

        } else {

          result = false;

        }

      }

      catch (Exception exception)

      {

        throwDAOException(exception);

      }

      finally

      {

        closeSqlRefferance(resultSet, preparedStatement, connection);

      }

      logger.info("Exiting Method");

      return result;

    }

    public boolean isGRShpRelDataAvailable(String tableName, String columnName1, String columnName2, String columnName3, String columnName4, String columnName5, String columnName6, String value1, String value2, String value3, String value4, String value5, String value6)

      throws DAOException

    {

      logger.error("Entering Method");

      Connection connection = null;

      LoggableStatement preparedStatement = null;

      ResultSet resultSet = null;

      boolean result = false;

      try

      {

        connection = DBConnectionUtility.getConnection();

        String query = "SELECT COUNT(1) FROM  TABLE_NAME  WHERE COLUMN_NAME1=? AND COLUMN_NAME2=? AND COLUMN_NAME3=?  AND COLUMN_NAME4=? AND COLUMN_NAME5=? AND COLUMN_NAME6=?";

        query = query.replace("TABLE_NAME", tableName);

        query = query.replace("COLUMN_NAME1", columnName1);

        query = query.replace("COLUMN_NAME2", columnName2);

        query = query.replace("COLUMN_NAME3", columnName3);

        query = query.replace("COLUMN_NAME4", columnName4);

        query = query.replace("COLUMN_NAME5", columnName5);

        query = query.replace("COLUMN_NAME6", columnName6);

        preparedStatement = new LoggableStatement(connection, query);

        preparedStatement.setString(1, value1);

        preparedStatement.setString(2, value2);

        preparedStatement.setString(3, value3);

        preparedStatement.setString(4, value4);

        preparedStatement.setString(5, value5);

        preparedStatement.setString(6, value6);

        resultSet = preparedStatement.executeQuery();

        resultSet.next();

        if (resultSet.getInt(1) > 0) {

          result = true;

        } else {

          result = false;

        }

      }

      catch (Exception exception)

      {

        throwDAOException(exception);

      }

      finally

      {

        closeSqlRefferance(resultSet, preparedStatement, connection);

      }

      logger.info("Exiting Method");

      return result;

    }

    public boolean isGRInvRelDataAvailable(String tableName, String columnName1, String columnName2, String columnName3, String columnName4, String columnName5, String columnName6, String columnName7, String columnName8, String value1, String value2, String value3, String value4, String value5, String value6, String value7, String value8)

      throws DAOException

    {

      logger.error("Entering Method");

      Connection connection = null;

      LoggableStatement preparedStatement = null;

      ResultSet resultSet = null;

      boolean result = false;

      try

      {

        connection = DBConnectionUtility.getConnection();

        String query = "SELECT COUNT(1) FROM  TABLE_NAME  WHERE COLUMN_NAME1=? AND COLUMN_NAME2=? AND COLUMN_NAME3=? \tAND COLUMN_NAME4=? AND COLUMN_NAME5=? AND COLUMN_NAME6=? AND COLUMN_NAME7=? AND COLUMN_NAME8=? ";

        query = query.replace("TABLE_NAME", tableName);

        query = query.replace("COLUMN_NAME1", columnName1);

        query = query.replace("COLUMN_NAME2", columnName2);

        query = query.replace("COLUMN_NAME3", columnName3);

        query = query.replace("COLUMN_NAME4", columnName4);

        query = query.replace("COLUMN_NAME5", columnName5);

        query = query.replace("COLUMN_NAME6", columnName6);

        query = query.replace("COLUMN_NAME7", columnName7);

        query = query.replace("COLUMN_NAME8", columnName8);

        preparedStatement = new LoggableStatement(connection, query);

        preparedStatement.setString(1, value1);

        preparedStatement.setString(2, value2);

        preparedStatement.setString(3, value3);

        preparedStatement.setString(4, value4);

        preparedStatement.setString(5, value5);

        preparedStatement.setString(6, value6);

        preparedStatement.setString(7, value7);

        preparedStatement.setString(8, value8);

        resultSet = preparedStatement.executeQuery();

        resultSet.next();

        if (resultSet.getInt(1) > 0) {

          result = true;

        } else {

          result = false;

        }

      }

      catch (Exception exception)

      {

        throwDAOException(exception);

      }

      finally

      {

        closeSqlRefferance(resultSet, preparedStatement, connection);

      }

      logger.info("Exiting Method");

      return result;

    }

  }
   
  d
   
  package in.co.localization.dao.localization;
   
  import in.co.localization.dao.AbstractDAO;

  import in.co.localization.dao.exception.DAOException;

  import in.co.localization.utility.ActionConstantsQuery;

  import in.co.localization.utility.CommonMethods;

  import in.co.localization.utility.DBConnectionUtility;

  import in.co.localization.utility.LoggableStatement;

  import in.co.localization.vo.localization.AcknowledgementVO;

  import java.sql.CallableStatement;

  import java.sql.Connection;

  import java.sql.ResultSet;

  import java.sql.SQLException;

  import java.util.ArrayList;

  import javax.servlet.http.HttpServletRequest;

  import javax.servlet.http.HttpSession;

  import org.apache.log4j.Logger;

  import org.apache.struts2.ServletActionContext;
   
  public class AcknowledgementDAO

    extends AbstractDAO

    implements ActionConstantsQuery

  {

    static AcknowledgementDAO dao;

    String bType = null;

    private static Logger logger = Logger.getLogger(AcknowledgementDAO.class.getName());

    public static AcknowledgementDAO getDAO()

    {

      if (dao == null) {

        dao = new AcknowledgementDAO();

      }

      return dao;

    }

    public AcknowledgementVO fetchOrmData(AcknowledgementVO ackVO)

      throws DAOException

    {

      logger.info("--------------fetchOrmData--------------");

      int successCount = 0;

      int failCount = 0;

      int iCount = 0;

      String sInParam = "";

      String errorDesc = "";

      String errorDesc1 = "";

      String sEerrDescQuery = "";

      String errorCode = "";

      CommonMethods commonMethods = null;

      String ormNo = "";

      String paymDate = "";

      String adCodde = "";

      String ieCodde = "";

      String sAckStatus = null;

      String ORM_QUERY = null;

      String ORM_QUERY1 = null;

      ArrayList<AcknowledgementVO> ormAckList = null;

      LoggableStatement pst = null;

      LoggableStatement pst1 = null;

      ResultSet rs = null;

      ResultSet rs1 = null;

      Connection con = null;

      boolean ormNoFlag = false;

      boolean paymDateFlag = false;

      boolean adCoddeFlag = false;

      boolean ieCoddeFlag = false;

      int setValue = 0;

      try

      {

        con = DBConnectionUtility.getConnection();

        ormAckList = new ArrayList();

   
   
        commonMethods = new CommonMethods();

        sAckStatus = ackVO.getAckStatus();

        ORM_QUERY = "SELECT OUTWARDREFERNECNO,ADCODE,AMOUNT,CURR,TO_CHAR(TO_DATE(PAYDATE,'DD-MM-YY'),'DD/MM/YYYY') AS PAYDATE, IECODE,IENAME,IEADDRESS,IEPANNO,ISCAPITAL,BENNAME,BENACCNO,BENCOUNTRY,SWIFT,PURPCODE,RECORDINDICATOR,REMARKS,\tPAYTERMS,ERRORCODES FROM ETT_ORM_ACK WHERE OUTWARDREFERNECNO = OUTWARDREFERNECNO ";

   
   
        ormNo = ackVO.getOutRemNo();

        if (!commonMethods.isNull(ormNo))

        {

          ORM_QUERY = ORM_QUERY + " AND OUTWARDREFERNECNO = '" + ormNo.trim() + "'";

          ormNoFlag = true;

        }

        paymDate = commonMethods.getEmptyIfNull(ackVO.getPaymDate());

        if (!commonMethods.isNull(paymDate))

        {

          ORM_QUERY = ORM_QUERY + " AND TO_CHAR(TO_DATE(PAYDATE,'DD-MM-YY'),'DD/MM/YYYY') = '" + paymDate.trim() + "'";

          paymDateFlag = true;

        }

        adCodde = ackVO.getAdCodde();

        if (!commonMethods.isNull(adCodde))

        {

          ORM_QUERY = ORM_QUERY + " AND ADCODE =  '" + adCodde.trim() + "'";

          adCoddeFlag = true;

        }

        ieCodde = ackVO.getIeCodde();

        if (!commonMethods.isNull(ieCodde))

        {

          ORM_QUERY = ORM_QUERY + " AND IECODE =  '" + ieCodde.trim() + "'";

          ieCoddeFlag = true;

        }

        if (sAckStatus.equals("S"))

        {

          ORM_QUERY = ORM_QUERY + " AND ERRORCODES = 'SUCCESS'";

        }

        else if (sAckStatus.equals("F"))

        {

          ORM_QUERY = ORM_QUERY + " AND OUTWARDREFERNECNO NOT IN (SELECT OUTWARDREFERNECNO FROM ETT_ORM_ACK WHERE ERRORCODES = 'SUCCESS')";

        }

        else

        {

          ORM_QUERY1 = ORM_QUERY.toString();

          ORM_QUERY = ORM_QUERY + " AND ERRORCODES = 'SUCCESS' UNION ALL ";

          ORM_QUERY1 = ORM_QUERY1 + " AND OUTWARDREFERNECNO NOT IN (SELECT OUTWARDREFERNECNO FROM ETT_ORM_ACK WHERE ERRORCODES = 'SUCCESS')";

          ORM_QUERY = ORM_QUERY + ORM_QUERY1.toString();

        }

        pst = new LoggableStatement(con, ORM_QUERY);

   
        logger.info("--------------fetchOrmData-------query-------" + pst.getQueryString());

        rs = pst.executeQuery();

        while (rs.next())

        {

          ackVO = new AcknowledgementVO();

          ackVO.setOrmNo(rs.getString("OUTWARDREFERNECNO"));

          ackVO.setAdcode(rs.getString("ADCODE"));

          ackVO.setAmount(rs.getString("AMOUNT"));

          ackVO.setCurr(rs.getString("CURR"));

          ackVO.setPayDate(rs.getString("PAYDATE"));

          ackVO.setIeCode(rs.getString("IECODE"));

          ackVO.setIeName(rs.getString("IENAME"));

          ackVO.setIeAddress(rs.getString("IEADDRESS"));

          ackVO.setIePanNo(rs.getString("IEPANNO"));

          ackVO.setIsCapital(rs.getString("ISCAPITAL"));

          ackVO.setBenName(rs.getString("BENNAME"));

          ackVO.setBenAccNo(rs.getString("BENACCNO"));

          ackVO.setBenCountry(rs.getString("BENCOUNTRY"));

          ackVO.setSwift(rs.getString("SWIFT"));

          ackVO.setPurpCode(rs.getString("PURPCODE"));

          ackVO.setRecordInd(rs.getString("RECORDINDICATOR"));

          ackVO.setRemarks(rs.getString("REMARKS"));

          ackVO.setPayTerms(rs.getString("PAYTERMS"));

          if (rs.getString("ERRORCODES") != null) {

            errorCode = rs.getString("ERRORCODES");

          }

          if (ormNoFlag) {

            ackVO.setOutRemNo(rs.getString("OUTWARDREFERNECNO"));

          }

          if (paymDateFlag) {

            ackVO.setPaymDate(rs.getString("PAYDATE"));

          }

          if (adCoddeFlag) {

            ackVO.setAdCodde(rs.getString("ADCODE"));

          }

          if (ieCoddeFlag) {

            ackVO.setIeCodde(rs.getString("IECODE"));

          }

          ackVO.setErrorDes(errorCode);

   
          ArrayList<String> alStr = new ArrayList();

          ArrayList<String> alStrDB = new ArrayList();

          String[] splitErrorCodes = { "" };

          if (errorCode.contains(","))

          {

            splitErrorCodes = errorCode.split(",");

            sInParam = "";

            for (int j = 0; j < splitErrorCodes.length; j++)

            {

              alStr.add(splitErrorCodes[j]);

              if (sInParam.equals("")) {

                sInParam = "'" + splitErrorCodes[j] + "'";

              } else {

                sInParam = sInParam + ",'" + splitErrorCodes[j] + "'";

              }

            }

          }

          else

          {

            alStr.add(errorCode);

            sInParam = "'" + errorCode + "'";

          }

          sEerrDescQuery = "SELECT Trim(ERRCO) as ERRCO, ERRCO||'-'||ERRMSG ERRCO FROM (SELECT ERROR_CODE ERRCO, ERROR_MSG ERRMSG FROM ETT_IDPMS_ERROR_CODES WHERE trim(ERROR_CODE) IN (?) AND PCATEGORY = 'ORM')";

          pst1 = new LoggableStatement(con, sEerrDescQuery);

          pst1.setString(1, sInParam);

          logger.info("--------------fetchOrmData---111111----query-------" + pst1.getQueryString());

          rs1 = pst1.executeQuery();

          while (rs1.next())

          {

            alStrDB.add(rs1.getString(1));

            errorDesc = rs1.getString(2);

            if (errorDesc1.isEmpty()) {

              errorDesc1 = errorDesc.trim();

            } else {

              errorDesc1 = errorDesc1 + ",\n" + errorDesc.trim();

            }

          }

          for (String temp : alStr) {

            if (!alStrDB.contains(temp)) {

              if (errorDesc1.isEmpty()) {

                errorDesc1 = temp.trim();

              } else {

                errorDesc1 = errorDesc1 + ",\n" + temp.trim();

              }

            }

          }

          ackVO.setErrorDesc(errorDesc1);

          errorDesc = "";

          errorDesc1 = "";

          rs1.close();

          pst1.close();

          if (errorCode.equals("SUCCESS"))

          {

            ackVO.setStatusField(errorCode);

            successCount++;

          }

          if (!errorCode.equals("SUCCESS"))

          {

            ackVO.setStatusField("FAIL");

            failCount++;

          }

          ormAckList.add(ackVO);

          iCount = ormAckList.size();

        }

        ackVO.setAckStatus(sAckStatus);

        ackVO.setSuccessCount(successCount);

        ackVO.setFailCount(failCount);

        ackVO.setOrmAckValues(ormAckList);

        ackVO.setTotalCount(iCount);

      }

      catch (Exception exception)

      {

        logger.info("--------------fetchOrmData------exception--------" + exception);

        exception.printStackTrace();

        throwDAOException(exception);

      }

      finally

      {

        DBConnectionUtility.surrenderDB(con, pst, rs);

      }

      logger.info("Exiting Method");

      return ackVO;

    }
   
  
   
    public AcknowledgementVO fetchBesData(AcknowledgementVO ackVO)

      throws DAOException

    {

      logger.info("Entering Method");

      int successCount = 0;

      int failCount = 0;

      int iCount = 0;

      String sInParam = "";

      String errorDesc = "";

      String errorDesc1 = "";

      String sEerrDescQuery = "";

      String errorCode = "";

      ArrayList<AcknowledgementVO> besAckList = null;

      LoggableStatement pst = null;

      ResultSet rs = null;

      Connection con = null;

      String query = null;

      LoggableStatement pst1 = null;

      ResultSet rs1 = null;

      CommonMethods commonMethods = null;

      String BES_QUERY = null;

      String BES_QUERY1 = null;

      String sAckStatus = "";

      boolean outRefNoFlag = false;

      boolean boeNoFlag = false;

      boolean boeDateFlag = false;

      boolean portcodeFlag = false;

      boolean payrefnoFlag = false;

      int setValue = 0;

      try

      {

        con = DBConnectionUtility.getConnection();

   
        besAckList = new ArrayList();

   
   
        sAckStatus = ackVO.getAckStatus();

        commonMethods = new CommonMethods();

        BES_QUERY = "SELECT PORTOFDISCHARGE,BOENUMBER,TO_CHAR(TO_DATE(BOEDATE,'DD-MM-YY'),'DD/MM/YYYY') AS BOEDATE,IECODE,CHANGEIECODE,ADCODE,RECORDINDICATOR,PAYMENTPARTY,PAYMENTREFNUMBER,ORNUMBER,ORADCODE,REMITTCUR,BILLCLOSUREINDI,INVOICESERIALNO,INVOICENO,INVOICEAMTIC,INVOICEAMT,ERRORCODES FROM ETT_BES_ACK WHERE BOENUMBER = BOENUMBER  ";

   
   
        String outRefNo = ackVO.getOrmNo();

        if (!commonMethods.isNull(outRefNo))

        {

          BES_QUERY = BES_QUERY + " AND TRIM(ORNUMBER) ='" + outRefNo.trim() + "'";

          outRefNoFlag = true;

        }

        String boeNo = ackVO.getBoeNoEntered();

        if (!commonMethods.isNull(boeNo))

        {

          BES_QUERY = BES_QUERY + " AND TRIM(BOENUMBER) ='" + boeNo.trim() + "'";

          boeNoFlag = true;

        }

        String boeDate = commonMethods.getEmptyIfNull(ackVO.getBoeDateEntered());

        if (!commonMethods.isNull(boeDate))

        {

          BES_QUERY = BES_QUERY + " AND TO_CHAR(TO_DATE(BOEDATE,'DD-MM-YY'),'DD/MM/YYYY') ='" + boeDate.trim() + "'";

          boeDateFlag = true;

        }

        String portcode = commonMethods.getEmptyIfNull(ackVO.getBoePortEntered());

        if (!commonMethods.isNull(portcode))

        {

          BES_QUERY = BES_QUERY + " AND TRIM(PORTOFDISCHARGE) = '" + portcode.trim() + "'";

          portcodeFlag = true;

        }

        String payrefno = ackVO.getBoePayRefEntered();

        if (!commonMethods.isNull(payrefno))

        {

          BES_QUERY = BES_QUERY + " AND TRIM(PAYMENTREFNUMBER) = '" + payrefno.trim() + "'";

          payrefnoFlag = true;

        }

        if (sAckStatus.equals("S"))

        {

          BES_QUERY = BES_QUERY + " AND ERRORCODES = 'SUCCESS'";

        }

        else if (sAckStatus.equals("F"))

        {

          BES_QUERY = BES_QUERY + " AND PAYMENTREFNUMBER NOT IN (SELECT PAYMENTREFNUMBER FROM ETT_BES_ACK WHERE ERRORCODES = 'SUCCESS')";

        }

        else

        {

          BES_QUERY1 = BES_QUERY.toString();

          BES_QUERY = BES_QUERY + " AND ERRORCODES = 'SUCCESS' UNION ALL ";

          BES_QUERY1 = BES_QUERY1 + " AND PAYMENTREFNUMBER NOT IN (SELECT PAYMENTREFNUMBER FROM ETT_BES_ACK WHERE ERRORCODES = 'SUCCESS')";

          BES_QUERY = BES_QUERY + BES_QUERY1.toString();

        }

        pst = new LoggableStatement(con, BES_QUERY);

        rs = pst.executeQuery();

        while (rs.next())

        {

          ackVO = new AcknowledgementVO();

          ackVO.setPortDischarge(rs.getString("PORTOFDISCHARGE"));

          ackVO.setBoeNo(rs.getString("BOENUMBER"));

          ackVO.setBoeDate(rs.getString("BOEDATE"));

          ackVO.setBesIECode(rs.getString("IECODE"));

          ackVO.setChanIECode(rs.getString("CHANGEIECODE"));

          ackVO.setBesADCode(rs.getString("ADCODE"));

          ackVO.setBesrecordInd(rs.getString("RECORDINDICATOR"));

          ackVO.setPaymentParty(rs.getString("PAYMENTPARTY"));

          ackVO.setPaymentRefNo(rs.getString("PAYMENTREFNUMBER"));

          ackVO.setOrNo(rs.getString("ORNUMBER"));

          ackVO.setOrADCode(rs.getString("ORADCODE"));

          ackVO.setRemitCurr(rs.getString("REMITTCUR"));

          ackVO.setBesbillCloInd(rs.getString("BILLCLOSUREINDI"));

          ackVO.setInvoiceSNo(rs.getString("INVOICESERIALNO"));

          ackVO.setInvoiceNo(rs.getString("INVOICENO"));

          ackVO.setInvoiceAmt(rs.getString("INVOICEAMT"));

          ackVO.setInvoiceAmtIC(rs.getString("INVOICEAMTIC"));

          if (outRefNoFlag) {

            ackVO.setOrmNo(outRefNo);

          }

          if (boeNoFlag) {

            ackVO.setBoeNoEntered(boeNo);

          }

          if (boeDateFlag) {

            ackVO.setBoeDateEntered(boeDate);

          }

          if (portcodeFlag) {

            ackVO.setBoePortEntered(portcode);

          }

          if (payrefnoFlag) {

            ackVO.setBoePayRefEntered(payrefno);

          }

          if (rs.getString("ERRORCODES") != null) {

            errorCode = rs.getString("ERRORCODES");

          }

          ackVO.setBesErrorCodes(errorCode);

   
   
          ArrayList<String> alStr = new ArrayList();

          ArrayList<String> alStrDB = new ArrayList();

          String[] splitErrorCodes = { "" };

          if (errorCode.contains(","))

          {

            splitErrorCodes = errorCode.split(",");

            sInParam = "";

            for (int j = 0; j < splitErrorCodes.length; j++)

            {

              alStr.add(splitErrorCodes[j]);

              if (sInParam.equals("")) {

                sInParam = "'" + splitErrorCodes[j] + "'";

              } else {

                sInParam = sInParam + ",'" + splitErrorCodes[j] + "'";

              }

            }

          }

          else

          {

            alStr.add(errorCode);

            sInParam = "'" + errorCode + "'";

          }

          sEerrDescQuery = "SELECT Trim(ERRCO) as ERRCO, ERRCO||'-'||ERRMSG ERRCO FROM (SELECT ERROR_CODE ERRCO, ERROR_MSG ERRMSG FROM ETT_IDPMS_ERROR_CODES WHERE trim(ERROR_CODE) IN (?) AND PCATEGORY = 'BES')";

          pst1 = new LoggableStatement(con, sEerrDescQuery);

          pst1.setString(1, sInParam);

          rs1 = pst1.executeQuery();

          while (rs1.next())

          {

            alStrDB.add(rs1.getString(1));

            errorDesc = rs1.getString(2);

            if (errorDesc1.isEmpty()) {

              errorDesc1 = errorDesc.trim();

            } else {

              errorDesc1 = errorDesc1 + ",\n" + errorDesc.trim();

            }

          }

          for (String temp : alStr) {

            if (!alStrDB.contains(temp)) {

              if (errorDesc1.isEmpty()) {

                errorDesc1 = temp.trim();

              } else {

                errorDesc1 = errorDesc1 + ",\n" + temp.trim();

              }

            }

          }

          ackVO.setErrorDesc(errorDesc1);

          errorDesc = "";

          errorDesc1 = "";

          rs1.close();

          pst1.close();

          if (errorCode.equals("SUCCESS"))

          {

            ackVO.setStatusField(errorCode);

            successCount++;

          }

          if (!errorCode.equals("SUCCESS"))

          {

            ackVO.setStatusField("FAIL");

            failCount++;

          }

          besAckList.add(ackVO);

          iCount = besAckList.size();

        }

        logger.info(ackVO.getOrmNo() + " :: " + ackVO.getBoeNoEntered() + " :: " + ackVO.getBoeDateEntered() + " :: " + ackVO.getBoePortEntered() + " :: " + ackVO.getBoePayRefEntered());

        ackVO.setSuccessCount(successCount);

        ackVO.setFailCount(failCount);

        ackVO.setBesAckValues(besAckList);

        ackVO.setTotalCount(iCount);

      }

      catch (Exception exception)

      {

        exception.printStackTrace();

        throwDAOException(exception);

      }

      finally

      {

        DBConnectionUtility.surrenderDB(con, pst, rs);

      }

      logger.info("Exiting Method");

      return ackVO;

    }

   
  
   
  public AcknowledgementVO fetchBeeData(AcknowledgementVO ackVO)

      throws DAOException

    {

      logger.info("--------------fetchBeeData-----------");

      int iCount = 0;

      int successCount = 0;

      int failCount = 0;

      String sInParam = "";

      String errorDesc = "";

      String errorDesc1 = "";

      String sEerrDescQuery = "";

      String errorCode = "";

      ArrayList<AcknowledgementVO> beeAckList = null;

      LoggableStatement pst = null;

      ResultSet rs = null;

      Connection con = null;

      String query = null;

      LoggableStatement pst1 = null;

      ResultSet rs1 = null;

      String sAckStatus = "";

      CommonMethods commonMethods = null;

      String BEE_QUERY = null;

      String BEE_QUERY1 = null;

      boolean boeNoFlag = false;

      boolean boeDateFlag = false;

      boolean portCodeFlag = false;

      boolean ieCodeFlag = false;

      boolean adCodeFlag = false;

      int setValue = 0;

      try

      {

        con = DBConnectionUtility.getConnection();

        beeAckList = new ArrayList();

   
   
        sAckStatus = ackVO.getAckStatus();

        commonMethods = new CommonMethods();

        BEE_QUERY = "SELECT PORTOFDISCHARGE,BOENUMBER,TO_CHAR(BOEDATE,'DD/MM/YYYY') AS BOEDATE,IECODE,ADCODE,RECORDINDICATOR,EXTENSIONBY AS EXTENSIONBY,LETTERNO AS LETTERNO,TO_CHAR(LETTERDATE,'DD/MM/YYYY') AS LETTERDATE,TO_CHAR(EXTENSIONDATE,'DD/MM/YYYY') AS EXTENSIONDATE,REMARKS,ERRORCODES  FROM ETT_BOE_EXT_ACK WHERE BOENUMBER = BOENUMBER ";

   
   
   
        String boeNo = ackVO.getBoeNo();

        if (!commonMethods.isNull(boeNo))

        {

          BEE_QUERY = BEE_QUERY + " AND TRIM(BOENUMBER) ='" + boeNo.trim() + "'";

          boeNoFlag = true;

        }

        String boeDate = commonMethods.getEmptyIfNull(ackVO.getBoeDate());

        if (!commonMethods.isNull(boeDate))

        {

          BEE_QUERY = BEE_QUERY + " AND TO_CHAR(TO_DATE(BOEDATE,'DD-MM-YY'),'DD/MM/YYYY') = '" + boeDate.trim() + "'";

          boeDateFlag = true;

        }

        String portCode = ackVO.getPortCode();

        if (!commonMethods.isNull(portCode))

        {

          BEE_QUERY = BEE_QUERY + " AND TRIM(PORTOFDISCHARGE) ='" + portCode.trim() + "'";

          portCodeFlag = true;

        }

        String ieCode = ackVO.getBesIECode();

        if (!commonMethods.isNull(ieCode))

        {

          BEE_QUERY = BEE_QUERY + " AND TRIM(IECODE) =  '" + ieCode.trim() + "'";

          ieCodeFlag = true;

        }

        String adCode = ackVO.getBesADCode();

        if (!commonMethods.isNull(adCode))

        {

          BEE_QUERY = BEE_QUERY + " AND TRIM(ADCODE) = '" + adCode.trim() + "'";

          adCodeFlag = true;

        }

        if (sAckStatus.equals("S"))

        {

          BEE_QUERY = BEE_QUERY + " AND ERRORCODES = 'SUCCESS'";

        }

        else if (sAckStatus.equals("F"))

        {

          BEE_QUERY = BEE_QUERY + " AND BOENUMBER NOT IN (SELECT BOENUMBER FROM ETT_BOE_EXT_ACK WHERE ERRORCODES = 'SUCCESS')";

        }

        else

        {

          BEE_QUERY1 = BEE_QUERY.toString();

          BEE_QUERY = BEE_QUERY + " AND ERRORCODES = 'SUCCESS' UNION ALL ";

          BEE_QUERY1 = BEE_QUERY1 + " AND BOENUMBER NOT IN (SELECT BOENUMBER FROM ETT_BOE_EXT_ACK WHERE ERRORCODES = 'SUCCESS')";

          BEE_QUERY = BEE_QUERY + BEE_QUERY1.toString();

        }

        pst = new LoggableStatement(con, BEE_QUERY);

   
   
        logger.info("--------------fetchBeeData------query-----" + pst.getQueryString());

        rs = pst.executeQuery();

        while (rs.next())

        {

          ackVO = new AcknowledgementVO();

          ackVO.setPortDischarge(rs.getString("PORTOFDISCHARGE"));

          ackVO.setBoeNoVal(rs.getString("BOENUMBER"));

          ackVO.setBoeDateVal(rs.getString("BOEDATE"));

          ackVO.setIeCodeVal(rs.getString("IECODE"));

          ackVO.setAdCodeVal(rs.getString("ADCODE"));

          ackVO.setBesrecordInd(rs.getString("RECORDINDICATOR"));

          ackVO.setExtBy(rs.getString("EXTENSIONBY"));

          ackVO.setLetterNo(rs.getString("LETTERNO"));

          ackVO.setLetterDate(rs.getString("LETTERDATE"));

          ackVO.setExtRealDate(rs.getString("EXTENSIONDATE"));

          String remarks = rs.getString("REMARKS");

          if (remarks != null) {

            remarks = remarks.replaceAll("[^A-Za-z0-9 ]", "");

          }

          if (boeNoFlag) {

            ackVO.setBoeNo(boeNo.trim());

          }

          if (boeDateFlag) {

            ackVO.setBoeDate(boeDate.trim());

          }

          if (portCodeFlag) {

            ackVO.setPortCode(portCode.trim());

          }

          if (ieCodeFlag) {

            ackVO.setBesIECode(ieCode.trim());

          }

          if (adCodeFlag) {

            ackVO.setBesADCode(adCode.trim());

          }

          ackVO.setRemarks(remarks);

          if (rs.getString("ERRORCODES") != null) {

            errorCode = rs.getString("ERRORCODES");

          }

          ackVO.setBesErrorCodes(errorCode);

   
   
          ArrayList<String> alStr = new ArrayList();

          ArrayList<String> alStrDB = new ArrayList();

          String[] splitErrorCodes = { "" };

          if (errorCode.contains(","))

          {

            splitErrorCodes = errorCode.split(",");

            sInParam = "";

            for (int j = 0; j < splitErrorCodes.length; j++)

            {

              alStr.add(splitErrorCodes[j]);

              if (sInParam.equals("")) {

                sInParam = "'" + splitErrorCodes[j] + "'";

              } else {

                sInParam = sInParam + ",'" + splitErrorCodes[j] + "'";

              }

            }

          }

          else

          {

            alStr.add(errorCode);

            sInParam = "'" + errorCode + "'";

          }

          sEerrDescQuery = "SELECT Trim(ERRCO) as ERRCO, ERRCO||'-'||ERRMSG ERRCO FROM (SELECT ERROR_CODE ERRCO, ERROR_MSG ERRMSG FROM ETT_IDPMS_ERROR_CODES WHERE trim(ERROR_CODE) IN (?) AND PCATEGORY = 'BEE')";

          pst1 = new LoggableStatement(con, sEerrDescQuery);

          pst1.setString(1, sInParam);

          logger.info("--------------fetchBeeData--- ERRMSG---query-----" + pst1.getQueryString());

          rs1 = pst1.executeQuery();

          while (rs1.next())

          {

            alStrDB.add(rs1.getString(1));

            errorDesc = rs1.getString(2);

            if (errorDesc1.isEmpty()) {

              errorDesc1 = errorDesc.trim();

            } else {

              errorDesc1 = errorDesc1 + ",\n" + errorDesc.trim();

            }

          }

          for (String temp : alStr) {

            if (!alStrDB.contains(temp)) {

              if (errorDesc1.isEmpty()) {

                errorDesc1 = temp.trim();

              } else {

                errorDesc1 = errorDesc1 + ",\n" + temp.trim();

              }

            }

          }

          ackVO.setErrorDesc(errorDesc1);

          rs1.close();

          pst1.close();

          errorDesc = "";

          errorDesc1 = "";

          if (errorCode.equals("SUCCESS"))

          {

            ackVO.setStatusField(errorCode);

            successCount++;

          }

          if (!errorCode.equals("SUCCESS"))

          {

            ackVO.setStatusField("FAIL");

            failCount++;

          }

          beeAckList.add(ackVO);

          iCount = beeAckList.size();

        }

        ackVO.setSuccessCount(successCount);

        ackVO.setFailCount(failCount);

        ackVO.setBeeAckValues(beeAckList);

        ackVO.setTotalCount(iCount);

      }

      catch (Exception exception)

      {

        logger.info("--------------fetchBeeData----exception-------" + exception);

        exception.printStackTrace();

        throwDAOException(exception);

      }

      finally

      {

        DBConnectionUtility.surrenderDB(con, pst, rs);

      }

      logger.info("Exiting Method");

      return ackVO;

    }
   
  
   
  public AcknowledgementVO fetchBeaData(AcknowledgementVO ackVO)

      throws DAOException

    {

      logger.info("-------------fetchBeaData----------------");

      int iCount = 0;

      int successCount = 0;

      int failCount = 0;

      String sInParam = "";

      String errorDesc = "";

      String errorDesc1 = "";

      String sEerrDescQuery = "";

      String errorCode = "";

      ArrayList<AcknowledgementVO> beaAckList = null;

      LoggableStatement pst = null;

      ResultSet rs = null;

      Connection con = null;

      LoggableStatement pst1 = null;

      ResultSet rs1 = null;

      CommonMethods commonMethods = null;

      String BEA_QUERY = null;

      String BEA_QUERY1 = null;

      String sAckStatus = null;

      boolean boeNoFlag = false;

      boolean boeDateFlag = false;

      boolean portCodeFlag = false;

      boolean ieCodeFlag = false;

      boolean adCodeFlag = false;

      int setValue = 0;

      try

      {

        con = DBConnectionUtility.getConnection();

        beaAckList = new ArrayList();

   
   
        sAckStatus = ackVO.getAckStatus();

        commonMethods = new CommonMethods();

        BEA_QUERY = "SELECT PORTOFDISCHARGE,BOENUMBER,TO_CHAR(BOEDATE,'DD/MM/YYYY') AS BOEDATE,IECODE,ADCODE,RECORDINDICATOR,TO_CHAR(ADJ_DATE,'DD/MM/YYYY') AS ADJ_DATE,ADJ_IND,ADJ_NO,CLOSE_IND,DOCUMENT_NO,TO_CHAR(DOCUMENT_DATE,'DD/MM/YYYY') AS DOCUMENT_DATE,DOCUMENT_PORT,APPROVEDBY,LETTERNO,TO_CHAR(LETTERDATE,'DD/MM/YYYY') AS LETTERDATE,REMARKS,INV_SNO,INV_NO,ADJ_INVAMT,ERRORCODES FROM ETT_BEA_ACK WHERE BOENUMBER = BOENUMBER ";

   
   
   
        String boeNo = ackVO.getBoeNo();

        if (!commonMethods.isNull(boeNo))

        {

          BEA_QUERY = BEA_QUERY + " AND TRIM(BOENUMBER) = '" + boeNo.trim() + "'";

          boeNoFlag = true;

        }

        String boeDate = commonMethods.getEmptyIfNull(ackVO.getBoeDate());

        if (!commonMethods.isNull(boeDate))

        {

          BEA_QUERY = BEA_QUERY + " AND TO_CHAR(TO_DATE(BOEDATE,'DD-MM-YY'),'DD/MM/YYYY') = '" + boeDate.trim() + "'";

          boeDateFlag = true;

        }

        String portCode = ackVO.getPortCode();

        if (!commonMethods.isNull(portCode))

        {

          BEA_QUERY = BEA_QUERY + " AND TRIM(PORTOFDISCHARGE) ='" + portCode.trim() + "'";

          portCodeFlag = true;

        }

        String ieCode = ackVO.getBesIECode();

        if (!commonMethods.isNull(ieCode))

        {

          BEA_QUERY = BEA_QUERY + " AND TRIM(IECODE) =  '" + ieCode.trim() + "'";

          ieCodeFlag = true;

        }

        String adCode = ackVO.getBesADCode();

        if (!commonMethods.isNull(adCode))

        {

          BEA_QUERY = BEA_QUERY + " AND TRIM(ADCODE) =  '" + adCode.trim() + "'";

          adCodeFlag = true;

        }

        if (sAckStatus.equals("S"))

        {

          BEA_QUERY = BEA_QUERY + " AND ERRORCODES = 'SUCCESS'";

        }

        else if (sAckStatus.equals("F"))

        {

          BEA_QUERY = BEA_QUERY + " AND BOENUMBER NOT IN (SELECT BOENUMBER FROM ETT_BEA_ACK WHERE ERRORCODES = 'SUCCESS')";

        }

        else

        {

          BEA_QUERY1 = BEA_QUERY.toString();

          BEA_QUERY = BEA_QUERY + " AND ERRORCODES = 'SUCCESS' UNION ALL ";

          BEA_QUERY1 = BEA_QUERY1 + " AND BOENUMBER NOT IN (SELECT BOENUMBER FROM ETT_BEA_ACK WHERE ERRORCODES = 'SUCCESS')";

          BEA_QUERY = BEA_QUERY + BEA_QUERY1.toString();

        }

        pst = new LoggableStatement(con, BEA_QUERY);

        logger.info("-------------fetchBeaData----------getBEAAckList query------" + pst.getQueryString());

        rs = pst.executeQuery();

        while (rs.next())

        {

          ackVO = new AcknowledgementVO();

          ackVO.setPortDischarge(rs.getString("PORTOFDISCHARGE"));

          ackVO.setBoeNoVal(rs.getString("BOENUMBER"));

          ackVO.setBoeDateVal(rs.getString("BOEDATE"));

          ackVO.setIeCodeVal(rs.getString("IECODE"));

          ackVO.setAdCodeVal(rs.getString("ADCODE"));

          ackVO.setBesrecordInd(rs.getString("RECORDINDICATOR"));

          ackVO.setAdjDate(rs.getString("ADJ_DATE"));

          ackVO.setAdjInd(rs.getString("ADJ_IND"));

          ackVO.setAdjNo(rs.getString("ADJ_NO"));

          ackVO.setBesbillCloInd(rs.getString("CLOSE_IND"));

          ackVO.setDocNo(rs.getString("DOCUMENT_NO"));

          ackVO.setDocDate(rs.getString("DOCUMENT_DATE"));

          ackVO.setDocPort(rs.getString("DOCUMENT_PORT"));

          ackVO.setExtBy(rs.getString("APPROVEDBY"));

          ackVO.setLetterNo(rs.getString("LETTERNO"));

          ackVO.setLetterDate(rs.getString("LETTERDATE"));

          String remarks = rs.getString("REMARKS");

          if (remarks != null) {

            remarks = remarks.replaceAll("[^A-Za-z0-9 ]", "");

          }

          ackVO.setRemarks(remarks);

          ackVO.setInvSerialNo(rs.getString("INV_SNO"));

          ackVO.setInvNo(rs.getString("INV_NO"));

          ackVO.setAmount(rs.getString("ADJ_INVAMT"));

          if (rs.getString("ERRORCODES") != null) {

            errorCode = rs.getString("ERRORCODES");

          }

          ackVO.setBesErrorCodes(errorCode);

          if (boeNoFlag) {

            ackVO.setBoeNo(boeNo.trim());

          }

          if (boeDateFlag) {

            ackVO.setBoeDate(boeDate.trim());

          }

          if (portCodeFlag) {

            ackVO.setPortCode(portCode.trim());

          }

          if (ieCodeFlag) {

            ackVO.setBesIECode(ieCode.trim());

          }

          if (adCodeFlag) {

            ackVO.setBesADCode(adCode.trim());

          }

          ArrayList<String> alStr = new ArrayList();

          ArrayList<String> alStrDB = new ArrayList();

          String[] splitErrorCodes = { "" };

          if (errorCode.contains(","))

          {

            splitErrorCodes = errorCode.split(",");

            sInParam = "";

            for (int j = 0; j < splitErrorCodes.length; j++)

            {

              alStr.add(splitErrorCodes[j]);

              if (sInParam.equals("")) {

                sInParam = "'" + splitErrorCodes[j] + "'";

              } else {

                sInParam = sInParam + ",'" + splitErrorCodes[j] + "'";

              }

            }

          }

          else

          {

            alStr.add(errorCode);

            sInParam = "'" + errorCode + "'";

          }

          sEerrDescQuery = "SELECT Trim(ERRCO) as ERRCO, ERRCO||'-'||ERRMSG ERRCO FROM (SELECT ERROR_CODE ERRCO, ERROR_MSG ERRMSG FROM ETT_IDPMS_ERROR_CODES WHERE trim(ERROR_CODE) IN (?) AND PCATEGORY = 'BEA')";

          pst1 = new LoggableStatement(con, sEerrDescQuery);

          pst1.setString(1, sInParam);

          logger.info("-------------fetchBeaData-----ERRCO-----sEerrDescQuery------" + pst1.getQueryString());

          rs1 = pst1.executeQuery();

          while (rs1.next())

          {

            alStrDB.add(rs1.getString(1));

            errorDesc = rs1.getString(2);

            if (errorDesc1.isEmpty()) {

              errorDesc1 = errorDesc.trim();

            } else {

              errorDesc1 = errorDesc1 + ",\n" + errorDesc.trim();

            }

          }

          for (String temp : alStr) {

            if (!alStrDB.contains(temp)) {

              if (errorDesc1.isEmpty()) {

                errorDesc1 = temp.trim();

              } else {

                errorDesc1 = errorDesc1 + ",\n" + temp.trim();

              }

            }

          }

          ackVO.setErrorDesc(errorDesc1);

          errorDesc = "";

          errorDesc1 = "";

          rs1.close();

          pst1.close();

          if (errorCode.equals("SUCCESS"))

          {

            ackVO.setStatusField(errorCode);

            successCount++;

          }

          if (!errorCode.equals("SUCCESS"))

          {

            ackVO.setStatusField("FAIL");

            failCount++;

          }

          beaAckList.add(ackVO);

          iCount = beaAckList.size();

        }

        ackVO.setBeaAckValues(beaAckList);

        ackVO.setFailCount(failCount);

        ackVO.setSuccessCount(successCount);

        ackVO.setTotalCount(iCount);

      }

      catch (Exception exception)

      {

        logger.info("-------------fetchBeaData-----exception-----------" + exception);

        exception.printStackTrace();

        throwDAOException(exception);

      }

      finally

      {

        DBConnectionUtility.surrenderDB(con, pst, rs);

      }

      logger.info("Exiting Method");

      return ackVO;

    }
   
  
   
  public AcknowledgementVO fetchMbeData(AcknowledgementVO ackVO)

      throws DAOException

    {

      logger.info("-----------fetchMbeData--------------------");

      int iCount = 0;

      int successCount = 0;

      int failCount = 0;

      String sInParam = "";

      String errorDesc = "";

      String errorDesc1 = "";

      String sEerrDescQuery = "";

      String errorCode = "";

      ArrayList<AcknowledgementVO> mbeAckList = null;

      LoggableStatement pst = null;

      ResultSet rs = null;

      Connection con = null;

      LoggableStatement pst1 = null;

      ResultSet rs1 = null;

      CommonMethods commonMethods = null;

      String MBE_QUERY = null;

      String MBE_QUERY1 = null;

      String sAckStatus = null;

      String query = null;

      boolean boeNoFlag = false;

      boolean boeDateFlag = false;

      boolean ieCodeFlag = false;

      boolean adCodeFlag = false;

      int setValue = 0;

      try

      {

        con = DBConnectionUtility.getConnection();

        mbeAckList = new ArrayList();

   
   
   
        sAckStatus = ackVO.getAckStatus();

        commonMethods = new CommonMethods();

        MBE_QUERY = "SELECT PORTOFDISCHARGE,IMPORTAGENCY,BILLOFENTRYNUMBER,BILLOFENTRYDATE,ADCODE,GP,IECODE,IENAME,IEADDRESS,IEPANNUMBER,PORTOFSHIPMENT,RECORDINDICATOR,IGMNUMBER,IGMDATE,MAWBMBLNUMBER,MAWBMBLDATE,HAWBHBLNUMBER,HAWBHBLDATE,INVOICESERIALNO,INVOICENO,TERMSOFINVOICE,SUPPLIERNAME,SUPPLIERADDRESS,SUPPLIERCOUNTRY,SELLERNAME,SELLERADDRESS,SELLERCOUNTRY,INVOICEAMOUNT,INVOICECURRENCY,FREIGHTAMOUNT,FREIGHTCURRENCYCODE,INSURANCEAMOUNT,INSURANCECURRENCYCODE,AGENCYCOMMISSION,AGENCYCURRENCY,DISCOUNTCHARGES,DISCOUNTCURRENCY,MISCELLANEOUSCHARGES,MISCELLANEOUSCURRENCY,THIRDPARTYNAME,THIRDPARTYADDRESS,THIRDPARTYCOUNTRY,ERRORCODE FROM ETT_MANUAL_BOE_ACK WHERE BILLOFENTRYNUMBER = BILLOFENTRYNUMBER ";

   
   
   
   
        String boeNo = ackVO.getBoeNo();

        if (!commonMethods.isNull(boeNo))

        {

          MBE_QUERY = MBE_QUERY + " AND TRIM(BILLOFENTRYNUMBER) ='" + boeNo.trim() + "' ";

          boeNoFlag = true;

        }

        String boeDate = commonMethods.getEmptyIfNull(ackVO.getBoeDate());

        if (!commonMethods.isNull(boeDate))

        {

          MBE_QUERY = MBE_QUERY + " AND TO_CHAR(TO_DATE(BILLOFENTRYDATE,'DD-MM-YY'),'DD/MM/YYYY') = '" + boeDate.trim() + "' ";

          boeDateFlag = true;

        }

        String ieCode = ackVO.getBesIECode();

        if (!commonMethods.isNull(ieCode))

        {

          MBE_QUERY = MBE_QUERY + " AND TRIM(IECODE) =  '" + ieCode.trim() + "' ";

          ieCodeFlag = true;

        }

        String adCode = ackVO.getBesADCode();

        if (!commonMethods.isNull(adCode))

        {

          MBE_QUERY = MBE_QUERY + " AND TRIM(ADCODE) =  '" + adCode.trim() + "' ";

          adCodeFlag = true;

        }

        if (sAckStatus.equalsIgnoreCase("S"))

        {

          MBE_QUERY = MBE_QUERY + " AND ERRORCODE = 'SUCCESS'";

        }

        else if (sAckStatus.equalsIgnoreCase("F"))

        {

          MBE_QUERY = MBE_QUERY + " AND BILLOFENTRYNUMBER NOT IN (SELECT BILLOFENTRYNUMBER FROM ETT_MANUAL_BOE_ACK WHERE ERRORCODE = 'SUCCESS')";

        }

        else

        {

          MBE_QUERY1 = MBE_QUERY.toString();

          MBE_QUERY = MBE_QUERY + " AND ERRORCODE = 'SUCCESS' UNION ALL ";

          MBE_QUERY1 = MBE_QUERY1 + " AND BILLOFENTRYNUMBER NOT IN (SELECT BILLOFENTRYNUMBER FROM ETT_MANUAL_BOE_ACK WHERE ERRORCODE = 'SUCCESS')";

          MBE_QUERY = MBE_QUERY + MBE_QUERY1.toString();

        }

        pst = new LoggableStatement(con, MBE_QUERY);

        logger.info("-------fetchMbeData---exception------query-------" + pst.getQueryString());

        rs = pst.executeQuery();

        while (rs.next())

        {

          ackVO = new AcknowledgementVO();

          ackVO.setPortVal(rs.getString("PORTOFDISCHARGE"));

          ackVO.setImAgencyVal(rs.getString("IMPORTAGENCY"));

          ackVO.setBoeNoVal(rs.getString("BILLOFENTRYNUMBER"));

          ackVO.setBoeDateVal(rs.getString("BILLOFENTRYDATE"));

          ackVO.setAdCodeVal(rs.getString("ADCODE"));

          ackVO.setGpCodeVal(rs.getString("GP"));

          ackVO.setIeCodeVal(rs.getString("IECODE"));

          ackVO.setIeNameVal(rs.getString("IENAME"));

          ackVO.setIeAddressVal(rs.getString("IEADDRESS"));

          ackVO.setIePanVal(rs.getString("IEPANNUMBER"));

          ackVO.setPosVal(rs.getString("PORTOFSHIPMENT"));

          ackVO.setRecordIndVal(rs.getString("RECORDINDICATOR"));

          ackVO.setIgmNoVal(rs.getString("IGMNUMBER"));

          ackVO.setIgmDateVal(rs.getString("IGMDATE"));

          ackVO.setMblNoVal(rs.getString("MAWBMBLNUMBER"));

          ackVO.setMblDateVal(rs.getString("MAWBMBLDATE"));

          ackVO.setHblNoVal(rs.getString("HAWBHBLNUMBER"));

          ackVO.setHblDateVal(rs.getString("HAWBHBLDATE"));

          ackVO.setInvSnoVal(rs.getString("INVOICESERIALNO"));

          ackVO.setInvNoVal(rs.getString("INVOICENO"));

          ackVO.setTermsofInvoiceVal(rs.getString("TERMSOFINVOICE"));

          ackVO.setInvAmtVal(rs.getString("INVOICEAMOUNT"));

          ackVO.setInvAmtCurrVal(rs.getString("INVOICECURRENCY"));

          ackVO.setInvFrAmtVal(rs.getString("FREIGHTAMOUNT"));

          ackVO.setInvFrAmtCurrVal(rs.getString("FREIGHTCURRENCYCODE"));

          ackVO.setInvInsAmtVal(rs.getString("INSURANCEAMOUNT"));

          ackVO.setInvInsAmtCurrVal(rs.getString("INSURANCECURRENCYCODE"));

          ackVO.setInvAgAmtVal(rs.getString("AGENCYCOMMISSION"));

          ackVO.setInvAgAmtCurrVal(rs.getString("AGENCYCURRENCY"));

          ackVO.setInvDisAmtVal(rs.getString("DISCOUNTCHARGES"));

          ackVO.setInvDisAmtCurrVal(rs.getString("DISCOUNTCURRENCY"));

          ackVO.setInvMisAmtVal(rs.getString("MISCELLANEOUSCHARGES"));

          ackVO.setInvMisAmtCurrVal(rs.getString("MISCELLANEOUSCURRENCY"));

          ackVO.setSupNameVal(rs.getString("SUPPLIERNAME"));

          ackVO.setSupAddrVal(rs.getString("SUPPLIERADDRESS"));

          ackVO.setSupCounVal(rs.getString("SUPPLIERCOUNTRY"));

          ackVO.setSellNameVal(rs.getString("SELLERNAME"));

          ackVO.setSellAddrVal(rs.getString("SELLERADDRESS"));

          ackVO.setSellCounVal(rs.getString("SELLERCOUNTRY"));

          ackVO.setThirdPartyNameVal(rs.getString("THIRDPARTYNAME"));

          ackVO.setThirdPartyAddrVal(rs.getString("THIRDPARTYADDRESS"));

          ackVO.setThirdPartyCounVal(rs.getString("THIRDPARTYCOUNTRY"));

          if (rs.getString("ERRORCODE") != null) {

            errorCode = rs.getString("ERRORCODE");

          }

          if (boeNoFlag) {

            ackVO.setBoeNo(boeNo.trim());

          }

          if (boeDateFlag) {

            ackVO.setBoeDate(boeDate.trim());

          }

          if (ieCodeFlag) {

            ackVO.setBesIECode(ieCode.trim());

          }

          if (adCodeFlag) {

            ackVO.setBesADCode(adCode.trim());

          }

          ackVO.setErrorCodesVal(rs.getString("ERRORCODE"));

   
   
          ArrayList<String> alStr = new ArrayList();

          ArrayList<String> alStrDB = new ArrayList();

          String[] splitErrorCodes = { "" };

          if (errorCode.contains(","))

          {

            splitErrorCodes = errorCode.split(",");

            sInParam = "";

            for (int j = 0; j < splitErrorCodes.length; j++)

            {

              alStr.add(splitErrorCodes[j]);

              if (sInParam.equals("")) {

                sInParam = "'" + splitErrorCodes[j] + "'";

              } else {

                sInParam = sInParam + ",'" + splitErrorCodes[j] + "'";

              }

            }

          }

          else

          {

            alStr.add(errorCode);

            sInParam = "'" + errorCode + "'";

          }

          sEerrDescQuery = "SELECT Trim(ERRCO) as ERRCO, ERRCO||'-'||ERRMSG ERRCO FROM (SELECT ERROR_CODE ERRCO, ERROR_MSG ERRMSG FROM ETT_IDPMS_ERROR_CODES WHERE trim(ERROR_CODE) IN (?) AND PCATEGORY = 'MBE')";

          pst1 = new LoggableStatement(con, sEerrDescQuery);

          pst1.setString(1, sInParam);

          logger.info("-------fetchMbeData---exception------ERRCO-------" + pst1.getQueryString());

          rs1 = pst1.executeQuery();

          while (rs1.next())

          {

            alStrDB.add(rs1.getString(1));

            errorDesc = rs1.getString(2);

            if (errorDesc1.isEmpty()) {

              errorDesc1 = errorDesc.trim();

            } else {

              errorDesc1 = errorDesc1 + ",\n" + errorDesc.trim();

            }

          }

          for (String temp : alStr) {

            if (!alStrDB.contains(temp)) {

              if (errorDesc1.isEmpty()) {

                errorDesc1 = temp.trim();

              } else {

                errorDesc1 = errorDesc1 + ",\n" + temp.trim();

              }

            }

          }

          ackVO.setErrorDesc(errorDesc1);

          errorDesc = "";

          errorDesc1 = "";

          rs1.close();

          pst1.close();

          if (errorCode.equalsIgnoreCase("SUCCESS"))

          {

            ackVO.setStatusField(errorCode);

            successCount++;

          }

          if (!errorCode.equalsIgnoreCase("SUCCESS"))

          {

            ackVO.setStatusField("FAIL");

            failCount++;

          }

          mbeAckList.add(ackVO);

          iCount = mbeAckList.size();

        }

        ackVO.setSuccessCount(successCount);

        ackVO.setFailCount(failCount);

        ackVO.setMbeAckValues(mbeAckList);

        ackVO.setTotalCount(iCount);

      }

      catch (Exception exception)

      {

        logger.info("-------fetchMbeData---exception-------------" + exception);

        exception.printStackTrace();

        throwDAOException(exception);

      }

      finally

      {

        DBConnectionUtility.surrenderDB(con, pst, rs);

      }

      logger.info("Exiting Method");

      return ackVO;

    }
   
  
   
  public AcknowledgementVO fetchOraData(AcknowledgementVO ackVO)

      throws DAOException

    {

      logger.info("-----------------fetchOraData-----------------");

      int iCount = 0;

      int successCount = 0;

      int failCount = 0;

      String sInParam = "";

      String errorDesc = "";

      String errorDesc1 = "";

      String sEerrDescQuery = "";

      String errorCode = "";

      ArrayList<AcknowledgementVO> oraAckList = null;

      LoggableStatement pst = null;

      ResultSet rs = null;

      Connection con = null;

      LoggableStatement pst1 = null;

      ResultSet rs1 = null;

      String query = null;

      CommonMethods commonMethods = null;

      String ORA_QUERY = null;

      String ORA_QUERY1 = null;

      String sAckStatus = null;

      boolean ormNoFlag = false;

      boolean ieCodeFlag = false;

      boolean adCodeFlag = false;

      int setValue = 0;

      try

      {

        con = DBConnectionUtility.getConnection();

   
        oraAckList = new ArrayList();

   
   
   
        sAckStatus = ackVO.getAckStatus();

        commonMethods = new CommonMethods();

        ORA_QUERY = "SELECT OUTWARDREFERENCENUMBER , ADCODE, CURRENCYCODE, CLOSAMT, TO_CHAR(TO_DATE(CLOSDAT,'DD/MM/YYYY'),'DD/MM/YYYY') AS CLDAT, IECODE, SWIFTMESSAGE, ADJCLOINDI, APPROVEDBY, LETTERNO, TO_CHAR(LETTERDAT,'DD/MM/YYYY') LETDAT, DOCNUM, TO_CHAR(DOCDAT,'DD/MM/YYYY') DOCDAT, PORTCODE, RECORDINDICATOR, REMARKS, ERRORCODES FROM ETT_ORA_ACK WHERE OUTWARDREFERENCENUMBER = OUTWARDREFERENCENUMBER ";

   
   
        String ormNo = ackVO.getOrmNo();

        logger.info("-----------------ormNo-----------------" + ormNo.trim());

        if (!commonMethods.isNull(ormNo))

        {

          ORA_QUERY = ORA_QUERY + " AND TRIM(OUTWARDREFERENCENUMBER) ='" + ormNo.trim() + "'";

          ormNoFlag = true;

        }

        String ieCode = ackVO.getIecode();

        logger.info("-----------------ieCode-----------------" + ieCode.trim());

        if (!commonMethods.isNull(ieCode))

        {

          ORA_QUERY = ORA_QUERY + " AND TRIM(IECODE) ='" + ieCode.trim() + "'";

          ieCodeFlag = true;

        }

        String adCode = ackVO.getAdcode();

        logger.info("-----------------adCode-----------------" + adCode.trim());

        if (!commonMethods.isNull(adCode))

        {

          ORA_QUERY = ORA_QUERY + " AND TRIM(ADCODE) ='" + adCode.trim() + "'";

          adCodeFlag = true;

        }

        if (sAckStatus.equals("S"))

        {

          ORA_QUERY = ORA_QUERY + " AND ERRORCODES = 'SUCCESS'";

        }

        else if (sAckStatus.equals("F"))

        {

          ORA_QUERY = ORA_QUERY + " AND OUTWARDREFERENCENUMBER NOT IN (SELECT OUTWARDREFERENCENUMBER FROM ETT_ORA_ACK WHERE ERRORCODES = 'SUCCESS')";

        }

        else

        {

          ORA_QUERY1 = ORA_QUERY.toString();

          ORA_QUERY = ORA_QUERY + " AND ERRORCODES = 'SUCCESS' UNION ALL ";

          ORA_QUERY1 = ORA_QUERY1 + " AND OUTWARDREFERENCENUMBER NOT IN (SELECT OUTWARDREFERENCENUMBER FROM ETT_ORA_ACK WHERE ERRORCODES = 'SUCCESS')";

          ORA_QUERY = ORA_QUERY + ORA_QUERY1.toString();

        }

        pst = new LoggableStatement(con, ORA_QUERY);

        logger.info("-----------------fetchOraData-----query------------" + pst.getQueryString());

        rs = pst.executeQuery();

        while (rs.next())

        {

          ackVO = new AcknowledgementVO();

          ackVO.setAckOrmNo(rs.getString("OUTWARDREFERENCENUMBER"));

          ackVO.setOrmAdCode(rs.getString("ADCODE"));

          ackVO.setOrmCurr(rs.getString("CURRENCYCODE"));

          ackVO.setOrmAmt(rs.getString("CLOSAMT"));

          ackVO.setOrmDate(rs.getString("CLDAT"));

          ackVO.setOrmIeCode(rs.getString("IECODE"));

          ackVO.setOrmSwift(rs.getString("SWIFTMESSAGE"));

          ackVO.setOrmIndicator(rs.getString("ADJCLOINDI"));

          ackVO.setOrmApprovedBy(rs.getString("APPROVEDBY"));

          ackVO.setOrmLno(rs.getString("LETTERNO"));

          ackVO.setOrmLdate(rs.getString("LETDAT"));

          ackVO.setOrmDocNo(rs.getString("DOCNUM"));

          ackVO.setOrmDocDate(rs.getString("DOCDAT"));

          ackVO.setOrmPod(rs.getString("PORTCODE"));

          ackVO.setOrmRec(rs.getString("RECORDINDICATOR"));

          String remarks = rs.getString("REMARKS");

          if (remarks != null) {

            remarks = remarks.replaceAll("[^A-Za-z0-9 ]", "");

          }

          if (ormNoFlag) {

            ackVO.setOrmNo(ormNo.trim());

          }

          if (ieCodeFlag) {

            ackVO.setIecode(ieCode.trim());

          }

          if (adCodeFlag) {

            ackVO.setAdcode(adCode.trim());

          }

          ackVO.setOrmRemarks(remarks);

          if (rs.getString("ERRORCODES") != null) {

            errorCode = rs.getString("ERRORCODES");

          }

          ArrayList<String> alStr = new ArrayList();

          ArrayList<String> alStrDB = new ArrayList();

          String[] splitErrorCodes = { "" };

          if (errorCode.contains(","))

          {

            splitErrorCodes = errorCode.split(",");

            sInParam = "";

            for (int j = 0; j < splitErrorCodes.length; j++)

            {

              alStr.add(splitErrorCodes[j]);

              if (sInParam.equals("")) {

                sInParam = "'" + splitErrorCodes[j] + "'";

              } else {

                sInParam = sInParam + ",'" + splitErrorCodes[j] + "'";

              }

            }

          }

          else

          {

            alStr.add(errorCode);

            sInParam = "'" + errorCode + "'";

          }

          sEerrDescQuery = "SELECT Trim(ERRCO) as ERRCO, ERRCO||'-'||ERRMSG ERRCO FROM (SELECT ERROR_CODE ERRCO, ERROR_MSG ERRMSG FROM ETT_IDPMS_ERROR_CODES WHERE trim(ERROR_CODE) IN (" + sInParam + ") AND PCATEGORY = 'ORA')";

          pst1 = new LoggableStatement(con, sEerrDescQuery);

          logger.info("-----------------fetchOraData------sEerrDescQuery-----------" + pst1.getQueryString());

          rs1 = pst1.executeQuery();

          while (rs1.next())

          {

            alStrDB.add(rs1.getString(1));

            errorDesc = rs1.getString(2);

            if (errorDesc1.isEmpty()) {

              errorDesc1 = errorDesc.trim();

            } else {

              errorDesc1 = errorDesc1 + ",\n" + errorDesc.trim();

            }

          }

          for (String temp : alStr) {

            if (!alStrDB.contains(temp)) {

              if (errorDesc1.isEmpty()) {

                errorDesc1 = temp.trim();

              } else {

                errorDesc1 = errorDesc1 + ",\n" + temp.trim();

              }

            }

          }

          ackVO.setErrorDesc(errorDesc1);

          errorDesc = "";

          errorDesc1 = "";

          pst1.close();

          rs1.close();

          if (errorCode.equals("SUCCESS"))

          {

            ackVO.setStatusField(errorCode);

            successCount++;

          }

          if (!errorCode.equals("SUCCESS"))

          {

            ackVO.setStatusField("FAIL");

            failCount++;

          }

          oraAckList.add(ackVO);

          iCount = oraAckList.size();

        }

        ackVO.setSuccessCount(successCount);

        ackVO.setFailCount(failCount);

        ackVO.setOraAckValues(oraAckList);

        ackVO.setTotalCount(iCount);

      }

      catch (Exception exception)

      {

        logger.info("-----------------fetchOraData------exception-----------" + exception);

        exception.printStackTrace();

        throwDAOException(exception);

      }

      finally

      {

        DBConnectionUtility.surrenderDB(con, pst, rs);

      }

      logger.info("Exiting Method");

      return ackVO;

    }
   
  
   
  public AcknowledgementVO fetchObbData(AcknowledgementVO ackVO)

      throws DAOException

    {

      logger.info("Entering Method");

      int recCount = 0;

      int successCount = 0;

      int uploadCount = 0;

      int iCount = 0;

      String sInParam = "";

      String errorDesc = "";

      String errorDesc1 = "";

      String sEerrDescQuery = "";

      String boeStatus = "";

      ArrayList<AcknowledgementVO> obbAckList = null;

      LoggableStatement pst = null;

      LoggableStatement pst1 = null;

      ResultSet rs = null;

      ResultSet rs1 = null;

      Connection con = null;

      String query = null;

      CommonMethods commonMethods = null;

      String obbBoeNo = "";

      String boeDate = "";

      String adCodde = "";

      String ieCodde = "";

      String recDate = "";

      String portCode = "";

      String sAckStatus = null;

      String OBB_QUERY = null;

      String OBB_QUERY1 = null;

      boolean obbBoeNoFlag = false;

      boolean boeDateFlag = false;

      boolean portCodeFlag = false;

      boolean recDateFlag = false;

      boolean adCoddeFlag = false;

      boolean ieCoddeFlag = false;

      int setValue = 0;

      try

      {

        con = DBConnectionUtility.getConnection();

        obbAckList = new ArrayList();

   
   
   
        commonMethods = new CommonMethods();

        sAckStatus = ackVO.getAckStatus();

   
        OBB_QUERY = "SELECT BOENUM, TO_CHAR(BOEDATE,'DD/MM/YYYY') BOEDATE, PORTCODE, ADCODE, IE_CODE, IENAME, BOE_IMP_AGENCY, DECODE(STATUS, 'R', 'RECEIVED', 'U', 'UPLOADED', 'S', 'SUCCESS','') STATUS, TO_CHAR(TO_DATE(REC_DATE,'DD/MM/rrrr'), 'DD/MM/YYYY') REC_DATE FROM ETT_BOE_OBB_DETAILS WHERE BOENUM = BOENUM ";

   
   
        obbBoeNo = ackVO.getBoeNo();

        if (!commonMethods.isNull(obbBoeNo))

        {

          OBB_QUERY = OBB_QUERY + " AND BOENUM =?";

          obbBoeNoFlag = true;

        }

        boeDate = commonMethods.getEmptyIfNull(ackVO.getBoeDate());

        if (!commonMethods.isNull(boeDate))

        {

          OBB_QUERY = OBB_QUERY + " AND TO_CHAR(TO_DATE(BOEDATE,'DD-MM-YY'),'DD/MM/YYYY') = ?";

          boeDateFlag = true;

        }

        portCode = ackVO.getPortCode();

        if (!commonMethods.isNull(portCode))

        {

          OBB_QUERY = OBB_QUERY + " AND TRIM(PORTCODE) =?";

          portCodeFlag = true;

        }

        recDate = ackVO.getRecDate();

        if (!commonMethods.isNull(recDate))

        {

          OBB_QUERY = OBB_QUERY + " AND REC_DATE =  TO_CHAR(TO_DATE(?,'DD/MM/YYYY'),'DD/MM/YYYY') ";

          recDateFlag = true;

        }

        adCodde = ackVO.getAdCodde();

        if (!commonMethods.isNull(adCodde))

        {

          OBB_QUERY = OBB_QUERY + " AND ADCODE =  ?";

          adCoddeFlag = true;

        }

        ieCodde = ackVO.getIeCodde();

        if (!commonMethods.isNull(ieCodde))

        {

          OBB_QUERY = OBB_QUERY + " AND IECODE = ?";

          ieCoddeFlag = true;

        }

        if (sAckStatus.equals("R")) {

          OBB_QUERY = OBB_QUERY + " AND STATUS = 'R'";

        }

        if (sAckStatus.equals("U")) {

          OBB_QUERY = OBB_QUERY + " AND STATUS = 'U'";

        }

        if (sAckStatus.equals("S")) {

          OBB_QUERY = OBB_QUERY + " AND STATUS = 'S'";

        }

        pst = new LoggableStatement(con, query);

        if (obbBoeNoFlag) {

          pst.setString(++setValue, obbBoeNo.trim());

        }

        if (boeDateFlag) {

          pst.setString(++setValue, boeDate.trim());

        }

        if (portCodeFlag) {

          pst.setString(++setValue, portCode.trim());

        }

        if (recDateFlag) {

          pst.setString(++setValue, recDate.trim());

        }

        if (adCoddeFlag) {

          pst.setString(++setValue, adCodde.trim());

        }

        if (ieCoddeFlag) {

          pst.setString(++setValue, ieCodde.trim());

        }

        rs = pst.executeQuery();

        while (rs.next())

        {

          ackVO = new AcknowledgementVO();

          ackVO.setPortVal(rs.getString("PORTCODE"));

          ackVO.setImAgencyVal(rs.getString("BOE_IMP_AGENCY"));

          ackVO.setBoeNoVal(rs.getString("BOENUM"));

          ackVO.setBoeDateVal(rs.getString("BOEDATE"));

          ackVO.setAdCodeVal(rs.getString("ADCODE"));

          ackVO.setIeCodeVal(rs.getString("IE_CODE"));

          ackVO.setIeNameVal(rs.getString("IENAME"));

          boeStatus = rs.getString("STATUS");

          ackVO.setRecDate(rs.getString("REC_DATE"));

          if (boeStatus.equalsIgnoreCase("RECEIVED"))

          {

            ackVO.setStatusField(boeStatus);

            recCount++;

          }

          if (boeStatus.equalsIgnoreCase("UPLOADED"))

          {

            ackVO.setStatusField(boeStatus);

            uploadCount++;

          }

          if (boeStatus.equalsIgnoreCase("SUCCESS"))

          {

            ackVO.setStatusField(boeStatus);

            successCount++;

          }

          obbAckList.add(ackVO);

          iCount = obbAckList.size();

        }

        ackVO.setSuccessCount(successCount);

        ackVO.setRecCount(recCount);

        ackVO.setUploadCount(uploadCount);

        ackVO.setObbAckValues(obbAckList);

        ackVO.setTotalCount(iCount);

      }

      catch (Exception exception)

      {

        exception.printStackTrace();

        throwDAOException(exception);

      }

      finally

      {

        DBConnectionUtility.surrenderDB(con, pst, rs);

      }

      logger.info("Exiting Method");

      return ackVO;

    }
   
  
   
  public AcknowledgementVO fetchBttData(AcknowledgementVO ackVO)

      throws DAOException

    {

      logger.info("-------------fetchBeaData----------------");

      int iCount = 0;

      int successCount = 0;

      int failCount = 0;

      String sInParam = "";

      String errorDesc = "";

      String errorDesc1 = "";

      String sEerrDescQuery = "";

      String errorCode = "";

      ArrayList<AcknowledgementVO> bttAckList = null;

      LoggableStatement pst = null;

      ResultSet rs = null;

      Connection con = null;

      LoggableStatement pst1 = null;

      ResultSet rs1 = null;

      CommonMethods commonMethods = null;

      String BTT_QUERY = null;

      String BTT_QUERY1 = null;

      String sAckStatus = null;

      boolean boeNoFlag = false;

      boolean boeDateFlag = false;

      boolean portCodeFlag = false;

      boolean ieCodeFlag = false;

      boolean adCodeFlag = false;

      int setValue = 0;

      try

      {

        con = DBConnectionUtility.getConnection();

        bttAckList = new ArrayList();

   
   
        sAckStatus = ackVO.getAckStatus();

        commonMethods = new CommonMethods();

        BTT_QUERY = "SELECT PORTCODE,BOENO,TO_CHAR(BOEDATE,'DD/MM/YYYY') AS BOEDATE,IECODE,ADCODE,RECORDINDICATOR,\tIGMNUMBER,TO_CHAR(IGMDATE,'DD/MM/YYYY') AS IGMDATE,MBLNUMBER,TO_CHAR(MBLDATE,'DD/MM/YYYY') AS MBLDATE,  INSNO,INNO,TOTALINVOICEAMTIC,REALIZEDINVOICEAMTIC,UNREALIZEDINVOICEAMTIC\tFROM ETT_BTT_ACK WHERE BOENO = BOENO ";

   
   
        String boeNo = ackVO.getBoeNo();

        if (!commonMethods.isNull(boeNo))

        {

          BTT_QUERY = BTT_QUERY + " AND TRIM(BOENO) =?";

          boeNoFlag = true;

        }

        String boeDate = commonMethods.getEmptyIfNull(ackVO.getBoeDate());

        if (!commonMethods.isNull(boeDate))

        {

          BTT_QUERY = BTT_QUERY + " AND TO_CHAR(TO_DATE(BOEDATE,'DD-MM-YY'),'DD/MM/YYYY') = ?";

          boeDateFlag = true;

        }

        String portCode = ackVO.getPortCode();

        if (!commonMethods.isNull(portCode))

        {

          BTT_QUERY = BTT_QUERY + " AND TRIM(PORTCODE) =?";

          portCodeFlag = true;

        }

        String ieCode = ackVO.getBesIECode();

        if (!commonMethods.isNull(ieCode))

        {

          BTT_QUERY = BTT_QUERY + " AND TRIM(IECODE) =  ?";

          ieCodeFlag = true;

        }

        String adCode = ackVO.getBesADCode();

        if (!commonMethods.isNull(adCode))

        {

          BTT_QUERY = BTT_QUERY + " AND TRIM(ADCODE) =  ?";

          adCodeFlag = true;

        }

        pst = new LoggableStatement(con, BTT_QUERY);

        if (boeNoFlag) {

          pst.setString(++setValue, boeNo.trim());

        }

        if (boeDateFlag) {

          pst.setString(++setValue, boeDate.trim());

        }

        if (portCodeFlag) {

          pst.setString(++setValue, portCode.trim());

        }

        if (ieCodeFlag) {

          pst.setString(++setValue, ieCode.trim());

        }

        if (adCodeFlag) {

          pst.setString(++setValue, adCode.trim());

        }

        logger.info("-------------fetchBttData----------getBEAAckList query------" + pst.getQueryString());

        rs = pst.executeQuery();

        while (rs.next())

        {

          ackVO = new AcknowledgementVO();

          ackVO.setPortDischarge(rs.getString("PORTCODE"));

          ackVO.setBoeNo(rs.getString("BOENO"));

          ackVO.setBoeDate(rs.getString("BOEDATE"));

          ackVO.setBesIECode(rs.getString("IECODE"));

          ackVO.setBesADCode(rs.getString("ADCODE"));

          ackVO.setBesrecordInd(rs.getString("RECORDINDICATOR"));

          ackVO.setIgmNumber(rs.getString("IGMNUMBER"));

          ackVO.setIgmDate(rs.getString("IGMDATE"));

          ackVO.setMblNumber(rs.getString("MBLNUMBER"));

          ackVO.setMblDate(rs.getString("MBLDATE"));

          ackVO.setInvSerialNo(rs.getString("INSNO"));

          ackVO.setInvNo(rs.getString("INNO"));

          ackVO.setTotalInvoiceAmtIc(rs.getString("TOTALINVOICEAMTIC"));

          ackVO.setRealizedInvoiceAmtIc(rs.getString("REALIZEDINVOICEAMTIC"));

          ackVO.setUnrealizedInvoiceAmtIc(rs.getString("UNREALIZEDINVOICEAMTIC"));

   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
   
          bttAckList.add(ackVO);

          iCount = bttAckList.size();

        }

        ackVO.setBttAckValues(bttAckList);

   
        ackVO.setTotalCount(iCount);

      }

      catch (Exception exception)

      {

        logger.info("-------------fetchBttData-----exception-----------" + exception);

        exception.printStackTrace();

        throwDAOException(exception);

      }

      finally

      {

        DBConnectionUtility.surrenderDB(con, pst, rs);

      }

      logger.info("Exiting Method");

      return ackVO;

    }
   
  
   
  public AcknowledgementVO fetchBesDataForMaker(AcknowledgementVO ackVO)

      throws DAOException

    {

      logger.info("Entering Method");

      int successCount = 0;

      int failCount = 0;

      int iCount = 0;

      String sInParam = "";

      String errorDesc = "";

      String errorDesc1 = "";

      String sEerrDescQuery = "";

      String errorCode = "";

      ArrayList<AcknowledgementVO> besAckList = null;

      LoggableStatement pst = null;

      ResultSet rs = null;

      Connection con = null;

      String query = null;

      LoggableStatement pst1 = null;

      ResultSet rs1 = null;

      CommonMethods commonMethods = null;

      String BES_QUERY = null;

      String BES_QUERY1 = null;

      String sAckStatus = "";

      boolean outRefNoFlag = false;

      boolean boeNoFlag = false;

      boolean boeDateFlag = false;

      boolean portcodeFlag = false;

      boolean payrefnoFlag = false;

      int setValue = 0;

      try

      {

        con = DBConnectionUtility.getConnection();

   
        besAckList = new ArrayList();

   
   
        sAckStatus = ackVO.getAckStatus();

        commonMethods = new CommonMethods();

        BES_QUERY = "SELECT eba.PORTOFDISCHARGE,eba.BOENUMBER,TO_CHAR(TO_DATE(eba.BOEDATE,'DD-MM-YY'),'DD/MM/YYYY') AS BOEDATE,eba.IECODE,eba.CHANGEIECODE,eba.ADCODE,eba.RECORDINDICATOR,eba.PAYMENTPARTY,eba.PAYMENTREFNUMBER,eba.ORNUMBER,eba.ORADCODE,eba.REMITTCUR,eba.BILLCLOSUREINDI,eba.INVOICESERIALNO,eba.INVOICENO,eba.INVOICEAMTIC,eba.INVOICEAMT,eba.ERRORCODES FROM ETT_BES_ACK eba WHERE eba.BOENUMBER not in (select ebac.BOENUMBER from ETT_BES_ACK_CANCEL ebac where  ebac.BOENUMBER = eba.BOENUMBER and ebac.BOEDATE=eba.BOEDATE and ebac.PORTOFDISCHARGE=eba.PORTOFDISCHARGE and ebac.PAYMENTREFNUMBER=eba.PAYMENTREFNUMBER) ";

   
   
   
        String outRefNo = ackVO.getOrmNo();

        if (!commonMethods.isNull(outRefNo))

        {

          BES_QUERY = BES_QUERY + " AND TRIM(ORNUMBER) ='" + outRefNo.trim() + "'";

          outRefNoFlag = true;

        }

        String boeNo = ackVO.getBoeNoEntered();

        if (!commonMethods.isNull(boeNo))

        {

          BES_QUERY = BES_QUERY + " AND TRIM(BOENUMBER) ='" + boeNo.trim() + "'";

          boeNoFlag = true;

        }

        String boeDate = commonMethods.getEmptyIfNull(ackVO.getBoeDateEntered());

        if (!commonMethods.isNull(boeDate))

        {

          BES_QUERY = BES_QUERY + " AND TO_CHAR(TO_DATE(BOEDATE,'DD-MM-YY'),'DD/MM/YYYY') ='" + boeDate.trim() + "'";

          boeDateFlag = true;

        }

        String portcode = commonMethods.getEmptyIfNull(ackVO.getBoePortEntered());

        if (!commonMethods.isNull(portcode))

        {

          BES_QUERY = BES_QUERY + " AND TRIM(PORTOFDISCHARGE) = '" + portcode.trim() + "'";

          portcodeFlag = true;

        }

        String payrefno = ackVO.getBoePayRefEntered();

        if (!commonMethods.isNull(payrefno))

        {

          BES_QUERY = BES_QUERY + " AND TRIM(PAYMENTREFNUMBER) = '" + payrefno.trim() + "'";

          payrefnoFlag = true;

        }

        if (sAckStatus.equals("S"))

        {

          BES_QUERY = BES_QUERY + " AND ERRORCODES = 'SUCCESS'";

        }

        else if (sAckStatus.equals("F"))

        {

          BES_QUERY = BES_QUERY + " AND PAYMENTREFNUMBER NOT IN (SELECT PAYMENTREFNUMBER FROM ETT_BES_ACK WHERE ERRORCODES = 'SUCCESS')";

        }

        else

        {

          BES_QUERY1 = BES_QUERY.toString();

          BES_QUERY = BES_QUERY + " AND ERRORCODES = 'SUCCESS' UNION ALL ";

          BES_QUERY1 = BES_QUERY1 + " AND PAYMENTREFNUMBER NOT IN (SELECT PAYMENTREFNUMBER FROM ETT_BES_ACK WHERE ERRORCODES = 'SUCCESS')";

          BES_QUERY = BES_QUERY + BES_QUERY1.toString();

        }

        pst = new LoggableStatement(con, BES_QUERY);

        rs = pst.executeQuery();

        while (rs.next())

        {

          ackVO = new AcknowledgementVO();

          ackVO.setPortDischarge(rs.getString("PORTOFDISCHARGE"));

          ackVO.setBoeNo(rs.getString("BOENUMBER"));

          ackVO.setBoeDate(rs.getString("BOEDATE"));

          ackVO.setBesIECode(rs.getString("IECODE"));

          ackVO.setChanIECode(rs.getString("CHANGEIECODE"));

          ackVO.setBesADCode(rs.getString("ADCODE"));

          ackVO.setBesrecordInd(rs.getString("RECORDINDICATOR"));

          ackVO.setPaymentParty(rs.getString("PAYMENTPARTY"));

          ackVO.setPaymentRefNo(rs.getString("PAYMENTREFNUMBER"));

          ackVO.setOrNo(rs.getString("ORNUMBER"));

          ackVO.setOrADCode(rs.getString("ORADCODE"));

          ackVO.setRemitCurr(rs.getString("REMITTCUR"));

          ackVO.setBesbillCloInd(rs.getString("BILLCLOSUREINDI"));

          ackVO.setInvoiceSNo(rs.getString("INVOICESERIALNO"));

          ackVO.setInvoiceNo(rs.getString("INVOICENO"));

          ackVO.setInvoiceAmt(rs.getString("INVOICEAMT"));

          ackVO.setInvoiceAmtIC(rs.getString("INVOICEAMTIC"));

          if (outRefNoFlag) {

            ackVO.setOrmNo(outRefNo);

          }

          if (boeNoFlag) {

            ackVO.setBoeNoEntered(boeNo);

          }

          if (boeDateFlag) {

            ackVO.setBoeDateEntered(boeDate);

          }

          if (portcodeFlag) {

            ackVO.setBoePortEntered(portcode);

          }

          if (payrefnoFlag) {

            ackVO.setBoePayRefEntered(payrefno);

          }

          if (rs.getString("ERRORCODES") != null) {

            errorCode = rs.getString("ERRORCODES");

          }

          ackVO.setBesErrorCodes(errorCode);

   
   
          ArrayList<String> alStr = new ArrayList();

          ArrayList<String> alStrDB = new ArrayList();

          String[] splitErrorCodes = { "" };

          if (errorCode.contains(","))

          {

            splitErrorCodes = errorCode.split(",");

            sInParam = "";

            for (int j = 0; j < splitErrorCodes.length; j++)

            {

              alStr.add(splitErrorCodes[j]);

              if (sInParam.equals("")) {

                sInParam = "'" + splitErrorCodes[j] + "'";

              } else {

                sInParam = sInParam + ",'" + splitErrorCodes[j] + "'";

              }

            }

          }

          else

          {

            alStr.add(errorCode);

            sInParam = "'" + errorCode + "'";

          }

          sEerrDescQuery = "SELECT Trim(ERRCO) as ERRCO, ERRCO||'-'||ERRMSG ERRCO FROM (SELECT ERROR_CODE ERRCO, ERROR_MSG ERRMSG FROM ETT_IDPMS_ERROR_CODES WHERE trim(ERROR_CODE) IN (?) AND PCATEGORY = 'BES')";

          pst1 = new LoggableStatement(con, sEerrDescQuery);

          pst1.setString(1, sInParam);

          rs1 = pst1.executeQuery();

          while (rs1.next())

          {

            alStrDB.add(rs1.getString(1));

            errorDesc = rs1.getString(2);

            if (errorDesc1.isEmpty()) {

              errorDesc1 = errorDesc.trim();

            } else {

              errorDesc1 = errorDesc1 + ",\n" + errorDesc.trim();

            }

          }

          for (String temp : alStr) {

            if (!alStrDB.contains(temp)) {

              if (errorDesc1.isEmpty()) {

                errorDesc1 = temp.trim();

              } else {

                errorDesc1 = errorDesc1 + ",\n" + temp.trim();

              }

            }

          }

          ackVO.setErrorDesc(errorDesc1);

          errorDesc = "";

          errorDesc1 = "";

          rs1.close();

          pst1.close();

          if (errorCode.equals("SUCCESS"))

          {

            ackVO.setStatusField(errorCode);

            successCount++;

          }

          if (!errorCode.equals("SUCCESS"))

          {

            ackVO.setStatusField("FAIL");

            failCount++;

          }

          besAckList.add(ackVO);

          iCount = besAckList.size();

        }

        logger.info(ackVO.getOrmNo() + " :: " + ackVO.getBoeNoEntered() + " :: " + ackVO.getBoeDateEntered() + " :: " + ackVO.getBoePortEntered() + " :: " + ackVO.getBoePayRefEntered());

        ackVO.setSuccessCount(successCount);

        ackVO.setFailCount(failCount);

        ackVO.setBesAckValues(besAckList);

        ackVO.setTotalCount(iCount);

      }

      catch (Exception exception)

      {

        exception.printStackTrace();

        throwDAOException(exception);

      }

      finally

      {

        DBConnectionUtility.surrenderDB(con, pst, rs);

      }

      logger.info("Exiting Method");

      return ackVO;

    }
   
  
   
    public AcknowledgementVO deleteBesData(String[] chkList, String remarks, AcknowledgementVO ackVO)

      throws DAOException

    {

      logger.info("Entering Method deleteBesData");

      Connection con = null;

      ResultSet rs = null;

      String BES_QUERY = null;

      String BES_QUERY1 = null;

      String BES_QUERY2 = null;

      LoggableStatement pst = null;

      CommonMethods commonMethods = null;

      boolean outRefNoFlag = false;

      boolean boeNoFlag = false;

      boolean boeDateFlag = false;

      boolean portcodeFlag = false;

      boolean payrefnoFlag = false;

      int successCount = 0;

      int failCount = 0;

      int iCount = 0;

      String sInParam = "";

      String errorDesc = "";

      String errorDesc1 = "";

      String sEerrDescQuery = "";

      String errorCode = "";

      ResultSet rs1 = null;

      ResultSet rs2 = null;

      LoggableStatement pst1 = null;

      LoggableStatement pst2 = null;

      String BOE_QUERY = null;

      LoggableStatement pstBOE = null;

      ResultSet rsBOE = null;

      int recCnt = 0;

      try

      {

        ArrayList<AcknowledgementVO> besAckList = new ArrayList();

        commonMethods = new CommonMethods();

        con = DBConnectionUtility.getConnection();

        BES_QUERY = "SELECT PORTOFDISCHARGE,BOENUMBER,TO_CHAR(TO_DATE(BOEDATE,'DD-MM-YY'),'DD/MM/YYYY') AS BOEDATE,IECODE,CHANGEIECODE,ADCODE,RECORDINDICATOR,PAYMENTPARTY,PAYMENTREFNUMBER,ORNUMBER,ORADCODE,REMITTCUR,BILLCLOSUREINDI,INVOICESERIALNO,INVOICENO,INVOICEAMTIC,INVOICEAMT,ERRORCODES FROM ETT_BES_ACK WHERE BOENUMBER = BOENUMBER  ";

        if (chkList != null) {

          for (int i = 0; i < chkList.length; i++)

          {

            String a = chkList[i];

            String[] b = a.split(":");

            logger.info(b[0] + " :: " + b[1] + " :: " + b[2] + " :: " + b[3] + " :: " + b[4] + " :: " + b[5]);

            if (!commonMethods.isNull(b[4]))

            {

              BES_QUERY = BES_QUERY + " AND TRIM(ORNUMBER) ='" + b[4] + "'";

              outRefNoFlag = true;

            }

            if (!commonMethods.isNull(b[0]))

            {

              BES_QUERY = BES_QUERY + " AND TRIM(BOENUMBER) ='" + b[0] + "'";

              boeNoFlag = true;

            }

            if (!commonMethods.isNull(b[1]))

            {

              BES_QUERY = BES_QUERY + " AND TO_CHAR(TO_DATE(BOEDATE,'DD-MM-YY'),'DD/MM/YYYY') ='" + b[1] + "'";

              boeDateFlag = true;

            }

            if (!commonMethods.isNull(b[2]))

            {

              BES_QUERY = BES_QUERY + " AND TRIM(PORTOFDISCHARGE) = '" + b[2] + "'";

              portcodeFlag = true;

            }

            if (!commonMethods.isNull(b[3]))

            {

              BES_QUERY = BES_QUERY + " AND TRIM(PAYMENTREFNUMBER) = '" + b[3] + "'";

              payrefnoFlag = true;

            }

            pst = new LoggableStatement(con, BES_QUERY);

            logger.info(" Query 1 :: " + pst.getQueryString());

            rs = pst.executeQuery();

            BOE_QUERY = "SELECT COUNT(*) AS RECORDCOUNT FROM ETT_BOE_PAYMENT WHERE BOE_PAYMENT_BP_BOE_NO=? AND PORT_CODE=? AND BOE_PAYMENT_BP_BOE_DT=? AND TRIM(BOE_PAYMENT_BP_PAY_REF)||TRIM(BOE_PAYMENT_BP_PAY_PART_REF)=? ";

            pstBOE = new LoggableStatement(con, BOE_QUERY);

            pstBOE.setString(1, b[0].trim());

            pstBOE.setString(2, b[2].trim());

            pstBOE.setString(3, b[1].trim());

            pstBOE.setString(4, b[4].trim());

            logger.info("record count :: " + pstBOE.getQueryString());

            rsBOE = pstBOE.executeQuery();

            if (rsBOE.next()) {

              recCnt = Integer.parseInt(rsBOE.getString("RECORDCOUNT").toString());

            }

            if (recCnt > 0) {

              try

              {

                int Inv_amt = 0;

                HttpSession session = ServletActionContext.getRequest().getSession();

                String loginedUserId = (String)session.getAttribute("loginedUserName");

                logger.info("loginedUserId IN updateStatus :: " + loginedUserId);

                String userID = loginedUserId;

                logger.info("Inside Before Delete :: outRefNo :: " + b[4] + " :: boeNo :: " + b[0] + " :: boeDate :: " + b[1] + " :: portcode :: " + b[2] + " :: payrefno :: " + b[3] + " :: Inv_amt :: " + b[5] + " :: Inv_No :: " + b[6]);

   
                CallableStatement cs = null;

                String sProcName = "{call ETT_BOE_CNCELLATION_RECORDS(?,?,?,?,?,?,?,?)}";

                cs = con.prepareCall(sProcName);

                cs.setString(1, commonMethods.getEmptyIfNull(b[4].trim()));

                cs.setString(2, commonMethods.getEmptyIfNull(b[0].trim()));

                cs.setString(3, commonMethods.getEmptyIfNull(b[1].trim()));

                cs.setString(4, commonMethods.getEmptyIfNull(b[2].trim()));

                cs.setString(5, commonMethods.getEmptyIfNull(b[3].trim()));

                cs.setDouble(6, Double.parseDouble(commonMethods.getEmptyIfNull(b[5].trim())));

                cs.setString(7, commonMethods.getEmptyIfNull(userID.trim()));

                cs.setString(8, commonMethods.getEmptyIfNull(remarks.trim()));

                if (cs.executeUpdate() <= 0) {

                  logger.info("Procedure having some problem");

                }

                if (cs != null) {

                  cs.close();

                }

              }

              catch (SQLException exc)

              {

                exc.printStackTrace();

              }

            }

          }

        }

        String sAckStatus = ackVO.getAckStatus();

        BES_QUERY1 = "SELECT eba.PORTOFDISCHARGE,eba.BOENUMBER,TO_CHAR(TO_DATE(eba.BOEDATE,'DD-MM-YY'),'DD/MM/YYYY') AS BOEDATE,eba.IECODE,eba.CHANGEIECODE,eba.ADCODE,eba.RECORDINDICATOR,eba.PAYMENTPARTY,eba.PAYMENTREFNUMBER,eba.ORNUMBER,eba.ORADCODE,eba.REMITTCUR,eba.BILLCLOSUREINDI,eba.INVOICESERIALNO,eba.INVOICENO,eba.INVOICEAMTIC,eba.INVOICEAMT,eba.ERRORCODES FROM ETT_BES_ACK eba WHERE eba.BOENUMBER not in (select ebac.BOENUMBER from ETT_BES_ACK_CANCEL ebac where  ebac.BOENUMBER = eba.BOENUMBER and ebac.BOEDATE=eba.BOEDATE and ebac.PORTOFDISCHARGE=eba.PORTOFDISCHARGE and ebac.PAYMENTREFNUMBER=eba.PAYMENTREFNUMBER) ";

   
   
   
        String outRefNo1 = ackVO.getOrmNo();

        if (!commonMethods.isNull(outRefNo1))

        {

          BES_QUERY1 = BES_QUERY1 + " AND TRIM(ORNUMBER) ='" + outRefNo1.trim() + "'";

          outRefNoFlag = true;

        }

        String boeNo1 = ackVO.getBoeNoEntered();

        if (!commonMethods.isNull(boeNo1))

        {

          BES_QUERY1 = BES_QUERY1 + " AND TRIM(BOENUMBER) ='" + boeNo1.trim() + "'";

          boeNoFlag = true;

        }

        String boeDate1 = commonMethods.getEmptyIfNull(ackVO.getBoeDateEntered());

        if (!commonMethods.isNull(boeDate1))

        {

          BES_QUERY1 = BES_QUERY1 + " AND TO_CHAR(TO_DATE(BOEDATE,'DD-MM-YY'),'DD/MM/YYYY') ='" + boeDate1.trim() + "'";

          boeDateFlag = true;

        }

        String portcode1 = commonMethods.getEmptyIfNull(ackVO.getBoePortEntered());

        if (!commonMethods.isNull(portcode1))

        {

          BES_QUERY1 = BES_QUERY1 + " AND TRIM(PORTOFDISCHARGE) = '" + portcode1.trim() + "'";

          portcodeFlag = true;

        }

        String payrefno1 = ackVO.getBoePayRefEntered();

        if (!commonMethods.isNull(payrefno1))

        {

          BES_QUERY1 = BES_QUERY1 + " AND TRIM(PAYMENTREFNUMBER) = '" + payrefno1.trim() + "'";

          payrefnoFlag = true;

        }

        if (sAckStatus.equals("S"))

        {

          BES_QUERY1 = BES_QUERY1 + " AND ERRORCODES = 'SUCCESS'";

        }

        else if (sAckStatus.equals("F"))

        {

          BES_QUERY1 = BES_QUERY1 + " AND PAYMENTREFNUMBER NOT IN (SELECT PAYMENTREFNUMBER FROM ETT_BES_ACK WHERE ERRORCODES = 'SUCCESS')";

        }

        else

        {

          BES_QUERY2 = BES_QUERY1.toString();

          BES_QUERY1 = BES_QUERY1 + " AND ERRORCODES = 'SUCCESS' UNION ALL ";

          BES_QUERY2 = BES_QUERY2 + " AND PAYMENTREFNUMBER NOT IN (SELECT PAYMENTREFNUMBER FROM ETT_BES_ACK WHERE ERRORCODES = 'SUCCESS')";

          BES_QUERY1 = BES_QUERY1 + BES_QUERY2.toString();

        }

        pst1 = new LoggableStatement(con, BES_QUERY1);
        rs1 = pst1.executeQuery();
        while (rs1.next())
        {
          ackVO = new AcknowledgementVO();
          ackVO.setPortDischarge(rs1.getString("PORTOFDISCHARGE"));
          ackVO.setBoeNo(rs1.getString("BOENUMBER"));
          ackVO.setBoeDate(rs1.getString("BOEDATE"));
          ackVO.setBesIECode(rs1.getString("IECODE"));
          ackVO.setChanIECode(rs1.getString("CHANGEIECODE"));
          ackVO.setBesADCode(rs1.getString("ADCODE"));
          ackVO.setBesrecordInd(rs1.getString("RECORDINDICATOR"));
          ackVO.setPaymentParty(rs1.getString("PAYMENTPARTY"));
          ackVO.setPaymentRefNo(rs1.getString("PAYMENTREFNUMBER"));
          ackVO.setOrNo(rs1.getString("ORNUMBER"));
          ackVO.setOrADCode(rs1.getString("ORADCODE"));
          ackVO.setRemitCurr(rs1.getString("REMITTCUR"));
          ackVO.setBesbillCloInd(rs1.getString("BILLCLOSUREINDI"));
          ackVO.setInvoiceSNo(rs1.getString("INVOICESERIALNO"));
          ackVO.setInvoiceNo(rs1.getString("INVOICENO"));
          ackVO.setInvoiceAmt(rs1.getString("INVOICEAMT"));
          ackVO.setInvoiceAmtIC(rs1.getString("INVOICEAMTIC"));
          if (rs1.getString("ERRORCODES") != null) {
            errorCode = rs1.getString("ERRORCODES");
          }
          if (outRefNoFlag) {
            ackVO.setOrmNo(outRefNo1);
          }
          if (boeNoFlag) {
            ackVO.setBoeNoEntered(boeNo1);
          }
          if (boeDateFlag) {
            ackVO.setBoeDateEntered(boeDate1);
          }
          if (portcodeFlag) {
            ackVO.setBoePortEntered(portcode1);
          }
          if (payrefnoFlag) {
            ackVO.setBoePayRefEntered(payrefno1);
          }
          ackVO.setBesErrorCodes(errorCode);

   
   
          ArrayList<String> alStr = new ArrayList();
          ArrayList<String> alStrDB = new ArrayList();
          String[] splitErrorCodes = { "" };
          if (errorCode.contains(","))
          {
            splitErrorCodes = errorCode.split(",");
            sInParam = "";
            for (int j = 0; j < splitErrorCodes.length; j++)
            {
              alStr.add(splitErrorCodes[j]);
              if (sInParam.equals("")) {
                sInParam = "'" + splitErrorCodes[j] + "'";
              } else {
                sInParam = sInParam + ",'" + splitErrorCodes[j] + "'";
              }
            }
          }
          else
          {
            alStr.add(errorCode);
            sInParam = "'" + errorCode + "'";
          }
          sEerrDescQuery = "SELECT Trim(ERRCO) as ERRCO, ERRCO||'-'||ERRMSG ERRCO FROM (SELECT ERROR_CODE ERRCO, ERROR_MSG ERRMSG FROM ETT_IDPMS_ERROR_CODES WHERE trim(ERROR_CODE) IN (?) AND PCATEGORY = 'BES')";
          pst2 = new LoggableStatement(con, sEerrDescQuery);
          pst2.setString(1, sInParam);
          rs2 = pst2.executeQuery();
          while (rs2.next())
          {
            alStrDB.add(rs2.getString(1));
            errorDesc = rs2.getString(2);
            if (errorDesc1.isEmpty()) {
              errorDesc1 = errorDesc.trim();
            } else {
              errorDesc1 = errorDesc1 + ",\n" + errorDesc.trim();
            }
          }
          for (String temp : alStr) {
            if (!alStrDB.contains(temp)) {
              if (errorDesc1.isEmpty()) {
                errorDesc1 = temp.trim();
              } else {
                errorDesc1 = errorDesc1 + ",\n" + temp.trim();
              }
            }
          }
          ackVO.setErrorDesc(errorDesc1);
          errorDesc = "";
          errorDesc1 = "";
          rs2.close();
          pst2.close();
          if (errorCode.equals("SUCCESS"))
          {
            ackVO.setStatusField(errorCode);
            successCount++;
          }
          if (!errorCode.equals("SUCCESS"))
          {
            ackVO.setStatusField("FAIL");
            failCount++;
          }
          besAckList.add(ackVO);
          iCount = besAckList.size();
        }
        ackVO.setSuccessCount(successCount);
        ackVO.setFailCount(failCount);
        ackVO.setBesAckValues(besAckList);
        ackVO.setTotalCount(iCount);
      }
      catch (Exception exception)
      {
        exception.printStackTrace();
      }
      finally
      {
        DBConnectionUtility.surrenderDB(null, pst1, rs1);
        DBConnectionUtility.surrenderDB(null, pstBOE, rsBOE);
        DBConnectionUtility.surrenderDB(con, pst, rs);
      }
      logger.info("Exiting Method deleteBesData");
      return ackVO;
    }
    public AcknowledgementVO fetchBesDeleteDataForChecker(AcknowledgementVO ackVO)
      throws DAOException
    {
      logger.info("Entering Method");
      int successCount = 0;
      int failCount = 0;
      int iCount = 0;
      String sInParam = "";
      String errorDesc = "";
      String errorDesc1 = "";
      String sEerrDescQuery = "";
      String errorCode = "";
      ArrayList<AcknowledgementVO> besAckList = null;
      LoggableStatement pst = null;
      ResultSet rs = null;
      Connection con = null;
      String query = null;
      LoggableStatement pst1 = null;
      ResultSet rs1 = null;
      CommonMethods commonMethods = null;
      String BES_QUERY = null;
      String BES_QUERY1 = null;
      String sAckStatus = "";
      boolean outRefNoFlag = false;
      boolean boeNoFlag = false;
      boolean boeDateFlag = false;
      boolean portcodeFlag = false;
      boolean payrefnoFlag = false;
      int setValue = 0;
      try
      {
        con = DBConnectionUtility.getConnection();

   
        besAckList = new ArrayList();

   
   
        HttpSession session = ServletActionContext.getRequest().getSession();
        String loginedUserId = (String)session.getAttribute("loginedUserName");
        logger.info("loginedUserId Checker data :: " + loginedUserId);
        sAckStatus = ackVO.getAckStatus();
        commonMethods = new CommonMethods();
        BES_QUERY = "SELECT PORTOFDISCHARGE,BOENUMBER,TO_CHAR(TO_DATE(BOEDATE,'DD-MM-YY'),'DD/MM/YYYY') AS BOEDATE,IECODE,CHANGEIECODE,ADCODE,RECORDINDICATOR,PAYMENTPARTY,PAYMENTREFNUMBER,ORNUMBER,ORADCODE,REMITTCUR,BILLCLOSUREINDI,INVOICESERIALNO,INVOICENO,INVOICEAMTIC,INVOICEAMT,ERRORCODES,CANCEL_MAKER_REMARK FROM ETT_BES_ACK_CANCEL WHERE BOENUMBER = BOENUMBER  AND CANCEL_STATUS='P' ";

   
   
        String outRefNo = ackVO.getOrmNo();
        if (!commonMethods.isNull(outRefNo))

        {

          BES_QUERY = BES_QUERY + " AND TRIM(ORNUMBER) ='" + outRefNo.trim() + "'";

          outRefNoFlag = true;

        }

        String boeNo = ackVO.getBoeNoEntered();

        if (!commonMethods.isNull(boeNo))

        {

          BES_QUERY = BES_QUERY + " AND TRIM(BOENUMBER) ='" + boeNo.trim() + "'";

          boeNoFlag = true;

        }

        String boeDate = commonMethods.getEmptyIfNull(ackVO.getBoeDateEntered());

        if (!commonMethods.isNull(boeDate))

        {

          BES_QUERY = BES_QUERY + " AND TO_CHAR(TO_DATE(BOEDATE,'DD-MM-YY'),'DD/MM/YYYY') ='" + boeDate.trim() + "'";

          boeDateFlag = true;

        }

        String portcode = commonMethods.getEmptyIfNull(ackVO.getBoePortEntered());

        if (!commonMethods.isNull(portcode))

        {

          BES_QUERY = BES_QUERY + " AND TRIM(PORTOFDISCHARGE) = '" + portcode.trim() + "'";

          portcodeFlag = true;

        }

        String payrefno = ackVO.getBoePayRefEntered();

        if (!commonMethods.isNull(payrefno))

        {

          BES_QUERY = BES_QUERY + " AND TRIM(PAYMENTREFNUMBER) = '" + payrefno.trim() + "'";

          payrefnoFlag = true;

        }

        if (!commonMethods.isNull(loginedUserId)) {

          BES_QUERY = BES_QUERY + " AND (TRIM(CANCEL_MAKER_USERID) != '" + loginedUserId.trim() + "' OR TRIM(CANCEL_MAKER_USERID) IS NULL)";

        }

        if (sAckStatus.equals("S"))

        {

          BES_QUERY = BES_QUERY + " AND ERRORCODES = 'SUCCESS'";

        }

        else if (sAckStatus.equals("F"))

        {

          BES_QUERY = BES_QUERY + " AND PAYMENTREFNUMBER NOT IN (SELECT PAYMENTREFNUMBER FROM ETT_BES_ACK WHERE ERRORCODES = 'SUCCESS')";

        }

        else

        {

          BES_QUERY1 = BES_QUERY.toString();

          BES_QUERY = BES_QUERY + " AND ERRORCODES = 'SUCCESS' UNION ALL ";

          BES_QUERY1 = BES_QUERY1 + " AND PAYMENTREFNUMBER NOT IN (SELECT PAYMENTREFNUMBER FROM ETT_BES_ACK WHERE ERRORCODES = 'SUCCESS')";

          BES_QUERY = BES_QUERY + BES_QUERY1.toString();

        }

        pst = new LoggableStatement(con, BES_QUERY);

        logger.info("Checker data Query :: " + pst.getQueryString());

        rs = pst.executeQuery();

        while (rs.next())

        {

          ackVO = new AcknowledgementVO();

          ackVO.setPortDischarge(rs.getString("PORTOFDISCHARGE"));

          ackVO.setBoeNo(rs.getString("BOENUMBER"));

          ackVO.setBoeDate(rs.getString("BOEDATE"));

          ackVO.setBesIECode(rs.getString("IECODE"));

          ackVO.setChanIECode(rs.getString("CHANGEIECODE"));

          ackVO.setBesADCode(rs.getString("ADCODE"));

          ackVO.setBesrecordInd(rs.getString("RECORDINDICATOR"));

          ackVO.setPaymentParty(rs.getString("PAYMENTPARTY"));

          ackVO.setPaymentRefNo(rs.getString("PAYMENTREFNUMBER"));

          ackVO.setOrNo(rs.getString("ORNUMBER"));

          ackVO.setOrADCode(rs.getString("ORADCODE"));

          ackVO.setRemitCurr(rs.getString("REMITTCUR"));

          ackVO.setBesbillCloInd(rs.getString("BILLCLOSUREINDI"));

          ackVO.setInvoiceSNo(rs.getString("INVOICESERIALNO"));

          ackVO.setInvoiceNo(rs.getString("INVOICENO"));

          ackVO.setInvoiceAmt(rs.getString("INVOICEAMT"));

          ackVO.setInvoiceAmtIC(rs.getString("INVOICEAMTIC"));

          ackVO.setMakerRemark(rs.getString("CANCEL_MAKER_REMARK"));

          if (outRefNoFlag) {

            ackVO.setOrmNo(outRefNo);

          }

          if (boeNoFlag) {

            ackVO.setBoeNoEntered(boeNo);

          }

          if (boeDateFlag) {

            ackVO.setBoeDateEntered(boeDate);

          }

          if (portcodeFlag) {

            ackVO.setBoePortEntered(portcode);

          }

          if (payrefnoFlag) {

            ackVO.setBoePayRefEntered(payrefno);

          }

          if (rs.getString("ERRORCODES") != null) {

            errorCode = rs.getString("ERRORCODES");

          }

          ackVO.setBesErrorCodes(errorCode);

   
   
          ArrayList<String> alStr = new ArrayList();

          ArrayList<String> alStrDB = new ArrayList();

          String[] splitErrorCodes = { "" };

          if (errorCode.contains(","))

          {

            splitErrorCodes = errorCode.split(",");

            sInParam = "";

            for (int j = 0; j < splitErrorCodes.length; j++)

            {

              alStr.add(splitErrorCodes[j]);

              if (sInParam.equals("")) {

                sInParam = "'" + splitErrorCodes[j] + "'";

              } else {

                sInParam = sInParam + ",'" + splitErrorCodes[j] + "'";

              }

            }

          }

          else

          {

            alStr.add(errorCode);

            sInParam = "'" + errorCode + "'";

          }

          sEerrDescQuery = "SELECT Trim(ERRCO) as ERRCO, ERRCO||'-'||ERRMSG ERRCO FROM (SELECT ERROR_CODE ERRCO, ERROR_MSG ERRMSG FROM ETT_IDPMS_ERROR_CODES WHERE trim(ERROR_CODE) IN (?) AND PCATEGORY = 'BES')";

          pst1 = new LoggableStatement(con, sEerrDescQuery);

          pst1.setString(1, sInParam);

          rs1 = pst1.executeQuery();

          while (rs1.next())

          {

            alStrDB.add(rs1.getString(1));

            errorDesc = rs1.getString(2);

            if (errorDesc1.isEmpty()) {

              errorDesc1 = errorDesc.trim();

            } else {

              errorDesc1 = errorDesc1 + ",\n" + errorDesc.trim();

            }

          }

          for (String temp : alStr) {

            if (!alStrDB.contains(temp)) {

              if (errorDesc1.isEmpty()) {

                errorDesc1 = temp.trim();

              } else {

                errorDesc1 = errorDesc1 + ",\n" + temp.trim();

              }

            }

          }

          ackVO.setErrorDesc(errorDesc1);

          errorDesc = "";

          errorDesc1 = "";

          rs1.close();

          pst1.close();

          if (errorCode.equals("SUCCESS"))

          {

            ackVO.setStatusField(errorCode);

            successCount++;

          }

          if (!errorCode.equals("SUCCESS"))

          {

            ackVO.setStatusField("FAIL");

            failCount++;

          }

          besAckList.add(ackVO);

          iCount = besAckList.size();

        }

        logger.info(ackVO.getOrmNo() + " :: " + ackVO.getBoeNoEntered() + " :: " + ackVO.getBoeDateEntered() + " :: " + ackVO.getBoePortEntered() + " :: " + ackVO.getBoePayRefEntered());

        ackVO.setSuccessCount(successCount);

        ackVO.setFailCount(failCount);

        ackVO.setBesAckValues(besAckList);

        ackVO.setTotalCount(iCount);

      }

      catch (Exception exception)

      {

        exception.printStackTrace();

        throwDAOException(exception);

      }

      finally

      {

        DBConnectionUtility.surrenderDB(con, pst, rs);

      }

      logger.info("Exiting Method");

      return ackVO;

    }
    public String updateBesCheckerData(String[] chkList1, String check1, String remarks1)

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

    	    String sqlQuery2 = null;

    	    String sqlQuery4 = null;

    	    String deletequery = null;

    	    int count = 0;

    	    int count1 = 0;

    	    int count2 = 0;

    	    int count3 = 0;

    	    int count4 = 0;

    	    CommonMethods commonMethods = null;

    	    try

    	    {

    	      commonMethods = new CommonMethods();

    	      con = DBConnectionUtility.getConnection();

    	      HttpSession session = ServletActionContext.getRequest().getSession();

    	      String loginedUserId = (String)session.getAttribute("loginedUserName");

    	      logger.info("loginedUserId IN updateBesCheckerData :: " + loginedUserId);

    	      if ((chkList1 != null) && (check1 != null)) {

    	        for (int i = 0; i < chkList1.length; i++)

    	        {

    	          String temp = chkList1[i];

    	          String[] b = temp.split(":");

    	          logger.info(b[0] + " :: " + b[1] + " :: " + b[2] + " :: " + b[3] + " :: " + b[4] + " :: " + b[5] + " :: " + b[6]);

    	          if (check1.equalsIgnoreCase("Approve"))

    	          {

    	            query = "UPDATE ETT_BES_ACK_CANCEL SET CANCEL_STATUS ='A',CANCEL_CHECKER_TIMESTAMP = CURRENT_TIMESTAMP,CANCEL_CHECKER_USERID=?,CANCEL_CHECKER_REMARK=?  WHERE ORNUMBER=? AND PAYMENTREFNUMBER=? AND BOENUMBER=? AND BOEDATE=? AND PORTOFDISCHARGE=?";

    	 
    	            loggableStatement = new LoggableStatement(con, query);

    	            loggableStatement.setString(1, loginedUserId);

    	            loggableStatement.setString(2, remarks1);

    	            loggableStatement.setString(3, b[4].trim());

    	            loggableStatement.setString(4, b[3].trim());

    	            loggableStatement.setString(5, b[0].trim());

    	            loggableStatement.setString(6, b[1].trim());

    	            loggableStatement.setString(7, b[2].trim());

    	            count = loggableStatement.executeUpdate();

    	            if (count > 0)

    	            {

    	              logger.info("BEFORE DELETE  ETT_BES_ACK :: " + b[0]);

    	              deletequery = "DELETE FROM ETT_BES_ACK WHERE ORNUMBER=? AND PAYMENTREFNUMBER=? AND BOENUMBER=? AND BOEDATE=? AND PORTOFDISCHARGE=?";

    	 
    	              LoggableStatement loggableStatement1 = new LoggableStatement(con, deletequery);

    	              loggableStatement1.setString(1, b[4].trim());

    	              loggableStatement1.setString(2, b[3].trim());

    	              loggableStatement1.setString(3, b[0].trim());

    	              loggableStatement1.setString(4, b[1].trim());

    	              loggableStatement1.setString(5, b[2].trim());

    	              count1 = loggableStatement1.executeUpdate();

    	              closeSqlRefferance(null, loggableStatement1, null);

    	              logger.info("AFTER DELETE  ETT_BES_ACK :: " + b[0]);

    	              if (count1 > 0)

    	              {

    	                logger.info("BEFORE DELETE  ETT_BOE_PAYMENT");

    	                sqlQuery1 = "DELETE FROM ETT_BOE_PAYMENT    WHERE TRIM(BOE_PAYMENT_BP_BOE_NO) = ? AND TO_DATE(BOE_PAYMENT_BP_BOE_DT) = TO_DATE(?,'DD/MM/YYYY')    AND TRIM(PORT_CODE) = ? AND (TRIM(BOE_PAYMENT_BP_PAY_REF)||TRIM(BOE_PAYMENT_BP_PAY_PART_REF)) = ?";

    	 
    	 
    	                LoggableStatement loggableStatement2 = new LoggableStatement(con, sqlQuery1);

    	                loggableStatement2.setString(1, b[0].trim());

    	                loggableStatement2.setString(2, b[1].trim());

    	                loggableStatement2.setString(3, b[2].trim());

    	                loggableStatement2.setString(4, b[4].trim());

    	                count2 = loggableStatement2.executeUpdate();

    	                closeSqlRefferance(null, loggableStatement2, null);

    	                logger.info("AFTER DELETE ETT_BOE_PAYMENT");

    	                if (count2 > 0)

    	                {

    	                  logger.info("BEFORE DELETE  ETT_BOE_INV_PAYMENT ");

    	                  sqlQuery2 = " DELETE FROM ETT_BOE_INV_PAYMENT  WHERE BOE_NO=? AND TO_DATE(BOE_DATE) = TO_DATE(?,'DD/MM/YYYY') AND TRIM(PORTCODE)=? AND (TRIM(PAYMENT_REF)||TRIM(EVENT_REF))=? AND RINV_SEQNO=? AND INV_NO=? ";

    	 
    	 
    	                  LoggableStatement loggableStatement3 = new LoggableStatement(con, sqlQuery2);

    	                  loggableStatement3.setString(1, b[0].trim());

    	                  loggableStatement3.setString(2, b[1].trim());

    	                  loggableStatement3.setString(3, b[2].trim());

    	                  loggableStatement3.setString(4, b[4].trim());

    	                  loggableStatement3.setString(5, b[3].trim());

    	                  loggableStatement3.setString(6, b[6].trim());

    	                  count3 = loggableStatement3.executeUpdate();

    	                  closeSqlRefferance(null, loggableStatement3, null);

    	                  logger.info("AFTER DELETE  ETT_BOE_INV_PAYMENT ");

    	                  String extField = "";

    	                  String getExField = "SELECT BEV.EXTFIELD as EXTFIELD FROM MASTER MAS,BASEEVENT BEV  WHERE MAS.KEY97=BEV.MASTER_KEY AND  TRIM(MAS.MASTER_REF)||TRIM(BEV.REFNO_PFIX)||TRIM(LPAD(BEV.REFNO_SERL,3,0))='" + 

    	                    b[4].trim() + "'";

    	                  LoggableStatement pstExtField = new LoggableStatement(con, getExField);

    	                  ResultSet rsExtField = pstExtField.executeQuery();

    	                  if (rsExtField.next())

    	                  {

    	                    extField = rsExtField.getString("EXTFIELD");

    	                    logger.info("------extField------" + extField);

    	                  }

    	                  closeStatementResultSet(pstExtField, rsExtField);

    	 
    	                  logger.info("BEFORE DELETE  EXTEVENTBOE ");

    	                  sqlQuery4 = "DELETE FROM EXTEVENTBOE where TRIM(FK_EVENT)=? and TRIM(BOENUM)=? ";

    	                  LoggableStatement loggableStatement4 = new LoggableStatement(con, sqlQuery4);

    	                  loggableStatement4.setString(1, extField.trim());

    	                  loggableStatement4.setString(2, b[0].trim());

    	                  count4 = loggableStatement4.executeUpdate();

    	                  closeSqlRefferance(null, loggableStatement4, null);

    	                  logger.info("AFTER DELETE  EXTEVENTBOE ");

    	                }

    	              }

    	            }

    	          }

    	          else if (check1.equalsIgnoreCase("Reject"))

    	          {

    	            query = "UPDATE ETT_BES_ACK_CANCEL SET CANCEL_STATUS ='R',CANCEL_CHECKER_TIMESTAMP = CURRENT_TIMESTAMP,CANCEL_CHECKER_USERID=?,CANCEL_CHECKER_REMARK=?  WHERE ORNUMBER=? AND PAYMENTREFNUMBER=? AND BOENUMBER=? AND BOEDATE=? AND PORTOFDISCHARGE=?";

    	 
    	            loggableStatement = new LoggableStatement(con, query);

    	            loggableStatement.setString(1, loginedUserId);

    	            loggableStatement.setString(2, remarks1);

    	            loggableStatement.setString(3, b[4].trim());

    	            loggableStatement.setString(4, b[3].trim());

    	            loggableStatement.setString(5, b[0].trim());

    	            loggableStatement.setString(6, b[1].trim());

    	            loggableStatement.setString(7, b[2].trim());

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
    public int checkLoginedUserType(AcknowledgementVO ackVO)

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

    	      if (!commonMethods.isNull(ackVO.getSessionUserName()))

    	      {

    	        sqlQuery = 

    	          "SELECT COUNT(*) AS LOGIN_COUNT FROM SECAGE88 U,TEAMUSRMAP T WHERE T.USERKEY = U.SKEY80 AND TRIM(UPPER(U.NAME85))  = TRIM(UPPER('" + ackVO.getSessionUserName() + "'))" + " AND TRIM(UPPER(T.TEAMKEY)) = TRIM(UPPER('" + ackVO.getPageType() + "'))";

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

    	}
    	 