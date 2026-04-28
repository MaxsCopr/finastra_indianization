package com.idfc.in.banking.tradefinance.tfadapter.enquiretfccodaccount;
 
import com.idfc.in.tradefinance.tfadapter.v1.EnquireTFCCODAccountRequestType;

import com.idfc.in.tradefinance.tfadapter.v1.EnquireTFCCODAccountResponseType;

import com.idfc.in.tradefinance.tfadapter.v1.ObjectFactory;

import javax.jws.WebMethod;

import javax.jws.WebParam;

import javax.jws.WebResult;

import javax.jws.WebService;

import javax.jws.soap.SOAPBinding;

import javax.jws.soap.SOAPBinding.ParameterStyle;

import javax.xml.bind.annotation.XmlSeeAlso;
 
@WebService(name="EnquireTFCCODAccount", targetNamespace="http://www.idfc.com/IN/Banking/TradeFinance/TFAdapter/EnquireTFCCODAccount")

@SOAPBinding(parameterStyle=SOAPBinding.ParameterStyle.BARE)

@XmlSeeAlso({ObjectFactory.class})

public abstract interface EnquireTFCCODAccount

{

  @WebMethod(operationName="EnquireTFCCODAccount_01", action="EnquireTFCCODAccount_01")

  @WebResult(name="EnquireTFCCODtAccountResponse", targetNamespace="http://www.idfc.com/IN/TradeFinance/TFAdapter/v1", partName="Response")

  public abstract EnquireTFCCODAccountResponseType enquireTFCCODAccount01(@WebParam(name="EnquireTFCCODAccountRequest", targetNamespace="http://www.idfc.com/IN/TradeFinance/TFAdapter/v1", partName="Request") EnquireTFCCODAccountRequestType paramEnquireTFCCODAccountRequestType)

    throws EnquireTFCCODAccountFault;

}