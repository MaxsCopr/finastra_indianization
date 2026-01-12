package com.misys.tiplus2.customisation.extension;

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
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.bs.theme.transport.client.ThemeTransportClient;
import com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP;
import com.misys.tiplus2.enigma.customisation.AdhocQuery;
import com.misys.tiplus2.enigma.customisation.control.ExtendedHyperlinkControlWrapper;
import com.misys.tiplus2.customisation.entity.ExtEventAccountRealisation;
import com.misys.tiplus2.customisation.entity.ExtEventAdvanceTable;
import com.misys.tiplus2.customisation.entity.ExtEventBOECAP;
import com.misys.tiplus2.customisation.entity.ExtEventInvoiceData;
import com.misys.tiplus2.customisation.entity.ExtEventInvoicedetailsLC;
import com.misys.tiplus2.customisation.entity.ExtEventInvoicelc;
import com.misys.tiplus2.customisation.entity.ExtEventLienMarking;
import com.misys.tiplus2.customisation.entity.ExtEventLoanDetails;
import com.misys.tiplus2.customisation.entity.ExtEventShippingTable;
import com.misys.tiplus2.customisation.entity.ExtEventShippingdetailslc;
import com.misys.tiplus2.customisation.pane.EventPane;
import com.misys.tiplus2.enigma.customisation.AdhocQuery;
import com.misys.tiplus2.enigma.customisation.control.ExtendedHyperlinkControlWrapper;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails.ErrorType;
import com.misys.tiplus2.enigma.customisation.validation.ValidationDetails.WarningType;
import com.misys.tiplus2.foundations.lang.logging.Loggers;
import com.misys.tiplus2.persistent.database.GetForUpdateResults;

public class FreeNegLC extends ConnectionMaster {

      @SuppressWarnings("unused")
      private static final Logger logger = Logger
                  .getLogger(ConnectionMaster.class.getName());

      Connection con, con1, conne = null;
      PreparedStatement ps1, ps, ps2, dmsp, pst, ship_prepare, pres = null;
      ResultSet rs1, rs, rs2, dmsr, rst, ship_result, resSet = null;
      String dmsstr = null;
      double marginB = 0.0;

      // ConnectionMaster conMas = new ConnectionMaster();

      // @Override
      public boolean onPostInitialise()

      {

            String strPropName = "MigrationDone";
            String dailyval = "";
            @SuppressWarnings("unchecked")
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryRCODE = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strPropName + "'");
            // //System.out.println("ADDDailyTxnLimit initially-------->");
            EXTGENCUSTPROP PROPCode = queryRCODE.getUnique();
            if (PROPCode != null) {

                  dailyval = PROPCode.getPropval();

            } else {
                  // // System.out.println("ADDDailyTxnLimit is empty-------->");

            }
            String eventStatus = getDriverWrapper().getEventFieldAsText("EVSS",
                        "s", "");

            // System.out.println("Event status in service class====>" +
            // eventStatus);
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
                        // System.out.println("ADDDailyTxnLimit is empty-------->");

                  }

                  String step_csm = getDriverWrapper().getEventFieldAsText("CSID",
                              "s", "").trim();

                  // TST
                  try {
                        String val = "DMSTS";
                        String TSTHyperlink = Link(val);

                        ExtendedHyperlinkControlWrapper dmsh1 = getPane()
                                    .getCtlTSTFREENEGLCOUTPclayHyperlink();
                        dmsh1.setUrl(TSTHyperlink);

                        ExtendedHyperlinkControlWrapper dmsha1 = getPane()
                                    .getCtlTSTFREENEGLCHyperlink();
                        dmsha1.setUrl(TSTHyperlink);

                        ExtendedHyperlinkControlWrapper dmshC = getPane()
                                    .getCtlTSTFREENEGADJHyperlink();
                        dmshC.setUrl(TSTHyperlink);

                  } catch (Exception ees) {
                        System.out
                                    .println("Exception Transaction link called in service"
                                                + ees.getMessage());
                  }

                  // LINK PASSING
                  // PRESHIPMENT LINK
                  try {

                        String Preshipment = getHyperPreshipment();
                        ExtendedHyperlinkControlWrapper dmsh5 = getPane()
                                    .getCtlPreshipmentFREENEGLCOUTPclayHyperlink();
                        dmsh5.setUrl(Preshipment);

                  } catch (Exception e) {
                        // System.out.println("Exception in setting PreShipment url " +
                        // e.getMessage());
                  }
                  // InwardLink
                  // SFMS
                  try {

                        String getHyperSFMS = getIFSCSEARCH();

                        ExtendedHyperlinkControlWrapper ifscilc1 = getPane()
                                    .getCtlSFMSFREENEGLCHyperlink();
                        ifscilc1.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper ifscilc2 = getPane()
                                    .getCtlSFMSFREENEGLCOUTPclayHyperlink();
                        ifscilc2.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdvD = getPane()
                                    .getCtlSFMSFREENEGADJHyperlink();
                        sfmsExpAdvD.setUrl(getHyperSFMS);

                  } catch (Exception e) {
                        System.out.println("Exception IFSCSEARCH" + e.getMessage());
                  }

                  // WORKFLOW CHECKLIST CSM

                  try {
                        String Hyperreferel12 = getWorkflow().trim();
                        ExtendedHyperlinkControlWrapper csmreftrack1 = getPane()
                                    .getCtlCSMCHECKLISTFREENEGLCHyperlink();
                        csmreftrack1.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper csmreftrackamd1 = getPane()
                                    .getCtlCSMCHECKLISTFREENEGLCOUTPclayHyperlink();
                        csmreftrackamd1.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper csmreftrackamd2 = getPane()
                                    .getCtlCSMCHECKLISTFREENEGADJHyperlink();
                        csmreftrackamd2.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper csmreftrackamd4 = getPane()
                                    .getCtlCSMCHECKLISTELCAMDclayHyperlink();
                        csmreftrackamd4.setUrl(Hyperreferel12);

//                      ExtendedHyperlinkControlWrapper csmreftrackamd6 = getPane()
//                                  .getCtlCSMCHECKLISTELCSETTclayHyperlink();
//                      csmreftrackamd6.setUrl(Hyperreferel12);

                  } catch (Exception ees) {
                        // System.out.println("WORKFLOW CHECKLIST--------->" +
                        // ees.getMessage());
                  }

                  // REFERE TRACKING CSM
                  try {

                        String Hyperreferel = getReferealtracking().trim();
                        ExtendedHyperlinkControlWrapper csmreftrack = getPane()
                                    .getCtlCSMREFERALFREENEGLCHyperlink();
                        csmreftrack.setUrl(Hyperreferel);

                        ExtendedHyperlinkControlWrapper csmreftrackamd = getPane()
                                    .getCtlCSMREFERALFREENEGLCOUTPclayHyperlink();
                        csmreftrackamd.setUrl(Hyperreferel);

                        ExtendedHyperlinkControlWrapper csmreftrackamd1 = getPane()
                                    .getCtlCSMREFRALIMPADVclayHyperlink();
                        csmreftrackamd1.setUrl(Hyperreferel);

                        ExtendedHyperlinkControlWrapper csmreftrackamd3 = getPane()
                                    .getCtlCSMREFRALFREENEGADJHyperlink();
                        csmreftrackamd3.setUrl(Hyperreferel);

                        ExtendedHyperlinkControlWrapper csmreftrackamd5 = getPane()
                                    .getCtlCSMREFRALILCSETTclayHyperlink();
                        csmreftrackamd5.setUrl(Hyperreferel);

                        ExtendedHyperlinkControlWrapper csmreftrackamd7 = getPane()
                                    .getCtlCSMREFRALILCCANHyperlink();
                        csmreftrackamd7.setUrl(Hyperreferel);

                        ExtendedHyperlinkControlWrapper csmreftrackk1 = getPane()
                                    .getCtlCSMREFRALELCADVclayHyperlink();
                        csmreftrackk1.setUrl(Hyperreferel);

//                      ExtendedHyperlinkControlWrapper csmreftrackk3 = getPane()
//                                  .getCtlCSMREFRALELCDPclayHyperlink();
//                      csmreftrackk3.setUrl(Hyperreferel);

                        ExtendedHyperlinkControlWrapper csmreftrackk5 = getPane()
                                    .getCtlCSMREFRALELCADJclayHyperlink();
                        csmreftrackk5.setUrl(Hyperreferel);

                        ExtendedHyperlinkControlWrapper csmreftrackk7 = getPane()
                                    .getCtlCSMREFRALELCAMDclayHyperlink();
                        csmreftrackk7.setUrl(Hyperreferel);

//                      ExtendedHyperlinkControlWrapper csmreftrackk9 = getPane()
//                                  .getCtlCSMREFRALELCSETTclayHyperlink();
//                      csmreftrackk9.setUrl(Hyperreferel);

                  } catch (Exception ees) {
                        // System.out.println("REFERE TRACKING--------->" +
                        // ees.getMessage());
                  }

                  // Workflow CBS
                  try {
                        String Hyperreferel12 = getWorkflowCBS().trim();
                        ExtendedHyperlinkControlWrapper cpcreftrack1 = getPane()
                                    .getCtlCPCCHECKFREENEGLCHyperlink();
                        cpcreftrack1.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd1 = getPane()
                                    .getCtlCPCCHECKFREENEGLCOUTPclayHyperlink();
                        cpcreftrackamd1.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd3 = getPane()
                                    .getCtlCPCCHECKFREENEGADJHyperlink();
                        cpcreftrackamd3.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd5 = getPane()
                                    .getCtlCPCCHECKELCAMDclayHyperlink();
                        cpcreftrackamd5.setUrl(Hyperreferel12);
//                      ExtendedHyperlinkControlWrapper cpcreftrackamd7 = getPane()
//                                  .getCtlCPCCHECKELCSETTclayHyperlink();
//                      cpcreftrackamd7.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd9 = getPane()
                                    .getCtlCPCCHECKOUTDOCCOLADJclayHyperlink();
                        cpcreftrackamd9.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd11 = getPane()
                                    .getCtlCPCCHECKOUTDOCCOLAMDclayHyperlink();
                        cpcreftrackamd11.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd13 = getPane()
                                    .getCtlCPCCHECKINWDOCCOLPAYclayHyperlink();
                        cpcreftrackamd13.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd15 = getPane()
                                    .getCtlCPCCHECKINWDOCCOLADJclayHyperlink();
                        cpcreftrackamd15.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd17 = getPane()
                                    .getCtlCPCCHECKINWDOCCOLAMDclayHyperlink();
                        cpcreftrackamd17.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper cpcreftrack21 = getPane()
                                    .getCtlCPCCHECKIMPLCclayHyperlink();
                        cpcreftrack21.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd22 = getPane()
                                    .getCtlCPCCHECKILCAMDclayHyperlink();
                        cpcreftrackamd22.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd23 = getPane()
                                    .getCtlCPCCHECKIMPADVclayHyperlink();
                        cpcreftrackamd23.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd24 = getPane()
                                    .getCtlCPCCHECKIMPLCCLAIMRECclayHyperlink();
                        cpcreftrackamd24.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd25 = getPane()
                                    .getCtlCPCCHECKILCSETTclayHyperlink();
                        cpcreftrackamd25.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd26 = getPane()
                                    .getCtlCPCCHECKILCCANHyperlink();
                        cpcreftrackamd26.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper csmreftrackamd23 = getPane()
                                    .getCtlCPCCHECKEGTADJclayHyperlink();
                        csmreftrackamd23.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper csmreftrackamd24 = getPane()
                                    .getCtlCPCCHECKEGTAMDclayHyperlink();
                        csmreftrackamd24.setUrl(Hyperreferel12);

                  } catch (Exception ees) {
                        // System.out.println("WORKFLOW CHECKLIST WorkflowCBS--------->"
                        // + ees.getMessage());
                  }

                  // Refereal CBS

                  try {
                        String Hyperreferel12 = getReferealtrackingCBS().trim();
                        ExtendedHyperlinkControlWrapper cpcreftrack = getPane()
                                    .getCtlCPCREFERALFREENEGLCHyperlink();
                        cpcreftrack.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd = getPane()
                                    .getCtlCPCREFERALFREENEGLCOUTPclayHyperlink();
                        cpcreftrackamd.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd2 = getPane()
                                    .getCtlCPCREFRALFREENEGADJHyperlink();
                        cpcreftrackamd2.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd4 = getPane()
                                    .getCtlCPCREFERELIMPLCCLAIMRECclayHyperlink();
                        cpcreftrackamd4.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd6 = getPane()
                                    .getCtlCPCREFERELILCSETTclayHyperlink();
                        cpcreftrackamd6.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd8 = getPane()
                                    .getCtlCPCREFERELILCCANHyperlink();
                        cpcreftrackamd8.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper csmreftrackk2 = getPane()
                                    .getCtlCPCREFERELELCADVclayHyperlink();
                        csmreftrackk2.setUrl(Hyperreferel12);
//                      ExtendedHyperlinkControlWrapper csmreftrackk4 = getPane()
//                                  .getCtlCPCREFERELELCDPclayHyperlink();
//                      csmreftrackk4.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper csmreftrackk6 = getPane()
                                    .getCtlCPCREFERELELCADJclayHyperlink();
                        csmreftrackk6.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper csmreftrackk8 = getPane()
                                    .getCtlCPCREFERELELCAMDclayHyperlink();
                        csmreftrackk8.setUrl(Hyperreferel12);
//                      ExtendedHyperlinkControlWrapper csmreftrackk10 = getPane()
//                                  .getCtlCPCREFRALELCSETTclayHyperlink();
//                      csmreftrackk10.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackam = getPane()
                                    .getCtlCPCREFERELOUTDOCCOLADJclayHyperlink();
                        cpcreftrackam.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd12 = getPane()
                                    .getCtlCPCREFERELOUTDOCCOLAMDclayHyperlink();
                        cpcreftrackamd12.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd13 = getPane()
                                    .getCtlCPCREFERELINWDOCCOLPAYclayHyperlink();
                        cpcreftrackamd13.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd15 = getPane()
                                    .getCtlCPCREFERELINWDOCCOLADJclayHyperlink();
                        cpcreftrackamd15.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd17 = getPane()
                                    .getCtlCPCREFERELINWDOCCOLAMDclayHyperlink();
                        cpcreftrackamd17.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper csmreftrackamd23 = getPane()
                                    .getCtlCPCREFERELEGTADJclayHyperlink();
                        csmreftrackamd23.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper csmreftrackamd24 = getPane()
                                    .getCtlCPCREFERELEGTAMDclayHyperlink();
                        csmreftrackamd24.setUrl(Hyperreferel12);
                  } catch (Exception ees) {
                        // System.out.println("REFERE TRACKING REFERE CBS--------->" +
                        // ees.getMessage());
                  }

                  // Currency Conversion
                  try {
                        currencyCalc();
                  } catch (Exception e) {

                  }

                  String rtgsFlag = getDriverWrapper().getEventFieldAsText("cBPJ",
                              "l", "");
                  if (rtgsFlag.equalsIgnoreCase("N")
                              && !rtgsFlag.equalsIgnoreCase("Y")) {
                        // System.out.println("inside if");
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

                  // SENDREF
                  String mas = getDriverWrapper().getEventFieldAsText("MST", "r", "");
                  // String = "0172ELFX0003716";
                  String evt = getDriverWrapper()
                              .getEventFieldAsText("MEVR", "r", "");
                  // String str ="";
                  if (getMinorCode().trim().equalsIgnoreCase("FNF")) {
                        try {
                              // //System.out.println("enter into bill generate no");
                              String str = mas.substring(4, 16);
                              // //System.out.println("str---->" + str);
                              String strevt = evt.substring(0, 1);
                              // //System.out.println("strevt ---->" + strevt);

                              String str11 = evt.substring(3, 6);
                              // //System.out.println("str11 ---->" + str11);
                              String val = str + strevt + str11;
                              // //System.out.println("Total ---->" + val);
                              getWrapper().setBLLREFNO(val);
                              getPane().setBLLREFNO(val);
                        } catch (Exception e) {
                              // System.out.println("exception" + e);
                        }
                  }
                  /*try {

                        String refer = getDriverWrapper().getEventFieldAsText("ISS",
                                    "r", "").trim();
                        getPane().setSENDREF(refer);
                        getWrapper().setSENDREF(refer);
                  } catch (Exception e) {
                        // System.out.println(e.getMessage());

                  }*/
                  // Finance Event Check--TENORDET

                  String finace_event = getDriverWrapper().getEventFieldAsText("BFC",
                              "l", "");
                  if (getMinorCode().equalsIgnoreCase("FNF")) {
                        if (finace_event.equalsIgnoreCase("Y")) {
                              getPane().setTENORDET("FRN");
                              getWrapper().setTENORDET("FRN");
                        } else {
                              getPane().setTENORDET("NO");
                              getWrapper().setTENORDET("NO");
                        }
                  } else if (getMinorCode().equalsIgnoreCase("POF")) {

                        if (finace_event.equalsIgnoreCase("Y")) {
                              getPane().setTENORDET("POD");
                              getWrapper().setTENORDET("POD");
                        }

                  }

                  // REETIN

                  // Non Consituent Borrower -CONSBOR

                  // Non Constituent borrower
                  String constituent = getDriverWrapper().getEventFieldAsText("BEN",
                              "p", "no").trim();
                  // //System.out.println("Non Constituent borrower
                  // constituent--------->" + constituent);
                  if (constituent.length() > 0) {
                        // //System.out.println("Non Constituent borrower
                        // constituent--------->" + constituent);
                        String alpha_val = constituent.replaceAll("[^A-Za-z]+", "");
                        // //System.out.println("alpha_val---->" + alpha_val);
                        if (alpha_val.length() > 0) {
                              String alpha_fin = alpha_val.substring(0, 4);
                              // //System.out.println("Non Constituent borrower
                              // constituent
                              // after convert--------->" + alpha_fin);
                              if (getMajorCode().equalsIgnoreCase("FRN")) {
                                    if (alpha_fin.equalsIgnoreCase("LCUS")) {
                                          getPane().setCONSBORR(true);
                                          // //System.out.println(" Non Constituent
                                          // borrower
                                          // ELC
                                          // if
                                          // loop--------->");
                                    } else {
                                          getPane().setCONSBORR(false);
                                    }
                              } else {
                                    getPane().setCONSBORR(false);

                              }
                        } else {
                              getPane().setCONSBORR(false);
                        }
                  } else {

                        getPane().setCONSBORR(false);

                        // //System.out.println(" Non Constituent borrower is
                        // empty--------->");
                  }
                  

                  // FORMLIM
                  String eventCode = getDriverWrapper().getEventFieldAsText("EVR",
                              "r", "").trim(); // ISS001
                  String prd_typ = getDriverWrapper().getEventFieldAsText("PTP", "s",
                              "").trim();
                  String masRefNo = getDriverWrapper().getEventFieldAsText("MST",
                              "r", "").trim();
                  String step_Input = getDriverWrapper().getEventFieldAsText("CSTY",
                              "s", "");
                  String step_log = getDriverWrapper().getEventFieldAsText("ECOI",
                              "s", "").trim();
                  // //System.out.println("Step id for log CSM---->" + step_log);
                  String step_input = getDriverWrapper().getEventFieldAsText("CSID",
                              "s", "").trim();
                  // // System.out.println("step id check for CSM ----->" +
                  // // step_input);
                  String gateway = getDriverWrapper().getEventFieldAsText("FRGI",
                              "l", "");
                  // // System.out.println("gateway flag checking----->" +
                  // // gateway);

                  if (gateway.equalsIgnoreCase("Y")) {
                        getPane().setPERADV("");
                        getPane().setADVREC("");
                        getPane().setNETRECIV("");
                        getPane().setBILLPAY("");
                  }

                  // System.out.println("todo on forcelim");
                  // System.out.println("stepinpu" +step_input);
                  if ((step_input.equalsIgnoreCase("Create") || step_input
                              .equalsIgnoreCase("CSM"))
                              && (getMinorCode().equalsIgnoreCase("POF"))) {
                        // System.out.println("Get force
                        // limit"+getPane().getFORLIM());

                        getPane().setFORLIM(false);
                        // System.out.println("After force
                        // limit"+getPane().getFORLIM());
                  }
                  if ((step_input.equalsIgnoreCase("Create") || step_input
                              .equalsIgnoreCase("CSM"))
                              && (getMinorCode().equalsIgnoreCase("POF") || getMinorCode()
                                          .equalsIgnoreCase("POC"))) {
                        // //System.out.println("Step id for log Create if for log
                        // step---->" + step_log);
                        // System.out.println("Inside if 21/01/2019");
                        getPane().setRTGNFT("");
                        getPane().setRTGSN(false);
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
                        getPane().setRTGSNEFT("");
                        getPane().setUTRNO("");
                        getPane().setNARRTVE("");
                        getPane().setBADDRE_Name("");
                        getPane().setRTGSPART("");

                        getPane().setTENEXT("");
                        getPane().setTENRSN("");
                        getPane().setLETNUM("");
                        getPane().setLETDAT("");
                        getPane().setREEXTIN("");
                        getPane().setEXTDAT("");

                        getPane().setWRAMT("");
                        getPane().setWRAMCU("");
                        getPane().setWRITDATE("");
                        getPane().setWRITNDI("");
                        getPane().setTRANTYP("");
                        getPane().setREALTYP("");
                        getPane().setBOENO("");
                        getPane().setBOEDATE("");
                        getPane().setPOSTDIS("");
                        // getPane().setRATECOV("");
                        // getPane().setRATEDET("");

                        getPane().setEBRCFLAG("");

                        getPane().setREPAYAMT("");
                        getPane().setTOTUSD(0 + " USD");
                        getPane().setTOTINR(0 + " INR");
                        getPane().setTOTEUR(0 + " EUR");
                        getPane().setTOTJPY(0 + " JPY");
                        getPane().setTOTGBP(0 + " GBP");

                  }
                  if (step_Input.equalsIgnoreCase("i")) {
                        try {
                              List<ExtEventShippingTable> shipTable = (List<ExtEventShippingTable>) getWrapper()
                                          .getExtEventShippingTable();
                              // //System.out.println("shipping table for notional
                              // rate---->" + shipTable.size());
                              for (int i = 0; i < shipTable.size(); i++) {

                                    ExtEventShippingTable ship = shipTable.get(i);
                                    if (getMajorCode().equalsIgnoreCase("FRN")
                                                && getMinorCode().equalsIgnoreCase("FNF")) {
                                          BigDecimal shipAmt = new BigDecimal("0");
                                          BigDecimal notVal = new BigDecimal("1");
                                          ship.setREPAYAM(shipAmt);
                                          // System.out.println("repayamount
                                          // 1===>"+ship.getREPAYAM());

                                          // System.out.println("repayamount===>
                                          // 1===>"+shipAmt);
                                          ship.setREPAYAMCurrency(ship.getSHPAMTCurrency());
                                          ship.setSHCOLAM(shipAmt);
                                          ship.setSHCOLAMCurrency(ship.getSHPAMTCurrency());
                                          ship.setNOTIONAL(notVal);
                                    }
                                    String not_str = ship.getNOTIONAL().toString();
                                    // //System.out.println("shipping table for notional
                                    // length====>" + not_str.length());

                                    if (getMinorCode().equalsIgnoreCase("POF")) {
                                          double notional = 1;
                                          BigDecimal rAmt = ship.getREPAYAM();
                                          String shiamtcuy = ship.getSHPAMTCurrency();
                                          String reyamtcuy = ship.getREPAYAMCurrency();
                                          try {
                                                con = getConnection();
                                                String query = "SELECT ETT_SPOTRATE_CAL('"
                                                            + shiamtcuy + "','" + reyamtcuy
                                                            + "') FROM DUAL";
                                                // // System.out.println("Notional rate
                                                // // function
                                                // // " +
                                                // // query);

                                                ps1 = con.prepareStatement(query);
                                                rs1 = ps1.executeQuery();
                                                if (rs1.next()) {
                                                      notional = rs1.getDouble(1);
                                                      // System.out.println("shipping table
                                                      // notional
                                                      // length start===>" + notional);
                                                      // ship.setNOTIONAL(new
                                                      // BigDecimal(notional));
                                                }
                                          } catch (Exception e1) {
                                                // System.out.println("Exception Notional
                                                // rate
                                                // function" + e1.getMessage());
                                          }

                                          String temp_notRate = String.valueOf(notional);

                                          if (null != not_str
                                                      && not_str.equalsIgnoreCase("1")) {
                                                ship.setNOTIONAL(new BigDecimal(temp_notRate));
                                                // // System.out.println("shipping table
                                                // // notional
                                                // // length if loop====>" +
                                                // // ship.getNOTIONAL());

                                                BigDecimal notional_big = new BigDecimal(
                                                            notional);
                                                BigDecimal equi_bill = notional_big
                                                            .multiply(rAmt);
                                                // // System.out.println("Notional rate +
                                                // // Repament
                                                // // amount in big decimal POD---->" +
                                                // // equi_bill);
                                                ship.setEQUBILL(equi_bill);
                                                ship.setEQUBILLCurrency(ship
                                                            .getSHPAMTCurrency());
                                          } else if (null != not_str
                                                      && !temp_notRate.equalsIgnoreCase(not_str)) {
                                                ship.setNOTIONAL(new BigDecimal(not_str));
                                                // // System.out.println("shipping table
                                                // // notional
                                                // // length else if loop====>" +
                                                // // ship.getNOTIONAL());
                                                BigDecimal notional_big = new BigDecimal(
                                                            not_str);
                                                BigDecimal equi_bill = notional_big
                                                            .multiply(rAmt);
                                                // // System.out.println("Notional rate +
                                                // // Repament
                                                // // amount in big decimal POD---->" +
                                                // // equi_bill);
                                                ship.setEQUBILL(equi_bill);
                                                ship.setEQUBILLCurrency(ship
                                                            .getSHPAMTCurrency());

                                          } else {
                                                // // System.out.println("Notional rate is
                                                // // blank---->");
                                          }

                                    }

                              }

                        } catch (Exception e) {
                              // // System.out.println("Exception in validating for
                              // // notional
                              // // rate--->" + e.getMessage());
                        }
                  }

                  if (getMajorCode().equalsIgnoreCase("FRN")
                              && getMinorCode().equalsIgnoreCase("FNF")
                              || getMinorCode().equalsIgnoreCase("POF")) {
                        // PostingCustom post = null;
                        // if(step_csm.equalsIgnoreCase("CSM Maker 1"))
                        // String strPSID =
                        // getDriverWrapper().getPostingFieldAsText("PSID",
                        // "").trim();
                        try {
                              // String relevnt=null;
                              String Mast = getDriverWrapper().getEventFieldAsText(
                                          "MMST", "r", "").trim();
                              String Evnt = getDriverWrapper().getEventFieldAsText("EVR",
                                          "r", "").trim();

                              // System.out.println("MasterReferenceNNum on
                              // post----------->" + Mast);
                              // System.out.println("their Reference on
                              // post----------->" + Evnt);

                              /*
                               * if (dailyval_Log.equalsIgnoreCase("YES")) {
                               *
                               * System.out.println("MasterReference-------->" + Mast);
                               * System.out.println("their Reference-------->" + Evnt); //
                               * System.out.println("PSID----------->" + strPSID); }
                               */
                              con = getConnection();

                              String query = "WITH MAXVAL AS (SELECT row_number() over (ORDER BY BEV.KEY97 asc) as ROWN,"
                                          + "MAS.MASTER_REF,MAS.KEY97 mas_key,"
                                          + "BEV.KEY97 bev_key,BEV.REFNO_PFIX || lpad(BEV.REFNO_SERL,3,0) bev_ref ,LIMBLK FROM"
                                          + " MASTER MAS,BASEEVENT  BEV ,EXTEVENT  EXT WHERE MAS.KEY97=BEV.MASTER_KEY AND BEV.KEY97=EXT.EVENT"
                                          + " and BEV.status<>'a' and trim(LIMBLK) is not null AND MAS.MASTER_REF='"
                                          + Mast
                                          + "'  ORDER BY BEV.KEY97)"
                                          + " SELECT LIMBLK FROM MAXVAL "
                                          + " WHERE ROWN IN (SELECT CASE WHEN '"
                                          + Evnt
                                          + "' lIKE 'DPR%' THEN ROWN ELSE ROWN-1 END  "
                                          + " FROM MAXVAL WHERE bev_ref='" + Evnt + "')";
                              

                              ps1 = con.prepareStatement(query);

                              rs1 = ps1.executeQuery();
                              while (rs1.next()) {

                                    /*
                                     * if (dailyval_Log.equalsIgnoreCase("YES")) { //
                                     * System.out.println("Entering While // Loop=======>");
                                     *
                                     * }
                                     */
                                    // System.out.println("Entering While
                                    // Loop========>");

                                    String prelimit = rs1.getString(1);
                                    System.out.println("Prelimit=======>" + prelimit);

                                    getPane().setPRVLIMBL(prelimit);
                              }
                              System.out.println("Previous limit"
                                          + getPane().getPRVLIMBL());
                              // }
                        } catch (Exception e) {
                              e.printStackTrace();
                              System.out.println("Exception Previous limit blocking"
                                          + e.getMessage());

                        } finally {
                              try {
                                    if (rs1 != null)
                                          rs1.close();
                                    if (ps1 != null)
                                          ps1.close();
                                    if (rs != null)
                                          rs.close();
                                    if (pst != null)
                                          pst.close();
                                    if (con != null)
                                          con.close();
                              } catch (SQLException e) {
                                    // System.out.println("Connection Failed! Check
                                    // output
                                    // console");
                                    e.printStackTrace();
                              }
                        }

                  }

                  // OWNLC

                  try {

                        String kotak_no = getDriverWrapper().getEventFieldAsText("ISS",
                                    "p", "cu").trim();
                        // System.out.println("kotak Ownlc tick--------->" +
                        // kotak_no);

                        if (getMajorCode().equalsIgnoreCase("FRN")) {
                              if ((kotak_no.equalsIgnoreCase("INKKBK") || kotak_no.trim()
                                          .equalsIgnoreCase("11908474"))) {
                                    getPane().setOWNLC(true);
                              } else {
                                    getPane().setOWNLC(false);
                                    /*
                                     * if (dailyval_Log.equalsIgnoreCase("YES")) { //
                                     * System.out.println("kotak Ownlc tick // else------->"
                                     * + kotak_no); }
                                     */
                              }
                        } else {
                              getPane().setOWNLC(false);

                        }
                  } catch (Exception e) {
                        System.out.println("Exception kotak systemOutput--------->"
                                    + e.getMessage());
                  }

                  // PENALRAT -Actual Penal Rate

                  if (getMajorCode().equalsIgnoreCase("FRN")) {
                        try {

                              getactualPenalRate();

                        } catch (Exception e) {
                              // System.out.println("Exception update" +
                              // e.getMessage());
                        }
                  }

                  // GOODS DESCRIPTION

                  if ((getMajorCode().equalsIgnoreCase("FRN") || getMinorCode()
                              .equalsIgnoreCase("FNF"))
                              || (getMajorCode().equalsIgnoreCase("ILC") || getMinorCode()
                                          .equalsIgnoreCase("CRC"))) {
                        try {
                              System.out.println("Inside Goods Start");
                              String Fromcity = getDriverWrapper().getEventFieldAsText(
                                          "SDF", "s", "");
                              // //System.out.println("from city" + Fromcity);
                              String Tocity = getDriverWrapper().getEventFieldAsText(
                                          "SDT", "s", "");
                              // //System.out.println("to city " + Tocity);
                              String gooddesc = getDriverWrapper().getEventFieldAsText(
                                          "cAIJ", "s", "");
                              // //System.out.println("goods description" + gooddesc);
                              String goodcode = getDriverWrapper().getEventFieldAsText(
                                          "cAKM", "s", "");
                              getPane().setDESGOODS(gooddesc);
                              getPane().setGOODT(goodcode);
                              getPane().setSHIPFMLC(Fromcity);
                              getPane().setSHIPTOLC(Tocity);
                              System.out.println("Inside Goods Ends");

                        } catch (Exception e) {

                        }
                  }

                  String csty_id = getDriverWrapper().getEventFieldAsText("CSTY",
                              "s", "");
                  String masterRefNumber = getDriverWrapper().getEventFieldAsText(
                              "MST", "r", "");
                  if ((getMajorCode().equalsIgnoreCase("FRN") || getMinorCode()
                              .equalsIgnoreCase("FNF"))
                              || (getMajorCode().equalsIgnoreCase("ILC") || getMinorCode()
                                          .equalsIgnoreCase("CRC"))
                              && csty_id.equalsIgnoreCase("i")) {
                        try {
                              System.out.println("Inside Goods Start1");
                              // //System.out.println("entered from and to population
                              // ");

                              String Fromcity = getDriverWrapper().getEventFieldAsText(
                                          "SDF", "s", "");
                              // //System.out.println("from city" + Fromcity);
                              String Tocity = getDriverWrapper().getEventFieldAsText(
                                          "SDT", "s", "");
                              // //System.out.println("to city " + Tocity);
                              String gooddesc = getDriverWrapper().getEventFieldAsText(
                                          "cAIJ", "s", "");
                              // //System.out.println("goods description" + gooddesc);
                              String goodcode = getDriverWrapper().getEventFieldAsText(
                                          "cAKM", "s", "");
                              getPane().setDESGOODS(gooddesc);
                              getPane().setGOODT(goodcode);
                              getPane().setSHIPFMLC(Fromcity);
                              getPane().setSHIPTOLC(Tocity);

                              System.out.println("Inside Goods End 1");

                              // //System.out.println("hscode Value in try---->" +
                              // hscodeval);
                              String hyperValue = "select lcm.TAX_PAY_BY,lcm.OUR_CHGS,lcm.OVR_CHGS,lcm.LOADING,lcm.DISCHARGE from master mas, lcmaster lcm where mas.KEY97 = lcm.KEY97 and mas.MASTER_REF  = '"
                                          + masterRefNumber + "'";
                              // //System.out.println("TI field query Value---->" +
                              // hyperValue);
                              con = ConnectionMaster.getConnection();
                              ps1 = con.prepareStatement(hyperValue);
                              rs = ps1.executeQuery();
                              if (rs.next()) {
                                    String taxvale = rs.getString(1);
                                    String ourchsrge = rs.getString(2);
                                    String overses = rs.getString(3);
                                    String loading = rs.getString(4);
                                    String dischrge = rs.getString(5);
                                    // //System.out.println("port code description---->"
                                    // +

                                    if (taxvale.equalsIgnoreCase("P")) {
                                          getPane().setTAXPAID("Charge payer");
                                    } else {
                                          getPane().setTAXPAID("Customer");
                                    }
                                    if (ourchsrge.equalsIgnoreCase("A")) {
                                          getPane().setOURS("Applicant");
                                    } else {
                                          getPane().setOURS("Beneficiary");
                                    }
                                    if (overses.equalsIgnoreCase("A")) {
                                          getPane().setOVERSES("Applicant");
                                    } else {
                                          getPane().setOVERSES("Beneficiary");
                                    }
                                    if (loading != null && loading.length() > 0) {
                                          getPane().setSHPMTFRM(loading);
                                    }
                                    if (dischrge != null && dischrge.length() > 0) {
                                          getPane().setSHIPMTTO(dischrge);
                                    }
                              }

                              // con.close();
                              // ps1.close();
                              // rs.close();

                        } catch (Exception e) {
                              // System.out.println(e.getMessage());
                        } finally {
                              try {

                                    if (rs != null)
                                          rs.close();
                                    if (ps1 != null)
                                          ps1.close();
                                    if (con != null)
                                          con.close();

                              } catch (SQLException e) {
                                    // // System.out.println("Connection Failed! Check
                                    // // output
                                    // // console");
                                    e.printStackTrace();
                              }
                        }
                  } else {
                        // // System.out.println("--------->" + getMajorCode() +
                        // // getMinorCode());

                  }

                  // ShippingDetailsLc

                  if (prd_typ.equalsIgnoreCase("NLD")
                              || (!prd_typ.equalsIgnoreCase("ELF"))) {
                        EventPane pane = (EventPane) getPane();

                        pane.getBtnFetchInvdetFREENEGLC().setEnabled(false);
                        pane.getBtnFetchShipdetFREENEGLC().setEnabled(false);
                        pane.getBtnFetchInvdetFREENEGLC().setEnabled(false);
                        pane.getBtnFetchshipdetFREENEGLCOUTPclay().setEnabled(false);

                        pane.getExtEventShippingTableNew().setEnabled(false);
                        pane.getExtEventShippingTableDelete().setEnabled(false);
                        pane.getExtEventShippingdetailslcNew().setEnabled(false);
                        pane.getExtEventShippingdetailslcDelete().setEnabled(false);
                        pane.getExtEventShippingdetailslcUpdate().setEnabled(false);
                        pane.getExtEventInvoicedetailsLCNew().setEnabled(false);
                        pane.getExtEventInvoicedetailsLCUpdate().setEnabled(false);
                        pane.getExtEventInvoicedetailsLCDelete().setEnabled(false);

                        pane.getExtEventAdvanceTableNew().setEnabled(false);
                        pane.getExtEventAdvanceTableDelete().setEnabled(false);
                        pane.getExtEventAdvanceTableUpdate().setEnabled(false);
                        pane.getBtnonfetchFREENEGLC().setEnabled(false);
                        pane.getBtnonfetchFREENEGLCOUTPclay().setEnabled(false);

                  }

                  else {
                        // //System.out.println("Product type is forign");
                        EventPane pane = (EventPane) getPane();
                        pane.getExtEventAdvanceTableNew().setEnabled(true);
                        pane.getExtEventAdvanceTableDelete().setEnabled(true);
                        pane.getExtEventAdvanceTableUpdate().setEnabled(true);
                        pane.getBtnonfetchFREENEGLC().setEnabled(false);
                        pane.getBtnonfetchFREENEGLCOUTPclay().setEnabled(false);

                        pane.getExtEventShippingTableNew().setEnabled(true);
                        pane.getExtEventShippingTableDelete().setEnabled(true);
                        pane.getExtEventShippingdetailslcNew().setEnabled(true);
                        pane.getExtEventShippingdetailslcDelete().setEnabled(true);
                        pane.getExtEventShippingdetailslcUpdate().setEnabled(true);
                        pane.getExtEventInvoicedetailsLCNew().setEnabled(true);
                        pane.getExtEventInvoicedetailsLCUpdate().setEnabled(true);
                        pane.getExtEventInvoicedetailsLCDelete().setEnabled(true);

                        pane.getBtnFetchInvdetFREENEGLC().setEnabled(false);
                        pane.getBtnFetchShipdetFREENEGLC().setEnabled(false);
                  }
                  if (getMajorCode().equalsIgnoreCase("FRN")
                              && getMinorCode().equalsIgnoreCase("POF")) {
                        EventPane pane = (EventPane) getPane();
                        pane.getBtnFetchInvdetFREENEGLC().setEnabled(false);
                        pane.getBtnFetchshipdetFREENEGLCOUTPclay().setEnabled(false);
                        pane.getExtEventShippingTableNew().setEnabled(false);
                        pane.getExtEventShippingTableDelete().setEnabled(false);
                        pane.getExtEventShippingdetailslcNew().setEnabled(false);
                        pane.getExtEventShippingdetailslcDelete().setEnabled(false);
                        pane.getExtEventShippingdetailslcUpdate().setEnabled(false);
                        pane.getExtEventInvoicedetailsLCNew().setEnabled(false);
                        pane.getExtEventInvoicedetailsLCUpdate().setEnabled(false);
                        pane.getExtEventInvoicedetailsLCDelete().setEnabled(false);
                  }
                  if (getMajorCode().equalsIgnoreCase("FRN")
                              && getMinorCode().equalsIgnoreCase("POF")) {
                        List<ExtEventShippingTable> shipcol = (List<ExtEventShippingTable>) getWrapper()
                                    .getExtEventShippingTable();
                        if (shipcol.size() > 0) {
                              // //System.out.println("shipdetails --->" + shipcol);
                              EventPane pane = (EventPane) getPane();
                              pane.getExtEventShippingTableDelete().setEnabled(false);
                        }
                  }
                  String payaction = getDriverWrapper().getEventFieldAsText("PYAN",
                              "s", "");

                  String step_Id = "Input";
                  // System.out.println("payment Type action---->" + payaction);
                  String tranType = getWrapper().getTRANTYP();
                  // System.out.println("Write of Type---->" + tranType +
                  // "tranType---->" + tranType.length());
                  if (tranType.equalsIgnoreCase("WRITEOFF")
                              && payaction.equalsIgnoreCase("Pay")
                              && prd_typ.equalsIgnoreCase("ELF")
                              && !step_Id.equalsIgnoreCase("CSM")) {
                        getPane().setEBRCFLAG("N");
                  } else if ((tranType.equalsIgnoreCase("") || !tranType
                              .equalsIgnoreCase("WRITEOFF"))
                              && !payaction.equalsIgnoreCase("Pay")
                              && prd_typ.equalsIgnoreCase("ELF")
                              && !step_Id.equalsIgnoreCase("CSM")) {
                        getPane().setEBRCFLAG("N");
                  } else if ((tranType.equalsIgnoreCase("") || !tranType
                              .equalsIgnoreCase("WRITEOFF"))
                              && payaction.equalsIgnoreCase("Pay")
                              && prd_typ.equalsIgnoreCase("ELF")
                              && !step_Id.equalsIgnoreCase("CSM")) {
                        getPane().setEBRCFLAG("Y");

                  } else if (step_Id.equalsIgnoreCase("CSM")) {
                        getPane().setEBRCFLAG("");
                  }

                  if ((step_input.equalsIgnoreCase("Create")
                              || step_log.equalsIgnoreCase("Log") || step_input
                                    .equalsIgnoreCase("CSM"))
                              && getMinorCode().equalsIgnoreCase("POF")) {
                        getPane().setEBRCFLAG("");

                  }
                  // // System.out.println("Event Status " + eventStatus);
                  if (getMinorCode().equalsIgnoreCase("POF")) {
                        try {

                              String shipbillnum = "x";
                              String SHIPBILLDATE = "";
                              String PORTCODE = "";
                              String FORMNO = "";
                              String OUTSTANDINGAMOUNT = "";
                              String outStandingAmount = "";
                              // String outStandingAmount = "0.0 USD";
                              String currency = "USD";
                              String ShipAmount = "";
                              double outAmount = 0.0;
                              String outstr = "";
                              ArrayList<String> shippValues = new ArrayList<String>();
                              List<ExtEventShippingTable> shipingDetails = (List<ExtEventShippingTable>) getWrapper()
                                          .getExtEventShippingTable();

                              for (int i = 0; i < shipingDetails.size(); i++) {
                                    if (!step_csm.equalsIgnoreCase("Final print")
                                                && !eventStatus.equalsIgnoreCase("Completed")) {
                                          ExtEventShippingTable ship_Obj = shipingDetails
                                                      .get(i);
                                          SimpleDateFormat format = new SimpleDateFormat(
                                                      "dd-MM-yy");
                                          ShipAmount = String.valueOf(ship_Obj.getSHPAMT())
                                                      + " " + ship_Obj.getSHPAMTCurrency();
                                          OUTSTANDINGAMOUNT = ShipAmount;

                                          shipbillnum = ship_Obj.getBILLNUM().trim();
                                          // //System.out.println("shipbillnum.length()---->"
                                          // +
                                          // shipbillnum.length());
                                          shipbillnum = setxxforparameters(shipbillnum.trim());
                                          // //System.out.println("Shipping Bill Number "
                                          // +
                                          // shipbillnum);
                                          shippValues.add(shipbillnum);
                                          SHIPBILLDATE = format.format(ship_Obj.getBILLDAT());
                                          // //System.out.println("Shipping Bill Date " +
                                          // SHIPBILLDATE);
                                          SHIPBILLDATE = setxxforparameters(SHIPBILLDATE
                                                      .trim());
                                          // //System.out.println("Shipping Bill Date " +
                                          // SHIPBILLDATE);
                                          shippValues.add(SHIPBILLDATE);
                                          if (shipbillnum.length() > 1) {

                                                // //System.out.println("Shipping Bill
                                                // Number "
                                                // +
                                                // shipbillnum);

                                                String outStanding_Amnt = "SELECT OUTSAMT ,SHIPCCY FROM ETT_EDPMS_SHP_OUTSAMT WHERE SHIPBILLNUM='"
                                                            + shipbillnum
                                                            + "' and SHIPBILLDATE='"
                                                            + SHIPBILLDATE + "'";
                                                // // System.out.println( "Repayment Amount
                                                // // query
                                                // // outStanding_Amnt if loop----->" +
                                                // // outStanding_Amnt);
                                                con = ConnectionMaster.getConnection();
                                                ship_prepare = con
                                                            .prepareStatement(outStanding_Amnt);
                                                ship_result = ship_prepare.executeQuery();
                                                // //System.out.println("Query
                                                // executeQuery");
                                                while (ship_result.next()) {

                                                      outAmount = ship_result
                                                                  .getDouble("OUTSAMT");
                                                      outstr = ship_result.getString("OUTSAMT");
                                                      // // System.out.println("Outstanding
                                                      // // amount
                                                      // // while loop------>" + outAmount);

                                                      if (outAmount > 0) {
                                                            // //System.out.println("outAmount
                                                            // is if
                                                            // loop" + outAmount);
                                                            currency = ship_result.getString(2);
                                                            // //System.out.println("currency is
                                                            // " +
                                                            // currency);
                                                            // outStandingAmount =
                                                            // String.valueOf(outAmount / 10);
                                                            OUTSTANDINGAMOUNT = outstr + " "
                                                                        + currency;
                                                            // //System.out.println("Out Amount
                                                            // is"
                                                            // +
                                                            // OUTSTANDINGAMOUNT);
                                                            ship_Obj.setColumn("LOUTSAMT",
                                                                        OUTSTANDINGAMOUNT);
                                                            if (payaction.equalsIgnoreCase("Pay")) {
                                                                  // ship_Obj.setColumn("REPAYAM",
                                                                  // OUTSTANDINGAMOUNT);
                                                            } else {

                                                                  String repout = 0 + " " + currency;
                                                                  // // System.out.println("New
                                                                  // // Repayment Amount else loop"
                                                                  // // +
                                                                  // // repout);
                                                                  ship_Obj.setColumn("REPAYAM",
                                                                              repout);
                                                                  ship_Obj.setColumn("EQUBILL",
                                                                              repout);
                                                            }
                                                      } else {
                                                            // // System.out.println("outAmount
                                                            // // is
                                                            // // else loop" + outAmount);
                                                            currency = ship_result.getString(2);
                                                            // //System.out.println("currency is
                                                            // " +
                                                            // currency);
                                                            // outStandingAmount =
                                                            // String.valueOf(outAmount / 10);
                                                            OUTSTANDINGAMOUNT = 0 + " " + currency;
                                                            // System.out.println("Out Amount
                                                            // else"
                                                            // + OUTSTANDINGAMOUNT);
                                                            ship_Obj.setColumn("LOUTSAMT",
                                                                        OUTSTANDINGAMOUNT);
                                                            ship_Obj.setColumn("REPAYAM",
                                                                        OUTSTANDINGAMOUNT);
                                                            ship_Obj.setColumn("EQUBILL",
                                                                        OUTSTANDINGAMOUNT);
                                                            // //System.out.println("Repayment
                                                            // Amount
                                                            // collection else loop" +
                                                            // OUTSTANDINGAMOUNT);

                                                      }

                                                      // // System.out.println("Repayment
                                                      // // Amount
                                                      // // collection if loop" +
                                                      // // OUTSTANDINGAMOUNT);
                                                }
                                          }

                                          else {
                                                // // System.out.println("Repayment Amount
                                                // // collection else loop----->");
                                                PORTCODE = ship_Obj.getPORTCODDD();
                                                // //System.out.println("Port Code " +
                                                // PORTCODE);
                                                PORTCODE = setxxforparameters(PORTCODE.trim());
                                                // //System.out.println("Port Code " +
                                                // PORTCODE);
                                                shippValues.add(PORTCODE);
                                                FORMNO = ship_Obj.getFORMNUM();
                                                // //System.out.println("Form No" + FORMNO);
                                                FORMNO = setxxforparameters(FORMNO.trim());
                                                // //System.out.println("Form No" + FORMNO);
                                                shippValues.add(FORMNO);
                                                String outStanding_Amnt = "SELECT OUTSAMT ,SHIPCCY FROM ETT_EDPMS_SHP_OUTSAMT WHERE SHIPBILLNUM='"
                                                            + shipbillnum
                                                            + "' and SHIPBILLDATE='"
                                                            + SHIPBILLDATE
                                                            + "' AND PORTCODE='"
                                                            + PORTCODE
                                                            + "' AND FORMNO='"
                                                            + FORMNO
                                                            + "'";
                                                // // System.out.println("Repayment Amount
                                                // // query
                                                // // outStanding_Amnt else loop----->" +
                                                // // outStanding_Amnt);
                                                con = ConnectionMaster.getConnection();
                                                ship_prepare = con
                                                            .prepareStatement(outStanding_Amnt);
                                                ship_result = ship_prepare.executeQuery();
                                                while (ship_result.next()) {

                                                      outAmount = ship_result
                                                                  .getDouble("OUTSAMT");
                                                      outstr = ship_result.getString("OUTSAMT");

                                                      // //System.out.println("Outstanding
                                                      // amount
                                                      // while loop for form no------>" +
                                                      // outAmount);

                                                      if (outAmount > 0) {
                                                            // //System.out.println("outAmount
                                                            // is if
                                                            // loop" + outAmount);
                                                            currency = ship_result.getString(2);
                                                            // //System.out.println("currency is
                                                            // " +
                                                            // currency);
                                                            // outStandingAmount =
                                                            // String.valueOf(outAmount / 10);
                                                            OUTSTANDINGAMOUNT = outstr + " "
                                                                        + currency;
                                                            // // System.out.println("Out Amount
                                                            // // " +
                                                            // // OUTSTANDINGAMOUNT);
                                                            ship_Obj.setColumn("LOUTSAMT",
                                                                        OUTSTANDINGAMOUNT);
                                                            if (payaction.equalsIgnoreCase("Pay")) {
                                                                  // stem.out.println("Repayment
                                                                  // Amount CURRENCY
                                                                  // Checking=======>"
                                                                  // + OUTSTANDINGAMOUNT);

                                                                  // ship_Obj.setColumn("REPAYAM",
                                                                  // OUTSTANDINGAMOUNT);
                                                            } else {

                                                                  String repout = 0 + " " + currency;
                                                                  // // System.out.println("New
                                                                  // // Repayment Amount else loop"
                                                                  // // +
                                                                  // // repout);
                                                                  ship_Obj.setColumn("REPAYAM",
                                                                              repout);
                                                                  ship_Obj.setColumn("EQUBILL",
                                                                              repout);
                                                            }
                                                      } else {
                                                            // //System.out.println("outAmount
                                                            // is
                                                            // else loop" + outAmount);
                                                            currency = ship_result.getString(2);
                                                            // //System.out.println("currency is
                                                            // " +
                                                            // currency);
                                                            // outStandingAmount =
                                                            // String.valueOf(outAmount / 10);
                                                            OUTSTANDINGAMOUNT = 0 + " " + currency;
                                                            // //System.out.println("Out Amount
                                                            // " +
                                                            // OUTSTANDINGAMOUNT);
                                                            ship_Obj.setColumn("LOUTSAMT",
                                                                        OUTSTANDINGAMOUNT);
                                                            ship_Obj.setColumn("REPAYAM",
                                                                        OUTSTANDINGAMOUNT);
                                                            ship_Obj.setColumn("EQUBILL",
                                                                        OUTSTANDINGAMOUNT);

                                                      }

                                                }
                                          }
                                    } else {
                                          // // System.out.println("Event Status is
                                          // // Completed");
                                    }
                              }

                        } catch (Exception e) {
                              // // System.out.println("Exception in setting outsing
                              // // amount
                              // // " + e.getMessage());
                        } finally {
                              try {

                                    if (ship_result != null)
                                          ship_result.close();
                                    if (ship_prepare != null)
                                          ship_prepare.close();
                                    if (con != null)
                                          con.close();

                              } catch (SQLException e) {
                                    // System.out.println("Connection Failed! Check
                                    // output
                                    // console");
                                    e.printStackTrace();
                              }
                        }
                  } else {
                        // // System.out.println("Product type for outstanding
                        // // amount-->"
                        // // + getMinorCode());
                  }
                  if (getMinorCode().equalsIgnoreCase("POF")) {
                        try {

                              String step_Input1 = getDriverWrapper()
                                          .getEventFieldAsText("CSTY", "s", "");
                              // //System.out.println("step_Input check ----->" +
                              // step_Input);
                              // //System.out.println("step id check for CSM ----->" +
                              // step_csm);
                              String eventStatus1 = getDriverWrapper()
                                          .getEventFieldAsText("EVSS", "s", "");

                              Connection conne = null;
                              PreparedStatement pres = null;
                              ResultSet resSet = null;
                              String master_refNo = "";
                              master_refNo = getmasRefNo();
                              String EventRefNo = "";
                              EventRefNo = geteventRefNo();

                              List<ExtEventShippingTable> liste = (List<ExtEventShippingTable>) getWrapper()
                                          .getExtEventShippingTable();

                              for (int i = 0; i < liste.size(); i++) {
                                    if (!step_csm.equalsIgnoreCase("Final print")
                                                && !eventStatus.equalsIgnoreCase("Completed")) {
                                          conne = ConnectionMaster.getConnection();
                                          double outStandingAmt = 0;
                                          String outStandingAmtCurr = "";
                                          ExtEventShippingTable ship = liste.get(i);
                                          String ShipBillNo = "";
                                          ShipBillNo = ship.getBILLNUM();
                                          outStandingAmt = ship.getLOUTSAMT().doubleValue();
                                          String outStanding_str = String
                                                      .valueOf(outStandingAmt);
                                          // //System.out.println("Outstanding Amount " +
                                          // outStandingAmt);
                                          String outStanding = ship.getLOUTSAMT().toString();
                                          outStandingAmtCurr = ship.getLOUTSAMTCurrency();
                                          // //System.out.println("Shipping Bill Number is
                                          // " +
                                          // ShipBillNo);
                                          Date ShipDate = ship.getBILLDAT();
                                          String billDate = "";
                                          SimpleDateFormat date = new SimpleDateFormat(
                                                      "dd-MM-yy");
                                          if (ShipDate != null) {
                                                billDate = date.format(ShipDate);
                                          }
                                          String formNo = "";
                                          formNo = ship.getFORMNUM();
                                          // //System.out.println("Form nO is " + formNo);
                                          String portCode = "";
                                          portCode = ship.getPORTCODDD();
                                          // //System.out.println("Port Code is " +
                                          // portCode);
                                          ShipBillNo = setxxforparameters(ShipBillNo);
                                          billDate = setxxforparameters(billDate);
                                          formNo = setxxforparameters(formNo);
                                          portCode = setxxforparameters(portCode);
                                          // //System.out.println("Ship Bill Amount " +
                                          // ShipBillNo +
                                          // "Bill Date " + billDate + "Form No " + formNo
                                          // +
                                          // "portCode
                                          // " + portCode);
                                          String query = "select billamt AS BILL,billequcurr AS BCURR,repayamt AS REP,repaycurr AS RCURR,REPAYTYPE as TYPE,SHORTAMT as SHORT,SHORTCURR as SHORTCURR,REASON as REASON,NOTIONAL AS NOTIONAL from ETT_EDPMS_ALLSHP_REPAYAMT where master_ref='"
                                                      + master_refNo
                                                      + "' and eventrefno='"
                                                      + EventRefNo
                                                      + "' and shipbillnum='"
                                                      + ShipBillNo
                                                      + "' AND shipbilldate='"
                                                      + billDate
                                                      + "' and portcode='"
                                                      + portCode
                                                      + "' and formno='" + formNo + "'";
                                          System.out.println("Resetting amount Query is "
                                                      + query);
                                          pres = conne.prepareStatement(query);
                                          resSet = pres.executeQuery();
                                          if (resSet.next()) {

                                                if (resSet.getDouble("REP") > 0
                                                            && payaction.equalsIgnoreCase("Pay")) {
                                                      // // System.out.println("Resetting REP
                                                      // // value
                                                      // // else if for loop currency==>>" +
                                                      // // resSet.getString("RCURR"));
                                                      ship.setREPAYAM(new BigDecimal(resSet
                                                                  .getString("REP")));
                                                      // System.out.println("repayamount"+ship.getREPAYAM());
                                                      String resSetCCY = ship
                                                                  .getREPAYAMCurrency().toString();
                                                      // // System.out.println("Getting the
                                                      // // repayment currency---->" +
                                                      // // resSetCCY);
                                                      ship.setREPAYAMCurrency(resSetCCY);
                                                } else {
                                                      String reystr = "0";
                                                      ship.setREPAYAM(new BigDecimal(Double
                                                                  .valueOf(reystr)));
                                                      System.out.println("repayamount  2===>"
                                                                  + ship.getREPAYAM());

                                                      // System.out.println("repayamount
                                                      // 1===>"+shipAmt);
                                                      ship.setREPAYAMCurrency(outStandingAmtCurr);
                                                }

                                                if (resSet.getDouble("BILL") > 0
                                                            && payaction.equalsIgnoreCase("Pay")) {
                                                      // // System.out.println("Equlant bill
                                                      // // value
                                                      // // if loop=====>" +
                                                      // // resSet.getDouble("BILL"));
                                                      ship.setEQUBILL(new BigDecimal(resSet
                                                                  .getString("BILL")));
                                                      ship.setEQUBILLCurrency(resSet
                                                                  .getString("BCURR"));
                                                } else {
                                                      // //System.out.println("Equlant bill
                                                      // value
                                                      // else loop=====>" +
                                                      // resSet.getDouble("BILL"));

                                                      String equ = "0";
                                                      ship.setEQUBILL(new BigDecimal(Double
                                                                  .valueOf(equ)));
                                                      ship.setEQUBILLCurrency(outStandingAmtCurr);
                                                }

                                                // //
                                                // ship.setREPTYPE(resSet.getString("TYPE"));
                                                // ship.setREASHRF(resSet.getString("REASON"));
                                                // ship.setSHCOLAM(new
                                                // BigDecimal(resSet.getString("SHORT")));
                                                // ship.setSHCOLAMCurrency(resSet.getString("SHORTCURR"));

                                          } else {
                                                // // System.out.println("Entered Else
                                                // // Resetting--->" + outStandingAmt);
                                                if (payaction.equalsIgnoreCase("Pay")) {
                                                      ship.setREPAYAM(new BigDecimal(outStanding));
                                                      // System.out.println("reystr
                                                      // if"+ship.getREPAYAM());
                                                      // System.out.println("outstanding"+outStanding);
                                                      ship.setREPAYAMCurrency(outStandingAmtCurr);
                                                } else {
                                                      String reystr = "0";
                                                      ship.setREPAYAM(new BigDecimal(Double
                                                                  .valueOf(reystr)));
                                                      // System.out.println("reystr
                                                      // else"+ship.getREPAYAM());
                                                      ship.setREPAYAMCurrency(outStandingAmtCurr);
                                                }

                                                if (payaction.equalsIgnoreCase("Pay")) {

                                                      ship.setEQUBILL(new BigDecimal(outStanding));
                                                      ship.setEQUBILLCurrency(outStandingAmtCurr);
                                                } else {
                                                      String equ = "0";
                                                      ship.setEQUBILL(new BigDecimal(Double
                                                                  .valueOf(equ)));
                                                      ship.setEQUBILLCurrency(outStandingAmtCurr);
                                                }
                                          }

                                          if (outStandingAmt == 0 || outStandingAmt < 1) {
                                                // //System.out.println("Entered
                                                // outStandingAmt
                                                // Resetting if loop--->" + outStandingAmt);
                                                String reystr = "0";
                                                ship.setREPAYAM(new BigDecimal(Double
                                                            .valueOf(reystr)));
                                                // System.out.println("reystr
                                                // if==================>"+ship.getREPAYAM());
                                                // System.out.println("outstanding"+outStanding);
                                                ship.setREPAYAMCurrency(outStandingAmtCurr);
                                          }

                                    } else {
                                          // // System.out.println("Event is already
                                          // // Completed");
                                    }

                              }

                        } catch (Exception e) {
                              // System.out.println("Exception e " + e.getMessage());
                        }

                  }

                  // InwardLink
                  try {
                        String HyperInward = getINWARDREM();
                        ExtendedHyperlinkControlWrapper InwardLink = getPane()
                                    .getCtlINWARDFREENEGLCHyperlink();
                        InwardLink.setUrl(HyperInward);
                        ExtendedHyperlinkControlWrapper InwardLink1 = getPane()
                                    .getCtlINWARDFREENEGLCOUTPclayHyperlink();
                        InwardLink1.setUrl(HyperInward);

                  } catch (Exception ees) {
                        System.out.println("Exception Inward Link ===>"
                                    + ees.getMessage());
                  }

                  if (getMinorCode().equalsIgnoreCase("POF")
                              && step_input.equalsIgnoreCase("CSM")) {
                        try {
                              List<ExtEventShippingTable> shipTab = (List<ExtEventShippingTable>) getWrapper()
                                          .getExtEventShippingTable();
                              // //System.out.println("shipping table for notional
                              // rate---->" + shipTable.size());
                              String repval_new = "2";
                              String notionalrate = "1";
                              for (int i = 0; i < shipTab.size(); i++) {

                                    ExtEventShippingTable ship = shipTab.get(i);
                                    BigDecimal outStandAmt = ship.getLOUTSAMT();
                                    String outStandccy = ship.getLOUTSAMTCurrency()
                                                .toString();
                                    // repval_new = ship.getREPTYPE().toString();
                                    // // System.out.println("shipping repayment in CSM
                                    // step
                                    // if====>" + outStandAmt + "" + payaction);

                                    ship.setREPAYAM(outStandAmt);
                                    ship.setREPAYAMCurrency(outStandccy);
                                    ship.setNOTIONAL(new BigDecimal(Double
                                                .valueOf(notionalrate)));
                                    // // System.out.println("Notional rate in CSM step
                                    // // if====>" + ship.getNOTIONAL());
                                    String shipcol = "0";
                                    ship.setSHCOLAM(new BigDecimal(Double.valueOf(shipcol)));
                                    ship.setSHCOLAMCurrency(outStandccy);

                                    if (repval_new.equalsIgnoreCase("2")
                                                || repval_new.equalsIgnoreCase("")
                                                && (!repval_new.equalsIgnoreCase("1") || !repval_new
                                                            .equalsIgnoreCase("Part"))) {
                                          // // System.out.println("Repayment type in CSM
                                          // // step
                                          // // if====>" + repval_new);
                                          ship.setREPTYPE(repval_new);
                                    } else {
                                          // // System.out.println("Repayment type in CSM
                                          // // step
                                          // // else loop====>" + repval_new);
                                    }
                              }
                        } catch (Exception e) {
                              // System.out.println("Exception e " + e.getMessage());
                        }
                  }

                  if ((step_input.equalsIgnoreCase("Create") || step_input
                              .equalsIgnoreCase("CSM"))
                              && (getMinorCode().equalsIgnoreCase("POF") || getMinorCode()
                                          .equalsIgnoreCase("POC"))
                              && (!gateway.equalsIgnoreCase("Y"))) {
                        // System.out.println("inside second if");
                        /*
                         * getPane().setNOSAMT(""); getPane().setNOSTOUT("");
                         */
                        getPane().setEBRCFLAG("");

                        /*
                         * getPane().setNOSTRM(""); getPane().setNOSTMT("");
                         * getPane().setNOSTAMT(""); getPane().setNOSTDAT("");
                         *
                         * getPane().setPOOLAMT(""); getPane().setNOSTACC("");
                         * getPane().setINWMSG(""); getPane().setMTMESG("");
                         */}
                  
                  if (getMinorCode().equalsIgnoreCase("FNF")
                              || getMinorCode().equalsIgnoreCase("POF")) {
                        try {
                              getrtgsNEFT();

                        } catch (Exception e) {
                              // System.out.println("Exception in purpose
                              // code--------->"
                              // + e.getMessage());
                        }
                  }


            }
            return false;

      }

      @SuppressWarnings("unchecked")
      public void onValidate(ValidationDetails validationDetails) {
            String strPropName = "MigrationDone";
            String dailyval = "";
            AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryRCODE = getDriverWrapper()
                        .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strPropName + "'");
            // //System.out.println("ADDDailyTxnLimit initially-------->");
            EXTGENCUSTPROP PROPCode = queryRCODE.getUnique();
            if (PROPCode != null) {

                  dailyval = PROPCode.getPropval();
                  // //System.out.println("ADDDailyTxnLimit -------->" +
                  // PROPCode.getPropval());
            } else {
                  // // System.out.println("ADDDailyTxnLimit is empty-------->");

            }
            String eventStatus = getDriverWrapper().getEventFieldAsText("EVSS",
                        "s", "");
            if (dailyval.equalsIgnoreCase("NO")) {

                  String strLog = "Log";
                  String dailyval_Log = "";
                  AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryLog = getDriverWrapper()
                              .createQuery("EXTGENCUSTPROP", "PROPNAME='" + strLog + "'");
                  EXTGENCUSTPROP CodeLog = queryLog.getUnique();
                  if (CodeLog != null) {

                        dailyval_Log = CodeLog.getPropval();
                  } else {
                        // System.out.println("ADDDailyTxnLimit is empty-------->");

                  }

                  // InwardLink
                  try {
                        String HyperInward = getINWARDREM();
                        ExtendedHyperlinkControlWrapper InwardLink = getPane()
                                    .getCtlINWARDFREENEGLCHyperlink();
                        InwardLink.setUrl(HyperInward);
                        ExtendedHyperlinkControlWrapper InwardLink1 = getPane()
                                    .getCtlINWARDFREENEGLCOUTPclayHyperlink();
                        InwardLink1.setUrl(HyperInward);

                  } catch (Exception ees) {
                        System.out.println("Exception Inward Link ===>"
                                    + ees.getMessage());
                  }

                  // TST

                  try {
                        String val = "DMSTS";
                        String TSTHyperlink = Link(val);

                        ExtendedHyperlinkControlWrapper dmsh1 = getPane()
                                    .getCtlTSTFREENEGLCOUTPclayHyperlink();
                        dmsh1.setUrl(TSTHyperlink);

                        ExtendedHyperlinkControlWrapper dmsha1 = getPane()
                                    .getCtlTSTFREENEGLCHyperlink();
                        dmsha1.setUrl(TSTHyperlink);

                        ExtendedHyperlinkControlWrapper dmshC = getPane()
                                    .getCtlTSTFREENEGADJHyperlink();
                        dmshC.setUrl(TSTHyperlink);

                  } catch (Exception ees) {
                        System.out
                                    .println("Exception Transaction link called in service"
                                                + ees.getMessage());
                  }

                  String step_Input = getDriverWrapper().getEventFieldAsText("CSTY",
                              "s", "").trim();
                  String step_csm = getDriverWrapper().getEventFieldAsText("CSID",
                              "s", "").trim();
                  String tenstart = getDriverWrapper().getEventFieldAsText("TNRF",
                              "s", "").trim();
                  

                  try {
                        if (getMajorCode().equalsIgnoreCase("ELC")
                                    && getMinorCode().equalsIgnoreCase("POD")
                                    && (!step_csm.equalsIgnoreCase("Final print") && !eventStatus
                                                .equalsIgnoreCase("Completed"))) {

                              getPane().onNOSTROELCSETTclayButton();

                        } else {
                              // // System.out.println("getMajorCode() ILC------->" +
                              // // getMajorCode());

                        }
                  } catch (Exception e) {
                        /*
                         * if (dailyval_Log.equalsIgnoreCase("YES")) {
                         * //System.out.println("Exception in nostro button---->" +
                         * e.getMessage());
                         *
                         * }
                         */
                  }
                  /*
                   * // amount to be collect
                   *
                   * if (getMajorCode().equalsIgnoreCase("FRN") &&
                   * (getMinorCode().equalsIgnoreCase("FNF") ||
                   * getMinorCode().equalsIgnoreCase("POD"))) { try { //
                   * System.out.println("Net amount"+getMinorCode()); //
                   * getnetAmount(); getnetAmountOdcPay(); } catch (Exception e) {
                   *
                   * } }
                   */
                  // FCY Tax calculation
                  try {
                        getPane().getExtEventLienMarkingDelete().setEnabled(false);

                        getFCCTCALCULATION();

                  } catch (Exception e) {
                        // System.out.println("Exception update" +
                        // e.getMessage());
                  }

                  try {

                        getutrNoGenerated();
                  } catch (Exception ee) {
                        // System.out.println("UTR Number getutrNoGenerated" +
                        // ee.getMessage());

                  }
                  /*
                   * if (getMajorCode().equalsIgnoreCase("FRN")) { try {
                   *
                   * getactualPenalRate();
                   *
                   * } catch (Exception e) { // System.out.println("Exception update"
                   * + e.getMessage()); } }
                   */

                  String gateWayVal = getDriverWrapper().getEventFieldAsText("FRSI",
                              "l", "").trim();
                  if (getMinorCode().equalsIgnoreCase("POF")
                              && (gateWayVal.equalsIgnoreCase("Y") || !gateWayVal
                                          .equalsIgnoreCase("N"))) {
                        try {
                              getbillNumber();
                        } catch (Exception e) {
                              // System.out.println("Exception update" + e.getMessage());
                        }
                  }
                  if (getMinorCode().equalsIgnoreCase("FNF")
                              || getMinorCode().equalsIgnoreCase("POF")) {
                        try {
                              getrtgsNEFT();

                        } catch (Exception e) {
                              // System.out.println("Exception in purpose
                              // code--------->"
                              // + e.getMessage());
                        }
                  }
                  /*
                   * if (getMajorCode().equalsIgnoreCase("FRN")) {
                   *
                   * getPane().getExtEventLoanDetailsNew().setEnabled(false);
                   * getPane().getExtEventLoanDetailsDelete().setEnabled(false);
                   * getPane().getExtEventLoanDetailsUpdate().setEnabled(false);
                   *
                   * // getPane().getCtlLIMTR().setEnabled(false); //
                   * getPane().getCtlLIMTR().setEnabled(true);
                   *
                   * }
                   */
                  // // //System.out.println("step_Input check ----->" + step_Input);
                  String step_Id = getDriverWrapper().getEventFieldAsText("CSID",
                              "s", "").trim();

                  String gateway = getDriverWrapper().getEventFieldAsText("FRGI",
                              "l", "");

                  if (gateway.equalsIgnoreCase("Y")) {
                        getPane().setPERADV("");
                        getPane().setADVREC("");
                        getPane().setNETRECIV("");
                        getPane().setBILLPAY("");
                  }

                  if ((step_Id.equalsIgnoreCase("Create") || step_Id
                              .equalsIgnoreCase("CSM"))
                              && (getMinorCode().equalsIgnoreCase("POF"))) {
                        // System.out.println("Get force
                        // limit=================>"+getPane().getFORLIM());

                        getPane().setFORLIM(false);
                        // System.out.println("After force
                        // limit==============>"+getPane().getFORLIM());
                  }

                  // // System.out.println("gateway flag checking----->" + gateway);
                  if ((step_Id.equalsIgnoreCase("Create") || step_Id
                              .equalsIgnoreCase("CSM"))
                              && (getMinorCode().equalsIgnoreCase("POD") || getMinorCode()
                                          .equalsIgnoreCase("POC")
                              || getMinorCode().equalsIgnoreCase("POF"))) {

                        getPane().setRTGNFT("");
                        getPane().setRTGSN(false);
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
                        getPane().setRTGSNEFT("");
                        getPane().setUTRNO("");
                        getPane().setNARRTVE("");
                        getPane().setBADDRE_Name("");
                        getPane().setRTGSPART("");

                        getPane().setTENEXT("");
                        getPane().setTENRSN("");
                        getPane().setLETNUM("");
                        getPane().setLETDAT("");
                        getPane().setREEXTIN("");
                        getPane().setEXTDAT("");

                        getPane().setWRAMT("");
                        getPane().setWRAMCU("");
                        getPane().setWRITDATE("");
                        getPane().setWRITNDI("");
                        getPane().setTRANTYP("");
                        getPane().setREALTYP("");
                        getPane().setBOENO("");
                        getPane().setBOEDATE("");
                        getPane().setPOSTDIS("");

                        getPane().setREPAYAMT("");
                        getPane().setTOTUSD(0 + " USD");
                        getPane().setTOTINR(0 + " INR");
                        getPane().setTOTEUR(0 + " EUR");
                        getPane().setTOTJPY(0 + " JPY");
                        getPane().setTOTGBP(0 + " GBP");

                  } else {
                        // // System.out.println("Step id for else step---->" +
                        // // step_log);
                  }

                  String rtgsFlag = getDriverWrapper().getEventFieldAsText("cBPJ",
                              "l", "");
                  if (rtgsFlag.equalsIgnoreCase("N")
                              && !rtgsFlag.equalsIgnoreCase("Y")) {
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

                  if ((step_Id.equalsIgnoreCase("Create") || step_Id
                              .equalsIgnoreCase("CSM"))
                              && (getMinorCode().equalsIgnoreCase("POF") || getMinorCode()
                                          .equalsIgnoreCase("POC"))
                              && (!gateway.equalsIgnoreCase("Y"))) {
                        // System.out.println("to on===========================>");
                        // getPane().setNOSAMT("");
                        // getPane().setNOSTOUT("");
                        getPane().setEBRCFLAG("");

                  }
                  try {
                        getPane().onBENIFSCELCSETTclayButton();
                        getPane().onRTGSELCDPclayButton();
                        getPane().onBENIFSCILCSETTclayButton();
                        getPane().onBENIFSCINWDOCCOLPAYclayButton();
                        // getPane().onFETCHLOANELCSETTclayButton();

                        String getHyperSFMS = getIFSCSEARCH();
                        ExtendedHyperlinkControlWrapper ifscilc = getPane()
                                    .getCtlSFMSIMPLCclayHyperlink();
                        ifscilc.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper ifscilc1 = getPane()
                                    .getCtlSFMSILCAMDclayHyperlink();
                        ifscilc1.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper ifscilc2 = getPane()
                                    .getCtlSFMSILCCANHyperlink();
                        ifscilc2.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper ifscilc3 = getPane()
                                    .getCtlSFMSILCCORESSlayHyperlink();
                        ifscilc3.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper ifscilc4 = getPane()
                                    .getCtlSFMSILCEXPHyperlink();
                        ifscilc4.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper ifscilc5 = getPane()
                                    .getCtlSFMSILCISSTAKEclayHyperlink();
                        ifscilc5.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper ifscilc6 = getPane()
                                    .getCtlSFMSILCMAINLayHyperlink();
                        ifscilc6.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper ifscilc7 = getPane()
                                    .getCtlSFMSILCSETTclayHyperlink();
                        ifscilc7.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper ifscilc8 = getPane()
                                    .getCtlSFMSIMPADVclayHyperlink();
                        ifscilc8.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper ifscilc9 = getPane()
                                    .getCtlSFMSIMPADVclayHyperlink();
                        ifscilc9.setUrl(getHyperSFMS);
//                      ExtendedHyperlinkControlWrapper ifscilc10 = getPane()
//                                  .getCtlSFMSIMPLCCLAIMRECclayHyperlink();
//                      ifscilc10.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper ifscilc11 = getPane()
                                    .getCtlSFMSILCMAINLayHyperlink();
                        ifscilc11.setUrl(getHyperSFMS);

                        // Freely Negotiable LC

                        ExtendedHyperlinkControlWrapper sfmsFreeNegFNF = getPane()
                                    .getCtlSFMSFREENEGLCHyperlink();
                        sfmsFreeNegFNF.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsFreeNegOutStanding = getPane()
                                    .getCtlSFMSFREENEGLCOUTPclayHyperlink();
                        sfmsFreeNegOutStanding.setUrl(getHyperSFMS);
                        ExtendedHyperlinkControlWrapper sfmsExpAdvD = getPane()
                                    .getCtlSFMSFREENEGADJHyperlink();
                        sfmsExpAdvD.setUrl(getHyperSFMS);

                  } catch (Exception e) {
                        // System.out.println(e.getMessage());
                  }

                  if (getMajorCode().equalsIgnoreCase("ILC")) {
                        getPeriodicAdv();
                        getPane().onINSURENCEIMPLCclayButton();
                        String facid = getWrapper().getFACLTYID().trim();
                        if (facid == null || facid.equalsIgnoreCase("")) {
                              getPane().setPMARGIN("");
                              getPane().setMARAMT("");
                        }

                  } else {
                        // // System.out.println("getMajorCode() ILC------->" +
                        // // getMajorCode());

                  }

                  String prd_typ = getDriverWrapper().getEventFieldAsText("PTP", "s",
                              "").trim();

                  String payaction = getDriverWrapper().getEventFieldAsText("PYAN",
                              "s", "");

                  String tranType = getWrapper().getTRANTYP();

                  try {

                        String FDenquiryLink = FDenquiry();
                        ExtendedHyperlinkControlWrapper fdlink = getPane()
                                    .getCtlFDENQUIRYIMPLCclayHyperlink();
                        fdlink.setUrl(FDenquiryLink);
                        ExtendedHyperlinkControlWrapper fdlink1 = getPane()
                                    .getCtlFDENQUIRYILCAMDclayHyperlink();
                        fdlink1.setUrl(FDenquiryLink);
                        ExtendedHyperlinkControlWrapper fdlink2 = getPane()
                                    .getCtlFDENQUIRYIMPADVclayHyperlink();
                        fdlink2.setUrl(FDenquiryLink);
                        ExtendedHyperlinkControlWrapper fdlink3 = getPane()
                                    .getCtlFDENQUIRYIMPLCCLAIMRECclayHyperlink();
                        fdlink3.setUrl(FDenquiryLink);
                        ExtendedHyperlinkControlWrapper fdlink4 = getPane()
                                    .getCtlFDENQUIRYILCSETTclayHyperlink();
                        fdlink4.setUrl(FDenquiryLink);

                  } catch (Exception e) {
                        // System.out.println("FDenquiry link" + e.getMessage());
                  }
                  // Notional due date elc new
                  /*
                   * try { if (getMajorCode().equalsIgnoreCase("FRN")) {
                   * getNotionalDueDateELC(); } } catch (Exception e1) {
                   *
                   * }
                   */

                  EventPane pane = (EventPane) getPane();

                  if (prd_typ.equalsIgnoreCase("NLD")
                              || (!prd_typ.equalsIgnoreCase("ELF"))) {

                        pane.getBtnFetchShipdetELCDPclay().setEnabled(false);
                        pane.getBtnFetchInvdetELCDPclay().setEnabled(false);
                        pane.getBtnFetchshipdetELCSETTclay().setEnabled(false);
                        pane.getBtnFetchinvdetELCSETTclay().setEnabled(false);

                        pane.getExtEventShippingTableNew().setEnabled(false);
                        pane.getExtEventShippingTableDelete().setEnabled(false);
                        pane.getExtEventShippingdetailslcNew().setEnabled(false);
                        pane.getExtEventShippingdetailslcDelete().setEnabled(false);
                        pane.getExtEventShippingdetailslcUpdate().setEnabled(false);
                        pane.getExtEventInvoicedetailsLCNew().setEnabled(false);
                        pane.getExtEventInvoicedetailsLCUpdate().setEnabled(false);
                        pane.getExtEventInvoicedetailsLCDelete().setEnabled(false);

                        pane.getExtEventAdvanceTableNew().setEnabled(false);
                        pane.getExtEventAdvanceTableDelete().setEnabled(false);
                        pane.getExtEventAdvanceTableUpdate().setEnabled(false);
                        pane.getBtnonfetchELCDPclay().setEnabled(false);
                        pane.getBtnonfetchELCSETTclay().setEnabled(false);

                  }

                  else {
                        // //System.out.println("Product type is forign");
                        pane.getExtEventAdvanceTableNew().setEnabled(true);
                        pane.getExtEventAdvanceTableDelete().setEnabled(true);
                        pane.getExtEventAdvanceTableUpdate().setEnabled(true);
                        pane.getBtnonfetchELCDPclay().setEnabled(true);
                        pane.getBtnonfetchELCSETTclay().setEnabled(true);

                        pane.getExtEventShippingTableNew().setEnabled(true);
                        pane.getExtEventShippingTableDelete().setEnabled(true);
                        pane.getExtEventShippingdetailslcNew().setEnabled(true);
                        pane.getExtEventShippingdetailslcDelete().setEnabled(true);
                        pane.getExtEventShippingdetailslcUpdate().setEnabled(true);
                        pane.getExtEventInvoicedetailsLCNew().setEnabled(true);
                        pane.getExtEventInvoicedetailsLCUpdate().setEnabled(true);
                        pane.getExtEventInvoicedetailsLCDelete().setEnabled(true);

                        pane.getBtnFetchShipdetELCDPclay().setEnabled(true);
                        pane.getBtnFetchInvdetELCDPclay().setEnabled(true);
                  }

                  // WORKFLOW CHECKLIST CSM

                  try {
                        String Hyperreferel12 = getWorkflow().trim();
                        ExtendedHyperlinkControlWrapper csmreftrack1 = getPane()
                                    .getCtlCSMCHECKLISTFREENEGLCHyperlink();
                        csmreftrack1.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper csmreftrackamd1 = getPane()
                                    .getCtlCSMCHECKLISTFREENEGLCOUTPclayHyperlink();
                        csmreftrackamd1.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper csmreftrackamd2 = getPane()
                                    .getCtlCSMCHECKLISTFREENEGADJHyperlink();
                        csmreftrackamd2.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper csmreftrackamd4 = getPane()
                                    .getCtlCSMCHECKLISTELCAMDclayHyperlink();
                        csmreftrackamd4.setUrl(Hyperreferel12);

//                      ExtendedHyperlinkControlWrapper csmreftrackamd6 = getPane()
//                                  .getCtlCSMCHECKLISTELCSETTclayHyperlink();
//                      csmreftrackamd6.setUrl(Hyperreferel12);

                  } catch (Exception ees) {
                        // System.out.println("WORKFLOW CHECKLIST--------->" +
                        // ees.getMessage());
                  }

                  // REFERE TRACKING CSM
                  try {

                        String Hyperreferel = getReferealtracking().trim();
                        ExtendedHyperlinkControlWrapper csmreftrack = getPane()
                                    .getCtlCSMREFERALFREENEGLCHyperlink();
                        csmreftrack.setUrl(Hyperreferel);

                        ExtendedHyperlinkControlWrapper csmreftrackamd = getPane()
                                    .getCtlCSMREFERALFREENEGLCOUTPclayHyperlink();
                        csmreftrackamd.setUrl(Hyperreferel);

                        ExtendedHyperlinkControlWrapper csmreftrackamd1 = getPane()
                                    .getCtlCSMREFRALIMPADVclayHyperlink();
                        csmreftrackamd1.setUrl(Hyperreferel);

                        ExtendedHyperlinkControlWrapper csmreftrackamd3 = getPane()
                                    .getCtlCSMREFRALFREENEGADJHyperlink();
                        csmreftrackamd3.setUrl(Hyperreferel);

                        ExtendedHyperlinkControlWrapper csmreftrackamd5 = getPane()
                                    .getCtlCSMREFRALILCSETTclayHyperlink();
                        csmreftrackamd5.setUrl(Hyperreferel);

                        ExtendedHyperlinkControlWrapper csmreftrackamd7 = getPane()
                                    .getCtlCSMREFRALILCCANHyperlink();
                        csmreftrackamd7.setUrl(Hyperreferel);

                        ExtendedHyperlinkControlWrapper csmreftrackk1 = getPane()
                                    .getCtlCSMREFRALELCADVclayHyperlink();
                        csmreftrackk1.setUrl(Hyperreferel);

//                      ExtendedHyperlinkControlWrapper csmreftrackk3 = getPane()
//                                  .getCtlCSMREFRALELCDPclayHyperlink();
//                      csmreftrackk3.setUrl(Hyperreferel);

                        ExtendedHyperlinkControlWrapper csmreftrackk5 = getPane()
                                    .getCtlCSMREFRALELCADJclayHyperlink();
                        csmreftrackk5.setUrl(Hyperreferel);

                        ExtendedHyperlinkControlWrapper csmreftrackk7 = getPane()
                                    .getCtlCSMREFRALELCAMDclayHyperlink();
                        csmreftrackk7.setUrl(Hyperreferel);

//                      ExtendedHyperlinkControlWrapper csmreftrackk9 = getPane()
//                                  .getCtlCSMREFRALELCSETTclayHyperlink();
//                      csmreftrackk9.setUrl(Hyperreferel);

                  } catch (Exception ees) {
                        // System.out.println("REFERE TRACKING--------->" +
                        // ees.getMessage());
                  }

                  // Workflow CBS
                  try {
                        String Hyperreferel12 = getWorkflowCBS().trim();
                        ExtendedHyperlinkControlWrapper cpcreftrack1 = getPane()
                                    .getCtlCPCCHECKFREENEGLCHyperlink();
                        cpcreftrack1.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd1 = getPane()
                                    .getCtlCPCCHECKFREENEGLCOUTPclayHyperlink();
                        cpcreftrackamd1.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd3 = getPane()
                                    .getCtlCPCCHECKFREENEGADJHyperlink();
                        cpcreftrackamd3.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd5 = getPane()
                                    .getCtlCPCCHECKELCAMDclayHyperlink();
                        cpcreftrackamd5.setUrl(Hyperreferel12);
//                      ExtendedHyperlinkControlWrapper cpcreftrackamd7 = getPane()
//                                  .getCtlCPCCHECKELCSETTclayHyperlink();
//                      cpcreftrackamd7.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd9 = getPane()
                                    .getCtlCPCCHECKOUTDOCCOLADJclayHyperlink();
                        cpcreftrackamd9.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd11 = getPane()
                                    .getCtlCPCCHECKOUTDOCCOLAMDclayHyperlink();
                        cpcreftrackamd11.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd13 = getPane()
                                    .getCtlCPCCHECKINWDOCCOLPAYclayHyperlink();
                        cpcreftrackamd13.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd15 = getPane()
                                    .getCtlCPCCHECKINWDOCCOLADJclayHyperlink();
                        cpcreftrackamd15.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd17 = getPane()
                                    .getCtlCPCCHECKINWDOCCOLAMDclayHyperlink();
                        cpcreftrackamd17.setUrl(Hyperreferel12);

                        ExtendedHyperlinkControlWrapper cpcreftrack21 = getPane()
                                    .getCtlCPCCHECKIMPLCclayHyperlink();
                        cpcreftrack21.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd22 = getPane()
                                    .getCtlCPCCHECKILCAMDclayHyperlink();
                        cpcreftrackamd22.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd23 = getPane()
                                    .getCtlCPCCHECKIMPADVclayHyperlink();
                        cpcreftrackamd23.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd24 = getPane()
                                    .getCtlCPCCHECKIMPLCCLAIMRECclayHyperlink();
                        cpcreftrackamd24.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd25 = getPane()
                                    .getCtlCPCCHECKILCSETTclayHyperlink();
                        cpcreftrackamd25.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd26 = getPane()
                                    .getCtlCPCCHECKILCCANHyperlink();
                        cpcreftrackamd26.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper csmreftrackamd23 = getPane()
                                    .getCtlCPCCHECKEGTADJclayHyperlink();
                        csmreftrackamd23.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper csmreftrackamd24 = getPane()
                                    .getCtlCPCCHECKEGTAMDclayHyperlink();
                        csmreftrackamd24.setUrl(Hyperreferel12);

                  } catch (Exception ees) {
                        // System.out.println("WORKFLOW CHECKLIST WorkflowCBS--------->"
                        // + ees.getMessage());
                  }

                  // Refereal CBS

                  try {
                        String Hyperreferel12 = getReferealtrackingCBS().trim();
                        ExtendedHyperlinkControlWrapper cpcreftrack = getPane()
                                    .getCtlCPCREFERALFREENEGLCHyperlink();
                        cpcreftrack.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd = getPane()
                                    .getCtlCPCREFERALFREENEGLCOUTPclayHyperlink();
                        cpcreftrackamd.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd2 = getPane()
                                    .getCtlCPCREFRALFREENEGADJHyperlink();
                        cpcreftrackamd2.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd4 = getPane()
                                    .getCtlCPCREFERELIMPLCCLAIMRECclayHyperlink();
                        cpcreftrackamd4.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd6 = getPane()
                                    .getCtlCPCREFERELILCSETTclayHyperlink();
                        cpcreftrackamd6.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd8 = getPane()
                                    .getCtlCPCREFERELILCCANHyperlink();
                        cpcreftrackamd8.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper csmreftrackk2 = getPane()
                                    .getCtlCPCREFERELELCADVclayHyperlink();
                        csmreftrackk2.setUrl(Hyperreferel12);
//                      ExtendedHyperlinkControlWrapper csmreftrackk4 = getPane()
//                                  .getCtlCPCREFERELELCDPclayHyperlink();
//                      csmreftrackk4.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper csmreftrackk6 = getPane()
                                    .getCtlCPCREFERELELCADJclayHyperlink();
                        csmreftrackk6.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper csmreftrackk8 = getPane()
                                    .getCtlCPCREFERELELCAMDclayHyperlink();
                        csmreftrackk8.setUrl(Hyperreferel12);
//                      ExtendedHyperlinkControlWrapper csmreftrackk10 = getPane()
//                                  .getCtlCPCREFRALELCSETTclayHyperlink();
//                      csmreftrackk10.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackam = getPane()
                                    .getCtlCPCREFERELOUTDOCCOLADJclayHyperlink();
                        cpcreftrackam.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd12 = getPane()
                                    .getCtlCPCREFERELOUTDOCCOLAMDclayHyperlink();
                        cpcreftrackamd12.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd13 = getPane()
                                    .getCtlCPCREFERELINWDOCCOLPAYclayHyperlink();
                        cpcreftrackamd13.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd15 = getPane()
                                    .getCtlCPCREFERELINWDOCCOLADJclayHyperlink();
                        cpcreftrackamd15.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper cpcreftrackamd17 = getPane()
                                    .getCtlCPCREFERELINWDOCCOLAMDclayHyperlink();
                        cpcreftrackamd17.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper csmreftrackamd23 = getPane()
                                    .getCtlCPCREFERELEGTADJclayHyperlink();
                        csmreftrackamd23.setUrl(Hyperreferel12);
                        ExtendedHyperlinkControlWrapper csmreftrackamd24 = getPane()
                                    .getCtlCPCREFERELEGTAMDclayHyperlink();
                        csmreftrackamd24.setUrl(Hyperreferel12);
                  } catch (Exception ees) {
                        // System.out.println("REFERE TRACKING REFERE CBS--------->" +
                        // ees.getMessage());
                  }

                  // FDenquiry link
                  String MasterReference = getDriverWrapper().getEventFieldAsText(
                              "MST", "r", "").trim(); //
                  String evnt = getDriverWrapper()
                              .getEventFieldAsText("EPF", "s", "");
                  String evvcount = getDriverWrapper().getEventFieldAsText("ESQ",
                              "i", "");

                  try {

                        String FDenquiryLink = FDenquiry().trim();
                        ExtendedHyperlinkControlWrapper fdlink = getPane()
                                    .getCtlFDENQUIRYIMPLCclayHyperlink();
                        fdlink.setUrl(FDenquiryLink);
                  } catch (Exception e) {
                        // System.out.println("FDenquiry link" + e.getMessage());
                  }

                  try {

                        String Preshipment = getHyperPreshipment();
                        ExtendedHyperlinkControlWrapper dmsh5 = getPane()
                                    .getCtlPreshipmentELCSETTclayHyperlink();
                        dmsh5.setUrl(Preshipment);

                  } catch (Exception e) {
                        // System.out.println("Exception in setting PreShipment url " +
                        // e.getMessage());
                  }

                  // PRESHIPMENT LINK
                  try {

                        String Preshipment = getHyperPreshipment();
                        ExtendedHyperlinkControlWrapper dmsh5 = getPane()
                                    .getCtlPreshipmentFREENEGLCOUTPclayHyperlink();
                        dmsh5.setUrl(Preshipment);

                  } catch (Exception e) {
                        // System.out.println("Exception in setting PreShipment url " +
                        // e.getMessage());
                  }

                  if (getMajorCode().equalsIgnoreCase("FRN")
                              && getMinorCode().equalsIgnoreCase("POF")) {
                        List<ExtEventShippingTable> shipcol = (List<ExtEventShippingTable>) getWrapper()
                                    .getExtEventShippingTable();
                        if (shipcol.size() > 0) {
                              // //System.out.println("shipdetails --->" + shipcol);

                              pane.getExtEventShippingTableDelete().setEnabled(false);
                        }
                  }
                  if (getMinorCode().equalsIgnoreCase("POF")) {
                        try {

                              String shipbillnum = "x";
                              String SHIPBILLDATE = "";
                              String PORTCODE = "";
                              String FORMNO = "";
                              String OUTSTANDINGAMOUNT = "";
                              String outStandingAmount = "";
                              // String outStandingAmount = "0.0 USD";
                              String currency = "USD";
                              String ShipAmount = "";
                              double outAmount = 0.0;
                              String outstr = "";
                              ArrayList<String> shippValues = new ArrayList<String>();
                              List<ExtEventShippingTable> shipingDetails = (List<ExtEventShippingTable>) getWrapper()
                                          .getExtEventShippingTable();

                              for (int i = 0; i < shipingDetails.size(); i++) {
                                    if (!step_csm.equalsIgnoreCase("Final print")
                                                && !eventStatus.equalsIgnoreCase("Completed")) {
                                          ExtEventShippingTable ship_Obj = shipingDetails
                                                      .get(i);
                                          SimpleDateFormat format = new SimpleDateFormat(
                                                      "dd-MM-yy");
                                          ShipAmount = String.valueOf(ship_Obj.getSHPAMT())
                                                      + " " + ship_Obj.getSHPAMTCurrency();
                                          OUTSTANDINGAMOUNT = ShipAmount;

                                          shipbillnum = ship_Obj.getBILLNUM().trim();
                                          // //System.out.println("shipbillnum.length()---->"
                                          // +
                                          // shipbillnum.length());
                                          shipbillnum = setxxforparameters(shipbillnum.trim());
                                          // //System.out.println("Shipping Bill Number "
                                          // +
                                          // shipbillnum);
                                          shippValues.add(shipbillnum);
                                          SHIPBILLDATE = format.format(ship_Obj.getBILLDAT());
                                          // //System.out.println("Shipping Bill Date " +
                                          // SHIPBILLDATE);
                                          SHIPBILLDATE = setxxforparameters(SHIPBILLDATE
                                                      .trim());
                                          // //System.out.println("Shipping Bill Date " +
                                          // SHIPBILLDATE);
                                          shippValues.add(SHIPBILLDATE);
                                          if (shipbillnum.length() > 1) {

                                                // //System.out.println("Shipping Bill
                                                // Number "
                                                // +
                                                // shipbillnum);

                                                String outStanding_Amnt = "SELECT OUTSAMT ,SHIPCCY FROM ETT_EDPMS_SHP_OUTSAMT WHERE SHIPBILLNUM='"
                                                            + shipbillnum
                                                            + "' and SHIPBILLDATE='"
                                                            + SHIPBILLDATE + "'";
                                                // // System.out.println( "Repayment Amount
                                                // // query
                                                // // outStanding_Amnt if loop----->" +
                                                // // outStanding_Amnt);
                                                con = ConnectionMaster.getConnection();
                                                ship_prepare = con
                                                            .prepareStatement(outStanding_Amnt);
                                                ship_result = ship_prepare.executeQuery();
                                                // //System.out.println("Query
                                                // executeQuery");
                                                while (ship_result.next()) {

                                                      outAmount = ship_result
                                                                  .getDouble("OUTSAMT");
                                                      outstr = ship_result.getString("OUTSAMT");
                                                      // // System.out.println("Outstanding
                                                      // // amount
                                                      // // while loop------>" + outAmount);

                                                      if (outAmount > 0) {
                                                            // //System.out.println("outAmount
                                                            // is if
                                                            // loop" + outAmount);
                                                            currency = ship_result.getString(2);
                                                            // //System.out.println("currency is
                                                            // " +
                                                            // currency);
                                                            // outStandingAmount =
                                                            // String.valueOf(outAmount / 10);
                                                            OUTSTANDINGAMOUNT = outstr + " "
                                                                        + currency;
                                                            // //System.out.println("Out Amount
                                                            // is"
                                                            // +
                                                            // OUTSTANDINGAMOUNT);
                                                            ship_Obj.setColumn("LOUTSAMT",
                                                                        OUTSTANDINGAMOUNT);
                                                            if (payaction.equalsIgnoreCase("Pay")) {
                                                                  // ship_Obj.setColumn("REPAYAM",
                                                                  // OUTSTANDINGAMOUNT);
                                                            } else {

                                                                  String repout = 0 + " " + currency;
                                                                  // // System.out.println("New
                                                                  // // Repayment Amount else loop"
                                                                  // // +
                                                                  // // repout);
                                                                  ship_Obj.setColumn("REPAYAM",
                                                                              repout);
                                                                  ship_Obj.setColumn("EQUBILL",
                                                                              repout);
                                                            }
                                                      } else {
                                                            // // System.out.println("outAmount
                                                            // // is
                                                            // // else loop" + outAmount);
                                                            currency = ship_result.getString(2);
                                                            // //System.out.println("currency is
                                                            // " +
                                                            // currency);
                                                            // outStandingAmount =
                                                            // String.valueOf(outAmount / 10);
                                                            OUTSTANDINGAMOUNT = 0 + " " + currency;
                                                            // System.out.println("Out Amount
                                                            // else"
                                                            // + OUTSTANDINGAMOUNT);
                                                            ship_Obj.setColumn("LOUTSAMT",
                                                                        OUTSTANDINGAMOUNT);
                                                            ship_Obj.setColumn("REPAYAM",
                                                                        OUTSTANDINGAMOUNT);
                                                            ship_Obj.setColumn("EQUBILL",
                                                                        OUTSTANDINGAMOUNT);
                                                            // //System.out.println("Repayment
                                                            // Amount
                                                            // collection else loop" +
                                                            // OUTSTANDINGAMOUNT);

                                                      }

                                                      // // System.out.println("Repayment
                                                      // // Amount
                                                      // // collection if loop" +
                                                      // // OUTSTANDINGAMOUNT);
                                                }
                                          }

                                          else {
                                                // // System.out.println("Repayment Amount
                                                // // collection else loop----->");
                                                PORTCODE = ship_Obj.getPORTCODDD();
                                                // //System.out.println("Port Code " +
                                                // PORTCODE);
                                                PORTCODE = setxxforparameters(PORTCODE.trim());
                                                // //System.out.println("Port Code " +
                                                // PORTCODE);
                                                shippValues.add(PORTCODE);
                                                FORMNO = ship_Obj.getFORMNUM();
                                                // //System.out.println("Form No" + FORMNO);
                                                FORMNO = setxxforparameters(FORMNO.trim());
                                                // //System.out.println("Form No" + FORMNO);
                                                shippValues.add(FORMNO);
                                                String outStanding_Amnt = "SELECT OUTSAMT ,SHIPCCY FROM ETT_EDPMS_SHP_OUTSAMT WHERE SHIPBILLNUM='"
                                                            + shipbillnum
                                                            + "' and SHIPBILLDATE='"
                                                            + SHIPBILLDATE
                                                            + "' AND PORTCODE='"
                                                            + PORTCODE
                                                            + "' AND FORMNO='"
                                                            + FORMNO
                                                            + "'";
                                                // // System.out.println("Repayment Amount
                                                // // query
                                                // // outStanding_Amnt else loop----->" +
                                                // // outStanding_Amnt);
                                                con = ConnectionMaster.getConnection();
                                                ship_prepare = con
                                                            .prepareStatement(outStanding_Amnt);
                                                ship_result = ship_prepare.executeQuery();
                                                while (ship_result.next()) {

                                                      outAmount = ship_result
                                                                  .getDouble("OUTSAMT");
                                                      outstr = ship_result.getString("OUTSAMT");

                                                      // //System.out.println("Outstanding
                                                      // amount
                                                      // while loop for form no------>" +
                                                      // outAmount);

                                                      if (outAmount > 0) {
                                                            // //System.out.println("outAmount
                                                            // is if
                                                            // loop" + outAmount);
                                                            currency = ship_result.getString(2);
                                                            // //System.out.println("currency is
                                                            // " +
                                                            // currency);
                                                            // outStandingAmount =
                                                            // String.valueOf(outAmount / 10);
                                                            OUTSTANDINGAMOUNT = outstr + " "
                                                                        + currency;
                                                            // // System.out.println("Out Amount
                                                            // // " +
                                                            // // OUTSTANDINGAMOUNT);
                                                            ship_Obj.setColumn("LOUTSAMT",
                                                                        OUTSTANDINGAMOUNT);
                                                            if (payaction.equalsIgnoreCase("Pay")) {
                                                                  // stem.out.println("Repayment
                                                                  // Amount CURRENCY
                                                                  // Checking=======>"
                                                                  // + OUTSTANDINGAMOUNT);

                                                                  // ship_Obj.setColumn("REPAYAM",
                                                                  // OUTSTANDINGAMOUNT);
                                                            } else {

                                                                  String repout = 0 + " " + currency;
                                                                  // // System.out.println("New
                                                                  // // Repayment Amount else loop"
                                                                  // // +
                                                                  // // repout);
                                                                  ship_Obj.setColumn("REPAYAM",
                                                                              repout);
                                                                  ship_Obj.setColumn("EQUBILL",
                                                                              repout);
                                                            }
                                                      } else {
                                                            // //System.out.println("outAmount
                                                            // is
                                                            // else loop" + outAmount);
                                                            currency = ship_result.getString(2);
                                                            // //System.out.println("currency is
                                                            // " +
                                                            // currency);
                                                            // outStandingAmount =
                                                            // String.valueOf(outAmount / 10);
                                                            OUTSTANDINGAMOUNT = 0 + " " + currency;
                                                            // //System.out.println("Out Amount
                                                            // " +
                                                            // OUTSTANDINGAMOUNT);
                                                            ship_Obj.setColumn("LOUTSAMT",
                                                                        OUTSTANDINGAMOUNT);
                                                            ship_Obj.setColumn("REPAYAM",
                                                                        OUTSTANDINGAMOUNT);
                                                            ship_Obj.setColumn("EQUBILL",
                                                                        OUTSTANDINGAMOUNT);

                                                      }

                                                }
                                          }
                                    } else {
                                          // // System.out.println("Event Status is
                                          // // Completed");
                                    }
                              }

                        } catch (Exception e) {
                              // // System.out.println("Exception in setting outsing
                              // // amount
                              // // " + e.getMessage());
                        } finally {
                              try {

                                    if (ship_result != null)
                                          ship_result.close();
                                    if (ship_prepare != null)
                                          ship_prepare.close();
                                    if (con != null)
                                          con.close();

                              } catch (SQLException e) {
                                    // System.out.println("Connection Failed! Check
                                    // output
                                    // console");
                                    e.printStackTrace();
                              }
                        }
                  }
                  if (getMajorCode().equalsIgnoreCase("FRN")) {
                        // value for LOB
                        try {
                              getLOBFreeNeg();

                        } catch (Exception ee) {
                              // System.out.println("Exception LOB Catch" +
                              // ee.getMessage());
                        }
                  }

                  try {
                        getLOBISSUE();

                  } catch (Exception ee) {
                        System.out.println(ee.getMessage());
                  }

                  if ((getMinorCode().equalsIgnoreCase("FNF") || getMinorCode()
                              .equalsIgnoreCase("POF"))) {
                        try {
                              String finaceCreate = getDriverWrapper()
                                          .getEventFieldAsText("BFC", "l", "");
                              String limitblocking = getDriverWrapper()
                                          .getEventFieldAsText("cBNR", "s", "");
                              if (finaceCreate.equalsIgnoreCase("Y")
                                          && !finaceCreate.equalsIgnoreCase("N")) {
                                    String facility = getWrapper().getLIMITID().trim();

                                    /*
                                     * if (dailyval_Log.equalsIgnoreCase("YES")) {
                                     * //System.out .println(
                                     * "ELC finance limit id validation===>" + facility);
                                     * //System.out.println(
                                     * "ELC LOB code finance created validation=======>" +
                                     * finaceCreate); }
                                     */

                                    // if (finaceCreate.equalsIgnoreCase("Y") && (facility
                                    // == null || facility.equalsIgnoreCase(""))
                                    // && (step_csm.equalsIgnoreCase("CBS Maker")
                                    // || step_csm.equalsIgnoreCase("CBS Maker 1"))&&
                                    // !limitblocking.equalsIgnoreCase("4")) {
                                    // validationDetails.addWarning(ValidationDetails.WarningType.Other,
                                    // "Please input the LOB limit id which is attached in
                                    // credit facility [CM]");
                                    // }
                                    if (facility != null && !facility.equalsIgnoreCase("")) {
                                          try {
                                                try {
                                                      int count = 0;
                                                      String query = "select count(*) from XMLAPISTO where FACILITYID='"
                                                                  + facility + "'";
                                                      con = getConnection();
                                                      ps1 = con.prepareStatement(query);
                                                      rs1 = ps1.executeQuery();
                                                      if (rs1.next()) {
                                                            count = rs1.getInt(1);

                                                      }

                                                      /*
                                                       * if ((count == 0 || count < 1) &&
                                                       * (step_csm .equalsIgnoreCase(
                                                       * "CBS Authoriser"))) { validationDetails
                                                       * .addWarning(ValidationDetails
                                                       * .WarningType.Other,
                                                       * "Please enter valid LOB limit id [CM]");
                                                       * }
                                                       */

                                                } catch (Exception e) {
                                                      // System.out
                                                      // .println("Exception ODC finance limit id
                                                      // validation===>"
                                                      // + e.getMessage());
                                                }

                                                try {
                                                      String lobCreate = getWrapper().getLOB()
                                                                  .trim();
                                                      String lobfinance = "";
                                                      String query = "SELECT exte.LOB FROM master mas, BASEEVENT bev, BASEEVENT bev1, EXTEVENT exte WHERE mas.KEY97 = bev.MASTER_KEY AND bev.KEY97 = bev1.ATTACHD_EV AND bev1.KEY97 = exte.EVENT AND trim(exte.LOB) IS NOT NULL AND mas.MASTER_REF = '"
                                                                  + MasterReference + "'";
                                                      /*
                                                       * if (dailyval_Log.equalsIgnoreCase("YES"))
                                                       * { //System.out.println(
                                                       * "ELC Attched finance LOB code===>" +
                                                       * query); }
                                                       */
                                                      con = getConnection();
                                                      ps1 = con.prepareStatement(query);
                                                      rs1 = ps1.executeQuery();
                                                      if (rs1.next()) {
                                                            lobfinance = rs1.getString(1).trim();

                                                      }

                                                } catch (Exception e) {
                                                      System.out
                                                                  .println("Exception ODC authorized validation===>"
                                                                              + e.getMessage());
                                                }

                                          } catch (Exception e) {
                                                System.out
                                                            .println("Exception in connection closed===>"
                                                                        + e.getMessage());
                                          } finally {
                                                try {

                                                      if (rs1 != null)
                                                            rs1.close();
                                                      if (ps1 != null)
                                                            ps1.close();
                                                      if (con != null)
                                                            con.close();
                                                } catch (SQLException e) {
                                                      // System.out.println("Connection Failed!
                                                      // Check output
                                                      // console");
                                                      e.printStackTrace();
                                                }
                                          }
                                    } else {
                                          /*
                                           * if (dailyval_Log.equalsIgnoreCase("YES")) { //
                                           * System .out.println(
                                           * "ELC finance limit id blank===>"); }
                                           */
                                    }

                              } else {
                                    /*
                                     * if (dailyval_Log.equalsIgnoreCase("YES")) {
                                     * //System.out.println("ELC finance not created===>" +
                                     * finaceCreate); }
                                     */
                              }

                        } catch (Exception e) {
                              // System.out.println("Exception ELC finance validation===>"
                              // + e.getMessage());
                        }

                  }

                  if (getMinorCode().equalsIgnoreCase("FNF")
                              || getMinorCode().equalsIgnoreCase("POF")) {
                        try {
                              getrtgsNEFT();

                        } catch (Exception e) {
                              // System.out.println("Exception in purpose
                              // code--------->"
                              // + e.getMessage());
                        }
                  }

                  try {
                        getPerdChgEndDate();

                  } catch (Exception ee) {
                        // System.out.println(ee.getMessage());
                  }

                  try {

                        getGoodsCategory();

                  } catch (Exception ee) {
                        // System.out.println(ee.getMessage());
                        // //System.out.println("LOB Catch");
                  }

                  try {
                        currencyCalc();
                  } catch (Exception e) {
                        /*
                         * if (dailyval_Log.equalsIgnoreCase("YES")) { //
                         * System.out.println (
                         * "Exception in currency value population---->" +
                         * e.getMessage());
                         *
                         * }
                         */
                  }

                  try {
                        // //System.out.println("getChargeBasis");
                        if (getMajorCode().equalsIgnoreCase("ILC")) {
                              getChargeBasis();
                        }

                  } catch (Exception ee) {
                        // System.out.println(ee.getMessage());
                        // //System.out.println("Service TAX Catch");
                  }

                  try {
                        // //System.out.println("getChargeBasis");
                        if (getMajorCode().equalsIgnoreCase("ILC")
                                    && getMinorCode().equalsIgnoreCase("NAMI")) {

                              String newLia = getDriverWrapper().getEventFieldAsText(
                                          "cAAX", "v", "m");
                              String oldLia = getDriverWrapper().getEventFieldAsText(
                                          "cACP", "v", "m");
                              String newCur = getDriverWrapper().getEventFieldAsText(
                                          "cAAX", "v", "c");
                              /*
                               * if (dailyval_Log.equalsIgnoreCase("YES")) {
                               *
                               * //System.out.println("New total liability" + newLia);
                               * //System.out.println("Old total liability" + oldLia); }
                               */
                              BigDecimal newVal = new BigDecimal(newLia);
                              BigDecimal oldVal = new BigDecimal(oldLia);

                              BigDecimal newValue = newVal.subtract(oldVal);
                              ConnectionMaster connectionMaster = new ConnectionMaster();
                              String divideByDecimal = connectionMaster
                                          .getDecimalforCur(newCur);
                              if (divideByDecimal.equalsIgnoreCase("1")) {

                                    DecimalFormat diff = new DecimalFormat("0");
                                    diff.setMaximumFractionDigits(1);
                                    String totalVal = diff.format(newValue);
                                    /*
                                     * if (dailyval_Log.equalsIgnoreCase("YES")) { //
                                     * System. out.println(
                                     * "New Total liability final value<==1==>" + totalVal +
                                     * "" + newCur); }
                                     */
                                    if (newValue.compareTo(BigDecimal.ZERO) > 0) {
                                          getPane().setINTLIAMT(totalVal + " " + newCur);
                                    } else {
                                          totalVal = "0";
                                          getPane().setINTLIAMT(totalVal + " " + newCur);
                                          // System.out.println("New Total liability final
                                          // value<==3==>"
                                          // + totalVal + "" + newCur);
                                    }
                                    if (newValue.compareTo(BigDecimal.ZERO) > 0) {
                                          getPane().setINTLIAMT(totalVal + " " + newCur);
                                    } else {
                                          totalVal = "0";
                                          getPane().setINTLIAMT(totalVal + " " + newCur);

                                    }
                              } else {

                                    DecimalFormat diff = new DecimalFormat("0.00");
                                    diff.setMaximumFractionDigits(2);
                                    String totalVal = diff.format(newValue);
                                    /*
                                     * if (dailyval_Log.equalsIgnoreCase("YES")) { //
                                     * System. out.println(
                                     * "New Total liability final value<==2==>" + totalVal +
                                     * "" + newCur); }
                                     */
                                    if (newValue.compareTo(BigDecimal.ZERO) > 0) {
                                          getPane().setINTLIAMT(totalVal + " " + newCur);
                                    } else {
                                          totalVal = "0";
                                          getPane().setINTLIAMT(totalVal + " " + newCur);

                                    }

                              }

                        }

                  } catch (Exception e) {
                        System.out.println("Exception New total liability"
                                    + e.getMessage());

                  }

                  // WORKFLOW

                  if (getMinorCode().equalsIgnoreCase("POF")
                              && step_Id.equalsIgnoreCase("CSM")) {

                        try {
                              List<ExtEventShippingTable> shipTab = (List<ExtEventShippingTable>) getWrapper()
                                          .getExtEventShippingTable();
                              // //System.out.println("shipping table for notional
                              // rate---->" + shipTable.size());
                              String repval_new = "2";
                              String notionalrate = "1";
                              for (int i = 0; i < shipTab.size(); i++) {

                                    ExtEventShippingTable ship = shipTab.get(i);
                                    BigDecimal outStandAmt = ship.getLOUTSAMT();
                                    String outStandccy = ship.getLOUTSAMTCurrency()
                                                .toString();
                                    repval_new = ship.getREPTYPE().toString();
                                    // // System.out.println("shipping repayment in CSM step
                                    // // if====>" + outStandAmt + "" + payaction);

                                    ship.setREPAYAM(outStandAmt);
                                    // System.out.println("outstanding=====>"+outStandAmt);
                                    ship.setREPAYAMCurrency(outStandccy);
                                    String shipcol = "0";
                                    ship.setSHCOLAM(new BigDecimal(Double.valueOf(shipcol)));
                                    ship.setSHCOLAMCurrency(outStandccy);
                                    ship.setNOTIONAL(new BigDecimal(Double
                                                .valueOf(notionalrate)));
                                    // // System.out.println("Notional rate in CSM step
                                    // // if====>" + ship.getNOTIONAL());
                                    if (repval_new.equalsIgnoreCase("2")
                                                || repval_new.equalsIgnoreCase("")
                                                && (!repval_new.equalsIgnoreCase("1") || !repval_new
                                                            .equalsIgnoreCase("Part"))) {
                                          // // System.out.println("Repayment type in CSM step
                                          // // if====>" + repval_new);
                                          ship.setREPTYPE(repval_new);
                                    } else {
                                          // // System.out.println("Repayment type in CSM step
                                          // // else loop====>" + repval_new);
                                    }
                              }
                        } catch (Exception e) {
                              // System.out.println("Exception e " + e.getMessage());
                        }
                  } else {
                        // // System.out.println("shipping repayment in CSM step
                        // // else====>" + payaction);
                  }

                  String productcode = getDriverWrapper().getEventFieldAsText("PCO",
                              "s", "").trim(); // ILC
                  String eventCode = getDriverWrapper().getEventFieldAsText("EVR",
                              "r", "").trim(); // ISS001
                  String subproductCode = getDriverWrapper().getEventFieldAsText(
                              "PTP", "s", "").trim();// ILF
                  MasterReference = getDriverWrapper().getEventFieldAsText("MST",
                              "r", "").trim(); //
                  evnt = getDriverWrapper().getEventFieldAsText("EPF", "s", "");
                  evvcount = getDriverWrapper().getEventFieldAsText("ESQ", "i", "");
                  // //System.out.println("Event reference for evvcount----> " +
                  // evvcount);
                  // workflow error configuration
                  try {

                        int count = 0;

                        if (step_Id.equalsIgnoreCase("CSM")
                                    || step_Id.equalsIgnoreCase("AdhocCSM")) {
                              String query_wrk = "select count(*) from ETT_WF_CHKLST_TRACKING where MASTER_REF='"
                                          + MasterReference
                                          + "' and EVENTREF='"
                                          + eventCode
                                          + "' and PROD_CODE='"
                                          + productcode
                                          + "' and SUB_PRODUCT_CODE='"
                                          + subproductCode
                                          + "' and INIT_AT='CSM'";
                              /*
                               * if (dailyval_Log.equalsIgnoreCase("YES")) {
                               *
                               * System.out.println("ETT_WF_CHKLST_TRACKING query" +
                               * query_wrk); }
                               */
                              con = getConnection();
                              ps1 = con.prepareStatement(query_wrk);
                              rs1 = ps1.executeQuery();
                              while (rs1.next()) {
                                    count = rs1.getInt(1);
                                    // //System.out.println("ETT_WF_CHKLST_TRACKING in while
                                    // loop----> " + count3);

                              }
                        } else if (step_Id.equalsIgnoreCase("CBS Maker")) {
                              String query_wrk = "select count(*) from ETT_WF_CHKLST_TRACKING where MASTER_REF='"
                                          + MasterReference
                                          + "' and EVENTREF='"
                                          + eventCode
                                          + "' and PROD_CODE='"
                                          + productcode
                                          + "' and SUB_PRODUCT_CODE='"
                                          + subproductCode
                                          + "' and INIT_AT='CBS Maker'";
                              /*
                               * if (dailyval_Log.equalsIgnoreCase("YES")) {
                               *
                               * //System.out.println("ETT_WF_CHKLST_TRACKING query" +
                               * query_wrk); }
                               */
                              con = getConnection();
                              ps1 = con.prepareStatement(query_wrk);
                              rs1 = ps1.executeQuery();
                              while (rs1.next()) {
                                    count = rs1.getInt(1);
                                    // //System.out.println("ETT_WF_CHKLST_TRACKING in while
                                    // loop----> " + count3);

                              }
                        }

                        if (count < 1
                                    && step_Input.equalsIgnoreCase("i")
                                    && (step_Id.equalsIgnoreCase("CBS Maker")
                                                || step_Id.equalsIgnoreCase("CSM") || step_Id
                                                      .equalsIgnoreCase("AdhocCSM"))
                                    && (!getMajorCode().equalsIgnoreCase("SHG"))
                                    && (getMinorCode().equalsIgnoreCase("ADE")
                                                || getMinorCode().equalsIgnoreCase("FNF")
                                                || getMinorCode().equalsIgnoreCase("ELCD")
                                                || getMinorCode().equalsIgnoreCase("NAJF") || getMinorCode()
                                                .equalsIgnoreCase("POF"))) {

                              validationDetails.addError(ErrorType.Other,
                                          "Workflow checklist is mandatory  [CM]");
                        }

                  } catch (Exception e) {

                        /*
                         * if (dailyval_Log.equalsIgnoreCase("YES")) {
                         *
                         * // System.out.println("Exception ETT_WF_CHKLST_TRACKING" +
                         * e.getMessage()); }
                         */
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
                              // // System.out.println("Connection Failed! Check output
                              // // console");
                              e.printStackTrace();
                        }
                  }

                  // Non Constituent borrower-CONSBOR
                  try {
                        String constituent = getDriverWrapper().getEventFieldAsText(
                                    "BEN", "p", "no").trim();
                        // //System.out.println("Non Constituent borrower
                        // constituent--------->" +
                        // constituent);
                        if (constituent.length() > 0) {
                              // //System.out.println("Non Constituent borrower
                              // constituent--------->" + constituent);
                              String alpha_val = constituent.replaceAll("[^A-Za-z]+", "");
                              // //System.out.println("alpha_val---->" + alpha_val);
                              if (alpha_val.length() > 0) {
                                    String alpha_fin = alpha_val.substring(0, 4);
                                    // //System.out.println("Non Constituent borrower
                                    // constituent
                                    // after convert--------->" + alpha_fin);
                                    if (getMajorCode().equalsIgnoreCase("ELC")) {
                                          if (alpha_fin.equalsIgnoreCase("LCUS")) {
                                                getPane().setCONSBORR(true);
                                                // //System.out.println(" Non Constituent
                                                // borrower
                                                // ELC
                                                // if
                                                // loop--------->");
                                          } else {
                                                getPane().setCONSBORR(false);
                                          }
                                    } else {
                                          getPane().setCONSBORR(false);
                                          // //System.out.println(" Non Constituent borrower
                                          // not
                                          // in
                                          // ELC
                                          // else--------->");
                                    }
                              } else {
                                    getPane().setCONSBORR(false);
                              }
                        } else {

                              getPane().setCONSBORR(false);

                              // //System.out.println(" Non Constituent borrower is
                              // empty--------->");
                        }
                  } catch (Exception e) {

                  }

                  // Invoice details validation
                  String systemDat = getDriverWrapper().getEventFieldAsText("TDY",
                              "d", "");
                  try {
                        if ((step_Input.equalsIgnoreCase("i"))
                                    && (step_Id.equalsIgnoreCase("CBS Maker"))
                                    && getMajorCode().equalsIgnoreCase("IDC")) {
                              // //System.out.println("Invoice Validate Called");
                              List<ExtEventInvoiceData> Invoic = (List<ExtEventInvoiceData>) getWrapper()
                                          .getExtEventInvoiceData();
                              if (Invoic.size() < 1
                                          && (getMinorCode().equalsIgnoreCase("NCAJ") || getMinorCode()
                                                      .equalsIgnoreCase("NCAM"))) {
                                    validationDetails.addError(ErrorType.Other,
                                                "Invoice details is mandatory  [CM]");
                              }

                              else {
                                    // // System.out.println("Invoice data is not empty");
                              }
                        }

                        List<ExtEventInvoicelc> invoice1 = (List<ExtEventInvoicelc>) getWrapper()
                                    .getExtEventInvoicelc();

                        if (invoice1.size() == 0 || invoice1.size() < 1) {
                              if (getMajorCode().equalsIgnoreCase("ILC")
                                          && getMinorCode().equalsIgnoreCase("CRC")
                                          && (step_Input.equalsIgnoreCase("i"))
                                          && (step_Id.equalsIgnoreCase("CBS Maker") || step_Id
                                                      .equalsIgnoreCase("CBS Maker 1"))) {

                                    validationDetails.addWarning(WarningType.Other,
                                                "Invoice details grid is mandatory[CM]");
                              }
                        }

                        String payamount = "";
                        double amtVal = 0;
                        double invadob = 0;
                        double final_payamountlong = 0;
                        for (int l = 0; l < invoice1.size(); l++) {
                              ExtEventInvoicelc invoicedetails = invoice1.get(l);
                              payamount = getDriverWrapper().getEventFieldAsText("AMT",
                                          "v", "m");
                              // //System.out.println("Collection amount ----->" +
                              // payamt);
                              String paycur = getDriverWrapper().getEventFieldAsText(
                                          "AMT", "v", "c");
                              double invamt = invoicedetails.getINVAM().doubleValue();
                              // //System.out.println("Invoice grid initial amount ----->"
                              // +
                              // invamt);
                              /*
                               * if (dailyval_Log.equalsIgnoreCase("YES")) {
                               *
                               * //System.out.println("Invoice grid initial amount ----->"
                               * + invamt); }
                               */
                              String invcurr = invoicedetails.getINVAMCurrency();

                              amtVal = amtVal + invamt;
                              // //System.out.println("Invoice amount after add ----->" +
                              // amount);
                              /*
                               * if (dailyval_Log.equalsIgnoreCase("YES")) {
                               *
                               * //System.out.println("Invoice amount after ad ----->" +
                               * amtVal); }
                               */

                              ConnectionMaster connectionMaster = new ConnectionMaster();
                              double divideByDecimal = connectionMaster
                                          .getDecimalforCurrency(invcurr);
                              invadob = amtVal / divideByDecimal;

                              final_payamountlong = Double.parseDouble(payamount);
                              // //System.out.println("payamtflot for conpare ----->" +
                              // final_payamtlong + "<<");
                              // //System.out.println("invoice final amount for conpare
                              // ----->"
                              // + invadob_tot + "<<");

                              /*
                               * if (dailyval_Log.equalsIgnoreCase("YES")) {
                               *
                               * // System.out.println("payamtflot for conpare ----->" +
                               * final_payamountlong + "<<"); // System.out.println(
                               * "invoice final amount for conpare ----->" + invadob +
                               * "<<"); }
                               */
                              String deteNeg = String.valueOf(invoicedetails.getINDTE());
                              // System.out.println("Invoice date " + deteNeg
                              // +" SIZE "+invoice1.size());

                              if (invoice1.size() > 0 && (!deteNeg.isEmpty())
                                          && (getMajorCode().equalsIgnoreCase("ILC"))
                                          && (getMinorCode().equalsIgnoreCase("CRC"))
                                          && (step_Input.equalsIgnoreCase("i"))
                                          && (step_Id.equalsIgnoreCase("CBS Maker"))) {
                                    if ((deteNeg
                                                .matches("(^(3[01]|[12][0-9]|0[1-9])/(1[0-2]|0[1-9])/[0-9]{4}$)"))) {

                                          // System.out.println("Date of invoice is valid ");
                                    } else {
                                          // //System.out.println("Date of invoice is not
                                          // valid
                                          // ");
                                          validationDetails
                                                      .addError(ErrorType.Other,
                                                                  "Invalid Date format(DD/MM/YYYY) in Invoice date grid  [CM]");

                                    }
                              } else {
                                    // System.out.println("Date of invoice is empty valid
                                    // ");

                              }
                              String invdate = String.valueOf(invoicedetails.getINDTE());
                              if (invoice1.size() > 0) {
                                    // System.out.println("Invoice Date---> " + invdate);
                                    try {
                                          SimpleDateFormat formatter1 = new SimpleDateFormat(
                                                      "dd/MM/yy");
                                          Date shipbillDate = (Date) formatter1
                                                      .parse(invdate);
                                          Date sysDate = formatter1.parse(systemDat);
                                          // System.out.println("sysDate Date---> " +
                                          // sysDate);
                                          // System.out.println("shipbillDate -----> " +
                                          // shipbillDate);
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                // System.out.println("system Date for Invoice
                                                // validation---> "
                                                // + sysDate);
                                          }
                                          if (invoice1.size() > 0
                                                      && shipbillDate.after(sysDate)
                                                      && getMajorCode().equalsIgnoreCase("ILC")) {
                                                // System.out.println("date1 is before Date2");
                                                validationDetails
                                                            .addError(ErrorType.Other,
                                                                        "Invoice date is future date than the today date [CM]");
                                          } else {

                                                if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                      // System.out.println("----date3 is after
                                                      // Date2 shipbillDate -----> "
                                                      // + shipbillDate);
                                                }
                                          }
                                    } catch (Exception e) {

                                          if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                // System.out.println("Invoice date is future
                                                // date"
                                                // + e.getMessage());
                                          }
                                    }
                              } else {
                                    // System.out.println("sysDate Date else---> ");

                              }

                        }
                        if (dailyval_Log.equalsIgnoreCase("YES")) {

                              // System.out.println("invoice and master amount not
                              // equal---->"
                              // + invadob + "" + final_payamountlong);
                        }
                        if (invadob > 0 && (invadob != final_payamountlong)) {

                              if (getMajorCode().equalsIgnoreCase("ILC")
                                          && getMinorCode().equalsIgnoreCase("CRC")
                                          && (step_Input.equalsIgnoreCase("i"))
                                          && (step_Id.equalsIgnoreCase("CBS Maker") || step_Id
                                                      .equalsIgnoreCase("CBS Maker 1"))) {

                                    validationDetails
                                                .addWarning(WarningType.Other,
                                                            "Claim amount should be equal to Invoice grid amount [CM]");
                              }
                        } else {
                              if (dailyval_Log.equalsIgnoreCase("YES")) {

                                    // System.out.println(
                                    // "invoice and master amount not equal else===>" +
                                    // invadob + "" + final_payamountlong);
                              }
                        }
                  } catch (Exception e) {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              // System.out.println("Exception invoice and master amount
                              // not equal"
                              // + e.getMessage());
                        }
                  }

                  // INVOICE VALIDATION
                  try {

                        double invadob_tot = 0.0;
                        double amount = 0.0;
                        String payamt = "0";
                        double final_payamtlong = 0.0;
                        if (getMinorCode().equalsIgnoreCase("NCAJ")
                                    || getMinorCode().equalsIgnoreCase("NCAM")
                                    && getMajorCode().equalsIgnoreCase("IDC")) {

                              List<ExtEventInvoiceData> invoice = (List<ExtEventInvoiceData>) getWrapper()
                                          .getExtEventInvoiceData();

                              for (int l = 0; l < invoice.size(); l++) {
                                    ExtEventInvoiceData invoicedat = invoice.get(l);
                                    payamt = getDriverWrapper().getEventFieldAsText("AMT",
                                                "v", "m");
                                    // //System.out.println("Collection amount ----->" +
                                    // payamt);
                                    String paycur = getDriverWrapper().getEventFieldAsText(
                                                "AMT", "v", "c");
                                    double invamt = invoicedat.getINAMOUT().doubleValue();
                                    // //System.out.println("Invoice grid initial amount
                                    // ----->"
                                    // +
                                    // invamt);
                                    String invcurr = invoicedat.getINAMOUTCurrency();

                                    amount = amount + invamt;
                                    // //System.out.println("Invoice amount after add
                                    // ----->" +
                                    // amount);

                                    ConnectionMaster connectionMaster = new ConnectionMaster();
                                    double divideByDecimal = connectionMaster
                                                .getDecimalforCurrency(invcurr);
                                    invadob_tot = amount / divideByDecimal;

                                    final_payamtlong = Double.parseDouble(payamt);
                                    // //System.out.println("payamtflot for conpare ----->"
                                    // +
                                    // final_payamtlong + "<<");
                                    // //System.out.println("invoice final amount for
                                    // conpare
                                    // ----->"
                                    // + invadob_tot + "<<");

                                    if ((!paycur.equalsIgnoreCase(invcurr))
                                                && getMinorCode().equalsIgnoreCase("NCAJ")
                                                || getMinorCode().equalsIgnoreCase("NCAM")
                                                && getMajorCode().equalsIgnoreCase("IDC")) {
                                          // validationDetails.addError(ErrorType.Other,
                                          // "Collection
                                          // amount currency should be equal to Invoice grid
                                          // currency
                                          // [CM]");
                                    }

                              }

                              if (dailyval_Log.equalsIgnoreCase("YES")) {

                                    // System.out.println(
                                    // " invoice and master amount not equal amend" +
                                    // invadob_tot + "" + final_payamtlong);
                              }

                              if ((invadob_tot != final_payamtlong)) {
                                    // //System.out.println(" invoice and master amount not
                                    // equal
                                    // ---->1" + invadob_tot + "" + final_payamtlong);
                                    if (getMinorCode().equalsIgnoreCase("NCAJ")
                                                || getMinorCode().equalsIgnoreCase("NCAM")
                                                && getMajorCode().equalsIgnoreCase("IDC")
                                                && (step_Input.equalsIgnoreCase("i"))
                                                && (step_Id.equalsIgnoreCase("CBS Maker") || step_Id
                                                            .equalsIgnoreCase("CBS Maker 1"))) {

                                          validationDetails
                                                      .addWarning(WarningType.Other,
                                                                  "Collection amount should be equal to Invoice grid amount [CM]");
                                    }
                              } else {

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {

                                          // System.out.println(" invoice and master amount
                                          // not equal amend else"
                                          // + invadob_tot + ""
                                          // + final_payamtlong);
                                    }
                              }

                        }
                        List<ExtEventInvoiceData> invoice = (List<ExtEventInvoiceData>) getWrapper()
                                    .getExtEventInvoiceData();
                        for (int j = 0; j < invoice.size(); j++) {
                              ExtEventInvoiceData invoicedat = invoice.get(j);
                              String deteNeg = String.valueOf(invoicedat.getINDATE());
                              // //System.out.println("Invoice date " + deteNeg);

                              if (invoice.size() > 0 && (!deteNeg.isEmpty())
                                          && (getMajorCode().equalsIgnoreCase("IDC"))
                                          && (step_Input.equalsIgnoreCase("i"))
                                          && (step_Id.equalsIgnoreCase("CBS Maker"))) {
                                    if ((deteNeg
                                                .matches("(^(3[01]|[12][0-9]|0[1-9])/(1[0-2]|0[1-9])/[0-9]{4}$)"))) {

                                    } else {
                                          // //System.out.println("Date of invoice is not
                                          // valid
                                          // ");
                                          validationDetails
                                                      .addError(ErrorType.Other,
                                                                  "Invalid Date format(DD/MM/YYYY) in Invoice date grid  [CM]");

                                    }
                              } else {

                                    if (dailyval_Log.equalsIgnoreCase("YES")) {

                                          // System.out.println("Date of invoice is empty
                                          // valid amend");
                                    }

                              }
                              String invdate = String.valueOf(invoicedat.getINDATE());
                              if (invoice.size() > 0) {
                                    // System.out.println("Invoice Date---> " + invdate);
                                    try {
                                          SimpleDateFormat formatter1 = new SimpleDateFormat(
                                                      "dd/MM/yy");
                                          Date shipbillDate = (Date) formatter1
                                                      .parse(invdate);
                                          Date sysDate = formatter1.parse(systemDat);
                                          // System.out.println("sysDate Date---> " +
                                          // sysDate);
                                          // System.out.println("shipbillDate -----> " +
                                          // shipbillDate);
                                          if (invoice.size() > 0
                                                      && shipbillDate.after(sysDate)
                                                      && (getMajorCode().equalsIgnoreCase("IDC"))) {
                                                // System.out.println("date1 is before Date2");
                                                validationDetails
                                                            .addError(ErrorType.Other,
                                                                        "Invoice date is future date than the today date [CM]");
                                          } else {
                                                // System.out.println("----date3 is after Date2
                                                // shipbillDate -----> " + shipbillDate + ""
                                                // + invoice.size());
                                          }
                                    } catch (Exception e) {
                                          if (dailyval_Log.equalsIgnoreCase("YES")) {

                                                // System.out.println("Date of invoice is empty
                                                // valid amend"
                                                // + e.getMessage());
                                          }
                                    }
                              } else {

                              }

                        }
                  } catch (Exception e) {
                        // System.out.println(e.getMessage());
                        if (dailyval_Log.equalsIgnoreCase("YES")) {

                              // System.out.println("Exception in invoice valid amend" +
                              // e.getMessage());
                        }
                  }
                  String masterRefNumber = getDriverWrapper().getEventFieldAsText(
                              "MST", "r", "");
                  String eventPrefix = getDriverWrapper().getEventFieldAsText("EVCD",
                              "s", "");

                  // get from and to fields to shipping details
                  if ((getMajorCode().equalsIgnoreCase("FRN") || getMinorCode()
                              .equalsIgnoreCase("FNF"))
                              || (getMajorCode().equalsIgnoreCase("ILC") || getMinorCode()
                                          .equalsIgnoreCase("CRC"))) {
                        try {
                              System.out.println("Inside Goods Star2t");
                              // //System.out.println("entered from and to population ");

                              String Fromcity = getDriverWrapper().getEventFieldAsText(
                                          "SDF", "s", "");
                              // //System.out.println("from city" + Fromcity);
                              String Tocity = getDriverWrapper().getEventFieldAsText(
                                          "SDT", "s", "");
                              // //System.out.println("to city " + Tocity);
                              String gooddesc = getDriverWrapper().getEventFieldAsText(
                                          "cAIJ", "s", "");
                              // //System.out.println("goods description" + gooddesc);
                              String goodcode = getDriverWrapper().getEventFieldAsText(
                                          "cAKM", "s", "");
                              getPane().setDESGOODS(gooddesc);
                              getPane().setGOODT(goodcode);
                              getPane().setSHIPFMLC(Fromcity);
                              getPane().setSHIPTOLC(Tocity);
                              System.out.println("Inside Goods End 2");

                              // //System.out.println("hscode Value in try---->" +
                              // hscodeval);
                              String hyperValue = "select lcm.TAX_PAY_BY,lcm.OUR_CHGS,lcm.OVR_CHGS,lcm.LOADING,lcm.DISCHARGE from master mas, lcmaster lcm where mas.KEY97 = lcm.KEY97 and mas.MASTER_REF  = '"
                                          + masterRefNumber + "'";
                              // //System.out.println("TI field query Value---->" +
                              // hyperValue);
                              con = ConnectionMaster.getConnection();
                              ps1 = con.prepareStatement(hyperValue);
                              rs = ps1.executeQuery();
                              if (rs.next()) {
                                    String taxvale = rs.getString(1);
                                    String ourchsrge = rs.getString(2);
                                    String overses = rs.getString(3);
                                    String loading = rs.getString(4);
                                    String dischrge = rs.getString(5);
                                    // //System.out.println("port code description---->" +

                                    if (taxvale.equalsIgnoreCase("P")) {
                                          getPane().setTAXPAID("Charge payer");
                                    } else {
                                          getPane().setTAXPAID("Customer");
                                    }
                                    if (ourchsrge.equalsIgnoreCase("A")) {
                                          getPane().setOURS("Applicant");
                                    } else {
                                          getPane().setOURS("Beneficiary");
                                    }
                                    if (overses.equalsIgnoreCase("A")) {
                                          getPane().setOVERSES("Applicant");
                                    } else {
                                          getPane().setOVERSES("Beneficiary");
                                    }

                                    if (loading != null && loading.length() > 0) {
                                          getPane().setSHPMTFRM(loading);
                                    }
                                    if (dischrge != null && dischrge.length() > 0) {
                                          getPane().setSHIPMTTO(dischrge);
                                    }
                              }

                        } catch (Exception e) {
                              // System.out.println(e.getMessage());
                        } finally {
                              try {
                                    if (rs != null)
                                          rs.close();
                                    if (ps1 != null)
                                          ps1.close();
                                    if (con != null)
                                          con.close();
                              } catch (SQLException e) {
                                    // // System.out.println("Connection Failed! Check
                                    // output
                                    // // console");
                                    e.printStackTrace();
                              }
                        }
                  } else {
                        // // System.out.println("--------->" + getMajorCode() +
                        // // getMinorCode());

                  }

                  // port code population
                  String portval = getWrapper().getPORTCOD_Name().trim();
                  // //System.out.println("Port Value---->" + portval);
                  if ((!portval.equalsIgnoreCase("")) && portval != null) {
                        try {
                              // //System.out.println("hscode Value in try---->" +
                              // hscodeval);
                              String hyperValue = "SELECT trim(PNAME),trim(COUNTRY) FROM EXTPORTCO WHERE PCODE='"
                                          + portval + "'";
                              // //System.out.println("port code query Value---->" +
                              // hyperValue);
                              con = ConnectionMaster.getConnection();
                              ps1 = con.prepareStatement(hyperValue);
                              rs = ps1.executeQuery();
                              while (rs.next()) {
                                    String hsploy = rs.getString(1);
                                    String poname = rs.getString(2);
                                    // //System.out.println("port code description---->" +
                                    // hsploy);
                                    getPane().setPORTDESC(hsploy);
                                    getPane().setPORTDECO(poname);
                              }

                              // con.close();
                              // ps1.close();
                              // rs.close();
                        } catch (Exception e) {
                              // System.out.println("exception is " + e);
                        } finally {
                              try {
                                    if (rs != null)
                                          rs.close();
                                    if (ps1 != null)
                                          ps1.close();
                                    if (con != null)
                                          con.close();
                              } catch (SQLException e) {
                                    // // System.out.println("Connection Failed! Check
                                    // output
                                    // // console");
                                    e.printStackTrace();
                              }
                        }
                  } else {
                        // //System.out.println("port code is empty");
                  }
                  String custVal = "";
                  if (getMinorCode().equalsIgnoreCase("POC")) {
                        custVal = getDriverWrapper().getEventFieldAsText("PRM", "p",
                                    "no").trim();
                  } else if (getMajorCode().equalsIgnoreCase("IDC")
                              && getMinorCode().equalsIgnoreCase("CLP")) {
                        custVal = getDriverWrapper().getEventFieldAsText("DRE", "p",
                                    "no").trim();
                  }

                  // buyer credit field reference no checking

                  String inwardremNO = getWrapper().getPREBUYRE();
                  // //System.out.println("Buyer credit reference no ---------->" +
                  // inwardremNO);
                  if ((!inwardremNO.equalsIgnoreCase("") && inwardremNO.length() > 0)
                              && (getMinorCode().equalsIgnoreCase("CLP") || getMinorCode()
                                          .equalsIgnoreCase("POC"))) {
                        try {
                              String mercht = getDriverWrapper().getEventFieldAsText(
                                          "cBHW", "l", "").toString();

                              int dmT = 0;

                              String dms = "SELECT DISTINCT DECODE(trim(mas.MASTER_REF) ,NULL ,0,1)AS REMERREF FROM master mas, BASEEVENT bev, EXTEVENT exte, PRODTYPE prod WHERE mas.KEY97 = bev.MASTER_KEY AND bev.KEY97 = exte.EVENT AND mas.PRODTYPE = prod.KEY97 AND mas.MASTER_REF ='"
                                          + inwardremNO
                                          + "' AND mas.PRICUSTMNM ='"
                                          + custVal
                                          + "'";
                              // System.out.println("Master ref no valid for Import lc" +
                              // dms);
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    // System.out.println("Master ref number valid query" +
                                    // dms);

                              }
                              con = ConnectionMaster.getConnection();
                              ps = con.prepareStatement(dms);
                              rs = ps.executeQuery();
                              while (rs.next()) {
                                    dmT = rs.getInt(1);
                                    // System.out.println("AFTER GET THE VALUE " + dmT);

                              }
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    // System.out.println("Master ref no valid count" +
                                    // dmT);

                              }
                              // System.out.println("ILC=====>buyers credit");
                              if ((dmT == 0 || dmT < 1)
                                          && inwardremNO.length() > 0
                                          && mercht.equalsIgnoreCase("Y")
                                          && (step_Input.equalsIgnoreCase("i"))
                                          && (step_Id.equalsIgnoreCase("CBS Maker") || step_Id
                                                      .equalsIgnoreCase("CBS Maker 1"))) {
                                    validationDetails
                                                .addWarning(
                                                            WarningType.Other,
                                                            "Buyer's Credit reference number is not valid number,Kindly check the valid reference number with CRN [CM]");

                              }

                              else {
                                    if (dailyval_Log.equalsIgnoreCase("YES")) {
                                          // System.out.println(
                                          // "Master ref number is valid in else" + dmT +
                                          // "buyer credit check box" + mercht);

                                    }
                              }

                        } catch (Exception e) {
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    // System.out.println("Exception in Master ref no
                                    // validation"
                                    // + e.getMessage());
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
                                    // System.out.println("Connection Failed! Check output
                                    // console");
                                    e.printStackTrace();
                              }
                        }
                  } else {
                        if (dailyval_Log.equalsIgnoreCase("YES")) {
                              // System.out.println("Master ref number for BCR" +
                              // inwardremNO);
                        }

                  }

                  try {
                        // //System.out.println("IE code validation ");
                        List<ExtEventShippingdetailslc> shpcol5 = (List<ExtEventShippingdetailslc>) getWrapper()
                                    .getExtEventShippingdetailslc();

                        if (shpcol5.size() > 0) {
                              int count = 0;
                              for (int l = 0; l < shpcol5.size(); l++) {
                                    ExtEventShippingdetailslc shipCol = shpcol5.get(l);

                                    String recind = shipCol.getRECIN().trim();
                                    String shipbill = shipCol.getLCBILLNO();
                                    // // System.out.println("Record indicator is===>" +
                                    // // recind);

                                    if (recind.equalsIgnoreCase("3")
                                                && (step_Input.equalsIgnoreCase("i"))
                                                && (step_Id.equalsIgnoreCase("CBS Maker"))) {
                                          validationDetails.addError(ErrorType.Other,
                                                      "Shipping bill is already cancelled ("
                                                                  + shipbill + ")  [CM]");
                                    } else {
                                          // // System.out.println("recind is not 3===>" +
                                          // // recind);
                                    }

                              }

                        }

                  } catch (Exception e) {
                        // System.out.println("IE CODE exception " + e.getMessage());
                  }

                  String presendRef = getDriverWrapper().getEventFieldAsText("PRB",
                              "r", "");
                  if (presendRef != null && presendRef.length() > 0) {
                        try {

                              String prod = getDriverWrapper().getEventFieldAsText("PCO",
                                          "s", "");

                              String query7 = "SELECT count(*) FROM BASEEVENT BSE, MASTER MAS where BSE.MASTER_KEY=MAS.KEY97 AND BSE.THEIR_REF='"
                                          + presendRef
                                          + "' and MAS.REFNO_PFIX='"
                                          + prod
                                          + "'";
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    // System.out.println("Presenter Reference number
                                    // query===>"
                                    // + query7);
                              }
                              con = ConnectionMaster.getConnection();
                              ps = con.prepareStatement(query7);
                              rs = ps.executeQuery();
                              int count2 = 0;
                              while (rs.next()) {
                                    count2 = rs.getInt(1);
                                    // //System.out.println("Count value for REFERENCE VALUE
                                    // DUPLICATE---------->" + count2);
                              }
                              if (presendRef != null
                                          && presendRef.length() > 0
                                          && count2 > 1
                                          && (step_Input.equalsIgnoreCase("i"))
                                          && (step_Id.equalsIgnoreCase("CBS Maker") || step_Id
                                                      .equalsIgnoreCase("CBS Maker 1"))
                                          && getMajorCode().equalsIgnoreCase("ILC")
                                          && getMinorCode().equalsIgnoreCase("CRC")) {

                                    validationDetails
                                                .addWarning(WarningType.Other,
                                                            "Presenter's Reference number already exist [CM]");

                              } else {
                                    // //System.out.println("Reference number is new");

                              }

                        }

                        catch (Exception e) {
                              if (dailyval_Log.equalsIgnoreCase("YES")) {
                                    System.out
                                                .println("Exception Presenter Reference number===>"
                                                            + e.getMessage());
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
                                    // // System.out.println("Connection Failed! Check
                                    // output
                                    // // console");
                                    e.printStackTrace();
                              }
                        }
                  }

                  // BOE validation

                  // String systemDate = getDriverWrapper().getEventFieldAsText("TDY",
                  // "d", "");
                  // //System.out.println("systemDate " + systemDate);
                  List<ExtEventBOECAP> BOECAP = (List<ExtEventBOECAP>) getWrapper()
                              .getExtEventBOECAP();
                  for (int j = 0; j < BOECAP.size(); j++) {
                        ExtEventBOECAP boeval = BOECAP.get(j);
                        String billdate = String.valueOf(boeval.getBOEDAT());
                        // //System.out.println("BOE Date " + billdate);
                        try {
                              // //System.out.println("1");
                              SimpleDateFormat parseFormat = new SimpleDateFormat(
                                          "EEE MMM dd HH:mm:ss z yyyy");
                              Date date1 = parseFormat.parse(billdate);// grid enter SHIP
                                                                                                // BILL
                                                                                                // date
                              SimpleDateFormat formatter1 = new SimpleDateFormat(
                                          "dd/MM/yy");
                              String result1 = formatter1.format(date1);
                              Date shipbillDate = (Date) formatter1.parse(result1);
                              Date sysDate = formatter1.parse(systemDat);
                              if (shipbillDate.after(sysDate)
                                          && (step_Input.equalsIgnoreCase("i"))
                                          && (step_Id.equalsIgnoreCase("CBS Maker"))) {
                                    // //System.out.println("date1 is before Date2");
                                    validationDetails
                                                .addError(ErrorType.Other,
                                                            "BOE date is future date than the today date [CM]");
                              } else {
                                    // //System.out.println("----date3 is after Date2---");
                              }
                        } catch (Exception e) {
                              // System.out.println(e.getMessage());
                        }
                  }

                  String grwai = getDriverWrapper().getEventFieldAsText("cAOJ", "l",
                              "").trim();
                  // // System.out.println("GR WAIVER grwaiver---" + grwai);

                  String formtyp = getWrapper().getFORTYP();
                  String sezCust = getDriverWrapper().getEventFieldAsText("PRM", "p",
                              "cBBI").trim();
                  // System.out.println("Form type for SEZ" + formtyp + " SEZ
                  // customer" + sezCust);
                  if (sezCust.equalsIgnoreCase("SEZ")
                              && getMinorCode().equalsIgnoreCase("FNF")
                              && prd_typ.equalsIgnoreCase("ELF")) {
                        if (!formtyp.equalsIgnoreCase("EXEMPTED")
                                    && (step_Input.equalsIgnoreCase("i"))
                                    && (step_Id.equalsIgnoreCase("CBS Maker"))) {
                              validationDetails
                                          .addError(ErrorType.Other,
                                                      "Please select Form type as EXEMPTED as Exporter category is SEZ [CM]");
                        } else {

                        }

                  }

                  List<ExtEventShippingTable> shipgrid = (List<ExtEventShippingTable>) getWrapper()
                              .getExtEventShippingTable();

                  if ((prd_typ.equalsIgnoreCase("ELF"))
                              && formtyp.equalsIgnoreCase("EXEMPTED")) {
                        if (shipgrid.size() > 0 && (step_Input.equalsIgnoreCase("i"))
                                    && (step_Id.equalsIgnoreCase("CBS Maker"))
                                    && getMinorCode().equalsIgnoreCase("FNF")) {

                              validationDetails
                                          .addError(
                                                      ErrorType.Other,
                                                      "Form type is selected as 'EXEMPTED' Kindly delete the shipping bill details [CM]");
                        }
                  }

                  // //System.out.println("GR WAIVER grwai ---" + grwai);
                  // //System.out.println("product type is ---" + prd_typ);
                  String mixedpay = getDriverWrapper().getEventFieldAsText("MIX",
                              "l", "");
                  double mixpay_tot = 0;
                  double am = 0;
                  double am_rey = 0;
                  // long repay_total = 0;
                  double short_total = 0;
                  double amountdob = 0;
                  double repay_Amt = 0.0;
                  if ((!grwai.equalsIgnoreCase("Y")) && grwai.equalsIgnoreCase("N")) {
                        if (prd_typ.equalsIgnoreCase("ELF")
                                    && (step_Input.equalsIgnoreCase("i"))
                                    && (step_Id.equalsIgnoreCase("CBS Maker") || step_Id
                                                .equalsIgnoreCase("CBS Maker 1"))
                                    && grwai.equalsIgnoreCase("N")
                                    && (!formtyp.equalsIgnoreCase("EXEMPTED"))) {

                              try {

                                    // //System.out.println("shipment validation start");
                                    // validatiommjkvdv>>>>>>>>>>>>>??????????????????? ");
                                    List<ExtEventShippingTable> shiplcd = (List<ExtEventShippingTable>) getWrapper()
                                                .getExtEventShippingTable();

                                    if (shiplcd.size() == 0
                                                && (step_Input.equalsIgnoreCase("i"))
                                                && (step_Id.equalsIgnoreCase("CBS Maker"))
                                                && getMinorCode().equalsIgnoreCase("FNF")) {

                                          // //System.out.println("size of grid inside if loop
                                          // "
                                          // + shiplcd.size());
                                          validationDetails.addError(ErrorType.Other,
                                                      "Shipping Bill details is mandatory [CM]");
                                    } else {

                                          List<ExtEventShippingdetailslc> shiplc = (List<ExtEventShippingdetailslc>) getWrapper()
                                                      .getExtEventShippingdetailslc();
                                          List<ExtEventInvoicedetailsLC> invlc = (List<ExtEventInvoicedetailsLC>) getWrapper()
                                                      .getExtEventInvoicedetailsLC();

                                          if ((formtyp.trim().equalsIgnoreCase("EDI") || formtyp
                                                      .trim().equalsIgnoreCase("SOFTEX"))
                                                      && shiplc.size() == 0
                                                      && (step_Input.equalsIgnoreCase("i"))
                                                      && (step_Id.equalsIgnoreCase("CBS Maker"))
                                                      && getMinorCode().equalsIgnoreCase("FNF")) {

                                                // //System.out.println("Size of grid
                                                // shipping
                                                // details if loop " + shiplc.size());
                                                validationDetails
                                                            .addError(ErrorType.Other,
                                                                        "Please Fetch the Shipping details button [CM]");
                                          } else if (formtyp.trim().equalsIgnoreCase("EDF")
                                                      && shiplc.size() == 0
                                                      && (step_Input.equalsIgnoreCase("i"))
                                                      && (step_Id.equalsIgnoreCase("CBS Maker"))
                                                      && getMinorCode().equalsIgnoreCase("FNF")) {

                                                // //System.out.println("Size of grid
                                                // shipping
                                                // details if loop " + shiplc.size());
                                                validationDetails
                                                            .addError(ErrorType.Other,
                                                                        "Kindly input shipping bill details manually as Form Type is 'EDF' [CM]");
                                          } else {
                                                System.out.println("Form type is" + formtyp);
                                          }

                                          if ((formtyp.trim().equalsIgnoreCase("EDI") || formtyp
                                                      .trim().equalsIgnoreCase("SOFTEX"))
                                                      && invlc.size() == 0
                                                      && (step_Input.equalsIgnoreCase("i"))
                                                      && (step_Id.equalsIgnoreCase("CBS Maker"))
                                                      && getMinorCode().equalsIgnoreCase("FNF")) {

                                                // //System.out.println("Size of grid
                                                // shipping
                                                // details if loop " + invlc.size());
                                                validationDetails
                                                            .addError(ErrorType.Other,
                                                                        "Please Fetch the Invoice details button [CM]");
                                          } else if (formtyp.trim().equalsIgnoreCase("EDF")
                                                      && invlc.size() == 0
                                                      && (step_Input.equalsIgnoreCase("i"))
                                                      && (step_Id.equalsIgnoreCase("CBS Maker"))
                                                      && getMinorCode().equalsIgnoreCase("FNF")) {

                                                // //System.out.println("Size of grid
                                                // shipping
                                                // details if loop " + invlc.size());
                                                validationDetails
                                                            .addError(ErrorType.Other,
                                                                        "Kindly input Invoice bill details manually as Form Type is 'EDF' [CM]");
                                          } else {
                                                // System.out.println("Form type is" + formtyp);
                                          }

                                          String billrow = shiplcd.get(0).getBILLNUM();
                                          // //System.out.println("Shipping Number billrow
                                          // softax--->" + billrow);
                                          String formNo = shiplcd.get(0).getFORMNUM();
                                          // System.out.println("Form Number softax--->" +
                                          // formNo);

                                          if (formtyp.trim().equalsIgnoreCase("EDI")
                                                      && (step_Input.equalsIgnoreCase("i"))
                                                      && (step_Id.equalsIgnoreCase("CBS Maker"))
                                                      && getMinorCode().equalsIgnoreCase("FNF")) {
                                                if ((billrow.trim().equalsIgnoreCase(""))
                                                            || billrow.trim().isEmpty()
                                                            && getMinorCode().equalsIgnoreCase(
                                                                        "FNF")) {
                                                      // //System.out.println("Shipping Number
                                                      // blank
                                                      // softax--->");
                                                      validationDetails
                                                                  .addError(ErrorType.Other,
                                                                              "Form Type: EDI,Shipping Bill No is Mandatory in shipping details pane :[CM]");
                                                }
                                          }

                                          if (formtyp.trim().equalsIgnoreCase("SOFTEX")
                                                      && (step_Input.equalsIgnoreCase("i"))
                                                      && (step_Id.equalsIgnoreCase("CBS Maker"))
                                                      && getMinorCode().equalsIgnoreCase("FNF")) {
                                                if ((formNo.trim().equalsIgnoreCase(""))
                                                            || formNo.trim().isEmpty()
                                                            && getMinorCode().equalsIgnoreCase(
                                                                        "FNF")) {
                                                      validationDetails
                                                                  .addError(ErrorType.Other,
                                                                              "Form Type:SOFTEX,Form No is Mandatory in shipping details pane :[CM]");
                                                }
                                          }

                                          if (shiplcd.size() > 0) {
                                                // // System.out.println("shipment validation in
                                                // // else part");
                                                // //System.out.println("size shipping grid " +
                                                // shiplcd.size());
                                                String AmountS = getDriverWrapper()
                                                            .getEventFieldAsText("AMT", "v", "m")
                                                            .trim();
                                                String cur1 = getDriverWrapper()
                                                            .getEventFieldAsText("AMT", "v", "c")
                                                            .trim();

                                                double amountint = Double.parseDouble(AmountS);
                                                int counter = 0;
                                                double rep_total = 0.0;
                                                double invadob_total = 0.0;
                                                double ship_total = 0.0;
                                                double equal_amt = 0.0;
                                                String ship_Str = "0";

                                                double equal_tot = 0.0;
                                                double equal_total = 0.0;
                                                String repayAmount = "";
                                                String repval = "0";
                                                String acm = "N";
                                                String acp = "N";
                                                String billno = "";

                                                ExtEventShippingTable shipinglc = null;
                                                double notioanl = 1;
                                                for (int l = 0; l < shiplcd.size(); l++) {
                                                      // //System.out.println("shipment validation
                                                      // in
                                                      // for
                                                      // loop");

                                                      shipinglc = shiplcd.get(l);
                                                      if (getMajorCode().equalsIgnoreCase("FRN")
                                                                  && getMinorCode().equalsIgnoreCase(
                                                                              "FNF")) {
                                                            BigDecimal shipAmt = new BigDecimal("0");
                                                            BigDecimal notVal = new BigDecimal("1");
                                                            shipinglc.setREPAYAM(shipAmt);
                                                            shipinglc.setREPAYAMCurrency(shipinglc
                                                                        .getSHPAMTCurrency());
                                                            shipinglc.setSHCOLAM(shipAmt);
                                                            shipinglc.setSHCOLAMCurrency(shipinglc
                                                                        .getSHPAMTCurrency());
                                                            shipinglc.setNOTIONAL(notVal);
                                                      }

                                                      try {
                                                            String billrow1 = shipinglc
                                                                        .getBILLNUM();
                                                            String formNo1 = shipinglc.getFORMNUM();
                                                            if (formtyp.trim().equalsIgnoreCase(
                                                                        "EDF")
                                                                        && (step_Input
                                                                                    .equalsIgnoreCase("i"))
                                                                        && (step_Id
                                                                                    .equalsIgnoreCase("CBS Maker"))
                                                                        && getMinorCode()
                                                                                    .equalsIgnoreCase("FNF")) {
                                                                  if ((billrow1.equalsIgnoreCase("") || billrow1 == null)
                                                                              && (formNo1
                                                                                          .equalsIgnoreCase("") || formNo1 == null)
                                                                              && getMinorCode()
                                                                                          .equalsIgnoreCase(
                                                                                                      "FNF")) {
                                                                        validationDetails
                                                                                    .addError(
                                                                                                ErrorType.Other,
                                                                                                "Form Type:EDF,Form No or Shipping Bill No is Mandatory in shipping details pane :[CM]");
                                                                  } else {
                                                                        if (dailyval_Log
                                                                                    .equalsIgnoreCase("YES")) {
                                                                              // System.out.println("Shiping
                                                                              // bill number--->"
                                                                              // + billrow1
                                                                              // + "Form number" +
                                                                              // formNo1);
                                                                        }
                                                                  }
                                                            }
                                                      } catch (Exception e) {
                                                            if (dailyval_Log
                                                                        .equalsIgnoreCase("YES")) {
                                                                  System.out
                                                                              .println("Exception Shiping bill number and form number--->"
                                                                                          + e.getMessage());
                                                            }
                                                      }

                                                      ship_Str = shipinglc.getSHPAMT().toString();
                                                      if (ship_Str == null
                                                                  || ship_Str.equalsIgnoreCase("")) {

                                                            ship_Str = "0";
                                                            // //System.out.println("tendays in if
                                                            // --->" +
                                                            // tendays);
                                                      }
                                                      // //System.out.println("Shiping amount--->"
                                                      // +
                                                      // ship_Str);
                                                      double ship_dob = 0.0;
                                                      try {

                                                            ship_dob = Double.valueOf(ship_Str);
                                                      } catch (Exception e) {
                                                            // System.out.println("tendays in catch"
                                                            // + e.getMessage());
                                                      }

                                                      ship_total = shipinglc.getSHPAMT()
                                                                  .doubleValue();

                                                      if (getMinorCode().equalsIgnoreCase("POF")
                                                                  && getMajorCode().equalsIgnoreCase(
                                                                              "FRN")
                                                                  && (step_Input
                                                                              .equalsIgnoreCase("i"))
                                                                  && (step_Id
                                                                              .equalsIgnoreCase("CBS Maker"))) {

                                                            acm = getDriverWrapper()
                                                                        .getEventFieldAsText("ACM",
                                                                                    "l", "");
                                                            acp = getDriverWrapper()
                                                                        .getEventFieldAsText("ACP",
                                                                                    "l", "");
                                                            // //System.out.println("Payment action
                                                            // ---->"
                                                            // + acm + "<<<>>>" + acp);
                                                            // repayAmount =
                                                            // getDriverWrapper().getEventFieldAsText("AAF",
                                                            // "*", "");
                                                            // repayAmount =
                                                            // getDriverWrapper().getEventFieldAsText("AAF",
                                                            // "*", "");
                                                            // // //System.out.println("repayment in
                                                            // // initialy---->" + repayAmount);
                                                            // BigDecimal rAmt =
                                                            // shipinglc.getREPAYAM();
                                                            // if (rAmt != null) {
                                                            // repay_Amt = rAmt.doubleValue();
                                                            // // repayAmount =
                                                            // // String.valueOf(repay_Amt);
                                                            // //// System.out.println("repayment
                                                            // // in bigdeciaml---->" + repay_Amt);
                                                            // } else {
                                                            //
                                                            // repayAmount = "";
                                                            // }

                                                            rep_total = shipinglc.getREPAYAM()
                                                                        .doubleValue();
                                                            // System.out.println("shipmentrep_total---->"
                                                            // + rep_total);
                                                            String curr = shipinglc
                                                                        .getREPAYAMCurrency().trim();
                                                            // System.out.println("Repaycurrency" +
                                                            // curr);
                                                            ConnectionMaster connectionMaster = new ConnectionMaster();
                                                            double divideBy = connectionMaster
                                                                        .getDecimalforCurrency(curr);
                                                            repay_Amt = rep_total / divideBy;
                                                            // System.out.println("Repayamount" +
                                                            // repay_Amt);
                                                            am_rey = am_rey + repay_Amt;
                                                            // System.out.println("Repayamount_am_rey"
                                                            // + am_rey);

                                                            repval = shipinglc.getREPTYPE();
                                                            // //System.out.println("Repayment type
                                                            // if---->>>" + repval);
                                                            if (repval.equalsIgnoreCase("")
                                                                        && ((!repval
                                                                                    .equalsIgnoreCase("1")) || !repval
                                                                                    .equalsIgnoreCase("2"))
                                                                        && getMinorCode()
                                                                                    .equalsIgnoreCase("POF")
                                                                        && (step_Input
                                                                                    .equalsIgnoreCase("i"))
                                                                        && (step_Id
                                                                                    .equalsIgnoreCase("CBS Maker") || step_Id
                                                                                    .equalsIgnoreCase("CBS Maker 1"))) {
                                                                  validationDetails
                                                                              .addWarning(
                                                                                          WarningType.Other,
                                                                                          "Repayment type is mandatory in shipping grid [CM]");
                                                            } else {
                                                                  // // System.out.println("Repayment
                                                                  // // type in else---->>>" +
                                                                  // repval);
                                                            }

                                                            if ((rep_total == 0.0 || rep_total < 1)
                                                                        && ((repval
                                                                                    .equalsIgnoreCase("1")) || repval
                                                                                    .equalsIgnoreCase("2"))
                                                                        && (step_Input
                                                                                    .equalsIgnoreCase("i"))
                                                                        && (step_Id
                                                                                    .equalsIgnoreCase("CBS Maker") || step_Id
                                                                                    .equalsIgnoreCase("CBS Maker 1"))
                                                                        && payaction
                                                                                    .equalsIgnoreCase("Pay")) {
                                                                  validationDetails
                                                                              .addWarning(
                                                                                          WarningType.Other,
                                                                                          "Please enter the Repayment amount in shipping Bill grid [CM]");
                                                            } else {
                                                                  // System.out.println("Repayment
                                                                  // amount--->>>" + rep_total +
                                                                  // "Repayment type" + repval);
                                                            }

                                                      }
                                                      String shipamtcurr = shipinglc
                                                                  .getSHPAMTCurrency().trim();
                                                      /*
                                                       * String advancerec =
                                                       * getWrapper().getADVREC(); if(advancerec)
                                                       */
                                                      ConnectionMaster connectionMaster = new ConnectionMaster();
                                                      // System.out.println("shipamtcurr"+shipamtcurr);
                                                      double divideByDecimal = connectionMaster
                                                                  .getDecimalforCurrency(shipamtcurr);
                                                      // System.out.println("ship_total"+ship_total);
                                                      invadob_total = ship_total
                                                                  / divideByDecimal;
                                                      // System.out.println("invadob_total"+invadob_total);
                                                      am = am + invadob_total;
                                                      // System.out.println("am"+am);

                                                      equal_amt = shipinglc.getEQUBILL()
                                                                  .doubleValue();
                                                      String equal_cur = shipinglc
                                                                  .getEQUBILLCurrency();
                                                      // //System.out.println("Shipping
                                                      // equaivalent
                                                      // amount " + equal_amt);
                                                      double deci = connectionMaster
                                                                  .getDecimalforCurrency(equal_cur);
                                                      equal_tot = equal_amt / deci;
                                                      equal_total = equal_total + equal_tot;
                                                      // //System.out.println("Shipping
                                                      // equaivalent
                                                      // amount after convert" + equal_total);
                                                      billno = shipinglc.getBILLNUM();

                                                }
                                                double claimAmountDub = 0;
                                                double addAmountDub = 0;
                                                try {
                                                      String addAmount = getDriverWrapper()
                                                                  .getEventFieldAsText("AAC", "v",
                                                                              "m");
                                                      try {
                                                            addAmountDub = Double
                                                                        .valueOf(addAmount);

                                                      } catch (Exception e) {
                                                            addAmountDub = 0;
                                                            // System.out.println("1st Exception in
                                                            // addAmount"
                                                            // + e.getMessage());
                                                      }
                                                      String claimAmount = getDriverWrapper()
                                                                  .getEventFieldAsText("AMC", "v",
                                                                              "m");
                                                      // // System.out.println("Shipping
                                                      // // equaivalent
                                                      // // amount for compare" + equal_total +
                                                      // // "claimAmountDub" + claimAmountDub);
                                                      if (claimAmount.length() > 0) {
                                                            claimAmountDub = Double
                                                                        .parseDouble(claimAmount);
                                                      }
                                                      // // System.out.println("Shipping
                                                      // // equaivalent
                                                      // // amount for compare" + equal_total +
                                                      // // "claimAmountDub" + claimAmountDub);

                                                      if (equal_total != claimAmountDub
                                                                  && addAmountDub > 0
                                                                  && getMinorCode().equalsIgnoreCase(
                                                                              "FNF")
                                                                  && (step_Input
                                                                              .equalsIgnoreCase("i"))
                                                                  && (step_Id
                                                                              .equalsIgnoreCase("CBS Maker") || step_Id
                                                                              .equalsIgnoreCase("CBS Maker 1"))) {
                                                            validationDetails
                                                                        .addWarning(
                                                                                    WarningType.Other,
                                                                                    "Sum of shipping bill amount should be equal to Bill lodgement amount (Presentation amt+ additional amt) [CM]");
                                                      } else if (equal_total != amountint
                                                                  && (step_Input
                                                                              .equalsIgnoreCase("i"))
                                                                  && (step_Id
                                                                              .equalsIgnoreCase("CBS Maker") || step_Id
                                                                              .equalsIgnoreCase("CBS Maker 1"))
                                                                  && getMinorCode().equalsIgnoreCase(
                                                                              "FNF")) {

                                                            validationDetails
                                                                        .addWarning(WarningType.Other,
                                                                                    "Sum of shipping bill amount should be equal to Bill lodgement amount [CM]");

                                                      } else {
                                                            // // System.out.println("Sum of
                                                            // // equivalent
                                                            // // bill amounts is equal to Bill
                                                            // // amount");
                                                      }
                                                } catch (Exception e) {
                                                      // System.out.println("Exception in
                                                      // addAmount"
                                                      // + e.getMessage());
                                                }

                                                if (counter > 0
                                                            && (step_Input.equalsIgnoreCase("i"))
                                                            && (step_Id
                                                                        .equalsIgnoreCase("CBS Maker"))) {

                                                      notioanl = shipinglc.getNOTIONAL()
                                                                  .doubleValue();
                                                      // //System.out.println("Notional rate is
                                                      // ----> " + notioanl);
                                                      if (notioanl < 2
                                                                  && (step_Input
                                                                              .equalsIgnoreCase("i"))
                                                                  && (step_Id
                                                                              .equalsIgnoreCase("CBS Maker") || step_Id
                                                                              .equalsIgnoreCase("CBS Maker 1"))
                                                                  && getMinorCode().equalsIgnoreCase(
                                                                              "FNF")) {
                                                            // //System.out.println("Notional rate
                                                            // is
                                                            // empty " + notioanl);
                                                            validationDetails
                                                                        .addWarning(
                                                                                    WarningType.Other,
                                                                                    "shipping bill currency is different from collection currency, Notional rate is mandatory [CM]");
                                                      } else {

                                                            // // System.out.println("Notional rate
                                                            // // is greater than 0 values----> ");
                                                      }
                                                }

                                                // Start to edit for repay

                                                List<ExtEventShippingTable> rep = (List<ExtEventShippingTable>) getWrapper()
                                                            .getExtEventShippingTable();
                                                String repval_new = "0";
                                                double short_totals = 0;
                                                double short_totvalue = 0;
                                                double short_convert = 0;
                                                try {

                                                      for (int j = 0; j < rep.size(); j++) {
                                                            ExtEventShippingTable type = rep.get(j);
                                                            double rep_totalss = type.getREPAYAM()
                                                                        .doubleValue();
                                                            double short_tot = type.getSHCOLAM()
                                                                        .doubleValue();
                                                            double equl_tot = type.getEQUBILL()
                                                                        .doubleValue();
                                                            String equl_ccy = type
                                                                        .getEQUBILLCurrency();
                                                            short_totals = short_tot + equl_tot;
                                                            ConnectionMaster connectionMaster = new ConnectionMaster();
                                                            double divideByDecimal = connectionMaster
                                                                        .getDecimalforCurrency(equl_ccy);
                                                            short_convert = short_totals
                                                                        / divideByDecimal;
                                                            short_totvalue = short_totvalue
                                                                        + short_convert;

                                                            double covertShot = short_tot
                                                                        / divideByDecimal;
                                                            short_total = short_total + covertShot;

                                                            repval_new = type.getREPTYPE();
                                                            // //System.out.println("shipping
                                                            // repayment
                                                            // amount" + repval);
                                                            String sbil = type.getBILLNUM();
                                                            double ship_total_ss = type
                                                                        .getLOUTSAMT().doubleValue();
                                                            // //System.out.println("Shipping bill
                                                            // amount_____" + rep_totalss);
                                                            double shpamt = type.getSHPAMT()
                                                                        .doubleValue();

                                                            BigDecimal shotAmt = type.getSHCOLAM();
                                                            // System.out.println("Short collection
                                                            // amount bigdecimal---->>>" + shotAmt);
                                                            BigDecimal outAmt = type.getLOUTSAMT();
                                                            // System.out.println("Short collection
                                                            // amount bigdecimal---->>>" + outAmt);

                                                            if (shotAmt.compareTo(outAmt) > 0
                                                                        && (step_Input
                                                                                    .equalsIgnoreCase("i"))
                                                                        && (step_Id
                                                                                    .equalsIgnoreCase("CBS Maker"))
                                                                        && getMinorCode()
                                                                                    .equalsIgnoreCase("POF")) {
                                                                  validationDetails
                                                                              .addError(
                                                                                          ErrorType.Other,
                                                                                          "Short collection amount should not be greater than SB outstanding amount in shipping grid [CM]");

                                                            } else {
                                                                  // System.out.println("Short
                                                                  // collection
                                                                  // amount bigdecimal else---->>>" +
                                                                  // shotAmt);
                                                            }

                                                            if ((repval_new.equalsIgnoreCase("2"))
                                                                        && (step_Input
                                                                                    .equalsIgnoreCase("i"))
                                                                        && (step_Id
                                                                                    .equalsIgnoreCase("CBS Maker"))
                                                                        && getMinorCode()
                                                                                    .equalsIgnoreCase("POF")) {

                                                                  if ((ship_total_ss == 0.0 && rep_totalss != 0.0)
                                                                              && (step_Input
                                                                                          .equalsIgnoreCase("i"))
                                                                              && (step_Id
                                                                                          .equalsIgnoreCase("CBS Maker") || step_Id
                                                                                          .equalsIgnoreCase("CBS Maker 1"))
                                                                              && getMinorCode()
                                                                                          .equalsIgnoreCase(
                                                                                                      "POF")) {
                                                                        validationDetails
                                                                                    .addWarning(
                                                                                                WarningType.Other,
                                                                                                "Repayment Type is Full payment, please do not modify already paid shipping bill ("
                                                                                                            + sbil
                                                                                                            + ") [CM]");
                                                                  } else {
                                                                        // //
                                                                        // System.out.println("Repayment
                                                                        // // amount is part
                                                                        // // payment---->>>"
                                                                        // // + repval);
                                                                  }

                                                            }

                                                            else {
                                                                  // // System.out.println("rep
                                                                  // // amount--->>>" + rep_total);
                                                            }
                                                            // // System.out.println("Outstanding
                                                            // and
                                                            // // short+equvalent amount---->>>" +
                                                            // // ship_total_ss+ "" + short_totals);
                                                            if ((ship_total_ss == short_totals)
                                                                        && (repval_new
                                                                                    .equalsIgnoreCase("1") || repval_new
                                                                                    .equalsIgnoreCase(""))
                                                                        && (step_Input
                                                                                    .equalsIgnoreCase("i"))
                                                                        && (step_Id
                                                                                    .equalsIgnoreCase("CBS Maker") || step_Id
                                                                                    .equalsIgnoreCase("CBS Maker 1"))
                                                                        && getMinorCode()
                                                                                    .equalsIgnoreCase("POF")) {
                                                                  if (payaction
                                                                              .equalsIgnoreCase("Pay")) {
                                                                        validationDetails
                                                                                    .addWarning(
                                                                                                WarningType.Other,
                                                                                                "Sum of SB Equivalent Bill Amount and short collection amount is equal to SB outstanding amt, then select repayment type as 'Full'  ("
                                                                                                            + sbil
                                                                                                            + ") [CM]");
                                                                  }

                                                            } else {
                                                                  // // System.out.println("Repay type
                                                                  // // is
                                                                  // // full" + repval_new);
                                                            }

                                                            if ((ship_total_ss > short_totals)
                                                                        && repval_new
                                                                                    .equalsIgnoreCase("2")
                                                                        && (step_Input
                                                                                    .equalsIgnoreCase("i"))
                                                                        && (step_Id
                                                                                    .equalsIgnoreCase("CBS Maker") || step_Id
                                                                                    .equalsIgnoreCase("CBS Maker 1"))
                                                                        && getMinorCode()
                                                                                    .equalsIgnoreCase("POF")) {

                                                                  if (payaction
                                                                              .equalsIgnoreCase("Pay")) {
                                                                        validationDetails
                                                                                    .addWarning(
                                                                                                WarningType.Other,
                                                                                                "Sum of SB Equivalent Bill Amount and short collection amount is less than SB outstanding amount, then select repayment type as 'Part' ("
                                                                                                            + sbil
                                                                                                            + ") [CM]");
                                                                  }

                                                            } else {
                                                                  // // System.out.println("Repay type
                                                                  // // is
                                                                  // // part" + repval_new);
                                                            }

                                                            if (((ship_total_ss == 0.0) && repval
                                                                        .equalsIgnoreCase("1"))
                                                                        && (step_Input
                                                                                    .equalsIgnoreCase("i"))
                                                                        && (step_Id
                                                                                    .equalsIgnoreCase("CBS Maker") || step_Id
                                                                                    .equalsIgnoreCase("CBS Maker 1"))
                                                                        && getMinorCode()
                                                                                    .equalsIgnoreCase("FNF")) {

                                                                  // //System.out.println("string
                                                                  // repayment
                                                                  // type_____" + repval);
                                                                  validationDetails
                                                                              .addWarning(
                                                                                          WarningType.Other,
                                                                                          "Shipping bill ("
                                                                                                      + sbil
                                                                                                      + ") is already paid, Please change repayment type as Full [CM]");
                                                                  //

                                                            }

                                                            else {
                                                                  // //
                                                                  // System.out.println("ship_total_ss
                                                                  // // amount----->>>" +
                                                                  // // ship_total_ss);
                                                            }

                                                            if ((ship_total_ss < short_totals)
                                                                        && (step_Input
                                                                                    .equalsIgnoreCase("i"))
                                                                        && (step_Id
                                                                                    .equalsIgnoreCase("CBS Maker") || step_Id
                                                                                    .equalsIgnoreCase("CBS Maker 1"))
                                                                        && getMinorCode()
                                                                                    .equalsIgnoreCase("POF")) {

                                                                  // //System.out.println("string
                                                                  // repayment
                                                                  // amount ---->" + rep_totalss);
                                                                  if (payaction
                                                                              .equalsIgnoreCase("Pay")) {
                                                                        validationDetails
                                                                                    .addWarning(
                                                                                                WarningType.Other,
                                                                                                "Sum of SB Equivalent Bill Amount and short collection amount should not greater than SB Outstanding amount ("
                                                                                                            + sbil
                                                                                                            + ") [CM]");
                                                                  }

                                                            }

                                                      }

                                                }

                                                catch (Exception e) {
                                                      // System.out.println("Repayment modify
                                                      // exception"
                                                      // + e.getMessage());
                                                }
                                                String amountcol = getDriverWrapper()
                                                            .getEventFieldAsText("AMP", "v", "m")
                                                            .trim();

                                                if (amountcol.length() > 0) {
                                                      amountdob = Double.parseDouble(amountcol);
                                                }

                                                double addAmountdoub = 0;
                                                double addtionalAmt = 0;
                                                try {
                                                      String addtionalAmount = getDriverWrapper()
                                                                  .getEventFieldAsText("CLAD", "v",
                                                                              "m").trim();

                                                      try {
                                                            addtionalAmt = Double
                                                                        .parseDouble(addtionalAmount);

                                                      } catch (Exception e) {
                                                            addtionalAmt = 0;
                                                            // System.out.println("1st Exception in
                                                            // addAmount"
                                                            // + e.getMessage());
                                                      }

                                                      String addAmountPay = getDriverWrapper()
                                                                  .getEventFieldAsText("AMC", "v",
                                                                              "m").trim();
                                                      if (addAmountPay.length() > 0) {
                                                            addAmountdoub = Double
                                                                        .parseDouble(addAmountPay);
                                                      }
                                                } catch (Exception e) {
                                                      // System.out.println("Exception in
                                                      // addtional amount"
                                                      // + e.getMessage());
                                                }

                                                // System.out.println("Mixed payment
                                                // checkbox--->>>" + mixedpay + "" + amountcol);
                                                // System.out.println("Amount to be collect
                                                // compare--->>>"
                                                // + amountdob
                                                // + "short_totvalueequl bill amount" +
                                                // short_totvalue);
                                                if (amountcol.length() > 0
                                                            && mixedpay.equalsIgnoreCase("N")
                                                            && (payaction.equalsIgnoreCase("Pay"))) {

                                                      if ((short_totvalue != addAmountdoub)
                                                                  && addtionalAmt > 0
                                                                  && (step_Input
                                                                              .equalsIgnoreCase("i"))
                                                                  && (step_Id
                                                                              .equalsIgnoreCase("CBS Maker") || step_Id
                                                                              .equalsIgnoreCase("CBS Maker 1"))
                                                                  && getMinorCode().equalsIgnoreCase(
                                                                              "POF")) {
                                                            validationDetails
                                                                        .addWarning(
                                                                                    WarningType.Other,
                                                                                    "Sum of SB Equivalent Bill Amount and short collection amount should be equal to payment amount (Payment amt+ additional amt) [CM]");
                                                      } else if ((short_totvalue != amountdob)
                                                                  && (step_Input
                                                                              .equalsIgnoreCase("i"))
                                                                  && (step_Id
                                                                              .equalsIgnoreCase("CBS Maker") || step_Id
                                                                              .equalsIgnoreCase("CBS Maker 1"))
                                                                  && getMinorCode().equalsIgnoreCase(
                                                                              "POF")) {
                                                            validationDetails
                                                                        .addWarning(
                                                                                    WarningType.Other,
                                                                                    "Sum of SB Equivalent Bill Amount and short collection amount should be equal to payment amount (Payment details)  [CM]");
                                                      }

                                                      else {
                                                            /*
                                                             * System.out.println(
                                                             * "Sum of Equivalent Bill Amount else loop---->>>"
                                                             * + short_totvalue + " Amount in elc" +
                                                             * amountdob + "addAmountdoub" +
                                                             * addAmountdoub);
                                                             */
                                                      }
                                                }
                                                // Mixed payment validation
                                                else if (mixedpay.equalsIgnoreCase("Y")
                                                            && (payaction.equalsIgnoreCase("Pay"))) {
                                                      String mixpay = "";
                                                      String mixcur = "";

                                                      con = getConnection();
                                                      String dms = "SELECT mas.MASTER_REF, bev.REFNO_PFIX, bev.REFNO_SERL, sum(pap.PAY_AMT), pap.PAY_AMTCCY FROM master mas, BASEEVENT bev, LCPAYMENT lcp, PARTPAYMNT pap WHERE mas.KEY97 = bev.MASTER_KEY AND bev.KEY97 = lcp.KEY97 AND lcp.KEY97 = pap.PAYEV_KEY AND pap.PP_STAT='P' AND pap.PAID_REJ   <>'P' AND mas.MASTER_REF = '"
                                                                  + MasterReference
                                                                  + "' AND bev.REFNO_PFIX = '"
                                                                  + evnt
                                                                  + "' AND bev.REFNO_SERL ="
                                                                  + evvcount
                                                                  + " group by mas.MASTER_REF, bev.REFNO_PFIX, bev.REFNO_SERL, pap.PAY_AMTCCY"; //
                                                      dmsp = con.prepareStatement(dms);
                                                      // System.out.println("Mixed payment
                                                      // validation query--->"
                                                      // + dms);
                                                      dmsr = dmsp.executeQuery();
                                                      while (dmsr.next()) {
                                                            mixpay = dmsr.getString(4);
                                                            mixcur = dmsr.getString(5).trim();

                                                      }
                                                      if (mixpay.length() > 0) {
                                                            double mixpay_dob = Double
                                                                        .valueOf(mixpay);
                                                            ConnectionMaster connectionMaster = new ConnectionMaster();
                                                            double dividemixcur = connectionMaster
                                                                        .getDecimalforCurrency(mixcur);
                                                            mixpay_tot = mixpay_dob / dividemixcur;
                                                            // System.out.println("Mixed payment
                                                            // value--->"
                                                            // + mixpay_tot);
                                                            // System.out.println("Final
                                                            // short_totvalue--->"
                                                            // + short_totvalue);
                                                            if ((short_totvalue != mixpay_tot)
                                                                        && (step_Input
                                                                                    .equalsIgnoreCase("i"))
                                                                        && (step_Id
                                                                                    .equalsIgnoreCase("CBS Maker 1"))
                                                                        && getMinorCode()
                                                                                    .equalsIgnoreCase("POF")) {
                                                                  validationDetails
                                                                              .addWarning(
                                                                                          WarningType.Other,
                                                                                          "Sum of SB Equivalent Bill Amount and short collection amount should be equal to payment amount (Payment details) for Mixed payment [CM]");
                                                            } else {
                                                                  // //System.out.println(
                                                                  // "Sum of Equivalent Bill Amount
                                                                  // should be equal to Amount in elc
                                                                  // else loop for Mixed
                                                                  // payment---->>>"
                                                                  // + short_totvalue + " " +
                                                                  // mixpay_tot);
                                                            }

                                                      }

                                                } else {
                                                      // // System.out.println("Sum of Equivalent
                                                      // // Bill Amount and short collection
                                                      // amount
                                                      // // should be equal====>>>");
                                                }

                                                // equal_total

                                                // end to edit for repay
                                          }

                                    }

                              } catch (Exception e) {
                                    System.out.println("Shipment validation Error "
                                                + e.getMessage());
                              }

                              try {

                                    List<ExtEventShippingTable> shiplcd = (List<ExtEventShippingTable>) getWrapper()
                                                .getExtEventShippingTable();

                                    int counterv = 0;
                                    String rep1 = "";
                                    for (int l = 0; l < shiplcd.size(); l++) {

                                          ExtEventShippingTable shipinglc = shiplcd.get(l);

                                          // duplicate bill no identify
                                          String dateresult = "";
                                          String billnoo = shipinglc.getBILLNUM();
                                          String formNO = shipinglc.getFORMNUM();
                                          String port = shipinglc.getPORTCODDD();
                                          String billdate = shipinglc.getBILLDAT().toString();
                                          // // System.out.println("Shipping bill date for
                                          // // checking valid" + billdate);

                                          SimpleDateFormat parseFormat1 = new SimpleDateFormat(
                                                      "EEE MMM dd HH:mm:ss z yyyy");
                                          SimpleDateFormat parseFormat = new SimpleDateFormat(
                                                      "yyyy-MM-dd");
                                          SimpleDateFormat formatter1 = new SimpleDateFormat(
                                                      "dd/MM/yy");

                                          if (billdate.length() > 0 && billdate != null
                                                      || !billdate.equalsIgnoreCase("")) {
                                                if (billdate.length() > 10) {
                                                      dateresult = formatter1.format(parseFormat1
                                                                  .parse(billdate));
                                                      // //System.out.println("Shipping Bill date
                                                      // after result1 if----->" + dateresult);

                                                } else {
                                                      dateresult = formatter1.format(parseFormat
                                                                  .parse(billdate));
                                                      // //System.out.println("Shipping Bill date
                                                      // after result1 else----->" + dateresult);

                                                }
                                          }

                                          int count2 = 0;
                                          String query2 = "select count(*) from ett_edpms_shp where SHIPBILLNO ='"
                                                      + billnoo + "'";
                                          // //System.out.println("Query2---------->" +
                                          // query2);
                                          con = ConnectionMaster.getConnection();
                                          ps = con.prepareStatement(query2);
                                          rs = ps.executeQuery();
                                          while (rs.next()) {
                                                count2 = rs.getInt(1);

                                          }
                                          // Port,bill date,bill no combination validation
                                          int count4 = 0;
                                          String query4 = "SELECT count(*) FROM ett_edpms_shp WHERE shipbillno ='"
                                                      + billnoo
                                                      + "' AND SHIPBILLDATE = TO_CHAR(to_date('"
                                                      + dateresult
                                                      + "','DD-MM-YY'),'ddmmyyyy') AND PORTCODE ='"
                                                      + port + "'";
                                          // //System.out.println("Port,bill date,bill no
                                          // combination validation====>" + query4);
                                          // con = ConnectionMaster.getConnection();
                                          ps1 = con.prepareStatement(query4);
                                          rs1 = ps1.executeQuery();
                                          while (rs1.next()) {
                                                count4 = rs1.getInt(1);

                                          }

                                          int count3 = 0;
                                          String query3 = "select count(*) from ett_edpms_shp_softex where FORMNO ='"
                                                      + formNO + "'";
                                          // con = ConnectionMaster.getConnection();
                                          ps1 = con.prepareStatement(query3);
                                          rs1 = ps1.executeQuery();
                                          while (rs1.next()) {
                                                count3 = rs1.getInt(1);

                                          }

                                          // Port,bill date,form no combination validation
                                          int count5 = 0;
                                          String query5 = "SELECT count(*) FROM ett_edpms_shp_inv_softex A WHERE FORMNO ='"
                                                      + formNO
                                                      + "' AND SHIPBILLDATE = TO_CHAR(to_date('"
                                                      + dateresult
                                                      + "','DD-MM-YY'),'ddmmyyyy') AND PORTCODE ='"
                                                      + port + "'";
                                          // //System.out.println("Port,bill date,form no
                                          // combination validation====>" + count5);
                                          // con = ConnectionMaster.getConnection();
                                          ps1 = con.prepareStatement(query5);
                                          rs1 = ps1.executeQuery();
                                          while (rs1.next()) {
                                                count5 = rs1.getInt(1);

                                          }
                                          String formType = getPane().getFORTYP().trim();
                                          // System.out.println("Form type===>"+formType);

                                          if ((count2 == 0 || count2 < 1)
                                                      && (step_Input.equalsIgnoreCase("i"))
                                                      && (!billnoo.equalsIgnoreCase(""))
                                                      && (step_Id.equalsIgnoreCase("CBS Maker") || step_Id
                                                                  .equalsIgnoreCase("CBS Maker 1"))
                                                      && getMinorCode().equalsIgnoreCase("FNF")
                                                      && !formType.equalsIgnoreCase("EDF")) {

                                                validationDetails
                                                            .addWarning(
                                                                        WarningType.Other,
                                                                        "Shipping bill no ("
                                                                                    + billnoo
                                                                                    + ") not found in MDF file  [CM]");

                                          } else {
                                                // // System.out.println("Count value in else
                                                // // dup---------->" + count2);
                                          }

                                          if ((count3 < 1 || count3 == 0)
                                                      && (!formNO.equalsIgnoreCase("") && billnoo
                                                                  .equalsIgnoreCase(""))
                                                      && (step_Input.equalsIgnoreCase("i"))
                                                      && (step_Id.equalsIgnoreCase("CBS Maker") || step_Id
                                                                  .equalsIgnoreCase("CBS Maker 1"))
                                                      && getMinorCode().equalsIgnoreCase("FNF")) {

                                                validationDetails
                                                            .addWarning(
                                                                        WarningType.Other,
                                                                        "Shipping Form no ("
                                                                                    + formNO
                                                                                    + ") not found in MDF file  [CM]");

                                          } else {
                                                // // System.out.println("Count value in else
                                                // // dup---------->" + count3);
                                          }

                                          // Port,bill date,bill no combination validation

                                          if ((count4 < 1 || count4 == 0)
                                                      && (!billnoo.equalsIgnoreCase(""))
                                                      && (step_Input.equalsIgnoreCase("i"))
                                                      && (step_Id.equalsIgnoreCase("CBS Maker") || step_Id
                                                                  .equalsIgnoreCase("CBS Maker 1"))
                                                      && getMinorCode().equalsIgnoreCase("FNF")
                                                      && !formType.equalsIgnoreCase("EDF")) {

                                                validationDetails
                                                            .addWarning(WarningType.Other,
                                                                        "Kindly enter valid Shipping bill no,bill date,port code  [CM]");

                                          } else {
                                                // // System.out.println("valid Bill
                                                // // no---------->" + count4);
                                          }

                                          // Port,bill date,form no combination validation

                                          if ((count5 < 1 || count5 == 0)
                                                      && (!formNO.equalsIgnoreCase("") && billnoo
                                                                  .equalsIgnoreCase(""))
                                                      && (step_Input.equalsIgnoreCase("i"))
                                                      && (step_Id.equalsIgnoreCase("CBS Maker") || step_Id
                                                                  .equalsIgnoreCase("CBS Maker 1"))
                                                      && getMinorCode().equalsIgnoreCase("FNF")) {
                                                validationDetails
                                                            .addWarning(WarningType.Other,
                                                                        "Kindly enter valid Form no,bill date,port code  [CM]");

                                          } else {
                                                // // System.out.println("valid Form
                                                // // no---------->" + count5);
                                          }

                                    }
                              } catch (Exception e) {
                                    // System.out.println("shipping bill dup " +
                                    // e.getMessage());
                              } finally {
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
                                          // // System.out.println("Connection Failed! Check
                                          // // output console");
                                          e.printStackTrace();
                                    }
                              }

                              // shipping bill validation
                              try {
                                    List<ExtEventShippingTable> shiplcd = (List<ExtEventShippingTable>) getWrapper()
                                                .getExtEventShippingTable();
                                    int countv = 0;
                                    int countv1 = 0;
                                    int countvodc = 0;
                                    int countv1odc = 0;
                                    String shipBilNo = "";
                                    String shipBilldate = "";
                                    String formNo = "";
                                    String portCode = "";
                                    for (int l = 0; l < shiplcd.size(); l++) {
                                          ExtEventShippingTable shipinglc = shiplcd.get(l);
                                          shipBilNo = shipinglc.getBILLNUM().trim();
                                          formNo = shipinglc.getFORMNUM();

                                          SimpleDateFormat format = new SimpleDateFormat(
                                                      "dd/MM/yy");

                                          shipBilldate = format
                                                      .format(shipinglc.getBILLDAT());
                                          portCode = shipinglc.getPORTCODDD();
                                          // System.out.println("Shpping Bill date " +
                                          // shipBilldate + "and " + portCode);

                                          con = ConnectionMaster.getConnection();
                                          String query = "select count(*) from  exteventsht shc, baseevent bev, master mas where mas.KEY97 = bev.MASTER_KEY and shc.fk_event = bev.extfield AND (bev.STATUS = 'c' OR (mas.STATUS = 'LIV' AND bev.STATUS = case when bev.REFNO_PFIX in ('DPR') then 'c' else 'i' end )) and shc.BILLNUM='"
                                                      + shipBilNo
                                                      + "' AND  TO_CHAR(shc.BILLDAT,'DD/MM/YY')='"
                                                      + shipBilldate
                                                      + "' and shc.PORTCODD='"
                                                      + portCode + "' ";

                                          ps1 = con.prepareStatement(query);
                                          rs1 = ps1.executeQuery();
                                          while (rs1.next()) {
                                                // //System.out.println("Entered while");
                                                countv = rs1.getInt(1);
                                                // //System.out.println("value of count in while
                                                // "
                                                // + countv);

                                          }
                                          // System.out.println("Shipping bill Query
                                          // authorised" + query + "countv===" + countv);

                                          String query1 = "select count(*) from  exteventsht shc, baseevent bev, master mas where mas.KEY97 = bev.MASTER_KEY and shc.fk_event = bev.extfield AND (bev.STATUS = 'c' OR (mas.STATUS = 'LIV' AND bev.STATUS = case when bev.REFNO_PFIX in ('DPR') then 'c' else 'i' end )) and shc.FORMNUM='"
                                                      + formNo
                                                      + "' AND  TO_CHAR(shc.BILLDAT,'DD/MM/YY')='"
                                                      + shipBilldate
                                                      + "' and shc.PORTCODD='"
                                                      + portCode + "' ";

                                          // //System.out.println("Shipping bill formNo
                                          // authorised" + query1);
                                          // con = ConnectionMaster.getConnection();
                                          ps1 = con.prepareStatement(query1);
                                          rs1 = ps1.executeQuery();
                                          while (rs1.next()) {
                                                // //System.out.println("Entered while");
                                                countv1 = rs1.getInt(1);
                                                // //System.out.println("value of count in while
                                                // "
                                                // + countv);

                                          }
                                          // System.out.println("Shipping form Query
                                          // authorised" + query1 + "countv1===" + countv1);

                                          String query2 = "select count(*) from  exteventshc shc, baseevent bev, master mas where mas.KEY97 = bev.MASTER_KEY and shc.fk_event = bev.extfield AND (bev.STATUS = 'c' OR (mas.STATUS = 'LIV' AND bev.STATUS = case when bev.REFNO_PFIX in ('DPR') then 'c' else 'i' end )) and shc.CBILLNUM='"
                                                      + shipBilNo
                                                      + "' AND  TO_CHAR(shc.CBILLDA,'DD/MM/YY')='"
                                                      + shipBilldate
                                                      + "' and shc.CPORTCO='"
                                                      + portCode + "' ";
                                          // //System.out.println("Collection bill no
                                          // used---->
                                          // " + query);
                                          // con = ConnectionMaster.getConnection();
                                          ps1 = con.prepareStatement(query2);
                                          rs1 = ps1.executeQuery();
                                          while (rs1.next()) {
                                                // //System.out.println("Entered while");
                                                countvodc = rs1.getInt(1);
                                                // //System.out.println("value of count in while
                                                // "
                                                // +
                                                // countv);
                                          }

                                          String query3 = "select count(*) from  exteventshc shc, baseevent bev, master mas where mas.KEY97 = bev.MASTER_KEY and shc.fk_event = bev.extfield AND (bev.STATUS = 'c' OR (mas.STATUS = 'LIV' AND bev.STATUS = case when bev.REFNO_PFIX in ('DPR') then 'c' else 'i' end )) and shc.CFORMN='"
                                                      + formNo
                                                      + "' AND  TO_CHAR(shc.CBILLDA,'DD/MM/YY')='"
                                                      + shipBilldate
                                                      + "' and shc.CPORTCO='"
                                                      + portCode + "' ";
                                          // //System.out.println("Collection bill no
                                          // used---->
                                          // " +
                                          // query);
                                          // con = ConnectionMaster.getConnection();
                                          ps1 = con.prepareStatement(query3);
                                          rs1 = ps1.executeQuery();
                                          while (rs1.next()) {
                                                // //System.out.println("Entered while");
                                                countv1odc = rs1.getInt(1);
                                                // //System.out.println("value of count in while
                                                // "
                                                // +
                                                // countv);
                                          }

                                          // FRN validation
if (countv > 0
        && (step_Input.equalsIgnoreCase("i"))
        && (step_Id.equalsIgnoreCase("CBS Maker") || step_Id
                    .equalsIgnoreCase("CBS Maker 1"))
        && getMinorCode().equalsIgnoreCase("FNF")) {
  validationDetails.addWarning(WarningType.Other,
              "Entered shipping bill number already used ("
                          + shipBilNo + ") [CM]");
}

if (countv1 > 0
        && (step_Input.equalsIgnoreCase("i"))
        && (step_Id.equalsIgnoreCase("CBS Maker") || step_Id
                    .equalsIgnoreCase("CBS Maker 1"))
        && getMinorCode().equalsIgnoreCase("FNF")) {
  validationDetails.addWarning(WarningType.Other,
              "Entered shipping form number already used ("
                          + formNo + ") [CM]");
}

if (countvodc > 0
        && (step_Input.equalsIgnoreCase("i"))
        && (step_Id.equalsIgnoreCase("CBS Maker") || step_Id
                    .equalsIgnoreCase("CBS Maker 1"))
        && getMinorCode().equalsIgnoreCase("FNF")) {
  validationDetails.addWarning(WarningType.Other,
              "Entered shipping bill number already used ("
                          + shipBilNo + ") [CM]");
}

if (countv1odc > 0
        && (step_Input.equalsIgnoreCase("i"))
        && (step_Id.equalsIgnoreCase("CBS Maker") || step_Id
                    .equalsIgnoreCase("CBS Maker 1"))
        && getMinorCode().equalsIgnoreCase("FNF")) {
  validationDetails.addWarning(WarningType.Other,
              "Entered shipping Form number already used ("
                          + formNo + ") [CM]");
}

}

} catch (Exception e) {
// System.out.println("shipping bill exception already
// used" + e.getMessage());
} finally {
try {
if (rs1 != null)
  rs1.close();
if (ps1 != null)
  ps1.close();
if (con != null)
  con.close();
} catch (SQLException e) {
// // System.out.println("Connection Failed! Check
// // output console");
e.printStackTrace();
}
}

// //System.out.println("Form type for shipping--->" +
// formtyp);
if (formtyp.equalsIgnoreCase("")
&& (step_Input.equalsIgnoreCase("i"))
&& (step_Id.equalsIgnoreCase("CBS Maker"))
&& getMinorCode().equalsIgnoreCase("FNF")
&& getMajorCode().equalsIgnoreCase("FRN")) {
/*
* if (dailyval_Log.equalsIgnoreCase("YES")) {
* System.out.println("Form type if loop --->" +
* formtyp); }
*/

validationDetails.addError(ErrorType.Other,
  "Form type is mandatory [CM]");

} else {
/*
* if (dailyval_Log.equalsIgnoreCase("YES")) {
* System.out .println(
* "Form type for shipping is blank--->" + formtyp); }
*/
}

} else {

pane.getBtnFetchshipdetFREENEGLCOUTPclay()
.setEnabled(false);
pane.getBtnFetchInvdetFREENEGLC().setEnabled(false);
pane.getBtnFetchShipdetFREENEGLC().setEnabled(false);
pane.getBtnFetchinvdetFREENEGLCOUTPclay().setEnabled(false);
// //System.out.println("product type is not a local cur");
}

// Notional function
if (step_Input.equalsIgnoreCase("i")) {
try {
List<ExtEventShippingTable> shipTable = (List<ExtEventShippingTable>) getWrapper()
  .getExtEventShippingTable();
for (int i = 0; i < shipTable.size(); i++) {

ExtEventShippingTable ship = shipTable.get(i);

String not_str = ship.getNOTIONAL().toString();
// // System.out.println( "shipping table notional
// // length
// // start====>" + not_str + "==>" +
// // not_str.length());
String shiamtcuy = ship.getSHPAMTCurrency();
String reyamtcuy = ship.getREPAYAMCurrency();

BigDecimal rAmt = ship.getREPAYAM();

if (getMinorCode().equalsIgnoreCase("POF")) {
  double notional = 1;
  try {
        con = getConnection();
        String query = "SELECT ETT_SPOTRATE_CAL('"
                    + shiamtcuy + "','" + reyamtcuy
                    + "') FROM DUAL";
        // // System.out.println("Notional rate
        // // function
        // // " + query);

        ps1 = con.prepareStatement(query);
        rs1 = ps1.executeQuery();
        if (rs1.next()) {
              notional = rs1.getDouble(1);
              // // System.out.println("shipping table
              // // notional length start===>" +
              // // notional);
              // ship.setNOTIONAL(new
              // BigDecimal(notional));
        }
  } catch (Exception e1) {
        // System.out.println("Exception Notional
        // rate
        // function" + e1.getMessage());
  }

  String temp_notRate = String.valueOf(notional);

  if (null != not_str
              && not_str.equalsIgnoreCase("1")) {
        ship.setNOTIONAL(new BigDecimal(
                    temp_notRate));
        // // System.out.println("shipping table
        // // notional
        // // length if loop====>" +
        // // ship.getNOTIONAL());

        BigDecimal notional_big = new BigDecimal(
                    notional);
        BigDecimal equi_bill = notional_big
                    .multiply(rAmt);
        // // System.out.println("Notional rate +
        // // Repament amount in big decimal
        // // POD---->" +
        // // equi_bill);
        ship.setEQUBILL(equi_bill);
        ship.setEQUBILLCurrency(ship
                    .getSHPAMTCurrency());
  } else if (null != not_str
              && !temp_notRate
                          .equalsIgnoreCase(not_str)) {
        ship.setNOTIONAL(new BigDecimal(not_str));
        // // System.out.println("shipping table
        // // notional
        // // length else if loop====>" +
        // // ship.getNOTIONAL());
        BigDecimal notional_big = new BigDecimal(
                    not_str);
        BigDecimal equi_bill = notional_big
                    .multiply(rAmt);
        // // System.out.println("Notional rate +
        // // Repament amount in big decimal
        // // POD---->" +
        // // equi_bill);
        ship.setEQUBILL(equi_bill);
        ship.setEQUBILLCurrency(ship
                    .getSHPAMTCurrency());

  } else {
        // // System.out.println("Notional rate is
        // // blank---->");
  }

} else if (getMinorCode().equalsIgnoreCase("FNF")) {
  double notion = 1;
  notion = ship.getNOTIONAL().doubleValue();
  BigDecimal notion_big = new BigDecimal(notion);
  // double shipamt = (notion * ship_Amount);
  // //System.out.println("Notional rate in big
  // decimal DOP---->" + notion_big);
  String ship_str = ship.getSHPAMT().toString();
  BigDecimal shipamt_str = new BigDecimal(
              ship_str);
  BigDecimal shipamt_big = notion_big
              .multiply(shipamt_str);
  // //System.out.println("Notional rate +
  // Shipment
  // amount in big decimal DOP---->" +
  // shipamt_big);
  ship.setEQUBILL(shipamt_big);
  ship.setEQUBILLCurrency(ship
              .getSHPAMTCurrency());
  ship.setLOUTSAMT(shipamt_str);
  ship.setLOUTSAMTCurrency(ship
              .getSHPAMTCurrency());
}
}

} catch (Exception e) {
// System.out.println("Exception in validating for
// notional
// rate--->" + e.getMessage());
} finally {
try {
if (rs1 != null)
  rs1.close();
if (ps1 != null)
  ps1.close();
if (con != null)
  con.close();
} catch (SQLException e) {
// // System.out.println("Connection Failed! Check
// // output
// // console");
e.printStackTrace();
}
}
}
try {

if ((!grwai.equalsIgnoreCase("Y"))
&& grwai.equalsIgnoreCase("N")
&& prd_typ.equalsIgnoreCase("ELF")) {
List<ExtEventShippingTable> rep = (List<ExtEventShippingTable>) getWrapper()
  .getExtEventShippingTable();

for (int j = 0; j < rep.size(); j++) {
ExtEventShippingTable type = rep.get(j);

double notionalDouble = type.getNOTIONAL()
        .doubleValue();
/*
* if (dailyval_Log.equalsIgnoreCase("YES")) {
*
* System.out.println("Notional rate value---->" +
* notionalDouble); }
*/
if ((notionalDouble == 0.0 || notionalDouble == 0 || notionalDouble < 1)
        && rep.size() > 0
        && (step_Input.equalsIgnoreCase("i"))
        && (step_Id.equalsIgnoreCase("CBS Maker") || step_Id
                    .equalsIgnoreCase("CBS Maker 1"))) {
  validationDetails
              .addWarning(WarningType.Other,
                          "Notional rate should not be '0' in shipping details grid [CM]");
} else {
  /*
   * if (dailyval_Log.equalsIgnoreCase("YES")) {
   *
   * // System.out.println(
   * "Notional rate value in else---->" +
   * notionalDouble); }
   */
}

double shpamt = type.getSHPAMT().doubleValue();

// //System.out.println("shipment amount table for
// negative amount===>" + shpamt);
if (shpamt < 0
        && getMinorCode().equalsIgnoreCase("FNF")
        && getMajorCode().equalsIgnoreCase("FRN")
        && (step_Input.equalsIgnoreCase("i"))
        && (step_Id.equalsIgnoreCase("CBS Maker"))) {
  validationDetails
              .addError(ErrorType.Other,
                          "Shipping bill amount should not be negative in shipping details grid [CM]");
} else {
  // // System.out.println("New shipment for not
  // // negative---->" + shpamt);
}
double reyamt = type.getREPAYAM().doubleValue();
if (reyamt < 0
        && getMinorCode().equalsIgnoreCase("POF")
        && getMajorCode().equalsIgnoreCase("FRN")
        && (step_Input.equalsIgnoreCase("i"))
        && (step_Id.equalsIgnoreCase("CBS Maker"))) {
  validationDetails
              .addError(ErrorType.Other,
                          "Repayment amount should not be negative in shipping details grid [CM]");
} else {
  // // System.out.println("New shipment for not
  // // negative---->" + shpamt);
}
}
}
} catch (Exception e) {
// System.out.println("Exception in validating for
// notional
// rate--->" + e.getMessage());
}

// FOB + fright+insurance validation
try {
} catch (Exception e) {
// System.out.println("Exception is Advance Table Validation
// in customer" + e.getMessage());
}
} else {
// // System.out.println("GR waivwr is ticked");
}

// Shipping bill need to delete
if ((step_Id.equalsIgnoreCase("CBS Maker 1"))
&& getMajorCode().equalsIgnoreCase("FRN")
&& getMinorCode().equalsIgnoreCase("FNF")) {

try {

List<ExtEventInvoicedetailsLC> invoiceDet = (List<ExtEventInvoicedetailsLC>) getWrapper()

.getExtEventInvoicedetailsLC();
for (int l = 0; l < invoiceDet.size(); l++) {

ExtEventInvoicedetailsLC invoice_ship = invoiceDet
  .get(l);

try {
String invShipBill = invoice_ship
        .getIShippingBillNo().trim();

if (dailyval_Log.equalsIgnoreCase("YES")) {
  System.out.println("Invoice no shipping bill"
              + invShipBill);
}

if ((invShipBill != null && !invShipBill
        .equalsIgnoreCase(""))
        && invShipBill.length() > 0) {

  /*
   * if (dailyval_Log.equalsIgnoreCase("YES")) {
   * //System.out.println(
   * "Invoice shipping bill length greater than zero"
   * ); }
   */
  int count = 0;
  String query = "SELECT COUNT(*), mas.MASTER_REF, bev.REFNO_PFIX, bev.REFNO_SERL, shc.BILLNUM FROM exteventsht shc, baseevent bev, master mas WHERE mas.KEY97 = bev.MASTER_KEY AND shc.fk_event = bev.extfield AND shc.BILLNUM ='"
              + invShipBill
              + "' GROUP BY mas.MASTER_REF, bev.REFNO_PFIX, bev.REFNO_SERL, shc.BILLNUM";

  /*
   * if (dailyval_Log.equalsIgnoreCase("YES")) {
   * //
   * System.out.println("Invoice shipping bill query"
   * + query); }
   */

  con = ConnectionMaster.getConnection();
  ps1 = con.prepareStatement(query);
  rs1 = ps1.executeQuery();
  while (rs1.next()) {
        // //System.out.println("Entered while");
        count = rs1.getInt(1);

  }

  /*
   * if (dailyval_Log.equalsIgnoreCase("YES")) {
   * System
   * .out.println("Invoice bill count if loop" +
   * count); }
   */
  if ((count == 0 || count < 1)
              && (step_Input.equalsIgnoreCase("i"))
              && !prd_typ.equalsIgnoreCase("NLD")
              && (step_Id
                          .equalsIgnoreCase("CBS Maker 1"))) {
        validationDetails
                    .addError(
                                ErrorType.Other,
                                "Please delete the Invoice number row ("
                                            + invShipBill
                                            + ") which is not available in 1st Shipping bill grid [CM]");
  } else {

        /*
         * if (dailyval_Log.equalsIgnoreCase("YES"))
         * { //System.out.println(
         * "Shipping bill for comparation invShipBill else===>"
         * // + invShipBill + "And count" + count);
         * }
         */

  }
} else {

}
} catch (Exception e) {

if (dailyval_Log.equalsIgnoreCase("YES")) {
  System.out.println("Exception Invoice number"
              + e.getMessage());
}
}
try {
String invFormBill = invoice_ship.getIFORNO();

if (dailyval_Log.equalsIgnoreCase("YES")) {
  // System.out.println("Invoice no shipping bill"
  // + invFormBill);
}
if ((invFormBill != null && !invFormBill
        .equalsIgnoreCase(""))
        && invFormBill.length() > 0) {

  if (dailyval_Log.equalsIgnoreCase("YES")) {
        // System.out.println("Invoice form no length greater than zero");
  }

  int count = 0;
  String query = "SELECT COUNT(*), mas.MASTER_REF, bev.REFNO_PFIX, bev.REFNO_SERL, shc.FORMNUM FROM exteventsht shc, baseevent bev, master mas WHERE mas.KEY97 = bev.MASTER_KEY AND shc.fk_event = bev.extfield AND shc.FORMNUM ='"
              + invFormBill
              + "' GROUP BY mas.MASTER_REF, bev.REFNO_PFIX, bev.REFNO_SERL, shc.FORMNUM";
  if (dailyval_Log.equalsIgnoreCase("YES")) {
        // System.out.println("Invoice form no query"
        // + query);
  }

  con = ConnectionMaster.getConnection();
  ps1 = con.prepareStatement(query);
  rs1 = ps1.executeQuery();
  if (rs1.next()) {
        // //System.out.println("Entered while");
        count = rs1.getInt(1);

        /*
         * if (dailyval_Log.equalsIgnoreCase("YES"))
         * { // System.out.println(
         * "Invoice form number count if loop" +
         * count); }
         */
  }

  else if ((step_Input.equalsIgnoreCase("i"))
              && !prd_typ.equalsIgnoreCase("NLD")
              && (step_Id
                          .equalsIgnoreCase("CBS Maker 1"))) {
        validationDetails
                    .addError(
                                ErrorType.Other,
                                "Please delete the Invoice form number row ("
                                            + invFormBill
                                            + ") which is not available in 1st Shipping bill grid [CM]");
  } else {

        /*
         * if (dailyval_Log.equalsIgnoreCase("YES"))
         * { //System.out.println(
         * "Shipping bill for comparation invFormBill else===>"
         * // + invFormBill + "And count" + count);
         * }
         */

  }

} else {

}
} catch (Exception e) {

if (dailyval_Log.equalsIgnoreCase("YES")) {
  System.out.println("Exception Form number"
              + e.getMessage());
}
}
}

} catch (Exception e) {

if (dailyval_Log.equalsIgnoreCase("YES")) {
System.out.println("Exception in Invoice details"
  + e.getMessage());
}
}

// Shipping bill need to delete

try {
List<ExtEventShippingdetailslc> shipDet = (List<ExtEventShippingdetailslc>) getWrapper()
.getExtEventShippingdetailslc();
for (int l = 0; l < shipDet.size(); l++) {

ExtEventShippingdetailslc invoice_ship = shipDet.get(l);

try {
String invShipBill = invoice_ship.getLCBILLNO()
        .trim();
/*
* if (dailyval_Log.equalsIgnoreCase("YES")) {
* //System.out.println("2nd shipping bill" +
* invShipBill); }
*/
if ((invShipBill != null && !invShipBill
        .equalsIgnoreCase(""))
        && invShipBill.length() > 0) {
  int count = 0;
  String query = "SELECT COUNT(*), mas.MASTER_REF, bev.REFNO_PFIX, bev.REFNO_SERL, shc.BILLNUM FROM exteventsht shc, baseevent bev, master mas WHERE mas.KEY97 = bev.MASTER_KEY AND shc.fk_event = bev.extfield AND shc.BILLNUM ='"
              + invShipBill
              + "' GROUP BY mas.MASTER_REF, bev.REFNO_PFIX, bev.REFNO_SERL, shc.BILLNUM";
  if (dailyval_Log.equalsIgnoreCase("YES")) {
        // System.out.println("2nd shipping bill query"
        // + query);
  }

  con = ConnectionMaster.getConnection();
  ps1 = con.prepareStatement(query);
  rs1 = ps1.executeQuery();
  if (rs1.next()) {
        // //System.out.println("Entered while");
        count = rs1.getInt(1);

        /*
         * if (dailyval_Log.equalsIgnoreCase("YES"))
         * { System.out.println(
         * "2nd shipping bill count if loop" +
         * count); }
         */
  }

  else if ((step_Input.equalsIgnoreCase("i"))
              && !prd_typ.equalsIgnoreCase("NLD")
              && (step_Id
                          .equalsIgnoreCase("CBS Maker 1"))) {
        validationDetails
                    .addError(
                                ErrorType.Other,
                                "Please delete the Shipping bill number row ("
                                            + invShipBill
                                            + ") which is not available in 1st Shipping bill grid [CM]");
  } else {

        /*
         * if (dailyval_Log.equalsIgnoreCase("YES"))
         * { // System.out.println(
         * "Shipping bill for comparation invShipBill else===>"
         * // + invShipBill + "And count" + count);
         * }
         */

  }
} else {

}

} catch (Exception e) {

/*
* if (dailyval_Log.equalsIgnoreCase("YES")) {
* //System
* .out.println("Exception 2nd shipping bill" +
* e.getMessage());
*
* }
*/

}
try {
String invForm = invoice_ship.getLCFORMNO().trim();

if (dailyval_Log.equalsIgnoreCase("YES")) {
  // System.out.println("2nd Shipping Form number"
  // + invForm);

}

if ((invForm != null && !invForm
        .equalsIgnoreCase(""))
        && invForm.length() > 0) {

  int count = 0;
  String query = "SELECT COUNT(*), mas.MASTER_REF, bev.REFNO_PFIX, bev.REFNO_SERL, shc.FORMNUM FROM exteventsht shc, baseevent bev, master mas WHERE mas.KEY97 = bev.MASTER_KEY AND shc.fk_event = bev.extfield AND shc.FORMNUM ='"
              + invForm
              + "' GROUP BY mas.MASTER_REF, bev.REFNO_PFIX, bev.REFNO_SERL, shc.FORMNUM";

  if (dailyval_Log.equalsIgnoreCase("YES")) {
        // System.out.println("2nd Shipping Form number query"
        // + query);

  }

  con = ConnectionMaster.getConnection();
  ps1 = con.prepareStatement(query);
  rs1 = ps1.executeQuery();
  if (rs1.next()) {
        // //System.out.println("Entered while");
        count = rs1.getInt(1);
  }

  else if ((step_Input.equalsIgnoreCase("i"))
              && !prd_typ.equalsIgnoreCase("NLD")
              && (step_Id
                          .equalsIgnoreCase("CBS Maker 1"))) {
        validationDetails
                    .addError(
                                ErrorType.Other,
                                "Please delete the 2nd Shipping form number row ("
                                            + invForm
                                            + ") which is not available in 1st Shipping bill grid [CM]");
  } else {

        if (dailyval_Log.equalsIgnoreCase("YES")) {
              // System.out.println("Shipping form for comparation 2nd Shipping from  else===>"
              // + invForm + "And count" + count);

        }
  }

} else {

}
} catch (Exception e) {

if (dailyval_Log.equalsIgnoreCase("YES")) {
  System.out
              .println("Exception 2nd shipping form"
                          + e.getMessage());

}

}

}

} catch (Exception e) {

if (dailyval_Log.equalsIgnoreCase("YES")) {
System.out.println("Exception in Shipping details"
  + e.getMessage());

}
}

}
// -------------****************-------------------

// Advance Table Validations

List<ExtEventAdvanceTable> liste = (List<ExtEventAdvanceTable>) getWrapper()
.getExtEventAdvanceTable();

// Advance Table Validations
if (getMajorCode().equalsIgnoreCase("FRN")
&& getMinorCode().equalsIgnoreCase("POF")) {
// //System.out.println("Advance Table Validation for
// collection--->");
try {
String customer = getDriverWrapper().getEventFieldAsText(
"BEN", "p", "no").trim();
String advrec = getWrapper().getPERADV();

if (dailyval_Log.equalsIgnoreCase("YES")) {

// System.out.println("Advance received" + advrec);
}
if ((!advrec.equalsIgnoreCase(""))
&& (advrec.equalsIgnoreCase("Full") || advrec
        .equalsIgnoreCase("Part"))) {

if ((liste.size() == 0 || liste.size() < 1)
  && (step_Input.equalsIgnoreCase("i"))
  && (step_Id.equalsIgnoreCase("CBS Maker"))) {
validationDetails
        .addError(
                    ErrorType.Other,
                    "Advance Received selection is selected,please input Advance Remittance Grid [CM]");
}

}

if ((liste.size() > 0)
&& (step_Input.equalsIgnoreCase("i"))
&& step_Id.equalsIgnoreCase("CBS Maker")) {
if (advrec.equalsIgnoreCase("") || advrec.length() < 1) {
validationDetails
        .addError(ErrorType.Other,
                    "Advance Remittance Grid entered,Please select Advance Received selection [CM]");
}
}

for (int a = 0; a < liste.size(); a++) {
ExtEventAdvanceTable advance = liste.get(a);

// System.out.println("RemCunntry no---------->" +
// RemCunntry);
// String RemDate = advance.getDATREM().toString();
// System.out.println("RemDate---------->" + RemDate);
String inwRem = advance.getINWARD().trim();
// System.out.println("Inward remittance no---------->"
// +
// inwRem);
String firc = advance.getFINUMB();
// System.out.println("firc no---------->" + firc);
String cusNum = advance.getCUSCIFNO();
// System.out.println("cusNum no---------->" + cusNum);
String RemName = advance.getNAMREM();
// System.out.println("RemName no---------->" +
// RemName);
String advrem = advance.getADVRECB();
// System.out.println("advrem no---------->" + advrem);
String RemCunntry = advance.getCOUNREM();

if (dailyval_Log.equalsIgnoreCase("YES")) {

System.out
        .println("Inward remittance no---------->"
                    + inwRem);
System.out.println("firc no---------->" + firc);
}

try {

if (!inwRem.equalsIgnoreCase("")
        || inwRem.length() > 0) {
  if (cusNum.equalsIgnoreCase("")
              && RemName.equalsIgnoreCase("")
              && (RemCunntry.equalsIgnoreCase(""))
              && (step_Input.equalsIgnoreCase("i"))
              && (step_Id
                          .equalsIgnoreCase("CBS Maker") || step_Id
                          .equalsIgnoreCase("CBS Maker 1"))) {
        validationDetails
                    .addWarning(WarningType.Other,
                                "Please fetch the advance details table [CM]");

  } else {

        if (dailyval_Log.equalsIgnoreCase("YES")) {

              // System.out.println(
              // "Fiels value cusNumber--------->" +
              // cusNum + "RemName" + RemName);
        }
  }
} else {

  if (dailyval_Log.equalsIgnoreCase("YES")) {

        // System.out.println("Inward remittance
        // no---------->"
        // + inwRem);
  }
}

if (!firc.equalsIgnoreCase("") || firc.length() > 0) {
  if (cusNum.equalsIgnoreCase("")
              && RemName.equalsIgnoreCase("")
              && advrem.equalsIgnoreCase("")
              && RemCunntry.equalsIgnoreCase("")
              && (step_Input.equalsIgnoreCase("i"))
              && (step_Id
                          .equalsIgnoreCase("CBS Maker"))) {
        validationDetails
                    .addError(
                                ErrorType.Other,
                                "Please Input Remittance AdCode,Remitter Name,Remittance Date,Remittance country,Customer CIF No,Available amount [CM]");

  } else {

        if (dailyval_Log.equalsIgnoreCase("YES")) {

              // System.out.println("Fiels value
              // cusNu--------->"
              // + cusNum + "RemName" + RemName
              // + "advrem" + advrem + "RemCunntry" +
              // RemCunntry);
        }
  }
} else {
  // System.out.println("firc number---------->" +
  // firc);
}
if (!firc.equalsIgnoreCase("") || firc.length() > 0) {
  if ((!inwRem.equalsIgnoreCase("") || inwRem
              .length() > 0)
              && (step_Input.equalsIgnoreCase("i"))
              && (step_Id
                          .equalsIgnoreCase("CBS Maker"))) {

        validationDetails
                    .addError(ErrorType.Other,
                                "FIRC No is inputted, Please remove the Inward remittance no [CM]");
  }
}

if (!inwRem.equalsIgnoreCase("")
        || inwRem.length() > 0) {
  if ((!firc.equalsIgnoreCase("") || firc
              .length() > 0)
              && (step_Input.equalsIgnoreCase("i"))
              && (step_Id
                          .equalsIgnoreCase("CBS Maker"))) {

        validationDetails
                    .addError(ErrorType.Other,
                                "Inward remittance no is inputted, Please remove the FIRC No [CM]");
  }
}

String query2 = "select count(*) FROM ETTV_INWARD_REMITTANCE_AMOUNT WHERE MAS_REF='"
        + inwRem + "'";
con = ConnectionMaster.getConnection();
ps = con.prepareStatement(query2);
if (dailyval_Log.equalsIgnoreCase("YES")) {

  // System.out.println("Inward remittance number
  // query"
  // + query2);
}
rs = ps.executeQuery();
int count1 = 0;
// //System.out.println("result of query "+rs2);
while (rs.next()) {
  count1 = rs.getInt(1);
  if ((liste.size() > 0)
              && (count1 == 0 || count1 < 1)
              && (step_Input.equalsIgnoreCase("i"))
              && (step_Id
                          .equalsIgnoreCase("CBS Maker") || step_Id
                          .equalsIgnoreCase("CBS Maker 1"))) {
        validationDetails.addWarning(
                    WarningType.Other,
                    "Please enter Valid Inward remittance number ("
                                + inwRem + ") [CM]");
  }

}

} catch (Exception e) {

if (dailyval_Log.equalsIgnoreCase("YES")) {

  System.out
              .println("Exception Inward remittance number"
                          + e.getMessage());
}
}
}

try {
String cusNo = "";
double balan = 0.0;
double amtutl = 0.0;
String balancur = "";
String amtutlcur = "";
double utlamt_long = 0;
double utlamt_total = 0;
double total_rep = 0;

for (int a = 0; a < liste.size(); a++) {
ExtEventAdvanceTable adve = liste.get(a);
cusNo = adve.getCUSCIFNO().trim();

amtutl = adve.getAMTUTIL().doubleValue();

amtutlcur = adve.getAMTUTILCurrency();

ConnectionMaster connectionMaster = new ConnectionMaster();
double divideByDecimal = connectionMaster
        .getDecimalforCurrency(amtutlcur);
utlamt_long = amtutl / divideByDecimal;
// //System.out.println("Advance Table utilization
// amount
// after divided--->" + utlamt_long);
utlamt_total = utlamt_total + utlamt_long;
// //System.out.println("Advance Table utilization
// amount
// total--->" + utlamt_total);

// todo sep 5 2018
double rep_total = 0.0;
List<ExtEventShippingTable> shiplcd = (List<ExtEventShippingTable>) getWrapper()
        .getExtEventShippingTable();
ExtEventShippingTable shipinglc = null;
shipinglc = shiplcd.get(a);
rep_total = shipinglc.getREPAYAM().doubleValue();
// System.out.println("shipmentrep_total!!!!!!---->"
// + rep_total);
String curr = shipinglc.getREPAYAMCurrency().trim();
// System.out.println("Repaycurrency!!!!!!!!" +
// curr);
// ConnectionMaster connectionMaster = new
// ConnectionMaster();
double divideBy = connectionMaster
        .getDecimalforCurrency(curr);
repay_Amt = rep_total / divideBy;
// System.out.println("Repayamount!!!!!!!" +
// repay_Amt);
total_rep = total_rep + repay_Amt;
// System.out.println("Repayamount__am_rey!!!!!!!" +
// total_rep);

if (dailyval_Log.equalsIgnoreCase("YES")) {

  // System.out.println("Advance Table utilization
  // amount total--->"
  // + utlamt_total);
  // System.out.println("Repayment amount
  // total--->"
  // + total_rep);

  /*
   * rep_total =
   * shipinglc.getREPAYAM().doubleValue();
   * System.out.println("shipmentrep_total---->" +
   * rep_total);
   *
   * System.out.println("Repaycurrency"+curr);
   */

  // System.out.println("Repayamount" +
  // repay_Amt);

  /*
   * System.out.println("Repayamount_am_rey"+
   * am_rey);
   */
}

if ((!cusNo.equalsIgnoreCase(customer))
        && (step_Input.equalsIgnoreCase("i"))
        && (step_Id.equalsIgnoreCase("CBS Maker") || step_Id
                    .equalsIgnoreCase("CBS Maker 1"))) {
  validationDetails
              .addWarning(WarningType.Other,
                          "Remittance Customer is not same as the Beneficiary in advance table [CM]");

}

balan = adve.getBALANCE().doubleValue();
balancur = adve.getBALANCECurrency();
if (amtutl > balan
        && (step_Input.equalsIgnoreCase("i"))
        && (step_Id.equalsIgnoreCase("CBS Maker"))) {
  validationDetails
              .addError(
                          ErrorType.Other,
                          "Advance payment utillization  amount should not greater than available amount in advance table [CM]");
}
if (!balancur.equalsIgnoreCase(amtutlcur)
        && (step_Input.equalsIgnoreCase("i"))
        && (step_Id.equalsIgnoreCase("CBS Maker") || step_Id
                    .equalsIgnoreCase("CBS Maker 1"))) {
  validationDetails
              .addWarning(
                          WarningType.Other,
                          "Utillization of amount currency should equal to available amount currency in advance table [CM]");
}

}

if (liste.size() > 0
  && (utlamt_total != total_rep)
  && (step_Input.equalsIgnoreCase("i"))
  && (step_Id.equalsIgnoreCase("CBS Maker") || step_Id
              .equalsIgnoreCase("CBS Maker 1"))) {
// // System.out.println("Advance Table
// // utilization
// // amount & repayment compare if loop--->"+
// // utlamt_total + "<===>" + repay_total);
// System.out.println("Inside SUM of" + total_rep +
// "Util" + utlamt_total);
validationDetails
        .addWarning(
                    WarningType.Other,
                    "Sum of Advance payment Utillization amount and Repayment amount should be same [CM]");

} else {
// System.out.println("Utilization amount &
// repayment
// compare else--->" + utlamt_total + "<===>"+
// repay_total);
}

} catch (Exception e) {

if (dailyval_Log.equalsIgnoreCase("YES")) {

System.out
        .println("Exception is Advance Table Validation in customer"
                    + e.getMessage());
}
}

} catch (Exception e) {
if (dailyval_Log.equalsIgnoreCase("YES")) {

System.out.println("Exception is Advance Table Final"
  + e.getMessage());
}
}

} else {

if (dailyval_Log.equalsIgnoreCase("YES")) {

System.out
.println("Advance Table Validation other product--->");
}
}

if (mixedpay.equalsIgnoreCase("Y")
&& (payaction.equalsIgnoreCase("Pay"))) {
double addAmountDub = 0;
String amountcol = "";
try {
String addAmount = getDriverWrapper().getEventFieldAsText(
"AAC", "v", "m").trim();

addAmountDub = Double.valueOf(addAmount);

} catch (Exception e) {
addAmountDub = 0;
System.out
.println("Exception in additional Amount mixed payment"
        + e.getMessage());
}
if (addAmountDub > 0) {
amountcol = getDriverWrapper().getEventFieldAsText("AMC",
"v", "m");
} else {
amountcol = getDriverWrapper().getEventFieldAsText("EVAM",
"v", "m");
}
BigDecimal amountBig = new BigDecimal(amountcol);
// if (amountcol.length() > 0) {
// amountdob = Double.parseDouble(amountcol);
// }

if (dailyval_Log.equalsIgnoreCase("YES")) {
// System.out.println("Mixed payment Presentation
// amount--->"
// + amountBig);
}
String mixpay = "";
String mixcur = "";
try {

con = getConnection();
String dms = "SELECT mas.MASTER_REF, bev.REFNO_PFIX, bev.REFNO_SERL, SUM(pap.PAY_AMT), pap.PAY_AMTCCY FROM master mas, BASEEVENT bev, LCPAYMENT lcp, PARTPAYMNT pap WHERE mas.KEY97 = bev.MASTER_KEY AND bev.KEY97 = lcp.KEY97 AND lcp.KEY97 = pap.PAYEV_KEY AND mas.MASTER_REF = '"
+ MasterReference
+ "' AND bev.REFNO_PFIX = '"
+ evnt
+ "' AND bev.REFNO_SERL ="
+ evvcount
+ " GROUP BY mas.MASTER_REF, bev.REFNO_PFIX, bev.REFNO_SERL, pap.PAY_AMTCCY";

dmsp = con.prepareStatement(dms);
if (dailyval_Log.equalsIgnoreCase("YES")) {
System.out
  .println("Mixed payment validation for Comparation--->"
              + dms);
}
dmsr = dmsp.executeQuery();
while (dmsr.next()) {
mixpay = dmsr.getString(4);
mixcur = dmsr.getString(5).trim();

}
} catch (SQLException e) {

if (dailyval_Log.equalsIgnoreCase("YES")) {
System.out.println("Mixed payment value Exception--->"
  + e.getMessage());
}
}
if (mixpay.length() > 0) {
// double mixpay_dob = Double.valueOf(mixpay);

BigDecimal mixpayAmt = new BigDecimal(mixpay);

if (dailyval_Log.equalsIgnoreCase("YES")) {
System.out
  .println("Mixed payment Value in BigDecimal--->"
              + mixpayAmt);
}
ConnectionMaster connectionMaster = new ConnectionMaster();
double dividemixcur = connectionMaster
.getDecimalforCurrency(mixcur);
BigDecimal divideMix = new BigDecimal(dividemixcur);

if (dailyval_Log.equalsIgnoreCase("YES")) {
// System.out.println("Mixed payment Value decimal--->"
// + divideMix);
}
BigDecimal mixpayBig = mixpayAmt.divide(divideMix);
// mixpay_tot = mixpay_dob / dividemixcur;

if (dailyval_Log.equalsIgnoreCase("YES")) {
// System.out.println("Mixed payment value--->" +
// mixpayBig + "Outstanding amount" + amountBig);
}
if ((amountBig.compareTo(mixpayBig) != 0)
&& (step_Input.equalsIgnoreCase("i"))
&& (step_Id.equalsIgnoreCase("CBS Maker 1"))
&& getMinorCode().equalsIgnoreCase("POF")) {
validationDetails
  .addError(
              ErrorType.Other,
              "Mixed payment is checked, Payment amount should equal to Presentation amount [CM]");
} else {

if (dailyval_Log.equalsIgnoreCase("YES")) {
// System.out.println(
// "Mixed payment value else loop--->" + mixpayBig +
// "Outstanding amount" + amountBig);
}
}

}

} else {

if (dailyval_Log.equalsIgnoreCase("YES")) {
// System.out.println(
// "Sum of Equivalent Bill Amount and short collection
// amount should be equal====>>>");
}
}

try {
String nosamt = getWrapper().getNOSAMT();
String nosacc = getWrapper().getNOSTACC();
String nosamtt = getWrapper().getNOSTAMT();
String nosDate = getWrapper().getNOSTDAT();
String nosPoolamt = getWrapper().getPOOLAMT();
String nosRef103 = getWrapper().getNOSTMT();
String nosREF950 = getWrapper().getNOSTRM();

if (liste.size() > 0 && getMinorCode().equalsIgnoreCase("POF")) {

if ((nosamt.length() > 0 || nosacc.length() > 0
|| nosamtt.length() > 0 || nosDate.length() > 0
|| nosPoolamt.length() > 0
|| nosRef103.length() > 0 || nosREF950.length() > 0)
&& (step_Input.equalsIgnoreCase("i"))
&& (step_Id.equalsIgnoreCase("CBS Maker"))) {
validationDetails
  .addError(
              ErrorType.Other,
              "Advance payment details are there kindly do not enter Nostro Utilization Amount I/O Nostro details [CM]");
} else {
// // System.out.println("Advance remittance grid using
// // this transaction else");
}

} else if ((nosamt.length() > 0 || nosacc.length() > 0
|| nosamtt.length() > 0 || nosDate.length() > 0
|| nosPoolamt.length() > 0 || nosRef103.length() > 0 || nosREF950
.length() > 0)
&& getMinorCode().equalsIgnoreCase("POF")) {
if (liste.size() > 0
&& (step_Input.equalsIgnoreCase("i"))
&& (step_Id.equalsIgnoreCase("CBS Maker") || step_Id
        .equalsIgnoreCase("CBS Maker 1"))) {
validationDetails
  .addWarning(WarningType.Other,
              "Nostro details are there kindly do not enter Advance payment details [CM]");
} else {
// // System.out.println("Nostro values using this
// // transaction else");
}
}
} catch (Exception e) {
// System.out.println("Exception for Notro values getting===>" +
// e);
}

// Category code populate based on input branch
try {
String BranchCode = getDriverWrapper().getEventFieldAsText(
"BIN", "b", "c");
con = ConnectionMaster.getConnection();
if (!(BranchCode.length() == 0)) {
String sql6 = "select telex from capf where cabrnm='"
+ BranchCode + "'";
// //System.out.println("BranchCode Query - " + sql6);
PreparedStatement ps1 = con.prepareStatement(sql6);
ResultSet rs = ps1.executeQuery();
while (rs.next()) {
String inmt = rs.getString(1);
// //System.out.println("category code - " + inmt);
getWrapper().setIMBRCODE(inmt);
getPane().setIMBRCODE(inmt);
}

}
} catch (Exception e) {
// System.out.println("Exception caught on branch code
// validation......" + e.getMessage());
} finally {
try {
if (rs != null)
rs.close();
if (ps1 != null)
ps1.close();
if (con != null)
con.close();
} catch (SQLException e) {
// // System.out.println("Connection Failed! Check output
// // console");
e.printStackTrace();
}
}

try {

String IEcodec = getDriverWrapper().getEventFieldAsText("DRW",
"p", "cBBF");
// //System.out.println("IEcodecode" + IEcodec);
List<ExtEventShippingTable> shpcol5 = (List<ExtEventShippingTable>) getWrapper()
.getExtEventShippingTable();
for (int l = 0; l < shpcol5.size(); l++) {
ExtEventShippingTable shipCol = shpcol5.get(l);
String shipBilNo = shipCol.getBILLNUM();
// //System.out.println("ship BilNo softax ---->" +
// shipBilNo);
String formNo = shipCol.getFORMNUM();
String IEcode = shipCol.getIECOD();
if (IEcode.trim().length() > 0
&& (step_Input.equalsIgnoreCase("i"))
&& (step_Id.equalsIgnoreCase("CBS Maker") || step_Id
        .equalsIgnoreCase("CBS Maker 1"))) {
if (IEcodec.equalsIgnoreCase("IEcode")) {
validationDetails.addWarning(WarningType.Other,
        "Please enter Valid IECODE (" + IEcodec
                    + ") [CM]");
}
}

}

String sizeship = getWrapper().getNOOFBLLS();
if (!(shpcol5.size() == (Integer.valueOf(sizeship)))
&& getMinorCode().equalsIgnoreCase("FNF")
&& (step_Input.equalsIgnoreCase("i"))
&& (step_Id.equalsIgnoreCase("CBS Maker"))) {
validationDetails.addError(ErrorType.Other,
"Number of Shipping bills(" + sizeship
        + ")is not equal to ShippingDetails grid("
        + shpcol5.size() + ") :[CM]");
}

} catch (Exception e) {
// System.out.println("Exception in EDI for softax" +
e.getMessage();
}

// port of destination description

String portdes = getWrapper().getPORTDES().trim();
// //System.out.println("Port of destination Value---->" + portdes);
if ((!portdes.equalsIgnoreCase("")) && portdes != null) {
try {
String portvalqury = "select trim(PODEST),trim(COUNTRY) from EXTPORTDESTINATION WHERE PODEST='"
+ portdes + "'";
// //System.out.println("port code destination query
// Value---->" +
// portvalqury);
con = ConnectionMaster.getConnection();
ps1 = con.prepareStatement(portvalqury);
rs = ps1.executeQuery();
while (rs.next()) {
String hsploy = rs.getString(1);
String COUNTRY = rs.getString(2);
// //System.out.println("port code description---->" +
// hsploy);
getPane().setPODESCP(hsploy);
getPane().setPODESCON(COUNTRY);
}

// con.close();
// ps1.close();
// rs.close();
} catch (Exception e) {
// System.out.println("exception is " + e.getMessage());
}

finally {
try {
if (rs != null)
rs.close();
if (ps1 != null)
ps1.close();
if (con != null)
con.close();
} catch (SQLException e) {
// // System.out.println("Connection Failed! Check
// output
// // console");
e.printStackTrace();
}
}
} else {
// //System.out.println("port code is destination empty");
}

// SUPPLIERS CREDIT
String issDate = getDriverWrapper().getEventFieldAsText("ISS", "d",
"");
String expDate = getDriverWrapper().getEventFieldAsText("EXP", "d",
"");
// System.out.println("suppliers credit issdate " + issDate +
// "expDate" + expDate);
int tnr = 0;
try {

// // System.out.println("Nosrto date for calculation" +
// // d);

if (issDate != null && expDate != null && issDate.length() > 0
&& expDate.length() > 0) {
String dateval = (difference(expDate, issDate).substring(0,
difference(expDate, issDate).indexOf(".")));
// System.out.println("SUPPLIERS date differnce" + dateval);
tnr = Integer.parseInt(dateval);

} else {
// // System.out.println("else part of date
// // difference values");
}
// System.out.println("SUPPLIERS date in number" + tnr);
if (tnr >= 180) {
// //System.out.println("suppliers credit is yes ");
getWrapper().setSUPLCRE(true);
getPane().setSUPLCRE(true);

// //System.out.println("suppliers credit is yes ended ");

} else {
getWrapper().setSUPLCRE(false);
getPane().setSUPLCRE(false);
}

} catch (Exception e) {
// System.out.println("SUPPLIER exeception date difference
// values" + e);
}

// PAN number validation
try {
if (getDriverWrapper().getEventFieldAsText("EVCD", "s", " ")
.trim().equalsIgnoreCase("PCOC")
&& (step_Input.equalsIgnoreCase("i"))
&& (step_Id.equalsIgnoreCase("CBS Maker")))
;
{
double amt;
// //System.out.println("PAN VALIDATION");

String product = getDriverWrapper().getEventFieldAsText("",
"r", "");
String panamt = getDriverWrapper().getEventFieldAsText(
"AMT", "v", "i");
// //System.out.println("panamt " +panamt);
String pannumber = getDriverWrapper().getEventFieldAsText(
"cAGE", "s", "");
// //System.out.println("pannumber"+pannumber);
// //System.out.println("pannumber length" +
// pannumber.length());
if (panamt.length() == 1) {

amt = 0.0;
} else {
// //System.out.println("first else block");
amt = Integer.parseInt(panamt.substring(0,
  panamt.length() - 3));
if (amt >= 25000 && pannumber.length() == 1) {
// validationDetails.addWarning(ValidationDetails.WarningType.Other,
// "Please enter the PAN Number");
// // System.out.println("second if block");
}
}
}
} catch (Exception e) {
}
// IFSC code validation

String Ifsc = getWrapper().getIFSCCO_Name().trim();
if (Ifsc.trim().equalsIgnoreCase("") || Ifsc == null) {
} else {
if ((!Ifsc.trim().equalsIgnoreCase("") || Ifsc != null)
&& (step_Input.equalsIgnoreCase("i"))
&& (step_Id.equalsIgnoreCase("CBS Maker"))) {
try {
con = getConnection();
String query = "select count(*) from EXTIFSCC where IFSC='"
  + Ifsc + "'";
// //System.out.println("query " + query);
int count = 0;
ps1 = con.prepareStatement(query);
rs1 = ps1.executeQuery();
while (rs1.next()) {
// //System.out.println("Entered while");
count = rs1.getInt(1);
}

if (count == 0 && (step_Input.equalsIgnoreCase("i"))
  && (step_Id.equalsIgnoreCase("CBS Maker"))) {

validationDetails.addError(ErrorType.Other,
        "Invalid Beneficiary IFSC code(" + Ifsc
                    + ")  [CM]");
}

} catch (Exception e1) {
System.out.println("Exception" + e1.getMessage());
}

finally {
ConnectionMaster.surrenderDB(con, ps1, rs1);
}
}
}

// LIMIT BLOCKING -LIMBLK

if (getMajorCode().equalsIgnoreCase("FRN")
&& getMinorCode().equalsIgnoreCase("FNF")
|| getMinorCode().equalsIgnoreCase("POF")) {

try {
// String relevnt=null;
String Mast = getDriverWrapper().getEventFieldAsText(
"MMST", "r", "").trim();
String Evnt = getDriverWrapper().getEventFieldAsText("EVR",
"r", "").trim();

con = getConnection();

String query = "WITH MAXVAL AS (SELECT row_number() over (ORDER BY BEV.KEY97 asc) as ROWN,"
+ "MAS.MASTER_REF,MAS.KEY97 mas_key,"
+ "BEV.KEY97 bev_key,BEV.REFNO_PFIX || lpad(BEV.REFNO_SERL,3,0) bev_ref ,LIMBLK FROM"
+ " MASTER MAS,BASEEVENT  BEV ,EXTEVENT  EXT WHERE MAS.KEY97=BEV.MASTER_KEY AND BEV.KEY97=EXT.EVENT"
+ " and BEV.status<>'a' and trim(LIMBLK) is not null AND MAS.MASTER_REF='"
+ Mast
+ "'  ORDER BY BEV.KEY97)"
+ " SELECT LIMBLK FROM MAXVAL "
+ " WHERE ROWN IN (SELECT CASE WHEN '"
+ Evnt
+ "' lIKE 'FN%' THEN ROWN ELSE ROWN-1 END  "
+ " FROM MAXVAL WHERE bev_ref='" + Evnt + "')";

ps1 = con.prepareStatement(query);

rs1 = ps1.executeQuery();
while (rs1.next()) {
String prelimit = rs1.getString(1);
System.out.println("Prelimit=======>" + prelimit);

getPane().setPRVLIMBL(prelimit);
}
System.out.println("Previous limit"
+ getPane().getPRVLIMBL());
// }
} catch (Exception e) {
e.printStackTrace();
System.out.println("Exception Previous limit blocking"
+ e.getMessage());

} finally {
ConnectionMaster.surrenderDB(con, ps1, rs1);

}

}

// PRESHIPMENT LINK-LOANDETAILS
String masterref = getDriverWrapper().getEventFieldAsText("MST",
"r", "").trim();

if (getMajorCode().equalsIgnoreCase("FRN")
&& getMinorCode().equalsIgnoreCase("POF")
&& (step_csm.equalsIgnoreCase("CBS Maker")
|| step_csm.equalsIgnoreCase("CBS Maker 1")
|| step_csm.equalsIgnoreCase("CBS Authoriser") || step_csm
  .equalsIgnoreCase("CBS Reject"))) {
// int value=0;
String loanRef = "";
BigDecimal Amount = null;
String ccy = "";
int loancount = 0;

try {
System.out.println("Inside Loan details for preshipment");
List<ExtEventLoanDetails> LoanDet = (List<ExtEventLoanDetails>) getWrapper()
.getExtEventLoanDetails();
loancount = LoanDet.size();

for (int l = 0; l < LoanDet.size(); l++) {
ExtEventLoanDetails loandetails = LoanDet.get(l);
BigDecimal pre_out = null;
BigDecimal pre_out1 = null;
String master = null;
// invnum=invnum+invoicedetails.getINVNUMB().trim();
loanRef = loandetails.getDEALREF().trim();
Amount = loandetails.getREAMOUNT();

ccy = loandetails.getREAMOUNTCurrency();
System.out.println("Dealreference-------------> "
  + loanRef);
System.out.println("Loan Amount-------------> "
  + Amount);
System.out
  .println("Loan Amount currency-------------> "
              + ccy);
con = getConnection();
String checkOut = "select AMT_O_S from master,C8PF c8"
  + " where C8.C8CCY= MASTER.CCY AND master_ref='"
  + loanRef + "' and refno_pfix<>'NEW'";
// System.out.println("Loan Amount outstanding query-------------> "+checkOut);
ps1 = con.prepareStatement(checkOut);
rs1 = ps1.executeQuery();
if (rs1.next()) {
pre_out = rs1.getBigDecimal(1);
// System.out.println("Amount in query "+pre_out);
}
int res = 0;
res = pre_out.compareTo(Amount);
if (res == -1) {
validationDetails
        .addError(ErrorType.Other,
                    "Preshipment knock of amount is greater than the outstanding amount[CM]");
}
String checkOut1 = "SELECT ETT.LOAN_REF, SUM(ETT.REPAYAMT) AS OUT_AMT FROM ETT_PRESHIPMENT_APISERVER ETT ,MASTER MAS,"
  + "  BASEEVENT BEV, (SELECT BEV.KEY97 AS BEV_KEY FROM UBZONE.MASTER MAS, BASEEVENT BEV, EVENTSTEP EVS, ORCH_MAP ORM WHERE MAS.KEY97   = BEV.MASTER_KEY"
  + " AND BEV.KEY97     = EVS.EVENT_KEY AND EVS.ORCH_MAP  = ORM.KEY97 AND ORM.ORCH_STEP = 16 AND BEV.STATUS   <>'c')fp WHERE TRIM(ETT.MASREF)=TRIM(MAS.MASTER_REF) AND TRIM(ETT.EVENTREF)=TRIM(BEV.REFNO_PFIX)||LPAD(BEV.REFNO_SERL,3,0) AND MAS.KEY97  =BEV.MASTER_KEY"
  + " AND bev.key97   = fp.bev_key(+) AND BEV.STATUS ='i' AND fp.bev_key   IS NULL  AND ETT.LOAN_REF= '"
  + loanRef + "' GROUP BY ETT.LOAN_REF";

ps = con.prepareStatement(checkOut1);
rs = ps.executeQuery();
if (rs.next()) {
pre_out1 = rs.getBigDecimal(2);
// System.out.println("Amount in query11 "+pre_out1);
}
int res1 = 0;
res1 = pre_out.compareTo(pre_out1);
if (res1 == -1) {
String query = "SELECT ETT.MASREF FROM UBZONE.ETT_PRESHIPMENT_APISERVER ETT ,UBZONE.MASTER MAS,UBZONE.BASEEVENT BEV, (SELECT BEV.KEY97 AS BEV_KEY FROM UBZONE.MASTER MAS, BASEEVENT BEV, EVENTSTEP EVS, ORCH_MAP ORM WHERE MAS.KEY97   = BEV.MASTER_KEY"
        + " AND BEV.KEY97     = EVS.EVENT_KEY AND EVS.ORCH_MAP  = ORM.KEY97 AND ORM.ORCH_STEP = 16 AND BEV.STATUS   <>'c')fp"
        + " WHERE TRIM(ETT.MASREF)=TRIM(MAS.MASTER_REF)"
        + " AND TRIM(ETT.EVENTREF)=TRIM(BEV.REFNO_PFIX)||LPAD(BEV.REFNO_SERL,3,0) "
        + " AND MAS.KEY97=BEV.MASTER_KEY AND bev.key97   = fp.bev_key(+) AND BEV.STATUS ='i' AND fp.bev_key   IS NULL  AND ETT.LOAN_REF= '"
        + loanRef
        + "' and ETT.MASREF!='"
        + masterref + "'";

ps2 = con.prepareStatement(query);
rs2 = ps2.executeQuery();
while (rs2.next()) {
  if (master == null || master.isEmpty())
        master = rs2.getString("MASREF");
  else
        master = master + " "
                    + rs2.getString("MASREF");
}
validationDetails
        .addError(
                    ErrorType.Other,
                    "Pre shipment knocked off reference number already fetched in"
                                + master
                                + ". Kindly check the outstanding amount [CM]");
} else {
System.out
        .println("Amount less than outstanding failure");
}
}

} catch (Exception e) {
e.printStackTrace();

} finally {
try {
if (rs != null)
rs.close();
if (ps2 != null)
ps2.close();
if (rs2 != null)
rs2.close();
if (ps1 != null)
ps1.close();
if (con != null)
con.close();
} catch (SQLException e) {
e.printStackTrace();
}
}
}

// RELEASE BANK CHECK BOX-RELBNKLM
if (getMajorCode().equalsIgnoreCase("FRN")
&& getMinorCode().equalsIgnoreCase("POF")) {
try {
String payAction = getDriverWrapper().getEventFieldAsText(
"PYAD", "s", "");
if (payAction.equalsIgnoreCase("AMD")) {
getPane().getCtlRELBNKLM().setEnabled(true);
} else {
getPane().getCtlRELBNKLM().setEnabled(false);
}
} catch (Exception e) {
System.out
.println("Exception on payment action based scenario"
        + e.getMessage());
}
}

// FORCE DEBIT-FORCDEBT
/*
* String step_Id = getDriverWrapper() .getEventFieldAsText("CSID",
* "s", "").trim();
*/
if ((step_Id.equalsIgnoreCase("CBS Maker 1")
|| step_csm.equalsIgnoreCase("CBS Reject") || step_csm
.equalsIgnoreCase("CBS Authoriser"))
&& ((getMajorCode().equalsIgnoreCase("FRN") && (getMinorCode()
.equalsIgnoreCase("FNF") || getMinorCode()
.equalsIgnoreCase("POF"))))) {
// String forEvent = getFORCEDEBIT();
String forFin = getFORCEDEBITFIN();

String TransForceDebit = getDriverWrapper()
.getEventFieldAsText("cAHW", "l", "").trim();
if (TransForceDebit.equalsIgnoreCase("N")
&& forFin.equalsIgnoreCase("Y")) {
if (getMinorCode().equalsIgnoreCase("FNF"))
validationDetails
  .addError(ErrorType.Other,
              "Force Debit Flag should be ticked in Freely Negotiable LC [CM]");
else if (getMinorCode().equalsIgnoreCase("POF"))
validationDetails
  .addError(ErrorType.Other,
              "Force Debit Flag should be ticked in Outstanding Presentation [CM]");

}

}

// TENORDET
String finace_event = getDriverWrapper().getEventFieldAsText("BFC",
"l", "");
if (getMinorCode().equalsIgnoreCase("FNF")) {
if (finace_event.equalsIgnoreCase("Y")) {
getPane().setTENORDET("FNR");
getWrapper().setTENORDET("FNR");
} else {
getPane().setTENORDET("NO");
getWrapper().setTENORDET("NO");
}
} else if (getMinorCode().equalsIgnoreCase("POF")) {

if (finace_event.equalsIgnoreCase("Y")) {
getPane().setTENORDET("POD");
getWrapper().setTENORDET("POD");
}
/*
* EventPane pane1 = (EventPane) getPane();
* pane1.getBtnDELETEELCSETTclay().setEnabled(false);
*/

}

// RBI PURPOSE CODE

try {
String rbipur = getWrapper().getOPURPOS_Name().trim();
if (rbipur.equalsIgnoreCase("") || rbipur.length() > 0) {
String str = rbipur.substring(0, 1);
if (((!str.equalsIgnoreCase("S")) || str
.equalsIgnoreCase("P"))
&& (step_Input.equalsIgnoreCase("i"))
&& (step_Id.equalsIgnoreCase("CBS Maker"))
&& (getMajorCode().equalsIgnoreCase("ILC") || (getMajorCode()
        .equalsIgnoreCase("IDC") && getMinorCode()
        .equalsIgnoreCase("CLP")))) {
validationDetails.addError(ErrorType.Other,
  "RBI purpose code should start with S  [CM]");

} else {
// System.out.println("Purpose code is P----> " + str);
}
}
} catch (Exception e) {
if (dailyval_Log.equalsIgnoreCase("YES")) {
System.out
.println("Exception in Purpose code start with S---->"
        + e.getMessage());

}
}

// OWNLC

try {

String kotak_no = getDriverWrapper().getEventFieldAsText("ISS",
"p", "cu").trim();
// System.out.println("kotak Ownlc tick--------->" + kotak_no);

if (getMajorCode().equalsIgnoreCase("FRN")) {
if ((kotak_no.equalsIgnoreCase("INKKBK") || kotak_no.trim()
.equalsIgnoreCase("11908474"))) {
getPane().setOWNLC(true);
} else {
getPane().setOWNLC(false);
/*
* if (dailyval_Log.equalsIgnoreCase("YES")) {
* System.out.println("kotak Ownlc tick else------->" +
* kotak_no); }
*/
}
} else {
getPane().setOWNLC(false);

}
} catch (Exception e) {
// System.out.println("Exception kotak systemOutput--------->" +
// e.getMessage());
}

String subprod = getDriverWrapper().getEventFieldAsText("PTP", "s",
"").trim();
String payactcode = getDriverWrapper().getEventFieldAsText("PYAD",
"s", "").trim();
String ownlc = getDriverWrapper().getEventFieldAsText("cBKR", "l",
"").trim();
System.out.println("ownlc" + ownlc);
System.out.println("payactcode" + payactcode);
System.out.println("subprod" + subprod);
if ((step_csm.equalsIgnoreCase("CBS Maker 1")
|| step_csm.equalsIgnoreCase("CBS Authoriser") || step_csm
.equalsIgnoreCase("CBS Reject"))
&& getMajorCode().equalsIgnoreCase("FRN")
&& getMinorCode().equalsIgnoreCase("POF")
&& subprod.equalsIgnoreCase("NLD")
&& !payactcode.equalsIgnoreCase("AMD")
&& ownlc.equalsIgnoreCase("N")) {
int match = 0;
System.out.println("Entered POF");
match = getLimitMatch();
if (match == 1)
validationDetails.addError(ErrorType.Other,
"Limit blocking cannot be changed [CM]");
}
try {
int cnt = 0;

cnt = getRepayProb();
// System.out.println("Repayment in subsidery" +cnt );
if (cnt == 1) {
validationDetails
.addError(ErrorType.Other,
        "Finance repay amount should be equal to payment amount[CM]");
// System.out.println("Repayment in subsidery" +cnt );
}
} catch (Exception ee) {
// System.out.println("Repayment in subsidery" +
// ee.getMessage());

}

// Actual Penal Rate-PENALRAT

if (getMajorCode().equalsIgnoreCase("FRN")) {
try {

getactualPenalRate();

} catch (Exception e) {
// System.out.println("Exception update" +
// e.getMessage());
}
}

if (getMajorCode().equalsIgnoreCase("FRN")
&& getMinorCode().equalsIgnoreCase("FNF")
|| getMinorCode().equalsIgnoreCase("POF")) {
// PostingCustom post = null;
// if(step_csm.equalsIgnoreCase("CSM Maker 1"))
// String strPSID =
// getDriverWrapper().getPostingFieldAsText("PSID",
// "").trim();
try {
// String relevnt=null;
String Mast = getDriverWrapper().getEventFieldAsText(
"MMST", "r", "").trim();
String Evnt = getDriverWrapper().getEventFieldAsText("EVR",
"r", "").trim();

// System.out.println("MasterReferenceNNum on
// post----------->" + Mast);
// System.out.println("their Reference on
// post----------->" + Evnt);

/*
* if (dailyval_Log.equalsIgnoreCase("YES")) {
*
* System.out.println("MasterReference-------->" + Mast);
* System.out.println("their Reference-------->" + Evnt); //
* System.out.println("PSID----------->" + strPSID); }
*/
con = getConnection();

String query = "WITH MAXVAL AS (SELECT row_number() over (ORDER BY BEV.KEY97 asc) as ROWN,"
+ "MAS.MASTER_REF,MAS.KEY97 mas_key,"
+ "BEV.KEY97 bev_key,BEV.REFNO_PFIX || lpad(BEV.REFNO_SERL,3,0) bev_ref ,LIMBLK FROM"
+ " MASTER MAS,BASEEVENT  BEV ,EXTEVENT  EXT WHERE MAS.KEY97=BEV.MASTER_KEY AND BEV.KEY97=EXT.EVENT"
+ " and BEV.status<>'a' and trim(LIMBLK) is not null AND MAS.MASTER_REF='"
+ Mast
+ "'  ORDER BY BEV.KEY97)"
+ " SELECT LIMBLK FROM MAXVAL "
+ " WHERE ROWN IN (SELECT CASE WHEN '"
+ Evnt
+ "' lIKE 'DPR%' THEN ROWN ELSE ROWN-1 END  "
+ " FROM MAXVAL WHERE bev_ref='" + Evnt + "')";
/*
* String closureQuery =
* "SELECT MAS.MASTER_REF AS PREVIOUS_MASTER, BEV.REFNO_PFIX|| LPAD(BEV.REFNO_SERL,3,0) AS PREVIOUS_EVENT"
* +
* " FROM MASTER MAS,BASEEVENT BEV, BASEEVENT BEV1, LCPAYMENT LCP WHERE MAS.KEY97    = BEV.MASTER_KEY"
* +
* "  AND BEV.KEY97      = LCP.KEY97      AND LCP.NEXTOUT_EV = BEV1.KEY97     AND BEV1.REFNO_PFIX || LPAD(BEV1.REFNO_SERL,3,0)= '"
* +Evnt+"'" +" AND MAS.MASTER_REF            = '" + Mast +
* "'" ;
*
*
* pst = con.prepareStatement(closureQuery); rs =
* pst.executeQuery(); while (rs.next()) {
* relevnt=rs.getString("PREVIOUS_EVENT"); }
* if(relevnt!=null||(!relevnt.equalsIgnoreCase(""))) {
* String query =
* "SELECT row_number() over (ORDER BY BEV.KEY97 ASC) AS ROWN, MAS.MASTER_REF, MAS.KEY97 mas_key,"
* +
* " BEV.KEY97 bev_key, BEV.REFNO_PFIX||lpad(BEV.REFNO_SERL,3,0) bev_ref,LIMBLK FROM MASTER MAS, BASEEVENT BEV,"
* +
* " EXTEVENT EXT WHERE MAS.KEY97   =BEV.MASTER_KEY AND BEV.KEY97     =EXT.EVENT AND BEV.status   <>'a' AND trim(LIMBLK) IS NOT NULL"
* +" AND MAS.MASTER_REF='" + Mast +
* "' and trim(BEV.REFNO_PFIX || lpad(BEV.REFNO_SERL,3,0))='"
* +relevnt+"' ORDER BY BEV.KEY97";
*/
/*
* if (dailyval_Log.equalsIgnoreCase("YES")) {
* System.out.println
* ("Query Result for Previous limit blocking-> " + query);
*
* }
*/

// System.out.println("Query Result for Previous limit
// blocking ->" + query);

ps1 = con.prepareStatement(query);

rs1 = ps1.executeQuery();
while (rs1.next()) {

/*
* if (dailyval_Log.equalsIgnoreCase("YES")) { //
* System.out.println("Entering While // Loop=======>");
*
* }
*/
// System.out.println("Entering While
// Loop========>");

String prelimit = rs1.getString(1);
System.out.println("Prelimit=======>" + prelimit);

getPane().setPRVLIMBL(prelimit);
}
System.out.println("Previous limit"
+ getPane().getPRVLIMBL());
// }
} catch (Exception e) {
e.printStackTrace();
// System.out.println("Exception Previous limit blocking" +
// e.getMessage());

} finally {
try {
if (rs1 != null)
rs1.close();
if (ps1 != null)
ps1.close();
if (con != null)
con.close();
} catch (SQLException e) {
// System.out.println("Connection Failed! Check
// output
// console");
e.printStackTrace();
}
}

}

try {

// //System.out.println("Margin validation part Entered");
// boolean isMarginAvailableForCIF =
// checkAvailblityOfMarginForCIF(con);

String facilityId = getDriverWrapper().getEventFieldAsText(
"cBEC", "s", "");

String margin = getWrapper().getMARAMT();
if (margin == null) {
margin = "0";
}
// //System.out.println("Current margin amount " + margin);
double marDob = Double.valueOf(margin);
// calculateMargin();
List<ExtEventLienMarking> ExtEventLienMark = (List<ExtEventLienMarking>) getWrapper()
.getExtEventLienMarking();
// //System.out.println("ExtEventLienMark ILC------> " +
// ExtEventLienMark.size());
double totalMargin = 0.0;
String dopNo = "";
int counterVal = 0;
ArrayList<Double> arr = new ArrayList<Double>();
for (int l = 0; l < ExtEventLienMark.size(); l++) {
ExtEventLienMarking eventLien = (ExtEventLienMarking) ExtEventLienMark
.get(l);
// BigDecimal clearBal = eventLien.getCLEARBLC();
BigDecimal marginAmt = eventLien.getMARGAMT();
String liensta = eventLien.getLINEST();
dopNo = eventLien.getDEPOSTNO().trim();
// System.out.println("Lien status for mark----> " +
// liensta);
// double clearB = 0.0;

if (ExtEventLienMark.size() > 0
&& (liensta.equalsIgnoreCase("") || liensta == null)
&& (step_Input.equalsIgnoreCase("i"))
&& (step_Id.equalsIgnoreCase("CBS Maker"))) {
validationDetails
  .addError(ErrorType.Other,
              "Lien amount is available please Mark the lien [CM]");
}

double marginDob = eventLien.getMARGAMT().doubleValue();
if ((ExtEventLienMark.size() > 0)
&& (marginDob == 0.0 || marginDob == 0 || marginDob < 1)
&& (step_Input.equalsIgnoreCase("i"))
&& (step_Id.equalsIgnoreCase("CBS Maker"))) {
validationDetails.addError(ErrorType.Other,
  "Lien amount should be greater than Zero [CM]");
pane.getExtEventLienMarkingUpdate().setEnabled(true);
}

if (marginAmt != null)
marginB = Double.valueOf(marginAmt.doubleValue() / 100);

totalMargin += marginB;
if (ExtEventLienMark.size() > 0
&& totalMargin > marDob
&& (step_Input.equalsIgnoreCase("i"))
&& (step_Id.equalsIgnoreCase("CBS Maker") || step_Id
        .equalsIgnoreCase("CBS Maker 1"))) {
// //System.out.println("totalMargin -----> " +
// totalMargin);
validationDetails
  .addWarning(WarningType.Other,
              "Grid Margin amount should not greater then the Margin Amount  [CM]");
} else {
// // System.out.println("totalMargin is less then the
// // margin amount ");
}

// todo for jira 7045

System.out.println("Lien status check===> " + liensta);

if (liensta.equalsIgnoreCase("MARK FAILED")) {
System.out.println("inside lien mark failed error");
validationDetails.addError(ErrorType.Other,
  "Lien Marking Failed[CM]");
} else {
System.out.println("else lien mark failed error");
}

// end of 7045

if (liensta.equalsIgnoreCase("MARK SUCCEEDED")) {
arr.add(marginB);
// // System.out.println("lien status array list
// // arr---->" + arr);

} else {
// // System.out.println("lien status in else loop---->"
// // + liensta);
}
}

if (step_Id.equalsIgnoreCase("CBS Maker 1")
&& ExtEventLienMark.size() > 0) {
try {

String query = "SELECT count(*),lmg.DEPOSTNO,lmg.LINEST FROM MASTER mas, BASEEVENT bas, EXTEVENTLMG lmg WHERE mas.KEY97 =bas.MASTER_KEY AND bas.EXTFIELD =lmg.FK_EVENT AND lmg.DEPOSTNO='"
  + dopNo
  + "' AND lmg.LINEST ='MARK SUCCEEDED' AND mas.MASTER_REF ='"
  + MasterReference
  + "' AND bas.REFNO_PFIX ='"
  + evnt
  + "' AND bas.REFNO_SERL="
  + evvcount
  + " group by lmg.DEPOSTNO,lmg.LINEST";
if (dailyval_Log.equalsIgnoreCase("YES")) {
System.out
        .println("lien status check---->" + query);
}
con = getConnection();
ps1 = con.prepareStatement(query);
rs1 = ps1.executeQuery();
while (rs1.next()) {
counterVal = rs1.getInt(1);
if (dailyval_Log.equalsIgnoreCase("YES")) {
  System.out
              .println("lien counterVal in while---->"
                          + counterVal);
}

}
} catch (Exception e) {
if (dailyval_Log.equalsIgnoreCase("YES")) {
System.out.println("Lien account no duplication"
        + e.getMessage());
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
e.printStackTrace();
}
}

if (ExtEventLienMark.size() > 0 && counterVal > 1
&& (step_Id.equalsIgnoreCase("CBS Maker 1"))) {

if (dailyval_Log.equalsIgnoreCase("YES")) {
System.out
        .println("Multiple liens cannot be marked for same account number---->"
                    + dopNo);
}

validationDetails
  .addError(
              ErrorType.Other,
              "Multiple liens cannot be marked for same account number ("
                          + dopNo
                          + "). Reverse lien for account number and mark new lien [CM]");
}

else if (step_Id.equalsIgnoreCase("CBS Maker 1")
&& ExtEventLienMark.size() > 0) {
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
  BigDecimal margin_Amount = margin_Amt
              .divide(hundred);
  DecimalFormat diff = new DecimalFormat("0.00");
  diff.setMaximumFractionDigits(2);
  String marginAmount = diff
              .format(margin_Amount);
  String marginAcc = fdwrapper.getDEPOSTNO()
              .trim();
  String LienID = fdwrapper.getLIENID().trim();
  String masRefNo = getDriverWrapper()
              .getEventFieldAsText("MST", "r", "");
  String eventRefNo = getDriverWrapper()
              .getEventFieldAsText("MEVR", "r", "");
  String issueDate = getDriverWrapper()
              .getEventFieldAsText("ISS", "d", "");
  String mas_evnt = masRefNo;
  //added user id in tag
  String user1 = getDriverWrapper().getEventFieldAsText("USER", "s", "");
  String user="";
  if(user1!=null && !user1.isEmpty()){
         user=user1;
  }

  String behalfBranch = getDriverWrapper()
              .getEventFieldAsText("BOB", "s", "");
  String mas_evntRef = masRefNo;
  String lienbox = fdwrapper.getLINEST();
  if (!lienbox.equalsIgnoreCase("")
              && lienbox != null
              && lienbox
                          .equalsIgnoreCase("MARK SUCCEEDED")) {
        // System.out.println("Lien Status is
        // blank");
        String fdDefaultXml = "<FDRow> <MasterReference>Maseter_ref</MasterReference> <EventReference>Event_ref</EventReference> <BehalfOfBranch>behalf_Branch</BehalfOfBranch><AccountNumber>Account_Number</AccountNumber><LienId>Lien_ID</LienId><Amount>Amount_Margin</Amount> <Currency>INR</Currency> <ReasonCode>TRD</ReasonCode> <Remarks>Maseter_ref</Remarks> <UserId>user</UserId> </FDRow>";
        fdDefaultXml = fdDefaultXml.replace(
                    "Maseter_ref", masRefNo);
        fdDefaultXml = fdDefaultXml.replace(
                    "Event_ref", eventRefNo);
        fdDefaultXml = fdDefaultXml.replace(
                    "behalf_Branch", behalfBranch);
        fdDefaultXml = fdDefaultXml.replace(
                    "Account_Number", marginAcc);
        fdDefaultXml = fdDefaultXml.replace(
                    "Lien_ID", LienID);
        fdDefaultXml = fdDefaultXml.replace(
                    "Amount_Margin", marginAmount);
        fdDefaultXml = fdDefaultXml.replace(
                    "Maseter_ref", masRefNo);
        fdDefaultXml = fdDefaultXml.replace("user", user);

        gridXml = gridXml + fdDefaultXml;
  } else {
        if (dailyval_Log.equalsIgnoreCase("YES")) {
              // System.out.println("Lien Status in else"
              // + lienbox);
        }
  }

}

char j = '"';
String val = Character.toString(j);
headerReq = headerReq.replace("$", val);
String correlationId = randomCorrelationId();
// System.out.println("correlationId " +
// correlationId);
headerReq = headerReq.replace("Correlation_Id",
        correlationId);
String finalXml = headerReq + gridXml + endXml;
if (dailyval_Log.equalsIgnoreCase("YES")) {
  // System.out.println("Themebridge finalXml for enquiry"
  // + finalXml);
}
ThemeTransportClient anThemeTransportClient = new ThemeTransportClient();
String responseXML = anThemeTransportClient.invoke(
        "Customization", "FDLienAdd", finalXml);
if (dailyval_Log.equalsIgnoreCase("YES")) {
  System.out
              .println("Final Themebridge responseXML for enquiry==========>"
                          + responseXML);
}
// EnigmaArray<ExtEventLienMarkingEntityWrapper>
// leanDataList = getExtEventLienMarkingData();
String line = responseXML;
String[] sp_line = line.split(",");
for (int k = 0; k < sp_line.length; k++) {
  String[] res_sp_line = sp_line[k].split("~");
  Iterator<ExtEventLienMarking> iterator1 = ExtEventLien
              .iterator();

  for (ExtEventLienMarking str : ExtEventLien) {
        String depositNum = str.getDEPOSTNO()
                    .trim();

        BigDecimal hundred = new BigDecimal(100);
        BigDecimal margin_Amt = str.getMARGAMT();
        BigDecimal margin_Amount = margin_Amt
                    .divide(hundred);
        DecimalFormat diff = new DecimalFormat(
                    "0.00");
        diff.setMaximumFractionDigits(2);
        String lienAmt = diff.format(margin_Amount);
        {
              // System.out.println("Lien mark Amount"
              // + lienAmt);
        }
        String currentDepositNum = res_sp_line[0]
                    .trim();
        if (dailyval_Log.equalsIgnoreCase("YES")) {
              // System.out.println("Lien response currentDepositNum"
              // + currentDepositNum);
        }
        String responseValue = res_sp_line[3]
                    .trim();
        BigDecimal responseAmount = new BigDecimal(
                    responseValue);
        String responseAmt = diff
                    .format(responseAmount);
        if (dailyval_Log.equalsIgnoreCase("YES")) {
              // System.out.println("Lien response amount"
              // + responseAmt);
        }
        String markVal = res_sp_line[4].trim();
        String markValue = "MARK " + markVal;
        if (dailyval_Log.equalsIgnoreCase("YES")) {
              // System.out.println("Lien mark response Value"
              // + markValue);
        }
        String lienbox = str.getLINEST().trim();
        if (dailyval_Log.equalsIgnoreCase("YES")) {
              // System.out.println("Lien mark grig Value"
              // + lienbox);
        }
        // String lienStr = str.getMARGAMT();
        // double lienAmt = Double.valueOf(lienStr);

        if (depositNum
                    .equalsIgnoreCase(currentDepositNum)
                    && markValue
                                .equalsIgnoreCase("MARK SUCCEEDED")
                    && lienbox
                                .equalsIgnoreCase("MARK SUCCEEDED")) {
              if (!lienAmt
                          .equalsIgnoreCase(responseAmt)) {
                    validationDetails
                                .addError(
                                            ErrorType.Other,
                                            "For Account no ("
                                                        + depositNum
                                                        + "), lien is marked for amount ("
                                                        + responseAmt
                                                        + ") in finacle. Kindly update the lien amount");
              }

              else {
                    if (dailyval_Log
                                .equalsIgnoreCase("YES")) {
                          // System.out.println("lien and response amount is same else"
                          // + lienAmt
                          // + "responseAmt" +
                          // responseAmt);
                    }
              }
        } else {
              if (dailyval_Log
                          .equalsIgnoreCase("YES")) {
                    // System.out.println("lien depositNum else"
                    // + depositNum
                    // + "currentDepositNum===>" +
                    // currentDepositNum);
              }
        }

  }

}

// }
} catch (Exception e) {
if (dailyval_Log.equalsIgnoreCase("YES")) {
  System.out
              .println("ThemeTransportClient Exception response "
                          + e.getMessage());
}
}
} else {

}

}

// validation mark succeed
double dob1 = 0;
for (Double dob : arr) {

dob1 = dob1 + dob;
// System.out.println("marksucceed=======>" + dob);
}

// System.out.println("marksucceed=======>" + dob1);
// System.out.println("arr.size() =======>" + arr.size() );

if (arr.size() > 0
&& (step_Input.equalsIgnoreCase("i"))
&& (step_Id.equalsIgnoreCase("CBS Maker") || step_Id
  .equalsIgnoreCase("CBS Maker 1"))
&& getMinorCode().equalsIgnoreCase("POC")) {
System.out.println("Marginamt_log for -----> " + dob1);
validationDetails.addWarning(WarningType.Other,
"Total Lien Mark amount is (" + dob1
        + " INR), Please Release      the Lien [CM]");
} else {
// // System.out.println("Lien mark amount in else" + dob1);
}

if (marDob > 0
&& ExtEventLienMark.size() == 0
&& (step_Input.equalsIgnoreCase("i"))
&& (step_Id.equalsIgnoreCase("CBS Maker") || step_Id
  .equalsIgnoreCase("CBS Maker 1"))
&& (getMinorCode().equalsIgnoreCase("ISI")
  || getMinorCode().equalsIgnoreCase("NADI") || getMinorCode()
  .equalsIgnoreCase("NAMI"))) {
// //System.out.println("ExtEventLienMark ILC--> " +
// ExtEventLienMark + " & marginAmt ILC ---> " + marDob);
validationDetails
.addWarning(WarningType.Other,
        "Lien amount is calculated and no Lien Amount is entered in FD lien grid  [CM]");
} else {
// // System.out.println(" FD lien grid is entered----->");

}

// //System.out.println("totalMargin - "+totalMargin);
String expectedMarginStr = getDriverWrapper()
.getEventFieldAsText("cAAR", "v", "m").trim();
// System.out.println("expectedMarginStr --------- " +
// expectedMarginStr);
double expectedMarginAmt = 0.0;
if (expectedMarginStr.length() != 0) {
expectedMarginAmt = Double.parseDouble(expectedMarginStr);
// System.out.println("expectedMarginAmt - " +
// expectedMarginAmt);
}
/*
* System.out.println("expectedMarginAmt --------- " +
* expectedMarginAmt);
* System.out.println("totalMargin--------- " + totalMargin);
* System.out.println("ExtEventLienMark.size() --------- " +
* ExtEventLienMark.size());
*/
if ((ExtEventLienMark.size() > 0)
&& (totalMargin != expectedMarginAmt)
&& (step_Input.equalsIgnoreCase("i"))
&& (step_Id.equalsIgnoreCase("CBS Maker") || step_Id
  .equalsIgnoreCase("CBS Maker 1"))) {
validationDetails
.addWarning(WarningType.Other,
        "Sum of margin amount should be equal to Required Lien Amount  [CM]");
}

} catch (Exception e) {
// System.out.println("Exception occured in margin validation -
// " + e.getMessage());
}

// Account realization warning
try {
List<ExtEventAccountRealisation> Acr = (List<ExtEventAccountRealisation>) getWrapper()
.getExtEventAccountRealisation();
if (Acr.size() > 0
&& getDriverWrapper()
  .getEventFieldAsText("EVR", "r", "").trim()
  .substring(0, 2) == "POF"
&& (step_Input.equalsIgnoreCase("i"))
&& (step_Id.equalsIgnoreCase("CBS Maker") || step_Id
  .equalsIgnoreCase("CBS Maker 1"))) {
validationDetails.addWarning(
ValidationDetails.WarningType.Other,
"Disposal instruction exist  [CM]");
}
} catch (Exception e) {
// System.out.println(e.getMessage());
}

try {
con = ConnectionMaster.getConnection();
List<ExtEventShippingTable> shpcol = (List<ExtEventShippingTable>) getWrapper()
.getExtEventShippingTable();
for (int l = 0; l < shpcol.size(); l++) {
ExtEventShippingTable shipCol = shpcol.get(l);
String shipBilNo = shipCol.getBILLNUM();

String formno = shipCol.getFORMNUM();
if (formno.length() > 0) {
try {

String query1 = "SELECT COUNT(*) FROM EXTEVENTSHT WHERE FORMNUM= '"
        + formno + "'";
// //System.out.println("Query1 for shipping bill
// duplicate" + query1);
int count1 = 0;
ps2 = con.prepareStatement(query1);
rs2 = ps2.executeQuery();
while (rs2.next()) {
  // //System.out.println("Entered while");
  count1 = rs2.getInt(1);
  // //System.out.println("value of count in while
  // "
  // + count);
}
if (count1 > 1
        && (step_Input.equalsIgnoreCase("i"))
        && (step_Id.equalsIgnoreCase("CBS Maker") || step_Id
                    .equalsIgnoreCase("CBS Maker 1"))
        && getMinorCode().equalsIgnoreCase("FNF")) {
  // //System.out.println("Count is " + count);
  validationDetails
              .addWarning(WarningType.Other,
                          "Entered Form Number is already Exist  [CM]");
} else {
  // //
  // System.out.println("Form Number is valid");
}

} catch (Exception e1) {
// System.out.println("Exception CHECK===>" +
// e1.getMessage());
}
try {
String query1 = "SELECT COUNT(*) FROM EXTEVENTSHC WHERE CFORMN= '"
        + formno + "'";
// //System.out.println("Query1 for shipping bill
// duplicate" + query1);
int count1 = 0;
ps2 = con.prepareStatement(query1);
rs2 = ps2.executeQuery();
while (rs2.next()) {
  // //System.out.println("Entered while");
  count1 = rs2.getInt(1);
  // //System.out.println("value of count in while
  // "
  // + count);
}
if (count1 > 1
        && (step_Input.equalsIgnoreCase("i"))
        && (step_Id.equalsIgnoreCase("CBS Maker") || step_Id
                    .equalsIgnoreCase("CBS Maker 1"))
        && getMinorCode().equalsIgnoreCase("FNF")) {
  // //System.out.println("Count is " + count);
  validationDetails
              .addWarning(WarningType.Other,
                          "Entered Form Number is already Exist  [CM]");
} else {
  // //
  // System.out.println("Form Number is valid");
}

} catch (Exception e) {
// System.out.println("Exception for Form Number is
// valid" + e);
}
} else {
// // System.out.println("Form Number is empty");
}

}
} catch (Exception e1) {
// System.out.println("Exception for form no is already used" +
// e1.getMessage());
} finally {
try {
if (rs2 != null)
rs2.close();
if (ps2 != null)
ps2.close();
if (con != null)
con.close();

} catch (SQLException e) {
// // System.out.println("Connection Failed! Check output
// // console");
e.printStackTrace();
}
}
// Shipping Details

// Nostro Changes

try {
double nosrtoAmt = 0;
double nosrtoOutAmt = 0;

double nosTotal = 0;
double creditAmt = 0;

double creditAmt940 = 0;
BigDecimal nosrtoAmount = new BigDecimal("0");
BigDecimal nosrtoOutAmount = new BigDecimal("0");

BigDecimal creditAmount = new BigDecimal("0");
BigDecimal creditAmount940 = new BigDecimal("0");
String nostref_MT103102 = getWrapper().getNOSTMT().trim();
String nostref_MT940 = getWrapper().getNOSTRM().trim();

try {
String nosAmt = getDriverWrapper().getEventFieldAsText(
"cABI", "v", "m");
if (!nosAmt.equalsIgnoreCase("") && nosAmt.length() > 0) {
nosrtoAmt = Double.parseDouble(nosAmt);
nosTotal = short_total + nosrtoAmt;
}
nosrtoAmount = new BigDecimal(nosAmt);

if (dailyval_Log.equalsIgnoreCase("YES")) {
// System.out.println(" Nostro utilizaton amount in explc"
// + nosrtoAmount);
}
} catch (Exception e) {
nosrtoAmount = new BigDecimal("0");
}

try {
String credAmt = getDriverWrapper().getEventFieldAsText(
"cANE", "v", "m");
if (!credAmt.equalsIgnoreCase("") && credAmt.length() > 0) {
creditAmt = Double.parseDouble(credAmt);

}
creditAmount = new BigDecimal(credAmt);
if (dailyval_Log.equalsIgnoreCase("YES")) {
// System.out.println(" Nostro credit amount in explc" +
// creditAmount);
}
} catch (Exception e) {
creditAmount = new BigDecimal("0");
}

try {
String credAmt940 = getDriverWrapper().getEventFieldAsText(
"cABN", "v", "m");
if (!credAmt940.equalsIgnoreCase("")
&& credAmt940.length() > 0) {
creditAmt940 = Double.parseDouble(credAmt940);

}

creditAmount940 = new BigDecimal(credAmt940);
if (dailyval_Log.equalsIgnoreCase("YES")) {
// System.out.println(" Nostro credit 940 amount in explc"
// + creditAmount940);
}
} catch (Exception e) {
creditAmount940 = new BigDecimal("0");
}

try {
String nosOutAmt = getDriverWrapper().getEventFieldAsText(
"cBJL", "v", "m");
if (!nosOutAmt.equalsIgnoreCase("")
&& nosOutAmt.length() > 0) {
nosrtoOutAmt = Double.parseDouble(nosOutAmt);
}
nosrtoOutAmount = new BigDecimal(nosOutAmt);

if (dailyval_Log.equalsIgnoreCase("YES")) {
// System.out.println(" Nostro Outstanding amount in inward remittance"
// + nosrtoOutAmount);
}
} catch (Exception e) {
nosrtoOutAmount = new BigDecimal("0");
}

if (nosrtoAmt > 0
&& nosrtoOutAmt > 0
&& (nosrtoOutAmount.compareTo(nosrtoAmount) < 0)
&& getMinorCode().equalsIgnoreCase("POF")
&& (step_csm.equalsIgnoreCase("CBS Maker")
  || step_csm.equalsIgnoreCase("CBS Maker 1") || step_csm
        .equalsIgnoreCase("CBS Authoriser"))) {
validationDetails
.addError(ErrorType.Other,
        "Notsro utilization amount is greater than nostro Outstanding amount [CM]");
} else {
if (dailyval_Log.equalsIgnoreCase("YES")) {
// System.out.println(" Notsro Outstanding amount is greater"
// + nosrtoOutAmount);
}
}

String advrec = getWrapper().getPERADV();

if (nosrtoAmt > 0) {

if (advrec.equalsIgnoreCase("")
&& nosrtoAmt > 0
&& (amountdob != nosTotal)
&& (step_Input.equalsIgnoreCase("i"))
&& (step_Id.equalsIgnoreCase("CBS Maker") || step_Id
        .equalsIgnoreCase("CBS Maker 1"))
&& getMinorCode().equalsIgnoreCase("POF")
&& !formtyp.equalsIgnoreCase("EXEMPTED")
&& !mixedpay.equalsIgnoreCase("Y")
&& (payaction.equalsIgnoreCase("Pay"))) {
validationDetails
  .addWarning(
              WarningType.Other,
              "Sum of short collection amount(Other bank charges) + Nostro amount should be equal to payment amount (Payment details) [CM]");
}

else {

}

if (mixedpay.equalsIgnoreCase("Y")
&& (payaction.equalsIgnoreCase("Pay"))) {
if (dailyval_Log.equalsIgnoreCase("YES")) {
/*
* System.out.println(
* "Sum of short collection amount(Other bank charges) + Nostro amount===>"
* + nosTotal + "Mixed paymen===>" + mixpay_tot);
*/
}
if (advrec.equalsIgnoreCase("") && nosrtoAmt > 0
  && (nosTotal != mixpay_tot)
  && (step_Input.equalsIgnoreCase("i"))
  && (step_Id.equalsIgnoreCase("CBS Maker 1"))
  && getMinorCode().equalsIgnoreCase("POF")
  && !formtyp.equalsIgnoreCase("EXEMPTED")) {
validationDetails
        .addWarning(
                    WarningType.Other,
                    "Sum of short collection amount(Other bank charges) + Nostro amount should be equal to payment amount (Payment details) for Mixed payment [CM]");
} else {
if (dailyval_Log.equalsIgnoreCase("YES")) {
  /*
   * System.out.println(
   * "Sum of short collection amount(Other bank charges) + Nostro amount else loop for Mixed payment---->>>"
   * + nosTotal + " " + mixpay_tot);
   */
}
}
} else {
// // System.out.println("Sum of Equivalent
// // Bill Amount and short collection amount
// // should be equal====>>>");
}

}

if (nostref_MT103102.length() > 0
&& getMinorCode().equalsIgnoreCase("POF")) {
if (nosrtoAmt > 0
&& creditAmt > 0
&& (creditAmount.compareTo(nosrtoAmount) < 0)
&& (step_Input.equalsIgnoreCase("i"))
&& (step_csm.equalsIgnoreCase("CBS Maker") || step_csm
        .equalsIgnoreCase("CBS Maker 1"))
&& getMinorCode().equalsIgnoreCase("POF")) {
validationDetails
  .addError(ErrorType.Other,
              "Notsro utilization amount is greater than nostro credit amount [CM]");
}

else if (nosrtoAmt > 0
&& creditAmt > 0
&& (creditAmount.compareTo(nosrtoAmount) > 0)
&& (step_Input.equalsIgnoreCase("i"))
&& (step_csm.equalsIgnoreCase("CBS Maker") || step_csm
        .equalsIgnoreCase("CBS Maker 1"))
&& getMinorCode().equalsIgnoreCase("POF")) {
validationDetails
  .addWarning(WarningType.Other,
              "Notsro utilization amount is less than nostro credit amount [CM]");
} else {
if (dailyval_Log.equalsIgnoreCase("YES")) {
/*
* System.out.println(
* "Nostro creditAmt amount else" + creditAmount +
* "nosrtoAmt===>" + nosrtoAmount);
*/
}
}
} else if (nostref_MT940.length() > 0
&& getMinorCode().equalsIgnoreCase("POF")) {
if (nosrtoAmt > 0
&& creditAmt940 > 0
&& (creditAmount940.compareTo(nosrtoAmount) < 0)
&& (step_Input.equalsIgnoreCase("i"))
&& (step_csm.equalsIgnoreCase("CBS Maker") || step_csm
        .equalsIgnoreCase("CBS Maker 1"))
&& getMinorCode().equalsIgnoreCase("POF")) {
validationDetails
  .addError(ErrorType.Other,
              "Notsro utilization amount is greater than nostro credit amount [CM]");
}

else if (nosrtoAmt > 0
&& creditAmt940 > 0
&& (creditAmount940.compareTo(nosrtoAmount) > 0)
&& (step_Input.equalsIgnoreCase("i"))
&& (step_csm.equalsIgnoreCase("CBS Maker") || step_csm
        .equalsIgnoreCase("CBS Maker 1"))
&& getMinorCode().equalsIgnoreCase("POF")) {
validationDetails
  .addWarning(WarningType.Other,
              "Notsro utilization amount is less than nostro credit amount [CM]");
} else {
if (dailyval_Log.equalsIgnoreCase("YES")) {
/*
* System.out.println(
* "Nostro creditAmt amount else" + creditAmount940
* + "nosrtoAmt===>" + nosrtoAmount);
*/
}
}
}

} catch (Exception ex) {
if (dailyval_Log.equalsIgnoreCase("YES")) {
System.out
.println("Exception short collection amount(Other bank charger) + Nostro amount"
        + ex.getMessage());
}

}

String customera = getDriverWrapper().getEventFieldAsText("DRW",
"p", "no").trim();
if (getMajorCode().equalsIgnoreCase("FRN")
&& (getMinorCode().equalsIgnoreCase("FNF") || getMinorCode()
.equalsIgnoreCase("POF"))) {
// //System.out.println("Advance Table Validation for
// collection--->");
try {
String inwnum = "";
double totalAmt = 0;
String balanceValCurrency = ""; // String
double balance = 0.0;
String creditcur = "";
long creditAmount = 0;
long balanceAmt = 0;
String masReference = getDriverWrapper()
.getEventFieldAsText("MST", "r", "");
// String eventCode =
// getDriverWrapper().getEventFieldAsText("EVR", "r", "");
String cusNo = "";
double balan = 0.0;
double amtutl = 0.0;
String balancur = "";
String amtutlcur = "";
List<ExtEventAdvanceTable> lists = (List<ExtEventAdvanceTable>) getWrapper()
.getExtEventAdvanceTable();
for (int a = 0; a < lists.size(); a++) {
String master = null;
ExtEventAdvanceTable adve = lists.get(a);
cusNo = adve.getCUSCIFNO().trim();
balan = adve.getBALANCE().doubleValue();
amtutl = adve.getAMTUTIL().doubleValue();
balancur = adve.getBALANCECurrency();
amtutlcur = adve.getAMTUTILCurrency();
inwnum = adve.getINWARD().trim();
// //System.out.println("Advance Table Validation in
// customer in odc--->" + cusNo + "" + customera);

System.out.println("balance  AMount " + balan);
/*
* if ((!cusNo.equalsIgnoreCase(customera)) &&
* (step_Input.equalsIgnoreCase("i")) &&
* (step_csm.equalsIgnoreCase("CBS Maker") ||
* step_csm.equalsIgnoreCase("CBS Maker 1"))) {
* validationDetails.addWarning(WarningType.Other,
* "Remittance Customer is not same as the Drawer in advance table [CM]"
* );
*
* }
*/
if (amtutl > balan
  && (step_Input.equalsIgnoreCase("i"))
  && (step_csm.equalsIgnoreCase("CBS Maker") || step_csm
              .equalsIgnoreCase("CBS Maker 1"))) {
validationDetails
        .addError(
                    ErrorType.Other,
                    "Utillization of amount should not greater than available amount in advance table [CM]");
}
/*
* if (!balancur.equalsIgnoreCase(amtutlcur) &&
* (step_Input.equalsIgnoreCase("i")) &&
* (step_csm.equalsIgnoreCase("CBS Maker") ||
* step_csm.equalsIgnoreCase("CBS Maker 1"))) {
* validationDetails.addWarning(WarningType.Other,
* "Utillization of amount currency should equal to available amount currency in advance table [CM]"
* ); }
*/

if (!inwnum.equalsIgnoreCase("")) {

String inwardDetails = "SELECT TRIM(ORDCUS_CST), trim(TO_CHAR(value_date,'DD/MM/YY')), trim(REMITTER_COUNTRY), CREDIT_AMOUNT, REC_CCY, trim(CIF_NO),TRIM(BANK_ADCODE) FROM ETTV_INWARD_REMITTANCE_AMOUNT WHERE trim(MAS_REF)='"
        + inwnum + "'";
System.out
        .println("query for getting all fields in inward Remittance grid22 "
                    + inwardDetails);
con = getConnection();
pst = con.prepareStatement(inwardDetails);

rs1 = pst.executeQuery();
if (rs1.next()) {

  creditAmount = rs1.getLong(4);
  creditcur = rs1.getString(5);

  // System.out.println("Credit AMount22 " +
  // creditAmount);

  String BalAmtQuery = "SELECT SUM(NVL(INWARDAMT,0)) AS INWARDAMOUNT FROM (SELECT SUM(NVL(ext.amtutil,0)) AS INWARDAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, exteventadv ext WHERE mas.KEY97 =bas.MASTER_KEY AND ext.FK_EVENT = bas.EXTFIELD AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF ='"
              + masReference
              + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) !='"
              + eventCode
              + "') AND ext.INWARD ='"
              + inwnum
              + "' GROUP BY mas.MASTER_REF UNION ALL SELECT SUM(NVL(ext.amtutil,0)) AS INWARDAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, exteventadv ext WHERE mas.KEY97 =bas.MASTER_KEY AND ext.FK_EVENT = bas.EXTFIELD AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF !='"
              + masReference
              + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) !='"
              + eventCode
              + "') AND ext.INWARD ='"
              + inwnum
              + "' GROUP BY mas.MASTER_REF UNION ALL SELECT SUM(NVL(ext.amtutil,0)) AS INWARDAMT, mas.MASTER_REF FROM master mas, BASEEVENT bas, exteventadv ext WHERE mas.KEY97 =bas.MASTER_KEY AND ext.FK_EVENT = bas.EXTFIELD AND bas.STATUS IN ('i','c') AND (mas.MASTER_REF !='"
              + masReference
              + "' AND trim(bas.REFNO_PFIX ||LPAD(bas.REFNO_SERL,3,0)) ='"
              + eventCode
              + "') AND ext.INWARD ='"
              + inwnum
              + "' GROUP BY mas.MASTER_REF ) INWARDAMOUNT";

  // System.out.println("Query for getting Inward Utilized Amount===>"
  // + BalAmtQuery);

  pst = con.prepareStatement(BalAmtQuery);
  rs1 = pst.executeQuery();
  if (rs1.next()) {

        totalAmt = rs1.getDouble(1);

        balanceValCurrency = creditcur;

        double irmAmt = 0;
        String closureQuery = "SELECT SUM(IR.AMOUNT)*POWER(10,C8.C8CED) AS IRMAMT FROM ETT_IRM_CLOSURE_TBL IR,C8PF C8 "
                    + " WHERE IR.MASTER_REFNO = '"
                    + inwnum
                    + "' AND "
                    + " IR.CURRENCY = C8.C8CCY AND IR.STATUS IN ('TP','A') GROUP BY C8.C8CED ";

        if (dailyval_Log.equalsIgnoreCase("YES")) {
              System.out
                          .println("IRM Clourse Query ---"
                                      + closureQuery);
        }
        pst = con.prepareStatement(closureQuery);
        rs1 = pst.executeQuery();
        if (rs1.next()) {
              irmAmt = rs1.getDouble("IRMAMT");
        } else {
              irmAmt = 0;
        }

        totalAmt = totalAmt + irmAmt;

        balanceAmt = (long) (creditAmount - totalAmt);

        if (dailyval_Log.equalsIgnoreCase("YES")) {
              System.out
                          .println("Balance Credit Amount22-->"
                                      + balanceAmt);
        }
        // fdwarapper1.setCUSCIFNO(cif_no);
        if (balanceAmt > 0) {

              balance = Double.valueOf(balanceAmt);
              System.out
                          .println("Balance Credit Amount221-->"
                                      + balance);

        } else {
              balance = 0.0;
        }
  } else {

        double irmAmt = 0;
        String closureQuery = "SELECT SUM(IR.AMOUNT)*POWER(10,C8.C8CED) AS IRMAMT FROM ETT_IRM_CLOSURE_TBL IR,C8PF C8 "
                    + " WHERE IR.MASTER_REFNO = '"
                    + inwnum
                    + "' AND "
                    + " IR.CURRENCY = C8.C8CCY AND IR.STATUS IN ('TP','A') GROUP BY C8.C8CED ";

        if (dailyval_Log.equalsIgnoreCase("YES")) {
              // System.out.println("IRM Clourse Query --->"
              // + closureQuery);
        }
        pst = con.prepareStatement(closureQuery);
        rs1 = pst.executeQuery();
        if (rs1.next()) {
              irmAmt = rs1.getDouble("IRMAMT");
        } else {
              irmAmt = 0;
        }

        long balan_cret = (long) (creditAmount - irmAmt);

        if (dailyval_Log.equalsIgnoreCase("YES")) {
              System.out
                          .println("Balance Credit Amount22-->"
                                      + balan_cret);
        }

        if (balan_cret > 0) {

              balance = Double.valueOf(balan_cret);

        } else {
              balance = 0.0;
        }

  }

  BigDecimal divideByBig1 = new BigDecimal(
              balance);
  BigDecimal divideByBig2 = new BigDecimal(amtutl);
  int res1 = 0;
  res1 = divideByBig1.compareTo(divideByBig2);

  System.out
              .println("Balance Credit Amount221-->"
                          + balance + "bal" + amtutl);
  if ((res1 == -1)
              && (step_csm
                          .equalsIgnoreCase("CBS Authoriser") || step_csm
                          .equalsIgnoreCase("CBS Maker 1"))) {
        System.out
                    .println("Amount less than outstanding12");

        String query = "SELECT mas.MASTER_REF FROM UBZONE.master mas,UBZONE.BASEEVENT bas,"
                    + " UBZONE.exteventadv ext WHERE mas.KEY97      =bas.MASTER_KEY AND ext.FK_EVENT = bas.EXTFIELD AND bas.STATUS  IN ('i','c')"
                    + " and ext.INWARD='"
                    + inwnum
                    + "' and mas.master_ref!='"
                    + masReference + "'";
        System.out
                    .println("Loan Amount currency query12-------------> "
                                + query);
        ps2 = con.prepareStatement(query);
        rs2 = ps2.executeQuery();
        while (rs2.next()) {
              /*
               * String
               * master=rs2.getString("MASREF");
               * mst=mst + " " +master;
               */

              if (master == null || master.isEmpty())
                    master = rs2
                                .getString("MASTER_REF");
              else
                    master = master
                                + " "
                                + rs2.getString("MASTER_REF");
        }

        System.out
                    .println("Balance Credit Amount221-->"
                                + balance);
        validationDetails
                    .addError(
                                ErrorType.Other,
                                "Available amount for XAR "
                                            + inwnum
                                            + " is used in "
                                            + master
                                            + " so available amount exceeds please check  [CM]");
  }
}

}

}
} catch (Exception e) {
// System.out.println("Exception is Advance Table
// Validation
// in customer" + e.getMessage());
}
}

try {
String HScode = getDriverWrapper().getEventFieldAsText("cACO",
"s", "");
/*
* if (dailyval_Log.equalsIgnoreCase("YES")) {
*
* //System.out.println("HS Code" + HScode + "HS Code lengh" +
* HScode.length()); }
*/
if ((!HScode.equalsIgnoreCase("") && HScode != null)
&& (step_Input.equalsIgnoreCase("i"))
&& (step_Id.equalsIgnoreCase("CBS Maker") || step_Id
  .equalsIgnoreCase("CBS Maker 1"))
&& HScode.length() > 1) {
String qr = "select count(*) from exthmcode where hcodee='"
+ HScode + "'";
// //System.out.println("Query " + qr);
con = ConnectionMaster.getConnection();
ps = con.prepareStatement(qr);
// //System.out.println(qr);
rs = ps.executeQuery();
while (rs.next()) {
String k = rs.getString(1);
// //System.out.println("while of count of hscode " +k);
int ka = Integer.parseInt(k);
if (ka == 0 && HScode.length() > 1) {
// //System.out.println("warning of hs code ");
validationDetails.addWarning(
        ValidationDetails.WarningType.Other,
        "HS Code is Invalid (" + HScode + ")"
                    + "[CM]");

}
}

} else {
// System.out.println("HS code");
}
} catch (Exception e) {
// //System.out.println("exception caught " +e.getMessage() );
} finally {
try {
if (rs != null)
rs.close();
if (ps != null)
ps.close();
if (con != null)
con.close();
} catch (SQLException e) {
// System.out.println("Connection Failed! Check output
// console");
e.printStackTrace();
}
}

if (step_Id.equalsIgnoreCase("CBS Authoriser")
|| step_Id.equalsIgnoreCase("CBS Maker 1")) {
try {
con = getConnection();
String query = "select wm.MESSAGE from master mas, BASEEVENT bev, warnmsg wm where mas.key97=BEV.MASTER_KEY and bev.key97=wm.associ30 and WM.PARTCODE = 'MIXFX' and MAS.MASTER_REF = '"
+ MasterReference
+ "' and BEV.REFNO_PFIX= '"
+ evnt
+ "' and BEV.REFNO_SERL = '"
+ evvcount
+ "'";

/*
* if (dailyval_Log.equalsIgnoreCase("YES")) {
*
* //System.out.println("FX deal message in authorization "
* + query); }
*/
String fxDeal = "";
ps1 = con.prepareStatement(query);
rs1 = ps1.executeQuery();
while (rs1.next()) {
// //System.out.println("Entered while");
String fxAmount = rs1.getString(1);
// System.out.println("FX deal message===>" + fxAmount);
fxDeal = fxAmount.substring(4, 6);
// System.out.println("FX deal message converted===>" +
// fxDeal);

/*
* if (dailyval_Log.equalsIgnoreCase("YES")) {
*
* // System.out.println("FX deal message converted===>"
* + fxDeal); }
*/
}

if (fxDeal.equalsIgnoreCase("FX")
&& (step_Id.equalsIgnoreCase("CBS Authoriser") || step_Id
        .equalsIgnoreCase("CBS Maker 1"))) {
// System.out.println("warning message in authorization
// if
// loop" + count3 + " and step_Id" + step_csm);
validationDetails
  .addError(ErrorType.Other,
              "FX deal amount more than transaction amount [CM]");
}

else {
// System.out.println("warning message in authorization
// else" + count3 + " and step_Id" + step_csm);
/*
* if (dailyval_Log.equalsIgnoreCase("YES")) {
*
* //System.out.println(
* "warning message in authorization else" + fxDeal); }
*/
}

} catch (Exception e) {
// System.out.println("Exception" + e1.getMessage());
/*
* if (dailyval_Log.equalsIgnoreCase("YES")) {
*
* //System.out.println("Exception fx Deal warning message"
* + e.getMessage()); }
*/
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
// System.out.println("Connection Failed! Check output
// console");
e.printStackTrace();
}
}
}

try {
con = getConnection();
String query = "select count(*) from master mas, BASEEVENT bev, warnmsg wm where mas.key97=BEV.MASTER_KEY and bev.key97=wm.associ30 and WM.PARTCODE = 'RtTol' and MAS.MASTER_REF = '"
+ MasterReference
+ "' and BEV.REFNO_PFIX= '"
+ evnt
+ "' and BEV.REFNO_SERL = '" + evvcount + "'";
// System.out.println("Query FX deal rate is outside specified
// tolerance " + query);
/*
* if (dailyval_Log.equalsIgnoreCase("YES")) {
*
* //System.out.println(
* "Query FX deal rate is outside specified tolerance " +
* query); }
*/
int count = 0;
ps1 = con.prepareStatement(query);
rs1 = ps1.executeQuery();
while (rs1.next()) {
// //System.out.println("Entered while");
count = rs1.getInt(1);
// //System.out.println("value of count in while
// authorization" + count);
}

if (count > 0
&& (step_Id.equalsIgnoreCase("CBS Authoriser") || step_Id
  .equalsIgnoreCase("CBS Maker 1"))) {
// //System.out.println("warning message in authorization if
// loop" + count + " and step_Id" + step_Id);
validationDetails.addError(ErrorType.Other,
"FX deal rate is outside specified tolerance [CM]");
}

else {
/*
* if (dailyval_Log.equalsIgnoreCase("YES")) {
*
* // System.out.println(
* "FX deal rate is outside specified tolerance else " +
* count); }
*/
}

} catch (Exception e) {
// System.out.println("Exception" + e.getMessage());
if (dailyval_Log.equalsIgnoreCase("YES")) {

System.out
.println("Exception FX deal rate is outside specified tolerance"
        + e.getMessage());
}
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
// // System.out.println("Connection Failed! Check output
// // console");
e.printStackTrace();
}
}

// System.out.println("payment Type action---->" + payaction);
tranType = getWrapper().getTRANTYP();
// System.out.println("Write of Type---->" + tranType +
// "tranType---->" + tranType.length());
if (tranType.equalsIgnoreCase("WRITEOFF")
&& payaction.equalsIgnoreCase("Pay")
&& prd_typ.equalsIgnoreCase("ELF")
&& !step_Id.equalsIgnoreCase("CSM")) {
getPane().setEBRCFLAG("N");
} else if ((tranType.equalsIgnoreCase("") || !tranType
.equalsIgnoreCase("WRITEOFF"))
&& !payaction.equalsIgnoreCase("Pay")
&& prd_typ.equalsIgnoreCase("ELF")
&& !step_Id.equalsIgnoreCase("CSM")) {
getPane().setEBRCFLAG("N");
} else if ((tranType.equalsIgnoreCase("") || !tranType
.equalsIgnoreCase("WRITEOFF"))
&& payaction.equalsIgnoreCase("Pay")
&& prd_typ.equalsIgnoreCase("ELF")
&& !step_Id.equalsIgnoreCase("CSM")) {
getPane().setEBRCFLAG("Y");

} else if (step_Id.equalsIgnoreCase("CSM")) {
getPane().setEBRCFLAG("");
}

// TRANTYP

String paymentExt = "PAYEXT";
String paymentVal = "";
double paymentValue = 0;
try {
AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryValue = getDriverWrapper()
.createQuery("EXTGENCUSTPROP",
  "PROPNAME='" + paymentExt + "'");
// //System.out.println("ADDDailyTxnLimit initially-------->");
EXTGENCUSTPROP val = queryValue.getUnique();
if (val != null) {

paymentVal = val.getPropval();

paymentValue = Double.valueOf(paymentVal);
// System.out.println("payment Value date ===>" +
// paymentValue);
}

double datedouble = 0;
String tenorExt = getWrapper().getTENEXT();
if (getMajorCode().equalsIgnoreCase("FRN")
&& prd_typ.equalsIgnoreCase("ELF")
&& getMinorCode().equalsIgnoreCase("POF")
&& payaction.equalsIgnoreCase("Pay")
&& !tenorExt.equalsIgnoreCase("Yes")
&& (formtyp.equalsIgnoreCase("SOFTEX") || formtyp
  .equalsIgnoreCase("EDI"))) {
List<ExtEventShippingTable> bill = (List<ExtEventShippingTable>) getWrapper()
.getExtEventShippingTable();

SimpleDateFormat parseFormat = new SimpleDateFormat(
"yyyy-MM-dd");
SimpleDateFormat parseFormat1 = new SimpleDateFormat(
"EEE MMM dd HH:mm:ss z yyyy");
// SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yy");

List<Date> list = new ArrayList<Date>();
SimpleDateFormat formatter1 = new SimpleDateFormat(
"dd/MM/yyyy");
for (int l = 0; l < bill.size(); l++) {
ExtEventShippingTable billref = bill.get(l);

// //System.out.println("Shipment date for buyer cradit
// before====>");
String billdat = billref.getBILLDAT().toString();

if (billdat.length() > 0 && billdat != null) {
// //System.out.println("Bill referen date in
// string----->" + billdat);
// //System.out.println("Bill referen date in
// string----->" + billdat.length());

try {
  if (billdat.length() > 10) {
        String result1 = formatter1
                    .format(parseFormat1.parse(billdat));
        // //System.out.println("Bill referen date
        // after
        // result1 if----->" + result1);
        Date dat = (Date) formatter1.parse(result1);
        // //System.out.println("Bill referen date
        // after
        // convert----->" + dat);

        list.add(dat);
  } else {
        String result1 = formatter1
                    .format(parseFormat.parse(billdat));
        // //System.out.println("Bill referen date
        // after
        // result1 else----->" + result1);
        Date dat = (Date) formatter1.parse(result1);
        // //System.out.println("Bill referen date
        // after
        // convert----->" + dat);

        list.add(dat);
  }

} catch (Exception e) {
  System.out
              .println("Exception Bill reference date"
                          + e.getMessage());
}
} else {

// // System.out.println("Shipping bill date is
// // empty");
}

}
// System.out.println("Payment extention======>" +
// tenorExt);
if (list.size() > 0) {
Date comprareDate = ConnectionMaster
  .compareAndReturnDate(list);

String resultdate = formatter1.format(comprareDate);

// System.out.println("Greater Shipping bill======>" +
// resultdate);

try {
String d = getWrapper().getNOSTDAT();
// // System.out.println("Nosrto date for
// // calculation" +
// // d);
String d1 = resultdate;
if (d != null && d1 != null && d1.length() > 0
        && d.length() > 0) {
  String dateval = (difference(d, d1).substring(
              0, difference(d, d1).indexOf(".")));
  // System.out.println("Total calculation shipping bill date value====>"
  // + dateval);
  datedouble = Double.valueOf(dateval);
} else {
  // // System.out.println("else part of date
  // // difference values");
}
} catch (Exception e) {
// System.out.println("exeception date difference
// values" + e);
}

if (resultdate.length() > 0
  && datedouble > paymentValue
  && (step_Input.equalsIgnoreCase("i"))
  && (step_Id.equalsIgnoreCase("CBS Maker"))
  && getMinorCode().equalsIgnoreCase("POF")
  && !tenorExt.equalsIgnoreCase("Yes")
  && (formtyp.equalsIgnoreCase("SOFTEX") || formtyp
              .equalsIgnoreCase("EDI"))) {

validationDetails
        .addError(
                    ErrorType.Other,
                    "Kindly make payment extension since payment receive date (Nostro value date, "
                                + datedouble
                                + ") is exceeding "
                                + paymentValue
                                + " days from shipping bill date  [CM]");

} else {
// System.out.println("payment extension values" +
// datedouble + "paymentValue" + paymentValue);
}
// Payment write off
double dateWrite = 0;
if (tranType.equalsIgnoreCase("WRITEOFF")) {
try {
  String sysdate = getDriverWrapper()
              .getEventFieldAsText("TDY", "d", "");
  // // System.out.println("Nosrto date for
  // // calculation" +
  // // d);
  String d1 = resultdate;

  if (sysdate != null && d1 != null
              && d1.length() > 0
              && sysdate.length() > 0) {
        String dateval = (difference(sysdate, d1)
                    .substring(0,
                                difference(sysdate, d1)
                                            .indexOf(".")));
        // // System.out.println("Total calculation
        // // shipping
        // // bill date value====>" + dateval);
        dateWrite = Double.valueOf(dateval);
  } else {
        // // System.out.println("else part of date
        // // difference values");
  }
} catch (Exception e) {
  // System.out.println("exeception date
  // difference
  // values" + e);
}
if (resultdate.length() > 0
        && dateWrite > paymentValue
        && (step_Input.equalsIgnoreCase("i"))
        && (step_Id.equalsIgnoreCase("CBS Maker"))
        && getMinorCode().equalsIgnoreCase("POF")
        && !tranType.equalsIgnoreCase("WRITEOFF")
        && !tenorExt.equalsIgnoreCase("Yes")
        && (formtyp.equalsIgnoreCase("SOFTEX") || formtyp
                    .equalsIgnoreCase("EDI"))) {
  // System.out.println("Inside ilcpoc 6738");
  validationDetails
              .addError(
                          ErrorType.Other,
                          "Kindly make payment extension since write off date (System's todays date "
                                      + dateWrite
                                      + ") is exceeding "
                                      + paymentValue
                                      + " days from shipping bill date  [CM]");

}

} else {
// System.out.println("write off date else" +
// dateWrite + "paymentValue" + paymentValue);
}

}

} else {
// // System.out.println("Subproduct type is local");
}
} catch (Exception e) {
// System.out.println("Exception for Notro values getting===>" +
// e);
}

// AdvanceTable date checking
try {
double datedoub = 0;
if (prd_typ.equalsIgnoreCase("ELF")
&& getMinorCode().equalsIgnoreCase("POF")) {
List<ExtEventAdvanceTable> bill = (List<ExtEventAdvanceTable>) getWrapper()
.getExtEventAdvanceTable();
SimpleDateFormat parseFormat1 = new SimpleDateFormat(
"EEE MMM dd HH:mm:ss z yyyy");
// String ds1 = "2007-06-30";
SimpleDateFormat parseFormat = new SimpleDateFormat(
"yyyy-MM-dd");

List<Date> list = new ArrayList<Date>();
SimpleDateFormat formatter1 = new SimpleDateFormat(
"dd/MM/yyyy");
for (int l = 0; l < bill.size(); l++) {
ExtEventAdvanceTable billref = bill.get(l);

// // System.out.println("Shipment date for buyer cradit
// // before====>");
String billdat = billref.getDATREM().toString();

if (billdat.length() > 0 && billdat != null) {

try {
  if (billdat.length() > 10) {
        String result1 = formatter1
                    .format(parseFormat1.parse(billdat));
        // //System.out.println("Bill referen date
        // after
        // result1 if----->" + result1);
        Date dat = (Date) formatter1.parse(result1);
        // //System.out.println("Bill referen date
        // after
        // convert----->" + dat);

        list.add(dat);
  } else {
        String result1 = formatter1
                    .format(parseFormat.parse(billdat));
        // //System.out.println("Bill referen date
        // after
        // result1 else----->" + result1);
        Date dat = (Date) formatter1.parse(result1);
        // //System.out.println("Bill referen date
        // after
        // convert----->" + dat);

        list.add(dat);
  }

} catch (Exception e) {
  // System.out.println("Exception Bill referen
  // date"
  // + e.getMessage());
}
} else {

// // System.out.println("Shipping bill date is
// // empty");
}

}
if (list.size() > 0) {
Date comprareDate = ConnectionMaster
  .compareAndReturnDate(list);

String resultdate = formatter1.format(comprareDate);

// // System.out.println("Advance table Greater Shipping
// // bill======>" + resultdate);

String d = "";
try {
con = getConnection();
String query = "select TO_CHAR(lcp.PRES_DATE,'DD/MM/YYYY') from master mas, BASEEVENT bev,LCPAYMENT lcp where mas.KEY97 = bev.MASTER_KEY and bev.KEY97 = lcp.KEY97 and mas.REFNO_PFIX = 'ELC' and bev.REFNO_PFIX = 'DPR' and mas.MASTER_REF = '"
        + MasterReference
        + "' AND PRES_DATE IS NOT NULL";
// //System.out.println("presantation date take===>"
// +
// query);

ps1 = con.prepareStatement(query);
rs1 = ps1.executeQuery();
if (rs1.next()) {
  d = rs1.getString(1);
  // // System.out.println("presantation date in
  // // while
  // // loop===>" + d);
}
} catch (Exception e1) {
// System.out.println("Exception presantation date"
// +
// e1.getMessage());
}

try {

// // System.out.println("presentation date for
// // calculation" + d);
String d1 = resultdate;
if (d != null && d1 != null && d1.length() > 0
        && d.length() > 0) {
  String dateval = (difference(d, d1).substring(
              0, difference(d, d1).indexOf(".")));
  // // System.out.println("Advance table Total
  // // calculation date value====>" + dateval);
  datedoub = Double.valueOf(dateval);
} else {
  // // System.out.println("else part of date
  // // difference values");
}
} catch (Exception e) {
// System.out.println("exeception date difference
// values" + e);
}

if (resultdate.length() > 0
  && datedoub > paymentValue
  && (step_Input.equalsIgnoreCase("i"))
  && (step_Id.equalsIgnoreCase("CBS Maker") || step_Id
              .equalsIgnoreCase("CBS Maker 1"))) {

validationDetails.addWarning(
        ValidationDetails.WarningType.Other,
        "Advance payment received date is exceeding 9 months ("
                    + datedoub + ")> " + paymentValue
                    + " [CM]");
}

} else {
// // System.out.println("Advance table Total
// calculation
// // date value list size less than 1====>");

}
} else {
// // System.out.println("Subproduct type is local");
}
} catch (Exception e) {
// System.out.println("Exception for Notro values getting===>" +
// e);
}

if (getMinorCode().equalsIgnoreCase("POF")
&& prd_typ.equalsIgnoreCase("ELF")) {
try {

String theirRef = getDriverWrapper().getEventFieldAsText(
"THE", "r", "").trim();
String nostref_MT103102 = getWrapper().getNOSTMT().trim();
String nostref_MT940 = getWrapper().getNOSTRM().trim();
String query = "";
if (dailyval_Log.equalsIgnoreCase("YES")) {
System.out.println("Nostro ref no nostref_MT103102"
  + nostref_MT103102);
}
// String nostref_MT940950 =
// getWrapper().getNOSTRM().trim();

if (nostref_MT103102.length() > 0) {

if ((!theirRef.equalsIgnoreCase(nostref_MT103102))
  && (step_Input.equalsIgnoreCase("i"))
  && step_csm.equalsIgnoreCase("CBS Maker")) {
validationDetails
        .addWarning(WarningType.Other,
                    "Nostro Reference number and Received reference number should be same [CM]");

} else {
if (dailyval_Log.equalsIgnoreCase("YES")) {
  System.out
              .println("Nostro ref no nostref_MT103102 else"
                          + nostref_MT103102);
}
}

query = "SELECT count(*) as COUNT FROM ETTV_NOSTRO_MT103_MT202_TBL WHERE MT103_MT202_REFERENCE_NUMBER='"
  + nostref_MT103102 + "'";
if (dailyval_Log.equalsIgnoreCase("YES")) {
System.out
        .println("Nostro ref no nostref_MT103102 query"
                    + query);
}
con = ConnectionMaster.getConnection();
ps = con.prepareStatement(query);
rs = ps.executeQuery();
int value = 0;
if (rs.next()) {
value = rs.getInt(1);

}
query = "select count(*) from ETT_NOSTRO_UTILITY_MT103 M where M.QUEUE_TYPE='CLSQ' and M.REFERENCE_NUMBER='"
  + nostref_MT103102 + "'";
/*
* if (dailyval_Log.equalsIgnoreCase("YES")) {
* System.out
* .println("Nostro ref no nostref_MT103102 query" +
* query); }
*/
con = ConnectionMaster.getConnection();
ps = con.prepareStatement(query);
rs = ps.executeQuery();
int value103 = 0;
if (rs.next()) {
value103 = rs.getInt(1);

}

/*
* if (dailyval_Log.equalsIgnoreCase("YES")) {
* System.out
* .println("Nostro ref no nostref_MT103102 count" +
* value + "value103" + value103); }
*/
// if ((nostref_MT103102.length() > 0) && (value == 0 &&
// value103 == 0)
query = "select count(*) from ETT_NOSTRO_UTILITY_MT202 M where M.QUEUE_TYPE='CLSQ' and M.REFERENCE_NUMBER='"
  + nostref_MT103102 + "'";
/*
* if (dailyval_Log.equalsIgnoreCase("YES")) {
* System.out
* .println("Nostro ref no nostref_MT103102 query" +
* query); }
*/
con = ConnectionMaster.getConnection();
ps = con.prepareStatement(query);
rs = ps.executeQuery();
int value202 = 0;
if (rs.next()) {
value202 = rs.getInt(1);

}

/*
* if (dailyval_Log.equalsIgnoreCase("YES")) {
* System.out
* .println("Nostro ref no nostref_MT103102 count" +
* value + "value202" + value202); }
*/
if ((nostref_MT103102.length() > 0)
  && ((value == 0 && value103 == 0) && (value == 0 && value202 == 0))
  && (step_Input.equalsIgnoreCase("i"))
  && (step_csm.equalsIgnoreCase("BRANCHINPUT")
              || step_csm.equalsIgnoreCase("CSM") || step_csm
                    .equalsIgnoreCase("CBS Maker"))) {
validationDetails.addError(ErrorType.Other,
        "MT103/202 Nostro Reference number is not valid ("
                    + nostref_MT103102 + ") [CM]");

getPane().setNOSTAMT("");
getPane().setNOSTDAT("");
getPane().setPOOLAMT("");
getPane().setNOSTACC("");
getPane().setMTMESG("");
getPane().setINWMSG("");
getPane().setNOSTOUT("");

} else {
/*
* if (dailyval_Log.equalsIgnoreCase("YES")) {
* System.out
* .println("MT103 Nostro Reference else===>" +
* value + "value103===>" + value103); }
*/
}

} else if (nostref_MT940.length() > 0) {
query = "SELECT count(*) FROM ETTV_NOSTRO_MT103_MT202_TBL WHERE MT940_MT950_REFERENCE_NUMBER='"
  + nostref_MT940 + "'";
/*
* if (dailyval_Log.equalsIgnoreCase("YES")) {
* System.out
* .println("Nostro ref no nostref_MT940 query" +
* query); }
*/
con = ConnectionMaster.getConnection();
ps = con.prepareStatement(query);
rs = ps.executeQuery();
int value = 0;
while (rs.next()) {
value = rs.getInt(1);
}
/*
* if (dailyval_Log.equalsIgnoreCase("YES")) {
* System.out
* .println("Nostro ref no nostref_MT940 count" +
* value); }
*/
if ((nostref_MT940.length() > 0)
  && value == 0
  && (step_Input.equalsIgnoreCase("i"))
  && (step_csm.equalsIgnoreCase("BRANCHINPUT")
              || step_csm.equalsIgnoreCase("CSM") || step_csm
                    .equalsIgnoreCase("CBS Maker"))) {
validationDetails.addError(ErrorType.Other,
        "MT940 Nostro Reference number is not valid ("
                    + nostref_MT940 + ") [CM]");
getPane().setNOSTAMT("");
getPane().setNOSTDAT("");
getPane().setPOOLAMT("");
getPane().setNOSTACC("");
getPane().setMTMESG("");
getPane().setINWMSG("");
getPane().setNOSTOUT("");
} else {
/*
* if (dailyval_Log.equalsIgnoreCase("YES")) {
* System.out.println("MT940 Nostro Reference else"
* + value); }
*/
}
}

} catch (Exception e1) {
// System.out.println("Exception for Nostro" +
// e1.getMessage());
/*
* if (dailyval_Log.equalsIgnoreCase("YES")) {
* System.out.println("Exception Nostro ref no validation" +
* e1.getMessage()); }
*/
} finally {
try {
if (rs != null)
rs.close();
if (ps != null)
ps.close();
if (con != null)
con.close();
} catch (SQLException e) {
// System.out.println("Connection Failed! Check output
// console");
e.printStackTrace();
}
}
}

String cust = "";
if (getMajorCode().equalsIgnoreCase("ILC")) {
cust = getDriverWrapper().getEventFieldAsText("PRM", "p", "no")
.trim();
}

else if (getMajorCode().equalsIgnoreCase("FRN")) {

cust = getDriverWrapper().getEventFieldAsText("BEN", "p", "no");
}

String eventREF = getDriverWrapper().getEventFieldAsText("MEVR",
"r", "").trim();

String chargecol = getDriverWrapper().getEventFieldAsText("BOTC",
"l", "");
// //System.out.println("charge account collected " + chargecol);
String acctno = getDriverWrapper().getEventFieldAsText("CHA", "a",
"").trim();
// System.out.println("charge account in event field======>" +
// acctno);
String custval = "";

try {
if (step_Id.equalsIgnoreCase("CBS Maker")
|| step_Id.equalsIgnoreCase("CBS Maker 1")) {

con = getConnection();

String dmstlmt = "select TRIM(CUS_MNM) from ETT_CUS_ACCT_SETTLE where MASTER_REF ='"
+ masterref
+ "' and EVENT_REF = '"
+ eventREF
+ "'";
dmsp = con.prepareStatement(dmstlmt);

dmsr = dmsp.executeQuery();
while (dmsr.next()) {

custval = dmsr.getString(1);

if (custval.length() > 0
  && chargecol.equalsIgnoreCase("Y")
  && (!chargecol.equalsIgnoreCase("N"))) {

// // System.out.println("custoemr number in query"
// +
// // custval);
// // System.out.println("custoemr number in
// // transaction" + cust);
validationDetails
        .addWarning(
                    WarningType.Other,
                    "Account selected in Settlement for charges does not belong to the Applicant  [CM]");
} else {
// System.out.println("charge account collected
// matched");
}

}

String dms = "select trim(acc.BO_ACCTNO) from ACCOUNT acc where acc.CUS_MNM ='"
+ cust + "' and acc.BO_ACCTNO='" + acctno + "'";
if (dailyval_Log.equalsIgnoreCase("YES")) {
// System.out.println("ETT_CUS_ACCT_SETTLE QUERY====> "
// + dms);
}
dmsp = con.prepareStatement(dms);

dmsr = dmsp.executeQuery();
if (dmsr.next()) {
// // System.out.println("custoemr number in query if
// // loop----> " + dmsr.getString(1));
custval = dmsr.getString(1);

// // System.out.println("custoemr number in transaction
// // if loop" + acctno);

}

else {
// // System.out.println("custoemr number in query else
// // loop" + custval);
// // System.out.println("custoemr number in transaction
// // else loop" + acctno);
if (acctno.length() > 0
  && chargecol.equalsIgnoreCase("Y")
  && (!chargecol.equalsIgnoreCase("N"))) {

if ((step_Id.equalsIgnoreCase("CBS Maker") || step_Id
        .equalsIgnoreCase("CBS Maker 1"))
        && (step_Input.equalsIgnoreCase("i"))) {

  validationDetails
              .addWarning(
                          WarningType.Other,
                          "Account selected for princial and charges does not belong to the Applicant [CM]");

} else {
  // // System.out.println("Not CBS Maker for
  // // charge account");
}
} else {
// // System.out.println("charge account collected
// // check box in else " + chargecol);
}
}

} else {
// //
// System.out.println("Not CBS Maker for charge account");
}

} catch (Exception e) {
// System.out.println("charge account collected----->" +
// e.getMessage());
} finally {
try {
if (dmsr != null)
dmsr.close();
if (dmsp != null)
dmsp.close();
if (con != null)
con.close();
} catch (SQLException e) {
// // System.out.println("Connection Failed! Check output
// // console");
e.printStackTrace();
}
}

// NPA customer

try {

String productVal = "N";
if (getMajorCode().equalsIgnoreCase("ILC")) {
productVal = getDriverWrapper().getEventFieldAsText("APP",
"p", "cAJB");
}

else if (getMajorCode().equalsIgnoreCase("FRN")
|| getMajorCode().equalsIgnoreCase("EGT")) {

productVal = getDriverWrapper().getEventFieldAsText("BEN",
"p", "cAJB");
} else if (getMajorCode().equalsIgnoreCase("ODC")) {
productVal = getDriverWrapper().getEventFieldAsText("DRW",
"p", "cAJB");
}

if (dailyval_Log.equalsIgnoreCase("YES")) {
// System.out.println("NPA customer-----> " + productVal);
}
if (productVal.equalsIgnoreCase("Y")
&& (step_Input.equalsIgnoreCase("i"))
&& (step_Id.equalsIgnoreCase("AdhocCSM")
  || step_Id.equalsIgnoreCase("CSM")
  || step_Id.equalsIgnoreCase("CBS Maker") || step_Id
        .equalsIgnoreCase("CBS Maker 1"))) {
validationDetails.addWarning(WarningType.Other,
"Customer is a NPA customer [CM]");
} else {
if (dailyval_Log.equalsIgnoreCase("YES")) {
// System.out.println("Not a NPA customer-----> " +
// productVal);
}

}

} catch (Exception e) {
if (dailyval_Log.equalsIgnoreCase("YES")) {
System.out.println("Exception NPA customer-----> "
+ e.getMessage());
}
}

// The master has already shipping guarantee issued validation
try {
// //System.out.println("Shipping guarantee");
if (getDriverWrapper().getEventFieldAsText("EVCD", "s", "")
.trim().equalsIgnoreCase("CRC")) {
// //System.out.println("enter into shipping guarantee
// issued");
// String masterRefNumber =
// getDriverWrapper().getEventFieldAsText("MST", "r", "");
// String stepid =
// getDriverWrapper().getEventFieldAsText("CSID", "s", "");
// //System.out.println("step is information" + stepid);
if ((step_Input.equalsIgnoreCase("i"))
&& (step_Id.equalsIgnoreCase("CBS Maker") || step_Id
        .equalsIgnoreCase("CBS Maker 1"))) {
String qr = "select count(*) from master m, baseevent b where     m.key97 = b.master_key and b.refno_pfix = 'LSC' and b.status = 'c' and m.master_ref='"
  + masterRefNumber + "'";
// //System.out.println("Query " + qr);
con = ConnectionMaster.getConnection();
ps1 = con.prepareStatement(qr);
// //System.out.println(qr);
rs = ps1.executeQuery();
while (rs.next()) {
String k = rs.getString(1);
// //System.out.println("while of count of hscode "
// +k);
int ka = Integer.parseInt(k);
if (ka == 0) {

} else {
  validationDetails
              .addWarning(
                          ValidationDetails.WarningType.Other,
                          "Shipping guarantee exists for this Lc  [CM]");
}
}
// con.close();
// peco.close();
// rs.close();
}
} else {
// // System.out.println("shipping guarantee issued");
}
} catch (Exception e) {
// //System.out.println("exception caught " +e.getMessage() );
} finally {
try {
if (rs != null)
rs.close();
if (ps1 != null)
ps1.close();
if (con != null)
con.close();
} catch (SQLException e) {
// // System.out.println("Connection Failed! Check output
// // console");
e.printStackTrace();
}
}

try {
int noOfRecord = 0;
con = getConnection();
String customerId = "";
String amount_am = "";
String currency = "";
String masterRef = "";
customerId = getDriverWrapper().getEventFieldAsText("APP", "p",
"no").trim();
// //System.out.println("Customer Id " + customerId);
amount_am = getDriverWrapper().getEventFieldAsText("AMT", "v",
"m");
String amount = amount_am.replaceAll("[^0-9]", "");
// //System.out.println("Currency is " + amount_am);
currency = getDriverWrapper().getEventFieldAsText("AMT", "v",
"c");
// //System.out.println("Currency is " + currency);
masterRef = getmasRefNo();
// //System.out.println("Master Reference " + masterRef);
// //System.out.println("Entered Validate method in CpcoPcoc
// class");
if (amount_am.length() > 0 && currency.length() > 0) {

// //System.out.println("Duplicate Record Query is " +
// duplicateMaster);

String duplicateMaster = "select COUNT(*) as COUNT from ETT_DUPLICAT_WARNING_VIEW where PRICUSTMNM='"
+ customerId
+ "' and AMOUNT='"
+ amount
+ "' and CCY='"
+ currency
+ "' and MASTER_REF !='"
+ MasterReference + "'";
if (dailyval_Log.equalsIgnoreCase("YES")) {

System.out.println("Duplicate Record Query is "
  + duplicateMaster);
}
ps1 = con.prepareStatement(duplicateMaster);
rs1 = ps1.executeQuery();
if (rs1.next()) {
noOfRecord = rs1.getInt("COUNT");
}
// //System.out.println("No of Existing records is " +
// noOfRecord);
if ((noOfRecord == 1 || noOfRecord > 0)
&& (step_Input.equalsIgnoreCase("i"))
&& (step_csm.equalsIgnoreCase("CSM")
        || step_csm.equalsIgnoreCase("CBS Maker")
        || step_csm.equalsIgnoreCase("CSM Reject")
        || step_csm.equalsIgnoreCase("CBS Reject") || step_csm
              .equalsIgnoreCase("AdhocCSM"))) {
validationDetails
  .addWarning(
              WarningType.Other,
              "There is an existing transaction for the Same Customer,Amount and Currency [CM]");
} else {
if (dailyval_Log.equalsIgnoreCase("YES")) {

System.out.println("Duplicate Record in else "
        + noOfRecord);
}
}
}
// connection.close();
} catch (Exception e) {

if (dailyval_Log.equalsIgnoreCase("YES")) {

System.out.println("Exception in Duplicate master "
+ e.getMessage());
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
// // System.out.println("Connection Failed! Check output
// // console");
e.printStackTrace();
}
}

try {

String query5 = "select count(*),trim(mas.PRICUSTMNM),ref.STATUS,ref.REFERRAL_STATUS from MASTER mas,ETT_REFERRAL_TRACKING ref where trim(mas.MASTER_REF)=ref.MASTER_REF_NO and mas.MASTER_REF='"
+ MasterReference
+ "' and ref.REFERRAL_STATUS='PEND' group by mas.PRICUSTMNM,ref.STATUS,ref.REFERRAL_STATUS";
if (dailyval_Log.equalsIgnoreCase("YES")) {
System.out
.println("Query REFERRAL_STATUS pending" + query5);
}

con = getConnection();
pst = con.prepareStatement(query5);

rs = pst.executeQuery();
if (rs.next()) {
// int counter = getInt(1);

if (dailyval_Log.equalsIgnoreCase("YES")) {
// System.out.println("Pending referal status count if loop");
}

if (step_Input.equalsIgnoreCase("i")
&& (step_csm.equalsIgnoreCase("CSM")
        || step_csm.equalsIgnoreCase("AdhocCSM") || step_csm
              .equalsIgnoreCase("CBS Maker"))) {
validationDetails.addWarning(WarningType.Other,
  "Past pending deferral for same client [CM]");
}

} else {
if (dailyval_Log.equalsIgnoreCase("YES")) {
// System.out.println("No pending referal status" +
// query5);
}

}

} catch (Exception e) {
if (dailyval_Log.equalsIgnoreCase("YES")) {
System.out.println("ExceptionPending referal status"
+ e.getMessage());
}
}
try {

String char_val = getWrapper().getBENNAME_Name();
if (char_val != null && char_val.length() > 0) {
int counter = 0;
for (int i = 0; i < char_val.length(); i++) {
if (!Character.toString(char_val.charAt(i)).matches(
  "^[a-zA-Z\\s]*$")) {
counter++;
}
}

if (counter > 0 && (step_Input.equalsIgnoreCase("i"))
&& (step_csm.equalsIgnoreCase("CBS Maker"))) {
validationDetails
  .addError(ErrorType.Other,
              "Special characters should not be in Beneficiary name [CM]");
}
}

String ben_val = getWrapper().getBADDRE_Name();
if (ben_val != null && ben_val.length() > 0) {
int counter_val = 0;
for (int i = 0; i < ben_val.length(); i++) {
if (!Character.toString(ben_val.charAt(i)).matches(
  "^[a-zA-Z0-9\\s]*$")) {
counter_val++;
}
}

if (counter_val > 0 && (step_Input.equalsIgnoreCase("i"))
&& (step_csm.equalsIgnoreCase("CBS Maker"))) {
validationDetails
  .addError(ErrorType.Other,
              "Special characters should not be in Beneficiary Address [CM]");
}
}

String acc_val = getWrapper().getBENACC_Name();
if (acc_val != null && acc_val.length() > 0
&& (!acc_val.matches("[a-zA-Z0-9]+"))
&& (step_Input.equalsIgnoreCase("i"))
&& (step_csm.equalsIgnoreCase("CBS Maker"))) {
validationDetails
.addError(ErrorType.Other,
        "Special characters should not be in Beneficiary Account no [CM]");
}

} catch (Exception e) {
if (dailyval_Log.equalsIgnoreCase("YES")) {
System.out
.println("Exception pecial characters checking-----> "
        + e.getMessage());
}
}

// Notes Populated in Summary

try {
con = getConnection();
String query = "select * from master mas,NOTE, TIDATAITEM tid WHERE mas.KEY97 = tid.MASTER_KEY AND tid.KEY97 = NOTE.KEY97 AND (NOTE.TYPE NOT IN (3,129,1089) or NOTE.TYPE is null) AND note_event IS NOT NULL AND NOTE.ACTIVE = 'Y' AND mas.MASTER_REF = '"
+ MasterReference + "' ";
// //
// System.out.println("Notes Populated in Summary query====> "
// // + query);

ps1 = con.prepareStatement(query);
rs1 = ps1.executeQuery();
while (rs1.next()) {
String notes = rs1.getString(1);
if (notes.length() > 0
&& (step_Input.equalsIgnoreCase("i"))
&& (step_csm.equalsIgnoreCase("CSM")
        || step_csm.equalsIgnoreCase("CBS Maker") || step_Id
              .equalsIgnoreCase("CBS Maker 1"))) {
validationDetails
  .addWarning(WarningType.Other,
              "Notes Populated in Summary. Kindly check [CM]");
}

}

} catch (Exception e1) {
// System.out.println("Exception" + e1.getMessage());
} finally {
try {
if (rs1 != null)
rs1.close();
if (ps1 != null)
ps1.close();
if (con != null)
con.close();
} catch (SQLException e) {
// // System.out.println("Connection Failed! Check output
// // console");
e.printStackTrace();
}
}
String rtgs = getWrapper().getPROREMT();
String rtgspart = getWrapper().getRTGSPART();

// unbalanced posting error
try {
if ((step_csm.equalsIgnoreCase("CBS Authoriser") || step_csm
.equalsIgnoreCase("Authorise"))
&& (rtgs.equalsIgnoreCase("RTG") || rtgs
  .equalsIgnoreCase("NEF"))
&& rtgspart.equalsIgnoreCase("FULL")
&& (prd_typ.equalsIgnoreCase("ICF"))) {
// System.out.println("try Unbalanced posting valid");
try {
con = getConnection();
String query = "SELECT * FROM KMB_RTGS_NEFT_ACC_VALID_VIEW WHERE MASTER_REF = '"
  + MasterReference
  + "' AND REFNO_PFIX = '"
  + evnt
  + "' AND REFNO_SERL = '"
  + evvcount
  + "'";
// System.out.println("Unbalanced posting error" +
// query);
// int count = 0;
ps1 = con.prepareStatement(query);
rs1 = ps1.executeQuery();
if (rs1.next()) {
// System.out.println("Unbalanced posting valid in
// if loop");
} else if ((step_csm.equalsIgnoreCase("CBS Authoriser") || step_csm
  .equalsIgnoreCase("Authorise"))
  && (rtgs.equalsIgnoreCase("RTG") || rtgs
              .equalsIgnoreCase("NEF"))) {

validationDetails.addError(ErrorType.Other,
        "Please enter RTGS account number [CM]");
} else {
// System.out.println("Unbalanced posting valid in
// else loop");
}
} catch (Exception e1) {
// System.out.println("Exception Unbalanced posting
// error" + e1.getMessage());
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
// System.out.println("Connection Failed! Check
// output console");
e.printStackTrace();
}
}
} else {
// System.out.println("Please enter RTGS account in else");

}
} catch (Exception e1) {
// System.out.println("Exception Unbalanced posting===>" +
// e1.getMessage());
}

try {

if (rtgs.equalsIgnoreCase("NEF")) {

String NEFTHOLY = "NEFTWorking";
String NEFTTime = "";
AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryResult = getDriverWrapper()
.createQuery("EXTGENCUSTPROP",
        "PROPNAME='" + NEFTHOLY + "'");
// //System.out.println("ADDDailyTxnLimit
// initially-------->");
EXTGENCUSTPROP querycode = queryResult.getUnique();
if (querycode != null) {

NEFTTime = querycode.getPropval().trim();

} else {
if (dailyval_Log.equalsIgnoreCase("YES")) {
// System.out.println("NEFT value Blank-------->" +
// NEFTTime);
}

}
if (NEFTTime.equalsIgnoreCase("NO")
&& (rtgs.equalsIgnoreCase("NEF"))
&& (step_csm.equalsIgnoreCase("CBS Maker")
        || step_csm
                    .equalsIgnoreCase("CBS Authoriser") || step_csm
              .equalsIgnoreCase("Authorise"))) {
validationDetails
  .addError(ErrorType.Other,
              "Today is holiday. No NEFT entry will proceed [CM]");
} else {
if (dailyval_Log.equalsIgnoreCase("YES")) {
// System.out.println("Today is not holyday so NEFT will avilable");
}
}

} else if (rtgs.equalsIgnoreCase("RTG")) {

String RTGSHOLY = "RTGSWorking";
String RTGSTime = "";
AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryResult = getDriverWrapper()
.createQuery("EXTGENCUSTPROP",
        "PROPNAME='" + RTGSHOLY + "'");
// //System.out.println("ADDDailyTxnLimit
// initially-------->");
EXTGENCUSTPROP querycode = queryResult.getUnique();
if (querycode != null) {

RTGSTime = querycode.getPropval().trim();

} else {
if (dailyval_Log.equalsIgnoreCase("YES")) {
// System.out.println("RTGS value Blank-------->" +
// RTGSTime);
}

}
if (RTGSTime.equalsIgnoreCase("NO")
&& (rtgs.equalsIgnoreCase("RTG"))
&& (step_csm.equalsIgnoreCase("CBS Maker")
        || step_csm
                    .equalsIgnoreCase("CBS Authoriser") || step_csm
              .equalsIgnoreCase("Authorise"))) {
validationDetails
  .addError(ErrorType.Other,
              "Today is holiday. No RTGS entry will proceed [CM]");
} else {
if (dailyval_Log.equalsIgnoreCase("YES")) {
// System.out.println("Today is not holyday so RTGS will avilable");
}
}

}

} catch (Exception e) {
if (dailyval_Log.equalsIgnoreCase("YES")) {
System.out.println("Exception RTGS/NEFT holiday"
+ e.getMessage());
}
}

try {
String NEFT = "NEFTCUTOFFTIME";
String NEFTTime = "";
AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryResult = getDriverWrapper()
.createQuery("EXTGENCUSTPROP",
  "PROPNAME='" + NEFT + "'");
// //System.out.println("ADDDailyTxnLimit initially-------->");
EXTGENCUSTPROP querycode = queryResult.getUnique();
if (querycode != null) {

NEFTTime = querycode.getPropval();
// //System.out.println("NEFTTime time for cut off-------->"
// +
// querycode.getPropval());
} else {
// // System.out.println("NEFTTime time for cut off is
// // empty-------->");

}
// //System.out.println("NEFTTime time for cut off from static
// table-------->" + NEFTTime);
// String RTGSTime = "14:30:47";
DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
Date date = new Date();
// //System.out.println("Current Time " +
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
if (x.before(c1.getTime())
&& rtgs.equalsIgnoreCase("NEF")
&& (step_csm.equalsIgnoreCase("CBS Authoriser")
  || step_csm.equalsIgnoreCase("Authorise") || step_csm
        .equalsIgnoreCase("CSM"))) {
// //System.out.println("RTGS cut off TIME is exceeded");
validationDetails.addError(ErrorType.Other,
"NEFT cut-off time has been exceeded [CM]");
} else {
// // System.out.println("NEFT cut off TIME is avilable");
}
} catch (Exception e) {
// System.out.println("NEFT cut off TIME Exception" +
// e.getMessage());
}

String bankpay = getWrapper().getRTGNFT().trim();
try {
String RTGS = "BANKTOBANK";
String RTGSTime = "";
AdhocQuery<? extends com.misys.tiplus2.customisation.entity.EXTGENCUSTPROP> queryResult = getDriverWrapper()
.createQuery("EXTGENCUSTPROP",
  "PROPNAME='" + RTGS + "'");
// //System.out.println("ADDDailyTxnLimit initially-------->");
EXTGENCUSTPROP querycode = queryResult.getUnique();
if (querycode != null) {

RTGSTime = querycode.getPropval();
// //System.out.println("RTGS time for cut off-------->" +
// querycode.getPropval());
} else {
// // System.out.println("Bank to Bank Payment time for cut
// // off is empty-------->");

}
// //System.out.println("Bank to Bank Payment time for cut off
// from static table-------->" + RTGSTime);
// String RTGSTime = "14:30:47";
DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
Date date = new Date();
// //System.out.println("Current Time " +
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
if (x.before(c1.getTime())
&& bankpay.equalsIgnoreCase("B2B")
&& (step_csm.equalsIgnoreCase("CBS Authoriser")
  || step_csm.equalsIgnoreCase("Authorise") || step_csm
        .equalsIgnoreCase("CSM"))) {
// System.out.println("RTGS cut off TIME is exceeded");
validationDetails
.addError(ErrorType.Other,
        "Bank to Bank Payment cut-off time has been exceeded [CM]");
} else {
// // System.out.println("Bank to Bank Payment cut off TIME
// // is available");
}
} catch (Exception e) {
// System.out.println("Bank to Bank Payment cut off TIME
// Exception" + e.getMessage());
}
// Over due bill exists for this customer
if (step_csm.equalsIgnoreCase("CBS Maker")) {
try {
con = getConnection();
String query = "select * from ETT_OVERDUE_BILCUS_EOD where CUSTOMER_ID= '"
+ cust + "'";

// // System.out.println("Over due bill exists for this
// // customer " + query);
// int count = 0;
ps1 = con.prepareStatement(query);
rs1 = ps1.executeQuery();
while (rs1.next()) {
// //System.out.println("Entered while Over due bill
// exists
// for this customer");

if (step_Input.equalsIgnoreCase("i")
  && (step_csm.equalsIgnoreCase("CBS Maker") || step_Id
              .equalsIgnoreCase("CBS Maker 1"))) {
// //System.out.println("Over due bill exists for
// this
// customer in if loop " + cust);
validationDetails.addWarning(WarningType.Other,
        "Over due bill exists for this customer ("
                    + cust + ")  [CM]");
}

else {
// //System.out.println("Over due bill exists for
// this
// customer in else " + cust);
}
}

} catch (Exception e1) {
// System.out.println("Exception Over due bill" +
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
// // System.out.println("Connection Failed! Check
// output
// // console");
e.printStackTrace();
}
}
}
// crystallization validation
if (prd_typ.equalsIgnoreCase("ELF")) {
try {
String paymentOption = getDriverWrapper()
.getEventFieldAsText("FPP:XPSD", "s", "");
con = getConnection();
String sql = "SELECT mas.MASTER_REF, bev.REFNO_PFIX, bev.REFNO_SERL, mas1.MASTER_REF, bev1.REFNO_PFIX, bev1.REFNO_SERL, exte.REPCRY FROM master mas, BASEEVENT bev, BASEEVENT bev1, master mas1, EXTEVENT exte WHERE mas.KEY97 = bev.MASTER_KEY AND bev.KEY97 = bev1.ATTACHD_EV AND bev1.MASTER_KEY = mas1.KEY97 and bev1.KEY97 = exte.EVENT and bev1.REFNO_PFIX = 'RFS' AND mas.MASTER_REF = '"
+ MasterReference
+ "' and bev.REFNO_PFIX = '"
+ evnt + "' and bev.REFNO_SERL =" + evvcount + "";
// System.out.println("Query value for REPCRY--->" + sql);
ps = con.prepareStatement(sql);
rs = ps.executeQuery();
while (rs.next()) {
String repayCheck = rs.getString(7).trim();
// System.out.println("Repayment value===>" + repayCheck
// + "paymentOption====>" + paymentOption);
if (!paymentOption.equalsIgnoreCase("CRY")
  && repayCheck.equalsIgnoreCase("Y")
  && (step_csm.equalsIgnoreCase("CBS Maker 1"))) {
validationDetails
        .addError(
                    ErrorType.Other,
                    "Repayment check box is ticked in Finance,Please select payment option as Crystallisation [CM]");
} else {
// System.out.println(
// "Repayment value else===>" + repayCheck +
// "paymentOption====>" + paymentOption);
}

}

if (paymentOption.equalsIgnoreCase("CRY")) {

String sql1 = "SELECT doc.PAYSTSCODE, exte.REPCRY, mas.MASTER_REF, mas1.MASTER_REF, bev1.REFNO_PFIX, bev1.REFNO_SERL FROM master mas, BASEEVENT bev, BASEEVENT bev1, master mas1, extevent exte, DOCSPRE doc WHERE mas.KEY97 = bev.MASTER_KEY AND bev.KEY97 = bev1.ATTACHD_EV AND bev1.MASTER_KEY = mas1.KEY97 AND bev1.KEY97 = exte.EVENT AND bev.KEY97 = doc.KEY97 AND mas.MASTER_REF = '"
  + MasterReference
  + "' AND bev.REFNO_PFIX ='"
  + evnt
  + "' AND bev.REFNO_SERL ="
  + evvcount
  + "";
// System.out.println("Query value for
// paymentOption--->" + sql1);
con = getConnection();
ps = con.prepareStatement(sql1);
rs = ps.executeQuery();
while (rs.next()) {
String repayCheck = rs.getString(2).trim();
// System.out.println("Repayment value===>" +
// repayCheck + "paymentOption====>" +
// paymentOption);
if (paymentOption.equalsIgnoreCase("CRY")
        && !repayCheck.equalsIgnoreCase("Y")
        && (step_csm
                    .equalsIgnoreCase("CBS Maker 1"))) {
  validationDetails
              .addError(
                          ErrorType.Other,
                          "Payment option selected as Crystallisation please tick Repayment check box in Finance event [CM]");
} else {
  // System.out.println("Repayment value else===>"
  // + repayCheck + "paymentOption====>" +
  // paymentOption);
}

}
}

} catch (Exception e) {
// // System.out.println("Exception update" +
// // e.getMessage());
} finally {
try {
if (rs != null)
rs.close();
if (ps != null)
ps.close();
if (con != null)
con.close();
} catch (SQLException e) {
// // System.out.println("Connection Failed! Check
// output
// // console");
e.printStackTrace();
}
}
}
// FOB + fright+insurance validation
try {
List<ExtEventInvoicedetailsLC> invoice_shiplc = (List<ExtEventInvoicedetailsLC>) getWrapper()
.getExtEventInvoicedetailsLC();

double fobAmt = 0.0;
double insAmt = 0.0;
double friAmt = 0.0;
double comm = 0.0;
double pack = 0.0;
double discount = 0.0;
double dedut = 0.0;
double discal = 0.0;
double toFobamt = 0.0;
double toFobamt_tot = 0.0;

double inscuramt = 0.0;
BigDecimal inscuramtBig = new BigDecimal(0);
BigDecimal insconamt = new BigDecimal(0);

double fricuramt = 0.0;
BigDecimal fricuramtBig = new BigDecimal(0);
BigDecimal friconamt = new BigDecimal(0);

double commcuramt = 0.0;
BigDecimal commcuramtBig = new BigDecimal(0);
BigDecimal commconamt = new BigDecimal(0);

double packcuramt = 0.0;
BigDecimal packcuramtBig = new BigDecimal(0);
BigDecimal packconamt = new BigDecimal(0);

for (int l = 0; l < invoice_shiplc.size(); l++) {

ExtEventInvoicedetailsLC invoice_ship = invoice_shiplc
.get(l);

fobAmt = fobAmt + invoice_ship.getIFOBAMT().doubleValue();
// System.out.println("FOBAmount ---> " + fobAmt);
String fobcur = invoice_ship.getIFOBAMTCurrency();

insAmt = insAmt + invoice_ship.getINSUAMT().doubleValue();
// System.out.println("INSAmount ----> " + insAmt);
String insccy = invoice_ship.getINSUAMTCurrency();

friAmt = friAmt + invoice_ship.getINVFRAMT().doubleValue();
// System.out.println("friAmtAmount ----> " + friAmt);
String friccy = invoice_ship.getINVFRAMTCurrency();

comm = comm + invoice_ship.getICOMMAMT().doubleValue();
// System.out.println("commision amount ----> " + comm);
String commccy = invoice_ship.getICOMMAMTCurrency();

pack = pack + invoice_ship.getIPKGAMT().doubleValue();
// System.out.println("Paking credit ----> " + pack);
String packccy = invoice_ship.getIPKGAMTCurrency();

discount = discount
+ invoice_ship.getIDISCAMT().doubleValue();
// System.out.println("Discount amount ----> " + discount);
dedut = dedut + invoice_ship.getIDEDUAMT().doubleValue();
// System.out.println("Deduction amount ----> " + dedut);

discal = discount + dedut;
// System.out.println("deduction and discount discal----> "
// + discal);

try {
con = getConnection();
String query = "SELECT ETT_SPOTRATE_CAL('" + fobcur
  + "','" + insccy + "') FROM DUAL";
// System.out.println("insurance amount " + query);

ps1 = con.prepareStatement(query);
rs1 = ps1.executeQuery();
if (rs1.next()) {
inscuramt = rs1.getDouble(1);
inscuramtBig = new BigDecimal(inscuramt);
insconamt = new BigDecimal(insAmt);
insconamt = inscuramtBig.multiply(insconamt);

}

String query1 = "SELECT ETT_SPOTRATE_CAL('" + fobcur
  + "','" + friccy + "') FROM DUAL";
// System.out.println("freight amount " + query1);

ps1 = con.prepareStatement(query1);
rs1 = ps1.executeQuery();
if (rs1.next()) {
fricuramt = rs1.getDouble(1);
fricuramtBig = new BigDecimal(fricuramt);
friconamt = new BigDecimal(friAmt);
friconamt = fricuramtBig.multiply(friconamt);

}

String query2 = "SELECT ETT_SPOTRATE_CAL('" + fobcur
  + "','" + commccy + "') FROM DUAL";
// System.out.println("commission amount " + query2);

ps1 = con.prepareStatement(query2);
rs1 = ps1.executeQuery();
if (rs1.next()) {
commcuramt = rs1.getDouble(1);
commcuramtBig = new BigDecimal(commcuramt);
commconamt = new BigDecimal(comm);
commconamt = commcuramtBig.multiply(commconamt);

}

String query3 = "SELECT ETT_SPOTRATE_CAL('" + fobcur
  + "','" + packccy + "') FROM DUAL";
// System.out.println("package amount " + query3);

ps1 = con.prepareStatement(query3);
rs1 = ps1.executeQuery();
if (rs1.next()) {
packcuramt = rs1.getDouble(1);
packcuramtBig = new BigDecimal(packcuramt);
packconamt = new BigDecimal(pack);
packconamt = packcuramtBig.multiply(packconamt);

}

inscuramt = insconamt.doubleValue();
fricuramt = friconamt.doubleValue();
commcuramt = commconamt.doubleValue();
packcuramt = packconamt.doubleValue();
// System.out.println("inscuramt"+inscuramt);
// System.out.println("fricuramt"+fricuramt);
// System.out.println("commcuramt"+commcuramt);
// System.out.println("packcuramt"+packcuramt);
} catch (Exception e) {
System.out.println("Exception in sum of fob"
  + e.getMessage());
} finally {
surrenderDB(con, ps1, rs1);
}

Double totalInvoiceAmt = fobAmt + inscuramt + fricuramt
+ commcuramt + packcuramt;
// System.out.println("totalInvoiceAmt"+totalInvoiceAmt);
ConnectionMaster connectionMaster = new ConnectionMaster();
double divideByDecimal = connectionMaster
.getDecimalforCurrency(fobcur);
// System.out.println("divideByDecimal"+divideByDecimal);
toFobamt_tot = totalInvoiceAmt / divideByDecimal;

// System.out.println("toFobamt_tot"+toFobamt_tot);

toFobamt = (toFobamt_tot - (discal / divideByDecimal));
// System.out.println("toFobamt"+toFobamt);

// System.out.println("am==="+am);
String finval = String.format("%.2f", am);
double finvalue = Double.valueOf(finval);
// System.out.println("finvalue"+finvalue);

String totval = String.format("%.2f", toFobamt);
double totvalue = Double.valueOf(totval);

// System.out.println("toFobamt"+toFobamt);
// System.out.println("totvalue"+totvalue);

if (totvalue > 0
&& totvalue != finvalue
&& (step_Input.equalsIgnoreCase("i"))
&& (step_Id.equalsIgnoreCase("CBS Maker") || step_Id
        .equalsIgnoreCase("CBS Maker 1"))
&& getMinorCode().equalsIgnoreCase("FNF")) {
validationDetails
  .addWarning(
              WarningType.Other,
              "Sum of FOB + freight+ insurance+commission+package amount minus (Discount + deduction amt)("
                          + totvalue
                          + ") should be equal to shipping bill amount ("
                          + finvalue + ") [CM]");

}
}
} catch (Exception e) {
// System.out.println("Exception is Advance Table Validation
// in customer" + e.getMessage());
}

// Payment amount exceeds available amount

try {

String avalibaleAmt = getDriverWrapper().getEventFieldAsText(
"FOA", "v", "m");
String presentAmt = getDriverWrapper().getEventFieldAsText(
"AMPR", "v", "m");
BigDecimal avalibaleAmount = new BigDecimal(0);
BigDecimal presentAmount = new BigDecimal(0);

try {
avalibaleAmount = new BigDecimal(avalibaleAmt);
} catch (Exception e) {
avalibaleAmount = new BigDecimal(0);
}

try {
presentAmount = new BigDecimal(presentAmt);
} catch (Exception e) {
presentAmount = new BigDecimal(0);
}

if (dailyval_Log.equalsIgnoreCase("YES")) {
System.out
.println("Available amount of ELC" + avalibaleAmt);
System.out.println("Presented amount of ELC"
+ presentAmount);
}

if ((avalibaleAmount.compareTo(presentAmount) < 0)
&& (step_Input.equalsIgnoreCase("i"))
&& (step_csm.equalsIgnoreCase("CBS Maker"))
&& getMinorCode().equalsIgnoreCase("FNF")) {
validationDetails
.addError(ErrorType.Other,
        "Presented amount is should not greater than the Available amount  [CM]");

} else {

if (dailyval_Log.equalsIgnoreCase("YES")) {
System.out.println("Available amount is less"
  + avalibaleAmt + "presented amount===>"
  + presentAmount);
}
}

} catch (Exception e) {

if (dailyval_Log.equalsIgnoreCase("YES")) {
System.out
.println("Exception Available amount is less presented amount===>"
        + e.getMessage());
}

}

String advpaymentmade = getDriverWrapper().getEventFieldAsText(
"cAJO", "l", "");
String dateofremitt = getWrapper().getDATEREM();
String remittrefnoadv = getWrapper().getREMREFAP();
String shipdate = getWrapper().getDASHIP_Name();
String advpaymentreceived = getDriverWrapper().getEventFieldAsText(
"cAJR", "l", "");

if (getMajorCode().equalsIgnoreCase("FRN")
&& advpaymentreceived.equalsIgnoreCase("Y")) {

if (dateofremitt == null || dateofremitt.equalsIgnoreCase("")) {
validationDetails.addError(ErrorType.Other,
"Please enter date of remittance [CM]");
}
}

if (getMajorCode().equalsIgnoreCase("FRN")
&& advpaymentreceived.equalsIgnoreCase("Y")) {
if (remittrefnoadv == null
|| remittrefnoadv.equalsIgnoreCase("")) {
validationDetails
.addError(ErrorType.Other,
        "Please enter remittance reference number for advance payment [CM]");
}
}

if ((step_Id.equalsIgnoreCase("CBS Maker 1")||step_csm.equalsIgnoreCase("CBS Reject")||step_csm.equalsIgnoreCase("CBS Authoriser")) &&
((getMajorCode().equalsIgnoreCase("FRN")
&& (getMinorCode().equalsIgnoreCase("FNF") || getMinorCode().equalsIgnoreCase("POF")))
|| (getMajorCode().equalsIgnoreCase("ILC") && (getMinorCode().equalsIgnoreCase("POC"))))) {
// String forEvent = getFORCEDEBIT();
String forFin = getFORCEDEBITFIN();

String TransForceDebit = getDriverWrapper().getEventFieldAsText("cAHW", "l", "").trim();
// String
// TransForceDebitFinance=getDriverWrapper().getEventFieldAsText("cAHW",
// "l","").trim();
//    System.out.println("Force Debit in main event " + TransForceDebit);
//System.out.println("Force Debit in Finance " + forFin);

if (TransForceDebit.equalsIgnoreCase("N") && forFin.equalsIgnoreCase("Y")) {
if (getMinorCode().equalsIgnoreCase("FNF"))
validationDetails.addError(ErrorType.Other,
  "Force Debit Flag should be ticked in Documents Presented [CM]");
else if (getMinorCode().equalsIgnoreCase("POF"))
validationDetails.addError(ErrorType.Other,
  "Force Debit Flag should be ticked in Outstanding Presentation [CM]");
else if (getMinorCode().equalsIgnoreCase("POC"))
validationDetails.addError(ErrorType.Other,
  "Force Debit Flag should be ticked in Outstanding Claim [CM]");
}

}

//Validation of reference Number in ELC and Freely Neg LC

String issuingBankMnemonic = getDriverWrapper().getEventFieldAsText("ISU",
"p", "cu").trim();
System.out.println("issuingBankMnemonic~~~~~~>"+issuingBankMnemonic);
String issuingBankReference = getDriverWrapper().getEventFieldAsText("ISU",
"p", "r").trim();
System.out.println("issuingBankReference~~~~~~>"+issuingBankReference);
int countOfReference=0;
String masterRefInELC="";
try{
System.out.println("Validation of Reference Number in ELC and FRN");
if(issuingBankMnemonic!=null && !issuingBankMnemonic.isEmpty() && issuingBankReference!=null && !issuingBankReference.isEmpty()){
con=getConnection();
String query1="SELECT COUNT(*),TRIM(MASTER_REF) FROM MASTER WHERE PRICUSTMNM='"+issuingBankMnemonic+"' AND PRI_REF='"+issuingBankReference+"' AND STATUS='LIV' AND REFNO_PFIX='ELC' GROUP BY MASTER_REF";
System.out.println("Query String~~~~>"+query1);
ps1=con.prepareStatement(query1);
dmsr = ps1.executeQuery();
while (dmsr.next()) {                                             
countOfReference=dmsr.getInt(1);
System.out.println("countOfReference~~~~~>"+countOfReference);
masterRefInELC=dmsr.getString(2).trim();
System.out.println("masterRef~~~~~>"+masterRefInELC);
}
if(countOfReference>0){
validationDetails.addError(ErrorType.Other,
"LC is advised by us under ref "+masterRefInELC+".Please lodge documents under Export LC Module instead of Freely Negotiable");
}

}
}catch(Exception e){
System.out.println("Exception in validation of same reference number in ELC~~~~>"+e.getMessage());
e.printStackTrace();
}
finally{
surrenderDB(con,ps1,dmsr);
}
try {

if (getMinorCode().equalsIgnoreCase("POF")
&& (gateWayVal.equalsIgnoreCase("Y")))
{
String orgRefNumber = getDriverWrapper().getEventFieldAsText("CLM", "r", "no");
String billRefNumber = getDriverWrapper().getEventFieldAsText("cANV", "s", "");
if (orgRefNumber != null && billRefNumber != null) {
if (orgRefNumber != "" && billRefNumber != "") {
String firstChar = (orgRefNumber.trim()).substring(0, 1);
if (dailyval_Log.equalsIgnoreCase("YES")) {
  //System.out.println("org ref Laste 1 char" + firstChar);
}
String lastChar = (orgRefNumber.trim()).substring(orgRefNumber.length() - 3);
if (dailyval_Log.equalsIgnoreCase("YES")) {
  //System.out.println("org ref Laste 3 char" + lastChar);
}
String OrgRefNumberCon = firstChar + lastChar;
if (dailyval_Log.equalsIgnoreCase("YES")) {
  //System.out.println("org concord value" + OrgRefNumberCon);
}
String billReflast = (billRefNumber.trim()).substring(billRefNumber.length() - 4);
if (dailyval_Log.equalsIgnoreCase("YES")) {
  //System.out.println("Bill Ref Laste 4 char" + billReflast);
}
if ((!OrgRefNumberCon.equalsIgnoreCase(billReflast))
        && (gateWayVal.equalsIgnoreCase("Y") || !gateWayVal.equalsIgnoreCase("N"))
        && (step_csm.equalsIgnoreCase("CBS Maker")
                    || step_csm.equalsIgnoreCase("CBS Maker 1"))) {
  //System.out.println("MasterReference--" + MasterReference);
  if(!MasterReference.equalsIgnoreCase(billRefNumber)){
  validationDetails.addError(ErrorType.Other, "Bill reference number doesn't match [CM]");
  }
//    validationDetails.addError(ErrorType.Other, "Bill reference number doesn't match [CM]");
} else {
  if (dailyval_Log.equalsIgnoreCase("YES")) {
        //System.out.println("Else Bill reference number is match");
  }
}
}
}
}

} catch (Exception e) {

if (dailyval_Log.equalsIgnoreCase("YES")) {
System.out.println("Exception Bill reference number doesn't match");
}

}
String merchanting=getDriverWrapper().getEventFieldAsText("cARQ","l","");
String mertch = getDriverWrapper().getEventFieldAsText("cAXS", "l", "");
if(getMajorCode().equalsIgnoreCase("ILC")||getMajorCode().equalsIgnoreCase("IDC")
||getMajorCode().equalsIgnoreCase("FRN")||getMajorCode().equalsIgnoreCase("ODC"))
{

try{
//String merchanting=getWrapper().getMERTRA().toString();

String merchref=getWrapper().getREMERREF();
//System.out.println("Merchanting------ "+merchanting);
//System.out.println("Merchanting Ref No-----"+merchref);
if(merchanting.equalsIgnoreCase("Y"))
{
if(merchref==null||merchref.equalsIgnoreCase(""))
{
validationDetails.addError(ErrorType.Other,"Enter the merchant trade reference number");
}
}
else
{

}
}
catch(Exception e)
{
System.out.println("Exception in Merchant Trade"+e.getMessage());
}
}
try{


if(mertch.equalsIgnoreCase("Y"))
{
if((getMajorCode().equalsIgnoreCase("ILC")&&getMinorCode().equalsIgnoreCase("POC"))||
(getMajorCode().equalsIgnoreCase("FRN")&&getMinorCode().equalsIgnoreCase("POF")))
{

String compbydate = getDriverWrapper().getEventFieldAsText("cAJF", "d","");
String valuedate=getDriverWrapper().getEventFieldAsText("FPP:XSD", "d", "");
if((valuedate!=null)&&(valuedate.compareTo(compbydate)<0)){
validationDetails.addError(ErrorType.Other,
  "Value date must be less than the merchanting trade completion date [CM]");
}
}
}
}
catch(Exception e)
{
System.out.println("Exception in merchant and valuedate"+e.getMessage());
}

}
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
// System.out.println("randomCorrelationId generate");
return UUID.randomUUID().toString();
}

private String setValueTOString(double d1) {
DecimalFormat df = new DecimalFormat("#.##");
BigDecimal dValue = new BigDecimal(df.format(d1)).setScale(2,
RoundingMode.HALF_UP);
return String.valueOf(dValue);
}

/*
* public String NullChecker(String conv) { if (conv.isEmpty() ||
* conv.equalsIgnoreCase("") || conv == null) { conv = "0.0"; } return conv;
* }
*/
public static String difference(String a, String b) throws ParseException {
SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");
Date date1 = format.parse(a);
Date date2 = format.parse(b);
long diff = date2.getTime() - date1.getTime();
double z = Math.abs(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
// //System.out.println("values " + Math.abs(TimeUnit.DAYS.convert(diff,
// TimeUnit.MILLISECONDS)));
return Double.toString(z);

}

public String setxxforparameters(String Value) {
try {
if (Value.equalsIgnoreCase("")) {
Value = "x";
}

} catch (Exception e) {
// System.out.println("Exception in setxxforparameters " +
// e.getMessage());
}
return Value;
}

}