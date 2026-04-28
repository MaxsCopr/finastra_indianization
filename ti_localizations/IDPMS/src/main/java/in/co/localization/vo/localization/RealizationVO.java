package in.co.localization.vo.localization;
 
import java.util.ArrayList;
 
public class RealizationVO
{
  String key;
  String value;
  String billRefNo;
  private String billCurr;
  private String custCIFNo;
  private String ieCodec;
  private String selectedEventRifNo;
  private String billAmt;
  private String custName;
  private String adCodec;
  private String transacrefNo;
  private String transacSerialno;
  private String transacDate;
  private String transacCurrency;
  private String transacAmount;
  private String tempValue;
  private String exchangeRange;
  private String shippingBillDate;
  private String formNo;
  private String shippingBillNo;
  private String portCode;
  private String ex_rate;
  private int status;
  String errorMsg;
  ArrayList<RealizationVO> eventList = null;
  ArrayList<RealizationVO> transactionList = null;
  ArrayList<ShippingDetailsVO> transShippingList = null;
  ArrayList<InvoiceDetailsVO> invoiceList = null;
  ArrayList<AlertMessagesVO> errorList = null;
  public String getErrorMsg()
  {
    return this.errorMsg;
  }
  public void setErrorMsg(String errorMsg)
  {
    this.errorMsg = errorMsg;
  }
  public int getStatus()
  {
    return this.status;
  }
  public void setStatus(int status)
  {
    this.status = status;
  }
  public String getEx_rate()
  {
    return this.ex_rate;
  }
  public void setEx_rate(String ex_rate)
  {
    this.ex_rate = ex_rate;
  }
  public ArrayList<AlertMessagesVO> getErrorList()
  {
    return this.errorList;
  }
  public void setErrorList(ArrayList<AlertMessagesVO> errorList)
  {
    this.errorList = errorList;
  }
  public String getBillRefNo()
  {
    return this.billRefNo;
  }
  public void setBillRefNo(String billRefNo)
  {
    this.billRefNo = billRefNo;
  }
  public String getBillCurr()
  {
    return this.billCurr;
  }
  public void setBillCurr(String billCurr)
  {
    this.billCurr = billCurr;
  }
  public String getCustCIFNo()
  {
    return this.custCIFNo;
  }
  public void setCustCIFNo(String custCIFNo)
  {
    this.custCIFNo = custCIFNo;
  }
  public String getIeCodec()
  {
    return this.ieCodec;
  }
  public void setIeCodec(String ieCodec)
  {
    this.ieCodec = ieCodec;
  }
  public String getSelectedEventRifNo()
  {
    return this.selectedEventRifNo;
  }
  public void setSelectedEventRifNo(String selectedEventRifNo)
  {
    this.selectedEventRifNo = selectedEventRifNo;
  }
  public String getBillAmt()
  {
    return this.billAmt;
  }
  public void setBillAmt(String billAmt)
  {
    this.billAmt = billAmt;
  }
  public String getCustName()
  {
    return this.custName;
  }
  public void setCustName(String custName)
  {
    this.custName = custName;
  }
  public String getAdCodec()
  {
    return this.adCodec;
  }
  public void setAdCodec(String adCodec)
  {
    this.adCodec = adCodec;
  }
  public String getKey()
  {
    return this.key;
  }
  public void setKey(String key)
  {
    this.key = key;
  }
  public String getValue()
  {
    return this.value;
  }
  public void setValue(String value)
  {
    this.value = value;
  }
  public ArrayList<RealizationVO> getEventList()
  {
    return this.eventList;
  }
  public void setEventList(ArrayList<RealizationVO> eventList)
  {
    this.eventList = eventList;
  }
  public ArrayList<RealizationVO> getTransactionList()
  {
    return this.transactionList;
  }
  public void setTransactionList(ArrayList<RealizationVO> transactionList)
  {
    this.transactionList = transactionList;
  }
  public String getTransacrefNo()
  {
    return this.transacrefNo;
  }
  public void setTransacrefNo(String transacrefNo)
  {
    this.transacrefNo = transacrefNo;
  }
  public String getTransacSerialno()
  {
    return this.transacSerialno;
  }
  public void setTransacSerialno(String transacSerialno)
  {
    this.transacSerialno = transacSerialno;
  }
  public String getTransacDate()
  {
    return this.transacDate;
  }
  public void setTransacDate(String transacDate)
  {
    this.transacDate = transacDate;
  }
  public String getTransacCurrency()
  {
    return this.transacCurrency;
  }
  public void setTransacCurrency(String transacCurrency)
  {
    this.transacCurrency = transacCurrency;
  }
  public String getTransacAmount()
  {
    return this.transacAmount;
  }
  public void setTransacAmount(String transacAmount)
  {
    this.transacAmount = transacAmount;
  }
  public ArrayList<ShippingDetailsVO> getTransShippingList()
  {
    return this.transShippingList;
  }
  public void setTransShippingList(ArrayList<ShippingDetailsVO> transShippingList)
  {
    this.transShippingList = transShippingList;
  }
  public ArrayList<InvoiceDetailsVO> getInvoiceList()
  {
    return this.invoiceList;
  }
  public void setInvoiceList(ArrayList<InvoiceDetailsVO> invoiceList)
  {
    this.invoiceList = invoiceList;
  }
  public String getShippingBillDate()
  {
    return this.shippingBillDate;
  }
  public void setShippingBillDate(String shippingBillDate)
  {
    this.shippingBillDate = shippingBillDate;
  }
  public String getFormNo()
  {
    return this.formNo;
  }
  public void setFormNo(String formNo)
  {
    this.formNo = formNo;
  }
  public String getShippingBillNo()
  {
    return this.shippingBillNo;
  }
  public void setShippingBillNo(String shippingBillNo)
  {
    this.shippingBillNo = shippingBillNo;
  }
  public String getPortCode()
  {
    return this.portCode;
  }
  public void setPortCode(String portCode)
  {
    this.portCode = portCode;
  }
  public String getTempValue()
  {
    return this.tempValue;
  }
  public void setTempValue(String tempValue)
  {
    this.tempValue = tempValue;
  }
  public String getExchangeRange()
  {
    return this.exchangeRange;
  }
  public void setExchangeRange(String exchangeRange)
  {
    this.exchangeRange = exchangeRange;
  }
}