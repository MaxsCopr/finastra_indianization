package com.idfc.in.tradefinance.tfadapter.v1;
 
import javax.xml.bind.annotation.XmlEnum;

import javax.xml.bind.annotation.XmlType;
 
@XmlType(name="FreezeUnfreezeFlagType")

@XmlEnum

public enum FreezeUnfreezeFlagType

{

  F,  R;

  public String value()

  {

    return name();

  }

  public static FreezeUnfreezeFlagType fromValue(String v)

  {

    return valueOf(v);

  }

}