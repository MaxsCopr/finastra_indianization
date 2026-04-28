package com.rest.json.model;

public class AvailBalBankReq
{
  String reqdata;
  String msgid;
  public AvailBalBankReq(String reqData, String msgId)
  {
    this.msgid = msgId;
    this.reqdata = reqData;
  }
}
