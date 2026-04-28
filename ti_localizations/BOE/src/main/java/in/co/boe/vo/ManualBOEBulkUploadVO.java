package in.co.boe.vo;

import java.io.File;
import java.util.ArrayList;
 
public class ManualBOEBulkUploadVO
{
  private String boeNo;
  private String boeDate;
  private String portCode;
  private String govprv;
  private String ieCode;
  private String iename;
  private String ieadd;
  private String iepan;
  private String imAgency;
  private String adCode;
  private String pos;
  private String igmNo;
  private String igmDate;
  private String hblNo;
  private String hblDate;
  private String mblNo;
  private String mblDate;
  private String cifNo;
  private String invoiceSerialNo;
  private String invoiceNo;
  private String invoiceAmt;
  private String invoiceCurrency;
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
  private ArrayList<ManualBOEBulkUploadVO> manualBoevoList;
  private File inputFile;
  private String overridStatus;
  private String recInd;
  private String errorDesc;
  public ArrayList<ManualBOEBulkUploadVO> getManualBoevoList()
  {
    return this.manualBoevoList;
  }
  public void setManualBoevoList(ArrayList<ManualBOEBulkUploadVO> manualBoevoList)
  {
    this.manualBoevoList = manualBoevoList;
  }
  public String getErrorDesc()
  {
    return this.errorDesc;
  }
  public void setErrorDesc(String errorDesc)
  {
    this.errorDesc = errorDesc;
  }
  public String getRecInd()
  {
    return this.recInd;
  }
  public void setRecInd(String recInd)
  {
    this.recInd = recInd;
  }
  public String getOverridStatus()
  {
    return this.overridStatus;
  }
  public void setOverridStatus(String overridStatus)
  {
    this.overridStatus = overridStatus;
  }
  public String getCifNo()
  {
    return this.cifNo;
  }
  public void setCifNo(String cifNo)
  {
    this.cifNo = cifNo;
  }
  public String getBoeNo()
  {
    return this.boeNo;
  }
  public void setBoeNo(String boeNo)
  {
    this.boeNo = boeNo;
  }
  public String getBoeDate()
  {
    return this.boeDate;
  }
  public void setBoeDate(String boeDate)
  {
    this.boeDate = boeDate;
  }
  public String getPortCode()
  {
    return this.portCode;
  }
  public void setPortCode(String portCode)
  {
    this.portCode = portCode;
  }
  public String getGovprv()
  {
    return this.govprv;
  }
  public void setGovprv(String govprv)
  {
    this.govprv = govprv;
  }
  public String getIeCode()
  {
    return this.ieCode;
  }
  public void setIeCode(String ieCode)
  {
    this.ieCode = ieCode;
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
  public File getInputFile()
  {
    return this.inputFile;
  }
  public void setInputFile(File inputFile)
  {
    this.inputFile = inputFile;
  }
  public String getTermsofInvoice()
  {
    return this.termsofInvoice;
  }
  public void setTermsofInvoice(String termsofInvoice)
  {
    this.termsofInvoice = termsofInvoice;
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
  public String getInvoiceSerialNo()
  {
    return this.invoiceSerialNo;
  }
  public void setInvoiceSerialNo(String invoiceSerialNo)
  {
    this.invoiceSerialNo = invoiceSerialNo;
  }
  public String getInvoiceNo()
  {
    return this.invoiceNo;
  }
  public void setInvoiceNo(String invoiceNo)
  {
    this.invoiceNo = invoiceNo;
  }
  public String getInvoiceAmt()
  {
    return this.invoiceAmt;
  }
  public void setInvoiceAmt(String invoiceAmt)
  {
    this.invoiceAmt = invoiceAmt;
  }
  public String getInvoiceCurrency()
  {
    return this.invoiceCurrency;
  }
  public void setInvoiceCurrency(String invoiceCurrency)
  {
    this.invoiceCurrency = invoiceCurrency;
  }
}
