package in.co.stp.vo;

import java.util.ArrayList;

public class StatusHomeVO
{
  private String beneficiaryAccountNo;
  private String ifscCode;
  private String accountOfficer;
  private String documentRelease;
  private String productType;
  private String receviedFromRef;
  private String receviedOn;
  private String collectionAmount;
  private String financeRequested;
  private String receviedFrom;
  private String sendTo;
  private String drawee;
  private String tenorPeriod;
  private String baseDate;
  private String hasAttachedDoc;
  private String documentCode;
  private String firstMail;
  private String secondMail;
  private String total;
  private String noInvoice;
  private String invoiceSerialNo;
  private String invoiceAmtCcy;
  private String discountedAmtCcy;
  private String deductionAmtCcy;
  private String financeRefNo;
  private String productType2;
  private String period;
  private String interestAdvance;
  private String interestArrears;
  private String baseRate;
  private String spreadRate;
  private String batchId;
  private String odcUpDate;
  private String invoiceDate;
  private String status;
  ArrayList<ExcelDataVO> invoiceDetails;
  ArrayList<Object> transactionList;
  ArrayList<ExcelDataVO> invoiceAjaxListval;
  public String getStatus()
  {
    return this.status;
  }
  public void setStatus(String status)
  {
    this.status = status;
  }
  public String getInvoiceDate()
  {
    return this.invoiceDate;
  }
  public void setInvoiceDate(String invoiceDate)
  {
    this.invoiceDate = invoiceDate;
  }
  public ArrayList<ExcelDataVO> getInvoiceAjaxListval()
  {
    return this.invoiceAjaxListval;
  }
  public void setInvoiceAjaxListval(ArrayList<ExcelDataVO> invoiceAjaxListval)
  {
    this.invoiceAjaxListval = invoiceAjaxListval;
  }
  public ArrayList<Object> getTransactionList()
  {
    return this.transactionList;
  }
  public void setTransactionList(ArrayList<Object> transactionList)
  {
    this.transactionList = transactionList;
  }
  public ArrayList<ExcelDataVO> getInvoiceDetails()
  {
    return this.invoiceDetails;
  }
  public void setInvoiceDetails(ArrayList<ExcelDataVO> invoiceDetails)
  {
    this.invoiceDetails = invoiceDetails;
  }
  public String getDocumentRelease()
  {
    return this.documentRelease;
  }
  public void setDocumentRelease(String documentRelease)
  {
    this.documentRelease = documentRelease;
  }
  public String getProductType()
  {
    return this.productType;
  }
  public void setProductType(String productType)
  {
    this.productType = productType;
  }
  public String getReceviedFromRef()
  {
    return this.receviedFromRef;
  }
  public void setReceviedFromRef(String receviedFromRef)
  {
    this.receviedFromRef = receviedFromRef;
  }
  public String getReceviedOn()
  {
    return this.receviedOn;
  }
  public void setReceviedOn(String receviedOn)
  {
    this.receviedOn = receviedOn;
  }
  public String getCollectionAmount()
  {
    return this.collectionAmount;
  }
  public void setCollectionAmount(String collectionAmount)
  {
    this.collectionAmount = collectionAmount;
  }
  public String getFinanceRequested()
  {
    return this.financeRequested;
  }
  public void setFinanceRequested(String financeRequested)
  {
    this.financeRequested = financeRequested;
  }
  public String getReceviedFrom()
  {
    return this.receviedFrom;
  }
  public void setReceviedFrom(String receviedFrom)
  {
    this.receviedFrom = receviedFrom;
  }
  public String getSendTo()
  {
    return this.sendTo;
  }
  public void setSendTo(String sendTo)
  {
    this.sendTo = sendTo;
  }
  public String getDrawee()
  {
    return this.drawee;
  }
  public void setDrawee(String drawee)
  {
    this.drawee = drawee;
  }
  public String getTenorPeriod()
  {
    return this.tenorPeriod;
  }
  public void setTenorPeriod(String tenorPeriod)
  {
    this.tenorPeriod = tenorPeriod;
  }
  public String getBaseDate()
  {
    return this.baseDate;
  }
  public void setBaseDate(String baseDate)
  {
    this.baseDate = baseDate;
  }
  public String getHasAttachedDoc()
  {
    return this.hasAttachedDoc;
  }
  public void setHasAttachedDoc(String hasAttachedDoc)
  {
    this.hasAttachedDoc = hasAttachedDoc;
  }
  public String getDocumentCode()
  {
    return this.documentCode;
  }
  public void setDocumentCode(String documentCode)
  {
    this.documentCode = documentCode;
  }
  public String getFirstMail()
  {
    return this.firstMail;
  }
  public void setFirstMail(String firstMail)
  {
    this.firstMail = firstMail;
  }
  public String getSecondMail()
  {
    return this.secondMail;
  }
  public void setSecondMail(String secondMail)
  {
    this.secondMail = secondMail;
  }
  public String getTotal()
  {
    return this.total;
  }
  public void setTotal(String total)
  {
    this.total = total;
  }
  public String getNoInvoice()
  {
    return this.noInvoice;
  }
  public void setNoInvoice(String noInvoice)
  {
    this.noInvoice = noInvoice;
  }
  public String getInvoiceSerialNo()
  {
    return this.invoiceSerialNo;
  }
  public void setInvoiceSerialNo(String invoiceSerialNo)
  {
    this.invoiceSerialNo = invoiceSerialNo;
  }
  public String getInvoiceAmtCcy()
  {
    return this.invoiceAmtCcy;
  }
  public void setInvoiceAmtCcy(String invoiceAmtCcy)
  {
    this.invoiceAmtCcy = invoiceAmtCcy;
  }
  public String getDiscountedAmtCcy()
  {
    return this.discountedAmtCcy;
  }
  public void setDiscountedAmtCcy(String discountedAmtCcy)
  {
    this.discountedAmtCcy = discountedAmtCcy;
  }
  public String getDeductionAmtCcy()
  {
    return this.deductionAmtCcy;
  }
  public void setDeductionAmtCcy(String deductionAmtCcy)
  {
    this.deductionAmtCcy = deductionAmtCcy;
  }
  public String getFinanceRefNo()
  {
    return this.financeRefNo;
  }
  public void setFinanceRefNo(String financeRefNo)
  {
    this.financeRefNo = financeRefNo;
  }
  public String getProductType2()
  {
    return this.productType2;
  }
  public void setProductType2(String productType2)
  {
    this.productType2 = productType2;
  }
  public String getPeriod()
  {
    return this.period;
  }
  public void setPeriod(String period)
  {
    this.period = period;
  }
  public String getInterestAdvance()
  {
    return this.interestAdvance;
  }
  public void setInterestAdvance(String interestAdvance)
  {
    this.interestAdvance = interestAdvance;
  }
  public String getInterestArrears()
  {
    return this.interestArrears;
  }
  public void setInterestArrears(String interestArrears)
  {
    this.interestArrears = interestArrears;
  }
  public String getBaseRate()
  {
    return this.baseRate;
  }
  public void setBaseRate(String baseRate)
  {
    this.baseRate = baseRate;
  }
  public String getSpreadRate()
  {
    return this.spreadRate;
  }
  public void setSpreadRate(String spreadRate)
  {
    this.spreadRate = spreadRate;
  }
  public String getBatchId()
  {
    return this.batchId;
  }
  public void setBatchId(String batchId)
  {
    this.batchId = batchId;
  }
  public String getOdcUpDate()
  {
    return this.odcUpDate;
  }
  public void setOdcUpDate(String odcUpDate)
  {
    this.odcUpDate = odcUpDate;
  }
  public String getAccountOfficer()
  {
    return this.accountOfficer;
  }
  public void setAccountOfficer(String accountOfficer)
  {
    this.accountOfficer = accountOfficer;
  }
  public String getIfscCode()
  {
    return this.ifscCode;
  }
  public void setIfscCode(String ifscCode)
  {
    this.ifscCode = ifscCode;
  }
  public String getBeneficiaryAccountNo()
  {
    return this.beneficiaryAccountNo;
  }
  public void setBeneficiaryAccountNo(String beneficiaryAccountNo)
  {
    this.beneficiaryAccountNo = beneficiaryAccountNo;
  }
}