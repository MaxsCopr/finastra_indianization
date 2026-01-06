package in.co.FIRC.vo.ourBank;

import in.co.FIRC.vo.AlertMessageVO;
import java.util.ArrayList;

public class PrintOurBankVO
{
  private String transrefno;
  private String transactionrefno;
  private String issuingBank;
  private String orderingcustomer;
  private String country;
  private String amount;
  private String benificiarydetails;
  private String paidamount;
  private String purposecode;
  private String value_date;
  private String available_amt;
  private String originalHeld;
  private String firc_printed_no;
  private String exchange_rate;
  private String firc_our_bank;
  private String userId;
  private String rem_country;
  private String rem_address;
  private String fircdate;
  private String fircno;
  private String issunibranch;
  private String remmitngbankdetails;
  private String currency;
  private String paymentdate;
  private String paidcurrency;
  private String purposedesc;
  private String purposedescription;
  private String paymenttransfermethod;
  private String cif_no;
  private String rembank;
  private String bendetails;
  private String mode;
  private String remarks;
  ArrayList<AlertMessageVO> errorList = null;
  private String systemDate;
  ArrayList<PrintOurBankVO> printList = null;
  ArrayList<PrintOurBankVO> accList = null;
  private String tempPurposeCode;
  private String accNo;
  private String fircAmount;
  private String fircCurr;
  private String accType;
  private String fircEX;
  private String fircConAmt;
  private String fircCurrRec;
  private String oddAddress;
  private String fcy;
  private String lcy;
  private String fx_rate;
  private String indexVal;
  private String indexVal1;
  private String dd_tt_RefNo;
  private String nostroNo;
  private String nostroDate;
  private String accDescription;
  private String amountWord;
  private String address1;
  private String address2;
  private String address3;
  private String address4;
  private String address5;
  private String inwardType;
  private String solIdAddress;
 
  public String getSolIdAddress()
  {
    return this.solIdAddress;
  }
 
  public void setSolIdAddress(String solIdAddress)
  {
    this.solIdAddress = solIdAddress;
  }
 
  public String getInwardType()
  {
    return this.inwardType;
  }
 
  public void setInwardType(String inwardType)
  {
    this.inwardType = inwardType;
  }
 
  public String getAddress1()
  {
    return this.address1;
  }
 
  public void setAddress1(String address1)
  {
    this.address1 = address1;
  }
 
  public String getAddress2()
  {
    return this.address2;
  }
 
  public void setAddress2(String address2)
  {
    this.address2 = address2;
  }
 
  public String getAddress3()
  {
    return this.address3;
  }
 
  public void setAddress3(String address3)
  {
    this.address3 = address3;
  }
 
  public String getAddress4()
  {
    return this.address4;
  }
 
  public void setAddress4(String address4)
  {
    this.address4 = address4;
  }
 
  public String getAddress5()
  {
    return this.address5;
  }
 
  public void setAddress5(String address5)
  {
    this.address5 = address5;
  }
 
  public String getAmountWord()
  {
    return this.amountWord;
  }
 
  public void setAmountWord(String amountWord)
  {
    this.amountWord = amountWord;
  }
 
  public String getAccDescription()
  {
    return this.accDescription;
  }
 
  public void setAccDescription(String accDescription)
  {
    this.accDescription = accDescription;
  }
 
  public String getNostroNo()
  {
    return this.nostroNo;
  }
 
  public void setNostroNo(String nostroNo)
  {
    this.nostroNo = nostroNo;
  }
 
  public String getNostroDate()
  {
    return this.nostroDate;
  }
 
  public void setNostroDate(String nostroDate)
  {
    this.nostroDate = nostroDate;
  }
 
  public String getDd_tt_RefNo()
  {
    return this.dd_tt_RefNo;
  }
 
  public void setDd_tt_RefNo(String dd_tt_RefNo)
  {
    this.dd_tt_RefNo = dd_tt_RefNo;
  }
 
  public String getTempPurposeCode()
  {
    return this.tempPurposeCode;
  }
 
  public void setTempPurposeCode(String tempPurposeCode)
  {
    this.tempPurposeCode = tempPurposeCode;
  }
 
  public String getIndexVal1()
  {
    return this.indexVal1;
  }
 
  public void setIndexVal1(String indexVal1)
  {
    this.indexVal1 = indexVal1;
  }
 
  public String getIndexVal()
  {
    return this.indexVal;
  }
 
