package com.rest.json.model;

public class LimitFetchRequestData
{
  private String custCifId;
  public LimitFetchRequestData(String custCifId)
  {
    this.custCifId = custCifId;
  }
  public String getCustCifId()
  {
    return this.custCifId;
  }
  public void setCustCifId(String custCifId)
  {
    this.custCifId = custCifId;
  }
}
