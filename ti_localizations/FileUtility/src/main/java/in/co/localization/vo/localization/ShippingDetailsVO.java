package in.co.localization.vo.localization;

import java.util.ArrayList;

public class ShippingDetailsVO
{
  private String billRefNo;
  private String billCurr;
  private String custCIFNo;
  private String selectedEnqMod;
  private String billAmt;
  private String custName;
  private String ieCode;
  private String adCode;
  private String ieCodec;
  private String adCodec;
  private String noOfShippingBill;
  private String shippingBillNo;
  private String portCode;
  private String selectedExportAgency;
  private String tiIeCode;
  private String tiAdCode;
  private String countryOfDest;
  private String leoDate;
  private String adBillNo;
  private String buyerName;
  private String shippingBillDate;
  private String formNo;
  private String selectedExportType;
  private String custSerialNo;
  private String recInd;
  private String dateOfNeg;
  private String directDispInd;
  private String buyerCountry;
  private String noOfInvoices;
  private String id;
  private String value;
  private int tiShippingIndex;
  private String exportAgency;
  private String exportType;
  private String remarks;
  private String storeType;
  private String transacCurrency;
  private String transacAmount;
  private String tempValue;
  private String exchangeRange;
  private String writeAmt;
  private String writeDate;
  private String tempTransAmt;
  private String shippingBillAmt;
  private String shippingBillCurr;
  private String tempRealAmt;
  private String realizationAmt;
  private String realizationCurr;
  private String currentRealAmt;
  private String currentRealCurr;
  private String selectedEventRifNo;
  private String ex_rate;
  String transBillDate;
  String RStatus;
  int status;
  private String rodSent;
  private String penSent;
  private String prnSent;
  private String grType;
  private String tempShippingBillNo;
  private String tempShippingBillDate;
  private String tempPortCode;
  private String transEventRefNo;
  private String alreadyTransEventRefNo;
  private int fileNo;
  private String paySerialNo;
  private String totalRealAmt;
  ArrayList<ShippingDetailsVO> shippingList = null;
  ArrayList<InvoiceDetailsVO> invoiceList = null;
  ArrayList<AlertMessagesVO> errorList = null;
  ArrayList<ShippingDetailsVO> shippingBillList = null;
 
  public String getPaySerialNo()
  {
    return this.paySerialNo;
  }
 
  public void setPaySerialNo(String paySerialNo)
  {
    this.paySerialNo = paySerialNo;
  }
 
  public String getTotalRealAmt()
  {
    return this.totalRealAmt;
  }
 
  public void setTotalRealAmt(String totalRealAmt)
  {
    this.totalRealAmt = totalRealAmt;
  }
 
  public int getFileNo()
  {
    return this.fileNo;
  }
 
  public void setFileNo(int fileNo)
  {
    this.fileNo = fileNo;
  }
 
  public String getTransEventRefNo()
  {
    return this.transEventRefNo;
  }
 
  public void setTransEventRefNo(String transEventRefNo)
  {
    this.transEventRefNo = transEventRefNo;
  }
 
  public String getAlreadyTransEventRefNo()
  {
    return this.alreadyTransEventRefNo;
  }
 
  public void setAlreadyTransEventRefNo(String alreadyTransEventRefNo)
  {
    this.alreadyTransEventRefNo = alreadyTransEventRefNo;
  }
 
  public int getStatus()
  {
    return this.status;
  }
 
  public void setStatus(int status)
  {
    this.status = status;
  }
 
  public ArrayList<ShippingDetailsVO> getShippingBillList()
  {
    return this.shippingBillList;
  }
 
  public void setShippingBillList(ArrayList<ShippingDetailsVO> shippingBillList)
  {
    this.shippingBillList = shippingBillList;
  }
 
  public ArrayList<InvoiceDetailsVO> getInvoiceList()
  {
    return this.invoiceList;
  }
 
  public void setInvoiceList(ArrayList<InvoiceDetailsVO> invoiceList)
  {
    this.invoiceList = invoiceList;
  }
 
  public String getTempShippingBillNo()
  {
    return this.tempShippingBillNo;
  }
 
  public void setTempShippingBillNo(String tempShippingBillNo)
  {
    this.tempShippingBillNo = tempShippingBillNo;
  }
 
  public String getTempShippingBillDate()
  {
    return this.tempShippingBillDate;
  }
 
  public void setTempShippingBillDate(String tempShippingBillDate)
  {
    this.tempShippingBillDate = tempShippingBillDate;
  }
 
  public String getTempPortCode()
  {
    return this.tempPortCode;
  }
 
