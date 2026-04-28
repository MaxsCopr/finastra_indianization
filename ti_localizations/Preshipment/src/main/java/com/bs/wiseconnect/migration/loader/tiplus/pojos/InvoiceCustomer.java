package com.bs.wiseconnect.migration.loader.tiplus.pojos;
 
import javax.persistence.Basic;

import javax.persistence.Column;

import javax.persistence.Id;

import javax.persistence.Table;
 
@Table(name="SupplyChainInvoice")

public class InvoiceCustomer

{

  @Column(name="PROGRAMME")

  private String programme;

  @Column(name="SELLER")

  private String seller;

  @Column(name="BUYER")

  private String buyer;

  @Column(name="BATCHID")

  private String batchid;

  @Column(name="INVOICEREF")

  private String invoiceref;

  @Column(name="FACEAMOUNT")

  private String faceamount;

  @Column(name="FACECCY")

  private String faceccy;

  @Column(name="MNEMONIC")

  private String mnemonic;

  @Column(name="INVOICENUMBER")

  private String invoicenumber;

  @Column(name="ISSUEDDATE")

  private String issueddate;

  @Column(name="SETTELEMENTDATE")

  private String settelementdate;

  @Column(name="SOURCEBANKINGBUISNESS")

  private String sourcebankingbuisness;

  @Column(name="THEIRREF")

  private String theirref;

  @Column(name="CUSTOMER")

  private String customer;

  @Column(name="BRANCH")

  private String branch;

  @Column(name="BEHALFOFBRANCH")

  private String behalfofbranch;

  @Column(name="FLAGVAL")

  private String flag;

  private String financeCurrency;

  private String OutstandingAmount;

  private String OutstandingAmountCurrency;

  @Id

  @Basic(optional=false)

  @Column(name="LOGID")

  private String logid;

  @Column(name="STATUS")

  private String status;

  @Column(name="ERRORDTLS")

  private String errorDtls;

  @Column(name="TICORRID")

  private String ticorrid;

  public String getTheirref()

  {

    return this.theirref;

  }

  public void setTheirref(String theirref)

  {

    this.theirref = theirref;

  }

  public String getCustomer()

  {

    return this.customer;

  }

  public void setCustomer(String customer)

  {

    this.customer = customer;

  }

  public String getBranch()

  {

    return this.branch;

  }

  public void setBranch(String branch)

  {

    this.branch = branch;

  }

  public String getBehalfofbranch()

  {

    return this.behalfofbranch;

  }

  public void setBehalfofbranch(String behalfofbranch)

  {

    this.behalfofbranch = behalfofbranch;

  }

  public String getFlag()

  {

    return this.flag;

  }

  public void setFlag(String flag)

  {

    this.flag = flag;

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

  public String getInvoicenumber()

  {

    return this.invoicenumber;

  }

  public void setInvoicenumber(String invoicenumber)

  {

    this.invoicenumber = invoicenumber;

  }

  public String getProgramme()

  {

    return this.programme;

  }

  public String getIssueddate()

  {

    return this.issueddate;

  }

  public void setIssueddate(String issueddate)

  {

    this.issueddate = issueddate;

  }

  public String getSettelementdate()

  {

    return this.settelementdate;

  }

  public void setSettelementdate(String settelementdate)

  {

    this.settelementdate = settelementdate;

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

  public String getBatchid()

  {

    return this.batchid;

  }

  public void setBatchid(String batchid)

  {

    this.batchid = batchid;

  }

  public String getInvoiceref()

  {

    return this.invoiceref;

  }

  public void setInvoiceref(String invoiceref)

  {

    this.invoiceref = invoiceref;

  }

  public String getFaceamount()

  {

    return this.faceamount;

  }

  public void setFaceamount(String faceamount)

  {

    this.faceamount = faceamount;

  }

  public String getFaceccy()

  {

    return this.faceccy;

  }

  public void setFaceccy(String faceccy)

  {

    this.faceccy = faceccy;

  }

  public String getLogid()

  {

    return this.logid;

  }

  public void setLogid(String logid)

  {

    this.logid = logid;

  }

  public String getStatus()

  {

    return this.status;

  }

  public void setStatus(String status)

  {

    this.status = status;

  }

  public String getErrorDtls()

  {

    return this.errorDtls;

  }

  public void setErrorDtls(String errorDtls)

  {

    this.errorDtls = errorDtls;

  }

  public String getTicorrid()

  {

    return this.ticorrid;

  }

  public void setTicorrid(String ticorrid)

  {

    this.ticorrid = ticorrid;

  }

  public String getFinanceCurrency()

  {

    return this.financeCurrency;

  }

  public void setFinanceCurrency(String financeCurrency)

  {

    this.financeCurrency = financeCurrency;

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

}