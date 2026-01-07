package in.co.localization.utility;
 
import in.co.localization.action.BOEClosBulkUploadAction;
import in.co.localization.dao.exception.DAOException;
import in.co.localization.vo.localization.BOEClosBulkUploadVO;
import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import org.apache.log4j.Logger;
 
public class ValidationUtility
  implements ActionConstantsQuery
{
  DBConnectionUtility dbConnectionUtility;
  private static Logger logger = Logger.getLogger(BOEClosBulkUploadAction.class.getName());
  public static boolean isValidFile(String sFileName)
  {
    logger.info("Entering Method");
    File file = null;
    file = new File(sFileName);
    String sAbsFilePath = file.getAbsolutePath();
    DecimalFormat df = new DecimalFormat(".00");
    String sFilePath = System.getProperty("user.dir") + "\\obe_xml\\";
    String sXMLFileWithPath = sFilePath + sFileName;
    file = new File(sXMLFileWithPath);
    double dFileSize = file.length() / 1048576L;
    double dFileMBSize = Double.parseDouble(df.format(dFileSize));
    if (dFileMBSize <= 8.0D) {
      return true;
    }
    logger.info("Exiting Method");
    return false;
  }
  public static boolean isValiedPortCode(String sPortCode)
  {
    logger.info("Entering Method");
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
    logger.info("Exiting Method");
    return false;
  }
  public static boolean isValidAmount(String amountVal)
  {
    logger.info("Entering Method");
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
    logger.info("Exiting Method");
    return false;
  }
  public static boolean isCheckInvTerm(String sInvTerm)
  {
    logger.info("Entering Method");
    try
    {
      if ((sInvTerm != null) && (!sInvTerm.equals("")) && (
        (sInvTerm.equalsIgnoreCase("CIF")) || (sInvTerm.equalsIgnoreCase("CF")) || 
        (sInvTerm.equalsIgnoreCase("CI")) || (sInvTerm.equalsIgnoreCase("FOB")) || 
        (sInvTerm.equalsIgnoreCase("OTHERS")))) {
        return true;
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      logger.info("Exiting Method");
    }
    return false;
  }
  public static boolean isValidCountry(String sCountryName)
  {
    logger.info("Entering Method");
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
    logger.info("Exiting Method");
    return false;
  }
  public static boolean compareDate(String sDateOne, String sDateTwo)
  {
    logger.info("Entering Method");
    SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyymmdd");
    Calendar cal = Calendar.getInstance();
    try
    {
      if ((sDateOne != null) && (!sDateOne.equals("")) && (sDateTwo != null) && (!sDateTwo.equals("")))
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
      logger.info("Exiting Method");
    }
    return false;
  }
  public static boolean isBOEClosed(String sBOENo, String sBOEDate, String sPortCode)
  {
    logger.info("Entering Method");
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
      if (iRet == 0) {
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
    logger.info("Exiting Method");
    return false;
  }
  public ArrayList valClosBulkUpload(ArrayList<BOEClosBulkUploadVO> alBulkVO)
		    throws DAOException
		  {
		    logger.info("Entering Method");
		    alBulkVO.trimToSize();
		    int k = 1;
		    String j = "";
		    ArrayList<String> alBoeClosBulkUpload = new ArrayList();
		    CommonMethods commonMethods = null;
		    BOEClosBulkUploadVO excelDataVO = null;
		    try
		    {
		      commonMethods = new CommonMethods();
		      alBulkVO.trimToSize();
		      for (int i = 0; i < alBulkVO.size(); i++)
		      {
		        k++;
		        excelDataVO = new BOEClosBulkUploadVO();
		        excelDataVO = (BOEClosBulkUploadVO)alBulkVO.get(i);
		        if (commonMethods.isNull(excelDataVO.getBoeNo())) {
		          j = "Please enter BOE Number,";
		        } else if (excelDataVO.getBoeNo().length() > 7) {
		          j = "Invalid BOE Number Length (" + excelDataVO.getBoeNo() + "),";
		        }
		        if (commonMethods.isNull(excelDataVO.getBoeDate())) {
		          j = j + "Please enter BOE Date,";
		        } else if ((!validateddmmyyyyLength(excelDataVO.getBoeDate())) && 
		          (excelDataVO.getBoeDate().length() != 10)) {
		          j = j + "Please enter BOE Date in dd/mm/yyyy format,";
		        }
		        if (commonMethods.isNull(excelDataVO.getPortCode())) {
		          j = j + "Port Code is Mandatory,";
		        } else if (excelDataVO.getPortCode().length() > 6) {
		          j = j + "Invalid Port Code Length (" + excelDataVO.getPortCode() + "),";
		        }
		        if (commonMethods.isNull(excelDataVO.getClosType())) {
		          j = j + "Please enter Closure Type,";
		        } else if (excelDataVO.getClosType().length() > 2) {
		          j = j + "Invalid Closure Type (" + excelDataVO.getClosType() + "),";
		        }
		        if ((!commonMethods.isNull(excelDataVO.getDocDate())) && 
		          (!validateddmmyyyyLength(excelDataVO.getDocDate()))) {
		          j = j + "Please enter Document Date in dd/mm/yyyy format,";
		        }
		        if ((!commonMethods.isNull(excelDataVO.getAdjClsDate())) && 
		          (!validateddmmyyyyLength(excelDataVO.getAdjClsDate()))) {
		          j = j + "Please enter AdjCls Date in dd/mm/yyyy format,";
		        }
		        if ((!commonMethods.isNull(excelDataVO.getLetterNo())) && 
		          (excelDataVO.getLetterNo().length() > 50)) {
		          j = j + "Invalid Letter Number (" + excelDataVO.getLetterNo() + "),";
		        }
		        if ((!commonMethods.isNull(excelDataVO.getLetterDate())) && 
		          (!validateddmmyyyyLength(excelDataVO.getLetterDate()))) {
		          j = j + "Please enter Letter Date in dd/mm/yyyy format,";
		        }
		        if ((!commonMethods.isNull(excelDataVO.getRemarks())) && 
		          (excelDataVO.getRemarks().length() > 200)) {
		          j = j + "Remarks Exceed 200 characters (" + excelDataVO.getRemarks() + "),";
		        }
		        if (commonMethods.isNull(excelDataVO.getInvoiceSerNo())) {
		          j = j + "Please enter Invoice Serial Number,";
		        } else if (excelDataVO.getInvoiceSerNo().length() > 10) {
		          j = j + "Invalid Invoice Serial Number (" + excelDataVO.getInvoiceSerNo() + "),";
		        }
		        if (commonMethods.isNull(excelDataVO.getInvoiceNo())) {
		          j = j + "Please enter Invoice number,";
		        } else if (excelDataVO.getInvoiceNo().length() > 50) {
		          j = j + "Invalid Invoice Number (" + excelDataVO.getInvoiceNo() + "),";
		        }
		        if (commonMethods.isNull(excelDataVO.getInvamnt())) {
		          j = j + "Please enter Invoice Amount,";
		        } else if (!validatePrecision(excelDataVO.getInvamnt(), "20", "4")) {
		          j = j + "Invalid Invoice Amount (" + excelDataVO.getInvamnt() + "),";
		        }
		        if (commonMethods.isNull(excelDataVO.getInvcurr())) {
		          j = j + "Please enter Invoice Currency,";
		        } else if (excelDataVO.getInvcurr().length() > 10) {
		          j = j + "Invalid Invoice Currency (" + excelDataVO.getInvcurr() + "),";
		        }
		        if (commonMethods.isNull(excelDataVO.getOutamnt())) {
		          j = j + "Please enter Outstanding amount,";
		        } else if (!validatePrecision(excelDataVO.getOutamnt(), "20", "4")) {
		          j = j + "Invalid  Outstanding Amount (" + excelDataVO.getOutamnt() + "),";
		        }
		        if (commonMethods.isNull(excelDataVO.getAdjInvAmtIC())) {
		          j = j + "Please enter Adjusted Invoice AmountIC,";
		        } else if (!validatePrecision(excelDataVO.getAdjInvAmtIC(), "20", "4")) {
		          j = j + "Invalid Adjusted Invoice AmountIC (" + excelDataVO.getAdjInvAmtIC() + "),";
		        }
		        if (!j.equals(""))
		        {
		          j = j.substring(0, j.length() - 1);
		          excelDataVO.setErrorDesc(j);
		          j = Integer.toString(k) + ".[" + j + "] |";
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
		    logger.info("Exiting Method");
		    return alBoeClosBulkUpload;
		  }
		  public boolean validateDateLength(String sDate)
		  {
		    logger.info("Entering Method");
		    DateFormat sdf1 = new SimpleDateFormat("dd-MMM-yyyy");
		    SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
		    try
		    {
		      if (sDate.length() == 10)
		      {
		        if ((sDate != null) && (!sDate.equals("")))
		        {
		          Date date = sdf1.parse(sDate);
		          if (sdf.format(date).length() == 10) {
		            return true;
		          }
		        }
		        if ((sDate == null) || (sDate.equals(""))) {
		          return true;
		        }
		      }
		    }
		    catch (ParseException pe)
		    {
		      pe.printStackTrace();
		      logger.info("Exiting Method");
		    }
		    return false;
		  }
		  public boolean validatePrecision(String sValue, String sLenOne, String sLenTwo)
		  {
		    logger.info("Entering Method");
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
		    logger.info("Exiting Method");
		    return false;
		  }
		  public boolean validateddmmyyyyLength(String sDate)
		  {
		    logger.info("Entering Method");
		    logger.info("This is Inside of validateddmmyyyyLength : " + sDate);
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
		      logger.info("Exiting Method");
		    }
		    return false;
		  }
		}