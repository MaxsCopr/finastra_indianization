package in.co.forwardcontract.service.model;

public class LimitBlockBankResSharedLimit2
{
  private SerialNumber key;
  private String primaryCustomer;
  private LimitBlockBankResCategoryCode limitCategoryCode;
  private String activeFlg;
  public SerialNumber getKey()
  {
    return this.key;
  }
  public void setKey(SerialNumber key)
  {
    this.key = key;
  }
  public String getPrimaryCustomer()
  {
    return this.primaryCustomer;
  }
  public void setPrimaryCustomer(String primaryCustomer)
  {
    this.primaryCustomer = primaryCustomer;
  }
  public LimitBlockBankResCategoryCode getLimitCategoryCode()
  {
    return this.limitCategoryCode;
  }
  public void setLimitCategoryCode(LimitBlockBankResCategoryCode limitCategoryCode)
  {
    this.limitCategoryCode = limitCategoryCode;
  }
  public String getActiveFlg()
  {
    return this.activeFlg;
  }
  public void setActiveFlg(String activeFlg)
  {
    this.activeFlg = activeFlg;
  }
}
