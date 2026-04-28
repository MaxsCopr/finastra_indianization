package com.misys.tiplus2.customisation.extension;

import java.math.BigDecimal;
//com.misys.tiplus2.customisation.extension.CustomValidation
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang.StringUtils;
//import org.apache.log4j.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bs.theme.transport.client.ThemeTransportClient;
import com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP;
import com.misys.tiplus2.customisation.entity.ExtEventBillReference;
import com.misys.tiplus2.customisation.entity.ExtEventCounterGurantee;
import com.misys.tiplus2.customisation.entity.ExtEventLienMarking;
import com.misys.tiplus2.customisation.pane.EventPane;
import com.misys.tiplus2.enigma.customisation.AdhocQuery;
import com.misys.tiplus2.enigma.customisation.control.ExtendedButtonControlWrapper;
import com.misys.tiplus2.enigma.customisation.control.ExtendedCheckBoxControl;
import com.misys.tiplus2.enigma.customisation.control.ExtendedCheckBoxControlWrapper;
import com.misys.tiplus2.enigma.customisation.control.ExtendedHyperlinkControlWrapper;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails.ErrorType;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails.WarningType;
import com.misys.tiplus2.foundations.lang.logging.Loggers;

public class CustomValidation extends ConnectionMaster {
      // private static final Logger logger =
      // Logger.getLogger(ConnectionMaster.class.getName());
      private static final Logger LOG = LoggerFactory.getLogger(CustomValidation.class);
      Connection con, con1 = null;
      PreparedStatement ps1, ps, ps2, dmsp, pst, pes, peco = null;
      ResultSet rs1, rs, rs2, dmsr, rst, ress, dmsr1 = null;

      // @Override
      @SuppressWarnings({ "unused", "unchecked" })
      public boolean onPostInitialise() {

            String strPropName = "MigrationDone";
            String dailyval = "";
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryRCODE = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strPropName + "'");
            // //Loggers.general().info(LOG,"ADDDailyTxnLimit initially-------->");
            EXTGENCUSTPROP PROPCode = queryRCODE.getUnique();
            if (PROPCode != null) {

                  dailyval = PROPCode.getPropval();
                  // //Loggers.general().info(LOG,"ADDDailyTxnLimit -------->" +
                  // PROPCode.getPropval());
            } else {
                  // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }
            String eventStatus = getDriverWrapper().getEventFieldAsText("EVSS", "s", "");
            if (dailyval.equalsIgnoreCase("NO")) {
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
                  String eventCode = getDriverWrapper().getEventFieldAsText("EVR", "r", "").trim(); // ISS001
                  String MasterReference = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim(); //
                  String stepID = getDriverWrapper().getEventFieldAsText("CSID", "s", "");
                  if (!stepID.equalsIgnoreCase("Final print") && !eventStatus.equalsIgnoreCase("Completed")) {

                        // getChargeGur();
                        // getStatutoryChargeGur();
                        // getChargeEndDateGur();

                        // getPerdChgEndDate();

                        getPane().ondisplayvalIMPGUAOUTCLAcalyButton();
                        getPane().getExtEventLienMarkingUpdate().setEnabled(true);
                        getPane().getExtEventLienMarkingDelete().setEnabled(false);

                  } else {
                        // Loggers.general().info(LOG," Else systemOutput");
                  }
                  // todo on sep 14
                  if (!stepID.equalsIgnoreCase("Final print") && !eventStatus.equalsIgnoreCase("Completed")) {

                        if (getMajorCode().equalsIgnoreCase("EGT") && getMinorCode().equalsIgnoreCase("VEG")) {

                              try {

                                    String lob1 = getDriverWrapper().getEventFieldAsText("BEN", "p", "cBAV").trim();
                                    String lob2 = getDriverWrapper().getEventFieldAsText("BEN", "p", "cAGQ").trim();
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG, "LOB code 1 ===>" + lob1);
                                          Loggers.general().info(LOG, "LOB code 2 ===>" + lob2);
                                    }

                                    if (lob1 != null && !lob1.equalsIgnoreCase("") && lob1.length() > 0) {
                                          getPane().setLOB(lob1);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG, "LOB code populated from TI+--->" + getPane().getLOB());
                                          }
                                    } else if (lob2 != null && !lob2.equalsIgnoreCase("") && lob2.length() > 0) {
                                          getPane().setLOB(lob2);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG, "LOB code populated from BCIF--->" + getPane().getLOB());
                                          }
                                    }

                              } catch (Exception e) {
                                    Loggers.general().info(LOG, "Exception for LOB code population in EGT===>" + e.getMessage());

                              }

                        }
                  }

                  try {
                        currencyCalc();
                  } catch (Exception e) {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG, "Exception in currency value population---->" + e.getMessage());

                        }
                  }

                  if (getMajorCode().equalsIgnoreCase("IGT")) {
                        getPeriodicAdv();
                  }

                  if ((!getMajorCode().equalsIgnoreCase("ILC") && !getMajorCode().equalsIgnoreCase("IGT")
                              && !getMajorCode().equalsIgnoreCase("ISB"))) {
                        // value for LOB
                        try {
                              getLob();

                        } catch (Exception ee) {
                              Loggers.general().info(LOG, "Exception LOB Catch" + ee.getMessage());
                        }
                  }

                  try {
                        getLOBISSUE();

                  } catch (Exception ee) {
                        Loggers.general().info(LOG, ee.getMessage());
                  }

                  if ((stepID.equalsIgnoreCase("CSM") || stepID.equalsIgnoreCase("CBS Maker"))
                              && (getMinorCode().equalsIgnoreCase("OIG"))) {
                        try {
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG, "Custom fields are clear method");
                              }
                              getcustomValueDelete();

                        }

                        catch (Exception e) {
                              e.getStackTrace();
                        }
                  }
                  /*
                   *
                   * // EBG Field try { String stateCode = getPane().getSTATECOD(); if
                   * (!stateCode.isEmpty()) { System.out.println("inside !stateCode.isEmpty() ");
                   * double stampDuty = getStateWiseStampDuty(stateCode);
                   * getPane().setEBGSTAMPDUTY(stampDuty + " " + "INR");
                   * System.out.println("The state code is ::: " + stateCode +
                   * " Stamp Amount is ::: " + stampDuty); } } catch (Exception e) {
                   *
                   * e.getStackTrace(); }
                   */

                  ExtendedCheckBoxControlWrapper ebgstamp = getPane().getCtlESTAMP_Flag();

                  if (ebgstamp.getValue()) {

                        ExtendedButtonControlWrapper stampDutyCalButton = getPane().getBtnSTAMPIMPGUAAMDclay_2_2();
                        stampDutyCalButton.setEnabled(true);

                        getPane().getCtlEBGSTAMPDUTY().setEnabled(true);

                  } else {
                        ExtendedButtonControlWrapper stampDutyCalButton = getPane().getBtnSTAMPIMPGUAAMDclay_2_2();
                        stampDutyCalButton.setEnabled(false);

                        getPane().setEBGSTAMPDUTY("0.00 INR");
                        getPane().getCtlEBGSTAMPDUTY().setEnabled(false);

                  }

//    DMS field
                  try {
                        String getDmsreferncelink = getDmslink();

                        ExtendedHyperlinkControlWrapper Dmslink = getPane().getCtlDMSReferenceIGTISSclayHyperlink();
                        Dmslink.setUrl(getDmsreferncelink);

                        ExtendedHyperlinkControlWrapper Dmslink1 = getPane().getCtlDMSReferenceIMPGUAAMDclayHyperlink();
                        Dmslink1.setUrl(getDmsreferncelink);

                        ExtendedHyperlinkControlWrapper Dmslink2 = getPane().getCtlDMSReferenceOGTINVOCclayHyperlink();
                        Dmslink2.setUrl(getDmsreferncelink);

                        ExtendedHyperlinkControlWrapper Dmslink3 = getPane().getCtlDMSReferenceIGTCORRElay_2_2Hyperlink();
                        Dmslink3.setUrl(getDmsreferncelink);

                        ExtendedHyperlinkControlWrapper Dmslink4 = getPane().getCtlDMSReferenceOGTISSclay_2_2Hyperlink();
                        Dmslink4.setUrl(getDmsreferncelink);

                        ExtendedHyperlinkControlWrapper Dmslink5 = getPane().getCtlDMSReferenceIMPGUAAMDclay_2_2Hyperlink();
                        Dmslink5.setUrl(getDmsreferncelink);

                        ExtendedHyperlinkControlWrapper Dmslink6 = getPane().getCtlDMSReferenceIGTRIGclayHyperlink();
                        Dmslink6.setUrl(getDmsreferncelink);

                        ExtendedHyperlinkControlWrapper Dmslink7 = getPane().getCtlDMSReferenceIMPGUAOUTCLAcalyHyperlink();
                        Dmslink7.setUrl(getDmsreferncelink);

                        System.out.println("getDmsreferncelink custom validation class " + getDmsreferncelink);
                        Loggers.general().info(LOG, "getDmsreferncelink " + getDmsreferncelink);

                  } catch (Exception e) {

                        System.out.println("getDmsreferncelink exception custom validation class " + e.getStackTrace());
                        e.getStackTrace();
                  }
                  System.out.println("DMS FIELD in custom validation class");

//                end DMS link

//                DMS repo field
                  try {
                        String getDmsreferncelink = getRepolink();

                        ExtendedHyperlinkControlWrapper Dmslink = getPane().getCtlDMSCUSTREPOIGTISSclayHyperlink();
                        Dmslink.setUrl(getDmsreferncelink);

                        ExtendedHyperlinkControlWrapper Dmslink1 = getPane().getCtlDMSCUSTREPOIMPGUAAMDclayHyperlink();
                        Dmslink1.setUrl(getDmsreferncelink);

                        ExtendedHyperlinkControlWrapper Dmslink2 = getPane().getCtlDMSCUSTREPOOGTINVOCclayHyperlink();
                        Dmslink2.setUrl(getDmsreferncelink);

                        ExtendedHyperlinkControlWrapper Dmslink3 = getPane().getCtlDMSCUSTREPOIGTCORRElay_2_2Hyperlink();
                        Dmslink3.setUrl(getDmsreferncelink);

                        ExtendedHyperlinkControlWrapper Dmslink4 = getPane().getCtlDMSCUSTREPOOGTISSclay_2_2Hyperlink();
                        Dmslink4.setUrl(getDmsreferncelink);

                        ExtendedHyperlinkControlWrapper Dmslink5 = getPane().getCtlDMSCUSTREPOIMPGUAAMDclay_2_2Hyperlink();
                        Dmslink5.setUrl(getDmsreferncelink);

                        ExtendedHyperlinkControlWrapper Dmslink6 = getPane().getCtlDMSCUSTREPOIGTRIGclayHyperlink();
                        Dmslink6.setUrl(getDmsreferncelink);

                        ExtendedHyperlinkControlWrapper Dmslink7 = getPane().getCtlDMSCUSTREPOIMPGUAOUTCLAcalyHyperlink();
                        Dmslink7.setUrl(getDmsreferncelink);

                        System.out.println("getDmsReporeferncelink custom validation class " + getDmsreferncelink);
                        Loggers.general().info(LOG, "getDmsReporeferncelink " + getDmsreferncelink);

                  } catch (Exception e) {

                        System.out.println("getDmsReporeferncelink exception custom Onvalidation class " + e.getStackTrace());
                        e.getStackTrace();
                  }
                  System.out.println("DMS repo FIELD in custom validation class");

