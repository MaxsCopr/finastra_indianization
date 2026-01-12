package com.misys.tiplus2.customisation.pane;

import java.math.BigDecimal;
//com.misys.tiplus2.customisation.pane.CutomPane
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

//import org.apache.log4j.Logger;

import com.bs.theme.transport.client.ThemeTransportClient;
import com.misys.tiplus2.apps.ti.kernel.extpm.entity.ExtEvent;
import com.misys.tiplus2.apps.ti.kernel.extpm.entity.Key_ExtEvent;
import com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP;
import com.misys.tiplus2.customisation.entity.ExtEventBUYSCREDITEDIT;
import com.misys.tiplus2.customisation.entity.ExtEventBUYSCREDITMASTER;
import com.misys.tiplus2.customisation.entity.ExtEventBUYSCREDITMASTEREntityWrapper;
import com.misys.tiplus2.customisation.entity.ExtEventBillReferenceEntityWrapper;
import com.misys.tiplus2.customisation.entity.ExtEventCounterGuranteeEntityWrapper;
import com.misys.tiplus2.customisation.entity.ExtEventLienMarkingEntityWrapper;
import com.misys.tiplus2.customisation.entity.ExtEventPreshipmentEntityWrapper;
import com.misys.tiplus2.customisation.entity.ExtEventServiceTax;
import com.misys.tiplus2.customisation.entity.ExtEventServiceTaxEntityWrapper;
import com.misys.tiplus2.customisation.extension.ConnectionMaster;
import com.misys.tiplus2.customisation.extension.OdcFEC;
import com.misys.tiplus2.enigma.customisation.AdhocQuery;
import com.misys.tiplus2.enigma.customisation.control.ExtendedButtonControlWrapper;
import com.misys.tiplus2.enigma.customisation.control.ExtendedCheckBoxControlWrapper;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails;
import com.misys.tiplus2.enigma.customisation.validation.ValidationTexts;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails.WarningType;
import com.misys.tiplus2.enigma.lang.control.EnigmaControl;
import com.misys.tiplus2.enigma.lang.datatype.EnigmaArray;
import com.misys.tiplus2.foundations.lang.logging.Loggers;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.misys.tiplus2.foundations.lang.logging.Loggers;

public class CutomPane extends EventPane {
      // private static final Logger logger =
      // Logger.getLogger(ConnectionMaster.class.getName());
      private static final Logger LOG = LoggerFactory.getLogger(CutomPane.class);
      Connection con, con1 = null;
      PreparedStatement peco, ps1, ps, dmsp, ttRatePS, psd = null;
      ResultSet dmsr1, rs1, rs, dmsr, ttRateRS, rst = null;

      // ConnectionMaster cm = new ConnectionMaster();
      public static String randomCorrelationId() {
            // Loggers.general().info(LOG,"randomCorrelationId generate");
            return UUID.randomUUID().toString();
      }

      public void onESTAMP_FlagIMPGUAAMDclay_2_2AutoSubmit() {

            System.out.println("Entering Stamp Duty Click");

            ExtendedCheckBoxControlWrapper ebgstamp = this.getCtlESTAMP_Flag();

            if (ebgstamp.getValue()) {

                  ExtendedButtonControlWrapper stampDutyCalButton = this.getBtnSTAMPIMPGUAAMDclay_2_2();
                  stampDutyCalButton.setEnabled(true);

                  this.getCtlEBGSTAMPDUTY().setEnabled(true);
                  onSTAMPIMPGUAAMDclay_2_2Button();

            } else {
                  ExtendedButtonControlWrapper stampDutyCalButton = this.getBtnSTAMPIMPGUAAMDclay_2_2();
                  stampDutyCalButton.setEnabled(false);

                  this.setEBGSTAMPDUTY("0.00 INR");
                  this.getCtlEBGSTAMPDUTY().setEnabled(false);

            }

      }

      public void onEBGACCEPIMPGUAAMDclay_2_2AutoSubmit() {

            System.out.println("Entering EBG Accept API");

            String strService = "EBGAcceptRejectAPI";
            String url = "";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strService + "'");
            EXTGENCUSTPROP CodeURL = queryLog.getUnique();
            if (CodeURL != null) {

                  url = CodeURL.getPropval();
            } else {

            }

            String response = null;
            // String url = "http://10.128.232.200:8092/wiseconnect/ebg/v1/event-response";
            String masRefNo = getDriverWrapper().getEventFieldAsText("MST", "r", "");
            String eventRefNo = getDriverWrapper().getEventFieldAsText("EVR", "r", "");

            String request = "{\n" + "    \"txn_id\": \"" + masRefNo.concat(eventRefNo) + "\",\n" + " \"ebg_ref_no\": \""
                        + masRefNo + "\",\n" + "      \"event\": \"Accepted\",\n" + "     \"rmrks\": \"Accept\"\n}";

            StringBuffer buffer = new StringBuffer();
            PostMethod post = new PostMethod(url);

