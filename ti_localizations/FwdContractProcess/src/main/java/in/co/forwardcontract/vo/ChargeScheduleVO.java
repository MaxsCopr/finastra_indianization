package in.co.forwardcontract.vo;

import java.util.ArrayList;

public class ChargeScheduleVO
{
  private String customerCif;
  private String customerName;
  private String goodsDescription;
 
  public String getCustomerName()
  {
    return this.customerName;
  }
 
  public void setCustomerName(String customerName)
  {
    this.customerName = customerName;
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
  private String cifID;
  private String beneficiaryName;
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
  private ArrayList<ChargeScheduleVO> purchaseOrderList;
  String fromDate;
  String toDate;
  private String status;
  String statusList;
  String remark;
  ArrayList<AlertMessagesVO> errorList;
 
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
 
  public String getRemark()
  {
    return this.remark;
  }
 
  public void setRemark(String remark)
  {
    this.remark = remark;
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
 
  public String getCifID()
  {
    return this.cifID;
  }
 
  public void setCifID(String cifID)
  {
    this.cifID = cifID;
  }
 
  public String getBeneficiaryName()
  {
    return this.beneficiaryName;
  }
 
  public void setBeneficiaryName(String beneficiaryName)
  {
    this.beneficiaryName = beneficiaryName;
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
 
  public ArrayList<ChargeScheduleVO> getPurchaseOrderList()
  {
    return this.purchaseOrderList;
  }
 
  public void setPurchaseOrderList(ArrayList<ChargeScheduleVO> purchaseOrderList)
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
}
