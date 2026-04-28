package in.co.boe.action;

import in.co.boe.businessdelegate.allRejectedBoe.RejectedBoeBD;

import in.co.boe.businessdelegate.billnotomanypaymentreference.BoeBillNoToManyPaymentReferenceBD;

import in.co.boe.businessdelegate.exception.BusinessException;

import in.co.boe.businessdelegate.pricereftomanybill.BoePriceReferenceToManyBillNoBD;

import in.co.boe.dao.exception.ApplicationException;

import in.co.boe.utility.ActionConstants;

import in.co.boe.utility.CommonMethods;

import in.co.boe.vo.BOESearchVO;

import in.co.boe.vo.BoeVO;

import in.co.boe.vo.MeditorVO;

import in.co.boe.vo.TransactionVO;

import java.util.ArrayList;

import java.util.Map;

import org.apache.log4j.Logger;
 
public class BOERejectedAction

  extends BoeBaseAction

{

  private static final long serialVersionUID = 1L;

  private static Logger logger = Logger.getLogger(BoeCheckerAction.class.getName());

  ArrayList<TransactionVO> delBoeList = null;

  ArrayList<TransactionVO> rejectedBoeList = null;

  String check = null;

  String[] chkList = null;

  String[] bill_chkList = null;

  String remarks = null;

  String boeData = null;

  Map<String, String> gpTypeList;

  Map<String, String> transTypeList;

  BOESearchVO boeSearchVO = null;

  BoeVO boeVO = null;

  ArrayList<TransactionVO> invoiceList = null;

  ArrayList<MeditorVO> paymentList = null;

  public Map<String, String> boeBesSBIndList;

  public Map<String, String> boeBesMBIndList;

  ArrayList<TransactionVO> invoiceList1;

  public Map<String, String> getBoeBesSBIndList()

  {

    return ActionConstants.BESSB_IND;

  }

  public void setBoeBesSBIndList(Map<String, String> boeBesSBIndList)

  {

    this.boeBesSBIndList = boeBesSBIndList;

  }

  public Map<String, String> getBoeBesMBIndList()

  {

    return ActionConstants.BESMB_IND;

  }

  public void setBoeBesMBIndList(Map<String, String> boeBesMBIndList)

  {

    this.boeBesMBIndList = boeBesMBIndList;

  }

  public ArrayList<MeditorVO> getPaymentList()

  {

    return this.paymentList;

  }

  public void setPaymentList(ArrayList<MeditorVO> paymentList)

  {

    this.paymentList = paymentList;

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

  public ArrayList<TransactionVO> getInvoiceList()

  {

    return this.invoiceList;

  }

  public void setInvoiceList(ArrayList<TransactionVO> invoiceList)

  {

    this.invoiceList = invoiceList;

  }

  public String getCheck()

  {

    return this.check;

  }

  public void setCheck(String check)

  {

    this.check = check;

  }

  public String[] getChkList()

  {

    return this.chkList;

  }

  public void setChkList(String[] chkList)

  {

    this.chkList = chkList;

  }

  public String getBoeData()

  {

    return this.boeData;

  }

  public void setBoeData(String boeData)

  {

    this.boeData = boeData;

  }

  public BoeVO getBoeVO()

  {

    return this.boeVO;

  }

  public void setBoeVO(BoeVO boeVO)

  {

    this.boeVO = boeVO;

  }

  public BOESearchVO getBoeSearchVO()

  {

    return this.boeSearchVO;

  }

  public void setBoeSearchVO(BOESearchVO boeSearchVO)

  {

    this.boeSearchVO = boeSearchVO;

  }

  public ArrayList<TransactionVO> getInvoiceList1()

  {

    return this.invoiceList1;

  }

  public void setInvoiceList1(ArrayList<TransactionVO> invoiceList1)

  {

    this.invoiceList1 = invoiceList1;

  }

  public static long getSerialversionuid()

  {

    return 1L;

  }

  public ArrayList<TransactionVO> getRejectedBoeList()

  {

    return this.rejectedBoeList;

  }

  public void setRejectedBoeList(ArrayList<TransactionVO> rejectedBoeList)

  {

    this.rejectedBoeList = rejectedBoeList;

  }

  public String execute()

    throws ApplicationException

  {

    logger.info("Entering Method");

    try

    {

      logger.info("BOE Rejected Action");

    }

    catch (Exception e)

    {

      throwApplicationException(e);

    }

    logger.info("Exiting Method");

    return "success";

  }

  public String showRejectedRecords()

    throws ApplicationException

  {

    logger.info("Entering Method");

    RejectedBoeBD bd = null;

    try

    {

      if ((this.boeSearchVO != null) || (this.boeVO.getToRejectedPage().equals("Y")))

      {

        bd = new RejectedBoeBD();

        this.rejectedBoeList = bd.fetchRejectedRecords(this.boeSearchVO);

      }

    }

    catch (NullPointerException npe)

    {

      npe.printStackTrace();

    }

    catch (Exception e)

    {

      throwApplicationException(e);

    }

    logger.info("Exiting Method");

    return "success";

  }

  public String showRejectedRecordSingleBill()

    throws ApplicationException

  {

    logger.info("Entering Method");

    BoePriceReferenceToManyBillNoBD boeManyBillNoBD = null;

    CommonMethods commonMethods = null;

    try

    {

      commonMethods = new CommonMethods();

      if (this.boeData != null)

      {

        if (this.boeVO == null) {

          this.boeVO = new BoeVO();

        }

        String[] temp = this.boeData.split(":");

        this.boeVO.setBoeNo(commonMethods.getEmptyIfNull(temp[0]).trim());

        this.boeVO.setBoeDate(commonMethods.getEmptyIfNull(temp[1]).trim());

        this.boeVO.setPortCode(commonMethods.getEmptyIfNull(temp[2]).trim());

        this.boeVO.setPaymentRefNo(commonMethods.getEmptyIfNull(temp[3]).trim());

        this.boeVO.setPartPaymentSlNo(commonMethods.getEmptyIfNull(temp[4]).trim());

      }

      if (this.boeVO != null)

      {

        boeManyBillNoBD = BoePriceReferenceToManyBillNoBD.getBD();

        this.boeVO = boeManyBillNoBD.retriveDataFromTI(this.boeVO);

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

        this.boeVO.setOkIdFlg("reject");

        this.boeVO = boeManyBillNoBD.retriveDataBasedOnBillNO(this.boeVO);

        logger.info("The Ok Id Value is : Action : " + this.boeVO.getOkIdFlg());

        if ((this.boeVO.getInvoiceList() != null) && 

          (!this.boeVO.getInvoiceList().isEmpty())) {

          this.invoiceList = this.boeVO.getInvoiceList();

        }

      }

    }

    catch (BusinessException exception)

    {

      throwApplicationException(exception);

    }

    logger.info("Exiting Method");

    return "success";

  }

  public String showRejectedRecordManyBill()

    throws ApplicationException

  {

    logger.info("Entering Method");

    BoeBillNoToManyPaymentReferenceBD boeManyPayRefBD = null;

    CommonMethods commonMethods = null;

    try

    {

      boeManyPayRefBD = BoeBillNoToManyPaymentReferenceBD.getBD();

      commonMethods = new CommonMethods();

      if (this.boeData != null)

      {

        if (this.boeVO == null) {

          this.boeVO = new BoeVO();

        }

        String[] temp = this.boeData.split(":");

        this.boeVO.setBoeNo(commonMethods.getEmptyIfNull(temp[0]).trim());

        this.boeVO.setBoeDate(commonMethods.getEmptyIfNull(temp[1]).trim());

        this.boeVO.setPortCode(commonMethods.getEmptyIfNull(temp[2]).trim());

        this.boeVO.setPaymentRefNo(commonMethods.getEmptyIfNull(temp[3]).trim());

        this.boeVO.setPartPaymentSlNo(commonMethods.getEmptyIfNull(temp[4]).trim());

      }

      if (this.boeVO != null)

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

        this.boeVO.setOkIdFlg("reject");

      }

    }

    catch (BusinessException exception)

    {

      throwApplicationException(exception);

    }

    logger.info("Exiting Method");

    return "success";

  }

  public String deleteRejectedRecords()
		    throws ApplicationException
		  {
		    logger.info("Entering Method");
		    RejectedBoeBD bd = null;
		    CommonMethods commonMethods = null;
		    try
		    {
		      commonMethods = new CommonMethods();
		      bd = new RejectedBoeBD();
		      if (this.boeSearchVO != null)
		      {
		        if (this.boeVO == null) {
		          this.boeVO = new BoeVO();
		        }
		        String[] temp = this.boeData.split(":");
		        this.boeVO.setBoeNo(commonMethods.getEmptyIfNull(temp[0]).trim());
		        this.boeVO.setBoeDate(commonMethods.getEmptyIfNull(temp[1]).trim());
		        this.boeVO.setPortCode(commonMethods.getEmptyIfNull(temp[2]).trim());
		        this.boeVO.setPaymentRefNo(commonMethods.getEmptyIfNull(temp[3]).trim());
		        this.boeVO.setPartPaymentSlNo(commonMethods.getEmptyIfNull(temp[4]).trim());
		        this.boeVO = bd.deleteRejectedRecords(this.boeVO);
		      }
		    }
		    catch (Exception e)
		    {
		      throwApplicationException(e);
		    }
		    logger.info("Exiting Method");
		    return "success";
		  }
		  public String delRejRecords()
		    throws BusinessException
		  {
		    RejectedBoeBD dao = null;
		    try
		    {
		      dao = new RejectedBoeBD();

		 
		      dao.delRejRecords(this.chkList);
		      if (this.boeSearchVO != null)
		      {
		        dao = new RejectedBoeBD();
		        this.rejectedBoeList = dao.fetchRejectedRecords(this.boeSearchVO);
		      }
		    }
		    catch (Exception localException) {}
		    logger.info("Exiting Method");
		    return "success";
		  }
		  public String del_Bill_vs_Multiple_BOEs()
		    throws BusinessException
		  {
		    logger.info("------------del_Bill_vs_Multiple_BOEs--------------");
		    RejectedBoeBD dao = null;
		    try
		    {
		      dao = new RejectedBoeBD();

		 
		 
		      dao.delRejRecords_bill_VsM(this.bill_chkList);
		    }
		    catch (Exception e)
		    {
		      logger.info("del_Bill_vs_Multiple_BOEs() Exception------------------------------------------------------->" + e.getMessage());
		    }
		    return "success";
		  }
		}
