package com.rest.json.model;

public class LimitFetchBankReq
{
  String reqdata;
  String msgid;
  public LimitFetchBankReq(String reqData, String msgId)
  {
    this.msgid = msgId;
    this.reqdata = reqData;
  }
}
