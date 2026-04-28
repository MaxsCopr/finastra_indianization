package com.in.fetchAccount.bean;
 
public class AccountAvailRequestData

{

  private String type;

  private String accountNumber;

  private String senderCode;

  public String getType()

  {

    return this.type;

  }

  public void setType(String type)

  {

    this.type = type;

  }

  public String getAccountNumber()

  {

    return this.accountNumber;

  }

  public void setAccountNumber(String accountNumber)

  {

    this.accountNumber = accountNumber;

  }

  public String getSenderCode()

  {

    return this.senderCode;

  }

  public void setSenderCode(String senderCode)

  {

    this.senderCode = senderCode;

  }

}