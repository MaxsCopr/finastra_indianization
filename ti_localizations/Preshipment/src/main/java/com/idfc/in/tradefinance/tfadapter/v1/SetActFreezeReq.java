package com.idfc.in.tradefinance.tfadapter.v1;
 
import java.util.ArrayList;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;

import javax.xml.bind.annotation.XmlAccessorType;

import javax.xml.bind.annotation.XmlElement;

import javax.xml.bind.annotation.XmlType;
 
@XmlAccessorType(XmlAccessType.FIELD)

@XmlType(name="SetActFreezeReq", propOrder={"setFreezeAccountRq"})

public class SetActFreezeReq

{

  @XmlElement(name="SetFreezeAccountRq", required=true)

  protected List<FreezeUnfreezeAccountRqDtls> setFreezeAccountRq;

  public List<FreezeUnfreezeAccountRqDtls> getSetFreezeAccountRq()

  {

    if (this.setFreezeAccountRq == null) {

      this.setFreezeAccountRq = new ArrayList();

    }

    return this.setFreezeAccountRq;

  }

}