package com.bs.theme.migration.loader.utility;
 
import com.bs.theme.migration.loader.tiplus.pojos.TFilCapp;

import javax.xml.bind.JAXBElement;

import javax.xml.bind.annotation.XmlElementDecl;

import javax.xml.namespace.QName;
 
public class CustomObjectFactory

{

  private static final QName _TFilCapp_QNAME = new QName(

    "urn:TFilCapp.modal.xsd.ti.theme.com", "TFilCapp");

  @XmlElementDecl(namespace="urn:TFilCapp.modal.xsd.ti.theme.com", name="TFilCapp")

  public static JAXBElement<TFilCapp> createTFilCapp(TFilCapp value)

  {

    return new JAXBElement(_TFilCapp_QNAME, TFilCapp.class, null, 

      value);

  }

}