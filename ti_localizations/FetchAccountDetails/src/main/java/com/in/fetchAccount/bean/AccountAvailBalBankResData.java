package com.in.fetchAccount.bean;
 
public class AccountAvailBalBankResData

{

  private AccountAvailBalBankResAmt amount;

  private String ledgerBalance;

  private String bankTxnId;

  private String currency;

  private String transactionId;

  private String responseCode;

  private String status;

  public AccountAvailBalBankResAmt getAmount()

  {

    return this.amount;

  }

  public void setAmount(AccountAvailBalBankResAmt amount)

  {

    this.amount = amount;

  }

  public String getLedgerBalance()

  {

    return this.ledgerBalance;

  }

  public void setLedgerBalance(String ledgerBalance)

  {

    this.ledgerBalance = ledgerBalance;

  }

  public String getBankTxnId()

  {

    return this.bankTxnId;

  }

  public void setBankTxnId(String bankTxnId)

  {

    this.bankTxnId = bankTxnId;

  }

  public String getCurrency()

  {

    return this.currency;

  }

  public void setCurrency(String currency)

  {

    this.currency = currency;

  }

  public String getTransactionId()

  {

    return this.transactionId;

  }

  public void setTransactionId(String transactionId)

  {

    this.transactionId = transactionId;

  }

  public String getResponseCode()

  {

    return this.responseCode;

  }

  public void setResponseCode(String responseCode)

  {

    this.responseCode = responseCode;

  }

  public String getStatus()

  {

    return this.status;

  }

  public void setStatus(String status)

  {

    this.status = status;

  }

}