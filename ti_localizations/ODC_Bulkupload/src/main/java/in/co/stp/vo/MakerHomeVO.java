package in.co.stp.vo;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;

public class MakerHomeVO
{
  ArrayList<ExcelDataVO> invoiceList;
  ArrayList<MakerHomeVO> eventlist;
  private File inputFile;
  private String statusFlag;
  String contentDisposition;
  public ArrayList<Object> aaData;
  ArrayList<Object> transactionList;
  ByteArrayInputStream excelStream;
  private String state;
  private String theirref;
  private String behalfofbranch;
  private String drawer;
  private String draweecustomerid;
  private String draweecustomername;
  private String draweeaddress;
  private String collectionamount;
  private String collectioncurrency;
  private String draweecountry;
  private String shipmenttocountry;
  private String shipmentfromcountry;
  private String hscode;
  private String incoterms;
  private String rbi_purposecode;
  private String goodscode;
  private String gooddescription;
  private String portofdestination;
  private String portofloading;
  private String transportdocno;
  private String transportdocdate;
  private String invoiceno;
  private String invoicedate;
  private String form_type;
  private String shpbill_no;
  private String bill_date;
  private String port_code;
  private String form_no;
  private String shp_amt;
  private String shp_currency;
  private String repamt_wrt_amt;
  private String notional_rate;
  private String shortcollectionamount;
  private String reason_for_short_collection;
  private String leo_date;
  private String export_agency;
  private String export_type;
  private String destination_countrry;
  private String iecode;
  private String adcode;
  private String customs_number;
  private String shp_inv_sno;
  private String shp_invoice_no;
  private String shp_invoice_date;
  private String fob_amt;
  private String freight_amt;
  private String ins_amt;
  private String comm_amt;
  private String discount_amt;
  private String deduction_amt;
  private String package_amt;
  private String remittance_adcode;
  private String remittance_num;
  private String firc_no;
  private String remitter_name;
  private String remitter_date;
  private String remitter_country;
  private String utilization_amount;
  private String batchId;
  private String tempStatus;
  private String rowCheck;
  private String status;
 
  public String getStatusFlag()
  {
    return this.statusFlag;
  }
 
  public void setStatusFlag(String statusFlag)
  {
    this.statusFlag = statusFlag;
  }
 
  public File getInputFile()
  {
    return this.inputFile;
  }
 
  public void setInputFile(File inputFile)
  {
    this.inputFile = inputFile;
  }
 
  public ByteArrayInputStream getExcelStream()
  {
    return this.excelStream;
  }
 
  public void setExcelStream(ByteArrayInputStream excelStream)
  {
    this.excelStream = excelStream;
  }
 
  public ArrayList<Object> getTransactionList()
  {
    return this.transactionList;
  }
 
  public void setTransactionList(ArrayList<Object> transactionList)
  {
    this.transactionList = transactionList;
  }
 
  public String getState()
  {
    return this.state;
  }
 
  public void setState(String state)
  {
    this.state = state;
  }
 
  public ArrayList<ExcelDataVO> getInvoiceList()
  {
    return this.invoiceList;
  }
 
  public void setInvoiceList(ArrayList<ExcelDataVO> invoiceList)
  {
    this.invoiceList = invoiceList;
  }
 
  public ArrayList<MakerHomeVO> getEventlist()
  {
    return this.eventlist;
  }
 
  public void setEventlist(ArrayList<MakerHomeVO> eventlist)
  {
    this.eventlist = eventlist;
  }
 
  public String getTheirref()
  {
    return this.theirref;
  }
 
  public void setTheirref(String theirref)
  {
    this.theirref = theirref;
  }
 
  public String getBehalfofbranch()
  {
    return this.behalfofbranch;
  }
 
  public void setBehalfofbranch(String behalfofbranch)
  {
    this.behalfofbranch = behalfofbranch;
  }
 
  public String getDrawer()
  {
    return this.drawer;
  }
 
  public void setDrawer(String drawer)
  {
    this.drawer = drawer;
  }
 
  public String getDraweecustomerid()
  {
    return this.draweecustomerid;
  }
 
  public void setDraweecustomerid(String draweecustomerid)
  {
    this.draweecustomerid = draweecustomerid;
  }
 
  public String getDraweecustomername()
  {
    return this.draweecustomername;
  }
 
  public void setDraweecustomername(String draweecustomername)
  {
    this.draweecustomername = draweecustomername;
  }
 
  public String getDraweeaddress()
  {
    return this.draweeaddress;
  }
 
  public void setDraweeaddress(String draweeaddress)
  {
    this.draweeaddress = draweeaddress;
  }
 
  public String getCollectionamount()
  {
    return this.collectionamount;
  }
 
  public void setCollectionamount(String collectionamount)
  {
    this.collectionamount = collectionamount;
  }
 
  public String getCollectioncurrency()
  {
    return this.collectioncurrency;
  }
 
  public void setCollectioncurrency(String collectioncurrency)
  {
    this.collectioncurrency = collectioncurrency;
  }
 
