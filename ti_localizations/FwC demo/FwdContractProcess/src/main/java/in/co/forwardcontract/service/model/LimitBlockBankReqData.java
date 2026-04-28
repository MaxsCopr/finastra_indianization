package in.co.forwardcontract.service.model;

public class LimitBlockBankReqData
{
  private String limitPrefix;
  private String limitSuffix;
  private String amountValue;
  private String currencyCode;
  private String refId;
  private String serialno;
  private String remarks;
  private String starDt;
  private String endDt;
  public String getLimitPrefix()
  {
    return this.limitPrefix;
  }
  public void setLimitPrefix(String limitPrefix)
  {
    this.limitPrefix = limitPrefix;
  }
  public String getLimitSuffix()
  {
    return this.limitSuffix;
  }
  public void setLimitSuffix(String limitSuffix)
  {
    this.limitSuffix = limitSuffix;
  }
  public String getAmountValue()
  {
    return this.amountValue;
  }
  public void setAmountValue(String amountValue)
  {
    this.amountValue = amountValue;
  }
  public String getCurrencyCode()
  {
    return this.currencyCode;
  }
  public void setCurrencyCode(String currencyCode)
  {
    this.currencyCode = currencyCode;
  }
  public String getRefId()
  {
    return this.refId;
  }
  public void setRefId(String refId)
  {
    this.refId = refId;
  }
  public String getRemarks()
  {
    return this.remarks;
  }
  public void setRemarks(String remarks)
  {
    this.remarks = remarks;
  }
  public String getStarDt()
  {
    return this.starDt;
  }
  public void setStarDt(String starDt)
  {
    this.starDt = starDt;
  }
  public String getEndDt()
  {
    return this.endDt;
  }
  public void setEndDt(String endDt)
  {
    this.endDt = endDt;
  }
  public String getSerialno()
  {
    return this.serialno;
  }
  public void setSerialno(String serialno)
  {
    this.serialno = serialno;
  }
}
