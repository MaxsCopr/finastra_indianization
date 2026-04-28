package in.co.forwardcontract.service.model;

public class PostingReqTrnAddRequest
{
  private PostingReqTrnAddRq XferTrnAddRq;
  private PostingReqCustomData XferTrnAdd_CustomData;
  public PostingReqTrnAddRq getXferTrnAddRq()
  {
    return this.XferTrnAddRq;
  }
  public void setXferTrnAddRq(PostingReqTrnAddRq xferTrnAddRq)
  {
    this.XferTrnAddRq = xferTrnAddRq;
  }
  public PostingReqCustomData getXferTrnAdd_CustomData()
  {
    return this.XferTrnAdd_CustomData;
  }
  public void setXferTrnAdd_CustomData(PostingReqCustomData xferTrnAdd_CustomData)
  {
    this.XferTrnAdd_CustomData = xferTrnAdd_CustomData;
  }
}