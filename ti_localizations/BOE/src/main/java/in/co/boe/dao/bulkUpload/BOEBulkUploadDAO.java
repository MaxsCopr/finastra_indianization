package in.co.boe.dao.bulkUpload;

import in.co.boe.dao.AbstractDAO;
import in.co.boe.dao.exception.DAOException;
import in.co.boe.utility.ActionConstants;
import in.co.boe.utility.ActionConstantsQuery;
import in.co.boe.utility.CommonMethods;
import in.co.boe.utility.DBConnectionUtility;
import in.co.boe.utility.LoggableStatement;
import in.co.boe.utility.ValidationUtility;
import in.co.boe.vo.BOEBulkUploadVO;
import in.co.boe.vo.TransactionBoeBlkVO;
import in.co.boe.vo.TransactionVO;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
 
public class BOEBulkUploadDAO
  extends AbstractDAO
  implements ActionConstantsQuery, ActionConstants
{
  private static Logger logger = Logger.getLogger(BOEBulkUploadDAO.class
    .getName());
  static BOEBulkUploadDAO dao;
  String contentDisposition;
  ByteArrayInputStream excelStream;
  public static BOEBulkUploadDAO getDAO()
  {
    if (dao == null) {
      dao = new BOEBulkUploadDAO();
    }
    return dao;
  }
  public TransactionBoeBlkVO getExcelValidate(BOEBulkUploadVO bulkVO)
    throws DAOException, IOException
  {
    logger.info("Entering Method");
    int i = 1;
    int insertedRowCount = 0;
    String batchId = "";
    HSSFWorkbook workbook = null;
    ValidationUtility validation = new ValidationUtility();
    ArrayList<BOEBulkUploadVO> boeExtList = null;
    ArrayList<String> excelErrorList = new ArrayList();
    TransactionBoeBlkVO trans = null;
    try
    {
      trans = new TransactionBoeBlkVO();
      boeExtList = new ArrayList();
      File filename = bulkVO.getInputFile();
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
          logger.info("rows.hasNext---" + rows.hasNext());
          String paymentRefNo = "";String eventRefNo = "";String paymentAmnt = "";String payAmntCurr = "";String boeNo = "";String boeDate = "";String portDischarge = "";String boeAmnt = "";String boeAmntCurr = "";String boeAmntEndorse = "";String boeAllocAmnt = "";String remarks = "";String invoiceSerNo = "";String changeIeCode = "";String besRecInd = "";String invoiceNo = "";String invRelAmt = "";
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
            bulkVO = new BOEBulkUploadVO();
            logger.info("row---2" + rows);
            HSSFRow rowObject = (HSSFRow)rows.next();
            logger.info("row---3" + rows);
            DataFormatter formatter = new DataFormatter();
            HSSFCell sPaymentRefNo = rowObject.getCell(0);
            HSSFCell sEventRefNo = rowObject.getCell(1);
            HSSFCell sPaymentAmnt = rowObject.getCell(2);
            HSSFCell sPayAmntCurr = rowObject.getCell(3);
            HSSFCell billEntryNo = rowObject.getCell(4);
            HSSFCell billEntryDate = rowObject.getCell(5);
            HSSFCell portCode = rowObject.getCell(6);
            HSSFCell billEntryAmnt = rowObject.getCell(7);
            HSSFCell billEntryAmntCurr = rowObject.getCell(8);
            HSSFCell sboeAmntEndorse = rowObject.getCell(9);
            HSSFCell sboeAllocAmnt = rowObject.getCell(10);
            HSSFCell sChangeIeCode = rowObject.getCell(11);
            HSSFCell sbesRecInd = rowObject.getCell(12);
            HSSFCell sInvoiceSerNo = rowObject.getCell(13);
            HSSFCell sInvoiceNo = rowObject.getCell(14);
            HSSFCell sInvRelAmt = rowObject.getCell(15);

 
 
 
            HSSFCell sRemarks = rowObject.getCell(16);
            logger.info("paymentamnt---" + 
              sPaymentAmnt);
            logger.info("paymentrefno---" + sPaymentRefNo);
            if ((sPaymentRefNo != null) && 
              (sPaymentRefNo.getCellType() != 3))
            {
              paymentRefNo = 
                formatter.formatCellValue(sPaymentRefNo);
              paymentRefNo = paymentRefNo.trim();
            }
            if ((sEventRefNo != null) && 
              (sEventRefNo.getCellType() != 3))
            {
              eventRefNo = formatter.formatCellValue(sEventRefNo);
              eventRefNo = eventRefNo.trim();
            }
            if ((sPaymentAmnt != null) && 
              (sPaymentAmnt.getCellType() != 3))
            {
              paymentAmnt = 
                formatter.formatCellValue(sPaymentAmnt);
              paymentAmnt = paymentAmnt.trim();
              logger.info("inside payment amnt---" + 
                paymentAmnt);
            }
            if ((sPayAmntCurr != null) && 
              (sPayAmntCurr.getCellType() != 3))
            {
              payAmntCurr = 
                formatter.formatCellValue(sPayAmntCurr);
              payAmntCurr = payAmntCurr.trim();
            }
            if ((billEntryNo != null) && 
              (billEntryNo.getCellType() != 3))
            {
              boeNo = formatter.formatCellValue(billEntryNo);
              boeNo = boeNo.trim();
            }
            if ((billEntryDate != null) && 
              (billEntryDate.getCellType() != 3))
            {
              boeDate = billEntryDate.toString().trim();
              boeDate = boeDate.trim();
            }
            if ((portCode != null) && 
              (portCode.getCellType() != 3))
            {
              portDischarge = formatter.formatCellValue(portCode);
              portDischarge = portDischarge.trim();
            }
            if ((billEntryAmnt != null) && 
              (billEntryAmnt.getCellType() != 3))
            {
              boeAmnt = formatter.formatCellValue(billEntryAmnt);
              boeAmnt = boeAmnt.trim();
            }
            if ((billEntryAmntCurr != null) && 
              (billEntryAmntCurr.getCellType() != 3))
            {
              boeAmntCurr = 
                formatter.formatCellValue(billEntryAmntCurr);
              boeAmntCurr = boeAmntCurr.trim();
            }
            if ((sboeAmntEndorse != null) && 
              (sboeAmntEndorse.getCellType() != 3))
            {
              boeAmntEndorse = 
                formatter.formatCellValue(sboeAmntEndorse);
              boeAmntEndorse = boeAmntEndorse.trim();
            }
            if ((sboeAllocAmnt != null) && 
              (sboeAllocAmnt.getCellType() != 3))
            {
              boeAllocAmnt = 
                formatter.formatCellValue(sboeAllocAmnt);
              boeAllocAmnt = boeAllocAmnt.trim();
            }
            if ((sChangeIeCode != null) && 
              (sChangeIeCode.getCellType() != 3))
            {
              changeIeCode = 
                formatter.formatCellValue(sChangeIeCode);
              changeIeCode = changeIeCode.trim();
            }
            if ((sbesRecInd != null) && 
              (sbesRecInd.getCellType() != 3))
            {
              besRecInd = formatter.formatCellValue(sbesRecInd);
              besRecInd = besRecInd.trim();
            }
            if ((sInvoiceSerNo != null) && 
                    (sInvoiceSerNo.getCellType() != 3))
                  {
                    invoiceSerNo = 
                      formatter.formatCellValue(sInvoiceSerNo);
                    invoiceSerNo = invoiceSerNo.trim();
                  }
                  if ((sInvoiceNo != null) && 
                    (sInvoiceNo.getCellType() != 3))
                  {
                    invoiceNo = formatter.formatCellValue(sInvoiceNo);
                    invoiceNo = invoiceNo.trim();
                  }
                  if ((sInvRelAmt != null) && 
                    (sInvRelAmt.getCellType() != 3))
                  {
                    invRelAmt = formatter.formatCellValue(sInvRelAmt);
                    invRelAmt = invRelAmt.trim();
                  }
                  if ((sRemarks != null) && 
                    (sRemarks.getCellType() != 3))
                  {
                    remarks = formatter.formatCellValue(sRemarks);
                    remarks = remarks.trim();
                    logger.info("remarks  :" + remarks);
                  }
                  bulkVO.setPaymentRefNo(paymentRefNo);
                  bulkVO.setEventRefNo(eventRefNo);
                  bulkVO.setPaymentAmnt(paymentAmnt);
                  logger.info("paymentamnt" + bulkVO.getPaymentAmnt());
                  bulkVO.setPayAmntCurr(payAmntCurr);
                  bulkVO.setBoeNo(boeNo);
                  bulkVO.setBoeDate(boeDate);
                  bulkVO.setPortCode(portDischarge);
                  bulkVO.setBoeAmnt(boeAmnt);
                  bulkVO.setBoeAmntCurr(boeAmntCurr);
                  bulkVO.setBoeAmntEndorse(boeAmntEndorse);
                  bulkVO.setBoeAllocAmnt(boeAllocAmnt);
                  bulkVO.setChangeIeCode(changeIeCode);
                  bulkVO.setBesRecInd(besRecInd);
                  bulkVO.setInvoiceSerNo(invoiceSerNo);
                  bulkVO.setInvoiceNo(invoiceNo);
                  bulkVO.setInvRelAmt(invRelAmt);
                  logger.info("invRelAmt :" + bulkVO.getInvRelAmt());

       
       
       
                  bulkVO.setRemarks(remarks);
                  logger.info("remarks" + bulkVO.getRemarks());
                  boeExtList.add(bulkVO);
                }
                i++;
                logger.info("i  :" + i);
              }
              logger.info("boelist size---" + boeExtList.size());
              logger.info("i----" + i);
              if ((boeExtList != null) && (boeExtList.size() > 0))
              {
                ArrayList<TransactionBoeBlkVO> errorData = validation
                  .valExtBulkUpload(boeExtList);
                if ((errorData == null) || (errorData.size() == 0))
                {
                  batchId = insertExcelData(boeExtList);
                  insertedRowCount = validateExcel(batchId);
                  if (insertedRowCount > 0)
                  {
                    ArrayList<TransactionBoeBlkVO> boeList = getBulkStgData(batchId);
                    trans.setBoeList(boeList);
                    trans.setBatchId(batchId);
                  }
                }
                else
                {
                  trans.setErrorCodeDesc(errorData);
                  logger.info("BOEEXLIST size ===>" + boeExtList.size());
                  trans.setBoevoList(boeExtList);
                  logger.info("trans.getBoevoList().size()" + trans.getBoevoList().size());
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
          return "PAYMENTREFNO|EVENTREFNO|PAYMENTAMOUNT|PAYMENTAMNTCURR|BOENUM|BOEDATE|PORTCODE|BOEAMNT|BOEAMNTCURR|BOEAMNTENDORSE|BOEALLOCAMNT|CHANGEIECODE|RECORDINDICATOR|INVOICESERNUM|INVOICENUM|REALAMT|REMARKS";
        }
        public String getContentDisposition()
        {
          return this.contentDisposition;
        }
        public void setContentDisposition(String contentDisposition)
        {
          this.contentDisposition = contentDisposition;
        }
        public ByteArrayInputStream getExcelStream()
        {
          return this.excelStream;
        }
        public void setExcelStream(ByteArrayInputStream excelStream)
        {
          this.excelStream = excelStream;
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
              String procedureQuery = "{call ETT_BULK_BOE_VAL_DATA(?)}";
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
            DBConnectionUtility.surrenderDB(aConnection, aCallableStatement, 
              aResultSet);
          }
          logger.info("Exiting Method");
          return insertedRowCount;
        }
        public String insertExcelData(ArrayList<BOEBulkUploadVO> boeExtList)
          throws DAOException
        {
          logger.info("Entering Method");
          Connection con = null;
          ResultSet rs = null;
          PreparedStatement ppt = null;
          BOEBulkUploadVO excelDataVO = null;
          String batchId = null;
          CommonMethods commonMethods = null;
          try
          {
            con = DBConnectionUtility.getConnection();
            commonMethods = new CommonMethods();
            HttpSession session = ServletActionContext.getRequest()
              .getSession();
            String userId = (String)session.getAttribute("loginedUserId");
            String query = "SELECT ETT_BULK_BOE_SEQ_NO AS BATCHID FROM DUAL";
            LoggableStatement pst = new LoggableStatement(con, query);
            ResultSet rs1 = pst.executeQuery();
            if (rs1.next()) {
              batchId = rs1.getString("BATCHID");
            }
            pst.close();
            ppt = con.prepareStatement("INSERT INTO ETT_BULK_BOE_STG_DAT (PAYMENTREFNO, EVENTREFNO,PAYMENTAMOUNT,PAYMENTAMOUNTCURRENCY, BOENUMBER, BOEDATE, PORTCODE,BOEAMOUNT,BOEAMOUNTCURRENCY,BOEAMOUNTAVAILABLEENDORSEMENT,BOEALLOCATEDAMOUNTFORPAYCURR, CHANGEIECODE,BESRECORDINDICATOR, INV_SER_NO, INV_NO, INV_REL_AMT, REMARKS, BATCHID, USERID)VALUES(?, ?, ?, ?, ?, TO_DATE(?, 'DD/MM/YYYY'), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,? ,?)");
            for (int i = 0; i < boeExtList.size(); i++)
            {
              excelDataVO = new BOEBulkUploadVO();
              excelDataVO = (BOEBulkUploadVO)boeExtList.get(i);
              if ((!commonMethods.isNull(excelDataVO.getBoeNo())) && 
                (!commonMethods.isNull(excelDataVO.getBoeDate())) && 
                (!commonMethods.isNull(excelDataVO.getPortCode())) && 
                (!commonMethods.isNull(excelDataVO.getInvoiceNo())))
              {
                ppt.setString(1, excelDataVO.getPaymentRefNo());
                ppt.setString(2, excelDataVO.getEventRefNo());
                ppt.setString(3, excelDataVO.getPaymentAmnt());
                ppt.setString(4, excelDataVO.getPayAmntCurr());
                ppt.setString(5, excelDataVO.getBoeNo());
                ppt.setString(6, excelDataVO.getBoeDate());
                ppt.setString(7, excelDataVO.getPortCode());
                ppt.setString(8, excelDataVO.getBoeAmnt());
                ppt.setString(9, excelDataVO.getBoeAmntCurr());
                ppt.setString(10, excelDataVO.getBoeAmntEndorse());
                ppt.setString(11, excelDataVO.getBoeAllocAmnt());
                ppt.setString(12, excelDataVO.getChangeIeCode());
                ppt.setString(13, excelDataVO.getBesRecInd());
                ppt.setString(14, excelDataVO.getInvoiceSerNo());
                ppt.setString(15, excelDataVO.getInvoiceNo());
                ppt.setString(16, excelDataVO.getInvRelAmt());

       
       
       
                ppt.setString(17, excelDataVO.getRemarks());
                ppt.setString(18, batchId);
                ppt.setString(19, userId);
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
        public ArrayList<TransactionBoeBlkVO> getBulkStgData(String batchId)
          throws DAOException
        {
          logger.info("Entering Method");
          Connection con = null;
          LoggableStatement pst = null;
          ResultSet rs = null;
          String sqlQuery = null;
          CommonMethods commonMethods = null;
          ArrayList<TransactionBoeBlkVO> boeList = null;
          try
          {
            commonMethods = new CommonMethods();
            boeList = new ArrayList();
            con = DBConnectionUtility.getConnection();
            sqlQuery = "SELECT PAYMENTREFNO, EVENTREFNO,PAYMENTAMOUNT,PAYMENTAMOUNTCURRENCY,BOENUMBER, TO_CHAR(BOEDATE, 'DD/MM/YYYY') BOEDATE, PORTCODE,BOEAMOUNT,BOEAMOUNTCURRENCY,BOEAMOUNTAVAILABLEENDORSEMENT,BOEALLOCATEDAMOUNTFORPAYCURR,CHANGEIECODE,BESRECORDINDICATOR, INV_SER_NO, INV_NO, INV_REL_AMT, REMARKS, ERRORDTLS FROM ETT_BULK_BOE_STG_DAT WHERE BATCHID = ?";
            pst = new LoggableStatement(con, sqlQuery);
            pst.setString(1, batchId);
            rs = pst.executeQuery();
            while (rs.next())
            {
              TransactionBoeBlkVO boe = new TransactionBoeBlkVO();
              boe.setPaymentRefNo(rs.getString("PAYMENTREFNO"));
              boe.setEventRefNo(rs.getString("EVENTREFNO"));
              boe.setPaymentAmnt(rs.getString("PAYMENTAMOUNT"));
              boe.setPayAmntCurr(rs.getString("PAYMENTAMOUNTCURRENCY"));
              boe.setBoeNo(rs.getString("BOENUMBER"));
              boe.setBoeDate(rs.getString("BOEDATE"));
              boe.setPortCode(rs.getString("PORTCODE"));
              boe.setBoeAmnt(rs.getString("BOEAMOUNT"));
              boe.setBoeAmntCurr(rs.getString("BOEAMOUNTCURRENCY"));
              boe.setBoeAmntEndorse(rs
                .getString("BOEAMOUNTAVAILABLEENDORSEMENT"));
              boe.setBoeAllocAmnt(rs
                .getString("BOEALLOCATEDAMOUNTFORPAYCURR"));
              boe.setChangeIeCode(rs.getString("CHANGEIECODE"));
              boe.setBesRecInd(rs.getString("BESRECORDINDICATOR"));
              boe.setInvoiceSerNo(rs.getString("INV_SER_NO"));
              boe.setInvoiceNo(rs.getString("INV_NO"));
              boe.setInvRealAmt(rs.getString("INV_REL_AMT"));

       
       
       
              boe.setRemarks(rs.getString("REMARKS"));
              boe.setErrDesc(rs.getString("ERRORDTLS"));
              if (commonMethods.isNull(boe.getErrDesc())) {
                boe.setStatus("SUCCESS");
              } else {
                boe.setStatus("FAIL");
              }
              boeList.add(boe);
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
          return boeList;
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
        	      String delQuery = "DELETE FROM ETT_BULK_BOE_STG_DAT where BATCHID =?";
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
        	  public TransactionBoeBlkVO uploadDataToTable(String batchId)
        	    throws SQLException
        	  {
        	    logger.info("Entering Method");
        	    ResultSet aResultSet = null;
        	    Connection aConnection = null;
        	    CallableStatement aCallableStatement = null;
        	    LoggableStatement pst = null;
        	    TransactionBoeBlkVO boe = new TransactionBoeBlkVO();
        	    int iCount = 0;
        	    String sBoeNum = null;
        	    LoggableStatement pst2 = null;
        	    ResultSet aResultSet2 = null;
        	    try
        	    {
        	      aConnection = DBConnectionUtility.getConnection();
        	      if (aConnection != null)
        	      {
        	        String sqlQuery = "SELECT COUNT(*) FROM ETT_BULK_BOE_STG_DAT WHERE BATCHID = ? AND ERRORDTLS IS NULL ";
        	        pst = new LoggableStatement(aConnection, sqlQuery);
        	        pst.setString(1, batchId);
        	        aResultSet = pst.executeQuery();
        	        if (aResultSet.next()) {
        	          iCount = aResultSet.getInt(1);
        	        }
        	        logger.info("iCount after query---> " + iCount);
        	        if (iCount > 0)
        	        {
        	          logger.info("INSIDE COUNT>0---> ");
        	          String procedureQuery = "{call ETT_BULK_BOE_INS_TBL(?, ?, ?)}";
        	          aCallableStatement = aConnection
        	            .prepareCall(procedureQuery);
        	          aCallableStatement.setString(1, batchId);
        	          aCallableStatement.registerOutParameter(2, 
        	            12);
        	          aCallableStatement.registerOutParameter(3, 
        	            12);
        	          if (aCallableStatement.executeUpdate() >= 0)
        	          {
        	            logger.info("INSIDE STORED_PROCEDURE OUT---> ");
        	            String sCnt = aCallableStatement.getString(2);
        	            sBoeNum = aCallableStatement.getString(3);

        	 
        	            boe.setSuccessCount(iCount);

        	 
        	            boe.setCount(sCnt);
        	            boe.setBoeNo(sBoeNum);
        	          }
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
        	      DBConnectionUtility.surrenderDB(aConnection, aCallableStatement, 
        	        aResultSet);
        	      DBConnectionUtility.surrenderDB(null, pst2, aResultSet2);
        	      pst.close();
        	    }
        	    logger.info("Exiting Method");
        	    return boe;
        	  }
        	  public TransactionVO getBOEData(String boeNo, String boeDate, String portCode, String paymentRef, String paymentSlNo)
        	    throws DAOException
        	  {
        	    logger.info("Entering Method");
        	    Connection con = null;
        	    LoggableStatement loggableStatement = null;
        	    ResultSet rs = null;
        	    TransactionVO transactionVO = null;
        	    String sqlQuery = null;
        	    try
        	    {
        	      con = DBConnectionUtility.getConnection();
        	      sqlQuery = "SELECT NVL(TRIM(BP.BOE_PAYMENT_BP_PAY_REF), ' ') as BOE_PAYMENT_BP_PAY_REF,  NVL(TRIM(BP.BOE_PAYMENT_BP_BOE_NO), ' ') as BOE_PAYMENT_BP_BOE_NO,  TO_CHAR(TO_DATE(BP.BOE_PAYMENT_BP_BOE_DT, 'dd-mm-yy'), 'dd/mm/yy') as BOE_PAYMENT_BP_BOE_DT, NVL(TRIM(BP.BOE_PAYMENT_BP_BOE_CCY), ' ') as BOE_PAYMENT_BP_PAY_CCY,PORT_CODE, NVL(TRIM(to_char(BP.BOE_PAYMENT_BP_PAY_ENDORSE_AMT)), '0')as BOE_PAYMENT_BP_PAY_ENDORSE_AMT  FROM ETT_BOE_PAYMENT BP WHERE BOE_PAYMENT_BP_BOE_NO =? AND TO_CHAR(BOE_PAYMENT_BP_BOE_DT,'DD-MM-YYYY') = ? AND PORT_CODE =? AND BOE_PAYMENT_BP_PAY_REF =? AND BOE_PAYMENT_BP_PAY_PART_REF=?";

        	 
        	 
        	 
        	 
        	 
        	      loggableStatement = new LoggableStatement(con, sqlQuery);
        	      loggableStatement.setString(1, boeNo);
        	      loggableStatement.setString(2, boeDate);
        	      loggableStatement.setString(3, portCode);
        	      loggableStatement.setString(4, paymentRef);
        	      loggableStatement.setString(5, paymentSlNo);
        	      rs = loggableStatement.executeQuery();
        	      if (rs.next())
        	      {
        	        transactionVO = new TransactionVO();
        	        transactionVO.setPaymentRefNo(rs
        	          .getString("BOE_PAYMENT_BP_PAY_REF"));
        	        transactionVO.setBoeNo(rs
        	          .getString("BOE_PAYMENT_BP_BOE_NO"));
        	        transactionVO.setBoeDate(rs
        	          .getString("BOE_PAYMENT_BP_BOE_DT"));
        	        transactionVO.setPortCode(rs.getString("PORT_CODE"));
        	        transactionVO.setBoeCurr(rs
        	          .getString("BOE_PAYMENT_BP_PAY_CCY"));
        	        transactionVO.setBalEndorseAmt(CommonMethods.setCheckValue(rs
        	          .getString("BOE_PAYMENT_BP_PAY_ENDORSE_AMT")));
        	      }
        	    }
        	    catch (SQLException e)
        	    {
        	      throwDAOException(e);
        	    }
        	    finally
        	    {
        	      closeSqlRefferance(rs, loggableStatement, con);
        	    }
        	    logger.info("Exiting Method");
        	    return transactionVO;
        	  }
        	}
