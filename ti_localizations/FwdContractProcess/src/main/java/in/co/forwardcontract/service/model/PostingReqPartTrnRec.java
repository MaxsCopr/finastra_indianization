package in.co.forwardcontract.service.model;

public class PostingReqPartTrnRec
{
  private PostingReqAcc AcctId;
  private String CreditDebitFlg;
  private AmtAndCcy TrnAmt;
  private String TrnParticulars;
  private String ValueDt;
  private PostingReqPmtInst PmtInst;
  private String SerialNum;
  public PostingReqAcc getAcctId()
  {
    return this.AcctId;
  }
  public void setAcctId(PostingReqAcc acctId)
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
  public AmtAndCcy getTrnAmt()
  {
    return this.TrnAmt;
  }
  public void setTrnAmt(AmtAndCcy trnAmt)
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
  public PostingReqPmtInst getPmtInst()
  {
    return this.PmtInst;
  }
  public void setPmtInst(PostingReqPmtInst pmtInst)
  {
    this.PmtInst = pmtInst;
  }
  public String getSerialNum()
  {
    return this.SerialNum;
  }
  public void setSerialNum(String serialNum)
  {
    this.SerialNum = serialNum;
  }
}
