package com.rest.json.model;

public class PostingRequest
{
  private PostingRequestAddRequest XferTrnAddRequest;
  private String msgid;
  public PostingRequestAddRequest getXferTrnAddRequest()
  {
    return this.XferTrnAddRequest;
  }
  public void setXferTrnAddRequest(PostingRequestAddRequest xferTrnAddRequest)
  {
    this.XferTrnAddRequest = xferTrnAddRequest;
  }
  public String getMsgid()
  {
    return this.msgid;
  }
  public void setMsgid(String msgid)
  {
    this.msgid = msgid;
  }
}
