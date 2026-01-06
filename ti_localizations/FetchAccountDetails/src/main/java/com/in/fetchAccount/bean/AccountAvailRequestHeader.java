package com.in.fetchAccount.bean;
 
public class AccountAvailRequestHeader

{

  private String requestType;

  private String msgid;

  private AccountAvailRequestData data;

  public String getRequestType()

  {

    return this.requestType;

  }

  public void setRequestType(String requestType)

  {

    this.requestType = requestType;

  }

  public String getMsgid()

  {

    return this.msgid;

  }

  public void setMsgid(String msgid)

  {

    this.msgid = msgid;

  }

  public AccountAvailRequestData getData()

  {

    return this.data;

  }

  public void setData(AccountAvailRequestData data)

  {

    this.data = data;

  }

}