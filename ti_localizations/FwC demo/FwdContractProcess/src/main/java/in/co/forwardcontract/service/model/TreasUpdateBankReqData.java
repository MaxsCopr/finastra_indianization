package in.co.forwardcontract.service.model;

public class TreasUpdateBankReqData
{
  private String option;
  private String utilizationAmount;
  private String unUtilizedAmount;
  private String refAmount;
  private String treRefNo;
  public String getOption()
  {
    return this.option;
  }
  public void setOption(String option)
  {
    this.option = option;
  }
  public String getUtilizationAmount()
  {
    return this.utilizationAmount;
  }
  public void setUtilizationAmount(String utilizationAmount)
  {
    this.utilizationAmount = utilizationAmount;
  }
  public String getUnUtilizedAmount()
  {
    return this.unUtilizedAmount;
  }
  public void setUnUtilizedAmount(String unUtilizedAmount)
  {
    this.unUtilizedAmount = unUtilizedAmount;
  }
  public String getRefAmount()
  {
    return this.refAmount;
  }
  public void setRefAmount(String refAmount)
  {
    this.refAmount = refAmount;
  }
  public String getTreRefNo()
  {
    return this.treRefNo;
  }
  public void setTreRefNo(String treRefNo)
  {
    this.treRefNo = treRefNo;
  }
}