package in.co.forwardcontract.vo;

import java.util.ArrayList;

public class ForwardContractVO
{
  private String id;
  private String category;
  private String subProduct;
  private String fwdContractNo;
  private String customerID;
  private String acctNumber;
  private String branchCode;
  private String dealCurrency;
  private String bookingDate;
  private String toCurrency;
  private String dealValidFromDate;
  private String treasuryRefNo;
  private String outstandingAmt;
  private String fwdContractAmt;
  private String toCurrencyAmt;
  private String dealValidToDate;
  private String treasuryRate;
  private String limitID;
  private String withoutLimit;
  private String availableLimit;
  private String washRate;
  private String plAmount;
  private String instructions;
  private String leiNumber;
  private String chargeAmount;
  private String gstAmount;
  private String margin;
  private String screenType;
  private String rateStatus;
  private String rateBuyOrSell;
  private String billId;
  private String buyOrSell;
  private String buyAmount;
  private String sellAmount;
  private String tranType;
  private String validFrom;
  private String validTo;
  private String flag;
  private String deleteFlag;
  private String spMakerFlag;
  ArrayList<FWCPostingVO> postingList;
  private ArrayList<ForwardContractVO> fwdContractDetailsList;
  private String customerCif;
  private String customerName;
  private String goodsDescription;
 
  public String getSpMakerFlag()
  {
    return this.spMakerFlag;
  }
 
  public void setSpMakerFlag(String spMakerFlag)
  {
    this.spMakerFlag = spMakerFlag;
  }
 
  public String getDeleteFlag()
  {
    return this.deleteFlag;
  }
 
  public void setDeleteFlag(String deleteFlag)
  {
    this.deleteFlag = deleteFlag;
  }
 
  public String getFlag()
  {
    return this.flag;
  }
 
  public void setFlag(String flag)
  {
    this.flag = flag;
  }
 
  public String getValidFrom()
  {
    return this.validFrom;
  }
 
  public void setValidFrom(String validFrom)
  {
    this.validFrom = validFrom;
  }
 
  public String getValidTo()
  {
    return this.validTo;
  }
 
  public void setValidTo(String validTo)
  {
    this.validTo = validTo;
  }
 
  public String getBillId()
  {
    return this.billId;
  }
 
  public void setBillId(String billId)
  {
    this.billId = billId;
  }
 
  public String getBuyOrSell()
  {
    return this.buyOrSell;
  }
 
  public void setBuyOrSell(String buyOrSell)
  {
    this.buyOrSell = buyOrSell;
  }
 
  public String getBuyAmount()
  {
    return this.buyAmount;
  }
 
  public void setBuyAmount(String buyAmount)
  {
    this.buyAmount = buyAmount;
  }
 
  public String getSellAmount()
  {
    return this.sellAmount;
  }
 
  public void setSellAmount(String sellAmount)
  {
    this.sellAmount = sellAmount;
  }
 
  public String getTranType()
  {
    return this.tranType;
  }
 
  public void setTranType(String tranType)
  {
    this.tranType = tranType;
  }
 
  public ArrayList<FWCPostingVO> getPostingList()
  {
    return this.postingList;
  }
 
  public void setPostingList(ArrayList<FWCPostingVO> postingList)
  {
    this.postingList = postingList;
  }
 
  public String getRateStatus()
  {
    return this.rateStatus;
  }
 
  public void setRateStatus(String rateStatus)
  {
    this.rateStatus = rateStatus;
  }
 
  public String getRateBuyOrSell()
  {
    return this.rateBuyOrSell;
  }
 
  public void setRateBuyOrSell(String rateBuyOrSell)
  {
    this.rateBuyOrSell = rateBuyOrSell;
  }
 
  public String getScreenType()
  {
    return this.screenType;
  }
 
  public void setScreenType(String screenType)
  {
    this.screenType = screenType;
  }
 
  public ArrayList<ForwardContractVO> getFwdContractDetailsList()
  {
    return this.fwdContractDetailsList;
  }
 
  public void setFwdContractDetailsList(ArrayList<ForwardContractVO> fwdContractDetailsList)
  {
    this.fwdContractDetailsList = fwdContractDetailsList;
  }
 
  public String getId()
  {
    return this.id;
  }
 
  public void setId(String id)
  {
    this.id = id;
  }
 
