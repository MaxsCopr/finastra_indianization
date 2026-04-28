package in.co.localization.dao.localization;
 
import in.co.localization.dao.AbstractDAO;
import in.co.localization.dao.exception.DAOException;
import in.co.localization.utility.ActionConstantsQuery;
import in.co.localization.utility.CommonMethods;
import in.co.localization.utility.DBConnectionUtility;
import in.co.localization.utility.LoggableStatement;
import in.co.localization.vo.localization.BOEDataSearchVO;
import in.co.localization.vo.localization.BoeVO;
import in.co.localization.vo.localization.IdpmsOrmVO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import org.apache.log4j.Logger;
 
public class SearchRecordsDAO
  extends AbstractDAO
  implements ActionConstantsQuery
{
  static SearchRecordsDAO dao;
  private static Logger logger = Logger.getLogger(SearchRecordsDAO.class
    .getName());
  public static SearchRecordsDAO getDAO()
  {
    if (dao == null) {
      dao = new SearchRecordsDAO();
    }
    return dao;
  }
  public ArrayList<BoeVO> boeExSearch(BOEDataSearchVO boeSearchVO)
    throws DAOException
  {
    logger.info("---------------boeExSearch---------------");
    Connection con = null;
    LoggableStatement pst = null;
    ResultSet rs = null;
    ArrayList<BoeVO> boeList = null;
    CommonMethods commonMethods = null;
    String BOE_QUERY = null;
    boolean boeNoFlag = false;
    boolean boeDateFlag = false;
    boolean portCodeFlag = false;
    boolean transStatusFlag = false;
    int setValue = 0;
    try
    {
      con = DBConnectionUtility.getConnection();
      commonMethods = new CommonMethods();
      boeList = new ArrayList();

 
 
      commonMethods = new CommonMethods();
      BOE_QUERY = "SELECT BOENUMBER,TO_CHAR(BOEDATE,'DD/MM/YYYY') AS BOEDATE,PORTOFDISCHARGE, ADCODE,IECODE,RECORDINDICATOR,EXTENSIONBY,LETTERNO,TO_CHAR(LETTERDATE,'DD-MM-YYYY') AS LETTERDATE, TO_CHAR(EXTENSIONDATE,'DD-MM-YYYY') AS EXTENSIONDATE,REMARKS,STATUS, EXT_INDICATOR FROM ETT_BOE_EXT_DATA WHERE BOENUMBER = BOENUMBER ";
      String boeNo = boeSearchVO.getBoeNo();
      if (!commonMethods.isNull(boeNo))
      {
        BOE_QUERY = BOE_QUERY + " AND UPPER(BOENUMBER) LIKE UPPER('%'||?||'%')";
        boeNoFlag = true;
      }
      String boeDate = boeSearchVO.getBoeDate();
      if (!commonMethods.isNull(boeDate))
      {
        BOE_QUERY = BOE_QUERY + " AND TO_CHAR(BOEDATE,'DD/MM/YYYY')  = ?";
        boeDateFlag = true;
      }
      String portCode = boeSearchVO.getPortCode();
      if (!commonMethods.isNull(portCode))
      {
        BOE_QUERY = BOE_QUERY + " AND UPPER(PORTOFDISCHARGE) = UPPER(?)";
        portCodeFlag = true;
      }
      String transStatus = commonMethods.getEmptyIfNull(boeSearchVO.getStatus()).trim();
      if (!commonMethods.isNull(transStatus))
      {
        BOE_QUERY = BOE_QUERY + " AND STATUS = ?";
        transStatusFlag = true;
      }
      BOE_QUERY = BOE_QUERY + " ORDER BY BOENUMBER ";
      pst = new LoggableStatement(con, BOE_QUERY);
      if (boeNoFlag) {
        pst.setString(++setValue, boeNo.trim());
      }
      if (boeDateFlag) {
        pst.setString(++setValue, boeDate.trim());
      }
      if (portCodeFlag) {
        pst.setString(++setValue, portCode.trim());
      }
      if (transStatusFlag) {
        pst.setString(++setValue, transStatus);
      }
      rs = pst.executeQuery();
      logger.info("---------------boeExSearch------query---------" + pst.getQueryString());
      while (rs.next())
      {
        BoeVO vo = new BoeVO();
        vo.setBoeNumber(rs.getString("BOENUMBER"));
        vo.setBoeDate(rs.getString("BOEDATE"));
        vo.setPortCode(rs.getString("PORTOFDISCHARGE"));
        vo.setAdCode(rs.getString("ADCODE"));
        vo.setIeCode(rs.getString("IECODE"));
        String recInd = rs.getString("RECORDINDICATOR");
        if ((recInd != null) && (recInd.equalsIgnoreCase("1"))) {
          vo.setRecordIndicator("New");
        } else if ((recInd != null) && (recInd.equalsIgnoreCase("3"))) {
          vo.setRecordIndicator("Cancel");
        }
        String recExtInd = rs.getString("EXT_INDICATOR");
        if ((recExtInd != null) && (recExtInd.equalsIgnoreCase("1"))) {
          vo.setBoeExtTxtInd("New");
        } else if ((recExtInd != null) && (recExtInd.equalsIgnoreCase("3"))) {
          vo.setBoeExtTxtInd("Cancel");
        }
        String extBy = rs.getString("EXTENSIONBY");
        if ((extBy != null) && (extBy.equalsIgnoreCase("1"))) {
          vo.setApprovedBy("RBI");
        } else if ((extBy != null) && (extBy.equalsIgnoreCase("2"))) {
          vo.setApprovedBy("AD Bank");
        } else if ((extBy != null) && (extBy.equalsIgnoreCase("3"))) {
          vo.setApprovedBy("Others");
        }
        vo.setExtLetterNo(rs.getString("LETTERNO"));
        vo.setExtLetterDate(rs.getString("LETTERDATE"));
        vo.setExtDate(rs.getString("EXTENSIONDATE"));
        vo.setExtRemarks(rs.getString("REMARKS"));

 
        String transStatuss = commonMethods.getEmptyIfNull(rs.getString("STATUS")).trim();
        if (transStatuss.equalsIgnoreCase("P")) {
          vo.setStatus("Pending");
        } else if (transStatuss.equalsIgnoreCase("A")) {
          vo.setStatus("Approved");
        } else if (transStatuss.equalsIgnoreCase("R")) {
          vo.setStatus("Reject");
        }
        boeList.add(vo);
      }
    }
    catch (Exception e)
    {
      logger.info("---------------boeExSearch-------exception--------" + e);
      e.printStackTrace();
    }
    finally
    {
      DBConnectionUtility.surrenderDB(con, pst, rs);
    }
    logger.info("Exiting Method");
    return boeList;
  }
  public ArrayList<BoeVO> boeClSearch(BOEDataSearchVO boeSearchVO)
		    throws DAOException
		  {
		    logger.info("----------------------------boeClSearch----------------------------");
		    Connection con = null;
		    LoggableStatement pst = null;
		    ResultSet rs = null;
		    ArrayList<BoeVO> boeList = null;
		    CommonMethods commonMethods = null;
		    String BOE_QUERY = null;
		    boolean boeNoFlag = false;
		    boolean boeDateFlag = false;
		    boolean portCodeFlag = false;
		    boolean clTypeFlag = false;
		    boolean transStatusFlag = false;
		    int setValue = 0;
		    try
		    {
		      con = DBConnectionUtility.getConnection();
		      commonMethods = new CommonMethods();
		      boeList = new ArrayList();

		 
		 
		 
		      commonMethods = new CommonMethods();
		      BOE_QUERY = "SELECT BOENUMBER,TO_CHAR(BOEDATE,'DD/MM/YYYY') AS BOEDATE,PORTOFDISCHARGE, ADCODE,IECODE,BOE_CLOS_IND,REMARKS,BOE_CL_TYPE,STATUS,CLOSURE_SNO FROM ETT_BOE_CLOSURE_DATA WHERE BOENUMBER = BOENUMBER ";
		      String boeNo = boeSearchVO.getBoeNo();
		      if (!commonMethods.isNull(boeNo))
		      {
		        BOE_QUERY = BOE_QUERY + " AND UPPER(BOENUMBER) LIKE UPPER('%'||?||'%')";
		        boeNoFlag = true;
		      }
		      String boeDate = boeSearchVO.getBoeDate();
		      if (!commonMethods.isNull(boeDate))
		      {
		        BOE_QUERY = BOE_QUERY + " AND TO_CHAR(BOEDATE,'DD/MM/YYYY')  = ?";
		        boeDateFlag = true;
		      }
		      String portCode = boeSearchVO.getPortCode();
		      if (!commonMethods.isNull(portCode))
		      {
		        BOE_QUERY = BOE_QUERY + " AND UPPER(PORTOFDISCHARGE) = UPPER(?)";
		        portCodeFlag = true;
		      }
		      String clType = boeSearchVO.getClTransType();
		      if (!commonMethods.isNull(clType))
		      {
		        BOE_QUERY = BOE_QUERY + " AND BOE_CL_TYPE =?";
		        clTypeFlag = true;
		      }
		      String transStatus = commonMethods.getEmptyIfNull(boeSearchVO.getStatus()).trim();
		      if (!commonMethods.isNull(transStatus))
		      {
		        BOE_QUERY = BOE_QUERY + " AND STATUS = ?";
		        transStatusFlag = true;
		      }
		      BOE_QUERY = BOE_QUERY + " ORDER BY BOENUMBER ";
		      pst = new LoggableStatement(con, BOE_QUERY);
		      if (boeNoFlag) {
		        pst.setString(++setValue, boeNo.trim());
		      }
		      if (boeDateFlag) {
		        pst.setString(++setValue, boeDate.trim());
		      }
		      if (portCodeFlag) {
		        pst.setString(++setValue, portCode.trim());
		      }
		      if (clTypeFlag) {
		        pst.setString(++setValue, clType.trim());
		      }
		      if (transStatusFlag) {
		        pst.setString(++setValue, transStatus);
		      }
		      rs = pst.executeQuery();
		      logger.info("----------------------------boeClSearch-------------query---------------" + pst.getQueryString());
		      while (rs.next())
		      {
		        BoeVO vo = new BoeVO();
		        vo.setBoeNumber(rs.getString("BOENUMBER"));
		        vo.setBoeDate(rs.getString("BOEDATE"));
		        vo.setPortCode(rs.getString("PORTOFDISCHARGE"));
		        vo.setAdCode(rs.getString("ADCODE"));
		        vo.setIeCode(rs.getString("IECODE"));
		        String recInd = rs.getString("BOE_CLOS_IND");
		        if ((recInd != null) && (recInd.equalsIgnoreCase("1"))) {
		          vo.setRecordIndicator("New");
		        } else if ((recInd != null) && (recInd.equalsIgnoreCase("3"))) {
		          vo.setRecordIndicator("Cancel");
		        }
		        vo.setClosureRemarks(rs.getString("REMARKS"));
		        vo.setExKey(rs.getString("CLOSURE_SNO"));
		        String transStatuss = commonMethods.getEmptyIfNull(rs.getString("STATUS")).trim();
		        if (transStatuss.equalsIgnoreCase("P")) {
		          vo.setStatus("Pending");
		        } else if (transStatuss.equalsIgnoreCase("A")) {
		          vo.setStatus("Approved");
		        } else if (transStatuss.equalsIgnoreCase("R")) {
		          vo.setStatus("Reject");
		        }
		        String clTypee = commonMethods.getEmptyIfNull(rs.getString("BOE_CL_TYPE")).trim();
		        if (clTypee.equalsIgnoreCase("C")) {
		          vo.setClTransType("BOE Closure");
		        } else if (clTypee.equalsIgnoreCase("O")) {
		          vo.setClTransType("Endorsed By Other AD");
		        }
		        boeList.add(vo);
		      }
		    }
		    catch (Exception e)
		    {
		      logger.info("----------------------------boeClSearch------------exception----------------" + e);
		      e.printStackTrace();
		    }
		    finally
		    {
		      DBConnectionUtility.surrenderDB(con, pst, rs);
		    }
		    logger.info("Exiting Method");
		    return boeList;
		  }
		  public ArrayList<IdpmsOrmVO> ormClSearch(BOEDataSearchVO boeSearchVO)
		    throws DAOException
		  {
		    logger.info("----------ormClSearch---------------------");
		    LoggableStatement pst = null;
		    ResultSet rs = null;
		    Connection con = null;
		    String query = null;
		    IdpmsOrmVO idpmsOrmVO = null;
		    CommonMethods commonMethods = null;
		    ArrayList<IdpmsOrmVO> idpmsOrmList = null;
		    try
		    {
		      con = DBConnectionUtility.getConnection();
		      idpmsOrmList = new ArrayList();
		      commonMethods = new CommonMethods();
		      String transStatus = commonMethods.getEmptyIfNull(boeSearchVO.getStatus()).trim();
		      query = "SELECT TOUTWARDREFERENCENUMBER,TADCODE,TCLOSAMT,TCURRENCYCODE,TIECODE,TSWIFTMESSAGE, TRECORDINDICATOR,TREMARKS,TLETTERNO,TO_CHAR(TLETTERDAT,'DD/MM/YYYY') AS TLETTERDAT, TO_CHAR(TCLOSDAT,'DD/MM/YYYY') AS TCLOSDAT,TDOCNUM,TO_CHAR(TDOCDAT,'DD/MM/YYYY') AS TDOCDAT, TADJCLOINDI,TPORTCODE,APPROVEDBY,TSTATUS FROM ETT_IDPMS_DATA_EC WHERE TOUTWARDREFERENCENUMBER = TOUTWARDREFERENCENUMBER ";

		 
		 
		      String ormNo = commonMethods.getEmptyIfNull(boeSearchVO.getOrmNumber()).trim();
		      if ((!commonMethods.isNull(ormNo)) && (!commonMethods.isNull(transStatus)))
		      {
		        query = query + "AND  TOUTWARDREFERENCENUMBER = ?  AND TSTATUS = ?";
		        pst = new LoggableStatement(con, query);
		        pst.setString(1, ormNo);
		        pst.setString(2, transStatus);
		      }
		      else if (!commonMethods.isNull(transStatus))
		      {
		        query = query + " AND TSTATUS = ?";
		        pst = new LoggableStatement(con, query);
		        pst.setString(1, transStatus);
		      }
		      else if (!commonMethods.isNull(ormNo))
		      {
		        query = query + "AND   TOUTWARDREFERENCENUMBER = ?";
		        pst = new LoggableStatement(con, query);
		        pst.setString(1, ormNo);
		      }
		      rs = pst.executeQuery();
		      logger.info("ormClSearch---------Query String----" + pst.getQueryString());
		      while (rs.next())
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
		        if ((adjInd != null) && (adjInd.equalsIgnoreCase("1"))) {
		          idpmsOrmVO.setOadjcloind("Destroy Of Goods");
		        } else if ((adjInd != null) && (adjInd.equalsIgnoreCase("2"))) {
		          idpmsOrmVO.setOadjcloind("Sort Shipment");
		        } else if ((adjInd != null) && (adjInd.equalsIgnoreCase("3"))) {
		          idpmsOrmVO.setOadjcloind("Quality Issue");
		        } else if ((adjInd != null) && (adjInd.equalsIgnoreCase("4"))) {
		          idpmsOrmVO.setOadjcloind("Re-Import");
		        } else if ((adjInd != null) && (adjInd.equalsIgnoreCase("5"))) {
		          idpmsOrmVO.setOadjcloind("Re-Export");
		        } else if ((adjInd != null) && (adjInd.equalsIgnoreCase("6"))) {
		          idpmsOrmVO.setOadjcloind("Set-off/Net off");
		        } else if ((adjInd != null) && (adjInd.equalsIgnoreCase("7"))) {
		          idpmsOrmVO.setOadjcloind("Others");
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
		        String ormStatus = commonMethods.getEmptyIfNull(rs.getString("TSTATUS")).trim();
		        if (ormStatus.equalsIgnoreCase("P")) {
		          idpmsOrmVO.setStatus("Pending");
		        } else if (ormStatus.equalsIgnoreCase("A")) {
		          idpmsOrmVO.setStatus("Approved");
		        } else if (ormStatus.equalsIgnoreCase("R")) {
		          idpmsOrmVO.setStatus("Reject");
		        }
		        idpmsOrmList.add(idpmsOrmVO);
		      }
		    }
		    catch (Exception exception)
		    {
		      logger.info("ormClSearch-------exception----" + exception);
		      throwDAOException(exception);
		    }
		    finally
		    {
		      DBConnectionUtility.surrenderDB(con, pst, rs);
		    }
		    logger.info("Exiting Method");
		    return idpmsOrmList;
		  }
		}