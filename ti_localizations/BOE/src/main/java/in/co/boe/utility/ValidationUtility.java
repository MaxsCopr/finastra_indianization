package in.co.boe.utility;

import in.co.boe.dao.AbstractDAO;
import in.co.boe.dao.exception.DAOException;
import in.co.boe.vo.BOEBulkUploadVO;
import in.co.boe.vo.BoeVO;
import in.co.boe.vo.ManualBOEBulkUploadVO;
import in.co.boe.vo.OBBBulkUploadVO;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import org.apache.log4j.Logger;
 
public class ValidationUtility
  extends AbstractDAO
  implements ActionConstantsQuery
{
  DBConnectionUtility dbConnectionUtility;
  private static Logger logger = Logger.getLogger(ValidationUtility.class
    .getName());
  public static boolean isValiedPortCode(String sPortCode)
  {
    int isPortExt = 0;
    String sQuery = "";
    Connection con = null;
    LoggableStatement pst = null;
    ResultSet rs = null;
    try
    {
      sQuery = "SELECT COUNT(1) FROM EXTPORTCO WHERE TRIM(PCODE) = ? ";
      con = DBConnectionUtility.getConnection();
      pst = new LoggableStatement(con, sQuery);
      pst.setString(1, sPortCode);
      rs = pst.executeQuery();
      if (rs.next())
      {
        isPortExt = rs.getInt(1);
        if (isPortExt > 0) {
          return true;
        }
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    finally
    {
      sQuery = null;
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    sQuery = null;
    DBConnectionUtility.surrenderDB(con, pst, rs);
    return false;
  }
  public static boolean isDataExist_OBE_XML_UPLOAD(String BoeNo, String BoeDate, String Port_Code)
  {
    String Chck_OBE_DATA = "";
    Connection cone = null;
    LoggableStatement lst = null;
    ResultSet rst = null;
    try
    {
      Chck_OBE_DATA = "SELECT * FROM ETT_MANUAL_BOE_ACK WHERE  BILLOFENTRYNUMBER=? AND PORTOFDISCHARGE=? AND BILLOFENTRYDATE=?";
      cone = DBConnectionUtility.getConnection();
      lst = new LoggableStatement(cone, Chck_OBE_DATA);
      lst.setString(1, BoeNo);
      lst.setString(2, Port_Code);
      lst.setString(3, BoeDate);
      rst = lst.executeQuery();
      if (rst.next()) {
        return true;
      }
      return false;
    }
    catch (Exception e)
    {
      e.printStackTrace();
      logger.info("OBE Data Validation ------------------------>" + 
        e.getMessage());
    }
    finally
    {
      Chck_OBE_DATA = null;
      DBConnectionUtility.surrenderDB(cone, lst, rst);
    }
    return false;
  }
  public static boolean isValidAmount(String amountVal)
  {
    BigDecimal amount = null;
    try
    {
      String[] sAmount = amountVal.split("\\D");
      for (int i = 0; i < sAmount.length; i++) {
        amount = new BigDecimal(sAmount[i]);
      }
      if (amount.doubleValue() > 0.0D) {
        return true;
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      return false;
    }
    return false;
  }
  public static boolean isCheckInvTerm(String sInvTerm)
  {
    try
    {
      if ((sInvTerm != null) && (!sInvTerm.equals("")) && (
        (sInvTerm.equalsIgnoreCase("CIF")) || 
        (sInvTerm.equalsIgnoreCase("CF")) || 
        (sInvTerm.equalsIgnoreCase("CI")) || 
        (sInvTerm.equalsIgnoreCase("FOB")) || 
        (sInvTerm.equalsIgnoreCase("OTHERS")))) {
        return true;
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    return false;
  }
  public static boolean isValidCountry(String sCountryName)
  {
    String sQuery = "";
    Connection con = null;
    LoggableStatement pst = null;
    ResultSet rs = null;
    try
    {
      sQuery = "SELECT COUNT(1) FROM C7PF where TRIM(C7CNA) = ?";
      con = DBConnectionUtility.getConnection();
      pst = new LoggableStatement(con, sQuery);
      pst.setString(1, sCountryName);
      rs = pst.executeQuery();
      if ((rs.next()) && 
        (rs.getInt(1) > 0)) {
        return true;
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    finally
    {
      sQuery = null;
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    sQuery = null;
    DBConnectionUtility.surrenderDB(con, pst, rs);
    return false;
  }
  public static boolean compareDate(String sDateOne, String sDateTwo)
  {
    SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyymmdd");
    Calendar cal = Calendar.getInstance();
    try
    {
      if ((sDateOne != null) && (!sDateOne.equals("")) && 
        (sDateTwo != null) && (!sDateTwo.equals("")))
      {
        Date newDate1 = sdf.parse(sDateOne);
        Date newDate2 = sdf.parse(sDateTwo);
        cal.setTime(newDate1);
        String strDate1 = sdf1.format(cal.getTime());
        cal.setTime(newDate2);
        String strDate2 = sdf1.format(cal.getTime());
        if (Integer.parseInt(strDate2) > Integer.parseInt(strDate1)) {
          return true;
        }
      }
    }
    catch (ParseException ex)
    {
      ex.printStackTrace();
    }
    return false;
  }
  public static int isBOEClosed(String sBOENo, String sBOEDate, String sPortCode)
  {
    int iRet = 0;
    String sQuery = "";
    Connection con = null;
    LoggableStatement pst = null;
    ResultSet rs = null;
    sQuery = "SELECT ETT_IS_BOE_CLOSED(?,?,?) AS CLSTATUS FROM DUAL";
    try
    {
      con = DBConnectionUtility.getConnection();
      pst = new LoggableStatement(con, sQuery);
      pst.setString(1, sBOENo);
      pst.setString(2, sBOEDate);
      pst.setString(3, sPortCode);
      rs = pst.executeQuery();
      if (rs.next()) {
        iRet = rs.getInt(1);
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    finally
    {
      sQuery = null;
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    return iRet;
  }
  public static int isValidBillDate(BoeVO boeVO)
  {
    int iRet = 0;
    String sChkBox = "false";
    String sQuery = "";
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    sQuery = "SELECT (TO_DATE(EXPDATE,'dd/mm/rrrr') - TO_DATE(SYSDATE,'dd/mm/rrrr')) FROM(SELECT TO_DATE(BOE_PAYMENT_BP_BOE_DT,'DD/MM/rrrr')+180 EXPDATE FROM ETT_BOE_PAYMENT WHERE BOE_PAYMENT_BP_BOE_NO = ? AND PORT_CODE = ? AND ADCODE = ?)";
    try
    {
      con = DBConnectionUtility.getConnection();
      pst = con.prepareStatement(sQuery);
      pst.setString(1, boeVO.getBoeNo());
      pst.setString(2, boeVO.getPortCode());
      pst.setString(3, boeVO.getAdCode());
      rs = pst.executeQuery();
      if (rs.next())
      {
        String sBillDateStatus = rs.getString(1);
        if (Integer.parseInt(sBillDateStatus) > 0) {
          if (sChkBox.equals("true")) {
            iRet = 1;
          }
        }
        if (Integer.parseInt(sBillDateStatus) < 0) {
          if (sChkBox.equals("false")) {
            iRet = 2;
          }
        }
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      sQuery = null;
      try
      {
        rs.close();
        pst.close();
        con.close();
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
    finally
    {
      sQuery = null;
      try
      {
        rs.close();
        pst.close();
        con.close();
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
    }
    return iRet;
  }
  public static boolean validatePrecision(String sValue)
  {
    if ((sValue != null) && (!sValue.equals("")))
    {
      if ((sValue.charAt(0) == '.') && 
        (sValue.length() < 6)) {
        return true;
      }
      if (sValue.contains("."))
      {
        String[] sDoupleArr = sValue.split("\\.");
        if (sDoupleArr[0].length() < 17)
        {
          if (sDoupleArr.length == 2)
          {
            int iLen = sDoupleArr[1].length();
            if (iLen < 5) {
              return true;
            }
            return false;
          }
          return true;
        }
        return false;
      }
      if (sValue.length() < 17) {
        return true;
      }
    }
    return false;
  }
  public static String dateConvertToDDMMYYYString(String sDate, String sPaySrl)
  {
    String sConvertedDate = "";
    String sIncr = "";
    SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
    SimpleDateFormat sdf1 = new SimpleDateFormat("ddmmyyyy");
    try
    {
      Date date = sdf.parse(sDate);
      sPaySrl = sPaySrl.substring(sPaySrl.length() - 2);
      sConvertedDate = sdf1.format(date);
      int iIncr = Integer.parseInt(sPaySrl) + 1;
      String sIncre = Integer.toString(iIncr);
      if (sIncre.length() == 1) {
        sIncr = sConvertedDate + "0" + Integer.toString(iIncr);
      }
      if (sIncre.length() == 2) {
        sIncr = sConvertedDate + Integer.toString(iIncr);
      }
      if ((sPaySrl == null) || (sPaySrl.equalsIgnoreCase(""))) {
        sIncr = sConvertedDate + "01";
      }
    }
    catch (ParseException pe)
    {
      pe.printStackTrace();
    }
    return sIncr;
  }
  public boolean validateDateLength(String sDate)
  {
    SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
    try
    {
      if ((sDate != null) && (!sDate.equals("")))
      {
        Date date = sdf.parse(sDate);
        if (sdf.format(date).length() == 10) {
          return true;
        }
      }
      if ((sDate == null) || (sDate.equals(""))) {
        return true;
      }
    }
    catch (ParseException pe)
    {
      pe.printStackTrace();
    }
    return false;
  }
  public ArrayList valBulkUpload(ArrayList<ManualBOEBulkUploadVO> allBulkVO)
    throws DAOException
  {
    allBulkVO.trimToSize();
    int k = 1;
    String j = "";
    ArrayList<String> alBoemanbulkUpload = new ArrayList();
    CommonMethods commonMethods = null;
    ManualBOEBulkUploadVO excelDataVO = null;
    logger.info("size----" + allBulkVO.size());
    try
    {
      commonMethods = new CommonMethods();
      allBulkVO.trimToSize();
      for (int i = 0; i < allBulkVO.size(); i++)
      {
        k++;
        excelDataVO = new ManualBOEBulkUploadVO();
        logger.info("start to excel validation");
        excelDataVO = (ManualBOEBulkUploadVO)allBulkVO.get(i);
        if (commonMethods.isNull(excelDataVO.getBoeNo())) {
          j = j + "Please enter BOE Number,";
        } else if (excelDataVO.getBoeNo().length() != 7) {
          j = 
            j + "BOE Number should be always 7 digit(" + excelDataVO.getBoeNo() + "),";
        }
        if (commonMethods.isNull(excelDataVO.getBoeDate())) {
          j = j + "Please enter BOE Date,";
        } else if ((!validateddmmyyyyLength(excelDataVO.getBoeDate())) && 
          (excelDataVO.getBoeDate().length() != 10)) {
          j = j + "Please enter Date in dd/mm/yyyy format,";
        }
        if (commonMethods.isNull(excelDataVO.getPortCode())) {
          j = j + "Please enter Port Code,";
        } else if (excelDataVO.getPortCode().length() > 6) {
          j = 
            j + "Invalid Port Code Length (" + excelDataVO.getPortCode() + "),";
        }
        if (commonMethods.isNull(excelDataVO.getIeCode())) {
          j = j + "Please enter IE Code,";
        } else if (excelDataVO.getIeCode().length() > 10) {
          j = 
            j + "Invalid IE Code Length (" + excelDataVO.getIeCode() + "),";
        }
        if (commonMethods.isNull(excelDataVO.getAdCode())) {
          j = j + "Please enter AD Code,";
        } else if (excelDataVO.getAdCode().length() > 10) {
          j = 
            j + "Invalid AD Code Length (" + excelDataVO.getAdCode() + "),";
        }
        if ((!commonMethods.isNull(excelDataVO.getIgmDate())) && 
          (!validateddmmyyyyLength(excelDataVO.getIgmDate()))) {
          j = j + "Please enter Igm Date in dd/mm/yyyy format,";
        }
        if ((!commonMethods.isNull(excelDataVO.getHblDate())) && 
          (!validateddmmyyyyLength(excelDataVO.getHblDate()))) {
          j = j + "Please enter Hbl Date in dd/mm/yyyy format,";
        }
        if ((!commonMethods.isNull(excelDataVO.getMblDate())) && 
          (!validateddmmyyyyLength(excelDataVO.getMblDate()))) {
          j = j + "Please enter Mbl Date in dd/mm/yyyy format,";
        }
        if (commonMethods.isNull(excelDataVO.getImAgency())) {
          j = j + "Please enter Import Agency,";
        } else if (excelDataVO.getImAgency().length() > 1) {
          j = 
            j + "Invalid Import Agency Length (" + excelDataVO.getImAgency() + "),";
        }
        if (commonMethods.isNull(excelDataVO.getRecInd())) {
          j = j + "Please enter Record Indicator,";
        } else if (!commonMethods.isNumeric(excelDataVO.getRecInd())) {
          j = 
            j + "Invalid Record Indicator (" + excelDataVO.getRecInd() + "),";
        } else if (excelDataVO.getRecInd().length() > 1) {
          j = 
            j + "Invalid Record Indicator Length (" + excelDataVO.getRecInd() + "),";
        }
        if (commonMethods.isNull(excelDataVO.getGovprv())) {
          j = j + "Please enter G-P,";
        } else if (excelDataVO.getGovprv().length() > 1) {
          j = 
            j + "Invalid G-P Length (" + excelDataVO.getGovprv() + "),";
        }
        if (commonMethods.isNull(excelDataVO.getPos())) {
          j = j + "Please enter Port of shipment,";
        } else if (excelDataVO.getPos().length() > 50) {
          j = 
            j + "Invalid Port of Shipment Length (" + excelDataVO.getPos() + "(,";
        }
        if (commonMethods.isNull(excelDataVO.getInvoiceSerialNo())) {
          j = j + "Please enter Invoice Serial Number,";
        } else if (excelDataVO.getInvoiceSerialNo().length() > 10) {
          j = 
            j + "Invalid Invoice Serial Number (" + excelDataVO.getInvoiceSerialNo() + "),";
        }
        if (commonMethods.isNull(excelDataVO.getInvoiceNo())) {
          j = j + "Please enter Invoice Number,";
        } else if (excelDataVO.getInvoiceNo().length() > 50) {
          j = 
            j + "Invalid Invoice Number (" + excelDataVO.getInvoiceNo() + "),";
        }
        if (commonMethods.isNull(excelDataVO.getTermsofInvoice()))
        {
          j = j + "Please Enter Terms of Services,";
          logger.info("Please Enter Terms of Services");
        }
        else if (!excelDataVO.getTermsofInvoice().equalsIgnoreCase("CIF"))
        {
          if (!excelDataVO.getTermsofInvoice().equalsIgnoreCase("OTHERS")) {
            if (!excelDataVO.getTermsofInvoice().equalsIgnoreCase("CF")) {
              if (!excelDataVO.getTermsofInvoice().equalsIgnoreCase("CI")) {
                if (!excelDataVO.getTermsofInvoice().equalsIgnoreCase("FOB"))
                {
                  j = 
                    j + excelDataVO.getTermsofInvoice() + "Please Enter valid Terms Of Services,";
                  logger.info("Please Enter valid Terms Of Services");
                }
              }
            }
          }
        }
        if (commonMethods.isNull(excelDataVO.getInvoiceAmt())) {
          j = j + "Please enter Invoice Amount,";
        } else if (!validatePrecision(excelDataVO.getInvoiceAmt(), "20", "4")) {
          j = 
            j + "Invalid Invoice Amount (" + excelDataVO.getInvoiceAmt() + "),";
        }
        if (commonMethods.isNull(excelDataVO.getInvoiceCurrency()))
        {
          j = j + "Please enter Invoice Amount Currency,";
        }
        else
        {
          if (excelDataVO.getInvoiceCurrency().length() > 3) {
            j = 
              j + "Invalid Invoice Amount Currency Length (" + excelDataVO.getInvoiceCurrency() + "),";
          }
          if (commonMethods.isNumeric(excelDataVO.getInvoiceCurrency())) {
            j = 
              j + "Invalid Invoice Amount Currency Length (" + excelDataVO.getInvoiceCurrency() + "),";
          }
        }
        if (commonMethods.isNull(excelDataVO.getFreightAmount())) {
          j = j + "Please enter Freight Amount,";
        } else if (!validatePrecision(excelDataVO.getFreightAmount(), "20", "4")) {
          j = 
            j + "Invalid Freight Amount (" + excelDataVO.getFreightAmount() + "),";
        }
        if (commonMethods.isNull(excelDataVO.getFreightCurrencyCode()))
        {
          j = j + "Please enter Freight Amount Currency,";
        }
        else
        {
          if (excelDataVO.getFreightCurrencyCode().length() > 3) {
            j = 
              j + "Invalid Freight Amount Currency Length (" + excelDataVO.getFreightCurrencyCode() + "),";
          }
          if (commonMethods.isNumeric(excelDataVO.getInvoiceCurrency())) {
            j = 
              j + "Invalid Freight Amount Currency Length (" + excelDataVO.getFreightCurrencyCode() + "),";
          }
        }
        if (commonMethods.isNull(excelDataVO.getInsuranceAmount())) {
          j = j + "Please enter Insurance Amount,";
        } else if (!validatePrecision(excelDataVO.getInsuranceAmount(), "20", "4")) {
          j = 
            j + "Invalid Insurance Amount (" + excelDataVO.getInsuranceAmount() + "),";
        }
        if (commonMethods.isNull(excelDataVO.getInsuranceCurrencyCode()))
        {
          j = j + "Please enter Insurance Amount Currency,";
        }
        else
        {
          if (excelDataVO.getInsuranceCurrencyCode().length() > 3) {
            j = 
              j + "Invalid Insurance Amount Currency Length (" + excelDataVO.getInsuranceCurrencyCode() + "),";
          }
          if (commonMethods.isNumeric(excelDataVO.getInvoiceCurrency())) {
            j = 
              j + "Invalid Insurance Amount Currency Length (" + excelDataVO.getInsuranceCurrencyCode() + "),";
          }
        }
        if (commonMethods.isNull(excelDataVO.getSupplierName())) {
          j = j + "Please enter Supplier Name,";
        } else if (excelDataVO.getSupplierName().length() > 200) {
          j = 
            j + " Invalid Supplier Name Length (" + excelDataVO.getSupplierName() + "),";
        }
        if (commonMethods.isNull(excelDataVO.getSupplierAddress())) {
          j = j + "Please enter Supplier Address,";
        } else if (excelDataVO.getSupplierAddress().length() > 500) {
          j = 
            j + "Invalid Supplier Address Length (" + excelDataVO.getSupplierAddress() + "),";
        }
        if (commonMethods.isNull(excelDataVO.getSupplierCountry())) {
          j = j + "Please enter Supplier Country,";
        } else if (excelDataVO.getSupplierCountry().length() > 50) {
          j = 
            j + "Invalid Supplier Country Length (" + excelDataVO.getSupplierCountry() + "),";
        }
        if (!j.equals(""))
        {
          j = j.substring(0, j.length() - 1);
          excelDataVO.setErrorDesc(j);
          j = "Row Number:" + Integer.toString(k) + " " + j + " |";
          alBoemanbulkUpload.add(j);
          j = "";
        }
      }
      k = 0;
      logger.info("end of excel validation");
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
      throwDAOException(exception);
    }
    return alBoemanbulkUpload;
  }
  public boolean validateddmmyyyyLength(String sDate)
  {
    DateFormat sdf1 = new SimpleDateFormat("dd/mm/yyyy");
    try
    {
      if ((sDate != null) && (!sDate.equals("")) && (sDate.length() == 10))
      {
        Date date = sdf1.parse(sDate);
        if (sdf1.format(date).length() == 10) {
          return true;
        }
        return false;
      }
    }
    catch (ParseException pe)
    {
      pe.printStackTrace();
    }
    return false;
  }
  public ArrayList obbValBulkUpload(ArrayList<OBBBulkUploadVO> alBulkVO)
    throws DAOException
  {
    alBulkVO.trimToSize();
    int k = 1;
    String j = "";
    CommonMethods commonMethods = null;
    OBBBulkUploadVO excelDataVO = null;
    ArrayList<String> obbBoeBulkUpload = new ArrayList();
    try
    {
      commonMethods = new CommonMethods();
      alBulkVO.trimToSize();
      for (int i = 0; i < alBulkVO.size(); i++)
      {
        k++;
        excelDataVO = new OBBBulkUploadVO();
        excelDataVO = (OBBBulkUploadVO)alBulkVO.get(i);
        if (commonMethods.isNull(excelDataVO.getBoeNo())) {
          j = j + "Please enter BOE Number,";
        } else if (!commonMethods.isNull(excelDataVO.getBoeNo())) {
          if (excelDataVO.getBoeNo().length() > 7) {
            j = 
              excelDataVO.getBoeNo() + " Invalid BOE Number Length,";
          }
        }
        if ((!commonMethods.isNull(excelDataVO.getImAgency())) && 
          (excelDataVO.getImAgency().length() > 1)) {
          j = 
            j + excelDataVO.getImAgency() + " Invalid Import Agency Length,";
        }
        if (commonMethods.isNull(excelDataVO.getBoeDate())) {
          j = j + "Please enter BOE Date,";
        } else if ((!validateddmmyyyyLength(excelDataVO.getBoeDate())) && 
          (excelDataVO.getBoeDate().length() != 10)) {
          j = j + excelDataVO.getBoeDate() + " Invalid BOE Date,";
        }
        if (commonMethods.isNull(excelDataVO.getPortCode())) {
          j = j + "Please enter Port Code,";
        } else if ((!commonMethods.isNull(excelDataVO.getPortCode())) && 
          (excelDataVO.getPortCode().length() > 6)) {
          j = 
            j + excelDataVO.getPortCode() + " Invalid Port Code Length,";
        }
        if ((!commonMethods.isNull(excelDataVO.getAdCode())) && 
          (excelDataVO.getAdCode().length() > 7)) {
          j = 
            j + excelDataVO.getAdCode() + " Invalid ADCode Length,";
        }
        if ((!commonMethods.isNull(excelDataVO.getIename())) && 
          (excelDataVO.getIename().length() > 200)) {
          j = 
            j + excelDataVO.getIename() + " Invalid IEName Length,";
        }
        if ((!commonMethods.isNull(excelDataVO.getIeCode())) && 
          (excelDataVO.getIeCode().length() > 10)) {
          j = j + excelDataVO.getIeCode() + " Invalid IECode Length";
        }
        if (!j.equals(""))
        {
          j = j.substring(0, j.length() - 1);
          j = "Row Number:" + Integer.toString(k) + " " + j + " |";
          obbBoeBulkUpload.add(j);
          j = "";
        }
      }
      k = 0;
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
      throwDAOException(exception);
    }
    return obbBoeBulkUpload;
  }
  public boolean dateFormat(String sDate)
  {
    String sDay = "";
    String sMonth = "";
    String sYear = "";
    if ((sDate != null) && (sDate.length() <= 10))
    {
      sDay = sDate.substring(0, 2);
      sMonth = sDate.substring(3, 5);
      sYear = sDate.substring(6, 10);
      int iMonth = Integer.parseInt(sMonth);
      if ((iMonth == 1) || (iMonth == 3) || (iMonth == 5) || (iMonth == 7) || 
        (iMonth == 8) || (iMonth == 10) || (iMonth == 12))
      {
        if (Integer.parseInt(sDay) <= 31) {
          return true;
        }
        return false;
      }
      if (iMonth == 2)
      {
        int iYear = Integer.parseInt(sYear);
        if (iYear % 4 == 0)
        {
          if (Integer.parseInt(sDay) <= 29) {
            return true;
          }
          return false;
        }
        if (Integer.parseInt(sDay) <= 28) {
          return true;
        }
        return false;
      }
      if ((iMonth == 4) || (iMonth == 6) || (iMonth == 9) || (iMonth == 11))
      {
        if (Integer.parseInt(sDay) <= 30) {
          return true;
        }
        return false;
      }
    }
    return false;
  }
  public ArrayList valExtBulkUpload(ArrayList<BOEBulkUploadVO> alBulkVO)
    throws DAOException
  {
    alBulkVO.trimToSize();
    int k = 1;
    String j = "";
    ArrayList<String> alBoeClosBulkUpload = new ArrayList();

 
 
    CommonMethods commonMethods = null;
    BOEBulkUploadVO excelDataVO = null;
    try
    {
      commonMethods = new CommonMethods();
      alBulkVO.trimToSize();
      logger.info("in valExtBulkUpload  ");
      for (int i = 0; i < alBulkVO.size(); i++)
      {
        k++;
        excelDataVO = new BOEBulkUploadVO();
        logger.info("start to excel validation");
        excelDataVO = (BOEBulkUploadVO)alBulkVO.get(i);
        if (commonMethods.isNull(excelDataVO.getPaymentRefNo())) {
          j = j + "Please enter Payment Reference Number,";
        } else if (excelDataVO.getPaymentRefNo().length() > 30) {
          j = 
            j + "Invalid Payment Reference Number Length (" + excelDataVO.getPaymentRefNo() + "),";
        }
        if (commonMethods.isNull(excelDataVO.getEventRefNo())) {
          j = j + "Please enter Event Reference Number,";
        } else if (excelDataVO.getEventRefNo().length() > 6) {
          j = 
            j + "Invalid Event Reference Number Length (" + excelDataVO.getEventRefNo() + "),";
        }
        if (commonMethods.isNull(excelDataVO.getPaymentAmnt())) {
          j = j + "Please enter Payment Amount,";
        } else if (!isValidAmount(excelDataVO.getPaymentAmnt())) {
          j = 
            j + "Invalid Payment Amount (" + excelDataVO.getPaymentAmnt() + "),";
        }
        if (commonMethods.isNull(excelDataVO.getPayAmntCurr()))
        {
          j = j + "Please enter Payment Amount Currency,";
        }
        else
        {
          if (excelDataVO.getPayAmntCurr().length() > 3) {
            j = 
              j + "Invalid Payment Amount Currency (" + excelDataVO.getPayAmntCurr() + "),";
          }
          if (commonMethods.isNumeric(excelDataVO.getPayAmntCurr())) {
            j = 
              j + "Invalid Payment Amount Currency (" + excelDataVO.getPayAmntCurr() + "),";
          }
        }
        if (commonMethods.isNull(excelDataVO.getBoeNo())) {
          j = j + "Please enter BOE Number,";
        } else if (excelDataVO.getBoeNo().length() != 7) {
          j = 
            j + "Invalid BOE Number (" + excelDataVO.getBoeNo() + ").BOE Number should be always 7 digit,";
        }
        if (commonMethods.isNull(excelDataVO.getBoeDate())) {
          j = j + "Please enter BOE Date,";
        } else if ((!validateddmmyyyyLength(excelDataVO.getBoeDate())) && 
          (excelDataVO.getBoeDate().length() != 10)) {
          j = j + "Please enter BOE Date in dd/mm/yyyy format,";
        }
        if (commonMethods.isNull(excelDataVO.getPortCode())) {
          j = j + "Please enter Port Code,";
        } else if (excelDataVO.getPortCode().length() > 6) {
          j = 
            j + "Invalid Port Code Length (" + excelDataVO.getPortCode() + "),";
        }
        if (commonMethods.isNull(excelDataVO.getBoeAmnt())) {
            j = j + "Please enter BOE Amount,";
          } else if (!isValidAmount(excelDataVO.getBoeAmnt())) {
            j = 
              j + "Invalid BOE Amount (" + excelDataVO.getBoeAmnt() + "),";
          }
          if (commonMethods.isNull(excelDataVO.getBoeAmntCurr()))
          {
            j = j + "Please enter BOE Amount Currency,";
          }
          else
          {
            if (excelDataVO.getBoeAmntCurr().length() > 3) {
              j = 
                j + "Invalid BOE Amount Currency (" + excelDataVO.getBoeAmntCurr() + "),";
            }
            if (commonMethods.isNumeric(excelDataVO.getBoeAmntCurr())) {
              j = 
                j + "Invalid BOE Amount Currency (" + excelDataVO.getBoeAmntCurr() + "),";
            }
          }
          if (commonMethods.isNull(excelDataVO.getBoeAmntEndorse())) {
            j = j + "Please enter BOE Amount available for Endorsement,";
          } else if (!isValidAmount(excelDataVO.getBoeAmntEndorse())) {
            j = 
              j + "Invalid BOE Amount available for Endorsement (" + excelDataVO.getBoeAmntEndorse() + "),";
          }
          if (commonMethods.isNull(excelDataVO.getBoeAllocAmnt())) {
            j = j + "Please enter BOE Allocated Amount in Payment Currency,";
          } else if (!isValidAmount(excelDataVO.getBoeAllocAmnt())) {
            j = 
              j + "Invalid BOE Allocated Amount in Payment Currency (" + excelDataVO.getBoeAllocAmnt() + "),";
          }
          if ((!commonMethods.isNull(excelDataVO.getChangeIeCode())) && 
            (excelDataVO.getChangeIeCode().length() > 10)) {
            j = 
              j + "Invalid Change IE Code Length (" + excelDataVO.getChangeIeCode() + "),";
          }
          if (commonMethods.isNull(excelDataVO.getBesRecInd())) {
            j = j + "Please enter BES Record Indicator,";
          } else if (!commonMethods.isNumeric(excelDataVO.getBesRecInd())) {
            j = j + "Invalid BES Record Indicator,";
          } else if (excelDataVO.getBesRecInd().length() > 1) {
            j = j + "BES Record Indicator should be a single digit,";
          }
          if ((!commonMethods.isNull(excelDataVO.getRemarks())) && 
            (excelDataVO.getRemarks().length() > 200)) {
            j = 
              j + "Remarks Exceed 200 characters (" + excelDataVO.getRemarks() + "),";
          }
          if (commonMethods.isNull(excelDataVO.getInvoiceSerNo())) {
            j = j + "Please enter Invoice Serial Number,";
          } else if (excelDataVO.getInvoiceSerNo().length() > 10) {
            j = 
              j + "Invalid Invoice Serial Number (" + excelDataVO.getInvoiceSerNo() + "),";
          }
          if (commonMethods.isNull(excelDataVO.getInvoiceNo())) {
            j = j + "Please enter Invoice Number,";
          } else if (excelDataVO.getInvoiceNo().length() > 50) {
            j = 
              j + "Invalid Invoice Number (" + excelDataVO.getInvoiceNo() + "),";
          }
          if (commonMethods.isNull(excelDataVO.getInvRelAmt())) {
            j = j + "Please enter Invoice Realization Amount,";
          } else if (!validatePrecision(excelDataVO.getInvRelAmt(), "20", "4")) {
            j = 
              j + "Invoice Realization Amount (" + excelDataVO.getInvRelAmt() + "),";
          }
          if ((!commonMethods.isNull(excelDataVO.getPaymentRefNo())) && (!commonMethods.isNull(excelDataVO.getEventRefNo())))
          {
            j = j + CommonMethods.invalidPaymentReference(excelDataVO.getPaymentRefNo(), excelDataVO.getEventRefNo());
            logger.info("j  :" + j);
          }
          logger.info("j finnaly :" + j);
          if (!j.equals(""))
          {
            j = j.substring(0, j.length() - 1);
            excelDataVO.setErrorDesc(j);
            j = "Row Number:" + Integer.toString(k) + " " + j + " |";
            alBoeClosBulkUpload.add(j);
            j = "";
          }
        }
        k = 0;
      }
      catch (Exception exception)
      {
        exception.printStackTrace();
        logger.info(exception);
      }
      return alBoeClosBulkUpload;
    }
    public boolean validatePrecision(String sValue, String sLenOne, String sLenTwo)
    {
      if ((sValue != null) && (!sValue.equals(""))) {
        if (sValue.contains("."))
        {
          String[] sDotCount = sValue.split("\\.");
          if ((sDotCount.length <= 2) || (sDotCount.length == 0))
          {
            if ((sValue.charAt(0) == '.') && 
              (sValue.length() <= Integer.valueOf(sLenTwo).intValue())) {
              return true;
            }
            if (sDotCount.length == 2)
            {
              int iFirstLen = sDotCount[0].length();
              int iSecLen = sDotCount[1].length();
              if (iFirstLen <= Integer.valueOf(sLenOne).intValue())
              {
                if (iSecLen <= Integer.valueOf(sLenTwo).intValue()) {
                  return true;
                }
                return false;
              }
            }
          }
        }
        else if (sValue.length() <= Integer.valueOf(sLenOne).intValue())
        {
          return true;
        }
      }
      return false;
    }
  }
