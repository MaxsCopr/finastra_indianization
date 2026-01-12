package in.co.localization.vo.localization;
 
public class EodDownloadVO
{
  private String eodDate;
  private String eodFileType;
  private String eodFileName;
  private String generatedVia;
  private String billRefNo;
  private String shipBillNo;
  private String shipBillDate;
  private String iecode;
  public String getShipBillNo()
  {
    return this.shipBillNo;
  }
  public void setShipBillNo(String shipBillNo)
  {
    this.shipBillNo = shipBillNo;
  }
  public String getShipBillDate()
  {
    return this.shipBillDate;
  }
  public void setShipBillDate(String shipBillDate)
  {
    this.shipBillDate = shipBillDate;
  }
  public String getIecode()
  {
    return this.iecode;
  }
  public void setIecode(String iecode)
  {
    this.iecode = iecode;
  }
  public String getBillRefNo()
  {
    return this.billRefNo;
  }
  public void setBillRefNo(String billRefNo)
  {
    this.billRefNo = billRefNo;
  }
  public String getGeneratedVia()
  {
    return this.generatedVia;
  }
  public void setGeneratedVia(String generatedVia)
  {
    this.generatedVia = generatedVia;
  }
  public String getEodDate()
  {
    return this.eodDate;
  }
  public void setEodDate(String eodDate)
  {
    this.eodDate = eodDate;
  }
  public String getEodFileType()
  {
    return this.eodFileType;
  }
  public void setEodFileType(String eodFileType)
  {
    this.eodFileType = eodFileType;
  }
  public String getEodFileName()
  {
    return this.eodFileName;
  }
  public void setEodFileName(String eodFileName)
  {
    this.eodFileName = eodFileName;
  }
}