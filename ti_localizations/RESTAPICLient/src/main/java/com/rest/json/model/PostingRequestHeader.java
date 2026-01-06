package com.rest.json.model;

public class PostingRequestHeader
{
  private String TrnType;
  private String TrnSubType;
  public String getTrnType()
  {
    return this.TrnType;
  }
  public void setTrnType(String trnType)
  {
    this.TrnType = trnType;
  }
  public String getTrnSubType()
  {
    return this.TrnSubType;
  }
  public void setTrnSubType(String trnSubType)
  {
    this.TrnSubType = trnSubType;
  }
}
