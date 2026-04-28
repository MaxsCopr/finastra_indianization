package in.co.forwardcontract.service.model;

public class LimitFetchBankResCustLimitDtls
{
  private String expiryDate;
  private String crncyCode;
  private String limitSuffix;
  private String limitAmt;
  private String limitPrefix;
  private String sanctionDate;
  private String limitDesc;
  private String TotalLiability;
  public String getExpiryDate()
  {
    return this.expiryDate;
  }
  public void setExpiryDate(String expiryDate)
  {
    this.expiryDate = expiryDate;
  }
  public String getCrncyCode()
  {
    return this.crncyCode;
  }
  public void setCrncyCode(String crncyCode)
  {
    this.crncyCode = crncyCode;
  }
  public String getLimitSuffix()
  {
    return this.limitSuffix;
  }
  public void setLimitSuffix(String limitSuffix)
  {
    this.limitSuffix = limitSuffix;
  }
  public String getLimitAmt()
  {
    return this.limitAmt;
  }
  public void setLimitAmt(String limitAmt)
  {
    this.limitAmt = limitAmt;
  }
  public String getLimitPrefix()
  {
    return this.limitPrefix;
  }
  public void setLimitPrefix(String limitPrefix)
  {
    this.limitPrefix = limitPrefix;
  }
  public String getSanctionDate()
  {
    return this.sanctionDate;
  }
  public void setSanctionDate(String sanctionDate)
  {
    this.sanctionDate = sanctionDate;
  }
  public String getLimitDesc()
  {
    return this.limitDesc;
  }
  public void setLimitDesc(String limitDesc)
  {
    this.limitDesc = limitDesc;
  }
  public String getTotalLiability()
  {
    return this.TotalLiability;
  }
  public void setTotalLiability(String totalLiability)
  {
    this.TotalLiability = totalLiability;
  }
}