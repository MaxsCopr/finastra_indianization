package in.co.localization.vo.localization;
 
import java.util.ArrayList;
 
public class AcknowledgementListVO

{

  ArrayList<AcknowledgementVO> prnShippingList;

  ArrayList<AcknowledgementVO> prnInvoiceList;

  ArrayList<AcknowledgementVO> wsnShippingList;

  ArrayList<AcknowledgementVO> wsnInvoiceList;

  ArrayList<ShippingDetailsVO> shippingList;

  ArrayList<InvoiceDetailsVO> invoiceList;

  private String shippingBillNo;

  private String shippingBillDate;

  private String portCode;

  private String formNo;

  private String billRefNo;

  private String eventRefNo;

  private String custName;

  private String ieCode;

  private String custNo;

  private String custCIFNo;

  private String bankname;

  private String customerCIFNo;

  private String customerNo;

  private String customerName;

  private String blocked;

  private String country;

  public String getBankname()

  {

    return this.bankname;

  }

  public void setBankname(String bankname)

  {

    this.bankname = bankname;

  }

  public String getCustomerCIFNo()

  {

    return this.customerCIFNo;

  }

  public void setCustomerCIFNo(String customerCIFNo)

  {

    this.customerCIFNo = customerCIFNo;

  }

  public String getCustomerNo()

  {

    return this.customerNo;

  }

  public void setCustomerNo(String customerNo)

  {

    this.customerNo = customerNo;

  }

  public String getCustomerName()

  {

    return this.customerName;

  }

  public void setCustomerName(String customerName)

  {

    this.customerName = customerName;

  }

  public String getBlocked()

  {

    return this.blocked;

  }

  public void setBlocked(String blocked)

  {

    this.blocked = blocked;

  }

  public String getCountry()

  {

    return this.country;

  }

  public void setCountry(String country)

  {

    this.country = country;

  }

  public String getCustCIFNo()

  {

    return this.custCIFNo;

  }

  public void setCustCIFNo(String custCIFNo)

  {

    this.custCIFNo = custCIFNo;

  }

  public String getCustNo()

  {

    return this.custNo;

  }

  public void setCustNo(String custNo)

  {

    this.custNo = custNo;

  }

  public String getBillRefNo()

  {

    return this.billRefNo;

  }

  public void setBillRefNo(String billRefNo)

  {

    this.billRefNo = billRefNo;

  }

  public String getEventRefNo()

  {

    return this.eventRefNo;

  }

  public void setEventRefNo(String eventRefNo)

  {

    this.eventRefNo = eventRefNo;

  }

  public String getCustName()

  {

    return this.custName;

  }

  public void setCustName(String custName)

  {

    this.custName = custName;

  }

  public String getIeCode()

  {

    return this.ieCode;

  }

  public void setIeCode(String ieCode)

  {

    this.ieCode = ieCode;

  }

  public String getShippingBillNo()

  {

    return this.shippingBillNo;

  }

  public void setShippingBillNo(String shippingBillNo)

  {

    this.shippingBillNo = shippingBillNo;

  }

  public String getShippingBillDate()

  {

    return this.shippingBillDate;

  }

  public void setShippingBillDate(String shippingBillDate)

  {

    this.shippingBillDate = shippingBillDate;

  }

  public String getPortCode()

  {

    return this.portCode;

  }

  public void setPortCode(String portCode)

  {

    this.portCode = portCode;

  }

  public String getFormNo()

  {

    return this.formNo;

  }

  public void setFormNo(String formNo)

  {

    this.formNo = formNo;

  }

  public ArrayList<AcknowledgementVO> getPrnShippingList()

  {

    return this.prnShippingList;

  }

  public void setPrnShippingList(ArrayList<AcknowledgementVO> prnShippingList)

  {

    this.prnShippingList = prnShippingList;

  }

  public ArrayList<AcknowledgementVO> getPrnInvoiceList()

  {

    return this.prnInvoiceList;

  }

  public void setPrnInvoiceList(ArrayList<AcknowledgementVO> prnInvoiceList)

  {

    this.prnInvoiceList = prnInvoiceList;

  }

  public ArrayList<AcknowledgementVO> getWsnShippingList()

  {

    return this.wsnShippingList;

  }

  public void setWsnShippingList(ArrayList<AcknowledgementVO> wsnShippingList)

  {

    this.wsnShippingList = wsnShippingList;

  }

  public ArrayList<AcknowledgementVO> getWsnInvoiceList()

  {

    return this.wsnInvoiceList;

  }

  public void setWsnInvoiceList(ArrayList<AcknowledgementVO> wsnInvoiceList)

  {

    this.wsnInvoiceList = wsnInvoiceList;

  }

  public ArrayList<ShippingDetailsVO> getShippingList()

  {

    return this.shippingList;

  }

  public void setShippingList(ArrayList<ShippingDetailsVO> shippingList)

  {

    this.shippingList = shippingList;

  }

  public ArrayList<InvoiceDetailsVO> getInvoiceList()

  {

    return this.invoiceList;

  }

  public void setInvoiceList(ArrayList<InvoiceDetailsVO> invoiceList)

  {

    this.invoiceList = invoiceList;

  }

}