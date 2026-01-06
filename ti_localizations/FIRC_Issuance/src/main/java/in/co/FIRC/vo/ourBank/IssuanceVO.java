package in.co.FIRC.vo.ourBank;

public class IssuanceVO
{
  private String fircno;
  private String transrefno;
  private String status;
  private String dataFrom;
  private String dataTo;
  private String cifNo;
  private String ifscCode;
  private String swiftBIC;
  private String CRN;
  private String checked;
  private String cifID;
  private String beneficiaryName;
  public String getCifID()
  {
    return this.cifID;
  }
  public void setCifID(String cifID)
  {
    this.cifID = cifID;
  }
  public String getBeneficiaryName()
  {
    return this.beneficiaryName;
  }
  public void setBeneficiaryName(String beneficiaryName)
  {
    this.beneficiaryName = beneficiaryName;
  }
  public String getChecked()
  {
    return this.checked;
  }
  public void setChecked(String checked)
  {
    this.checked = checked;
  }
  public String getCRN()
  {
    return this.CRN;
  }
  public void setCRN(String cRN)
  {
    this.CRN = cRN;
  }
  public String getIfscCode()
  {
    return this.ifscCode;
  }
  public void setIfscCode(String ifscCode)
  {
    this.ifscCode = ifscCode;
  }
  public String getSwiftBIC()
  {
    return this.swiftBIC;
  }
  public void setSwiftBIC(String swiftBIC)
  {
    this.swiftBIC = swiftBIC;
  }
  public String getCifNo()
  {
    return this.cifNo;
  }
  public void setCifNo(String cifNo)
  {
    this.cifNo = cifNo;
  }
  public String getDataFrom()
  {
    return this.dataFrom;
  }
  public void setDataFrom(String dataFrom)
  {
    this.dataFrom = dataFrom;
  }
  public String getDataTo()
  {
    return this.dataTo;
  }
  public void setDataTo(String dataTo)
  {
    this.dataTo = dataTo;
  }
  public String getStatus()
  {
    return this.status;
  }
  public void setStatus(String status)
  {
    this.status = status;
  }
  public String getFircno()
  {
    return this.fircno;
  }
  public void setFircno(String fircno)
  {
    this.fircno = fircno;
  }
  public String getTransrefno()
  {
    return this.transrefno;
  }
  public void setTransrefno(String transrefno)
  {
    this.transrefno = transrefno;
  }
}
