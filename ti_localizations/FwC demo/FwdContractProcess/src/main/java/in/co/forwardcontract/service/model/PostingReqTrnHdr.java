package in.co.forwardcontract.service.model;

public class PostingReqTrnHdr
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
