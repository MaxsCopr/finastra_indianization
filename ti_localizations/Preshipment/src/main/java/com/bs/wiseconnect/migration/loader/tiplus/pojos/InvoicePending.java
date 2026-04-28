package com.bs.wiseconnect.migration.loader.tiplus.pojos;
 
import javax.persistence.Column;

import javax.persistence.Table;
 
@Table(name="ETT_INVOICE_MATCHING")

public class InvoicePending

{

  @Column(name="PROGRAM_ID")

  private String prgId;

  @Column(name="DEBIT_PARTY")

  private String debitParty;

  @Column(name="VALUE_DATE")

  private String valDate;

  @Column(name="PAYMENT_AMOUNT")

  private String payAmt;

  @Column(name="MAS_REF")

  private String masterRef;

  @Column(name="INV_NUMBER")

  private String invNumber;

  @Column(name="DISBURSE_DATE")

  private String disburseDate;

  @Column(name="DUE_DATE")

  private String dueDate;

  @Column(name="LOAN_AMOUNT")

  private String loanAmt;

  @Column(name="OUT_AMOUNT")

  private String outAmt;

  @Column(name="REPAY_AMOUNT")

  private String repayAmt;

  @Column(name="BATCHID")

  private String batchId;

  @Column(name="LOGID")

  private String logId;

  @Column(name="INPUTBRN")

  private String inputBrn;

  @Column(name="BEHALFBRN")

  private String behalfBrn;

  public String getPrgId()

  {

    return this.prgId;

  }

  public String getDebitParty()

  {

    return this.debitParty;

  }

  public String getValDate()

  {

    return this.valDate;

  }

  public String getPayAmt()

  {

    return this.payAmt;

  }

  public String getMasterRef()

  {

    return this.masterRef;

  }

  public String getInvNumber()

  {

    return this.invNumber;

  }

  public String getDisburseDate()

  {

    return this.disburseDate;

  }

  public String getDueDate()

  {

    return this.dueDate;

  }

  public String getLoanAmt()

  {

    return this.loanAmt;

  }

  public String getOutAmt()

  {

    return this.outAmt;

  }

  public String getRepayAmt()

  {

    return this.repayAmt;

  }

  public String getBatchId()

  {

    return this.batchId;

  }

  public String getLogId()

  {

    return this.logId;

  }

  public String getInputBrn()

  {

    return this.inputBrn;

  }

  public String getBehalfBrn()

  {

    return this.behalfBrn;

  }

  public void setPrgId(String prgId)

  {

    this.prgId = prgId;

  }

  public void setDebitParty(String debitParty)

  {

    this.debitParty = debitParty;

  }

  public void setValDate(String valDate)

  {

    this.valDate = valDate;

  }

  public void setPayAmt(String payAmt)

  {

    this.payAmt = payAmt;

  }

  public void setMasterRef(String masterRef)

  {

    this.masterRef = masterRef;

  }

  public void setInvNumber(String invNumber)

  {

    this.invNumber = invNumber;

  }

  public void setDisburseDate(String disburseDate)

  {

    this.disburseDate = disburseDate;

  }

  public void setDueDate(String dueDate)

  {

    this.dueDate = dueDate;

  }

  public void setLoanAmt(String loanAmt)

  {

    this.loanAmt = loanAmt;

  }

  public void setOutAmt(String outAmt)

  {

    this.outAmt = outAmt;

  }

  public void setRepayAmt(String repayAmt)

  {

    this.repayAmt = repayAmt;

  }

  public void setBatchId(String batchId)

  {

    this.batchId = batchId;

  }

  public void setLogId(String logId)

  {

    this.logId = logId;

  }

  public void setInputBrn(String inputBrn)

  {

    this.inputBrn = inputBrn;

  }

  public void setBehalfBrn(String behalfBrn)

  {

    this.behalfBrn = behalfBrn;

  }

}