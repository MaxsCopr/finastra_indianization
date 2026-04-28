package in.co.localization.dao.localization;

import in.co.localization.dao.AbstractDAO;
import in.co.localization.dao.exception.DAOException;
import in.co.localization.utility.ActionConstantsQuery;
import in.co.localization.utility.CommonMethods;
import in.co.localization.utility.DBConnectionUtility;
import in.co.localization.utility.LoggableStatement;
import in.co.localization.vo.localization.AcknowledgementListVO;
import in.co.localization.vo.localization.InvoiceDetailsVO;
import in.co.localization.vo.localization.ShippingDetailsVO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import org.apache.log4j.Logger;

public class EDPMSUtilizeSearchDAO
  extends AbstractDAO
  implements ActionConstantsQuery
{
  static EDPMSUtilizeSearchDAO dao;
  private static Logger logger = Logger.getLogger(EDPMSUtilizeSearchDAO.class
    .getName());
 
  public static EDPMSUtilizeSearchDAO getDAO()
  {
    if (dao == null) {
      dao = new EDPMSUtilizeSearchDAO();
    }
    return dao;
  }
 
  public AcknowledgementListVO fetchGRData(AcknowledgementListVO ackListVO)
    throws DAOException
  {
    logger.info("Entering Method");
    ArrayList shpList = null;
    ArrayList invList = null;
    try
    {
      shpList = fetchShippingData(ackListVO);
      logger.info("fetchShippingData-----------------done");
      if (!shpList.isEmpty()) {
        ackListVO.setShippingList(shpList);
      }
      invList = fetchInvoiceData(ackListVO);
     
      logger.info("fetchInvoiceData-----------------done");
      if (!invList.isEmpty()) {
        ackListVO.setInvoiceList(invList);
      }
    }
    catch (Exception exception)
    {
      logger.info("fetchGRData-------------exception");
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return ackListVO;
  }
 
  public ArrayList<ShippingDetailsVO> fetchShippingData(AcknowledgementListVO ackListVO)
    throws DAOException
  {
    logger.info("Entering------------fetchShippingData----------- Method");
    ArrayList shippingList = null;
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    String query = null;
    ShippingDetailsVO shpVO = null;
    boolean billRefNoFlag = false;
    boolean eventNoFlag = false;
    boolean custNameFlag = false;
    boolean shippingNoFlag = false;
    boolean shippingDateFlag = false;
    boolean portCodeFlag = false;
    boolean ieCodeFlag = false;
    boolean formNoFlag = false;
    int setValue = 0;
    try
    {
      shippingList = new ArrayList();
      con = DBConnectionUtility.getConnection();
     
      CommonMethods commonMethods = null;
     
      String SHP_QUERY = "SELECT BILLREFNO,EVENTREFNO,CUSTNAME,SHIPBILLNO,TO_CHAR(TO_DATE(SHIPBILLDATE, 'ddmmyyyy'),'DD-MM-YYYY') as SHIPBILLDATE, PORTCODE,FORMNO,CUSTSERIALNO,IECODE,ADCODE, EXPORTAGENCY,TO_CHAR(LEODATE, 'DD-MM-YYYY') as LEODATE,EXPORTTYPE,RODSENT,PENSENT,PRNSENT,GRTYPE,COUNTRYOFDEST FROM ETT_GR_SHP_TBL WHERE PORTCODE = PORTCODE ";
     
      commonMethods = new CommonMethods();
     
      String billRefNo = ackListVO.getBillRefNo();
      if (!commonMethods.isNull(billRefNo))
      {
        SHP_QUERY = SHP_QUERY + "AND BILLREFNO =?";
        billRefNoFlag = true;
      }
      String eventNo = ackListVO.getEventRefNo();
      if (!commonMethods.isNull(eventNo))
      {
        SHP_QUERY = SHP_QUERY + "AND EVENTREFNO =?";
        eventNoFlag = true;
      }
      String custName = ackListVO.getCustCIFNo();
      if (!commonMethods.isNull(custName))
      {
        SHP_QUERY = SHP_QUERY + "AND CUSTCIFNO =?";
        custNameFlag = true;
      }
      String shippingNo = ackListVO.getShippingBillNo();
      if (!commonMethods.isNull(shippingNo))
      {
        SHP_QUERY = SHP_QUERY + "AND SHIPBILLNO =?";
        shippingNoFlag = true;
      }
      String shippingDate = commonMethods.getEmptyIfNull(ackListVO.getShippingBillDate()).trim();
      if (!commonMethods.isNull(shippingDate))
      {
        SHP_QUERY = SHP_QUERY + "AND TO_CHAR(TO_DATE(SHIPBILLDATE,'DDMMYYYY'),'DD-MM-YY') = ?";
        shippingDateFlag = true;
      }
      String portCode = ackListVO.getPortCode();
      if (!commonMethods.isNull(portCode))
      {
        SHP_QUERY = SHP_QUERY + "AND PORTCODE = ?";
        portCodeFlag = true;
      }
      String ieCode = ackListVO.getIeCode();
      if (!commonMethods.isNull(ieCode))
      {
        SHP_QUERY = SHP_QUERY + "AND IECODE =  ?";
        ieCodeFlag = true;
      }
      String formNo = ackListVO.getFormNo();
      if (!commonMethods.isNull(formNo))
      {
        SHP_QUERY = SHP_QUERY + "AND FORMNO =  ?";
        formNoFlag = true;
      }
      pst = new LoggableStatement(con, SHP_QUERY);
      if (billRefNoFlag) {
        pst.setString(++setValue, billRefNo.trim());
      }
      if (eventNoFlag) {
        pst.setString(++setValue, eventNo.trim());
      }
      if (custNameFlag) {
        pst.setString(++setValue, custName.trim());
      }
      if (shippingNoFlag) {
        pst.setString(++setValue, shippingNo.trim());
      }
      if (shippingDateFlag) {
        pst.setString(++setValue, shippingDate.trim());
      }
      if (portCodeFlag) {
        pst.setString(++setValue, portCode.trim());
      }
      if (ieCodeFlag) {
        pst.setString(++setValue, ieCode.trim());
      }
      if (formNoFlag) {
        pst.setString(++setValue, formNo.trim());
      }
      logger.info("EDPMS Query -------------" + pst.getQueryString());
      rs = pst.executeQuery();
      while (rs.next())
      {
        shpVO = new ShippingDetailsVO();
        shpVO.setBillRefNo(rs.getString("BILLREFNO"));
        shpVO.setSelectedEventRifNo(rs.getString("EVENTREFNO"));
        shpVO.setCustName(rs.getString("CUSTNAME"));
        shpVO.setShippingBillNo(rs.getString("SHIPBILLNO"));
        shpVO.setShippingBillDate(rs.getString("SHIPBILLDATE"));
        shpVO.setPortCode(rs.getString("PORTCODE"));
        shpVO.setFormNo(rs.getString("FORMNO"));
        shpVO.setLeoDate(rs.getString("LEODATE"));
        shpVO.setAdCode(rs.getString("ADCODE"));
        shpVO.setIeCode(rs.getString("IECODE"));
        shpVO.setCustSerialNo(rs.getString("CUSTSERIALNO"));
        shpVO.setExportAgency(rs.getString("EXPORTAGENCY"));
        shpVO.setExportType(rs.getString("EXPORTTYPE"));
        shpVO.setGrType(rs.getString("GRTYPE"));
        shpVO.setRodSent(rs.getString("RODSENT"));
        shpVO.setPenSent(rs.getString("PENSENT"));
        shpVO.setPrnSent(rs.getString("PRNSENT"));
        shpVO.setCountryOfDest(rs.getString("COUNTRYOFDEST"));
        shippingList.add(shpVO);
      }
    }
    catch (Exception exception)
    {
      logger.info("Entering------------fetchShippingData----------- Mexception" + exception);
      throwDAOException(exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    logger.info("Exiting Method");
    return shippingList;
  }
 
  public ArrayList<InvoiceDetailsVO> fetchInvoiceData(AcknowledgementListVO ackListVO)
    throws DAOException
  {
    logger.info("Entering-------------fetchInvoiceData---------------- Method");
    ArrayList invoiceList = null;
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    String query = null;
    InvoiceDetailsVO invoiceVO = null;
    ShippingDetailsVO shpVO = null;
    CommonMethods commonMethods = null;
    ArrayList shippingBillList = null;
    boolean billRefNoFlag = false;
    boolean eventNoFlag = false;
    boolean custNameFlag = false;
    boolean shippingNoFlag = false;
    boolean shippingDateFlag = false;
    boolean portCodeFlag = false;
    boolean ieCodeFlag = false;
    boolean formNoFlag = false;
    int setValue = 0;
    try
    {
      invoiceList = new ArrayList();
      commonMethods = new CommonMethods();
      shippingBillList = new ArrayList();
      con = DBConnectionUtility.getConnection();
     




      String SHP_QUERY = "SELECT BILLREFNO,EVENTREFNO,CUSTNAME,SHIPBILLNO,TO_CHAR(TO_DATE(SHIPBILLDATE, 'ddmmyyyy'),'DD-MM-YYYY') as SHIPBILLDATE, PORTCODE,FORMNO,CUSTSERIALNO,IECODE,ADCODE, EXPORTAGENCY,TO_CHAR(LEODATE, 'DD-MM-YYYY') as LEODATE,EXPORTTYPE,RODSENT,PENSENT,PRNSENT,GRTYPE,COUNTRYOFDEST FROM ETT_GR_SHP_TBL WHERE PORTCODE = PORTCODE ";
     
      commonMethods = new CommonMethods();
     
      String billRefNo = ackListVO.getBillRefNo();
      if (!commonMethods.isNull(billRefNo))
      {
        SHP_QUERY = SHP_QUERY + " AND BILLREFNO =?";
        billRefNoFlag = true;
      }
      String eventNo = ackListVO.getEventRefNo();
      if (!commonMethods.isNull(eventNo))
      {
        SHP_QUERY = SHP_QUERY + " AND EVENTREFNO =?";
        eventNoFlag = true;
      }
      String custName = ackListVO.getCustCIFNo();
      if (!commonMethods.isNull(custName))
      {
        SHP_QUERY = SHP_QUERY + " AND CUSTCIFNO =?";
        custNameFlag = true;
      }
      String shippingNo = ackListVO.getShippingBillNo();
      if (!commonMethods.isNull(shippingNo))
      {
        SHP_QUERY = SHP_QUERY + " AND SHIPBILLNO =?";
        shippingNoFlag = true;
      }
      String shippingDate = commonMethods.getEmptyIfNull(ackListVO.getShippingBillDate()).trim();
      if (!commonMethods.isNull(shippingDate))
      {
        SHP_QUERY = SHP_QUERY + " AND TO_CHAR(TO_DATE(SHIPBILLDATE,'DDMMYYYY'),'DD-MM-YY') = ?";
        shippingDateFlag = true;
      }
      String portCode = ackListVO.getPortCode();
      if (!commonMethods.isNull(portCode))
      {
        SHP_QUERY = SHP_QUERY + " AND PORTCODE = ?";
        portCodeFlag = true;
      }
      String ieCode = ackListVO.getIeCode();
      if (!commonMethods.isNull(ieCode))
      {
        SHP_QUERY = SHP_QUERY + " AND IECODE =  ?";
        ieCodeFlag = true;
      }
      String formNo = ackListVO.getFormNo();
      if (!commonMethods.isNull(formNo))
      {
        SHP_QUERY = SHP_QUERY + " AND FORMNO =  ?";
        formNoFlag = true;
      }
      pst = new LoggableStatement(con, SHP_QUERY);
      if (billRefNoFlag) {
        pst.setString(++setValue, billRefNo.trim());
      }
      if (eventNoFlag) {
        pst.setString(++setValue, eventNo.trim());
      }
      if (custNameFlag) {
        pst.setString(++setValue, custName.trim());
      }
      if (shippingNoFlag) {
        pst.setString(++setValue, shippingNo.trim());
      }
      if (shippingDateFlag) {
        pst.setString(++setValue, shippingDate.trim());
      }
      if (portCodeFlag) {
        pst.setString(++setValue, portCode.trim());
      }
      if (ieCodeFlag) {
        pst.setString(++setValue, ieCode.trim());
      }
      if (formNoFlag) {
        pst.setString(++setValue, formNo.trim());
      }
      rs = pst.executeQuery();
      logger.info("Entering-------------fetchInvoiceData---------query----" + pst.getQueryString());
      while (rs.next())
      {
        shpVO = new ShippingDetailsVO();
        shpVO.setBillRefNo(commonMethods.getEmptyIfNull(rs.getString("BILLREFNO")).trim());
        shpVO.setSelectedEventRifNo(commonMethods.getEmptyIfNull(rs.getString("EVENTREFNO")).trim());
        shpVO.setShippingBillNo(commonMethods.getEmptyIfNull(rs.getString("SHIPBILLNO")).trim());
        String tempDate = commonMethods.getEmptyIfNull(rs.getString("SHIPBILLDATE")).replace("-", "");
        shpVO.setShippingBillDate(tempDate);
        shpVO.setPortCode(commonMethods.getEmptyIfNull(rs.getString("PORTCODE")).trim());
        shpVO.setFormNo(commonMethods.getEmptyIfNull(rs.getString("FORMNO")).trim());
        shippingBillList.add(shpVO);
      }
      for (int i = 0; i < shippingBillList.size(); i++)
      {
        ShippingDetailsVO shpBillVO = (ShippingDetailsVO)shippingBillList.get(i);
        String billRefNo1 = shpBillVO.getBillRefNo();
        String eventRefNo = shpBillVO.getSelectedEventRifNo();
        String shipBillNo = shpBillVO.getShippingBillNo();
        String shipBillDate = shpBillVO.getShippingBillDate();
        String portCode1 = shpBillVO.getPortCode();
        String formNo1 = shpBillVO.getFormNo();
       




        String INV_QUERY = "SELECT  BILLREFNO,EVENTREFNO,SHIPBILLNO,SHIPBILLDATE,PORTCODE,INVSERIALNO,INVNO,INVDATE,FOBCURRCODE,FOBAMT,FRIEGHTCURRCODE,FRIEGHTAMT,INSCURRCODE,INSAMT,COMMCURRCODE,COMMAMT,DEDCURRCODE,DEDAMT,DISCURRCODE,DISAMT,PKGCURRCODE,PKGAMT FROM ETT_GR_INV_TBL";
        if ((!commonMethods.isNull(billRefNo1)) && (!commonMethods.isNull(eventRefNo)) && (!commonMethods.isNull(shipBillNo)) &&
          (!commonMethods.isNull(shipBillDate)) && (!commonMethods.isNull(portCode1)))
        {
          logger.info("-----" + INV_QUERY);
          INV_QUERY = INV_QUERY + " WHERE BILLREFNO = ?  AND EVENTREFNO =? AND SHIPBILLNO = ?  AND SHIPBILLDATE = ? AND PORTCODE =? ";
        }
        else if ((!commonMethods.isNull(billRefNo1)) && (!commonMethods.isNull(eventRefNo)) && (!commonMethods.isNull(formNo1)) &&
          (!commonMethods.isNull(shipBillDate)) && (!commonMethods.isNull(portCode1)))
        {
          logger.info("2-----" + INV_QUERY);
          INV_QUERY = INV_QUERY + " WHERE BILLREFNO = ? AND EVENTREFNO =? AND FORMNO = ? AND SHIPBILLDATE = ? AND PORTCODE =?";
        }
        pst = new LoggableStatement(con, INV_QUERY);
       
        logger.info("query----" + INV_QUERY);
        if ((!commonMethods.isNull(billRefNo1)) && (!commonMethods.isNull(eventRefNo)) && (!commonMethods.isNull(shipBillNo)) &&
          (!commonMethods.isNull(shipBillDate)) && (!commonMethods.isNull(portCode1)))
        {
          pst.setString(1, billRefNo1);
          pst.setString(2, eventRefNo);
          pst.setString(3, shipBillNo);
          pst.setString(4, shipBillDate);
          pst.setString(5, portCode1);
          logger.info("-----" + pst.getQueryString());
        }
        else if ((!commonMethods.isNull(billRefNo1)) && (!commonMethods.isNull(eventRefNo)) && (!commonMethods.isNull(formNo1)) &&
          (!commonMethods.isNull(shipBillDate)) && (!commonMethods.isNull(portCode1)))
        {
          pst.setString(1, billRefNo1);
          pst.setString(2, eventRefNo);
          pst.setString(3, formNo1);
          pst.setString(4, shipBillDate);
          pst.setString(5, portCode1);
          logger.info("2-----" + pst.getQueryString());
        }
        if (INV_QUERY != null)
        {
          logger.info("Entering-------------fetchInvoiceData---------INV_QUERY----" + pst.getQueryString());
          ResultSet rst = pst.executeQuery();
          while (rst.next())
          {
            invoiceVO = new InvoiceDetailsVO();
            invoiceVO.setBillRefNo(rst.getString("BILLREFNO"));
            invoiceVO.setSelectedEventRifNo(rst.getString("EVENTREFNO"));
            invoiceVO.setShippingBillNO(rst.getString("SHIPBILLNO"));
            invoiceVO.setShippingDate(rst.getString("SHIPBILLDATE"));
            invoiceVO.setShippingPort(rst.getString("PORTCODE"));
            invoiceVO.setInvoiceSerialNo(rst.getString("INVSERIALNO"));
            invoiceVO.setInvoiceNo(rst.getString("INVNO"));
            invoiceVO.setInvoiceDate(rst.getString("INVDATE"));
           
            invoiceVO.setFobCurr(rst.getString("FOBCURRCODE"));
            invoiceVO.setFobAmt(rst.getString("FOBAMT"));
            invoiceVO.setFreightCurr(rst.getString("FRIEGHTCURRCODE"));
            invoiceVO.setFreightAmt(rst.getString("FRIEGHTAMT"));
            invoiceVO.setInsuranceCurr(rst.getString("INSCURRCODE"));
            invoiceVO.setInsuranceAmt(rst.getString("INSAMT"));
            invoiceVO.setCommissionCurr(rst.getString("COMMCURRCODE"));
            invoiceVO.setCommissionAmt(rst.getString("COMMAMT"));
            invoiceVO.setDeductionCurr(rst.getString("DEDCURRCODE"));
            invoiceVO.setDeductionAmt(rst.getString("DEDAMT"));
            invoiceVO.setDiscountCurr(rst.getString("DISCURRCODE"));
            invoiceVO.setDiscountAmt(rst.getString("DISAMT"));
            invoiceVO.setPackagingCurr(rst.getString("PKGCURRCODE"));
            invoiceVO.setPackagingAmt(rst.getString("PKGAMT"));
            invoiceList.add(invoiceVO);
          }
        }
      }
    }
    catch (Exception exception)
    {
      logger.info("Entering-------------fetchInvoiceData----exception------------ Method" + exception);
      exception.printStackTrace();
      throwDAOException(exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    logger.info("Exiting Method");
    return invoiceList;
  }
 
  public ArrayList<AcknowledgementListVO> custData(AcknowledgementListVO ackListVO)
    throws DAOException
  {
    logger.info("Entering Method");
    AcknowledgementListVO ackList = null;
    ArrayList custList = null;
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    String query = null;
    CommonMethods commonMethods = null;
    boolean cifFlag = false;
    boolean fullnameFlag = false;
    boolean numberFlag = false;
    int setValue = 0;
    try
    {
      commonMethods = new CommonMethods();
      custList = new ArrayList();
      con = DBConnectionUtility.getConnection();
     
      query = "SELECT * FROM GFPF WHERE GFCUN = GFCUN";
     
      String cif = ackListVO.getCustomerCIFNo();
      String fullname = ackListVO.getCustomerName();
      String number = ackListVO.getCustomerNo();
      if (fullname != null) {
        fullname = fullname.toLowerCase();
      }
      if ((cif != null) && (cif.length() > 0))
      {
        query = query + " and GFCUS1 like '%'||?||'%'";
        cifFlag = true;
      }
      if ((fullname != null) && (fullname.length() > 0))
      {
        query = query + " and UPPER(GFCUN) like UPPER('%'||?||'%')";
        fullnameFlag = true;
      }
      if ((number != null) && (number.length() > 0))
      {
        query = query + " and GFCPNC like '%'||?||'%'";
        numberFlag = true;
      }
      pst = new LoggableStatement(con, query);
      if (cifFlag) {
        pst.setString(++setValue, cif);
      }
      if (fullnameFlag) {
        pst.setString(++setValue, fullname);
      }
      if (numberFlag) {
        pst.setString(++setValue, number);
      }
      rs = pst.executeQuery();
      while (rs.next())
      {
        ackList = new AcknowledgementListVO();
        ackList.setCustNo(commonMethods.getEmptyIfNull(rs.getString("GFCPNC")).trim());
        ackList.setCustName(commonMethods.getEmptyIfNull(rs.getString("GFCUN")).trim());
        ackList.setCustCIFNo(commonMethods.getEmptyIfNull(rs.getString("GFCUS1")).trim());
        ackList.setCountry(commonMethods.getEmptyIfNull(rs.getString("gfcnal")).trim());
        ackList.setBlocked(commonMethods.getEmptyIfNull(rs.getString("gfcub")).trim());
        custList.add(ackList);
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
    return custList;
  }
 
  public ArrayList<ShippingDetailsVO> fetchSBUnUtilizeBills(AcknowledgementListVO ackListVO)
    throws DAOException
  {
    logger.info("Entering -------fetchSBUnUtilizeBills----------Method");
    ArrayList shpList = null;
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    String query = null;
    ShippingDetailsVO sbVO = null;
    CommonMethods commonMethods = null;
    boolean billRefNoFlag = false;
    boolean eventNoFlag = false;
    boolean shippingNoFlag = false;
    boolean shippingDateFlag = false;
    boolean portCodeFlag = false;
    boolean formNoFlag = false;
    int setValue = 0;
    try
    {
      shpList = new ArrayList();
      commonMethods = new CommonMethods();
      con = DBConnectionUtility.getConnection();
     

      String SHP_QUERY = "SELECT BILLREFNO,EVENTREFNO,SHIPBILLNO,TO_CHAR(TO_DATE(SHIPBILLDATE, 'ddmmyyyy'),'DD-MM-YYYY') as SHIPBILLDATE, PORTCODE,FORMNO,SHIPPINGBILLAMOUNT,SHIPPINGBILLCURR,UTILIZED_AMT,UNUTILIZED_AMT FROM ETTV_SB_OUTSTANDING_BILL_VW WHERE BILLREFNO = BILLREFNO ";
     
      commonMethods = new CommonMethods();
     
      String billRefNo = ackListVO.getBillRefNo();
      if (!commonMethods.isNull(billRefNo))
      {
        SHP_QUERY = SHP_QUERY + "AND BILLREFNO =?";
        billRefNoFlag = true;
      }
      String eventRefNo = ackListVO.getEventRefNo();
      if (!commonMethods.isNull(eventRefNo))
      {
        SHP_QUERY = SHP_QUERY + "AND EVENTREFNO =? ";
        eventNoFlag = true;
      }
      String shippingNo = ackListVO.getShippingBillNo();
      if (!commonMethods.isNull(shippingNo))
      {
        SHP_QUERY = SHP_QUERY + "AND SHIPBILLNO =? ";
        shippingNoFlag = true;
      }
      String shippingDate = commonMethods.getEmptyIfNull(ackListVO.getShippingBillDate()).trim();
      if (!commonMethods.isNull(shippingDate))
      {
        SHP_QUERY = SHP_QUERY + "AND TO_CHAR(TO_DATE(SHIPBILLDATE,'DDMMYYYY'),'DD-MM-YY') =  ?";
        shippingDateFlag = true;
      }
      String portCode = ackListVO.getPortCode();
      if (!commonMethods.isNull(portCode))
      {
        SHP_QUERY = SHP_QUERY + "AND PORTCODE = ?";
        portCodeFlag = true;
      }
      String formNo = ackListVO.getFormNo();
      if (!commonMethods.isNull(formNo))
      {
        SHP_QUERY = SHP_QUERY + "AND FORMNO = ?";
        formNoFlag = true;
      }
      pst = new LoggableStatement(con, SHP_QUERY);
      if (billRefNoFlag) {
        pst.setString(++setValue, billRefNo.trim());
      }
      if (eventNoFlag) {
        pst.setString(++setValue, eventRefNo.trim());
      }
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
      logger.info("EDPMS Query ------------------" + pst.getQueryString());
      rs = pst.executeQuery();
      while (rs.next())
      {
        sbVO = new ShippingDetailsVO();
        sbVO.setBillRefNo(commonMethods.getEmptyIfNull(rs.getString("BILLREFNO")).trim());
        sbVO.setSelectedEventRifNo(commonMethods.getEmptyIfNull(rs.getString("EVENTREFNO")).trim());
        sbVO.setShippingBillNo(commonMethods.getEmptyIfNull(rs.getString("SHIPBILLNO")).trim());
        sbVO.setShippingBillDate(commonMethods.getEmptyIfNull(rs.getString("SHIPBILLDATE")).trim());
        sbVO.setPortCode(commonMethods.getEmptyIfNull(rs.getString("PORTCODE")).trim());
        sbVO.setFormNo(commonMethods.getEmptyIfNull(rs.getString("FORMNO")).trim());
        sbVO.setShippingBillAmt(commonMethods.getEmptyIfNull(rs.getString("SHIPPINGBILLAMOUNT")).trim());
        sbVO.setShippingBillCurr(commonMethods.getEmptyIfNull(rs.getString("SHIPPINGBILLCURR")).trim());
        sbVO.setUtilizedAmt(commonMethods.getEmptyIfNull(rs.getString("UTILIZED_AMT")).trim());
        sbVO.setUnUtilizedAmt(commonMethods.getEmptyIfNull(rs.getString("UNUTILIZED_AMT")).trim());
        shpList.add(sbVO);
      }
    }
    catch (Exception exception)
    {
      logger.info("----------fetchSBUnUtilizeBills  exception--------------" + exception);
      throwDAOException(exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    logger.info("Exiting Method");
    return shpList;
  }
 
  public ArrayList<InvoiceDetailsVO> fetchMDFUnUtilizeBills(AcknowledgementListVO ackListVO)
    throws DAOException
  {
    logger.info("Entering ---------fetchMDFUnUtilizeBills----------Method");
    ArrayList shpList = null;
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    String query = null;
    InvoiceDetailsVO sbVO = null;
    CommonMethods commonMethods = null;
    boolean dateFromFlag = false;
    boolean dateToFlag = false;
    boolean shippingNoFlag = false;
    boolean shippingDateFlag = false;
    boolean portCodeFlag = false;
    boolean formNoFlag = false;
    int setValue = 0;
    try
    {
      shpList = new ArrayList();
      commonMethods = new CommonMethods();
      con = DBConnectionUtility.getConnection();
     

      String SHP_QUERY = "SELECT SHIPBILLNO,TO_CHAR(TO_DATE(SHIPBILLDATE, 'ddmmyyyy'),'DD-MM-YYYY') AS SHIPBILLDATE,PORTCODE, FORMNO,IECODE,ADCODE,EXPORTAGENCY,EXPORTTYPE,RECIND,INVSERIALNO,INVNO, TO_CHAR(TO_DATE(INVDATE, 'DD-MM-YY'),'DD-MM-YYYY') AS INVDATE,FOBCURRCODE,FOBAMT, FRIEGHTCURRCODE,FRIEGHTAMT,INSCURRCODE,INSAMT FROM ETTV_SB_UNUTILIZED_BILL_VW WHERE PORTCODE = PORTCODE ";
     
      commonMethods = new CommonMethods();
     
      String dateFrom = ackListVO.getDateFrom();
     
      String dateTo = ackListVO.getDateTo();
      if ((!commonMethods.isNull(dateFrom)) && (!commonMethods.isNull(dateTo)))
      {
        SHP_QUERY =
          SHP_QUERY + " AND MDF_DATE BETWEEN TO_DATE(?,'DD-MM-YY') " + " AND TO_DATE(?,'DD-MM-YY')";
        dateFromFlag = true;
        dateToFlag = true;
      }
      else
      {
        if (!commonMethods.isNull(dateFrom))
        {
          SHP_QUERY = SHP_QUERY + " AND MDF_DATE >= TO_DATE(?,'DD-MM-YY')";
          dateFromFlag = true;
        }
        if (!commonMethods.isNull(dateTo))
        {
          SHP_QUERY = SHP_QUERY + " AND MDF_DATE <= TO_DATE(?,'DD-MM-YY')";
          dateToFlag = true;
        }
      }
      String shippingNo = ackListVO.getShippingBillNo();
      if (!commonMethods.isNull(shippingNo))
      {
        SHP_QUERY = SHP_QUERY + "AND SHIPBILLNO =? ";
        shippingNoFlag = true;
      }
      String shippingDate = commonMethods.getEmptyIfNull(ackListVO.getShippingBillDate()).replace("/", "").trim();
      if (!commonMethods.isNull(shippingDate))
      {
        SHP_QUERY = SHP_QUERY + "AND TO_CHAR(TO_DATE(SHIPBILLDATE,'DDMMYYYY'),'DD-MM-YY') =?";
        shippingDateFlag = true;
      }
      String portCode = ackListVO.getPortCode();
      if (!commonMethods.isNull(portCode))
      {
        SHP_QUERY = SHP_QUERY + "AND PORTCODE =  ? ";
        portCodeFlag = true;
      }
      String formNo = ackListVO.getFormNo();
      if (!commonMethods.isNull(formNo))
      {
        SHP_QUERY = SHP_QUERY + "AND FORMNO =  ? ";
        formNoFlag = true;
      }
      pst = new LoggableStatement(con, SHP_QUERY);
      if (dateFromFlag) {
        pst.setString(++setValue, dateFrom.trim());
      }
      if (dateToFlag) {
        pst.setString(++setValue, dateTo.trim());
      }
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
      logger.info("EDPMS Query------------fetchMDFUnUtilizeBills" + pst.getQueryString());
      rs = pst.executeQuery();
      while (rs.next())
      {
        sbVO = new InvoiceDetailsVO();
        sbVO.setShippingBillNO(commonMethods.getEmptyIfNull(rs.getString("SHIPBILLNO")).trim());
        sbVO.setShippingDate(commonMethods.getEmptyIfNull(rs.getString("SHIPBILLDATE")).trim());
        sbVO.setShippingPort(commonMethods.getEmptyIfNull(rs.getString("PORTCODE")).trim());
        sbVO.setFormNo(commonMethods.getEmptyIfNull(rs.getString("FORMNO")).trim());
        sbVO.setIeCode(commonMethods.getEmptyIfNull(rs.getString("IECODE")).trim());
        sbVO.setAdCode(commonMethods.getEmptyIfNull(rs.getString("ADCODE")).trim());
       
        String tempExpAgency = commonMethods.getEmptyIfNull(rs.getString("EXPORTAGENCY")).trim();
        String expAgency = "";
        if (tempExpAgency.equalsIgnoreCase("1")) {
          expAgency = "Customs";
        } else if (tempExpAgency.equalsIgnoreCase("2")) {
          expAgency = "SEZ";
        } else if (tempExpAgency.equalsIgnoreCase("3")) {
          expAgency = "STPI";
        }
        sbVO.setExportAgency(expAgency);
       
        String tempExpType = commonMethods.getEmptyIfNull(rs.getString("EXPORTTYPE")).trim();
        String expType = "";
        if (tempExpType.equalsIgnoreCase("1")) {
          expType = "Goods";
        } else if (tempExpType.equalsIgnoreCase("2")) {
          expType = "Softex";
        } else if (tempExpType.equalsIgnoreCase("3")) {
          expType = "Royalty";
        }
        sbVO.setExportType(expType);
       
        sbVO.setRecordInd(commonMethods.getEmptyIfNull(rs.getString("RECIND")).trim());
        sbVO.setInvoiceSerialNo(commonMethods.getEmptyIfNull(rs.getString("INVSERIALNO")).trim());
        sbVO.setInvoiceNo(commonMethods.getEmptyIfNull(rs.getString("INVNO")).trim());
        sbVO.setInvoiceDate(commonMethods.getEmptyIfNull(rs.getString("INVDATE")).trim());
        sbVO.setFobCurr(commonMethods.getEmptyIfNull(rs.getString("FOBCURRCODE")).trim());
        sbVO.setFobAmt(commonMethods.getEmptyIfNull(rs.getString("FOBAMT")).trim());
        sbVO.setFreightCurr(commonMethods.getEmptyIfNull(rs.getString("FRIEGHTCURRCODE")).trim());
        sbVO.setFreightAmt(commonMethods.getEmptyIfNull(rs.getString("FRIEGHTAMT")).trim());
        sbVO.setInsuranceCurr(commonMethods.getEmptyIfNull(rs.getString("INSCURRCODE")).trim());
        sbVO.setInsuranceAmt(commonMethods.getEmptyIfNull(rs.getString("INSAMT")).trim());
        shpList.add(sbVO);
      }
    }
    catch (Exception exception)
    {
      logger.info("Entering ---------fetchMDFUnUtilizeBills----------exception" + exception);
      throwDAOException(exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    logger.info("Exiting Method");
    return shpList;
  }
}