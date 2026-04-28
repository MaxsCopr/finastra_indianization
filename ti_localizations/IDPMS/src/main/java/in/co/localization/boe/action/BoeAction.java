package in.co.localization.boe.action;
 
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import in.co.boe.vo.TransactionVO;
import in.co.localization.action.LocalizationBaseAction;
import in.co.localization.boe.businessdelegate.BoeBD;
import in.co.localization.businessdelegate.localization.AdTransferBD;
import in.co.localization.dao.exception.ApplicationException;
import in.co.localization.utility.ActionConstants;
import in.co.localization.utility.ActionConstantsQuery;
import in.co.localization.utility.CommonMethods;
import in.co.localization.utility.DBConnectionUtility;
import in.co.localization.utility.LoggableStatement;
import in.co.localization.utility.ValidationUtility;
import in.co.localization.vo.localization.AlertMessagesVO;
import in.co.localization.vo.localization.BOEDataSearchVO;
import in.co.localization.vo.localization.BoeVO;
import in.co.localization.vo.localization.InvoiceDetailsVO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
 
public class BoeAction
  extends LocalizationBaseAction
  implements ActionConstantsQuery, Action
{
  private static final long serialVersionUID = 1L;
  private static Logger logger = Logger.getLogger(BoeAction.class.getName());
  private ArrayList<AlertMessagesVO> alertMsgArray;
  BoeVO boevo;
  ArrayList<BoeVO> boeList = null;
  String[] checkList;
  String[] exChkList;
  String[] clChkList;
  String boeExStatus;
  String boeClStatus;
  String remarks;
  String uniqueId;
  Map<String, String> boeTypeList;
  Map<String, String> appGivenByList;
  Map<String, String> indicatorList;
  Map<String, String> billIndicator;
  Map<String, String> clTransType;
  Map<String, String> boeExtIndList;
  Map<String, String> boeCloIndList;
  ArrayList<InvoiceDetailsVO> invoiceList;
  String[] chkInvlist;
  BOEDataSearchVO boeSearchVO;
  String exStatus;
  String clStatus;
  TransactionVO transactionVO;
  ArrayList<BoeVO> dsgPortList = null;
  public ArrayList<BoeVO> getDsgPortList()
  {
    return this.dsgPortList;
  }
  public void setDsgPortList(ArrayList<BoeVO> dsgPortList)
  {
    this.dsgPortList = dsgPortList;
  }
  public Map<String, String> getBoeCloIndList()
  {
    return ActionConstants.BOE_CLO_IND;
  }
  public void setBoeCloIndList(Map<String, String> boeCloIndList)
  {
    this.boeCloIndList = boeCloIndList;
  }
  public Map<String, String> getBoeExtIndList()
  {
    return ActionConstants.BOE_EXT_IND;
  }
  public void setBoeExtIndList(Map<String, String> boeExtIndList)
  {
    this.boeExtIndList = boeExtIndList;
  }
  public Map<String, String> getClTransType()
  {
    return ActionConstants.BOE_CL_TYPE;
  }
  public void setClTransType(Map<String, String> clTransType)
  {
    this.clTransType = clTransType;
  }
  public TransactionVO getTransactionVO()
  {
    return this.transactionVO;
  }
  public void setTransactionVO(TransactionVO transactionVO)
  {
    this.transactionVO = transactionVO;
  }
  public String getExStatus()
  {
    return this.exStatus;
  }
  public void setExStatus(String exStatus)
  {
    this.exStatus = exStatus;
  }
  public String getClStatus()
  {
    return this.clStatus;
  }
  public void setClStatus(String clStatus)
  {
    this.clStatus = clStatus;
  }
  public String[] getClChkList()
  {
    return this.clChkList;
  }
  public void setClChkList(String[] clChkList)
  {
    this.clChkList = clChkList;
  }
  public String getBoeClStatus()
  {
    return this.boeClStatus;
  }
  public void setBoeClStatus(String boeClStatus)
  {
    this.boeClStatus = boeClStatus;
  }
  public String[] getExChkList()
  {
    return this.exChkList;
  }
  public void setExChkList(String[] exChkList)
  {
    this.exChkList = exChkList;
  }
  public String getBoeExStatus()
  {
    return this.boeExStatus;
  }
  public void setBoeExStatus(String boeExStatus)
  {
    this.boeExStatus = boeExStatus;
  }
  public BOEDataSearchVO getBoeSearchVO()
  {
    return this.boeSearchVO;
  }
  public void setBoeSearchVO(BOEDataSearchVO boeSearchVO)
  {
    this.boeSearchVO = boeSearchVO;
  }
  public String[] getChkInvlist()
  {
    return this.chkInvlist;
  }
  public void setChkInvlist(String[] chkInvlist)
  {
    this.chkInvlist = chkInvlist;
  }
  public Map<String, String> getBillIndicator()
  {
    return ActionConstants.BOE_CLOSURE_IND;
  }
  public void setBillIndicator(Map<String, String> billIndicator)
  {
    this.billIndicator = billIndicator;
  }
  public ArrayList<AlertMessagesVO> getAlertMsgArray()
  {
    return this.alertMsgArray;
  }
  public void setAlertMsgArray(ArrayList<AlertMessagesVO> alertMsgArray)
  {
    this.alertMsgArray = alertMsgArray;
  }
  public ArrayList<InvoiceDetailsVO> getInvoiceList()
  {
    return this.invoiceList;
  }
  public void setInvoiceList(ArrayList<InvoiceDetailsVO> invoiceList)

  {

    this.invoiceList = invoiceList;

  }

  public Map<String, String> getIndicatorList()

  {

    return ActionConstants.BOE_CLOSURE_INDICATOR;

  }

  public void setIndicatorList(Map<String, String> indicatorList)

  {

    this.indicatorList = indicatorList;

  }

  public Map<String, String> getAppGivenByList()

  {

    return ActionConstants.APP_BY;

  }

  public void setAppGivenByList(Map<String, String> appGivenByList)

  {

    this.appGivenByList = appGivenByList;

  }

  public Map<String, String> getBoeTypeList()

  {

    return ActionConstants.BOE_TYPE;

  }

  public void setBoeTypeList(Map<String, String> boeTypeList)

  {

    this.boeTypeList = boeTypeList;

  }

  public String getUniqueId()

  {

    return this.uniqueId;

  }

  public void setUniqueId(String uniqueId)

  {

    this.uniqueId = uniqueId;

  }

  public String[] getCheckList()

  {

    return this.checkList;

  }

  public void setCheckList(String[] checkList)

  {

    this.checkList = checkList;

  }

  public String getRemarks()

  {

    return this.remarks;

  }

  public void setRemarks(String remarks)

  {

    this.remarks = remarks;

  }

  public BoeVO getBoevo()

  {

    return this.boevo;

  }

  public void setBoevo(BoeVO boevo)

  {

    this.boevo = boevo;

  }

  public ArrayList<BoeVO> getBoeList()

  {

    return this.boeList;

  }

  public void setBoeList(ArrayList<BoeVO> boeList)

  {

    this.boeList = boeList;

  }

  public String boeEX_CLMaker()

    throws ApplicationException

  {

    logger.info("Entering Method");

    String target = null;

    AdTransferBD bd = null;

    try

    {

      isSessionAvailable();

      bd = new AdTransferBD();

      HttpSession session = ServletActionContext.getRequest().getSession();

      String sessionUserName = (String)session.getAttribute("loginedUserName");

 
      logger.info(" boeEX_CLMaker sessionUserName--->" + sessionUserName);

      String pageType = "BOEMaker";

      int count = bd.checkLoginedUserType(sessionUserName, pageType);

      if (count > 0) {

        target = "success";

      } else {

        target = "fail";

      }

    }

    catch (Exception exception)

    {

      throwApplicationException(exception);

    }

    logger.info("Exiting Method");

    return target;

  }

  public String boeEX_CLChecker()

    throws ApplicationException

  {

    logger.info("Entering Method");

    String target = null;

    AdTransferBD bd = null;

    try

    {

      isSessionAvailable();

      bd = new AdTransferBD();

      HttpSession session = ServletActionContext.getRequest().getSession();

      String sessionUserName = (String)session.getAttribute("loginedUserName");

      logger.info("boeEX_CLChecker  sessionUserName--->" + sessionUserName);

 
      String pageType = "BOEChecker";

      int count = bd.checkLoginedUserType(sessionUserName, pageType);

      logger.info("count eligible for Checker ---" + count);

      if (count > 0) {

        target = "success";

      } else {

        target = "fail";

      }

    }

    catch (Exception exception)

    {

      logger.info("boeEX_CLChecker-----------Exception" + exception);

      throwApplicationException(exception);

    }

    logger.info("Exiting Method");

    return target;

  }

  public String boeExtension()

    throws ApplicationException

  {

    logger.info("Entering Method");

    try

    {

      isSessionAvailable();

      if ((this.exStatus != null) && (this.exStatus.equalsIgnoreCase("1"))) {

        addActionMessage("BOE Extension details are successfully submitted");

      } else if ((this.exStatus != null) && (this.exStatus.equalsIgnoreCase("2"))) {

        addActionError("Error in BOE Extension details");

      }

    }

    catch (Exception exception)

    {

      logger.info("boeExtension-----------------" + exception);

      throwApplicationException(exception);

    }

    logger.info("Exiting Method");

    return "success";

  }

  public String fetchBOEExtensionDetails()

    throws ApplicationException

  {

    logger.info("------------------fetchBOEExtensionDetails--------------------------");

    BoeBD bd = null;

    try

    {

      bd = BoeBD.getBD();

      this.boevo = bd.fetchBOEExtension(this.boevo);

    }

    catch (Exception e)

    {

      logger.info("fetchBOEExtensionDetails----------------" + e);

      e.printStackTrace();

    }

    logger.info("Exiting Method");

    return "success";

  }

  public String boeExtensionSubmit()

    throws ApplicationException

  {

    logger.info("-----------boeExtensionSubmit---------------");

    BoeBD bd = null;

    String target = null;

    try

    {

      bd = new BoeBD();

      boeExtensionValidations(this.boevo);

      if (this.alertMsgArray.size() > 0)

      {

        logger.info("Extension Error Page");

        target = "error";

      }

      else

      {

        this.boevo = bd.boeExtSubmit(this.boevo);

        if (this.boevo.getErrorCode().equalsIgnoreCase("success")) {

          this.exStatus = "1";

        } else {

          this.exStatus = "2";

        }

        target = "success";

      }

    }

    catch (Exception e)

    {

      logger.info("-----------boeExtensionSubmit-------Exception--------" + e);

    }

    logger.info("Exiting Method");

    return target;

  }

  public String boeClosure()

    throws ApplicationException

  {

    logger.info("Entering Method");

    try

    {

      isSessionAvailable();

      if ((this.clStatus != null) && (this.clStatus.equalsIgnoreCase("1"))) {

        addActionMessage("BOE Closure details are successfully submitted");

      } else if ((this.clStatus != null) && (this.clStatus.equalsIgnoreCase("2"))) {

        addActionError("Error in BOE Closure details");

      }

    }

    catch (Exception exception)

    {

      logger.info("boeClosure-------------" + exception);

      throwApplicationException(exception);

    }

    logger.info("Exiting Method");

    return "success";

  }
  public String fetchBOEClosureDetails()

		    throws ApplicationException

		  {

		    logger.info("---------fetchBOEClosureDetails---------");

		    BoeBD bd = null;

		    try

		    {

		      bd = BoeBD.getBD();

		      this.boevo = bd.fetchBOEClosure(this.boevo);

		      if ((this.boevo.getInvoiceList() != null) && 

		        (!this.boevo.getInvoiceList().isEmpty())) {

		        this.invoiceList = this.boevo.getInvoiceList();

		      }

		    }

		    catch (Exception e)

		    {

		      logger.info("---------fetchBOEClosureDetails--exception-------" + e);

		      e.printStackTrace();

		    }

		    logger.info("Exiting Method");

		    return "success";

		  }

		  public String crossCurrencyPage()

		    throws ApplicationException

		  {

		    logger.info("Entering Method");

		    BoeBD boeBD = null;

		    try

		    {

		      boeBD = BoeBD.getBD();

		      this.transactionVO = boeBD.getInvoiceDetails(this.boevo);

		    }

		    catch (Exception e)

		    {

		      e.printStackTrace();

		      throwApplicationException(e);

		    }

		    logger.info("Exiting Method");

		    return "success";

		  }

		  public String backBOEData()

		    throws ApplicationException

		  {

		    logger.info("Entering Method");

		    BoeBD boeBD = null;

		    try

		    {

		      boeBD = BoeBD.getBD();

		      if (this.boevo != null)

		      {

		        this.boevo = boeBD.revertBOEDetails(this.boevo);

		        if ((this.boevo.getInvoiceList() != null) && 

		          (!this.boevo.getInvoiceList().isEmpty())) {

		          this.invoiceList = this.boevo.getInvoiceList();

		        }

		      }

		    }

		    catch (Exception exception)

		    {

		      throwApplicationException(exception);

		    }

		    logger.info("Exiting Method");

		    return "success";

		  }

		  public String updateCrossCurrencyValues()

		    throws ApplicationException

		  {

		    logger.info("Entering Method");

		    BoeBD boeBD = null;

		    String target = "error";

		    try

		    {

		      boeBD = BoeBD.getBD();

		      this.transactionVO = boeBD.updateCrossCurrencyData(this.boevo, this.transactionVO);

		      if (this.boevo != null)

		      {

		        this.boevo = boeBD.revertBOEDetails(this.boevo);

		        if ((this.boevo.getInvoiceList() != null) && 

		          (!this.boevo.getInvoiceList().isEmpty())) {

		          this.invoiceList = this.boevo.getInvoiceList();

		        }

		      }

		      target = "success";

		    }

		    catch (Exception e)

		    {

		      throwApplicationException(e);

		    }

		    logger.info("Exiting Method");

		    return target;

		  }

		  public String invoiceData()

		    throws ApplicationException

		  {

		    logger.info("Entering Method");

		    BoeBD boeBD = null;

		    try

		    {

		      boeBD = BoeBD.getBD();

		      if (this.boevo != null) {

		        this.transactionVO = boeBD.fetchInvoiceData(this.boevo);

		      }

		    }

		    catch (Exception exception)

		    {

		      throwApplicationException(exception);

		    }

		    logger.info("Exiting Method");

		    return "success";

		  }

		  public String boeClosureSubmit()

		    throws ApplicationException

		  {

		    logger.info("---boeClosureSubmit--");

		    BoeBD bd = null;

		    String target = null;

		    try

		    {

		      bd = new BoeBD();

		      boeClosureValidations(this.boevo);

		      logger.info("boeClosureSubmit Maker  boevo--------------------" + this.boevo);

		      if (this.alertMsgArray.size() > 0)

		      {

		        this.boevo = bd.fetchBOEClosure(this.boevo);

		        if ((this.boevo.getInvoiceList() != null) && 

		          (!this.boevo.getInvoiceList().isEmpty())) {

		          this.invoiceList = this.boevo.getInvoiceList();

		        }

		        target = "error";

		      }

		      else

		      {

		        logger.info("chkInvlist---------------->" + this.chkInvlist);

		        this.boevo = bd.boeClosureSubmit(this.boevo, this.chkInvlist);

		        this.boevo = bd.fetchBOEClosure(this.boevo);

		        if ((this.boevo.getInvoiceList() != null) && 

		          (!this.boevo.getInvoiceList().isEmpty())) {

		          this.invoiceList = this.boevo.getInvoiceList();

		        }

		        if (this.boevo.getErrorCode().equalsIgnoreCase("success")) {

		          this.clStatus = "1";

		        } else {

		          this.clStatus = "2";

		        }

		        target = "success";

		      }

		    }

		    catch (Exception e)

		    {

		      logger.info("boeClosureSubmit error values of action " + e.getMessage());

		    }

		    logger.info("Exiting Method");

		    return target;

		  }
		  public void boeExtensionValidations(BoeVO boevo)

		  {

		    BoeBD bd = null;

		    CommonMethods commonMethods = null;

		    try

		    {

		      bd = new BoeBD();

		      this.alertMsgArray = new ArrayList();

		      commonMethods = new CommonMethods();

		      if ((this.alertMsgArray != null) && 

		        (this.alertMsgArray.size() > 0)) {

		        this.alertMsgArray.clear();

		      }

		      if (commonMethods.isNull(boevo.getAdCode()))

		      {

		        String errorcode = commonMethods.getErrorDesc("BOEAD2", "N100");

		        Object[] arg = { "0", "E", errorcode, "Input" };

		        setErrorvalues(arg);

		      }

		      if (commonMethods.isNull(boevo.getIeCode()))

		      {

		        String errorcode = commonMethods.getErrorDesc("BOE04", "N100");

		        Object[] arg = { "0", "E", errorcode, "Input" };

		        setErrorvalues(arg);

		      }

		      if (commonMethods.isNull(boevo.getRecordIndicator()))

		      {

		        String errorcode = commonMethods.getErrorDesc("ORM002", "N100");

		        Object[] arg = { "0", "E", errorcode, "Input" };

		        setErrorvalues(arg);

		      }

		      if (commonMethods.isNull(boevo.getExtLetterNo()))

		      {

		        String errorcode = commonMethods.getErrorDesc("BEA006", "N100");

		        Object[] arg = { "0", "E", errorcode, "Input" };

		        setErrorvalues(arg);

		      }

		      if (commonMethods.isNull(boevo.getExtLetterDate()))

		      {

		        String errorcode = commonMethods.getErrorDesc("BEA007", "N100");

		        Object[] arg = { "0", "E", errorcode, "Input" };

		        setErrorvalues(arg);

		      }

		      if (commonMethods.isNull(boevo.getApprovalBy()))

		      {

		        String errorcode = commonMethods.getErrorDesc("BOE28", "N100");

		        Object[] arg = { "0", 

		          "E", errorcode, "Input" };

		        setErrorvalues(arg);

		      }

		      if (commonMethods.isNull(boevo.getBoeExtDate()))

		      {

		        String errorcode = commonMethods.getErrorDesc("BOE29", "N100");

		        Object[] arg = { "0", "E", errorcode, "Input" };

		        setErrorvalues(arg);

		      }

		      int boeExCount = bd.getBoeExCount(boevo);

		      if (boeExCount > 0)

		      {

		        String errorcode = commonMethods.getErrorDesc("BOE30", "N100");

		        Object[] arg = { "0", "E", errorcode, "Input" };

		        setErrorvalues(arg);

		      }

		      int boeStatus = bd.getBoeExStatus(boevo);

		      if (boeStatus > 0)

		      {

		        String errorcode = commonMethods.getErrorDesc("BOE31", "N100");

		        Object[] arg = { "0", "E", errorcode, "Input" };

		        setErrorvalues(arg);

		      }

		      int mbeCount = bd.getMBEStatus(boevo);

		      if (mbeCount == 0)

		      {

		        String errorcode = commonMethods.getErrorDesc("BOE33", "N100");

		        Object[] arg = { "0", "E", errorcode, "Input" };

		        setErrorvalues(arg);

		      }

		      if (!ValidationUtility.isBOEClosed(boevo.getBoeNumber(), boevo.getBoeDate(), boevo.getPortCode()))

		      {

		        String errorcode = "Bill Of Entry is already closed";

		        Object[] arg = { "0", "E", errorcode, "Input" };

		        setErrorvalues(arg);

		      }

		      if (commonMethods.isNull(boevo.getBoeExtInd()))

		      {

		        String errorcode = "BOE Extension Indicator is Mandatory";

		        Object[] arg = { "0", "E", errorcode, "Input" };

		        setErrorvalues(arg);

		      }

		    }

		    catch (Exception e)

		    {

		      logger.info("boeExtensionValidations------Exception--------" + e);

		      e.printStackTrace();

		    }

		  }

		  public void boeClosureValidations(BoeVO boevo)

		  {

		    BoeBD bd = null;

		    CommonMethods commonMethods = null;

		    try

		    {

		      this.alertMsgArray = new ArrayList();

		      commonMethods = new CommonMethods();

		      bd = new BoeBD();

		      if ((this.alertMsgArray != null) && 

		        (this.alertMsgArray.size() > 0)) {

		        this.alertMsgArray.clear();

		      }

		      if (commonMethods.isNull(boevo.getAdCode()))

		      {

		        logger.info("boevo.getAdCode() :: " + boevo.getAdCode());

		        String errorcode = commonMethods.getErrorDesc("BOEAD2", "N100");

		        Object[] arg = { "0", "E", errorcode, "Input" };

		        setErrorvalues(arg);

		      }

		      if (commonMethods.isNull(boevo.getIeCode()))

		      {

		        logger.info("boevo.getIeCode() :: " + boevo.getIeCode());

		        String errorcode = commonMethods.getErrorDesc("BOE04", "N100");

		        Object[] arg = { "0", "E", errorcode, "Input" };

		        setErrorvalues(arg);

		      }

		      if (commonMethods.isNull(boevo.getRecordIndicator()))

		      {

		        logger.info("boevo.getRecordIndicator() :: " + boevo.getRecordIndicator());

		        String errorcode = commonMethods.getErrorDesc("ORM002", "N100");

		        Object[] arg = { "0", "E", errorcode, "Input" };

		        setErrorvalues(arg);

		      }

		      if ((!commonMethods.isNull(boevo.getClTransType())) && 

		        (boevo.getClTransType().equalsIgnoreCase("C")))

		      {

		        logger.info("boevo.getClTransType() :: " + boevo.getClTransType());

		        if (!commonMethods.isNull(boevo.getAdjClosureIndicator()))

		        {

		          logger.info("boevo.getAdjClosureIndicator() :: " + boevo.getAdjClosureIndicator());

		          if ((boevo.getAdjClosureIndicator().equalsIgnoreCase("4")) || (boevo.getAdjClosureIndicator().equalsIgnoreCase("5")) || 

		            (boevo.getAdjClosureIndicator().equalsIgnoreCase("6")))

		          {

		            if (commonMethods.isNull(boevo.getDocNo()))

		            {

		              String errorcode = commonMethods.getErrorDesc("BEA002", "N100");

		              Object[] arg = { "0", "E", errorcode, "Input" };

		              setErrorvalues(arg);

		            }

		            if (commonMethods.isNull(boevo.getDocDate()))

		            {

		              String errorcode = commonMethods.getErrorDesc("BEA003", "N100");

		              Object[] arg = { "0", "E", errorcode, "Input" };

		              setErrorvalues(arg);

		            }

		            if (commonMethods.isNull(boevo.getDocPort()))

		            {

		              String errorcode = commonMethods.getErrorDesc("BEA004", "N100");

		              Object[] arg = { "0", "E", errorcode, "Input" };

		              setErrorvalues(arg);

		            }

		          }

		        }
		        else if (commonMethods.isNull(boevo.getAdjClosureIndicator()))

		        {

		          String errorcode = commonMethods.getErrorDesc("BEA001", "N100");

		          Object[] arg = { "0", "E", errorcode, "Input" };

		          setErrorvalues(arg);

		        }

		        if (!commonMethods.isNull(boevo.getApprovedBy()))

		        {

		          logger.info("boevo.getApprovedBy() :: " + boevo.getApprovedBy());

		          if (boevo.getApprovedBy().equalsIgnoreCase("1"))

		          {

		            if (commonMethods.isNull(boevo.getClosureLetterNo()))

		            {

		              String errorcode = commonMethods.getErrorDesc("BEA006", "N100");

		              Object[] arg = { "0", "E", errorcode, "Input" };

		              setErrorvalues(arg);

		            }

		            if (commonMethods.isNull(boevo.getClosureLetterDate()))

		            {

		              String errorcode = commonMethods.getErrorDesc("BEA007", "N100");

		              Object[] arg = { "0", "E", errorcode, "Input" };

		              setErrorvalues(arg);

		            }

		          }

		        }

		        else if (commonMethods.isNull(boevo.getApprovedBy()))

		        {

		          String errorcode = commonMethods.getErrorDesc("BEA005", "N100");

		          Object[] arg = { "0", "E", errorcode, "Input" };

		          setErrorvalues(arg);

		        }

		        logger.info("boevo.getAdjClosureDate() :: " + boevo.getAdjClosureDate());

		        if (commonMethods.isNull(boevo.getAdjClosureDate()))

		        {

		          String errorcode = commonMethods.getErrorDesc("BEA008", "N100");

		          Object[] arg = { "0", "E", errorcode, "Input" };

		          setErrorvalues(arg);

		        }

		      }

		      int boeClCount = bd.getBoeClCount(boevo);

		      logger.info("boeClCount :: " + boeClCount);

		      if (boeClCount > 0)

		      {

		        String errorcode = commonMethods.getErrorDesc("BOE32", "N100");

		        Object[] arg = { "0", "E", errorcode, "Input" };

		        setErrorvalues(arg);

		      }

		      int mbeCount = bd.getMBEStatus(boevo);

		      logger.info("mbeCount :: " + mbeCount);

		      if (mbeCount == 0)

		      {

		        String errorcode = commonMethods.getErrorDesc("BOE33", "N100");

		        Object[] arg = { "0", "E", errorcode, "Input" };

		        setErrorvalues(arg);

		      }

		      logger.info("boevo.getBoeCloInd() :: " + boevo.getBoeCloInd());

		      if (commonMethods.isNull(boevo.getBoeCloInd()))

		      {

		        String errorcode = "BEA Closure Indicator is Mandatory";

		        Object[] arg = { "0", "E", errorcode, "Input" };

		        setErrorvalues(arg);

		      }

		    }

		    catch (Exception e)

		    {

		      e.printStackTrace();

		    }

		  }

		  public String boeExChecker()

		    throws ApplicationException

		  {

		    logger.info("--------boeExChecker----------");

		    BoeBD bd = null;

		    try

		    {

		      bd = BoeBD.getBD();

		      logger.info("boeSearchVO------Value-----" + this.boeSearchVO);

		      if (this.boeSearchVO != null) {

		        this.boeList = bd.boeExChecker(this.boeSearchVO);

		      }

		    }

		    catch (Exception e)

		    {

		      logger.info("Exception in boeExChecker----" + e);

		      throwApplicationException(e);

		    }

		    logger.info("Exiting Method");

		    return "success";

		  }

		  public String boeClosureChecker()

		    throws ApplicationException

		  {

		    logger.info("-----boeClosureChecker---");

		    BoeBD bd = null;

		    try

		    {

		      bd = BoeBD.getBD();

		      logger.info("boeSearchVO----Value---------" + this.boeSearchVO);

		      if (this.boeSearchVO != null) {

		        this.boeList = bd.boeClosureChecker(this.boeSearchVO);

		      }

		    }

		    catch (Exception e)

		    {

		      logger.info("boeClosureChecker----Exception--------" + e);

		      throwApplicationException(e);

		    }

		    logger.info("Exiting Method");

		    return "success";

		  }

		  public String boeExAuthorize()

		    throws ApplicationException

		  {

		    logger.info("boeExAuthorize");

		    BoeBD bd = null;

		    try

		    {

		      bd = BoeBD.getBD();

		      this.boeList = bd.boeExAuthorize(this.boeSearchVO, this.exChkList, this.boeExStatus, this.remarks);

		      this.remarks = "";

		    }

		    catch (Exception e)

		    {

		      logger.info("boeExAuthorize-------Exception" + e);

		      throwApplicationException(e);

		    }

		    logger.info("Exiting Method");

		    return "success";

		  }

		  public String boeClAuthorize()

		    throws ApplicationException

		  {

		    logger.info("Entering Method");

		    BoeBD bd = null;

		    try

		    {

		      bd = BoeBD.getBD();

		      this.boeList = bd.boeClosureAuthorize(this.boeSearchVO, this.clChkList, this.boeClStatus, this.remarks);

		      this.remarks = "";

		    }

		    catch (Exception e)

		    {

		      throwApplicationException(e);

		    }

		    logger.info("Exiting Method");

		    return "success";

		  }

		  public String getBoeDetailss()

		    throws ApplicationException

		  {

		    logger.info("Entering Method");

		    BoeBD bd = null;

		    try

		    {

		      bd = BoeBD.getBD();

		      this.boevo = bd.getBoeDetailss(this.boevo);

		      if (this.boevo != null) {

		        this.invoiceList = this.boevo.getInvoiceList();

		      }

		    }

		    catch (Exception e)

		    {

		      throwApplicationException(e);

		    }

		    logger.info("Exiting Method");

		    return "success";

		  }

		  public void setErrorvalues(Object[] arg)

		  {

		    logger.info("Entering Method");

		    CommonMethods commonMethods = new CommonMethods();

		    AlertMessagesVO altMsg = new AlertMessagesVO();

		    altMsg.setErrorId(commonMethods.getEmptyIfNull(arg[1])

		      .equalsIgnoreCase("W") ? "Warning" : 

		      "Error");

		    altMsg.setErrorDesc("General");

		    altMsg.setErrorCode(commonMethods.getEmptyIfNull(arg[3]));

		    altMsg.setErrorDetails(commonMethods.getEmptyIfNull(arg[2]));

		    altMsg.setErrorMsg(commonMethods.getEmptyIfNull(arg[1])

		      .equalsIgnoreCase("W") ? "N" : 

		      "Error");

		    this.alertMsgArray.add(altMsg);

		    logger.info("alertMsgArray :: " + this.alertMsgArray.toString());

		    logger.info("Exiting Method");

		  }
		  public String boeView()

				    throws ApplicationException

				  {

				    logger.info("Entering Method");

				    BoeBD bd = null;

				    try

				    {

				      bd = BoeBD.getBD();

				      this.boevo = bd.boeView(this.boeSearchVO);

				      if (this.boevo != null) {

				        this.invoiceList = this.boevo.getInvoiceList();

				      }

				    }

				    catch (Exception e)

				    {

				      throwApplicationException(e);

				    }

				    logger.info("Exiting Method");

				    return "success";

				  }

				  public String fetchPaymentDetails()

				    throws ApplicationException

				  {

				    logger.info("Entering Method");

				    BoeBD bd = null;

				    try

				    {

				      bd = BoeBD.getBD();

				      this.boeList = bd.fetchPaymentDetails(this.boeSearchVO);

				    }

				    catch (Exception e)

				    {

				      throwApplicationException(e);

				    }

				    logger.info("Exiting Method");

				    return "success";

				  }

				  public String closeWindow()

				    throws Exception

				  {

				    logger.info("Entering Method");

				    Connection con = null;

				    LoggableStatement log = null;

				    ResultSet rs = null;

				    String closeUrl = "";

				    try

				    {

				      con = DBConnectionUtility.getConnection();

				 
				      String query = "SELECT VALUE1 FROM ETT_PARAMETER_TBL WHERE PARAMETER_ID = 'closeURL'";

				      log = new LoggableStatement(con, query);

				      rs = log.executeQuery();

				      if (rs.next()) {

				        closeUrl = rs.getString("VALUE1");

				      }

				      HttpServletResponse response = (HttpServletResponse)

				        ActionContext.getContext().get("com.opensymphony.xwork2.dispatcher.HttpServletResponse");

				      response.sendRedirect(closeUrl);

				    }

				    catch (Exception exception)

				    {

				      throwApplicationException(exception);

				    }

				    finally

				    {

				      DBConnectionUtility.surrenderDB(con, log, rs);

				    }

				    logger.info("Exiting Method");

				    return "none";

				  }

				  public String boeExternalWindow()

				    throws Exception

				  {

				    logger.info("Entering Method");

				    Connection con = null;

				    LoggableStatement log = null;

				    ResultSet rs = null;

				    String closeUrl = "";

				    try

				    {

				      HttpSession session = ServletActionContext.getRequest().getSession();

				      String username = (String)session.getAttribute("loginedUserName");

				 
				      logger.info("username--username session----boeExternalWindow---------" + username);

				      con = DBConnectionUtility.getConnection();

				      String query = "SELECT VALUE1 FROM ETT_PARAMETER_TBL WHERE PARAMETER_ID = 'BOE_EXT'";

				      log = new LoggableStatement(con, query);

				      rs = log.executeQuery();

				      if (rs.next())

				      {

				        closeUrl = rs.getString("VALUE1").trim();

				        logger.info("closeUrl--query value----boeExternalWindow---------" + closeUrl);

				      }

				      closeUrl = closeUrl + "?u_id=" + username;

				      logger.info("closeUrl-------boeExternalWindow---------" + closeUrl);

				      HttpServletResponse response = (HttpServletResponse)

				        ActionContext.getContext().get("com.opensymphony.xwork2.dispatcher.HttpServletResponse");

				      response.sendRedirect(closeUrl);

				    }

				    catch (Exception exception)

				    {

				      throwApplicationException(exception);

				    }

				    finally

				    {

				      DBConnectionUtility.surrenderDB(con, log, rs);

				    }

				    logger.info("Exiting Method");

				    return "none";

				  }

				  public String getPortOfDischarge()

				  {

				    logger.info("Entering Method");

				    logger.info("Exiting Method");

				    return "success";

				  }

				  public String fetchDischargePortList()

				    throws ApplicationException

				  {

				    logger.info("Entering Method");

				    String sStatus = "fail";

				    BoeBD bd = null;

				    try

				    {

				      bd = BoeBD.getBD();

				      if (bd != null)

				      {

				        this.dsgPortList = bd.fetchDischargePortList(this.boevo);

				        if (this.dsgPortList.size() > 0) {

				          sStatus = "success";

				        } else {

				          sStatus = "fail";

				        }

				      }

				    }

				    catch (Exception e)

				    {

				      throwApplicationException(e);

				    }

				    logger.info("Exiting Method");

				    return sStatus;

				  }

				  public String portOfDischargeToBEA()

				    throws ApplicationException

				  {

				    logger.info("Entering Method");

				    BoeBD bd = null;

				    try

				    {

				      bd = BoeBD.getBD();

				      this.invoiceList = bd.fetchInvoiceList(this.boevo);

				    }

				    catch (Exception e)

				    {

				      throwApplicationException(e);

				    }

				    logger.info("Exiting Method");

				    return "success";

				  }

				}

				 