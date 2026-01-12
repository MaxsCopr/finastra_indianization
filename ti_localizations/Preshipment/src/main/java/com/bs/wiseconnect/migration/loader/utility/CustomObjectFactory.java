package com.bs.wiseconnect.migration.loader.utility;
 
import javax.xml.bind.JAXBElement;

import javax.xml.bind.annotation.XmlElementDecl;

import javax.xml.namespace.QName;

import com.bs.wiseconnect.migration.loader.tiplus.pojos.TFilCapp;
 
public class CustomObjectFactory

{

  private static final QName _TFilCapp_QNAME = new QName(

    "urn:TFilCapp.modal.xsd.ti.wiseconnect.com", "TFilCapp");

  @XmlElementDecl(namespace="urn:TFilCapp.modal.xsd.ti.wiseconnect.com", name="TFilCapp")

  public static JAXBElement<TFilCapp> createTFilCapp(TFilCapp value)

  {

    return new JAXBElement(_TFilCapp_QNAME, TFilCapp.class, null, 

      value);

  }

}