  public String getCategory()
  {
    return this.category;
  }
 
  public void setCategory(String category)
  {
    this.category = category;
  }
 
  public String getSubProduct()
  {
    return this.subProduct;
  }
 
  public void setSubProduct(String subProduct)
  {
    this.subProduct = subProduct;
  }
 
  public String getFwdContractNo()
  {
    return this.fwdContractNo;
  }
 
  public void setFwdContractNo(String fwdContractNo)
  {
    this.fwdContractNo = fwdContractNo;
  }
 
  public String getCustomerID()
  {
    return this.customerID;
  }
 
  public void setCustomerID(String customerID)
  {
    this.customerID = customerID;
  }
 
  public String getAcctNumber()
  {
    return this.acctNumber;
  }
 
  public void setAcctNumber(String acctNumber)
  {
    this.acctNumber = acctNumber;
  }
 
  public String getBranchCode()
  {
    return this.branchCode;
  }
 
  public void setBranchCode(String branchCode)
  {
    this.branchCode = branchCode;
  }
 
  public String getDealCurrency()
  {
    return this.dealCurrency;
  }
 
  public void setDealCurrency(String dealCurrency)
  {
    this.dealCurrency = dealCurrency;
  }
 
  public String getBookingDate()
  {
    return this.bookingDate;
  }
 
  public void setBookingDate(String bookingDate)
  {
    this.bookingDate = bookingDate;
  }
 
  public String getToCurrency()
  {
    return this.toCurrency;
  }
 
  public void setToCurrency(String toCurrency)
  {
    this.toCurrency = toCurrency;
  }
 
  public String getDealValidFromDate()
  {
    return this.dealValidFromDate;
  }
 
  public void setDealValidFromDate(String dealValidFromDate)
  {
    this.dealValidFromDate = dealValidFromDate;
  }
 
  public String getTreasuryRefNo()
  {
    return this.treasuryRefNo;
  }
 
  public void setTreasuryRefNo(String treasuryRefNo)
  {
    this.treasuryRefNo = treasuryRefNo;
  }
 
  public String getOutstandingAmt()
  {
    return this.outstandingAmt;
  }
 
  public void setOutstandingAmt(String outstandingAmt)
  {
    this.outstandingAmt = outstandingAmt;
  }
 
  public String getFwdContractAmt()
  {
    return this.fwdContractAmt;
  }
 
  public void setFwdContractAmt(String fwdContractAmt)
  {
    this.fwdContractAmt = fwdContractAmt;
  }
 
  public String getToCurrencyAmt()
  {
    return this.toCurrencyAmt;
  }
 
  public void setToCurrencyAmt(String toCurrencyAmt)
  {
    this.toCurrencyAmt = toCurrencyAmt;
  }
 
  public String getDealValidToDate()
  {
    return this.dealValidToDate;
  }
 
  public void setDealValidToDate(String dealValidToDate)
  {
    this.dealValidToDate = dealValidToDate;
  }
 
  public String getTreasuryRate()
  {
    return this.treasuryRate;
  }
 
  public void setTreasuryRate(String treasuryRate)
  {
    this.treasuryRate = treasuryRate;
  }
 
  public String getLimitID()
  {
    return this.limitID;
  }
 
  public void setLimitID(String limitID)
  {
    this.limitID = limitID;
  }
 
  public String getWithoutLimit()
  {
    return this.withoutLimit;
  }
 
  public void setWithoutLimit(String withoutLimit)
  {
    this.withoutLimit = withoutLimit;
  }
 
  public String getAvailableLimit()
  {
    return this.availableLimit;
  }
 
  public void setAvailableLimit(String availableLimit)
  {
    this.availableLimit = availableLimit;
  }
 
  public String getWashRate()
  {
    return this.washRate;
  }
 
  public void setWashRate(String washRate)
  {
    this.washRate = washRate;
  }
 
  public String getPlAmount()
  {
    return this.plAmount;
  }
 
  public void setPlAmount(String plAmount)
  {
    this.plAmount = plAmount;
  }
 
  public String getInstructions()
  {
    return this.instructions;
  }
 
  public void setInstructions(String instructions)
  {
    this.instructions = instructions;
  }
 
  public String getLeiNumber()
  {
    return this.leiNumber;
  }
 
