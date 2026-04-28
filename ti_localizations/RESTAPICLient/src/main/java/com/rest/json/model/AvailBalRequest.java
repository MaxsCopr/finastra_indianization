package com.rest.json.model;

public class AvailBalRequest
{
  private String requestType;
  private String msgid;
  private AvailBalRequestData data;
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
  public AvailBalRequestData getData()
  {
    return this.data;
  }
  public void setData(AvailBalRequestData data)
  {
    this.data = data;
  }
}
