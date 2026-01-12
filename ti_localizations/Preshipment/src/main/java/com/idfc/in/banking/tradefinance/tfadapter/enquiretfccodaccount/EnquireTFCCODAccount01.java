package com.idfc.in.banking.tradefinance.tfadapter.enquiretfccodaccount;
 
import java.net.MalformedURLException;

import java.net.URL;

import javax.xml.namespace.QName;

import javax.xml.ws.Service;

import javax.xml.ws.WebEndpoint;

import javax.xml.ws.WebServiceClient;

import javax.xml.ws.WebServiceException;

import javax.xml.ws.WebServiceFeature;
 
@WebServiceClient(name="EnquireTFCCODAccount_01", targetNamespace="http://www.idfc.com/IN/Banking/TradeFinance/TFAdapter/EnquireTFCCODAccount", wsdlLocation="file:/D:/WSDL/EnquireTFCCODAccount.wsdl")

public class EnquireTFCCODAccount01

  extends Service

{

  private static final URL ENQUIRETFCCODACCOUNT01_WSDL_LOCATION;

  private static final WebServiceException ENQUIRETFCCODACCOUNT01_EXCEPTION;

  private static final QName ENQUIRETFCCODACCOUNT01_QNAME = new QName("http://www.idfc.com/IN/Banking/TradeFinance/TFAdapter/EnquireTFCCODAccount", "EnquireTFCCODAccount_01");

  static

  {

    URL url = null;

    WebServiceException e = null;

    try

    {

      url = new URL("file:/D:/WSDL/EnquireTFCCODAccount.wsdl");

    }

    catch (MalformedURLException ex)

    {

      e = new WebServiceException(ex);

    }

    ENQUIRETFCCODACCOUNT01_WSDL_LOCATION = url;

    ENQUIRETFCCODACCOUNT01_EXCEPTION = e;

  }

  public EnquireTFCCODAccount01()

  {

    super(__getWsdlLocation(), ENQUIRETFCCODACCOUNT01_QNAME);

  }

  public EnquireTFCCODAccount01(WebServiceFeature... features)

  {

    super(__getWsdlLocation(), ENQUIRETFCCODACCOUNT01_QNAME);

  }

  public EnquireTFCCODAccount01(URL wsdlLocation)

  {

    super(wsdlLocation, ENQUIRETFCCODACCOUNT01_QNAME);

  }

  public EnquireTFCCODAccount01(URL wsdlLocation, WebServiceFeature... features)

  {

    super(wsdlLocation, ENQUIRETFCCODACCOUNT01_QNAME);

  }

  public EnquireTFCCODAccount01(URL wsdlLocation, QName serviceName)

  {

    super(wsdlLocation, serviceName);

  }

  public EnquireTFCCODAccount01(URL wsdlLocation, QName serviceName, WebServiceFeature... features)

  {

    super(wsdlLocation, serviceName);

  }

  @WebEndpoint(name="EnquireTFCCODAccount_01")

  public EnquireTFCCODAccount getEnquireTFCCODAccount01()

  {

    return (EnquireTFCCODAccount)super.getPort(new QName("http://www.idfc.com/IN/Banking/TradeFinance/TFAdapter/EnquireTFCCODAccount", "EnquireTFCCODAccount_01"), EnquireTFCCODAccount.class);

  }

  @WebEndpoint(name="EnquireTFCCODAccount_01")

  public EnquireTFCCODAccount getEnquireTFCCODAccount01(WebServiceFeature... features)

  {

    return (EnquireTFCCODAccount)super.getPort(new QName("http://www.idfc.com/IN/Banking/TradeFinance/TFAdapter/EnquireTFCCODAccount", "EnquireTFCCODAccount_01"), EnquireTFCCODAccount.class, features);

  }

  private static URL __getWsdlLocation()

  {

    if (ENQUIRETFCCODACCOUNT01_EXCEPTION != null) {

      throw ENQUIRETFCCODACCOUNT01_EXCEPTION;

    }

    return ENQUIRETFCCODACCOUNT01_WSDL_LOCATION;

  }

}