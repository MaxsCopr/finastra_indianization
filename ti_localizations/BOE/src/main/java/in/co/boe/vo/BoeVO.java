package in.co.boe.vo;

import java.util.ArrayList;
import org.apache.log4j.Logger;
 
public class BoeVO
{
  private static Logger logger = Logger.getLogger(BoeVO.class.getName());
  private String boeNo;
  private String cifNo;
  private String boeType;
  private String boeCurr;
  private String boeDate;
  private String custName;
  private String ieCode;
  private String ieCodeChange;
  private String boeAmt;
  private String custNo;
  private String custCIFNo;
  private String eventRefno;
  private String paymentRefNo;
  private String paymentCurr;
  private String endorseAmt;
  private String outAmt;
  private String outAmt1;
  private String paymentAmount;
  private String payDate;
  private String fullyAlloc;
  private String partPaymentSlNo;
  private String balEndorseAmt;
  private String adEndorseAmt;
  private String actualEndorseAmt;
  private String initEndorseAmt;
  private String fullyAlloc_temp;
  private String endorseAmt_temp;
  private String blocked;
  private String country;
  private String exRate;
  private String boeAllocAmt;
  public String getEventRefno()
  {
    return this.eventRefno;
  }
  public void setEventRefno(String eventRefno)
  {
    this.eventRefno = eventRefno;
  }
  private double sumofBoeAllocAmt = 0.0D;
  private String customerNo;
  private String customerCIFNo;
  private String customerName;
  private String Status;
  private String benefName;
  private String benefCountry;
  private String invoiceSerialNumber;
  private String invoiceNumber;
  private String invoiceAmount;
  private String realizedAmount;
  private String realizedAmountIC;
  private String igmNo;
  private String igmDate;
  private String hblNo;
  private String hblDate;
  private String mblNo;
  private String mblDate;
  private String recInd;
  private String imAgency;
  private String adCode;
  private String pos;
  private String invoiceCurr;
  private String termsofInvoice;
  private String supplierName;
  private String supplierAddress;
  private String supplierCountry;
  private String sellerName;
  private String sellerAddress;
  private String sellerCountry;
  private String freightAmount;
  private String freightCurrencyCode;
  private String insuranceAmount;
  private String insuranceCurrencyCode;
  private String agencyCommission;
  private String agencyCurrency;
  private String discountCharges;
  private String discountCurrency;
  private String miscellaneousCharges;
  private String miscellaneousCurrency;
  private String thirdPartyName;
  private String thirdPartyAddress;
  private String thirdPartyCountry;
  private String currentSINo;
  private String thrdParty;
  private String custIECode;
  private String pordisc;
  private String iename;
  private String ieadd;
  private String iepan;
  private String govprv;
  private String boeExRate;
  private String eqPaymentAmount;
  private String gpType;
  private String adcod;
  private String transType;
  private String totalEndorseAmt;
  private String portCode;
  private String actualEndorseAmt_temp;
  private String adEndorseAmt_temp;
  private ArrayList<TransactionVO> invoiceList;
  private ArrayList<MeditorVO> paymentList;
  private String buttonType;
  private String pageType;
  private String invValue;
  private String manualPartialData;
  private ArrayList<InvoiceDetailsVO> boeInvoiceList;
  private ArrayList<TransactionVO> boePaymentList;
  private String ieAddr1;
  private String ieAddr2;
  private String ieAddr3;
  private String ieAddr4;
  private String emailID;
  private String overridStatus;
  private String hideFromWarning;
  private String boeBesSBInd;
  private String boeBesMBInd;
  private String okIdFlg;
  private String toRejectedPage;
  private String btnModify;
  private String[] bill_chkList = null;
  private String boeValue;
  private String invoicVal;
  private String flg;
  private String boestatus;
  private String recordCount = null;
  private String insertCount = null;
  private int updateCount;
  private String outAmt_temp;
  private String billAmt;
  private String currAmt;
  private String convAmt;
  private String allocAmt;
  private String remarks;
  private String endorse;
  private String out;
  private String actualAmt;
  private String fully;
  private String billCurrency;
  private String outstandingAmt;
  private String paymentDate;
  private String portId;
  private String portLocation;
  private String countryName;
  private String customerCIF;
  private String payRef;
  private String eveRef;
  private String osAmt;
  private String txnDate;
  private String ormFrmDate;
  private String ormToDate;
  private String searchBoeNo;
  private String boeIeCode;
  private String ackNackStatus;
  private String boeNumberSearch;
  private String boeDateSearch;
  private String boePortCodeSearch;
  private String boeOsAmt;
  private String boeFrmDate;
  private String boeToDate;
  private String ormNum;
  public int getUpdateCount()
  {
    return this.updateCount;
  }
  public void setUpdateCount(int updateCount)
  {
    this.updateCount = updateCount;
  }
  public String getRecordCount()
  {
    return this.recordCount;
  }
  public void setRecordCount(String recordCount)
  {
    this.recordCount = recordCount;
  }
  public String getInsertCount()
  {
    return this.insertCount;
  }
  public void setInsertCount(String insertCount)
  {
    this.insertCount = insertCount;
  }
  public String getBoestatus()
  {
    return this.boestatus;
  }
  public void setBoestatus(String boestatus)
  {
    this.boestatus = boestatus;
  }
  public String getFlg()
  {
    return this.flg;
  }
  public void setFlg(String flg)
  {
    this.flg = flg;
  }
  public String getInvoicVal()
  {
    return this.invoicVal;
  }
  public void setInvoicVal(String invoicVal)
  {
    this.invoicVal = invoicVal;
  }
  public String getBoeValue()
  {
    return this.boeValue;
  }
  public void setBoeValue(String boeValue)
  {
    this.boeValue = boeValue;
  }
  public String[] getBill_chkList()
  {
    return this.bill_chkList;
  }
  public void setBill_chkList(String[] bill_chkList)
  {
    this.bill_chkList = bill_chkList;
  }
  public String getBtnModify()
  {
    return this.btnModify;
  }
  public void setBtnModify(String btnModify)
  {
    this.btnModify = btnModify;
  }
  public String getToRejectedPage()
  {
    return this.toRejectedPage;
  }
  public void setToRejectedPage(String toRejectedPage)
  {
    this.toRejectedPage = toRejectedPage;
  }
  public String getOkIdFlg()
  {
    return this.okIdFlg;
  }
  public void setOkIdFlg(String okIdFlg)
  {
    this.okIdFlg = okIdFlg;
  }
  public String getBoeBesMBInd()
  {
    return this.boeBesMBInd;
  }
  public void setBoeBesMBInd(String boeBesMBInd)
  {
    this.boeBesMBInd = boeBesMBInd;
  }
  public void setBoeBesSBInd(String boeBesSBInd)
  {
    this.boeBesSBInd = boeBesSBInd;
  }
  public String getBoeBesSBInd()
  {
    return this.boeBesSBInd;
  }
  public String getHideFromWarning()
  {
    logger.info("The hideFromWarning in Set Method is : " + this.hideFromWarning);
    return this.hideFromWarning;
  }
  public void setHideFromWarning(String hideFromWarning)
  {
    logger.info("The hideFromWarning in Set Method is : " + hideFromWarning);
    this.hideFromWarning = hideFromWarning;
  }
  public String getOverridStatus()
  {
    return this.overridStatus;
  }
  public void setOverridStatus(String overridStatus)
  {
    this.overridStatus = overridStatus;
  }
  public String getEmailID()
  {
    return this.emailID;
  }
  public void setEmailID(String emailID)
  {
    this.emailID = emailID;
  }
  public String getIeAddr1()
  {
    return this.ieAddr1;
  }
  public void setIeAddr1(String ieAddr1)
  {
    this.ieAddr1 = ieAddr1;
  }
  public String getIeAddr2()
  {
    return this.ieAddr2;
  }
  public void setIeAddr2(String ieAddr2)
  {
    this.ieAddr2 = ieAddr2;
  }
  public String getIeAddr3()
  {
    return this.ieAddr3;
  }
  public void setIeAddr3(String ieAddr3)
  {
    this.ieAddr3 = ieAddr3;
  }
  public String getIeAddr4()
  {
    return this.ieAddr4;
  }
  public void setIeAddr4(String ieAddr4)
  {
    this.ieAddr4 = ieAddr4;
  }
  public ArrayList<TransactionVO> getBoePaymentList()
  {
    return this.boePaymentList;
  }
  public void setBoePaymentList(ArrayList<TransactionVO> boePaymentList)
  {
    this.boePaymentList = boePaymentList;
  }
  public ArrayList<InvoiceDetailsVO> getBoeInvoiceList()
  {
    return this.boeInvoiceList;
  }
  public void setBoeInvoiceList(ArrayList<InvoiceDetailsVO> boeInvoiceList)
  {
    this.boeInvoiceList = boeInvoiceList;
  }
  public ArrayList<MeditorVO> getPaymentList()
  {
    return this.paymentList;
  }
  public void setPaymentList(ArrayList<MeditorVO> paymentList)
  {
    this.paymentList = paymentList;
  }
  public String getManualPartialData()
  {
    return this.manualPartialData;
  }
  public void setManualPartialData(String manualPartialData)
  {
    this.manualPartialData = manualPartialData;
  }
  public String getInvValue()
  {
    return this.invValue;
  }
  public void setInvValue(String invValue)
  {
    this.invValue = invValue;
  }
  public String getButtonType()
  {
    return this.buttonType;
  }
  public void setButtonType(String buttonType)
  {
    this.buttonType = buttonType;
  }
  public String getPageType()
  {
    return this.pageType;
  }
  public void setPageType(String pageType)
  {
    this.pageType = pageType;
  }
  public ArrayList<TransactionVO> getInvoiceList()
  {
    return this.invoiceList;
  }
  public void setInvoiceList(ArrayList<TransactionVO> invoiceList)
  {
    this.invoiceList = invoiceList;
  }
  public String getActualEndorseAmt_temp()
  {
    return this.actualEndorseAmt_temp;
  }
  public void setActualEndorseAmt_temp(String actualEndorseAmt_temp)
  {
    this.actualEndorseAmt_temp = actualEndorseAmt_temp;
  }
  public String getAdEndorseAmt_temp()
  {
    return this.adEndorseAmt_temp;
  }
  public void setAdEndorseAmt_temp(String adEndorseAmt_temp)
  {
    this.adEndorseAmt_temp = adEndorseAmt_temp;
  }
  public String getPortCode()
  {
    return this.portCode;
  }
  public void setPortCode(String portCode)
  {
    this.portCode = portCode;
  }
  public String getTotalEndorseAmt()
  {
    return this.totalEndorseAmt;
  }
  public void setTotalEndorseAmt(String totalEndorseAmt)
  {
    this.totalEndorseAmt = totalEndorseAmt;
  }
  public String getTransType()
  {
    return this.transType;
  }
  public void setTransType(String transType)
  {
    this.transType = transType;
  }
  public String getEqPaymentAmount()
  {
    return this.eqPaymentAmount;
  }
  public void setEqPaymentAmount(String eqPaymentAmount)
  {
    this.eqPaymentAmount = eqPaymentAmount;
  }
  public String getAdcod()
  {
    return this.adcod;
  }
  public void setAdcod(String adcod)
  {
    this.adcod = adcod;
  }
  public String getGpType()
  {
    return this.gpType;
  }
  public void setGpType(String gpType)
  {
    this.gpType = gpType;
  }
  public String getGovprv()
  {
    return this.govprv;
  }
  public void setGovprv(String govprv)
  {
    this.govprv = govprv;
  }
  public String getPordisc()
  {
    return this.pordisc;
  }
  public void setPordisc(String pordisc)
  {
    this.pordisc = pordisc;
  }
  public String getIename()
  {
    return this.iename;
  }
  public void setIename(String iename)
  {
    this.iename = iename;
  }
  public String getIeadd()
  {
    return this.ieadd;
  }
  public void setIeadd(String ieadd)
  {
    this.ieadd = ieadd;
  }
  public String getIepan()
  {
    return this.iepan;
  }
  public void setIepan(String iepan)
  {
    this.iepan = iepan;
  }
  public String getSupplierName()
  {
    return this.supplierName;
  }
  public void setSupplierName(String supplierName)
  {
    this.supplierName = supplierName;
  }
  public String getSupplierAddress()
  {
    return this.supplierAddress;
  }
  public void setSupplierAddress(String supplierAddress)
  {
    this.supplierAddress = supplierAddress;
  }
  public String getSupplierCountry()
  {
    return this.supplierCountry;
  }
  public void setSupplierCountry(String supplierCountry)
  {
    this.supplierCountry = supplierCountry;
  }
  public String getSellerName()
  {
    return this.sellerName;
  }
  public void setSellerName(String sellerName)
  {
    this.sellerName = sellerName;
  }
  public String getSellerAddress()
  {
    return this.sellerAddress;
  }
  public void setSellerAddress(String sellerAddress)
  {
    this.sellerAddress = sellerAddress;
  }
  public String getSellerCountry()
  {
    return this.sellerCountry;
  }
  public void setSellerCountry(String sellerCountry)
  {
    this.sellerCountry = sellerCountry;
  }
  public String getFreightAmount()
  {
    return this.freightAmount;
  }
  public void setFreightAmount(String freightAmount)
  {
    this.freightAmount = freightAmount;
  }
  public String getFreightCurrencyCode()
  {
    return this.freightCurrencyCode;
  }
  public void setFreightCurrencyCode(String freightCurrencyCode)
  {
    this.freightCurrencyCode = freightCurrencyCode;
  }
  public String getInsuranceAmount()
  {
    return this.insuranceAmount;
  }
  public void setInsuranceAmount(String insuranceAmount)
  {
    this.insuranceAmount = insuranceAmount;
  }
  public String getInsuranceCurrencyCode()
  {
    return this.insuranceCurrencyCode;
  }
  public void setInsuranceCurrencyCode(String insuranceCurrencyCode)
  {
    this.insuranceCurrencyCode = insuranceCurrencyCode;
  }
  public String getAgencyCommission()
  {
    return this.agencyCommission;
  }
  public void setAgencyCommission(String agencyCommission)
  {
    this.agencyCommission = agencyCommission;
  }
  public String getAgencyCurrency()
  {
    return this.agencyCurrency;
  }
  public void setAgencyCurrency(String agencyCurrency)
  {
    this.agencyCurrency = agencyCurrency;
  }
  public String getDiscountCharges()
  {
    return this.discountCharges;
  }
  public void setDiscountCharges(String discountCharges)
  {
    this.discountCharges = discountCharges;
  }
  public String getDiscountCurrency()
  {
    return this.discountCurrency;
  }
  public void setDiscountCurrency(String discountCurrency)
  {
    this.discountCurrency = discountCurrency;
  }
  public String getMiscellaneousCharges()
  {
    return this.miscellaneousCharges;
  }
  public void setMiscellaneousCharges(String miscellaneousCharges)
  {
    this.miscellaneousCharges = miscellaneousCharges;
  }
  public String getMiscellaneousCurrency()
  {
    return this.miscellaneousCurrency;
  }
  public void setMiscellaneousCurrency(String miscellaneousCurrency)
  {
    this.miscellaneousCurrency = miscellaneousCurrency;
  }
  public String getThirdPartyName()
  {
    return this.thirdPartyName;
  }
  public void setThirdPartyName(String thirdPartyName)
  {
    this.thirdPartyName = thirdPartyName;
  }
  public String getThirdPartyAddress()
  {
    return this.thirdPartyAddress;
  }
  public void setThirdPartyAddress(String thirdPartyAddress)
  {
    this.thirdPartyAddress = thirdPartyAddress;
  }
  public String getThirdPartyCountry()
  {
    return this.thirdPartyCountry;
  }
  public void setThirdPartyCountry(String thirdPartyCountry)
  {
    this.thirdPartyCountry = thirdPartyCountry;
  }
  public String getRecInd()
  {
    return this.recInd;
  }
  public void setRecInd(String recInd)
  {
    this.recInd = recInd;
  }
  public String getImAgency()
  {
    return this.imAgency;
  }
  public void setImAgency(String imAgency)
  {
    this.imAgency = imAgency;
  }
  public String getAdCode()
  {
    return this.adCode;
  }
  public void setAdCode(String adCode)
  {
    this.adCode = adCode;
  }
  public String getPos()
  {
    return this.pos;
  }
  public void setPos(String pos)
  {
    this.pos = pos;
  }
  public String getIgmNo()
  {
    return this.igmNo;
  }
  public void setIgmNo(String igmNo)
  {
    this.igmNo = igmNo;
  }
  public String getIgmDate()
  {
    return this.igmDate;
  }
  public void setIgmDate(String igmDate)
  {
    this.igmDate = igmDate;
  }
  public String getHblNo()
  {
    return this.hblNo;
  }
  public void setHblNo(String hblNo)
  {
    this.hblNo = hblNo;
  }
  public String getHblDate()
  {
    return this.hblDate;
  }
  public void setHblDate(String hblDate)
  {
    this.hblDate = hblDate;
  }
  public String getMblNo()
  {
    return this.mblNo;
  }
  public void setMblNo(String mblNo)
  {
    this.mblNo = mblNo;
  }
  public String getMblDate()
  {
    return this.mblDate;
  }
  public void setMblDate(String mblDate)
  {
    this.mblDate = mblDate;
  }
  public String getInvoiceSerialNumber()
  {
    return this.invoiceSerialNumber;
  }
  public void setInvoiceSerialNumber(String invoiceSerialNumber)
  {
    this.invoiceSerialNumber = invoiceSerialNumber;
  }
  public String getInvoiceNumber()
  {
    return this.invoiceNumber;
  }
  public void setInvoiceNumber(String invoiceNumber)
  {
    this.invoiceNumber = invoiceNumber;
  }
  public String getInvoiceAmount()
  {
    return this.invoiceAmount;
  }
  public void setInvoiceAmount(String invoiceAmount)
  {
    this.invoiceAmount = invoiceAmount;
  }
  public String getRealizedAmount()
  {
    return this.realizedAmount;
  }
  public void setRealizedAmount(String realizedAmount)
  {
    this.realizedAmount = realizedAmount;
  }
  public String getBenefName()
  {
    return this.benefName;
  }
  public void setBenefName(String benefName)
  {
    this.benefName = benefName;
  }
  public String getBenefCountry()
  {
    return this.benefCountry;
  }
  public void setBenefCountry(String benefCountry)
  {
    this.benefCountry = benefCountry;
  }
  public String getExRate()
  {
    return this.exRate;
  }
  public void setExRate(String exRate)
  {
    this.exRate = exRate;
  }
  public String getBoeAllocAmt()
  {
    return this.boeAllocAmt;
  }
  public void setBoeAllocAmt(String boeAllocAmt)
  {
    this.boeAllocAmt = boeAllocAmt;
  }
  public String getStatus()
  {
    return this.Status;
  }
  public void setStatus(String status)
  {
    this.Status = status;
  }
  public String getCustomerNo()
  {
    return this.customerNo;
  }
  public void setCustomerNo(String customerNo)
  {
    this.customerNo = customerNo;
  }
  public String getCustomerCIFNo()
  {
    return this.customerCIFNo;
  }
  public void setCustomerCIFNo(String customerCIFNo)
  {
    this.customerCIFNo = customerCIFNo;
  }
  public String getCustomerName()
  {
    return this.customerName;
  }
  public void setCustomerName(String customerName)
  {
    this.customerName = customerName;
  }
  public String getBlocked()
  {
    return this.blocked;
  }
  public void setBlocked(String blocked)
  {
    this.blocked = blocked;
  }
  public String getCountry()
  {
    return this.country;
  }
  public void setCountry(String country)
  {
    this.country = country;
  }
  public String getCustNo()
  {
    return this.custNo;
  }
  public void setCustNo(String custNo)
  {
    this.custNo = custNo;
  }
  public String getCustCIFNo()
  {
    return this.custCIFNo;
  }
  public void setCustCIFNo(String custCIFNo)
  {
    this.custCIFNo = custCIFNo;
  }
  public String getEndorseAmt_temp()
  {
    return this.endorseAmt_temp;
  }
  public void setEndorseAmt_temp(String endorseAmt_temp)
  {
    this.endorseAmt_temp = endorseAmt_temp;
  }
  public String getFullyAlloc_temp()
  {
    return this.fullyAlloc_temp;
  }
  public void setFullyAlloc_temp(String fullyAlloc_temp)
  {
    this.fullyAlloc_temp = fullyAlloc_temp;
  }
  public String getOutAmt_temp()
  {
    return this.outAmt_temp;
  }
  public void setOutAmt_temp(String outAmt_temp)
  {
    this.outAmt_temp = outAmt_temp;
  }
  public String getBillCurrency()
  {
    return this.billCurrency;
  }
  public void setBillCurrency(String billCurrency)
  {
    this.billCurrency = billCurrency;
  }
  public String getOutstandingAmt()
  {
    return this.outstandingAmt;
  }
  public void setOutstandingAmt(String outstandingAmt)
  {
    this.outstandingAmt = outstandingAmt;
  }
  public String getPaymentDate()
  {
    return this.paymentDate;
  }
  public void setPaymentDate(String paymentDate)
  {
    this.paymentDate = paymentDate;
  }
  public String getBoeNo()
  {
    return this.boeNo;
  }
  public void setBoeNo(String boeNo)
  {
    this.boeNo = boeNo;
  }
  public String getCifNo()
  {
    return this.cifNo;
  }
  public void setCifNo(String cifNo)
  {
    this.cifNo = cifNo;
  }
  public String getBoeType()
  {
    return this.boeType;
  }
  public void setBoeType(String boeType)
  {
    this.boeType = boeType;
  }
  public String getBoeCurr()
  {
    return this.boeCurr;
  }
  public void setBoeCurr(String boeCurr)
  {
    this.boeCurr = boeCurr;
  }
  public String getBoeDate()
  {
    return this.boeDate;
  }
  public void setBoeDate(String boeDate)
  {
    this.boeDate = boeDate;
  }
  public String getCustName()
  {
    return this.custName;
  }
  public void setCustName(String custName)
  {
    this.custName = custName;
  }
  public String getIeCode()
  {
    return this.ieCode;
  }
  public void setIeCode(String ieCode)
  {
    this.ieCode = ieCode;
  }
  public String getBoeAmt()
  {
    return this.boeAmt;
  }
  public void setBoeAmt(String boeAmt)
  {
    this.boeAmt = boeAmt;
  }
  public String getPaymentRefNo()
  {
    return this.paymentRefNo;
  }
  public void setPaymentRefNo(String paymentRefNo)
  {
    this.paymentRefNo = paymentRefNo;
  }
  public String getPaymentCurr()
  {
    return this.paymentCurr;
  }
  public void setPaymentCurr(String paymentCurr)
  {
    this.paymentCurr = paymentCurr;
  }
  public String getEndorseAmt()
  {
    return this.endorseAmt;
  }
  public void setEndorseAmt(String endorseAmt)
  {
    this.endorseAmt = endorseAmt;
  }
  public String getOutAmt()
  {
    return this.outAmt;
  }
  public void setOutAmt(String outAmt)
  {
    this.outAmt = outAmt;
  }
  public String getOutAmt1()
  {
    return this.outAmt1;
  }
  public void setOutAmt1(String outAmt1)
  {
    this.outAmt1 = outAmt1;
  }
  public String getPaymentAmount()
  {
    return this.paymentAmount;
  }
  public void setPaymentAmount(String paymentAmount)
  {
    this.paymentAmount = paymentAmount;
  }
  public String getPayDate()
  {
    return this.payDate;
  }
  public void setPayDate(String payDate)
  {
    this.payDate = payDate;
  }
  public String getFullyAlloc()
  {
    return this.fullyAlloc;
  }
  public void setFullyAlloc(String fullyAlloc)
  {
    this.fullyAlloc = fullyAlloc;
  }
  public String getPartPaymentSlNo()
  {
    return this.partPaymentSlNo;
  }
  public void setPartPaymentSlNo(String partPaymentSlNo)
  {
    this.partPaymentSlNo = partPaymentSlNo;
  }
  public String getBalEndorseAmt()
  {
    return this.balEndorseAmt;
  }
  public void setBalEndorseAmt(String balEndorseAmt)
  {
    this.balEndorseAmt = balEndorseAmt;
  }
  public String getAdEndorseAmt()
  {
    return this.adEndorseAmt;
  }
  public void setAdEndorseAmt(String adEndorseAmt)
  {
    this.adEndorseAmt = adEndorseAmt;
  }
  public String getActualEndorseAmt()
  {
    return this.actualEndorseAmt;
  }
  public void setActualEndorseAmt(String actualEndorseAmt)
  {
    this.actualEndorseAmt = actualEndorseAmt;
  }
  public String getInitEndorseAmt()
  {
    return this.initEndorseAmt;
  }
  public void setInitEndorseAmt(String initEndorseAmt)
  {
    this.initEndorseAmt = initEndorseAmt;
  }
  public String getBillAmt()
  {
    return this.billAmt;
  }
  public void setBillAmt(String billAmt)
  {
    this.billAmt = billAmt;
  }
  public String getCurrAmt()
  {
    return this.currAmt;
  }
  public void setCurrAmt(String currAmt)
  {
    this.currAmt = currAmt;
  }
  public String getConvAmt()
  {
    return this.convAmt;
  }
  public void setConvAmt(String convAmt)
  {
    this.convAmt = convAmt;
  }
  public String getAllocAmt()
  {
    return this.allocAmt;
  }
  public void setAllocAmt(String allocAmt)
  {
    this.allocAmt = allocAmt;
  }
  public String getRemarks()
  {
    return this.remarks;
  }
  public void setRemarks(String remarks)
  {
    this.remarks = remarks;
  }
  public String getEndorse()
  {
    return this.endorse;
  }
  public void setEndorse(String endorse)
  {
    this.endorse = endorse;
  }
  public String getOut()
  {
    return this.out;
  }
  public void setOut(String out)
  {
    this.out = out;
  }
  public String getActualAmt()
  {
    return this.actualAmt;
  }
  public void setActualAmt(String actualAmt)
  {
    this.actualAmt = actualAmt;
  }
  public String getFully()
  {
    return this.fully;
  }
  public void setFully(String fully)
  {
    this.fully = fully;
  }
  public String getInvoiceCurr()
  {
    return this.invoiceCurr;
  }
  public void setInvoiceCurr(String invoiceCurr)
  {
    this.invoiceCurr = invoiceCurr;
  }
  public String getTermsofInvoice()
  {
    return this.termsofInvoice;
  }
  public void setTermsofInvoice(String termsofInvoice)
  {
    this.termsofInvoice = termsofInvoice;
  }
  public double getSumofBoeAllocAmt()
  {
    return this.sumofBoeAllocAmt;
  }
  public void setSumofBoeAllocAmt(double sumofBoeAllocAmt)
  {
    this.sumofBoeAllocAmt = sumofBoeAllocAmt;
  }
  public String getCurrentSINo()
  {
    return this.currentSINo;
  }
  public void setCurrentSINo(String currentSINo)
  {
    this.currentSINo = currentSINo;
  }
  public String getIeCodeChange()
  {
    return this.ieCodeChange;
  }
  public void setIeCodeChange(String ieCodeChange)
  {
    this.ieCodeChange = ieCodeChange;
  }
  public String getThrdParty()
  {
    return this.thrdParty;
  }
  public void setThrdParty(String thrdParty)
  {
    this.thrdParty = thrdParty;
  }
  public String getCustIECode()
  {
    return this.custIECode;
  }
  public void setCustIECode(String custIECode)
  {
    this.custIECode = custIECode;
  }
  public String getBoeExRate()
  {
    return this.boeExRate;
  }
  public void setBoeExRate(String boeExRate)
  {
    this.boeExRate = boeExRate;
  }
  public String getRealizedAmountIC()
  {
    return this.realizedAmountIC;
  }
  public void setRealizedAmountIC(String realizedAmountIC)
  {
    this.realizedAmountIC = realizedAmountIC;
  }
  public String getPortId()
  {
    return this.portId;
  }
  public void setPortId(String portId)
  {
    this.portId = portId;
  }
  public String getPortLocation()
  {
    return this.portLocation;
  }
  public void setPortLocation(String portLocation)
  {
    this.portLocation = portLocation;
  }
  public String getCountryName()
  {
    return this.countryName;
  }
  public void setCountryName(String countryName)
  {
    this.countryName = countryName;
  }
  public String getSearchBoeNo()
  {
    return this.searchBoeNo;
  }
  public void setSearchBoeNo(String searchBoeNo)
  {
    this.searchBoeNo = searchBoeNo;
  }
  public String getBoeIeCode()
  {
    return this.boeIeCode;
  }
  public void setBoeIeCode(String boeIeCode)
  {
    this.boeIeCode = boeIeCode;
  }
  public String getCustomerCIF()
  {
    return this.customerCIF;
  }
  public void setCustomerCIF(String customerCIF)
  {
    this.customerCIF = customerCIF;
  }
  public String getPayRef()
  {
    return this.payRef;
  }
  public void setPayRef(String payRef)
  {
    this.payRef = payRef;
  }
  public String getEveRef()
  {
    return this.eveRef;
  }
  public void setEveRef(String eveRef)
  {
    this.eveRef = eveRef;
  }
  public String getOsAmt()
  {
    return this.osAmt;
  }
  public void setOsAmt(String osAmt)
  {
    this.osAmt = osAmt;
  }
  public String getTxnDate()
  {
    return this.txnDate;
  }
  public void setTxnDate(String txnDate)
  {
    this.txnDate = txnDate;
  }
  public String getOrmFrmDate()
  {
    return this.ormFrmDate;
  }
  public void setOrmFrmDate(String ormFrmDate)
  {
    this.ormFrmDate = ormFrmDate;
  }
  public String getOrmToDate()
  {
    return this.ormToDate;
  }
  public void setOrmToDate(String ormToDate)
  {
    this.ormToDate = ormToDate;
  }
  public String getAckNackStatus()
  {
    return this.ackNackStatus;
  }
  public void setAckNackStatus(String ackNackStatus)
  {
    this.ackNackStatus = ackNackStatus;
  }
  public String getBoeNumberSearch()
  {
    return this.boeNumberSearch;
  }
  public void setBoeNumberSearch(String boeNumberSearch)
  {
    this.boeNumberSearch = boeNumberSearch;
  }
  public String getBoeDateSearch()
  {
    return this.boeDateSearch;
  }
  public void setBoeDateSearch(String boeDateSearch)
  {
    this.boeDateSearch = boeDateSearch;
  }
  public String getBoePortCodeSearch()
  {
    return this.boePortCodeSearch;
  }
  public void setBoePortCodeSearch(String boePortCodeSearch)
  {
    this.boePortCodeSearch = boePortCodeSearch;
  }
  public String getBoeOsAmt()
  {
    return this.boeOsAmt;
  }
  public void setBoeOsAmt(String boeOsAmt)
  {
    this.boeOsAmt = boeOsAmt;
  }
  public String getBoeFrmDate()
  {
    return this.boeFrmDate;
  }
  public void setBoeFrmDate(String boeFrmDate)
  {
    this.boeFrmDate = boeFrmDate;
  }
  public String getBoeToDate()
  {
    return this.boeToDate;
  }
  public void setBoeToDate(String boeToDate)
  {
    this.boeToDate = boeToDate;
  }
  public String getOrmNum()
  {
    return this.ormNum;
  }
  public void setOrmNum(String ormNum)
  {
    this.ormNum = ormNum;
  }
}