  public void setTempPortCode(String tempPortCode)
  {
    this.tempPortCode = tempPortCode;
  }
 
  public String getRodSent()
  {
    return this.rodSent;
  }
 
  public void setRodSent(String rodSent)
  {
    this.rodSent = rodSent;
  }
 
  public String getPenSent()
  {
    return this.penSent;
  }
 
  public void setPenSent(String penSent)
  {
    this.penSent = penSent;
  }
 
  public String getPrnSent()
  {
    return this.prnSent;
  }
 
  public void setPrnSent(String prnSent)
  {
    this.prnSent = prnSent;
  }
 
  public String getGrType()
  {
    return this.grType;
  }
 
  public void setGrType(String grType)
  {
    this.grType = grType;
  }
 
  public String getEx_rate()
  {
    return this.ex_rate;
  }
 
  public void setEx_rate(String ex_rate)
  {
    this.ex_rate = ex_rate;
  }
 
  public String getRealizationAmt()
  {
    return this.realizationAmt;
  }
 
  public void setRealizationAmt(String realizationAmt)
  {
    this.realizationAmt = realizationAmt;
  }
 
  public String getRealizationCurr()
  {
    return this.realizationCurr;
  }
 
  public void setRealizationCurr(String realizationCurr)
  {
    this.realizationCurr = realizationCurr;
  }
 
  public String getCurrentRealAmt()
  {
    return this.currentRealAmt;
  }
 
  public void setCurrentRealAmt(String currentRealAmt)
  {
    this.currentRealAmt = currentRealAmt;
  }
 
  public String getCurrentRealCurr()
  {
    return this.currentRealCurr;
  }
 
  public void setCurrentRealCurr(String currentRealCurr)
  {
    this.currentRealCurr = currentRealCurr;
  }
 
  public ArrayList<AlertMessagesVO> getErrorList()
  {
    return this.errorList;
  }
 
  public void setErrorList(ArrayList<AlertMessagesVO> errorList)
  {
    this.errorList = errorList;
  }
 
  public String getTempRealAmt()
  {
    return this.tempRealAmt;
  }
 
  public void setTempRealAmt(String tempRealAmt)
  {
    this.tempRealAmt = tempRealAmt;
  }
 
  public String getRStatus()
  {
    return this.RStatus;
  }
 
  public void setRStatus(String rStatus)
  {
    this.RStatus = rStatus;
  }
 
  public String getTransBillDate()
  {
    return this.transBillDate;
  }
 
  public void setTransBillDate(String transBillDate)
  {
    this.transBillDate = transBillDate;
  }
 
  public String getShippingBillAmt()
  {
    return this.shippingBillAmt;
  }
 
  public void setShippingBillAmt(String shippingBillAmt)
  {
    this.shippingBillAmt = shippingBillAmt;
  }
 
  public String getShippingBillCurr()
  {
    return this.shippingBillCurr;
  }
 
  public void setShippingBillCurr(String shippingBillCurr)
  {
    this.shippingBillCurr = shippingBillCurr;
  }
 
  public String getTempTransAmt()
  {
    return this.tempTransAmt;
  }
 
  public void setTempTransAmt(String tempTransAmt)
  {
    this.tempTransAmt = tempTransAmt;
  }
 
  public ArrayList<ShippingDetailsVO> getShippingList()
  {
    return this.shippingList;
  }
 