//     end DMS repo link

                  // workflow CHECKLIST
                  try {

                        String Hyperreferel1 = getWorkflow().trim();
                        ExtendedHyperlinkControlWrapper csmreftrack1 = getPane().getCtlCSMCHECKLISTOGTISSclayHyperlink();
                        csmreftrack1.setUrl(Hyperreferel1);

                        ExtendedHyperlinkControlWrapper csmreftrackamd1 = getPane().getCtlCSMCHECKLISTIMPGUAADJclayHyperlink();
                        csmreftrackamd1.setUrl(Hyperreferel1);

                        ExtendedHyperlinkControlWrapper csmreftrackamd2 = getPane().getCtlCSMCHECKLISTOGTINVOCclayHyperlink();
                        csmreftrackamd2.setUrl(Hyperreferel1);

                        ExtendedHyperlinkControlWrapper csmreftrackamd4 = getPane()
                                    .getCtlCSMCHECKLISTIMPGUAOUTCLAcalyHyperlink();
                        csmreftrackamd4.setUrl(Hyperreferel1);

                        // >>>
                        ExtendedHyperlinkControlWrapper csmreftrackamd6 = getPane().getCtlCSMCHECKLISTIMPGUAAMDclayHyperlink();
                        csmreftrackamd6.setUrl(Hyperreferel1);

                        // ExtendedHyperlinkControlWrapper csmreftrackamd8 =
                        // getPane().getCtlCSMCHECKLISTIMPGUACANclayHyperlink();
                        // csmreftrackamd8.setUrl(Hyperreferel1);

                        ExtendedHyperlinkControlWrapper csmreftrackamd9 = getPane().getCtlCSMCHECKIGTISSclayHyperlink();
                        csmreftrackamd9.setUrl(Hyperreferel1);
                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"REFERE TRACKING--------->" +
                        // ees.getMessage());
                  }

                  // CR 143-Limit Nodes
                  try {
                        String HyperLimitNode = getLimitNode().trim();
                        ExtendedHyperlinkControlWrapper limitnode1 = getPane().getCtlUnavailablelimitnodesOGTISSclayHyperlink();
                        limitnode1.setUrl(HyperLimitNode);
                        ExtendedHyperlinkControlWrapper limitnode2 = getPane()
                                    .getCtlUnavailablelimitnodesIMPGUAADJclayHyperlink();
                        limitnode2.setUrl(HyperLimitNode);
                        ExtendedHyperlinkControlWrapper limitnode3 = getPane()
                                    .getCtlUnavailablelimitnodesIMPGUAAMDclayHyperlink();
                        limitnode3.setUrl(HyperLimitNode);
                        ExtendedHyperlinkControlWrapper limitnode4 = getPane().getCtlUnavailablelimitnodesIGTISSclayHyperlink();
                        limitnode4.setUrl(HyperLimitNode);
                  } catch (Exception e) {
                        System.out.println("For Limit Node" + e.getMessage());

                  }

                  // REFERE TRACKING
                  try {

                        String Hyperreferel = getReferealtracking().trim();
                        ExtendedHyperlinkControlWrapper csmreftrack = getPane().getCtlCSMREFRALOGTISSclayHyperlink();
                        csmreftrack.setUrl(Hyperreferel);

                        ExtendedHyperlinkControlWrapper csmreftrackamd = getPane().getCtlCSMREFRALIMPGUAAMDclayHyperlink();
                        csmreftrackamd.setUrl(Hyperreferel);

                        ExtendedHyperlinkControlWrapper csmreftrackamd1 = getPane().getCtlCSMREFRALIMPGUAADJclayHyperlink();
                        csmreftrackamd1.setUrl(Hyperreferel);

                        ExtendedHyperlinkControlWrapper csmreftrackamd3 = getPane().getCtlCSMREFRALIMPGUAOUTCLAcalyHyperlink();
                        csmreftrackamd3.setUrl(Hyperreferel);

                        ExtendedHyperlinkControlWrapper csmreftrackamd6 = getPane().getCtlCSMREFRALOGTINVOCclayHyperlink();
                        csmreftrackamd6.setUrl(Hyperreferel);

                        // ExtendedHyperlinkControlWrapper csmreftrackamd7 =
                        // getPane().getCtlCSMREFRALIMPGUACANclayHyperlink();
                        // csmreftrackamd7.setUrl(Hyperreferel);
                        ExtendedHyperlinkControlWrapper csmreftrackamd8 = getPane().getCtlCSMREFRALIGTISSclayHyperlink();
                        csmreftrackamd8.setUrl(Hyperreferel);

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"REFERE TRACKING--------->" +
                        // ees.getMessage());
                  }

                  // Workflow CBS
                  try {
                        String Hyperreferel12 = getWorkflowCBS().trim();
                        ExtendedHyperlinkControlWrapper cpcreftrack1 = getPane().getCtlCPCCHECKOGTISSclayHyperlink();
                        cpcreftrack1.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper cpcreftrackamd1 = getPane().getCtlCPCCHECKIMPGUAADJclayHyperlink();
                        cpcreftrackamd1.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper csmreftrackamd3 = getPane().getCtlCPCCHECKOGTINVOCclayHyperlink();
                        csmreftrackamd3.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper cpcreftrackamd5 = getPane().getCtlCPCCHECKIMPGUAOUTCLAcalyHyperlink();
                        cpcreftrackamd5.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper cpcreftrackamd7 = getPane().getCtlCPCCHECKIMPGUAAMDclayHyperlink();
                        cpcreftrackamd7.setUrl(Hyperreferel12);

                        // ExtendedHyperlinkControlWrapper cpcreftrackamd9 =
                        // getPane().getCtlCPCCHECKIMPGUACANclayHyperlink();
                        // cpcreftrackamd9.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper cpcreftrackamd10 = getPane().getCtlCPCCHECKIGTISSclayHyperlink();
                        cpcreftrackamd10.setUrl(Hyperreferel12);
                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"WORKFLOW CHECKLIST WorkflowCBS--------->"
                        // + ees.getMessage());
                  }

                  // Refereal CBS

                  try {
                        String Hyperreferel12 = getReferealtrackingCBS().trim();
                        ExtendedHyperlinkControlWrapper cpcreftrack = getPane().getCtlCPCREFERELOGTISSclayHyperlink();
                        cpcreftrack.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper cpcreftrackamd = getPane().getCtlCPCREFERELIMPGUAAMDclayHyperlink();
                        cpcreftrackamd.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper cpcreftrackamd2 = getPane().getCtlCPCREFERELIMPGUAADJclayHyperlink();
                        cpcreftrackamd2.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper csmreftrackamd5 = getPane().getCtlCPCREFERELIMPGUAOUTCLAcalyHyperlink();
                        csmreftrackamd5.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper cpcreftrackamd4 = getPane().getCtlCPCREFERELOGTINVOCclayHyperlink();
                        cpcreftrackamd4.setUrl(Hyperreferel12);

                        // ExtendedHyperlinkControlWrapper cpcreftrackamd8 =
                        // getPane().getCtlCPCREFERELIMPGUACANclayHyperlink();
                        // cpcreftrackamd8.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper cpcreftrackamd9 = getPane().getCtlCPCREFERELIGTISSclayHyperlink();
                        cpcreftrackamd9.setUrl(Hyperreferel12);
                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"REFERE TRACKING REFERE CBS--------->" +
                        // ees.getMessage());
                  }

                  // FDenquiry link

                  try {

                        String FDenquiryLink = FDenquiry();
                        ExtendedHyperlinkControlWrapper fdlink = getPane().getCtlFDENQUIRYOGTISSclayHyperlink();
                        fdlink.setUrl(FDenquiryLink);
                        ExtendedHyperlinkControlWrapper fdlink1 = getPane().getCtlFDENQUIRYIMPGUAADJclayHyperlink();
                        fdlink1.setUrl(FDenquiryLink);
                        ExtendedHyperlinkControlWrapper fdlink2 = getPane().getCtlFDENQUIRYIMPGUAAMDclayHyperlink();
                        fdlink2.setUrl(FDenquiryLink);
                        ExtendedHyperlinkControlWrapper fdlink3 = getPane().getCtlFDENQUIRYOGTINVOCclayHyperlink();
                        fdlink3.setUrl(FDenquiryLink);
                        ExtendedHyperlinkControlWrapper fdlink4 = getPane().getCtlFDENQUIRYIMPGUAOUTCLAcalyHyperlink();
                        fdlink4.setUrl(FDenquiryLink);

                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"FDenquiry link"+e.getMessage());
                  }

                  // check list table disabled

                  // //Loggers.general().info(LOG,"prodCode for chacklist------------>" +
                  // stepID);

                  // try
                  //
                  // {
                  // getclaimDateCal();
                  // } catch (Exception e) {
                  // if (dailyval_Log.equalsIgnoreCase("YES")) {
                  // Loggers.general().info(LOG,"IGT claim and new claim expiry
                  // connection---->" + e.getMessage());
                  // }
                  // }

                  // Claim date calculation
                  String product = getDriverWrapper().getEventFieldAsText("PCO", "s", "").trim();
                  // //Loggers.general().info(LOG,"product code--->" + product);
                  String event = getDriverWrapper().getEventFieldAsText("EVCD", "s", "").trim();
                  // //Loggers.general().info(LOG,"Event code--->" + event);
                  String expdate = getDriverWrapper().getEventFieldAsText("EXP", "d", ""); // 12/10/16
                  String provisional = getDriverWrapper().getEventFieldAsText("EPRV", "l", "");
                  System.out.println("provisional--->" + provisional);
                  SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                  Calendar c = Calendar.getInstance();
                  try {
                        // CR 146 Starts
                        if ((getMajorCode().equalsIgnoreCase("IGT")) && (getMinorCode().equalsIgnoreCase("IIG"))) {
                              this.getPane().getCtlNEWCLMDT().getControl().setVisible(false);
                              this.getPane().getCtlPROVIN().getControl().setVisible(false);

                              if (provisional.equalsIgnoreCase("Y")) {
                                    getPane().setPROVIN(true);
                              }
                              if (provisional.equalsIgnoreCase("N")) {
                                    getPane().setPROVIN(false);
                              }
                        }
                        // CR 146 ends
                        // //Loggers.general().info(LOG,"Expiry date" + expdate);
                        String graceda = "0";
                        graceda = getWrapper().getGRACEPER();
                        /*
                         * String claimdate = getWrapper().getCLIMEXPD(); //Loggers.general().info(LOG,
                         * "claimdate is claimdate ----> " + claimdate);
                         */
                        int gra = 0;
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG, "claim day initial ----> " + graceda);
                        }

                        if (((!graceda.equalsIgnoreCase("")) || graceda != null) && graceda.length() > 0) {
                              // //Loggers.general().info(LOG,"claimdate is blank");
                              try {
                                    if (graceda.length() > 0 && getMinorCode().equalsIgnoreCase("IIG")
                                                && product.equalsIgnoreCase("IGT")) {
                                          gra = Integer.parseInt(graceda);
                                          c.setTime(sdf.parse(expdate));
                                          // //Loggers.general().info(LOG,"expdate in issue------->
                                          // ");
                                          c.add(Calendar.DATE, gra);
                                          // //Loggers.general().info(LOG,"DATE 1"+ c);
                                          String output = sdf.format(c.getTime());
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG, "Claim expiry date----> " + output);
                                          }
                                          getPane().setCLIMEXPD(output);
                                          // CR 146 Starts
                                          getPane().setNEWCLMDT(output);
                                          getWrapper().setNEWCLMDT(output);
                                          // CR 146 Ends

                                    } else {
                                          // //Loggers.general().info(LOG,"New claim expiry date enter
                                          // ------->
                                          // ");
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG, "claim day amend ----> " + graceda);
                                          }
                                          if (graceda.length() > 0 && product.equalsIgnoreCase("IGT") && expdate.length() > 0) {
                                                gra = Integer.parseInt(graceda);
                                                c.setTime(sdf.parse(expdate));
                                                // //Loggers.general().info(LOG,"expdate in adjust and
                                                // amend-------> ");
                                                c.add(Calendar.DATE, gra);
                                                // //Loggers.general().info(LOG,"DATE 1"+ c);
                                                String output = sdf.format(c.getTime());
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG, "New Claim expiry date----> " + output);
                                                }
                                                getPane().setNEWCLMDT(output);
                                                getWrapper().setNEWCLMDT(output);
                                          } else {
                                                // Loggers.general().info(LOG,"else part of grace
                                                // days");
                                          }
                                    }
                              } catch (Exception e) {
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG, "IGT claim and new claim expiry date---->" + e.getMessage());
                                    }
                              }

                        } else {

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG, "claim day is blank ----> " + graceda);
                              }
                              try {
                                    if (expdate.length() > 0) {
                                          c.setTime(sdf.parse(expdate));

                                          c.add(Calendar.DATE, gra);
                                          // //Loggers.general().info(LOG,"DATE 1"+ c);
                                          String output = sdf.format(c.getTime());
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG, "claim expiry days and claim day is blank ----> " + output);
                                          }
                                          getPane().setCLIMEXPD(output);
                                          getWrapper().setCLIMEXPD(output);
                                    } else {
                                          // Loggers.general().info(LOG,"expdate is blank" + expdate);
                                    }
                              } catch (ParseException e) {
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG, "IGT grace days---->" + e.getMessage());
                                    }
                              }

                        }
                  } catch (Exception e) {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG, "IGT grace days---->" + e.getMessage());
                        }
                  }
                  // Effective date population
                  String systemDate = getDriverWrapper().getEventFieldAsText("TDY", "d", "");
                  String effct = getWrapper().getEFFDATE();
                  // //Loggers.general().info(LOG,"effct date --->" + effct);
                  if (!stepID.equalsIgnoreCase("Final print") && !eventStatus.equalsIgnoreCase("Completed")) {
                        if (effct == null) {

                              try {
                                    SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yy");
                                    Calendar cal = Calendar.getInstance();
                                    int val = 0;
                                    cal.setTime(sdf1.parse(systemDate));
                                    // //Loggers.general().info(LOG,"effct in issue-------> ");
                                    cal.add(Calendar.DATE, val);
                                    String output = sdf1.format(cal.getTime());
                                    // Loggers.general().info(LOG,"output----->" + output);
                                    getPane().setEFFDATE(output);

                              } catch (Exception e) {
                                    // Loggers.general().info(LOG,"EFFECTIVE date --->" +
                                    // e.getMessage());
                              }
                        } else {
                              String effctive = getWrapper().getEFFDATE();
                              // //Loggers.general().info(LOG,"effctive date in else --->" +
                              // effctive);
                              try {
                                    SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yy");
                                    Calendar cal = Calendar.getInstance();
                                    int val = 0;
                                    cal.setTime(sdf1.parse(effctive));
                                    // //Loggers.general().info(LOG,"effct in issue-------> ");
                                    cal.add(Calendar.DATE, val);
                                    String output = sdf1.format(cal.getTime());
                                    // Loggers.general().info(LOG,"output in else----->" + output);
                                    getPane().setEFFDATE(output);

                              } catch (Exception e) {
                                    // Loggers.general().info(LOG,"EFFECTIVE date in else--->" +
                                    // e.getMessage());
                              }
                        }
                  }
                  // SFMS
                  try {

                        String getHyperSFMS = getIFSCSEARCH();
                        ExtendedHyperlinkControlWrapper sfmsExpAdv = getPane().getCtlSFMSOGTISSclayHyperlink();
                        sfmsExpAdv.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv1 = getPane().getCtlSFMSIMPGUAAMDclayHyperlink();
                        sfmsExpAdv1.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv2 = getPane().getCtlSFMSIMPGUAADJclayHyperlink();
                        sfmsExpAdv2.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv3 = getPane().getCtlSFMSIMPGUAOUTCLAcalyHyperlink();
                        sfmsExpAdv3.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv4 = getPane().getCtlSFMSIMPGUAISSONcalyHyperlink();
                        sfmsExpAdv4.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv5 = getPane().getCtlSFMSOGTINVOCclayHyperlink();
                        sfmsExpAdv5.setUrl(getHyperSFMS);
                        // ExtendedHyperlinkControlWrapper sfmsExpAdv6 =
                        // getPane().getCtlSFMSIMPGUACANclayHyperlink();
                        // sfmsExpAdv6.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv7 = getPane().getCtlSFMSIMPGUAREPclayHyperlink();
                        sfmsExpAdv7.setUrl(getHyperSFMS);

                        ExtendedHyperlinkControlWrapper sfmsExpAdv8 = getPane().getCtlSFMSIGTISSclayHyperlink();
                        sfmsExpAdv8.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv9 = getPane().getCtlSFMSEXPGRNTCANLayHyperlink();
                        sfmsExpAdv9.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv10 = getPane().getCtlSFMSEXPGRNTEEOPclayHyperlink();
                        sfmsExpAdv10.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv11 = getPane().getCtlSFMSIGTCORRElayHyperlink();
                        sfmsExpAdv11.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv12 = getPane().getCtlSFMSIMPGUACHARGESclayHyperlink();
                        sfmsExpAdv12.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv13 = getPane().getCtlSFMSOGTISSclay_2_2Hyperlink();
                        sfmsExpAdv13.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv14 = getPane().getCtlSFMSIMPGUAAMDclay_2_2Hyperlink();
                        sfmsExpAdv14.setUrl(getHyperSFMS);
                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,ees.getMessage());
                  }

                  // TST
                  try {
                        String val = "DMSTS";
                        String TSTHyperlink = Link(val);
                        // String TSTHyperlink = getTSTHyperLINK();

                        ExtendedHyperlinkControlWrapper dmsh = getPane().getCtlTSTOGTISSclay_2_2Hyperlink();
                        dmsh.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh1 = getPane().getCtlTSTIMPGUAADJclayHyperlink();
                        dmsh1.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh2 = getPane().getCtlTSTIMPGUAAMDclay_2_2Hyperlink();
                        dmsh2.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh3 = getPane().getCtlTSTIMPGUAISSONcalyHyperlink();
                        dmsh3.setUrl(TSTHyperlink);
                        // ExtendedHyperlinkControlWrapper dmsh4 =
                        // getPane().getCtlTSTIMPGUACANclayHyperlink();
                        // dmsh4.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh5 = getPane().getCtlTSTIMPGUAOUTCLAcalyHyperlink();
                        dmsh5.setUrl(TSTHyperlink);
                        // ExtendedHyperlinkControlWrapper dmsh6 =
                        // getPane().getCtlTSTIMPGUAEXPclayHyperlink();
                        // dmsh6.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh7 = getPane().getCtlTSTIMPGUACHARGESclayHyperlink();
                        dmsh7.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh8 = getPane().getCtlTSTOGTINVOCclayHyperlink();
                        dmsh8.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh9 = getPane().getCtlTSTIMPGUAREPclayHyperlink();
                        dmsh9.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh10 = getPane().getCtlTSTIGTCORRElayHyperlink();
                        dmsh10.setUrl(TSTHyperlink);

                        ExtendedHyperlinkControlWrapper dmsh11 = getPane().getCtlTSTEGTBenResptoCanceLayHyperlink();
                        dmsh11.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh12 = getPane().getCtlTSTEXPGRNTCANLayHyperlink();
                        dmsh12.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh13 = getPane().getCtlTSTEGTBenResptoAmdLayHyperlink();
                        dmsh13.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh14 = getPane().getCtlTSTEXPGRNTEEOPclayHyperlink();
                        dmsh14.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh15 = getPane().getCtlTSTIGTISSclayHyperlink();
                        dmsh15.setUrl(TSTHyperlink);

                        ExtendedHyperlinkControlWrapper dmsh16 = getPane().getCtlTSTIMPGUAMANULayHyperlink();
                        dmsh16.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh17 = getPane().getCtlTSTIMPGUABOOKLayHyperlink();
                        dmsh17.setUrl(TSTHyperlink);

                        // ExtendedHyperlinkControlWrapper dmsh18 =
                        // getPane().getCtlTSTIMPGUABENCANlayHyperlink();
                        // dmsh18.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh19 = getPane().getCtlTSTIMPGUABENAMDlayHyperlink();
                        dmsh19.setUrl(TSTHyperlink);

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,ees.getMessage());
                  }

                  // TST
                  try {
                        String val = "DMSTS";
                        String TSTHyperlink = Link(val);
                        // String TSTHyperlink = getTSTHyperLINK();

                        ExtendedHyperlinkControlWrapper dmsh = getPane().getCtlTSTOGTISSclayHyperlink();
                        dmsh.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh1 = getPane().getCtlTSTIMPGUAADJclayHyperlink();
                        dmsh1.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh2 = getPane().getCtlTSTIMPGUAAMDclayHyperlink();
                        dmsh2.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh3 = getPane().getCtlTSTIMPGUAISSONcalyHyperlink();
                        dmsh3.setUrl(TSTHyperlink);
                        // ExtendedHyperlinkControlWrapper dmsh4 =
                        // getPane().getCtlTSTIMPGUACANclayHyperlink();
                        // dmsh4.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh5 = getPane().getCtlTSTIMPGUAOUTCLAcalyHyperlink();
                        dmsh5.setUrl(TSTHyperlink);
                        // ExtendedHyperlinkControlWrapper dmsh6 =
                        // getPane().getCtlTSTIMPGUAEXPclayHyperlink();
                        // dmsh6.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh7 = getPane().getCtlTSTIMPGUACHARGESclayHyperlink();
                        dmsh7.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh8 = getPane().getCtlTSTOGTINVOCclayHyperlink();
                        dmsh8.setUrl(TSTHyperlink);
                        ExtendedHyperlinkControlWrapper dmsh9 = getPane().getCtlTSTIMPGUAREPclayHyperlink();
                        dmsh9.setUrl(TSTHyperlink);
                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,ees.getMessage());
                  }
                  String subproCode = getDriverWrapper().getEventFieldAsText("PTP", "s", "");
                  String fullcur = getDriverWrapper().getEventFieldAsText("ORA", "v", "c");
                  String csty_id = getDriverWrapper().getEventFieldAsText("CSID", "s", "");
                  String eventPrefix = getDriverWrapper().getEventFieldAsText("EVCD", "s", "");
                  String portode = getWrapper().getPORTCOD_Name();
                  String shipfrm = getWrapper().getSHIPFTO_Name();
                  String shipto = getWrapper().getSHIPTOP_Name();

                  // INTEREST AMOUNT CALCULATION
                  try {
                        if (subproCode.equalsIgnoreCase("BCR")
                                    && (!stepID.equalsIgnoreCase("Final print") && !eventStatus.equalsIgnoreCase("Completed"))) {

                              totalLiaAmount();

                        } else {
                              // Loggers.general().info(LOG,"Product type is not a buyer credit");
                        }
                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"Exception in total liability amount" +
                        // e.getMessage());
                  }

                  String portval = getWrapper().getPORTCOD_Name().trim();
                  // //Loggers.general().info(LOG,"Port Value---->" + portval);
                  if ((!portval.equalsIgnoreCase("")) && portval != null) {
                        try {
                              // //Loggers.general().info(LOG,"hscode Value in try---->" +
                              // hscodeval);
                              String hyperValue = "SELECT trim(PNAME),trim(COUNTRY) FROM EXTPORTCO WHERE PCODE='" + portval + "'";
                              // //Loggers.general().info(LOG,"port code query Value---->" +
                              // hyperValue);
                              con = ConnectionMaster.getConnection();
                              ps1 = con.prepareStatement(hyperValue);
                              rs = ps1.executeQuery();
                              while (rs.next()) {
                                    String hsploy = rs.getString(1);
                                    String poname = rs.getString(2);
                                    // //Loggers.general().info(LOG,"port code description---->" +
                                    // hsploy);
                                    getPane().setPORTDESC(hsploy);
                                    getPane().setPORTDECO(poname);
                              }

                        } catch (Exception e) {
                              // Loggers.general().info(LOG,"exception is " + e);

                        } finally {
                              try {

                                    if (rs != null)
                                          rs.close();
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
                        // Loggers.general().info(LOG,"port code is empty");
                  }

                  // DIFFERENCE BETWEEN EXPIRY DATE AND SHIPMENT DATE
                  if (subproCode.equalsIgnoreCase("BCR")
                              && (!stepID.equalsIgnoreCase("Final print") && !eventStatus.equalsIgnoreCase("Completed"))) {
                        try {
                              String d = getDriverWrapper().getEventFieldAsText("EXP", "d", "").trim();
                              // Loggers.general().info(LOG,"Shipment date difference values");
                              String d1 = getWrapper().getSHTDAT();
                              if (d != null && d1 != null && d1.length() > 0 && d.length() > 0) {
                                    getPane().setNUMDAYS(difference(d, d1).substring(0, difference(d, d1).indexOf(".")));
                                    getWrapper().setNUMDAYS(difference(d, d1).substring(0, difference(d, d1).indexOf(".")));
                              } else {
                                    // Loggers.general().info(LOG,"else part of date difference
                                    // values");
                              }
                        } catch (Exception e) {
                              // Loggers.general().info(LOG,"exeception date difference values" +
                              // e);
                        }
                  }

                  String rtgsFlag = getDriverWrapper().getEventFieldAsText("cBPJ", "l", "");
                  if (rtgsFlag.equalsIgnoreCase("N") && !rtgsFlag.equalsIgnoreCase("Y")) {
                        getPane().setRTGNFT("");
                        getPane().setNETSCF("");
                        getPane().setPROREMT("");
                        getPane().setPURCODE("");
                        getPane().setBENTYP("");
                        getPane().setBENACC_Name("");
                        getPane().setBENNAME_Name("");
                        getPane().setIFSCCO_Name("");
                        getPane().setBENBAK("");
                        getPane().setBENBRN("");
                        getPane().setBENCITY("");
                        getPane().setUTRNO("");
                        getPane().setRTGSNEFT("");
                        getPane().setNARRTVE("");

                  }

                  // Update value
                  String rtgspart = getWrapper().getRTGSPART();
                  try {

                        // getutrNoGenerated();
                  } catch (Exception ee) {
                        Loggers.general().info(LOG, "UTR Number getutrNoGenerated" + ee.getMessage());

                  }

                  try {
                        String nodays = getWrapper().getNODTBFEX();
                        // Loggers.general().info(LOG,"Number of days==========>" + nodays);
                        if ((nodays != null && nodays.length() > 0) && (expdate != null && expdate.length() > 0)
                                    && (!stepID.equalsIgnoreCase("Final print") && !eventStatus.equalsIgnoreCase("Completed"))) {
                              // //Loggers.general().info(LOG,"claimdate is blank");
                              int numInt = 0;

                              String numSub = "-" + nodays;
                              // Loggers.general().info(LOG,"Number of days in minus==========>" +
                              // nodays);
                              numInt = Integer.parseInt(numSub);
                              // Loggers.general().info(LOG,"Number of days in int==========>"
                              // +numInt);
                              c.setTime(sdf.parse(expdate));
                              // //Loggers.general().info(LOG,"expdate in issue-------> ");
                              c.add(Calendar.DATE, numInt);
                              // //Loggers.general().info(LOG,"DATE 1"+ c);
                              String output = sdf.format(c.getTime());
                              // Loggers.general().info(LOG,"Number of days in output==========>"
                              // + output);
                              // getPane().setINVOREM(output);
                              // getWrapper().setINVOREM(output);
                        } else {
                              // getPane().setINVOREM("");
                        }

                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"exeception" + e);
                  }

            
                  if (stepID.equalsIgnoreCase("CSM") || stepID.equalsIgnoreCase("CBS Maker")) {
                        Loggers.general().info(LOG, "statutory calling in input step on post====>");
                        try {
                              // getPerdChgEndDate();
                              String newExpDate = getDriverWrapper().getEventFieldAsText("EXP", "d", ""); // 12/10/16
                              Loggers.general().info(LOG,
                                          "Expiry date in statutory claim in customvalidation on post" + newExpDate);

//                            getPane().setTEMEXPIR(newExpDate);
//                            Loggers.general().info(LOG,"Temp expiry date in customvalidaton on post"+getPane().getTEMEXPIR());

                        } catch (Exception e) {
                              Loggers.general().info(LOG, "Exception in getStatutoryChargeGur onpost" + e.getMessage());
                        }
                  }

                  // to do on 08 AUG 2019

                  String perichargval = getPane().getPERCHRAD().trim();
                  Loggers.general().info(LOG, "Periodic charge in upfront " + getPane().getPERCHRAD());

                  if ((getMajorCode().equalsIgnoreCase("IGT")) && (getMinorCode().equalsIgnoreCase("IIG"))) {
                        try {

                              String d = getDriverWrapper().getEventFieldAsText("ISS", "d", "").trim();
                              String d1 = getDriverWrapper().getEventFieldAsText("cBKY", "d", "").trim();

                              Loggers.general().info(LOG, "Issue date issue" + d);
                              Loggers.general().info(LOG, "Charge basis end date issue " + d1);

                              getPane().setCHRPERAM(difference(d, d1).substring(0, difference(d, d1).indexOf(".")));
                              Loggers.general().info(LOG, "charge - issue date days issue" + getPane().getCHRPERAM());

                        } catch (Exception e) {
                              Loggers.general().info(LOG, "Exception in calculating days issue " + e.getMessage());
                        }
                  }
                  if ((getMajorCode().equalsIgnoreCase("IGT")) && (getMinorCode().equalsIgnoreCase("NJIG"))) {
                        try {

                              String d = getDriverWrapper().getEventFieldAsText("AMD", "d", "").trim();
                              String d1 = getDriverWrapper().getEventFieldAsText("cBKY", "d", "").trim();

                              Loggers.general().info(LOG, "Issue date adjust" + d);
                              Loggers.general().info(LOG, "Charge basis end date adjust " + d1);

                              getPane().setCHRPERAM(difference(d, d1).substring(0, difference(d, d1).indexOf(".")));
                              Loggers.general().info(LOG, "charge - issue date days adjust" + getPane().getCHRPERAM());

                        } catch (Exception e) {
                              Loggers.general().info(LOG, "Exception in calculating days adjust " + e.getMessage());
                        }
                  }

                  if (perichargval.equalsIgnoreCase("Yes")) {

                        if ((getMajorCode().equalsIgnoreCase("IGT")) && (getMinorCode().equalsIgnoreCase("NAIG"))) {

                              String imlAmount = getDriverWrapper().getEventFieldAsText("IML", "v", "m").trim();
                              String n = getDriverWrapper().getEventFieldAsText("cBKY", "d", "").trim();
                              String m = getDriverWrapper().getEventFieldAsText("cBKZ", "d", "").trim();
                              Loggers.general().info(LOG, "iml amount amend" + imlAmount);
                              Loggers.general().info(LOG, "charge end end mirror " + m);
                              Loggers.general().info(LOG, "charge end end  " + n);

                              try {
                                    SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");

                                    Date date1 = sdf1.parse(n);
                                    Date date2 = sdf1.parse(m);

                                    Loggers.general().info(LOG, "date1.compareTo(date2) " + date1.compareTo(date2));
                                    if (date1.compareTo(date2) > 0) {
                                          try {

                                                String d = getDriverWrapper().getEventFieldAsText("occBKY", "d", "");
                                                String d1 = getDriverWrapper().getEventFieldAsText("nccBKY", "d", "");

                                                Loggers.general().info(LOG, "Issue date amend" + d);
                                                Loggers.general().info(LOG, "Charge basis end date amend " + d1);

                                                getPane().setCHRTENIN(difference(d, d1).substring(0, difference(d, d1).indexOf(".")));
                                                Loggers.general().info(LOG, "charge - issue date days amend" + getPane().getCHRTENIN());

                                          } catch (Exception e) {
                                                Loggers.general().info(LOG, "Exception in calculating days amend " + e.getMessage());
                                          }
                                    }

                                    if (imlAmount != null && !imlAmount.equalsIgnoreCase("")) {

                                          try {

                                                String d = getDriverWrapper().getEventFieldAsText("AMD", "d", "");
                                                String d1 = getDriverWrapper().getEventFieldAsText("cBKZ", "d", "");

                                                Loggers.general().info(LOG, "Issue date amend" + d);
                                                Loggers.general().info(LOG, "Charge basis end date amend " + d1);

                                          } catch (Exception e) {
                                                Loggers.general().info(LOG, "Exception in calculating days amend " + e.getMessage());
                                          }
                                    }

                              } catch (Exception e) {
                                    Loggers.general().info(LOG, "Exception in date format" + e.getMessage());
                              }

                        }
                  }

                  // end to do on 08 AUG 2019

            }
            return false;

      }

      @SuppressWarnings("unused")
      @Override
      public void onValidate(ValidationDetails validationDetails) {


            String strPropName = "MigrationDone";
            String dailyval = "";
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryRCODE = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strPropName + "'");
            // //Loggers.general().info(LOG,"ADDDailyTxnLimit initially-------->");
            EXTGENCUSTPROP PROPCode = queryRCODE.getUnique();
            if (PROPCode != null) {

                  dailyval = PROPCode.getPropval();
                  // //Loggers.general().info(LOG,"ADDDailyTxnLimit -------->" +
                  // PROPCode.getPropval());
            } else {
                  // Loggers.general().info(LOG,"ADDDailyTxnLimit is empty-------->");

            }
            String eventStatus = getDriverWrapper().getEventFieldAsText("EVSS", "s", "");

            if (dailyval.equalsIgnoreCase("NO")) {

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
                        getPostingFxrate();
                        System.out.println("outside posting rate details");
                  } catch (Exception e) {
                        e.printStackTrace();
                  }
                  getChargeBasisAmountSplit();// ChargeBasisAmountCalculation_210921
                  String step_Input = getDriverWrapper().getEventFieldAsText("CSTY", "s", "");
                  String step_csm = getDriverWrapper().getEventFieldAsText("CSID", "s", "");
                  // Loggers.general().info(LOG,"step id check for CSM ----->" + step_csm);

                  String rtgsFlag = getDriverWrapper().getEventFieldAsText("cBPJ", "l", "");
                  if (rtgsFlag.equalsIgnoreCase("N") && !rtgsFlag.equalsIgnoreCase("Y")) {
                        getPane().setRTGNFT("");
                        getPane().setNETSCF("");
                        getPane().setPROREMT("");
                        getPane().setPURCODE("");
                        getPane().setBENTYP("");
                        getPane().setBENACC_Name("");
                        getPane().setBENNAME_Name("");
                        getPane().setIFSCCO_Name("");
                        getPane().setBENBAK("");
                        getPane().setBENBRN("");
                        getPane().setBENCITY("");
                        getPane().setUTRNO("");
                        getPane().setRTGSNEFT("");
                        getPane().setNARRTVE("");

                  }

                  // SFMS
                  try {
                        getPane().ondisplayvalIMPGUAOUTCLAcalyButton();

                        String getHyperSFMS = getIFSCSEARCH();
                        ExtendedHyperlinkControlWrapper sfmsExpAdv = getPane().getCtlSFMSOGTISSclayHyperlink();
                        sfmsExpAdv.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv1 = getPane().getCtlSFMSIMPGUAAMDclayHyperlink();
                        sfmsExpAdv1.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv2 = getPane().getCtlSFMSIMPGUAADJclayHyperlink();
                        sfmsExpAdv2.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv3 = getPane().getCtlSFMSIMPGUAOUTCLAcalyHyperlink();
                        sfmsExpAdv3.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv4 = getPane().getCtlSFMSIMPGUAISSONcalyHyperlink();
                        sfmsExpAdv4.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv5 = getPane().getCtlSFMSOGTINVOCclayHyperlink();
                        sfmsExpAdv5.setUrl(getHyperSFMS);
                        // ExtendedHyperlinkControlWrapper sfmsExpAdv6 =
                        // getPane().getCtlSFMSIMPGUACANclayHyperlink();
                        // sfmsExpAdv6.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv7 = getPane().getCtlSFMSIMPGUAREPclayHyperlink();
                        sfmsExpAdv7.setUrl(getHyperSFMS);

                        ExtendedHyperlinkControlWrapper sfmsExpAdv8 = getPane().getCtlSFMSIGTISSclayHyperlink();
                        sfmsExpAdv8.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv9 = getPane().getCtlSFMSEXPGRNTCANLayHyperlink();
                        sfmsExpAdv9.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv10 = getPane().getCtlSFMSEXPGRNTEEOPclayHyperlink();
                        sfmsExpAdv10.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv11 = getPane().getCtlSFMSIGTCORRElayHyperlink();
                        sfmsExpAdv11.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdv12 = getPane().getCtlSFMSIMPGUACHARGESclayHyperlink();
                        sfmsExpAdv12.setUrl(getHyperSFMS);
                        // ExtendedHyperlinkControlWrapper sfmsExpAdv13 =
                        // getPane().getCtlSFMSIMPGUAEXPclayHyperlink();
                        // sfmsExpAdv13.setUrl(getHyperSFMS);
                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,ees.getMessage());
                  }

                  // FDenquiry link

                  try {

                        String FDenquiryLink = FDenquiry();
                        ExtendedHyperlinkControlWrapper fdlink = getPane().getCtlFDENQUIRYOGTISSclayHyperlink();
                        fdlink.setUrl(FDenquiryLink);
                        ExtendedHyperlinkControlWrapper fdlink1 = getPane().getCtlFDENQUIRYIMPGUAADJclayHyperlink();
                        fdlink1.setUrl(FDenquiryLink);
                        ExtendedHyperlinkControlWrapper fdlink2 = getPane().getCtlFDENQUIRYIMPGUAAMDclayHyperlink();
                        fdlink2.setUrl(FDenquiryLink);
                        ExtendedHyperlinkControlWrapper fdlink3 = getPane().getCtlFDENQUIRYOGTINVOCclayHyperlink();
                        fdlink3.setUrl(FDenquiryLink);
                        ExtendedHyperlinkControlWrapper fdlink4 = getPane().getCtlFDENQUIRYIMPGUAOUTCLAcalyHyperlink();
                        fdlink4.setUrl(FDenquiryLink);

                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"FDenquiry link"+e.getMessage());
                  }

                  // workflow CHECKLIST
                  try {
                        String Hyperreferel1 = getWorkflow().trim();
                        ExtendedHyperlinkControlWrapper csmreftrack1 = getPane().getCtlCSMCHECKLISTOGTISSclayHyperlink();
                        csmreftrack1.setUrl(Hyperreferel1);

                        ExtendedHyperlinkControlWrapper csmreftrackamd1 = getPane().getCtlCSMCHECKLISTIMPGUAADJclayHyperlink();
                        csmreftrackamd1.setUrl(Hyperreferel1);

                        ExtendedHyperlinkControlWrapper csmreftrackamd2 = getPane().getCtlCSMCHECKLISTOGTINVOCclayHyperlink();
                        csmreftrackamd2.setUrl(Hyperreferel1);

                        ExtendedHyperlinkControlWrapper csmreftrackamd4 = getPane()
                                    .getCtlCSMCHECKLISTIMPGUAOUTCLAcalyHyperlink();
                        csmreftrackamd4.setUrl(Hyperreferel1);

                        // >>>
                        ExtendedHyperlinkControlWrapper csmreftrackamd6 = getPane().getCtlCSMCHECKLISTIMPGUAAMDclayHyperlink();
                        csmreftrackamd6.setUrl(Hyperreferel1);

                        // ExtendedHyperlinkControlWrapper csmreftrackamd8 =
                        // getPane().getCtlCSMCHECKLISTIMPGUACANclayHyperlink();
                        // csmreftrackamd8.setUrl(Hyperreferel1);

                        ExtendedHyperlinkControlWrapper csmreftrackamd9 = getPane().getCtlCSMCHECKIGTISSclayHyperlink();
                        csmreftrackamd9.setUrl(Hyperreferel1);
                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"REFERE TRACKING--------->" +
                        // ees.getMessage());
                  }

                  // CR 143-Limit Nodes
                  try {
                        String HyperLimitNode = getLimitNode().trim();
                        ExtendedHyperlinkControlWrapper limitnode1 = getPane().getCtlUnavailablelimitnodesOGTISSclayHyperlink();
                        limitnode1.setUrl(HyperLimitNode);
                        ExtendedHyperlinkControlWrapper limitnode2 = getPane()
                                    .getCtlUnavailablelimitnodesIMPGUAADJclayHyperlink();
                        limitnode2.setUrl(HyperLimitNode);
                        ExtendedHyperlinkControlWrapper limitnode3 = getPane()
                                    .getCtlUnavailablelimitnodesIMPGUAAMDclayHyperlink();
                        limitnode3.setUrl(HyperLimitNode);
                        ExtendedHyperlinkControlWrapper limitnode4 = getPane().getCtlUnavailablelimitnodesIGTISSclayHyperlink();
                        limitnode4.setUrl(HyperLimitNode);
                  } catch (Exception e) {
                        System.out.println("For Limit Node" + e.getMessage());

                  }

                  // REFERE TRACKING
                  try {

                        String Hyperreferel = getReferealtracking().trim();
                        ExtendedHyperlinkControlWrapper csmreftrack = getPane().getCtlCSMREFRALOGTISSclayHyperlink();
                        csmreftrack.setUrl(Hyperreferel);

                        ExtendedHyperlinkControlWrapper csmreftrackamd = getPane().getCtlCSMREFRALIMPGUAAMDclayHyperlink();
                        csmreftrackamd.setUrl(Hyperreferel);

                        ExtendedHyperlinkControlWrapper csmreftrackamd1 = getPane().getCtlCSMREFRALIMPGUAADJclayHyperlink();
                        csmreftrackamd1.setUrl(Hyperreferel);

                        ExtendedHyperlinkControlWrapper csmreftrackamd3 = getPane().getCtlCSMREFRALIMPGUAOUTCLAcalyHyperlink();
                        csmreftrackamd3.setUrl(Hyperreferel);

                        ExtendedHyperlinkControlWrapper csmreftrackamd6 = getPane().getCtlCSMREFRALOGTINVOCclayHyperlink();
                        csmreftrackamd6.setUrl(Hyperreferel);

                        // ExtendedHyperlinkControlWrapper csmreftrackamd7 =
                        // getPane().getCtlCSMREFRALIMPGUACANclayHyperlink();
                        // csmreftrackamd7.setUrl(Hyperreferel);
                        ExtendedHyperlinkControlWrapper csmreftrackamd8 = getPane().getCtlCSMREFRALIGTISSclayHyperlink();
                        csmreftrackamd8.setUrl(Hyperreferel);

                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"REFERE TRACKING--------->" +
                        // ees.getMessage());
                  }

                  // Workflow CBS
                  try {
                        String Hyperreferel12 = getWorkflowCBS().trim();
                        ExtendedHyperlinkControlWrapper cpcreftrack1 = getPane().getCtlCPCCHECKOGTISSclayHyperlink();
                        cpcreftrack1.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper cpcreftrackamd1 = getPane().getCtlCPCCHECKIMPGUAADJclayHyperlink();
                        cpcreftrackamd1.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper csmreftrackamd3 = getPane().getCtlCPCCHECKOGTINVOCclayHyperlink();
                        csmreftrackamd3.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper cpcreftrackamd5 = getPane().getCtlCPCCHECKIMPGUAOUTCLAcalyHyperlink();
                        cpcreftrackamd5.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper cpcreftrackamd7 = getPane().getCtlCPCCHECKIMPGUAAMDclayHyperlink();
                        cpcreftrackamd7.setUrl(Hyperreferel12);

                        // ExtendedHyperlinkControlWrapper cpcreftrackamd9 =
                        // getPane().getCtlCPCCHECKIMPGUACANclayHyperlink();
                        // cpcreftrackamd9.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper cpcreftrackamd10 = getPane().getCtlCPCCHECKIGTISSclayHyperlink();
                        cpcreftrackamd10.setUrl(Hyperreferel12);
                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"WORKFLOW CHECKLIST WorkflowCBS--------->"
                        // + ees.getMessage());
                  }

                  // Refereal CBS

                  try {
                        String Hyperreferel12 = getReferealtrackingCBS().trim();
                        ExtendedHyperlinkControlWrapper cpcreftrack = getPane().getCtlCPCREFERELOGTISSclayHyperlink();
                        cpcreftrack.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper cpcreftrackamd = getPane().getCtlCPCREFERELIMPGUAAMDclayHyperlink();
                        cpcreftrackamd.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper cpcreftrackamd2 = getPane().getCtlCPCREFERELIMPGUAADJclayHyperlink();
                        cpcreftrackamd2.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper csmreftrackamd5 = getPane().getCtlCPCREFERELIMPGUAOUTCLAcalyHyperlink();
                        csmreftrackamd5.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper cpcreftrackamd4 = getPane().getCtlCPCREFERELOGTINVOCclayHyperlink();
                        cpcreftrackamd4.setUrl(Hyperreferel12);

                        // ExtendedHyperlinkControlWrapper cpcreftrackamd8 =
                        // getPane().getCtlCPCREFERELIMPGUACANclayHyperlink();
                        // cpcreftrackamd8.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper cpcreftrackamd9 = getPane().getCtlCPCREFERELIGTISSclayHyperlink();
                        cpcreftrackamd9.setUrl(Hyperreferel12);
                  } catch (Exception ees) {
                        // Loggers.general().info(LOG,"REFERE TRACKING REFERE CBS--------->" +
                        // ees.getMessage());
                  }
                  // todo on sep 14
                  if (!step_csm.equalsIgnoreCase("Final print") && !eventStatus.equalsIgnoreCase("Completed")) {

                        if (getMajorCode().equalsIgnoreCase("EGT") && getMinorCode().equalsIgnoreCase("VEG")) {

                              try {

                                    String lob1 = getDriverWrapper().getEventFieldAsText("BEN", "p", "cBAV").trim();
                                    String lob2 = getDriverWrapper().getEventFieldAsText("BEN", "p", "cAGQ").trim();
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG, "LOB code 1 ===>" + lob1);
                                          Loggers.general().info(LOG, "LOB code 2 ===>" + lob2);
                                    }

                                    if (lob1 != null && !lob1.equalsIgnoreCase("") && lob1.length() > 0) {
                                          getPane().setLOB(lob1);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG, "LOB code populated from TI+--->" + getPane().getLOB());
                                          }
                                    } else if (lob2 != null && !lob2.equalsIgnoreCase("") && lob2.length() > 0) {
                                          getPane().setLOB(lob2);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG, "LOB code populated from BCIF--->" + getPane().getLOB());
                                          }
                                    }

                              } catch (Exception e) {
                                    Loggers.general().info(LOG, "Exception for LOB code population in EGT===>" + e.getMessage());

                              }

                        }
                  }

                  if (!step_csm.equalsIgnoreCase("Final print") && !eventStatus.equalsIgnoreCase("Completed")) {
                        try {
                              getPerdChgEndDate();
                              // getStatutoryChargeGur();
                        } catch (Exception e) {
                              Loggers.general().info(LOG, "Exception in getStatutoryChargeGur" + e.getMessage());
                        }
                  }

                  String masterRefNumber = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                  String productcode = getDriverWrapper().getEventFieldAsText("PCO", "s", "").trim(); // ILC
                  String eventCode = getDriverWrapper().getEventFieldAsText("EVR", "r", "").trim(); // ISS001
                  String subproductCode = getDriverWrapper().getEventFieldAsText("PTP", "s", "").trim();// ILF
                  String MasterReference = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim(); //
                  String evnt = getDriverWrapper().getEventFieldAsText("EPF", "s", "");
                  String evvcount = getDriverWrapper().getEventFieldAsText("ESQ", "i", "");

                  // workflow error configuration
                  try {
                        int count3 = 0;

                        if (step_csm.equalsIgnoreCase("CSM") || step_csm.equalsIgnoreCase("AdhocCSM")) {
                              String query_wrk = "select count(*) from ETT_WF_CHKLST_TRACKING where MASTER_REF='"
                                          + MasterReference + "' and EVENTREF='" + eventCode + "' and PROD_CODE='" + productcode
                                          + "' and SUB_PRODUCT_CODE='" + subproductCode + "' and INIT_AT='CSM'";
                              con = getConnection();
                              ps1 = con.prepareStatement(query_wrk);
                              rs1 = ps1.executeQuery();
                              while (rs1.next()) {
                                    count3 = rs1.getInt(1);
                                    // //Loggers.general().info(LOG,"ETT_WF_CHKLST_TRACKING in while
                                    // loop----> " + count3);

                              }
                        } else if (step_csm.equalsIgnoreCase("CBS Maker")) {
                              String query_wrk = "select count(*) from ETT_WF_CHKLST_TRACKING where MASTER_REF='"
                                          + MasterReference + "' and EVENTREF='" + eventCode + "' and PROD_CODE='" + productcode
                                          + "' and SUB_PRODUCT_CODE='" + subproductCode + "' and INIT_AT='CBS Maker'";
                              con = getConnection();
                              ps1 = con.prepareStatement(query_wrk);
                              rs1 = ps1.executeQuery();
                              while (rs1.next()) {
                                    count3 = rs1.getInt(1);
                                    // //Loggers.general().info(LOG,"ETT_WF_CHKLST_TRACKING in while
                                    // loop----> " + count3);

                              }
                        }
                        if (count3 < 1 && step_Input.equalsIgnoreCase("i")
                                    && (step_csm.equalsIgnoreCase("CBS Maker") || step_csm.equalsIgnoreCase("CSM")
                                                || step_csm.equalsIgnoreCase("AdhocCSM"))
                                    && (getMinorCode().equalsIgnoreCase("IGTD") || getMinorCode().equalsIgnoreCase("IIG")
                                                || getMinorCode().equalsIgnoreCase("LIG") || getMinorCode().equalsIgnoreCase("NAIG")
                                                || getMinorCode().equalsIgnoreCase("NJIG") || getMinorCode().equalsIgnoreCase("NIG")
                                                || getMinorCode().equalsIgnoreCase("OIG") || getMinorCode().equalsIgnoreCase("VEG"))) {

                              validationDetails.addError(ErrorType.Other, "Workflow checklist is mandatory  [CM]");
                        }

                        else {
                              // Loggers.general().info(LOG,"ETT_WF_CHKLST_TRACKING in else
                              // loop----> " + count);
                        }

                  } catch (Exception e1) {
                        // Loggers.general().info(LOG,"Exception ETT_WF_CHKLST_TRACKING" +
                        // e1.getMessage());
                  }

                  finally {
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

                  if (step_csm.equalsIgnoreCase("CBS Authoriser") || step_csm.equalsIgnoreCase("CBS Maker 1")) {
                        try {
                              con = getConnection();
                              String query = "select wm.MESSAGE from master mas, BASEEVENT bev, warnmsg wm where mas.key97=BEV.MASTER_KEY and bev.key97=wm.associ30 and WM.PARTCODE = 'MIXFX' and MAS.MASTER_REF = '"
                                          + MasterReference + "' and BEV.REFNO_PFIX= '" + evnt + "' and BEV.REFNO_SERL = '" + evvcount
                                          + "'";
                              // Loggers.general().info(LOG,"FX deal message in authorization " +
                              // query);
                              String fxDeal = "";
                              ps1 = con.prepareStatement(query);
                              rs1 = ps1.executeQuery();
                              while (rs1.next()) {
                                    // //Loggers.general().info(LOG,"Entered while");
                                    String fxAmount = rs1.getString(1);
                                    // Loggers.general().info(LOG,"FX deal message===>" + fxAmount);
                                    fxDeal = fxAmount.substring(4, 6);
                                    // Loggers.general().info(LOG,"FX deal message converted===>" +
                                    // fxDeal);
                              }

                              if (fxDeal.equalsIgnoreCase("FX") && (step_csm.equalsIgnoreCase("CBS Authoriser")
                                          || step_csm.equalsIgnoreCase("CBS Maker 1"))) {
                                    // Loggers.general().info(LOG,"warning message in authorization
                                    // if
                                    // loop" + count3 + " and step_Id" + step_csm);
                                    validationDetails.addError(ErrorType.Other, "FX deal amount more than transaction amount [CM]");
                              }

                              else {
                                    // Loggers.general().info(LOG,"warning message in authorization
                                    // else" + count3 + " and step_Id" + step_csm);
                              }

                        } catch (Exception e1) {
                              // Loggers.general().info(LOG,"Exception" + e1.getMessage());
                        }

                        finally {
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
                  }
//CR 131 starts
                  if (((getMajorCode().equalsIgnoreCase("IGT")
                              && (getMinorCode().equalsIgnoreCase("IIG") || getMinorCode().equalsIgnoreCase("NAIG")))
                              || (getMinorCode().equalsIgnoreCase("NJIG")))
                              && (step_csm.equalsIgnoreCase("CBS Maker") || step_csm.equalsIgnoreCase("CBS Maker 1")
                                          || step_csm.equalsIgnoreCase("CBS Authoriser")
                                          || step_csm.equalsIgnoreCase("CBS Reject"))) {

                        String guranteeRefNo = "";
                        String availableAmount = "";
                        String guranteeAmount = "";
                        String counter_gurantee = getDriverWrapper().getEventFieldAsText("CGTC", "s", "");
                        Loggers.general().info(LOG, "counter_gurantee===>" + counter_gurantee);

                        if (counter_gurantee.equalsIgnoreCase("C") || counter_gurantee.equalsIgnoreCase("T")) {

                              List<ExtEventCounterGurantee> counterGuaranteeDetails = (List<ExtEventCounterGurantee>) getWrapper()
                                          .getExtEventCounterGurantee();
                              if (counterGuaranteeDetails.size() < 1 && (step_Input.equalsIgnoreCase("i"))) {
                                    Loggers.general().info(LOG, "Inside if not EMpty");
                                    validationDetails.addError(ErrorType.Other,
                                                "Counter Guarantee is received.Please enter Counter Details(GuaranteeRefNo/AvailableAmount/GuaranteeAmount)[CM]");

                              }
                        }
                  }
//CR 131 ends                 
                  if (getMajorCode().equalsIgnoreCase("IGT")) {
                        getPeriodicAdv();
                  }
                  if (getMajorCode().equalsIgnoreCase("IGT")) {
                        try {
                              getPane().getExtEventLienMarkingDelete().setEnabled(false);
                              getFCCTCALCULATION();
                              getFcctDetails(validationDetails);

                        } catch (Exception e) {
                              // Loggers.general().info(LOG,"Exception update" +
                              // e.getMessage());
                              e.printStackTrace();
                        }
                        // Notes Populated in Summary
                  }
                  try {
                        con = getConnection();
                        String query = "select * from master mas,NOTE, TIDATAITEM tid WHERE mas.KEY97 = tid.MASTER_KEY AND tid.KEY97 = NOTE.KEY97 AND (NOTE.TYPE NOT IN (3,129,1089) or NOTE.TYPE is null) AND note_event IS NOT NULL AND NOTE.ACTIVE = 'Y' AND mas.MASTER_REF = '"
                                    + MasterReference + "' ";

                        ps1 = con.prepareStatement(query);
                        rs1 = ps1.executeQuery();
                        while (rs1.next()) {
                              String notes = rs1.getString(1);
                              if (notes.length() > 0 && (step_Input.equalsIgnoreCase("i"))
                                          && (step_csm.equalsIgnoreCase("CSM") || step_csm.equalsIgnoreCase("CBS Maker"))) {
                                    validationDetails.addWarning(WarningType.Other,
                                                "Notes Populated in Summary. Kindly check [CM]");
                              }

                        }

                  } catch (Exception e) {
                        Loggers.general().info(LOG, "Exception Notes Populated in Summary" + e.getMessage());
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

                  // previous buyer credit reference no
                  String prebuyer = getWrapper().getPREBUYRE().trim();
                  String rollbuyer = getWrapper().getROLLBUCR().trim();

                  if ((!prebuyer.trim().equalsIgnoreCase("")) && rollbuyer.trim().equalsIgnoreCase("Yes")
                              && (getMinorCode().equalsIgnoreCase("IIG") || getMinorCode().equalsIgnoreCase("NAIG"))
                              && (step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker"))) {
                        // //Loggers.general().info(LOG, "Previous ref no and rool over is not
                        // yes
                        // if loop---->" + prebuyer + "and" + rollbuyer);
                        String qr1 = "SELECT a.master_ref FROM master A, baseevent B WHERE A.key97 =B.master_key AND a.refno_pfix IN('IGT') AND b.key97 in (SELECT d.key97 FROM extevent C,BASEEVENT d WHERE d.key97=C.event AND PREBUYRE ='"
                                    + prebuyer + "' AND ROLLBUCR ='Yes' and d.key97 = C.EVENT AND d.status ='c' )";
                        // //Loggers.general().info(LOG,"query for Previous ref no----> " +
                        // qr1);
                        try {

                              con = ConnectionMaster.getConnection();
                              ps1 = con.prepareStatement(qr1);
                              rs1 = ps1.executeQuery();
                              while (rs1.next()) {
                                    // //Loggers.general().info(LOG,"while of count for Previous ref
                                    // no----> ");
                                    String mas_re = rs1.getString(1);
                                    // //Loggers.general().info(LOG,"Result value already Previous
                                    // ref
                                    // no----> " + mas_re);

                                    validationDetails.addWarning(WarningType.Other, "Buyer's credit has been rolled over (" + mas_re
                                                + "). Please check the reference [CM]");

                              }
                              // Loggers.general().info(LOG,"Previous ref no in out of
                              // loop---->");

                        }

                        catch (Exception e) {
                              // Loggers.general().info(LOG,"Exception already remitance out" +
                              // e.getMessage());
                        }

                        finally {
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
                        // Loggers.general().info(LOG,"Previous ref no and rool over is not yes
                        // else loop---->" + prebuyer + "and" + rollbuyer);

                  }

                  // Effective date population
                  String systemDate = getDriverWrapper().getEventFieldAsText("TDY", "d", "");
                  String effct = getWrapper().getEFFDATE();
                  // //Loggers.general().info(LOG,"effct date --->" + effct);
                  if (effct == null) {

                        try {
                              SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yy");
                              Calendar cal = Calendar.getInstance();
                              int val = 0;
                              cal.setTime(sdf1.parse(systemDate));
                              // //Loggers.general().info(LOG,"effct in issue-------> ");
                              cal.add(Calendar.DATE, val);
                              String output = sdf1.format(cal.getTime());
                              // //Loggers.general().info(LOG,"output----->" + output);
                              getPane().setEFFDATE(output);

                        } catch (Exception e) {
                              // Loggers.general().info(LOG,"EFFECTIVE date --->" +
                              // e.getMessage());
                        }
                  } else {
                        String effctive = getWrapper().getEFFDATE();
                        // //Loggers.general().info(LOG,"effctive date in else --->" +
                        // effctive);
                        try {
                              SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yy");
                              Calendar cal = Calendar.getInstance();
                              int val = 0;
                              cal.setTime(sdf1.parse(effctive));
                              // //Loggers.general().info(LOG,"effct in issue-------> ");
                              cal.add(Calendar.DATE, val);
                              String output = sdf1.format(cal.getTime());
                              // //Loggers.general().info(LOG,"output in else----->" + output);
                              getPane().setEFFDATE(output);

                        } catch (Exception e) {
                              // Loggers.general().info(LOG,"EFFECTIVE date in else--->" +
                              // e.getMessage());
                        }
                  }

                  // ---------------------
                  String buyerREF = getWrapper().getBUYBILLR().trim();
                  int intcount = 0;
                  // //Loggers.general().info(LOG,"buyer reference no---> " + buyerREF);

                  try {
                        con = ConnectionMaster.getConnection();
                        String query = "select COUNT(*) from EXTEVENT where BUYBILLR ='" + buyerREF + "'";
                        // //Loggers.general().info(LOG,"buyer reference no value-----> " +
                        // query);

                        ps1 = con.prepareStatement(query);
                        rs1 = ps1.executeQuery();
                        while (rs1.next()) {
                              intcount = rs1.getInt(1);
                              // //Loggers.general().info(LOG,"value of count in while " +
                              // intcount);

                        }

                        if (intcount > 0 && (step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker"))) {

                              validationDetails.addWarning(ValidationDetails.WarningType.Other,
                                          "This bill(" + buyerREF + ") is already utilized under other Buyers Credit ref [CM]");
                        }

                  } catch (Exception e1) {
                        // Loggers.general().info(LOG,"Exception" + e1.getMessage());
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

                  // INTEREST AMOUNT CALCULATION
                  try {
                        String subproCode = getDriverWrapper().getEventFieldAsText("PTP", "s", "");
                        if (subproCode.equalsIgnoreCase("BCR")
                                    && (!step_csm.equalsIgnoreCase("Final print") && !eventStatus.equalsIgnoreCase("Completed"))) {

                              totalLiaAmount();

                        } else {
                              // Loggers.general().info(LOG,"Product type is not a buyer credit");
                        }
                  } catch (Exception e) {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG, "Exception in Total liability amount---> " + e.getMessage());
                        }
                  }

                  String portval = getWrapper().getPORTCOD_Name().trim();
                  // Loggers.general().info(LOG,"Port Value---->" + portval);
                  if ((!portval.equalsIgnoreCase("")) && portval != null) {
                        try {
                              // //Loggers.general().info(LOG,"hscode Value in try---->" +
                              // hscodeval);
                              String hyperValue = "SELECT trim(PNAME),trim(COUNTRY) FROM EXTPORTCO WHERE PCODE='" + portval + "'";
                              // //Loggers.general().info(LOG,"port code query Value---->" +
                              // hyperValue);
                              con = ConnectionMaster.getConnection();
                              ps1 = con.prepareStatement(hyperValue);
                              rs = ps1.executeQuery();
                              while (rs.next()) {
                                    String hsploy = rs.getString(1);
                                    String poname = rs.getString(2);
                                    // //Loggers.general().info(LOG,"port code description---->" +
                                    // hsploy);
                                    getPane().setPORTDESC(hsploy);
                                    getPane().setPORTDECO(poname);
                              }

                              // con.close();
                              // ps1.close();
                              // rs.close();
                        } catch (Exception e) {
                              // Loggers.general().info(LOG,"exception is " + e);
                        } finally {
                              try {
                                    if (rs != null)
                                          rs.close();
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
                        // Loggers.general().info(LOG,"port code is empty");
                  }

                  try {
                        Loggers.general().info(LOG, "Entered inside LIEN");

                        String subprod = getDriverWrapper().getEventFieldAsText("PTP", "s", "");
                        Loggers.general().info(LOG, "Sub prod type of bg" + subprod);
                        String margin = getWrapper().getMARAMT();

                        if (margin == null) {
                              margin = "0";
                        }

                        Loggers.general().info(LOG, "Current margin amount " + margin);
                        double marginB = 0.0;
                        double marDob = Double.valueOf(margin);
                        // calculateMargin();
                        List<ExtEventLienMarking> ExtEventLienMark = (List<ExtEventLienMarking>) getWrapper()
                                    .getExtEventLienMarking();
                        if (marDob > 0 && ExtEventLienMark.size() == 0 && (step_Input.equalsIgnoreCase("i"))
                                    && (step_csm.equalsIgnoreCase("CBS Maker"))
                                    && (getMinorCode().equalsIgnoreCase("IIG") || getMinorCode().equalsIgnoreCase("NAIG")
                                                || getMinorCode().equalsIgnoreCase("NJIG"))) {
                              Loggers.general().info(LOG,
                                          "ExtEventLienMark IGT--> " + ExtEventLienMark + " & marginAmt IGT ---> " + marDob);
                              validationDetails.addWarning(WarningType.Other,
                                          "Lien amount is calculated and no Lien Amount is entered in FD lien grid  [CM]");
                        } else {
                              Loggers.general().info(LOG, " FD lien grid is entered----->");

                        }

                        // //Loggers.general().info(LOG,"ExtEventLienMark size---> " +
                        // ExtEventLienMark);
                        double totalMargin = 0.0;
                        String dopNo = "";
                        int counterVal = 0;
                        ArrayList<Double> arr = new ArrayList<Double>();
                        for (int l = 0; l < ExtEventLienMark.size(); l++) {
                              ExtEventLienMarking eventLien = (ExtEventLienMarking) ExtEventLienMark.get(l);
                              // //Loggers.general().info(LOG,"eventLien ---> " + eventLien);
                              // BigDecimal clearBal = eventLien.getCLEARBLC();
                              // //Loggers.general().info(LOG,"clearBal ---> " + clearBal);
                              BigDecimal marginAmt = eventLien.getMARGAMT();
                              // //Loggers.general().info(LOG," marginAmt - " + marginAmt);
                              String liensta = eventLien.getLINEST();
                              dopNo = eventLien.getDEPOSTNO().trim();
                              // //Loggers.general().info(LOG,"Lien status for mark----> " +
                              // liensta);
                              if (ExtEventLienMark.size() > 0 && (liensta.equalsIgnoreCase("") || liensta == null)
                                          && (step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker"))) {
                                    validationDetails.addError(ErrorType.Other,
                                                "Lien amount is available please Mark the lien [CM]");
                              }

                              double marginDob = eventLien.getMARGAMT().doubleValue();
                              if ((ExtEventLienMark.size() > 0) && (marginDob == 0.0 || marginDob == 0 || marginDob < 1)
                                          && (step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker"))) {
                                    validationDetails.addError(ErrorType.Other, "Lien amount should be greater than Zero [CM]");
                                    EventPane pane = (EventPane) getPane();
                                    pane.getExtEventLienMarkingUpdate().setEnabled(true);
                              }
                              Loggers.general().info(LOG, "marginAmt----" + marginAmt);
                              if (marginAmt != null)
                                    marginB = Double.valueOf(marginAmt.doubleValue() / 100);
                              Loggers.general().info(LOG, "marginB----" + marginB);

                              totalMargin += marginB;
                              Loggers.general().info(LOG, "totalMargin----" + totalMargin);
                              Loggers.general().info(LOG, "marDob" + marDob);
                              Loggers.general().info(LOG, "SIZE of lien===" + ExtEventLienMark.size());
                              if (ExtEventLienMark.size() > 0 && totalMargin > marDob && (step_Input.equalsIgnoreCase("i"))
                                          && (step_csm.equalsIgnoreCase("CBS Maker"))) {
                                    Loggers.general().info(LOG, "totalMargin -----> " + totalMargin);
                                    validationDetails.addWarning(WarningType.Other,
                                                "Grid Margin amount should not greater then the Margin Amount  [CM]");
                              } else {
                                    Loggers.general().info(LOG, "totalMargin is less then the margin amount ");
                              }

                              // todo for jira 7045

                              Loggers.general().info(LOG, "Lien status check===> " + liensta);

                              if (liensta.equalsIgnoreCase("MARK FAILED")) {
                                    Loggers.general().info(LOG, "inside lien mark failed error");
                                    validationDetails.addError(ErrorType.Other, "Lien Marking Failed[CM]");
                              } else {
                                    Loggers.general().info(LOG, "else lien mark failed error");
                              }

                              // end of 7045

                              if (liensta.equalsIgnoreCase("MARK SUCCEEDED")) {
                                    arr.add(marginB);
                                    // Loggers.general().info(LOG,"lien status array list arr---->"
                                    // + arr);

                              } else {
                                    // Loggers.general().info(LOG,"lien status in else loop---->" +
                                    // liensta);
                              }
                        }
                        if (step_csm.equalsIgnoreCase("CBS Maker 1") && ExtEventLienMark.size() > 0) {
                              try {

                                    String query = "SELECT count(*),lmg.DEPOSTNO,lmg.LINEST FROM MASTER mas, BASEEVENT bas, EXTEVENTLMG lmg WHERE mas.KEY97 =bas.MASTER_KEY AND bas.EXTFIELD =lmg.FK_EVENT AND lmg.DEPOSTNO='"
                                                + dopNo + "' AND lmg.LINEST ='MARK SUCCEEDED' AND mas.MASTER_REF ='" + MasterReference
                                                + "' AND bas.REFNO_PFIX ='" + evnt + "' AND bas.REFNO_SERL=" + evvcount
                                                + " group by lmg.DEPOSTNO,lmg.LINEST";
                                    Loggers.general().info(LOG, "lien status check---->" + query);
                                    con = getConnection();
                                    ps1 = con.prepareStatement(query);
                                    rs1 = ps1.executeQuery();
                                    while (rs1.next()) {
                                          counterVal = rs1.getInt(1);
                                          // Loggers.general().info(LOG,"lien counterVal in
                                          // while---->" + counterVal);

                                    }
                              } catch (Exception e) {
                                    Loggers.general().info(LOG, "Lien account===> " + e.getMessage());
                              } finally {
                                    try {
                                          if (rs1 != null)
                                                rs1.close();
                                          if (ps1 != null)
                                                ps1.close();
                                          if (con != null)
                                                con.close();
                                    } catch (SQLException e) {
                                          Loggers.general().info(LOG, "Connection Failed! Check output console");
                                          e.printStackTrace();
                                    }
                              }

                              if (ExtEventLienMark.size() > 0 && counterVal > 1 && (step_csm.equalsIgnoreCase("CBS Maker 1"))) {

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG,
                                                      "Multiple liens cannot be marked for same account number---->" + dopNo);
                                    }

                                    validationDetails.addError(ErrorType.Other,
                                                "Multiple liens cannot be marked for same account number(" + dopNo
                                                            + "). Reverse lien for account number and mark new lien [CM]");
                              }

                              else if (step_csm.equalsIgnoreCase("CBS Maker 1") && ExtEventLienMark.size() > 0) {
                                    try {

                                          String headerReq = "<?xml version=$1.0$ encoding=$UTF-8$?> <ServiceRequest xmlns=$urn:control.services.tiplus2.misys.com$ xmlns:ns2=$urn:messages.service.ti.apps.tiplus2.misys.com$ xmlns:ns3=$urn:common.service.ti.apps.tiplus2.misys.com$ xmlns:ns4=$urn:custom.service.ti.apps.tiplus2.misys.com$> <RequestHeader> <Service>Customization</Service> <Operation>FDLienEnquiry</Operation> <Credentials> <Name>SUPERVISOR</Name> </Credentials> <ReplyFormat>FULL</ReplyFormat> <TargetSystem>KOTAKEXT</TargetSystem> <SourceSystem>ZONE1</SourceSystem> <NoRepair>Y</NoRepair> <NoOverride>Y</NoOverride> <CorrelationId>Correlation_Id</CorrelationId> <TransactionControl>NONE</TransactionControl> </RequestHeader> <fdlienenquiry xmlns:xsi=$http://www.w3.org/2001/XMLSchema-instance$>";
                                          String endXml = "</fdlienenquiry></ServiceRequest>";
                                          String gridXml = "";
                                          String startDate = "";
                                          String endDate = "";
                                          List<ExtEventLienMarking> ExtEventLien = (List<ExtEventLienMarking>) getWrapper()
                                                      .getExtEventLienMarking();
                                          for (ExtEventLienMarking fdwrapper : ExtEventLien) {
                                                BigDecimal hundred = new BigDecimal(100);
                                                BigDecimal margin_Amt = fdwrapper.getMARGAMT();
                                                BigDecimal margin_Amount = margin_Amt.divide(hundred);
                                                DecimalFormat diff = new DecimalFormat("0.00");
                                                diff.setMaximumFractionDigits(2);
                                                String marginAmount = diff.format(margin_Amount);
                                                String marginAcc = fdwrapper.getDEPOSTNO().trim();
                                                String LienID = fdwrapper.getLIENID().trim();
                                                String masRefNo = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                                                String eventRefNo = getDriverWrapper().getEventFieldAsText("MEVR", "r", "");
                                                String issueDate = getDriverWrapper().getEventFieldAsText("ISS", "d", "");
                                                String mas_evnt = masRefNo;

                                                String behalfBranch = getDriverWrapper().getEventFieldAsText("BOB", "s", "");
                                                String mas_evntRef = masRefNo;
                                                String lienbox = fdwrapper.getLINEST();
                                                if (!lienbox.equalsIgnoreCase("") && lienbox != null
                                                            && lienbox.equalsIgnoreCase("MARK SUCCEEDED")) {
                                                      // Loggers.general().info(LOG,"Lien Status is
                                                      // blank");
                                                      String fdDefaultXml = "<FDRow> <MasterReference>Maseter_ref</MasterReference> <EventReference>Event_ref</EventReference> <BehalfOfBranch>behalf_Branch</BehalfOfBranch><AccountNumber>Account_Number</AccountNumber><LienId>Lien_ID</LienId><Amount>Amount_Margin</Amount> <Currency>INR</Currency> <ReasonCode>TRD</ReasonCode> <Remarks>Maseter_ref</Remarks> </FDRow>";
                                                      fdDefaultXml = fdDefaultXml.replace("Maseter_ref", masRefNo);
                                                      fdDefaultXml = fdDefaultXml.replace("Event_ref", eventRefNo);
                                                      fdDefaultXml = fdDefaultXml.replace("behalf_Branch", behalfBranch);
                                                      fdDefaultXml = fdDefaultXml.replace("Account_Number", marginAcc);
                                                      fdDefaultXml = fdDefaultXml.replace("Lien_ID", LienID);
                                                      fdDefaultXml = fdDefaultXml.replace("Amount_Margin", marginAmount);
                                                      fdDefaultXml = fdDefaultXml.replace("Maseter_ref", masRefNo);

                                                      gridXml = gridXml + fdDefaultXml;
                                                } else {
                                                      Loggers.general().info(LOG, "Lien Status in else" + lienbox);
                                                }

                                          }

                                          char j = '"';
                                          String val = Character.toString(j);
                                          headerReq = headerReq.replace("$", val);
                                          String correlationId = randomCorrelationId();
                                          // Loggers.general().info(LOG,"correlationId " +
                                          // correlationId);
                                          headerReq = headerReq.replace("Correlation_Id", correlationId);
                                          String finalXml = headerReq + gridXml + endXml;
                                          Loggers.general().info(LOG, "Themebridge finalXml for enquiry" + finalXml);

                                          ThemeTransportClient anThemeTransportClient = new ThemeTransportClient();
                                          String responseXML = anThemeTransportClient.invoke("Customization", "FDLienAdd", finalXml);
                                          Loggers.general().info(LOG,
                                                      "Final Themebridge responseXML for enquiry==========>" + responseXML);
                                          // EnigmaArray<ExtEventLienMarkingEntityWrapper>
                                          // leanDataList = getExtEventLienMarkingData();
                                          String line = responseXML;
                                          String[] sp_line = line.split(",");
                                          for (int k = 0; k < sp_line.length; k++) {
                                                String[] res_sp_line = sp_line[k].split("~");
                                                Iterator<ExtEventLienMarking> iterator1 = ExtEventLien.iterator();

                                                for (ExtEventLienMarking str : ExtEventLien) {
                                                      String depositNum = str.getDEPOSTNO().trim();

                                                      BigDecimal hundred = new BigDecimal(100);
                                                      BigDecimal margin_Amt = str.getMARGAMT();
                                                      BigDecimal margin_Amount = margin_Amt.divide(hundred);
                                                      DecimalFormat diff = new DecimalFormat("0.00");
                                                      diff.setMaximumFractionDigits(2);
                                                      String lienAmt = diff.format(margin_Amount);
                                                      Loggers.general().info(LOG, "Lien mark Amount" + lienAmt);
                                                      String currentDepositNum = res_sp_line[0].trim();
                                                      Loggers.general().info(LOG, "Lien response currentDepositNum" + currentDepositNum);
                                                      String responseValue = res_sp_line[3].trim();
                                                      BigDecimal responseAmount = new BigDecimal(responseValue);
                                                      String responseAmt = diff.format(responseAmount);

                                                      Loggers.general().info(LOG, "Lien response amount" + responseAmt);
                                                      String markVal = res_sp_line[4].trim();
                                                      String markValue = "MARK " + markVal;
                                                      Loggers.general().info(LOG, "Lien mark response Value" + markValue);
                                                      String lienbox = str.getLINEST().trim();
                                                      Loggers.general().info(LOG, "Lien mark grig Value" + lienbox);
                                                      // String lienStr = str.getMARGAMT();
                                                      // double lienAmt = Double.valueOf(lienStr);

                                                      if (depositNum.equalsIgnoreCase(currentDepositNum)
                                                                  && markValue.equalsIgnoreCase("MARK SUCCEEDED")
                                                                  && lienbox.equalsIgnoreCase("MARK SUCCEEDED")) {
                                                            if (!lienAmt.equalsIgnoreCase(responseAmt)) {
                                                                  validationDetails.addError(ErrorType.Other,
                                                                              "For Account no (" + depositNum + "), lien is marked for amount ("
                                                                                          + responseAmt
                                                                                          + ") in finacle. Kindly update the lien amount");
                                                            }

                                                            else {

                                                                  Loggers.general().info(LOG, "lien and response amount is same else"
                                                                              + lienAmt + "responseAmt" + responseAmt);
                                                            }
                                                      } else {
                                                            Loggers.general().info(LOG, "lien depositNum else" + depositNum
                                                                        + "currentDepositNum===>" + currentDepositNum);
                                                      }

                                                }

                                          }

                                          // }
                                    } catch (Exception e) {
                                          Loggers.general().info(LOG, "ThemeTransportClient Exception response " + e.getMessage());
                                    }
                              } else {

                              }

                        }
                        // validation mark succeed
                        double dob1 = 0;
                        for (Double dob : arr) {

                              dob1 = dob1 + dob;
                              Loggers.general().info(LOG, "marksucceed=======>" + dob);
                        }

                        Loggers.general().info(LOG, "marksucceed=======>" + dob1);
                        Loggers.general().info(LOG, "array size" + arr.size());
                        Loggers.general().info(LOG, "New BCR added invocation====");

                        String finalpayment = getDriverWrapper().getEventFieldAsText("FNP", "l", "").trim();
                        Loggers.general().info(LOG, "Final payment==> " + finalpayment);

                        String availbleamt = getDriverWrapper().getEventFieldAsText("AMPR", "v", "m");
                        Loggers.general().info(LOG, "Available amount====> " + availbleamt);

                        String maximumamt = getDriverWrapper().getEventFieldAsText("MAL", "v", "m").trim();
                        Loggers.general().info(LOG, "Presented amount after trim====> " + maximumamt);

                        if (arr.size() > 0 && (step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker"))
                                    && (getMinorCode().equalsIgnoreCase("OIG")
                                                || ((getMinorCode().equalsIgnoreCase("LIG") && subprod.equalsIgnoreCase("BCR"))))
                                    && ((finalpayment.equalsIgnoreCase("Y") || (maximumamt.equalsIgnoreCase("0.00"))))) {
                              Loggers.general().info(LOG, "Marginamt_log for -----> " + dob1);
                              validationDetails.addWarning(WarningType.Other,
                                          "Total Lien Mark amount is (" + dob1 + " INR), Please Release the Lien [CM]");
                        } else {
                              Loggers.general().info(LOG, "Lien mark amount in else" + dob1);
                        }
                        Loggers.general().info(LOG, "totalMargin - " + totalMargin);
                        String expectedMarginStr = getDriverWrapper().getEventFieldAsText("cAAR", "v", "m").trim();
                        Loggers.general().info(LOG, "expectedMarginStr" + expectedMarginStr);

                        double expectedMarginAmt = 0.0;
                        Loggers.general().info(LOG, "expectedMarginAmt before -------- " + expectedMarginAmt);
                        Loggers.general().info(LOG, "expectedMarginStr.length() " + expectedMarginStr.length());
                        if (expectedMarginStr.length() != 0) {
                              expectedMarginAmt = Double.parseDouble(expectedMarginStr);
                              Loggers.general().info(LOG, "expectedMarginAmt - " + expectedMarginAmt);
                        }
                        Loggers.general().info(LOG, "expectedMarginAmt -------- " + expectedMarginAmt);
                        Loggers.general().info(LOG, "Total margin" + totalMargin);
                        Loggers.general().info(LOG, "ExtEventLienMark.size() " + ExtEventLienMark.size());
                        if ((ExtEventLienMark.size() > 0) && (totalMargin != expectedMarginAmt)
                                    && (step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker"))) {
                              validationDetails.addWarning(WarningType.Other,
                                          "Sum of margin amount should be equal to Required Lien Amount  [CM]");
                        }

                  } catch (Exception e) {
                        Loggers.general().info(LOG, "Exception occured in margin validation " + e.getMessage());
                  }
            
                  if (getMajorCode().equalsIgnoreCase("IGT")) {

                        getPeriodicAdv();

                        String facid = getWrapper().getFACLTYID().trim();

                        if (facid == null || facid.equalsIgnoreCase("")) {
                              getPane().setPMARGIN("");
                              getPane().setMARAMT("");
                        }

                  }

                  // try
                  //
                  // {
                  // getclaimDateCal();
                  // } catch (Exception e) {
                  // if (dailyval_Log.equalsIgnoreCase("YES")) {
                  // Loggers.general().info(LOG,"IGT claim and new claim expiry
                  // connection---->" + e.getMessage());
                  // }
                  // }
                  // Claim date calculation
                  String product = getDriverWrapper().getEventFieldAsText("PCO", "s", "").trim();
                  // //Loggers.general().info(LOG,"product code--->" + product);
                  String event = getDriverWrapper().getEventFieldAsText("EVCD", "s", "").trim();
                  // //Loggers.general().info(LOG,"Event code--->" + event);
                  String expdate = getDriverWrapper().getEventFieldAsText("EXP", "d", ""); // 12/10/16
                  String provisional = getDriverWrapper().getEventFieldAsText("EPRV", "l", "");
                  System.out.println("provisional--->" + provisional);
                  SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                  Calendar c = Calendar.getInstance();
                  try {
                        // CR 146 Starts
                        if ((getMajorCode().equalsIgnoreCase("IGT")) && (getMinorCode().equalsIgnoreCase("IIG"))) {
                              this.getPane().getCtlNEWCLMDT().getControl().setVisible(false);
                              this.getPane().getCtlPROVIN().getControl().setVisible(false);

                              if (provisional.equalsIgnoreCase("Y")) {
                                    getPane().setPROVIN(true);
                              }
                              if (provisional.equalsIgnoreCase("N")) {
                                    getPane().setPROVIN(false);
                              }
                        }
                        // CR 146 Ends
                        // //Loggers.general().info(LOG,"Expiry date" + expdate);
                        String graceda = "0";
                        graceda = getWrapper().getGRACEPER();

                        int gra = 0;
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG, "claim day initial ----> " + graceda);
                        }

                        if (((!graceda.equalsIgnoreCase("")) || graceda != null) && graceda.length() > 0) {
                              // //Loggers.general().info(LOG,"claimdate is blank");
                              try {
                                    if (graceda.length() > 0 && getMinorCode().equalsIgnoreCase("IIG")
                                                && product.equalsIgnoreCase("IGT")) {
                                          gra = Integer.parseInt(graceda);
                                          c.setTime(sdf.parse(expdate));
                                          // //Loggers.general().info(LOG,"expdate in issue------->
                                          // ");
                                          c.add(Calendar.DATE, gra);
                                          // //Loggers.general().info(LOG,"DATE 1"+ c);
                                          String output = sdf.format(c.getTime());
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG, "Claim expiry date----> " + output);
                                          }
                                          getPane().setCLIMEXPD(output);
                                          // CR 146 starts
                                          getPane().setNEWCLMDT(output);
                                          // CR 146 Ends
                                    } else {
                                          // //Loggers.general().info(LOG,"New claim expiry date enter
                                          // ------->
                                          // ");
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG, "claim day amend ----> " + graceda);
                                          }
                                          if (graceda.length() > 0 && product.equalsIgnoreCase("IGT") && expdate.length() > 0) {
                                                gra = Integer.parseInt(graceda);
                                                c.setTime(sdf.parse(expdate));
                                                // //Loggers.general().info(LOG,"expdate in adjust and
                                                // amend-------> ");
                                                c.add(Calendar.DATE, gra);
                                                // //Loggers.general().info(LOG,"DATE 1"+ c);
                                                String output = sdf.format(c.getTime());
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG, "New Claim expiry date----> " + output);
                                                }
                                                getPane().setNEWCLMDT(output);
                                                getWrapper().setNEWCLMDT(output);
                                          } else {
                                                // Loggers.general().info(LOG,"else part of grace
                                                // days");
                                          }
                                    }
                              } catch (Exception e) {
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG, "IGT claim and new claim expiry date---->" + e.getMessage());
                                    }
                              }

                        } else {

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG, "claim day is blank ----> " + graceda);
                              }
                              try {
                                    if (expdate.length() > 0) {
                                          c.setTime(sdf.parse(expdate));

                                          c.add(Calendar.DATE, gra);
                                          // //Loggers.general().info(LOG,"DATE 1"+ c);
                                          String output = sdf.format(c.getTime());
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG, "claim expiry days and claim day is blank ----> " + output);
                                          }
                                          getPane().setCLIMEXPD(output);
                                          getWrapper().setCLIMEXPD(output);
                                    } else {
                                          // Loggers.general().info(LOG,"expdate is blank" + expdate);
                                    }
                              } catch (ParseException e) {
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG, "IGT grace days---->" + e.getMessage());
                                    }
                              }

                        }
                  } catch (Exception e) {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG, "IGT grace days---->" + e.getMessage());
                        }
                  }

                  String custval = "";
                  String cust = "";
                  String masterref = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim();
                  String eventREF = getDriverWrapper().getEventFieldAsText("MEVR", "r", "").trim();
                  String acctno = getDriverWrapper().getEventFieldAsText("CHA", "a", "").trim();
                  // Loggers.general().info(LOG,"charge account in event field======>" +
                  // acctno);
                  String chargecol = getDriverWrapper().getEventFieldAsText("BOTC", "l", "");
                  // Loggers.general().info(LOG,"charge account collected " + chargecol);
                  if (product.trim().equalsIgnoreCase("EGT")) {
                        cust = getDriverWrapper().getEventFieldAsText("BEN", "p", "no").trim();

                  } else if (product.trim().equalsIgnoreCase("IGT")) {
                        // //Loggers.general().info(LOG,"Major Code" + getMajorCode());
                        cust = getDriverWrapper().getEventFieldAsText("APP", "p", "no").trim();

                  } else {
                        // Loggers.general().info(LOG,"Primary customer taking else----> " +
                        // cust);
                  }
                  // Loggers.general().info(LOG,"Primary customer taking ----> " + cust);

                  try {
                        if (step_csm.equalsIgnoreCase("CBS Maker") || step_csm.equalsIgnoreCase("CBS Maker 1")) {

                              con = getConnection();

                              String dmstlmt = "select TRIM(CUS_MNM) from ETT_CUS_ACCT_SETTLE where MASTER_REF ='" + masterref
                                          + "' and EVENT_REF = '" + eventREF + "'";
                              dmsp = con.prepareStatement(dmstlmt);

                              dmsr = dmsp.executeQuery();
                              while (dmsr.next()) {

                                    custval = dmsr.getString(1);

                                    if (custval.length() > 0 && chargecol.equalsIgnoreCase("Y")
                                                && (!chargecol.equalsIgnoreCase("N")) && step_csm.equalsIgnoreCase("CBS Maker 1")) {

                                          // Loggers.general().info(LOG,"custoemr number in query" +
                                          // custval);
                                          // Loggers.general().info(LOG,"custoemr number in
                                          // transaction" + cust);
                                          validationDetails.addWarning(WarningType.Other,
                                                      "Account selected in Settlement for charges does not belong to the Applicant [CM]");
                                    } else {
                                          // Loggers.general().info(LOG,"charge account collected
                                          // matched");
                                    }

                              }

                              String dms = "select trim(acc.BO_ACCTNO) from ACCOUNT acc where acc.CUS_MNM ='" + cust
                                          + "' and acc.BO_ACCTNO='" + acctno + "'";
                              // Loggers.general().info(LOG,"ETT_CUS_ACCT_SETTLE QUERY====> " +
                              // dms);
                              dmsp = con.prepareStatement(dms);

                              dmsr = dmsp.executeQuery();
                              if (dmsr.next()) {
                                    // Loggers.general().info(LOG,"custoemr number in query if
                                    // loop----> " + dmsr.getString(1));
                                    custval = dmsr.getString(1);

                                    // Loggers.general().info(LOG,"custoemr number in transaction if
                                    // loop" + acctno);

                              }

                              else {
                                    // Loggers.general().info(LOG,"custoemr number in query else
                                    // loop" + custval);
                                    // Loggers.general().info(LOG,"custoemr number in transaction
                                    // else loop" + acctno);
                                    if (acctno.length() > 0 && chargecol.equalsIgnoreCase("Y")
                                                && (!chargecol.equalsIgnoreCase("N"))) {

                                          if ((step_csm.equalsIgnoreCase("CBS Maker") || step_csm.equalsIgnoreCase("CSM"))
                                                      && (step_Input.equalsIgnoreCase("i"))) {

                                                validationDetails.addWarning(WarningType.Other,
                                                            "Account selected in charges does not belong to the Applicant [CM]");

                                          } else {
                                                // Loggers.general().info(LOG,"Not CBS Maker for charge
                                                // account");
                                          }
                                    } else {
                                          // Loggers.general().info(LOG,"charge account collected
                                          // check box in else " + chargecol);
                                    }
                              }

                        } else {
                              // Loggers.general().info(LOG,"Not CBS Maker for charge account");
                        }

                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"charge account collected----->" +
                        // e.getMessage());
                  } finally {
                        try {
                              if (con != null)
                                    con.close();
                              if (dmsp != null)
                                    dmsp.close();
                              if (dmsr != null)
                                    dmsr.close();
                        } catch (SQLException e) {
                              // Loggers.general().info(LOG,"Connection Failed! Check output
                              // console");
                              e.printStackTrace();
                        }
                  }

                  if (getMinorCode().equalsIgnoreCase("BIG")) {
                        try {

                              // String queryNarrative = "Select
                              // mas.MASTER_REF,bev.REFNO_PFIX,
                              // bev.REFNO_SERL,pos.NARR_P1,pos.NARR_P2,pos.POSTED_AS,pos.*
                              // from master mas, BASEEVENT bev, RELITEM rel, POSTING pos
                              // where mas.KEY97 = bev.MASTER_KEY and bev.KEY97 =
                              // rel.EVENT_KEY and rel.KEY97 = pos.KEY97 and pos.POSTED_AS
                              // is not null and trim(pos.NARR_P1) is not null and
                              // trim(pos.NARR_P2) is not null and trim(mas.MASTER_REF)
                              // ='"
                              // + MasterReference + "' and bev.REFNO_PFIX ='" + event +
                              // "' and bev.REFNO_SERL='" + evvcount
                              // + "'";

                              String queryNarrative = "SELECT TRIM(POS.NARR_P1), POS.NARR_P2, MAS.MASTER_REF, BEV.REFNO_PFIX, POS.* FROM MASTER MAS , BASEEVENT BEV, RELITEM REL , POSTING POS WHERE MAS.KEY97 =BEV.MASTER_KEY AND BEV.KEY97 =REL.EVENT_KEY AND REL.KEY97 = POS.KEY97 AND bev.REFNO_PFIX ='PST' AND bev.REFNO_SERL =1 AND BEV.REFNO_PFIX ||LPAD(BEV.REFNO_SERL,3,0) =POS.EVENTREF AND mas.master_ref ='"
                                          + MasterReference + "'";

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG, "QueryNarrative result==========>" + queryNarrative);
                              }
                              con = getConnection();
                              ps = con.prepareStatement(queryNarrative);
                              rs = ps.executeQuery();
                              while (rs.next()) {

                                    String narrative = rs.getString(1);

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG, "Posting Narrative field not blank==========>" + narrative);
                                    }

                                    if ((narrative == null || narrative.equalsIgnoreCase(""))
                                                && (step_csm.equalsIgnoreCase("CBS Maker 1")) && (step_Input.equalsIgnoreCase("i"))) {
                                          validationDetails.addWarning(WarningType.Other, "Posting Narration field is blank [CM]");
                                    } else {
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG, "Posting Narrative field not blank else====>" + narrative);
                                          }
                                    }

                              }

                        } catch (Exception e) {
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG, "Exception in Posting Narrative field==========>" + e.getMessage());
                              }
                        }

                        finally {

                              try {
                                    if (con != null)
                                          con.close();
                                    if (rs != null)
                                          rs.close();
                                    if (ps != null)
                                          ps.close();
                              } catch (SQLException e) {
                                    // Loggers.general().info(LOG,"Connection Failed! Check output
                                    // console");
                                    e.printStackTrace();
                              }

                        }
                  }

                  // NPA customer

                  try {

                        String productVal = "N";
                        if (product.trim().equalsIgnoreCase("EGT")) {
                              productVal = getDriverWrapper().getEventFieldAsText("BEN", "p", "cAJB").trim();

                        } else if (product.trim().equalsIgnoreCase("IGT")) {
                              // //Loggers.general().info(LOG,"Major Code" + getMajorCode());
                              productVal = getDriverWrapper().getEventFieldAsText("APP", "p", "cAJB").trim();

                        } else {
                              // Loggers.general().info(LOG,"Primary customer taking else----> " +
                              // cust);
                        }

                        if (productVal.equalsIgnoreCase("Y") && (step_Input.equalsIgnoreCase("i"))
                                    && (step_csm.equalsIgnoreCase("AdhocCSM") || step_csm.equalsIgnoreCase("CSM")
                                                || step_csm.equalsIgnoreCase("CBS Maker")
                                                || step_csm.equalsIgnoreCase("CBS Maker 1"))) {
                              validationDetails.addWarning(WarningType.Other, "Customer is a NPA customer [CM]");
                        } else {
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG, "Not a NPA customer-----> " + productVal);
                              }

                        }

                  } catch (Exception e) {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG, "Exception NPA customer-----> " + e.getMessage());
                        }
                  }

                  
                  if (getMajorCode().equalsIgnoreCase("IGT")) {
                        // counter guarantee validation
                        try {
                              String transExp = getDriverWrapper().getEventFieldAsText("EXP", "d", "");
                              // //Loggers.general().info(LOG,"Transaction expiry date-> " +
                              // transExp);
                              List<ExtEventCounterGurantee> counterg = (List<ExtEventCounterGurantee>) getWrapper()
                                          .getExtEventCounterGurantee();
                              for (int l = 0; l < counterg.size(); l++) {

                                    ExtEventCounterGurantee gur = counterg.get(l);
                                    String gridexpda = gur.getEXDATE().toString();
                                    // //Loggers.general().info(LOG,"Grid expiry date-> " +
                                    // gridexpda);
                                    SimpleDateFormat parseFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
                                    Date gridexpdate = parseFormat.parse(gridexpda);
                                    // //Loggers.general().info(LOG,"Grid expiry date-> " +
                                    // gridexpdate);
                                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
                                    String dateI = formatter.format(gridexpdate);
                                    // //Loggers.general().info(LOG,"String grid date ----> " +
                                    // gridexpdate);
                                    Date dat = (Date) formatter.parse(dateI);
                                    // //Loggers.general().info(LOG,"Final Grid expiry date " +
                                    // dat);
                                    String transExpdate = formatter.format(transExp);
                                    // //Loggers.general().info(LOG,"String expiry date " +
                                    // transExpdate);
                                    Date dat3 = (Date) formatter.parse(transExpdate);
                                    // //Loggers.general().info(LOG,"Final grid Expiry date " +
                                    // dat3);
                                    if (counterg.size() > 0 && gridexpda != null && gridexpda.length() > 0 && dat.after(dat3)
                                                && (step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker"))) {

                                          // //Loggers.general().info(LOG,"Expire date is Guarantee
                                          // expiry
                                          // date [CM]");
                                          validationDetails.addWarning(ValidationDetails.WarningType.Other,
                                                      "Expiry date should not be greater than Guarantee expiry date  [CM]");
                                    }

                                    else {
                                          // Loggers.general().info(LOG,"Expire date is less than
                                          // Guarantee expiry date in grid [CM]");
                                    }

                              }

                        } catch (Exception e) {
                              Loggers.general().info(LOG, "exception " + e.getMessage());
                        }

                        // validation for counter guarantee

                        try {
                              double billTotal = 0.0;
                              double guranteeamt = 0.0;
                              double guranteeamount = 0.0;

                              List<ExtEventCounterGurantee> CounterGurantee = (List<ExtEventCounterGurantee>) getWrapper()
                                          .getExtEventCounterGurantee();
                              for (int i = 0; i < CounterGurantee.size(); i++) {
                                    ExtEventCounterGurantee gurantee = CounterGurantee.get(i);
                                    // CUR
                                    String mastamount = getDriverWrapper().getEventFieldAsText("CUR", "v", "m");

                                    billTotal = Double.valueOf(mastamount);

                                    String guamt = gurantee.getGURAMOT().toString();
                                    // //Loggers.general().info(LOG,"Gurantee amount initialy" +
                                    // guamt);
                                    String guamtCur = gurantee.getGURAMOTCurrency();
                                    // //Loggers.general().info(LOG,"Guarantee currency" +
                                    // guamtCur);
                                    ConnectionMaster connectionMaster = new ConnectionMaster();
                                    double divideByDecimal = connectionMaster.getDecimalforCurrency(guamtCur);
                                    // //Loggers.general().info(LOG,"Currency return fom method " +
                                    // divideByDecimal);
                                    guranteeamt = Double.valueOf(guamt);
                                    guranteeamt = guranteeamount + guranteeamt;
                                    double guaconv = guranteeamt / divideByDecimal;
                                    // //Loggers.general().info(LOG,"Gurantee amount after converted
                                    // --->" +
                                    // guaconv);
                                    String convamt = String.valueOf(guaconv);

                                    if (guaconv > 0 && guaconv < billTotal && (step_Input.equalsIgnoreCase("i"))
                                                && (step_csm.equalsIgnoreCase("CBS Maker"))) {
                                          // //Loggers.general().info(LOG," Master amount is not equal
                                          // to
                                          // guarantee amt");
                                          validationDetails.addWarning(WarningType.Other,
                                                      "Master gurantee amount should not be greater than counter guarantee amount  [CM]");

                                    } else {
                                          // Loggers.general().info(LOG," Master amount is equal to
                                          // guarantee
                                          // amt");
                                    }

                              }
                        } catch (Exception e) {
                              Loggers.general().info(LOG, "Exception caught on branch code validation......" + e.getMessage());
                        }

                  }

                  if ((!getMajorCode().equalsIgnoreCase("ILC") && !getMajorCode().equalsIgnoreCase("IGT")
                              && !getMajorCode().equalsIgnoreCase("ISB"))) {
                        // value for LOB
                        try {
                              getLob();

                        } catch (Exception ee) {
                              Loggers.general().info(LOG, "Exception LOB Catch" + ee.getMessage());
                        }
                  }

                  try {
                        getLOBISSUE();

                  } catch (Exception ee) {
                        Loggers.general().info(LOG, ee.getMessage());
                  }

                  // HS Code Validation
                  try {
                        String HScode = getDriverWrapper().getEventFieldAsText("cACO", "s", "").trim();
                        // //Loggers.general().info(LOG,"step is information" + stepid);
                        if ((!HScode.trim().equalsIgnoreCase("")) || HScode != null && (step_Input.equalsIgnoreCase("i"))
                                    && (step_csm.equalsIgnoreCase("CBS Maker")) && HScode.length() > 0) {
                              String qr = "select count(*) from exthmcode where hcodee='" + HScode + "'";
                              // //Loggers.general().info(LOG,"Query " + qr);
                              con = ConnectionMaster.getConnection();
                              ps = con.prepareStatement(qr);
                              // //Loggers.general().info(LOG,qr);
                              rs = ps.executeQuery();
                              while (rs.next()) {
                                    String k = rs.getString(1);
                                    // //Loggers.general().info(LOG,"while of count of hscode " +k);
                                    int ka = Integer.parseInt(k);
                                    if (ka == 0 && (step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker"))) {
                                          // //Loggers.general().info(LOG,"warning of hs code ");
                                          validationDetails.addWarning(ValidationDetails.WarningType.Other,
                                                      "HS Code is Invalid (" + HScode + ")" + "[CM]");

                                    }
                              }

                              // con.close();
                              // peco.close();
                              // rs.close();

                        } else {
                              // Loggers.general().info(LOG,"HS code");
                        }
                  } catch (Exception e) {
                        // //Loggers.general().info(LOG,"exception caught " +e.getMessage() );
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

//                try {
//                      currencyCalc();
//                } catch (Exception e) {
//                      if (dailyval_Log.equalsIgnoreCase("YES")) {
//                            Loggers.general().info(LOG,"Exception in currency value population---->" + e.getMessage());
//
//                      }
//                }

                  // IFSC code validation 050816
                  try {
                        String Ifsc = getWrapper().getIFSCCO_Name().trim();
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG, "IFSC Code====>" + Ifsc);
                        }
                        if (Ifsc.trim().equalsIgnoreCase("") || Ifsc == null) {
                              // validationDetails.addWarning(WarningType.Other, "Receiver
                              // IFSC code should not be blank");
                        } else {
                              if (!Ifsc.equalsIgnoreCase("") || Ifsc != null) {
                                    try {
                                          con = getConnection();
                                          String query = "select count(*) from EXTIFSCC where IFSC='" + Ifsc + "'";
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG, "Receiver IFSC code query " + query);
                                          }
                                          int count = 0;
                                          ps1 = con.prepareStatement(query);
                                          rs1 = ps1.executeQuery();
                                          while (rs1.next()) {
                                                count = rs1.getInt(1);

                                          }
                                          if (count == 0 && (step_Input.equalsIgnoreCase("i"))
                                                      && (step_csm.equalsIgnoreCase("CBS Maker"))) {

                                                // //Loggers.general().info(LOG,"If in IFSC");
                                                validationDetails.addError(ErrorType.Other,
                                                            "Invali Beneficiary IFSC code(" + Ifsc + ")  [CM]");
                                          }

                                    } catch (Exception e1) {
                                          // Loggers.general().info(LOG,"Exception" +
                                          // e1.getMessage());
                                    } finally {
                                          try {
                                                if (rs1 != null)
                                                      rs1.close();
                                                if (ps1 != null)
                                                      ps1.close();
                                                if (con != null)
                                                      con.close();
                                          } catch (SQLException e) {
                                                if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                      Loggers.general().info(LOG, "Exception IFSC code---->" + e.getMessage());

                                                }
                                          }
                                    }

                              }

                        }
                  } catch (Exception e) {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG, "Exception IFSC code validation---->" + e.getMessage());

                        }
                  }
                  
                  
                  // Category code populate based on input branch
                  try {
                        String BranchCode = getDriverWrapper().getEventFieldAsText("BIN", "b", "c");
                        con = ConnectionMaster.getConnection();
                        if (!(BranchCode.length() == 0)) {
                              String sql6 = "select telex from capf where cabrnm='" + BranchCode + "'";
                              // //Loggers.general().info(LOG,"BranchCode Query - " + sql6);
                              ps = con.prepareStatement(sql6);
                              rs = ps.executeQuery();
                              while (rs.next()) {
                                    String inmt = rs.getString(1);
                                    // //Loggers.general().info(LOG,"category code - " + inmt);
                                    getWrapper().setIMBRCODE(inmt);
                                    getPane().setIMBRCODE(inmt);
                              }

                              // con.close();
                              // psm.close();
                              // RST.close();
                        }
                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"Exception caught on branch code
                        // validation......" + e.getMessage());
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

                  /*
                   * // Charge Account Validation boolean isChargeDifferent =
                   * isChargeAccountDiff(con); if (isChargeDifferent)
                   * validationDetails.addWarning(WarningType.Other,
                   * "Account selected for charges does not belong to the Applicant");
                   */

                  // gl no validation
                  String gpdno = getWrapper().getNPDNUM().trim();
                  if (gpdno.trim().equalsIgnoreCase("") || gpdno == null) {
                        // validationDetails.addWarning(WarningType.Other, "Receiver
                        // IFSC
                        // code should not be blank");
                  } else {
                        if (!gpdno.trim().equalsIgnoreCase("") || gpdno != null) {

                              try {
                                    String query = "select count(*) from FTRT_DETAILS where TR_REF_NUM='" + gpdno + "'";
                                    // Loggers.general().info(LOG,"Query for Non-Position deal --->
                                    // " + query);

                                    con = ConnectionMaster.getThemeBridgeConnection();
                                    pst = con.prepareStatement(query);
                                    rst = pst.executeQuery();

                                    while (rst.next()) {
                                          int count2 = rst.getInt(1);

                                          if (count2 == 0 && (step_Input.equalsIgnoreCase("i"))
                                                      && (step_csm.equalsIgnoreCase("CBS Maker"))) {

                                                validationDetails.addError(ErrorType.Other,
                                                            "Invalid Non-Position deal reference number(" + gpdno + ")  [CM]");
                                          } else {
                                                // Loggers.general().info(LOG,"Non-Position value of
                                                // count in while " + count2);
                                          }
                                    }

                              } catch (Exception e1) {
                                    // Loggers.general().info(LOG,"Exception" + e1.getMessage());

                              } finally {
                                    try {

                                          if (rst != null)
                                                rst.close();
                                          if (pst != null)
                                                pst.close();
                                          if (con1 != null)
                                                con1.close();

                                    } catch (SQLException e) {
                                          // Loggers.general().info(LOG,"Connection Failed! Check
                                          // output console");
                                          e.printStackTrace();
                                    }
                              }
                        }

                  }

                  try {
                        int noOfRecord = 0;
                        con = getConnection();
                        String customerId = "";
                        String amount_am = "";
                        String currency = "";
                        String masterRef = "";
                        customerId = getDriverWrapper().getEventFieldAsText("APP", "p", "no").trim();
                        // //Loggers.general().info(LOG,"Customer Id " + customerId);
                        amount_am = getDriverWrapper().getEventFieldAsText("AMT", "v", "m");
                        String amount = amount_am.replaceAll("[^0-9]", "");
                        // //Loggers.general().info(LOG,"Currency is " + amount_am);
                        currency = getDriverWrapper().getEventFieldAsText("AMT", "v", "c");
                        // //Loggers.general().info(LOG,"Currency is " + currency);
                        masterRef = getmasRefNo();
                        // //Loggers.general().info(LOG,"Master Reference " + masterRef);
                        if (amount_am.length() > 0 && currency.length() > 0) {

                              // //Loggers.general().info(LOG,"Duplicate Record Query is " +
                              // duplicateMaster);

                              String duplicateMaster = "select COUNT(*) as COUNT from ETT_DUPLICAT_WARNING_VIEW where PRICUSTMNM='"
                                          + customerId + "' and AMOUNT='" + amount + "' and CCY='" + currency + "' and MASTER_REF !='"
                                          + MasterReference + "'";
                              if (dailyval_Log.equalsIgnoreCase("YES")) {

                                    Loggers.general().info(LOG, "Duplicate Record Query is " + duplicateMaster);
                              }
                              ps1 = con.prepareStatement(duplicateMaster);
                              rs1 = ps1.executeQuery();
                              if (rs1.next()) {
                                    noOfRecord = rs1.getInt("COUNT");
                              }
                              // //Loggers.general().info(LOG,"No of Existing records is " +
                              // noOfRecord);
                              if ((noOfRecord == 1 || noOfRecord > 0) && (step_Input.equalsIgnoreCase("i"))
                                          && (step_csm.equalsIgnoreCase("CSM") || step_csm.equalsIgnoreCase("CBS Maker")
                                                      || step_csm.equalsIgnoreCase("CSM Reject")
                                                      || step_csm.equalsIgnoreCase("CBS Reject")
                                                      || step_csm.equalsIgnoreCase("AdhocCSM"))) {
                                    validationDetails.addWarning(WarningType.Other,
                                                "There is an existing transaction for the Same Customer,Amount and Currency [CM]");
                              } else {
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {

                                          Loggers.general().info(LOG, "Duplicate Record in else " + noOfRecord);
                                    }
                              }
                        }
                  } catch (Exception e) {

                        if (dailyval_Log.equalsIgnoreCase("YES")) {

                              Loggers.general().info(LOG, "Exception in Duplicate master " + e.getMessage());
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
                              //// Loggers.general().info(LOG,"Connection Failed! Check output
                              //// console");
                              e.printStackTrace();
                        }
                  }

                  e.getMessage());
                  

                  String seifsc = getWrapper().getSENIFSC().trim();
                  // //Loggers.general().info(LOG,"IFSC Code"+Ifsc);

                  if (seifsc.length() > 0 && (step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker"))) {
                        try {

                              String query = "select count(*) from EXTIFSCC where IFSC='" + seifsc + "'";
                              // //Loggers.general().info(LOG,"query " + query);
                              int count3 = 0;
                              con = getConnection();
                              ps1 = con.prepareStatement(query);
                              rs1 = ps1.executeQuery();
                              while (rs1.next()) {
                                    // //Loggers.general().info(LOG,"Entered while");
                                    count3 = rs1.getInt(1);
                              }

                              if (count3 == 0 && (step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker"))) {

                                    validationDetails.addError(ErrorType.Other, "Invalid Sender IFSC code(" + seifsc + ")  [CM]");
                              } else {
                                    // Loggers.general().info(LOG,"valid Sender IFSC code");
                              }
                              // ps1.close();
                              // rs1.close();
                              // con.close();

                        } catch (Exception e1) {
                              // Loggers.general().info(LOG,"Exception Sender IFSC Code" +
                              // e1.getMessage());
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
                        // Loggers.general().info(LOG,"Sender IFSC Code" + seifsc);

                  }

                  String recifsc = getWrapper().getRECIFSC().trim();
                  // //Loggers.general().info(LOG,"IFSC Code"+Ifsc);

                  if (recifsc.length() > 0 && (step_Input.equalsIgnoreCase("i"))
                              && (step_csm.equalsIgnoreCase("CBS Maker"))) {
                        try {

                              String query = "select count(*) from EXTIFSCC where IFSC='" + recifsc + "'";
                              // //Loggers.general().info(LOG,"query " + query);
                              int count3 = 0;
                              con = getConnection();
                              ps1 = con.prepareStatement(query);
                              rs1 = ps1.executeQuery();
                              while (rs1.next()) {
                                    // //Loggers.general().info(LOG,"Entered while");
                                    count3 = rs1.getInt(1);
                              }

                              if (count3 == 0 && (step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker"))) {

                                    validationDetails.addError(ErrorType.Other,
                                                "Invalid Receiver IFSC code(" + recifsc + ")  [CM]");
                              } else {
                                    // Loggers.general().info(LOG,"valid Receiver IFSC code");
                              }

                              // ps1.close();
                              // rs1.close();
                              // con.close();

                        } catch (Exception e1) {
                              // Loggers.general().info(LOG,"Exception Receiver IFSC Code" +
                              // e1.getMessage());
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
                        // Loggers.general().info(LOG,"Receiver IFSC Code" + recifsc);

                  }

                  try {

                        /*
                         * String query =
                         * "SELECT REF.STEP_ID, COUNT(*) FROM MASTER MAS, BASEEVENT BEV, ETT_REFERRAL_TRACKING REF WHERE MAS.KEY97 = BEV.MASTER_KEY AND trim(MAS.MASTER_REF) =TRIM(REF.MASTER_REF_NO)  AND BEV.STATUS <>'a' AND TRIM(REF.MASTER_REF_NO) ='"
                         * + MasterReference + "' AND REF.EVENT_REF_NO = '" + eventCode +
                         * "' AND REF.SUB_PRODUCT_CODE = '" + subproductCode +
                         * "' AND (REF.REFERRAL_STATUS ='REJ' OR REF.REFERRAL_STATUS ='PEND') GROUP BY REF.STEP_ID"
                         * ;
                         */
                        String query = "SELECT REF.STEP_ID, COUNT(*) FROM MASTER MAS, ETT_REFERRAL_TRACKING REF WHERE  trim(MAS.MASTER_REF) =TRIM(REF.MASTER_REF_NO)  AND TRIM(REF.MASTER_REF_NO) ='"
                                    + MasterReference + "' AND REF.EVENT_REF_NO = '" + eventCode + "' AND REF.SUB_PRODUCT_CODE = '"
                                    + subproductCode
                                    + "' AND (REF.REFERRAL_STATUS ='REJ' OR REF.REFERRAL_STATUS ='PEND') GROUP BY REF.STEP_ID";

                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG, "Query ETT_REFERRAL_TRACKING count===>" + query);
                        }
                        int count = 0;
                        String step = null;
                        String concat = null;
                        con = getConnection();
                        ps1 = con.prepareStatement(query);
                        rs1 = ps1.executeQuery();
                        while (rs1.next()) {

                              step = rs1.getString(1);

                              count = rs1.getInt(2);
                              Loggers.general().info(LOG, "Entered while referal step" + step + " count" + count);
                              if (count > 0) {
                                    if ((step_csm.equalsIgnoreCase("CBS Maker") || step_csm.equalsIgnoreCase("CBS Reject"))
                                                && !step_csm.equalsIgnoreCase("CBS Authoriser")) {

                                          String ref = null;
                                          String statusReferral = null;
                                          String warningMessage = null;
                                          String query6 = "SELECT  REFERRAL,REFERRAL_STATUS,STEP_ID "
                                                      + " FROM ETT_REFERRAL_TRACKING  WHERE  MASTER_REF_NO ='" + MasterReference + "'"
                                                      + " AND EVENT_REF_NO = '" + eventCode + "'" + " AND SUB_PRODUCT_CODE = '"
                                                      + subproductCode + "'" + " and (REFERRAL_STATUS='REJ' OR REFERRAL_STATUS='PEND')";
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,
                                                            "ETT_REFERRAL_TRACKING Warning Message query step2====>" + query6);
                                          }
                                          ps1 = con.prepareStatement(query6);

                                          rs = ps1.executeQuery();
                                          ArrayList<String> al = new ArrayList<String>();
                                          while (rs.next()) {
                                                Loggers.general().info(LOG, "referral_)status------------>" + rs.getString(2));
                                                if (rs.getString(2).equals("PEND")) {

                                                      statusReferral = "Pending";
                                                      ref = rs.getString(3) + "'s Referral " + "\"" + rs.getString(1) + "\"" + " is "
                                                                  + statusReferral + " for approval ";

                                                } else {
                                                      statusReferral = "Rejected";
                                                      ref = rs.getString(3) + "'s Referral " + "\"" + rs.getString(1) + "\"" + " is "
                                                                  + statusReferral + "by approver";

                                                }
                                                al.add(ref);
                                          }
                                          warningMessage = StringUtils.join(al, "\n");
                                          // Loggers.general().info(LOG,"Single Warning Message " +
                                          // warningMessage);

                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG, "Single Warning Message " + warningMessage);
                                          }

                                          validationDetails.addWarning(WarningType.Other, warningMessage);

                                    } else if (step_csm.equalsIgnoreCase("CBS Checker")
                                                || step_csm.equalsIgnoreCase("CBS Authoriser")) {
                                          Loggers.general().info(LOG, "Authoriser");
                                          String ref = null;
                                          String statusReferral = null;
                                          String warningMessage = null;
                                          String query6 = "SELECT  REFERRAL,REFERRAL_STATUS,STEP_ID "
                                                      + " FROM ETT_REFERRAL_TRACKING  WHERE  MASTER_REF_NO ='" + MasterReference + "'"
                                                      + " AND EVENT_REF_NO = '" + eventCode + "'" + " AND SUB_PRODUCT_CODE = '"
                                                      + subproductCode + "'" + " and (REFERRAL_STATUS='REJ' OR REFERRAL_STATUS='PEND')";
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG,
                                                            "ETT_REFERRAL_TRACKING Error Message query step3====>" + query6);
                                          }
                                          ps1 = con.prepareStatement(query6);

                                          rs = ps1.executeQuery();
                                          ArrayList<String> al = new ArrayList<String>();
                                          while (rs.next()) {
                                                Loggers.general().info(LOG, "referral_)status------------>" + rs.getString(2));
                                                if (rs.getString(2).equals("PEND")) {

                                                      statusReferral = "Pending";
                                                      ref = " Referral " + rs.getString(1) + " is " + statusReferral + " with "
                                                                  + rs.getString(3);

                                                } else {
                                                      statusReferral = "Rejected";
                                                      ref = " Referral " + rs.getString(1) + " has been " + statusReferral + " by "
                                                                  + rs.getString(3);
                                                }
                                                al.add(ref);
                                          }
                                          warningMessage = StringUtils.join(al, "\n");
                                          // Loggers.general().info(LOG,"Single Warning Message " +
                                          // warningMessage);

                                          validationDetails.addError(ErrorType.Other, warningMessage);

                                    } else {
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {
                                                Loggers.general().info(LOG, "referal step in else" + step_csm);
                                          }
                                    }
                              } else {

                                    // Loggers.general().info(LOG,"referal step in step" +
                                    // step_csm);
                              }
                        }
                        // Loggers.general().info(LOG,"Entered while referal count out of loop"
                        // + count);
                  } catch (Exception e1) {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG, "Exception ETT_REFERRAL_TRACKING" + e1.getMessage());
                        }
                  } finally {
                        try {
                              if (rs != null)
                                    rs.close();
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

                  String prd_code = getDriverWrapper().getEventFieldAsText("PCO", "s", "").trim();
                  // rejection validation
                  try {
                        int counter = 0;
                        String query5 = "SELECT  count(*) FROM ETT_WF_CHKLST_TRACKING  WHERE  MASTER_REF ='" + MasterReference
                                    + "'" + " AND EVENTREF= '" + eventCode + "'" + " and PROD_CODE='" + prd_code + "'"
                                    + " AND SUB_PRODUCT_CODE= '" + subproductCode + "'"
                                    + " and INIT_AT IN ('CSM','CBS Maker') and MANDATORY='REJ'  and CHECKED_YN ='Y'";
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG,
                                          "ArrayList Warning Message ETT_WF_CHKLST_TRACKING rejection 1st" + query5);
                        }
                        con = getConnection();
                        pst = con.prepareStatement(query5);

                        rs = pst.executeQuery();
                        while (rs.next()) {
                              counter = Integer.valueOf(rs.getString(1));
                        }

                        // //Loggers.general().info(LOG,"Rejection list count===>" + counter);

                        if (counter > 0) {
                              String ref = null;
                              String statusReferral = null;
                              String warningMessage = null;
                              String query6 = "SELECT  INIT_AT,CHECKLIST_SHRT_DESCR "
                                          + " FROM ETT_WF_CHKLST_TRACKING  WHERE  MASTER_REF ='" + MasterReference + "'"
                                          + " AND EVENTREF = '" + eventCode + "'" + " and PROD_CODE='" + prd_code + "'"
                                          + " AND SUB_PRODUCT_CODE = '" + subproductCode + "'"
                                          + " and INIT_AT IN ('CSM','CBS Maker') and MANDATORY='REJ' and CHECKED_YN ='Y'";
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG,
                                                "ArrayList Warning Message ETT_WF_CHKLST_TRACKING rejection 2nd" + query6);
                              }
                              pst = con.prepareStatement(query6);
                              ArrayList<String> al = new ArrayList<String>();
                              rs = pst.executeQuery();
                              while (rs.next()) {

                                    ref = " Rejections " + "\"" + rs.getString(2) + "\"" + " raised by " + rs.getString(1)
                                                + " is not resolved [CM]";
                                    al.add(ref);
                              }

                              warningMessage = StringUtils.join(al, "\n");
                              if (al.size() > 0 && (step_csm.equalsIgnoreCase("CSM") || step_csm.equalsIgnoreCase("CSM Reject")
                                          || step_csm.equalsIgnoreCase("AdhocCSM"))) {
                                    // Loggers.general().info(LOG,"ArrayList Single Warning CSM 2nd"
                                    // + warningMessage);
                                    validationDetails.addWarning(WarningType.Other, warningMessage);
                              }

                              else if (al.size() > 0
                                          && (step_csm.equalsIgnoreCase("CBS Reject") || step_csm.equalsIgnoreCase("CBS Maker"))) {
                                    // Loggers.general().info(LOG,"ArrayList Single Warning CBS 2nd"
                                    // + warningMessage);
                                    validationDetails.addError(ErrorType.Other, warningMessage);
                              } else if (al.size() > 0 && step_csm.equalsIgnoreCase("CBS Authoriser")) {
                                    validationDetails.addError(ErrorType.Other, warningMessage);
                              }
                        }

                  } catch (SQLException e) {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG, "Exception ETT_WF_CHKLST_TRACKING rejection" + e.getMessage());
                        }
                        e.printStackTrace();
                  } finally {
                        try {

                              if (rs != null)
                                    rs.close();
                              if (pst != null)
                                    pst.close();
                              if (con != null)
                                    con.close();

                        } catch (SQLException e) {
                              // Loggers.general().info(LOG,"Connection Failed! Check output
                              // console");
                              e.printStackTrace();
                        }
                  }

                  try {

                        String query5 = "select count(*),trim(mas.PRICUSTMNM),ref.STATUS,ref.REFERRAL_STATUS from MASTER mas,ETT_REFERRAL_TRACKING ref where trim(mas.MASTER_REF)=ref.MASTER_REF_NO and mas.MASTER_REF='"
                                    + MasterReference
                                    + "' and ref.REFERRAL_STATUS='PEND' group by mas.PRICUSTMNM,ref.STATUS,ref.REFERRAL_STATUS";
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG, "Query REFERRAL_STATUS pending" + query5);
                        }

                        con = getConnection();
                        pst = con.prepareStatement(query5);

                        rs = pst.executeQuery();
                        if (rs.next()) {
                              // int counter = getInt(1);

                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG, "Pending referal status count if loop");
                              }

                              if (step_Input.equalsIgnoreCase("i") && (step_csm.equalsIgnoreCase("CSM")
                                          || step_csm.equalsIgnoreCase("AdhocCSM") || step_csm.equalsIgnoreCase("CBS Maker"))) {
                                    validationDetails.addWarning(WarningType.Other, "Past pending deferral for same client [CM]");
                              }

                        } else {
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    Loggers.general().info(LOG, "No pending referal status" + query5);
                              }

                        }

                  } catch (Exception e) {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG, "ExceptionPending referal status" + e.getMessage());
                        }
                  }

                  String char_val = getWrapper().getBENNAME_Name();
                  if (char_val != null && char_val.length() > 0) {
                        int counter = 0;
                        for (int i = 0; i < char_val.length(); i++) {
                              if (!Character.toString(char_val.charAt(i)).matches("^[a-zA-Z\\s]*$")) {
                                    counter++;
                              }
                        }

                        if (counter > 0 && (step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker"))) {
                              validationDetails.addError(ErrorType.Other,
                                          "Special characters should not be in Beneficiary name [CM]");
                        }
                  }

                  String ben_val = getWrapper().getBADDRE_Name();
                  if (ben_val != null && ben_val.length() > 0) {
                        int counter_val = 0;
                        for (int i = 0; i < ben_val.length(); i++) {
                              if (!Character.toString(ben_val.charAt(i)).matches("^[a-zA-Z0-9\\s]*$")) {
                                    counter_val++;
                              }
                        }

                        if (counter_val > 0 && (step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker"))) {
                              validationDetails.addError(ErrorType.Other,
                                          "Special characters should not be in Beneficiary Address [CM]");
                        }
                  }

                  String acc_val = getWrapper().getBENACC_Name();
                  if (acc_val != null && acc_val.length() > 0 && (!acc_val.matches("[a-zA-Z0-9]+"))
                              && (step_Input.equalsIgnoreCase("i")) && (step_csm.equalsIgnoreCase("CBS Maker"))) {
                        validationDetails.addError(ErrorType.Other,
                                    "Special characters should not be in Beneficiary Account no [CM]");
                  }

                  // DIFFERENCE BETWEEN EXPIRY DATE AND SHIPMENT DATE
                  if (subproductCode.equalsIgnoreCase("BCR")) {
                        try {
                              String d = getDriverWrapper().getEventFieldAsText("EXP", "d", "").trim();
                              // Loggers.general().info(LOG,"Shipment date difference values");
                              String d1 = getWrapper().getSHTDAT();
                              if (d != null && d1 != null && d1.length() > 0 && d.length() > 0) {
                                    getPane().setNUMDAYS(difference(d, d1).substring(0, difference(d, d1).indexOf(".")));
                                    getWrapper().setNUMDAYS(difference(d, d1).substring(0, difference(d, d1).indexOf(".")));
                              } else {
                                    // Loggers.general().info(LOG,"else part of date difference
                                    // values");
                              }
                        } catch (Exception e) {
                              // Loggers.general().info(LOG,"exeception date difference values" +
                              // e);
                        }
                  }

                  // Bill reference date compare
                  try {
                        if (subproductCode.equalsIgnoreCase("BCR")) {
                              List<ExtEventBillReference> bill = (List<ExtEventBillReference>) getWrapper()
                                          .getExtEventBillReference();
                              List<Date> list = new ArrayList<Date>();
                              SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yy");
                              for (int l = 0; l < bill.size(); l++) {
                                    ExtEventBillReference billref = bill.get(l);

                                    // subproCode
                                    String shipcountry = billref.getSHTOCON().trim();
                                    // //Loggers.general().info(LOG,"Shipment to country" +
                                    // shipcountry);
                                    if ((!shipcountry.equalsIgnoreCase("IN")) && subproductCode.equalsIgnoreCase("BCR")
                                                && step_Input.equalsIgnoreCase("i") && (step_csm.equalsIgnoreCase("CBS Maker"))) {
                                          validationDetails.addWarning(WarningType.Other,
                                                      "Shipment to country should be IN. Please check if the transaction is Merchanding trade  [CM]");

                                    } else {
                                          // Loggers.general().info(LOG,"Shipment to country should be
                                          // IN");

                                    }
                                    // //Loggers.general().info(LOG,"Shipment date for buyer cradit
                                    // before====>");
                                    String billdat = billref.getSHPDA().toString();
                                    // //Loggers.general().info(LOG,"Shipment date for buyer cradit
                                    // after====>" + billdat);
                                    // if (billdat.equalsIgnoreCase("") || billdat == null
                                    // &&
                                    // subproCode.equalsIgnoreCase("BCR")
                                    // && step_Input.equalsIgnoreCase("i") &&
                                    // (step_csm.equalsIgnoreCase("CBS Maker"))) {
                                    // validationDetails.addError(ErrorType.Other,
                                    // "Shipment date is mandatory in Bill reference grid
                                    // [CM]");
                                    // } else {
                                    // //Loggers.general().info(LOG,"Shipment date is not
                                    // blank====>");
                                    // }

                                    if (billdat.length() > 0 && billdat != null) {
                                          // //Loggers.general().info(LOG,"Bill referen date in
                                          // string----->" + billdat);
                                          SimpleDateFormat parseFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");

                                          try {
                                                Date date1 = parseFormat.parse(billdat);
                                                // //Loggers.general().info(LOG,"Bill referen date in
                                                // date
                                                // format----->" + date1);
                                                String result1 = formatter1.format(date1);
                                                Date dat = (Date) formatter1.parse(result1);
                                                // //Loggers.general().info(LOG,"Bill referen date after
                                                // convert----->" + dat);
                                                list.add(dat);
                                                // //Loggers.general().info(LOG,"Bill referen date
                                                // list----->"
                                                // + list);
                                          } catch (Exception e) {
                                                // Loggers.general().info(LOG,"Exception Bill referen
                                                // date"
                                                // + e.getMessage());
                                          }
                                    } else {
                                          getPane().setSHTDAT("");
                                          // //Loggers.general().info(LOG,"Bill reference date is
                                          // empty");
                                    }

                              }
                              if (list.size() > 0) {
                                    Date comprareDate = ConnectionMaster.compareAndReturnDate(list);
                                    // //Loggers.general().info(LOG,"Least Bill referen
                                    // comprareDate----->" + comprareDate);
                                    String resultdate = formatter1.format(comprareDate);

                                    // //Loggers.general().info(LOG,"Least Bill referen resultdate
                                    // for
                                    // set----->" + resultdate);
                                    getPane().setSHTDAT(resultdate);
                                    getWrapper().setSHTDAT(resultdate);

                                    // DIFFERENCE BETWEEN EXPIRY DATE AND SHIPMENT DATE
                                    try {
                                          String d = getDriverWrapper().getEventFieldAsText("EXP", "d", "").trim();
                                          // String accept=getWrapper().getSHTDAT();
                                          String d1 = getWrapper().getSHTDAT();
                                          if (d != null && d1 != null && d1.length() > 0 && d.length() > 0) {
                                                getPane().setNUMDAYS(difference(d, d1).substring(0, difference(d, d1).indexOf(".")));
                                                getWrapper().setNUMDAYS(difference(d, d1).substring(0, difference(d, d1).indexOf(".")));
                                          } else {
                                                // Loggers.general().info(LOG,"else part of date
                                                // difference
                                                // values");
                                          }
                                    } catch (Exception e) {
                                          // Loggers.general().info(LOG,"exeception date difference
                                          // values" + e);
                                    }

                              } else {
                                    // Loggers.general().info(LOG,"Array list size for bill
                                    // reference
                                    // grid is less than 1");

                              }
                        } else {
                              // Loggers.general().info(LOG,"Subproduct type is not BCR");
                        }
                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"exeception date difference values" +
                        // e);
                  }

                  String rtgs = getWrapper().getPROREMT();
                  // Loggers.general().info(LOG,"RTGS for initially-------->" + rtgs);

                  // Old validation

                  // try {
                  // String NEFTHOLY = "RTGSHOLY";
                  // String NEFTTime = "";
                  // AdhocQuery<? extends
                  // com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP>
                  // queryResult = getDriverWrapper()
                  // .createQuery("EXTGENCUSTPROP", "PROPNAME='" + NEFTHOLY + "'");
                  // // //Loggers.general().info(LOG,"ADDDailyTxnLimit initially-------->");
                  // EXTGENCUSTPROP querycode = queryResult.getUnique();
                  // if (querycode != null) {
                  //
                  // NEFTTime = querycode.getPropval().trim();
                  // // //Loggers.general().info(LOG,"NEFTTime time for cut off-------->"
                  // // +
                  // // querycode.getPropval());
                  // } else {
                  // // Loggers.general().info(LOG,"Today is holyday so RTGS/NEFT
                  // // empty-------->");
                  //
                  // }
                  //
                  // if (NEFTTime.equalsIgnoreCase("NO") &&
                  // (rtgs.equalsIgnoreCase("RTG") || rtgs.equalsIgnoreCase("NEF"))
                  // && (step_csm.equalsIgnoreCase("CBS Maker") ||
                  // step_csm.equalsIgnoreCase("Authoriser")
                  // || step_csm.equalsIgnoreCase("Authorise"))) {
                  // validationDetails.addError(ErrorType.Other, "Today is holiday so
                  // RTGS/NEFT will no proceed [CM]");
                  // } else {
                  // // Loggers.general().info(LOG,"Today is holyday so RTGS/NEFT will
                  // // avilable");
                  // }
                  // } catch (Exception e) {
                  // // Loggers.general().info(LOG,"NEFT cut off TIME Exception" +
                  // // e.getMessage());
                  // }

                  // New Validation

                  try {

                        if (rtgs.equalsIgnoreCase("NEF")) {

                              String NEFTHOLY = "NEFTWorking";
                              String NEFTTime = "";
                              AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryResult = getDriverWrapper()
                                          .createQuery("EXTGENCUSTPROP", "PROPNAME='" + NEFTHOLY + "'");
                              // //Loggers.general().info(LOG,"ADDDailyTxnLimit
                              // initially-------->");
                              EXTGENCUSTPROP querycode = queryResult.getUnique();
                              if (querycode != null) {

                                    NEFTTime = querycode.getPropval().trim();

                              } else {
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG, "NEFT value Blank-------->" + NEFTTime);
                                    }

                              }
                              if (NEFTTime.equalsIgnoreCase("NO") && (rtgs.equalsIgnoreCase("NEF"))
                                          && (step_csm.equalsIgnoreCase("CBS Maker") || step_csm.equalsIgnoreCase("CBS Authoriser")
                                                      || step_csm.equalsIgnoreCase("Authorise"))) {
                                    validationDetails.addError(ErrorType.Other,
                                                "Today is holiday. No NEFT entry will proceed [CM]");
                              } else {
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG, "Today is not holyday so NEFT will avilable");
                                    }
                              }

                        } else if (rtgs.equalsIgnoreCase("RTG")) {

                              String RTGSHOLY = "RTGSWorking";
                              String RTGSTime = "";
                              AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryResult = getDriverWrapper()
                                          .createQuery("EXTGENCUSTPROP", "PROPNAME='" + RTGSHOLY + "'");
                              // //Loggers.general().info(LOG,"ADDDailyTxnLimit
                              // initially-------->");
                              EXTGENCUSTPROP querycode = queryResult.getUnique();
                              if (querycode != null) {

                                    RTGSTime = querycode.getPropval().trim();

                              } else {
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG, "RTGS value Blank-------->" + RTGSTime);
                                    }

                              }
                              if (RTGSTime.equalsIgnoreCase("NO") && (rtgs.equalsIgnoreCase("RTG"))
                                          && (step_csm.equalsIgnoreCase("CBS Maker") || step_csm.equalsIgnoreCase("CBS Authoriser")
                                                      || step_csm.equalsIgnoreCase("Authorise"))) {
                                    validationDetails.addError(ErrorType.Other,
                                                "Today is holiday. No RTGS entry will proceed [CM]");
                              } else {
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          Loggers.general().info(LOG, "Today is not holyday so RTGS will avilable");
                                    }
                              }

                        }

                  } catch (Exception e) {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              Loggers.general().info(LOG, "Exception RTGS/NEFT holiday" + e.getMessage());
                        }
                  }
                  String bankpay = getWrapper().getRTGNFT().trim();
                  try {
                        String RTGS = "RTGSCUTOFFTIME";
                        String RTGSTime = "";
                        AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryResult = getDriverWrapper()
                                    .createQuery("EXTGENCUSTPROP", "PROPNAME='" + RTGS + "'");
                        // //Loggers.general().info(LOG,"ADDDailyTxnLimit initially-------->");
                        EXTGENCUSTPROP querycode = queryResult.getUnique();
                        if (querycode != null) {

                              RTGSTime = querycode.getPropval();
                              // //Loggers.general().info(LOG,"RTGS time for cut off-------->" +
                              // querycode.getPropval());
                        } else {
                              // Loggers.general().info(LOG,"RTGS time for cut off is
                              // empty-------->");

                        }
                        // Loggers.general().info(LOG,"RTGS time for cut off from static
                        // table-------->" + RTGSTime);
                        // String RTGSTime = "14:30:47";
                        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                        Date date = new Date();
                        // //Loggers.general().info(LOG,"Current Time " +
                        // dateFormat.format(date));
                        String str1 = dateFormat.format(date);
                        Date t1 = null;
                        try {
                              t1 = new SimpleDateFormat("HH:mm:ss").parse(str1);
                        } catch (ParseException e) {
                              e.printStackTrace();
                        }
                        Calendar c1 = Calendar.getInstance();
                        c1.setTime(t1);

                        Date d = null;
                        try {
                              d = new SimpleDateFormat("HH:mm:ss").parse(RTGSTime);
                        } catch (ParseException e) {
                              // TODO Auto-generated catch block
                              e.printStackTrace();
                        }
                        Calendar c3 = Calendar.getInstance();
                        c3.setTime(d);
                        c3.add(Calendar.DATE, 0);

                        Date x = c3.getTime();
                        if (x.before(c1.getTime()) && rtgs.equalsIgnoreCase("RTG") && bankpay.equalsIgnoreCase("B2C")
                                    && (step_csm.equalsIgnoreCase("CBS Authoriser") || step_csm.equalsIgnoreCase("Authorise")
                                                || step_csm.equalsIgnoreCase("CSM"))) {
                              // Loggers.general().info(LOG,"RTGS cut off TIME is exceeded");
                              validationDetails.addError(ErrorType.Other, "RTGS cut-off time has been exceeded [CM]");
                        } else {
                              // Loggers.general().info(LOG,"RTGS cut off TIME is available");
                        }
                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"RTGS cut off TIME Exception" +
                        // e.getMessage());
                  }

                  try {
                        String NEFT = "NEFTCUTOFFTIME";
                        String NEFTTime = "";
                        AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryResult = getDriverWrapper()
                                    .createQuery("EXTGENCUSTPROP", "PROPNAME='" + NEFT + "'");
                        // //Loggers.general().info(LOG,"ADDDailyTxnLimit initially-------->");
                        EXTGENCUSTPROP querycode = queryResult.getUnique();
                        if (querycode != null) {

                              NEFTTime = querycode.getPropval();
                              // //Loggers.general().info(LOG,"NEFTTime time for cut off-------->"
                              // +
                              // querycode.getPropval());
                        } else {
                              // Loggers.general().info(LOG,"NEFTTime time for cut off is
                              // empty-------->");

                        }
                        // //Loggers.general().info(LOG,"NEFTTime time for cut off from static
                        // table-------->" + NEFTTime);
                        // String RTGSTime = "14:30:47";
                        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                        Date date = new Date();
                        // //Loggers.general().info(LOG,"Current Time " +
                        // dateFormat.format(date));
                        String str1 = dateFormat.format(date);
                        Date t1 = null;
                        try {
                              t1 = new SimpleDateFormat("HH:mm:ss").parse(str1);
                        } catch (ParseException e) {
                              e.printStackTrace();
                        }
                        Calendar c1 = Calendar.getInstance();
                        c1.setTime(t1);

                        Date d = null;
                        try {
                              d = new SimpleDateFormat("HH:mm:ss").parse(NEFTTime);
                        } catch (ParseException e) {
                              // TODO Auto-generated catch block
                              e.printStackTrace();
                        }
                        Calendar c3 = Calendar.getInstance();
                        c3.setTime(d);
                        c3.add(Calendar.DATE, 0);

                        Date x = c3.getTime();
                        if (x.before(c1.getTime()) && rtgs.equalsIgnoreCase("NEF")
                                    && (step_csm.equalsIgnoreCase("CBS Authoriser") || step_csm.equalsIgnoreCase("Authorise")
                                                || step_csm.equalsIgnoreCase("CSM"))) {
                              // //Loggers.general().info(LOG,"RTGS cut off TIME is exceeded");
                              validationDetails.addError(ErrorType.Other, "NEFT cut-off time has been exceeded [CM]");
                        } else {
                              // Loggers.general().info(LOG,"NEFT cut off TIME is avilable");
                        }
                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"NEFT cut off TIME Exception" +
                        // e.getMessage());
                  }

                  try {
                        String RTGS = "BANKTOBANK";
                        String RTGSTime = "";
                        AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryResult = getDriverWrapper()
                                    .createQuery("EXTGENCUSTPROP", "PROPNAME='" + RTGS + "'");
                        // //Loggers.general().info(LOG,"ADDDailyTxnLimit initially-------->");
                        EXTGENCUSTPROP querycode = queryResult.getUnique();
                        if (querycode != null) {

                              RTGSTime = querycode.getPropval();
                              // //Loggers.general().info(LOG,"RTGS time for cut off-------->" +
                              // querycode.getPropval());
                        } else {
                              // Loggers.general().info(LOG,"Bank to Bank Payment time for cut off
                              // is empty-------->");

                        }
                        // Loggers.general().info(LOG,"Bank to Bank Payment time for cut off
                        // from static table-------->" + RTGSTime);
                        // String RTGSTime = "14:30:47";
                        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                        Date date = new Date();
                        // //Loggers.general().info(LOG,"Current Time " +
                        // dateFormat.format(date));
                        String str1 = dateFormat.format(date);
                        Date t1 = null;
                        try {
                              t1 = new SimpleDateFormat("HH:mm:ss").parse(str1);
                        } catch (ParseException e) {
                              e.printStackTrace();
                        }
                        Calendar c1 = Calendar.getInstance();
                        c1.setTime(t1);

                        Date d = null;
                        try {
                              d = new SimpleDateFormat("HH:mm:ss").parse(RTGSTime);
                        } catch (ParseException e) {
                              // TODO Auto-generated catch block
                              e.printStackTrace();
                        }
                        Calendar c3 = Calendar.getInstance();
                        c3.setTime(d);
                        c3.add(Calendar.DATE, 0);

                        Date x = c3.getTime();
                        if (x.before(c1.getTime()) && bankpay.equalsIgnoreCase("B2B")
                                    && (step_csm.equalsIgnoreCase("CBS Authoriser") || step_csm.equalsIgnoreCase("Authorise")
                                                || step_csm.equalsIgnoreCase("CSM"))) {
                              // Loggers.general().info(LOG,"RTGS cut off TIME is exceeded");
                              validationDetails.addError(ErrorType.Other,
                                          "Bank to Bank Payment cut-off time has been exceeded [CM]");
                        } else {
                              // Loggers.general().info(LOG,"Bank to Bank Payment cut off TIME is
                              // available");
                        }
                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"Bank to Bank Payment cut off TIME
                        // Exception" + e.getMessage());
                  }

                  // Over due bill exists for this customer
                  if (step_csm.equalsIgnoreCase("CBS Maker") || step_csm.equalsIgnoreCase("CBS Maker 1")) {
                        try {
                              con = getConnection();
                              String query = "select * from ETT_OVERDUE_BILCUS_EOD where CUSTOMER_ID= '" + cust + "'";

                              // Loggers.general().info(LOG,"Over due bill exists for this
                              // customer " + query);
                              // int count = 0;
                              ps1 = con.prepareStatement(query);
                              rs1 = ps1.executeQuery();
                              while (rs1.next()) {
                                    // //Loggers.general().info(LOG,"Entered while Over due bill
                                    // exists
                                    // for this customer");

                                    if (step_Input.equalsIgnoreCase("i") && (step_csm.equalsIgnoreCase("CBS Maker"))) {
                                          // //Loggers.general().info(LOG,"Over due bill exists for
                                          // this
                                          // customer in if loop " + cust);
                                          validationDetails.addWarning(WarningType.Other,
                                                      "Over due bill exists for this customer (" + cust + ")  [CM]");
                                    }

                                    else {
                                          // //Loggers.general().info(LOG,"Over due bill exists for
                                          // this
                                          // customer in else " + cust);
                                    }
                              }

                        } catch (Exception e1) {
                              // Loggers.general().info(LOG,"Exception Over due bill" +
                              // e1.getMessage());
                        }

                        finally {
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
                        // Loggers.general().info(LOG,"Step id not CMS Maker");
                  }

                  
                  try {

                        getutrNoGenerated();
                  } catch (Exception ee) {
                        Loggers.general().info(LOG, "UTR Number getutrNoGenerated" + ee.getMessage());

                  }

                  /*
                   * if (((!step_csm.equalsIgnoreCase("CBS Authoriser"))||(!step_csm.
                   * equalsIgnoreCase("Final print"))) &&
                   * !eventStatus.equalsIgnoreCase("Completed")) { try { //getPerdChgEndDate();
                   * getStatutoryChargeGur(); getPerdChgEndDate(); } catch (Exception e) {
                   * Loggers.general().info(LOG,"Exception in getStatutoryChargeGur onvalidate" +
                   * e.getMessage()); } }
                   */
                  if (step_csm.equalsIgnoreCase("CSM") || step_csm.equalsIgnoreCase("CBS Maker")) {
                        Loggers.general().info(LOG, "statutory calling in input step on validate after addig csm");
                        try {
                              // getPerdChgEndDate();
                              String newExpDate = getDriverWrapper().getEventFieldAsText("EXP", "d", ""); // 12/10/16
                              Loggers.general().info(LOG, "Expiry date in statutory claim in customvalidation" + newExpDate);

                              getStatutoryChargeGur();

//                            getPane().setTEMEXPIR(newExpDate);
//                            Loggers.general().info(LOG,"Temp expiry date in customvalidaton"+getPane().getTEMEXPIR());

                              getPerdChgEndDate();
                        } catch (Exception e) {
                              Loggers.general().info(LOG, "Exception in getStatutoryChargeGur onvalidate" + e.getMessage());
                        }
                  }

                  // Update value
                  String rtgspart = getWrapper().getRTGSPART();

                  // if (rtgspart.equalsIgnoreCase("FULL")) {
                  // try {
                  // con = getConnection();
                  // String sql = "SELECT * from KMB_RTGS_NEFT_VIEW where MASTER_REF =
                  // '" + MasterReference
                  // + "' AND EVENT_REF = '" + eventCode + "'";
                  // // Loggers.general().info(LOG,"Query value for RTGS amount--->" +
                  // // sql);
                  // ps = con.prepareStatement(sql);
                  // rs = ps.executeQuery();
                  // while (rs.next()) {
                  // String rtgsVal = rs.getString(3);
                  // getPane().setRTGSNEFT(rtgsVal + " INR");
                  // getWrapper().setRTGSNEFT(rtgsVal + " INR");
                  // // //Loggers.general().info(LOG,"Query value in while-->" +
                  // // rtgsVal);
                  // //
                  // // String updatequry = "UPDATE EXTEVENT EXTE SET
                  // // EXTE.RTGSNEFT ='" + rtgsVal
                  // // + "', CCY_62 = 'INR' where EXTE.event in (SELECT
                  // // BEV.KEY97 FROM MASTER MAS, BASEEVENT BEV where
                  // // mas.KEY97 = bev.MASTER_KEY and mas.master_ref = '"
                  // // + MasterReference + "' and
                  // // BEV.REFNO_PFIX||lpad(bev.refno_serl,3,0) = '" +
                  // // eventCode
                  // // + "' )";
                  // // // //Loggers.general().info(LOG,"Update--->" + updatequry);
                  // // ps2 = con.prepareStatement(updatequry);
                  // // int insetrResult = ps2.executeUpdate();
                  // // // //Loggers.general().info(LOG,"Update value
                  // // insetrResult--->" +
                  // // // insetrResult);
                  //
                  // }
                  //
                  // } catch (Exception e) {
                  // // Loggers.general().info(LOG,"Exception update" + e.getMessage());
                  // } finally {
                  // try {
                  // if (con != null) {
                  // con.close();
                  // if (ps != null)
                  // ps.close();
                  // if (ps2 != null)
                  // ps2.close();
                  // if (rs != null)
                  // rs.close();
                  // }
                  // } catch (SQLException e) {
                  // // Loggers.general().info(LOG,"Connection Failed! Check output
                  // // console");
                  // e.printStackTrace();
                  // }
                  // }
                  // } else {
                  // Loggers.general().info(LOG,"RTGS/NEFT type is part");

                  // }
                  try {
                        String nodays = getWrapper().getNODTBFEX();
                        // Loggers.general().info(LOG,"Number of days==========>" + nodays);
                        if ((nodays != null && nodays.length() > 0) && expdate != null && expdate.length() > 0) {
                              // //Loggers.general().info(LOG,"claimdate is blank");
                              int numInt = 0;

                              String numSub = "-" + nodays;
                              // Loggers.general().info(LOG,"Number of days in minus==========>" +
                              // nodays);
                              numInt = Integer.parseInt(numSub);
                              // Loggers.general().info(LOG,"Number of days in int==========>" +
                              // numInt);
                              c.setTime(sdf.parse(expdate));
                              // //Loggers.general().info(LOG,"expdate in issue-------> ");
                              c.add(Calendar.DATE, numInt);
                              // //Loggers.general().info(LOG,"DATE 1"+ c);
                              String output = sdf.format(c.getTime());
                              // Loggers.general().info(LOG,"Number of days in output==========>"
                              // + output);
                              // getPane().setINVOREM(output);
                              // getWrapper().setINVOREM(output);
                        } else {
                              // getPane().setINVOREM("");
                        }

                  } catch (Exception e) {
                        // Loggers.general().info(LOG,"exeception" + e);
                  }
                  // Swift/sfms
                  /*
                   * if(getMajorCode().equalsIgnoreCase("IGT") &&
                   * (getMinorCode().equalsIgnoreCase("IIG")||getMinorCode().equalsIgnoreCase(
                   * "NJIG"))) { int v; String
                   * advDirect=getDriverWrapper().getEventFieldAsText("ABD", "l", "").trim();
                   * if(advDirect.equalsIgnoreCase("N") && !advDirect.equalsIgnoreCase("Y")) {
                   * try{ String Branch=getDriverWrapper().getEventFieldAsText("NPR", "p",
                   * "ss").trim(); Loggers.general().info(LOG,"Branch code12 ---->"+Branch);
                   * v=getSWIFTSFMS(Branch); if (v==1) {
                   * validationDetails.addError(ErrorType.Other,
                   * "Please select SFMS in Swift/Sfms [CM]"); } if(v==2) {
                   * validationDetails.addError(ErrorType.Other,
                   * "Please select SWIFT in Swift/Sfms [CM]"); }
                   *
                   * } catch(Exception e) { Loggers.general().info(LOG,"Swiftsfms--->" +
                   * e.getMessage());
                   *
                   * } } }
                   */
                  if ((step_csm.equalsIgnoreCase("CBS Maker 1") || step_csm.equalsIgnoreCase("CBS Reject")
                              || step_csm.equalsIgnoreCase("CBS Authoriser")) && getMajorCode().equalsIgnoreCase("IGT")
                              && (getMinorCode().equalsIgnoreCase("LIG") || getMinorCode().equalsIgnoreCase("OIG"))) {
                        // String forEvent = getFORCEDEBIT();
                        String forFin = getFORCEDEBITFIN();

                        String TransForceDebit = getDriverWrapper().getEventFieldAsText("cAHW", "l", "").trim();
                        // String TransForceDebitFinance=getDriverWrapper().getEventFieldAsText("cAHW",
                        // "l","").trim();
                        Loggers.general().info(LOG, "Force Debit in main event " + TransForceDebit);
                        Loggers.general().info(LOG, "Force Debit in Finance " + forFin);

                        /*
                         * if (forFin.equalsIgnoreCase("N")&&TransForceDebit.equalsIgnoreCase("Y")) {
                         *
                         * validationDetails.addError(ErrorType.Other,
                         * "Force Debit Flag should be ticked in subsidiary event [CM]");
                         *
                         * } else
                         */ if (TransForceDebit.equalsIgnoreCase("N") && forFin.equalsIgnoreCase("Y")) {
                              if (getMinorCode().equalsIgnoreCase("LIG"))
                                    validationDetails.addError(ErrorType.Other,
                                                "Force Debit Flag should be ticked in Claim Received [CM]");
                              else if (getMinorCode().equalsIgnoreCase("OIG"))
                                    validationDetails.addError(ErrorType.Other,
                                                "Force Debit Flag should be ticked in Outstanding claim [CM]");
                        }

                  }

                  // todo on 08AUG2019

                  String perichargval = getPane().getPERCHRAD().trim();
                  Loggers.general().info(LOG, "Periodic charge in upfront " + getPane().getPERCHRAD());

                  if ((getMajorCode().equalsIgnoreCase("IGT")) && (getMinorCode().equalsIgnoreCase("IIG"))) {
                        try {

                              String d = getDriverWrapper().getEventFieldAsText("ISS", "d", "").trim();
                              String d1 = getDriverWrapper().getEventFieldAsText("cBKY", "d", "").trim();

                              Loggers.general().info(LOG, "Issue date issue" + d);
                              Loggers.general().info(LOG, "Charge basis end date issue " + d1);

                              getPane().setCHRPERAM(difference(d, d1).substring(0, difference(d, d1).indexOf(".")));
                              Loggers.general().info(LOG, "charge - issue date days issue" + getPane().getCHRPERAM());

                        } catch (Exception e) {
                              Loggers.general().info(LOG, "Exception in calculating days issue " + e.getMessage());
                        }
                  }
                  if ((getMajorCode().equalsIgnoreCase("IGT")) && (getMinorCode().equalsIgnoreCase("NJIG"))) {
                        try {

                              String d = getDriverWrapper().getEventFieldAsText("AMD", "d", "").trim();
                              String d1 = getDriverWrapper().getEventFieldAsText("cBKY", "d", "").trim();

                              Loggers.general().info(LOG, "Issue date adjust" + d);
                              Loggers.general().info(LOG, "Charge basis end date adjust " + d1);

                              getPane().setCHRPERAM(difference(d, d1).substring(0, difference(d, d1).indexOf(".")));
                              Loggers.general().info(LOG, "charge - issue date days adjust" + getPane().getCHRPERAM());

                        } catch (Exception e) {
                              Loggers.general().info(LOG, "Exception in calculating days adjust " + e.getMessage());
                        }
                  }

                  if (perichargval.equalsIgnoreCase("Yes")) {

                        if ((getMajorCode().equalsIgnoreCase("IGT")) && (getMinorCode().equalsIgnoreCase("NAIG"))) {

                              String imlAmount = getDriverWrapper().getEventFieldAsText("IML", "v", "m").trim();
                              String n = getDriverWrapper().getEventFieldAsText("cBKY", "d", "").trim();
                              String m = getDriverWrapper().getEventFieldAsText("cBKZ", "d", "").trim();
                              Loggers.general().info(LOG, "iml amount amend" + imlAmount);
                              Loggers.general().info(LOG, "charge end end mirror " + m);
                              Loggers.general().info(LOG, "charge end end  " + n);

                              try {
                                    SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");

                                    Date date1 = sdf1.parse(n);
                                    Date date2 = sdf1.parse(m);

                                    Loggers.general().info(LOG, "date1.compareTo(date2) " + date1.compareTo(date2));
                                    if (date1.compareTo(date2) > 0) {
                                          try {

                                                String d = getDriverWrapper().getEventFieldAsText("occBKY", "d", "");
                                                String d1 = getDriverWrapper().getEventFieldAsText("nccBKY", "d", "");

                                                Loggers.general().info(LOG, "Issue date amend" + d);
                                                Loggers.general().info(LOG, "Charge basis end date amend " + d1);

                                                getPane().setCHRTENIN(difference(d, d1).substring(0, difference(d, d1).indexOf(".")));
                                                Loggers.general().info(LOG, "charge - issue date days amend" + getPane().getCHRTENIN());

                                          } catch (Exception e) {
                                                Loggers.general().info(LOG, "Exception in calculating days amend " + e.getMessage());
                                          }
                                    }

                                    if (imlAmount != null && !imlAmount.equalsIgnoreCase("")) {

                                          try {

                                                String d = getDriverWrapper().getEventFieldAsText("AMD", "d", "");
                                                String d1 = getDriverWrapper().getEventFieldAsText("cBKZ", "d", "");

                                                Loggers.general().info(LOG, "Issue date amend" + d);
                                                Loggers.general().info(LOG, "Charge basis end date amend " + d1);

                                          } catch (Exception e) {
                                                Loggers.general().info(LOG, "Exception in calculating days amend " + e.getMessage());
                                          }
                                    }

                              } catch (Exception e) {
                                    Loggers.general().info(LOG, "Exception in date format" + e.getMessage());
                              }

                        }
                  }

                  if ((getMajorCode().equalsIgnoreCase("IGT")) && ((getMinorCode().equalsIgnoreCase("IIG")
                              || getMinorCode().equalsIgnoreCase("NJIG") || getMinorCode().equalsIgnoreCase("NAIG")))) {
                        String val = "";
                        String sts = "";
                        String percharup = getPane().getPERCHRAD().trim();

                        try {

                              con = getConnection();
                              String query = "select dis_msg,status from KMB_M_CHG_VALID_MSG where trim(Master_ref)= '"
                                          + masterref + "' and trim(event_ref)='" + eventCode + "'";

                              ps1 = con.prepareStatement(query);
                              rs1 = ps1.executeQuery();
                              while (rs1.next()) {

                                    val = rs1.getString(1);
                                    Loggers.general().info(LOG, "Value  " + val);

                                    sts = rs1.getString(2);
                                    Loggers.general().info(LOG, "Status  " + sts);

                              }

                        } catch (Exception e1) {

                        }

                        finally {
                              try {
                                    if (rs1 != null)
                                          rs1.close();
                                    if (ps1 != null)
                                          ps1.close();
                                    if (con != null)
                                          con.close();
                              } catch (SQLException e) {

                                    e.printStackTrace();
                              }
                        }

                        if ((percharup.equalsIgnoreCase("Yes") && sts.equalsIgnoreCase("N")
                                    && step_csm.equalsIgnoreCase("CBS Authoriser"))) {
                              validationDetails.addError(ErrorType.Other, val + "[CM]");
                        }

                  }
                  try {
                        String paymentType = getWrapper().getPROREMT();
                        String utrNum = getWrapper().getUTRNO().trim();
                        if (paymentType.equalsIgnoreCase("RTG") && utrNum.length() != 22
                                    && (step_csm.equalsIgnoreCase("CBS Authoriser") || step_csm.equalsIgnoreCase("Authorise")
                                                || step_csm.equalsIgnoreCase("CBS Maker1") || step_csm.equalsIgnoreCase("CBS Maker")
                                                || step_csm.equalsIgnoreCase("CBS Reject"))
                                    && rtgsFlag.equalsIgnoreCase("Y") && !rtgsFlag.equalsIgnoreCase("N")) {
                              validationDetails.addError(ErrorType.Other, "Please review UTR number for RTGS[CM]");
                        } else if (paymentType.equalsIgnoreCase("NEF") && utrNum.length() != 16
                                    && (step_csm.equalsIgnoreCase("CBS Authoriser") || step_csm.equalsIgnoreCase("Authorise")
                                                || step_csm.equalsIgnoreCase("CBS Maker1") || step_csm.equalsIgnoreCase("CBS Maker")
                                                || step_csm.equalsIgnoreCase("CBS Reject"))
                                    && rtgsFlag.equalsIgnoreCase("Y") && !rtgsFlag.equalsIgnoreCase("N")) {
                              validationDetails.addError(ErrorType.Other, "Please review UTR number for NEFT[CM]");
                        } else {
                              Loggers.general().info(LOG, "Lenth is correct");
                        }

                  } catch (Exception e) {
                        e.printStackTrace();
                  }
                  // todo end on 08AUG2019

                  // Invocation
                  if ((getMajorCode().equalsIgnoreCase("IGT")) && (getMinorCode().equalsIgnoreCase("LIG"))) {
                        try {
                              String subproduct = getDriverWrapper().getEventFieldAsText("PTP", "s", "").trim();
                              String eventname = getDriverWrapper().getEventFieldAsText("EVLN", "s", "").trim();
                              String Crn = getDriverWrapper().getEventFieldAsText("APP", "p", "no").trim();
                              String swift = getDriverWrapper().getEventFieldAsText("APP", "p", "ss").trim();
                              Loggers.general().info(LOG, "subproduct----------8/1/2020  " + subproduct);
                              Loggers.general().info(LOG, "Crn----------8/1/2020  " + Crn);
                              Loggers.general().info(LOG, "swift----------8/1/2020  " + swift);
                              Loggers.general().info(LOG, "swift----------8/1/2020  " + eventname);
                              String result = null;
                              String result2 = null;
                              if (subproduct.equalsIgnoreCase("BCR") && eventname.equalsIgnoreCase("Invocation")) {
                                    Loggers.general().info(LOG, "result----------8/1/2020  " + result);
                                    con = getConnection();
                                    String query = "SELECT trim(PTY.CUS_MNM),TRIM(SW_BANK)||TRIM(SW_CTR)||TRIM(SW_LOC)||TRIM(SW_BRANCH) CUS_MNM"
                                                + " FROM MASTER MAS, TIDATAITEM TI, PARTYDTLS PTY WHERE MAS.KEY97    = TI.MASTER_KEY AND TI.KEY97       = PTY.KEY97"
                                                + "  AND PTY.ROLE       = 'APP' AND MAS.MASTER_REF = '" + masterref + "'";
                                    Loggers.general().info(LOG, "query----------8/1/2020  " + query);
                                    ps1 = con.prepareStatement(query);
                                    rs1 = ps1.executeQuery();
                                    while (rs1.next()) {
                                          result = rs1.getString(1);
                                          result2 = rs1.getString(2);
                                          Loggers.general().info(LOG, "result----------8/1/2020  " + result);
                                          Loggers.general().info(LOG, "result----------8/1/2020  " + result2);
                                    }

                                    if (result != null && Crn != null && !result.equalsIgnoreCase(Crn)) {
                                          Loggers.general().info(LOG, "CRN----------8/1/2020  " + Crn);
                                          validationDetails.addError(ErrorType.Other,
                                                      "Please do not change the principal party [CM]");
                                    } else if (result2 != null && (Crn == null || Crn.equalsIgnoreCase(" ")) && swift != null
                                                && !result2.equalsIgnoreCase(swift)) {
                                          Loggers.general().info(LOG, "Swift----------8/1/2020  " + swift);
                                          validationDetails.addError(ErrorType.Other, "Please do not change the principal party[CM]");
                                    } else {
                                          Loggers.general().info(LOG, "Swift----------8/1/2020  " + swift);
                                    }
                              }
                        } catch (Exception e) {

                        } finally {
                              try {
                                    if (rs1 != null)
                                          rs1.close();
                                    if (ps1 != null)
                                          ps1.close();
                                    if (con != null)
                                          con.close();
                              } catch (SQLException e) {

                                    e.printStackTrace();
                              }
                        }
                  }

                  // Limit ID change error
                  if ((getMajorCode().equalsIgnoreCase("IGT"))
                              && (getMinorCode().equalsIgnoreCase("NAIG") || getMinorCode().equalsIgnoreCase("NJIG"))
                              && (step_csm.equalsIgnoreCase("CBS Maker 1") || step_csm.equalsIgnoreCase("CBS Authoriser"))) {
                        try {
                              Loggers.general().info(LOG, "result2----------9/1/2020  ");
                              // String facility = getDriverWrapper().getEventFieldAsText("1FID", "s",
                              // "").trim();
                              // String eventCode = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
                              String limit = getDriverWrapper().getEventFieldAsText("PUL4", "s", "").trim();
                              Loggers.general().info(LOG, "eventCode----------9/1/2020  " + eventCode);
                              Loggers.general().info(LOG, "limit----------9/1/2020  " + limit);
                              String result = null;
                              String result2 = null;
                              // Loggers.general().info(LOG,"result----------9/1/2020 "+result);
                              con = getConnection();
                              String query = "SELECT trim(ecm.FACILTYID) FROM master mas , ecmstatus ecm WHERE mas.KEY97=ecm.MASTER_KEY AND mas.master_ref ='"
                                          + masterref + "'";
                              Loggers.general().info(LOG, "query----------9/1/2020  " + query);
                              ps1 = con.prepareStatement(query);
                              rs1 = ps1.executeQuery();
                              while (rs1.next()) {
                                    result = rs1.getString(1);

                                    Loggers.general().info(LOG, "result----------9/1/2020  " + result);

                              }
                              // Loggers.general().info(LOG,"result----------9/1/2020 "+result);

                              /*
                               * String query1 =
                               * "SELECT SUBSTR(XMLTEXT,INSTR(XMLTEXT,'<id><![CDATA[') +LENGTH('<id><![CDATA['), INSTR(XMLTEXT,']]></id>')-INSTR(XMLTEXT,'<id><![CDATA[')-LENGTH('<id><![CDATA[')) AS FACILITY_ID"
                               * +" FROM XMLAPISTO WHERE TYPE = 'FA' AND XMLTEXT LIKE ('%"+masterref+eventCode
                               * +"%')";
                               */
//query changed taking only live events
                              String query1 = "SELECT TO_CHAR(SUBSTR(XMLTEXT,INSTR(XMLTEXT,'<id><![CDATA[') +LENGTH('<id><![CDATA['), INSTR(XMLTEXT,']]></id>')-INSTR(XMLTEXT,'<id><![CDATA[')-LENGTH('<id><![CDATA['))) AS FACILITY_ID"
                                          + " FROM UBZONE.XMLAPISTO XMLAPI,UBZONE.posting post WHERE TYPE = 'FA' AND TO_NUMBER( TO_CHAR(SUBSTR(XMLAPI.XMLTEXT,INSTR(XMLAPI.XMLTEXT,'<postingKey>')+12,INSTR(SUBSTR(XMLAPI.XMLTEXT,INSTR(XMLAPI.XMLTEXT,'<postingKey>')+12),'</postingKey>')-1 )))= post.key97"
                                          + " AND XMLTEXT LIKE ('%" + masterref + eventCode + "%')";

                              Loggers.general().info(LOG, "query----------9/1/2020  " + query1);
                              ps = con.prepareStatement(query1);
                              rs = ps.executeQuery();
                              while (rs.next()) {
                                    result2 = rs.getString(1);

                                    Loggers.general().info(LOG, "result----------9/1/2020  " + result2);

                              }

                              if (result != null && result2 != null && !result.equalsIgnoreCase(result2)
                                          && (getMinorCode().equalsIgnoreCase("NAIG"))) {
                                    Loggers.general().info(LOG, "result2----------9/1/2020  " + result2);

                                    validationDetails.addError(ErrorType.Other, "Limit node cannot be changed [CM]");
                              }
                              if (result != null && result2 != null && !result.equalsIgnoreCase(result2)
                                          && (getMinorCode().equalsIgnoreCase("NJIG")) && (!limit.equalsIgnoreCase("Yes"))) {
                                    Loggers.general().info(LOG, "limit----------9/1/2020  " + limit);

                                    validationDetails.addError(ErrorType.Other, "Limit node cannot be changed [CM]");
                              }
                        }

                        catch (Exception e) {

                        } finally {
                              try {
                                    if (rs1 != null)
                                          rs1.close();
                                    if (ps1 != null)
                                          ps1.close();
                                    if (rs2 != null)
                                          rs2.close();
                                    if (ps2 != null)
                                          ps2.close();
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
                  /// error configured for duplicate events
                  try {
                        int cnt = 0;
                        Loggers.general().info(LOG, "duplicate reference");
                        cnt = getduplicate();
                        Loggers.general().info(LOG, "count" + cnt);
                        if (cnt > 1) {
                              Loggers.general().info(LOG, "duplicate reference");
                              validationDetails.addError(ErrorType.Other,
                                          "The event reference no already exsist kindly abort and recreate the event [CM]");
                        }
                  } catch (Exception ee) {
                        ee.printStackTrace();
                        Loggers.general().info(LOG, "duplicate reference" + ee.getMessage());

                  }

                  String creation = getDriverWrapper().getEventFieldAsText("ECMC", "s", "").trim();
                  Loggers.general().info(LOG, "Manual for EXP and CAN --------> " + creation);
                  // Validation for cancel and expire
                  if (getMajorCode().equalsIgnoreCase("IGT")
                              && ((getMinorCode().equalsIgnoreCase("XIG") && creation.equalsIgnoreCase("M"))
                                          || getMinorCode().equalsIgnoreCase("NIG"))) {
                        try {
                              int count = 0;
                              String Masterref = getDriverWrapper().getEventFieldAsText("MST", "r", "").trim();// ECMC
                              // String
                              // creation1=getDriverWrapper().getEventFieldAsText("ECMC","r","").trim();
                              Loggers.general().info(LOG, "Masterref count===>" + Masterref);
                              con = getConnection();
                              String query1 = "SELECT COUNT(*) FROM master mas, baseevent bev   WHERE mas.KEY97     =bev.MASTER_KEY AND mas.master_ref  ='"
                                          + Masterref + "'" + " AND bev.refno_pfix IN ('CAN','EXP')   AND bev.status      ='i'";
                              Loggers.general().info(LOG, "Query for EXP and CAN --------> " + query1);
                              ps1 = con.prepareStatement(query1);

                              rs1 = ps1.executeQuery();
                              while (rs1.next()) {
                                    count = rs1.getInt(1);
                                    Loggers.general().info(LOG, "Count for EXP and CAN --------> " + count);
                              }
                              if (count == 2) {
                                    validationDetails.addError(ErrorType.Other,
                                                "Cancel and expire event are in inprogress abort one and process the other [CM]");

                              }

                        } catch (Exception e) {

                        } finally {
                              surrenderDB(con, ps1, rs1);
                        }
                  }
                  // CR220 Changes Startes here
                  String subProductype = getDriverWrapper().getEventFieldAsText("PTP", "s", "");
                  if (getMajorCode().equalsIgnoreCase("IGT") && (subProductype.equalsIgnoreCase("BCR")
                              || subProductype.equalsIgnoreCase("IGP") || subProductype.equalsIgnoreCase("IGF"))) {
                        System.out.println("CR-220 Validation starts here");
                        try {
                              con = getConnection();
                              String query = "select count(*) from master mas, BASEEVENT bev, warnmsg wm where mas.key97=BEV.MASTER_KEY and bev.key97=wm.associ30 and WM.PARTCODE = 'RtTol' and MAS.MASTER_REF = '"
                                          + MasterReference + "' and BEV.REFNO_PFIX= '" + evnt + "' and BEV.REFNO_SERL = '" + evvcount
                                          + "'";
                              int count = 0;
                              ps1 = con.prepareStatement(query);
                              rs1 = ps1.executeQuery();
                              while (rs1.next()) {
                                    count = rs1.getInt(1);
                              }

                              if (count > 0 && (step_csm.equalsIgnoreCase("CBS Authoriser")
                                          || step_csm.equalsIgnoreCase("CBS Maker 1"))) {
                                    System.out.println("CR-220 Validation setting up Error");
                                    validationDetails.addError(ErrorType.Other, "FX deal rate is outside specified tolerance [CM]");
                              } else {
                              }

                        } catch (Exception e) {
                              System.out.println("CR-220 Validation Exception occured");
                              e.printStackTrace();
                        }

                        finally {
                              try {
                                    if (rs1 != null)
                                          rs1.close();
                                    if (ps1 != null)
                                          ps1.close();
                                    if (con != null)
                                          con.close();
                              } catch (SQLException e) {
                                    e.printStackTrace();
                              }
                        }
                        System.out.println("CR-220 Validation starts here");
                  }
                  // CR220 Changes Ends here

                  // CR 215 starts here

                  if ((getMinorCode().equalsIgnoreCase("BIG"))
                              && (step_csm.equalsIgnoreCase("CBS Maker 1") || step_csm.equalsIgnoreCase("CBS Authoriser"))) {

                        int getAccountFromTI = 0;

                        try {
                              getAccountFromTI = getAccountFromTI();

                              System.out.println("Result getAccountFromTI-->" + getAccountFromTI);

                              if (getAccountFromTI > 0) {
                                    validationDetails.addError(ErrorType.Other,
                                                "Manual book keeping entries not allowed for these GL Accounts [CM]");
                              }

                        } catch (Exception e) {
                              System.out.println("Exception in getAcoount-->" + e.getMessage());
                              e.printStackTrace();
                        }

                  }

                  // CR 215 ends here

                  try {

                        // getPostingFxrate();
                        // Vaisak 12-02-2022
                        if ((getMajorCode().equalsIgnoreCase("IGT"))
                                    && (getMinorCode().equalsIgnoreCase("LIG") || getMinorCode().equalsIgnoreCase("OIG"))) {
                              DecimalFormat df = new DecimalFormat("##,##,##0.##");
                              String amountStr = getDriverWrapper().getEventFieldAsText("AMPR", "v", "m");
                              String amountCcy = getDriverWrapper().getEventFieldAsText("AMPR", "v", "c");
                              String intAmountStr = getDriverWrapper().getEventFieldAsText("cAKB", "v", "m");
                              String intAmountCcy = getDriverWrapper().getEventFieldAsText("cAKB", "v", "c");
                              BigDecimal amount = new BigDecimal(amountStr);
                              BigDecimal intAmount = new BigDecimal(intAmountStr);
                              BigDecimal totalAmount = new BigDecimal(0);
                              if (!intAmountStr.equalsIgnoreCase("") && intAmountStr.length() > 0
                                          && amountCcy.equalsIgnoreCase(intAmountCcy)) {

                                    totalAmount = intAmount.add(amount);
                                    String value1 = df.format(totalAmount) + " " + amountCcy;
                                    getPane().setTOTLOAM(value1);
                              }

                        }

                  } catch (Exception e) {
                        e.printStackTrace();
                        // Loggers.general().info(LOG,"Exception update" + e.getMessage());
                  }

                  if ((getMajorCode().equalsIgnoreCase("IGT")) && (getMinorCode().equalsIgnoreCase("IIG"))) {
                        try {
                              getPurposeAdv();
                              getNostroDetails();
                              System.out.println("Inside Advance purpose code");
                        } catch (Exception e) {
                              e.printStackTrace();
                              // Loggers.general().info(LOG,"Exception update" + e.getMessage());
                        }
                        try {
                              getBOEWarning(validationDetails);
                        } catch (Exception e) {
                              e.printStackTrace();
                        }
                  }
                  if ((getMajorCode().equalsIgnoreCase("IGT"))
                              && (getMinorCode().equalsIgnoreCase("LIG") || getMinorCode().equalsIgnoreCase("NJIG"))) {

                        try {
                              String masReference = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                              String eventref = getDriverWrapper().getEventFieldAsText("EVR", "r", "");
                              String custId = getDriverWrapper().getEventFieldAsText("PRM", "p", "cu").trim();
                              String uniqueId = getDriverWrapper().getEventFieldAsText("cBUB", "s", "").trim();
                              String branch = getDriverWrapper().getEventFieldAsText("BIN", "b", "c");
                              String eventCode1 = getDriverWrapper().getEventFieldAsText("PCO", "s", "").trim();
                              String inputXml = "<?xml version=\"1.0\" standalone=\"yes\"?><ServiceRequest xmlns:m='urn:messages.service.ti.apps.tiplus2.misys.com' xmlns='urn:control.services.tiplus2.misys.com' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>"
                                          + "<RequestHeader>" + "<Service>Treasury</Service>" + "<Operation>Validate</Operation>"
                                          + "<Credentials>" + "<Name>Name</Name>" + "<Password>Password</Password>"
                                          + "<Certificate>Certificate</Certificate>" + "<Digest>Digest</Digest>" + "</Credentials>"
                                          + "<ReplyFormat>FULL</ReplyFormat>" + "<NoRepair>Y</NoRepair>"
                                          + "<NoOverride>Y</NoOverride>" + "<CorrelationId>CorrelationId</CorrelationId>"
                                          + "<TransactionControl>NONE</TransactionControl>" + "</RequestHeader>"
                                          + "<TreasuryValidateRequest>" + "<MASTERREF>" + masReference + "</MASTERREF>" + "<EVENTREF>"
                                          + eventref + "</EVENTREF>" + "<CUSTID>" + custId + "</CUSTID>" + "<UNIQUEID>" + uniqueId
                                          + "</UNIQUEID>" + "<INPUTBRANCH>" + branch + "</INPUTBRANCH>" + "<EVENTCODE>" + eventCode1
                                          + "</EVENTCODE>" + "<VALIDATIONTYPE>BOVERIFY</VALIDATIONTYPE>"
                                          + "</TreasuryValidateRequest></ServiceRequest>";

                              ThemeTransportClient aClient = new ThemeTransportClient();
                              String resultXml = aClient.invoke("Treasury", "Validate", inputXml);

                              String errMsg = getTagValue(resultXml, "ERRORMSG");
                              String warningMsg = getTagValue(resultXml, "WARNMSG");
                              if (errMsg != null && !errMsg.trim().isEmpty()) {
                                    {
                                          validationDetails.addError(ErrorType.Other, errMsg);
                                    }
                              } else if (warningMsg != null && !warningMsg.trim().isEmpty()) {
                                    {
                                          validationDetails.addWarning(WarningType.Other, warningMsg);
                                    }
                              }
                        } catch (Exception e) {
                              e.printStackTrace();
                        }
                        try {
                              String contractReference = getDriverWrapper().getEventFieldAsText("FOCR", "s", "");
                              // String pay = getDriverWrapper().getEventFieldAsText("ACT", "s", "");
                              if (contractReference == null || contractReference.isEmpty()
                                          || contractReference.trim().equals("")) {
                                    if ((getMajorCode().equalsIgnoreCase("IGT")) && (getMinorCode().equalsIgnoreCase("LIG"))) {
                                          String subproCode = getDriverWrapper().getEventFieldAsText("PTP", "s", "").trim();
                                          String[] domesticSubProduct = {"I3D", "I6D", "I8D", "I1D", "I2D", "I4D", "I5D",
                                                      "I7D", "O1D", "O2D", "O4D", "O5D", "O7D", "O8D", "O9D", "O0D"};
                                          if (!Arrays.asList(domesticSubProduct).contains(subproCode)) {
                                                validationDetails.addError(ErrorType.Other,
                                                            "Reference Contract Number/Deal ID is not entered in Settlements tab[CM]");
                                          }

                                    }
                                    if ((getMajorCode().equalsIgnoreCase("IGT")) && (getMinorCode().equalsIgnoreCase("NJIG"))) {
                                          validationDetails.addWarning(WarningType.Other,
                                                      "Reference Contract Number/Deal ID is not entered in Settlements tab[CM]");

                                    }
                              }
                        } catch (Exception ee) {
                              ee.printStackTrace();
                              // Loggers.general () .info(LOG,"PCR/PCF Exception" + ee.getMessage());

                        }
                  }
//                                  try {
//                                        String contractReference = getDriverWrapper().getEventFieldAsText("FOCR", "s", "");
//                                        if(contractReference==null || contractReference.isEmpty() || contractReference.trim().equals("")) {
//                                              validationDetails.addError(ErrorType.Other, "Reference Contract Number/Deal ID is not entered in Settlements tab[CM]");
//                                        }
//                                  }
//                                  catch (Exception ee) {
//                                        ee.printStackTrace();
//                                        //Loggers.general () .info(LOG,"PCR/PCF Exception" + ee.getMessage());
//
//                                  }

                  if ((getMajorCode().equalsIgnoreCase("IGT"))
                              && (getMinorCode().equalsIgnoreCase("IIG") || getMinorCode().equalsIgnoreCase("NAIG"))) {
                        try {
                              long numdays = 0, tendays = 0, claimdiff = 0;
                              LocalDate exp = null;
                              LocalDate shp = null;
                              LocalDate eff = null;
                              LocalDate claim = null;
                              DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                              String expDate = getDriverWrapper().getEventFieldAsText("EXP", "d", "");
                              String shpDate = getDriverWrapper().getEventFieldAsText("cBEF", "d", "");
                              String effDate = getDriverWrapper().getEventFieldAsText("cBJX", "d", "");
                              String claimDate = getDriverWrapper().getEventFieldAsText("cAIT", "d", "");
                              if (!expDate.trim().equalsIgnoreCase("") && expDate != null) {
                                    exp = LocalDate.parse(expDate, formatters);
                                    if (!shpDate.trim().equalsIgnoreCase("") && shpDate != null) {
                                          shp = LocalDate.parse(shpDate, formatters);
                                          numdays = ChronoUnit.DAYS.between(shp, exp);
                                    }
                                    if (!effDate.trim().equalsIgnoreCase("") && effDate != null) {
                                          eff = LocalDate.parse(effDate, formatters);
                                          tendays = ChronoUnit.DAYS.between(eff, exp);
                                    }
                                    if (!claimDate.trim().equalsIgnoreCase("") && claimDate != null) {
                                          claim = LocalDate.parse(claimDate, formatters);
                                          claimdiff = ChronoUnit.DAYS.between(exp, claim);
                                    }
                              }

                              System.out
                                          .println("Number of days: " + exp + " " + shp + " " + eff + " " + numdays + " " + tendays);
                              if (numdays >= 0) {
                                    getPane().setNUMDAYS(String.valueOf(numdays));
                              }
                              if (tendays >= 0) {
                                    getPane().setTENDAYS(String.valueOf(tendays));
                              }
                              if (claimdiff >= 0) {
                                    getPane().setGRACEPER(String.valueOf(claimdiff));
                              }
                        } catch (Exception e) {
                              e.printStackTrace();
                              // System.out.println("outside BUYERS data:"+e);

                        }
                        try {
                              System.out.println("inside ifsc fetch");
                              getIFSC();
                        } catch (Exception e) {
                              e.printStackTrace();
                              // System.out.println("outside BUYERS data:"+e);

                        }
                        try {
                              getLimitError(validationDetails);
                        } catch (Exception e) {
                              e.printStackTrace();
                              // System.out.println("outside BUYERS data:"+e);

                        }
                  }
                  if ((getMajorCode().equalsIgnoreCase("IGT"))
                              && (getMinorCode().equalsIgnoreCase("IIG") || getMinorCode().equalsIgnoreCase("LIG"))) {
                        try {
                              getCountryRisk();
                        } catch (Exception e) {
                              e.printStackTrace();
                        }
                        try {
                              getPostingBranch(validationDetails);
                        } catch (Exception e) {
                              e.printStackTrace();
                        }
                  }

            }

//          DMS field
            try {
                  String getDmsreferncelink = getDmslink();

                  ExtendedHyperlinkControlWrapper Dmslink = getPane().getCtlDMSReferenceIGTISSclayHyperlink();
                  Dmslink.setUrl(getDmsreferncelink);

                  ExtendedHyperlinkControlWrapper Dmslink1 = getPane().getCtlDMSReferenceIMPGUAAMDclayHyperlink();
                  Dmslink1.setUrl(getDmsreferncelink);

                  ExtendedHyperlinkControlWrapper Dmslink2 = getPane().getCtlDMSReferenceOGTINVOCclayHyperlink();
                  Dmslink2.setUrl(getDmsreferncelink);

                  ExtendedHyperlinkControlWrapper Dmslink3 = getPane().getCtlDMSReferenceIGTCORRElay_2_2Hyperlink();
                  Dmslink3.setUrl(getDmsreferncelink);

                  ExtendedHyperlinkControlWrapper Dmslink4 = getPane().getCtlDMSReferenceOGTISSclay_2_2Hyperlink();
                  Dmslink4.setUrl(getDmsreferncelink);

                  ExtendedHyperlinkControlWrapper Dmslink5 = getPane().getCtlDMSReferenceIMPGUAAMDclay_2_2Hyperlink();
                  Dmslink5.setUrl(getDmsreferncelink);

                  ExtendedHyperlinkControlWrapper Dmslink6 = getPane().getCtlDMSReferenceIGTRIGclayHyperlink();
                  Dmslink6.setUrl(getDmsreferncelink);

                  ExtendedHyperlinkControlWrapper Dmslink7 = getPane().getCtlDMSReferenceIMPGUAOUTCLAcalyHyperlink();
                  Dmslink7.setUrl(getDmsreferncelink);

                  System.out.println("getDmsreferncelink custom validation class " + getDmsreferncelink);
                  Loggers.general().info(LOG, "getDmsreferncelink " + getDmsreferncelink);

            } catch (Exception e) {

                  System.out.println("getDmsreferncelink exception custom validation class " + e.getStackTrace());
                  e.getStackTrace();
            }
            System.out.println("DMS FIELD in custom validation class");

//                      end DMS link

//                      DMS repo field
            try {
                  String getDmsreferncelink = getRepolink();

                  ExtendedHyperlinkControlWrapper Dmslink = getPane().getCtlDMSCUSTREPOIGTISSclayHyperlink();
                  Dmslink.setUrl(getDmsreferncelink);

                  ExtendedHyperlinkControlWrapper Dmslink1 = getPane().getCtlDMSCUSTREPOIMPGUAAMDclayHyperlink();
                  Dmslink1.setUrl(getDmsreferncelink);

                  ExtendedHyperlinkControlWrapper Dmslink2 = getPane().getCtlDMSCUSTREPOOGTINVOCclayHyperlink();
                  Dmslink2.setUrl(getDmsreferncelink);

                  ExtendedHyperlinkControlWrapper Dmslink3 = getPane().getCtlDMSCUSTREPOIGTCORRElay_2_2Hyperlink();
                  Dmslink3.setUrl(getDmsreferncelink);

                  ExtendedHyperlinkControlWrapper Dmslink4 = getPane().getCtlDMSCUSTREPOOGTISSclay_2_2Hyperlink();
                  Dmslink4.setUrl(getDmsreferncelink);

                  ExtendedHyperlinkControlWrapper Dmslink5 = getPane().getCtlDMSCUSTREPOIMPGUAAMDclay_2_2Hyperlink();
                  Dmslink5.setUrl(getDmsreferncelink);

                  ExtendedHyperlinkControlWrapper Dmslink6 = getPane().getCtlDMSCUSTREPOIGTRIGclayHyperlink();
                  Dmslink6.setUrl(getDmsreferncelink);

                  ExtendedHyperlinkControlWrapper Dmslink7 = getPane().getCtlDMSCUSTREPOIMPGUAOUTCLAcalyHyperlink();
                  Dmslink7.setUrl(getDmsreferncelink);

                  System.out.println("getDmsReporeferncelink custom validation class " + getDmsreferncelink);
                  Loggers.general().info(LOG, "getDmsReporeferncelink " + getDmsreferncelink);

            } catch (Exception e) {

                  System.out.println("getDmsReporeferncelink exception custom Onvalidation class " + e.getStackTrace());
                  e.getStackTrace();
            }
            System.out.println("DMS repo FIELD in custom validation class");

//                                  end DMS repo link

      }

      private String getSTATECOD() {
            // TODO Auto-generated method stub
            return null;
      }

      private String getString(int i) {
            // TODO Auto-generated method stub
            return null;
      }

      private int getInt(int i) {
            // TODO Auto-generated method stub
            return 0;
      }

      
      public static String randomCorrelationId() {
            // Loggers.general().info(LOG,"randomCorrelationId generate");
            return UUID.randomUUID().toString();
      }

      public static String difference(String a, String b) throws ParseException {
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");
            Date date1 = format.parse(a);
            Date date2 = format.parse(b);
            long diff = date2.getTime() - date1.getTime();
            double z = Math.abs(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
            // //Loggers.general().info(LOG,"values " + Math.abs(TimeUnit.DAYS.convert(diff,
            // TimeUnit.MILLISECONDS)));
            return Double.toString(z);

      }
      
      
}