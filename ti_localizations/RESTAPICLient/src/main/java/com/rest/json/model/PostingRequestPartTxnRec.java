package com.rest.json.model;

public class PostingRequestPartTxnRec
{
  private PostingRequestAcctId AcctId;
  private String CreditDebitFlg;
  private PostingRequestTrnAmt TrnAmt;
  private String TrnParticulars;
  private String ValueDt;
  private PostingRequestPmtInst PmtInst;
  public PostingRequestAcctId getAcctId()
  {
    return this.AcctId;
  }
  public void setAcctId(PostingRequestAcctId acctId)
  {
    this.AcctId = acctId;
  }
  public String getCreditDebitFlg()
  {
    return this.CreditDebitFlg;
  }
  public void setCreditDebitFlg(String creditDebitFlg)
  {
    this.CreditDebitFlg = creditDebitFlg;
  }
  public PostingRequestTrnAmt getTrnAmt()
  {
    return this.TrnAmt;
  }
  public void setTrnAmt(PostingRequestTrnAmt trnAmt)
  {
    this.TrnAmt = trnAmt;
  }
  public String getTrnParticulars()
  {
    return this.TrnParticulars;
  }
  public void setTrnParticulars(String trnParticulars)
  {
    this.TrnParticulars = trnParticulars;
  }
  public String getValueDt()
  {
    return this.ValueDt;
  }
  public void setValueDt(String valueDt)
  {
    this.ValueDt = valueDt;
  }
  public PostingRequestPmtInst getPmtInst()
  {
    return this.PmtInst;
  }
  public void setPmtInst(PostingRequestPmtInst pmtInst)
  {
    this.PmtInst = pmtInst;
  }
}