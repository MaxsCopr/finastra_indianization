package com.idfc.in.tradefinance.tfadapter.v1;
 
import javax.xml.bind.annotation.XmlAccessType;

import javax.xml.bind.annotation.XmlAccessorType;

import javax.xml.bind.annotation.XmlElement;

import javax.xml.bind.annotation.XmlType;
 
@XmlAccessorType(XmlAccessType.FIELD)

@XmlType(name="FreezeUnfreezeAccountRqDtls", propOrder={"acctNum", "creditDebitFlag", "freezeUnfreezeFlag"})

public class FreezeUnfreezeAccountRqDtls

{

  @XmlElement(name="AcctNum", required=true)

  protected String acctNum;

  @XmlElement(name="CreditDebitFlag", required=true)

  protected String creditDebitFlag;

  @XmlElement(name="FreezeUnfreezeFlag", required=true)

  protected FreezeUnfreezeFlagType freezeUnfreezeFlag;

  public String getAcctNum()

  {

    return this.acctNum;

  }

  public void setAcctNum(String value)

  {

    this.acctNum = value;

  }

  public String getCreditDebitFlag()

  {

    return this.creditDebitFlag;

  }

  public void setCreditDebitFlag(String value)

  {

    this.creditDebitFlag = value;

  }

  public FreezeUnfreezeFlagType getFreezeUnfreezeFlag()

  {

    return this.freezeUnfreezeFlag;

  }

  public void setFreezeUnfreezeFlag(FreezeUnfreezeFlagType value)

  {

    this.freezeUnfreezeFlag = value;

  }

}