  public void setShippingList(ArrayList<ShippingDetailsVO> shippingList)
  {
    this.shippingList = shippingList;
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
 
  public String getStoreType()
  {
    return this.storeType;
  }
 
  public void setStoreType(String storeType)
  {
    this.storeType = storeType;
  }
 
  public String getRemarks()
  {
    return this.remarks;
  }
 
  public void setRemarks(String remarks)
  {
    this.remarks = remarks;
  }
 
  public String getExportAgency()
  {
    return this.exportAgency;
  }
 
  public void setExportAgency(String exportAgency)
  {
    this.exportAgency = exportAgency;
  }
 
  public String getExportType()
  {
    return this.exportType;
  }
 
  public void setExportType(String exportType)
  {
    this.exportType = exportType;
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
 
  public String getIeCode()
  {
    return this.ieCode;
  }
 
  public void setIeCode(String ieCode)
  {
    this.ieCode = ieCode;
  }
 
  public String getSelectedEnqMod()
  {
    return this.selectedEnqMod;
  }
 
  public void setSelectedEnqMod(String selectedEnqMod)
  {
    this.selectedEnqMod = selectedEnqMod;
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
 
  public String getAdCode()
  {
    return this.adCode;
  }
 
  public void setAdCode(String adCode)
  {
    this.adCode = adCode;
  }
 
  public String getIeCodec()
  {
    return this.ieCodec;
  }
 
  public void setIeCodec(String ieCodec)
  {
    this.ieCodec = ieCodec;
  }
 
  public String getAdCodec()
  {
    return this.adCodec;
  }
 
  public void setAdCodec(String adCodec)
  {
    this.adCodec = adCodec;
  }
 
  public String getNoOfShippingBill()
  {
    return this.noOfShippingBill;
  }
 
  public void setNoOfShippingBill(String noOfShippingBill)
  {
    this.noOfShippingBill = noOfShippingBill;
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
 
  public String getSelectedExportAgency()
  {
    return this.selectedExportAgency;
  }
 
  public void setSelectedExportAgency(String selectedExportAgency)
  {
    this.selectedExportAgency = selectedExportAgency;
  }
 
  public String getTiIeCode()
  {
    return this.tiIeCode;
  }
 
  public void setTiIeCode(String tiIeCode)
  {
    this.tiIeCode = tiIeCode;
  }
 
  public String getTiAdCode()
  {
    return this.tiAdCode;
  }
 
  public void setTiAdCode(String tiAdCode)
  {
    this.tiAdCode = tiAdCode;
  }
 
  public String getCountryOfDest()
  {
    return this.countryOfDest;
  }
 
  public void setCountryOfDest(String countryOfDest)
  {
    this.countryOfDest = countryOfDest;
  }
 
  public String getLeoDate()
  {
    return this.leoDate;
  }
 
  public void setLeoDate(String leoDate)
  {
    this.leoDate = leoDate;
  }
 
  public String getAdBillNo()
  {
    return this.adBillNo;
  }
 
  public void setAdBillNo(String adBillNo)
  {
    this.adBillNo = adBillNo;
  }
 
  public String getBuyerName()
  {
    return this.buyerName;
  }
 
  public void setBuyerName(String buyerName)
  {
    this.buyerName = buyerName;
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
 
  public String getSelectedExportType()
  {
    return this.selectedExportType;
  }
 
  public void setSelectedExportType(String selectedExportType)
  {
    this.selectedExportType = selectedExportType;
  }
 
  public String getCustSerialNo()
  {
    return this.custSerialNo;
  }
 
  public void setCustSerialNo(String custSerialNo)
  {
    this.custSerialNo = custSerialNo;
  }
 
  public String getRecInd()
  {
    return this.recInd;
  }
 
  public void setRecInd(String recInd)
  {
    this.recInd = recInd;
  }
 
  public String getDateOfNeg()
  {
    return this.dateOfNeg;
  }
 
  public void setDateOfNeg(String dateOfNeg)
  {
    this.dateOfNeg = dateOfNeg;
  }
 
  public String getDirectDispInd()
  {
    return this.directDispInd;
  }
 
  public void setDirectDispInd(String directDispInd)
  {
    this.directDispInd = directDispInd;
  }
 
  public String getBuyerCountry()
  {
    return this.buyerCountry;
  }
 
  public void setBuyerCountry(String buyerCountry)
  {
    this.buyerCountry = buyerCountry;
  }
 
  public String getNoOfInvoices()
  {
    return this.noOfInvoices;
  }
 
  public void setNoOfInvoices(String noOfInvoices)
  {
    this.noOfInvoices = noOfInvoices;
  }
 
  public String getId()
  {
    return this.id;
  }
 
  public void setId(String id)
  {
    this.id = id;
  }
 
  public String getValue()
  {
    return this.value;
  }
 
  public void setValue(String value)
  {
    this.value = value;
  }
 
  public int getTiShippingIndex()
  {
    return this.tiShippingIndex;
  }
 
  public void setTiShippingIndex(int tiShippingIndex)
  {
    this.tiShippingIndex = tiShippingIndex;
  }
 
  public String getWriteAmt()
  {
    return this.writeAmt;
  }
 
  public void setWriteAmt(String writeAmt)
  {
    this.writeAmt = writeAmt;
  }
 
  public String getWriteDate()
  {
    return this.writeDate;
  }
 
  public void setWriteDate(String writeDate)
  {
    this.writeDate = writeDate;
  }
 
  public String getSelectedEventRifNo()
  {
    return this.selectedEventRifNo;
  }
 
  public void setSelectedEventRifNo(String selectedEventRifNo)
  {
    this.selectedEventRifNo = selectedEventRifNo;
  }
}