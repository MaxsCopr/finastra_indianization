package com.bs.wiseconnect.migration.loader.tiplus.pojos;
 
import javax.persistence.Column;

import javax.persistence.Table;
 
@Table(name="ETTDM_BUYFIN_STAGING")

public class TFBuyFin

{

  @Column(name="PROGRAMME")

  private String programme;

  @Column(name="SELLER")

  private String seller;

  @Column(name="BUYER")

  private String buyer;

  @Column(name="MNEMONIC")

  private String mnemonic;

  @Column(name="SOURCEBANKINGBUSINESS")

  private String sourcebankingbuisness;

  @Column(name="RECEIVEDON")

  private String receivedOn;

  @Column(name="PRODUCTTYPE")

  private String productType;

  @Column(name="TICORRID")

  private String tiCorrID;

  @Column(name="BRANCH")

  private String branch;

  @Column(name="CUSTOMER")

  private String customer;

  @Column(name="PRODUCT")

  private String product;

  @Column(name="BEHALFOFBRANCH")

  private String behalfofbranch;

  @Column(name="DOCID")

  private String docID;

  @Column(name="DOCTYPE")

  private String docType;

  @Column(name="DESCRIPTION")

  private String description;

  @Column(name="RECEIVEDDATE")

  private String receivedDate;

  @Column(name="RECEIVEDTIME")

  private String receivedTime;

  @Column(name="DMSID")

  private String dmsID;

  @Column(name="MATURITYDATE")

  private String maturityDate;

  @Column(name="FINANCECCY")

  private String financeCurrency;

  @Column(name="FINANCEPERCENT")

  private String financePercent;

  @Column(name="FINANCEDATE")

  private String financeDate;

  @Column(name="FINANCEINST")

  private String financeInstructions;

  @Column(name="FINANCEOFFERONLY")

  private String financeOfferOnly;

  @Column(name="INVOICENUMBER")

  private String InvoiceNumber;

  @Column(name="ISSUEDATE")

  private String IssueDate;

  @Column(name="OUTSTANDINGAMT")

  private String OutstandingAmount;

  @Column(name="OUTSTANDINGAMTCCY")

  private String OutstandingAmountCurrency;

  @Column(name="STATUS")

  private String status;

  @Column(name="LOGID")

  private String logid;

  @Column(name="THEIRREF")

  private String theirRef;

  @Column(name="EBANKMASTERREF")

  private String eBankMasterRef;

  @Column(name="EBANKEVENTREF")

  private String eBankEventRef;

  @Column(name="MASTERREF")

  private String masterRef;

  public String getProgramme()

  {

    return this.programme;

  }

  public void setProgramme(String programme)

  {

    this.programme = programme;

  }

  public String getSeller()

  {

    return this.seller;

  }

  public void setSeller(String seller)

  {

    this.seller = seller;

  }

  public String getBuyer()

  {

    return this.buyer;

  }

  public void setBuyer(String buyer)

  {

    this.buyer = buyer;

  }

  public String getMnemonic()

  {

    return this.mnemonic;

  }

  public void setMnemonic(String mnemonic)

  {

    this.mnemonic = mnemonic;

  }

  public String getSourcebankingbuisness()

  {

    return this.sourcebankingbuisness;

  }

  public void setSourcebankingbuisness(String sourcebankingbuisness)

  {

    this.sourcebankingbuisness = sourcebankingbuisness;

  }

  public String getReceivedOn()

  {

    return this.receivedOn;

  }

  public void setReceivedOn(String receivedOn)

  {

    this.receivedOn = receivedOn;

  }

  public String getProductType()

  {

    return this.productType;

  }

  public void setProductType(String productType)

  {

    this.productType = productType;

  }

  public String getTiCorrID()

  {

    return this.tiCorrID;

  }

  public void setTiCorrID(String tiCorrID)

  {

    this.tiCorrID = tiCorrID;

  }

  public String getBranch()

  {

    return this.branch;

  }

  public void setBranch(String branch)

  {

    this.branch = branch;

  }

  public String getCustomer()

  {

    return this.customer;

  }

  public void setCustomer(String customer)

  {

    this.customer = customer;

  }

  public String getProduct()