  public String getDraweecountry()
  {
    return this.draweecountry;
  }
 
  public void setDraweecountry(String draweecountry)
  {
    this.draweecountry = draweecountry;
  }
 
  public String getShipmenttocountry()
  {
    return this.shipmenttocountry;
  }
 
  public void setShipmenttocountry(String shipmenttocountry)
  {
    this.shipmenttocountry = shipmenttocountry;
  }
 
  public String getShipmentfromcountry()
  {
    return this.shipmentfromcountry;
  }
 
  public void setShipmentfromcountry(String shipmentfromcountry)
  {
    this.shipmentfromcountry = shipmentfromcountry;
  }
 
  public String getHscode()
  {
    return this.hscode;
  }
 
  public void setHscode(String hscode)
  {
    this.hscode = hscode;
  }
 
  public String getIncoterms()
  {
    return this.incoterms;
  }
 
  public void setIncoterms(String incoterms)
  {
    this.incoterms = incoterms;
  }
 
  public String getRbi_purposecode()
  {
    return this.rbi_purposecode;
  }
 
  public void setRbi_purposecode(String rbi_purposecode)
  {
    this.rbi_purposecode = rbi_purposecode;
  }
 
  public String getGoodscode()
  {
    return this.goodscode;
  }
 
  public void setGoodscode(String goodscode)
  {
    this.goodscode = goodscode;
  }
 
  public String getGooddescription()
  {
    return this.gooddescription;
  }
 
  public void setGooddescription(String gooddescription)
  {
    this.gooddescription = gooddescription;
  }
 
  public String getPortofdestination()
  {
    return this.portofdestination;
  }
 
  public void setPortofdestination(String portofdestination)
  {
    this.portofdestination = portofdestination;
  }
 
  public String getPortofloading()
  {
    return this.portofloading;
  }
 
  public void setPortofloading(String portofloading)
  {
    this.portofloading = portofloading;
  }
 
  public String getTransportdocno()
  {
    return this.transportdocno;
  }
 
  public void setTransportdocno(String transportdocno)
  {
    this.transportdocno = transportdocno;
  }
 
  public String getTransportdocdate()
  {
    return this.transportdocdate;
  }
 
  public void setTransportdocdate(String transportdocdate)
  {
    this.transportdocdate = transportdocdate;
  }
 
  public String getInvoiceno()
  {
    return this.invoiceno;
  }
 
  public void setInvoiceno(String invoiceno)
  {
    this.invoiceno = invoiceno;
  }
 
  public String getInvoicedate()
  {
    return this.invoicedate;
  }
 
  public void setInvoicedate(String invoicedate)
  {
    this.invoicedate = invoicedate;
  }
 
  public String getForm_type()
  {
    return this.form_type;
  }
 
  public void setForm_type(String form_type)
  {
    this.form_type = form_type;
  }
 
  public String getShpbill_no()
  {
    return this.shpbill_no;
  }
 
  public void setShpbill_no(String shpbill_no)
  {
    this.shpbill_no = shpbill_no;
  }
 
  public String getBill_date()
  {
    return this.bill_date;
  }
 
  public void setBill_date(String bill_date)
  {
    this.bill_date = bill_date;
  }
 
  public String getPort_code()
  {
    return this.port_code;
  }
 
  public void setPort_code(String port_code)
  {
    this.port_code = port_code;
  }
 
  public String getForm_no()
  {
    return this.form_no;
  }
 
  public void setForm_no(String form_no)
  {
    this.form_no = form_no;
  }
 
  public String getShp_amt()
  {
    return this.shp_amt;
  }
 
  public void setShp_amt(String shp_amt)
  {
    this.shp_amt = shp_amt;
  }
 
  public String getShp_currency()
  {
    return this.shp_currency;
  }
 
  public void setShp_currency(String shp_currency)
  {
    this.shp_currency = shp_currency;
  }
 
  public String getRepamt_wrt_amt()
  {
    return this.repamt_wrt_amt;
  }
 
  public void setRepamt_wrt_amt(String repamt_wrt_amt)
  {
    this.repamt_wrt_amt = repamt_wrt_amt;
  }
 
  public String getNotional_rate()
  {
    return this.notional_rate;
  }
 
  public void setNotional_rate(String notional_rate)
  {
    this.notional_rate = notional_rate;
  }
 
  public String getShortcollectionamount()
  {
    return this.shortcollectionamount;
  }
 
  public void setShortcollectionamount(String shortcollectionamount)
  {
    this.shortcollectionamount = shortcollectionamount;
  }
 
  public String getReason_for_short_collection()
  {
    return this.reason_for_short_collection;
  }
 
  public void setReason_for_short_collection(String reason_for_short_collection)
  {
    this.reason_for_short_collection = reason_for_short_collection;
  }
 
  public String getLeo_date()
  {
    return this.leo_date;
  }
 
  public void setLeo_date(String leo_date)
  {
    this.leo_date = leo_date;
  }
 
