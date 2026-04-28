package in.co.forwardcontract.service.model;

public class PostingReqTrnAddRq
{
  private PostingReqTrnHdr XferTrnHdr;
  private PostingReqCount XferCount;
  private PostingReqTrnDetail XferTrnDetail;
  public PostingReqTrnHdr getXferTrnHdr()
  {
    return this.XferTrnHdr;
  }
  public void setXferTrnHdr(PostingReqTrnHdr xferTrnHdr)
  {
    this.XferTrnHdr = xferTrnHdr;
  }
  public PostingReqCount getXferCount()
  {
    return this.XferCount;
  }
  public void setXferCount(PostingReqCount xferCount)
  {
    this.XferCount = xferCount;
  }
  public PostingReqTrnDetail getXferTrnDetail()
  {
    return this.XferTrnDetail;
  }
  public void setXferTrnDetail(PostingReqTrnDetail xferTrnDetail)
  {
    this.XferTrnDetail = xferTrnDetail;
  }
}
