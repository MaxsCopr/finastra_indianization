package in.co.boe.action;

import in.co.boe.businessdelegate.billnotomanypaymentreference.BoeBillNoToManyPaymentReferenceBD;
import in.co.boe.businessdelegate.exception.BusinessException;
import in.co.boe.businessdelegate.pricereftomanybill.BoePriceReferenceToManyBillNoBD;
import in.co.boe.dao.exception.ApplicationException;
import in.co.boe.utility.ActionConstants;
import in.co.boe.utility.CommonMethods;
import in.co.boe.vo.AlertMessagesVO;
import in.co.boe.vo.BOEPortSelectionVO;
import in.co.boe.vo.BOESearchVO;
import in.co.boe.vo.BillTypeVO;
import in.co.boe.vo.BoeVO;
import in.co.boe.vo.TransactionVO;
import java.util.ArrayList;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
 
public class PaymentReferenceTOManyBillNoAction
  extends BoeBaseAction
{
  private static final long serialVersionUID = 1L;
  private static Logger logger = Logger.getLogger(PaymentReferenceTOManyBillNoAction.class.getName());
  ArrayList<TransactionVO> tiList = null;
  TransactionVO transactionVO = null;
  BoeVO boeVO = null;
  ArrayList<BillTypeVO> eventList = null;
  private String searchButton = null;
  String sMstRefNums = null;
  String sEvtRefNums = null;
  public String getsMstRefNums()
  {
    return this.sMstRefNums;
  }
  public void setsMstRefNums(String sMstRefNums)
  {
    this.sMstRefNums = sMstRefNums;
  }
  public String getsEvtRefNums()
  {
    return this.sEvtRefNums;
  }
  public void setsEvtRefNums(String sEvtRefNums)
  {
    this.sEvtRefNums = sEvtRefNums;
  }
  private String modifyButton = null;
  String master_reference = null;
  public String getMaster_reference()
  {
    return this.master_reference;
  }
  public void setMaster_reference(String master_reference)
  {
    this.master_reference = master_reference;
  }
  private String newButton = "Y";
  Map<String, String> gpTypeList;
  Map<String, String> importAgencyList;
  Map<String, String> recordInd;
  Map<String, String> transTypeList;
  ArrayList<TransactionVO> invoiceList = null;
  private ArrayList<AlertMessagesVO> alertMsgArray = null;
  String[] chkInvlist = null;
  String[] bill_chkList = null;
  public Map<String, String> boeBesSBIndList;
  private String invoiceSerNumber = null;
  private String invoiceNumber = null;
  ArrayList<BOEPortSelectionVO> portList = null;
  ArrayList<BoeVO> boeList = null;
  ArrayList<BoeVO> dsgPortList = null;
  ArrayList<BoeVO> ormList = null;
  BOESearchVO boeSearchVO = null;
  public ArrayList<BoeVO> getBoeList()
  {
    return this.boeList;
  }
  public void setBoeList(ArrayList<BoeVO> boeList)
  {
    this.boeList = boeList;
  }
  public String[] getBill_chkList()
  {
    return this.bill_chkList;
  }
  public void setBill_chkList(String[] bill_chkList)
  {
    this.bill_chkList = bill_chkList;
  }
  public BOESearchVO getBoeSearchVO()
  {
    return this.boeSearchVO;
  }
  public void setBoeSearchVO(BOESearchVO boeSearchVO)
  {
    this.boeSearchVO = boeSearchVO;
  }
  public ArrayList<BOEPortSelectionVO> getPortList()
  {
    return this.portList;
  }
  public void setPortList(ArrayList<BOEPortSelectionVO> portList)
  {
    this.portList = portList;
  }
  public ArrayList<BoeVO> getDsgPortList()
  {
    return this.dsgPortList;
  }
  public void setDsgPortList(ArrayList<BoeVO> dsgPortList)
  {
    this.dsgPortList = dsgPortList;
  }
  public String getInvoiceSerNumber()
  {
    return this.invoiceSerNumber;
  }
  public void setInvoiceSerNumber(String invoiceSerNumber)
  {
    this.invoiceSerNumber = invoiceSerNumber;
  }
  public String getInvoiceNumber()
  {
    return this.invoiceNumber;
  }
  public void setInvoiceNumber(String invoiceNumber)
  {
    this.invoiceNumber = invoiceNumber;
  }
  public Map<String, String> getBoeBesSBIndList()
  {
    return ActionConstants.BESSB_IND;
  }
  public void setBoeBesSBIndList(Map<String, String> boeBesSBIndList)
  {
    this.boeBesSBIndList = boeBesSBIndList;
  }
  public Map<String, String> getImportAgencyList()
  {
    return ActionConstants.IMPORT_AGENCY;
  }
  public void setImportAgencyList(Map<String, String> importAgencyList)
  {
    this.importAgencyList = importAgencyList;
  }
  public Map<String, String> getRecordInd()
  {
    return ActionConstants.RECORD_IND;
  }
  public void setRecordInd(Map<String, String> recordInd)
  {
    this.recordInd = recordInd;
  }
  public String[] getChkInvlist()
  {
    return this.chkInvlist;
  }
  public void setChkInvlist(String[] chkInvlist)
  {
    this.chkInvlist = chkInvlist;
  }
  public ArrayList<AlertMessagesVO> getAlertMsgArray()
  {
    return this.alertMsgArray;
  }
  public void setAlertMsgArray(ArrayList<AlertMessagesVO> alertMsgArray)
  {
    this.alertMsgArray = alertMsgArray;
  }
  public ArrayList<TransactionVO> getInvoiceList()
  {
    return this.invoiceList;
  }
  public void setInvoiceList(ArrayList<TransactionVO> invoiceList)
  {
    this.invoiceList = invoiceList;
  }
  public Map<String, String> getTransTypeList()
  {
    return ActionConstants.TRANS_TYPE;
  }
  public void setTransTypeList(Map<String, String> transTypeList)
  {
    this.transTypeList = transTypeList;
  }
  public Map<String, String> getGpTypeList()
  {
    return ActionConstants.FIRM_TYPE;
  }
  public void setGpTypeList(Map<String, String> gpTypeList)
  {
    this.gpTypeList = gpTypeList;
  }
  public String getNewButton()
  {
    return this.newButton;
  }
  public void setNewButton(String newButton)
  {
    this.newButton = newButton;
  }
  public String getSearchButton()
  {
    return this.searchButton;
  }
  public void setSearchButton(String searchButton)
  {
    this.searchButton = searchButton;
  }
  public ArrayList<TransactionVO> getTiList()
  {
    return this.tiList;
  }
  public void setTiList(ArrayList<TransactionVO> tiList)
  {
    this.tiList = tiList;
  }
  public TransactionVO getTransactionVO()
  {
    return this.transactionVO;
  }
  public void setTransactionVO(TransactionVO transactionVO)
  {
    this.transactionVO = transactionVO;
  }
  public BoeVO getBoeVO()
  {
    return this.boeVO;
  }
  public void setBoeVO(BoeVO boeVO)
  {
    this.boeVO = boeVO;
  }
  public ArrayList<BillTypeVO> getEventList()
  {
    return this.eventList;
  }
  public void setEventList(ArrayList<BillTypeVO> eventList)
  {
    this.eventList = eventList;
  }
  public ArrayList<BoeVO> getOrmList()
  {
    return this.ormList;
  }
  public void setOrmList(ArrayList<BoeVO> ormList)
  {
    this.ormList = ormList;
  }
  public String landingPage()
    throws ApplicationException
  {
    logger.info("landingPage----------------landingPage---------");
    BoeBillNoToManyPaymentReferenceBD boeBD = null;
    try
    {
      logger.info("sMstRefNums------------>" + this.sMstRefNums);
      logger.info("sEvtRefNums ------------>" + this.sEvtRefNums);
      HttpSession session = ServletActionContext.getRequest().getSession();
      String master_reference = (String)session.getAttribute("xMstRefNum");
      logger.info("Master Reference nUmber----------->" + master_reference);
      boeBD = new BoeBillNoToManyPaymentReferenceBD();

 
 
      this.eventList = boeBD.fetchPayRef(this.boeVO);
      logger.info("eventList.size() : " + this.eventList.size());
      if ((this.eventList != null) && (this.eventList.size() > 0)) {
        this.searchButton = "Y";
      }
    }
    catch (Exception e)
    {
      logger.info(" landingPage Error here ---------" + e.getMessage());
    }
    logger.info("Exiting Method");
    return "success";
  }
  public String retriveDataFromTI()
    throws ApplicationException
  {
    logger.info("---------------retriveDataFromTI---------------");
    BoePriceReferenceToManyBillNoBD boeManyBillNoBD = null;
    BoeBillNoToManyPaymentReferenceBD boeManyPaymentBD = null;
    CommonMethods commonMethods = null;
    try
    {
      commonMethods = new CommonMethods();
      boeManyBillNoBD = BoePriceReferenceToManyBillNoBD.getBD();
      if (this.boeVO != null)
      {
        boeManyPaymentBD = BoeBillNoToManyPaymentReferenceBD.getBD();
        this.boeVO = boeManyBillNoBD.retriveDataFromTI(this.boeVO);
        logger.info("TI---List Before");
        this.tiList = boeManyBillNoBD.fetchPaymentReferenceDataList(this.boeVO);
        logger.info("TI---List After");
        if ((!commonMethods.isNull(this.boeVO.getFullyAlloc())) && 
          (this.boeVO.getFullyAlloc().equalsIgnoreCase("N"))) {
          this.newButton = "N";
        }
      }
      this.eventList = boeManyPaymentBD.fetchPayRef(this.boeVO);
      if ((this.eventList != null) && (this.eventList.size() > 0)) {
        this.searchButton = "Y";
      }
    }
    catch (BusinessException exception)
    {
      logger.info("---------------retriveDataFromTI--------exception-------" + exception);
      exception.printStackTrace();
    }
    logger.info("Exiting Method");
    return "success";
  }
  
  public String getDataFromTIForSecond()
		    throws ApplicationException
		  {
		    logger.info("-------------------getDataFromTIForSecond-------------------");
		    BoePriceReferenceToManyBillNoBD boeManyBillNoBD = null;
		    BoeBillNoToManyPaymentReferenceBD boeManyPaymentBD = null;
		    try
		    {
		      if (this.boeVO != null)
		      {
		        boeManyBillNoBD = BoePriceReferenceToManyBillNoBD.getBD();
		        boeManyPaymentBD = BoeBillNoToManyPaymentReferenceBD.getBD();
		        if ((this.boeVO.getBtnModify() != null) && (this.boeVO.getBtnModify().equalsIgnoreCase("M")))
		        {
		          logger.info("This is inside of Modify condition table");
		          this.boeVO = boeManyBillNoBD.retriveDataFromPayTable(this.boeVO);
		          if ((this.boeVO.getInvoiceList() != null) && (!this.boeVO.getInvoiceList().isEmpty())) {
		            this.invoiceList = this.boeVO.getInvoiceList();
		          }
		          logger.info("Return to Modify Page");
		        }
		        else
		        {
		          this.boeVO = boeManyBillNoBD.retriveDataFromTI(this.boeVO);
		        }
		        if ((this.boeVO.getOutAmt() != null) && (this.boeVO.getOutAmt().equalsIgnoreCase("0")))
		        {
		          this.boeVO.setOutAmt(this.boeVO.getPaymentAmount());
		          this.boeVO.setOutAmt_temp(this.boeVO.getPaymentAmount());
		        }
		        else
		        {
		          this.boeVO.setOutAmt(this.boeVO.getOutAmt());
		          this.boeVO.setOutAmt_temp(this.boeVO.getOutAmt());
		        }
		        this.boeVO.setFullyAlloc_temp(this.boeVO.getFullyAlloc());
		        this.boeVO.setInitEndorseAmt(this.boeVO.getEndorseAmt());
		        this.boeVO.setEndorseAmt_temp(this.boeVO.getEndorseAmt());
		      }
		      this.eventList = boeManyPaymentBD.fetchPayRef(this.boeVO);
		      if ((this.eventList != null) && (this.eventList.size() > 0)) {
		        this.searchButton = "N";
		      }
		    }
		    catch (BusinessException exception)
		    {
		      logger.info("-------------------getDataFromTIForSecond---BusinessException exception----------------" + exception);
		      throwApplicationException(exception);
		    }
		    catch (Exception exception)
		    {
		      logger.info("-------------------getDataFromTIForSecond--- exception----------------" + exception);
		      throwApplicationException(exception);
		    }
		    logger.info("Exiting Method");
		    return "success";
		  }
		  public String boe_modification()
		    throws ApplicationException
		  {
		    logger.info("boe_modification------------");
		    BoePriceReferenceToManyBillNoBD boeManyBillNoBD = null;
		    try
		    {
		      if (this.bill_chkList != null) {
		        for (int i = 0; i < this.bill_chkList.length; i++)
		        {
		          String a = this.bill_chkList[i];
		          String[] b = a.split(":");
		          logger.info("111111111111111--------------->" + b[0]);
		          logger.info("111111111111111--------------->" + b[1]);
		          logger.info("111111111111111--------------->" + b[2]);
		          logger.info("111111111111111--------------->" + b[3]);
		          logger.info("111111111111111--------------->" + b[4]);
		          this.boeVO.setPaymentRefNo(b[0]);
		          this.boeVO.setEventRefno(b[1]);
		          this.boeVO.setBoeNo(b[2]);
		          this.boeVO.setBoeDate(b[3]);
		          this.boeVO.setPortCode(b[4]);
		        }
		      }
		      boeManyBillNoBD = BoePriceReferenceToManyBillNoBD.getBD();
		      if (this.boeVO != null)
		      {
		        this.boeVO = boeManyBillNoBD.retriveDataBasedOnBillNO1(this.boeVO);
		        if ((this.boeVO.getInvoiceList() != null) && (!this.boeVO.getInvoiceList().isEmpty())) {
		          this.invoiceList = this.boeVO.getInvoiceList();
		        }
		      }
		    }
		    catch (BusinessException exception)
		    {
		      logger.info("boe_modification----------exception--" + exception);
		      throwApplicationException(exception);
		    }
		    logger.info("Exiting Method");
		    return "success";
		  }
		  public String retriveDataBasedOnBillNO()
		    throws ApplicationException
		  {
		    logger.info("retriveDataBasedOnBillNO-------------------");
		    BoePriceReferenceToManyBillNoBD boeManyBillNoBD = null;
		    try
		    {
		      boeManyBillNoBD = BoePriceReferenceToManyBillNoBD.getBD();
		      if (this.boeVO != null)
		      {
		        this.boeVO = boeManyBillNoBD.retriveDataBasedOnBillNO(this.boeVO);
		        if ((this.boeVO.getInvoiceList() != null) && (!this.boeVO.getInvoiceList().isEmpty())) {
		          this.invoiceList = this.boeVO.getInvoiceList();
		        }
		      }
		    }
		    catch (BusinessException exception)
		    {
		      logger.info("retriveDataBasedOnBillNO------exception-------------" + exception);
		      throwApplicationException(exception);
		    }
		    logger.info("Exiting Method");
		    return "success";
		  }
		  public String fetchAllocateInvoice()
		    throws ApplicationException
		  {
		    logger.info("fetchAllocateInvoice--------------------");
		    BoePriceReferenceToManyBillNoBD boeManyBillNoBD = null;
		    try
		    {
		      boeManyBillNoBD = BoePriceReferenceToManyBillNoBD.getBD();
		      if (this.boeVO != null)
		      {
		        this.boeVO = boeManyBillNoBD.fetchAllocateInvoice(this.boeVO);
		        if ((this.boeVO.getInvoiceList() != null) && (!this.boeVO.getInvoiceList().isEmpty())) {
		          this.invoiceList = this.boeVO.getInvoiceList();
		        }
		      }
		    }
		    catch (BusinessException exception)
		    {
		      logger.info("fetchAllocateInvoice--------------exception------" + exception);
		      throwApplicationException(exception);
		    }
		    logger.info("Exiting Method");
		    return "success";
		  }
		  
		  public String storeBOEData()
				    throws ApplicationException
				  {
				    logger.info("Entering Method");
				    logger.info("--------------BOE----storeBOEData---------");
				    BoePriceReferenceToManyBillNoBD boeManyBillNoBD = null;
				    BoeBillNoToManyPaymentReferenceBD boeManyPaymentBD = null;
				    CommonMethods commonMethods = null;
				    String target = null;
				    try
				    {
				      boeManyBillNoBD = BoePriceReferenceToManyBillNoBD.getBD();
				      boeManyPaymentBD = BoeBillNoToManyPaymentReferenceBD.getBD();
				      commonMethods = new CommonMethods();
				      logger.info("The Modification Flag is : " + this.boeVO.getBtnModify());
				      int iRet = parseChkList(this.chkInvlist);
				      HttpSession session = ServletActionContext.getRequest().getSession();

				 
				 
				      String master_ref_no = (String)session.getAttribute("xMstRefNum");
				      logger.info("master_ref_no------------------");
				      if (this.boeVO != null) {
				        validateData(this.boeVO);
				      }
				      if (this.alertMsgArray.size() == 0)
				      {
				        String result = boeManyBillNoBD.storeBillData(this.chkInvlist, this.boeVO);
				        logger.info("Store Data--->" + result);
				        if (result != null)
				        {
				          logger.info("BOE & Master reference number not null inside and getting the data back ");
				          boeManyPaymentBD = BoeBillNoToManyPaymentReferenceBD.getBD();
				          this.boeVO = boeManyBillNoBD.retriveDataFromTI(this.boeVO);
				          this.tiList = boeManyBillNoBD.fetchPaymentReferenceDataList(this.boeVO);
				          if ((!commonMethods.isNull(this.boeVO.getFullyAlloc())) && 
				            (this.boeVO.getFullyAlloc().equalsIgnoreCase("N"))) {
				            this.newButton = "N";
				          }
				        }
				        this.eventList = boeManyPaymentBD.fetchPayRef(this.boeVO);
				        if ((this.eventList != null) && (this.eventList.size() > 0)) {
				          this.searchButton = "Y";
				        }
				        if (!commonMethods.isNull(this.boeVO.getOkIdFlg()))
				        {
				          this.boeSearchVO.setPaymentRefNo("");
				          this.boeSearchVO.setPaymentSerialNo("");
				          this.boeSearchVO.setBoeNo("");
				          this.boeSearchVO.setPaymentCurrency("");
				          target = "reject";
				        }
				        else
				        {
				          target = "success";
				        }
				      }
				      else if ((this.alertMsgArray.size() == 1) || (this.alertMsgArray.size() == 2))
				      {
				        String errorMsg = ((AlertMessagesVO)this.alertMsgArray.get(0)).getErrorMsg();
				        if ((errorMsg != null) && (errorMsg.equalsIgnoreCase("Y")))
				        {
				          String result = boeManyBillNoBD.storeBillData(this.chkInvlist, this.boeVO);
				          logger.info("Store Data--->" + result);
				          logger.info("Store Data--->" + result);
				          if (this.boeVO != null)
				          {
				            boeManyPaymentBD = BoeBillNoToManyPaymentReferenceBD.getBD();
				            this.boeVO = boeManyBillNoBD.retriveDataFromTI(this.boeVO);
				            this.tiList = boeManyBillNoBD.fetchPaymentReferenceDataList(this.boeVO);
				            if ((!commonMethods.isNull(this.boeVO.getFullyAlloc())) && 
				              (this.boeVO.getFullyAlloc().equalsIgnoreCase("N"))) {
				              this.newButton = "N";
				            }
				          }
				          this.eventList = boeManyPaymentBD.fetchPayRef(this.boeVO);
				          if ((this.eventList != null) && (this.eventList.size() > 0)) {
				            this.searchButton = "Y";
				          }
				          if (!commonMethods.isNull(this.boeVO.getOkIdFlg()))
				          {
				            this.boeSearchVO.setPaymentRefNo("");
				            this.boeSearchVO.setPaymentSerialNo("");
				            this.boeSearchVO.setBoeNo("");
				            this.boeSearchVO.setPaymentCurrency("");
				            target = "reject";
				          }
				          else
				          {
				            target = "success";
				          }
				        }
				        else
				        {
				          logger.info("Else with single error");
				          this.boeVO = boeManyBillNoBD.fetchAllocateInvoice(this.boeVO);
				          if ((this.boeVO.getInvoiceList() != null) && (!this.boeVO.getInvoiceList().isEmpty()))
				          {
				            this.invoiceList = this.boeVO.getInvoiceList();
				            this.invoiceList = replaceInvoiceDataIfManuallyEdited(this.chkInvlist, this.invoiceList);
				          }
				          target = "error";
				        }
				      }
				      else
				      {
				        logger.info("Else with multiple error");
				        this.boeVO = boeManyBillNoBD.fetchAllocateInvoice(this.boeVO);
				        if ((this.boeVO.getInvoiceList() != null) && (!this.boeVO.getInvoiceList().isEmpty()))
				        {
				          this.invoiceList = this.boeVO.getInvoiceList();
				          this.invoiceList = replaceInvoiceDataIfManuallyEdited(this.chkInvlist, this.invoiceList);
				        }
				        target = "error";
				      }
				    }
				    catch (Exception e)
				    {
				      e.printStackTrace();
				      logger.info("Exception in Store BOE Data---> " + e.getMessage());
				      logger.info("Exception in Store BOE Data---> " + e.getMessage());
				    }
				    logger.info("Exiting Method");
				    return target;
				  }
				  public String invoiceData()
				    throws ApplicationException
				  {
				    logger.info("invoiceData---------------");
				    BoePriceReferenceToManyBillNoBD boeManyBillNoBD = null;
				    CommonMethods commonMethods = null;
				    try
				    {
				      boeManyBillNoBD = BoePriceReferenceToManyBillNoBD.getBD();
				      commonMethods = new CommonMethods();
				      if (this.boeVO != null)
				      {
				        String buttonType = commonMethods.getEmptyIfNull(this.boeVO.getButtonType()).trim();
				        if ((!commonMethods.isNull(buttonType)) && (buttonType.equalsIgnoreCase("View"))) {
				          this.transactionVO = boeManyBillNoBD.fetchInvoiceData(this.boeVO);
				        }
				      }
				    }
				    catch (Exception exception)
				    {
				      logger.info("invoiceData------------exception---" + exception);
				      throwApplicationException(exception);
				    }
				    logger.info("Exiting Method");
				    return "success";
				  }
				  public void invoiceDetailsValidation(TransactionVO transactionVO)
				    throws ApplicationException
				  {
				    CommonMethods commonMethods = null;
				    try
				    {
				      this.alertMsgArray = new ArrayList();
				      commonMethods = new CommonMethods();
				      if ((this.alertMsgArray != null) && 
				        (this.alertMsgArray.size() > 0)) {
				        this.alertMsgArray.clear();
				      }
				      if (commonMethods.isNull(this.boeVO.getBoeNo()))
				      {
				        String errorcode = commonMethods.getErrorDesc("INV01", "N100");
				        Object[] arg = { "0", "E", errorcode, "Input" };
				        setErrorvalues(arg);
				      }
				      if (commonMethods.isNull(this.boeVO.getBoeDate()))
				      {
				        String errorcode = commonMethods.getErrorDesc("INV02", "N100");
				        Object[] arg = { "0", "E", errorcode, "Input" };
				        setErrorvalues(arg);
				      }
				      if (commonMethods.isNull(transactionVO.getInvoiceNumber()))
				      {
				        String errorcode = commonMethods.getErrorDesc("INV03", "N100");
				        Object[] arg = { "0", "E", errorcode, "Input" };
				        setErrorvalues(arg);
				      }
				      if (commonMethods.isNull(transactionVO.getInvSno()))
				      {
				        String errorcode = commonMethods.getErrorDesc("INV04", "N100");
				        Object[] arg = { "0", "E", errorcode, "Input" };
				        setErrorvalues(arg);
				      }
				      if (commonMethods.isNull(transactionVO.getInvAmt()))
				      {
				        String errorcode = commonMethods.getErrorDesc("INV05", "N100");
				        Object[] arg = { "0", "E", errorcode, "Input" };
				        setErrorvalues(arg);
				      }
				      if (commonMethods.isNull(transactionVO.getInvoiceCurr()))
				      {
				        String errorcode = commonMethods.getErrorDesc("INV06", "N100");
				        Object[] arg = { "0", "E", errorcode, "Input" };
				        setErrorvalues(arg);
				      }
				      if (commonMethods.isNull(transactionVO.getSupplierName()))
				      {
				        String errorcode = commonMethods.getErrorDesc("INV10", "N100");
				        Object[] arg = { "0", "E", errorcode, "Input" };
				        setErrorvalues(arg);
				      }
				      if (commonMethods.isNull(transactionVO.getSupplierAddress()))
				      {
				        String errorcode = commonMethods.getErrorDesc("INV11", "N100");
				        Object[] arg = { "0", "E", errorcode, "Input" };
				        setErrorvalues(arg);
				      }
				      if (commonMethods.isNull(transactionVO.getSupplierCountry()))
				      {
				        String errorcode = commonMethods.getErrorDesc("INV12", "N100");
				        Object[] arg = { "0", "E", errorcode, "Input" };
				        setErrorvalues(arg);
				      }
				      if (commonMethods.isNull(transactionVO.getTermsofInvoice()))
				      {
				        String errorcode = commonMethods.getErrorDesc("INV08", "N100");
				        Object[] arg = { "0", "E", errorcode, "Input" };
				        setErrorvalues(arg);
				      }
				    }
				    catch (Exception e)
				    {
				      throwApplicationException(e);
				    }
				  }
				  
				  public void setErrorvalues(Object[] arg)
						    throws ApplicationException
						  {
						    logger.info("Entering Method");
						    for (int i = 0; i < arg.length; i++) {
						      logger.info("The Error Value is : " + arg[i]);
						    }
						    try
						    {
						      CommonMethods commonMethods = new CommonMethods();
						      AlertMessagesVO altMsg = new AlertMessagesVO();
						      altMsg.setErrorId(commonMethods.getEmptyIfNull(arg[1]).equalsIgnoreCase("W") ? 
						        "Warning" : "Error");
						      altMsg.setErrorDesc("General");
						      altMsg.setErrorCode(commonMethods.getEmptyIfNull(arg[3]));
						      altMsg.setErrorDetails(commonMethods.getEmptyIfNull(arg[2]));
						      altMsg.setErrorMsg(commonMethods.getEmptyIfNull(arg[1]).equalsIgnoreCase("W") ? "N" : "");
						      this.alertMsgArray.add(altMsg);
						    }
						    catch (Exception exception)
						    {
						      throwApplicationException(exception);
						    }
						    logger.info("Exiting Method");
						  }
						  public String backBOEData()
						    throws ApplicationException
						  {
						    logger.info("-----------backBOEData-----------");
						    BoePriceReferenceToManyBillNoBD boeManyBillNoBD = null;
						    try
						    {
						      boeManyBillNoBD = BoePriceReferenceToManyBillNoBD.getBD();
						      if (this.boeVO != null)
						      {
						        this.boeVO = boeManyBillNoBD.fetchAllocateInvoice(this.boeVO);
						        if ((this.boeVO.getInvoiceList() != null) && (!this.boeVO.getInvoiceList().isEmpty())) {
						          this.invoiceList = this.boeVO.getInvoiceList();
						        }
						      }
						    }
						    catch (Exception exception)
						    {
						      logger.info("-----------backBOEData----exception-------" + exception);
						      throwApplicationException(exception);
						    }
						    logger.info("Exiting Method");
						    return "success";
						  }
						  public String crossCurrencyPage()
						    throws ApplicationException
						  {
						    logger.info("Entering Method");
						    BoePriceReferenceToManyBillNoBD boeManyBillNoBD = null;
						    try
						    {
						      boeManyBillNoBD = BoePriceReferenceToManyBillNoBD.getBD();
						      this.transactionVO = boeManyBillNoBD.getInvoiceDetails(this.boeVO);
						    }
						    catch (Exception e)
						    {
						      e.printStackTrace();
						      throwApplicationException(e);
						    }
						    logger.info("Exiting Method");
						    return "success";
						  }
						  public String updateCrossCurrencyValues()
						    throws ApplicationException
						  {
						    logger.info("---------------updateCrossCurrencyValues---------------");
						    BoePriceReferenceToManyBillNoBD boeManyBillNoBD = null;
						    String target = "error";
						    try
						    {
						      boeManyBillNoBD = BoePriceReferenceToManyBillNoBD.getBD();
						      this.transactionVO = boeManyBillNoBD.updateCrossCurrencyData(this.boeVO, this.transactionVO);
						      if (this.boeVO != null)
						      {
						        this.boeVO = boeManyBillNoBD.revertBOEDetails(this.boeVO);
						        if ((this.boeVO.getInvoiceList() != null) && (!this.boeVO.getInvoiceList().isEmpty())) {
						          this.invoiceList = this.boeVO.getInvoiceList();
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
						  public void validateData(BoeVO boeVO)
						    throws ApplicationException
						  {
						    logger.info("Entering Method");
						    CommonMethods commonMethods = null;
						    BoePriceReferenceToManyBillNoBD boeManyBillNoBD = null;
						    try
						    {
						      this.alertMsgArray = new ArrayList();
						      commonMethods = new CommonMethods();
						      boeManyBillNoBD = new BoePriceReferenceToManyBillNoBD();
						      if ((this.alertMsgArray != null) && 
						        (this.alertMsgArray.size() > 0)) {
						        this.alertMsgArray.clear();
						      }
						      HttpSession session = ServletActionContext.getRequest().getSession();
						      String loginType = (String)session.getAttribute("loginType");
						      String dirAmount = (String)session.getAttribute("dirAmount");
						      if ((!commonMethods.isNull(loginType)) && (loginType.equalsIgnoreCase("Yes"))) {
						        if (!commonMethods.isNull(boeVO.getPaymentAmount()))
						        {
						          double paymentAmt_DB = commonMethods.convertToDouble(boeVO.getPaymentAmount());
						          double dirAmount_URL = commonMethods.convertToDouble(dirAmount);
						          if (paymentAmt_DB != dirAmount_URL)
						          {
						            String errorcode = commonMethods.getErrorDesc("BOE34", "N100");
						            Object[] arg = { "0", "E", errorcode, "Input" };
						            setErrorvalues(arg);
						          }
						        }
						      }
						      if (commonMethods.isNull(boeVO.getAdCode()))
						      {
						        String errorcode = commonMethods.getErrorDesc("BOEAD1", "N100");
						        Object[] arg = { "0", "E", errorcode, "Input" };
						        setErrorvalues(arg);
						      }
						      if (commonMethods.isNull(boeVO.getImAgency()))
						      {
						        String errorcode = commonMethods.getErrorDesc("BOE16", "N100");
						        Object[] arg = { "0", "E", errorcode, "Input" };
						        setErrorvalues(arg);
						      }
						      if (commonMethods.isNull(boeVO.getPos()))
						      {
						        String errorcode = commonMethods.getErrorDesc("BOE17", "N100");
						        Object[] arg = { "0", "E", errorcode, "Input" };
						        setErrorvalues(arg);
						      }
						      if (commonMethods.isNull(boeVO.getRecInd()))
						      {
						        String errorcode = commonMethods.getErrorDesc("BOE18", "N100");
						        Object[] arg = { "0", "E", errorcode, "Input" };
						        setErrorvalues(arg);
						      }
						      if (commonMethods.isNull(boeVO.getBoeNo()))
						      {
						        String errorcode = commonMethods.getErrorDesc("BOE01", "N100");
						        Object[] arg = { "0", "E", errorcode, "Input" };
						        setErrorvalues(arg);
						      }
						      if (commonMethods.isNull(boeVO.getBoeDate()))
						      {
						        String errorcode = commonMethods.getErrorDesc("BOE02", "N100");
						        Object[] arg = { "0", "E", errorcode, "Input" };
						        setErrorvalues(arg);
						      }
						      if (commonMethods.isNull(boeVO.getCifNo()))
						      {
						        String errorcode = commonMethods.getErrorDesc("BOE03", "N100");
						        Object[] arg = { "0", "W", errorcode, "Input" };
						        setErrorvalues(arg);
						      }
						      if (commonMethods.isNull(boeVO.getIeCode()))
						      {
						        String errorcode = commonMethods.getErrorDesc("BOE04", "N100");
						        Object[] arg = { "0", "E", errorcode, "Input" };
						        setErrorvalues(arg);
						      }
						      else
						      {
						        int ieCodeCount = boeManyBillNoBD.isIECodeCount(boeVO);
						        if (ieCodeCount == 0)
						        {
						          String errorcode = commonMethods.getErrorDesc("BOE13", "N100");
						          Object[] arg = { "0", "W", errorcode, "Input" };
						          setErrorvalues(arg);
						        }
						      }
						      if (commonMethods.isNull(boeVO.getBillCurrency()))
						      {
						        String errorcode = commonMethods.getErrorDesc("BOE05", "N100");
						        Object[] arg = { "0", "E", errorcode, "Input" };
						        setErrorvalues(arg);
						      }
						      if (commonMethods.isNull(boeVO.getBillAmt()))
						      {
						        String errorcode = commonMethods.getErrorDesc("BOE06", "N100");
						        Object[] arg = { "0", "E", errorcode, "Input" };
						        setErrorvalues(arg);
						      }
						      if (commonMethods.isNull(boeVO.getEndorseAmt()))
						      {
						        String errorcode = commonMethods.getErrorDesc("BOE10", "N100");
						        Object[] arg = { "0", "E", errorcode, "Input" };
						        setErrorvalues(arg);
						      }
						      if (commonMethods.isNull(boeVO.getAllocAmt()))
						      {
						        String errorcode = commonMethods.getErrorDesc("BOE07", "N100");
						        Object[] arg = { "0", "E", errorcode, "Input" };
						        setErrorvalues(arg);
						      }
						      if (commonMethods.isNull(boeVO.getEqPaymentAmount()))
						      {
						        String errorcode = commonMethods.getErrorDesc("BOE24", "N100");
						        Object[] arg = { "0", "E", errorcode, "Input" };
						        setErrorvalues(arg);
						      }
						      else
						      {
						        double boeAllocAmt = commonMethods.convertToDouble(boeVO.getEqPaymentAmount());
						        if (boeAllocAmt == 0.0D)
						        {
						          String errorcode = commonMethods.getErrorDesc("BOE24", "N100");
						          Object[] arg = { "0", "E", errorcode, "Input" };
						          setErrorvalues(arg);
						        }
						      }
						      if (!commonMethods.isNull(boeVO.getAllocAmt()))
						      {
						        double allocAmt = commonMethods.convertToDouble(commonMethods.toDouble(boeVO.getAllocAmt()));
						        double outStandingAmount = commonMethods
						          .convertToDouble(commonMethods.toDouble(boeVO.getOutAmt_temp()));
						        double closedAmt = boeManyBillNoBD
						          .getOrmClosedAmt(commonMethods.getEmptyIfNull(boeVO.getPaymentRefNo()).trim() + 
						          commonMethods.getEmptyIfNull(boeVO.getPartPaymentSlNo()));
						        double balanceAmt = outStandingAmount - closedAmt;
						        if (balanceAmt < 0.0D)
						        {
						          String errorcode = commonMethods.getErrorDesc("BOE20", "N100");
						          Object[] arg = { "0", "E", errorcode, "Input" };
						          setErrorvalues(arg);
						        }
						        else if (allocAmt > balanceAmt)
						        {
						          String errorcode = commonMethods.getErrorDesc("BOE23", "N100");
						          Object[] arg = { "0", "E", errorcode, "Input" };
						          setErrorvalues(arg);
						        }
						      }
						      if (commonMethods.isNull(boeVO.getBoeExRate()))
						      {
						        String errorcode = commonMethods.getErrorDesc("BOE26", "N100");
						        Object[] arg = { "0", "E", errorcode, "Input" };
						        setErrorvalues(arg);
						      }
						      else
						      {
						        double exRate = commonMethods.convertToDouble(boeVO.getBoeExRate());
						        if (exRate == 0.0D)
						        {
						          String errorcode = commonMethods.getErrorDesc("BOE26", "N100");
						          Object[] arg = { "0", "E", errorcode, "Input" };
						          setErrorvalues(arg);
						        }
						      }
						      boolean crossCurrencyFlg = false;
						      String sInvNumber = getInvoiceNumber();
						      String sInvSerNum = getInvoiceSerNumber();
						      boeVO.setInvoiceSerialNumber(sInvSerNum);
						      boeVO.setInvoiceNumber(sInvNumber);
						      if ((boeVO.getRecInd() != null) && 
						        (boeVO.getRecInd().equalsIgnoreCase("3")))
						      {
						        String errCode = commonMethods.getErrorDesc("BOEAD3", "N100");
						        Object[] arg = { "0", "E", errCode, "Input" };
						        setErrorvalues(arg);
						      }
						      int mbeCount = boeManyBillNoBD.getMBEStatus(boeVO);
						      if (mbeCount == 0)
						      {
						        String errorcode = commonMethods.getErrorDesc("BOE33", "N100");
						        Object[] arg = { "0", "E", errorcode, 
						          "Input" };
						        setErrorvalues(arg);
						      }
						      if (commonMethods.isNull(boeVO.getPortCode()))
						      {
						        String errorcode = commonMethods.getErrorDesc("BOE15", "N100");
						        Object[] arg = { "0", "E", errorcode, "Input" };
						        setErrorvalues(arg);
						      }
						      else
						      {
						        int portCodeCount = boeManyBillNoBD
						          .validatePortCode(commonMethods.getEmptyIfNull(boeVO.getPortCode()).trim());
						        if (portCodeCount == 0)
						        {
						          String errorcode = "Invalid Port Code";
						          Object[] arg = { "0", "E", errorcode, "Input" };
						          setErrorvalues(arg);
						        }
						      }
						      if ((!commonMethods.isNull(boeVO.getIeCodeChange())) && 
						        (boeVO.getIeCodeChange().length() != 10))
						      {
						        String errorcode = "Changed IE Code Should be equal to Ten Characters";
						        Object[] arg = { "0", "E", errorcode, "Input" };
						        setErrorvalues(arg);
						      }
						      if (commonMethods.isNull(boeVO.getBoeBesSBInd()))
						      {
						        String errorcode = "BES Record Indicator is Mandatory ";
						        Object[] arg = { "0", "E", errorcode, "Input" };
						        setErrorvalues(arg);
						      }
						      for (int a = 0; a < this.alertMsgArray.size(); a++) {
						        if (((AlertMessagesVO)this.alertMsgArray.get(a)).getErrorMsg().equalsIgnoreCase("N")) {
						          if ((boeVO.getOverridStatus() != null) && 
						            (boeVO.getOverridStatus().equalsIgnoreCase("Y")))
						          {
						            ((AlertMessagesVO)this.alertMsgArray.get(a)).setErrorMsg("Y");
						            boeVO.setOverridStatus("Y");
						          }
						          else if ((boeVO.getOverridStatus() != null) && 
						            (boeVO.getOverridStatus().equalsIgnoreCase("N")))
						          {
						            ((AlertMessagesVO)this.alertMsgArray.get(a)).setErrorMsg("N");
						            boeVO.setOverridStatus("N");
						          }
						        }
						      }
						    }
						    catch (Exception exception)
						    {
						      exception.printStackTrace();
						    }
						  }
						  public String updateWarning()
						    throws ApplicationException
						  {
						    logger.info("updateWarning-----------");
						    BoePriceReferenceToManyBillNoBD boeManyBillNoBD = null;
						    try
						    {
						      boeManyBillNoBD = BoePriceReferenceToManyBillNoBD.getBD();
						      if (this.boeVO != null) {
						        validateData(this.boeVO);
						      }
						      this.boeVO = boeManyBillNoBD.fetchAllocateInvoice(this.boeVO);
						      if ((this.boeVO.getInvoiceList() != null) && (!this.boeVO.getInvoiceList().isEmpty()))
						      {
						        this.invoiceList = this.boeVO.getInvoiceList();
						        this.invoiceList = replaceInvoiceDataIfManuallyEdited(this.chkInvlist, this.invoiceList);
						      }
						    }
						    catch (Exception e)
						    {
						      logger.info("updateWarning-----exception------" + e);
						      e.printStackTrace();
						    }
						    return "success";
						  }
						  public ArrayList<TransactionVO> replaceInvoiceDataIfManuallyEdited(String[] chkInvlist, ArrayList<TransactionVO> invoiceList)
						  {
						    logger.info("invoiceList - " + invoiceList);
						    logger.info("chkInvlist - " + chkInvlist);
						    for (int k = 0; k < chkInvlist.length; k++) {
						      if ((chkInvlist[k] != null) && (chkInvlist.length > 0))
						      {
						        String[] sTmpInvNo = chkInvlist[k].split(":");
						        for (int i = 0; i < invoiceList.size(); i++)
						        {
						          String sInvNo = ((TransactionVO)invoiceList.get(i)).getInvoiceNumber();
						          if (sTmpInvNo[1].equals(sInvNo.trim()))
						          {
						            ((TransactionVO)invoiceList.get(i)).setRealizedAmount(sTmpInvNo[2]);
						            ((TransactionVO)invoiceList.get(i)).setRealizedAmountIC(sTmpInvNo[3]);
						          }
						        }
						        logger.info("getRealizedAmount After - " + ((TransactionVO)invoiceList.get(k)).getRealizedAmount());
						        logger.info("getRealizedAmountIC After - " + ((TransactionVO)invoiceList.get(k)).getRealizedAmountIC());
						      }
						    }
						    return invoiceList;
						  }
						  public int parseChkList(String[] chkList)
						  {
						    int iRet = 0;
						    BoeVO boeVO = new BoeVO();
						    for (int k = 0; k < chkList.length; k++) {
						      if ((chkList[k] != null) && (chkList.length > 0))
						      {
						        String[] sTmpInvNo = chkList[k].split(":");
						        for (int i = 0; i < sTmpInvNo.length; i++)
						        {
						          String sInvSerNo = sTmpInvNo[0];
						          String sInvNo = sTmpInvNo[1];
						          setInvoiceSerNumber(sInvSerNo);
						          setInvoiceNumber(sInvNo);
						          iRet = 1;
						        }
						      }
						    }
						    return iRet;
						  }
						  public String getPortOfShipmentList()
						  {
						    return "success";
						  }
						  public String fetchPortOfShipmentList()
						    throws ApplicationException
						  {
						    logger.info("Entering Method");
						    String sStatus = "fail";
						    BoePriceReferenceToManyBillNoBD boeManyBillNoBD = null;
						    try
						    {
						      boeManyBillNoBD = BoePriceReferenceToManyBillNoBD.getBD();
						      if (this.boeVO != null)
						      {
						        this.portList = boeManyBillNoBD.fetchPortOfShipmentList(this.boeVO);
						        if (this.boeVO != null)
						        {
						          invoiceData();
						          sStatus = "success";
						        }
						        else
						        {
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
						  public String portOfShipmentToBOE()
						    throws ApplicationException
						  {
						    logger.info("Entering Method");
						    logger.info("Exiting Method");
						    return "success";
						  }
						  public String getPortOfDischarge()
						  {
						    return "success";
						  }
						  
						  public String fetchDischargePortList()
								    throws ApplicationException
								  {
								    logger.info("Entering Method");
								    String sStatus = "fail";
								    BoePriceReferenceToManyBillNoBD boeManyBillNoBD = null;
								    try
								    {
								      boeManyBillNoBD = BoePriceReferenceToManyBillNoBD.getBD();
								      if (this.boeVO != null)
								      {
								        this.dsgPortList = boeManyBillNoBD.fetchDischargePortList(this.boeVO);
								        if (this.boeVO != null)
								        {
								          invoiceData();
								          sStatus = "success";
								        }
								        else
								        {
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
								  public String portOfDischargeToBOE()
								  {
								    logger.info("Entering Method");
								    logger.info("Exiting Method");
								    return "success";
								  }
								  public String deleteBOEData()
								    throws ApplicationException
								  {
								    logger.info("deleteBOEData-------------------------");
								    BoePriceReferenceToManyBillNoBD boeManyBillNoBD = null;
								    try
								    {
								      logger.info("Delete Method 1111111111111111111");
								      boeManyBillNoBD = BoePriceReferenceToManyBillNoBD.getBD();
								      if (this.boeVO != null)
								      {
								        int iRet = boeManyBillNoBD.deleteBOEData(this.bill_chkList);
								        retriveDataFromTI();
								      }
								    }
								    catch (Exception e)
								    {
								      logger.info("deleteBOEData---------------exception----------" + e);
								      throwApplicationException(e);
								    }
								    logger.info("Exiting Method");
								    return "success";
								  }
								  public String getOutstandingORM()
								  {
								    return "success";
								  }
								  public String fetchoutstandingORMList()
								    throws ApplicationException
								  {
								    logger.info("Entering Method");
								    String sStatus = "fail";
								    BoePriceReferenceToManyBillNoBD boeManyBillNoBD = null;
								    try
								    {
								      boeManyBillNoBD = BoePriceReferenceToManyBillNoBD.getBD();
								      if (this.boeVO != null)
								      {
								        this.ormList = boeManyBillNoBD.fetchoutstandingORMList(this.boeVO);
								        if (this.boeVO != null) {
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
								  public String outStandingORMToORM()
								  {
								    logger.info("Entering Method");
								    logger.info("PayRef :: " + this.boeVO.getPaymentRefNo());
								    BoeBillNoToManyPaymentReferenceBD boeBD = null;
								    try
								    {
								      logger.info("sMstRefNums------------>" + this.sMstRefNums);
								      logger.info("sEvtRefNums ------------>" + this.sEvtRefNums);
								      HttpSession session = ServletActionContext.getRequest().getSession();
								      String master_reference = (String)session.getAttribute("xMstRefNum");
								      logger.info("Master Reference nUmber----------->" + master_reference);
								      boeBD = new BoeBillNoToManyPaymentReferenceBD();
								      this.eventList = boeBD.fetchPayRef(this.boeVO);
								      logger.info("eventList.size() : " + this.eventList.size());
								      if ((this.eventList != null) && (this.eventList.size() > 0)) {
								        this.searchButton = "Y";
								      }
								    }
								    catch (Exception e)
								    {
								      logger.info(" landingPage Error here ---------" + e.getMessage());
								    }
								    logger.info("Exiting Method");
								    return "success";
								  }
								  public String getOutstandingBOEList1()
								  {
								    return "success";
								  }
								  public String fetchOutstandingBOEList1()
								    throws ApplicationException
								  {
								    logger.info("Entering Method");
								    String sStatus = "fail";
								    BoePriceReferenceToManyBillNoBD boeManyBillNoBD = null;
								    try
								    {
								      boeManyBillNoBD = BoePriceReferenceToManyBillNoBD.getBD();
								      if (this.boeVO != null)
								      {
								        this.boeList = boeManyBillNoBD.fetchOutstandingBOEList(this.boeVO);
								        if (this.boeVO != null) {
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
								  public String outstandingBOEToBOE1()
								    throws ApplicationException
								  {
								    logger.info("Entering Method");
								    getDataFromTIForSecond();
								    logger.info("Exiting Method");
								    return "success";
								  }
								}
				  