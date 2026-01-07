package com.idfc.in.tradefinance.tfadapter.v1;
 
import javax.xml.bind.annotation.XmlAccessType;

import javax.xml.bind.annotation.XmlAccessorType;

import javax.xml.bind.annotation.XmlElement;

import javax.xml.bind.annotation.XmlType;
 
@XmlAccessorType(XmlAccessType.FIELD)

@XmlType(name="SetActFreezeRep", propOrder={"status"})

public class SetActFreezeRep

{

  @XmlElement(name="Status", required=true)

  protected String status;

  public String getStatus()

  {

    return this.status;

  }

  public void setStatus(String value)

  {

    this.status = value;

  }

}