  public void setIndexVal(String indexVal)
  {
    this.indexVal = indexVal;
  }
 
  public String getFircCurrRec()
  {
    return this.fircCurrRec;
  }
 
  public void setFircCurrRec(String fircCurrRec)
  {
    this.fircCurrRec = fircCurrRec;
  }
 
  public String getOddAddress()
  {
    return this.oddAddress;
  }
 
  public void setOddAddress(String oddAddress)
  {
    this.oddAddress = oddAddress;
  }
 
  public String getFcy()
  {
    return this.fcy;
  }
 
  public void setFcy(String fcy)
  {
    this.fcy = fcy;
  }
 
  public String getLcy()
  {
    return this.lcy;
  }
 
  public void setLcy(String lcy)
  {
    this.lcy = lcy;
  }
 
  public String getFx_rate()
  {
    return this.fx_rate;
  }
 
  public void setFx_rate(String fx_rate)
  {
    this.fx_rate = fx_rate;
  }
 
  public String getFircEX()
  {
    return this.fircEX;
  }
 
  public void setFircEX(String fircEX)
  {
    this.fircEX = fircEX;
  }
 
  public String getFircConAmt()
  {
    return this.fircConAmt;
  }
 
  public void setFircConAmt(String fircConAmt)
  {
    this.fircConAmt = fircConAmt;
  }
 
  public String getAccType()
  {
    return this.accType;
  }
 
  public void setAccType(String accType)
  {
    this.accType = accType;
  }
 
  public String getAccNo()
  {
    return this.accNo;
  }
 
  public void setAccNo(String accNo)
  {
    this.accNo = accNo;
  }
 
  public String getFircAmount()
  {
    return this.fircAmount;
  }
 
  public void setFircAmount(String fircAmount)
  {
    this.fircAmount = fircAmount;
  }
 
  public String getFircCurr()
  {
    return this.fircCurr;
  }
 
  public void setFircCurr(String fircCurr)
  {
    this.fircCurr = fircCurr;
  }
 
  public ArrayList<PrintOurBankVO> getPrintList()
  {
    return this.printList;
  }
 
  public void setPrintList(ArrayList<PrintOurBankVO> printList)
  {
    this.printList = printList;
  }
 
  public ArrayList<PrintOurBankVO> getAccList()
  {
    return this.accList;
  }
 
  public void setAccList(ArrayList<PrintOurBankVO> accList)
  {
    this.accList = accList;
  }
 
  public String getSystemDate()
  {
    return this.systemDate;
  }
 
  public void setSystemDate(String systemDate)
  {
    this.systemDate = systemDate;
  }
 
  public String getTransrefno()
  {
    return this.transrefno;
  }
 
  public void setTransrefno(String transrefno)
  {
    this.transrefno = transrefno;
  }
 
  public String getTransactionrefno()
  {
    return this.transactionrefno;
  }
 
  public void setTransactionrefno(String transactionrefno)
  {
    this.transactionrefno = transactionrefno;
  }
 
  public String getIssuingBank()
  {
    return this.issuingBank;
  }
 
  public void setIssuingBank(String issuingBank)
  {
    this.issuingBank = issuingBank;
  }
 
  public String getOrderingcustomer()
  {
    return this.orderingcustomer;
  }
 
  public void setOrderingcustomer(String orderingcustomer)
  {
    this.orderingcustomer = orderingcustomer;
  }
 
  public String getCountry()
  {
    return this.country;
  }
 
  public void setCountry(String country)
  {
    this.country = country;
  }
 
  public String getAmount()
  {
    return this.amount;
  }
 
  public void setAmount(String amount)
  {
    this.amount = amount;
  }
 
  public String getBenificiarydetails()
  {
    return this.benificiarydetails;
  }
 
  public void setBenificiarydetails(String benificiarydetails)
  {
    this.benificiarydetails = benificiarydetails;
  }
 
  public String getPaidamount()
  {
    return this.paidamount;
  }
 
  public void setPaidamount(String paidamount)
  {
    this.paidamount = paidamount;
  }
 
  public String getPurposecode()
  {
    return this.purposecode;
  }
 
  public void setPurposecode(String purposecode)
  {
    this.purposecode = purposecode;
  }
 
  public String getValue_date()
  {
    return this.value_date;
  }
 
  public void setValue_date(String value_date)
  {
    this.value_date = value_date;
  }
 
