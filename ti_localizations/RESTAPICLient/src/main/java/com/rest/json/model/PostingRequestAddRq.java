package com.rest.json.model;

public class PostingRequestAddRq
{
  private PostingRequestHeader XferTrnHdr;
  private PostingRequestCount XferCount;
  private PostingRequestDetail XferTrnDetail;
  public PostingRequestHeader getXferTrnHdr()
  {
    return this.XferTrnHdr;
  }
  public void setXferTrnHdr(PostingRequestHeader xferTrnHdr)
  {
    this.XferTrnHdr = xferTrnHdr;
  }
  public PostingRequestCount getXferCount()
  {
    return this.XferCount;
  }
  public void setXferCount(PostingRequestCount xferCount)
  {
    this.XferCount = xferCount;
  }
  public PostingRequestDetail getXferTrnDetail()
  {
    return this.XferTrnDetail;
  }
  public void setXferTrnDetail(PostingRequestDetail xferTrnDetail)
  {
    this.XferTrnDetail = xferTrnDetail;
  }
}
