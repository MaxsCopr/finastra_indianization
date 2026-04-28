package com.misys.tiplus2.customisation.pane;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import org.apache.log4j.Logger;

import com.bs.theme.transport.client.ThemeTransportClient;
import com.misys.tiplus2.apps.ti.kernel.extpm.entity.ExtEvent;
import com.misys.tiplus2.apps.ti.kernel.extpm.entity.Key_ExtEvent;
import com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP;
import com.misys.tiplus2.customisation.entity.ExtEventAccountRealisation;
import com.misys.tiplus2.customisation.entity.ExtEventAccountRealisationEntityWrapper;
import com.misys.tiplus2.customisation.entity.ExtEventAdvanceTableEntityWrapper;
import com.misys.tiplus2.customisation.entity.ExtEventBillReferenceEntityWrapper;
import com.misys.tiplus2.customisation.entity.ExtEventCounterGuranteeEntityWrapper;
import com.misys.tiplus2.customisation.entity.ExtEventLienMarkingEntityWrapper;
import com.misys.tiplus2.customisation.entity.ExtEventLoanDetails;
import com.misys.tiplus2.customisation.entity.ExtEventLoanDetailsEntityWrapper;
import com.misys.tiplus2.customisation.extension.ConnectionMaster;
import com.misys.tiplus2.enigma.customisation.AdhocQuery;
import com.misys.tiplus2.enigma.customisation.control.ExtendedHyperlinkControlWrapper;
import com.misys.tiplus2.enigma.lang.datatype.EnigmaArray;
import com.misys.tiplus2.foundations.lang.logging.Loggers;

public class OutGurPane extends EventPane {
      // private static final Logger logger =
      // Logger.getLogger(ConnectionMaster.class.getName());
      private static final Logger LOG = LoggerFactory.getLogger(OutGurPane.class);
      Connection con, con1 = null;
      PreparedStatement dmsp, ps, psd, ps3, ps4, psd1, account = null;
      PreparedStatement dmsp1, ps1, ps2 = null;
      ResultSet dmsr, drms1, rs, rst, rs3, rs4, rst1 = null;
      ResultSet dmsr1, rs1, rs2 = null;
      String swachhCharge = "";
      String serviceTax = "";
      ConnectionMaster cm = new ConnectionMaster();
      PreparedStatement ttRatePS = null;
      ResultSet ttRateRS = null;

