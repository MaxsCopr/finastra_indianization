package com.in.fetchAccount.bean;
 
public class FetchAccountRequestBean

{

  private String sessionUserName = "";

  private String masterRef = "";

  private String eventRef = "";

  private String customerId = "";

  private String dbKeyValue = "";

  private String btnStatus = "";

  public String getSessionUserName()

  {

    return this.sessionUserName;

  }

  public void setSessionUserName(String sessionUserName)

  {

    this.sessionUserName = sessionUserName;

  }

  public String getMasterRef()

  {

    return this.masterRef;

  }

  public void setMasterRef(String masterRef)

  {

    this.masterRef = masterRef;

  }

  public String getEventRef()

  {

    return this.eventRef;

  }

  public void setEventRef(String eventRef)

  {

    this.eventRef = eventRef;

  }

  public String getCustomerId()

  {

    return this.customerId;

  }

  public void setCustomerId(String customerId)

  {

    this.customerId = customerId;

  }

  public String getDbKeyValue()

  {

    return this.dbKeyValue;

  }

  public void setDbKeyValue(String dbKeyValue)

  {

    this.dbKeyValue = dbKeyValue;

  }

  public String getBtnStatus()

  {

    return this.btnStatus;

  }

  public void setBtnStatus(String btnStatus)

  {

    this.btnStatus = btnStatus;

  }

}