  public String getAvailable_amt()
  {
    return this.available_amt;
  }
 
  public void setAvailable_amt(String available_amt)
  {
    this.available_amt = available_amt;
  }
 
  public String getOriginalHeld()
  {
    return this.originalHeld;
  }
 
  public void setOriginalHeld(String originalHeld)
  {
    this.originalHeld = originalHeld;
  }
 
  public String getFirc_printed_no()
  {
    return this.firc_printed_no;
  }
 
  public void setFirc_printed_no(String firc_printed_no)
  {
    this.firc_printed_no = firc_printed_no;
  }
 
  public String getExchange_rate()
  {
    return this.exchange_rate;
  }
 
  public void setExchange_rate(String exchange_rate)
  {
    this.exchange_rate = exchange_rate;
  }
 
  public String getFirc_our_bank()
  {
    return this.firc_our_bank;
  }
 
  public void setFirc_our_bank(String firc_our_bank)
  {
    this.firc_our_bank = firc_our_bank;
  }
 
  public String getUserId()
  {
    return this.userId;
  }
 
  public void setUserId(String userId)
  {
    this.userId = userId;
  }
 
  public String getRem_country()
  {
    return this.rem_country;
  }
 
  public void setRem_country(String rem_country)
  {
    this.rem_country = rem_country;
  }
 
  public String getRem_address()
  {
    return this.rem_address;
  }
 
  public void setRem_address(String rem_address)
  {
    this.rem_address = rem_address;
  }
 
  public String getFircdate()
  {
    return this.fircdate;
  }
 
  public void setFircdate(String fircdate)
  {
    this.fircdate = fircdate;
  }
 
  public String getFircno()
  {
    return this.fircno;
  }
 
  public void setFircno(String fircno)
  {
    this.fircno = fircno;
  }
 
  public String getIssunibranch()
  {
    return this.issunibranch;
  }
 
  public void setIssunibranch(String issunibranch)
  {
    this.issunibranch = issunibranch;
  }
 
  public String getRemmitngbankdetails()
  {
    return this.remmitngbankdetails;
  }
 
  public void setRemmitngbankdetails(String remmitngbankdetails)
  {
    this.remmitngbankdetails = remmitngbankdetails;
  }
 
  public String getCurrency()
  {
    return this.currency;
  }
 
  public void setCurrency(String currency)
  {
    this.currency = currency;
  }
 
  public String getPaymentdate()
  {
    return this.paymentdate;
  }
 
  public void setPaymentdate(String paymentdate)
  {
    this.paymentdate = paymentdate;
  }
 
  public String getPaidcurrency()
  {
    return this.paidcurrency;
  }
 
  public void setPaidcurrency(String paidcurrency)
  {
    this.paidcurrency = paidcurrency;
  }
 
  public String getPurposedesc()
  {
    return this.purposedesc;
  }
 
  public void setPurposedesc(String purposedesc)
  {
    this.purposedesc = purposedesc;
  }
 
  public String getPurposedescription()
  {
    return this.purposedescription;
  }
 
  public void setPurposedescription(String purposedescription)
  {
    this.purposedescription = purposedescription;
  }
 
  public String getPaymenttransfermethod()
  {
    return this.paymenttransfermethod;
  }
 
  public void setPaymenttransfermethod(String paymenttransfermethod)
  {
    this.paymenttransfermethod = paymenttransfermethod;
  }
 
  public String getCif_no()
  {
    return this.cif_no;
  }
 
  public void setCif_no(String cif_no)
  {
    this.cif_no = cif_no;
  }
 
  public String getRembank()
  {
    return this.rembank;
  }
 
  public void setRembank(String rembank)
  {
    this.rembank = rembank;
  }
 
  public String getBendetails()
  {
    return this.bendetails;
  }
 
  public void setBendetails(String bendetails)
  {
    this.bendetails = bendetails;
  }
 
  public String getMode()
  {
    return this.mode;
  }
 
  public void setMode(String mode)
  {
    this.mode = mode;
  }
 
  public String getRemarks()
  {
    return this.remarks;
  }
 
  public void setRemarks(String remarks)
  {
    this.remarks = remarks;
  }
 
  public ArrayList<AlertMessageVO> getErrorList()
  {
    return this.errorList;
  }
 
  public void setErrorList(ArrayList<AlertMessageVO> errorList)
  {
    this.errorList = errorList;
  }
}