  public String getExport_agency()
  {
    return this.export_agency;
  }
 
  public void setExport_agency(String export_agency)
  {
    this.export_agency = export_agency;
  }
 
  public String getExport_type()
  {
    return this.export_type;
  }
 
  public void setExport_type(String export_type)
  {
    this.export_type = export_type;
  }
 
  public String getDestination_countrry()
  {
    return this.destination_countrry;
  }
 
  public void setDestination_countrry(String destination_countrry)
  {
    this.destination_countrry = destination_countrry;
  }
 
  public String getIecode()
  {
    return this.iecode;
  }
 
  public void setIecode(String iecode)
  {
    this.iecode = iecode;
  }
 
  public String getAdcode()
  {
    return this.adcode;
  }
 
  public void setAdcode(String adcode)
  {
    this.adcode = adcode;
  }
 
  public String getCustoms_number()
  {
    return this.customs_number;
  }
 
  public void setCustoms_number(String customs_number)
  {
    this.customs_number = customs_number;
  }
 
  public String getShp_inv_sno()
  {
    return this.shp_inv_sno;
  }
 
  public void setShp_inv_sno(String shp_inv_sno)
  {
    this.shp_inv_sno = shp_inv_sno;
  }
 
  public String getShp_invoice_no()
  {
    return this.shp_invoice_no;
  }
 
  public void setShp_invoice_no(String shp_invoice_no)
  {
    this.shp_invoice_no = shp_invoice_no;
  }
 
  public String getShp_invoice_date()
  {
    return this.shp_invoice_date;
  }
 
  public void setShp_invoice_date(String shp_invoice_date)
  {
    this.shp_invoice_date = shp_invoice_date;
  }
 
  public String getFob_amt()
  {
    return this.fob_amt;
  }
 
  public void setFob_amt(String fob_amt)
  {
    this.fob_amt = fob_amt;
  }
 
  public String getFreight_amt()
  {
    return this.freight_amt;
  }
 
  public void setFreight_amt(String freight_amt)
  {
    this.freight_amt = freight_amt;
  }
 
  public String getIns_amt()
  {
    return this.ins_amt;
  }
 
  public void setIns_amt(String ins_amt)
  {
    this.ins_amt = ins_amt;
  }
 
  public String getComm_amt()
  {
    return this.comm_amt;
  }
 
  public void setComm_amt(String comm_amt)
  {
    this.comm_amt = comm_amt;
  }
 
  public String getDiscount_amt()
  {
    return this.discount_amt;
  }
 
  public void setDiscount_amt(String discount_amt)
  {
    this.discount_amt = discount_amt;
  }
 
  public String getDeduction_amt()
  {
    return this.deduction_amt;
  }
 
  public void setDeduction_amt(String deduction_amt)
  {
    this.deduction_amt = deduction_amt;
  }
 
  public String getPackage_amt()
  {
    return this.package_amt;
  }
 
  public void setPackage_amt(String package_amt)
  {
    this.package_amt = package_amt;
  }
 
  public String getRemittance_adcode()
  {
    return this.remittance_adcode;
  }
 
  public void setRemittance_adcode(String remittance_adcode)
  {
    this.remittance_adcode = remittance_adcode;
  }
 
  public String getRemittance_num()
  {
    return this.remittance_num;
  }
 
  public void setRemittance_num(String remittance_num)
  {
    this.remittance_num = remittance_num;
  }
 
  public String getFirc_no()
  {
    return this.firc_no;
  }
 
  public void setFirc_no(String firc_no)
  {
    this.firc_no = firc_no;
  }
 
  public String getRemitter_name()
  {
    return this.remitter_name;
  }
 
  public void setRemitter_name(String remitter_name)
  {
    this.remitter_name = remitter_name;
  }
 
  public String getRemitter_date()
  {
    return this.remitter_date;
  }
 
  public void setRemitter_date(String remitter_date)
  {
    this.remitter_date = remitter_date;
  }
 
  public String getRemitter_country()
  {
    return this.remitter_country;
  }
 
  public void setRemitter_country(String remitter_country)
  {
    this.remitter_country = remitter_country;
  }
 
  public String getUtilization_amount()
  {
    return this.utilization_amount;
  }
 
  public void setUtilization_amount(String utilization_amount)
  {
    this.utilization_amount = utilization_amount;
  }
 
  public String getBatchId()
  {
    return this.batchId;
  }
 
  public void setBatchId(String batchId)
  {
    this.batchId = batchId;
  }
 
  public String getTempStatus()
  {
    return this.tempStatus;
  }
 
  public void setTempStatus(String tempStatus)
  {
    this.tempStatus = tempStatus;
  }
 
  public String getStatus()
  {
    return this.status;
  }
 
  public void setStatus(String status)
  {
    this.status = status;
  }
 
  public String getRowCheck()
  {
    return this.rowCheck;
  }
 
  public void setRowCheck(String rowCheck)
  {
    this.rowCheck = rowCheck;
  }
}