  public void setLeiNumber(String leiNumber)
  {
    this.leiNumber = leiNumber;
  }
 
  public String getChargeAmount()
  {
    return this.chargeAmount;
  }
 
  public void setChargeAmount(String chargeAmount)
  {
    this.chargeAmount = chargeAmount;
  }
 
  public String getGstAmount()
  {
    return this.gstAmount;
  }
 
  public void setGstAmount(String gstAmount)
  {
    this.gstAmount = gstAmount;
  }
 
  public String getMargin()
  {
    return this.margin;
  }
 
  public void setMargin(String margin)
  {
    this.margin = margin;
  }
 
  String userTitle = null;
  String firstName = null;
  String lastName = null;
  String address = null;
  String address2 = null;
  String address3 = null;
  String city = null;
  String pincode = null;
  String state = null;
  private String inwardNo = null;
  String cityString = null;
  String stateString = null;
  String country = null;
  String mobileNumber = null;
  String emailId = null;
  String basicinforesult = null;
  int userid;
  String refNumber;
  int ngoId;
  private String copyVal;
  String destinationCountry = null;
  String NCIFList = null;
 
  public String getCustomerName()
  {
    return this.customerName;
  }
 
  public void setCustomerName(String customerName)
  {
    this.customerName = customerName;
  }
 
  public String getDestinationCountry()
  {
    return this.destinationCountry;
  }
 
  public String getNCIFList()
  {
    return this.NCIFList;
  }
 
  public void setNCIFList(String nCIFList)
  {
    this.NCIFList = nCIFList;
  }
 
  public void setDestinationCountry(String destinationCountry)
  {
    this.destinationCountry = destinationCountry;
  }
 
  String sessionUserName = null;
  String pageType = null;
  private String chargeType;
  private String chargeId;
  private String chargeDesc;
  private String chargeKey97;
  private String productType;
  private String productId;
  private String productDesc;
  private String productKey97;
  private String updateCusCif;
  private String updateChargeId;
  private String updateChargeKey97;
  private String updateProductId;
  private String exportOrderNumber;
  private String exporterOrderDate;
  private String incoTerms;
  private String goodsCode;
  private String poValue;
  private String exportcurrency;
  private String exportexpiryDate;
  private String lastShipmentDate;
  private String freightDeduction;
  private String insuranceDeduction;
  private String marginPercentage;
  private String eligibleAmount;
  private String description;
  private int count;
  private String importerName;
  private String poNo;
  private String poDate;
  private String poCif;
  private String poBen;
  private String poInco;
  private String poGoodDesc;
  private String poImpName;
  private String poAmtValue;
  private String poExpdate;
  private String poLastShipDate;
  private String poFrDeduct;
  private String poInsDeduct;
  private String poMargin;
  private String poEligAmt;
  private ArrayList<ForwardContractVO> purchaseOrderList;
  String fromDate;
  String toDate;
  private String status;
  String statusList;
  String remarks;
  ArrayList<AlertMessagesVO> errorList;
  String cancellationamount;
  String bookingrate;
  String transid;
  String transdate;
 
  public String getPageType()
  {
    return this.pageType;
  }
 
  public void setPageType(String pageType)
  {
    this.pageType = pageType;
  }
 
  public String getSessionUserName()
  {
    return this.sessionUserName;
  }
 
  public void setSessionUserName(String sessionUserName)
  {
    this.sessionUserName = sessionUserName;
  }
 
  public String getUserTitle()
  {
    return this.userTitle;
  }
 
  public void setUserTitle(String userTitle)
  {
    this.userTitle = userTitle;
  }
 
  public String getFirstName()
  {
    return this.firstName;
  }
 
  public void setFirstName(String firstName)
  {
    this.firstName = firstName;
  }
 
  public String getLastName()
  {
    return this.lastName;
  }
 
  public void setLastName(String lastName)
  {
    this.lastName = lastName;
  }
 
  public String getAddress()
  {
    return this.address;
  }
 
  public void setAddress(String address)
  {
    this.address = address;
  }
 
  public String getCity()
  {
    return this.city;
  }
 
  public void setCity(String city)
  {
    this.city = city;
  }
 
  public String getPincode()
  {
    return this.pincode;
  }
 
  public void setPincode(String pincode)
  {
    this.pincode = pincode;
  }
 
