package in.co.forwardcontract.service.model;

public class LimitFetchBankResHeaderDtls
{
  private String fundedLimit;
  private String nonFundedLimit;
  public String getFundedLimit()
  {
    return this.fundedLimit;
  }
  public void setFundedLimit(String fundedLimit)
  {
    this.fundedLimit = fundedLimit;
  }
  public String getNonFundedLimit()
  {
    return this.nonFundedLimit;
  }
  public void setNonFundedLimit(String nonFundedLimit)
  {
    this.nonFundedLimit = nonFundedLimit;
  }
}
