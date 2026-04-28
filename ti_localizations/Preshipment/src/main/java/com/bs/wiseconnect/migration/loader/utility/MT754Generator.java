package com.bs.wiseconnect.migration.loader.utility;

import com.bs.wiseconnect.migration.loader.tiplus.pojos.MT754Model;
import com.prowidesoftware.swift.io.parser.SwiftParser;
import com.prowidesoftware.swift.model.SwiftBlock2;
import com.prowidesoftware.swift.model.SwiftMessage;
import com.prowidesoftware.swift.model.field.Field20;
import com.prowidesoftware.swift.model.field.Field21;
import com.prowidesoftware.swift.model.field.Field32A;
import com.prowidesoftware.swift.model.field.Field32B;
import com.prowidesoftware.swift.model.field.Field33B;
import com.prowidesoftware.swift.model.field.Field34A;
import com.prowidesoftware.swift.model.field.Field34B;
import com.prowidesoftware.swift.model.field.Field53D;
import com.prowidesoftware.swift.model.field.Field57D;
import com.prowidesoftware.swift.model.field.Field58D;
import com.prowidesoftware.swift.model.mt.mt7xx.MT754;
import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MT754Generator
{
  public static String getILCCLMSMT754Message(MT754Model mT754Model)
  {
    String finalMT754Message = "";
   
    MT754 m = new MT754(new SwiftMessage(true));
   
    m.setSender(mT754Model.getSenderSwiftAddress());
    if ((mT754Model.getReceiverSwiftAddress() != null) &&
      (!mT754Model.getReceiverSwiftAddress().equals(""))) {
      m.setReceiver(mT754Model.getReceiverSwiftAddress());
    }
    m.getSwiftMessage().getBlock2().setMessageType("754");
   
    m.addField(new Field20(mT754Model.getSenderReference()));
    m.addField(new Field21(mT754Model.getRelatedReference()));
    if ((mT754Model.getClaimAmount() != null) &&
      (!mT754Model.getClaimAmount().equals("")))
    {
      Field32A f32A = new Field32A();
     
      f32A.setDate(convertSwiftDate(mT754Model.getValueDate()));
     
      f32A.setCurrency(mT754Model.getClaimCurrency());
     
      f32A.setAmount(mT754Model.getClaimAmount());
     
      m.addField(f32A);
    }
    if ((mT754Model.getAdditionalAmount() != null) &&
      (!mT754Model.getAdditionalAmount().equals("")))
    {
      Field33B f33B = new Field33B();
     
      f33B.setCurrency(mT754Model.getAdditionalAmountCurrency());
     
      f33B.setAmount(mT754Model.getAdditionalAmount());
     
      m.addField(f33B);
    }
    if ((mT754Model.getTotalAmountClaimed() != null) &&
      (!mT754Model.getTotalAmountClaimed().equals("")))
    {
      Field34A f34A = new Field34A();
     
      f34A.setDate(convertSwiftDate(mT754Model.getValueDate()));
     
      f34A.setCurrency(mT754Model.getTotalAmountClaimedCurrency());
     
      f34A.setAmount(mT754Model.getTotalAmountClaimed());
     
      m.addField(f34A);
    }
    if ((mT754Model.getAccountWithBank() != null) &&
      (!mT754Model.getAccountWithBank().equals(""))) {
      CommonUtils.get57tagFromIdentification(mT754Model.getAccountWithBank(), m);
    }
    if ((mT754Model.getBeneficiaryBank() != null) &&
      (!mT754Model.getBeneficiaryBank().equals(""))) {
      CommonUtils.get58tagFromIdentification(mT754Model.getBeneficiaryBank(), m);
    }
    if ((mT754Model.getReimbursingBank() != null) &&
      (!mT754Model.getReimbursingBank().equals(""))) {
      m.addField(new Field53D(mT754Model.getReimbursingBank()));
    }
    finalMT754Message = m.FIN();
   
    System.out.println("Before Converting -> " + finalMT754Message);
    finalMT754Message = finalMT754Message.replaceAll("2:I754", "2:O7541718" +
      MigrationUtil.generateRandom(6));
   
    finalMT754Message = finalMT754Message
      .replaceAll("N\\}\\{4:",
      "X8009695071409091" + MigrationUtil.generateRandom(3) +
      "N}{4:");
   
    System.out.println("After Converting -> " + finalMT754Message);
   
    return finalMT754Message;
  }
 
  public static String getELCDOCPRNMT754Message(MT754Model mT754Model)
  {
    String finalMT754Message = "";
   
    MT754 m = new MT754(new SwiftMessage(true));
   
    m.setSender(mT754Model.getSenderSwiftAddress());
   
    m.getSwiftMessage().getBlock2().setMessageType("754");
    if ((mT754Model.getReceiverSwiftAddress() != null) &&
      (!mT754Model.getReceiverSwiftAddress().equals(""))) {
      m.setReceiver(mT754Model.getReceiverSwiftAddress());
    }
    m.addField(new Field20(mT754Model.getSenderReference()));
    m.addField(new Field21(mT754Model.getRelatedReference()));
    if ((mT754Model.getClaimAmount() != null) &&
      (!mT754Model.getClaimAmount().equals("")))
    {
      Field32A f32A = new Field32A();
     
      f32A.setDate(convertSwiftDate(mT754Model.getValueDate()));
     
      f32A.setCurrency(mT754Model.getClaimCurrency());
     
      f32A.setAmount(mT754Model.getClaimAmount());
     
      m.addField(f32A);
    }
    if ((mT754Model.getAdditionalAmount() != null) &&
      (!mT754Model.getAdditionalAmount().equals("")))
    {
      Field33B f33B = new Field33B();
     
      f33B.setCurrency(mT754Model.getAdditionalAmountCurrency());
     
      f33B.setAmount(mT754Model.getAdditionalAmount());
     
      m.addField(f33B);
    }
    if ((mT754Model.getTotalAmountClaimed() != null) &&
      (!mT754Model.getTotalAmountClaimed().equals("")))
    {
      Field34A f34A = new Field34A();
     
      f34A.setDate(convertSwiftDate(mT754Model.getValueDate()));
     
      f34A.setCurrency(mT754Model.getTotalAmountClaimedCurrency());
     
      f34A.setAmount(mT754Model.getTotalAmountClaimed());
     
      m.addField(f34A);
    }
    if ((mT754Model.getAccountWithBank() != null) &&
      (!mT754Model.getAccountWithBank().equals(""))) {
      m.addField(new Field57D(mT754Model.getAccountWithBank()));
    }
    if ((mT754Model.getBeneficiaryBank() != null) &&
      (!mT754Model.getBeneficiaryBank().equals(""))) {
      m.addField(new Field58D(mT754Model.getBeneficiaryBank()));
    }
    if ((mT754Model.getReimbursingBank() != null) &&
      (!mT754Model.getReimbursingBank().equals(""))) {
      m.addField(new Field53D(mT754Model.getReimbursingBank()));
    }
    finalMT754Message = m.FIN();
   
    System.out.println("Before Converting -> " + finalMT754Message);
    finalMT754Message = finalMT754Message.replaceAll("2:I754", "2:O7541718" +
      MigrationUtil.generateRandom(6));
    finalMT754Message = finalMT754Message
      .replaceAll("N\\}\\{4:",
      "X800969507140909" + MigrationUtil.generateRandom(4) +
      "N}{4:");
   
    System.out.println("After Converting -> " + finalMT754Message);
   
    return finalMT754Message;
  }
 
  public static String getGteeCLMSMT754Message(MT754Model mT754Model)
  {
    String finalMT754Message = "";
   
    MT754 m = new MT754(new SwiftMessage(true));
   
    m.setSender(mT754Model.getSenderSwiftAddress());
    if ((mT754Model.getReceiverSwiftAddress() != null) &&
      (!mT754Model.getReceiverSwiftAddress().equals(""))) {
      m.setReceiver(mT754Model.getReceiverSwiftAddress());
    }
    m.getSwiftMessage().getBlock2().setMessageType("754");
   
    m.addField(new Field20(mT754Model.getSenderReference()));
    m.addField(new Field21(mT754Model.getRelatedReference()));
    if ((mT754Model.getClaimAmount() != null) &&
      (!mT754Model.getClaimAmount().equals("")))
    {
      Field32B f32B = new Field32B();
     
      f32B.setCurrency(mT754Model.getClaimCurrency());
     
      f32B.setAmount(mT754Model.getClaimAmount());
     
      m.addField(f32B);
    }
    if ((mT754Model.getAdditionalAmount() != null) &&
      (!mT754Model.getAdditionalAmount().equals("")))
    {
      Field33B f33B = new Field33B();
     
      f33B.setCurrency(mT754Model.getAdditionalAmountCurrency());
     
      f33B.setAmount(mT754Model.getAdditionalAmount());
     
      m.addField(f33B);
    }
    if ((mT754Model.getTotalAmountClaimed() != null) &&
      (!mT754Model.getTotalAmountClaimed().equals("")))
    {
      Field34B f34B = new Field34B();
     
      f34B.setCurrency(mT754Model.getTotalAmountClaimedCurrency());
     
      f34B.setAmount(mT754Model.getTotalAmountClaimed());
     
      m.addField(f34B);
    }
    if ((mT754Model.getAccountWithBank() != null) &&
      (!mT754Model.getAccountWithBank().equals(""))) {
      CommonUtils.get57tagFromIdentification(mT754Model.getAccountWithBank(), m);
    }
    if ((mT754Model.getBeneficiaryBank() != null) &&
      (!mT754Model.getBeneficiaryBank().equals(""))) {
      m.addField(new Field58D(mT754Model.getBeneficiaryBank()));
    }
    if ((mT754Model.getReimbursingBank() != null) &&
      (!mT754Model.getReimbursingBank().equals(""))) {
      m.addField(new Field53D(mT754Model.getReimbursingBank()));
    }
    finalMT754Message = m.FIN();
   
    System.out.println("Before Converting -> " + finalMT754Message);
    finalMT754Message = finalMT754Message.replaceAll("2:I754", "2:O7541718" +
      MigrationUtil.generateRandom(6));
    finalMT754Message = finalMT754Message
      .replaceAll("N\\}\\{4:",
      "X8009695071409091" + MigrationUtil.generateRandom(3) +
      "N}{4:");
   
    System.out.println("After Converting -> " + finalMT754Message);
   
    return finalMT754Message;
  }
 
  public static Boolean validateSwiftMessage(String swiftMessage)
  {
    Boolean flag = null;
   
    SwiftParser parser = new SwiftParser(swiftMessage);
    try
    {
      SwiftMessage message = parser.message();
      flag = Boolean.valueOf(message.isCOV());
    }
    catch (IOException ex)
    {
      Logger.getLogger(MT754Generator.class.getName()).log(Level.SEVERE,
        null, ex);
    }
    return flag;
  }
 
  public static String convertSwiftDate(String date)
  {
    date = CommonUtils.getStringDateInFormat(date, "yyyymmdd", "yymmdd");
   
    return date;
  }
 
  public static void main(String[] args)
  {
    System.out.println("Date Converted ->" + convertSwiftDate(null));
  }
}