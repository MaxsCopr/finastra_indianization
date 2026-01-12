package com.idfc.in.tradefinance.tfadapter.v1;
 
import java.util.ArrayList;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;

import javax.xml.bind.annotation.XmlAccessorType;

import javax.xml.bind.annotation.XmlElement;

import javax.xml.bind.annotation.XmlType;
 
@XmlAccessorType(XmlAccessType.FIELD)

@XmlType(name="UnSetActFreezeReq", propOrder={"unSetFreezeAccountRq"})

public class UnSetActFreezeReq

{

  @XmlElement(name="UnSetFreezeAccountRq", required=true)

  protected List<FreezeUnfreezeAccountRqDtls> unSetFreezeAccountRq;

  public List<FreezeUnfreezeAccountRqDtls> getUnSetFreezeAccountRq()

  {

    if (this.unSetFreezeAccountRq == null) {

      this.unSetFreezeAccountRq = new ArrayList();

    }

    return this.unSetFreezeAccountRq;

  }

}