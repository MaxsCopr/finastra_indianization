package com.idfc.in.banking.tradefinance.tfadapter.enquiretfccodaccount;
 
import javax.xml.ws.WebFault;
 
@WebFault(name="string_element", targetNamespace="http://www.idfc.com/IN/TradeFinance/TFAdapter/v1")

public class EnquireTFCCODAccountFault

  extends Exception

{

  private String faultInfo;

  public EnquireTFCCODAccountFault(String message, String faultInfo)

  {

    super(message);

    this.faultInfo = faultInfo;

  }

  public EnquireTFCCODAccountFault(String message, String faultInfo, Throwable cause)

  {

    super(message, cause);

    this.faultInfo = faultInfo;

  }

  public String getFaultInfo()

  {

    return this.faultInfo;

  }

}