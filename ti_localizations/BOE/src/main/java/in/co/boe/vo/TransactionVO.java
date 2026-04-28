package in.co.boe.vo;

import java.util.ArrayList;

public class TransactionVO
{
  private String disp_status;
  private String boeNo;
  private String cifNo;
  private String boeType;
  private String boeCurr;
  private String boeDate;
  private String custName;
  private String ieCode;
  private String boeAmt;
  private String isEdit;
  private String fromDate;
  private String product;
  private String isEditFields;
  private String status;
  private String boeAllocAmt;
  private String pendingAmt;
  private String portCode;
  private String harCode;
  private String benefName;
  private String benefCountry;
  private String filterCurrency;
  private ArrayList<InvoiceVO> invoiceList;
  private ArrayList<TransactionVO> invoiceList1;
  private String invoiceSerialNumber;
  private String invoiceNumber;
  private String invoiceAmount;
  private String realizedAmount;
  private String boeNo1;
  private String invNo;
  private String invSno;
  private String invAmt;
  private String invoiceSerialNumber1;
  private String invoiceNumber1;
  private String invoiceAmount1;
  private String realizedAmount1;
  private String realizedAmountIC;
  private String pageType;
  private String buttonType;
  private String billDate1;
  private String portStatus;
  private String invoiceCurr;
  private String termsofInvoice;
  private String manualBOE;
  private String termsofService;
  private String subProdValue;
  private String subProdId;
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
  private String invCurr;
  private String adStatus;
  private String pordisc;
  private String impagc;
  private String adcode;
  private String iecode;
  private String iename;
  private String ieadd;
  private String iepan;
  private String govprv;
  private String porshp;
  private String recindi;
  private String supname;
  private String supadd;
  private String supcon;
  private String freamt;
  private String frecuc;
  private String inamt;
  private String insucod;
  private String agecom;
  private String agencuc;
  private String dischar;
  private String discucy;
  private String mischar;
  private String miscucy;
  private String selname;
  private String seladdr;
  private String selcty;
  private String thrdname;
  private String ptyaddr;
  private String ptycty;
  private String crossBoeNo;
  private String crossTotInvAmt;
  private String crossinvSno;
  private String crossInvNo;
  private String crossFobAmt;
  private String crossFobCurr;
  private String crossInsAmt;
  private String crossInsCurr;
  private String crossExRate1;
  private String crossEqInsAmt;
  private String crossFrAmt;
  private String crossFrCurr;
  private String crossExRate2;
  private String crossEqFrAmt;
  private String crossInvAmt;
  private String payMade;
  private String equivalentInsAmt;
  private String equivalentFreAmt;
  private String exchangeRate;
  private String frExchangeRate;
  private String inExchangeRate;
  private String recordInd;
  private String alreadyRealizedAmt;
  private String alreadyRealizedAmtIC;
  private String utilityRefNo;
  private String totalInvAmt;
  private String outAmtIC;
  private String closureAmt;
  private String mblNumber;
  private String mblDate;
  private String hblNumber;
  private String hblDate;
  private String igmNumber;
  private String igmDate;
  private String errDesc;
  private String batchId;
  private String errorStatus;
  private ArrayList<TransactionVO> boeList;
  private ArrayList<TransactionVO> errorCodeDesc;
  private String realDesc;
  private String realDescOne;
  private String delBoeDetails;
  private String invPendingCount;
  private ArrayList<String> errorList;
  private ArrayList<ManualBOEBulkUploadVO> manualBoevoList;
  private ArrayList<OBBBulkUploadVO> obbvoList;
  private String productType;
  private String toDate;
  private String paymentRefNo;
  private String paymentCurr;
  private String endorseAmt;
  public String getDisp_status()
  {
    return this.disp_status;
  }
  public void setDisp_status(String disp_status)
  {
    this.disp_status = disp_status;
  }
  public ArrayList<String> getErrorList()
  {
    return this.errorList;
  }
  public void setErrorList(ArrayList<String> errorList)
  {
    this.errorList = errorList;
  }
  public ArrayList<ManualBOEBulkUploadVO> getManualBoevoList()
  {
    return this.manualBoevoList;
  }
  public void setManualBoevoList(ArrayList<ManualBOEBulkUploadVO> manualBoevoList)
  {
    this.manualBoevoList = manualBoevoList;
  }
  public ArrayList<OBBBulkUploadVO> getObbvoList()
  {
    return this.obbvoList;
  }
  public void setObbvoList(ArrayList<OBBBulkUploadVO> obbvoList)
  {
    this.obbvoList = obbvoList;
  }
  public String getInvPendingCount()
  {
    return this.invPendingCount;
  }
  public void setInvPendingCount(String invPendingCount)
  {
    this.invPendingCount = invPendingCount;
  }
  public String getDelBoeDetails()
  {
    return this.delBoeDetails;
  }
  public void setDelBoeDetails(String delBoeDetails)
  {
    this.delBoeDetails = delBoeDetails;
  }
  public String getRealDescOne()
  {
    return this.realDescOne;
  }
  public void setRealDescOne(String realDescOne)
  {
    this.realDescOne = realDescOne;
  }
  public String getRealDesc()
  {
    return this.realDesc;
  }
  public void setRealDesc(String realDesc)
  {
    this.realDesc = realDesc;
  }
  public ArrayList<TransactionVO> getErrorCodeDesc()
  {
    return this.errorCodeDesc;
  }
  public void setErrorCodeDesc(ArrayList<TransactionVO> errorCodeDesc)
  {
    this.errorCodeDesc = errorCodeDesc;
  }
  public ArrayList<TransactionVO> getBoeList()
  {
    return this.boeList;
  }
  public void setBoeList(ArrayList<TransactionVO> boeList)
  {
    this.boeList = boeList;
  }
  public String getErrorStatus()
  {
    return this.errorStatus;
  }
  public void setErrorStatus(String errorStatus)
  {
    this.errorStatus = errorStatus;
  }
  public String getMblNumber()
  {
    return this.mblNumber;
  }
  public void setMblNumber(String mblNumber)
  {
    this.mblNumber = mblNumber;
  }
  public String getMblDate()
  {
    return this.mblDate;
  }
  public void setMblDate(String mblDate)
  {
    this.mblDate = mblDate;
  }
  public String getHblNumber()
  {
    return this.hblNumber;
  }
  public void setHblNumber(String hblNumber)
  {
    this.hblNumber = hblNumber;
  }
  public String getHblDate()
  {
    return this.hblDate;
  }
  public void setHblDate(String hblDate)
  {
    this.hblDate = hblDate;
  }
  public String getIgmNumber()
  {
    return this.igmNumber;
  }
  public void setIgmNumber(String igmNumber)
  {
    this.igmNumber = igmNumber;
  }
  public String getIgmDate()
  {
    return this.igmDate;
  }
  public void setIgmDate(String igmDate)
  {
    this.igmDate = igmDate;
  }
  public String getClosureAmt()
  {
    return this.closureAmt;
  }
  public void setClosureAmt(String closureAmt)
  {
    this.closureAmt = closureAmt;
  }
  public String getOutAmtIC()
  {
    return this.outAmtIC;
  }
  public void setOutAmtIC(String outAmtIC)
  {
    this.outAmtIC = outAmtIC;
  }
  public String getTotalInvAmt()
  {
    return this.totalInvAmt;
  }
  public void setTotalInvAmt(String totalInvAmt)
  {
    this.totalInvAmt = totalInvAmt;
  }
  public String getUtilityRefNo()
  {
    return this.utilityRefNo;
  }
  public void setUtilityRefNo(String utilityRefNo)
  {
    this.utilityRefNo = utilityRefNo;
  }
  public String getAlreadyRealizedAmt()
  {
    return this.alreadyRealizedAmt;
  }
  public void setAlreadyRealizedAmt(String alreadyRealizedAmt)
  {
    this.alreadyRealizedAmt = alreadyRealizedAmt;
  }
  public String getAlreadyRealizedAmtIC()
  {
    return this.alreadyRealizedAmtIC;
  }
  public void setAlreadyRealizedAmtIC(String alreadyRealizedAmtIC)
  {
    this.alreadyRealizedAmtIC = alreadyRealizedAmtIC;
  }
  public String getRecordInd()
  {
    return this.recordInd;
  }
  public void setRecordInd(String recordInd)
  {
    this.recordInd = recordInd;
  }
  public String getFrExchangeRate()
  {
    return this.frExchangeRate;
  }
  public void setFrExchangeRate(String frExchangeRate)
  {
    this.frExchangeRate = frExchangeRate;
  }
  public String getInExchangeRate()
  {
    return this.inExchangeRate;
  }
  public void setInExchangeRate(String inExchangeRate)
  {
    this.inExchangeRate = inExchangeRate;
  }
  public String getExchangeRate()
  {
    return this.exchangeRate;
  }
  public void setExchangeRate(String exchangeRate)
  {
    this.exchangeRate = exchangeRate;
  }
  public String getEquivalentInsAmt()
  {
    return this.equivalentInsAmt;
  }
  public void setEquivalentInsAmt(String equivalentInsAmt)
  {
    this.equivalentInsAmt = equivalentInsAmt;
  }
  public String getEquivalentFreAmt()
  {
    return this.equivalentFreAmt;
  }
  public void setEquivalentFreAmt(String equivalentFreAmt)
  {
    this.equivalentFreAmt = equivalentFreAmt;
  }
  public String getCrossBoeNo()
  {
    return this.crossBoeNo;
  }
  public void setCrossBoeNo(String crossBoeNo)
  {
    this.crossBoeNo = crossBoeNo;
  }
  public String getCrossTotInvAmt()
  {
    return this.crossTotInvAmt;
  }
  public void setCrossTotInvAmt(String crossTotInvAmt)
  {
    this.crossTotInvAmt = crossTotInvAmt;
  }
  public String getCrossinvSno()
  {
    return this.crossinvSno;
  }
  public void setCrossinvSno(String crossinvSno)
  {
    this.crossinvSno = crossinvSno;
  }
  public String getCrossInvNo()
  {
    return this.crossInvNo;
  }
  public void setCrossInvNo(String crossInvNo)
  {
    this.crossInvNo = crossInvNo;
  }
  public String getCrossFobAmt()
  {
    return this.crossFobAmt;
  }
  public void setCrossFobAmt(String crossFobAmt)
  {
    this.crossFobAmt = crossFobAmt;
  }
  public String getCrossFobCurr()
  {
    return this.crossFobCurr;
  }
  public void setCrossFobCurr(String crossFobCurr)
  {
    this.crossFobCurr = crossFobCurr;
  }
  public String getCrossInsAmt()
  {
    return this.crossInsAmt;
  }
  public void setCrossInsAmt(String crossInsAmt)
  {
    this.crossInsAmt = crossInsAmt;
  }
  public String getCrossInsCurr()
  {
    return this.crossInsCurr;
  }
  public void setCrossInsCurr(String crossInsCurr)
  {
    this.crossInsCurr = crossInsCurr;
  }
  public String getCrossExRate1()
  {
    return this.crossExRate1;
  }
  public void setCrossExRate1(String crossExRate1)
  {
    this.crossExRate1 = crossExRate1;
  }
  public String getCrossEqInsAmt()
  {
    return this.crossEqInsAmt;
  }
  public void setCrossEqInsAmt(String crossEqInsAmt)
  {
    this.crossEqInsAmt = crossEqInsAmt;
  }
  public String getCrossFrAmt()
  {
    return this.crossFrAmt;
  }
  public void setCrossFrAmt(String crossFrAmt)
  {
    this.crossFrAmt = crossFrAmt;
  }
  public String getCrossFrCurr()
  {
    return this.crossFrCurr;
  }
  public void setCrossFrCurr(String crossFrCurr)
  {
    this.crossFrCurr = crossFrCurr;
  }
  public String getCrossExRate2()
  {
    return this.crossExRate2;
  }
  public void setCrossExRate2(String crossExRate2)
  {
    this.crossExRate2 = crossExRate2;
  }
  public String getCrossEqFrAmt()
  {
    return this.crossEqFrAmt;
  }
  public void setCrossEqFrAmt(String crossEqFrAmt)
  {
    this.crossEqFrAmt = crossEqFrAmt;
  }
  public String getCrossInvAmt()
  {
    return this.crossInvAmt;
  }
  public void setCrossInvAmt(String crossInvAmt)
  {
    this.crossInvAmt = crossInvAmt;
  }
  public String getGovprv()
  {
    return this.govprv;
  }
  public void setGovprv(String govprv)
  {
    this.govprv = govprv;
  }
  public String getFreamt()
  {
    return this.freamt;
  }
  public void setFreamt(String freamt)
  {
    this.freamt = freamt;
  }
  public String getFrecuc()
  {
    return this.frecuc;
  }
  public void setFrecuc(String frecuc)
  {
    this.frecuc = frecuc;
  }
  public String getInamt()
  {
    return this.inamt;
  }
  public void setInamt(String inamt)
  {
    this.inamt = inamt;
  }
  public String getInsucod()
  {
    return this.insucod;
  }
  public void setInsucod(String insucod)
  {
    this.insucod = insucod;
  }
  public String getAgecom()
  {
    return this.agecom;
  }
  public void setAgecom(String agecom)
  {
    this.agecom = agecom;
  }
  public String getAgencuc()
  {
    return this.agencuc;
  }
  public void setAgencuc(String agencuc)
  {
    this.agencuc = agencuc;
  }
  public String getDischar()
  {
    return this.dischar;
  }
  public void setDischar(String dischar)
  {
    this.dischar = dischar;
  }
  public String getDiscucy()
  {
    return this.discucy;
  }
  public void setDiscucy(String discucy)
  {
    this.discucy = discucy;
  }
  public String getMischar()
  {
    return this.mischar;
  }
  public void setMischar(String mischar)
  {
    this.mischar = mischar;
  }
  public String getMiscucy()
  {
    return this.miscucy;
  }
  public void setMiscucy(String miscucy)
  {
    this.miscucy = miscucy;
  }
  public String getSelname()
  {
    return this.selname;
  }
  public void setSelname(String selname)
  {
    this.selname = selname;
  }
  public String getSeladdr()
  {
    return this.seladdr;
  }
  public void setSeladdr(String seladdr)
  {
    this.seladdr = seladdr;
  }
  public String getSelcty()
  {
    return this.selcty;
  }
  public void setSelcty(String selcty)
  {
    this.selcty = selcty;
  }
  public String getThrdname()
  {
    return this.thrdname;
  }
  public void setThrdname(String thrdname)
  {
    this.thrdname = thrdname;
  }
  public String getPtyaddr()
  {
    return this.ptyaddr;
  }
  public void setPtyaddr(String ptyaddr)
  {
    this.ptyaddr = ptyaddr;
  }
  public String getPtycty()
  {
    return this.ptycty;
  }
  public void setPtycty(String ptycty)
  {
    this.ptycty = ptycty;
  }
  public String getPordisc()
  {
    return this.pordisc;
  }
  public void setPordisc(String pordisc)
  {
    this.pordisc = pordisc;
  }
  public String getImpagc()
  {
    return this.impagc;
  }
  public void setImpagc(String impagc)
  {
    this.impagc = impagc;
  }
  public String getAdcode()
  {
    return this.adcode;
  }
  public void setAdcode(String adcode)
  {
    this.adcode = adcode;
  }
  public String getIecode()
  {
    return this.iecode;
  }
  public void setIecode(String iecode)
  {
    this.iecode = iecode;
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
  public String getPorshp()
  {
    return this.porshp;
  }
  public void setPorshp(String porshp)
  {
    this.porshp = porshp;
  }
  public String getRecindi()
  {
    return this.recindi;
  }
  public void setRecindi(String recindi)
  {
    this.recindi = recindi;
  }
  public String getSupname()
  {
    return this.supname;
  }
  public void setSupname(String supname)
  {
    this.supname = supname;
  }
  public String getSupadd()
  {
    return this.supadd;
  }
  public void setSupadd(String supadd)
  {
    this.supadd = supadd;
  }
  public String getSupcon()
  {
    return this.supcon;
  }
  public void setSupcon(String supcon)
  {
    this.supcon = supcon;
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
  public String getInvoiceNumber1()
  {
    return this.invoiceNumber1;
  }
  public void setInvoiceNumber1(String invoiceNumber1)
  {
    this.invoiceNumber1 = invoiceNumber1;
  }
  public String getInvoiceAmount1()
  {
    return this.invoiceAmount1;
  }
  public void setInvoiceAmount1(String invoiceAmount1)
  {
    this.invoiceAmount1 = invoiceAmount1;
  }
  public String getRealizedAmount1()
  {
    return this.realizedAmount1;
  }
  public void setRealizedAmount1(String realizedAmount1)
  {
    this.realizedAmount1 = realizedAmount1;
  }
  public String getInvNo()
  {
    return this.invNo;
  }
  public void setInvNo(String invNo)
  {
    this.invNo = invNo;
  }
  public String getInvSno()
  {
    return this.invSno;
  }
  public void setInvSno(String invSno)
  {
    this.invSno = invSno;
  }
  public String getInvAmt()
  {
    return this.invAmt;
  }
  public void setInvAmt(String invAmt)
  {
    this.invAmt = invAmt;
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
  public void setIsEditFields(String isEditFields)
  {
    this.isEditFields = isEditFields;
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
  public String getFilterCurrency()
  {
    return this.filterCurrency;
  }
  public void setFilterCurrency(String filterCurrency)
  {
    this.filterCurrency = filterCurrency;
  }
  public String getPortCode()
  {
    return this.portCode;
  }
  public void setPortCode(String portCode)
  {
    this.portCode = portCode;
  }
  public String getHarCode()
  {
    return this.harCode;
  }
  public void setHarCode(String harCode)
  {
    this.harCode = harCode;
  }
  public String getStatus()
  {
    return this.status;
  }
  public void setStatus(String status)
  {
    this.status = status;
  }
  public String getPendingAmt()
  {
    return this.pendingAmt;
  }
  public void setPendingAmt(String pendingAmt)
  {
    this.pendingAmt = pendingAmt;
  }
  public String getIsEditFields()
  {
    return this.isEditFields;
  }
  public void setIsEditFieldsId(String isEditFields)
  {
    this.isEditFields = isEditFields;
  }
  public String getProduct()
  {
    return this.product;
  }
  public void setProduct(String product)
  {
    this.product = product;
  }
  public String getProductType()
  {
    return this.productType;
  }
  public void setProductType(String productType)
  {
    this.productType = productType;
  }
  public String getFromDate()
  {
    return this.fromDate;
  }
  public void setFromDate(String fromDate)
  {
    this.fromDate = fromDate;
  }
  public String getToDate()
  {
    return this.toDate;
  }
  public void setToDate(String toDate)
  {
    this.toDate = toDate;
  }
  public String getIsEdit()
  {
    return this.isEdit;
  }
  public void setIsEdit(String isEdit)
  {
    this.isEdit = isEdit;
  }
  private String outAmt = "0";
  private String paymentAmount;
  private String payDate;
  private String fullyAlloc;
  private String partPaymentSlNo;
  private String balEndorseAmt;
  private String adEndorseAmt = "0";
  private String actualEndorseAmt;
  private String initEndorseAmt = "0";
  private String outstandingAmt;
  private String paymentDate;
  private String billAmt;
  private String currAmt;
  private String convAmt;
  public String getPaymentDate()
  {
    return this.paymentDate;
  }
  public void setPaymentDate(String paymentDate)
  {
    this.paymentDate = paymentDate;
  }
  public String getOutstandingAmt()
  {
    return this.outstandingAmt;
  }
  public void setOutstandingAmt(String outstandingAmt)
  {
    this.outstandingAmt = outstandingAmt;
  }
  private String allocAmt = "0";
  private String totalEndorseAmt;
  private String fullyAlloc_temp;
  private String actualEndorseAmt_temp;
  private String remarks;
  private String adEndorseAmt_temp;
  private String outAmt_temp;
  private String billCurrency;
  public String getAdEndorseAmt_temp()
  {
    return this.adEndorseAmt_temp;
  }
  public void setAdEndorseAmt_temp(String adEndorseAmt_temp)
  {
    this.adEndorseAmt_temp = adEndorseAmt_temp;
  }
  public String getRemarks()
  {
    return this.remarks;
  }
  public void setRemarks(String remarks)
  {
    this.remarks = remarks;
  }
  public String getActualEndorseAmt_temp()
  {
    return this.actualEndorseAmt_temp;
  }
  public void setActualEndorseAmt_temp(String actualEndorseAmt_temp)
  {
    this.actualEndorseAmt_temp = actualEndorseAmt_temp;
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
  public String getTotalEndorseAmt()
  {
    return this.totalEndorseAmt;
  }
  public void setTotalEndorseAmt(String totalEndorseAmt)
  {
    this.totalEndorseAmt = totalEndorseAmt;
  }
  public String getBillCurrency()
  {
    return this.billCurrency;
  }
  public void setBillCurrency(String billCurrency)
  {
    this.billCurrency = billCurrency;
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
  public ArrayList<TransactionVO> getInvoiceList1()
  {
    return this.invoiceList1;
  }
  public void setInvoiceList1(ArrayList<TransactionVO> invoiceList1)
  {
    this.invoiceList1 = invoiceList1;
  }
  public String getBoeNo1()
  {
    return this.boeNo1;
  }
  public void setBoeNo1(String boeNo1)
  {
    this.boeNo1 = boeNo1;
  }
  public ArrayList<InvoiceVO> getInvoiceList()
  {
    return this.invoiceList;
  }
  public void setInvoiceList(ArrayList<InvoiceVO> invoiceList)
  {
    this.invoiceList = invoiceList;
  }
  public String getInvoiceSerialNumber1()
  {
    return this.invoiceSerialNumber1;
  }
  public void setInvoiceSerialNumber1(String invoiceSerialNumber1)
  {
    this.invoiceSerialNumber1 = invoiceSerialNumber1;
  }
  public String getPageType()
  {
    return this.pageType;
  }
  public void setPageType(String pageType)
  {
    this.pageType = pageType;
  }
  public String getButtonType()
  {
    return this.buttonType;
  }
  public void setButtonType(String buttonType)
  {
    this.buttonType = buttonType;
  }
  public String getBillDate1()
  {
    return this.billDate1;
  }
  public void setBillDate1(String billDate1)
  {
    this.billDate1 = billDate1;
  }
  public String getPortStatus()
  {
    return this.portStatus;
  }
  public void setPortStatus(String portStatus)
  {
    this.portStatus = portStatus;
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
  public String getManualBOE()
  {
    return this.manualBOE;
  }
  public void setManualBOE(String manualBOE)
  {
    this.manualBOE = manualBOE;
  }
  public String getTermsofService()
  {
    return this.termsofService;
  }
  public void setTermsofService(String termsofService)
  {
    this.termsofService = termsofService;
  }
  public String getBoeAllocAmt()
  {
    return this.boeAllocAmt;
  }
  public void setBoeAllocAmt(String boeAllocAmt)
  {
    this.boeAllocAmt = boeAllocAmt;
  }
  public String getSubProdValue()
  {
    return this.subProdValue;
  }
  public void setSubProdValue(String subProdValue)
  {
    this.subProdValue = subProdValue;
  }
  public String getSubProdId()
  {
    return this.subProdId;
  }
  public void setSubProdId(String subProdId)
  {
    this.subProdId = subProdId;
  }
  public String getInvCurr()
  {
    return this.invCurr;
  }
  public void setInvCurr(String invCurr)
  {
    this.invCurr = invCurr;
  }
  public String getAdStatus()
  {
    return this.adStatus;
  }
  public void setAdStatus(String adStatus)
  {
    this.adStatus = adStatus;
  }
  public String getPayMade()
  {
    return this.payMade;
  }
  public void setPayMade(String payMade)
  {
    this.payMade = payMade;
  }
  public String getRealizedAmountIC()
  {
    return this.realizedAmountIC;
  }
  public void setRealizedAmountIC(String realizedAmountIC)
  {
    this.realizedAmountIC = realizedAmountIC;
  }
  public String getErrDesc()
  {
    return this.errDesc;
  }
  public void setErrDesc(String errDesc)
  {
    this.errDesc = errDesc;
  }
  public String getBatchId()
  {
    return this.batchId;
  }
  public void setBatchId(String batchId)
  {
    this.batchId = batchId;
  }
}
