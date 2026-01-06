package com.rest.json.model;

import java.util.List;

public class PostingRequestDetail
{
  private List<PostingRequestPartTxnRec> PartTrnRec;
  public List<PostingRequestPartTxnRec> getPartTrnRec()
  {
    return this.PartTrnRec;
  }
  public void setPartTrnRec(List<PostingRequestPartTxnRec> partTrnRec)
  {
    this.PartTrnRec = partTrnRec;
  }
}
