package com.bs.wiseconnect.migration.loader.tiplus.pojos;
 
public class MT754Model

{

  private String senderSwiftAddress;

  private String receiverSwiftAddress;

  private String senderReference;

  private String relatedReference;

  private String claimCurrency;

  private String claimAmount;

  private String additionalAmount;

  private String additionalAmountCurrency;

  private String totalAmountClaimed;

  private String totalAmountClaimedCurrency;

  private String reimbursingBank;

  private String accountWithBank;

  private String beneficiaryBank;

  private String senderToReceiverInformation;

  private String valueDate;

  public String getValueDate()

  {

    return this.valueDate;

  }

  public void setValueDate(String valueDate)

  {

    this.valueDate = valueDate;

  }

  public String getAccountWithBank()

  {

    return this.accountWithBank;

  }

  public void setAccountWithBank(String accountWithBank)

  {

    this.accountWithBank = accountWithBank;

  }

  public String getAdditionalAmount()

  {

    return this.additionalAmount;

  }

  public void setAdditionalAmount(String additionalAmount)

  {

    this.additionalAmount = additionalAmount;

  }

  public String getAdditionalAmountCurrency()

  {

    return this.additionalAmountCurrency;

  }

  public void setAdditionalAmountCurrency(String additionalAmountCurrency)

  {

    this.additionalAmountCurrency = additionalAmountCurrency;

  }

  public String getBeneficiaryBank()

  {

    return this.beneficiaryBank;

  }

  public void setBeneficiaryBank(String beneficiaryBank)

  {

    this.beneficiaryBank = beneficiaryBank;

  }

  public String getClaimAmount()

  {

    return this.claimAmount;

  }

  public void setClaimAmount(String claimAmount)

  {

    this.claimAmount = claimAmount;

  }

  public String getClaimCurrency()

  {

    return this.claimCurrency;

  }

  public void setClaimCurrency(String claimCurrency)

  {

    this.claimCurrency = claimCurrency;

  }

  public String getReceiverSwiftAddress()

  {

    return this.receiverSwiftAddress;

  }

  public void setReceiverSwiftAddress(String receiverSwiftAddress)

  {

    this.receiverSwiftAddress = receiverSwiftAddress;

  }

  public String getReimbursingBank()

  {

    return this.reimbursingBank;

  }

  public void setReimbursingBank(String reimbursingBank)

  {

    this.reimbursingBank = reimbursingBank;

  }

  public String getRelatedReference()

  {

    return this.relatedReference;

  }

  public void setRelatedReference(String relatedReference)

  {

    this.relatedReference = relatedReference;

  }

  public String getSenderReference()

  {

    return this.senderReference;

  }

  public void setSenderReference(String senderReference)

  {

    this.senderReference = senderReference;

  }

  public String getSenderSwiftAddress()

  {

    return this.senderSwiftAddress;

  }

  public void setSenderSwiftAddress(String senderSwiftAddress)

  {

    this.senderSwiftAddress = senderSwiftAddress;

  }

  public String getSenderToReceiverInformation()

  {

    return this.senderToReceiverInformation;

  }

  public void setSenderToReceiverInformation(String senderToReceiverInformation)

  {

    this.senderToReceiverInformation = senderToReceiverInformation;

  }

  public String getTotalAmountClaimed()

  {

    return this.totalAmountClaimed;

  }

  public void setTotalAmountClaimed(String totalAmountClaimed)

  {

    this.totalAmountClaimed = totalAmountClaimed;

  }

  public String getTotalAmountClaimedCurrency()

  {

    return this.totalAmountClaimedCurrency;

  }

  public void setTotalAmountClaimedCurrency(String totalAmountClaimedCurrency)

  {

    this.totalAmountClaimedCurrency = totalAmountClaimedCurrency;

  }

}