  public String getState()
  {
    return this.state;
  }
 
  public void setState(String state)
  {
    this.state = state;
  }
 
  public String getCountry()
  {
    return this.country;
  }
 
  public void setCountry(String country)
  {
    this.country = country;
  }
 
  public String getMobileNumber()
  {
    return this.mobileNumber;
  }
 
  public void setMobileNumber(String mobileNumber)
  {
    this.mobileNumber = mobileNumber;
  }
 
  public String getEmailId()
  {
    return this.emailId;
  }
 
  public void setEmailId(String emailId)
  {
    this.emailId = emailId;
  }
 
  public String getBasicinforesult()
  {
    return this.basicinforesult;
  }
 
  public void setBasicinforesult(String basicinforesult)
  {
    this.basicinforesult = basicinforesult;
  }
 
  public int getUserid()
  {
    return this.userid;
  }
 
  public void setUserid(int userid)
  {
    this.userid = userid;
  }
 
  public String getRefNumber()
  {
    return this.refNumber;
  }
 
  public void setRefNumber(String refNumber)
  {
    this.refNumber = refNumber;
  }
 
  public int getNgoId()
  {
    return this.ngoId;
  }
 
  public void setNgoId(int ngoId)
  {
    this.ngoId = ngoId;
  }
 
  public String getAddress2()
  {
    return this.address2;
  }
 
  public void setAddress2(String address2)
  {
    this.address2 = address2;
  }
 
  public String getAddress3()
  {
    return this.address3;
  }
 
  public void setAddress3(String address3)
  {
    this.address3 = address3;
  }
 
  public String getCityString()
  {
    return this.cityString;
  }
 
  public void setCityString(String cityString)
  {
    this.cityString = cityString;
  }
 
  public String getStateString()
  {
    return this.stateString;
  }
 
  public void setStateString(String stateString)
  {
    this.stateString = stateString;
  }
 
  public String getRemarks()
  {
    return this.remarks;
  }
 
  public void setRemarks(String remarks)
  {
    this.remarks = remarks;
  }
 
  public String getStatusList()
  {
    return this.statusList;
  }
 
  public void setStatusList(String statusList)
  {
    this.statusList = statusList;
  }
 
  public String getStatus()
  {
    return this.status;
  }
 
  public void setStatus(String status)
  {
    this.status = status;
  }
 
