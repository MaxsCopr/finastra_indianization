package com.rest.json.model;

public class LimitFetchRequest
{
  private String requestType;
  private String msgid;
  private LimitFetchRequestData data;
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
  public LimitFetchRequestData getData()
  {
    return this.data;
  }
  public void setData(LimitFetchRequestData data)
  {
    this.data = data;
  }
}