      public void onEBGACCEPOGTINVOCclayAutoSubmit() {

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

      public void onEBGREJECOGTINVOCclayAutoSubmit() {

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

      public void onSTATECODOGTISSclay_2_2AutoSubmit() {

            System.out.println("Entering Stamp Duty");

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
                              System.out.println("Amount OUTGURPANE=" +amount);
                              double findStampDuty = findStampDuty(amount, percentage);
                              System.out.println("Value OF findStampDuty in ISS RJ=" +findStampDuty);
                              if (findStampDuty >= 25000) {
                                    stampDuty = 25000.00;
                              } else {
                                    stampDuty = findStampDuty;
                              }

                        } else if (eventRef.substring(0, 3).equalsIgnoreCase("AMD")) {
                              double percentage = 0.25;
                              System.out.println("Amount OUTGURPANE=" +amount);
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

                  } else if (stateCode.trim().equalsIgnoreCase("UK")) {
                        double percentage = 0.5;
                        System.out.println("Amount OUTGURPANE=" +amount);
                        double findStampDuty = findStampDuty(amount, percentage);
                        System.out.println("Value OF findStampDuty in UK=" +findStampDuty);
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
      public void onSTAMPOGTISSclay_2_2Button() {

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

      public static String randomCorrelationId() {
            // Loggers.general().info(LOG,"randomCorrelationId generate");
            return UUID.randomUUID().toString();
      }

      public void onFETCHIFSCEGTAMDclayButton() {
            // Loggers.general().info(LOG,"IFSC Inward Amend");
            FETCHIFSC();
            // FETCHIFSCIncoming();
      }

      public void onFETCHIFSCEGTADJclayButton() {
            // Loggers.general().info(LOG,"IFSC Inward Amend");
            FETCHIFSC();
            // FETCHIFSCIncoming();
      }

      public void onFETCHIFSCOGTISSclayButton() {
            // Loggers.general().info(LOG,"IFSC Outward Issue");
            FETCHIFSC();
      }

      public void onFETCHIFSCIMPGUAADJclayButton() {
            // Loggers.general().info(LOG,"IFSC outward Adjust");
            FETCHIFSC();
      }

      public void onFETCHIFSCIMPGUAAMDclayButton() {
            // Loggers.general().info(LOG,"IFSC outward Amend");
            FETCHIFSC();
      }

      public void onFETCHIFSCINWRMTCUSclayButton() {
            // Loggers.general().info(LOG,"IFSC outward Amend");
            FETCHIFSC();
      }

      public void onFETCHIFSCOUTRMTCUSclayButton() {
            // Loggers.general().info(LOG,"IFSC outward Amend");
            FETCHIFSC();
      }

      public void onMERCHATOUTRMTCUSclayButton() {
            // String masRefNo = getDriverWrapper().getEventFieldAsText("MST", "r",
            // "");
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

            String mercht = getDriverWrapper().getEventFieldAsText("cARQ", "l", "").toString();
            String relrefno = getREMERREF();
            String adremno = getINWREMNO();

            int dmT = 0;
            try {
                  // //Loggers.general().info(LOG,"enter into try");

                  String dms = "SELECT DISTINCT DECODE(trim(mas.MASTER_REF) ,NULL ,0,1)AS REMERREF FROM master mas, BASEEVENT bev, EXTEVENT exte, PRODTYPE prod WHERE mas.KEY97 = bev.MASTER_KEY AND bev.KEY97 = exte.EVENT AND mas.PRODTYPE = prod.KEY97 AND mas.MASTER_REF ='"
                              + relrefno + "'";
                  // Loggers.general().info(LOG,"Master ref no valid for outward remittance" +
                  // dms);
                  if (dailyval_Log.equalsIgnoreCase("YES")) {

                        Loggers.general().info(LOG, "Master ref no valid for outward remittance" + dms);

                  }
                  Connection con = ConnectionMaster.getConnection();
                  ps = con.prepareStatement(dms);
                  rs = ps.executeQuery();
                  while (rs.next()) {
                        dmT = rs.getInt(1);
                        // Loggers.general().info(LOG,"AFTER GET THE VALUE outward remittance" +
                        // dmT);
                        if (dmT > 0 && mercht.equalsIgnoreCase("Y")) {
                              try {
                                    // //Loggers.general().info(LOG,"enter into try");

                                    String query_dms = "SELECT TO_CHAR(ext.MERDUET,'DD/MM/YY') AS MERDUET FROM master mas, baseevent bev, extevent ext WHERE mas.key97 = bev.master_key AND bev.key97 = ext.event AND bev.refno_pfix IN ('AMD','ADJ','ISS','CLM','CRE') AND mas.master_ref ='"
                                                + relrefno + "'";
                                    // Loggers.general().info(LOG,"values fetching outward
                                    // remittance" + query_dms);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {

                                          Loggers.general().info(LOG, "values fetching outward remittance" + query_dms);

                                    }
                                    con = ConnectionMaster.getConnection();
                                    ps = con.prepareStatement(query_dms);
                                    rs = ps.executeQuery();
                                    while (rs.next()) {
                                          String merdate = rs.getString(1);
                                          // setMERDUET(merdate);
                                          // Loggers.general().info(LOG,"AFTER GET THE VALUE outward
                                          // remittance" + dmT);
                                    }

                              } catch (Exception e) {
                                    // Loggers.general().info(LOG,e.getMessage());
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {

                                          Loggers.general().info(LOG, e.getMessage());

                                    }
                              }

                        } else {
                              // Loggers.general().info(LOG,"Merchant trade is not tickec or
                              // master not valide outward remittance --->" + dmT +
                              // mercht);
                        }

                  }
            } catch (Exception e) {
                  // Loggers.general().info(LOG,"Merchanting details outward remittance--->" +
                  // e.getMessage());
                  if (dailyval_Log.equalsIgnoreCase("YES")) {

                        Loggers.general().info(LOG, "Merchanting details outward remittance--->" + e.getMessage());

                  }
            }
            try {
                  // inward renittance
                  String query_adv = "SELECT DECODE(trim(mas.MASTER_REF) ,NULL ,0,1)AS ADOUTREM FROM master mas, BASEEVENT bev, EXTEVENT exte, PRODTYPE prod WHERE mas.KEY97 = bev.MASTER_KEY AND bev.KEY97 = exte.EVENT AND mas.PRODTYPE = prod.KEY97 AND prod.CODE in('XAR') AND mas.MASTER_REF ='"
                              + adremno + "'";
                  // Loggers.general().info(LOG,"Advance rem no valid for outward remittance"
                  // + query_adv);
                  if (dailyval_Log.equalsIgnoreCase("YES")) {

                        Loggers.general().info(LOG, "Advance rem no valid for outward remittance" + query_adv);

                  }
                  Connection con = ConnectionMaster.getConnection();
                  ps = con.prepareStatement(query_adv);
                  rs = ps.executeQuery();
                  while (rs.next()) {
                        dmT = rs.getInt(1);
                        // Loggers.general().info(LOG,"query_adv AFTER GET THE VALUE outward
                        // remittance" + dmT);
                        if (dmT > 0 && mercht.equalsIgnoreCase("Y")) {
                              try {

                                    String query_dms = "SELECT TO_CHAR(cpm.RCV_DATE,'DD/MM/YY') AS REV_DATE FROM master mas, CPAYMASTER cpm, PARTYDTLS prt, TIDATAITEM tid WHERE mas.key97 = tid.MASTER_KEY AND tid.KEY97 = prt.KEY97 AND mas.KEY97 = cpm.KEY97 AND prt.ROLE = 'BEN' AND mas.master_ref ='"
                                                + adremno + "'";
                                    // Loggers.general().info(LOG,"Advance rem no values fetching
                                    // outward remittance" + query_dms);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {

                                          Loggers.general().info(LOG,
                                                      "Advance rem no values fetching outward remittance" + query_dms);

                                    }
                                    con = ConnectionMaster.getConnection();
                                    ps = con.prepareStatement(query_dms);
                                    rs = ps.executeQuery();
                                    while (rs.next()) {
                                          String recdate = rs.getString(1);

                                          // Loggers.general().info(LOG,"Inward received date fetching
                                          // " + recdate);

                                          // Loggers.general().info(LOG,"Inward received systemDate "
                                          // + systemDate);
                                          SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                                          Calendar cal = Calendar.getInstance();
                                          int gra = 270;
                                          // int gra = tnr + gr;
                                          // //Loggers.general().info(LOG,"tenor days and 90 daye "
                                          // + gra);

                                          try {
                                                cal.setTime(sdf.parse(recdate));
                                                // //Loggers.general().info(LOG,"expdate in
                                                // issue------->
                                                // ");
                                                cal.add(Calendar.DATE, gra);
                                                String output = sdf.format(cal.getTime());
                                                // Loggers.general().info(LOG,"output----->" + output);
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                      Loggers.general().info(LOG, "output----->" + output);

                                                }
                                                // setMERDUET(output);

                                          } catch (Exception e) {
                                                // Loggers.general().info(LOG,"merchat value date --->"
                                                // + e.getMessage());
                                          }

                                    }
                              } catch (Exception e) {
                                    // Loggers.general().info(LOG,"merchat received value date --->"
                                    // + e.getMessage());
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {

                                          Loggers.general().info(LOG, "merchat received value date --->" + e.getMessage());

                                    }

                              }

                        } else {
                              // Loggers.general().info(LOG, "Merchant trade is not ticked or
                              // master not valide outward remittance --->" + dmT +
                              // mercht);
                        }

                  }

            } catch (Exception e) {
                  // Loggers.general().info(LOG,"Merchanting details outward remittance--->" +
                  // e.getMessage());
                  if (dailyval_Log.equalsIgnoreCase("YES")) {

                        Loggers.general().info(LOG, "Merchanting details outward remittance--->" + e.getMessage());

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

      public void onMULBUYEROGTISSclayButton() {

            // Loggers.general().info(LOG,"MULBUYER Button");
            try {
                  MULBUYER();
            } catch (Exception e) {
                  // Loggers.general().info(LOG,"Connection Failed! Check output
                  // console");
                  e.printStackTrace();
            }

      }

      public void onMULBUYERIMPGUAISSONcalyButton() {

            // Loggers.general().info(LOG,"MULBUYER Button");
            try {
                  MULBUYER();
            } catch (Exception e) {
                  // Loggers.general().info(LOG,"Connection Failed! Check output
                  // console");
                  e.printStackTrace();
            }

      }

      public void onfetchOGTISSclay_2_2Button() {
            try {
                  System.out.println("inside buyer fetch button");
                  MULBUYER();
            } catch (Exception e) {
                  // Loggers.general().info(LOG,"Connection Failed! Check output
                  // console");
                  e.printStackTrace();
            }
      }

      public void MULBUYER() {

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
                  String master = "";
                  String event = "";
                  // String drawer=getDriverWrapper().getEventFieldAsText("DRW", "p", "n");
                  // String beneficiary=getDriverWrapper().getEventFieldAsText("BEN", "p", "n");
                  EnigmaArray<ExtEventBillReferenceEntityWrapper> list = getExtEventBillReferenceData();
                  for (int i = 0; i < list.getSize().intValue(); i++) {
                        Iterator<ExtEventBillReferenceEntityWrapper> iterator1 = list.iterator();
                        // Getting the table Values
                        while (iterator1.hasNext()) {
                              ExtEventBillReferenceEntityWrapper fdwrapper = (ExtEventBillReferenceEntityWrapper) iterator1
                                          .next();
                              con = ConnectionMaster.getConnection();
                              String billNumber = fdwrapper.getBILLNUMB().trim();
                              // billNumbeString billno = getBUYBILLR();
                              master = billNumber.substring(0, 16);
                              event = billNumber.substring(16);
                              // if (event.substring(0, 3).equalsIgnoreCase("CLM")) {
                              System.out.println("Multiple Buyer bill reference no " + billNumber + " " + master + " " + event);
                              String query = "select DISTINCT TRIM(exte.ORGIN),TRIM(exte.PORLOD), TO_CHAR(TO_DATE(exte.DASHIP,'dd-mm-yy'),'yy-mm-dd') AS DATESHIP,TRIM(exte.PORTCOD) AS PORTOFDIS,TRIM(exte.PLCDESTI) AS PLCDESTI,TRIM(exte.PLCOFREC), TRIM(exte.TRNDOCNO),TRIM(exte.TRANSPOR),exte.BENEFI,exte.vessel from EXTEVENT exte,master mas ,baseevent bev where mas.key97=bev.master_key and bev.key97=exte.event and  mas.master_ref='"
                                          + master + "'" + " and trim(BEV.REFNO_PFIX ||LPAD(BEV.REFNO_SERL,3,0))='" + event + "'";
                              System.out.println("Multiple Buyer bill reference" + query);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {

                                    System.out.println("Multiple Buyer bill reference no query" + query);

                              }
                              con1 = ConnectionMaster.getConnection();
                              psd = con1.prepareStatement(query);
                              rst = psd.executeQuery();
                              while (rst.next()) {
                                    System.out.println("Enter into while --->");
                                    String origin = rst.getString(1);
                                    String portOfLoading = rst.getString(2);
                                    String shipment_date = rst.getString(3);
                                    String portOfDischarge = rst.getString(4);
                                    String placeOfDesti = rst.getString(5);
                                    String placeOfReceipt = rst.getString(6);
                                    String trandocno = rst.getString(7);
                                    String transpor = rst.getString(8);
                                    String capitalGoods = rst.getString(9);
                                    String vessel = rst.getString(10);

                                    fdwrapper.setSHIFMCN(vessel);
                                    fdwrapper.setBUYGOODS(transpor);
                                    fdwrapper.setSHTOCON(trandocno);
                                    fdwrapper.setSHPDA(shipment_date);
                                    fdwrapper.setPCODE(portOfLoading + " ," + placeOfReceipt);
                                    fdwrapper.setPCODES(portOfDischarge + " ," + placeOfDesti);
                                    fdwrapper.setORIGIN(origin);
                                    fdwrapper.setCAPITALG(capitalGoods);
                              }
                              if (event.substring(0, 3).equalsIgnoreCase("CRE")) {
                                    String query1 = "select inv.INVNUMB, inv.INDATE from master mas,baseevent bev,extevent ext,exteventind inv "
                                                + "where mas.key97=bev.MASTER_KEY " + "and bev.key97=ext.event "
                                                + "and ext.key29=inv.fk_event " + "and mas.MASTER_REF='" + master + "'"
                                                + " and trim(BEV.REFNO_PFIX ||LPAD(BEV.REFNO_SERL,3,0))='" + event + "'";

                                    System.out.println("INVOICE SCRIPT Buyer bill reference" + query1);
                                    con1 = ConnectionMaster.getConnection();
                                    ps1 = con1.prepareStatement(query1);
                                    rs1 = ps1.executeQuery();
                                    while (rs1.next()) {
                                          System.out.println("Enter into invoice while1 --->" + rs1.getString(1));
                                          String invoice = rs1.getString(1);
                                          String invoiceDate = rs1.getString(2);

                                          fdwrapper.setINVOCENM(invoice);
                                          fdwrapper.setINVOCEDT(invoiceDate);

                                    }

                              }

                              else if (event.substring(0, 3).equalsIgnoreCase("CLM")) {
                                    String query2 = "select inv.INNUM, inv.INDTE from master mas,baseevent bev,extevent ext,exteventilc inv "
                                                + "where mas.key97=bev.MASTER_KEY " + "and bev.key97=ext.event "
                                                + "and ext.key29=inv.fk_event " + "and mas.MASTER_REF='" + master + "'"
                                                + " and trim(BEV.REFNO_PFIX ||LPAD(BEV.REFNO_SERL,3,0))='" + event + "'";

                                    System.out.println("INVOICE SCRIPT Buyer bill reference" + query2);
                                    con1 = ConnectionMaster.getConnection();
                                    ps1 = con1.prepareStatement(query2);
                                    rs1 = ps1.executeQuery();
                                    while (rs1.next()) {
                                          System.out.println("Enter into invoice while1 --->" + rs1.getString(1));
                                          String invoice = rs1.getString(1);
                                          String invoiceDate = rs1.getString(2);

                                          fdwrapper.setINVOCENM(invoice);
                                          fdwrapper.setINVOCEDT(invoiceDate);

                                    }
                              }
//                            else {
//                                  // Loggers.general().info(LOG,"multiple Buyer master reference
//                                  // no" + billNumber);
//                                  String query = "SELECT TRIM(exte.SHIPFTO) AS SHIPFTO, TRIM(exte.SHIPTOP) AS SHIPTOP, TRIM(TO_CHAR(exte.DASHIP,'DD/MM/YY')) AS DATESHIP, TRIM(exte.PORTCOD) AS PORTCOD, TRIM(exte.PORTDESC) AS PORTDESC, usop.NAME FROM master mas, BASEEVENT bev, USEROPTN usop, EXTEVENT exte, PRODTYPE prod WHERE mas.KEY97 = bev.MASTER_KEY AND bev.KEY97 = exte.EVENT AND mas.PRODTYPE = prod.KEY97 and mas.USEROPTN1 = usop.KEY29 AND bev.REFNO_PFIX = 'CRE' AND prod.CODE ='ICF' AND mas.MASTER_REF ='"
//                                              + billNumber + "'";
//                                  // Loggers.general().info(LOG,"Multiple Buyer master reference
//                                  // no query" + query);
//                                  if (dailyval_Log.equalsIgnoreCase("YES")) {
//
//                                      System.out.println("Multiple Buyer master reference no query" + query);
//
//                                  }
//                                  con1 = ConnectionMaster.getConnection();
//                                  psd = con1.prepareStatement(query);
//                                  rst = psd.executeQuery();
//                                  while (rst.next()) {
//                                      System.out.println("Enter into while --->");
//                                        String shipment_from = rst.getString(1);
//                                        String shipment_to = rst.getString(2);
//                                        String shipment_date = rst.getString(3);
//                                        String shipment_port = rst.getString(4);
//                                        String shipment_portdesc = rst.getString(5);
//                                        String shipment_goods = rst.getString(6);
//                                        fdwrapper.setSHIFMCN(shipment_from);
//                                        fdwrapper.setSHTOCON(shipment_to);
//                                        fdwrapper.setSHPDA(shipment_date);
//                                        fdwrapper.setPCODE(shipment_port);
//                                        fdwrapper.setPCODES(shipment_portdesc);
//                                        fdwrapper.setBUYGOODS(shipment_goods);
//                                  }
//
//                            }
                              if (event.substring(0, 3).equalsIgnoreCase("CRE")) {
                                    String query2 = "SELECT P.ADDRESSF,c.DRAWER_PTY FROM MASTER MAS ,PARTYDTLS P ,BASEEVENT BEV,collmaster c WHERE "
                                                + "MAS.KEY97=BEV.MASTER_KEY AND " + "c.key97=mas.key97 and "
                                                + "c.DRAWER_PTY=p.key97 AND " + " mas.MASTER_REF='" + master + "'"
                                                + " and trim(BEV.REFNO_PFIX ||LPAD(BEV.REFNO_SERL,3,0))='" + event + "'";

                                    con1 = ConnectionMaster.getConnection();
                                    ps2 = con1.prepareStatement(query2);
                                    rs2 = ps2.executeQuery();
                                    while (rs2.next()) {
                                          System.out.println("Enter into export name while1 --->");
                                          String exporter = rs2.getString(1);
                                          setDRAWNAM(exporter);
                                    }
                              }
                              if (event.substring(0, 3).equalsIgnoreCase("CLM")) {
                                    String query2 = "SELECT P.ADDRESSF,lc.ben_pty FROM MASTER MAS ,PARTYDTLS P ,BASEEVENT BEV,lcmaster lc WHERE "
                                                + "MAS.KEY97=BEV.MASTER_KEY AND " + "lc.key97=mas.key97 and "
                                                + "lc.ben_pty=p.key97 AND " + " mas.MASTER_REF='" + master + "'"
                                                + " and trim(BEV.REFNO_PFIX ||LPAD(BEV.REFNO_SERL,3,0))='" + event + "'";

                                    con1 = ConnectionMaster.getConnection();
                                    ps2 = con1.prepareStatement(query2);
                                    rs2 = ps2.executeQuery();
                                    while (rs2.next()) {
                                          System.out.println("Enter into export name while1 --->");
                                          String exporter = rs2.getString(1);
                                          setDRAWNAM(exporter);
                                    }
                              }
                        }
                  }

            } catch (Exception e) {
                  // Loggers.general().info(LOG,"Exception is Multiple Buyer bill reference" +
                  // e.getMessage());

                  if (dailyval_Log.equalsIgnoreCase("YES")) {

                        Loggers.general().info(LOG, "Exception is Multiple Buyer bill reference" + e.getMessage());

                  }
            } finally {

                  try {
                        if (rst != null)
                              rs1.close();
                        if (psd != null)
                              ps1.close();
                        if (rs2 != null)
                              rs2.close();
                        if (ps2 != null)
                              ps2.close();
                        if (con1 != null)
                              con.close();
                  } catch (SQLException e) {
                        // Loggers.general().info(LOG,"Connection Failed! Check output
                        // console");
                        e.printStackTrace();
                  }

            }
//          return value;

      }

      public void onFETCHIFSCIGTISSclayButton() {
            FETCHIFSC();

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
      // // setRCIFSCIN(recifsc);
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
                        con = ConnectionMaster.getConnection();
                        ps1 = con.prepareStatement(query);
                        rs1 = ps1.executeQuery();
                        while (rs1.next()) {
                              senderIfsc = rs1.getString(1);
                        }
                        // Loggers.general().info(LOG,"Sender IFSC code----->" + senderIfsc);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {

                              Loggers.general().info(LOG, "Sender IFSC code----->" + senderIfsc);

                        }
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

      public void onNOSTROOUTWREMCorrespondenceLayButton() {

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
                  String nostref_MT103102 = getNOSTMT().trim();
                  String nostref_MT940950 = getNOSTRM().trim();
                  String masReference = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                  String eventCode = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
                  String Credit_AmountMT103202 = "";
                  String Credit_curMT103202 = "";
                  String ValuedateMT103202 = "";
                  String Credit_AmountMT940950 = "";
                  String Credit_curMT940950 = "";
                  String CreditAccountMT940950 = "";
                  String msg940950_1 = "";
                  String msg940950_2 = "";
                  String msg940950_concat = "";
                  String swift_type = "";
                  con = ConnectionMaster.getConnection();
                  if (nostref_MT103102.length() > 0) {
                        try {

                              String query = "SELECT MT103_MT202_AMOUNT AS CreditMT103202, MT103_MT202_CURRENCY_CODE AS CreditCurMT103202, TO_CHAR(MT103_MT202_VALUE_DATE,'DD/MM/YY') AS ValuedateMT103202, MT940_MT950_AMOUNT AS CreditAmountMT940950, MT940_MT950_CURRENCY_CODE AS CreditCurMT940950, MT940_MT950_ACCOUNT_NO AS CreditAccountMT940950, MT940_MT950_IND_TEXT, MT940_MT950_ACC_OWNER_INFO,Trim(SWIFT_TYPE),MT940_MT950_REFERENCE_NUMBER FROM ETTV_NOSTRO_MT103_MT202_TBL WHERE MT103_MT202_REFERENCE_NUMBER='"
                                          + nostref_MT103102 + "'";
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG, "Nostro ETTV_NOSTRO_MT103_MT202_TBL value query " + query);
                              }

                              ps = con.prepareStatement(query);
                              rs = ps.executeQuery();
                              if (rs.next()) {

                                    Credit_AmountMT103202 = rs.getString(1);
                                    Credit_curMT103202 = rs.getString(2);

                                    // setNOSTAMT(Credit_AmountMT103202 + " " + Credit_curMT103202);
                                    setNOSTOUT(Credit_AmountMT103202 + " " + Credit_curMT103202);
                                    ValuedateMT103202 = rs.getString(3);
                                    setNOSTDAT(ValuedateMT103202);
                                    Credit_AmountMT940950 = rs.getString(4);
                                    Credit_curMT940950 = rs.getString(5);

                                    // setPOOLAMT(Credit_AmountMT940950 + " " + Credit_curMT940950);
                                    CreditAccountMT940950 = rs.getString(6);

                                    setNOSTACC(CreditAccountMT940950);
                                    /*
                                     * msg940950_1 = rs.getString(7); msg940950_2 = rs.getString(8);
                                     *
                                     * msg940950_concat = msg940950_1 + " \n " + msg940950_2;
                                     * setMTMESG(msg940950_concat);
                                     */
                                    swift_type = rs.getString(9);
                                    String nostref_MT940 = rs.getString(10);
                                    setNOSTRM(nostref_MT940);

                                    BigDecimal totalVal1 = new BigDecimal(Credit_AmountMT103202);

                                    ConnectionMaster connectionMaster1 = new ConnectionMaster();
                                    double divideByDecimal1 = connectionMaster1.getDecimalforCurrency(Credit_curMT103202);
                                    BigDecimal divideByBig1 = new BigDecimal(divideByDecimal1);
                                    BigDecimal totalValue1 = totalVal1.divide(divideByBig1);

                                    // BigDecimal totalValue1 = new BigDecimal(Credit_AmountMT103202);

                                    BigDecimal totalVal2 = new BigDecimal(Credit_AmountMT940950);

                                    // ConnectionMaster connectionMaster1 = new ConnectionMaster();
                                    double divideByDecimal2 = connectionMaster1.getDecimalforCurrency(Credit_curMT940950);
                                    BigDecimal divideByBig2 = new BigDecimal(divideByDecimal2);
                                    BigDecimal totalValue2 = totalVal2.divide(divideByBig2);

                                    // =======================

                                    // =====================

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,
                                                      "Total Nostro Credit amount-940=====================-->" + totalValue1);
                                          Loggers.general().info(LOG, "poollll amount-====940=================-->" + totalValue2);

                                    }
                                    String finalVal1 = String.format("%.2f", totalValue1);
                                    String finalVal2 = String.format("%.2f", totalValue2);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG, "Final Nostro Credit amount==940=========--->" + finalVal1);
                                          Loggers.general().info(LOG, "Final poolll amount===940========--->" + finalVal2);

                                    }
                                    setNOSTAMT(finalVal1 + " " + Credit_curMT103202);

                                    setPOOLAMT(finalVal2 + " " + Credit_curMT940950);

                                    // =================================

                                    // String dms = "SELECT NVL(SUM(NOSUTLAMT),0) AS
                                    // OUTSAMOUNT FROM (SELECT SUM(ext.NOSAMT) AS NOSUTLAMT,
                                    // mas.MASTER_REF FROM master mas, BASEEVENT bas,
                                    // extevent ext WHERE mas.KEY97 =bas.MASTER_KEY AND
                                    // bas.KEY97 =ext.EVENT AND bas.STATUS IN ('i','c') AND
                                    // (mas.MASTER_REF ='"
                                    // + masReference + "' AND trim(bas.REFNO_PFIX
                                    // ||LPAD(bas.REFNO_SERL,3,0)) !='" + eventCode
                                    // + "') AND ext.NOSTMT ='" + nostref_MT103102
                                    // + "' GROUP BY mas.MASTER_REF UNION ALL SELECT
                                    // SUM(ext.NOSAMT) AS NOSUTLAMT, mas.MASTER_REF FROM
                                    // master mas, BASEEVENT bas, extevent ext WHERE
                                    // mas.KEY97 =bas.MASTER_KEY AND bas.KEY97 =ext.EVENT
                                    // AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF !='"
                                    // + masReference + "' AND trim(bas.REFNO_PFIX
                                    // ||LPAD(bas.REFNO_SERL,3,0)) !='" + eventCode
                                    // + "') AND ext.NOSTMT ='" + nostref_MT103102
                                    // + "' GROUP BY mas.MASTER_REF ) NOSRTOAMOUNT";

                                    String dms = "SELECT NVL(SUM(NOSUTLAMT),0) AS OUTSAMOUNT FROM (SELECT SUM(ext.NOSAMT) AS NOSUTLAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, extevent ext WHERE mas.KEY97 =bas.MASTER_KEY AND bas.KEY97 =ext.EVENT AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF ='"
                                                + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) !='" + eventCode
                                                + "') AND ext.NOSTMT ='" + nostref_MT103102
                                                + "' GROUP BY mas.MASTER_REF UNION ALL SELECT SUM(ext.NOSAMT) AS NOSUTLAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, extevent ext WHERE mas.KEY97 =bas.MASTER_KEY AND bas.KEY97 =ext.EVENT AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF !='"
                                                + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) !='" + eventCode
                                                + "') AND ext.NOSTMT ='" + nostref_MT103102
                                                + "' GROUP BY mas.MASTER_REF  UNION ALL SELECT SUM(ext.NOSAMT) AS NOSUTLAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, extevent ext WHERE mas.KEY97 =bas.MASTER_KEY AND bas.KEY97 =ext.EVENT AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF !='"
                                                + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) ='" + eventCode
                                                + "') AND ext.NOSTMT ='" + nostref_MT103102
                                                + "' GROUP BY mas.MASTER_REF  ) NOSRTOAMOUNT";

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG, "Nostro outsanding amount--->" + dms);

                                    }
                                    ps = con.prepareStatement(dms);
                                    rs = ps.executeQuery();
                                    while (rs.next()) {
                                          String nostOut = rs.getString(1).trim();
                                          BigDecimal nostOutBig = new BigDecimal(nostOut);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG, "Result of Nostro outsanding amount--->" + nostOutBig);

                                          }
                                          BigDecimal CreditBig = new BigDecimal(Credit_AmountMT940950);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG, "Nostro Credit amount--->" + CreditBig);

                                          }
                                          BigDecimal totalVal = CreditBig.subtract(nostOutBig);

                                          ConnectionMaster connectionMaster = new ConnectionMaster();
                                          double divideByDecimal = connectionMaster.getDecimalforCurrency(Credit_curMT940950);
                                          BigDecimal divideByBig = new BigDecimal(divideByDecimal);
                                          BigDecimal totalValue = totalVal.divide(divideByBig);

                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG, "Total Nostro Credit amount--->" + totalValue);

                                          }
                                          String finalVal = String.format("%.2f", totalValue);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG, "Final Nostro Credit amount--->" + finalVal);

                                          }
                                          if (totalValue.compareTo(BigDecimal.ZERO) >= 0) {
                                                setNOSTOUT(finalVal + " " + Credit_curMT940950);
                                          } else {
                                                finalVal = "0";
                                                setNOSTOUT(finalVal + " " + Credit_curMT940950);

                                          }

                                    }

                                    String query_940 = "SELECT TRIM( 'MessageDetails:'||SUBSTR(MESSAGE_DATA,1, INSTR(MESSAGE_DATA,':61:')-1 ) )AS MSGDATA,TRIM('TAG 61:'||IND_TEXT) AS IND_TEXT,TRIM('TAG 86:'||ACC_OWNER_INFO) AS ACC_OWNER_INFO FROM ETT_NOSTRO_UTILITY_MT940 where IND_NOS_CUS_REFNO='"
                                                + nostref_MT940 + "' or IND_NOS_BANK_REFNO='" + nostref_MT940 + "'";
                                    //// Loggers.general().info(LOG,"Nostro swift_type is
                                    //// query_103
                                    //// " + query_103);
                                    ps3 = con.prepareStatement(query_940);
                                    rs3 = ps3.executeQuery();
                                    while (rs3.next()) {
                                          String msg1 = rs3.getString(1);
                                          String msg2 = rs3.getString(2);
                                          String msg3 = rs3.getString(3);
                                          String fullmsg = msg1 + " \n " + msg2 + " \n " + msg3;
                                          setMTMESG(fullmsg);
                                    }
                                    String query_950 = "SELECT TRIM( 'MessageDetails:'||SUBSTR(MESSAGE_DATA,1, INSTR(MESSAGE_DATA,':61:')-1 ) )AS MSGDATA,TRIM('TAG 61:'||IND_TEXT) AS IND_TEXT FROM ETT_NOSTRO_UTILITY_MT950 where IND_NOS_CUS_REFNO='"
                                                + nostref_MT940 + "' or IND_NOS_BANK_REFNO='" + nostref_MT940 + "'";
                                    //// Loggers.general().info(LOG,"Nostro swift_type is
                                    //// query_103
                                    //// " + query_103);
                                    ps4 = con.prepareStatement(query_950);
                                    rs4 = ps4.executeQuery();
                                    while (rs4.next()) {
                                          String msg1 = rs4.getString(1);
                                          String msg2 = rs4.getString(2);

                                          String fullmsg = msg1 + " \n " + msg2;
                                          setMTMESG(fullmsg);
                                    }

                                    if (swift_type.equalsIgnoreCase("103")) {
                                          String query_103 = "SELECT MESSAGE_DATA FROM ETT_NOSTRO_UTILITY_MT103 WHERE REFERENCE_NUMBER ='"
                                                      + nostref_MT103102 + "'";
                                          //// Loggers.general().info(LOG,"Nostro swift_type is
                                          //// query_103
                                          //// " + query_103);
                                          ps = con.prepareStatement(query_103);
                                          rs = ps.executeQuery();
                                          while (rs.next()) {
                                                String swift = rs.getString(1);
                                                setINWMSG(swift);
                                          }
                                    } else if (swift_type.equalsIgnoreCase("202")) {
                                          String query_202 = "SELECT MESSAGE_DATA FROM ETT_NOSTRO_UTILITY_MT202 WHERE REFERENCE_NUMBER ='"
                                                      + nostref_MT103102 + "'";
                                          //// Loggers.general().info(LOG,"Nostro swift_type is
                                          //// query_202
                                          //// " + query_202);

                                          ps = con.prepareStatement(query_202);
                                          rs = ps.executeQuery();
                                          while (rs.next()) {
                                                String swift = rs.getString(1);
                                                setINWMSG(swift);
                                          }
                                    } else {
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG, "Nostro swift_type is empty " + swift_type);
                                          }
                                          setINWMSG("");

                                    }

                              } else {

                                    setPOOLAMT("");
                                    setNOSTACC("");
                                    setMTMESG("");

                                    String queryVal = "select M.AMOUNT,M.CURRENCY_CODE, trim(TO_CHAR(M.VALUE_DATE,'DD/MM/YY')),M.MESSAGE_DATA from ETT_NOSTRO_UTILITY_MT103 M where trim(M.REFERENCE_NUMBER)='"
                                                + nostref_MT103102 + "'";
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,
                                                      "Nostro ETT_NOSTRO_UTILITY_MT103 value query else===> " + queryVal);
                                    }
                                    ps = con.prepareStatement(queryVal);
                                    rs = ps.executeQuery();

                                    while (rs.next()) {
                                          Credit_AmountMT103202 = rs.getString(1);
                                          BigDecimal Credit_Amount = new BigDecimal(Credit_AmountMT103202);

                                          Credit_curMT103202 = rs.getString(2);
                                          String Credit_AmountMT103 = String.format("%.2f", Credit_Amount);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,
                                                            "Nostro ETT_NOSTRO_UTILITY_MT103 value==1==> " + Credit_AmountMT103202);
                                                Loggers.general().info(LOG,
                                                            "Nostro ETT_NOSTRO_UTILITY_MT103 value==2==> " + Credit_AmountMT103);
                                          }
                                          setNOSTAMT(Credit_AmountMT103 + " " + Credit_curMT103202);
                                          setNOSTOUT(Credit_AmountMT103 + " " + Credit_curMT103202);
                                          ValuedateMT103202 = rs.getString(3);
                                          setNOSTDAT(ValuedateMT103202);

                                          msg940950_concat = rs.getString(4);
                                          setINWMSG(msg940950_concat);

                                          // String dms = "SELECT NVL(SUM(NOSUTLAMT),0) AS
                                          // OUTSAMOUNT FROM (SELECT SUM(ext.NOSAMT) AS
                                          // NOSUTLAMT, mas.MASTER_REF FROM master mas,
                                          // BASEEVENT bas, extevent ext WHERE mas.KEY97
                                          // =bas.MASTER_KEY AND bas.KEY97 =ext.EVENT AND
                                          // bas.STATUS IN ('i','c') AND (mas.MASTER_REF ='"
                                          // + masReference + "' AND trim(bas.REFNO_PFIX
                                          // ||LPAD(bas.REFNO_SERL,3,0)) !='" + eventCode
                                          // + "') AND ext.NOSTMT ='" + nostref_MT103102
                                          // + "' GROUP BY mas.MASTER_REF UNION ALL SELECT
                                          // SUM(ext.NOSAMT) AS NOSUTLAMT, mas.MASTER_REF FROM
                                          // master mas, BASEEVENT bas, extevent ext WHERE
                                          // mas.KEY97 =bas.MASTER_KEY AND bas.KEY97
                                          // =ext.EVENT AND bas.STATUS IN ('i','c') AND
                                          // (mas.MASTER_REF !='"
                                          // + masReference + "' AND trim(bas.REFNO_PFIX
                                          // ||LPAD(bas.REFNO_SERL,3,0)) !='" + eventCode
                                          // + "') AND ext.NOSTMT ='" + nostref_MT103102
                                          // + "' GROUP BY mas.MASTER_REF ) NOSRTOAMOUNT";

                                          String dms = "SELECT NVL(SUM(NOSUTLAMT),0) AS OUTSAMOUNT FROM (SELECT SUM(ext.NOSAMT) AS NOSUTLAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, extevent ext WHERE mas.KEY97 =bas.MASTER_KEY AND bas.KEY97 =ext.EVENT AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF ='"
                                                      + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) !='"
                                                      + eventCode + "') AND ext.NOSTMT ='" + nostref_MT103102
                                                      + "' GROUP BY mas.MASTER_REF UNION ALL SELECT SUM(ext.NOSAMT) AS NOSUTLAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, extevent ext WHERE mas.KEY97 =bas.MASTER_KEY AND bas.KEY97 =ext.EVENT AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF !='"
                                                      + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) !='"
                                                      + eventCode + "') AND ext.NOSTMT ='" + nostref_MT103102
                                                      + "' GROUP BY mas.MASTER_REF  UNION ALL SELECT SUM(ext.NOSAMT) AS NOSUTLAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, extevent ext WHERE mas.KEY97 =bas.MASTER_KEY AND bas.KEY97 =ext.EVENT AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF !='"
                                                      + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) ='"
                                                      + eventCode + "') AND ext.NOSTMT ='" + nostref_MT103102
                                                      + "' GROUP BY mas.MASTER_REF  ) NOSRTOAMOUNT";

                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG, "Nostro outsanding amount else--->" + dms);

                                          }
                                          ps = con.prepareStatement(dms);
                                          rs = ps.executeQuery();
                                          while (rs.next()) {
                                                String nostOut = rs.getString(1).trim();
                                                BigDecimal nostOutBig = new BigDecimal(nostOut);
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG, "Result of Nostro outsanding amount--->" + nostOutBig);

                                                }
                                                BigDecimal CreditBig = new BigDecimal(Credit_AmountMT103202);
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG, "Nostro Credit amount for MT103--->" + CreditBig);

                                                }
                                                ConnectionMaster connectionMaster = new ConnectionMaster();
                                                double divideByDecimal = connectionMaster.getDecimalforCurrency(Credit_curMT103202);
                                                BigDecimal divideByBig = new BigDecimal(divideByDecimal);
                                                BigDecimal creditAmount = CreditBig.multiply(divideByBig);
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,
                                                                  "Nostro Credit amount for MT103 after multifly===>" + CreditBig);

                                                }
                                                BigDecimal totalVal = creditAmount.subtract(nostOutBig);

                                                BigDecimal totalValue = totalVal.divide(divideByBig);

                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG, "Total Nostro Credit amount--->" + totalValue);

                                                }
                                                String finalVal = String.format("%.2f", totalValue);
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG, "Final Nostro Credit amount--->" + finalVal);

                                                }
                                                if (totalValue.compareTo(BigDecimal.ZERO) >= 0) {
                                                      setNOSTOUT(finalVal + " " + Credit_curMT103202);
                                                } else {
                                                      if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                            Loggers.general().info(LOG,
                                                                        "Final Nostro Credit amount ZERO in else--->" + finalVal);

                                                      }
                                                      finalVal = "0";
                                                      setNOSTOUT(finalVal + " " + Credit_curMT103202);

                                                }

                                          }

                                    }

                              }

                        } catch (Exception ee) {
                              Loggers.general().info(LOG, "Exception Inward reference 103" + ee.getMessage());
                        }
                  } else if (nostref_MT940950.length() > 0) {

                        //// Loggers.general().info(LOG,"the Nostro MT103/202 reference number
                        //// is
                        //// empty");

                        try {

                              String query = "SELECT MT103_MT202_AMOUNT AS CreditMT103202, MT103_MT202_CURRENCY_CODE AS CreditCurMT103202, TO_CHAR(MT103_MT202_VALUE_DATE,'DD/MM/YY') AS ValuedateMT103202, MT940_MT950_AMOUNT AS CreditAmountMT940950, MT940_MT950_CURRENCY_CODE AS CreditCurMT940950, MT940_MT950_ACCOUNT_NO AS CreditAccountMT940950, MT940_MT950_IND_TEXT, MT940_MT950_ACC_OWNER_INFO,Trim(SWIFT_TYPE),MT103_MT202_REFERENCE_NUMBER FROM ETTV_NOSTRO_MT103_MT202_TBL WHERE MT940_MT950_REFERENCE_NUMBER='"
                                          + nostref_MT940950 + "'";
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG, "Nostro MT940950 value query " + query);
                              }
                              ps = con.prepareStatement(query);
                              rs = ps.executeQuery();
                              while (rs.next()) {

                                    Credit_AmountMT103202 = rs.getString(1);
                                    Credit_curMT103202 = rs.getString(2);
                                    //// Loggers.general().info(LOG,"Nostro MT103202 credit amount
                                    //// and
                                    //// currency" + Credit_AmountMT103202 + " " +
                                    //// Credit_curMT103202);
                                    // setNOSTAMT(Credit_AmountMT103202 + " " + Credit_curMT103202);

                                    ValuedateMT103202 = rs.getString(3);
                                    setNOSTDAT(ValuedateMT103202);
                                    Credit_AmountMT940950 = rs.getString(4);
                                    Credit_curMT940950 = rs.getString(5);
                                    //// Loggers.general().info(LOG,"Nostro MT940950 credit amount
                                    //// and
                                    //// currency" + Credit_AmountMT940950 + " " +
                                    //// Credit_curMT940950);
                                    // setPOOLAMT(Credit_AmountMT940950 + " " + Credit_curMT940950);
                                    setNOSTOUT(Credit_AmountMT940950 + " " + Credit_curMT940950);
                                    CreditAccountMT940950 = rs.getString(6);
                                    //// Loggers.general().info(LOG,"setNOSTACC------------>" +
                                    //// getNOSTACC());
                                    setNOSTACC(CreditAccountMT940950);
                                    /*
                                     * msg940950_1 = rs.getString(7); msg940950_2 = rs.getString(8);
                                     *
                                     * msg940950_concat = msg940950_1 + " \n " + msg940950_2;
                                     * setMTMESG(msg940950_concat);
                                     */
                                    swift_type = rs.getString(9);
                                    String nostref_MT103 = rs.getString(10);
                                    setNOSTMT(nostref_MT103);

                                    BigDecimal totalVal1 = new BigDecimal(Credit_AmountMT103202);

                                    ConnectionMaster connectionMaster1 = new ConnectionMaster();
                                    double divideByDecimal1 = connectionMaster1.getDecimalforCurrency(Credit_curMT103202);
                                    BigDecimal divideByBig1 = new BigDecimal(divideByDecimal1);
                                    BigDecimal totalValue1 = totalVal1.divide(divideByBig1);

                                    // BigDecimal totalValue1 = new BigDecimal(Credit_AmountMT103202);

                                    BigDecimal totalVal2 = new BigDecimal(Credit_AmountMT940950);

                                    // ConnectionMaster connectionMaster1 = new ConnectionMaster();
                                    double divideByDecimal2 = connectionMaster1.getDecimalforCurrency(Credit_curMT940950);
                                    BigDecimal divideByBig2 = new BigDecimal(divideByDecimal2);
                                    BigDecimal totalValue2 = totalVal2.divide(divideByBig2);

                                    // =======================

                                    // =====================

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,
                                                      "Total Nostro Credit amount-940=====================-->" + totalValue1);
                                          Loggers.general().info(LOG, "poollll amount-====940=================-->" + totalValue2);

                                    }
                                    String finalVal1 = String.format("%.2f", totalValue1);
                                    String finalVal2 = String.format("%.2f", totalValue2);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG, "Final Nostro Credit amount==940=========--->" + finalVal1);
                                          Loggers.general().info(LOG, "Final poolll amount===940========--->" + finalVal2);

                                    }
                                    setNOSTAMT(finalVal1 + " " + Credit_curMT103202);

                                    setPOOLAMT(finalVal2 + " " + Credit_curMT940950);

                                    // =================================

                                    // String dms = "SELECT NVL(SUM(NOSUTLAMT),0) AS
                                    // OUTSAMOUNT FROM (SELECT SUM(ext.NOSAMT) AS NOSUTLAMT,
                                    // mas.MASTER_REF FROM master mas, BASEEVENT bas,
                                    // extevent ext WHERE mas.KEY97 =bas.MASTER_KEY AND
                                    // bas.KEY97 =ext.EVENT AND bas.STATUS IN ('i','c') AND
                                    // (mas.MASTER_REF ='"
                                    // + masReference + "' AND trim(bas.REFNO_PFIX
                                    // ||LPAD(bas.REFNO_SERL,3,0)) !='" + eventCode
                                    // + "') AND ext.NOSTRM ='" + nostref_MT940950
                                    // + "' GROUP BY mas.MASTER_REF UNION ALL SELECT
                                    // SUM(ext.NOSAMT) AS NOSUTLAMT, mas.MASTER_REF FROM
                                    // master mas, BASEEVENT bas, extevent ext WHERE
                                    // mas.KEY97 =bas.MASTER_KEY AND bas.KEY97 =ext.EVENT
                                    // AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF !='"
                                    // + masReference + "' AND trim(bas.REFNO_PFIX
                                    // ||LPAD(bas.REFNO_SERL,3,0)) !='" + eventCode
                                    // + "') AND ext.NOSTRM ='" + nostref_MT940950
                                    // + "' GROUP BY mas.MASTER_REF ) NOSRTOAMOUNT";

                                    String dms = "SELECT NVL(SUM(NOSUTLAMT),0) AS OUTSAMOUNT FROM (SELECT SUM(ext.NOSAMT) AS NOSUTLAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, extevent ext WHERE mas.KEY97 =bas.MASTER_KEY AND bas.KEY97 =ext.EVENT AND bas.STATUS IN ('i','c')  AND (mas.MASTER_REF ='"
                                                + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) !='" + eventCode
                                                + "') AND ext.NOSTRM ='" + nostref_MT940950
                                                + "' GROUP BY mas.MASTER_REF UNION ALL SELECT SUM(ext.NOSAMT) AS NOSUTLAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, extevent ext WHERE mas.KEY97 =bas.MASTER_KEY AND bas.KEY97 =ext.EVENT AND bas.STATUS IN ('i','c')  AND (mas.MASTER_REF !='"
                                                + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) !='" + eventCode
                                                + "') AND ext.NOSTRM ='" + nostref_MT940950
                                                + "' GROUP BY mas.MASTER_REF UNION ALL SELECT SUM(ext.NOSAMT) AS NOSUTLAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, extevent ext WHERE mas.KEY97 =bas.MASTER_KEY AND bas.KEY97 =ext.EVENT AND bas.STATUS IN ('i','c')  AND (mas.MASTER_REF !='"
                                                + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) ='" + eventCode
                                                + "') AND ext.NOSTRM ='" + nostref_MT940950
                                                + "' GROUP BY mas.MASTER_REF ) NOSRTOAMOUNT";

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG, "Nostro outsanding amount--->" + dms);

                                    }
                                    ps1 = con.prepareStatement(dms);
                                    rs = ps1.executeQuery();
                                    while (rs.next()) {
                                          String nostOut = rs.getString(1).trim();
                                          BigDecimal nostOutBig = new BigDecimal(nostOut);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG, "Result of Nostro outsanding amount--->" + nostOutBig);

                                          }
                                          BigDecimal CreditBig = new BigDecimal(Credit_AmountMT940950);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG, "Nostro Credit amount--->" + CreditBig);

                                          }
                                          BigDecimal totalVal = CreditBig.subtract(nostOutBig);

                                          ConnectionMaster connectionMaster = new ConnectionMaster();
                                          double divideByDecimal = connectionMaster.getDecimalforCurrency(Credit_curMT940950);
                                          BigDecimal divideByBig = new BigDecimal(divideByDecimal);
                                          BigDecimal totalValue = totalVal.divide(divideByBig);

                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG, "Total Nostro Credit amount--->" + totalValue);

                                          }
                                          String finalVal = String.format("%.2f", totalValue);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG, "Final Nostro Credit amount--->" + finalVal);

                                          }
                                          if (totalValue.compareTo(BigDecimal.ZERO) >= 0) {
                                                setNOSTOUT(finalVal + " " + Credit_curMT940950);
                                          } else {
                                                finalVal = "0";
                                                setNOSTOUT(finalVal + " " + Credit_curMT940950);

                                          }

                                    }

                                    String query_940 = "SELECT TRIM( 'MessageDetails:'||SUBSTR(MESSAGE_DATA,1, INSTR(MESSAGE_DATA,':61:')-1 ) )AS MSGDATA,TRIM('TAG 61:'||IND_TEXT) AS IND_TEXT,TRIM('TAG 86:'||ACC_OWNER_INFO) AS ACC_OWNER_INFO FROM ETT_NOSTRO_UTILITY_MT940 where IND_NOS_CUS_REFNO='"
                                                + nostref_MT940950 + "' or IND_NOS_BANK_REFNO='" + nostref_MT940950 + "'";
                                    //// Loggers.general().info(LOG,"Nostro swift_type is
                                    //// query_103
                                    //// " + query_103);
                                    ps3 = con.prepareStatement(query_940);
                                    rs3 = ps3.executeQuery();
                                    while (rs3.next()) {
                                          String msg1 = rs3.getString(1);
                                          String msg2 = rs3.getString(2);
                                          String msg3 = rs3.getString(3);
                                          String fullmsg = msg1 + " \n " + msg2 + " \n " + msg3;
                                          setMTMESG(fullmsg);
                                    }
                                    String query_950 = "SELECT TRIM( 'MessageDetails:'||SUBSTR(MESSAGE_DATA,1, INSTR(MESSAGE_DATA,':61:')-1 ) )AS MSGDATA,TRIM('TAG 61:'||IND_TEXT) AS IND_TEXT FROM ETT_NOSTRO_UTILITY_MT950 where IND_NOS_CUS_REFNO='"
                                                + nostref_MT940950 + "' or IND_NOS_BANK_REFNO='" + nostref_MT940950 + "'";
                                    //// Loggers.general().info(LOG,"Nostro swift_type is
                                    //// query_103
                                    //// " + query_103);
                                    ps4 = con.prepareStatement(query_950);
                                    rs4 = ps4.executeQuery();
                                    while (rs4.next()) {
                                          String msg1 = rs4.getString(1);
                                          String msg2 = rs4.getString(2);

                                          String fullmsg = msg1 + " \n " + msg2;
                                          setMTMESG(fullmsg);
                                    }

                                    if (swift_type.equalsIgnoreCase("103")) {
                                          String query_103 = "SELECT MESSAGE_DATA FROM ETT_NOSTRO_UTILITY_MT103 WHERE REFERENCE_NUMBER ='"
                                                      + nostref_MT103 + "'";
                                          //// Loggers.general().info(LOG,"Nostro swift_type is
                                          //// query_103
                                          //// " + query_103);
                                          ps = con.prepareStatement(query_103);
                                          rs = ps.executeQuery();
                                          while (rs.next()) {
                                                String swift = rs.getString(1);
                                                setINWMSG(swift);
                                          }
                                    } else if (swift_type.equalsIgnoreCase("202")) {
                                          String query_202 = "SELECT MESSAGE_DATA FROM ETT_NOSTRO_UTILITY_MT202 WHERE REFERENCE_NUMBER ='"
                                                      + nostref_MT103 + "'";
                                          //// Loggers.general().info(LOG,"Nostro swift_type is
                                          //// query_202
                                          //// " + query_202);

                                          ps = con.prepareStatement(query_202);
                                          rs = ps.executeQuery();
                                          while (rs.next()) {
                                                String swift = rs.getString(1);
                                                setINWMSG(swift);
                                          }
                                    } else {
                                          //// Loggers.general().info(LOG,"Nostro swift_type is empty
                                          //// " +
                                          //// swift_type);

                                          setINWMSG("");

                                    }

                              }

                        } catch (Exception ee) {
                              Loggers.general().info(LOG, "Exception Inward reference 940" + ee.getMessage());
                        }

                  } else {
                        setNOSTAMT("");
                        setNOSTDAT("");
                        setPOOLAMT("");
                        setNOSTACC("");
                        setMTMESG("");
                        setINWMSG("");
                        setNOSTOUT("");
                  }

                  try {

                        String query = "";
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG, "Nostro ref no nostref_MT103102" + nostref_MT103102);
                        }
                        // String nostref_MT940950 = getWrapper().getNOSTRM().trim();

                        if (nostref_MT103102.length() > 0) {

                              query = "SELECT count(*) as COUNT FROM ETTV_NOSTRO_MT103_MT202_TBL WHERE MT103_MT202_REFERENCE_NUMBER='"
                                          + nostref_MT103102 + "'";
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG, "Nostro ref no ETTV_NOSTRO_MT103_MT202_TBL query" + query);
                              }
                              con = ConnectionMaster.getConnection();
                              ps = con.prepareStatement(query);
                              rs = ps.executeQuery();
                              int val = 0;
                              if (rs.next()) {
                                    val = rs.getInt(1);

                              }

                              query = "select count(*) from ETT_NOSTRO_UTILITY_MT103 M where M.QUEUE_TYPE='CLSQ' and M.REFERENCE_NUMBER='"
                                          + nostref_MT103102 + "'";
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG, "Nostro ref no ETT_NOSTRO_UTILITY_MT103 query" + query);
                              }
                              con = ConnectionMaster.getConnection();
                              ps = con.prepareStatement(query);
                              rs = ps.executeQuery();
                              int value103 = 0;
                              if (rs.next()) {
                                    value103 = rs.getInt(1);

                              }

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,
                                                "Nostro ref no nostref_MT103102 count" + val + "value103" + value103);
                              }
                              if ((nostref_MT103102.length() > 0) && (val == 0 && value103 == 0)) {
                                    setNOSTAMT("");
                                    setNOSTDAT("");
                                    setPOOLAMT("");
                                    setNOSTACC("");
                                    setMTMESG("");
                                    setINWMSG("");
                                    setNOSTOUT("");
                              } else {
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG, "MT103 Nostro Reference else" + val);
                                    }
                              }

                        } else if (nostref_MT940950.length() > 0) {
                              query = "SELECT count(*) FROM ETTV_NOSTRO_MT103_MT202_TBL WHERE MT940_MT950_REFERENCE_NUMBER='"
                                          + nostref_MT940950 + "'";
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG, "Nostro ref no nostref_MT940 query" + query);
                              }
                              ps = con.prepareStatement(query);
                              rs = ps.executeQuery();
                              int val = 0;
                              while (rs.next()) {
                                    val = rs.getInt(1);
                              }
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG, "Nostro ref no nostref_MT940 count" + val);
                              }
                              if ((nostref_MT940950.length() > 0) && val == 0) {
                                    setNOSTAMT("");
                                    setNOSTDAT("");
                                    setPOOLAMT("");
                                    setNOSTACC("");
                                    setMTMESG("");
                                    setINWMSG("");
                                    setNOSTOUT("");

                              } else {
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG, "MT940 Nostro Reference else" + val);
                                    }
                              }
                        }

                  } catch (Exception e1) {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG, "Exception Nostro ref no validation" + e1.getMessage());
                        }
                  }

            } catch (Exception e) {
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG, "Exception details===>" + e.getMessage());
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

      // public void onCHECKLISTOGTISSclayButton() {
      //
      // try {
      // String subcode = "0";
      // getExtEventChecklistNew().setEnabled(false);
      // getExtEventChecklistDelete().setEnabled(false);
      // //Loggers.general().info(LOG,"Enter into try Import gur after code
      // change----->
      // ");
      // EnigmaArray<ExtEventChecklistEntityWrapper> liste =
      // getExtEventChecklistData();
      // int count = 0;
      // Iterator<ExtEventChecklistEntityWrapper> iterator = liste.iterator();
      // String prodCode = getDriverWrapper().getEventFieldAsText("PCO", "s", "");
      // //Loggers.general().info(LOG,"prodCode for chacklist------------>" +
      // prodCode);
      // String eventCod = getDriverWrapper().getEventFieldAsText("EVCD", "s",
      // "");
      // //Loggers.general().info(LOG,"eventCod for chacklist------------>" +
      // eventCod);
      // subcode = getDriverWrapper().getEventFieldAsText("PTP", "s", "").trim();
      // if (subcode.equalsIgnoreCase("")) {
      // subcode = "0";
      // }
      // //Loggers.general().info(LOG,"SUB_PRODUCT_CODE for chacklist------------>" +
      // subcode);
      // String stepID = getDriverWrapper().getEventFieldAsText("CSID", "s", "");
      // //Loggers.general().info(LOG,"prodCode for chacklist------------>" +
      // prodCode);
      //
      // String query = "select TRIM(CHECKLIST_DESCR),TRIM(SEQ_NO) from
      // ETT_WF_CHKLST_MST WHERE TRIM(PROD_CODE) ='"
      // + prodCode + "' AND TRIM(EVENT_CODE)='" + eventCod + "' AND
      // TRIM(SUB_PRODUCT_CODE) ='" + subcode
      // + "' AND TRIM(INIT_AT)='" + stepID + "'";
      // //Loggers.general().info(LOG,"Query for checklist------------>" + query);
      // con1 = ConnectionMaster.getConnection();
      // psd = con1.prepareStatement(query);
      // rst = psd.executeQuery();
      // //Loggers.general().info(LOG,"executeQuery statement ");
      // while (rst.next()) {
      // //Loggers.general().info(LOG,"Enter into while");
      // Key_ExtEvent<? extends ExtEvent> fkey = new Key_ExtEvent<ExtEvent>();
      // ExtEventChecklist checkdet = new ExtEventChecklist();
      // //Loggers.general().info(LOG,"liste size -----> " + fkey);
      // checkdet.setColumn("CHEKCOL", rst.getString(1));
      // checkdet.setColumn("CSMSNO", rst.getString(2));
      // checkdet.setNewKey();
      // checkdet.setFk(fkey);
      // checkdet.setSequence(count);
      //
      // ExtEventChecklistEntityWrapper projectdetchk = new
      // ExtEventChecklistEntityWrapper(checkdet,
      // getDriverWrapper());
      // addNewExtEventChecklist(projectdetchk);
      //
      // count++;
      // }
      // rst.close();
      // psd.close();
      // con1.close();
      // if (liste.getSize().intValue() > 0) {
      // //Loggers.general().info(LOG,"liste size -----> " + liste.getSize());
      // getBtnCHECKLISTOGTISSclay().setEnabled(false);
      // getExtEventChecklistNew().setEnabled(false);
      // getExtEventChecklistDelete().setEnabled(false);
      // getExtEventChecklistUp().setEnabled(false);
      // getExtEventChecklistDown().setEnabled(false);
      // getExtEventChecklistView().setEnabled(false);
      //
      // }
      // } catch (Exception e) {
      // //Loggers.general().info(LOG,"catch result" + e.getMessage());
      //
      // }
      // }

      // @Override
      // protected void loadExtEventChecklistViewPane(ExtensionViewPaneMode mode,
      // ExtEventChecklistEntityWrapper item) {
      // // TODO Auto-generated method stub
      // super.loadExtEventChecklistViewPane(mode, item);
      // //Loggers.general().info(LOG,"loadExtEventChecklistViewPane ----> " + item);
      // //Loggers.general().info(LOG,"Mode ----> " + mode);
      // ExtensionViewPaneMode modeval = mode;
      // //Loggers.general().info(LOG,"modeval.name() - " + modeval.name());
      // if (modeval.name().equalsIgnoreCase("UPDATE")) {
      // //Loggers.general().info(LOG,"Mode UPDATE----> ");
      // getExtEventChecklistDelete().setEnabled(false);
      // } else {
      // //Loggers.general().info(LOG,"modeval.name() in esle ----> " +
      // modeval.name());
      // }
      // }

      public void onEXITFETCHOUTRMTCUSclayButton() {
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
            // Loggers.general().info(LOG,"After Calling Button Action in validate");
            String newpro = getNEWPRO().trim();
            // Loggers.general().info(LOG,"New projetc no ------>" + newpro);
            if (dailyval_Log.equalsIgnoreCase("YES")) {

                  Loggers.general().info(LOG, "New projetc no ------>" + newpro);

            }
            if (newpro.length() > 0 && (!newpro.equalsIgnoreCase(""))) {
                  try {

                        // double totAmt = 0;
                        String projeval = "";
                        PreparedStatement psd = null;
                        ResultSet rst = null;

                        String query = "SELECT NVL(SUM(ROUND(ett.ProjectVal * cer1.EXCH_RATE/ cer2.EXCH_RATE,2)),0) AS AMOUNT FROM ETT_vShowPrjDtls ett, CCY_EXCH_RATE_VIEW cer1, CCY_EXCH_RATE_VIEW cer2 WHERE PROJREFNO ='"
                                    + newpro
                                    + "' AND TO_CHAR(RemmitedDate,'DD-MM-YY') > '01-04-16' and ett.PROJCCY = cer1.CURR_CODE AND cer2.CURR_CODE = 'USD'";
                        // Loggers.general().info(LOG,"ProjectVal query ==========>>>>" +
                        // query);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {

                              Loggers.general().info(LOG, "ProjectVal query ==========>>>>" + query);

                        }
                        con1 = ConnectionMaster.getConnection();
                        psd = con1.prepareStatement(query);
                        rst = psd.executeQuery();
                        // Loggers.general().info(LOG,"leared executeQuery statement ");
                        while (rst.next()) {
                              // Loggers.general().info(LOG,"Enter into while");
                              projeval = rst.getString("AMOUNT").trim();
                              double pproVal = Double.valueOf(projeval);
                              // Loggers.general().info(LOG,"projeval from get double---->" +
                              // pproVal);
                              if (pproVal > 0) {
                                    // Loggers.general().info(LOG,"projeval from get string---->" +
                                    // projeval);
                                    String projcur = "USD";
                                    BigDecimal proj_big = new BigDecimal(projeval);
                                    BigDecimal husd = new BigDecimal("100");
                                    BigDecimal proj = husd.multiply(proj_big);
                                    // Loggers.general().info(LOG,"projeval from get string after
                                    // multyfly 100---->" + proj);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {

                                          Loggers.general().info(LOG, "projeval from get string after multyfly 100---->" + proj);
                                    }
                                    setTOTREM(proj + "" + projcur);
                                    // Loggers.general().info(LOG,"projeval from after set---->" +
                                    // getTOTREM());

                                    setTOTUSD(getUSDAMT());
                                    String totusd = getTOTUSD();
                                    BigDecimal hundred = new BigDecimal(100);
                                    String totnum = totusd.replaceAll("[^0-9]", "");
                                    BigDecimal totbig = new BigDecimal(totnum);
                                    BigDecimal usedtot = totbig.divide(hundred);
                                    BigDecimal convert = new BigDecimal(projeval);
                                    BigDecimal convert_hus = convert.add(usedtot);
                                    // Loggers.general().info(LOG,"convert_hus Project Value and
                                    // current Amount " + convert_hus);
                                    BigDecimal finalval = convert_hus.multiply(hundred);
                                    // Loggers.general().info(LOG,"Final Project Value and current
                                    // Amount " + finalval);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {

                                          Loggers.general().info(LOG, "Final Project Value and current Amount " + finalval);
                                    }
                                    setTOREAMT(finalval + " USD");
                              } else {
                                    setTOTREM("");
                                    setTOTUSD("");
                                    setTOREAMT("");
                              }
                        }

                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"Catch result Project Value" +
                        // e.getMessage());
                        if (dailyval_Log.equalsIgnoreCase("YES")) {

                              Loggers.general().info(LOG, "Catch result Project Value" + e.getMessage());
                        }
                        e.getStackTrace();
                  } finally {
                        try {
                              if (rst != null)
                                    rst.close();
                              if (psd != null)
                                    psd.close();
                              if (con1 != null)
                                    con1.close();

                        } catch (SQLException e) {
                              // Loggers.general().info(LOG,"Connection Failed! Check output
                              // console");
                              e.printStackTrace();
                        }
                  }

            } else {
                  setTOTREM("");
                  setTOTUSD("");
                  setTOREAMT("");
                  // Loggers.general().info(LOG,"New Project Value is empty");
            }
      }

      public boolean lienMark() {
            boolean value = false;

            String Margamt = "0";
            String gridamtcur = "";
            String result = "";
            String systemDate = getDriverWrapper().getEventFieldAsText("TDY", "d", "");
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

                        // try {
                        // if (!deptno.equalsIgnoreCase("")) {
                        // String qr = "SELECT COUNT(*),LIEN,ACCOUNTID,MASTERREFERENCE
                        // FROM THEMEBRIDGE.LIENDETAILS WHERE ACCOUNTID ='"
                        // + deptno + "' AND MASTERREFERENCE ='" + masRefNo + "' AND
                        // EVENTREFERENCE ='" + eventCode
                        // + "' GROUP BY LIEN,ACCOUNTID,MASTERREFERENCE";
                        // Loggers.general().info(LOG,"Query for LIENDETAILS====>" + qr);
                        // con = ConnectionMaster.getThemeBridgeConnection();
                        // ps = con.prepareStatement(qr);
                        // // //Loggers.general().info(LOG,qr);
                        // rs = ps.executeQuery();
                        // while (rs.next()) {
                        // if (rs.getString(2).equalsIgnoreCase("R")) {
                        // count_R++;
                        // Loggers.general().info(LOG,"LIENDETAILS count_Reversal====>" +
                        // count_R);
                        // } else if (rs.getString(2).equalsIgnoreCase("M")) {
                        // count_M++;
                        // Loggers.general().info(LOG,"LIENDETAILS count_Mark====>" + count_M);
                        // }
                        //
                        // }
                        // }
                        //
                        // } catch (Exception e) {
                        // //Loggers.general().info(LOG,"exception caught " +e.getMessage()
                        // );
                        // } finally {
                        // try {
                        // if (con != null) {
                        // con.close();
                        // if (ps != null)
                        // ps.close();
                        // if (rs != null)
                        // rs.close();
                        // }
                        // } catch (SQLException e) {
                        // // Loggers.general().info(LOG,"Connection Failed! Check output
                        // // console");
                        // e.printStackTrace();
                        // }
                        // }
                  }

            }
            // Loggers.general().info(LOG,"count_Reversal====>" + count_R +
            // "count_Mark=====>" + count_M);

            // if (count_R >= count_M) {
            // Loggers.general().info(LOG,"If loop ==== > Account no is added new");
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

                                          Date sysdate = (Date) formatter1.parse(systemDate);
                                          // Loggers.general().info(LOG,"systemDate Date lien----- " +
                                          // systemDate);
                                          result = formatter1.format(sysdate);
                                          // Loggers.general().info(LOG,"result Date ----- " +
                                          // result);

                                    } catch (ParseException e) {
                                          // Loggers.general().info(LOG,"Date Exception ");
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
                                          // //Loggers.general().info(LOG,"res_sp_line[0]==>"+res_sp_line[0]);
                                          // String lienStr = str.getMARGAMT();
                                          // double lienAmt = Double.valueOf(lienStr);
                                          String lienbox = str.getLINEST();
                                          // //Loggers.general().info(LOG,"currentDepositNum -
                                          // "+currentDepositNum);
                                          // //Loggers.general().info(LOG,"depositNum -
                                          // "+depositNum);
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
                                                            // str.setLIENDAT(result);
                                                            getExtEventLienMarkingUpdate().setEnabled(false);
                                                            getExtEventLienMarkingDelete().setEnabled(false);
                                                      } else if ((lienbox.equalsIgnoreCase("") || lienbox == null)) {
                                                            // Loggers.general().info(LOG,"lien FAILED
                                                            // status" + res_sp_line[2]);
                                                            str.setLINEST(res_sp_line[2]);
                                                            str.setLIENID(res_sp_line[1]);
                                                            str.setLIENREM(res_sp_line[3]);
                                                            // str.setLIENDAT(result);
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
                              // Loggers.general().info(LOG,"ThemeTransportClient Exceptions!
                              // " +
                              // e.getMessage());
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

                                    Date sysdate = (Date) formatter1.parse(systemDate);
                                    // Loggers.general().info(LOG,"systemDate Date lien----- " +
                                    // systemDate);
                                    result = formatter1.format(sysdate);
                                    // Loggers.general().info(LOG,"result Date ----- " + result);

                              } catch (ParseException e) {
                                    // Loggers.general().info(LOG,"Date Exception ");
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
                                                      // str.setLIENDAT(result);
                                                      getExtEventLienMarkingUpdate().setEnabled(false);
                                                      getExtEventLienMarkingDelete().setEnabled(false);
                                                } else if ((lienbox.equalsIgnoreCase("") || lienbox == null)) {
                                                      // Loggers.general().info(LOG,"lien FAILED
                                                      // status" +
                                                      // res_sp_line[2]);
                                                      str.setLINEST(res_sp_line[2]);
                                                      str.setLIENID(res_sp_line[1]);
                                                      str.setLIENREM(res_sp_line[3]);
                                                      // str.setLIENDAT(result);
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
                        // Loggers.general().info(LOG,"ThemeTransportClient Exceptions! " +
                        // e.getMessage());
                  }

            }
            // } else {
            // getExtEventLienMarkingDelete().setEnabled(false);
            // // Loggers.general().info(LOG,"else ==== > Account no is already exit");
            // String sameAcc = "Same account no should not add for same master
            // reference no,Please modify existing account no";
            // EnigmaArray<ExtEventLienMarkingEntityWrapper> leanDataList =
            // getExtEventLienMarkingData();
            //
            // Iterator<ExtEventLienMarkingEntityWrapper> iterator1 =
            // leanDataList.iterator();
            //
            // for (ExtEventLienMarkingEntityWrapper str : leanDataList) {
            // String lienremark = str.getLIENREM().trim();
            // if (lienremark.equalsIgnoreCase("") || lienremark == null) {
            // str.setLIENREM(sameAcc);
            // getExtEventLienMarkingDelete().setEnabled(false);
            // }
            //
            // }
            // }

            return value;
      }

      public void onfetchlienOGTISSclayButton() {

            lienMark();
      }

      public void onREVERSELIENOGTISSclayButton() {
            lienReverse();
      }

      // Ranji
      public void onfetchlienOGTINVOCclayButton() {
            Loggers.general().info(LOG, "insert lien mark");
            lienMark();
      }

      public void onREVERSELIENOGTINVOCclayButton() {
            Loggers.general().info(LOG, "insert invocation reversal");
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

      public void onFETCHMT103INWRMTCUSclayButton() {
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

            String displayVal = "";
            try {
                  Loggers.general().info(LOG, "Entered fetching SI Link");
                  String strPropName = "Standing";
                  @SuppressWarnings("unchecked")
                  AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryRCODE = getDriverWrapper()
                              .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strPropName + "'");
                  EXTGENCUSTPROP PROPCode = queryRCODE.getUnique();
                  if (PROPCode != null) {
                        Loggers.general().info(LOG, "Standing URL is not empty-------->");
                        displayVal = PROPCode.getPropval();
                  } else {
                        // Loggers.general().info(LOG,"Standing URL is empty-------->");
                  }
                  Loggers.general().info(LOG, "displayVal - " + displayVal);
            } catch (Exception e) {
                  Loggers.general().info(LOG, "Exception in getting SI Link - " + e.getMessage());
            }
            try {
                  String theirRefNumber = getDriverWrapper().getEventFieldAsText("THE", "r", "").trim();
                  // String accountno_103=getNOSTMT().trim();
                  String actnum = null;
                  String act = null;
                  String query_103 = "SELECT TRIM(MT103.BENEF_NO) AS ACCOUNT_NO, TRIM(ACC.CUS_MNM) AS CUST_NO, TRIM(SX.SVNAFF) AS BENEF_ADDR,TRIM(MT103.CH_BEN_ACC_NO) FROM ETT_NOSTRO_UTILITY_MT103 MT103, ACCOUNT ACC, SX20LF SX WHERE (TRIM(MT103.CH_BEN_ACC_NO) = TRIM(ACC.BO_ACCTNO) or TRIM(MT103.benef_no)= TRIM(ACC.BO_ACCTNO)) AND ACC.CUS_MNM = SX.SXCUS1 AND REFERENCE_NUMBER = '"
                              + theirRefNumber + "'";
                  Loggers.general().info(LOG, "Nostro theirRefNumber====>" + theirRefNumber);
                  Loggers.general().info(LOG, "Query====>" + query_103);
                  // + query_103);
                  con = ConnectionMaster.getConnection();
                  ps = con.prepareStatement(query_103);
                  rs = ps.executeQuery();
                  while (rs.next()) {
                        Loggers.general().info(LOG, "While occured for remittance");
                        act = rs.getString(4);
                        Loggers.general().info(LOG, "Nostro theirRefNumber====>" + act);
                        if (act != null && !act.equalsIgnoreCase("")) {
                              actnum = rs.getString(4).trim();
                        } else {
                              actnum = rs.getString(1).trim();
                        }
                        String cusnum = rs.getString(2).trim();
                        String benificiary = rs.getString(3).trim();
                        setACTNUM(actnum);
                        setCUSTNUM(cusnum);
                        setBENEADD(benificiary);
                        // Enable / Disable - standing instruction check
                        ConnectionMaster conn = new ConnectionMaster();
                        /* String Standing = conn.getStandinglink(); */
                        String Standing = displayVal;
                        ExtendedHyperlinkControlWrapper Standing1 = getCtlSTANDINGINWRMTCUSclayHyperlink();
                        String nostroAccountNum = actnum;
                        Standing = Standing + nostroAccountNum;
                        Standing1.setUrl(Standing);
                        Loggers.general().info(LOG, "Standing " + Standing);
                        String mt103Currency = getDriverWrapper().getEventFieldAsText("EVAM", "v", "c");
                        Loggers.general().info(LOG, "mt103Currency - " + mt103Currency);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG, "nostroAccountNum - " + nostroAccountNum);
                              Loggers.general().info(LOG, "Standing - " + Standing);
                              Loggers.general().info(LOG, "Account number for SI Availability check - " + actnum
                                          + " And Credit CCY - " + mt103Currency);
                        }
                        Standing1.setVisible(conn.isSIAvailable(actnum, mt103Currency));
                        Loggers.general().info(LOG, "getvisible " + Standing1.getControlVisibility());
                  }
            } catch (Exception e1) {
                  Loggers.general().info(LOG, "Exception Nostro 103INWRMTCUS====>" + e1.getMessage());
                  e1.printStackTrace();
            } finally {
                  try {
                        if (rs != null)
                              rs.close();
                        if (ps != null)
                              ps.close();
                        if (con != null)
                              con.close();
                  } catch (SQLException e) {
                        //// Loggers.general().info(LOG,"Connection Failed! Check output
                        //// console");
                        e.printStackTrace();
                  }
            }
      }

      public void onNOSTROINWRMTCUSclayButton() {

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
//                String nostref_MT103102 = getNOSTMT().trim();
//                String nostref_MT940950 = getNOSTRM().trim();
//                String masReference = getDriverWrapper().getEventFieldAsText("MST", "r", "");
//                String eventCode = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
//                String Credit_AmountMT103202 = "";
//                String Credit_curMT103202 = "";
//                String ValuedateMT103202 = "";
//                String Credit_AmountMT940950 = "";
//                String Credit_curMT940950 = "";
//                String CreditAccountMT940950 = "";
//                String msg940950_1 = "";
//                String msg940950_2 = "";
//                String msg940950_concat = "";
//                String swift_type = "";
//                String Nostro_key="";
//                con = ConnectionMaster.getConnection();
//                if (nostref_MT103102.length() > 0) {
//                      try {
//
//                            String query = "SELECT MT103_MT202_AMOUNT AS CreditMT103202, MT103_MT202_CURRENCY_CODE AS CreditCurMT103202, TO_CHAR(MT103_MT202_VALUE_DATE,'DD/MM/YY') AS ValuedateMT103202, MT940_MT950_AMOUNT AS CreditAmountMT940950, MT940_MT950_CURRENCY_CODE AS CreditCurMT940950, MT940_MT950_ACCOUNT_NO AS CreditAccountMT940950, MT940_MT950_IND_TEXT, MT940_MT950_ACC_OWNER_INFO,Trim(SWIFT_TYPE),MT940_MT950_REFERENCE_NUMBER,NOSTRO_KEY FROM ETTV_NOSTRO_MT103_MT202_TBL WHERE MT103_MT202_REFERENCE_NUMBER='"
//                                        + nostref_MT103102 + "'";
//                            if (dailyval_Log.equalsIgnoreCase("YES")) {
//                                  Loggers.general().info(LOG,"Nostro ETTV_NOSTRO_MT103_MT202_TBL value query " + query);
//                            }
//
//                            ps = con.prepareStatement(query);
//                            rs = ps.executeQuery();
//                            if (rs.next()) {
//
//                                  Credit_AmountMT103202 = rs.getString(1);
//                                  Credit_curMT103202 = rs.getString(2);
//
//                                  //setNOSTAMT(Credit_AmountMT103202 + " " + Credit_curMT103202);
//                                  setNOSTOUT(Credit_AmountMT103202 + " " + Credit_curMT103202);
//                                  ValuedateMT103202 = rs.getString(3);
//                                  setNOSTDAT(ValuedateMT103202);
//                                  Credit_AmountMT940950 = rs.getString(4);
//                                  Credit_curMT940950 = rs.getString(5);
//
//                            //    setPOOLAMT(Credit_AmountMT940950 + " " + Credit_curMT940950);
//                                  CreditAccountMT940950 = rs.getString(6);
//
//                                  setNOSTACC(CreditAccountMT940950);
//                                  /*msg940950_1 = rs.getString(7);
//                                  msg940950_2 = rs.getString(8);
//
//                                  msg940950_concat = msg940950_1 + " \n " + msg940950_2;
//                                  setMTMESG(msg940950_concat);*/
//                                  swift_type = rs.getString(9);
//                                  String nostref_MT940 = rs.getString(10);
//                                  setNOSTRM(nostref_MT940);
//                                  
//                                  Nostro_key=rs.getString(11);
//                                  Loggers.general().info(LOG,"Nostro Key-=====================-->" + Nostro_key);
//                                        setNOSTROKE(Nostro_key);
//                                  
//                                  BigDecimal totalVal1 = new BigDecimal(Credit_AmountMT103202);
//
//                                  ConnectionMaster connectionMaster1 = new ConnectionMaster();
//                                  double divideByDecimal1 = connectionMaster1.getDecimalforCurrency(Credit_curMT103202);
//                                  BigDecimal divideByBig1 = new BigDecimal(divideByDecimal1);
//                                  BigDecimal totalValue1 = totalVal1.divide(divideByBig1);
//                                  
//                                  //BigDecimal totalValue1 = new BigDecimal(Credit_AmountMT103202);
//                                  
//                                  BigDecimal totalVal2 = new BigDecimal(Credit_AmountMT940950);
//
//                                  //ConnectionMaster connectionMaster1 = new ConnectionMaster();
//                                  double divideByDecimal2 = connectionMaster1.getDecimalforCurrency(Credit_curMT940950);
//                                  BigDecimal divideByBig2 = new BigDecimal(divideByDecimal2);
//                                  BigDecimal totalValue2 = totalVal2.divide(divideByBig2);
//                                  
//
//                                  
//                                  //=======================
//                                  
//                                  //=====================
//
//                                  if (dailyval_Log.equalsIgnoreCase("YES")) {
//                                        Loggers.general().info(LOG,"Total Nostro Credit amount-940=====================-->" + totalValue1);
//                                        Loggers.general().info(LOG,"poollll amount-====940=================-->" + totalValue2);
//
//
//                                  }
//                                  String finalVal1 = String.format("%.2f", totalValue1);
//                                  String finalVal2 = String.format("%.2f", totalValue2);
//                                  if (dailyval_Log.equalsIgnoreCase("YES")) {
//                                        Loggers.general().info(LOG,"Final Nostro Credit amount==940=========--->" + finalVal1);
//                                        Loggers.general().info(LOG,"Final poolll amount===940========--->" + finalVal2);
//
//                                  }
//                                  setNOSTAMT(finalVal1 + " " + Credit_curMT103202);
//
//                                  setPOOLAMT(finalVal2 + " " + Credit_curMT940950);
//                                  
//
//                                  // String dms = "SELECT NVL(SUM(NOSUTLAMT),0) AS
//                                  // OUTSAMOUNT FROM (SELECT SUM(ext.NOSAMT) AS NOSUTLAMT,
//                                  // mas.MASTER_REF FROM master mas, BASEEVENT bas,
//                                  // extevent ext WHERE mas.KEY97 =bas.MASTER_KEY AND
//                                  // bas.KEY97 =ext.EVENT AND bas.STATUS IN ('i','c') AND
//                                  // (mas.MASTER_REF ='"
//                                  // + masReference + "' AND trim(bas.REFNO_PFIX
//                                  // ||LPAD(bas.REFNO_SERL,3,0)) !='" + eventCode
//                                  // + "') AND ext.NOSTMT ='" + nostref_MT103102
//                                  // + "' GROUP BY mas.MASTER_REF UNION ALL SELECT
//                                  // SUM(ext.NOSAMT) AS NOSUTLAMT, mas.MASTER_REF FROM
//                                  // master mas, BASEEVENT bas, extevent ext WHERE
//                                  // mas.KEY97 =bas.MASTER_KEY AND bas.KEY97 =ext.EVENT
//                                  // AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF !='"
//                                  // + masReference + "' AND trim(bas.REFNO_PFIX
//                                  // ||LPAD(bas.REFNO_SERL,3,0)) !='" + eventCode
//                                  // + "') AND ext.NOSTMT ='" + nostref_MT103102
//                                  // + "' GROUP BY mas.MASTER_REF ) NOSRTOAMOUNT";
//
//                                  String dms = "SELECT NVL(SUM(NOSUTLAMT),0) AS OUTSAMOUNT FROM (SELECT SUM(ext.NOSAMT) AS NOSUTLAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, extevent ext WHERE mas.KEY97 =bas.MASTER_KEY AND bas.KEY97 =ext.EVENT AND bas.STATUS IN ('i','c')  AND (mas.MASTER_REF ='"
//                                              + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) !='" + eventCode
//                                              + "') AND ext.NOSTMT ='" + nostref_MT103102
//                                              + "' GROUP BY mas.MASTER_REF UNION ALL SELECT SUM(ext.NOSAMT) AS NOSUTLAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, extevent ext WHERE mas.KEY97 =bas.MASTER_KEY AND bas.KEY97 =ext.EVENT AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF !='"
//                                              + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) !='" + eventCode
//                                              + "') AND ext.NOSTMT ='" + nostref_MT103102
//                                              + "' GROUP BY mas.MASTER_REF  UNION ALL SELECT SUM(ext.NOSAMT) AS NOSUTLAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, extevent ext WHERE mas.KEY97 =bas.MASTER_KEY AND bas.KEY97 =ext.EVENT AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF !='"
//                                              + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) ='" + eventCode
//                                              + "') AND ext.NOSTMT ='" + nostref_MT103102
//                                              + "' GROUP BY mas.MASTER_REF  ) NOSRTOAMOUNT";
//
//                                  if (dailyval_Log.equalsIgnoreCase("YES")) {
//                                        Loggers.general().info(LOG,"Nostro outsanding amount--->" + dms);
//
//                                  }
//                                  ps = con.prepareStatement(dms);
//                                  rs = ps.executeQuery();
//                                  while (rs.next()) {
//                                        String nostOut = rs.getString(1).trim();
//                                        BigDecimal nostOutBig = new BigDecimal(nostOut);
//                                        if (dailyval_Log.equalsIgnoreCase("YES")) {
//                                              Loggers.general().info(LOG,"Result of Nostro outsanding amount--->" + nostOutBig);
//
//                                        }
//                                        BigDecimal CreditBig = new BigDecimal(Credit_AmountMT940950);
//                                        if (dailyval_Log.equalsIgnoreCase("YES")) {
//                                              Loggers.general().info(LOG,"Nostro Credit amount--->" + CreditBig);
//
//                                        }
//                                        BigDecimal totalVal = CreditBig.subtract(nostOutBig);
//
//                                        ConnectionMaster connectionMaster = new ConnectionMaster();
//                                        double divideByDecimal = connectionMaster.getDecimalforCurrency(Credit_curMT940950);
//                                        BigDecimal divideByBig = new BigDecimal(divideByDecimal);
//                                        BigDecimal totalValue = totalVal.divide(divideByBig);
//
//                                        if (dailyval_Log.equalsIgnoreCase("YES")) {
//                                              Loggers.general().info(LOG,"Total Nostro Credit amount--->" + totalValue);
//
//                                        }
//                                        String finalVal = String.format("%.2f", totalValue);
//                                        if (dailyval_Log.equalsIgnoreCase("YES")) {
//                                              Loggers.general().info(LOG,"Final Nostro Credit amount--->" + finalVal);
//
//                                        }
//                                        if (totalValue.compareTo(BigDecimal.ZERO) >= 0) {
//                                              setNOSTOUT(finalVal + " " + Credit_curMT940950);
//                                        } else {
//                                              finalVal = "0";
//                                              setNOSTOUT(finalVal + " " + Credit_curMT940950);
//
//                                        }
//
//                                  }
//                                  
//                                  String query_940 = "SELECT TRIM( 'MessageDetails:'||SUBSTR(MESSAGE_DATA,1, INSTR(MESSAGE_DATA,':61:')-1 ) )AS MSGDATA,TRIM('TAG 61:'||IND_TEXT) AS IND_TEXT,TRIM('TAG 86:'||ACC_OWNER_INFO) AS ACC_OWNER_INFO FROM ETT_NOSTRO_UTILITY_MT940 where IND_NOS_CUS_REFNO='"+ nostref_MT940 + "' or IND_NOS_BANK_REFNO='"+ nostref_MT940 + "'";
//                                  //// Loggers.general().info(LOG,"Nostro swift_type is
//                                  //// query_103
//                                  //// " + query_103);
//                                  ps3 = con.prepareStatement(query_940);
//                                  rs3 = ps3.executeQuery();
//                                  while (rs3.next()) {
//                                        String msg1 = rs3.getString(1);
//                                        String msg2 = rs3.getString(2);
//                                        String msg3 = rs3.getString(3);
//                                        String fullmsg = msg1 + " \n " + msg2 + " \n " + msg3;
//                                        setMTMESG(fullmsg);
//                                  }
//                                  String query_950 = "SELECT TRIM( 'MessageDetails:'||SUBSTR(MESSAGE_DATA,1, INSTR(MESSAGE_DATA,':61:')-1 ) )AS MSGDATA,TRIM('TAG 61:'||IND_TEXT) AS IND_TEXT FROM ETT_NOSTRO_UTILITY_MT950 where IND_NOS_CUS_REFNO='"+ nostref_MT940 + "' or IND_NOS_BANK_REFNO='"+ nostref_MT940 + "'";
//                                  //// Loggers.general().info(LOG,"Nostro swift_type is
//                                  //// query_103
//                                  //// " + query_103);
//                                  ps4 = con.prepareStatement(query_950);
//                                  rs4 = ps4.executeQuery();
//                                  while (rs4.next()) {
//                                        String msg1 = rs4.getString(1);
//                                        String msg2 = rs4.getString(2);
//                                        
//                                        String fullmsg = msg1 + " \n " + msg2 ;
//                                        setMTMESG(fullmsg);
//                                  }
//
//                                  if (swift_type.equalsIgnoreCase("103")) {
//                                        String query_103 = "SELECT MESSAGE_DATA FROM ETT_NOSTRO_UTILITY_MT103 WHERE REFERENCE_NUMBER ='"
//                                                    + nostref_MT103102 + "'";
//                                        //// Loggers.general().info(LOG,"Nostro swift_type is
//                                        //// query_103
//                                        //// " + query_103);
//                                        ps = con.prepareStatement(query_103);
//                                        rs = ps.executeQuery();
//                                        while (rs.next()) {
//                                              String swift = rs.getString(1);
//                                              setINWMSG(swift);
//                                        }
//                                  } else if (swift_type.equalsIgnoreCase("202")) {
//                                        String query_202 = "SELECT MESSAGE_DATA FROM ETT_NOSTRO_UTILITY_MT202 WHERE REFERENCE_NUMBER ='"
//                                                    + nostref_MT103102 + "'";
//                                        //// Loggers.general().info(LOG,"Nostro swift_type is
//                                        //// query_202
//                                        //// " + query_202);
//
//                                        ps = con.prepareStatement(query_202);
//                                        rs = ps.executeQuery();
//                                        while (rs.next()) {
//                                              String swift = rs.getString(1);
//                                              setINWMSG(swift);
//                                        }
//                                  } else {
//                                        if (dailyval_Log.equalsIgnoreCase("YES")) {
//                                              Loggers.general().info(LOG,"Nostro swift_type is empty " + swift_type);
//                                        }
//                                        setINWMSG("");
//
//                                  }
//
//                            } else {
//
//                                  setPOOLAMT("");
//                                  setNOSTACC("");
//                                  setMTMESG("");
//
//                                  String queryVal = "select M.AMOUNT,M.CURRENCY_CODE, trim(TO_CHAR(M.VALUE_DATE,'DD/MM/YY')),M.MESSAGE_DATA,M.MT103_SL_NO from ETT_NOSTRO_UTILITY_MT103 M where trim(M.REFERENCE_NUMBER)='"
//                                              + nostref_MT103102 + "'";
//                                  if (dailyval_Log.equalsIgnoreCase("YES")) {
//                                        Loggers.general().info(LOG,"Nostro ETT_NOSTRO_UTILITY_MT103 value query else===> " + queryVal);
//                                  }
//                                  ps = con.prepareStatement(queryVal);
//                                  rs = ps.executeQuery();
//
//                                  while (rs.next()) {
//                                        Credit_AmountMT103202 = rs.getString(1);
//                                        BigDecimal Credit_Amount = new BigDecimal(Credit_AmountMT103202);
//
//                                        Credit_curMT103202 = rs.getString(2);
//                                        String Credit_AmountMT103 = String.format("%.2f", Credit_Amount);
//                                        if (dailyval_Log.equalsIgnoreCase("YES")) {
//                                              System.out.println(
//                                                          "Nostro ETT_NOSTRO_UTILITY_MT103 value==1==> " + Credit_AmountMT103202);
//                                              Loggers.general().info(LOG,"Nostro ETT_NOSTRO_UTILITY_MT103 value==2==> " + Credit_AmountMT103);
//                                        }
//                                        setNOSTAMT(Credit_AmountMT103 + " " + Credit_curMT103202);
//                                        setNOSTOUT(Credit_AmountMT103 + " " + Credit_curMT103202);
//                                        ValuedateMT103202 = rs.getString(3);
//                                        setNOSTDAT(ValuedateMT103202);
//                                        
//                                        Nostro_key=rs.getString(5);
//                                        Loggers.general().info(LOG,"Nostro Key-=====================-->" + Nostro_key);
//                                        setNOSTROKE(Nostro_key);
//
//                                        msg940950_concat = rs.getString(4);
//                                        setINWMSG(msg940950_concat);
//
//                                        // String dms = "SELECT NVL(SUM(NOSUTLAMT),0) AS
//                                        // OUTSAMOUNT FROM (SELECT SUM(ext.NOSAMT) AS
//                                        // NOSUTLAMT, mas.MASTER_REF FROM master mas,
//                                        // BASEEVENT bas, extevent ext WHERE mas.KEY97
//                                        // =bas.MASTER_KEY AND bas.KEY97 =ext.EVENT AND
//                                        // bas.STATUS IN ('i','c') AND (mas.MASTER_REF ='"
//                                        // + masReference + "' AND trim(bas.REFNO_PFIX
//                                        // ||LPAD(bas.REFNO_SERL,3,0)) !='" + eventCode
//                                        // + "') AND ext.NOSTMT ='" + nostref_MT103102
//                                        // + "' GROUP BY mas.MASTER_REF UNION ALL SELECT
//                                        // SUM(ext.NOSAMT) AS NOSUTLAMT, mas.MASTER_REF FROM
//                                        // master mas, BASEEVENT bas, extevent ext WHERE
//                                        // mas.KEY97 =bas.MASTER_KEY AND bas.KEY97
//                                        // =ext.EVENT AND bas.STATUS IN ('i','c') AND
//                                        // (mas.MASTER_REF !='"
//                                        // + masReference + "' AND trim(bas.REFNO_PFIX
//                                        // ||LPAD(bas.REFNO_SERL,3,0)) !='" + eventCode
//                                        // + "') AND ext.NOSTMT ='" + nostref_MT103102
//                                        // + "' GROUP BY mas.MASTER_REF ) NOSRTOAMOUNT";
//
//                                        String dms = "SELECT NVL(SUM(NOSUTLAMT),0) AS OUTSAMOUNT FROM (SELECT SUM(ext.NOSAMT) AS NOSUTLAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, extevent ext WHERE mas.KEY97 =bas.MASTER_KEY AND bas.KEY97 =ext.EVENT AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF ='"
//                                                    + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) !='"
//                                                    + eventCode + "') AND ext.NOSTMT ='" + nostref_MT103102
//                                                    + "' GROUP BY mas.MASTER_REF UNION ALL SELECT SUM(ext.NOSAMT) AS NOSUTLAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, extevent ext WHERE mas.KEY97 =bas.MASTER_KEY AND bas.KEY97 =ext.EVENT AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF !='"
//                                                    + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) !='"
//                                                    + eventCode + "') AND ext.NOSTMT ='" + nostref_MT103102
//                                                    + "' GROUP BY mas.MASTER_REF  UNION ALL SELECT SUM(ext.NOSAMT) AS NOSUTLAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, extevent ext WHERE mas.KEY97 =bas.MASTER_KEY AND bas.KEY97 =ext.EVENT AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF !='"
//                                                    + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) ='"
//                                                    + eventCode + "') AND ext.NOSTMT ='" + nostref_MT103102
//                                                    + "' GROUP BY mas.MASTER_REF  ) NOSRTOAMOUNT";
//
//                                        if (dailyval_Log.equalsIgnoreCase("YES")) {
//                                              Loggers.general().info(LOG,"Nostro outsanding amount else--->" + dms);
//
//                                        }
//                                        ps = con.prepareStatement(dms);
//                                        rs = ps.executeQuery();
//                                        while (rs.next()) {
//                                              String nostOut = rs.getString(1).trim();
//                                              BigDecimal nostOutBig = new BigDecimal(nostOut);
//                                              if (dailyval_Log.equalsIgnoreCase("YES")) {
//                                                    Loggers.general().info(LOG,"Result of Nostro outsanding amount--->" + nostOutBig);
//
//                                              }
//                                              BigDecimal CreditBig = new BigDecimal(Credit_AmountMT103202);
//                                              if (dailyval_Log.equalsIgnoreCase("YES")) {
//                                                    Loggers.general().info(LOG,"Nostro Credit amount for MT103--->" + CreditBig);
//
//                                              }
//                                              ConnectionMaster connectionMaster = new ConnectionMaster();
//                                              double divideByDecimal = connectionMaster.getDecimalforCurrency(Credit_curMT103202);
//                                              BigDecimal divideByBig = new BigDecimal(divideByDecimal);
//                                              BigDecimal creditAmount = CreditBig.multiply(divideByBig);
//                                              if (dailyval_Log.equalsIgnoreCase("YES")) {
//                                                    Loggers.general().info(LOG,"Nostro Credit amount for MT103 after multifly===>" + CreditBig);
//
//                                              }
//                                              BigDecimal totalVal = creditAmount.subtract(nostOutBig);
//
//                                              BigDecimal totalValue = totalVal.divide(divideByBig);
//
//                                              if (dailyval_Log.equalsIgnoreCase("YES")) {
//                                                    Loggers.general().info(LOG,"Total Nostro Credit amount--->" + totalValue);
//
//                                              }
//                                              String finalVal = String.format("%.2f", totalValue);
//                                              if (dailyval_Log.equalsIgnoreCase("YES")) {
//                                                    Loggers.general().info(LOG,"Final Nostro Credit amount--->" + finalVal);
//
//                                              }
//                                              if (totalValue.compareTo(BigDecimal.ZERO) >= 0) {
//                                                    setNOSTOUT(finalVal + " " + Credit_curMT103202);
//                                              } else {
//                                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
//                                                          Loggers.general().info(LOG,"Final Nostro Credit amount ZERO in else--->" + finalVal);
//
//                                                    }
//                                                    finalVal = "0";
//                                                    setNOSTOUT(finalVal + " " + Credit_curMT103202);
//
//                                              }
//
//                                        }
//
//                                  }
//
//                            }
//
//                      } catch (Exception ee) {
//                            Loggers.general().info(LOG,"Exception Inward reference 103" + ee.getMessage());
//                      }
//                } else if (nostref_MT940950.length() > 0) {
//
//                      //// Loggers.general().info(LOG,"the Nostro MT103/202 reference number
//                      //// is
//                      //// empty");
//
//                      try {
//
//                            String query = "SELECT MT103_MT202_AMOUNT AS CreditMT103202, MT103_MT202_CURRENCY_CODE AS CreditCurMT103202, TO_CHAR(MT103_MT202_VALUE_DATE,'DD/MM/YY') AS ValuedateMT103202, MT940_MT950_AMOUNT AS CreditAmountMT940950, MT940_MT950_CURRENCY_CODE AS CreditCurMT940950, MT940_MT950_ACCOUNT_NO AS CreditAccountMT940950, MT940_MT950_IND_TEXT, MT940_MT950_ACC_OWNER_INFO,Trim(SWIFT_TYPE),MT103_MT202_REFERENCE_NUMBER,NOSTRO_KEY FROM ETTV_NOSTRO_MT103_MT202_TBL WHERE MT940_MT950_REFERENCE_NUMBER='"
//                                        + nostref_MT940950 + "'";
//                            if (dailyval_Log.equalsIgnoreCase("YES")) {
//                                  Loggers.general().info(LOG,"Nostro MT940950 value query " + query);
//                            }
//                            ps = con.prepareStatement(query);
//                            rs = ps.executeQuery();
//                            while (rs.next()) {
//
//                                  Credit_AmountMT103202 = rs.getString(1);
//                                  Credit_curMT103202 = rs.getString(2);
//                                  //// Loggers.general().info(LOG,"Nostro MT103202 credit amount
//                                  //// and
//                                  //// currency" + Credit_AmountMT103202 + " " +
//                                  //// Credit_curMT103202);
//                                  //setNOSTAMT(Credit_AmountMT103202 + " " + Credit_curMT103202);
//
//                                  ValuedateMT103202 = rs.getString(3);
//                                  setNOSTDAT(ValuedateMT103202);
//                                  Credit_AmountMT940950 = rs.getString(4);
//                                  Credit_curMT940950 = rs.getString(5);
//                                  //// Loggers.general().info(LOG,"Nostro MT940950 credit amount
//                                  //// and
//                                  //// currency" + Credit_AmountMT940950 + " " +
//                                  //// Credit_curMT940950);
//                            //    setPOOLAMT(Credit_AmountMT940950 + " " + Credit_curMT940950);
//                                  setNOSTOUT(Credit_AmountMT940950 + " " + Credit_curMT940950);
//                                  CreditAccountMT940950 = rs.getString(6);
//                                  //// Loggers.general().info(LOG,"setNOSTACC------------>" +
//                                  //// getNOSTACC());
//                                  setNOSTACC(CreditAccountMT940950);
//                                  /*msg940950_1 = rs.getString(7);
//                                  msg940950_2 = rs.getString(8);
//
//                                  msg940950_concat = msg940950_1 + " \n " + msg940950_2;
//                                  setMTMESG(msg940950_concat);*/
//                                  swift_type = rs.getString(9);
//                                  String nostref_MT103 = rs.getString(10);
//                                  setNOSTMT(nostref_MT103);
//                                  
//                                  Nostro_key=rs.getString(11);
//                                  Loggers.general().info(LOG,"Nostro Key-=====================-->" + Nostro_key);
//                                  setNOSTROKE(Nostro_key);
//                                  
//                                  BigDecimal totalVal1 = new BigDecimal(Credit_AmountMT103202);
//
//                                  ConnectionMaster connectionMaster1 = new ConnectionMaster();
//                                  double divideByDecimal1 = connectionMaster1.getDecimalforCurrency(Credit_curMT103202);
//                                  BigDecimal divideByBig1 = new BigDecimal(divideByDecimal1);
//                                  BigDecimal totalValue1 = totalVal1.divide(divideByBig1);
//                                  
//                                  //BigDecimal totalValue1 = new BigDecimal(Credit_AmountMT103202);
//                                  
//                                  BigDecimal totalVal2 = new BigDecimal(Credit_AmountMT940950);
//
//                                  //ConnectionMaster connectionMaster1 = new ConnectionMaster();
//                                  double divideByDecimal2 = connectionMaster1.getDecimalforCurrency(Credit_curMT940950);
//                                  BigDecimal divideByBig2 = new BigDecimal(divideByDecimal2);
//                                  BigDecimal totalValue2 = totalVal2.divide(divideByBig2);
//                                  
//
//                                  
//                                  //=======================
//                                  
//                                  //=====================
//
//                                  if (dailyval_Log.equalsIgnoreCase("YES")) {
//                                        Loggers.general().info(LOG,"Total Nostro Credit amount-940=====================-->" + totalValue1);
//                                        Loggers.general().info(LOG,"poollll amount-====940=================-->" + totalValue2);
//
//
//                                  }
//                                  String finalVal1 = String.format("%.2f", totalValue1);
//                                  String finalVal2 = String.format("%.2f", totalValue2);
//                                  if (dailyval_Log.equalsIgnoreCase("YES")) {
//                                        Loggers.general().info(LOG,"Final Nostro Credit amount==940=========--->" + finalVal1);
//                                        Loggers.general().info(LOG,"Final poolll amount===940========--->" + finalVal2);
//
//                                  }
//                                  setNOSTAMT(finalVal1 + " " + Credit_curMT103202);
//
//                                  setPOOLAMT(finalVal2 + " " + Credit_curMT940950);
//
//                                  // String dms = "SELECT NVL(SUM(NOSUTLAMT),0) AS
//                                  // OUTSAMOUNT FROM (SELECT SUM(ext.NOSAMT) AS NOSUTLAMT,
//                                  // mas.MASTER_REF FROM master mas, BASEEVENT bas,
//                                  // extevent ext WHERE mas.KEY97 =bas.MASTER_KEY AND
//                                  // bas.KEY97 =ext.EVENT AND bas.STATUS IN ('i','c') AND
//                                  // (mas.MASTER_REF ='"
//                                  // + masReference + "' AND trim(bas.REFNO_PFIX
//                                  // ||LPAD(bas.REFNO_SERL,3,0)) !='" + eventCode
//                                  // + "') AND ext.NOSTRM ='" + nostref_MT940950
//                                  // + "' GROUP BY mas.MASTER_REF UNION ALL SELECT
//                                  // SUM(ext.NOSAMT) AS NOSUTLAMT, mas.MASTER_REF FROM
//                                  // master mas, BASEEVENT bas, extevent ext WHERE
//                                  // mas.KEY97 =bas.MASTER_KEY AND bas.KEY97 =ext.EVENT
//                                  // AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF !='"
//                                  // + masReference + "' AND trim(bas.REFNO_PFIX
//                                  // ||LPAD(bas.REFNO_SERL,3,0)) !='" + eventCode
//                                  // + "') AND ext.NOSTRM ='" + nostref_MT940950
//                                  // + "' GROUP BY mas.MASTER_REF ) NOSRTOAMOUNT";
//
//                                  String dms = "SELECT NVL(SUM(NOSUTLAMT),0) AS OUTSAMOUNT FROM (SELECT SUM(ext.NOSAMT) AS NOSUTLAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, extevent ext WHERE mas.KEY97 =bas.MASTER_KEY AND bas.KEY97 =ext.EVENT AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF ='"
//                                              + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) !='" + eventCode
//                                              + "') AND ext.NOSTRM ='" + nostref_MT940950
//                                              + "' GROUP BY mas.MASTER_REF UNION ALL SELECT SUM(ext.NOSAMT) AS NOSUTLAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, extevent ext WHERE mas.KEY97 =bas.MASTER_KEY AND bas.KEY97 =ext.EVENT AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF !='"
//                                              + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) !='" + eventCode
//                                              + "') AND ext.NOSTRM ='" + nostref_MT940950
//                                              + "' GROUP BY mas.MASTER_REF UNION ALL SELECT SUM(ext.NOSAMT) AS NOSUTLAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, extevent ext WHERE mas.KEY97 =bas.MASTER_KEY AND bas.KEY97 =ext.EVENT AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF !='"
//                                              + masReference + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) ='" + eventCode
//                                              + "') AND ext.NOSTRM ='" + nostref_MT940950
//                                              + "' GROUP BY mas.MASTER_REF ) NOSRTOAMOUNT";
//
//                                  if (dailyval_Log.equalsIgnoreCase("YES")) {
//                                        Loggers.general().info(LOG,"Nostro outsanding amount--->" + dms);
//
//                                  }
//                                  ps1 = con.prepareStatement(dms);
//                                  rs = ps1.executeQuery();
//                                  while (rs.next()) {
//                                        String nostOut = rs.getString(1).trim();
//                                        BigDecimal nostOutBig = new BigDecimal(nostOut);
//                                        if (dailyval_Log.equalsIgnoreCase("YES")) {
//                                              Loggers.general().info(LOG,"Result of Nostro outsanding amount--->" + nostOutBig);
//
//                                        }
//                                        BigDecimal CreditBig = new BigDecimal(Credit_AmountMT940950);
//                                        if (dailyval_Log.equalsIgnoreCase("YES")) {
//                                              Loggers.general().info(LOG,"Nostro Credit amount--->" + CreditBig);
//
//                                        }
//                                        BigDecimal totalVal = CreditBig.subtract(nostOutBig);
//
//                                        ConnectionMaster connectionMaster = new ConnectionMaster();
//                                        double divideByDecimal = connectionMaster.getDecimalforCurrency(Credit_curMT940950);
//                                        BigDecimal divideByBig = new BigDecimal(divideByDecimal);
//                                        BigDecimal totalValue = totalVal.divide(divideByBig);
//
//                                        if (dailyval_Log.equalsIgnoreCase("YES")) {
//                                              Loggers.general().info(LOG,"Total Nostro Credit amount--->" + totalValue);
//
//                                        }
//                                        String finalVal = String.format("%.2f", totalValue);
//                                        if (dailyval_Log.equalsIgnoreCase("YES")) {
//                                              Loggers.general().info(LOG,"Final Nostro Credit amount--->" + finalVal);
//
//                                        }
//                                        if (totalValue.compareTo(BigDecimal.ZERO) >= 0) {
//                                              setNOSTOUT(finalVal + " " + Credit_curMT940950);
//                                        } else {
//                                              finalVal = "0";
//                                              setNOSTOUT(finalVal + " " + Credit_curMT940950);
//
//                                        }
//
//                                  }
//                                  
//                                  String query_940 = "SELECT TRIM( 'MessageDetails:'||SUBSTR(MESSAGE_DATA,1, INSTR(MESSAGE_DATA,':61:')-1 ) )AS MSGDATA,TRIM('TAG 61:'||IND_TEXT) AS IND_TEXT,TRIM('TAG 86:'||ACC_OWNER_INFO) AS ACC_OWNER_INFO FROM ETT_NOSTRO_UTILITY_MT940 where IND_NOS_CUS_REFNO='"+ nostref_MT940950 + "' or IND_NOS_BANK_REFNO='"+ nostref_MT940950 + "'";
//                                  //// Loggers.general().info(LOG,"Nostro swift_type is
//                                  //// query_103
//                                  //// " + query_103);
//                                  ps3 = con.prepareStatement(query_940);
//                                  rs3 = ps3.executeQuery();
//                                  while (rs3.next()) {
//                                        String msg1 = rs3.getString(1);
//                                        String msg2 = rs3.getString(2);
//                                        String msg3 = rs3.getString(3);
//                                        String fullmsg = msg1 + " \n " + msg2 + " \n " + msg3;
//                                        setMTMESG(fullmsg);
//                                  }
//                                  String query_950 = "SELECT TRIM( 'MessageDetails:'||SUBSTR(MESSAGE_DATA,1, INSTR(MESSAGE_DATA,':61:')-1 ) )AS MSGDATA,TRIM('TAG 61:'||IND_TEXT) AS IND_TEXT FROM ETT_NOSTRO_UTILITY_MT950 where IND_NOS_CUS_REFNO='"+ nostref_MT940950 + "' or IND_NOS_BANK_REFNO='"+ nostref_MT940950 + "'";
//                                  //// Loggers.general().info(LOG,"Nostro swift_type is
//                                  //// query_103
//                                  //// " + query_103);
//                                  ps4 = con.prepareStatement(query_950);
//                                  rs4 = ps4.executeQuery();
//                                  while (rs4.next()) {
//                                        String msg1 = rs4.getString(1);
//                                        String msg2 = rs4.getString(2);
//                                        
//                                        String fullmsg = msg1 + " \n " + msg2 ;
//                                        setMTMESG(fullmsg);
//                                  }
//
//                                  if (swift_type.equalsIgnoreCase("103")) {
//                                        String query_103 = "SELECT MESSAGE_DATA FROM ETT_NOSTRO_UTILITY_MT103 WHERE REFERENCE_NUMBER ='"
//                                                    + nostref_MT103 + "'";
//                                        //// Loggers.general().info(LOG,"Nostro swift_type is
//                                        //// query_103
//                                        //// " + query_103);
//                                        ps = con.prepareStatement(query_103);
//                                        rs = ps.executeQuery();
//                                        while (rs.next()) {
//                                              String swift = rs.getString(1);
//                                              setINWMSG(swift);
//                                        }
//                                  } else if (swift_type.equalsIgnoreCase("202")) {
//                                        String query_202 = "SELECT MESSAGE_DATA FROM ETT_NOSTRO_UTILITY_MT202 WHERE REFERENCE_NUMBER ='"
//                                                    + nostref_MT103 + "'";
//                                        //// Loggers.general().info(LOG,"Nostro swift_type is
//                                        //// query_202
//                                        //// " + query_202);
//
//                                        ps = con.prepareStatement(query_202);
//                                        rs = ps.executeQuery();
//                                        while (rs.next()) {
//                                              String swift = rs.getString(1);
//                                              setINWMSG(swift);
//                                        }
//                                  } else {
//                                        //// Loggers.general().info(LOG,"Nostro swift_type is empty
//                                        //// " +
//                                        //// swift_type);
//
//                                        setINWMSG("");
//
//                                  }
//
//                            }
//
//                      } catch (Exception ee) {
//                            Loggers.general().info(LOG,"Exception Inward reference 940" + ee.getMessage());
//                      }
//
//                } else {
//                      setNOSTAMT("");
//                      setNOSTDAT("");
//                      setPOOLAMT("");
//                      setNOSTACC("");
//                      setMTMESG("");
//                      setINWMSG("");
//                      setNOSTOUT("");
//
//                }
//
//                try {
//
//                      String query = "";
//                      if (dailyval_Log.equalsIgnoreCase("YES")) {
//                            Loggers.general().info(LOG,"Nostro ref no nostref_MT103102" + nostref_MT103102);
//                      }
//                      // String nostref_MT940950 = getWrapper().getNOSTRM().trim();
//
//                      if (nostref_MT103102.length() > 0) {
//
//                            query = "SELECT count(*) as COUNT FROM ETTV_NOSTRO_MT103_MT202_TBL WHERE MT103_MT202_REFERENCE_NUMBER='"
//                                        + nostref_MT103102 + "'";
//                            if (dailyval_Log.equalsIgnoreCase("YES")) {
//                                  Loggers.general().info(LOG,"Nostro ref no ETTV_NOSTRO_MT103_MT202_TBL query" + query);
//                            }
//                            con = ConnectionMaster.getConnection();
//                            ps = con.prepareStatement(query);
//                            rs = ps.executeQuery();
//                            int val = 0;
//                            if (rs.next()) {
//                                  val = rs.getInt(1);
//
//                            }
//
//                            query = "select count(*) from ETT_NOSTRO_UTILITY_MT103 M where M.QUEUE_TYPE='CLSQ' and M.REFERENCE_NUMBER='"
//                                        + nostref_MT103102 + "'";
//                            if (dailyval_Log.equalsIgnoreCase("YES")) {
//                                  Loggers.general().info(LOG,"Nostro ref no ETT_NOSTRO_UTILITY_MT103 query" + query);
//                            }
//                            con = ConnectionMaster.getConnection();
//                            ps = con.prepareStatement(query);
//                            rs = ps.executeQuery();
//                            int value103 = 0;
//                            if (rs.next()) {
//                                  value103 = rs.getInt(1);
//
//                            }
//
//                            if (dailyval_Log.equalsIgnoreCase("YES")) {
//                                  Loggers.general().info(LOG,"Nostro ref no nostref_MT103102 count" + val + "value103" + value103);
//                            }
//                            if ((nostref_MT103102.length() > 0) && (val == 0 && value103 == 0)) {
//                                  setNOSTAMT("");
//                                  setNOSTDAT("");
//                                  setPOOLAMT("");
//                                  setNOSTACC("");
//                                  setMTMESG("");
//                                  setINWMSG("");
//                                  setNOSTOUT("");
//                            } else {
//                                  if (dailyval_Log.equalsIgnoreCase("YES")) {
//                                        Loggers.general().info(LOG,"MT103 Nostro Reference else" + val);
//                                  }
//                            }
//
//                      } else if (nostref_MT940950.length() > 0) {
//                            query = "SELECT count(*) FROM ETTV_NOSTRO_MT103_MT202_TBL WHERE MT940_MT950_REFERENCE_NUMBER='"
//                                        + nostref_MT940950 + "'";
//                            if (dailyval_Log.equalsIgnoreCase("YES")) {
//                                  Loggers.general().info(LOG,"Nostro ref no nostref_MT940 query" + query);
//                            }
//                            ps = con.prepareStatement(query);
//                            rs = ps.executeQuery();
//                            int val = 0;
//                            while (rs.next()) {
//                                  val = rs.getInt(1);
//                            }
//                            if (dailyval_Log.equalsIgnoreCase("YES")) {
//                                  Loggers.general().info(LOG,"Nostro ref no nostref_MT940 count" + val);
//                            }
//                            if ((nostref_MT940950.length() > 0) && val == 0) {
//                                  setNOSTAMT("");
//                                  setNOSTDAT("");
//                                  setPOOLAMT("");
//                                  setNOSTACC("");
//                                  setMTMESG("");
//                                  setINWMSG("");
//                                  setNOSTOUT("");
//
//                            } else {
//                                  if (dailyval_Log.equalsIgnoreCase("YES")) {
//                                        Loggers.general().info(LOG,"MT940 Nostro Reference else" + val);
//                                  }
//                            }
//                      }
//
//                } catch (Exception e1) {
//                      if (dailyval_Log.equalsIgnoreCase("YES")) {
//                            Loggers.general().info(LOG,"Exception Nostro ref no validation" + e1.getMessage());
//                      }
//                }
//
                  String forwardUid = getFDTRSUID().trim();
                  String nostro_Reference_no1 = "";
                  String nostro_Reference_no2 = "";
                  String nostro_value_date = "";
                  con = ConnectionMaster.getConnection();
                  String Credit_Amount = "";
                  String Credit_currency = "";
                  String CreditAccount = "";
                  String available_Amount = "";

                  String query = "SELECT AMOUNT AS Credit_Amount, CCY AS Credit_currency, TO_CHAR(VALUE_DATE,'DD/MM/YY') AS nostro_value_date,  NOSTRO AS CreditAccount,INST_REF_NUM,OWNER_REF_NUM FROM UID_CREDIT_DAILY_TBL WHERE UNIQUE_ID='"
                              + forwardUid + "'";
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        System.out.println("Nostro ETTV_NOSTRO_MT103_MT202_TBL value query " + query);
                  }

                  ps = con.prepareStatement(query);
                  rs = ps.executeQuery();
                  if (rs.next()) {
                        // setNOSTOUT(Credit_AmountMT103202 + " " + Credit_curMT103202);
                        nostro_value_date = rs.getString(3);

                        Credit_Amount = rs.getString(1);
                        Credit_currency = rs.getString(2);
                        nostro_Reference_no1 = rs.getString(6);
                        nostro_Reference_no2 = rs.getString(5);
                        CreditAccount = rs.getString(4);
                        System.out.println("Nostro values  details===>" + Credit_Amount + " " + nostro_Reference_no2 + " "
                                    + CreditAccount);
                        setNOSTMT(nostro_Reference_no1);
                        setNOSTRM(nostro_Reference_no2);
                        setNOSTDAT(nostro_value_date);
                        setPOOLAMT(Credit_Amount + " " + Credit_currency);
                        setNOSTACC(CreditAccount);
                  }
