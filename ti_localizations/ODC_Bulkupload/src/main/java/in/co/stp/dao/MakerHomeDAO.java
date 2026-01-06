package in.co.stp.dao;

import com.misys.tiplus2.services.control.ServiceResponse;
import com.misys.tiplus2.services.control.ServiceResponse.ResponseHeader;
import com.misys.tiplus2.services.control.StatusEnum;
import com.opensymphony.xwork2.ActionContext;
import in.co.stp.action.TIPlusEJBClient;
import in.co.stp.businessdelegate.MessageUtil;
import in.co.stp.dao.exception.DAOException;
import in.co.stp.utility.ActionConstants;
import in.co.stp.utility.ActionConstantsQuery;
import in.co.stp.utility.CommonMethods;
import in.co.stp.utility.DBConnectionUtility;
import in.co.stp.utility.LoggableStatement;
import in.co.stp.vo.AlertMessagesVO;
import in.co.stp.vo.EQ3Posting;
import in.co.stp.vo.ExcelDataVO;
import in.co.stp.vo.MakerHomeVO;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.rmi.RemoteException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.struts2.ServletActionContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MakerHomeDAO
  extends AbstractDAO
  implements ActionConstantsQuery, ActionConstants
{
  static MakerHomeDAO dao;
  private static Logger logger = LogManager.getLogger(MakerHomeDAO.class.getName());
  private ArrayList<AlertMessagesVO> alertMsgArray = new ArrayList();
 
  public static MakerHomeDAO getDAO()
  {
    if (dao == null) {
      dao = new MakerHomeDAO();
    }
    return dao;
  }
 
  public void setDate()
    throws DAOException
  {
    logger.info("Entering Method");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    try
    {
      CommonMethods commonMethods = null;
      commonMethods = new CommonMethods();
      Map<String, Object> session = ActionContext.getContext().getSession();
     
      con = DBConnectionUtility.getConnection();
     
      String query = "SELECT TO_CHAR(TO_DATE(PROCDATE, 'dd-mm-yy'),'dd-mm-yyyy') AS PROCDATE FROM dlyprccycl";
      logger.info("Date Query" + query);
      pst = new LoggableStatement(con, query);
      rs = pst.executeQuery();
      logger.info("query" + rs);
      if (rs.next()) {
        session.put("processDate", commonMethods.getEmptyIfNull(rs.getString("PROCDATE")).trim());
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
 
  public void isSessionAvailable()
    throws DAOException
  {
    logger.info("Entering Method");
    String sessionUserName = null;
    String userId = null;
    try
    {
      HttpSession session = ServletActionContext.getRequest().getSession();
      logger.info("RemoteUserNameBefore Login" + sessionUserName);
      logger.info("RemoteUserNameBefore Login" + sessionUserName);
      HttpServletRequest request = (HttpServletRequest)ActionContext.getContext()
        .get("com.opensymphony.xwork2.dispatcher.HttpServletRequest");
      sessionUserName = request.getRemoteUser();
      logger.info("RemoteUserNameAfter Login" + sessionUserName);
      logger.info("RemoteUserNameAfter Login" + sessionUserName);
      if (sessionUserName == null) {
        sessionUserName = "SUPERVISOR";
      }
      logger.info("RemoteUserNameAfter session Login :" + sessionUserName);
      userId = getUserId(sessionUserName);
      logger.info("RemoteUserNameAfter session Login Id :" + userId);
      session.setAttribute("loginedUserName", userId);
    }
    catch (Exception exception)
    {
      throwDAOException(exception);
    }
    logger.info("Exiting Method");
  }
 
  public String getUserId(String userName)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement pst = null;
    String userId = null;
    ResultSet rs = null;
    try
    {
      con = DBConnectionUtility.getConnection();
      String getUserId = "select skey80 from secage88 where name85 ='" + userName + "'";
      logger.info("user id" + getUserId);
      pst = new LoggableStatement(con, getUserId);
      rs = pst.executeQuery();
      if (rs.next()) {
        userId = String.valueOf(rs.getInt("SKEY80"));
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
    return userId;
  }
 
  public String getBranchListQuery(MakerHomeVO makervo)
  {
    CommonMethods commonMethods = null;
    String SCF_QUERY = "select * FROM CAPF WHERE BRANCHENT=BRANCHENT AND CABRNM!='IDFC' ";
    commonMethods = new CommonMethods();
   















    return SCF_QUERY;
  }
 
  public ArrayList<MakerHomeVO> getBranchList(MakerHomeVO makervo)
    throws DAOException
  {
    logger.info("Entering Method");
    LoggableStatement pst = null;
    ResultSet rs = null;
    Connection con = null;
    CommonMethods commonMethods = null;
    ArrayList<MakerHomeVO> branchList = null;
    try
    {
      con = DBConnectionUtility.getConnection();
      commonMethods = new CommonMethods();
      branchList = new ArrayList();
      String query = getBranchListQuery(makervo);
      pst = new LoggableStatement(con, query);
      rs = pst.executeQuery();
      while (rs.next()) {}
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
    return branchList;
  }
 
  public MakerHomeVO getExcelLoad(MakerHomeVO makervo)
    throws DAOException, IOException
  {
    logger.info("Entering Method");
    logger.info("Start Loading - " + new Date());
    String tempError1 = null;
    ArrayList<ExcelDataVO> odcDataList = null;
   








    int i = 1;
    HSSFWorkbook workbook = null;
    ExcelDataVO excelDataVO = null;
    try
    {
      File filename = makervo.getInputFile();
      odcDataList = new ArrayList();
      if (filename != null)
      {
        FileInputStream file = new FileInputStream(filename);
       
        workbook = new HSSFWorkbook(file);
        HSSFSheet sheet = workbook.getSheetAt(0);
       
        logger.info("Exel Read 2");
        Iterator<Row> rows = sheet.rowIterator();
        logger.info("Exel Read 4");
       
        logger.info("Exel Read 4");
        Connection con = DBConnectionUtility.getConnection();
        while (rows.hasNext())
        {
          String tempError = null;
          String errordesc = "";String status = "";String theirref = "";String drawee_name_address = "";
          String drawee_country = "";String behalfofbranch = "";String drawer = "";String draweecustomerid = "";
          String collectionamount = "";String collectioncurrency = "";String shipmenttocountry = "";
          String shipmentfromcountry = "";String hscode = "";String incoterms = "";String goodscode = "";
          String gooddescription = "";String portofdestination = "";String portofloading = "";
          String transportdocno = "";String transportdocdate = "";String invoiceno = "";String invoicedate = "";
          String form_type = "";String shpbill_no = "";String bill_date = "";String port_code = "";
          String form_no = "";String shp_amt = "";String shp_currency = "";String repamt_wrt_amt = "";
          String shortcollectionamount = "";String record_indicator = "";String leo_date = "";
          String export_agency = "";String export_type = "";String destination_countrry = "";String iecode = "";
          String adcode = "";String customs_number = "";String shp_inv_sno = "";String shp_invoice_no = "";
          String shp_invoice_date = "";String fob_amt = "";String freight_amt = "";String ins_amt = "";
          String comm_amt = "";String discount_amt = "";String deduction_amt = "";String package_amt = "";
          String remittance_adcode = "";String remittance_num = "";String firc_no = "";String remitter_name = "";
          String remitter_date = "";String remitter_country = "";String utilization_amount = "";
          String cif_number = "";String remitance_amount = "";String reason_short_shp_amt = "";
          String short_shp_amt = "";String charge_debit_ac_no = "";String collecting_bank = "";
          HSSFRow rowObject;
          if (i == 1)
          {
            logger.info("Entered Header part");
            rowObject = (HSSFRow)rows.next();
          }
          else
          {
            excelDataVO = new ExcelDataVO();
            logger.info("Entered Else Part");
            HSSFRow rowObject = (HSSFRow)rows.next();
            HSSFCell THEIRREF = rowObject.getCell(0);
            HSSFCell COLLECTING_BANK = rowObject.getCell(1);
            HSSFCell BEHALFOFBRANCH = rowObject.getCell(2);
            HSSFCell DRAWER = rowObject.getCell(3);
            HSSFCell DRAWEECUSTOMERID = rowObject.getCell(4);
            HSSFCell DRAWEE_NAME_ADDRESS = rowObject.getCell(5);
            HSSFCell DRAWEE_COUNTRY = rowObject.getCell(6);
            HSSFCell CHARGE_DEBIT_AC_NO = rowObject.getCell(7);
            HSSFCell COLLECTIONAMOUNT = rowObject.getCell(8);
            HSSFCell COLLECTIONCURRENCY = rowObject.getCell(9);
            HSSFCell SHIPMENTTOCOUNTRY = rowObject.getCell(10);
            HSSFCell SHIPMENTFROMCOUNTRY = rowObject.getCell(11);
            HSSFCell HSCODE = rowObject.getCell(12);
            HSSFCell INCOTERMS = rowObject.getCell(13);
            HSSFCell GOODSCODE = rowObject.getCell(14);
            HSSFCell GOODDESCRIPTION = rowObject.getCell(15);
            HSSFCell PORTOFDESTINATION = rowObject.getCell(16);
            HSSFCell PORTOFLOADING = rowObject.getCell(17);
            HSSFCell TRANSPORTDOCNO = rowObject.getCell(18);
            HSSFCell TRANSPORTDOCDATE = rowObject.getCell(19);
            HSSFCell INVOICENO = rowObject.getCell(20);
            HSSFCell INVOICEDATE = rowObject.getCell(21);
            HSSFCell FORM_TYPE = rowObject.getCell(22);
            HSSFCell SHPBILL_NO = rowObject.getCell(23);
            HSSFCell BILL_DATE = rowObject.getCell(24);
            HSSFCell PORT_CODE = rowObject.getCell(25);
            HSSFCell FORM_NO = rowObject.getCell(26);
            HSSFCell SHP_AMT = rowObject.getCell(27);
            HSSFCell SHP_CURRENCY = rowObject.getCell(28);
            HSSFCell REASON_SHORT_SHP_AMT = rowObject.getCell(29);
            HSSFCell SHORT_SHP_AMT = rowObject.getCell(30);
            HSSFCell REPAMT_WRT_AMT = rowObject.getCell(31);
            HSSFCell SHORTCOLLECTIONAMOUNT = rowObject.getCell(32);
            HSSFCell REMITTANCE_NUM = rowObject.getCell(33);
            HSSFCell FIRC_NO = rowObject.getCell(34);
            HSSFCell REMITANCE_AMOUNT = rowObject.getCell(35);
            HSSFCell UTILIZATION_AMOUNT = rowObject.getCell(36);
            HSSFCell REMITTANCE_ADCODE = rowObject.getCell(37);
            HSSFCell REMITTER_NAME = rowObject.getCell(38);
            HSSFCell REMITTER_DATE = rowObject.getCell(39);
            HSSFCell REMITTER_COUNTRY = rowObject.getCell(40);
            HSSFCell CIF_NUMBER = rowObject.getCell(41);
            HSSFCell LEO_DATE = rowObject.getCell(42);
            HSSFCell EXPORT_AGENCY = rowObject.getCell(43);
            HSSFCell EXPORT_TYPE = rowObject.getCell(44);
            HSSFCell DESTINATION_COUNTRRY = rowObject.getCell(45);
            HSSFCell IECODE = rowObject.getCell(46);
            HSSFCell ADCODE = rowObject.getCell(47);
            HSSFCell CUSTOMS_NUMBER = rowObject.getCell(48);
            HSSFCell SHP_INV_SNO = rowObject.getCell(49);
            HSSFCell SHP_INVOICE_NO = rowObject.getCell(50);
            HSSFCell SHP_INVOICE_DATE = rowObject.getCell(51);
            HSSFCell FOB_AMT = rowObject.getCell(52);
            HSSFCell FREIGHT_AMT = rowObject.getCell(53);
            HSSFCell INS_AMT = rowObject.getCell(54);
            HSSFCell COMM_AMT = rowObject.getCell(55);
            HSSFCell DISCOUNT_AMT = rowObject.getCell(56);
            HSSFCell DEDUCTION_AMT = rowObject.getCell(57);
            HSSFCell PACKAGE_AMT = rowObject.getCell(58);
            HSSFCell RECORD_INDICATOR = rowObject.getCell(59);
            if ((DRAWEE_COUNTRY != null) && (DRAWEE_COUNTRY.getCellType() != 3))
            {
              DRAWEE_COUNTRY.setCellType(1);
              drawee_country = DRAWEE_COUNTRY.toString().trim();
            }
            if ((COLLECTING_BANK != null) && (COLLECTING_BANK.getCellType() != 3))
            {
              COLLECTING_BANK.setCellType(1);
              collecting_bank = COLLECTING_BANK.toString().trim();
            }
            if ((DRAWEE_NAME_ADDRESS != null) && (DRAWEE_NAME_ADDRESS.getCellType() != 3))
            {
              DRAWEE_NAME_ADDRESS.setCellType(1);
              drawee_name_address = DRAWEE_NAME_ADDRESS.toString().trim();
            }
            if ((CHARGE_DEBIT_AC_NO != null) && (CHARGE_DEBIT_AC_NO.getCellType() != 3))
            {
              CHARGE_DEBIT_AC_NO.setCellType(1);
              charge_debit_ac_no = CHARGE_DEBIT_AC_NO.toString().trim();
            }
            if ((REASON_SHORT_SHP_AMT != null) && (REASON_SHORT_SHP_AMT.getCellType() != 3))
            {
              REASON_SHORT_SHP_AMT.setCellType(1);
              reason_short_shp_amt = REASON_SHORT_SHP_AMT.toString().trim();
            }
            if ((SHORT_SHP_AMT != null) && (SHORT_SHP_AMT.getCellType() != 3))
            {
              SHORT_SHP_AMT.setCellType(1);
              short_shp_amt = SHORT_SHP_AMT.toString().trim();
            }
            if ((THEIRREF != null) && (THEIRREF.getCellType() != 3))
            {
              THEIRREF.setCellType(1);
              theirref = THEIRREF.toString().trim();
            }
            if ((BEHALFOFBRANCH != null) && (BEHALFOFBRANCH.getCellType() != 3))
            {
              BEHALFOFBRANCH.setCellType(1);
              behalfofbranch = BEHALFOFBRANCH.toString().trim();
            }
            if ((DRAWER != null) && (DRAWER.getCellType() != 3))
            {
              DRAWER.setCellType(1);
              drawer = DRAWER.toString().trim();
            }
            if ((DRAWEECUSTOMERID != null) && (DRAWEECUSTOMERID.getCellType() != 3))
            {
              DRAWEECUSTOMERID.setCellType(1);
              draweecustomerid = DRAWEECUSTOMERID.toString().trim();
            }
            if ((COLLECTIONAMOUNT != null) && (COLLECTIONAMOUNT.getCellType() != 3))
            {
              COLLECTIONAMOUNT.setCellType(1);
              collectionamount = COLLECTIONAMOUNT.toString().trim();
            }
            if ((COLLECTIONCURRENCY != null) && (COLLECTIONCURRENCY.getCellType() != 3))
            {
              COLLECTIONCURRENCY.setCellType(1);
              collectioncurrency = COLLECTIONCURRENCY.toString().trim();
            }
            if ((SHIPMENTTOCOUNTRY != null) && (SHIPMENTTOCOUNTRY.getCellType() != 3))
            {
              SHIPMENTTOCOUNTRY.setCellType(1);
              shipmenttocountry = SHIPMENTTOCOUNTRY.toString().trim();
            }
            if ((SHIPMENTFROMCOUNTRY != null) && (SHIPMENTFROMCOUNTRY.getCellType() != 3))
            {
              SHIPMENTFROMCOUNTRY.setCellType(1);
              shipmentfromcountry = SHIPMENTFROMCOUNTRY.toString().trim();
            }
            if ((HSCODE != null) && (HSCODE.getCellType() != 3))
            {
              HSCODE.setCellType(1);
              hscode = HSCODE.toString().trim();
            }
            if ((INCOTERMS != null) && (INCOTERMS.getCellType() != 3))
            {
              INCOTERMS.setCellType(1);
              incoterms = INCOTERMS.toString().trim();
            }
            if ((GOODSCODE != null) && (GOODSCODE.getCellType() != 3))
            {
              GOODSCODE.setCellType(1);
              goodscode = GOODSCODE.toString().trim();
            }
            if ((GOODDESCRIPTION != null) && (GOODDESCRIPTION.getCellType() != 3))
            {
              GOODDESCRIPTION.setCellType(1);
              gooddescription = GOODDESCRIPTION.toString().trim();
            }
            if ((PORTOFDESTINATION != null) && (PORTOFDESTINATION.getCellType() != 3))
            {
              PORTOFDESTINATION.setCellType(1);
              portofdestination = PORTOFDESTINATION.toString().trim();
            }
            if ((PORTOFLOADING != null) && (PORTOFLOADING.getCellType() != 3))
            {
              PORTOFLOADING.setCellType(1);
              portofloading = PORTOFLOADING.toString().trim();
            }
            if ((TRANSPORTDOCNO != null) && (TRANSPORTDOCNO.getCellType() != 3))
            {
              TRANSPORTDOCNO.setCellType(1);
              transportdocno = TRANSPORTDOCNO.toString().trim();
            }
            if ((TRANSPORTDOCDATE != null) && (TRANSPORTDOCDATE.getCellType() != 3))
            {
              TRANSPORTDOCDATE.setCellType(1);
              transportdocdate = TRANSPORTDOCDATE.toString().trim();
            }
            if ((INVOICENO != null) && (INVOICENO.getCellType() != 3))
            {
              INVOICENO.setCellType(1);
              invoiceno = INVOICENO.toString().trim();
            }
            if ((INVOICEDATE != null) && (INVOICEDATE.getCellType() != 3))
            {
              INVOICEDATE.setCellType(1);
              invoicedate = INVOICEDATE.toString().trim();
            }
            if ((FORM_TYPE != null) && (FORM_TYPE.getCellType() != 3))
            {
              FORM_TYPE.setCellType(1);
              form_type = FORM_TYPE.toString().trim();
            }
            if ((SHPBILL_NO != null) && (SHPBILL_NO.getCellType() != 3))
            {
              SHPBILL_NO.setCellType(1);
              shpbill_no = SHPBILL_NO.toString().trim();
            }
            if ((BILL_DATE != null) && (BILL_DATE.getCellType() != 3))
            {
              BILL_DATE.setCellType(1);
              bill_date = BILL_DATE.toString().trim();
            }
            if ((PORT_CODE != null) && (PORT_CODE.getCellType() != 3))
            {
              PORT_CODE.setCellType(1);
              port_code = PORT_CODE.toString().trim();
            }
            if ((FORM_NO != null) && (FORM_NO.getCellType() != 3))
            {
              FORM_NO.setCellType(1);
              form_no = FORM_NO.toString().trim();
            }
            if ((SHP_AMT != null) && (SHP_AMT.getCellType() != 3))
            {
              SHP_AMT.setCellType(1);
              shp_amt = SHP_AMT.toString().trim();
            }
            if ((SHP_CURRENCY != null) && (SHP_CURRENCY.getCellType() != 3))
            {
              SHP_CURRENCY.setCellType(1);
              shp_currency = SHP_CURRENCY.toString().trim();
            }
            if ((REPAMT_WRT_AMT != null) && (REPAMT_WRT_AMT.getCellType() != 3))
            {
              REPAMT_WRT_AMT.setCellType(1);
              repamt_wrt_amt = REPAMT_WRT_AMT.toString().trim();
            }
            if ((SHORTCOLLECTIONAMOUNT != null) && (SHORTCOLLECTIONAMOUNT.getCellType() != 3))
            {
              SHORTCOLLECTIONAMOUNT.setCellType(1);
              shortcollectionamount = SHORTCOLLECTIONAMOUNT.toString().trim();
            }
            if ((RECORD_INDICATOR != null) && (RECORD_INDICATOR.getCellType() != 3))
            {
              RECORD_INDICATOR.setCellType(1);
              record_indicator = RECORD_INDICATOR.toString().trim();
            }
            if ((LEO_DATE != null) && (LEO_DATE.getCellType() != 3))
            {
              LEO_DATE.setCellType(1);
              leo_date = LEO_DATE.toString().trim();
            }
            if ((EXPORT_AGENCY != null) && (EXPORT_AGENCY.getCellType() != 3))
            {
              EXPORT_AGENCY.setCellType(1);
              export_agency = EXPORT_AGENCY.toString().trim();
            }
            if ((EXPORT_TYPE != null) && (EXPORT_TYPE.getCellType() != 3))
            {
              EXPORT_TYPE.setCellType(1);
              export_type = EXPORT_TYPE.toString().trim();
            }
            if ((DESTINATION_COUNTRRY != null) && (DESTINATION_COUNTRRY.getCellType() != 3))
            {
              DESTINATION_COUNTRRY.setCellType(1);
              destination_countrry = DESTINATION_COUNTRRY.toString().trim();
            }
            if ((IECODE != null) && (IECODE.getCellType() != 3))
            {
              IECODE.setCellType(1);
              iecode = IECODE.toString().trim();
            }
            if ((ADCODE != null) && (ADCODE.getCellType() != 3))
            {
              ADCODE.setCellType(1);
              adcode = ADCODE.toString().trim();
            }
            if ((CUSTOMS_NUMBER != null) && (CUSTOMS_NUMBER.getCellType() != 3))
            {
              CUSTOMS_NUMBER.setCellType(1);
              customs_number = CUSTOMS_NUMBER.toString().trim();
            }
            if ((SHP_INV_SNO != null) && (SHP_INV_SNO.getCellType() != 3))
            {
              SHP_INV_SNO.setCellType(1);
              shp_inv_sno = SHP_INV_SNO.toString().trim();
            }
            if ((SHP_INVOICE_NO != null) && (SHP_INVOICE_NO.getCellType() != 3))
            {
              SHP_INVOICE_NO.setCellType(1);
              shp_invoice_no = SHP_INVOICE_NO.toString().trim();
            }
            if ((SHP_INVOICE_DATE != null) && (SHP_INVOICE_DATE.getCellType() != 3))
            {
              SHP_INVOICE_DATE.setCellType(1);
              shp_invoice_date = SHP_INVOICE_DATE.toString().trim();
            }
            if ((FOB_AMT != null) && (FOB_AMT.getCellType() != 3))
            {
              FOB_AMT.setCellType(1);
              fob_amt = FOB_AMT.toString().trim();
            }
            if ((FREIGHT_AMT != null) && (FREIGHT_AMT.getCellType() != 3))
            {
              FREIGHT_AMT.setCellType(1);
              freight_amt = FREIGHT_AMT.toString().trim();
            }
            if ((INS_AMT != null) && (INS_AMT.getCellType() != 3))
            {
              INS_AMT.setCellType(1);
              ins_amt = INS_AMT.toString().trim();
            }
            if ((COMM_AMT != null) && (COMM_AMT.getCellType() != 3))
            {
              COMM_AMT.setCellType(1);
              comm_amt = COMM_AMT.toString().trim();
            }
            if ((DISCOUNT_AMT != null) && (DISCOUNT_AMT.getCellType() != 3))
            {
              DISCOUNT_AMT.setCellType(1);
              discount_amt = DISCOUNT_AMT.toString().trim();
            }
            if ((DEDUCTION_AMT != null) && (DEDUCTION_AMT.getCellType() != 3))
            {
              DEDUCTION_AMT.setCellType(1);
              deduction_amt = DEDUCTION_AMT.toString().trim();
            }
            if ((PACKAGE_AMT != null) && (PACKAGE_AMT.getCellType() != 3))
            {
              PACKAGE_AMT.setCellType(1);
              package_amt = PACKAGE_AMT.toString().trim();
            }
            if ((REMITTANCE_ADCODE != null) && (REMITTANCE_ADCODE.getCellType() != 3))
            {
              REMITTANCE_ADCODE.setCellType(1);
              remittance_adcode = REMITTANCE_ADCODE.toString().trim();
            }
            if ((REMITTANCE_NUM != null) && (REMITTANCE_NUM.getCellType() != 3))
            {
              REMITTANCE_NUM.setCellType(1);
              remittance_num = REMITTANCE_NUM.toString().trim();
            }
            if ((FIRC_NO != null) && (FIRC_NO.getCellType() != 3))
            {
              FIRC_NO.setCellType(1);
              firc_no = FIRC_NO.toString().trim();
            }
            if ((REMITTER_NAME != null) && (REMITTER_NAME.getCellType() != 3))
            {
              REMITTER_NAME.setCellType(1);
              remitter_name = REMITTER_NAME.toString().trim();
            }
            if ((REMITTER_DATE != null) && (REMITTER_DATE.getCellType() != 3))
            {
              REMITTER_DATE.setCellType(1);
              remitter_date = REMITTER_DATE.toString().trim();
            }
            if ((REMITTER_COUNTRY != null) && (REMITTER_COUNTRY.getCellType() != 3))
            {
              REMITTER_COUNTRY.setCellType(1);
              remitter_country = REMITTER_COUNTRY.toString().trim();
            }
            if ((UTILIZATION_AMOUNT != null) && (UTILIZATION_AMOUNT.getCellType() != 3))
            {
              UTILIZATION_AMOUNT.setCellType(1);
              utilization_amount = UTILIZATION_AMOUNT.toString().trim();
            }
            if ((CIF_NUMBER != null) && (CIF_NUMBER.getCellType() != 3))
            {
              CIF_NUMBER.setCellType(1);
              cif_number = CIF_NUMBER.toString().trim();
            }
            if ((REMITANCE_AMOUNT != null) && (REMITANCE_AMOUNT.getCellType() != 3))
            {
              REMITANCE_AMOUNT.setCellType(1);
              remitance_amount = REMITANCE_AMOUNT.toString().trim();
            }
            String upload_date = null;
           
            Date today = new Date();
           
            Timestamp ts1 = new Timestamp(today.getTime());
           
            String s_date = ts1.toString();
            logger.info(new Timestamp(today.getTime()));
            try
            {
              String query = "SELECT TO_CHAR(TO_DATE(CURRENT_DATE,'DD-MM-YYYY'), 'DD-MM-YYYY')  TODAY  FROM DUAL";
             
              LoggableStatement pst = new LoggableStatement(con, query);
              ResultSet rs1 = pst.executeQuery();
              if (rs1.next()) {
                upload_date = rs1.getString("TODAY");
              }
            }
            catch (SQLException e)
            {
              logger.info("Getting Date Exception In Insertion Part--------" + e);
            }
            HttpSession session = ServletActionContext.getRequest().getSession();
            String sessionUserName = (String)session.getAttribute("loginedUserName");
           
            logger.info("==============>>> sessionUserName : " + sessionUserName);
           
            excelDataVO.setTheirref(theirref);
            excelDataVO.setCollecting_bank(collecting_bank);
            excelDataVO.setBehalfofbranch(behalfofbranch);
            excelDataVO.setDrawer(drawer);
            excelDataVO.setDraweecustomerid(draweecustomerid);
           
            excelDataVO.setDrawee_address(drawee_name_address);
           
            logger.info("Draweee Name--------------------->" + drawee_name_address);
            logger.info("Draweee Address- Country----------------->" + drawee_country);
            logger.info("drawee_country----------------->" + drawee_country);
           
            excelDataVO.setDrawee_name(drawee_country);
            excelDataVO.setChargeAccount(charge_debit_ac_no);
           
            excelDataVO.setCollectionamount(collectionamount);
           
            logger.info("---------------Collection Amount-----------------" + collectionamount);
           


            excelDataVO.setCollectioncurrency(collectioncurrency);
            excelDataVO.setShipmenttocountry(shipmenttocountry);
            excelDataVO.setShipmentfromcountry(shipmentfromcountry);
            excelDataVO.setHscode(hscode);
            excelDataVO.setIncoterms(incoterms);
            excelDataVO.setGoodscode(goodscode);
            excelDataVO.setGooddescription(gooddescription);
            excelDataVO.setPortofdestination(portofdestination);
            excelDataVO.setPortofloading(portofloading);
            excelDataVO.setTransportdocno(transportdocno);
            excelDataVO.setTransportdocdate(transportdocdate);
            excelDataVO.setInvoiceno(invoiceno);
            excelDataVO.setInvoicedate(invoicedate);
            excelDataVO.setForm_type(form_type);
            excelDataVO.setShpbill_no(shpbill_no);
            excelDataVO.setBill_date(bill_date);
            excelDataVO.setPort_code(port_code);
            excelDataVO.setForm_no(form_no);
            excelDataVO.setShp_amt(shp_amt);
            excelDataVO.setShp_currency(shp_currency);
            excelDataVO.setReason_short_shp_amt(reason_short_shp_amt);
            excelDataVO.setShort_shp_amt(short_shp_amt);
            excelDataVO.setRepamt_wrt_amt(repamt_wrt_amt);
            excelDataVO.setShortcollectionamount(shortcollectionamount);
            excelDataVO.setRecord_indicator(record_indicator);
            excelDataVO.setLeo_date(leo_date);
            excelDataVO.setExport_agency(export_agency);
            excelDataVO.setExport_type(export_type);
            excelDataVO.setDestination_countrry(destination_countrry);
            excelDataVO.setIecode(iecode);
            excelDataVO.setAdcode(adcode);
            excelDataVO.setCustoms_number(customs_number);
            excelDataVO.setShp_inv_sno(shp_inv_sno);
            excelDataVO.setShp_invoice_no(shp_invoice_no);
            excelDataVO.setShp_invoice_date(shp_invoice_date);
            excelDataVO.setFob_amt(fob_amt);
            excelDataVO.setFreight_amt(freight_amt);
            excelDataVO.setIns_amt(ins_amt);
            excelDataVO.setComm_amt(comm_amt);
            excelDataVO.setDiscount_amt(discount_amt);
            excelDataVO.setDeduction_amt(deduction_amt);
            excelDataVO.setPackage_amt(package_amt);
            excelDataVO.setRemittance_adcode(remittance_adcode);
            excelDataVO.setRemittance_num(remittance_num);
            excelDataVO.setFirc_no(firc_no);
            excelDataVO.setRemitter_name(remitter_name);
            excelDataVO.setRemitter_date(remitter_date);
            excelDataVO.setRemitter_country(remitter_country);
            excelDataVO.setUtilization_amount(utilization_amount);
            logger.info("Utilization Amt--------123456------------------->" + utilization_amount);
            excelDataVO.setCif_number(cif_number);
            excelDataVO.setAvailable_amount(remitance_amount);
            excelDataVO.setU_name(sessionUserName);
           
            excelDataVO.setUpload_date(s_date);
            odcDataList.add(excelDataVO);
          }
          i++;
        }
        String batchId = null;
        if ((odcDataList != null) && (!odcDataList.isEmpty())) {
          batchId = insertExcelData(odcDataList, makervo);
        }
        makervo.setBatchId(batchId);
        if (batchId != null) {
          makervo = getExcelDataValue(makervo);
        }
        con.close();
      }
      logger.info("End Loading - " + new Date());
    }
    catch (Exception exception)
    {
      logger.info("Insert Exceptionss----------->" + exception);
      logger.info("Insert Exceptionss----------->" + exception);
    }
    logger.info("Exiting Method");
    return makervo;
  }
 
  public String getExcelValidate(String batchId, MakerHomeVO makervo)
    throws DAOException, IOException
  {
    logger.info("Entering Method");
    Connection con = null;
    try
    {
      logger.info("Start Validation - " + new Date());
      con = DBConnectionUtility.getConnection();
      ArrayList<ExcelDataVO> invoiceList = null;
      invoiceList = getInvoiceListBasedOnBatchId(batchId, con);
      for (int i = 0; i < invoiceList.size(); i++)
      {
        ExcelDataVO excelDataVO = null;
        excelDataVO = (ExcelDataVO)invoiceList.get(i);
        validateExcelData(batchId, excelDataVO, con);
      }
      makervo = getExcelDataValueBasedBatchId(con, makervo, batchId);
      String state = getcurrentstatus(batchId, con);
      makervo.setState(state);
    }
    catch (Exception exception)
    {
      logger.info("Exception in Validation------------->" + exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, null, null);
    }
    logger.info("End Validation ----------------------> " + new Date());
    logger.info("Exiting Method");
    return "success";
  }
 
  public double getExRateValue(Connection con, String ccy, String date)
  {
    LoggableStatement pst = null;
    ResultSet rs = null;
    double exRate = 0.0D;
    try
    {
      String query = "SELECT TRIM(RATE) FROM THEMEBRIDGE.SPOTRATEHISTORY WHERE TRIM(CCY)=? and trim(quote_date)=?";
      pst = new LoggableStatement(con, query);
      pst.setString(1, ccy);
      pst.setString(2, date);
     
      rs = pst.executeQuery();
      if (rs.next()) {
        exRate = Double.parseDouble(rs.getString(1));
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderStatement(pst, rs);
    }
    return exRate;
  }
 
  public void validateExcelData(String batchId, ExcelDataVO excelDataVO, Connection con)
  {
    try
    {
      String tempStatus = "0";
      ArrayList<String> errorList = new ArrayList();
      if ((excelDataVO.getTheirref() != null) && (!excelDataVO.getTheirref().equalsIgnoreCase("")) &&
        (!excelDataVO.getTheirref().equalsIgnoreCase(" ")))
      {
        ResultSet rs2 = null;
        String qquery = "select pri_ref THR from master where trim(pri_ref)= '" + excelDataVO.getTheirref() +
          "' ";
        LoggableStatement ls2 = new LoggableStatement(con, qquery);
        rs2 = ls2.executeQuery();
        if (rs2.next())
        {
          tempStatus = "1";
          errorList.add("Their Reference already used in TI transaction");
        }
        DBConnectionUtility.surrenderDB(null, ls2, rs2);
      }
      else
      {
        tempStatus = "1";
        errorList.add("Please Enter Their reference");
      }
      if ((excelDataVO.getCollecting_bank() != null) && (!excelDataVO.getCollecting_bank().equalsIgnoreCase("")) &&
        (!excelDataVO.getCollecting_bank().equalsIgnoreCase(" ")))
      {
        ResultSet rs2 = null;
        String qquery = "SELECT F.SVNAFF AS SEND_TO_ADDRESS FROM GFPF G,SX20LF  F  WHERE TRIM(G.GFCUS1)=TRIM(F.SXCUS1) AND F.SXCUS1='" + excelDataVO.getCollecting_bank() + "' ";
        LoggableStatement ls2 = new LoggableStatement(con, qquery);
        rs2 = ls2.executeQuery();
        if (!rs2.next())
        {
          tempStatus = "1";
          errorList.add("Collecting Bank Not in Master");
        }
        DBConnectionUtility.surrenderDB(null, ls2, rs2);
      }
      else
      {
        tempStatus = "1";
        errorList.add("Please Enter Collecting Bank");
      }
      if ((excelDataVO.getBehalfofbranch() != null) && (!excelDataVO.getBehalfofbranch().equalsIgnoreCase("")) &&
        (!excelDataVO.getBehalfofbranch().equalsIgnoreCase(" ")))
      {
        ResultSet rs2 = null;
        String qquery = "select CABRNM from capf where trim(CABRNM)= '" + excelDataVO.getBehalfofbranch() +
          "' ";
        LoggableStatement ls2 = new LoggableStatement(con, qquery);
        rs2 = ls2.executeQuery();
        if (!rs2.next())
        {
          tempStatus = "1";
          errorList.add("Behalf of Branch not in Master");
        }
        DBConnectionUtility.surrenderDB(null, ls2, rs2);
      }
      else
      {
        tempStatus = "1";
        errorList.add("Please Enter Behalf of Branch");
      }
      if ((excelDataVO.getPortofloading() != null) && (!excelDataVO.getPortofloading().equalsIgnoreCase("")) &&
        (!excelDataVO.getPortofloading().equalsIgnoreCase(" ")))
      {
        ResultSet rs2 = null;
        String qquery = "SELECT * FROM EXTPORTODLOAD WHERE TRIM(PORTLOAD)='" + excelDataVO.getPortofloading() +
          "' ";
        LoggableStatement ls2 = new LoggableStatement(con, qquery);
       
        rs2 = ls2.executeQuery();
        if (!rs2.next())
        {
          tempStatus = "1";
          errorList.add("Port of Loading  not in Master");
        }
        DBConnectionUtility.surrenderDB(null, ls2, rs2);
      }
      else
      {
        tempStatus = "1";
        errorList.add("Please Enter Port of Loading");
      }
      if ((excelDataVO.getDrawer() != null) && (!excelDataVO.getDrawer().equalsIgnoreCase("")) &&
        (!excelDataVO.getDrawer().equalsIgnoreCase(" ")))
      {
        ResultSet rs2 = null;
        String qquery = "SELECT GFCUS1 FROM GFPF WHERE TRIM(GFCUS1) = '" + excelDataVO.getDrawer() + "' ";
        LoggableStatement ls2 = new LoggableStatement(con, qquery);
       
        rs2 = ls2.executeQuery();
        if (!rs2.next())
        {
          tempStatus = "1";
          errorList.add("Drawer ID not in Master");
        }
        DBConnectionUtility.surrenderDB(null, ls2, rs2);
      }
      else
      {
        tempStatus = "1";
        errorList.add("Please Enter Drawer ID");
      }
      logger.info("excelDataVO.getCollecting_bank()------------------>" + excelDataVO.getCollecting_bank());
      if (excelDataVO.getCollecting_bank() == null)
      {
        tempStatus = "1";
        errorList.add(" Please Enter Collecting Bank");
      }
      if ((excelDataVO.getDraweecustomerid() == "") && (excelDataVO.getDrawee_address() == ""))
      {
        tempStatus = "1";
        errorList.add(" Please Enter Drawee Customer ID or Drawee Name");
      }
      if ((excelDataVO.getDraweecustomerid() == "") && (excelDataVO.getDrawee_address() == " "))
      {
        tempStatus = "1";
        errorList.add(" Please Enter Drawee Customer ID or Drawee Name");
      }
      if ((excelDataVO.getDraweecustomerid() == " ") && (excelDataVO.getDrawee_address() == ""))
      {
        tempStatus = "1";
        errorList.add(" Please Enter Drawee Customer ID or Drawee Name");
      }
      if ((excelDataVO.getDraweecustomerid() == " ") && (excelDataVO.getDrawee_address() == " "))
      {
        tempStatus = "1";
        errorList.add(" Please Enter Drawee Customer ID or Drawee Name");
      }
      if ((excelDataVO.getDraweecustomerid() == null) && (excelDataVO.getDrawee_address() == null))
      {
        tempStatus = "1";
        errorList.add(" Please Enter Drawee Customer ID or Drawee Name");
      }
      if ((excelDataVO.getDraweecustomerid() != null) &&
        (!excelDataVO.getDraweecustomerid().equalsIgnoreCase("")) &&
        (!excelDataVO.getDraweecustomerid().equalsIgnoreCase(" ")))
      {
        ResultSet rs2 = null;
        String qquery = "select gfcus1 from GFPF where trim(gfcus1) = '" + excelDataVO.getDraweecustomerid() +
          "' ";
        LoggableStatement ls2 = new LoggableStatement(con, qquery);
        rs2 = ls2.executeQuery();
        if (!rs2.next())
        {
          tempStatus = "1";
          errorList.add("Drawee Customer ID not in Master");
        }
        DBConnectionUtility.surrenderDB(null, ls2, rs2);
      }
      if ((excelDataVO.getCollectioncurrency() != null) && (!excelDataVO.getCollectioncurrency().equalsIgnoreCase("")) &&
        (!excelDataVO.getCollectioncurrency().equalsIgnoreCase(" ")))
      {
        ResultSet rs2 = null;
        String qquery = "select C8CCY from C8PF where C8CCY  = '" + excelDataVO.getCollectioncurrency() + "' ";
        LoggableStatement ls2 = new LoggableStatement(con, qquery);
       
        rs2 = ls2.executeQuery();
        if (!rs2.next())
        {
          tempStatus = "1";
          errorList.add("Collection Currency not matches with Master");
        }
        DBConnectionUtility.surrenderDB(null, ls2, rs2);
      }
      else
      {
        tempStatus = "1";
        errorList.add("Please Enter Collection Currency");
      }
      if (excelDataVO.getForm_type() != null)
      {
        String quer = excelDataVO.getForm_type().trim();
        if ((!quer.equalsIgnoreCase("EDI")) && (!quer.equalsIgnoreCase("EDF")) && (!quer.equalsIgnoreCase("SOFTEX")) &&
          (!quer.equalsIgnoreCase("EXEMPTED")))
        {
          tempStatus = "1";
          errorList.add("Form Type Should be [EDI,EDF,SOFTEX,EXEMPTED]");
        }
        else
        {
          String formTypeErrorStr = validateBasedOnFormType(con, quer, excelDataVO);
          if (formTypeErrorStr != null)
          {
            tempStatus = "1";
            errorList.add(formTypeErrorStr);
          }
        }
        if ((excelDataVO.getForm_type().equalsIgnoreCase("EDI")) || (excelDataVO.getForm_type().equalsIgnoreCase("EDF")))
        {
          if ((excelDataVO.getBill_date() != null) && (excelDataVO.getBill_date() != "") &&
            (excelDataVO.getBill_date() != " "))
          {
            String datestate = ValidateDate(excelDataVO.getBill_date());
            if (datestate.equalsIgnoreCase("invalid"))
            {
              tempStatus = "1";
              errorList.add("Invalid Bill Date ,Please Enter DD-MM-YYYYY Format");
            }
          }
          if ((excelDataVO.getCollectionamount() == null) || (excelDataVO.getCollectionamount() == "") ||
            (excelDataVO.getCollectionamount() == " "))
          {
            tempStatus = "1";
            errorList.add("Please Enter Collectin Amount");
          }
          if ((excelDataVO.getShp_amt() == null) || (excelDataVO.getShp_amt() == "") ||
            (excelDataVO.getShp_amt() == " "))
          {
            tempStatus = "1";
            errorList.add("Please Enter Shipment Amount");
          }
          double short_amt = 0.0D;
          boolean reason_shp_amt = false;
          if ((excelDataVO.getTheirref() != null) && (excelDataVO.getTheirref() != "") && (excelDataVO.getTheirref() != " "))
          {
            if ((excelDataVO.getReason_short_shp_amt() == null) || (excelDataVO.getReason_short_shp_amt() == "") ||
              (excelDataVO.getReason_short_shp_amt() == " "))
            {
              tempStatus = "1";
              errorList.add("Please Enter Reason for Short Shipment Amount  [YES/NO] ");
            }
            if (excelDataVO.getReason_short_shp_amt().equalsIgnoreCase("YES"))
            {
              short_amt = Double.valueOf(excelDataVO.getShort_shp_amt()).doubleValue();
              reason_shp_amt = true;
            }
            else if (excelDataVO.getReason_short_shp_amt().equalsIgnoreCase("NO"))
            {
              reason_shp_amt = true;
            }
          }
          double shipment_amt = 0.0D;
          double collection_amt = 0.0D;
          if (reason_shp_amt)
          {
            ResultSet rs2 = null;
           
            String querys = "SELECT DISTINCT SHPBILL_NO, BILL_DATE, PORT_CODE, FORM_NO, NVL(SHP_AMT,'0') SHP_AMT FROM ETTDM_BULKUPLOAD_STAGING WHERE BATCH_NO='" +
              batchId + "'" +
              " AND THEIRREF='" + excelDataVO.getTheirref() + "'";
           






            LoggableStatement ls2 = new LoggableStatement(con, querys);
            rs2 = ls2.executeQuery();
            while (rs2.next()) {
              shipment_amt += Double.parseDouble(rs2.getString("SHP_AMT"));
            }
            logger.info("Shipment Amount-----------" + shipment_amt);
           

            DBConnectionUtility.surrenderDB(null, ls2, rs2);
          }
          collection_amt = Double.parseDouble(excelDataVO.getCollectionamount());
          logger.info("Collection Amount---------" + collection_amt);
          if (Math.round(collection_amt * 100.0D) / 100.0D != Math.round(shipment_amt * 100.0D) / 100.0D)
          {
            tempStatus = "1";
            errorList.add("Collection Amount Not Matches with the Shipment Amount");
          }
          double inv_shp_amt = 0.0D;
          if (excelDataVO.getForm_type().equalsIgnoreCase("EDF"))
          {
            String shp_no = null;
            shp_no = excelDataVO.getShpbill_no();
            if (shp_no == null) {
              shp_no = excelDataVO.getForm_no();
            }
            if (excelDataVO.getShpbill_no() != null)
            {
              String query = "SELECT DISTINCT SHPBILL_NO,SHP_INVOICE_NO,BILL_DATE,NVL(FOB_AMT,'0')+NVL(FREIGHT_AMT,'0')+NVL(INS_AMT,'0') AS SHP_AMT  FROM ETTDM_BULKUPLOAD_STAGING WHERE  BATCH_NO='" +
                batchId + "' AND THEIRREF='" + excelDataVO.getTheirref() + "' ";
             

              ResultSet rs3 = null;
              LoggableStatement ls3 = new LoggableStatement(con, query);
             
              rs3 = ls3.executeQuery();
              while (rs3.next()) {
                inv_shp_amt += Double.parseDouble(rs3.getString("SHP_AMT"));
              }
              logger.info("INvoice amt------------>" + inv_shp_amt);
              DBConnectionUtility.surrenderDB(null, ls3, rs3);
            }
            if (!excelDataVO.getForm_no().equalsIgnoreCase(""))
            {
              String query = "SELECT DISTINCT FORM_NO,SHP_INVOICE_NO, BILL_DATE,NVL(FOB_AMT,'0')+NVL(FREIGHT_AMT,'0')+NVL(INS_AMT,'0') AS SHP_AMT  FROM ETTDM_BULKUPLOAD_STAGING WHERE  BATCH_NO='" +
                batchId + "' AND THEIRREF='" + excelDataVO.getTheirref() + "'  ";
             

              ResultSet rs3 = null;
              LoggableStatement ls3 = new LoggableStatement(con, query);
              logger.info("Qery----Staging Shipping Invoice Amt------------>" + ls3.getQueryString());
              rs3 = ls3.executeQuery();
              while (rs3.next()) {
                inv_shp_amt += Double.parseDouble(rs3.getString("SHP_AMT"));
              }
              logger.info("Form Invoice Shipment Amount---->" + inv_shp_amt);
              DBConnectionUtility.surrenderDB(null, ls3, rs3);
            }
            logger.info("Collection amount---111---------->" + collection_amt);
            logger.info("invoice  amount-----11111-------->" + inv_shp_amt);
          }
          if (excelDataVO.getForm_type().equalsIgnoreCase("EDI"))
          {
            String query = "SELECT DISTINCT INV.INVNO,SHIPBILLDATE,NVL(INV.FOBAMT,'0') AS FOBAMT,  NVL(INV.FRIEGHTAMT,'0') AS FRIEGHTAMT, NVL(INV.INSAMT,'0') AS INSAMT,  INV.FOBCURRCODE, INV.FRIEGHTCURRCODE, INV.INSCURRCODE  FROM ETT_EDPMS_SHP_INV INV,ETTDM_BULKUPLOAD_STAGING STG WHERE INV.SHIPBILLNO   =STG.SHPBILL_NO AND TO_CHAR(to_date(INV.SHIPBILLDATE,'DD-MM-YYYY'),'DD/MM/YYYY')=TO_CHAR(to_date(STG.BILL_DATE,'DD-MM-YYYY'),'DD/MM/YYYY')  AND INV.PORTCODE  =STG.PORT_CODE AND STG.THEIRREF='" +
           


              excelDataVO.getTheirref() + "' AND STG.BATCH_NO='" + batchId + "'";
           
            ResultSet rs3 = null;
            LoggableStatement ls3 = new LoggableStatement(con, query);
            logger.info("Qery---------------->" + ls3.getQueryString());
            rs3 = ls3.executeQuery();
            while (rs3.next())
            {
              double fobExrate = getExRateValue(con, rs3.getString("FOBCURRCODE"), rs3.getString("SHIPBILLDATE"));
             
              double friExrate = getExRateValue(con, rs3.getString("FRIEGHTCURRCODE"), rs3.getString("SHIPBILLDATE"));
             
              double insExrate = getExRateValue(con, rs3.getString("INSCURRCODE"), rs3.getString("SHIPBILLDATE"));
             
              double fobAmt = Double.parseDouble(rs3.getString("FOBAMT"));
             
              double friAmt = Double.parseDouble(rs3.getString("FRIEGHTAMT"));
             
              double insAmt = Double.parseDouble(rs3.getString("INSAMT"));
             
              inv_shp_amt += fobAmt;
              if (fobExrate == friExrate) {
                inv_shp_amt += friAmt;
              } else if (fobExrate > friExrate) {
                inv_shp_amt += friAmt * friExrate / fobExrate;
              } else {
                inv_shp_amt += friAmt * (friExrate / fobExrate);
              }
              if (fobExrate == insExrate) {
                inv_shp_amt += insAmt;
              } else if (fobExrate > insExrate) {
                inv_shp_amt += insAmt * insExrate / fobExrate;
              } else {
                inv_shp_amt += insAmt * (insExrate / fobExrate);
              }
            }
            logger.info("Collection amount---111---------->" + collection_amt);
            logger.info("invoice  amount-----11111-------->" + inv_shp_amt);
           








            DBConnectionUtility.surrenderDB(null, ls3, rs3);
          }
          if (excelDataVO.getForm_type().equalsIgnoreCase("SOFTEX"))
          {
            String query = "SELECT DISTINCT SOFT.INVNO,STG.THEIRREF,STG.BATCH_NO,NVL(SOFT.FOBAMT,'0') FOBAMT,  NVL(SOFT.FRIEGHTAMT,'0') FRIEGHTAMT, NVL(SOFT.INSAMT,'0') AS INSAMT,SOFT.SHIPBILLDATE  SOFT.FOBCURRCODE, SOFT.FRIEGHTCURRCODE, SOFT.INSCURRCODE  FROM ETT_EDPMS_SHP_INV_SOFTEX SOFT,ETTDM_BULKUPLOAD_STAGING STG WHERE SOFT.FORMNO  =STG.FORM_NO  AND TO_CHAR(to_date(SOFT.SHIPBILLDATE,'DD-MM-YYYY'),'DD/MM/YYYY')=TO_CHAR(to_date(STG.BILL_DATE,'DD-MM-YYYY'),'DD/MM/YYYY')  AND SOFT.PORTCODE  =STG.PORT_CODE AND STG.THEIRREF ='" +
           


              excelDataVO.getTheirref() + "' AND STG.BATCH_NO ='" + batchId + "' ";
           

            ResultSet rs3 = null;
            LoggableStatement ls3 = new LoggableStatement(con, query);
            logger.info("Qery---------------->" + ls3.getQueryString());
            rs3 = ls3.executeQuery();
            while (rs3.next())
            {
              double fobExrate = getExRateValue(con, rs3.getString("FOBCURRCODE"), rs3.getString("SHIPBILLDATE"));
             
              double friExrate = getExRateValue(con, rs3.getString("FRIEGHTCURRCODE"), rs3.getString("SHIPBILLDATE"));
             
              double insExrate = getExRateValue(con, rs3.getString("INSCURRCODE"), rs3.getString("SHIPBILLDATE"));
             
              double fobAmt = Double.parseDouble(rs3.getString("FOBAMT"));
             
              double friAmt = Double.parseDouble(rs3.getString("FRIEGHTAMT"));
             
              double insAmt = Double.parseDouble(rs3.getString("INSAMT"));
             
              inv_shp_amt += fobAmt;
              if (fobExrate == friExrate) {
                inv_shp_amt += friAmt;
              } else if (fobExrate > friExrate) {
                inv_shp_amt += friAmt * friExrate / fobExrate;
              } else {
                inv_shp_amt += friAmt * (friExrate / fobExrate);
              }
              if (fobExrate == insExrate) {
                inv_shp_amt += insAmt;
              } else if (fobExrate > insExrate) {
                inv_shp_amt += insAmt * insExrate / fobExrate;
              } else {
                inv_shp_amt += insAmt * (insExrate / fobExrate);
              }
            }
            logger.info("Collection amount---111---------->" + collection_amt);
            logger.info("invoice  amount-----11111-------->" + inv_shp_amt);
           







            DBConnectionUtility.surrenderDB(null, ls3, rs3);
          }
          String short_shp_amt = excelDataVO.getShort_shp_amt();
          String repay_write_off_amt = excelDataVO.getRepamt_wrt_amt();
          String short_collection_amt = excelDataVO.getShortcollectionamount();
          double short_shp_amt_1 = 0.0D;
          double repay_write_off_amt_1 = 0.0D;
          double collection_amt_11 = 0.0D;
         


          double sp_rate = 1.0D;
          double sp_reate_1 = 0.0D;
          double collection_amt_123 = 0.0D;
          double remittance_amt_123 = 0.0D;
          if (excelDataVO.getForm_type().equalsIgnoreCase("EDF"))
          {
            String invoice_amt = "SELECT DISTINCT SHP_INV_SNO,NVL(FOB_AMT+ FREIGHT_AMT+INS_AMT+COMM_AMT,0) AS AMT  FROM ETTDM_BULKUPLOAD_STAGING  WHERE BATCH_NO='" +
              batchId + "' AND THEIRREF  ='" + excelDataVO.getTheirref() + "' ";
           
            LoggableStatement ls = new LoggableStatement(con, invoice_amt);
            ResultSet rs1 = ls.executeQuery();
            while (rs1.next()) {
              repay_write_off_amt_1 += Double.valueOf(rs1.getString("AMT")).doubleValue();
            }
            if (repay_write_off_amt_1 == 0.0D) {
              repay_write_off_amt_1 = Double.valueOf(repay_write_off_amt).doubleValue();
            }
            logger.info("Repay Write Amount EDF Query------------->" + ls.getQueryString());
            logger.info("Repay Write off Amount------in EDF ---------->" + repay_write_off_amt_1);
           
            DBConnectionUtility.surrenderDB(null, ls, rs1);
          }
          else
          {
            if ((short_shp_amt == null) || (short_shp_amt == "") || (short_shp_amt == " ")) {
              short_shp_amt_1 = 0.0D;
            } else {
              short_shp_amt_1 = Double.valueOf(short_shp_amt).doubleValue();
            }
            if ((repay_write_off_amt == null) || (repay_write_off_amt == "") || (repay_write_off_amt == " "))
            {
              repay_write_off_amt_1 = 0.0D;
            }
            else
            {
              double repayAmt = 0.0D;
              double shortCollAmt = 0.0D;
              double shortShpAmt = 0.0D;
              double shpAmt = 0.0D;
             



              String collection_currency_1 = "SELECT SHPBILL_NO, NVL(REPAMT_WRT_AMT,'0') AS REP_AMT, NVL(SHORTCOLLECTIONAMOUNT,'0') AS SHORTCOLLECTIONAMOUNT, NVL(SHORT_SHP_AMT,'0') AS SHORT_SHP_AMT FROM ETTDM_BULKUPLOAD_STAGING WHERE BATCH_NO='" +
             


                batchId + "' AND THEIRREF  ='" + excelDataVO.getTheirref() + "'";
             
              LoggableStatement val1 = new LoggableStatement(con, collection_currency_1);
              logger.info("Collection Query--------------------->" + val1.getQueryString());
              ResultSet rsval1 = val1.executeQuery();
              while (rsval1.next())
              {
                repayAmt += Double.valueOf(rsval1.getString("REP_AMT")).doubleValue();
                shortCollAmt += Double.valueOf(rsval1.getString("SHORTCOLLECTIONAMOUNT")).doubleValue();
                shortShpAmt += Double.valueOf(rsval1.getString("SHORT_SHP_AMT")).doubleValue();
              }
              DBConnectionUtility.surrenderDB(null, val1, rsval1);
              repay_write_off_amt_1 = repayAmt;
              collection_amt_11 = shortCollAmt;
              short_shp_amt_1 = shortShpAmt;
            }
            if ((short_collection_amt == null) || (short_collection_amt == "") || (short_collection_amt == " ")) {
              collection_amt_11 = 0.0D;
            } else {
              collection_amt_11 = Double.valueOf(collection_amt_11).doubleValue();
            }
            String shp_currency = "";
           
            String collection_currency = excelDataVO.getCollectioncurrency();
            if ((excelDataVO.getRemittance_num() != null) && (excelDataVO.getRemittance_num() != "") && (excelDataVO.getRemittance_num() != " "))
            {
              ResultSet rsval = null;
              ResultSet rsval1 = null;
              LoggableStatement val = null;
              LoggableStatement val1 = null;
             

              String collection_currency_1 = "SELECT REMITTANCE_NUM,NVL(REMITTANCE_AMOUNT,'0') AS REMITTANCE_AMOUNT,SHP_CURRENCY \tFROM ETTV_REMITANCE_AMT WHERE REMITTANCE_NUM='" + excelDataVO.getRemittance_num() + "' " +
                " GROUP BY REMITTANCE_NUM, REMITTANCE_AMOUNT,SHP_CURRENCY";
             
              val1 = new LoggableStatement(con, collection_currency_1);
              rsval1 = val1.executeQuery();
              if (!rsval1.next()) {
                shp_currency = collection_currency;
              } else {
                shp_currency = rsval1.getString("SHP_CURRENCY");
              }
              DBConnectionUtility.surrenderDB(null, val1, rsval1);
              logger.info("Collection Currency--------------------->" + collection_currency_1);
              String spot_rate = "SELECT ETT_SPOTRATE_CAL('" + collection_currency + "','" + shp_currency + "') AS RATE FROM DUAL";
             
              val = new LoggableStatement(con, spot_rate);
             
              logger.info("Loggable Statement Query--------------->" + val.getQueryString());
              rsval = val.executeQuery();
              while (rsval.next()) {
                sp_rate = Double.valueOf(rsval.getString("RATE")).doubleValue();
              }
              logger.info("Shipping  Currency--------------------->" + shp_currency);
              logger.info("Shipping  Currency--------------------->" + shp_currency);
              logger.info("Exchange Rate--------------------->" + sp_rate);
              logger.info("Exchange Rate--------------------->" + sp_rate);
              logger.info("repayamt Amount----------------->" + repay_write_off_amt_1);
              logger.info("repayamt Amount----------------->" + repay_write_off_amt_1);
             
              DBConnectionUtility.surrenderDB(null, val, rsval);
            }
          }
          collection_amt_123 = repay_write_off_amt_1 * sp_rate;
         

          double r_amt = Double.valueOf(collection_amt_123).doubleValue();
         
          logger.info("Rounding Math Amount------------>" + r_amt);
         
          logger.info("Shipment Amount--88---------->" + shipment_amt);
         
          logger.info("short_shp_amt_1---88---------->" + short_shp_amt_1);
          logger.info("r_amt---------88---->" + r_amt);
          logger.info("collection_amt_11---88---------->" + collection_amt_11);
          logger.info("Shipment Amount------------>" + shipment_amt);
         
          logger.info("Total Amount------------" + short_shp_amt_1 + r_amt + collection_amt_11);
         
          double totalAmt = short_shp_amt_1 + r_amt + collection_amt_11;
          if (Math.round(shipment_amt * 100.0D) / 100.0D != Math.round(totalAmt * 100.0D) / 100.0D)
          {
            tempStatus = "1";
            errorList.add("Sum of Short shipment amount + repay amount + short collection amount is not equal to shipping bill amount.");
          }
          if ((excelDataVO.getPort_code() != null) && (!excelDataVO.getPort_code().equalsIgnoreCase("")) && (!excelDataVO.getPort_code().equalsIgnoreCase(" ")))
          {
            ResultSet rs2 = null;
            String qquery = "select PCODE from EXTPORTCO where PCODE  = '" + excelDataVO.getPort_code() + "' ";
            LoggableStatement ls2 = new LoggableStatement(con, qquery);
            rs2 = ls2.executeQuery();
            if (!rs2.next())
            {
              tempStatus = "1";
              errorList.add("Port Code not in Master");
            }
            DBConnectionUtility.surrenderDB(null, ls2, rs2);
          }
          else
          {
            tempStatus = "1";
            errorList.add("Please Enter Port Code");
          }
        }
        if (excelDataVO.getForm_type().equalsIgnoreCase("EDF"))
        {
          if ((excelDataVO.getLeo_date() == null) || (excelDataVO.getLeo_date() == "") || (excelDataVO.getLeo_date() == " "))
          {
            tempStatus = "1";
            errorList.add("Please Enter Leo Date DD-MM-YYYY Format");
          }
          else if (excelDataVO.getLeo_date() != null)
          {
            String datestate = ValidateDate(excelDataVO.getLeo_date());
            if (datestate.equalsIgnoreCase("invalid"))
            {
              tempStatus = "1";
              errorList.add("Invalid Leo Date DD-MM-YYYY Format");
            }
          }
          if ((excelDataVO.getExport_agency() != null) && (excelDataVO.getExport_agency() != "") && (excelDataVO.getExport_agency() != " "))
          {
            String Str = excelDataVO.getExport_agency().trim();
            if ((!Str.equalsIgnoreCase("1")) && (!Str.equalsIgnoreCase("2")) &&
              (!Str.equalsIgnoreCase("3")))
            {
              tempStatus = "1";
              errorList.add("Export Agency Should be [1,2,3]");
            }
          }
          else
          {
            tempStatus = "1";
            errorList.add("Please Enter Export Agency");
          }
          if ((excelDataVO.getExport_type() != null) && (excelDataVO.getExport_type() != "") && (excelDataVO.getExport_type() != " "))
          {
            String Str = excelDataVO.getExport_type().trim();
            if ((!Str.equalsIgnoreCase("1")) && (!Str.equalsIgnoreCase("2")) && (!Str.equalsIgnoreCase("3")))
            {
              tempStatus = "1";
              errorList.add("Export Type Should be [1,2,3]");
            }
          }
          else
          {
            tempStatus = "1";
            errorList.add("Please Enter Export Type");
          }
        }
      }
      else
      {
        tempStatus = "1";
        errorList.add("Please Enter Form Type");
      }
      if (excelDataVO.getForm_type().equals("EDF"))
      {
        if ((excelDataVO.getDestination_countrry() == null) || (excelDataVO.getDestination_countrry() == "") ||
          (excelDataVO.getDestination_countrry() == " "))
        {
          tempStatus = "1";
          errorList.add("Please Enter Destination Country");
        }
        if ((excelDataVO.getIecode() == null) || (excelDataVO.getIecode() == "") ||
          (excelDataVO.getIecode() == " "))
        {
          tempStatus = "1";
          errorList.add("Please Enter IECODE");
        }
        if ((excelDataVO.getAdcode() == null) || (excelDataVO.getAdcode() == "") ||
          (excelDataVO.getAdcode() == " "))
        {
          tempStatus = "1";
          errorList.add("Please Enter AD CODE");
        }
        if ((excelDataVO.getCustoms_number() == null) || (excelDataVO.getCustoms_number() == "") ||
          (excelDataVO.getCustoms_number() == " "))
        {
          tempStatus = "1";
          errorList.add("Please Enter Customs Number");
        }
        if ((excelDataVO.getShp_inv_sno() == null) || (excelDataVO.getShp_inv_sno() == "") ||
          (excelDataVO.getShp_inv_sno() == " "))
        {
          tempStatus = "1";
          errorList.add("Please Enter SHP_INV_SNO");
        }
        if ((excelDataVO.getShp_invoice_no() == null) || (excelDataVO.getShp_invoice_no() == "") ||
          (excelDataVO.getShp_invoice_no() == " "))
        {
          tempStatus = "1";
          errorList.add("Please Enter SHP_INVOICE_NO");
        }
        if ((excelDataVO.getShp_invoice_date() == null) || (excelDataVO.getShp_invoice_date() == "") ||
          (excelDataVO.getShp_invoice_date() == " "))
        {
          tempStatus = "1";
          errorList.add("Please Enter Shipment Invoice  Date");
        }
        else if (excelDataVO.getShp_invoice_date() != null)
        {
          String datestate = ValidateDate(excelDataVO.getShp_invoice_date());
          if (datestate.equalsIgnoreCase("invalid"))
          {
            tempStatus = "1";
            errorList.add("Invalid Shipping Invoice Date Please Enter DD-MM-YYYY Format");
          }
        }
        if ((excelDataVO.getFob_amt() == null) || (excelDataVO.getFob_amt() == "") ||
          (excelDataVO.getFob_amt() == " "))
        {
          tempStatus = "1";
          errorList.add("Please Enter FOB_AMT");
        }
        if ((excelDataVO.getFreight_amt() == null) || (excelDataVO.getFreight_amt() == "") ||
          (excelDataVO.getFreight_amt() == " "))
        {
          tempStatus = "1";
          errorList.add("Please Enter FREIGHT_AMT");
        }
        if ((excelDataVO.getIns_amt() == null) || (excelDataVO.getIns_amt() == "") ||
          (excelDataVO.getIns_amt() == " "))
        {
          tempStatus = "1";
          errorList.add("Please Enter Insurance Amount ");
        }
        if ((excelDataVO.getComm_amt() == null) || (excelDataVO.getComm_amt() == "") ||
          (excelDataVO.getComm_amt() == " "))
        {
          tempStatus = "1";
          errorList.add("Please Enter Commission Amount");
        }
        if ((excelDataVO.getDiscount_amt() == null) || (excelDataVO.getDiscount_amt() == "") ||
          (excelDataVO.getDiscount_amt() == " "))
        {
          tempStatus = "1";
          errorList.add("Please Enter Discount Amount");
        }
        if ((excelDataVO.getDeduction_amt() == null) || (excelDataVO.getDeduction_amt() == "") ||
          (excelDataVO.getDeduction_amt() == " "))
        {
          tempStatus = "1";
          errorList.add("Please Enter Deduction Amount");
        }
        if ((excelDataVO.getPackage_amt() == null) || (excelDataVO.getPackage_amt() == "") ||
          (excelDataVO.getPackage_amt() == " "))
        {
          tempStatus = "1";
          errorList.add("Please Enter Package Amount");
        }
        if (excelDataVO.getRecord_indicator() != null)
        {
          if (excelDataVO.getRecord_indicator().equalsIgnoreCase("3"))
          {
            tempStatus = "1";
            errorList.add(" Record Indicator is 3 this Record is Not Allowed for Transaction ");
          }
          else if ((!excelDataVO.getRecord_indicator().equalsIgnoreCase("1")) &&
            (!excelDataVO.getRecord_indicator().equalsIgnoreCase("2")) && (!excelDataVO.getRecord_indicator().equalsIgnoreCase("3")))
          {
            tempStatus = "1";
            errorList.add("Record Indicator should be [1,2,3]");
          }
        }
        else if ((excelDataVO.getRecord_indicator() == null) || (excelDataVO.getRecord_indicator() == "") || (excelDataVO.getRecord_indicator() == " "))
        {
          tempStatus = "1";
          errorList.add("Please Enter Record Indicator");
        }
      }
      if ((excelDataVO.getShipmentfromcountry() != null) && (excelDataVO.getShipmentfromcountry() != "") &&
        (excelDataVO.getShipmentfromcountry() != " "))
      {
        ResultSet rs2 = null;
        String qquery = "select C7CNA from c7pf where C7CNA   = '" + excelDataVO.getShipmentfromcountry() +
          "' ";
        LoggableStatement ls2 = new LoggableStatement(con, qquery);
        rs2 = ls2.executeQuery();
        if (!rs2.next())
        {
          tempStatus = "1";
          errorList.add("Shipment From Country not in Master");
        }
        DBConnectionUtility.surrenderDB(null, ls2, rs2);
      }
      else
      {
        tempStatus = "1";
        errorList.add("Please Enter Shipment From Country");
      }
      if ((excelDataVO.getShipmenttocountry() != null) && (excelDataVO.getShipmenttocountry() != "") &&
        (excelDataVO.getShipmenttocountry() != " "))
      {
        ResultSet rs2 = null;
        String qquery = "select C7CNA from c7pf where C7CNA   = '" + excelDataVO.getShipmenttocountry() + "' ";
        LoggableStatement ls2 = new LoggableStatement(con, qquery);
        rs2 = ls2.executeQuery();
        if (!rs2.next())
        {
          tempStatus = "1";
          errorList.add("Shipment to Country not in Master");
        }
        DBConnectionUtility.surrenderDB(null, ls2, rs2);
      }
      else
      {
        tempStatus = "1";
        errorList.add("Please Enter Shipment to Country");
      }
      if ((excelDataVO.getHscode() != null) && (excelDataVO.getHscode() != "") && (excelDataVO.getHscode() != " "))
      {
        ResultSet rs2 = null;
        String qquery = "select HCODEE from EXTHMCODE where HCODEE = '" + excelDataVO.getHscode() + "' ";
        LoggableStatement ls2 = new LoggableStatement(con, qquery);
        rs2 = ls2.executeQuery();
        if (!rs2.next())
        {
          tempStatus = "1";
          errorList.add("Harmonised code not in Master");
        }
        DBConnectionUtility.surrenderDB(null, ls2, rs2);
      }
      else
      {
        tempStatus = "1";
        errorList.add("Please Enter Harmonised code");
      }
      if ((excelDataVO.getGoodscode() != null) && (excelDataVO.getGoodscode() != "") &&
        (excelDataVO.getGoodscode() != " "))
      {
        ResultSet rs2 = null;
        String qquery = "select CODE79 from GOODSC87 where CODE79 = '" + excelDataVO.getGoodscode() + "' ";
        LoggableStatement ls2 = new LoggableStatement(con, qquery);
       
        rs2 = ls2.executeQuery();
        if (!rs2.next())
        {
          tempStatus = "1";
          errorList.add("Goods Code not in Master");
        }
        DBConnectionUtility.surrenderDB(null, ls2, rs2);
      }
      else
      {
        tempStatus = "1";
        errorList.add("Please Enter Goods Code");
      }
      if ((excelDataVO.getIncoterms() != null) && (excelDataVO.getIncoterms() != "") &&
        (excelDataVO.getIncoterms() != " "))
      {
        ResultSet rs2 = null;
        String qquery = "SELECT * FROM EXTINCOTERM WHERE CODE= '" + excelDataVO.getIncoterms() + "' ";
        LoggableStatement ls2 = new LoggableStatement(con, qquery);
       
        rs2 = ls2.executeQuery();
        if (!rs2.next())
        {
          tempStatus = "1";
          errorList.add("Incoterms not in Master");
        }
        DBConnectionUtility.surrenderDB(null, ls2, rs2);
      }
      else
      {
        tempStatus = "1";
        errorList.add("Please Enter Incoterms");
      }
      if ((excelDataVO.getGooddescription() == null) || (excelDataVO.getGooddescription() == "") ||
        (excelDataVO.getGooddescription() == " "))
      {
        tempStatus = "1";
        errorList.add("Please Enter Goods Description ");
      }
      if ((excelDataVO.getTransportdocno() == null) || (excelDataVO.getTransportdocno() == "") ||
        (excelDataVO.getTransportdocno() == " "))
      {
        tempStatus = "1";
        errorList.add("Please Enter Transport Document Number");
      }
      if ((excelDataVO.getInvoiceno() == null) || (excelDataVO.getInvoiceno() == "") ||
        (excelDataVO.getInvoiceno() == " "))
      {
        tempStatus = "1";
        errorList.add("Please Enter Invoice Number");
      }
      if ((excelDataVO.getPortofdestination() != null) && (excelDataVO.getPortofdestination() != "") &&
        (excelDataVO.getPortofdestination() != " "))
      {
        ResultSet rs2 = null;
        String qquery = "SELECT * FROM EXTPORTDESTINATION WHERE PODEST = '" + excelDataVO.getPortofdestination() +
          "' ";
        LoggableStatement ls2 = new LoggableStatement(con, qquery);
       
        rs2 = ls2.executeQuery();
        if (!rs2.next())
        {
          tempStatus = "1";
          errorList.add("Port of Destination not in Master");
        }
        DBConnectionUtility.surrenderDB(null, ls2, rs2);
      }
      else
      {
        tempStatus = "1";
        errorList.add("Please Enter Port of Destination");
      }
      if ((excelDataVO.getChargeAccount() != null) && (excelDataVO.getChargeAccount() != "") && (excelDataVO.getChargeAccount() != " "))
      {
        logger.info("Charge Account Number Checking ");
        ResultSet rs2 = null;
       




        String qquery = "select * from account where BO_ACCTNO='" + excelDataVO.getChargeAccount() + "'";
       

        logger.info("Validating Charge  ---------------->" + qquery);
       
        LoggableStatement ls2 = new LoggableStatement(con, qquery);
       
        rs2 = ls2.executeQuery();
        if (!rs2.next())
        {
          tempStatus = "1";
          errorList.add("Charge Debit Account Number Not Available in TI");
        }
        DBConnectionUtility.surrenderDB(null, ls2, rs2);
      }
      else if (excelDataVO.getDrawer() != null)
      {
        logger.info("Validating Drawee ---------------->");
        ResultSet rs2 = null;
       
        String qquery = "SELECT * FROM ACCOUNT AC,GVPF GP  WHERE GP.GVMVT='C' AND GP.GVPRF='R' AND GP.GVACCT=AC.ACCT_KEY  AND AC.CUS_MNM ='" +
          excelDataVO.getDrawer() + "' ";
       

        LoggableStatement ls2 = new LoggableStatement(con, qquery);
       
        rs2 = ls2.executeQuery();
        if (!rs2.next())
        {
          tempStatus = "1";
          errorList.add("Settlement Instruction Not Available for this Customer");
        }
        DBConnectionUtility.surrenderDB(null, ls2, rs2);
      }
      else if ((excelDataVO.getChargeAccount() == null) || (excelDataVO.getChargeAccount() == "") || (excelDataVO.getChargeAccount() == " "))
      {
        tempStatus = "1";
        errorList.add("Please Enter Charge Account Number");
      }
      if ((excelDataVO.getForm_type().equals("EDI")) || (excelDataVO.getForm_type().equals("EDF")) || (
        (excelDataVO.getForm_type().equals("EXEMPTED")) && (excelDataVO.getForm_type().equals("SOFTEX"))))
      {
        if (excelDataVO.getTransportdocdate() != null)
        {
          String datestate = ValidateDate(excelDataVO.getTransportdocdate());
          if (datestate.equalsIgnoreCase("invalid"))
          {
            tempStatus = "1";
            errorList.add("Invalid Transport Doc Date Please Enter DD-MM-YYYY");
          }
        }
        else
        {
          tempStatus = "1";
          errorList.add("Please Enter Transport Doc Date in DD-MM-YYYY format");
        }
        if (excelDataVO.getInvoicedate() != null)
        {
          String datestate = ValidateDate(excelDataVO.getInvoicedate());
          if (datestate.equalsIgnoreCase("invalid"))
          {
            tempStatus = "1";
            errorList.add("Invalid Invoice Date Please Enter DD-MM-YYYY Format");
          }
        }
        else
        {
          tempStatus = "1";
          errorList.add("Please Enter Invoice Date in DD-MM-YYYY Format ");
        }
      }
      if ((excelDataVO.getRemitter_date() != null) && (excelDataVO.getRemitter_date() != "") && (excelDataVO.getRemitter_date() != " ")) {
        if (excelDataVO.getRemitter_date() != null)
        {
          String datestate = ValidateDate(excelDataVO.getRemitter_date());
          if (datestate.equalsIgnoreCase("invalid"))
          {
            tempStatus = "1";
            errorList.add("Invalid Remitter Date Please Enter DD-MM-YYYY FORMAT");
          }
        }
      }
      if ((excelDataVO.getForm_type().equals("EDI")) || (excelDataVO.getForm_type().equals("SOFTEX")) || (excelDataVO.getForm_type().equals("EDF")))
      {
        if ((excelDataVO.getRepamt_wrt_amt() == null) || (excelDataVO.getRepamt_wrt_amt() == "") ||
          (excelDataVO.getRepamt_wrt_amt() == " "))
        {
          tempStatus = "1";
          errorList.add("Please Enter Rrepay Write of Amount");
        }
        String short_shp_amt = excelDataVO.getShort_shp_amt();
        if (excelDataVO.getRepamt_wrt_amt() != null)
        {
          String repay_amt = excelDataVO.getRepamt_wrt_amt();
         

          double amt = 0.0D;
          if ((repay_amt == null) || (repay_amt == "") || (repay_amt == " "))
          {
            tempStatus = "1";
           
            errorList.add("Please Enter Valid Repay Amount Default 0 or above amount");
          }
          else
          {
            amt = Double.valueOf(repay_amt).doubleValue();
          }
        }
        if ((short_shp_amt == null) || (short_shp_amt == "") || (short_shp_amt == " "))
        {
          logger.info("short shipment amount value");
         
          tempStatus = "1";
         
          errorList.add("Please Enter Valid Repay Amount Default 0 or above amount");
        }
        if ((excelDataVO.getShortcollectionamount() == null) || (excelDataVO.getShortcollectionamount() == "") ||
          (excelDataVO.getShortcollectionamount() == " "))
        {
          tempStatus = "1";
          errorList.add("Please Enter Short Collection  Amount");
        }
        if ((excelDataVO.getShp_currency() == null) || (excelDataVO.getShp_currency() == "") || (excelDataVO.getShp_currency() == " "))
        {
          tempStatus = "1";
          errorList.add("Please Enter Shipment Currency");
        }
      }
      boolean multiple_rem = false;
     
      ResultSet rs2 = null;
     
      double remitance_remittance_Amount = 0.0D;
      String transactions_ext = "";
      String transactions_ext_ccy = "";
      double transactions_ext_amt = 0.0D;
      double r_rem_amt = 0.0D;
      double staging_tble_Utlization_Amt = 0.0D;
      String rem = null;
      ResultSet rs = null;
      ResultSet rs1 = null;
      ResultSet rs3 = null;
      ResultSet rs4 = null;
     
      boolean exceucte_remitance = false;
      boolean firc_status = false;
      boolean ready = false;
      if ((excelDataVO.getRemittance_num().trim().equals("")) && (excelDataVO.getFirc_no().trim().equals("")))
      {
        tempStatus = "1";
        errorList.add("Please Enter Remittance Number OR  FIRC Number");
      }
      if ((excelDataVO.getRemittance_num() != null) && (excelDataVO.getFirc_no() != null)) {
        ready = false;
      }
      if ((excelDataVO.getRemittance_num() != null) || (excelDataVO.getFirc_no() != null))
      {
        ready = true;
        exceucte_remitance = true;
      }
      if ((excelDataVO.getFirc_no() != null) && (excelDataVO.getFirc_no() != "") && (excelDataVO.getFirc_no() != " ")) {
        firc_status = true;
      }
      if ((excelDataVO.getRemittance_num() != null) && (excelDataVO.getRemittance_num() != "") &&
        (excelDataVO.getRemittance_num() != " ") && (excelDataVO.getFirc_no() != null) &&
        (excelDataVO.getFirc_no() != "") && (excelDataVO.getFirc_no() != " "))
      {
        tempStatus = "1";
        errorList.add("Remittance Number and Firc Number Both Not Allowed");
      }
      if ((excelDataVO.getForm_type().equalsIgnoreCase("EDI")) || (excelDataVO.getForm_type().equalsIgnoreCase("SOFTEX")) || (excelDataVO.getForm_type().equalsIgnoreCase("EXEMPTED")) || (excelDataVO.getForm_type().equalsIgnoreCase("EDF")))
      {
        String remittance_currency = null;
        if (exceucte_remitance)
        {
          boolean rem_state = true;
          if (ready) {
            if ((excelDataVO.getRemittance_num() != null) && (excelDataVO.getRemittance_num() != "") && (excelDataVO.getRemittance_num() != " "))
            {
              String query = "SELECT COUNT(REMITTANCE_NUM) AS  R,SHP_CURRENCY FROM ETTV_REMITANCE_AMT WHERE trim(REMITTANCE_NUM)='" + excelDataVO.getRemittance_num().trim() + "' GROUP BY SHP_CURRENCY";
             
              LoggableStatement ls2 = new LoggableStatement(con, query);
              rs2 = ls2.executeQuery();
              int a = 0;
              if (rs2.next())
              {
                a = Integer.valueOf(rs2.getString("R")).intValue();
               
                remittance_currency = rs2.getString("SHP_CURRENCY");
              }
              if (a == 0)
              {
                tempStatus = "1";
                errorList.add("Invalid Remittance Number");
              }
              else
              {
                rem_state = true;
              }
              DBConnectionUtility.surrenderDB(null, ls2, rs2);
            }
          }
          double collection_amt = 0.0D;
          if (rem_state)
          {
            String qeue = "SELECT NVL(REMITTANCE_NUM,'F')  AS REMITTANCE_NUM, NVL(FIRC_NO,'R') AS FIRC_NO,SUM(NVL(UTILIZATION_AMOUNT,'0')) AS UTILIZATION_AMOUNT, SUM(NVL(SHORTCOLLECTIONAMOUNT,0)) AS SHORTCOLLECTIONAMOUNT  FROM ETTDM_BULKUPLOAD_STAGING  WHERE BATCH_NO    ='" +
           

              batchId + "'  AND THEIRREF      ='" + excelDataVO.getTheirref() +
              "' GROUP BY NVL(REMITTANCE_NUM,'F'), NVL(FIRC_NO,'R')";
           
            LoggableStatement ls = new LoggableStatement(con, qeue);
            rs = ls.executeQuery();
            while (rs.next())
            {
              rem = rs.getString("FIRC_NO");
             
              String rem_no = rs.getString("REMITTANCE_NUM");
              if (rem.equals("R"))
              {
                staging_tble_Utlization_Amt += Double.parseDouble(rs.getString("UTILIZATION_AMOUNT"));
                logger.info("***************Sting util Amt---------------------->" + staging_tble_Utlization_Amt);
              }
              collection_amt += Double.parseDouble(rs.getString("SHORTCOLLECTIONAMOUNT"));
             
              logger.info("Short Collection amount--------> " + collection_amt);
             
              String abc = String.format("%.2f", new Object[] { Double.valueOf(staging_tble_Utlization_Amt) });
             

              logger.info("***************After Conversion---------------------->" + abc);
             

              staging_tble_Utlization_Amt = Double.valueOf(abc).doubleValue();
             
              String query1 = "SELECT REMITTANCE_NUM,NVL(REMITTANCE_AMOUNT,'0') AS REMITTANCE_AMOUNT FROM ETTV_REMITANCE_AMT WHERE REMITTANCE_NUM='" +
                rem_no + "' GROUP BY REMITTANCE_NUM,REMITTANCE_AMOUNT ";
              LoggableStatement lse = new LoggableStatement(con, query1);
              rs1 = lse.executeQuery();
              while (rs1.next())
              {
                String remit = rs1.getString("REMITTANCE_AMOUNT");
                remitance_remittance_Amount += Double.parseDouble(remit);
                logger.info("Total Remittance AMount------------------------:" + remitance_remittance_Amount);
              }
              DBConnectionUtility.surrenderDB(null, lse, rs1);
             
              ResultSet rser = null;
              String query = "SELECT nvl(listagg(trim(mas.master_ref),',') within group (order by mas.master_ref),'') utilized_ref, ext.ccy_1,NVL(SUM(THEME_GENIUS_PKG.CONVAMT( ext.ccy_1, ext.amtutil) ),0) AS TOTAL_AMT FROM exteventadv ext, BASEEVENT bev,MASTER mas WHERE ext.FK_EVENT = bev.EXTFIELD AND mas.KEY97 = bev.MASTER_KEY AND bev.STATUS    IN ('i','c')  AND ext.inward     ='" +
             

                rem_no + "' GROUP BY ext.ccy_1 ";
             
              LoggableStatement lser = new LoggableStatement(con, query);
              logger.info("transactions_ext_amt-------------Query---------->" + lser.getQueryString());
              rser = lser.executeQuery();
              if (rser.next())
              {
                transactions_ext = rser.getString(1);
                transactions_ext_ccy = rser.getString(2);
                transactions_ext_amt = rser.getDouble(3);
              }
              logger.info("------------transactions_ext_amt----------------------" + transactions_ext_amt);
              DBConnectionUtility.surrenderDB(null, lser, rser);
            }
            DBConnectionUtility.surrenderDB(null, ls, rs);
          }
          double firc_utilamt = 0.0D;
         
          String querss = "SELECT THEIRREF,NVL(UTILIZATION_AMOUNT,'0') AS UTILIZATION_AMOUNT,NVL(REMITTANCE_NUM,'F') AS FIRC_NO FROM ETTDM_BULKUPLOAD_STAGING WHERE THEIRREF='" +
         
            excelDataVO.getTheirref() + "' " + "AND BATCH_NO  ='" + batchId + "' ";
          LoggableStatement lls = new LoggableStatement(con, querss);
          rs3 = lls.executeQuery();
          while (rs3.next())
          {
            String firc = rs3.getString("FIRC_NO");
            if (firc.equals("F")) {
              firc_utilamt += Double.parseDouble(rs3.getString("UTILIZATION_AMOUNT"));
            }
          }
          DBConnectionUtility.surrenderDB(null, lls, rs3);
         
          String r_balance_sate = null;
          double r_balance = 0.0D;
         
          String remitance_balance = "SELECT count( case  when trim(R_BALANCE) is not null then R_BALANCE  else 0 end  ) AS DATA FROM ETTDM_BULKUPLOAD_STAGING WHERE REMITTANCE_NUM='" +
            excelDataVO.getRemittance_num() + "' " + " AND THEIRREF NOT   IN ('" +
            excelDataVO.getTheirref() + "')  AND BATCH_NO ='" + batchId + "' ";
         
          LoggableStatement lses = new LoggableStatement(con, remitance_balance);
          rs2 = lses.executeQuery();
         
          boolean r_status = false;
          while (rs2.next())
          {
            double r_s = Double.parseDouble(rs2.getString("DATA"));
            if (r_s == 0.0D) {
              r_status = true;
            }
          }
          DBConnectionUtility.surrenderDB(null, lses, rs2);
          if (r_status)
          {
            r_balance_sate = "R_BALANCE_NEW";
          }
          else
          {
            String Balance1 = "SELECT NVL(R_BALANCE,'0.5') AS R_BALANCE FROM ETTDM_BULKUPLOAD_STAGING WHERE REMITTANCE_NUM='" +
              excelDataVO.getRemittance_num() + "' AND THEIRREF NOT   IN ('" +
              excelDataVO.getTheirref() + "') " + " AND BATCH_NO  ='" + batchId + "' ";
           









            lses = new LoggableStatement(con, Balance1);
            logger.info("R-BALANCE QUERY--------->" + Balance1);
           

            rs2 = lses.executeQuery();
            if (rs2.next())
            {
              r_balance += Double.parseDouble(rs2.getString("R_BALANCE"));
             
              logger.info("R_BALANCE---------------" + r_balance);
              if (r_balance == 0.5D)
              {
                r_balance = 0.0D;
                r_balance_sate = "R_BALANCE_NEW";
              }
              else if (r_balance > 0.0D)
              {
                r_balance_sate = "R_BALANCE_AVAIL";
              }
              else if (r_balance < 0.0D)
              {
                r_balance_sate = "R_BALANCE_NEGATIVE";
              }
              else if (r_balance == 0.0D)
              {
                r_balance_sate = "R_BALANCE_ZERO";
              }
            }
          }
          DBConnectionUtility.surrenderDB(null, lses, rs2);
         
          double balance = 0.0D;
          if ((r_balance_sate.equalsIgnoreCase("R_BALANCE_AVAIL")) || (r_balance_sate.equalsIgnoreCase("R_BALANCE_NEGATIVE")) || (r_balance_sate.equalsIgnoreCase("R_BALANCE_ZERO")))
          {
            r_rem_amt = r_balance - staging_tble_Utlization_Amt;
          }
          else if (r_balance_sate.equalsIgnoreCase("R_BALANCE_NEW"))
          {
            r_rem_amt = remitance_remittance_Amount - transactions_ext_amt;
           
            logger.info("Remitance_remittance_Amount------------------>" + remitance_remittance_Amount);
            logger.info("Transaction Amount-------------------------+" + transactions_ext_amt);
            logger.info("r_rem_amt---------------------------------->" + r_rem_amt);
           

            logger.info("r_rem_amt--->" + r_rem_amt + "=(Remitance_remittance_Amount)" + remitance_remittance_Amount + "(Transactions_ext_amt)-" + transactions_ext_amt);
           



            r_rem_amt = getRemittanceBalance(r_rem_amt, staging_tble_Utlization_Amt);
           




            logger.info("Remittance Amount---->" + r_rem_amt + " - " + "Utilization AMount --->" + " " + staging_tble_Utlization_Amt);
            logger.info("r_rem_amt@@" + balance + "Remittance Amount" + r_rem_amt + "Staging Utilization AMount" + staging_tble_Utlization_Amt);
          }
          if (r_rem_amt < 0.0D)
          {
            tempStatus = "1";
            if ((transactions_ext_ccy == null) || (transactions_ext_ccy == "") || (transactions_ext == null) || (transactions_ext == "")) {
              errorList.add("Remittance Amount Not Available to Utilize");
            } else {
              errorList.add("Remittance Amount Not Available to Utilize. Already utilized amount " +
                transactions_ext_amt + " " + transactions_ext_ccy + " in (" + transactions_ext + ")");
            }
          }
          String qu = "UPDATE ETTDM_BULKUPLOAD_STAGING  SET R_BALANCE='" + r_rem_amt +
            "' WHERE  REMITTANCE_NUM='" + excelDataVO.getRemittance_num() + "'" + "AND BATCH_NO='" +
            batchId + "'  ";
         
          logger.info("balance Update QUery --------------------" + qu);
         
          LoggableStatement lse = new LoggableStatement(con, qu);
          int row = lse.executeUpdate();
          DBConnectionUtility.surrenderDB(null, lse, null);
         





          double collection_amt_1 = 0.0D;
         



















          collection_amt_1 = Double.valueOf(collection_amt).doubleValue();
         

          logger.info("staging_tble_Utlization_Amt------>" + staging_tble_Utlization_Amt);
          logger.info("firc_utilamt________________>" + firc_utilamt);
         
          logger.info("collection_amt_1-------------->" + collection_amt_1);
         

          double sp_rate_1 = 1.0D;
          ResultSet rs11 = null;
          ResultSet rsval = null;
          LoggableStatement va = null;
          LoggableStatement val1 = null;
          String shp_currency_1 = "";
         
          String collection_currency = excelDataVO.getCollectioncurrency();
          if ((excelDataVO.getRemittance_num() != null) && (excelDataVO.getRemittance_num() != "") && (excelDataVO.getRemittance_num() != " "))
          {
            String collection_currency_1 = "SELECT REMITTANCE_NUM,NVL(REMITTANCE_AMOUNT,'0') AS REMITTANCE_AMOUNT,SHP_CURRENCY \tFROM ETTV_REMITANCE_AMT WHERE REMITTANCE_NUM='" + excelDataVO.getRemittance_num() + "' " +
              " GROUP BY REMITTANCE_NUM, REMITTANCE_AMOUNT,SHP_CURRENCY";
           
            va = new LoggableStatement(con, collection_currency_1);
            rs11 = va.executeQuery();
            if (!rs11.next()) {
              shp_currency_1 = "USD";
            } else {
              shp_currency_1 = rs11.getString("SHP_CURRENCY");
            }
            DBConnectionUtility.surrenderDB(null, va, rs11);
            logger.info("Collection Currency--------------------->" + collection_currency_1);
            String spot_rate = "SELECT ETT_SPOTRATE_CAL('" + collection_currency + "','" + shp_currency_1 + "') AS RATE FROM DUAL";
           
            val1 = new LoggableStatement(con, spot_rate);
           
            logger.info("Loggable Statement Query--------------->" + val1.getQueryString());
            rsval = val1.executeQuery();
            while (rsval.next()) {
              sp_rate_1 = Double.valueOf(rsval.getString("RATE")).doubleValue();
            }
            staging_tble_Utlization_Amt *= sp_rate_1;
          }
          DBConnectionUtility.surrenderDB(null, val1, rsval);
         

          double balance1 = staging_tble_Utlization_Amt + firc_utilamt + collection_amt_1;
          logger.info("Utilization Amount-------------------------->" + balance1);
         


          String collection_Amt = excelDataVO.getCollectionamount().trim();
         

          double collection_amts = Double.parseDouble(collection_Amt);
         
          logger.info("Collection Amount--------------------->" + collection_amts);
         
          String checking_remittance_firccurrency = excelDataVO.getFirc_no();
          String collection_currency_1 = excelDataVO.getCollectioncurrency();
          if (checking_remittance_firccurrency != null) {
            remittance_currency = collection_currency;
          }
          logger.info("Remittance Currency----------------->" + remittance_currency);
          logger.info("Collection Currency----------------->" + collection_currency_1);
          if (remittance_currency.equalsIgnoreCase(collection_currency_1))
          {
            logger.info("Collection Currency matches");
            if (collection_amts > 1.0D)
            {
              if (Math.round(collection_amts * 100.0D) / 100.0D != Math.round(balance1 * 100.0D) / 100.0D)
              {
                tempStatus = "1";
                errorList.add("Collection Amount not matches with Utilization Amount");
              }
              if (Math.round(collection_amts * 100.0D) / 100.0D > Math.round(balance1 * 100.0D) / 100.0D)
              {
                tempStatus = "1";
                errorList.add("Collection Amount Greater than  Remittance Amount");
              }
            }
          }
          else
          {
            ResultSet rsva = null;
            LoggableStatement val = null;
            LoggableStatement vaes = null;
           
            double sp_rate = 0.0D;
            double collection_amt_123 = 0.0D;
           

            String spot_rate = "SELECT ETT_SPOTRATE_CAL('" + collection_currency + "','" + remittance_currency + "') AS RATE FROM DUAL";
           
            vaes = new LoggableStatement(con, spot_rate);
            rsva = vaes.executeQuery();
            while (rsva.next()) {
              sp_rate = Double.valueOf(rsva.getString("RATE")).doubleValue();
            }
            logger.info("Collection Currency--------------------->" + collection_currency);
            logger.info("Remittance Currency--------------------->" + remittance_currency);
            logger.info("Exchange Rate--------------------->" + sp_rate);
            logger.info("REmittance Amount----------------->" + remitance_remittance_Amount);
           


            logger.info("Collection Currency--------------------->" + collection_currency);
            logger.info("Remittance Currency--------------------->" + remittance_currency);
            logger.info("Exchange Rate--------------------->" + sp_rate);
            logger.info("REmittance Amount----------------->" + remitance_remittance_Amount);
           

            collection_amt_123 = staging_tble_Utlization_Amt * sp_rate;
           

            double r_amt = Double.valueOf(collection_amt_123).doubleValue();
           
            logger.info("Rounding Math Amount------------>" + r_amt);
            if (Math.round(collection_amts * 100.0D) / 100.0D != Math.round(r_amt * 100.0D) / 100.0D)
            {
              tempStatus = "1";
              errorList.add("Collection Amount not matches with Utilization Amount");
            }
            String abc = String.format("%.2f", new Object[] { Double.valueOf(collection_amt_123) });
           


            logger.info("Collection Multiply Amount------------>" + collection_amt_123);
           


            DBConnectionUtility.surrenderDB(null, vaes, rsva);
           
            logger.info("Not Matches");
          }
        }
        if (firc_status)
        {
          logger.info(" Remittance AD Code" + excelDataVO.getRemittance_adcode());
          if ((excelDataVO.getRemittance_adcode() == null) || (excelDataVO.getRemittance_adcode() == "") ||
            (excelDataVO.getRemittance_adcode() == " "))
          {
            tempStatus = "1";
            errorList.add("Please Enter Remittance AD Code");
          }
          if ((excelDataVO.getRemitter_name() == null) || (excelDataVO.getRemitter_name() == "") ||
            (excelDataVO.getRemitter_name() == " "))
          {
            tempStatus = "1";
            errorList.add("Please Enter Remitter Name");
          }
          if ((excelDataVO.getRemitter_date() == null) || (excelDataVO.getRemitter_date() == "") ||
            (excelDataVO.getRemitter_date() == " "))
          {
            tempStatus = "1";
            errorList.add("Please Enter Remitter Date");
          }
          if ((excelDataVO.getRemitter_country() == null) || (excelDataVO.getRemitter_country() == "") ||
            (excelDataVO.getRemitter_country() == " "))
          {
            tempStatus = "1";
            errorList.add("Please Enter Remitter Country");
          }
          if ((excelDataVO.getAvailable_amount() == null) || (excelDataVO.getAvailable_amount() == "") ||
            (excelDataVO.getAvailable_amount() == " "))
          {
            tempStatus = "1";
            errorList.add("Please Enter Remittance Amount");
          }
          if ((excelDataVO.getShp_currency() == null) || (excelDataVO.getShp_currency() == "") ||
            (excelDataVO.getShp_currency() == " "))
          {
            tempStatus = "1";
            errorList.add("Please Enter Shipping Currency");
          }
          if ((excelDataVO.getUtilization_amount() == null) || (excelDataVO.getUtilization_amount() == "") ||
            (excelDataVO.getUtilization_amount() == " "))
          {
            tempStatus = "1";
            errorList.add("Please Enter Utilization Amount");
          }
          if ((excelDataVO.getCif_number() == null) || (excelDataVO.getCif_number() == "") ||
            (excelDataVO.getCif_number() == " "))
          {
            tempStatus = "1";
            errorList.add("Please Enter Cif Number");
          }
        }
      }
      String errorString = "";
      for (int i = 0; i < errorList.size(); i++) {
        if (errorString.equalsIgnoreCase("")) {
          errorString = (String)errorList.get(i);
        } else {
          errorString = errorString + ", " + (String)errorList.get(i);
        }
      }
      logger.info("Error---------:" + errorString);
      excelDataVO.setErrordtls(errorString);
     
      logger.info("tempStatus===" + tempStatus);
     
      String errorStatus = "SUCCESS";
      if (tempStatus.equalsIgnoreCase("1")) {
        errorStatus = "FAILED";
      }
      updateValidationStatus(batchId, excelDataVO, errorStatus, errorString);
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      logger.info("Validation Exception------------> " + e);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(null, null, null);
    }
  }
 
  public void updateValidationStatus(String batchId, ExcelDataVO excelDataVO, String errorStatus, String errorString)
  {
    LoggableStatement pst = null;
    Connection con = null;
    try
    {
      con = DBConnectionUtility.getConnection();
      if (errorString == null) {
        errorString = " ";
      }
      String query = "UPDATE ETTDM_BULKUPLOAD_STAGING SET STATUS = '" + errorStatus + "', ERRORDESCRIPTION = '" +
        errorString + "'" + " WHERE BATCH_NO = '" + batchId + "'  AND SNUMBER ='" +
        excelDataVO.getSnumber() + "' ";
     
      logger.info("Update Query-------------------------->" + query);
     
      pst = new LoggableStatement(con, query);
      int row = pst.executeUpdate();
     
      logger.info("Error Updation status----count-----------" + row);
      logger.info("Error Updation status----count-----------" + row);
    }
    catch (Exception e)
    {
      logger.info("Exception in updateValidationStatus - " + e.getMessage() + " for the reference - " +
        excelDataVO.getTheirref());
      logger.info("Exception in updateValidationStatus - " + e.getMessage() + " for the reference - " +
        excelDataVO.getTheirref());
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, null);
    }
  }
 
  public String ValidateDate(String input)
  {
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    sdf.setLenient(false);
    try
    {
      Date localDate = sdf.parse(input);
    }
    catch (ParseException e)
    {
      return input = "invalid";
    }
    return input = "valid";
  }
 
  public String validateBasedOnFormType(Connection con, String formType, ExcelDataVO excelDataVO)
  {
    String errorString = null;
    try
    {
      ResultSet rs4 = null;
      String query4 = null;
      LoggableStatement ls4 = null;
     
      ArrayList<String> errorList = new ArrayList();
      if (formType.equalsIgnoreCase("EDI"))
      {
        String shipBillNo = excelDataVO.getShpbill_no();
        String shipBillDate = excelDataVO.getBill_date();
        String portCode = excelDataVO.getPort_code();
        if ((shipBillNo != null) && (!shipBillNo.equalsIgnoreCase("")) && (!shipBillNo.equalsIgnoreCase(" ")) && (shipBillDate != null) && (!shipBillDate.equalsIgnoreCase("")) &&
          (!shipBillDate.equalsIgnoreCase(" ")) && (portCode != null) && (!portCode.equalsIgnoreCase("")) && (!portCode.equalsIgnoreCase(" ")))
        {
          ResultSet rs1 = null;
          String query1 = "SELECT SHIPBILLNO FROM ETT_EDPMS_SHP WHERE TRIM(SHIPBILLNO) = '" + shipBillNo +
            "' ";
          LoggableStatement ls1 = new LoggableStatement(con, query1);
         
          rs1 = ls1.executeQuery();
          if (!rs1.next())
          {
            errorList.add("Shipping Bill No is not in Master");
          }
          else
          {
            ResultSet rs2 = null;
            String query2 = "SELECT SHIPBILLNO FROM ETT_EDPMS_SHP WHERE TRIM(SHIPBILLNO) = '" + shipBillNo + "' AND TO_DATE(SHIPBILLDATE, 'DDMMYYYY') = TO_DATE('" + shipBillDate + "' , 'DD-MM-YYYY')";
            LoggableStatement ls2 = new LoggableStatement(con, query2);
           

            query4 = " SELECT COUNT(mas.MASTER_REF) T FROM exteventshc shc,baseevent bev,master mas WHERE mas.KEY97 = bev.MASTER_KEY  AND shc.fk_event = bev.extfield AND (bev.STATUS  = 'c' OR (mas.STATUS  = 'LIV' AND bev.STATUS  = 'i'))  and bev.STATUS = 'c' AND shc.CBILLNUM ='" +
           
              shipBillNo + "' AND TO_CHAR(TO_DATE(shc.CBILLDA,'DD/MM/YY'))=TO_DATE('" + shipBillDate + "','DD/MM/YY')AND shc.CPORTCO='" + portCode + "' ";
            ls4 = new LoggableStatement(con, query4);
           
            rs4 = ls4.executeQuery();
            if (rs4.next())
            {
              int a = Integer.valueOf(rs4.getString("T")).intValue();
              if (a > 0) {
                errorList.add("Shipping Bill Number, Bill Date,Port Code Already Used for TI Transactions");
              }
            }
            DBConnectionUtility.surrenderDB(null, ls4, rs4);
           
            rs2 = ls2.executeQuery();
            if (!rs2.next())
            {
              errorList.add("Shipping Bill Number and  Date is not matched with Shipping Bill No");
            }
            else
            {
              ResultSet rs3 = null;
              String query3 = "SELECT SHIPBILLNO FROM ETT_EDPMS_SHP WHERE TRIM(SHIPBILLNO) = '" + shipBillNo + "' AND TO_DATE(SHIPBILLDATE, 'DDMMYYYY') = TO_DATE('" + shipBillDate + "' , 'DD-MM-YYYY') AND TRIM(PORTCODE) = '" + portCode + "'";
              LoggableStatement ls3 = new LoggableStatement(con, query3);
              rs3 = ls3.executeQuery();
              if (!rs3.next()) {
                errorList.add("Port Code is not matched with Shipping Bill No");
              }
              DBConnectionUtility.surrenderDB(null, ls3, rs3);
            }
            DBConnectionUtility.surrenderDB(null, ls2, rs2);
          }
          DBConnectionUtility.surrenderDB(null, ls1, rs1);
        }
        else
        {
          errorList.add("Please Enter Shipping Bill Number,Bill Date,Port Code");
        }
      }
      else if (formType.equalsIgnoreCase("SOFTEX"))
      {
        String shipBillDate = excelDataVO.getBill_date();
        String portCode = excelDataVO.getPort_code();
        String formNo = excelDataVO.getForm_no();
        if ((formNo != null) && (!formNo.equalsIgnoreCase("")) && (!formNo.equalsIgnoreCase(" ")) && (shipBillDate != null) && (!shipBillDate.equalsIgnoreCase("")) && (!shipBillDate.equalsIgnoreCase(" ")) &&
          (portCode != null) && (!portCode.equalsIgnoreCase("")) && (!portCode.equalsIgnoreCase(" ")))
        {
          ResultSet rs1 = null;
          String query1 = "SELECT FORMNO FROM ETT_EDPMS_SHP_SOFTEX WHERE TRIM(FORMNO) = '" + formNo + "' ";
          LoggableStatement ls1 = new LoggableStatement(con, query1);
          rs1 = ls1.executeQuery();
          if (!rs1.next())
          {
            errorList.add("Form No is not in Master");
          }
          else
          {
            ResultSet rs2 = null;
            String query2 = "SELECT FORMNO FROM ETT_EDPMS_SHP_SOFTEX WHERE TRIM(FORMNO) = '" + formNo +
              "' AND TO_DATE(SHIPBILLDATE, 'DDMMYYYY') = TO_DATE('" + shipBillDate +
              "' , 'DD-MM-YYYY')";
            LoggableStatement ls2 = new LoggableStatement(con, query2);
            rs2 = ls2.executeQuery();
            if (!rs2.next())
            {
              errorList.add("Shipping Bill Date is not matched with Form No");
            }
            else
            {
              ResultSet rs3 = null;
              String query3 = "SELECT FORMNO FROM ETT_EDPMS_SHP_SOFTEX WHERE TRIM(FORMNO) = '" + formNo + "' AND TO_DATE(SHIPBILLDATE, 'DDMMYYYY') = TO_DATE('" + shipBillDate + "' , 'DD-MM-YYYY') AND TRIM(PORTCODE) = '" + portCode + "'";
              LoggableStatement ls3 = new LoggableStatement(con, query3);
              rs3 = ls3.executeQuery();
              if (!rs3.next()) {
                errorList.add("Port Code is not matched with Form No");
              }
              DBConnectionUtility.surrenderDB(null, ls3, rs3);
             
              query4 = " SELECT COUNT(mas.MASTER_REF) T FROM exteventshc shc,baseevent bev,master mas WHERE mas.KEY97 = bev.MASTER_KEY  AND shc.fk_event = bev.extfield AND (bev.STATUS  = 'c' OR (mas.STATUS  = 'LIV' AND bev.STATUS  = 'i'))  and bev.STATUS = 'c' AND shc.CFORMN ='" +
                formNo + "' AND TO_CHAR(TO_DATE('" + shipBillDate + "','DD/MM/YY'))=TO_DATE('" + shipBillDate + "','DD/MM/YY')AND shc.CPORTCO='" + portCode + "' ";
              ls4 = new LoggableStatement(con, query4);
             
              rs4 = ls4.executeQuery();
              if (rs4.next())
              {
                int a = Integer.valueOf(rs4.getString("T")).intValue();
                if (a > 0) {
                  errorList.add("Form Number, Bill Date,Port Code Already Used for TI Transactions");
                }
              }
              DBConnectionUtility.surrenderDB(null, ls4, rs4);
            }
            DBConnectionUtility.surrenderDB(null, ls2, rs2);
          }
          DBConnectionUtility.surrenderDB(null, ls1, rs1);
        }
        else
        {
          errorList.add("Please Enter Form  Number,Bill Date,Port Code");
        }
      }
      else if (formType.equalsIgnoreCase("EDF"))
      {
        String formNo = excelDataVO.getForm_no();
        String shipBillNo = excelDataVO.getShpbill_no();
        String shipBillDate = excelDataVO.getBill_date();
        String portCode = excelDataVO.getPort_code();
        if ((shipBillNo != null) && (shipBillNo != "") && (shipBillNo != " ") && (shipBillDate != null) && (shipBillDate != "") && (shipBillDate != " ") && (portCode != null) && (portCode != "") && (portCode != " "))
        {
          query4 =
         
            " SELECT COUNT(mas.MASTER_REF) T FROM exteventshc shc,baseevent bev,master mas WHERE mas.KEY97 = bev.MASTER_KEY  AND shc.fk_event = bev.extfield AND (bev.STATUS  = 'c' OR (mas.STATUS  = 'LIV' AND bev.STATUS  = 'i'))  and bev.STATUS = 'c' AND shc.CBILLNUM ='" + shipBillNo + "' AND TO_CHAR(TO_DATE(shc.CBILLDA,'DD/MM/YY'))=TO_DATE('" + shipBillDate + "','DD/MM/YY')AND shc.CPORTCO='" + portCode + "' ";
          ls4 = new LoggableStatement(con, query4);
         
          rs4 = ls4.executeQuery();
          if (rs4.next())
          {
            int a = Integer.valueOf(rs4.getString("T")).intValue();
            if (a > 0) {
              errorList.add("Shipping Bill Number, Bill Date,Port Code Already Used for TI Transactions");
            }
          }
          DBConnectionUtility.surrenderDB(null, ls4, rs4);
        }
        else if ((formNo != null) && (formNo != "") && (formNo != " ") && (shipBillDate != null) && (shipBillDate != "") && (shipBillDate != " ") && (portCode != null) && (portCode != "") && (portCode != " "))
        {
          query4 =
         
            " SELECT COUNT(mas.MASTER_REF) T FROM exteventshc shc,baseevent bev,master mas WHERE mas.KEY97 = bev.MASTER_KEY  AND shc.fk_event = bev.extfield AND (bev.STATUS  = 'c' OR (mas.STATUS  = 'LIV' AND bev.STATUS  = 'i'))  and bev.STATUS = 'c' AND shc.CBILLNUM ='" + shipBillNo + "' AND TO_CHAR(TO_DATE(shc.CBILLDA,'DD/MM/YY'))=TO_DATE('" + shipBillDate + "','DD/MM/YY')AND shc.CPORTCO='" + portCode + "' ";
          ls4 = new LoggableStatement(con, query4);
         
          rs4 = ls4.executeQuery();
          if (rs4.next())
          {
            int a = Integer.valueOf(rs4.getString("T")).intValue();
            if (a > 0) {
              errorList.add("Shipping Bill Number, Bill Date,Port Code Already Used for TI Transactions");
            }
          }
          DBConnectionUtility.surrenderDB(null, ls4, rs4);
        }
      }
      for (int i = 0; i < errorList.size(); i++)
      {
        if (i == 0) {
          errorString = (String)errorList.get(i);
        }
        if (i > 0) {
          errorString = errorString + ", " + (String)errorList.get(i);
        }
      }
      excelDataVO.setErrordtls(errorString);
    }
    catch (Exception e)
    {
      logger.info("Exception in validateExcel - " + e.getMessage() + " for the reference - " +
        excelDataVO.getTheirref());
      logger.info("Validation  Exception --------:" + errorString);
    }
    return errorString;
  }
 
  public String insertExcelData(ArrayList<ExcelDataVO> odcDataList, MakerHomeVO makervo)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    ResultSet rs = null;
    PreparedStatement ppt = null;
    ExcelDataVO excelDataVO = null;
    String batchId = null;
    String userId = null;
    try
    {
      con = DBConnectionUtility.getConnection();
      excelDataVO = new ExcelDataVO();
     
      HttpSession session = ServletActionContext.getRequest().getSession();
      userId = (String)session.getAttribute("loginedUserName");
     
      String query = "select ODC_SEQUENCE.nextval as batchno from dual";
      LoggableStatement pst = new LoggableStatement(con, query);
      ResultSet rs1 = pst.executeQuery();
      if (rs1.next()) {
        batchId = rs1.getString("batchno");
      }
      logger.info("The Query is : =================>>>  INSERT INTO ETTDM_BULKUPLOAD_STAGING (THEIRREF,COLLECTING_BANK,BEHALFOFBRANCH,DRAWER,DRAWEECUSTOMERID,DRAWEE_ADDRESS,DRAWEE_NAME,CHARGE_ACC,  COLLECTIONAMOUNT,COLLECTIONCURRENCY,SHIPMENTTOCOUNTRY,SHIPMENTFROMCOUNTRY,HSCODE,INCOTERMS,  GOODSCODE,GOODDESCRIPTION,PORTOFDESTINATION,PORTOFLOADING,TRANSPORTDOCNO,TRANSPORTDOCDATE,INVOICENO,INVOICEDATE,  FORM_TYPE,SHPBILL_NO,BILL_DATE,PORT_CODE,FORM_NO,SHP_AMT,SHP_CURRENCY,REPAMT_WRT_AMT,SHORTCOLLECTIONAMOUNT,REMITTANCE_NUM, RECORD_INDICATOR,LEO_DATE,EXPORT_AGENCY,EXPORT_TYPE,DESTINATION_COUNTRRY,IECODE,ADCODE,CUSTOMS_NUMBER,SHP_INV_SNO,  SHP_INVOICE_NO,SHP_INVOICE_DATE,FOB_AMT,FREIGHT_AMT,INS_AMT,COMM_AMT,DISCOUNT_AMT,DEDUCTION_AMT,PACKAGE_AMT,REMITTANCE_ADCODE,  FIRC_NO,REMITTER_NAME,REMITTER_DATE,REMITTER_COUNTRY,UTILIZATION_AMOUNT,CIF_NUMBER,AVAILABLE_AMOUNT,STATUS,BATCH_NO,SNUMBER,R_BALANCE,USERNAME,UPLOAD_DATE,REASON_SHORT_SHP_AMT,SHORT_SHP_AMT,UPDATED,HOSTNAME) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,ETTDM_BULKUPLOAD_STG_SEQUENCE.NEXTVAL,?,?,?,?,?,'N',?)");
      ppt = con.prepareStatement(" INSERT INTO ETTDM_BULKUPLOAD_STAGING (THEIRREF,COLLECTING_BANK,BEHALFOFBRANCH,DRAWER,DRAWEECUSTOMERID,DRAWEE_ADDRESS,DRAWEE_NAME,CHARGE_ACC,  COLLECTIONAMOUNT,COLLECTIONCURRENCY,SHIPMENTTOCOUNTRY,SHIPMENTFROMCOUNTRY,HSCODE,INCOTERMS,  GOODSCODE,GOODDESCRIPTION,PORTOFDESTINATION,PORTOFLOADING,TRANSPORTDOCNO,TRANSPORTDOCDATE,INVOICENO,INVOICEDATE,  FORM_TYPE,SHPBILL_NO,BILL_DATE,PORT_CODE,FORM_NO,SHP_AMT,SHP_CURRENCY,REPAMT_WRT_AMT,SHORTCOLLECTIONAMOUNT,REMITTANCE_NUM, RECORD_INDICATOR,LEO_DATE,EXPORT_AGENCY,EXPORT_TYPE,DESTINATION_COUNTRRY,IECODE,ADCODE,CUSTOMS_NUMBER,SHP_INV_SNO,  SHP_INVOICE_NO,SHP_INVOICE_DATE,FOB_AMT,FREIGHT_AMT,INS_AMT,COMM_AMT,DISCOUNT_AMT,DEDUCTION_AMT,PACKAGE_AMT,REMITTANCE_ADCODE,  FIRC_NO,REMITTER_NAME,REMITTER_DATE,REMITTER_COUNTRY,UTILIZATION_AMOUNT,CIF_NUMBER,AVAILABLE_AMOUNT,STATUS,BATCH_NO,SNUMBER,R_BALANCE,USERNAME,UPLOAD_DATE,REASON_SHORT_SHP_AMT,SHORT_SHP_AMT,UPDATED,HOSTNAME) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,ETTDM_BULKUPLOAD_STG_SEQUENCE.NEXTVAL,?,?,?,?,?,'N',?)");
      for (int i = 0; i < odcDataList.size(); i++)
      {
        logger.info("Insertion Time Starts------- >" + i + "<< " + new Timestamp(System.currentTimeMillis()));
        excelDataVO = (ExcelDataVO)odcDataList.get(i);
       
        ppt.setString(1, excelDataVO.getTheirref());
        ppt.setString(2, excelDataVO.getCollecting_bank());
        ppt.setString(3, excelDataVO.getBehalfofbranch());
        ppt.setString(4, excelDataVO.getDrawer());
        ppt.setString(5, excelDataVO.getDraweecustomerid());
       
        ppt.setString(6, excelDataVO.getDrawee_address());
        ppt.setString(7, excelDataVO.getDrawee_name());
        logger.info("excelDataVO.getDrawee_name()" + excelDataVO.getDrawee_name());
       
        ppt.setString(8, excelDataVO.getChargeAccount());
        ppt.setString(9, excelDataVO.getCollectionamount());
        ppt.setString(10, excelDataVO.getCollectioncurrency());
        ppt.setString(11, excelDataVO.getShipmenttocountry());
        ppt.setString(12, excelDataVO.getShipmentfromcountry());
        ppt.setString(13, excelDataVO.getHscode());
        ppt.setString(14, excelDataVO.getIncoterms());
        ppt.setString(15, excelDataVO.getGoodscode());
        ppt.setString(16, excelDataVO.getGooddescription());
        ppt.setString(17, excelDataVO.getPortofdestination());
        ppt.setString(18, excelDataVO.getPortofloading());
        ppt.setString(19, excelDataVO.getTransportdocno());
        ppt.setString(20, excelDataVO.getTransportdocdate());
        ppt.setString(21, excelDataVO.getInvoiceno());
        ppt.setString(22, excelDataVO.getInvoicedate());
        ppt.setString(23, excelDataVO.getForm_type());
        ppt.setString(24, excelDataVO.getShpbill_no());
        ppt.setString(25, excelDataVO.getBill_date());
        ppt.setString(26, excelDataVO.getPort_code());
        ppt.setString(27, excelDataVO.getForm_no());
        ppt.setString(28, excelDataVO.getShp_amt());
        ppt.setString(29, excelDataVO.getShp_currency());
        ppt.setString(30, excelDataVO.getRepamt_wrt_amt());
        ppt.setString(31, excelDataVO.getShortcollectionamount());
        ppt.setString(32, excelDataVO.getRemittance_num());
        ppt.setString(33, excelDataVO.getRecord_indicator());
        ppt.setString(34, excelDataVO.getLeo_date());
        ppt.setString(35, excelDataVO.getExport_agency());
        ppt.setString(36, excelDataVO.getExport_type());
        ppt.setString(37, excelDataVO.getDestination_countrry());
        ppt.setString(38, excelDataVO.getIecode());
        ppt.setString(39, excelDataVO.getAdcode());
        ppt.setString(40, excelDataVO.getCustoms_number());
        ppt.setString(41, excelDataVO.getShp_inv_sno());
        ppt.setString(42, excelDataVO.getShp_invoice_no());
        ppt.setString(43, excelDataVO.getShp_invoice_date());
        ppt.setString(44, excelDataVO.getFob_amt());
        ppt.setString(45, excelDataVO.getFreight_amt());
        ppt.setString(46, excelDataVO.getIns_amt());
        ppt.setString(47, excelDataVO.getComm_amt());
        ppt.setString(48, excelDataVO.getDiscount_amt());
        ppt.setString(49, excelDataVO.getDeduction_amt());
        ppt.setString(50, excelDataVO.getPackage_amt());
        ppt.setString(51, excelDataVO.getRemittance_adcode());
        ppt.setString(52, excelDataVO.getFirc_no());
        ppt.setString(53, excelDataVO.getRemitter_name());
        ppt.setString(54, excelDataVO.getRemitter_date());
        ppt.setString(55, excelDataVO.getRemitter_country());
        ppt.setString(56, excelDataVO.getUtilization_amount());
        ppt.setString(57, excelDataVO.getCif_number());
        ppt.setString(58, excelDataVO.getAvailable_amount());
        ppt.setString(59, excelDataVO.getStatus());
        ppt.setString(60, batchId);
        ppt.setString(61, excelDataVO.getR_balance());
        ppt.setString(62, excelDataVO.getU_name());
        ppt.setString(63, excelDataVO.getUpload_date());
        ppt.setString(64, excelDataVO.getReason_short_shp_amt());
        ppt.setString(65, excelDataVO.getShort_shp_amt());
        ppt.setString(66, CommonMethods.getCurrentHost());
       
        logger.info("-------------------->>> END");
        ppt.addBatch();
        logger.info("Insertion Time Ends------- >" + i + "<< " + new Timestamp(System.currentTimeMillis()));
      }
      ppt.executeBatch();
    }
    catch (Exception exception)
    {
      logger.info("Insertion Exception----->" + exception);
      logger.info("Insertion Exception--e--->" + exception);
      exception.printStackTrace();
    }
    finally
    {
      closeSqlRefferance(rs, ppt, con);
    }
    logger.info("Exiting Method");
    logger.error("KJ");
    return batchId;
  }
 
  private MakerHomeVO getExcelDataValue(MakerHomeVO makerVO)
  {
    logger.info("Entering Method");
    Connection con = null;
    ResultSet rs = null;
    LoggableStatement ps = null;
    ArrayList<ExcelDataVO> invoiceList = null;
    ExcelDataVO excelDataVO = null;
    try
    {
      String query = "select THEIRREF,COLLECTING_BANK,BEHALFOFBRANCH,DRAWER,DRAWEECUSTOMERID,DRAWEE_NAME,DRAWEE_ADDRESS,CHARGE_ACC,  COLLECTIONAMOUNT,COLLECTIONCURRENCY,SHIPMENTTOCOUNTRY,SHIPMENTFROMCOUNTRY,HSCODE,INCOTERMS,  GOODSCODE,GOODDESCRIPTION,PORTOFDESTINATION,PORTOFLOADING,TRANSPORTDOCNO,TRANSPORTDOCDATE,INVOICENO,INVOICEDATE,  FORM_TYPE,SHPBILL_NO,BILL_DATE,PORT_CODE,FORM_NO,SHP_AMT,SHP_CURRENCY,REPAMT_WRT_AMT,SHORTCOLLECTIONAMOUNT,  RECORD_INDICATOR,LEO_DATE,EXPORT_AGENCY,EXPORT_TYPE,DESTINATION_COUNTRRY,IECODE,ADCODE,CUSTOMS_NUMBER,SHP_INV_SNO,  SHP_INVOICE_NO,SHP_INVOICE_DATE,FOB_AMT,FREIGHT_AMT,INS_AMT,COMM_AMT,DISCOUNT_AMT,DEDUCTION_AMT,PACKAGE_AMT,REMITTANCE_ADCODE,  REMITTANCE_NUM,FIRC_NO,REMITTER_NAME,REMITTER_DATE,REMITTER_COUNTRY,UTILIZATION_AMOUNT,ERRORDESCRIPTION,AVAILABLE_AMOUNT,CIF_NUMBER,STATUS,SNUMBER,R_BALANCE,REASON_SHORT_SHP_AMT,SHORT_SHP_AMT from ETTDM_BULKUPLOAD_STAGING WHERE TRIM(BATCH_NO) = ? ORDER BY THEIRREF";
      logger.info("Select value grid" + query);
     

      invoiceList = new ArrayList();
      con = DBConnectionUtility.getConnection();
      ps = new LoggableStatement(con, query);
      ps.setString(1, CommonMethods.nullAndTrimString(makerVO.getBatchId()).trim());
      rs = ps.executeQuery();
      while (rs.next())
      {
        excelDataVO = new ExcelDataVO();
       
        excelDataVO.setTheirref(CommonMethods.nullAndTrimString(rs.getString("THEIRREF")));
        excelDataVO.setCollecting_bank(CommonMethods.nullAndTrimString(rs.getString("COLLECTING_BANK")));
        excelDataVO.setBehalfofbranch(CommonMethods.nullAndTrimString(rs.getString("BEHALFOFBRANCH")));
        excelDataVO.setDrawer(CommonMethods.nullAndTrimString(rs.getString("DRAWER")));
        excelDataVO.setDraweecustomerid(CommonMethods.nullAndTrimString(rs.getString("DRAWEECUSTOMERID")));
       
        excelDataVO.setDrawee_address(CommonMethods.nullAndTrimString(rs.getString("DRAWEE_ADDRESS")));
        excelDataVO.setDrawee_name(CommonMethods.nullAndTrimString(rs.getString("DRAWEE_NAME")));
        excelDataVO.setChargeAccount(CommonMethods.nullAndTrimString(rs.getString("CHARGE_ACC")));
       
        excelDataVO.setCollectionamount(CommonMethods.nullAndTrimString(rs.getString("COLLECTIONAMOUNT")));
        excelDataVO.setCollectioncurrency(CommonMethods.nullAndTrimString(rs.getString("COLLECTIONCURRENCY")));
       
        excelDataVO.setShipmenttocountry(CommonMethods.nullAndTrimString(rs.getString("SHIPMENTTOCOUNTRY")));
        excelDataVO
          .setShipmentfromcountry(CommonMethods.nullAndTrimString(rs.getString("SHIPMENTFROMCOUNTRY")));
        excelDataVO.setHscode(CommonMethods.nullAndTrimString(rs.getString("HSCODE")));
        excelDataVO.setIncoterms(CommonMethods.nullAndTrimString(rs.getString("INCOTERMS")));
        excelDataVO.setGoodscode(CommonMethods.nullAndTrimString(rs.getString("GOODSCODE")));
        excelDataVO.setGooddescription(CommonMethods.nullAndTrimString(rs.getString("GOODDESCRIPTION")));
        excelDataVO.setPortofdestination(CommonMethods.nullAndTrimString(rs.getString("PORTOFDESTINATION")));
        excelDataVO.setPortofloading(CommonMethods.nullAndTrimString(rs.getString("PORTOFLOADING")));
        excelDataVO.setTransportdocno(CommonMethods.nullAndTrimString(rs.getString("TRANSPORTDOCNO")));
        excelDataVO.setTransportdocdate(CommonMethods.nullAndTrimString(rs.getString("TRANSPORTDOCDATE")));
        excelDataVO.setInvoiceno(CommonMethods.nullAndTrimString(rs.getString("INVOICENO")));
        excelDataVO.setInvoicedate(CommonMethods.nullAndTrimString(rs.getString("INVOICEDATE")));
        excelDataVO.setForm_type(CommonMethods.nullAndTrimString(rs.getString("FORM_TYPE")));
        excelDataVO.setShpbill_no(CommonMethods.nullAndTrimString(rs.getString("SHPBILL_NO")));
        excelDataVO.setBill_date(CommonMethods.nullAndTrimString(rs.getString("BILL_DATE")));
        excelDataVO.setPort_code(CommonMethods.nullAndTrimString(rs.getString("PORT_CODE")));
        excelDataVO.setForm_no(CommonMethods.nullAndTrimString(rs.getString("FORM_NO")));
        excelDataVO.setShp_amt(CommonMethods.nullAndTrimString(rs.getString("SHP_AMT")));
        excelDataVO.setShp_currency(CommonMethods.nullAndTrimString(rs.getString("SHP_CURRENCY")));
        excelDataVO.setRepamt_wrt_amt(CommonMethods.nullAndTrimString(rs.getString("REPAMT_WRT_AMT")));
        excelDataVO.setShortcollectionamount(
          CommonMethods.nullAndTrimString(rs.getString("SHORTCOLLECTIONAMOUNT")));
        excelDataVO.setRecord_indicator(CommonMethods.nullAndTrimString(rs.getString("RECORD_INDICATOR")));
        excelDataVO.setLeo_date(CommonMethods.nullAndTrimString(rs.getString("LEO_DATE")));
        excelDataVO.setExport_agency(CommonMethods.nullAndTrimString(rs.getString("EXPORT_AGENCY")));
        excelDataVO.setExport_type(CommonMethods.nullAndTrimString(rs.getString("EXPORT_TYPE")));
        excelDataVO
          .setDestination_countrry(CommonMethods.nullAndTrimString(rs.getString("DESTINATION_COUNTRRY")));
        excelDataVO.setIecode(CommonMethods.nullAndTrimString(rs.getString("IECODE")));
        excelDataVO.setAdcode(CommonMethods.nullAndTrimString(rs.getString("ADCODE")));
        excelDataVO.setCustoms_number(CommonMethods.nullAndTrimString(rs.getString("CUSTOMS_NUMBER")));
        excelDataVO.setShp_inv_sno(CommonMethods.nullAndTrimString(rs.getString("SHP_INV_SNO")));
        excelDataVO.setShp_invoice_no(CommonMethods.nullAndTrimString(rs.getString("SHP_INVOICE_NO")));
        excelDataVO.setShp_invoice_date(CommonMethods.nullAndTrimString(rs.getString("SHP_INVOICE_DATE")));
        excelDataVO.setFob_amt(CommonMethods.nullAndTrimString(rs.getString("FOB_AMT")));
        excelDataVO.setFreight_amt(CommonMethods.nullAndTrimString(rs.getString("FREIGHT_AMT")));
        excelDataVO.setIns_amt(CommonMethods.nullAndTrimString(rs.getString("INS_AMT")));
        excelDataVO.setComm_amt(CommonMethods.nullAndTrimString(rs.getString("COMM_AMT")));
        excelDataVO.setDiscount_amt(CommonMethods.nullAndTrimString(rs.getString("DISCOUNT_AMT")));
        excelDataVO.setDeduction_amt(CommonMethods.nullAndTrimString(rs.getString("DEDUCTION_AMT")));
        excelDataVO.setPackage_amt(CommonMethods.nullAndTrimString(rs.getString("PACKAGE_AMT")));
        excelDataVO.setRemittance_adcode(CommonMethods.nullAndTrimString(rs.getString("REMITTANCE_ADCODE")));
        excelDataVO.setRemittance_num(CommonMethods.nullAndTrimString(rs.getString("REMITTANCE_NUM")));
        excelDataVO.setFirc_no(CommonMethods.nullAndTrimString(rs.getString("FIRC_NO")));
        excelDataVO.setRemitter_name(CommonMethods.nullAndTrimString(rs.getString("REMITTER_NAME")));
        excelDataVO.setRemitter_date(CommonMethods.nullAndTrimString(rs.getString("REMITTER_DATE")));
        excelDataVO.setRemitter_country(CommonMethods.nullAndTrimString(rs.getString("REMITTER_COUNTRY")));
        excelDataVO.setUtilization_amount(CommonMethods.nullAndTrimString(rs.getString("UTILIZATION_AMOUNT")));
        excelDataVO.setCif_number(CommonMethods.nullAndTrimString(rs.getString("CIF_NUMBER")));
        excelDataVO.setAvailable_amount(CommonMethods.nullAndTrimString(rs.getString("AVAILABLE_AMOUNT")));
       


        invoiceList.add(excelDataVO);
      }
      makerVO.setInvoiceList(invoiceList);
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, ps, rs);
    }
    logger.info("Exiting Method");
    return makerVO;
  }
 
  private MakerHomeVO getExcelDataValueBasedBatchId(Connection con, MakerHomeVO makerVO, String batchId)
  {
    logger.info("Get ALl the excel value based  getExcelDataValueBasedBatchId");
    logger.info("Entering Method");
    ResultSet rs = null;
    LoggableStatement ps = null;
    ArrayList<ExcelDataVO> invoiceList = null;
    ExcelDataVO excelDataVO = null;
    try
    {
      String query = "select THEIRREF,COLLECTING_BANK,BEHALFOFBRANCH,DRAWER,DRAWEECUSTOMERID,DRAWEE_NAME,DRAWEE_ADDRESS,CHARGE_ACC,  COLLECTIONAMOUNT,COLLECTIONCURRENCY,SHIPMENTTOCOUNTRY,SHIPMENTFROMCOUNTRY,HSCODE,INCOTERMS,  GOODSCODE,GOODDESCRIPTION,PORTOFDESTINATION,PORTOFLOADING,TRANSPORTDOCNO,TRANSPORTDOCDATE,INVOICENO,INVOICEDATE,  FORM_TYPE,SHPBILL_NO,BILL_DATE,PORT_CODE,FORM_NO,SHP_AMT,SHP_CURRENCY,REPAMT_WRT_AMT,SHORTCOLLECTIONAMOUNT,  RECORD_INDICATOR,LEO_DATE,EXPORT_AGENCY,EXPORT_TYPE,DESTINATION_COUNTRRY,IECODE,ADCODE,CUSTOMS_NUMBER,SHP_INV_SNO,  SHP_INVOICE_NO,SHP_INVOICE_DATE,FOB_AMT,FREIGHT_AMT,INS_AMT,COMM_AMT,DISCOUNT_AMT,DEDUCTION_AMT,PACKAGE_AMT,REMITTANCE_ADCODE,  REMITTANCE_NUM,FIRC_NO,REMITTER_NAME,REMITTER_DATE,REMITTER_COUNTRY,UTILIZATION_AMOUNT,ERRORDESCRIPTION,AVAILABLE_AMOUNT,CIF_NUMBER,STATUS,SNUMBER,R_BALANCE,REASON_SHORT_SHP_AMT,SHORT_SHP_AMT from ETTDM_BULKUPLOAD_STAGING WHERE TRIM(BATCH_NO) = ? ORDER BY THEIRREF";
      logger.info("Select value grid" + query);
     


      invoiceList = new ArrayList();
      ps = new LoggableStatement(con, query);
      ps.setString(1, batchId);
      rs = ps.executeQuery();
      while (rs.next())
      {
        excelDataVO = new ExcelDataVO();
       
        excelDataVO.setTheirref(CommonMethods.nullAndTrimString(rs.getString("THEIRREF")));
        excelDataVO.setCollecting_bank(CommonMethods.nullAndTrimString(rs.getString("COLLECTING_BANK")));
        excelDataVO.setBehalfofbranch(CommonMethods.nullAndTrimString(rs.getString("BEHALFOFBRANCH")));
        excelDataVO.setDrawer(CommonMethods.nullAndTrimString(rs.getString("DRAWER")));
        excelDataVO.setDraweecustomerid(CommonMethods.nullAndTrimString(rs.getString("DRAWEECUSTOMERID")));
       
        excelDataVO.setDrawee_address(CommonMethods.nullAndTrimString(rs.getString("DRAWEE_ADDRESS")));
        excelDataVO.setDrawee_name(CommonMethods.nullAndTrimString(rs.getString("DRAWEE_NAME")));
        excelDataVO.setChargeAccount(CommonMethods.nullAndTrimString(rs.getString("CHARGE_ACC")));
       
        excelDataVO.setCollectionamount(CommonMethods.nullAndTrimString(rs.getString("COLLECTIONAMOUNT")));
        excelDataVO.setCollectioncurrency(CommonMethods.nullAndTrimString(rs.getString("COLLECTIONCURRENCY")));
        excelDataVO.setShipmenttocountry(CommonMethods.nullAndTrimString(rs.getString("SHIPMENTTOCOUNTRY")));
        excelDataVO
          .setShipmentfromcountry(CommonMethods.nullAndTrimString(rs.getString("SHIPMENTFROMCOUNTRY")));
        excelDataVO.setHscode(CommonMethods.nullAndTrimString(rs.getString("HSCODE")));
        excelDataVO.setIncoterms(CommonMethods.nullAndTrimString(rs.getString("INCOTERMS")));
        excelDataVO.setGoodscode(CommonMethods.nullAndTrimString(rs.getString("GOODSCODE")));
        excelDataVO.setGooddescription(CommonMethods.nullAndTrimString(rs.getString("GOODDESCRIPTION")));
        excelDataVO.setPortofdestination(CommonMethods.nullAndTrimString(rs.getString("PORTOFDESTINATION")));
        excelDataVO.setPortofloading(CommonMethods.nullAndTrimString(rs.getString("PORTOFLOADING")));
        excelDataVO.setTransportdocno(CommonMethods.nullAndTrimString(rs.getString("TRANSPORTDOCNO")));
        excelDataVO.setTransportdocdate(CommonMethods.nullAndTrimString(rs.getString("TRANSPORTDOCDATE")));
        excelDataVO.setInvoiceno(CommonMethods.nullAndTrimString(rs.getString("INVOICENO")));
        excelDataVO.setInvoicedate(CommonMethods.nullAndTrimString(rs.getString("INVOICEDATE")));
        excelDataVO.setForm_type(CommonMethods.nullAndTrimString(rs.getString("FORM_TYPE")));
        excelDataVO.setShpbill_no(CommonMethods.nullAndTrimString(rs.getString("SHPBILL_NO")));
        excelDataVO.setBill_date(CommonMethods.nullAndTrimString(rs.getString("BILL_DATE")));
        excelDataVO.setPort_code(CommonMethods.nullAndTrimString(rs.getString("PORT_CODE")));
        excelDataVO.setForm_no(CommonMethods.nullAndTrimString(rs.getString("FORM_NO")));
        excelDataVO.setShp_amt(CommonMethods.nullAndTrimString(rs.getString("SHP_AMT")));
        excelDataVO.setShp_currency(CommonMethods.nullAndTrimString(rs.getString("SHP_CURRENCY")));
        excelDataVO.setRepamt_wrt_amt(CommonMethods.nullAndTrimString(rs.getString("REPAMT_WRT_AMT")));
        excelDataVO.setShortcollectionamount(
          CommonMethods.nullAndTrimString(rs.getString("SHORTCOLLECTIONAMOUNT")));
        excelDataVO.setRecord_indicator(CommonMethods.nullAndTrimString(rs.getString("RECORD_INDICATOR")));
        excelDataVO.setLeo_date(CommonMethods.nullAndTrimString(rs.getString("LEO_DATE")));
        excelDataVO.setExport_agency(CommonMethods.nullAndTrimString(rs.getString("EXPORT_AGENCY")));
        excelDataVO.setExport_type(CommonMethods.nullAndTrimString(rs.getString("EXPORT_TYPE")));
        excelDataVO
          .setDestination_countrry(CommonMethods.nullAndTrimString(rs.getString("DESTINATION_COUNTRRY")));
        excelDataVO.setIecode(CommonMethods.nullAndTrimString(rs.getString("IECODE")));
        excelDataVO.setAdcode(CommonMethods.nullAndTrimString(rs.getString("ADCODE")));
        excelDataVO.setCustoms_number(CommonMethods.nullAndTrimString(rs.getString("CUSTOMS_NUMBER")));
        excelDataVO.setShp_inv_sno(CommonMethods.nullAndTrimString(rs.getString("SHP_INV_SNO")));
        excelDataVO.setShp_invoice_no(CommonMethods.nullAndTrimString(rs.getString("SHP_INVOICE_NO")));
        excelDataVO.setShp_invoice_date(CommonMethods.nullAndTrimString(rs.getString("SHP_INVOICE_DATE")));
        excelDataVO.setFob_amt(CommonMethods.nullAndTrimString(rs.getString("FOB_AMT")));
        excelDataVO.setFreight_amt(CommonMethods.nullAndTrimString(rs.getString("FREIGHT_AMT")));
        excelDataVO.setIns_amt(CommonMethods.nullAndTrimString(rs.getString("INS_AMT")));
        excelDataVO.setComm_amt(CommonMethods.nullAndTrimString(rs.getString("COMM_AMT")));
        excelDataVO.setDiscount_amt(CommonMethods.nullAndTrimString(rs.getString("DISCOUNT_AMT")));
        excelDataVO.setDeduction_amt(CommonMethods.nullAndTrimString(rs.getString("DEDUCTION_AMT")));
        excelDataVO.setPackage_amt(CommonMethods.nullAndTrimString(rs.getString("PACKAGE_AMT")));
        excelDataVO.setRemittance_adcode(CommonMethods.nullAndTrimString(rs.getString("REMITTANCE_ADCODE")));
        excelDataVO.setRemittance_num(CommonMethods.nullAndTrimString(rs.getString("REMITTANCE_NUM")));
        excelDataVO.setFirc_no(CommonMethods.nullAndTrimString(rs.getString("FIRC_NO")));
        excelDataVO.setRemitter_name(CommonMethods.nullAndTrimString(rs.getString("REMITTER_NAME")));
        excelDataVO.setRemitter_date(CommonMethods.nullAndTrimString(rs.getString("REMITTER_DATE")));
        excelDataVO.setRemitter_country(CommonMethods.nullAndTrimString(rs.getString("REMITTER_COUNTRY")));
        excelDataVO.setUtilization_amount(CommonMethods.nullAndTrimString(rs.getString("UTILIZATION_AMOUNT")));
        excelDataVO.setErrordtls(CommonMethods.nullAndTrimString(rs.getString("ERRORDESCRIPTION")));
        excelDataVO.setStatus(CommonMethods.nullAndTrimString(rs.getString("STATUS")));
        invoiceList.add(excelDataVO);
      }
      makerVO.setInvoiceList(invoiceList);
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
      logger.info("Invoice Selection Exception" + exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(null, ps, rs);
    }
    logger.info("Exiting Method");
   
    return makerVO;
  }
 
  private ArrayList<ExcelDataVO> getInvoiceListBasedOnBatchId(String batchId, Connection con)
  {
    logger.info("Entering Method");
    ResultSet rs = null;
    LoggableStatement ps = null;
    ArrayList<ExcelDataVO> invoiceList = null;
    ExcelDataVO excelDataVO = null;
    try
    {
      String query = "select THEIRREF,COLLECTING_BANK,BEHALFOFBRANCH,DRAWER,DRAWEECUSTOMERID,DRAWEE_NAME,DRAWEE_ADDRESS,CHARGE_ACC,  COLLECTIONAMOUNT,COLLECTIONCURRENCY,SHIPMENTTOCOUNTRY,SHIPMENTFROMCOUNTRY,HSCODE,INCOTERMS,  GOODSCODE,GOODDESCRIPTION,PORTOFDESTINATION,PORTOFLOADING,TRANSPORTDOCNO,TRANSPORTDOCDATE,INVOICENO,INVOICEDATE,  FORM_TYPE,SHPBILL_NO,BILL_DATE,PORT_CODE,FORM_NO,SHP_AMT,SHP_CURRENCY,REPAMT_WRT_AMT,SHORTCOLLECTIONAMOUNT,  RECORD_INDICATOR,LEO_DATE,EXPORT_AGENCY,EXPORT_TYPE,DESTINATION_COUNTRRY,IECODE,ADCODE,CUSTOMS_NUMBER,SHP_INV_SNO,  SHP_INVOICE_NO,SHP_INVOICE_DATE,FOB_AMT,FREIGHT_AMT,INS_AMT,COMM_AMT,DISCOUNT_AMT,DEDUCTION_AMT,PACKAGE_AMT,REMITTANCE_ADCODE,  REMITTANCE_NUM,FIRC_NO,REMITTER_NAME,REMITTER_DATE,REMITTER_COUNTRY,UTILIZATION_AMOUNT,ERRORDESCRIPTION,AVAILABLE_AMOUNT,CIF_NUMBER,STATUS,SNUMBER,R_BALANCE,REASON_SHORT_SHP_AMT,SHORT_SHP_AMT from ETTDM_BULKUPLOAD_STAGING WHERE TRIM(BATCH_NO) = ? ORDER BY THEIRREF";
     


      invoiceList = new ArrayList();
      ps = new LoggableStatement(con, query);
      ps.setString(1, batchId);
      rs = ps.executeQuery();
      while (rs.next())
      {
        excelDataVO = new ExcelDataVO();
       
        excelDataVO.setTheirref(CommonMethods.nullAndTrimString(rs.getString("THEIRREF")));
        excelDataVO.setCollecting_bank(CommonMethods.nullAndTrimString(rs.getString("COLLECTING_BANK")));
        excelDataVO.setBehalfofbranch(CommonMethods.nullAndTrimString(rs.getString("BEHALFOFBRANCH")));
        excelDataVO.setDrawer(CommonMethods.nullAndTrimString(rs.getString("DRAWER")));
        excelDataVO.setDraweecustomerid(CommonMethods.nullAndTrimString(rs.getString("DRAWEECUSTOMERID")));
       
        logger.info("Draweee Customer ID----------------------------------->" + CommonMethods.nullAndTrimString(rs.getString("DRAWEECUSTOMERID")));
       
        excelDataVO.setDrawee_address(CommonMethods.nullAndTrimString(rs.getString("DRAWEE_ADDRESS")));
        excelDataVO.setDrawee_name(CommonMethods.nullAndTrimString(rs.getString("DRAWEE_NAME")));
       
        excelDataVO.setChargeAccount(CommonMethods.nullAndTrimString(rs.getString("CHARGE_ACC")));
       
        excelDataVO.setCollectionamount(CommonMethods.nullAndTrimString(rs.getString("COLLECTIONAMOUNT")));
        excelDataVO.setCollectioncurrency(CommonMethods.nullAndTrimString(rs.getString("COLLECTIONCURRENCY")));
       
        excelDataVO.setShipmenttocountry(CommonMethods.nullAndTrimString(rs.getString("SHIPMENTTOCOUNTRY")));
        excelDataVO
          .setShipmentfromcountry(CommonMethods.nullAndTrimString(rs.getString("SHIPMENTFROMCOUNTRY")));
        excelDataVO.setHscode(CommonMethods.nullAndTrimString(rs.getString("HSCODE")));
        excelDataVO.setIncoterms(CommonMethods.nullAndTrimString(rs.getString("INCOTERMS")));
        excelDataVO.setGoodscode(CommonMethods.nullAndTrimString(rs.getString("GOODSCODE")));
        excelDataVO.setGooddescription(CommonMethods.nullAndTrimString(rs.getString("GOODDESCRIPTION")));
        excelDataVO.setPortofdestination(CommonMethods.nullAndTrimString(rs.getString("PORTOFDESTINATION")));
        excelDataVO.setPortofloading(CommonMethods.nullAndTrimString(rs.getString("PORTOFLOADING")));
        excelDataVO.setTransportdocno(CommonMethods.nullAndTrimString(rs.getString("TRANSPORTDOCNO")));
        excelDataVO.setTransportdocdate(CommonMethods.nullAndTrimString(rs.getString("TRANSPORTDOCDATE")));
        excelDataVO.setInvoiceno(CommonMethods.nullAndTrimString(rs.getString("INVOICENO")));
        excelDataVO.setInvoicedate(CommonMethods.nullAndTrimString(rs.getString("INVOICEDATE")));
        excelDataVO.setForm_type(CommonMethods.nullAndTrimString(rs.getString("FORM_TYPE")));
        excelDataVO.setShpbill_no(CommonMethods.nullAndTrimString(rs.getString("SHPBILL_NO")));
        excelDataVO.setBill_date(CommonMethods.nullAndTrimString(rs.getString("BILL_DATE")));
        excelDataVO.setPort_code(CommonMethods.nullAndTrimString(rs.getString("PORT_CODE")));
        excelDataVO.setForm_no(CommonMethods.nullAndTrimString(rs.getString("FORM_NO")));
        excelDataVO.setShp_amt(CommonMethods.nullAndTrimString(rs.getString("SHP_AMT")));
        excelDataVO.setShp_currency(CommonMethods.nullAndTrimString(rs.getString("SHP_CURRENCY")));
       
        excelDataVO
          .setReason_short_shp_amt(CommonMethods.nullAndTrimString(rs.getString("REASON_SHORT_SHP_AMT")));
        excelDataVO.setShort_shp_amt(CommonMethods.nullAndTrimString(rs.getString("SHORT_SHP_AMT")));
       
        excelDataVO.setRepamt_wrt_amt(CommonMethods.nullAndTrimString(rs.getString("REPAMT_WRT_AMT")));
        excelDataVO.setShortcollectionamount(
          CommonMethods.nullAndTrimString(rs.getString("SHORTCOLLECTIONAMOUNT")));
        excelDataVO.setRecord_indicator(CommonMethods.nullAndTrimString(rs.getString("RECORD_INDICATOR")));
        excelDataVO.setLeo_date(CommonMethods.nullAndTrimString(rs.getString("LEO_DATE")));
        excelDataVO.setExport_agency(CommonMethods.nullAndTrimString(rs.getString("EXPORT_AGENCY")));
        excelDataVO.setExport_type(CommonMethods.nullAndTrimString(rs.getString("EXPORT_TYPE")));
        excelDataVO
          .setDestination_countrry(CommonMethods.nullAndTrimString(rs.getString("DESTINATION_COUNTRRY")));
        excelDataVO.setIecode(CommonMethods.nullAndTrimString(rs.getString("IECODE")));
        excelDataVO.setAdcode(CommonMethods.nullAndTrimString(rs.getString("ADCODE")));
        excelDataVO.setCustoms_number(CommonMethods.nullAndTrimString(rs.getString("CUSTOMS_NUMBER")));
        excelDataVO.setShp_inv_sno(CommonMethods.nullAndTrimString(rs.getString("SHP_INV_SNO")));
        excelDataVO.setShp_invoice_no(CommonMethods.nullAndTrimString(rs.getString("SHP_INVOICE_NO")));
        excelDataVO.setShp_invoice_date(CommonMethods.nullAndTrimString(rs.getString("SHP_INVOICE_DATE")));
        excelDataVO.setFob_amt(CommonMethods.nullAndTrimString(rs.getString("FOB_AMT")));
        excelDataVO.setFreight_amt(CommonMethods.nullAndTrimString(rs.getString("FREIGHT_AMT")));
        excelDataVO.setIns_amt(CommonMethods.nullAndTrimString(rs.getString("INS_AMT")));
        excelDataVO.setComm_amt(CommonMethods.nullAndTrimString(rs.getString("COMM_AMT")));
        excelDataVO.setDiscount_amt(CommonMethods.nullAndTrimString(rs.getString("DISCOUNT_AMT")));
        excelDataVO.setDeduction_amt(CommonMethods.nullAndTrimString(rs.getString("DEDUCTION_AMT")));
        excelDataVO.setPackage_amt(CommonMethods.nullAndTrimString(rs.getString("PACKAGE_AMT")));
        excelDataVO.setRemittance_adcode(CommonMethods.nullAndTrimString(rs.getString("REMITTANCE_ADCODE")));
        excelDataVO.setRemittance_num(CommonMethods.nullAndTrimString(rs.getString("REMITTANCE_NUM")));
        excelDataVO.setFirc_no(CommonMethods.nullAndTrimString(rs.getString("FIRC_NO")));
        excelDataVO.setRemitter_name(CommonMethods.nullAndTrimString(rs.getString("REMITTER_NAME")));
        excelDataVO.setRemitter_date(CommonMethods.nullAndTrimString(rs.getString("REMITTER_DATE")));
        excelDataVO.setRemitter_country(CommonMethods.nullAndTrimString(rs.getString("REMITTER_COUNTRY")));
        excelDataVO.setUtilization_amount(CommonMethods.nullAndTrimString(rs.getString("UTILIZATION_AMOUNT")));
        excelDataVO.setErrordtls(CommonMethods.nullAndTrimString(rs.getString("ERRORDESCRIPTION")));
        excelDataVO.setCif_number(CommonMethods.nullAndTrimString(rs.getString("CIF_NUMBER")));
        excelDataVO.setAvailable_amount(CommonMethods.nullAndTrimString(rs.getString("AVAILABLE_AMOUNT")));
        excelDataVO.setStatus(CommonMethods.nullAndTrimString(rs.getString("STATUS")));
        excelDataVO.setSnumber(CommonMethods.nullAndTrimString(rs.getString("SNUMBER")));
        excelDataVO.setR_balance(CommonMethods.nullAndTrimString(rs.getString("R_BALANCE")));
        invoiceList.add(excelDataVO);
      }
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
      logger.info("Select Invoice Grid Exception" + exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(null, ps, rs);
    }
    logger.info("Exiting Method");
    return invoiceList;
  }
 
  public String getcurrentstatus(String batchid, Connection con)
  {
    String Status = "Failed";
    ResultSet rs = null;
    LoggableStatement ps = null;
    ResultSet rs1 = null;
    LoggableStatement ps1 = null;
   


    String query = "select count(*) as COUNT from ETTDM_BULKUPLOAD_STAGING  WHERE TRIM(BATCH_NO) = '" + batchid + "' AND STATUS='SUCCESS'";
    int i = 0;
    try
    {
      logger.info("getcurrentstatus====");
      ps = new LoggableStatement(con, query);
      rs = ps.executeQuery();
      while (rs.next()) {
        i = rs.getInt("COUNT");
      }
      logger.info("Total Success count--->" + i);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    logger.info("Success COUNT====" + i);
   
    int j = 0;
    String togetTotalCount = "select count(*) as COUNT from ETTDM_BULKUPLOAD_STAGING  WHERE TRIM(BATCH_NO) = '" + batchid + "' ";
    try
    {
      ps1 = new LoggableStatement(con, togetTotalCount);
      rs1 = ps1.executeQuery();
      while (rs1.next()) {
        j = rs1.getInt("COUNT");
      }
      logger.info("Total No of count--->" + j);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    if (j == i)
    {
      Status = "Success";
      logger.info("Counts got matched----->>>---SUCCESS");
    }
    else
    {
      logger.info("Counts not got matched----->>>---FAILED");
    }
    logger.info("Status====" + Status);
   
    return Status;
  }
 
  public MakerHomeVO fetchInvoiceDetails(MakerHomeVO makervo)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    ResultSet rs = null;
    LoggableStatement ps = null;
    ArrayList<Object> transactionList = null;
    ExcelDataVO excelDataVO = null;
    try
    {
      transactionList = new ArrayList();
     
      String query = "select * from ETTDM_BULKUPLOAD_STAGING where BATCH_NO = '" + makervo.getBatchId().trim() +
        "' AND STATUS IN('SUCCESS','FAILED')";
     
      logger.info("Excel  Value--->" + query);
      logger.info("Download Excel Selection Query" + query);
      con = DBConnectionUtility.getConnection();
      ps = new LoggableStatement(con, query);
      rs = ps.executeQuery();
      while (rs.next())
      {
        excelDataVO = new ExcelDataVO();
       
        excelDataVO.setTheirref(CommonMethods.nullAndTrimString(rs.getString("THEIRREF")));
        excelDataVO.setCollecting_bank(CommonMethods.nullAndTrimString(rs.getString("COLLECTING_BANK")));
        excelDataVO.setBehalfofbranch(CommonMethods.nullAndTrimString(rs.getString("BEHALFOFBRANCH")));
        excelDataVO.setDrawer(CommonMethods.nullAndTrimString(rs.getString("DRAWER")));
        excelDataVO.setDraweecustomerid(CommonMethods.nullAndTrimString(rs.getString("DRAWEECUSTOMERID")));
       
        excelDataVO.setDrawee_name(CommonMethods.nullAndTrimString(rs.getString("DRAWEE_NAME")));
        excelDataVO.setDrawee_address(CommonMethods.nullAndTrimString(rs.getString("DRAWEE_ADDRESS")));
       
        excelDataVO.setCollectionamount(CommonMethods.nullAndTrimString(rs.getString("COLLECTIONAMOUNT")));
        excelDataVO.setCollectioncurrency(CommonMethods.nullAndTrimString(rs.getString("COLLECTIONCURRENCY")));
        excelDataVO.setShipmenttocountry(CommonMethods.nullAndTrimString(rs.getString("SHIPMENTTOCOUNTRY")));
        excelDataVO
          .setShipmentfromcountry(CommonMethods.nullAndTrimString(rs.getString("SHIPMENTFROMCOUNTRY")));
        excelDataVO.setHscode(CommonMethods.nullAndTrimString(rs.getString("HSCODE")));
        excelDataVO.setIncoterms(CommonMethods.nullAndTrimString(rs.getString("INCOTERMS")));
        excelDataVO.setGoodscode(CommonMethods.nullAndTrimString(rs.getString("GOODSCODE")));
        excelDataVO.setGooddescription(CommonMethods.nullAndTrimString(rs.getString("GOODDESCRIPTION")));
        excelDataVO.setPortofdestination(CommonMethods.nullAndTrimString(rs.getString("PORTOFDESTINATION")));
        excelDataVO.setPortofloading(CommonMethods.nullAndTrimString(rs.getString("PORTOFLOADING")));
        excelDataVO.setTransportdocno(CommonMethods.nullAndTrimString(rs.getString("TRANSPORTDOCNO")));
        excelDataVO.setTransportdocdate(CommonMethods.nullAndTrimString(rs.getString("TRANSPORTDOCDATE")));
        excelDataVO.setInvoiceno(CommonMethods.nullAndTrimString(rs.getString("INVOICENO")));
        excelDataVO.setInvoicedate(CommonMethods.nullAndTrimString(rs.getString("INVOICEDATE")));
        excelDataVO.setForm_type(CommonMethods.nullAndTrimString(rs.getString("FORM_TYPE")));
        excelDataVO.setShpbill_no(CommonMethods.nullAndTrimString(rs.getString("SHPBILL_NO")));
        excelDataVO.setBill_date(CommonMethods.nullAndTrimString(rs.getString("BILL_DATE")));
        excelDataVO.setPort_code(CommonMethods.nullAndTrimString(rs.getString("PORT_CODE")));
        excelDataVO.setForm_no(CommonMethods.nullAndTrimString(rs.getString("FORM_NO")));
        excelDataVO.setShp_amt(CommonMethods.nullAndTrimString(rs.getString("SHP_AMT")));
        excelDataVO.setShp_currency(CommonMethods.nullAndTrimString(rs.getString("SHP_CURRENCY")));
       
        excelDataVO
          .setReason_short_shp_amt(CommonMethods.nullAndTrimString(rs.getString("REASON_SHORT_SHP_AMT")));
        excelDataVO.setShort_shp_amt(CommonMethods.nullAndTrimString(rs.getString("SHORT_SHP_AMT")));
       
        excelDataVO.setRepamt_wrt_amt(CommonMethods.nullAndTrimString(rs.getString("REPAMT_WRT_AMT")));
        excelDataVO.setShortcollectionamount(
          CommonMethods.nullAndTrimString(rs.getString("SHORTCOLLECTIONAMOUNT")));
        excelDataVO.setRecord_indicator(CommonMethods.nullAndTrimString(rs.getString("RECORD_INDICATOR")));
        excelDataVO.setLeo_date(CommonMethods.nullAndTrimString(rs.getString("LEO_DATE")));
        excelDataVO.setExport_agency(CommonMethods.nullAndTrimString(rs.getString("EXPORT_AGENCY")));
        excelDataVO.setExport_type(CommonMethods.nullAndTrimString(rs.getString("EXPORT_TYPE")));
        excelDataVO
          .setDestination_countrry(CommonMethods.nullAndTrimString(rs.getString("DESTINATION_COUNTRRY")));
        excelDataVO.setIecode(CommonMethods.nullAndTrimString(rs.getString("IECODE")));
        excelDataVO.setAdcode(CommonMethods.nullAndTrimString(rs.getString("ADCODE")));
        excelDataVO.setCustoms_number(CommonMethods.nullAndTrimString(rs.getString("CUSTOMS_NUMBER")));
        excelDataVO.setShp_inv_sno(CommonMethods.nullAndTrimString(rs.getString("SHP_INV_SNO")));
        excelDataVO.setShp_invoice_no(CommonMethods.nullAndTrimString(rs.getString("SHP_INVOICE_NO")));
        excelDataVO.setShp_invoice_date(CommonMethods.nullAndTrimString(rs.getString("SHP_INVOICE_DATE")));
        excelDataVO.setFob_amt(CommonMethods.nullAndTrimString(rs.getString("FOB_AMT")));
        excelDataVO.setFreight_amt(CommonMethods.nullAndTrimString(rs.getString("FREIGHT_AMT")));
        excelDataVO.setIns_amt(CommonMethods.nullAndTrimString(rs.getString("INS_AMT")));
        excelDataVO.setComm_amt(CommonMethods.nullAndTrimString(rs.getString("COMM_AMT")));
        excelDataVO.setDiscount_amt(CommonMethods.nullAndTrimString(rs.getString("DISCOUNT_AMT")));
        excelDataVO.setDeduction_amt(CommonMethods.nullAndTrimString(rs.getString("DEDUCTION_AMT")));
        excelDataVO.setPackage_amt(CommonMethods.nullAndTrimString(rs.getString("PACKAGE_AMT")));
        excelDataVO.setRemittance_adcode(CommonMethods.nullAndTrimString(rs.getString("REMITTANCE_ADCODE")));
        excelDataVO.setRemittance_num(CommonMethods.nullAndTrimString(rs.getString("REMITTANCE_NUM")));
        excelDataVO.setFirc_no(CommonMethods.nullAndTrimString(rs.getString("FIRC_NO")));
        excelDataVO.setRemitter_name(CommonMethods.nullAndTrimString(rs.getString("REMITTER_NAME")));
        excelDataVO.setRemitter_date(CommonMethods.nullAndTrimString(rs.getString("REMITTER_DATE")));
        excelDataVO.setRemitter_country(CommonMethods.nullAndTrimString(rs.getString("REMITTER_COUNTRY")));
        excelDataVO.setUtilization_amount(CommonMethods.nullAndTrimString(rs.getString("UTILIZATION_AMOUNT")));
        excelDataVO.setCif_number(CommonMethods.nullAndTrimString(rs.getString("CIF_NUMBER")));
        excelDataVO.setAvailable_amount(CommonMethods.nullAndTrimString(rs.getString("AVAILABLE_AMOUNT")));
        excelDataVO.setTi_error(CommonMethods.nullAndTrimString(rs.getString("TI_ERROR")));
        excelDataVO.setTi_status(CommonMethods.nullAndTrimString(rs.getString("TI_STATUS")));
        excelDataVO.setErrordtls(CommonMethods.nullAndTrimString(rs.getString("ERRORDESCRIPTION")));
        excelDataVO.setStatus(CommonMethods.nullAndTrimString(rs.getString("STATUS")));
       
        transactionList.add(excelDataVO);
      }
      makervo.setTransactionList(transactionList);
    }
    catch (Exception exception)
    {
      throwDAOException(exception);
      logger.info("Download Exception----:" + exception);
      logger.info("Download Exception----:" + exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, ps, rs);
    }
    logger.info("Exiting Method");
    return makervo;
  }
 
  public MakerHomeVO fetchInvoiceDetails1(MakerHomeVO makervo)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    ResultSet rs = null;
    LoggableStatement ps = null;
    ArrayList<Object> transactionList = null;
    ExcelDataVO excelDataVO = null;
    try
    {
      String bat = makervo.getBatchId().trim();
      transactionList = new ArrayList();
     

      String updated = " SELECT MAS.MASTER_REF AS TI_REFRENCE, TI_ERROR, TI_STATUS, BS.*,THEIRREF FROM ETTDM_BULKUPLOAD_STAGING BS,MASTER MAS WHERE BS.STATUS = 'SUCCESS' AND BS.TI_STATUS    = 'SUCCEEDED' AND BS.BATCH_NO       = '" +
        bat + "'  AND trim(BS.THEIRREF) = trim(MAS.PRI_REF) ";
     







      logger.info("Excel  Value for --->" + updated);
      con = DBConnectionUtility.getConnection();
      ps = new LoggableStatement(con, updated);
      rs = ps.executeQuery();
      while (rs.next())
      {
        excelDataVO = new ExcelDataVO();
        excelDataVO.setTi_reference(CommonMethods.nullAndTrimString(rs.getString("TI_REFRENCE")));
        excelDataVO.setCollecting_bank(CommonMethods.nullAndTrimString(rs.getString("COLLECTING_BANK")));
        excelDataVO.setTi_status(CommonMethods.nullAndTrimString(rs.getString("TI_STATUS")));
        excelDataVO.setTheirref(CommonMethods.nullAndTrimString(rs.getString("THEIRREF")));
        excelDataVO.setBehalfofbranch(CommonMethods.nullAndTrimString(rs.getString("BEHALFOFBRANCH")));
        excelDataVO.setDrawer(CommonMethods.nullAndTrimString(rs.getString("DRAWER")));
        excelDataVO.setDraweecustomerid(CommonMethods.nullAndTrimString(rs.getString("DRAWEECUSTOMERID")));
       
        excelDataVO.setDrawee_address(CommonMethods.nullAndTrimString(rs.getString("DRAWEE_NAME")));
        excelDataVO.setDrawee_name(CommonMethods.nullAndTrimString(rs.getString("DRAWEE_ADDRESS")));
       
        excelDataVO.setCollectionamount(CommonMethods.nullAndTrimString(rs.getString("COLLECTIONAMOUNT")));
        excelDataVO.setCollectioncurrency(CommonMethods.nullAndTrimString(rs.getString("COLLECTIONCURRENCY")));
       
        excelDataVO.setShipmenttocountry(CommonMethods.nullAndTrimString(rs.getString("SHIPMENTTOCOUNTRY")));
        excelDataVO
          .setShipmentfromcountry(CommonMethods.nullAndTrimString(rs.getString("SHIPMENTFROMCOUNTRY")));
        excelDataVO.setHscode(CommonMethods.nullAndTrimString(rs.getString("HSCODE")));
        excelDataVO.setIncoterms(CommonMethods.nullAndTrimString(rs.getString("INCOTERMS")));
        excelDataVO.setGoodscode(CommonMethods.nullAndTrimString(rs.getString("GOODSCODE")));
        excelDataVO.setGooddescription(CommonMethods.nullAndTrimString(rs.getString("GOODDESCRIPTION")));
        excelDataVO.setPortofdestination(CommonMethods.nullAndTrimString(rs.getString("PORTOFDESTINATION")));
        excelDataVO.setPortofloading(CommonMethods.nullAndTrimString(rs.getString("PORTOFLOADING")));
        excelDataVO.setTransportdocno(CommonMethods.nullAndTrimString(rs.getString("TRANSPORTDOCNO")));
        excelDataVO.setTransportdocdate(CommonMethods.nullAndTrimString(rs.getString("TRANSPORTDOCDATE")));
        excelDataVO.setInvoiceno(CommonMethods.nullAndTrimString(rs.getString("INVOICENO")));
        excelDataVO.setInvoicedate(CommonMethods.nullAndTrimString(rs.getString("INVOICEDATE")));
        excelDataVO.setForm_type(CommonMethods.nullAndTrimString(rs.getString("FORM_TYPE")));
        excelDataVO.setShpbill_no(CommonMethods.nullAndTrimString(rs.getString("SHPBILL_NO")));
        excelDataVO.setBill_date(CommonMethods.nullAndTrimString(rs.getString("BILL_DATE")));
        excelDataVO.setPort_code(CommonMethods.nullAndTrimString(rs.getString("PORT_CODE")));
        excelDataVO.setForm_no(CommonMethods.nullAndTrimString(rs.getString("FORM_NO")));
        excelDataVO.setShp_amt(CommonMethods.nullAndTrimString(rs.getString("SHP_AMT")));
        excelDataVO.setShp_currency(CommonMethods.nullAndTrimString(rs.getString("SHP_CURRENCY")));
        excelDataVO
          .setReason_short_shp_amt(CommonMethods.nullAndTrimString(rs.getString("REASON_SHORT_SHP_AMT")));
        excelDataVO.setShort_shp_amt(CommonMethods.nullAndTrimString(rs.getString("SHORT_SHP_AMT")));
        excelDataVO.setRepamt_wrt_amt(CommonMethods.nullAndTrimString(rs.getString("REPAMT_WRT_AMT")));
        excelDataVO.setShortcollectionamount(
          CommonMethods.nullAndTrimString(rs.getString("SHORTCOLLECTIONAMOUNT")));
        excelDataVO.setRecord_indicator(CommonMethods.nullAndTrimString(rs.getString("RECORD_INDICATOR")));
        excelDataVO.setLeo_date(CommonMethods.nullAndTrimString(rs.getString("LEO_DATE")));
        excelDataVO.setExport_agency(CommonMethods.nullAndTrimString(rs.getString("EXPORT_AGENCY")));
        excelDataVO.setExport_type(CommonMethods.nullAndTrimString(rs.getString("EXPORT_TYPE")));
        excelDataVO
          .setDestination_countrry(CommonMethods.nullAndTrimString(rs.getString("DESTINATION_COUNTRRY")));
        excelDataVO.setIecode(CommonMethods.nullAndTrimString(rs.getString("IECODE")));
        excelDataVO.setAdcode(CommonMethods.nullAndTrimString(rs.getString("ADCODE")));
        excelDataVO.setCustoms_number(CommonMethods.nullAndTrimString(rs.getString("CUSTOMS_NUMBER")));
        excelDataVO.setShp_inv_sno(CommonMethods.nullAndTrimString(rs.getString("SHP_INV_SNO")));
        excelDataVO.setShp_invoice_no(CommonMethods.nullAndTrimString(rs.getString("SHP_INVOICE_NO")));
        excelDataVO.setShp_invoice_date(CommonMethods.nullAndTrimString(rs.getString("SHP_INVOICE_DATE")));
        excelDataVO.setFob_amt(CommonMethods.nullAndTrimString(rs.getString("FOB_AMT")));
        excelDataVO.setFreight_amt(CommonMethods.nullAndTrimString(rs.getString("FREIGHT_AMT")));
        excelDataVO.setIns_amt(CommonMethods.nullAndTrimString(rs.getString("INS_AMT")));
        excelDataVO.setComm_amt(CommonMethods.nullAndTrimString(rs.getString("COMM_AMT")));
        excelDataVO.setDiscount_amt(CommonMethods.nullAndTrimString(rs.getString("DISCOUNT_AMT")));
        excelDataVO.setDeduction_amt(CommonMethods.nullAndTrimString(rs.getString("DEDUCTION_AMT")));
        excelDataVO.setPackage_amt(CommonMethods.nullAndTrimString(rs.getString("PACKAGE_AMT")));
        excelDataVO.setRemittance_adcode(CommonMethods.nullAndTrimString(rs.getString("REMITTANCE_ADCODE")));
        excelDataVO.setRemittance_num(CommonMethods.nullAndTrimString(rs.getString("REMITTANCE_NUM")));
        excelDataVO.setFirc_no(CommonMethods.nullAndTrimString(rs.getString("FIRC_NO")));
        excelDataVO.setRemitter_name(CommonMethods.nullAndTrimString(rs.getString("REMITTER_NAME")));
        excelDataVO.setRemitter_date(CommonMethods.nullAndTrimString(rs.getString("REMITTER_DATE")));
        excelDataVO.setRemitter_country(CommonMethods.nullAndTrimString(rs.getString("REMITTER_COUNTRY")));
        excelDataVO.setUtilization_amount(CommonMethods.nullAndTrimString(rs.getString("UTILIZATION_AMOUNT")));
        excelDataVO.setCif_number(CommonMethods.nullAndTrimString(rs.getString("CIF_NUMBER")));
        excelDataVO.setAvailable_amount(CommonMethods.nullAndTrimString(rs.getString("AVAILABLE_AMOUNT")));
       
        transactionList.add(excelDataVO);
      }
      makervo.setTransactionList(transactionList);
    }
    catch (Exception exception)
    {
      throwDAOException(exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, ps, rs);
    }
    logger.info("Exiting Method");
    return makervo;
  }
 
  public boolean getUploadStatus(String batchId)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    ResultSet rs = null;
    LoggableStatement ps = null;
    boolean uploadStatus = false;
    try
    {
      String queys = "SELECT COUNT(*) FROM ETTDM_BULKUPLOAD_STAGING WHERE TRIM(BATCH_NO)='" +
        batchId + "' AND UPLOAD_STATUS='Y'";
      con = DBConnectionUtility.getConnection();
      ps = new LoggableStatement(con, queys);
      rs = ps.executeQuery();
      while (rs.next()) {
        if (rs.getInt(1) > 0) {
          uploadStatus = true;
        }
      }
    }
    catch (Exception exception)
    {
      throwDAOException(exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, ps, rs);
    }
    logger.info("Exiting Method");
    return uploadStatus;
  }
 
  public void setUploadStatus(String batchId)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement ps = null;
    try
    {
      String queys = "UPDATE ETTDM_BULKUPLOAD_STAGING SET UPLOAD_STATUS='Y' WHERE TRIM(BATCH_NO)='" +
        batchId + "'";
      con = DBConnectionUtility.getConnection();
      ps = new LoggableStatement(con, queys);
      ps.executeUpdate();
    }
    catch (Exception exception)
    {
      throwDAOException(exception);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, ps, null);
    }
    logger.info("Exiting Method");
  }
 
  public int makerUpload(MakerHomeVO makervo)
    throws DAOException
  {
    logger.info("Entering Method");
    Connection con = null;
    LoggableStatement ps = null;
    LoggableStatement ps1 = null;
    LoggableStatement ps2 = null;
    LoggableStatement ps3 = null;
    LoggableStatement ps4 = null;
    LoggableStatement ps5 = null;
    LoggableStatement ps6 = null;
   
    LoggableStatement charge = null;
   
    LoggableStatement lob = null;
   
    ResultSet rs = null;
    ResultSet rset = null;
    ResultSet rses = null;
    ResultSet rsess = null;
    ResultSet rsesses = null;
    ResultSet rsessess = null;
    ResultSet rsessesss = null;
    ResultSet charge_1 = null;
    ResultSet lob1 = null;
   
    CommonMethods commonMethods = null;
    ArrayList<EQ3Posting> list = new ArrayList();
    EQ3Posting eq3Posting = new EQ3Posting();
    String result = null;
    String payxml = null;String theirref = null;
    String ref = "";
    String sThRef = " ";
    String sshipbillno = " ";
    String sshipbilldate = " ";
    String pportcode = " ";
    String remno = " ";
    String formnumber = " ";
    String draweecusid = " ";
    String refer = " ";
    String remitance = " ";
    String Msg = " ";
    int count = 0;int rowCount = 0;
    try
    {
      String queys = "SELECT DISTINCT THEIRREF  FROM ETTDM_BULKUPLOAD_STAGING  WHERE STATUS='SUCCESS' and  TI_STATUS IS NULL AND BATCH_NO= '" +
        makervo.getBatchId() + "' ORDER BY THEIRREF ASC";
      con = DBConnectionUtility.getConnection();
      ps1 = new LoggableStatement(con, queys);
      rset = ps1.executeQuery();
      while (rset.next())
      {
        sThRef = rset.getString("THEIRREF");
       
        String querys = " SELECT NVL(THEIRREF,'')THEIRREF,NVL(COLLECTING_BANK,'')COLLECTING_BANK,NVL(BEHALFOFBRANCH,'')BEHALFOFBRANCH,NVL(DRAWER,'')DRAWER,NVL(DRAWEE_NAME,'')DRAWEE_NAME,NVL(DRAWEE_ADDRESS,'')DRAWEE_ADDRESS,NVL(DRAWEECUSTOMERID,'')DRAWEECUSTOMERID, NVL(CHARGE_ACC,'')CHARGE_ACC,NVL(COLLECTIONAMOUNT,'')COLLECTIONAMOUNT,NVL(REASON_SHORT_SHP_AMT,''),REASON_SHORT_SHP_AMT,NVL(SHORT_SHP_AMT,'')SHORT_SHP_AMT,NVL(COLLECTIONCURRENCY,'')COLLECTIONCURRENCY,NVL(SHIPMENTTOCOUNTRY,'')SHIPMENTTOCOUNTRY, NVL(SHIPMENTFROMCOUNTRY,'')SHIPMENTFROMCOUNTRY,NVL(HSCODE,'')HSCODE,NVL(INCOTERMS,'')INCOTERMS,NVL(GOODSCODE,'')GOODSCODE,NVL(GOODDESCRIPTION,'')GOODDESCRIPTION,  NVL(PORTOFDESTINATION,'')PORTOFDESTINATION,NVL(PORTOFLOADING,'')PORTOFLOADING,NVL(TRANSPORTDOCNO,'')TRANSPORTDOCNO,NVL(TRANSPORTDOCDATE,'')TRANSPORTDOCDATE,NVL(INVOICENO,'')INVOICENO,TO_CHAR(TO_DATE(INVOICEDATE,'DD-MM-YYYY'),'YYYY-MM-DD')INVOICEDATE, NVL(FORM_TYPE,'')FORM_TYPE,NVL(SHPBILL_NO,'')SHPBILL_NO,TO_CHAR(TO_DATE(BILL_DATE,'DD-MM-YYYY'), 'YYYY-MM-DD') BILL_DATE,NVL(PORT_CODE,'')PORT_CODE,NVL(FORM_NO,'')FORM_NO,NVL(SHP_AMT,'')SHP_AMT,NVL(SHP_CURRENCY,'')SHP_CURRENCY,  NVL(REPAMT_WRT_AMT,'')REPAMT_WRT_AMT,NVL(SHORTCOLLECTIONAMOUNT,'')SHORTCOLLECTIONAMOUNT,NVL(RECORD_INDICATOR,'')RECORD_INDICATOR,TO_CHAR(TO_DATE(LEO_DATE,'DD-MM-YYYY'),'YYYY-MM-DD') LEO_DATE, NVL(EXPORT_AGENCY,'')EXPORT_AGENCY,NVL(EXPORT_TYPE,'')EXPORT_TYPE,NVL(DESTINATION_COUNTRRY,'')DESTINATION_COUNTRRY,NVL(IECODE,'')IECODE,NVL(ADCODE,'')ADCODE,NVL(CUSTOMS_NUMBER,'')CUSTOMS_NUMBER,  NVL(SHP_INV_SNO,'')SHP_INV_SNO,NVL(SHP_INVOICE_NO,'')SHP_INVOICE_NO,TO_CHAR(TO_DATE(SHP_INVOICE_DATE,'DD-MM-YYYY'),'YYYY-MM-DD')SHP_INVOICE_DATE,NVL(FOB_AMT,'')FOB_AMT,NVL(FREIGHT_AMT,'')FREIGHT_AMT,  NVL(INS_AMT,'')INS_AMT,NVL(COMM_AMT,'')COMM_AMT,NVL(DISCOUNT_AMT,'')DISCOUNT_AMT,NVL(DEDUCTION_AMT,'')DEDUCTION_AMT,NVL(PACKAGE_AMT,'')PACKAGE_AMT,NVL(REMITTANCE_ADCODE,'')REMITTANCE_ADCODE,  NVL(REMITTANCE_NUM,'')REMITTANCE_NUM,NVL(FIRC_NO,'')FIRC_NO,NVL(REMITTER_NAME,'')REMITTER_NAME,TO_CHAR(TO_DATE(REMITTER_DATE,'DD-MM-YYYY'),'YYYY-MM-DD')REMITTER_DATE,NVL(REMITTER_COUNTRY,'')REMITTER_COUNTRY, NVL(UTILIZATION_AMOUNT,'')UTILIZATION_AMOUNT, NVL(CIF_NUMBER,'')CIF_NUMBER,NVL(AVAILABLE_AMOUNT,'')AVAILABLE_AMOUNT,SNUMBER,TO_CHAR(TO_DATE(DR.PROCDATE,'DD-MM-YY'),'YYYY-MM-DD') AS EXPDATE FROM ETTDM_BULKUPLOAD_STAGING,DLYPRCCYCL DR WHERE STATUS ='SUCCESS' AND TI_STATUS IS NULL AND BATCH_NO = '" +
       









          makervo.getBatchId() + "' AND THEIRREF='" +
          sThRef + "' ";
       
        logger.info("Maker Upload Startss Query-------------->" + querys);
        ps = new LoggableStatement(con, querys);
        rs = ps.executeQuery();
        if (rs.next())
        {
          Msg = "Test 123";
         
          String shp_amt = " SELECT  CASE   WHEN RESSHPAMT = 'YES' THEN NVL(SUM(SHPAMT),0)+NVL(SUM(SHSHPAMT),0) WHEN RESSHPAMT = 'NO' THEN NVL(SUM(SHPAMT),0) ELSE 0 END SHPAMT  FROM (SELECT REASON_SHORT_SHP_AMT RESSHPAMT,SHP_AMT SHPAMT, SHORT_SHP_AMT SHSHPAMT  FROM ETTDM_BULKUPLOAD_STAGING  WHERE BATCH_NO='" +
         
            makervo.getBatchId() + "' AND THEIRREF  ='" + sThRef + "' " +
            " AND STATUS    ='SUCCESS' AND REASON_SHORT_SHP_AMT ='" +
            rs.getString("REASON_SHORT_SHP_AMT") + "') GROUP BY RESSHPAMT ";
         
          ps5 = new LoggableStatement(con, shp_amt);
          rsessess = ps5.executeQuery();
          if (rsessess.next()) {
            eq3Posting.setShp_amt(rsessess.getString("SHPAMT"));
          }
          rsessess.close();
          ps5.close();
         




          String lob_code = "SELECT NVL(LOBBCIF,LOB) LOB_CODE FROM EXTCUST WHERE CUST='" + rs.getString("DRAWER") + "'";
         
          lob = new LoggableStatement(con, lob_code);
         
          lob1 = lob.executeQuery();
          if (lob1.next()) {
            eq3Posting.setLob_code(lob1.getString("LOB_CODE"));
          }
          if (lob1 != null) {
            lob1.close();
          }
          if (lob != null) {
            lob.close();
          }
          double notional_rate = 0.0D;
          double notional_rate1 = 0.0D;
          double collection_amt = 0.0D;
          double inr_amt = 0.0D;
          String usd_equivalent_amt = "SELECT ETT_SPOTRATE_CAL('INR','USD') AS NOTIONAL_RATE FROM dual ";
         
          ps2 = new LoggableStatement(con, usd_equivalent_amt);
          rses = ps2.executeQuery();
          while (rses.next()) {
            notional_rate1 = Double.valueOf(rses.getString("NOTIONAL_RATE")).doubleValue();
          }
          if (rses != null) {
            rses.close();
          }
          if (ps2 != null) {
            ps2.close();
          }
          eq3Posting.setRate_in_usd(String.valueOf(notional_rate1));
         
          collection_amt = Double.valueOf(rs.getString("COLLECTIONAMOUNT")).doubleValue();
         
          eq3Posting.setUsd_quivalent_amt(String.valueOf(collection_amt));
         
          inr_amt = collection_amt * notional_rate1;
         
          eq3Posting.setInr_quivalent(String.valueOf(inr_amt));
          try
          {
            eq3Posting.setCollecting_bank(rs.getString("COLLECTING_BANK"));
           
            String collecting_bank = String.valueOf(rs.getString("COLLECTING_BANK"));
           
            String collecting_bank_address = "SELECT F.SVNAFF AS SEND_TO_ADDRESS FROM GFPF G,SX20LF  F  WHERE TRIM(G.GFCUS1)=TRIM(F.SXCUS1) AND F.SXCUS1='" + collecting_bank + "'";
            ps2 = new LoggableStatement(con, collecting_bank_address);
            logger.info("Collecting Bank Query---------------->" + ps2.getQueryString());
            rses = ps2.executeQuery();
            if (rses.next()) {
              eq3Posting.setCollecting_bank_addresss(rses.getString("SEND_TO_ADDRESS"));
            }
            if (rses != null) {
              rses.close();
            }
            if (ps2 != null) {
              ps2.close();
            }
          }
          catch (Exception e)
          {
            logger.info("Collecting Bank Exception----------------------->" + e.getMessage());
          }
          String port_of_loading_country = "SELECT NVL(PORTDESC,' ')PORTDESC,NVL(COUNTRY,' ')COUNTRY FROM EXTPORTODLOAD WHERE PORTLOAD='" +
            rs.getString("PORTOFLOADING") + "'";
          ps2 = new LoggableStatement(con, port_of_loading_country);
          rses = ps2.executeQuery();
          if (rses.next())
          {
            eq3Posting.setPortofloading_country(rses.getString("COUNTRY"));
            eq3Posting.setProtofloading_description(rses.getString("PORTDESC"));
          }
          if (rses != null) {
            rses.close();
          }
          if (ps2 != null) {
            ps2.close();
          }
          String remittance_num = rs.getString("REMITTANCE_NUM");
          if (remittance_num != null)
          {
            String spotrate_remittance = "SELECT ETT_SPOTRATE_CAL(STG.COLLECTIONCURRENCY, RM.SHP_CURRENCY) AS NOTIIONAL_RATE FROM ETTDM_BULKUPLOAD_STAGING STG,ETTV_REMITANCE_AMT RM WHERE TRIM(RM.REMITTANCE_NUM)=TRIM(STG.REMITTANCE_NUM) AND TRIM(STG.REMITTANCE_NUM)='" + remittance_num + "'" +
              "AND STG.STATUS  ='SUCCESS' AND STG.BATCH_NO ='" + makervo.getBatchId() + "' AND STG.THEIRREF  ='" + sThRef + "'";
           
            ps2 = new LoggableStatement(con, spotrate_remittance);
           
            rses = ps2.executeQuery();
            while (rses.next()) {
              notional_rate = Double.valueOf(rses.getString("NOTIIONAL_RATE")).doubleValue();
            }
            if (rses != null) {
              rses.close();
            }
            if (ps2 != null) {
              ps2.close();
            }
          }
          else
          {
            String notional_rates = "SELECT DISTINCT ETT_SPOTRATE_CAL(STG.COLLECTIONCURRENCY, STG.COLLECTIONCURRENCY) AS NOTIIONAL_RATE FROM ETTDM_BULKUPLOAD_STAGING STG,ETTV_REMITANCE_AMT RM  WHERE  STG.STATUS  ='SUCCESS' AND STG.BATCH_NO ='" +
              makervo.getBatchId() + "' AND STG.THEIRREF  ='" + sThRef + "'";
           
            LoggableStatement lss = new LoggableStatement(con, notional_rates);
           
            logger.info("Notional Rate----------------->" + lss.getQueryString());
           
            ResultSet resvalss = lss.executeQuery();
            if (resvalss.next())
            {
              String notiona_rates = resvalss.getString("NOTIIONAL_RATE");
              eq3Posting.setNotional_rate(notiona_rates);
            }
            if (resvalss != null) {
              resvalss.close();
            }
            if (lss != null) {
              lss.close();
            }
          }
          eq3Posting.setNotional_rate(String.valueOf(notional_rate));
         
          double rem_amt = 0.0D;
          try
          {
            String get_remittance_num = null;
            double remittance_amt = 0.0D;
            if (remittance_num != null)
            {
              LoggableStatement ls1 = null;
              ResultSet resvals1 = null;
              double ti_remittance_amts = 0.0D;
              double original_remittance = 0.0D;
              double original_firc_avail_amt = 0.0D;
              double original_firc_avail_amt1 = 0.0D;
              double original_firc_util_amt = 0.0D;
              double original_firc_util_amt1 = 0.0D;
              double stg_utilization_amt_1 = 0.0D;
              double amts_for_remittance_ti = 0.0D;
              double amts_for_remittance_or = 0.0D;
              double stg_remittance_amt_11 = 0.0D;
             

              String ti_remittance_amt = "SELECT ext.ccy_1,NVL(SUM(THEME_GENIUS_PKG.CONVAMT( ext.ccy_1, ext.amtutil) ),0) AS TOTAL_AMT FROM exteventadv ext,BASEEVENT bev,MASTER mas,ETTDM_BULKUPLOAD_STAGING stg WHERE ext.FK_EVENT = bev.EXTFIELD AND mas.KEY97      = bev.MASTER_KEY AND bev.STATUS    IN ('i','c') AND trim(ext.inward)     =trim(stg.REMITTANCE_NUM) AND stg.THEIRREF='" +
                sThRef + "' " +
                "\tAND  stg.BATCH_NO='" + makervo.getBatchId() + "' GROUP BY ext.ccy_1";
             
              ls1 = new LoggableStatement(con, ti_remittance_amt);
             
              logger.info("TI Remittance Query-------------->" + ls1.getQueryString());
             
              resvals1 = ls1.executeQuery();
              while (resvals1.next()) {
                ti_remittance_amts += Double.valueOf(resvals1.getString("TOTAL_AMT")).doubleValue();
              }
              logger.info("Ti Used Remittance Amount---------------->" + ti_remittance_amts);
              if (ls1 != null) {
                ls1.close();
              }
              if (resvals1 != null) {
                resvals1.close();
              }
              logger.info("amts_for_remittance_ti----------->" + amts_for_remittance_ti);
             





              String original_remittance_amt = "SELECT  RM.REMITTANCE_NUM,RM.SHP_CURRENCY,RM.REMITTANCE_AMOUNT FROM ETTV_REMITANCE_AMT RM,ETTDM_BULKUPLOAD_STAGING STG WHERE TRIM(RM.REMITTANCE_NUM)=TRIM(STG.REMITTANCE_NUM) AND STG.THEIRREF='" +
                sThRef + "' AND STG.BATCH_NO='" + makervo.getBatchId() + "'";
             

              LoggableStatement ls11 = new LoggableStatement(con, original_remittance_amt);
              ResultSet resvals11 = ls11.executeQuery();
             
              logger.info("Original Remittance Query-------------->" + ls11.getQueryString());
              while (resvals11.next())
              {
                original_remittance += Double.valueOf(resvals11.getString("REMITTANCE_AMOUNT")).doubleValue();
                eq3Posting.setRemittance_currency(resvals11.getString("SHP_CURRENCY"));
              }
              logger.info("amts_for_remittance_or--------------->" + original_remittance);
              if (ls11 != null) {
                ls11.close();
              }
              if (resvals11 != null) {
                resvals11.close();
              }
              String getting_firc_amt = "SELECT   FIRC_NO,AVAILABLE_AMOUNT,UTILIZATION_AMOUNT FROM ETTDM_BULKUPLOAD_STAGING WHERE FIRC_NO IS NOT NULL AND THEIRREF='" + sThRef + "' AND BATCH_NO='" + makervo.getBatchId() + "'";
             
              LoggableStatement ls12 = new LoggableStatement(con, getting_firc_amt);
              ResultSet resvals12 = ls12.executeQuery();
             
              logger.info("Original Remittance Query-------------->" + ls11.getQueryString());
              while (resvals12.next())
              {
                original_firc_avail_amt += Double.valueOf(resvals12.getString("AVAILABLE_AMOUNT")).doubleValue();
                original_firc_util_amt += Double.valueOf(resvals12.getString("UTILIZATION_AMOUNT")).doubleValue();
              }
              original_firc_avail_amt1 += original_firc_avail_amt;
              original_firc_util_amt1 += original_firc_util_amt;
              logger.info("amts_for_remittance_or--------------->" + amts_for_remittance_or);
              if (ls12 != null) {
                ls12.close();
              }
              if (resvals12 != null) {
                resvals12.close();
              }
              String stg_utilization_amt = "SELECT  REMITTANCE_NUM,AVAILABLE_AMOUNT,UTILIZATION_AMOUNT FROM ETTDM_BULKUPLOAD_STAGING WHERE STATUS='SUCCESS' AND THEIRREF ='" + sThRef + "' AND BATCH_NO='" + makervo.getBatchId() + "' AND REMITTANCE_NUM IS NOT NULL ";
              ls1 = new LoggableStatement(con, stg_utilization_amt);
              logger.info("stg_remittance_amt------>" + ls1.getQueryString());
             
              resvals1 = ls1.executeQuery();
              while (resvals1.next()) {
                stg_utilization_amt_1 += Double.valueOf(resvals1.getString("UTILIZATION_AMOUNT")).doubleValue();
              }
              logger.info("stg_remittance_amt_11------------->" + stg_utilization_amt_1);
              if (ls1 != null) {
                ls1.close();
              }
              if (resvals1 != null) {
                resvals1.close();
              }
              double stg_remittance_amt_temp = stg_utilization_amt_1 + original_firc_util_amt;
             
              logger.info("Advance Received Amount--------------------------->" + amts_for_remittance_or);
              logger.info("FIRC AVAILABLE [REMITTANCE AMOUNT]-------------------------->" + original_firc_avail_amt1);
              logger.info("FIRC UTIZATION AMOUNT----------------------------->" + original_firc_util_amt);
              logger.info("ti_remittance_amts-------------------------------->" + amts_for_remittance_ti);
             
              double advance_received_amt = original_firc_avail_amt1 + original_remittance - ti_remittance_amts;
              logger.info("advance_received_amt-------------------------------->" + advance_received_amt);
              logger.info("setAdvance_collection_amt--------------> " + advance_received_amt);
              eq3Posting.setAdvance_collection_amt(String.valueOf(advance_received_amt));
             
              logger.info("setAmt_tobe_collected-------------> " + stg_remittance_amt_temp);
              eq3Posting.setAmt_tobe_collected(String.valueOf(stg_remittance_amt_temp));
            }
            else
            {
              String firc_num = rs.getString("FIRC_NO");
             
              logger.info("FIRC Amount---------part--------->");
             

              String advnce_amt_query = " SELECT   REMITTANCE_NUM,FIRC_NO,NVL(UTILIZATION_AMOUNT,0) AS UTILIZATION_AMOUNT,NVL(AVAILABLE_AMOUNT,0) REMITTANCE_AMT  FROM ETTDM_BULKUPLOAD_STAGING WHERE STATUS='SUCCESS' AND THEIRREF='" + sThRef + "' AND BATCH_NO='" + makervo.getBatchId() + "' AND FIRC_NO IS NOT NULL";
             
              LoggableStatement ls = new LoggableStatement(con, advnce_amt_query);
             
              logger.info("FIRC ADVance Received & AMount to be COllected Query------------->" + ls.getQueryString());
             
              ResultSet resvals = ls.executeQuery();
              while (resvals.next())
              {
                rem_amt += Double.valueOf(resvals.getString("UTILIZATION_AMOUNT")).doubleValue();
                remittance_amt = Double.valueOf(resvals.getString("REMITTANCE_AMT")).doubleValue();
              }
              if (resvals != null) {
                resvals.close();
              }
              if (ls != null) {
                ls.close();
              }
              eq3Posting.setAmt_tobe_collected(String.valueOf(remittance_amt));
             
              logger.info("Amount to be Collected--------------->" + remittance_amt);
              eq3Posting.setAdvance_collection_amt(String.valueOf(rem_amt));
              logger.info("Advance Collection amount--------------->" + rem_amt);
             
              eq3Posting.setRemittance_currency(rs.getString("COLLECTIONCURRENCY"));
            }
          }
          catch (Exception e)
          {
            logger.info("Exception-------in fric-------------------------------------------" + e.getMessage());
          }
          eq3Posting.setDraweecustomerid(rs.getString("DRAWEECUSTOMERID"));
          eq3Posting.setDraweecountry(rs.getString("DRAWEE_NAME"));
          String drawee_name = rs.getString("DRAWEECUSTOMERID");
          if (drawee_name != null)
          {
            String drawee_address = "SELECT NVL(GFCUN,' ') GFCUN ,NVL(GFCNAL,' ')GFCNAL FROM GFPF  WHERE  gfcus1 ='" +
              rs.getString("DRAWEECUSTOMERID") + "'";
            ps3 = new LoggableStatement(con, drawee_address);
            rsess = ps3.executeQuery();
            if (rsess.next())
            {
              eq3Posting.setDrawee_address(rsess.getString("GFCUN"));
              eq3Posting.setDrawee_name(rsess.getString("GFCNAL"));
            }
            rsess.close();
            ps3.close();
          }
          else if (drawee_name == null)
          {
            eq3Posting.setDrawee_address(rs.getString("DRAWEE_ADDRESS"));
           
            eq3Posting.setDrawee_name(rs.getString("DRAWEE_NAME"));
          }
          String portofDestination = "SELECT NVL(PODESPN,' ')PODESPN,NVL(COUNTRY,' ')COUNTRY FROM EXTPORTDESTINATION WHERE PODEST='" +
            rs.getString("PORTOFDESTINATION") + "'";
          ps4 = new LoggableStatement(con, portofDestination);
          rsesses = ps4.executeQuery();
          if (rsesses.next())
          {
            eq3Posting.setPortofDestinationCountry(rsesses.getString("COUNTRY"));
            eq3Posting.setPortofDestinationDescription(rsesses.getString("PODESPN"));
          }
          rsesses.close();
          ps4.close();
         
          String charge_account_number = rs.getString("CHARGE_ACC");
          if (charge_account_number != null)
          {
            logger.info("Charge Account Number from excel-------------------->" + charge_account_number);
            eq3Posting.setChargeAccount(charge_account_number);
          }
          else
          {
            String get_charge_account_number = "SELECT AC.BO_ACCTNO AS BO_ACCTNO FROM ACCOUNT AC, GVPF GP\tWHERE GP.GVMVT ='C'\tAND GP.GVPRF   ='R'\tAND GP.GVACCT  =AC.ACCT_KEY AND AC.CUS_MNM ='" + rs.getString("DRAWER") + "' ";
           


            charge = new LoggableStatement(con, get_charge_account_number);
            logger.info("Charge Account Number------------------->" + charge.getQueryString());
           

            charge_1 = charge.executeQuery();
            if (charge_1.next())
            {
              eq3Posting.setChargeAccount(charge_1.getString("BO_ACCTNO"));
             
              logger.info("Charge Account Number from back end data-------------------->" + charge_1.getString("BO_ACCTNO"));
            }
          }
          sshipbillno = rs.getString("SHPBILL_NO");
          sshipbilldate = rs.getString("BILL_DATE");
          pportcode = rs.getString("PORT_CODE");
          remno = rs.getString("REMITTANCE_NUM");
          formnumber = rs.getString("FORM_NO");
          draweecusid = rs.getString("DRAWEECUSTOMERID");
          theirref = rs.getString("THEIRREF");
          eq3Posting.setMsg(Msg);
         
          eq3Posting.setTheirref(rs.getString("THEIRREF"));
          ref = rs.getString("THEIRREF");
          eq3Posting.setTheirref(rs.getString("THEIRREF"));
          eq3Posting.setBehalfofbranch(rs.getString("BEHALFOFBRANCH"));
          eq3Posting.setDrawer(rs.getString("DRAWER"));
         
          eq3Posting.setCollectionamount(rs.getString("COLLECTIONAMOUNT"));
          eq3Posting.setCollectioncurrency(rs.getString("COLLECTIONCURRENCY"));
          eq3Posting.setShipmenttocountry(rs.getString("SHIPMENTTOCOUNTRY"));
          eq3Posting.setShipmentfromcountry(rs.getString("SHIPMENTFROMCOUNTRY"));
          eq3Posting.setHscode(rs.getString("HSCODE"));
          eq3Posting.setIncoterms(rs.getString("INCOTERMS"));
          eq3Posting.setGoodscode(rs.getString("GOODSCODE"));
          eq3Posting.setGooddescription(rs.getString("GOODDESCRIPTION"));
          eq3Posting.setPortofdestination(rs.getString("PORTOFDESTINATION"));
          eq3Posting.setPortofloading(rs.getString("PORTOFLOADING"));
          eq3Posting.setTransportdocno(rs.getString("TRANSPORTDOCNO"));
          eq3Posting.setTransportdocdate(rs.getString("TRANSPORTDOCDATE"));
          eq3Posting.setInvoiceno(rs.getString("INVOICENO"));
          eq3Posting.setInvoicedate(rs.getString("INVOICEDATE"));
          eq3Posting.setForm_type(rs.getString("FORM_TYPE"));
          eq3Posting.setShpbill_no(rs.getString("SHPBILL_NO"));
          eq3Posting.setBill_date(rs.getString("BILL_DATE"));
          eq3Posting.setPort_code(rs.getString("PORT_CODE"));
          eq3Posting.setForm_no(rs.getString("FORM_NO"));
          eq3Posting.setShp_currency(rs.getString("SHP_CURRENCY"));
          eq3Posting.setReason_short_shp_amt(rs.getString("REASON_SHORT_SHP_AMT"));
          eq3Posting.setShort_shp_amt(rs.getString("SHORT_SHP_AMT"));
         
          eq3Posting.setRepamt_wrt_amt(rs.getString("REPAMT_WRT_AMT"));
          eq3Posting.setShortcollectionamount(rs.getString("SHORTCOLLECTIONAMOUNT"));
          eq3Posting.setRecord_indicator(rs.getString("RECORD_INDICATOR"));
          eq3Posting.setLeo_date(rs.getString("LEO_DATE"));
          eq3Posting.setExport_agency(rs.getString("EXPORT_AGENCY"));
          eq3Posting.setExport_type(rs.getString("EXPORT_TYPE"));
          eq3Posting.setDestination_countrry(rs.getString("DESTINATION_COUNTRRY"));
          eq3Posting.setIecode(rs.getString("IECODE"));
          eq3Posting.setAdcode(rs.getString("ADCODE"));
          eq3Posting.setCustoms_number(rs.getString("CUSTOMS_NUMBER"));
          eq3Posting.setShp_inv_sno(rs.getString("SHP_INV_SNO"));
          eq3Posting.setShp_invoice_date(rs.getString("SHP_INVOICE_DATE"));
          eq3Posting.setFob_amt(rs.getString("FOB_AMT"));
          eq3Posting.setFreight_amt(rs.getString("FREIGHT_AMT"));
          eq3Posting.setIns_amt(rs.getString("INS_AMT"));
          eq3Posting.setComm_amt(rs.getString("COMM_AMT"));
          eq3Posting.setDiscount_amt(rs.getString("DISCOUNT_AMT"));
          eq3Posting.setDeduction_amt(rs.getString("DEDUCTION_AMT"));
          eq3Posting.setDeduction_amt(rs.getString("PACKAGE_AMT"));
          eq3Posting.setRemittance_adcode(rs.getString("REMITTANCE_ADCODE"));
          eq3Posting.setRemittance_num(rs.getString("REMITTANCE_NUM"));
          eq3Posting.setFirc_no(rs.getString("FIRC_NO"));
          eq3Posting.setRemitter_name(rs.getString("REMITTER_NAME"));
          eq3Posting.setRemitter_date(rs.getString("REMITTER_DATE"));
          eq3Posting.setRemitter_country(rs.getString("REMITTER_COUNTRY"));
          eq3Posting.setUtilization_amount(rs.getString("UTILIZATION_AMOUNT"));
          eq3Posting.setCif_number(rs.getString("CIF_NUMBER"));
          eq3Posting.setSnumber(rs.getString("SNUMBER"));
          eq3Posting.setExp_date(rs.getString("EXPDATE"));
         
          list.add(eq3Posting);
          result = getLimitXMLFORPosting(list, result, con, makervo.getBatchId().trim(), ref, sshipbillno,
            sshipbilldate, pportcode, remno, formnumber, draweecusid, theirref);
          list.clear();
         
          rowCount++;
        }
      }
    }
    catch (Exception exception)
    {
      logger.info("XML GENERNATING MAKER UPLOAD XML\t Exception---------------------------->" + exception);
    }
    finally
    {
      closeSqlRefferance(ps, con);
    }
    logger.info("Exiting Method");
    try
    {
      logger.info("Updating Date in the pane........");
     

      String updatepane = "{call ETT_ODC_PANE_UPDATER()}";
      Connection conn = DBConnectionUtility.getConnection();
      CallableStatement callableStatement = conn.prepareCall(updatepane);
      callableStatement.executeUpdate();
      closeSqlRefferance(null, conn);
     
      logger.info("Updation Completed !!!!!!....");
    }
    catch (Exception e)
    {
      e.printStackTrace();
      logger.info("*-------WARNING-------*" + e.getMessage());
    }
    return count;
  }
 
  public int rejectAll(MakerHomeVO makervo)
    throws DAOException
  {
    int count = 0;
    Connection conn = null;
    LoggableStatement pse = null;
   






    return count;
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
 
  public String getLimitXMLFORPosting(ArrayList<EQ3Posting> list, String result, Connection con, String batch, String ref, String sshipbillno, String sshipbilldate, String pportcode, String remno, String formnumber, String draweecusid, String theirref)
    throws TransformerConfigurationException, ParserConfigurationException, RemoteException, Exception
  {
    logger.info("The EXCEL VALUE LIST SIZE : " + list.size());
    logger.info("The EXCEL Value is : " + list);
   
    String temp = null;
    String[] finalXML = null;
    if (list != null)
    {
      int size = list.size();
      finalXML = new String[size];
      for (int i = 0; i < size; i++)
      {
        String endString = "";
        String xml1 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><ServiceRequest xmlns:ns3=\"urn:common.service.ti.apps.tiplus2.misys.com\" xmlns:ns2=\"urn:messages.service.ti.apps.tiplus2.misys.com\" xmlns=\"urn:control.services.tiplus2.misys.com\" xmlns:ns4=\"urn:custom.service.ti.apps.tiplus2.misys.com\"><RequestHeader><Service>TIBulk</Service><Operation>Item</Operation><Credentials><Name>ODCBULK</Name></Credentials><ReplyFormat>FULL</ReplyFormat><NoRepair>N</NoRepair><NoOverride>N</NoOverride></RequestHeader><ns2:ItemRequest><ServiceRequest><RequestHeader><Service>TI</Service><Operation>TFCOLNEW</Operation><Credentials><Name>SUPERVISOR</Name></Credentials><ReplyFormat>FULL</ReplyFormat><NoRepair>N</NoRepair><NoOverride>N</NoOverride><CorrelationId>" +
       







          ref +
          "</CorrelationId></RequestHeader>";
       
        String xml2 = "</ServiceRequest></ns2:ItemRequest></ServiceRequest>";
        endString = endString + xml1;
       
        logger.info("Posting xml generation.******************************************." + list.size());
        temp = generatecreatexml((EQ3Posting)list.get(i), con, batch, ref, sshipbillno, sshipbilldate, pportcode, remno,
          formnumber, draweecusid, theirref);
        temp = temp.replaceAll("\\<\\?xml(.+?)\\?\\>", "").trim();
        finalXML[i] = temp;
        endString = endString + finalXML[i];
        endString = endString + xml2;
        logger.info(list.get(i) + " TI XML Stating Time - " + new Date());
        try
        {
          logger.info("endString------------>" + endString);
         
          logger.info("endString---------------------------->" + endString);
          int insertStatus = CommonMethods.insertBulkAPIFiles(batch, ref, endString);
          if (insertStatus > 0)
          {
            String finalResult = TIPlusEJBClient.process(endString, 1);
            if (!finalResult.equals("Unable to Process"))
            {
              List<ServiceResponse> serviceResponses = MessageUtil.processResponse(finalResult);
              for (ServiceResponse serviceResponse : serviceResponses)
              {
                logger.info("**Response Status***" + serviceResponse.getResponseHeader().getStatus());
                logger.info("**Response CorrID***" + serviceResponse.getResponseHeader().getStatus());
               
                logger.info("TI XML STATUS------:" + serviceResponse.getResponseHeader().getStatus());
                logger.info("TI XML STATUS CORELATION ID------:" +
                  serviceResponse.getResponseHeader().getCorrelationId());
                logger.info("TI XML STATUS ERROR------:" +
                  serviceResponse.getResponseHeader().getDetails().toString());
               
                updateTi_Status(con, batch, serviceResponse.getResponseHeader().getStatus(),
                  serviceResponse.getResponseHeader().getCorrelationId(),
                  serviceResponse.getResponseHeader().getDetails().toString());
                CommonMethods.updateAPIServer(batch, ref, finalResult, serviceResponse.getResponseHeader().getStatus().toString());
                updateExpiryDateInDiary(con, batch, ref);
              }
            }
          }
          else
          {
            logger.info("Duplicate Event tried to created..........So skipped the process for Reference: " + ref + " Batch: " + batch);
          }
        }
        catch (Exception e)
        {
          logger.info("ThemeTransportClient------------>" + e.getMessage());
          e.printStackTrace();
        }
        logger.info("Debugmessage:: Limit Response " + result);
        logger.info(list.get(i) + " TI XML Ending Time - " + new Date());
      }
      logger.info("<--Uploaded Reference :" + ref + " Batch: " + batch);
    }
    logger.info("<------------Uploaded Successfully--------------->");
    return result;
  }
 
  public String updatingFieldsManually(EQ3Posting list1, String result, Connection con, String batch, String ref, String sshipbillno, String sshipbilldate, String pportcode, String remno, String formnumber, String draweecusid, String theirref)
  {
    ODCUpdate odcupdate = new ODCUpdate();
   

    String PRI_REF = null;
    String batchno = null;
    String updateQuery = null;
    String status = "P";
    String Shpbillno = null;
    String portcde = null;
    String formno = null;
    String updatedate = null;
    String leodate = null;
    String invno = null;
    String invdate = null;
    String cust = null;
    String remitNum = null;
    String firc = null;
    String remitName = null;
    String remitDate = null;
    String formtype = null;
   

    String valueeventshc = null;
    String valueexteventspd = null;
    String valueexteventinv = null;
    String valuegetgetremittance = null;
    int insertValue;
    try
    {
      PRI_REF = list1.getTheirref();logger.info("----------PRI_REF------------" + PRI_REF);
      batchno = list1.getBatchId();
      Shpbillno = list1.getShpbill_no();logger.info("----------Shpbillno------------" + Shpbillno);
      portcde = list1.getPort_code();logger.info("----------portcde------------" + portcde);
      formno = list1.getForm_no();logger.info("----------formno------------" + formno);
      updatedate = list1.getBill_date();logger.info("----------updatedate------------" + updatedate);
      leodate = list1.getLeo_date();logger.info("----------leodate------------" + leodate);
      invno = list1.getInvoiceno();logger.info("----------invno------------" + invno);
      invdate = list1.getInvoicedate();logger.info("----------invdate------------" + invdate);
      cust = list1.getDrawer();logger.info("----------cust------------" + cust);
      remitNum = list1.getRemittance_num();logger.info("-------remitNum---------" + remitNum);
      firc = list1.getFirc_no();logger.info("-------firc---------" + firc);
      remitName = list1.getRemitter_name();logger.info("-------remitName---------" + remitName);
      remitDate = list1.getRemitter_date();logger.info("----------remitDate-----------" + remitDate);
      formtype = list1.getForm_type();logger.info("-----------formtype--------------" + formtype);
     

      valueeventshc = odcupdate.Updateexteventshc(PRI_REF, portcde, formno, Shpbillno, updatedate);
      logger.info("---------Event exteventshc------" + valueeventshc);
     

      valueexteventspd = odcupdate.getvalueexteventspd(PRI_REF, portcde, formno, Shpbillno, updatedate, leodate);
      logger.info("---------Event exteventspd------" + valueexteventspd);
     

      valueexteventinv = odcupdate.getupdateexteventinv(PRI_REF, invno, formno, Shpbillno, invdate);
      logger.info("----------Event exteventinv----------" + valueexteventinv);
     
      String inwardRemitDate = list1.getRemitter_date();logger.info("----------inwardRemitDate------------" + inwardRemitDate);
     
      valuegetgetremittance = odcupdate.getgetremittance(PRI_REF, cust, inwardRemitDate);
      logger.info("----------Event EXTEVENTADV----------" + valuegetgetremittance);
      logger.info("---------------Milestone------------01--^^^^^^^^^^^^^^^");
      updateQuery = valueeventshc + ":" + valueexteventspd + ":" + valueexteventinv + ":" + valuegetgetremittance;
      logger.info("---------------Milestone------------02--^^^^^^^^^^^^^^^");
      logger.info("^^^^^^^^^^^^^^^^^^^^^^" + updateQuery);
    }
    catch (Exception e)
    {
      e.printStackTrace();
      logger.info("Error in catching ***************" + e.getMessage());
      try
      {
        logger.info("------------(- -)--------------" + updateQuery);
        logger.info("------------(-^-)--------------" + updateQuery);
        logger.info("-----------Inside if----Milestone------------02--^^^^^^^^^!!!!!!^^^^^^");
        int insertValue = odcupdate.insert_ODCUPDATE_PANE(PRI_REF, updateQuery, status, Shpbillno, portcde, formno, updatedate, leodate, invno, invdate, cust, remitNum, firc, remitName, remitDate, formtype);
        logger.info("#-----Insert count------#" + insertValue);
      }
      catch (Exception e)
      {
        e.printStackTrace();
        logger.info("Error in catching ***************" + e.getMessage());
      }
    }
    finally
    {
      try
      {
        logger.info("------------(- -)--------------" + updateQuery);
        logger.info("------------(-^-)--------------" + updateQuery);
        logger.info("-----------Inside if----Milestone------------02--^^^^^^^^^!!!!!!^^^^^^");
        int insertValue = odcupdate.insert_ODCUPDATE_PANE(PRI_REF, updateQuery, status, Shpbillno, portcde, formno, updatedate, leodate, invno, invdate, cust, remitNum, firc, remitName, remitDate, formtype);
        logger.info("#-----Insert count------#" + insertValue);
      }
      catch (Exception e)
      {
        e.printStackTrace();
        logger.info("Error in catching ***************" + e.getMessage());
      }
    }
    return null;
  }
 
  public static void updateTi_Status(Connection con, String batch, StatusEnum statusEnum, String correaltionID, String errorDtls)
    throws SQLException
  {
    logger.info("1----StatusEum" + statusEnum);
    logger.info("2----errorDtls" + errorDtls);
    logger.info("3----correaltionID" + correaltionID);
    try
    {
      String query = "UPDATE ETTDM_BULKUPLOAD_STAGING SET TI_STATUS = '" + statusEnum + "', TI_ERROR = '" +
        errorDtls + "' WHERE TRIM(BATCH_NO) = '" + batch + "' AND TRIM(THEIRREF) = '" + correaltionID +
        "' AND TRIM(STATUS)='SUCCESS'";
      LoggableStatement ti_update = new LoggableStatement(con, query);
      int i = ti_update.executeUpdate();
    }
    catch (SQLException ex)
    {
      logger.info("updateTi_Status Exception" + ex);
    }
  }
 
  public void updateExpiryDateInDiary(Connection con, String batchId, String theirRef)
  {
    LoggableStatement pst = null;
    LoggableStatement pst1 = null;
    ResultSet rs = null;
    try
    {
      String diaryFetchQuery = "SELECT KEY97 FROM ETTV_ODC_BULKUPLOAD_DIARYENTRY WHERE BATCH_NO = '" + batchId +
        "' AND THEIRREF = '" + theirRef + "' ";
      pst = new LoggableStatement(con, diaryFetchQuery);
      rs = pst.executeQuery();
      while (rs.next())
      {
        String key97 = rs.getString("KEY97");
        String query = "UPDATE DIARYENTRY SET DDATE = (SELECT TO_DATE(PROCDATE, 'DD-MM-YY') FROM DLYPRCCYCL) WHERE KEY97 = '" +
          key97 + "'";
        pst1 = new LoggableStatement(con, query);
        int row = pst1.executeUpdate();
        logger.info(row + " updated successfully");
      }
    }
    catch (Exception e)
    {
      logger.info(
        "Exception in updateExpiryDateInDiary - " + e.getMessage() + " for the reference - " + theirRef);
    }
    finally
    {
      DBConnectionUtility.surrenderDB(null, pst1, null);
      DBConnectionUtility.surrenderDB(null, pst, rs);
    }
  }
 
  public String getpayxml(ArrayList<EQ3Posting> list, String result, Connection con, String theirref, String batch)
    throws TransformerConfigurationException, ParserConfigurationException, RemoteException, Exception
  {
    String masterRef = null;
    String remitanceamount = null;
    con = DBConnectionUtility.getConnection();
    Statement stmt = con.createStatement();
    String query = "select master_ref from master mas where mas.pri_ref='" + theirref + "'";
    ResultSet rs = stmt.executeQuery(query);
    logger.info("Query to select Master Reference from TI Table");
    logger.info(query);
    while (rs.next()) {
      masterRef = rs.getString("master_ref").trim();
    }
    Statement stmt2 = con.createStatement();
    String query2 = "select sum(REMITTANCE_AMOUNT) sumamt,THEIRREF  from ETTDM_BULKUPLOAD_STAGING where THEIRREF = '" +
      theirref + "'  " + " and BATCH_NO = '" + batch + "' ";
    ResultSet rs2 = stmt2.executeQuery(query2);
    while (rs2.next()) {
      remitanceamount = rs2.getString("sumamt");
    }
    String[] finalXML = null;
    if (list != null)
    {
      int size = list.size();
     
      finalXML = new String[size];
      for (int i = 0; i < size; i++)
      {
        String endString = "";
        String xml1 = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><ServiceRequest xmlns:ns3=\"urn:common.service.ti.apps.tiplus2.misys.com\" xmlns:ns2=\"urn:messages.service.ti.apps.tiplus2.misys.com\" xmlns=\"urn:control.services.tiplus2.misys.com\" xmlns:ns4=\"urn:custom.service.ti.apps.tiplus2.misys.com\"><RequestHeader><Service>TIBulk</Service><Operation>Item</Operation><Credentials><Name>SUPERVISOR</Name></Credentials><ReplyFormat>FULL</ReplyFormat><NoRepair>N</NoRepair><NoOverride>N</NoOverride><CorrelationId>da98f562df34d822:-1a2b12fe:159bdd90698:-7f39</CorrelationId></RequestHeader><ns2:ItemRequest><ServiceRequest><RequestHeader><Service>TI</Service><Operation>TFCOLPAY</Operation><Credentials><Name>SUPERVISOR</Name></Credentials><ReplyFormat>FULL</ReplyFormat><NoRepair>N</NoRepair><NoOverride>N</NoOverride></RequestHeader>";
       







        String xml2 = "</ServiceRequest></ns2:ItemRequest></ServiceRequest>";
        endString = endString + xml1;
        String temp = null;
        logger.info("Posting xml generation..");
       
        temp = generatepayxml((EQ3Posting)list.get(i), con, masterRef, theirref, batch, remitanceamount);
        temp = temp.replaceAll("\\<\\?xml(.+?)\\?\\>", "").trim();
        finalXML[i] = temp;
        endString = endString + finalXML[i];
        endString = endString + xml2;
        try
        {
          logger.info("Debugmessage::Final Limit XML------------>" + endString);
          logger.info("Debugmessage::Final Limit XML------------>" + endString);
          logger.info("endString------------>" + endString);
          String finalResult = TIPlusEJBClient.process(endString, 1);
          if (!finalResult.equals("Unable to Process"))
          {
            List<ServiceResponse> serviceResponses = MessageUtil.processResponse(finalResult);
            for (ServiceResponse serviceResponse : serviceResponses)
            {
              logger.info("**Response Status***" + serviceResponse.getResponseHeader().getStatus());
              logger.info("**Response CorrID***" + serviceResponse.getResponseHeader().getStatus());
            }
          }
        }
        catch (Exception e)
        {
          logger.info("ThemeTransportClient------------>" + e.getMessage());
          logger.info("ThemeTransportClient------------>" + e.getMessage());
          e.printStackTrace();
        }
      }
    }
    return result;
  }
 
  public String generatepayxml(EQ3Posting eq3Posting, Connection con, String masterRef, String theirref, String batch, String remamount)
    throws TransformerConfigurationException, ParserConfigurationException, Exception
  {
    DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
    Document document = documentBuilder.newDocument();
   
    Element root = document.createElement("ns2:TI");
    document.appendChild(root);
   
    Element rocdoc0 = document.createElement("ns2:TFLCOLNEW");
    root.appendChild(rocdoc0);
   
    String str1 = convertDocumentToString(document);
   
    return str1;
  }
 
  public String generatecreatexml(EQ3Posting eq3Posting, Connection con, String batch, String ref, String sshipbillno, String sshipbilldate, String pportcode, String remno, String formnumber, String draweecusid, String theirref)
    throws TransformerConfigurationException, ParserConfigurationException, Exception
  {
    ref = String.valueOf(eq3Posting.getTheirref());
   
    String str1 = null;
    try
    {
      String remitance = "";
      Statement stmt = con.createStatement();
      Statement stmt1 = con.createStatement();
      Statement stmt2 = con.createStatement();
      Statement stmt4 = con.createStatement();
      Statement stmt5 = con.createStatement();
      Statement stmt6 = con.createStatement();
      Statement stmt7 = con.createStatement();
      Statement stmt8 = con.createStatement();
     

      ResultSet rs = null;
      ResultSet rs1 = null;
      ResultSet rs2 = null;
      ResultSet rs4 = null;
      ResultSet rs5 = null;
      ResultSet rs6 = null;
      ResultSet rs7 = null;
      ResultSet rs8 = null;
     
      String formtyp = eq3Posting.getForm_type();
     
      ArrayList<String> remList = new ArrayList();
     
      DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
      Document document = documentBuilder.newDocument();
      if ((formtyp.equalsIgnoreCase("EDI")) || (formtyp.equalsIgnoreCase("SOFTEX")) ||
        (formtyp.equalsIgnoreCase("EXEMPTED")) || (formtyp.equalsIgnoreCase("EDF")))
      {
        Element root = document.createElement("ns2:TFCOLNEW");
        document.appendChild(root);
       
        Element rocdoc0 = document.createElement("ns2:Context");
        root.appendChild(rocdoc0);
       
        String Branch = String.valueOf(eq3Posting.getBehalfofbranch());
        Element ps = document.createElement("ns3:Branch");
        ps.appendChild(document.createTextNode(Branch.trim()));
        rocdoc0.appendChild(ps);
       
        String customer = String.valueOf(eq3Posting.getDrawer());
        ps = document.createElement("ns3:Customer");
        ps.appendChild(document.createTextNode(customer.trim()));
        rocdoc0.appendChild(ps);
       
        ps = document.createElement("ns3:Product");
        ps.appendChild(document.createTextNode("ODC"));
        rocdoc0.appendChild(ps);
       
        ps = document.createElement("ns3:Event");
        ps.appendChild(document.createTextNode("CRE"));
        rocdoc0.appendChild(ps);
       
        String TheirReference = String.valueOf(eq3Posting.getTheirref());
        ps = document.createElement("ns3:TheirReference");
        ps.appendChild(document.createTextNode(TheirReference.trim()));
        rocdoc0.appendChild(ps);
       
        ps = document.createElement("ns3:Team");
        ps.appendChild(document.createTextNode("BULKUPLOAD"));
        rocdoc0.appendChild(ps);
       
        String BehalfOfBranch = String.valueOf(eq3Posting.getBehalfofbranch());
        ps = document.createElement("ns3:BehalfOfBranch");
        ps.appendChild(document.createTextNode(BehalfOfBranch.trim()));
        rocdoc0.appendChild(ps);
       
        Element ps1 = document.createElement("ns2:CollectionType");
        ps1.appendChild(document.createTextNode("T"));
        root.appendChild(ps1);
       
        Element ps2 = document.createElement("ns2:Drawer");
        root.appendChild(ps2);
       
        String Drawercust = String.valueOf(eq3Posting.getDrawer());
        ps = document.createElement("ns3:Customer");
        ps.appendChild(document.createTextNode(Drawercust.trim()));
        ps2.appendChild(ps);
       
        String refno = String.valueOf(eq3Posting.getTheirref());
        ps = document.createElement("ns3:Reference");
        ps.appendChild(document.createTextNode(refno.trim()));
        ps2.appendChild(ps);
       
        Element drawee = document.createElement("ns2:Drawee");
        root.appendChild(drawee);
       
        String Draweecust = String.valueOf(eq3Posting.getDraweecustomerid());
        ps = document.createElement("ns3:Customer");
        ps.appendChild(document.createTextNode(Draweecust.trim()));
        drawee.appendChild(ps);
       
        String draweeAddress = String.valueOf(eq3Posting.getDrawee_address());
        ps = document.createElement("ns3:NameAddress");
        ps.appendChild(document.createTextNode(draweeAddress.trim()));
        drawee.appendChild(ps);
       
        String draweerefno = String.valueOf(eq3Posting.getDraweecountry());
        logger.info("------Country-----" + draweerefno);
        if (!draweerefno.equals(""))
        {
          ps = document.createElement("ns3:Country");
          ps.appendChild(document.createTextNode(draweerefno.trim()));
          drawee.appendChild(ps);
        }
        Element direct = document.createElement("ns2:DirectCollection");
        direct.appendChild(document.createTextNode("N"));
        root.appendChild(direct);
       
        Element collectionbank = document.createElement("ns2:CollectingBank");
        root.appendChild(collectionbank);
       

        String collecting_bank = String.valueOf(eq3Posting.getCollecting_bank());
        ps = document.createElement("ns3:Customer");
        ps.appendChild(document.createTextNode(collecting_bank.trim()));
        collectionbank.appendChild(ps);
       

        String collecting_bank_address = String.valueOf(eq3Posting.getCollecting_bank_addresss().trim());
        ps = document.createElement("ns3:NameAddress");
        ps.appendChild(document.createTextNode(collecting_bank_address.trim()));
        collectionbank.appendChild(ps);
       


        Element collectionamt = document.createElement("ns2:CollectionAmount");
        root.appendChild(collectionamt);
       
        String collectAmount = String.valueOf(eq3Posting.getCollectionamount());
        ps = document.createElement("ns3:Amount");
        ps.appendChild(document.createTextNode(collectAmount.trim()));
        collectionamt.appendChild(ps);
       

        String collectccy = String.valueOf(eq3Posting.getCollectioncurrency());
        ps = document.createElement("ns3:Currency");
        ps.appendChild(document.createTextNode(collectccy.trim()));
        collectionamt.appendChild(ps);
       
        Element collectdraft1 = document.createElement("ns2:CollectionDraftss");
        root.appendChild(collectdraft1);
       
        Element collectdraft2 = document.createElement("ns2:CollectionDrafts");
        collectdraft1.appendChild(collectdraft2);
       
        ps = document.createElement("ns2:DraftRelease");
        ps.appendChild(document.createTextNode("P"));
        collectdraft2.appendChild(ps);
       
        String GoodsDesc = String.valueOf(eq3Posting.getGooddescription());
        Element GoodsDescription = document.createElement("ns2:GoodsDescription");
        GoodsDescription.appendChild(document.createTextNode(GoodsDesc.trim()));
        root.appendChild(GoodsDescription);
       
        String Goodscode = String.valueOf(eq3Posting.getGoodscode());
        Element Goodscd = document.createElement("ns2:GoodsCode");
        Goodscd.appendChild(document.createTextNode(Goodscode.trim()));
        root.appendChild(Goodscd);
       
        Element doc1 = document.createElement("ns2:Documentss");
        root.appendChild(doc1);
       
        Element doc2 = document.createElement("ns2:Documents");
        doc1.appendChild(doc2);
       
        ps = document.createElement("ns2:Document");
        ps.appendChild(document.createTextNode("GEN"));
        doc2.appendChild(ps);
       
        ps = document.createElement("ns2:DocumentDesc");
        ps.appendChild(document.createTextNode("Other Document"));
        doc2.appendChild(ps);
       
        String chargeAccount = String.valueOf(eq3Posting.getChargeAccount());
        ps = document.createElement("ns2:ChargeAccount");
        ps.appendChild(document.createTextNode(chargeAccount.trim()));
        root.appendChild(ps);
       


        String expdate = String.valueOf(eq3Posting.getExp_date());
        ps = document.createElement("ns2:ExpiryDate");
        ps.appendChild(document.createTextNode(expdate.trim()));
        root.appendChild(ps);
       
        ps = document.createElement("ns2:ProductType");
        ps.appendChild(document.createTextNode("MBI"));
        root.appendChild(ps);
       
        Element extradate = document.createElement("ns2:ExtraData");
        root.appendChild(extradate);
       

        String lob_code = String.valueOf(eq3Posting.getLob_code());
        ps = document.createElement("ns4:LOB");
        ps.appendChild(document.createTextNode(lob_code.trim()));
        extradate.appendChild(ps);
       
        String invo = String.valueOf(eq3Posting.getInvoiceno());
        ps = document.createElement("ns4:INVOICNO");
        ps.appendChild(document.createTextNode(invo.trim()));
        extradate.appendChild(ps);
       
        String portdes = String.valueOf(eq3Posting.getPortofdestination());
        ps = document.createElement("ns4:PORTDES");
        ps.appendChild(document.createTextNode(portdes.trim()));
        extradate.appendChild(ps);
       
        String transdoc = String.valueOf(eq3Posting.getTransportdocno());
        ps = document.createElement("ns4:TRANSDOC");
        ps.appendChild(document.createTextNode(transdoc.trim()));
        extradate.appendChild(ps);
       
        String invdates = String.valueOf(eq3Posting.getInvoicedate());
        ps = document.createElement("ns4:INVNODT");
        ps.appendChild(document.createTextNode(invdates.trim()));
        extradate.appendChild(ps);
       
        String transdocdat = String.valueOf(eq3Posting.getTransportdocdate());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdf.setLenient(false);
        Date dates = sdf.parse(transdocdat);
       
        ps = document.createElement("ns4:TRADOCDT");
        ps.appendChild(document.createTextNode(new SimpleDateFormat("yyyy-MM-dd").format(dates)));
        extradate.appendChild(ps);
       
        ps = document.createElement("ns4:FORCDEBT");
        ps.appendChild(document.createTextNode("Y"));
        extradate.appendChild(ps);
       


        ps = document.createElement("ns4:OPURPOS_Name");
        ps.appendChild(document.createTextNode("P0102"));
        extradate.appendChild(ps);
       
        String hcode = String.valueOf(eq3Posting.getHscode());
        ps = document.createElement("ns4:HMON");
        ps.appendChild(document.createTextNode(hcode.trim()));
        extradate.appendChild(ps);
       


        String rate_in_usd = String.valueOf(eq3Posting.getRate_in_usd());
        ps = document.createElement("ns4:CRAMT_Name");
        ps.appendChild(document.createTextNode(rate_in_usd.trim()));
        extradate.appendChild(ps);
       

        String shipmentfrom = String.valueOf(eq3Posting.getShipmentfromcountry());
        ps = document.createElement("ns4:SHIPFTO_Name");
        ps.appendChild(document.createTextNode(shipmentfrom.trim()));
        extradate.appendChild(ps);
       
        String shipmentto = String.valueOf(eq3Posting.getShipmenttocountry());
        ps = document.createElement("ns4:SHIPTOP_Name");
        ps.appendChild(document.createTextNode(shipmentto.trim()));
        extradate.appendChild(ps);
       
        String formtypee = String.valueOf(eq3Posting.getForm_type());
        ps = document.createElement("ns4:FORTYP");
        ps.appendChild(document.createTextNode(formtypee.trim()));
        extradate.appendChild(ps);
       


        String advance_collectionamt = String.valueOf(eq3Posting.getAdvance_collection_amt());
        logger.info("XML----------------eq3Posting.getAdvance_collection_amt()----------------->" + eq3Posting.getAdvance_collection_amt());
       
        String collection_currnecy = String.valueOf(eq3Posting.getCollectioncurrency());
        String remittance_currency = String.valueOf(eq3Posting.getRemittance_currency());
       

        String currency = null;
        if (remittance_currency != null) {
          currency = remittance_currency;
        } else {
          currency = collection_currnecy;
        }
        ps = document.createElement("ns4:ADVREC");
        ps.appendChild(document.createTextNode(advance_collectionamt.trim() + currency));
        extradate.appendChild(ps);
       

        ps = document.createElement("ns4:PERADV");
        ps.appendChild(document.createTextNode("Full"));
        extradate.appendChild(ps);
       
        String inr_amt = String.valueOf(eq3Posting.getInr_quivalent());
        ps = document.createElement("ns4:INRAMT");
        ps.appendChild(document.createTextNode(inr_amt.trim() + "INR"));
        extradate.appendChild(ps);
       
        String usd_amt = String.valueOf(eq3Posting.getUsd_quivalent_amt());
        ps = document.createElement("ns4:USDAMT");
        ps.appendChild(document.createTextNode(usd_amt.trim() + "USD"));
        extradate.appendChild(ps);
       

        String inco = String.valueOf(eq3Posting.getIncoterms());
        ps = document.createElement("ns4:INCOTER");
        ps.appendChild(document.createTextNode(inco.trim()));
        extradate.appendChild(ps);
       
        String portlod = String.valueOf(eq3Posting.getPortofloading());
        ps = document.createElement("ns4:PORLOD");
        ps.appendChild(document.createTextNode(portlod.trim()));
        extradate.appendChild(ps);
       
        String amount_tobe_collected = String.valueOf(eq3Posting.getAmt_tobe_collected());
        logger.info("===--------------eq3Posting.getAmt_tobe_collected()-----------------------------" + eq3Posting.getAmt_tobe_collected());
        ps = document.createElement("ns4:NETRECIV");
        ps.appendChild(document.createTextNode(amount_tobe_collected.trim() + currency));
        extradate.appendChild(ps);
       
        String portofDestinationDescription = String.valueOf(eq3Posting.getPortofDestinationDescription());
        ps = document.createElement("ns4:PODESCP");
        ps.appendChild(document.createTextNode(portofDestinationDescription.trim()));
        extradate.appendChild(ps);
       
        String portofDestinationCountry = String.valueOf(eq3Posting.getPortofDestinationCountry());
        ps = document.createElement("ns4:PODESCON");
        ps.appendChild(document.createTextNode(portofDestinationCountry.trim()));
        extradate.appendChild(ps);
       
        String portofLoadingCountry = String.valueOf(eq3Posting.getPortofloading_country());
        ps = document.createElement("ns4:POLDCON");
        ps.appendChild(document.createTextNode(portofLoadingCountry.trim()));
        extradate.appendChild(ps);
       
        String portofLoadingDescription = String.valueOf(eq3Posting.getProtofloading_description());
        ps = document.createElement("ns4:POLOADES");
        ps.appendChild(document.createTextNode(portofLoadingDescription.trim()));
        extradate.appendChild(ps);
        if ((formtyp.equalsIgnoreCase("EDI")) || (formtyp.equalsIgnoreCase("SOFTEX")) || (formtyp.equalsIgnoreCase("EDF")))
        {
          double notional_rate = Double.valueOf(eq3Posting.getNotional_rate()).doubleValue();
          String currency_temp = eq3Posting.getRemittance_currency();
          String rm_currency = null;
          if (currency_temp == null) {
            currency_temp = eq3Posting.getCollectioncurrency();
          }
          String shipColQueryEDI = "SELECT DISTINCT NVL(SHIP.IECODE, ' ') IECODE,NVL(EBS.SHP_AMT, '') SHP_AMT,NVL(EBS.SHPBILL_NO,' ')SHPBILL_NO,NVL(STG.REP_AMOUNT, '') REPAMT_WRT_AMT,NVL(EBS.SHORTCOLLECTIONAMOUNT,'') SHORTCOLLECTIONAMOUNT,TO_CHAR(TO_DATE(SHIPBILLDATE,'DD-MM-YY'),'YYYY-MM-DD')SHIPBILLDATE,NVL(SHIP.SHIPBILLNO,' ') SHIPBILLNO,NVL(SHIP.SHIPBILLDATE, ' ') SHIPBILLDATE, NVL(EBS.FORM_NO,' ')FORM_NO,NVL(EBS.PORT_CODE, ' ')PORT_CODE,NVL(EBS.SHP_CURRENCY, ' ')SHP_CURRENCY,NVL(EBS.SHP_AMT, ' ')SHP_AMT,'" +
            currency_temp + "' AS RM_CURRENCY,  CASE WHEN EBS.SHORTCOLLECTIONAMOUNT IS NULL THEN ' '  ELSE '4' END SHORT_REASON, ('" + notional_rate + "' ) AS NOTIONAL_RATE, (STG.REP_AMOUNT + NVL(EBS.SHORTCOLLECTIONAMOUNT, 0)) * +( '" + notional_rate + "' ) EQUIL_BILL_AMT," +
            " EBS.SHP_AMT     - ((STG.REP_AMOUNT*'" + notional_rate + "') + NVL(EBS.SHORTCOLLECTIONAMOUNT, 0)) SHIP_OUTS_AMT,EBS.STATUS STATUS\tFROM ETT_EDPMS_SHP SHIP, ETTDM_BULKUPLOAD_STAGING EBS, (SELECT EBS.SHPBILL_NO,EBS.BILL_DATE,EBS.BATCH_NO ,EBS.THEIRREF,EBS.STATUS,EBS.PORT_CODE,SUM(EBS.REPAMT_WRT_AMT) REP_AMOUNT  FROM (SELECT DISTINCT EBS1.SHPBILL_NO, EBS1.BILL_DATE," +
            " EBS1.BATCH_NO ,EBS1.THEIRREF,EBS1.STATUS,EBS1.PORT_CODE,EBS1.REPAMT_WRT_AMT  FROM ETTDM_BULKUPLOAD_STAGING EBS1) EBS  GROUP BY EBS.SHPBILL_NO,EBS.BILL_DATE,EBS.PORT_CODE, EBS.BATCH_NO ,EBS.THEIRREF,EBS.STATUS ) stg WHERE stg.STATUS  =EBS.STATUS AND EBS.STATUS =stg.STATUS \tAND STG.PORT_CODE  =EBS.PORT_CODE AND STG.BILL_DATE =EBS.BILL_DATE AND stg.THEIRREF  =EBS.THEIRREF " +
            " AND SHIP.SHIPBILLNO   = EBS.SHPBILL_NO AND SHIP.PORTCODE  =EBS.PORT_CODE AND to_date(SHIP.SHIPBILLDATE,'DDMMYYYY') = to_date(EBS.BILL_DATE,'DD-MM-YYYY') AND stg.SHPBILL_NO=EBS.SHPBILL_NO AND EBS.STATUS ='SUCCESS' AND EBS.BATCH_NO  ='" + batch + "' AND EBS.THEIRREF ='" + theirref + "'";
         

          String shipColQuery_Softex = "SELECT DISTINCT NVL(SHIP.IECODE, ' ') IECODE,NVL(EBS.SHP_AMT,0)SHP_AMT,NVL(EBS.SHP_CURRENCY,' ')SHP_CURRENCY,TO_CHAR(TO_DATE(SHIPBILLDATE,'DD-MM-YYYY'),'YYYY-MM-DD') SHIPBILLDATE,NVL(EBS.FORM_NO, ' ')FORM_NO, NVL(EBS.PORT_CODE,' ')PORT_CODE,NVL(EBS.SHP_AMT, 0)SHP_AMT,NVL(EBS.SHPBILL_NO,' ')SHPBILL_NO,NVL(STG.REP_AMOUNT,0) REPAMT_WRT_AMT,NVL (EBS.SHORTCOLLECTIONAMOUNT,0) SHORTCOLLECTIONAMOUNT,('" + currency_temp + "') AS RM_CURRENCY,CASE " +
            " WHEN EBS.SHORTCOLLECTIONAMOUNT IS NULL  THEN '0' ELSE '4' END SHORT_REASON,('" + notional_rate + "') NOTIONAL_RATE,((STG.REP_AMOUNT + NVL(EBS.SHORTCOLLECTIONAMOUNT, 0)) * ('" + notional_rate + "')) EQUIL_BILL_AMT,EBS.SHP_AMT      - ((STG.REP_AMOUNT* ('" + notional_rate + "') + NVL(EBS.SHORTCOLLECTIONAMOUNT, 0))) SHIP_OUTS_AMT,EBS.STATUS STATUS,EBS.BATCH_NO AS BATCH_NO FROM ETT_EDPMS_SHP_SOFTEX SHIP,ETTDM_BULKUPLOAD_STAGING EBS,(SELECT EBS.FORM_NO,EBS.BILL_DATE,EBS.BATCH_NO," +
            " EBS.THEIRREF,EBS.STATUS,EBS.PORT_CODE,SUM(EBS.REPAMT_WRT_AMT) REP_AMOUNT FROM (SELECT DISTINCT EBS1.FORM_NO, EBS1.BILL_DATE,EBS1.BATCH_NO ,EBS1.THEIRREF,EBS1.STATUS,EBS1.PORT_CODE,EBS1.REPAMT_WRT_AMT FROM ETTDM_BULKUPLOAD_STAGING EBS1)EBS GROUP BY EBS.FORM_NO,EBS.BILL_DATE,EBS.PORT_CODE,EBS.BATCH_NO ,EBS.THEIRREF,EBS.STATUS ) stg WHERE stg.STATUS  =EBS.STATUS AND EBS.STATUS   =stg.STATUS AND STG.FORM_NO =EBS.FORM_NO AND STG.PORT_CODE =EBS.PORT_CODE " +
            " AND STG.BILL_DATE  =EBS.BILL_DATE AND stg.THEIRREF  =EBS.THEIRREF AND SHIP.PORTCODE= EBS.PORT_CODE AND TO_CHAR(to_date(SHIP.SHIPBILLDATE,'DD-MM-YYYY'),'DD/MM/YYYY') =TO_CHAR(to_date(EBS.BILL_DATE,'DD-MM-YYYY'),'DD/MM/YYYY')\tAND SHIP.FORMNO  = EBS.FORM_NO \tAND EBS.BATCH_NO   ='" + batch + "' AND EBS.FORM_TYPE   ='" + formtyp + "' AND EBS.STATUS   ='SUCCESS' AND EBS.THEIRREF  ='" + theirref + "' ";
         



          String shipmentinvoice_Edi = "SELECT DISTINCT\tNVL(SHIP.SHIPBILLNO, ' ') SHPBILL_NO,TO_CHAR(TO_DATE(SHIP.SHIPBILLDATE, 'DD-MM-YYYY'), 'YYYY-MM-DD') SHIPBILLDATE,  NVL(SHIP.PORTCODE, ' ') PORTCODE,TO_CHAR(TO_DATE(SHIP.LEODATE, 'DD-MM-YY'), 'YYYY-MM-DD') LEODATE,NVL(SHIP.CUSTNO, ' ') CUSTNO,  NVL(SHIP.FORMNO, ' ') FORMNO,NVL(SHIP.EXPORTAGENCY, ' ') EXPORT_AGENCY,NVL(SHIP.EXPORTTYPE, ' ') EXPORTTYPE, NVL(SHIP.COUNTRYDEST, ' ') COUNTRYDEST,NVL(SHIP.IECODE, ' ') IECODE,\tNVL(SHIP.ADCODE, ' ') ADCODE,NVL(SHIP.RECIND, ' ') RECIND,  EBS.STATUS STATUS,BATCH_NO BATCH_NO FROM\tETT_EDPMS_SHP SHIP,ETTDM_BULKUPLOAD_STAGING EBS  WHERE SHIP.SHIPBILLNO = EBS.SHPBILL_NO\tAND SHIP.PORTCODE = EBS.PORT_CODE AND to_date(SHIP.SHIPBILLDATE,'DDMMYYYY') = to_date(EBS.BILL_DATE,'DD-MM-YYYY')  AND EBS.BATCH_NO ='" +
         




            batch + "' AND EBS.THEIRREF = '" + theirref +
            "' AND STATUS ='SUCCESS' AND  EBS.FORM_TYPE='" + formtyp + "' ";
         
          String shipmentinvoice_softex = " SELECT DISTINCT NVL(SHPBILL_NO,' ')SHPBILL_NO,TO_CHAR(TO_DATE(SHIP.SHIPBILLDATE, 'DD-MM-YYYY'), 'YYYY-MM-DD') SHIPBILLDATE,NVL(SHIP.PORTCODE, ' ') PORTCODE,TO_CHAR(TO_DATE(SHIP.LEODATE, 'DD-MM-YY'), 'YYYY-MM-DD') LEODATE, NVL(SHIP.CUSTNO, ' ') CUSTNO,NVL(SHIP.FORMNO, ' ') FORMNO,NVL(SHIP.EXPORTAGENCY, ' ') EXPORT_AGENCY,NVL(SHIP.EXPORTTYPE, ' ') EXPORTTYPE,  NVL(SHIP.COUNTRYDEST, ' ') COUNTRYDEST,NVL(SHIP.IECODE, ' ') IECODE,NVL(SHIP.ADCODE, ' ') ADCODE,NVL(SHIP.RECIND, ' ') RECIND,EBS.STATUS STATUS,  BATCH_NO BATCH_NO FROM ETT_EDPMS_SHP_SOFTEX SHIP,ETTDM_BULKUPLOAD_STAGING EBS WHERE SHIP.PORTCODE = EBS.PORT_CODE AND to_date(SHIP.SHIPBILLDATE,'DD-MM-YYYY') = to_date(EBS.BILL_DATE,'DD-MM-YYYY')  AND SHIP.FORMNO = EBS.FORM_NO AND EBS.FORM_TYPE = '" +
         


            formtyp +
            "' AND EBS.STATUS ='SUCCESS'  AND BATCH_NO='" + batch + "' AND EBS.THEIRREF='" + theirref +
            "'";
         
          String invoice_Softex = "  SELECT DISTINCT NVL(SHIP.INVSERIALNO, ' ') INVSERIALNO,NVL(SHP_CURRENCY,' ')SHP_CURRENCY,NVL(EBS.FORM_NO,' ')FORM_NO,NVL(SHIP.INVNO, ' ') INVNO,INVDATE,NVL(SHIP.FOBAMT, 0) FOBAMT,NVL(SHIP.FRIEGHTAMT, 0) FRIEGHTAMT,NVL(SHIP.INSAMT, 0) INSAMT, NVL(SHIP.COMMAMT, 0) COMMAMT,NVL(SHIP.DISAMT, 0) DISAMT,NVL(SHIP.DEDAMT, 0) DEDAMT,NVL(SHIP.PKGAMT, 0) PKGAMT,NVL(EBS.PORT_CODE, ' ') PORT_CODE, NVL(EBS.SHPBILL_NO,' ')SHPBILL_NO,NVL(EBS.SHPBILL_NO,' ')SHIPBILLNO FROM ETT_EDPMS_SHP_INV_SOFTEX SHIP, ETTDM_BULKUPLOAD_STAGING EBS WHERE SHIP.PORTCODE = EBS.PORT_CODE AND TO_CHAR(to_date(SHIP.SHIPBILLDATE,'DD-MM-YYYY'),'DD/MM/YYYY') = TO_CHAR(to_date(EBS.BILL_DATE,'DD-MM-YYYY'),'DD/MM/YYYY') AND SHIP.FORMNO = EBS.FORM_NO  AND THEIRREF ='" +
         

            theirref +
            "' AND EBS.FORM_TYPE = 'SOFTEX' AND STATUS  ='SUCCESS' AND EBS.BATCH_NO  ='" + batch +
            "'";
         
          String invoice_Edi = "  SELECT DISTINCT NVL(SHIP.INVSERIALNO, ' ') INVSERIALNO,NVL(SHP_CURRENCY,' ')SHP_CURRENCY, NVL(SHIP.INVNO, ' ') INVNO,NVL(EBS.SHPBILL_NO,' ')SHPBILL_NO,NVL(EBS.PORT_CODE,' ')PORT_CODE, INVDATE, NVL(SHIP.FOBAMT, 0) FOBAMT, NVL(SHIP.FRIEGHTAMT, 0) FRIEGHTAMT,  NVL(SHIP.INSAMT, 0) INSAMT, NVL(SHIP.COMMAMT, 0) COMMAMT, NVL(SHIP.DISAMT, 0) DISAMT, NVL(SHIP.DEDAMT, 0) DEDAMT, NVL(SHIP.PKGAMT, 0) PKGAMT,   NVL(SHIP.PORTCODE, ' ') PORTCODE, NVL(SHIP.SHIPBILLNO, ' ') SHIPBILLNO, NVL(SHIP.FORMNO, ' ') FORM_NO, EBS.STATUS STATUS FROM ETT_EDPMS_SHP_INV SHIP,   ETTDM_BULKUPLOAD_STAGING EBS  WHERE SHIP.SHIPBILLNO = EBS.SHPBILL_NO AND SHIP.PORTCODE =EBS.PORT_CODE AND to_date(SHIP.SHIPBILLDATE,'DD-MM-YYYY') = to_date(EBS.BILL_DATE,'DD-MM-YYYY')  AND EBS.STATUS  ='SUCCESS' AND EBS.THEIRREF ='" +
         


            theirref + "'  AND BATCH_NO = '" +
            batch + "' ";
         
          String shipping_details_edf = " SELECT DISTINCT NVL(EBS.SHPBILL_NO, ' ') SHPBILL_NO,TO_CHAR(TO_DATE(EBS.BILL_DATE, 'DD-MM-YYYY'), 'YYYY-MM-DD') SHIPBILLDATE,NVL(EBS.PORT_CODE, ' ') PORTCODE, TO_CHAR(TO_DATE(EBS.LEO_DATE, 'DD-MM-YY'), 'YYYY-MM-DD') LEODATE,NVL(EBS.CUSTOMS_NUMBER, ' ') CUSTNO,NVL(EBS.FORM_NO, ' ') FORMNO,NVL(EBS.EXPORT_AGENCY, ' ') EXPORT_AGENCY,  NVL(EBS.EXPORT_TYPE, ' ') EXPORTTYPE,NVL(EBS.DESTINATION_COUNTRRY, ' ') COUNTRYDEST,NVL(EBS.IECODE, ' ') IECODE,NVL(EBS.ADCODE, ' ') ADCODE ,  EBS.RECORD_INDICATOR as  RECIND  FROM ETTDM_BULKUPLOAD_STAGING EBS WHERE EBS.BATCH_NO = '" +
         

            batch +
            "' AND EBS.THEIRREF   ='" + theirref + "' AND STATUS ='SUCCESS'";
         
          String inoice_edf = " SELECT DISTINCT NVL(SHP_INV_SNO,'')INVSERIALNO,NVL(EBS.SHP_CURRENCY,' ')SHP_CURRENCY,NVL(EBS.FORM_NO, ' ') FORM_NO,NVL(EBS.SHP_INVOICE_NO, ' ') INVNO,  NVL(EBS.FOB_AMT, 0) FOBAMT,NVL(EBS.FREIGHT_AMT, 0) FRIEGHTAMT,NVL(EBS.INS_AMT, 0) INSAMT,NVL(EBS.COMM_AMT, 0) COMMAMT,NVL(EBS.DISCOUNT_AMT, 0) DISAMT,  NVL(EBS.DEDUCTION_AMT, 0) DEDAMT,NVL(EBS.PACKAGE_AMT, 0) PKGAMT,TO_CHAR(TO_DATE(SHP_INVOICE_DATE,'DD-MM-YYYY'),'YYYY-MM-DD')INVDATE,NVL(EBS.SHPBILL_NO,' ')SHPBILL_NO,NVL(EBS.SHPBILL_NO,' ')SHIPBILLNO,  NVL(EBS.PORT_CODE,' ')PORT_CODE FROM ETTDM_BULKUPLOAD_STAGING EBS WHERE EBS.STATUS ='SUCCESS' AND EBS.THEIRREF ='" +
         


            theirref + "' AND BATCH_NO= '" + batch + "' ";
         
          String collections_edf = "SELECT DISTINCT NVL(EBS.IECODE, ' ') IECODE,NVL(EBS.SHP_AMT, '') SHP_AMT,NVL(EBS.SHPBILL_NO,' ')SHPBILL_NO,NVL(STG.REP_AMOUNT, '') REPAMT_WRT_AMT,NVL(EBS.SHORTCOLLECTIONAMOUNT,'') SHORTCOLLECTIONAMOUNT,TO_CHAR(TO_DATE(EBS.BILL_DATE,'DD-MM-YYYY'),'YYYY-MM-DD')SHIPBILLDATE,NVL(EBS.FORM_NO,' ')FORM_NO,NVL(EBS.PORT_CODE, ' ')PORT_CODE,NVL(EBS.SHP_CURRENCY, ' ')SHP_CURRENCY,NVL(EBS.SHP_AMT, ' ')SHP_AMT, '" +
            currency_temp + "' AS RM_CURRENCY,CASE   WHEN EBS.SHORTCOLLECTIONAMOUNT IS NULL   THEN ' '   ELSE '4' END SHORT_REASON, ('" + notional_rate + "' ) NOTIONAL_RATE,(STG.REP_AMOUNT + NVL(EBS.SHORTCOLLECTIONAMOUNT, 0)) * ('" + notional_rate + "') EQUIL_BILL_AMT,EBS.SHP_AMT - (((STG.REP_AMOUNT*'" + notional_rate + "') + NVL(EBS.SHORTCOLLECTIONAMOUNT, 0))) SHIP_OUTS_AMT,EBS.STATUS STATUS FROM ETTDM_BULKUPLOAD_STAGING EBS," +
            " (SELECT EBS1.SHPBILL_NO,EBS1.BILL_DATE,EBS1.BATCH_NO,EBS1.THEIRREF,EBS1.STATUS,EBS1.PORT_CODE,SUM(REP_AMOUNT) AS REP_AMOUNT FROM (SELECT DISTINCT EBS1.SHPBILL_NO,  EBS1.BILL_DATE,  EBS1.BATCH_NO ,  EBS1.THEIRREF,  EBS1.STATUS,  EBS1.PORT_CODE,  (REPAMT_WRT_AMT) REP_AMOUNT FROM ETTDM_BULKUPLOAD_STAGING EBS1 ) EBS1 GROUP BY EBS1.SHPBILL_NO,   EBS1.BILL_DATE,  EBS1.BATCH_NO ,  EBS1.THEIRREF,  EBS1.STATUS,  EBS1.PORT_CODE) stg" +
            " WHERE stg.STATUS  =EBS.STATUS AND EBS.STATUS    =stg.STATUS AND STG.SHPBILL_NO=EBS.SHPBILL_NO AND STG.PORT_CODE =EBS.PORT_CODE AND STG.BILL_DATE =EBS.BILL_DATE AND stg.THEIRREF  =EBS.THEIRREF AND EBS.STATUS    ='SUCCESS' AND EBS.BATCH_NO  ='" + batch + "' AND EBS.THEIRREF  ='" + theirref + "'";
          if (formtyp.equalsIgnoreCase("EDI"))
          {
            rs1 = stmt1.executeQuery(shipmentinvoice_Edi);
            logger.info("Shipment details Invoice EDI - " + shipmentinvoice_Edi);
           


            rs = stmt.executeQuery(invoice_Edi);
            logger.info("Invoice  details EDI - " + invoice_Edi);
           

            rs2 = stmt2.executeQuery(shipColQueryEDI);
            logger.info("Collection Detail EDI- " + shipColQueryEDI);
          }
          else if (formtyp.equalsIgnoreCase("SOFTEX"))
          {
            rs1 = stmt1.executeQuery(shipmentinvoice_softex);
            logger.info("Shipment Invoice SOFTEX - " + shipmentinvoice_softex);
           
            rs = stmt.executeQuery(invoice_Softex);
            logger.info("Invoice details  SOFTEX - " + invoice_Softex);
           
            rs2 = stmt2.executeQuery(shipColQuery_Softex);
            logger.info("Collection Detail SOFTEX- " + shipColQuery_Softex);
          }
          else if (formtyp.equalsIgnoreCase("EDF"))
          {
            rs1 = stmt1.executeQuery(shipping_details_edf);
            logger.info("EDF Shipping Details" + shipping_details_edf);
            logger.info("EDF Shipping Details" + shipping_details_edf);
            rs = stmt.executeQuery(inoice_edf);
            logger.info("EDF Invoice Details" + inoice_edf);
            logger.info("EDF Invoice Details" + inoice_edf);
            logger.info("EDF Collection Details" + collections_edf);
            rs2 = stmt2.executeQuery(collections_edf);
            logger.info("EDF Collection Details" + collections_edf);
           
            logger.info("EDF Collection Details" + collections_edf);
          }
          while (rs1.next())
          {
            Element ExtEventShippingDetails = document.createElement("ns4:ExtEventShippingdetails");
            extradate.appendChild(ExtEventShippingDetails);
           
            ps = document.createElement("ns4:SDBILLNO");
            ps.appendChild(document.createTextNode(rs1.getString("SHPBILL_NO")));
            ExtEventShippingDetails.appendChild(ps);
           
            String datee = String.valueOf(rs1.getString("SHIPBILLDATE"));
            ps = document.createElement("ns4:SDBILDAT");
            ps.appendChild(document.createTextNode(datee.trim()));
            ExtEventShippingDetails.appendChild(ps);
           
            String portcode = String.valueOf(rs1.getString("PORTCODE"));
            ps = document.createElement("ns4:SDPORTCODE");
            ps.appendChild(document.createTextNode(portcode.trim()));
            ExtEventShippingDetails.appendChild(ps);
           
            String leodate = String.valueOf(rs1.getString("LEODATE"));
            ps = document.createElement("ns4:SDLEODATE");
            ps.appendChild(document.createTextNode(leodate.trim()));
            ExtEventShippingDetails.appendChild(ps);
           
            String custno = String.valueOf(rs1.getString("CUSTNO"));
            ps = document.createElement("ns4:SDCUSTNO");
            ps.appendChild(document.createTextNode(custno.trim()));
            ExtEventShippingDetails.appendChild(ps);
           
            String formno = String.valueOf(rs1.getString("FORMNO"));
            ps = document.createElement("ns4:SDFORMNO");
            ps.appendChild(document.createTextNode(formno.trim()));
            ExtEventShippingDetails.appendChild(ps);
           
            String exportagency = String.valueOf(rs1.getString("EXPORT_AGENCY"));
            ps = document.createElement("ns4:SDEXPAGENC");
            ps.appendChild(document.createTextNode(exportagency.trim()));
            ExtEventShippingDetails.appendChild(ps);
           
            String exporttype = String.valueOf(rs1.getString("EXPORTTYPE"));
            ps = document.createElement("ns4:SDEXPTYPE");
            ps.appendChild(document.createTextNode(exporttype.trim()));
            ExtEventShippingDetails.appendChild(ps);
           
            String destinationcountry = String.valueOf(rs1.getString("COUNTRYDEST"));
            ps = document.createElement("ns4:SDDESTCOUN");
            ps.appendChild(document.createTextNode(destinationcountry.trim()));
            ExtEventShippingDetails.appendChild(ps);
           
            String iecode = String.valueOf(rs1.getString("IECODE"));
            ps = document.createElement("ns4:SDIECODE");
            ps.appendChild(document.createTextNode(iecode.trim()));
            ExtEventShippingDetails.appendChild(ps);
           
            String adcode = String.valueOf(rs1.getString("ADCODE"));
            ps = document.createElement("ns4:SDADCODE");
            ps.appendChild(document.createTextNode(adcode.trim()));
            ExtEventShippingDetails.appendChild(ps);
           
            String recordindicator = String.valueOf(rs1.getString("RECIND"));
            ps = document.createElement("ns4:CREIND");
            ps.appendChild(document.createTextNode(recordindicator.trim()));
            ExtEventShippingDetails.appendChild(ps);
          }
          DBConnectionUtility.surrenderDB(null, stmt1, rs1);
          while (rs.next())
          {
            Element ExtEventInvoiceCollections = document.createElement("ns4:ExtEventInvoiceDetails");
            extradate.appendChild(ExtEventInvoiceCollections);
           
            String inserialno = String.valueOf(rs.getString("INVSERIALNO"));
            ps = document.createElement("ns4:INVSRNO");
            ps.appendChild(document.createTextNode(inserialno.trim()));
            ExtEventInvoiceCollections.appendChild(ps);
           
            String invoiceno = String.valueOf(rs.getString("INVNO"));
            ps = document.createElement("ns4:INVNO");
            ps.appendChild(document.createTextNode(invoiceno.trim()));
            ExtEventInvoiceCollections.appendChild(ps);
           
            String invDate = rs.getString("INVDATE");
            if (invDate != null)
            {
              SimpleDateFormat sdfSource = new SimpleDateFormat("dd-MM-yy");
              SimpleDateFormat sdfDestination = new SimpleDateFormat("yyyy-MM-dd");
              Date date = sdfSource.parse(invDate);
              invDate = sdfDestination.format(date);
             
              String invdate = String.valueOf(invDate);
              ps = document.createElement("ns4:INVDATE");
              ps.appendChild(document.createTextNode(invdate.trim()));
              ExtEventInvoiceCollections.appendChild(ps);
            }
            String invfobamt = String.valueOf(rs.getString("FOBAMT"));
            String invfobCCY = String.valueOf(rs.getString("SHP_CURRENCY"));
           
            ps = document.createElement("ns4:IFOBAMT");
            ps.appendChild(document.createTextNode(invfobamt + " " + invfobCCY));
            ExtEventInvoiceCollections.appendChild(ps);
           
            String invfrgamt = String.valueOf(rs.getString("FRIEGHTAMT"));
            String invfrgCCY = String.valueOf(rs.getString("SHP_CURRENCY"));
           
            ps = document.createElement("ns4:INVFRAMT");
            ps.appendChild(document.createTextNode(invfrgamt + " " + invfrgCCY));
            ExtEventInvoiceCollections.appendChild(ps);
           

            String insamt = String.valueOf(rs.getString("INSAMT"));
            String insCCY = String.valueOf(rs.getString("SHP_CURRENCY"));
           
            ps = document.createElement("ns4:INSUAMT");
            ps.appendChild(document.createTextNode(insamt + " " + insCCY));
            ExtEventInvoiceCollections.appendChild(ps);
           

            String commamt = String.valueOf(rs.getString("COMMAMT"));
            String commCCY = String.valueOf(rs.getString("SHP_CURRENCY"));
           
            ps = document.createElement("ns4:ICOMMAMT");
            ps.appendChild(document.createTextNode(commamt + " " + commCCY));
            ExtEventInvoiceCollections.appendChild(ps);
           
            String disamt = String.valueOf(rs.getString("DISAMT"));
            String disCCY = String.valueOf(rs.getString("SHP_CURRENCY"));
           
            ps = document.createElement("ns4:IDISCAMT");
            ps.appendChild(document.createTextNode(disamt + " " + disCCY));
            ExtEventInvoiceCollections.appendChild(ps);
           
            String dedamt = String.valueOf(rs.getString("DEDAMT"));
            String dedCCY = String.valueOf(rs.getString("SHP_CURRENCY"));
           
            ps = document.createElement("ns4:IDEDUAMT");
            ps.appendChild(document.createTextNode(dedamt + " " + dedCCY));
            ExtEventInvoiceCollections.appendChild(ps);
           

            String pkgamt = String.valueOf(rs.getString("PKGAMT"));
            String pkgCCY = String.valueOf(rs.getString("SHP_CURRENCY"));
           
            ps = document.createElement("ns4:IPKGAMT");
            ps.appendChild(document.createTextNode(pkgamt + " " + pkgCCY));
            ExtEventInvoiceCollections.appendChild(ps);
           
            ps = document.createElement("ns4:INVPRTCD");
            ps.appendChild(document.createTextNode(rs.getString("PORT_CODE")));
            ExtEventInvoiceCollections.appendChild(ps);
           
            ps = document.createElement("ns4:ISHPBILL");
            ps.appendChild(document.createTextNode(rs.getString("SHIPBILLNO")));
            ExtEventInvoiceCollections.appendChild(ps);
           

            ps = document.createElement("ns4:IFORNO");
            ps.appendChild(document.createTextNode(rs.getString("FORM_NO")));
            ExtEventInvoiceCollections.appendChild(ps);
          }
          DBConnectionUtility.surrenderDB(null, stmt, rs);
          while (rs2.next())
          {
            Element ExtEventShippingCollections = document.createElement("ns4:ExtEventShippingCollections");
            extradate.appendChild(ExtEventShippingCollections);
           
            logger.info("Inside Shipping Collection");
            String shipbilno = String.valueOf(rs2.getString("SHPBILL_NO"));
            ps = document.createElement("ns4:CBILLNUM");
            ps.appendChild(document.createTextNode(shipbilno.trim()));
            ExtEventShippingCollections.appendChild(ps);
           
            String cbillda = String.valueOf(rs2.getString("SHIPBILLDATE"));
            ps = document.createElement("ns4:CBILLDA");
            ps.appendChild(document.createTextNode(cbillda.trim()));
            ExtEventShippingCollections.appendChild(ps);
           
            String formno = String.valueOf(rs2.getString("FORM_NO"));
            ps = document.createElement("ns4:CFORMN");
            ps.appendChild(document.createTextNode(formno.trim()));
            ExtEventShippingCollections.appendChild(ps);
           
            String portcode = String.valueOf(rs2.getString("PORT_CODE"));
            ps = document.createElement("ns4:CPORTCO");
            ps.appendChild(document.createTextNode(portcode.trim()));
            ExtEventShippingCollections.appendChild(ps);
           
            String shipbillamt =
              String.valueOf(rs2.getString("SHP_AMT") + " " + rs2.getString("SHP_CURRENCY"));
            ps = document.createElement("ns4:CSHPAMT");
            ps.appendChild(document.createTextNode(shipbillamt.trim()));
            ExtEventShippingCollections.appendChild(ps);
           


            String repayamt = String.valueOf(rs2.getString("REPAMT_WRT_AMT"));
           
            double rep_amt = Double.valueOf(repayamt).doubleValue();
           
            String abc = String.valueOf(rep_amt + " " + rs2.getString("RM_CURRENCY"));
           
            ps = document.createElement("ns4:CREPAY");
            ps.appendChild(document.createTextNode(abc.trim()));
            ExtEventShippingCollections.appendChild(ps);
           
            String shortcollectionamt =
              String.valueOf(rs2.getString("SHORTCOLLECTIONAMOUNT") + " " + rs2.getString("SHP_CURRENCY"));
            ps = document.createElement("ns4:CSHCOLAM");
            ps.appendChild(document.createTextNode(shortcollectionamt.trim()));
            ExtEventShippingCollections.appendChild(ps);
           
            String shortReason = rs2.getString("SHORT_REASON");
            ps = document.createElement("ns4:CRSNASHF");
            ps.appendChild(document.createTextNode(shortReason.trim()));
            ExtEventShippingCollections.appendChild(ps);
           
            String notionalRate = rs2.getString("NOTIONAL_RATE");
            ps = document.createElement("ns4:CNOTIONL");
            ps.appendChild(document.createTextNode(notionalRate.trim()));
            ExtEventShippingCollections.appendChild(ps);
           
            String equilBillAmt =
              String.valueOf(rs2.getString("EQUIL_BILL_AMT") + " " + rs2.getString("SHP_CURRENCY"));
            ps = document.createElement("ns4:CEQUBILL");
            ps.appendChild(document.createTextNode(equilBillAmt.trim()));
            ExtEventShippingCollections.appendChild(ps);
           
            String shipOutsAmt =
              String.valueOf(rs2.getString("SHIP_OUTS_AMT") + " " + rs2.getString("SHP_CURRENCY"));
            ps = document.createElement("ns4:COUTSAMT");
            ps.appendChild(document.createTextNode(shipOutsAmt.trim()));
            ExtEventShippingCollections.appendChild(ps);
           
            String iecode = rs2.getString("IECODE");
            ps = document.createElement("ns4:CIECOD");
            ps.appendChild(document.createTextNode(iecode.trim()));
            ExtEventShippingCollections.appendChild(ps);
          }
          DBConnectionUtility.surrenderDB(null, stmt2, rs2);
        }
        if ((eq3Posting.getForm_type().equalsIgnoreCase("EDI")) ||
          (eq3Posting.getForm_type().equalsIgnoreCase("EDF")) ||
          (eq3Posting.getForm_type().equalsIgnoreCase("SOFTEX")) ||
          (eq3Posting.getForm_type().equalsIgnoreCase("EXEMPTED")))
        {
          String queryse = "SELECT NVL(REMITTANCE_NUM,'N_R')AS REMITTANCE_NUM,NVL(FIRC_NO,'N_F') AS FIRC_NO FROM ETTDM_BULKUPLOAD_STAGING\tWHERE  \tBATCH_NO= '" +
            batch + "' " + " AND THEIRREF='" + theirref + "' GROUP BY REMITTANCE_NUM,FIRC_NO";
          rs5 = stmt5.executeQuery(queryse);
          while (rs5.next())
          {
            String r_status = rs5.getString("REMITTANCE_NUM").trim();
            String f_status = rs5.getString("FIRC_NO").trim();
           
            logger.info("-----------------------r_status" + r_status);
            logger.info("-----------------------f_status" + f_status);
            if (f_status.equals("N_F"))
            {
              logger.info("----->REMITTANCE_NUM ---");
             








              String advanceQueryWithout_FIRC = "SELECT * FROM (SELECT DISTINCT NVL(extbr.ADCODE,' ') as ADCODE,NVL(mas.MASTER_REF,' ') as REMITTANCE_NUM,NVL(OC_NM_ADD1,' ') as REMITTER_NAME ,TO_CHAR(TO_DATE(cpm.RCV_DATE,'DD-MM-YY'),'YYYY-MM-DD') AS REMITTER_DATE,NVL(exte.REMTRY,' ') as REMITTER_COUNTRY,' '  FIRC_NO,cpm.RCV_AMT/power(10,C82.C8CED) AS AVAILABLE_AMOUNT,NVL(cpm.RCV_CCY,' ') as SHP_CURRENCY,NVL(mas.NPRCUSTMNM,' ') as CIF_NO,NVL(mas.NPRNAME_L1,' ') as Customer_CIF_Name ,NVL(STG.UTILIZATION_AMOUNT,' ') as UTILIZATION_AMOUNT FROM master mas,  ETTDM_BULKUPLOAD_STAGING STG,BASEEVENT bev,EXTEVENT exte,CPAYMASTER cpm,EXEMPL30 exe,MSTRSETTLE mst1,EXTBRAMAS extbr,c8pf c82 WHERE mas.KEY97 = bev.MASTER_KEY AND mas.KEY97 = cpm.KEY97  AND mas.STATUS= 'LIV' AND bev.REFNO_PFIX = 'CRE' AND mas.EXEMPLAR = exe.KEY97 AND cpm.PAY_SI = mst1.KEY97 AND exe.CODE79  IN ('CPCI','CPBI','CPHI') AND bev.KEY97= exte.EVENT AND trim(mas.BHALF_BRN) = extbr.BCODE AND trim(cpm.RCV_CCY)= trim(C82.C8CCY) AND mas.MASTER_REF = '" +
             

                r_status + "'  AND STG.THEIRREF ='" + theirref + "' AND STG.BATCH_NO = '" + batch + "'   AND STG.STATUS='SUCCESS' AND STG.REMITTANCE_NUM  ='" + r_status + "'" +
                " UNION all SELECT DISTINCT NVL(EXTBR.ADCODE,' ') as  ADCODE,REM.REMIT_NO as REMITTANCE_NUM,REM.REMIT_NAME AS REMITTER_NAME ,TO_CHAR(TO_DATE(REM.REMIT_DATE,'DD-MM-YY'),'YYYY-MM-DD') AS REMITTER_DATE, REM.REMIT_COUNTRY as REMITTER_COUNTRY,' '  FIRC_NO,STG.AVAILABLE_AMOUNT/power(10,C82.C8CED) AS AVAILABLE_AMOUNT,NVL(REM.REMIT_CURR,' ') as SHP_CURRENCY,REM.REMIT_BENE_CIF  as CIF_NO,REM.REMIT_NAME  as  CUSTOMER_CIF_NAME ,NVL(STG.UTILIZATION_AMOUNT,' ') as UTILIZATION_AMOUNT " +
                " FROM ETTDM_BULKUPLOAD_STAGING STG,ETT_INW_REMITTANCE REM, EXTBRAMAS EXTBR, c8pf c82" +
                " WHERE TRIM(REM.REMIT_BRANCH) = EXTBR.BCODE AND C82.C8CCY=REM.REMIT_CURR AND REM.REMIT_NO  ='" + r_status + "' AND STG.THEIRREF ='" + theirref + "'  AND STG.BATCH_NO  ='" + batch + "' AND STG.STATUS='SUCCESS' AND STG.REMITTANCE_NUM  =  '" + r_status + "' )  ORDER BY REMITTANCE_NUM";
             
              logger.info(remitance + "Remittance Without FIRC" + advanceQueryWithout_FIRC);
             
              rs4 = stmt4.executeQuery(advanceQueryWithout_FIRC);
              if (rs4.next())
              {
                double stg_remittance_amt_1 = 0.0D;
                double ti_remittance_amts = 0.0D;
                double balance_remt_amt = 0.0D;
                try
                {
                  Statement stmt9 = con.createStatement();
                  Statement stmt10 = con.createStatement();
                  ResultSet rs9 = null;
                  ResultSet rs10 = null;
                 
                  logger.info("R-status------------------>" + r_status);
                  String ti_remittance_amt = "SELECT ext.ccy_1,NVL(SUM(THEME_GENIUS_PKG.CONVAMT( ext.ccy_1, ext.amtutil) ),0) AS TOTAL_AMT FROM exteventadv ext, BASEEVENT bev,MASTER mas WHERE ext.FK_EVENT = bev.EXTFIELD AND mas.KEY97 = bev.MASTER_KEY AND bev.STATUS    IN ('i','c') AND ext.inward ='" +
                    r_status + "' GROUP BY ext.ccy_1 ";
                 
                  logger.info("remittance---------ti----------" + ti_remittance_amt);
                  rs10 = stmt10.executeQuery(ti_remittance_amt);
                  while (rs10.next()) {
                    ti_remittance_amts += Double.valueOf(rs10.getString("TOTAL_AMT")).doubleValue();
                  }
                  DBConnectionUtility.surrenderDB(null, stmt10, rs10);
                 

                  double original_remittance = 0.0D;
                  String original_remittance_amt = "SELECT REMITTANCE_AMOUNT FROM ETTV_REMITANCE_AMT WHERE REMITTANCE_NUM='" + r_status + "'";
                  rs9 = stmt9.executeQuery(original_remittance_amt);
                  logger.info("-----------------------------Original Remittance--------------->" + original_remittance);
                  while (rs9.next()) {
                    original_remittance = Double.valueOf(rs9.getString("REMITTANCE_AMOUNT")).doubleValue();
                  }
                  DBConnectionUtility.surrenderDB(null, stmt9, rs9);
                 












                  balance_remt_amt = original_remittance - ti_remittance_amts;
                  logger.info("original_remittance----------------->" + original_remittance);
                  logger.info("ti_remittance_amts----------------->" + ti_remittance_amts);
                  logger.info("stg_remittance_amt_1----------------->" + stg_remittance_amt_1);
                  logger.info("balance_remt_amt----------------->" + balance_remt_amt);
                }
                catch (Exception e)
                {
                  logger.info("Remittance Exception---------->" + e.getMessage());
                }
                logger.info("ExtEventAdvanceTable");
                Element ExtEventAdvanceTable = document.createElement("ns4:ExtEventAdvanceTable");
                extradate.appendChild(ExtEventAdvanceTable);
               
                String inwardremitancenumber = String.valueOf(rs4.getString("REMITTANCE_NUM"));
                ps = document.createElement("ns4:INWARD");
                ps.appendChild(document.createTextNode(inwardremitancenumber.trim()));
                ExtEventAdvanceTable.appendChild(ps);
               
                String firc_Number = String.valueOf(rs4.getString("FIRC_NO")).trim();
                logger.info("firc_Number---" + firc_Number);
                ps = document.createElement("ns4:FINUMB");
                if (firc_Number != null)
                {
                  ps.appendChild(document.createTextNode(firc_Number.trim()));
                  ExtEventAdvanceTable.appendChild(ps);
                }
                String remittername = String.valueOf(rs4.getString("REMITTER_NAME"));
                ps = document.createElement("ns4:NAMREM");
                ps.appendChild(document.createTextNode(remittername.trim()));
                ExtEventAdvanceTable.appendChild(ps);
               
                String remittancedate = String.valueOf(rs4.getString("REMITTER_DATE"));
                ps = document.createElement("ns4:DATREM");
                ps.appendChild(document.createTextNode(remittancedate.trim()));
                ExtEventAdvanceTable.appendChild(ps);
               
                String remittercountry = String.valueOf(rs4.getString("REMITTER_COUNTRY"));
                ps = document.createElement("ns4:COUNREM");
                ps.appendChild(document.createTextNode(remittercountry.trim()));
                ExtEventAdvanceTable.appendChild(ps);
               
                String utilizationccy = String.valueOf(rs4.getString("SHP_CURRENCY"));
               






                String available_amount = String.valueOf(balance_remt_amt);
                ps = document.createElement("ns4:BALANCE");
                ps.appendChild(document.createTextNode(available_amount + " " + utilizationccy));
                ExtEventAdvanceTable.appendChild(ps);
               





                String utliizationamt = String.valueOf(CommonMethods.getUtilizationSum(r_status, theirref, batch));
                logger.info("Final Utilization amount----->" + utliizationamt);
                ps = document.createElement("ns4:AMTUTIL");
                ps.appendChild(document.createTextNode(utliizationamt + " " + utilizationccy));
                ExtEventAdvanceTable.appendChild(ps);
               
                String remadcode = String.valueOf(rs4.getString("ADCODE"));
                ps = document.createElement("ns4:ADVRECB");
                ps.appendChild(document.createTextNode(remadcode.trim()));
                ExtEventAdvanceTable.appendChild(ps);
               
                String customercifno = String.valueOf(rs4.getString("CIF_NO"));
                ps = document.createElement("ns4:CUSCIFNO");
                ps.appendChild(document.createTextNode(customercifno.trim()));
                ExtEventAdvanceTable.appendChild(ps);
              }
            }
            else if (r_status.equals("N_R"))
            {
              logger.info("----->FIRC_NO ---");
             
              String advanceQueryManualFirc = "  SELECT NVL(STG.REMITTANCE_ADCODE, ' ') ADCODE,NVL(AVAILABLE_AMOUNT,' ')AVAILABLE_AMOUNT,NVL(STG.REMITTANCE_NUM, ' ') REMITTANCE_NUM,NVL(STG.REMITTER_NAME, ' ') REMITTER_NAME,NVL(STG.FIRC_NO,'') FIRC_NO,   TO_CHAR(TO_DATE(STG.REMITTER_DATE,'DD-MM-YYYY'), 'YYYY-MM-DD') REMITTER_DATE,NVL(STG.REMITTER_COUNTRY, ' ') REMITTER_COUNTRY,NVL(STG.AVAILABLE_AMOUNT,'')AVAILABLE_AMOUNT,   NVL(STG.SHP_CURRENCY,'')SHP_CURRENCY,  NVL(STG.UTILIZATION_AMOUNT,' ') UTILIZATION_AMOUNT,NVL(STG.CIF_NUMBER, ' ') CIF_NO  FROM ETTDM_BULKUPLOAD_STAGING STG  WHERE STG.THEIRREF ='" +
             

                theirref + "'  " + "  AND STG.BATCH_NO ='" + batch + "'  AND FIRC_NO='" + f_status +
                "' ";
             
              rs6 = stmt6.executeQuery(advanceQueryManualFirc);
              logger.info("Remtiance With FIRC" + advanceQueryManualFirc);
              if (rs6.next())
              {
                logger.info("ExtEventAdvanceTable");
                Element ExtEventAdvanceTable = document.createElement("ns4:ExtEventAdvanceTable");
                extradate.appendChild(ExtEventAdvanceTable);
               
                String inwardremitancenumber = String.valueOf(rs6.getString("REMITTANCE_NUM"));
                ps = document.createElement("ns4:INWARD");
                ps.appendChild(document.createTextNode(inwardremitancenumber.trim()));
                ExtEventAdvanceTable.appendChild(ps);
               
                String firc_Number = String.valueOf(rs6.getString("FIRC_NO")).trim();
                logger.info("firc_Number---" + firc_Number);
                ps = document.createElement("ns4:FINUMB");
                if (firc_Number != null)
                {
                  ps.appendChild(document.createTextNode(firc_Number));
                  ExtEventAdvanceTable.appendChild(ps);
                }
                String remittername = String.valueOf(rs6.getString("REMITTER_NAME"));
                ps = document.createElement("ns4:NAMREM");
                ps.appendChild(document.createTextNode(remittername.trim()));
                ExtEventAdvanceTable.appendChild(ps);
               
                String remittancedate = String.valueOf(rs6.getString("REMITTER_DATE"));
                ps = document.createElement("ns4:DATREM");
                ps.appendChild(document.createTextNode(remittancedate.trim()));
                ExtEventAdvanceTable.appendChild(ps);
               
                String remittercountry = String.valueOf(rs6.getString("REMITTER_COUNTRY"));
                ps = document.createElement("ns4:COUNREM");
                ps.appendChild(document.createTextNode(remittercountry.trim()));
                ExtEventAdvanceTable.appendChild(ps);
               
                String utliizationamt = String.valueOf(rs6.getString("UTILIZATION_AMOUNT"));
                String utilizationccy = String.valueOf(rs6.getString("SHP_CURRENCY"));
               
                String available_amt = String.valueOf(rs6.getString("AVAILABLE_AMOUNT"));
               
                ps = document.createElement("ns4:BALANCE");
                ps.appendChild(document.createTextNode(available_amt + " " + utilizationccy));
                ExtEventAdvanceTable.appendChild(ps);
               
                ps = document.createElement("ns4:AMTUTIL");
                ps.appendChild(document.createTextNode(utliizationamt + " " + utilizationccy));
                ExtEventAdvanceTable.appendChild(ps);
               
                String remadcode = String.valueOf(rs6.getString("ADCODE"));
                ps = document.createElement("ns4:ADVRECB");
                ps.appendChild(document.createTextNode(remadcode.trim()));
                ExtEventAdvanceTable.appendChild(ps);
               
                String customercifno = String.valueOf(rs6.getString("CIF_NO"));
                ps = document.createElement("ns4:CUSCIFNO");
                ps.appendChild(document.createTextNode(customercifno.trim()));
                ExtEventAdvanceTable.appendChild(ps);
              }
            }
          }
        }
        DBConnectionUtility.surrenderDB(null, stmt6, rs6);
        DBConnectionUtility.surrenderDB(null, stmt4, rs4);
      }
      str1 = convertDocumentToString(document);
    }
    catch (SQLException e)
    {
      logger.info("Exception in generatecreatexml - " + e.getMessage());
      e.printStackTrace();
    }
    return str1;
  }
 
  private static String convertDocumentToString(Document doc)
  {
    TransformerFactory tf = TransformerFactory.newInstance();
    try
    {
      Transformer transformer = tf.newTransformer();
     

      transformer.setOutputProperty("omit-xml-declaration", "yes");
     
      StringWriter writer = new StringWriter();
     
      transformer.transform(new DOMSource(doc), new StreamResult(writer));
     
      return writer.getBuffer().toString();
    }
    catch (TransformerException e)
    {
      e.printStackTrace();
    }
    return null;
  }
 
  public static double getRemittanceBalance(double remittanceAmount, double utilizationAmount)
  {
    BigDecimal remitAmT = new BigDecimal(remittanceAmount).setScale(1, RoundingMode.HALF_EVEN);
    BigDecimal utilAmT = new BigDecimal(utilizationAmount).setScale(1, RoundingMode.HALF_EVEN);
   

    BigDecimal subtractedValue = remitAmT.subtract(utilAmT);
    logger.info("subtractedValue-------->" + subtractedValue);
   

    return subtractedValue.doubleValue();
  }
}