  public String getPoNo()
  {
    return this.poNo;
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
 
  public void setPoNo(String poNo)
  {
    this.poNo = poNo;
  }
 
  public String getPoDate()
  {
    return this.poDate;
  }
 
  public void setPoDate(String poDate)
  {
    this.poDate = poDate;
  }
 
  public String getPoCif()
  {
    return this.poCif;
  }
 
  public void setPoCif(String poCif)
  {
    this.poCif = poCif;
  }
 
  public String getPoBen()
  {
    return this.poBen;
  }
 
  public void setPoBen(String poBen)
  {
    this.poBen = poBen;
  }
 
  public String getPoInco()
  {
    return this.poInco;
  }
 
  public void setPoInco(String poInco)
  {
    this.poInco = poInco;
  }
 
  public String getPoGoodDesc()
  {
    return this.poGoodDesc;
  }
 
  public void setPoGoodDesc(String poGoodDesc)
  {
    this.poGoodDesc = poGoodDesc;
  }
 
  public String getPoImpName()
  {
    return this.poImpName;
  }
 
  public void setPoImpName(String poImpName)
  {
    this.poImpName = poImpName;
  }
 
  public String getPoAmtValue()
  {
    return this.poAmtValue;
  }
 
  public void setPoAmtValue(String poAmtValue)
  {
    this.poAmtValue = poAmtValue;
  }
 
  public String getPoExpdate()
  {
    return this.poExpdate;
  }
 
  public void setPoExpdate(String poExpdate)
  {
    this.poExpdate = poExpdate;
  }
 
  public String getPoLastShipDate()
  {
    return this.poLastShipDate;
  }
 
  public void setPoLastShipDate(String poLastShipDate)
  {
    this.poLastShipDate = poLastShipDate;
  }
 
  public String getPoFrDeduct()
  {
    return this.poFrDeduct;
  }
 
  public void setPoFrDeduct(String poFrDeduct)
  {
    this.poFrDeduct = poFrDeduct;
  }
 
  public String getPoInsDeduct()
  {
    return this.poInsDeduct;
  }
 
  public void setPoInsDeduct(String poInsDeduct)
  {
    this.poInsDeduct = poInsDeduct;
  }
 
  public String getPoMargin()
  {
    return this.poMargin;
  }
 
  public void setPoMargin(String poMargin)
  {
    this.poMargin = poMargin;
  }
 
  public String getPoEligAmt()
  {
    return this.poEligAmt;
  }
 
  public void setPoEligAmt(String poEligAmt)
  {
    this.poEligAmt = poEligAmt;
  }
 
  public String getImporterName()
  {
    return this.importerName;
  }
 
  public void setImporterName(String importerName)
  {
    this.importerName = importerName;
  }
 
  public int getCount()
  {
    return this.count;
  }
 
  public void setCount(int count)
  {
    this.count = count;
  }
 
  public String getDescription()
  {
    return this.description;
  }
 
  public void setDescription(String description)
  {
    this.description = description;
  }
 
  public String getExportOrderNumber()
  {
    return this.exportOrderNumber;
  }
 
  public void setExportOrderNumber(String exportOrderNumber)
  {
    this.exportOrderNumber = exportOrderNumber;
  }
 
  public String getExporterOrderDate()
  {
    return this.exporterOrderDate;
  }
 
  public void setExporterOrderDate(String exporterOrderDate)
  {
    this.exporterOrderDate = exporterOrderDate;
  }
 
  public String getIncoTerms()
  {
    return this.incoTerms;
  }
 
  public void setIncoTerms(String incoTerms)
  {
    this.incoTerms = incoTerms;
  }
 
  public String getGoodsCode()
  {
    return this.goodsCode;
  }
 
  public void setGoodsCode(String goodsCode)
  {
    this.goodsCode = goodsCode;
  }
 
  public String getPoValue()
  {
    return this.poValue;
  }
 
  public void setPoValue(String poValue)
  {
    this.poValue = poValue;
  }
 
  public String getExportcurrency()
  {
    return this.exportcurrency;
  }
 
  public void setExportcurrency(String exportcurrency)
  {
    this.exportcurrency = exportcurrency;
  }
 
  public String getExportexpiryDate()
  {
    return this.exportexpiryDate;
  }
 
  public void setExportexpiryDate(String exportexpiryDate)
  {
    this.exportexpiryDate = exportexpiryDate;
  }
 
  public String getLastShipmentDate()
  {
    return this.lastShipmentDate;
  }
 
  public void setLastShipmentDate(String lastShipmentDate)
  {
    this.lastShipmentDate = lastShipmentDate;
  }
 
  public String getFreightDeduction()
  {
    return this.freightDeduction;
  }
 
  public void setFreightDeduction(String freightDeduction)
  {
    this.freightDeduction = freightDeduction;
  }
 
  public String getInsuranceDeduction()
  {
    return this.insuranceDeduction;
  }
 
  public void setInsuranceDeduction(String insuranceDeduction)
  {
    this.insuranceDeduction = insuranceDeduction;
  }
 
  public String getMarginPercentage()
  {
    return this.marginPercentage;
  }
 
  public void setMarginPercentage(String marginPercentage)
  {
    this.marginPercentage = marginPercentage;
  }
 
  public String getEligibleAmount()
  {
    return this.eligibleAmount;
  }
 
  public void setEligibleAmount(String eligibleAmount)
  {
    this.eligibleAmount = eligibleAmount;
  }
 
  public String getUpdateProductId()
  {
    return this.updateProductId;
  }
 
  public void setUpdateProductId(String updateProductId)
  {
    this.updateProductId = updateProductId;
  }
 
  public String getCustomerCif()
  {
    return this.customerCif;
  }
 
  public void setCustomerCif(String customerCif)
  {
    this.customerCif = customerCif;
  }
 
  public ArrayList<AlertMessagesVO> getErrorList()
  {
    return this.errorList;
  }
 
  public void setErrorList(ArrayList<AlertMessagesVO> errorList)
  {
    this.errorList = errorList;
  }
 
  public String getChargeType()
  {
    return this.chargeType;
  }
 
  public void setChargeType(String chargeType)
  {
    this.chargeType = chargeType;
  }
 
  public String getChargeId()
  {
    return this.chargeId;
  }
 
  public void setChargeId(String chargeId)
  {
    this.chargeId = chargeId;
  }
 
  public String getChargeKey97()
  {
    return this.chargeKey97;
  }
 
  public void setChargeKey97(String chargeKey97)
  {
    this.chargeKey97 = chargeKey97;
  }
 
  public String getProductType()
  {
    return this.productType;
  }
 
  public void setProductType(String productType)
  {
    this.productType = productType;
  }
 
  public String getProductId()
  {
    return this.productId;
  }
 
  public void setProductId(String productId)
  {
    this.productId = productId;
  }
 
  public String getProductKey97()
  {
    return this.productKey97;
  }
 
  public void setProductKey97(String productKey97)
  {
    this.productKey97 = productKey97;
  }
 
  public String getChargeDesc()
  {
    return this.chargeDesc;
  }
 
  public void setChargeDesc(String chargeDesc)
  {
    this.chargeDesc = chargeDesc;
  }
 
  public String getProductDesc()
  {
    return this.productDesc;
  }
 
  public void setProductDesc(String productDesc)
  {
    this.productDesc = productDesc;
  }
 
  public String getUpdateCusCif()
  {
    return this.updateCusCif;
  }
 
  public void setUpdateCusCif(String updateCusCif)
  {
    this.updateCusCif = updateCusCif;
  }
 
  public String getUpdateChargeId()
  {
    return this.updateChargeId;
  }
 
  public void setUpdateChargeId(String updateChargeId)
  {
    this.updateChargeId = updateChargeId;
  }
 
  public String getUpdateChargeKey97()
  {
    return this.updateChargeKey97;
  }
 
  public void setUpdateChargeKey97(String updateChargeKey97)
  {
    this.updateChargeKey97 = updateChargeKey97;
  }
 
  public ArrayList<ForwardContractVO> getPurchaseOrderList()
  {
    return this.purchaseOrderList;
  }
 
  public void setPurchaseOrderList(ArrayList<ForwardContractVO> purchaseOrderList)
  {
    this.purchaseOrderList = purchaseOrderList;
  }
 
  public String getInwardNo()
  {
    return this.inwardNo;
  }
 
  public void setInwardNo(String inwardNo)
  {
    this.inwardNo = inwardNo;
  }
 
  public String getGoodsDescription()
  {
    return this.goodsDescription;
  }
 
  public void setGoodsDescription(String goodsDescription)
  {
    this.goodsDescription = goodsDescription;
  }
 
  public String getCopyVal()
  {
    return this.copyVal;
  }
 
  public void setCopyVal(String copyVal)
  {
    this.copyVal = copyVal;
  }
 
  public String getCancellationamount()
  {
    return this.cancellationamount;
  }
 
  public void setCancellationamount(String cancellationamount)
  {
    this.cancellationamount = cancellationamount;
  }
 
  public String getBookingrate()
  {
    return this.bookingrate;
  }
 
  public void setBookingrate(String bookingrate)
  {
    this.bookingrate = bookingrate;
  }
 
  public String getTransid()
  {
    return this.transid;
  }
 
  public void setTransid(String transid)
  {
    this.transid = transid;
  }
 
  public String getTransdate()
  {
    return this.transdate;
  }
 
  public void setTransdate(String transdate)
  {
    this.transdate = transdate;
  }
 
  public String toString()
  {
    return
   




































      "ForwardContractVO [id=" + this.id + ", category=" + this.category + ", subProduct=" + this.subProduct + ", fwdContractNo=" + this.fwdContractNo + ", customerID=" + this.customerID + ", acctNumber=" + this.acctNumber + ", branchCode=" + this.branchCode + ", dealCurrency=" + this.dealCurrency + ", bookingDate=" + this.bookingDate + ", toCurrency=" + this.toCurrency + ", dealValidFromDate=" + this.dealValidFromDate + ", treasuryRefNo=" + this.treasuryRefNo + ", outstandingAmt=" + this.outstandingAmt + ", fwdContractAmt=" + this.fwdContractAmt + ", toCurrencyAmt=" + this.toCurrencyAmt + ", dealValidToDate=" + this.dealValidToDate + ", treasuryRate=" + this.treasuryRate + ", limitID=" + this.limitID + ", withoutLimit=" + this.withoutLimit + ", availableLimit=" + this.availableLimit + ", washRate=" + this.washRate + ", plAmount=" + this.plAmount + ", instructions=" + this.instructions + ", leiNumber=" + this.leiNumber + ", chargeAmount=" + this.chargeAmount + ", gstAmount=" + this.gstAmount + ", margin=" + this.margin + ", screenType=" + this.screenType + ", rateStatus=" + this.rateStatus + ", rateBuyOrSell=" + this.rateBuyOrSell + ", billId=" + this.billId + ", buyOrSell=" + this.buyOrSell + ", buyAmount=" + this.buyAmount + ", sellAmount=" + this.sellAmount + ", tranType=" + this.tranType + ", validFrom=" + this.validFrom + ", validTo=" + this.validTo + ", postingList=" + this.postingList + ", fwdContractDetailsList=" + this.fwdContractDetailsList + ", customerCif=" + this.customerCif + ", customerName=" + this.customerName + ", goodsDescription=" + this.goodsDescription + ", userTitle=" + this.userTitle + ", firstName=" + this.firstName + ", lastName=" + this.lastName + ", address=" + this.address + ", address2=" + this.address2 + ", address3=" + this.address3 + ", city=" + this.city + ", pincode=" + this.pincode + ", state=" + this.state + ", inwardNo=" + this.inwardNo + ", cityString=" + this.cityString + ", stateString=" + this.stateString + ", country=" + this.country + ", mobileNumber=" + this.mobileNumber + ", emailId=" + this.emailId + ", basicinforesult=" + this.basicinforesult + ", userid=" + this.userid + ", refNumber=" + this.refNumber + ", ngoId=" + this.ngoId + ", copyVal=" + this.copyVal + ", destinationCountry=" + this.destinationCountry + ", NCIFList=" + this.NCIFList + ", sessionUserName=" + this.sessionUserName + ", chargeType=" + this.chargeType + ", chargeId=" + this.chargeId + ", chargeDesc=" + this.chargeDesc + ", chargeKey97=" + this.chargeKey97 + ", productType=" + this.productType + ", productId=" + this.productId + ", productDesc=" + this.productDesc + ", productKey97=" + this.productKey97 + ", updateCusCif=" + this.updateCusCif + ", updateChargeId=" + this.updateChargeId + ", updateChargeKey97=" + this.updateChargeKey97 + ", updateProductId=" + this.updateProductId + ", exportOrderNumber=" + this.exportOrderNumber + ", exporterOrderDate=" + this.exporterOrderDate + ", incoTerms=" + this.incoTerms + ", goodsCode=" + this.goodsCode + ", poValue=" + this.poValue + ", exportcurrency=" + this.exportcurrency + ", exportexpiryDate=" + this.exportexpiryDate + ", lastShipmentDate=" + this.lastShipmentDate + ", freightDeduction=" + this.freightDeduction + ", insuranceDeduction=" + this.insuranceDeduction + ", marginPercentage=" + this.marginPercentage + ", eligibleAmount=" + this.eligibleAmount + ", description=" + this.description + ", count=" + this.count + ", importerName=" + this.importerName + ", poNo=" + this.poNo + ", poDate=" + this.poDate + ", poCif=" + this.poCif + ", poBen=" + this.poBen + ", poInco=" + this.poInco + ", poGoodDesc=" + this.poGoodDesc + ", poImpName=" + this.poImpName + ", poAmtValue=" + this.poAmtValue + ", poExpdate=" + this.poExpdate + ", poLastShipDate=" + this.poLastShipDate + ", poFrDeduct=" + this.poFrDeduct + ", poInsDeduct=" + this.poInsDeduct + ", poMargin=" + this.poMargin + ", poEligAmt=" + this.poEligAmt + ", purchaseOrderList=" + this.purchaseOrderList + ", fromDate=" + this.fromDate + ", toDate=" + this.toDate + ", status=" + this.status + ", statusList=" + this.statusList + ", remarks=" + this.remarks + ", errorList=" + this.errorList + ", cancellationamount=" + this.cancellationamount + ", bookingrate=" + this.bookingrate + ", transid=" + this.transid + ", transdate=" + this.transdate + "]";
  }
}
