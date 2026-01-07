package com.idfc.in.tradefinance.tfadapter.v1;
 
import javax.xml.bind.annotation.XmlAccessType;

import javax.xml.bind.annotation.XmlAccessorType;

import javax.xml.bind.annotation.XmlType;

import javax.xml.bind.annotation.XmlValue;
 
@XmlAccessorType(XmlAccessType.FIELD)

@XmlType(name="fetchFxContractDetailsByDealIDRequestType", propOrder={"value"})

public class FetchFxContractDetailsByDealIDRequestType

{

  @XmlValue

  protected String value;

  public String getValue()

  {

    return this.value;

  }

  public void setValue(String value)

  {

    this.value = value;

  }

}