  {

    return this.product;

  }

  public void setProduct(String product)

  {

    this.product = product;

  }

  public String getBehalfofbranch()

  {

    return this.behalfofbranch;

  }

  public void setBehalfofbranch(String behalfofbranch)

  {

    this.behalfofbranch = behalfofbranch;

  }

  public String getDocID()

  {

    return this.docID;

  }

  public void setDocID(String docID)

  {

    this.docID = docID;

  }

  public String getDocType()

  {

    return this.docType;

  }

  public void setDocType(String docType)

  {

    this.docType = docType;

  }

  public String getDescription()

  {

    return this.description;

  }

  public void setDescription(String description)

  {

    this.description = description;

  }

  public String getReceivedDate()

  {

    return this.receivedDate;

  }

  public void setReceivedDate(String receivedDate)

  {

    this.receivedDate = receivedDate;

  }

  public String getReceivedTime()

  {

    return this.receivedTime;

  }

  public void setReceivedTime(String receivedTime)

  {

    this.receivedTime = receivedTime;

  }

  public String getDmsID()

  {

    return this.dmsID;

  }

  public void setDmsID(String dmsID)

  {

    this.dmsID = dmsID;

  }

  public String getMaturityDate()

  {

    return this.maturityDate;

  }

  public void setMaturityDate(String maturityDate)

  {

    this.maturityDate = maturityDate;

  }

  public String getFinanceCurrency()

  {

    return this.financeCurrency;

  }

  public void setFinanceCurrency(String financeCurrency)

  {

    this.financeCurrency = financeCurrency;

  }

  public String getFinancePercent()

  {

    return this.financePercent;

  }

  public void setFinancePercent(String financePercent)

  {

    this.financePercent = financePercent;

  }

  public String getFinanceDate()

  {

    return this.financeDate;

  }

  public void setFinanceDate(String financeDate)

  {

    this.financeDate = financeDate;

  }

  public String getFinanceInstructions()

  {

    return this.financeInstructions;

  }

  public void setFinanceInstructions(String financeInstructions)

  {

    this.financeInstructions = financeInstructions;

  }

  public String getFinanceOfferOnly()

  {

    return this.financeOfferOnly;

  }

  public void setFinanceOfferOnly(String financeOfferOnly)

  {

    this.financeOfferOnly = financeOfferOnly;

  }

  public String getInvoiceNumber()

  {

    return this.InvoiceNumber;

  }

  public void setInvoiceNumber(String invoiceNumber)

  {

    this.InvoiceNumber = invoiceNumber;

  }

  public String getIssueDate()

  {

    return this.IssueDate;

  }

  public void setIssueDate(String issueDate)

  {

    this.IssueDate = issueDate;

  }

  public String getOutstandingAmount()

  {

    return this.OutstandingAmount;

  }

  public void setOutstandingAmount(String outstandingAmount)

  {

    this.OutstandingAmount = outstandingAmount;

  }

  public String getOutstandingAmountCurrency()

  {

    return this.OutstandingAmountCurrency;

  }

  public void setOutstandingAmountCurrency(String outstandingAmountCurrency)

  {

    this.OutstandingAmountCurrency = outstandingAmountCurrency;

  }

  public String getStatus()

  {

    return this.status;

  }

  public void setStatus(String status)

  {

    this.status = status;

  }

  public String getLogid()

  {

    return this.logid;

  }

  public void setLogid(String logid)

  {

    this.logid = logid;

  }

  public String getTheirRef()

  {

    return this.theirRef;

  }

  public void setTheirRef(String theirRef)

  {

    this.theirRef = theirRef;

  }

  public String geteBankMasterRef()

  {

    return this.eBankMasterRef;

  }

  public void seteBankMasterRef(String eBankMasterRef)

  {

    this.eBankMasterRef = eBankMasterRef;

  }

  public String geteBankEventRef()

  {

    return this.eBankEventRef;

  }

  public void seteBankEventRef(String eBankEventRef)

  {

    this.eBankEventRef = eBankEventRef;

  }

  public String getMasterRef()

  {

    return this.masterRef;

  }

  public void setMasterRef(String masterRef)

  {

    this.masterRef = masterRef;

  }

}