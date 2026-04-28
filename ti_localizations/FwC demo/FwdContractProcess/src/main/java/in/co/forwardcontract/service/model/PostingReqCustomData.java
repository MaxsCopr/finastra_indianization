package in.co.forwardcontract.service.model;

import java.util.List;

public class PostingReqCustomData
{
  private String solid;
  private List<PostingReqTranPart> TRANPART;
  public String getSolid()
  {
    return this.solid;
  }
  public void setSolid(String solid)
  {
    this.solid = solid;
  }
  public List<PostingReqTranPart> getTRANPART()
  {
    return this.TRANPART;
  }
  public void setTRANPART(List<PostingReqTranPart> tRANPART)
  {
    this.TRANPART = tRANPART;
  }
}