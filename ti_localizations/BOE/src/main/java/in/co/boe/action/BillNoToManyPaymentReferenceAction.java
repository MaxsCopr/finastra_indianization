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
import in.co.boe.vo.MeditorVO;
import in.co.boe.vo.TransactionVO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
 
public class BillNoToManyPaymentReferenceAction
  extends BoeBaseAction
{
  private static final long serialVersionUID = 1L;
  private static Logger logger = Logger.getLogger(BillNoToManyPaymentReferenceAction.class.getName());
  Map<String, String> gpTypeList;
  Map<String, String> transTypeList;
  Map<String, String> productTypeList;
  BoeVO boeVO = null;
  ArrayList<BillTypeVO> eventList = null;
  private String searchButton = null;
  public String getSearchButton()
  {
    return this.searchButton;
  }
  public void setSearchButton(String searchButton)
  {
    this.searchButton = searchButton;
  }
  public ArrayList<BillTypeVO> getEventList()
  {
    return this.eventList;
  }
  public void setEventList(ArrayList<BillTypeVO> eventList)
  {
    this.eventList = eventList;
  }
  ArrayList<TransactionVO> invoiceList = null;
  ArrayList<TransactionVO> boePaymentList = null;
  ArrayList<MeditorVO> paymentList = null;
  BOESearchVO searchVO = null;
  TransactionVO transactionVO = null;
  private ArrayList<AlertMessagesVO> alertMsgArray = null;
  String[] chkPayList = null;
  String[] radioId = null;
  Map<String, String> boeBesMBIndList;
  ArrayList<BOEPortSelectionVO> portList = null;
  ArrayList<BoeVO> boeList = null;
  public String[] getRadioId()
  {
    return this.radioId;
  }
  public void setRadioId(String[] radioId)
  {
    this.radioId = radioId;
  }
  ArrayList<BoeVO> dsgPortList = null;
  BOESearchVO boeSearchVO = null;
  public ArrayList<BoeVO> getBoeList()
  {
    return this.boeList;
  }
  public void setBoeList(ArrayList<BoeVO> boeList)
  {
    this.boeList = boeList;
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
  public Map<String, String> getBoeBesMBIndList()
  {
    return ActionConstants.BESMB_IND;
  }
  public void setBoeBesMBIndList(Map<String, String> boeBesMBIndList)
  {
    this.boeBesMBIndList = boeBesMBIndList;
  }
  public ArrayList<TransactionVO> getBoePaymentList()
  {
    return this.boePaymentList;
  }
  public void setBoePaymentList(ArrayList<TransactionVO> boePaymentList)
  {
    this.boePaymentList = boePaymentList;
  }
  public String[] getChkPayList()
  {
    return this.chkPayList;
  }
  public void setChkPayList(String[] chkPayList)
  {
    this.chkPayList = chkPayList;
  }
  public ArrayList<AlertMessagesVO> getAlertMsgArray()
  {
    return this.alertMsgArray;
  }
  public void setAlertMsgArray(ArrayList<AlertMessagesVO> alertMsgArray)
  {
    this.alertMsgArray = alertMsgArray;
  }
  public TransactionVO getTransactionVO()
  {
    return this.transactionVO;
  }
  public void setTransactionVO(TransactionVO transactionVO)
  {
    this.transactionVO = transactionVO;
  }
  public ArrayList<MeditorVO> getPaymentList()
  {
    return this.paymentList;
  }
  public void setPaymentList(ArrayList<MeditorVO> paymentList)
  {
    this.paymentList = paymentList;
  }
  public BOESearchVO getSearchVO()
  {
    return this.searchVO;
  }
  public void setSearchVO(BOESearchVO searchVO)
  {
    this.searchVO = searchVO;
  }
  public ArrayList<TransactionVO> getInvoiceList()
  {
    return this.invoiceList;
  }
  public void setInvoiceList(ArrayList<TransactionVO> invoiceList)
  {
    this.invoiceList = invoiceList;
  }
  public Map<String, String> getGpTypeList()
  {
    return ActionConstants.FIRM_TYPE;
  }
  public void setGpTypeList(Map<String, String> gpTypeList)
  {
    this.gpTypeList = gpTypeList;
  }
  public Map<String, String> getTransTypeList()
  {
    return ActionConstants.TRANS_TYPE;
  }
  public void setTransTypeList(Map<String, String> transTypeList)
  {
    this.transTypeList = transTypeList;
  }
  public Map<String, String> getProductTypeList()
  {
    return ActionConstants.PRODUCT_LIST;
  }
  public void setProductTypeList(Map<String, String> productTypeList)
  {
    this.productTypeList = productTypeList;
  }
  public BoeVO getBoeVO()
  {
    return this.boeVO;
  }
  public void setBoeVO(BoeVO boeVO)
  {
    this.boeVO = boeVO;
  }
  public String landingPage()
    throws ApplicationException
  {
    logger.info("Entering Method");
    try
    {
      logger.info("BillNo To Many Payment Reference Action");
    }
    catch (Exception e)
    {
      throwApplicationException(e);
    }
    logger.info("Exiting Method");
    return "success";
  }
  public String retriveDataBasedOnBOE()
    throws ApplicationException
  {
    logger.info("Entering Method");
    BoeBillNoToManyPaymentReferenceBD boeManyPayRefBD = null;
    try
    {
      boeManyPayRefBD = BoeBillNoToManyPaymentReferenceBD.getBD();
      if (this.boeVO != null)
      {
        if ((this.boeVO.getBtnModify() != null) && (!this.boeVO.getBtnModify().equalsIgnoreCase("M"))) {
          this.boeVO = boeManyPayRefBD.retriveDataBasedOnBillNO(this.boeVO);
        }
        if ((this.boeVO.getBtnModify() != null) && (this.boeVO.getBtnModify().equalsIgnoreCase("M"))) {
          this.boeVO = boeManyPayRefBD.retriveDataFromMBPayTable(this.boeVO);
        }
        if ((this.boeVO.getInvoiceList() != null) && 
          (!this.boeVO.getInvoiceList().isEmpty())) {
          this.invoiceList = this.boeVO.getInvoiceList();
        }
        if ((this.boeVO.getPaymentList() != null) && 
          (!this.boeVO.getPaymentList().isEmpty())) {
          this.paymentList = this.boeVO.getPaymentList();
        }
        if ((this.boeVO.getBoePaymentList() != null) && 
          (!this.boeVO.getBoePaymentList().isEmpty())) {
          this.boePaymentList = this.boeVO.getBoePaymentList();
        }
      }
    }
    catch (BusinessException exception)
    {
      throwApplicationException(exception);
    }
    logger.info("Exiting Method");
    logger.info("------------- Break point 2----------------");
    return "success";
  }
  
  public String fetchPaymentData()
		    throws ApplicationException
		  {
		    logger.info("-----------fetchPaymentData--------------");
		    BoeBillNoToManyPaymentReferenceBD boeManyPayRefBD = null;
		    try
		    {
		      boeManyPayRefBD = BoeBillNoToManyPaymentReferenceBD.getBD();
		      if (this.boeVO != null)
		      {
		        this.boeVO = boeManyPayRefBD.fetchPaymentData(this.boeVO, this.searchVO);
		        if ((this.boeVO.getInvoiceList() != null) && 
		          (!this.boeVO.getInvoiceList().isEmpty())) {
		          this.invoiceList = this.boeVO.getInvoiceList();
		        }
		        if ((this.boeVO.getPaymentList() != null) && 
		          (!this.boeVO.getPaymentList().isEmpty())) {
		          this.paymentList = this.boeVO.getPaymentList();
		        }
		        if ((this.boeVO.getBoePaymentList() != null) && 
		          (!this.boeVO.getBoePaymentList().isEmpty())) {
		          this.boePaymentList = this.boeVO.getBoePaymentList();
		        }
		      }
		    }
		    catch (BusinessException exception)
		    {
		      logger.info("-----------fetchPaymentData-------exception-------" + exception);
		      throwApplicationException(exception);
		    }
		    logger.info("Exiting Method");
		    return "success";
		  }
		  public String storeManyBillData()
		    throws IOException
		  {
		    logger.info("Entering Method");
		    logger.info("--------------storeManyBillData--------------");
		    logger.info("--------------storeManyBillData--------------");
		    String target = "success";
		    BoeBillNoToManyPaymentReferenceBD boeManyPayRefBD = null;
		    CommonMethods commonMethods = null;
		    try
		    {
		      boeManyPayRefBD = BoeBillNoToManyPaymentReferenceBD.getBD();
		      commonMethods = new CommonMethods();
		      if (this.boeVO != null) {
		        validateData(this.boeVO);
		      }
		      if (this.alertMsgArray.size() > 0)
		      {
		        this.boeVO = boeManyPayRefBD.retriveDataBasedOnBillNO(this.boeVO);
		        if ((this.boeVO.getInvoiceList() != null) && 
		          (!this.boeVO.getInvoiceList().isEmpty())) {
		          this.invoiceList = this.boeVO.getInvoiceList();
		        }
		        if ((this.boeVO.getPaymentList() != null) && 
		          (!this.boeVO.getPaymentList().isEmpty())) {
		          this.paymentList = this.boeVO.getPaymentList();
		        }
		        if ((this.boeVO.getBoePaymentList() != null) && 
		          (!this.boeVO.getBoePaymentList().isEmpty())) {
		          this.boePaymentList = this.boeVO.getBoePaymentList();
		        }
		      }
		      else
		      {
		        logger.info("radioId--------value-------" + this.radioId);
		        logger.info("radioId------------value---" + this.radioId);
		        for (int i = 0; i < this.radioId.length; i++)
		        {
		          String[] a = this.radioId[i].split(":");
		          logger.info("Radi--Invoice-----0000000000--------------->" + a[0]);
		          logger.info("Radi--Invoice-----0000000000--------------->" + a[0]);
		          logger.info("Radi--Invoice-----1111111111--------------->" + a[1]);
		          logger.info("Radi--Invoice-----1111111111--------------->" + a[1]);
		        }
		        String result = boeManyPayRefBD.storeBillData(this.boeVO, this.chkPayList, this.radioId);
		        logger.info("Store Data--->" + result);
		        logger.info("Store Data--->" + result);
		        if (result.equalsIgnoreCase("success"))
		        {
		          this.boeVO = boeManyPayRefBD.retriveDataBasedOnBillNO(this.boeVO);
		          if ((this.boeVO.getInvoiceList() != null) && 
		            (!this.boeVO.getInvoiceList().isEmpty())) {
		            this.invoiceList = this.boeVO.getInvoiceList();
		          }
		          if ((this.boeVO.getPaymentList() != null) && 
		            (!this.boeVO.getPaymentList().isEmpty())) {
		            this.paymentList = this.boeVO.getPaymentList();
		          }
		          if ((this.boeVO.getBoePaymentList() != null) && 
		            (!this.boeVO.getBoePaymentList().isEmpty())) {
		            this.boePaymentList = this.boeVO.getBoePaymentList();
		          }
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
		    }
		    catch (BusinessException exception)
		    {
		      logger.info("BOE STORE EXCEPTION -----------------------------------throwApplicationException(exception);+" + exception.getMessage());
		      logger.info("BOE STORE EXCEPTION -----------------------------------throwApplicationException(exception);+" + exception.getMessage());
		    }
		    catch (Exception e)
		    {
		      logger.info("Exception --------------------------" + e);
		    }
		    logger.info("Exiting Method");
		    return target;
		  }
		  public String store_boe_modification()
		    throws ApplicationException
		  {
		    logger.info("Entering Method");
		    String target = "success";
		    BoeBillNoToManyPaymentReferenceBD boeManyPayRefBD = null;
		    CommonMethods commonMethods = null;
		    try
		    {
		      boeManyPayRefBD = BoeBillNoToManyPaymentReferenceBD.getBD();
		      commonMethods = new CommonMethods();
		      if (this.boeVO != null) {
		        validateData(this.boeVO);
		      }
		      if (this.alertMsgArray.size() > 0)
		      {
		        this.boeVO = boeManyPayRefBD.retriveDataBasedOnBillNO(this.boeVO);
		        if ((this.boeVO.getInvoiceList() != null) && 
		          (!this.boeVO.getInvoiceList().isEmpty())) {
		          this.invoiceList = this.boeVO.getInvoiceList();
		        }
		        if ((this.boeVO.getPaymentList() != null) && 
		          (!this.boeVO.getPaymentList().isEmpty())) {
		          this.paymentList = this.boeVO.getPaymentList();
		        }
		        if ((this.boeVO.getBoePaymentList() != null) && 
		          (!this.boeVO.getBoePaymentList().isEmpty())) {
		          this.boePaymentList = this.boeVO.getBoePaymentList();
		        }
		      }
		      else
		      {
		        HttpServletRequest request = ServletActionContext.getRequest();
		        String chkInvoiceVal = request.getParameter("radioId");

		 
		        String result = boeManyPayRefBD.storeBillData_1(this.boeVO, this.chkPayList, this.radioId);
		        logger.info("Store Data--->" + result);
		        if (result.equalsIgnoreCase("success"))
		        {
		          this.boeVO = boeManyPayRefBD.retriveDataBasedOnBillNO(this.boeVO);
		          if ((this.boeVO.getInvoiceList() != null) && 
		            (!this.boeVO.getInvoiceList().isEmpty())) {
		            this.invoiceList = this.boeVO.getInvoiceList();
		          }
		          if ((this.boeVO.getPaymentList() != null) && 
		            (!this.boeVO.getPaymentList().isEmpty())) {
		            this.paymentList = this.boeVO.getPaymentList();
		          }
		          if ((this.boeVO.getBoePaymentList() != null) && 
		            (!this.boeVO.getBoePaymentList().isEmpty())) {
		            this.boePaymentList = this.boeVO.getBoePaymentList();
		          }
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
		    }
		    catch (BusinessException exception)
		    {
		      throwApplicationException(exception);
		    }
		    logger.info("Exiting Method");
		    return target;
		  }
		  public String invoiceData()
		    throws ApplicationException
		  {
		    logger.info("Entering Method");
		    BoePriceReferenceToManyBillNoBD boeManyBillNoBD = null;
		    CommonMethods commonMethods = null;
		    try
		    {
		      boeManyBillNoBD = BoePriceReferenceToManyBillNoBD.getBD();
		      commonMethods = new CommonMethods();
		      if (this.boeVO != null)
		      {
		        String buttonType = commonMethods.getEmptyIfNull(this.boeVO.getButtonType()).trim();
		        if ((!commonMethods.isNull(buttonType)) && 
		          (buttonType.equalsIgnoreCase("View"))) {
		          this.transactionVO = boeManyBillNoBD.fetchInvoiceData(this.boeVO);
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
		  public void setErrorvalues(Object[] arg)
		    throws ApplicationException
		  {
		    logger.info("Entering Method");
		    try
		    {
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
		        "");
		      this.alertMsgArray.add(altMsg);
		    }
		    catch (Exception exception)
		    {
		      throwApplicationException(exception);
		    }
		    logger.info("Exiting Method");
		  }
		  
		  public String backManyBillData()
				    throws ApplicationException
				  {
				    logger.info("Entering Method");
				    BoeBillNoToManyPaymentReferenceBD boeManyPayRefBD = null;
				    try
				    {
				      boeManyPayRefBD = BoeBillNoToManyPaymentReferenceBD.getBD();
				      if (this.boeVO != null)
				      {
				        this.boeVO = boeManyPayRefBD.retriveBackManyBillData(this.boeVO);
				        if ((this.boeVO.getInvoiceList() != null) && 
				          (!this.boeVO.getInvoiceList().isEmpty())) {
				          this.invoiceList = this.boeVO.getInvoiceList();
				        }
				        if ((this.boeVO.getPaymentList() != null) && 
				          (!this.boeVO.getPaymentList().isEmpty())) {
				          this.paymentList = this.boeVO.getPaymentList();
				        }
				        if ((this.boeVO.getBoePaymentList() != null) && 
				          (!this.boeVO.getBoePaymentList().isEmpty())) {
				          this.boePaymentList = this.boeVO.getBoePaymentList();
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
				    logger.info("Entering Method");
				    BoePriceReferenceToManyBillNoBD boeManyBillNoBD = null;
				    BoeBillNoToManyPaymentReferenceBD boeManyPayRefBD = null;
				    String target = "error";
				    try
				    {
				      boeManyBillNoBD = BoePriceReferenceToManyBillNoBD.getBD();
				      boeManyPayRefBD = BoeBillNoToManyPaymentReferenceBD.getBD();
				      this.transactionVO = boeManyBillNoBD.updateCrossCurrencyData(this.boeVO, this.transactionVO);
				      if (this.boeVO != null)
				      {
				        this.boeVO = boeManyPayRefBD.retriveBackManyBillData(this.boeVO);
				        if ((this.boeVO.getInvoiceList() != null) && 
				          (!this.boeVO.getInvoiceList().isEmpty())) {
				          this.invoiceList = this.boeVO.getInvoiceList();
				        }
				        if ((this.boeVO.getPaymentList() != null) && 
				          (!this.boeVO.getPaymentList().isEmpty())) {
				          this.paymentList = this.boeVO.getPaymentList();
				        }
				        if ((this.boeVO.getBoePaymentList() != null) && 
				          (!this.boeVO.getBoePaymentList().isEmpty())) {
				          this.boePaymentList = this.boeVO.getBoePaymentList();
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
				  public void validateModifyData(BoeVO boeVO)
				    throws ApplicationException
				  {
				    logger.info("Entering Method");
				    CommonMethods commonMethods = null;
				    BoeBillNoToManyPaymentReferenceBD boeManyBillNumBD = null;
				    try
				    {
				      this.alertMsgArray = new ArrayList();
				      commonMethods = new CommonMethods();
				      boeManyBillNumBD = new BoeBillNoToManyPaymentReferenceBD();
				      if ((this.alertMsgArray != null) && 
				        (this.alertMsgArray.size() > 0)) {
				        this.alertMsgArray.clear();
				      }
				      int iRet = boeManyBillNumBD.checkPendingStatus(boeVO);
				      logger.info("The Return value is : " + iRet);
				      if (iRet == 0)
				      {
				        String errorcode = "";
				        logger.info("The Error Flg : " + boeVO.getFlg());
				        if (boeVO.getFlg().equalsIgnoreCase("M")) {
				          errorcode = "Approved BOE Compination Cannot Modify";
				        }
				        if (boeVO.getFlg().equalsIgnoreCase("D")) {
				          errorcode = "Approved BOE Compination Cannot Delete";
				        }
				        Object[] arg = { "0", "E", errorcode, "Input" };
				        setErrorvalues(arg);
				      }
				    }
				    catch (Exception exception)
				    {
				      exception.printStackTrace();
				    }
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
				      if (commonMethods.isNull(boeVO.getAdCode()))
				      {
				        String errorcode = commonMethods.getErrorDesc("BOEAD2", "N100");
				        Object[] arg = { "0", "E", errorcode, "Input" };
				        setErrorvalues(arg);
				      }
				      if (commonMethods.isNull(boeVO.getPortCode()))
				      {
				        String errorcode = commonMethods.getErrorDesc("BOE15", "N100");
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
				      int crossCount = boeManyBillNoBD.isCrossCurrencyCase(boeVO);
				      if (crossCount > 0)
				      {
				        String errorcode = commonMethods.getErrorDesc("BOE27", "N100");
				        Object[] arg = { "0", "E", errorcode, "Input" };
				        setErrorvalues(arg);
				      }
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
				        Object[] arg = { "0", "E", errorcode, "Input" };
				        setErrorvalues(arg);
				      }
				      if (commonMethods.isNull(boeVO.getBoeBesMBInd()))
				      {
				        String errorcode = "BES Record Indicator is Mandatory";
				        Object[] arg = { "0", "E", errorcode, "Input" };
				        setErrorvalues(arg);
				      }
				    }
				    catch (Exception exception)
				    {
				      exception.printStackTrace();
				    }
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
				    fetchPaymentData();
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
						    throws ApplicationException
						  {
						    logger.info("Entering Method");
						    fetchPaymentData();
						    logger.info("Exiting Method");
						    return "success";
						  }
						  public String deleteBOEMBData()
						    throws ApplicationException
						  {
						    logger.info("Entering Method");
						    BoeBillNoToManyPaymentReferenceBD boeManyPayRefBD = null;
						    HttpServletRequest request = ServletActionContext.getRequest();
						    try
						    {
						      boeManyPayRefBD = BoeBillNoToManyPaymentReferenceBD.getBD();
						      if (this.boeVO != null)
						      {
						        String chkInvoiceVal = request.getParameter("radioId");
						        int iRet = boeManyPayRefBD.deleteBOEMBData(this.boeVO, this.chkPayList, chkInvoiceVal);
						        retriveDataBasedOnBOE();
						        logger.info("----------after method ------------************8");
						      }
						    }
						    catch (Exception e)
						    {
						      throwApplicationException(e);
						    }
						    logger.info("Exiting Method");
						    logger.info("Going to JSP page ---BoeSuccess.jsp---**************");
						    logger.info("-------------------Check point----------final**************");
						    return "success";
						  }
						  public String getOutstandingBOEList()
						  {
						    return "success";
						  }
						  public String fetchOutstandingBOEList()
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
						  public String outstandingBOEToBOE()
						    throws ApplicationException
						  {
						    logger.info("Entering Method");
						    fetchPaymentData();
						    logger.info("Exiting Method");
						    return "success";
						  }
						}
