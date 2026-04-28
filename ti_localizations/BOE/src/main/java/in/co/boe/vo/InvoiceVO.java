package in.co.boe.vo;

public class InvoiceVO
{
  private String invoiceSerialNumber;
  private String invoiceNumber;
  private String invoiceAmount;
  private String realizedAmount;
  private String subProdVal;
  public String getRealizedAmount()
  {
    return this.realizedAmount;
  }
  public void setRealizedAmount(String realizedAmount)
  {
    this.realizedAmount = realizedAmount;
  }
  public String getInvoiceSerialNumber()
  {
    return this.invoiceSerialNumber;
  }
  public void setInvoiceSerialNumber(String invoiceSerialNumber)
  {
    this.invoiceSerialNumber = invoiceSerialNumber;
  }
  public String getInvoiceNumber()
  {
    return this.invoiceNumber;
  }
  public void setInvoiceNumber(String invoiceNumber)
  {
    this.invoiceNumber = invoiceNumber;
  }
  public String getInvoiceAmount()
  {
    return this.invoiceAmount;
  }
  public void setInvoiceAmount(String invoiceAmount)
  {
    this.invoiceAmount = invoiceAmount;
  }
  public String getSubProdVal()
  {
    return this.subProdVal;
  }
  public void setSubProdVal(String subProdVal)
  {
    this.subProdVal = subProdVal;
  }
}