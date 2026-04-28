package in.co.boe.action;

import in.co.boe.businessdelegate.boe_checker.BoeCheckerBD;
import in.co.boe.businessdelegate.exception.BusinessException;
import in.co.boe.businessdelegate.pricereftomanybill.BoePriceReferenceToManyBillNoBD;
import in.co.boe.dao.exception.ApplicationException;
import in.co.boe.utility.ActionConstants;
import in.co.boe.utility.CommonMethods;
import in.co.boe.utility.ValidationUtility;
import in.co.boe.vo.AlertMessagesVO;
import in.co.boe.vo.BOEPortSelectionVO;
import in.co.boe.vo.BOESearchVO;
import in.co.boe.vo.BoeVO;
import in.co.boe.vo.InvoiceDetailsVO;
import in.co.boe.vo.TransactionVO;
import java.util.ArrayList;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
 
public class ManualBOEAction
  extends BoeBaseAction
{
  private static final long serialVersionUID = 1L;
  private static Logger logger = Logger.getLogger(ManualBOEAction.class.getName());
  ArrayList<TransactionVO> tiList = null;
  TransactionVO transactionVO = null;
  BoeVO boeVO = null;
  Map<String, String> gpTypeList;
  Map<String, String> importAgencyList;
  Map<String, String> recordInd;
  Map<String, String> transTypeList;
  ArrayList<TransactionVO> invoiceList = null;
  Map<String, String> incoTermsList;
  Map<String, String> manualBOEStatus;
  private ArrayList<AlertMessagesVO> alertMsgArray = null;
  String boeData = null;
  String boeStatus = null;
  ArrayList<BoeVO> boeList = null;
  BOESearchVO boeSearchVO = null;
  ArrayList<InvoiceDetailsVO> boeInvoiceList = null;
  private String boeAuthorizeStatus;
  String[] manualchkList = null;
  String remarks = null;
  String sPortCode = null;
  ArrayList<BOEPortSelectionVO> portList = null;
  ArrayList<BoeVO> dsgPortList = null;
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
  public ArrayList<BoeVO> getDsgPortList()
  {
    return this.dsgPortList;
  }
  public void setDsgPortList(ArrayList<BoeVO> dsgPortList)
  {
    this.dsgPortList = dsgPortList;
  }
  public ArrayList<BOEPortSelectionVO> getPortList()
  {
    return this.portList;
  }
  public void setPortList(ArrayList<BOEPortSelectionVO> portList)
  {
    this.portList = portList;
  }
  public String getsPortCode()
  {
    return this.sPortCode;
  }
  public void setsPortCode(String sPortCode)
  {
    this.sPortCode = sPortCode;
  }
  public Map<String, String> getManualBOEStatus()
  {
    return ActionConstants.BOE_STATUS;
  }
  public void setManualBOEStatus(Map<String, String> manualBOEStatus)
  {
    this.manualBOEStatus = manualBOEStatus;
  }
  public String[] getManualchkList()
  {
    return this.manualchkList;
  }
  public void setManualchkList(String[] manualchkList)
  {
    this.manualchkList = manualchkList;
  }
  public String getRemarks()
  {
    return this.remarks;
  }
  public void setRemarks(String remarks)
  {
    this.remarks = remarks;
  }
  public String getBoeAuthorizeStatus()
  {
    return this.boeAuthorizeStatus;
  }
  public void setBoeAuthorizeStatus(String boeAuthorizeStatus)
  {
    this.boeAuthorizeStatus = boeAuthorizeStatus;
  }
  public ArrayList<InvoiceDetailsVO> getBoeInvoiceList()
  {
    return this.boeInvoiceList;
  }
  public void setBoeInvoiceList(ArrayList<InvoiceDetailsVO> boeInvoiceList)
  {
    this.boeInvoiceList = boeInvoiceList;
  }
  public BOESearchVO getBoeSearchVO()
  {
    return this.boeSearchVO;
  }
  public void setBoeSearchVO(BOESearchVO boeSearchVO)
  {
    this.boeSearchVO = boeSearchVO;
  }
  public ArrayList<BoeVO> getBoeList()
  {
    return this.boeList;
  }
  public void setBoeList(ArrayList<BoeVO> boeList)
  {
    this.boeList = boeList;
  }
  public String getBoeData()
  {
    return this.boeData;
  }
  public void setBoeData(String boeData)
  {
    this.boeData = boeData;
  }
  public String getBoeStatus()
  {
    return this.boeStatus;
  }
  public void setBoeStatus(String boeStatus)
  {
    this.boeStatus = boeStatus;
  }
  public Map<String, String> getIncoTermsList()
  {
    return ActionConstants.INVOICE_TERMS;
  }
  public void setIncoTermsList(Map<String, String> incoTermsList)
  {
    this.incoTermsList = incoTermsList;
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
  public String manualBOE()
    throws ApplicationException
  {
    logger.info("Entering Method");
    try
    {
      logger.info("Manual BOE Action");
      logger.info("sMstRefNums------------>" + this.sMstRefNums);
      logger.info("sEvtRefNums ------------>" + this.sEvtRefNums);
      HttpSession session = ServletActionContext.getRequest().getSession();
      String master_reference = (String)session.getAttribute("xMstRefNum");
      logger.info("Master Reference nUmber----------->" + master_reference);
    }
    catch (Exception e)
    {
      logger.info("Error here " + e.getMessage());
    }
    logger.info("Exiting Method");
    return "success";
  }

  public String retriveBOEData()
		    throws ApplicationException
		  {
		    logger.info("Entering Method");
		    BoePriceReferenceToManyBillNoBD boeManyBillNoBD = null;
		    try
		    {
		      boeManyBillNoBD = BoePriceReferenceToManyBillNoBD.getBD();
		      if (this.boeVO != null)
		      {
		        this.boeVO = boeManyBillNoBD.fetchManualBOEDetails(this.boeVO);
		        if ((this.boeVO.getInvoiceList() != null) && (!this.boeVO.getInvoiceList().isEmpty())) {
		          this.invoiceList = this.boeVO.getInvoiceList();
		        }
		      }
		    }
		    catch (Exception e)
		    {
		      logger.info("Error here " + e.getMessage());
		    }
		    logger.info("Exiting Method");
		    return "success";
		  }
		  public String storeManualBOEData()
		    throws ApplicationException
		  {
		    logger.info("Entering Method");
		    BoePriceReferenceToManyBillNoBD boeManyBillNoBD = null;
		    CommonMethods commonMethods = null;
		    commonMethods = new CommonMethods();
		    String buttonType = commonMethods.getEmptyIfNull(this.boeVO.getButtonType()).trim();
		    try
		    {
		      boeManyBillNoBD = BoePriceReferenceToManyBillNoBD.getBD();
		      if (this.boeVO != null) {
		        validateData(this.boeVO);
		      }
		      if (this.alertMsgArray.size() > 0)
		      {
		        this.boeVO = boeManyBillNoBD.revertManualBOEDetails(this.boeVO);
		        if ((this.boeVO.getInvoiceList() != null) && (!this.boeVO.getInvoiceList().isEmpty())) {
		          this.invoiceList = this.boeVO.getInvoiceList();
		        }
		      }
		      else
		      {
		        int boeCount = boeManyBillNoBD.insertManualBOEData(this.boeVO);
		        logger.info("BOE Data -->" + boeCount);
		        if (boeCount == 1)
		        {
		          this.boeVO = new BoeVO();
		          if ((!commonMethods.isNull(buttonType)) && (buttonType.equalsIgnoreCase("Insert"))) {
		            addActionMessage("BOE Data Is Inserted Successfully");
		          } else if ((!commonMethods.isNull(buttonType)) && 
		            (buttonType.equalsIgnoreCase("Delete"))) {
		            addActionMessage("BOE  Data Is Deleted Successfully");
		          }
		        }
		        else
		        {
		          this.boeVO = boeManyBillNoBD.revertManualBOEDetails(this.boeVO);
		          if ((this.boeVO.getInvoiceList() != null) && (!this.boeVO.getInvoiceList().isEmpty())) {
		            this.invoiceList = this.boeVO.getInvoiceList();
		          }
		          addActionError("BOE Data Is Failed");
		        }
		      }
		    }
		    catch (Exception e)
		    {
		      logger.info("Exception Store BOE Data---> " + e.getMessage());
		    }
		    logger.info("Exiting Method");
		    return "success";
		  }
		  public String manualInsertinvoice()
		    throws ApplicationException
		  {
		    logger.info("Entering Method");
		    BoePriceReferenceToManyBillNoBD boeManyBillNoBD = null;
		    CommonMethods commonMethods = null;
		    try
		    {
		      boeManyBillNoBD = BoePriceReferenceToManyBillNoBD.getBD();
		      commonMethods = new CommonMethods();
		      logger.info("111111111111111111111111111111");
		      if (this.boeVO != null) {
		        invoiceDetailsValidation1(this.boeVO);
		      }
		      if (this.transactionVO != null)
		      {
		        logger.info("00000000000000000000000000");
		        invoiceDetailsValidation(this.transactionVO);
		      }
		      logger.info("************************2222222222222222222222222222222222");
		      if (this.boeVO != null)
		      {
		        String buttonType = commonMethods.getEmptyIfNull(this.boeVO.getButtonType()).trim();
		        if ((!commonMethods.isNull(buttonType)) && (buttonType.equalsIgnoreCase("Insert"))) {
		          return "success";
		        }
		      }
		    }
		    catch (Exception exception)
		    {
		      throwApplicationException(exception);
		      logger.info("Exiting Method");
		    }
		    return "success";
		  }
		  public String manualInvoiceData()
		    throws ApplicationException
		  {
		    logger.info("Entering Method");
		    BoePriceReferenceToManyBillNoBD boeManyBillNoBD = null;
		    CommonMethods commonMethods = null;
		    try
		    {
		      boeManyBillNoBD = BoePriceReferenceToManyBillNoBD.getBD();
		      commonMethods = new CommonMethods();
		      logger.info("111111111111111111111111111111");
		      if (this.boeVO != null) {
		        invoiceDetailsValidation1(this.boeVO);
		      }
		      if (this.transactionVO != null)
		      {
		        logger.info("00000000000000000000000000");
		        invoiceDetailsValidation(this.transactionVO);
		      }
		      logger.info("************************2222222222222222222222222222222222");
		      if (this.boeVO != null)
		      {
		        String buttonType = commonMethods.getEmptyIfNull(this.boeVO.getButtonType()).trim();
		        if ((!commonMethods.isNull(buttonType)) && (buttonType.equalsIgnoreCase("Insert"))) {
		          return "success";
		        }
		        if ((!commonMethods.isNull(buttonType)) && (buttonType.equalsIgnoreCase("Update")))
		        {
		          this.transactionVO = boeManyBillNoBD.fetchInvoiceData(this.boeVO);
		        }
		        else if ((!commonMethods.isNull(buttonType)) && 
		          (buttonType.equalsIgnoreCase("UpdateEditedData")))
		        {
		          logger.info(">>>>>>>>>>>>>>>>>>>>INSIDE EDIT UPDATE BLOCK>>>>>>>>>>>>>>>");
		          this.transactionVO = boeManyBillNoBD.updateboeDatatoTable(this.boeVO, this.transactionVO);
		          if (this.boeVO.getUpdateCount() > 0) {
		            addActionMessage("BOE Data Is Updated Successfully");
		          }
		        }
		        else if ((!commonMethods.isNull(buttonType)) && (buttonType.equalsIgnoreCase("Delete")))
		        {
		          this.transactionVO = boeManyBillNoBD.fetchInvoiceData(this.boeVO);
		          this.transactionVO = boeManyBillNoBD.deletedata(this.boeVO, this.transactionVO);
		          int boeCount = boeManyBillNoBD.loaddeletedata(this.boeVO);
		          logger.info("BOE Data -->" + boeCount);
		          if (boeCount == 0) {
		            addActionMessage("Mannual BOE Invoice Deleted Successfully ");
		          }
		        }
		        else if ((!commonMethods.isNull(buttonType)) && (buttonType.equalsIgnoreCase("View")))
		        {
		          this.transactionVO = boeManyBillNoBD.fetchInvoiceData(this.boeVO);
		        }
		        else
		        {
		          if ((!commonMethods.isNull(buttonType)) && (buttonType.equalsIgnoreCase("Edit")))
		          {
		            this.transactionVO = boeManyBillNoBD.fetchInvoiceData(this.boeVO);
		            if (!boeManyBillNoBD.checkBOEStatus(this.boeVO))
		            {
		              logger.info("-----------------------BOE NOT SUBMITTED ---------------");
		              return "success";
		            }
		            logger.info("-----------------------BOE  SUBMITTED ---------------");
		            retriveBOEData();
		            this.transactionVO = boeManyBillNoBD.fetchInvoiceData(this.boeVO);
		            return "error";
		          }
		          if ((!commonMethods.isNull(buttonType)) && (buttonType.equalsIgnoreCase("deleteBOEdata")))
		          {
		            this.transactionVO = boeManyBillNoBD.fetchInvoiceData(this.boeVO);
		            this.transactionVO = boeManyBillNoBD.deleteBOEdata(this.boeVO, this.transactionVO);
		            int boeCount = boeManyBillNoBD.loaddeletedata(this.boeVO);
		            logger.info("BOE Data -->" + boeCount);
		            if (boeCount == 0)
		            {
		              this.boeVO = new BoeVO();
		              addActionMessage("BOE  Data Is Deleted Successfully");
		            }
		          }
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
		  public String delete_mannual_boe()
		    throws ApplicationException
		  {
		    BoePriceReferenceToManyBillNoBD boeManyBillNoBD = null;
		    boeManyBillNoBD = BoePriceReferenceToManyBillNoBD.getBD();
		    CommonMethods commonMethods = null;
		    this.alertMsgArray = new ArrayList();
		    commonMethods = new CommonMethods();
		    try
		    {
		      int mannual_boe_invoice_deletion = boeManyBillNoBD.deleteManualBOEDataInvoice(this.boeVO);
		      if (mannual_boe_invoice_deletion < 0) {
		        addActionMessage("Mannual BOE Invoice Deleted Successfully ");
		      }
		    }
		    catch (BusinessException e1)
		    {
		      logger.info("Mannual BOE Invoice Deletion Exception--->" + e1.getMessage());
		    }
		    if ((this.alertMsgArray != null) && 
		      (this.alertMsgArray.size() > 0)) {
		      this.alertMsgArray.clear();
		    }
		    try
		    {
		      if ((!commonMethods.isNull(this.boeVO.getBoeNo())) && (!commonMethods.isNull(this.boeVO.getPortCode())) && 
		        (!commonMethods.isNull(this.boeVO.getPortCode())))
		      {
		        boolean isData_exist = ValidationUtility.isDataExist_OBE_XML_UPLOAD(this.boeVO.getBoeNo().toString(), 
		          this.boeVO.getBoeDate().toString(), this.boeVO.getPortCode().toString());
		        if (isData_exist)
		        {
		          String errorcode = "BOE Number BOE Date Port Code Already Used In OBE File Upload ";
		          Object[] arg = { "0", "E", errorcode, "Input" };
		          setErrorvalues(arg);
		        }
		      }
		      String errorcode = "Data deleted Successfully";
		      Object[] arg = { "0", "E", errorcode, "Output" };
		      setErrorvalues(arg);
		    }
		    catch (Exception e)
		    {
		      logger.info("delete_mannual_boe action method Exception----------->" + e.getMessage());
		    }
		    logger.info("Exiting Method");
		    return "success";
		  }
		  
		  public String insertManualInvoiceData()
				    throws ApplicationException
				  {
				    logger.info("Entering Method");
				    BoePriceReferenceToManyBillNoBD boeManyBillNoBD = null;
				    String target = "";
				    CommonMethods commonMethods = null;
				    commonMethods = new CommonMethods();
				    try
				    {
				      boeManyBillNoBD = BoePriceReferenceToManyBillNoBD.getBD();
				      if (this.transactionVO != null) {
				        invoiceDetailsValidation(this.transactionVO);
				      }
				      if (this.alertMsgArray.size() > 0)
				      {
				        target = "error";
				      }
				      else
				      {
				        this.transactionVO = boeManyBillNoBD.insertInvoiceDatatoTable(this.boeVO, this.transactionVO);
				        if (this.boeVO != null)
				        {
				          this.boeVO = boeManyBillNoBD.revertManualBOEDetails(this.boeVO);
				          if ((this.boeVO.getInvoiceList() != null) && (!this.boeVO.getInvoiceList().isEmpty())) {
				            this.invoiceList = this.boeVO.getInvoiceList();
				          }
				        }
				        String buttonType = commonMethods.getEmptyIfNull(this.boeVO.getButtonType()).trim();
				        if ((!commonMethods.isNull(buttonType)) && (buttonType.equalsIgnoreCase("Update"))) {
				          addActionMessage("BOE Invoice Data Is Updated Successfully");
				        }
				        target = "success";
				      }
				    }
				    catch (Exception e)
				    {
				      logger.info("Exception Insert_InvoiceData_Table---> " + e.getMessage());
				    }
				    logger.info("Exiting Method");
				    return target;
				  }
				  public String updateManualInvoiceData()
				    throws ApplicationException
				  {
				    logger.info("Entering Method");
				    BoePriceReferenceToManyBillNoBD boeManyBillNoBD = null;
				    String target = "";
				    try
				    {
				      boeManyBillNoBD = BoePriceReferenceToManyBillNoBD.getBD();
				      if (this.transactionVO != null) {
				        invoiceDetailsValidation(this.transactionVO);
				      }
				      if (this.alertMsgArray.size() > 0)
				      {
				        target = "error";
				      }
				      else
				      {
				        this.transactionVO = boeManyBillNoBD.UpdateInvoiceDatatoTable(this.boeVO, this.transactionVO);
				        if (this.boeVO != null)
				        {
				          this.boeVO = boeManyBillNoBD.revertManualBOEDetails(this.boeVO);
				          if ((this.boeVO.getInvoiceList() != null) && (!this.boeVO.getInvoiceList().isEmpty())) {
				            this.invoiceList = this.boeVO.getInvoiceList();
				          }
				        }
				        target = "success";
				      }
				    }
				    catch (BusinessException e)
				    {
				      e.printStackTrace();
				    }
				    return target;
				  }
				  public void delete_mannual_boe(BoeVO boeVO)
				    throws ApplicationException
				  {
				    BoePriceReferenceToManyBillNoBD boeManyBillNoBD = null;
				    boeManyBillNoBD = BoePriceReferenceToManyBillNoBD.getBD();
				    CommonMethods commonMethods = null;
				    this.alertMsgArray = new ArrayList();
				    commonMethods = new CommonMethods();
				    try
				    {
				      int mannual_boe_invoice_deletion = boeManyBillNoBD.deleteManualBOEDataInvoice(boeVO);
				      if (mannual_boe_invoice_deletion < 0) {
				        addActionMessage("Mannual BOE Invoice Deleted Successfully ");
				      }
				    }
				    catch (BusinessException e1)
				    {
				      logger.info("Mannual BOE Invoice Deletion Exception--->" + e1.getMessage());
				    }
				    if ((this.alertMsgArray != null) && 
				      (this.alertMsgArray.size() > 0)) {
				      this.alertMsgArray.clear();
				    }
				    try
				    {
				      if ((!commonMethods.isNull(boeVO.getBoeNo())) && (!commonMethods.isNull(boeVO.getPortCode())) && 
				        (!commonMethods.isNull(boeVO.getPortCode())))
				      {
				        boolean isData_exist = ValidationUtility.isDataExist_OBE_XML_UPLOAD(boeVO.getBoeNo().toString(), 
				          boeVO.getBoeDate().toString(), boeVO.getPortCode().toString());
				        if (isData_exist)
				        {
				          String errorcode = "BOE Number BOE Date Port Code Already Used In OBE File Upload ";
				          Object[] arg = { "0", "E", errorcode, "Input" };
				          setErrorvalues(arg);
				        }
				      }
				      String errorcode = "Data deleted Successfully";
				      Object[] arg = { "0", "E", errorcode, "Output" };
				      setErrorvalues(arg);
				    }
				    catch (Exception e)
				    {
				      logger.info("OUtSide SQL Exception ------------------------->" + e.getMessage());
				    }
				  }
				  public void invoiceDetailsValidation1(BoeVO boeVO)
				    throws ApplicationException
				  {
				    CommonMethods commonMethods = null;
				    this.alertMsgArray = new ArrayList();
				    commonMethods = new CommonMethods();
				    if ((this.alertMsgArray != null) && 
				      (this.alertMsgArray.size() > 0)) {
				      this.alertMsgArray.clear();
				    }
				    try
				    {
				      if ((!commonMethods.isNull(boeVO.getBoeNo())) && (!commonMethods.isNull(boeVO.getPortCode())) && 
				        (!commonMethods.isNull(boeVO.getPortCode())))
				      {
				        boolean isData_exist = ValidationUtility.isDataExist_OBE_XML_UPLOAD(boeVO.getBoeNo().toString(), 
				          boeVO.getBoeDate().toString(), boeVO.getPortCode().toString());
				        if (isData_exist)
				        {
				          String errorcode = "BOE Number BOE Date Port Code Already Used In OBE File Upload ";
				          Object[] arg = { "0", "E", errorcode, "Input" };
				          setErrorvalues(arg);
				        }
				      }
				    }
				    catch (Exception e)
				    {
				      logger.info("OUtSide SQL Exception ------------------------->" + e.getMessage());
				    }
				  }
				  
				  public void invoiceDetailsValidation(TransactionVO transactionVO)
						    throws ApplicationException
						  {
						    CommonMethods commonMethods = null;
						    BoePriceReferenceToManyBillNoBD boeManyBillNoBD = null;
						    try
						    {
						      boeManyBillNoBD = BoePriceReferenceToManyBillNoBD.getBD();
						      this.alertMsgArray = new ArrayList();
						      commonMethods = new CommonMethods();
						      if ((this.alertMsgArray != null) && 
						        (this.alertMsgArray.size() > 0)) {
						        this.alertMsgArray.clear();
						      }
						      int invCount = 0;
						      if (this.boeVO != null)
						      {
						        if (this.boeVO.getButtonType().equalsIgnoreCase("Insert")) {
						          invCount = boeManyBillNoBD.checkInvCount(this.boeVO, transactionVO);
						        }
						        if (invCount > 0)
						        {
						          String errorcode = commonMethods.getErrorDesc("INV13", "N100");
						          Object[] arg = { "0", "E", errorcode, "Input" };
						          setErrorvalues(arg);
						        }
						        else
						        {
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
						          else if ((this.boeVO.getButtonType().equalsIgnoreCase("Insert")) || 
						            (this.boeVO.getButtonType().equalsIgnoreCase("Update")))
						          {
						            int bCount = boeManyBillNoBD.boeInvCount(this.boeVO, transactionVO);
						            if (bCount > 0)
						            {
						              String errorcode = commonMethods.getErrorDesc("INV14", 
						                "N100");
						              Object[] arg = { "0", "E", errorcode, 
						                "Input" };
						              setErrorvalues(arg);
						            }
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
						          if ((!commonMethods.isNull(transactionVO.getFreightAmount())) && 
						            (commonMethods.isNull(transactionVO.getFreightCurrencyCode())))
						          {
						            String errorcode = commonMethods.getErrorDesc("INV15", "N100");
						            Object[] arg = { "0", "E", errorcode, "Input" };
						            setErrorvalues(arg);
						          }
						          if ((!commonMethods.isNull(transactionVO.getInsuranceAmount())) && 
						            (commonMethods.isNull(transactionVO.getInsuranceCurrencyCode())))
						          {
						            String errorcode = commonMethods.getErrorDesc("INV16", "N100");
						            Object[] arg = { "0", "E", errorcode, "Input" };
						            setErrorvalues(arg);
						          }
						          if ((!commonMethods.isNull(transactionVO.getAgencyCommission())) && 
						            (commonMethods.isNull(transactionVO.getAgencyCurrency())))
						          {
						            String errorcode = commonMethods.getErrorDesc("INV17", "N100");
						            Object[] arg = { "0", "E", errorcode, "Input" };
						            setErrorvalues(arg);
						          }
						          if ((!commonMethods.isNull(transactionVO.getDiscountCharges())) && 
						            (commonMethods.isNull(transactionVO.getDiscountCurrency())))
						          {
						            String errorcode = commonMethods.getErrorDesc("INV18", "N100");
						            Object[] arg = { "0", "E", errorcode, "Input" };
						            setErrorvalues(arg);
						          }
						          if ((!commonMethods.isNull(transactionVO.getMiscellaneousCharges())) && 
						            (commonMethods.isNull(transactionVO.getMiscellaneousCurrency())))
						          {
						            String errorcode = commonMethods.getErrorDesc("INV19", "N100");
						            Object[] arg = { "0", "E", errorcode, "Input" };
						            setErrorvalues(arg);
						          }
						          if ((!commonMethods.isNull(transactionVO.getFreightCurrencyCode())) && 
						            (commonMethods.isNull(transactionVO.getFreightAmount())))
						          {
						            String errorcode = commonMethods.getErrorDesc("INV20", "N100");
						            Object[] arg = { "0", "E", errorcode, "Input" };
						            setErrorvalues(arg);
						          }
						          if ((!commonMethods.isNull(transactionVO.getInsuranceCurrencyCode())) && 
						            (commonMethods.isNull(transactionVO.getInsuranceAmount())))
						          {
						            String errorcode = commonMethods.getErrorDesc("INV21", "N100");
						            Object[] arg = { "0", "E", errorcode, "Input" };
						            setErrorvalues(arg);
						          }
						          if ((!commonMethods.isNull(transactionVO.getAgencyCurrency())) && 
						            (commonMethods.isNull(transactionVO.getAgencyCommission())))
						          {
						            String errorcode = commonMethods.getErrorDesc("INV22", "N100");
						            Object[] arg = { "0", "E", errorcode, "Input" };
						            setErrorvalues(arg);
						          }
						          if ((!commonMethods.isNull(transactionVO.getDiscountCurrency())) && 
						            (commonMethods.isNull(transactionVO.getDiscountCharges())))
						          {
						            String errorcode = commonMethods.getErrorDesc("INV23", "N100");
						            Object[] arg = { "0", "E", errorcode, "Input" };
						            setErrorvalues(arg);
						          }
						          if ((!commonMethods.isNull(transactionVO.getMiscellaneousCurrency())) && 
						            (commonMethods.isNull(transactionVO.getMiscellaneousCharges())))
						          {
						            String errorcode = commonMethods.getErrorDesc("INV24", "N100");
						            Object[] arg = { "0", "E", errorcode, "Input" };
						            setErrorvalues(arg);
						          }
						          if (!commonMethods.isNull(transactionVO.getFreightCurrencyCode()))
						          {
						            int invCurrCount = boeManyBillNoBD.validateInvCurrency(transactionVO.getFreightCurrencyCode());
						            if (invCurrCount == 0)
						            {
						              String errorcode = "Invalid Freight Currency";
						              Object[] arg = { "0", "E", errorcode, "Input" };
						              setErrorvalues(arg);
						            }
						          }
						          if (!commonMethods.isNull(transactionVO.getInsuranceCurrencyCode()))
						          {
						            int invCurrCount = boeManyBillNoBD
						              .validateInvCurrency(transactionVO.getInsuranceCurrencyCode());
						            if (invCurrCount == 0)
						            {
						              String errorcode = "Invalid Insurance Currency";
						              Object[] arg = { "0", "E", errorcode, "Input" };
						              setErrorvalues(arg);
						            }
						          }
						          if (!commonMethods.isNull(transactionVO.getAgencyCurrency()))
						          {
						            int invCurrCount = boeManyBillNoBD.validateInvCurrency(transactionVO.getAgencyCurrency());
						            if (invCurrCount == 0)
						            {
						              String errorcode = "Invalid Agency Currency";
						              Object[] arg = { "0", "E", errorcode, "Input" };
						              setErrorvalues(arg);
						            }
						          }
						          if (!commonMethods.isNull(transactionVO.getDiscountCurrency()))
						          {
						            int invCurrCount = boeManyBillNoBD.validateInvCurrency(transactionVO.getDiscountCurrency());
						            if (invCurrCount == 0)
						            {
						              String errorcode = "Invalid Discount Currency";
						              Object[] arg = { "0", "E", errorcode, "Input" };
						              setErrorvalues(arg);
						            }
						          }
						          if (!commonMethods.isNull(transactionVO.getMiscellaneousCurrency()))
						          {
						            int invCurrCount = boeManyBillNoBD
						              .validateInvCurrency(transactionVO.getMiscellaneousCurrency());
						            if (invCurrCount == 0)
						            {
						              String errorcode = "Invalid Miscellaneous Currency";
						              Object[] arg = { "0", "E", errorcode, "Input" };
						              setErrorvalues(arg);
						            }
						          }
						          if (!commonMethods.isNull(transactionVO.getInvAmt()))
						          {
						            boolean bInvAmtStatus = ValidationUtility.isValidAmount(transactionVO.getInvAmt().toString());
						            if (!bInvAmtStatus)
						            {
						              String errorcode = "Invoice Amount Should be Greater than Zero";
						              Object[] arg = { "0", "E", errorcode, "Input" };
						              setErrorvalues(arg);
						            }
						          }
						          if (commonMethods.isNull(this.boeVO.getPortCode()))
						          {
						            String errorcode = commonMethods.getErrorDesc("BOE15", "N100");
						            Object[] arg = { "0", "E", errorcode, "Input" };
						            setErrorvalues(arg);
						          }
						          else
						          {
						            int portCodeCount = boeManyBillNoBD
						              .validatePortCode(commonMethods.getEmptyIfNull(this.boeVO.getPortCode()).trim());
						            if (portCodeCount == 0)
						            {
						              String errorcode = "Invalid Port Code";
						              Object[] arg = { "0", "E", errorcode, "Input" };
						              setErrorvalues(arg);
						            }
						          }
						          if (!commonMethods.isNull(transactionVO.getTermsofInvoice()))
						          {
						            boolean bTermOfInv = ValidationUtility.isCheckInvTerm(transactionVO.getTermsofInvoice());
						            if (!bTermOfInv)
						            {
						              String errorcode = "Terms of Invoice Only CIF, CF, CI, FOB and OTHERS";
						              Object[] arg = { "0", "E", errorcode, "Input" };
						              setErrorvalues(arg);
						            }
						          }
						          if (!commonMethods.isNull(transactionVO.getSupplierCountry()))
						          {
						            boolean bSupplierConName = ValidationUtility.isValidCountry(transactionVO.getSupplierCountry());
						            if (!bSupplierConName)
						            {
						              String errorcode = "Invalid Supplier Country Name, Kindly input the country code";
						              Object[] arg = { "0", "E", errorcode, "Input" };
						              setErrorvalues(arg);
						            }
						          }
						          if (!commonMethods.isNull(transactionVO.getSellerCountry()))
						          {
						            boolean bSellerConName = ValidationUtility.isValidCountry(transactionVO.getSellerCountry());
						            if (!bSellerConName)
						            {
						              String errorcode = "Invalid Seller Country Name, Kindly input the country code";
						              Object[] arg = { "0", "E", errorcode, "Input" };
						              setErrorvalues(arg);
						            }
						          }
						          if (!commonMethods.isNull(transactionVO.getThirdPartyCountry()))
						          {
						            boolean bThirdPartyConName = 
						              ValidationUtility.isValidCountry(transactionVO.getThirdPartyCountry());
						            if (!bThirdPartyConName)
						            {
						              String errorcode = "Invalid Third Party Country Name";
						              Object[] arg = { "0", "E", errorcode, "Input" };
						              setErrorvalues(arg);
						            }
						          }
						          if (!commonMethods.isNull(transactionVO.getInvAmt()))
						          {
						            boolean bValidInvPrecision = ValidationUtility.validatePrecision(transactionVO.getInvAmt());
						            if (!bValidInvPrecision)
						            {
						              String errorcode = "Invalid Invoice Amount length";
						              Object[] arg = { "0", "E", errorcode, "Input" };
						              setErrorvalues(arg);
						            }
						          }
						          if (!commonMethods.isNull(transactionVO.getFreightAmount()))
						          {
						            boolean bValidFgtPrecision = 
						              ValidationUtility.validatePrecision(transactionVO.getFreightAmount());
						            if (!bValidFgtPrecision)
						            {
						              String errorcode = "Invalid Freight Amount length";
						              Object[] arg = { "0", "E", errorcode, "Input" };
						              setErrorvalues(arg);
						            }
						          }
						          if (!commonMethods.isNull(transactionVO.getInsuranceAmount()))
						          {
						            boolean bValidIncPrecision = 
						              ValidationUtility.validatePrecision(transactionVO.getInsuranceAmount());
						            if (!bValidIncPrecision)
						            {
						              String errorcode = "Invalid Insurence Amount length";
						              Object[] arg = { "0", "E", errorcode, "Input" };
						              setErrorvalues(arg);
						            }
						          }
						          if (!commonMethods.isNull(transactionVO.getAgencyCommission()))
						          {
						            boolean bValidCommPrecision = 
						              ValidationUtility.validatePrecision(transactionVO.getAgencyCommission());
						            if (!bValidCommPrecision)
						            {
						              String errorcode = "Invalid Commission Amount length";
						              Object[] arg = { "0", "E", errorcode, "Input" };
						              setErrorvalues(arg);
						            }
						          }
						          if (!commonMethods.isNull(transactionVO.getDiscountCharges()))
						          {
						            boolean bValidDisPrecision = 
						              ValidationUtility.validatePrecision(transactionVO.getDiscountCharges());
						            if (!bValidDisPrecision)
						            {
						              String errorcode = "Invalid Discount Amount length";
						              Object[] arg = { "0", "E", errorcode, "Input" };
						              setErrorvalues(arg);
						            }
						          }
						          if (!commonMethods.isNull(transactionVO.getMiscellaneousCharges()))
						          {
						            boolean bValidMisPrecision = 
						              ValidationUtility.validatePrecision(transactionVO.getMiscellaneousCharges());
						            if (!bValidMisPrecision)
						            {
						              String errorcode = "Invalid Miscellaneous Amount length";
						              Object[] arg = { "0", "E", errorcode, "Input" };
						              setErrorvalues(arg);
						            }
						          }
						        }
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
						    try
						    {
						      CommonMethods commonMethods = new CommonMethods();
						      AlertMessagesVO altMsg = new AlertMessagesVO();
						      altMsg.setErrorId(commonMethods.getEmptyIfNull(arg[1]).equalsIgnoreCase("W") ? 
						        "Warning" : 
						        "Error");
						      altMsg.setErrorDesc("General");
						      altMsg.setErrorCode(commonMethods.getEmptyIfNull(arg[3]));
						      altMsg.setErrorDetails(commonMethods.getEmptyIfNull(arg[2]));
						      altMsg.setErrorMsg(
						        commonMethods.getEmptyIfNull(arg[1]).equalsIgnoreCase("W") ? "N" : 
						        "");
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
						    logger.info("Entering Method");
						    BoePriceReferenceToManyBillNoBD boeManyBillNoBD = null;
						    try
						    {
						      boeManyBillNoBD = BoePriceReferenceToManyBillNoBD.getBD();
						      if (this.boeVO != null)
						      {
						        this.boeVO = boeManyBillNoBD.revertManualBOEDetails(this.boeVO);
						        if ((this.boeVO.getInvoiceList() != null) && (!this.boeVO.getInvoiceList().isEmpty())) {
						          this.invoiceList = this.boeVO.getInvoiceList();
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
						      int boeCount = 0;
						      if (boeVO != null)
						      {
						        boeCount = boeManyBillNoBD.checkBoeCount(boeVO);
						        if (boeCount > 0)
						        {
						          String errorcode = commonMethods.getErrorDesc("BEA017", "N100");
						          Object[] arg = { "0", "E", errorcode, "Input" };
						          setErrorvalues(arg);
						        }
						        else
						        {
						          if (!commonMethods.isNull(boeVO.getAdCode()))
						          {
						            int adCount = boeManyBillNoBD
						              .checkAdCode(commonMethods.getEmptyIfNull(boeVO.getAdCode()).trim());
						            if (adCount == 0)
						            {
						              String errorcode = commonMethods.getErrorDesc("BOEAD1", "N100");
						              Object[] arg = { "0", "E", errorcode, 
						                "Input" };
						              setErrorvalues(arg);
						            }
						          }
						          else if (commonMethods.isNull(boeVO.getAdCode()))
						          {
						            String errorcode = commonMethods.getErrorDesc("BOEAD1", "N100");
						            Object[] arg = { "0", "E", errorcode, "Input" };
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
						          else
						          {
						            int posCount = boeManyBillNoBD.validatePos(commonMethods.getEmptyIfNull(boeVO.getPos()).trim());
						            if (posCount == 0)
						            {
						              String errorcode = "Invalid Port of Shipment";
						              Object[] arg = { "0", "E", errorcode, "Input" };
						              setErrorvalues(arg);
						            }
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
						          if (commonMethods.isNull(boeVO.getIeCode()))
						          {
						            String errorcode = commonMethods.getErrorDesc("BOE04", "N100");
						            Object[] arg = { "0", "E", errorcode, "Input" };
						            setErrorvalues(arg);
						          }
						          else
						          {
						            if (commonMethods.isNull(boeVO.getCustName()))
						            {
						              String errorcode = "Customer Name Should not empty";
						              Object[] arg = { "0", "E", errorcode, "Input" };
						              setErrorvalues(arg);
						            }
						            if (commonMethods.isNull(boeVO.getIepan()))
						            {
						              String errorcode = "IE Pan Number Should not empty";
						              Object[] arg = { "0", "E", errorcode, "Input" };
						              setErrorvalues(arg);
						            }
						            if (commonMethods.isNull(boeVO.getIeadd()))
						            {
						              String errorcode = "IE Address Should not empty";
						              Object[] arg = { "0", "E", errorcode, "Input" };
						              setErrorvalues(arg);
						            }
						          }
						          int ieCodeCount = boeManyBillNoBD.checkIECodeData(boeVO);
						          if (ieCodeCount == 0)
						          {
						            String errorcode = commonMethods.getErrorDesc("BOE22", "N100");
						            Object[] arg = { "0", "E", errorcode, "Input" };
						            setErrorvalues(arg);
						          }
						          if (ieCodeCount == 0)
						          {
						            String errorcode = commonMethods.getErrorDesc("BOE22", "N100");
						            Object[] arg = { "0", "E", errorcode, "Input" };
						            setErrorvalues(arg);
						          }
						        }
						      }
						    }
						    catch (Exception exception)
						    {
						      exception.printStackTrace();
						    }
						  }
						  public String checkBOEStatus()
						    throws ApplicationException
						  {
						    logger.info("Entering Method");
						    BoePriceReferenceToManyBillNoBD boeManyBillNoBD = null;
						    int status = 0;
						    try
						    {
						      boeManyBillNoBD = new BoePriceReferenceToManyBillNoBD();
						      if (this.boeData != null)
						      {
						        status = boeManyBillNoBD.checkBOEStatus(this.boeData);
						        setBoeStatus(String.valueOf(status));
						      }
						    }
						    catch (Exception exception)
						    {
						      throwApplicationException(exception);
						    }
						    logger.info("Exiting Method");
						    return "success";
						  }
						  public String fetchCheckerManualBOE()
						    throws ApplicationException
						  {
						    logger.info("Entering Method");
						    BoeCheckerBD boeCheckerBD = null;
						    try
						    {
						      boeCheckerBD = BoeCheckerBD.getBD();
						      if (this.boeSearchVO != null) {
						        this.boeList = boeCheckerBD.fetchCheckerManualBOE(this.boeSearchVO);
						      }
						    }
						    catch (BusinessException exception)
						    {
						      throwApplicationException(exception);
						    }
						    logger.info("Exiting Method");
						    return "success";
						  }
						  public String getBoeDetails()
						    throws ApplicationException
						  {
						    logger.info("Entering Method");
						    BoeCheckerBD boeCheckerBD = null;
						    try
						    {
						      boeCheckerBD = BoeCheckerBD.getBD();
						      this.boeVO = boeCheckerBD.getBoeDetails(this.boeData);
						      if (this.boeVO != null) {
						        this.boeInvoiceList = this.boeVO.getBoeInvoiceList();
						      }
						    }
						    catch (Exception e)
						    {
						      throwApplicationException(e);
						    }
						    logger.info("Exiting Method");
						    return "success";
						  }
						  public String updateManualBOE()
						    throws ApplicationException
						  {
						    logger.info("Entering Method");
						    BoeCheckerBD boeCheckerBD = null;
						    try
						    {
						      boeCheckerBD = BoeCheckerBD.getBD();
						      boeCheckerBD.updateManualBOE(this.manualchkList, this.boeAuthorizeStatus, this.remarks);
						      if (this.boeSearchVO != null) {
						        this.boeList = boeCheckerBD.fetchCheckerManualBOE(this.boeSearchVO);
						      }
						      this.remarks = "";
						    }
						    catch (Exception e)
						    {
						      throwApplicationException(e);
						    }
						    logger.info("Exiting Method");
						    return "success";
						  }
						  
						  public String manualBOESearch()
								    throws ApplicationException
								  {
								    logger.info("Entering Method");
								    BoeCheckerBD boeCheckerBD = null;
								    try
								    {
								      boeCheckerBD = BoeCheckerBD.getBD();
								      if (this.boeSearchVO != null) {
								        this.boeList = boeCheckerBD.fetchManualBOESearch(this.boeSearchVO);
								      }
								    }
								    catch (BusinessException exception)
								    {
								      throwApplicationException(exception);
								    }
								    logger.info("Exiting Method");
								    return "success";
								  }
								  public String getManualBoeDetails()
								    throws ApplicationException
								  {
								    logger.info("Entering Method");
								    BoeCheckerBD boeCheckerBD = null;
								    try
								    {
								      boeCheckerBD = BoeCheckerBD.getBD();
								      this.boeVO = boeCheckerBD.getManualBoeDetails(this.boeData);
								      if (this.boeVO != null) {
								        this.boeInvoiceList = this.boeVO.getBoeInvoiceList();
								      }
								    }
								    catch (Exception e)
								    {
								      throwApplicationException(e);
								    }
								    logger.info("Exiting Method");
								    return "success";
								  }
								  public String manualPopCIFDetails()
								    throws ApplicationException
								  {
								    logger.info("Entering Method");
								    BoePriceReferenceToManyBillNoBD boeManyBillNoBD = null;
								    try
								    {
								      boeManyBillNoBD = BoePriceReferenceToManyBillNoBD.getBD();
								      if (this.boeVO != null)
								      {
								        this.boeVO = boeManyBillNoBD.manualPopCIFDetails(this.boeVO);
								        if (this.boeVO != null)
								        {
								          this.boeVO = boeManyBillNoBD.revertManualBOEDetails(this.boeVO);
								          if ((this.boeVO.getInvoiceList() != null) && (!this.boeVO.getInvoiceList().isEmpty())) {
								            this.invoiceList = this.boeVO.getInvoiceList();
								          }
								        }
								      }
								    }
								    catch (Exception e)
								    {
								      throwApplicationException(e);
								    }
								    logger.info("Exiting Method");
								    return "success";
								  }
								  public String getPortOfShipmentList()
								    throws ApplicationException
								  {
								    logger.info("Entering Method");
								    logger.info("Exiting Method");
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
								  public String portOfDischargeToBOE()
								  {
								    logger.info("Entering Method");
								    logger.info("Exiting Method");
								    return "success";
								  }
								}