package com.in.fetchAccount.bean;
 
public class AccountAvailBalEncryptedRequest

{

  String reqdata;

  String msgid;

  public AccountAvailBalEncryptedRequest(String reqData, String msgId)

  {

    this.msgid = msgId;

    this.reqdata = reqData;

  }

}