//          String amountquery = "SELECT UID_AVAILABLE_AMOUNT||' '||ccy as availableAmount from REP_UID_AVAILABLE_AMOUNT where UNIQUE_ID='"+ forwardUid + "'";
//          ps1 = con.prepareStatement(amountquery);
//          rs1 = ps1.executeQuery();
//          if (rs1.next()) {
//                available_Amount=rs1.getString(1);
//                System.out.println("aVAILABLE AMOUNT "+available_Amount);
//                setNOSTAMT(available_Amount);
//          }
            } catch (Exception e) {
                  e.printStackTrace();
            }

            finally {
                  try {
                        if (rs != null)
                              rs.close();
                        if (ps != null)
                              ps.close();
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

      }

      public void ononfetchINWRMTCUSclayButton() {

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
            String inwnum = "";
            ResultSet rus = null;
            ResultSet rs = null;
            PreparedStatement pst = null;
            PreparedStatement ps = null;
            Double utilisationAmount1 = 0.00;
            Double balAmt = 0.00;
            Double finalAvailableAmt = 0.00;
            Double lcamt = 0.00;
            Double amt = 0.00;
            String lc = "";
            String currency = "";
            String lcAmount = "";
            String curvalue = "";

            String val = "";
            double totalBillAmt = 0.00;
            Double totalAmt = 0.00;
            String Acurrency = "";
            double cross = 0;
            double curamt = 0;
            String utilisationAmountt = "";
            String utilcur = "";
            String eqamt1 = "";
            String eqamt = "";
            String balance = "";

            try {
                  con = ConnectionMaster.getConnection();
                  EnigmaArray<ExtEventAdvanceTableEntityWrapper> liste = getExtEventAdvanceTableData();
                  for (int i = 0; i < liste.getSize().intValue(); i++) {
                        // Loggers.general().info(LOG,"size of fixed deposit :" +
                        // liste.getSize());
                        Iterator<ExtEventAdvanceTableEntityWrapper> iterator1 = liste.iterator();
                        while (iterator1.hasNext()) {
                              ExtEventAdvanceTableEntityWrapper fdwarapper1 = (ExtEventAdvanceTableEntityWrapper) iterator1
                                          .next();
                              inwnum = fdwarapper1.getINWARD().trim();// from user inward
                              // remittance no
                              // Loggers.general().info(LOG,"inward remittance no" + inwnum);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG, "inward remittance no" + inwnum);
                              }
                              utilisationAmountt = fdwarapper1.getAMTUTILAmount();// from
                                                                                                            // user
                                                                                                            // utiliszation
                                                                                                            // amount
                              Double utilisationAmt1 = Double.parseDouble(utilisationAmountt);
                              utilisationAmount1 = utilisationAmt1 / 100;
                              // Loggers.general().info(LOG,"the utilisation amount" +
                              // utilisationAmountt);
                              // String billCur =
                              // String.valueOf(fdwarapper1.getAMTEQUCurrency());
                              utilcur = fdwarapper1.getAMTUTILCurrency();

                              // Loggers.general().info(LOG,"grid currency " + utilcur);
                              // String totalAmountQuery = "select
                              // trim(amount),trim(ccy)from master where
                              // trim(master_ref)='"+inwnum+"'";
                              String totalAmountQuery = "select trim(mas.amount),mas.ccy from master mas,baseevent bas  where mas.key97=bas.master_key and mas.master_ref='"
                                          + inwnum + "'";
                              con = ConnectionMaster.getConnection();
                              pst = con.prepareStatement(totalAmountQuery);
                              rus = pst.executeQuery();
                              while (rus.next()) {

                                    lcAmount = rus.getString(1);
                                    Acurrency = rus.getString(2).trim();
                                    // Loggers.general().info(LOG,"lc amount is " + lcAmount);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG, "lc amount is " + lcAmount);
                                    }
                                    lcamt = Double.valueOf(lcAmount);
                                    lcamt = lcamt / 100;
                                    // Loggers.general().info(LOG,"lc amount in double" + lcamt);
                              }

                              String utiliseAmountQuery = "select nvl(sum(amtutil),0) from exteventadv ext,baseevent bev where ext.fk_event=bev.key97 and bev.status='c'and ext.inward='"
                                          + inwnum + "'";
                              ps = con.prepareStatement(utiliseAmountQuery);
                              rs = ps.executeQuery();
                              // Double overallUtilAmt = 0.0;
                              // Double currentUtilAmt=0.00;
                              while (rs.next()) {
                                    // Loggers.general().info(LOG,"enter into while for utilised
                                    // amount");
                                    String currentUtilAmt = rs.getString(1);
                                    curamt = Double.valueOf(currentUtilAmt);
                                    // Loggers.general().info(LOG," utilised amount " + curamt);
                                    amt = curamt / 100;
                                    // overallUtilAmt = overallUtilAmt + currentUtilAmt;
                              }
                              String query = "select trim(SPOTRATE),trim(currency) from spotrate where currency='" + Acurrency
                                          + "'";
                              // Loggers.general().info(LOG,"query 3 value" + query);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG, "query 3 value" + query);
                              }
                              pst = con.prepareStatement(query);
                              rus = pst.executeQuery();
                              while (rus.next()) {
                                    curvalue = rus.getString(1);
                                    // Loggers.general().info(LOG,"currency" + curvalue);
                                    cross = Double.parseDouble(curvalue);
                                    // Loggers.general().info(LOG,"currency double value" + cross);
                                    currency = rus.getString(2);
                                    // Loggers.general().info(LOG,"the currency type" + currency);
                              }

                              if (Acurrency.equalsIgnoreCase(utilcur)) {

                                    if (lcamt != null) {
                                          balAmt = lcamt - amt;
                                          // Loggers.general().info(LOG," Balance amount " + balAmt);

                                          // Loggers.general().info(LOG," utilazation amount Currency
                                          // -->" + utilcur);

                                          if (balAmt > 0) {
                                                // fdwarapper1.setNOTIONL("1.0");
                                                Double finalAvailableAmount = balAmt - utilisationAmount1;
                                                finalAvailableAmt = finalAvailableAmount * 100;
                                                // Loggers.general().info(LOG,"final amount " +
                                                // finalAvailableAmt);
                                                balance = String.valueOf(finalAvailableAmt);
                                                eqamt = String.valueOf(utilisationAmount1);
                                                fdwarapper1.setBALANCE(balance + " " + Acurrency);
                                                // fdwarapper1.setAMTEQU(balance+ " " +
                                                // Acurrency);
                                                // Loggers.general().info(LOG,"setBALANCE -----> " +
                                                // fdwarapper1.getBALANCE());

                                                String balcur = fdwarapper1.getBALANCECurrency();

                                                // Loggers.general().info(LOG,"balance amount currency "
                                                // + balcur);
                                                // Double billAmt =
                                                // Double.valueOf(String.valueOf(fdwarapper1.getAMTEQUAmount()));
                                                // totalBillAmt = totalBillAmt + billAmt;
                                                // Loggers.general().info(LOG,"the total bill Amt --->"
                                                // + totalBillAmt);
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG, "the total bill Amt --->" + totalBillAmt);
                                                }
                                                totalAmt = totalBillAmt / 100;
                                                val = String.format("%.2f", totalAmt);
                                                setBALAMT(val + " " + Acurrency);

                                          }
                                    }
                              } else {
                                    if (lcamt != null) {
                                          balAmt = lcamt * cross - amt;
                                          // Loggers.general().info(LOG," Balance amount " + balAmt);

                                          // Loggers.general().info(LOG," utilazation amount Currency
                                          // -->" + utilcur);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG, " utilazation amount Currency -->" + utilcur);
                                          }

                                          if (balAmt > 0) {
                                                // fdwarapper1.setNOTIONL(curvalue);
                                                Double finalAvailableAmount = balAmt - utilisationAmount1;
                                                finalAvailableAmt = finalAvailableAmount * 100;
                                                Double finalAmt = finalAvailableAmount * 100 / cross;
                                                // Loggers.general().info(LOG,"final amount for balance
                                                // " + finalAmt);
                                                // Loggers.general().info(LOG,"final amount for bill
                                                // equalent" + finalAvailableAmt);
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG, "final amount for bill equalent" + finalAvailableAmt);
                                                }
                                                eqamt1 = String.valueOf(finalAvailableAmt);
                                                // eqamt=String.format("%.2f", eqamt1);
                                                balance = String.valueOf(finalAmt);
                                                // balVal=String.format("%.2f", balance);
                                                fdwarapper1.setBALANCE(balance + " " + Acurrency);
                                                // fdwarapper1.setAMTEQU(eqamt1+ " " +utilcur );
                                                // Loggers.general().info(LOG,"setBALANCE -----> " +
                                                // fdwarapper1.getBALANCE());

                                                // Double billAmt =
                                                // Double.valueOf(String.valueOf(fdwarapper1.getAMTEQUAmount()));
                                                // totalBillAmt = totalBillAmt + billAmt;
                                                // Loggers.general().info(LOG,"the total bill Amt --->"
                                                // + totalBillAmt);
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG, "the total bill Amt --->" + totalBillAmt);
                                                }
                                                totalAmt = totalBillAmt / 100;
                                                val = String.format("%.2f", totalAmt);
                                                // setBALAMT(val + " " + utilcur);
                                          }

                                    }
                              }

                              String Query = "select TRIM(REMITTER_NAME),trim(to_char(value_date,'DD/MM/YY')),TRIM(REMITTER_COUNTRY) FROM ettv_firc_lodgement_view,MASTER MAS where MAS.master_ref='"
                                          + inwnum + "'";
                              // Loggers.general().info(LOG," Query for remiter name " + Query);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG, " Query for remiter name " + Query);
                              }
                              pst = con.prepareStatement(Query);
                              rus = pst.executeQuery();

                              while (rus.next()) {
                                    // Loggers.general().info(LOG,"enter into while for remiter
                                    // name");
                                    String remname = rus.getString(1);
                                    fdwarapper1.setNAMREM(remname);
                                    String remcdat = rus.getString(2);
                                    fdwarapper1.setDATREM(remcdat);
                                    fdwarapper1.setCOUNREM(rus.getString(3));
                              }
                        }
                  }

            } catch (Exception e) {
                  //// Loggers.general().info(LOG,"Got inward remittance excepton ");
                  // Loggers.general().info(LOG,"e.getMessage - " + e.getMessage());
                  //// Loggers.general().info(LOG,);
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG, "e.getMessage - " + e.getMessage());
                  }
            }

      }

      public void onGURANTEEOGTISSclayButton() {
            counterGuarantee();
      }

      public void onGURANTEEOGTINVOCclayButton() {
            counterGuarantee();
      }

      public void onGURANTEEIMPGUAADJclayButton() {
            counterGuarantee();
      }

      public void onGURANTEEIMPGUAAMDclayButton() {
            counterGuarantee();
      }

      public boolean counterGuarantee() {
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

            BigDecimal Amount100 = new BigDecimal("100");
            BigDecimal Amount1000 = new BigDecimal("1000");
            BigDecimal eligibleVal = new BigDecimal("0");
            String eligibleValule = "";
            String mamt = getDriverWrapper().getEventFieldAsText("MST", "r", "");
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
                                    Loggers.general().info(LOG, "Counter guarantee Master amount in query " + query);
                              }
                              ps = con.prepareStatement(query);
                              rs = ps.executeQuery();
                              if (rs.next()) {
                                    String eligible_amt = rs.getString(1);
                                    BigDecimal eligible_Amount = new BigDecimal(eligible_amt);
                                    String getced = "select c8ced from c8pf where c8ccy='" + rs.getString(2) + "'";
                                    ps = con.prepareStatement(getced);
                                    // Loggers.general().info(LOG,"sql" + getced);
                                    rs = ps.executeQuery();
                                    int a = 0;
                                    while (rs.next()) {
                                          a = rs.getInt(1);
                                    }
                                    if (a == 2) {

                                          eligibleVal = eligible_Amount.divide(Amount100);
                                          DecimalFormat diff = new DecimalFormat("0.00");
                                          diff.setMaximumFractionDigits(2);
                                          eligibleValule = diff.format(eligibleVal);
                                    } else if (a == 3) {
                                          DecimalFormat diff = new DecimalFormat("0.000");
                                          diff.setMaximumFractionDigits(3);
                                          eligibleVal = eligible_Amount.divide(Amount1000);
                                          eligibleValule = diff.format(eligibleVal);
                                    } else {
                                          DecimalFormat diff = new DecimalFormat("0");
                                          diff.setMaximumFractionDigits(0);
                                          eligibleValule = diff.format(eligibleVal);
                                    }
                                    // Loggers.general().info(LOG,"Total eligible_amt--->" +
                                    // eligible_amt);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG, "Total eligible_amt--->" + eligibleVal);

                                    }

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,
                                                      "Final counter value--->" + eligibleValule + "" + rs.getString(2));

                                    }
                                    fdwrapper.setELIAMT(eligibleValule + " " + rs.getString(2));

                                    fdwrapper.setEXDATE(rs.getString(3));

                              }

                        }
                  }
            } catch (Exception e) {
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG, "Exception in Counter guarantee Master amount " + e.getMessage());
                  }
            } finally {
                  try {
                        if (rs != null)
                              rs.close();
                        if (ps != null)
                              ps.close();
                        if (con != null)
                              con.close();
                  } catch (SQLException e) {
                        //// Loggers.general().info(LOG,"Connection Failed! Check output
                        //// console");
                        e.printStackTrace();
                  }
            }
            return true;

      }

      /*
       * public void onGURANTEEOGTISSclayButton() { String strLog = "Log"; String
       * dailyval_Log = "";
       *
       * @SuppressWarnings("unchecked") AdhocQuery<? extends
       * com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog =
       * getDriverWrapper() .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog +
       * "'"); EXTGENCUSTPROP CodeLog = queryLog.getUnique(); if (CodeLog != null) {
       *
       * dailyval_Log = CodeLog.getPropval(); } else { //
       * Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");
       *
       * }
       *
       * BigDecimal Amount100 = new BigDecimal("100"); BigDecimal Amount1000 = new
       * BigDecimal("1000"); BigDecimal eligibleVal = new BigDecimal("0"); String mamt
       * = getDriverWrapper().getEventFieldAsText("MST", "r", ""); try {
       *
       * // Getting the Table Structure and Size
       * EnigmaArray<ExtEventCounterGuranteeEntityWrapper> list =
       * getExtEventCounterGuranteeData(); for (int i = 0; i <
       * list.getSize().intValue(); i++) {
       * Iterator<ExtEventCounterGuranteeEntityWrapper> iterator1 = list.iterator();
       * // Getting the table Values while (iterator1.hasNext()) {
       * ExtEventCounterGuranteeEntityWrapper fdwrapper =
       * (ExtEventCounterGuranteeEntityWrapper) iterator1 .next(); con =
       * ConnectionMaster.getConnection(); String guaranteeNumber =
       * fdwrapper.getGURNO().trim(); String query =
       * "select trim(mas.amount), mas.ccy , to_char( mas.expiry_dat,'dd/mm/yy' ) from master mas,baseevent bas  where mas.key97=bas.master_key and mas.master_ref='"
       * + guaranteeNumber + "'"; //
       * Loggers.general().info(LOG,"Master amount in query " + query); if
       * (dailyval_Log.equalsIgnoreCase("YES")) {
       * Loggers.general().info(LOG,"Counter guarantee Master amount in query " +
       * query); } ps = con.prepareStatement(query); rs = ps.executeQuery(); if
       * (rs.next()) { String eligible_amt = rs.getString(1); BigDecimal
       * eligible_Amount = new BigDecimal(eligible_amt); String getced =
       * "select c8ced from c8pf where c8ccy='" + rs.getString(2) + "'"; ps =
       * con.prepareStatement(getced); // Loggers.general().info(LOG,"sql" + getced);
       * rs = ps.executeQuery(); int a = 0; while (rs.next()) { a = rs.getInt(1); } if
       * (a == 2) {
       *
       * eligibleVal = eligible_Amount.divide(Amount100); } if (a == 3) { eligibleVal
       * = eligible_Amount.divide(Amount1000); } //
       * Loggers.general().info(LOG,"Total eligible_amt--->" + // eligible_amt); if
       * (dailyval_Log.equalsIgnoreCase("YES")) {
       * Loggers.general().info(LOG,"Total eligible_amt--->" + eligibleVal);
       *
       * }
       *
       * DecimalFormat diff = new DecimalFormat("0.00");
       * diff.setMaximumFractionDigits(2); String eligibleValule =
       * diff.format(eligibleVal); if (dailyval_Log.equalsIgnoreCase("YES")) {
       * Loggers.general().info(LOG,"Final counter value--->" + eligibleValule + "" +
       * rs.getString(2));
       *
       * } fdwrapper.setELIAMT(eligibleValule + " " + rs.getString(2));
       *
       * fdwrapper.setEXDATE(rs.getString(3));
       *
       * }
       *
       * } } } catch (Exception e) { if (dailyval_Log.equalsIgnoreCase("YES")) {
       * Loggers.general().info(LOG,"Exception in Counter guarantee Master amount " +
       * e.getMessage()); } } finally { try { if (rs != null) rs.close(); if (ps !=
       * null) ps.close(); if (con != null) con.close(); } catch (SQLException e) {
       * //// Loggers.general().info(LOG,"Connection Failed! Check output ////
       * console"); e.printStackTrace(); } }
       *
       * }
       */

      // todo on sep 20

      /*
       * public void onGURANTEEIMPSTDLCISSclayButton() { String strLog = "Log"; String
       * dailyval_Log = "";
       *
       * @SuppressWarnings("unchecked") AdhocQuery<? extends
       * com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog =
       * getDriverWrapper() .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog +
       * "'"); EXTGENCUSTPROP CodeLog = queryLog.getUnique(); if (CodeLog != null) {
       *
       * dailyval_Log = CodeLog.getPropval(); } else { //
       * Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");
       *
       * }
       *
       * BigDecimal Amount100 = new BigDecimal("100"); BigDecimal Amount1000 = new
       * BigDecimal("1000"); BigDecimal eligibleVal = new BigDecimal("0"); String mamt
       * = getDriverWrapper().getEventFieldAsText("MST", "r", ""); try {
       *
       * // Getting the Table Structure and Size
       * EnigmaArray<ExtEventCounterGuranteeEntityWrapper> list =
       * getExtEventCounterGuranteeData(); for (int i = 0; i <
       * list.getSize().intValue(); i++) {
       * Iterator<ExtEventCounterGuranteeEntityWrapper> iterator1 = list.iterator();
       * // Getting the table Values while (iterator1.hasNext()) {
       * ExtEventCounterGuranteeEntityWrapper fdwrapper =
       * (ExtEventCounterGuranteeEntityWrapper) iterator1 .next(); con =
       * ConnectionMaster.getConnection(); String guaranteeNumber =
       * fdwrapper.getGURNO().trim(); String query =
       * "select trim(mas.amount), mas.ccy , to_char( mas.expiry_dat,'dd/mm/yy' ) from master mas,baseevent bas  where mas.key97=bas.master_key and mas.master_ref='"
       * + guaranteeNumber + "'"; //
       * Loggers.general().info(LOG,"Master amount in query " + query); if
       * (dailyval_Log.equalsIgnoreCase("YES")) {
       * Loggers.general().info(LOG,"Counter guarantee Master amount in query " +
       * query); } ps = con.prepareStatement(query); rs = ps.executeQuery(); if
       * (rs.next()) { String eligible_amt = rs.getString(1); BigDecimal
       * eligible_Amount = new BigDecimal(eligible_amt); String getced =
       * "select c8ced from c8pf where c8ccy='" + rs.getString(2) + "'"; ps =
       * con.prepareStatement(getced); // Loggers.general().info(LOG,"sql" + getced);
       * rs = ps.executeQuery(); int a = 0; while (rs.next()) { a = rs.getInt(1); } if
       * (a == 2) {
       *
       * eligibleVal = eligible_Amount.divide(Amount100); } if (a == 3) { eligibleVal
       * = eligible_Amount.divide(Amount1000); } //
       * Loggers.general().info(LOG,"Total eligible_amt--->" + // eligible_amt); if
       * (dailyval_Log.equalsIgnoreCase("YES")) {
       * Loggers.general().info(LOG,"Total eligible_amt--->" + eligibleVal);
       *
       * }
       *
       * DecimalFormat diff = new DecimalFormat("0.00");
       * diff.setMaximumFractionDigits(2); String eligibleValule =
       * diff.format(eligibleVal); if (dailyval_Log.equalsIgnoreCase("YES")) {
       * Loggers.general().info(LOG,"Final counter value--->" + eligibleValule + "" +
       * rs.getString(2));
       *
       * } fdwrapper.setELIAMT(eligibleValule + " " + rs.getString(2));
       *
       * fdwrapper.setEXDATE(rs.getString(3));
       *
       * }
       *
       * } } } catch (Exception e) { if (dailyval_Log.equalsIgnoreCase("YES")) {
       * Loggers.general().info(LOG,"Exception in Counter guarantee Master amount " +
       * e.getMessage()); } } finally { try { if (rs != null) rs.close(); if (ps !=
       * null) ps.close(); if (con != null) con.close(); } catch (SQLException e) {
       * //// Loggers.general().info(LOG,"Connection Failed! Check output ////
       * console"); e.printStackTrace(); } }
       *
       * }
       *
       */

      /*
       * public void onGURANTEEOGTINVOCclayButton() { String strLog = "Log"; String
       * dailyval_Log = "";
       *
       * @SuppressWarnings("unchecked") AdhocQuery<? extends
       * com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog =
       * getDriverWrapper() .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog +
       * "'"); EXTGENCUSTPROP CodeLog = queryLog.getUnique(); if (CodeLog != null) {
       *
       * dailyval_Log = CodeLog.getPropval(); } else { //
       * Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");
       *
       * }
       *
       * BigDecimal Amount100 = new BigDecimal("100"); BigDecimal Amount1000 = new
       * BigDecimal("1000"); BigDecimal eligibleVal = new BigDecimal("0"); String mamt
       * = getDriverWrapper().getEventFieldAsText("MST", "r", ""); try {
       *
       * // Getting the Table Structure and Size
       * EnigmaArray<ExtEventCounterGuranteeEntityWrapper> list =
       * getExtEventCounterGuranteeData(); for (int i = 0; i <
       * list.getSize().intValue(); i++) {
       * Iterator<ExtEventCounterGuranteeEntityWrapper> iterator1 = list.iterator();
       * // Getting the table Values while (iterator1.hasNext()) {
       * ExtEventCounterGuranteeEntityWrapper fdwrapper =
       * (ExtEventCounterGuranteeEntityWrapper) iterator1 .next(); con =
       * ConnectionMaster.getConnection(); String guaranteeNumber =
       * fdwrapper.getGURNO().trim(); String query =
       * "select trim(mas.amount), mas.ccy , to_char( mas.expiry_dat,'dd/mm/yy' ) from master mas,baseevent bas  where mas.key97=bas.master_key and mas.master_ref='"
       * + guaranteeNumber + "'"; //
       * Loggers.general().info(LOG,"Master amount in query " + query); if
       * (dailyval_Log.equalsIgnoreCase("YES")) {
       * Loggers.general().info(LOG,"Counter guarantee Master amount in query " +
       * query); } ps = con.prepareStatement(query); rs = ps.executeQuery(); if
       * (rs.next()) { String eligible_amt = rs.getString(1); BigDecimal
       * eligible_Amount = new BigDecimal(eligible_amt); String getced =
       * "select c8ced from c8pf where c8ccy='" + rs.getString(2) + "'"; ps =
       * con.prepareStatement(getced); // Loggers.general().info(LOG,"sql" + getced);
       * rs = ps.executeQuery(); int a = 0; while (rs.next()) { a = rs.getInt(1); } if
       * (a == 2) {
       *
       * eligibleVal = eligible_Amount.divide(Amount100); } if (a == 3) { eligibleVal
       * = eligible_Amount.divide(Amount1000); } //
       * Loggers.general().info(LOG,"Total eligible_amt--->" + // eligible_amt); if
       * (dailyval_Log.equalsIgnoreCase("YES")) {
       * Loggers.general().info(LOG,"Total eligible_amt--->" + eligibleVal);
       *
       * }
       *
       * DecimalFormat diff = new DecimalFormat("0.00");
       * diff.setMaximumFractionDigits(2); String eligibleValule =
       * diff.format(eligibleVal); if (dailyval_Log.equalsIgnoreCase("YES")) {
       * Loggers.general().info(LOG,"Final counter value--->" + eligibleValule + "" +
       * rs.getString(2));
       *
       * } fdwrapper.setELIAMT(eligibleValule + " " + rs.getString(2));
       *
       * fdwrapper.setEXDATE(rs.getString(3));
       *
       * }
       *
       * } } } catch (Exception e) { if (dailyval_Log.equalsIgnoreCase("YES")) {
       * Loggers.general().info(LOG,"Exception in Counter guarantee Master amount " +
       * e.getMessage()); } } finally { try { if (rs != null) rs.close(); if (ps !=
       * null) ps.close(); if (con != null) con.close(); } catch (SQLException e) {
       * //// Loggers.general().info(LOG,"Connection Failed! Check output ////
       * console"); e.printStackTrace(); } }
       *
       * }
       *
       * public void onGURANTEEIMPGUAAMDclayButton() { String strLog = "Log"; String
       * dailyval_Log = "";
       *
       * @SuppressWarnings("unchecked") AdhocQuery<? extends
       * com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog =
       * getDriverWrapper() .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog +
       * "'"); EXTGENCUSTPROP CodeLog = queryLog.getUnique(); if (CodeLog != null) {
       *
       * dailyval_Log = CodeLog.getPropval(); } else { //
       * Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");
       *
       * }
       *
       * BigDecimal Amount100 = new BigDecimal("100"); BigDecimal Amount1000 = new
       * BigDecimal("1000"); BigDecimal eligibleVal = new BigDecimal("0"); String mamt
       * = getDriverWrapper().getEventFieldAsText("MST", "r", ""); try {
       *
       * // Getting the Table Structure and Size
       * EnigmaArray<ExtEventCounterGuranteeEntityWrapper> list =
       * getExtEventCounterGuranteeData(); for (int i = 0; i <
       * list.getSize().intValue(); i++) {
       * Iterator<ExtEventCounterGuranteeEntityWrapper> iterator1 = list.iterator();
       * // Getting the table Values while (iterator1.hasNext()) {
       * ExtEventCounterGuranteeEntityWrapper fdwrapper =
       * (ExtEventCounterGuranteeEntityWrapper) iterator1 .next(); con =
       * ConnectionMaster.getConnection(); String guaranteeNumber =
       * fdwrapper.getGURNO().trim(); String query =
       * "select trim(mas.amount), mas.ccy , to_char( mas.expiry_dat,'dd/mm/yy' ) from master mas,baseevent bas  where mas.key97=bas.master_key and mas.master_ref='"
       * + guaranteeNumber + "'"; //
       * Loggers.general().info(LOG,"Master amount in query " + query); if
       * (dailyval_Log.equalsIgnoreCase("YES")) {
       * Loggers.general().info(LOG,"Counter guarantee Master amount in query " +
       * query); } ps = con.prepareStatement(query); rs = ps.executeQuery(); if
       * (rs.next()) { String eligible_amt = rs.getString(1); BigDecimal
       * eligible_Amount = new BigDecimal(eligible_amt); String getced =
       * "select c8ced from c8pf where c8ccy='" + rs.getString(2) + "'"; ps =
       * con.prepareStatement(getced); // Loggers.general().info(LOG,"sql" + getced);
       * rs = ps.executeQuery(); int a = 0; while (rs.next()) { a = rs.getInt(1); } if
       * (a == 2) {
       *
       * eligibleVal = eligible_Amount.divide(Amount100); } if (a == 3) { eligibleVal
       * = eligible_Amount.divide(Amount1000); } //
       * Loggers.general().info(LOG,"Total eligible_amt--->" + // eligible_amt); if
       * (dailyval_Log.equalsIgnoreCase("YES")) {
       * Loggers.general().info(LOG,"Total eligible_amt--->" + eligibleVal);
       *
       * }
       *
       * DecimalFormat diff = new DecimalFormat("0.00");
       * diff.setMaximumFractionDigits(2); String eligibleValule =
       * diff.format(eligibleVal); if (dailyval_Log.equalsIgnoreCase("YES")) {
       * Loggers.general().info(LOG,"Final counter value--->" + eligibleValule + "" +
       * rs.getString(2));
       *
       * } fdwrapper.setELIAMT(eligibleValule + " " + rs.getString(2));
       *
       * fdwrapper.setEXDATE(rs.getString(3));
       *
       * }
       *
       * } } } catch (Exception e) { if (dailyval_Log.equalsIgnoreCase("YES")) {
       * Loggers.general().info(LOG,"Exception in Counter guarantee Master amount " +
       * e.getMessage()); } } finally { try { if (rs != null) rs.close(); if (ps !=
       * null) ps.close(); if (con != null) con.close(); } catch (SQLException e) {
       * //// Loggers.general().info(LOG,"Connection Failed! Check output ////
       * console"); e.printStackTrace(); } }
       *
       * }
       *
       * public void onGURANTEEIMPGUAADJclayButton() { String strLog = "Log"; String
       * dailyval_Log = "";
       *
       * @SuppressWarnings("unchecked") AdhocQuery<? extends
       * com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog =
       * getDriverWrapper() .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog +
       * "'"); EXTGENCUSTPROP CodeLog = queryLog.getUnique(); if (CodeLog != null) {
       *
       * dailyval_Log = CodeLog.getPropval(); } else { //
       * Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");
       *
       * }
       *
       * BigDecimal Amount100 = new BigDecimal("100"); BigDecimal Amount1000 = new
       * BigDecimal("1000"); BigDecimal eligibleVal = new BigDecimal("0"); String mamt
       * = getDriverWrapper().getEventFieldAsText("MST", "r", ""); try {
       *
       * // Getting the Table Structure and Size
       * EnigmaArray<ExtEventCounterGuranteeEntityWrapper> list =
       * getExtEventCounterGuranteeData(); for (int i = 0; i <
       * list.getSize().intValue(); i++) {
       * Iterator<ExtEventCounterGuranteeEntityWrapper> iterator1 = list.iterator();
       * // Getting the table Values while (iterator1.hasNext()) {
       * ExtEventCounterGuranteeEntityWrapper fdwrapper =
       * (ExtEventCounterGuranteeEntityWrapper) iterator1 .next(); con =
       * ConnectionMaster.getConnection(); String guaranteeNumber =
       * fdwrapper.getGURNO().trim(); String query =
       * "select trim(mas.amount), mas.ccy , to_char( mas.expiry_dat,'dd/mm/yy' ) from master mas,baseevent bas  where mas.key97=bas.master_key and mas.master_ref='"
       * + guaranteeNumber + "'"; //
       * Loggers.general().info(LOG,"Master amount in query " + query); if
       * (dailyval_Log.equalsIgnoreCase("YES")) {
       * Loggers.general().info(LOG,"Counter guarantee Master amount in query " +
       * query); } ps = con.prepareStatement(query); rs = ps.executeQuery(); if
       * (rs.next()) { String eligible_amt = rs.getString(1); BigDecimal
       * eligible_Amount = new BigDecimal(eligible_amt); String getced =
       * "select c8ced from c8pf where c8ccy='" + rs.getString(2) + "'"; ps =
       * con.prepareStatement(getced); // Loggers.general().info(LOG,"sql" + getced);
       * rs = ps.executeQuery(); int a = 0; while (rs.next()) { a = rs.getInt(1); } if
       * (a == 2) {
       *
       * eligibleVal = eligible_Amount.divide(Amount100); } if (a == 3) { eligibleVal
       * = eligible_Amount.divide(Amount1000); } //
       * Loggers.general().info(LOG,"Total eligible_amt--->" + // eligible_amt); if
       * (dailyval_Log.equalsIgnoreCase("YES")) {
       * Loggers.general().info(LOG,"Total eligible_amt--->" + eligibleVal);
       *
       * }
       *
       * DecimalFormat diff = new DecimalFormat("0.00");
       * diff.setMaximumFractionDigits(2); String eligibleValule =
       * diff.format(eligibleVal); if (dailyval_Log.equalsIgnoreCase("YES")) {
       * Loggers.general().info(LOG,"Final counter value--->" + eligibleValule + "" +
       * rs.getString(2));
       *
       * } fdwrapper.setELIAMT(eligibleValule + " " + rs.getString(2));
       *
       * fdwrapper.setEXDATE(rs.getString(3));
       *
       * }
       *
       * } } } catch (Exception e) { if (dailyval_Log.equalsIgnoreCase("YES")) {
       * Loggers.general().info(LOG,"Exception in Counter guarantee Master amount " +
       * e.getMessage()); } } finally { try { if (rs != null) rs.close(); if (ps !=
       * null) ps.close(); if (con != null) con.close(); } catch (SQLException e) {
       * //// Loggers.general().info(LOG,"Connection Failed! Check output ////
       * console"); e.printStackTrace(); } }
       *
       * }
       */

      // public void onGURANTEEIMPGUAOUTCLAcalyButton() {
      //
      // String strLog = "Log";
      // String dailyval_Log = "";
      // @SuppressWarnings("unchecked")
      // AdhocQuery<? extends
      // com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog =
      // getDriverWrapper()
      // .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
      // EXTGENCUSTPROP CodeLog = queryLog.getUnique();
      // if (CodeLog != null) {
      //
      // dailyval_Log = CodeLog.getPropval();
      // } else {
      // // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");
      //
      // }
      // String pono = "";
      // Connection con = null;
      // PreparedStatement ps = null;
      // ResultSet res = null;
      // String medAmt = "";
      // String Current_finance_eligible_Amt = "";
      // Double eligible_amt = 0.0;
      // String currency = "";
      // String firstcurrfamt = "";
      // long eligible_long = 0;
      // long mAmt_long = 0;
      // String mamt = getDriverWrapper().getEventFieldAsText("MST", "r", "");
      // try {
      //
      // // Getting the Table Structure and Size
      // EnigmaArray<ExtEventCounterGuranteeEntityWrapper> list =
      // getExtEventCounterGuranteeData();
      // for (int i = 0; i < list.getSize().intValue(); i++) {
      // Iterator<ExtEventCounterGuranteeEntityWrapper> iterator1 =
      // list.iterator();
      // // Getting the table Values
      // while (iterator1.hasNext()) {
      // ExtEventCounterGuranteeEntityWrapper fdwrapper =
      // (ExtEventCounterGuranteeEntityWrapper) iterator1
      // .next();
      // con = ConnectionMaster.getConnection();
      // String guaranteeNumber = fdwrapper.getGURNO().trim();
      // String query = "select trim(mas.amount), mas.ccy , to_char(
      // mas.expiry_dat,'dd/mm/yy' ) from master mas,baseevent bas where
      // mas.key97=bas.master_key and mas.master_ref='"
      // + guaranteeNumber + "'";
      // // Loggers.general().info(LOG,"Master amount in query " + query);
      // if (dailyval_Log.equalsIgnoreCase("YES")) {
      // Loggers.general().info(LOG,"Master amount in query " + query);
      // }
      // ps = con.prepareStatement(query);
      // res = ps.executeQuery();
      // if (res.next()) {
      // eligible_amt = res.getDouble(1);
      // // Loggers.general().info(LOG,"Total eligible_amt " +
      // // eligible_amt);
      // if (dailyval_Log.equalsIgnoreCase("YES")) {
      // Loggers.general().info(LOG,"Total eligible_amt " + eligible_amt);
      // }
      // String getced = "select c8ced from c8pf where c8ccy='" + res.getString(2)
      // + "'";
      // Connection con1 = ConnectionMaster.getConnection();
      // PreparedStatement drms = con1.prepareStatement(getced);
      // // Loggers.general().info(LOG,"sql" + getced);
      // ResultSet drms1 = drms.executeQuery();
      // int a = 0;
      // while (drms1.next()) {
      // a = drms1.getInt(1);
      // }
      // if (a == 2) {
      // eligible_amt = eligible_amt / 100;
      // }
      // if (a == 3) {
      // eligible_amt = eligible_amt / 1000;
      // }
      // // Loggers.general().info(LOG,"Total eligible_amt--->" +
      // // eligible_amt);
      // if (dailyval_Log.equalsIgnoreCase("YES")) {
      // Loggers.general().info(LOG,"Total eligible_amt--->" + eligible_amt);
      // }
      // String eligible_String = String.valueOf(eligible_amt);
      // float eligible_flot = Float.valueOf(eligible_String);
      // eligible_long = (long) eligible_flot;
      // fdwrapper.setELIAMT(String.valueOf(eligible_long * 100) + " " +
      // res.getString(2));
      //
      // firstcurrfamt = String.valueOf(eligible_long * 100) + "" +
      // res.getString(2);
      // fdwrapper.setEXDATE(res.getString(3));
      // currency = res.getString(2);
      // }
      // /*
      // * // Setting Current Finance Eligible Amount String
      // * CurrentAmount =
      // * "SELECT NVL(SUM(ext.GURAMOT)/100, 0) from EXTEVENTCGT ext,BASEEVENT bev
      // where ext.FK_EVENT=bev.EXTFIELD AND bev.STATUS in ('i','c') AND
      // trim(bev.EXTFIELD) not in (select b.EXTFIELD from master m, baseevent b
      // where m.key97 = b.master_key and trim(m.master_ref)='"
      // * + mamt + "')"; //Loggers.general().info(LOG,
      // * "Current Eligible_Amount is " + CurrentAmount);
      // * PreparedStatement ps2 =
      // * con.prepareStatement(CurrentAmount); ResultSet res2 =
      // * ps2.executeQuery(); Double mAmt = 0.0; if (res2.next()) {
      // * medAmt = res2.getString(1).trim(); //Loggers.general().info(LOG,
      // * "medAmt " + medAmt); mAmt = Double.parseDouble(medAmt);
      // * String mAmt_String = String.valueOf(mAmt); float
      // * mAmt_flot = Float.valueOf(mAmt_String); mAmt_long =
      // * (long) (mAmt_flot*100); //Loggers.general().info(LOG,
      // * "mAmt_long --->" + mAmt_long);
      // * Current_finance_eligible_Amt =
      // * String.valueOf(eligible_long - mAmt_long); } else {
      // * //Loggers.general().info(LOG,"value is " + firstcurrfamt);
      // * fdwrapper.setCURAMT(firstcurrfamt);
      // *
      // * } //Loggers.general().info(LOG,"Current_finance_eligible_Amt--->"
      // * + Current_finance_eligible_Amt); //
      // * Current_finance_eligible_Amt //
      // * +=Current_finance_eligible_Amt;
      // * fdwrapper.setCURAMT(Current_finance_eligible_Amt + "" +
      // * currency);
      // */
      //
      // }
      // }
      // } catch (Exception e) {
      // // Loggers.general().info(LOG,"Exception is " + e.getMessage());
      // if (dailyval_Log.equalsIgnoreCase("YES")) {
      // Loggers.general().info(LOG,"Exception is " + e.getMessage());
      // }
      // }
      //
      // }

      public void onGETPANOUTRMTcalyButton() {
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
            String customer = getDriverWrapper().getEventFieldAsText("DBT", "p", "cu");
            try {
                  con = ConnectionMaster.getConnection();
                  String query = "select panno from extcust where cust= '" + customer + "'";
                  // Loggers.general().info(LOG,"query " + query);
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG, "query " + query);
                  }
                  ps = con.prepareStatement(query);
                  rs = ps.executeQuery();
                  while (rs.next()) {
                        String cust = rs.getString(1);
                        // Loggers.general().info(LOG,"Customer" + cust);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG, "Customer" + cust);
                        }
                        setPANDETAI(cust);
                  }
            } catch (Exception e) {
                  // Loggers.general().info(LOG,"exception caught");
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG, "exception caught");
                  }
            }

      }

      // getting pan number
      public void onGETPANOUTRMTCUSclayButton() {
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
            String customer = getDriverWrapper().getEventFieldAsText("DBT", "p", "cu");
            try {
                  con = ConnectionMaster.getConnection();
                  String query = "select panno from extcust where cust= '" + customer + "'";
                  // Loggers.general().info(LOG,"query " + query);
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG, "query " + query);
                  }
                  ps = con.prepareStatement(query);
                  rs = ps.executeQuery();
                  while (rs.next()) {
                        String cust = rs.getString(1);
                        // Loggers.general().info(LOG,"Customer" + cust);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG, "Customer" + cust);
                        }
                        setPANDETAI(cust);
                  }
            } catch (Exception e) {
                  // Loggers.general().info(LOG,"exception caught");
            }

      }

      @Override
      public void oncalcmarginOGTISSclayButton() {
            MarginCAL();
      }

      @Override
      public void oncalcmarginOGTINVOCclayButton() {
            MarginCAL();
      }

      @Override
      public void oncalcmarginIMPGUAOUTCLAcalyButton() {
            MarginCAL();
      }

      public boolean MarginCAL() {
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
                        String curr = getDriverWrapper().getEventFieldAsText("AMT", "v", "c").trim();
                        String customerNo = getDriverWrapper().getEventFieldAsText("APP", "p", "no");

                        // Loggers.general().info(LOG,"Currency " + curr);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG, "Currency " + curr);
                        }

                        con = ConnectionMaster.getConnection();
                        if (permar.equalsIgnoreCase("") || permar == null) {
                              String queryfac = "select margin from customermargin where facility='" + facid + "' and CIF ='"
                                          + customerNo + "'";
                              // Loggers.general().info(LOG,"queryfac " + queryfac);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG, "queryfac " + queryfac);
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
                                          Loggers.general().info(LOG, "Margin " + magn);
                                          Loggers.general().info(LOG, "Adding amount---- " + (magne + permaradub));
                                    }
                                    if (curr.equalsIgnoreCase("INR")) {
                                          notionalRate = 1;
                                    } else {
                                          String ttRateQuery = "select trim(SPOTRATE) from spotrate where currency='" + curr + "'";
                                          // Loggers.general().info(LOG,"ttRateQuery - " +
                                          // ttRateQuery);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG, "ttRateQuery - " + ttRateQuery);
                                          }
                                          ttRatePS = con.prepareStatement(ttRateQuery);
                                          ttRateRS = ttRatePS.executeQuery();
                                          while (ttRateRS.next()) {
                                                String ttRate_String = ttRateRS.getString(1).trim();
                                                notionalRate = Double.parseDouble(ttRate_String);
                                          }
                                    }
                                    // Loggers.general().info(LOG,"notionalRate - " + notionalRate);
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG, "notionalRate - " + notionalRate);
                                    }
                                    long k = (long) (((amount01 * (magne + permaradub))) * notionalRate);
                                    String ks = String.valueOf(k);
                                    // Loggers.general().info(LOG,"Margin amount in final for set if
                                    // stmt " + ks + "" + "INR");
                                    setPMARGIN(magn);
                                    // setNOTRATE(String.valueOf(notionalRate));
                                    setMARAMT(ks + " " + "INR");
                                    //////// setPRMARAMT(ks + " " + "INR");
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
                                    Loggers.general().info(LOG, "magndou value in long else--->" + magndou);
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
                                    ttRatePS = con.prepareStatement(ttRateQuery);
                                    ttRateRS = ttRatePS.executeQuery();
                                    while (ttRateRS.next()) {
                                          String ttRate_String = ttRateRS.getString(1).trim();
                                          notionalRate = Double.parseDouble(ttRate_String);
                                    }
                              }
                              // Loggers.general().info(LOG,"Adding amount in else---- " +
                              // (magndou + permaradub));
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG, "Adding amount in else---- " + (magndou + permaradub));
                              }
                              long k = (long) (((amount01 * (magndou + permaradub))) * notionalRate);
                              // Loggers.general().info(LOG,"Expected margin amount in else---> -
                              // " + k);
                              String ks = String.valueOf(k);
                              // Loggers.general().info(LOG,"Margin amount in final for set else
                              // stmt " + ks + "" + "INR");
                              setPMARGIN(permar);
                              // setNOTRATE(String.valueOf(notionalRate));
                              setMARAMT(ks + " " + "INR");
                              //////// setPRMARAMT(ks + " " + "INR");
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

                        if (ttRateRS != null)
                              ttRateRS.close();
                        if (ttRatePS != null)
                              ttRatePS.close();
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

            return value;

      }

      public void onRTGSINWRMTclayButton() {

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
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG, "query for IFSC button" + query);
                        }
                        int count = 0;
                        ps1 = con.prepareStatement(query);
                        rs1 = ps1.executeQuery();
                        while (rs1.next()) {
                              // Loggers.general().info(LOG,"Entered while");
                              count = rs1.getInt(1);
                              // Loggers.general().info(LOG,"value of count in while " + count);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG, "value of count in while " + count);
                              }
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
                                    con = ConnectionMaster.getConnection();
                                    String spq = "Select trim(BANK),trim(BRANCH),trim(CITY) from EXTIFSCC where IFSC ='" + Ifsc
                                                + "'";
                                    // Loggers.general().info(LOG,spq);
                                    ps1 = con.prepareStatement(spq);
                                    dmsr1 = ps1.executeQuery();
                                    while (dmsr1.next()) {
                                          setBENBAK((dmsr1.getString(1).trim()));
                                          setBENBRN(dmsr1.getString(2).trim());
                                          setBENCITY((dmsr1.getString(3).trim()));

                                    }

                              } catch (Exception e) {

                                    // Loggers.general().info(LOG,"event catch");
                              }

                        }
                  } catch (Exception e) {

                        // Loggers.general().info(LOG,"event catch");
                  } finally {
                        try {

                              if (dmsr1 != null)
                                    dmsr1.close();
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

      public void onRTGSINWRMTCUSclayButton() {

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
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG, "query for IFSC button" + query);
                        }
                        int count = 0;
                        ps1 = con.prepareStatement(query);
                        rs1 = ps1.executeQuery();
                        while (rs1.next()) {
                              // Loggers.general().info(LOG,"Entered while");
                              count = rs1.getInt(1);
                              // Loggers.general().info(LOG,"value of count in while " + count);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG, "value of count in while " + count);
                              }
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
                                    con = ConnectionMaster.getConnection();
                                    String spq = "Select trim(BANK),trim(BRANCH),trim(CITY) from EXTIFSCC where IFSC ='" + Ifsc
                                                + "'";
                                    // Loggers.general().info(LOG,spq);
                                    ps1 = con.prepareStatement(spq);
                                    dmsr1 = ps1.executeQuery();
                                    while (dmsr1.next()) {
                                          setBENBAK((dmsr1.getString(1).trim()));
                                          setBENBRN(dmsr1.getString(2).trim());
                                          setBENCITY((dmsr1.getString(3).trim()));

                                    }

                              } catch (Exception e) {

                                    // Loggers.general().info(LOG,"event catch");
                              }

                        }
                  } catch (Exception e) {

                        // Loggers.general().info(LOG,"event catch");
                  } finally {
                        try {

                              if (dmsr1 != null)
                                    dmsr1.close();
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

      public void onRTGSOUTRMTCUSclayButton() {

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
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG, "query for IFSC button" + query);
                        }
                        int count = 0;
                        ps1 = con.prepareStatement(query);
                        rs1 = ps1.executeQuery();
                        while (rs1.next()) {
                              // Loggers.general().info(LOG,"Entered while");
                              count = rs1.getInt(1);
                              // Loggers.general().info(LOG,"value of count in while " + count);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG, "value of count in while " + count);
                              }
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
                                    con = ConnectionMaster.getConnection();
                                    String spq = "Select trim(BANK),trim(BRANCH),trim(CITY) from EXTIFSCC where IFSC ='" + Ifsc
                                                + "'";
                                    // Loggers.general().info(LOG,spq);
                                    ps1 = con.prepareStatement(spq);
                                    dmsr1 = ps1.executeQuery();
                                    while (dmsr1.next()) {
                                          setBENBAK((dmsr1.getString(1).trim()));
                                          setBENBRN(dmsr1.getString(2).trim());
                                          setBENCITY((dmsr1.getString(3).trim()));

                                    }

                              } catch (Exception e) {

                                    // Loggers.general().info(LOG,"event catch");
                              }

                        }
                  } catch (Exception e) {

                        // Loggers.general().info(LOG,"event catch");
                  } finally {
                        try {

                              if (dmsr1 != null)
                                    dmsr1.close();
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

      public boolean deleteLoanDetails() {
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
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG, "New PreShipment method is Called");
                  }
                  EnigmaArray<ExtEventLoanDetailsEntityWrapper> loanDetails = getExtEventLoanDetailsData();
                  for (ExtEventLoanDetailsEntityWrapper detailsEntityWrapper : loanDetails) {
                        removeExtEventLoanDetails(detailsEntityWrapper);
                  }
                  return true;
            } catch (Exception e) {
                  e.printStackTrace();
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG, "Exception in PreShip Fetch Test");
                  }
                  return false;
            }
      }

      public void onPreshipFetchINWRMTCUSclayButton() {

            String strLog = "Log";
            String dailyval_Log = "";
            // String Subproduct="";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
            EXTGENCUSTPROP CodeLog = queryLog.getUnique();
            if (CodeLog != null) {

                  dailyval_Log = CodeLog.getPropval();
            } else {
                  // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }
            Connection con = null;
            PreparedStatement prepareStmt = null;

            ResultSet result_loan_emplty = null;
            String masref = "";
            String eventRef = "";
            // int count = 0;
            // int k = 0;
            String[] currencies = { "USD", "JPY", "INR", "GBP", "EUR" };
            int temp_count = 0;
            String startCurr = "";
            double sumAmount = 0;
            ArrayList<String> sumAmountInAllCurrencies;
            int sequence = 0;
            int count = 0;
            int cc = 0;
            String loanType = "";// This will fetch the Loan Type from the grid
            ArrayList<String> loans = new ArrayList<String>();
            try {

                  deleteLoanDetails();
                  sumAmountInAllCurrencies = new ArrayList<String>();
                  masref = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                  eventRef = getDriverWrapper().getEventFieldAsText("MEVR", "r", "");
                  /*
                   * Subproduct = getDriverWrapper().getEventFieldAsText("PTP", "s", "");
                   * Loggers.general().info(LOG,"Preshipment subproduct"+Subproduct);
                   */

                  EnigmaArray<ExtEventLoanDetailsEntityWrapper> liste = getExtEventLoanDetailsData();
                  temp_count = liste.getSize().intValue();
                  // Loggers.general().info(LOG,"temp value " + temp_count);
                  if (liste.getSize() == 0) {
                        con = ConnectionMaster.getConnection();
                        String loan_query = "select trim(loan_ref) AS LOAN,repayamt AS AMOUNT ,trim(CURR) AS CURR,to_char(VALUE_DATE,'dd/mm/yyyy') AS VDATE,TRIM(PCFC) AS TYPE from ett_preshipment_apiserver where masref='"
                                    + masref + "' and eventref='" + eventRef + "' ORDER BY CURR";
                        // Loggers.general().info(LOG,"loan query is " + loan_query);
                        PreparedStatement ps = con.prepareStatement(loan_query);
                        ResultSet ress = ps.executeQuery();
                        while (ress.next()) {

                              loans.add(ress.getString("LOAN"));
                              loans.add(ress.getString("AMOUNT").replaceAll("[^\\d]", "").trim() + " "
                                          + ress.getString("CURR").trim());
                              loans.add(ress.getString("VDATE"));
                              loanType = ress.getString("TYPE");
                              // Loggers.general().info(LOG,"eneteerd while");

                        }
                        // Loggers.general().info(LOG,"Loan Type Value is " + loanType);
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG, "Loan Type Value is " + loanType);
                        }
                        for (int i = 0; i < loans.size() / 3; i++) {
                              SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                              Key_ExtEvent<? extends ExtEvent> fkey = new Key_ExtEvent<ExtEvent>();
                              ExtEventLoanDetails loanvalues = new ExtEventLoanDetails();
                              loanvalues.setColumn("DEALREF", loans.get(cc));
                              loanvalues.setColumn("REAMOUNT", loans.get(cc + 1));
                              Date date1 = format.parse(loans.get(cc + 2));
                              loanvalues.setVALDATE(date1);

                              loanvalues.setNewKey();
                              loanvalues.setFk(fkey);
                              loanvalues.setSequence(sequence);
                              ExtEventLoanDetailsEntityWrapper loanDetailsWrapper = new ExtEventLoanDetailsEntityWrapper(
                                          loanvalues, getDriverWrapper());

                              addNewExtEventLoanDetails(loanDetailsWrapper);
                              sequence++;
                              cc = cc + 3;
                              /*
                               * setPRESHIP(Subproduct); Loggers.general().info(LOG,"Preshipment subproduct"+
                               * getPRESHIP());
                               */
                              // Loggers.general().info(LOG,"va " + cc);
                        }
                        // setLOANTYPE(loanType);
                        try {
                              // Loggers.general().info(LOG,"PreShipment new Changes");
                              sumAmountInAllCurrencies = new ArrayList<String>();

                              for (int a = 0; a < currencies.length; a++) {
                                    try {
                                          String query = "SELECT NVL(SUM(REPAYAMT),'0') FROM ETT_PRESHIPMENT_APISERVER WHERE CURR='"
                                                      + currencies[a] + "' AND masref='" + masref + "' and eventref='" + eventRef
                                                      + "' ORDER BY CURR DESC";
                                          // Loggers.general().info(LOG,"PreShipment Sum query " +
                                          // query);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG, "PreShipment Sum query " + query);
                                          }
                                          PreparedStatement prep = con.prepareStatement(query);
                                          ResultSet result = prep.executeQuery();
                                          if (result.next()) {
                                                double amount = 0;
                                                double dob_amt = result.getDouble(1);
                                                // Loggers.general().info(LOG,"Entered if and the Value
                                                // of Amount in double" + dob_amt);
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG,
                                                                  "Entered if and the Value of Amount in double" + dob_amt);
                                                }
                                                String amt = result.getString(1);
                                                BigDecimal amt_dec = new BigDecimal(amt);
                                                // Loggers.general().info(LOG,"Entered if and the Value
                                                // of Amount in String " + amt + " Bigdecimal" +
                                                // amt_dec);
                                                if (currencies[a].equalsIgnoreCase("EUR")) {
                                                      amount = result.getDouble(1) / 100;
                                                      // Loggers.general().info(LOG,"PRESHIPMENT Value of
                                                      // Amount in if loop" + amount);

                                                } else {
                                                      amount = result.getDouble(1) / 100;
                                                      // Loggers.general().info(LOG,"PRESHIPMENT Value of
                                                      // Amount in else loop" + amount);
                                                }
                                                // Loggers.general().info(LOG,"Entered if and the Value
                                                // of Amount in double" + amount);
                                                // -----------DECIMAL
                                                // ISSUE-----------------------------------------------------------

                                                if (currencies[a].equalsIgnoreCase("USD")) {
                                                      // Loggers.general().info(LOG,"PRESHIPMENT Value of
                                                      // Amount in before" + amount);
                                                      if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                            Loggers.general().info(LOG, "PRESHIPMENT Value of Amount in before" + amount);
                                                      }
                                                      amount = result.getDouble(1) / 100;
                                                      // Loggers.general().info(LOG,"PRESHIPMENT Value of
                                                      // Amount in afte" + amount);
                                                } else {

                                                      // amount = result.getDouble(1) * 10;
                                                }

                                                if (currencies[a].equalsIgnoreCase("INR")) {
                                                      // Loggers.general().info(LOG,"PRESHIPMENT Value of
                                                      // Amount in before" + amount);
                                                      amount = result.getDouble(1) / 100;
                                                      // Loggers.general().info(LOG,"PRESHIPMENT Value of
                                                      // Amount in afte" + amount);
                                                } else {
                                                      // amount = result.getDouble(1) * 10;
                                                }

                                                if (currencies[a].equalsIgnoreCase("JPY")) {
                                                      // Loggers.general().info(LOG,"PRESHIPMENT Value of
                                                      // Amount in before" + amount);
                                                      amount = result.getDouble(1) / 1;
                                                      // Loggers.general().info(LOG,"PRESHIPMENT Value of
                                                      // Amount in afte" + amount);
                                                } else {
                                                      // amount = result.getDouble(1) * 10;
                                                }

                                                if (currencies[a].equalsIgnoreCase("GBP")) {
                                                      // Loggers.general().info(LOG,"PRESHIPMENT Value of
                                                      // Amount in before" + amount);
                                                      amount = result.getDouble(1) / 100;
                                                      // Loggers.general().info(LOG,"PRESHIPMENT Value of
                                                      // Amount in afte" + amount);
                                                } else {
                                                      // amount = result.getDouble(1) * 10;
                                                }

                                                // -------------------------------------------------------------------------------------
                                                // 18-aug-19 decimal to bigdecimal for exponential in amount
                                                BigDecimal b_amt = new BigDecimal(amount);
                                                Currency cur = Currency.getInstance(currencies[a]);
                                                int precision = cur.getDefaultFractionDigits();
                                                RoundingMode DEFAULT_ROUNDING = RoundingMode.HALF_EVEN;
                                                BigDecimal roundoffvalue = null;
                                                roundoffvalue = b_amt.setScale(precision, DEFAULT_ROUNDING);
                                                sumAmountInAllCurrencies.add(roundoffvalue + " " + currencies[a]);
                                                // sumAmountInAllCurrencies.add(amount + " " + currencies[a]);
                                          } else {
                                                // Loggers.general().info(LOG,"ENtered else since the
                                                // Resultset is empty");
                                          }
                                          result.close();
                                          prep.close();

                                    } catch (SQLException sqlexception) {
                                          sqlexception.printStackTrace();
                                    }

                              }
                              // Loggers.general().info(LOG," sumAmountInAllCurrencies Size " +
                              // sumAmountInAllCurrencies.size());
                              for (int b = 0; b < sumAmountInAllCurrencies.size(); b++) {
                                    // Loggers.general().info(LOG,"Inside For Loop " + b);
                                    if (b == 0) {
                                          // Loggers.general().info(LOG,"Value of b " + b);
                                          setTOTUSD(sumAmountInAllCurrencies.get(b));
                                    }
                                    if (b == 1) {
                                          setTOTJPY(sumAmountInAllCurrencies.get(b));
                                          // Loggers.general().info(LOG,"Value of b " + b);
                                    }
                                    if (b == 2) {
                                          // Loggers.general().info(LOG,"Value of b " + b);
                                          setTOTINR(sumAmountInAllCurrencies.get(b));
                                    }
                                    if (b == 3) {
                                          // Loggers.general().info(LOG,"Value of b " + b);
                                          setTOTGBP(sumAmountInAllCurrencies.get(b));
                                    }
                                    if (b == 4) {
                                          // Loggers.general().info(LOG,"Value of b " + b);
                                          setTOTEUR(sumAmountInAllCurrencies.get(b));
                                    }
                              }

                        } catch (Exception e) {
                              // Loggers.general().info(LOG,"Exception in try inside try" +
                              // e.getMessage());
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG, "Exception in try inside try" + e.getMessage());
                              }
                        }

                  }
                  getExtEventLoanDetailsUpdate().setEnabled(false);
                  getExtEventLoanDetailsNew().setEnabled(false);
                  getExtEventLoanDetailsDelete().setEnabled(false);
                  getExtEventLoanDetailsUp().setEnabled(false);
                  getExtEventLoanDetailsDown().setEnabled(false);
            } catch (Exception e) {
                  // Loggers.general().info(LOG,"Exception int preshipment fetch " +
                  // e.getMessage());
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        Loggers.general().info(LOG, "Exception int preshipment fetch" + e.getMessage());
                  }
            }

      }

      public void ondisplayvalOGTINVOCclayButton() {

            // Loggers.general().info(LOG,"Imp gur button for POD");
            String benname = "";
            String subproCode = getDriverWrapper().getEventFieldAsText("PTP", "s", "");
            String masRefNo = getDriverWrapper().getEventFieldAsText("MST", "r", "");
            String eventRefNo = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
            setRTGSNEFT("");
            con = ConnectionMaster.getConnection();
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

                  String query = "SELECT pos.BO_ACC_NO, POS.ORIGAMOUNT/100,POS.ORIGAMTCCY "
                              + "FROM  MASTER MAS,BASEEVENT BEV,RELITEM REL ,POSTING POS "
                              + "WHERE MAS.KEY97=BEV.MASTER_KEY   AND BEV.KEY97=REL.EVENT_KEY " + " AND REL.KEY97=POS.KEY97"
                              + " AND MAS.MASTER_REF='" + masRefNo + "' " + " AND (BEV.REFNO_PFIX || LPAD(BEV.REFNO_SERL,3,0))='"
                              + eventRefNo + "' " + " and (BEV.REFNO_PFIX || LPAD(BEV.REFNO_SERL,3,0))=pos.eventref "
                              + " AND POS.DR_CR_FLG='D' " + " AND POS.ACC_TYPE IN ('CA','CCGEN','CCUTR','ODA','ODGEN','CAA') ";
                  ps = con.prepareStatement(query);
                  rs = ps.executeQuery();
                  if (rs.next()) {
                        String dracct = rs.getString(1).trim();
                        String amount = rs.getString(2).trim();
                        String ccy = rs.getString(3).trim();
                        System.out.println("RTGS NEFT QUERY " + query + " " + amount);
                        setDRINTACC(dracct);
                        setRTGSNEFT(amount + " " + ccy);
                  }
                  String rtgsType = getPROREMT();
                  if (rtgsType.trim().equalsIgnoreCase("RTG")) {

                        setCRPOOLAC("504505120004000");

                  }

                  if (rtgsType.trim().equalsIgnoreCase("NEF")) {

                        // String debtacc="1980050000";
                        setCRPOOLAC("473802480015000");

                  }
//                            String collAmt = getDriverWrapper().getEventFieldAsText("FPP:XAM", "v", "m");
//                      String collAmtCurr = getDriverWrapper().getEventFieldAsText("FPP:XAM", "v", "c");
//                      if ((collAmt != null && !collAmt.isEmpty())&&(collAmtCurr != null && !collAmtCurr.isEmpty())) {
//                      setRTGSNEFT(collAmt + " " + collAmtCurr);
//                      }
                  String proceed = getRTGNFT();
                  if (proceed.equalsIgnoreCase("B2B")) {
                        benname = getDriverWrapper().getEventFieldAsText("SEL", "p", "f");
                        setBENNAME_Name(benname);
                  }
                  if (proceed.equalsIgnoreCase("B2C")) {
                        benname = getDriverWrapper().getEventFieldAsText("SEL", "p", "f");
                        setBENNAME_Name(benname);
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
                        // Loggers.general().info(LOG,"Connection Failed! Check output
                        // console");
                        e.printStackTrace();
                  }
            }
      }

      public void onFETCHACCOUNTINWRMTCUSclayButton() {
            account();
      }

      public void onFETCHACCOUNTINWRMTclayButton() {
            account();
      }

      public void account() {
            try {
                  EnigmaArray<ExtEventAccountRealisationEntityWrapper> liste = getExtEventAccountRealisationData();
                  con = ConnectionMaster.getConnection();
                  String masReference = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                  String eventCode = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
                  String key29 = "";
                  int count = 1;
//                      String query="Select bev.extfield from master mas,baseevent bev "+
//                             "where mas.key97=bev.master_key "+
//                             "and mas.MASTER_REF='"+masReference.trim()+"'"+
//                            " and bev.REFNO_PFIX||LPAD(bev.refno_serl,3,0)='"+eventCode+"'"+
//                            " and bev.status not in ('a','c')";
//                System.out.println("query for account code button:"+query+" "+masReference+" "+eventCode);
//                dmsp = con.prepareStatement(query);
//                // //Loggers.general().info(LOG,"DMS Query " + dms);
//                dmsr = dmsp.executeQuery();   
//                while (dmsr.next()) {
//                      key29 = dmsr.getString(1);
//                      // dmsurl = dmsstr + "&dirAmount=" + payamt;
//                      
//                      }
                  String accountQuery = "Select xkey,seqn,fk_event,raccount,raccttyp,ramount,rpercent,response,drcr from exteventaccount "
                              + " where fk_event=(" + "Select bev.extfield from master mas,baseevent bev "
                              + "where mas.key97=bev.master_key " + "and mas.MASTER_REF='" + masReference + "'"
                              + " and bev.REFNO_PFIX||LPAD(bev.refno_serl,3,0)='" + eventCode + "'"
                              + " and bev.status not in ('a','c')" + ")";
                  account = con.prepareStatement(accountQuery);
                  rst1 = account.executeQuery();
                  System.out.println("account query fetch" + accountQuery);
                  while (rst1.next()) {
                        Key_ExtEvent<? extends ExtEvent> fkey = new Key_ExtEvent<ExtEvent>();
                        ExtEventAccountRealisation account = new ExtEventAccountRealisation();

                        account.setRACCOUNT(rst1.getString(4));
                        account.setRACCTTYP(rst1.getString(5));
                        account.setRAMOUNT(rst1.getString(6));
                        account.setRPERCENT(rst1.getString(7));
                        System.out
                                    .println("inside while loop of account" + rst1.getString(3) + " ramount " + rst1.getString(5));
                        account.setRESPONSE(rst1.getString(8));
                        account.setDRCR(rst1.getString(9));
                        account.setNewKey();
                        account.setFk(fkey);
                        account.setSequence(count);
                        System.out.println("last of while foreign " + fkey);
                        ExtEventAccountRealisationEntityWrapper accountwrapper = new ExtEventAccountRealisationEntityWrapper(
                                    account, getDriverWrapper());
                        addNewExtEventAccountRealisation(accountwrapper);
                        count++;
                        if (liste.getSize().intValue() > 0) {
                              getBtnFETCHACCOUNTEXPCOLSETTclay().setEnabled(false);
                              getBtnFETCHACCOUNTEXPBILLclay().setEnabled(false);
                        }

                        System.out.println("last of while  " + count);
                  }
            } catch (Exception e) {
                  e.printStackTrace();
                  System.out.println("Exception update" + e.getMessage());
            } finally {
                  ConnectionMaster.surrenderDB(con, account, rst1);
                  ConnectionMaster.surrenderDB(con, dmsp, dmsr);
            }
      }

