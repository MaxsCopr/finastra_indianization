package com.bs.wiseconnect.migration.loader.tiplus.pojos;
 
import javax.persistence.Basic;

import javax.persistence.Column;

import javax.persistence.Id;

import javax.persistence.Table;
 
@Table(name="TiattdocTable")

public class TiattdocExtra

{

  @Column(name="BRANCH")

  private String branch;

  @Column(name="CUSTOMER")

  private String customer;

  @Column(name="PRODUCT")

  private String product;

  @Column(name="EVENT")

  private String event;

  @Column(name="THEIRREFERENCE")

  private String theirreference;

  @Column(name="TEAM")

  private String team;

  @Column(name="BEHALFOFBRANCH")

  private String behalfofbranch;

  @Column(name="INSCONAME")

  private String insconame;

  @Id

  @Basic(optional=false)

  @Column(name="LOGID")

  private String logid;

  @Column(name="STATUS")

  private String status;

  @Column(name="ERRORDTLS")

  private String errorDtls;

  @Column(name="TICORRID")

  private String ticorrid;

  public String getBranch()

  {

    return this.branch;

  }

  public void setBranch(String branch)

  {

    this.branch = branch;

  }

  public String getCustomer()

  {

    return this.customer;

  }

  public void setCustomer(String customer)

  {

    this.customer = customer;

  }

  public String getProduct()

  {

    return this.product;

  }

  public void setProduct(String product)

  {

    this.product = product;

  }

  public String getEvent()

  {

    return this.event;

  }

  public void setEvent(String event)

  {

    this.event = event;

  }

  public String getTheirreference()

  {

    return this.theirreference;

  }

  public void setTheirreference(String theirreference)

  {

    this.theirreference = theirreference;

  }

  public String getTeam()

  {

    return this.team;

  }

  public void setTeam(String team)

  {

    this.team = team;

  }

  public String getBehalfofbranch()

  {

    return this.behalfofbranch;

  }

  public void setBehalfofbranch(String behalfofbranch)

  {

    this.behalfofbranch = behalfofbranch;

  }

  public String getInsconame()

  {

    return this.insconame;

  }

  public void setInsconame(String insconame)

  {

    this.insconame = insconame;

  }

  public String getLogid()

  {

    return this.logid;

  }

  public void setLogid(String logid)

  {

    this.logid = logid;

  }

  public String getStatus()

  {

    return this.status;

  }

  public void setStatus(String status)

  {

    this.status = status;

  }

  public String getErrorDtls()

  {

    return this.errorDtls;

  }

  public void setErrorDtls(String errorDtls)

  {

    this.errorDtls = errorDtls;

  }

  public String getTicorrid()

  {

    return this.ticorrid;

  }

  public void setTicorrid(String ticorrid)

  {

    this.ticorrid = ticorrid;

  }

}