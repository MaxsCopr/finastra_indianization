package in.co.boe.dao.bulkUpload;

import in.co.boe.dao.AbstractDAO;
import in.co.boe.dao.exception.DAOException;
import in.co.boe.utility.ActionConstants;
import in.co.boe.utility.ActionConstantsQuery;
import in.co.boe.utility.CommonMethods;
import in.co.boe.utility.DBConnectionUtility;
import in.co.boe.utility.LoggableStatement;
import in.co.boe.utility.ValidationUtility;
import in.co.boe.vo.AlertMessagesVO;
import in.co.boe.vo.ManualBOEBulkUploadVO;
import in.co.boe.vo.TransactionVO;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.struts2.ServletActionContext;
 
public class ManualBOEBulkUploadDAO
  extends AbstractDAO
  implements ActionConstantsQuery, ActionConstants
{
  private static Logger logger = Logger.getLogger(ManualBOEBulkUploadDAO.class.getName());
  static ManualBOEBulkUploadDAO dao;
  public static ManualBOEBulkUploadDAO getDAO()
  {
    if (dao == null) {
      dao = new ManualBOEBulkUploadDAO();
    }
    return dao;
  }
  private ArrayList<AlertMessagesVO> alertMsgArray = new ArrayList();
  public ArrayList<AlertMessagesVO> getAlertMsgArray()
  {
    return this.alertMsgArray;
  }
  public void setAlertMsgArray(ArrayList<AlertMessagesVO> alertMsgArray)
  {
    this.alertMsgArray = alertMsgArray;
  }
  public TransactionVO getExcelValidate(ManualBOEBulkUploadVO manbulkVO)
    throws DAOException, IOException
  {
    logger.info("Entering Method");
    int i = 1;
    int insertedRowCount = 0;
    String batchId = "";
    String sErrorMsg = "";
    HSSFWorkbook workbook = null;
    ValidationUtility validation1 = new ValidationUtility();
    ArrayList<ManualBOEBulkUploadVO> manualBOEList = null;
    ArrayList<String> excelErrorList = new ArrayList();
    TransactionVO trans = null;
    String errorExcel = "";
    try
    {
      trans = new TransactionVO();
      manualBOEList = new ArrayList();
      File filename = manbulkVO.getInputFile();
      if (filename != null)
      {
        FileInputStream mbeFile = new FileInputStream(filename);
        workbook = new HSSFWorkbook(mbeFile);
        HSSFSheet sheet = workbook.getSheetAt(0);
        String sequenceOfColumns = getSequenceOfColumns();
        String[] columnsArray = sequenceOfColumns.split("\\|");
        boolean isSquenceMissed = false;
        Iterator<Row> rows = sheet.rowIterator();
        while (rows.hasNext())
        {
          logger.info("row---1" + rows);
          String boeNo = "";String boeDate = "";String portCode = "";String impagc = "";String RecordInd = "";String adCode = "";
          String govprv = "";String ieCode = "";String iename = "";String ieadd = "";String iepan = "";String cifNo = "";
          String porshp = "";String igmNumber = "";String igmDate = "";
          String mblNumber = "";String mblDate = "";String hblNumber = "";String hblDate = "";
          String invSNo = "";String invNo = "";String termsofInvoice = "";String invAmt = "";String invCurr = "";
          String freightAmount = "";String freightCurrencyCode = "";String insuranceAmount = "";String insuranceCurrencyCode = "";
          String agencyCommission = "";String agencyCurrency = "";String discountCharges = "";String discountCurrency = "";
          String miscellaneousCharges = "";String miscellaneousCurrency = "";String supplierName = "";String supplierAddress = "";String supplierCountry = "";
          String sellerName = "";String sellerAddress = "";String sellerCountry = "";String thirdPartyName = "";
          String ThirdPartyAddress = "";String thirdPartyCountry = "";
          if (i == 1)
          {
            HSSFRow rowObject = (HSSFRow)rows.next();
            logger.info("Excel Headers");
            if (columnsArray.length != rowObject.getLastCellNum())
            {
              logger.info("Excel Column count mismatched");
              String excelError = "Excel Column count mismatched";
              excelErrorList.add(excelError);
              break;
            }
            for (int columnCount = 0; columnCount < columnsArray.length; columnCount++)
            {
              logger.info("Column Name from Sequence---> [" + columnCount + "]" + columnsArray[columnCount]);
              logger.info("Column Name from Excel---> [" + columnCount + "]" + rowObject.getCell(columnCount));
              if (!columnsArray[columnCount].toString().equalsIgnoreCase(rowObject.getCell(columnCount).toString()))
              {
                logger.info("Excel Column Sequence mismatch");
                isSquenceMissed = true;
              }
            }
            if (isSquenceMissed)
            {
              String excelError = "Excel Column Sequence mismatch";
              excelErrorList.add(excelError);
              break;
            }
          }
          else
          {
            logger.info("inside---" + i);
            manbulkVO = new ManualBOEBulkUploadVO();
            logger.info("row---2" + rows);
            HSSFRow rowObject = (HSSFRow)rows.next();
            logger.info("row---3" + rows);
            DataFormatter formatter = new DataFormatter();

 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
            HSSFCell billEntryNo = rowObject.getCell(0);
            HSSFCell billEntryDate = rowObject.getCell(1);
            HSSFCell portofCode = rowObject.getCell(2);
            HSSFCell boeIeCode = rowObject.getCell(3);
            HSSFCell boeAdcode = rowObject.getCell(4);
            HSSFCell boeIgmNo = rowObject.getCell(5);
            HSSFCell boeIgmDate = rowObject.getCell(6);
            HSSFCell boeHblNo = rowObject.getCell(7);
            HSSFCell boeHblDate = rowObject.getCell(8);
            HSSFCell boeMblNo = rowObject.getCell(9);
            HSSFCell boeMblDate = rowObject.getCell(10);
            HSSFCell importAgency = rowObject.getCell(11);
            HSSFCell recInd = rowObject.getCell(12);
            HSSFCell boeGP = rowObject.getCell(13);
            HSSFCell portOfShipment = rowObject.getCell(14);

 
            HSSFCell invoiceSNo = rowObject.getCell(15);
            HSSFCell invoiceNo = rowObject.getCell(16);
            HSSFCell termsInv = rowObject.getCell(17);
            HSSFCell invoiceAmt = rowObject.getCell(18);
            HSSFCell invoiceCurr = rowObject.getCell(19);
            HSSFCell freAmt = rowObject.getCell(20);
            HSSFCell freCurr = rowObject.getCell(21);
            HSSFCell insAmt = rowObject.getCell(22);
            HSSFCell insCurr = rowObject.getCell(23);
            HSSFCell agencyComm = rowObject.getCell(24);
            HSSFCell agencyCurr = rowObject.getCell(25);
            HSSFCell disAmt = rowObject.getCell(26);
            HSSFCell disCurr = rowObject.getCell(27);
            HSSFCell miscAmt = rowObject.getCell(28);
            HSSFCell miscCurr = rowObject.getCell(29);
            HSSFCell suppName = rowObject.getCell(30);
            HSSFCell suppAddr = rowObject.getCell(31);
            HSSFCell suppCty = rowObject.getCell(32);
            HSSFCell sellName = rowObject.getCell(33);
            HSSFCell sellAddr = rowObject.getCell(34);
            HSSFCell sellCty = rowObject.getCell(35);
            HSSFCell thirdName = rowObject.getCell(36);
            HSSFCell thirdAddr = rowObject.getCell(37);
            HSSFCell thirdCty = rowObject.getCell(38);
            if ((billEntryNo != null) && 
                    (billEntryNo.getCellType() != 3))
                  {
                    boeNo = formatter.formatCellValue(billEntryNo);
                    boeNo = boeNo.trim();
                  }
                  if ((billEntryDate != null) && 
                    (billEntryDate.getCellType() != 3)) {
                    boeDate = billEntryDate.toString().trim();
                  }
                  if ((portofCode != null) && 
                    (portofCode.getCellType() != 3))
                  {
                    portCode = formatter.formatCellValue(portofCode);
                    portCode = portCode.trim();
                  }
                  if ((importAgency != null) && 
                    (importAgency.getCellType() != 3))
                  {
                    impagc = formatter.formatCellValue(importAgency);
                    impagc = impagc.trim();
                  }
                  if ((recInd != null) && 
                    (recInd.getCellType() != 3))
                  {
                    RecordInd = formatter.formatCellValue(recInd);
                    RecordInd = RecordInd.trim();
                  }
                  if ((boeAdcode != null) && 
                    (boeAdcode.getCellType() != 3))
                  {
                    adCode = formatter.formatCellValue(boeAdcode);
                    adCode = adCode.trim();
                  }
                  if ((boeGP != null) && 
                    (boeGP.getCellType() != 3))
                  {
                    govprv = formatter.formatCellValue(boeGP);
                    govprv = govprv.trim();
                  }
                  if ((boeIeCode != null) && 
                    (boeIeCode.getCellType() != 3))
                  {
                    ieCode = formatter.formatCellValue(boeIeCode);
                    ieCode = ieCode.trim();
                  }
                  if ((portOfShipment != null) && 
                    (portOfShipment.getCellType() != 3))
                  {
                    porshp = formatter.formatCellValue(portOfShipment);
                    porshp = porshp.trim();
                  }
                  if ((boeIgmNo != null) && 
                    (boeIgmNo.getCellType() != 3))
                  {
                    igmNumber = formatter.formatCellValue(boeIgmNo);
                    igmNumber = igmNumber.trim();
                  }
                  if ((boeIgmDate != null) && 
                    (boeIgmDate.getCellType() != 3))
                  {
                    igmDate = boeIgmDate.toString().trim();
                    igmDate = igmDate.trim();
                  }
                  if ((boeMblNo != null) && 
                    (boeMblNo.getCellType() != 3))
                  {
                    mblNumber = formatter.formatCellValue(boeMblNo);
                    mblNumber = mblNumber.trim();
                  }
                  if ((boeMblDate != null) && 
                    (boeMblDate.getCellType() != 3)) {
                    mblDate = boeMblDate.toString().trim();
                  }
                  if ((boeHblNo != null) && 
                    (boeHblNo.getCellType() != 3))
                  {
                    hblNumber = formatter.formatCellValue(boeHblNo);
                    hblNumber = hblNumber.trim();
                  }
                  if ((boeHblDate != null) && 
                    (boeHblDate.getCellType() != 3)) {
                    hblDate = boeHblDate.toString().trim();
                  }
                  if ((invoiceSNo != null) && 
                    (invoiceSNo.getCellType() != 3))
                  {
                    invSNo = formatter.formatCellValue(invoiceSNo);
                    invSNo = invSNo.trim();
                  }
                  if ((invoiceNo != null) && 
                    (invoiceNo.getCellType() != 3))
                  {
                    invNo = formatter.formatCellValue(invoiceNo);
                    invNo = invNo.trim();
                  }
                  if ((termsInv != null) && 
                    (termsInv.getCellType() != 3))
                  {
                    termsofInvoice = formatter.formatCellValue(termsInv);
                    termsofInvoice = termsofInvoice.trim();
                  }
                  if ((invoiceAmt != null) && 
                    (invoiceAmt.getCellType() != 3))
                  {
                    invAmt = formatter.formatCellValue(invoiceAmt);
                    invAmt = invAmt.trim();
                  }
                  if ((invoiceCurr != null) && 
                    (invoiceCurr.getCellType() != 3))
                  {
                    invCurr = formatter.formatCellValue(invoiceCurr);
                    invCurr = invCurr.trim();
                  }
                  if ((freAmt != null) && 
                    (freAmt.getCellType() != 3))
                  {
                    freightAmount = formatter.formatCellValue(freAmt);
                    freightAmount = freightAmount.trim();
                  }
                  if ((freCurr != null) && 
                    (freCurr.getCellType() != 3))
                  {
                    freightCurrencyCode = formatter.formatCellValue(freCurr);
                    freightCurrencyCode = freightCurrencyCode.trim();
                  }
                  if ((insAmt != null) && 
                    (insAmt.getCellType() != 3))
                  {
                    insuranceAmount = formatter.formatCellValue(insAmt);
                    insuranceAmount = insuranceAmount.trim();
                  }
                  if ((insCurr != null) && 
                    (insCurr.getCellType() != 3))
                  {
                    insuranceCurrencyCode = formatter.formatCellValue(insCurr);
                    insuranceCurrencyCode = insuranceCurrencyCode.trim();
                  }
                  if ((agencyComm != null) && 
                    (agencyComm.getCellType() != 3))
                  {
                    agencyCommission = formatter.formatCellValue(agencyComm);
                    agencyCommission = agencyCommission.trim();
                  }
                  if ((agencyCurr != null) && 
                    (agencyCurr.getCellType() != 3))
                  {
                    agencyCurrency = formatter.formatCellValue(agencyCurr);
                    agencyCurrency = agencyCurrency.trim();
                  }
                  if ((disAmt != null) && 
                    (disAmt.getCellType() != 3))
                  {
                    discountCharges = formatter.formatCellValue(disAmt);
                    discountCharges = discountCharges.trim();
                  }
                  if ((disCurr != null) && 
                    (disCurr.getCellType() != 3))
                  {
                    discountCurrency = formatter.formatCellValue(disCurr);
                    discountCurrency = discountCurrency.trim();
                  }
                  if ((miscAmt != null) && 
                    (miscAmt.getCellType() != 3))
                  {
                    miscellaneousCharges = formatter.formatCellValue(miscAmt);
                    miscellaneousCharges = miscellaneousCharges.trim();
                  }
                  if ((miscCurr != null) && 
                    (miscCurr.getCellType() != 3))
                  {
                    miscellaneousCurrency = formatter.formatCellValue(miscCurr);
                    miscellaneousCurrency = miscellaneousCurrency.trim();
                  }
                  if ((suppName != null) && 
                    (suppName.getCellType() != 3))
                  {
                    supplierName = formatter.formatCellValue(suppName);
                    supplierName = supplierName.trim();
                  }
                  if ((suppAddr != null) && 
                    (suppAddr.getCellType() != 3))
                  {
                    supplierAddress = formatter.formatCellValue(suppAddr);
                    supplierAddress = supplierAddress.trim();
                  }
                  if ((suppCty != null) && 
                    (suppCty.getCellType() != 3))
                  {
                    supplierCountry = formatter.formatCellValue(suppCty);
                    supplierCountry = supplierCountry.trim();
                  }
                  if ((sellName != null) && 
                    (sellName.getCellType() != 3))
                  {
                    sellerName = formatter.formatCellValue(sellName);
                    sellerName = sellerName.trim();
                  }
                  if ((sellAddr != null) && 
                    (sellAddr.getCellType() != 3))
                  {
                    sellerAddress = formatter.formatCellValue(sellAddr);
                    sellerAddress = sellerAddress.trim();
                  }
                  if ((sellCty != null) && 
                    (sellCty.getCellType() != 3))
                  {
                    sellerCountry = formatter.formatCellValue(sellCty);
                    sellerCountry = sellerCountry.trim();
                  }
                  if ((thirdName != null) && 
                    (thirdName.getCellType() != 3))
                  {
                    thirdPartyName = formatter.formatCellValue(thirdName);
                    thirdPartyName = thirdPartyName.trim();
                  }
                  if ((thirdAddr != null) && 
                    (thirdAddr.getCellType() != 3))
                  {
                    ThirdPartyAddress = formatter.formatCellValue(thirdAddr);
                    ThirdPartyAddress = ThirdPartyAddress.trim();
                  }
                  if ((thirdCty != null) && 
                    (thirdCty.getCellType() != 3))
                  {
                    thirdPartyCountry = formatter.formatCellValue(thirdCty);
                    thirdPartyCountry = thirdPartyCountry.trim();
                  }
                  manbulkVO.setBoeNo(boeNo);
                  manbulkVO.setBoeDate(boeDate);
                  manbulkVO.setPortCode(portCode);
                  manbulkVO.setIeCode(ieCode);
                  manbulkVO.setAdCode(adCode);
                  manbulkVO.setIgmNo(igmNumber);
                  manbulkVO.setIgmDate(igmDate);
                  manbulkVO.setHblNo(hblNumber);
                  manbulkVO.setHblDate(hblDate);
                  manbulkVO.setMblNo(mblNumber);
                  manbulkVO.setMblDate(mblDate);
                  manbulkVO.setImAgency(impagc);

       
       
                  manbulkVO.setRecInd(RecordInd);
                  manbulkVO.setGovprv(govprv);

       
       
       
       
                  manbulkVO.setPos(porshp);
                  manbulkVO.setInvoiceSerialNo(invSNo);
                  manbulkVO.setInvoiceNo(invNo);
                  manbulkVO.setTermsofInvoice(termsofInvoice);
                  manbulkVO.setInvoiceAmt(invAmt);
                  manbulkVO.setInvoiceCurrency(invCurr);
                  manbulkVO.setFreightAmount(freightAmount);
                  manbulkVO.setFreightCurrencyCode(freightCurrencyCode);
                  manbulkVO.setInsuranceAmount(insuranceAmount);
                  manbulkVO.setInsuranceCurrencyCode(insuranceCurrencyCode);
                  manbulkVO.setAgencyCommission(agencyCommission);
                  manbulkVO.setAgencyCurrency(agencyCurrency);
                  manbulkVO.setDiscountCharges(discountCharges);
                  manbulkVO.setDiscountCurrency(discountCurrency);
                  manbulkVO.setMiscellaneousCharges(miscellaneousCharges);
                  manbulkVO.setMiscellaneousCurrency(miscellaneousCurrency);
                  manbulkVO.setSupplierName(supplierName);
                  manbulkVO.setSupplierAddress(supplierAddress);
                  manbulkVO.setSupplierCountry(supplierCountry);
                  manbulkVO.setSellerName(sellerName);
                  manbulkVO.setSellerAddress(sellerAddress);
                  manbulkVO.setSellerCountry(sellerCountry);
                  manbulkVO.setThirdPartyName(thirdPartyName);
                  manbulkVO.setThirdPartyAddress(ThirdPartyAddress);
                  manbulkVO.setThirdPartyCountry(thirdPartyCountry);
                  manualBOEList.add(manbulkVO);
                }
                logger.info("outside----" + i);
                i++;
              }
              logger.info("boelist size---" + manualBOEList.size());
              logger.info("i----" + i);
              if ((manualBOEList != null) && 
                (manualBOEList.size() > 0))
              {
                ArrayList<TransactionVO> errorData = validation1.valBulkUpload(manualBOEList);
                if ((errorData == null) || (errorData.size() == 0))
                {
                  batchId = insertExcelData(manualBOEList);
                  insertedRowCount = validateExcel(batchId);
                  if (insertedRowCount > 0)
                  {
                    logger.info("---" + insertedRowCount);
                    ArrayList<TransactionVO> InvoiceList1 = getBulkStgData(batchId);
                    trans.setBoeList(InvoiceList1);
                    trans.setBatchId(batchId);
                  }
                }
                else
                {
                  trans.setErrorCodeDesc(errorData);
                  trans.setManualBoevoList(manualBOEList);
                  logger.info("trans.getManualBoevoList().size() :" + trans.getManualBoevoList().size());
                }
              }
            }
            if ((excelErrorList != null) && (excelErrorList.size() > 0)) {
              trans.setErrorList(excelErrorList);
            }
          }
          catch (Exception exception)
          {
            exception.printStackTrace();
            throwDAOException(exception);
          }
          logger.info("Exiting Method");
          return trans;
        }
        public String getSequenceOfColumns()
        {
          return "BillOfEntryNumber|BillOfEntryDate|PORTCODE|IECODE|ADCODE|IGMNo|IGMDate|HAWB-HBLNo|HAWB-HBLDate|MAWB-MBLNo|MAWB-MBLDate|ImportAgency|RecordIndicator|G-P|PortOfShipment|InvoiceSerialNo|InvoiceNo|TermsOfServices|InvoiceAmount|InvoiceCurrency|FreightAmount|FreightCurrencyCode|InsuranceAmount|InsuranceCurrencyCode|AgencyCommission|AgencyCurrency|DiscountCharges|DiscountCurrency|MiscellaneousCharges|MiscellaneousCurrency|SupplierName|SupplierAddress|SupplierCountry|SellerName|SellerAddress|SellerCountry|ThirdPartyName|ThirdPartyAddress|ThirdPartyCountry";
        }
        private int validateExcel(String batchId)
        {
          logger.info("Entering Method");
          int insertedRowCount = 0;
          ResultSet aResultSet = null;
          Connection aConnection = null;
          CallableStatement aCallableStatement = null;
          try
          {
            aConnection = DBConnectionUtility.getConnection();
            if (aConnection != null)
            {
              String procedureQuery = "{call ETT_BULK_MANUAL_BOE_VAL_DATA(?)}";
              aCallableStatement = aConnection.prepareCall(procedureQuery);
              aCallableStatement.setString(1, batchId);
              insertedRowCount = aCallableStatement.executeUpdate();
            }
          }
          catch (SQLException ex)
          {
            ex.printStackTrace();
          }
          catch (Exception ex)
          {
            ex.printStackTrace();
          }
          finally
          {
            DBConnectionUtility.surrenderDB(aConnection, aCallableStatement, aResultSet);
          }
          logger.info("Exiting Method");
          return insertedRowCount;
        }
        public String insertExcelData(ArrayList<ManualBOEBulkUploadVO> manualBOEList)
          throws DAOException
        {
          logger.info("Entering Method");
          Connection con = null;
          ResultSet rs = null;
          PreparedStatement ppt = null;
          ManualBOEBulkUploadVO excelDataVO = null;
          String batchId = null;
          CommonMethods commonMethods = null;
          try
          {
            con = DBConnectionUtility.getConnection();
            commonMethods = new CommonMethods();
            HttpSession session = ServletActionContext.getRequest().getSession();
            String userId = (String)session.getAttribute("loginedUserId");
            String query = "SELECT ETT_BULK_MANUAL_BOE_SEQ_NO AS BATCHID FROM DUAL";
            LoggableStatement pst = new LoggableStatement(con, query);
            ResultSet rs1 = pst.executeQuery();
            if (rs1.next()) {
              batchId = rs1.getString("BATCHID");
            }
            pst.close();
            logger.info("batch id" + batchId);
            ppt = con.prepareStatement("INSERT INTO ETT_BULK_MAUNAL_BOE_STG_DAT(BOE_NUMBER,BOE_DATE,BOE_PORT_OF_DISCHARGE,BOE_IMPORT_AGENCY,BOE_AD_CODE,BOE_GP,BOE_IE_CODE,BOE_IE_NAME,BOE_IE_ADDRESS,BOE_IE_PANNUMBER,BOE_PORT_OF_SHIPMENT,BOE_RECORD_INDICATOR,BOE_MAWB_MBLNUMBER,BOE_MAWB_MBLDATE,BOE_HAWB_HBLNUMBER,BOE_HAWB_HBLDATE,BOE_IGMNUMBER,BOE_IGMDATE,INVOICE_SERIAL_NUMBER,INVOICE_NUMBER,INVOICE_TERMS_OF_INVOICE,INVOICE_SUPPLIER_NAME,INVOICE_SUPPLIER_ADDRESS,INVOICE_SUPPLIER_COUNTRY,INVOICE_SELLER_NAME,INVOICE_SELLER_ADDRESS,INVOICE_SELLER_COUNTRY,INVOICE_FOBAMOUNT,INVOICE_FOBCURRENCY,INVOICE_FRIEGHTAMOUNT,INVOICE_FRIEGHTCURRENCYCODE,INVOICE_INSURANCEAMOUNT,INVOICE_INSURANCECURRENCY_CODE,INVOICE_AGENCY_COMMISSION,INVOICE_AGENCY_CURRENCY,INVOICE_DISCOUNT_CHARGES,INVOICE_DISCOUNT_CURRENCY,INVOICE_MISCELLANEOUS_CHARGES,INVOICE_MISCELLANEOUS_CURRENCY,INVOICE_THIRDPARTY_NAME,INVOICE_THIRDPARTY_ADDRESS,INVOICE_THIRDPARTY_COUNTRY,BATCHID,USERID,CIF_NO)VALUES (?,TO_DATE(?,'DD/MM/YY'),?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?,'DD/MM/YY'),?,TO_DATE(?,'DD/MM/YY'),?,TO_DATE(?,'DD/MM/YY'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            for (int i = 0; i < manualBOEList.size(); i++)
            {
              excelDataVO = new ManualBOEBulkUploadVO();
              excelDataVO = (ManualBOEBulkUploadVO)manualBOEList.get(i);
              if ((!commonMethods.isNull(excelDataVO.getBoeNo())) && (!commonMethods.isNull(excelDataVO.getBoeDate())) && 
                (!commonMethods.isNull(excelDataVO.getPortCode())) && (!commonMethods.isNull(excelDataVO.getInvoiceSerialNo())) && 
                (!commonMethods.isNull(excelDataVO.getInvoiceNo())))
              {
                excelDataVO = fetchCIFDetails(excelDataVO);
                ppt.setString(1, excelDataVO.getBoeNo());
                ppt.setString(2, excelDataVO.getBoeDate());
                logger.info("boe date---" + excelDataVO.getBoeDate());
                ppt.setString(3, excelDataVO.getPortCode());
                int imAgency = (int)Double.parseDouble(excelDataVO.getImAgency());
                ppt.setString(4, String.valueOf(imAgency));
                ppt.setString(5, excelDataVO.getAdCode());
                int gp = (int)Double.parseDouble(excelDataVO.getGovprv());
                ppt.setString(6, String.valueOf(gp));
                ppt.setString(7, excelDataVO.getIeCode());
                ppt.setString(8, excelDataVO.getIename());
                ppt.setString(9, excelDataVO.getIeadd());
                ppt.setString(10, excelDataVO.getIepan());
                ppt.setString(11, excelDataVO.getPos());
                ppt.setString(12, excelDataVO.getRecInd());
                ppt.setString(13, excelDataVO.getMblNo());
                ppt.setString(14, excelDataVO.getMblDate());
                logger.info("mbl date---" + excelDataVO.getMblDate());
                ppt.setString(15, excelDataVO.getHblNo());
                ppt.setString(16, excelDataVO.getHblDate());
                logger.info("hbl date---" + excelDataVO.getHblDate());
                ppt.setString(17, excelDataVO.getIgmNo());
                ppt.setString(18, excelDataVO.getIgmDate());
                logger.info("igm date---" + excelDataVO.getIgmDate());
                ppt.setString(19, excelDataVO.getInvoiceSerialNo());
                ppt.setString(20, excelDataVO.getInvoiceNo());
                ppt.setString(21, excelDataVO.getTermsofInvoice());
                ppt.setString(22, excelDataVO.getSupplierName());
                ppt.setString(23, excelDataVO.getSupplierAddress());
                ppt.setString(24, excelDataVO.getSupplierCountry());
                ppt.setString(25, excelDataVO.getSellerName());
                ppt.setString(26, excelDataVO.getSellerAddress());
                ppt.setString(27, excelDataVO.getSellerCountry());
                ppt.setString(28, excelDataVO.getInvoiceAmt());
                ppt.setString(29, excelDataVO.getInvoiceCurrency());
                ppt.setString(30, excelDataVO.getFreightAmount());
                ppt.setString(31, excelDataVO.getFreightCurrencyCode());
                ppt.setString(32, excelDataVO.getInsuranceAmount());
                ppt.setString(33, excelDataVO.getInsuranceCurrencyCode());
                ppt.setString(34, excelDataVO.getAgencyCommission());
                ppt.setString(35, excelDataVO.getAgencyCurrency());
                ppt.setString(36, excelDataVO.getDiscountCharges());
                ppt.setString(37, excelDataVO.getDiscountCurrency());
                ppt.setString(38, excelDataVO.getMiscellaneousCharges());
                ppt.setString(39, excelDataVO.getMiscellaneousCurrency());
                ppt.setString(40, excelDataVO.getThirdPartyName());
                ppt.setString(41, excelDataVO.getThirdPartyAddress());
                ppt.setString(42, excelDataVO.getThirdPartyCountry());
                ppt.setString(43, batchId);
                ppt.setString(44, userId);
                ppt.setString(45, excelDataVO.getCifNo());
                ppt.addBatch();
              }
            }
            logger.info("Insert Record ");
            ppt.executeBatch();
            logger.info("Inserted Record Successfully");
          }
          catch (Exception exception)
          {
            exception.printStackTrace();
            throwDAOException(exception);
          }
          finally
          {
            closeSqlRefferance(rs, ppt, con);
          }
          logger.info("Exiting Method");
          return batchId;
        }
        public ManualBOEBulkUploadVO fetchCIFDetails(ManualBOEBulkUploadVO boeVO)
          throws DAOException
        {
          logger.info("Entering Method");
          Connection con = null;
          LoggableStatement pst = null;
          ResultSet rs = null;
          String sqlQuery = null;
          CommonMethods commonMethods = null;
          try
          {
            commonMethods = new CommonMethods();
            con = DBConnectionUtility.getConnection();
            sqlQuery = "SELECT TRIM(EXT.CUST) AS CUST,TRIM(GF.GFCUN) AS CUST_NAME,TRIM(SX.SVNA1) AS IE_ADDR1,TRIM(SX.SVNA2) AS IE_ADDR2,TRIM(SX.SVNA3) AS IE_ADDR3,TRIM(SX.SVNA4) AS IE_ADDR4,TRIM(SX.SVNA5) AS IE_ADDR5,TRIM(EXT.PANNO) AS PANNO FROM EXTCUST EXT,GFPF GF,SX20LF SX WHERE EXT.CUST = SX.SXCUS1 AND EXT.CUST = GF.GFCUS1 AND EXT.IECODE = ?";

       
       
            pst = new LoggableStatement(con, sqlQuery);
            pst.setString(1, commonMethods.getEmptyIfNull(boeVO.getIeCode()).trim());
            rs = pst.executeQuery();
            if (rs.next())
            {
              boeVO.setIename(rs.getString("CUST_NAME"));
              boeVO.setIepan(rs.getString("PANNO"));
              boeVO.setCifNo(rs.getString("CUST"));
              String ieAddr1 = commonMethods.getEmptyIfNull(rs.getString("IE_ADDR1")).trim();
              String ieAddr2 = commonMethods.getEmptyIfNull(rs.getString("IE_ADDR2")).trim();
              String ieAddr3 = commonMethods.getEmptyIfNull(rs.getString("IE_ADDR3")).trim();
              String ieAddr4 = commonMethods.getEmptyIfNull(rs.getString("IE_ADDR4")).trim();
              String ieAddr5 = commonMethods.getEmptyIfNull(rs.getString("IE_ADDR5")).trim();
              String ieAddr = ieAddr1 + " " + ieAddr2 + " " + ieAddr3 + " " + ieAddr4 + " " + ieAddr5;
              boeVO.setIeadd(ieAddr);
            }
          }
          catch (SQLException e)
          {
            e.printStackTrace();
          }
          finally
          {
            closeSqlRefferance(rs, pst, con);
          }
          logger.info("Exiting Method");
          return boeVO;
        }
        public ArrayList<TransactionVO> getBulkStgData(String batchId)
          throws DAOException
        {
          logger.info("Entering Method");
          Connection con = null;
          LoggableStatement pst = null;
          ResultSet rs = null;
          String sqlQuery = null;
          CommonMethods commonMethods = null;
          ArrayList<TransactionVO> invoiceList = null;
          SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
          try
          {
            commonMethods = new CommonMethods();
            invoiceList = new ArrayList();
            con = DBConnectionUtility.getConnection();
            sqlQuery = "select * from ETT_BULK_MAUNAL_BOE_STG_DAT where BATCHID =?";
            pst = new LoggableStatement(con, sqlQuery);
            pst.setString(1, batchId);
            rs = pst.executeQuery();
            while (rs.next())
            {
              TransactionVO boe = new TransactionVO();
              boe.setBoeNo(rs.getString("BOE_NUMBER"));
              boe.setBoeDate(sdf.format(rs.getDate("BOE_DATE")));
              boe.setPortCode(rs.getString("BOE_PORT_OF_DISCHARGE"));
              boe.setImpagc(rs.getString("BOE_IMPORT_AGENCY"));
              boe.setAdcode(rs.getString("BOE_AD_CODE"));
              boe.setGovprv(rs.getString("BOE_GP"));
              boe.setIeCode(rs.getString("BOE_IE_CODE"));

       
       
              boe.setRecordInd(rs.getString("BOE_RECORD_INDICATOR"));
              boe.setPorshp(rs.getString("BOE_PORT_OF_SHIPMENT"));
              boe.setMblNumber(rs.getString("BOE_MAWB_MBLNUMBER"));
              if (rs.getDate("BOE_MAWB_MBLDATE") != null) {
                boe.setMblDate(sdf.format(rs.getDate("BOE_MAWB_MBLDATE")));
              }
              boe.setHblNumber(rs.getString("BOE_HAWB_HBLNUMBER"));
              if (rs.getDate("BOE_HAWB_HBLDATE") != null) {
                boe.setHblDate(sdf.format(rs.getDate("BOE_HAWB_HBLDATE")));
              }
              boe.setIgmNumber(rs.getString("BOE_IGMNUMBER"));
              if (rs.getDate("BOE_IGMDATE") != null) {
                boe.setIgmDate(sdf.format(rs.getDate("BOE_IGMDATE")));
              }
              boe.setInvSno(rs.getString("INVOICE_SERIAL_NUMBER"));
              boe.setInvNo(rs.getString("INVOICE_NUMBER"));
              boe.setTermsofInvoice(rs.getString("INVOICE_TERMS_OF_INVOICE"));
              boe.setSupplierName(rs.getString("INVOICE_SUPPLIER_NAME"));
              boe.setSupplierAddress(rs.getString("INVOICE_SUPPLIER_ADDRESS"));
              boe.setSupplierCountry(rs.getString("INVOICE_SUPPLIER_COUNTRY"));
              boe.setSellerName(rs.getString("INVOICE_SELLER_NAME"));
              boe.setSellerAddress(rs.getString("INVOICE_SELLER_ADDRESS"));
              boe.setSellerCountry(rs.getString("INVOICE_SELLER_COUNTRY"));
              boe.setInvAmt(rs.getString("INVOICE_FOBAMOUNT"));
              boe.setInvCurr(rs.getString("INVOICE_FOBCURRENCY"));
              boe.setInsuranceAmount(rs.getString("INVOICE_INSURANCEAMOUNT"));
              boe.setInsuranceCurrencyCode(rs.getString("INVOICE_INSURANCECURRENCY_CODE"));
              boe.setFreightAmount(rs.getString("INVOICE_FRIEGHTAMOUNT"));
              boe.setFreightCurrencyCode(rs.getString("INVOICE_FRIEGHTCURRENCYCODE"));
              boe.setAgencyCommission(rs.getString("INVOICE_AGENCY_COMMISSION"));
              boe.setAgencyCurrency(rs.getString("INVOICE_AGENCY_CURRENCY"));
              boe.setDiscountCharges(rs.getString("INVOICE_DISCOUNT_CHARGES"));
              boe.setDiscountCurrency(rs.getString("INVOICE_DISCOUNT_CURRENCY"));
              boe.setMiscellaneousCharges(rs.getString("INVOICE_MISCELLANEOUS_CHARGES"));
              boe.setMiscellaneousCurrency(rs.getString("INVOICE_MISCELLANEOUS_CURRENCY"));
              boe.setThirdPartyName(rs.getString("INVOICE_THIRDPARTY_NAME"));
              boe.setThirdPartyAddress(rs.getString("INVOICE_THIRDPARTY_ADDRESS"));
              boe.setThirdPartyCountry(rs.getString("INVOICE_THIRDPARTY_COUNTRY"));
              boe.setErrDesc(rs.getString("ERRORDTLS"));
              if (commonMethods.isNull(boe.getErrDesc())) {
                boe.setStatus("SUCCESS");
              } else {
                boe.setStatus("FAIL");
              }
              logger.info("----" + boe);
              invoiceList.add(boe);
            }
          }
          catch (SQLException e)
          {
            e.printStackTrace();
          }
          finally
          {
            closeSqlRefferance(rs, pst, con);
          }
          logger.info("Exiting Method");
          return invoiceList;
        }
        public int rejectData(String batchId)
        	    throws DAOException
        	  {
        	    logger.info("Entering Method");
        	    Connection con = null;
        	    LoggableStatement pst = null;
        	    int delCount = 0;
        	    try
        	    {
        	      con = DBConnectionUtility.getConnection();
        	      String delQuery = "DELETE FROM ETT_BULK_MAUNAL_BOE_STG_DAT where BATCHID =?";
        	      LoggableStatement pst1 = new LoggableStatement(con, delQuery);
        	      pst1.setString(1, batchId);
        	      delCount = pst1.executeUpdate();
        	      logger.info("Delete Count --->" + delCount);
        	      closePreparedStatement(pst1);
        	    }
        	    catch (Exception e)
        	    {
        	      e.printStackTrace();
        	      throwDAOException(e);
        	    }
        	    finally
        	    {
        	      closeSqlRefferance(pst, con);
        	    }
        	    logger.info("Exiting Method");
        	    return delCount;
        	  }
        	  public int uploadDataToTable(String batchId)
        	    throws SQLException
        	  {
        	    logger.info("Entering Method");
        	    ResultSet aResultSet = null;
        	    Connection aConnection = null;
        	    CallableStatement aCallableStatement = null;
        	    LoggableStatement pst = null;
        	    int count = 0;
        	    try
        	    {
        	      aConnection = DBConnectionUtility.getConnection();
        	      if (aConnection != null)
        	      {
        	        String sqlQuery = "SELECT COUNT(*) FROM ETT_BULK_MAUNAL_BOE_STG_DAT WHERE BATCHID = ? AND ERRORDTLS IS NULL ";
        	        pst = new LoggableStatement(aConnection, sqlQuery);
        	        pst.setString(1, batchId);
        	        aResultSet = pst.executeQuery();
        	        if (aResultSet.next()) {
        	          count = aResultSet.getInt(1);
        	        }
        	        if (count > 0)
        	        {
        	          String procedureQuery = "{call ETT_BULK_MANUAL_BOE_INS_TBL(?)}";
        	          aCallableStatement = aConnection.prepareCall(procedureQuery);
        	          aCallableStatement.setString(1, batchId);
        	          aCallableStatement.executeUpdate();
        	        }
        	      }
        	    }
        	    catch (SQLException ex)
        	    {
        	      ex.printStackTrace();
        	    }
        	    catch (Exception ex)
        	    {
        	      ex.printStackTrace();
        	    }
        	    finally
        	    {
        	      DBConnectionUtility.surrenderDB(aConnection, aCallableStatement, aResultSet);
        	      pst.close();
        	    }
        	    logger.info("Exiting Method");
        	    return count;
        	  }
        	}