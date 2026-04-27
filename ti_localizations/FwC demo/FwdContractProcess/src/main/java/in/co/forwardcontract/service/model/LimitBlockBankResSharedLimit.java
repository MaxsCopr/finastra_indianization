package in.co.forwardcontract.service.model;

public class LimitBlockBankResSharedLimit
{
  private String cifId;
  private SerialNumber key;
  private String primaryCustomer;
  private LimitBlockBankResCategoryCode limitCategoryCode;
  private String activeFlg;
  public String getCifId()
  {
    return this.cifId;
  }
  public void setCifId(String cifId)
  {
    this.cifId = cifId;
  }
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