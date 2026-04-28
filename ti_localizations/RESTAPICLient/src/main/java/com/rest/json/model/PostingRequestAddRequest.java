package com.rest.json.model;

public class PostingRequestAddRequest
{
  private PostingRequestAddRq XferTrnAddRq;
  private PostingRequestCustomData XferTrnAdd_CustomData;
  public PostingRequestAddRq getXferTrnAddRq()
  {
    return this.XferTrnAddRq;
  }
  public void setXferTrnAddRq(PostingRequestAddRq xferTrnAddRq)
  {
    this.XferTrnAddRq = xferTrnAddRq;
  }
  public PostingRequestCustomData getXferTrnAdd_CustomData()
  {
    return this.XferTrnAdd_CustomData;
  }
  public void setXferTrnAdd_CustomData(PostingRequestCustomData xferTrnAdd_CustomData)
  {
    this.XferTrnAdd_CustomData = xferTrnAdd_CustomData;
  }
}