//Button to get nostro details added by Vishal G      
      public void onNOSTROINWRMTclayButton() {
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
                  String forwardUid = getFDTRSUID().trim();
                  String nostro_Reference_no1 = "";
                  String nostro_Reference_no2 = "";
                  String nostro_value_date = "";
                  con = ConnectionMaster.getConnection();
                  String Credit_Amount = "";
                  String Credit_currency = "";
                  String CreditAccount = "";
                  String available_Amount = "";

                  String query = "SELECT AMOUNT AS Credit_Amount, CCY AS Credit_currency, TO_CHAR(VALUE_DATE,'DD/MM/YY') AS nostro_value_date,  NOSTRO AS CreditAccount,INST_REF_NUM,OWNER_REF_NUM FROM UID_CREDIT_DAILY_TBL WHERE UNIQUE_ID='"
                              + forwardUid + "'";
                  if (dailyval_Log.equalsIgnoreCase("YES")) {
                        System.out.println("Nostro ETTV_NOSTRO_MT103_MT202_TBL value query " + query);
                  }

                  ps = con.prepareStatement(query);
                  rs = ps.executeQuery();
                  if (rs.next()) {
                        // setNOSTOUT(Credit_AmountMT103202 + " " + Credit_curMT103202);
                        nostro_value_date = rs.getString(3);

                        Credit_Amount = rs.getString(1);
                        Credit_currency = rs.getString(2);
                        nostro_Reference_no1 = rs.getString(6);
                        nostro_Reference_no2 = rs.getString(5);
                        CreditAccount = rs.getString(4);
                        System.out.println("Nostro values  details===>" + Credit_Amount + " " + nostro_Reference_no2 + " "
                                    + CreditAccount);
                        setNOSTMT(nostro_Reference_no1);
                        setNOSTRM(nostro_Reference_no2);
                        setNOSTDAT(nostro_value_date);
                        setPOOLAMT(Credit_Amount + " " + Credit_currency);
                        setNOSTACC(CreditAccount);
                  }
                  String amountquery = "SELECT AVBL_AMOUNT||' '||CCY as availableAmount from UID_CREDIT_DAILY_TRAN_TBL where UNIQUE_ID='"
                              + forwardUid + "' ORDER BY ROW_SEQ DESC fetch first 1 row only";
                  ps1 = con.prepareStatement(amountquery);
                  rs1 = ps1.executeQuery();
                  if (rs1.next()) {
                        available_Amount = rs1.getString(1);
                        System.out.println("aVAILABLE AMOUNT " + available_Amount);
                        setNOSTAMT(available_Amount);
                  }
            } catch (Exception e) {
                  e.printStackTrace();
            }

            finally {
                  try {
                        if (rs != null)
                              rs.close();
                        if (ps != null)
                              ps.close();
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

      }

      // this is for ebg cancel event
      public void onEBGACCEPIMPGUACANclay_2_2AutoSubmit() {

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

      public void onEBGREJECIMPGUACANclay_2_2AutoSubmit() {

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