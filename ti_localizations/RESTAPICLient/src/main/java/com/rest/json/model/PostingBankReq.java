package com.rest.json.model;

public class PostingBankReq
{
  String reqdata;
  String msgid;
  public PostingBankReq(String reqData, String msgId)
  {
    this.msgid = msgId;
    this.reqdata = reqData;
  }
}
