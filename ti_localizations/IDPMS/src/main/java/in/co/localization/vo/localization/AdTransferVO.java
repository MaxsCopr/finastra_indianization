package in.co.localization.vo.localization;
 
import java.util.ArrayList;
 
public class AdTransferVO
{
  private String tshippingBillNo;
  private String tnoofShipingBill;
  private String tportCode;
  private String texportAgency;
  private String toldAdCode;
  private String tieCode;
  private String tshippingBillDate;
  private String tformNo;
  private String tnewAdCode;
  private String ttypeofExport;
  private String tempValue;
  private String shipId;
  ArrayList<AlertMessagesVO> errorList = null;
  public String getShipId()
  {
    return this.shipId;
  }
  public void setShipId(String shipId)
  {
    this.shipId = shipId;
  }
  public ArrayList<AlertMessagesVO> getErrorList()
  {
    return this.errorList;
  }
  public void setErrorList(ArrayList<AlertMessagesVO> errorList)
  {
    this.errorList = errorList;
  }
  public String getTempValue()
  {
    return this.tempValue;
  }
  public void setTempValue(String tempValue)
  {
    this.tempValue = tempValue;
  }
  public String getTshippingBillNo()
  {
    return this.tshippingBillNo;
  }
  public void setTshippingBillNo(String tshippingBillNo)
  {
    this.tshippingBillNo = tshippingBillNo;
  }
  public String getTnoofShipingBill()
  {
    return this.tnoofShipingBill;
  }
  public void setTnoofShipingBill(String tnoofShipingBill)
  {
    this.tnoofShipingBill = tnoofShipingBill;
  }
  public String getTportCode()
  {
    return this.tportCode;
  }
  public void setTportCode(String tportCode)
  {
    this.tportCode = tportCode;
  }
  public String getTexportAgency()
  {
    return this.texportAgency;
  }
  public void setTexportAgency(String texportAgency)
  {
    this.texportAgency = texportAgency;
  }
  public String getToldAdCode()
  {
    return this.toldAdCode;
  }
  public void setToldAdCode(String toldAdCode)
  {
    this.toldAdCode = toldAdCode;
  }
  public String getTieCode()
  {
    return this.tieCode;
  }
  public void setTieCode(String tieCode)
  {
    this.tieCode = tieCode;
  }
  public String getTshippingBillDate()
  {
    return this.tshippingBillDate;
  }
  public void setTshippingBillDate(String tshippingBillDate)
  {
    this.tshippingBillDate = tshippingBillDate;
  }
  public String getTformNo()
  {
    return this.tformNo;
  }
  public void setTformNo(String tformNo)
  {
    this.tformNo = tformNo;
  }
  public String getTnewAdCode()
  {
    return this.tnewAdCode;
  }
  public void setTnewAdCode(String tnewAdCode)
  {
    this.tnewAdCode = tnewAdCode;
  }
  public String getTtypeofExport()
  {
    return this.ttypeofExport;
  }
  public void setTtypeofExport(String ttypeofExport)
  {
    this.ttypeofExport = ttypeofExport;
  }
}