            try {
                  System.out.println("EBG Accept API Requst: " + request);
                  StringRequestEntity requestEntity = new StringRequestEntity(request, "application/json", "utf-8");
                  post.setRequestEntity(requestEntity);
                  HttpClient httpclient = new HttpClient();

                  int result = httpclient.executeMethod(post);

                  if (result != 200) {
                        throw new Exception(" EBG Accept API Error " + result);
                  }
                  response = post.getResponseBodyAsString();
                  System.out.println(" EBG Accept API Response From Bank-->\n" + response);

                  System.out.println("Exiting  EBG Accept API");
            } catch (Exception e) {
                  System.out.println("Exception in  EBG Accept API:- " + e);
                  e.printStackTrace();
            } finally {
                  post.releaseConnection();
            }
      }

      public void onEBGREJECIMPGUAAMDclay_2_2AutoSubmit() {

            System.out.println("Entering EBG Reject API");

            String strService = "EBGAcceptRejectAPI";
            String url = "";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strService + "'");
            EXTGENCUSTPROP CodeURL = queryLog.getUnique();
            if (CodeURL != null) {

                  url = CodeURL.getPropval();
            } else {

            }

            String response = null;
            // String url = "http://10.128.232.200:8092/wiseconnect/ebg/v1/event-response";
            String masRefNo = getDriverWrapper().getEventFieldAsText("MST", "r", "");
            String eventRefNo = getDriverWrapper().getEventFieldAsText("EVR", "r", "");

            String request = "{\n" + "    \"txn_id\": \"" + masRefNo.concat(eventRefNo) + "\",\n" + " \"ebg_ref_no\": \""
                        + masRefNo + "\",\n" + "      \"event\": \"Rejected\",\n" + "     \"rmrks\": \"Reject\"\n}";

            StringBuffer buffer = new StringBuffer();
            PostMethod post = new PostMethod(url);

            try {
                  System.out.println("EBG Reject API Requst: " + request);
                  StringRequestEntity requestEntity = new StringRequestEntity(request, "application/json", "utf-8");
                  post.setRequestEntity(requestEntity);
                  HttpClient httpclient = new HttpClient();

                  int result = httpclient.executeMethod(post);

                  if (result != 200) {
                        throw new Exception(" EBG Reject API Error " + result);
                  }
                  response = post.getResponseBodyAsString();
                  System.out.println(" EBG Reject API Response From Bank-->\n" + response);

                  System.out.println("Exiting  EBG Reject API");
            } catch (Exception e) {
                  System.out.println("Exception in  EBG Reject API:- " + e);
                  e.printStackTrace();
            } finally {
                  post.releaseConnection();
            }
      }

      public void onSTATECODIMPGUAAMDclay_2_2AutoSubmit() {

            System.out.println("Entering Stamp Duty");

            ExtendedCheckBoxControlWrapper ebgstamp = this.getCtlESTAMP_Flag();

            if (ebgstamp.getValue()) {

                  try {

                        // String stateCode = this.getSTATECOD();
                        // String stateCode = getDriverWrapper().getEventFieldAsText("cBDS", "s", "");
                        String stateCode = this.getCtlSTATECOD().getValue();
                        System.out.println("Stamp Duty  --> StateCode " + stateCode.trim());
                        if (!stateCode.isEmpty()) {
                              double stampDuty = getStateWiseStampDuty(stateCode);
                              this.setEBGSTAMPDUTY(stampDuty + " " + "INR");

                              System.out.println("The state code is ::: " + stateCode + " Stamp Amount is ::: " + stampDuty);
                              System.out.println("Existing Stamp Duty");
                        } else {
                              System.out.println("The state code is empty");
                        }
                  } catch (Exception e) {
                        System.out.println("Stamp Duty exception " + e.getMessage());
                        e.getStackTrace();
                  }
            }

      }

      public Double getStateWiseStampDuty(String stateCode) {

            double stampValue = 0.00;
            con = ConnectionMaster.getConnection();
            try {
                  String eventRef = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
                  System.out.println("eventRef-> " + eventRef);
                  String fullamt = getDriverWrapper().getEventFieldAsText("FOA", "v", "m");

                  System.out.println("fullamt-> " + fullamt);

                  String query = "SELECT COALESCE(STAMPDUTY,'Empty') AS STAMPDUTY  from EXTSTATE WHERE STATCOD = '"
                              + stateCode.trim() + "'";
                  System.out.println("query :: -> " + query);
                  System.out.println("connection::: ->");
                  ps1 = con.prepareStatement(query);
                  rs1 = ps1.executeQuery();
                  System.out.println("rs1 ->" + rs1);
                  while (rs1.next()) {

                        String StampDuty = rs1.getString(1);

                        System.out.println("StampDuty :::: " + StampDuty);
                        if (StampDuty.equalsIgnoreCase("Empty")) {

                              stampValue = getStampDutyAmount(stateCode, StampDuty, fullamt, eventRef);
                              System.out.println("stampValue ::  " + stampValue);

                        } else {
                              stampValue = Double.parseDouble(StampDuty);
                              System.out.println("stampValue ::  " + stampValue);
                        }

                  }
            } catch (Exception e) {
                  e.printStackTrace();
                  System.out.println("error::  " + e);
            } finally {
                  ConnectionMaster.surrenderDB(con, ps1, rs1);

                  System.out.println("inside Finally block");
            }

            return stampValue;
      }

      public static Double getStampDutyAmount(String stateCode, String StampDuty, String amount, String eventRef) {
            System.out.println("getStampDutyAmount");
            double stampDuty = 0.00;

            if (StampDuty.equalsIgnoreCase("Empty")) {
                  if (stateCode.trim().equalsIgnoreCase("MP")) {
                        double percentage = 0.25;
                        double findStampDuty = findStampDuty(amount, percentage);
                        System.out.println("inside MP for testing -->> " + findStampDuty);
                        if (findStampDuty >= 25000) {
                              stampDuty = 25000.00;
                        } else {
                              stampDuty = findStampDuty;
                        }

                  } else if (stateCode.trim().equalsIgnoreCase("RJ")) {
                        if (eventRef.substring(0, 3).equalsIgnoreCase("ISS")) {
                              double percentage = 0.25;
                              System.out.println("AMOUNT CUSTOMPANE=" +amount);
                              double findStampDuty = findStampDuty(amount, percentage);
                              System.out.println("Value OF findStampDuty in ISS RJ=" +findStampDuty);
                              if (findStampDuty >= 25000) {
                                    stampDuty = 25000.00;
                              } else {
                                    stampDuty = findStampDuty;
                              }

                        } else if (eventRef.substring(0, 3).equalsIgnoreCase("AMD")) {
                              double percentage = 0.25;
                              System.out.println("AMOUNT CUSTOMPANE=" +amount);
                              double findStampDuty = findStampDuty(amount, percentage);
                              System.out.println("Value OF findStampDuty in AMD RJ=" +findStampDuty);
                              if (findStampDuty >= 1000) {
                                    stampDuty = 1000.00;
                              } else {
                                    stampDuty = findStampDuty;
                              }

                        }

                        System.out.println("BEFORE StampDuty=" +stampDuty);
                        double surcharge = Math.ceil(stampDuty*0.1);
                        System.out.println("StampDuty=" +stampDuty);
                        System.out.println("Value of surcharge=" +surcharge);
                        surcharge = surcharge*3;
                        System.out.println("Value of surcharge after multiplying by 3=" +surcharge);
                        stampDuty = stampDuty + surcharge;
                        System.out.println("Final stampDuty of RJ" +stampDuty);
                        
                  } else if (stateCode.trim().equalsIgnoreCase("UP")) {
                        double percentage = 0.5;
                        double findStampDuty = findStampDuty(amount, percentage);
                        if (findStampDuty >= 10000) {
                              stampDuty = 10000.00;
                        } else {
                              stampDuty = findStampDuty;
                        }

                  }else if (stateCode.trim().equalsIgnoreCase("UK")) {
                        double percentage = 0.5;
                        System.out.println("AMOUNT CUSTOMPANE=" +amount);
                        double findStampDuty = findStampDuty(amount, percentage);
                        if (findStampDuty >= 10000) {
                              System.out.println("IF Value OF findStampDuty in UK=" +findStampDuty);
                              stampDuty = 10000.00;
                        } else {
                              System.out.println("ELSE Value OF findStampDuty in UK=" +findStampDuty);
                              stampDuty = findStampDuty;
                        }

                  } else if (stateCode.trim().equalsIgnoreCase("MH")) {

                        double baseAmount = Double.valueOf(amount);

                        if (baseAmount < 1000000) {

                              double percentage = 0.1;
                              double findStampDuty = findStampDuty(amount, percentage);
                              if (findStampDuty <= 100) {
                                    stampDuty = 100.00;
                              } else {
                                    stampDuty = findStampDuty;
                              }

                        } else {
                              double percentage = 0.2;
                              double findStampDuty = findStampDuty(amount, percentage);
                              stampDuty = findStampDuty;
                        }

                  }

            }
            return stampDuty;

      }

      public static Double findStampDuty(String amount, double percentage) {

            System.out.println("findStampDuty :: " + amount + " percentage :: " + percentage);

            double baseValue = Double.valueOf(amount);
            System.out.println(" baseValue :: " + baseValue);
            double result = Math.ceil(((percentage / 100) * baseValue));
            result = Double.parseDouble(String.format("%.2f", result));
            System.out.println("0.25% of " + baseValue + " is: " + result);

            System.out.println("ends findStampDuty" + result);
            return result;
      }

      @Override
      public void onSTAMPIMPGUAAMDclay_2_2Button() {

            System.out.println("Entering Stamp Duty");

            try {
                  // String stateCode = this.getSTATECOD();
                  String stateCode = this.getCtlSTATECOD().getValue();
                  System.out.println("Stamp Duty  --> StateCode " + stateCode.trim());
                  // String stateCode = getDriverWrapper().getEventFieldAsText("cBDS", "s", "");
                  System.out.println("Stamp Duty  --> StateCode " + stateCode.trim());
                  if (!stateCode.isEmpty()) {
                        double stampDuty = getStateWiseStampDuty(stateCode);
                        this.setEBGSTAMPDUTY(stampDuty + " " + "INR");

                        System.out.println("The state code is ::: " + stateCode + " Stamp Amount is ::: " + stampDuty);
                        System.out.println("Existing Stamp Duty");
                  } else {
                        System.out.println("The state code is empty");
                  }
            } catch (Exception e) {
                  System.out.println("Stamp Duty exception " + e.getMessage());
                  e.getStackTrace();
            }

      }

      public void onMULBUYERIMPGUAAMDclayButton() {

            // Loggers.general().info(LOG,"onMULBUYERIMPGUAAMDclayButton");
            if (MULBUYER()) {
                  // Loggers.general().info(LOG,"MULBUYER calling");
            } else {
                  // Loggers.general().info(LOG,"MULBUYER Else systemOutput");
            }

      }

      public void onMULBUYERIMPGUAADJclayButton() {

            // Loggers.general().info(LOG,"onMULBUYERIMPGUAADJclayButton");
            if (MULBUYER()) {
                  // Loggers.general().info(LOG,"MULBUYER calling");
            } else {
                  // Loggers.general().info(LOG,"MULBUYER Else systemOutput");
            }

      }

      public boolean MULBUYER() {
            boolean value = false;

            String strLog = "Log";
            String dailyval_Log = "";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
            EXTGENCUSTPROP CodeLog = queryLog.getUnique();
            if (CodeLog != null) {

                  dailyval_Log = CodeLog.getPropval();
            } else {
                  // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }

            try {

                  // Getting the Table Structure and Size
                  con = ConnectionMaster.getConnection();
                  EnigmaArray<ExtEventBillReferenceEntityWrapper> list = getExtEventBillReferenceData();
                  for (int i = 0; i < list.getSize().intValue(); i++) {
                        Iterator<ExtEventBillReferenceEntityWrapper> iterator1 = list.iterator();
                        // Getting the table Values
                        while (iterator1.hasNext()) {
                              ExtEventBillReferenceEntityWrapper fdwrapper = (ExtEventBillReferenceEntityWrapper) iterator1
                                          .next();
                              String billNumber = fdwrapper.getBILLNUMB().trim();
                              // billNumbeString billno = getBUYBILLR();
                              String str = billNumber.substring(4, 7);
                              if (!str.equalsIgnoreCase("ICF")) {
                                    // Loggers.general().info(LOG,"Multiple Buyer bill reference no"
                                    // + billNumber);
                                    String query = "select DISTINCT TRIM(exte.SHIPFMLC),TRIM(exte.SHIPTOLC),TRIM(to_char(exte.DASHIP,'DD/MM/YY')) AS DATESHIP,TRIM(exte.PORTCOD) AS PORTCOD,TRIM(exte.PORTDESC) AS PORTDESC,GOODCAT from EXTEVENT exte where BLLREFNO ='"
                                                + billNumber + "'";

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG, "Multiple Buyer bill reference no query" + query);

                                    }
                                    // Loggers.general().info(LOG,"Multiple Buyer bill reference no
                                    // query" + query);

                                    psd = con.prepareStatement(query);
                                    rst = psd.executeQuery();
                                    while (rst.next()) {
                                          // Loggers.general().info(LOG,"Enter into while --->");
                                          String shipment_from = rst.getString(1);
                                          String shipment_to = rst.getString(2);
                                          String shipment_date = rst.getString(3);
                                          String shipment_port = rst.getString(4);
                                          String shipment_portdesc = rst.getString(5);
                                          String shipment_goods = rst.getString(6);
                                          fdwrapper.setSHIFMCN(shipment_from);
                                          fdwrapper.setSHTOCON(shipment_to);
                                          fdwrapper.setSHPDA(shipment_date);
                                          fdwrapper.setPCODE(shipment_port);
                                          fdwrapper.setPCODES(shipment_portdesc);
                                          fdwrapper.setBUYGOODS(shipment_goods);
                                    }

                              } else {
                                    // Loggers.general().info(LOG,"multiple Buyer master reference
                                    // no" + billNumber);
                                    String query = "SELECT TRIM(exte.SHIPFTO) AS SHIPFTO, TRIM(exte.SHIPTOP) AS SHIPTOP, TRIM(TO_CHAR(exte.DASHIP,'DD/MM/YY')) AS DATESHIP, TRIM(exte.PORTCOD) AS PORTCOD, TRIM(exte.PORTDESC) AS PORTDESC, usop.NAME FROM master mas, BASEEVENT bev, USEROPTN usop, EXTEVENT exte, PRODTYPE prod WHERE mas.KEY97 = bev.MASTER_KEY AND bev.KEY97 = exte.EVENT AND mas.PRODTYPE = prod.KEY97 and mas.USEROPTN1 = usop.KEY29 AND bev.REFNO_PFIX = 'CRE' AND prod.CODE ='ICF' AND mas.MASTER_REF ='"
                                                + billNumber + "'";
                                    // Loggers.general().info(LOG,"Multiple Buyer master reference
                                    // no query" + query);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG, "Multiple Buyer master reference no query" + query);

                                    }
                                    psd = con.prepareStatement(query);
                                    rst = psd.executeQuery();
                                    while (rst.next()) {
                                          String shipment_from = rst.getString(1);
                                          String shipment_to = rst.getString(2);
                                          String shipment_date = rst.getString(3);
                                          String shipment_port = rst.getString(4);
                                          String shipment_portdesc = rst.getString(5);
                                          String shipment_goods = rst.getString(6);
                                          fdwrapper.setSHIFMCN(shipment_from);
                                          fdwrapper.setSHTOCON(shipment_to);
                                          fdwrapper.setSHPDA(shipment_date);
                                          fdwrapper.setPCODE(shipment_port);
                                          fdwrapper.setPCODES(shipment_portdesc);
                                          fdwrapper.setBUYGOODS(shipment_goods);
                                    }

                              }
                        }
                  }
            } catch (Exception e) {

                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG, "Exception is Multiple Buyer bill reference" + e.getMessage());

                  }
            } finally {
                  try {
                        if (rst != null)
                              rst.close();
                        if (psd != null)
                              psd.close();
                        if (con != null)
                              con.close();

                  } catch (SQLException e) {
                        // Loggers.general().info(LOG,"Connection Failed! Check output
                        // console");
                        e.printStackTrace();
                  }
            }

            return value;
      }

      public void onFETCHIFSCIMPGUAAMDclayButton() {
            if (FETCHIFSC()) {
                  // Loggers.general().info(LOG,"Sender ifsc calling IGT ADJ");
            }
            // FETCHIFSCIncoming();
      }

      public void onFETCHIFSCIMPGUAADJclayButton() {
            if (FETCHIFSC()) {
                  // Loggers.general().info(LOG,"Sender ifsc calling IGT AMD");
            }
            // FETCHIFSCIncoming();
      }

      // public boolean FETCHIFSCIncoming() {
      // boolean value = false;
      // String masterRefNumber = getDriverWrapper().getEventFieldAsText("THE",
      // "r", "").trim();
      // Connection con = null;
      // PreparedStatement ps1 = null;
      // ResultSet rs1 = null;
      // try {
      // // Code changed by Sreedhar
      // String query = "select TRIM(SENDERIFSC),TRIM(RECEIVERIFSC) from
      // EXTSFMSCUSTMAP where MASTERREFERENCE ='"
      // + masterRefNumber + "' and SENDERIFSC is not null and RECEIVERIFSC is not
      // null";
      //
      // // Loggers.general().info(LOG,"Sender ifsc code=====>" + query);
      // con = ConnectionMaster.getConnection();
      // ps1 = con.prepareStatement(query);
      // rs1 = ps1.executeQuery();
      //
      // while (rs1.next()) {
      // String senifsc = rs1.getString(1);
      // // Loggers.general().info(LOG,"Sender ifsc code=====>" + senifsc);
      // String recifsc = rs1.getString(2);
      // // Loggers.general().info(LOG,"Sender ifsc code=====>" + recifsc);
      // setSNIFSCIN(senifsc);
      // //setRCIFSCIN(recifsc);
      // }
      // } catch (Exception e) {
      // // Loggers.general().info(LOG,"Exeception of incoming recifsc " +
      // // e.getMessage());
      //
      // } finally {
      //
      // try {
      // if (con != null) {
      // con.close();
      // if (ps1 != null)
      // ps1.close();
      // if (rs1 != null)
      // rs1.close();
      // }
      // } catch (SQLException e) {
      // // Loggers.general().info(LOG,"Connection Failed! Check output
      // // console");
      // e.printStackTrace();
      // }
      //
      // }
      //
      // return value;
      // }

      public boolean FETCHIFSC() {
            boolean value = false;
            String strLog = "Log";
            String dailyval_Log = "";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
            EXTGENCUSTPROP CodeLog = queryLog.getUnique();
            if (CodeLog != null) {

                  dailyval_Log = CodeLog.getPropval();
            } else {
                  // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }

            try {

                  String subProductType = "";
                  String behalfHalfBranch = getDriverWrapper().getEventFieldAsText("BOB", "s", "");
                  String amountCurrency = getDriverWrapper().getEventFieldAsText("ORA", "v", "c");
                  if (getMajorCode().equalsIgnoreCase("IGT")) {
                        subProductType = getDriverWrapper().getEventFieldAsText("PUL2", "s", "");
                  } else if (getMajorCode().equalsIgnoreCase("EGT")) {
                        subProductType = getDriverWrapper().getEventFieldAsText("PUL1", "s", "");
                  }
                  String query = "";
                  String senderIfsc = "";
                  if (subProductType.equalsIgnoreCase("Inland") && amountCurrency.equalsIgnoreCase("INR")) {
                        query = "select trim(IFSC) from extbramas where BCODE='" + behalfHalfBranch + "' ";
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG, "IFSC fetch code" + query);

                        }
                        con = ConnectionMaster.getConnection();
                        ps1 = con.prepareStatement(query);
                        rs1 = ps1.executeQuery();
                        while (rs1.next()) {
                              senderIfsc = rs1.getString(1);
                        }
                        // Loggers.general().info(LOG,"Sender IFSC code----->" + senderIfsc);
                        setSENIFSC(senderIfsc);
                  }
            } catch (Exception e) {
                  // Loggers.general().info(LOG,"Exeception of recifsc " + e.getMessage());
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG, "Exeception of recifsc " + e.getMessage());

                  }

            } finally {

                  try {
                        if (rs1 != null)
                              rs1.close();
                        if (ps1 != null)
                              ps1.close();
                        if (con != null)
                              con.close();

                  } catch (SQLException e) {
                        // Loggers.general().info(LOG,"Connection Failed! Check output
                        // console");
                        e.printStackTrace();
                  }

            }
            return value;
      }

      public void oncalcmarginIMPGUAAMDclayButton() {

            String strLog = "Log";
            String dailyval_Log = "";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
            EXTGENCUSTPROP CodeLog = queryLog.getUnique();
            if (CodeLog != null) {

                  dailyval_Log = CodeLog.getPropval();
            } else {
                  // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }
            try {

                  double notionalRate = 0.0;
                  double permaradub = 0.0;
                  String facid = getFACLTYID().trim();
                  String permar = getPMARGIN();
                  String permaradd = getPADDMARG();
                  if (!facid.equalsIgnoreCase("")) {
                        if (permaradd == null || permaradd.isEmpty()) {
                              permaradd = "0";
                        }
                        permaradub = Double.valueOf(permaradd);
                        // String magn = "";
                        int magne = 0;
                        String ttRateString = "";
                        // Loggers.general().info(LOG,"Percentage margin ----" + permar);
                        String mastamt = getDriverWrapper().getEventFieldAsText("FOA", "v", "m");
                        // Loggers.general().info(LOG,"Master and tolerant amount--->" +
                        // mastamt);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG, "Master and tolerant amount--->" + mastamt);

                        }
                        if (mastamt.isEmpty() || mastamt == null) {
                              // Loggers.general().info(LOG,"Master and tolerant amount is
                              // empty--->" + mastamt);
                              mastamt = "0";
                        }
                        float amunt1 = Float.valueOf(mastamt);
                        // Loggers.general().info(LOG,"amount" + amunt1);
                        long amount01 = (long) amunt1;
                        // Loggers.general().info(LOG,"current Value ----" + amount01);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG, "current Value ----" + amount01);

                        }

                        String curr = getDriverWrapper().getEventFieldAsText("AMT", "v", "c").trim();
                        String customerNo = getDriverWrapper().getEventFieldAsText("APP", "p", "no");

                        // Loggers.general().info(LOG,"Currency " + curr);

                        con = ConnectionMaster.getConnection();
                        if (permar.equalsIgnoreCase("") || permar == null) {
                              String queryfac = "select margin from customermargin where facility='" + facid + "' and CIF ='"
                                          + customerNo + "'"; // Loggers.general().info(LOG,"queryfac
                                                                        // " + queryfac);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG, "query for customermargin" + queryfac);

                              }
                              dmsp = con.prepareStatement(queryfac);
                              // Loggers.general().info(LOG,"dmsp-----> ");
                              dmsr = dmsp.executeQuery();
                              // Loggers.general().info(LOG,"executeQuery-----> ");
                              Boolean facilityFound = false;
                              while (dmsr.next()) {
                                    // Loggers.general().info(LOG,"while margin-----> ");
                                    facilityFound = true;
                                    // Loggers.general().info(LOG,"magn--->");
                                    String magn = dmsr.getString(1).trim();
                                    // Loggers.general().info(LOG,"magn--->" + magn);
                                    magne = Integer.parseInt(magn);
                                    // Loggers.general().info(LOG,"Margin " + magn);
                                    // Loggers.general().info(LOG,"Adding amount---- " + (magne +
                                    // permaradub));

                                    if (curr.equalsIgnoreCase("INR")) {
                                          notionalRate = 1;
                                    } else {
                                          String ttRateQuery = "select trim(SPOTRATE) from spotrate where currency='" + curr + "'";
                                          // Loggers.general().info(LOG,"ttRateQuery - " +
                                          // ttRateQuery);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG, "Spotrate Query - " + ttRateQuery);

                                          }
                                          dmsp = con.prepareStatement(ttRateQuery);
                                          dmsr = dmsp.executeQuery();
                                          while (dmsr.next()) {
                                                String ttRate_String = dmsr.getString(1).trim();
                                                notionalRate = Double.parseDouble(ttRate_String);
                                          }
                                    }
                                    // Loggers.general().info(LOG,"notionalRate - " + notionalRate);
                                    long k = (long) (((amount01 * (magne + permaradub))) * notionalRate);
                                    String ks = String.valueOf(k);
                                    // Loggers.general().info(LOG,"Margin amount in final for set if
                                    // stmt " + ks + "" + "INR");
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG, "Margin amount in final for set if stmt " + ks + "" + "INR");

                                    }
                                    setPMARGIN(magn);
                                    // setNOTRATE(String.valueOf(notionalRate));
                                    setMARAMT(ks + " " + "INR");
                                    //// setPRMARAMT(ks + " " + "INR");
                              }
                              if (facilityFound == false) {
                                    setPMARGIN("");
                                    // setNOTRATE("");
                                    setMARAMT("");
                                    // setPRMARAMT("");
                              }
                        } else {
                              permar = getPMARGIN();
                              // Loggers.general().info(LOG,"permar value in string else--->" +
                              // permar);
                              float permarflo = Float.valueOf(permar);
                              long magndou = (long) permarflo;
                              // Loggers.general().info(LOG,"magndou value in long else--->" +
                              // magndou);
                              // Loggers.general().info(LOG,"float value in else--->" + magndou);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG, "float value in else--->" + magndou);

                              }

                              if (curr.equalsIgnoreCase("INR")) {
                                    notionalRate = 1;
                              } else {
                                    // Loggers.general().info(LOG,"margin lc in else");
                                    String ttRateQuery = "select trim(SPOTRATE) from spotrate where currency='" + curr + "'";
                                    // Loggers.general().info(LOG,"ttRateQuery else - " +
                                    // ttRateQuery);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG, "ttRateQuery else - " + ttRateQuery);

                                    }
                                    dmsp = con.prepareStatement(ttRateQuery);
                                    dmsr = dmsp.executeQuery();
                                    while (dmsr.next()) {
                                          String ttRate_String = dmsr.getString(1).trim();
                                          notionalRate = Double.parseDouble(ttRate_String);
                                    }
                              }
                              // Loggers.general().info(LOG,"Adding amount in else---- " +
                              // (magndou + permaradub));
                              long k = (long) (((amount01 * (magndou + permaradub))) * notionalRate);
                              // Loggers.general().info(LOG,"Expected margin amount in else---> -
                              // " + k);
                              String ks = String.valueOf(k);
                              // Loggers.general().info(LOG,"Margin amount in final for set else
                              // stmt " + ks + "" + "INR");
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG, "Margin amount in final for set else stmt " + ks + "" + "INR");
                              }
                              setPMARGIN(permar);
                              // setNOTRATE(String.valueOf(notionalRate));
                              setMARAMT(ks + " " + "INR");
                              //// setPRMARAMT(ks + " " + "INR");
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG, "Margin amount in final for set else stmt " + ks + "" + "INR");
                              }
                        }
                  } else {

                        // Loggers.general().info(LOG,"Facility id is blank");
                        setPMARGIN("");
                        // setNOTRATE("");
                        setMARAMT("");
                        setPADDMARG("");
                  }
            } catch (Exception e) {
                  // Loggers.general().info(LOG,"exception caught on margin button validation
                  // " + e.getMessage());
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG, "exception caught on margin button validation" + e.getMessage());
                  }

            } finally {
                  try {
                        if (dmsr != null)
                              dmsr.close();
                        if (dmsp != null)
                              dmsp.close();
                        if (con != null)
                              con.close();

                  } catch (SQLException e) {
                        // Loggers.general().info(LOG,"Connection Failed! Check output
                        // console");
                        e.printStackTrace();
                  }
            }
      }

      @Override
      public void oncalcmarginIMPGUAADJclayButton() {

            String strLog = "Log";
            String dailyval_Log = "";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
            EXTGENCUSTPROP CodeLog = queryLog.getUnique();
            if (CodeLog != null) {

                  dailyval_Log = CodeLog.getPropval();
            } else {
                  // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }
            try {

                  double notionalRate = 0.0;
                  double permaradub = 0.0;
                  String facid = getFACLTYID().trim();
                  String permar = getPMARGIN();
                  String permaradd = getPADDMARG();
                  if (!facid.equalsIgnoreCase("")) {
                        if (permaradd == null || permaradd.isEmpty()) {
                              permaradd = "0";
                        }
                        permaradub = Double.valueOf(permaradd);
                        // String magn = "";
                        int magne = 0;
                        String ttRateString = "";
                        // Loggers.general().info(LOG,"Percentage margin ----" + permar);
                        String mastamt = getDriverWrapper().getEventFieldAsText("FOA", "v", "m");
                        // Loggers.general().info(LOG,"Master and tolerant amount--->" +
                        // mastamt);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG, "Master and tolerant amount--->" + mastamt);
                        }
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG, "Master and tolerant amount--->" + mastamt);
                        }
                        if (mastamt.isEmpty() || mastamt == null) {
                              // Loggers.general().info(LOG,"Master and tolerant amount is
                              // empty--->" + mastamt);
                              mastamt = "0";
                        }
                        float amunt1 = Float.valueOf(mastamt);
                        // Loggers.general().info(LOG,"amount" + amunt1);
                        long amount01 = (long) amunt1;
                        // Loggers.general().info(LOG,"current Value ----" + amount01);
                        String curr = getDriverWrapper().getEventFieldAsText("AMT", "v", "c").trim();
                        String customerNo = getDriverWrapper().getEventFieldAsText("APP", "p", "no");

                        // Loggers.general().info(LOG,"Currency " + curr);

                        con = ConnectionMaster.getConnection();
                        if (permar.equalsIgnoreCase("") || permar == null) {
                              String queryfac = "select margin from customermargin where facility='" + facid + "' and CIF ='"
                                          + customerNo + "'"; // Loggers.general().info(LOG,"queryfac
                                                                        // " + queryfac);

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG, "queryfac value is" + queryfac);
                              }
                              dmsp = con.prepareStatement(queryfac);
                              // Loggers.general().info(LOG,"dmsp-----> ");
                              dmsr = dmsp.executeQuery();
                              // Loggers.general().info(LOG,"executeQuery-----> ");
                              Boolean facilityFound = false;
                              while (dmsr.next()) {
                                    // Loggers.general().info(LOG,"while margin-----> ");
                                    facilityFound = true;
                                    // Loggers.general().info(LOG,"magn--->");
                                    String magn = dmsr.getString(1).trim();
                                    // Loggers.general().info(LOG,"magn--->" + magn);
                                    magne = Integer.parseInt(magn);
                                    // Loggers.general().info(LOG,"Margin " + magn);
                                    // Loggers.general().info(LOG,"Adding amount---- " + (magne +
                                    // permaradub));
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG, "Adding amount---- " + (magne + permaradub));
                                    }

                                    if (curr.equalsIgnoreCase("INR")) {
                                          notionalRate = 1;
                                    } else {
                                          String ttRateQuery = "select trim(SPOTRATE) from spotrate where currency='" + curr + "'";
                                          // Loggers.general().info(LOG,"ttRateQuery - " +
                                          // ttRateQuery);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG, "ttRateQuery spotrate value - " + ttRateQuery);
                                          }
                                          dmsp = con.prepareStatement(ttRateQuery);
                                          dmsr = dmsp.executeQuery();
                                          while (dmsr.next()) {
                                                String ttRate_String = dmsr.getString(1).trim();
                                                notionalRate = Double.parseDouble(ttRate_String);
                                          }
                                    }
                                    // Loggers.general().info(LOG,"notionalRate - " + notionalRate);
                                    long k = (long) (((amount01 * (magne + permaradub))) * notionalRate);
                                    String ks = String.valueOf(k);
                                    // Loggers.general().info(LOG,"Margin amount in final for set if
                                    // stmt " + ks + "" + "INR");
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG, "Margin amount in final for set if stmt " + ks + "" + "INR");
                                    }
                                    setPMARGIN(magn);
                                    // setNOTRATE(String.valueOf(notionalRate));
                                    setMARAMT(ks + " " + "INR");
                                    //// setPRMARAMT(ks + " " + "INR");
                              }
                              if (facilityFound == false) {
                                    setPMARGIN("");
                                    // setNOTRATE("");
                                    setMARAMT("");
                                    // setPRMARAMT("");
                              }
                        } else {
                              permar = getPMARGIN();
                              // Loggers.general().info(LOG,"permar value in string else--->" +
                              // permar);
                              float permarflo = Float.valueOf(permar);
                              long magndou = (long) permarflo;
                              // Loggers.general().info(LOG,"magndou value in long else--->" +
                              // magndou);
                              // Loggers.general().info(LOG,"float value in else--->" + magndou);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG, "float value in else--->" + magndou);
                              }
                              if (curr.equalsIgnoreCase("INR")) {
                                    notionalRate = 1;
                              } else {
                                    // Loggers.general().info(LOG,"margin lc in else");
                                    String ttRateQuery = "select trim(SPOTRATE) from spotrate where currency='" + curr + "'";
                                    // Loggers.general().info(LOG,"ttRateQuery else - " +
                                    // ttRateQuery);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG, "ttRateQuery for spotrate - " + ttRateQuery);
                                    }
                                    dmsp = con.prepareStatement(ttRateQuery);
                                    dmsr = dmsp.executeQuery();
                                    while (dmsr.next()) {
                                          String ttRate_String = dmsr.getString(1).trim();
                                          notionalRate = Double.parseDouble(ttRate_String);
                                    }
                              }
                              // Loggers.general().info(LOG,"Adding amount in else---- " +
                              // (magndou + permaradub));
                              long k = (long) (((amount01 * (magndou + permaradub))) * notionalRate);
                              // Loggers.general().info(LOG,"Expected margin amount in else---> -
                              // " + k);
                              String ks = String.valueOf(k);
                              // Loggers.general().info(LOG,"Margin amount in final for set else
                              // stmt " + ks + "" + "INR");

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG, "Margin amount in final for set else stmt " + ks + "" + "INR");
                              }
                              setPMARGIN(permar);
                              // setNOTRATE(String.valueOf(notionalRate));
                              setMARAMT(ks + " " + "INR");
                              //// setPRMARAMT(ks + " " + "INR");
                        }
                  } else {

                        // Loggers.general().info(LOG,"Facility id is blank");
                        setPMARGIN("");
                        // setNOTRATE("");
                        setMARAMT("");
                        setPADDMARG("");
                  }
            } catch (Exception e) {
                  // Loggers.general().info(LOG,"exception caught on margin button validation
                  // " + e.getMessage());
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG, "exception caught on margin button validation " + e.getMessage());
                  }
            } finally {
                  try {
                        if (dmsr != null)
                              dmsr.close();
                        if (dmsp != null)
                              dmsp.close();
                        if (con != null)
                              con.close();

                  } catch (SQLException e) {
                        // Loggers.general().info(LOG,"Connection Failed! Check output
                        // console");
                        e.printStackTrace();
                  }
            }
      }

      public void onfetchlienIMPGUAAMDclayButton() {
            lienMark();
      }

      public void onfetchlienIMPGUAADJclayButton() {
            lienMark();
      }

      // Ranji
      public void onfetchlienIMPGUAOUTCLAcalyButton() {
            lienMark();
      }

      public void onREVERSELIENIMPGUAADJclayButton() {
            lienReverse();
      }

      public void onREVERSELIENIMPGUAAMDclayButton() {
            lienReverse();
      }

      public void onREVERSELIENIMPGUAOUTCLAcalyButton() {
            lienReverse();
      }

      public boolean lienReverse() {

            boolean value = false;

            String releaseacc = getLIENBAL().trim();
            String behalfBranch = getDriverWrapper().getEventFieldAsText("BOB", "s", "");
            String strLog = "Log";
            String dailyval_Log = "";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
            EXTGENCUSTPROP CodeLog = queryLog.getUnique();
            if (CodeLog != null) {

                  dailyval_Log = CodeLog.getPropval();
            } else {
                  // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }
            // Loggers.general().info(LOG,"Lien reversal text box=====>" + releaseacc);
            if (releaseacc.equalsIgnoreCase("") || releaseacc == null) {

                  try {
                        // Loggers.general().info(LOG,"Reverse service method calling started
                        // IGT Adjust in if loop");
                        String headerReq = "<?xml version=$1.0$ encoding=$UTF-8$?><ServiceRequest xmlns=$urn:control.services.tiplus2.misys.com$ xmlns:ns2=$urn:messages.service.ti.apps.tiplus2.misys.com$ xmlns:ns3=$urn:common.service.ti.apps.tiplus2.misys.com$ xmlns:ns4=$urn:custom.service.ti.apps.tiplus2.misys.com$><RequestHeader><Service>Customization</Service><Operation>FDLienRemoval</Operation><Credentials><Name>SUPERVISOR</Name></Credentials><ReplyFormat>FULL</ReplyFormat><TargetSystem>KOTAKEXT</TargetSystem><SourceSystem>ZONE1</SourceSystem><NoRepair>Y</NoRepair><NoOverride>Y</NoOverride><CorrelationId>Correlation_Id</CorrelationId><TransactionControl>NONE</TransactionControl></RequestHeader><fdlienremoval xmlns:xsi=$http://www.w3.org/2001/XMLSchema-instance$>";
                        String endXml = "</fdlienremoval></ServiceRequest>";
                        String gridXml = "";
                        String lienStatus = "";
                        String lienbox = "";
                        EnigmaArray<ExtEventLienMarkingEntityWrapper> list = getExtEventLienMarkingData();
                        for (ExtEventLienMarkingEntityWrapper fdwrapper : list) {
                              // //Loggers.general().info(LOG,"11112212121");
                              String marginAmt = fdwrapper.getMARGAMT();
                              String marginAcc = fdwrapper.getDEPOSTNO().trim();
                              String luCur = getDriverWrapper().getEventFieldAsText("AMT", "v", "c");
                              String masRefNo = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                              // String eventRefNo = getDriverWrapper().getEventFieldAsText("MEVR", "r", "");
                              String eventRefNo = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
                              String lien_id = fdwrapper.getLIENID();
                              lienStatus = fdwrapper.getLINEST().trim();
                              lienbox = lienbox + lienStatus;
                              // Loggers.general().info(LOG,"Lien Status " + lienbox);
                              if ((!lienbox.equalsIgnoreCase("") || lienbox != null)
                                          && lienStatus.equalsIgnoreCase("MARK SUCCEEDED")) {

                                    String fdDefaultXml = "<FDRow><MasterReference>Maseter_ref</MasterReference><EventReference>Event_ref</EventReference><BehalfOfBranch>behalf_Branch</BehalfOfBranch><AccountNumber>Account_Number</AccountNumber><LienId>lienRev_id</LienId><Amount>Amount_Ccy</Amount><Currency>Lc_Ccy</Currency><Remarks>masRefNo</Remarks></FDRow>";
                                    // String endXml = "</fdlien></ServiceRequest>";
                                    fdDefaultXml = fdDefaultXml.replace("Maseter_ref", masRefNo);
                                    fdDefaultXml = fdDefaultXml.replace("Event_ref", eventRefNo);
                                    fdDefaultXml = fdDefaultXml.replace("behalf_Branch", behalfBranch);
                                    fdDefaultXml = fdDefaultXml.replace("Account_Number", marginAcc);
                                    fdDefaultXml = fdDefaultXml.replace("lienRev_id", lien_id);
                                    fdDefaultXml = fdDefaultXml.replace("Amount_Ccy", marginAmt);
                                    fdDefaultXml = fdDefaultXml.replace("Lc_Ccy", luCur);
                                    fdDefaultXml = fdDefaultXml.replace("masRefNo", masRefNo);
                                    gridXml = gridXml + fdDefaultXml;
                              } else {
                                    // Loggers.general().info(LOG,"Lien status is blank");
                              }

                        }
                        // //Loggers.general().info(LOG,"gridXml - "+gridXml);
                        // //Loggers.general().info(LOG,"gridXml final ----> "+gridXml);
                        // Final formation of request
                        char j = '"';
                        String val = Character.toString(j);
                        // Loggers.general().info(LOG,"char value - " + j);
                        headerReq = headerReq.replace("$", val);
                        // Replace correlation id
                        String correlationId = randomCorrelationId();
                        // Loggers.general().info(LOG,"correlationId " + correlationId);
                        headerReq = headerReq.replace("Correlation_Id", correlationId);
                        // //Loggers.general().info(LOG,"headerReq replaced with quotes - " +
                        // headerReq);

                        String finalXml = headerReq + gridXml + endXml;
                        // Loggers.general().info(LOG,"Themebridge finalXml -----> " +
                        // finalXml);

                        // sending to themebridge
                        ThemeTransportClient anThemeTransportClient = new ThemeTransportClient();
                        String resquestXML = anThemeTransportClient.invoke("Customization", "FDLienAdd", finalXml);
                        // Loggers.general().info(LOG,"Themebridge resquestXML" + resquestXML);

                        // String line = "12312~500~SUCCEEDED~Lien test
                        // reversal,222450~600~SUCCEEDED~Lien
                        // reversal,33333~700~FAILED~Lien
                        // failed";
                        String line = resquestXML;
                        // String reverse = "Reversal";
                        String[] sp_line = line.split(",");
                        for (int k = 0; k < sp_line.length; k++) {
                              String[] res_sp_line = sp_line[k].split("~");
                              EnigmaArray<ExtEventLienMarkingEntityWrapper> leanDataList = getExtEventLienMarkingData();
                              for (ExtEventLienMarkingEntityWrapper str : leanDataList) {
                                    String currentDepositNum = res_sp_line[0];
                                    String depositNum = str.getDEPOSTNO().trim();
                                    String lienst = str.getLINEST().trim();
                                    // Loggers.general().info(LOG,"currentDepositNum - " +
                                    // currentDepositNum);
                                    // Loggers.general().info(LOG,"depositNum - " + depositNum);
                                    if (depositNum.equalsIgnoreCase(currentDepositNum)) {
                                          if (lienst.equalsIgnoreCase("MARK SUCCEEDED")
                                                      && (!lienbox.equalsIgnoreCase("") || lienbox != null)) {
                                                // Loggers.general().info(LOG,"update the lien status
                                                // blank");
                                                str.setLINEST(res_sp_line[2]);
                                                // Loggers.general().info(LOG,"Lien id " +
                                                // res_sp_line[1]);
                                                str.setLIENID(res_sp_line[1]);
                                                str.setLIENREM(res_sp_line[3]);
                                                // getBtnReverselienIMPLCCLAIMRECclay().setEnabled(false);
                                          } else if (lienst.equalsIgnoreCase("REVERSAL SUCCEEDED")) {

                                          } else {
                                                // Loggers.general().info(LOG,"update the lien status
                                                // FAILED");
                                                // str.setLIENID(res_sp_line[1]);
                                                // str.setLINEST(res_sp_line[2]);
                                                // str.setLIENREM(res_sp_line[3]);
                                          }
                                    } else {
                                          // Loggers.general().info(LOG,"depositNum is not equal
                                          // currentDepositNum in reversal");
                                    }

                              }
                        }
                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"ThemeTransportClient Exceptions! " +
                        // e.getMessage());
                  }

            } else {
                  // Loggers.general().info(LOG,"Reverse service method calling started IGT
                  // Adjust in else====>");

                  try {
                        // Loggers.general().info(LOG,"Reverse service method calling started
                        // IGT Adjust");
                        String headerReq = "<?xml version=$1.0$ encoding=$UTF-8$?><ServiceRequest xmlns=$urn:control.services.tiplus2.misys.com$ xmlns:ns2=$urn:messages.service.ti.apps.tiplus2.misys.com$ xmlns:ns3=$urn:common.service.ti.apps.tiplus2.misys.com$ xmlns:ns4=$urn:custom.service.ti.apps.tiplus2.misys.com$><RequestHeader><Service>Customization</Service><Operation>FDLienRemoval</Operation><Credentials><Name>SUPERVISOR</Name></Credentials><ReplyFormat>FULL</ReplyFormat><TargetSystem>KOTAKEXT</TargetSystem><SourceSystem>ZONE1</SourceSystem><NoRepair>Y</NoRepair><NoOverride>Y</NoOverride><CorrelationId>Correlation_Id</CorrelationId><TransactionControl>NONE</TransactionControl></RequestHeader><fdlienremoval xmlns:xsi=$http://www.w3.org/2001/XMLSchema-instance$>";
                        String endXml = "</fdlienremoval></ServiceRequest>";
                        String gridXml = "";
                        String lienStatus = "";
                        String lienbox = "";
                        EnigmaArray<ExtEventLienMarkingEntityWrapper> list = getExtEventLienMarkingData();
                        for (ExtEventLienMarkingEntityWrapper fdwrapper : list) {
                              // //Loggers.general().info(LOG,"11112212121");
                              String marginAmt = fdwrapper.getMARGAMT();
                              String DepositNum = fdwrapper.getDEPOSTNO().trim();
                              String marginAcc = getLIENBAL().trim();
                              String luCur = getDriverWrapper().getEventFieldAsText("AMT", "v", "c");
                              String masRefNo = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                              String eventRefNo = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
                              String lien_id = fdwrapper.getLIENID();
                              lienStatus = fdwrapper.getLINEST();
                              lienbox = lienbox + lienStatus;
                              // Loggers.general().info(LOG,"Lien Status " + lienbox);
                              if ((!lienbox.equalsIgnoreCase("") || lienbox != null)
                                          && (!marginAcc.equalsIgnoreCase("") || marginAcc != null)
                                          && marginAcc.equalsIgnoreCase(DepositNum)
                                          && lienStatus.equalsIgnoreCase("MARK SUCCEEDED")) {
                                    String fdDefaultXml = "<FDRow><MasterReference>Maseter_ref</MasterReference><EventReference>Event_ref</EventReference><BehalfOfBranch>behalf_Branch</BehalfOfBranch><AccountNumber>Account_Number</AccountNumber><LienId>lienRev_id</LienId><Amount>Amount_Ccy</Amount><Currency>Lc_Ccy</Currency><Remarks>masRefNo</Remarks></FDRow>";
                                    // String endXml = "</fdlien></ServiceRequest>";
                                    fdDefaultXml = fdDefaultXml.replace("Maseter_ref", masRefNo);
                                    fdDefaultXml = fdDefaultXml.replace("Event_ref", eventRefNo);
                                    fdDefaultXml = fdDefaultXml.replace("behalf_Branch", behalfBranch);
                                    fdDefaultXml = fdDefaultXml.replace("Account_Number", marginAcc);
                                    fdDefaultXml = fdDefaultXml.replace("lienRev_id", lien_id);
                                    fdDefaultXml = fdDefaultXml.replace("Amount_Ccy", marginAmt);
                                    fdDefaultXml = fdDefaultXml.replace("Lc_Ccy", luCur);
                                    fdDefaultXml = fdDefaultXml.replace("masRefNo", masRefNo);
                                    gridXml = gridXml + fdDefaultXml;
                              } else {
                                    // Loggers.general().info(LOG,"Lien status and lien deposit no
                                    // is blank" + DepositNum + "and====>" + marginAcc);
                              }
                        }
                        // //Loggers.general().info(LOG,"gridXml - "+gridXml);
                        // //Loggers.general().info(LOG,"gridXml final ----> "+gridXml);
                        // Final formation of request
                        char j = '"';
                        String val = Character.toString(j);
                        // Loggers.general().info(LOG,"char value - " + j);
                        headerReq = headerReq.replace("$", val);
                        // Replace correlation id
                        String correlationId = randomCorrelationId();
                        // Loggers.general().info(LOG,"correlationId " + correlationId);
                        headerReq = headerReq.replace("Correlation_Id", correlationId);
                        // //Loggers.general().info(LOG,"headerReq replaced with quotes - " +
                        // headerReq);

                        String finalXml = headerReq + gridXml + endXml;
                        // Loggers.general().info(LOG,"Themebridge finalXml -----> " +
                        // finalXml);

                        // sending to themebridge
                        ThemeTransportClient anThemeTransportClient = new ThemeTransportClient();
                        String resquestXML = anThemeTransportClient.invoke("Customization", "FDLienAdd", finalXml);
                        // Loggers.general().info(LOG,"Themebridge resquestXML" + resquestXML);

                        // String line = "12312~500~SUCCEEDED~Lien test
                        // reversal,222450~600~SUCCEEDED~Lien
                        // reversal,33333~700~FAILED~Lien
                        // failed";
                        String line = resquestXML;
                        // String reverse = "Reversal";
                        String[] sp_line = line.split(",");
                        for (int k = 0; k < sp_line.length; k++) {
                              String[] res_sp_line = sp_line[k].split("~");

                              EnigmaArray<ExtEventLienMarkingEntityWrapper> leanDataList = getExtEventLienMarkingData();
                              for (ExtEventLienMarkingEntityWrapper str : leanDataList) {
                                    // String currentDepositNum = res_sp_line[0].trim();
                                    String currentDepositNum = str.getDEPOSTNO().trim();
                                    String depositNum = getLIENBAL().trim();
                                    String lienst = str.getLINEST().trim();
                                    // Loggers.general().info(LOG,"currentDepositNum - " +
                                    // currentDepositNum);
                                    // Loggers.general().info(LOG,"depositNum - " + depositNum);
                                    if (depositNum.equalsIgnoreCase(currentDepositNum)) {
                                          if (lienst.equalsIgnoreCase("MARK SUCCEEDED")
                                                      && (!lienbox.equalsIgnoreCase("") || lienbox != null)) {
                                                // Loggers.general().info(LOG,"update the lien status
                                                // blank");
                                                str.setLINEST(res_sp_line[2]);
                                                // Loggers.general().info(LOG,"Lien id " +
                                                // res_sp_line[1]);
                                                str.setLIENID(res_sp_line[1]);
                                                str.setLIENREM(res_sp_line[3]);
                                                // getBtnReverselienIMPLCCLAIMRECclay().setEnabled(false);
                                          } else if (lienst.equalsIgnoreCase("REVERSAL SUCCEEDED")) {
                                                // Loggers.general().info(LOG,"REVERSAL update=====>");

                                          } else {
                                                // Loggers.general().info(LOG,"update the lien status
                                                // FAILED");
                                                // str.setLIENID(res_sp_line[1]);
                                                // str.setLINEST(res_sp_line[2]);
                                                // str.setLIENREM(res_sp_line[3]);
                                          }
                                    } else {
                                          // Loggers.general().info(LOG,"depositNum is not equal
                                          // currentDepositNum in reversal");
                                    }

                              }

                        }
                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"ThemeTransportClient Exceptions! " +
                        // e.getMessage());
                  }

            }

            return value;
      }

      public boolean lienMark() {
            boolean value = false;

            String Margamt = "0";
            String gridamtcur = "";
            String result = "";
            String lienres = "";
            String systemDate = getDriverWrapper().getEventFieldAsText("TDY", "d", "").trim();
            // String lieDate = getDriverWrapper().getEventFieldAsText("TDY", "d",
            // "").trim();
            String AcnoRem = "Check Space or Spl charactor should not be there";
            String behalfBranch = getDriverWrapper().getEventFieldAsText("BOB", "s", "").trim();
            getExtEventLienMarkingDelete().setEnabled(false);
            Margamt = getMARAMT().toString();
            String facility = getFACLTYID();
            int count_R = 0;
            int count_M = 0;
            if (Margamt.equalsIgnoreCase("") || Margamt == null) {
                  Margamt = "0";

            }
            BigDecimal margindec = new BigDecimal("0");
            BigDecimal margindecadd = new BigDecimal("0");
            BigDecimal marginbig = new BigDecimal("0");
            BigDecimal one = new BigDecimal(1);
            BigDecimal thousand = new BigDecimal(1000);
            BigDecimal hundred = new BigDecimal(100);
            String add_lie = Margamt.replaceAll("[^0-9]", "");
            // Loggers.general().info(LOG," MarginAmt in number----> " + add_lie);
            // Margamtdob = Double.valueOf(add_lie);
            BigDecimal MargamtNum = new BigDecimal(add_lie);
            BigDecimal Margamtdob = MargamtNum.divide(hundred);

            // Loggers.general().info(LOG,"Grid BigDecimal amount--->" + Margamtdob);

            getExtEventLienMarkingDelete().setEnabled(false);
            String strLog = "Log";
            String dailyval_Log = "";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
            EXTGENCUSTPROP CodeLog = queryLog.getUnique();
            if (CodeLog != null) {

                  dailyval_Log = CodeLog.getPropval();
            } else {
                  // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }

            if (dailyval_Log.equalsIgnoreCase("YES")) {
                  Loggers.general().info(LOG, "Grid BigDecimal amount--->" + Margamtdob);

            }

            EnigmaArray<ExtEventLienMarkingEntityWrapper> liste2 = getExtEventLienMarkingData();

            if (liste2.getSize() > 0) {
                  Iterator<ExtEventLienMarkingEntityWrapper> iterator1 = liste2.iterator();

                  while (iterator1.hasNext()) {
                        ExtEventLienMarkingEntityWrapper ExtEventLienMark = (ExtEventLienMarkingEntityWrapper) iterator1.next();

                        String marginAmt = ExtEventLienMark.getMARGAMTAmount().toString();

                        // Loggers.general().info(LOG,"Amount marginAmt String-----> " +
                        // marginAmt);

                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG, "Amount marginAmt String-----> " + marginAmt);

                        }
                        marginbig = new BigDecimal(marginAmt);

                        gridamtcur = ExtEventLienMark.getMARGAMTCurrency().toString();
                        // Loggers.general().info(LOG,"Amount marginAmt-----> " + marginAmt);

                        // ConnectionMaster connectionMaster = new ConnectionMaster();
                        // double divideByDecimal =
                        // connectionMaster.getDecimalforCurrency(gridamtcur);
                        // gridadded = valamtdob / divideByDecimal;
                        // Loggers.general().info(LOG,"Grid divided amount--->" + gridadded);

                        // gridamount = gridamount + gridadded;
                        //
                        // gridamount += gridadded;
                        // Loggers.general().info(LOG,"Amount marginAmt double in while-----> "
                        // + gridamount);
                        if (gridamtcur.equalsIgnoreCase("BHD") || gridamtcur.equalsIgnoreCase("KWD")
                                    || gridamtcur.equalsIgnoreCase("OMR")) {
                              margindec = marginbig.divide(thousand);
                        } else if (gridamtcur.equalsIgnoreCase("JPY") || gridamtcur.equalsIgnoreCase("KRW")) {

                              margindec = marginbig.divide(one);
                        } else {
                              margindec = marginbig.divide(hundred);
                        }
                        // Loggers.general().info(LOG,"Amount marginAmt bigdecimal in
                        // while-----> " + margindec);

                        margindecadd = margindecadd.add(margindec);

                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG, "Margin add amount-----> " + margindecadd);

                        }

                        String deptno = ExtEventLienMark.getDEPOSTNO().trim();
                        String masRefNo = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim();
                        String eventCode = getDriverWrapper().getEventFieldAsText("EVR", "r", "").trim();

                  }

            }

            if (facility.length() > 0) {
                  if (Margamtdob.compareTo(margindecadd) >= 0) {

                        try {

                              String headerReq = "<?xml version=$1.0$ encoding=$UTF-8$?><ServiceRequest xmlns=$urn:control.services.tiplus2.misys.com$ xmlns:ns2=$urn:messages.service.ti.apps.tiplus2.misys.com$ xmlns:ns3=$urn:common.service.ti.apps.tiplus2.misys.com$ xmlns:ns4=$urn:custom.service.ti.apps.tiplus2.misys.com$><RequestHeader><Service>Customization</Service><Operation>FDLienAdd</Operation><Credentials><Name>SUPERVISOR</Name></Credentials><ReplyFormat>FULL</ReplyFormat><TargetSystem>KOTAKEXT</TargetSystem><SourceSystem>ZONE1</SourceSystem><NoRepair>Y</NoRepair><NoOverride>Y</NoOverride><CorrelationId>Correlation_Id</CorrelationId><TransactionControl>NONE</TransactionControl></RequestHeader><fdlienadd xmlns:xsi=$http://www.w3.org/2001/XMLSchema-instance$>";
                              String endXml = "</fdlienadd></ServiceRequest>";
                              String gridXml = "";
                              String startDate = "";
                              String endDate = "";
                              EnigmaArray<ExtEventLienMarkingEntityWrapper> list = getExtEventLienMarkingData();
                              for (ExtEventLienMarkingEntityWrapper fdwrapper : list) {
                                    String marginAmt = fdwrapper.getMARGAMT();
                                    String marginAcc = fdwrapper.getDEPOSTNO().trim();
                                    String masRefNo = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                                    String eventRefNo = getDriverWrapper().getEventFieldAsText("MEVR", "r", "");
                                    String issueDate = getDriverWrapper().getEventFieldAsText("ISS", "d", "");
                                    String mas_evnt = masRefNo;
                                    String expiryDate = getDriverWrapper().getEventFieldAsText("EXP", "d", "");
                                    DateFormat userDateFormat = new SimpleDateFormat("dd/mm/yy");
                                    DateFormat dateFormatNeeded = new SimpleDateFormat("yyyy-mm-dd");

                                    Date date;
                                    Date date1;

                                    try {
                                          date = (Date) userDateFormat.parse(issueDate);
                                          startDate = dateFormatNeeded.format(date);
                                          date1 = (Date) userDateFormat.parse(expiryDate);
                                          endDate = dateFormatNeeded.format(date1);

                                          SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yy");
                                          Loggers.general().info(LOG, "System date" + systemDate);

                                          Date sysdate = (Date) formatter1.parse(systemDate);
                                          Loggers.general().info(LOG, "systemDate Date lien----- " + sysdate);
                                          result = formatter1.format(sysdate);
                                          Loggers.general().info(LOG, "result Date ----- " + result);

                                    } catch (ParseException e) {
                                          Loggers.general().info(LOG, "Date Exception facility " + e.getMessage());
                                    }
                                    String luCur = getDriverWrapper().getEventFieldAsText("AMT", "v", "c");
                                    String mas_evntRef = masRefNo;
                                    String lienbox = fdwrapper.getLINEST();
                                    if (lienbox.equalsIgnoreCase("") || lienbox == null) {
                                          // Loggers.general().info(LOG,"Lien Status is blank");
                                          String fdDefaultXml = "<FDRow><MasterReference>Maseter_ref</MasterReference><EventReference>Event_ref</EventReference><BehalfOfBranch>behalf_Branch</BehalfOfBranch><AccountNumber>Account_Number</AccountNumber><Amount>Amount_Ccy</Amount><Currency>LC_Cur</Currency><ReasonCode>TRD</ReasonCode><Remarks>mas_evnt</Remarks><LienStartDate>Issue_Date</LienStartDate><LienEndDate>Expiry_Date</LienEndDate></FDRow>";
                                          fdDefaultXml = fdDefaultXml.replace("Maseter_ref", masRefNo);
                                          fdDefaultXml = fdDefaultXml.replace("Event_ref", eventRefNo);
                                          fdDefaultXml = fdDefaultXml.replace("behalf_Branch", behalfBranch);
                                          fdDefaultXml = fdDefaultXml.replace("Account_Number", marginAcc);
                                          fdDefaultXml = fdDefaultXml.replace("Amount_Ccy", marginAmt);
                                          fdDefaultXml = fdDefaultXml.replace("LC_Cur", luCur);
                                          fdDefaultXml = fdDefaultXml.replace("mas_evnt", mas_evntRef);
                                          fdDefaultXml = fdDefaultXml.replace("Issue_Date", startDate);
                                          fdDefaultXml = fdDefaultXml.replace("Expiry_Date", endDate);
                                          gridXml = gridXml + fdDefaultXml;
                                    } else {
                                          // Loggers.general().info(LOG,"Lien Status is not
                                          // blank");
                                    }

                              }

                              char j = '"';
                              String val = Character.toString(j);
                              headerReq = headerReq.replace("$", val);
                              String correlationId = randomCorrelationId();
                              // Loggers.general().info(LOG,"correlationId " + correlationId);
                              headerReq = headerReq.replace("Correlation_Id", correlationId);
                              String finalXml = headerReq + gridXml + endXml;
                              // Loggers.general().info(LOG,"Themebridge finalXml " +
                              // finalXml);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG, "Themebridge finalXml " + finalXml);

                              }
                              ThemeTransportClient anThemeTransportClient = new ThemeTransportClient();
                              String responseXML = anThemeTransportClient.invoke("Customization", "FDLienAdd", finalXml);

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG, "Final Themebridge responseXML==========>" + responseXML);

                              }

                              EnigmaArray<ExtEventLienMarkingEntityWrapper> leanDataList = getExtEventLienMarkingData();
                              String line = responseXML;
                              String[] sp_line = line.split(",");
                              for (int k = 0; k < sp_line.length; k++) {
                                    String[] res_sp_line = sp_line[k].split("~");
                                    Iterator<ExtEventLienMarkingEntityWrapper> iterator1 = leanDataList.iterator();

                                    for (ExtEventLienMarkingEntityWrapper str : leanDataList) {
                                          String depositNum = str.getDEPOSTNO().trim();

                                          String currentDepositNum = res_sp_line[0].trim();
                                          String lienbox = str.getLINEST();

                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG, "Lien grid depositNum" + depositNum);
                                                Loggers.general().info(LOG, "Lien response currentDepositNum" + currentDepositNum);

                                          }
                                          if (depositNum.matches("[0-9]+") && depositNum.length() > 1) {
                                                if (depositNum.equalsIgnoreCase(currentDepositNum)) {
                                                      if (res_sp_line[2].equalsIgnoreCase("MARK SUCCEEDED")
                                                                  && (lienbox.equalsIgnoreCase("") || lienbox == null)) {
                                                            // Loggers.general().info(LOG,"lien
                                                            // SUCCEEDED
                                                            // status" + res_sp_line[2]);
                                                            str.setLINEST(res_sp_line[2]);
                                                            str.setLIENID(res_sp_line[1]);
                                                            str.setLIENREM(res_sp_line[3]);

                                                            getExtEventLienMarkingUpdate().setEnabled(false);
                                                            getExtEventLienMarkingDelete().setEnabled(false);
                                                      } else if ((lienbox.equalsIgnoreCase("") || lienbox == null)) {
                                                            // Loggers.general().info(LOG,"lien FAILED
                                                            // status" + res_sp_line[2]);
                                                            str.setLINEST(res_sp_line[2]);
                                                            str.setLIENID(res_sp_line[1]);
                                                            str.setLIENREM(res_sp_line[3]);

                                                            getExtEventLienMarkingUpdate().setEnabled(true);
                                                            getExtEventLienMarkingDelete().setEnabled(false);
                                                      }

                                                      else {

                                                            // Loggers.general().info(LOG,"lien marked
                                                            // depositNum is not equal to
                                                            // currentDepositNum");
                                                      }
                                                } else {
                                                      // Loggers.general().info(LOG,"lien marked
                                                      // status is
                                                      // not blank");
                                                }
                                          } else {
                                                str.setLIENREM(AcnoRem);
                                                // Loggers.general().info(LOG,"Special charactor");
                                          }
                                    }

                              }

                              // }
                        } catch (Exception e) {
                              Loggers.general().info(LOG, "ThemeTransportClient Exceptions!" + e.getMessage());
                        }

                  }

                  else {
                        // Loggers.general().info(LOG,"Lien mark amount is greater than the
                        // margin amount");

                  }
            } else {
                  // Loggers.general().info(LOG,"Facility id is blank");

                  try {

                        String headerReq = "<?xml version=$1.0$ encoding=$UTF-8$?><ServiceRequest xmlns=$urn:control.services.tiplus2.misys.com$ xmlns:ns2=$urn:messages.service.ti.apps.tiplus2.misys.com$ xmlns:ns3=$urn:common.service.ti.apps.tiplus2.misys.com$ xmlns:ns4=$urn:custom.service.ti.apps.tiplus2.misys.com$><RequestHeader><Service>Customization</Service><Operation>FDLienAdd</Operation><Credentials><Name>SUPERVISOR</Name></Credentials><ReplyFormat>FULL</ReplyFormat><TargetSystem>KOTAKEXT</TargetSystem><SourceSystem>ZONE1</SourceSystem><NoRepair>Y</NoRepair><NoOverride>Y</NoOverride><CorrelationId>Correlation_Id</CorrelationId><TransactionControl>NONE</TransactionControl></RequestHeader><fdlienadd xmlns:xsi=$http://www.w3.org/2001/XMLSchema-instance$>";
                        String endXml = "</fdlienadd></ServiceRequest>";
                        String gridXml = "";
                        String startDate = "";
                        String endDate = "";
                        EnigmaArray<ExtEventLienMarkingEntityWrapper> list = getExtEventLienMarkingData();
                        for (ExtEventLienMarkingEntityWrapper fdwrapper : list) {
                              String marginAmt = fdwrapper.getMARGAMT();
                              String marginAcc = fdwrapper.getDEPOSTNO().trim();
                              String masRefNo = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                              String eventRefNo = getDriverWrapper().getEventFieldAsText("MEVR", "r", "");
                              String issueDate = getDriverWrapper().getEventFieldAsText("ISS", "d", "");
                              String mas_evnt = masRefNo;
                              String expiryDate = getDriverWrapper().getEventFieldAsText("EXP", "d", "");
                              DateFormat userDateFormat = new SimpleDateFormat("dd/mm/yy");
                              DateFormat dateFormatNeeded = new SimpleDateFormat("yyyy-mm-dd");
                              Date date;
                              Date date1;
                              try {
                                    date = (Date) userDateFormat.parse(issueDate);
                                    startDate = dateFormatNeeded.format(date);
                                    date1 = (Date) userDateFormat.parse(expiryDate);
                                    endDate = dateFormatNeeded.format(date1);

                                    SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yy");
                                    Loggers.general().info(LOG, "System date" + systemDate);

                                    Date sysdate = (Date) formatter1.parse(systemDate);
                                    Loggers.general().info(LOG, "systemDate Date lien----- " + sysdate);
                                    result = formatter1.format(sysdate);
                                    Loggers.general().info(LOG, "result Date ----- " + result);

                              } catch (ParseException e) {
                                    Loggers.general().info(LOG, "Date Exception " + e.getMessage());
                              }
                              String luCur = getDriverWrapper().getEventFieldAsText("AMT", "v", "c");
                              String mas_evntRef = masRefNo;
                              String lienbox = fdwrapper.getLINEST();
                              if (lienbox.equalsIgnoreCase("") || lienbox == null) {
                                    // Loggers.general().info(LOG,"Lien Status is blank");
                                    String fdDefaultXml = "<FDRow><MasterReference>Maseter_ref</MasterReference><EventReference>Event_ref</EventReference><BehalfOfBranch>behalf_Branch</BehalfOfBranch><AccountNumber>Account_Number</AccountNumber><Amount>Amount_Ccy</Amount><Currency>LC_Cur</Currency><ReasonCode>TRD</ReasonCode><Remarks>mas_evnt</Remarks><LienStartDate>Issue_Date</LienStartDate><LienEndDate>Expiry_Date</LienEndDate></FDRow>";
                                    fdDefaultXml = fdDefaultXml.replace("Maseter_ref", masRefNo);
                                    fdDefaultXml = fdDefaultXml.replace("Event_ref", eventRefNo);
                                    fdDefaultXml = fdDefaultXml.replace("behalf_Branch", behalfBranch);
                                    fdDefaultXml = fdDefaultXml.replace("Account_Number", marginAcc);
                                    fdDefaultXml = fdDefaultXml.replace("Amount_Ccy", marginAmt);
                                    fdDefaultXml = fdDefaultXml.replace("LC_Cur", luCur);
                                    fdDefaultXml = fdDefaultXml.replace("mas_evnt", mas_evntRef);
                                    fdDefaultXml = fdDefaultXml.replace("Issue_Date", startDate);
                                    fdDefaultXml = fdDefaultXml.replace("Expiry_Date", endDate);
                                    gridXml = gridXml + fdDefaultXml;
                              } else {
                                    // Loggers.general().info(LOG,"Lien Status is not blank");
                              }

                        }

                        char j = '"';
                        String val = Character.toString(j);
                        headerReq = headerReq.replace("$", val);
                        String correlationId = randomCorrelationId();
                        // Loggers.general().info(LOG,"correlationId " + correlationId);
                        headerReq = headerReq.replace("Correlation_Id", correlationId);
                        String finalXml = headerReq + gridXml + endXml;
                        // Loggers.general().info(LOG,"Themebridge finalXml " + finalXml);

                        ThemeTransportClient anThemeTransportClient = new ThemeTransportClient();
                        String responseXML = anThemeTransportClient.invoke("Customization", "FDLienAdd", finalXml);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG, "Final Themebridge responseXML==========>" + responseXML);

                        }

                        EnigmaArray<ExtEventLienMarkingEntityWrapper> leanDataList = getExtEventLienMarkingData();
                        String line = responseXML;
                        String[] sp_line = line.split(",");
                        for (int k = 0; k < sp_line.length; k++) {
                              String[] res_sp_line = sp_line[k].split("~");
                              Iterator<ExtEventLienMarkingEntityWrapper> iterator1 = leanDataList.iterator();

                              for (ExtEventLienMarkingEntityWrapper str : leanDataList) {
                                    String depositNum = str.getDEPOSTNO().trim();

                                    String currentDepositNum = res_sp_line[0];
                                    // //Loggers.general().info(LOG,"res_sp_line[0]==>"+res_sp_line[0]);

                                    String lienbox = str.getLINEST();
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG, "Lien grid depositNum" + depositNum);
                                          Loggers.general().info(LOG, "Lien response currentDepositNum" + currentDepositNum);

                                    }

                                    if (depositNum.matches("[0-9]+") && depositNum.length() > 1) {

                                          if (depositNum.equalsIgnoreCase(currentDepositNum)) {

                                                if (res_sp_line[2].equalsIgnoreCase("MARK SUCCEEDED")
                                                            && (lienbox.equalsIgnoreCase("") || lienbox == null)) {
                                                      // Loggers.general().info(LOG,"lien SUCCEEDED
                                                      // status" + res_sp_line[2]);
                                                      str.setLINEST(res_sp_line[2]);
                                                      str.setLIENID(res_sp_line[1]);
                                                      str.setLIENREM(res_sp_line[3]);

                                                      getExtEventLienMarkingUpdate().setEnabled(false);
                                                      getExtEventLienMarkingDelete().setEnabled(false);
                                                } else if ((lienbox.equalsIgnoreCase("") || lienbox == null)) {
                                                      // Loggers.general().info(LOG,"lien FAILED
                                                      // status" +
                                                      // res_sp_line[2]);
                                                      str.setLINEST(res_sp_line[2]);
                                                      str.setLIENID(res_sp_line[1]);
                                                      str.setLIENREM(res_sp_line[3]);

                                                      getExtEventLienMarkingUpdate().setEnabled(true);
                                                      getExtEventLienMarkingDelete().setEnabled(false);
                                                }

                                                else {

                                                      // Loggers.general().info(LOG,"lien marked
                                                      // depositNum is not equal to
                                                      // currentDepositNum");
                                                }
                                          } else {
                                                // Loggers.general().info(LOG,"lien marked status is
                                                // not
                                                // blank");
                                          }
                                    } else {
                                          str.setLIENREM(AcnoRem);
                                          // Loggers.general().info(LOG,"Special charactor");
                                    }
                              }

                        }

                        // }
                  } catch (Exception e) {
                        Loggers.general().info(LOG, "ThemeTransportClient Exceptions! " + e.getMessage());
                  }

            }

            return value;
      }

      public void onGURANTEEIMPGUAAMDclayButton() {

            String pono = "";

            String medAmt = "";
            String Current_finance_eligible_Amt = "";
            Double eligible_amt = 0.0;
            String currency = "";
            String firstcurrfamt = "";
            long eligible_long = 0;
            long mAmt_long = 0;
            String mamt = getDriverWrapper().getEventFieldAsText("MST", "r", "");
            String strLog = "Log";
            String dailyval_Log = "";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
            EXTGENCUSTPROP CodeLog = queryLog.getUnique();
            if (CodeLog != null) {

                  dailyval_Log = CodeLog.getPropval();
            } else {
                  // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }
            try {

                  // Getting the Table Structure and Size
                  EnigmaArray<ExtEventCounterGuranteeEntityWrapper> list = getExtEventCounterGuranteeData();
                  for (int i = 0; i < list.getSize().intValue(); i++) {
                        Iterator<ExtEventCounterGuranteeEntityWrapper> iterator1 = list.iterator();
                        // Getting the table Values
                        while (iterator1.hasNext()) {
                              ExtEventCounterGuranteeEntityWrapper fdwrapper = (ExtEventCounterGuranteeEntityWrapper) iterator1
                                          .next();
                              con = ConnectionMaster.getConnection();
                              String guaranteeNumber = fdwrapper.getGURNO().trim();
                              String query = "select trim(mas.amount), mas.ccy , to_char( mas.expiry_dat,'dd/mm/yy' ) from master mas,baseevent bas  where mas.key97=bas.master_key and mas.master_ref='"
                                          + guaranteeNumber + "'";
                              // Loggers.general().info(LOG,"Master amount in query " + query);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG, "Master amount in query " + query);
                              }
                              ps = con.prepareStatement(query);
                              rs = ps.executeQuery();
                              if (rs.next()) {
                                    eligible_amt = rs.getDouble(1);
                                    // Loggers.general().info(LOG,"Total eligible_amt " +
                                    // eligible_amt);
                                    String getced = "select c8ced from c8pf where c8ccy='" + rs.getString(2) + "'";
                                    PreparedStatement drms = con.prepareStatement(getced);
                                    // Loggers.general().info(LOG,"sql" + getced);
                                    ResultSet drms1 = drms.executeQuery();
                                    int a = 0;
                                    while (drms1.next()) {
                                          a = drms1.getInt(1);
                                    }
                                    if (a == 2) {
                                          eligible_amt = eligible_amt / 100;
                                    }
                                    if (a == 3) {
                                          eligible_amt = eligible_amt / 1000;
                                    }
                                    // Loggers.general().info(LOG,"Total eligible_amt--->" +
                                    // eligible_amt);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG, "Total eligible_amt--->" + eligible_amt);
                                    }
                                    String eligible_String = String.valueOf(eligible_amt);
                                    float eligible_flot = Float.valueOf(eligible_String);
                                    eligible_long = (long) eligible_flot;
                                    fdwrapper.setELIAMT(String.valueOf(eligible_long * 100) + " " + rs.getString(2));

                                    firstcurrfamt = String.valueOf(eligible_long * 100) + "" + rs.getString(2);
                                    fdwrapper.setEXDATE(rs.getString(3));
                                    currency = rs.getString(2);
                              }

                        }
                  }
            } catch (Exception e) {
                  // Loggers.general().info(LOG,"Exception is " + e.getMessage());

                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG, "Exception is " + e.getMessage());
                  }
            }

            finally {
                  try {
                        if (rs != null)
                              rs.close();
                        if (ps != null)
                              ps.close();
                        if (con != null)
                              con.close();

                  } catch (SQLException e) {
                        // Loggers.general().info(LOG,"Connection Failed! Check output
                        // console");
                        e.printStackTrace();
                  }
            }

      }

      public void onGURANTEEIMPGUAADJclayButton() {

            String pono = "";

            String medAmt = "";
            String Current_finance_eligible_Amt = "";
            Double eligible_amt = 0.0;
            String currency = "";
            String firstcurrfamt = "";
            long eligible_long = 0;
            long mAmt_long = 0;
            String mamt = getDriverWrapper().getEventFieldAsText("MST", "r", "");
            String strLog = "Log";
            String dailyval_Log = "";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
            EXTGENCUSTPROP CodeLog = queryLog.getUnique();
            if (CodeLog != null) {

                  dailyval_Log = CodeLog.getPropval();
            } else {
                  // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }
            try {

                  // Getting the Table Structure and Size
                  EnigmaArray<ExtEventCounterGuranteeEntityWrapper> list = getExtEventCounterGuranteeData();
                  for (int i = 0; i < list.getSize().intValue(); i++) {
                        Iterator<ExtEventCounterGuranteeEntityWrapper> iterator1 = list.iterator();
                        // Getting the table Values
                        while (iterator1.hasNext()) {
                              ExtEventCounterGuranteeEntityWrapper fdwrapper = (ExtEventCounterGuranteeEntityWrapper) iterator1
                                          .next();
                              con = ConnectionMaster.getConnection();
                              String guaranteeNumber = fdwrapper.getGURNO().trim();
                              String query = "select trim(mas.amount), mas.ccy , to_char( mas.expiry_dat,'dd/mm/yy' ) from master mas,baseevent bas  where mas.key97=bas.master_key and mas.master_ref='"
                                          + guaranteeNumber + "'";
                              // Loggers.general().info(LOG,"Master amount in query " + query);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG, "Master amount in query " + query);
                              }
                              ps = con.prepareStatement(query);
                              rs = ps.executeQuery();
                              if (rs.next()) {
                                    eligible_amt = rs.getDouble(1);
                                    // Loggers.general().info(LOG,"Total eligible_amt " +
                                    // eligible_amt);
                                    String getced = "select c8ced from c8pf where c8ccy='" + rs.getString(2) + "'";
                                    PreparedStatement drms = con.prepareStatement(getced);
                                    // Loggers.general().info(LOG,"sql" + getced);
                                    ResultSet drms1 = drms.executeQuery();
                                    int a = 0;
                                    while (drms1.next()) {
                                          a = drms1.getInt(1);
                                    }
                                    if (a == 2) {
                                          eligible_amt = eligible_amt / 100;
                                    }
                                    if (a == 3) {
                                          eligible_amt = eligible_amt / 1000;
                                    }
                                    // Loggers.general().info(LOG,"Total eligible_amt--->" +
                                    // eligible_amt);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG, "Total eligible_amt--->" + eligible_amt);
                                    }
                                    String eligible_String = String.valueOf(eligible_amt);
                                    float eligible_flot = Float.valueOf(eligible_String);
                                    eligible_long = (long) eligible_flot;
                                    fdwrapper.setELIAMT(String.valueOf(eligible_long * 100) + " " + rs.getString(2));

                                    firstcurrfamt = String.valueOf(eligible_long * 100) + "" + rs.getString(2);
                                    fdwrapper.setEXDATE(rs.getString(3));
                                    currency = rs.getString(2);
                              }

                        }
                  }
            } catch (Exception e) {
                  // Loggers.general().info(LOG,"Exception is " + e.getMessage());

                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG, "Exception is " + e.getMessage());
                  }
            }

            finally {
                  try {
                        if (rs != null)
                              rs.close();
                        if (ps != null)
                              ps.close();
                        if (con != null)
                              con.close();

                  } catch (SQLException e) {
                        // Loggers.general().info(LOG,"Connection Failed! Check output
                        // console");
                        e.printStackTrace();
                  }
            }

      }

      public void ondisplayvalIMPGUAOUTCLAcalyButton() {
            // Loggers.general().info(LOG,"Imp gur button for POD");
            String strLog = "Log";
            String dailyval_Log = "";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
            EXTGENCUSTPROP CodeLog = queryLog.getUnique();
            if (CodeLog != null) {

                  dailyval_Log = CodeLog.getPropval();
            } else {
                  // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }
            String Ifsc = getIFSCCO_Name().trim();

            if (!Ifsc.trim().equalsIgnoreCase("") || Ifsc != null) {
                  try {
                        con = ConnectionMaster.getConnection();
                        String query = "select count(*) from EXTIFSCC where IFSC='" + Ifsc + "'";
                        // Loggers.general().info(LOG,"query for IFSC button" + query);
                        int count = 0;
                        ps1 = con.prepareStatement(query);
                        rs1 = ps1.executeQuery();
                        while (rs1.next()) {
                              // Loggers.general().info(LOG,"Entered while");
                              count = rs1.getInt(1);
                              // Loggers.general().info(LOG,"value of count in while " + count);
                        }

                        if (count == 0) {

                              // Loggers.general().info(LOG,"IFSC Code is Not valid ");
                              String ban = getBENBAK();
                              String bran = getBENBRN();
                              String cit = getBENCITY();
                              ban = "";
                              bran = "";
                              bran = "";
                              setBENBAK(ban);
                              setBENBRN(bran);
                              setBENCITY(bran);

                        } else {
                              try {
                                    // Loggers.general().info(LOG,"IFSC Code is valid");
                                    String spq = "Select trim(BANK),trim(BRANCH),trim(CITY) from EXTIFSCC where IFSC ='" + Ifsc
                                                + "'";
                                    // Loggers.general().info(LOG,spq);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG, "IFSC code is " + spq);
                                    }
                                    ps1 = con.prepareStatement(spq);
                                    rs1 = ps1.executeQuery();
                                    while (rs1.next()) {
                                          setBENBAK((rs1.getString(1).trim()));
                                          setBENBRN(rs1.getString(2).trim());
                                          setBENCITY((rs1.getString(3).trim()));

                                    }

                              } catch (Exception e) {

                                    // Loggers.general().info(LOG,"event catch");
                              }

                        }
                  } catch (Exception e) {

                        // Loggers.general().info(LOG,"event catch");
                  } finally {
                        try {
                              if (rs1 != null)
                                    rs1.close();
                              if (ps1 != null)
                                    ps1.close();
                              if (con != null)
                                    con.close();
                        } catch (SQLException e) {
                              // Loggers.general().info(LOG,"Connection Failed! Check output
                              // console");
                              e.printStackTrace();
                        }
                  }
            } else {
                  // Loggers.general().info(LOG,"IFSC code is blak");
            }

      }

      public void onBUYFETCHIMPGUAADJclayButton() {
            try {
                  System.out.println("Buyer Credit Button Clicked !!");
                  String masRefNo = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim();
                  String eventCode = getDriverWrapper().getEventFieldAsText("EVR", "r", "").trim();
                  EnigmaArray<ExtEventBUYSCREDITMASTEREntityWrapper> liste = getExtEventBUYSCREDITMASTERData();
                  int count = 0;
                  Iterator<ExtEventBUYSCREDITMASTEREntityWrapper> iterator = liste.iterator();
                  String query = "SELECT EXT.* FROM MASTER MAS, BASEEVENT BEV, EXTEVENTBUY EXT"
                              + "WHERE MAS.KEY97 = BEV.MASTER_KEY AND BEV.EXTFIELD = EXT.FK_EVENT"
                              + "AND TRIM(MAS.MASTER_REF) = '" + masRefNo + "' AND BEV.REFNO_PFIX||LPAD(BEV.REFNO_SERL,3,0) = '"
                              + eventCode + "'";
                  System.out.println("Buyers Credit Grid Query:" + query);
                  con1 = ConnectionMaster.getConnection();
                  psd = con1.prepareStatement(query);
                  rst = psd.executeQuery();
                  while (rst.next()) {
                        System.out.println("Buyers Credit Grid: In While Loop");
                        Key_ExtEvent<? extends ExtEvent> fkey = new Key_ExtEvent<ExtEvent>();
                        ExtEventBUYSCREDITMASTER check = new ExtEventBUYSCREDITMASTER();
                        System.out.println("Due Date: " + rst.getString(4));
                        check.setColumn("DUEDATES", rst.getString(4));
                        check.setColumn("PERDDAYS", rst.getString(5));
                        check.setColumn("INTERRAT", rst.getString(6));
                        check.setColumn("INTERAMT", rst.getString(7) + " " + rst.getString(8));
                        check.setColumn("FIXCHRGS", rst.getString(9) + " " + rst.getString(10));
                        check.setColumn("TOTINTCH", rst.getString(11) + " " + rst.getString(12));
                        check.setColumn("AMTREMIT", rst.getString(13) + " " + rst.getString(14));
                        check.setColumn("OUTPYREF", rst.getString(15));
                        check.setColumn("DATEREMC", rst.getString(16));
                        check.setNewKey();
                        check.setFk(fkey);
                        check.setSequence(count);

                        ExtEventBUYSCREDITMASTEREntityWrapper project = new ExtEventBUYSCREDITMASTEREntityWrapper(check,
                                    getDriverWrapper());
                        addNewExtEventBUYSCREDITMASTER(project);

                        count++;
                        System.out.println("Buyers Credit Grid - Count of Data:" + count);
                  }

            } catch (Exception e) {
                  e.printStackTrace();

            } finally {
                  try {
                        if (rs != null)
                              rs.close();
                        if (ps != null)
                              ps.close();
                        if (con != null)
                              con.close();

                  } catch (SQLException e) {

                        e.printStackTrace();
                  }
            }
      }
      
      //ebg expiry event
      public void onEBGACCEPIMPGUAEXPclayAutoSubmit() {
            
            System.out.println("Entering EBG Accept API");
 
            String strService = "EBGAcceptRejectAPI";
            String url = "";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strService + "'");
            EXTGENCUSTPROP CodeURL = queryLog.getUnique();
            if (CodeURL != null) {
 
                  url = CodeURL.getPropval();
            } else {
 
            }
 
            String response = null;
            // String url = "http://10.128.232.200:8092/wiseconnect/ebg/v1/event-response";
            String masRefNo = getDriverWrapper().getEventFieldAsText("MST", "r", "");
            String eventRefNo = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
 
            String request = "{\n" + "    \"txn_id\": \"" + masRefNo.concat(eventRefNo) + "\",\n" + " \"ebg_ref_no\": \""
                        + masRefNo + "\",\n" + "      \"event\": \"Accepted\",\n" + "     \"rmrks\": \"Accept\"\n}";
 
            StringBuffer buffer = new StringBuffer();
            PostMethod post = new PostMethod(url);
 
            try {
                  System.out.println("EBG Accept API Requst: " + request);
                  StringRequestEntity requestEntity = new StringRequestEntity(request, "application/json", "utf-8");
                  post.setRequestEntity(requestEntity);
                  HttpClient httpclient = new HttpClient();
 
                  int result = httpclient.executeMethod(post);
 
                  if (result != 200) {
                        throw new Exception(" EBG Accept API Error " + result);
                  }
                  response = post.getResponseBodyAsString();
                  System.out.println(" EBG Accept API Response From Bank-->\n" + response);
 
                  System.out.println("Exiting  EBG Accept API");
            } catch (Exception e) {
                  System.out.println("Exception in  EBG Accept API:- " + e);
                  e.printStackTrace();
            } finally {
                  post.releaseConnection();
            }
      }
 
      public void onEBGREJECIMPGUAEXPclayAutoSubmit() {
 
            System.out.println("Entering EBG Reject API");
 
            String strService = "EBGAcceptRejectAPI";
            String url = "";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strService + "'");
            EXTGENCUSTPROP CodeURL = queryLog.getUnique();
            if (CodeURL != null) {
 
                  url = CodeURL.getPropval();
            } else {
 
            }
 
            String response = null;
            // String url = "http://10.128.232.200:8092/wiseconnect/ebg/v1/event-response";
            String masRefNo = getDriverWrapper().getEventFieldAsText("MST", "r", "");
            String eventRefNo = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
 
            String request = "{\n" + "    \"txn_id\": \"" + masRefNo.concat(eventRefNo) + "\",\n" + " \"ebg_ref_no\": \""
                        + masRefNo + "\",\n" + "      \"event\": \"Rejected\",\n" + "     \"rmrks\": \"Reject\"\n}";
 
            StringBuffer buffer = new StringBuffer();
            PostMethod post = new PostMethod(url);
 
            try {
                  System.out.println("EBG Reject API Requst: " + request);
                  StringRequestEntity requestEntity = new StringRequestEntity(request, "application/json", "utf-8");
                  post.setRequestEntity(requestEntity);
                  HttpClient httpclient = new HttpClient();
 
                  int result = httpclient.executeMethod(post);
 
                  if (result != 200) {
                        throw new Exception(" EBG Reject API Error " + result);
                  }
                  response = post.getResponseBodyAsString();
                  System.out.println(" EBG Reject API Response From Bank-->\n" + response);
 
                  System.out.println("Exiting  EBG Reject API");
            } catch (Exception e) {
                  System.out.println("Exception in  EBG Reject API:- " + e);
                  e.printStackTrace();
            } finally {
                  post.releaseConnection();
            }
      }

}