package in.co.localization.dao.localization;
 
import in.co.localization.dao.AbstractDAO;

import in.co.localization.dao.exception.DAOException;

import in.co.localization.utility.ActionConstantsQuery;

import in.co.localization.utility.CommonMethods;

import in.co.localization.utility.DBConnectionUtility;

import in.co.localization.utility.LoggableStatement;

import in.co.localization.vo.localization.AcknowledgementListVO;

import in.co.localization.vo.localization.AcknowledgementVO;

import java.sql.Connection;

import java.sql.ResultSet;

import java.util.ArrayList;

import org.apache.log4j.Logger;
 
public class AcknowledgementDAO

  extends AbstractDAO

  implements ActionConstantsQuery

{

  static AcknowledgementDAO dao;

  private static Logger logger = Logger.getLogger(AcknowledgementDAO.class.getName());

  public static AcknowledgementDAO getDAO()

  {

    if (dao == null) {

      dao = new AcknowledgementDAO();

    }

    return dao;

  }

  public ArrayList<AcknowledgementVO> fetchRodData(AcknowledgementVO ackVO)

    throws DAOException

  {

    logger.info("Entering Method");

    ArrayList rodAckList = null;

    LoggableStatement pst = null;

    ResultSet rs = null;

    Connection con = null;

    boolean shippingNoFlag = false;

    boolean shippingDateFlag = false;

    boolean portCodeFlag = false;

    boolean formNoFlag = false;

    boolean dateFromFlag = false;

    boolean dateToFlag = false;

    int setValue = 0;

    try

    {

      con = DBConnectionUtility.getConnection();

      rodAckList = new ArrayList();

      CommonMethods commonMethods = null;

      String ROD_QUERY = "SELECT ESR.SHIPPINGBILLNO,TO_CHAR(TO_DATE(ESR.SHIPPINGBILLDATE, 'ddmmyyyy'),'dd-mm-yyyy') AS SHIPPINGBILLDATE, ESR.PORTCODE,ESR.FORMNO,ESR.RECORDINDICATOR,ESR.EXPORTTYPE,TO_CHAR(ESR.LEODATE, 'dd-mm-yyyy') AS LEODATE,ESR.IECODE, ESR.CHANGEDIECODE,ESR.ADCODE,ESR.ADEXPORTAGENCY,ESR.DIRECTDISPATCHINDICATOR,ESR.ADBILLNO,TO_CHAR(ESR.DATEOFNEGOTIATION, 'dd-mm-yyyy') AS DATEOFNEGOTIATION, ESR.BUYERNAME,ESR.BUYERCOUNTRY,ESR.ERRORCODES FROM ETT_ROD_ACK_FILES ERA,ETT_SHP_ROD_ACK_STG ESR WHERE ERA.FILE_NO = ESR.FILE_NO  AND ESR.ERRORCODES='SUCCESS'";

      commonMethods = new CommonMethods();

      String shippingNo = ackVO.getShippingBillNo();

      if (!commonMethods.isNull(shippingNo))

      {

        ROD_QUERY = ROD_QUERY + "AND ESR.SHIPPINGBILLNO =?";

        shippingNoFlag = true;

      }

      String shippingDate = commonMethods.getEmptyIfNull(ackVO.getShippingBillDate()).trim();

      if (!commonMethods.isNull(shippingDate))

      {

        ROD_QUERY = ROD_QUERY + "AND TO_CHAR(TO_DATE(ESR.SHIPPINGBILLDATE,'DDMMYYYY'),'DD-MM-YY')  =  ?";

        shippingDateFlag = true;

      }

      String portCode = ackVO.getPortCode();

      if (!commonMethods.isNull(portCode))

      {

        ROD_QUERY = ROD_QUERY + "AND ESR.PORTCODE =  ?";

        portCodeFlag = true;

      }

      String formNo = ackVO.getFormNo();

      if (!commonMethods.isNull(formNo))

      {

        ROD_QUERY = ROD_QUERY + "AND ESR.FORMNO =  ?";

        formNoFlag = true;

      }

      String dateFrom = ackVO.getDateFrom();

      String dateTo = ackVO.getDateTo();

      if ((!commonMethods.isNull(dateFrom)) && (!commonMethods.isNull(dateTo)))

      {

        ROD_QUERY = 

          ROD_QUERY + " AND ERA.PROCESSED_DATE BETWEEN TO_DATE(?,'DD-MM-YY') " + " AND TO_DATE(?,'DD-MM-YY')";

        dateFromFlag = true;

        dateToFlag = true;

      }

      else

      {

        if (!commonMethods.isNull(dateFrom))

        {

          ROD_QUERY = ROD_QUERY + " AND ERA.PROCESSED_DATE >= TO_DATE(?,'DD-MM-YY')";

          dateFromFlag = true;

        }

        if (!commonMethods.isNull(dateTo))

        {

          ROD_QUERY = ROD_QUERY + " AND ERA.PROCESSED_DATE <= TO_DATE(?,'DD-MM-YY')";

          dateToFlag = true;

        }

      }

      pst = new LoggableStatement(con, ROD_QUERY);

      if (shippingNoFlag) {

        pst.setString(++setValue, shippingNo.trim());

      }

      if (shippingDateFlag) {

        pst.setString(++setValue, shippingDate.trim());

      }

      if (portCodeFlag) {

        pst.setString(++setValue, portCode.trim());

      }

      if (formNoFlag) {

        pst.setString(++setValue, formNo.trim());

      }

      if (dateFromFlag) {

        pst.setString(++setValue, dateFrom.trim());

      }

      if (dateToFlag) {

        pst.setString(++setValue, dateTo.trim());

      }

      rs = pst.executeQuery();

      while (rs.next())

      {

        ackVO = new AcknowledgementVO();

        ackVO.setShippingBillNo(rs.getString("SHIPPINGBILLNO"));

        ackVO.setShippingBillDate(rs.getString("SHIPPINGBILLDATE"));

        ackVO.setPortCode(rs.getString("PORTCODE"));

        ackVO.setRecordInd(rs.getString("RECORDINDICATOR"));

        ackVO.setExportType(rs.getString("EXPORTTYPE"));

        ackVO.setFormNo(rs.getString("FORMNO"));

        ackVO.setLeoDate(rs.getString("LEODATE"));

        ackVO.setIeCode(rs.getString("IECODE"));

        ackVO.setChangedIecode(rs.getString("CHANGEDIECODE"));

        ackVO.setAdcode(rs.getString("ADCODE"));

        ackVO.setAdExportAgency(rs.getString("ADEXPORTAGENCY"));

        ackVO.setDirectDispatchInd(rs.getString("DIRECTDISPATCHINDICATOR"));

        ackVO.setDateOfNgo(rs.getString("DATEOFNEGOTIATION"));

        ackVO.setAdBillNo(rs.getString("ADBILLNO"));

        ackVO.setBuyerName(rs.getString("BUYERNAME"));

        ackVO.setBuyerCountry(rs.getString("BUYERCOUNTRY"));

        String errorCode = rs.getString("ERRORCODES");

        if ((errorCode != null) && (!errorCode.equalsIgnoreCase("SUCCESS"))) {

          errorCode = getErrorDescBasedOnErrorCode(errorCode, con);

        }

        ackVO.setErrorDes(errorCode);

        rodAckList.add(ackVO);

      }

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

    return rodAckList;

  }

  public ArrayList<AcknowledgementVO> fetchPenData(AcknowledgementVO ackVO)

    throws DAOException

  {

    logger.info("Entering -----------fetchPenData------------Method");

    ArrayList penAckList = null;

    LoggableStatement pst = null;

    ResultSet rs = null;

    Connection con = null;

    String query = null;

    boolean shippingNoFlag = false;

    boolean shippingDateFlag = false;

    boolean portCodeFlag = false;

    boolean formNoFlag = false;

    boolean dateFromFlag = false;

    boolean dateToFlag = false;

    int setValue = 0;

    try

    {

      con = DBConnectionUtility.getConnection();

      penAckList = new ArrayList();

      CommonMethods commonMethods = null;

      String PEN_QUERY = "SELECT EPS.SHIPPINGBILLNO,TO_CHAR(TO_DATE(EPS.SHIPPINGBILLDATE, 'ddmmyyyy'),'dd-mm-yyyy') as SHIPPINGBILLDATE, EPS.PORTCODE,EPS.FORMNO,EPS.RECORDINDICATOR,EPS.EXPORTTYPE,TO_CHAR(EPS.LEODATE, 'dd-mm-yyyy') as LEODATE,EPS.IECODE,EPS.ADCODE, EPS.REALIZATIONEXTENSIONIND,EPS.LETTERNO,TO_CHAR(EPS.LETTERDATE, 'dd-mm-yyyy') as LETTERDATE, TO_CHAR(EPS.EXTENDEDREALIZATIONDATE, 'dd-mm-yyyy') as EXTENDEDREALIZATIONDATE,EPS.ERRORCODES FROM ETT_PEN_ACK_FILES EPA,ETT_PEN_SHP_ACK_STG EPS WHERE EPA.FILE_NO = EPS.FILE_NO  ";

      commonMethods = new CommonMethods();

      String shippingNo = ackVO.getShippingBillNo();
      if (!commonMethods.isNull(shippingNo))

      {

        PEN_QUERY = PEN_QUERY + "AND EPS.SHIPPINGBILLNO =?";

        shippingNoFlag = true;

      }

      String shippingDate = commonMethods.getEmptyIfNull(ackVO.getShippingBillDate()).trim();

      if (!commonMethods.isNull(shippingDate))

      {

        PEN_QUERY = PEN_QUERY + "AND TO_CHAR(TO_DATE(EPS.SHIPPINGBILLDATE,'DDMMYYYY'),'DD-MM-YY') = ?";

        shippingDateFlag = true;

      }

      String portCode = ackVO.getPortCode();

      if (!commonMethods.isNull(portCode))

      {

        PEN_QUERY = PEN_QUERY + "AND EPS.PORTCODE =  ?";

        portCodeFlag = true;

      }

      String formNo = ackVO.getFormNo();

      if (!commonMethods.isNull(formNo))

      {

        PEN_QUERY = PEN_QUERY + "AND EPS.FORMNO =  ?";

        formNoFlag = true;

      }

      String dateFrom = ackVO.getDateFrom();

      String dateTo = ackVO.getDateTo();

      if ((!commonMethods.isNull(dateFrom)) && (!commonMethods.isNull(dateTo)))

      {

        PEN_QUERY = 

          PEN_QUERY + " AND EPA.PROCESSED_DATE BETWEEN TO_DATE(?,'DD-MM-YY') " + " AND TO_DATE(?,'DD-MM-YY')";

        dateFromFlag = true;

        dateToFlag = true;

      }

      else

      {

        if (!commonMethods.isNull(dateFrom))

        {

          PEN_QUERY = PEN_QUERY + " AND EPA.PROCESSED_DATE >= TO_DATE(?,'DD-MM-YY')";

          dateFromFlag = true;

        }

        if (!commonMethods.isNull(dateTo))

        {

          PEN_QUERY = PEN_QUERY + " AND EPA.PROCESSED_DATE <= TO_DATE(?,'DD-MM-YY')";

          dateToFlag = true;

        }

      }

      pst = new LoggableStatement(con, PEN_QUERY);

      if (shippingNoFlag) {

        pst.setString(++setValue, shippingNo.trim());

      }

      if (shippingDateFlag) {

        pst.setString(++setValue, shippingDate.trim());

      }

      if (portCodeFlag) {

        pst.setString(++setValue, portCode.trim());

      }

      if (formNoFlag) {

        pst.setString(++setValue, formNo.trim());

      }

      if (dateFromFlag) {

        pst.setString(++setValue, dateFrom.trim());

      }

      if (dateToFlag) {

        pst.setString(++setValue, dateTo.trim());

      }

      logger.info("Entering -----------fetchPenData-------query-----Method" + pst.getQueryString());

      rs = pst.executeQuery();

      while (rs.next())

      {

        ackVO = new AcknowledgementVO();

        ackVO.setShippingBillNo(rs.getString("SHIPPINGBILLNO"));

        ackVO.setShippingBillDate(rs.getString("SHIPPINGBILLDATE"));

        ackVO.setPortCode(rs.getString("PORTCODE"));

        ackVO.setRecordInd(rs.getString("RECORDINDICATOR"));

        ackVO.setExportType(rs.getString("EXPORTTYPE"));

        ackVO.setFormNo(rs.getString("FORMNO"));

        ackVO.setLeoDate(rs.getString("LEODATE"));

        ackVO.setIeCode(rs.getString("IECODE"));

        ackVO.setAdcode(rs.getString("ADCODE"));

        ackVO.setRealExtInd(rs.getString("REALIZATIONEXTENSIONIND"));

        ackVO.setLetterNo(rs.getString("LETTERNO"));

        ackVO.setLetterDate(rs.getString("LETTERDATE"));

        ackVO.setExtRealDate(rs.getString("EXTENDEDREALIZATIONDATE"));

        String errorCode = rs.getString("ERRORCODES");

        if ((errorCode != null) && (!errorCode.equalsIgnoreCase("SUCCESS"))) {

          errorCode = getErrorDescBasedOnErrorCode(errorCode, con);

        }

        ackVO.setErrorDes(errorCode);

        penAckList.add(ackVO);

      }

    }

    catch (Exception exception)

    {

      logger.info("Entering -----------fetchPenData------------exception" + exception);

      throwDAOException(exception);

    }

    finally

    {

      DBConnectionUtility.surrenderDB(con, pst, rs);

    }

    logger.info("Exiting Method");

    return penAckList;

  }

  public ArrayList<AcknowledgementVO> fetchTrrData(AcknowledgementVO ackVO)

    throws DAOException

  {

    logger.info("Entering ----------fetchTrrData-----------Method");

    ArrayList trrAckList = null;

    LoggableStatement pst = null;

    ResultSet rs = null;

    Connection con = null;

    String query = null;

    boolean shippingNoFlag = false;

    boolean shippingDateFlag = false;

    boolean portCodeFlag = false;

    boolean formNoFlag = false;

    boolean dateFromFlag = false;

    boolean dateToFlag = false;

    int setValue = 0;

    try

    {

      con = DBConnectionUtility.getConnection();

      trrAckList = new ArrayList();

      CommonMethods commonMethods = null;

      String TRR_QUERY = "SELECT ETS.SHIPPINGBILLNO,TO_CHAR(TO_DATE(ETS.SHIPPINGBILLDATE, 'ddmmyyyy'),'dd-mm-yyyy') as SHIPPINGBILLDATE, ETS.PORTCODE,ETS.FORMNO,ETS.EXPORTAGENCY,ETS.EXPORTTYPE,TO_CHAR(ETS.TRANSMISSIONDATE, 'dd-mm-yyyy') as TRANSMISSIONDATE, ETS.IECODE,ETS.EXISTINGADCODE,ETS.NEWADCODE,ETS.ERRORCODE  FROM ETT_TRR_ACK_FILES ETR,ETT_TRR_SHP_ACK_STG ETS WHERE ETR.FILE_NO = ETS.FILE_NO  ";

      commonMethods = new CommonMethods();

      String shippingNo = ackVO.getShippingBillNo();

      if (!commonMethods.isNull(shippingNo))

      {

        TRR_QUERY = TRR_QUERY + "AND ETS.SHIPPINGBILLNO =?";

        shippingNoFlag = true;

      }

      String shippingDate = commonMethods.getEmptyIfNull(ackVO.getShippingBillDate()).trim();

      if (!commonMethods.isNull(shippingDate))

      {

        TRR_QUERY = TRR_QUERY + "AND TO_CHAR(TO_DATE(ETS.SHIPPINGBILLDATE,'DDMMYYYY'),'DD-MM-YY') = ?";

        shippingDateFlag = true;

      }

      String portCode = ackVO.getPortCode();

      if (!commonMethods.isNull(portCode))

      {

        TRR_QUERY = TRR_QUERY + "AND ETS.PORTCODE = ?";

        portCodeFlag = true;

      }

      String formNo = ackVO.getFormNo();

      if (!commonMethods.isNull(formNo))

      {

        TRR_QUERY = TRR_QUERY + "AND ETS.FORMNO = ?";

        formNoFlag = true;

      }

      String dateFrom = ackVO.getDateFrom();

      String dateTo = ackVO.getDateTo();

      if ((!commonMethods.isNull(dateFrom)) && (!commonMethods.isNull(dateTo)))

      {

        TRR_QUERY = 

          TRR_QUERY + " AND ETR.PROCESSED_DATE BETWEEN TO_DATE(?,'DD-MM-YY') " + " AND TO_DATE(?,'DD-MM-YY')";

        dateFromFlag = true;

        dateToFlag = true;

      }

      else

      {

        if (!commonMethods.isNull(dateFrom))

        {

          TRR_QUERY = TRR_QUERY + " AND ETR.PROCESSED_DATE >= TO_DATE(?,'DD-MM-YY')";

          dateFromFlag = true;

        }

        if (!commonMethods.isNull(dateTo))

        {

          TRR_QUERY = TRR_QUERY + " AND ETR.PROCESSED_DATE <= TO_DATE(?,'DD-MM-YY')";

          dateToFlag = true;

        }

      }

      pst = new LoggableStatement(con, TRR_QUERY);

      if (shippingNoFlag) {

        pst.setString(++setValue, shippingNo.trim());

      }

      if (shippingDateFlag) {

        pst.setString(++setValue, shippingDate.trim());

      }

      if (portCodeFlag) {

        pst.setString(++setValue, portCode.trim());

      }

      if (formNoFlag) {

        pst.setString(++setValue, formNo.trim());

      }

      if (dateFromFlag) {

        pst.setString(++setValue, dateFrom.trim());

      }

      if (dateToFlag) {

        pst.setString(++setValue, dateTo.trim());

      }

      logger.info("Entering ----------fetchTrrData--------exception" + pst.getQueryString());

      rs = pst.executeQuery();

      while (rs.next())

      {

        ackVO = new AcknowledgementVO();

        ackVO.setShippingBillNo(rs.getString("SHIPPINGBILLNO"));

        ackVO.setShippingBillDate(rs.getString("SHIPPINGBILLDATE"));

        ackVO.setPortCode(rs.getString("PORTCODE"));

        ackVO.setFormNo(rs.getString("FORMNO"));

        ackVO.setAdExportAgency(rs.getString("EXPORTAGENCY"));

        ackVO.setExportType(rs.getString("EXPORTTYPE"));

        ackVO.setIeCode(rs.getString("IECODE"));

        ackVO.setExAdcode(rs.getString("EXISTINGADCODE"));

        ackVO.setNewAdcode(rs.getString("NEWADCODE"));

        String errorCode = rs.getString("ERRORCODE");

        if ((errorCode != null) && (!errorCode.equalsIgnoreCase("SUCCESS"))) {

          errorCode = getErrorDescBasedOnErrorCode(errorCode, con);

        }

        ackVO.setErrorDes(errorCode);

        trrAckList.add(ackVO);

      }

    }

    catch (Exception exception)

    {

      logger.info("Entering ----------fetchTrrData---------exception" + exception);

      throwDAOException(exception);

    }

    finally

    {

      DBConnectionUtility.surrenderDB(con, pst, rs);

    }

    logger.info("Exiting Method");

    return trrAckList;

  }

  public ArrayList<AcknowledgementVO> fetchIrmData(AcknowledgementVO ackVO)

    throws DAOException

  {

    logger.info("Entering------------fetchIrmData---------- Method");

    LoggableStatement pst = null;

    ResultSet rs = null;

    Connection con = null;

    ArrayList irmAckList = null;

    String query = null;

    boolean billRefNoFlag = false;

    boolean fircNoFlag = false;

    boolean dateFromFlag = false;

    boolean dateToFlag = false;

    int setValue = 0;

    try

    {

      con = DBConnectionUtility.getConnection();

      irmAckList = new ArrayList();

      CommonMethods commonMethods = null;

      String IRM_QUERY = "SELECT IRM.TRXN_REFNO AS MASTER_REFNO,IRM.REM_AMOUNT AS REM_AMOUNT, TO_CHAR(TO_DATE(IRM.REM_DATE,'DD-MM-YY'),'DD-MM-YY') AS REM_DATE,IRM.ADCODE AS ADCODE,IRM.FIRC_FLAG AS FIRC_FLAG, IRM.FIRC_NO AS FIRC_SERIAL_NO,TO_CHAR(TO_DATE(IRM.FIRC_DATE,'DD-MM-YY'),'DD-MM-YY') AS FIRC_DATE, IRM.FIRCAMOUNT AS AMOUNT,IRM.CURRENCY AS CURRENCY,IRM.IECODE AS IECODE,IRM.IENAME AS IENAME, IRM.REM_NAME AS REM_NAME,IRM.REM_COUNTRY AS REM_COUNTRY,IRM.REM_BANKNAME AS REM_BANKNAME, IRM.REM_BANKCOUNTRY AS REM_BANKCOUNTRY,IRM.SWIFT_REFNO AS SWIFT_REFNO, IRM.PURPOSECODE AS PURPOSECODE,IRM.REC_IND AS REC_IND,IRM.ACK_STATUS AS ACK_STATUS FROM ETT_IRM_XML_FILES_TBL IRM WHERE TRIM(IRM.MASTER_REFNO) = TRIM(IRM.MASTER_REFNO)  AND TRIM(IRM.ACK_STATUS) IS NOT NULL ";

      commonMethods = new CommonMethods();

      String billRefNo = ackVO.getBillRefNo();

      if (!commonMethods.isNull(billRefNo))

      {

        IRM_QUERY = IRM_QUERY + "AND IRM.TRXN_REFNO =?";

        billRefNoFlag = true;

      }

      String fircNo = ackVO.getFircNo();

      if (!commonMethods.isNull(fircNo))

      {

        IRM_QUERY = IRM_QUERY + "AND IRM.FIRC_NO = ?";

        fircNoFlag = true;

      }

      String dateFrom = ackVO.getDateFrom();

      String dateTo = ackVO.getDateTo();

      if ((!commonMethods.isNull(dateFrom)) && (!commonMethods.isNull(dateTo)))

      {

        IRM_QUERY = IRM_QUERY + " AND TO_DATE(TO_CHAR(IRM.CREATE_DATE,'DD-MM-YY'),'DD-MM-YY')  BETWEEN TO_DATE(?,'DD-MM-YY') " + " AND TO_DATE(?,'DD-MM-YY')";

        dateFromFlag = true;

        dateToFlag = true;

      }

      else

      {

        if (!commonMethods.isNull(dateFrom))

        {

          IRM_QUERY = IRM_QUERY + " AND TO_DATE(TO_CHAR(IRM.CREATE_DATE,'DD-MM-YY'),'DD-MM-YY')  >= TO_DATE(?,'DD-MM-YY')";
          dateFromFlag = true;
        }
        if (!commonMethods.isNull(dateTo))
        {
          IRM_QUERY = IRM_QUERY + " AND TO_DATE(TO_CHAR(IRM.CREATE_DATE,'DD-MM-YY'),'DD-MM-YY')  <= TO_DATE(?,'DD-MM-YY')";
          dateToFlag = true;
        }
      }
      pst = new LoggableStatement(con, IRM_QUERY);
      if (billRefNoFlag) {
        pst.setString(++setValue, billRefNo.trim());
      }
      if (fircNoFlag) {
        pst.setString(++setValue, fircNo.trim());
      }
      if (dateFromFlag) {
        pst.setString(++setValue, dateFrom.trim());
      }
      if (dateToFlag) {
        pst.setString(++setValue, dateTo.trim());
      }
      logger.info("Entering------------fetchIrmData------query" + pst.getQueryString());
      rs = pst.executeQuery();
      while (rs.next())
      {
        ackVO = new AcknowledgementVO();
        ackVO.setBillRefNo(rs.getString("MASTER_REFNO"));
        ackVO.setRemAmount(rs.getString("REM_AMOUNT"));
        ackVO.setRemDate(rs.getString("REM_DATE"));
        ackVO.setAdcode(rs.getString("ADCODE"));
        ackVO.setFircFlag(rs.getString("FIRC_FLAG"));
        ackVO.setFircNo(rs.getString("FIRC_SERIAL_NO"));
        ackVO.setFircAmount(rs.getString("AMOUNT"));
        ackVO.setFircDate(rs.getString("FIRC_DATE"));
        ackVO.setRemCurrency(rs.getString("CURRENCY"));
        ackVO.setIeCode(rs.getString("IECODE"));
        ackVO.setIeName(rs.getString("IENAME"));
        ackVO.setRemName(rs.getString("REM_NAME"));
        ackVO.setRemCountry(rs.getString("REM_COUNTRY"));
        ackVO.setRemBankName(rs.getString("REM_BANKNAME"));
        ackVO.setRemBankCountry(rs.getString("REM_BANKCOUNTRY"));
        ackVO.setPurposeCode(rs.getString("PURPOSECODE"));
        ackVO.setRecInd(rs.getString("REC_IND"));
        ackVO.setSwiftRefNo(rs.getString("SWIFT_REFNO"));
        String errorCode = rs.getString("ACK_STATUS");
        if ((errorCode != null) && (!errorCode.equalsIgnoreCase("SUCCESS"))) {
          errorCode = getErrorDescBasedOnErrorCode(errorCode, con);
        }
        ackVO.setErrorCode(errorCode);
        irmAckList.add(ackVO);
      }
    }
    catch (Exception exception)
    {
      logger.info("Entering------------fetchIrmData---------- exception--" + exception);
      throwDAOException(exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    logger.info("Exiting Method");
    return irmAckList;
  }
  public ArrayList<AcknowledgementVO> fetchIRFIRcData(AcknowledgementVO ackVO)
    throws DAOException
  {
    logger.info("Entering-----------fetchIRFIRcData-------- Method");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    ArrayList irFircAckList = null;
    String query = null;
    boolean billRefNoFlag = false;
    boolean fircNoFlag = false;
    boolean dateFromFlag = false;
    boolean dateToFlag = false;
    int setValue = 0;
    try
    {
      con = DBConnectionUtility.getConnection();
      irFircAckList = new ArrayList();
      CommonMethods commonMethods = null;
      String IRFIRC_QUERY = "SELECT FIR.TRANS_REF_NO AS MASTER_REFNO, EXTB.ADCODE AS ADCODE, CASE WHEN TRIM(FIR.FIRC_SERIAL_NO) IS NULL THEN 'N' ELSE 'Y' END AS FIRC_FLAG, FIR.FIRC_SERIAL_NO AS FIRC_SERIAL_NO, TO_CHAR(TO_DATE(FIR.FIRC_DATE,'DD-MM-YY'),'DD-MM-YY') AS FIRC_DATE, FIR.AMOUNT AS AMOUNT, FIR.PUR_CODE AS PURPOSECODE, FIR.ACK_STATUS AS ACK_STATUS FROM MASTER MAS, EXTBRAMAS EXTB,ETT_FIRC_LODGEMENT FIR WHERE TRIM(MAS.MASTER_REF) = TRIM(FIR.TRANS_REF_NO) AND TRIM(MAS.BHALF_BRN) = TRIM(EXTB.BCODE (+)) AND TRIM(FIR.ACK_STATUS) IS NOT NULL ";
      commonMethods = new CommonMethods();
      String billRefNo = ackVO.getBillRefNo();
      if (!commonMethods.isNull(billRefNo))
      {
        IRFIRC_QUERY = IRFIRC_QUERY + "AND FIR.TRANS_REF_NO =?";
        billRefNoFlag = true;
      }
      String fircNo = ackVO.getFircNo();
      if (!commonMethods.isNull(fircNo))
      {
        IRFIRC_QUERY = IRFIRC_QUERY + "AND FIR.FIRC_SERIAL_NO =  ?";
        fircNoFlag = true;
      }
      String dateFrom = ackVO.getDateFrom();
      String dateTo = ackVO.getDateTo();
      if ((!commonMethods.isNull(dateFrom)) && (!commonMethods.isNull(dateTo)))
      {
        IRFIRC_QUERY = 
          IRFIRC_QUERY + " AND TO_DATE(TO_CHAR(FIR.LASTUPDATE,'DD-MM-YY'),'DD-MM-YY')  BETWEEN TO_DATE(?,'DD-MM-YY') " + " AND TO_DATE(?,'DD-MM-YY')";
        dateFromFlag = true;
        dateToFlag = true;
      }
      else
      {
        if (!commonMethods.isNull(dateFrom))
        {
          IRFIRC_QUERY = 
            IRFIRC_QUERY + " AND TO_DATE(TO_CHAR(FIR.LASTUPDATE,'DD-MM-YY'),'DD-MM-YY')  >= TO_DATE(?,'DD-MM-YY')";
          dateFromFlag = true;
        }
        if (!commonMethods.isNull(dateTo))
        {
          IRFIRC_QUERY = 
            IRFIRC_QUERY + " AND TO_DATE(TO_CHAR(FIR.LASTUPDATE,'DD-MM-YY'),'DD-MM-YY')  <= TO_DATE(?,'DD-MM-YY')";
          dateToFlag = true;
        }
      }
      pst = new LoggableStatement(con, IRFIRC_QUERY);
      if (billRefNoFlag) {
        pst.setString(++setValue, billRefNo.trim());
      }
      if (fircNoFlag) {
        pst.setString(++setValue, fircNo.trim());
      }
      if (dateFromFlag) {
        pst.setString(++setValue, dateFrom.trim());
      }
      if (dateToFlag) {
        pst.setString(++setValue, dateTo.trim());
      }
      logger.info("Entering-----------fetchIRFIRcData-------query" + pst.getQueryString());
      rs = pst.executeQuery();
      while (rs.next())
      {
        ackVO = new AcknowledgementVO();
        ackVO.setBillRefNo(rs.getString("MASTER_REFNO"));
        ackVO.setAdcode(rs.getString("ADCODE"));
        ackVO.setFircFlag(rs.getString("FIRC_FLAG"));
        ackVO.setFircNo(rs.getString("FIRC_SERIAL_NO"));
        ackVO.setFircAmount(rs.getString("AMOUNT"));
        ackVO.setFircDate(rs.getString("FIRC_DATE"));
        String errorCode = rs.getString("ACK_STATUS");
        if ((errorCode != null) && (!errorCode.equalsIgnoreCase("SUCCESS"))) {
          errorCode = getErrorDescBasedOnErrorCode(errorCode, con);
        }
        ackVO.setErrorCode(errorCode);
        irFircAckList.add(ackVO);
      }
    }
    catch (Exception exception)
    {
      logger.info("Entering-----------fetchIRFIRcData------exception" + exception);
      throwDAOException(exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    logger.info("Exiting Method");
    return irFircAckList;
  }
  public AcknowledgementListVO getAckPRNReportList(AcknowledgementListVO ackListVO)
    throws DAOException
  {
    logger.info("Entering---getAckPRNReportList--- Method");
    ArrayList prnAckShippingList = null;
    ArrayList prnAckInvoiceList = null;
    try
    {
      prnAckShippingList = fetchPrnShippingData(ackListVO);
      if (!prnAckShippingList.isEmpty()) {
        ackListVO.setPrnShippingList(prnAckShippingList);
      }
      prnAckInvoiceList = fetchPrnInvoiceData(ackListVO);
      if (!prnAckInvoiceList.isEmpty()) {
        ackListVO.setPrnInvoiceList(prnAckInvoiceList);
      }
    }
    catch (Exception exception)

    {

      logger.info("Entering---getAckPRNReportList--- exception" + exception);

      exception.printStackTrace();

      throwDAOException(exception);

    }

    logger.info("Exiting Method");

    return ackListVO;

  }

  public ArrayList<AcknowledgementVO> fetchPrnShippingData(AcknowledgementListVO ackListVO)

    throws DAOException

  {

    logger.info("Entering---fetchPrnShippingData-- Method");

    ArrayList prnAckShippingList = null;

    LoggableStatement pst = null;

    ResultSet rs = null;

    Connection con = null;

    String query = null;

    AcknowledgementVO ackVO = null;

    boolean shippingNoFlag = false;

    boolean shippingDateFlag = false;

    boolean portCodeFlag = false;

    boolean formNoFlag = false;

    boolean paySeqNoFlag = false;

    boolean dateFromFlag = false;

    boolean dateToFlag = false;

    int setValue = 0;

    try

    {

      con = DBConnectionUtility.getConnection();

      prnAckShippingList = new ArrayList();

      CommonMethods commonMethods = null;

      String PRN_SHP_QUERY = "SELECT EPS.SHIPPINGBILLNO,TO_CHAR(TO_DATE(EPS.SHIPPINGBILLDATE, 'ddmmyyyy'),'dd-mm-yyyy') as SHIPPINGBILLDATE, EPS.PORTCODE,EPS.FORMNO,EPS.RECORDINDICATOR,EPS.EXPORTTYPE,TO_CHAR(EPS.LEODATE, 'dd-mm-yyyy') as LEODATE,EPS.ADCODE, EPS.PAY_SEQNO,EPS.INWARDNO,EPS.FIRCNO,EPS.REM_ADCODE,EPS.PAY_PARTY,EPS.REALIZATONCURR,TO_CHAR(EPS.REALIZATONDATE, 'dd-mm-yyyy') as REALIZATONDATE,EPS.ERRORCODES  FROM ETT_PRN_ACK_FILES EPA,ETT_PRN_SHP_ACK_STG EPS WHERE EPA.FILE_NO = EPS.FILE_NO  ";

      commonMethods = new CommonMethods();

      String shippingNo = ackListVO.getShippingBillNo();

      if (!commonMethods.isNull(shippingNo))

      {

        PRN_SHP_QUERY = PRN_SHP_QUERY + "AND EPS.SHIPPINGBILLNO =?";

        shippingNoFlag = true;

      }

      String shippingDate = commonMethods.getEmptyIfNull(ackListVO.getShippingBillDate()).trim();

      if (!commonMethods.isNull(shippingDate))

      {

        PRN_SHP_QUERY = PRN_SHP_QUERY + "AND TO_CHAR(TO_DATE(EPS.SHIPPINGBILLDATE,'DDMMYYYY'),'DD-MM-YY') = ?";

        shippingDateFlag = true;

      }

      String portCode = ackListVO.getPortCode();

      if (!commonMethods.isNull(portCode))

      {

        PRN_SHP_QUERY = PRN_SHP_QUERY + "AND EPS.PORTCODE =  ?";

        portCodeFlag = true;

      }

      String formNo = ackListVO.getFormNo();

      if (!commonMethods.isNull(formNo))

      {

        PRN_SHP_QUERY = PRN_SHP_QUERY + "AND EPS.FORMNO = ?";

        formNoFlag = true;

      }

      String paySeqNo = ackListVO.getPaySeqNo();

      if (!commonMethods.isNull(paySeqNo))

      {

        PRN_SHP_QUERY = PRN_SHP_QUERY + "AND EPS.PAY_SEQNO = ?";

        paySeqNoFlag = true;

      }

      String dateFrom = ackListVO.getDateFrom();

      String dateTo = ackListVO.getDateTo();

      if ((!commonMethods.isNull(dateFrom)) && (!commonMethods.isNull(dateTo)))

      {

        PRN_SHP_QUERY = PRN_SHP_QUERY + " AND EPA.PROCESSED_DATE BETWEEN TO_DATE(?,'DD-MM-YY') " + " AND TO_DATE(?,'DD-MM-YY')";

        dateFromFlag = true;

        dateToFlag = true;

      }

      else

      {

        if (!commonMethods.isNull(dateFrom))

        {

          PRN_SHP_QUERY = PRN_SHP_QUERY + " AND EPA.PROCESSED_DATE >= TO_DATE(?,'DD-MM-YY')";

          dateFromFlag = true;

        }

        if (!commonMethods.isNull(dateTo))

        {

          PRN_SHP_QUERY = PRN_SHP_QUERY + " AND EPA.PROCESSED_DATE <= TO_DATE(?,'DD-MM-YY')";

          dateToFlag = true;

        }

      }

      pst = new LoggableStatement(con, PRN_SHP_QUERY);

      if (shippingNoFlag) {

        pst.setString(++setValue, shippingNo.trim());

      }

      if (shippingDateFlag) {

        pst.setString(++setValue, shippingDate.trim());

      }

      if (portCodeFlag) {

        pst.setString(++setValue, portCode.trim());

      }

      if (formNoFlag) {

        pst.setString(++setValue, formNo.trim());

      }

      if (paySeqNoFlag) {

        pst.setString(++setValue, paySeqNo.trim());

      }

      if (dateFromFlag) {

        pst.setString(++setValue, dateFrom.trim());

      }

      if (dateToFlag) {

        pst.setString(++setValue, dateTo.trim());

      }

      logger.info("Entering---fetchPrnShippingData--query" + pst.getQueryString());

      rs = pst.executeQuery();

      while (rs.next())

      {

        ackVO = new AcknowledgementVO();

        ackVO.setShippingBillNo(rs.getString("SHIPPINGBILLNO"));

        ackVO.setShippingBillDate(rs.getString("SHIPPINGBILLDATE"));

        ackVO.setPortCode(rs.getString("PORTCODE"));

        ackVO.setFormNo(rs.getString("FORMNO"));

        ackVO.setExportType(rs.getString("EXPORTTYPE"));

        ackVO.setRecordInd(rs.getString("RECORDINDICATOR"));

        ackVO.setLeoDate(rs.getString("LEODATE"));

        ackVO.setExAdcode(rs.getString("ADCODE"));

        ackVO.setPaymentSequence(rs.getString("PAY_SEQNO"));

        ackVO.setIrmNumber(rs.getString("INWARDNO"));

        ackVO.setFircNo(rs.getString("FIRCNO"));

        ackVO.setRemittanceAdCode(rs.getString("REM_ADCODE"));

        ackVO.setRealizationCurr(rs.getString("REALIZATONCURR"));

        ackVO.setRealizationDate(rs.getString("REALIZATONDATE"));

        ackVO.setThirdParty(rs.getString("PAY_PARTY"));

        String errorCode = rs.getString("ERRORCODES");

        if ((errorCode != null) && (!errorCode.equalsIgnoreCase("SUCCESS"))) {

          errorCode = getErrorDescBasedOnErrorCode(errorCode, con);

        }

        ackVO.setErrorDes(errorCode);

        prnAckShippingList.add(ackVO);

      }

    }

    catch (Exception exception)

    {

      logger.info("Entering---fetchPrnShippingData--exception" + exception);

      throwDAOException(exception);

    }

    finally

    {

      DBConnectionUtility.surrenderDB(con, pst, rs);

    }

    logger.info("Exiting Method");

    return prnAckShippingList;

  }

  public ArrayList<AcknowledgementVO> fetchPrnInvoiceData(AcknowledgementListVO ackListVO)

    throws DAOException

  {

    logger.info("Entering -------fetchPrnInvoiceData------Method");

    ArrayList prnAckInvoiceList = null;

    LoggableStatement pst = null;

    ResultSet rs = null;

    Connection con = null;

    String query = null;
    AcknowledgementVO ackVO = null;

    boolean shippingNoFlag = false;

    boolean shippingDateFlag = false;

    boolean portCodeFlag = false;

    boolean formNoFlag = false;

    boolean paySeqNoFlag = false;

    boolean dateFromFlag = false;

    boolean dateToFlag = false;

    int setValue = 0;

    try

    {

      con = DBConnectionUtility.getConnection();

      prnAckInvoiceList = new ArrayList();

      CommonMethods commonMethods = null;

      String PRN_INV_QUERY = "SELECT EPI.SHIPPINGBILLNO,EPI.SHIPPINGBILLDATE,EPI.PORTCODE,EPI.FORMNO,EPI.INVOICESERIALNO,EPI.INVOICENO, TO_CHAR(EPI.INVOICEDATE, 'dd-mm-yyyy') as INVOICEDATE, EPI.ACCOUNTNUMBER,to_char(EPI.BANK_CHG,'999,999,999,999,999.99') as BANKINGCHARGESAMT, to_char(EPI.FOBAMT,'999,999,999,999,999.99') as FOBAMT,to_char(EPI.FOBAMTIC,'999,999,999,999,999.99') as FOBAMTIC, to_char(EPI.FREIGHTAMT,'999,999,999,999,999.99') as FREIGHTAMT,to_char(EPI.FREIGHTAMTIC,'999,999,999,999,999.99') as FREIGHTAMTIC, to_char(EPI.INSURANCEAMT,'999,999,999,999,999.99') as INSURANCEAMT,to_char(EPI.INSURANCEAMTIC,'999,999,999,999,999.99') as INSURANCEAMTIC, EPI.CLOSEOFBILLINDICATOR,EPI.REMITTERNAME,EPI.REMITTERCOUNTRY, EPI.INVOICEERRORCODES FROM ETT_PRN_ACK_FILES EPA,ETT_PRN_SHP_INV_ACK_STG EPI WHERE EPA.FILE_NO = EPI.FILE_NO  ";

      commonMethods = new CommonMethods();

      String shippingNo = ackListVO.getShippingBillNo();

      if (!commonMethods.isNull(shippingNo))

      {

        PRN_INV_QUERY = PRN_INV_QUERY + "AND EPI.SHIPPINGBILLNO =?";

        shippingNoFlag = true;

      }

      String shippingDate = commonMethods.getEmptyIfNull(ackListVO.getShippingBillDate()).trim();

      if (!commonMethods.isNull(shippingDate))

      {

        PRN_INV_QUERY = PRN_INV_QUERY + "AND TO_CHAR(TO_DATE(EPI.SHIPPINGBILLDATE,'DDMMYYYY'),'DD-MM-YY') = ?";

        shippingDateFlag = true;

      }

      String portCode = ackListVO.getPortCode();

      if (!commonMethods.isNull(portCode))

      {

        PRN_INV_QUERY = PRN_INV_QUERY + "AND EPI.PORTCODE =  ?";

        portCodeFlag = true;

      }

      String formNo = ackListVO.getFormNo();

      if (!commonMethods.isNull(formNo))

      {

        PRN_INV_QUERY = PRN_INV_QUERY + "AND EPI.FORMNO = ?";

        formNoFlag = true;

      }

      String paySeqNo = ackListVO.getPaySeqNo();

      if (!commonMethods.isNull(paySeqNo))

      {

        PRN_INV_QUERY = PRN_INV_QUERY + "AND EPI.PAY_SEQNO =  ?";

        paySeqNoFlag = true;

      }

      String dateFrom = ackListVO.getDateFrom();

      String dateTo = ackListVO.getDateTo();

      if ((!commonMethods.isNull(dateFrom)) && (!commonMethods.isNull(dateTo)))

      {

        PRN_INV_QUERY = PRN_INV_QUERY + " AND EPA.PROCESSED_DATE BETWEEN TO_DATE(?,'DD-MM-YY') " + " AND TO_DATE(?,'DD-MM-YY')";

        dateFromFlag = true;

        dateToFlag = true;

      }

      else

      {

        if (!commonMethods.isNull(dateFrom))

        {

          PRN_INV_QUERY = PRN_INV_QUERY + " AND EPA.PROCESSED_DATE >= TO_DATE(?,'DD-MM-YY')";

          dateFromFlag = true;

        }

        if (!commonMethods.isNull(dateTo))

        {

          PRN_INV_QUERY = PRN_INV_QUERY + " AND EPA.PROCESSED_DATE <= TO_DATE(?,'DD-MM-YY')";

          dateToFlag = true;

        }

      }

      pst = new LoggableStatement(con, PRN_INV_QUERY);

      if (shippingNoFlag) {

        pst.setString(++setValue, shippingNo.trim());

      }

      if (shippingDateFlag) {

        pst.setString(++setValue, shippingDate.trim());

      }

      if (portCodeFlag) {

        pst.setString(++setValue, portCode.trim());

      }

      if (formNoFlag) {

        pst.setString(++setValue, formNo.trim());

      }

      if (paySeqNoFlag) {

        pst.setString(++setValue, paySeqNo.trim());

      }

      if (dateFromFlag) {

        pst.setString(++setValue, dateFrom.trim());

      }

      if (dateToFlag) {

        pst.setString(++setValue, dateTo.trim());

      }

      logger.info("Entering -------fetchPrnInvoiceData------Mquery" + pst.getQueryString());

      rs = pst.executeQuery();

      while (rs.next())

      {

        ackVO = new AcknowledgementVO();

        ackVO.setShippingBillNo(rs.getString("SHIPPINGBILLNO"));

        ackVO.setShippingBillDate(rs.getString("SHIPPINGBILLDATE"));

        ackVO.setPortCode(rs.getString("PORTCODE"));

        ackVO.setFormNo(rs.getString("FORMNO"));

        ackVO.setInvSerialNo(rs.getString("INVOICESERIALNO"));

        ackVO.setInvNo(rs.getString("INVOICENO"));

        ackVO.setInvDate(rs.getString("INVOICEDATE"));

        ackVO.setAccNo(rs.getString("ACCOUNTNUMBER"));

        ackVO.setBankAmt(rs.getString("BANKINGCHARGESAMT"));

        ackVO.setFobAmt(rs.getString("FOBAMT"));

        ackVO.setFobAmtIc(rs.getString("FOBAMTIC"));

        ackVO.setFreightAmt(rs.getString("FREIGHTAMT"));

        ackVO.setFreightIc(rs.getString("FREIGHTAMTIC"));

        ackVO.setInsAmt(rs.getString("INSURANCEAMT"));

        ackVO.setInsAmtIc(rs.getString("INSURANCEAMTIC"));

        ackVO.setCloseInd(rs.getString("CLOSEOFBILLINDICATOR"));

        ackVO.setRemName(rs.getString("REMITTERNAME"));

        ackVO.setRemCountry(rs.getString("REMITTERCOUNTRY"));

        String errorCode = rs.getString("INVOICEERRORCODES");

        if ((errorCode != null) && (!errorCode.equalsIgnoreCase("SUCCESS"))) {

          errorCode = getErrorDescBasedOnErrorCode(errorCode, con);

        }

        ackVO.setErrorDes(errorCode);

        ackVO.setInvErrorcode(errorCode);

        prnAckInvoiceList.add(ackVO);

      }

    }

    catch (Exception exception)

    {

      logger.info("Entering -------fetchPrnInvoiceData------exception" + exception);

      exception.printStackTrace();

      throwDAOException(exception);

    }

    finally

    {

      DBConnectionUtility.surrenderDB(con, pst, rs);

    }

    logger.info("Exiting Method");

    return prnAckInvoiceList;

  }

  public AcknowledgementListVO getAckWSNReportList(AcknowledgementListVO ackListVO)

    throws DAOException

  {

    logger.info("Entering-------getAckWSNReportList-------- Method");

    ArrayList wsnAckShippingList = null;

    ArrayList wsnAckInvoiceList = null;

    try

    {

      wsnAckShippingList = fetchWSNShippingData(ackListVO);

      if (!wsnAckShippingList.isEmpty()) {

        ackListVO.setPrnShippingList(wsnAckShippingList);

      }

      wsnAckInvoiceList = fetchWSNInvoiceData(ackListVO);

      if (!wsnAckInvoiceList.isEmpty()) {

        ackListVO.setPrnInvoiceList(wsnAckInvoiceList);

      }

    }

    catch (Exception exception)

    {

      logger.info("Entering-------getAckWSNReportList-------- exception" + exception);

      throwDAOException(exception);

    }

    logger.info("Exiting Method");

    return ackListVO;

  }

  public ArrayList<AcknowledgementVO> fetchWSNShippingData(AcknowledgementListVO ackListVO)

    throws DAOException

  {

    logger.info("Entering ---------fetchWSNShippingData----------Method");

    ArrayList wsnAckShippingList = null;
    LoggableStatement pst = null;

    ResultSet rs = null;

    Connection con = null;

    String query = null;

    AcknowledgementVO ackVO = null;

    boolean shippingNoFlag = false;

    boolean shippingDateFlag = false;

    boolean portCodeFlag = false;

    boolean formNoFlag = false;

    boolean dateFromFlag = false;

    boolean dateToFlag = false;

    int setValue = 0;

    try

    {

      con = DBConnectionUtility.getConnection();

      wsnAckShippingList = new ArrayList();

      CommonMethods commonMethods = null;

      String PRN_SHP_QUERY = "SELECT EWS.SHIPPINGBILLNO,TO_CHAR(TO_DATE(EWS.SHIPPINGBILLDATE, 'ddmmyyyy'),'dd-mm-yyyy') as SHIPPINGBILLDATE, EWS.PORTCODE,EWS.FORMNO,EWS.RECORDINDICATOR,EWS.EXPORTTYPE,TO_CHAR(EWS.LEODATE, 'dd-mm-yyyy') as LEODATE,EWS.ADCODE, EWS.ERRORCODES FROM ETT_WSN_ACK_FILES EWT,ETT_WSN_SHP_ACK_STG EWS WHERE EWT.FILE_NO = EWS.FILE_NO ";

      commonMethods = new CommonMethods();

      String shippingNo = ackListVO.getShippingBillNo();

      if (!commonMethods.isNull(shippingNo))

      {

        PRN_SHP_QUERY = PRN_SHP_QUERY + "AND EWS.SHIPPINGBILLNO =?";

        shippingNoFlag = true;

      }

      String shippingDate = commonMethods.getEmptyIfNull(ackListVO.getShippingBillDate()).trim();

      if (!commonMethods.isNull(shippingDate))

      {

        PRN_SHP_QUERY = PRN_SHP_QUERY + "AND TO_CHAR(TO_DATE(EWS.SHIPPINGBILLDATE,'DDMMYYYY'),'DD-MM-YY') = ?";

        shippingDateFlag = true;

      }

      String portCode = ackListVO.getPortCode();

      if (!commonMethods.isNull(portCode))

      {

        PRN_SHP_QUERY = PRN_SHP_QUERY + "AND EWS.PORTCODE =  ?";

        portCodeFlag = true;

      }

      String formNo = ackListVO.getFormNo();

      if (!commonMethods.isNull(formNo))

      {

        PRN_SHP_QUERY = PRN_SHP_QUERY + "AND EWS.FORMNO = ?";

        formNoFlag = true;

      }

      String dateFrom = ackListVO.getDateFrom();

      String dateTo = ackListVO.getDateTo();

      if ((!commonMethods.isNull(dateFrom)) && (!commonMethods.isNull(dateTo)))

      {

        PRN_SHP_QUERY = PRN_SHP_QUERY + " AND EWT.PROCESSED_DATE BETWEEN TO_DATE(?,'DD-MM-YY') " + " AND TO_DATE(?,'DD-MM-YY')";

        dateFromFlag = true;

        dateToFlag = true;

      }

      else

      {

        if (!commonMethods.isNull(dateFrom))

        {

          PRN_SHP_QUERY = PRN_SHP_QUERY + " AND EWT.PROCESSED_DATE >= TO_DATE(?,'DD-MM-YY')";

          dateFromFlag = true;

        }

        if (!commonMethods.isNull(dateTo))

        {

          PRN_SHP_QUERY = PRN_SHP_QUERY + " AND EWT.PROCESSED_DATE <= TO_DATE(?,'DD-MM-YY')";

          dateToFlag = true;

        }

      }

      pst = new LoggableStatement(con, PRN_SHP_QUERY);

      if (shippingNoFlag) {

        pst.setString(++setValue, shippingNo.trim());

      }

      if (shippingDateFlag) {

        pst.setString(++setValue, shippingDate.trim());

      }

      if (portCodeFlag) {

        pst.setString(++setValue, portCode.trim());

      }

      if (formNoFlag) {

        pst.setString(++setValue, formNo.trim());

      }

      if (dateFromFlag) {

        pst.setString(++setValue, dateFrom.trim());

      }

      if (dateToFlag) {

        pst.setString(++setValue, dateTo.trim());

      }

      logger.info("Entering ---------fetchWSNShippingData--------exception" + pst.getQueryString());

      rs = pst.executeQuery();

      while (rs.next())

      {

        ackVO = new AcknowledgementVO();

        ackVO.setShippingBillNo(rs.getString("SHIPPINGBILLNO"));

        ackVO.setShippingBillDate(rs.getString("SHIPPINGBILLDATE"));

        ackVO.setPortCode(rs.getString("PORTCODE"));

        ackVO.setFormNo(rs.getString("FORMNO"));

        ackVO.setExportType(rs.getString("EXPORTTYPE"));

        ackVO.setRecordInd(rs.getString("RECORDINDICATOR"));

        ackVO.setLeoDate(rs.getString("LEODATE"));

        ackVO.setExAdcode(rs.getString("ADCODE"));

        String errorCode = rs.getString("ERRORCODES");

        if ((errorCode != null) && (!errorCode.equalsIgnoreCase("SUCCESS"))) {

          errorCode = getErrorDescBasedOnErrorCode(errorCode, con);

        }

        ackVO.setErrorDes(errorCode);

        wsnAckShippingList.add(ackVO);

      }

    }

    catch (Exception exception)

    {

      logger.info("Entering ---------fetchWSNShippingData----------exception" + exception);

      throwDAOException(exception);

    }

    finally

    {

      DBConnectionUtility.surrenderDB(con, pst, rs);

    }

    logger.info("Exiting Method");

    return wsnAckShippingList;

  }

  public ArrayList<AcknowledgementVO> fetchWSNInvoiceData(AcknowledgementListVO ackListVO)

    throws DAOException

  {

    logger.info("Entering-------fetchWSNInvoiceData--------- Method");

    ArrayList wsnAckInvoiceList = null;

    LoggableStatement pst = null;

    ResultSet rs = null;

    Connection con = null;

    String query = null;

    AcknowledgementVO ackVO = null;

    boolean shippingNoFlag = false;

    boolean shippingDateFlag = false;

    boolean portCodeFlag = false;

    boolean dateFromFlag = false;

    boolean dateToFlag = false;

    int setValue = 0;

    try

    {

      con = DBConnectionUtility.getConnection();

      wsnAckInvoiceList = new ArrayList();

      CommonMethods commonMethods = null;

      String WSN_INV_QUERY = "SELECT EWI.SHIPPINGBILLNO,EWI.SHIPPINGBILLDATE,EWI.PORTCODE,EWI.INVOICESERIALNO,EWI.INVOICENO, TO_CHAR(EWI.INVOICEDATE, 'dd-mm-yyyy') as INVOICEDATE,EWI.SHIPMENTINDICATOR, EWI.WRITEOFFINDICATOR,to_char(EWI.WRITEOFFAMOUNT,'999,999,999,999,999.99')as WRITEOFFAMOUNT,TO_CHAR(EWI.WRITEOFFDATE, 'dd-mm-yyyy') as WRITEOFFDATE, EWI.CLOSEOFBILLINDICATOR,EWI.INVOICEERRORCODES FROM ETT_WSN_ACK_FILES EWT,ETT_WSN_SHP_INV_ACK_STG EWI WHERE EWT.FILE_NO = EWI.FILE_NO  ";

      commonMethods = new CommonMethods();

      String shippingNo = ackListVO.getShippingBillNo();

      if (!commonMethods.isNull(shippingNo))

      {

        WSN_INV_QUERY = WSN_INV_QUERY + "AND EWI.SHIPPINGBILLNO =?";

        shippingNoFlag = true;

      }

      String shippingDate = commonMethods.getEmptyIfNull(ackListVO.getShippingBillDate()).trim();

      if (!commonMethods.isNull(shippingDate))

      {

        WSN_INV_QUERY = WSN_INV_QUERY + "AND TO_CHAR(TO_DATE(EWI.SHIPPINGBILLDATE,'DDMMYYYY'),'DD-MM-YY') =?";

        shippingDateFlag = true;

      }

      String portCode = ackListVO.getPortCode();

      if (!commonMethods.isNull(portCode))

      {

        WSN_INV_QUERY = WSN_INV_QUERY + "AND EWI.PORTCODE =  ?";

        portCodeFlag = true;

      }

      String dateFrom = ackListVO.getDateFrom();
      String dateTo = ackListVO.getDateTo();

      if ((!commonMethods.isNull(dateFrom)) && (!commonMethods.isNull(dateTo)))

      {

        WSN_INV_QUERY = WSN_INV_QUERY + " AND EWT.PROCESSED_DATE BETWEEN TO_DATE(?,'DD-MM-YY') " + " AND TO_DATE(?,'DD-MM-YY')";

        dateFromFlag = true;

        dateToFlag = true;

      }

      else

      {

        if (!commonMethods.isNull(dateFrom))

        {

          WSN_INV_QUERY = WSN_INV_QUERY + " AND EWT.PROCESSED_DATE >= TO_DATE(?,'DD-MM-YY')";

          dateFromFlag = true;

        }

        if (!commonMethods.isNull(dateTo))

        {

          WSN_INV_QUERY = WSN_INV_QUERY + " AND EWT.PROCESSED_DATE <= TO_DATE(?,'DD-MM-YY')";

          dateFromFlag = true;

        }

      }

      pst = new LoggableStatement(con, WSN_INV_QUERY);

      if (shippingNoFlag) {

        pst.setString(++setValue, shippingNo.trim());

      }

      if (shippingDateFlag) {

        pst.setString(++setValue, shippingDate.trim());

      }

      if (portCodeFlag) {

        pst.setString(++setValue, portCode.trim());

      }

      if (dateFromFlag) {

        pst.setString(++setValue, dateFrom.trim());

      }

      if (dateToFlag) {

        pst.setString(++setValue, dateTo.trim());

      }

      logger.info("Entering-------fetchWSNInvoiceData---------query" + pst.getQueryString());

      rs = pst.executeQuery();

      while (rs.next())

      {

        ackVO = new AcknowledgementVO();

        ackVO.setShippingBillNo(rs.getString("SHIPPINGBILLNO"));

        ackVO.setShippingBillDate(rs.getString("SHIPPINGBILLDATE"));

        ackVO.setPortCode(rs.getString("PORTCODE"));

        ackVO.setInvSerialNo(rs.getString("INVOICESERIALNO"));

        ackVO.setInvNo(rs.getString("INVOICENO"));

        ackVO.setInvDate(rs.getString("INVOICEDATE"));

        ackVO.setShpInd(rs.getString("SHIPMENTINDICATOR"));

        ackVO.setWriteOffInd(rs.getString("WRITEOFFINDICATOR"));

        ackVO.setWriteOffAmt(rs.getString("WRITEOFFAMOUNT"));

        ackVO.setWriteOffDate(rs.getString("WRITEOFFDATE"));

        ackVO.setCloseInd(rs.getString("CLOSEOFBILLINDICATOR"));

        String errorCode = rs.getString("INVOICEERRORCODES");

        if ((errorCode != null) && (!errorCode.equalsIgnoreCase("SUCCESS"))) {

          errorCode = getErrorDescBasedOnErrorCode(errorCode, con);

        }

        ackVO.setErrorDes(errorCode);

        ackVO.setInvErrorcode(errorCode);

        wsnAckInvoiceList.add(ackVO);

      }

    }

    catch (Exception exception)

    {

      logger.info("Entering-------fetchWSNInvoiceData----exception-----" + exception);

      throwDAOException(exception);

    }

    finally

    {

      DBConnectionUtility.surrenderDB(con, pst, rs);

    }

    logger.info("Exiting Method");

    return wsnAckInvoiceList;

  }

  public String getErrorDesc(String errorCode)

    throws DAOException

  {

    logger.info("Entering Method");

    LoggableStatement pst = null;

    ResultSet rs = null;

    Connection con = null;

    String query = null;

    String errorMsg = null;

    String errMsg = null;

    try

    {

      con = DBConnectionUtility.getConnection();

      String[] errCode = errorCode.split(",");

      if (errCode != null) {

        for (int i = 0; i < errCode.length; i++)

        {

          query = "SELECT ERROR_MSG FROM AD_ERROR_CODES WHERE ERROR_CODES = ?";

          pst = new LoggableStatement(con, query);

          pst.setString(1, errCode[i]);

          rs = pst.executeQuery();

          while (rs.next())

          {

            errMsg = rs.getString("ERROR_MSG");

            errMsg = errMsg.replace(".", "");

          }

          if (errorMsg != null) {

            errorMsg = errorMsg + "," + errMsg;

          } else if (errorMsg == null) {

            errorMsg = errMsg;

          }

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

    return errorMsg;

  }

  public String getErrorDescBasedOnErrorCode(String errorCode, Connection con)

  {

    LoggableStatement pst1 = null;

    ResultSet rs1 = null;

    String errorDesc = "";

    String errorDesc1 = "";

    try

    {

      String sInParam = "";

      if (errorCode.contains("SUCCESS"))

      {

        sInParam = "''";

        errorDesc1 = errorCode;

      }

      else if (errorCode.contains(","))

      {

        String[] splitErrorCodes = errorCode.split(",");

        sInParam = "";

        for (int j = 0; j < splitErrorCodes.length; j++) {

          if (sInParam.equals("")) {

            sInParam = "'" + splitErrorCodes[j].trim() + "'";

          } else {

            sInParam = sInParam + ",'" + splitErrorCodes[j].trim() + "'";

          }

        }

      }

      else

      {

        sInParam = "'" + errorCode + "'";

      }

      String sEerrDescQuery = "SELECT ERRCO||' - '||ERRMSG FROM (Select Trim(ERROR_CODE) ERRCO, Trim(ERROR_MSG) ERRMSG From ETT_EDPMS_ERROR_CODES where trim(ERROR_CODE) in (?))";

      pst1 = new LoggableStatement(con, sEerrDescQuery);

      pst1.setString(1, sInParam);

      rs1 = pst1.executeQuery();

      while (rs1.next())

      {

        errorDesc = rs1.getString(1);

        if (errorDesc1.isEmpty()) {

          errorDesc1 = errorDesc;

        } else {

          errorDesc1 = errorDesc1 + ", " + errorDesc;

        }

      }

      errorCode = errorDesc1;

    }

    catch (Exception e)

    {

      logger.info("Exception in getErrorDescBasedOnErrorCode - " + e.getMessage());

      e.printStackTrace();

    }

    finally

    {

      DBConnectionUtility.surrenderDB(null, pst1, rs1);

    }

    return errorCode;

  }

}